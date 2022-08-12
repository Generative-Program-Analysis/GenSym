#include "../../headers/llsc_client.h"
#include <stdio.h>

int	strcmp(char *str1,  char *str2) {
  while (*str1 == *str2) {
    if (*str1 == '\0') {
      return 0;
    }
    str1++;
    str2++;
  }
  if (*str1 < *str2) {
    return -1;
  } else {
    return  1;
  }
}
int	strlen(char *str) {
  int len = 0;
  while(*str++ != '\0') {
    len++;
  }
  return  len;
}

int main() {
  char c = 1;
  int i = -1;
  int s;
  float f = 1.1;
  double d = -99.99;
  make_symbolic_whole(&s, sizeof(int));
  llsc_assume(s > 19);
  llsc_assume(s < 21);
  char * message = "string message";
  char res[200];
  char* output = "execute native sprintf! : c = 1, i = -1, s = 20, f = 1.1, d = -99.99\nstring message = string message\n";
  int ret = -1;
  ret = sprintf(res, "execute native sprintf! : c = %d, i = %d, s = %d, f = %.1f, d = %.2lf\nstring message = %s\n", c, i, s, f, d, message);

  printf(res);

  printf("generating template %d\n", 1);
  printf("generating template %d\n", 2);

  int cmp = -1;
  cmp = strcmp(res, output);
  llsc_assert_eager(0 == cmp);
  llsc_assert_eager(ret > 0);
  llsc_assert_eager(strlen(output) == ret);
}