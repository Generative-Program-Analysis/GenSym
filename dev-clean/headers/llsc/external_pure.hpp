#ifndef LLSC_EXTERNAL_PURE_HEADERS
#define LLSC_EXTERNAL_PURE_HEADERS

#include "external_shared.hpp"

/******************************************************************************/

template<typename T>
inline T __llsc_assert(SS& state, List<PtrVal>& args, __Cont<T> k, __Halt<T> h) {
  auto v = args.at(0);
  auto i = v->to_IntV();
  if (i) {
    if (i->i == 0) {
      // concrete false - generate the test and ``halt''
      std::cout << "Warning: assert violates; abort and generate test.\n";
      return h(state, { make_IntV(-1) });
    }
    return k(state, make_IntV(1, 32));
  }
  // otherwise add a symbolic condition that constraints it to be true
  // undefined/error if v is a value of other types
  auto cond = to_SMTNeg(v);
  auto new_s = state.add_PC(cond);
  if (check_pc(new_s.get_PC())) {
    std::cout << "Warning: assert violates; abort and generate test.\n";
    return h(new_s, { make_IntV(-1) }); // check if v == 1 is not valid
  }
  return k(state.add_PC(v), make_IntV(1, 32));
}

/******************************************************************************/

template<typename T>
inline T __make_symbolic(SS& state, List<PtrVal>& args, __Cont<T> k) {
  PtrVal loc = args.at(0);
  ASSERT(std::dynamic_pointer_cast<LocV>(loc) != nullptr, "Non-location value");
  IntData len = proj_IntV(args.at(1));
  SS res = state;
  //std::cout << "sym array size: " << proj_LocV_size(loc) << "\n";
  for (int i = 0; i < len; i++) {
    res = res.update(loc + i, make_SymV(fresh(), 8));
  }
  return k(res, make_IntV(0));
}

inline List<SSVal> make_symbolic(SS state, List<PtrVal> args) {
  return __make_symbolic<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate make_symbolic(SS state, List<PtrVal> args, Cont k) {
  return __make_symbolic<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

template<typename T>
inline T __make_symbolic_whole(SS& state, List<PtrVal>& args, __Cont<T> k) {
  PtrVal loc = args.at(0);
  ASSERT(std::dynamic_pointer_cast<LocV>(loc) != nullptr, "Non-location value");
  IntData sz = proj_IntV(args.at(1));
  SS res = state.update(loc, make_SymV(fresh(), sz*8));
  return k(res, make_IntV(0));
}

inline List<SSVal> make_symbolic_whole(SS state, List<PtrVal> args) {
  return __make_symbolic_whole<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate make_symbolic_whole(SS state, List<PtrVal> args, Cont k) {
  return __make_symbolic_whole<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

/******************************************************************************/

template<typename T>
inline T __malloc(SS& state, List<PtrVal>& args, __Cont<T> k) {
  IntData bytes = proj_IntV(args.at(0));
  auto emptyMem = List<PtrVal>(bytes, nullptr);
  PtrVal memLoc = make_LocV(state.heap_size(), LocV::kHeap, bytes);
  if (exlib_failure_branch)
    return k(state.heap_append(emptyMem), memLoc) + k(state, make_LocV_null());
  return k(state.heap_append(emptyMem), memLoc);
}

inline List<SSVal> malloc(SS state, List<PtrVal> args) {
  return __malloc<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate malloc(SS state, List<PtrVal> args, Cont k) {
  // TODO: in the thread pool version, we should add task into the pool when forking
  return __malloc<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

/******************************************************************************/

template<typename T>
inline T __realloc(SS& state, List<PtrVal>& args, __Cont<T> k) {
  Addr src = proj_LocV(args.at(0));
  IntData bytes = proj_IntV(args.at(1));
  auto emptyMem = List<PtrVal>(bytes, nullptr);
  std::cout << "realloc size: " << emptyMem.size() << std::endl;
  PtrVal memLoc = make_LocV(state.heap_size(), LocV::kHeap, bytes);
  IntData prevBytes = proj_LocV_size(args.at(0));
  std::cout << "prev size: " << prevBytes << std::endl;
  SS res = state.heap_append(emptyMem);
  for (int i = 0; i < prevBytes; i++) {
    res = res.update(memLoc + i, res.heap_lookup(src + i));
  }
  return k(res, memLoc);
}

inline List<SSVal> realloc(SS state, List<PtrVal> args) {
  return __realloc<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate realloc(SS state, List<PtrVal> args, Cont k) {
  return __realloc<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

/******************************************************************************/

template<typename T>
inline T __llvm_memcpy(SS& state, List<PtrVal>& args, __Cont<T> k) {
  PtrVal dest = args.at(0);
  PtrVal src = args.at(1);
  IntData bytes_int = proj_IntV(args.at(2));
  ASSERT(std::dynamic_pointer_cast<LocV>(dest) != nullptr, "Non-location value");
  ASSERT(std::dynamic_pointer_cast<LocV>(src) != nullptr, "Non-location value");
  SS res = state;
  for (int i = 0; i < bytes_int; i++) {
    res = res.update(dest + i, res.at(src + i));
  }
  return k(res, IntV0);
}

inline List<SSVal> llvm_memcpy(SS state, List<PtrVal> args) {
  return __llvm_memcpy<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate llvm_memcpy(SS state, List<PtrVal> args, Cont k) {
  return __llvm_memcpy<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

/******************************************************************************/

template<typename T>
inline T __llvm_memmove(SS& state, List<PtrVal>& args, __Cont<T> k) {
  PtrVal dest = args.at(0);
  PtrVal src = args.at(1);
  ASSERT(std::dynamic_pointer_cast<LocV>(dest) != nullptr, "Non-location value");
  ASSERT(std::dynamic_pointer_cast<LocV>(src) != nullptr, "Non-location value");
  SS res = state;
  IntData bytes_int = proj_IntV(args.at(2));
  // Optimize: flex_vector_transient
  auto temp_mem = List<PtrVal>{};
  for (int i = 0; i < bytes_int; i++) {
    temp_mem = temp_mem.push_back(res.at(src + i));
  }
  for (int i = 0; i < bytes_int; i++) {
    res = res.update(dest + i, temp_mem.at(i));
  }
  return k(res, IntV0);
}

inline List<SSVal> llvm_memmove(SS state, List<PtrVal> args) {
  return __llvm_memmove<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate llvm_memmove(SS state, List<PtrVal> args, Cont k) {
  return __llvm_memmove<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

/******************************************************************************/

template<typename T>
inline T __llvm_memset(SS& state, List<PtrVal>& args, __Cont<T> k) {
  PtrVal dest = args.at(0);
  IntData bytes_int = proj_IntV(args.at(2));
  ASSERT(std::dynamic_pointer_cast<LocV>(dest) != nullptr, "Non-location value");
  auto v = make_IntV(0, 8);
  SS res = state;
  for (int i = 0; i < bytes_int; i++) {
    res = res.update(dest + i, v);
  }
  return k(res, IntV0);
}

inline List<SSVal> llvm_memset(SS state, List<PtrVal> args) {
  return __llvm_memset<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate llvm_memset(SS state, List<PtrVal> args, Cont k) {
  return __llvm_memset<std::monostate>(state, args, [&k](auto s, auto v) { return k(s, v); });
}

/******************************************************************************/
inline int64_t getIntArg(PtrVal x) {
  auto x_i = std::dynamic_pointer_cast<IntV>(x);
  ASSERT(x_i, "Non integer argument!");
  return x_i->as_signed();
}

class ShadowMemEntry {
  private:
  char* buf = nullptr;
  public:
  size_t size;
  PtrVal mem_addr;
  ShadowMemEntry(PtrVal addr, size_t size) {
    //std::cout << "ShadowMemEntry constructed" << std::endl;
    ASSERT(std::dynamic_pointer_cast<LocV>(addr) != nullptr, "Non-location value");
    this->buf = (char*) malloc(size+1);
    memset(this->buf, 0, size+1);
    this->mem_addr = addr;
    this->size = size;
  }
  ~ShadowMemEntry() {
    free(buf);
    //std::cout << "ShadowMemEntry destructed" << std::endl;
  }
  SS writeback(SS& state) {
    SS res = state;
    for (int i = 0; i < size; i++) {
      res = res.update(mem_addr + i, make_IntV(buf[i], 8));
    }
    // Todo: Check whether this writeback will break the memory layout.
    return res;
  }
  void readbuf(SS state) {
    for (int i = 0; i < size; ) {
      auto val = state.at(mem_addr + i);
      if (val) {
        if (std::dynamic_pointer_cast<ShadowV>(val)) {
          ABORT("unhandled ptrval: shadowv");
        }
        auto bytes = val->to_bytes();
        int bytes_num = bytes.size();
        ASSERT(bytes_num > 0, "Invalid bytes");
        // All bytes must be concrete IntV
        for (int j=0; j<bytes_num; j++) {
          buf[i+j] = (char) getIntArg(bytes.at(j));
        }
        i = i + bytes_num;
      } else {
        buf[i] = '\0';
        i = i + 1;
      }
    }
  }
  char* getbuf() {
    return buf;
  }
};

template<typename T>
inline T __syscall(SS& state, List<PtrVal>& args, __Cont<T> k) {
  PtrVal x = args.at(0);
  auto x_i = std::dynamic_pointer_cast<IntV>(x);
  ASSERT(x_i && (64 == x_i->bw), "syscall's argument must be concrete and must be long (i64)!");
  long syscall_number = x_i->as_signed();
  long retval = -1;
  SS res = state;
  switch (syscall_number) {
    case __NR_read: {
      int fd = getIntArg(args.at(1));
      ASSERT(0 == fd, "syscall read can only read from stdin, other fd should use pread64\n");
      size_t count = getIntArg(args.at(3));
      ShadowMemEntry temp(args.at(2), count);
      retval = syscall(__NR_read, fd, temp.getbuf(), count);
      if (retval >= 0) res = temp.writeback(res);
      break;
    }
    case __NR_write: {
      int fd = getIntArg(args.at(1));
      ASSERT((1 == fd) || (2 == fd) ,"syscall write can only write to stdout and stderr, other fd should use pwrite64\n");
      size_t count = getIntArg(args.at(3));
      ShadowMemEntry temp(args.at(2), count);
      temp.readbuf(res);
      retval = syscall(__NR_write, fd, temp.getbuf(), count);
      break;
    }
    case __NR_open: {
      ASSERT(3 == args.size() || 4 == args.size(), "open has 2 or 3 arguments");
      mode_t mode = 4 == args.size() ? getIntArg(args.at(3)) : 0;
      int flags = getIntArg(args.at(2));
      std::string pathname = get_string(args.at(1), state);
      //std::cout << "pathname: " << pathname << " flags: " << flags << " mode: " << mode << std::endl;
      retval = syscall(__NR_open, pathname.c_str(), flags, mode);
      break;
    }
    case __NR_close: {
      int fd = getIntArg(args.at(1));
      retval = syscall(__NR_close, fd);
      break;
    }
    case __NR_stat: {
      std::string pathname = get_string(args.at(1), state);
      size_t count = sizeof(struct stat64);
      ShadowMemEntry temp(args.at(2), count);
      retval = syscall(__NR_stat, pathname.c_str(), temp.getbuf());
      if (retval >= 0) res = temp.writeback(res);
      break;
    }
    case __NR_fstat: {
      int fd = getIntArg(args.at(1));
      size_t count = sizeof(struct stat64);
      ShadowMemEntry temp(args.at(2), count);
      retval = syscall(__NR_fstat, fd, temp.getbuf());
      if (retval >= 0) res = temp.writeback(res);
      break;
    }
    case __NR_lstat: {
      std::string pathname = get_string(args.at(1), state);
      size_t count = sizeof(struct stat64);
      ShadowMemEntry temp(args.at(2), count);
      retval = syscall(__NR_lstat, pathname.c_str(), temp.getbuf());
      if (retval >= 0) res = temp.writeback(res);
      break;
    }
    case __NR_lseek: {
      int fd = getIntArg(args.at(1));
      off64_t offset = getIntArg(args.at(2));
      int whence = getIntArg(args.at(3));
      retval = syscall(__NR_lseek, fd, offset, whence);
      break;
    }
    case __NR_ioctl:
      ABORT("Unsupported Systemcall");
      break;
    case __NR_pread64: {
      int fd = getIntArg(args.at(1));
      ASSERT(fd > 2, "can not call pread/pwrite on stdin, stdout and stderr\n");
      size_t count = getIntArg(args.at(3));
      off64_t offset = getIntArg(args.at(4));
      ShadowMemEntry temp(args.at(2), count);
      //std::cout << "pread: " << " fd: " << fd << " buf: " << std::string(temp.getbuf()) << " count: " << count << " offset: " << offset << std::endl;
      retval = syscall(__NR_pread64, fd, temp.getbuf(), count, offset);
      if (retval >= 0) res = temp.writeback(res);
      break;
    }
    case __NR_pwrite64: {
      int fd = getIntArg(args.at(1));
      ASSERT(fd > 2, "can not call pread/pwrite on stdin, stdout and stderr\n");
      size_t count = getIntArg(args.at(3));
      off64_t offset = getIntArg(args.at(4));
      ShadowMemEntry temp(args.at(2), count);
      temp.readbuf(res);
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
    case __NR_ftruncate:
      ABORT("Unsupported Systemcall");
      break;
    case __NR_getcwd:
      ABORT("Unsupported Systemcall");
      break;
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

  //std::cout << "syscall_num: " << syscall_number << "  retval: " << retval << std::endl;

  return k(res, make_IntV(retval, 64));
}

inline List<SSVal> syscall(SS state, List<PtrVal> args) {
  return __syscall<List<SSVal>>(state, args, [](auto s, auto v) { return List<SSVal>{{s, v}}; });
}

inline std::monostate syscall(SS state, List<PtrVal> args, Cont k) {
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
  PtrVal va_arg = state.getVarargLoc();
  SS res = state;
  res = res.update(va_list + 0, IntV0, 4);
  res = res.update(va_list + 4, IntV0), 4;
  res = res.update(va_list + 8, va_arg + 48, 8);
  res = res.update(va_list + 16, va_arg, 8);
  return k(res, IntV0);
}
template<typename T>
inline T __llvm_va_end(SS& state, List<PtrVal>& args, __Cont<T> k) {
  PtrVal va_list = args.at(0);
  ASSERT(std::dynamic_pointer_cast<LocV>(va_list) != nullptr, "Non-location value");
  PtrVal va_arg = state.getVarargLoc();
  SS res = state;
  for (int i = 0; i<24; i++) {
    res = res.update(va_list + i, nullptr);
  }
  return k(res, IntV0);
}
template<typename T>
inline T __llvm_va_copy(SS& state, List<PtrVal>& args, __Cont<T> k) {
  PtrVal dst_va_list = args.at(0);
  PtrVal src_va_list = args.at(1);
  ASSERT(std::dynamic_pointer_cast<LocV>(dst_va_list) != nullptr, "Dest valist Non-location value");
  ASSERT(std::dynamic_pointer_cast<LocV>(src_va_list) != nullptr, "Src valist Non-location value");
  ASSERT(std::dynamic_pointer_cast<LocV>(state.at(src_va_list + 16, 8)) != nullptr, "Src valist must be initialized");
  SS res = state;
  res = res.update(dst_va_list + 0, state.at(src_va_list + 0, 4), 4);
  res = res.update(dst_va_list + 4, state.at(src_va_list + 4, 4), 4);
  res = res.update(dst_va_list + 8, state.at(src_va_list + 8, 8), 8);
  res = res.update(dst_va_list + 16, state.at(src_va_list + 16, 8), 8);
  return k(res, IntV0);
}

/******************************************************************************/

template<typename T>
inline T __llsc_assume(SS& state, List<PtrVal>& args, __Cont<T> k, __Halt<T> h) {
  auto v = args.at(0);
  auto i = v->to_IntV();
  if (i) {
    if (i->i == 0) {
      // concrete false - generate the test and ``halt''
      std::cout << "Warning: assume is unsatisfiable; abort and generate test.\n";
      return h(state, { make_IntV(-1) });
    }
    return k(state, make_IntV(1, 32));
  }
  ASSERT(std::dynamic_pointer_cast<SymV>(v) != nullptr, "Non-Symv");
  // otherwise add a symbolic condition that constraints it to be true
  // undefined/error if v is a value of other types
  auto cond = v;
  auto new_s = state.add_PC(cond);
  if (!check_pc(new_s.get_PC())) {
    std::cout << "Warning: assume is unsatisfiable; abort and generate test.\n";
    return h(new_s, { make_IntV(-1) }); // check if v == 1 is satisfiable
  }
  return k(new_s, make_IntV(1, 32));
}

#endif
