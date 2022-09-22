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
  virtual std::pair<bool, UIntData> get_sat_value(PC pc, PtrVal v) = 0;
  virtual void generate_test(SS state) = 0;
};

template <typename Self, typename Expr, typename M>
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
  // XXX: SymV could be a compound symbolic expression as well
  using SymVar = simple_ptr<SymV>;
  // a map from symbolic variables to their solver expressions
  using VarMap = std::unordered_map<PtrVal, Expr>; // XXX PtrVal should be sym var
  // a map from symbolic variable to top-level predicate expressions that (transitively) contain this variable
  using ReachMap = std::multimap<PtrVal, PtrVal>; // XXX PtrVal should be sym var
  // object cache type
  // query cache key
  using CexCacheKey = std::set<PtrVal>;
  // assignment of symbolic variables/expr to concrete data
  using Model = std::unordered_map<PtrVal, IntData>; //XXX PtrVal
  // the result of check, accompanying with a model if sat
  using CheckResult = std::pair<solver_result, std::shared_ptr<Model>>;
  // query cache type
  using CexCache = std::map<CexCacheKey, CheckResult>;

  ////////////////////////////////////////////////

  using ObjCache = std::unordered_map<PtrVal, Expr>;
  ObjCache objcache;
  using ObjCacheIter = typename decltype(objcache)::iterator;

  using BrCacheKey = std::set<PtrVal>;

  struct hash_BrCacheKey {
    size_t operator()(BrCacheKey const& k) const noexcept {
      size_t n = 0;
      for (auto& c : k) n += c->to_SymV()->id;
      return n;
    }
  };

  using BrCex = std::pair<solver_result, M>;
  //using BrCache = immer::map_transient<BrCacheKey, solver_result, hash_BrCacheKey>;
  using BrCache = std::map<BrCacheKey, solver_result>;
  using MCexCache = immer::map_transient<CexCacheKey, M, hash_BrCacheKey>;
  using BrCexCache = immer::map_transient<BrCacheKey, BrCex, hash_BrCacheKey>;
  BrCache br_cache;
  MCexCache mcex_cache;
  BrCexCache brcex_cache;


  // Construct the solver expression with object cache for a Value
  const Expr& construct_expr(PtrVal e) {
    auto fd = objcache.find(e);
    if (fd != objcache.end()) return fd->second;
    auto [it, ins] = objcache.emplace(e, self()->construct_expr_internal(e));
    return it->second;
  }

  inline void resolve_indep_uf(UnionFind& uf, PtrVal root, BrCacheKey& result, bool add_root = true) {
    if (add_root) result.insert(root);
    auto last_expr = root;
    while (root != uf.next[last_expr]) {
      last_expr = uf.next[last_expr];
      if (!last_expr->to_SymV()->is_var()) {
        result.insert(last_expr);
      }
    }
  }

  inline void resolve_indep_uf(UnionFind& uf, PtrVal root, std::function<void(PtrVal)> f, bool add_root = true) {
    if (add_root) f(root);
    auto last_expr = root;
    while (root != uf.next[last_expr]) {
      last_expr = uf.next[last_expr];
      if (!last_expr->to_SymV()->is_var()) f(last_expr);
    }
  }

  M* query_model(CexCacheKey& conds) {
    auto it = mcex_cache.find(conds);
    M* m = nullptr;
    //if (it != mcex_cache.end()) {
    if (it != nullptr) {
      cached_query_num += 1;
      return (M*) it;
      //m = &it->second;
    } else {
      push();
      for (auto& v: conds) self()->add_constraint_internal(construct_expr(v));
      auto result = check_model();
      br_cache.emplace(conds, result);
      if (result == sat) {
        mcex_cache.set(conds, self()->get_model_internal());
        pop();
        return (M*) mcex_cache.find(conds);
        //m = &it->second;
        //return m;
      }
      pop();
      return nullptr;
    }
    //return m;
  }

  /////////////////////////////////////////////////////////////

  void clear_cache() {
    objcache.clear();
    //cexcache.clear();
  }

  // a unoptimized but correct baseline
  inline void gen_default_format_M(PC& pc, M* model, unsigned int test_id) {
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
             << self()->eval_model(model, construct_expr(v)) << std::endl;
    }
    int n = write(out_fd, output.str().c_str(), output.str().size());
    close(out_fd);
  }

  inline void gen_ktest_format(std::shared_ptr<Model> model, unsigned int test_id,
    List<SymObj> sym_objs, int conc_argc, char** conc_argv) {
    KTest b;
    b.numArgs = conc_argc;
    b.args = conc_argv;
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
      assert(o->bytes);
      if (obj.is_whole) {
        // XXX(GW): why obj.size must be 4?
        ASSERT(obj.size == 4, "Bad whole object");
        auto key = std::dynamic_pointer_cast<SymV>(make_SymV(obj.name, obj.size*8));
        ASSERT(key, "Invalid key");
        auto it = model->find(key);
        uint32_t value = (uint32_t) it->second;
        if (it == model->end()) memset(o->bytes, 0, o->numBytes);
        else memcpy(o->bytes, (char*) &value, o->numBytes);
      } else {
        for (int idx = 0; idx < o->numBytes; idx++) {
          auto key = std::dynamic_pointer_cast<SymV>(make_SymV(obj.name + "_" + std::to_string(idx), 8));
          ASSERT(key, "Invalid key");
          auto it = model->find(key);
          if (it != model->end()) {
            o->bytes[idx] = (unsigned char) it->second;
          } else {
            o->bytes[idx] = 0;
          }
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

  inline void gen_default_format(std::shared_ptr<Model> model, unsigned int test_id) {
    std::stringstream output;
    output << "Query number: " << (test_id+1) << std::endl;
    output << "Query is sat." << std::endl;
    std::stringstream filename;
    filename << "tests/" << test_id << ".test";
    int out_fd = open(filename.str().c_str(), O_RDWR | O_CREAT, 0777);
    if (out_fd == -1) {
      ABORT("Cannot create the test case file, abort.\n");
    }
    for (auto& [k, v]: *model) {
      output << k->to_SymV()->name << "=" << v << std::endl;
    }
    int n = write(out_fd, output.str().c_str(), output.str().size());
    close(out_fd);
  }

  // interfaces

    /*
    resolve_indep_uf(uf, cond, [this](auto v){
      this->self()->add_constraint_internal(construct_expr(v));
    }, pc.contains(cond));
    */

  virtual BrResult check_branch(PC& pc, PtrVal cond) override {
    if (!use_solver) return std::make_pair(sat, sat);
    br_query_num++;

    auto start = steady_clock::now();
    auto neg_cond = SymV::neg(cond);
    UnionFind uf(pc.uf);
    for (auto& v : cond->to_SymV()->vars) uf.join(v, cond);
    BrCacheKey common;
    resolve_indep_uf(uf, cond, common, pc.contains(cond));
    auto end = steady_clock::now();
    cons_indep_time += duration_cast<microseconds>(end - start).count();

    BrResult result;

    common.insert(cond);
    auto then_hit = br_cache.find(common);
    if (!pc.contains(cond)) common.erase(cond);
    common.insert(neg_cond);
    auto else_hit = br_cache.find(common);
    if (!pc.contains(neg_cond)) common.erase(neg_cond);

    if (then_hit != br_cache.end() && else_hit != br_cache.end()) {
      // both hit
      cached_query_num += 2;
      result.first = then_hit->second;
      result.second = else_hit->second;
    } else if (then_hit != br_cache.end()) {
      // only "then" branch hits
      cached_query_num += 1;
      result.first = then_hit->second;
      common.insert(neg_cond);
      if (result.first == solver_result::unsat) {
        result.second = solver_result::sat;
        br_cache.emplace(common, result.second);
      } else {
        push();
        for (auto& v: common) self()->add_constraint_internal(construct_expr(v));
        result.second = check_model();
        br_cache.emplace(common, result.second);
        if (result.second == sat) mcex_cache.set(common, self()->get_model_internal());
        pop();
      }
      auto then_time1 = steady_clock::now();
      then_br_time1 += duration_cast<microseconds>(then_time1 - end).count();
    } else if (else_hit != br_cache.end()) {
      // only "else" branch hits
      cached_query_num += 1;
      result.second = else_hit->second;
      common.insert(cond);
      if (result.second == solver_result::unsat) {
        result.first = solver_result::sat;
        br_cache.emplace(common, result.first);
      } else {
        push();
        for (auto& v: common) self()->add_constraint_internal(construct_expr(v));
        result.first = check_model();
        br_cache.emplace(common, result.first);
        if (result.first == sat) mcex_cache.set(common, self()->get_model_internal());
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
      br_cache.emplace(common, result.first);
      if (result.first == sat) mcex_cache.set(common, self()->get_model_internal());
      pop();

      if (!pc.contains(cond)) common.erase(cond);
      common.insert(neg_cond);
      if (result.first == solver_result::unsat) {
        result.second = solver_result::sat;
        br_cache.emplace(common, result.second);
        pop();
      } else {
        self()->add_constraint_internal(construct_expr(neg_cond));
        result.second = check_model();
        br_cache.emplace(common, result.second);
        if (result.second == sat) mcex_cache.set(common, self()->get_model_internal());
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
    resolve_indep_uf(pc.uf, e, conds, false);
    solver_result result;
    auto m = query_model(conds);
    if (m != nullptr) result = sat;
    return std::make_pair(result == sat, result == sat ? self()->eval_model(m, construct_expr(e)) : 0);
    //return std::make_pair(result == sat, result == sat ? m->at(sym_e) : 0);
  }

  virtual void generate_test(SS state) override {
    completed_path_num++;
    if (only_output_covernew && !state.has_cover_new()) return;
    if (!use_solver) return;

    if (mkdir("tests", 0777) == -1) {
      if (errno == EEXIST) { }
      else ABORT("Cannot create the folder tests, abort.\n");
    }

    auto& conds = state.get_PC().conds;
    CexCacheKey pc;
    pc.insert(conds.begin(), conds.end());
    M* m = nullptr;
    if (state.get_preferred_cex().size() > 0) {
      for (auto& v: state.get_preferred_cex()) {
        //std::cout << "Add preferred cex " << v->toString() << "\n";
        pc.insert(v);
        m = query_model(pc);
        if (m == nullptr) pc.erase(v);
      }
      m = query_model(pc);
    } else {
      m = query_model(pc);
    }
    ASSERT(m != nullptr, "Cannot generate test cases for unsat conditions!");
    generated_test_num++;
    // FIXME
    //if (output_ktest) gen_ktest_format(model, generated_test_num, state.get_sym_objs(), g_conc_argc, g_conc_argv);
    gen_default_format_M(state.get_PC(), m, generated_test_num);
    //gen_default_format(m, generated_test_num);
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

// FIXME: external.hpp still uses check_pc
inline bool check_pc(PC pc) {
  return check_branch(std::move(pc), sym_bool_const(true)).first == solver_result::sat;
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
