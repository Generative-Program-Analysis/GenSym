int add(int x, int y) {
  return x + y;
}

int sub(int x, int y) {
  return x - y;
}

typedef int (*func_t)(int, int);

int apply(func_t f, int x, int y) {
  return f(x, y);
}

int main() {
  // (1 + 2) + (3 - 4) = 2
  int z = apply(add, 1, 2) + apply(sub, 3, 4);
  // printf("%d\n", z);
  return z;
}
