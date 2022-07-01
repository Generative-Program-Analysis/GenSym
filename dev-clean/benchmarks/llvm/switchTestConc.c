#include <assert.h>
int main() {
  int a = 3;
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
    default:
      assert(1);
      break;
  }
}