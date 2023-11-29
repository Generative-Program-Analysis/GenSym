#include "deque.h"
#include <gillian-c/gillian-c.h>

static Deque *deque;
static DequeConf conf;
int stat;

void setup_tests() { stat = deque_new(&deque); }

void teardown_tests() { deque_destroy(deque); }

int main() {
    setup_tests();

    int a = __builtin_annot_intval("symb_int", a);
    int b = __builtin_annot_intval("symb_int", b);
    int c = __builtin_annot_intval("symb_int", c);

    deque_add(deque, &a);
    deque_add(deque, &b);
    deque_add(deque, &c);

    deque_remove_all(deque);

    void *first;
    int stat1 = deque_get_first(deque, &first);
    void *last;
    int stat2 = deque_get_last(deque, &last);

    ASSERT(CC_ERR_OUT_OF_RANGE == stat1);
    ASSERT(CC_ERR_OUT_OF_RANGE == stat2);

    ASSERT(0 == deque_size(deque));

    teardown_tests();
    return 0;
}
