#include <fcntl.h>
#include <sys/stat.h>

/* NOTE: depends on creat, stat, also tests for chown <2022-05-28, David Deng> */
int main()
{
  char filename[] = "new_file";
  struct stat sfile;
  if (creat(filename, 0644) != 0) {
    sym_exit(1);
  } else if (chmod(filename, 01721) != 0) {
    // chmod should succeed
    sym_exit(2);
  } else if (chown(filename, 51, 93) != 0) {
    // chown should be successful
    sym_exit(3);
  } else if (stat(filename, &sfile) != 0) {
    // stat should be successful
    sym_exit(4);
  } else if ((sfile.st_mode & 07777) != 01721) {
    // chmod should take effect
    sym_exit(5);
  } else if (sfile.st_uid != 51 || sfile.st_gid != 93) {
    // chmod should take effect
    sym_exit(6);
  } else {
    sym_exit(0);
  }
}
