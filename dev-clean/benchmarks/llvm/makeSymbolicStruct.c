int main() {
  struct nested {
    char n1;
    int n2;
  };
  struct st {
    int f1;
    char f2;
    struct nested f3;
    char f4;
  };
  struct st a;
  make_symbolic(&a, 11); // make the struct symbolic
  a.f2 = 'a'; // assign concrete value to some field
  sym_print(a.f1); // SymV(concat, { SymV(x0, 8), SymV(x1, 8), SymV(x2, 8), SymV(x3, 8) })
  sym_print(a.f2); // IntV(97, 32);
  sym_print(a.f3.n1); // SymV(sext, { SymV(x5, 8), }, 32);
  sym_print(a.f3.n2); // SymV(concat, { SymV(x6, 8), SymV(x7, 8), SymV(x8, 8), SymV(x9, 8) })
  sym_print(a.f4); // SymV(sext, { SymV(x10, 8), }, 32);
}
