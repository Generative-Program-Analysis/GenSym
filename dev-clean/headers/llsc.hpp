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

#include <memory>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <future>

#include <sai.hpp>
//#include <thread_pool.hpp>
#include <immer/flex_vector_transient.hpp>

#define STR_SYMV

#ifndef STR_SYMV
#include <stp/c_interface.h>
#include <stp_handle.hpp>
#endif

using namespace std::chrono;

inline unsigned int bitwidth = 32;
inline unsigned int var_name = 0;

using BlockLabel = int;
using Id = int;
using Addr = unsigned int;
using IntData = int;

#ifdef STR_SYMV
using Expr = std::string;
#else
inline std::mutex vc_lock;
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
  virtual Ptr<Value> to_IntV() const = 0;
  virtual bool is_conc() const = 0;
};

using PtrVal = std::shared_ptr<Value>;

struct IntV : Value {
  IntData i;
  IntV(IntData i) : i(i) {}
  IntV(const IntV& v) { i = v.i; }
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "IntV(" << i << ")";
  }
  virtual Expr to_SMTExpr() const override {
#ifdef STR_SYMV
    return "dummy";
#else
    std::unique_lock<std::mutex> lk(vc_lock);
    return vc_bvConstExprFromInt(vc, 32, i);
#endif
  }
  virtual Expr to_SMTBool() const override {
#ifdef STR_SYMV
    return "dummy";
#else
    std::unique_lock<std::mutex> lk(vc_lock);
    if (i) return vc_trueExpr(vc);
    else return vc_falseExpr(vc);
#endif
  }
  virtual Ptr<Value> to_IntV() const override { return std::make_shared<IntV>(i); }
  virtual bool is_conc() const override { return true; }
};

inline Ptr<Value> make_IntV(IntData i) {
  return std::make_shared<IntV>(i);
}

inline Ptr<Value> make_IntV(IntData i, int bw) {
  //FIXME, bit width
  return std::make_shared<IntV>(i);
}

inline IntData proj_IntV(Ptr<Value> v) {
  return std::dynamic_pointer_cast<IntV>(v)->i;
}

struct FloatV : Value {
  float f;
  FloatV(float f) : f(f) {}
  FloatV(const FloatV& v) { f = v.f; }
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "FloatV(" << f << ")";
  }
  virtual Expr to_SMTExpr() const override {
    ABORT("to_SMTExpr: unexpected value FloatV.");
  }
  virtual Expr to_SMTBool() const override {
    ABORT("to_SMTBool: unexpected value FloatV.");
  }
  virtual bool is_conc() const override { return true; }
  virtual Ptr<Value> to_IntV() const override { return nullptr; }
};

inline Ptr<Value> make_FloatV(float f) {
  return std::make_shared<FloatV>(f);
}

inline int proj_FloatV(Ptr<Value> v) {
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
  virtual Expr to_SMTExpr() const override {
    ABORT("to_SMTExpr: unexpected value LocV.");
  }
  virtual Expr to_SMTBool() const override {
    ABORT("to_SMTBool: unexpected value LocV.");
  }
  virtual bool is_conc() const override {
    ABORT("is_conc: unexpected value LocV.");
  }
  virtual Ptr<Value> to_IntV() const override { return std::make_shared<IntV>(l); }
};

inline Ptr<Value> make_LocV(unsigned int i, LocV::Kind k, int size) {
  return std::make_shared<LocV>(i, k, size);
}

inline Ptr<Value> make_LocV(unsigned int i, LocV::Kind k) {
  return std::make_shared<LocV>(i, k, -1);
}

inline unsigned int proj_LocV(Ptr<Value> v) {
  return std::dynamic_pointer_cast<LocV>(v)->l;
}
inline LocV::Kind proj_LocV_kind(Ptr<Value> v) {
  return std::dynamic_pointer_cast<LocV>(v)->k;
}
inline int proj_LocV_size(Ptr<Value> v) {
  return std::dynamic_pointer_cast<LocV>(v)->size;
}

inline Ptr<Value> make_LocV_inc(Ptr<Value> loc, int i) {
  return make_LocV(proj_LocV(loc) + i, proj_LocV_kind(loc), proj_LocV_size(loc));
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
  virtual Ptr<Value> to_IntV() const override { return nullptr; }
};
inline Ptr<Value> make_SymV(String n) { return std::make_shared<SymV>(n); }
inline Ptr<Value> make_SymV(String n, int bw) { 
  // ignore the bw
  return std::make_shared<SymV>(n);
}
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
  virtual Ptr<Value> to_IntV() const override { return nullptr; }
};
inline Ptr<Value> make_SymV(String n) {
  std::unique_lock<std::mutex> lk(vc_lock);
  return std::make_shared<SymV>(vc_varExpr(vc, n.c_str(), vc_bv32Type(vc)));
}
inline Ptr<Value> make_SymV(String n, int bw) { 
  // FIXME: make bit vector of bw bits
  std::unique_lock<std::mutex> lk(vc_lock);
  return std::make_shared<SymV>(vc_varExpr(vc, n.c_str(), vc_bv32Type(vc)));
}
inline Expr to_SMTBoolNeg(PtrVal v) {
  auto e = v->to_SMTBool();
  std::unique_lock<std::mutex> lk(vc_lock);
  return vc_notExpr(vc, e);
}
#endif

struct StructV : Value {
  immer::flex_vector<PtrVal> fs;
  StructV(immer::flex_vector<Ptr<Value>> fs) : fs(fs) {}
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "StructV(..)";
  }
  virtual Expr to_SMTExpr() const override {
    ABORT("to_SMTExpr: unexpected value StructV.");
  }
  virtual Expr to_SMTBool() const override {
    ABORT("to_SMTBool: unexpected value StructV.");
  }
  virtual bool is_conc() const override {
    ABORT("is_conc: unexpected value StructV.");
  }
  virtual Ptr<Value> to_IntV() const override { return nullptr; }
};

inline PtrVal structV_at(PtrVal v, int idx) {
  auto sv = std::dynamic_pointer_cast<StructV>(v);
  if (sv) return (sv->fs).at(idx);
  else ABORT("StructV_at: non StructV value");
}

enum iOP {
  op_add, op_sub, op_mul, op_sdiv, op_udiv,
  op_eq, op_uge, op_ugt, op_ule, op_ult, 
  op_sge, op_sgt, op_sle, op_slt, op_neq,
  op_shl, op_lshr, op_ashr, op_and, op_or, op_xor, 
  op_urem, op_srem
};

inline Ptr<Value> int_op_2(iOP op, Ptr<Value> v1, Ptr<Value> v2) {
  auto i1 = std::dynamic_pointer_cast<IntV>(v1->to_IntV());
  auto i2 = std::dynamic_pointer_cast<IntV>(v2->to_IntV())
;
  if (i1 && i2) {
    if (op == op_add) {
      return make_IntV(i1->i + i2->i);
    } else if (op == op_sub) {
      return make_IntV(i1->i - i2->i);
    } else if (op == op_mul) {
      return make_IntV(i1->i * i2->i);
    // FIXME: singed and unsigned div
    } else if (op == op_sdiv || op == op_udiv) {
      return make_IntV(i1->i / i2->i);
    } else if (op == op_eq) {
      return make_IntV(i1->i == i2->i);
    } else if (op == op_uge || op == op_sge) {
      return make_IntV(i1->i >= i2->i);
    } else if (op == op_ugt || op == op_sgt) {
      return make_IntV(i1->i > i2->i);
    } else if (op == op_ule || op == op_sle) {
      return make_IntV(i1->i <= i2->i);
    } else if (op == op_ult || op == op_slt) {
      return make_IntV(i1->i < i2->i);
    } else if (op == op_neq) {
      return make_IntV(i1->i != i2->i);
    } else if (op == op_urem || op == op_srem) {
      return make_IntV(i1->i % i2->i);
    } else {
      ABORT("invalid operator");
    }
  } else {
#ifdef STR_SYMV
    return make_SymV("unknown");
#else
    Expr e1 = v1->to_SMTExpr();
    Expr e2 = v2->to_SMTExpr();
    std::unique_lock<std::mutex> lk(vc_lock);
    if (op == op_add) {
      return std::make_shared<SymV>(vc_bv32PlusExpr(vc, e1, e2));
    } else if (op == op_sub) {
      return std::make_shared<SymV>(vc_bv32MinusExpr(vc, e1, e2));
    } else if (op == op_mul) {
      return std::make_shared<SymV>(vc_bv32MultExpr(vc, e1, e2));
    } else if (op == op_sdiv || op == op_udiv) {
      return std::make_shared<SymV>(vc_bvDivExpr(vc, 32, e1, e2));
    } else if (op == op_eq) {
      return std::make_shared<SymV>(vc_eqExpr(vc, e1, e2));
    } else if (op == op_uge || op == op_sge) {
      return std::make_shared<SymV>(vc_bvGeExpr(vc, e1, e2));
    } else if (op == op_ugt || op == op_sgt) {
      return std::make_shared<SymV>(vc_bvGtExpr(vc, e1, e2));
    } else if (op == op_ule || op == op_sle) {
      return std::make_shared<SymV>(vc_sbvLeExpr(vc, e1, e2));
    } else if (op == op_ult || op == op_slt) {
      return std::make_shared<SymV>(vc_sbvLtExpr(vc, e1, e2));
    } else if (op == op_neq) {
      return std::make_shared<SymV>(vc_notExpr(vc, vc_eqExpr(vc, e1, e2)));
    } else {
      ABORT("invalid operator");
    }
#endif
  }
}

enum fOP {
  op_fadd, op_fsub, op_fmul, op_fdiv
};

inline Ptr<Value> float_op_2(fOP op, Ptr<Value> v1, Ptr<Value> v2) {
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

// FIXME: 
inline Ptr<Value> bv_sext(Ptr<Value> v, int bw) {
  return v;
}

inline Ptr<Value> trunc(PtrVal v1, int from, int to) {
  auto i1 = std::dynamic_pointer_cast<IntV>(v1);
  if (i1) {
    IntData i = i1->i;
    i = i << (from - to);
    i = i >> (from - to);
    return make_IntV(i);
  } else {
    auto s1 = std::dynamic_pointer_cast<SymV>(v1);
    if (s1) {
      ABORT("Truncate a symvalue, needs work!");
    }
    ABORT("Truncate a ??? value, exit");
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
    immer::set<Expr> pc;
  public:
    PC(immer::set<Expr> pc) : pc(pc) {}
    PC add(Expr e) { return PC(pc.insert(e)); }
    PC addSet(immer::set<Expr> new_pc) { return PC(Set::join(pc, new_pc)); }
    immer::set<Expr> getPC() { return pc; }
    void print() { print_set(pc); }
};

class SS {
  private:
    Mem heap;
    Stack stack;
    PC pc;
    BlockLabel bb;
  public:
    SS(Mem heap, Stack stack, PC pc, BlockLabel bb) : heap(heap), stack(stack), pc(pc), bb(bb) {}
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
    SS addPC(Expr e) { return SS(heap, stack, pc.add(e), bb); }
    SS addPCSet(immer::set<Expr> s) { return SS(heap, stack, pc.addSet(s), bb); }
    SS addIncomingBlock(BlockLabel blabel) { return SS(heap, stack, pc, blabel); }
    immer::set<Expr> getPC() { return pc.getPC(); }
    // TODO temp solution
    PtrVal getVarargLoc() {return stack.getVarargLoc(); }
};

inline const Mem mt_mem = Mem(immer::flex_vector<PtrVal>{});
inline const Stack mt_stack = Stack(mt_mem, immer::flex_vector<Frame>{});
inline const PC mt_pc = PC(immer::set<Expr>{});
inline const BlockLabel mt_bb = 0;
inline const SS mt_ss = SS(mt_mem, mt_stack, mt_pc, mt_bb);

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

/* Async */

inline size_t MAX_ASYNC = 4;
inline std::mutex m;
//inline std::condition_variable cv;
inline std::atomic<unsigned int> num_async = 0;
inline std::atomic<unsigned int> tt_num_async = 0;
//inline thread_pool pool(4);

inline bool can_par() {
  return num_async < MAX_ASYNC;
  //return pool.can_enqueue();
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
  //return pool.enqueue(f);
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
    CoverageMonitor(std::uint64_t num_blocks) : num_blocks(num_blocks), start(steady_clock::now()) {}
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
      std::cout << "#threads: " << num_async + 1 << " #async created: " << tt_num_async << "\n";
      //std::cout << "current #async: " << pool.tasks_size() << " total #async: " << tt_num_async << "\n";
    }
    void print_time() {
      steady_clock::time_point now = steady_clock::now();
      std::cout << "[" << (duration_cast<milliseconds>(now - start).count() / 1000.0) << " s] ";
    }
    void start_monitor() {
      std::thread([this]{
        while (this->block_cov.size() <= this->num_blocks) {
          print_time();
          print_block_cov();
          print_path_cov();
          print_async();
          std::this_thread::sleep_for(seconds(1));
        }
      }).detach();
    }
};

inline CoverageMonitor cov;

#endif
