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
  // a map from symbolic *variables* to their solver expressions
  using VarMap = std::map<simple_ptr<SymV>, Expr>;
  // a map from symbolic variable to expression that contains this expression
  using ReachMap = std::multimap<simple_ptr<SymV>, PtrVal>;
  // Extended Expr
  struct XExpr {
    Expr expr;
    VarMap varmap;     // varmap is *transitively* closed wrt all variables in subexpressions
    ReachMap reachmap;
  };
  // object cache type
  using ObjCache = std::unordered_map<PtrVal, XExpr>;

  // query cache key
  using CexCacheKey = std::set<PtrVal>;
  // assignment of symbolic variables/expr to concrete data
  using Model = std::unordered_map<simple_ptr<SymV>, IntData>;
  // the result of check, accompanying with a model if sat
  using CheckResult = std::pair<solver_result, std::shared_ptr<Model>>;
  // query cache type
  using CexCache = std::map<CexCacheKey, CheckResult>;

  ObjCache objcache;
  CexCache cexcache;

  using ObjCacheIter = typename decltype(objcache)::iterator;
  using CachedPC = std::vector<ObjCacheIter>;

  void clear_cache() {
    objcache.clear();
    cexcache.clear();
  }

  // domain must be variable (is_var()), codomain only top-level predicate
  ReachMap global_reach_map;

  // Construct the solver expression for a Value
  inline const XExpr construct_expr0(PtrVal& e, bool top_level) {
    VarMap tmp;
    ReachMap reachmap;
    auto expr = self()->construct_expr_internal(e, tmp, reachmap, top_level);
    /*
    std::cout << "reach begin\n";
    for (auto& p: reach_map) {
      std::cout << p.first->toString() << " -> " << p.second->toString() << "\n";
    }
    std::cout << "reach end\n";
    */
    return {expr, std::move(tmp), std::move(reachmap)};
  }

  // Construct the solver expression with object cache for a Value
  const XExpr& construct_expr(PtrVal e, bool top_level = false) {
    auto fd = objcache.find(e);
    if (fd != objcache.end()) {
      if (top_level) {
        auto start = steady_clock::now();
        global_reach_map.insert(fd->second.reachmap.begin(), fd->second.reachmap.end());
        auto end = steady_clock::now();
        cons_indep_time_new += duration_cast<microseconds>(end - start).count();
      }
      return fd->second;
    }
    auto [it, ins] = objcache.emplace(e, std::move(construct_expr0(e, top_level)));
    if (top_level) {
      auto start = steady_clock::now();
      global_reach_map.insert(it->second.reachmap.begin(), it->second.reachmap.end());
      auto end = steady_clock::now();
      cons_indep_time_new += duration_cast<microseconds>(end - start).count();
    }
    return it->second;
  }

  void construct_model(VarMap& varmap, std::shared_ptr<Model> model) {
    for (auto& [v, e]: varmap) {
      model->emplace(v, self()->get_model_value_internal(e));
    }
  }

  // The most primitive function to check satisfiability/model.
  // If `model` is passed, it is obligated to construct a model.
  // Return value indicates sat/unsat.
  inline solver_result check_model0(const CachedPC& cachedObjs, std::set<PtrVal>& pc, std::shared_ptr<Model> model) {
    VarMap varmap;
    push();
    if (cachedObjs.size()) {
      // use local cache if possible
      for (auto& v: cachedObjs) {
        auto& [e, vm, rm] = v->second;
        self()->add_constraint_internal(e);
        if (model) varmap.insert(vm.begin(), vm.end());
      }
    } else {
      for (auto& v: pc) {
        // query objcache still
        auto& [e, vm, rm] = objcache.at(v);
        self()->add_constraint_internal(e);
        if (model) varmap.insert(vm.begin(), vm.end());
      }
    }
    solver_result result = check_model();
    pop();
    if (result == sat && model) construct_model(varmap, model);
    return result;
  }

  inline bool cache_hit(CexCache::iterator& it, std::shared_ptr<Model>& model) {
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
        //cexcache.erase(it); // XXX: is it necessary?
        return false;
      }
    }
    return false;
  }

  // On top of check_model0, additionally it caches counterexamples and models
  inline solver_result check_cexcached_model0(const CachedPC& cachedObjs, std::set<PtrVal>& pc, std::shared_ptr<Model>& model) {
    ASSERT(use_cexcache, "Why not?");
    auto it = cexcache.find(pc);
    if (cache_hit(it, model)) {
      cached_query_num += 1;
      return it->second.first;
    }
    solver_result result = check_model0(cachedObjs, pc, model);
    cexcache.emplace(std::move(pc), std::make_pair(result, model));
    return result;
  }

  // On top of check_cexcached_model0, we can query a point of symbolic variable
  CheckResult check_cexcached_model(const CachedPC& cachedObjs, std::set<PtrVal>& pc,
                                    simple_ptr<SymV> query_expr, bool require_model) {
    ASSERT(!require_model || !query_expr, "Choose either query_expr or require_model, but not both");
    std::shared_ptr<Model> model = (use_cexcache || require_model) ? std::make_shared<Model>() : nullptr;
    solver_result result = use_cexcache ? check_cexcached_model0(cachedObjs, pc, model) : check_model0(cachedObjs, pc, model);
    if (result == sat && query_expr) {
      // query_expr can be a compound expression
      // reuse the model in case use_cexcache already sets a model
      if (!model) model = std::make_shared<Model>();
      auto& e = objcache.at(query_expr).expr;
      model->emplace(query_expr, self()->get_model_value_internal(e));
    }
    return std::make_pair(result, model);
  }

  template <template <typename> typename T>
  void get_indep_conds(const T<PtrVal>& conds, CachedPC& cachedObjs, std::set<PtrVal>& pc, simple_ptr<SymV> query_expr) {
    ASSERT(use_cons_indep, "why not?");
    if (conds.size() <= (query_expr!=nullptr ? 0 : 1)) {
      // Not necessary to use independence solver since conds is too small
      pc.insert(conds.begin(), conds.end());
      return;
    }
    auto start = steady_clock::now();
    std::vector<ObjCacheIter> queue;
    ObjCacheIter cur;
    int idx;

    for (auto& v: conds) cachedObjs.push_back(objcache.find(v));

    if (query_expr == nullptr) {
      cur = cachedObjs.back();
      cachedObjs.pop_back();
      idx = 1;
      queue.push_back(cur);
      pc.insert(cur->first);
    } else {
      cur = objcache.find(query_expr);
      idx = 0;
    }

    do {
      auto& cset = cur->second.varmap;
      for (auto& next: cachedObjs) if (next != objcache.end()) {
        auto& nset = next->second.varmap;
        auto cit = cset.begin();
        auto nit = nset.begin();
        if (cit != cset.end() && nit != nset.end()) {
          do if (nit->first == cit->first) {
            pc.insert(next->first);
            queue.push_back(next);
            next = objcache.end();
            break;
          } while (nit->first < cit->first ? ++nit != nset.end() : ++cit != cset.end());
        }
      }
    } while (idx < queue.size() && (cur = queue[idx++], true));

    cachedObjs = std::move(queue);

    auto end = steady_clock::now();
    cons_indep_time_old += duration_cast<microseconds>(end - start).count();
  }

  void mark(PtrVal root, std::set<PtrVal>& visited, std::set<PtrVal>& result, bool add_root = true) {
    ASSERT(use_cons_indep, "why not?");

    if (add_root) result.insert(root);
    auto root_obj = objcache.find(root);
    ASSERT(root_obj != objcache.end(), "Root not cached");
    auto& varmap = root_obj->second.varmap;
    for (auto& [var, vexp] : varmap) {
      if (visited.find(var) != visited.end()) continue;
      //std::cout << "  it reaches var " << var->toString() << "\n";
      visited.insert(var);
      auto [beg, end] = global_reach_map.equal_range(var);
      for (auto it = beg; it != end; it++) {
        mark(it->second, visited, result);
      }
    }
  }

  // Query satisfiability, potentially return the concretized value of query_expr
  template <template <typename> typename T>
  CheckResult check_model_indep(const T<PtrVal>& conds, simple_ptr<SymV> query_expr = nullptr) {
    global_reach_map.clear();

    // XXX: what if we do cons_indep at front?
    if (!use_objcache) objcache.clear(); // XXX: if not using objcache, why bother clear it?
    for (auto &v: conds) construct_expr(v, true);
    if (query_expr) construct_expr(query_expr, true);

    // local storage
    CachedPC cachedObjs;
    std::set<PtrVal> pc;
    if (!use_cons_indep) pc.insert(conds.begin(), conds.end());
    else if (conds.size() <= (query_expr != nullptr ? 0 : 1)) {
      // Not necessary to use independence solver since conds is too small
      pc.insert(conds.begin(), conds.end());
    } else {
      //cachedObjs.reserve(conds.size());
      //for (auto& v: conds) cachedObjs.push_back(objcache.find(v));
      //get_indep_conds(cachedObjs, pc, query_expr);
      //std::cout << "\nglobal reach map:\n";
      //for (auto& p: global_reach_map) std::cout << "  " << p.first->toString() << " ~> " << p.second->toString() << '\n';

      auto start = steady_clock::now();
      std::set<PtrVal> visited;
      if (query_expr == nullptr) {
        auto root = *std::prev(conds.end());
        //std::cout << "root is (non-query_expr): " << root->toString() << "\n";
        mark(root, visited, pc);
      } else {
        //std::cout << "root is (query_expr): " << query_expr->toString() << "\n";
        mark(query_expr, visited, pc, false);
      }
      auto end = steady_clock::now();
      cons_indep_time_new += duration_cast<microseconds>(end - start).count();
      /*
      std::cout << "All PC: \n";
      for (auto& c : conds) {
        std::cout << "  " << c->toString() << "\n";
      }
      std::cout << "PC after indep: \n";
      for (auto& c : pc) {
        std::cout << "  " << c->toString() << "\n";
      }
      */
    }

    //std::set<PtrVal> pc2;
    //pc2.insert(conds.begin(), conds.end());
    //auto [s1, m1] = check_cexcached_model(cachedObjs, pc2, query_expr, false);
    //auto [s2, m2] = check_cexcached_model(cachedObjs, pc, query_expr, false);
    //if (s1 != s2) ABORT("FUCK");
    //return {s2, m2};
    return check_cexcached_model(cachedObjs, pc, query_expr, false);
  }

  template <template <typename> typename T>
  CheckResult check_with_full_model(const T<PtrVal>& conds) {
    // translation
    if (!use_objcache) objcache.clear(); // XXX: if not using objcache, why bother clear it?
    for (auto &v: conds) construct_expr(v);

    // local storage
    CachedPC cachedObjs;
    std::set<PtrVal> pc;

    if (!use_cons_indep) {
      pc.insert(conds.begin(), conds.end());
      return check_cexcached_model(cachedObjs, pc, nullptr, true);
    }

    auto start = steady_clock::now();

    // constraint independence resolving
    solver_result check_result;
    std::vector<PtrVal> curr_conds(conds.begin(), conds.end());
    std::shared_ptr<Model> model = std::make_shared<Model>();

    while (curr_conds.size() > 0) {
      get_indep_conds(curr_conds, cachedObjs, pc, nullptr);
      int before_size = curr_conds.size();
      for (auto& v: pc) {
        curr_conds.erase(std::remove(curr_conds.begin(), curr_conds.end(), v), curr_conds.end());
      }
      ASSERT(curr_conds.size() < before_size, "Invalid elimination");
      auto [sub_result, sub_model] = check_cexcached_model(cachedObjs, pc, nullptr, true);
      if (model->size() == 0)
        check_result = sub_result;
      // Note (Ruiqi): make sure that sub_model's key won't overlap, because std::unordered_map::insert will not update for same key.
      model->insert(sub_model->begin(), sub_model->end());
      cachedObjs.clear();
      pc.clear();
    }

    auto end = steady_clock::now();
    full_model_time += duration_cast<microseconds>(end - start).count();
    return std::make_pair(check_result, model);
  }

  // interfaces

  virtual bool check_pc(PC pc) override {
    if (!use_solver) return true;
    br_query_num++;
    auto [r, m] = check_model_indep(pc.get_path_conds());
    return r == sat;
  }

  virtual std::pair<bool, UIntData> get_sat_value(PC pc, PtrVal v) override {
    auto v2 = std::dynamic_pointer_cast<SymV>(v);
    //ASSERT(v2->is_var(), "concretizing a compound symbolic expression; try add equational constraint");
    auto [r, m] = check_model_indep(pc.get_path_conds(), v2);
    return std::make_pair(r == sat, r == sat ? m->at(v2) : 0);
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
      output << k->name << "=" << v << std::endl;
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
    auto pc = std::move(state.get_PC());
    if (!use_solver) return;
    if (mkdir("tests", 0777) == -1) {
      if (errno == EEXIST) { }
      else ABORT("Cannot create the folder tests, abort.\n");
    }
    completed_path_num++;
    if (only_output_covernew && !state.has_cover_new()) return;
    auto conds = pc.get_path_conds();
    CheckResult res;
    solver_result result;
    std::shared_ptr<Model> model;
    if (state.get_preferred_cex().size() > 0) {
      std::vector<PtrVal> new_conds(conds.begin(), conds.end());
      auto preferred_cex = state.get_preferred_cex();
      for (auto& v: preferred_cex) {
        new_conds.push_back(v);
        auto [r, m] = check_model_indep(new_conds);
        if (r != sat)
          new_conds.pop_back();
      }
      res = check_with_full_model(new_conds);
    } else {
      res = check_with_full_model(conds);
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
      }
      else if (solver_kind == SolverKind::stp) {
        checker_map[id] = std::make_unique<CheckerSTP>();
      }
      else {
        ABORT("unknown solver");
      }
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

inline void check_pc_to_file(SS state) {
  auto start = steady_clock::now();
  checker_manager.get_checker().generate_test(std::move(state));
  auto end = steady_clock::now();
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
