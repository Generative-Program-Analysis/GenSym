// prepare necessary declarations and definitions for library mode compilation
#include <gensym/runtime.hpp>
using namespace gensym::runtime::v1;
std::monostate app_main(SS&, Args, Cont);
std::monostate gs_main(SS&, Args, Cont);
inline std::monostate gs_dummy(SS&, Args, Cont) {
  std::cout << "Warning: invoking gs_dummy, some path is not continued!\n";
  return std::monostate{};
}
inline std::monostate start_gs_main(SS& state, Args args, Cont cont) {
  if (can_par_tp()) {
    add_task(1, [=] () mutable { return gs_main(state, args, cont); });
    return std::monostate{};
  }
  return gs_main(state, args, cont);
}
