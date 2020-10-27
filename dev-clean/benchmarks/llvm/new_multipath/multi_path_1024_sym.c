int f(int x0, int x1, int x2, int x3, int x4, int x5, int x6, int x7, int x8,
    int x9) {
if (x0 > 0) {
  x0 = x9 + x1 + 60;
  x1 = x8 + x9 + 60;
}
if (x1 > 1) {
  x1 = x9 + x9 + 65;
  x2 = x7 + x8 + 65;
}
if (x2 > 2) {
  x2 = x7 + x5 + 32;
  x3 = x2 + x3 + 32;
}
if (x3 > 3) {
  x3 = x1 + x9 + 15;
  x4 = x5 + x1 + 15;
}
if (x4 > 4) {
  x4 = x6 + x7 + 16;
  x5 = x2 + x3 + 16;
}
if (x5 > 5) {
  x5 = x8 + x4 + 17;
  x6 = x3 + x4 + 17;
}
if (x6 > 6) {
  x6 = x1 + x8 + 17;
  x7 = x6 + x3 + 17;
}
if (x7 > 7) {
  x7 = x3 + x4 + 73;
  x8 = x4 + x1 + 73;
}
if (x8 > 8) {
  x8 = x7 + x9 + 36;
  x9 = x5 + x3 + 36;
}
if (x9 > 9) {
  x9 = x1 + x9 + 86;
  x1 = x3 + x7 + 86;
}
return x5 + x2;
}

int main() {
  return 0;
}
