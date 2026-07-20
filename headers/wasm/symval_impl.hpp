#ifndef WASM_SYMVAL_IMPL_HPP
#define WASM_SYMVAL_IMPL_HPP

#include "symval_decl.hpp"
#include "symval_factory.hpp"
#include "wasm/concrete_num.hpp"

inline SymVal SymVal::add(const SymVal &other) const {
  ManagedInterfaceTimer interface_timer("SymVal::add");
  return SVFactory::make_binary(ADD, *this, other);
}

inline SymVal SymVal::minus(const SymVal &other) const {
  ManagedInterfaceTimer interface_timer("SymVal::minus");
  return SVFactory::make_binary(SUB, *this, other);
}

inline SymVal SymVal::mul(const SymVal &other) const {
  ManagedInterfaceTimer interface_timer("SymVal::mul");
  return SVFactory::make_binary(MUL, *this, other);
}

inline SymVal SymVal::div(const SymVal &other) const {
  return SVFactory::make_binary(DIV, *this, other);
}

inline SymVal SymVal::div_u(const SymVal &other) const {
  ManagedInterfaceTimer interface_timer("SymVal::div_u");
  return SVFactory::make_binary(DIV_U, *this, other);
}

inline SymVal SymVal::land(const SymVal &other) const {
  return SVFactory::make_binary(AND, *this, other);
}

inline SymVal SymVal::lor(const SymVal &other) const {
  return SVFactory::make_binary(OR, *this, other);
}

inline SymVal SymVal::eq_bool(const SymVal &other) const {
  return SVFactory::make_binary(EQ_BOOL, *this, other);
}

inline SymVal SymVal::neq_bool(const SymVal &other) const {
  return SVFactory::make_binary(NEQ_BOOL, *this, other);
}

inline SymVal SymVal::eq(const SymVal &other) const {
  ManagedInterfaceTimer interface_timer("SymVal::eq");
  return SVFactory::make_binary(EQ_BOOL, *this, other);
}

inline SymVal SymVal::neq(const SymVal &other) const {
  ManagedInterfaceTimer interface_timer("SymVal::neq");
  return SVFactory::make_binary(NEQ_BOOL, *this, other);
}

inline SymVal SymVal::bv2bool() const {
  auto rhs = SVFactory::make_concrete_bv(I32V(0), symptr->width());
  return SVFactory::make_binary(NEQ_BOOL, *this, rhs);
}

inline SymVal SymVal::bool2bv() const {
  return SVFactory::make_unary(BOOL2BV, *this);
}

inline SymVal SymVal::extend_to_i64() const {
  return SVFactory::make_unary(EXTEND, *this);
}

inline SymVal SymVal::lt(const SymVal &other) const {
  return SVFactory::make_binary(LT_BOOL, *this, other);
}

inline SymVal SymVal::ltu(const SymVal &other) const {
  ManagedInterfaceTimer interface_timer("SymVal::ltu");
  return SVFactory::make_binary(LTU_BOOL, *this, other);
}

inline SymVal SymVal::le(const SymVal &other) const {
  ManagedInterfaceTimer interface_timer("SymVal::le");
  return SVFactory::make_binary(LEQ_BOOL, *this, other);
}

inline SymVal SymVal::leu(const SymVal &other) const {
  ManagedInterfaceTimer interface_timer("SymVal::leu");
  return SVFactory::make_binary(LEU_BOOL, *this, other);
}

inline SymVal SymVal::gt(const SymVal &other) const {
  return SVFactory::make_binary(GT_BOOL, *this, other);
}

inline SymVal SymVal::gtu(const SymVal &other) const {
  return SVFactory::make_binary(GTU_BOOL, *this, other);
}

inline SymVal SymVal::ge(const SymVal &other) const {
  ManagedInterfaceTimer interface_timer("SymVal::ge");
  return SVFactory::make_binary(GEQ_BOOL, *this, other);
}

inline SymVal SymVal::geu(const SymVal &other) const {
  ManagedInterfaceTimer interface_timer("SymVal::geu");
  return SVFactory::make_binary(GEU_BOOL, *this, other);
}

inline SymVal SymVal::shl(const SymVal &other) const {
  ManagedInterfaceTimer interface_timer("SymVal::shl");
  return SVFactory::make_binary(SHL, *this, other);
}

inline SymVal SymVal::shr_u(const SymVal &other) const {
  return SVFactory::make_binary(SHR_U, *this, other);
}

inline SymVal SymVal::shr_s(const SymVal &other) const {
  return SVFactory::make_binary(SHR_S, *this, other);
}

inline SymVal SymVal::rem_u(const SymVal &other) const {
  return SVFactory::make_binary(REM_U, *this, other);
}

inline SymVal SymVal::is_zero() const {
  ManagedInterfaceTimer interface_timer("SymVal::is_zero");
  return SVFactory::make_binary(
      EQ_BOOL, *this, SVFactory::make_concrete_bv(I64V(0), symptr->width()));
}

inline SymVal SymVal::bv_negate() const {
  assert(symptr->width() != 1);
  return SVFactory::make_binary(
      EQ_BOOL, *this, SVFactory::make_concrete_bv(I64V(0), symptr->width()));
}

inline SymVal SymVal::bool_not() const {
  return SVFactory::make_unary(NOT, *this);
}

inline SymVal SymVal::abs() const {
  return SVFactory::make_unary(ABS, *this);
}

inline SymVal SymVal::concat(const SymVal &other) const {
  return SVFactory::make_concat(*this, other);
}

inline SymVal SymVal::extract(int high, int low) const {
  return SVFactory::make_extract(*this, high, low);
}

inline SymVal SymVal::bitwise_and(const SymVal &other) const {
  ManagedInterfaceTimer interface_timer("SymVal::bitwise_and");
  return SVFactory::make_binary(B_AND, *this, other);
}

inline SymVal SymVal::bitwise_xor(const SymVal &other) const {
  return SVFactory::make_binary(B_XOR, *this, other);
}

inline SymVal SymVal::bitwise_or(const SymVal &other) const {
  return SVFactory::make_binary(B_OR, *this, other);
}

inline SymVal SymVal::get_witness_symbol() {
  static SymVal witness = SymVal(SVFactory::SymBookKeeper.allocate<Witness>());
  return witness;
}

inline SymVal SymVal::makeI32Symbol() const {
  if (auto concrete = dynamic_cast<SymConcrete *>(symptr.get())) {
    auto id = concrete->value.toInt();
    return SVFactory::make_int_symbolic(id, 32);
  }
  throw std::runtime_error(
      "Cannot make symbolic a non-concrete symbolic value");
}

inline SymVal SymVal::makeI64Symbol() const {
  if (auto concrete = dynamic_cast<SymConcrete *>(symptr.get())) {
    auto id = concrete->value.toInt();
    return SVFactory::make_int_symbolic(id, 64);
  }
  throw std::runtime_error(
      "Cannot make symbolic a non-concrete symbolic value");
}

inline SymVal SymVal::makeF32Symbol() const {
  if (auto concrete = dynamic_cast<SymConcrete *>(symptr.get())) {
    auto id = concrete->value.toInt();
    return SVFactory::make_fp_symbolic(id, 32);
  }
  throw std::runtime_error(
      "Cannot make symbolic a non-concrete symbolic value");
}

inline SymVal SymVal::makeF64Symbol() const {
  auto concrete = dynamic_cast<SymConcrete *>(symptr.get());
  if (concrete) {
    auto id = concrete->value.toInt();
    return SVFactory::make_fp_symbolic(id, 64);
  }
  throw std::runtime_error(
      "Cannot make symbolic a non-concrete symbolic value");
}

inline bool SymVal::is_concrete() const {
  ManagedInterfaceTimer interface_timer("SymVal::is_concrete");
  return dynamic_cast<SymConcrete *>(symptr.get()) != nullptr;
}

inline SymVal Concrete(Num num, int width) {
  ManagedInterfaceTimer interface_timer("Concrete");
  // std::cout << "Creating concrete value: " << num.toInt() << " with width "
  // << width
  //           << std::endl;
  assert(width == 32 || width == 64);
  return SVFactory::make_concrete_bv(num, width);
}

inline SymVal FPConcrete(Num num, int width) {
  assert(width == 32 || width == 64);
  return SVFactory::make_concrete_fp(num, width);
}


#endif // WASM_SYMVAL_IMPL_HPP
