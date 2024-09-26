#include "slist.h"
#include "mockups.h"

static SList *list;
static SList *list2;
static int stat;

int a, b, c, d, e, f, g, h;

void setup_test() {
    slist_new(&list), slist_new(&list2);

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

    slist_add(list, va);
    slist_add(list, vb);
    slist_add(list, vc);
    slist_add(list, vd);

    va = (int *)malloc(sizeof(int));
    vb = (int *)malloc(sizeof(int));
    vc = (int *)malloc(sizeof(int));
    vd = (int *)malloc(sizeof(int));

    *va = e;
    *vb = f;
    *vc = g;
    *vd = h;

    slist_add(list2, va);
    slist_add(list2, vb);
    slist_add(list2, vc);
    slist_add(list2, vd);
};

void teardown_test() {
    slist_destroy(list);
    slist_destroy(list2);
};

int main() {
    setup_test();

    int i = sym_int("i");
    int *i1 = (int *)malloc(sizeof(int));

    *i1 = i;

    SListIter iter;
    slist_iter_init(&iter, list);

    assume(c != a && c != b && c != d && d != a && d != b &&
           d != i);

    int *el;
    while (slist_iter_next(&iter, (void *)&el) != CC_ITER_END) {
        if (*el == c)
            slist_iter_add(&iter, i1);
    }
    assert(5 == slist_size(list));

    int *li3;
    slist_get_at(list, 3, (void *)&li3);
    assert(*li3 == i);

    int *li4;
    slist_get_at(list, 4, (void *)&li4);
    assert(d == *li4);

    int x = sym_int("x");
    int *i2 = (int *)malloc(sizeof(int));

    *i2 = x;

    slist_iter_init(&iter, list);
    while (slist_iter_next(&iter, (void *)&el) != CC_ITER_END) {
        if (*el == d)
            slist_iter_add(&iter, i2);
    }

    void *e;
    slist_get_last(list, &e);
    assert(x == *(int *)e);
    assert(6 == slist_size(list));

    teardown_test();
    return 0;
}
