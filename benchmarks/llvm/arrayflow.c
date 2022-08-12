#include "../../headers/llsc_client.h"
#include <stdio.h>
#include <assert.h>

int main() {
    char mapping[] = "bcdefghijklmna";
    int idx;
    make_symbolic(&idx, 4);
    if (idx >= sizeof(mapping) - 1) {
        printf("overflow!\n");
    }
    else {
      char res = mapping[idx];
      printf("encoded to: %c\n", res);
      switch (res) {
        case 'b': assert(1); break;
        case 'c': assert(1); break;
        case 'd': assert(1); break;
        case 'e': assert(1); break;
        case 'f': assert(1); break;
        case 'g': assert(1); break;
        case 'h': assert(1); break;
        case 'i': assert(1); break;
        case 'j': assert(1); break;
        case 'k': assert(1); break;
        case 'l': assert(1); break;
        case 'm': assert(1); break;
        case 'n': assert(1); break;
        case 'a': assert(1); break;
        default : llsc_assert_eager(0); break;
      }
    }
    return 0;
}