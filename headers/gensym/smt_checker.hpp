#ifndef GS_SMT_CHECKER_HEADER
#define GS_SMT_CHECKER_HEADER

#include "ktest.hpp"

enum solver_result { unsat, sat, unknown };
using BrResult = std::pair<solver_result, solver_result>;

inline std::string check_result_to_string(solver_result res) {
  switch (res) {
    case sat: return "sat";
    case unsat: return "unsat";
    case unknown: return "unknown";
    default: ABORT("wow");
  }
}

class Checker {
public:
  virtual ~Checker() {}
  virtual BrResult check_branch(PC& pc, PtrVal cond) = 0;
  virtual solver_result check_cond(PC& pc) = 0;
  virtual std::pair<bool, UIntData> get_sat_value(PC pc, PtrVal v) = 0;
  virtual void generate_test(SS state) = 0;
};

template <typename Self, typename Expr, typename Model>
class CachedChecker : public Checker {
protected:
  using ObjCache = std::unordered_map<PtrVal, Expr>;
  using BrCacheKey = std::set<PtrVal>;
  using CexCacheKey = BrCacheKey;
  using solver_response = std::pair<solver_result, std::shared_ptr<Model>>;
  using br_response = std::pair<solver_response, solver_response>;

  struct hash_BrCacheKey {
    // Idea: can we use this https://matt.might.net/papers/liang2014godel.pdf?
    size_t operator()(BrCacheKey const& k) const noexcept {
      size_t n = 0;
      for (auto& c : k) n += c->to_SymV()->id;
      return n;
    }
  };

  // TODO: BrCache and MCexCache can be shared among threads
  using BrCache = immer::map_transient<BrCacheKey, solver_result, hash_BrCacheKey>;
  using MCexCache = immer::map_transient<CexCacheKey, std::shared_ptr<Model>, hash_BrCacheKey>;

  ObjCache obj_cache;
  BrCache br_cache;
  MCexCache mcex_cache;

  // Construct the solver expression with object cache for a Value
  inline const Expr construct_expr(PtrVal e) {
    if (use_objcache) {
      auto fd = obj_cache.find(e);
      if (fd != obj_cache.end()) return fd->second;
      auto [it, ins] = obj_cache.emplace(e, self()->construct_expr_internal(e));
      return it->second;
    }
    return self()->construct_expr_internal(e);
  }

  inline const Expr to_expr(PtrVal e) {
    auto start = steady_clock::now();
    auto expr = construct_expr(e);
    auto end = steady_clock::now();
    cons_expr_time += duration_cast<microseconds>(end - start).count();
    return expr;
  }

private:
  Self* self() { return static_cast<Self*>(this); }

  // Self has to define:
  // - push_internal()
  // - pop_internal()
  // - check_model_internal()
  // - construct_expr_internal()
  // - add_constraint_internal()
  // - eval(), eval_model()

  void push() {
    auto start = steady_clock::now();
    self()->push_internal();
    auto end = steady_clock::now();
    ext_solver_time += duration_cast<microseconds>(end - start).count();
  }

  void pop() {
    auto start = steady_clock::now();
    self()->pop_internal();
    auto end = steady_clock::now();
    ext_solver_time += duration_cast<microseconds>(end - start).count();
  }

  void reset() {
    auto start = steady_clock::now();
    self()->reset_internal();
    auto end = steady_clock::now();
    ext_solver_time += duration_cast<microseconds>(end - start).count();
  }

  const solver_result* query_sat_cache(BrCacheKey& conds) {
    if (use_brcache) return br_cache.find(conds);
    return nullptr;
  }

  void update_sat_cache(solver_result& res, BrCacheKey& conds) {
    if (use_brcache) br_cache.set(conds, res);
  }

  solver_response check_model(BrCacheKey& conds) {
    auto start = steady_clock::now();
    solver_response response = self()->check_model_internal(conds);
    update_sat_cache(response.first, conds);
    auto end = steady_clock::now();
    ext_solver_time += duration_cast<microseconds>(end - start).count();
    return response;
  }

  inline void resolve_indep_uf(UnionFind& uf, PtrVal root, BrCacheKey& result, bool add_root = true) {
    auto start = steady_clock::now();
    if (add_root) result.insert(root);
    auto last_expr = root;
    while (root != uf.next[last_expr]) {
      last_expr = uf.next[last_expr];
      if (!last_expr) break;
      if (!last_expr->to_SymV()->is_var()) result.insert(last_expr);
    }
    auto end = steady_clock::now();
    cons_indep_time += duration_cast<microseconds>(end - start).count();
  }

  inline std::shared_ptr<Model> update_model_cache(solver_response& res, CexCacheKey& conds) {
    if (res.first == solver_result::sat) {
      auto m = res.second;
      if (use_cexcache) mcex_cache.set(conds, m);
      return m;
    }
    return nullptr;
  }

  inline void gen_default_format(PC& pc, std::shared_ptr<Model> model, unsigned int test_id) {
    std::stringstream output;
    output << "Query number: " << (test_id+1) << std::endl;
    output << "Query is sat." << std::endl;
    std::stringstream filename;
    filename << "tests/" << test_id << ".test";
    int out_fd = open(filename.str().c_str(), O_RDWR | O_CREAT, 0777);
    if (out_fd == -1) {
      ABORT("Cannot create the test case file, abort.\n");
    }
    for (auto& v : pc.vars) {
      output << v->to_SymV()->name << "="
             << self()->eval_model(model, v) << std::endl;
    }
    int n = write(out_fd, output.str().c_str(), output.str().size());
    close(out_fd);
  }

  inline void gen_ktest_format(PC& pc, std::shared_ptr<Model> model, unsigned int test_id, List<SymObj> sym_objs) {
    KTest b;
    b.numArgs = g_conc_argc;
    b.args = g_conc_argv;
    b.symArgvs = 0;
    b.symArgvLen = 0;
    b.numObjects = sym_objs.size();
    b.objects = new KTestObject[b.numObjects];
    assert(b.objects);
    for (int i = 0; i < b.numObjects; i++) {
      KTestObject* o = &b.objects[i];
      auto& obj = sym_objs[i];
      o->name = new char[obj.name.size() + 1];
      memcpy(o->name, obj.name.c_str(), obj.name.size());
      o->name[obj.name.size()] = 0;
      o->numBytes = obj.size;
      o->bytes = new unsigned char[o->numBytes];
      memset(o->bytes, 0, o->numBytes);
      if (obj.is_whole) {
        // XXX(GW): why obj.size must be 4?
        ASSERT(obj.size == 4, "Bad whole object");
        auto key = make_SymV(obj.name, obj.size*8)->to_SymV();
        auto value = self()->eval_model(model, key);
        memcpy(o->bytes, (char*) &value, o->numBytes);
      } else {
        for (int idx = 0; idx < o->numBytes; idx++) {
          auto key = make_SymV(obj.name + "_" + std::to_string(idx), 8)->to_SymV();
          o->bytes[idx] = self()->eval_model(model, key);
        }
      }
    }

    std::stringstream filename;
    filename << "tests/" << test_id << ".ktest";
    int success = kTest_toFile(&b, filename.str().c_str());

    if (!success)
      ABORT("Failed to write ktest to file");

    for (unsigned i = 0; i < b.numObjects; i++) {
      delete[] b.objects[i].name;
      delete[] b.objects[i].bytes;
    }
    delete[] b.objects;
  }

public:
  void clear_cache() {
    obj_cache.clear();
    br_cache = BrCache();
    mcex_cache = MCexCache();
  }

  std::shared_ptr<Model> query_model(CexCacheKey& conds) {
    std::shared_ptr<Model> m;
    if (use_cexcache) {
      auto it = mcex_cache.find(conds);
      if (it != nullptr) {
        cached_query_num += 1;
        m = *it;
      } else {
        push();
        //for (auto& v: conds) self()->add_constraint_internal(to_expr(v));
        auto response = check_model(conds);
        m = update_model_cache(response, conds);
        pop();
      }
    } else {
      push();
      //for (auto& v: conds) self()->add_constraint_internal(to_expr(v));
      auto response = check_model(conds);
      if (response.first == sat) m = response.second;
      pop();
    }
    return m;
  }

  virtual solver_result check_cond(PC& pc) override {
    if (!use_solver) return sat;
    br_query_num++;

    BrCacheKey indep_pc;
    if (use_cons_indep) resolve_indep_uf(pc.uf, *std::prev(pc.conds.end()), indep_pc);
    else indep_pc.insert(pc.conds.begin(), pc.conds.end());

    auto hit = query_sat_cache(indep_pc);
    if (hit) return *hit;

    push();
    //for (auto& v: indep_pc) self()->add_constraint_internal(to_expr(v));
    auto response = check_model(indep_pc);
    update_model_cache(response, indep_pc);
    pop();

    return response.first;
  }

  // Rewrite the expression val according to given equalities
  PtrVal rewriteExpr(std::map< PtrVal, PtrVal >& equalities, PtrVal val) {
    if (val->to_IntV()) return val;

    auto sym_val = val->to_SymV();
    ASSERT(sym_val, "Rewriting a non-symbolic term");
    auto eq_it = equalities.find(sym_val);
    if (eq_it != equalities.end()) {
      return eq_it->second;
    }
    if (sym_val->is_var()) {
      return sym_val;
    }
    if (sym_val->rator == iOP::op_extract) {
      auto hi = (*sym_val)[1]->to_IntV()->as_signed();
      auto lo = (*sym_val)[2]->to_IntV()->as_signed();
      return bv_extract(rewriteExpr(equalities, (*sym_val)[0]), hi, lo);
    }
    if (sym_val->rands.size() == 1) {
      if (sym_val->rator == iOP::op_trunc) {
        auto from = (*sym_val)[0]->get_bw();
        auto to = sym_val->get_bw();
        return int_op_1(sym_val->rator, rewriteExpr(equalities, (*sym_val)[0]), { from, to });
      }
      return int_op_1(sym_val->rator, rewriteExpr(equalities, (*sym_val)[0]), { sym_val->get_bw() });
    }
    if (sym_val->rands.size() == 2) {
      return int_op_2(sym_val->rator, rewriteExpr(equalities, (*sym_val)[0]), rewriteExpr(equalities, (*sym_val)[1]));
    }
    if (sym_val->rands.size() == 3) {
      return int_op_3(sym_val->rator, rewriteExpr(equalities, (*sym_val)[0]), rewriteExpr(equalities, (*sym_val)[1]), rewriteExpr(equalities, (*sym_val)[2]));
    }
    ABORT("Unknown operation");
  }

  PtrVal simplifyExpr(PC& pc, PtrVal cond) {
    if (cond->to_IntV())
      return cond;

    std::map< PtrVal, PtrVal > equalities;

    auto eq_start = steady_clock::now();

    for (auto &constraint : pc.get_path_conds()) {
      auto sym_cons = constraint->to_SymV();
      if (sym_cons) {
        if (sym_cons->rands.size() == 2 && iOP::op_eq == sym_cons->rator) {
          auto left = (*sym_cons)[0];
          auto right = (*sym_cons)[1];
          if (left->to_IntV()) {
            equalities.insert(std::make_pair(right, left));
          } else if (right->to_IntV()) {
            equalities.insert(std::make_pair(left, right));
          } else {
            equalities.insert(std::make_pair(constraint, make_IntV(1, 1)));
          }
        } else {
          equalities.insert(std::make_pair(constraint, make_IntV(1, 1)));
        }
      }
    }

    auto eq_end = steady_clock::now();
    equality_time += duration_cast<microseconds>(eq_end - eq_start).count();

    if (!equalities.empty()) {
      auto rw_start = steady_clock::now();
      cond = rewriteExpr(equalities, cond);
      auto rw_end = steady_clock::now();
      rewriting_time += duration_cast<microseconds>(rw_end - rw_start).count();
    }

    return cond;
  }

  virtual BrResult check_branch(PC& pc, PtrVal cond) override {
    if (!use_solver) return std::make_pair(sat, sat);
    br_query_num++;
    auto simp_start = steady_clock::now();
    if (use_equality_substitution) {
      cond = simplifyExpr(pc, cond);
    }
    auto simp_end = steady_clock::now();
    simp_expr_time += duration_cast<microseconds>(simp_end - simp_start).count();
    if (cond->to_IntV()) {
      rewrited_query_num++;
      IntData condv = proj_IntV(cond);
      return std::make_pair(0 == condv ? solver_result::unsat : solver_result::sat, 0 == condv ? solver_result::sat : solver_result::unsat);
    }

    auto start = steady_clock::now();
    auto neg_cond = SymV::neg(cond);
    BrCacheKey common;
    if (use_cons_indep) {
      UnionFind uf(pc.uf);
      for (auto& v : cond->to_SymV()->vars) uf.join(v, cond);
      resolve_indep_uf(uf, cond, common, pc.contains(cond));
    } else {
      common.insert(pc.conds.begin(), pc.conds.end());
    }
    auto end = steady_clock::now();

    BrResult result;
    common.insert(cond);
    auto then_hit = query_sat_cache(common);
    if (!pc.contains(cond)) common.erase(cond);
    common.insert(neg_cond);
    auto else_hit = query_sat_cache(common);
    if (!pc.contains(neg_cond)) common.erase(neg_cond);

    if (then_hit != nullptr && else_hit != nullptr) {
      // both hit cache
      cached_query_num += 2;
      result.first = *then_hit;
      result.second = *else_hit;
    } else if (then_hit != nullptr) {
      // only "then" branch hits cache
      cached_query_num += 1;
      result.first = *then_hit;
      common.insert(neg_cond);
      if (result.first == solver_result::unsat) {
        result.second = solver_result::sat;
        update_sat_cache(result.second, common);
      } else {
        push();
        //for (auto& v: common) self()->add_constraint_internal(to_expr(v));
        auto response = check_model(common);
        result.second = response.first;
        update_model_cache(response, common);
        pop();
      }
      auto else_query_time = steady_clock::now();
      else_miss_time += duration_cast<microseconds>(else_query_time - end).count();
    } else if (else_hit != nullptr) {
      // only "else" branch hits cache
      cached_query_num += 1;
      result.second = *else_hit;
      common.insert(cond);
      if (result.second == solver_result::unsat) {
        result.first = solver_result::sat;
        update_sat_cache(result.first, common);
      } else {
        push();
        //for (auto& v: common) self()->add_constraint_internal(to_expr(v));
        auto response = check_model(common);
        result.first = response.first;
        update_model_cache(response, common);
        pop();
      }
      auto then_query_time = steady_clock::now();
      then_miss_time += duration_cast<microseconds>(then_query_time - end).count();
    } else {
      // neither hits cache
      push();
      //for (auto& v: common) self()->add_constraint_internal(to_expr(v));

      //self()->add_constraint_internal(to_expr(cond));
      common.insert(cond);
      auto response = check_model(common);
      result.first = response.first;
      update_model_cache(response, common);
      pop();

      push();
      if (!pc.contains(cond)) common.erase(cond);
      common.insert(neg_cond);
      if (result.first == solver_result::unsat) {
        result.second = solver_result::sat;
        update_sat_cache(result.second, common);
      } else {
        //self()->add_constraint_internal(to_expr(neg_cond));
        response = check_model(common);
        result.second = response.first;
        update_model_cache(response, common);
      }
      pop();

      auto query_both_time = steady_clock::now();
      both_miss_time += duration_cast<microseconds>(query_both_time - end).count();
    }
    end = steady_clock::now();
    br_solver_time += duration_cast<microseconds>(end - start).count();
    return result;
  }

  virtual std::pair<bool, UIntData> get_sat_value(PC pc, PtrVal e) override {
    conc_query_num++;
    auto sym_e = e->to_SymV();
    ASSERT(sym_e != nullptr, "concretizing a non-symbolic value");
    for (auto& v: sym_e->vars) pc.uf.join(v, sym_e);

    CexCacheKey conds;
    if (use_cons_indep) resolve_indep_uf(pc.uf, e, conds, false);
    else conds.insert(pc.conds.begin(), pc.conds.end());
    UIntData data = 0;
    solver_result result;
    auto m = query_model(conds);
    if (m != nullptr) result = sat;
    return std::make_pair(result == sat, result == sat ? self()->eval_model(m, e) : 0);
  }

  virtual void generate_test(SS state) override {
    completed_path_num++;
    if (only_output_covernew && !state.has_cover_new()) return;
    if (!use_solver) return;

    if (mkdir("tests", 0777) == -1) {
      if (errno == EEXIST) { }
      else ABORT("Cannot create the folder tests, abort.\n");
    }

    std::shared_ptr<Model> m;
    CexCacheKey conds(state.get_PC().conds.begin(), state.get_PC().conds.end());
    if (state.get_preferred_cex().size() > 0) {
      // Note(GW): the algorithm resolves preferred cex depending
      // on the traversal order of get_preferred_cex. Since once a preferred cex
      // is hold, it is added and preserved when checking the next preferred cex.
      // This implementation needs to maintain a copy of the original PC every iteration,
      // and adds all previously established "preferred cex" every time. It
      // could be improved if we have implemented a "delete" method for UnionFind.
      CexCacheKey established;

      for (auto& c: state.get_preferred_cex()) {
        PC pc(state.get_PC());
        for (auto& t : established) pc.add(t);
        pc.add(c);
        CexCacheKey pc_pcex;
        if (use_cons_indep) resolve_indep_uf(pc.uf, c, pc_pcex);
        else pc_pcex.insert(pc.conds.begin(), pc.conds.end());
        m = query_model(pc_pcex);
        if (m != nullptr) established.insert(c);
      }
      conds.insert(established.begin(), established.end());
      m = query_model(conds);
    } else {
      m = query_model(conds);
    }
    ASSERT(m != nullptr, "Cannot generate test cases for unsat conditions!");
    generated_test_num++;
    if (output_ktest) gen_ktest_format(state.get_PC(), m, generated_test_num, state.get_sym_objs());
    else gen_default_format(state.get_PC(), m, generated_test_num);
  }
};

#include "smt_stp.hpp"
#include "smt_z3.hpp"

class CheckerManager {
public:
  std::map<std::thread::id, std::unique_ptr<Checker>> checker_map;

  void init_checkers() {
    auto fun = [this](auto id) {
      if (solver_kind == SolverKind::z3) {
        checker_map[id] = std::make_unique<CheckerZ3>();
      } else if (solver_kind == SolverKind::stp) {
        checker_map[id] = std::make_unique<CheckerSTP>();
      } else ABORT("unknown solver");
    };
    fun(std::this_thread::get_id());
    tp.with_thread_ids(fun);
  }

  Checker& get_checker() {
    // why would this improve the performance (with G++)?
    // GW: it seems clang++ doesn't need this trick.
    static std::unique_ptr<Checker> wtf(solver_kind == SolverKind::stp ?  static_cast<Checker*>(new CheckerSTP) : static_cast<Checker*>(new CheckerZ3));
    return *(checker_map[std::this_thread::get_id()]);
  }
};

inline CheckerManager checker_manager;

// To be compatible with generated code:

inline void init_solvers() { checker_manager.init_checkers(); }

inline BrResult check_branch(PC pc, PtrVal cond) {
  auto start = steady_clock::now();
  auto result = checker_manager.get_checker().check_branch(pc, cond);
  auto end = steady_clock::now();
  int_solver_time += duration_cast<microseconds>(end - start).count();
  return result;
}

inline bool check_pc(PC pc) {
  auto start = steady_clock::now();
  auto result = checker_manager.get_checker().check_cond(pc);
  auto end = steady_clock::now();
  int_solver_time += duration_cast<microseconds>(end - start).count();
  return result == solver_result::sat;
}

inline void check_pc_to_file(SS& state) {
  auto start = steady_clock::now();
  checker_manager.get_checker().generate_test(std::move(state));
  auto end = steady_clock::now();
  gen_test_time += duration_cast<microseconds>(end - start).count();
  int_solver_time += duration_cast<microseconds>(end - start).count();
}

inline std::pair<bool, UIntData> get_sat_value(PC pc, PtrVal v) {
  auto start = steady_clock::now();
  auto result = checker_manager.get_checker().get_sat_value(std::move(pc), v);
  auto end = steady_clock::now();
  conc_solver_time += duration_cast<microseconds>(end - start).count();
  int_solver_time += duration_cast<microseconds>(end - start).count();
  return result;
}

#endif
