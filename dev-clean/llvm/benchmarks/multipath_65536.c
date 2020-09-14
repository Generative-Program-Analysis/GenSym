int f(int a, int b, int c) {
int x = a;
int y = b;
int z = c;
if (x > 1) {
  y = x + z + 24;
  z = x + y + 2;
}
if (x > 2) {
  y = x + z + 48;
  z = x + y + 12;
}
if (x > 3) {
  y = x + z + 47;
  z = x + y + 16;
}
if (x > 4) {
  y = x + z + 95;
  z = x + y + 51;
}
if (x > 5) {
  y = x + z + 43;
  z = x + y + 30;
}
if (x > 6) {
  y = x + z + 69;
  z = x + y + 86;
}
if (x > 7) {
  y = x + z + 66;
  z = x + y + 72;
}
if (x > 8) {
  y = x + z + 14;
  z = x + y + 63;
}
if (x > 9) {
  y = x + z + 92;
  z = x + y + 33;
}
if (x > 10) {
  y = x + z + 3;
  z = x + y + 78;
}
if (x > 11) {
  y = x + z + 94;
  z = x + y + 56;
}
if (x > 12) {
  y = x + z + 73;
  z = x + y + 34;
}
if (x > 13) {
  y = x + z + 67;
  z = x + y + 64;
}
if (x > 14) {
  y = x + z + 74;
  z = x + y + 98;
}
if (x > 15) {
  y = x + z + 73;
  z = x + y + 68;
}
if (x > 16) {
  y = x + z + 90;
  z = x + y + 62;
}
return x + y + c;
}
