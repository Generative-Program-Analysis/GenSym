#ifndef WASM_SYMVAL_HPP
#define WASM_SYMVAL_HPP
#include "concrete_num.hpp"
#include <memory>
#include <unordered_map>
#include <unordered_set>

enum BinOperation {
  ADD,     // Addition
  SUB,     // Subtraction
  MUL,     // Multiplication
  DIV,     // Division
  AND,     // Logical AND
  OR,      // Logical OR
  EQ_BOOL, // Equal (return a boolean) TODO: remove bv version of comparison ops
  NEQ_BOOL, // Not equal (return a boolean)
  LT_BOOL,  // Less than (return a boolean)
  LTU_BOOL, // Unsigned less than (return a boolean)
  LEQ_BOOL, // Less than or equal (return a boolean)
  GT_BOOL,  // Greater than (return a boolean)
  GTU_BOOL, // Unsigned greater than (return a boolean)
  GEQ_BOOL, // Greater than or equal (return a boolean)
  GEU_BOOL, // Unsigned greater than or equal (return a boolean)
  SHR_U,    // Shift right unsigned
  SHR_S,    // Shift right signed
  REM_U,    // Unsigned remainder
  B_AND,    // Bitwise AND
  B_XOR,    // Bitwise XOR
  B_OR,     // Bitwise OR
  CONCAT,   // Byte-level concatenation
};

enum UnaryOperation {
  NOT,     // bool not
  BOOL2BV, // bool to bitvector,
  EXTEND,   // bitvector extension, extend i32 to i64
};

class Symbolic;

struct SymVal {
  std::shared_ptr<Symbolic> symptr;

  SymVal() = delete;
  SymVal(std::shared_ptr<Symbolic> symptr) : symptr(symptr) {}

  // data structure operations
  SymVal makeSymbolic(int width) const;

  // bitvector arithmetic operations
  SymVal is_zero() const;
  SymVal add(const SymVal &other) const;
  SymVal minus(const SymVal &other) const;
  SymVal mul(const SymVal &other) const;
  SymVal div(const SymVal &other) const;
  SymVal eq_bool(const SymVal &other) const;
  SymVal neq_bool(const SymVal &other) const;
  SymVal land(const SymVal &other) const;
  SymVal lor(const SymVal &other) const;
  SymVal eq(const SymVal &other) const;
  SymVal neq(const SymVal &other) const;
  SymVal lt(const SymVal &other) const;
  SymVal ltu(const SymVal &other) const;
  SymVal le(const SymVal &other) const;
  SymVal gt(const SymVal &other) const;
  SymVal gtu(const SymVal &other) const;
  SymVal ge(const SymVal &other) const;
  SymVal geu(const SymVal &other) const;
  SymVal shr_u(const SymVal &other) const;
  SymVal shr_s(const SymVal &other) const;
  SymVal bv_negate() const;
  SymVal bool_not() const;
  SymVal bitwise_and(const SymVal &other) const;
  SymVal bitwise_xor(const SymVal &other) const;
  SymVal bitwise_or(const SymVal &other) const;
  SymVal concat(const SymVal &other) const;
  SymVal extract(int high, int low) const;
  SymVal bv2bool() const;
  SymVal bool2bv() const;
  SymVal rem_u(const SymVal &other) const;
  SymVal extend_to_i64() const; // only for i32 symbolic values, extend to i64 by sign extension
  // TODO: add bitwise operations, and use the underlying bitvector theory

  bool is_concrete() const;

  static SymVal get_witness_symbol();

  Symbolic *operator->() const { return symptr.get(); }
  bool operator==(const SymVal &other) const { return symptr == other.symptr; }
};

struct SymValHash {
  size_t operator()(const SymVal &key) const {
    return std::hash<void *>{}(key.symptr.get());
  }
};

using SymValSet = std::unordered_set<SymVal, SymValHash>;

template <typename TValue>
using SymValMap = std::unordered_map<SymVal, TValue, SymValHash>;

template <typename... Args> inline bool allConcrete(const Args &...args) {
  static_assert((std::is_same_v<Args, SymVal> && ...),
                "all_concrete only accepts SymVal arguments");
  return (... && args.is_concrete());
}

inline SymVal Concrete(Num num, int width);

#endif // WASM_SYMVAL_HPP
