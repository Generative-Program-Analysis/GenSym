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

/* `stop` only stops the execution of current path. */
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
inline std::monostate noop(SS state, List<PtrVal> args, Cont k) {
  return k(state, make_IntV(0));
}

inline List<SSVal> _exit(SS state, List<PtrVal> args) {
  return stop(state, args);
}

inline std::monostate _exit(SS state, List<PtrVal> args, Cont k) {
  return stop(state, args, k);
}

inline List<SSVal> exit(SS state, List<PtrVal> args) {
  return stop(state, args);
}

inline std::monostate exit(SS state, List<PtrVal> args, Cont k) {
  return stop(state, args, k);
}

inline List<SSVal> abort(SS state, List<PtrVal> args) {
  return stop(state, List<PtrVal>{make_IntV(-1)});
}

inline std::monostate abort(SS state, List<PtrVal> args, Cont k) {
  return stop(state, List<PtrVal>{make_IntV(-1)}, k);
}

/******************************************************************************/

/* `__sym_exit` stops the whole execution of all paths. */
template<typename T>
inline T __sym_exit(SS& state, List<PtrVal>& args, __Cont<T> k) {
  ASSERT(args.size() == 1, "sym_exit accepts exactly one argument");
  auto v = args.at(0)->to_IntV();
  ASSERT(v != nullptr, "sym_exit only accepts integer argument");
  int status = v->as_signed();
  check_pc_to_file(state);
  if (can_par_tp()) {
    // XXX: brutally call _exit? then what should happen if two threads are calling sym_exit?
    tp.stop_all_tasks();
    set_exit_code(status);
    return k(state, nullptr);
  } else {
    cov().print_all();
    _exit(status);
  }
}

inline List<SSVal> sym_exit(SS state, List<PtrVal> args) {
  return __sym_exit<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{}; });
}

inline std::monostate sym_exit(SS state, List<PtrVal> args, Cont k) {
  return __sym_exit<std::monostate>(state, args, [](auto s, auto v) { return std::monostate{}; });
}

/******************************************************************************/

inline char proj_IntV_char(const PtrVal& v) {
  auto intV = v->to_IntV();
  ASSERT(intV->get_bw() == 8, "proj_IntV_char: Bitwidth mismatch");
  return static_cast<char>(proj_IntV(intV));
}

inline std::string get_string_at(PtrVal ptr, SS& state) {
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

inline UIntData get_int_arg(SS& state, PtrVal x) {
  auto x_i = std::dynamic_pointer_cast<IntV>(x);
  // Todo: add this concretization tp path constraints
  if (x_i) {
    return x_i->as_signed();
  } else {
    auto sym_v = std::dynamic_pointer_cast<SymV>(x);
    ASSERT(sym_v, "get value of non-symbolic variable");
    std::pair<bool, UIntData> res = get_sat_value(state.get_PC(), sym_v);
    ASSERT(res.first, "Un-feasible path");
    return res.second;
  }
}

inline double get_float_arg(SS& state, PtrVal x) {
  auto x_f = std::dynamic_pointer_cast<FloatV>(x);
  ASSERT(x_f, "getting Non-FloatV");
  return (double)x_f->f;
}

inline std::string get_string_arg(SS& state, PtrVal ptr) {
  std::string name;
  char c = get_int_arg(state, state.at(ptr)); // c = *ptr
  ASSERT(std::dynamic_pointer_cast<LocV>(ptr) != nullptr, "Non-location value");
  while (c != '\0') {
    name += c;
    ptr = ptr + 1;
    c = get_int_arg(state, state.at(ptr)); // c = *ptr
  }
  return name;
}

inline void copy_state2native(SS& state, PtrVal ptr, char* buf, int size) {
  ASSERT(buf && size > 0, "Invalid native buffer");
  for (int i = 0; i < size; ) {
    auto val = state.at(ptr + i);
    if (val) {
      if (std::dynamic_pointer_cast<ShadowV>(val) || std::dynamic_pointer_cast<LocV>(val)) {
        ABORT("unhandled ptrval: shadowv && LocV");
      }
      auto bytes = val->to_bytes();
      int bytes_num = bytes.size();
      ASSERT(bytes_num > 0, "Invalid bytes");
      // All bytes must be concrete IntV
      for (int j = 0; j < bytes_num; j++) {
        buf[i] = (char) get_int_arg(state, bytes.at(j));
        i++;
        if (i >= size) break;
      }
    } else {
      buf[i] = '\0';
      i++;
    }
  }
}

inline char* get_pointer_arg(SS& state, PtrVal loc) {
  if (is_LocV_null(loc)) return nullptr;
  ASSERT(std::dynamic_pointer_cast<LocV>(loc), "Non LocV");
  size_t count = get_pointer_realsize(loc);
  char * buf = (char*)malloc(count);
  copy_state2native(state, loc, buf, count);
  return buf;
}

template<typename T>
inline T __print_string(SS& state, List<PtrVal>& args, __Cont<T> k) {
  PtrVal x = args.at(0);
  if (std::dynamic_pointer_cast<LocV>(x)) {
    std::cout << get_string_at(x, state);
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

template<typename T>
inline T __llvm_va_copy(SS& state, List<PtrVal>& args, __Cont<T> k);

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

inline List<SSVal> llvm_va_copy(SS state, List<PtrVal> args) {
  return __llvm_va_copy<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate llvm_va_copy(SS state, List<PtrVal> args, Cont k) {
  return __llvm_va_copy<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

/******************************************************************************/

template<typename T>
inline T __llsc_assume(SS& state, List<PtrVal>& args, __Cont<T> k, __Halt<T> h);

inline List<SSVal> llsc_assume(SS state, List<PtrVal> args) {
  return __llsc_assume<List<SSVal>>(state, args,
      [](auto s, auto v) { return List<SSVal>{{s, v}}; },
      [](auto s, auto a) { return sym_exit(s, a); });
}

inline std::monostate llsc_assume(SS state, List<PtrVal> args, Cont k) {
  return __llsc_assume<std::monostate>(state, args,
      [&k](auto s, auto v) { return k(s, v); },
      [&k](auto s, auto a) { return sym_exit(s, a, k); });
}

/******************************************************************************/

// Todo: could use is_conc method of struct value
template<typename T>
inline T __llsc_is_symbolic(SS& state, List<PtrVal>& args, __Cont<T> k) {
  auto v = args.at(0);
  ASSERT(v, "null pointer");
  auto sym_v = std::dynamic_pointer_cast<SymV>(v);
  if (sym_v) return k(state, make_IntV(1, 32));
  ASSERT(std::dynamic_pointer_cast<IntV>(v), "non-intv");
  return k(state, make_IntV(0, 32));
}

inline List<SSVal> llsc_is_symbolic(SS state, List<PtrVal> args) {
  return __llsc_is_symbolic<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate llsc_is_symbolic(SS state, List<PtrVal> args, Cont k) {
  return __llsc_is_symbolic<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

/******************************************************************************/

template<typename T>
inline T __llsc_get_valuel(SS& state, List<PtrVal>& args, __Cont<T> k) {
  auto v = args.at(0);
  ASSERT(v, "null pointer");
  auto i = v->to_IntV();
  if (i) return k(state, v);
  auto sym_v = std::dynamic_pointer_cast<SymV>(v);
  ASSERT(sym_v, "get value of non-symbolic variable");
  ASSERT(64 == sym_v->get_bw(), "Bitwidth mismatch");
  std::pair<bool, UIntData> res = get_sat_value(state.get_PC(), sym_v);
  ASSERT(res.first, "Un-feasible path");
  return k(state, make_IntV(res.second, 64));
}

inline List<SSVal> llsc_get_valuel(SS state, List<PtrVal> args) {
  return __llsc_get_valuel<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate llsc_get_valuel(SS state, List<PtrVal> args, Cont k) {
  return __llsc_get_valuel<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

/******************************************************************************/

template<typename T>
inline T __getpagesize(SS& state, List<PtrVal>& args, __Cont<T> k) {
  static int page_size = getpagesize();
  return k(state, make_IntV(page_size, 32));
}

inline List<SSVal> getpagesize(SS state, List<PtrVal> args) {
  return __getpagesize<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate getpagesize(SS state, List<PtrVal> args, Cont k) {
  return __getpagesize<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

#endif
