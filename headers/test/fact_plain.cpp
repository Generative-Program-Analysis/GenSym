#include <iostream>
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

int main(int argc, char *argv[]) {
  if (argc != 2) {
    printf("usage: %s <arg>\n", argv[0]);
    return 0;
  }

  auto start1 = high_resolution_clock::now(); 
  std::cout << factorial(atoi(argv[1])) << std::endl;
  auto stop1 = high_resolution_clock::now();
  auto duration1 = duration_cast<microseconds>(stop1 - start1);
  std::cout << "duration (hand written): " << duration1.count() << std::endl;
}
