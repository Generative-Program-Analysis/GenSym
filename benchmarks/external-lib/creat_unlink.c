#include <fcntl.h>
#include <sys/stat.h>
#include "../../headers/gensym_client.h"

/* NOTE: depends on stat, also tests for unlink <2022-05-28, David Deng> */
int main()
{
  char filename[] = "new_file";
  struct stat sfile;

  // can branch here, possible that creat fails due to permission error
  if (creat(filename, 0644) == -1) {
    return 0;
  }

  gs_assert_eager(stat(filename, &sfile) == 0, "stat should be successful");
  gs_assert_eager(S_ISREG(sfile.st_mode), "should be a file");

  gs_assert_eager(unlink(filename) == 0, "removal should be successful");
  gs_assert_eager(unlink(filename) == -1, "re-removing the file should return error");
  gs_assert_eager(stat(filename, &sfile) == -1, "file should be removed");
  return 0;
}
