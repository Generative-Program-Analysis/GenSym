#include <stdint.h>
#include <string.h>
#include <stdio.h>


typedef struct Pair {
    int32_t a, b;
} pair_t;


typedef struct Option {
    char longname[10];
    int32_t idx;
    int32_t (*func)(pair_t);  // function pointer
} option_t;


int32_t add(pair_t arg) {  //arg coerce
    return arg.a + arg.b;
}

int32_t sub(pair_t arg) {
    return arg.a - arg.b;
}


option_t optionlist[] = {  // structured global constant
    {"hello", 1, add},
    {"world", 2, sub}
};


int main(int argc, const char** argv) {
    int index;
    make_symbolic(&index, sizeof(index));
    for (int i = 0; i < sizeof(optionlist) / sizeof(option_t); i++) {
        if (optionlist[i].idx == index) {
            pair_t tmp = {4, 2};
            printf("%d\n", optionlist[i].func(tmp));  // indirect call
            return 0;
        }
    }
    return 1;
}