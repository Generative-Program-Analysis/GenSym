#ifdef KLEE
#include <klee/klee.h>
#endif
#include <memory.h>
int main()
{
  int a;
// Currently does not support symbolic argument to malloc
#ifdef KLEE
  klee_make_symbolic(&a, sizeof(a), "a");
#else
  /* make_symbolic(&a, sizeof(a)); */
  a = 8;
#endif
  void *ptr = malloc(a);
  if (ptr) {
    printf("successfully allocated %d bytes\n", a);
  } else {
    printf("failed to allocate %d bytes of memory\n", a);
  }
  return 0;
}
