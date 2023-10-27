int sym_int(char* name) { return (int) name; }
void assert(int expr) {}

int main() {
  int a = sym_int("a");
  int b = sym_int("b");

  if (a == b) {
    return 1;
  } else {
    return 0;
  }
}
