#ifndef LLSC_BRANCH_HEADER
#define LLSC_BRANCH_HEADER

// TODO: should be able to generate these functions too

#ifdef PURE_STATE

inline std::monostate async_exec_block(uint64_t ssid, const std::function<std::monostate()>& f) {
  if (can_par_tp()) {
    tp.add_task(ssid, f);
    return std::monostate{};
  }
  return f();
}

inline immer::flex_vector<std::pair<SS, PtrVal>>
sym_exec_br(SS ss, unsigned int block_id, PtrVal t_cond, PtrVal f_cond,
            immer::flex_vector<std::pair<SS, PtrVal>> (*tf)(SS),
            immer::flex_vector<std::pair<SS, PtrVal>> (*ff)(SS)) {
  auto pc = ss.get_PC();
  auto tbr_sat = check_pc(pc.add(t_cond));
  auto fbr_sat = check_pc(pc.add(f_cond));
  if (tbr_sat && fbr_sat) {
    cov().inc_path(1);
    SS tbr_ss = ss.add_PC(t_cond);
    SS fbr_ss = ss.fork().add_PC(f_cond);
    if (can_par_async()) {
      std::future<immer::flex_vector<std::pair<SS, PtrVal>>> tf_res =
        create_async<immer::flex_vector<std::pair<SS, PtrVal>>>([&]{
          cov().inc_branch(block_id, 0);
          return tf(tbr_ss);
        });
      cov().inc_branch(block_id, 1);
      auto ff_res = ff(fbr_ss);
      return tf_res.get() + ff_res;
    } else {
      cov().inc_branch(block_id, 0);
      cov().inc_branch(block_id, 1);
      return tf(tbr_ss) + ff(fbr_ss);
    }
  } else if (tbr_sat) {
    cov().inc_branch(block_id, 0);
    SS tbr_ss = ss.add_PC(t_cond);
    return tf(tbr_ss);
  } else if (fbr_sat) {
    cov().inc_branch(block_id, 1);
    SS fbr_ss = ss.add_PC(f_cond);
    return ff(fbr_ss);
  } else {
    return immer::flex_vector<std::pair<SS, PtrVal>>{};
  }
}

inline std::monostate
sym_exec_br_k(SS ss, unsigned int block_id, PtrVal t_cond, PtrVal f_cond,
              std::function<std::monostate(SS, std::function<std::monostate(SS, PtrVal)>)> tf,
              std::function<std::monostate(SS, std::function<std::monostate(SS, PtrVal)>)> ff,
              std::function<std::monostate(SS, PtrVal)> k) {
  auto pc = ss.get_PC();
  auto tbr_sat = check_pc(pc.add(t_cond));
  auto fbr_sat = check_pc(pc.add(f_cond));
  if (tbr_sat && fbr_sat) {
    cov().inc_path(1);
    SS tbr_ss = ss.add_PC(t_cond);
    SS fbr_ss = ss.fork().add_PC(f_cond);
    if (can_par_tp()) {
      tp.add_task(tbr_ss.get_ssid(), [tf, block_id, tbr_ss=std::move(tbr_ss), k]{
        cov().inc_branch(block_id, 0);
        return tf(tbr_ss, k);
      });
      tp.add_task(fbr_ss.get_ssid(), [ff, block_id, fbr_ss=std::move(fbr_ss), k]{
        cov().inc_branch(block_id, 1);
        return ff(fbr_ss, k);
      });
      return std::monostate{};
    } else {
      cov().inc_branch(block_id, 0);
      tf(tbr_ss, k);
      cov().inc_branch(block_id, 1);
      ff(fbr_ss, k);
      return std::monostate{};
    }
  } else if (tbr_sat) {
    cov().inc_branch(block_id, 0);
    SS tbr_ss = ss.add_PC(t_cond);
    return tf(tbr_ss, k);
  } else if (fbr_sat) {
    cov().inc_branch(block_id, 1);
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
    auto pc2 = ss.get_PC().add(low_cond).add(high_cond);
    auto res = get_sat_value(pc2, offsym);
    while (res.first) {
      cnt++;
      int offset_val = res.second;
      auto t_cond = int_op_2(iOP::op_eq, offsym, make_IntV(offset_val, offsym->get_bw()));
      if (1 == cnt) {
        result.push_back(std::make_pair(ss.add_PC(t_cond), baseloc + (offset_val*esize)));
      } else {
        result.push_back(std::make_pair(ss.fork().add_PC(t_cond), baseloc + (offset_val*esize)));
      }
      pc2 = pc2.add(SymV::neg(t_cond));
      res = get_sat_value(pc2, offsym);
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
    auto pc2 = ss.get_PC().add(low_cond).add(high_cond);
    auto res = get_sat_value(pc2, offsym);
    while (res.first) {
      cnt++;
      int offset_val = res.second;
      auto t_cond = int_op_2(iOP::op_eq, offsym, make_IntV(offset_val, offsym->get_bw()));
      auto new_loc = baseloc + (offset_val*esize);
      auto new_ss = (1 == cnt) ? ss.add_PC(t_cond) : ss.fork().add_PC(t_cond);
      if (can_par_tp()) {
        tp.add_task(new_ss.get_ssid(), [new_loc=std::move(new_loc), new_ss=std::move(new_ss), k]{ return k(new_ss, new_loc); });
      } else {
        k(new_ss, new_loc);
      }
      pc2 = pc2.add(SymV::neg(t_cond));
      res = get_sat_value(pc2, offsym);
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
    tp.add_task(ss.get_ssid(), [f, ss=std::move(ss), k]{ return f((SS&)ss, k); });
    return std::monostate{};
  }
  return f(ss, k);
}

// use immer::flex_vector as argument list and result list
inline immer::flex_vector<std::pair<SS, PtrVal>>
sym_exec_br(SS& ss, unsigned int block_id, PtrVal t_cond, PtrVal f_cond,
            immer::flex_vector<std::pair<SS, PtrVal>> (*tf)(SS&),
            immer::flex_vector<std::pair<SS, PtrVal>> (*ff)(SS&)) {
  auto pc = ss.get_PC();
  pc.add(t_cond);
  auto tbr_sat = check_pc(pc);
  pc.replace_last_cond(f_cond);
  auto fbr_sat = check_pc(pc);
  if (tbr_sat && fbr_sat) {
    cov().inc_path(1);
    SS tbr_ss = ss;
    SS fbr_ss = ss.fork();
    tbr_ss.add_PC(t_cond);
    fbr_ss.add_PC(f_cond);
    if (can_par_async()) {
      std::future<immer::flex_vector<std::pair<SS, PtrVal>>> tf_res =
        create_async<immer::flex_vector<std::pair<SS, PtrVal>>>([&]{
          cov().inc_branch(block_id, 0);
          return tf(tbr_ss);
        });
      cov().inc_branch(block_id, 1);
      auto ff_res = ff(fbr_ss);
      return tf_res.get() + ff_res;
    } else {
      cov().inc_branch(block_id, 0);
      cov().inc_branch(block_id, 1);
      return tf(tbr_ss) + ff(fbr_ss);
    }
  } else if (tbr_sat) {
    cov().inc_branch(block_id, 0);
    SS tbr_ss = ss.add_PC(t_cond);
    return tf(tbr_ss);
  } else if (fbr_sat) {
    cov().inc_branch(block_id, 1);
    SS fbr_ss = ss.add_PC(f_cond);
    return ff(fbr_ss);
  } else {
    return immer::flex_vector<std::pair<SS, PtrVal>>{};
  }
}

// use std::vector as argument list and result list
[[deprecated]]
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
    SS tbr_ss = ss;
    SS fbr_ss = ss.fork();
    tbr_ss.add_PC(t_cond);
    fbr_ss.add_PC(f_cond);
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
sym_exec_br_k(SS& ss, unsigned int block_id, PtrVal t_cond, PtrVal f_cond,
              std::function<std::monostate(SS&, std::function<std::monostate(SS&, PtrVal)>)> tf,
              std::function<std::monostate(SS&, std::function<std::monostate(SS&, PtrVal)>)> ff,
              std::function<std::monostate(SS&, PtrVal)> k) {
  auto pc = ss.get_PC();
  pc.add(t_cond);
  auto tbr_sat = check_pc(pc);
  pc.replace_last_cond(f_cond);
  auto fbr_sat = check_pc(pc);

  if (tbr_sat && fbr_sat) {
    cov().inc_path(1);
    SS tbr_ss = ss;
    SS fbr_ss = ss.fork();
    tbr_ss.add_PC(t_cond);
    fbr_ss.add_PC(f_cond);
    if (can_par_tp()) {
      tp.add_task(tbr_ss.get_ssid(), [tf, block_id, tbr_ss=std::move(tbr_ss), k]{
        cov().inc_branch(block_id, 0);
        return tf((SS&)tbr_ss, k);
      });
      tp.add_task(fbr_ss.get_ssid(), [ff, block_id, fbr_ss=std::move(fbr_ss), k]{
        cov().inc_branch(block_id, 1);
        return ff((SS&)fbr_ss, k);
      });
      return std::monostate{};
    } else {
      cov().inc_branch(block_id, 0);
      tf(tbr_ss, k);
      cov().inc_branch(block_id, 1);
      ff(fbr_ss, k);
      return std::monostate{};
    }
  } else if (tbr_sat) {
    cov().inc_branch(block_id, 0);
    SS tbr_ss = ss.add_PC(t_cond);
    return tf(tbr_ss, k);
  } else if (fbr_sat) {
    cov().inc_branch(block_id, 1);
    SS fbr_ss = ss.add_PC(f_cond);
    return ff(fbr_ss, k);
  } else {
    return std::monostate{};
  }
}

// Note: seems a way to reduce the size of generated code,
//       but we need to make sure those block-functions will not be DCE-ed.
inline std::monostate
br_k(SS& ss, PtrVal t_cond, PtrVal f_cond,
     std::monostate (*tf)(SS&, std::function<std::monostate(SS&, PtrVal)>),
     std::monostate (*ff)(SS&, std::function<std::monostate(SS&, PtrVal)>),
     std::function<std::monostate(SS&, PtrVal)> k) {
  if (t_cond->is_conc()) {
    if (proj_IntV(t_cond) == 1) return tf(ss, k);
    else return ff(ss, k);
  }
  // FIXME: pass correct current block id
  return sym_exec_br_k(ss, 0, t_cond, f_cond, tf, ff, k);
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
    auto pc2 = ss.copy_PC().add(low_cond).add(high_cond);
    auto res = get_sat_value(pc2, offsym);
    while (res.first) {
      cnt++;
      int offset_val = res.second;
      auto t_cond = int_op_2(iOP::op_eq, offsym, make_IntV(offset_val, offsym->get_bw()));
      if (1 == cnt) {
        result.push_back(std::make_pair(std::move(ss.add_PC(t_cond)), baseloc + (offset_val*esize)));
      } else {
        result.push_back(std::make_pair(std::move(ss.fork().add_PC(t_cond)), baseloc + (offset_val*esize)));
      }
      pc2.add(SymV::neg(t_cond));
      res = get_sat_value(pc2, offsym);
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
    auto pc2 = ss.copy_PC().add(low_cond).add(high_cond);
    auto res = get_sat_value(pc2, offsym);
    while (res.first) {
      cnt++;
      int offset_val = res.second;
      auto t_cond = int_op_2(iOP::op_eq, offsym, make_IntV(offset_val, offsym->get_bw()));
      auto new_loc = baseloc + (offset_val*esize);
      auto new_ss = (1 == cnt) ? ss.add_PC(t_cond) : ss.fork().add_PC(t_cond);
      if (can_par_tp()) {
        tp.add_task(new_ss.get_ssid(), [new_loc=std::move(new_loc), new_ss=std::move(new_ss), k]{ return k((SS&)new_ss, new_loc); });
      } else {
        k(new_ss, new_loc);
      }
      pc2.add(SymV::neg(t_cond));
      res = get_sat_value(pc2, offsym);
    }
    ASSERT(cnt > 0, "No satisfiable offset value");
    cov().inc_path(cnt - 1);
  } else ABORT("Error: unknown array offset kind.");
  return std::monostate{};
}
#endif

#endif
