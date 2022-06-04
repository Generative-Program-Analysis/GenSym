#include "../../headers/llsc_client.h"

int main() {
    int nums[] = {0,1,2,3,4};
    int * p_int = (int *)nums + 2;
    int idx;
    make_symbolic(&idx, 4);
    llsc_assume(idx != 1);

    sym_print(p_int[idx]);
    return 0;
}