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
    int e = sym_int("e");
    int f = sym_int("f");

    array_add(v1, &a);
    array_add(v1, &b);
    array_add(v1, &c);
    array_add(v1, &e);
    array_add(v1, &f);

    Array *sub;
    array_subarray(v1, 1, 3, &sub);

    assert(3 == array_size(sub));

    int *s0;
    int *s1;
    int *s2;
    array_get_at(sub, 0, (void *)&s0);
    array_get_at(sub, 1, (void *)&s1);
    array_get_at(sub, 2, (void *)&s2);

    assert(&b == s0);
    assert(&c == s1);
    assert(&e == s2);

    array_destroy(sub);

    array_destroy(v1);

    return 0;
}
