#ifndef SMT_SOLVER_HPP
#define SMT_SOLVER_HPP

#include "concrete_rt.hpp"
#include "symbolic_rt.hpp"
#include "utils.hpp"
#include "wasm/profile.hpp"
#include "z3++.h"
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
    z3::solver z3_solver(z3_ctx);
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
  z3::expr build_z3_expr(SymVal &sym_val);

private:
  z3::expr to_z3_conjunction(std::vector<SymVal> &conditions) {
    z3::expr conjunction = z3_ctx.bool_val(true);
    for (auto &cond : conditions) {
      auto z3_cond = build_z3_expr(cond);
      conjunction = conjunction && z3_cond != z3_ctx.bv_val(0, 32);
    }
#ifdef DEBUG
    std::cout << "Symbolic conditions size: " << conditions.size() << std::endl;
    std::cout << "Solving conditions: " << conjunction << std::endl;
#endif
    return conjunction;
  }

  z3::context z3_ctx;
  z3::expr build_z3_expr_aux(SymVal &sym_val);
};

static Solver solver;

inline z3::expr Solver::build_z3_expr_aux(SymVal &sym_val) {
  if (auto sym = std::dynamic_pointer_cast<Symbol>(sym_val.symptr)) {
    return z3_ctx.bv_const(("s_" + std::to_string(sym->get_id())).c_str(), 32);
  } else if (auto concrete =
                 std::dynamic_pointer_cast<SymConcrete>(sym_val.symptr)) {
    return z3_ctx.bv_val(concrete->value.value, 32);
  } else if (auto smallbv =
                 std::dynamic_pointer_cast<SmallBV>(sym_val.symptr)) {
    return z3_ctx.bv_val(smallbv->get_value(), smallbv->get_size());
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
    case LTU: {
      auto temp_bool = z3::ult(left, right);
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
    case GTU: {
      auto temp_bool = z3::ugt(left, right);
      return z3::ite(temp_bool, one_bv, zero_bv);
    }
    case GEU: {
      auto temp_bool = z3::uge(left, right);
      return z3::ite(temp_bool, one_bv, zero_bv);
    }
    case SHR: {
      return z3::lshr(left, right);
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
    case B_AND: {
      return left & right;
    }
    case B_XOR: {
      return left ^ right;
    }
    case CONCAT: {
      return z3::concat(left, right);
    }
    default:
      throw std::runtime_error("Operation not supported: " +
                               std::to_string(binary->op));
    }
  } else if (auto extract = dynamic_cast<SymExtract *>(sym_val.symptr.get())) {
    assert(extract);
    int high = extract->high * 8 - 1;
    int low = extract->low * 8 - 8;
    auto s = build_z3_expr(extract->value);
    auto res = s.extract(high, low);
    return res;
  }
  throw std::runtime_error("Unsupported symbolic value type");
}

inline z3::expr Solver::build_z3_expr(SymVal &sym_val) {
  if (sym_val.symptr->z3_expr()) {
    return *sym_val.symptr->z3_expr();
  }
  auto e = build_z3_expr_aux(sym_val);
  sym_val.symptr->update_z3_expr(e);
  return e;
}

inline EvalRes eval_sym_expr_by_model(const SymVal &sym, z3::model &model) {
  auto expr = solver.build_z3_expr(const_cast<SymVal &>(sym));
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
  Profile.remove_instruction_time(TimeProfileKind::INSTR, time_need_to_be_removed.count());
  if (result.has_value()) {
    std::cout << "Symbolic assertion failed" << std::endl;
    throw std::runtime_error("Symbolic assertion failed");
  }
  return std::monostate{};
}

#endif // SMT_SOLVER_HPP