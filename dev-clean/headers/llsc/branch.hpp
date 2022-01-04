#ifndef LLSC_BRANCH_HEADERS
#define LLSC_BRANCH_HEADERS

#ifdef PURE_STATE

inline immer::flex_vector<std::pair<SS, PtrVal>>
sym_exec_br(SS ss, SExpr t_cond, SExpr f_cond,
            immer::flex_vector<std::pair<SS, PtrVal>> (*tf)(SS),
            immer::flex_vector<std::pair<SS, PtrVal>> (*ff)(SS)) {
  auto pc = ss.getPC();
  auto tbr_sat = check_pc(pc.add(t_cond));
  auto fbr_sat = check_pc(pc.add(f_cond));
  if (tbr_sat && fbr_sat) {
    cov.inc_path(1);
    SS tbr_ss = ss.addPC(t_cond);
    SS fbr_ss = ss.addPC(f_cond);
    if (can_par()) {
      std::future<immer::flex_vector<std::pair<SS, PtrVal>>> tf_res =
        create_async<immer::flex_vector<std::pair<SS, PtrVal>>>([&]{
          return tf(tbr_ss);
        });
      auto ff_res = ff(fbr_ss);
      return tf_res.get() + ff_res;
    } else return tf(tbr_ss) + ff(fbr_ss);
  } else if (tbr_sat) {
    SS tbr_ss = ss.addPC(t_cond);
    return tf(tbr_ss);
  } else if (fbr_sat) {
    SS fbr_ss = ss.addPC(f_cond);
    return ff(fbr_ss);
  } else {
    return immer::flex_vector<std::pair<SS, PtrVal>>{};
  }
}

#endif

#ifdef IMPURE_STATE
inline std::monostate
sym_exec_br_k(SS& ss, SExpr t_cond, SExpr f_cond,
              std::monostate (*tf)(SS&, std::function<std::monostate(SS&, PtrVal)>),
              std::monostate (*ff)(SS&, std::function<std::monostate(SS&, PtrVal)>),
              std::function<std::monostate(SS&, PtrVal)> k) {
  auto pc = ss.getPC();
  auto ins = pc.insert(t_cond);
  auto tbr_sat = check_pc(pc);
  if (ins.second) pc.erase(ins.first);
  pc.insert(f_cond);
  auto fbr_sat = check_pc(pc);
  if (tbr_sat && fbr_sat) {
    cov.inc_path(1);
    SS tbr_ss = ss.copy().addPC(t_cond);
    SS fbr_ss = ss.addPC(f_cond);
    if (can_par()) {
      std::future<std::monostate> tf_res =
        create_async<std::monostate>([&]{
          return tf(tbr_ss, k);
        });
      auto ff_res = ff(fbr_ss, k);
      tf_res.get();
      return std::monostate{};
    } else {
      tf(tbr_ss, k);
      ff(fbr_ss, k);
      return std::monostate{};
    }
  } else if (tbr_sat) {
    SS tbr_ss = ss.addPC(t_cond);
    return tf(tbr_ss, k);
  } else if (fbr_sat) {
    SS fbr_ss = ss.addPC(f_cond);
    return ff(fbr_ss, k);
  } else {
    return std::monostate{};
  }
}
#endif

#endif
