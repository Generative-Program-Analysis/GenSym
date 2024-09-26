#include "queue.h"
#include "mockups.h"

static Queue *q;
static Queue *q2;
static int stat;

void setup_test() {
    stat = queue_new(&q);
    queue_new(&q2);
}

void teardown_test() {
    queue_destroy(q);
    queue_destroy(q2);
}

int main() {
    setup_test();

    int a = sym_int("a");
    int b = sym_int("b");
    int c = sym_int("c");

    queue_enqueue(q, &a);
    queue_enqueue(q, &b);
    queue_enqueue(q, &c);

    void *p;

    queue_poll(q, &p);
    assert(&a == p);

    queue_peek(q, &p);
    assert(&b == p);

    queue_poll(q, &p);
    assert(&b == p);

    queue_peek(q, &p);
    assert(&c == p);

    teardown_test();
    return 0;
}
