#ifndef LLSC_EXTERNAL_PURE_HEADERS
#define LLSC_EXTERNAL_PURE_HEADERS

using Cont = std::function<std::monostate(SS, PtrVal)>;

inline std::string get_string(PtrVal ptr, SS state) {
  std::string name;
  char c = proj_IntV_char(state.at(ptr)); // c = *ptr
  while (c != '\0') {
    name += c;
    ptr = make_LocV_inc(ptr, 1); // ptr++
    c = proj_IntV_char(state.at(ptr)); // c = *ptr
  }
  return name;
}

inline immer::flex_vector<std::pair<SS, PtrVal>> sym_print(SS state, immer::flex_vector<PtrVal> args) {
  for (auto x : args) {
    std::cout << ptrval_to_string(x) << "; " << std::endl;
  }
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state, make_IntV(0)}};
}

inline std::monostate sym_print(SS state, immer::flex_vector<PtrVal> args, Cont k) {
  for (auto x : args) {
    if (x == nullptr) {
      std::cout << "nullptr";
    } else {
      std::cout << *x;
    }
    std::cout << "; " << std::endl;
  }
  return k(state, make_IntV(0));
}

inline immer::flex_vector<std::pair<SS, PtrVal>> print_string(SS state, immer::flex_vector<PtrVal> args) {
  PtrVal x = args.at(0);
  if (std::dynamic_pointer_cast<LocV>(x)){
    std::cout << get_string(x, state) << std::endl;
  } else {
    ABORT("Cannot print non-LocV value as string");
  }
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state, make_IntV(0)}};
}

inline std::monostate print_string(SS state, immer::flex_vector<PtrVal> args, Cont k) {
  PtrVal x = args.at(0);
  if (std::dynamic_pointer_cast<LocV>(x)){
    std::cout << get_string(x, state) << std::endl;
  } else {
    ABORT("Cannot print non-LocV value as string");
  }
  return k(state, make_IntV(0));
}

inline immer::flex_vector<std::pair<SS, PtrVal>> noop(SS state, immer::flex_vector<PtrVal> args) {
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state, make_IntV(0)}};
}

inline immer::flex_vector<std::pair<SS, PtrVal>> stop(SS state, immer::flex_vector<PtrVal> args) {
  check_pc_to_file(state);
  return immer::flex_vector<std::pair<SS, PtrVal>>{};
}

inline std::monostate stop(SS state, immer::flex_vector<PtrVal> args, Cont k) {
  check_pc_to_file(state);
  return std::monostate();
}

inline immer::flex_vector<std::pair<SS, PtrVal>> malloc(SS state, immer::flex_vector<PtrVal> args) {
  IntData bytes = proj_IntV(args.at(0));
  auto emptyMem = immer::flex_vector<PtrVal>(bytes, nullptr);
  PtrVal memLoc = make_LocV(state.heap_size(), LocV::kHeap, bytes);
  if (exlib_failure_branch) {
    // simulating the failed branch
    PtrVal nullLoc = make_LocV_null();
    return immer::flex_vector<std::pair<SS, PtrVal>>{{state.heap_append(emptyMem), memLoc}, {state, nullLoc}};
  } else {
    return immer::flex_vector<std::pair<SS, PtrVal>>{{state.heap_append(emptyMem), memLoc}};
  }
}

inline std::monostate malloc(SS state, immer::flex_vector<PtrVal> args, Cont k) {
  IntData bytes = proj_IntV(args.at(0));
  auto emptyMem = immer::flex_vector<PtrVal>(bytes, nullptr);
  PtrVal memLoc = make_LocV(state.heap_size(), LocV::kHeap, bytes);
  if (exlib_failure_branch) {
    // simulating the failed branch
    PtrVal nullLoc = make_LocV_null();
    k(state.heap_append(emptyMem), memLoc);
    return k(state, nullLoc);
  } else {
    return k(state.heap_append(emptyMem), memLoc);
  }
}


inline immer::flex_vector<std::pair<SS, PtrVal>> realloc(SS state, immer::flex_vector<PtrVal> args) {
  Addr src = proj_LocV(args.at(0));
  IntData bytes = proj_IntV(args.at(1));

  auto emptyMem = immer::flex_vector<PtrVal>(bytes, nullptr);
  std::cout << "realloc size: " << emptyMem.size() << std::endl;
  PtrVal memLoc = make_LocV(state.heap_size(), LocV::kHeap, bytes);
  IntData prevBytes = proj_LocV_size(args.at(0));
  std::cout << "prev size: " << prevBytes << std::endl;
  SS res = state.heap_append(emptyMem);
  for (int i = 0; i < prevBytes; i++) {
    res = res.update(make_LocV_inc(memLoc, i), res.heap_lookup(src + i));
  }
  return immer::flex_vector<std::pair<SS, PtrVal>>{{res, memLoc}};
}

inline immer::flex_vector<std::pair<SS, PtrVal>> sym_exit(SS state, immer::flex_vector<PtrVal> args) {
  ASSERT(args.size() == 1, "sym_exit accepts exactly one argument");
  auto v = args.at(0)->to_IntV();
  ASSERT(v != nullptr, "sym_exit only accepts integer argument");
  int status = v->as_signed();
  check_pc_to_file(state);
#ifdef USE_TP
  tp.stop_all_tasks();
  set_exit_code(status);
  return immer::flex_vector<std::pair<SS, PtrVal>>{};
#else
  cov.print_all();
  _exit(status);
#endif
}

/* TODO: Generate both versions of sym_exit <2022-01-24, David Deng> */
inline std::monostate sym_exit(SS state, immer::flex_vector<PtrVal> args, Cont k) {
  ASSERT(args.size() == 1, "sym_exit accepts exactly one argument");
  auto v = args.at(0)->to_IntV();
  ASSERT(v != nullptr, "sym_exit only accepts integer argument");
  int status = v->as_signed();
  check_pc_to_file(state);
#ifdef USE_TP
  // XXX: brutally call _exit? then what should happen if two threads are calling sym_exit?
  tp.stop_all_tasks();
  set_exit_code(status);
  return std::monostate{};
#else
  cov.print_all();
  _exit(status);
#endif
}

inline immer::flex_vector<std::pair<SS, PtrVal>> llsc_assert(SS state, immer::flex_vector<PtrVal> args) {
  auto v = args.at(0);
  auto i = v->to_IntV();
  if (i) {
    if (i->i == 0) return stop(state, args); // concrete false - generate the test and exit
    return immer::flex_vector<std::pair<SS, PtrVal>>{{state, make_IntV(1, 32)}};
  }
  // otherwise add a symbolic condition that constraints it to be true
  // undefined/error if v is a value of other types
  auto cond = to_SMTNeg(v);
  auto new_s = state.add_PC(cond);
  if (check_pc(new_s.get_PC())) return stop(new_s, args); // check if v == 1 is not valid
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state.add_PC(v), make_IntV(1, 32)}};
}

inline std::monostate llsc_assert(SS state, immer::flex_vector<PtrVal> args, Cont k) {
  auto v = args.at(0);
  auto i = v->to_IntV();
  if (i) {
    if (i->i == 0) return stop(state, args, k); // concrete false - generate the test and exit
    return k(state, make_IntV(1, 32));
  }
  // otherwise add a symbolic condition that constraints it to be true
  // undefined/error if v is a value of other types
  auto cond = to_SMTNeg(v);
  auto new_s = state.add_PC(cond);
  if (check_pc(new_s.get_PC())) return stop(new_s, args, k); // check if v == 1 is not valid
  return k(state.add_PC(v), make_IntV(1, 32));
}

inline immer::flex_vector<std::pair<SS, PtrVal>> llsc_assert_eager(SS state, immer::flex_vector<PtrVal> args) {
  auto v = args.at(0);
  auto i = v->to_IntV();
  if (i) {
    if (i->i == 0) sym_exit(state, { make_IntV(-1) }); // concrete false - generate the test and exit
    return immer::flex_vector<std::pair<SS, PtrVal>>{{state, make_IntV(1, 32)}};
  }
  // otherwise add a symbolic condition that constraints it to be true
  // undefined/error if v is a value of other types
  auto cond = to_SMTNeg(v);
  auto new_s = state.add_PC(cond);
  if (check_pc(new_s.get_PC())) sym_exit(new_s, { make_IntV(-1) }); // check if v == 1 is not valid
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state.add_PC(v), make_IntV(1, 32)}};
}

inline std::monostate llsc_assert_eager(SS state, immer::flex_vector<PtrVal> args, Cont k) {
  auto v = args.at(0);
  auto i = v->to_IntV();
  if (i) {
    if (i->i == 0) sym_exit(state, { make_IntV(-1) }); // concrete false - generate the test and exit
    return k(state, make_IntV(1, 32));
  }
  // otherwise add a symbolic condition that constraints it to be true
  // undefined/error if v is a value of other types
  auto cond = to_SMTNeg(v);
  auto new_s = state.add_PC(cond);
  if (check_pc(new_s.get_PC())) sym_exit(new_s, { make_IntV(-1) }); // check if v == 1 is not valid
  return k(state.add_PC(v), make_IntV(1, 32));
}

inline immer::flex_vector<std::pair<SS, PtrVal>> make_symbolic(SS state, immer::flex_vector<PtrVal> args) {
  PtrVal make_loc = args.at(0);
  IntData len = proj_IntV(args.at(1));
  SS res = state;
  //std::cout << "sym array size: " << proj_LocV_size(make_loc) << "\n";
  for (int i = 0; i < len; i++) {
    res = res.update(make_LocV_inc(make_loc, i), make_SymV("x" + std::to_string(var_name++), 8));
  }
  return immer::flex_vector<std::pair<SS, PtrVal>>{{res, make_IntV(0)}};
}

inline std::monostate make_symbolic(SS state, immer::flex_vector<PtrVal> args, Cont k) {
  PtrVal make_loc = args.at(0);
  IntData len = proj_IntV(args.at(1));
  SS res = state;
  //std::cout << "sym array size: " << proj_LocV_size(make_loc) << "\n";
  for (int i = 0; i < len; i++) {
    res = res.update(make_LocV_inc(make_loc, i), make_SymV("x" + std::to_string(var_name++), 8));
  }
  return k(res, make_IntV(0));
}

inline immer::flex_vector<std::pair<SS, PtrVal>> __assert_fail(SS state, immer::flex_vector<PtrVal> args) {
  // TODO get real argument string
  // std::cout << "Fail: Calling to __assert_fail" << std::endl;
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state, make_IntV(0)}};
}

inline std::monostate __assert_fail(SS state, immer::flex_vector<PtrVal> args, Cont k) {
  // TODO get real argument string
  // std::cout << "Fail: Calling to __assert_fail" << std::endl;
  return k(state, make_IntV(0));
}

#endif
