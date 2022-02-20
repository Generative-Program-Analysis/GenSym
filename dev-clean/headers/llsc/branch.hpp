#ifndef LLSC_BRANCH_HEADERS
#define LLSC_BRANCH_HEADERS

// TODO: should be able to generate these functions too

#ifdef PURE_STATE

inline immer::flex_vector<std::pair<SS, PtrVal>>
sym_exec_br(SS ss, PtrVal t_cond, PtrVal f_cond,
            immer::flex_vector<std::pair<SS, PtrVal>> (*tf)(SS),
            immer::flex_vector<std::pair<SS, PtrVal>> (*ff)(SS)) {
  auto pc = ss.get_PC();
  auto tbr_sat = check_pc(pc.add(t_cond));
  auto fbr_sat = check_pc(pc.add(f_cond));
  if (tbr_sat && fbr_sat) {
    cov.inc_path(1);
    SS tbr_ss = ss.add_PC(t_cond);
    SS fbr_ss = ss.add_PC(f_cond);
    if (can_par_async()) {
      std::future<immer::flex_vector<std::pair<SS, PtrVal>>> tf_res =
        create_async<immer::flex_vector<std::pair<SS, PtrVal>>>([&]{
          return tf(tbr_ss);
        });
      auto ff_res = ff(fbr_ss);
      return tf_res.get() + ff_res;
    } else return tf(tbr_ss) + ff(fbr_ss);
  } else if (tbr_sat) {
    SS tbr_ss = ss.add_PC(t_cond);
    return tf(tbr_ss);
  } else if (fbr_sat) {
    SS fbr_ss = ss.add_PC(f_cond);
    return ff(fbr_ss);
  } else {
    return immer::flex_vector<std::pair<SS, PtrVal>>{};
  }
}

inline std::monostate
sym_exec_br_k(SS ss, PtrVal t_cond, PtrVal f_cond,
              std::monostate (*tf)(SS, std::function<std::monostate(SS, PtrVal)>),
              std::monostate (*ff)(SS, std::function<std::monostate(SS, PtrVal)>),
              std::function<std::monostate(SS, PtrVal)> k) {
  auto pc = ss.get_PC();
  auto tbr_sat = check_pc(pc.add(t_cond));
  auto fbr_sat = check_pc(pc.add(f_cond));
  if (tbr_sat && fbr_sat) {
    cov.inc_path(1);
    SS tbr_ss = ss.add_PC(t_cond);
    SS fbr_ss = ss.add_PC(f_cond);
#if USE_TP
    tp.add_task([tf, tbr_ss=std::move(tbr_ss), k]{ tf(tbr_ss, k); });
    tp.add_task([ff, fbr_ss=std::move(fbr_ss), k]{ ff(fbr_ss, k); });
    return std::monostate{};
#else
    if (can_par_async()) {
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
#endif
  } else if (tbr_sat) {
    SS tbr_ss = ss.add_PC(t_cond);
    return tf(tbr_ss, k);
  } else if (fbr_sat) {
    SS fbr_ss = ss.add_PC(f_cond);
    return ff(fbr_ss, k);
  } else {
    return std::monostate{};
  }
}

inline immer::flex_vector<std::pair<SS, PtrVal>>
array_lookup(SS ss, PtrVal base, PtrVal offset, size_t esize, size_t nsize) {
  auto baseaddr = proj_LocV(base);
  auto basekind = proj_LocV_kind(base);
  if (offset->to_IntV()) {
    auto off = proj_IntV(offset);
    return { std::make_pair(ss, make_LocV(baseaddr + esize * off, basekind)) };
  }
  int cnt = 0;
  immer::flex_vector_transient<std::pair<SS, PtrVal>> tmp;
  for (size_t idx = 0; idx < nsize; idx++) {
    auto cond = int_op_2(op_eq, offset, make_IntV(idx, offset->get_bw()));
    auto ss2 = ss.add_PC(cond);
    if (check_pc(ss2.get_PC())) {
      cnt++;
      tmp.push_back(std::make_pair(ss2, make_LocV(baseaddr + esize * idx, basekind)));
    }
  }
  assert(cnt > 0);
  cov.inc_path(cnt - 1);
  return tmp.persistent();
}

inline std::monostate
array_lookup_k(SS ss, PtrVal base, PtrVal offset, size_t esize, size_t nsize,
               std::function<std::monostate(SS, PtrVal)> k) {
  auto baseaddr = proj_LocV(base);
  auto basekind = proj_LocV_kind(base);
  if (offset->to_IntV()) {
    auto off = proj_IntV(offset);
    return k(ss, make_LocV(baseaddr + esize * off, basekind));
  }
  int cnt = 0;
  for (size_t idx = 0; idx < nsize; idx++) {
    auto cond = int_op_2(op_eq, offset, make_IntV(idx, offset->get_bw()));
    auto ss2 = ss.add_PC(cond);
    if (check_pc(ss2.get_PC())) {
      cnt++;
      auto addr = make_LocV(baseaddr + esize * idx, basekind);
#if USE_TP
      tp.add_task([addr=std::move(addr), ss2, k]{ k(ss2, addr); });
#else
      k(ss2, addr);
#endif
    }
  }
  assert(cnt > 0);
  cov.inc_path(cnt - 1);
  return std::monostate{};
}

#endif

#ifdef IMPURE_STATE

// use immer::flex_vector as argument list and result list
inline immer::flex_vector<std::pair<SS, PtrVal>>
sym_exec_br(SS& ss, PtrVal t_cond, PtrVal f_cond,
            immer::flex_vector<std::pair<SS, PtrVal>> (*tf)(SS&),
            immer::flex_vector<std::pair<SS, PtrVal>> (*ff)(SS&)) {
  auto pc = ss.get_path_conds();
  pc.push_back(t_cond);
  auto tbr_sat = check_pc(pc);
  pc.pop_back();
  pc.push_back(f_cond);
  auto fbr_sat = check_pc(pc);
  if (tbr_sat && fbr_sat) {
    cov.inc_path(1);
    SS tbr_ss = ss.copy().add_PC(t_cond);
    SS fbr_ss = ss.add_PC(f_cond);
    if (can_par_async()) {
      std::future<immer::flex_vector<std::pair<SS, PtrVal>>> tf_res =
        create_async<immer::flex_vector<std::pair<SS, PtrVal>>>([&]{
          return tf(tbr_ss);
        });
      auto ff_res = ff(fbr_ss);
      return tf_res.get() + ff_res;
    } else return tf(tbr_ss) + ff(fbr_ss);
  } else if (tbr_sat) {
    SS tbr_ss = ss.add_PC(t_cond);
    return tf(tbr_ss);
  } else if (fbr_sat) {
    SS fbr_ss = ss.add_PC(f_cond);
    return ff(fbr_ss);
  } else {
    return immer::flex_vector<std::pair<SS, PtrVal>>{};
  }
}

// use std::vector as argument list and result list
inline std::vector<std::pair<SS, PtrVal>>
sym_exec_br(SS& ss, PtrVal t_cond, PtrVal f_cond,
            std::vector<std::pair<SS, PtrVal>> (*tf)(SS&),
            std::vector<std::pair<SS, PtrVal>> (*ff)(SS&)) {
  auto pc = ss.get_path_conds();
  pc.push_back(t_cond);
  auto tbr_sat = check_pc(pc);
  pc.pop_back();
  pc.push_back(f_cond);
  auto fbr_sat = check_pc(pc);
  if (tbr_sat && fbr_sat) {
    cov.inc_path(1);
    SS tbr_ss = ss.copy().add_PC(t_cond);
    SS fbr_ss = ss.add_PC(f_cond);
    if (can_par_async()) {
      std::future<std::vector<std::pair<SS, PtrVal>>> tf_res =
        create_async<std::vector<std::pair<SS, PtrVal>>>([&]{
          return tf(tbr_ss);
        });
      auto ff_vec = ff(fbr_ss);
      auto tf_vec = tf_res.get();
      tf_vec.insert(tf_vec.end(), ff_vec.begin(), ff_vec.end());
      return tf_vec;
    } else {
      auto tf_vec = tf(tbr_ss);
      auto ff_vec = ff(fbr_ss);
      tf_vec.insert(tf_vec.end(), ff_vec.begin(), ff_vec.end());
      return tf_vec;
    }
  } else if (tbr_sat) {
    SS tbr_ss = ss.add_PC(t_cond);
    return tf(tbr_ss);
  } else if (fbr_sat) {
    SS fbr_ss = ss.add_PC(f_cond);
    return ff(fbr_ss);
  } else {
    return std::vector<std::pair<SS, PtrVal>>{};
  }
}

inline std::monostate
sym_exec_br_k(SS& ss, PtrVal t_cond, PtrVal f_cond,
              std::monostate (*tf)(SS&, std::function<std::monostate(SS&, PtrVal)>),
              std::monostate (*ff)(SS&, std::function<std::monostate(SS&, PtrVal)>),
              std::function<std::monostate(SS&, PtrVal)> k) {
  auto pc = ss.get_path_conds();
  pc.push_back(t_cond);
  auto tbr_sat = check_pc(pc);
  pc.pop_back();
  pc.push_back(f_cond);
  auto fbr_sat = check_pc(pc);
  if (tbr_sat && fbr_sat) {
    cov.inc_path(1);
    SS tbr_ss = ss.copy().add_PC(t_cond);
    SS fbr_ss = ss.add_PC(f_cond);
    if (can_par_async()) {
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
    SS tbr_ss = ss.add_PC(t_cond);
    return tf(tbr_ss, k);
  } else if (fbr_sat) {
    SS fbr_ss = ss.add_PC(f_cond);
    return ff(fbr_ss, k);
  } else {
    return std::monostate{};
  }
}

inline immer::flex_vector<std::pair<SS, PtrVal>>
array_lookup(SS& ss, PtrVal base, PtrVal offset, size_t esize, size_t nsize) {
  auto baseaddr = proj_LocV(base);
  auto basekind = proj_LocV_kind(base);
  if (offset->to_IntV()) {
    auto off = proj_IntV(offset);
    return { std::make_pair(std::move(ss), make_LocV(baseaddr + esize * off, basekind)) };
  }
  int cnt = 0;
  immer::flex_vector_transient<std::pair<SS, PtrVal>> tmp;
  for (size_t idx = 0; idx < nsize; idx++) {
    auto cond = int_op_2(op_eq, offset, make_IntV(idx, offset->get_bw()));
    auto ss2 = ss.copy().add_PC(cond);
    if (check_pc(ss2.get_PC())) {
      cnt++;
      tmp.push_back(std::make_pair(std::move(ss2), make_LocV(baseaddr + esize * idx, basekind)));
    }
  }
  assert(cnt > 0);
  cov.inc_path(cnt - 1);
  return tmp.persistent();
}

inline std::monostate
array_lookup_k(SS& ss, PtrVal base, PtrVal offset, size_t esize, size_t nsize,
               std::function<std::monostate(SS&, PtrVal)> k) {
  auto baseaddr = proj_LocV(base);
  auto basekind = proj_LocV_kind(base);
  if (offset->to_IntV()) {
    auto off = proj_IntV(offset);
    return k(ss, make_LocV(baseaddr + esize * off, basekind));
  }
  int cnt = 0;
  for (size_t idx = 0; idx < nsize; idx++) {
    auto cond = int_op_2(op_eq, offset, make_IntV(idx, offset->get_bw()));
    auto ss2 = ss.copy().add_PC(cond);
    if (check_pc(ss2.get_PC())) {
      cnt++;
      k(ss2, make_LocV(baseaddr + esize * idx, basekind));
    }
  }
  assert(cnt > 0);
  cov.inc_path(cnt - 1);
  return std::monostate{};
}
#endif

#endif
