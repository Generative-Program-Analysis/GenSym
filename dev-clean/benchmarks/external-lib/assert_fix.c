#include <stdbool.h>

void llsc_assert(bool);

int main() {
  int x, y, z;
  make_symbolic(&x, sizeof(x));
  make_symbolic(&y, sizeof(y));
  make_symbolic(&z, sizeof(z));
  if (x == 2 && y == 3) {
    llsc_assert(x + y == 5);
    // the if (x + y == 5) branch should be satisfied, therefore the path number is 4
    if (x + y == 5) {
      if (z == 1) {
        sym_print(z);
      } else {
        sym_print(z);
      }
    }
  }
}
