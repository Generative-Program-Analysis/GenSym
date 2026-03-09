#ifndef WASM_SYMVAL_FACTORY_HPP
#define WASM_SYMVAL_FACTORY_HPP

#include "heap_mem_bookkeeper.hpp"
#include "symval_decl.hpp"
#include "symbolic_decl.hpp"

namespace SVFactory {

SymVal make_concrete_bv(Num num, int width);
SymVal make_concrete_bool(bool b);
SymVal make_int_symbolic(int index, int width);
SymVal make_smallbv(int width, int64_t value);
SymVal make_binary(BinOperation op, const SymVal &lhs, const SymVal &rhs);
SymVal make_unary(UnaryOperation op, const SymVal &value);
SymVal make_extract(const SymVal &value, int high, int low);

// Core allocator and common constants.
static MemBookKeeper<Symbolic> SymBookKeeper;

static SymVal I32ZERO =
    SymVal(SymBookKeeper.allocate<SymConcrete>(I32V(0), KindBV, 32));

static SymVal I64ZERO =
    SymVal(SymBookKeeper.allocate<SymConcrete>(I64V(0), KindBV, 64));

static SymVal TRUE =
    SymVal(SymBookKeeper.allocate<SymConcrete>(I32V(1), KindBool, 32));

static SymVal FALSE =
    SymVal(SymBookKeeper.allocate<SymConcrete>(I32V(0), KindBool, 32));

static SymVal ZeroByte =
    SymVal(SymBookKeeper.allocate<SymConcrete>(I64V(0), KindBV, 8));

// Key and hash types.
struct SmallBVKey {
  int width;
  int64_t value;
  SmallBVKey(int width, int64_t value) : width(width), value(value) {}

  bool operator==(const SmallBVKey &other) const {
    return width == other.width && value == other.value;
  }
};

struct SmallBVKeyHash {
  size_t operator()(const SmallBVKey &key) const {
    size_t h1 = std::hash<int>{}(key.width);
    size_t h2 = std::hash<int64_t>{}(key.value);
    return h1 ^ (h2 << 1);
  }
};

struct ExtractKey {
  SymVal value;
  int high;
  int low;
  ExtractKey(const SymVal &value, int high, int low)
      : value(value), high(high), low(low) {}

  bool operator==(const ExtractKey &other) const {
    return value.symptr == other.value.symptr && high == other.high &&
           low == other.low;
  }
};

struct ExtractKeyHash {
  size_t operator()(const ExtractKey &key) const {
    size_t h1 = std::hash<void *>{}(key.value.symptr.get());
    size_t h2 = std::hash<int>{}(key.high);
    size_t h3 = std::hash<int>{}(key.low);
    return h1 ^ (h2 << 1) ^ (h3 << 2);
  }
};

struct BinOpKey {
  BinOperation op;
  SymVal lhs;
  SymVal rhs;
  BinOpKey(BinOperation op, const SymVal &lhs, const SymVal &rhs)
      : op(op), lhs(lhs), rhs(rhs) {}

  bool operator==(const BinOpKey &other) const {
    return op == other.op && lhs.symptr == other.lhs.symptr &&
           rhs.symptr == other.rhs.symptr;
  }
};

struct BinOpKeyHash {
  size_t operator()(const BinOpKey &key) const {
    size_t h1 = std::hash<int>{}(static_cast<int>(key.op));
    size_t h2 = std::hash<void *>{}(key.lhs.symptr.get());
    size_t h3 = std::hash<void *>{}(key.rhs.symptr.get());
    return h1 ^ (h2 << 1) ^ (h3 << 2);
  }
};

struct UnaryOpKey {
  UnaryOperation op;
  SymVal value;
  UnaryOpKey(UnaryOperation op, const SymVal &value) : op(op), value(value) {}

  bool operator==(const UnaryOpKey &other) const {
    return op == other.op && value.symptr == other.value.symptr;
  }
};

struct UnaryOpKeyHash {
  size_t operator()(const UnaryOpKey &key) const {
    size_t h1 = std::hash<int>{}(static_cast<int>(key.op));
    size_t h2 = std::hash<void *>{}(key.value.symptr.get());
    return h1 ^ (h2 << 1);
  }
};

// Caches.
static std::unordered_map<int, SymVal> SymbolStore;
static std::unordered_map<int64_t, SymVal> concrete_pool;
static std::unordered_map<SmallBVKey, SymVal, SmallBVKeyHash> SmallBVStore;
static std::unordered_map<ExtractKey, SymVal, ExtractKeyHash>
    ExtractOperationStore;
static std::unordered_map<BinOpKey, SymVal, BinOpKeyHash> BinaryOperationStore;
static std::unordered_map<UnaryOpKey, SymVal, UnaryOpKeyHash>
    UnaryOperationStore;

// Factory implementations.
inline SymVal make_concrete_bv(Num num, int width) {
  auto it = concrete_pool.find(num.toInt());
  if (it != concrete_pool.end()) {
    return it->second;
  }

  auto new_val =
      SymVal(SymBookKeeper.allocate<SymConcrete>(num, KindBV, width));
  concrete_pool.insert({num.toInt(), new_val});
  return new_val;
}

inline SymVal make_concrete_fp(Num num, int width) {
  auto it = concrete_pool.find(num.toInt());
  if (it != concrete_pool.end()) {
    return it->second;
  }

  auto new_val =
      SymVal(SymBookKeeper.allocate<SymConcrete>(num, KindFP, width));
  concrete_pool.insert({num.toInt(), new_val});
  return new_val;
}

inline SymVal make_concrete_bool(bool b) {
  if (b) {
    return TRUE;
  } else {
    return FALSE;
  }
}

inline SymVal make_int_symbolic(int index, int width) {
  auto it = SymbolStore.find(index);
  if (it != SymbolStore.end()) {
    return it->second;
  }
  SymVal new_symbol =
      SymVal(SymBookKeeper.allocate<Symbol>(index, width, KindBV));
  SymbolStore.insert({index, new_symbol});
  return new_symbol;
}

inline SymVal make_fp_symbolic(int index, int width) {
  auto it = SymbolStore.find(index);
  if (it != SymbolStore.end()) {
    return it->second;
  }
  SymVal new_symbol =
      SymVal(SymBookKeeper.allocate<Symbol>(index, width, KindFP));
  SymbolStore.insert({index, new_symbol});
  return new_symbol;
}

inline SymVal make_smallbv(int width, int64_t value) {
  if (width == 32) {
    return make_concrete_bv(I32V(value), width);
  }
  if (width == 64) {
    return make_concrete_bv(I64V(value), width);
  }
  auto key = SmallBVKey(width, value);
  auto it = SmallBVStore.find(key);
  if (it != SmallBVStore.end()) {
    return it->second;
  }
  auto new_val =
      SymVal(SymBookKeeper.allocate<SymConcrete>(I64V(value), KindBV, width));
  SmallBVStore.insert({key, new_val});
  return new_val;
}

inline SymVal make_extract(const SymVal &value, int high, int low) {
  assert(value.symptr != nullptr && "Symbolic expression is null in extract");
  assert(high >= low && "Invalid extract range");
  int new_width = (high - low + 1) * 8;
  int shift_bits = (low - 1) * 8;

  if (auto concrete = std::dynamic_pointer_cast<SymConcrete>(value.symptr)) {
    if (concrete->kind != KindBV) {
      throw std::runtime_error("Extract only supports bitvector concrete values");
    }
    // extract from concrete bitvector value
    int64_t val = concrete->value.value;
    int64_t mask = (1LL << ((high - low + 1) * 8)) - 1;
    int64_t new_value = (val >> shift_bits) & mask;
    return SVFactory::make_smallbv(new_width, new_value);
  }

  // If the value is already an extract, we can merge the two extracts into one
  if (auto extract = std::dynamic_pointer_cast<SymExtract>(value.symptr)) {
    if (extract->low == low && extract->high == high) {
      // extracting the same range, return directly
      return value;
    }
  }

  // Otherwise, create a new extract symbolic value
  ExtractKey key(value, high, low);
  auto it = ExtractOperationStore.find(key);
  if (it != ExtractOperationStore.end()) {
    return it->second;
  }
  auto result = SymVal(SymBookKeeper.allocate<SymExtract>(value, high, low));
  ExtractOperationStore.insert({key, result});
  return result;
}

inline SymVal make_binary(BinOperation op, const SymVal &lhs,
                          const SymVal &rhs) {
  assert(lhs.symptr != nullptr && rhs.symptr != nullptr);

  BinOpKey key(op, lhs, rhs);
  auto it = BinaryOperationStore.find(key);
  if (it != BinaryOperationStore.end()) {
    return it->second;
  }

  if (auto lhs_concrete = dynamic_cast<SymConcrete *>(lhs.symptr.get())) {
    if (auto rhs_concrete = dynamic_cast<SymConcrete *>(rhs.symptr.get())) {
      auto lhs_value = lhs_concrete->value;
      auto rhs_value = rhs_concrete->value;
      auto lhs_width = lhs_concrete->width();
      auto rhs_width = rhs_concrete->width();

      auto make_eval_bv = [&](Num num, int width) {
        auto result = SVFactory::make_concrete_bv(num, width);
        BinaryOperationStore.insert({key, result});
        return result;
      };
      auto make_eval_bool = [&](Num num) {
        auto result = SVFactory::make_concrete_bool(num.value);
        BinaryOperationStore.insert({key, result});
        return result;
      };

      switch (op) {
      case ADD:
        if (lhs_width == 32 && rhs_width == 32)
          return make_eval_bv(lhs_value.i32_add(rhs_value), 32);
        if (lhs_width == 64 && rhs_width == 64)
          return make_eval_bv(lhs_value.i64_add(rhs_value), 64);
        break;
      case SUB:
        if (lhs_width == 32 && rhs_width == 32)
          return make_eval_bv(lhs_value.i32_sub(rhs_value), 32);
        if (lhs_width == 64 && rhs_width == 64)
          return make_eval_bv(lhs_value.i64_sub(rhs_value), 64);
        break;
      case MUL:
        if (lhs_width == 32 && rhs_width == 32)
          return make_eval_bv(lhs_value.i32_mul(rhs_value), 32);
        if (lhs_width == 64 && rhs_width == 64)
          return make_eval_bv(lhs_value.i64_mul(rhs_value), 64);
        break;
      case DIV:
        if (lhs_width == 32 && rhs_width == 32)
          return make_eval_bv(lhs_value.i32_div_s(rhs_value), 32);
        if (lhs_width == 64 && rhs_width == 64)
          return make_eval_bv(lhs_value.i64_div_s(rhs_value), 64);
        break;
      case LT_BOOL:
        if (lhs_width == 32 && rhs_width == 32)
          return make_eval_bool(lhs_value.i32_lt_s(rhs_value));
        if (lhs_width == 64 && rhs_width == 64)
          return make_eval_bool(lhs_value.i64_lt_s(rhs_value));
        break;
      case LEQ_BOOL:
        if (lhs_width == 32 && rhs_width == 32)
          return make_eval_bool(lhs_value.i32_le_s(rhs_value));
        if (lhs_width == 64 && rhs_width == 64)
          return make_eval_bool(lhs_value.i64_le_s(rhs_value));
        break;
      case GT_BOOL:
        if (lhs_width == 32 && rhs_width == 32)
          return make_eval_bool(lhs_value.i32_gt_s(rhs_value));
        if (lhs_width == 64 && rhs_width == 64)
          return make_eval_bool(lhs_value.i64_gt_s(rhs_value));
        break;
      case GEQ_BOOL:
        if (lhs_width == 32 && rhs_width == 32)
          return make_eval_bool(lhs_value.i32_ge_s(rhs_value));
        if (lhs_width == 64 && rhs_width == 64)
          return make_eval_bool(lhs_value.i64_ge_s(rhs_value));
        break;
      case NEQ_BOOL:
        if (lhs_width == 32 && rhs_width == 32)
          return make_eval_bool(lhs_value.i32_ne(rhs_value));
        if (lhs_width == 64 && rhs_width == 64)
          return make_eval_bool(lhs_value.i64_ne(rhs_value));
        break;
      case EQ_BOOL:
        if (lhs_width == 32 && rhs_width == 32)
          return make_eval_bool(lhs_value.i32_eq(rhs_value));
        if (lhs_width == 64 && rhs_width == 64)
          return make_eval_bool(lhs_value.i64_eq(rhs_value));
        break;
      case B_AND:
        if (lhs_width == 32 && rhs_width == 32)
          return make_eval_bv(lhs_value.i32_and(rhs_value), 32);
        if (lhs_width == 64 && rhs_width == 64)
          return make_eval_bv(lhs_value.i64_and(rhs_value), 64);
        break;
      case CONCAT: {
        auto conc_value = (lhs_value.value << rhs_width) | rhs_value.value;
        auto new_width = lhs_width + rhs_width;
        return make_eval_bv(Num(I64V(conc_value)), new_width);
      }
      case B_XOR:
        if (lhs_width == 32 && rhs_width == 32)
          return make_eval_bv(lhs_value.i32_xor(rhs_value), 32);
        if (lhs_width == 64 && rhs_width == 64)
          return make_eval_bv(lhs_value.i64_xor(rhs_value), 64);
        break;
      case B_OR:
        if (lhs_width == 32 && rhs_width == 32)
          return make_eval_bv(lhs_value.i32_or(rhs_value), 32);
        if (lhs_width == 64 && rhs_width == 64)
          return make_eval_bv(lhs_value.i64_or(rhs_value), 64);
        break;
      case SHR_U:
        if (lhs_width == 32 && rhs_width == 32)
          return make_eval_bv(lhs_value.i32_shr_u(rhs_value), 32);
        if (lhs_width == 64 && rhs_width == 64)
          return make_eval_bv(lhs_value.i64_shr_u(rhs_value), 64);
        break;
      case SHR_S:
        if (lhs_width == 32 && rhs_width == 32)
          return make_eval_bv(lhs_value.i32_shr_s(rhs_value), 32);
        if (lhs_width == 64 && rhs_width == 64)
          return make_eval_bv(lhs_value.i64_shr_s(rhs_value), 64);
        break;
      case LTU_BOOL:
        if (lhs_width == 32 && rhs_width == 32)
          return make_eval_bool(lhs_value.i32_lt_u(rhs_value));
        if (lhs_width == 64 && rhs_width == 64)
          return make_eval_bool(lhs_value.i64_lt_u(rhs_value));
        break;
      case GTU_BOOL:
        if (lhs_width == 32 && rhs_width == 32)
          return make_eval_bool(lhs_value.i32_gt_u(rhs_value));
        if (lhs_width == 64 && rhs_width == 64)
          return make_eval_bool(lhs_value.i64_gt_u(rhs_value));
        break;
      case GEU_BOOL:
        if (lhs_width == 32 && rhs_width == 32)
          return make_eval_bool(lhs_value.i32_ge_u(rhs_value));
        if (lhs_width == 64 && rhs_width == 64)
          return make_eval_bool(lhs_value.i64_ge_u(rhs_value));
        break;
      case AND:
        return make_eval_bool(lhs_value.logical_and(rhs_value));
      case OR:
        return make_eval_bool(lhs_value.logical_or(rhs_value));
      default:
        break;
      }
      assert(false && "Operation not supported in evaluation");
    }
  }

  if (op == EQ_BOOL) {
    if (auto lhs_unary = dynamic_cast<SymUnary *>(lhs.symptr.get())) {
      if (auto rhs_concrete = dynamic_cast<SymConcrete *>(rhs.symptr.get())) {
        if (lhs_unary->op == BOOL2BV) {
          auto rhs_value = rhs_concrete->value;
          if (rhs_value.value == 0) {
            auto result = lhs_unary->value.bool_not();
            BinaryOperationStore.insert({key, result});
            return result;
          }
        }
      }
    }

    if (auto rhs_unary = dynamic_cast<SymUnary *>(rhs.symptr.get())) {
      if (auto lhs_concrete = dynamic_cast<SymConcrete *>(lhs.symptr.get())) {
        if (rhs_unary->op == BOOL2BV) {
          auto lhs_value = lhs_concrete->value;
          if (lhs_value.value == 0) {
            auto result = rhs_unary->value.bool_not();
            BinaryOperationStore.insert({key, result});
            return result;
          }
        }
      }
    }
  }

  if (op == NEQ_BOOL) {
    if (auto lhs_unary = dynamic_cast<SymUnary *>(lhs.symptr.get())) {
      if (auto rhs_concrete = dynamic_cast<SymConcrete *>(rhs.symptr.get())) {
        if (rhs_concrete->kind == KindBV && rhs_concrete->value.value == 0) {
          if (lhs_unary->op == BOOL2BV) {
            auto result = lhs_unary->value;
            BinaryOperationStore.insert({key, result});
            return result;
          }
        }
      }
    }
    if (auto rhs_unary = dynamic_cast<SymUnary *>(rhs.symptr.get())) {
      if (auto lhs_concrete = dynamic_cast<SymConcrete *>(lhs.symptr.get())) {
        if (lhs_concrete->kind == KindBV && lhs_concrete->value.value == 0) {
          if (rhs_unary->op == BOOL2BV) {
            auto result = rhs_unary->value;
            BinaryOperationStore.insert({key, result});
            return result;
          }
        }
      }
    }
  }

  if (op == EQ_BOOL && lhs == rhs) {
    auto result = SVFactory::make_concrete_bool(true);
    BinaryOperationStore.insert({key, result});
    return result;
  }

  if (op == NEQ_BOOL && lhs == rhs) {
    auto result = SVFactory::make_concrete_bool(false);
    BinaryOperationStore.insert({key, result});
    return result;
  }

  if ((op == GT_BOOL || op == LT_BOOL || NEQ_BOOL) && lhs == rhs) {
    auto result = SVFactory::make_concrete_bool(false);
    BinaryOperationStore.insert({key, result});
    return result;
  }

  if (op == AND) {
    if (auto rhs_concrete = dynamic_cast<SymConcrete *>(rhs.symptr.get())) {
      if (rhs_concrete->kind == KindBool && rhs_concrete->value.value == 0) {
        auto result = SVFactory::make_concrete_bool(false);
        BinaryOperationStore.insert({key, result});
        return result;
      }
    }
    if (auto lhs_concrete = dynamic_cast<SymConcrete *>(lhs.symptr.get())) {
      if (lhs_concrete->kind == KindBool && lhs_concrete->value.value == 0) {
        auto result = SVFactory::make_concrete_bool(false);
        BinaryOperationStore.insert({key, result});
        return result;
      }
    }
    if (auto rhs_concrete = dynamic_cast<SymConcrete *>(rhs.symptr.get())) {
      if (rhs_concrete->kind == KindBool && rhs_concrete->value.value != 0) {
        BinaryOperationStore.insert({key, lhs});
        return lhs;
      }
    }
    if (auto lhs_concrete = dynamic_cast<SymConcrete *>(lhs.symptr.get())) {
      if (lhs_concrete->kind == KindBool && lhs_concrete->value.value != 0) {
        BinaryOperationStore.insert({key, rhs});
        return rhs;
      }
    }
  }

  if (op == B_AND) {
    if (auto lhs_unary = dynamic_cast<SymUnary *>(lhs.symptr.get())) {
      if (auto rhs_unary = dynamic_cast<SymUnary *>(rhs.symptr.get())) {
        if (lhs_unary->op == BOOL2BV && rhs_unary->op == BOOL2BV) {
          auto result = lhs_unary->value.land(rhs_unary->value).bool2bv();
          BinaryOperationStore.insert({key, result});
          return result;
        }
      }
    }

    if (auto rhs_concrete = dynamic_cast<SymConcrete *>(rhs.symptr.get())) {
      if (rhs_concrete->kind == KindBV && rhs_concrete->value.value == 1) {
        if (auto lhs_unary = dynamic_cast<SymUnary *>(lhs.symptr.get())) {
          if (lhs_unary->op == BOOL2BV) {
            BinaryOperationStore.insert({key, lhs});
            return lhs;
          }
        }
      }
    }

    if (auto lhs_concrete = dynamic_cast<SymConcrete *>(lhs.symptr.get())) {
      if (lhs_concrete->kind == KindBV && lhs_concrete->value.value == 1) {
        if (auto rhs_unary = dynamic_cast<SymUnary *>(rhs.symptr.get())) {
          if (rhs_unary->op == BOOL2BV) {
            BinaryOperationStore.insert({key, rhs});
            return rhs;
          }
        }
      }
    }
  }

  auto result = SymVal(SVFactory::SymBookKeeper.allocate<SymBinary>(op, lhs, rhs));
  BinaryOperationStore.insert({key, result});
  return result;
}

inline SymVal make_unary(UnaryOperation op, const SymVal &value) {
  assert(value.symptr != nullptr);

  UnaryOpKey key(op, value);
  auto it = UnaryOperationStore.find(key);
  if (it != UnaryOperationStore.end()) {
    return it->second;
  }

  if (op == BOOL2BV) {
    if (auto concrete = dynamic_cast<SymConcrete *>(value.symptr.get())) {
      auto value_conc = concrete->value;
      if (concrete->kind == KindBool) {
        if (value_conc.value != 0) {
          auto result = SVFactory::make_concrete_bv(Num(I32V(1)), 32);
          UnaryOperationStore.insert({key, result});
          return result;
        } else {
          auto result = SVFactory::make_concrete_bv(Num(I32V(0)), 32);
          UnaryOperationStore.insert({key, result});
          return result;
        }
      }
    }
  }

  if (op == NOT) {
    if (auto concrete = dynamic_cast<SymConcrete *>(value.symptr.get())) {
      if (concrete->kind == KindBool) {
        auto result = SVFactory::make_concrete_bool(concrete->value.value == 0);
        UnaryOperationStore.insert({key, result});
        return result;
      }
    }

    if (auto inner_unary = dynamic_cast<SymUnary *>(value.symptr.get())) {
      if (inner_unary->op == NOT) {
        auto result = inner_unary->value;
        UnaryOperationStore.insert({key, result});
        return result;
      }
    }

    if (auto inner_binary = dynamic_cast<SymBinary *>(value.symptr.get())) {
      BinOperation negated_op;
      switch (inner_binary->op) {
      case EQ_BOOL:
        negated_op = NEQ_BOOL;
        break;
      case NEQ_BOOL:
        negated_op = EQ_BOOL;
        break;
      case LT_BOOL:
        negated_op = GEQ_BOOL;
        break;
      case GT_BOOL:
        negated_op = LEQ_BOOL;
        break;
      case LEQ_BOOL:
        negated_op = GT_BOOL;
        break;
      case GEQ_BOOL:
        negated_op = LT_BOOL;
        break;
      default:
        negated_op = inner_binary->op;
        break;
      }
      if (negated_op != inner_binary->op) {
        auto result =
            SVFactory::make_binary(negated_op, inner_binary->lhs, inner_binary->rhs);
        UnaryOperationStore.insert({key, result});
        return result;
      }
    }
  }

  auto result = SymVal(SVFactory::SymBookKeeper.allocate<SymUnary>(op, value));
  UnaryOperationStore.insert({key, result});
  return result;
}

inline SymVal make_concat(const SymVal &lhs, const SymVal &rhs) {
  if (auto lhs_concrete = std::dynamic_pointer_cast<SymConcrete>(lhs.symptr)) {
    if (auto rhs_concrete = std::dynamic_pointer_cast<SymConcrete>(rhs.symptr)) {
      if (lhs_concrete->kind == KindBV && rhs_concrete->kind == KindBV) {
        int new_width = lhs_concrete->width() + rhs_concrete->width();
        int64_t new_value =
            (lhs_concrete->value.value << rhs_concrete->width()) |
            rhs_concrete->value.value;
        return SVFactory::make_smallbv(new_width, new_value);
      }
    }
  }
  if (auto extract1 = std::dynamic_pointer_cast<SymExtract>(lhs.symptr)) {
    if (auto extract2 = std::dynamic_pointer_cast<SymExtract>(rhs.symptr)) {
      if (extract1->low == extract2->high + 1 &&
          extract1->value == extract2->value) {
        if (extract1->high == 4 && extract2->low == 1) {
          // special case for full 4-byte extract concatenation
          // TODO: support 64-bit later, this optimization is only valid when we
          // only work on 32-bit values
          return extract1->value;
        }
        // two extracts are adjacent, we can merge them
        return extract1->value.extract(extract1->high, extract2->low);
      }
    }
  }
  return SVFactory::make_binary(CONCAT, lhs, rhs);
}

} // namespace SVFactory

#endif // WASM_SYMVAL_FACTORY_HPP
