#ifndef LLSC_VALUE_OPS_HEADERS
#define LLSC_VALUE_OPS_HEADERS

struct Value;
struct IntV;
struct SymV;
struct SymLocV;
struct SS;
class PC;

template <typename T>
struct simple_ptr {
  T *ptr;
public:
  simple_ptr(T* p): ptr(p) { }
  simple_ptr(): simple_ptr(nullptr) { }
  template<typename U, typename = std::enable_if_t<std::is_base_of_v<T, U>>>
  simple_ptr(const simple_ptr<U> &rhs): simple_ptr(static_cast<T*>(rhs.get())) { }
  ~simple_ptr() { }
  T* get() const { return ptr; }
  T& operator*() const { return *ptr; }
  T* operator->() const { return ptr; }
  explicit operator bool() const { return bool(ptr); }
  bool operator<(const simple_ptr& rhs) const { return ptr < rhs.ptr; }
  bool operator!=(const simple_ptr& rhs) const { return ptr != rhs.ptr; }
  bool operator==(const simple_ptr& rhs) const { return ptr == rhs.ptr; }
};

template <typename T>
std::ostream& operator<<(std::ostream& outs, const simple_ptr<T>& rhs) {
  return outs << rhs.ptr;
}

template <typename T>
struct enable_simple_from_this {
  simple_ptr<T> shared_from_this() {
    return static_cast<T*>(this);
  }
};

template<typename T, typename... Args>
simple_ptr<T> make_simple(Args&&... args) {
  return simple_ptr(new T(std::forward<Args>(args)...));
}

namespace std {
template<typename T, typename U>
simple_ptr<T> dynamic_pointer_cast(simple_ptr<U> v) {
  return simple_ptr<T>(dynamic_cast<T*>(v.get()));
}

template<typename T, typename U>
simple_ptr<T> static_pointer_cast(simple_ptr<U> v) {
  return simple_ptr<T>(static_cast<T*>(v.get()));
}

template <typename T>
struct hash<simple_ptr<T>> {
  size_t operator()(const simple_ptr<T> &rhs) const noexcept {
    return hash<T*>{}(rhs.get());
  }
};
}

using PtrVal = simple_ptr<Value>;
inline PtrVal bv_extract(const PtrVal& v1, int hi, int lo);
inline PtrVal bv_sext(const PtrVal& v, size_t bw);
inline PtrVal bv_zext(const PtrVal& v, size_t bw);
inline PtrVal make_IntV(IntData i, size_t bw=default_bw, bool toMSB=true);
inline std::pair<bool, UIntData> get_sat_value(PC pc, PtrVal v);

/* Value representations */

struct Value : public enable_simple_from_this<Value>, public Printable {
  virtual bool is_conc() const = 0;
  virtual size_t get_bw() const = 0;
  size_t get_byte_size() const { return (get_bw() + 7) / 8; }
  virtual bool compare(const Value* v) const = 0;
  virtual simple_ptr<IntV> to_IntV() = 0;
  inline bool operator==(const Value& rhs){ return compare(&rhs); }

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

  size_t hashval;
  Value() : hashval(0) {}
  size_t& hash() { return hashval; }

  /* Since from_bytes/from_bytes_shadow only concate ``bit-vectors'' (either concrete or symbolic),
   * and they do not work with location/function values, at some point, we may find that
   * it doesn't make much sense to distinguish Int and Float as variants of Value...
   */
  static PtrVal from_bytes(const List<PtrVal>& xs) {
    // Note: it should work with a List of SymV/IntV, containing _no_ ShadowV/LocV/FunV
    // XXX what if v is nullptr/padding
    return Vec::foldRight(xs.take(xs.size()-1), xs.back(), [](auto&& x, auto&& acc) { return bv_concat(acc, x); });
  }
  static PtrVal from_bytes_shadow(const List<PtrVal>& xs) {
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

  static List<PtrVal> from_string(const std::string& str) {
    immer::flex_vector_transient<PtrVal> res;
    for (auto& c : str) {
      res.push_back(make_IntV(c, 8));
    }
    return res.persistent();
  }

};

struct hash_PtrVal {
  size_t operator()(PtrVal const& v) const noexcept {
    return v ? v->hash() : std::hash<nullptr_t>{}(nullptr);
  }
};

struct equal_to_PtrVal {
  bool operator()(PtrVal const& a, PtrVal const& b) const {
    if (!a || !b) return a == b;
    if (std::type_index(typeid(*a)) != std::type_index(typeid(*b)))
      return false;
    return a->compare(b.get());
  }
};

inline phmap::parallel_flat_hash_set<PtrVal,
    hash_PtrVal,
    equal_to_PtrVal,
    std::allocator<PtrVal>,
    4,
    std::mutex> objpool;

inline PtrVal hashconsing(const PtrVal &ret) {
  // if (!use_hashcons) return ret;
  auto [ret2, ins] = objpool.insert(ret);
  if (!ins) delete ret.get();
  return *ret2;
}

// Uninitialized value
inline PtrVal make_UinitV() {
  static PtrVal UinitV = make_IntV(0, 8);
  return UinitV;
}

struct ShadowV : public Value {
  int8_t offset;
  ShadowV() : offset(0) {}
  ShadowV(int8_t offset) : offset(offset) {}
  virtual bool is_conc() const { return true; };
  virtual size_t get_bw() const { return 0; }
  virtual bool compare(const Value* v) const { return false; }
  virtual simple_ptr<IntV> to_IntV() { return nullptr; }
  virtual std::string toString() const { return "❏"; }
  virtual List<PtrVal> to_bytes() { return List<PtrVal>{shared_from_this()}; }
  virtual List<PtrVal> to_bytes_shadow() { return to_bytes(); }
};

inline PtrVal make_ShadowV() {
  static PtrVal singleton = make_simple<ShadowV>();
  return singleton;
}

inline PtrVal make_ShadowV(int8_t offset) {
  ASSERT(-16 < offset && offset < 0, "unexpected ShadowV's offset");
  static PtrVal shadow_vals[16] = {
    nullptr,
    make_simple<ShadowV>(-1),   make_simple<ShadowV>(-2),
    make_simple<ShadowV>(-3),   make_simple<ShadowV>(-4),
    make_simple<ShadowV>(-5),   make_simple<ShadowV>(-6),
    make_simple<ShadowV>(-7),   make_simple<ShadowV>(-8),
    make_simple<ShadowV>(-9),   make_simple<ShadowV>(-10),
    make_simple<ShadowV>(-11),  make_simple<ShadowV>(-12),
    make_simple<ShadowV>(-13),  make_simple<ShadowV>(-14),
    make_simple<ShadowV>(-15)
  };
  return shadow_vals[-offset];
}

inline List<PtrVal> make_ShadowV_seq(int8_t size) {
  auto res = List<PtrVal>{}.transient();
  for (size_t i = 1; i <= size; i++) {
    res.push_back(make_ShadowV(-i));
  }
  return res.persistent();
}

struct IntV : Value {
  size_t bw;
  IntData i;
  IntV(IntData i, size_t bw) : i(i), bw(bw) {
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
  virtual simple_ptr<IntV> to_IntV() override {
    auto thisptr = shared_from_this();
    return std::static_pointer_cast<IntV>(thisptr);
  }
  virtual bool is_conc() const override { return true; }
  virtual size_t get_bw() const override { return bw; }

  virtual bool compare(const Value* v) const override {
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

inline PtrVal make_IntV(IntData i, size_t bw, bool toMSB) {
  auto ret = make_simple<IntV>(toMSB ? (i << (addr_bw - bw)) : i, bw);
  return hashconsing(ret);
}

inline IntData proj_IntV(const PtrVal& v) {
  if (v->get_bw() == 1) return std::dynamic_pointer_cast<IntV>(v)->i ? 1 : 0;
  return std::dynamic_pointer_cast<IntV>(v)->as_signed();
}

struct FloatV : Value {
  long double f;
  size_t bw;
  FloatV(long double f, size_t bw=32) : f(f), bw(bw) {
    hash_combine(hash(), std::string("floatv"));
    hash_combine(hash(), f);
    hash_combine(hash(), bw);
  }
  FloatV(const FloatV& v): FloatV(v.f, v.bw) {}
  std::string toString() const override {
    std::ostringstream ss;
    ss << "FloatV(f=" << f << ", bw=" << bw << ")";
    return ss.str();
  }
  virtual bool is_conc() const override { return true; }
  virtual simple_ptr<IntV> to_IntV() override { return nullptr; }
  virtual size_t get_bw() const override { return bw; }

  virtual bool compare(const Value* v) const override {
    auto that = static_cast<decltype(this)>(v);
    if (this->f != that->f) return false;
    return this->bw == that->bw;
  }
  virtual List<PtrVal> to_bytes() {
    return List<PtrVal>{shared_from_this()} + List<PtrVal>(get_byte_size()-1, make_ShadowV());
  }
  virtual List<PtrVal> to_bytes_shadow() {
    return List<PtrVal>{shared_from_this()} + make_ShadowV_seq(get_byte_size()-1);
  }
};

inline PtrVal make_FloatV(long double f) {
  auto ret = make_simple<FloatV>(f);
  return hashconsing(ret);
}

inline PtrVal make_FloatV(long double f, size_t bw) {
  auto ret = make_simple<FloatV>(f, bw);
  return hashconsing(ret);
}

inline PtrVal make_FloatV_fp80(std::array<unsigned char, 10> buf) {
  std::cout << "*(__float80*)&buf: " << *(__float80*)&buf << std::endl;
  return make_FloatV((long double)*(__float80*)&buf, 80);
}

inline long double proj_FloatV(PtrVal v) {
  return std::dynamic_pointer_cast<FloatV>(v)->f;
}

inline PtrVal ui_tofp(PtrVal v) {
  auto ui = std::dynamic_pointer_cast<IntV>(v);
  ASSERT(ui != nullptr, "value passed to ui_tofp is not an IntV");
  return make_FloatV(ui->i, ui->bw);
}

inline PtrVal fp_toui(PtrVal v, size_t bw) {
  auto fp = std::dynamic_pointer_cast<FloatV>(v);
  ASSERT(fp != nullptr, "value passed to fp_toui is not a FloatV");
  return make_IntV((uint64_t)fp->f, bw);
}

inline PtrVal fp_tosi(const PtrVal& v, size_t bw) {
  auto fp = std::dynamic_pointer_cast<FloatV>(v);
  ASSERT(fp != nullptr, "value passed to fp_tosi is not a FloatV");
  return make_IntV(fp->f, bw);
}

inline PtrVal si_tofp(const PtrVal& v) {
  auto si = std::dynamic_pointer_cast<IntV>(v);
  ASSERT(si != nullptr, "value passed to si_tofp is not an IntV");
  return make_FloatV(si->i, si->bw);
}

struct LocV : IntV {
  enum Kind { kStack, kHeap, kNative };
  static constexpr int64_t MemOffset[3] = { 1LL<<28, 2LL<<28, 3LL<<28 };
  Addr l;
  Kind k;
  size_t base, size;

  LocV(Addr base, Kind k, int size, int off) :
    IntV(MemOffset[k] + base + off, 64), l(base + off), k(k), base(base), size(size) {
    hash_combine(hash(), std::string("locv"));
    hash_combine(hash(), k);
    hash_combine(hash(), l);
    hash_combine(hash(), base);
    hash_combine(hash(), size);
  }
  LocV(const LocV& v) : LocV(v.base, v.k, v.size, v.l - v.base) {}

  std::string toString() const override {
    std::ostringstream ss;
    ss << "LocV(l:" << l << ", " << "base:" <<base <<", size:" << size <<", " << std::string(k == kStack ? "kStack" : "kHeap") << ")";
    return ss.str();
  }

  virtual bool compare(const Value* v) const override {
    auto that = static_cast<decltype(this)>(v);
    if (this->l != that->l) return false;
    if (this->k != that->k) return false;
    if (this->base != that->base) return false;
    return this->size == that->size;
  }
};

inline PtrVal make_LocV(Addr base, LocV::Kind k, size_t size, size_t off = 0) {
  auto ret = make_simple<LocV>(base, k, size, off);
  return hashconsing(ret);
}

// Todo: what should proj_LocV return?
inline unsigned int proj_LocV(const PtrVal& v) {
  return std::dynamic_pointer_cast<LocV>(v)->l;
}
inline LocV::Kind proj_LocV_kind(const PtrVal& v) {
  return std::dynamic_pointer_cast<LocV>(v)->k;
}
inline int proj_LocV_size(const PtrVal& v) {
  return std::dynamic_pointer_cast<LocV>(v)->size;
}

inline PtrVal make_LocV_null() {
  static const PtrVal loc0 = make_IntV(0, 64);
  return loc0;
}

// a null locv can be any IntV(0)
inline bool is_LocV_null(const PtrVal& v) {
  auto int_v = std::dynamic_pointer_cast<IntV>(v);
  ASSERT(int_v && 64 == int_v->bw, "Bad pointer");
  return (0 == int_v->i);
}

inline size_t get_pointer_realsize(const PtrVal& v) {
  auto loc = std::dynamic_pointer_cast<LocV>(v);
  ASSERT((loc->l >= loc->base) && (loc->l < (loc->base + loc->size)), "Out of bound pointer");
  size_t count = loc->size - (loc->l - loc->base);
  return count;
}

// FunV types:
//   use template to delay type instantiation
//   cause SS is currently incomplete, unable to use in containers

template <typename func_t>
struct FunV : LocV {
  func_t f;
  FunV(func_t f) : LocV(static_cast<Addr>(reinterpret_cast<intptr_t>(f)), LocV::kNative, 1, 0), f(f) {
    ASSERT(f != nullptr, "funv cannot be nullptr");
    hash_combine(hash(), std::string("funv"));
    hash_combine(hash(), f);
  }
  std::string toString() const override {
    std::ostringstream ss;
    ss << "FunV(" << f << ")";
    return ss.str();
  }
  virtual bool compare(const Value* v) const override {
    auto that = static_cast<decltype(this)>(v);
    return this->f == that->f;
  }
};

struct SymV : Value {
  String name;
  size_t bw;
  iOP rator;
  immer::array<PtrVal> rands;
  SymV(String name, size_t bw) : name(name), bw(bw) {
    hash_combine(hash(), std::string("symv1"));
    hash_combine(hash(), name);
    hash_combine(hash(), bw);
  }
  SymV(iOP rator, immer::array<PtrVal> rands, size_t bw) : rator(rator), rands(rands), bw(bw) {
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
  virtual bool is_conc() const override { return false; }
  virtual simple_ptr<IntV> to_IntV() override { return nullptr; }
  virtual size_t get_bw() const override { return bw; }

  virtual bool compare(const Value* v) const override {
    auto that = static_cast<decltype(this)>(v);
    if (this->bw != that->bw) return false;
    if (this->name != that->name) return false;
    if (this->rator != that->rator) return false;
    return this->rands == that->rands;
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

  static PtrVal simplify(iOP& rator, List<PtrVal>& rands, size_t& bw) {
    auto start = steady_clock::now();
    auto arg0 = std::dynamic_pointer_cast<SymV>(rands[0]);
    if (rator == iOP::op_neg && arg0) {
      switch (arg0->rator) {
        case iOP::op_neq: return make_simple<SymV>(iOP::op_eq, arg0->rands, bw);
        case iOP::op_eq:  return make_simple<SymV>(iOP::op_neq, arg0->rands, bw);
        case iOP::op_sle: return make_simple<SymV>(iOP::op_sgt, arg0->rands, bw);
        case iOP::op_slt: return make_simple<SymV>(iOP::op_sge, arg0->rands, bw);
        case iOP::op_sge: return make_simple<SymV>(iOP::op_slt, arg0->rands, bw);
        case iOP::op_sgt: return make_simple<SymV>(iOP::op_sle, arg0->rands, bw);
        case iOP::op_ule: return make_simple<SymV>(iOP::op_ugt, arg0->rands, bw);
        case iOP::op_ult: return make_simple<SymV>(iOP::op_uge, arg0->rands, bw);
        case iOP::op_uge: return make_simple<SymV>(iOP::op_ult, arg0->rands, bw);
        case iOP::op_ugt: return make_simple<SymV>(iOP::op_ule, arg0->rands, bw);
      }
    }
    auto end = steady_clock::now();
    return nullptr;
  }

  static PtrVal neg(const PtrVal& v);
};

/*
inline std::map<size_t, PtrVal> symv_cache;
  size_t h = 0;
  // compute hash
  auto res = symv_cache.find(h);
  if (res != symv_cache.end()) {
    return res->second;
  }
  auto v = ...
  symv_cache[v->hashval] = v;
*/

inline PtrVal make_SymV(const String& n) {
  auto ret = make_simple<SymV>(n, default_bw);
  return hashconsing(ret);
}

inline PtrVal make_SymV(String n, size_t bw) {
  auto ret = make_simple<SymV>(n, bw);
  return hashconsing(ret);
}

inline PtrVal make_SymV(iOP rator, immer::array<PtrVal> rands, size_t bw) {
  // auto s = SymV::simplify(rator, rands, bw);
  // if (s) {
  //   return s;
  // }
  auto ret = make_simple<SymV>(rator, std::move(rands), bw);
  return hashconsing(ret);
}

inline List<PtrVal> make_SymV_seq(unsigned length, const std::string& prefix, size_t bw) {
  immer::flex_vector_transient<PtrVal> res;
  for (auto i = 0; i < length; i++) {
    res.push_back(make_SymV(fresh(prefix), bw));
  }
  return res.persistent();
}

inline PtrVal SymV::neg(const PtrVal& v) {
  return make_SymV(iOP::op_neg, { v }, v->get_bw());
}

// XXX GW: just use bv_sext? seems not much difference?
// XXX GW: addr_index_bw vs addr_bw?
inline PtrVal addr_index_ext(const PtrVal& off) {
  ASSERT(off->get_bw() <= addr_index_bw, "Invalid offset");
  if (off->get_bw() == addr_index_bw) {
    return off;
  } else {
    // Todo: whether zext or sext?
    return bv_sext(off, addr_index_bw);
  }
}

inline PtrVal SymLocV_index(const int off) {
  ASSERT(off >= 0, "Bad off");
  return make_IntV(off, addr_index_bw);
}

struct SymLocV : SymV {
  PtrVal off;
  LocV::Kind k;
  size_t base, size;

  SymLocV(Addr base, LocV::Kind k, int size, PtrVal off) :
    SymV(iOP::op_add, { bv_sext(off, addr_bw), make_IntV((LocV::MemOffset[k] + base), addr_bw) }, addr_bw),
    off(addr_index_ext(off)), k(k), base(base), size(size) {
    hash_combine(hash(), std::string("symlocv"));
    hash_combine(hash(), std::hash<PtrVal>{}(off));
    hash_combine(hash(), k);
    hash_combine(hash(), base);
    hash_combine(hash(), size);
  }

  SymLocV(const SymLocV& v) : SymLocV(v.base, v.k, v.size, v.off) {}

  std::string toString() const override {
    std::ostringstream ss;
    ss << "LocV(off:" << *off << ", " << "base:" << base << ", size:" << size
       <<", " << std::string(k == LocV::Kind::kStack ? "kStack" : "kHeap") << ")";
    return ss.str();
  }

  virtual bool compare(const Value* v) const override {
    auto that = static_cast<decltype(this)>(v);
    if (this->off != that->off) return false;
    if (this->k != that->k) return false;
    if (this->base != that->base) return false;
    return this->size == that->size;
  }
};

inline PtrVal make_SymLocV(Addr base, LocV::Kind k, size_t size, PtrVal off) {
  auto ret = make_simple<SymLocV>(base, k, size, off);
  return hashconsing(ret);
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
  virtual bool is_conc() const override {
    ABORT("is_conc: unexpected value StructV.");
  }
  virtual simple_ptr<IntV> to_IntV() override { return nullptr; }
  virtual size_t get_bw() const override { ABORT("get_bw: unexpected value StructV."); }

  virtual bool compare(const Value* v) const override {
    auto that = static_cast<decltype(this)>(v);
    return this->fs == that->fs;
  }

  virtual List<PtrVal> to_bytes() { ABORT("???"); }
  virtual List<PtrVal> to_bytes_shadow() { ABORT("???"); }
};

inline PtrVal structV_at(const PtrVal& v, int idx) {
  auto sv = std::dynamic_pointer_cast<StructV>(v);
  if (sv) return (sv->fs).at(idx);
  ABORT("StructV_at: non StructV value");
}

// assume all values are signed, convert to unsigned if necessary
// require return value to be signed or non-negative
inline PtrVal int_op_2(iOP op, const PtrVal& v1, const PtrVal& v2) {
  auto i1 = v1->to_IntV();
  auto i2 = v2->to_IntV();
  auto bw1 = v1->get_bw();
  auto bw2 = v2->get_bw();
  if (bw1 != bw2) {
    std::cout << *v1 << " " << int_op2string(op) << " " << *v2 << "\n";
    ABORT("int_op_2: bitwidth of operands mismatch");
  }
  if (i1 && i2) {
    switch (op) {
      case iOP::op_add:
        return make_IntV(i1->i + i2->i, bw1, false);
      case iOP::op_sub:
        return make_IntV(i1->i - i2->i, bw1, false);
      case iOP::op_mul:
        return make_IntV(i1->i * i2->as_signed(), bw1, false);
      case iOP::op_sdiv:  // divide overflow is hardware exception
        return make_IntV(int64_t(i1->i) / int64_t(i2->i), bw1);
      case iOP::op_udiv:
        return make_IntV(uint64_t(i1->i) / uint64_t(i2->i), bw1);
      case iOP::op_srem:
        return make_IntV(int64_t(i1->i) % int64_t(i2->i), bw1, false);
      case iOP::op_urem:
        return make_IntV(uint64_t(i1->i) % uint64_t(i2->i), bw1, false);
      case iOP::op_eq:
        return make_IntV(i1->i == i2->i, 1);
      case iOP::op_neq:
        return make_IntV(i1->i != i2->i, 1);
      case iOP::op_uge:
        return make_IntV(uint64_t(i1->i) >= uint64_t(i2->i), 1);
      case iOP::op_sge:
        return make_IntV(int64_t(i1->i) >= int64_t(i2->i), 1);
      case iOP::op_ugt:
        return make_IntV(uint64_t(i1->i) > uint64_t(i2->i), 1);
      case iOP::op_sgt:
        return make_IntV(int64_t(i1->i) > int64_t(i2->i), 1);
      case iOP::op_ule:
        return make_IntV(uint64_t(i1->i) <= uint64_t(i2->i), 1);
      case iOP::op_sle:
        return make_IntV(int64_t(i1->i) <= int64_t(i2->i), 1);
      case iOP::op_ult:
        return make_IntV(uint64_t(i1->i) < uint64_t(i2->i), 1);
      case iOP::op_slt:
        return make_IntV(int64_t(i1->i) < int64_t(i2->i), 1);
      case iOP::op_and:
        return make_IntV(i1->i & i2->i, bw1, false);
      case iOP::op_or:
        return make_IntV(i1->i | i2->i, bw1, false);
      case iOP::op_xor:
        return make_IntV(i1->i ^ i2->i, bw1, false);
      case iOP::op_shl:
        return make_IntV(i1->i << i2->as_signed(), bw1, false);
      case iOP::op_ashr:
        return make_IntV(int64_t(i1->i) >> (i2->as_signed() + addr_bw - bw1), bw1);
      case iOP::op_lshr:
        return make_IntV(uint64_t(i1->i) >> (i2->as_signed() + addr_bw - bw1), bw1);
      default:
        std::cout << int_op2string(op) << std::endl;
        ABORT("invalid operator");
    }
  } else {
    ASSERT((i1 || std::dynamic_pointer_cast<SymV>(v1)) && (i2 || std::dynamic_pointer_cast<SymV>(v2)), "Invalid operand");
    auto bw = bw1;
    switch (op) {
      case iOP::op_eq:
      case iOP::op_neq:
      case iOP::op_uge:
      case iOP::op_sge:
      case iOP::op_ugt:
      case iOP::op_sgt:
      case iOP::op_ule:
      case iOP::op_sle:
      case iOP::op_ult:
      case iOP::op_slt:
        bw = 1;
      default:
        break;
    }
    return make_SymV(op, { v1, v2 }, bw);
  }
}

inline PtrVal float_op_2(fOP op, const PtrVal& v1, const PtrVal& v2) {
  auto f1 = std::dynamic_pointer_cast<FloatV>(v1);
  auto f2 = std::dynamic_pointer_cast<FloatV>(v2);
  auto bw1 = f1->get_bw();
  auto bw2 = f2->get_bw();
  if (bw1 != bw2) {
    std::cout << *v1 << " " << float_op2string(op) << " " << *v2 << "\n";
    ABORT("float_op_2: bitwidth of operands mismatch");
  }

  if (f1 && f2) {
    switch (op) {
      case fOP::op_fadd:   return make_FloatV(f1->f + f2->f, MAX(f1->bw, f2->bw));
      case fOP::op_fsub:   return make_FloatV(f1->f - f2->f, MAX(f1->bw, f2->bw));
      case fOP::op_fmul:   return make_FloatV(f1->f * f2->f, MAX(f1->bw, f2->bw));
      case fOP::op_fdiv:   return make_FloatV(f1->f / f2->f, MAX(f1->bw, f2->bw));
      case fOP::op_oeq:    return make_IntV(f1->f == f2->f, 1);
      case fOP::op_ogt:    return make_IntV(f1->f > f2->f, 1);
      case fOP::op_oge:    return make_IntV(f1->f >= f2->f, 1);
      case fOP::op_olt:    return make_IntV(f1->f < f2->f, 1);
      case fOP::op_ole:    return make_IntV(f1->f <= f2->f, 1);
      case fOP::op_one:    return make_IntV(f1->f != f2->f, 1);
      case fOP::op_ueq:    return make_IntV(f1->f == f2->f, 1);
      case fOP::op_ugt:    return make_IntV(f1->f > f2->f, 1);
      case fOP::op_uge:    return make_IntV(f1->f >= f2->f, 1);
      case fOP::op_ult:    return make_IntV(f1->f < f2->f, 1);
      case fOP::op_ule:    return make_IntV(f1->f <= f2->f, 1);
      case fOP::op_une:    return make_IntV(f1->f != f2->f, 1);
      /* TODO: QNAN <2022-03-10, David Deng> */
      case fOP::op_ord:    return make_IntV(1);
      case fOP::op_uno:    return make_IntV(0);
      case fOP::op_false:  return make_IntV(0, 1);
      case fOP::op_true:   return make_IntV(1, 1);
    }
    ABORT("Unknown float_op_2 operation");
  } else {
    ABORT("Non-concrete Float Detected");
  }
}

/* TODO: implement those two <2022-03-10, David Deng> */

inline PtrVal fp_ext(const PtrVal& v1, int from, int to) {
  auto f1 = std::dynamic_pointer_cast<FloatV>(v1);
  ASSERT((f1 != nullptr), "extending a non-FloatV value");
  return make_FloatV(f1->f, to);
}

inline PtrVal fp_trunc(const PtrVal& v1, int from, int to) {
  auto f1 = std::dynamic_pointer_cast<FloatV>(v1);
  ASSERT((f1 != nullptr), "truncating a non-FloatV value");
  /* TODO: support other bw values (e.g. fp80) <2022-03-10, David Deng> */
  switch (to) {
    case 32: return make_FloatV(float(f1->f), to);
    case 64: return make_FloatV(double(f1->f), to);
    default: return make_FloatV(f1->f, to);
  }
}

inline PtrVal ite(const PtrVal& cond, const PtrVal& v_t, const PtrVal& v_e) {
  ASSERT(1 == cond->get_bw(), "Non-boolean condition");
  ASSERT(v_t->get_bw() == v_e->get_bw(), "Inconsistent operand widths");
  auto cond_i = std::dynamic_pointer_cast<IntV>(cond);
  if (cond_i) {
    return cond_i->i ? v_t : v_e;
  }
  ASSERT(std::dynamic_pointer_cast<SymV>(cond), "Non-symbolic condition");
  return make_SymV(iOP::op_ite, { cond, v_t, v_e }, v_t->get_bw());
}

inline PtrVal bv_sext(const PtrVal& v, size_t bw) {
  ASSERT(v->get_bw() <= bw, "Extended to smaller bw");
  if (v->get_bw() == bw) {
    return v;
  }
  auto i1 = std::dynamic_pointer_cast<IntV>(v);
  if (i1) {
    return make_IntV(int64_t(i1->i) >> (bw - i1->bw), bw, false);
  }
  auto s1 = std::dynamic_pointer_cast<SymV>(v);
  if (s1) {
    // Note: instead of passing new bw as an operand
    // we override the original bw here
    return make_SymV(iOP::op_sext, { s1 }, bw);
  }
  ABORT("Sext an invalid value, exit");
}

inline PtrVal bv_zext(const PtrVal& v, size_t bw) {
  ASSERT(v->get_bw() <= bw, "Extended to smaller bw");
  if (v->get_bw() == bw) {
    return v;
  }
  auto i1 = std::dynamic_pointer_cast<IntV>(v);
  if (i1) {
    return make_IntV(uint64_t(i1->i) >> (bw - i1->bw), bw, false);
  }
  auto s1 = std::dynamic_pointer_cast<SymV>(v);
  if (s1) {
    // Note: instead of passing new bw as an operand
    // we override the original bw here
    return make_SymV(iOP::op_zext, { s1 }, bw);
  }
  ABORT("Zext an invalid value, exit");
}

inline PtrVal trunc(const PtrVal& v1, int from, int to) {
  auto i1 = std::dynamic_pointer_cast<IntV>(v1);
  if (i1) {
    return make_IntV(i1->i << (from - to), to, false);
  }
  auto s1 = std::dynamic_pointer_cast<SymV>(v1);
  if (s1) {
    return make_SymV(iOP::op_trunc, { v1 }, to);
  }
  ABORT("Truncate an invalid value, exit");
}

inline PtrVal bv_extract(const PtrVal& v1, int hi, int lo) {
  auto i1 = std::dynamic_pointer_cast<IntV>(v1);
  if (i1) {
    return make_IntV(i1->i >> (lo + addr_bw - i1->bw), hi - lo + 1);
  }
  auto s1 = std::dynamic_pointer_cast<SymV>(v1);
  if (s1) {
    return make_SymV(iOP::op_extract, { s1, make_IntV(hi), make_IntV(lo) }, hi - lo + 1);
  }
  ABORT("Extract an invalid value, exit");
}

inline PtrVal bv_concat(const PtrVal& v1, const PtrVal& v2) {
  auto i1 = v1->to_IntV();
  auto i2 = v2->to_IntV();
  auto bw1 = v1->get_bw();
  auto bw2 = v2->get_bw();
  assert(bw1 + bw2 <= addr_bw);
  if (i1 && i2) return make_IntV(i1->i | (uint64_t(i2->i) >> bw1), bw1 + bw2, false);
  ASSERT(!std::dynamic_pointer_cast<ShadowV>(v1) && !std::dynamic_pointer_cast<ShadowV>(v2),
         "Cannot concat ShadowV values");
  // XXX: also check LocV and FunV?
  return make_SymV(iOP::op_concat, { v1, v2 }, bw1 + bw2);
}

inline std::string ptrval_to_string(const PtrVal& ptr) {
  return std::string(ptr == nullptr ? "nullptr" : ptr->toString());
}

inline PtrVal operator+ (const PtrVal& lhs, const int& rhs) {
  if (auto loc = std::dynamic_pointer_cast<LocV>(lhs)) {
    return make_LocV(loc->base, loc->k, loc->size, loc->l - loc->base + rhs);
  }
  if (auto i = std::dynamic_pointer_cast<IntV>(lhs)) {
    return make_IntV(i->i + rhs, i->bw);
  }
  ABORT("Unknown application of operator+");
}

inline PtrVal operator+ (const PtrVal& lhs, const PtrVal& rhs) {
  auto int_rhs = std::dynamic_pointer_cast<IntV>(rhs);
  auto sym_rhs = std::dynamic_pointer_cast<SymV>(rhs);
  ASSERT(int_rhs || sym_rhs, "Invalid rhs");

  if (auto loc = std::dynamic_pointer_cast<LocV>(lhs)) {
    ASSERT(rhs->get_bw() == addr_index_bw, "Invalid index bitwidth");
    if (int_rhs) {
      return make_LocV(loc->base, loc->k, loc->size, loc->l - loc->base + int_rhs->as_signed());
    } else {
      auto new_off = int_op_2(iOP::op_add, sym_rhs, SymLocV_index(loc->l - loc->base));
      return make_SymLocV(loc->base, loc->k, loc->size, new_off);
    }
  }
  if (auto symloc = std::dynamic_pointer_cast<SymLocV>(lhs)) {
    auto off = std::dynamic_pointer_cast<SymV>(symloc->off);
    ASSERT(off && (off->get_bw() == addr_index_bw), "Invalid offset index");
    ASSERT(rhs->get_bw() == addr_index_bw, "Invalid index bitwidth");
    auto new_off = int_op_2(iOP::op_add, off, rhs);
    return make_SymLocV(symloc->base, symloc->k, symloc->size, new_off);
  }
  ABORT("Unknown application of operator+");
}

inline const PtrVal IntV0 = make_IntV(0, 64);

#endif
