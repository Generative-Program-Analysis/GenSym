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
  printf("%s\n","Name,Value,Flag,Name1");
  int32_t x2 = 0;
  int32_t x3 = 0;
  int32_t* x4 = (int32_t*)malloc(256 * sizeof(int32_t));
  for(int x6=0; x6 < 256; x6++) {
    x4[x6] = -1;
  }
  char** x10 = (char**)malloc(65536 * sizeof(char*));
  int32_t* x11 = (int32_t*)malloc(65536 * sizeof(int32_t));
  int32_t* x12 = (int32_t*)malloc(65536 * sizeof(int32_t));
  char** x13 = (char**)malloc(65536 * sizeof(char*));
  int32_t* x14 = (int32_t*)malloc(65536 * sizeof(int32_t));
  int32_t x15 = 0;
  int32_t x16 = 0;
  int32_t* x17 = (int32_t*)malloc(65536 * sizeof(int32_t));
  int32_t* x18 = (int32_t*)malloc(256 * sizeof(int32_t));
  int32_t x22 = 0;
  int32_t x19 = open("src/data/t.csv",0);
  int32_t x20 = fsize(x19);
  char* x21 = mmap(0, x20, PROT_READ, MAP_FILE | MAP_SHARED, x19, 0);
  for (;;) {
    int32_t x23 = x22;
    char x24 = x21[x23];
    bool x25 = x24 != ',';
    if (!x25) break;
    x22 += 1;
  }
  int32_t x30 = x22;
  x22 += 1;
  int32_t x33 = x22;
  int32_t x34 = 0;
  for (;;) {
    int32_t x35 = x22;
    char x36 = x21[x35];
    bool x37 = x36 != ',';
    if (!x37) break;
    int32_t x39 = x34;
    int32_t x41 = x22;
    int32_t x40 = x39 * 10;
    char x42 = x21[x41];
    char x43 = x42 - '0';
    int32_t x44 = x40 + x43;
    x34 = x44;
    x22 += 1;
  }
  x22 += 1;
  int32_t x50 = x34;
  int32_t x51 = x22;
  for (;;) {
    int32_t x52 = x22;
    char x53 = x21[x52];
    bool x54 = x53 != '\n';
    if (!x54) break;
    x22 += 1;
  }
  int32_t x59 = x22;
  x22 += 1;
  int32_t x115 = (int32_t)0L;
  int32_t x116 = x115 & 255;
  bool x122 = !true;
  for (;;) {
    int32_t x63 = x22;
    bool x64 = x63 < x20;
    if (!x64) break;
    int32_t x66 = x22;
    for (;;) {
      int32_t x67 = x22;
      char x68 = x21[x67];
      bool x69 = x68 != ',';
      if (!x69) break;
      x22 += 1;
    }
    int32_t x74 = x22;
    x22 += 1;
    int32_t x78 = x22;
    int32_t x79 = 0;
    for (;;) {
      int32_t x80 = x22;
      char x81 = x21[x80];
      bool x82 = x81 != ',';
      if (!x82) break;
      int32_t x84 = x79;
      int32_t x86 = x22;
      int32_t x85 = x84 * 10;
      char x87 = x21[x86];
      char x88 = x87 - '0';
      int32_t x89 = x85 + x88;
      x79 = x89;
      x22 += 1;
    }
    x22 += 1;
    int32_t x95 = x79;
    int32_t x96 = x22;
    for (;;) {
      int32_t x97 = x22;
      char x98 = x21[x97];
      bool x99 = x98 != '\n';
      if (!x99) break;
      x22 += 1;
    }
    int32_t x104 = x22;
    x22 += 1;
    int32_t x108 = x16;
    char* x77 = x21+x66;
    x10[x108] = x77;
    int32_t x75 = x74 - x66;
    x11[x108] = x75;
    x12[x108] = x95;
    char* x107 = x21+x96;
    x13[x108] = x107;
    int32_t x105 = x104 - x96;
    x14[x108] = x105;
    x16 += 1;
    //#hash_lookup
    // generated code for hash lookup
    int32_t x117 = x116;
    for (;;) {
      int32_t x118 = x117;
      int32_t x119 = x4[x118];
      bool x121 = x119 == -1;
      bool x123;
      if (x121) {
        x123 = false;
      } else {
        x123 = x122;
      }
      if (!x123) break;
      int32_t x125 = x117;
      int32_t x126 = x125 + 1;
      int32_t x127 = x126 & 255;
      x117 = x127;
    }
    int32_t x131 = x117;
    int32_t x132 = x4[x131];
    bool x133 = x132 == -1;
    int32_t x139;
    if (x133) {
      int32_t x134 = x3;
      x3 += 1;
      x4[x131] = x134;
      x18[x134] = 0;
      x139 = x134;
    } else {
      x139 = x132;
    }
    int32_t x141 = x139;
    //#hash_lookup
    int32_t x142 = x18[x141];
    int32_t x143 = x141 * 256;
    int32_t x144 = x143 + x142;
    x17[x144] = x108;
    int32_t x146 = x142 + 1;
    x18[x141] = x146;
  }
  close(x19);
  int32_t x151 = 0;
  for (;;) {
    int32_t x152 = x151;
    char x153 = x21[x152];
    bool x154 = x153 != ',';
    if (!x154) break;
    x151 += 1;
  }
  int32_t x159 = x151;
  x151 += 1;
  int32_t x161 = x151;
  int32_t x162 = 0;
  for (;;) {
    int32_t x163 = x151;
    char x164 = x21[x163];
    bool x165 = x164 != ',';
    if (!x165) break;
    int32_t x167 = x162;
    int32_t x169 = x151;
    int32_t x168 = x167 * 10;
    char x170 = x21[x169];
    char x171 = x170 - '0';
    int32_t x172 = x168 + x171;
    x162 = x172;
    x151 += 1;
  }
  x151 += 1;
  int32_t x178 = x162;
  int32_t x179 = x151;
  for (;;) {
    int32_t x180 = x151;
    char x181 = x21[x180];
    bool x182 = x181 != '\n';
    if (!x182) break;
    x151 += 1;
  }
  int32_t x187 = x151;
  x151 += 1;
  for (;;) {
    int32_t x191 = x151;
    bool x192 = x191 < x20;
    if (!x192) break;
    int32_t x194 = x151;
    for (;;) {
      int32_t x195 = x151;
      char x196 = x21[x195];
      bool x197 = x196 != ',';
      if (!x197) break;
      x151 += 1;
    }
    int32_t x202 = x151;
    x151 += 1;
    int32_t x206 = x151;
    int32_t x207 = 0;
    for (;;) {
      int32_t x208 = x151;
      char x209 = x21[x208];
      bool x210 = x209 != ',';
      if (!x210) break;
      int32_t x212 = x207;
      int32_t x214 = x151;
      int32_t x213 = x212 * 10;
      char x215 = x21[x214];
      char x216 = x215 - '0';
      int32_t x217 = x213 + x216;
      x207 = x217;
      x151 += 1;
    }
    x151 += 1;
    int32_t x223 = x207;
    int32_t x224 = x151;
    for (;;) {
      int32_t x225 = x151;
      char x226 = x21[x225];
      bool x227 = x226 != '\n';
      if (!x227) break;
      x151 += 1;
    }
    int32_t x232 = x151;
    x151 += 1;
    //#hash_lookup
    // generated code for hash lookup
    int32_t x236 = x116;
    for (;;) {
      int32_t x237 = x236;
      int32_t x238 = x4[x237];
      bool x240 = x238 == -1;
      bool x241;
      if (x240) {
        x241 = false;
      } else {
        x241 = x122;
      }
      if (!x241) break;
      int32_t x243 = x236;
      int32_t x244 = x243 + 1;
      int32_t x245 = x244 & 255;
      x236 = x245;
    }
    int32_t x249 = x236;
    int32_t x250 = x4[x249];
    int32_t x252 = x250;
    //#hash_lookup
    bool x254 = x252 == -1;
    if (x254) {
    } else {
      int32_t x255 = x18[x252];
      int32_t x256 = x252 * 256;
      int32_t x257 = x256 + x255;
      char* x205 = x21+x194;
      for(int x259=x256; x259 < x257; x259++) {
        int32_t x260 = x17[x259];
        char* x261 = x10[x260];
        int32_t x262 = x11[x260];
        int32_t x263 = x12[x260];
        char* x264 = x13[x260];
        int32_t x265 = x14[x260];
        int32_t x266 = printll(x261);
        printf(",");
        printf("%d",x263);
        printf(",");
        int32_t x270 = printll(x264);
        printf(",");
        int32_t x272 = printll(x205);
        printf("%s\n","");
      }
    }
  }
  close(x19);
}
/*****************************************
End of C Generated Code
*******************************************/
