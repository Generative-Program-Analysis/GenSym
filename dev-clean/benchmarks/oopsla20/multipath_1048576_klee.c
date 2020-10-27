#include <klee/klee.h>
int f() {
int x0; klee_make_symbolic(&x0, sizeof(x0), "x0"); 
int x1; klee_make_symbolic(&x1, sizeof(x1), "x1"); 
int x2; klee_make_symbolic(&x2, sizeof(x2), "x2"); 
int x3; klee_make_symbolic(&x3, sizeof(x3), "x3"); 
int x4; klee_make_symbolic(&x4, sizeof(x4), "x4"); 
int x5; klee_make_symbolic(&x5, sizeof(x5), "x5"); 
int x6; klee_make_symbolic(&x6, sizeof(x6), "x6"); 
int x7; klee_make_symbolic(&x7, sizeof(x7), "x7"); 
int x8; klee_make_symbolic(&x8, sizeof(x8), "x8"); 
int x9; klee_make_symbolic(&x9, sizeof(x9), "x9"); 
int x10; klee_make_symbolic(&x10, sizeof(x10), "x10"); 
int x11; klee_make_symbolic(&x11, sizeof(x11), "x11"); 
int x12; klee_make_symbolic(&x12, sizeof(x12), "x12"); 
int x13; klee_make_symbolic(&x13, sizeof(x13), "x13"); 
int x14; klee_make_symbolic(&x14, sizeof(x14), "x14"); 
int x15; klee_make_symbolic(&x15, sizeof(x15), "x15"); 
int x16; klee_make_symbolic(&x16, sizeof(x16), "x16"); 
int x17; klee_make_symbolic(&x17, sizeof(x17), "x17"); 
int x18; klee_make_symbolic(&x18, sizeof(x18), "x18"); 
int x19; klee_make_symbolic(&x19, sizeof(x19), "x19"); 
if (x0 > 0) {
  x0 = x7 + x13 + 13;
}
if (x1 > 1) {
  x1 = x10 + x7 + 35;
}
if (x2 > 2) {
  x2 = x0 + x4 + 69;
}
if (x3 > 3) {
  x3 = x13 + x14 + 91;
}
if (x4 > 4) {
  x4 = x19 + x10 + 47;
}
if (x5 > 5) {
  x5 = x15 + x6 + 74;
}
if (x6 > 6) {
  x6 = x19 + x8 + 15;
}
if (x7 > 7) {
  x7 = x10 + x9 + 34;
}
if (x8 > 8) {
  x8 = x6 + x12 + 57;
}
if (x9 > 9) {
  x9 = x10 + x11 + 3;
}
if (x10 > 10) {
  x10 = x6 + x6 + 21;
}
if (x11 > 11) {
  x11 = x6 + x5 + 72;
}
if (x12 > 12) {
  x12 = x19 + x6 + 55;
}
if (x13 > 13) {
  x13 = x0 + x3 + 12;
}
if (x14 > 14) {
  x14 = x0 + x12 + 69;
}
if (x15 > 15) {
  x15 = x18 + x5 + 92;
}
if (x16 > 16) {
  x16 = x0 + x12 + 39;
}
if (x17 > 17) {
  x17 = x15 + x8 + 39;
}
if (x18 > 18) {
  x18 = x17 + x3 + 64;
}
if (x19 > 19) {
  x19 = x11 + x5 + 78;
}
int r = x1 + x2 + x3 + x4 + x5 + x6 + x7 + x8 + x9 + x10 + x11 + x12 + x13 + x14 + x15 + x16 + x17 + x18 + x19;
klee_assert(0);
return r;
}

int main() {
  f();
  return 0;
}
