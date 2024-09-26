#include "list.h"
#include "mockups.h"

static List *list1;
static List *list2;

int a, b, c, d, e, f, g, h;

void setup_tests() {
    list_new(&list1), list_new(&list2);

    a = sym_int("a");
    b = sym_int("b");
    c = sym_int("c");
    d = sym_int("d");
    e = sym_int("e");
    f = sym_int("f");
    g = sym_int("g");
    h = sym_int("h");

    int *va = (int *)malloc(sizeof(int));
    int *vb = (int *)malloc(sizeof(int));
    int *vc = (int *)malloc(sizeof(int));
    int *vd = (int *)malloc(sizeof(int));

    *va = a;
    *vb = b;
    *vc = c;
    *vd = d;

    list_add(list1, va);
    list_add(list1, vb);
    list_add(list1, vc);
    list_add(list1, vd);

    va = (int *)malloc(sizeof(int));
    vb = (int *)malloc(sizeof(int));
    vc = (int *)malloc(sizeof(int));
    vd = (int *)malloc(sizeof(int));

    *va = e;
    *vb = f;
    *vc = g;
    *vd = h;

    list_add(list2, va);
    list_add(list2, vb);
    list_add(list2, vc);
    list_add(list2, vd);
}

void teardown_test() {
    list_destroy_cb(list1, free);
    list_destroy(list2);
}

int main() {
    setup_tests();

    ListIter iter;
    list_diter_init(&iter, list1);

    assume(a != b && a != c && a != d && c != b && c != d);

    int *i;
    while (list_diter_next(&iter, (void *)&i) != CC_ITER_END) {
        if (*i == a || *i == c) {
            list_diter_remove(&iter, NULL);
            free(i);
        }
    }
    assert(2 == list_size(list1));

    int *first;
    list_get_first(list1, (void *)&first);

    int *last;
    list_get_last(list1, (void *)&last);

    int *i1;
    list_get_at(list1, 1, (void *)&i1);

    assert(b == *first);
    assert(d == *last);
    assert(d == *i1);

    teardown_test();
}
