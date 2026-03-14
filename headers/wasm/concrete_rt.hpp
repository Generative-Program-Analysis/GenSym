#ifndef WASM_CONCRETE_RT_HPP
#define WASM_CONCRETE_RT_HPP

#include "concrete_num.hpp"
#include "controls.hpp"
#include "immer/vector_transient.hpp"
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
    assert(count > 0 && "Stack underflow");
#ifdef DEBUG
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
class SymFrames_t;

const int FRAME_SIZE = 1024 * 8;
class Frames_t {
public:
  Frames_t() : count(0), stack_ptr(new Num[FRAME_SIZE]), frame_ptrs() {
    size_t page_size = (size_t)sysconf(_SC_PAGESIZE);
    // pre touch the memory to avoid page faults during execution
    for (int i = 0; i < FRAME_SIZE; i += page_size) {
      stack_ptr[i] = Num(0);
    }
  }

  std::monostate popFrameCaller(std::int32_t size) {
    assert(size >= 0);
    assert(size <= count);
    assert(!frame_ptrs.empty());
    auto frame_base = current_frame_base();
    assert(frame_base + size == count);
    count -= size;
#ifdef USE_IMM
    frame_ptrs.take(frame_ptrs.size() - 1);
#else
    frame_ptrs.pop_back();
#endif
    return std::monostate{};
  }

  std::monostate popFrameCallee(std::int32_t size) {
    assert(size >= 0);
    assert(size <= count);
    count -= size;
    return std::monostate{};
  }

  Num get(std::int32_t index) {
    assert(!frame_ptrs.empty() && "No active frame");
    auto frame_base = current_frame_base();
    assert(index >= 0 && frame_base + index < count && "Index out of bounds");
    Profile.step(StepProfileKind::GET);
    auto ret = stack_ptr[frame_base + index];
    return ret;
  }

  void set(std::int32_t index, Num num) {
    assert(!frame_ptrs.empty() && "No active frame");
    auto frame_base = current_frame_base();
    assert(index >= 0 && frame_base + index < count && "Index out of bounds");
    Profile.step(StepProfileKind::SET);
    stack_ptr[frame_base + index] = num;
  }

  void pushFrameCaller(std::int32_t size) {
    assert(size >= 0);
    frame_ptrs.push_back(count);
    count += size;
    // Zero-initialize the new stack frames.
    for (std::int32_t i = 0; i < size; ++i) {
      stack_ptr[count - size + i] = Num(0);
    }
  }

  void pushFrameCallee(std::int32_t size) {
    assert(size >= 0);
    assert(!frame_ptrs.empty() && "No active frame");
    auto old_count = count;
    count += size;
    for (std::int32_t i = 0; i < size; ++i) {
      stack_ptr[old_count + i] = Num(0);
    }
  }

  void reset() {
    count = 0;
#ifdef USE_IMM
    frame_ptrs = immer::vector_transient<size_t>();
#else
    frame_ptrs.clear();
#endif
  }

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
  friend class SymFrames_t;

  size_t current_frame_base() const {
#ifdef USE_IMM
    return *(frame_ptrs.end() - 1);
#else
    return frame_ptrs.back();
#endif
  }

  int32_t count;
  Num *stack_ptr;
#ifdef USE_IMM
  immer::vector_transient<size_t> frame_ptrs;
#else
  std::vector<size_t> frame_ptrs;
#endif
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

  uint8_t loadByte(int32_t base, int32_t offset) {
    int32_t addr = base + offset;
    if (!(addr < memory.size()) || addr < 0) {
      throw std::runtime_error("Invalid memory access " + std::to_string(addr));
    }
    return memory[addr];
  }

  int32_t loadInt8U(int32_t base, int32_t offset) {
    return static_cast<uint32_t>(loadByte(base, offset));
  }

  int32_t loadInt8S(int32_t base, int32_t offset) {
    return static_cast<int8_t>(loadByte(base, offset));
  }

  int32_t loadInt16U(int32_t base, int32_t offset) {
    uint32_t b0 = static_cast<uint32_t>(loadByte(base, offset));
    uint32_t b1 = static_cast<uint32_t>(loadByte(base, offset + 1));
    return static_cast<int32_t>(b0 | (b1 << 8));
  }

  int32_t loadInt16S(int32_t base, int32_t offset) {
    uint32_t b0 = static_cast<uint32_t>(loadByte(base, offset));
    uint32_t b1 = static_cast<uint32_t>(loadByte(base, offset + 1));
    uint16_t raw = static_cast<uint16_t>(b0 | (b1 << 8));
    return static_cast<int16_t>(raw);
  }

  int64_t loadLong8U(int32_t base, int32_t offset) {
    return static_cast<uint64_t>(loadByte(base, offset));
  }

  int64_t loadLong8S(int32_t base, int32_t offset) {
    return static_cast<int8_t>(loadByte(base, offset));
  }

  int64_t loadLong16U(int32_t base, int32_t offset) {
    uint64_t b0 = static_cast<uint64_t>(loadByte(base, offset));
    uint64_t b1 = static_cast<uint64_t>(loadByte(base, offset + 1));
    return static_cast<int64_t>(b0 | (b1 << 8));
  }

  int64_t loadLong16S(int32_t base, int32_t offset) {
    uint64_t b0 = static_cast<uint64_t>(loadByte(base, offset));
    uint64_t b1 = static_cast<uint64_t>(loadByte(base, offset + 1));
    uint16_t raw = static_cast<uint16_t>(b0 | (b1 << 8));
    return static_cast<int16_t>(raw);
  }

  int64_t loadLong32U(int32_t base, int32_t offset) {
    uint64_t b0 = static_cast<uint64_t>(loadByte(base, offset));
    uint64_t b1 = static_cast<uint64_t>(loadByte(base, offset + 1));
    uint64_t b2 = static_cast<uint64_t>(loadByte(base, offset + 2));
    uint64_t b3 = static_cast<uint64_t>(loadByte(base, offset + 3));
    return static_cast<int64_t>(b0 | (b1 << 8) | (b2 << 16) | (b3 << 24));
  }

  int64_t loadLong32S(int32_t base, int32_t offset) {
    uint64_t b0 = static_cast<uint64_t>(loadByte(base, offset));
    uint64_t b1 = static_cast<uint64_t>(loadByte(base, offset + 1));
    uint64_t b2 = static_cast<uint64_t>(loadByte(base, offset + 2));
    uint64_t b3 = static_cast<uint64_t>(loadByte(base, offset + 3));
    uint32_t raw = static_cast<uint32_t>(b0 | (b1 << 8) | (b2 << 16) | (b3 << 24));
    return static_cast<int32_t>(raw);
  }

  int64_t loadLong(int32_t base, int32_t offset) {
    int32_t addr = base + offset;
    if (!(addr + 7 < memory.size()) || addr < 0) {
      throw std::runtime_error("Invalid memory access " + std::to_string(addr));
    }
    int64_t result = 0;
    for (int i = 0; i < 8; ++i) {
      result |= static_cast<int64_t>(memory[addr + i]) << (8 * i);
    }
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

  std::monostate storeLong(int32_t base, int32_t offset, int64_t value) {
    int32_t addr = base + offset;
    if (!(addr + 7 < memory.size()) || addr < 0) {
      throw std::runtime_error("Invalid memory access " + std::to_string(addr));
    }
    for (int i = 0; i < 8; ++i) {
      memory[addr + i] = static_cast<uint8_t>((static_cast<uint64_t>(value) >> (8 * i)) & 0xFF);
    }
    return std::monostate{};
  }

  std::monostate storeInt8(int32_t base, int32_t offset, int32_t value) {
    return store_byte(base + offset, static_cast<uint8_t>(value & 0xFF));
  }

  std::monostate storeInt16(int32_t base, int32_t offset, int32_t value) {
    store_byte(base + offset, static_cast<uint8_t>(value & 0xFF));
    store_byte(base + offset + 1, static_cast<uint8_t>((value >> 8) & 0xFF));
    return std::monostate{};
  }

  std::monostate storeLong8(int32_t base, int32_t offset, int64_t value) {
    return store_byte(base + offset, static_cast<uint8_t>(value & 0xFF));
  }

  std::monostate storeLong16(int32_t base, int32_t offset, int64_t value) {
    store_byte(base + offset, static_cast<uint8_t>(value & 0xFF));
    store_byte(base + offset + 1, static_cast<uint8_t>((value >> 8) & 0xFF));
    return std::monostate{};
  }

  std::monostate storeLong32(int32_t base, int32_t offset, int64_t value) {
    store_byte(base + offset, static_cast<uint8_t>(value & 0xFF));
    store_byte(base + offset + 1, static_cast<uint8_t>((value >> 8) & 0xFF));
    store_byte(base + offset + 2, static_cast<uint8_t>((value >> 16) & 0xFF));
    store_byte(base + offset + 3, static_cast<uint8_t>((value >> 24) & 0xFF));
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
      std::cout << "Function table entry at index " << index
                << " is empty or invalid" << std::endl;
      assert(false && "Function table entry is empty or invalid");
      throw std::runtime_error("Function table entry at index " +
                               std::to_string(index) + " is empty or invalid");
    }
    return table[index];
  }

  std::monostate set(Num offset, int32_t index, Func_t func) {
    assert(offset.value == 1 && "Only support one function table per module for now");
    if (index < 0 || index >= table.size()) {
      throw std::runtime_error("Function table set out of bounds: " +
                               std::to_string(index));
    }
    table[index] = func;
    return std::monostate{};
  }
};

static FuncTable_t FuncTable;

#endif // WASM_CONCRETE_RT_HPP
