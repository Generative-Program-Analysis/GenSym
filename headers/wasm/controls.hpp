
#ifndef WASM_CONTROLS_HPP
#define WASM_CONTROLS_HPP

#include <cassert>
#include <functional>

#include <iostream>
#include <memory>
#include <variant>

class MContRepr;
struct MCont_t {
  std::shared_ptr<MContRepr> ptr;
  MCont_t() : ptr(nullptr) {}
  MCont_t(const MCont_t &p) : ptr(p.ptr) {}
  MCont_t(std::shared_ptr<MContRepr> p) : ptr(p) {}
  MCont_t(std::function<std::monostate(std::monostate)> haltK)
      : ptr(std::make_shared<MContRepr>(haltK)) {
    assert(haltK);
  }
  bool is_null() const { return ptr == nullptr; }

  std::monostate enter();
};
using Cont_t = std::function<std::monostate(std::monostate)>;

static MCont_t CURRENT_MCONT;

inline std::monostate updateCurrentMCont(MCont_t newMCont) {
  CURRENT_MCONT = newMCont;
  return std::monostate{};
}

class MContRepr {
public:
  MContRepr(Cont_t cont, MCont_t mcont) : cont(cont), mcont(mcont) {}

  MContRepr(std::function<std::monostate(std::monostate)> haltK)
      : cont(haltK), mcont() {}

  // MContRepr() : cont(nullptr), mcont() {}

  std::monostate enter() {
    // std::cout << "Entering MCont\n";
    // std::cout << "Cont cont: " << (cont ? "valid" : "null") << "\n";
    // std::cout << "MCont mcont: " << (mcont ? "valid" : "null") << "\n";
    CURRENT_MCONT = mcont;
    return cont(std::monostate{});
  }

private:
  Cont_t cont;
  MCont_t mcont;
};

inline MCont_t prependCont(Cont_t k, MCont_t mcont) {
  return std::make_shared<MContRepr>(k, mcont);
}

inline std::monostate MCont_t::enter() { return ptr->enter(); }

struct Control {
  Cont_t cont;
  MCont_t mcont;

  Control(Cont_t cont, MCont_t mcont) : cont(cont), mcont(mcont) {}
};

using Func_t = std::function<std::monostate(std::monostate)>;

#endif // WASM_CONTROLS_HPP