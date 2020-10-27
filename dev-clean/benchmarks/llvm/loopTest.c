int main() {
  int a;
  make_symbolic(&a, 1);
  while (a > 10) {
    a = a - 1;
  }
  if (a <= 10) {
    assert();
  } else {
    exit(0);
  }
}