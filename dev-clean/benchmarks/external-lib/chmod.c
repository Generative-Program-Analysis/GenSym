#include <fcntl.h>
#include <sys/stat.h>

/* NOTE: depends on creat, stat, also tests for chown <2022-05-28, David Deng> */
int main()
{
  char filename[] = "new_file";
  struct stat sfile;
  llsc_assert_eager(creat(filename, 0644) != -1, "creat should succeed");
  llsc_assert_eager(chmod(filename, 01721) == 0, "chmod should succeed");
  llsc_assert_eager(chown(filename, 51, 93) == 0, "chown should be successful");
  llsc_assert_eager(stat(filename, &sfile) == 0, "stat should be successful");
  llsc_assert_eager((sfile.st_mode & 07777) == 01721, "chmod should take effect");
  llsc_assert_eager(sfile.st_uid == 51 && sfile.st_gid == 93, "chmod should take effect");
  return 0;
}
