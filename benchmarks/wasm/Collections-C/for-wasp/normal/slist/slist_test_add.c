#include "slist.h"
#include "mockups.h"

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

    int a = sym_int("a");

    char str_a[] = {a, '\0'};

    int b = sym_int("b");

    char str_b[] = {b, '\0'};

    int c = sym_int("c");

    char str_c[] = {c, '\0'};

    int d = sym_int("d");

    char str_d[] = {d, '\0'};

    assert(CC_OK == slist_add(list, str_a));
    assert(CC_OK == slist_add(list, str_b));
    assert(CC_OK == slist_add(list, str_c));
    assert(CC_OK == slist_add(list, str_d));

    void *e;
    slist_get_first(list, &e);
    assert(e != NULL);

    slist_get_last(list, &e);
    assert(e != NULL);

    teardown_test();
    return 0;
}
