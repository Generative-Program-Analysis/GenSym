void f() {
  target();
}

void g() {
  f();
}

int main () {
  f();
  g();
  return 0;
}
