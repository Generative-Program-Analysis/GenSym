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

    SList *list2 = NULL;
    slist_new(&list2);
    ASSERT(list != NULL);
    ASSERT(list2 != NULL);

    void *e = NULL;
    slist_get_first(list, &e);
    ASSERT(NULL == e);

    slist_get_last(list, &e);
    ASSERT(NULL == e);
    ASSERT(0 == slist_size(list));
    ASSERT(list != list2);

    teardown_test();
    return 0;
}
