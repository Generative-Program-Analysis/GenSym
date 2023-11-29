#include "slist.h"
#include "utils.h"
#include <gillian-c/gillian-c.h>

static SList *list;
static SList *list2;
static int stat;

void setup_test() {
    stat = slist_new(&list);
    slist_new(&list2);
};

void teardown_test() {
    slist_destroy(list);
    slist_destroy(list2);
};

int main() {
    setup_test();

    char a = (char)__builtin_annot_intval("symb_int", a);
    ASSUME(a > 0); ASSUME(a < 127);
    char str_a[] = {a, '\0'};

    char b = (char)__builtin_annot_intval("symb_int", b);
    ASSUME(b > 0); ASSUME(b < 127);
    char str_b[] = {b, '\0'};

    char c = (char)__builtin_annot_intval("symb_int", c);
    ASSUME(c > 0); ASSUME(c < 127);
    char str_c[] = {c, '\0'};

    char d = (char)__builtin_annot_intval("symb_int", d);
    ASSUME(d > 0); ASSUME(d < 127);
    char str_d[] = {d, '\0'};

    char e = (char)__builtin_annot_intval("symb_int", e);
    ASSUME(e > 0); ASSUME(e < 127);
    char str_e[] = {e, '\0'};

    char f = (char)__builtin_annot_intval("symb_int", f);
    ASSUME(f > 0); ASSUME(f < 127);
    char str_f[] = {f, '\0'};

    char g = (char)__builtin_annot_intval("symb_int", g);
    ASSUME(g > 0); ASSUME(g < 127);
    char str_g[] = {g, '\0'};

    char h = (char)__builtin_annot_intval("symb_int", h);
    ASSUME(h > 0); ASSUME(h < 127);
    char str_h[] = {h, '\0'};

    char i = (char)__builtin_annot_intval("symb_int", i);
    ASSUME(i > 0); ASSUME(i < 127);
    char str_i[] = {i, '\0'};

    char x = (char)__builtin_annot_intval("symb_int", x);
    ASSUME(x > 0); ASSUME(x < 127);
    char str_x[] = {x, '\0'};

    char y = (char)__builtin_annot_intval("symb_int", y);
    ASSUME(y > 0); ASSUME(y < 127);
    char str_y[] = {y, '\0'};

    ASSUME(b != a && b != c && b != d);
    ASSUME(h != a && h != b && h != c && h != d);
    ASSUME(i != e && i != f && i != g);

    slist_add(list, str_a);
    slist_add(list, str_b);
    slist_add(list, str_c);
    slist_add(list, str_d);

    slist_add(list2, str_e);
    slist_add(list2, str_f);
    slist_add(list2, str_g);

    SListZipIter zip;
    slist_zip_iter_init(&zip, list, list2);

    void *e1, *e2;
    void *r1, *r2;
    while (slist_zip_iter_next(&zip, &e1, &e2) != CC_ITER_END) {
        if (strcmp((char *)e1, str_b) == 0)
            slist_zip_iter_replace(&zip, str_h, str_i, &r1, &r2);
    }

    size_t index;
    slist_index_of(list, str_h, &index);
    ASSERT(1 == index);

    slist_index_of(list, str_i, &index);
    ASSERT(1 == index);
    ASSERT(1 == slist_contains(list, str_h));
    ASSERT(1 == slist_contains(list2, str_i));

    teardown_test();
    return 0;
}
