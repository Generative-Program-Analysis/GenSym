
#include <stdarg.h>
#include <stdio.h>
#include <assert.h>
#include "../../headers/gensym_client.h"

int __add_em_up (int count, va_list ap)
{
  int i, sum;

  sum = 0;
  for (i = 0; i < count; i++)
    sum += va_arg (ap, int);    /* Get the next argument value. */

  return sum;
}

int add_em_up (int count, ...)
{
  va_list ap, copy;
  int i, sum;

  va_start (ap, count);         /* Initialize the argument list. */
  va_copy (copy, ap);

  sum = 0;
  for (i = 0; i < count; i++)
    sum += va_arg (ap, int);    /* Get the next argument value. */

  va_end (ap);                  /* Clean up. */

  sum += __add_em_up(count, copy);
  va_end (copy);
  return sum;
}

int main (void) {
  /* This call prints 32. */
  // printf ("%d\n", add_em_up (3, 5, 5, 6));
  int sum = 0;
  sum = add_em_up (3, 5, 5, 6);
  sym_print(sum);
  gs_assert_eager(32 == sum);

  /* This call prints 110. */
  // printf ("%d\n", add_em_up (10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
  sum = add_em_up (10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
  sym_print(sum);
  gs_assert_eager(110 == sum);

  return 0;
}