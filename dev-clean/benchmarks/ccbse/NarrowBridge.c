void f(int a, int b) {
  int sum = 0;
  for (int i = 0; i < 8; i++) if (b == 0xDEADBEEF) sum += 1;
  if (a == 100 && sum == 8) target();
}

int main () {
  int b; make_symbolic(&b, 1);
  for (int i = 0; i < 1000; i++) f(i, b);
}