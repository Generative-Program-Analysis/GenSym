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
if (x0 > 0) {
  x0 = x8 + x7 + 83;
}
if (x1 > 1) {
  x1 = x12 + x10 + 0;
}
if (x2 > 2) {
  x2 = x6 + x2 + 47;
}
if (x3 > 3) {
  x3 = x11 + x11 + 8;
}
if (x4 > 4) {
  x4 = x12 + x14 + 28;
}
if (x5 > 5) {
  x5 = x9 + x10 + 46;
}
if (x6 > 6) {
  x6 = x3 + x15 + 37;
}
if (x7 > 7) {
  x7 = x13 + x5 + 50;
}
if (x8 > 8) {
  x8 = x6 + x9 + 20;
}
if (x9 > 9) {
  x9 = x4 + x8 + 45;
}
if (x10 > 10) {
  x10 = x14 + x0 + 87;
}
if (x11 > 11) {
  x11 = x0 + x4 + 7;
}
if (x12 > 12) {
  x12 = x7 + x4 + 10;
}
if (x13 > 13) {
  x13 = x4 + x2 + 2;
}
if (x14 > 14) {
  x14 = x2 + x0 + 73;
}
if (x15 > 15) {
  x15 = x11 + x8 + 62;
}
klee_assert(0);
return x15 + x2;
}

int main() {
  f();
  return 0;
}
