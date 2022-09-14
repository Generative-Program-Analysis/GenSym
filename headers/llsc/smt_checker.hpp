#ifndef LLSC_SMT_CHECKER_HEADER
#define LLSC_SMT_CHECKER_HEADER

#include "ktest.hpp"

class Checker {
public:
  virtual ~Checker() {}

  enum solver_result {
    unsat, sat, unknown
  };
  static std::string check_result_to_string(solver_result res) {
    switch (res) {
      case sat: return "sat";
      case unsat: return "unsat";
      case unknown: return "unknown";
      default: ABORT("wow");
    }
  }

  virtual bool check_pc(PC pc) = 0;
  virtual std::pair<bool, UIntData> get_sat_value(PC pc, PtrVal v) = 0;
  virtual void generate_test(SS state) = 0;
};

template <typename Self, typename Expr>
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
  using ObjCache = std::unordered_map<PtrVal, Expr>;
  // query cache key
  using CexCacheKey = std::set<PtrVal>;
  // assignment of symbolic variables/expr to concrete data
  using Model = std::unordered_map<PtrVal, IntData>; //XXX PtrVal
  // the result of check, accompanying with a model if sat
  using CheckResult = std::pair<solver_result, std::shared_ptr<Model>>;
  // query cache type
  using CexCache = std::map<CexCacheKey, CheckResult>;

  ObjCache objcache;
  CexCache cexcache;
  std::map<CexCacheKey, bool> querycache;
  ReachMap global_reachmap;
  VarMap global_varmap;

  using ObjCacheIter = typename decltype(objcache)::iterator;
  using CachedPC = std::vector<ObjCacheIter>;

  void clear_cache() {
    objcache.clear();
    cexcache.clear();
  }

  // a unoptimized but correct baseline
  void construct_reachmap(ReachMap* rm, const PtrVal& top_cnd, const PtrVal& e) {
    auto sym_e = e->to_SymV();
    if (!sym_e) return;
    if (sym_e->is_var()) rm->emplace(sym_e, top_cnd);
    for (auto& rand : sym_e->rands) construct_reachmap(rm, top_cnd, rand);
  }

  // Construct the solver expression with object cache for a Value
  const Expr& construct_expr(PtrVal e) {
    auto fd = objcache.find(e);
    if (fd != objcache.end()) return fd->second;
    auto [it, ins] = objcache.emplace(e, self()->construct_expr_internal(e));
    return it->second;
  }

  void construct_model(CexCacheKey& pc, std::shared_ptr<Model>& model) {
    for (auto& e: pc) {
      for (auto& v: e->to_SymV()->vars) model->emplace(v, self()->eval(global_varmap.at(v)));
    }
  }

  // The most primitive function to check satisfiability/model.
  // If `model` is passed, it is obligated to construct a model.
  // Return value indicates sat/unsat.
  inline solver_result check_model0(CexCacheKey& pc, std::shared_ptr<Model> model) {
    //VarMap varmap;
    push();
    for (auto& v: pc) self()->add_constraint_internal(objcache.at(v));
    solver_result result = check_model();
    if (result == sat && model) construct_model(pc, model);
    pop();
    return result;
  }

  //inline bool cache_hit(CexCache::iterator& it, std::shared_ptr<Model>& model) {
  inline bool cache_hit(typename decltype(cexcache)::iterator& it, std::shared_ptr<Model>& model) {
    if (it != cexcache.end()) {
      // model cached and required
      if (it->second.second && model) {
        model = it->second.second;
        return true;
      }
      // model cached and not required
      if (it->second.second && !model) {
        return true;
      }
      // model not cached and not required
      if (!it->second.second && !model) {
        return true;
      }
      // model not cached but required, usually it is gnereating test case
      if (!it->second.second && model) {
        cexcache.erase(it); // XXX: is it necessary?
        return false;
      }
    }
    return false;
  }

  // On top of check_model0, additionally it caches counterexamples and models
  inline solver_result check_cexcached_model0(CexCacheKey& pc, std::shared_ptr<Model>& model) {
    ASSERT(use_cexcache, "Why not?");
    auto it = cexcache.find(pc);
    if (cache_hit(it, model)) {
      cached_query_num += 1;
      return it->second.first;
    }
    solver_result result = check_model0(pc, model);
    cexcache.emplace(std::move(pc), std::make_pair(result, model));
    return result;
  }

  // On top of check_cexcached_model0, we can query a point of symbolic variable
  CheckResult check_cexcached_model(CexCacheKey& pc, simple_ptr<SymV> query_expr, bool require_model) {
    ASSERT(!require_model || !query_expr, "Choose either query_expr or require_model, but not both");
    std::shared_ptr<Model> model = (use_cexcache || require_model) ? std::make_shared<Model>() : nullptr;
    solver_result result = use_cexcache ? check_cexcached_model0(pc, model) : check_model0(pc, model);
    if (result == sat && query_expr) {
      // query_expr can be a compound expression
      // reuse the model in case use_cexcache already sets a model
      if (!model) model = std::make_shared<Model>();
      model->emplace(query_expr, self()->eval(objcache.at(query_expr))); // XXX: could be wrong -- since a cache hit may not lead to valid eval(e) value!
    }
    return std::make_pair(result, model);
  }

  void mark(PtrVal root, CexCacheKey& visited, CexCacheKey& result, bool add_root = true) {
    ASSERT(use_cons_indep, "why not?");
    if (add_root) result.insert(root);
    auto sym_root = root->to_SymV();
    for (auto& var : sym_root->vars) {
      if (visited.find(var) != visited.end()) continue;
      //std::cout << "  it reaches var " << var->toString() << "\n";
      visited.insert(var);
      auto [beg, end] = global_reachmap.equal_range(var);
      for (auto it = beg; it != end; it++) mark(it->second, visited, result);
    }
  }

  void resolve_indep_uf(UnionFind& uf, PtrVal root, CexCacheKey& result, bool add_root = true) {
    auto start = steady_clock::now();
    if (add_root) result.insert(root);
    auto last_expr = root;
    while (root != uf.next[last_expr]) {
      last_expr = uf.next[last_expr];
      if (!last_expr->to_SymV()->is_var()) result.insert(last_expr);
    }
    auto end = steady_clock::now();
    cons_indep_time += duration_cast<microseconds>(end - start).count();
  }

  CheckResult check_model_at(PC& pc, PtrVal expr) {
    auto start = steady_clock::now();
    std::shared_ptr<Model> model = std::make_shared<Model>();
    CexCacheKey indep_pc;
    resolve_indep_uf(pc.uf, expr, indep_pc, false);

    auto it = cexcache.find(indep_pc);
    if (expr->to_SymV()->is_var() && cache_hit(it, model)) {
      // if expr is a variable, them the hitted model should (?) garuante to have a value for it
      cached_query_num += 1;
      auto end = steady_clock::now();
      mono_solver_time += duration_cast<microseconds>(end - start).count();
      return it->second;
    }
    std::set<PtrVal> vars;
    // check model
    push();
    for (auto& cnd: indep_pc) {
      auto cnd_e = cnd->to_SymV();
      vars.insert(cnd_e->vars.begin(), cnd_e->vars.end());
      self()->add_constraint_internal(construct_expr(cnd));
    }
    solver_result result = check_model();
    // store model
    if (result == sat) {
      model->insert_or_assign(expr, self()->eval(construct_expr(expr))); // XXX: seems not necessary
      for (auto& v : vars) model->insert_or_assign(v, self()->eval(global_varmap.at(v)));
    }
    // update cex cache
    cexcache.emplace(std::move(indep_pc), std::make_pair(result, model));
    pop();
    auto end = steady_clock::now();
    mono_solver_time += duration_cast<microseconds>(end - start).count();
    return std::make_pair(result, model);
  }

  CheckResult check_model_mono(PC& pc) {
    auto start = steady_clock::now();
    auto model = std::make_shared<Model>();
    CexCacheKey indep_pc;
    resolve_indep_uf(pc.uf, *std::prev(pc.conds.end()), indep_pc);

    // cex cache
    auto it = cexcache.find(indep_pc);
    if (cache_hit(it, model)) {
      cached_query_num += 1;
      auto end = steady_clock::now();
      mono_solver_time += duration_cast<microseconds>(end - start).count();
      return it->second;
    }
    std::set<PtrVal> vars;
    // check model
    push();
    for (auto& cnd: indep_pc) {
      auto cnd_e = cnd->to_SymV();
      vars.insert(cnd_e->vars.begin(), cnd_e->vars.end());
      self()->add_constraint_internal(construct_expr(cnd));
    }
    solver_result result = check_model();
    // store model
    if (result == sat) {
      for (auto& v : vars) model->insert_or_assign(v, self()->eval(global_varmap.at(v)));
    }
    // update cex cache
    cexcache.emplace(std::move(indep_pc), std::make_pair(result, model));
    pop();
    auto end = steady_clock::now();
    mono_solver_time += duration_cast<microseconds>(end - start).count();
    return std::make_pair(result, model);
  }

  void construct_reachmap_fast(ReachMap& rm, const PtrVal& top_cnd) {
    for (auto& v : top_cnd->to_SymV()->vars)
      rm.emplace(v->to_SymV(), top_cnd);
  }

  // Query satisfiability, potentially return the concretized value of query_expr
  template <template <typename> typename T>
  CheckResult check_model_indep(const T<PtrVal>& conds, simple_ptr<SymV> query_expr = nullptr) {
    auto start = steady_clock::now();
    global_reachmap.clear();
    for (auto &v: conds) construct_expr(v);
    if (query_expr) construct_expr(query_expr);
    CexCacheKey indep_pc;
    if (!use_cons_indep) indep_pc.insert(conds.begin(), conds.end());
    else if (conds.size() <= (query_expr != nullptr ? 0 : 1)) {
      // Not necessary to use independence solver since conds is too small
      indep_pc.insert(conds.begin(), conds.end());
    } else {
      for (auto& cnd : conds) construct_reachmap_fast(global_reachmap, cnd);
      CexCacheKey visited;
      auto root = query_expr ? query_expr : *std::prev(conds.end());
      bool add_root = query_expr ? false : true;
      mark(root, visited, indep_pc, add_root);
    }
    auto res = check_cexcached_model(indep_pc, query_expr, false);

    auto end = steady_clock::now();
    cons_indep_time_old += duration_cast<microseconds>(end - start).count();
    return res;
  }

  template <template <typename> typename T>
  CheckResult check_with_full_model(const T<PtrVal>& conds) {
    // translation
    auto start = steady_clock::now();
    for (auto &v: conds) construct_expr(v);
    auto end = steady_clock::now();
    cons_expr_time += duration_cast<microseconds>(end - start).count();

    start = steady_clock::now();

    CexCacheKey pc;
    pc.insert(conds.begin(), conds.end());
    auto res = check_cexcached_model(pc, nullptr, true);

    end = steady_clock::now();
    full_model_time += duration_cast<microseconds>(end - start).count();
    return res;
    /*
    if (!use_cons_indep) {
      pc.insert(conds.begin(), conds.end());
      return check_cexcached_model(pc, nullptr, true);
    }

    start = steady_clock::now();

    // constraint independence resolving
    solver_result check_result;
    std::vector<PtrVal> curr_conds(conds.begin(), conds.end());
    std::shared_ptr<Model> model = std::make_shared<Model>();

    while (curr_conds.size() > 0) {
      get_indep_conds(curr_conds, pc, nullptr);
      int before_size = curr_conds.size();
      for (auto& v: pc) {
        curr_conds.erase(std::remove(curr_conds.begin(), curr_conds.end(), v), curr_conds.end());
      }
      ASSERT(curr_conds.size() < before_size, "Invalid elimination");
      auto [sub_result, sub_model] = check_cexcached_model(pc, nullptr, true);
      if (model->size() == 0)
        check_result = sub_result;
      // Note (Ruiqi): make sure that sub_model's key won't overlap, because std::unordered_map::insert will not update for same key.
      model->insert(sub_model->begin(), sub_model->end());
      pc.clear();
    }

    end = steady_clock::now();
    full_model_time += duration_cast<microseconds>(end - start).count();
    return std::make_pair(check_result, model);
    */
  }

  // interfaces

  virtual bool check_pc(PC pc) override {
    if (!use_solver) return true;
    br_query_num++;
    auto [r, m] = check_model_mono(pc);
    return r == sat;
  }

  virtual std::pair<bool, UIntData> get_sat_value(PC pc, PtrVal e) override {
    auto sym_e = e->to_SymV();
    ASSERT(sym_e != nullptr, "concretizing a non-symbolic value");
    for (auto& v: sym_e->vars) pc.uf.join(v, sym_e);
    auto [r, m] = check_model_at(pc, sym_e);
    return std::make_pair(r == sat, r == sat ? m->at(sym_e) : 0);
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

  virtual void generate_test(SS state) override {
    auto pc = state.get_PC();
    if (!use_solver) return;
    if (mkdir("tests", 0777) == -1) {
      if (errno == EEXIST) { }
      else ABORT("Cannot create the folder tests, abort.\n");
    }
    completed_path_num++;
    if (only_output_covernew && !state.has_cover_new()) return;
    CheckResult res;
    solver_result result;
    std::shared_ptr<Model> model;
    TrList<PtrVal> new_conds(pc.get_path_conds());
    if (state.get_preferred_cex().size() > 0) {
      auto preferred_cex = state.get_preferred_cex();
      for (auto& v: preferred_cex) {
        new_conds.push_back(v);
        auto [r, m] = check_model_indep(new_conds);
        if (r != sat) new_conds.take(new_conds.size()-1);
      }
      res = check_with_full_model(new_conds);
    } else {
      res = check_with_full_model(new_conds);
    }
    result = res.first;
    model = res.second;
    if (result == sat) {
      generated_test_num++;
      if (output_ktest) gen_ktest_format(model, generated_test_num, state.get_sym_objs(), g_conc_argc, g_conc_argv);
      else gen_default_format(model, generated_test_num);
    } else {
      ABORT("Cannot find satisfiable test cases");
    }
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

inline bool check_pc(PC pc) {
  auto start = steady_clock::now();
  auto result = checker_manager.get_checker().check_pc(std::move(pc));
  auto end = steady_clock::now();
  int_solver_time += duration_cast<microseconds>(end - start).count();
  return result;
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
  int_solver_time += duration_cast<microseconds>(end - start).count();
  return result;
}

#endif
