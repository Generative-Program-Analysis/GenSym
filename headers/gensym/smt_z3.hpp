#ifndef GS_Z3_HEADER
#define GS_Z3_HEADER

#include "z3.h"

//using namespace z3;

template <typename T> class Z3NodeHandle {
  // Internally these Z3 types are pointers
  // so storing these should be cheap.
  // It would be nice if we could infer the Z3_context from the node
  // but I can't see a way to do this from Z3's API.
protected:
  T node;
  ::Z3_context context;

private:
  // To be specialised
  inline ::Z3_ast as_ast();

public:
  Z3NodeHandle() : node(NULL), context(NULL) {}
  Z3NodeHandle(const T _node, const ::Z3_context _context)
      : node(_node), context(_context) {
    if (node && context) {
      ::Z3_inc_ref(context, as_ast());
    }
  };
  ~Z3NodeHandle() {
    if (node && context) {
      ::Z3_dec_ref(context, as_ast());
    }
  }
  Z3NodeHandle(const Z3NodeHandle &b) : node(b.node), context(b.context) {
    if (node && context) {
      ::Z3_inc_ref(context, as_ast());
    }
  }
  Z3NodeHandle &operator=(const Z3NodeHandle &b) {
    if (node == NULL && context == NULL) {
      // Special case for when this object was constructed
      // using the default constructor. Try to inherit a non null
      // context.
      context = b.context;
    }
    assert(context == b.context && "Mismatched Z3 contexts!");
    // node != nullptr ==> context != NULL
    assert((node == NULL || context) &&
           "Can't have non nullptr node with nullptr context");

    if (node && context) {
      ::Z3_dec_ref(context, as_ast());
    }
    node = b.node;
    if (node && context) {
      ::Z3_inc_ref(context, as_ast());
    }
    return *this;
  }
  // To be specialised
  inline void dump();

  operator T() const { return node; }
};

// Specialise for Z3_sort
typedef Z3NodeHandle<Z3_sort> Z3SortHandle;
template <> inline ::Z3_ast Z3NodeHandle<Z3_sort>::as_ast() {
  // In Z3 internally this call is just a cast. We could just do that
  // instead to simplify our implementation but this seems cleaner.
  return ::Z3_sort_to_ast(context, node);
}

template <> inline void Z3NodeHandle<Z3_sort>::dump() {
  std::cout << "Z3SortHandle:\n" << ::Z3_sort_to_string(context, node)
            << "\n";
}

// Specialise for Z3_ast
typedef Z3NodeHandle<Z3_ast> Z3ASTHandle;
template <> inline ::Z3_ast Z3NodeHandle<Z3_ast>::as_ast() { return node; }
template <> inline void Z3NodeHandle<Z3_ast>::dump() {
  std::cout << "Z3ASTHandle:\n" << ::Z3_ast_to_string(context, as_ast())
            << "\n";
}

class CheckerZ3 : public CachedChecker<CheckerZ3, Z3ASTHandle, Z3_model> {
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
      Z3_model_inc_ref(ctx, *cex_model);
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
    Z3_inc_ref(ctx, conc_val);
    uint64_t i;
    bool r = Z3_get_numeral_uint64(ctx, conc_val, &i);
    Z3_dec_ref(ctx, conc_val);
    return i;
  }

  Z3SortHandle getBvSort(unsigned width) {
    return Z3SortHandle(Z3_mk_bv_sort(ctx, width), ctx);
  }

  Z3ASTHandle getBvVal(int64_t val, unsigned sz) {
    return Z3ASTHandle(Z3_mk_int64(ctx, val, getBvSort(sz)), ctx);
  }

  unsigned getBVLength(Z3ASTHandle expr) {
    return Z3_get_bv_sort_size(ctx, Z3SortHandle(Z3_get_sort(ctx, expr), ctx));
  }

  Z3ASTHandle construct_expr_internal(PtrVal e) {
    auto int_e = std::dynamic_pointer_cast<IntV>(e);
    if (int_e) {
      // XXX(GW): using this vs sym_bool_const?
      if (int_e->bw == 1) {
        bool bool_e = int_e->i ? true : false;
        return bool_e ? Z3ASTHandle(Z3_mk_true(ctx), ctx) : Z3ASTHandle(Z3_mk_false(ctx), ctx);
      }
      return  Z3ASTHandle(Z3_mk_int64(ctx, int_e->as_signed(), getBvSort(int_e->bw)), ctx);
    }

    auto sym_e = std::dynamic_pointer_cast<SymV>(e);
    if (!sym_e) ABORT("Non-symbolic/integer value in path condition");
    if (sym_e->is_var()) {
      ASSERT(sym_e->bw > 1, "Named symbolic constant of size 1");
      return Z3ASTHandle(Z3_mk_const(ctx, Z3_mk_string_symbol(ctx, sym_e->name.c_str()), getBvSort(sym_e->bw)), ctx);
    }
    std::vector<Z3ASTHandle> expr_rands;
    for (auto& rand : sym_e->rands) {
      expr_rands.push_back(construct_expr(rand));
    }
    int bw = sym_e->bw;
    switch (sym_e->rator) {
      case iOP::op_add:
        return Z3ASTHandle(Z3_mk_bvadd(ctx, expr_rands[0], expr_rands[1]), ctx);
      case iOP::op_sub:
        return Z3ASTHandle(Z3_mk_bvsub(ctx, expr_rands[0], expr_rands[1]), ctx);
      case iOP::op_mul:
        return Z3ASTHandle(Z3_mk_bvmul(ctx, expr_rands[0], expr_rands[1]), ctx);
      // need to change??
      case iOP::op_sdiv:
        return Z3ASTHandle(Z3_mk_bvsdiv(ctx, expr_rands[0], expr_rands[1]), ctx);
      // need to change??
      case iOP::op_udiv:
        return Z3ASTHandle(Z3_mk_bvudiv(ctx, expr_rands[0], expr_rands[1]), ctx);
      case iOP::op_uge:
        return Z3ASTHandle(Z3_mk_bvuge(ctx, expr_rands[0], expr_rands[1]), ctx);
      case iOP::op_sge:
        return Z3ASTHandle(Z3_mk_bvsge(ctx, expr_rands[0], expr_rands[1]), ctx);
      case iOP::op_ugt:
        return Z3ASTHandle(Z3_mk_bvugt(ctx, expr_rands[0], expr_rands[1]), ctx);
      case iOP::op_sgt:
        return Z3ASTHandle(Z3_mk_bvsgt(ctx, expr_rands[0], expr_rands[1]), ctx);
      case iOP::op_ule:
        return Z3ASTHandle(Z3_mk_bvule(ctx, expr_rands[0], expr_rands[1]), ctx);
      case iOP::op_sle:
        return Z3ASTHandle(Z3_mk_bvsle(ctx, expr_rands[0], expr_rands[1]), ctx);
      case iOP::op_ult:
        return Z3ASTHandle(Z3_mk_bvult(ctx, expr_rands[0], expr_rands[1]), ctx);
      case iOP::op_slt:
        return Z3ASTHandle(Z3_mk_bvslt(ctx, expr_rands[0], expr_rands[1]), ctx);
      case iOP::op_eq: {
        //std::cout << "eq left:" << Z3_ast_to_string(ctx, expr_rands[0]) << " right:" << Z3_ast_to_string(ctx, expr_rands[1]) << "\n";
        return Z3ASTHandle(Z3_mk_eq(ctx, expr_rands[0], expr_rands[1]), ctx);
      }
      case iOP::op_neq: {
        Z3_ast args[2] = { expr_rands[0], expr_rands[1] };
        return Z3ASTHandle(Z3_mk_distinct(ctx, 2, args), ctx);
      }
      case iOP::op_neg: {
        Z3SortHandle rand0_sort = Z3SortHandle(Z3_get_sort(ctx, expr_rands[0]), ctx);
        ASSERT(Z3_get_sort_kind(ctx, rand0_sort) == Z3_BOOL_SORT, "negating a non-boolean");
        return Z3ASTHandle(Z3_mk_not(ctx, expr_rands[0]), ctx);
      }
      case iOP::op_bvnot: {
        Z3SortHandle rand0_sort = Z3SortHandle(Z3_get_sort(ctx, expr_rands[0]), ctx);
        ASSERT(Z3_get_sort_kind(ctx, rand0_sort) == Z3_BV_SORT, "negating a non-bitvector");
        return Z3ASTHandle(Z3_mk_bvnot(ctx, expr_rands[0]), ctx);
      }
      case iOP::op_sext: {
        auto v = expr_rands[0];
        Z3SortHandle v_sort = Z3SortHandle(Z3_get_sort(ctx, v), ctx);
        if (Z3_get_sort_kind(ctx,  v_sort) == Z3_BOOL_SORT)
          v = Z3ASTHandle(Z3_mk_ite(ctx, v, Z3ASTHandle(Z3_mk_int64(ctx, 1, getBvSort(1)), ctx), Z3ASTHandle(Z3_mk_int64(ctx, 0, getBvSort(1)), ctx)), ctx);
        auto ext_size = bw - getBVLength(v);
        ASSERT(ext_size >= 0, "negative sign extension size");
        if (ext_size > 0) return Z3ASTHandle(Z3_mk_sign_ext(ctx, ext_size, v), ctx);
        return v;
      }
      case iOP::op_zext: {
        auto v = expr_rands[0];
        Z3SortHandle v_sort = Z3SortHandle(Z3_get_sort(ctx, v), ctx);
        if (Z3_get_sort_kind(ctx,  v_sort) == Z3_BOOL_SORT)
          v = Z3ASTHandle(Z3_mk_ite(ctx, v, Z3ASTHandle(Z3_mk_int64(ctx, 1, getBvSort(1)), ctx), Z3ASTHandle(Z3_mk_int64(ctx, 0, getBvSort(1)), ctx)), ctx);
        auto ext_size = bw - getBVLength(v);
        ASSERT(ext_size >= 0, "negative zero extension size");
        if (ext_size > 0) return Z3ASTHandle(Z3_mk_zero_ext(ctx, ext_size, v), ctx);
        return v;
      }
      case iOP::op_shl:
        return Z3ASTHandle(Z3_mk_bvshl(ctx, expr_rands[0], expr_rands[1]), ctx);
      case iOP::op_lshr:
        return Z3ASTHandle(Z3_mk_bvlshr(ctx, expr_rands[0], expr_rands[1]), ctx);
      case iOP::op_ashr:
        return Z3ASTHandle(Z3_mk_bvashr(ctx, expr_rands[0], expr_rands[1]), ctx);
      case iOP::op_and: {
        Z3SortHandle op_sort = Z3SortHandle(Z3_get_sort(ctx, expr_rands[0]), ctx);
        if (Z3_get_sort_kind(ctx, op_sort) == Z3_BOOL_SORT) {
          Z3_ast args[2] = { expr_rands[0], expr_rands[1] };
          return Z3ASTHandle(Z3_mk_and(ctx, 2, args), ctx);
        } else {
          return Z3ASTHandle(Z3_mk_bvand(ctx, expr_rands[0], expr_rands[1]), ctx);
        }
      }
      case iOP::op_or:  {
        Z3SortHandle op_sort = Z3SortHandle(Z3_get_sort(ctx, expr_rands[0]), ctx);
        if (Z3_get_sort_kind(ctx, op_sort) == Z3_BOOL_SORT) {
          Z3_ast args[2] = { expr_rands[0], expr_rands[1] };
          return Z3ASTHandle(Z3_mk_or(ctx, 2, args), ctx);
        } else {
          return Z3ASTHandle(Z3_mk_bvor(ctx, expr_rands[0], expr_rands[1]), ctx);
        }
      }
      case iOP::op_xor:  {
        Z3SortHandle op_sort = Z3SortHandle(Z3_get_sort(ctx, expr_rands[0]), ctx);
        if (Z3_get_sort_kind(ctx, op_sort) == Z3_BOOL_SORT) {
          return Z3ASTHandle(Z3_mk_xor(ctx, expr_rands[0], expr_rands[1]), ctx);
        } else {
          return Z3ASTHandle(Z3_mk_bvxor(ctx, expr_rands[0], expr_rands[1]), ctx);
        }
      }
      case iOP::op_urem:
        return Z3ASTHandle(Z3_mk_bvurem(ctx, expr_rands[0], expr_rands[1]), ctx);
      case iOP::op_srem:
        return Z3ASTHandle(Z3_mk_bvsrem(ctx, expr_rands[0], expr_rands[1]), ctx);
      case iOP::op_trunc: {
        // XXX is it right?
        auto v = Z3ASTHandle(Z3_mk_extract(ctx, bw-1, 0, expr_rands[0]), ctx);
        if (1 == bw) {
          v = Z3ASTHandle(Z3_mk_eq(ctx, Z3ASTHandle(Z3_mk_int64(ctx, 1, getBvSort(1)), ctx), v), ctx);
        }
        return v;
      }
      // is this right? there are 3 concat functions
      case iOP::op_concat:
        return Z3ASTHandle(Z3_mk_concat(ctx, expr_rands[0], expr_rands[1]), ctx);
      case iOP::op_extract: {
        unsigned hi;
        bool r = Z3_get_numeral_uint(ctx, expr_rands[1], &hi);
        unsigned lo;
        r = Z3_get_numeral_uint(ctx, expr_rands[2], &lo);
        return Z3ASTHandle(Z3_mk_extract(ctx, hi, lo, expr_rands[0]), ctx);
      }
      case iOP::op_ite: {
        auto cond = expr_rands[0];
        auto v_t = expr_rands[1];
        auto v_e = expr_rands[2];
        Z3SortHandle cond_sort = Z3SortHandle(Z3_get_sort(ctx, cond), ctx);
        ASSERT(Z3_get_sort_kind(ctx, cond_sort) == Z3_BOOL_SORT, "Non Boolean condition");
        // Todo: add assertion
        //ASSERT(((v_t.get_sort().is_bv() && v_e.get_sort().is_bv()) || (v_t.get_sort().is_bool() && v_e.get_sort().is_bool())), "Operation between different type");
        return Z3ASTHandle(Z3_mk_ite(ctx, cond, v_t, v_e), ctx);
      }
      case iOP::const_true:
        return Z3ASTHandle(Z3_mk_true(ctx), ctx);
      case iOP::const_false:
        return Z3ASTHandle(Z3_mk_false(ctx), ctx);
      default: break;
    }
    ABORT("unkown operator when constructing STP expr");
    return Z3ASTHandle(Z3_mk_const(ctx, Z3_mk_string_symbol(ctx, "x"), getBvSort(32)), ctx);
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
