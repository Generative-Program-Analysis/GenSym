#include "../../headers/gensym_client.h"
#include <stdio.h>

int main() {
  char x[10];
  make_symbolic(x, 10);

  gs_assume(0 == x[0]);
  if (x[0] == 2) {
    print_string("should be here!\n");
  } else {
    print_string("oops!\n");
  }

  return 0;
}