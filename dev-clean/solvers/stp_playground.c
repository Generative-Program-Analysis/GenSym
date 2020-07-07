#include <stp/c_interface.h>
#include <stdlib.h>
#include <tuple>
#include <stdio.h>
#include <stdint.h>
#include <stdbool.h>
#include <iostream>

int main(int argc, char **argv) {
  VC vc = vc_createValidityChecker();
  Expr x1 = vc_varExpr(vc, "x", vc_boolType(vc));
  Expr x2 = vc_varExpr(vc, "y", vc_boolType(vc));
  Expr x3 = vc_iffExpr(vc, x1, x2);
  vc_assertFormula(vc, x3);
  Expr x4 = vc_trueExpr(vc);
  Expr x5 = vc_orExpr(vc, x2, x1);
  vc_assertFormula(vc, x5);
  int x6 = vc_query(vc, x1);
  std::cout << x6 << std::endl;
  std::cout << "Done" << std::endl;

  //Delete validity checker
  vc_Destroy(vc);

  return 0;
}