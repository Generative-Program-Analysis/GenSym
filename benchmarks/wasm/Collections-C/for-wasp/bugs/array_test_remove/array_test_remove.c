#include "array.h"
#include "mockups.h"

static Array *v1;
static Array *v2;
static ArrayConf vc;
static int stat;

int main() {
    stat = array_new(&v1);
    int n = sym_int("n");
    assume(n > 2); assume(n < 16);

    int *last;
    int *next_to_last;

    for (int i = 0; i < n; i++) {
        int *a = malloc(sizeof(int));
        array_add(v1, a);
        next_to_last = last;
        last = a;
    }

    array_remove(v1, next_to_last, NULL);

    assert(array_size(v1) < n);

    array_destroy(v1);

    return 0;
}
