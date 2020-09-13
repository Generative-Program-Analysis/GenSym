#include <ostream>
#include <variant>
#include <string>
#include <vector>
#include <iostream>

#include <immer/flex_vector.hpp>
#include <sai.hpp>

#ifndef SAI_LLVM_SYM_HEADERS
#define SAI_LLVM_SYM_HEADERS

/* Note:
 * Mem := flex_vector<Value>
 * Addr := Int
 * Value := IntV | SymV | LocV
 * TODO: update_mem, stack_addr, select_mem, stack_addr_save
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

#define make_IntV(i) std::make_shared<IntV>(i)

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


using PtrVal = Ptr<Value>;
using Mem = immer::flex_vector<PtrVal>;

static Mem mt_mem = immer::flex_vector<PtrVal>{};
static immer::flex_vector<std::pair<std::tuple<Mem, Mem, set<Value>>, Value>> mt_res = flex_vector<std::pair<std::tuple<Mem, Mem, set<Value>>, Value>>{};

#define mem_take(m, n) m.take(n)
#define mem_size(m) m.size()
#define mem_lookup(m, a) m.at(a)
#define mem_alloc(m, size) m

Mem mem_update(Mem m, unsigned int addr, Ptr<Value> v) {
  if (m.size() <= addr) // TODO: or <=?
    return m.push_back(v);
  return m.update(addr, [&](auto l) { return v; });
}

Mem mem_updateL(Mem m, unsigned int addr, immer::flex_vector<PtrVal> vals) {
  return m + vals;
}

#endif
