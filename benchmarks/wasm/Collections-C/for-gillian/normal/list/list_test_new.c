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

    ASSERT(list1 != NULL);
    ASSERT(list2 != NULL);

    void *e = NULL;
    list_get_first(list1, &e);
    ASSERT(e == NULL);

    list_get_last(list1, &e);
    ASSERT(e == NULL);

    ASSERT(0 == list_size(list1));
    ASSERT(list1 != list2);

    teardown_test();
}
