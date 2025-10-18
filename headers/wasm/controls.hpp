
#ifndef WASM_CONTROLS_HPP
#define WASM_CONTROLS_HPP

#include <functional>

#include <variant>

using MCont_t = std::function<std::monostate(std::monostate)>;
using Cont_t = std::function<std::monostate(MCont_t)>;

struct Control {
  Cont_t cont;
  MCont_t mcont;

  Control(Cont_t cont, MCont_t mcont) : cont(cont), mcont(mcont) {}
};

#endif // WASM_CONTROLS_HPP