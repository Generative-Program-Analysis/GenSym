#include <stdint.h>
#include <stdio.h>
#include <unistd.h>

void f(int a, int b) {
  int x, sum = 0;
  for (int i = 0; i < 6; i++) {
    if (b==4234) sum += 1;
  }
  if (a == 652 && sum == 6) {
    fprintf(stderr, "ERROR!!!.\n\n");
    exit(-1);
  }
}

void testf(int a) { 
  for (int i = 0; i < 1000; i++) {
    f(i, a);
  }
}

void main() {
  int a;
  if (read(STDIN_FILENO, &a, sizeof(a)) != sizeof(a)) {
    fprintf(stderr, "Failed to read the input\n");
  }

  testf(a);
}