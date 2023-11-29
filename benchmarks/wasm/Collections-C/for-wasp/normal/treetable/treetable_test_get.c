#include "treetable.h"
#include "utils.h"
#include "mockups.h"

static TreeTable *table;

int main() {
    treetable_new(cmp, &table);

    int x = sym_int("x");
    int y = sym_int("y");
    int z = sym_int("z");

    int a = sym_int("a");

    char str_a[] = {a, '\0'};

    int b = sym_int("b");

    char str_b[] = {b, '\0'};

    assume(x != y);

    treetable_add(table, &x, str_a);
    treetable_add(table, &y, str_b);

    char *ra;
    char *rb;

    treetable_get(table, &x, (void *)&ra);
    treetable_get(table, &y, (void *)&rb);

    assert(strcmp(ra, str_a) == 0);
    assert(strcmp(rb, str_b) == 0);

    treetable_destroy(table);
}
