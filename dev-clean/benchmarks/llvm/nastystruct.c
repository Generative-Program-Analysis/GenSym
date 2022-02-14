#include <stdint.h>
#include <stdio.h>
#include <stdbool.h>

void llsc_assert_eager(bool);

struct S {
  char c;
  int i;
};

struct T {
  int a;
  int b;
};

struct W {
  uint64_t a;
  uint64_t b;
};

void func_ST() {
  struct S s = { 'a', 513 };
  struct S* p = &s;
  char* p2 = &(p->c);
  char w = *(p2+1);
  printf("%d", w);

  struct T t = { 1, 2 };
  char* m = (char*) &(t.b) - 1;
  int i = *((int*)m);
  llsc_assert_eager(i == 512);

  struct W w2 = {1, 2};
  uint32_t* m2 = ((uint32_t*) &(w2.a)) + 1;
  uint64_t i2 = *((uint64_t*) m2);
  printf("%ld", i2);
}

struct P {
  int* ptr;
};

void func_P() {
  struct P p;
  memset(&p, 0, sizeof(p));
  int tmp = 10;
  p.ptr = &tmp;
  llsc_assert_eager(*(p.ptr) == 10);
  printf("%d", *(p.ptr));
}

#ifdef BITSTRUCT
struct weird {
   uint32_t v : 20;
   uint32_t v0 : 24;
   uint64_t v1 : 40;
   uint64_t v2 : 48;
   uint64_t v3 : 56;
   uint64_t v4 : 57;
};

void func_weird() {
  struct weird w = { 20, 123, 456, 789, 1024, 777 };
  printf("%ld %ld %ld %ld %ld %ld \n", w.v, w.v0, w.v1, w.v2, w.v3, w.v4);
}
#endif

int main() {
  func_ST();
  func_P();
  //func_weird();
  return 0;
}
