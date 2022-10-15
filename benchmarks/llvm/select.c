#include "../../headers/gensym_client.h"
int main() {
  int x;
  make_symbolic(&x, 4);
  int y = x ? 1 : 0;
  return y;
}
