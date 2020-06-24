int f(int x) {
  int y = 0;
  if (x == 0) {
    y = x + 1;
    y = y + y;
  } else {
    y = x + 2;
    y = y - y;
  }
  return y;
}
