#include <fcntl.h>

int main()
{
  // --sym-file-size 10 --add-sym-file A
  // expected paths: 2
  char filename[] = "A";
  char buf[5];
  int fd;

  // write to the file
  fd = open(filename, O_WRONLY);
  if (fd == -1) sym_exit(1);
  if (write(fd, "abcd", 2) != 2) sym_exit(2); // write 2 characters
  if (close(fd) != 0) sym_exit(3); // close the file

  fd = open(filename, O_RDONLY);
  if (fd == -1) sym_exit(4);
  if (read(fd, buf, 4) != 4) sym_exit(5);
  if (buf[0] != 'a') sym_exit(6);
  if (buf[1] != 'b') sym_exit(7);
  if (buf[2] != 'c') {
    // possible, because it is symbolic
    sym_print(buf[2]);
  } else {
    sym_print(buf[2]);
  }
  if (close(fd) != 0) sym_exit(8); // close the file

  return 0;
}
