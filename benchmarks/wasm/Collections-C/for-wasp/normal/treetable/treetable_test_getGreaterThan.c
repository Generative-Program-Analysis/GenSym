#include "treetable.h"
#include "utils.h"
#include "mockups.h"

static TreeTable *table;

int main() {
    treetable_new(cmp, &table);

    int x = sym_int("x");
    int y = sym_int("y");
    int z = sym_int("z");
    int w = sym_int("w");

    int a = sym_int("a");

    char str_a[] = {a, '\0'};

    int b = sym_int("b");

    char str_b[] = {b, '\0'};

    int c = sym_int("c");

    char str_c[] = {c, '\0'};

    int d = sym_int("d");

    char str_d[] = {d, '\0'};

    assume(x < y && y < z && z < w);

    treetable_add(table, &z, str_a);
    treetable_add(table, &w, str_b);
    treetable_add(table, &y, str_c);
    treetable_add(table, &x, str_d);

    int *g;
    treetable_get_greater_than(table, &y, (void *)&g);

    assert(z == *g);

    treetable_destroy(table);
}
