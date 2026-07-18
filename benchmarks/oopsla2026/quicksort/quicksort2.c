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

int d[100];
int s[SYM_SIZE];

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

// This will cause error

static void init_then_sort(int first, int second, int third, int size) {
  d[0] = first;
  d[1] = second;
  d[2] = third;
  for (int i = 3; i < size; i++) {
    d[i] = size - i;
  }
  qsort(0, size - 1);
}


static void init_symbol(int start, int size) {
  int i = start;
  int j = 0;
  while (i < start + size) {
    d[start + i] = s[j];
    i++;
    j++;
  }
}
static void init_concrete(int start, int size) {
  int i = start;
  while (i < size) {
    d[i] = i;
    i++;
  }
}

static void prelude_aux(int size) {
  for (int j = 0; j < 400; j++) {
    int i = 1;
    while (i < size) {
      d[i] = i * d[i] * 7 + i + 3 + d[i - 1] * i;
      i++;
    }
  }
}
static void prelude(int size) {
  prelude_aux(size);
}

static void create_symbol_table(void) {
  for (int i = 0; i < SYM_SIZE; i++) {
    s[i] = i32_symbolic(i);
  }
}

static int quicksort_entry(void) {
  create_symbol_table();
  init_symbol(0, SYM_SIZE);
  prelude(SYM_SIZE);

  init_concrete(SYM_SIZE, SIZE);
  init_symbol(SYM_SIZE, SYM_SIZE);
  qsort(SYM_SIZE, SYM_SIZE + SIZE - 1);

  init_concrete(0, SIZE);
  init_symbol(0, SYM_SIZE);
  qsort(0, SIZE - 1);


  return 0;
}

void _start(void) { (void)quicksort_entry(); }

int main(void) { return quicksort_entry(); }
