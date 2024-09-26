#ifndef TEST_UTILS_H
#define TEST_UTILS_H

#include <gillian-c/gillian-c.h>

#define symb_str(X)                                                            \
    char X = (char)__builtin_annot_intval("symb_int", X);                      \
    char str_##X[] = {X, '\0'}

#define symb_uint(X)                                                           \
    uint64_t X = (uint64_t)__builtin_annot_intval("symb_int", X)

void CHECK_EQUAL_C_STRING(char *s1, char *s2);

void *copy(void *e1);

int cmp(void const *e1, void const *e2);

int zero_if_ptr_eq(void const *e1, void const *e2);

#endif /* TEST_UTILS_H */
