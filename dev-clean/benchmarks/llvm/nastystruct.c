#include <stdint.h>
#include <stdio.h>

struct S {
  char c;
  int i;
};

struct T {
  int a;
  int b;
};

void func_ST() {
  struct S s = { 'a', 512 };
  struct S* p = &s;
  char* p2 = &(p->c);
  char w = *(p2+1);

  struct T t = { 1, 2 };
  char* m = (char*) &(t.b) - 1;
  int i = *((int*)p);
  printf("%d", i);
}

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

int main() {
  func_weird();
  return 0;
}
