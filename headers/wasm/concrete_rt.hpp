#ifndef WASM_CONCRETE_RT_HPP
#define WASM_CONCRETE_RT_HPP

#include "controls.hpp"
#include "wasm/profile.hpp"
#include "wasm/utils.hpp"
#include <cassert>
#include <cstdint>
#include <cstdio>
#include <iostream>
#include <memory>
#include <ostream>
#include <string>
#include <unistd.h>
#include <variant>
#include <vector>

struct Num {
  Num(int64_t value) : value(value) {}
  Num() : value(0) {}
  int64_t value;

  int32_t toInt() const { return static_cast<int32_t>(value); }
  uint32_t toUInt() const { return static_cast<uint32_t>(value); }

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

const int STACK_SIZE = 1024 * 64;

class Stack_t {
public:
  Stack_t() : count(0), stack_ptr(new Num[STACK_SIZE]) {
    size_t page_size = (size_t)sysconf(_SC_PAGESIZE);
    // pre touch the memory to avoid page faults during execution
    for (int i = 0; i < STACK_SIZE; i += page_size) {
      stack_ptr[i] = Num(0);
    }
  }

  std::monostate push(Num &&num) {
#ifdef DEBUG
    printf("[Debug] pushing a value %ld to stack, size of concrete stack is: "
           "%d\n",
           num.value, count);
#endif
    Profile.step(StepProfileKind::PUSH);
    stack_ptr[count] = num;
    count++;
    return std::monostate{};
  }

  std::monostate push(Num &num) {
    Profile.step(StepProfileKind::PUSH);
    stack_ptr[count] = num;
    count++;
    return std::monostate{};
  }

  Num pop() {
    Profile.step(StepProfileKind::POP);
#ifdef DEBUG
    assert(count > 0 && "Stack underflow");
    printf("[Debug] popping a value %ld from stack, size of concrete stack is: "
           "%d\n",
           stack_ptr[count - 1].value, count);
#endif
    Num num = stack_ptr[count - 1];
    count--;
    return num;
  }

  Num peek() {
    Profile.step(StepProfileKind::PEEK);
#ifdef DEBUG
    if (count == 0) {
      throw std::runtime_error("Stack underflow");
    }
#endif
    return stack_ptr[count - 1];
  }

  int32_t size() { return count; }

  void shift(int32_t offset, int32_t size) {
    Profile.step(StepProfileKind::SHIFT);
#ifdef DEBUG
    if (offset < 0) {
      throw std::out_of_range("Invalid offset: " + std::to_string(offset));
    }
    if (size < 0) {
      throw std::out_of_range("Invalid size: " + std::to_string(size));
    }
    std::cout << "Shifting stack by offset " << offset << " and size " << size
              << std::endl;
    std::cout << "Current stack size: " << count << std::endl;
#endif
    // shift last `size` of numbers forward of `offset`
    for (int32_t i = count - size; i < count; ++i) {
      assert(i - offset >= 0);
      stack_ptr[i - offset] = stack_ptr[i];
    }
    count -= offset;
  }

  void print() {
    std::cout << "Stack contents: " << std::endl;
    for (int32_t i = 0; i < count; ++i) {
      std::cout << stack_ptr[count - i - 1].value << std::endl;
    }
    std::cout << "End of Stack contents" << std::endl;
  }

  void initialize() {
    // todo: remove this method
    reset();
  }

  void reset() { count = 0; }

  void resize(int32_t new_size) {
    assert(new_size >= 0);
    count = new_size;
  }

  void set_from_front(int32_t index, const Num &num) {
    assert(index >= 0 && index < count);
    stack_ptr[index] = num;
  }

private:
  int32_t count;
  Num *stack_ptr;
};
static Stack_t Stack;

const int FRAME_SIZE = 1024 * 8;
class Frames_t {
public:
  Frames_t() : count(0), stack_ptr(new Num[FRAME_SIZE]) {
    size_t page_size = (size_t)sysconf(_SC_PAGESIZE);
    // pre touch the memory to avoid page faults during execution
    for (int i = 0; i < FRAME_SIZE; i += page_size) {
      stack_ptr[i] = Num(0);
    }
  }

  std::monostate popFrame(std::int32_t size) {
    assert(size >= 0);
    count -= size;
    return std::monostate{};
  }

  Num get(std::int32_t index) {
    Profile.step(StepProfileKind::GET);
    auto ret = stack_ptr[count - 1 - index];
    return ret;
  }

  void set(std::int32_t index, Num num) {
    Profile.step(StepProfileKind::SET);
    stack_ptr[count - 1 - index] = num;
  }

  void pushFrame(std::int32_t size) {
    assert(size >= 0);
    count += size;
    // Zero-initialize the new stack frames.
    for (std::int32_t i = 0; i < size; ++i) {
      stack_ptr[count - 1 - i] = Num(0);
    }
  }

  void reset() { count = 0; }

  size_t size() const { return count; }

  void set_from_front(int32_t index, const Num &num) {
    assert(index >= 0 && index < count && "Index out of bounds");
    stack_ptr[index] = num;
  }

  void resize(int32_t new_size) {
    assert(new_size >= 0);
    count = new_size;
  }

private:
  int32_t count;
  Num *stack_ptr;
};

static Frames_t Frames;
static Frames_t Globals;

static void initRand() {
  // for now, just do nothing
}

static std::monostate unreachable() {
  std::cout << "Unreachable code reached!" << std::endl;
  throw std::runtime_error("Unreachable code reached");
}

static const int PRE_ALLOC_PAGES = 20;
static int32_t pagesize = 65536;
static int32_t page_count = 0;

struct Memory_t {

  Memory_t(int32_t init_page_count)
      : memory(PRE_ALLOC_PAGES * pagesize), init_page_count(init_page_count),
        page_count(init_page_count), allocated_pages(PRE_ALLOC_PAGES) {
    reset();
  }

  int32_t loadInt(int32_t base, int32_t offset) {
    int32_t addr = base + offset;
    if (!(addr + 3 < memory.size()) || addr < 0) {
      throw std::runtime_error("Invalid memory access " + std::to_string(addr));
    }
    int32_t result = 0;
    // Little-endian: lowest byte at lowest address
    for (int i = 0; i < 4; ++i) {
      result |= static_cast<int32_t>(memory[addr + i]) << (8 * i);
    }
#ifdef DEBUG
    std::cout << "[Debug] loading int " << result << " from memory at address "
              << addr << std::endl;

#endif
    // just load a 4-byte integer from memory of the vector
    return result;
  }

  std::monostate storeInt(int32_t base, int32_t offset, int32_t value) {
    int32_t addr = base + offset;
    // Ensure we don't write out of bounds
    if (!(addr + 3 < memory.size())) {
      throw std::runtime_error("Invalid memory access " + std::to_string(addr));
    }
    for (int i = 0; i < 4; ++i) {
      memory[addr + i] = static_cast<uint8_t>((value >> (8 * i)) & 0xFF);
    }
#ifdef DEBUG
    std::cout << "[Debug] storing int " << value << " to memory at address "
              << addr << std::endl;
#endif
    return std::monostate{};
  }

  std::monostate store_byte(int32_t addr, uint8_t value) {
#ifdef DEBUG
    std::cout << "[Debug] storing byte " << std::to_string(value)
              << " to memory at address " << addr << std::endl;
#endif
    assert(addr < memory.size());
    memory[addr] = value;
    return std::monostate{};
  }

  // grow memory by delta bytes when bytes > 0. return -1 if failed, return old
  // size when success
  int32_t grow(int32_t delta) {
    Profile.step(StepProfileKind::MEM_GROW);
    if (delta <= 0) {
      return page_count * pagesize;
    }

    if (page_count + delta < allocated_pages) {
      page_count += delta;
      return page_count * pagesize;
    }

    try {
      assert(false && "Use pre-allocated memory, should not reach here");
      memory.resize(memory.size() + delta * pagesize);
      auto old_page_count = page_count;
      page_count += delta;
      return memory.size();
    } catch (const std::bad_alloc &e) {
      return -1;
    }
  }

  void reset() {
    page_count = init_page_count;
    allocated_pages = PRE_ALLOC_PAGES;
    for (int i = 0; i < memory.size() && i < page_count * pagesize; ++i) {
      memory[i] = 0;
    }
  }

private:
  std::vector<uint8_t> memory;
  int init_page_count;
  int page_count;
  int allocated_pages;
};

static Memory_t Memory(4); // 4 page memory

struct FuncTable_t {
  FuncTable_t() : table(20) {}
  std::vector<Func_t> table;

  Func_t read(int32_t index) {
    if (index < 0 || index >= table.size()) {
      throw std::runtime_error("Function table read out of bounds: " +
                               std::to_string(index));
    }
    if (!table[index]) {
      assert(false);
      throw std::runtime_error("Function table entry at index " +
                               std::to_string(index) + " is empty or invalid");
    }
    return table[index];
  }

  std::monostate set(Num offset, int32_t index, Func_t func) {
    if (index < 0 || index >= table.size()) {
      throw std::runtime_error("Function table set out of bounds: " +
                               std::to_string(index));
    }
    table[offset.toInt() + index] = func;
    return std::monostate{};
  }
};

static FuncTable_t FuncTable;

#endif // WASM_CONCRETE_RT_HPP