#include "../../headers/gensym_client.h"
#include <assert.h>
int main() {
  int a;
  make_symbolic(&a, 4);
  gs_assume(a > 2);
  gs_assume(a < 8);
  switch (a) {
    case 1:
      assert(1);
      break;
    case 2:
      assert(1);
      break;
    case 3:
      assert(1);
      break;
    case 4:
      assert(1);
      break;
    case 5:
      assert(1);
      break;
    case 6:
      assert(1);
      break;
    case 7:
      assert(1);
      break;
    case 8:
      assert(1);
      break;
    default:
      assert(1);
      break;
  }
}