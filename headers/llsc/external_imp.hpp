#ifndef LLSC_EXTERNAL_IMP_HEADER
#define LLSC_EXTERNAL_IMP_HEADER

#include "external_shared.hpp"

/******************************************************************************/

template<typename T>
inline T __llsc_assert(SS& state, List<PtrVal>& args, __Cont<T> k, __Halt<T> h) {
  auto v = args.at(0);
  auto i = v->to_IntV();
  if (i) {
    if (i->i == 0) {
      if (args.size() >= 2) {
        auto msg = get_string_arg(state, args.at(1));
        std::cout << "Eager assertion error: " << msg << std::endl;
      }
      return h(state, { make_IntV(-1) }); // concrete false - generate the test and ``halt''
    }
    return k(state, make_IntV(1, 32));
  }
  // otherwise add a symbolic condition that constraints it to be true
  // undefined/error if v is a value of other types
  auto [fls_sat, tru_sat] = check_branch(state.get_PC(), SymV::neg(v)); // check if v == 1 is not valid
  if (fls_sat) return h(state, { make_IntV(-1, 32) }); 
  state.add_PC(v);
  return k(state, make_IntV(1, 32));
}

/******************************************************************************/

template<typename T>
inline T __llsc_assume(SS& state, List<PtrVal>& args, __Cont<T> k, __Halt<T> h) {
  auto v = args.at(0);
  auto i = v->to_IntV();
  if (i) {
    if (i->i == 0) return h(state, { make_IntV(-1, 32) }); // concrete false - generate the test and ``halt''
    return k(state, make_IntV(1, 32));
  }
  // otherwise add a symbolic condition that constraints it to be true
  // undefined/error if v is a value of other types
  ASSERT(std::dynamic_pointer_cast<SymV>(v) != nullptr, "Non-Symv");
  auto [tru_sat, fls_sat] = check_branch(state.get_PC(), v); // check if v == 1 is satisfiable
  if (!tru_sat) return h(state, { make_IntV(-1, 32) }); 
  state.add_PC(v);
  return k(state, make_IntV(1, 32));
}

/******************************************************************************/

template<typename T>
inline T __make_symbolic(SS& state, List<PtrVal>& args, __Cont<T> k) {
  PtrVal loc = args.at(0);
  ASSERT(std::dynamic_pointer_cast<LocV>(loc) != nullptr, "Non-location value");
  IntData len = proj_IntV(args.at(1));
  ASSERT(len > 0, "Invalid length");
  ASSERT(2 == args.size() || 3 == args.size(), "Too much arguments for make_symbolic");
  std::string object_name = (2 == args.size()) ? fresh("unnamed") : state.get_unique_name(get_string_at(args.at(2), state));
  state.add_symbolic(object_name, len, false);
  //std::cout << "sym array size: " << proj_LocV_size(loc) << "\n";
  for (int i = 0; i < len; i++) {
    state.update(loc + i, make_SymV(object_name + "_" + std::to_string(i), 8));
  }
  return k(state, make_IntV(0));
}

inline List<SSVal> make_symbolic(SS& state, List<PtrVal> args) {
  return __make_symbolic<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate make_symbolic(SS& state, List<PtrVal> args, Cont k) {
  return __make_symbolic<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

template<typename T>
inline T __make_symbolic_whole(SS& state, List<PtrVal>& args, __Cont<T> k) {
  PtrVal loc = args.at(0);
  ASSERT(std::dynamic_pointer_cast<LocV>(loc) != nullptr, "Non-location value");
  IntData sz = proj_IntV(args.at(1));
  ASSERT(sz > 0, "Invalid length");
  ASSERT(2 == args.size() || 3 == args.size(), "Too much arguments for make_symbolic");
  std::string object_name = (2 == args.size()) ? fresh("unnamed") : state.get_unique_name(get_string_at(args.at(2), state));
  state.add_symbolic(object_name, sz, true);
  state.update(loc, make_SymV(object_name, sz*8));
  return k(state, make_IntV(0));
}

inline List<SSVal> make_symbolic_whole(SS& state, List<PtrVal> args) {
  return __make_symbolic_whole<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate make_symbolic_whole(SS& state, List<PtrVal> args, Cont k) {
  return __make_symbolic_whole<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

/******************************************************************************/

template<typename T>
inline T __malloc(SS& state, List<PtrVal> args, __Cont<T> k) {
  auto size = args.at(0);
  if (auto symvite = std::dynamic_pointer_cast<SymV>(size)) {
    ASSERT(iOP::op_ite == symvite->rator, "Invalid memory read by symv index");
    auto cond = (*symvite)[0];
    auto v_t = (*symvite)[1];
    auto v_f = (*symvite)[2];
    auto [tbr_sat, fbr_sat] = check_branch(state.get_PC(), cond);
    auto t_args = List<PtrVal>{v_t};
    auto f_args = List<PtrVal>{v_f};
    if (tbr_sat && fbr_sat) {
      cov().inc_path(1);
      SS& tbr_ss = state;
      SS fbr_ss = state.fork();
      tbr_ss.add_PC(cond);
      fbr_ss.add_PC(SymV::neg(cond));
      return __malloc(tbr_ss, t_args, k) + __malloc(fbr_ss, f_args, k);
    } else if (tbr_sat) {
      SS tbr_ss = state.add_PC(cond);
      return __malloc(tbr_ss, t_args, k);
    } else if (fbr_sat) {
      SS fbr_ss = state.add_PC(SymV::neg(cond));
      return __malloc(fbr_ss, f_args, k);
    } else {
      ABORT("no feasible path");
    }
  } else {
    IntData bytes = proj_IntV(size);
    auto emptyMem = List<PtrVal>(bytes, make_UnInitV());
    PtrVal memLoc = make_LocV(state.heap_size(), LocV::kHeap, bytes);
    if (exlib_failure_branch)
      return k(state.heap_append(emptyMem), memLoc) + k(state, make_LocV_null());
    return k(state.heap_append(emptyMem), memLoc);
  }
}

inline List<SSVal> malloc(SS& state, List<PtrVal> args) {
  return __malloc<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate malloc(SS& state, List<PtrVal> args, Cont k) {
  return __malloc<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

/******************************************************************************/

template<typename T>
inline T __memalign(SS& state, List<PtrVal>& args, __Cont<T> k) {
  size_t alignment = proj_IntV(args.at(0));
  size_t bytes = proj_IntV(args.at(1));
  auto fillmem = List<PtrVal>((((state.heap_size() + (alignment - 1)) / alignment) * alignment) - state.heap_size(), make_UnInitV());
  auto emptyMem = List<PtrVal>(bytes, make_UnInitV());
  state.heap_append(fillmem);
  ASSERT(0 == state.heap_size() % alignment, "non-aligned address");
  PtrVal memLoc = make_LocV(state.heap_size(), LocV::kHeap, bytes);
  if (exlib_failure_branch)
    return k(state.heap_append(emptyMem), memLoc) + k(state, make_LocV_null());
  return k(state.heap_append(emptyMem), memLoc);
}

inline List<SSVal> memalign(SS& state, List<PtrVal> args) {
  return __memalign<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate memalign(SS& state, List<PtrVal> args, Cont k) {
  return __memalign<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

/******************************************************************************/

template<typename T>
inline T __calloc(SS& state, List<PtrVal>& args, __Cont<T> k) {
  IntData nmemb = proj_IntV(args.at(0));
  IntData size = proj_IntV(args.at(1));
  ASSERT(size > 0 && nmemb > 0, "Invalid nmemb and size");
  auto emptyMem = List<PtrVal>(nmemb * size, make_IntV(0, 8));
  PtrVal memLoc = make_LocV(state.heap_size(), LocV::kHeap, nmemb * size);
  if (exlib_failure_branch)
    return k(state.heap_append(emptyMem), memLoc) + k(state, make_LocV_null());
  return k(state.heap_append(emptyMem), memLoc);
}

inline List<SSVal> calloc(SS& state, List<PtrVal> args) {
  return __calloc<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate calloc(SS& state, List<PtrVal> args, Cont k) {
  return __calloc<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

/******************************************************************************/

template<typename T>
inline T __realloc(SS& state, List<PtrVal>& args, __Cont<T> k) {
  IntData bytes = proj_IntV(args.at(1));
  auto emptyMem = List<PtrVal>(bytes, make_UnInitV());
  PtrVal memLoc = make_LocV(state.heap_size(), LocV::kHeap, bytes);
  state.heap_append(emptyMem);
  if (!is_LocV_null(args.at(0))) {
    Addr src = proj_LocV(args.at(0));
    IntData prevBytes = proj_LocV_size(args.at(0));
    for (int i = 0; i < prevBytes; i++) {
      state.update(memLoc + i, state.heap_lookup(src + i));
    }
  }
  return k(state, memLoc);
}

inline List<SSVal> realloc(SS& state, List<PtrVal> args) {
  return __realloc<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate realloc(SS& state, List<PtrVal> args, Cont k) {
  return __realloc<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

/******************************************************************************/
template<typename T>
inline T __reallocarray(SS& state, List<PtrVal>& args, __Cont<T> k) {
  IntData nmemb = proj_IntV(args.at(1));
  IntData size = proj_IntV(args.at(2));
  ASSERT(size > 0 && nmemb > 0, "Invalid nmemb and size");
  IntData bytes = nmemb * size;
  auto emptyMem = List<PtrVal>(bytes, make_UnInitV());
  PtrVal memLoc = make_LocV(state.heap_size(), LocV::kHeap, bytes);
  state.heap_append(emptyMem);
  if (!is_LocV_null(args.at(0))) {
    Addr src = proj_LocV(args.at(0));
    IntData prevBytes = proj_LocV_size(args.at(0));
    for (int i = 0; i < prevBytes; i++) {
      state.update(memLoc + i, state.heap_lookup(src + i));
    }
  }
  return k(state, memLoc);
}

inline List<SSVal> reallocarray(SS& state, List<PtrVal> args) {
  return __reallocarray<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate reallocarray(SS& state, List<PtrVal> args, Cont k) {
  return __reallocarray<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

/******************************************************************************/

template<typename T>
inline T __llvm_memcpy(SS& state, List<PtrVal>& args, __Cont<T> k) {
  PtrVal dest = args.at(0);
  PtrVal src = args.at(1);
  PtrVal size = args.at(2);
  IntData bytes_int;
  // Todo (Ruiqi): should we fork here
  if (auto symvite = std::dynamic_pointer_cast<SymV>(size)) {
    ASSERT(iOP::op_ite == symvite->rator, "Invalid memory read by symv index");
    auto cond = (*symvite)[0];
    auto v_t = (*symvite)[1];
    auto v_f = (*symvite)[2];
    auto [tbr_sat, fbr_sat] = check_branch(state.get_PC(), cond);
    ASSERT((!tbr_sat || !fbr_sat) && (tbr_sat || fbr_sat), "Should already forked before, only one path is feasible");
    bytes_int = tbr_sat ? proj_IntV(v_t) : proj_IntV(v_f);
    if (auto srcite = std::dynamic_pointer_cast<SymV>(src)) {
      ASSERT(iOP::op_ite == srcite->rator && (*srcite)[0] == cond, "Inconsistent ite src and size");
      src = tbr_sat ? (*srcite)[1] : (*srcite)[2];
    }
  } else {
    bytes_int = proj_IntV(size);
  }
  ASSERT(std::dynamic_pointer_cast<LocV>(dest) != nullptr, "Non-location value");
  ASSERT(std::dynamic_pointer_cast<LocV>(src) != nullptr, "Non-location value");
  for (int i = 0; i < bytes_int; i++) {
    state.update(dest + i, state.at(src + i));
  }
  return k(state, IntV0);
}

inline List<SSVal> llvm_memcpy(SS& state, List<PtrVal> args) {
  return __llvm_memcpy<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate llvm_memcpy(SS& state, List<PtrVal> args, Cont k) {
  return __llvm_memcpy<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

/******************************************************************************/

template<typename T>
inline T __llvm_memmove(SS& state, List<PtrVal>& args, __Cont<T> k) {
  PtrVal dest = args.at(0);
  PtrVal src = args.at(1);
  ASSERT(std::dynamic_pointer_cast<LocV>(dest) != nullptr, "Non-location value");
  ASSERT(std::dynamic_pointer_cast<LocV>(src) != nullptr, "Non-location value");
  IntData bytes_int = proj_IntV(args.at(2));
  // Optimize: flex_vector_transient
  auto temp_mem = List<PtrVal>{};
  for (int i = 0; i < bytes_int; i++) {
    temp_mem = temp_mem.push_back(state.at(src + i));
  }
  for (int i = 0; i < bytes_int; i++) {
    state.update(dest + i, temp_mem.at(i));
  }
  return k(state, IntV0);
}

inline List<SSVal> llvm_memmove(SS& state, List<PtrVal> args) {
  return __llvm_memmove<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate llvm_memmove(SS& state, List<PtrVal> args, Cont k) {
  return __llvm_memmove<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

/******************************************************************************/

template<typename T>
inline T __llvm_memset(SS& state, List<PtrVal>& args, __Cont<T> k) {
  PtrVal dest = args.at(0);
  IntData bytes_int = proj_IntV(args.at(2));
  ASSERT(std::dynamic_pointer_cast<LocV>(dest) != nullptr, "Non-location value");
  auto v = make_IntV(0, 8);
  for (int i = 0; i < bytes_int; i++) {
    state.update(dest + i, v);
  }
  return k(state, IntV0);
}

inline List<SSVal> llvm_memset(SS& state, List<PtrVal> args) {
  return __llvm_memset<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate llvm_memset(SS& state, List<PtrVal> args, Cont k) {
  return __llvm_memset<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

inline void copy_native2state(SS& state, PtrVal ptr, char* buf, int size) {
  ASSERT(buf && size > 0, "Invalid native buffer");
  for (int i = 0; i < size; ) {
    auto old_val = state.at(ptr + i);
    if (old_val) {
      if (std::dynamic_pointer_cast<ShadowV>(old_val) || std::dynamic_pointer_cast<LocV>(old_val)) {
        ABORT("unhandled ptrval: shadowv && LocV");
      }
      auto bytes_num = old_val->get_byte_size();
      ASSERT(bytes_num > 0, "Invalid bytes");
      // Do not over-write symbolic variable
      if (std::dynamic_pointer_cast<SymV>(old_val)) {
        i = i + bytes_num;
      } else {
        for (int j = 0; j < bytes_num; j++) {
          state.update(ptr + i, make_IntV(buf[i], 8));
          i++;
          if (i >= size) break;
        }
      }
    } else {
      state.update(ptr + i, make_IntV(buf[i], 8));
      i++;
    }
  }
}

inline void writeback_pointer_arg(SS& state, PtrVal loc, void* buf) {
  if (is_LocV_null(loc)) {
    ASSERT(nullptr == buf, "allocate memory for null locv");
    return;
  }
  ASSERT(std::dynamic_pointer_cast<LocV>(loc), "Non LocV");
  size_t count = get_pointer_realsize(loc);
  copy_native2state(state, loc, (char*)buf, count);
  free(buf);
}

// TODO: should we put this (and the one in external_pure.hpp) into external_shared.hpp?
class ShadowMemEntry {
  private:
  char* buf;
  public:
  size_t size;
  PtrVal mem_addr;
  ShadowMemEntry(PtrVal addr, size_t size) : buf(new char[size+1]), mem_addr(addr), size(size) {
    ASSERT(std::dynamic_pointer_cast<LocV>(addr) != nullptr, "Non-location value");
    memset(buf, 0, size+1);
  }
  ~ShadowMemEntry() { delete buf; }
  void writeback(SS& state) { copy_native2state(state, mem_addr, buf, size); }
  void readbuf(SS& state) { copy_state2native(state, mem_addr, buf, size); }
  char* getbuf() { return buf; }
};

template<typename T>
inline T __syscall(SS& state, List<PtrVal>& args, __Cont<T> k) {
  PtrVal x = args.at(0);
  auto x_i = std::dynamic_pointer_cast<IntV>(x);
  ASSERT(x_i && (64 == x_i->bw), "syscall's argument must be concrete and must be long (i64)!");
  long syscall_number = x_i->as_signed();
  long retval = -1;

  // Save errno
  errno = proj_IntV(state.at(state.error_loc(), 4));

  switch (syscall_number) {
    case __NR_read: {
      int fd = get_int_arg(state, args.at(1));
      ASSERT(0 == fd, "syscall read can only read from stdin, other fd should use pread64\n");
      size_t count = get_int_arg(state, args.at(3));
      ShadowMemEntry temp(args.at(2), count);
      retval = syscall(__NR_read, fd, temp.getbuf(), count);
      if (retval >= 0) temp.writeback(state);
      break;
    }
    case __NR_write: {
      int fd = get_int_arg(state, args.at(1));
      ASSERT((1 == fd) || (2 == fd) ,"syscall write can only write to stdout and stderr, other fd should use pwrite64\n");
      size_t count = get_int_arg(state, args.at(3));
      ShadowMemEntry temp(args.at(2), count);
      temp.readbuf(state);
      retval = syscall(__NR_write, fd, temp.getbuf(), count);
      break;
    }
    case __NR_open: {
      ASSERT(3 == args.size() || 4 == args.size(), "open has 2 or 3 arguments");
      mode_t mode = 4 == args.size() ? get_int_arg(state, args.at(3)) : 0;
      int flags = get_int_arg(state, args.at(2));
      std::string pathname = get_string_arg(state, args.at(1));
      //std::cout << "pathname: " << pathname << " flags: " << flags << " mode: " << mode << std::endl;
      retval = syscall(__NR_open, pathname.c_str(), flags, mode);
      break;
    }
    case __NR_close: {
      int fd = get_int_arg(state, args.at(1));
      retval = syscall(__NR_close, fd);
      break;
    }
    case __NR_stat: {
      std::string pathname = get_string_arg(state, args.at(1));
      size_t count = sizeof(struct stat64);
      ShadowMemEntry temp(args.at(2), count);
      retval = syscall(__NR_stat, pathname.c_str(), temp.getbuf());
      if (retval >= 0) temp.writeback(state);
      break;
    }
    case __NR_fstat: {
      int fd = get_int_arg(state, args.at(1));
      size_t count = sizeof(struct stat64);
      ShadowMemEntry temp(args.at(2), count);
      retval = syscall(__NR_fstat, fd, temp.getbuf());
      if (retval >= 0) temp.writeback(state);
      break;
    }
    case __NR_lstat: {
      std::string pathname = get_string_arg(state, args.at(1));
      size_t count = sizeof(struct stat64);
      ShadowMemEntry temp(args.at(2), count);
      retval = syscall(__NR_lstat, pathname.c_str(), temp.getbuf());
      if (retval >= 0) temp.writeback(state);
      break;
    }
    case __NR_lseek: {
      int fd = get_int_arg(state, args.at(1));
      off64_t offset = get_int_arg(state, args.at(2));
      int whence = get_int_arg(state, args.at(3));
      retval = syscall(__NR_lseek, fd, offset, whence);
      break;
    }
    case __NR_ioctl: {
      int fd = get_int_arg(state, args.at(1));
      unsigned long request = get_int_arg(state, args.at(2));
      auto buf = std::dynamic_pointer_cast<LocV>(args.at(3));
      size_t count = buf->size - (buf->l - buf->base);
      ShadowMemEntry temp(buf, count);
      retval = syscall(__NR_ioctl, fd, request, temp.getbuf());
      //std::cout << "ioctl: " << " fd: " << fd << " request: " << request << " buf: " << std::string(temp.getbuf()) << " count: " << count << " result: " << retval << std::endl;
      if (retval >= 0) temp.writeback(state);
      break;
    }
    case __NR_pread64: {
      int fd = get_int_arg(state, args.at(1));
      ASSERT(fd > 2, "can not call pread/pwrite on stdin, stdout and stderr\n");
      size_t count = get_int_arg(state, args.at(3));
      off64_t offset = get_int_arg(state, args.at(4));
      ShadowMemEntry temp(args.at(2), count);
      //std::cout << "pread: " << " fd: " << fd << " buf: " << std::string(temp.getbuf()) << " count: " << count << " offset: " << offset << std::endl;
      retval = syscall(__NR_pread64, fd, temp.getbuf(), count, offset);
      if (retval >= 0) temp.writeback(state);
      break;
    }
    case __NR_pwrite64: {
      int fd = get_int_arg(state, args.at(1));
      ASSERT(fd > 2, "can not call pread/pwrite on stdin, stdout and stderr\n");
      size_t count = get_int_arg(state, args.at(3));
      off64_t offset = get_int_arg(state, args.at(4));
      ShadowMemEntry temp(args.at(2), count);
      temp.readbuf(state);
      //std::cout << "pwrite: " << " fd: " << fd << " buf: " << std::string(temp.getbuf()) << " count: " << count << " offset: " << offset << std::endl;
      retval = syscall(__NR_pwrite64, fd, temp.getbuf(), count, offset);
      break;
    }
    case __NR_access:
      ABORT("Unsupported Systemcall");
      break;
    case __NR_select:
      ABORT("Unsupported Systemcall");
      break;
    case __NR_fcntl:
      ABORT("Unsupported Systemcall");
      break;
    case __NR_fsync:
      ABORT("Unsupported Systemcall");
      break;
    case __NR_ftruncate: {
      int fd = get_int_arg(state, args.at(1));
      off_t length = get_int_arg(state, args.at(2));
      retval = syscall(__NR_ftruncate, fd, length);
      break;
    }
    case __NR_getcwd: {
      size_t count = get_int_arg(state, args.at(2));
      ASSERT(count > 0, "empty buffer for getcwd");
      ASSERT(!is_LocV_null(args.at(1)), "null buffer pointer");
      ShadowMemEntry temp(args.at(1), count);
      retval = syscall(__NR_getcwd, temp.getbuf(), count);
      if (retval >= 0) temp.writeback(state);
      break;
    }
    case __NR_chdir:
      ABORT("Unsupported Systemcall");
      break;
    case __NR_fchdir:
      ABORT("Unsupported Systemcall");
      break;
    case __NR_readlink:
      ABORT("Unsupported Systemcall");
      break;
    case __NR_chmod:
      ABORT("Unsupported Systemcall");
      break;
    case __NR_fchmod:
      ABORT("Unsupported Systemcall");
      break;
    case __NR_chown:
      ABORT("Unsupported Systemcall");
      break;
    case __NR_fchown:
      ABORT("Unsupported Systemcall");
      break;
    case __NR_statfs:
      ABORT("Unsupported Systemcall");
      break;
    case __NR_fstatfs:
      ABORT("Unsupported Systemcall");
      break;
    case __NR_getdents64:
      ABORT("Unsupported Systemcall");
      break;
    case __NR_utimes:
      ABORT("Unsupported Systemcall");
      break;
    case __NR_openat:
      ABORT("Unsupported Systemcall");
      break;
    case __NR_futimesat:
      ABORT("Unsupported Systemcall");
      break;
    case __NR_newfstatat:
      ABORT("Unsupported Systemcall");
      break;
    default:
      ABORT("Unsupported Systemcall");
      break;
  }

  // Write back errno
  state.update(state.error_loc(), make_IntV(errno, 32), 4);
  //std::cout << "syscall_num: " << syscall_number << "  retval: " << retval << std::endl;
  return k(state, make_IntV(retval, 64));
}

inline List<SSVal> syscall(SS& state, List<PtrVal> args) {
  return __syscall<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate syscall(SS& state, List<PtrVal> args, Cont k) {
  return __syscall<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

/******************************************************************************/

// FIXME: vaargs and refactor
// args 0: LocV to {i32, i32, i8*, i8*}
// in memory {4, 4, 8, 8}
template<typename T>
inline T __llvm_va_start(SS& state, List<PtrVal>& args, __Cont<T> k) {
  PtrVal va_list = args.at(0);
  ASSERT(std::dynamic_pointer_cast<LocV>(va_list) != nullptr, "Non-location value");
  PtrVal va_arg = state.vararg_loc();
  // FIXME: magic number 48?
  state.update(va_list + 0, IntV0, 4);
  state.update(va_list + 4, IntV0, 4);
  state.update(va_list + 8, va_arg + 48, 8);
  state.update(va_list + 16, va_arg, 8);
  return k(state, IntV0);
}

template<typename T>
inline T __llvm_va_end(SS& state, List<PtrVal>& args, __Cont<T> k) {
  PtrVal va_list = args.at(0);
  ASSERT(std::dynamic_pointer_cast<LocV>(va_list) != nullptr, "Non-location value");
  // FIXME: magic number 24?
  for (int i = 0; i < 24; i++) {
    state.update(va_list + i, nullptr);
  }
  return k(state, IntV0);
}

template<typename T>
inline T __llvm_va_copy(SS& state, List<PtrVal>& args, __Cont<T> k) {
  PtrVal dst_va_list = args.at(0);
  PtrVal src_va_list = args.at(1);
  ASSERT(std::dynamic_pointer_cast<LocV>(dst_va_list) != nullptr, "Dest valist Non-location value");
  ASSERT(std::dynamic_pointer_cast<LocV>(src_va_list) != nullptr, "Src valist Non-location value");
  ASSERT(std::dynamic_pointer_cast<LocV>(state.at(src_va_list + 16, 8)) != nullptr, "Src valist must be initialized");
  state.update(dst_va_list + 0, state.at(src_va_list + 0, 4), 4);
  state.update(dst_va_list + 4, state.at(src_va_list + 4, 4), 4);
  state.update(dst_va_list + 8, state.at(src_va_list + 8, 8), 8);
  state.update(dst_va_list + 16, state.at(src_va_list + 16, 8), 8);
  return k(state, IntV0);
}

/******************************************************************************/

template<typename T>
inline T __llsc_prefer_cex(SS& state, List<PtrVal>& args, __Cont<T> k) {
  ASSERT(2 == args.size(), "Invalid number of arguments for llsc_prefer_cex");
  auto cond = args.at(1);
  ASSERT(std::dynamic_pointer_cast<SymV>(cond) != nullptr, "Not a symbolic expression");
  state.add_cex(cond);
  return k(state, make_IntV(0));
}

#endif
