#include "slist.h"
#include <gillian-c/gillian-c.h>

static SList *list;
static SList *list2;
static int stat;

void setup_test() {
    stat = slist_new(&list);
    slist_new(&list2);
};

void teardown_test() {
    slist_destroy(list);
    slist_destroy(list2);
};

int main() {
    setup_test();

    int a = __builtin_annot_intval("symb_int", a);
    int b = __builtin_annot_intval("symb_int", b);
    int c = __builtin_annot_intval("symb_int", c);
    int d = __builtin_annot_intval("symb_int", d);

    int p = __builtin_annot_intval("symb_int", p);

    slist_add(list, &a);
    slist_add(list, &b);
    slist_add(list, &c);
    slist_add(list, &d);

    ASSERT(4 == slist_size(list));

    int *last;
    slist_get_last(list, (void *)&last);
    ASSERT(d == *last);

    slist_add_last(list, &p);
    ASSERT(5 == slist_size(list));

    slist_get_last(list, (void *)&last);
    ASSERT(p == *last);

    teardown_test();
    return 0;
}
