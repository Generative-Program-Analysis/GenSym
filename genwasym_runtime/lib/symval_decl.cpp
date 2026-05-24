#include "wasm/symval_decl.hpp"

#include <cassert>
#include <cstdlib>
#include <iostream>

SymVal::SymVal(std::shared_ptr<Symbolic> symptr) : symptr(symptr) {}

Symbolic *SymVal::operator->() const {
  return symptr.get();
}

bool SymVal::operator==(const SymVal &other) const {
  return symptr == other.symptr;
}

size_t SymValHash::operator()(const SymVal &key) const {
  return std::hash<void *>{}(key.symptr.get());
}

[[noreturn]] SymVal debug_unreachable(const char *msg) {
  std::cerr << "unreachable: " << msg << '\n';
  assert(false && "unreachable reached");
  std::abort();
}