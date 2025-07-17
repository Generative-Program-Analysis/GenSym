#ifndef WASM_CONCRETE_RT_HPP
#define WASM_CONCRETE_RT_HPP

#include <cassert>
#include <cstdint>
#include <cstdio>
#include <iostream>
#include <memory>
#include <ostream>
#include <variant>
#include <vector>

void info() {
#ifdef DEBUG
  std::cout << std::endl;
#endif
}

template <typename T, typename... Args>
void info(const T &first, const Args &...args) {
#ifdef DEBUG
  std::cout << first << " ";
  info(args...);
#endif
}

struct Num {
  Num(int64_t value) : value(value) {}
  Num() : value(0) {}
  int64_t value;
  int32_t toInt() { return static_cast<int32_t>(value); }

  bool operator==(const Num &other) const { return value == other.value; }
  bool operator!=(const Num &other) const { return !(*this == other); }
  Num operator+(const Num &other) const { return Num(value + other.value); }
  Num operator-(const Num &other) const { return Num(value - other.value); }
  Num operator*(const Num &other) const { return Num(value * other.value); }
  Num operator/(const Num &other) const {
    if (other.value == 0) {
      throw std::runtime_error("Division by zero");
    }
    return Num(value / other.value);
  }
  Num operator<(const Num &other) const { return Num(value < other.value); }
  Num operator<=(const Num &other) const { return Num(value <= other.value); }
  Num operator>(const Num &other) const { return Num(value > other.value); }
  Num operator>=(const Num &other) const { return Num(value >= other.value); }
  Num operator&(const Num &other) const { return Num(value & other.value); }
};

static Num I32V(int v) { return v; }

static Num I64V(int64_t v) { return v; }

const int STACK_SIZE = 1024 * 64;

class Stack_t {
public:
  Stack_t() : count(0), stack_ptr(new Num[STACK_SIZE]) {}

  std::monostate push(Num &&num) {
    stack_ptr[count] = num;
    count++;
    return std::monostate{};
  }

  std::monostate push(Num &num) {
    stack_ptr[count] = num;
    count++;
    return std::monostate{};
  }

  Num pop() {
#ifdef DEBUG
    if (count == 0) {
      throw std::runtime_error("Stack underflow");
    }
#endif
    Num num = stack_ptr[count - 1];
    count--;
    return num;
  }

  Num peek() {
#ifdef DEBUG
    if (count == 0) {
      throw std::runtime_error("Stack underflow");
    }
#endif
    return stack_ptr[count - 1];
  }

  int32_t size() { return count; }

  void shift(int32_t offset, int32_t size) {
#ifdef DEBUG
    if (offset < 0) {
      throw std::out_of_range("Invalid offset: " + std::to_string(offset));
    }
    if (size < 0) {
      throw std::out_of_range("Invalid size: " + std::to_string(size));
    }
#endif
    // shift last `size` of numbers forward of `offset`
    for (int32_t i = count - size; i < count; ++i) {
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

private:
  int32_t count;
  Num *stack_ptr;
};
static Stack_t Stack;

const int FRAME_SIZE = 1024;

class Frames_t {
public:
  Frames_t() : count(0), stack_ptr(new Num[FRAME_SIZE]) {}

  std::monostate popFrame(std::int32_t size) {
    assert(size >= 0);
    count -= size;
    return std::monostate{};
  }

  Num get(std::int32_t index) {
    auto ret = stack_ptr[count - 1 - index];
    return ret;
  }

  void set(std::int32_t index, Num num) { stack_ptr[count - 1 - index] = num; }

  void pushFrame(std::int32_t size) {
    assert(size >= 0);
    count += size;
  }

  void reset() { count = 0; }

private:
  int32_t count;
  Num *stack_ptr;
};

static Frames_t Frames;

static void initRand() {
  // for now, just do nothing
}

static std::monostate unreachable() {
  std::cout << "Unreachable code reached!" << std::endl;
  throw std::runtime_error("Unreachable code reached");
}

static int32_t pagesize = 65536;
static int32_t page_count = 0;

struct Memory_t {
  std::vector<uint8_t> memory;
  Memory_t(int32_t init_page_count) : memory(init_page_count * pagesize) {}

  int32_t loadInt(int32_t base, int32_t offset) {
    return *reinterpret_cast<int32_t *>(static_cast<uint8_t *>(memory.data()) +
                                        base + offset);
  }

  std::monostate storeInt(int32_t base, int32_t offset, int32_t value) {
    *reinterpret_cast<int32_t *>(static_cast<uint8_t *>(memory.data()) + base +
                                 offset) = value;
    return std::monostate{};
  }

  // grow memory by delta bytes when bytes > 0. return -1 if failed, return old
  // size when success
  int32_t grow(int32_t delta) {
    if (delta <= 0) {
      return memory.size();
    }

    try {
      memory.resize(memory.size() + delta * pagesize);
      auto old_page_count = page_count;
      page_count += delta;
      return memory.size();
    } catch (const std::bad_alloc &e) {
      return -1;
    }
  }
};

static Memory_t Memory(1); // 1 page memory

#endif // WASM_CONCRETE_RT_HPP