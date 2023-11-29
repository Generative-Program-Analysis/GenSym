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

    char a = (char)__builtin_annot_intval("symb_int", a);

    char str_a[] = {a, '\0'};

    char b = (char)__builtin_annot_intval("symb_int", b);

    char str_b[] = {b, '\0'};

    char c = (char)__builtin_annot_intval("symb_int", c);

    char str_c[] = {c, '\0'};

    char d = (char)__builtin_annot_intval("symb_int", d);

    char str_d[] = {d, '\0'};

    ASSERT(CC_OK == slist_add(list, str_a));
    ASSERT(CC_OK == slist_add(list, str_b));
    ASSERT(CC_OK == slist_add(list, str_c));
    ASSERT(CC_OK == slist_add(list, str_d));

    void *e;
    slist_get_first(list, &e);
    ASSERT(e != NULL);

    slist_get_last(list, &e);
    ASSERT(e != NULL);

    teardown_test();
    return 0;
}
