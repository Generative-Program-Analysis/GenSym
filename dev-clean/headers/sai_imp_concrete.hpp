#include <ostream>
#include <variant>

#include <sai.hpp>

#ifndef SAI_IMP_CONC_HEADERS
#define SAI_IMP_CONC_HEADERS

struct IntV { int i; };
struct BoolV { bool b; };
using Value = std::variant<IntV, BoolV>;

template<class... Ts> struct overloaded : Ts... { using Ts::operator()...; };
template<class... Ts> overloaded(Ts...) -> overloaded<Ts...>;

std::ostream& operator<<(std::ostream& os, const IntV& i) {
  return os << "IntV(" << i.i << ")" << std::endl;
}

std::ostream& operator<<(std::ostream& os, const BoolV& b) {
  return os << "BoolV(" << b.b << ")" << std::endl;
}

std::ostream& operator<<(std::ostream& os, const Value& v) {
  return std::visit(overloaded {
    [&](IntV i)->std::ostream& { return os << i; },
    [&](BoolV b)->std::ostream& { return os << b; }
  }, v);
}

#endif
