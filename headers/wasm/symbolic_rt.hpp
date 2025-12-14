#ifndef WASM_SYMBOLIC_RT_HPP
#define WASM_SYMBOLIC_RT_HPP

#include "concrete_rt.hpp"
#include "config.hpp"
#include "controls.hpp"
#include "heap_mem_bookkeeper.hpp"
#include "immer/map.hpp"
#include "immer/map_transient.hpp"
#include "immer/vector_transient.hpp"
#include "profile.hpp"
#include "utils.hpp"
#include "wasm/z3_env.hpp"
#include "z3++.h"
#include <cassert>
#include <cstddef>
#include <cstdint>
#include <cstdio>
#include <filesystem>
#include <fstream>
#include <iterator>
#include <memory>
#include <optional>
#include <ostream>
#include <set>
#include <string>
#include <unordered_map>
#include <variant>
#include <vector>

enum Operation {
  ADD,    // Addition
  SUB,    // Subtraction
  MUL,    // Multiplication
  DIV,    // Division
  EQ,     // Equal
  NEQ,    // Not equal
  LT,     // Less than
  LTU,    // Unsigned less than
  LEQ,    // Less than or equal
  GT,     // Greater than
  GTU,    // Unsigned greater than
  GEQ,    // Greater than or equal
  GEU,    // Unsigned greater than or equal
  SHR,    // Shift right
  B_AND,  // Bitwise AND
  B_XOR,  // Bitwise XOR
  B_OR,   // Bitwise OR
  CONCAT, // Byte-level concatenation
};
class Symbolic;
struct SymVal {
  std::shared_ptr<Symbolic> symptr;

  SymVal();
  SymVal(std::shared_ptr<Symbolic> symptr);

  // data structure operations
  SymVal makeSymbolic() const;

  // bitvector arithmetic operations
  SymVal is_zero() const;
  SymVal add(const SymVal &other) const;
  SymVal minus(const SymVal &other) const;
  SymVal mul(const SymVal &other) const;
  SymVal div(const SymVal &other) const;
  SymVal eq(const SymVal &other) const;
  SymVal neq(const SymVal &other) const;
  SymVal lt(const SymVal &other) const;
  SymVal ltu(const SymVal &other) const;
  SymVal le(const SymVal &other) const;
  SymVal gt(const SymVal &other) const;
  SymVal gtu(const SymVal &other) const;
  SymVal ge(const SymVal &other) const;
  SymVal geu(const SymVal &other) const;
  SymVal shr(const SymVal &other) const;
  SymVal negate() const;
  SymVal bitwise_and(const SymVal &other) const;
  SymVal bitwise_xor(const SymVal &other) const;
  SymVal bitwise_or(const SymVal &other) const;
  SymVal concat(const SymVal &other) const;
  SymVal extract(int high, int low) const;
  // TODO: add bitwise operations, and use the underlying bitvector theory

  bool is_concrete() const;
  int size() const;

private:
  static SymVal make_binary(Operation op, const SymVal &lhs, const SymVal &rhs);
};

class Symbolic {
public:
  Symbolic() {}
  virtual ~Symbolic() = default; // Make Symbolic polymorphic
  virtual int dag_size() = 0;
  virtual z3::expr z3_expr() { return build_z3_expr(); }
  z3::expr build_z3_expr();

private:
  z3::expr build_z3_expr_aux();
  std::optional<z3::expr> _z3_expr;
};

static int max_id = 0;

class Symbol : public Symbolic {
public:
  // TODO: add type information to determine the size of bitvector
  // for now we just assume that only i32 will be used
  Symbol(int id) : id(id) { max_id = std::max(max_id, id); }
  int get_id() const { return id; }

  int dag_size() override { return 1; }

private:
  int id;
};

class SymConcrete : public Symbolic {
public:
  Num value;
  SymConcrete(Num num) : value(num) {}

  int dag_size() override { return 1; }
};

class SmallBV : public Symbolic {
public:
  SmallBV(int width, int64_t value) : width(width), value(value) {}
  int get_size() const { return width; }
  int64_t get_value() const { return value; }

  int dag_size() override { return 1; }

private:
  int width; // in bits
  int64_t value;
};
struct SymBinary;

static MemBookKeeper<Symbolic> SymBookKeeper;

static std::shared_ptr<SymConcrete> ZERO =
    SymBookKeeper.allocate<SymConcrete>(I32V(0));
static std::shared_ptr<SmallBV> ZeroByte =
    SymBookKeeper.allocate<SmallBV>(8, 0);

inline SymVal::SymVal() : symptr(ZERO) {}
inline SymVal::SymVal(std::shared_ptr<Symbolic> symptr) : symptr(symptr) {}
inline int SymVal::size() const { return symptr->dag_size(); }

static SymVal make_symbolic(int index) {
  return SymVal(SymBookKeeper.allocate<Symbol>(index));
}

static SymVal makeSmallBV(int width, int64_t value) {
  if (width == 32) {
    return SymVal(
        SymBookKeeper.allocate<SymConcrete>(Num(static_cast<int32_t>(value))));
  }
  if (width == 64) {
    return SymVal(
        SymBookKeeper.allocate<SymConcrete>(Num(static_cast<int64_t>(value))));
  }
  return SymVal(SymBookKeeper.allocate<SmallBV>(width, value));
}

static std::unordered_map<int64_t, SymVal> concrete_pool;

inline SymVal Concrete(Num num) {
  if (concrete_pool.find(num.toInt()) != concrete_pool.end()) {
    return concrete_pool[num.toInt()];
  }

  auto new_val = SymVal(SymBookKeeper.allocate<SymConcrete>(num));
  concrete_pool[num.toInt()] = new_val;
  return new_val;
}

// Extract is different from other operations, it only has one symbolic operand,
// the other two operands are constants
// Extract from value, both high and low are inclusive byte indexes
struct SymExtract : public Symbolic {
  SymVal value;
  int high;
  int low;

  SymExtract(SymVal value, int high, int low)
      : value(value), high(high), low(low) {}

  int dag_size() override {
    if (_cached_dag_size.has_value()) {
      return _cached_dag_size.value();
    }
    _cached_dag_size = 1 + value.symptr->dag_size();
    return _cached_dag_size.value();
  }

private:
  friend std::tuple<int, bool>
  count_dag_size_aux(Symbolic &val, std::set<Symbolic *> &visited);

  std::optional<int> _cached_dag_size;
};

inline int count_dag_size(Symbolic &val);

struct SymBinary : public Symbolic {
  Operation op;
  SymVal lhs;
  SymVal rhs;

  SymBinary(Operation op, SymVal lhs, SymVal rhs)
      : op(op), lhs(lhs), rhs(rhs) {}

  int dag_size() override {
    if (_cached_dag_size.has_value()) {
      return _cached_dag_size.value();
    }

    auto size = count_dag_size(*this);
    _cached_dag_size = size;
    return size;
  }

private:
  friend std::tuple<int, bool>
  count_dag_size_aux(Symbolic &val, std::set<Symbolic *> &visited);
  std::optional<int> _cached_dag_size;
};

inline std::tuple<int, bool> count_dag_size_aux(Symbolic &val,
                              std::set<Symbolic *> &visited) {
  if (visited.find(&val) != visited.end()) {
    return {0, true};
  }
  visited.insert(&val);

  if (auto binary = dynamic_cast<SymBinary *>(&val)) {
    int size = 1;
    auto [lhs_size, lhs_sharing] =
        count_dag_size_aux(*binary->lhs.symptr, visited);
    auto [rhs_size, rhs_sharing] =
        count_dag_size_aux(*binary->rhs.symptr, visited);
    size += lhs_size + rhs_size;
    if (!lhs_sharing && !rhs_sharing) {
      // if there is no sharing in two operands, this temporary size is valid
      // and reusable
      binary->_cached_dag_size = size;
    }
    return {size, lhs_sharing || rhs_sharing};
  } else if (auto extract = dynamic_cast<SymExtract *>(&val)) {
    int size = 1;
    auto [value_size, value_sharing] =
        count_dag_size_aux(*extract->value.symptr, visited);
    size += value_size;
    if (!value_sharing) {
      extract->_cached_dag_size = size;
    }
    return {size, value_sharing};
  } else if (auto symbol = dynamic_cast<Symbol *>(&val)) {
    return {1, false};
  } else if (auto concrete = dynamic_cast<SymConcrete *>(&val)) {
    return {1, false};
  } else if (auto smallbv = dynamic_cast<SmallBV *>(&val)) {
    return {1, false};
  } else {
    throw std::runtime_error("Unknown symbolic type in dag size counting");
  }
}

inline int count_dag_size(Symbolic &val) {
  std::set<Symbolic *> visited;
  auto [size, _] = count_dag_size_aux(val, visited);
  return size;
}

inline SymVal SymVal::add(const SymVal &other) const {
  return make_binary(ADD, *this, other);
}

inline SymVal SymVal::minus(const SymVal &other) const {
  return make_binary(SUB, *this, other);
}

inline SymVal SymVal::mul(const SymVal &other) const {
  return make_binary(MUL, *this, other);
}

inline SymVal SymVal::div(const SymVal &other) const {
  return make_binary(DIV, *this, other);
}

inline SymVal SymVal::eq(const SymVal &other) const {
  return make_binary(EQ, *this, other);
}

inline SymVal SymVal::neq(const SymVal &other) const {
  return make_binary(NEQ, *this, other);
}

inline SymVal SymVal::lt(const SymVal &other) const {
  return make_binary(LT, *this, other);
}

inline SymVal SymVal::ltu(const SymVal &other) const {
  // for now, we treat unsigned less than as signed less than
  return make_binary(LTU, *this, other);
}

inline SymVal SymVal::le(const SymVal &other) const {
  return make_binary(LEQ, *this, other);
}

inline SymVal SymVal::gt(const SymVal &other) const {
  return make_binary(GT, *this, other);
}

inline SymVal SymVal::gtu(const SymVal &other) const {
  return make_binary(GTU, *this, other);
}

inline SymVal SymVal::ge(const SymVal &other) const {
  return make_binary(GEQ, *this, other);
}

inline SymVal SymVal::geu(const SymVal &other) const {
  return make_binary(GEU, *this, other);
}

inline SymVal SymVal::shr(const SymVal &other) const {
  return make_binary(SHR, *this, other);
}

inline SymVal SymVal::is_zero() const {
  return make_binary(EQ, *this, Concrete(I32V(0)));
}

inline SymVal SymVal::negate() const {
  return make_binary(EQ, *this, Concrete(I32V(0)));
}

inline SymVal SymVal::concat(const SymVal &other) const {
  if (auto bv1 = std::dynamic_pointer_cast<SmallBV>(symptr)) {
    if (auto bv2 = std::dynamic_pointer_cast<SmallBV>(other.symptr)) {
      int new_width = bv1->get_size() + bv2->get_size();
      int64_t new_value =
          (bv1->get_value() << bv2->get_size()) | bv2->get_value();
      return makeSmallBV(new_width, new_value);
    }
  }
  return make_binary(CONCAT, *this, other);
}

inline SymVal SymVal::extract(int high, int low) const {
  assert(high >= low && "Invalid extract range");
  int new_width = (high - low + 1) * 8;
  int shift_bits = (low - 1) * 8;

  if (auto bv = std::dynamic_pointer_cast<SmallBV>(symptr)) {
    int64_t mask = (1LL << new_width) - 1;
    int64_t new_value = (bv->get_value() >> shift_bits) & mask;
    return makeSmallBV(new_width, new_value);
  } else if (auto concrete = std::dynamic_pointer_cast<SymConcrete>(symptr)) {
    // extract from concrete value
    int32_t val = concrete->value.toInt();
    int32_t mask = (1LL << ((high - low + 1) * 8)) - 1;
    int32_t new_value = (val >> shift_bits) & mask;
    return makeSmallBV(new_width, new_value);
  }
  return SymVal(SymBookKeeper.allocate<SymExtract>(*this, high, low));
}

inline SymVal SymVal::bitwise_and(const SymVal &other) const {
  return make_binary(B_AND, *this, other);
}

inline SymVal SymVal::bitwise_xor(const SymVal &other) const {
  return make_binary(B_XOR, *this, other);
}

inline SymVal SymVal::bitwise_or(const SymVal &other) const {
  return make_binary(B_OR, *this, other);
}

inline SymVal SymVal::make_binary(Operation op, const SymVal &lhs,
                                  const SymVal &rhs) {
  assert(lhs.symptr != nullptr && rhs.symptr != nullptr);
  return SymVal(SymBookKeeper.allocate<SymBinary>(op, lhs, rhs));
}
static std::unordered_map<int, SymVal> SymbolCache;

inline SymVal SymVal::makeSymbolic() const {
  auto concrete = dynamic_cast<SymConcrete *>(symptr.get());
  if (concrete) {
    // If the symbolic value is a concrete value, use it to create a symbol
    auto id = concrete->value.toInt();
    auto it = SymbolCache.find(id);
    if (it != SymbolCache.end()) {
      return it->second;
    }
    auto sym = Symbol(id);
    auto ptr = SymBookKeeper.allocate<Symbol>(sym);
    return SymVal(ptr);

  } else {
    throw std::runtime_error(
        "Cannot make symbolic a non-concrete symbolic value");
  }
}

inline z3::expr Symbolic::build_z3_expr_aux() {
  if (auto sym = dynamic_cast<Symbol *>(this)) {
    return global_z3_ctx().bv_const(
        ("s_" + std::to_string(sym->get_id())).c_str(), 32);
  } else if (auto concrete = dynamic_cast<SymConcrete *>(this)) {
    return global_z3_ctx().bv_val(concrete->value.value, 32);
  } else if (auto smallbv = dynamic_cast<SmallBV *>(this)) {
    return global_z3_ctx().bv_val(smallbv->get_value(), smallbv->get_size());
  } else if (auto binary = dynamic_cast<SymBinary *>(this)) {
    auto bit_width = 32;
    z3::expr zero_bv = global_z3_ctx().bv_val(
        0, bit_width); // Represents 0 as a 32-bit bitvector
    z3::expr one_bv = global_z3_ctx().bv_val(
        1, bit_width); // Represents 1 as a 32-bit bitvector

    z3::expr left = binary->lhs.symptr->build_z3_expr();
    z3::expr right = binary->rhs.symptr->build_z3_expr();
    // TODO: make sure the semantics of these operations are aligned with wasm
    switch (binary->op) {
    case EQ: {
      auto temp_bool = left == right;
      return z3::ite(temp_bool, one_bv, zero_bv);
    }
    case NEQ: {
      auto temp_bool = left != right;
      return z3::ite(temp_bool, one_bv, zero_bv);
    }
    case LT: {
      auto temp_bool = left < right;
      return z3::ite(temp_bool, one_bv, zero_bv);
    }
    case LTU: {
      auto temp_bool = z3::ult(left, right);
      return z3::ite(temp_bool, one_bv, zero_bv);
    }
    case LEQ: {
      auto temp_bool = left <= right;
      return z3::ite(temp_bool, one_bv, zero_bv);
    }
    case GT: {
      auto temp_bool = left > right;
      return z3::ite(temp_bool, one_bv, zero_bv);
    }
    case GTU: {
      auto temp_bool = z3::ugt(left, right);
      return z3::ite(temp_bool, one_bv, zero_bv);
    }
    case GEU: {
      auto temp_bool = z3::uge(left, right);
      return z3::ite(temp_bool, one_bv, zero_bv);
    }
    case SHR: {
      return z3::lshr(left, right);
    }
    case GEQ: {
      auto temp_bool = left >= right;
      return z3::ite(temp_bool, one_bv, zero_bv);
    }
    case ADD: {
      return left + right;
    }
    case SUB: {
      return left - right;
    }
    case MUL: {
      return left * right;
    }
    case DIV: {
      return left / right;
    }
    case B_AND: {
      return left & right;
    }
    case B_XOR: {
      return left ^ right;
    }
    case B_OR: {
      return left | right;
    }
    case CONCAT: {
      return z3::concat(left, right);
    }
    default:
      throw std::runtime_error("Operation not supported: " +
                               std::to_string(binary->op));
    }
  } else if (auto extract = dynamic_cast<SymExtract *>(this)) {
    assert(extract);
    int high = extract->high * 8 - 1;
    int low = extract->low * 8 - 8;
    auto s = extract->value.symptr->build_z3_expr();
    auto res = s.extract(high, low);
    return res;
  }
  throw std::runtime_error("Unsupported symbolic value type");
}

inline z3::expr Symbolic::build_z3_expr() {
  if (_z3_expr.has_value()) {
    return *_z3_expr;
  }
  auto e = build_z3_expr_aux();
  _z3_expr = e;
  return e;
}

inline bool SymVal::is_concrete() const {
  return dynamic_cast<SymConcrete *>(symptr.get()) != nullptr;
}

template <typename... Args> inline bool allConcrete(const Args &...args) {
  static_assert((std::is_same_v<Args, SymVal> && ...),
                "all_concrete only accepts SymVal arguments");
  return (... && args.is_concrete());
}

class Snapshot_t;

class SymStack_t {
public:
  void push(SymVal val) {
    // Push a symbolic value to the stack
    stack.push_back(val);
    symbolic_size += val.size();
  }

  SymVal pop() {
    // Pop a symbolic value from the stack
#ifdef DEBUG
    printf("[Debug] poping from stack, size of symbolic stack is: %zu\n",
           stack.size());
#endif
#ifdef USE_IMM
    auto ret = *(stack.end() - 1);
    stack.take(stack.size() - 1);
    symbolic_size -= ret.size();
    return ret;
#else
    auto ret = stack.back();
    stack.pop_back();
    symbolic_size -= ret.size();
    return ret;
#endif
  }

  SymVal peek() { return *(stack.end() - 1); }

  std::monostate shift(int32_t offset, int32_t size) {
    auto n = stack.size();
    for (size_t i = n - size; i < n; ++i) {
      assert(i - offset >= 0);
#ifdef USE_IMM
      symbolic_size -= stack[i - offset].size();
      stack.set(i - offset, stack[i]);
#else
      stack[i - offset] = stack[i];
#endif
    }
#ifdef USE_IMM
    stack.take(n - offset);
#else
    stack.resize(n - offset);
#endif
    return std::monostate();
  }

  void reset() {
// Reset the symbolic stack
#ifdef USE_IMM
    stack = immer::vector_transient<SymVal>();
#else
    stack.clear();
#endif
    symbolic_size = 0;
  }

  size_t size() const { return stack.size(); }

  SymVal operator[](size_t index) const { return stack[index]; }

  int total_sym_size() const { return symbolic_size; }

private:
  int symbolic_size = 0;
#ifdef USE_IMM
  immer::vector_transient<SymVal> stack;
#else
  std::vector<SymVal> stack;
#endif
};

static SymStack_t SymStack;

class SymFrames_t {

public:
  void pushFrame(int size) {
    symbolic_size += size;
    // Push a new frame with the given size
#ifdef USE_IMM
    for (int i = 0; i < size; ++i) {
      stack.push_back(SymVal());
    }
#else
    stack.resize(size + stack.size());
#endif
  }
  std::monostate popFrame(int size) {
    // Pop the frame of the given size

    for (int i = 0; i < size; ++i) {
      symbolic_size -= stack[stack.size() - 1 - i].size();
    }
#ifdef USE_IMM
    stack.take(stack.size() - size);
#else
    stack.resize(stack.size() - size);
#endif
    return std::monostate();
  }

  SymVal get(int index) {
    // Get the symbolic value at the given frame index
    auto res = stack[stack.size() - 1 - index];
    return res;
  }

  void set(int index, SymVal val) {
    // Set the symbolic value at the given index
    assert(val.symptr != nullptr);
    symbolic_size += val.size() - stack[stack.size() - 1 - index].size();
#ifdef USE_IMM
    stack.set(stack.size() - 1 - index, val);
#else
    stack[stack.size() - 1 - index] = val;
#endif
  }

  void reset() {
    // Reset the symbolic frames

#ifdef USE_IMM
    stack = immer::vector_transient<SymVal>();
#else
    stack.clear();
#endif
    symbolic_size = 0;
  }

  size_t size() const { return stack.size(); }

  SymVal operator[](size_t index) const { return stack[index]; }

  int total_sym_size() const { return symbolic_size; }

private:
  int symbolic_size = 0;
#ifdef USE_IMM
  immer::vector_transient<SymVal> stack;
#else
  std::vector<SymVal> stack;
#endif
};

struct NodeBox;
struct SymEnv_t;

class SymMemory_t {
public:
#ifdef USE_IMM
  immer::map_transient<int, SymVal> memory;
#else
  std::unordered_map<int, SymVal> memory;
#endif
  int symbolic_size = 0;

  SymVal loadSymByte(int32_t addr) {
// if the address is not in the memory, it must be a zero-initialized memory
#ifdef USE_IMM
    auto it = memory.find(addr);
    if (it != nullptr) {
      return *it;
    } else {
      auto s = SymVal(ZeroByte);
      return s;
    }
#else
    auto it = memory.find(addr);
    SymVal s = (it != memory.end()) ? it->second : SymVal(ZeroByte);
    return s;
#endif
  }

  SymVal loadSym(int32_t base, int32_t offset) {
    // calculate the real address

#ifdef USE_IMM
    int32_t addr = base + offset;
    auto it = memory.find(addr);
    SymVal s0 = it ? *it : SymVal(ZeroByte);
    it = memory.find(addr + 1);
    SymVal s1 = it ? *it : SymVal(ZeroByte);
    it = memory.find(addr + 2);
    SymVal s2 = it ? *it : SymVal(ZeroByte);
    it = memory.find(addr + 3);
    SymVal s3 = it ? *it : SymVal(ZeroByte);

    return s3.concat(s2).concat(s1).concat(s0);
#else
    int32_t addr = base + offset;
    auto it = memory.find(addr);
    SymVal s0 = (it != memory.end()) ? it->second : SymVal(ZeroByte);
    it = memory.find(addr + 1);
    SymVal s1 = (it != memory.end()) ? it->second : SymVal(ZeroByte);
    it = memory.find(addr + 2);
    SymVal s2 = (it != memory.end()) ? it->second : SymVal(ZeroByte);
    it = memory.find(addr + 3);
    SymVal s3 = (it != memory.end()) ? it->second : SymVal(ZeroByte);

    return s3.concat(s2).concat(s1).concat(s0);
#endif
  }

  // when loading a symval, we need to concat 4 symbolic values
  // This sounds terribly bad for SMT...
  // Load a 4-byte symbolic value from memory
  // Store a 4-byte symbolic value to memory
  std::monostate storeSym(int32_t base, int32_t offset, SymVal value) {
    int32_t addr = base + offset;
    // Extract 4 bytes from that symbol
    SymVal s0 = value.extract(1, 1);
    SymVal s1 = value.extract(2, 2);
    SymVal s2 = value.extract(3, 3);
    SymVal s3 = value.extract(4, 4);
    storeSymByte(addr, s0);
    storeSymByte(addr + 1, s1);
    storeSymByte(addr + 2, s2);
    storeSymByte(addr + 3, s3);
    return std::monostate{};
  }

  std::monostate storeSymByte(int32_t addr, SymVal value) {
    // assume the input value is 8-bit symbolic value
    bool exists;
#ifdef USE_IMM
    auto it = memory.find(addr);
    exists = (it != nullptr);
#else
    auto it = memory.find(addr);
    exists = (it != memory.end());
#endif
    auto old_value = loadSymByte(addr);
    if (exists) {
      // We are overwriting an existing symbolic value
      symbolic_size -= old_value.size();
    }
    symbolic_size += value.size();
#ifdef USE_IMM
    memory.set(addr, value);
#else
    memory[addr] = value;
#endif
    return std::monostate{};
  }

  std::monostate reset() {
#ifdef USE_IMM
    memory = immer::map_transient<int, SymVal>();
#else
    memory.clear();
#endif
    return std::monostate{};
  }

  int total_sym_size() const { return symbolic_size; }
};

static SymMemory_t SymMemory;

static std::monostate memoryInitialize(int32_t offset,
                                       const std::string &data) {
  // initialize concrete memory
  for (size_t i = 0; i < data.size(); ++i) {
    Memory.storeInt(offset, i, static_cast<uint8_t>(data[i]));
  }
  // initialize symbolic memory
  for (size_t i = 0; i < data.size(); ++i) {
    SymMemory.storeSymByte(offset + i,
                           makeSmallBV(8, static_cast<uint8_t>(data[i])));
  }
  return {};
}

using NumMap = std::unordered_map<int, Num>;

class ImmNumMapBox {
public:
  ImmNumMapBox(const NumMap &sym_env)
      : map_ptr(std::make_shared<NumMap>(
            sym_env) /* create a immutable copy of SymEnv */
        ) {}

  NumMap persistent() const {
    return *map_ptr; // return a copy of the map
  }

private:
  std::shared_ptr<NumMap> map_ptr;
};

class SymEnv_t {
public:
  SymEnv_t() : map(), imm_map_box(map) {}

  Num read(const Symbol &symbol) {
#if DEBUG
    std::cout << "Read symbol: " << symbol.get_id()
              << " from symbolic environment" << std::endl;
    std::cout << "Current symbolic environment: " << to_string() << std::endl;
#endif
    map.try_emplace(symbol.get_id(), Num(I32V(0)));
    return map.at(symbol.get_id());
  }

  Num read(SymVal sym) {
    // Read the value of a symbolic value from the environment, it will update
    // the environment if the key does not exist.
    auto symbol = dynamic_cast<Symbol *>(sym.symptr.get());
    assert(symbol);
    return read(*symbol);
  }

  void update(NumMap new_env) {
    map = std::move(new_env);
    imm_map_box = ImmNumMapBox(map);
  }

  // Absorb another symbolic environment into this one, if some keys not exist
  // in another environment and exist in this one, they will be kept unchanged.
  void absorb(const NumMap &other) {
    for (const auto &[id, num] : other) {
      map[id] = num;
    }
    imm_map_box = ImmNumMapBox(map);
  }

  std::string to_string() const {
    std::string result;
    result += "(\n";
    for (const auto &[id, num] : map) {
      result +=
          "  (" + std::to_string(id) + "->" + std::to_string(num.value) + ")\n";
    }
    result += ")";
    return result;
  }

  size_t size() const { return map.size(); }

  ImmNumMapBox get_num_map() const { return imm_map_box; }

private:
  NumMap map; // The symbolic environment, a vector of Num
  ImmNumMapBox imm_map_box;
};

static SymEnv_t SymEnv;

// A snapshot of the symbolic state and execution context (control)
class Snapshot_t {
public:
  explicit Snapshot_t(Cont_t cont, MCont_t mcont, SymStack_t stack,
                      SymFrames_t frames,
                      SymFrames_t globals, SymMemory_t memory, ImmNumMapBox num_map /* Current num map that corresponds to the symbolic environment */);

  SymStack_t get_stack() const { return stack; }
  SymFrames_t get_frames() const { return frames; }
  SymFrames_t get_globals() const { return globals; }
  SymMemory_t get_memory() const { return memory; }

  std::monostate resume_execution(NodeBox *node) const;
  std::monostate resume_execution_by_model(NodeBox *node,
                                           z3::model &model) const;

  static double cost_of_snapshot();

private:
  SymStack_t stack;
  SymFrames_t frames;
  SymFrames_t globals;
  SymMemory_t memory;
  // The continuation at the snapshot point
  Cont_t cont;
  MCont_t mcont;
  ImmNumMapBox num_map;
  void restore_states_to_global() const;
};

static SymFrames_t SymFrames;
static SymFrames_t SymGlobals;

static Control makeControl(Cont_t cont, MCont_t mcont) {
  return Control(cont, mcont);
}

static Snapshot_t makeSnapshot(Control control) {
  // create a snapshot from the current symbolic states and the control
  return Snapshot_t(control.cont, control.mcont, SymStack, SymFrames,
                    SymGlobals, SymMemory, SymEnv.get_num_map());
}

struct Node;

struct NodeBox {
  explicit NodeBox(NodeBox *parent);
  std::unique_ptr<Node> node;
  NodeBox *parent;
  std::optional<double> cost;
  double instr_cost;

  bool fillIfElseNode(SymVal cond, int id);
  bool fillCallIndirectNode(SymVal cond, int id);
  std::monostate fillFinishedNode();
  std::monostate fillFailedNode();
  std::monostate fillUnreachableNode();
  std::monostate fillSnapshotNode(Snapshot_t snapshot);
  std::monostate fillNotToExploreNode();
  bool isUnexplored() const;
  bool isSnapshotNode() const;
  std::vector<SymVal> collect_path_conds();
  void reach_here(std::function<void()>);
};

struct Node {
  virtual ~Node(){};
  virtual std::string to_string() = 0;
  void to_graphviz(std::ostream &os) {
    os << "digraph G {\n";
    os << "  rankdir=TB;\n";
    os << "  node [shape=box, style=filled, fillcolor=lightblue];\n";
    current_id = 0;
    generate_dot(os, -1, "");

    os << "}\n";
  }
  virtual void generate_dot(std::ostream &os, int parent_dot_id,
                            const std::string &edge_label) = 0;

protected:
  // Counter for unique node IDs across the entire graph, only for generating
  // graphviz purpose
  static int current_id;
  void graphviz_node(std::ostream &os, const int node_id,
                     const std::string &label, const std::string &shape,
                     const std::string &fillcolor) {
    os << "  node" << node_id << " [label=\"" << label << "\", shape=" << shape
       << ", style=filled, fillcolor=" << fillcolor << "];\n";
  }

  void graphviz_edge(std::ostream &os, int from_id, int target_id,
                     const std::string &edge_label) {
    os << "  node" << from_id << " -> node" << target_id;
    if (!edge_label.empty()) {
      os << " [label=\"" << edge_label << "\"]";
    }
    os << ";\n";
  }
};

// TODO: use this header file in multiple compilation units will cause problems
// during linking
int Node::current_id = 0;

struct IfElseNode : Node {
  SymVal cond;
  std::unique_ptr<NodeBox> true_branch;
  std::unique_ptr<NodeBox> false_branch;
  int id;

  IfElseNode(SymVal cond, NodeBox *parent, int id)
      : cond(cond), true_branch(std::make_unique<NodeBox>(parent)),
        false_branch(std::make_unique<NodeBox>(parent)), id(id) {}

  std::string to_string() override {
    std::string result = "IfElseNode {\n";
    result += "  true_branch: ";
    if (true_branch) {
      result += true_branch->node->to_string();
    } else {
      result += "nullptr";
    }
    result += "\n";

    result += "  false_branch: ";
    if (false_branch) {
      result += false_branch->node->to_string();
    } else {
      result += "nullptr";
    }
    result += "\n";
    result += "}";
    return result;
  }

  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override {
    int current_node_dot_id = current_id;
    current_id += 1;

    graphviz_node(os, current_node_dot_id, "If", "diamond", "lightyellow");

    // Draw edge from parent if this is not the root node
    if (parent_dot_id != -1) {
      graphviz_edge(os, parent_dot_id, current_node_dot_id, edge_label);
    }
    assert(true_branch != nullptr);
    assert(true_branch->node != nullptr);
    true_branch->node->generate_dot(os, current_node_dot_id, "true");
    assert(false_branch != nullptr);
    assert(false_branch->node != nullptr);
    false_branch->node->generate_dot(os, current_node_dot_id, "false");
  }
};

struct CallIndirectNode : Node {
  SymVal cond;
  std::unordered_map<int, std::unique_ptr<NodeBox>> branches;
  std::unique_ptr<NodeBox> otherwise_branch;
  int id;
  CallIndirectNode(SymVal cond, NodeBox *parent, int id)
      : cond(cond), id(id),
        otherwise_branch(std::make_unique<NodeBox>(parent)) {}
  std::string to_string() override {
    std::string result = "CallIndirectNode {\n";
    for (const auto &pair : branches) {
      result += "  branch " + std::to_string(pair.first) + ": ";
      if (pair.second && pair.second->node) {
        result += pair.second->node->to_string();
      } else {
        result += "nullptr";
      }
      result += "\n";
    }
    result += "}";
    return result;
  }

  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override {
    int current_node_dot_id = current_id;
    current_id += 1;

    graphviz_node(os, current_node_dot_id, "Branch", "diamond", "lightyellow");

    // Draw edge from parent if this is not the root node
    if (parent_dot_id != -1) {
      graphviz_edge(os, parent_dot_id, current_node_dot_id, edge_label);
    }
    for (const auto &pair : branches) {
      assert(pair.second != nullptr);
      assert(pair.second->node != nullptr);
      pair.second->node->generate_dot(os, current_node_dot_id,
                                      "branch " + std::to_string(pair.first));
    }
  }
};

struct UnExploredNode : Node {
  UnExploredNode() {}
  std::string to_string() override { return "UnexploredNode"; }

protected:
  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override {
    int current_node_dot_id = current_id++;
    graphviz_node(os, current_node_dot_id, "Unexplored", "octagon",
                  "lightgrey");

    if (parent_dot_id != -1) {
      graphviz_edge(os, parent_dot_id, current_node_dot_id, edge_label);
    }
  }
};

struct NotToExploreNode : Node {
  NotToExploreNode() {}
  std::string to_string() override { return "NotToExploreNode"; }

protected:
  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override {
    int current_node_dot_id = current_id++;
    graphviz_node(os, current_node_dot_id, "NotToExplore", "box", "grey");

    if (parent_dot_id != -1) {
      graphviz_edge(os, parent_dot_id, current_node_dot_id, edge_label);
    }
  }
};

struct SnapshotNode : Node {
  SnapshotNode(Snapshot_t snapshot) : snapshot(snapshot) {}
  std::string to_string() override { return "SnapshotNode"; }
  const Snapshot_t &get_snapshot() const { return snapshot; }
  Snapshot_t move_out_snapshot() { return std::move(snapshot); }

protected:
  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override {
    int current_node_dot_id = current_id++;
    graphviz_node(os, current_node_dot_id, "Snapshot", "box", "lightblue");

    if (parent_dot_id != -1) {
      graphviz_edge(os, parent_dot_id, current_node_dot_id, edge_label);
    }
  }

private:
  Snapshot_t snapshot;
};

struct Finished : Node {
  Finished() {}
  std::string to_string() override { return "FinishedNode"; }

protected:
  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override {
    int current_node_dot_id = current_id++;
    graphviz_node(os, current_node_dot_id, "Finished", "box", "lightgreen");

    if (parent_dot_id != -1) {
      graphviz_edge(os, parent_dot_id, current_node_dot_id, edge_label);
    }
  }
};

struct Failed : Node {
  Failed() {}
  std::string to_string() override { return "FailedNode"; }

protected:
  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override {
    int current_node_dot_id = current_id++;
    graphviz_node(os, current_node_dot_id, "Failed", "box", "red");

    if (parent_dot_id != -1) {
      graphviz_edge(os, parent_dot_id, current_node_dot_id, edge_label);
    }
  }
};

struct Unreachable : Node {
  Unreachable() {}
  std::string to_string() override { return "UnreachableNode"; }

protected:
  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override {
    int current_node_dot_id = current_id++;
    graphviz_node(os, current_node_dot_id, "Unreachable", "box", "orange");

    if (parent_dot_id != -1) {
      graphviz_edge(os, parent_dot_id, current_node_dot_id, edge_label);
    }
  }
};

inline NodeBox::NodeBox(NodeBox *parent)
    : node(std::make_unique<UnExploredNode>()),
      /* TODO: avoid allocation of unexplored node */
      parent(parent), cost(std::nullopt), instr_cost(0) {}

inline bool NodeBox::fillIfElseNode(SymVal cond, int id) {
  // fill the current NodeBox with an ifelse branch node when it's unexplored
  if (auto ptr = dynamic_cast<SnapshotNode *>(node.get())) {
    node = std::make_unique<IfElseNode>(cond, this, id);
    return true;
  } else if (dynamic_cast<UnExploredNode *>(node.get())) {
    node = std::make_unique<IfElseNode>(cond, this, id);
    return true;
  } else if (dynamic_cast<NotToExploreNode *>(node.get()) != nullptr) {
    assert(false &&
           "Unexpected traversal: arrived at a node marked 'NotToExplore'.");
    return false;
  }

  assert(
      dynamic_cast<IfElseNode *>(node.get()) != nullptr &&
      "Current node is not an Unexplored nor an IfElseNode, cannot fill it!");
  return false;
}

inline bool NodeBox::fillCallIndirectNode(SymVal cond, int id) {
  // fill the current NodeBox with a call_indirect branch node when it's
  // unexplored
  if (auto ptr = dynamic_cast<SnapshotNode *>(node.get())) {
    node = std::make_unique<CallIndirectNode>(cond, this, id);
    return true;
  } else if (dynamic_cast<UnExploredNode *>(node.get())) {
    node = std::make_unique<CallIndirectNode>(cond, this, id);
    return true;
  } else if (dynamic_cast<NotToExploreNode *>(node.get()) != nullptr) {
    assert(false &&
           "Unexpected traversal: arrived at a node marked 'NotToExplore'.");
    return false;
  }

  assert(
      dynamic_cast<CallIndirectNode *>(node.get()) != nullptr &&
      "Current node is not an Unexplored nor a CallIndirectNode, cannot fill "
      "it!");
  return false;
}

inline std::monostate NodeBox::fillSnapshotNode(Snapshot_t snapshot) {
  if (this->isUnexplored()) {
    node = std::make_unique<SnapshotNode>(snapshot);
  }
  return std::monostate();
}

inline std::monostate NodeBox::fillNotToExploreNode() {
  if (this->isUnexplored()) {
    node = std::make_unique<NotToExploreNode>();
  } else {
    assert(dynamic_cast<NotToExploreNode *>(node.get()) != nullptr);
  }
  return std::monostate();
}

inline std::monostate NodeBox::fillFinishedNode() {
  if (this->isUnexplored()) {
    node = std::make_unique<Finished>();
  } else {
    assert(dynamic_cast<Finished *>(node.get()) != nullptr);
  }
  return std::monostate();
}

inline std::monostate NodeBox::fillFailedNode() {
  if (this->isUnexplored()) {
    node = std::make_unique<Failed>();
  } else {
    assert(dynamic_cast<Failed *>(node.get()) != nullptr);
  }
  return std::monostate();
}

inline std::monostate NodeBox::fillUnreachableNode() {
  if (this->isUnexplored()) {
    node = std::make_unique<Unreachable>();
  } else {
    assert(dynamic_cast<Unreachable *>(node.get()) != nullptr);
  }
  return std::monostate();
}

inline bool NodeBox::isSnapshotNode() const {
  assert(node != nullptr);
  return dynamic_cast<SnapshotNode *>(node.get()) != nullptr;
}

inline bool NodeBox::isUnexplored() const {
  assert(node != nullptr);
  if (dynamic_cast<UnExploredNode *>(node.get()) != nullptr) {
    return true;
  }
  if (this->isSnapshotNode()) {
    return true;
  }
  return false;
}

inline std::vector<SymVal> NodeBox::collect_path_conds() {
  auto box = this;
  auto result = std::vector<SymVal>();
  while (box->parent) {
    auto parent = box->parent;
    if (auto if_else_node = dynamic_cast<IfElseNode *>(parent->node.get())) {
      if (if_else_node->true_branch.get() == box) {
        // If the current box is the true branch, add the condition
        result.push_back(if_else_node->cond);
      } else if (if_else_node->false_branch.get() == box) {
        // If the current box is the false branch, add the negated condition
        result.push_back(if_else_node->cond.negate());
      } else {
        throw std::runtime_error("Unexpected node structure in explore tree");
      }
    } else if (auto call_indirect_node =
                   dynamic_cast<CallIndirectNode *>(parent->node.get())) {
      // Find which branch we are in
      bool found = false;
      for (const auto &pair : call_indirect_node->branches) {
        if (pair.second.get() == box) {
          // We are in this branch
          // Add the condition that leads to this branch
          result.push_back(
              call_indirect_node->cond.eq(Concrete(I32V(pair.first))));
          found = true;
          break;
        }
      }
      if (!found) {
        // We must be in the otherwise branch
        if (call_indirect_node->otherwise_branch.get() != box) {
          throw std::runtime_error("Unexpected node structure in explore tree");
        }
        // Add the negated conditions for all other branches
        SymVal negated_conditions = Concrete(I32V(1)); // true
        for (const auto &pair : call_indirect_node->branches) {
          negated_conditions = negated_conditions.bitwise_and(
              call_indirect_node->cond.neq(Concrete(I32V(pair.first))));
        }
        result.push_back(negated_conditions);
      }
    }
    // Move to parent
    box = box->parent;
  }
  return result;
}

inline Snapshot_t::Snapshot_t(Cont_t cont, MCont_t mcont, SymStack_t stack,
                              SymFrames_t frames, SymFrames_t globals,
                              SymMemory_t memory, ImmNumMapBox num_map)
    : stack(std::move(stack)), frames(std::move(frames)),
      globals(std::move(globals)), memory(std::move(memory)), cont(cont),
      mcont(mcont), num_map(num_map) {
  Profile.step(StepProfileKind::SNAPSHOT_CREATE);
#ifdef DEBUG
  std::cout << "Creating snapshot of size " << stack.size() << std::endl;
#endif
}

const double INSTR_COST_SCALING_FACTOR = 1E-03;

inline double Snapshot_t::cost_of_snapshot() {
  auto stack_sym_size = SymStack.total_sym_size();
  assert(stack_sym_size >= 0);
  auto frame_sym_size = SymFrames.total_sym_size();
  assert(frame_sym_size >= 0);
  auto memory_sym_size = SymMemory.total_sym_size();
  assert(memory_sym_size >= 0);
  auto global_sym_size = SymGlobals.total_sym_size();
  assert(global_sym_size >= 0);
  // The speed ratio between symbolic expression instantiation and WebAssembly
  // instruction execution, given by benchmark results
  return INSTR_COST_SCALING_FACTOR *
         (stack_sym_size + frame_sym_size + memory_sym_size + global_sym_size);
}

struct OverallResult {
  int unexplored_count = 0;
  int finished_count = 0;
  int failed_count = 0;
  int not_to_explore_count = 0;
  int unreachable_count = 0;

  void print() {
    std::cout << "Explore Tree Overall Result:" << std::endl;
    std::cout << "  Unexplored paths: " << unexplored_count << std::endl;
    std::cout << "  Finished paths: " << finished_count << std::endl;
    std::cout << "  Failed paths: " << failed_count << std::endl;
    std::cout << "  Unreachable paths: " << unreachable_count << std::endl;
    std::cout << "  NotToExplore paths: " << not_to_explore_count << std::endl;
  }
};

class ExploreTree_t {
public:
  explicit ExploreTree_t()
      : root(std::make_unique<NodeBox>(nullptr)), cursor(root.get()) {}

  void reset_cursor() {
    GENSYM_INFO("Resetting cursor to root");
    // Reset the cursor to the root of the tree
    cursor = root.get();
  }

  void clear() {
    GENSYM_INFO("Clearing the explore tree");
    root = std::make_unique<NodeBox>(nullptr);
    cursor = root.get();
    true_branch_cov_map.clear();
    false_branch_cov_map.clear();
  }

  void set_cursor(NodeBox *new_cursor) {
    GENSYM_INFO("Setting cursor to a new node");
    cursor = new_cursor;
    assert(dynamic_cast<SnapshotNode *>(cursor->node.get()) != nullptr);
  }

  std::monostate fillFinishedNode() { return cursor->fillFinishedNode(); }

  std::monostate fillFailedNode() { return cursor->fillFailedNode(); }

  std::monostate fillIfElseNode(SymVal cond, int id) {
    if (cursor->fillIfElseNode(cond, id)) {
      auto if_else_node = dynamic_cast<IfElseNode *>(cursor->node.get());
      register_new_node(if_else_node->true_branch.get());
      register_new_node(if_else_node->false_branch.get());
    }
    return std::monostate();
  }

  std::monostate fillCallIndirectNode(SymVal cond, int id) {
    if (cursor->fillCallIndirectNode(cond, id)) {
      auto indirect_node = dynamic_cast<CallIndirectNode *>(cursor->node.get());
      register_new_node(indirect_node->otherwise_branch.get());
    }
    return std::monostate();
  }

  std::monostate fillNotToExploredNode() {
    return cursor->fillNotToExploreNode();
  }

  std::vector<SymVal> collect_current_path_conds() {
    return cursor->collect_path_conds();
  }

  bool worth_to_create_snapshot() {
    if (!ENABLE_COST_MODEL) {
      // If we are not using cost model, always create snapshot
      return REUSE_SNAPSHOT;
    }
    // find out the best way to reach the current position via our cost model
    auto snapshot_cost = Snapshot_t::cost_of_snapshot();
    double re_execution_cost = cursor->instr_cost;
    if (snapshot_cost <= re_execution_cost) {
      GENSYM_INFO("Snapshot is worth to create");
    } else {
      GENSYM_INFO("Snapshot is NOT worth to create");
    }
    return snapshot_cost <= re_execution_cost;
  }

  std::monostate moveCursor(bool branch, Control control) {
    Profile.step(StepProfileKind::CURSOR_MOVE);
    assert(cursor != nullptr);
    auto if_else_node = dynamic_cast<IfElseNode *>(cursor->node.get());
    assert(
        if_else_node != nullptr &&
        "Can't move cursor when the branch node is not initialized correctly!");
    double cost_from_parent = CostManager.dump_instr_cost();
    double cost_from_root =
        cost_from_parent + (cursor->parent ? cursor->parent->instr_cost : 0);
    cursor->instr_cost = cost_from_root;
    // GENSYM_INFO(
    //     "Cursor move cost from parent: " + std::to_string(cost_from_parent) +
    //     ", total cost from root: " + std::to_string(cost_from_root));
    if (branch) {
      true_branch_cov_map[if_else_node->id] = true;
      if (!if_else_node->false_branch->isSnapshotNode() &&
          worth_to_create_snapshot()) {
        auto snapshot = makeSnapshot(control);
        if_else_node->false_branch->fillSnapshotNode(snapshot);
      } else {
        // Do nothing, the initial value of the branch is an unexplored node
      }
      cursor = if_else_node->true_branch.get();
    } else {
      false_branch_cov_map[if_else_node->id] = true;
      if (!if_else_node->true_branch->isSnapshotNode() &&
          worth_to_create_snapshot()) {
        auto snapshot = makeSnapshot(control);
        if_else_node->true_branch->fillSnapshotNode(snapshot);
      } else {
        // Do nothing, the initial value of the branch is an unexplored node
      }
      cursor = if_else_node->false_branch.get();
    }
    CostManager.reset_timer();
    return std::monostate();
  }

  std::monostate moveCursorNoControl(bool branch) {
    Profile.step(StepProfileKind::CURSOR_MOVE);
    assert(cursor != nullptr);
    auto if_else_node = dynamic_cast<IfElseNode *>(cursor->node.get());
    assert(
        if_else_node != nullptr &&
        "Can't move cursor when the branch node is not initialized correctly!");
    int cost_from_parent = CostManager.dump_instr_cost();
    int cost_from_root =
        cost_from_parent + (cursor->parent ? cursor->parent->instr_cost : 0);
    cursor->instr_cost = cost_from_root;
    if (branch) {
      true_branch_cov_map[if_else_node->id] = true;
      if_else_node->false_branch->fillNotToExploreNode();
      cursor = if_else_node->true_branch.get();
    } else {
      assert(false &&
             "moveCursorNoControl should not be used for false branch");
    }
    CostManager.reset_timer();
    return std::monostate();
  }

  std::monostate moveCursorIndirect(int branch_index) {
    // Dont use snapshot reuse for untaken branches of indirect call
    Profile.step(StepProfileKind::CURSOR_MOVE);
    assert(cursor != nullptr);
    auto branch_node = dynamic_cast<CallIndirectNode *>(cursor->node.get());
    assert(branch_node != nullptr &&
           "Can't move cursor when the branch node is not initialized ");
    if (branch_node->branches.find(branch_index) ==
        branch_node->branches.end()) {
      // Create a new branch
      branch_node->branches[branch_index] = std::make_unique<NodeBox>(cursor);
      register_new_node(branch_node->branches[branch_index].get());
    }
    cursor = branch_node->branches[branch_index].get();
    int cost_from_parent = CostManager.dump_instr_cost();
    int cost_from_root =
        cost_from_parent + (cursor->parent ? cursor->parent->instr_cost : 0);
    cursor->instr_cost = cost_from_root;
    return std::monostate();
  }

  std::monostate print() {
    std::cout << root->node->to_string() << std::endl;
    return std::monostate();
  }

  std::monostate to_graphviz(std::ostream &os) {
    root->node->to_graphviz(os);
    return std::monostate();
  }

  std::monostate dump_graphviz(std::string filepath) {
    std::filesystem::path out_path(filepath);
    auto parent = out_path.parent_path();
    if (!parent.empty()) {
      std::error_code ec;
      std::filesystem::create_directories(parent, ec);
      if (ec) {
        throw std::runtime_error("Failed to create output directory: " +
                                 ec.message());
      }
    }
    std::ofstream ofs(filepath);
    if (!ofs.is_open()) {
      throw std::runtime_error("Failed to open " + filepath + "  for writing");
    }
    to_graphviz(ofs);
    return std::monostate();
  }

  OverallResult read_current_overall_result() {
    OverallResult result;
    std::vector<NodeBox *> stack;
    stack.push_back(root.get());

    while (!stack.empty()) {
      NodeBox *node = stack.back();
      stack.pop_back();

      if (auto if_else_node = dynamic_cast<IfElseNode *>(node->node.get())) {
        stack.push_back(if_else_node->true_branch.get());
        stack.push_back(if_else_node->false_branch.get());
      } else if (dynamic_cast<UnExploredNode *>(node->node.get())) {
        result.unexplored_count += 1;
      } else if (dynamic_cast<Finished *>(node->node.get())) {
        result.finished_count += 1;
      } else if (dynamic_cast<Failed *>(node->node.get())) {
        result.failed_count += 1;
      } else if (dynamic_cast<Unreachable *>(node->node.get())) {
        result.unreachable_count += 1;
      } else if (dynamic_cast<SnapshotNode *>(node->node.get())) {
        // Snapshot node is considered unexplored
        result.unexplored_count += 1;
      } else if (dynamic_cast<NotToExploreNode *>(node->node.get())) {
        result.not_to_explore_count += 1;
      } else if (auto call_indirect_node =
                     dynamic_cast<CallIndirectNode *>(node->node.get())) {
        for (const auto &pair : call_indirect_node->branches) {
          stack.push_back(pair.second.get());
        }
        stack.push_back(call_indirect_node->otherwise_branch.get());
      } else {
        throw std::runtime_error("Unknown node type in explore tree");
      }
    }
    return result;
  }

  std::monostate print_overall_result() {}

  NodeBox *pick_unexplored() {
    // Pick an unexplored node from the tree
    // For now, we just iterate through the tree and return the first unexplored
    return pick_unexplored_of(root.get());
  }
  std::vector<bool> true_branch_cov_map;
  std::vector<bool> false_branch_cov_map;
  bool all_branch_covered() const {
    for (bool covered : true_branch_cov_map) {
      if (!covered)
        return false;
    }
    for (bool covered : false_branch_cov_map) {
      if (!covered)
        return false;
    }
    return true;
  }

  NodeBox *get_root() const { return root.get(); }

  void register_new_node_collector(std::function<void(NodeBox *)> func) {
    new_node_collectors.push_back(func);
  }

private:
  NodeBox *pick_unexplored_of(NodeBox *node) {
    if (node->isUnexplored()) {
      return node;
    }
    auto if_else_node = dynamic_cast<IfElseNode *>(node->node.get());
    if (if_else_node) {
      NodeBox *result = pick_unexplored_of(if_else_node->true_branch.get());
      if (result) {
        return result;
      }
      return pick_unexplored_of(if_else_node->false_branch.get());
    }
    return nullptr; // No unexplored node found
  }
  void register_new_node(NodeBox *node) {
    for (auto &func : new_node_collectors) {
      func(node);
    }
  }
  std::unique_ptr<NodeBox> root;
  NodeBox *cursor;
  std::vector<std::function<void(NodeBox *)>> new_node_collectors;
};

static ExploreTree_t ExploreTree;

static std::monostate reset_stacks() {
  Stack.reset();
  SymStack.reset();
  Frames.reset();
  SymFrames.reset();
  Memory.reset();
  SymMemory.reset();
  initRand();
  return std::monostate{};
}

[[deprecated]] inline void
NodeBox::reach_here(std::function<void()> entrypoint) {
  // reach the node of exploration tree with given input (symbolic environment)
  if (auto snapshot = dynamic_cast<SnapshotNode *>(node.get())) {
    assert(REUSE_SNAPSHOT);
    auto snap = snapshot->get_snapshot();
    snap.resume_execution(this);
    return;
  } else if (parent == nullptr) {
    // if it's the root node, the only way to reach here is to reset everything
    // and start a new execution
    assert(this == ExploreTree.get_root() &&
           "Only the root node can have no parent");
    auto timer = ManagedTimer(TimeProfileKind::INSTR);
    ExploreTree.reset_cursor();
    reset_stacks();
    entrypoint();
    return;
  }
  // Reach the parent node, then from the parent node, we can reach here
  // TODO: short circuit the lookup
  parent->reach_here(entrypoint);
  return;
}

struct EvalRes {
  Num value;
  int width; // in bits
  EvalRes(Num value, int width) : value(value), width(width) {}
};

// TODO: reduce the re-computation of the same symbolic expression, it's better
// if it can be done by the smt solver
static EvalRes eval_sym_expr(const SymVal &sym, SymEnv_t &sym_env) {
  Profile.step(StepProfileKind::SYM_EVAL);
  assert(sym.symptr != nullptr && "Symbolic expression is null");
  if (auto concrete = dynamic_cast<SymConcrete *>(sym.symptr.get())) {
    return EvalRes(concrete->value, 32);
  } else if (auto extract = dynamic_cast<SymExtract *>(sym.symptr.get())) {
    auto res = eval_sym_expr(extract->value, sym_env);
    int high = extract->high;
    int low = extract->low;
    assert(high >= low && "Invalid extract range");
    int size = high - low + 1; // size in bytes
    int64_t mask = (1LL << (size * 8)) - 1;
    int64_t extracted_value = (res.value.toInt() >> ((low - 1) * 8)) & mask;
    return EvalRes(Num(I64V(extracted_value)), size * 8);
  } else if (auto smallbv = dynamic_cast<SmallBV *>(sym.symptr.get())) {
    return EvalRes(Num(I64V(smallbv->get_value())), smallbv->get_size());
  } else if (auto operation = dynamic_cast<SymBinary *>(sym.symptr.get())) {
    // If it's a operation, we need to evaluate it
    auto lhs_res = eval_sym_expr(operation->lhs, sym_env);
    auto rhs_res = eval_sym_expr(operation->rhs, sym_env);
    auto lhs = lhs_res.value;
    auto rhs = rhs_res.value;
    auto lhs_width = lhs_res.width;
    auto rhs_width = rhs_res.width;
    switch (operation->op) {
    case ADD:
      if (lhs_width == 32 && rhs_width == 32) {
        return EvalRes(lhs.i32_add(rhs), 32);
      } else {
        assert(false && "TODO");
      }
    case SUB:
      if (lhs_width == 32 && rhs_width == 32) {
        return EvalRes(lhs.i32_sub(rhs), 32);
      } else {
        assert(false && "TODO");
      }
    case MUL:
      if (lhs_width == 32 && rhs_width == 32) {
        return EvalRes(lhs.i32_mul(rhs), 32);
      } else {
        assert(false && "TODO");
      }
    case DIV:
      if (lhs_width == 32 && rhs_width == 32) {
        return EvalRes(lhs.i32_div_s(rhs), 32);
      } else {
        assert(false && "TODO");
      }
    case LT:
      if (lhs_width == 32 && rhs_width == 32) {
        return EvalRes(lhs.i32_lt_s(rhs), 32);
      } else {
        assert(false && "TODO");
      }
    case LEQ:
      if (lhs_width == 32 && rhs_width == 32) {
        return EvalRes(lhs.i32_le_s(rhs), 32);
      } else {
        assert(false && "TODO");
      }
    case GT:
      if (lhs_width == 32 && rhs_width == 32) {
        return EvalRes(lhs.i32_gt_s(rhs), 32);
      } else {
        assert(false && "TODO");
      }
    case GEQ:
      if (lhs_width == 32 && rhs_width == 32) {
        return EvalRes(lhs.i32_ge_s(rhs), 32);
      } else {
        assert(false && "TODO");
      }
    case NEQ:
      if (lhs_width == 32 && rhs_width == 32) {
        return EvalRes(lhs.i32_ne(rhs), 32);
      } else {
        assert(false && "TODO");
      }
    case EQ:
      if (lhs_width == 32 && rhs_width == 32) {
        return EvalRes(lhs.i32_eq(rhs), 32);
      } else {
        assert(false && "TODO");
      }
    case B_AND:
      if (lhs_width == 32 && rhs_width == 32) {
        return EvalRes(lhs.i32_and(rhs), 32);
      } else {
        assert(false && "TODO");
      }
    case CONCAT: {
      auto lhs_width = lhs_res.width;
      auto rhs_width = rhs_res.width;
      auto conc_value = (lhs.value << rhs_width) | (rhs.value);
      auto new_width = lhs_width + rhs_width;
      return EvalRes(Num(I64V(conc_value)), new_width);
    }
    default:
      assert(false && "Operation not supported in evaluation");
    }
  } else if (auto symbol = dynamic_cast<Symbol *>(sym.symptr.get())) {
    auto sym_id = symbol->get_id();
    GENSYM_INFO("Reading symbol: " + std::to_string(sym_id));
    return EvalRes(sym_env.read(sym), 32);
  }
  throw std::runtime_error("Not supported symbolic expression");
}

inline EvalRes eval_sym_expr_by_model(const SymVal &sym, z3::model &model);

static void resume_conc_stack(const SymStack_t &sym_stack, Stack_t &stack,
                              SymEnv_t &sym_env) {
  stack.resize(sym_stack.size());
  for (size_t i = 0; i < sym_stack.size(); ++i) {
    auto sym = sym_stack[i];
    auto res = eval_sym_expr(sym, sym_env);
    auto conc = res.value;
    stack.set_from_front(i, conc);
  }
}

static void resume_conc_stack_by_model(const SymStack_t &sym_stack,
                                       Stack_t &stack, z3::model &model) {
  GENSYM_INFO("Restoring concrete stack from symbolic stack");
  stack.resize(sym_stack.size());
  for (size_t i = 0; i < sym_stack.size(); ++i) {
    auto sym = sym_stack[i];
    auto res = eval_sym_expr_by_model(sym, model);
    auto conc = res.value;
    stack.set_from_front(i, conc);
  }
}

static void resume_conc_frames(const SymFrames_t &sym_frame, Frames_t &frames,
                               SymEnv_t &sym_env) {
  GENSYM_INFO("Restoring concrete frames from symbolic frames");
  frames.resize(sym_frame.size());
  for (size_t i = 0; i < sym_frame.size(); ++i) {
    auto sym = sym_frame[i];
    assert(sym.symptr != nullptr);
    auto res = eval_sym_expr(sym, sym_env);
    auto conc = res.value;
    frames.set_from_front(i, conc);
  }
}

static void resume_conc_frames_by_model(const SymFrames_t &sym_frame,
                                        Frames_t &frames, z3::model &model) {
  GENSYM_INFO("Restoring concrete frames from symbolic frames");
  frames.resize(sym_frame.size());
  for (size_t i = 0; i < sym_frame.size(); ++i) {
    auto sym = sym_frame[i];
    assert(sym.symptr != nullptr);
    auto res = eval_sym_expr_by_model(sym, model);
    auto conc = res.value;
    frames.set_from_front(i, conc);
  }
}

static void resume_conc_memory(const SymMemory_t &sym_memory, Memory_t &memory,
                               SymEnv_t &sym_env) {
  GENSYM_INFO("Restoring concrete memory from symbolic memory");
  memory.reset();
  for (const auto &pair : sym_memory.memory) {
    int32_t addr = pair.first;
    SymVal sym = pair.second;
    assert(sym.symptr != nullptr);
    auto res = eval_sym_expr(sym, sym_env);
    auto conc = res.value;
    assert(res.width == 8 && "Memory should only store bytes");
    memory.store_byte(addr, conc.value & 0xFF);
  }
}

static void resume_conc_memory_by_model(const SymMemory_t &sym_memory,
                                        Memory_t &memory, z3::model &model) {
  GENSYM_INFO("Restoring concrete memory from symbolic memory");
  memory.reset();
  for (const auto &pair : sym_memory.memory) {
    int32_t addr = pair.first;
    SymVal sym = pair.second;
    assert(sym.symptr != nullptr);
    auto res = eval_sym_expr_by_model(sym, model);
    auto conc = res.value;
    assert(res.width == 8 && "Memory should only store bytes");
    memory.store_byte(addr, conc.value & 0xFF);
  }
}

static void resume_conc_states(const SymStack_t &sym_stack,
                               const SymFrames_t &sym_frame,
                               const SymFrames_t &sym_globals,
                               const SymMemory_t &sym_memory, Stack_t &stack,
                               Frames_t &frames, Frames_t &globals,
                               Memory_t &memory, SymEnv_t &sym_env) {
  resume_conc_stack(sym_stack, stack, sym_env);
  resume_conc_frames(sym_frame, frames, sym_env);
  resume_conc_frames(sym_globals, globals, sym_env);
  resume_conc_memory(sym_memory, memory, sym_env);
}

static void resume_conc_states_by_model(const SymStack_t &sym_stack,
                                        const SymFrames_t &sym_frame,
                                        const SymFrames_t &sym_globals,
                                        const SymMemory_t &sym_memory,
                                        Stack_t &stack, Frames_t &frames,
                                        Frames_t &globals, Memory_t &memory,
                                        z3::model &model) {
  resume_conc_stack_by_model(sym_stack, stack, model);
  resume_conc_frames_by_model(sym_frame, frames, model);
  resume_conc_frames_by_model(sym_globals, globals, model);
  resume_conc_memory_by_model(sym_memory, memory, model);
}

inline void Snapshot_t::restore_states_to_global() const {
  // Restore the symbolic state from the snapshot
  GENSYM_INFO("Reusing symbolic state from snapshot");
  SymStack = stack;
  SymFrames = frames;
  SymMemory = memory;
  SymGlobals = globals;
}

inline std::monostate
Snapshot_t::resume_execution_by_model(NodeBox *node, z3::model &model) const {
  // Reset explore tree's cursor and restore symbolic states
  ExploreTree.set_cursor(node);
  restore_states_to_global();

  {
    auto timer = ManagedTimer(TimeProfileKind::RESUME_SNAPSHOT);
    // Restore the concrete states from the symbolic states
    resume_conc_states_by_model(stack, frames, globals, memory, Stack, Frames,
                                Globals, Memory, model);
  }
  // Resume execution from the continuation
  auto timer = ManagedTimer(TimeProfileKind::INSTR);
  CostManager.reset_timer();
  CURRENT_MCONT = mcont;
  return cont(std::monostate{});
}

[[deprecated]] inline std::monostate
Snapshot_t::resume_execution(NodeBox *node) const {
  // Reset explore tree's cursor and restore symbolic states
  ExploreTree.set_cursor(node);
  restore_states_to_global();
  {
    auto timer = ManagedTimer(TimeProfileKind::RESUME_SNAPSHOT);
    // Restore the concrete states from the symbolic states
    resume_conc_states(stack, frames, globals, memory, Stack, Frames, Globals,
                       Memory, SymEnv);
  }

  // Resume execution from the continuation
  auto timer = ManagedTimer(TimeProfileKind::INSTR);
  CURRENT_MCONT = mcont;
  return cont(std::monostate{});
}

#endif // WASM_SYMBOLIC_RT_HPP
