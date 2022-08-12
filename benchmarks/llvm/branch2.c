//#include <klee/klee.h>
int f(int x, int z) {
  int y = 0;
  if (x > 0) {
    y = x + 1;
    y = y + y;
  } else {
    y = x + 2;
    y = y - y;
  }
  y = y - 1;
  if (z == 12) {
    y = y + 1;
  } else {
    y = y - 1;
  }
  return y;
}

int main() {
  int a;
  //klee_make_symbolic(&a, sizeof(a), "a");
  int b;
  //klee_make_symbolic(&b, sizeof(b), "b");
  return f(a, b);
}
