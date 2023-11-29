#include "treeset.h"
#include "utils.h"
#include <gillian-c/gillian-c.h>

static TreeSet *set;

int main() {
    treeset_new(cmp, &set);

    int a = __builtin_annot_intval("symb_int", a);
    int b = __builtin_annot_intval("symb_int", b);
    int c = __builtin_annot_intval("symb_int", c);
    ASSUME(a != b && a != c && b != c);

    treeset_add(set, &a);
    treeset_add(set, &b);
    treeset_add(set, &c);

    treeset_remove(set, &a, NULL);

    ASSERT(2 == treeset_size(set));
    ASSERT(0 == treeset_contains(set, &a));
}
