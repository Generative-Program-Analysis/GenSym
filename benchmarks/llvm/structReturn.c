struct coordinate {
  int x;
  int y;
};

struct point3d {
  int x;
  int y;
  int z;
};

struct point3d make_3dcoor(int x) {
  struct point3d p;
  p.x = x;
  p.y = -x;
  p.z = 1024;
  return p;
}

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
  //struct point3d p1 = make_3dcoor(5);
  //return c1.x + a1 - p1.y;
}
