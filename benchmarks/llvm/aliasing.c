#include <stdbool.h>
void gs_assert(bool);
int main() {
  int data;
  int* x = &data;
  int* y = &data;
  *x = 3;
  *y = 4;
  // Should not mistakenly optimize it
  gs_assert(*x + *y == 8);
  sym_print(data);
  return 0;
}
