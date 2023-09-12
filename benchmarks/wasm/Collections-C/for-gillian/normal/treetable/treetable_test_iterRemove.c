#include "treetable.h"
#include "utils.h"
#include <gillian-c/gillian-c.h>

static TreeTable *table;

int main() {
    treetable_new(cmp, &table);

    int x = __builtin_annot_intval("symb_int", x);
    int y = __builtin_annot_intval("symb_int", y);
    int z = __builtin_annot_intval("symb_int", z);

    ASSUME(x < y && y < z);

    char a = (char)__builtin_annot_intval("symb_int", a);

    char str_a[] = {a, '\0'};

    char b = (char)__builtin_annot_intval("symb_int", b);

    char str_b[] = {b, '\0'};

    char c = (char)__builtin_annot_intval("symb_int", c);

    char str_c[] = {c, '\0'};

    treetable_add(table, &x, str_a);
    treetable_add(table, &y, str_b);
    treetable_add(table, &z, str_c);

    TreeTableIter iter;
    treetable_iter_init(&iter, table);

    TreeTableEntry entry;
    while (treetable_iter_next(&iter, &entry) != CC_ITER_END) {
        int const *key = entry.key;

        if (*key == y) {
            ASSERT(CC_OK == treetable_iter_remove(&iter, NULL));

            ASSERT(CC_ERR_KEY_NOT_FOUND == treetable_iter_remove(&iter, NULL));
        }
    }

    ASSERT(2 == treetable_size(table));
    ASSERT(0 == treetable_contains_key(table, &y));

    treetable_destroy(table);
}
