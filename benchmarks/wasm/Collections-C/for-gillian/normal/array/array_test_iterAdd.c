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
    int d = __builtin_annot_intval("symb_int", d);

    int N = __builtin_annot_intval("symb_int", N);

    ASSUME(c != a && c != b && c != d && c != N);

    array_add(v1, &a);
    array_add(v1, &b);
    array_add(v1, &c);
    array_add(v1, &d);

    ArrayIter iter;
    array_iter_init(&iter, v1);

    int *e;
    while (array_iter_next(&iter, (void *)&e) != CC_ITER_END) {
        if (*e == c)
            array_iter_add(&iter, &N);
    }

    ASSERT(5 == array_size(v1));

    void *n;
    array_get_at(v1, 3, &n);
    ASSERT(N == *((int *)n));
    ASSERT(1 == array_contains(v1, &N));

    array_get_at(v1, 4, (void *)&n);
    ASSERT(d == *((int *)n));

    array_destroy(v1);

    return 0;
}
