#include "stp/c_interface.h"
#include <iostream>
#include <variant>

std::monostate handle(VC vc, int n) {
  switch (n) {
    case 0:
      std::cout << "Query is invalid" << std::endl;
      std::cout << "Counter example:\n";
      vc_printCounterExample(vc);
      break;
    case 1:
      std::cout << "Query is Valid" << std::endl;
      break;
    case 2:
      std::cout << "Could not answer query" << std::endl;
      break;
    case 3:
      std::cout << "timeout" << std::endl;
      break;
  }
  std::cout << std::endl;
  return std::monostate{};
}
