#include <llsc.hpp>

/* temp util functions */
inline immer::flex_vector<SExpr> set_to_list(immer::set<SExpr> s) {
  auto res = immer::flex_vector<SExpr>{};
  for (auto x : s) {
    res = res.push_back(x);
  }
  return res;
}

inline immer::flex_vector<std::pair<SS, PtrVal>> sym_print(SS state, immer::flex_vector<PtrVal> args) {
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
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state, make_IntV(0)}};
}

inline immer::flex_vector<std::pair<SS, PtrVal>> noop(SS state, immer::flex_vector<PtrVal> args) {
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state, make_IntV(0)}};
}

inline immer::flex_vector<std::pair<SS, PtrVal>> malloc(SS state, immer::flex_vector<PtrVal> args) {
  IntData bytes = proj_IntV(args.at(0));
  auto emptyMem = immer::flex_vector<PtrVal>(bytes, make_IntV(0));
  PtrVal memLoc = make_LocV(state.heap_size(), LocV::kHeap, bytes);
  if (exlib_failure_branch) {
    // simulating the failed branch
    PtrVal nullLoc = make_LocV_null();
    return immer::flex_vector<std::pair<SS, PtrVal>>{{state.heap_append(emptyMem), memLoc}, {state, nullLoc}};
  } else {
    return immer::flex_vector<std::pair<SS, PtrVal>>{{state.heap_append(emptyMem), memLoc}};
  }
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

inline immer::flex_vector<std::pair<SS, PtrVal>> open(SS state, immer::flex_vector<PtrVal> args) {
  PtrVal ptr = args.at(0);
  std::string name = get_string(ptr, state);
  FS& fs = state.get_fs();
  /* TODO: add flags for open_file <2021-11-03, David Deng> */
  Fd fd = fs.open_file(name, 0);
  state.set_fs(fs);
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state, make_IntV(fd)}};
}

inline immer::flex_vector<std::pair<SS, PtrVal>> close(SS state, immer::flex_vector<PtrVal> args) {
  Fd fd = proj_IntV(args.at(0));
  FS& fs = state.get_fs();
  int status = fs.close_file(fd);
  state.set_fs(fs);
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state, make_IntV(status)}};
}

/* inline immer::flex_vector<std::pair<SS, PtrVal>> lseek(SS state, immer::flex_vector<PtrVal> args) { */

/* } */

inline void handle_pc(immer::set<SExpr> pc) {

}

inline immer::flex_vector<std::pair<SS, PtrVal>> llsc_assert(SS state, immer::flex_vector<PtrVal> args) {
  // XXX(GW): temporarily commented, should invoke Checker and generate test case properly?
  //immer::set<SExpr> pc = state.getPC();
  //handle_pc(pc);
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state, make_IntV(0)}};
}

inline immer::flex_vector<std::pair<SS, PtrVal>> make_symbolic(SS state, immer::flex_vector<PtrVal> args) {
  PtrVal make_loc = args.at(0);
  IntData len = proj_IntV(args.at(1));
  SS res = state;
  //std::cout << "sym array size: " << proj_LocV_size(make_loc) << "\n";
  for (int i = 0; i < len; i++) {
    res = res.update(make_LocV_inc(make_loc, i), make_SymV("x" + std::to_string(var_name++)));
  }
  return immer::flex_vector<std::pair<SS, PtrVal>>{{res, make_IntV(0)}};
}

inline immer::flex_vector<std::pair<SS, PtrVal>> __assert_fail(SS state, immer::flex_vector<PtrVal> args) {
  // TODO get real argument string
  // std::cout << "Fail: Calling to __assert_fail" << std::endl;
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state, make_IntV(0)}};
}
