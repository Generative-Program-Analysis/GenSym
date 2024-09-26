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

    int r = sym_int("r");
    array_add(v1, &a);
    array_add(v1, &b);
    array_add(v1, &c);

    array_replace_at(v1, &r, 2, NULL);

    int *repl;
    array_get_at(v1, 2, (void *)&repl);

    assert(*repl != c);
    assert(*repl == r);

    array_destroy(v1);

    return 0;
}
