#include <fcntl.h>

int main()
{
  // --add-sym-file A
  // expected paths: 3
  char filename[] = "A";
  char buf[3];
  int fd = open(filename, O_RDONLY);
  if (read(fd, buf, 3) != 3) {
    sym_exit(1);
  }
  if (buf[0] == 'a') {
    if (buf[1] == 'b') {
      print_string("path 1/3");
    } else /* buf[0] == 'a', buf[1] != 'b' */ {
      print_string("path 2/3");
      if (buf[1] == 'b') {
        // should be unreachable, because we know that buf[1] != 'b'
        sym_exit(2);
      }
    }
  } else /* buf[0] != 'a' */ {
    print_string("path 3/3");
  }
  return 0;
}
