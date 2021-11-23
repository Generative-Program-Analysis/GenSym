struct coordinate {
  int x;
  int y;
};

struct coordinate make_coor(int x, int y) {
  struct coordinate res;
  res.x = x;
  res.y = y;
  return res;
}

int main () {
  struct coordinate c1 = make_coor(1, 3);
  int a1 = make_coor(3, 5).x;
  return c1.x + a1;
}