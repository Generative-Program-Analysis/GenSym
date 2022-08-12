#include <assert.h>

int main() {
  int a, b;
  make_symbolic(&a, 4);
  make_symbolic(&b, 4);
  if (a > 1) {
    b = b + 1;
  } else {
    b = b - 1;
  }

  if (b < 2) {
    assert(1);
  } else {
    assert(1);
  }
}