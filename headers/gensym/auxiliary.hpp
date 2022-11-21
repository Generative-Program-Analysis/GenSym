#ifndef GS_AUX_HEADER
#define GS_AUX_HEADER

#   define ABORT(message) \
    do { \
      std::cerr << "Abort at " << __FILE__ << " line " << __LINE__ \
                << ": " << message << std::endl; \
      exit(-1); \
    } while (false)


#ifndef NO_ASSERT
#   define ASSERT(condition, message) \
    do { \
      if (! (condition)) { \
        std::cerr << "Assertion `" #condition "` failed in " << __FILE__ \
                  << " line " << __LINE__ << ": " << message << std::endl; \
        std::terminate(); \
      } \
    } while (false)
#else
#   define ASSERT(condition, message) do { } while (false)
#endif

#ifdef DEBUG
#   define INFO(message) \
    do { \
      std::cout << "[Info] " << __FILE__ << " line " << __LINE__ \
                << ": " << message << std::endl; \
    } while (false)
#else
#   define INFO(message) do { } while (false)
#endif

#define MAX(a, b) ((a) > (b)) ? (a) : (b)
#define MIN(a, b) ((a) < (b)) ? (a) : (b)

template<typename T> using Ptr = std::shared_ptr<T>;

/* Date & Time */

using namespace std::chrono;

inline std::string get_current_datetime() {
  auto t = std::time(nullptr);
  auto tm = *std::localtime(&t);
  std::ostringstream oss;
  oss << std::put_time(&tm, "%d%m%Y-%H%M%S");
  return oss.str();
}

/* Exit code */

inline std::atomic<std::optional<int>> exit_code = {};
inline std::mutex exit_code_lock;

// Note: set_exit_code preserves the first value it sets.
// In execution with thread_pool, this means other paths
// invoking it later will be discarded and have no effect
// on `main`'s return code.
inline void set_exit_code(int code) {
  const std::scoped_lock lock(exit_code_lock);
  if (!exit_code.load().has_value()) {
    exit_code.store(std::make_optional<int>(code));
  }
}

/* Stack manipulation */

#define STACKSIZE_16MB (16 * 1024 * 1024)
#define STACKSIZE_32MB (32 * 1024 * 1024)
#define STACKSIZE_64MB (64 * 1024 * 1024)
#define STACKSIZE_128MB (128 * 1024 * 1024)
#define STACKSIZE_1GB (128 * 1024 * 1024 * 8)

inline void inc_stack(rlim_t lim) {
  struct rlimit rl;
  int result;

  result = getrlimit(RLIMIT_STACK, &rl);
  if (result == 0) {
    if (rl.rlim_cur < lim) {
      rl.rlim_cur = lim;
      result = setrlimit(RLIMIT_STACK, &rl);
      if (result != 0) {
        fprintf(stderr, "setrlimit returned result = %d\n", result);
      }
    }
  }
}

/* Combining hashes */

template<typename T>
void hash_combine(size_t& seed, T const& v) {
  seed ^= std::hash<T>{}(v) + 0x9e3779b9 + (seed << 6) + (seed >> 2);
}

/* Generating fresh names */

inline std::atomic<unsigned int> var_counter = 0;
inline std::string fresh(const std::string x) { return x + std::to_string(var_counter++); }

class SymObj : public Printable {
public:
  std::string name;
  size_t size;
  bool is_whole;
  SymObj(std::string name, size_t size, bool is_whole) : name(name), size(size), is_whole(is_whole) {}
  std::string toString() const override {
    std::ostringstream ss;
    ss << "SymObj(" << name << ", " << size << ", " << is_whole << ")";
    return ss.str();
  }
};

/* Random number generator */

inline std::mt19937 rng32;

inline void init_rand() {
  unsigned seed1 = std::chrono::system_clock::now().time_since_epoch().count();
  rng32 = std::mt19937(seed1);
}

inline uint32_t rand_uint32() {
  return rng32();
}

inline int rand_int(int ub) {
  int r =  (rng32() % ub) + 1; // [1, ub]
#ifdef DEBUG
  std::cout << "Generate a rand number: " << r << std::endl;
#endif
  return r;
}

/* Others */

inline std::monostate operator+ (const std::monostate& lhs, const std::monostate& rhs) {
  return std::monostate{};
}

template<class... Ts> struct overloaded : Ts... { using Ts::operator()...; };
template<class... Ts> overloaded(Ts...) -> overloaded<Ts...>;

template<typename T, class... Types>
inline bool isInstanceOf(const std::variant<Types...>& v) {
  return std::holds_alternative<T>(v);
}

#endif
