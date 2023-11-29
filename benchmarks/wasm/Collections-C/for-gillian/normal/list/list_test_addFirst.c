#include "list.h"
#include <gillian-c/gillian-c.h>

static List *list1;
static List *list2;

void setup_tests() { list_new(&list1), list_new(&list2); }

void teardown_test() {
    list_destroy(list1);
    list_destroy(list2);
}

int main() {
    setup_tests();

    int a = __builtin_annot_intval("symb_int", a);
    int b = __builtin_annot_intval("symb_int", b);
    int c = __builtin_annot_intval("symb_int", c);
    int d = __builtin_annot_intval("symb_int", d);
    int p = __builtin_annot_intval("symb_int", p);

    list_add(list1, &a);
    list_add(list1, &b);
    list_add(list1, &c);
    list_add(list1, &d);

    ASSERT(4 == list_size(list1));

    int *first;
    list_get_first(list1, (void *)&first);
    ASSERT(a == *first);

    list_add_last(list1, &p);
    ASSERT(5 == list_size(list1));

    list_get_last(list1, (void *)&first);
    ASSERT(p == *first);

    teardown_test();
}
