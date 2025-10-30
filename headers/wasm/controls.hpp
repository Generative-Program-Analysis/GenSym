
#ifndef WASM_CONTROLS_HPP
#define WASM_CONTROLS_HPP

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
      : ptr(std::make_shared<MContRepr>(haltK)) {}
  bool is_null() const { return ptr == nullptr; }

  std::monostate enter();
};
using Cont_t = std::function<std::monostate(MCont_t)>;
class MContRepr {
public:
  MContRepr(Cont_t cont, MCont_t mcont) : cont(cont), mcont(mcont) {}

  MContRepr(std::function<std::monostate(std::monostate)> haltK)
      : cont([=](MCont_t) {
          // std::cout << "Halting the program..." << std::endl;

          return haltK(std::monostate{});
        }),
        mcont() {}

  MContRepr() : cont(nullptr), mcont() {}

  std::monostate enter() {
    // std::cout << "Entering MCont\n";
    // std::cout << "Cont cont: " << (cont ? "valid" : "null") << "\n";
    // std::cout << "MCont mcont: " << (mcont ? "valid" : "null") << "\n";
    if (mcont.is_null()) {
      return cont(std::make_shared<MContRepr>(
          MContRepr())); // when mcont is null, we pass a dummy MContRepr
    }
    return cont(mcont);
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

#endif // WASM_CONTROLS_HPP