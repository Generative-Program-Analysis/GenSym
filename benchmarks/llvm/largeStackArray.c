#include <stdio.h>

int f(int n) {
  int arr[100000];
  if (n == 0) return n;
  else {
    return n + f(n-1);
  }
}

int main() {
  printf("%d\n", f(10000));
}
