#include <fcntl.h>
#include <sys/stat.h>

/* NOTE: depends on stat, also tests for rmdir <2022-05-28, David Deng> */
int main()
{
  char dirname[] = "new_dir";
  struct stat sfile;
  if (mkdir(dirname, 0644) != 0) {
    sym_exit(1);
  } else if (mkdir(dirname, 0644) != -1) {
    // recreating an existing directory should return error
    sym_exit(6);
  } else if (stat(dirname, &sfile) != 0) {
    // stat should be successful
    sym_exit(2);
  } else if (!S_ISDIR(sfile.st_mode)) {
    // should be a directory
    sym_exit(3);
  } else {
    if (rmdir(dirname) != 0) {
      // removal should be successful
      sym_exit(4);
    } else if (rmdir(dirname) != -1) {
      // re-removing the directory should return error
      sym_exit(7);
    } else if (stat(dirname, &sfile) != -1) {
      // directory should be removed
      sym_exit(5);
    }
  }
}
