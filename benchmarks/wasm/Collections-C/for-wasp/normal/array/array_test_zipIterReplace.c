#include "array.h"
#include "mockups.h"

static Array *v1;
static Array *v2;
static ArrayConf vc;
static int stat;

int main() {
    stat = array_new(&v1);

    int a = sym_int("a");

    char str_a[] = {a, '\0'};

    int b = sym_int("b");

    char str_b[] = {b, '\0'};

    int c = sym_int("c");

    char str_c[] = {c, '\0'};

    int d = sym_int("d");

    char str_d[] = {d, '\0'};

    int e = sym_int("e");

    char str_e[] = {e, '\0'};

    int f = sym_int("f");

    char str_f[] = {f, '\0'};

    int g = sym_int("g");

    char str_g[] = {g, '\0'};

    int h = sym_int("h");

    char str_h[] = {h, '\0'};

    int i = sym_int("i");

    char str_i[] = {i, '\0'};

    assume((!(strcmp(str_a, str_b) == 0)) && (!(strcmp(str_c, str_b) == 0)) &&
           (!(strcmp(str_c, str_a) == 0)) && (!(strcmp(str_c, str_d) == 0)) &&
           (!(strcmp(str_d, str_b) == 0)) && (!(strcmp(str_a, str_h) == 0)) &&
           (!(strcmp(str_c, str_h) == 0)) && (!(strcmp(str_d, str_h) == 0)) &&
           (!(strcmp(str_b, str_h) == 0)) && (!(strcmp(str_i, str_e) == 0)) &&
           (!(strcmp(str_i, str_g) == 0)) && (!(strcmp(str_i, str_f) == 0)));

    array_add(v1, str_a);
    array_add(v1, str_b);
    array_add(v1, str_c);
    array_add(v1, str_d);

    array_new(&v2);

    array_add(v2, str_e);
    array_add(v2, str_f);
    array_add(v2, str_g);

    ArrayZipIter zip;
    array_zip_iter_init(&zip, v1, v2);

    void *e1, *e2;
    void *r1, *r2;
    while (array_zip_iter_next(&zip, &e1, &e2) != CC_ITER_END) {
        if (strcmp((char *)e1, str_b) == 0) {
            array_zip_iter_replace(&zip, str_h, str_i, &r1, &r2);
        }
    }

    size_t index;

    assert(CC_OK == array_index_of(v1, str_h, &index));
    assert(1 == index);
    assert(CC_OK == array_index_of(v2, str_i, &index));
    assert(1 == index);
    assert(1 == array_contains(v1, str_h));
    assert(1 == array_contains(v2, str_i));

    array_destroy(v2);

    array_destroy(v1);

    return 0;
}
