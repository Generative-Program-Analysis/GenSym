int main () {
  static int i1 = 10;
  int i2 = 4;
  int *p1 = &i1, *p2 = &i2;
  long long int a1 = p1, a2 = p2;
  int sub = a1 - a2;
  int diff = *((int*)a1) - *((int*)a2);
  return sub;
}