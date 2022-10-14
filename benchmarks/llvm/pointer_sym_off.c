#include "../../headers/gensym_client.h"
#include <assert.h>

int main() {
    int nums[] = {0,1,2,3,4};
    int * p_int = (int *)nums + 2;
    int idx;
    make_symbolic_whole(&idx, sizeof(int));
    gs_assume(idx != 1);

    int res = p_int[idx];
    sym_print(res);
    switch (res) {
      case 0: assert(1); break;
      case 1: assert(1); break;
      case 2: assert(1); break;
      case 4: assert(1); break;
      default : gs_assert_eager(0); break;
    }
    return 0;
}