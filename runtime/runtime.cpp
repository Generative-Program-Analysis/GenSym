#include <gensym/runtime.hpp>

#include <gensym.hpp>

namespace gensym::runtime::v1 {

struct Bridge {
  static ::PtrVal unwrap(Value v) { return ::PtrVal(static_cast<::Value*>(v.impl_)); }
  static Value wrap(::PtrVal v) { return Value(v.get()); }
  static ::SS& unwrap(State& s) { return *static_cast<::SS*>(s.impl_); }
  static const ::SS& unwrap(const State& s) { return *static_cast<const ::SS*>(s.impl_); }
  static State own(::SS s) { return State(new ::SS(std::move(s)), true); }
  static State borrow(::SS& s) { return State(&s, false); }
  static ::PC& unwrap(PathCondition& pc) { return *static_cast<::PC*>(pc.impl_); }
  static const ::PC& unwrap(const PathCondition& pc) { return *static_cast<const ::PC*>(pc.impl_); }
  static PathCondition own(::PC pc) { return PathCondition(new ::PC(std::move(pc))); }
};

static ::List<::PtrVal> unwrap_args(const Args& args) {
  auto out = ::List<::PtrVal>{}.transient();
  for (auto value : args) out.push_back(Bridge::unwrap(value));
  return out.persistent();
}

static Args wrap_args(const ::List<::PtrVal>& args) {
  Args out;
  out.reserve(args.size());
  for (auto value : args) out.push_back(Bridge::wrap(value));
  return out;
}

static ::Cont unwrap_cont(Cont cont) {
  return [cont = std::move(cont)](::SS& state, ::PtrVal value) mutable {
    auto view = Bridge::borrow(state);
    return cont(view, Bridge::wrap(value));
  };
}

static Cont wrap_cont(::Cont cont) {
  return [cont = std::move(cont)](State& state, Value value) mutable {
    return cont(Bridge::unwrap(state), Bridge::unwrap(value));
  };
}

bool Value::is_conc() const { return Bridge::unwrap(*this)->is_conc(); }
std::size_t Value::get_bw() const { return Bridge::unwrap(*this)->get_bw(); }
Args Value::to_bytes() const { return wrap_args(Bridge::unwrap(*this)->to_bytes()); }
Args Value::to_bytes_shadow() const { return wrap_args(Bridge::unwrap(*this)->to_bytes_shadow()); }
Value Value::from_bytes(const Args& values) { return Bridge::wrap(::Value::from_bytes(unwrap_args(values))); }
Value Value::from_bytes_shadow(const Args& values) { return Bridge::wrap(::Value::from_bytes_shadow(unwrap_args(values))); }
std::ostream& operator<<(std::ostream& out, const Value& value) {
  if (!value) return out << "nullptr";
  return out << Bridge::unwrap(value)->toString();
}

PathCondition::PathCondition() : impl_(new ::PC(::mt_pc)) {}
PathCondition::PathCondition(const PathCondition& rhs) : impl_(new ::PC(Bridge::unwrap(rhs))) {}
PathCondition::PathCondition(PathCondition&& rhs) noexcept : impl_(rhs.impl_) { rhs.impl_ = nullptr; }
PathCondition& PathCondition::operator=(const PathCondition& rhs) {
  if (this != &rhs) {
    if (impl_) *static_cast<::PC*>(impl_) = Bridge::unwrap(rhs);
    else impl_ = new ::PC(Bridge::unwrap(rhs));
  }
  return *this;
}
PathCondition& PathCondition::operator=(PathCondition&& rhs) noexcept {
  if (this != &rhs) { delete static_cast<::PC*>(impl_); impl_ = rhs.impl_; rhs.impl_ = nullptr; }
  return *this;
}
PathCondition::~PathCondition() { delete static_cast<::PC*>(impl_); }
PathCondition& PathCondition::add(Value value) { Bridge::unwrap(*this).add(Bridge::unwrap(value)); return *this; }

State::State() : impl_(new ::SS(::mt_ss)), owned_(true) {}
State::State(const State& rhs) : impl_(new ::SS(Bridge::unwrap(rhs))), owned_(true) {}
State::State(State&& rhs) noexcept : impl_(rhs.impl_), owned_(rhs.owned_) { rhs.impl_ = nullptr; rhs.owned_ = false; }
State& State::operator=(const State& rhs) {
  if (this != &rhs) {
    if (owned_) delete static_cast<::SS*>(impl_);
    impl_ = new ::SS(Bridge::unwrap(rhs)); owned_ = true;
  }
  return *this;
}
State& State::operator=(State&& rhs) noexcept {
  if (this != &rhs) {
    if (owned_) delete static_cast<::SS*>(impl_);
    impl_ = rhs.impl_; owned_ = rhs.owned_; rhs.impl_ = nullptr; rhs.owned_ = false;
  }
  return *this;
}
State::~State() { if (owned_) delete static_cast<::SS*>(impl_); }
State State::fork() { return Bridge::own(Bridge::unwrap(*this).fork()); }
State State::copy() const { return State(*this); }
std::uint64_t State::get_ssid() const { return const_cast<::SS&>(Bridge::unwrap(*this)).get_ssid(); }
BlockLabel State::current_block() const { return const_cast<::SS&>(Bridge::unwrap(*this)).current_block(); }
int State::incoming_block() const { return const_cast<::SS&>(Bridge::unwrap(*this)).incoming_block(); }
Value State::env_lookup(int id) { return Bridge::wrap(Bridge::unwrap(*this).env_lookup(id)); }
std::size_t State::heap_size() const { return const_cast<::SS&>(Bridge::unwrap(*this)).heap_size(); }
std::size_t State::stack_size() const { return const_cast<::SS&>(Bridge::unwrap(*this)).stack_size(); }
Value State::at(Value address, std::size_t size) { return Bridge::wrap(Bridge::unwrap(*this).at(Bridge::unwrap(address), size)); }
Value State::at_struct(Value address, int size) { return Bridge::wrap(Bridge::unwrap(*this).at_struct(Bridge::unwrap(address), size)); }
Args State::at_seq(Value address, int size) { return wrap_args(Bridge::unwrap(*this).at_seq(Bridge::unwrap(address), size)); }
Value State::heap_lookup(std::size_t address) { return Bridge::wrap(Bridge::unwrap(*this).heap_lookup(address)); }
State& State::alloc_stack(std::size_t size) { Bridge::unwrap(*this).alloc_stack(size); return *this; }
State& State::alloc_heap(std::size_t size) { Bridge::unwrap(*this).alloc_heap(size); return *this; }
State& State::update(Value address, Value value) { Bridge::unwrap(*this).update(Bridge::unwrap(address), Bridge::unwrap(value), (value.get_bw() + 7) / 8); return *this; }
State& State::update(Value address, Value value, std::size_t size) { Bridge::unwrap(*this).update(Bridge::unwrap(address), Bridge::unwrap(value), size); return *this; }
State& State::update_seq(Value address, const Args& values) { Bridge::unwrap(*this).update_seq(Bridge::unwrap(address), unwrap_args(values)); return *this; }
State& State::push() { Bridge::unwrap(*this).push(); return *this; }
State& State::push(Cont cont) { Bridge::unwrap(*this).push(unwrap_cont(std::move(cont))); return *this; }
Cont State::pop(std::size_t keep) { return wrap_cont(Bridge::unwrap(*this).pop(keep)); }
State& State::assign(int id, Value value) { Bridge::unwrap(*this).assign(id, Bridge::unwrap(value)); return *this; }
State& State::assign_seq(const Ids& ids, const Args& values) {
  auto iids = ::List<::Id>(ids.begin(), ids.end());
  Bridge::unwrap(*this).assign_seq(std::move(iids), unwrap_args(values)); return *this;
}
State& State::heap_append(const Args& values) { Bridge::unwrap(*this).heap_append(unwrap_args(values)); return *this; }
State& State::add_PC(Value value) { Bridge::unwrap(*this).add_PC(Bridge::unwrap(value)); return *this; }
PathCondition State::get_PC() const { return Bridge::own(const_cast<::SS&>(Bridge::unwrap(*this)).copy_PC()); }
PathCondition State::copy_PC() const { return get_PC(); }
State& State::add_incoming_block(int block) { Bridge::unwrap(*this).add_incoming_block(block); return *this; }
State& State::cover_block(int block) { Bridge::unwrap(*this).cover_block(block); return *this; }
State& State::init_arg() { Bridge::unwrap(*this).init_arg(); return *this; }
State& State::init_error_loc() { Bridge::unwrap(*this).init_error_loc(); return *this; }
Value State::error_loc() { return Bridge::wrap(Bridge::unwrap(*this).error_loc()); }

static Coverage public_coverage;
Coverage& cov() { return public_coverage; }
bool debug_enabled() { return ::runtime_debug; }
void Coverage::set_num_blocks(std::size_t n) { ::cov().extend_blocks(n, {}); }
void Coverage::extend_blocks(std::size_t n, const std::vector<std::pair<unsigned, unsigned>>& branches,
                             const std::vector<std::vector<std::uint64_t>>& successors) {
  ::cov().extend_blocks(n, branches, successors);
}
void Coverage::inc_block(std::size_t id) { ::cov().inc_block(id); }
void Coverage::inc_branch(std::size_t id, std::size_t branch) { ::cov().inc_branch(id, branch); }
void Coverage::inc_path(std::size_t n) { ::cov().inc_path(n); }
void Coverage::inc_inst(std::size_t n) { ::cov().inc_inst(n); }
void Coverage::start_monitor() { ::cov().start_monitor(); }
void Coverage::print_block_cov() { ::cov().print_block_cov(std::cout); }
void Coverage::print_time() { ::cov().print_time(false, std::cout); }
void Coverage::print_path_cov() { ::cov().print_path_cov(std::cout); }

void configure(const ProgramConfig& config) {
  ::symbolic_uninit = config.symbolic_uninitialized;
  ::runtime_debug = config.debug;
  ::cov().extend_blocks(config.block_count, config.branch_arity, config.block_successors);
}
Value g_argc;
Value g_argv;
void prelude(int argc, char** argv, const ProgramConfig& config) {
  configure(config);
  ::prelude(argc, argv);
  g_argc = Bridge::wrap(::g_argc);
  g_argv = Bridge::wrap(::g_argv);
}
void epilogue() { ::epilogue(); }
int runtime_exit_code() { return ::exit_code.load().value_or(0); }
bool can_par_tp() { return ::can_par_tp(); }
void add_task(std::uint64_t id, BlockLabel block, std::function<std::monostate()> task) {
  ::tp.add_task(id, block, std::move(task));
}

State make_initial_state(const Args& heap) {
  if (heap.empty()) return Bridge::own(::mt_ss);
  return Bridge::own(::SS(unwrap_args(heap), ::mt_stack, ::mt_pc, ::mt_meta));
}
Value make_IntV(std::int64_t value, std::size_t bw, bool msb) { return Bridge::wrap(::make_IntV(value, bw, msb)); }
Value make_FloatV(double value, std::size_t bw) { return Bridge::wrap(::make_FloatV(value, bw)); }
Value make_FloatV_fp80(const std::vector<std::uint8_t>& bytes) {
  std::array<unsigned char, 10> data{};
  std::copy_n(bytes.begin(), std::min(bytes.size(), data.size()), data.begin());
  return Bridge::wrap(::make_FloatV_fp80(data));
}
static ::LocV::Kind unwrap_kind(LocV::Kind kind) { return static_cast<::LocV::Kind>(kind); }
Value make_LocV(std::uint32_t base, LocV::Kind kind, std::size_t size, std::size_t off) { return Bridge::wrap(::make_LocV(base, unwrap_kind(kind), size, off)); }
Value make_LocV_null() { return Bridge::wrap(::make_LocV_null()); }
Value make_SymV(const std::string& name, std::size_t bw) { return Bridge::wrap(::make_SymV(name, bw)); }
Value make_SymLocV(std::uint32_t base, LocV::Kind kind, std::size_t size, Value off) { return Bridge::wrap(::make_SymLocV(base, unwrap_kind(kind), size, Bridge::unwrap(off))); }
Value make_ShadowV() { return Bridge::wrap(::make_ShadowV()); }
Value make_ShadowV(std::int8_t off) { return Bridge::wrap(::make_ShadowV(off)); }
std::int64_t proj_IntV(Value value) { return ::proj_IntV(Bridge::unwrap(value)); }
Value int_op_1(iOP op, Value value) { return Bridge::wrap(::int_op_1(static_cast<::iOP>(op), Bridge::unwrap(value))); }
Value int_op_2(iOP op, Value lhs, Value rhs) { return Bridge::wrap(::int_op_2(static_cast<::iOP>(op), Bridge::unwrap(lhs), Bridge::unwrap(rhs))); }
Value int_op_3(iOP op, Value a, Value b, Value c) { return Bridge::wrap(::int_op_3(static_cast<::iOP>(op), Bridge::unwrap(a), Bridge::unwrap(b), Bridge::unwrap(c))); }
Value float_op_2(fOP op, Value lhs, Value rhs) { return Bridge::wrap(::float_op_2(static_cast<::fOP>(op), Bridge::unwrap(lhs), Bridge::unwrap(rhs))); }
Value bv_sext(Value value, std::size_t bw) { return Bridge::wrap(::bv_sext(Bridge::unwrap(value), bw)); }
Value bv_zext(Value value, std::size_t bw) { return Bridge::wrap(::bv_zext(Bridge::unwrap(value), bw)); }
Value fp_toui(Value value, std::size_t bw) { return Bridge::wrap(::fp_toui(Bridge::unwrap(value), bw)); }
Value fp_tosi(Value value, std::size_t bw) { return Bridge::wrap(::fp_tosi(Bridge::unwrap(value), bw)); }
Value ui_tofp(Value value) { return Bridge::wrap(::ui_tofp(Bridge::unwrap(value))); }
Value si_tofp(Value value) { return Bridge::wrap(::si_tofp(Bridge::unwrap(value))); }
Value trunc(Value value, int from, int to) { return Bridge::wrap(::trunc(Bridge::unwrap(value), from, to)); }
Value ite(Value condition, Value then_value, Value else_value) { return Bridge::wrap(::ite(Bridge::unwrap(condition), Bridge::unwrap(then_value), Bridge::unwrap(else_value))); }
Value ptr_add(Value pointer, Value offset) { return Bridge::wrap(::ptr_add(Bridge::unwrap(pointer), Bridge::unwrap(offset))); }
Value structV_at(Value value, std::size_t index) { return Bridge::wrap(::structV_at(Bridge::unwrap(value), static_cast<int>(index))); }

struct PublicCPSValue final : ::LocV {
  CPSFunc function;
  explicit PublicCPSValue(CPSFunc f)
      : ::LocV(static_cast<::Addr>(reinterpret_cast<std::intptr_t>(f)), ::LocV::kNative, 1, 0), function(f) {
    ASSERT(f, "public CPS function cannot be null");
  }
  bool compare(const ::Value* value) const override {
    auto* rhs = dynamic_cast<const PublicCPSValue*>(value);
    return rhs && function == rhs->function;
  }
  std::string toString() const override { return "PublicCPSFunV"; }
};

Value make_CPSFunV(CPSFunc function) { return Bridge::wrap(::PtrVal(new PublicCPSValue(function))); }
std::monostate cps_apply(Value value, State state, Args args, Cont cont) {
  auto* function = dynamic_cast<PublicCPSValue*>(Bridge::unwrap(value).get());
  ASSERT(function, "cps_apply: not a public CPS function value");
  return function->function(state, std::move(args), std::move(cont));
}
std::monostate cont_apply(Cont cont, State& state, Value value) { return cont(state, value); }

static std::function<std::monostate(::SS&, ::Cont)> unwrap_block(Block block) {
  return [block = std::move(block)](::SS& state, ::Cont cont) mutable {
    auto view = Bridge::borrow(state);
    return block(view, wrap_cont(std::move(cont)));
  };
}
std::monostate sym_exec_br_k(State& state, unsigned id, Value t, Value f,
                             BlockLabel t_id, BlockLabel f_id, Block tb, Block fb, Cont cont) {
  return ::sym_exec_br_k(Bridge::unwrap(state), id, Bridge::unwrap(t), Bridge::unwrap(f), t_id, f_id,
                         unwrap_block(std::move(tb)), unwrap_block(std::move(fb)), unwrap_cont(std::move(cont)));
}
std::vector<std::pair<State, Value>> array_lookup(State& state, Value base, Value offset, std::size_t size) {
  auto result = ::array_lookup(Bridge::unwrap(state), Bridge::unwrap(base), Bridge::unwrap(offset), size);
  std::vector<std::pair<State, Value>> out;
  out.reserve(result.size());
  for (auto& [s, v] : result) out.emplace_back(Bridge::own(std::move(s)), Bridge::wrap(v));
  return out;
}
std::monostate array_lookup_k(State& state, Value base, Value offset, std::size_t size, Cont cont) {
  return ::array_lookup_k(Bridge::unwrap(state), Bridge::unwrap(base), Bridge::unwrap(offset), size, unwrap_cont(std::move(cont)));
}
bool check_pc(PathCondition pc) { return ::check_pc(Bridge::unwrap(pc)); }
void check_pc_to_file(const State& state) { ::check_pc_to_file(Bridge::unwrap(state)); }

std::int64_t get_int_arg(State& state, Value value) { return ::get_int_arg(Bridge::unwrap(state), Bridge::unwrap(value)); }
double get_float_arg(State& state, Value value) { return ::get_float_arg(Bridge::unwrap(state), Bridge::unwrap(value)); }
void* get_pointer_arg(State& state, Value value) { return ::get_pointer_arg(Bridge::unwrap(state), Bridge::unwrap(value)); }
void writeback_pointer_arg(State& state, Value address, void* buffer) { ::writeback_pointer_arg(Bridge::unwrap(state), Bridge::unwrap(address), buffer); }

#define WRAP_EXTERNAL_REF(name) \
  std::monostate name(State& state, Args args, Cont cont) { \
    return ::name(Bridge::unwrap(state), unwrap_args(args), unwrap_cont(std::move(cont))); \
  }
#define WRAP_EXTERNAL_VAL(name) \
  std::monostate name(State& state, Args args, Cont cont) { \
    auto inner = Bridge::unwrap(state); \
    return ::name(std::move(inner), unwrap_args(args), unwrap_cont(std::move(cont))); \
  }

WRAP_EXTERNAL_VAL(stop)
WRAP_EXTERNAL_VAL(noop)
WRAP_EXTERNAL_VAL(_exit)
WRAP_EXTERNAL_VAL(exit)
WRAP_EXTERNAL_VAL(abort)
WRAP_EXTERNAL_VAL(sym_exit)
WRAP_EXTERNAL_VAL(print_string)
WRAP_EXTERNAL_VAL(sym_print)
WRAP_EXTERNAL_VAL(gs_assert)
WRAP_EXTERNAL_VAL(gs_assert_eager)
WRAP_EXTERNAL_VAL(__assert_fail)
WRAP_EXTERNAL_VAL(llvm_va_start)
WRAP_EXTERNAL_VAL(llvm_va_end)
WRAP_EXTERNAL_VAL(llvm_va_copy)
WRAP_EXTERNAL_VAL(gs_assume)
WRAP_EXTERNAL_VAL(gs_is_symbolic)
WRAP_EXTERNAL_VAL(gs_get_valuel)
WRAP_EXTERNAL_VAL(getpagesize)
WRAP_EXTERNAL_VAL(gs_prefer_cex)
WRAP_EXTERNAL_VAL(gs_posix_prefer_cex)
WRAP_EXTERNAL_VAL(gs_warning_once)
WRAP_EXTERNAL_REF(make_symbolic)
WRAP_EXTERNAL_REF(make_symbolic_whole)
WRAP_EXTERNAL_REF(malloc)
WRAP_EXTERNAL_REF(memalign)
WRAP_EXTERNAL_REF(calloc)
WRAP_EXTERNAL_REF(realloc)
WRAP_EXTERNAL_REF(reallocarray)
WRAP_EXTERNAL_REF(llvm_memcpy)
WRAP_EXTERNAL_REF(llvm_memmove)
WRAP_EXTERNAL_REF(llvm_memset)
WRAP_EXTERNAL_REF(syscall)
WRAP_EXTERNAL_VAL(__errno_location)
WRAP_EXTERNAL_VAL(syscall_open)
WRAP_EXTERNAL_VAL(syscall_close)
WRAP_EXTERNAL_VAL(syscall_read)
WRAP_EXTERNAL_VAL(syscall_write)
WRAP_EXTERNAL_VAL(syscall_lseek)
WRAP_EXTERNAL_VAL(syscall_lseek64)
WRAP_EXTERNAL_VAL(syscall_stat)
WRAP_EXTERNAL_VAL(syscall_fstat)
WRAP_EXTERNAL_VAL(syscall_lstat)
WRAP_EXTERNAL_VAL(syscall_statfs)
WRAP_EXTERNAL_VAL(syscall_mkdir)
WRAP_EXTERNAL_VAL(syscall_rmdir)
WRAP_EXTERNAL_VAL(syscall_creat)
WRAP_EXTERNAL_VAL(syscall_unlink)
WRAP_EXTERNAL_VAL(syscall_chmod)
WRAP_EXTERNAL_VAL(syscall_chown)
WRAP_EXTERNAL_VAL(syscall_ioctl)
WRAP_EXTERNAL_VAL(syscall_fcntl)

#undef WRAP_EXTERNAL_REF
#undef WRAP_EXTERNAL_VAL

Value make_symbolic_det(State& state, Args args) { return Bridge::wrap(::make_symbolic_det(Bridge::unwrap(state), unwrap_args(args))); }
Value make_symbolic_whole_det(State& state, Args args) { return Bridge::wrap(::make_symbolic_whole_det(Bridge::unwrap(state), unwrap_args(args))); }

} // namespace gensym::runtime::v1

namespace {
Monitor runtime_monitor;
}

Monitor& cov() { return runtime_monitor; }
