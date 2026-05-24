#ifndef WASM_Z3_ENV_HPP
#define WASM_Z3_ENV_HPP

#include "z3++.h"
#include <unordered_map>

struct Z3Env {
  z3::context z3_ctx;

  Z3Env();
};

extern Z3Env GLOBAL_Z3_ENV;

z3::context &global_z3_ctx();

// A map from z3 expression id to their ast size
extern std::unordered_map<unsigned, int> Z3ExprSizeMap;

int get_z3_fp_sort_size(const z3::sort &s);

int get_z3_expr_size(const z3::expr &e);

#endif // WASM_Z3_ENV_HPP