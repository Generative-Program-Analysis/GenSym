struct CoordIn {
  int x;
  int y;
};

struct Coord {
  int x;
  struct CoordIn c;
  int y;
};

struct Coord cglobal = { 3, {1, 2}, 4 };

int main() {
  int a = cglobal.c.x;
  sym_print(a);
  return 0;
}