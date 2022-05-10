#include "../../headers/llsc_client.h"
int main() {
  int a;
  make_symbolic(&a, 4);
  llsc_assume(a > 2);
  llsc_assume(a < 8);
  switch (a) {
    case 1:
      assert();
      break;
    case 2:
      assert();
      break;
    case 3:
      assert();
      break;
    case 4:
      assert();
      break;
    case 5:
      assert();
      break;
    case 6:
      assert();
      break;
    case 7:
      assert();
      break;
    case 8:
      assert();
      break;
    default:
      assert();
      break;
  }
}