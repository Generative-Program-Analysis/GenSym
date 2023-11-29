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

    array_add(v1, &a);
    array_add(v1, &b);
    array_add(v1, &c);
    array_add(v1, &e);

    int *ar;
    int *cr;
    array_get_at(v1, 0, (void *)&ar);
    array_get_at(v1, 2, (void *)&cr);

    assert(a == *ar);
    assert(c == *cr);

    array_destroy(v1);

    return 0;
}
