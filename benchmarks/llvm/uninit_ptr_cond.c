#include <stdlib.h>

// This test must be run with --thread=1
// so that the NullDerefException is caught

int main() {
    int* ptr;

    if (ptr == NULL) {
        return 0;
    } else {
        return *ptr;
    }
}
