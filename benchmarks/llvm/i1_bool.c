#include "../../headers/llsc_client.h"

int main() {
  int a;
  make_symbolic(&a, 4);
  llsc_assume(a >= 0);

  //The condition of the while loop will be compiled into the following ir.
  /*
  %2 = load i32, i32* %a, align 4
  %cmp1 = icmp sgt i32 %2, 3
  %lnot = xor i1 %cmp1, true
  br i1 %lnot, label %while.body, label %while.end
  */
  //which means we will perform arithmetic operation on i1
  while (!(a > 3)) {
    a++;
  }


  bool b1 = !(a > 3);
  //The condition of the if statement will be compiled into the following ir.
  /*
  %5 = load i8, i8* %b1, align 1
  %tobool = trunc i8 %5 to i1
  br i1 %tobool, label %if.then, label %if.end
  */
  //which means the condition of the branch instruction maybe a truncated i1 value
  if (b1) {
    sym_exit(-1);
  }
  return 0;
}