#include <iostream>
#include <functional>
#include <immer/flex_vector.hpp>
#include <immer/algorithm.hpp>

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

//Credit: https://bartoszmilewski.com/2013/11/13/functional-data-structures-in-c-lists/

template<typename U, typename T, typename Fn>
auto map(immer::flex_vector<T> vec, Fn f) {
  static_assert(std::is_convertible<Fn, std::function<U(T)>>::value,
		"map requires a function of type U(T)");
  if (vec.size() == 0) {
    return immer::flex_vector<U>();
  } else {
    auto head = f(vec.front());
    auto tail = map<U>(vec.drop(1), f);
    return tail.push_front(head);
  }
}

template<typename T, typename P>
auto filter(immer::flex_vector<T> vec, P p) {
  static_assert(std::is_convertible<P, std::function<bool(T)>>::value,
		"filter requires a function of type bool(T)");
  if (vec.size == 0) return immer::flex_vector<T>();
  if (p(vec.front())) {
    auto head = vec.front();
    auto tail = filter(vec.drop(1), p);
    return tail.push_front(head);
  }
  else {
    return filter(vec.drop(1), p);
  }
}

template<typename T, typename U, typename Fn>
U foldLeft(immer::flex_vector<T> vec, U acc, Fn f) {
  static_assert(std::is_convertible<Fn, std::function<U(U, T)>>::value,
		"foldLeft requires a function of type U(U, T)");
  if (vec.size() == 0)
    return acc;
  else
    return foldLeft(vec.drop(1), f(acc, vec.front()), f);
}

template<typename T, typename U, typename Fn>
U foldRight(immer::flex_vector<T> vec, U acc, Fn f) {
  static_assert(std::is_convertible<Fn, std::function<U(T, U)>>::value,
		"foldLeft requires a function of type U(T, U)");
  if (vec.size() == 0)
    return acc;
  else
    return f(vec.front(), foldRight(vec.drop(1), acc, f));
}

int main(int argc, char** argv) {
  auto v1 = immer::flex_vector<int> {1, 2, 3};
  auto v2 = immer::accumulate(v1, 0, [&](int x, int y)->int { return x + y; });
  auto v3 = immer::accumulate(v1, 1, [&](int x, int y) { return x * y; });

  // map
  auto mt = immer::flex_vector<int>();
  auto f = [](const immer::flex_vector<int> v, const int x)->immer::flex_vector<int> { return v.push_back(x+1); };
  auto v4 = immer::accumulate(v1.begin(), v1.end(), mt, f);

  // another way to do map
  auto v5 = immer::accumulate(v1.begin(), v1.end(),
			      v1.take(0),
			      [](auto v, auto x) {
				return v.push_back(x+1);
			      });
  assert(v4 == v5); 

  // still map
  auto v6 = map<int>(v1, [](auto x) { return x + 1; } );

  for (int i = 0; i < v6.size(); i++) {
    std::cout << v6.at(i) << "\n";
  }

  assert(v5 == v6);

  // foldLeft and foldRight

  auto n = foldLeft(v1, 0, [&](int x, int y) { return x + y; });
  assert(n == 6);
  n = foldLeft(v1, 1, [&](int x, int y) { return x * (y + 1); });
  assert(n == 2*3*4);

  n = foldRight(v1, 0, [&](int x , int y) { return x + y; });
  assert(n == 6);
  n = foldRight(v1, 1, [&](int x, int y) { return (x + 1) * y; });
  assert(n == 2*3*4);

  // flatMap
  //auto v7 = flatMap(v1, [](auto x) { return immer::flex_vector<int> {x}; });

  return 0;
}
