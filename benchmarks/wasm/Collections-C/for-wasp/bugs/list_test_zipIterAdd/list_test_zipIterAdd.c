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

    char str_a[] = {a, '\0'};

    int b = sym_int("b");

    char str_b[] = {b, '\0'};

    int c = sym_int("c");

    char str_c[] = {c, '\0'};

    int d = sym_int("d");

    char str_d[] = {d, '\0'};

    int e = sym_int("e");

    char str_e[] = {e, '\0'};

    int f = sym_int("f");

    char str_f[] = {f, '\0'};

    int g = sym_int("g");

    char str_g[] = {g, '\0'};

    int h = sym_int("h");

    char str_h[] = {h, '\0'};

    int i = sym_int("i");

    char str_i[] = {i, '\0'};

    int x = sym_int("x");

    char str_x[] = {x, '\0'};

    int y = sym_int("y");

    char str_y[] = {y, '\0'};

    assume(a != b && a != c && a != d && a != h && a != x);
    assume(b != c && b != d && b != h && b != x);
    assume(c != d && c != h && c != x);
    assume(d != h && d != x && h != x);

    assume(e != f && e != g && e != i && e != y && f != g && f != i && f != y &&
           g != i && g != y && i != y);

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
    while (list_zip_iter_next(&zip, &e1, &e2) != CC_ITER_END) {
        if (strcmp((char *)e1, str_b) == 0)
            list_zip_iter_add(&zip, str_h, str_i);
    }

    size_t index;
    assert(CC_OK == list_index_of(list1, str_h, zero_if_ptr_eq, &index));
    assert(2 == index);

    assert(CC_OK == list_index_of(list1, str_i, zero_if_ptr_eq, &index));
    assert(2 == index);

    assert(CC_OK == list_index_of(list1, str_c, zero_if_ptr_eq, &index));
    assert(3 == index);

    assert(1 == list_contains(list1, str_h));
    assert(1 == list_contains(list2, str_i));
    assert(5 == list_size(list1));
    assert(4 == list_size(list2));

    list_zip_iter_init(&zip, list1, list2);
    while (list_zip_iter_next(&zip, &e1, &e2) != CC_ITER_END) {
        if (strcmp((char *)e2, str_g) == 0)
            list_zip_iter_add(&zip, str_x, str_y);
    }

    char *last;
    list_get_last(list1, (void *)&last);
    CHECK_EQUAL_C_STRING(str_d, last);

    list_get_last(list2, (void *)&last);
    CHECK_EQUAL_C_STRING(str_y, last);

    teardown_test();
}
