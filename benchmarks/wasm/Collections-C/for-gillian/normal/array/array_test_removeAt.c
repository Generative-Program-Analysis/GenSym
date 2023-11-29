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

    array_add(v1, &a);
    array_add(v1, &b);
    array_add(v1, &c);
    array_add(v1, &e);

    array_remove_at(v1, 2, NULL);

    ASSERT(3 == array_size(v1));

    int *r;
    array_get_at(v1, 2, (void *)&r);

    ASSERT(r == &e);

    array_destroy(v1);

    return 0;
}
