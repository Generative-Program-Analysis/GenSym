#ifndef WASM_SYMVAL_REPR_HPP
#define WASM_SYMVAL_REPR_HPP

#include "symval_decl.hpp"
#include <optional>
#include <set>

enum BinOperation {
  ADD,     // Addition
  SUB,     // Subtraction
  MUL,     // Multiplication
  DIV,     // Division
  DIV_U,   // Unsigned division
  AND,     // Logical AND
  OR,      // Logical OR
  EQ_BOOL, // Equal (return a boolean) TODO: remove bv version of comparison ops
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
  Symbol(int id, int width, ValueKind kind)
      : id(id), _width(width), _kind(kind) {}
  int get_id() const { return id; }

  int size() override { return 1; }

  ValueKind value_kind() override { return _kind; }
  int width() override { return _width; }

private:
  int id;
  int _width;
  ValueKind _kind;
};

class Witness : public Symbolic {
public:
  int size() override { return 1; }

  ValueKind value_kind() override { return KindBV; }

  int width() override { return 32; }
};

class SymConcrete : public Symbolic {
public:
  Num value;
  ValueKind kind;
  SymConcrete(Num num, ValueKind kind, int width)
      : value(num), kind(kind), _width(width) {}

  int size() override { return 1; }

  ValueKind value_kind() override { return kind; }
  int width() override { return _width; }

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

  SymExtract(SymVal value, int high, int low)
      : value(value), high(high), low(low) {}

  int size() override {
    if (_cached_dag_size.has_value()) {
      return _cached_dag_size.value();
    }
    _cached_dag_size = 1 + value->size();
    return _cached_dag_size.value();
  }

  ValueKind value_kind() override { return KindBV; }

  int width() override { return (high - low + 1) * 8; }

private:
  friend std::tuple<int, bool>
  count_dag_size_aux(Symbolic &val, std::set<Symbolic *> &visited);

  std::optional<int> _cached_dag_size;
};

struct SymBinary : public Symbolic {
  BinOperation op;
  SymVal lhs;
  SymVal rhs;

  SymBinary(BinOperation op, SymVal lhs, SymVal rhs)
      : op(op), lhs(lhs), rhs(rhs) {
    auto lhs_kind = lhs->value_kind();
    auto rhs_kind = rhs->value_kind();
    auto lhs_width = lhs->width();
    auto rhs_width = rhs->width();

    switch (op) {
    case ADD:
    case SUB:
    case MUL:
    case DIV:
    case DIV_U:
    case SHL:
    case SHR_U:
    case SHR_S:
    case REM_U:
    case B_AND:
    case B_XOR:
    case B_OR:
      assert(lhs_kind == KindBV && rhs_kind == KindBV);
      assert(lhs_width == rhs_width);
      _kind = KindBV;
      _width = lhs_width;
      break;
    case CONCAT:
      assert(lhs_kind == KindBV && rhs_kind == KindBV);
      _kind = KindBV;
      _width = lhs_width + rhs_width;
      break;
    case EQ_BOOL:
    case NEQ_BOOL:
    case LT_BOOL:
    case LTU_BOOL:
    case LEQ_BOOL:
    case LEU_BOOL:
    case GT_BOOL:
    case GTU_BOOL:
    case GEQ_BOOL:
    case GEU_BOOL:
      assert(lhs_kind == rhs_kind);
      if (lhs_kind == KindBV) {
        assert(lhs_width == rhs_width);
      }
      _kind = KindBool;
      _width = 1;
      break;
    case AND:
    case OR:
      assert(lhs_kind == KindBool && rhs_kind == KindBool);
      assert(lhs_width == 1 && rhs_width == 1);
      _kind = KindBool;
      _width = 1;
      break;
    default:
      assert(false && "Unhandled binary operation");
    }
  }

  int size() override {
    if (_cached_dag_size.has_value()) {
      return _cached_dag_size.value();
    }

    auto size = count_dag_size(*this);
    _cached_dag_size = size;
    return size;
  }

  int width() override { return _width; }

  ValueKind value_kind() override { return _kind; }

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

  SymUnary(UnaryOperation op, SymVal value) : op(op), value(value) {
    switch (op) {
    case BOOL2BV:
      assert(value->value_kind() == KindBool);
      _width = 32; // Only 32 bit bit vector can be converted to boolean and
                   // vice versa.
      break;
    case NOT:
      _width = 1;
      break;
    default:
      assert(false && "Unknown unary operation");
    }
  }

  int width() override { return _width; }

  int size() override {
    if (_cached_dag_size.has_value()) {
      return _cached_dag_size.value();
    }
    _cached_dag_size = 1 + value->size();
    return _cached_dag_size.value();
  }

  ValueKind value_kind() override {
    switch (op) {
    case NOT: {
      return ValueKind::KindBool;
    }
    case BOOL2BV: {
      return ValueKind::KindBV;
    }
    default: {
      assert(false && "Unknown unary operation");
    }
    }
  }

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
    throw std::runtime_error("Unknown symbolic type in dag size counting");
  }
}

inline int count_dag_size(Symbolic &val) {
  std::set<Symbolic *> visited;
  auto [size, _] = count_dag_size_aux(val, visited);
  return size;
}

#endif // WASM_SYMVAL_REPR_HPP
