#ifndef LLSC_AUX_HEADER
#define LLSC_AUX_HEADER

#define MAX(a, b) ((a) > (b)) ? (a) : (b)
#define MIN(a, b) ((a) < (b)) ? (a) : (b)

inline std::mutex dt_lock;
inline duration<double, std::micro> debug_time = microseconds::zero();

template<typename T> using List = immer::flex_vector<T>;
template<typename T> using TrList = immer::flex_vector_transient<T>;

using BlockLabel = int;
using Id = int;
using Addr = unsigned int;
using IntData = int64_t;
using UIntData = unsigned long long int;
using Fd = int;
using status_t = unsigned short;

inline int vararg_id = -1;

// Default bitwidth when creating integers or symbolic values
inline unsigned int default_bw = 32;
// The bitwidth of addresses (64 by default)
inline unsigned int addr_bw = 64;

inline unsigned int addr_index_bw = 64;

inline std::atomic<std::optional<int>> exit_code;
inline std::mutex exit_code_lock;

/* Stat */
// TODO: consider merging with monitor

// Number of async currently used (Deprecated)
inline std::atomic<unsigned int> num_async = 0;
// Number of totoal async (Deprecated)
inline std::atomic<unsigned int> tt_num_async = 0;
// Number of queries performed for generating test cases
inline unsigned int test_query_num = 0;
// Number of queries performed for checking branch satisfiability
inline unsigned int br_query_num = 0;
// Number of query cache hits
inline unsigned int cached_query_num = 0;

/* Global options */

// The number of total threads (including the main thread)
inline unsigned int n_thread = 1;
// The number of queues when using thread pool
inline unsigned int n_queue = 1;
// Use solver or not
inline bool use_solver = true;
// Indicates if there is only one solver instance
inline bool use_global_solver = false;
// Use hash consing or not
inline bool use_hashcons = true;
// Use object caching or not
inline bool use_objcache = true;
// Use counterexample caching or not
inline bool use_cexcache = true;
// Use constraint independence resolving or not
inline bool use_cons_indep = false;
// Only generate testcases for states that cover new blocks or not
inline bool only_output_covernew = false;
// Output ktest format or not
inline bool output_ktest = false;
// Simulate possible failure in external functions (results in state forking)
inline bool exlib_failure_branch = false;
// Timeout in seconds (one hour by default)
inline unsigned int timeout = 3600;
// Print the number of executed instructions
inline bool print_inst_cnt = false;
// Print block/branch coverage detail at the end of execution
inline bool print_cov_detail = false;

enum class SolverKind { z3, stp };
// The backend SMT solver to be used
inline SolverKind solver_kind = SolverKind::stp;

// External solver time (e.g. Z3, STP)
inline std::atomic<long int> ext_solver_time = 0;
// Internal solver time (the whole process of constraint translation/caching/solving)
inline std::atomic<long int> int_solver_time = 0;

// Different strategies to handle symbolic pointer index read/write
// one:       only search one feasible concrete index
// feasible:  search all feasible concrete indexes
// all:       enumerate all possible indexes (feasible or not)
enum class SymLocStrategy { one, feasible, all };

inline SymLocStrategy symloc_strategy = SymLocStrategy::all;

enum class iOP {
  op_add, op_sub, op_mul, op_sdiv, op_udiv,
  op_eq, op_uge, op_ugt, op_ule, op_ult,
  op_sge, op_sgt, op_sle, op_slt, op_neq,
  op_shl, op_lshr, op_ashr, op_and, op_or, op_xor,
  op_urem, op_srem, op_neg, op_sext, op_zext, op_trunc,
  op_concat, op_extract, op_ite
};

enum class fOP {
  op_fadd, op_fsub, op_fmul, op_fdiv,
  op_oeq, op_ogt, op_oge, op_olt, op_ole, op_one, op_ord,
  op_ueq, op_ugt, op_uge, op_ult, op_ule, op_une, op_uno,
  op_false, op_true
};

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

inline std::string int_op2string(iOP op) {
  switch (op) {
    case iOP::op_add: return "+";
    case iOP::op_sub: return "-";
    case iOP::op_mul: return "*";
    case iOP::op_sdiv: return "s/";
    case iOP::op_udiv: return "u/";
    case iOP::op_eq:  return "=";
    case iOP::op_uge: return "u>=";
    case iOP::op_ugt: return "u>";
    case iOP::op_ule: return "u<=";
    case iOP::op_ult: return "u<";
    case iOP::op_sge: return "s>e";
    case iOP::op_sgt: return "s>";
    case iOP::op_sle: return "s<=";
    case iOP::op_slt: return "s<";
    case iOP::op_neq: return "!=";
    case iOP::op_shl: return "shl";
    case iOP::op_lshr: return "lshr";
    case iOP::op_ashr: return "ashr";
    case iOP::op_and: return "/\\";
    case iOP::op_or: return "\\/";
    case iOP::op_xor: return "xor";
    case iOP::op_urem: return "u%";
    case iOP::op_srem: return "s%";
    case iOP::op_neg: return "!";
    case iOP::op_sext: return "sext";
    case iOP::op_zext: return "zext";
    case iOP::op_trunc: return "trunc";
    case iOP::op_concat: return "concat";
    case iOP::op_extract: return "extract";
    case iOP::op_ite: return "ite";
  }
  return "unknown op";
}

inline std::string float_op2string(fOP op) {
  switch (op) {
    case fOP::op_fadd:   return "+";
    case fOP::op_fsub:   return "-";
    case fOP::op_fmul:   return "*";
    case fOP::op_fdiv:   return "/";
    case fOP::op_oeq:    return "oeq";
    case fOP::op_ogt:    return "ogt";
    case fOP::op_oge:    return "oge";
    case fOP::op_olt:    return "olt";
    case fOP::op_ole:    return "ole";
    case fOP::op_one:    return "one";
    case fOP::op_ord:    return "ord";
    case fOP::op_ueq:    return "ueq";
    case fOP::op_ugt:    return "ugt";
    case fOP::op_uge:    return "uge";
    case fOP::op_ult:    return "ult";
    case fOP::op_ule:    return "ule";
    case fOP::op_une:    return "une";
    case fOP::op_uno:    return "uno";
    case fOP::op_false:  return "false";
    case fOP::op_true:   return "true";
  }
  return "unknown op";
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

/* hash */
template<typename T>
void hash_combine(size_t& seed, T const& v) {
  seed ^= std::hash<T>{}(v) + 0x9e3779b9 + (seed << 6) + (seed >> 2);
}

class Printable {
  public:
    friend std::ostream& operator<<(std::ostream& os, const Printable& p) {
      return os << p.toString();
    }
    virtual std::string toString() const = 0;
};

inline std::monostate operator+ (const std::monostate& lhs, const std::monostate& rhs) {
  return std::monostate{};
}

/* Generating fresh names */

inline std::atomic<unsigned int> var_name = 0;
inline std::string fresh(const std::string& x) { return x + std::to_string(var_name++); }

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

#endif
