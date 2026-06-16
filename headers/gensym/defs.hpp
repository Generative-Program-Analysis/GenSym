#ifndef GS_DEFS_HEADER
#define GS_DEFS_HEADER

using atomic_ulong = std::atomic<unsigned long int>;

inline std::mutex dt_lock;
inline duration<double, std::micro> debug_time = microseconds::zero();

using BlockLabel = int;
using Id = int;
using Addr = unsigned int;
using IntData = int64_t;
using UIntData = unsigned long long int;
using Fd = int;
using status_t = unsigned short;

// Forward declarations

class SS;
class PC;
struct Value;
struct IntV;
struct SymV;
struct LocV;
struct SymLocV;
struct FloatV;
struct ShadowV;

struct NullDerefException {
  immer::box<SS> ss;
};

extern int vararg_id;

// Default bitwidth when creating integers or symbolic values
extern unsigned int default_bw;
// The bitwidth of addresses (64 by default)
extern unsigned int addr_bw;
extern unsigned int addr_index_bw;

// Used to assign a unique ID for each SymV value
extern std::atomic<uint32_t> g_sym_id;

/* Stat */

// Number of async currently used (Deprecated)
inline atomic_ulong num_async = 0;
// Number of totoal async (Deprecated)
inline atomic_ulong tt_num_async = 0;
// Number of completed paths
inline atomic_ulong completed_path_num = 0;
// Number of queries performed for generating test cases
inline atomic_ulong generated_test_num = 0;
// Number of queries performed for checking branch satisfiability
inline atomic_ulong br_query_num = 0;
// Number of query cache hits
inline atomic_ulong cached_query_num = 0;
// Number of concretization queries
inline atomic_ulong conc_query_num = 0;
// Number of top-level symbolic constraints
inline atomic_ulong num_query_exprs = 0;
// Number of terms of all contraints
inline atomic_ulong num_total_size_query_exprs = 0;
// Number of `check_model` calls
inline atomic_ulong num_check_model = 0;
// Total sizes of check_model constraint sets
inline atomic_ulong num_check_model_pc_size = 0;

/* Global options */

extern bool use_thread_pool;
// The number of total threads (including the main thread)
extern unsigned int n_thread;
// The number of queues when using thread pool
extern unsigned int n_queue;
// Use solver or not
extern bool use_solver;
// Indicates if there is only one solver instance
extern bool use_global_solver;
// Use hash consing or not
extern bool use_hashcons;
// Use object caching or not
extern bool use_objcache;
// Use counterexample caching or not
extern bool use_cexcache;
// Use branch query caching or not
extern bool use_brcache;
// Use constraint independence resolving or not
extern bool use_cons_indep;
// Only generate testcases for states that cover new blocks or not
extern bool only_output_covernew;
// Output ktest format or not
extern bool output_ktest;
// Prefer generating human-readable file test cases
extern bool readable_file_tests;
// Only compatible when using KLEE's POSIX model at the momemt (?)
// Simulate possible failure in external functions (results in state forking)
// Currently including malloc, calloc, memalign. GW: what else?
extern bool exlib_failure_branch;
// Timeout in seconds (one hour by default)
extern unsigned int timeout;
// Print the number of executed instructions
extern bool print_inst_cnt;
// Print block/branch coverage detail at the end of execution
extern bool print_cov_detail;
// Print detailed log
// 0 - disabled
// 1 - print every second
// 2 - print at the end of execution
extern uint32_t print_detailed_log;
// The maximum size of symbolic location (used in memory read)
extern unsigned int max_sym_array_size;
// The upper bound for the number of feasible symbolic size on malloc and memcpy
extern unsigned int max_size_bound;
// Use simplification when constructing SymV values
extern bool use_symv_simplify;

// Output directory name
inline std::string output_dir_str = std::string("gensym-") + get_current_datetime();
// Test output directory name
inline std::string test_dir_str;
// Output log file name
inline std::string output_log_str;
// Output log file stream
inline std::ofstream gs_log;
// Disable output log in stdout
extern bool stdout_log;

enum class SearcherKind { randomPath, randomWeight };
// The path searcher to be used
inline SearcherKind searcher_kind = SearcherKind::randomWeight;

enum class SolverKind { z3, stp };
// The backend SMT solver to be used
inline SolverKind solver_kind = SolverKind::stp;

// External solver time (e.g. Z3, STP)
inline atomic_ulong ext_solver_time = 0;
// Internal solver time (the whole process of constraint translation/caching/solving)
inline atomic_ulong int_solver_time = 0;
// FS time: time taken to perform FS operations
inline atomic_ulong fs_time = 0;
// Time spent in solver expression construction
inline atomic_ulong cons_expr_time = 0;
// Time spent in resolving constraint independence
inline atomic_ulong cons_indep_time = 0;
// Time spent in branch query
inline atomic_ulong br_solver_time = 0;
// Time spent in if only "then" branch hits cache
inline atomic_ulong else_miss_time = 0;
// Time spent in if only "else" branch hits cache
inline atomic_ulong then_miss_time = 0;
// Time spent in if both branch miss cache
inline atomic_ulong both_miss_time = 0;
// Time spent in concretization
inline atomic_ulong conc_solver_time = 0;
// Time spent in generating test cases
inline atomic_ulong gen_test_time = 0;
// Time spent in add_constraint to the solver
inline atomic_ulong add_cons_time = 0;

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
  op_concat, op_extract, op_ite, op_bvnot, const_true, const_false
};

enum class fOP {
  op_fadd, op_fsub, op_fmul, op_fdiv,
  op_oeq, op_ogt, op_oge, op_olt, op_ole, op_one, op_ord,
  op_ueq, op_ugt, op_uge, op_ult, op_ule, op_une, op_uno,
  const_false, const_true
};

inline std::string int_op_string(iOP op) {
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
    case iOP::op_sge: return "s>=";
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
    case iOP::op_bvnot: return "bvnot";
    case iOP::const_false: return "false";
    case iOP::const_true:  return "true";
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
    case fOP::const_false: return "false";
    case fOP::const_true:  return "true";
  }
  return "unknown op";
}

#endif