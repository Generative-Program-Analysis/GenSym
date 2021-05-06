int f(int x, int y) {
  if (x <= 0 || y <= 0) return -1;
  if (x * x + y * y == 25) {
    return 1;
  } else {
    return 0;
  }
}
