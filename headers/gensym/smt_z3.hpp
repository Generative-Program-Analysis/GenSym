#ifndef GS_Z3_HEADER
#define GS_Z3_HEADER

#include "z3.h"

//using namespace z3;

class CheckerZ3 : public CachedChecker<CheckerZ3, Z3_ast, Z3_model> {
private:
  Z3_context ctx;
  Z3_params solverParameters;
  //solver* g_solver;
public:
  CheckerZ3() {
    std::cout << "Use Z3 " << Z3_get_full_version() << "\n";
    Z3_config cfg = Z3_mk_config();
    ctx = Z3_mk_context_rc(cfg);
    Z3_del_config(cfg);
    solverParameters = Z3_mk_params(ctx);
    Z3_params_inc_ref(ctx, solverParameters);
  }
  virtual ~CheckerZ3() override {
    clear_cache();
    //delete g_solver;
  }
  //void add_constraint_internal(expr e) {
  //  auto temp_start = steady_clock::now();
  //  g_solver->add(e);
  //  auto temp_end = steady_clock::now();
  //  add_cons_time += duration_cast<microseconds>(temp_end - temp_start).count();
  //}
  //inline IntData eval(expr val) {
  //  auto const_val = g_solver->get_model().eval(val, true);
  //  return const_val.get_numeral_uint64();
  //}

  inline solver_result to_solver_result(Z3_lbool l) {
      if (l == Z3_L_TRUE) return solver_result::sat;
      else if (l == Z3_L_FALSE) return solver_result::unsat;
      return solver_result::unknown;
  }

  solver_response check_model_internal(BrCacheKey& conds) {
    Z3_solver theSolver = Z3_mk_solver(ctx);
    Z3_solver_inc_ref(ctx, theSolver);
    Z3_solver_set_params(ctx, theSolver, solverParameters);

    auto temp_start = steady_clock::now();
    for (auto& v: conds) {
      Z3_solver_assert(ctx, theSolver, to_expr(v));
    }
    auto temp_end = steady_clock::now();
    add_cons_time += duration_cast<microseconds>(temp_end - temp_start).count();

    Z3_lbool z3_res = Z3_solver_check(ctx, theSolver);
    auto result = to_solver_result(z3_res);

    std::shared_ptr<Z3_model> cex_model = nullptr;
    if (solver_result::sat == result) {
      cex_model = std::make_shared<Z3_model>(Z3_solver_get_model(ctx, theSolver));
    }

    Z3_solver_dec_ref(ctx, theSolver);
    return std::make_pair((solver_result) result, cex_model);
  }

  //inline std::shared_ptr<model> get_model_internal(BrCacheKey& conds) {
  //  return std::make_shared<model>(g_solver->get_model());
  //}

  inline IntData eval_model(std::shared_ptr<Z3_model> m, PtrVal val) {
    Z3_ast conc_val = 0;
    bool status = Z3_model_eval(ctx, *m, construct_expr(val), true, &conc_val);
    uint64_t i;
    bool r = Z3_get_numeral_uint64(ctx, conc_val, &i);
    return i;
  }

  Z3_ast construct_expr_internal(PtrVal e) {
    auto int_e = std::dynamic_pointer_cast<IntV>(e);
    if (int_e) {
      // XXX(GW): using this vs sym_bool_const?
      if (int_e->bw == 1) {
        bool bool_e = int_e->i ? true : false;
        return bool_e ? Z3_mk_true(ctx) : Z3_mk_false(ctx);
      }
      return Z3_mk_int64(ctx, int_e->as_signed(), Z3_mk_bv_sort(ctx, int_e->bw));
    }

    auto sym_e = std::dynamic_pointer_cast<SymV>(e);
    if (!sym_e) ABORT("Non-symbolic/integer value in path condition");
    if (sym_e->is_var()) {
      ASSERT(sym_e->bw > 1, "Named symbolic constant of size 1");
      return Z3_mk_const(ctx, Z3_mk_string_symbol(ctx, sym_e->name.c_str()), Z3_mk_bv_sort(ctx, sym_e->bw));
    }
    std::vector<Z3_ast> expr_rands;
    for (auto& rand : sym_e->rands) {
      expr_rands.push_back(construct_expr(rand));
    }
    int bw = sym_e->bw;
    switch (sym_e->rator) {
      case iOP::op_add:
        return Z3_mk_bvadd(ctx, expr_rands[0], expr_rands[1]);
      case iOP::op_sub:
        return Z3_mk_bvsub(ctx, expr_rands[0], expr_rands[1]);
      case iOP::op_mul:
        return Z3_mk_bvmul(ctx, expr_rands[0], expr_rands[1]);
      // need to change??
      case iOP::op_sdiv:
        return Z3_mk_bvsdiv(ctx, expr_rands[0], expr_rands[1]);
      // need to change??
      case iOP::op_udiv:
        return Z3_mk_bvudiv(ctx, expr_rands[0], expr_rands[1]);
      case iOP::op_uge:
        return Z3_mk_bvuge(ctx, expr_rands[0], expr_rands[1]);
      case iOP::op_sge:
        return Z3_mk_bvsge(ctx, expr_rands[0], expr_rands[1]);
      case iOP::op_ugt:
        return Z3_mk_bvugt(ctx, expr_rands[0], expr_rands[1]);
      case iOP::op_sgt:
        return Z3_mk_bvsgt(ctx, expr_rands[0], expr_rands[1]);
      case iOP::op_ule:
        return Z3_mk_bvule(ctx, expr_rands[0], expr_rands[1]);
      case iOP::op_sle:
        return Z3_mk_bvsle(ctx, expr_rands[0], expr_rands[1]);
      case iOP::op_ult:
        return Z3_mk_bvult(ctx, expr_rands[0], expr_rands[1]);
      case iOP::op_slt:
        return Z3_mk_bvslt(ctx, expr_rands[0], expr_rands[1]);
      case iOP::op_eq: {
        std::cout << "eq left:" << Z3_ast_to_string(ctx, expr_rands[0]) << " right:" << Z3_ast_to_string(ctx, expr_rands[1]) << "\n";
        return Z3_mk_eq(ctx, expr_rands[0], expr_rands[1]);
      }
      case iOP::op_neq: {
        Z3_ast args[2] = { expr_rands[0], expr_rands[1] };
        return Z3_mk_distinct(ctx, 2, args);
      }
      case iOP::op_neg: {
        ASSERT(Z3_get_sort_kind(ctx, Z3_get_sort(ctx, expr_rands[0])) == Z3_BOOL_SORT, "negating a non-boolean");
        return Z3_mk_not(ctx, expr_rands[0]);
      }
      case iOP::op_bvnot: {
        ASSERT(Z3_get_sort_kind(ctx, Z3_get_sort(ctx, expr_rands[0])) == Z3_BV_SORT, "negating a non-bitvector");
        return Z3_mk_bvnot(ctx, expr_rands[0]);
      }
      case iOP::op_sext: {
        auto v = expr_rands[0];
        if (Z3_get_sort_kind(ctx, Z3_get_sort(ctx, v)) == Z3_BOOL_SORT)
          v = Z3_mk_ite(ctx, v, Z3_mk_int64(ctx, 1, Z3_mk_bv_sort(ctx, 1)), Z3_mk_int64(ctx, 0, Z3_mk_bv_sort(ctx, 1)));
        auto ext_size = bw - Z3_get_bv_sort_size(ctx, Z3_get_sort(ctx, v));
        ASSERT(ext_size >= 0, "negative sign extension size");
        if (ext_size > 0) return Z3_mk_sign_ext(ctx, ext_size, v);
        return v;
      }
      case iOP::op_zext: {
        auto v = expr_rands[0];
        if (Z3_get_sort_kind(ctx, Z3_get_sort(ctx, v)) == Z3_BOOL_SORT)
          v = Z3_mk_ite(ctx, v, Z3_mk_int64(ctx, 1, Z3_mk_bv_sort(ctx, 1)), Z3_mk_int64(ctx, 0, Z3_mk_bv_sort(ctx, 1)));
        auto ext_size = bw - Z3_get_bv_sort_size(ctx, Z3_get_sort(ctx, v));
        ASSERT(ext_size >= 0, "negative zero extension size");
        if (ext_size > 0) return Z3_mk_zero_ext(ctx, ext_size, v);
        return v;
      }
      case iOP::op_shl:
        return Z3_mk_bvshl(ctx, expr_rands[0], expr_rands[1]);
      case iOP::op_lshr:
        return Z3_mk_bvlshr(ctx, expr_rands[0], expr_rands[1]);
      case iOP::op_ashr:
        return Z3_mk_bvashr(ctx, expr_rands[0], expr_rands[1]);
      case iOP::op_and: {
        if (Z3_get_sort_kind(ctx, Z3_get_sort(ctx, expr_rands[0])) == Z3_BOOL_SORT) {
          Z3_ast args[2] = { expr_rands[0], expr_rands[1] };
          return Z3_mk_and(ctx, 2, args);
        } else {
          return Z3_mk_bvand(ctx, expr_rands[0], expr_rands[1]);
        }
      }
      case iOP::op_or:  {
        if (Z3_get_sort_kind(ctx, Z3_get_sort(ctx, expr_rands[0])) == Z3_BOOL_SORT) {
          Z3_ast args[2] = { expr_rands[0], expr_rands[1] };
          return Z3_mk_or(ctx, 2, args);
        } else {
          return Z3_mk_bvor(ctx, expr_rands[0], expr_rands[1]);
        }
      }
      case iOP::op_xor:  {
        if (Z3_get_sort_kind(ctx, Z3_get_sort(ctx, expr_rands[0])) == Z3_BOOL_SORT) {
          return Z3_mk_xor(ctx, expr_rands[0], expr_rands[1]);
        } else {
          return Z3_mk_bvxor(ctx, expr_rands[0], expr_rands[1]);
        }
      }
      case iOP::op_urem:
        return Z3_mk_bvurem(ctx, expr_rands[0], expr_rands[1]);
      case iOP::op_srem:
        return Z3_mk_bvsrem(ctx, expr_rands[0], expr_rands[1]);
      case iOP::op_trunc: {
        // XXX is it right?
        auto v = Z3_mk_extract(ctx, bw-1, 0, expr_rands[0]);
        if (1 == bw) {
          v = Z3_mk_eq(ctx, Z3_mk_int64(ctx, 1, Z3_mk_bv_sort(ctx, 1)), v);
        }
        return v;
      }
      // is this right? there are 3 concat functions
      case iOP::op_concat:
        return Z3_mk_concat(ctx, expr_rands[0], expr_rands[1]);
      case iOP::op_extract: {
        unsigned hi;
        bool r = Z3_get_numeral_uint(ctx, expr_rands[1], &hi);
        unsigned lo;
        r = Z3_get_numeral_uint(ctx, expr_rands[2], &lo);
        return Z3_mk_extract(ctx, hi, lo, expr_rands[0]);
      }
      case iOP::op_ite: {
        auto cond = expr_rands[0];
        auto v_t = expr_rands[1];
        auto v_e = expr_rands[2];
        ASSERT(Z3_get_sort_kind(ctx, Z3_get_sort(ctx, cond)) == Z3_BOOL_SORT, "Non Boolean condition");
        // Todo: add assertion
        //ASSERT(((v_t.get_sort().is_bv() && v_e.get_sort().is_bv()) || (v_t.get_sort().is_bool() && v_e.get_sort().is_bool())), "Operation between different type");
        return Z3_mk_ite(ctx, cond, v_t, v_e);
      }
      case iOP::const_true:
        return Z3_mk_true(ctx);
      case iOP::const_false:
        return Z3_mk_false(ctx);
      default: break;
    }
    ABORT("unkown operator when constructing STP expr");
    return Z3_mk_const(ctx, Z3_mk_string_symbol(ctx, "x"), Z3_mk_bv_sort(ctx, 32));
  }
  void push_internal() {
    // XXX: z3's pop/push operation is quite expensive!
    //g_solver->push();
  }
  void pop_internal() {
    //g_solver->pop();
  }
  void reset_internal() {
    //g_solver->reset();
  }
};

#endif
