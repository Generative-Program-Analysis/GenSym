#ifndef LLSC_EXTERNAL_SHARED_HEADERS
#define LLSC_EXTERNAL_SHARED_HEADERS

/* This is the file that is supposed to be shared between
 * external_pure.hpp and external_imp.hpp.
 * If an external function does not depends on some specific
 * behavior of `SS`, then it probably should be put here.
 */

#ifdef PURE_STATE
using Cont = std::function<std::monostate(SS, PtrVal)>;
#endif
#ifdef IMPURE_STATE
using Cont = std::function<std::monostate(SS&, PtrVal)>;
#endif

template<typename T> using __Cont = std::function<T(SS, PtrVal)>;
template<typename T> using __Halt = std::function<T(SS, List<PtrVal>)>;

/******************************************************************************/

inline List<SSVal> stop(SS state, List<PtrVal> args) {
  check_pc_to_file(state);
  return List<SSVal>{};
}

inline std::monostate stop(SS state, List<PtrVal> args, Cont k) {
  check_pc_to_file(state);
  return std::monostate();
}

inline List<SSVal> noop(SS state, List<PtrVal> args) {
  return List<SSVal>{{state, make_IntV(0)}};
}

/******************************************************************************/

inline std::string get_string(PtrVal ptr, SS state) {
  std::string name;
  char c = proj_IntV_char(state.at(ptr)); // c = *ptr
  ASSERT(std::dynamic_pointer_cast<LocV>(ptr) != nullptr, "Non-location value");
  while (c != '\0') {
    name += c;
    ptr = ptr + 1;
    c = proj_IntV_char(state.at(ptr)); // c = *ptr
  }
  return name;
}

template<typename T>
inline T __print_string(SS& state, List<PtrVal>& args, __Cont<T> k) {
  PtrVal x = args.at(0);
  if (std::dynamic_pointer_cast<LocV>(x)) {
    std::cout << get_string(x, state);
    return k(state, make_IntV(0));
  }
  ABORT("Cannot print non-LocV value as string");
}

inline List<SSVal> print_string(SS state, List<PtrVal> args) {
  return __print_string<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate print_string(SS state, List<PtrVal> args, Cont k) {
  return __print_string<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

/******************************************************************************/

template<typename T>
inline T __sym_print(SS& state, List<PtrVal>& args, __Cont<T> k) {
  for (auto x : args) {
    std::cout << ((x == nullptr) ? "nullptr" : x->toString()) << ";" << std::endl;
  }
  return k(state, make_IntV(0));
}

inline List<SSVal> sym_print(SS state, List<PtrVal> args) {
  return __sym_print<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate sym_print(SS state, List<PtrVal> args, Cont k) {
  return __sym_print<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

/******************************************************************************/

template<typename T>
inline T __sym_exit(SS& state, List<PtrVal>& args, __Cont<T> k) {
  ASSERT(args.size() == 1, "sym_exit accepts exactly one argument");
  auto v = args.at(0)->to_IntV();
  ASSERT(v != nullptr, "sym_exit only accepts integer argument");
  int status = v->as_signed();
  check_pc_to_file(state);
#ifdef USE_TP
  // XXX: brutally call _exit? then what should happen if two threads are calling sym_exit?
  tp.stop_all_tasks();
  set_exit_code(status);
  return k(state, nullptr);
#else
  cov.print_all();
  _exit(status);
#endif
}

inline List<SSVal> sym_exit(SS state, List<PtrVal> args) {
  return __sym_exit<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{}; });
}

inline std::monostate sym_exit(SS state, List<PtrVal> args, Cont k) {
  return __sym_exit<std::monostate>(state, args, [](auto s, auto v) { return std::monostate{}; });
}

/******************************************************************************/

template<typename T>
inline T __llsc_assert(SS& state, List<PtrVal>& args, __Cont<T> k, __Halt<T> h);

inline List<SSVal> llsc_assert(SS state, List<PtrVal> args) {
  return __llsc_assert<List<SSVal>>(state, args,
      [](auto s, auto v) { return List<SSVal>{{s, v}}; },
      [](auto s, auto a) { return stop(s, a); });
}

inline std::monostate llsc_assert(SS state, List<PtrVal> args, Cont k) {
  return __llsc_assert<std::monostate>(state, args,
      [&k](auto s, auto v) { return k(s, v); },
      [&k](auto s, auto a) { return stop(s, a, k); });
}

inline List<SSVal> llsc_assert_eager(SS state, List<PtrVal> args) {
  return __llsc_assert<List<SSVal>>(state, args,
      [](auto s, auto v) { return List<SSVal>{{s, v}}; },
      [](auto s, auto a) { return sym_exit(s, a); });
}

inline std::monostate llsc_assert_eager(SS state, List<PtrVal> args, Cont k) {
  return __llsc_assert<std::monostate>(state, args,
      [&k](auto s, auto v) { return k(s, v); },
      [&k](auto s, auto a) { return sym_exit(s, a, k); });
}

/******************************************************************************/

inline List<SSVal> __assert_fail(SS state, List<PtrVal> args) {
  return llsc_assert(state, args);
}

inline std::monostate __assert_fail(SS state, List<PtrVal> args, Cont k) {
  return llsc_assert(state, args, k);
}

/******************************************************************************/

template<typename T>
inline T __llvm_va_start(SS& state, List<PtrVal>& args, __Cont<T> k);

template<typename T>
inline T __llvm_va_end(SS& state, List<PtrVal>& args, __Cont<T> k);

inline List<SSVal> llvm_va_start(SS state, List<PtrVal> args) {
  return __llvm_va_start<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate llvm_va_start(SS state, List<PtrVal> args, Cont k) {
  return __llvm_va_start<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

inline List<SSVal> llvm_va_end(SS state, List<PtrVal> args) {
  return __llvm_va_end<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate llvm_va_end(SS state, List<PtrVal> args, Cont k) {
  return __llvm_va_end<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

#endif
