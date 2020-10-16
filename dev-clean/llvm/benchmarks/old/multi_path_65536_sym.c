int f(int x0, int x1, int x2, int x3, int x4, int x5, int x6, int x7, int x8, int x9, int x10, int x11, int x12, int x13, int x14, int x15) {
if (x0 > 0) {
  x0 = x8 + x7 + 83;
}
if (x1 > 1) {
  x1 = x12 + x10 + 0;
}
if (x2 > 2) {
  x2 = x6 + x2 + 47;
}
if (x3 > 3) {
  x3 = x11 + x11 + 8;
}
if (x4 > 4) {
  x4 = x12 + x14 + 28;
}
if (x5 > 5) {
  x5 = x9 + x10 + 46;
}
if (x6 > 6) {
  x6 = x3 + x15 + 37;
}
if (x7 > 7) {
  x7 = x13 + x5 + 50;
}
if (x8 > 8) {
  x8 = x6 + x9 + 20;
}
if (x9 > 9) {
  x9 = x4 + x8 + 45;
}
if (x10 > 10) {
  x10 = x14 + x0 + 87;
}
if (x11 > 11) {
  x11 = x0 + x4 + 7;
}
if (x12 > 12) {
  x12 = x7 + x4 + 10;
}
if (x13 > 13) {
  x13 = x4 + x2 + 2;
}
if (x14 > 14) {
  x14 = x2 + x0 + 73;
}
if (x15 > 15) {
  x15 = x11 + x8 + 62;
}
int r = x1 + x2 + x3 + x4 + x5 + x6 + x7 + x8 + x9 + x10 + x11 + x12 + x13 + x14 + x15;
klee_assert(0);
return r;
}

int main() {
  //f();
  return 0;
}
