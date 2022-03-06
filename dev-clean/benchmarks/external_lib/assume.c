#include "../../headers/llsc_client.h"

int main() {
  int x, y, z;
  make_symbolic(&x, sizeof(x));
  make_symbolic(&y, sizeof(y));
  z = llsc_range(0, 10, "z");
  llsc_assert_eager((z >= 0) && (z < 10));
  llsc_assume(x < y);
  if (x >= y) {
    print_string("Unreachable\n");
  }
  sym_print(x, y, z);
}
