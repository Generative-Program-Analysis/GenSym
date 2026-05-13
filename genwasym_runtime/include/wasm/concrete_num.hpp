#ifndef WASM_CONCRETE_NUM_HPP
#define WASM_CONCRETE_NUM_HPP
#include "wasm/profile.hpp"
#include "wasm/utils.hpp"
#include <cmath>
#include <cstdint>

struct Num {
  Num(int64_t value) : value(value) {}
  Num() : value(0) {}
  int64_t value;

  int32_t toInt() const { return static_cast<int32_t>(value); }
  uint32_t toUInt() const { return static_cast<uint32_t>(value); }
  int64_t toInt64() const { return static_cast<int64_t>(value); }
  uint64_t toUInt64() const { return static_cast<uint64_t>(value); }
  float toF32() const { return *reinterpret_cast<const float *>(&value); }
  double toF64() const { return *reinterpret_cast<const double *>(&value); }

  // debug printer: enabled only when -DDEBUG
  static inline void debug_print(const char *op, const Num &a, const Num &b,
                                 const Num &res) {
#ifdef DEBUG_OP
    std::cout << "[Debug] " << op << ": lhs=" << static_cast<int32_t>(a.value)
              << " rhs=" << static_cast<int32_t>(b.value)
              << " -> res=" << static_cast<int32_t>(res.value) << std::endl;
#endif
  }

  // Helper to create a Wasm Boolean result (1 or 0 as Num)
  Num WasmBool(bool condition) const {
    Num res(condition ? 1 : 0);
    debug_print("WasmBool", *this, *this, res);
    return res;
  }
  // TODO: support different bit width operations, for now we just assume all
  // oprands are i32
  // i32.eq (Equals): *this == other
  inline Num i32_eq(const Num &other) const {
    Num res = WasmBool(this->toUInt() == other.toUInt());
    debug_print("i32.eq", *this, other, res);
    return res;
  }

  // i32.ne (Not Equals): *this != other
  inline Num i32_ne(const Num &other) const {
    Num res = WasmBool(this->toUInt() != other.toUInt());
    debug_print("i32.ne", *this, other, res);
    return res;
  }

  // i32.lt_s (Signed Less Than): *this < other
  inline Num i32_lt_s(const Num &other) const {
    Num res = WasmBool(this->toInt() < other.toInt());
    debug_print("i32.lt_s", *this, other, res);
    return res;
  }

  // i32.lt_u (Unsigned Less Than): *this < other (unsigned)
  inline Num i32_lt_u(const Num &other) const {
    Num res = WasmBool(this->toUInt() < other.toUInt());
    debug_print("i32.lt_u", *this, other, res);
    return res;
  }

  // i32.le_s (Signed Less Than or Equal): *this <= other
  inline Num i32_le_s(const Num &other) const {
    Num res = WasmBool(this->toInt() <= other.toInt());
    debug_print("i32.le_s", *this, other, res);
    return res;
  }
  // i32.le_u (Unsigned Less Than or Equal): *this <= other (unsigned)
  inline Num i32_le_u(const Num &other) const {
    Num res = WasmBool(this->toUInt() <= other.toUInt());
    debug_print("i32.le_u", *this, other, res);
    return res;
  }

  // i32.gt_s (Signed Greater Than): *this > other
  inline Num i32_gt_s(const Num &other) const {
    Num res = WasmBool(this->toInt() > other.toInt());
    debug_print("i32.gt_s", *this, other, res);
    return res;
  }

  // i32.gt_u (Unsigned Greater Than): *this > other (unsigned)
  inline Num i32_gt_u(const Num &other) const {
    Num res = WasmBool(this->toUInt() > other.toUInt());
    debug_print("i32.gt_u", *this, other, res);
    return res;
  }

  // i32.ge_s (Signed Greater Than or Equal): *this >= other
  inline Num i32_ge_s(const Num &other) const {
    Num res = WasmBool(this->toInt() >= other.toInt());
    debug_print("i32.ge_s", *this, other, res);
    return res;
  }

  // i32.ge_u (Unsigned Greater Than or Equal): *this >= other (unsigned)
  inline Num i32_ge_u(const Num &other) const {
    Num res = WasmBool(this->toUInt() >= other.toUInt());
    debug_print("i32.ge_u", *this, other, res);
    return res;
  }

  // i32.add (Wrapping addition)
  inline Num i32_add(const Num &other) const {
    uint32_t result_u = this->toUInt() + other.toUInt();
    Num res(static_cast<int32_t>(result_u));
    debug_print("i32.add", *this, other, res);
    return res;
  }

  // i32.sub (Wrapping subtraction)
  inline Num i32_sub(const Num &other) const {
    uint32_t result_u = this->toUInt() - other.toUInt();
    Num res(static_cast<int32_t>(result_u));
    debug_print("i32.sub", *this, other, res);
    return res;
  }

  // i32.mul (Wrapping multiplication)
  inline Num i32_mul(const Num &other) const {
    uint32_t result_u = this->toUInt() * other.toUInt();
    Num res(static_cast<int32_t>(result_u));
    debug_print("i32.mul", *this, other, res);
    return res;
  }

  // i32.div_s (Signed division with traps)
  inline Num i32_div_s(const Num &other) const {
    int32_t divisor = other.toInt();
    int32_t dividend = this->toInt();

    if (divisor == 0) {
      throw std::runtime_error("i32.div_s: Division by zero");
    }

    Num res(dividend / divisor);
    debug_print("i32.div_s", *this, other, res);
    return res;
  }

  // i32.div_u (Unsigned division with traps)
  inline Num i32_div_u(const Num &other) const {
    uint32_t divisor = other.toUInt();
    uint32_t dividend = this->toUInt();
    if (divisor == 0) {
      throw std::runtime_error("i32.div_u: Division by zero");
    }
    Num res(static_cast<int32_t>(dividend / divisor));
    debug_print("i32.div_u", *this, other, res);
    return res;
  }

  // i32.rem_s (Signed remainder with traps on division by zero)
  inline Num i32_rem_s(const Num &other) const {
    int32_t divisor = other.toInt();
    int32_t dividend = this->toInt();
    if (divisor == 0) {
      throw std::runtime_error("i32.rem_s: Division by zero");
    }
    // WebAssembly defines INT_MIN % -1 == 0
    if (dividend == INT32_MIN && divisor == -1) {
      Num res(0);
      debug_print("i32.rem_s", *this, other, res);
      return res;
    }
    Num res(dividend % divisor);
    debug_print("i32.rem_s", *this, other, res);
    return res;
  }

  // i32.rem_u (Unsigned remainder with traps on division by zero)
  inline Num i32_rem_u(const Num &other) const {
    uint32_t divisor = other.toUInt();
    uint32_t dividend = this->toUInt();
    if (divisor == 0) {
      throw std::runtime_error("i32.rem_u: Division by zero");
    }
    Num res(static_cast<int32_t>(dividend % divisor));
    debug_print("i32.rem_u", *this, other, res);
    return res;
  }

  // i32.shl (Shift Left): *this << other (shift count masked by 31)
  inline Num i32_shl(const Num &other) const {
    uint32_t shift_amount = other.toUInt() & 0x1F;
    uint32_t result_u = toUInt() << shift_amount;
    Num res(static_cast<int32_t>(result_u));
    debug_print("i32.shl", *this, other, res);
    return res;
  }

  // i32.shr_s (Signed Shift Right): *this >> other (Arithmetic shift)
  inline Num i32_shr_s(const Num &other) const {
    // Wasm masks the shift amount by 31 (0x1F)
    uint32_t shift_amount = other.toUInt() & 0x1F;
    int32_t result_s = toInt() >> shift_amount;
    Num res(result_s);
    debug_print("i32.shr_s", *this, other, res);
    return res;
  }

  // i32.shr_u (Unsigned Shift Right): *this >>> other (Logical shift)
  inline Num i32_shr_u(const Num &other) const {
    // Wasm masks the shift amount by 31 (0x1F)
    uint32_t shift_amount = other.toUInt() & 0x1F;
    uint32_t result_u = toUInt() >> shift_amount;
    Num res(static_cast<int32_t>(result_u));
    debug_print("i32.shr_u", *this, other, res);
    return res;
  }

  // i32.and (Bitwise AND)
  inline Num i32_and(const Num &other) const {
    uint32_t result_u = this->toUInt() & other.toUInt();
    Num res(static_cast<int32_t>(result_u));
    debug_print("i32.and", *this, other, res);
    return res;
  }

  // i32.xor (Bitwise XOR)
  inline Num i32_xor(const Num &other) const {
    uint32_t result_u = this->toUInt() ^ other.toUInt();
    Num res(static_cast<int32_t>(result_u));
    debug_print("i32.xor", *this, other, res);
    return res;
  }

  inline Num i32_or(const Num &other) const {
    uint32_t result_u = this->toUInt() | other.toUInt();
    Num res(static_cast<int32_t>(result_u));
    debug_print("i32.or", *this, other, res);
    return res;
  }

  // i64.extend_i32_s: sign-extend low 32 bits to i64
  inline Num i32_extend_to_i64_s() const {
    int64_t result_s = static_cast<int64_t>(this->toInt());
    Num res(result_s);
    debug_print("i32.extend_to_i64_s", *this, *this, res);
    return res;
  }

  // i64.extend_i32_u: zero-extend low 32 bits to i64
  inline Num i32_extend_to_i64_u() const {
    uint64_t result_u = static_cast<uint64_t>(this->toUInt());
    Num res(static_cast<int64_t>(result_u));
    debug_print("i32.extend_to_i64_u", *this, *this, res);
    return res;
  }

  // i64.eq (Equals): *this == other
  inline Num i64_eq(const Num &other) const {
    Num res = WasmBool(this->toUInt64() == other.toUInt64());
    debug_print("i64.eq", *this, other, res);
    return res;
  }

  // i64.ne (Not Equals): *this != other
  inline Num i64_ne(const Num &other) const {
    Num res = WasmBool(this->toUInt64() != other.toUInt64());
    debug_print("i64.ne", *this, other, res);
    return res;
  }

  // i64.lt_s (Signed Less Than): *this < other
  inline Num i64_lt_s(const Num &other) const {
    Num res = WasmBool(this->toInt64() < other.toInt64());
    debug_print("i64.lt_s", *this, other, res);
    return res;
  }

  // i64.lt_u (Unsigned Less Than): *this < other (unsigned)
  inline Num i64_lt_u(const Num &other) const {
    Num res = WasmBool(this->toUInt64() < other.toUInt64());
    debug_print("i64.lt_u", *this, other, res);
    return res;
  }

  // i64.le_s (Signed Less Than or Equal): *this <= other
  inline Num i64_le_s(const Num &other) const {
    Num res = WasmBool(this->toInt64() <= other.toInt64());
    debug_print("i64.le_s", *this, other, res);
    return res;
  }

  // i64.le_u (Unsigned Less Than or Equal): *this <= other (unsigned)
  inline Num i64_le_u(const Num &other) const {
    Num res = WasmBool(this->toUInt64() <= other.toUInt64());
    debug_print("i64.le_u", *this, other, res);
    return res;
  }

  // i64.gt_s (Signed Greater Than): *this > other
  inline Num i64_gt_s(const Num &other) const {
    Num res = WasmBool(this->toInt64() > other.toInt64());
    debug_print("i64.gt_s", *this, other, res);
    return res;
  }

  // i64.gt_u (Unsigned Greater Than): *this > other (unsigned)
  inline Num i64_gt_u(const Num &other) const {
    Num res = WasmBool(this->toUInt64() > other.toUInt64());
    debug_print("i64.gt_u", *this, other, res);
    return res;
  }

  // i64.ge_s (Signed Greater Than or Equal): *this >= other
  inline Num i64_ge_s(const Num &other) const {
    Num res = WasmBool(this->toInt64() >= other.toInt64());
    debug_print("i64.ge_s", *this, other, res);
    return res;
  }

  // i64.ge_u (Unsigned Greater Than or Equal): *this >= other (unsigned)
  inline Num i64_ge_u(const Num &other) const {
    Num res = WasmBool(this->toUInt64() >= other.toUInt64());
    debug_print("i64.ge_u", *this, other, res);
    return res;
  }

  // i64.add (Wrapping addition)
  inline Num i64_add(const Num &other) const {
    uint64_t result_u = this->toUInt64() + other.toUInt64();
    Num res(static_cast<int64_t>(result_u));
    debug_print("i64.add", *this, other, res);
    return res;
  }

  // i64.sub (Wrapping subtraction)
  inline Num i64_sub(const Num &other) const {
    uint64_t result_u = this->toUInt64() - other.toUInt64();
    Num res(static_cast<int64_t>(result_u));
    debug_print("i64.sub", *this, other, res);
    return res;
  }

  // i64.mul (Wrapping multiplication)
  inline Num i64_mul(const Num &other) const {
    uint64_t result_u = this->toUInt64() * other.toUInt64();
    Num res(static_cast<int64_t>(result_u));
    debug_print("i64.mul", *this, other, res);
    return res;
  }

  // i64.div_s (Signed division with traps)
  inline Num i64_div_s(const Num &other) const {
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

  // i64.div_u (Unsigned division with traps)
  inline Num i64_div_u(const Num &other) const {
    uint64_t divisor = other.toUInt64();
    uint64_t dividend = this->toUInt64();
    if (divisor == 0) {
      throw std::runtime_error("i64.div_u: Division by zero");
    }
    Num res(static_cast<int64_t>(dividend / divisor));
    debug_print("i64.div_u", *this, other, res);
    return res;
  }

  // i64.rem_s (Signed remainder with traps on division by zero)
  inline Num i64_rem_s(const Num &other) const {
    int64_t divisor = other.toInt64();
    int64_t dividend = this->toInt64();
    if (divisor == 0) {
      throw std::runtime_error("i64.rem_s: Division by zero");
    }
    // WebAssembly defines INT64_MIN % -1 == 0
    if (dividend == INT64_MIN && divisor == -1) {
      Num res(0);
      debug_print("i64.rem_s", *this, other, res);
      return res;
    }
    Num res(dividend % divisor);
    debug_print("i64.rem_s", *this, other, res);
    return res;
  }

  // i64.rem_u (Unsigned remainder with traps on division by zero)
  inline Num i64_rem_u(const Num &other) const {
    uint64_t divisor = other.toUInt64();
    uint64_t dividend = this->toUInt64();
    if (divisor == 0) {
      throw std::runtime_error("i64.rem_u: Division by zero");
    }
    Num res(static_cast<int64_t>(dividend % divisor));
    debug_print("i64.rem_u", *this, other, res);
    return res;
  }

  // i64.shl (Shift Left): *this << other (shift count masked by 63)
  inline Num i64_shl(const Num &other) const {
    uint64_t shift_amount = other.toUInt64() & 0x3F;
    uint64_t result_u = toUInt64() << shift_amount;
    Num res(static_cast<int64_t>(result_u));
    debug_print("i64.shl", *this, other, res);
    return res;
  }

  // i64.shr_s (Signed Shift Right): *this >> other (Arithmetic shift)
  inline Num i64_shr_s(const Num &other) const {
    uint64_t shift_amount = other.toUInt64() & 0x3F;
    int64_t result_s = toInt64() >> shift_amount;
    Num res(result_s);
    debug_print("i64.shr_s", *this, other, res);
    return res;
  }

  // i64.shr_u (Unsigned Shift Right): *this >>> other (Logical shift)
  inline Num i64_shr_u(const Num &other) const {
    uint64_t shift_amount = other.toUInt64() & 0x3F;
    uint64_t result_u = toUInt64() >> shift_amount;
    Num res(static_cast<int64_t>(result_u));
    debug_print("i64.shr_u", *this, other, res);
    return res;
  }

  // i64.and (Bitwise AND)
  inline Num i64_and(const Num &other) const {
    uint64_t result_u = this->toUInt64() & other.toUInt64();
    Num res(static_cast<int64_t>(result_u));
    debug_print("i64.and", *this, other, res);
    return res;
  }

  // i64.xor (Bitwise XOR)
  inline Num i64_xor(const Num &other) const {
    uint64_t result_u = this->toUInt64() ^ other.toUInt64();
    Num res(static_cast<int64_t>(result_u));
    debug_print("i64.xor", *this, other, res);
    return res;
  }

  // i64.or (Bitwise OR)
  inline Num i64_or(const Num &other) const {
    uint64_t result_u = this->toUInt64() | other.toUInt64();
    Num res(static_cast<int64_t>(result_u));
    debug_print("i64.or", *this, other, res);
    return res;
  }

  // f32 helpers: interpret low 32 bits of value as IEEE-754 float
  static inline float f32_from_bits(uint32_t bits) {
    union {
      uint32_t i;
      float f;
    } u;
    u.i = bits;
    return u.f;
  }
  static inline uint32_t f32_to_bits(float f) {
    union {
      uint32_t i;
      float f;
    } u;
    u.f = f;
    return u.i;
  }
  static inline bool f32_is_nan(uint32_t bits) {
    // Exponent all ones and mantissa non-zero -> NaN for IEEE-754 single
    return (bits & 0x7F800000u) == 0x7F800000u && (bits & 0x007FFFFFu) != 0;
  }

  // f32.add
  inline Num f32_add(const Num &other) const {
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

  // f32.sub
  inline Num f32_sub(const Num &other) const {
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

  // f32.mul
  inline Num f32_mul(const Num &other) const {
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

  // f32.div
  inline Num f32_div(const Num &other) const {
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

  // f32.eq : false if either is NaN
  inline Num f32_eq(const Num &other) const {
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

  // f32.ne : true if values are unordered or not equal (i.e., NaN makes it
  // true)
  inline Num f32_ne(const Num &other) const {
    uint32_t a_bits = toUInt();
    uint32_t b_bits = other.toUInt();
    // per wasm: if either is NaN, f32.ne is true
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

  // ordered comparisons: return false if any operand is NaN
  inline Num f32_lt(const Num &other) const {
    uint32_t a_bits = toUInt(), b_bits = other.toUInt();
    if (f32_is_nan(a_bits) || f32_is_nan(b_bits))
      return WasmBool(false);
    float a = f32_from_bits(a_bits), b = f32_from_bits(b_bits);
    Num res = WasmBool(a < b);
    debug_print("f32.lt", *this, other, res);
    return res;
  }
  inline Num f32_le(const Num &other) const {
    uint32_t a_bits = toUInt(), b_bits = other.toUInt();
    if (f32_is_nan(a_bits) || f32_is_nan(b_bits))
      return WasmBool(false);
    float a = f32_from_bits(a_bits), b = f32_from_bits(b_bits);
    Num res = WasmBool(a <= b);
    debug_print("f32.le", *this, other, res);
    return res;
  }
  inline Num f32_gt(const Num &other) const {
    uint32_t a_bits = toUInt(), b_bits = other.toUInt();
    if (f32_is_nan(a_bits) || f32_is_nan(b_bits))
      return WasmBool(false);
    float a = f32_from_bits(a_bits), b = f32_from_bits(b_bits);
    Num res = WasmBool(a > b);
    debug_print("f32.gt", *this, other, res);
    return res;
  }
  inline Num f32_ge(const Num &other) const {
    uint32_t a_bits = toUInt(), b_bits = other.toUInt();
    if (f32_is_nan(a_bits) || f32_is_nan(b_bits))
      return WasmBool(false);
    float a = f32_from_bits(a_bits), b = f32_from_bits(b_bits);
    Num res = WasmBool(a >= b);
    debug_print("f32.ge", *this, other, res);
    return res;
  }

  // f32.abs: clear sign bit
  inline Num f32_abs() const {
    uint32_t a_bits = toUInt();
    uint32_t r_bits = a_bits & 0x7FFFFFFFu;
    Num res(static_cast<int32_t>(r_bits));
    debug_print("f32.abs", *this, *this, res);
    return res;
  }

  // f32.neg: flip sign bit
  inline Num f32_neg() const {
    uint32_t a_bits = toUInt();
    uint32_t r_bits = a_bits ^ 0x80000000u;
    Num res(static_cast<int32_t>(r_bits));
    debug_print("f32.neg", *this, *this, res);
    return res;
  }

  inline Num convert_i32_to_f32_s() const {
    uint32_t r_bits = f32_to_bits(static_cast<float>(toInt()));
    return Num(static_cast<int32_t>(r_bits));
  }

  inline Num convert_i32_to_f32_u() const {
    uint32_t r_bits = f32_to_bits(static_cast<float>(toUInt()));
    return Num(static_cast<int32_t>(r_bits));
  }

  inline Num convert_i64_to_f32_s() const {
    uint32_t r_bits = f32_to_bits(static_cast<float>(toInt64()));
    return Num(static_cast<int32_t>(r_bits));
  }

  inline Num convert_i64_to_f32_u() const {
    uint32_t r_bits = f32_to_bits(static_cast<float>(toUInt64()));
    return Num(static_cast<int32_t>(r_bits));
  }

  // f32.min / f32.max: follow wasm-ish semantics: if either is NaN, return NaN
  // (propagate)
  inline Num f32_min(const Num &other) const {
    uint32_t a_bits = toUInt(), b_bits = other.toUInt();
    if (f32_is_nan(a_bits))
      return Num(static_cast<int32_t>(a_bits));
    if (f32_is_nan(b_bits))
      return Num(static_cast<int32_t>(b_bits));
    float a = f32_from_bits(a_bits), b = f32_from_bits(b_bits);
    // If values compare equal choose one to preserve signed zero: pick the one
    // whose sign bit is set for min when both zeros (so -0 wins for min).
    if (a == b) {
      if ((a_bits & 0x80000000u) || (b_bits & 0x80000000u))
        return Num(
            static_cast<int32_t>((a_bits & 0x80000000u) ? a_bits : b_bits));
      return Num(static_cast<int32_t>(a_bits));
    }
    float r = (a < b) ? a : b;
    uint32_t r_bits = f32_to_bits(r);
    Num res(static_cast<int32_t>(r_bits));
    debug_print("f32.min", *this, other, res);
    return res;
  }

  inline Num f32_max(const Num &other) const {
    uint32_t a_bits = toUInt(), b_bits = other.toUInt();
    if (f32_is_nan(a_bits))
      return Num(static_cast<int32_t>(a_bits));
    if (f32_is_nan(b_bits))
      return Num(static_cast<int32_t>(b_bits));
    float a = f32_from_bits(a_bits), b = f32_from_bits(b_bits);
    if (a == b) {
      if ((a_bits & 0x80000000u) || (b_bits & 0x80000000u))
        return Num(
            static_cast<int32_t>((a_bits & 0x80000000u) ? b_bits : a_bits));
      return Num(static_cast<int32_t>(a_bits));
    }
    float r = (a > b) ? a : b;
    uint32_t r_bits = f32_to_bits(r);
    Num res(static_cast<int32_t>(r_bits));
    debug_print("f32.max", *this, other, res);
    return res;
  }

  // f32.copysign: result has magnitude of lhs, sign of rhs
  inline Num f32_copysign(const Num &other) const {
    uint32_t a_bits = toUInt(), b_bits = other.toUInt();
    uint32_t r_bits = (a_bits & 0x7FFFFFFFu) | (b_bits & 0x80000000u);
    Num res(static_cast<int32_t>(r_bits));
    debug_print("f32.copysign", *this, other, res);
    return res;
  }

  // f64 helpers: interpret all 64 bits of value as IEEE-754 double
  static inline double f64_from_bits(uint64_t bits) {
    union {
      uint64_t i;
      double d;
    } u;
    u.i = bits;
    return u.d;
  }
  static inline uint64_t f64_to_bits(double d) {
    union {
      uint64_t i;
      double d;
    } u;
    u.d = d;
    return u.i;
  }
  static inline bool f64_is_nan(uint64_t bits) {
    // Exponent all ones and mantissa non-zero -> NaN for IEEE-754 double
    return (bits & 0x7FF0000000000000ull) == 0x7FF0000000000000ull &&
           (bits & 0x000FFFFFFFFFFFFFull) != 0;
  }

  // f64.add
  inline Num f64_add(const Num &other) const {
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

  // f64.sub
  inline Num f64_sub(const Num &other) const {
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

  // f64.mul
  inline Num f64_mul(const Num &other) const {
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

  // f64.div
  inline Num f64_div(const Num &other) const {
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

  // f64.eq : false if either is NaN
  inline Num f64_eq(const Num &other) const {
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

  // f64.ne : true if values are unordered or not equal (i.e., NaN makes it
  // true)
  inline Num f64_ne(const Num &other) const {
    uint64_t a_bits = toUInt64();
    uint64_t b_bits = other.toUInt64();
    // per wasm: if either is NaN, f64.ne is true
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

  // ordered comparisons: return false if any operand is NaN
  inline Num f64_lt(const Num &other) const {
    uint64_t a_bits = toUInt64(), b_bits = other.toUInt64();
    if (f64_is_nan(a_bits) || f64_is_nan(b_bits))
      return WasmBool(false);
    double a = f64_from_bits(a_bits), b = f64_from_bits(b_bits);
    Num res = WasmBool(a < b);
    debug_print("f64.lt", *this, other, res);
    return res;
  }
  inline Num f64_le(const Num &other) const {
    uint64_t a_bits = toUInt64(), b_bits = other.toUInt64();
    if (f64_is_nan(a_bits) || f64_is_nan(b_bits))
      return WasmBool(false);
    double a = f64_from_bits(a_bits), b = f64_from_bits(b_bits);
    Num res = WasmBool(a <= b);
    debug_print("f64.le", *this, other, res);
    return res;
  }
  inline Num f64_gt(const Num &other) const {
    uint64_t a_bits = toUInt64(), b_bits = other.toUInt64();
    if (f64_is_nan(a_bits) || f64_is_nan(b_bits))
      return WasmBool(false);
    double a = f64_from_bits(a_bits), b = f64_from_bits(b_bits);
    Num res = WasmBool(a > b);
    debug_print("f64.gt", *this, other, res);
    return res;
  }
  inline Num f64_ge(const Num &other) const {
    uint64_t a_bits = toUInt64(), b_bits = other.toUInt64();
    if (f64_is_nan(a_bits) || f64_is_nan(b_bits))
      return WasmBool(false);
    double a = f64_from_bits(a_bits), b = f64_from_bits(b_bits);
    Num res = WasmBool(a >= b);
    debug_print("f64.ge", *this, other, res);
    return res;
  }

  // f64.abs: clear sign bit
  inline Num f64_abs() const {
    uint64_t a_bits = toUInt64();
    uint64_t r_bits = a_bits & 0x7FFFFFFFFFFFFFFFull;
    Num res(static_cast<int64_t>(r_bits));
    debug_print("f64.abs", *this, *this, res);
    return res;
  }

  // f64.neg: flip sign bit
  inline Num f64_neg() const {
    uint64_t a_bits = toUInt64();
    uint64_t r_bits = a_bits ^ 0x8000000000000000ull;
    Num res(static_cast<int64_t>(r_bits));
    debug_print("f64.neg", *this, *this, res);
    return res;
  }

  inline Num convert_i32_to_f64_s() const {
    uint64_t r_bits = f64_to_bits(static_cast<double>(toInt()));
    return Num(static_cast<int64_t>(r_bits));
  }

  inline Num convert_i32_to_f64_u() const {
    uint64_t r_bits = f64_to_bits(static_cast<double>(toUInt()));
    return Num(static_cast<int64_t>(r_bits));
  }

  inline Num convert_i64_to_f64_s() const {
    uint64_t r_bits = f64_to_bits(static_cast<double>(toInt64()));
    return Num(static_cast<int64_t>(r_bits));
  }

  inline Num convert_i64_to_f64_u() const {
    uint64_t r_bits = f64_to_bits(static_cast<double>(toUInt64()));
    return Num(static_cast<int64_t>(r_bits));
  }

  inline Num trunc_f64_to_i32_u() const {
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

  // f64.min / f64.max: follow wasm-ish semantics: if either is NaN, return
  // NaN (propagate)
  inline Num f64_min(const Num &other) const {
    uint64_t a_bits = toUInt64(), b_bits = other.toUInt64();
    if (f64_is_nan(a_bits))
      return Num(static_cast<int64_t>(a_bits));
    if (f64_is_nan(b_bits))
      return Num(static_cast<int64_t>(b_bits));
    double a = f64_from_bits(a_bits), b = f64_from_bits(b_bits);
    // If values compare equal choose one to preserve signed zero: pick the one
    // whose sign bit is set for min when both zeros (so -0 wins for min).
    if (a == b) {
      if ((a_bits & 0x8000000000000000ull) || (b_bits & 0x8000000000000000ull))
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

  inline Num f64_max(const Num &other) const {
    uint64_t a_bits = toUInt64(), b_bits = other.toUInt64();
    if (f64_is_nan(a_bits))
      return Num(static_cast<int64_t>(a_bits));
    if (f64_is_nan(b_bits))
      return Num(static_cast<int64_t>(b_bits));
    double a = f64_from_bits(a_bits), b = f64_from_bits(b_bits);
    if (a == b) {
      if ((a_bits & 0x8000000000000000ull) || (b_bits & 0x8000000000000000ull))
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

  // f64.copysign: result has magnitude of lhs, sign of rhs
  inline Num f64_copysign(const Num &other) const {
    uint64_t a_bits = toUInt64(), b_bits = other.toUInt64();
    uint64_t r_bits =
        (a_bits & 0x7FFFFFFFFFFFFFFFull) | (b_bits & 0x8000000000000000ull);
    Num res(static_cast<int64_t>(r_bits));
    debug_print("f64.copysign", *this, other, res);
    return res;
  }

  // logic and
  inline bool logical_and(const Num &other) const {
    return (this->toUInt() != 0) && (other.toUInt() != 0);
  }

  // logic or
  inline bool logical_or(const Num &other) const {
    return (this->toUInt() != 0) || (other.toUInt() != 0);
  }
};

static Num I32V(int v) { return v; }

static Num I64V(int64_t v) { return v; }

static Num F32V(float f) {
  union {
    uint32_t i;
    float f;
  } u;
  u.f = f;
  return static_cast<int32_t>(u.i);
}

static Num F64V(double d) {
  union {
    uint64_t i;
    double d;
  } u;
  u.d = d;
  return static_cast<int64_t>(u.i);
}

#endif // WASM_CONCRETE_NUM_HPP
