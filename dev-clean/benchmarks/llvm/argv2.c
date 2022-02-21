int main(int argc, char *argv[])
{
  // --argv="abc#{3}def"
  if (argc != 1) {
    sym_exit(1);
  }
  /* TODO: Handle null ptr <2022-02-14, David Deng> */
  /* if (argv[1] != NULL) { */
  /*   sym_exit(2); */
  /* } */
  // argv[0] is a string with 9 bytes
  if (argv[0][9] != '\0') {
    sym_exit(3);
  }
  // argv[0][0:2] are concrete
  if (argv[0][0] != 'a') {
    sym_exit(4);
  }
  // argv[0][3:5] are symbolic
  if (argv[0][3] == 's') {
    if (argv[0][4] == 'y') {
      if (argv[0][5] == 'm') {
        print_string("path 1");
      } else /* argv[0][5] != 'm' */ {
        print_string("path 2");
      }
    } else /* argv[0][4] != 'y' */ {
      print_string("path 3");
    } 
  } else /* argv[0][3] != 'm' */ {
    print_string("path 4");
  }
  // argv[0][6:8] are concrete
  if (argv[0][6] == 'a') {
    sym_exit(5);
  }
  return 0;
}
