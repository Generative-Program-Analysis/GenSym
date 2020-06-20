int f(int a, int b, int c) {
int x = a;
int y = b;
int z = c;
if (x > 1) {
  y = x + z;
  z = x + y;
}
if (x > 2) {
  y = x + z;
  z = x + y;
}
if (x > 3) {
  y = x + z;
  z = x + y;
}
if (x > 4) {
  y = x + z;
  z = x + y;
}
if (x > 5) {
  y = x + z;
  z = x + y;
}
if (x > 6) {
  y = x + z;
  z = x + y;
}
if (x > 7) {
  y = x + z;
  z = x + y;
}
if (x > 8) {
  y = x + z;
  z = x + y;
}
return x + y + c;
}
