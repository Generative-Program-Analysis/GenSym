int flip(int x) {
  if (x > 0) {
    llsc_assert(0);
    return -x;
  } else {
    return x;
  }
}

int main() {
  int x;
  make_symbolic(&x, sizeof(x));
  flip(x); // llsc_assert should break, below will be skipped; only one test case will be generated
  if (x < 0) {
    flip(x);
  }
}
