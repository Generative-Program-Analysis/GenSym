int f(int x0, int x1, int x2, int x3, int x4, int x5, int x6, int x7, int x8, int x9) {
if (x0 > 0) {
  x0 = x7 + x5 + 8;
}
if (x1 > 1) {
  x1 = x7 + x6 + 4;
}
if (x2 > 2) {
  x2 = x6 + x9 + 5;
}
if (x3 > 3) {
  x3 = x5 + x3 + 53;
}
if (x4 > 4) {
  x4 = x8 + x3 + 15;
}
if (x5 > 5) {
  x5 = x1 + x9 + 99;
}
if (x6 > 6) {
  x6 = x9 + x2 + 65;
}
if (x7 > 7) {
  x7 = x7 + x4 + 8;
}
if (x8 > 8) {
  x8 = x5 + x6 + 21;
}
if (x9 > 9) {
  x9 = x0 + x3 + 59;
}

int r = x1 + x2 + x3 + x4 + x5 + x6 + x7 + x8 + x9;
return r;
}

int main() {
  return 0;
}
