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
if (x0 > 0) {
  x0 = x7 + x5 + 8;
}
if (x1 > 1) {
  x1 = x7 + x6 + 4;
}
if (x2 > 2) {
  x2 = x6 + x9 + 5;
}
if (x3 > 3) {
  x3 = x5 + x3 + 53;
}
if (x4 > 4) {
  x4 = x8 + x3 + 15;
}
if (x5 > 5) {
  x5 = x1 + x9 + 99;
}
if (x6 > 6) {
  x6 = x9 + x2 + 65;
}
if (x7 > 7) {
  x7 = x7 + x4 + 8;
}
if (x8 > 8) {
  x8 = x5 + x6 + 21;
}
if (x9 > 9) {
  x9 = x0 + x3 + 59;
}
klee_assert(0);
return x2 + x9;
}

int main() {
  f();
  return 0;
}
