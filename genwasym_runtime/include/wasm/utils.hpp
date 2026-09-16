#ifndef UTILS_HPP
#define UTILS_HPP
#include "config.hpp"
#include <cstddef>
#include <cstdint>
#include <iostream>
#include <limits>
#include <unordered_map>
#include <variant>

#ifndef GENSYM_ASSERT
#define GENSYM_ASSERT(condition)                                               \
  do {                                                                         \
    if (!(condition)) {                                                        \
      std::string message = std::string("Assertion failed: ") + " (" +         \
                            __FILE__ + ":" + std::to_string(__LINE__) + ")";   \
      if (SOFT_ASSERT) {                                                       \
        GENSYM_INFO(message);                                                  \
      } else {                                                                 \
        throw std::runtime_error(message);                                     \
      }                                                                        \
    }                                                                          \
  } while (0)
#endif

#define GENSYM_DBG(obj)                                                        \
  do {                                                                         \
    if (DBG_ENABLED) {                                                        \
      std::cout << "LOG: " << obj << " (" << __FILE__ << ":"                   \
                << std::to_string(__LINE__) << ")" << std::endl;               \
    }                                                                          \
  } while (0)

#define GENSYM_INFO(obj)                                                       \
  do {                                                                         \
    if (INFO_ENABLED) {                                                       \
      std::cout << obj << std::endl;                                           \
    }                                                                          \
  } while (0)

enum class GensymHeapStatus { Allocated, Freed };

struct GensymHeapRecord {
  int32_t size;
  GensymHeapStatus status;
};

extern std::unordered_map<int32_t, GensymHeapRecord> GENSYM_HEAP_RECORDS;

bool GENSYM_IS_IN_ALLOCATED_RANGE(int32_t addr, size_t width);

bool GENSYM_SHOULD_CHECK_ALLOCATION(int32_t addr);

void GENSYM_ASSERT_ADDR_ALLOCATED(int32_t addr, size_t width);

int32_t GENSYM_ALLOC(int32_t base, int32_t size);

std::monostate GENSYM_FREE(int32_t ptr);

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

inline std::monostate print_infos() {
  std::cout << std::endl;
  return std::monostate{};
}

template <typename T, typename... Args>
std::monostate print_infos(const T &first, const Args &...args) {
  std::cout << first << " ";
  print_infos(args...);
  return std::monostate{};
}

template <typename T, typename... Args>
std::monostate info(const T &first, const Args &...args) {
  if (DEBUG_ENABLED) {
    print_infos(first, args...);
  }
  return std::monostate{};
}

constexpr const char *DEBUG_OPTS_ENV_VAR = "GENSYM_DEBUG";

template <typename... Args>
std::monostate infoWhen(const char *dbg_option, const Args &...args) {
  if (DEBUG_WHEN_ENABLED) {
    const char *env_value = std::getenv(DEBUG_OPTS_ENV_VAR);
    if (env_value && std::string(env_value).find(std::string(dbg_option)) !=
                         std::string::npos) {
      print_infos(args...);
    }
  }
  return std::monostate{};
}

inline std::monostate get_unit() { return std::monostate{}; }
inline std::monostate get_unit(std::monostate x) { return std::monostate{}; }

#endif // UTILS_HPP
