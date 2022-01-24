int add(int x, int y) {
  return x + y;
}

int sub(int x, int y) {
  return x - y;
}

typedef int (*func_t)(int, int);

typedef struct data {
  int x;
  int y;
  func_t f;
} data_t;

data_t data[2] = {
  {1, 2, add},
  {3, 4, sub}
};

int apply(data_t d) {
  return d.f(d.x, d.y);
}

int main() {
  // (1 + 2) + (3 - 4) = 2
  int z = apply(data[0]) + apply(data[1]);
  // printf("%d\n", z);
  return z;
}
