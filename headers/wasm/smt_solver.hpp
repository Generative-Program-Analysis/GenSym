#ifndef SMT_SOLVER_HPP
#define SMT_SOLVER_HPP

#include "concrete_rt.hpp"
#include "symbolic_rt.hpp"
#include "utils.hpp"
#include "wasm/profile.hpp"
#include "z3++.h"
#include "z3_env.hpp"
#include <array>
#include <memory>
#include <set>
#include <string>
#include <tuple>
#include <variant>
#include <vector>

class Solver {
public:
  Solver() {}
  std::optional<std::pair<NumMap, z3::model>>
  solve(std::vector<SymVal> &conditions) {
    z3::solver z3_solver(global_z3_ctx());
    z3::check_result solver_result;
    {
      auto timer = ManagedTimer(TimeProfileKind::SOLVER);
      // make an conjunction of all conditions
      auto conjunction = to_z3_conjunction(conditions);
      // call z3 to solve the condition
      // NOTE: half of the solver time is spent in solver.add
      z3_solver.add(conjunction);
      solver_result = z3_solver.check();
    }
    switch (solver_result) {
    case z3::unsat:
      return std::nullopt; // No solution found
    case z3::sat: {
      z3::model model = z3_solver.get_model();
      NumMap result;
      // Reference:
      // https://github.com/Z3Prover/z3/blob/master/examples/c%2B%2B/example.cpp#L59
      GENSYM_INFO("Solved Z3 model");
      GENSYM_INFO(model);
      for (unsigned i = 0; i < model.size(); ++i) {
        z3::func_decl var = model[i];
        z3::expr value = model.get_const_interp(var);
        std::string name = var.name().str();
        if (starts_with(name, "s_")) {
          int id = std::stoi(name.substr(2));
          result[id] = Num(value.get_numeral_int64());
        } else {
          GENSYM_INFO("Find a variable that is not created by GenSym: " + name);
        }
      }
      return std::optional<std::pair<NumMap, z3::model>>(
          std::in_place, std::move(result), std::move(model));
    }
    case z3::unknown:
      throw std::runtime_error("Z3 solver returned unknown status");
    }
    return std::nullopt; // Should not reach here
  }

private:
  z3::expr to_z3_conjunction(std::vector<SymVal> &conditions) {
    z3::expr conjunction = global_z3_ctx().bool_val(true);
    for (auto &cond : conditions) {
      auto z3_cond = cond.symptr->build_z3_expr();
      conjunction = conjunction && z3_cond != global_z3_ctx().bv_val(0, 32);
    }
#ifdef DEBUG
    // std::cout << "Symbolic conditions size: " << conditions.size() <<
    // std::endl; std::cout << "Solving conditions: " << conjunction <<
    // std::endl;
#endif
    return conjunction;
  }
};

static Solver solver;

inline EvalRes eval_sym_expr_by_model(const SymVal &sym, z3::model &model) {
  auto expr = sym.symptr->build_z3_expr();
  // let z3 decide the value of symbols that are not in the model
  z3::expr value = model.eval(expr, true);
  // every value is bitvector
  int width = expr.get_sort().bv_size();
  return EvalRes(Num(value.get_numeral_int64()), width);
}

inline std::monostate GENSYM_SYM_ASSERT(SymVal &sym_cond) {
  std::vector<SymVal> conds = ExploreTree.collect_current_path_conds();
  conds.push_back(sym_cond.negate());
  auto start = std::chrono::steady_clock::now();
  auto result = solver.solve(conds);
  auto end = std::chrono::steady_clock::now();
  auto time_need_to_be_removed = std::chrono::duration<double>(end - start);
  Profile.remove_instruction_time(TimeProfileKind::INSTR,
                                  time_need_to_be_removed.count());
  if (result.has_value()) {
    std::cout << "Symbolic assertion failed" << std::endl;
    throw std::runtime_error("Symbolic assertion failed");
  }
  return std::monostate{};
}

#endif // SMT_SOLVER_HPP