#include "slist.h"
#include <gillian-c/gillian-c.h>

static SList *list;
static SList *list2;
static int stat;

int a, b, c, d, e, f, g, h;

void setup_test() {
    slist_new(&list), slist_new(&list2);

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

    int *last;
    slist_get_last(list, (void *)&last);
    ASSERT(d == *last);

    teardown_test();
    return 0;
}
