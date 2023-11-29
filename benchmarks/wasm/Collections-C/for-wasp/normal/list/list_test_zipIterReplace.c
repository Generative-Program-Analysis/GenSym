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

    int h = sym_int("h");
    assume(h > 0); assume(h < 127);
    char str_h[] = {h, '\0'};

    int i = sym_int("i");
    assume(i > 0); assume(i < 127);
    char str_i[] = {i, '\0'};

    assume(a != b && a != c && a != d && a != h && b != c && b != d && b != h &&
           c != d && c != h && d != h);

    assume(e != f && e != g && e != i && f != g && f != i && g != i);

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
            list_zip_iter_replace(&zip, str_h, str_i, &r1, &r2);
    }

    size_t index;
    assert(CC_OK == list_index_of(list1, str_h, zero_if_ptr_eq, &index));
    assert(1 == index);

    assert(CC_OK == list_index_of(list2, str_i, zero_if_ptr_eq, &index));
    assert(1 == index);
    assert(1 == list_contains(list1, str_h));
    assert(1 == list_contains(list2, str_i));

    teardown_test();
}
