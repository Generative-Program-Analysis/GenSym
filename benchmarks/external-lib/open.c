#ifdef KLEE
#include <klee/klee.h>
#define ASSERT_EAGER(cond, msg) klee_assert(cond)
#else
#include "../../headers/gensym_client.h"
#define ASSERT_EAGER(cond, msg) gs_assert_eager(cond, msg)
#endif
#include <errno.h>
#include <fcntl.h>

int main()
{
  // --add-sym-file A
  int fd;

  fd = -1; 
  fd = open("A", O_RDONLY);
  ASSERT_EAGER(fd != -1, "open should succeed");
  ASSERT_EAGER(close(fd) == 0, "close should succeed");

  fd = 0;
  fd = open("B", O_RDONLY);
  ASSERT_EAGER(fd == -1, "open should fail");
  ASSERT_EAGER(errno == ENOENT, "errno should be set to EACCES");

  fd = 0;
  fd = open("A", O_CREAT | O_EXCL);
  ASSERT_EAGER(fd == -1, "open should fail");
  ASSERT_EAGER(errno == EEXIST, "errno should be set to EEXIST");

  fd = 0;
  fd = open("B", O_CREAT);
  ASSERT_EAGER(fd != -1, "open should succeed with file created");
}
