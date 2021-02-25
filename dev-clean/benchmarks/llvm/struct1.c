struct CoordIn {
  int x;
  int y;
};

struct Coord {
  int x;
  struct CoordIn c;
  int y;
};


int main() {
  struct Coord clocal = { 3, {1, 2}, 4 };
  int a = clocal.c.x;
  return 0;
}