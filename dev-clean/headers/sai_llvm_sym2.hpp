#include <ostream>
#include <variant>
#include <string>
#include <vector>
#include <iostream>
#include <map>

#include <immer/flex_vector.hpp>
#include <sai.hpp>

#ifndef SAI_LLVM_SYM_HEADERS
#define SAI_LLVM_SYM_HEADERS

/* Note:
 * Mem := flex_vector<Value>
 * Addr := Int
 * Value := IntV | SymV | LocV
 * TODO: proj_SMTExpr, SymV, select_mem
 * Not necessary?: heap_addr, mem_alloc
 * Done: make_IntV, make_LocV, proj_LocV, proj_IntV, mt_mem, mem_take, mem_size, mem_lookup, mem_update
 *       mem_updateL
 */

struct Value {
  friend std::ostream& operator<<(std::ostream&os, const Value& v) {
    return v.toString(os);
  }
  virtual std::ostream& toString(std::ostream& os) const = 0;
};

struct IntV : Value {
  int i;
  IntV(int i) : i(i) {}
  IntV(const IntV& v) { i = v.i; }
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "IntV(" << i << ")";
  }
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
  unsigned int l;
  Kind k;
  LocV(unsigned int l, Kind k) : l(l), k(k) {}
  LocV(const LocV& v) { l = v.l; }
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "LocV(" << l << ")";
  }
};

#define make_LocV(i, k) std::make_shared<LocV>(i, k)

inline unsigned int proj_LocV(Ptr<Value> v) {
  return std::dynamic_pointer_cast<LocV>(v)->l;
}
inline LocV::Kind kStack() { return LocV::kStack; }
inline LocV::Kind kHeap() { return LocV::kHeap; }

struct SymV : Value {
  
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "SymV(" << "FIXME" << ")";
  }
};

Ptr<Value> proj_SMTExpr(Ptr<Value> v) {
  // FIXME
  return v;
}

using PtrVal = Ptr<Value>;
using SMTExpr = Ptr<Value>; //FIXME
using PC = immer::set<SMTExpr>;
using Mem = immer::flex_vector<PtrVal>;
using Env = immer::map<String, int>;
using SS = std::tuple<Mem, std::pair<Mem, immer::flex_vector<Env>>, PC>;

// static Mem mt_mem = immer::flex_vector<PtrVal>{};
inline Mem mt_mem() { return immer::flex_vector<PtrVal>{}; }

#define mem_take(m, n) m.take(n)
#define mem_size(m) m.size()
#define mem_lookup(m, a) m.at(a)
#define fresh_addr(m) m.size()

Mem mem_alloc(Mem m, int size) {
  return m + immer::flex_vector<PtrVal>(size, nullptr);
}

Mem mem_update(Mem m, unsigned int addr, PtrVal v) {
  return m.update(addr, [&](auto l) { return v; });
}

Mem mem_updateL(Mem m, unsigned int addr, immer::flex_vector<PtrVal> vals) {
  immer::flex_vector<PtrVal> res = m;
  for (int i = 0; i < vals.size(); i++) {
    res = res.update(addr + i, [&](auto l) { return vals.at(i); } );
  }
  return res;
}

Mem select_mem(PtrVal v, Mem heap, Mem stack) {
  auto loc = std::dynamic_pointer_cast<LocV>(v);
  if (loc == nullptr) {
    std::cout << "Value is not a location value!" << std::endl;
  }
  if (loc->k == LocV::kStack) return stack;
  return heap;
}

static std::map<String, int> stack_env{};

void stack_addr_save(String x, int addr) {
  if (stack_env.find(x) != stack_env.end()) {
    std::cout << "Existing environment mapping " << x << " -> " << addr << std::endl;
  }
  stack_env[x] = addr;
}

int stack_addr(Mem m, String x) {
  if (stack_env.find(x) == stack_env.end()) {
    std::cout << "Cannot find " << x << " in stack_env" << std::endl;
  }
  return stack_env.at(x);
}

SS update_mem(SS state, PtrVal k, PtrVal v) {
  auto loc = std::dynamic_pointer_cast<LocV>(k);
  if (loc->k == LocV::kStack) {
    auto stack_env = std::get<1>(state);
    auto stack = std::get<0>(stack_env);
    auto env = std::get<1>(stack_env);
    auto new_stack = mem_update(stack, loc->l, v);
    return {std::get<0>(state), {new_stack, env}, std::get<2>(state)};
  } else {
    auto heap = std::get<0>(state);
    auto new_heap = mem_update(heap, loc->l, v);
    return {new_heap, std::get<1>(state), std::get<2>(state)};
  }
}

#endif
