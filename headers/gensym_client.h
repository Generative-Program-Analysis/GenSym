#ifndef GS_CLIENT_HEADERS
#define GS_CLIENT_HEADERS

#include <stdio.h>
#include <stdint.h>
#include <stddef.h>
#include <stdarg.h>
#include <stdbool.h>

/* Construct `byte_size` 8-bitvectors starting at address `addr`.
 */
void make_symbolic(void* addr, size_t byte_size, ...);

/* Construct a single bitvector of size `byte_size`*8 at address `addr`.
 */
void make_symbolic_whole(void* addr, size_t byte_size, ...);

#define gs_make_symbolic(addr, byte_size, name) make_symbolic(addr, byte_size, name)
#define gs_make_symbolic_whole(addr, byte_size, name) make_symbolic_whole(addr, byte_size, name)

void gs_assert(bool, ...);
void gs_assert_eager(bool, ...);

void gs_assume(bool);
void sym_print(int, ...);
void print_string(const char *message);
void sym_exit(int);

/* gs_range - Construct a symbolic value in the signed interval
 * [begin,end).
 *
 * \arg name - A name used for identifying the object in messages, output
 * files, etc. If NULL, object is called "unnamed".
 */
static inline int gs_range(int begin, int end, const char *name) {
  int x;
  gs_make_symbolic(&x, sizeof(x), name);
  gs_assume(x >= begin);
  gs_assume(x < end);
  return x;
}

__attribute__((noreturn))
void stop(int status);

/* gs_report_error - Report a user defined error and terminate the current
 * gensym process.
 *
 * \arg file - The filename to report in the error message.
 * \arg line - The line number to report in the error message.
 * \arg message - A string to include in the error message.
 * \arg suffix - The suffix to use for error files.
 */
__attribute__((noreturn))
static inline void gs_report_error(const char *file,
		 int line,
		 const char *message,
		 const char *suffix)  {
  print_string(file);
  sym_print(line);
  print_string(message);
  print_string(suffix);
  print_string("\n");
  stop(-1);
}

static inline void gs_warning(const char *message) {
  print_string(message);
  print_string("\n");
}

void gs_warning_once(const char *message);

void gs_prefer_cex(void *object, bool condition);
void gs_posix_prefer_cex(void *object, bool condition);

/* return whether n is a symbolic value */
unsigned gs_is_symbolic(uintptr_t n);

/* return a feasible concrete value of expr */
long gs_get_valuel(long expr);

/* Support for running test-comp examples */

static inline void __VERIFIER_error(void) { gs_assert_eager(0); }
static inline void __VERIFIER_assert(int cond) { gs_assert_eager(cond); }
static inline void reach_error() {
  fprintf(stderr, "error reached\n");
  gs_assert_eager(0);
}
static inline void __VERIFIER_assume(int x) { /* TODO */ }

static inline int __VERIFIER_nondet_int(void) {
  int x;
  make_symbolic_whole(&x, sizeof(int));
  return x;
}

static inline unsigned int __VERIFIER_nondet_uint(void) {
  unsigned int x;
  make_symbolic_whole(&x, sizeof(x));
  return x;
}

typedef unsigned int u32;
static inline u32 __VERIFIER_nondet_u32(void) {
  return __VERIFIER_nondet_uint();
}

static inline unsigned __VERIFIER_nondet_unsigned(void) {
  unsigned x;
  make_symbolic_whole(&x, sizeof(x));
  return x;
}

static inline short __VERIFIER_nondet_short(void) {
  short x;
  make_symbolic_whole(&x, sizeof(x));
  return x;
}

static inline unsigned short __VERIFIER_nondet_ushort(void) {
  unsigned short x;
  make_symbolic_whole(&x, sizeof(x));
  return x;
}

static inline char __VERIFIER_nondet_char(void) {
  char x;
  make_symbolic_whole(&x, sizeof(x));
  return x;
}

static inline unsigned char __VERIFIER_nondet_uchar(void) {
  unsigned char x;
  make_symbolic_whole(&x, sizeof(x));
  return x;
}

static inline long __VERIFIER_nondet_long(void) {
  long x;
  make_symbolic_whole(&x, sizeof(x));
  return x;
}

static inline unsigned long __VERIFIER_nondet_ulong(void) {
  unsigned long x;
  make_symbolic_whole(&x, sizeof(x));
  return x;
}

/* Note: to be supported...
char* __VERIFIER_nondet_pchar(void) { }
float __VERIFIER_nondet_float(void) { }
double __VERIFIER_nondet_double(void) { }
void* __VERIFIER_nondet_pointer(void) { }
_Bool __VERIFIER_nondet_bool(void) { }
*/

#endif
