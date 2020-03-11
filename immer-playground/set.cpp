#include <tuple>
#include <iostream>
#include <cstdarg>
#include <functional>
#include <immer/set.hpp>
#include <immer/algorithm.hpp>
#include <sai.hpp>

int main(int argc, char** argv) {
  const auto v0 = immer::set<int>{};
  assert(v0.count(42) == 0);
  assert((!Set::contains(v0, 42)));

  immer::set<int> v1 = v0.insert(42);
  assert(v1.count(42) == 1);
  assert(Set::contains(v1, 42));
  assert(v1.count(42) == 1);

  const auto v2 = v1.erase(42);
  assert(v2.count(42) == 0);

  auto s1 = Set::make_set({1, 2, 3, 4});
  assert(s1.size() == 4);

  auto s2 = Set::join(v1, s1);
  assert(s2.size() == 5);

  auto s3 = Set::make_set({3, 4, 5});
  auto s4 = Set::intersect(s3, s2);
  assert(s4.size() == 2);

  print_set(s4);

  auto s5 = Set::map<int>(s4, [](auto x) { return x + 1; });
  print_set(s5);

  int n = Set::foldLeft(s5, 0, [](int x, int y) { return x + y; });
  assert(n == 9);

  auto vec = Set::toList(s5);
  print_vec(vec);
}
