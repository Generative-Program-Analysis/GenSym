#include "../../headers/llsc_client.h"
#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main() {
  int x, y, z;
  errno = EFAULT;
  make_symbolic(&x, sizeof(x));
  make_symbolic(&y, sizeof(y));
  z = llsc_range(0, 10, "z");
  llsc_assert_eager((z >= 0) && (z < 10));
  llsc_assume(x < y);
  if (x >= y) {
    print_string("Unreachable\n");
  }
  sym_print(x, y, z);
  llsc_assert_eager(errno == EFAULT);
  sym_print(errno);
  int ememb = 11;
  int *p1 = calloc(ememb, sizeof(int));    // allocate and zero out an array of ememb int
  int *p2 = calloc(1, sizeof(int[ememb])); // same, naming the array type directly
  for (int i=0;i<ememb;i++) {
    llsc_assert_eager(p1[i] == p2[i]);
    llsc_assert_eager(0 == p1[i]);
  }
}
