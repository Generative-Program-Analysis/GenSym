#include "../../headers/llsc_client.h"
#include <errno.h>
int main(int argc, char *argv[])
{
  errno = EFAULT;
  // --argv="abc def"
  // print argc and argv, shouldn't get error for concrete arguments
  print_string("argc\n");
  sym_print(argc);
  print_string("argv\n");
  sym_print(argv);
  for (int i = 0; i < argc; ++i) {
    print_string(argv[i]);
    print_string("\n");
  }
  llsc_assert_eager(errno == EFAULT);
  return 0;
}
