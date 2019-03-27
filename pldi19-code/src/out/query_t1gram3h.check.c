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
  printf("%s\n","Word,Value,Phrase,Year,MatchCount,VolumeCount");
  int32_t x2 = 0;
  int32_t x3 = 0;
  int32_t* x4 = (int32_t*)malloc(256 * sizeof(int32_t));
  for(int x6=0; x6 < 256; x6++) {
    x4[x6] = -1;
  }
  char** x10 = (char**)malloc(65536 * sizeof(char*));
  int32_t* x11 = (int32_t*)malloc(65536 * sizeof(int32_t));
  int32_t* x12 = (int32_t*)malloc(65536 * sizeof(int32_t));
  int32_t x13 = 0;
  int32_t x14 = 0;
  int32_t* x15 = (int32_t*)malloc(65536 * sizeof(int32_t));
  int32_t* x16 = (int32_t*)malloc(256 * sizeof(int32_t));
  int32_t x20 = 0;
  int32_t x17 = open("src/data/words.csv",0);
  int32_t x18 = fsize(x17);
  char* x19 = mmap(0, x18, PROT_READ, MAP_FILE | MAP_SHARED, x17, 0);
  for (;;) {
    int32_t x21 = x20;
    char x22 = x19[x21];
    bool x23 = x22 != ',';
    if (!x23) break;
    x20 += 1;
  }
  int32_t x28 = x20;
  x20 += 1;
  int32_t x31 = x20;
  int32_t x32 = 0;
  for (;;) {
    int32_t x33 = x20;
    char x34 = x19[x33];
    bool x35 = x34 != '\n';
    if (!x35) break;
    int32_t x37 = x32;
    int32_t x39 = x20;
    int32_t x38 = x37 * 10;
    char x40 = x19[x39];
    char x41 = x40 - '0';
    int32_t x42 = x38 + x41;
    x32 = x42;
    x20 += 1;
  }
  x20 += 1;
  int32_t x48 = x32;
  int32_t x87 = (int32_t)0L;
  int32_t x88 = x87 & 255;
  bool x94 = !true;
  for (;;) {
    int32_t x49 = x20;
    bool x50 = x49 < x18;
    if (!x50) break;
    int32_t x52 = x20;
    for (;;) {
      int32_t x53 = x20;
      char x54 = x19[x53];
      bool x55 = x54 != ',';
      if (!x55) break;
      x20 += 1;
    }
    int32_t x60 = x20;
    x20 += 1;
    int32_t x64 = x20;
    int32_t x65 = 0;
    for (;;) {
      int32_t x66 = x20;
      char x67 = x19[x66];
      bool x68 = x67 != '\n';
      if (!x68) break;
      int32_t x70 = x65;
      int32_t x72 = x20;
      int32_t x71 = x70 * 10;
      char x73 = x19[x72];
      char x74 = x73 - '0';
      int32_t x75 = x71 + x74;
      x65 = x75;
      x20 += 1;
    }
    x20 += 1;
    int32_t x81 = x65;
    int32_t x82 = x14;
    char* x63 = x19+x52;
    x10[x82] = x63;
    int32_t x61 = x60 - x52;
    x11[x82] = x61;
    x12[x82] = x81;
    x14 += 1;
    //#hash_lookup
    // generated code for hash lookup
    int32_t x89 = x88;
    for (;;) {
      int32_t x90 = x89;
      int32_t x91 = x4[x90];
      bool x93 = x91 == -1;
      bool x95;
      if (x93) {
        x95 = false;
      } else {
        x95 = x94;
      }
      if (!x95) break;
      int32_t x97 = x89;
      int32_t x98 = x97 + 1;
      int32_t x99 = x98 & 255;
      x89 = x99;
    }
    int32_t x103 = x89;
    int32_t x104 = x4[x103];
    bool x105 = x104 == -1;
    int32_t x111;
    if (x105) {
      int32_t x106 = x3;
      x3 += 1;
      x4[x103] = x106;
      x16[x106] = 0;
      x111 = x106;
    } else {
      x111 = x104;
    }
    int32_t x113 = x111;
    //#hash_lookup
    int32_t x114 = x16[x113];
    int32_t x115 = x113 * 256;
    int32_t x116 = x115 + x114;
    x15[x116] = x82;
    int32_t x118 = x114 + 1;
    x16[x113] = x118;
  }
  close(x17);
  int32_t x126 = 0;
  int32_t x123 = open(x0,0);
  int32_t x124 = fsize(x123);
  char* x125 = mmap(0, x124, PROT_READ, MAP_FILE | MAP_SHARED, x123, 0);
  for (;;) {
    int32_t x127 = x126;
    bool x128 = x127 < x124;
    if (!x128) break;
    int32_t x130 = x126;
    for (;;) {
      int32_t x131 = x126;
      char x132 = x125[x131];
      bool x133 = x132 != '\t';
      if (!x133) break;
      x126 += 1;
    }
    int32_t x138 = x126;
    x126 += 1;
    int32_t x142 = x126;
    for (;;) {
      int32_t x143 = x126;
      char x144 = x125[x143];
      bool x145 = x144 != '\t';
      if (!x145) break;
      x126 += 1;
    }
    int32_t x150 = x126;
    x126 += 1;
    int32_t x154 = x126;
    for (;;) {
      int32_t x155 = x126;
      char x156 = x125[x155];
      bool x157 = x156 != '\t';
      if (!x157) break;
      x126 += 1;
    }
    int32_t x162 = x126;
    x126 += 1;
    int32_t x166 = x126;
    for (;;) {
      int32_t x167 = x126;
      char x168 = x125[x167];
      bool x169 = x168 != '\n';
      if (!x169) break;
      x126 += 1;
    }
    int32_t x174 = x126;
    x126 += 1;
    //#hash_lookup
    // generated code for hash lookup
    int32_t x178 = x88;
    for (;;) {
      int32_t x179 = x178;
      int32_t x180 = x4[x179];
      bool x182 = x180 == -1;
      bool x183;
      if (x182) {
        x183 = false;
      } else {
        x183 = x94;
      }
      if (!x183) break;
      int32_t x185 = x178;
      int32_t x186 = x185 + 1;
      int32_t x187 = x186 & 255;
      x178 = x187;
    }
    int32_t x191 = x178;
    int32_t x192 = x4[x191];
    int32_t x194 = x192;
    //#hash_lookup
    bool x196 = x194 == -1;
    if (x196) {
    } else {
      int32_t x197 = x16[x194];
      int32_t x198 = x194 * 256;
      int32_t x199 = x198 + x197;
      char* x141 = x125+x130;
      char* x153 = x125+x142;
      char* x165 = x125+x154;
      char* x177 = x125+x166;
      for(int x201=x198; x201 < x199; x201++) {
        int32_t x202 = x15[x201];
        char* x203 = x10[x202];
        int32_t x204 = x11[x202];
        int32_t x205 = x12[x202];
        int32_t x206 = printll(x203);
        printf(",");
        printf("%d",x205);
        printf(",");
        int32_t x210 = printll(x141);
        printf(",");
        int32_t x212 = printll(x153);
        printf(",");
        int32_t x214 = printll(x165);
        printf(",");
        int32_t x216 = printll(x177);
        printf("%s\n","");
      }
    }
  }
  close(x123);
}
/*****************************************
End of C Generated Code
*******************************************/
