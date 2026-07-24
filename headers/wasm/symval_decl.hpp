#ifndef WASM_SYMVAL_HPP
#define WASM_SYMVAL_HPP
#include "concrete_num.hpp"
#include <memory>
#include <unordered_map>
#include <unordered_set>

class Symbolic;

struct SymVal {
  std::shared_ptr<Symbolic> symptr;

  SymVal() = delete;
  SymVal(std::shared_ptr<Symbolic> symptr) : symptr(symptr) {}

  // Create a new i32 symbol value
  SymVal makeI32Symbol() const;
  // Create a new i64 symbol value
  SymVal makeI64Symbol() const;
  // Create a new f32 symbol value
  SymVal makeF32Symbol() const;
  // Create a new f64 symbol value
  SymVal makeF64Symbol() const;

  // bitvector arithmetic operations
  SymVal is_zero() const;
  SymVal add(const SymVal &other) const;
  SymVal minus(const SymVal &other) const;
  SymVal mul(const SymVal &other) const;
  SymVal div(const SymVal &other) const;
  SymVal div_u(const SymVal &other) const;
  SymVal eq_bool(const SymVal &other) const;
  SymVal neq_bool(const SymVal &other) const;
  SymVal land(const SymVal &other) const;
  SymVal lor(const SymVal &other) const;
  SymVal eq(const SymVal &other) const;
  SymVal neq(const SymVal &other) const;
  SymVal lt(const SymVal &other) const;
  SymVal ltu(const SymVal &other) const;
  SymVal le(const SymVal &other) const;
  SymVal leu(const SymVal &other) const;
  SymVal gt(const SymVal &other) const;
  SymVal gtu(const SymVal &other) const;
  SymVal ge(const SymVal &other) const;
  SymVal geu(const SymVal &other) const;
  SymVal shl(const SymVal &other) const;
  SymVal shr_u(const SymVal &other) const;
  SymVal shr_s(const SymVal &other) const;
  SymVal bv_negate() const;
  SymVal bool_not() const;
  SymVal bitwise_and(const SymVal &other) const;
  SymVal bitwise_xor(const SymVal &other) const;
  SymVal bitwise_or(const SymVal &other) const;
  SymVal abs() const;
  SymVal concat(const SymVal &other) const;
  SymVal extract(int high, int low) const;
  SymVal bv2bool() const;
  SymVal bool2bv() const;
  SymVal rem_u(const SymVal &other) const;
  SymVal extend_to_i64() const; // only for i32 symbolic values, extend to i64 by sign extension


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
  ManagedInterfaceTimer interface_timer("allConcrete");
  static_assert((std::is_same_v<Args, SymVal> && ...),
                "all_concrete only accepts SymVal arguments");
  return (... && args.is_concrete());
}

inline Num isSymbolic(int index);

inline SymVal Concrete(Num num, int width);

[[noreturn]] inline SymVal debug_unreachable(const char* msg) {
    std::cerr << "unreachable: " << msg << '\n';
    assert(false && "unreachable reached");
    std::abort();
}

#endif // WASM_SYMVAL_HPP
