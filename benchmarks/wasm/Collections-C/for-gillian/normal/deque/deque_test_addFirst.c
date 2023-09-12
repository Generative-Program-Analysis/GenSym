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

    deque_add_first(deque, &a);
    deque_add_first(deque, &b);
    deque_add_first(deque, &c);

    ASSERT(3 == deque_size(deque));

    size_t m = deque_capacity(deque);
    const void *const *u = deque_get_buffer(deque);
    const void *e = u[m - 1];

    ASSERT(e == &a);

    e = u[m - 2];
    ASSERT(e == &b);

    e = u[m - 3];
    ASSERT(e == &c);

    teardown_tests();
    return 0;
}
