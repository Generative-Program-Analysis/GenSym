#include <conc_global_solver.hpp>

inline std::pair<SS, PtrVal> mark_symbolic(SS state, immer::flex_vector<PtrVal> args, immer::flex_vector<PtrVal> sargs) {
  PtrVal make_loc = args.at(0);
  IntData len = proj_IntV(args.at(1));
  IntData step = proj_IntV(args.at(2));
  SS res = state;
  //std::cout << "sym array size: " << proj_LocV_size(make_loc) << "\n";
  for (int i = 0; i < len; i+=step) {
    res = res.supdate(make_LocV_inc(make_loc, i), make_SymV("x" + std::to_string(var_name++)));
  }
  return std::pair<SS, PtrVal>{res, make_PairV(make_IntV(0), make_IntV(0))};
}

inline std::pair<SS, PtrVal> llvm_memset(SS state, immer::flex_vector<PtrVal> args, immer::flex_vector<PtrVal> sargs) {
  PtrVal dest = args.at(0);
  PtrVal seti8 = args.at(1);
  PtrVal bytes = args.at(2);

  SS res = state;
  Addr dest_addr = proj_LocV(dest);
  // what could be other set value?
  int setInt = 0;
  IntData bytes_int = proj_IntV(bytes);
  auto IntV0 = make_IntV(0);
  
  // Optmize
  // flex_vector_transient
  for (int i = 0; i < bytes_int; i++) {
    res = res.update(make_LocV_inc(dest, i), IntV0);
  }
  return std::pair<SS, PtrVal>{res, make_PairV(IntV0, IntV0)};
}