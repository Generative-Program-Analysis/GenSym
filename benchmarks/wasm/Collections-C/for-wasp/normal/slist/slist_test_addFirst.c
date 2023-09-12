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
    int b = sym_int("b");
    int c = sym_int("c");
    int d = sym_int("d");

    int p = sym_int("p");

    slist_add(list, &a);
    slist_add(list, &b);
    slist_add(list, &c);
    slist_add(list, &d);

    assert(4 == slist_size(list));

    int *first;
    slist_get_first(list, (void *)&first);
    assert(a == *first);

    slist_add_first(list, &p);

    assert(5 == slist_size(list));
    slist_get_first(list, (void *)&first);
    assert(p == *first);

    teardown_test();
    return 0;
}
