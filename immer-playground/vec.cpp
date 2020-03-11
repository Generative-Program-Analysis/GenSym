#include <tuple>
#include <iostream>
#include <functional>
#include <immer/flex_vector.hpp>
#include <immer/algorithm.hpp>
#include <sai.hpp>

/*
template<typename T, typename Fn>
auto map(immer::flex_vector<T>& v, Fn fn) {
  auto r = immer::accumulate(v.begin(), v.end(), v.take(0),
			     [&](auto v, auto x) {
			       return v.push_back(fn(x));
			     });
  return r;
}
*/

int main(int argc, char** argv) {
  auto v1 = immer::flex_vector<int> {1, 2, 3};
  auto v2 = immer::accumulate(v1, 0, [&](int x, int y)->int { return x + y; });
  auto v3 = immer::accumulate(v1, 1, [&](int x, int y) { return x * y; });

  // map
  auto mt = immer::flex_vector<int>();
  std::function<immer::flex_vector<int>(const immer::flex_vector<int>, const int)> f = [](const immer::flex_vector<int> v, const int x)->immer::flex_vector<int> { return v.push_back(x+1); };
  //auto f = [](const immer::flex_vector<int> v, const int x)->immer::flex_vector<int> { return v.push_back(x+1); };
  auto v4 = immer::accumulate(v1.begin(), v1.end(), mt, f);

  // another way to do map
  auto v5 = immer::accumulate(v1.begin(), v1.end(),
			      v1.take(0),
			      [](auto v, auto x) { return v.push_back(x+1); });
  assert(v4 == v5);

  // still map
  immer::flex_vector<int> v6 = Vec::vmap<int>(v1, [](int x) { return x + 1; } );

  for (int i = 0; i < v6.size(); i++) {
    std::cout << v6.at(i) << "\n";
  }

  assert(v5 == v6);

  // foldLeft and foldRight

  auto n = Vec::foldLeft(v1, 0, [&](int x, int y) { return x + y; });
  std::cout << "n is " << n ;
  assert(n == 6);

  n = Vec::foldLeft(v1, 1, [&](int x, int y) { return x * (y + 1); });
  assert(n == 2*3*4);

  n = Vec::foldRight(v1, 0, [&](int x , int y) { return x + y; });
  assert(n == 6);
  n = Vec::foldRight(v1, 1, [&](int x, int y) { return (x + 1) * y; });
  assert(n == 2*3*4);

  // flatMap
  auto v7 = Vec::flatMap<int, int>(v1, [](int x) { return immer::flex_vector<int>{x, x}; });
  print_vec(v7);

  auto v8 = Vec::reverse(v7);

  print_vec(v8); std::cout << "\n";

  auto v9 = Vec::zip(v7, v8);
  //print_vec(v9);

  return 0;
}
