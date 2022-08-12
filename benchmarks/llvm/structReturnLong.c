struct coordinate {
  long x;
  long y;
};

struct coordinate make_coor(long x, long y) {
  struct coordinate res;
  res.x = x;
  res.y = y;
  return res;
}

long main () {
  struct coordinate c1 = make_coor(1, 3);
  long a1 = make_coor(3, 5).x;
  return c1.x + a1;
}
