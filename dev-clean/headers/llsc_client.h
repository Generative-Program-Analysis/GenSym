#ifndef LLSC_CLIENT_HEADERS
#define LLSC_CLIENT_HEADERS

#include <stddef.h>
#include <stdbool.h>

// TODO: support assigning names for symbolic values

/* Construct `byte_size` 8-bitvectors starting at address `addr`.
 */
void make_symbolic(void* addr, size_t byte_size);

/* Construct a single bitvector of size `byte_size`*8 at address `addr`.
 */
void make_symbolic_whole(void* addr, size_t byte_size);

void llsc_assert(bool);
void llsc_assert_eager(bool);

/* Support for runing test-comp examples */

void __VERIFIER_error(void) { llsc_assert_eager(0); }
void __VERIFIER_assert(int cond) { llsc_assert_eager(cond); }
void __VERIFIER_assume(int x) { /* TODO */ }

int __VERIFIER_nondet_int(void) {
  int x;
  make_symbolic_whole(&x, sizeof(int));
  return x;
}

unsigned int __VERIFIER_nondet_uint(void) {
  unsigned int x;
  make_symbolic_whole(&x, sizeof(x));
  return x;
}

typedef unsigned int u32;
u32 __VERIFIER_nondet_u32(void) {
  return __VERIFIER_nondet_uint();
}

unsigned __VERIFIER_nondet_unsigned(void) {
  unsigned x;
  make_symbolic_whole(&x, sizeof(x));
  return x;
}

short __VERIFIER_nondet_short(void) {
  short x;
  make_symbolic_whole(&x, sizeof(x));
  return x;
}

unsigned short __VERIFIER_nondet_ushort(void) {
  unsigned short x;
  make_symbolic_whole(&x, sizeof(x));
  return x;
}

char __VERIFIER_nondet_char(void) {
  char x;
  make_symbolic_whole(&x, sizeof(x));
  return x;
}

unsigned char __VERIFIER_nondet_uchar(void) {
  unsigned char x;
  make_symbolic_whole(&x, sizeof(x));
  return x;
}

long __VERIFIER_nondet_long(void) {
  long x;
  make_symbolic_whole(&x, sizeof(x));
  return x;
}

unsigned long __VERIFIER_nondet_ulong(void) {
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
