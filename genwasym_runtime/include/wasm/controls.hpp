#ifndef WASM_CONTROLS_HPP
#define WASM_CONTROLS_HPP

#include <functional>
#include <memory>
#include <variant>

class MContRepr;

struct MCont_t {
  std::shared_ptr<MContRepr> ptr;

  MCont_t();
  MCont_t(const MCont_t &p);
  MCont_t(std::shared_ptr<MContRepr> p);
  MCont_t(std::function<std::monostate(std::monostate)> haltK);

  bool is_null() const;
  std::monostate enter();
};

using Cont_t = std::function<std::monostate(std::monostate)>;

extern MCont_t CURRENT_MCONT;

std::monostate updateCurrentMCont(MCont_t newMCont);

class MContRepr {
  friend std::monostate enterCC(std::monostate);

public:
  MContRepr(Cont_t cont, MCont_t mcont);
  MContRepr(std::function<std::monostate(std::monostate)> haltK);

  std::monostate enter();

private:
  Cont_t cont;
  MCont_t mcont;
};

MCont_t prependCont(Cont_t k, MCont_t mcont);

// Enter the current global MCont (CURRENT_MCONT)
std::monostate enterCC(std::monostate);

struct Control {
  Cont_t cont;
  MCont_t mcont;

  Control(Cont_t cont, MCont_t mcont);
};

using Func_t = std::function<std::monostate(std::monostate)>;

#endif // WASM_CONTROLS_HPP