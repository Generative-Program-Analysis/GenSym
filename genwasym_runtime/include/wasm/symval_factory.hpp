#ifndef WASM_SYMVAL_FACTORY_HPP
#define WASM_SYMVAL_FACTORY_HPP

#include "heap_mem_bookkeeper.hpp"
#include "symbolic_decl.hpp"
#include "symval_decl.hpp"

#include <unordered_map>

namespace SVFactory {

SymVal make_concrete_bv(Num num, int width);
SymVal make_concrete_fp(Num num, int width);
SymVal make_concrete_bool(bool b);
SymVal make_int_symbolic(int index, int width);
SymVal make_fp_symbolic(int index, int width);
SymVal make_smallbv(int width, int64_t value);
SymVal make_binary(BinOperation op, const SymVal &lhs, const SymVal &rhs);
SymVal make_unary(UnaryOperation op, const SymVal &value);
SymVal make_concat(const SymVal &lhs, const SymVal &rhs);
SymVal make_extract(const SymVal &value, int high, int low);

// Core allocator and common constants.
extern MemBookKeeper<Symbolic> SymBookKeeper;

extern SymVal I32ZERO;
extern SymVal I64ZERO;
extern SymVal TRUE;
extern SymVal FALSE;
extern SymVal ZeroByte;

// Key and hash types.
struct SmallBVKey {
  int width;
  int64_t value;

  SmallBVKey(int width, int64_t value);
  bool operator==(const SmallBVKey &other) const;
};

struct SmallBVKeyHash {
  size_t operator()(const SmallBVKey &key) const;
};

struct ExtractKey {
  SymVal value;
  int high;
  int low;

  ExtractKey(const SymVal &value, int high, int low);
  bool operator==(const ExtractKey &other) const;
};

struct ExtractKeyHash {
  size_t operator()(const ExtractKey &key) const;
};

struct BinOpKey {
  BinOperation op;
  SymVal lhs;
  SymVal rhs;

  BinOpKey(BinOperation op, const SymVal &lhs, const SymVal &rhs);
  bool operator==(const BinOpKey &other) const;
};

struct BinOpKeyHash {
  size_t operator()(const BinOpKey &key) const;
};

struct UnaryOpKey {
  UnaryOperation op;
  SymVal value;

  UnaryOpKey(UnaryOperation op, const SymVal &value);
  bool operator==(const UnaryOpKey &other) const;
};

struct UnaryOpKeyHash {
  size_t operator()(const UnaryOpKey &key) const;
};

// Caches.
extern std::unordered_map<int, SymVal> SymbolStore;
extern std::unordered_map<int64_t, SymVal> FPStore;
extern std::unordered_map<SmallBVKey, SymVal, SmallBVKeyHash> SmallBVStore;
extern std::unordered_map<ExtractKey, SymVal, ExtractKeyHash>
    ExtractOperationStore;
extern std::unordered_map<BinOpKey, SymVal, BinOpKeyHash> BinaryOperationStore;
extern std::unordered_map<UnaryOpKey, SymVal, UnaryOpKeyHash>
    UnaryOperationStore;

} // namespace SVFactory

#endif // WASM_SYMVAL_FACTORY_HPP