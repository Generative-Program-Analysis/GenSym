int f(int a, int b, int c) {
int x = a;
int y = b;
int z = c;
if (x > 1) {
  y = x + z + 73;
  z = x + y + 17;
}
if (x > 2) {
  y = x + z + 63;
  z = x + y + 96;
}
if (x > 3) {
  y = x + z + 79;
  z = x + y + 21;
}
if (x > 4) {
  y = x + z + 1;
  z = x + y + 51;
}
if (x > 5) {
  y = x + z + 18;
  z = x + y + 74;
}
if (x > 6) {
  y = x + z + 71;
  z = x + y + 95;
}
if (x > 7) {
  y = x + z + 88;
  z = x + y + 13;
}
if (x > 8) {
  y = x + z + 78;
  z = x + y + 40;
}
if (x > 9) {
  y = x + z + 23;
  z = x + y + 43;
}
if (x > 10) {
  y = x + z + 31;
  z = x + y + 27;
}
return x + y + c;
}
