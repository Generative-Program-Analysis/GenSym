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
  int32_t x15 = 0;
  int32_t x16 = 0;
  int32_t* x17 = (int32_t*)malloc(65536 * sizeof(int32_t));
  int32_t* x18 = (int32_t*)malloc(256 * sizeof(int32_t));
  int32_t x22 = 0;
  int32_t x19 = open("src/data/words.csv",0);
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
    bool x37 = x36 != '\n';
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
  for (;;) {
    int32_t x51 = x22;
    bool x52 = x51 < x20;
    if (!x52) break;
    int32_t x54 = x22;
    for (;;) {
      int32_t x55 = x22;
      char x56 = x21[x55];
      bool x57 = x56 != ',';
      if (!x57) break;
      x22 += 1;
    }
    int32_t x62 = x22;
    x22 += 1;
    int32_t x66 = x22;
    int32_t x67 = 0;
    for (;;) {
      int32_t x68 = x22;
      char x69 = x21[x68];
      bool x70 = x69 != '\n';
      if (!x70) break;
      int32_t x72 = x67;
      int32_t x74 = x22;
      int32_t x73 = x72 * 10;
      char x75 = x21[x74];
      char x76 = x75 - '0';
      int32_t x77 = x73 + x76;
      x67 = x77;
      x22 += 1;
    }
    x22 += 1;
    int32_t x83 = x67;
    int32_t x84 = x16;
    char* x65 = x21+x54;
    x12[x84] = x65;
    int32_t x63 = x62 - x54;
    x13[x84] = x63;
    x14[x84] = x83;
    x16 += 1;
    int64_t x89 = hash(x65,x63);
    int32_t x90 = (int32_t)x89;
    int32_t x91 = x90 & 255;
    //#hash_lookup
    // generated code for hash lookup
    int32_t x92 = x91;
    for (;;) {
      int32_t x93 = x92;
      int32_t x94 = x6[x93];
      bool x96 = x94 == -1;
      bool x118;
      if (x96) {
        x118 = false;
      } else {
        char* x97 = x2[x94];
        int32_t x98 = x3[x94];
        bool x100 = x98 == x63;
        bool x115;
        if (x100) {
          int32_t x101 = 0;
          for (;;) {
            int32_t x102 = x101;
            bool x103 = x102 < x98;
            bool x107;
            if (x103) {
              char x104 = x97[x102];
              char x105 = x65[x102];
              bool x106 = x104 == x105;
              x107 = x106;
            } else {
              x107 = false;
            }
            if (!x107) break;
            x101 += 1;
          }
          int32_t x112 = x101;
          bool x113 = x112 == x98;
          x115 = x113;
        } else {
          x115 = false;
        }
        bool x116 = !x115;
        x118 = x116;
      }
      if (!x118) break;
      int32_t x120 = x92;
      int32_t x121 = x120 + 1;
      int32_t x122 = x121 & 255;
      x92 = x122;
    }
    int32_t x126 = x92;
    int32_t x127 = x6[x126];
    bool x128 = x127 == -1;
    int32_t x136;
    if (x128) {
      int32_t x129 = x5;
      x2[x129] = x65;
      x3[x129] = x63;
      x5 += 1;
      x6[x126] = x129;
      x18[x129] = 0;
      x136 = x129;
    } else {
      x136 = x127;
    }
    int32_t x138 = x136;
    //#hash_lookup
    int32_t x139 = x18[x138];
    int32_t x140 = x138 * 256;
    int32_t x141 = x140 + x139;
    x17[x141] = x84;
    int32_t x143 = x139 + 1;
    x18[x138] = x143;
  }
  close(x19);
  int32_t x151 = 0;
  int32_t x148 = open(x0,0);
  int32_t x149 = fsize(x148);
  char* x150 = mmap(0, x149, PROT_READ, MAP_FILE | MAP_SHARED, x148, 0);
  for (;;) {
    int32_t x152 = x151;
    bool x153 = x152 < x149;
    if (!x153) break;
    int32_t x155 = x151;
    for (;;) {
      int32_t x156 = x151;
      char x157 = x150[x156];
      bool x158 = x157 != '\t';
      if (!x158) break;
      x151 += 1;
    }
    int32_t x163 = x151;
    x151 += 1;
    int32_t x167 = x151;
    for (;;) {
      int32_t x168 = x151;
      char x169 = x150[x168];
      bool x170 = x169 != '\t';
      if (!x170) break;
      x151 += 1;
    }
    int32_t x175 = x151;
    x151 += 1;
    int32_t x179 = x151;
    for (;;) {
      int32_t x180 = x151;
      char x181 = x150[x180];
      bool x182 = x181 != '\t';
      if (!x182) break;
      x151 += 1;
    }
    int32_t x187 = x151;
    x151 += 1;
    int32_t x191 = x151;
    for (;;) {
      int32_t x192 = x151;
      char x193 = x150[x192];
      bool x194 = x193 != '\n';
      if (!x194) break;
      x151 += 1;
    }
    int32_t x199 = x151;
    x151 += 1;
    int32_t x164 = x163 - x155;
    char* x166 = x150+x155;
    int64_t x203 = hash(x166,x164);
    int32_t x204 = (int32_t)x203;
    int32_t x205 = x204 & 255;
    //#hash_lookup
    // generated code for hash lookup
    int32_t x206 = x205;
    for (;;) {
      int32_t x207 = x206;
      int32_t x208 = x6[x207];
      bool x210 = x208 == -1;
      bool x232;
      if (x210) {
        x232 = false;
      } else {
        char* x211 = x2[x208];
        int32_t x212 = x3[x208];
        bool x214 = x212 == x164;
        bool x229;
        if (x214) {
          int32_t x215 = 0;
          for (;;) {
            int32_t x216 = x215;
            bool x217 = x216 < x212;
            bool x221;
            if (x217) {
              char x218 = x211[x216];
              char x219 = x166[x216];
              bool x220 = x218 == x219;
              x221 = x220;
            } else {
              x221 = false;
            }
            if (!x221) break;
            x215 += 1;
          }
          int32_t x226 = x215;
          bool x227 = x226 == x212;
          x229 = x227;
        } else {
          x229 = false;
        }
        bool x230 = !x229;
        x232 = x230;
      }
      if (!x232) break;
      int32_t x234 = x206;
      int32_t x235 = x234 + 1;
      int32_t x236 = x235 & 255;
      x206 = x236;
    }
    int32_t x240 = x206;
    int32_t x241 = x6[x240];
    int32_t x243 = x241;
    //#hash_lookup
    bool x245 = x243 == -1;
    if (x245) {
    } else {
      int32_t x246 = x18[x243];
      int32_t x247 = x243 * 256;
      int32_t x248 = x247 + x246;
      char* x178 = x150+x167;
      char* x190 = x150+x179;
      char* x202 = x150+x191;
      for(int x250=x247; x250 < x248; x250++) {
        int32_t x251 = x17[x250];
        char* x252 = x12[x251];
        int32_t x253 = x13[x251];
        int32_t x254 = x14[x251];
        int32_t x255 = printll(x252);
        printf(",");
        printf("%d",x254);
        printf(",");
        int32_t x259 = printll(x166);
        printf(",");
        int32_t x261 = printll(x178);
        printf(",");
        int32_t x263 = printll(x190);
        printf(",");
        int32_t x265 = printll(x202);
        printf("%s\n","");
      }
    }
  }
  close(x148);
}
/*****************************************
End of C Generated Code
*******************************************/
