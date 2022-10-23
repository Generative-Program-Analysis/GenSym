// prepare necessary declarations and definitions for library mode compilation
#include <gensym.hpp>
std::monostate app_main(SS&, immer::flex_vector<PtrVal>, std::function<std::monostate(SS&, PtrVal)>);
std::monostate gs_main(SS&, immer::flex_vector<PtrVal>, std::function<std::monostate(SS&, PtrVal)>);
inline std::monostate gs_dummy(SS&, immer::flex_vector<PtrVal>, std::function<std::monostate(SS&, PtrVal)>) {
  std::cout << "Warning: invoking gs_dummy, some paths is not continued!\n";
  return std::monostate{};
}
inline std::monostate start_gs_main(SS& state, immer::flex_vector<PtrVal> args, std::function<std::monostate(SS&, PtrVal)> cont) {
  if (can_par_tp()) {
    tp.add_task(1, [=] () mutable { return gs_main(state, args, cont); });
    return std::monostate{};
  }
  return gs_main(state, args, cont);
}
