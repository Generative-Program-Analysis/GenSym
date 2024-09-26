#include "mockups.h"

void assume(int expr) {/*EMPTY*/}
void assert(int expr) {/*EMPTY*/}
void* alloc(int base, int size) { return (void*)base; }
void dealloc(void *ptr) { /*EMPTY*/ }
int sym_int(char *name) { return (int)name; }
