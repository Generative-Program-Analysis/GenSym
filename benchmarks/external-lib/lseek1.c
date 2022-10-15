#include <fcntl.h>
#include <unistd.h>
#include <errno.h>

int main()
{
  // --sym-file-size 10 --add-sym-file A
  off_t pos;
  char filename[] = "A";
  char buf[4];
  int fd = open(filename, O_RDONLY);
  gs_assert_eager(fd != -1);

  pos = lseek(fd, 5, SEEK_SET);
  gs_assert_eager(pos == 5);
  pos = lseek(fd, 2, SEEK_CUR);
  gs_assert_eager(pos == 7);

  // seek an absolute position
  pos = lseek(fd, 1, SEEK_SET);
  gs_assert_eager(pos == 1);

  // seek beyond end
  pos = lseek(fd, 3, SEEK_END);
  gs_assert_eager(pos == 10 + 3);

  // invalid seeks
  errno = 0;
  pos = lseek(fd, -5, SEEK_SET);
  gs_assert_eager(pos == -1);
  gs_assert_eager(errno == EINVAL);

  lseek(fd, 0, SEEK_SET);
  errno = 0;
  pos = lseek(fd, -3, SEEK_CUR);
  gs_assert_eager(pos == -1);
  gs_assert_eager(errno == EINVAL);

  errno = 0;
  pos = lseek(fd, -12, SEEK_END);
  gs_assert_eager(pos == -1);
  gs_assert_eager(errno == EINVAL);

  errno = 0;
  pos = lseek(-1, 0, SEEK_CUR);
  gs_assert_eager(pos == -1);
  gs_assert_eager(errno == EBADF);

  return 0;
}
