#include <stdint.h>
#include <string.h>
#include <stdio.h>

typedef struct Pair {
    int32_t a, b;
} pair_t;

typedef struct Option {
    char longname[10];
    int32_t idx;
    int64_t func;
} option_t;

typedef int32_t (*func_t)(pair_t);

int32_t add(pair_t arg) {  //arg coerce
    return arg.a + arg.b;
}

int32_t sub(pair_t arg) {
    return arg.a - arg.b;
}

option_t optionlist[10] = {  // structured global constant, partially zero
    {"hello", 1, add},  // ptrtoint
    {"world", 2, sub}
};

int main(int argc, const char** argv) {
    int index;
    make_symbolic(&index, sizeof(index));
    for (int i = 0; i < 2; i++) {
        if (optionlist[i].idx == index) {
            func_t fun = optionlist[i].func;  // inttoptr
            pair_t tmp;
            tmp.a = 4;
            tmp.b = 2;
            printf("%d\n", fun(tmp));  // indirect call
            return 0;
        }
    }
    return 1;
}