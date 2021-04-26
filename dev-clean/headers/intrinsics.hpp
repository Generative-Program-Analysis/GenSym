#include <llsc.hpp>

inline immer::flex_vector<std::pair<SS, PtrVal>> llvm_memcpy(SS state, immer::flex_vector<PtrVal> args) {
  PtrVal dest = args.at(0);
  PtrVal src = args.at(1);
  PtrVal bytes = args.at(2);

  SS res = state;
  Addr dest_addr = proj_LocV(dest);
  Addr src_addr = proj_LocV(src);
  int bytes_int = proj_IntV(bytes);
  
  // Optmize
  // flex_vector_transient
  for (int i = 0; i < bytes_int; i++) {
    res = res.update(make_LocV_inc(dest, i), res.at(make_LocV_inc(src, i)));
  }
  return immer::flex_vector<std::pair<SS, PtrVal>>{{res, make_IntV(0)}};
}

// args 0: LocV to {i32, i32, i8*, i8*}
// in memory {4, 4, 8, 8}
inline immer::flex_vector<std::pair<SS, PtrVal>> llvm_va_start(SS state, immer::flex_vector<PtrVal> args) {
  PtrVal va_list = args.at(0);
  PtrVal va_arg = state.getVarargLoc();
  SS res = state;

  res = res.update(make_LocV_inc(va_list, 0), make_IntV(0));
  res = res.update(make_LocV_inc(va_list, 4), make_IntV(0));
  res = res.update(make_LocV_inc(va_list, 8), make_LocV_inc(va_arg, 40));
  res = res.update(make_LocV_inc(va_list, 16), va_arg);

  return immer::flex_vector<std::pair<SS, PtrVal>>{{res, make_IntV(0)}};
}