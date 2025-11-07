#ifndef WASM_CONCRETE_RT_HPP
#define WASM_CONCRETE_RT_HPP

#include "wasm/profile.hpp"
#include "wasm/utils.hpp"
#include <cassert>
#include <cstdint>
#include <cstdio>
#include <iostream>
#include <memory>
#include <ostream>
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
};

static Num I32V(int v) { return v; }

static Num I64V(int64_t v) { return v; }

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

const int FRAME_SIZE = 1024;

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
  // TODO: We assign a SymVal to each byte in memory
  std::vector<uint8_t> memory;
  int init_page_count;
  int page_count;
  int allocated_pages;

  Memory_t(int32_t init_page_count)
      : memory(PRE_ALLOC_PAGES * pagesize), init_page_count(init_page_count),
        page_count(init_page_count), allocated_pages(PRE_ALLOC_PAGES) {}

  int32_t loadInt(int32_t base, int32_t offset) {
#ifdef DEBUG
    std::cout << "[Debug] loading int from memory at address: "
              << (base + offset) << std::endl;
#endif
    // just load a 4-byte integer from memory of the vector
    int32_t addr = base + offset;
    if (!(addr + 3 < memory.size())) {
      throw std::runtime_error("Invalid memory access " + std::to_string(addr));
    }
    int32_t result = 0;
    // Little-endian: lowest byte at lowest address
    for (int i = 0; i < 4; ++i) {
      result |= static_cast<int32_t>(memory[addr + i]) << (8 * i);
    }
    return result;
  }

  std::monostate storeInt(int32_t base, int32_t offset, int32_t value) {
    int32_t addr = base + offset;
#ifdef DEBUG
    std::cout << "[Debug] storing int " << value << " to memory at address "
              << addr << std::endl;
#endif
    // Ensure we don't write out of bounds
    if (!(addr + 3 < memory.size())) {
      throw std::runtime_error("Invalid memory access " + std::to_string(addr));
    }
    for (int i = 0; i < 4; ++i) {
      memory[addr + i] = static_cast<uint8_t>((value >> (8 * i)) & 0xFF);
      // Optionally, update memory[addr + i].second (SymVal) if needed
    }
    return std::monostate{};
  }

  std::monostate store_byte(int32_t addr, uint8_t value) {
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
};


static Memory_t Memory(1); // 1 page memory

#endif // WASM_CONCRETE_RT_HPP