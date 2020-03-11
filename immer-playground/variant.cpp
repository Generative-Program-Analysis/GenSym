#include <iostream>
#include <variant>
#include <sai.hpp>
#include <sai_imp_concrete.hpp>

int main(int argc, char** argv) {
  Value v1 = (struct IntV){ 42 };
  Value v2 = (struct BoolV){ true };
  
  std::cout << isInstanceOf<IntV>(v1) << std::endl;
  std::cout << isInstanceOf<IntV>(v2) << std::endl;
  std::cout << isInstanceOf<BoolV>(v2) << std::endl;

  //std::cout << std::get<IntV>(v1).i;
  
  std::cout << v1;
  std::cout << v2;
  
}
