#include "treetable.h"
#include "utils.h"
#include <gillian-c/gillian-c.h>

static TreeTable *table;

int main() {
    treetable_new(cmp, &table);

    int x = __builtin_annot_intval("symb_int", x);
    int y = __builtin_annot_intval("symb_int", y);
    int z = __builtin_annot_intval("symb_int", z);
    int w = __builtin_annot_intval("symb_int", w);

    char a = (char)__builtin_annot_intval("symb_int", a);

    char str_a[] = {a, '\0'};

    char b = (char)__builtin_annot_intval("symb_int", b);

    char str_b[] = {b, '\0'};

    char c = (char)__builtin_annot_intval("symb_int", c);

    char str_c[] = {c, '\0'};

    char d = (char)__builtin_annot_intval("symb_int", d);

    char str_d[] = {d, '\0'};

    ASSUME(x < y && y < z && z < w);

    treetable_add(table, &z, str_a);
    treetable_add(table, &w, str_b);
    treetable_add(table, &y, str_c);
    treetable_add(table, &x, str_d);

    treetable_remove_first(table, NULL);

    ASSERT(0 == treetable_contains_key(table, &x));

    treetable_destroy(table);
}
