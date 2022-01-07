#include"z3++.h"

using namespace z3;

// TODO: define a checker interface
// TODO: abstract over smt_z3 and smt_stp

using instance = std::tuple<context*, solver*>;

class CheckerZ3 {
private:
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
    std::cout << "Using Z3 solver\n";
  }
  ~CheckerZ3() { destroy_solvers(); }
  void init_solvers() {
    //std::tie(g_ctx, g_solver) = new_instance();
    checker_map[std::this_thread::get_id()] = new_instance();
    tp.with_thread_ids([this](auto id) {
      checker_map[id] = new_instance();
    });
  }
  void destroy_solvers() {
    for (auto& [t, cs] : checker_map) {
      //delete std::get<0>(cs);
      delete std::get<1>(cs);
    }
  }
  instance get_my_thread_local_instance() {
    return checker_map[std::this_thread::get_id()];
  }
  check_result make_query(PC pc) {
    auto pc_set = pc.getPC();
    auto start = steady_clock::now();
    context* c; solver* s;
    std::tie(c, s) = get_my_thread_local_instance();
    for (auto& e: pc_set)
      s->add(construct_z3_expr(c, e));
    auto result = s->check();
    auto end = steady_clock::now();
    solver_time += duration_cast<microseconds>(end - start);
    return result;
  }
  expr construct_z3_expr(context* c, PtrVal e) {
    auto int_e = std::dynamic_pointer_cast<IntV>(e);
    if (int_e) {
      return c->bv_val((int64_t)int_e->i, int_e->bw);
    }
    auto sym_e = std::dynamic_pointer_cast<SymV>(e);
    if (!sym_e) ABORT("Non-symbolic/integer value in path condition");
    if (!sym_e->name.empty()) {
      return c->bv_const(sym_e->name.c_str(), sym_e->bw);
    }
    int bw = sym_e->bw;
    std::vector<expr> expr_rands;
    for (auto& e : sym_e->rands) {
      expr_rands.push_back(construct_z3_expr(c, e));
    }
    switch (sym_e->rator) {
      case op_add:
        return expr_rands.at(0) + expr_rands.at(1);
      case op_sub:
        return expr_rands.at(0) - expr_rands.at(1);
      case op_mul:
        return expr_rands.at(0) * expr_rands.at(1);
      case op_sdiv:
      case op_udiv:
        return expr_rands.at(0) / expr_rands.at(1);
      case op_uge:
        return uge(expr_rands.at(0), expr_rands.at(1));
      case op_sge:
        return sge(expr_rands.at(0), expr_rands.at(1));
      case op_ugt:
        return ugt(expr_rands.at(0), expr_rands.at(1));
      case op_sgt:
        return sgt(expr_rands.at(0), expr_rands.at(1));
      case op_ule:
        return ule(expr_rands.at(0), expr_rands.at(1));
      case op_sle:
        return sle(expr_rands.at(0), expr_rands.at(1));
      case op_ult:
        return ult(expr_rands.at(0), expr_rands.at(1));
      case op_slt:
        return slt(expr_rands.at(0), expr_rands.at(1));
      case op_eq:
        return expr_rands.at(0) == expr_rands.at(1);
      case op_neq:
        return expr_rands.at(0) != expr_rands.at(1);
      case op_neg:
        return !expr_rands.at(0);
      case op_sext:
        return sext(expr_rands.at(0), bw);
      case op_zext:
        return zext(expr_rands.at(0), bw);
      case op_shl:
        return shl(expr_rands.at(0), expr_rands.at(1));
      case op_lshr:
        return lshr(expr_rands.at(0), expr_rands.at(1));
      case op_ashr:
        return ashr(expr_rands.at(0), expr_rands.at(1));
      case op_and:
        return expr_rands.at(0) & expr_rands.at(1);
      case op_or:
        return expr_rands.at(0) | expr_rands.at(1);
      case op_xor:
        return expr_rands.at(0) ^ expr_rands.at(1);
      case op_urem:
        return urem(expr_rands.at(0), expr_rands.at(1));
      case op_srem:
        return srem(expr_rands.at(0), expr_rands.at(1));
      case op_trunc:
        // XXX is it right?
        return expr_rands.at(0).extract(bw-1, 0);
      default: break;
    }
    ABORT("unkown operator when constructing STP expr");
    return c->bv_const("x", 32);
  }
  void push() {
    context* c; solver* s;
    std::tie(c, s) = get_my_thread_local_instance();
    s->push();
  }
  void pop() {
    context* c; solver* s;
    std::tie(c, s) = get_my_thread_local_instance();
    s->pop();
  }
  void reset() {
    context* c; solver* s;
    std::tie(c, s) = get_my_thread_local_instance();
    s->reset();
  }
};

inline CheckerZ3 cz3;

inline bool check_pc(PC pc) {
  if (!use_solver) return true;
  br_query_num++;
  cz3.push();
  auto r = cz3.make_query(pc);
  cz3.pop();
  return r == sat;
}

inline std::string check_result_to_string(check_result res) {
  switch (res) {
    case sat: return "sat";
    case unsat: return "unsat";
    case unknown: return "unknown";
    default: ABORT("wow");
  }
}

inline void check_pc_to_file(SS state) {
  if (!use_solver) return;

  if (mkdir("tests", 0777) == -1) {
    if (errno == EEXIST) { }
    else {
      ABORT("Cannot create the folder tests, abort.\n");
    }
  }

  std::stringstream output;
  output << "Query number: " << (test_query_num+1) << std::endl;
  cz3.push();
  // XXX: reset harms performance a lot
  auto result = cz3.make_query(state.getPC());
  output << "Query is " << check_result_to_string(result) << std::endl;

  if (result == sat) {
    test_query_num++;
    std::stringstream filename;
    filename << "tests/" << test_query_num << ".test";
    int out_fd = open(filename.str().c_str(), O_RDWR | O_CREAT, 0777);
    if (out_fd == -1) {
      ABORT("Cannot create the test case file, abort.\n");
    }
    context* c; solver* s;
    std::tie(c, s) = cz3.get_my_thread_local_instance();
    model m = s->get_model();
    output << m << std::endl;
    int n = write(out_fd, output.str().c_str(), output.str().size());
    close(out_fd);
  }
  cz3.pop();
}

