#include "../../headers/llsc_client.h"
#include <assert.h>

int main() {
  int a;
  make_symbolic(&a, 4);
  llsc_assume(a >= 1);
  llsc_assume(a <= 5);
  switch (a) {
    case 1:
    case 2:
      printf("case 1 or 2\n");
      break;
    case 3:
    case 4:
      printf("case 3 or 4\n");
      break;
    case 5:
      printf("case 5\n");
      break;
    default:
      printf("case default, you should not see this\n");
      break;
  }
}
