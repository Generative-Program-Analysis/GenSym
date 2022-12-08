#include <stdlib.h>

#ifdef KLEE
#include "klee/klee.h"
#endif

int main() {
    int* ptr;

#ifdef KLEE
    klee_make_symbolic(&ptr, sizeof(int*), "ptr");
#else
    make_symbolic(&ptr, sizeof(int*), "ptr");
#endif

    if (ptr == NULL) {
        return 0;
    } else {
        return *ptr;
    }
}
