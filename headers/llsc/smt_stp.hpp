#ifndef LLSC_STP_HEADER
#define LLSC_STP_HEADER

#include <stp/c_interface.h>
#include <stp_handle.hpp>

struct ExprHandle: public std::shared_ptr<void> {
  typedef std::shared_ptr<void> Base;

  static void freeExpr(Expr e) {
    vc_DeleteExpr(e);
  }

  ExprHandle(Expr e): Base(e, freeExpr) {}
};

class CheckerSTP : public CachedChecker<CheckerSTP, ExprHandle> {
public:
  VC vc;

  ExprHandle construct_expr_internal(PtrVal e, VarMap &vars) {
    auto int_e = std::dynamic_pointer_cast<IntV>(e);
    if (int_e) {
      // XXX(GW): using this vs sym_bool_const?
      if (1 == int_e->bw)
        return int_e->i ? vc_trueExpr(vc) : vc_falseExpr(vc);
      return vc_bvConstExprFromLL(vc, int_e->bw, int_e->as_signed());
    }
    auto sym_e = std::dynamic_pointer_cast<SymV>(e);
    if (!sym_e) ABORT("Non-symbolic/integer value in path condition");
    if (!sym_e->name.empty()) {
      ASSERT(sym_e->bw > 1, "Named symbolic constant of size 1");
      auto name = sym_e->name;
      ExprHandle stp_expr = vc_varExpr(vc, name.c_str(), vc_bvType(vc, sym_e->bw));
      vars.emplace(sym_e, stp_expr);
      return stp_expr;
    }

    std::vector<ExprHandle> expr_rands;
    int bw = sym_e->bw;
    for (auto e : sym_e->rands) {
      auto& [e2, vm] = construct_expr(e);
      expr_rands.push_back(e2);
      vars.insert(vm.begin(), vm.end());
    }
    switch (sym_e->rator) {
    case iOP::op_add:
      return vc_bvPlusExpr(vc, bw, expr_rands[0].get(), expr_rands[1].get());
    case iOP::op_sub:
      return vc_bvMinusExpr(vc, bw, expr_rands[0].get(), expr_rands[1].get());
    case iOP::op_mul:
      return vc_bvMultExpr(vc, bw, expr_rands[0].get(), expr_rands[1].get());
    case iOP::op_sdiv:
    case iOP::op_udiv:
      return vc_bvDivExpr(vc, bw, expr_rands[0].get(), expr_rands[1].get());
    case iOP::op_uge:
      return vc_bvGeExpr(vc, expr_rands[0].get(), expr_rands[1].get());
    case iOP::op_sge:
      return vc_sbvGeExpr(vc, expr_rands[0].get(), expr_rands[1].get());
    case iOP::op_ugt:
      return vc_bvGtExpr(vc, expr_rands[0].get(), expr_rands[1].get());
    case iOP::op_sgt:
      return vc_sbvGtExpr(vc, expr_rands[0].get(), expr_rands[1].get());
    case iOP::op_ule:
      return vc_bvLeExpr(vc, expr_rands[0].get(), expr_rands[1].get());
    case iOP::op_sle:
      return vc_sbvLeExpr(vc, expr_rands[0].get(), expr_rands[1].get());
    case iOP::op_ult:
      return vc_bvLtExpr(vc, expr_rands[0].get(), expr_rands[1].get());
    case iOP::op_slt:
      return vc_sbvLtExpr(vc, expr_rands[0].get(), expr_rands[1].get());
    case iOP::op_eq:
      return vc_eqExpr(vc, expr_rands[0].get(), expr_rands[1].get());
    case iOP::op_neq:
      return vc_notExpr(vc, vc_eqExpr(vc, expr_rands[0].get(), expr_rands[1].get()));
    case iOP::op_neg: {
      ASSERT(BOOLEAN_TYPE == getType(expr_rands[0].get()), "negating a non-boolean");
      return vc_notExpr(vc, expr_rands[0].get());
    }
    case iOP::op_bvnot: {
      ASSERT(BITVECTOR_TYPE == getType(expr_rands[0].get()), "negating a non-bitvector");
      return vc_bvNotExpr(vc, expr_rands[0].get());
    }
    case iOP::op_sext: {
      auto v = expr_rands[0].get();
      if (BOOLEAN_TYPE == getType(v)) {
        v = vc_boolToBVExpr(vc, v);
      }
      return vc_bvSignExtend(vc, v, bw);
    }
    case iOP::op_zext: {
      auto v = expr_rands[0].get();
      if (BOOLEAN_TYPE == getType(v)) {
        v = vc_boolToBVExpr(vc, v);
      }
      int from_bw = getBVLength(v);
      ASSERT(bw > from_bw, "Zero extend to a smaller type");
      auto left = vc_bvConstExprFromLL(vc, bw - from_bw, 0);
      return vc_bvConcatExpr(vc, left, v);
    }
    case iOP::op_shl:
      return vc_bvLeftShiftExprExpr(vc, bw, expr_rands[0].get(), expr_rands[1].get());
    case iOP::op_lshr:
      return vc_bvRightShiftExprExpr(vc, bw, expr_rands[0].get(), expr_rands[1].get());
    case iOP::op_ashr:
      return vc_bvSignedRightShiftExprExpr(vc, bw, expr_rands[0].get(), expr_rands[1].get());
    case iOP::op_and: {
      auto v1 = expr_rands[0].get();
      auto v2 = expr_rands[1].get();
      ASSERT(getType(v1) == getType(v2), "Operation between different type");
      return (BOOLEAN_TYPE == getType(v1)) ? vc_andExpr(vc, v1, v2) : vc_bvAndExpr(vc, v1, v2);
    }
    case iOP::op_or: {
      auto v1 = expr_rands[0].get();
      auto v2 = expr_rands[1].get();
      ASSERT(getType(v1) == getType(v2), "Operation between different type");
      return (BOOLEAN_TYPE == getType(v1)) ? vc_orExpr(vc, v1, v2) : vc_bvOrExpr(vc, v1, v2);
    }
    case iOP::op_xor: {
      auto v1 = expr_rands[0].get();
      auto v2 = expr_rands[1].get();
      ASSERT(getType(v1) == getType(v2), "Operation between different type");
      return (BOOLEAN_TYPE == getType(v1)) ? vc_xorExpr(vc, v1, v2) : vc_bvXorExpr(vc, v1, v2);
    }
    case iOP::op_urem:
      return vc_bvRemExpr(vc, bw, expr_rands[0].get(), expr_rands[1].get());
    case iOP::op_srem:
      return vc_sbvRemExpr(vc, bw, expr_rands[0].get(), expr_rands[1].get());
    case iOP::op_trunc: {
      // bvExtract(vc, e, h, l) -> e[l:h+1]
      auto v =  vc_bvExtract(vc, expr_rands[0].get(), bw-1, 0);
      if (1 == bw) {
        v = vc_eqExpr(vc, v, vc_bvConstExprFromLL(vc, 1, 1));
      }
      return v;
    }
    case iOP::op_concat:
      return vc_bvConcatExpr(vc, expr_rands[0].get(), expr_rands[1].get());
    case iOP::op_extract:
      return vc_bvExtract(vc, expr_rands[0].get(),
                              getBVInt(expr_rands[1].get()),
                              getBVInt(expr_rands[2].get()));
    case iOP::op_ite: {
      auto cond = expr_rands[0].get();
      auto v_t = expr_rands[1].get();
      auto v_e = expr_rands[2].get();
      ASSERT(BOOLEAN_TYPE == getType(cond), "Non Boolean condition");
      ASSERT(getType(v_t) == getType(v_e), "Operation between different type");
      //ASSERT(getBVLength(v_t) == getBVLength(v_e),"Operation between different bv_length");
      return vc_iteExpr(vc, cond, v_t, v_e);
    }
    case iOP::const_true:
      return vc_trueExpr(vc);
    case iOP::const_false:
      return vc_falseExpr(vc);
    default: break;
    }
    ABORT("unkown operator when constructing STP expr");
  }

  void add_constraint_internal(ExprHandle e) {
    vc_assertFormula(vc, e.get());
  }

  solver_result check_model_internal() {
    ExprHandle fls = vc_falseExpr(vc);
    int retcode = vc_query(vc, fls.get());
    static solver_result mapping[4] = {sat, unsat, unknown, unknown};
    return mapping[retcode];
  }

  IntData get_value_internal(ExprHandle val) {
    ExprHandle const_val = vc_getCounterExample(vc, val.get());
    auto ret = getBVUnsignedLongLong(const_val.get());
    return ret;
  }

  CheckerSTP() {
    std::cout << "Use STP solver\n";
    vc = vc_createValidityChecker();
  }

  virtual ~CheckerSTP() override {
    clear_cache();
    vc_Destroy(vc);
  }

  void push_internal() {
    vc_push(vc);
  }

  void pop_internal() {
    vc_pop(vc);
  }
};

#endif
