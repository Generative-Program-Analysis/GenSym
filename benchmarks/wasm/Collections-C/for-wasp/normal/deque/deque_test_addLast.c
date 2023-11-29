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

    deque_add_last(deque, &a);
    deque_add_last(deque, &b);
    deque_add_last(deque, &c);

    assert(3 == deque_size(deque));

    const void *const *u = deque_get_buffer(deque);
    const void *e = u[0];

    assert(e == &a);

    e = u[1];
    assert(e == &b);

    e = u[2];
    assert(e == &c);

    teardown_tests();
    return 0;
}
