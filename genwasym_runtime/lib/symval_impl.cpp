#include "wasm/symval_impl.hpp"

#include <cassert>
#include <stdexcept>

SymVal SymVal::add(const SymVal &other) const {
  return SVFactory::make_binary(ADD, *this, other);
}

SymVal SymVal::minus(const SymVal &other) const {
  return SVFactory::make_binary(SUB, *this, other);
}

SymVal SymVal::mul(const SymVal &other) const {
  return SVFactory::make_binary(MUL, *this, other);
}

SymVal SymVal::div(const SymVal &other) const {
  return SVFactory::make_binary(DIV, *this, other);
}

SymVal SymVal::div_u(const SymVal &other) const {
  return SVFactory::make_binary(DIV_U, *this, other);
}

SymVal SymVal::land(const SymVal &other) const {
  return SVFactory::make_binary(AND, *this, other);
}

SymVal SymVal::lor(const SymVal &other) const {
  return SVFactory::make_binary(OR, *this, other);
}

SymVal SymVal::eq_bool(const SymVal &other) const {
  return SVFactory::make_binary(EQ_BOOL, *this, other);
}

SymVal SymVal::neq_bool(const SymVal &other) const {
  return SVFactory::make_binary(NEQ_BOOL, *this, other);
}

SymVal SymVal::eq(const SymVal &other) const {
  return SVFactory::make_binary(EQ_BOOL, *this, other);
}

SymVal SymVal::neq(const SymVal &other) const {
  return SVFactory::make_binary(NEQ_BOOL, *this, other);
}

SymVal SymVal::bv2bool() const {
  auto rhs = SVFactory::make_concrete_bv(I32V(0), symptr->width());
  return SVFactory::make_binary(NEQ_BOOL, *this, rhs);
}

SymVal SymVal::bool2bv() const {
  return SVFactory::make_unary(BOOL2BV, *this);
}

SymVal SymVal::extend_to_i64() const {
  return SVFactory::make_unary(EXTEND, *this);
}

SymVal SymVal::lt(const SymVal &other) const {
  return SVFactory::make_binary(LT_BOOL, *this, other);
}

SymVal SymVal::ltu(const SymVal &other) const {
  return SVFactory::make_binary(LTU_BOOL, *this, other);
}

SymVal SymVal::le(const SymVal &other) const {
  return SVFactory::make_binary(LEQ_BOOL, *this, other);
}

SymVal SymVal::leu(const SymVal &other) const {
  return SVFactory::make_binary(LEU_BOOL, *this, other);
}

SymVal SymVal::gt(const SymVal &other) const {
  return SVFactory::make_binary(GT_BOOL, *this, other);
}

SymVal SymVal::gtu(const SymVal &other) const {
  return SVFactory::make_binary(GTU_BOOL, *this, other);
}

SymVal SymVal::ge(const SymVal &other) const {
  return SVFactory::make_binary(GEQ_BOOL, *this, other);
}

SymVal SymVal::geu(const SymVal &other) const {
  return SVFactory::make_binary(GEU_BOOL, *this, other);
}

SymVal SymVal::shl(const SymVal &other) const {
  return SVFactory::make_binary(SHL, *this, other);
}

SymVal SymVal::shr_u(const SymVal &other) const {
  return SVFactory::make_binary(SHR_U, *this, other);
}

SymVal SymVal::shr_s(const SymVal &other) const {
  return SVFactory::make_binary(SHR_S, *this, other);
}

SymVal SymVal::rem_u(const SymVal &other) const {
  return SVFactory::make_binary(REM_U, *this, other);
}

SymVal SymVal::is_zero() const {
  return SVFactory::make_binary(
      EQ_BOOL, *this, SVFactory::make_concrete_bv(I64V(0), symptr->width()));
}

SymVal SymVal::bv_negate() const {
  assert(symptr->width() != 1);
  return SVFactory::make_binary(
      EQ_BOOL, *this, SVFactory::make_concrete_bv(I64V(0), symptr->width()));
}

SymVal SymVal::bool_not() const {
  return SVFactory::make_unary(NOT, *this);
}

SymVal SymVal::concat(const SymVal &other) const {
  return SVFactory::make_concat(*this, other);
}

SymVal SymVal::extract(int high, int low) const {
  return SVFactory::make_extract(*this, high, low);
}

SymVal SymVal::bitwise_and(const SymVal &other) const {
  return SVFactory::make_binary(B_AND, *this, other);
}

SymVal SymVal::bitwise_xor(const SymVal &other) const {
  return SVFactory::make_binary(B_XOR, *this, other);
}

SymVal SymVal::bitwise_or(const SymVal &other) const {
  return SVFactory::make_binary(B_OR, *this, other);
}

SymVal SymVal::get_witness_symbol() {
  static SymVal witness = SymVal(SVFactory::SymBookKeeper.allocate<Witness>());
  return witness;
}

SymVal SymVal::makeI32Symbol() const {
  if (auto concrete = dynamic_cast<SymConcrete *>(symptr.get())) {
    auto id = concrete->value.toInt();
    return SVFactory::make_int_symbolic(id, 32);
  }

  throw std::runtime_error(
      "Cannot make symbolic a non-concrete symbolic value");
}

SymVal SymVal::makeI64Symbol() const {
  if (auto concrete = dynamic_cast<SymConcrete *>(symptr.get())) {
    auto id = concrete->value.toInt();
    return SVFactory::make_int_symbolic(id, 64);
  }

  throw std::runtime_error(
      "Cannot make symbolic a non-concrete symbolic value");
}

SymVal SymVal::makeF32Symbol() const {
  if (auto concrete = dynamic_cast<SymConcrete *>(symptr.get())) {
    auto id = concrete->value.toInt();
    return SVFactory::make_fp_symbolic(id, 32);
  }

  throw std::runtime_error(
      "Cannot make symbolic a non-concrete symbolic value");
}

SymVal SymVal::makeF64Symbol() const {
  auto concrete = dynamic_cast<SymConcrete *>(symptr.get());
  if (concrete) {
    auto id = concrete->value.toInt();
    return SVFactory::make_fp_symbolic(id, 64);
  }

  throw std::runtime_error(
      "Cannot make symbolic a non-concrete symbolic value");
}

bool SymVal::is_concrete() const {
  return dynamic_cast<SymConcrete *>(symptr.get()) != nullptr;
}

SymVal Concrete(Num num, int width) {
  // std::cout << "Creating concrete value: " << num.toInt() << " with width "
  // << width
  //           << std::endl;
  assert(width == 32 || width == 64);
  return SVFactory::make_concrete_bv(num, width);
}

SymVal FPConcrete(Num num, int width) {
  assert(width == 32 || width == 64);
  return SVFactory::make_concrete_fp(num, width);
}