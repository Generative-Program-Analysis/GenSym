#include "wasm/symbolic_decl.hpp"

#include <cassert>

Symbol::Symbol(int id, int width, ValueKind kind)
    : id(id), _width(width), _kind(kind) {}

int Symbol::get_id() const {
  return id;
}

int Symbol::size() {
  return 1;
}

ValueKind Symbol::value_kind() {
  return _kind;
}

int Symbol::width() {
  return _width;
}

int Witness::size() {
  return 1;
}

ValueKind Witness::value_kind() {
  return KindBV;
}

int Witness::width() {
  return 32;
}

SymConcrete::SymConcrete(Num num, ValueKind kind, int width)
    : value(num), kind(kind), _width(width) {}

int SymConcrete::size() {
  return 1;
}

ValueKind SymConcrete::value_kind() {
  return kind;
}

int SymConcrete::width() {
  return _width;
}

SymExtract::SymExtract(SymVal value, int high, int low)
    : value(value), high(high), low(low) {}

int SymExtract::size() {
  if (_cached_dag_size.has_value()) {
    return _cached_dag_size.value();
  }
  _cached_dag_size = 1 + value->size();
  return _cached_dag_size.value();
}

ValueKind SymExtract::value_kind() {
  return KindBV;
}

int SymExtract::width() {
  return (high - low + 1) * 8;
}

SymBinary::SymBinary(BinOperation op, SymVal lhs, SymVal rhs)
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

int SymBinary::size() {
  if (_cached_dag_size.has_value()) {
    return _cached_dag_size.value();
  }

  auto size = count_dag_size(*this);
  _cached_dag_size = size;
  return size;
}

int SymBinary::width() {
  return _width;
}

ValueKind SymBinary::value_kind() {
  return _kind;
}

SymUnary::SymUnary(UnaryOperation op, SymVal value) : op(op), value(value) {
  switch (op) {
  case BOOL2BV:
    assert(value->value_kind() == KindBool);
    _width = 32;
    break;
  case NOT:
    _width = 1;
    break;
  default:
    assert(false && "Unknown unary operation");
  }
}

int SymUnary::width() {
  return _width;
}

int SymUnary::size() {
  if (_cached_dag_size.has_value()) {
    return _cached_dag_size.value();
  }
  _cached_dag_size = 1 + value->size();
  return _cached_dag_size.value();
}

ValueKind SymUnary::value_kind() {
  switch (op) {
  case NOT:
    return ValueKind::KindBool;
  case BOOL2BV:
    return ValueKind::KindBV;
  default:
    assert(false && "Unknown unary operation");
  }
}