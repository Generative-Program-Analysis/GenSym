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

    array_add(v1, &a);
    array_add(v1, &b);
    array_add(v1, &c);

    array_reverse(v1);

    int *i0;
    int *i1;
    int *i2;
    array_get_at(v1, 0, (void *)&i0);
    array_get_at(v1, 1, (void *)&i1);
    array_get_at(v1, 2, (void *)&i2);

    ASSERT(c == *i0);
    ASSERT(b == *i1);
    ASSERT(a == *i2);

    array_destroy(v1);

    return 0;
}
