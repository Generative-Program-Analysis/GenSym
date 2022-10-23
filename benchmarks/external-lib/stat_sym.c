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
  // similar to chmod.c, but operating on a symbolic file
  // only difference is that it should branch according to what is in the file system.
  int fd;
  struct stat sfile;
  char filename[10];

#ifdef KLEE
  klee_make_symbolic(&filename, 10, "filename");
#else
  make_symbolic(&filename, 9 * sizeof(char));
#endif

  filename[9] = '\0';

  if (stat(filename, &sfile) != 0) {
    print_string("failure branch");
    return 0;
  }
  // filename must point to a valid file from here
  ASSERT_EAGER(chmod(filename, 01721) == 0, "chmod should succeed");
  ASSERT_EAGER(chown(filename, 51, 93) == 0, "chown should be successful");
  ASSERT_EAGER(stat(filename, &sfile) == 0, "stat should be successful");
  ASSERT_EAGER((sfile.st_mode & 07777) == 01721, "chmod should take effect");
  ASSERT_EAGER(sfile.st_uid == 51 && sfile.st_gid == 93, "chmod should take effect");
  return 0;
}
