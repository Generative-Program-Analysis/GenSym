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

    deque_reverse(deque);

    int *ra;
    deque_get_at(deque, 0, (void *)&ra);
    int *rb;
    deque_get_at(deque, 1, (void *)&rb);
    int *rc;
    deque_get_at(deque, 2, (void *)&rc);

    ASSERT(c == *ra);
    ASSERT(b == *rb);
    ASSERT(a == *rc);

    teardown_tests();
    return 0;
}