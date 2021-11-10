#ifndef LLSC_HEADERS
#define LLSC_HEADERS

#include <ostream>
#include <fstream>
#include <variant>
#include <string>
#include <vector>
#include <iostream>
#include <map>
#include <cstdint>
#include <thread>
#include <mutex>
#include <chrono>

#include <memory>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <future>

#include <unistd.h>
#include <fcntl.h>
#include <bits/stdc++.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <sys/resource.h>

#include <sai.hpp>
//#include <thread_pool.hpp>
#include <immer/flex_vector_transient.hpp>

#ifndef STR_SYMV
#include <stp/c_interface.h>
#include <stp_handle.hpp>
#endif

using namespace std::chrono;

inline unsigned int bitwidth = 32;
inline unsigned int addr_bw = 64;
inline unsigned int var_name = 0;

using BlockLabel = int;
using Id = int;
using Addr = unsigned int;
using IntData = long long int;

enum iOP {
  op_add, op_sub, op_mul, op_sdiv, op_udiv,
  op_eq, op_uge, op_ugt, op_ule, op_ult,
  op_sge, op_sgt, op_sle, op_slt, op_neq,
  op_shl, op_lshr, op_ashr, op_and, op_or, op_xor,
  op_urem, op_srem, op_neg, op_sext
};

inline std::string int_op2string(iOP op) {
  switch (op) {
    case op_add: return "+";
    case op_sub: return "-";
    case op_mul: return "*";
    case op_sdiv: return "s/";
    case op_udiv: return "u/";
    case op_eq:  return "=";
    case op_uge: return "u>=";
    case op_ugt: return "u>";
    case op_ule: return "u<=";
    case op_ult: return "u<";
    case op_sge: return "s>e";
    case op_sgt: return "s>";
    case op_sle: return "s<=";
    case op_slt: return "s<";
    case op_neq: return "!=";
    case op_shl: return "shl";
    case op_lshr: return "lshr";
    case op_ashr: return "ashr";
    case op_and: return "/\\";
    case op_or: return "\\/";
    case op_xor: return "xor";
    case op_urem: return "u%";
    case op_srem: return "s%";
    case op_neg: return "!";
    case op_sext: return "sext";
  }
  return "unknown op";
}

struct Value;
// lazy construction of all the SMT expressions
using SExpr = std::shared_ptr<Value>;
inline std::mutex vc_lock;
inline VC global_vc = vc_createValidityChecker();

/* Value representations */

using PtrVal = std::shared_ptr<Value>;

struct Value : public std::enable_shared_from_this<Value> {
  friend std::ostream& operator<<(std::ostream&os, const Value& v) {
    return v.toString(os);
  }
  virtual std::ostream& toString(std::ostream& os) const = 0;
  //TODO(GW): toSMTExpr vs toSMTBool?
  virtual SExpr to_SMTExpr() = 0;
  virtual SExpr to_SMTBool() = 0;
  virtual PtrVal to_IntV() const = 0;
  virtual bool is_conc() const = 0;
  virtual int get_bw() const = 0;
  virtual int compare(const Value *v) const = 0;
};

template<typename T>
struct Compare3Ways {
  int operator()(const T &a, const T &b) const {
    return a == b ? 0 : (a < b ? -1 : 1);
  }
};

template<typename T>
inline int compare_3ways(const T &a, const T &b) {
  return Compare3Ways<T>()(a, b);
}

#define COMPARE_3WAYS_RET(a, b) \
  do { \
    int ret = compare_3ways(a, b); \
    if (ret) return ret; \
  } while (0)

template<>
struct Compare3Ways<PtrVal> {
  int operator()(const PtrVal &a, const PtrVal &b) const {
    COMPARE_3WAYS_RET(std::type_index(typeid(*a.get())), std::type_index(typeid(*b.get())));
    return a->compare(b.get());
  }
};

template<template<typename> typename T>
struct Compare3Ways< T<PtrVal> > {
  int operator()(const T<PtrVal> &a, const T<PtrVal> &b) const {
    COMPARE_3WAYS_RET(a.size(), b.size());
    for (int i = 0; i < a.size(); i++)
      COMPARE_3WAYS_RET(a[i], b[i]);
    return 0;
  }
};

struct PtrValCmp {
  bool operator()(const PtrVal &lhs, const PtrVal &rhs) const {
    return compare_3ways(lhs, rhs) < 0;
  }
};

struct IntV : Value {
  int bw;
  IntData i;
  IntV(IntData i, int bw) : i(i), bw(bw) {}
  IntV(const IntV& v) { i = v.i; bw = v.bw; }
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "IntV(" << i << ")";
  }
  virtual SExpr to_SMTExpr() override {
    return shared_from_this();
  }
  virtual SExpr to_SMTBool() override {
    ABORT("to_SMTBool: unexpected value IntV.");
  }
  virtual PtrVal to_IntV() const override { return std::make_shared<IntV>(i, bw); }
  virtual bool is_conc() const override { return true; }
  virtual int get_bw() const override { return bw; }

  virtual int compare(const Value *v) const override {
    return compare_3ways(i, static_cast<decltype(this)>(v)->i);
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

struct FloatV : Value {
  float f;
  FloatV(float f) : f(f) {}
  FloatV(const FloatV& v) { f = v.f; }
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
  virtual PtrVal to_IntV() const override { return nullptr; }
  virtual int get_bw() const override { ABORT("get_bw: unexpected value FloatV."); }

  virtual int compare(const Value *v) const override {
    return compare_3ways(f, static_cast<decltype(this)>(v)->f);
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

  LocV(unsigned int l, Kind k, int size) : l(l), k(k), size(size) {}
  LocV(const LocV& v) { l = v.l; }
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "LocV(" << l << ")";
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
  virtual PtrVal to_IntV() const override { return std::make_shared<IntV>(l, addr_bw); }
  virtual int get_bw() const override { ABORT("get_bw: unexpected value LocV."); }

  virtual int compare(const Value *v) const override {
    auto that = static_cast<decltype(this)>(v);
    COMPARE_3WAYS_RET(this->k, that->k);
    return compare_3ways(this->l, that->l);
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

inline PtrVal make_LocV_inc(PtrVal loc, int i) {
  return make_LocV(proj_LocV(loc) + i, proj_LocV_kind(loc), proj_LocV_size(loc));
}

struct SymV : Value {
  String name;
  int bw;
  iOP rator;
  immer::flex_vector<PtrVal> rands;
  SymV(String name, int bw) : name(name), bw(bw) {}
  SymV(iOP rator, immer::flex_vector<PtrVal> rands, int bw) : rator(rator), rands(rands), bw(bw) {}
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
  virtual PtrVal to_IntV() const override { return nullptr; }
  virtual int get_bw() const override { return bw; }

  virtual int compare(const Value *v) const override {
    auto that = static_cast<decltype(this)>(v);
    int kind1 = this->name.empty(), kind2 = that->name.empty();
    COMPARE_3WAYS_RET(kind1, kind2);
    if (!kind1) {  // symbol
      return compare_3ways(this->name, that->name);
    }
    else {  // expression
      COMPARE_3WAYS_RET(this->bw, that->bw);
      COMPARE_3WAYS_RET(this->rator, that->rator);
      return compare_3ways(this->rands, that->rands);
    }
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
  StructV(immer::flex_vector<PtrVal> fs) : fs(fs) {}
  StructV(std::vector<PtrVal> fs) : fs(fs.begin(), fs.end()) {}
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
  virtual PtrVal to_IntV() const override { return nullptr; }
  virtual int get_bw() const override { ABORT("get_bw: unexpected value StructV."); }

  virtual int compare(const Value *v) const override {
    auto that = static_cast<decltype(this)>(v);
    return compare_3ways(this->fs, that->fs);
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
  // ASSERT(bw1 == bw2, "IntOp2: bitwidth of operands mismatch");
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
      return make_IntV(i1->i == i2->i, bw1);
    } else if (op == op_uge || op == op_sge) {
      return make_IntV(i1->i >= i2->i, bw1);
    } else if (op == op_ugt || op == op_sgt) {
      return make_IntV(i1->i > i2->i, bw1);
    } else if (op == op_ule || op == op_sle) {
      return make_IntV(i1->i <= i2->i, bw1);
    } else if (op == op_ult || op == op_slt) {
      return make_IntV(i1->i < i2->i, bw1);
    } else if (op == op_neq) {
      return make_IntV(i1->i != i2->i, bw1);
    } else if (op == op_urem || op == op_srem) {
      return make_IntV(i1->i % i2->i, bw1);
    } else if (op == op_and) {
      return make_IntV(i1->i & i2->i, bw1);
    } else if (op == op_or) {
      return make_IntV(i1->i | i2->i, bw1);
    } else if (op == op_xor) {
      return make_IntV(i1->i ^ i2->i, bw1);
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

enum fOP {
  op_fadd, op_fsub, op_fmul, op_fdiv
};

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
      return std::make_shared<SymV>(op_sext,
        immer::flex_vector({ e1 }), bw);
    } else {
      ABORT("Sext an invalid value, exit");
    }
  }
}

inline PtrVal trunc(PtrVal v1, int from, int to) {
  auto i1 = std::dynamic_pointer_cast<IntV>(v1);
  if (i1) {
    IntData i = i1->i;
    i = i << (from - to);
    i = i >> (from - to);
    return make_IntV(i, to);
  } else {
    auto s1 = std::dynamic_pointer_cast<SymV>(v1);
    if (s1) {
      // FIXME: Trunc
      ABORT("Truncate a LAZY_SYMV, needs work!");
    }
    ABORT("Truncate an invalid value, exit");
  }
}

/* Memory, stack, and symbolic state representation */

// Note (5/17): now using a byte-oriented layout
template <class V>
class PreMem {
  private:
    std::vector<V> mem;
  public:
    PreMem(std::vector<V> mem) : mem(std::move(mem)) {}
    size_t size() { return mem.size(); }
    V at(size_t idx) { return mem.at(idx); }
    PreMem&& update(size_t idx, V val) {
      mem.at(idx) = val;
      return std::move(*this);
    }
    PreMem&& append(V val) {
      mem.push_back(val);
      return std::move(*this);
    }
    PreMem&& append(V val, size_t padding) {
      size_t idx = mem.size();
      return alloc(padding + 1).update(idx, val);
    }
    PreMem&& append(const std::vector<V>& vs) {
      mem.insert(mem.end(), vs.begin(), vs.end());
      return std::move(*this);
    }
    PreMem&& alloc(size_t size) {
      mem.resize(mem.size() + size, nullptr);
      return std::move(*this);
    }
    PreMem&& take(size_t keep) {
      mem.resize(keep);
      return std::move(*this);
    }
    PreMem slice(size_t idx, size_t len) {
      auto off = mem.begin() + idx;
      return PreMem(std::vector(off, off + len));
    }
    // PreMem<V> drop(size_t d) { return PreMem<V>(mem.drop(d)); }
    const std::vector<V>& getMem() { return mem; }
};

using Mem = PreMem<PtrVal>;

class Frame {
  public:
    using Env = std::map<Id, PtrVal>;
  private:
    Env env;
  public:
    Frame(Env env) : env(std::move(env)) {}
    Frame() : env(std::map<Id, PtrVal>{}) {}
    size_t size() { return env.size(); }
    PtrVal lookup_id(Id id) const { return env.at(id); }
    Frame&& assign(Id id, PtrVal v) {
      env.insert_or_assign(id, v);
      return std::move(*this);
    }
    Frame&& assign_seq(const std::vector<Id>& ids, const std::vector<PtrVal>& vals) {
      for (size_t i = 0; i < ids.size(); i++) {
        env.insert_or_assign(ids.at(i), vals.at(i));
      }
      return std::move(*this);
    }
};

class Stack {
  private:
    Mem mem;
    std::vector<Frame> env;
  public:
    Stack(Mem mem, std::vector<Frame> env) : mem(std::move(mem)), env(std::move(env)) {}
    size_t mem_size() { return mem.size(); }
    size_t frame_depth() { return env.size(); }
    PtrVal getVarargLoc() { return env.at(env.size()-2).lookup_id(0); }
    Stack&& pop(size_t keep) {
      mem.take(keep);
      env.pop_back();
      return std::move(*this);
    }
    Stack&& push() {
      return push(Frame());
    }
    Stack&& push(Frame f) {
      env.push_back(std::move(f));
      return std::move(*this);
    }

    Stack&& assign(Id id, PtrVal val) {
      env.back().assign(id, val);
      return std::move(*this);
    }
    Stack&& assign_seq(const std::vector<Id>& ids, std::vector<PtrVal> vals) {
      // varargs
      size_t id_size = ids.size();
      if (id_size > 0) {
        if (ids.back() == 0) {
          auto msize = mem.size();
          for (size_t i = id_size - 1; i < vals.size(); i++) {
            // FIXME: magic value 8, as vararg is retrived from +8 address
            mem.append(vals.at(i), 7);
          }
          if (mem.size() == msize) mem.alloc(8);
          vals.resize(id_size - 1);
          vals.push_back(make_LocV(msize, LocV::kStack));
        }
        env.back().assign_seq(ids, vals);
      }
      return std::move(*this);
    }
    PtrVal lookup_id(Id id) { return env.back().lookup_id(id); }

    PtrVal at(size_t idx) { return mem.at(idx); }
    PtrVal at(size_t idx, int size) {
      return std::make_shared<StructV>(mem.slice(idx, size).getMem());
    }
    Stack&& update(size_t idx, PtrVal val) {
      mem.update(idx, val);
      return std::move(*this);
    }
    Stack&& alloc(size_t size) {
      mem.alloc(size);
      return std::move(*this);
    }
};

class PC {
  private:
    std::set<SExpr> pc;
  public:
    PC(std::set<SExpr> pc) : pc(std::move(pc)) {}
    PC&& add(SExpr e) {
      pc.insert(e);
      return std::move(*this);
    }
    PC&& addSet(const std::set<SExpr>& new_pc) {
      pc.insert(new_pc.begin(), new_pc.end());
      return std::move(*this);
    }
    const std::set<SExpr>& getPC() { return pc; }
    void print() { print_set(pc); }
};

class SS {
  private:
    Mem heap;
    Stack stack;
    PC pc;
    BlockLabel bb;
  public:
    SS(Mem heap, Stack stack, PC pc, BlockLabel bb) : heap(std::move(heap)), stack(std::move(stack)), pc(std::move(pc)), bb(bb) {}
    SS clone() {
      return *this;
    }
    PtrVal env_lookup(Id id) { return stack.lookup_id(id); }
    size_t heap_size() { return heap.size(); }
    size_t stack_size() { return stack.mem_size(); }
    size_t fresh_stack_addr() { return stack_size(); }
    size_t frame_depth() { return frame_depth(); }
    PtrVal at(PtrVal addr) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack) return stack.at(loc->l);
      return heap.at(loc->l);
    }
    PtrVal at(PtrVal addr, int size) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack) return stack.at(loc->l, size);
      return std::make_shared<StructV>(heap.slice(loc->l, size).getMem());
    }
    PtrVal heap_lookup(size_t addr) { return heap.at(addr); }
    BlockLabel incoming_block() { return bb; }
    SS&& alloc_stack(size_t size) {
      stack.alloc(size);
      return std::move(*this);
    }
    SS&& alloc_heap(size_t size) {
      heap.alloc(size);
      return std::move(*this);
    }
    SS&& update(PtrVal addr, PtrVal val) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack)
        stack.update(loc->l, val);
      else
        heap.update(loc->l, val);
      return std::move(*this);
    }
    SS&& push() {
      stack.push();
      return std::move(*this);
    }
    SS&& pop(size_t keep) {
      stack.pop(keep);
      return std::move(*this);
    }
    SS&& assign(Id id, PtrVal val) {
      stack.assign(id, val);
      return std::move(*this);
    }
    SS&& assign_seq(const std::vector<Id>& ids, std::vector<PtrVal> vals) {
      stack.assign_seq(ids, std::move(vals));
      return std::move(*this);
    }
    SS&& assign_seq(immer::flex_vector<Id> ids, immer::flex_vector<PtrVal> vals) {
      return assign_seq(
        std::vector<Id>(ids.begin(), ids.end()),
        std::vector<PtrVal>(vals.begin(), vals.end()));
    }
    SS&& heap_append(const std::vector<PtrVal>& vals) {
      heap.append(vals);
      return std::move(*this);
    }
    SS&& heap_append(immer::flex_vector<PtrVal> vals) {
      return heap_append(std::vector<PtrVal>(vals.begin(), vals.end()));
    }
    SS&& addPC(SExpr e) {
      pc.add(e);
      return std::move(*this);
    }
    SS&& addPCSet(const std::set<SExpr>& s) {
      pc.addSet(s);
      return std::move(*this);
    }
    SS&& addIncomingBlock(BlockLabel blabel) {
      bb = blabel;
      return std::move(*this);
    }
    SS&& init_arg(int len) {
      ASSERT(stack.mem_size() == 0, "Stack Not New");
      // FIXME: ptr size magic
      stack.alloc(17 + len + 1);
      stack.update(0, make_LocV(16, LocV::kStack));
      stack.update(8, make_LocV(17, LocV::kStack));
      stack.update(16, make_IntV(0));
      int arg_index = 17;
      for (int i = 0; i < len; i++) {
        stack.update(arg_index, make_SymV("ARG" + std::to_string(i)));
        arg_index++;
      }
      stack.update(arg_index, make_IntV(0));
      return std::move(*this);
    }
    const std::set<SExpr>& getPC() { return pc.getPC(); }
    // TODO temp solution
    PtrVal getVarargLoc() { return stack.getVarargLoc(); }
};

inline const Mem mt_mem = Mem(std::vector<PtrVal>{});
inline const Stack mt_stack = Stack(mt_mem, std::vector<Frame>{});
inline const PC mt_pc = PC(std::set<SExpr>{});
inline const BlockLabel mt_bb = 0;
inline const SS mt_ss = SS(mt_mem, mt_stack, mt_pc, mt_bb);

inline const immer::flex_vector<std::pair<SS, PtrVal>> mt_path_result =
  immer::flex_vector<std::pair<SS, PtrVal>>{};

/* Stack manipulation */

#define STACKSIZE_16MB (16 * 1024 * 1024)
#define STACKSIZE_32MB (32 * 1024 * 1024)
#define STACKSIZE_64MB (64 * 1024 * 1024)
#define STACKSIZE_128MB (128 * 1024 * 1024)
#define STACKSIZE_1GB (128 * 1024 * 1024 * 8)

inline void inc_stack(rlim_t lim) {
  struct rlimit rl;
  int result;

  result = getrlimit(RLIMIT_STACK, &rl);
  if (result == 0) {
    if (rl.rlim_cur < lim) {
      rl.rlim_cur = lim;
      result = setrlimit(RLIMIT_STACK, &rl);
      if (result != 0) {
        fprintf(stderr, "setrlimit returned result = %d\n", result);
      }
    }
  }
}

/* Async */

inline size_t MAX_ASYNC = 4;
inline std::mutex m;
inline std::atomic<unsigned int> num_async = 0;
inline std::atomic<unsigned int> tt_num_async = 0;
//inline thread_pool pool(4);

inline bool can_par() {
  return num_async < MAX_ASYNC;
}

template <typename F, typename... Ts>
inline auto really_async(F&& f, Ts&&... params) {
  return std::async(std::launch::async, std::forward<F>(f), std::forward<Ts>(params)...);
}

template<class T>
auto create_async(std::function<T()> f) -> std::future<T> {
  std::unique_lock<std::mutex> lk(m);
  num_async++;
  tt_num_async++;
  lk.unlock();

  std::future<T> fu = std::async(std::launch::async, [&]{
    T t = f();
    std::unique_lock<std::mutex> lk(m);
    num_async--;
    lk.unlock();
    return t;
  });
  return fu;
}

// STP interaction
inline bool use_solver = true;
inline bool use_global_solver = false;
inline unsigned int test_query_num = 0;
inline unsigned int br_query_num = 0;
inline std::map<std::string, Expr> stp_env;
using VarSet = std::set<std::shared_ptr<SymV>>;

inline Expr construct_STP_expr(VC vc, PtrVal e, VarSet &vars) {
  auto int_e = std::dynamic_pointer_cast<IntV>(e);
  if (int_e) {
    return vc_bvConstExprFromLL(vc, int_e->bw, int_e->i);
  }
  auto sym_e = std::dynamic_pointer_cast<SymV>(e);
  if (!sym_e) ABORT("Non-symbolic/integer value in path condition");

  if (!sym_e->name.empty()) {
    auto name = sym_e->name;
    Expr stp_expr = vc_varExpr(vc, name.c_str(), vc_bvType(vc, sym_e->bw));
    vars.insert(sym_e);
    return stp_expr;
  }

  std::vector<Expr> expr_rands;
  int bw = sym_e->bw;
  for (auto e : sym_e->rands) {
    expr_rands.push_back(construct_STP_expr(vc, e, vars));
  }
  Expr ret = nullptr;
  switch (sym_e->rator) {
    case op_add:
      ret = vc_bvPlusExpr(vc, bw, expr_rands.at(0),expr_rands.at(1));
      break;
    case op_sub:
      ret = vc_bvMinusExpr(vc, bw, expr_rands.at(0),expr_rands.at(1));
      break;
    case op_mul:
      ret = vc_bvMultExpr(vc, bw, expr_rands.at(0),expr_rands.at(1));
      break;
    case op_sdiv:
    case op_udiv:
      ret = vc_bvDivExpr(vc, bw, expr_rands.at(0),expr_rands.at(1));
      break;
    case op_uge:
      ret = vc_bvGeExpr(vc, expr_rands.at(0),expr_rands.at(1));
      break;
    case op_sge:
      ret = vc_sbvGeExpr(vc, expr_rands.at(0),expr_rands.at(1));
      break;
    case op_ugt:
      ret = vc_bvGtExpr(vc, expr_rands.at(0),expr_rands.at(1));
      break;
    case op_sgt:
      ret = vc_sbvGtExpr(vc, expr_rands.at(0),expr_rands.at(1));
      break;
    case op_ule:
      ret = vc_bvLeExpr(vc, expr_rands.at(0),expr_rands.at(1));
      break;
    case op_sle:
      ret = vc_sbvLeExpr(vc, expr_rands.at(0),expr_rands.at(1));
      break;
    case op_ult:
      ret = vc_bvLtExpr(vc, expr_rands.at(0),expr_rands.at(1));
      break;
    case op_slt:
      ret = vc_sbvLtExpr(vc, expr_rands.at(0),expr_rands.at(1));
      break;
    case op_eq:
      ret = vc_eqExpr(vc, expr_rands.at(0),expr_rands.at(1));
      break;
    case op_neq:
      ret = vc_notExpr(vc, vc_eqExpr(vc, expr_rands.at(0), expr_rands.at(1)));
      break;
    case op_neg:
      ret = vc_notExpr(vc, expr_rands.at(0));
      break;
    case op_sext:
      ret = vc_bvSignExtend(vc, expr_rands.at(0), bw);
      break;
    case op_shl:
    case op_lshr:
    case op_ashr:
    case op_and:
    case op_or:
    case op_xor:
    case op_urem:
    case op_srem:
    default: break;
  }
  if (ret) {
    for (auto e: expr_rands)
      vc_DeleteExpr(e);
    return ret;
  }
  ABORT("unkown operator when constructing STP expr");
}

inline VarSet construct_STP_constraints(VC vc, const std::set<PtrVal>& pc) {
  VarSet ret;
  for (auto e : pc) {
    Expr stp_expr = construct_STP_expr(vc, e, ret);
    vc_assertFormula(vc, stp_expr);
    vc_DeleteExpr(stp_expr);
    //vc_printExprFile(vc, e, out_fd);
    //std::string smt_rep = vc_printSMTLIB(vc, e);
    //int n = write(out_fd, smt_rep.c_str(), smt_rep.length());
    //    n = write(out_fd, "\n", 1);
  }
  return ret;
}

using CacheKey = std::set<PtrVal, PtrValCmp>;
using CexType = std::map<std::shared_ptr<SymV>, IntData>;
using CacheResult = std::pair<int, CexType>;
inline std::map<CacheKey, CacheResult> cache_map;
inline std::mutex cache_mutex;
inline duration<double, std::micro> solver_time = std::chrono::microseconds::zero();

struct Checker {
  VarSet variables;
  const CexType *cex;
  VC vc;

  Checker(): cex(nullptr) {
    if (use_global_solver) {
      vc = global_vc;
      vc_push(vc);
    }
    else {
      vc = vc_createValidityChecker();
    }
  }

  ~Checker() {
    if (use_global_solver) {
      vc_pop(vc);
    }
    else {
      vc_Destroy(vc);
    }
  }

  int make_STP_query(const std::set<PtrVal>& pc) {
    variables = construct_STP_constraints(vc, pc);
    Expr fls = vc_falseExpr(vc);
    int result = vc_query(vc, fls);
    vc_DeleteExpr(fls);
    return result;
  }

  void get_STP_counterexample(CexType &cex) {
    for (auto &var: variables) {
      auto expr = vc_varExpr(vc, var->name.c_str(), vc_bvType(vc, var->bw));
      auto val = vc_getCounterExample(vc, expr);
      cex[var] = getBVUnsignedLongLong(val);
      vc_DeleteExpr(expr);
      vc_DeleteExpr(val);
    }
  }

// #define NOCACHE
#ifdef NOCACHE
  CacheResult fakecache;

  int make_query(immer::set<PtrVal> pc) {
    fakecache.first = make_STP_query(pc);
    return fakecache.first;
  }

  const CexType* get_counterexample() {
    if (fakecache.first == 0)
      get_STP_counterexample(fakecache.second);
    return &(fakecache.second);
  }
#else
  int make_query(const std::set<PtrVal>& pc) {
    CacheKey key(pc.begin(), pc.end());
    std::pair<decltype(cache_map)::iterator, bool> entry;
    CacheResult *ret;
    {
      std::lock_guard cachelock(cache_mutex);
      entry = cache_map.emplace(key, CacheResult());
      ret = &(entry.first->second);
    }
    if (entry.second) {  // newly inserted
      ret->first = make_STP_query(pc);
      if (ret->first == 0)
        get_STP_counterexample(ret->second);
    }
    cex = &(ret->second);
    return ret->first;
  }

  const CexType* get_counterexample() { return cex; }
#endif  // NOCACHE
};

// returns true if it is sat, otherwise false
// XXX: should explore paths with timeout/no-answer cond?
inline bool check_pc(const std::set<PtrVal>& pc) {
  if (!use_solver) return true;
  auto start = steady_clock::now();
  br_query_num++;
  Checker c;
  auto result = c.make_query(pc);
  auto end = steady_clock::now();
  solver_time += duration_cast<microseconds>(end - start);
  return result == 0;
}

inline void check_pc_to_file(SS state) {
  if (!use_solver) {
    return;
  }
  auto start = steady_clock::now();
  Checker c;

  if (mkdir("tests", 0777) == -1) {
    if (errno == EEXIST) { }
    else {
      ABORT("Cannot create the folder tests, abort.\n");
    }
  }

  std::stringstream output;
  output << "Query number: " << (test_query_num+1) << std::endl;

  auto result = c.make_query(state.getPC());

  switch (result) {
  case 0:
    output << "Query is invalid" << std::endl;
    break;
  case 1:
    output << "Query is Valid" << std::endl;
    break;
  case 2:
    output << "Could not answer the query" << std::endl;
    break;
  case 3:
    output << "Timeout" << std::endl;
    break;
  }

  if (result == 0) {
    test_query_num++;
    std::stringstream filename;
    filename << "tests/" << test_query_num << ".test";
    int out_fd = open(filename.str().c_str(), O_RDWR | O_CREAT, 0777);
    if (out_fd == -1) {
        ABORT("Cannot create the test case file, abort.\n");
    }

    auto &cex = *c.get_counterexample();
    for (auto &kv: cex) {
      output << kv.first->name << " == " << kv.second << std::endl;
    }
    int n = write(out_fd, output.str().c_str(), output.str().size());
    // vc_printCounterExampleFile(vc, out_fd);
    close(out_fd);
  }
  auto end = steady_clock::now();
  solver_time += duration_cast<microseconds>(end - start);
}

/* Coverage information */

// TODO: branch coverage
// Some note on overhead: recording coverage 1m path/block exec poses ~2.5sec overhead.
struct CoverageMonitor {
  private:
    using BlockId = std::int64_t;
    // Total number of blocks
    std::uint64_t num_blocks;
    // The number of execution for each block
    std::vector<std::uint64_t> block_cov;
    // Number of discovered paths
    std::uint64_t num_paths;
    // Starting time
    steady_clock::time_point start;
    std::mutex bm;
    std::mutex pm;
  public:
    CoverageMonitor() : num_blocks(0), num_paths(0), start(steady_clock::now()) {}
    CoverageMonitor(std::uint64_t num_blocks) : num_blocks(num_blocks), num_paths(0), start(steady_clock::now()) {}
    void set_num_blocks(std::uint64_t n) {
      num_blocks = n;
      block_cov.resize(n, 0);
    }
    void inc_block(BlockId b) {
      std::unique_lock<std::mutex> lk(bm);
      block_cov[b]++;
    }
    void inc_path(size_t n) {
      std::unique_lock<std::mutex> lk(pm);
      num_paths += n;
    }
    void print_path_cov(bool ending = true) {
      std::cout << "#paths: " << num_paths << "; " << std::flush;
      if (ending) std::cout << std::endl;
    }
    void print_block_cov() {
      size_t covered = 0;
      for (auto v : block_cov) { if (v != 0) covered++; }
      std::cout << "#blocks: "
                << covered << "/"
                << num_blocks << "; "
                << std::flush;
    }
    void print_block_cov_detail() {
      print_block_cov();
      for (int i = 0; i < block_cov.size(); i++) {
        std::cout << "Block: " << i << "; "
                  << "visited: " << block_cov[i] << "\n"
                  << std::flush;
      }
    }
    void print_async() {
      std::cout << "#threads: " << num_async + 1 << "; #async created: " << tt_num_async << "; ";
      //std::cout << "current #async: " << pool.tasks_size() << " total #async: " << tt_num_async << "\n";
    }
    void print_query_num() {
      std::cout << "#queries: " << br_query_num << "/" << test_query_num << "\n";
    }
    void print_time() {
      steady_clock::time_point now = steady_clock::now();
      std::cout << "[" << (solver_time.count() / 1.0e6) << "s/"
                << (duration_cast<milliseconds>(now - start).count() / 1000.0) << "s] ";
    }
    void start_monitor() {
      std::thread([this]{
        while (this->block_cov.size() <= this->num_blocks) {
          print_time();
          print_block_cov();
          print_path_cov(false);
          print_async();
          print_query_num();
          std::this_thread::sleep_for(seconds(1));
        }
      }).detach();
    }
};

inline CoverageMonitor cov;

// XXX: can also specify symbolic argument here?
inline void handle_cli_args(int argc, char** argv) {
  if (argc < 2 || argc > 3) {
    printf("usage: %s <#threads> [--disable-solver]\n", argv[0]);
    exit(-1);
  }
  int t = std::stoi(argv[1]);
  if (t <= 0) {
    std::cout << "Invalid #threads, use 1 instead.\\n";
    MAX_ASYNC = 0;
  } else {
    MAX_ASYNC = t - 1;
  }
  if (MAX_ASYNC == 0) {
    // It is safe the reuse the global_vc object within one thread, but not otherwise.
    use_global_solver = true;
  }
  if (argc == 3 && std::string(argv[2]) == "--disable-solver") {
    use_solver = false;
  }
}

inline immer::flex_vector<std::pair<SS, PtrVal>>
sym_exec_br(SS& ss, SExpr t_cond, SExpr f_cond,
            immer::flex_vector<std::pair<SS, PtrVal>> (*tf)(SS),
            immer::flex_vector<std::pair<SS, PtrVal>> (*ff)(SS)) {
  auto pc = ss.getPC();
  auto ins = pc.insert(t_cond);
  auto tbr_sat = check_pc(pc);
  if (ins.second) pc.erase(ins.first);
  pc.insert(f_cond);
  auto fbr_sat = check_pc(pc);
  if (tbr_sat && fbr_sat) {
    cov.inc_path(1);
    SS tbr_ss = ss.clone().addPC(t_cond);
    SS fbr_ss = ss.addPC(f_cond);
    if (can_par()) {
      std::future<immer::flex_vector<std::pair<SS, PtrVal>>> tf_res =
        create_async<immer::flex_vector<std::pair<SS, PtrVal>>>([&]{
          return tf(std::move(tbr_ss));
        });
      auto ff_res = ff(std::move(fbr_ss));
      return tf_res.get() + ff_res;
    } else return tf(std::move(tbr_ss)) + ff(std::move(fbr_ss));
  } else if (tbr_sat) {
    SS tbr_ss = ss.addPC(t_cond);
    return tf(std::move(tbr_ss));
  } else if (fbr_sat) {
    SS fbr_ss = ss.addPC(f_cond);
    return ff(std::move(fbr_ss));
  } else {
    return immer::flex_vector<std::pair<SS, PtrVal>>{};
  }
}

inline immer::flex_vector<std::pair<SS, PtrVal>>
exec_br(SS ss, PtrVal cndVal,
        immer::flex_vector<std::pair<SS, PtrVal>> (*tf)(SS),
        immer::flex_vector<std::pair<SS, PtrVal>> (*ff)(SS)) {
  if (cndVal->is_conc()) {
    if (proj_IntV(cndVal) == 1) return tf(ss);
    return ff(ss);
  }
  return sym_exec_br(ss, cndVal->to_SMTBool(), to_SMTBoolNeg(cndVal), tf, ff);
}

#endif
