#include "array.h"
#include <gillian-c/gillian-c.h>

static Array *v1;
static Array *v2;
static ArrayConf vc;
static int stat;

int main() {
    stat = array_new(&v1);

    int a = __builtin_annot_intval("symb_int", a);
    ASSERT(0 == array_size(v1));
    ASSERT(CC_ERR_OUT_OF_RANGE == array_add_at(v1, &a, 1));

    array_destroy(v1);

    return 0;
}
