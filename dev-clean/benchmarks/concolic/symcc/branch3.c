#include <stdint.h>
#include <stdio.h>
#include <unistd.h>

int f(int x, int y) {
  if (x < y) {
    if (x < y - 5) return 0;
    else return 1;
  } else {
    if (x < 2 * y) return 2;
    else return 3;
  }
}

int main() {
  int a;
  if (read(STDIN_FILENO, &a, sizeof(a)) != sizeof(a)) {
    fprintf(stderr, "Failed to read x\n");
    return -1;
  }
  //klee_make_symbolic(&a, sizeof(a), "a");
  int b;
  if (read(STDIN_FILENO, &b, sizeof(b)) != sizeof(b)) {
    fprintf(stderr, "Failed to read x\n");
    return -1;
  }
  //klee_make_symbolic(&b, sizeof(b), "b");
  return f(a, b);
}
