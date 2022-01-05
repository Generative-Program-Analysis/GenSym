#ifndef LLSC_EXTERNAL_IMP_HEADERS
#define LLSC_EXTERNAL_IMP_HEADERS

inline immer::flex_vector<std::pair<SS, PtrVal>> sym_print(SS state, immer::flex_vector<PtrVal> args) {
  for (auto x : args) { std::cout << *x << "; "; }
  std::cout << "\n";
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state, make_IntV(0)}};
}

inline std::monostate sym_print(SS& state, immer::flex_vector<PtrVal> args, std::function<std::monostate(SS&, PtrVal)> k) {
  for (auto x : args) { std::cout << *x << "; "; }
  std::cout << "\n";
  return k(state, make_IntV(0));
}

inline immer::flex_vector<std::pair<SS, PtrVal>> noop(SS state, immer::flex_vector<PtrVal> args) {
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state, make_IntV(0)}};
}

inline immer::flex_vector<std::pair<SS, PtrVal>> malloc(SS state, immer::flex_vector<PtrVal> args) {
  IntData bytes = proj_IntV(args.at(0));
  auto emptyMem = immer::flex_vector<PtrVal>(bytes, make_IntV(0));
  PtrVal memLoc = make_LocV(state.heap_size(), LocV::kHeap, bytes);
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state.heap_append(emptyMem), memLoc}};
}

inline std::monostate malloc(SS& state, immer::flex_vector<PtrVal> args, std::function<std::monostate(SS&, PtrVal)> k) {
  IntData bytes = proj_IntV(args.at(0));
  auto emptyMem = immer::flex_vector<PtrVal>(bytes, make_IntV(0));
  PtrVal memLoc = make_LocV(state.heap_size(), LocV::kHeap, bytes);
  SS ss = state.heap_append(emptyMem);
  return k(ss, memLoc);
}

inline immer::flex_vector<std::pair<SS, PtrVal>> realloc(SS state, immer::flex_vector<PtrVal> args) {
  Addr src = proj_LocV(args.at(0));
  IntData bytes = proj_IntV(args.at(1));

  auto emptyMem = immer::flex_vector<PtrVal>(bytes, make_IntV(0));
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

inline void handle_pc(const std::set<PtrVal>& pc) {

}

inline immer::flex_vector<std::pair<SS, PtrVal>> llsc_assert(SS state, immer::flex_vector<PtrVal> args) {
  // XXX(GW): temporarily commented, should invoke Checker and generate test case properly?
  // auto &pc = state.getPC();
  // handle_pc(pc);
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state, make_IntV(0)}};
}

inline std::monostate make_symbolic(SS& state, immer::flex_vector<PtrVal> args, std::function<std::monostate(SS&, PtrVal)> k) {
  PtrVal make_loc = args.at(0);
  IntData len = proj_IntV(args.at(1));
  SS res = std::move(state);
  //std::cout << "sym array size: " << proj_LocV_size(make_loc) << "\n";
  for (int i = 0; i < len; i++) {
    res.update(make_LocV_inc(make_loc, i), make_SymV("x" + std::to_string(var_name++)));
  }
  return k(res, make_IntV(0));
}

inline immer::flex_vector<std::pair<SS, PtrVal>> make_symbolic(SS& state, immer::flex_vector<PtrVal> args) {
  PtrVal make_loc = args.at(0);
  IntData len = proj_IntV(args.at(1));
  SS res = std::move(state);
  //std::cout << "sym array size: " << proj_LocV_size(make_loc) << "\n";
  for (int i = 0; i < len; i++) {
    res.update(make_LocV_inc(make_loc, i), make_SymV("x" + std::to_string(var_name++)));
  }
  return immer::flex_vector<std::pair<SS, PtrVal>>{{std::move(res), make_IntV(0)}};
}

inline immer::flex_vector<std::pair<SS, PtrVal>> __assert_fail(SS state, immer::flex_vector<PtrVal> args) {
  // TODO get real argument string
  // std::cout << "Fail: Calling to __assert_fail" << std::endl;
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state, make_IntV(0)}};
}

inline std::monostate __assert_fail(SS state, immer::flex_vector<PtrVal> args, std::function<std::monostate(SS&, PtrVal)> k) {
  // TODO get real argument string
  // std::cout << "Fail: Calling to __assert_fail" << std::endl;
  return k(state, make_IntV(0));
}

// Belows are the counterparts that use std::vector for argument list and return list
// TODO: refactor them using tempaltes?

inline std::vector<std::pair<SS, PtrVal>> sym_print(SS state, std::vector<PtrVal> args) {
  PtrVal x = args.at(0);
  if (std::dynamic_pointer_cast<FloatV>(x)) {
    std::cout << "FloatV" << std::dynamic_pointer_cast<FloatV>(x)->f << ")\n";
  } else if (std::dynamic_pointer_cast<IntV>(x)) {
    std::cout << "IntV(" << std::dynamic_pointer_cast<IntV>(x)->i << ")\n";
  } else if (std::dynamic_pointer_cast<LocV>(x)){
    ABORT("Unimplemented LOCV");
  } else if ( x == nullptr ){
    ABORT("Unimplemented nullptr");
  }
  return std::vector<std::pair<SS, PtrVal>>{{state, make_IntV(0)}};
}

inline std::vector<std::pair<SS, PtrVal>> noop(SS state, std::vector<PtrVal> args) {
  return std::vector<std::pair<SS, PtrVal>>{{state, make_IntV(0)}};
}

// TODO: sync with the malloc in external_pure
inline std::monostate malloc(SS& state, std::vector<PtrVal> args, std::function<std::monostate(SS&, PtrVal)> k) {
  IntData bytes = proj_IntV(args.at(0));
  auto emptyMem = std::vector<PtrVal>(bytes, make_IntV(0));
  PtrVal memLoc = make_LocV(state.heap_size(), LocV::kHeap, bytes);
  SS ss = state.heap_append(emptyMem);
  return k(ss, memLoc);
}

inline std::vector<std::pair<SS, PtrVal>> malloc(SS state, std::vector<PtrVal> args) {
  IntData bytes = proj_IntV(args.at(0));
  auto emptyMem = std::vector<PtrVal>(bytes, make_IntV(0));
  PtrVal memLoc = make_LocV(state.heap_size(), LocV::kHeap, bytes);
  return std::vector<std::pair<SS, PtrVal>>{{state.heap_append(emptyMem), memLoc}};
}

inline std::vector<std::pair<SS, PtrVal>> realloc(SS state, std::vector<PtrVal> args) {
  Addr src = proj_LocV(args.at(0));
  IntData bytes = proj_IntV(args.at(1));

  auto emptyMem = std::vector<PtrVal>(bytes, make_IntV(0));
  std::cout << "realloc size: " << emptyMem.size() << std::endl;
  PtrVal memLoc = make_LocV(state.heap_size(), LocV::kHeap, bytes);
  IntData prevBytes = proj_LocV_size(args.at(0));
  std::cout << "prev size: " << prevBytes << std::endl;
  SS res = state.heap_append(emptyMem);
  for (int i = 0; i < prevBytes; i++) {
    res = res.update(make_LocV_inc(memLoc, i), res.heap_lookup(src + i));
  }
  return std::vector<std::pair<SS, PtrVal>>{{res, memLoc}};
}

inline std::vector<std::pair<SS, PtrVal>> llsc_assert(SS state, std::vector<PtrVal> args) {
  // XXX(GW): temporarily commented, should invoke Checker and generate test case properly?
  // auto &pc = state.getPC();
  // handle_pc(pc);
  return std::vector<std::pair<SS, PtrVal>>{{state, make_IntV(0)}};
}

inline std::monostate make_symbolic(SS& state, std::vector<PtrVal> args, std::function<std::monostate(SS&, PtrVal)> k) {
  PtrVal make_loc = args.at(0);
  IntData len = proj_IntV(args.at(1));
  SS res = std::move(state);
  //std::cout << "sym array size: " << proj_LocV_size(make_loc) << "\n";
  for (int i = 0; i < len; i++) {
    res.update(make_LocV_inc(make_loc, i), make_SymV("x" + std::to_string(var_name++)));
  }
  return k(res, make_IntV(0));
}

inline std::vector<std::pair<SS, PtrVal>> make_symbolic(SS& state, std::vector<PtrVal> args) {
  PtrVal make_loc = args.at(0);
  IntData len = proj_IntV(args.at(1));
  SS res = std::move(state);
  //std::cout << "sym array size: " << proj_LocV_size(make_loc) << "\n";
  for (int i = 0; i < len; i++) {
    res.update(make_LocV_inc(make_loc, i), make_SymV("x" + std::to_string(var_name++)));
  }
  return std::vector<std::pair<SS, PtrVal>>{{std::move(res), make_IntV(0)}};
}

inline std::vector<std::pair<SS, PtrVal>> __assert_fail(SS state, std::vector<PtrVal> args) {
  // TODO get real argument string
  // std::cout << "Fail: Calling to __assert_fail" << std::endl;
  return std::vector<std::pair<SS, PtrVal>>{{state, make_IntV(0)}};
}

#endif
