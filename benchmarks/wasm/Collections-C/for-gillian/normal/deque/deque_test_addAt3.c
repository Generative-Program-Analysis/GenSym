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
    int d = __builtin_annot_intval("symb_int", d);
    int e = __builtin_annot_intval("symb_int", e);
    int f = __builtin_annot_intval("symb_int", f);
    int g = __builtin_annot_intval("symb_int", g);

    deque_add_last(deque, &a);
    deque_add_first(deque, &b);
    deque_add_first(deque, &c);
    deque_add_first(deque, &d);
    deque_add_first(deque, &e);
    deque_add_first(deque, &f);

    deque_add_at(deque, &g, 3);

    const void *const *buff = deque_get_buffer(deque);

    const void *elem = buff[6];
    ASSERT(elem == &g);

    const void *elem1 = buff[0];
    ASSERT(elem1 == &b);

    const void *elem2 = buff[7];
    ASSERT(elem2 == &c);

    const void *elem3 = buff[1];
    ASSERT(elem3 == &a);

    teardown_tests();
    return 0;
}
