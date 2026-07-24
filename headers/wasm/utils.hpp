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

enum class GensymHeapStatus { Allocated, Freed };

struct GensymHeapRecord {
  int32_t size;
  GensymHeapStatus status;
};

inline std::unordered_map<int32_t, GensymHeapRecord> GENSYM_HEAP_RECORDS;

inline bool GENSYM_IS_IN_ALLOCATED_RANGE(int32_t addr, size_t width) {
  const int64_t start = addr;
  const int64_t end = start + static_cast<int64_t>(width);
  for (const auto &[base, record] : GENSYM_HEAP_RECORDS) {
    if (record.status != GensymHeapStatus::Allocated) {
      continue;
    }
    const int64_t alloc_start = base;
    const int64_t alloc_end = alloc_start + static_cast<int64_t>(record.size);
    if (alloc_start <= start && end <= alloc_end) {
      return true;
    }
  }
  std::cout << "Address " << addr << " with width " << width
            << " is not in any allocated range." << std::endl;
  return false;
}

inline bool GENSYM_SHOULD_CHECK_ALLOCATION(int32_t addr) {
  if (GENSYM_HEAP_RECORDS.empty()) {
    return false;
  }
  int32_t heap_base = std::numeric_limits<int32_t>::max();
  for (const auto &[base, _] : GENSYM_HEAP_RECORDS) {
    heap_base = std::min(heap_base, base);
  }
  return addr >= heap_base;
}

inline void GENSYM_ASSERT_ADDR_ALLOCATED(int32_t addr, size_t width) {
  if (!ENABLE_BUG_FINDING)
    return;
  if (!GENSYM_SHOULD_CHECK_ALLOCATION(addr)) {
    return;
  }
  GENSYM_ASSERT(GENSYM_IS_IN_ALLOCATED_RANGE(addr, width));
}

inline int32_t GENSYM_ALLOC(int32_t base, int32_t size) {
  // std::cout << "Allocating memory at address " << base << " with size " <<
  // size
  //           << std::endl;
  GENSYM_ASSERT(base >= 0);
  GENSYM_ASSERT(size >= 0);
  GENSYM_HEAP_RECORDS[base] =
      GensymHeapRecord{size, GensymHeapStatus::Allocated};
  return base;
}

inline std::monostate GENSYM_FREE(int32_t ptr) {
  // std::cout << "Freeing memory at address " << ptr << std::endl;
  GENSYM_ASSERT(ptr >= 0);
  auto it = GENSYM_HEAP_RECORDS.find(ptr);
  GENSYM_ASSERT(it != GENSYM_HEAP_RECORDS.end());
  it->second.status = GensymHeapStatus::Freed;
  return std::monostate{};
}

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
#ifdef DEBUG
  print_infos(first, args...);
#endif
  return std::monostate{};
}

constexpr const char *DEBUG_OPTS_ENV_VAR = "GENSYM_DEBUG";

template <typename... Args>
std::monostate infoWhen(const char *dbg_option, const Args &...args) {
#ifdef DEBUGWHEN
  const char *env_value = std::getenv(DEBUG_OPTS_ENV_VAR);
  if (env_value && std::string(env_value).find(std::string(dbg_option)) !=
                       std::string::npos) {
    print_infos(args...);
  }
#endif
  return std::monostate{};
}

inline std::monostate get_unit() { return std::monostate{}; }
inline std::monostate get_unit(std::monostate x) { return std::monostate{}; }

#endif // UTILS_HPP
