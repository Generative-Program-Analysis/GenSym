#include "wasm/controls.hpp"

#include <cassert>
#include <memory>

MCont_t CURRENT_MCONT;

MCont_t::MCont_t() : ptr(nullptr) {}

MCont_t::MCont_t(const MCont_t &p) : ptr(p.ptr) {}

MCont_t::MCont_t(std::shared_ptr<MContRepr> p) : ptr(p) {}

MCont_t::MCont_t(std::function<std::monostate(std::monostate)> haltK)
    : ptr(std::make_shared<MContRepr>(haltK)) {
  assert(haltK);
}

bool MCont_t::is_null() const {
  return ptr == nullptr;
}

std::monostate MCont_t::enter() {
  return ptr->enter();
}

std::monostate updateCurrentMCont(MCont_t newMCont) {
  CURRENT_MCONT = newMCont;
  return std::monostate{};
}

MContRepr::MContRepr(Cont_t cont, MCont_t mcont)
    : cont(cont), mcont(mcont) {}

MContRepr::MContRepr(std::function<std::monostate(std::monostate)> haltK)
    : cont(haltK), mcont() {}

std::monostate MContRepr::enter() {
  std::monostate (*func_ptr)(std::monostate) = nullptr;
  {
    auto cont = this->cont;
    func_ptr = *cont.target<std::monostate (*)(std::monostate)>();
  }

  CURRENT_MCONT = mcont;

  return func_ptr(std::monostate{});
}

MCont_t prependCont(Cont_t k, MCont_t mcont) {
  return std::make_shared<MContRepr>(k, mcont);
}

std::monostate enterCC(std::monostate) {
  std::monostate (*func_ptr)(std::monostate) = nullptr;
  {
    auto cont = CURRENT_MCONT.ptr->cont;
    func_ptr = *cont.target<std::monostate (*)(std::monostate)>();
  }

  CURRENT_MCONT = CURRENT_MCONT.ptr->mcont;

  __attribute__((musttail)) return func_ptr(std::monostate{});
}

Control::Control(Cont_t cont, MCont_t mcont) : cont(cont), mcont(mcont) {}