struct Coord {
  int x;
  char b;
  int y;
};

struct Coord cglobal = { 1, 'c', 2 };

int main() {
  struct Coord clocal = { 3, 'd', 4 };
  return 0;
}