int main()
{
  long double a = 0x1e1;
  long double b = 0x1e2;
  long double c = 0x1e3;
  sym_print(a);
  sym_print(b);
  sym_print(c);
  long double f1 = a * b + c;
  long double f2 = a / b - c;
  sym_print(f1);
  sym_print(f2);
  return 0;
}
