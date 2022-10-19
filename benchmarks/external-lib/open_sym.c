#ifdef KLEE
#include <klee/klee.h>
#else
#include "../../headers/gensym_client.h"
#endif
#include <errno.h>
#include <fcntl.h>

int main()
{
  int fd;
  char filename[10];
#ifdef KLEE
  klee_make_symbolic(&filename, 10, "filename");
#else
  make_symbolic(&filename, 9 * sizeof(char));
#endif
  filename[9] = '\0';
  // should fork
  fd = open(filename, O_RDONLY);
  if (fd != -1) {
    print_string("successfully opened file\n");
    close(fd);
  } else {
    print_string("failed to open file\n");
  }
  return 0;
}
