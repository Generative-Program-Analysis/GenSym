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
  printf("%s\n","Name,Value,Flag,Name");
  char** x2 = (char**)malloc(256 * sizeof(char*));
  int32_t* x3 = (int32_t*)malloc(256 * sizeof(int32_t));
  int32_t x4 = 0;
  int32_t x5 = 0;
  int32_t* x6 = (int32_t*)malloc(256 * sizeof(int32_t));
  for(int x8=0; x8 < 256; x8++) {
    x6[x8] = -1;
  }
  char** x12 = (char**)malloc(65536 * sizeof(char*));
  int32_t* x13 = (int32_t*)malloc(65536 * sizeof(int32_t));
  int32_t* x14 = (int32_t*)malloc(65536 * sizeof(int32_t));
  char** x15 = (char**)malloc(65536 * sizeof(char*));
  int32_t* x16 = (int32_t*)malloc(65536 * sizeof(int32_t));
  int32_t x17 = 0;
  int32_t x18 = 0;
  int32_t* x19 = (int32_t*)malloc(65536 * sizeof(int32_t));
  int32_t* x20 = (int32_t*)malloc(256 * sizeof(int32_t));
  int32_t x24 = 0;
  int32_t x21 = open("src/data/t.csv",0);
  int32_t x22 = fsize(x21);
  char* x23 = mmap(0, x22, PROT_READ, MAP_FILE | MAP_SHARED, x21, 0);
  for (;;) {
    int32_t x25 = x24;
    char x26 = x23[x25];
    bool x27 = x26 != ',';
    if (!x27) break;
    x24 += 1;
  }
  int32_t x32 = x24;
  x24 += 1;
  int32_t x35 = x24;
  int32_t x36 = 0;
  for (;;) {
    int32_t x37 = x24;
    char x38 = x23[x37];
    bool x39 = x38 != ',';
    if (!x39) break;
    int32_t x41 = x36;
    int32_t x43 = x24;
    int32_t x42 = x41 * 10;
    char x44 = x23[x43];
    char x45 = x44 - '0';
    int32_t x46 = x42 + x45;
    x36 = x46;
    x24 += 1;
  }
  x24 += 1;
  int32_t x52 = x36;
  int32_t x53 = x24;
  for (;;) {
    int32_t x54 = x24;
    char x55 = x23[x54];
    bool x56 = x55 != '\n';
    if (!x56) break;
    x24 += 1;
  }
  int32_t x61 = x24;
  x24 += 1;
  for (;;) {
    int32_t x65 = x24;
    bool x66 = x65 < x22;
    if (!x66) break;
    int32_t x68 = x24;
    for (;;) {
      int32_t x69 = x24;
      char x70 = x23[x69];
      bool x71 = x70 != ',';
      if (!x71) break;
      x24 += 1;
    }
    int32_t x76 = x24;
    x24 += 1;
    int32_t x80 = x24;
    int32_t x81 = 0;
    for (;;) {
      int32_t x82 = x24;
      char x83 = x23[x82];
      bool x84 = x83 != ',';
      if (!x84) break;
      int32_t x86 = x81;
      int32_t x88 = x24;
      int32_t x87 = x86 * 10;
      char x89 = x23[x88];
      char x90 = x89 - '0';
      int32_t x91 = x87 + x90;
      x81 = x91;
      x24 += 1;
    }
    x24 += 1;
    int32_t x97 = x81;
    int32_t x98 = x24;
    for (;;) {
      int32_t x99 = x24;
      char x100 = x23[x99];
      bool x101 = x100 != '\n';
      if (!x101) break;
      x24 += 1;
    }
    int32_t x106 = x24;
    x24 += 1;
    int32_t x110 = x18;
    char* x79 = x23+x68;
    x12[x110] = x79;
    int32_t x77 = x76 - x68;
    x13[x110] = x77;
    x14[x110] = x97;
    char* x109 = x23+x98;
    x15[x110] = x109;
    int32_t x107 = x106 - x98;
    x16[x110] = x107;
    x18 += 1;
    int64_t x117 = hash(x79,x77);
    int32_t x118 = (int32_t)x117;
    int32_t x119 = x118 & 255;
    //#hash_lookup
    // generated code for hash lookup
    int32_t x120 = x119;
    for (;;) {
      int32_t x121 = x120;
      int32_t x122 = x6[x121];
      bool x124 = x122 == -1;
      bool x146;
      if (x124) {
        x146 = false;
      } else {
        char* x125 = x2[x122];
        int32_t x126 = x3[x122];
        bool x128 = x126 == x77;
        bool x143;
        if (x128) {
          int32_t x129 = 0;
          for (;;) {
            int32_t x130 = x129;
            bool x131 = x130 < x126;
            bool x135;
            if (x131) {
              char x132 = x125[x130];
              char x133 = x79[x130];
              bool x134 = x132 == x133;
              x135 = x134;
            } else {
              x135 = false;
            }
            if (!x135) break;
            x129 += 1;
          }
          int32_t x140 = x129;
          bool x141 = x140 == x126;
          x143 = x141;
        } else {
          x143 = false;
        }
        bool x144 = !x143;
        x146 = x144;
      }
      if (!x146) break;
      int32_t x148 = x120;
      int32_t x149 = x148 + 1;
      int32_t x150 = x149 & 255;
      x120 = x150;
    }
    int32_t x154 = x120;
    int32_t x155 = x6[x154];
    bool x156 = x155 == -1;
    int32_t x164;
    if (x156) {
      int32_t x157 = x5;
      x2[x157] = x79;
      x3[x157] = x77;
      x5 += 1;
      x6[x154] = x157;
      x20[x157] = 0;
      x164 = x157;
    } else {
      x164 = x155;
    }
    int32_t x166 = x164;
    //#hash_lookup
    int32_t x167 = x20[x166];
    int32_t x168 = x166 * 256;
    int32_t x169 = x168 + x167;
    x19[x169] = x110;
    int32_t x171 = x167 + 1;
    x20[x166] = x171;
  }
  close(x21);
  int32_t x176 = 0;
  for (;;) {
    int32_t x177 = x176;
    char x178 = x23[x177];
    bool x179 = x178 != ',';
    if (!x179) break;
    x176 += 1;
  }
  int32_t x184 = x176;
  x176 += 1;
  int32_t x186 = x176;
  int32_t x187 = 0;
  for (;;) {
    int32_t x188 = x176;
    char x189 = x23[x188];
    bool x190 = x189 != ',';
    if (!x190) break;
    int32_t x192 = x187;
    int32_t x194 = x176;
    int32_t x193 = x192 * 10;
    char x195 = x23[x194];
    char x196 = x195 - '0';
    int32_t x197 = x193 + x196;
    x187 = x197;
    x176 += 1;
  }
  x176 += 1;
  int32_t x203 = x187;
  int32_t x204 = x176;
  for (;;) {
    int32_t x205 = x176;
    char x206 = x23[x205];
    bool x207 = x206 != '\n';
    if (!x207) break;
    x176 += 1;
  }
  int32_t x212 = x176;
  x176 += 1;
  for (;;) {
    int32_t x216 = x176;
    bool x217 = x216 < x22;
    if (!x217) break;
    int32_t x219 = x176;
    for (;;) {
      int32_t x220 = x176;
      char x221 = x23[x220];
      bool x222 = x221 != ',';
      if (!x222) break;
      x176 += 1;
    }
    int32_t x227 = x176;
    x176 += 1;
    int32_t x231 = x176;
    int32_t x232 = 0;
    for (;;) {
      int32_t x233 = x176;
      char x234 = x23[x233];
      bool x235 = x234 != ',';
      if (!x235) break;
      int32_t x237 = x232;
      int32_t x239 = x176;
      int32_t x238 = x237 * 10;
      char x240 = x23[x239];
      char x241 = x240 - '0';
      int32_t x242 = x238 + x241;
      x232 = x242;
      x176 += 1;
    }
    x176 += 1;
    int32_t x248 = x232;
    int32_t x249 = x176;
    for (;;) {
      int32_t x250 = x176;
      char x251 = x23[x250];
      bool x252 = x251 != '\n';
      if (!x252) break;
      x176 += 1;
    }
    int32_t x257 = x176;
    x176 += 1;
    int32_t x228 = x227 - x219;
    char* x230 = x23+x219;
    int64_t x261 = hash(x230,x228);
    int32_t x262 = (int32_t)x261;
    int32_t x263 = x262 & 255;
    //#hash_lookup
    // generated code for hash lookup
    int32_t x264 = x263;
    for (;;) {
      int32_t x265 = x264;
      int32_t x266 = x6[x265];
      bool x268 = x266 == -1;
      bool x290;
      if (x268) {
        x290 = false;
      } else {
        char* x269 = x2[x266];
        int32_t x270 = x3[x266];
        bool x272 = x270 == x228;
        bool x287;
        if (x272) {
          int32_t x273 = 0;
          for (;;) {
            int32_t x274 = x273;
            bool x275 = x274 < x270;
            bool x279;
            if (x275) {
              char x276 = x269[x274];
              char x277 = x230[x274];
              bool x278 = x276 == x277;
              x279 = x278;
            } else {
              x279 = false;
            }
            if (!x279) break;
            x273 += 1;
          }
          int32_t x284 = x273;
          bool x285 = x284 == x270;
          x287 = x285;
        } else {
          x287 = false;
        }
        bool x288 = !x287;
        x290 = x288;
      }
      if (!x290) break;
      int32_t x292 = x264;
      int32_t x293 = x292 + 1;
      int32_t x294 = x293 & 255;
      x264 = x294;
    }
    int32_t x298 = x264;
    int32_t x299 = x6[x298];
    int32_t x301 = x299;
    //#hash_lookup
    bool x303 = x301 == -1;
    if (x303) {
    } else {
      int32_t x304 = x20[x301];
      int32_t x305 = x301 * 256;
      int32_t x306 = x305 + x304;
      for(int x308=x305; x308 < x306; x308++) {
        int32_t x309 = x19[x308];
        char* x310 = x12[x309];
        int32_t x311 = x13[x309];
        int32_t x312 = x14[x309];
        char* x313 = x15[x309];
        int32_t x314 = x16[x309];
        int32_t x315 = printll(x310);
        printf(",");
        printf("%d",x312);
        printf(",");
        int32_t x319 = printll(x313);
        printf(",");
        int32_t x321 = printll(x230);
        printf("%s\n","");
      }
    }
  }
  close(x21);
}
/*****************************************
End of C Generated Code
*******************************************/
