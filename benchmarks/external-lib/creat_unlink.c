#include <fcntl.h>
#include <sys/stat.h>
#include "../../headers/llsc_client.h"

/* NOTE: depends on stat, also tests for unlink <2022-05-28, David Deng> */
int main()
{
  char filename[] = "new_file";
  struct stat sfile;
  // can branch here, possible that creat fails due to permission error
  llsc_assert(creat(filename, 0644) != -1, "creat should succeed");

  llsc_assert_eager(stat(filename, &sfile) == 0, "stat should be successful");
  llsc_assert_eager(S_ISREG(sfile.st_mode), "should be a file");

  llsc_assert_eager(unlink(filename) == 0, "removal should be successful");
  llsc_assert_eager(unlink(filename) == -1, "re-removing the file should return error");
  llsc_assert_eager(stat(filename, &sfile) == -1, "file should be removed");
}
