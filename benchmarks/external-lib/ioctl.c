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
  gs_assert_eager(fd != -1, "open should succeed");

  ret = ioctl(-1, TCGETS, &k_termios);
  gs_assert(ret == -1, "ioctl should fail on invalid fd");
  gs_assert(errno == EBADF, "errno should be set accordingly");

  ret = ioctl(fd, 0xdeadbeef, &k_termios);
  gs_assert(ret == -1, "ioctl should fail with invalid request");
  gs_assert(errno == EINVAL, "errno should be set accordingly");

  ret = ioctl(fd, TCGETS, &k_termios);
  gs_assert(ret == -1, "TCGETS only works for character devices");

  /* TODO: test that k_termios is correctly set
   * need mechanism to create a character device first <2022-10-01, David Deng> */
  /* if (k_termios.c_cflag & 0x1) { */
  /*   sym_print("path 1"); */
  /* } else { */
  /*   sym_print("path 2"); */
  /* } */

}
