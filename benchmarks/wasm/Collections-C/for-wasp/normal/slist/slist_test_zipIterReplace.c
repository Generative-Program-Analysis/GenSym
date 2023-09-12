#include "slist.h"
#include "utils.h"
#include "mockups.h"

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

    int x = sym_int("x");
    assume(x > 0); assume(x < 127);
    char str_x[] = {x, '\0'};

    int y = sym_int("y");
    assume(y > 0); assume(y < 127);
    char str_y[] = {y, '\0'};

    assume(b != a && b != c && b != d);
    assume(h != a && h != b && h != c && h != d);
    assume(i != e && i != f && i != g);

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
    assert(1 == index);

    slist_index_of(list, str_i, &index);
    assert(1 == index);
    assert(1 == slist_contains(list, str_h));
    assert(1 == slist_contains(list2, str_i));

    teardown_test();
    return 0;
}
