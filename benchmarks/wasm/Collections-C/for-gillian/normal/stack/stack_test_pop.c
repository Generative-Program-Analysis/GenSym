#include "stack.h"
#include <gillian-c/gillian-c.h>

static Stack *s;

void setup_tests() { stack_new(&s); }

void teardown_tests() { stack_destroy(s); }

int main() {
    setup_tests();

    int a = __builtin_annot_intval("symb_int", a);
    int b = __builtin_annot_intval("symb_int", b);
    int c = __builtin_annot_intval("symb_int", c);

    stack_push(s, (void *)&a);
    stack_push(s, (void *)&b);
    stack_push(s, (void *)&c);

    int *pop;
    int *peek;

    stack_pop(s, (void *)&pop);
    ASSERT(&c == pop);

    stack_peek(s, (void *)&peek);
    ASSERT(&b == peek);

    teardown_tests();
    return 0;
}
