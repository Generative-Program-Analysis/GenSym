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
    cov().inc_path(1);
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
    cov().inc_path(1);
    SS tbr_ss = ss.add_PC(t_cond);
    SS fbr_ss = ss.add_PC(f_cond);
    if (can_par_tp()) {
      tp.add_task([tf, tbr_ss=std::move(tbr_ss), k]{ return tf(tbr_ss, k); });
      tp.add_task([ff, fbr_ss=std::move(fbr_ss), k]{ return ff(fbr_ss, k); });
      return std::monostate{};
    } else {
      tf(tbr_ss, k);
      ff(fbr_ss, k);
      return std::monostate{};
    }
    /*
    if (can_par_async()) {
      std::future<std::monostate> tf_res =
        create_async<std::monostate>([&]{
          return tf(tbr_ss, k);
        });
      auto ff_res = ff(fbr_ss, k);
      tf_res.get();
      return std::monostate{};
    }
    */
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
array_lookup(SS ss, PtrVal base, PtrVal offset, size_t esize) {
  immer::flex_vector_transient<std::pair<SS, PtrVal>> tmp;
  auto baseloc = std::dynamic_pointer_cast<LocV>(base);

  if (auto offint = std::dynamic_pointer_cast<IntV>(offset)) {
    tmp.push_back(std::make_pair(ss, baseloc + offint->as_signed() * esize));
  }
  else if (auto offsym = std::dynamic_pointer_cast<SymV>(offset)) {
    int cnt = 0;
    for (int newl = (baseloc->l - baseloc->base) % esize + baseloc->base;
         newl + esize <= baseloc->base + baseloc->size; newl += esize) {
      auto cond = int_op_2(iOP::op_eq, offset, make_IntV((newl - baseloc->l) / esize, offset->get_bw()));
      auto ss2 = ss.add_PC(cond);
      if (check_pc(ss2.get_PC())) {
        cnt++;
        tmp.push_back(std::make_pair(ss2, baseloc + (newl - baseloc->l)));
      }
    }
    assert(cnt > 0);
    cov().inc_path(cnt - 1);
  }
  else ABORT("Error: unknown array offset kind.");

  return tmp.persistent();
}

inline std::monostate
array_lookup_k(SS ss, PtrVal base, PtrVal offset, size_t esize,
               std::function<std::monostate(SS, PtrVal)> k) {
  auto baseloc = std::dynamic_pointer_cast<LocV>(base);

  if (auto offint = std::dynamic_pointer_cast<IntV>(offset)) {
    k(ss, baseloc + offint->as_signed() * esize);
  }
  else if (auto offsym = std::dynamic_pointer_cast<SymV>(offset)) {
    int cnt = 0;
    for (int newl = (baseloc->l - baseloc->base) % esize + baseloc->base;
         newl + esize <= baseloc->base + baseloc->size; newl += esize) {
      auto cond = int_op_2(iOP::op_eq, offset, make_IntV((newl - baseloc->l) / esize, offset->get_bw()));
      auto ss2 = ss.add_PC(cond);
      if (check_pc(ss2.get_PC())) {
        cnt++;
        auto addr = baseloc + (newl - baseloc->l);
        if (can_par_tp()) {
          tp.add_task([addr=std::move(addr), ss2=std::move(ss2), k]{ return k(ss2, addr); });
        } else {
          k(ss2, addr);
        }
      }
    }
    assert(cnt > 0);
    cov().inc_path(cnt - 1);
  }
  else ABORT("Error: unknown array offset kind.");
  return std::monostate{};
}

#endif

#ifdef IMPURE_STATE

// use immer::flex_vector as argument list and result list
inline immer::flex_vector<std::pair<SS, PtrVal>>
sym_exec_br(SS& ss, PtrVal t_cond, PtrVal f_cond,
            immer::flex_vector<std::pair<SS, PtrVal>> (*tf)(SS&),
            immer::flex_vector<std::pair<SS, PtrVal>> (*ff)(SS&)) {
  auto pc = ss.get_PC();
  pc.add(t_cond);
  auto tbr_sat = check_pc(pc);
  pc.replace_last_cond(f_cond);
  auto fbr_sat = check_pc(pc);
  if (tbr_sat && fbr_sat) {
    cov().inc_path(1);
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
  auto pc = ss.get_PC();
  pc.add(t_cond);
  auto tbr_sat = check_pc(pc);
  pc.replace_last_cond(f_cond);
  auto fbr_sat = check_pc(pc);
  if (tbr_sat && fbr_sat) {
    cov().inc_path(1);
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
  auto pc = ss.get_PC();
  pc.add(t_cond);
  auto tbr_sat = check_pc(pc);
  pc.replace_last_cond(f_cond);
  auto fbr_sat = check_pc(pc);

  if (tbr_sat && fbr_sat) {
    cov().inc_path(1);
    SS tbr_ss = ss.copy().add_PC(t_cond);
    SS fbr_ss = ss.add_PC(f_cond);
    if (can_par_tp()) {
      tp.add_task([tf, tbr_ss=std::move(tbr_ss), k]{ return tf((SS&)tbr_ss, k); });
      tp.add_task([ff, fbr_ss=std::move(fbr_ss), k]{ return ff((SS&)fbr_ss, k); });
      return std::monostate{};
    } else {
      tf(tbr_ss, k);
      ff(fbr_ss, k);
      return std::monostate{};
    }
    /*
    if (can_par_async()) {
      std::future<std::monostate> tf_res =
        create_async<std::monostate>([&]{
          return tf(tbr_ss, k);
        });
      auto ff_res = ff(fbr_ss, k);
      tf_res.get();
      return std::monostate{};
    }
    */
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
array_lookup(SS& ss, PtrVal base, PtrVal offset, size_t esize) {
  immer::flex_vector_transient<std::pair<SS, PtrVal>> tmp;
  auto baseloc = std::dynamic_pointer_cast<LocV>(base);

  if (auto offint = std::dynamic_pointer_cast<IntV>(offset)) {
    tmp.push_back(std::make_pair(std::move(ss), baseloc + offint->as_signed() * esize));
  }
  else if (auto offsym = std::dynamic_pointer_cast<SymV>(offset)) {
    int cnt = 0;
    for (int newl = (baseloc->l - baseloc->base) % esize + baseloc->base;
         newl + esize <= baseloc->base + baseloc->size; newl += esize) {
      auto cond = int_op_2(iOP::op_eq, offset, make_IntV((newl - baseloc->l) / esize, offset->get_bw()));
      auto ss2 = ss.copy().add_PC(cond);
      if (check_pc(ss2.get_PC())) {
        cnt++;
        tmp.push_back(std::make_pair(std::move(ss2), baseloc + (newl - baseloc->l)));
      }
    }
    assert(cnt > 0);
    cov().inc_path(cnt - 1);
  }
  else ABORT("Error: unknown array offset kind.");

  return tmp.persistent();
}

inline std::monostate
array_lookup_k(SS& ss, PtrVal base, PtrVal offset, size_t esize,
               std::function<std::monostate(SS&, PtrVal)> k) {
  auto baseloc = std::dynamic_pointer_cast<LocV>(base);

  if (auto offint = std::dynamic_pointer_cast<IntV>(offset)) {
    k(ss, baseloc + offint->as_signed() * esize);
  }
  else if (auto offsym = std::dynamic_pointer_cast<SymV>(offset)) {
    int cnt = 0;
    for (int newl = (baseloc->l - baseloc->base) % esize + baseloc->base;
         newl + esize <= baseloc->base + baseloc->size; newl += esize) {
      auto cond = int_op_2(iOP::op_eq, offset, make_IntV((newl - baseloc->l) / esize, offset->get_bw()));
      auto ss2 = ss.copy().add_PC(cond);
      if (check_pc(ss2.get_PC())) {
        cnt++;
        k(ss2, baseloc + (newl - baseloc->l));
      }
    }
    assert(cnt > 0);
    cov().inc_path(cnt - 1);
  }
  else ABORT("Error: unknown array offset kind.");
  return std::monostate{};
}
#endif

#endif
