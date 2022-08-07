#include <fcntl.h>
#include <unistd.h>

int main()
{
  // --sym-file-size 10 --add-sym-file A
  char filename[] = "A";
  char buf[4];
  int fd = open(filename, O_RDONLY);
  if (read(fd, buf, 4) != 4) {
    // should read the first 4 bytes
    sym_exit(1);
  } else {
    sym_print(buf[0], buf[1], buf[2], buf[3]);
  }
  if (read(fd, buf, 4) != 4) {
    // should read the next 4 bytes
    sym_exit(2);
  } else {
    sym_print(buf[0], buf[1], buf[2], buf[3]);
  }
  if (read(fd, buf, 99) != 2) {
    // should read the last 2 bytes
    sym_exit(3);
  } else {
    sym_print(buf[0], buf[1], buf[2], buf[3]);
  }
  return 0;
}
