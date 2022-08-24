#include <fcntl.h>
#include <sys/stat.h>

/* NOTE: depends on creat, stat, also tests for chown <2022-05-28, David Deng> */
int main()
{
  char filename[] = "new_file";
  struct stat sfile;
  llsc_assert(creat(filename, 0644) != -1, "creat should succeed");
  llsc_assert(chmod(filename, 01721) == 0, "chmod should succeed");
  llsc_assert(chown(filename, 51, 93) == 0, "chown should be successful");
  llsc_assert(stat(filename, &sfile) == 0, "stat should be successful");
  llsc_assert((sfile.st_mode & 07777) == 01721, "chmod should take effect");
  llsc_assert(sfile.st_uid == 51 && sfile.st_gid == 93, "chmod should take effect");
  return 0;
}
