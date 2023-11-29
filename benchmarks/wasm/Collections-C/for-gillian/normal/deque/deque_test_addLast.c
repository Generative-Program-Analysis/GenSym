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

    deque_add_last(deque, &a);
    deque_add_last(deque, &b);
    deque_add_last(deque, &c);

    ASSERT(3 == deque_size(deque));

    const void *const *u = deque_get_buffer(deque);
    const void *e = u[0];

    ASSERT(e == &a);

    e = u[1];
    ASSERT(e == &b);

    e = u[2];
    ASSERT(e == &c);

    teardown_tests();
    return 0;
}
