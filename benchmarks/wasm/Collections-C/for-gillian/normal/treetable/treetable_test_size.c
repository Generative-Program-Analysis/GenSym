#include "treetable.h"
#include "utils.h"
#include <gillian-c/gillian-c.h>

static TreeTable *table;

int main() {
    treetable_new(cmp, &table);

    int x = __builtin_annot_intval("symb_int", x);
    int y = __builtin_annot_intval("symb_int", y);
    int z = __builtin_annot_intval("symb_int", z);

    char a = (char)__builtin_annot_intval("symb_int", a);

    char str_a[] = {a, '\0'};

    char b = (char)__builtin_annot_intval("symb_int", b);

    char str_b[] = {b, '\0'};

    char c = (char)__builtin_annot_intval("symb_int", c);

    char str_c[] = {c, '\0'};

    ASSUME(x != y && x != z && y != z);

    treetable_add(table, &x, str_a);
    treetable_add(table, &y, str_b);
    treetable_add(table, &z, str_c);

    ASSERT(treetable_size(table) == 3);

    treetable_destroy(table);
}
