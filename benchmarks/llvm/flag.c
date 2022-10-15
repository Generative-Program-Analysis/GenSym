#include <fcntl.h>
#include <sys/stat.h>
#include "../../headers/gensym_client.h"

/* NOTE: depends on stat, also tests for unlink <2022-05-28, David Deng> */
int main()
{
  char buf[4];
  for (int i = 0; i < 4; ++i) {
    make_symbolic(buf+i, sizeof(char));
  }
  int y = *(int*)buf;
  int x = y;
  x = x & 0xffff0fff;
  x = x | 0x00008000;
  x = x & 0x0000f000;
  /* x = x == 0x00008000; */
  sym_print(x == 0x00008000);
  gs_assert_eager(x == 0x00008000, "should be cleared");
  return 0;
}
