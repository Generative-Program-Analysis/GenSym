#include <termios.h>
#include <sys/ioctl.h>
#include <errno.h>
int main()
{
  /* --sym-stdin 1 */
  struct termios k_termios;
  int ret;
  ret = ioctl(-1, TCGETS, &k_termios);
  llsc_assert_eager(ret == -1, "ioctl should fail on invalid fd");
  llsc_assert_eager(errno == EBADF, "errno should be set accordingly");

  ret = ioctl(0, 0xdeadbeef, &k_termios);
  llsc_assert_eager(ret == -1, "ioctl should fail with invalid request");
  llsc_assert_eager(errno == EINVAL, "errno should be set accordingly");

  ret = ioctl(0, TCGETS, &k_termios);
  llsc_assert_eager(ret == 0, "ioctl should succeed on stdin");

  if (k_termios.c_cflag & 0x1) {
    sym_print("path 1");
  } else {
    sym_print("path 2");
  }

}
