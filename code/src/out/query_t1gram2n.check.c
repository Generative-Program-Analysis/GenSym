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
  printf("%s\n","Word,Value,Word,Year,MatchCount,VolumeCount");
  int32_t x5 = 0;
  int32_t x2 = open("src/data/words.csv",0);
  int32_t x3 = fsize(x2);
  char* x4 = mmap(0, x3, PROT_READ, MAP_FILE | MAP_SHARED, x2, 0);
  for (;;) {
    int32_t x6 = x5;
    char x7 = x4[x6];
    bool x8 = x7 != ',';
    if (!x8) break;
    x5 += 1;
  }
  int32_t x13 = x5;
  x5 += 1;
  int32_t x16 = x5;
  int32_t x17 = 0;
  for (;;) {
    int32_t x18 = x5;
    char x19 = x4[x18];
    bool x20 = x19 != '\n';
    if (!x20) break;
    int32_t x22 = x17;
    int32_t x24 = x5;
    int32_t x23 = x22 * 10;
    char x25 = x4[x24];
    char x26 = x25 - '0';
    int32_t x27 = x23 + x26;
    x17 = x27;
    x5 += 1;
  }
  x5 += 1;
  int32_t x33 = x17;
  int32_t x67 = open(x0,0);
  int32_t x68 = fsize(x67);
  char* x69 = mmap(0, x68, PROT_READ, MAP_FILE | MAP_SHARED, x67, 0);
  for (;;) {
    int32_t x34 = x5;
    bool x35 = x34 < x3;
    if (!x35) break;
    int32_t x37 = x5;
    for (;;) {
      int32_t x38 = x5;
      char x39 = x4[x38];
      bool x40 = x39 != ',';
      if (!x40) break;
      x5 += 1;
    }
    int32_t x45 = x5;
    x5 += 1;
    int32_t x49 = x5;
    int32_t x50 = 0;
    for (;;) {
      int32_t x51 = x5;
      char x52 = x4[x51];
      bool x53 = x52 != '\n';
      if (!x53) break;
      int32_t x55 = x50;
      int32_t x57 = x5;
      int32_t x56 = x55 * 10;
      char x58 = x4[x57];
      char x59 = x58 - '0';
      int32_t x60 = x56 + x59;
      x50 = x60;
      x5 += 1;
    }
    x5 += 1;
    int32_t x66 = x50;
    int32_t x70 = 0;
    int32_t x46 = x45 - x37;
    char* x48 = x4+x37;
    for (;;) {
      int32_t x71 = x70;
      bool x72 = x71 < x68;
      if (!x72) break;
      int32_t x74 = x70;
      for (;;) {
        int32_t x75 = x70;
        char x76 = x69[x75];
        bool x77 = x76 != '\t';
        if (!x77) break;
        x70 += 1;
      }
      int32_t x82 = x70;
      x70 += 1;
      int32_t x86 = x70;
      for (;;) {
        int32_t x87 = x70;
        char x88 = x69[x87];
        bool x89 = x88 != '\t';
        if (!x89) break;
        x70 += 1;
      }
      int32_t x94 = x70;
      x70 += 1;
      int32_t x98 = x70;
      for (;;) {
        int32_t x99 = x70;
        char x100 = x69[x99];
        bool x101 = x100 != '\t';
        if (!x101) break;
        x70 += 1;
      }
      int32_t x106 = x70;
      x70 += 1;
      int32_t x110 = x70;
      for (;;) {
        int32_t x111 = x70;
        char x112 = x69[x111];
        bool x113 = x112 != '\n';
        if (!x113) break;
        x70 += 1;
      }
      int32_t x118 = x70;
      x70 += 1;
      int32_t x83 = x82 - x74;
      bool x123 = x46 == x83;
      bool x138;
      if (x123) {
        int32_t x124 = 0;
        char* x85 = x69+x74;
        for (;;) {
          int32_t x125 = x124;
          bool x126 = x125 < x46;
          bool x130;
          if (x126) {
            char x127 = x48[x125];
            char x128 = x85[x125];
            bool x129 = x127 == x128;
            x130 = x129;
          } else {
            x130 = false;
          }
          if (!x130) break;
          x124 += 1;
        }
        int32_t x135 = x124;
        bool x136 = x135 == x46;
        x138 = x136;
      } else {
        x138 = false;
      }
      if (x138) {
        int32_t x139 = printll(x48);
        printf(",");
        printf("%d",x66);
        printf(",");
        char* x85 = x69+x74;
        int32_t x143 = printll(x85);
        printf(",");
        char* x97 = x69+x86;
        int32_t x145 = printll(x97);
        printf(",");
        char* x109 = x69+x98;
        int32_t x147 = printll(x109);
        printf(",");
        char* x121 = x69+x110;
        int32_t x149 = printll(x121);
        printf("%s\n","");
      } else {
      }
    }
    close(x67);
  }
  close(x2);
}
/*****************************************
End of C Generated Code
*******************************************/
