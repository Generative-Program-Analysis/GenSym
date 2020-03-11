
/*****************************************
Emitting C Generated Code
*******************************************/
    
#include <sai_imp_concrete.hpp>
#include <immer/map.hpp>
#include <stdlib.h>
#include <tuple>
#include <stdio.h>
#include <stdint.h>
#include <stdbool.h>
#include <immer/flex_vector.hpp>
#include <immer/algorithm.hpp>
#include <sai.hpp>
#include <chrono>

using namespace std::chrono; 

unsigned long long factorial(unsigned long long n) {
  int i = 1;
  unsigned long long fact = 1;
  while (i <= n) {
    fact = fact * i;
    i = i + 1;
  }
  return fact;
}

/**************** Snippet ****************/
unsigned long long Snippet(long long x0){
  immer::map<std::string, Value> x3 = immer::map<std::string, Value>();
  Value x4 = (struct IntV){1};
  immer::map<std::string, Value> x5 = x3.insert({"i", x4});
  immer::map<std::string, Value> x6 = x5.insert({"fact", x4});
  std::function<immer::map<std::string, Value>(immer::map<std::string, Value>)> x7;
  x7 = [&](immer::map<std::string, Value> x8)->immer::map<std::string, Value> {
    Value x9 = x8.at("i");
    unsigned long long x10 = std::get<IntV>(x9).i;
    immer::map<std::string, Value> x11 = x10 <= x0 ? ({
        Value x12 = x8.at("fact");
        unsigned long long x13 = std::get<IntV>(x12).i;
        Value x14 = (struct IntV){x13 * x10};
        immer::map<std::string, Value> x15 = x8.insert({"fact", x14});
        Value x16 = x15.at("i");
        unsigned long long x17 = std::get<IntV>(x16).i;
        Value x18 = (struct IntV){x17 + 1};
        immer::map<std::string, Value> x19 = x15.insert({"i", x18});
        x7(x19);
        }) : x8;
    return x11;
  };
  Value x20 = x7(x6).at("fact");
  unsigned long long x21 = std::get<IntV>(x20).i;
  return x21;
}

/*****************************************
End of C Generated Code
*******************************************/
int main(int argc, char *argv[]) {
  if (argc != 2) {
    printf("usage: %s <arg>\n", argv[0]);
    return 0;
  }

  // TODO: what is the right way to pass arguments?
  auto start = high_resolution_clock::now(); 
  std::cout << Snippet(atoi(argv[1])) << std::endl;
  auto stop = high_resolution_clock::now();
  auto duration = duration_cast<microseconds>(stop - start);
  std::cout << "duration (LMS gen): " << duration.count() << std::endl;

  start = high_resolution_clock::now(); 
  std::cout << factorial(atoi(argv[1])) << std::endl;
  stop = high_resolution_clock::now();
  duration = duration_cast<microseconds>(stop - start);
  std::cout << "duration (hand written): " << duration.count() << std::endl;
  return 0;
}

