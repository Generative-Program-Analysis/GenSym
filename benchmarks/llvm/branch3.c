//#include <klee/klee.h>
int f(int x, int y) {
  if (x < y) {
    if (x < y - 5) return 0;
    else return 1;
  } else {
    if (x < 2 * y) return 2;
    else return 3;
  }
}

int main() {
  int a;
  //klee_make_symbolic(&a, sizeof(a), "a");
  int b;
  //klee_make_symbolic(&b, sizeof(b), "b");
  return f(a, b);
}
