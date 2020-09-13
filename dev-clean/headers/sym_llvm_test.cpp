#include <iostream>
#include <sai_llvm_sym2.hpp>
using namespace std;

int main() {
  cout << "LLVM Symbolic runtime." << endl;
  Ptr<Value> x = make_IntV(1);
  Ptr<Value> y = make_LocV(3, LocV::kStack);
  cout << proj_IntV(x) << endl;
  cout << proj_LocV(y) << endl;
  auto v1 = mt_mem.push_back(x);
  assert(proj_IntV(v1.at(0)) == 1);
  auto v2 = v1.push_back(y);
  assert(proj_LocV(v2.at(1)) == 3);

  auto v3 = mem_take(v2, 1);
  assert(proj_IntV(v3.at(0)) == 1);
  return 0;
}
