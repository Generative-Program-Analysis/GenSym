#ifndef LLSC_Z3_HEADERS
#define LLSC_Z3_HEADERS

#include "z3++.h"

using namespace z3;

class CheckerZ3 : public Checker {
private:
  using instance = std::tuple<context*, solver*>;
  std::map<std::thread::id, instance> checker_map;
  instance new_instance() {
    context* c = new context;
    solver* s = new solver(*c);
    return std::make_tuple(c, s);
  }
  context* g_ctx;
  solver* g_solver;
public:
  CheckerZ3() {
    std::cout << "Use Z3 " << Z3_get_full_version() << "\n";
  }
  ~CheckerZ3() { destroy_solvers(); }
  void init_solvers() override {
    //std::tie(g_ctx, g_solver) = new_instance();
    checker_map[std::this_thread::get_id()] = new_instance();
    tp.with_thread_ids([this](auto id) {
      checker_map[id] = new_instance();
    });
  }
  void destroy_solvers() override {
    for (auto& [t, cs] : checker_map) {
      //delete std::get<0>(cs);
      delete std::get<1>(cs);
    }
  }
  instance get_my_thread_local_instance() {
    return checker_map[std::this_thread::get_id()];
  }
  solver_result make_query(PC pc) override {
    auto pc_set = pc.get_path_conds();
    auto start = steady_clock::now();
    context* c; solver* s;
    std::tie(c, s) = get_my_thread_local_instance();
    for (auto& e: pc_set)
      s->add(construct_z3_expr(c, e));
    //std::cout << *s << "\n";
    auto result = s->check();
    auto end = steady_clock::now();
    solver_time += duration_cast<microseconds>(end - start);
    return (solver_result) result;
  }
  std::pair<bool, UIntData> concretize(PC pc, PtrVal v) override {
    context* c; solver* s;
    std::tie(c, s) = get_my_thread_local_instance();
    auto val = construct_z3_expr(c, v);
    auto result = make_query(pc);

    UIntData ret_val = 0;

    if (solver_result::sat == result) {
      auto const_val = s->get_model().eval(val, true);
      ret_val = const_val.get_numeral_uint64();
    }

    return std::make_pair(solver_result::sat == result, ret_val);
  }
  expr construct_z3_expr(context* c, PtrVal e) {
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
      return c->bv_const(sym_e->name.c_str(), sym_e->bw);
    }
    int bw = sym_e->bw;
    std::vector<expr> expr_rands;
    for (auto& e : sym_e->rands) {
      expr_rands.push_back(construct_z3_expr(c, e));
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
      default: break;
    }
    ABORT("unkown operator when constructing STP expr");
    return c->bv_const("x", 32);
  }
  void print_model(std::stringstream& output) override {
    context* c; solver* s;
    std::tie(c, s) = get_my_thread_local_instance();
    model m = s->get_model();
    output << m << std::endl;
  }
  void push() override {
    context* c; solver* s;
    std::tie(c, s) = get_my_thread_local_instance();
    s->push();
  }
  void pop() override {
    context* c; solver* s;
    std::tie(c, s) = get_my_thread_local_instance();
    s->pop();
  }
  void reset() override {
    context* c; solver* s;
    std::tie(c, s) = get_my_thread_local_instance();
    s->reset();
  }
};

#endif
