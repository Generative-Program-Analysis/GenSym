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
    int d = sym_int("d");

    deque_add_first(deque, &a);
    deque_add_last(deque, &b);
    deque_add_last(deque, &c);
    deque_add_last(deque, &d);

    int *first;
    deque_get_first(deque, (void *)&first);
    assert(a == *first);

    int *removed;
    deque_remove_first(deque, (void *)&removed);
    assert(a == *removed);

    deque_get_first(deque, (void *)&first);
    assert(b == *first);

    teardown_tests();
    return 0;
}
