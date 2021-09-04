void f1(int a, int b) {
  int x, sum = 0;
  for (int i = 0; i < 6; i++) {
    if (b==4234) sum += 1;
  }
  if (a == 5 && sum == 6) target();
}

void g1(int b) {
  for (int i = 0; i < 6; i++) f1(i, b);
}

void g2(int b) {
  for (int i = 0; i < 6; i++) g1(b);
}

void g3(int b) {
  for (int i = 0; i < 6; i++) g2(b);
}

void g4(int b) {
  for (int i = 0; i < 6; i++) g3(b);
}

void g5(int b) {
  for (int i = 0; i < 6; i++) g4(b);
}

// void g6(int b) {
//   for (int i = 0; i < 6; i++) g5(b);
// }

// void g7(int b) {
//   for (int i = 0; i < 6; i++) g6(b);
// }

// void g8(int b) {
//   for (int i = 0; i < 6; i++) g7(b);
// }

// void g9(int b) {
//   for (int i = 0; i < 6; i++) g8(b);
// }

// void g10(int b) {
//   for (int i = 0; i < 6; i++) g9(b);
// }


int main() {
  int a;
  make_symbolic(&a, 1);
  for (int i = 0; i < 6; i++) g5(a);
}