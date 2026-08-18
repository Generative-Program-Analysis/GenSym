#ifndef GENSYM_RUNTIME_HPP
#define GENSYM_RUNTIME_HPP

#include <cstddef>
#include <cstdint>
#include <functional>
#include <iostream>
#include <iosfwd>
#include <string>
#include <utility>
#include <variant>
#include <vector>
#include <unistd.h>

namespace gensym::runtime::v1 {

inline constexpr std::uint32_t api_version = 2;

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

struct LocV { enum Kind { kStack, kHeap, kNative }; };

class Value;
class State;
class PathCondition;

using PtrVal = Value;
using SS = State;
using PC = PathCondition;
using String = std::string;
using Addr = std::uint32_t;
using BlockLabel = int;
inline constexpr BlockLabel unknown_block_id = -1;
using IntData = std::int64_t;
using UIntData = std::uint64_t;
using Args = std::vector<Value>;
using Ids = std::vector<int>;
using Cont = std::function<std::monostate(State&, Value)>;
using CPSFunc = std::monostate (*)(State&, Args, Cont);
using Block = std::function<std::monostate(State&, Cont)>;

class Value {
  void* impl_ = nullptr;
  explicit Value(void* impl) noexcept : impl_(impl) {}
  friend struct Bridge;
public:
  Value() noexcept = default;
  Value(std::nullptr_t) noexcept {}
  Value& operator=(std::nullptr_t) noexcept { impl_ = nullptr; return *this; }
  explicit operator bool() const noexcept { return impl_ != nullptr; }
  bool operator==(std::nullptr_t) const noexcept { return impl_ == nullptr; }
  bool operator!=(std::nullptr_t) const noexcept { return impl_ != nullptr; }
  Value* operator->() noexcept { return this; }
  const Value* operator->() const noexcept { return this; }
  Value& operator*() noexcept { return *this; }
  const Value& operator*() const noexcept { return *this; }

  bool is_conc() const;
  std::size_t get_bw() const;
  Args to_bytes() const;
  Args to_bytes_shadow() const;
  static Value from_bytes(const Args&);
  static Value from_bytes_shadow(const Args&);
};

std::ostream& operator<<(std::ostream&, const Value&);

class PathCondition {
  void* impl_ = nullptr;
  explicit PathCondition(void* impl) noexcept : impl_(impl) {}
  friend struct Bridge;
  friend class State;
public:
  PathCondition();
  PathCondition(const PathCondition&);
  PathCondition(PathCondition&&) noexcept;
  PathCondition& operator=(const PathCondition&);
  PathCondition& operator=(PathCondition&&) noexcept;
  ~PathCondition();
  PathCondition& add(Value);
};

class State {
  void* impl_ = nullptr;
  bool owned_ = false;
  State(void* impl, bool owned) noexcept : impl_(impl), owned_(owned) {}
  friend struct Bridge;
public:
  State();
  State(const State&);
  State(State&&) noexcept;
  State& operator=(const State&);
  State& operator=(State&&) noexcept;
  ~State();

  State fork();
  State copy() const;
  std::uint64_t get_ssid() const;
  BlockLabel current_block() const;
  int incoming_block() const;
  Value env_lookup(int);
  std::size_t heap_size() const;
  std::size_t stack_size() const;
  Value at(Value, std::size_t);
  Value at_struct(Value, int);
  Args at_seq(Value, int);
  Value heap_lookup(std::size_t);
  State& alloc_stack(std::size_t);
  State& alloc_heap(std::size_t);
  State& update(Value, Value);
  State& update(Value, Value, std::size_t);
  State& update_seq(Value, const Args&);
  State& push();
  State& push(Cont);
  Cont pop(std::size_t);
  State& assign(int, Value);
  State& assign_seq(const Ids&, const Args&);
  State& heap_append(const Args&);
  State& add_PC(Value);
  PathCondition get_PC() const;
  PathCondition copy_PC() const;
  State& add_incoming_block(int);
  State& cover_block(int);
  State& init_arg();
  State& init_error_loc();
  Value error_loc();
};

struct ProgramConfig {
  std::size_t block_count = 0;
  std::vector<std::pair<unsigned, unsigned>> branch_arity;
  std::vector<std::vector<std::uint64_t>> block_successors;
  bool symbolic_uninitialized = false;
  bool debug = false;
};

class Coverage {
public:
  void set_num_blocks(std::size_t);
  void extend_blocks(std::size_t, const std::vector<std::pair<unsigned, unsigned>>&,
                     const std::vector<std::vector<std::uint64_t>>& = {});
  void inc_block(std::size_t);
  void inc_branch(std::size_t, std::size_t);
  void inc_path(std::size_t);
  void inc_inst(std::size_t);
  void start_monitor();
  void print_block_cov();
  void print_time();
  void print_path_cov();
};

Coverage& cov();
bool debug_enabled();
void configure(const ProgramConfig&);
void prelude(int argc, char** argv, const ProgramConfig&);
void epilogue();
int runtime_exit_code();
bool can_par_tp();
void add_task(std::uint64_t, BlockLabel, std::function<std::monostate()>);

State make_initial_state(const Args& heap = {});
extern Value g_argc;
extern Value g_argv;
Value make_IntV(std::int64_t, std::size_t = 32, bool = true);
Value make_FloatV(double, std::size_t);
Value make_FloatV_fp80(const std::vector<std::uint8_t>&);
Value make_LocV(std::uint32_t, LocV::Kind, std::size_t, std::size_t = 0);
Value make_LocV_null();
Value make_SymV(const std::string&, std::size_t);
Value make_SymLocV(std::uint32_t, LocV::Kind, std::size_t, Value);
Value make_ShadowV();
Value make_ShadowV(std::int8_t);
std::int64_t proj_IntV(Value);
Value int_op_1(iOP, Value);
Value int_op_2(iOP, Value, Value);
Value int_op_3(iOP, Value, Value, Value);
Value float_op_2(fOP, Value, Value);
Value bv_sext(Value, std::size_t);
Value bv_zext(Value, std::size_t);
Value fp_toui(Value, std::size_t);
Value fp_tosi(Value, std::size_t);
Value ui_tofp(Value);
Value si_tofp(Value);
Value trunc(Value, int, int);
Value ite(Value, Value, Value);
Value ptr_add(Value, Value);
Value structV_at(Value, std::size_t);

Value make_CPSFunV(CPSFunc);
std::monostate cps_apply(Value, State, Args, Cont);
std::monostate cont_apply(Cont, State&, Value);
std::monostate sym_exec_br_k(State&, unsigned, Value, Value, BlockLabel, BlockLabel, Block, Block, Cont);
std::vector<std::pair<State, Value>> array_lookup(State&, Value, Value, std::size_t);
std::monostate array_lookup_k(State&, Value, Value, std::size_t, Cont);
bool check_pc(PathCondition);
void check_pc_to_file(const State&);

std::int64_t get_int_arg(State&, Value);
double get_float_arg(State&, Value);
void* get_pointer_arg(State&, Value);
void writeback_pointer_arg(State&, Value, void*);

#define GENSYM_RUNTIME_EXTERNAL(name) \
  std::monostate name(State&, Args, Cont)
GENSYM_RUNTIME_EXTERNAL(stop);
GENSYM_RUNTIME_EXTERNAL(noop);
GENSYM_RUNTIME_EXTERNAL(_exit);
GENSYM_RUNTIME_EXTERNAL(exit);
GENSYM_RUNTIME_EXTERNAL(abort);
GENSYM_RUNTIME_EXTERNAL(sym_exit);
GENSYM_RUNTIME_EXTERNAL(print_string);
GENSYM_RUNTIME_EXTERNAL(sym_print);
GENSYM_RUNTIME_EXTERNAL(gs_assert);
GENSYM_RUNTIME_EXTERNAL(gs_assert_eager);
GENSYM_RUNTIME_EXTERNAL(__assert_fail);
GENSYM_RUNTIME_EXTERNAL(llvm_va_start);
GENSYM_RUNTIME_EXTERNAL(llvm_va_end);
GENSYM_RUNTIME_EXTERNAL(llvm_va_copy);
GENSYM_RUNTIME_EXTERNAL(gs_assume);
GENSYM_RUNTIME_EXTERNAL(gs_is_symbolic);
GENSYM_RUNTIME_EXTERNAL(gs_get_valuel);
GENSYM_RUNTIME_EXTERNAL(getpagesize);
GENSYM_RUNTIME_EXTERNAL(gs_prefer_cex);
GENSYM_RUNTIME_EXTERNAL(gs_posix_prefer_cex);
GENSYM_RUNTIME_EXTERNAL(gs_warning_once);
GENSYM_RUNTIME_EXTERNAL(make_symbolic);
GENSYM_RUNTIME_EXTERNAL(make_symbolic_whole);
GENSYM_RUNTIME_EXTERNAL(malloc);
GENSYM_RUNTIME_EXTERNAL(memalign);
GENSYM_RUNTIME_EXTERNAL(calloc);
GENSYM_RUNTIME_EXTERNAL(realloc);
GENSYM_RUNTIME_EXTERNAL(reallocarray);
GENSYM_RUNTIME_EXTERNAL(llvm_memcpy);
GENSYM_RUNTIME_EXTERNAL(llvm_memmove);
GENSYM_RUNTIME_EXTERNAL(llvm_memset);
GENSYM_RUNTIME_EXTERNAL(syscall);
GENSYM_RUNTIME_EXTERNAL(__errno_location);
GENSYM_RUNTIME_EXTERNAL(syscall_open);
GENSYM_RUNTIME_EXTERNAL(syscall_close);
GENSYM_RUNTIME_EXTERNAL(syscall_read);
GENSYM_RUNTIME_EXTERNAL(syscall_write);
GENSYM_RUNTIME_EXTERNAL(syscall_lseek);
GENSYM_RUNTIME_EXTERNAL(syscall_lseek64);
GENSYM_RUNTIME_EXTERNAL(syscall_stat);
GENSYM_RUNTIME_EXTERNAL(syscall_fstat);
GENSYM_RUNTIME_EXTERNAL(syscall_lstat);
GENSYM_RUNTIME_EXTERNAL(syscall_statfs);
GENSYM_RUNTIME_EXTERNAL(syscall_mkdir);
GENSYM_RUNTIME_EXTERNAL(syscall_rmdir);
GENSYM_RUNTIME_EXTERNAL(syscall_creat);
GENSYM_RUNTIME_EXTERNAL(syscall_unlink);
GENSYM_RUNTIME_EXTERNAL(syscall_chmod);
GENSYM_RUNTIME_EXTERNAL(syscall_chown);
GENSYM_RUNTIME_EXTERNAL(syscall_ioctl);
GENSYM_RUNTIME_EXTERNAL(syscall_fcntl);
#undef GENSYM_RUNTIME_EXTERNAL

Value make_symbolic_det(State&, Args);
Value make_symbolic_whole_det(State&, Args);

} // namespace gensym::runtime::v1

#ifndef INFO
#define INFO(message) do { if (::gensym::runtime::v1::debug_enabled()) { std::cout << "[Info] " << message << std::endl; } } while (false)
#endif

#endif
