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
#include <cstdio>
#include <cstdlib>
#include <getopt.h>

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
using Fd = int;
using status_t = unsigned short;

enum iOP {
  op_add, op_sub, op_mul, op_sdiv, op_udiv,
  op_eq, op_uge, op_ugt, op_ule, op_ult,
  op_sge, op_sgt, op_sle, op_slt, op_neq,
  op_shl, op_lshr, op_ashr, op_and, op_or, op_xor,
  op_urem, op_srem, op_neg, op_sext, op_trunc
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

struct IntV;

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
  virtual std::shared_ptr<IntV> to_IntV() const override { return std::make_shared<IntV>(i, bw); }
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

inline char proj_IntV_char(PtrVal v) {
  std::shared_ptr<IntV> intV = v->to_IntV();
  ASSERT(intV->get_bw() == 8, "proj_IntV_char: Bitwidth mismatch");
  return static_cast<char>(proj_IntV(intV));
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
  virtual std::shared_ptr<IntV> to_IntV() const override { return nullptr; }
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

  LocV(Addr l, Kind k, int size) : l(l), k(k), size(size) {}
  LocV(const LocV& v) { l = v.l; }
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
  virtual std::shared_ptr<IntV> to_IntV() const override { return nullptr; }
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
  //ASSERT(bw1 == bw2, "IntOp2: bitwidth of operands mismatch");
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
      return std::make_shared<SymV>(op_sext, immer::flex_vector({ e1 }), bw);
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
      return std::make_shared<SymV>(op_trunc,
        immer::flex_vector({ v1 }), to);
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

class PC {
  private:
    immer::set<SExpr> pc;
  public:
    PC(immer::set<SExpr> pc) : pc(pc) {}
    PC add(SExpr e) { return PC(pc.insert(e)); }
    PC addSet(immer::set<SExpr> new_pc) { return PC(Set::join(pc, new_pc)); }
    immer::set<SExpr> getPC() { return pc; }
    void print() { print_set(pc); }
};

/* TODO: Is the name field necessary? <2021-10-12, David Deng> */
class File {
  private:
    std::string name;
    immer::flex_vector<PtrVal> content;
  public:
    friend std::ostream& operator<<(std::ostream& os, const File& f) {
      os << "File(name=" << f.name << ", content=[" << std::endl;
      for (auto ptrval: f.content) {
        os << "\t" << *ptrval << "," << std::endl;
      }
      os << "])";
      return os;
    }
    File(std::string name): name(name) {}
    File(std::string name, immer::flex_vector<PtrVal> content): name(name), content(content) {}
    File(const File& f): name(f.name), content(f.content) {}

    // if writing beyond the last byte, will simply append to the end without filling
    void write_at_no_fill(immer::flex_vector<PtrVal> new_content, size_t pos) {
      content = content.take(pos) + new_content + content.drop(pos + new_content.size());
    }
    void write_at(immer::flex_vector<PtrVal> new_content, size_t pos, PtrVal fill_val) {
      int fill_size = pos - content.size();
      if (fill_size > 0) {
        // fill the new values to reflect the actual pos
        content = content + immer::flex_vector(fill_size, fill_val);
      }
      write_at_no_fill(new_content, pos);
    }
    void append(immer::flex_vector<PtrVal> new_content) {
      write_at_no_fill(new_content, content.size());
    }
    void clear() {
      content = immer::flex_vector<PtrVal>();
    }
    size_t get_size() const {
      return content.size();
    }
    std::string get_name() const {
      return name;
    }
    immer::flex_vector<PtrVal> read_at(size_t pos, size_t length) const {
      return content.drop(pos).take(length);
    }
    immer::flex_vector<PtrVal> get_content() const {
      return read_at(0, content.size());
    }
};

// return a symbolic file with size bytes
inline File make_SymFile(std::string name, size_t size) {
  immer::flex_vector<PtrVal> content;
  for (int i = 0; i < size; i++) {
    content = content.push_back(make_SymV(std::string("FILE_") + name + std::string("_BYTE_") + std::to_string(i)));
  }
  return File(name, content);
};

/* TODO: what is the rule about the lowest file descriptor guarantee?
 * How do we model that rule? Is there a usecase for that?
 * Use a particular data structure? <2021-10-12, David Deng> */

// An opened file
struct Stream {
  private:
    File file;
    int mode; // a combination of O_RDONLY, O_WRONLY, O_RDWR, etc.
    off_t cursor;
  public:
    Stream(const Stream &s):
      file(s.file),
      mode(s.mode),
      cursor(s.cursor) {}
    Stream(File file):
      file(file),
      mode(O_RDONLY),
      cursor(0) {}
    Stream(File file, int mode):
      file(file),
      mode(mode),
      cursor(0) {}
    Stream(File file, int mode, size_t cursor):
      file(file),
      mode(mode),
      cursor(cursor) {}

    off_t seek_start(off_t offset) {
      if (offset < 0) return -1;
      cursor = offset;
      return cursor;
    }
    off_t seek_end(off_t offset) {
      off_t new_cursor = file.get_size() + offset;
      if (new_cursor < 0) return -1;
      cursor = new_cursor;
      return cursor;
    }
    off_t seek_cur(off_t offset) {
      off_t new_cursor = cursor + offset;
      if (new_cursor < 0) return -1;
      cursor = new_cursor;
      return cursor;
    }
    size_t get_cursor() const { return cursor; }

    /* TODO: implement write, read
     * ssize_t write(const void *buf, size_t nbytes)
     * - write from the current cursor, update cursor
     * - support only concrete values
     * - can have another function write_sym to handle writing of symbolic values
     * pair<flex_vector<PtrVal>, ssize_t> read(size_t nbytes);
     * - read from the current cursor position, update cursor
     * <2021-11-15, David Deng> */
};

class FS {
  private:
    immer::map<Fd, Stream> opened_files;
    immer::map<std::string, File> files;
    Fd next_fd;

  public:
    FS(): next_fd(3) {
        // default initialize opened_files and files
        /* TODO: set up stdin and stdout using fd 1 and 2 <2021-11-03, David Deng> */
    }

    FS(const FS &fs):
      files(fs.files),
      opened_files(fs.opened_files),
      next_fd(3) {}

    FS(immer::map<Fd, Stream> opened_files,
        immer::map<std::string, File> files,
        status_t status,
        Fd next_fd,
        Fd last_opened_fd):
      opened_files(opened_files),
      files(files),
      next_fd(next_fd) {}

    /* Stream get_stream(Fd fd) { */
    /*   if (opened_files.find(fd) == nullptr) /1* Handle error here *1/ */
    /*     ASSERT(false, "cannot get stream that does not exist"); */
    /*   return opened_files.at(fd); */
    /* } */

    void add_file(File file) {
      ASSERT(!has_file(file.get_name()), "FS::add_file: File already exists");
      files = files.set(file.get_name(), file);
    }

    void remove_file(std::string name) {
      ASSERT(has_file(name), "FS::remove_file: File does not exist");
      files = files.erase(name);
    };

    inline bool has_file(std::string name) const {
      return files.find(name) != nullptr;
    }

    inline bool has_stream(Fd fd) const {
      return opened_files.find(fd) != nullptr;
    }

    Fd open_file(std::string name, int mode = O_RDONLY) {
      /* TODO: handle different mode <2021-10-12, David Deng> */
      if (!has_file(name)) return -1;
      opened_files = opened_files.set(next_fd, Stream(files.at(name)));
      return next_fd++;
    }

    int close_file(Fd fd) {
      /* TODO: set next_fd the lowest file descriptor? <2021-10-28, David Deng> */
      if (!has_stream(fd)) return -1;
      opened_files = opened_files.erase(fd);
      return 0;
    }

    /* TODO: implement read_file, write_file
     * what should the interface be? a simple wrapper around Stream's read and write?
     * <2021-11-15, David Deng> */
};

class SS {
  private:
    Mem heap;
    Stack stack;
    PC pc;
    BlockLabel bb;
    FS fs;
  public:
    SS(Mem heap, Stack stack, PC pc, BlockLabel bb) : heap(heap), stack(stack), pc(pc), bb(bb) {}
    SS(Mem heap, Stack stack, PC pc, BlockLabel bb, FS fs) : heap(heap), stack(stack), pc(pc), bb(bb), fs(fs) {}
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
      return std::make_shared<StructV>(heap.take(loc->l + size).drop(loc->l).getMem());
    }
    PtrVal heap_lookup(size_t addr) { return heap.at(addr); }
    BlockLabel incoming_block() { return bb; }
    SS alloc_stack(size_t size) { return SS(heap, stack.alloc(size), pc, bb); }
    SS alloc_heap(size_t size) { return SS(heap.alloc(size), stack, pc, bb); }
    SS update(PtrVal addr, PtrVal val) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack) return SS(heap, stack.update(loc->l, val), pc, bb);
      return SS(heap.update(loc->l, val), stack, pc, bb);
    }
    SS push() { return SS(heap, stack.push(), pc, bb); }
    SS pop(size_t keep) { return SS(heap, stack.pop(keep), pc, bb); }
    SS assign(Id id, PtrVal val) { return SS(heap, stack.assign(id, val), pc, bb); }
    SS assign_seq(immer::flex_vector<Id> ids, immer::flex_vector<PtrVal> vals) {
      return SS(heap, stack.assign_seq(ids, vals), pc, bb);
    }
    SS heap_append(immer::flex_vector<PtrVal> vals) {
      return SS(heap.append(vals), stack, pc, bb);
    }
    SS addPC(SExpr e) { return SS(heap, stack, pc.add(e), bb); }
    SS addPCSet(immer::set<SExpr> s) { return SS(heap, stack, pc.addSet(s), bb); }
    SS addIncomingBlock(BlockLabel blabel) { return SS(heap, stack, pc, blabel); }
    SS init_arg(int len) {
      ASSERT(stack.mem_size() == 0, "Stack Not New");
      // FIXME: ptr size magic
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
      return SS(heap, res_stack, pc, bb);
    }
    immer::set<SExpr> getPC() { return pc.getPC(); }
    // TODO temp solution
    PtrVal getVarargLoc() { return stack.getVarargLoc(); }
    void set_fs(FS& new_fs) { fs = new_fs; }
    FS& get_fs() { return fs; }
};

inline const Mem mt_mem = Mem(immer::flex_vector<PtrVal>{});
inline const Stack mt_stack = Stack(mt_mem, immer::flex_vector<Frame>{});
inline const PC mt_pc = PC(immer::set<SExpr>{});
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

struct ExprHandle: public std::shared_ptr<void> {
  typedef std::shared_ptr<void> Base;

  static void freeExpr(Expr e) {
    vc_DeleteExpr(e);
  }

  ExprHandle(Expr e): Base(e, freeExpr) {}
};

inline std::map<PtrVal, std::pair<ExprHandle, std::set<ExprHandle>>, PtrValCmp> stp_env;
inline ExprHandle construct_STP_expr_internal(VC, PtrVal, std::set<ExprHandle>&);

inline ExprHandle construct_STP_expr(VC vc, PtrVal e, std::set<ExprHandle> &vars) {
  // search expr cache
  if (use_global_solver) {
    auto it = stp_env.find(e);
    if (it != stp_env.end()) {
      auto &vars2 = it->second.second;
      vars.insert(vars2.begin(), vars2.end());
      return it->second.first;
    }
  }
  // query internal
  std::set<ExprHandle> vars2;
  auto ret = construct_STP_expr_internal(vc, e, vars2);
  vars.insert(vars2.begin(), vars2.end());
  // store expr cache
  if (use_global_solver) {
    stp_env.emplace(e, std::make_pair(ret, std::move(vars2)));
  }
  return ret;
}

inline ExprHandle construct_STP_expr_internal(VC vc, PtrVal e, std::set<ExprHandle> &vars) {
  auto int_e = std::dynamic_pointer_cast<IntV>(e);
  if (int_e) {
    return vc_bvConstExprFromLL(vc, int_e->bw, int_e->i);
  }
  auto sym_e = std::dynamic_pointer_cast<SymV>(e);
  if (!sym_e) ABORT("Non-symbolic/integer value in path condition");

  if (!sym_e->name.empty()) {
    auto name = sym_e->name;
    ExprHandle stp_expr = vc_varExpr(vc, name.c_str(), vc_bvType(vc, sym_e->bw));
    vars.insert(stp_expr);
    return stp_expr;
  }

  std::vector<ExprHandle> expr_rands;
  int bw = sym_e->bw;
  for (auto e : sym_e->rands) {
    expr_rands.push_back(construct_STP_expr(vc, e, vars));
  }
  switch (sym_e->rator) {
    case op_add:
      return vc_bvPlusExpr(vc, bw, expr_rands.at(0).get(), expr_rands.at(1).get());
    case op_sub:
      return vc_bvMinusExpr(vc, bw, expr_rands.at(0).get(), expr_rands.at(1).get());
    case op_mul:
      return vc_bvMultExpr(vc, bw, expr_rands.at(0).get(), expr_rands.at(1).get());
    case op_sdiv:
    case op_udiv:
      return vc_bvDivExpr(vc, bw, expr_rands.at(0).get(), expr_rands.at(1).get());
    case op_uge:
      return vc_bvGeExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case op_sge:
      return vc_sbvGeExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case op_ugt:
      return vc_bvGtExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case op_sgt:
      return vc_sbvGtExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case op_ule:
      return vc_bvLeExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case op_sle:
      return vc_sbvLeExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case op_ult:
      return vc_bvLtExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case op_slt:
      return vc_sbvLtExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case op_eq:
      return vc_eqExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case op_neq:
      return vc_notExpr(vc, vc_eqExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get()));
    case op_neg:
      return vc_notExpr(vc, expr_rands.at(0).get());
    case op_sext:
      return vc_bvSignExtend(vc, expr_rands.at(0).get(), bw);
    case op_shl:
      return vc_bvLeftShiftExprExpr(vc, bw, expr_rands.at(0).get(), expr_rands.at(1).get());
    case op_lshr:
      return vc_bvRightShiftExprExpr(vc, bw, expr_rands.at(0).get(), expr_rands.at(1).get());
    case op_ashr:
      return vc_bvSignedRightShiftExprExpr(vc, bw, expr_rands.at(0).get(), expr_rands.at(1).get());
    case op_and:
      return vc_bvAndExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case op_or:
      return vc_bvOrExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case op_xor:
      return vc_bvXorExpr(vc, expr_rands.at(0).get(), expr_rands.at(1).get());
    case op_urem:
      return vc_bvRemExpr(vc, bw, expr_rands.at(0).get(), expr_rands.at(1).get());
    case op_srem:
      return vc_sbvRemExpr(vc, bw, expr_rands.at(0).get(), expr_rands.at(1).get());
    case op_trunc:
      // bvExtract(vc, e, h, l) -> e[l:h+1]
      return vc_bvExtract(vc, expr_rands.at(0).get(), bw-1, 0);
    default: break;
  }
  ABORT("unkown operator when constructing STP expr");
}

using CacheKey = std::set<PtrVal, PtrValCmp>;
using CexType = std::map<ExprHandle, IntData>;
using CacheResult = std::pair<int, CexType>;
inline std::map<CacheKey, CacheResult> cache_map;
inline duration<double, std::micro> solver_time = std::chrono::microseconds::zero();

struct Checker {
  std::set<ExprHandle> variables;
  CexType *cex, cex2;
  VC vc;

  Checker() {
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

  int make_STP_query(immer::set<PtrVal> pc) {
    for (auto e: pc) {
      ExprHandle stp_expr = construct_STP_expr(vc, e, variables);
      vc_assertFormula(vc, stp_expr.get());
    }
    Expr fls = vc_falseExpr(vc);
    int result = vc_query(vc, fls);
    vc_DeleteExpr(fls);
    return result;
  }

  void get_STP_counterexample(CexType &cex) {
    for (auto expr: variables) {
      auto val = vc_getCounterExample(vc, expr.get());
      cex[expr] = getBVUnsignedLongLong(val);
      vc_DeleteExpr(val);
    }
  }

  int make_query(immer::set<PtrVal> pc) {
    CacheResult *result;
    if (use_global_solver) {
      auto ins = cache_map.emplace(CacheKey {pc.begin(), pc.end()}, CacheResult {});
      result = &(ins.first->second);
      cex = &(result->second);
      if (!ins.second)
        return result->first;
    }
    auto retcode = make_STP_query(pc);
    if (use_global_solver) {
      result->first = retcode;
      if (retcode == 0)
        get_STP_counterexample(*cex);
    }
    return retcode;
  }

  const CexType* get_counterexample() {
    if (use_global_solver) return cex;
    get_STP_counterexample(cex2);
    return &cex2;
  }
};

// returns true if it is sat, otherwise false
// XXX: should explore paths with timeout/no-answer cond?
inline bool check_pc(immer::set<PtrVal> pc) {
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

    auto cex = c.get_counterexample();
    for (auto &kv: *cex) {
      output << exprName(kv.first.get()) << " == " << kv.second << std::endl;
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
      std::cout << "#paths: " << num_paths;
      if (!ending) std::cout << "; ";
      if (ending) std::cout << std::endl;
      std::cout << std::flush;
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
      std::cout << "#threads: " << num_async + 1 << "; #async created: " << tt_num_async << "; " << std::flush;
      //std::cout << "current #async: " << pool.tasks_size() << " total #async: " << tt_num_async << "\n";
    }
    void print_query_num() {
      std::cout << "#queries: " << br_query_num << "/" << test_query_num << "\n" << std::flush;
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

inline bool exlib_failure_branch = false;

// XXX: can also specify symbolic argument here?
inline void handle_cli_args(int argc, char** argv) {
  int c;
  while (1) {
    static struct option long_options[] =
    {
      /* These options set a flag. */
      {"disable-solver",       no_argument, 0, 'd'},
      {"exlib-failure-branch", no_argument, 0, 'f'},
      {0,                      0,           0, 0}
    };
    int option_index = 0;

    c = getopt_long(argc, argv, "", long_options, &option_index);

    /* Detect the end of the options. */
    if (c == -1)
      break;

    switch (c)
    {
      case 0:
        break;
      case 'd':
        use_solver = false;
        break;
      case 'f':
        exlib_failure_branch = true;
        break;
      case '?':
        // parsing error, should be printed by getopt
      default:
        printf("usage: %s <#threads> [--disable-solver] [--exlib-failure-branch]\n", argv[0]);
        exit(-1);
    }

  }
  // FIXME: if #thread is not given, the program silently continues, which should not
  // parsing non-options
  if (optind < argc) {
    if (optind != argc - 1) {
      // more than one non-options
      printf("usage: %s <#threads> [--disable-solver] [--exlib-failure-branch]\n", argv[0]);
      exit(-1);
    }
    int t = std::stoi(argv[optind]);
    std::cout << t << "\n";
    if (t <= 0) {
      std::cout << "Invalid #threads, use 1 instead.\\n";
      MAX_ASYNC = 0;
    } else {
      MAX_ASYNC = t - 1;
    }
  }
  if (MAX_ASYNC == 0) {
    // It is safe the reuse the global_vc object within one thread, but not otherwise.
    use_global_solver = true;
  }
}

inline immer::flex_vector<std::pair<SS, PtrVal>>
sym_exec_br(SS ss, SExpr t_cond, SExpr f_cond,
            immer::flex_vector<std::pair<SS, PtrVal>> (*tf)(SS),
            immer::flex_vector<std::pair<SS, PtrVal>> (*ff)(SS)) {
  auto pc = ss.getPC();
  auto tbr_sat = check_pc(pc.insert(t_cond));
  auto fbr_sat = check_pc(pc.insert(f_cond));
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


#endif
