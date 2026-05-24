#include "wasm/z3_env.hpp"

#include <cassert>

Z3Env GLOBAL_Z3_ENV;
std::unordered_map<unsigned, int> Z3ExprSizeMap;

Z3Env::Z3Env() : z3_ctx() {}

z3::context &global_z3_ctx() {
  return GLOBAL_Z3_ENV.z3_ctx;
}

int get_z3_fp_sort_size(const z3::sort &s) {
  assert(s.is_fpa());
  return s.fpa_ebits() + s.fpa_sbits();
}

int get_z3_expr_size(const z3::expr &e) {
  unsigned id = e.id();

  if (Z3ExprSizeMap.find(id) != Z3ExprSizeMap.end()) {
    return Z3ExprSizeMap[id];
  }

  unsigned count = 1; // count self
  for (unsigned i = 0; i < e.num_args(); i++) {
    count += get_z3_expr_size(e.arg(i));
  }

  Z3ExprSizeMap[id] = count;
  return count;
}