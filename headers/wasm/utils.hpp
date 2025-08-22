#ifndef UTILS_HPP
#define UTILS_HPP

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

#endif // UTILS_HPP