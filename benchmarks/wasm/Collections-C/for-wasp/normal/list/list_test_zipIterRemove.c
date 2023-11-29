#include "list.h"
#include "utils.h"
#include "mockups.h"

static List *list1;
static List *list2;

void setup_tests() { list_new(&list1), list_new(&list2); }

void teardown_test() {
    list_destroy(list1);
    list_destroy(list2);
}

int main() {
    setup_tests();

    int a = sym_int("a");
    assume(a > 0); assume(a < 127);
    char str_a[] = {a, '\0'};

    int b = sym_int("b");
    assume(b > 0); assume(b < 127);
    char str_b[] = {b, '\0'};

    int c = sym_int("c");
    assume(c > 0); assume(c < 127);
    char str_c[] = {c, '\0'};

    int d = sym_int("d");
    assume(d > 0); assume(d < 127);
    char str_d[] = {d, '\0'};

    int e = sym_int("e");
    assume(e > 0); assume(e < 127);
    char str_e[] = {e, '\0'};

    int f = sym_int("f");
    assume(f > 0); assume(f < 127);
    char str_f[] = {f, '\0'};

    int g = sym_int("g");
    assume(g > 0); assume(g < 127);
    char str_g[] = {g, '\0'};

    assume(a != b && a != c && a != d && a != e && a != f && a != g && b != c &&
           b != d && b != e && b != f && b != g && c != d && c != e && c != f &&
           c != g && d != e && d != f && d != g && e != f && e != g && f != g);

    list_add(list1, str_a);
    list_add(list1, str_b);
    list_add(list1, str_c);
    list_add(list1, str_d);

    list_add(list2, str_e);
    list_add(list2, str_f);
    list_add(list2, str_g);

    ListZipIter zip;
    list_zip_iter_init(&zip, list1, list2);

    void *e1, *e2;
    void *r1, *r2;
    while (list_zip_iter_next(&zip, &e1, &e2) != CC_ITER_END) {
        if (strcmp((char *)e1, str_b) == 0)
            list_zip_iter_remove(&zip, &r1, &r2);
    }
    CHECK_EQUAL_C_STRING(str_b, (char *)r1);
    CHECK_EQUAL_C_STRING(str_f, (char *)r2);
    assert(0 == list_contains(list1, str_b));
    assert(0 == list_contains(list2, str_f));
    assert(3 == list_size(list1));
    assert(2 == list_size(list2));

    teardown_test();
}
