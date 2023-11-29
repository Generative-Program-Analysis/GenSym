#include "array.h"
#include "mockups.h"

static Array *v1;
static Array *v2;
static ArrayConf vc;
static int stat;

int main() {
    stat = array_new(&v1);
    assert(stat == CC_OK);

    int a = sym_int("a");
    int b = sym_int("b");
    int c = sym_int("c");

    array_add(v1, &a);
    array_add(v1, &b);
    array_add(v1, &c);

    int *ar;
    int *br;
    int *cr;

    array_get_at(v1, 0, (void *)&ar);
    array_get_at(v1, 1, (void *)&br);
    array_get_at(v1, 2, (void *)&cr);

    assert(a == *ar);
    assert(b == *br);
    assert(c == *cr);

    array_destroy(v1);

    return 0;
}
