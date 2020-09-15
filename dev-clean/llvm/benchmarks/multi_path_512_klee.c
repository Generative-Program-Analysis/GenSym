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
if (x0 > 0) {
  x0 = x5 + x1 + 36;
}
if (x1 > 1) {
  x1 = x0 + x1 + 33;
}
if (x2 > 2) {
  x2 = x0 + x4 + 61;
}
if (x3 > 3) {
  x3 = x0 + x2 + 39;
}
if (x4 > 4) {
  x4 = x2 + x4 + 39;
}
if (x5 > 5) {
  x5 = x0 + x3 + 36;
}
if (x6 > 6) {
  x6 = x6 + x5 + 47;
}
if (x7 > 7) {
  x7 = x7 + x3 + 84;
}
if (x8 > 8) {
  x8 = x8 + x8 + 44;
}
klee_assert(0);
return x5 + x7;
}

int main() {
  f();
  return 0;
}
