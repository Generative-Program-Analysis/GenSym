#include <stdio.h>

int main() {
    char mapping[] = "bcdefghijklmna";
    int idx;
    make_symbolic(&idx, 4);
    if (idx >= sizeof(mapping) - 1) {
        printf("overflow!\n");
    }
    else {
        printf("encoded to: %c", mapping[idx]);
    }
    return 0;
}