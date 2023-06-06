#include "../../headers/gensym_client.h"
#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

int main() {
  int x, y, z;
  errno = EFAULT;
  make_symbolic(&x, sizeof(x));
  make_symbolic(&y, sizeof(y));
  z = gs_range(0, 10, "z");
  gs_assert_eager((z >= 0) && (z < 10));
  gs_assume(x < y);
  if (x >= y) {
    print_string("Unreachable\n");
  }
  sym_print(x, y, z);
  gs_assert_eager(errno == EFAULT);
  sym_print(errno);
  int ememb = 11;
  int *p1 = calloc(ememb, sizeof(int));    // allocate and zero out an array of ememb int
  int *p2 = calloc(1, sizeof(int[ememb])); // same, naming the array type directly

  for (int i=0;i<ememb;i++) {
    gs_assert_eager(p1[i] == p2[i]);
    gs_assert_eager(0 == p1[i]);
  }
  for (int i=0;i<ememb;i++) {
    p1[i] = i;
  }
  int *c = reallocarray(p1, ememb + 5, sizeof(int));
  for (int i=0;i<ememb;i++) {
    gs_assert_eager(i == c[i]);
  }
  sym_print(c[ememb]);
  sym_print(c[ememb + 4]);
}
