#include "treeset.h"
#include "utils.h"
#include <gillian-c/gillian-c.h>

static TreeSet *set;

int main() {
    treeset_new(cmp, &set);

    int a = __builtin_annot_intval("symb_int", a);
    int b = __builtin_annot_intval("symb_int", b);
    int c = __builtin_annot_intval("symb_int", c);
    int d = __builtin_annot_intval("symb_int", d);
    ASSUME(a < b && b < c && c < d);

    treeset_add(set, &a);
    treeset_add(set, &b);
    treeset_add(set, &c);
    treeset_add(set, &d);

    int one = 0;
    int two = 0;
    int three = 0;
    int four = 0;

    TreeSetIter iter;
    treeset_iter_init(&iter, set);

    void *e;
    while (treeset_iter_next(&iter, &e) != CC_ITER_END) {
        if (*((int *)e) == a)
            one++;

        if (*((int *)e) == b)
            two++;

        if (*((int *)e) == c)
            three++;

        if (*((int *)e) == d)
            four++;
    }

    ASSERT(1 == one);
    ASSERT(1 == two);
    ASSERT(1 == three);
    ASSERT(1 == four);
}
