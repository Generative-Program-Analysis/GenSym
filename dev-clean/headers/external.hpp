#include <llsc.hpp>

/* temp util functions */
inline immer::flex_vector<Expr> set_to_list(immer::set<Expr> s) {
  auto res = immer::flex_vector<Expr>{};
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
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state.heap_append(emptyMem), memLoc}};
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

inline void handle_pc(immer::set<Expr> pc) {

}

inline immer::flex_vector<std::pair<SS, PtrVal>> llsc_assert(SS state, immer::flex_vector<PtrVal> args) {
  immer::set<Expr> pc = state.getPC();
  handle_pc(pc);
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state, make_IntV(0)}};
}