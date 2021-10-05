int main() {
  int a = 42;
  if (&a) {
    int* b = malloc(4);
    if (b) return 1;
    return 2;
  }
  else return 3;
}
