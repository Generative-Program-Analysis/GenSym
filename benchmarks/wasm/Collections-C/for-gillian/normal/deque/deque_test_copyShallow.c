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

    Deque *copy;
    deque_copy_shallow(deque, &copy);

    int size = deque_size(copy);
    ASSERT(3 == size);

    int *ca;
    deque_get_at(copy, 0, (void *)&ca);

    int *cb;
    deque_get_at(copy, 1, (void *)&cb);

    int *cc;
    deque_get_at(copy, 2, (void *)&cc);

    ASSERT(a == *ca);
    ASSERT(b == *cb);
    ASSERT(c == *cc);
    deque_destroy(copy);

    teardown_tests();
    return 0;
}
