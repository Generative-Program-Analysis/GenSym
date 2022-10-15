#include <stdbool.h>

void gs_assert(bool);
void f() {
  int i = 0;
  while (i < 5) { i++; }
}

int main() {
  int a;
  make_symbolic(&a, 4);
  int i = 0;
  while (i < a) {
    f();
    i++;
  }
  // the engine should be able to reach this
  gs_assert(false);
}
