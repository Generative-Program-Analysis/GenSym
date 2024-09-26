#include "pqueue.h"
#include <gillian-c/gillian-c.h>

static struct Pair { int a, b; } A, B, C;

static int comp(const void *a, const void *b) {
    int alpha1 = ((struct Pair *)a)->a, beta1 = ((struct Pair *)a)->b;
    int alpha2 = ((struct Pair *)b)->a, beta2 = ((struct Pair *)b)->b;
    if (alpha1 != alpha2)
        return alpha1 - alpha2;
    else
        return beta1 - beta2;
}

static int comp2(const void *a, const void *b) {
    return *((int *)a) - *((int *)b);
}

static PQueue *p1, *p2;

void setup_tests() {
    pqueue_new(&p1, comp2);
    PQueueConf cfg;
    pqueue_conf_init(&cfg, comp);
    pqueue_new_conf(&cfg, &p2);
}

void teardown_tests() {
    pqueue_destroy(p1);
    pqueue_destroy(p2);
}

int main() {
    setup_tests();

    int a = __builtin_annot_intval("symb_int", a);
    int b = __builtin_annot_intval("symb_int", b);
    int c = __builtin_annot_intval("symb_int", c);
    int d = __builtin_annot_intval("symb_int", d);
    int e = __builtin_annot_intval("symb_int", e);
    int f = __builtin_annot_intval("symb_int", f);

    int x = __builtin_annot_intval("symb_int", x);
    int y = __builtin_annot_intval("symb_int", y);
    int z = __builtin_annot_intval("symb_int", z);
    int *ptr;

    ASSUME(x < 8388608 && x > -8388608);
    ASSUME(y < 8388608 && y > -8388608);
    ASSUME(z < 8388608 && z > -8388608);

    ASSUME(x > z && z > y);

    pqueue_push(p1, (void *)&y);
    pqueue_push(p1, (void *)&x);
    pqueue_push(p1, (void *)&z);

    pqueue_pop(p1, (void *)&ptr);
    ASSERT(&x == ptr);

    pqueue_pop(p1, (void *)&ptr);
    ASSERT(&z == ptr);

    pqueue_pop(p1, (void *)&ptr);
    ASSERT(&y == ptr);

    struct Pair *ptr2;
    A.a = a, A.b = b;
    B.a = c, B.b = d;
    C.a = e, C.b = f;

    ASSUME(comp(&C, &A) > 0 && comp(&A, &B) > 0);

    pqueue_push(p2, (void *)&A);
    pqueue_push(p2, (void *)&B);
    pqueue_push(p2, (void *)&C);

    pqueue_pop(p2, (void *)&ptr2);
    ASSERT(&C == ptr2);

    pqueue_pop(p2, (void *)&ptr2);
    ASSERT(&A == ptr2);

    pqueue_pop(p2, (void *)&ptr2);
    ASSERT(&B == ptr2);

    teardown_tests();
    return 0;
}
