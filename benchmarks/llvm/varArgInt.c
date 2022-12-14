
#include <stdarg.h>
#include <stdio.h>
#include <assert.h>
#include <errno.h>
#include "../../headers/gensym_client.h"

int add_em_up (int count, ...)
{
  va_list ap;
  int i, sum;

  va_start (ap, count);         /* Initialize the argument list. */

  sum = 0;
  for (i = 0; i < count; i++)
    sum += va_arg (ap, int);    /* Get the next argument value. */

  va_end (ap);                  /* Clean up. */
  return sum;
}

int main (void) {
  /* This call prints 16. */
  // printf ("%d\n", add_em_up (3, 5, 5, 6));
  errno = EFAULT;
  int sum = 0;
  sum = add_em_up (3, 5, 5, 6);
  sym_print(sum);
  gs_assert_eager(16 == sum);

  /* This call prints 55. */
  // printf ("%d\n", add_em_up (10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
  sum = add_em_up (10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
  sym_print(sum);
  gs_assert_eager(55 == sum);
  gs_assert_eager(EFAULT == errno);

  return 0;
}