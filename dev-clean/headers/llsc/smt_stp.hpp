#ifndef LLSC_STP_HEADERS
#define LLSC_STP_HEADERS

#include <stp/c_interface.h>
#include <stp_handle.hpp>

struct ExprHandle: public std::shared_ptr<void> {
  typedef std::shared_ptr<void> Base;

  static void freeExpr(Expr e) {
    vc_DeleteExpr(e);
  }

  ExprHandle(Expr e): Base(e, freeExpr) {}
};

// TODO: refactor the caching mechanism for clarity
// Note: CheckerSTP does not make use of parallelism and currently doesn't work for async parallelism
class CheckerSTP : public Checker {
private:
  using CacheKey = std::set<ExprHandle>;
  using CexType = std::map<ExprHandle, IntData>;
  using CacheResult = std::pair<int, CexType>;

  VC vc;
  CexType *cex, cex2;
  std::set<ExprHandle> variables;
  std::map<CacheKey, CacheResult> cache_map;
  std::unordered_map<PtrVal, std::pair<ExprHandle, std::set<ExprHandle>>> stp_env;

  const CexType* get_counterexample() {
    if (use_cexcache) return cex;
    cex2.clear();
    get_STP_counterexample(cex2);
    return &cex2;
  }

  void get_STP_counterexample(CexType &cex) {
    for (auto expr: variables) {
      auto val = vc_getCounterExample(vc, expr.get());
      cex[expr] = getBVUnsignedLongLong(val);
      vc_DeleteExpr(val);
    }
  }

  ExprHandle construct_STP_expr(PtrVal e, std::set<ExprHandle> &vars) {
    // search expr cache
    if (use_objcache) {
      auto it = stp_env.find(e);
      if (it != stp_env.end()) {
        auto &vars2 = it->second.second;
        vars.insert(vars2.begin(), vars2.end());
        return it->second.first;
      }
    }
    // query internal
    std::set<ExprHandle> vars2;
    auto ret = construct_STP_expr_internal(e, vars2);
    vars.insert(vars2.begin(), vars2.end());
    // store expr cache
    if (use_objcache) {
      stp_env.emplace(e, std::make_pair(ret, std::move(vars2)));
    }
    return ret;
  }

  ExprHandle construct_STP_expr_internal(PtrVal e, std::set<ExprHandle> &vars) {
    auto int_e = std::dynamic_pointer_cast<IntV>(e);
    if (int_e) {
      return vc_bvConstExprFromLL(vc, int_e->bw, int_e->as_signed());
    }
    auto sym_e = std::dynamic_pointer_cast<SymV>(e);
    if (!sym_e) ABORT("Non-symbolic/integer value in path condition");

    if (!sym_e->name.empty()) {
      auto name = sym_e->name;
      ExprHandle stp_expr = vc_varExpr(vc, name.c_str(), vc_bvType(vc, sym_e->bw));
      vars.insert(stp_expr);
      return stp_expr;
    }

    std::vector<ExprHandle> expr_rands;
    int bw = sym_e->bw;
    for (auto e : sym_e->rands) {
      expr_rands.push_back(construct_STP_expr(e, vars));
    }
    switch (sym_e->rator) {
    case iOP::op_add:
      return vc_bvPlusExpr(vc, bw, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_sub:
      return vc_bvMinusExpr(vc, bw, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_mul:
      return vc_bvMultExpr(vc, bw, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_sdiv:
    case iOP::op_udiv:
      return vc_bvDivExpr(vc, bw, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_uge:
      return vc_bvGeExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_sge:
      return vc_sbvGeExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_ugt:
      return vc_bvGtExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_sgt:
      return vc_sbvGtExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_ule:
      return vc_bvLeExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_sle:
      return vc_sbvLeExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_ult:
      return vc_bvLtExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_slt:
      return vc_sbvLtExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_eq:
      return vc_eqExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_neq:
      return vc_notExpr(vc, vc_eqExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get()));
    case iOP::op_neg:
      return vc_notExpr(vc, expr_rands.at(0).get());
    case iOP::op_sext:
      return vc_bvSignExtend(vc, expr_rands.at(0).get(), bw);
    case iOP::op_zext:
      return vc_bvSignExtend(vc, expr_rands.at(0).get(), bw);  // TODO
    case iOP::op_shl:
      return vc_bvLeftShiftExprExpr(vc, bw, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_lshr:
      return vc_bvRightShiftExprExpr(vc, bw, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_ashr:
      return vc_bvSignedRightShiftExprExpr(vc, bw, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_and:
      return vc_bvAndExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_or:
      return vc_bvOrExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_xor:
      return vc_bvXorExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_urem:
      return vc_bvRemExpr(vc, bw, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_srem:
      return vc_sbvRemExpr(vc, bw, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_trunc:
      // bvExtract(vc, e, h, l) -> e[l:h+1]
      return vc_bvExtract(vc, expr_rands.at(0).get(), bw-1, 0);
    case iOP::op_concat:
      return vc_bvConcatExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case iOP::op_extract:
      return vc_bvExtract(vc, expr_rands.at(0).get(),
                              getBVInt(expr_rands.at(1).get()),
                              getBVInt(expr_rands.at(2).get()));
    default: break;
    }
    ABORT("unkown operator when constructing STP expr");
  }

  int make_query_internal(PC pcobj) {
    CacheResult *result;
    auto pc = pcobj.get_path_conds();
    auto last = pcobj.get_last_cond();
    CacheKey pc2;
    // constraint independence
    if (use_cons_indep && last && pc.size() > 1) {
      std::map<ExprHandle, std::set<ExprHandle>> v2q, q2v;
      std::queue<ExprHandle> queue;
      for (auto &e: pc) {
        std::set<ExprHandle> vars;
        auto q = construct_STP_expr(e, vars);
        if (e == last)
          queue.push(q);
        for (auto &v: vars)
          v2q[v].insert(q);
        q2v[q] = std::move(vars);
      }
      while (!queue.empty()) {
        auto q = queue.front(); queue.pop();
        auto &vars = q2v[q];
        if (!vars.empty()) {
          pc2.insert(q);
          variables.insert(vars.begin(), vars.end());
          for (auto &v: vars) {
            for (auto &q2: v2q[v])
              if (q2 != q)
                queue.push(q2);
            v2q[v].clear();
          }
          vars.clear();
        }
      }
    } else {
      for (auto& e: pc)
        pc2.insert(construct_STP_expr(e, variables));
    }
    // cex cache: query
    if (use_cexcache) {
      auto ins = cache_map.emplace(pc2, CacheResult {});
      result = &(ins.first->second);
      cex = &(result->second);
      if (!ins.second) {
        cached_query_num++;
        return result->first;
      }
    }
    // actual solving
    auto start = steady_clock::now();
    for (auto &e: pc2) {
      vc_assertFormula(vc, e.get());
    }
    ExprHandle fls = vc_falseExpr(vc);
    int retcode = vc_query(vc, fls.get());
    auto end = steady_clock::now();
    solver_time += duration_cast<microseconds>(end - start);
    // cex cache: store
    if (use_cexcache) {
      result->first = retcode;
      if (retcode == 0)
        get_STP_counterexample(*cex);
    }
    return retcode;
  }

  solver_result to_solver_result(int r) {
    solver_result mapping[4] = {sat, unsat, unknown, unknown};
    return mapping[r];
  }

public:
  CheckerSTP() {
    std::cout << "Use STP solver\n";
    vc = vc_createValidityChecker();
  }
  ~CheckerSTP() {
    variables.clear();
    cache_map.clear();
    stp_env.clear();
    vc_Destroy(vc);
  }
  void init_solvers() override {}
  void destroy_solvers() override {}
  solver_result make_query(PC pc) override {
    variables.clear();
    int r = make_query_internal(std::move(pc));
    return to_solver_result(r);
  }
  void print_model(std::stringstream& output) override {
    auto cex = get_counterexample();
    for (auto &kv: *cex) {
      output << exprName(kv.first.get()) << " == " << kv.second << std::endl;
    }
  }
  void push() override {
    vc_push(vc);
  }
  void pop() override {
    vc_pop(vc);
  }
  void reset() override {
    variables.clear();
    cache_map.clear();
    stp_env.clear();
    vc_Destroy(vc);
    vc = vc_createValidityChecker();
  }
};

#endif
