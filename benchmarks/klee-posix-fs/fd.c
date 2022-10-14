//===-- fd.c --------------------------------------------------------------===//
//
//                     The KLEE Symbolic Virtual Machine
//
// This file is distributed under the University of Illinois Open Source
// License. See LICENSE.TXT for details.
//
//===----------------------------------------------------------------------===//

#define _LARGEFILE64_SOURCE
#include "fd.h"

//#include "klee/klee.h"

#include <assert.h>
#include <errno.h>
#include <fcntl.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <sys/syscall.h>
#include <sys/types.h>
#ifndef __FreeBSD__
#include <sys/vfs.h>
#endif
#include <dirent.h>
#include <sys/ioctl.h>
#include <sys/mtio.h>
#include <sys/select.h>
#include <sys/time.h>
#include <termios.h>
#include <unistd.h>

/* Returns pointer to the symbolic file structure fs the pathname is symbolic */
static exe_disk_file_t *__get_sym_file(const char *pathname) {
  if (!pathname)
    return NULL;

  // Handle the case where symbolic file is given as an absolute path, ie.
  // /current/work/dir/A
  //if (pathname[0] == '/') {
  //  char cwd[1024] = {0};
  //  if (getcwd(cwd, 1024)) {
  //    size_t cwd_len = strlen(cwd);
  //    // strip trailing / if present
  //    if (cwd_len > 0 && cwd[cwd_len - 1] == '/') {
  //      cwd[--cwd_len] = '\0';
  //    }
  //    if (strncmp(pathname, cwd, cwd_len) == 0) {
  //      if (pathname[cwd_len] != '\0')
  //        pathname += cwd_len + 1;
  //    }
  //  }
  //}
  char c = pathname[0];
  unsigned i;

  if (c == 0 || pathname[1] != 0)
    return NULL;

  for (i=0; i<__exe_fs.n_sym_files; ++i) {
    if (c == 'A' + (char) i) {
      exe_disk_file_t *df = &__exe_fs.sym_files[i];
      if (df->stat->st_ino == 0)
        return NULL;
      return df;
    }
  }

  return NULL;
}

static void *__concretize_ptr(const void *p);
static size_t __concretize_size(size_t s);
static const char *__concretize_string(const char *s);

void memsetfile(exe_file_t * f) {
  f->fd = 0;
  f->flags = 0;
  f->off = 0;
  f->dfile = NULL;
}

/* Returns pointer to the file entry for a valid fd */
static exe_file_t *__get_file(int fd) {
  if (fd>=0 && fd<MAX_FDS) {
    exe_file_t *f = &__exe_env.fds[fd];
    if (f->flags & eOpen)
      return f;
  }

  return 0;
}

int access(const char *pathname, int mode) {
  exe_disk_file_t *dfile = __get_sym_file(pathname);

  if (dfile) {
    /* XXX we should check against stat values but we also need to
       enforce in open and friends then. */
    return 0;
  }
  return syscall(__NR_access, __concretize_string(pathname), mode);
}

mode_t umask(mode_t mask) {
  mode_t r = __exe_env.umask;
  __exe_env.umask = mask & 0777;
  return r;
}


/* Returns 1 if the process has the access rights specified by 'flags'
   to the file with stat 's'.  Returns 0 otherwise*/
static int has_permission(int flags, struct stat64 *s) {
  int write_access, read_access;
  mode_t mode = s->st_mode;

  if (flags & O_RDONLY || flags & O_RDWR)
    read_access = 1;
  else read_access = 0;

  if (flags & O_WRONLY || flags & O_RDWR)
    write_access = 1;
  else write_access = 0;

  /* XXX: We don't worry about process uid and gid for now.
     We allow access if any user has access to the file. */
#if 0
  uid_t uid = s->st_uid;
  uid_t euid = geteuid();
  gid_t gid = s->st_gid;
  gid_t egid = getegid();
#endif

  if (read_access && ((mode & S_IRUSR) | (mode & S_IRGRP) | (mode & S_IROTH)))
    return 0;

  if (write_access && !((mode & S_IWUSR) | (mode & S_IWGRP) | (mode & S_IWOTH)))
    return 0;

  return 1;
}


int __fd_open(const char *pathname, int flags, mode_t mode) {
  exe_disk_file_t *df;
  exe_file_t *f;
  int fd;

  for (fd = 0; fd < MAX_FDS; ++fd)
    if (!(__exe_env.fds[fd].flags & eOpen))
      break;
  if (fd == MAX_FDS) {
    errno = EMFILE;
    return -1;
  }

  f = &__exe_env.fds[fd];

  /* Should be the case if file was available, but just in case. */
  //memset(f, 0, sizeof *f);
  memsetfile(f);

  df = __get_sym_file(pathname);
  if (df) {
    /* XXX Should check access against mode / stat / possible
       deletion. */
    f->dfile = df;

    if ((flags & O_CREAT) && (flags & O_EXCL)) {
      errno = EEXIST;
      return -1;
    }

    if ((flags & O_TRUNC) && (flags & O_RDONLY)) {
      /* The result of using O_TRUNC with O_RDONLY is undefined, so we
	 return error */
      gs_warning("Undefined call to open(): O_TRUNC | O_RDONLY\n");
      errno = EACCES;
      return -1;
    }

    if ((flags & O_EXCL) && !(flags & O_CREAT)) {
      /* The result of using O_EXCL without O_CREAT is undefined, so
	 we return error */
      gs_warning("Undefined call to open(): O_EXCL w/o O_RDONLY\n");
      errno = EACCES;
      return -1;
    }

    if (!has_permission(flags, df->stat)) {
	errno = EACCES;
	return -1;
    }
    else
      f->dfile->stat->st_mode = ((f->dfile->stat->st_mode & ~0777) |
				 (mode & ~__exe_env.umask));
  } else {
    int os_fd = syscall(__NR_open, __concretize_string(pathname), flags, mode);
    if (os_fd == -1)
      return -1;
    f->fd = os_fd;
  }

  f->flags = eOpen;
  if ((flags & O_ACCMODE) == O_RDONLY) {
    f->flags |= eReadable;
  } else if ((flags & O_ACCMODE) == O_WRONLY) {
    f->flags |= eWriteable;
  } else { /* XXX What actually happens here if != O_RDWR. */
    f->flags |= eReadable | eWriteable;
  }

  return fd;
}

int __fd_openat(int basefd, const char *pathname, int flags, mode_t mode) {
  exe_file_t *f;
  int fd;
  if (basefd != AT_FDCWD) {
    exe_file_t *bf = __get_file(basefd);

    if (!bf) {
      errno = EBADF;
      return -1;
    } else if (bf->dfile) {
      gs_warning("symbolic file descriptor, ignoring (ENOENT)");
      errno = ENOENT;
      return -1;
    }
    basefd = bf->fd;
  }

  if (__get_sym_file(pathname)) {
    /* for a symbolic file, it doesn't matter if/where it exists on disk */
    return __fd_open(pathname, flags, mode);
  }

  for (fd = 0; fd < MAX_FDS; ++fd)
    if (!(__exe_env.fds[fd].flags & eOpen))
      break;
  if (fd == MAX_FDS) {
    errno = EMFILE;
    return -1;
  }

  f = &__exe_env.fds[fd];

  /* Should be the case if file was available, but just in case. */
  //memset(f, 0, sizeof *f);
  memsetfile(f);

  int os_fd = syscall(__NR_openat, (long)basefd, __concretize_string(pathname), (long)flags, mode);
  if (os_fd == -1)
    return -1;

  f->fd = os_fd;
  f->flags = eOpen;
  if ((flags & O_ACCMODE) == O_RDONLY) {
    f->flags |= eReadable;
  } else if ((flags & O_ACCMODE) == O_WRONLY) {
    f->flags |= eWriteable;
  } else { /* XXX What actually happens here if != O_RDWR. */
    f->flags |= eReadable | eWriteable;
  }

  return fd;
}


int klee_close(int fd) {
  static int n_calls = 0;
  exe_file_t *f;
  int r = 0;

  n_calls++;

  f = __get_file(fd);
  if (!f) {
    errno = EBADF;
    return -1;
  }

  if (__exe_fs.max_failures && *__exe_fs.close_fail == n_calls) {
    __exe_fs.max_failures--;
    errno = EIO;
    return -1;
  }

#if 0
  if (!f->dfile) {
    /* if a concrete fd */
    r = syscall(__NR_close, f->fd);
  }
  else r = 0;
#endif

  //memset(f, 0, sizeof *f);
  memsetfile(f);

  return r;
}

// Todo: add check for if f is readable
ssize_t klee_read(int fd, void *buf, size_t count) {
  static int n_calls = 0;
  exe_file_t *f;

  n_calls++;

  if (count == 0)
    return 0;

  if (buf == NULL) {
    errno = EFAULT;
    return -1;
  }

  f = __get_file(fd);

  if (!f) {
    errno = EBADF;
    return -1;
  }

  if (__exe_fs.max_failures && *__exe_fs.read_fail == n_calls) {
    __exe_fs.max_failures--;
    errno = EIO;
    return -1;
  }

  if (!f->dfile) {
    /* concrete file */
    int r;
    buf = __concretize_ptr(buf);
    count = __concretize_size(count);
    /* XXX In terms of looking for bugs we really should do this check
       before concretization, at least once the routine has been fixed
       to properly work with symbolics. */
    //klee_check_memory_access(buf, count);
    if (f->fd == 0)
      r = syscall(__NR_read, f->fd, buf, count);
    else
      r = syscall(__NR_pread64, f->fd, buf, count, (off64_t) f->off);

    if (r == -1)
      return -1;

    if (f->fd != 0)
      f->off += r;
    return r;
  }
  else {
    assert(f->off >= 0);
    if (((off64_t)f->dfile->size) < f->off)
      return 0;

    /* symbolic file */
    if (f->off + count > f->dfile->size) {
      count = f->dfile->size - f->off;
    }

    memcpy(buf, f->dfile->contents + f->off, count);
    f->off += count;

    return count;
  }
}


// Todo: add check for if f is writeable
ssize_t klee_write(int fd, const void *buf, size_t count) {
  static int n_calls = 0;
  exe_file_t *f;

  n_calls++;

  f = __get_file(fd);

  if (!f) {
    errno = EBADF;
    return -1;
  }

  if (__exe_fs.max_failures && *__exe_fs.write_fail == n_calls) {
    __exe_fs.max_failures--;
    errno = EIO;
    return -1;
  }

  if (!f->dfile) {
    int r;

    buf = __concretize_ptr(buf);
    count = __concretize_size(count);
    /* XXX In terms of looking for bugs we really should do this check
       before concretization, at least once the routine has been fixed
       to properly work with symbolics. */
    //klee_check_memory_access(buf, count);
    if (f->fd == 1 || f->fd == 2)
      r = syscall(__NR_write, f->fd, buf, count);
    else r = syscall(__NR_pwrite64, f->fd, buf, count, (off64_t) f->off);

    if (r == -1)
      return -1;

    assert(r >= 0);
    if (f->fd != 1 && f->fd != 2)
      f->off += r;

    return r;
  }
  else {
    /* symbolic file */
    size_t actual_count = 0;
    if (f->off + count <= f->dfile->size)
      actual_count = count;
    else {
      if (__exe_env.save_all_writes)
	assert(0);
      else {
	if (f->off < (off64_t) f->dfile->size)
	  actual_count = f->dfile->size - f->off;
      }
    }

    if (actual_count)
      memcpy(f->dfile->contents + f->off, buf, actual_count);

    if (count != actual_count)
      gs_warning("write() ignores bytes.\n");

    if (f->dfile == __exe_fs.sym_stdout)
      __exe_fs.stdout_writes += actual_count;

    f->off += count;
    return count;
  }
}


off64_t __fd_lseek(int fd, off64_t offset, int whence) {
  off64_t new_off;
  exe_file_t *f = __get_file(fd);

  if (!f) {
    errno = EBADF;
    return -1;
  }

  if (!f->dfile) {
    /* We could always do SEEK_SET then whence, but this causes
       troubles with directories since we play nasty tricks with the
       offset, and the OS doesn't want us to randomly seek
       directories. We could detect if it is a directory and correct
       the offset, but really directories should only be SEEK_SET, so
       this solves the problem. */
    if (whence == SEEK_SET) {
      new_off = syscall(__NR_lseek, f->fd, offset, SEEK_SET);
    } else {
      new_off = syscall(__NR_lseek, f->fd, f->off, SEEK_SET);

      /* If we can't seek to start off, just return same error.
         Probably ESPIPE. */
      if (new_off != -1) {
        assert(new_off == f->off);
        new_off = syscall(__NR_lseek, f->fd, offset, whence);
      }
    }

    if (new_off == -1)
      return -1;

    f->off = new_off;
    return new_off;
  }

  switch (whence) {
  case SEEK_SET: new_off = offset; break;
  case SEEK_CUR: new_off = f->off + offset; break;
  case SEEK_END: new_off = f->dfile->size + offset; break;
  default: {
    errno = EINVAL;
    return (off64_t) -1;
  }
  }

  if (new_off < 0) {
    errno = EINVAL;
    return (off64_t) -1;
  }

  f->off = new_off;
  return f->off;
}

int __fd_stat(const char *path, struct stat64 *buf) {
  exe_disk_file_t *dfile = __get_sym_file(path);
  if (dfile) {
    memcpy(buf, dfile->stat, sizeof(*dfile->stat));
    return 0;
  }

  {
#if __WORDSIZE == 64
    return syscall(__NR_stat, __concretize_string(path), buf);
#else
    return syscall(__NR_stat64, __concretize_string(path), buf);
#endif
  }
}

//#undef FD_SET
//#undef FD_CLR
//#undef FD_ISSET
//#undef FD_ZERO
//#define	FD_SET(n, p)	((p)->fds_bits[(n)/NFDBITS] |= (1 << ((n) % NFDBITS)))
//#define	FD_CLR(n, p)	((p)->fds_bits[(n)/NFDBITS] &= ~(1 << ((n) % NFDBITS)))
//#define	FD_ISSET(n, p)	((p)->fds_bits[(n)/NFDBITS] & (1 << ((n) % NFDBITS)))
//#define FD_ZERO(p)	memset((char *)(p), '\0', sizeof(*(p)))

/*** Helper functions ***/

static void *__concretize_ptr(const void *p) {
  /* XXX 32-bit assumption */
  //char *pc = (char*) klee_get_valuel((long) p);
  //klee_assume(pc == p);
  //return pc;
  return p;
}

static size_t __concretize_size(size_t s) {
  //size_t sc = klee_get_valuel((long)s);
  //klee_assume(sc == s);
  //return sc;
  return s;
}

static const char *__concretize_string(const char *s) {
  //char *sc = __concretize_ptr(s);
  //unsigned i;
//
  //for (i = 0;; ++i, ++sc) {
  //  char c = *sc;
  //  // Avoid writing read-only memory locations
  //  if (!klee_is_symbolic(c)) {
  //    if (!c)
  //      break;
  //    continue;
  //  }
  //  if (!(i&(i-1))) {
  //    if (!c) {
  //      *sc = 0;
  //      break;
  //    } else if (c=='/') {
  //      *sc = '/';
  //    }
  //  } else {
  //    char cc = (char) klee_get_valuel((long)c);
  //    klee_assume(cc == c);
  //    *sc = cc;
  //    if (!cc) break;
  //  }
  //}

  return s;
}
