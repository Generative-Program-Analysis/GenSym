#ifdef KLEE
#include <klee/klee.h>
#endif
#include <fcntl.h>
#include <unistd.h>
#include <assert.h>
#include <stdio.h>

int main()
{
  char filename[] = "A";
  int fd = open(filename, O_RDONLY | O_TRUNC);
  if (fd == -1) {
    printf("failed to open file %s\n", filename);
  } else {
    printf("successfully opened file %s\n", filename);
    close(fd);
  }
  return 0;
}
