#ifndef WASM_SYMVAL_REPR_HPP
#define WASM_SYMVAL_REPR_HPP

#include "symval.hpp"
#include "z3++.h"

#include <cassert>
#include <optional>
#include <set>
#include <tuple>

enum BinOperation {
  ADD,      // Addition
  SUB,      // Subtraction
  MUL,      // Multiplication
  DIV,      // Division
  DIV_U,    // Unsigned division
  AND,      // Logical AND
  OR,       // Logical OR
  EQ_BOOL,  // Equal (return a boolean) TODO: remove bv version of comparison ops
  NEQ_BOOL, // Not equal (return a boolean)
  LT_BOOL,  // Less than (return a boolean)
  LTU_BOOL, // Unsigned less than (return a boolean)
  LEQ_BOOL, // Less than or equal (return a boolean)
  LEU_BOOL, // Unsigned less than or equal (return a boolean)
  GT_BOOL,  // Greater than (return a boolean)
  GTU_BOOL, // Unsigned greater than (return a boolean)
  GEQ_BOOL, // Greater than or equal (return a boolean)
  GEU_BOOL, // Unsigned greater than or equal (return a boolean)
  SHL,      // Shift left
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
  EXTEND,  // bitvector extension, extend i32 to i64
};

enum ValueKind { KindBV, KindBool, KindFP };

class Symbolic {
public:
  Symbolic() {}
  virtual ~Symbolic() = default; // Make Symbolic polymorphic

  virtual int size() = 0;
  virtual ValueKind value_kind() = 0;
  virtual int width() = 0;
  virtual z3::expr z3_expr();

private:
  z3::expr build_z3_expr_aux();
  std::optional<z3::expr> _z3_expr;
};

class Symbol : public Symbolic {
public:
  // TODO: add type information to determine the size of bitvector
  // for now we just assume that only i32 will be used
  Symbol(int id, int width, ValueKind kind);

  int get_id() const;

  int size() override;
  ValueKind value_kind() override;
  int width() override;

private:
  int id;
  int _width;
  ValueKind _kind;
};

class Witness : public Symbolic {
public:
  int size() override;
  ValueKind value_kind() override;
  int width() override;
};

class SymConcrete : public Symbolic {
public:
  Num value;
  ValueKind kind;

  SymConcrete(Num num, ValueKind kind, int width);

  int size() override;
  ValueKind value_kind() override;
  int width() override;

private:
  int _width;
};

inline int count_dag_size(Symbolic &val);

// Extract is different from other operations, it only has one symbolic operand,
// the other two operands are constants
// Extract from value, both high and low are inclusive byte indexes
struct SymExtract : public Symbolic {
  SymVal value;
  int high;
  int low;

  SymExtract(SymVal value, int high, int low);

  int size() override;
  ValueKind value_kind() override;
  int width() override;

private:
  friend std::tuple<int, bool>
  count_dag_size_aux(Symbolic &val, std::set<Symbolic *> &visited);

  std::optional<int> _cached_dag_size;
};

struct SymBinary : public Symbolic {
  BinOperation op;
  SymVal lhs;
  SymVal rhs;

  SymBinary(BinOperation op, SymVal lhs, SymVal rhs);

  int size() override;
  int width() override;
  ValueKind value_kind() override;

private:
  friend std::tuple<int, bool>
  count_dag_size_aux(Symbolic &val, std::set<Symbolic *> &visited);

  std::optional<int> _cached_dag_size;
  ValueKind _kind;
  int _width;
};

struct SymUnary : public Symbolic {
  UnaryOperation op;
  SymVal value;

  SymUnary(UnaryOperation op, SymVal value);

  int width() override;
  int size() override;
  ValueKind value_kind() override;

private:
  friend std::tuple<int, bool>
  count_dag_size_aux(Symbolic &val, std::set<Symbolic *> &visited);

  int _width;
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
  } else if (auto unary = dynamic_cast<SymUnary *>(&val)) {
    int size = 1;
    auto [value_size, value_sharing] =
        count_dag_size_aux(*unary->value.symptr, visited);
    size += value_size;
    if (!value_sharing) {
      unary->_cached_dag_size = size;
    }
    return {size, value_sharing};
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
  } else if (auto witness = dynamic_cast<Witness *>(&val)) {
    assert(false && "Witness should not appear during instruction execution");
  } else {
    assert(false && "Unknown symbolic type in dag size counting");
  }
}

inline int count_dag_size(Symbolic &val) {
  std::set<Symbolic *> visited;
  auto [size, _] = count_dag_size_aux(val, visited);
  return size;
}

#endif // WASM_SYMVAL_REPR_HPP
