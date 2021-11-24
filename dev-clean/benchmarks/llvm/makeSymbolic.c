int main() {
  int a, b;
  make_symbolic(&a, 1);
  make_symbolic(&b, 1);
  if (a > 1) {
    b = b + 1; 
  } else {
    b = b - 1;
  }

  if (b < 2) {
    assert();
  } else {
    assert();
  }
}