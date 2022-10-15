#include <stdint.h>
#include <string.h>
#include <stdbool.h>
#include <stdio.h>

typedef struct {
    int32_t a;
    int16_t b, c;
} pair_t;

typedef struct {
    int32_t a;
    int16_t b;
} pair2_t;

void gs_assert_eager(bool);

int main() {
    do {
        int64_t val = 0x1234567890abcdef;
        pair_t *st = &val;
        int16_t tmp = st->b;
        st->b = st->c;
        st->c = tmp;
        gs_assert_eager(val == 0x5678123490abcdef);
    } while(0);
    do {
        pair_t val = { 0x12345678, 0x90ab, 0xcdef };
        int64_t *pt = &val;
        *pt = 0xfedcba0987654321;
        gs_assert_eager(val.a == 0x87654321);
        gs_assert_eager(val.b == (int16_t)0xba09);
        gs_assert_eager(val.c == (int16_t)0xfedc);
    } while(0);
    do {
        pair_t p1 = { 0x12345678, 0x90ab, 0xcdef };
        pair2_t p2;
        memcpy(&p2, &p1, sizeof(p2));
    } while(0);
    return 0;
}