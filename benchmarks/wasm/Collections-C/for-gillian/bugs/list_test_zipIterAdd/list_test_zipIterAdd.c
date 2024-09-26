#include "list.h"
#include "utils.h"
#include <gillian-c/gillian-c.h>

static List *list1;
static List *list2;

void setup_tests() { list_new(&list1), list_new(&list2); }

void teardown_test() {
    list_destroy(list1);
    list_destroy(list2);
}

int main() {
    setup_tests();

    char a = (char)__builtin_annot_intval("symb_int", a);

    char str_a[] = {a, '\0'};

    char b = (char)__builtin_annot_intval("symb_int", b);

    char str_b[] = {b, '\0'};

    char c = (char)__builtin_annot_intval("symb_int", c);

    char str_c[] = {c, '\0'};

    char d = (char)__builtin_annot_intval("symb_int", d);

    char str_d[] = {d, '\0'};

    char e = (char)__builtin_annot_intval("symb_int", e);

    char str_e[] = {e, '\0'};

    char f = (char)__builtin_annot_intval("symb_int", f);

    char str_f[] = {f, '\0'};

    char g = (char)__builtin_annot_intval("symb_int", g);

    char str_g[] = {g, '\0'};

    char h = (char)__builtin_annot_intval("symb_int", h);

    char str_h[] = {h, '\0'};

    char i = (char)__builtin_annot_intval("symb_int", i);

    char str_i[] = {i, '\0'};

    char x = (char)__builtin_annot_intval("symb_int", x);

    char str_x[] = {x, '\0'};

    char y = (char)__builtin_annot_intval("symb_int", y);

    char str_y[] = {y, '\0'};

    ASSUME(a != b && a != c && a != d && a != h && a != x);
    ASSUME(b != c && b != d && b != h && b != x);
    ASSUME(c != d && c != h && c != x);
    ASSUME(d != h && d != x && h != x);

    ASSUME(e != f && e != g && e != i && e != y && f != g && f != i && f != y &&
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
    ASSERT(CC_OK == list_index_of(list1, str_h, zero_if_ptr_eq, &index));
    ASSERT(2 == index);

    ASSERT(CC_OK == list_index_of(list1, str_i, zero_if_ptr_eq, &index));
    ASSERT(2 == index);

    ASSERT(CC_OK == list_index_of(list1, str_c, zero_if_ptr_eq, &index));
    ASSERT(3 == index);

    ASSERT(1 == list_contains(list1, str_h));
    ASSERT(1 == list_contains(list2, str_i));
    ASSERT(5 == list_size(list1));
    ASSERT(4 == list_size(list2));

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
