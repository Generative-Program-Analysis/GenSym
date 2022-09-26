#ifndef LLSC_SMT_CHECKER_HEADER
#define LLSC_SMT_CHECKER_HEADER

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
  Self* self() {
    return static_cast<Self*>(this);
  }

  // Self has to define:
  // - push_internal()
  // - pop_internal()
  // - check_model_internal()
  // - construct_expr_internal()
  // - add_constraint_internal()
  // - eval()

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

  solver_result check_model() {
    auto start = steady_clock::now();
    solver_result result = self()->check_model_internal();
    auto end = steady_clock::now();
    ext_solver_time += duration_cast<microseconds>(end - start).count();
    return result;
  }

public:
  using ObjCache = std::unordered_map<PtrVal, Expr>;
  ObjCache objcache;

  using BrCacheKey = std::set<PtrVal>;
  using CexCacheKey = std::set<PtrVal>;

  // TODO: recover use_cexcache
  // TODO: use_brcache

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
  using MCexCache = immer::map_transient<CexCacheKey, Model, hash_BrCacheKey>;
  BrCache br_cache;
  MCexCache mcex_cache;

  // FIXME
  void clear_cache() {
    objcache.clear();
    // clear br_cache, mcex_cache
  }

  // Construct the solver expression with object cache for a Value
  inline const Expr construct_expr(PtrVal e) {
    if (use_objcache) {
      auto fd = objcache.find(e);
      if (fd != objcache.end()) return fd->second;
      auto [it, ins] = objcache.emplace(e, self()->construct_expr_internal(e));
      return it->second;
    } else {
      return self()->construct_expr_internal(e);
    }
  }

  inline void resolve_indep_uf(UnionFind& uf, PtrVal root, BrCacheKey& result, bool add_root = true) {
    if (add_root) result.insert(root);
    auto last_expr = root;
    while (root != uf.next[last_expr]) {
      last_expr = uf.next[last_expr];
      if (!last_expr) break;
      if (!last_expr->to_SymV()->is_var()) result.insert(last_expr);
    }
  }

  Model* query_model(CexCacheKey& conds) {
    Model* m = nullptr;
    if (use_cexcache) {
      auto it = mcex_cache.find(conds);
      if (it != nullptr) {
        cached_query_num += 1;
        m = (Model*) it;
      } else {
        push();
        for (auto& v: conds) self()->add_constraint_internal(construct_expr(v));
        auto result = check_model();
        br_cache.set(conds, result);
        if (result == sat) {
          update_model_cache(conds);
          m = (Model*) mcex_cache.find(conds);
        }
        pop();
      }
    } else {
      push();
      for (auto& v: conds) self()->add_constraint_internal(construct_expr(v));
      auto result = check_model();
      // FIXME: set up return a model
      pop();
    }
    return m;
  }

  inline void gen_default_format(PC& pc, Model* model, unsigned int test_id) {
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

  inline void gen_ktest_format(PC& pc, Model* model, unsigned int test_id, List<SymObj> sym_objs) {
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

  void update_model_cache(BrCacheKey& conds) {
    mcex_cache.set(conds, self()->get_model_internal(conds));
  }

  // interfaces

  virtual solver_result check_cond(PC& pc) override {
    if (!use_solver) return sat;
    br_query_num++;

    auto start = steady_clock::now();
    BrCacheKey indep_pc;
    if (use_cons_indep) resolve_indep_uf(pc.uf, *std::prev(pc.conds.end()), indep_pc);
    else indep_pc.insert(pc.conds.begin(), pc.conds.end());
    auto end = steady_clock::now();
    cons_indep_time += duration_cast<microseconds>(end - start).count();

    auto hit = br_cache.find(indep_pc);
    if (hit) return *hit;

    push();
    for (auto& v: indep_pc) self()->add_constraint_internal(construct_expr(v));
    auto res = check_model();
    br_cache.set(indep_pc, res);
    if (res == sat) update_model_cache(indep_pc);
    pop();
    
    return res;
  }

  virtual BrResult check_branch(PC& pc, PtrVal cond) override {
    if (!use_solver) return std::make_pair(sat, sat);
    br_query_num++;

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
    cons_indep_time += duration_cast<microseconds>(end - start).count();

    BrResult result;
    common.insert(cond);
    auto then_hit = br_cache.find(common);
    if (!pc.contains(cond)) common.erase(cond);
    common.insert(neg_cond);
    auto else_hit = br_cache.find(common);
    if (!pc.contains(neg_cond)) common.erase(neg_cond);

    if (then_hit != nullptr && else_hit != nullptr) {
      // both hit
      cached_query_num += 2;
      result.first = *then_hit; //->second;
      result.second = *else_hit; //->second;
    } else if (then_hit != nullptr) {
      // only "then" branch hits
      cached_query_num += 1;
      result.first = *then_hit; //->second;
      common.insert(neg_cond);
      if (result.first == solver_result::unsat) {
        result.second = solver_result::sat;
        br_cache.set(common, result.second);
      } else {
        push();
        for (auto& v: common) self()->add_constraint_internal(construct_expr(v));
        result.second = check_model();
        br_cache.set(common, result.second);
        if (result.second == sat) update_model_cache(common);
        pop();
      }
      auto then_time1 = steady_clock::now();
      then_br_time1 += duration_cast<microseconds>(then_time1 - end).count();
    } else if (else_hit != nullptr) {
      // only "else" branch hits
      cached_query_num += 1;
      result.second = *else_hit; //->second;
      common.insert(cond);
      if (result.second == solver_result::unsat) {
        result.first = solver_result::sat;
        br_cache.set(common, result.first);
      } else {
        push();
        for (auto& v: common) self()->add_constraint_internal(construct_expr(v));
        result.first = check_model();
        br_cache.set(common, result.first);
        if (result.first == sat) update_model_cache(common);
        pop();
      }
      auto then_time2 = steady_clock::now();
      then_br_time2 += duration_cast<microseconds>(then_time2 - end).count();
    } else {
      // neither hit
      push();
      for (auto& v: common) self()->add_constraint_internal(construct_expr(v));

      push();
      self()->add_constraint_internal(construct_expr(cond));
      result.first = check_model();
      common.insert(cond);
      br_cache.set(common, result.first);
      if (result.first == sat) update_model_cache(common);
      pop();

      if (!pc.contains(cond)) common.erase(cond);
      common.insert(neg_cond);
      if (result.first == solver_result::unsat) {
        result.second = solver_result::sat;
        br_cache.set(common, result.second);
        pop();
      } else {
        self()->add_constraint_internal(construct_expr(neg_cond));
        result.second = check_model();
        br_cache.set(common, result.second);
        if (result.second == sat) update_model_cache(common);
        pop();
      }

      auto then_time3 = steady_clock::now();
      then_br_time3 += duration_cast<microseconds>(then_time3 - end).count();
    }
    end = steady_clock::now();
    mono_solver_time += duration_cast<microseconds>(end - start).count();
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

    Model* m = nullptr;
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
        // TODO: this is a temp fix since FS some how adds concrete values into PC.
        // Remove this line until FS is fixed.
        if (!c->to_SymV()) continue;

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
    // why would this improve the performance?
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

// FIXME: switch/external.hpp still uses check_pc
inline bool check_pc(PC pc) {
  auto result = checker_manager.get_checker().check_cond(pc);
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
