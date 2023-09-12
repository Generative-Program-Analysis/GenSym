#include "list.h"
#include "mockups.h"

static List *list1;
static List *list2;

static int cmp(void const *e1, void const *e2) {
    int i = *(*((int **)e1));
    int j = *(*((int **)e2));

    if (i < j)
        return -1;
    if (i == j)
        return 0;
    return 1;
}

void setup_tests() { list_new(&list1), list_new(&list2); }

void teardown_test() {
    list_destroy(list1);
    list_destroy(list2);
}

int main() {
    setup_tests();

    int a = sym_int("a");
    int b = sym_int("b");
    int c = sym_int("c");
    int d = sym_int("d");

    list_add(list1, &a);
    list_add(list1, &b);
    list_add(list1, &c);
    list_add(list1, &d);

    list_sort(list1, cmp);

    ListIter iter;
    list_iter_init(&iter, list1);

    void *prev;
    void *e;
    list_iter_next(&iter, &prev);
    while (list_iter_next(&iter, &e) != CC_ITER_END) {
        assert(*((int *)prev) <= *((int *)e));
        prev = e;
    }

    teardown_test();
}
