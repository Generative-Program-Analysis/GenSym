int f(int x, int y) {
  if (x <= 0 || y <= 0) return -1;
  if (x * x + y * y == 25) {
    // x = 3, y = 4
    return 1;
  }
  return 0;
}
