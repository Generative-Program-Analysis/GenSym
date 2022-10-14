#ifdef KLEE
#include <klee/klee.h>
#else
#include "../../headers/gensym_client.h"
#endif
#include <assert.h>

int main() {
  int a;
#ifdef KLEE
  klee_make_symbolic(&a, sizeof(a), "a");
  klee_assume(a >= 1);
  klee_assume(a <= 5);
#else
  make_symbolic(&a, 4);
  gs_assume(a >= 1);
  gs_assume(a <= 5);
#endif
  switch (a) {
    case 1:
    case 2:
      printf("case 1 or 2\n");
      break;
    case 3:
    case 4:
      printf("case 3 or 4\n");
      break;
    case 5:
      printf("case 5\n");
      break;
    default:
      printf("case default, you should not see this\n");
      break;
  }
}
