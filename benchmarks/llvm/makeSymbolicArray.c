int main() {
  int a[2];
  make_symbolic(a, 2);
  if (a[0] > 2) {
    a[1] = a[1] + 3;
  } else {
    a[1] = a[1] + 3;
  }

  if (a[1] < 2) {
    assert();
  } else {
    assert();
  }
}