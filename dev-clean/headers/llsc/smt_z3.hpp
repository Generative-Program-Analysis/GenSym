#ifndef LLSC_Z3_HEADERS
#define LLSC_Z3_HEADERS

#include "z3++.h"

using namespace z3;

class CheckerZ3 : public CachedChecker<CheckerZ3, expr> {
private:
  context* g_ctx;
  solver* g_solver;
public:
  CheckerZ3() {
    std::cout << "Use Z3 " << Z3_get_full_version() << "\n";
    g_ctx = new context;
    g_solver = new solver(*g_ctx);
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

  IntData get_value_internal(expr val) {
    auto const_val = g_solver->get_model().eval(val, true);
    return const_val.get_numeral_uint64();
  }
  expr construct_expr_internal(PtrVal e, VarMap &vars) {
    auto c = g_ctx;
    auto int_e = std::dynamic_pointer_cast<IntV>(e);
    if (int_e) {
      if (int_e->bw == 1)
        return c->bool_val(int_e->i ? true : false);
      return c->bv_val(int_e->as_signed(), int_e->bw);
    }
    auto sym_e = std::dynamic_pointer_cast<SymV>(e);
    if (!sym_e) ABORT("Non-symbolic/integer value in path condition");
    if (!sym_e->name.empty()) {
      ASSERT(sym_e->bw > 1, "i1 symv");
      auto ret = c->bv_const(sym_e->name.c_str(), sym_e->bw);
      vars.emplace(sym_e, ret);
      return ret;
    }
    int bw = sym_e->bw;
    std::vector<expr> expr_rands;
    for (auto& e : sym_e->rands) {
      auto [e2, vm] = construct_expr(e);
      expr_rands.push_back(e2);
      vars.insert(vm->begin(), vm->end());
    }
    switch (sym_e->rator) {
      case iOP::op_add:
        return expr_rands.at(0) + expr_rands.at(1);
      case iOP::op_sub:
        return expr_rands.at(0) - expr_rands.at(1);
      case iOP::op_mul:
        return expr_rands.at(0) * expr_rands.at(1);
      case iOP::op_sdiv:
      case iOP::op_udiv:
        return expr_rands.at(0) / expr_rands.at(1);
      case iOP::op_uge:
        return uge(expr_rands.at(0), expr_rands.at(1));
      case iOP::op_sge:
        return sge(expr_rands.at(0), expr_rands.at(1));
      case iOP::op_ugt:
        return ugt(expr_rands.at(0), expr_rands.at(1));
      case iOP::op_sgt:
        return sgt(expr_rands.at(0), expr_rands.at(1));
      case iOP::op_ule:
        return ule(expr_rands.at(0), expr_rands.at(1));
      case iOP::op_sle:
        return sle(expr_rands.at(0), expr_rands.at(1));
      case iOP::op_ult:
        return ult(expr_rands.at(0), expr_rands.at(1));
      case iOP::op_slt:
        return slt(expr_rands.at(0), expr_rands.at(1));
      case iOP::op_eq:
        return expr_rands.at(0) == expr_rands.at(1);
      case iOP::op_neq:
        return expr_rands.at(0) != expr_rands.at(1);
      case iOP::op_neg: {
        ASSERT(expr_rands.at(0).get_sort().is_bool(), "negate a bitvector");
        return !expr_rands.at(0);
      }
      case iOP::op_sext: {
        auto v = expr_rands.at(0);
        if (v.get_sort().is_bool())
          v = ite(v, c->bv_val(1, 1), c->bv_val(0, 1));
        auto ext_size = bw - v.get_sort().bv_size();
        ASSERT(ext_size >= 0, "negative sign extension size");
        if (ext_size > 0) return sext(v, ext_size);
        return v;
      }
      case iOP::op_zext: {
        auto v = expr_rands.at(0);
        if (v.get_sort().is_bool())
          v = ite(v, c->bv_val(1, 1), c->bv_val(0, 1));
        auto ext_size = bw - v.get_sort().bv_size();
        ASSERT(ext_size >= 0, "negative zero extension size");
        if (ext_size > 0) return zext(v, ext_size);
        return v;
      }
      case iOP::op_shl:
        return shl(expr_rands.at(0), expr_rands.at(1));
      case iOP::op_lshr:
        return lshr(expr_rands.at(0), expr_rands.at(1));
      case iOP::op_ashr:
        return ashr(expr_rands.at(0), expr_rands.at(1));
      case iOP::op_and:
        return expr_rands.at(0) & expr_rands.at(1);
      case iOP::op_or:
        return expr_rands.at(0) | expr_rands.at(1);
      case iOP::op_xor:
        return expr_rands.at(0) ^ expr_rands.at(1);
      case iOP::op_urem:
        return urem(expr_rands.at(0), expr_rands.at(1));
      case iOP::op_srem:
        return srem(expr_rands.at(0), expr_rands.at(1));
      case iOP::op_trunc: {
        // XXX is it right?
        auto v = expr_rands.at(0).extract(bw-1, 0);
        if (1 == bw) {
          v = (c->bv_val(1, 1) == v);
        }
        return v;
      }
      case iOP::op_concat:
        return concat(expr_rands.at(0), expr_rands.at(1));
      case iOP::op_extract:
        return expr_rands.at(0).extract(
                                expr_rands.at(1).get_numeral_uint(),
                                expr_rands.at(2).get_numeral_uint());
      case iOP::op_ite: {
        auto cond = expr_rands.at(0);
        auto v_t = expr_rands.at(1);
        auto v_e = expr_rands.at(2);
        ASSERT(cond.get_sort().is_bool(), "Non Boolean condition");
        ASSERT(v_t.get_sort().is_bv() && v_e.get_sort().is_bv(), "Operation between different type");
        ASSERT(v_t.get_sort().bv_size() == v_e.get_sort().bv_size(),"Operation between different bv_length");
        return ite(cond, v_t, v_e);
      }
      default: break;
    }
    ABORT("unkown operator when constructing STP expr");
    return c->bv_const("x", 32);
  }
  void push_internal() {
    // XXX: z3's pop/push operation is quite expensive!
    g_solver->push();
  }
  void pop_internal() {
    g_solver->pop();
  }
};

#endif
