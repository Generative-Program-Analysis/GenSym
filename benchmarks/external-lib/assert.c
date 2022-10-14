#include <stdbool.h>

void gs_assert(bool);

int flip(int x) {
  if (x > 0) {
    gs_assert(0);
    return -x;
  } else {
    return x;
  }
}

int main() {
  int x, y;
  make_symbolic(&x, sizeof(x));
  make_symbolic(&y, sizeof(y));
  if (x == 2 && y == 3) {
    gs_assert(x + y == 5); // symbolically valid, so will be ignored.
  }
  // the gs_assert in flip should break.
  // however with random exploration, we cannot guarantee only one test case will be generated.
  flip(x);
  if (x < 0) {
    flip(x);
  }
}
