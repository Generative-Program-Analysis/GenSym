#include <sys/stat.h>
#include <errno.h>
#include <fcntl.h>

int main(){
  struct stat sfile;
  int fd, status;

  // --add-sym-file A

  fd = open("A", O_RDONLY);
  gs_assert_eager(fd != -1); // open should succeed

  status = fstat(fd, &sfile);
  gs_assert_eager(status == 0); // fstat should succeed

  status = fstat(-1, &sfile);
  gs_assert_eager(status == -1); // fstat should fail
  gs_assert_eager(errno == EBADF); // errno should be EBADF

  return 0;
}
