#ifndef LLSC_SMT_CHECKER_HEADERS
#define LLSC_SMT_CHECKER_HEADERS

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
  virtual void generate_test(PC pc) = 0;
};

template <typename Self, typename Expr>
class CachedChecker : public Checker {
  Self* self() {
    return static_cast<Self*>(this);
  }

  void push() {
    self()->push_internal();
  }

  void pop() {
    self()->pop_internal();
  }

  solver_result check_model() {
    auto start = steady_clock::now();
    solver_result result = self()->check_model_internal();
    auto end = steady_clock::now();
    ext_solver_time += duration_cast<microseconds>(end - start).count();
    return result;
  }

public:
  using VarMap = std::map<simple_ptr<SymV>, Expr>;
  using ExprDetail = std::tuple<Expr, VarMap>;
  std::unordered_map<PtrVal, ExprDetail> objcache;

  using Model = std::unordered_map<simple_ptr<SymV>, IntData>;
  using CheckResult = std::tuple<solver_result, std::shared_ptr<Model>>;
  std::map<std::set<PtrVal>, CheckResult> cexcache;

  void clear_cache() {
    objcache.clear();
    cexcache.clear();
  }

  const ExprDetail& construct_expr(PtrVal e) {
    if (auto it = objcache.find(e); it != objcache.end())
      return it->second;
    VarMap tmp;
    auto expr = self()->construct_expr_internal(e, tmp);
    auto [it, ins] = objcache.emplace(e, std::make_tuple(expr, std::move(tmp)));
    return it->second;
  }

  template <template <typename> typename Cont>
  CheckResult check_model(
        const Cont<PtrVal>& conds,
        simple_ptr<SymV> query_expr=nullptr,
        bool require_model=false) {

    push();

    // translation
    if (!use_objcache)
      objcache.clear();
    for (auto &v: conds)
      construct_expr(v);
    if (query_expr)
      construct_expr(query_expr);

    // local storage
    using objiter_t = typename decltype(objcache)::iterator;
    std::vector<objiter_t> condvec;
    std::set<PtrVal> condset;

    // constraint independence resolving
    if (use_cons_indep && conds.size() > (query_expr ? 0 : 1)) {
      std::vector<objiter_t> queue;
      objiter_t cur;
      int idx;

      for (auto& v: conds) {
        condvec.push_back(objcache.find(v));
      }
      if (!query_expr) {
        cur = condvec.back(); condvec.pop_back();
        idx = 1;
        queue.push_back(cur);
        condset.insert(cur->first);
      }
      else {
        cur = objcache.find(query_expr);
        idx = 0;
      }

      do {
        auto& cset = std::get<1>(cur->second);
        for (auto& next: condvec) if (next != objcache.end()) {
          auto& nset = std::get<1>(next->second);
          auto cit = cset.begin();
          auto nit = nset.begin();
          if (cit != cset.end() && nit != nset.end()) {
            do if (nit->first == cit->first) {
              condset.insert(next->first);
              queue.push_back(next);
              next = objcache.end();
              break;
            }
            while (nit->first < cit->first ? ++nit != nset.end() : ++cit != cset.end());
          }
        }
      }
      while (idx < queue.size() && (cur = queue[idx++], true));
      condvec = std::move(queue);
    } else {
      condset.insert(conds.begin(), conds.end());
    }

    //solving with counterexample caching
    if (use_cexcache && (!query_expr || query_expr->name.size())) {
      if (auto it = cexcache.find(condset); it != cexcache.end()) {
        pop();
        return it->second;
      }
    }

    //assert and check
    VarMap varmap;
    if (condvec.size()) {  // use local cache if possible
      for (auto& v: condvec) {
        auto& [e, vm] = v->second;
        self()->add_constraint_internal(e);
        varmap.insert(vm.begin(), vm.end());
      }
    }
    else {
      for (auto& v: condset) {
        auto& [e, vm] = objcache.at(v);
        self()->add_constraint_internal(e);
        varmap.insert(vm.begin(), vm.end());
      }
    }
    solver_result result = check_model();

    //get model
    std::shared_ptr<Model> model;
    if (result == sat && (use_cexcache || require_model)) {
      model = std::make_shared<Model>();
      for (auto& [v, e]: varmap) {
        model->emplace(v, self()->get_value_internal(e));
      }
    }
    if (use_cexcache) {
      cexcache.emplace(std::move(condset), std::make_tuple(result, model));
    }
    if (result == sat && query_expr) {  // !require_model
      model = std::make_shared<Model>();
      auto& e = std::get<0>(objcache.at(query_expr));
      model->emplace(query_expr, self()->get_value_internal(e));
    }
    pop();
    return std::make_tuple(result, model);
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
    auto [r, m] = check_model(pc.get_path_conds(), v2);
    return std::make_pair(r == sat, r == sat ? m->at(v2) : 0);
  }

  virtual void generate_test(PC pc) override {
    if (!use_solver) return;
    if (mkdir("tests", 0777) == -1) {
      if (errno == EEXIST) { }
      else ABORT("Cannot create the folder tests, abort.\n");
    }
    std::stringstream output;
    output << "Query number: " << (test_query_num+1) << std::endl;
    auto [result, model] = check_model(pc.get_path_conds(), nullptr, true);
    output << "Query is " << check_result_to_string(result) << std::endl;
    if (result == sat) {
      test_query_num++;
      std::stringstream filename;
      filename << "tests/" << test_query_num << ".test";
      int out_fd = open(filename.str().c_str(), O_RDWR | O_CREAT, 0777);
      if (out_fd == -1) {
        ABORT("Cannot create the test case file, abort.\n");
      }
      for (auto [k, v]: *model) {
        output << k->name << " == " << v << std::endl;
      }
      int n = write(out_fd, output.str().c_str(), output.str().size());
      close(out_fd);
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
  checker_manager.get_checker().generate_test(std::move(state.get_PC()));
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
