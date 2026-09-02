#include "wasm/concrete_num.hpp"

#include <cmath>
#include <iostream>
#include <stdexcept>

Num::Num(int64_t value) : value(value) {}

Num::Num() : value(0) {}

int32_t Num::toInt() const { return static_cast<int32_t>(value); }

uint32_t Num::toUInt() const { return static_cast<uint32_t>(value); }

int64_t Num::toInt64() const { return static_cast<int64_t>(value); }

uint64_t Num::toUInt64() const { return static_cast<uint64_t>(value); }

float Num::toF32() const { return *reinterpret_cast<const float *>(&value); }

double Num::toF64() const { return *reinterpret_cast<const double *>(&value); }

void Num::debug_print(const char *op, const Num &a, const Num &b,
                      const Num &res) {
#ifdef DEBUG_OP
  std::cout << "[Debug] " << op << ": lhs=" << static_cast<int32_t>(a.value)
            << " rhs=" << static_cast<int32_t>(b.value)
            << " -> res=" << static_cast<int32_t>(res.value) << std::endl;
#endif
}

Num Num::WasmBool(bool condition) const {
  Num res(condition ? 1 : 0);
  debug_print("WasmBool", *this, *this, res);
  return res;
}

Num Num::i32_eq(const Num &other) const {
  Num res = WasmBool(this->toUInt() == other.toUInt());
  debug_print("i32.eq", *this, other, res);
  return res;
}

Num Num::i32_ne(const Num &other) const {
  Num res = WasmBool(this->toUInt() != other.toUInt());
  debug_print("i32.ne", *this, other, res);
  return res;
}

Num Num::i32_lt_s(const Num &other) const {
  Num res = WasmBool(this->toInt() < other.toInt());
  debug_print("i32.lt_s", *this, other, res);
  return res;
}

Num Num::i32_lt_u(const Num &other) const {
  Num res = WasmBool(this->toUInt() < other.toUInt());
  debug_print("i32.lt_u", *this, other, res);
  return res;
}

Num Num::i32_le_s(const Num &other) const {
  Num res = WasmBool(this->toInt() <= other.toInt());
  debug_print("i32.le_s", *this, other, res);
  return res;
}

Num Num::i32_le_u(const Num &other) const {
  Num res = WasmBool(this->toUInt() <= other.toUInt());
  debug_print("i32.le_u", *this, other, res);
  return res;
}

Num Num::i32_gt_s(const Num &other) const {
  Num res = WasmBool(this->toInt() > other.toInt());
  debug_print("i32.gt_s", *this, other, res);
  return res;
}

Num Num::i32_gt_u(const Num &other) const {
  Num res = WasmBool(this->toUInt() > other.toUInt());
  debug_print("i32.gt_u", *this, other, res);
  return res;
}

Num Num::i32_ge_s(const Num &other) const {
  Num res = WasmBool(this->toInt() >= other.toInt());
  debug_print("i32.ge_s", *this, other, res);
  return res;
}

Num Num::i32_ge_u(const Num &other) const {
  Num res = WasmBool(this->toUInt() >= other.toUInt());
  debug_print("i32.ge_u", *this, other, res);
  return res;
}

Num Num::i32_add(const Num &other) const {
  uint32_t result_u = this->toUInt() + other.toUInt();
  Num res(static_cast<int32_t>(result_u));
  debug_print("i32.add", *this, other, res);
  return res;
}

Num Num::i32_sub(const Num &other) const {
  uint32_t result_u = this->toUInt() - other.toUInt();
  Num res(static_cast<int32_t>(result_u));
  debug_print("i32.sub", *this, other, res);
  return res;
}

Num Num::i32_mul(const Num &other) const {
  uint32_t result_u = this->toUInt() * other.toUInt();
  Num res(static_cast<int32_t>(result_u));
  debug_print("i32.mul", *this, other, res);
  return res;
}

Num Num::i32_div_s(const Num &other) const {
  int32_t divisor = other.toInt();
  int32_t dividend = this->toInt();

  if (divisor == 0) {
    throw std::runtime_error("i32.div_s: Division by zero");
  }

  Num res(dividend / divisor);
  debug_print("i32.div_s", *this, other, res);
  return res;
}

Num Num::i32_div_u(const Num &other) const {
  uint32_t divisor = other.toUInt();
  uint32_t dividend = this->toUInt();

  if (divisor == 0) {
    throw std::runtime_error("i32.div_u: Division by zero");
  }

  Num res(static_cast<int32_t>(dividend / divisor));
  debug_print("i32.div_u", *this, other, res);
  return res;
}

Num Num::i32_rem_s(const Num &other) const {
  int32_t divisor = other.toInt();
  int32_t dividend = this->toInt();

  if (divisor == 0) {
    throw std::runtime_error("i32.rem_s: Division by zero");
  }

  if (dividend == INT32_MIN && divisor == -1) {
    Num res(0);
    debug_print("i32.rem_s", *this, other, res);
    return res;
  }

  Num res(dividend % divisor);
  debug_print("i32.rem_s", *this, other, res);
  return res;
}

Num Num::i32_rem_u(const Num &other) const {
  uint32_t divisor = other.toUInt();
  uint32_t dividend = this->toUInt();

  if (divisor == 0) {
    throw std::runtime_error("i32.rem_u: Division by zero");
  }

  Num res(static_cast<int32_t>(dividend % divisor));
  debug_print("i32.rem_u", *this, other, res);
  return res;
}

Num Num::i32_shl(const Num &other) const {
  uint32_t shift_amount = other.toUInt() & 0x1F;
  uint32_t result_u = toUInt() << shift_amount;
  Num res(static_cast<int32_t>(result_u));
  debug_print("i32.shl", *this, other, res);
  return res;
}

Num Num::i32_shr_s(const Num &other) const {
  uint32_t shift_amount = other.toUInt() & 0x1F;
  int32_t result_s = toInt() >> shift_amount;
  Num res(result_s);
  debug_print("i32.shr_s", *this, other, res);
  return res;
}

Num Num::i32_shr_u(const Num &other) const {
  uint32_t shift_amount = other.toUInt() & 0x1F;
  uint32_t result_u = toUInt() >> shift_amount;
  Num res(static_cast<int32_t>(result_u));
  debug_print("i32.shr_u", *this, other, res);
  return res;
}

Num Num::i32_and(const Num &other) const {
  uint32_t result_u = this->toUInt() & other.toUInt();
  Num res(static_cast<int32_t>(result_u));
  debug_print("i32.and", *this, other, res);
  return res;
}

Num Num::i32_xor(const Num &other) const {
  uint32_t result_u = this->toUInt() ^ other.toUInt();
  Num res(static_cast<int32_t>(result_u));
  debug_print("i32.xor", *this, other, res);
  return res;
}

Num Num::i32_or(const Num &other) const {
  uint32_t result_u = this->toUInt() | other.toUInt();
  Num res(static_cast<int32_t>(result_u));
  debug_print("i32.or", *this, other, res);
  return res;
}

Num Num::i32_extend_to_i64_s() const {
  int64_t result_s = static_cast<int64_t>(this->toInt());
  Num res(result_s);
  debug_print("i32.extend_to_i64_s", *this, *this, res);
  return res;
}

Num Num::i32_extend_to_i64_u() const {
  uint64_t result_u = static_cast<uint64_t>(this->toUInt());
  Num res(static_cast<int64_t>(result_u));
  debug_print("i32.extend_to_i64_u", *this, *this, res);
  return res;
}

Num Num::i64_eq(const Num &other) const {
  Num res = WasmBool(this->toUInt64() == other.toUInt64());
  debug_print("i64.eq", *this, other, res);
  return res;
}

Num Num::i64_ne(const Num &other) const {
  Num res = WasmBool(this->toUInt64() != other.toUInt64());
  debug_print("i64.ne", *this, other, res);
  return res;
}

Num Num::i64_lt_s(const Num &other) const {
  Num res = WasmBool(this->toInt64() < other.toInt64());
  debug_print("i64.lt_s", *this, other, res);
  return res;
}

Num Num::i64_lt_u(const Num &other) const {
  Num res = WasmBool(this->toUInt64() < other.toUInt64());
  debug_print("i64.lt_u", *this, other, res);
  return res;
}

Num Num::i64_le_s(const Num &other) const {
  Num res = WasmBool(this->toInt64() <= other.toInt64());
  debug_print("i64.le_s", *this, other, res);
  return res;
}

Num Num::i64_le_u(const Num &other) const {
  Num res = WasmBool(this->toUInt64() <= other.toUInt64());
  debug_print("i64.le_u", *this, other, res);
  return res;
}

Num Num::i64_gt_s(const Num &other) const {
  Num res = WasmBool(this->toInt64() > other.toInt64());
  debug_print("i64.gt_s", *this, other, res);
  return res;
}

Num Num::i64_gt_u(const Num &other) const {
  Num res = WasmBool(this->toUInt64() > other.toUInt64());
  debug_print("i64.gt_u", *this, other, res);
  return res;
}

Num Num::i64_ge_s(const Num &other) const {
  Num res = WasmBool(this->toInt64() >= other.toInt64());
  debug_print("i64.ge_s", *this, other, res);
  return res;
}

Num Num::i64_ge_u(const Num &other) const {
  Num res = WasmBool(this->toUInt64() >= other.toUInt64());
  debug_print("i64.ge_u", *this, other, res);
  return res;
}

Num Num::i64_add(const Num &other) const {
  uint64_t result_u = this->toUInt64() + other.toUInt64();
  Num res(static_cast<int64_t>(result_u));
  debug_print("i64.add", *this, other, res);
  return res;
}

Num Num::i64_sub(const Num &other) const {
  uint64_t result_u = this->toUInt64() - other.toUInt64();
  Num res(static_cast<int64_t>(result_u));
  debug_print("i64.sub", *this, other, res);
  return res;
}

Num Num::i64_mul(const Num &other) const {
  uint64_t result_u = this->toUInt64() * other.toUInt64();
  Num res(static_cast<int64_t>(result_u));
  debug_print("i64.mul", *this, other, res);
  return res;
}

Num Num::i64_div_s(const Num &other) const {
  int64_t divisor = other.toInt64();
  int64_t dividend = this->toInt64();

  if (divisor == 0) {
    throw std::runtime_error("i64.div_s: Division by zero");
  }

  if (dividend == INT64_MIN && divisor == -1) {
    throw std::runtime_error("i64.div_s: Integer overflow");
  }

  Num res(dividend / divisor);
  debug_print("i64.div_s", *this, other, res);
  return res;
}

Num Num::i64_div_u(const Num &other) const {
  uint64_t divisor = other.toUInt64();
  uint64_t dividend = this->toUInt64();

  if (divisor == 0) {
    throw std::runtime_error("i64.div_u: Division by zero");
  }

  Num res(static_cast<int64_t>(dividend / divisor));
  debug_print("i64.div_u", *this, other, res);
  return res;
}

Num Num::i64_rem_s(const Num &other) const {
  int64_t divisor = other.toInt64();
  int64_t dividend = this->toInt64();

  if (divisor == 0) {
    throw std::runtime_error("i64.rem_s: Division by zero");
  }

  if (dividend == INT64_MIN && divisor == -1) {
    Num res(0);
    debug_print("i64.rem_s", *this, other, res);
    return res;
  }

  Num res(dividend % divisor);
  debug_print("i64.rem_s", *this, other, res);
  return res;
}

Num Num::i64_rem_u(const Num &other) const {
  uint64_t divisor = other.toUInt64();
  uint64_t dividend = this->toUInt64();

  if (divisor == 0) {
    throw std::runtime_error("i64.rem_u: Division by zero");
  }

  Num res(static_cast<int64_t>(dividend % divisor));
  debug_print("i64.rem_u", *this, other, res);
  return res;
}

Num Num::i64_shl(const Num &other) const {
  uint64_t shift_amount = other.toUInt64() & 0x3F;
  uint64_t result_u = toUInt64() << shift_amount;
  Num res(static_cast<int64_t>(result_u));
  debug_print("i64.shl", *this, other, res);
  return res;
}

Num Num::i64_shr_s(const Num &other) const {
  uint64_t shift_amount = other.toUInt64() & 0x3F;
  int64_t result_s = toInt64() >> shift_amount;
  Num res(result_s);
  debug_print("i64.shr_s", *this, other, res);
  return res;
}

Num Num::i64_shr_u(const Num &other) const {
  uint64_t shift_amount = other.toUInt64() & 0x3F;
  uint64_t result_u = toUInt64() >> shift_amount;
  Num res(static_cast<int64_t>(result_u));
  debug_print("i64.shr_u", *this, other, res);
  return res;
}

Num Num::i64_and(const Num &other) const {
  uint64_t result_u = this->toUInt64() & other.toUInt64();
  Num res(static_cast<int64_t>(result_u));
  debug_print("i64.and", *this, other, res);
  return res;
}

Num Num::i64_xor(const Num &other) const {
  uint64_t result_u = this->toUInt64() ^ other.toUInt64();
  Num res(static_cast<int64_t>(result_u));
  debug_print("i64.xor", *this, other, res);
  return res;
}

Num Num::i64_or(const Num &other) const {
  uint64_t result_u = this->toUInt64() | other.toUInt64();
  Num res(static_cast<int64_t>(result_u));
  debug_print("i64.or", *this, other, res);
  return res;
}

float Num::f32_from_bits(uint32_t bits) {
  union {
    uint32_t i;
    float f;
  } u;
  u.i = bits;
  return u.f;
}

uint32_t Num::f32_to_bits(float f) {
  union {
    uint32_t i;
    float f;
  } u;
  u.f = f;
  return u.i;
}

bool Num::f32_is_nan(uint32_t bits) {
  return (bits & 0x7F800000u) == 0x7F800000u && (bits & 0x007FFFFFu) != 0;
}

Num Num::f32_add(const Num &other) const {
  uint32_t a_bits = toUInt();
  uint32_t b_bits = other.toUInt();
  float a = f32_from_bits(a_bits);
  float b = f32_from_bits(b_bits);
  float r = a + b;
  uint32_t r_bits = f32_to_bits(r);
  Num res(static_cast<int32_t>(r_bits));
  debug_print("f32.add", *this, other, res);
  return res;
}

Num Num::f32_sub(const Num &other) const {
  uint32_t a_bits = toUInt();
  uint32_t b_bits = other.toUInt();
  float a = f32_from_bits(a_bits);
  float b = f32_from_bits(b_bits);
  float r = a - b;
  uint32_t r_bits = f32_to_bits(r);
  Num res(static_cast<int32_t>(r_bits));
  debug_print("f32.sub", *this, other, res);
  return res;
}

Num Num::f32_mul(const Num &other) const {
  uint32_t a_bits = toUInt();
  uint32_t b_bits = other.toUInt();
  float a = f32_from_bits(a_bits);
  float b = f32_from_bits(b_bits);
  float r = a * b;
  uint32_t r_bits = f32_to_bits(r);
  Num res(static_cast<int32_t>(r_bits));
  debug_print("f32.mul", *this, other, res);
  return res;
}

Num Num::f32_div(const Num &other) const {
  uint32_t a_bits = toUInt();
  uint32_t b_bits = other.toUInt();
  float a = f32_from_bits(a_bits);
  float b = f32_from_bits(b_bits);
  float r = a / b;
  uint32_t r_bits = f32_to_bits(r);
  Num res(static_cast<int32_t>(r_bits));
  debug_print("f32.div", *this, other, res);
  return res;
}

Num Num::f32_eq(const Num &other) const {
  uint32_t a_bits = toUInt();
  uint32_t b_bits = other.toUInt();

  if (f32_is_nan(a_bits) || f32_is_nan(b_bits)) {
    Num res = WasmBool(false);
    debug_print("f32.eq", *this, other, res);
    return res;
  }

  float a = f32_from_bits(a_bits);
  float b = f32_from_bits(b_bits);
  Num res = WasmBool(a == b);
  debug_print("f32.eq", *this, other, res);
  return res;
}

Num Num::f32_ne(const Num &other) const {
  uint32_t a_bits = toUInt();
  uint32_t b_bits = other.toUInt();

  if (f32_is_nan(a_bits) || f32_is_nan(b_bits)) {
    Num res = WasmBool(true);
    debug_print("f32.ne", *this, other, res);
    return res;
  }

  float a = f32_from_bits(a_bits);
  float b = f32_from_bits(b_bits);
  Num res = WasmBool(a != b);
  debug_print("f32.ne", *this, other, res);
  return res;
}

Num Num::f32_lt(const Num &other) const {
  uint32_t a_bits = toUInt();
  uint32_t b_bits = other.toUInt();

  if (f32_is_nan(a_bits) || f32_is_nan(b_bits))
    return WasmBool(false);

  float a = f32_from_bits(a_bits);
  float b = f32_from_bits(b_bits);
  Num res = WasmBool(a < b);
  debug_print("f32.lt", *this, other, res);
  return res;
}

Num Num::f32_le(const Num &other) const {
  uint32_t a_bits = toUInt();
  uint32_t b_bits = other.toUInt();

  if (f32_is_nan(a_bits) || f32_is_nan(b_bits))
    return WasmBool(false);

  float a = f32_from_bits(a_bits);
  float b = f32_from_bits(b_bits);
  Num res = WasmBool(a <= b);
  debug_print("f32.le", *this, other, res);
  return res;
}

Num Num::f32_gt(const Num &other) const {
  uint32_t a_bits = toUInt();
  uint32_t b_bits = other.toUInt();

  if (f32_is_nan(a_bits) || f32_is_nan(b_bits))
    return WasmBool(false);

  float a = f32_from_bits(a_bits);
  float b = f32_from_bits(b_bits);
  Num res = WasmBool(a > b);
  debug_print("f32.gt", *this, other, res);
  return res;
}

Num Num::f32_ge(const Num &other) const {
  uint32_t a_bits = toUInt();
  uint32_t b_bits = other.toUInt();

  if (f32_is_nan(a_bits) || f32_is_nan(b_bits))
    return WasmBool(false);

  float a = f32_from_bits(a_bits);
  float b = f32_from_bits(b_bits);
  Num res = WasmBool(a >= b);
  debug_print("f32.ge", *this, other, res);
  return res;
}

Num Num::f32_abs() const {
  uint32_t a_bits = toUInt();
  uint32_t r_bits = a_bits & 0x7FFFFFFFu;
  Num res(static_cast<int32_t>(r_bits));
  debug_print("f32.abs", *this, *this, res);
  return res;
}

Num Num::f32_neg() const {
  uint32_t a_bits = toUInt();
  uint32_t r_bits = a_bits ^ 0x80000000u;
  Num res(static_cast<int32_t>(r_bits));
  debug_print("f32.neg", *this, *this, res);
  return res;
}

Num Num::convert_i32_to_f32_s() const {
  uint32_t r_bits = f32_to_bits(static_cast<float>(toInt()));
  return Num(static_cast<int32_t>(r_bits));
}

Num Num::convert_i32_to_f32_u() const {
  uint32_t r_bits = f32_to_bits(static_cast<float>(toUInt()));
  return Num(static_cast<int32_t>(r_bits));
}

Num Num::convert_i64_to_f32_s() const {
  uint32_t r_bits = f32_to_bits(static_cast<float>(toInt64()));
  return Num(static_cast<int32_t>(r_bits));
}

Num Num::convert_i64_to_f32_u() const {
  uint32_t r_bits = f32_to_bits(static_cast<float>(toUInt64()));
  return Num(static_cast<int32_t>(r_bits));
}

Num Num::f32_min(const Num &other) const {
  uint32_t a_bits = toUInt();
  uint32_t b_bits = other.toUInt();

  if (f32_is_nan(a_bits))
    return Num(static_cast<int32_t>(a_bits));
  if (f32_is_nan(b_bits))
    return Num(static_cast<int32_t>(b_bits));

  float a = f32_from_bits(a_bits);
  float b = f32_from_bits(b_bits);

  if (a == b) {
    if ((a_bits & 0x80000000u) || (b_bits & 0x80000000u))
      return Num(static_cast<int32_t>((a_bits & 0x80000000u) ? a_bits : b_bits));
    return Num(static_cast<int32_t>(a_bits));
  }

  float r = (a < b) ? a : b;
  uint32_t r_bits = f32_to_bits(r);
  Num res(static_cast<int32_t>(r_bits));
  debug_print("f32.min", *this, other, res);
  return res;
}

Num Num::f32_max(const Num &other) const {
  uint32_t a_bits = toUInt();
  uint32_t b_bits = other.toUInt();

  if (f32_is_nan(a_bits))
    return Num(static_cast<int32_t>(a_bits));
  if (f32_is_nan(b_bits))
    return Num(static_cast<int32_t>(b_bits));

  float a = f32_from_bits(a_bits);
  float b = f32_from_bits(b_bits);

  if (a == b) {
    if ((a_bits & 0x80000000u) || (b_bits & 0x80000000u))
      return Num(static_cast<int32_t>((a_bits & 0x80000000u) ? b_bits : a_bits));
    return Num(static_cast<int32_t>(a_bits));
  }

  float r = (a > b) ? a : b;
  uint32_t r_bits = f32_to_bits(r);
  Num res(static_cast<int32_t>(r_bits));
  debug_print("f32.max", *this, other, res);
  return res;
}

Num Num::f32_copysign(const Num &other) const {
  uint32_t a_bits = toUInt();
  uint32_t b_bits = other.toUInt();
  uint32_t r_bits = (a_bits & 0x7FFFFFFFu) | (b_bits & 0x80000000u);
  Num res(static_cast<int32_t>(r_bits));
  debug_print("f32.copysign", *this, other, res);
  return res;
}

double Num::f64_from_bits(uint64_t bits) {
  union {
    uint64_t i;
    double d;
  } u;
  u.i = bits;
  return u.d;
}

uint64_t Num::f64_to_bits(double d) {
  union {
    uint64_t i;
    double d;
  } u;
  u.d = d;
  return u.i;
}

bool Num::f64_is_nan(uint64_t bits) {
  return (bits & 0x7FF0000000000000ull) == 0x7FF0000000000000ull &&
         (bits & 0x000FFFFFFFFFFFFFull) != 0;
}

Num Num::f64_add(const Num &other) const {
  uint64_t a_bits = toUInt64();
  uint64_t b_bits = other.toUInt64();
  double a = f64_from_bits(a_bits);
  double b = f64_from_bits(b_bits);
  double r = a + b;
  uint64_t r_bits = f64_to_bits(r);
  Num res(static_cast<int64_t>(r_bits));
  debug_print("f64.add", *this, other, res);
  return res;
}

Num Num::f64_sub(const Num &other) const {
  uint64_t a_bits = toUInt64();
  uint64_t b_bits = other.toUInt64();
  double a = f64_from_bits(a_bits);
  double b = f64_from_bits(b_bits);
  double r = a - b;
  uint64_t r_bits = f64_to_bits(r);
  Num res(static_cast<int64_t>(r_bits));
  debug_print("f64.sub", *this, other, res);
  return res;
}

Num Num::f64_mul(const Num &other) const {
  uint64_t a_bits = toUInt64();
  uint64_t b_bits = other.toUInt64();
  double a = f64_from_bits(a_bits);
  double b = f64_from_bits(b_bits);
  double r = a * b;
  uint64_t r_bits = f64_to_bits(r);
  Num res(static_cast<int64_t>(r_bits));
  debug_print("f64.mul", *this, other, res);
  return res;
}

Num Num::f64_div(const Num &other) const {
  uint64_t a_bits = toUInt64();
  uint64_t b_bits = other.toUInt64();
  double a = f64_from_bits(a_bits);
  double b = f64_from_bits(b_bits);
  double r = a / b;
  uint64_t r_bits = f64_to_bits(r);
  Num res(static_cast<int64_t>(r_bits));
  debug_print("f64.div", *this, other, res);
  return res;
}

Num Num::f64_eq(const Num &other) const {
  uint64_t a_bits = toUInt64();
  uint64_t b_bits = other.toUInt64();

  if (f64_is_nan(a_bits) || f64_is_nan(b_bits)) {
    Num res = WasmBool(false);
    debug_print("f64.eq", *this, other, res);
    return res;
  }

  double a = f64_from_bits(a_bits);
  double b = f64_from_bits(b_bits);
  Num res = WasmBool(a == b);
  debug_print("f64.eq", *this, other, res);
  return res;
}

Num Num::f64_ne(const Num &other) const {
  uint64_t a_bits = toUInt64();
  uint64_t b_bits = other.toUInt64();

  if (f64_is_nan(a_bits) || f64_is_nan(b_bits)) {
    Num res = WasmBool(true);
    debug_print("f64.ne", *this, other, res);
    return res;
  }

  double a = f64_from_bits(a_bits);
  double b = f64_from_bits(b_bits);
  Num res = WasmBool(a != b);
  debug_print("f64.ne", *this, other, res);
  return res;
}

Num Num::f64_lt(const Num &other) const {
  uint64_t a_bits = toUInt64();
  uint64_t b_bits = other.toUInt64();

  if (f64_is_nan(a_bits) || f64_is_nan(b_bits))
    return WasmBool(false);

  double a = f64_from_bits(a_bits);
  double b = f64_from_bits(b_bits);
  Num res = WasmBool(a < b);
  debug_print("f64.lt", *this, other, res);
  return res;
}

Num Num::f64_le(const Num &other) const {
  uint64_t a_bits = toUInt64();
  uint64_t b_bits = other.toUInt64();

  if (f64_is_nan(a_bits) || f64_is_nan(b_bits))
    return WasmBool(false);

  double a = f64_from_bits(a_bits);
  double b = f64_from_bits(b_bits);
  Num res = WasmBool(a <= b);
  debug_print("f64.le", *this, other, res);
  return res;
}

Num Num::f64_gt(const Num &other) const {
  uint64_t a_bits = toUInt64();
  uint64_t b_bits = other.toUInt64();

  if (f64_is_nan(a_bits) || f64_is_nan(b_bits))
    return WasmBool(false);

  double a = f64_from_bits(a_bits);
  double b = f64_from_bits(b_bits);
  Num res = WasmBool(a > b);
  debug_print("f64.gt", *this, other, res);
  return res;
}

Num Num::f64_ge(const Num &other) const {
  uint64_t a_bits = toUInt64();
  uint64_t b_bits = other.toUInt64();

  if (f64_is_nan(a_bits) || f64_is_nan(b_bits))
    return WasmBool(false);

  double a = f64_from_bits(a_bits);
  double b = f64_from_bits(b_bits);
  Num res = WasmBool(a >= b);
  debug_print("f64.ge", *this, other, res);
  return res;
}

Num Num::f64_abs() const {
  uint64_t a_bits = toUInt64();
  uint64_t r_bits = a_bits & 0x7FFFFFFFFFFFFFFFull;
  Num res(static_cast<int64_t>(r_bits));
  debug_print("f64.abs", *this, *this, res);
  return res;
}

Num Num::f64_neg() const {
  uint64_t a_bits = toUInt64();
  uint64_t r_bits = a_bits ^ 0x8000000000000000ull;
  Num res(static_cast<int64_t>(r_bits));
  debug_print("f64.neg", *this, *this, res);
  return res;
}

Num Num::convert_i32_to_f64_s() const {
  uint64_t r_bits = f64_to_bits(static_cast<double>(toInt()));
  return Num(static_cast<int64_t>(r_bits));
}

Num Num::convert_i32_to_f64_u() const {
  uint64_t r_bits = f64_to_bits(static_cast<double>(toUInt()));
  return Num(static_cast<int64_t>(r_bits));
}

Num Num::convert_i64_to_f64_s() const {
  uint64_t r_bits = f64_to_bits(static_cast<double>(toInt64()));
  return Num(static_cast<int64_t>(r_bits));
}

Num Num::convert_i64_to_f64_u() const {
  uint64_t r_bits = f64_to_bits(static_cast<double>(toUInt64()));
  return Num(static_cast<int64_t>(r_bits));
}

Num Num::trunc_f64_to_i32_u() const {
  uint64_t bits = toUInt64();
  double value = f64_from_bits(bits);

  if (std::isnan(value)) {
    throw std::runtime_error("i32.trunc_f64_u: NaN");
  }

  if (std::isinf(value)) {
    throw std::runtime_error("i32.trunc_f64_u: Infinity");
  }

  if (value < 0.0 || value >= 4294967296.0) {
    throw std::runtime_error("i32.trunc_f64_u: Out of range");
  }

  double truncated = std::trunc(value);
  uint32_t result = static_cast<uint32_t>(truncated);
  Num res(static_cast<int32_t>(result));
  debug_print("i32.trunc_f64_u", *this, *this, res);
  return res;
}

Num Num::f64_min(const Num &other) const {
  uint64_t a_bits = toUInt64();
  uint64_t b_bits = other.toUInt64();

  if (f64_is_nan(a_bits))
    return Num(static_cast<int64_t>(a_bits));
  if (f64_is_nan(b_bits))
    return Num(static_cast<int64_t>(b_bits));

  double a = f64_from_bits(a_bits);
  double b = f64_from_bits(b_bits);

  if (a == b) {
    if ((a_bits & 0x8000000000000000ull) ||
        (b_bits & 0x8000000000000000ull))
      return Num(static_cast<int64_t>(
          (a_bits & 0x8000000000000000ull) ? a_bits : b_bits));
    return Num(static_cast<int64_t>(a_bits));
  }

  double r = (a < b) ? a : b;
  uint64_t r_bits = f64_to_bits(r);
  Num res(static_cast<int64_t>(r_bits));
  debug_print("f64.min", *this, other, res);
  return res;
}

Num Num::f64_max(const Num &other) const {
  uint64_t a_bits = toUInt64();
  uint64_t b_bits = other.toUInt64();

  if (f64_is_nan(a_bits))
    return Num(static_cast<int64_t>(a_bits));
  if (f64_is_nan(b_bits))
    return Num(static_cast<int64_t>(b_bits));

  double a = f64_from_bits(a_bits);
  double b = f64_from_bits(b_bits);

  if (a == b) {
    if ((a_bits & 0x8000000000000000ull) ||
        (b_bits & 0x8000000000000000ull))
      return Num(static_cast<int64_t>(
          (a_bits & 0x8000000000000000ull) ? b_bits : a_bits));
    return Num(static_cast<int64_t>(a_bits));
  }

  double r = (a > b) ? a : b;
  uint64_t r_bits = f64_to_bits(r);
  Num res(static_cast<int64_t>(r_bits));
  debug_print("f64.max", *this, other, res);
  return res;
}

Num Num::f64_copysign(const Num &other) const {
  uint64_t a_bits = toUInt64();
  uint64_t b_bits = other.toUInt64();
  uint64_t r_bits =
      (a_bits & 0x7FFFFFFFFFFFFFFFull) | (b_bits & 0x8000000000000000ull);
  Num res(static_cast<int64_t>(r_bits));
  debug_print("f64.copysign", *this, other, res);
  return res;
}

bool Num::logical_and(const Num &other) const {
  return (this->toUInt() != 0) && (other.toUInt() != 0);
}

bool Num::logical_or(const Num &other) const {
  return (this->toUInt() != 0) || (other.toUInt() != 0);
}

Num I32V(int v) { return v; }

Num I64V(int64_t v) { return v; }

Num F32V(float f) {
  union {
    uint32_t i;
    float f;
  } u;
  u.f = f;
  return static_cast<int32_t>(u.i);
}

Num F64V(double d) {
  union {
    uint64_t i;
    double d;
  } u;
  u.d = d;
  return static_cast<int64_t>(u.i);
}