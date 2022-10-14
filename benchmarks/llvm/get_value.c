#include "../../headers/gensym_client.h"
#include <stdio.h>

int main() {
  long x;
  make_symbolic_whole(&x, sizeof(long));
  gs_assume(x > 10);
  gs_assume(x < 20);

  gs_assert_eager(!gs_is_symbolic(gs_get_valuel(x)));
  gs_assert_eager(gs_get_valuel(x) > 10);
  gs_assert_eager(gs_get_valuel(x) < 20);

  gs_assume(x > 18);
  long x1 = gs_get_valuel(x + 1);
  long x0 = gs_get_valuel(x);
  gs_assert_eager(!gs_is_symbolic(x1));
  gs_assert_eager(!gs_is_symbolic(x0));
  gs_assert_eager(20 == x1);
  gs_assert_eager(19 == x0);

  return 0;
}
