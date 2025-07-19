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

#endif // UTILS_HPP