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
    char str_a[] = {a, '\0'};

    char b = __builtin_annot_intval("symb_int", b);
    char str_b[] = {b, '\0'};

    char c = __builtin_annot_intval("symb_int", c);
    char str_c[] = {c, '\0'};

    char d = __builtin_annot_intval("symb_int", d);
    char str_d[] = {d, '\0'};

    char e = __builtin_annot_intval("symb_int", e);
    char str_e[] = {e, '\0'};

    char f = __builtin_annot_intval("symb_int", f);
    char str_f[] = {f, '\0'};

    char g = __builtin_annot_intval("symb_int", g);
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

    DequeZipIter zip;
    deque_zip_iter_init(&zip, deque, d2);

    size_t i = 0;

    void *e1, *e2;
    while (deque_zip_iter_next(&zip, &e1, &e2) != CC_ITER_END) {
        if (i == 0) {
            ASSERT(strcmp(str_a, (char *)e1) == 0);
            ASSERT(strcmp(str_e, (char *)e2) == 0);
        }
        if (i == 2) {
            ASSERT(strcmp(str_c, (char *)e1) == 0);
            ASSERT(strcmp(str_g, (char *)e2) == 0);
        }
        i++;
    }
    ASSERT(3 == i);
    deque_destroy(d2);

    teardown_tests();
    return 0;
}
