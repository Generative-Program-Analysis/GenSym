#include "list.h"
#include "mockups.h"

static List *list1;
static List *list2;

void setup_tests() { list_new(&list1), list_new(&list2); }

void teardown_test() {
    list_destroy(list1);
    list_destroy(list2);
}

int main() {
    setup_tests();

    assert(list1 != NULL);
    assert(list2 != NULL);

    void *e = NULL;
    list_get_first(list1, &e);
    assert(e == NULL);

    list_get_last(list1, &e);
    assert(e == NULL);

    assert(0 == list_size(list1));
    assert(list1 != list2);

    teardown_test();
}
