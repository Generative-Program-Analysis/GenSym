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
  sym_print(st.Z.A);
  sym_print(st.Z.B[2]);
  sym_print(*(&st.Z.B[2]));
  int *x = foo(&st);
  sym_print(*x);
  return *x;
}
