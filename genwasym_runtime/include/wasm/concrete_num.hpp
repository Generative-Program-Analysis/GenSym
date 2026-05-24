#ifndef WASM_CONCRETE_NUM_HPP
#define WASM_CONCRETE_NUM_HPP

#include <cstdint>

struct Num {
  Num(int64_t value);
  Num();

  int64_t value;

  int32_t toInt() const;
  uint32_t toUInt() const;
  int64_t toInt64() const;
  uint64_t toUInt64() const;
  float toF32() const;
  double toF64() const;

  static void debug_print(const char *op, const Num &a, const Num &b,
                          const Num &res);

  Num WasmBool(bool condition) const;

  Num i32_eq(const Num &other) const;
  Num i32_ne(const Num &other) const;
  Num i32_lt_s(const Num &other) const;
  Num i32_lt_u(const Num &other) const;
  Num i32_le_s(const Num &other) const;
  Num i32_le_u(const Num &other) const;
  Num i32_gt_s(const Num &other) const;
  Num i32_gt_u(const Num &other) const;
  Num i32_ge_s(const Num &other) const;
  Num i32_ge_u(const Num &other) const;

  Num i32_add(const Num &other) const;
  Num i32_sub(const Num &other) const;
  Num i32_mul(const Num &other) const;
  Num i32_div_s(const Num &other) const;
  Num i32_div_u(const Num &other) const;
  Num i32_rem_s(const Num &other) const;
  Num i32_rem_u(const Num &other) const;

  Num i32_shl(const Num &other) const;
  Num i32_shr_s(const Num &other) const;
  Num i32_shr_u(const Num &other) const;
  Num i32_and(const Num &other) const;
  Num i32_xor(const Num &other) const;
  Num i32_or(const Num &other) const;

  Num i32_extend_to_i64_s() const;
  Num i32_extend_to_i64_u() const;

  Num i64_eq(const Num &other) const;
  Num i64_ne(const Num &other) const;
  Num i64_lt_s(const Num &other) const;
  Num i64_lt_u(const Num &other) const;
  Num i64_le_s(const Num &other) const;
  Num i64_le_u(const Num &other) const;
  Num i64_gt_s(const Num &other) const;
  Num i64_gt_u(const Num &other) const;
  Num i64_ge_s(const Num &other) const;
  Num i64_ge_u(const Num &other) const;

  Num i64_add(const Num &other) const;
  Num i64_sub(const Num &other) const;
  Num i64_mul(const Num &other) const;
  Num i64_div_s(const Num &other) const;
  Num i64_div_u(const Num &other) const;
  Num i64_rem_s(const Num &other) const;
  Num i64_rem_u(const Num &other) const;

  Num i64_shl(const Num &other) const;
  Num i64_shr_s(const Num &other) const;
  Num i64_shr_u(const Num &other) const;
  Num i64_and(const Num &other) const;
  Num i64_xor(const Num &other) const;
  Num i64_or(const Num &other) const;

  static float f32_from_bits(uint32_t bits);
  static uint32_t f32_to_bits(float f);
  static bool f32_is_nan(uint32_t bits);

  Num f32_add(const Num &other) const;
  Num f32_sub(const Num &other) const;
  Num f32_mul(const Num &other) const;
  Num f32_div(const Num &other) const;
  Num f32_eq(const Num &other) const;
  Num f32_ne(const Num &other) const;
  Num f32_lt(const Num &other) const;
  Num f32_le(const Num &other) const;
  Num f32_gt(const Num &other) const;
  Num f32_ge(const Num &other) const;
  Num f32_abs() const;
  Num f32_neg() const;

  Num convert_i32_to_f32_s() const;
  Num convert_i32_to_f32_u() const;
  Num convert_i64_to_f32_s() const;
  Num convert_i64_to_f32_u() const;

  Num f32_min(const Num &other) const;
  Num f32_max(const Num &other) const;
  Num f32_copysign(const Num &other) const;

  static double f64_from_bits(uint64_t bits);
  static uint64_t f64_to_bits(double d);
  static bool f64_is_nan(uint64_t bits);

  Num f64_add(const Num &other) const;
  Num f64_sub(const Num &other) const;
  Num f64_mul(const Num &other) const;
  Num f64_div(const Num &other) const;
  Num f64_eq(const Num &other) const;
  Num f64_ne(const Num &other) const;
  Num f64_lt(const Num &other) const;
  Num f64_le(const Num &other) const;
  Num f64_gt(const Num &other) const;
  Num f64_ge(const Num &other) const;
  Num f64_abs() const;
  Num f64_neg() const;

  Num convert_i32_to_f64_s() const;
  Num convert_i32_to_f64_u() const;
  Num convert_i64_to_f64_s() const;
  Num convert_i64_to_f64_u() const;

  Num trunc_f64_to_i32_u() const;

  Num f64_min(const Num &other) const;
  Num f64_max(const Num &other) const;
  Num f64_copysign(const Num &other) const;

  bool logical_and(const Num &other) const;
  bool logical_or(const Num &other) const;
};

Num I32V(int v);
Num I64V(int64_t v);
Num F32V(float f);
Num F64V(double d);

#endif // WASM_CONCRETE_NUM_HPP