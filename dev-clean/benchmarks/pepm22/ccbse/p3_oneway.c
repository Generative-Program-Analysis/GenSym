void f(int x) {
  for (int i = 0; i < 10; i++) {
    if (x == 5212) {
      target();
      return;
    }
  }
}

void g(int x) {
  if (x == 5212) f(x);
  else return;
}

void main() {
  int a;
  make_symbolic(&a, 1);
  
  g(a);
}