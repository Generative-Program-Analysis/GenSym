int f() {
int x0 = 8;
int x1 = 6;
int x2 = 5;
int x3 = 2;
int x4 = 3;
int x5 = 8;
int x6 = 1;
int x7 = 2;
int x8 = 0;
if (x0 > 0) {
  x0 = x4 + x4 + 28;
}
if (x1 > 1) {
  x1 = x0 + x8 + 82;
}
if (x2 > 2) {
  x2 = x6 + x2 + 49;
}
if (x3 > 3) {
  x3 = x6 + x7 + 52;
}
if (x4 > 4) {
  x4 = x7 + x8 + 48;
}
if (x5 > 5) {
  x5 = x5 + x8 + 82;
}
if (x6 > 6) {
  x6 = x6 + x5 + 89;
}
if (x7 > 7) {
  x7 = x2 + x2 + 15;
}
if (x8 > 8) {
  x8 = x8 + x8 + 20;
}
return x6 + x8;
}

int main() {
  f();
  return 0;
}
