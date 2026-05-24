#include "wasm/concrete_rt.hpp"

#include <cassert>
#include <cstdio>
#include <iostream>
#include <stdexcept>
#include <string>
#include <unistd.h>
#include "wasm/profile.hpp"

Stack_t Stack;
Frames_t Frames;
Frames_t Globals;
Memory_t Memory(4);
FuncTable_t FuncTable;

Stack_t::Stack_t() : count(0), stack_ptr(new Num[STACK_SIZE]) {
  size_t page_size = static_cast<size_t>(sysconf(_SC_PAGESIZE));
  for (int i = 0; i < STACK_SIZE; i += page_size) {
    stack_ptr[i] = Num(0);
  }
}

std::monostate Stack_t::push(Num &&num) {
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

std::monostate Stack_t::push(Num &num) {
  Profile.step(StepProfileKind::PUSH);
  stack_ptr[count] = num;
  count++;
  return std::monostate{};
}

Num Stack_t::pop() {
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

Num Stack_t::peek() {
  Profile.step(StepProfileKind::PEEK);
#ifdef DEBUG
  if (count == 0) {
    throw std::runtime_error("Stack underflow");
  }
#endif
  return stack_ptr[count - 1];
}

int32_t Stack_t::size() { return count; }

void Stack_t::shift(int32_t offset, int32_t size) {
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

  for (int32_t i = count - size; i < count; ++i) {
    assert(i - offset >= 0);
    stack_ptr[i - offset] = stack_ptr[i];
  }
  count -= offset;
}

void Stack_t::print() {
  std::cout << "Stack contents: " << std::endl;
  for (int32_t i = 0; i < count; ++i) {
    std::cout << stack_ptr[count - i - 1].value << std::endl;
  }
  std::cout << "End of Stack contents" << std::endl;
}

void Stack_t::initialize() {
  reset();
}

void Stack_t::reset() { count = 0; }

void Stack_t::resize(int32_t new_size) {
  assert(new_size >= 0);
  count = new_size;
}

void Stack_t::set_from_front(int32_t index, const Num &num) {
  assert(index >= 0 && index < count);
  stack_ptr[index] = num;
}

Frames_t::Frames_t() : count(0), stack_ptr(new Num[FRAME_SIZE]), frame_ptrs() {
  size_t page_size = static_cast<size_t>(sysconf(_SC_PAGESIZE));
  for (int i = 0; i < FRAME_SIZE; i += page_size) {
    stack_ptr[i] = Num(0);
  }
}

std::monostate Frames_t::popFrameCaller(std::int32_t size) {
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

std::monostate Frames_t::popFrameCallee(std::int32_t size) {
  assert(size >= 0);
  assert(size <= count);
  count -= size;
  return std::monostate{};
}

Num Frames_t::get(std::int32_t index) {
  assert(!frame_ptrs.empty() && "No active frame");
  auto frame_base = current_frame_base();
  assert(index >= 0 && frame_base + index < count && "Index out of bounds");
  Profile.step(StepProfileKind::GET);
  auto ret = stack_ptr[frame_base + index];
  return ret;
}

void Frames_t::set(std::int32_t index, Num num) {
  assert(!frame_ptrs.empty() && "No active frame");
  auto frame_base = current_frame_base();
  assert(index >= 0 && frame_base + index < count && "Index out of bounds");
  Profile.step(StepProfileKind::SET);
  stack_ptr[frame_base + index] = num;
}

void Frames_t::pushFrameCaller(std::int32_t size) {
  assert(size >= 0);
  frame_ptrs.push_back(count);
  count += size;
  for (std::int32_t i = 0; i < size; ++i) {
    stack_ptr[count - size + i] = Num(0);
  }
}

void Frames_t::pushFrameCallee(std::int32_t size) {
  assert(size >= 0);
  assert(!frame_ptrs.empty() && "No active frame");
  auto old_count = count;
  count += size;
  for (std::int32_t i = 0; i < size; ++i) {
    stack_ptr[old_count + i] = Num(0);
  }
}

void Frames_t::reset() {
  count = 0;
#ifdef USE_IMM
  frame_ptrs = immer::vector_transient<size_t>();
#else
  frame_ptrs.clear();
#endif
}

size_t Frames_t::size() const { return count; }

void Frames_t::set_from_front(int32_t index, const Num &num) {
  assert(index >= 0 && index < count && "Index out of bounds");
  stack_ptr[index] = num;
}

void Frames_t::resize(int32_t new_size) {
  assert(new_size >= 0);
  count = new_size;
}

size_t Frames_t::current_frame_base() const {
#ifdef USE_IMM
  return *(frame_ptrs.end() - 1);
#else
  return frame_ptrs.back();
#endif
}

void initRand() {
  // for now, just do nothing
}

std::monostate unreachable() {
  std::cout << "Unreachable code reached!" << std::endl;
  throw std::runtime_error("Unreachable code reached");
}

Memory_t::Memory_t(int32_t init_page_count)
    : memory(PRE_ALLOC_PAGES * pagesize), init_page_count(init_page_count),
      page_count(init_page_count), allocated_pages(PRE_ALLOC_PAGES) {
  reset();
}

int32_t Memory_t::loadInt(int32_t base, int32_t offset) {
  int32_t addr = base + offset;
  if (!(addr + 3 < memory.size()) || addr < 0) {
    throw std::runtime_error("Invalid memory access " + std::to_string(addr));
  }

  int32_t result = 0;
  for (int i = 0; i < 4; ++i) {
    result |= static_cast<int32_t>(memory[addr + i]) << (8 * i);
  }

#ifdef DEBUG
  std::cout << "[Debug] loading int " << result << " from memory at address "
            << addr << std::endl;
#endif

  return result;
}

uint8_t Memory_t::loadByte(int32_t base, int32_t offset) {
  int32_t addr = base + offset;
  if (!(addr < memory.size()) || addr < 0) {
    throw std::runtime_error("Invalid memory access " + std::to_string(addr));
  }
  return memory[addr];
}

int32_t Memory_t::loadInt8U(int32_t base, int32_t offset) {
  return static_cast<uint32_t>(loadByte(base, offset));
}

int32_t Memory_t::loadInt8S(int32_t base, int32_t offset) {
  return static_cast<int8_t>(loadByte(base, offset));
}

int32_t Memory_t::loadInt16U(int32_t base, int32_t offset) {
  uint32_t b0 = static_cast<uint32_t>(loadByte(base, offset));
  uint32_t b1 = static_cast<uint32_t>(loadByte(base, offset + 1));
  return static_cast<int32_t>(b0 | (b1 << 8));
}

int32_t Memory_t::loadInt16S(int32_t base, int32_t offset) {
  uint32_t b0 = static_cast<uint32_t>(loadByte(base, offset));
  uint32_t b1 = static_cast<uint32_t>(loadByte(base, offset + 1));
  uint16_t raw = static_cast<uint16_t>(b0 | (b1 << 8));
  return static_cast<int16_t>(raw);
}

int64_t Memory_t::loadLong8U(int32_t base, int32_t offset) {
  return static_cast<uint64_t>(loadByte(base, offset));
}

int64_t Memory_t::loadLong8S(int32_t base, int32_t offset) {
  return static_cast<int8_t>(loadByte(base, offset));
}

int64_t Memory_t::loadLong16U(int32_t base, int32_t offset) {
  uint64_t b0 = static_cast<uint64_t>(loadByte(base, offset));
  uint64_t b1 = static_cast<uint64_t>(loadByte(base, offset + 1));
  return static_cast<int64_t>(b0 | (b1 << 8));
}

int64_t Memory_t::loadLong16S(int32_t base, int32_t offset) {
  uint64_t b0 = static_cast<uint64_t>(loadByte(base, offset));
  uint64_t b1 = static_cast<uint64_t>(loadByte(base, offset + 1));
  uint16_t raw = static_cast<uint16_t>(b0 | (b1 << 8));
  return static_cast<int16_t>(raw);
}

int64_t Memory_t::loadLong32U(int32_t base, int32_t offset) {
  uint64_t b0 = static_cast<uint64_t>(loadByte(base, offset));
  uint64_t b1 = static_cast<uint64_t>(loadByte(base, offset + 1));
  uint64_t b2 = static_cast<uint64_t>(loadByte(base, offset + 2));
  uint64_t b3 = static_cast<uint64_t>(loadByte(base, offset + 3));
  return static_cast<int64_t>(b0 | (b1 << 8) | (b2 << 16) | (b3 << 24));
}

int64_t Memory_t::loadLong32S(int32_t base, int32_t offset) {
  uint64_t b0 = static_cast<uint64_t>(loadByte(base, offset));
  uint64_t b1 = static_cast<uint64_t>(loadByte(base, offset + 1));
  uint64_t b2 = static_cast<uint64_t>(loadByte(base, offset + 2));
  uint64_t b3 = static_cast<uint64_t>(loadByte(base, offset + 3));
  uint32_t raw =
      static_cast<uint32_t>(b0 | (b1 << 8) | (b2 << 16) | (b3 << 24));
  return static_cast<int32_t>(raw);
}

int64_t Memory_t::loadLong(int32_t base, int32_t offset) {
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

std::monostate Memory_t::storeInt(int32_t base, int32_t offset,
                                  int32_t value) {
  int32_t addr = base + offset;
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

std::monostate Memory_t::storeLong(int32_t base, int32_t offset,
                                   int64_t value) {
  int32_t addr = base + offset;
  if (!(addr + 7 < memory.size()) || addr < 0) {
    throw std::runtime_error("Invalid memory access " + std::to_string(addr));
  }

  for (int i = 0; i < 8; ++i) {
    memory[addr + i] =
        static_cast<uint8_t>((static_cast<uint64_t>(value) >> (8 * i)) & 0xFF);
  }

  return std::monostate{};
}

std::monostate Memory_t::storeInt8(int32_t base, int32_t offset,
                                   int32_t value) {
  return store_byte(base + offset, static_cast<uint8_t>(value & 0xFF));
}

std::monostate Memory_t::storeInt16(int32_t base, int32_t offset,
                                    int32_t value) {
  store_byte(base + offset, static_cast<uint8_t>(value & 0xFF));
  store_byte(base + offset + 1, static_cast<uint8_t>((value >> 8) & 0xFF));
  return std::monostate{};
}

std::monostate Memory_t::storeLong8(int32_t base, int32_t offset,
                                    int64_t value) {
  return store_byte(base + offset, static_cast<uint8_t>(value & 0xFF));
}

std::monostate Memory_t::storeLong16(int32_t base, int32_t offset,
                                     int64_t value) {
  store_byte(base + offset, static_cast<uint8_t>(value & 0xFF));
  store_byte(base + offset + 1, static_cast<uint8_t>((value >> 8) & 0xFF));
  return std::monostate{};
}

std::monostate Memory_t::storeLong32(int32_t base, int32_t offset,
                                     int64_t value) {
  store_byte(base + offset, static_cast<uint8_t>(value & 0xFF));
  store_byte(base + offset + 1, static_cast<uint8_t>((value >> 8) & 0xFF));
  store_byte(base + offset + 2, static_cast<uint8_t>((value >> 16) & 0xFF));
  store_byte(base + offset + 3, static_cast<uint8_t>((value >> 24) & 0xFF));
  return std::monostate{};
}

std::monostate Memory_t::store_byte(int32_t addr, uint8_t value) {
#ifdef DEBUG
  std::cout << "[Debug] storing byte " << std::to_string(value)
            << " to memory at address " << addr << std::endl;
#endif

  assert(addr < memory.size());
  memory[addr] = value;
  return std::monostate{};
}

int32_t Memory_t::grow(int32_t delta) {
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

void Memory_t::reset() {
  page_count = init_page_count;
  allocated_pages = PRE_ALLOC_PAGES;
  for (int i = 0; i < memory.size() && i < page_count * pagesize; ++i) {
    memory[i] = 0;
  }
}

FuncTable_t::FuncTable_t() : table(20) {}

Func_t FuncTable_t::read(int32_t index) {
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

std::monostate FuncTable_t::set(Num offset, int32_t index, Func_t func) {
  if (index < 0 || index >= table.size()) {
    throw std::runtime_error("Function table set out of bounds: " +
                             std::to_string(index));
  }

  table[offset.toInt() + index] = func;
  return std::monostate{};
}