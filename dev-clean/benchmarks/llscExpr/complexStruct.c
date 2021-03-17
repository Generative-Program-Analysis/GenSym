#include <stdio.h>

struct RT {
  int A;
  int B[3];
  int C;
};
struct ST {
  int X;
  int Y;
  struct RT Z;
};

int *foo(struct ST *s) {
  return &s[0].Z.B[2];
}

int main() {
  struct RT rt = {3, {10, 11, 12}, 4};
  struct ST st = {1, 2, rt};
  int *x = foo(&st);
  //printf("%d\n", *x);
  return *x;
}
