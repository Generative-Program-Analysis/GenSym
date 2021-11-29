int f(int a, int b) {
  int res = 0;
  for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
      if (b == 9) res++;
      else return 0;
    }
  }
  if (a == 42 && b == res) target();
}

int main() {
  int b;
  make_symbolic(&b, 1);
  for (int a = 0; a < 1024; a++) f(a, b);
}