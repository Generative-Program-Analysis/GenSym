#include "deque.h"
#include <gillian-c/gillian-c.h>

static Deque *deque;
static DequeConf conf;
int stat;

void setup_tests() { stat = deque_new(&deque); }

void teardown_tests() { deque_destroy(deque); }

int main() {
    setup_tests();

    DequeConf conf;
    deque_conf_init(&conf);

    conf.capacity = 2;

    Deque *deque;
    deque_new_conf(&conf, &deque);

    ASSERT(2 == deque_capacity(deque));

    int a = __builtin_annot_intval("symb_int", a);
    int b = __builtin_annot_intval("symb_int", b);
    int c = __builtin_annot_intval("symb_int", c);

    deque_add(deque, &a);
    deque_add(deque, &b);
    deque_add(deque, &c);

    ASSERT(4 == deque_capacity(deque));

    teardown_tests();
    return 0;
}
