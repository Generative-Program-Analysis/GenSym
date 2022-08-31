#include <termios.h>
#include <sys/ioctl.h>
#include <errno.h>
#include <fcntl.h>
int main()
{
  /* --add-sym-file A */
  struct termios k_termios = {0};
  int ret;

  int fd = open("A", O_RDONLY);
  llsc_assert_eager(fd != -1, "open should succeed");

  ret = ioctl(-1, TCGETS, &k_termios);
  llsc_assert(ret == -1, "ioctl should fail on invalid fd");
  llsc_assert(errno == EBADF, "errno should be set accordingly");

  ret = ioctl(fd, 0xdeadbeef, &k_termios);
  llsc_assert(ret == -1, "ioctl should fail with invalid request");
  llsc_assert(errno == EINVAL, "errno should be set accordingly");

  ret = ioctl(fd, TCGETS, &k_termios);
  llsc_assert(ret == 0, "ioctl should succeed on stdin");

  if (k_termios.c_cflag & 0x1) {
    sym_print("path 1");
  } else {
    sym_print("path 2");
  }

}
