std::monostate app_main(SS&, immer::flex_vector<PtrVal>, std::function<std::monostate(SS&, PtrVal)>);
std::monostate llsc_main(SS&, immer::flex_vector<PtrVal>, std::function<std::monostate(SS&, PtrVal)>);
inline std::monostate llsc_dummy(SS&, immer::flex_vector<PtrVal>, std::function<std::monostate(SS&, PtrVal)>) {
    return std::monostate{};
}
inline std::monostate start_llsc_main(SS& state, immer::flex_vector<PtrVal> args, std::function<std::monostate(SS&, PtrVal)> cont) {
  if (can_par_tp()) {
    tp.add_task(1, [=] () mutable { return llsc_main(state, args, cont); });
    return std::monostate{};
  }
  return llsc_main(state, args, cont);
}