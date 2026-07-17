#ifdef KLEE
#include "klee/klee.h"
#endif

#ifndef SIZE
#define SIZE 20
#endif

#ifndef SYM_SIZE
#define SYM_SIZE 5
#endif

extern int i32_symbolic(int value)
    __attribute__((import_module("i32"), import_name("symbolic")));

int d[SIZE];

void _start(void);

static void qsort(int l, int r) {
  if (l < r) {
    int x = d[r];
    int j = l - 1;

    for (int i = l; i <= r; i++) {
      if (d[i] <= x) {
        j++;
        int temp = d[i];
        d[i] = d[j];
        d[j] = temp;
      }
    }

    qsort(l, j - 1);
    qsort(j + 1, r);
  }
}

static void init_5_of_5_symbol_then_sort(void) {
  for (int i = 0; i < 5; i++) {
    d[i] = i32_symbolic(i);
  }
  qsort(0, 4);
}

static void init_5_of_7_symbol_then_sort(void) {
  for (int i = 0; i < 5; i++) {
    d[i] = i32_symbolic(i);
  }
  for (int i = 5; i < 7; i++) {
    d[i] = 7 - i;
  }
  qsort(0, 6);
}

static void init_7_of_7_symbol_then_sort(void) {
  for (int i = 0; i < 7; i++) {
    d[i] = i32_symbolic(i);
  }
  qsort(0, 6);
}

// This will cause error
static void init_10_of_10_symbol_then_sort(void) {
  for (int i = 0; i < 10; i++) {
    d[i] = i32_symbolic(i);
  }
  qsort(0, 9);
}

static void init_then_sort(int first, int second, int third, int size) {
  d[0] = first;
  d[1] = second;
  d[2] = third;
  for (int i = 3; i < size; i++) {
    d[i] = size - i;
  }
  qsort(0, size - 1);
}

static void init_3_symbol_of_5_then_sort(void) {
  init_then_sort(i32_symbolic(0), i32_symbolic(1), i32_symbolic(2), 5);
}

static void init_symbol(int size) {
  int i = 0;
  while (i < size) {
    d[i] = i32_symbolic(i);
    i++;
  }
  while (i < SIZE) {
#ifdef PRELUDE
    d[i] = i;
#else
    d[i] = SIZE - i;
#endif
    i++;
  }
}

static void prelude_aux(int size) {
  for (int j = 0; j < 30; j++) {
    int i = 1;
    while (i < size) {
      d[i] = d[i] * 7 + i + 3 - d[i - 1];
      i++;
    }
  }
}
static void prelude(int size) {
  #ifdef PRELUDE
    prelude_aux(size);
  #endif
}

static int quicksort_entry(void) {
  // init_symbol(SYM_SIZE);
  // prelude(SYM_SIZE);
  // qsort(0, SIZE);

  init_symbol(SYM_SIZE);
  qsort(0, SIZE - 1);
  return 0;
}

void _start(void) { (void)quicksort_entry(); }

int main(void) { return quicksort_entry(); }
