#ifndef LLSC_SMT_CHECKER_HEADER
#define LLSC_SMT_CHECKER_HEADER

#include "ktest.hpp"

// TODO: generic caching mechanisms should be shared no matter the solver
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
  // a map from symbolic variables to their solver expressions
  using VarMap = std::map<simple_ptr<SymV>, Expr>;
  // pair the solver expression with its varmap
  // XXX: what's the relation of that varmap and expr?
  using ExprDetail = std::pair<Expr, VarMap>;
  // assignment of symbolic variables to concrete data
  using Model = std::unordered_map<simple_ptr<SymV>, IntData>;
  // the result of check, accompanying with a model if sat
  using CheckResult = std::pair<solver_result, std::shared_ptr<Model>>;

  std::unordered_map<PtrVal, ExprDetail> objcache;
  std::map<std::set<PtrVal>, CheckResult> cexcache;

  using objiter_t = typename decltype(objcache)::iterator;

  void clear_cache() {
    objcache.clear();
    cexcache.clear();
  }

  // Construct the solver expression for a Value
  inline const ExprDetail constuct_expr0(PtrVal& e) {
    VarMap tmp;
    auto expr = self()->construct_expr_internal(e, tmp);
    return std::make_pair(expr, std::move(tmp));
  }

  // Construct the solver expression with object cache for a Value
  const ExprDetail& construct_expr(PtrVal e) {
    auto fd = objcache.find(e);
    if (fd != objcache.end()) return fd->second;
    auto [it, ins] = objcache.emplace(e, std::move(constuct_expr0(e)));
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
  inline solver_result check_model0(const std::vector<objiter_t>& condvec, std::set<PtrVal>& condset, std::shared_ptr<Model> model) {
    VarMap varmap;
    push();
    // XXX: what's the diff of condvec and condset???
    if (condvec.size()) {  // use local cache if possible
      for (auto& v: condvec) {
        auto& [e, vm] = v->second;
        self()->add_constraint_internal(e);
        if (model) varmap.insert(vm.begin(), vm.end());
      }
    } else {
      // XXX: why this case we should traverse over condset?
      for (auto& v: condset) {
        auto& [e, vm] = objcache.at(v);
        self()->add_constraint_internal(e);
        if (model) varmap.insert(vm.begin(), vm.end());
      }
    }
    solver_result result = check_model();
    pop();
    if (result == sat && model) construct_model(varmap, model);
    return result;
  }

  // On top of check_model0, additional it caches counterexamples and models
  inline solver_result check_cexcached_model0(const std::vector<objiter_t>& condvec, std::set<PtrVal>& condset, std::shared_ptr<Model>& model) {
    if (use_cexcache) {
      auto it = cexcache.find(condset);
      if (it != cexcache.end()) {
        // cache hit and there is indeed a model and caller requires a model
        if (it->second.second && model) {
          model = it->second.second;
          return it->second.first;
        }
        // otherwise cache hit but possibly no model, then refresh it
        cexcache.erase(it);
      }
    }
    solver_result result = check_model0(condvec, condset, model);
    if (use_cexcache) cexcache.emplace(std::move(condset), std::make_pair(result, model));
    return result;
  }

  // On top of check_cexcached_model0, we can query a point of symbolic variable
  CheckResult check_cexcached_model(const std::vector<objiter_t>& condvec, std::set<PtrVal>& condset,
                                    simple_ptr<SymV> query_expr, bool require_model) {
    ASSERT(!require_model || !query_expr, "Choose either query_expr or require_model, but not both");
    std::shared_ptr<Model> model = (require_model || query_expr) ? std::make_shared<Model>() : nullptr;
    solver_result result = check_cexcached_model0(condvec, condset, model);
    if (result == sat && query_expr) {
      auto& e = objcache.at(query_expr).first;
      model->emplace(query_expr, self()->get_model_value_internal(e));
    }
    return std::make_pair(result, model);
  }

  template <template <typename> typename T>
  void get_indep_conds(const T<PtrVal>& conds, std::vector<objiter_t>& condvec, std::set<PtrVal>& condset, simple_ptr<SymV> query_expr) {
    /*
    if (!use_cons_indep) {
      condset.insert(conds.begin(), conds.end());
      return;
    }
    */
    ASSERT(use_cons_indep, "why not?");
    if (conds.size() <= (query_expr!=nullptr ? 0 : 1)) {
      // Not necessary to use independence solver since conds is too small
      condset.insert(conds.begin(), conds.end());
      return;
    }
    std::vector<objiter_t> queue;
    objiter_t cur;
    int idx;

    for (auto& v: conds) condvec.push_back(objcache.find(v));

    if (query_expr == nullptr) {
      cur = condvec.back();
      condvec.pop_back();
      idx = 1;
      queue.push_back(cur);
      condset.insert(cur->first);
    } else {
      cur = objcache.find(query_expr);
      idx = 0;
    }

    do {
      auto& cset = cur->second.second;
      for (auto& next: condvec) if (next != objcache.end()) {
        auto& nset = next->second.second;
        auto cit = cset.begin();
        auto nit = nset.begin();
        if (cit != cset.end() && nit != nset.end()) {
          do if (nit->first == cit->first) {
            condset.insert(next->first);
            queue.push_back(next);
            next = objcache.end();
            break;
          } while (nit->first < cit->first ? ++nit != nset.end() : ++cit != cset.end());
        }
      }
    } while (idx < queue.size() && (cur = queue[idx++], true));

    condvec = std::move(queue);
  }

  template <template <typename> typename T>
  CheckResult check_model(const T<PtrVal>& conds, simple_ptr<SymV> query_expr = nullptr, bool require_model = false) {
    ASSERT(!query_expr || !require_model, "Conflicting request");
    // translation
    if (!use_objcache) objcache.clear(); // XXX: if not using objcache, why bother clear it?
    for (auto &v: conds) construct_expr(v);
    if (query_expr) construct_expr(query_expr);

    // local storage
    std::vector<objiter_t> condvec;
    std::set<PtrVal> condset;

    if (!use_cons_indep) {
      condset.insert(conds.begin(), conds.end());
      return check_cexcached_model(condvec, condset, query_expr, require_model);
    }
    if (!require_model) {
      get_indep_conds(conds, condvec, condset, query_expr);
      return check_cexcached_model(condvec, condset, query_expr, require_model);
    }

    // constraint independence resolving
    solver_result check_result;
    std::vector<PtrVal> curr_conds(conds.begin(), conds.end());
    std::shared_ptr<Model> model = std::make_shared<Model>();

    while (curr_conds.size() > 0) {
      get_indep_conds(curr_conds, condvec, condset, query_expr);
      int before_size = curr_conds.size();
      for (auto& v: condset) {
        curr_conds.erase(std::remove(curr_conds.begin(), curr_conds.end(), v), curr_conds.end());
      }
      ASSERT(curr_conds.size() < before_size, "Invalid elimination");
      auto [sub_result, sub_model] = check_cexcached_model(condvec, condset, query_expr, require_model);
      if (model->size() == 0)
        check_result = sub_result;
      // Note (Ruiqi): make sure that sub_model's key won't overlap, because std::unordered_map::insert will not update for same key.
      model->insert(sub_model->begin(), sub_model->end());
      condvec.clear();
      condset.clear();
    }
    return std::make_pair(check_result, model);
  }

  // interfaces

  virtual bool check_pc(PC pc) override {
    if (!use_solver) return true;
    br_query_num++;
    auto [r, m] = check_model(pc.get_path_conds());
    return r == sat;
  }

  virtual std::pair<bool, UIntData> get_sat_value(PC pc, PtrVal v) override {
    auto v2 = std::dynamic_pointer_cast<SymV>(v);
    //ASSERT(v2->is_var(), "concretizing a compound symbolic expression; try add equational constraint");
    auto [r, m] = check_model(pc.get_path_conds(), v2);
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
        auto [r, m] = check_model(new_conds);
        if (r != sat)
          new_conds.pop_back();
      }
      res = check_model(new_conds, nullptr, true);
    } else {
      res = check_model(conds, nullptr, true);
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
