#include "treeset.h"
#include "utils.h"
#include "mockups.h"

static TreeSet *set;

int main() {
    treeset_new(cmp, &set);

    int a = sym_int("a");
    int b = sym_int("b");
    int c = sym_int("c");
    assume(a != b && a != c && b != c);

    treeset_add(set, &a);
    treeset_add(set, &b);
    treeset_add(set, &c);
    treeset_add(set, &c);

    assert(3 == treeset_size(set));
    assert(1 == treeset_contains(set, &a));
    assert(1 == treeset_contains(set, &b));
}
