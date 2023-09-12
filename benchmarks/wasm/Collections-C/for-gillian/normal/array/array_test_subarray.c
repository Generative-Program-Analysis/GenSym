#include "array.h"
#include <gillian-c/gillian-c.h>

static Array *v1;
static Array *v2;
static ArrayConf vc;
static int stat;

int main() {
    stat = array_new(&v1);

    int a = __builtin_annot_intval("symb_int", a);
    int b = __builtin_annot_intval("symb_int", b);
    int c = __builtin_annot_intval("symb_int", c);
    int e = __builtin_annot_intval("symb_int", e);
    int f = __builtin_annot_intval("symb_int", f);

    array_add(v1, &a);
    array_add(v1, &b);
    array_add(v1, &c);
    array_add(v1, &e);
    array_add(v1, &f);

    Array *sub;
    array_subarray(v1, 1, 3, &sub);

    ASSERT(3 == array_size(sub));

    int *s0;
    int *s1;
    int *s2;
    array_get_at(sub, 0, (void *)&s0);
    array_get_at(sub, 1, (void *)&s1);
    array_get_at(sub, 2, (void *)&s2);

    ASSERT(&b == s0);
    ASSERT(&c == s1);
    ASSERT(&e == s2);

    array_destroy(sub);

    array_destroy(v1);

    return 0;
}
