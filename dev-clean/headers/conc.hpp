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

union Data {
  int i;
  long long int l;
  short s;
  char c;
};

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
};

// Try union for int data
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
    return true;
  }
  virtual PtrVal to_IntV() const override { return std::make_shared<IntV>(l, addr_bw); }
  virtual int get_bw() const override { ABORT("get_bw: unexpected value LocV."); }
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


struct PairV : Value {
  PtrVal first, second;
  PairV(PtrVal first, PtrVal second) : first(first), second(second) {}
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "PairV(..)";
  }
  virtual SExpr to_SMTExpr() override {
    ABORT("to_SMTExpr: unexpected value PairV.");
  }
  virtual SExpr to_SMTBool() override {
    ABORT("to_SMTBool: unexpected value PairV.");
  }
  virtual bool is_conc() const override {
    ABORT("is_conc: unexpected value PairV.");
  }
  virtual PtrVal to_IntV() const override { return nullptr; }
  virtual int get_bw() const override { ABORT("get_bw: unexpected value PairV."); }
};

inline PtrVal first(PtrVal v) {
  return std::dynamic_pointer_cast<PairV>(v)->first;
}

inline PtrVal second(PtrVal v) {
  return std::dynamic_pointer_cast<PairV>(v)->second;
}

inline PtrVal make_PairV(PtrVal v1, PtrVal v2) {
  return std::make_shared<PairV>(v1, v2);
}

struct FailV : Value {
  FailV() {}
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "FailV(..)";
  }
  virtual SExpr to_SMTExpr() override {
    ABORT("to_SMTExpr: unexpected value FailV.");
  }
  virtual SExpr to_SMTBool() override {
    ABORT("to_SMTBool: unexpected value FailV.");
  }
  virtual bool is_conc() const override {
    return true;
  }
  virtual PtrVal to_IntV() const override {
    ABORT("to_IntV: unexpected value FailV.");
  }
  virtual int get_bw() const override { ABORT("get_bw: unexpected value FailV."); }
};


struct StructV : Value {
  immer::flex_vector<PtrVal> fs;
  StructV(immer::flex_vector<PtrVal> fs) : fs(fs) {}
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
     }else {
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
    immer::flex_vector<V> mem;
  public:
    PreMem(immer::flex_vector<V> mem) : mem(mem) {}
    size_t size() { return mem.size(); }
    V at(size_t idx) { return mem.at(idx); }
    PreMem<V> update(size_t idx, V val) { return PreMem<V>(mem.set(idx, val)); }
    PreMem<V> append(V val) { return PreMem<V>(mem.push_back(val)); }
    PreMem<V> append(V val, size_t padding) {
      size_t idx = mem.size();
      return PreMem<V>(alloc(padding + 1).update(idx, val)); 
    }
    PreMem<V> append(immer::flex_vector<V> vs) { return PreMem<V>(mem + vs); }
    PreMem<V> alloc(size_t size) {
      auto m = mem.transient();
      for (int i = 0; i < size; i++) { m.push_back(nullptr); }
      return PreMem<V>(m.persistent());
    }
    PreMem<V> take(size_t keep) { return PreMem<V>(mem.take(keep)); }
    PreMem<V> drop(size_t d) { return PreMem<V>(mem.drop(d)); }
    immer::flex_vector<V> getMem() { return mem; }
};

using Mem = PreMem<PtrVal>;

class Frame {
  public:
    using Env = immer::map<Id, PtrVal>;
  private:
    Env env;
  public:
    Frame(Env env) : env(env) {}
    Frame() : env(immer::map<Id, PtrVal>{}) {}
    size_t size() { return env.size(); }
    PtrVal lookup_id(Id id) const { return env.at(id); }
    Frame assign(Id id, PtrVal v) const { return Frame(env.insert({id, v})); }
    Frame assign_seq(immer::flex_vector<Id> ids, immer::flex_vector<PtrVal> vals) const {
      Env env1 = env;
      for (size_t i = 0; i < ids.size(); i++) {
        env1 = env1.insert({ids.at(i), vals.at(i)});
      }
      return Frame(env1);
    }
};

class Stack {
  private:
    Mem mem;
    immer::flex_vector<Frame> env;
  public:
    Stack(Mem mem, immer::flex_vector<Frame> env) : mem(mem), env(env) {}
    size_t mem_size() { return mem.size(); }
    size_t frame_depth() { return env.size(); }
    PtrVal getVarargLoc() { return env.at(env.size()-2).lookup_id(0); }
    Stack pop(size_t keep) { return Stack(mem.take(keep), env.take(env.size()-1)); }
    Stack push() { return Stack(mem, env.push_back(Frame())); }
    Stack push(Frame f) { return Stack(mem, env.push_back(f)); }

    Stack assign(Id id, PtrVal val) {
      return Stack(mem, env.update(env.size()-1, [&](auto f) { return f.assign(id, val); }));
    }
    Stack assign_seq(immer::flex_vector<Id> ids, immer::flex_vector<PtrVal> vals) {
      // varargs
      size_t id_size = ids.size();
      if (id_size == 0) return Stack(mem, env);
      if (ids.at(id_size - 1) == 0) {
        auto updated_mem = mem;
        for (size_t i = id_size - 1; i < vals.size(); i++) {
          // FIXME: magic value 8, as vararg is retrived from +8 address
          updated_mem = updated_mem.append(vals.at(i), 7);
        }
        if (updated_mem.size() == mem.size()) updated_mem = updated_mem.alloc(8);
        auto updated_vals = vals.take(id_size - 1).push_back(make_LocV(mem.size(), LocV::kStack));
        auto stack = Stack(updated_mem, env.update(env.size()-1, [&](auto f) { return f.assign_seq(ids, updated_vals); }));
        return Stack(updated_mem, env.update(env.size()-1, [&](auto f) { return f.assign_seq(ids, updated_vals); }));
      } else {
        return Stack(mem, env.update(env.size()-1, [&](auto f) { return f.assign_seq(ids, vals); }));
      }
    }
    PtrVal lookup_id(Id id) { return env.back().lookup_id(id); }

    PtrVal at(size_t idx) { return mem.at(idx); }
    PtrVal at(size_t idx, int size) {
      return std::make_shared<StructV>(mem.take(idx + size).drop(idx).getMem());
    }
    Stack update(size_t idx, PtrVal val) { return Stack(mem.update(idx, val), env); }
    Stack alloc(size_t size) { return Stack(mem.alloc(size), env); }
};

template <class V>
class SPreMem {
  private:
    immer::map<size_t, V> mem;
  public:
    SPreMem(immer::map<size_t, V> mem) : mem(mem) {}
    size_t size() { return mem.size(); }
    const V* find(size_t idx) { return mem.find(idx); }
    V at(size_t idx) { return mem.at(idx); }
    SPreMem<V> update(size_t idx, V val) { return SPreMem<V>(mem.insert({idx, val})); }
    // PreMem<V> append(V val) { return PreMem<V>(mem.push_back(val)); }
    // PreMem<V> append(V val, size_t padding) {
    //   size_t idx = mem.size();
    //   return PreMem<V>(alloc(padding + 1).update(idx, val)); 
    // }
    // PreMem<V> append(immer::flex_vector<V> vs) { return PreMem<V>(mem + vs); }
    // PreMem<V> alloc(size_t size) {
    //   auto m = mem.transient();
    //   for (int i = 0; i < size; i++) { m.push_back(nullptr); }
    //   return PreMem<V>(m.persistent());
    // }
    // PreMem<V> take(size_t keep) { return PreMem<V>(mem.take(keep)); }
    // PreMem<V> drop(size_t d) { return PreMem<V>(mem.drop(d)); }
    immer::flex_vector<V> getMem() { return mem; }
};

using SMem = SPreMem<PtrVal>;

class SFrame {
  public:
    using Env = immer::map<Id, PtrVal>;
  private:
    Env env;
  public:
    SFrame(Env env) : env(env) {}
    SFrame() : env(immer::map<Id, PtrVal>{}) {}
    size_t size() { return env.size(); }
    PtrVal lookup_id(Id id) const {
      if (env.find(id)) return env.at(id);
      else return std::make_shared<FailV>();
    }
    SFrame assign(Id id, PtrVal v) const { return SFrame(env.insert({id, v})); }
    SFrame assign_seq(immer::flex_vector<Id> ids, immer::flex_vector<PtrVal> vals) const {
      Env env1 = env;
      for (size_t i = 0; i < ids.size(); i++) {
        if (!vals.at(i)->is_conc())
          env1 = env1.insert({ids.at(i), vals.at(i)});
      }
      return SFrame(env1);
    }
};

// Note: do we really need FailV? 
// One other solution could be use IntV. And any non-symbolic
// value retreived from symbolic enviornment should be discarded
class SStack {
  private:
    SMem mem;
    immer::flex_vector<SFrame> env;
  public:
    SStack(SMem mem, immer::flex_vector<SFrame> env) : mem(mem), env(env) {}
    size_t mem_size() { return mem.size(); }
    size_t frame_depth() { return env.size(); }
    PtrVal getVarargLoc() { return env.at(env.size()-2).lookup_id(0); }
    // what should we do about smem?
    SStack pop(size_t keep) { return SStack(mem, env.take(env.size()-1)); }
    SStack push() { return SStack(mem, env.push_back(SFrame())); }
    SStack push(SFrame f) { return SStack(mem, env.push_back(f)); }

    SStack assign(Id id, PtrVal val) {
      return SStack(mem, env.update(env.size()-1, [&](auto f) { return f.assign(id, val); }));
    }
    SStack assign_seq(immer::flex_vector<Id> ids, immer::flex_vector<PtrVal> vals) {
      // varargs
      size_t id_size = ids.size();
      if (id_size == 0) return SStack(mem, env);
      if (ids.at(id_size - 1) == 0) {
        ABORT("Varargs in symbolic mode");
        // auto updated_mem = mem;
        // for (size_t i = id_size - 1; i < vals.size(); i++) {
        //   // FIXME: magic value 8, as vararg is retrived from +8 address
        //   updated_mem = updated_mem.append(vals.at(i), 7);
        // }
        // if (updated_mem.size() == mem.size()) updated_mem = updated_mem.alloc(8);
        // auto updated_vals = vals.take(id_size - 1).push_back(make_LocV(mem.size(), LocV::kStack));
        // auto stack = Stack(updated_mem, env.update(env.size()-1, [&](auto f) { return f.assign_seq(ids, updated_vals); }));
        // return Stack(updated_mem, env.update(env.size()-1, [&](auto f) { return f.assign_seq(ids, updated_vals); }));
      } else {
        return SStack(mem, env.update(env.size()-1, [&](auto f) { return f.assign_seq(ids, vals); }));
      }
    }
    PtrVal lookup_id(Id id) { return env.back().lookup_id(id); }

    PtrVal at(size_t idx) {
      if (mem.find(idx)) return mem.at(idx);
      else return std::make_shared<FailV>();
    }
    // How to handle structV?
    // PtrVal at(size_t idx, int size) {
    //   return std::make_shared<StructV>(mem.take(idx + size).drop(idx).getMem());
    // }
    SStack update(size_t idx, PtrVal val) { return SStack(mem.update(idx, val), env); }
    // SStack alloc(size_t size) { return SStack(mem.alloc(size), env); }
};

class PC {
  private:
    immer::flex_vector<SExpr> pc;
  public:
    PC(immer::flex_vector<SExpr> pc) : pc(pc) {}
    PC add(SExpr e) { return PC(pc.push_back(e)); }
    PC addSet(immer::flex_vector<SExpr> new_pc) { return PC(pc + new_pc); }
    immer::flex_vector<SExpr> getPC() { return pc; }
    void print() { print_vec(pc); }
};

class SS {
  private:
    Mem heap;
    Stack stack;
    SMem sheap;
    SStack sstack;
    PC pc;
    BlockLabel bb;
  public:
    SS(Mem heap, Stack stack, SMem sheap, SStack sstack, PC pc, BlockLabel bb)
      : heap(heap), stack(stack), sheap(sheap), sstack(sstack), pc(pc), bb(bb) {}
    PtrVal env_lookup(Id id) { return stack.lookup_id(id); }
    PtrVal senv_lookup(Id id) {
      auto temp_val = sstack.lookup_id(id);
      return (temp_val->is_conc()) ? stack.lookup_id(id) : temp_val;
    }
    size_t heap_size() { return heap.size(); }
    size_t stack_size() { return stack.mem_size(); }
    size_t sstack_size() { return sstack.mem_size(); }
    size_t fresh_stack_addr() { return stack_size(); }
    size_t frame_depth() { return frame_depth(); }
    PtrVal at(PtrVal addr) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack) return stack.at(loc->l);
      return heap.at(loc->l);
    }
    // if a size is specified, we want to retreive a struct value
    PtrVal at(PtrVal addr, int size) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack) return stack.at(loc->l, size);
      return std::make_shared<StructV>(heap.take(loc->l + size).drop(loc->l).getMem());
    }
    PtrVal sat(PtrVal addr) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack) {
        auto temp_sval = sstack.at(loc->l);
        return (temp_sval->is_conc()) ? stack.at(loc->l) : temp_sval;
      }
      auto temp_hval = sheap.at(loc->l);
      return (temp_hval->is_conc()) ? heap.at(loc->l) : temp_hval;
    }
    // PtrVal sat(PtrVal addr, int size) {
    //   auto loc = std::dynamic_pointer_cast<LocV>(addr);
    //   ASSERT(loc != nullptr, "Lookup an non-address value");
    //   if (loc->k == LocV::kStack) return sstack.at(loc->l, size);
    //   return std::make_shared<StructV>(sheap.take(loc->l + size).drop(loc->l).getMem());
    // }
    PtrVal heap_lookup(size_t addr) { return heap.at(addr); }
    BlockLabel incoming_block() { return bb; }
    // symbolic heap and stack does not grow with concrete ones
    SS alloc_stack(size_t size) { return SS(heap, stack.alloc(size), sheap, sstack, pc, bb); }
    // SS alloc_sstack(size_t size) { return SS(heap, stack, sheap, sstack.alloc(size), pc, bb); }
    SS alloc_heap(size_t size) { return SS(heap.alloc(size), stack, sheap, sstack, pc, bb); }
    SS update(PtrVal addr, PtrVal val) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack)
        return SS(heap, stack.update(loc->l, val), sheap, sstack, pc, bb);
      return SS(heap.update(loc->l, val), stack, sheap, sstack, pc, bb);
    }
    SS supdate(PtrVal addr, PtrVal val) {
      if (val->is_conc()) {
        return SS(heap, stack, sheap, sstack, pc, bb);
      }
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack)
        return SS(heap, stack, sheap, sstack.update(loc->l, val), pc, bb);
      return SS(heap, stack, sheap.update(loc->l, val), sstack, pc, bb);
    }
    // symbolic heap and stack should grow with concrete ones
    SS push() { return SS(heap, stack.push(), sheap, sstack.push(), pc, bb); }
    SS pop(size_t keep) { return SS(heap, stack.pop(keep), sheap, sstack.pop(keep), pc, bb); }
    SS assign(Id id, PtrVal val) { return SS(heap, stack.assign(id, val), sheap, sstack, pc, bb); }
    SS sassign(Id id, PtrVal val) {
      if (val->is_conc()) return SS(heap, stack, sheap, sstack, pc, bb);
      return SS(heap, stack, sheap, sstack.assign(id, val), pc, bb);
    }
    SS assign_seq(immer::flex_vector<Id> ids, immer::flex_vector<PtrVal> vals) {
      return SS(heap, stack.assign_seq(ids, vals), sheap, sstack, pc, bb);
    }
    SS sassign_seq(immer::flex_vector<Id> ids, immer::flex_vector<PtrVal> vals) {
      return SS(heap, stack, sheap, sstack.assign_seq(ids, vals), pc, bb);
    }
    SS heap_append(immer::flex_vector<PtrVal> vals) {
      return SS(heap.append(vals), stack, sheap, sstack, pc, bb);
    }
    SS addPC(SExpr e) { return SS(heap, stack, sheap, sstack, pc.add(e), bb); }
    SS addPCSet(immer::flex_vector<SExpr> s) { return SS(heap, stack, sheap, sstack, pc.addSet(s), bb); }
    SS addIncomingBlock(BlockLabel blabel) { return SS(heap, stack, sheap, sstack, pc, blabel); }
    // TODO: this function needs rework under concolic situation
    SS init_arg(int len) {
      ASSERT(stack.mem_size() == 0, "Stack Not New");
      // FIXME: ptr size magic
      // This magic function is mimic the behavior of commandline argument
      // it creates a symbolic command line arguments of length len
      auto res_stack = stack.alloc(17 + len + 1);
      res_stack = res_stack.update(0, make_LocV(16, LocV::kStack));
      res_stack = res_stack.update(8, make_LocV(17, LocV::kStack));
      res_stack = res_stack.update(16, make_IntV(0));
      int arg_index = 17;
      for (int i = 0; i < len; i++) {
        res_stack = res_stack.update(arg_index, make_SymV("ARG" + std::to_string(i)));
        arg_index++;
      }
      res_stack = res_stack.update(arg_index, make_IntV(0));
      return SS(heap, res_stack, sheap, sstack, pc, bb);
    }
    immer::flex_vector<SExpr> getPC() { return pc.getPC(); }
    PtrVal getVarargLoc() {return stack.getVarargLoc(); }
};

inline const Mem mt_mem = Mem(immer::flex_vector<PtrVal>{});
inline const Stack mt_stack = Stack(mt_mem, immer::flex_vector<Frame>{});
inline const SMem mt_smem = SMem(immer::map<size_t, PtrVal>{});
inline const SStack mt_sstack = SStack(mt_smem, immer::flex_vector<SFrame>{});
inline const PC mt_pc = PC(immer::flex_vector<SExpr>{});
inline const BlockLabel mt_bb = 0;
inline const SS mt_ss = SS(mt_mem, mt_stack, mt_smem, mt_sstack, mt_pc, mt_bb);

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
#define concolic_debug 1
inline bool use_solver = true;
inline bool use_global_solver = false;
inline unsigned int test_query_num = 0;
inline unsigned int br_query_num = 0;
inline std::map<std::string, Expr> stp_env;

inline Expr construct_STP_expr(VC vc, PtrVal e) {
  auto int_e = std::dynamic_pointer_cast<IntV>(e);
  if (int_e) {
    return vc_bvConstExprFromLL(vc, int_e->bw, int_e->i);
  }
  auto sym_e = std::dynamic_pointer_cast<SymV>(e);
  if (!sym_e) ABORT("Non-symbolic/integer value in path condition");

  // if (!sym_e->name.empty()) {
  //   auto name = sym_e->name;
  //   auto it = stp_env.find(name);
  //   if (it == stp_env.end()) {
  //     Expr stp_expr = vc_varExpr(vc, name.c_str(), vc_bvType(vc, sym_e->bw));
  //     stp_env.insert(std::make_pair(name, stp_expr));
  //     return stp_expr;
  //   }
  //   return it->second;
  // }

  if (!sym_e->name.empty()) {
    auto name = sym_e->name;
    Expr stp_expr = vc_varExpr(vc, name.c_str(), vc_bvType(vc, sym_e->bw));
    return stp_expr;
  }

  std::vector<Expr> expr_rands;
  int bw = sym_e->bw;
  for (auto e : sym_e->rands) {
    expr_rands.push_back(construct_STP_expr(vc, e));
  }
  switch (sym_e->rator) {
    case op_add:
      return vc_bvPlusExpr(vc, bw, expr_rands.at(0), expr_rands.at(1));
    case op_sub:
      return vc_bvMinusExpr(vc, bw, expr_rands.at(0), expr_rands.at(1));
    case op_mul:
      return vc_bvMultExpr(vc, bw, expr_rands.at(0), expr_rands.at(1));
    case op_sdiv:
    case op_udiv:
      return vc_bvDivExpr(vc, bw, expr_rands.at(0), expr_rands.at(1));
    case op_uge:
      return vc_bvGeExpr(vc, expr_rands.at(0), expr_rands.at(1));
    case op_sge:
      return vc_sbvGeExpr(vc, expr_rands.at(0), expr_rands.at(1));
    case op_ugt:
      return vc_bvGtExpr(vc, expr_rands.at(0), expr_rands.at(1));
    case op_sgt:
      return vc_sbvGtExpr(vc, expr_rands.at(0), expr_rands.at(1));
    case op_ule:
      return vc_bvLeExpr(vc, expr_rands.at(0), expr_rands.at(1));
    case op_sle:
      return vc_sbvLeExpr(vc, expr_rands.at(0), expr_rands.at(1));
    case op_ult:
      return vc_bvLtExpr(vc, expr_rands.at(0), expr_rands.at(1));
    case op_slt:
      return vc_sbvLtExpr(vc, expr_rands.at(0), expr_rands.at(1));
    case op_eq:
      return vc_eqExpr(vc, expr_rands.at(0), expr_rands.at(1));
    case op_neq:
      return vc_notExpr(vc, vc_eqExpr(vc, expr_rands.at(0), expr_rands.at(1)));
    case op_neg:
      return vc_notExpr(vc, expr_rands.at(0));
    case op_sext:
      return vc_bvSignExtend(vc, expr_rands.at(0), bw);
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
  ABORT("unkown operator when constructing STP expr");
}

inline void construct_STP_constraints(VC vc, immer::flex_vector<PtrVal> pc) {
  for (auto e : pc) {
    Expr stp_expr = construct_STP_expr(vc, e);
    vc_assertFormula(vc, stp_expr);
    //vc_printExprFile(vc, e, out_fd); 
    //std::string smt_rep = vc_printSMTLIB(vc, e);
    //int n = write(out_fd, smt_rep.c_str(), smt_rep.length());
    //    n = write(out_fd, "\n", 1);
  }
  if (concolic_debug) vc_printAsserts(vc, 0);
}

// returns true if it is sat, otherwise false
// XXX: should explore paths with timeout/no-answer cond?
inline bool check_pc(immer::flex_vector<PtrVal> pc) {
  if (!use_solver) return true;
  br_query_num++;
  int result = -1;
  VC vc;
  if (use_global_solver) {
    vc = global_vc;
    vc_push(vc);
  } else vc = vc_createValidityChecker();
  construct_STP_constraints(vc, pc);
  Expr fls = vc_falseExpr(vc);
  result = vc_query(vc, fls);
  if (use_global_solver) {
    vc_pop(vc);
  } else {
    vc_Destroy(vc);
  }
  return result == 0;
}

// Note this function does not restore the state of VC
inline std::pair<bool, VC> check_pc_with_vc(immer::flex_vector<PtrVal> pc, VC vc) {
  if (!use_solver) return std::make_pair(false, vc);
  br_query_num++;
  int result = -1;
  if (use_global_solver) {
    vc = global_vc;
    vc_push(vc);
  } else vc = vc_createValidityChecker();
  construct_STP_constraints(vc, pc);
  Expr fls = vc_falseExpr(vc);
  result = vc_query(vc, fls);
  return std::make_pair(result == 0, vc);
}

inline void check_pc_to_file(SS state) {
  if (!use_solver) {
    return;
  }
  VC vc;
  if (use_global_solver) {
    vc = global_vc;
    vc_push(vc);
  } else vc = vc_createValidityChecker();

  if (mkdir("tests", 0777) == -1) {
    if (errno == EEXIST) { }
    else {
      ABORT("Cannot create the folder tests, abort.\n");
    }
  }

  std::stringstream output;
  output << "Query number: " << (test_query_num+1) << std::endl;
  
  construct_STP_constraints(vc, state.getPC());
  Expr fls = vc_falseExpr(vc);
  int result = vc_query(vc, fls);

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

    int n = write(out_fd, output.str().c_str(), output.str().size());
    vc_printCounterExampleFile(vc, out_fd);
    close(out_fd);
  }
  if (use_global_solver) {
    vc_pop(vc);
  } else {
    vc_Destroy(vc);
  }
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
    void print_path_cov() {
      std::cout << "#paths: " << num_paths << "; " << std::flush;
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
      std::cout << "[" << (duration_cast<milliseconds>(now - start).count() / 1000.0) << " s] ";
    }
    void start_monitor() {
      std::thread([this]{
        while (this->block_cov.size() <= this->num_blocks) {
        if (!concolic_debug) {
          print_time();
          print_block_cov();
          print_path_cov();
          print_async();
          print_query_num();
        }
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
    std::cout << "Invalid #threads, use 1 instead.\n";
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
sym_exec_br(SS ss, SExpr t_cond, SExpr f_cond,
            immer::flex_vector<std::pair<SS, PtrVal>> (*tf)(SS),
            immer::flex_vector<std::pair<SS, PtrVal>> (*ff)(SS)) {
  auto pc = ss.getPC();
  auto tbr_sat = check_pc(pc.push_back(t_cond));
  auto fbr_sat = check_pc(pc.push_back(f_cond));
  if (tbr_sat && fbr_sat) {
    cov.inc_path(1);
    SS tbr_ss = ss.addPC(t_cond);
    SS fbr_ss = ss.addPC(f_cond);
    if (can_par()) {
      std::future<immer::flex_vector<std::pair<SS, PtrVal>>> tf_res =
        create_async<immer::flex_vector<std::pair<SS, PtrVal>>>([&]{
          return tf(tbr_ss);
        });
      auto ff_res = ff(fbr_ss);
      return tf_res.get() + ff_res;
    } else return tf(tbr_ss) + ff(fbr_ss);
  } else if (tbr_sat) {
    SS tbr_ss = ss.addPC(t_cond);
    return tf(tbr_ss);
  } else if (fbr_sat) {
    SS fbr_ss = ss.addPC(f_cond);
    return ff(fbr_ss);
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

/* Concolic Worker */

using Input = immer::flex_vector<PtrVal>;
using CInput = std::tuple<int, Input, Input>;

inline immer::flex_vector<SExpr> conc_exec(Input, Input);

// TODO: Optimization: use option istead of flex_vector
inline immer::flex_vector<CInput> gen_new_input(Input args, Input sargs,
  immer::flex_vector<SExpr> pc, int bound) {
  VC vc;
  // stp_env.clear();
  auto [isSat, newvc] = check_pc_with_vc(pc, vc);
  if (isSat) {
    ASSERT(args.size() == sargs.size(), "argument size not equal");
    auto wc = vc_getWholeCounterExample(newvc);
    if (concolic_debug) vc_printCounterExample(newvc);
    for (int i = 0; i < sargs.size(); i++) {
      if (!sargs[i]->is_conc()) {
        Expr recon_arg = vc_simplify(newvc, construct_STP_expr(newvc, sargs[i]));
        Expr new_arg_expr = vc_getTermFromCounterExample(newvc, recon_arg, wc);
        // Note: why getBVInt? How to get long.
        // There are get unsigned long long in stp interface?
        int new_arg_int = getBVInt(new_arg_expr);
        auto new_arg = make_IntV(new_arg_int, sargs[i]->get_bw());
        args = args.set(i, new_arg);
      }
      // if (concolic_debug) print_vec(args);
    }
    if (use_global_solver) {
      vc_pop(newvc);
    } else {
      vc_Destroy(newvc);
    }
    return immer::flex_vector<CInput>{std::make_tuple(bound, args, sargs)};
  } else {
    if (newvc) {
      if (use_global_solver) {
        vc_pop(newvc);
      } else {
        vc_Destroy(newvc);
      }
    }
    return immer::flex_vector<CInput>{};
  }
}

inline immer::flex_vector<CInput> expand_exec(CInput input) {
  auto [bound, args, sargs] = input;
  if (concolic_debug) {
    for (int i = 0; i < args.size(); i++) {
      std::cout << std::dynamic_pointer_cast<IntV>(args[i])->i << " ";
    }
    std::cout << std::endl;
  }
  immer::flex_vector<SExpr> this_pc = conc_exec(args, sargs);
  immer::flex_vector<CInput> new_inputs = immer::flex_vector<CInput>{};
  for (int i = bound; i < this_pc.size(); i++) {
    auto pc_prime = this_pc.take(i).push_back(to_SMTBoolNeg(this_pc[i]));
    new_inputs = new_inputs + gen_new_input(args, sargs, pc_prime, i + 1);
  }
  return new_inputs;
}

inline void conc_main(Input args, Input sargs) {
  auto work_list = immer::flex_vector<CInput>{std::make_tuple(0, args, sargs)};
  auto result_inputs = immer::flex_vector<CInput>{std::make_tuple(0, args, sargs)};
  while (!work_list.empty()) {
    CInput input = work_list.front();
    work_list = work_list.drop(1);
    immer::flex_vector<CInput> child_input = expand_exec(input);
    work_list = work_list + child_input;
    result_inputs = result_inputs + child_input;
  }
  std::cout << "Generated " <<result_inputs.size() << " inputs"<< std::endl;
}

#endif
