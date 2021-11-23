int main(int argc, char* argv[]) {
  char * arg00 = argv[1];
  int res = 0;
  while (*arg00 != '\0') {
    arg00++; res++;
    sym_print(res);
  }
  int endsig = 999;
  sym_print(endsig);
  return 0;
}