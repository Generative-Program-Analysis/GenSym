#ifndef LLSC_VALUE_OPS_HEADERS
#define LLSC_VALUE_OPS_HEADERS

struct Value;
struct IntV;
using SExpr = std::shared_ptr<Value>;
using PtrVal = std::shared_ptr<Value>;

/* Value representations */

struct Value : public std::enable_shared_from_this<Value> {
  friend std::ostream& operator<<(std::ostream&os, const Value& v) {
    return v.toString(os);
  }
  virtual std::ostream& toString(std::ostream& os) const = 0;
  //TODO(GW): toSMTExpr vs toSMTBool?
  virtual SExpr to_SMTExpr() = 0;
  virtual SExpr to_SMTBool() = 0;
  virtual std::shared_ptr<IntV> to_IntV() const = 0;
  virtual bool is_conc() const = 0;
  virtual int get_bw() const = 0;
  virtual bool compare(const Value *v) const = 0;

  size_t hashval;
  Value() : hashval(0) {}
  size_t& hash() { return hashval; }
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

struct IntV : Value {
  int bw;
  IntData i;
  IntV(IntData i, int bw) : i(i), bw(bw) {
    hash_combine(hash(), std::string("intv"));
    hash_combine(hash(), i);
    hash_combine(hash(), bw);
  }
  IntV(const IntV& v) : IntV(v.i, v.bw) {}
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "IntV(" << i << ")";
  }
  virtual SExpr to_SMTExpr() override {
    return shared_from_this();
  }
  virtual SExpr to_SMTBool() override {
    ABORT("to_SMTBool: unexpected value IntV.");
  }
  virtual std::shared_ptr<IntV> to_IntV() const override { return std::make_shared<IntV>(i, bw); }
  virtual bool is_conc() const override { return true; }
  virtual int get_bw() const override { return bw; }

  virtual bool compare(const Value *v) const override {
    auto that = static_cast<decltype(this)>(v);
    if (this->i != that->i) return false;
    return this->bw == that->bw;
  }
};

inline PtrVal make_IntV(IntData i) {
  return std::make_shared<IntV>(i, bitwidth);
}

inline PtrVal make_IntV(IntData i, int bw) {
  //FIXME, bit width
  return std::make_shared<IntV>(i, bw);
}

inline IntData proj_IntV(PtrVal v) {
  return std::dynamic_pointer_cast<IntV>(v)->i;
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
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "FloatV(" << f << ")";
  }
  virtual SExpr to_SMTExpr() override {
    ABORT("to_SMTExpr: unexpected value FloatV.");
  }
  virtual SExpr to_SMTBool() override {
    ABORT("to_SMTBool: unexpected value FloatV.");
  }
  virtual bool is_conc() const override { return true; }
  virtual std::shared_ptr<IntV> to_IntV() const override { return nullptr; }
  virtual int get_bw() const override { ABORT("get_bw: unexpected value FloatV."); }

  virtual bool compare(const Value *v) const override {
    auto that = static_cast<decltype(this)>(v);
    return this->f == that->f;
  }
};

inline PtrVal make_FloatV(float f) {
  return std::make_shared<FloatV>(f);
}

inline int proj_FloatV(PtrVal v) {
  return std::dynamic_pointer_cast<FloatV>(v)->f;
}

struct LocV : Value {
  enum Kind { kStack, kHeap };
  Addr l;
  Kind k;
  int size;

  LocV(Addr l, Kind k, int size) : l(l), k(k), size(size) {
    hash_combine(hash(), std::string("locv"));
    hash_combine(hash(), k);
    hash_combine(hash(), l);
  }
  LocV(const LocV& v) : LocV(v.l, v.k, v.size) {}
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "LocV(" << l << ", " << k << ")";
  }
  virtual SExpr to_SMTExpr() override {
    ABORT("to_SMTExpr: unexpected value LocV.");
  }
  virtual SExpr to_SMTBool() override {
    ABORT("to_SMTBool: unexpected value LocV.");
  }
  virtual bool is_conc() const override {
    ABORT("is_conc: unexpected value LocV.");
  }
  virtual std::shared_ptr<IntV> to_IntV() const override { return std::make_shared<IntV>(l, addr_bw); }
  virtual int get_bw() const override { return addr_bw; }

  virtual bool compare(const Value *v) const override {
    auto that = static_cast<decltype(this)>(v);
    if (this->l != that->l) return false;
    return this->k == that->k;
  }
};

inline PtrVal make_LocV(unsigned int i, LocV::Kind k, int size) {
  return std::make_shared<LocV>(i, k, size);
}

inline PtrVal make_LocV(unsigned int i, LocV::Kind k) {
  return std::make_shared<LocV>(i, k, -1);
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
inline bool is_LocV_null(PtrVal v) {
  return std::dynamic_pointer_cast<LocV>(v)->l == -1;
}

inline PtrVal make_LocV_inc(PtrVal loc, int i) {
  return make_LocV(proj_LocV(loc) + i, proj_LocV_kind(loc), proj_LocV_size(loc));
}

inline PtrVal make_LocV_null() {
  return make_LocV(-1, LocV::kHeap, -1);
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
  virtual std::ostream& toString(std::ostream& os) const override {
    if (!name.empty()) return os << "SymV(" << name << ")";
    os << "SymV(" << int_op2string(rator) << ", ";
    for (auto e : rands) {
      os << *e << ", ";
    }
    return os << ")";
  }
  virtual SExpr to_SMTExpr() override { return shared_from_this(); }
  virtual SExpr to_SMTBool() override { return shared_from_this(); }
  virtual bool is_conc() const override { return false; }
  virtual std::shared_ptr<IntV> to_IntV() const override { return nullptr; }
  virtual int get_bw() const override { return bw; }

  virtual bool compare(const Value *v) const override {
    auto that = static_cast<decltype(this)>(v);
    if (this->bw != that->bw) return false;
    if (this->name != that->name) return false;
    if (this->rator != that->rator) return false;
    return std::equal_to<decltype(rands)>{}(this->rands, that->rands);
  }
};

inline PtrVal make_SymV(String n) {
  return std::make_shared<SymV>(n, bitwidth);
}
inline PtrVal make_SymV(String n, int bw) {
  return std::make_shared<SymV>(n, bw);
}
inline SExpr to_SMTBoolNeg(PtrVal v) {
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
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "StructV(..)";
  }
  virtual SExpr to_SMTExpr() override {
    ABORT("to_SMTExpr: unexpected value StructV.");
  }
  virtual SExpr to_SMTBool() override {
    ABORT("to_SMTBool: unexpected value StructV.");
  }
  virtual bool is_conc() const override {
    ABORT("is_conc: unexpected value StructV.");
  }
  virtual std::shared_ptr<IntV> to_IntV() const override { return nullptr; }
  virtual int get_bw() const override { ABORT("get_bw: unexpected value StructV."); }

  virtual bool compare(const Value *v) const override {
    auto that = static_cast<decltype(this)>(v);
    return std::equal_to<decltype(fs)>{}(this->fs, that->fs);
  }
};

inline PtrVal structV_at(PtrVal v, int idx) {
  auto sv = std::dynamic_pointer_cast<StructV>(v);
  if (sv) return (sv->fs).at(idx);
  else ABORT("StructV_at: non StructV value");
}

inline PtrVal int_op_2(iOP op, PtrVal v1, PtrVal v2) {
  auto i1 = std::dynamic_pointer_cast<IntV>(v1->to_IntV());
  auto i2 = std::dynamic_pointer_cast<IntV>(v2->to_IntV());
  int bw1 = v1->get_bw();
  int bw2 = v2->get_bw();
  ASSERT(bw1 == bw2, "IntOp2: bitwidth of operands mismatch");
  if (i1 && i2) {
    if (op == op_add) {
      return make_IntV(i1->i + i2->i, bw1);
    } else if (op == op_sub) {
      return make_IntV(i1->i - i2->i, bw1);
    } else if (op == op_mul) {
      return make_IntV(i1->i * i2->i, bw1);
    // FIXME: singed and unsigned div
    } else if (op == op_sdiv || op == op_udiv) {
      return make_IntV(i1->i / i2->i, bw1);
    } else if (op == op_eq) {
      return make_IntV(i1->i == i2->i, 1);
    } else if (op == op_uge || op == op_sge) {
      return make_IntV(i1->i >= i2->i, 1);
    } else if (op == op_ugt || op == op_sgt) {
      return make_IntV(i1->i > i2->i, 1);
    } else if (op == op_ule || op == op_sle) {
      return make_IntV(i1->i <= i2->i, 1);
    } else if (op == op_ult || op == op_slt) {
      return make_IntV(i1->i < i2->i, 1);
    } else if (op == op_neq) {
      return make_IntV(i1->i != i2->i, 1);
    } else if (op == op_urem || op == op_srem) {
      return make_IntV(i1->i % i2->i, bw1);
    } else if (op == op_and) {
      return make_IntV(i1->i & i2->i, bw1);
    } else if (op == op_or) {
      return make_IntV(i1->i | i2->i, bw1);
    } else if (op == op_xor) {
      return make_IntV(i1->i ^ i2->i, bw1);
    } else if (op == op_ashr) {
      return make_IntV((i1->i >> i2->i), bw1);
    } else if (op == op_shl) {
      return make_IntV((i1->i << i2->i), bw1);
    } else {
      std::cout << op << std::endl;
      ABORT("invalid operator");
    }
  } else {
    SExpr e1 = v1->to_SMTExpr();
    SExpr e2 = v2->to_SMTExpr();
    return std::make_shared<SymV>(op, immer::flex_vector({ e1, e2 }), bw1);
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
    return make_IntV(i1->i, bw);
  } else {
    auto s1 = std::dynamic_pointer_cast<SymV>(v);
    if (s1) {
      // Note: instead of passing new bw as an operand
      // we override the original bw here
      SExpr e1 = s1->to_SMTExpr();
      return std::make_shared<SymV>(op_sext, immer::flex_vector({ e1 }), bw);
    } else {
      ABORT("Sext an invalid value, exit");
    }
  }
}

inline PtrVal bv_zext(PtrVal v, int bw) {
  ABORT("TODO");
}

inline PtrVal trunc(PtrVal v1, int from, int to) {
  auto i1 = std::dynamic_pointer_cast<IntV>(v1);
  if (i1) {
    IntData i = i1->i;
    i = i << (from - to);
    i = i >> (from - to);
    return make_IntV(i, to);
  }
  auto s1 = std::dynamic_pointer_cast<SymV>(v1);
  if (s1) {
    return std::make_shared<SymV>(op_trunc, immer::flex_vector({ v1 }), to);
  }
  ABORT("Truncate an invalid value, exit");
}

inline const PtrVal IntV0 = make_IntV(0);

#endif
