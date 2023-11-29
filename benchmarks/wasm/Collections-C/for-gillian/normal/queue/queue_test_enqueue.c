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

    ASSERT(2 == queue_size(q));

    void *p;
    queue_peek(q, &p);
    ASSERT(&a == p);

    queue_enqueue(q, &c);

    queue_peek(q, &p);
    ASSERT(&a == p);

    teardown_test();
    return 0;
}
