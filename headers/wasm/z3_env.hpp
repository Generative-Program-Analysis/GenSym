#ifndef WASM_Z3_ENV_HPP
#define WASM_Z3_ENV_HPP
#include "z3++.h"

struct Z3Env {
  z3::context z3_ctx;

  Z3Env() : z3_ctx() {}
};

static Z3Env GLOBAL_Z3_ENV;

inline z3::context &global_z3_ctx() { return GLOBAL_Z3_ENV.z3_ctx; }

// A map from z3 expression id to their ast size
static std::unordered_map<unsigned, int> Z3ExprSizeMap;

static int get_z3_expr_size(const z3::expr &e) {
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

#endif // WASM_Z3_ENV_HPP
