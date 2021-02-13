#ifndef LLSC_HEADERS
#define LLSC_HEADERS

#include <sys/resource.h>

#include <ostream>
#include <variant>
#include <string>
#include <vector>
#include <iostream>
#include <map>
#include <cstdint>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <chrono>

#include <sai.hpp>
#include <immer/flex_vector_transient.hpp>

//#define STR_SYMV

#ifndef STR_SYMV
#include <stp/c_interface.h>
#include <stp_handle.hpp>
#endif

using namespace std::chrono;

inline unsigned int bitwidth = 32;
inline unsigned int var_name = 0;

using Id = int;
using Addr = unsigned int;

#ifdef STR_SYMV
using Expr = std::string;
#else
inline VC vc = vc_createValidityChecker();
#endif

/* Value representations */

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

using PtrVal = std::shared_ptr<Value>;

struct IntV : Value {
  int i;
  IntV(int i) : i(i) {}
  IntV(const IntV& v) { i = v.i; }
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "IntV(" << i << ")";
  }
  virtual Expr to_SMTExpr() const override {
#ifdef STR_SYMV
    return "dummy";
#else
    return vc_bvConstExprFromInt(vc, 32, i);
#endif
  }
  virtual Expr to_SMTBool() const override {
#ifdef STR_SYMV
    return "dummy";
#else
    if (i) return vc_trueExpr(vc);
    else return vc_falseExpr(vc);
#endif
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
inline LocV::Kind proj_LocV_kind(Ptr<Value> v) {
  return std::dynamic_pointer_cast<LocV>(v)->k;
}

#ifdef STR_SYMV
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
inline Ptr<Value> make_SymV(String n) { return std::make_shared<SymV>(n); }
#else
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
#endif

enum kOP {
  op_plus, op_minus, op_mult, op_div,
  op_eq, op_ge, op_gt, op_le, op_lt, op_neq,
};

inline Ptr<Value> op_2(kOP op, Ptr<Value> v1, Ptr<Value> v2) {
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
#ifdef STR_SYMV
    return make_SymV("unknown");
#else
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
#endif
  }
}

/* Memory, stack, and symbolic state representation */

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
    PreMem<V> alloc(size_t size) {
      auto m = mem.transient();
      for (int i = 0; i < size; i++) { m.push_back(nullptr); }
      return PreMem<V>(m.persistent());
    }
    PreMem<V> take(size_t keep) { return PreMem<V>(mem.take(keep)); }
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
    Stack pop(size_t keep) { return Stack(mem.take(keep), env.take(env.size()-1)); }
    Stack push() { return Stack(mem, env.push_back(Frame())); }
    Stack push(Frame f) { return Stack(mem, env.push_back(f)); }

    Stack assign(Id id, PtrVal val) {
      return Stack(mem, env.update(env.size()-1, [&](auto f) { return f.assign(id, val); }));
    }
    Stack assign_seq(immer::flex_vector<Id> ids, immer::flex_vector<PtrVal> vals) {
      return Stack(mem, env.update(env.size()-1, [&](auto f) { return f.assign_seq(ids, vals); }));
    }
    PtrVal lookup_id(Id id) { return env.back().lookup_id(id); }

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
    PtrVal env_lookup(Id id) { return stack.lookup_id(id); }
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

inline const Mem mt_mem = Mem(immer::flex_vector<PtrVal>{});
inline const Stack mt_stack = Stack(mt_mem, immer::flex_vector<Frame>{});
inline const PC mt_pc = PC(immer::set<Expr>{});
inline const SS mt_ss = SS(mt_mem, mt_stack, mt_pc);

inline const immer::flex_vector<std::pair<SS, PtrVal>> mt_path_result =
  immer::flex_vector<std::pair<SS, PtrVal>>{};

/* Stack manipulation */

#define STACKSIZE_16MB (16 * 1024 * 1024)
#define STACKSIZE_32MB (32 * 1024 * 1024)
#define STACKSIZE_64MB (64 * 1024 * 1024)
#define STACKSIZE_128MB (128 * 1024 * 1024)

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

/* Coverage information */

// TODO: branch coverage
struct CoverageMonitor {
  private:
    using BlockId = std::int64_t;
    // Total number of blocks
    std::uint64_t num_blocks;
    // The number of execution for each block
    std::map<BlockId, std::uint64_t> block_cov;
    steady_clock::time_point start;
    std::mutex m;
  public:
    CoverageMonitor() : num_blocks(0), start(steady_clock::now()) {}
    CoverageMonitor(std::uint64_t num_blocks) : num_blocks(num_blocks), start(steady_clock::now()) {}
    void set_num_blocks(std::uint64_t n) {
      num_blocks = n;
    }
    void inc_block(BlockId b) {
      std::unique_lock<std::mutex> lk(m);
      auto t = block_cov.find(b);
      if (t != block_cov.end()) t++;
      else block_cov[b] = 1;
    }
    void print_block_cov() {
      steady_clock::time_point now = steady_clock::now();
      std::cout << "[" << (duration_cast<milliseconds>(now - start).count() / 1000.0) << " s] ";
      std::cout << "Block coverage: "
                << block_cov.size() << "/"
                << num_blocks << "\n"
                << std::flush;
    }
    void start_monitor() {
      std::thread([this]{
        while (this->block_cov.size() <= this->num_blocks) {
          this->print_block_cov();
          std::this_thread::sleep_for(seconds(1));
        }
      }).detach();
    }
};

inline CoverageMonitor cov;

#endif
