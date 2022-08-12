void f(int a, int b) {
  int sum = 0;
  for (int i = 0; i < 4; i++) {
    int n = b % 2; 
    if (n) sum += 1;
    b /= 2;
  }
  if (a == 500 && sum == 4) target();
}

int main () {
  int b; make_symbolic(&b, 1);
  for (int i = 0; i < 1000; i++) f(i, b);
}