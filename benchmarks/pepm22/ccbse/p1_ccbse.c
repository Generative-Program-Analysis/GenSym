void f(int a, int b) {
  int x, sum = 0;
  for (int i = 0; i < 6; i++) {
    if (b==4234) sum += 1;
  }
  if (a == 652 && sum == 6) target();
}

void testf(int a) {
  for (int i = 0; i < 1000; i++) {
    f(i, a);
  }
}

void main() {
  int a;
  make_symbolic(&a, 1);
  testf(a);
}