void f() {
  target();
}

void g() {
  f();
}

int main () {
  g();
  f();
  return 0;
}
