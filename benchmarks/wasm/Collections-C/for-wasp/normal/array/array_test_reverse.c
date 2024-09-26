#include "array.h"
#include "mockups.h"

static Array *v1;
static Array *v2;
static ArrayConf vc;
static int stat;

int main() {
    stat = array_new(&v1);

    int a = sym_int("a");
    int b = sym_int("b");
    int c = sym_int("c");

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

    assert(c == *i0);
    assert(b == *i1);
    assert(a == *i2);

    array_destroy(v1);

    return 0;
}
