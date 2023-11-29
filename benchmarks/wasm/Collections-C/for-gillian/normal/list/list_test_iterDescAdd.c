#include "list.h"
#include <gillian-c/gillian-c.h>

static List *list1;
static List *list2;

int a, b, c, d, e, f, g, h;

void setup_tests() {
    list_new(&list1), list_new(&list2);

    a = __builtin_annot_intval("symb_int", a);
    b = __builtin_annot_intval("symb_int", b);
    c = __builtin_annot_intval("symb_int", c);
    d = __builtin_annot_intval("symb_int", d);
    e = __builtin_annot_intval("symb_int", e);
    f = __builtin_annot_intval("symb_int", f);
    g = __builtin_annot_intval("symb_int", g);
    h = __builtin_annot_intval("symb_int", h);

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

    int *px = (int *) malloc(sizeof(int));
    int *py = (int *) malloc(sizeof(int));
    int x = __builtin_annot_intval("symb_int", x);
    int y = __builtin_annot_intval("symb_int", y);

    *px = x, *py = y;

    ASSUME(c != a && c != b && c != d && d != a && d != b);

    ListIter iter;
    list_diter_init(&iter, list1);

    int *i;
    while (list_diter_next(&iter, (void *)&i) != CC_ITER_END) {
        if (*i == d) // add to tail
            list_diter_add(&iter, px);

        if (*i == c)
            list_diter_add(&iter, py);
    }

    ASSERT(6 == list_size(list1));
    int *el;
    list_get_first(list1, (void *)&el);
    ASSERT(a == *el);

    list_get_last(list1, (void *)&el);
    ASSERT(d == *el);

    list_get_at(list1, 2, (void *)&el);
    ASSERT(*py == *el);

    list_get_at(list1, 4, (void *)&el);
    ASSERT(*px == *el);

    teardown_test();
}
