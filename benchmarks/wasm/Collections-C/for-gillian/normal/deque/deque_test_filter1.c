#include "deque.h"
#include <gillian-c/gillian-c.h>

static Deque *deque;
static DequeConf conf;
int stat;

void setup_tests() { stat = deque_new(&deque); }

void teardown_tests() { deque_destroy(deque); }

bool pred1(const void *e) { return *(int *)e <= 3; }

bool pred2(const void *e) { return *(int *)e > 3; }

bool pred3(const void *e) { return *(int *)e > 5; }

int main() {
    setup_tests();

    int a = __builtin_annot_intval("symb_int", a);
    int b = __builtin_annot_intval("symb_int", b);
    int c = __builtin_annot_intval("symb_int", c);
    int d = __builtin_annot_intval("symb_int", d);
    int e = __builtin_annot_intval("symb_int", e);
    int f = __builtin_annot_intval("symb_int", f);

    ASSUME(!pred1(&d) && !pred1(&e) && !pred1(&f) && pred1(&a) && pred1(&b) &&
           pred1(&c));

    deque_add_last(deque, (void*)&a);
    deque_add_last(deque, (void*)&b);
    deque_add_last(deque, (void*)&c);
    deque_add_last(deque, (void*)&d);
    deque_add_last(deque, (void*)&e);
    deque_add_last(deque, (void*)&f);
    ASSERT(6 == deque_size(deque));

    Deque *filter = NULL;
    deque_filter(deque, pred1, &filter);
    ASSERT(3 == deque_size(filter));
    const void *const *buff = deque_get_buffer(filter);

    ASSERT(buff[0] == &a);
    ASSERT(buff[1] == &b);

    const void *elem = buff[2];
    ASSERT(elem == &c);
    free(filter);

    teardown_tests();
    return 0;
}
