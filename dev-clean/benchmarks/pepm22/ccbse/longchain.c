void f1(int a, int b) {
  int x, sum = 0;
  for (int i = 0; i < 6; i++) {
    if (b==4234) sum += 1;
  }
  if (a == 5 && sum == 6) target();
}

void g1(int x, int b) {
  for (int i = 0; i < 6; i++) f1(i, b);
}

void g2(int x, int b) {
  for (int i = 0; i < 6; i++) g1(i, b);
}

void g3(int x, int b) {
  for (int i = 0; i < 6; i++) g2(i, b);
}

void g4(int x, int b) {
  for (int i = 0; i < 6; i++) g3(i, b);
}

void g5(int x, int b) {
  for (int i = 0; i < 6; i++) g4(i, b);
}

void g6(int x, int b) {
  for (int i = 0; i < 6; i++) g5(i, b);
}

void g7(int x, int b) {
  for (int i = 0; i < 6; i++) g6(i, b);
}

void g8(int x, int b) {
  for (int i = 0; i < 6; i++) g7(i, b);
}

void g9(int x, int b) {
  for (int i = 0; i < 6; i++) g8(i, b);
}

void g10(int x, int b) {
  for (int i = 0; i < 6; i++) g9(i, b);
}


int main() {
  int a;
  make_symbolic(&a, 1);
  g10(2, a);
}