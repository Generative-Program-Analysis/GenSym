#include <functional>
#include <variant>

using MCont_t = std::function<std::monostate(std::monostate)>;
using Cont_t = std::function<std::monostate(MCont_t)>;