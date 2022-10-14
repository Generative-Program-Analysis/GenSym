#ifndef GS_Z3_HEADER
#define GS_Z3_HEADER

#include "z3++.h"

using namespace z3;

class CheckerZ3 : public CachedChecker<CheckerZ3, expr, model> {
private:
  context* ctx;
  solver* g_solver;
public:
  CheckerZ3() {
    std::cout << "Use Z3 " << Z3_get_full_version() << "\n";
    ctx = new context;
    g_solver = new solver(*ctx);
  }
  virtual ~CheckerZ3() override {
    clear_cache();
    delete g_solver;
  }
  void add_constraint_internal(expr e) {
    g_solver->add(e);
  }
  solver_result check_model_internal() {
    auto result = g_solver->check();
    return (solver_result) result;
  }

  inline std::shared_ptr<model> get_model_internal(BrCacheKey& conds) {
    return std::make_shared<model>(g_solver->get_model());
  }

  inline IntData eval(expr val) {
    auto const_val = g_solver->get_model().eval(val, true);
    return const_val.get_numeral_uint64();
  }

  inline IntData eval_model(std::shared_ptr<model> m, PtrVal val) {
    return m->eval(construct_expr(val), true).get_numeral_uint64();
  }

  expr construct_expr_internal(PtrVal e) {
    auto int_e = std::dynamic_pointer_cast<IntV>(e);
    if (int_e) {
      // XXX(GW): using this vs sym_bool_const?
      if (int_e->bw == 1)
        return ctx->bool_val(int_e->i ? true : false);
      return ctx->bv_val(int_e->as_signed(), int_e->bw);
    }

    auto sym_e = std::dynamic_pointer_cast<SymV>(e);
    if (!sym_e) ABORT("Non-symbolic/integer value in path condition");
    if (sym_e->is_var()) {
      ASSERT(sym_e->bw > 1, "Named symbolic constant of size 1");
      auto ret = ctx->bv_const(sym_e->name.c_str(), sym_e->bw);
      return ret;
    }
    std::vector<expr> expr_rands;
    for (auto& rand : sym_e->rands) {
      expr_rands.push_back(construct_expr(rand));
    }
    int bw = sym_e->bw;
    switch (sym_e->rator) {
      case iOP::op_add:
        return expr_rands[0] + expr_rands[1];
      case iOP::op_sub:
        return expr_rands[0] - expr_rands[1];
      case iOP::op_mul:
        return expr_rands[0] * expr_rands[1];
      case iOP::op_sdiv:
      case iOP::op_udiv:
        return expr_rands[0] / expr_rands[1];
      case iOP::op_uge:
        return uge(expr_rands[0], expr_rands[1]);
      case iOP::op_sge:
        return sge(expr_rands[0], expr_rands[1]);
      case iOP::op_ugt:
        return ugt(expr_rands[0], expr_rands[1]);
      case iOP::op_sgt:
        return sgt(expr_rands[0], expr_rands[1]);
      case iOP::op_ule:
        return ule(expr_rands[0], expr_rands[1]);
      case iOP::op_sle:
        return sle(expr_rands[0], expr_rands[1]);
      case iOP::op_ult:
        return ult(expr_rands[0], expr_rands[1]);
      case iOP::op_slt:
        return slt(expr_rands[0], expr_rands[1]);
      case iOP::op_eq:
        return expr_rands[0] == expr_rands[1];
      case iOP::op_neq:
        return expr_rands[0] != expr_rands[1];
      case iOP::op_neg: {
        ASSERT(expr_rands[0].get_sort().is_bool(), "negating a non-boolean");
        return !expr_rands[0];
      }
      case iOP::op_bvnot: {
        ASSERT(expr_rands[0].get_sort().is_bv(), "negating a non-bitvector");
        return ~expr_rands[0];
      }
      case iOP::op_sext: {
        auto v = expr_rands[0];
        if (v.get_sort().is_bool())
          v = ite(v, ctx->bv_val(1, 1), ctx->bv_val(0, 1));
        auto ext_size = bw - v.get_sort().bv_size();
        ASSERT(ext_size >= 0, "negative sign extension size");
        if (ext_size > 0) return sext(v, ext_size);
        return v;
      }
      case iOP::op_zext: {
        auto v = expr_rands[0];
        if (v.get_sort().is_bool())
          v = ite(v, ctx->bv_val(1, 1), ctx->bv_val(0, 1));
        auto ext_size = bw - v.get_sort().bv_size();
        ASSERT(ext_size >= 0, "negative zero extension size");
        if (ext_size > 0) return zext(v, ext_size);
        return v;
      }
      case iOP::op_shl:
        return shl(expr_rands[0], expr_rands[1]);
      case iOP::op_lshr:
        return lshr(expr_rands[0], expr_rands[1]);
      case iOP::op_ashr:
        return ashr(expr_rands[0], expr_rands[1]);
      case iOP::op_and:
        return expr_rands[0] & expr_rands[1];
      case iOP::op_or:
        return expr_rands[0] | expr_rands[1];
      case iOP::op_xor:
        return expr_rands[0] ^ expr_rands[1];
      case iOP::op_urem:
        return urem(expr_rands[0], expr_rands[1]);
      case iOP::op_srem:
        return srem(expr_rands[0], expr_rands[1]);
      case iOP::op_trunc: {
        // XXX is it right?
        auto v = expr_rands[0].extract(bw-1, 0);
        if (1 == bw) {
          v = (ctx->bv_val(1, 1) == v);
        }
        return v;
      }
      case iOP::op_concat:
        return concat(expr_rands[0], expr_rands[1]);
      case iOP::op_extract:
        return expr_rands[0].extract(
                                expr_rands[1].get_numeral_uint(),
                                expr_rands[2].get_numeral_uint());
      case iOP::op_ite: {
        auto cond = expr_rands[0];
        auto v_t = expr_rands[1];
        auto v_e = expr_rands[2];
        ASSERT(cond.get_sort().is_bool(), "Non Boolean condition");
        ASSERT(((v_t.get_sort().is_bv() && v_e.get_sort().is_bv()) || (v_t.get_sort().is_bool() && v_e.get_sort().is_bool())), "Operation between different type");
        return ite(cond, v_t, v_e);
      }
      case iOP::const_true:
        return ctx->bool_val(true);
      case iOP::const_false:
        return ctx->bool_val(false);
      default: break;
    }
    ABORT("unkown operator when constructing STP expr");
    return ctx->bv_const("x", 32);
  }
  void push_internal() {
    // XXX: z3's pop/push operation is quite expensive!
    g_solver->push();
  }
  void pop_internal() {
    g_solver->pop();
  }
  void reset_internal() {
    g_solver->reset();
  }
};

#endif
