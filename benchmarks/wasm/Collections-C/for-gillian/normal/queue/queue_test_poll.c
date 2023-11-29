#include "queue.h"
#include <gillian-c/gillian-c.h>

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

    int a = __builtin_annot_intval("symb_int", a);
    int b = __builtin_annot_intval("symb_int", b);
    int c = __builtin_annot_intval("symb_int", c);

    queue_enqueue(q, &a);
    queue_enqueue(q, &b);
    queue_enqueue(q, &c);

    void *p;

    queue_poll(q, &p);
    ASSERT(&a == p);

    queue_peek(q, &p);
    ASSERT(&b == p);

    queue_poll(q, &p);
    ASSERT(&b == p);

    queue_peek(q, &p);
    ASSERT(&c == p);

    teardown_test();
    return 0;
}
