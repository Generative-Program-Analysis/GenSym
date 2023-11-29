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

    deque_add_first(deque, &a);
    deque_add_first(deque, &b);
    deque_add_first(deque, &c);

    assert(3 == deque_size(deque));

    size_t m = deque_capacity(deque);
    const void *const *u = deque_get_buffer(deque);
    const void *e = u[m - 1];

    assert(e == &a);

    e = u[m - 2];
    assert(e == &b);

    e = u[m - 3];
    assert(e == &c);

    teardown_tests();
    return 0;
}
