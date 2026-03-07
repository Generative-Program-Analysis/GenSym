#ifndef UTILS_HPP
#define UTILS_HPP
#include <iostream>
#include <variant>

#ifndef GENSYM_ASSERT
#define GENSYM_ASSERT(condition)                                               \
  do {                                                                         \
    if (!(condition)) {                                                        \
      throw std::runtime_error(std::string("Assertion failed: ") + " (" +      \
                               __FILE__ + ":" + std::to_string(__LINE__) +     \
                               ")");                                           \
    }                                                                          \
  } while (0)
#endif

#ifndef NO_DBG
#define GENSYM_DBG(obj)                                                        \
  do {                                                                         \
    std::cout << "LOG: " << obj << " (" << __FILE__ << ":"                     \
              << std::to_string(__LINE__) << ")" << std::endl;                 \
  } while (0)
#else
#define GENSYM_LOG(message)                                                    \
  do {                                                                         \
  } while (0)
#endif

#ifndef NO_INFO
#define GENSYM_INFO(obj)                                                       \
  do {                                                                         \
    std::cout << obj << std::endl;                                             \
  } while (0)
#else
#define GENSYM_INFO(message)                                                   \
  do {                                                                         \
  } while (0)

#endif

#if __cplusplus < 202002L
#include <string>

inline bool starts_with(const std::string &str, const std::string &prefix) {
  return str.size() >= prefix.size() &&
         std::equal(prefix.begin(), prefix.end(), str.begin());
}
#else
#include <string>
inline bool starts_with(const std::string &str, const std::string &prefix) {
  return str.starts_with(prefix);
}
#endif

inline std::monostate info() {
#ifdef DEBUG
  std::cout << std::endl;
#endif
  return std::monostate{};
}

template <typename T, typename... Args>
std::monostate info(const T &first, const Args &...args) {
#ifdef DEBUG
  std::cout << first << " ";
  info(args...);
#endif
  return std::monostate{};
}

inline std::monostate get_unit() { return std::monostate{}; }
inline std::monostate get_unit(std::monostate x) { return std::monostate{}; }

#endif // UTILS_HPP