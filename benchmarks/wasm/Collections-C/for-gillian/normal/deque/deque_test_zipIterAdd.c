#include "deque.h"
#include "utils.h"
#include <gillian-c/gillian-c.h>

static Deque *deque;
static DequeConf conf;
int stat;

void setup_tests() { stat = deque_new(&deque); }

void teardown_tests() { deque_destroy(deque); }

int main() {
    setup_tests();

    char a = __builtin_annot_intval("symb_int", a);
    ASSUME(a > 0); ASSUME(a < 127);
    char str_a[] = {a, '\0'};

    char b = __builtin_annot_intval("symb_int", b);
    ASSUME(b > 0); ASSUME(b < 127);
    char str_b[] = {b, '\0'};

    char c = __builtin_annot_intval("symb_int", c);
    ASSUME(c > 0); ASSUME(c < 127);
    char str_c[] = {c, '\0'};

    char d = __builtin_annot_intval("symb_int", d);
    ASSUME(d > 0); ASSUME(d < 127);
    char str_d[] = {d, '\0'};

    char e = __builtin_annot_intval("symb_int", e);
    ASSUME(e > 0); ASSUME(e < 127);
    char str_e[] = {e, '\0'};

    char f = __builtin_annot_intval("symb_int", f);
    ASSUME(f > 0); ASSUME(f < 127);
    char str_f[] = {f, '\0'};

    char g = __builtin_annot_intval("symb_int", g);
    ASSUME(g > 0); ASSUME(g < 127);
    char str_g[] = {g, '\0'};

    ASSUME(b != a && b != c && b != d);

    deque_add(deque, str_a);
    deque_add(deque, str_b);
    deque_add(deque, str_c);
    deque_add(deque, str_d);

    Deque *d2;
    deque_new(&d2);

    deque_add(d2, str_e);
    deque_add(d2, str_f);
    deque_add(d2, str_g);

    symb_str(h);
    symb_str(i);

    DequeZipIter zip;
    deque_zip_iter_init(&zip, deque, d2);

    void *e1, *e2;
    while (deque_zip_iter_next(&zip, &e1, &e2) != CC_ITER_END) {
        if (strcmp((char *)e1, str_b) == 0)
            deque_zip_iter_add(&zip, str_h, str_i);
    }

    size_t index;
    deque_index_of(deque, str_h, &index);

    ASSERT(2 == index);

    deque_index_of(deque, str_i, &index);
    ASSERT(2 == index);

    deque_index_of(deque, str_c, &index);
    ASSERT(3 == index);
    ASSERT(1 == deque_contains(deque, str_h));
    ASSERT(1 == deque_contains(d2, str_i));
    ASSERT(5 == deque_size(deque));
    ASSERT(4 == deque_size(d2));
    deque_destroy(d2);

    teardown_tests();
    return 0;
}
