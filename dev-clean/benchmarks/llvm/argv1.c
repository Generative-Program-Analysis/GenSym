int main(int argc, char *argv[])
{
  // --argv="abc def"
  // print argc and argv, shouldn't get error for concrete arguments
  print_string("argc");
  sym_print(argc);
  print_string("argv");
  sym_print(argv);
  for (int i = 0; i < argc; ++i) {
    print_string(argv[i]);
  }
  return 0;
}
