#ifndef STP_HANDLE
#define STP_HANDLE

#include "stp/c_interface.h"
#include <iostream>
#include <variant>

inline int queries = 0;

inline std::monostate handle_query(VC vc, int n) {
  queries++;
  std::cout << "Query number: " << queries << std::endl;
  switch (n) {
    case 0:
      std::cout << "Query is invalid" << std::endl;
      vc_printCounterExample(vc);
      break;
    case 1:
      std::cout << "Query is Valid" << std::endl;
      break;
    case 2:
      std::cout << "Could not answer the query" << std::endl;
      break;
    case 3:
      std::cout << "timeout" << std::endl;
      break;
  }
  std::cout << std::endl;
  return std::monostate{};
}

inline bool stp_is_valid(VC vc, Expr e) {
  int n = vc_query(vc, e);
  switch (n) {
    case 0: return false;
    case 1: return true;
    default:
      std::cout << "Warning: Could not answer the query or timeout" << std::endl;
      return false;
  }
}

inline bool stp_is_sat(VC vc, Expr e) {
  Expr not_e = vc_notExpr(vc, e);
  int n = vc_query(vc, not_e);
  switch (n) {
    case 0: return true;
    case 1: return false;
    default:
      std::cout << "Warning: Could not answer the query or timeout" << std::endl;
      return false;
  }
}

#endif
