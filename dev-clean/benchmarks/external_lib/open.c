#ifdef KLEE
#include <klee/klee.h>
#endif
#include <fcntl.h>

int main()
{
  char filename[] = "A";
  int fd = open(filename, O_RDONLY);
  if (fd == -1) {
    // no parameter
    sym_exit(1);
  } else {
    // --add-sym-file A
    sym_exit(0);
  }
}
