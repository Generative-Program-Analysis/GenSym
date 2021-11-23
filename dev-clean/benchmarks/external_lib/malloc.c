int main() {
  int *a = malloc(sizeof(int) * 8);
  for (int i = 0; i < 8; i++) {
    a[i] = i;
  }
  sym_print(a[3]);
  int *b = realloc(a, 12 * sizeof(int));
  b[10] = 12;
  sym_print(a[4]);
  sym_print(b[3]);
  sym_print(b[10]);
  return 0;
}