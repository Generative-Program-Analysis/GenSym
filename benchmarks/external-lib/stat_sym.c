#ifdef KLEE
#include <klee/klee.h>
#endif
#include <errno.h>
#include <fcntl.h>

int main()
{
  // similar to chmod.c, but operating on a symbolic file
  // only difference is that it should branch according to what is in the file system.
  int fd;
  char filename[10];
  make_symbolic(&filename, 9 * sizeof(char));
  filename[9] = '\0';

  struct stat sfile;
  llsc_assert(chmod(filename, 01721) == 0, "chmod should succeed");
  llsc_assert(chown(filename, 51, 93) == 0, "chown should be successful");
  llsc_assert(stat(filename, &sfile) == 0, "stat should be successful");
  llsc_assert((sfile.st_mode & 07777) == 01721, "chmod should take effect");
  llsc_assert(sfile.st_uid == 51 && sfile.st_gid == 93, "chmod should take effect");
  return 0;
}
