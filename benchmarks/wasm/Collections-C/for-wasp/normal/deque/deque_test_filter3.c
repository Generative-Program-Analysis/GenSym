#include "deque.h"
#include "mockups.h"

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

    int a = sym_int("a");
    int b = sym_int("b");
    int c = sym_int("c");
    int d = sym_int("d");
    int e = sym_int("e");
    int f = sym_int("f");

    assume(!pred3(&d) && !pred3(&e) && pred3(&f) && !pred3(&a) && !pred3(&b) &&
           !pred3(&c));

    deque_add_last(deque, &a);
    deque_add_last(deque, &b);
    deque_add_last(deque, &c);
    deque_add_last(deque, &d);
    deque_add_last(deque, &e);
    deque_add_last(deque, &f);
    assert(6 == deque_size(deque));

    Deque *filter = NULL;
    deque_filter(deque, pred3, &filter);
    const void *const *buff = deque_get_buffer(filter);

    assert(1 == deque_size(filter));
    assert(buff[0] == &f);

    free(filter);

    teardown_tests();
    return 0;
}
