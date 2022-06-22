#ifndef LLSC_BRANCH_HEADERS
#define LLSC_BRANCH_HEADERS

// TODO: should be able to generate these functions too

#ifdef PURE_STATE

inline std::monostate async_exec_block(const std::function<std::monostate()>& f) {
  if (can_par_tp()) {
    tp.add_task(f);
    return std::monostate{};
  }
  return f();
}

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

// Todo : check offset out of bound
inline immer::flex_vector<std::pair<SS, PtrVal>>
array_lookup(SS ss, PtrVal base, PtrVal offset, size_t esize) {
  immer::flex_vector_transient<std::pair<SS, PtrVal>> result;
  auto baseloc = std::dynamic_pointer_cast<LocV>(base);

  if (auto offint = std::dynamic_pointer_cast<IntV>(offset)) {
    // base may not be a locv, ie a bad pointer
    result.push_back(std::make_pair(ss, base + (offint->as_signed() * esize)));
  } else if (auto offsym = std::dynamic_pointer_cast<SymV>(offset)) {
    int cnt = 0;
    int lower_bound = ((int)(baseloc->base - baseloc->l)) / esize;
    int higher_bound = ((int)(baseloc->base + baseloc->size - baseloc->l)) / esize - 1;
    ASSERT(higher_bound >= lower_bound, "Bad bound");
    int possible_num = (higher_bound - lower_bound) + 1;

    auto low_cond = int_op_2(iOP::op_sge, offsym, make_IntV(lower_bound, offsym->get_bw()));
    auto high_cond = int_op_2(iOP::op_sle, offsym, make_IntV(higher_bound, offsym->get_bw()));
    auto ss2 = ss.add_PC(low_cond).add_PC(high_cond);
    auto res = get_sat_value(ss2.get_PC(), offsym);
    while (res.first) {
      cnt++;
      int offset_val = res.second;
      auto t_cond = int_op_2(iOP::op_eq, offsym, make_IntV(offset_val, offsym->get_bw()));
      result.push_back(std::make_pair(ss.add_PC(t_cond), baseloc + (offset_val*esize)));
      ss2 = ss2.add_PC(SymV::neg(t_cond));
      res = get_sat_value(ss2.get_PC(), offsym);
    }
    ASSERT(cnt > 0, "No satisfiable offset value");
    cov().inc_path(cnt - 1);
  } else ABORT("Error: unknown array offset kind.");

  return result.persistent();
}

inline std::monostate
array_lookup_k(SS ss, PtrVal base, PtrVal offset, size_t esize,
               std::function<std::monostate(SS, PtrVal)> k) {
  auto baseloc = std::dynamic_pointer_cast<LocV>(base);

  if (auto offint = std::dynamic_pointer_cast<IntV>(offset)) {
    // base may not be a locv, ie a bad pointer
    k(ss, base + (offint->as_signed() * esize));
  }
  else if (auto offsym = std::dynamic_pointer_cast<SymV>(offset)) {
    int cnt = 0;
    int lower_bound = ((int)(baseloc->base - baseloc->l)) / esize;
    int higher_bound = ((int)(baseloc->base + baseloc->size - baseloc->l)) / esize - 1;
    ASSERT(higher_bound >= lower_bound, "Bad bound");
    int possible_num = (higher_bound - lower_bound) + 1;

    auto low_cond = int_op_2(iOP::op_sge, offsym, make_IntV(lower_bound, offsym->get_bw()));
    auto high_cond = int_op_2(iOP::op_sle, offsym, make_IntV(higher_bound, offsym->get_bw()));
    auto ss2 = ss.add_PC(low_cond).add_PC(high_cond);
    auto res = get_sat_value(ss2.get_PC(), offsym);
    while (res.first) {
      cnt++;
      int offset_val = res.second;
      auto t_cond = int_op_2(iOP::op_eq, offsym, make_IntV(offset_val, offsym->get_bw()));
      auto new_loc = baseloc + (offset_val*esize);
      auto new_ss = ss.add_PC(t_cond);
      if (can_par_tp()) {
        tp.add_task([new_loc=std::move(new_loc), new_ss=std::move(new_ss), k]{ return k(new_ss, new_loc); });
      } else {
        k(new_ss, new_loc);
      }
      ss2 = ss2.add_PC(SymV::neg(t_cond));
      res = get_sat_value(ss2.get_PC(), offsym);
    }
    ASSERT(cnt > 0, "No satisfiable offset value");
    cov().inc_path(cnt - 1);
  } else ABORT("Error: unknown array offset kind.");
  return std::monostate{};
}

#endif

#ifdef IMPURE_STATE

inline std::monostate async_exec_block(
    std::monostate (*f)(SS&, std::function<std::monostate(SS&, PtrVal)>),
    SS ss, std::function<std::monostate(SS&, PtrVal)> k) {
  if (can_par_tp()) {
    tp.add_task([f, ss=std::move(ss), k]{ return f((SS&)ss, k); });
    return std::monostate{};
  }
  return f(ss, k);
}

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
  immer::flex_vector_transient<std::pair<SS, PtrVal>> result;
  auto baseloc = std::dynamic_pointer_cast<LocV>(base);

  if (auto offint = std::dynamic_pointer_cast<IntV>(offset)) {
    // base may not be a locv, ie a bad pointer
    result.push_back(std::make_pair(std::move(ss), base + (offint->as_signed() * esize)));
  } else if (auto offsym = std::dynamic_pointer_cast<SymV>(offset)) {
    int cnt = 0;
    int lower_bound = ((int)(baseloc->base - baseloc->l)) / esize;
    int higher_bound = ((int)(baseloc->base + baseloc->size - baseloc->l)) / esize - 1;
    ASSERT(higher_bound >= lower_bound, "Bad bound");
    int possible_num = (higher_bound - lower_bound) + 1;

    auto low_cond = int_op_2(iOP::op_sge, offsym, make_IntV(lower_bound, offsym->get_bw()));
    auto high_cond = int_op_2(iOP::op_sle, offsym, make_IntV(higher_bound, offsym->get_bw()));
    auto ss2 = ss.copy().add_PC(low_cond).add_PC(high_cond);
    auto res = get_sat_value(ss2.get_PC(), offsym);
    while (res.first) {
      cnt++;
      int offset_val = res.second;
      auto t_cond = int_op_2(iOP::op_eq, offsym, make_IntV(offset_val, offsym->get_bw()));
      result.push_back(std::make_pair(std::move(ss.copy().add_PC(t_cond)), baseloc + (offset_val*esize)));
      ss2.add_PC(SymV::neg(t_cond));
      res = get_sat_value(ss2.get_PC(), offsym);
    }
    ASSERT(cnt > 0, "No satisfiable offset value");
    cov().inc_path(cnt - 1);
  } else ABORT("Error: unknown array offset kind.");

  return result.persistent();
}

inline std::monostate
array_lookup_k(SS& ss, PtrVal base, PtrVal offset, size_t esize,
               std::function<std::monostate(SS&, PtrVal)> k) {
  auto baseloc = std::dynamic_pointer_cast<LocV>(base);

  if (auto offint = std::dynamic_pointer_cast<IntV>(offset)) {
    // base may not be a locv, ie a bad pointer
    k(ss, base + (offint->as_signed() * esize));
  }
  else if (auto offsym = std::dynamic_pointer_cast<SymV>(offset)) {
    int cnt = 0;
    int lower_bound = ((int)(baseloc->base - baseloc->l)) / esize;
    int higher_bound = ((int)(baseloc->base + baseloc->size - baseloc->l)) / esize - 1;
    ASSERT(higher_bound >= lower_bound, "Bad bound");
    int possible_num = (higher_bound - lower_bound) + 1;

    auto low_cond = int_op_2(iOP::op_sge, offsym, make_IntV(lower_bound, offsym->get_bw()));
    auto high_cond = int_op_2(iOP::op_sle, offsym, make_IntV(higher_bound, offsym->get_bw()));
    auto ss2 = ss.copy().add_PC(low_cond).add_PC(high_cond);
    auto res = get_sat_value(ss2.get_PC(), offsym);
    while (res.first) {
      cnt++;
      int offset_val = res.second;
      auto t_cond = int_op_2(iOP::op_eq, offsym, make_IntV(offset_val, offsym->get_bw()));
      auto new_loc = baseloc + (offset_val*esize);
      auto new_ss = ss.copy().add_PC(t_cond);
      if (can_par_tp()) {
        tp.add_task([new_loc=std::move(new_loc), new_ss=std::move(new_ss), k]{ return k((SS&)new_ss, new_loc); });
      } else {
        k(new_ss, new_loc);
      }
      ss2.add_PC(SymV::neg(t_cond));
      res = get_sat_value(ss2.get_PC(), offsym);
    }
    ASSERT(cnt > 0, "No satisfiable offset value");
    cov().inc_path(cnt - 1);
  } else ABORT("Error: unknown array offset kind.");
  return std::monostate{};
}
#endif

#endif
