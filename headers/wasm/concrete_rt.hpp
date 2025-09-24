#ifndef WASM_CONCRETE_RT_HPP
#define WASM_CONCRETE_RT_HPP

#include "wasm/utils.hpp"
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
    assert(count > 0 && "Stack underflow");
    printf("[Debug] poping from stack, size of concrete stack is: %d\n", count);
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

static int32_t pagesize = 65536;
static int32_t page_count = 0;

#endif // WASM_CONCRETE_RT_HPP