#ifndef WASM_CONCRETE_RT_HPP
#define WASM_CONCRETE_RT_HPP

#include "concrete_num.hpp"
#include "controls.hpp"
#include "immer/vector_transient.hpp"
#include <cstdint>
#include <memory>
#include <variant>
#include <vector>

const int STACK_SIZE = 1024 * 64;

class Stack_t {
public:
  Stack_t();

  std::monostate push(Num &&num);
  std::monostate push(Num &num);
  Num pop();
  Num peek();
  int32_t size();
  void shift(int32_t offset, int32_t size);
  void print();
  void initialize();
  void reset();
  void resize(int32_t new_size);
  void set_from_front(int32_t index, const Num &num);

private:
  int32_t count;
  Num *stack_ptr;
};

extern Stack_t Stack;

class SymFrames_t;

const int FRAME_SIZE = 1024 * 8;

class Frames_t {
public:
  Frames_t();

  std::monostate popFrameCaller(std::int32_t size);
  std::monostate popFrameCallee(std::int32_t size);
  Num get(std::int32_t index);
  void set(std::int32_t index, Num num);
  void pushFrameCaller(std::int32_t size);
  void pushFrameCallee(std::int32_t size);
  void reset();
  size_t size() const;
  void set_from_front(int32_t index, const Num &num);
  void resize(int32_t new_size);

private:
  friend class SymFrames_t;

  size_t current_frame_base() const;

  int32_t count;
  Num *stack_ptr;

#ifdef USE_IMM
  immer::vector_transient<size_t> frame_ptrs;
#else
  std::vector<size_t> frame_ptrs;
#endif
};

extern Frames_t Frames;
extern Frames_t Globals;

void initRand();

std::monostate unreachable();

static const int PRE_ALLOC_PAGES = 20;
static const int32_t pagesize = 65536;

struct Memory_t {
  Memory_t(int32_t init_page_count);

  int32_t loadInt(int32_t base, int32_t offset);
  uint8_t loadByte(int32_t base, int32_t offset);
  int32_t loadInt8U(int32_t base, int32_t offset);
  int32_t loadInt8S(int32_t base, int32_t offset);
  int32_t loadInt16U(int32_t base, int32_t offset);
  int32_t loadInt16S(int32_t base, int32_t offset);
  int64_t loadLong8U(int32_t base, int32_t offset);
  int64_t loadLong8S(int32_t base, int32_t offset);
  int64_t loadLong16U(int32_t base, int32_t offset);
  int64_t loadLong16S(int32_t base, int32_t offset);
  int64_t loadLong32U(int32_t base, int32_t offset);
  int64_t loadLong32S(int32_t base, int32_t offset);
  int64_t loadLong(int32_t base, int32_t offset);

  std::monostate storeInt(int32_t base, int32_t offset, int32_t value);
  std::monostate storeLong(int32_t base, int32_t offset, int64_t value);
  std::monostate storeInt8(int32_t base, int32_t offset, int32_t value);
  std::monostate storeInt16(int32_t base, int32_t offset, int32_t value);
  std::monostate storeLong8(int32_t base, int32_t offset, int64_t value);
  std::monostate storeLong16(int32_t base, int32_t offset, int64_t value);
  std::monostate storeLong32(int32_t base, int32_t offset, int64_t value);
  std::monostate store_byte(int32_t addr, uint8_t value);

  int32_t grow(int32_t delta);
  void reset();

private:
  std::vector<uint8_t> memory;
  int init_page_count;
  int page_count;
  int allocated_pages;
};

extern Memory_t Memory;

struct FuncTable_t {
  FuncTable_t();

  std::vector<Func_t> table;

  Func_t read(int32_t index);
  std::monostate set(Num offset, int32_t index, Func_t func);
};

extern FuncTable_t FuncTable;

#endif // WASM_CONCRETE_RT_HPP