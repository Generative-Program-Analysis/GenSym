#include <ostream>
#include <variant>
#include <string>
#include <vector>
#include <iostream>
#include <map>

#include <sai.hpp>
#include <stp/c_interface.h>
#include <stp_handle.hpp>

#ifndef LLSC_HEADERS
#define LLSC_HEADERS

static VC vc = vc_createValidityChecker();
static unsigned int bitwidth = 32;
static unsigned int var_name = 0;

using Id = int;
using Addr = unsigned int;

struct Value {
  friend std::ostream& operator<<(std::ostream&os, const Value& v) {
    return v.toString(os);
  }
  virtual std::ostream& toString(std::ostream& os) const = 0;
  //TODO(GW): toSMTExpr vs toSMTBool?
  virtual Expr to_SMTExpr() const = 0;
  virtual Expr to_SMTBool() const = 0;
  virtual bool is_conc() const = 0;
};

struct IntV : Value {
  int i;
  IntV(int i) : i(i) {}
  IntV(const IntV& v) { i = v.i; }
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "IntV(" << i << ")";
  }
  virtual Expr to_SMTExpr() const override {
    return vc_bvConstExprFromInt(vc, 32, i);
  }
  virtual Expr to_SMTBool() const override {
    if (i) return vc_trueExpr(vc);
    else return vc_falseExpr(vc);
  }
  virtual bool is_conc() const override { return true; }
};

inline Ptr<Value> make_IntV(int i) {
  return std::make_shared<IntV>(i);
}

inline Ptr<Value> make_IntV(int i, int bw) {
  //FIXME, bit width
  return std::make_shared<IntV>(i);
}

inline int proj_IntV(Ptr<Value> v) {
  return std::dynamic_pointer_cast<IntV>(v)->i;
}

struct LocV : Value {
  enum Kind { kStack, kHeap };
  Addr l;
  Kind k;

  LocV(unsigned int l, Kind k) : l(l), k(k) {}
  LocV(const LocV& v) { l = v.l; }
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "LocV(" << l << ")";
  }
  virtual Expr to_SMTExpr() const override {
    ABORT("to_SMTExpr: unexpected value LocV.");
  }
  virtual Expr to_SMTBool() const override {
    ABORT("to_SMTBool: unexpected value LocV.");
  }
  virtual bool is_conc() const override {
    ABORT("is_conc: unexpected value LocV.");
  }
};

inline Ptr<Value> make_LocV(unsigned int i, LocV::Kind k) {
  return std::make_shared<LocV>(i, k);
}

inline unsigned int proj_LocV(Ptr<Value> v) {
  return std::dynamic_pointer_cast<LocV>(v)->l;
}
LocV::Kind proj_LocV_kind(Ptr<Value> v) {
  return std::dynamic_pointer_cast<LocV>(v)->k;
}
inline LocV::Kind kStack() { return LocV::kStack; }
inline LocV::Kind kHeap() { return LocV::kHeap; }

struct SymV : Value {
  Expr v;
  SymV(Expr v) : v(v) {}
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "SymV(" << v << ")";
  }
  virtual Expr to_SMTExpr() const override { return v; }
  // TODO(GW): how do we know this is a bool?
  virtual Expr to_SMTBool() const override { return v; }
  virtual bool is_conc() const override { return false; }
};

inline Ptr<Value> make_SymV(String n) {
  // TODO type, width
  return std::make_shared<SymV>(vc_varExpr(vc, n.c_str(), vc_bv32Type(vc)));
}

enum kOP {
  op_plus, op_minus, op_mult, op_div,
  op_eq, op_ge, op_gt, op_le, op_lt, op_neq,
};

Ptr<Value> op_2(kOP op, Ptr<Value> v1, Ptr<Value> v2) {
  auto i1 = std::dynamic_pointer_cast<IntV>(v1);
  auto i2 = std::dynamic_pointer_cast<IntV>(v2);

  if (i1 && i2) {
    if (op == op_plus) {
      return make_IntV(i1->i + i2->i);
    } else if (op == op_minus) {
      return make_IntV(i1->i - i2->i);
    } else if (op == op_mult) {
      return make_IntV(i1->i * i2->i);
    } else if (op == op_div) {
      return make_IntV(i1->i / i2->i);
    } else if (op == op_eq) {
      return make_IntV(i1->i == i2->i);
    } else if (op == op_ge) {
      return make_IntV(i1->i >= i2->i);
    } else if (op == op_gt) {
      return make_IntV(i1->i > i2->i);
    } else if (op == op_le) {
      return make_IntV(i1->i <= i2->i);
    } else if (op == op_lt) {
      return make_IntV(i1->i < i2->i);
    } else if (op == op_neq) {
      return make_IntV(i1->i != i2->i);
    } else {
      ABORT("invalid operator");
    }
  } else {
    Expr e1 = v1->to_SMTExpr();
    Expr e2 = v2->to_SMTExpr();
    if (op == op_plus) {
      return std::make_shared<SymV>(vc_bv32PlusExpr(vc, e1, e2));
    } else if (op == op_minus) {
      return std::make_shared<SymV>(vc_bv32MinusExpr(vc, e1, e2));
    } else if (op == op_mult) {
      return std::make_shared<SymV>(vc_bv32MultExpr(vc, e1, e2));
    } else if (op == op_div) {
      return std::make_shared<SymV>(vc_bvDivExpr(vc, 32, e1, e2));
    } else if (op == op_eq) {
      return std::make_shared<SymV>(vc_eqExpr(vc, e1, e2));
    } else if (op == op_ge) {
      return std::make_shared<SymV>(vc_bvGeExpr(vc, e1, e2));
    } else if (op == op_gt) {
      return std::make_shared<SymV>(vc_bvGtExpr(vc, e1, e2));
    } else if (op == op_le) {
      return std::make_shared<SymV>(vc_sbvLeExpr(vc, e1, e2));
    } else if (op == op_lt) {
      return std::make_shared<SymV>(vc_sbvLtExpr(vc, e1, e2));
    } else if (op == op_neq) {
      return std::make_shared<SymV>(vc_notExpr(vc, vc_eqExpr(vc, e1, e2)));
    } else {
      ABORT("invalid operator");
    }
  }
}

using PtrVal = std::shared_ptr<Value>;

// TODO(GW): using a byte-oriented memory?
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
    PreMem<V> append(immer::flex_vector<V> vs) { return PreMem<V>(mem + vs); }
    PreMem<V> alloc(size_t size) { return PreMem<V>(mem + immer::flex_vector<V>(size, nullptr)); }
    PreMem<V> take(size_t keep) { return PreMem<V>(mem.take(keep)); }
};

using Mem = PreMem<PtrVal>;

class Frame {
  public:
    using Env = immer::map<Id, Addr>;
  private:
    Env env;
  public:
    Frame(Env env) : env(env) {}
    Frame() : env(immer::map<Id, Addr>{}) {}
    size_t size() { return env.size(); }
    Addr lookup_id(Id id) const { return env.at(id); }
    Frame assign(Id id, Addr a) const {
      ASSERT(env.count(id) == 0, "Exist variable " << id << " in env");
      return Frame(env.insert({id, a})); 
    }
    Frame assign_seq(immer::flex_vector<Id> ids, size_t offset) const {
      Env env1 = env;
      for (size_t i = 0; i < ids.size(); i++) {
	env1 = env1.insert({ids.at(i), offset+i});
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
    Stack pop(size_t keep) { return Stack(mem.take(keep), env.take(env.size()-1)); }
    Stack push() { return Stack(mem, env.push_back(Frame())); }
    Stack push(Frame f) { return Stack(mem, env.push_back(f)); }

    Stack assign(Id id, PtrVal val) {
      Mem mem1 = mem.append(val);
      return Stack(mem1, env.drop(1).push_back(env.back().assign(id, mem1.size()-1)));
    }
    Stack assign_seq(immer::flex_vector<Id> ids, immer::flex_vector<PtrVal> vals) {
      Mem mem1 = mem.append(vals);
      return Stack(mem1, env.drop(1).push_back(env.back().assign_seq(ids, mem.size())));
    }
    PtrVal lookup_id(Id id) { return mem.at(env.back().lookup_id(id)); }

    PtrVal at(size_t idx) { return mem.at(idx); }
    Stack update(size_t idx, PtrVal val) { return Stack(mem.update(idx, val), env); }
    Stack alloc(size_t size) { return Stack(mem.alloc(size), env); }
};

class PC {
  private:
    immer::set<Expr> pc;
  public:
    PC(immer::set<Expr> pc) : pc(pc) {}
    PC add(Expr e) { return PC(pc.insert(e)); }
    PC addSet(immer::set<Expr> new_pc) { return PC(Set::join(pc, new_pc)); }
    void print() { print_set(pc); }
};

class SS {
  private:
    Mem heap;
    Stack stack;
    PC pc;
  public:
    SS(Mem heap, Stack stack, PC pc) : heap(heap), stack(stack), pc(pc) {}
    PtrVal stack_lookup(Id id) { return stack.lookup_id(id); }
    size_t stack_size() { return stack.mem_size(); }
    size_t fresh_stack_addr() { return stack_size(); }
    size_t frame_depth() { return frame_depth(); }
    PtrVal at(PtrVal addr) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack) return stack.at(loc->l);
      return heap.at(loc->l);
    }
    PtrVal heap_lookup(size_t addr) { return heap.at(addr); }
    SS alloc_stack(size_t size) { return SS(heap, stack.alloc(size), pc); }
    SS alloc_heap(size_t size) { return SS(heap.alloc(size), stack, pc); }
    SS update(PtrVal addr, PtrVal val) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack) return SS(heap, stack.update(loc->l, val), pc);
      return SS(heap.update(loc->l, val), stack, pc);
    }
    SS push() { return SS(heap, stack.push(), pc); }
    SS pop(size_t keep) { return SS(heap, stack.pop(keep), pc); }
    SS assign(Id id, PtrVal val) { return SS(heap, stack.assign(id, val), pc); }
    SS assign_seq(immer::flex_vector<Id> ids, immer::flex_vector<PtrVal> vals) {
      return SS(heap, stack.assign_seq(ids, vals), pc);
    }
    SS addPC(Expr e) { return SS(heap, stack, pc.add(e)); }
    SS addPCSet(immer::set<Expr> s) { return SS(heap, stack, pc.addSet(s)); }
};

static Mem mt_mem = Mem(immer::flex_vector<PtrVal>{});
static Stack mt_stack = Stack(mt_mem, immer::flex_vector<Frame>{});
static PC mt_pc = PC(immer::set<Expr>{});
static SS mt_ss = SS(mt_mem, mt_stack, mt_pc);

#endif
