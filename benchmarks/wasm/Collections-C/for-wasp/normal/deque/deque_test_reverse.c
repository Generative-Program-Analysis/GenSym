#include "deque.h"
#include "mockups.h"

static Deque *deque;
static DequeConf conf;
int stat;

void setup_tests() { stat = deque_new(&deque); }

void teardown_tests() { deque_destroy(deque); }

int main() {
    setup_tests();

    int a = sym_int("a");
    int b = sym_int("b");
    int c = sym_int("c");

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

    assert(c == *ra);
    assert(b == *rb);
    assert(a == *rc);

    teardown_tests();
    return 0;
}