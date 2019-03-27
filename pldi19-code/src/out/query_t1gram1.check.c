#include <fcntl.h>
#include <errno.h>
#include <err.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <stdio.h>
#include <stdint.h>
#include <unistd.h>
#ifndef MAP_FILE
#define MAP_FILE MAP_SHARED
#endif
int fsize(int fd) {
  struct stat stat;
  int res = fstat(fd,&stat);
  return stat.st_size;
}
int printll(char* s) {
  while (*s != '\n' && *s != ',' && *s != '\t') {
    putchar(*s++);
  }
  return 0;
}
long hash(char *str0, int len)
{
  unsigned char* str = (unsigned char*)str0;
  unsigned long hash = 5381;
  int c;
  while ((c = *str++) && len--)
  hash = ((hash << 5) + hash) + c; /* hash * 33 + c */
  return hash;
}
void Snippet(char*);
int main(int argc, char *argv[])
{
  if (argc != 2) {
    printf("usage: query <filename>\n");
    return 0;
  }
  Snippet(argv[1]);
  return 0;
}
/*****************************************
Emitting C Generated Code
*******************************************/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
void Snippet(char*  x0) {
  printf("%s\n","Phrase,Year,MatchCount,VolumeCount");
  int32_t x5 = 0;
  int32_t x2 = open(x0,0);
  int32_t x3 = fsize(x2);
  char* x4 = mmap(0, x3, PROT_READ, MAP_FILE | MAP_SHARED, x2, 0);
  for (;;) {
    int32_t x6 = x5;
    bool x7 = x6 < x3;
    if (!x7) break;
    int32_t x9 = x5;
    for (;;) {
      int32_t x10 = x5;
      char x11 = x4[x10];
      bool x12 = x11 != '\t';
      if (!x12) break;
      x5 += 1;
    }
    int32_t x17 = x5;
    x5 += 1;
    int32_t x21 = x5;
    for (;;) {
      int32_t x22 = x5;
      char x23 = x4[x22];
      bool x24 = x23 != '\t';
      if (!x24) break;
      x5 += 1;
    }
    int32_t x29 = x5;
    x5 += 1;
    int32_t x33 = x5;
    for (;;) {
      int32_t x34 = x5;
      char x35 = x4[x34];
      bool x36 = x35 != '\t';
      if (!x36) break;
      x5 += 1;
    }
    int32_t x41 = x5;
    x5 += 1;
    int32_t x45 = x5;
    for (;;) {
      int32_t x46 = x5;
      char x47 = x4[x46];
      bool x48 = x47 != '\n';
      if (!x48) break;
      x5 += 1;
    }
    int32_t x53 = x5;
    x5 += 1;
    char* x20 = x4+x9;
    int32_t x57 = printll(x20);
    printf(",");
    char* x32 = x4+x21;
    int32_t x59 = printll(x32);
    printf(",");
    char* x44 = x4+x33;
    int32_t x61 = printll(x44);
    printf(",");
    char* x56 = x4+x45;
    int32_t x63 = printll(x56);
    printf("%s\n","");
  }
  close(x2);
}
/*****************************************
End of C Generated Code
*******************************************/
