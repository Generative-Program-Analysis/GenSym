#ifndef SMT_SOLVER_HPP
#define SMT_SOLVER_HPP

#include "concrete_rt.hpp"
#include "symbolic_rt.hpp"
#include "z3++.h"
#include <array>
#include <set>
#include <string>
#include <tuple>
#include <vector>

class Solver {
public:
  Solver() {}
  std::optional<std::vector<Num>> solve(const std::vector<SymVal> &conditions) {
    // make an conjunction of all conditions
    z3::expr conjunction = z3_ctx.bool_val(true);
    for (const auto &cond : conditions) {
      auto z3_cond = build_z3_expr(cond);
      conjunction = conjunction && z3_cond != z3_ctx.bv_val(0, 32);
    }
#ifdef DEBUG
    std::cout << "Symbolic conditions size: " << conditions.size() << std::endl;
    std::cout << "Solving conditions: " << conjunction << std::endl;
#endif
    // call z3 to solve the condition
    z3::solver z3_solver(z3_ctx);
    z3_solver.add(conjunction);
    switch (z3_solver.check()) {
    case z3::unsat:
      return std::nullopt; // No solution found
    case z3::sat: {
      z3::model model = z3_solver.get_model();
      std::vector<Num> result;
      // Reference:
      // https://github.com/Z3Prover/z3/blob/master/examples/c%2B%2B/example.cpp#L59

      std::cout << "Solved Z3 model" << std::endl << model << std::endl;
      for (unsigned i = 0; i < model.size(); ++i) {
        z3::func_decl var = model[i];
        z3::expr value = model.get_const_interp(var);
        std::string name = var.name().str();
        if (name.starts_with("s_")) {
          int id = std::stoi(name.substr(2));
          if (id >= result.size()) {
            result.resize(id + 1);
          }
          result[id] = Num(value.get_numeral_int64());
        } else {
          std::cout << "Find a variable that is not created by GenSym: " << name
                    << std::endl;
        }
      }
      return result;
    }
    case z3::unknown:
      throw std::runtime_error("Z3 solver returned unknown status");
    }
    return std::nullopt; // Should not reach here
  }

private:
  z3::context z3_ctx;
  z3::expr build_z3_expr(const SymVal &sym_val);
};

inline z3::expr Solver::build_z3_expr(const SymVal &sym_val) {
  if (auto sym = std::dynamic_pointer_cast<Symbol>(sym_val.symptr)) {
    return z3_ctx.bv_const(("s_" + std::to_string(sym->get_id())).c_str(), 32);
  } else if (auto concrete =
                 std::dynamic_pointer_cast<SymConcrete>(sym_val.symptr)) {
    return z3_ctx.bv_val(concrete->value.value, 32);
  } else if (auto binary =
                 std::dynamic_pointer_cast<SymBinary>(sym_val.symptr)) {
    auto bit_width = 32;
    z3::expr zero_bv =
        z3_ctx.bv_val(0, bit_width); // Represents 0 as a 32-bit bitvector
    z3::expr one_bv =
        z3_ctx.bv_val(1, bit_width); // Represents 1 as a 32-bit bitvector

    z3::expr left = build_z3_expr(binary->lhs);
    z3::expr right = build_z3_expr(binary->rhs);
    // TODO: make sure the semantics of these operations are aligned with wasm
    switch (binary->op) {
    case EQ: {
      auto temp_bool = left == right;
      return z3::ite(temp_bool, one_bv, zero_bv);
    }
    case NEQ: {
      auto temp_bool = left != right;
      return z3::ite(temp_bool, one_bv, zero_bv);
    }
    case LT: {
      auto temp_bool = left < right;
      return z3::ite(temp_bool, one_bv, zero_bv);
    }
    case LEQ: {
      auto temp_bool = left <= right;
      return z3::ite(temp_bool, one_bv, zero_bv);
    }
    case GT: {
      auto temp_bool = left > right;
      return z3::ite(temp_bool, one_bv, zero_bv);
    }
    case GEQ: {
      auto temp_bool = left >= right;
      return z3::ite(temp_bool, one_bv, zero_bv);
    }
    case ADD: {
      return left + right;
    }
    case SUB: {
      return left - right;
    }
    case MUL: {
      return left * right;
    }
    case DIV: {
      return left / right;
    }
    }
  }
  throw std::runtime_error("Unsupported symbolic value type");
}
#endif // SMT_SOLVER_HPP