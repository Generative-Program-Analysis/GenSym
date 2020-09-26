#include <ostream>
#include <variant>
#include <string>
#include <vector>
#include <iostream>
#include <map>

#include <immer/flex_vector.hpp>
#include <sai.hpp>
#include <stp/c_interface.h>
#include <stp_handle.hpp>

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

extern VC vc;
static unsigned int bitwidth = 32;
static unsigned int var_name = 0;

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
  Expr v;
  SymV(Expr v) : v(v) {}
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "SymV(" << v << ")";
  }
};


Ptr<Value> make_SymV(String n) {
  // TODO type
  return std::make_shared<SymV>(vc_varExpr(vc, n.c_str(), vc_bv32Type(vc)));
}

Expr proj_SMTBool(Ptr<Value> v) {
  if (auto i = std::dynamic_pointer_cast<IntV>(v)) {
    if (i -> i) vc_trueExpr(vc);
    else return vc_falseExpr(vc);
  } else if (auto sym = std::dynamic_pointer_cast<SymV>(v)) {
    return sym->v;
  } else if (auto loc = std::dynamic_pointer_cast<LocV>(v)){
    std::cout << "Value is LocV" << std::endl;
  } else {
    std::cout << "Value is ???" << std::endl;
  }
}

Expr proj_SMTExpr(Ptr<Value> v) {
  if (auto i = std::dynamic_pointer_cast<IntV>(v)) {
    return vc_bvConstExprFromInt(vc, 32, i->i);
  } else if (auto sym = std::dynamic_pointer_cast<SymV>(v)) {
    return sym->v;
  } else if (auto loc = std::dynamic_pointer_cast<LocV>(v)){
    std::cout << "Value is LocV" << std::endl;
  } else {
    std::cout << "Value is ???" << std::endl;
  }
}

void print_pcset(immer::set<Expr>& s) {
  std::cout << "{";
  int i = 0;
  for (auto x : s) {
    vc_printExpr(vc, x);
    if (i != s.size()-1) std::cout << ", ";
    i = i + 1;
  }
  std::cout << "}";
}

immer::flex_vector<Expr> set_to_list(immer::set<Expr>& s) {
  auto res = immer::flex_vector<Expr>{};
  for (auto x : s) {
    res = res.push_back(x);
  }
  return res;
}

enum kOP {
  op_plus,
  op_minus,
  op_mult,
  op_div,
  op_eq,
  op_ge,
  op_gt,
  op_le,
  op_lt,
  op_neq,
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
      ASSERT(false, "invalid operator");
    }
  } else {
    Expr e1 = proj_SMTExpr(v1);
    Expr e2 = proj_SMTExpr(v2);
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
      ASSERT(false, "invalid operator");
    }
  }
}

using PtrVal = Ptr<Value>;
using SMTExpr = Expr; //FIXME
using PC = immer::set<SMTExpr>;
using Mem = immer::flex_vector<PtrVal>;
using Env = immer::map<int, int>;
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
  if (loc->k == LocV::kStack) {
    return stack;
  }
  return heap;
}

LocV::Kind select_loc(PtrVal v) {
  auto loc = std::dynamic_pointer_cast<LocV>(v);
  if (loc == nullptr) {
    std::cout << "Value is not a location value!" << std::endl;
  }
  return loc->k;
}

/*
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
*/

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

// primitive functions
immer::flex_vector<std::pair<SS, PtrVal>> make_symbolic(SS state, immer::flex_vector<PtrVal> args) {
  auto addr = std::dynamic_pointer_cast<LocV>(args.at(0));
  auto tempaddr = make_LocV(addr -> l, addr -> k);
  auto len = std::dynamic_pointer_cast<IntV>(args.at(1));
  
  for (int i = 0; i < len->i; i++) {
    // std::cout << "Some variable is made symbolic" << std::endl;
    // should name corresponds to original name?
    state = update_mem(state, tempaddr, make_SymV("x" + std::to_string(var_name++)));
    tempaddr->l = tempaddr->l + 1;
  }
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state, make_IntV(0)}};
}


immer::flex_vector<std::pair<SS, PtrVal>> read_maze(SS state, immer::flex_vector<PtrVal> args) {
  // TODO: be careful if addr is used later, or make a copy of args.at(1)
  auto addr = std::dynamic_pointer_cast<LocV>(args.at(1));
  auto len = std::dynamic_pointer_cast<IntV>(args.at(2));
  String raw_input = "ssssddddwwaawwddddssssddwwww";
  // val inputStr = rawInput.take(Math.min(len, rawInput.length))
  for (int i = 0; i < raw_input.size(); i++) {
    state = update_mem(state, addr, make_IntV(raw_input.at(i)));
    addr->l = addr->l + 1;
  }
  state = update_mem(state, addr, make_IntV(0));
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state, make_IntV(0)}};
}

immer::flex_vector<std::pair<SS, PtrVal>> sym_read(SS state, immer::flex_vector<PtrVal> args) {
  // TODO? not needed for now
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state, make_IntV(0)}};
}

immer::flex_vector<std::pair<SS, PtrVal>> sym_printf(SS state, immer::flex_vector<PtrVal> args) {
  // TODO?
  return immer::flex_vector<std::pair<SS, PtrVal>>{{state, make_IntV(0)}};
}

#endif
