#ifdef KLEE
#include <klee/klee.h>
#endif
#include <errno.h>
#include <fcntl.h>

int main()
{
  int fd;
  char fn[10];
  make_symbolic(&fn, 9 * sizeof(char));
  fn[9] = '\0';
  // should fork
  fd = open(fn, O_RDONLY);
  if (fd != -1) {
    print_string("successfully opened file\n");
    close(fd);
  } else {
    print_string("failed to open file\n");
  }
  return 0;
}
