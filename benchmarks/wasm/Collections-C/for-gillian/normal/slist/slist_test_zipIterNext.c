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

    slist_add(list, str_a);
    slist_add(list, str_b);
    slist_add(list, str_c);
    slist_add(list, str_d);

    slist_add(list2, str_e);
    slist_add(list2, str_f);
    slist_add(list2, str_g);

    SListZipIter zip;
    slist_zip_iter_init(&zip, list, list2);

    size_t i = 0;

    void *e1, *e2;
    while (slist_zip_iter_next(&zip, &e1, &e2) != CC_ITER_END) {
        if (i == 0) {
            CHECK_EQUAL_C_STRING(str_a, (char *)e1);
            CHECK_EQUAL_C_STRING(str_e, (char *)e2);
        }
        if (i == 2) {
            CHECK_EQUAL_C_STRING(str_c, (char *)e1);
            CHECK_EQUAL_C_STRING(str_g, (char *)e2);
        }
        i++;
    }
    ASSERT(3 == i);

    teardown_test();
    return 0;
}
