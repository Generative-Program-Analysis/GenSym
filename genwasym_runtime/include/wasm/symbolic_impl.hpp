#ifndef WASM_SYMBOLIC_IMPL_HPP
#define WASM_SYMBOLIC_IMPL_HPP

#include "symbolic_decl.hpp"
#include "wasm/symval_decl.hpp"
#include "wasm/z3_env.hpp"

inline z3::expr Symbolic::build_z3_expr_aux() {
  if (auto sym = dynamic_cast<Symbol *>(this)) {
    switch (sym->value_kind()) {

    case KindBV: {
      return global_z3_ctx().bv_const(
          ("s_int" + std::to_string(sym->get_id())).c_str(), width());
    }
    case KindBool: {
      assert(false && "Symbolic boolean variables are not supported yet");
    }
    case KindFP:
      if (sym->width() == 32) {
        return global_z3_ctx().fpa_const<32>(
            ("s_f32" + std::to_string(sym->get_id())).c_str());
      } else if (sym->width() == 64) {
        return global_z3_ctx().fpa_const<64>(
            ("s_f64" + std::to_string(sym->get_id())).c_str());
      } else {
        throw std::runtime_error("Unsupported floating-point width: " +
                                 std::to_string(sym->width()));
      }
    }
  } else if (auto witness = dynamic_cast<Witness *>(this)) {
    return global_z3_ctx().bv_const("witness", 32);
  } else if (auto concrete = dynamic_cast<SymConcrete *>(this)) {
    switch (concrete->kind) {
    case KindBool: {
      return global_z3_ctx().bool_val(concrete->value.toInt() != 0);
    }
    case KindBV: {
      return global_z3_ctx().bv_val(concrete->value.value, width());
    }
    case KindFP: {
      if (width() == 32) {
        return global_z3_ctx().fpa_val(concrete->value.toF32());
      } else if (width() == 64) {
        return global_z3_ctx().fpa_val(concrete->value.toF64());
      } else {
        throw std::runtime_error("Unsupported floating-point width: " +
                                 std::to_string(width()));
      }
    }
    }
  } else if (auto binary = dynamic_cast<SymBinary *>(this)) {
    auto bit_width = width();

    z3::expr left = binary->lhs->z3_expr();
    z3::expr right = binary->rhs->z3_expr();
    switch (binary->op) {
    case EQ_BOOL: {
      return left == right;
    }
    case NEQ_BOOL: {
      return left != right;
    }
    case AND: {
      return left && right;
    }
    case OR: {
      return left || right;
    }
    case LT_BOOL: {
      return left < right;
    }
    case LTU_BOOL: {
      return z3::ult(left, right);
    }
    case LEQ_BOOL: {
      return left <= right;
    }
    case LEU_BOOL: {
      return z3::ule(left, right);
    }
    case GT_BOOL: {
      return left > right;
    }
    case GTU_BOOL: {
      return z3::ugt(left, right);
    }
    case GEU_BOOL: {
      return z3::uge(left, right);
    }
    case SHL: {
      if (bit_width == 32) {
        z3::expr shift_mask = global_z3_ctx().bv_val(0x1F, bit_width);
        return z3::shl(left, right & shift_mask);
      } else if (bit_width == 64) {
        z3::expr shift_mask = global_z3_ctx().bv_val(0x3F, bit_width);
        return z3::shl(left, right & shift_mask);
      } else {
        throw std::runtime_error("Unsupported bit width for SHL: " +
                                 std::to_string(bit_width));
      }
    }
    case SHR_U: {
      return z3::lshr(left, right);
    }
    case SHR_S: {
      return z3::ashr(left, right);
    }
    case REM_U: {
      return z3::urem(left, right);
    }
    case GEQ_BOOL: {
      return left >= right;
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
    case DIV_U: {
      return z3::udiv(left, right);
    }
    case B_AND: {
      return left & right;
    }
    case B_XOR: {
      return left ^ right;
    }
    case B_OR: {
      return left | right;
    }
    case CONCAT: {
      return z3::concat(left, right);
    }
    default:
      throw std::runtime_error("Operation not supported: " +
                               std::to_string(binary->op));
    }
  } else if (auto unary = dynamic_cast<SymUnary *>(this)) {
    auto bit_width = 32;
    z3::expr zero_bv = global_z3_ctx().bv_val(0, bit_width);
    z3::expr one_bv = global_z3_ctx().bv_val(1, bit_width);
    switch (unary->op) {
    case NOT: {
      return !unary->value->z3_expr();
    }
    case BOOL2BV: {
      z3::expr bool_expr = unary->value->z3_expr();
      return z3::ite(bool_expr, one_bv, zero_bv);
    }
    default:
      throw std::runtime_error("Unary operation not supported: " +
                               std::to_string(unary->op));
    }
  } else if (auto extract = dynamic_cast<SymExtract *>(this)) {
    assert(extract);
    int high = extract->high * 8 - 1;
    int low = extract->low * 8 - 8;
    auto s = extract->value->z3_expr();
    auto res = s.extract(high, low);
    return res;
  }
  throw std::runtime_error("Unsupported symbolic value type");
}

inline z3::expr Symbolic::z3_expr() {
  if (_z3_expr.has_value()) {
    return *_z3_expr;
  }
  auto e = build_z3_expr_aux();
  _z3_expr = e;
  return e;
}

#endif // WASM_SYMBOLIC_IMPL_HPP
