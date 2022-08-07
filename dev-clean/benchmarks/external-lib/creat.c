#include <fcntl.h>
#include <sys/stat.h>

/* NOTE: depends on stat, also tests for unlink <2022-05-28, David Deng> */
int main()
{
  char filename[] = "new_file";
  struct stat sfile;
  if (creat(filename, 0644) != 0) {
    sym_exit(1);
  } else if (creat(filename, 0644) != -1) {
    // recreating an existing file should return error
    sym_exit(6);
  } else if (stat(filename, &sfile) != 0) {
    // stat should be successful
    sym_exit(2);
  } else if (!S_ISREG(sfile.st_mode)) {
    // should be a file
    sym_exit(3);
  } else {
    if (unlink(filename) != 0) {
      // removal should be successful
      sym_exit(4);
    } else if (unlink(filename) != -1) {
      // re-removing the file should return error
      sym_exit(7);
    } else if (stat(filename, &sfile) != -1) {
      // file should be removed
      sym_exit(5);
    }
  }
}
