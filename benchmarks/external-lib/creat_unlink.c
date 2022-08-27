#include <fcntl.h>
#include <sys/stat.h>

/* NOTE: depends on stat, also tests for unlink <2022-05-28, David Deng> */
int main()
{
  char filename[] = "new_file";
  struct stat sfile;
  llsc_assert(creat(filename, 0644) != -1, "creat should succeed");
  /* llsc_assert(creat(filename, 0644) == -1, "recreating an existing file should return error"); */

  llsc_assert(stat(filename, &sfile) == 0, "stat should be successful");
  llsc_assert(S_ISREG(sfile.st_mode), "should be a file");

  llsc_assert(unlink(filename) == 0, "removal should be successful");
  llsc_assert(unlink(filename) == -1, "re-removing the file should return error");
  llsc_assert(stat(filename, &sfile) == -1, "file should be removed");
}
