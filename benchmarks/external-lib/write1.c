#include <fcntl.h>
#include <unistd.h>

int main()
{
  // --sym-file-size 10 --add-sym-file A
  // expected paths: 3.
  // open ---- succeed --- (buf[2] != 'c') ---- true ---|
  //      \--- fail ---|                   \
  //                                        \--- false ---|
  // NOTE: the second open will not branch because O_RDONLY has a value of 0 and will never trigger the permission for read.
  char filename[] = "A";
  char buf[5];
  int fd;

  // write to the file
  fd = open(filename, O_WRONLY);
  gs_assert(fd != -1, "open for write should succeed");
  gs_assert(write(fd, "abcd", 2) == 2, "should write 2 characters") ;
  gs_assert(close(fd) == 0, "close the file should succeed");

  fd = open(filename, O_RDONLY);
  gs_assert(fd != -1, "open for read should succeed");
  gs_assert(read(fd, buf, 4) == 4, "should read 4 bytes");
  gs_assert(buf[0] == 'a', "should contain concrete value a");
  gs_assert(buf[1] == 'b', "should contain concrete value b");
  if (buf[2] != 'c') {
    // possible, because it is symbolic
    sym_print(buf[2]);
  } else {
    sym_print(buf[2]);
  }
  gs_assert(close(fd) == 0, "close the file should succeed");

  return 0;
}
