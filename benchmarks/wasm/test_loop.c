int sym_int(char* name) { return (int) name; }
void assert(int expr) {}

int main() {
  int a = sym_int("a");
  int b = sym_int("b");
  int n = 0;

  while (a < b) {
    a++;
    n++;
  }

  return n;
}
