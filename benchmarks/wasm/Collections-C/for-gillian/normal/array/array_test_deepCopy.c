#include "array.h"
#include "utils.h"
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

    array_copy_deep(v1, copy, &v2);

    ASSERT(array_size(v2) == array_size(v1));

    int *ca;
    array_get_at(v2, 0, (void *)&ca);

    ASSERT(a == *ca);
    array_destroy_cb(v2, free);

    array_destroy(v1);

    return 0;
}
