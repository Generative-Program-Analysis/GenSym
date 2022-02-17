#ifndef LLSC_VALUE_OPS_HEADERS
#define LLSC_VALUE_OPS_HEADERS

struct Value;
struct IntV;
struct SS;

using PtrVal = std::shared_ptr<Value>;
inline PtrVal bv_extract(PtrVal v1, int hi, int lo);

/* Value representations */

struct Value : public std::enable_shared_from_this<Value>, public Printable {
  virtual bool is_conc() const = 0;
  virtual int get_bw() const = 0;
  size_t get_byte_size() const { return (get_bw() + 7) / 8; }
  virtual bool compare(const Value *v) const = 0;

  /* `to_bytes` produces the memory representation of this value
   * following 64-bit little-endian data layout.
   */
  virtual List<PtrVal> to_bytes() = 0;

  /* `to_bytes_shadow` produces a compact memory representation
   * of this value, potentially using shadow values.
   * This should give us an ``optimized'' data layout that
   * avoids low-level concat or shift.
   */
  virtual List<PtrVal> to_bytes_shadow() = 0;

  virtual PtrVal to_SMT() = 0;
  virtual std::shared_ptr<IntV> to_IntV() = 0;

  size_t hashval;
  Value() : hashval(0) {}
  size_t& hash() { return hashval; }

  /* Since from_bytes/from_bytes_shadow only concate ``bit-vectors'' (either concrete or symbolic),
   * and they do not work with location/function values, at some point, we may find that
   * it doesn't make much sense to distinguish Int and Float as variants of Value...
   */
  static PtrVal from_bytes(List<PtrVal>&& xs) {
    // Note: it should work with a List of SymV/IntV, containing _no_ ShadowV/LocV/FunV
    // XXX what if v is nullptr/padding
    return Vec::foldRight(xs.take(xs.size()-1), xs.back(), [](auto&& x, auto&& acc) { return bv_concat(acc, x); });
  }
  static PtrVal from_bytes_shadow(List<PtrVal>&& xs) {
    // Note: it should work with a List of SymV/IntV/ShadowV, containing _no_ LocV/FunV.
    //       However, the head of xs should not be a Shadow V, if so the List is "incomplete".
    auto reified = List<PtrVal>{}.transient();
    for (auto i = 0; i < xs.size(); ) {
      auto v = xs.at(i);
      // XXX what if v is nullptr/padding
      int sz = v->get_byte_size();
      if (i + sz > xs.size()) {
        reified.append(v->to_bytes().take(xs.size()-i).transient());
        break;
      } else {
        reified.push_back(std::move(v));
        i += sz;
      }
    }
    return from_bytes(reified.persistent().take(xs.size()));
  }
};

template<>
struct std::hash<PtrVal> {
  size_t operator()(PtrVal const& v) const noexcept {
    return v ? v->hash() : std::hash<nullptr_t>{}(nullptr);
  }
};

template<>
struct std::equal_to<PtrVal> {
  bool operator()(PtrVal const& a, PtrVal const& b) const {
    if (!a || !b) return a == b;
    if (std::type_index(typeid(*a)) != std::type_index(typeid(*b)))
      return false;
    return a->compare(b.get());
  }
};

template<>
struct std::equal_to<immer::flex_vector<PtrVal>> {
  bool operator()(immer::flex_vector<PtrVal> const& a, immer::flex_vector<PtrVal> const& b) const {
    if (a.size() != b.size()) return false;
    std::equal_to<PtrVal> cmp;
    for (int i = 0; i < a.size(); i++)
      if (!cmp(a.at(i), b.at(i))) return false;
    return true;
  }
};

struct ShadowV : public Value {
  int8_t offset;
  ShadowV() : offset(0) {}
  ShadowV(int8_t offset) : offset(offset) {}
  virtual bool is_conc() const { return true; };
  virtual int get_bw() const { return 0; }
  virtual bool compare(const Value *v) const { return false; }
  virtual PtrVal to_SMT() { return nullptr; }
  virtual std::shared_ptr<IntV> to_IntV() { return nullptr; }
  virtual std::string toString() const { return "❏"; }
  virtual List<PtrVal> to_bytes() { return List<PtrVal>{shared_from_this()}; }
  virtual List<PtrVal> to_bytes_shadow() { return to_bytes(); }
};

inline PtrVal make_ShadowV() {
  static PtrVal singleton = std::make_shared<ShadowV>();
  return singleton;
}

inline PtrVal make_ShadowV(int8_t offset) {
  ASSERT(-8 < offset && offset < 0, "unexpected ShadowV's offset");
  static PtrVal shadow_vals[8] = {
    nullptr,
    std::make_shared<ShadowV>(-1), std::make_shared<ShadowV>(-2),
    std::make_shared<ShadowV>(-3), std::make_shared<ShadowV>(-4),
    std::make_shared<ShadowV>(-5), std::make_shared<ShadowV>(-6),
    std::make_shared<ShadowV>(-7) };
  return shadow_vals[-offset];
}

inline List<PtrVal> make_ShadowV_seq(int8_t size) {
  auto res = List<PtrVal>{}.transient();
  for (size_t i = 1; i <= size; i++) {
    res.push_back(make_ShadowV(-i));
  }
  return res.persistent();
}

// FunV types:
//   use template to delay type instantiation
//   cause SS is currently incomplete, unable to use in containers

template <typename func_t>
struct FunV : Value {
  func_t f;
  FunV(func_t f) : f(f) {
    hash_combine(hash(), std::string("funv"));
    hash_combine(hash(), f);
  }
  std::string toString() const override {
    std::ostringstream ss;
    ss << "FunV(" << f << ")";
    return ss.str();
  }
  virtual PtrVal to_SMT() override {
    ABORT("to_SMT: unexpected value FunV.");
  }
  virtual std::shared_ptr<IntV> to_IntV() override {
    ABORT("to_IntV: TODO for FunV?");
  }
  virtual bool is_conc() const override { return true; }
  virtual int get_bw() const override { return addr_bw; }
  virtual bool compare(const Value *v) const override {
    auto that = static_cast<decltype(this)>(v);
    return this->f == that->f;
  }
  virtual List<PtrVal> to_bytes() {
    return List<PtrVal>{shared_from_this()} + List<PtrVal>(7, make_ShadowV());
  }
  virtual List<PtrVal> to_bytes_shadow() {
    return List<PtrVal>{shared_from_this()} + make_ShadowV_seq(7);
  }
};

struct IntV : Value {
  int bw;
  IntData i;
  IntV(IntData i, int bw) : i(i), bw(bw) {
    hash_combine(hash(), std::string("intv"));
    hash_combine(hash(), i);
    hash_combine(hash(), bw);
  }
  IntV(const IntV& v) : IntV(v.i, v.bw) {}
  std::string toString() const override {
    std::ostringstream ss;
    ss << "IntV(" << as_signed() << ", " << bw << ")";
    return ss.str();
  }
  virtual PtrVal to_SMT() override {
    ABORT("to_SMT: unexpected value IntV.");
  }
  virtual std::shared_ptr<IntV> to_IntV() override {
    auto thisptr = shared_from_this();
    return std::static_pointer_cast<IntV>(thisptr);
  }
  virtual bool is_conc() const override { return true; }
  virtual int get_bw() const override { return bw; }

  virtual bool compare(const Value *v) const override {
    auto that = static_cast<decltype(this)>(v);
    if (this->i != that->i) return false;
    return this->bw == that->bw;
  }

  int64_t as_signed() const { return int64_t(i) >> (addr_bw - bw); }

  virtual List<PtrVal> to_bytes() {
    if (bw <= 8) return List<PtrVal>{shared_from_this()};
    auto res = List<PtrVal>{}.transient();
    for (size_t i = 0; i < bw/8; i++) {
      res.push_back(bv_extract(shared_from_this(), (i+1)*8-1, i*8));
    }
    return res.persistent();
  }
  virtual List<PtrVal> to_bytes_shadow() {
    if (bw <= 8) return List<PtrVal>{shared_from_this()};
    return List<PtrVal>{shared_from_this()} + make_ShadowV_seq(bw/8 - 1);
  }
};

inline PtrVal make_IntV(IntData i, int bw=bitwidth, bool toMSB=true) {
  return std::make_shared<IntV>(toMSB ? (i << (addr_bw - bw)) : i, bw);
}

inline IntData proj_IntV(PtrVal v) {
  if (v->get_bw() == 1) return std::dynamic_pointer_cast<IntV>(v)->i ? 1 : 0;
  return std::dynamic_pointer_cast<IntV>(v)->as_signed();
}

inline char proj_IntV_char(PtrVal v) {
  std::shared_ptr<IntV> intV = v->to_IntV();
  ASSERT(intV->get_bw() == 8, "proj_IntV_char: Bitwidth mismatch");
  return static_cast<char>(proj_IntV(intV));
}

struct FloatV : Value {
  float f;
  FloatV(float f) : f(f) {
    hash_combine(hash(), std::string("floatv"));
    hash_combine(hash(), f);
  }
  FloatV(const FloatV& v): FloatV(v.f) {}
  std::string toString() const override {
    std::ostringstream ss;
    ss << "FloatV(" << f << ")";
    return ss.str();
  }
  virtual PtrVal to_SMT() override {
    ABORT("to_SMT: unexpected value FloatV.");
  }
  virtual bool is_conc() const override { return true; }
  virtual std::shared_ptr<IntV> to_IntV() override { return nullptr; }
  virtual int get_bw() const override { return 32; } //TODO: support bw other than 32

  virtual bool compare(const Value *v) const override {
    auto that = static_cast<decltype(this)>(v);
    return this->f == that->f;
  }
  virtual List<PtrVal> to_bytes() {
    return List<PtrVal>{shared_from_this()} + List<PtrVal>(3, make_ShadowV()); // TODO: support bw other than 32
  }
  virtual List<PtrVal> to_bytes_shadow() {
    return List<PtrVal>{shared_from_this()} + make_ShadowV_seq(3);
  }
};

inline PtrVal make_FloatV(float f) {
  return std::make_shared<FloatV>(f);
}

inline PtrVal make_FloatV(float f, size_t bw) {
  return std::make_shared<FloatV>(f);
}

inline int proj_FloatV(PtrVal v) {
  return std::dynamic_pointer_cast<FloatV>(v)->f;
}

struct LocV : Value {
  enum Kind { kStack, kHeap };
  static const int64_t stack_offset = 1LL<<30;
  Addr l;
  Kind k;
  int size;

  LocV(Addr l, Kind k, int size) : l(l), k(k), size(size) {
    hash_combine(hash(), std::string("locv"));
    hash_combine(hash(), k);
    hash_combine(hash(), l);
  }
  LocV(const LocV& v) : LocV(v.l, v.k, v.size) {}
  std::string toString() const override {
    std::ostringstream ss;
    ss << "LocV(" << l << ", " << std::string(k == kStack ? "kStack" : "kHeap") << ")";
    return ss.str();
  }
  virtual PtrVal to_SMT() override {
    ABORT("to_SMT: unexpected value LocV.");
  }
  virtual bool is_conc() const override {
    ABORT("is_conc: unexpected value LocV.");
  }
  virtual std::shared_ptr<IntV> to_IntV() override {
    return std::make_shared<IntV>(l + (k == kStack ? stack_offset : 0), addr_bw);
  }
  virtual int get_bw() const override { return addr_bw; }

  virtual bool compare(const Value *v) const override {
    auto that = static_cast<decltype(this)>(v);
    if (this->l != that->l) return false;
    return this->k == that->k;
  }
  virtual List<PtrVal> to_bytes() {
    return List<PtrVal>{shared_from_this()} + List<PtrVal>(7, make_ShadowV());
  }
  virtual List<PtrVal> to_bytes_shadow() {
    return List<PtrVal>{shared_from_this()} + make_ShadowV_seq(7);
  }
};

inline PtrVal make_LocV(unsigned int i, LocV::Kind k, int size) {
  return std::make_shared<LocV>(i, k, size);
}

inline PtrVal make_LocV(unsigned int i, LocV::Kind k) {
  return std::make_shared<LocV>(i, k, -1);
}

inline PtrVal make_LocV(PtrVal v) {
  auto v2 = std::dynamic_pointer_cast<IntV>(v);
  assert(v2->get_bw() == addr_bw);
  if (v2->i >= LocV::stack_offset)
    return make_LocV(v2->i - LocV::stack_offset, LocV::kStack);
  else
    return make_LocV(v2->i, LocV::kHeap);
}

inline unsigned int proj_LocV(PtrVal v) {
  return std::dynamic_pointer_cast<LocV>(v)->l;
}
inline LocV::Kind proj_LocV_kind(PtrVal v) {
  return std::dynamic_pointer_cast<LocV>(v)->k;
}
inline int proj_LocV_size(PtrVal v) {
  return std::dynamic_pointer_cast<LocV>(v)->size;
}

inline PtrVal make_LocV_null() {
  static const PtrVal loc0 = make_LocV(0, LocV::kHeap);
  return loc0;
}
inline bool is_LocV_null(PtrVal v) {
  return v == make_LocV_null();
}

inline PtrVal operator+ (const PtrVal& lhs, const int& rhs) {
  if (auto loc = std::dynamic_pointer_cast<LocV>(lhs)) {
    return make_LocV(loc->l + rhs, loc->k, loc->size);
  }
  if (auto i = std::dynamic_pointer_cast<IntV>(lhs)) {
    return make_IntV(i->i + rhs, i->bw);
  }
  ABORT("Unknown application of operator+");
}

struct SymV : Value {
  String name;
  int bw;
  iOP rator;
  immer::flex_vector<PtrVal> rands;
  SymV(String name, int bw) : name(name), bw(bw) {
    hash_combine(hash(), std::string("symv1"));
    hash_combine(hash(), name);
    hash_combine(hash(), bw);
  }
  SymV(iOP rator, immer::flex_vector<PtrVal> rands, int bw) : rator(rator), rands(rands), bw(bw) {
    hash_combine(hash(), std::string("symv2"));
    hash_combine(hash(), rator);
    hash_combine(hash(), bw);
    for (auto &r: rands) hash_combine(hash(), std::hash<PtrVal>{}(r));
  }
  std::string toString() const override {
    std::ostringstream ss;
    ss << "SymV(";
    if (!name.empty()) {
      ss << name;
    } else {
      ss << int_op2string(rator) << ", { ";
      for (auto e : rands) {
        ss << *e << ", ";
      }
      ss << "}";
    }
    ss << ", " << bw << ")";
    return ss.str();
  }
  virtual PtrVal to_SMT() override { return shared_from_this(); }
  virtual bool is_conc() const override { return false; }
  virtual std::shared_ptr<IntV> to_IntV() override { return nullptr; }
  virtual int get_bw() const override { return bw; }

  virtual bool compare(const Value *v) const override {
    auto that = static_cast<decltype(this)>(v);
    if (this->bw != that->bw) return false;
    if (this->name != that->name) return false;
    if (this->rator != that->rator) return false;
    return std::equal_to<decltype(rands)>{}(this->rands, that->rands);
  }
  virtual List<PtrVal> to_bytes() {
    if (bw <= 8) return List<PtrVal>{shared_from_this()};
    auto res = List<PtrVal>{}.transient();
    for (size_t i = bw/8; i > 0; i--) {
      res.push_back(bv_extract(shared_from_this(), i*8-1, (i-1)*8));
    }
    return res.persistent();
  }
  virtual List<PtrVal> to_bytes_shadow() {
    if (bw <= 8) return List<PtrVal>{shared_from_this()};
    return List<PtrVal>{shared_from_this()} + make_ShadowV_seq(bw/8 - 1);
  }
};

inline PtrVal make_SymV(String n) {
  return std::make_shared<SymV>(n, bitwidth);
}
inline PtrVal make_SymV(String n, int bw) {
  return std::make_shared<SymV>(n, bw);
}
inline PtrVal to_SMTNeg(PtrVal v) {
  int bw = v->get_bw();
  return std::make_shared<SymV>(op_neg, immer::flex_vector({ v }), bw);
}

struct StructV : Value {
  immer::flex_vector<PtrVal> fs;
  StructV(immer::flex_vector<PtrVal> fs) : fs(fs) {
    hash_combine(hash(), std::string("structv"));
    for (auto &f: fs) hash_combine(hash(), std::hash<PtrVal>{}(f));
  }
  StructV(std::vector<PtrVal> fs) : fs(fs.begin(), fs.end()) {
    hash_combine(hash(), std::string("structv"));
    for (auto &f: fs) hash_combine(hash(), std::hash<PtrVal>{}(f));
  }
  std::string toString() const override {
    std::ostringstream ss;
    ss << "StructV(..)";
    return ss.str();
  }
  virtual PtrVal to_SMT() override {
    ABORT("to_SMT: unexpected value StructV.");
  }
  virtual bool is_conc() const override {
    ABORT("is_conc: unexpected value StructV.");
  }
  virtual std::shared_ptr<IntV> to_IntV() override { return nullptr; }
  virtual int get_bw() const override { ABORT("get_bw: unexpected value StructV."); }

  virtual bool compare(const Value *v) const override {
    auto that = static_cast<decltype(this)>(v);
    return std::equal_to<decltype(fs)>{}(this->fs, that->fs);
  }

  virtual List<PtrVal> to_bytes() { ABORT("???"); }
  virtual List<PtrVal> to_bytes_shadow() { ABORT("???"); }
};

inline PtrVal structV_at(PtrVal v, int idx) {
  auto sv = std::dynamic_pointer_cast<StructV>(v);
  if (sv) return (sv->fs).at(idx);
  ABORT("StructV_at: non StructV value");
}

// assume all values are signed, convert to unsigned if necessary
// require return value to be signed or non-negative
inline PtrVal int_op_2(iOP op, PtrVal v1, PtrVal v2) {
  auto i1 = v1->to_IntV();
  auto i2 = v2->to_IntV();
  int bw1 = v1->get_bw();
  int bw2 = v2->get_bw();
  if (bw1 != bw2) {
    std::cout << *v1 << " " << int_op2string(op) << " " << *v2 << "\n";
    ABORT("int_op_2: bitwidth of operands mismatch");
  }
  if (i1 && i2) {
    switch (op) {
    case op_add:
      return make_IntV(i1->i + i2->i, bw1, false);
    case op_sub:
      return make_IntV(i1->i - i2->i, bw1, false);
    case op_mul:
      return make_IntV(i1->i * i2->as_signed(), bw1, false);
    case op_sdiv:  // divide overflow is hardware exception
      return make_IntV(int64_t(i1->i) / int64_t(i2->i), bw1);
    case op_udiv:
      return make_IntV(uint64_t(i1->i) / uint64_t(i2->i), bw1);
    case op_srem:
      return make_IntV(int64_t(i1->i) % int64_t(i2->i), bw1, false);
    case op_urem:
      return make_IntV(uint64_t(i1->i) % uint64_t(i2->i), bw1, false);
    case op_eq:
      return make_IntV(i1->i == i2->i, 1);
    case op_neq:
      return make_IntV(i1->i != i2->i, 1);
    case op_uge:
      return make_IntV(uint64_t(i1->i) >= uint64_t(i2->i), 1);
    case op_sge:
      return make_IntV(int64_t(i1->i) >= int64_t(i2->i), 1);
    case op_ugt:
      return make_IntV(uint64_t(i1->i) > uint64_t(i2->i), 1);
    case op_sgt:
      return make_IntV(int64_t(i1->i) > int64_t(i2->i), 1);
    case op_ule:
      return make_IntV(uint64_t(i1->i) <= uint64_t(i2->i), 1);
    case op_sle:
      return make_IntV(int64_t(i1->i) <= int64_t(i2->i), 1);
    case op_ult:
      return make_IntV(uint64_t(i1->i) < uint64_t(i2->i), 1);
    case op_slt:
      return make_IntV(int64_t(i1->i) < int64_t(i2->i), 1);
    case op_and:
      return make_IntV(i1->i & i2->i, bw1, false);
    case op_or:
      return make_IntV(i1->i | i2->i, bw1, false);
    case op_xor:
      return make_IntV(i1->i ^ i2->i, bw1, false);
    case op_shl:
      return make_IntV(i1->i << i2->as_signed(), bw1, false);
    case op_ashr:
      return make_IntV(int64_t(i1->i) >> (i2->as_signed() + addr_bw - bw1), bw1);
    case op_lshr:
      return make_IntV(uint64_t(i1->i) >> (i2->as_signed() + addr_bw - bw1), bw1);
    default:
      std::cout << op << std::endl;
      ABORT("invalid operator");
    }
  } else {
    return std::make_shared<SymV>(op, immer::flex_vector({ v1, v2 }), bw1);
  }
}

inline PtrVal float_op_2(fOP op, PtrVal v1, PtrVal v2) {
  auto f1 = std::dynamic_pointer_cast<FloatV>(v1);
  auto f2 = std::dynamic_pointer_cast<FloatV>(v2);

  if (f1 && f2) {
    if (op == op_fadd) { return make_FloatV(f1->f + f2->f); }
    else if (op == op_fsub) { return make_FloatV(f1->f - f2->f); }
    else if (op == op_fmul) { return make_FloatV(f1->f * f2->f); }
    else if (op == op_fdiv) { return make_FloatV(f1->f / f2->f); }
    // FIXME: Float cmp operations
    else { return make_IntV(1); }
  } else {
    ABORT("Non-concrete Float Detected");
  }
}

inline PtrVal bv_sext(PtrVal v, int bw) {
  auto i1 = std::dynamic_pointer_cast<IntV>(v);
  if (i1) {
    return make_IntV(int64_t(i1->i) >> (bw - i1->bw), bw, false);
  } else {
    auto s1 = std::dynamic_pointer_cast<SymV>(v);
    if (s1) {
      // Note: instead of passing new bw as an operand
      // we override the original bw here
      return std::make_shared<SymV>(op_sext, immer::flex_vector<PtrVal>({ s1 }), bw);
    }
    ABORT("Sext an invalid value, exit");
  }
}

inline PtrVal bv_zext(PtrVal v, int bw) {
  auto i1 = std::dynamic_pointer_cast<IntV>(v);
  if (i1) {
    return make_IntV(uint64_t(i1->i) >> (bw - i1->bw), bw, false);
  } else {
    auto s1 = std::dynamic_pointer_cast<SymV>(v);
    if (s1) {
      // Note: instead of passing new bw as an operand
      // we override the original bw here
      return std::make_shared<SymV>(op_zext, immer::flex_vector<PtrVal>({ s1 }), bw);
    }
    ABORT("Zext an invalid value, exit");
  }
}

inline PtrVal trunc(PtrVal v1, int from, int to) {
  auto i1 = std::dynamic_pointer_cast<IntV>(v1);
  if (i1) {
    return make_IntV(i1->i << (from - to), to, false);
  }
  auto s1 = std::dynamic_pointer_cast<SymV>(v1);
  if (s1) {
    return std::make_shared<SymV>(op_trunc, immer::flex_vector({ v1 }), to);
  }
  ABORT("Truncate an invalid value, exit");
}

inline PtrVal bv_extract(PtrVal v1, int hi, int lo) {
  auto i1 = std::dynamic_pointer_cast<IntV>(v1);
  if (i1) {
    return make_IntV(i1->i >> (lo + addr_bw - i1->bw), hi - lo + 1);
  }
  auto s1 = std::dynamic_pointer_cast<SymV>(v1);
  if (s1) {
    return std::make_shared<SymV>(op_extract,
      immer::flex_vector<PtrVal> { s1, make_IntV(hi), make_IntV(lo) }, hi - lo + 1);
  }
  ABORT("Extract an invalid value, exit");
}

inline PtrVal bv_concat(PtrVal v1, PtrVal v2) {
  auto i1 = v1->to_IntV();
  auto i2 = v2->to_IntV();
  int bw1 = v1->get_bw();
  int bw2 = v2->get_bw();
  assert(bw1 + bw2 <= addr_bw);
  if (i1 && i2) return make_IntV(i1->i | (uint64_t(i2->i) >> bw1), bw1 + bw2, false);
  ASSERT(!std::dynamic_pointer_cast<ShadowV>(v1) && !std::dynamic_pointer_cast<ShadowV>(v2),
         "Cannot concat ShadowV values");
  // XXX: also check LocV and FunV?
  return std::make_shared<SymV>(op_concat, immer::flex_vector<PtrVal> { v1, v2 }, bw1 + bw2);
}

inline const PtrVal IntV0 = make_IntV(0);

inline std::string ptrval_to_string(const PtrVal& ptr) {
  return std::string(ptr == nullptr ? "nullptr" : ptr->toString());
}

#endif
