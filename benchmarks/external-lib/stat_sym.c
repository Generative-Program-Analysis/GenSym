#ifdef KLEE
#include <klee/klee.h>
#endif
#include <errno.h>
#include <fcntl.h>
#include "../../headers/gensym_client.h"

int main()
{
  // similar to chmod.c, but operating on a symbolic file
  // only difference is that it should branch according to what is in the file system.
  int fd;
  struct stat sfile;
  char filename[10];

  make_symbolic(&filename, 9 * sizeof(char));
  filename[9] = '\0';

  if (stat(filename, &sfile) != 0) {
    print_string("failure branch");
    return 0;
  }
  // filename must point to a valid file from here
  gs_assert_eager(chmod(filename, 01721) == 0, "chmod should succeed");
  gs_assert_eager(chown(filename, 51, 93) == 0, "chown should be successful");
  gs_assert_eager(stat(filename, &sfile) == 0, "stat should be successful");
  gs_assert_eager((sfile.st_mode & 07777) == 01721, "chmod should take effect");
  gs_assert_eager(sfile.st_uid == 51 && sfile.st_gid == 93, "chmod should take effect");
  return 0;
}
