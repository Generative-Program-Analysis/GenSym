int mult(int x, int y) {
  return x * y;
}

int power(int x, int n) {
  if (n == 0) return 1;
  else {
    int p = power(x, n - 1);
    return mult(x, p);
  }
}

int main() {
  return power(3, 3);
}
