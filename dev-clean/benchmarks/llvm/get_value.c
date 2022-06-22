#include "../../headers/llsc_client.h"
#include <stdio.h>

int main() {
  long x;
  make_symbolic_whole(&x, sizeof(long));
  llsc_assume(x > 10);
  llsc_assume(x < 20);

  llsc_assert_eager(!llsc_is_symbolic(llsc_get_valuel(x)));
  llsc_assert_eager(llsc_get_valuel(x) > 10);
  llsc_assert_eager(llsc_get_valuel(x) < 20);

  llsc_assume(x > 18);
  long x1 = llsc_get_valuel(x + 1);
  long x0 = llsc_get_valuel(x);
  llsc_assert_eager(!llsc_is_symbolic(x1));
  llsc_assert_eager(!llsc_is_symbolic(x0));
  llsc_assert_eager(20 == x1);
  llsc_assert_eager(19 == x0);

  return 0;
}
