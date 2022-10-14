#include "gensym.hpp"
using namespace immer;

int main() {
  flex_vector_transient<int> xs = flex_vector_transient<int>{};
  xs.push_back(3);
  auto ys = xs;
  // auto& ys = xs;
  ys.push_back(4);
  auto zs = std::move(xs);
  printf("xs %ld ys %ld zs %ld\n", xs.size(), ys.size(), zs.size());
  printf("hello GenSym\n");
}
