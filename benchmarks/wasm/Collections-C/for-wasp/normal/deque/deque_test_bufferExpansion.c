#include "deque.h"
#include "mockups.h"

static Deque *deque;
static DequeConf conf;
int stat;

void setup_tests() {
    deque_conf_init(&conf);
    conf.capacity = 4;
    deque_new_conf(&conf, &deque);
}

void teardown_tests() { deque_destroy(deque); }

int main() {
    setup_tests();

    int a = sym_int("a");
    int b = sym_int("b");
    int c = sym_int("c");
    int d = sym_int("d");
    int e = sym_int("e");
    int f = sym_int("f");

    deque_add_first(deque, &a);
    deque_add_last(deque, &b);
    deque_add_first(deque, &c);
    deque_add_last(deque, &d);

    size_t capacity = deque_capacity(deque);

    assert(4 == capacity);

    /* Current layout:
       _________________
       | b | d | c | a |
       -----------------
             L   F
    */
    /* This line should trigger the buffer expansion */
    deque_add_first(deque, &e);

    capacity = deque_capacity(deque);
    assert(8 == capacity);

    /* The expansion should align the elements.*/
    const void *const *buff = deque_get_buffer(deque);
    const int elem = *((int *)buff[0]);

    assert(elem == c);

    const int elem1 = *((int *)buff[1]);
    assert(elem1 == a);

    const int elem2 = *((int *)buff[2]);
    assert(elem2 == b);

    const int elem3 = *((int *)buff[3]);
    assert(elem3 == d);

    const int elem4 = *((int *)buff[7]);
    assert(elem4 == e);

    deque_add_last(deque, &f);

    const int elem5 = *((int *)buff[4]);
    assert(elem5 == f);

    teardown_tests();
    return 0;
}
