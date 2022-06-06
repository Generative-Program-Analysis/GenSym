#ifndef LLSC_SMT_CHECKER_HEADERS
#define LLSC_SMT_CHECKER_HEADERS

// TODO: generic caching mechanisms should be shared no matter the solver
class Checker {
public:
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
  virtual void init_solvers() = 0;
  virtual void destroy_solvers() = 0;
  virtual solver_result make_query(PC pc) = 0;
  virtual std::pair<bool, UIntData> concretize(PC pc, PtrVal v) = 0;
  virtual void push() = 0;
  virtual void pop() = 0;
  virtual void reset() = 0;
  virtual void print_model(std::stringstream&) = 0;

  bool check_pc(PC pc) {
    if (!use_solver) return true;
    br_query_num++;
    push();
    auto r = make_query(std::move(pc));
    pop();
    return r == sat;
  }
  std::pair<bool, UIntData> get_sat_value(PC pc, PtrVal v) {
    push();
    auto r = concretize(std::move(pc), v);
    pop();
    return r;
  }
  void generate_test(PC pc) {
    if (!use_solver) return;
    if (mkdir("tests", 0777) == -1) {
      if (errno == EEXIST) { }
      else ABORT("Cannot create the folder tests, abort.\n");
    }
    std::stringstream output;
    output << "Query number: " << (test_query_num+1) << std::endl;
    push();
    // XXX: reset harms performance a lot of Z3
    auto result = make_query(std::move(pc));
    output << "Query is " << check_result_to_string(result) << std::endl;
    if (result == sat) {
      test_query_num++;
      std::stringstream filename;
      filename << "tests/" << test_query_num << ".test";
      int out_fd = open(filename.str().c_str(), O_RDWR | O_CREAT, 0777);
      if (out_fd == -1) {
        ABORT("Cannot create the test case file, abort.\n");
      }
      print_model(output);
      int n = write(out_fd, output.str().c_str(), output.str().size());
      close(out_fd);
    }
    pop();
  }
};

#include "smt_stp.hpp"
#include "smt_z3.hpp"

inline Checker& get_checker() {
  if (solver_kind == SolverKind::z3) {
    static CheckerZ3 cz3;
    return cz3;
  } else if (solver_kind == SolverKind::stp) {
    static CheckerSTP cstp;
    return cstp;
  }
  ABORT("unknown solver");
}

// To be compatible with generated code:

inline bool check_pc(PC pc) { return get_checker().check_pc(std::move(pc)); }
inline void check_pc_to_file(SS state) { get_checker().generate_test(std::move(state.get_PC())); }
inline std::pair<bool, UIntData> get_sat_value(PC pc, PtrVal v) { return get_checker().get_sat_value(std::move(pc), v); }

#endif
