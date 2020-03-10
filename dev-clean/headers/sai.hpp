#include <tuple>
#include <cassert>
#include <cstdarg>
#include <iostream>
#include <sstream>
#include <functional>
#include <immer/flex_vector.hpp>
#include <immer/map.hpp>
#include <immer/algorithm.hpp>

//Credit: https://bartoszmilewski.com/2013/11/13/functional-data-structures-in-c-lists/

#ifndef SAI_HEADERS
#define SAI_HEADERS

#define DEBUG

// Auxiliary definitions

#ifndef NDEBUG
#   define ASSERT(condition, message) \
    do { \
        if (! (condition)) { \
            std::cerr << "Assertion `" #condition "` failed in " << __FILE__ \
                      << " line " << __LINE__ << ": " << message << std::endl; \
            std::terminate(); \
        } \
    } while (false)
#else
#   define ASSERT(condition, message) do { } while (false)
#endif

template<typename T>
void print_vec(immer::flex_vector<T>& v) {
  std::cout << "{ ";
  for (int i = 0; i < v.size(); i++) {
    std::cout << v.at(i);
    if (i != v.size()-1) std::cout << ", ";
  }
  std::cout << " }";
}

/* Vectors */

namespace Vec {

  // Iterative implementation of map
  template<typename U, typename T, typename Fn>
  inline immer::flex_vector<U> vmap(immer::flex_vector<T> vec, Fn f) {
    static_assert(std::is_convertible<Fn, std::function<U(T)>>::value,
      "map requires a function of type U(T)");
    auto res = immer::flex_vector<U>();
    for (int i = 0; i < vec.size(); i++) {
      res = res.push_back(f(vec.at(i)));
    }
    return res;
  }

  // Recursive implementation of map
  template<typename U, typename T, typename Fn>
  immer::flex_vector<U> map_rec(immer::flex_vector<T> vec, Fn f) {
    static_assert(std::is_convertible<Fn, std::function<U(T)>>::value,
      "map_rec requires a function of type U(T)");
    if (vec.size() == 0) {
      return immer::flex_vector<U>();
    } else {
      U head = f(vec.front());
      immer::flex_vector<U> tail = map_rec<U>(vec.drop(1), f);
      return tail.push_front(head);
    }
  }

  // Iterative implementation of filter
  template<typename T, typename P>
  inline auto filter(immer::flex_vector<T> vec, P p) {
    static_assert(std::is_convertible<P, std::function<bool(T)>>::value,
      "filter requires a function of type bool(T)");
    auto res = immer::flex_vector<T>();
    for (int i = 0; i < vec.size(); i++) {
      auto e = vec.at(i);
      if (p(e)) res = res.push_back(e);
    }
    return res;
  }

  // Recursive implementation of filter
  template<typename T, typename P>
  auto filter_rec(immer::flex_vector<T> vec, P p) {
    static_assert(std::is_convertible<P, std::function<bool(T)>>::value,
      "filter_rec requires a function of type bool(T)");
    if (vec.size == 0) return immer::flex_vector<T>();
    if (p(vec.front())) {
      auto head = vec.front();
      auto tail = filter_rec(vec.drop(1), p);
      return tail.push_front(head);
    }
    else {
      return filter_rec(vec.drop(1), p);
    }
  }

  // Iterative implementation of foldLeft
  template<typename T, typename U, typename Fn>
  inline U foldLeft(immer::flex_vector<T> vec, U acc, Fn f) {
    static_assert(std::is_convertible<Fn, std::function<U(U, T)>>::value,
      "foldLeft requires a function of type U(U, T)");
    U res = acc;
    for (int i = 0; i < vec.size(); i++) {
      res = f(res, vec.at(i));
    }
    return res;
  }

  // Recursive implementation of foldLeft
  template<typename T, typename U, typename Fn>
  U foldLeft_rec(immer::flex_vector<T> vec, U acc, Fn f) {
    static_assert(std::is_convertible<Fn, std::function<U(U, T)>>::value,
      "foldLeft_rec requires a function of type U(U, T)");
    if (vec.size() == 0)
      return acc;
    else
      return foldLeft_rec(vec.drop(1), f(acc, vec.front()), f);
  }

  // Iterative implementation of foldRight
  template<typename T, typename U, typename Fn>
  inline U foldRight(immer::flex_vector<T> vec, U acc, Fn f) {
    static_assert(std::is_convertible<Fn, std::function<U(T, U)>>::value,
      "foldLeft requires a function of type U(T, U)");
    U res = acc;
    for (int i = vec.size()-1; i >= 0; i--) {
      res = f(vec.at(i), res);
    }
    return res;
  }

  // Recursive implementation of foldRight
  template<typename T, typename U, typename Fn>
  U foldRight_rec(immer::flex_vector<T> vec, U acc, Fn f) {
    static_assert(std::is_convertible<Fn, std::function<U(T, U)>>::value,
      "foldRight_rec requires a function of type U(T, U)");
    if (vec.size() == 0) return acc;
    else return f(vec.front(), foldRight_rec(vec.drop(1), acc, f));
  }

  template<typename T, typename U, typename Fn>
  inline auto flatMap(immer::flex_vector<T> vec, Fn f) {
    static_assert(std::is_convertible<Fn, std::function<immer::flex_vector<U>(T)>>::value,
                  "flatMap requires a function of type flex_vector<U>(T)");
    auto v1 = vmap<immer::flex_vector<U>>(vec, f);
    auto res = immer::flex_vector<U>();
    for (int i = 0; i < v1.size(); i++) res = res + v1.at(i);
    return res;
  }

  template<typename T>
  inline auto reverse(immer::flex_vector<T> vec) {
    return foldLeft(vec, immer::flex_vector<T>(), [](auto acc, auto x) { return acc.push_front(x); });
  }

  template<typename T, typename U>
  inline immer::flex_vector<std::tuple<T, U>> zip(immer::flex_vector<T> v1, immer::flex_vector<U> v2) {
    ASSERT(v1.size() == v2.size(), "Vectors must have same size");
    auto res = immer::flex_vector<std::tuple<T, U>>();
    for (int i = 0; i < v1.size(); i++) {
      res = res.push_back(std::make_tuple(v1.at(i), v2.at(i)));
    }
    return res;
  }
}

namespace Tuple {

  template<typename T, typename U>
  inline std::tuple<U, T> swap(std::tuple<T, U> t) {
    return std::make_tuple(std::get<1>(t), std::get<0>(t));
  }
  
  template<typename T, typename U>
  inline std::pair<T, U> to_pair(std::tuple<T, U> t) {
    return std::make_pair(std::get<0>(t), std::get<1>(t));
  }
}

namespace Pair {
  
  template<typename T, typename U>
  inline std::tuple<T, U> to_tuple(std::pair<T, U> t) {
    return std::make_tuple(std::get<0>(t), std::get<1>(t));
  }

  template<typename T, typename U>
  inline std::pair<U, T> swap(std::pair<T, U> t) {
    return std::make_pair(std::get<1>(t), std::get<0>(t));
  }
}

/* Maps */

namespace Map {

  template<typename K, typename V>
  inline immer::map<K, V> make_map(std::initializer_list<std::pair<K, V>> kvs) {
    auto map0 = immer::map<K, V>{};
    for (auto&& kv : kvs) { map0 = map0.insert(kv); }
    return map0;
  }

  template<typename K, typename V>
  inline immer::map<K, V> make_map_from_tuples(std::initializer_list<std::tuple<K, V>> kvs) {
    auto map0 = immer::map<K, V>{};
    for (auto&& kv : kvs) { map0 = map0.insert(Tuple::to_pair(kv)); }
    return map0;
  }

  template<typename K, typename V>
  inline bool contains(immer::map<K, V> m, K k) {
    return m.count(k) == 1;
  }

  template<typename K, typename V>
  inline bool notContains(immer::map<K, V> m, K k) {
    return m.count(k) == 0;
  }

  template<typename K, typename V>
  inline auto safe_at(immer::map<K, V> m, K k) {
    ASSERT(contains(m, k), "The map does not contain key");
    return m.at(k);
  }

  template<typename K, typename V>
  inline auto getOrElse(immer::map<K, V> m, K k, V v) {
    if (m.count(k) == 0) return v;
    else return m[k];
  }
  
  template<typename K, typename V>
  inline immer::map<K, V> concat(immer::map<K, V> m1, immer::map<K, V> m2) {
    auto res = m1;
    for (auto kv : m2) { res = res.insert(kv); }
    return res;
  }

  template<typename K, typename V, typename P>
  inline immer::map<K, V> filter(immer::map<K, V> m, P f) {
    static_assert(std::is_convertible<P, std::function<bool(std::pair<K, V>)>>::value,
      "filter requires a function of type bool(std::pari<K, V>)");
    auto res = immer::map<K, V>{};
    for (auto kv : m) {
      if (f(kv)) res = res.insert(kv);
    }
    return res;
  }

  template<typename K, typename V, typename T, typename Fn>
  inline immer::flex_vector<T> map(immer::map<K, V> m, Fn f) {
    static_assert(std::is_convertible<Fn, std::function<T(K, V)>>::value,
      "map requires a function of type T(K, V)");
    auto res = immer::flex_vector<T>();
    for (auto kv : m) {
      res = res.push_back(f(std::get<0>(kv), std::get<1>(kv)));
    }
    return res;
  }

  template<typename K, typename V, typename Fn>
  inline immer::map<K, V> map2map(immer::map<K, V> m, Fn f) {
    static_assert(std::is_convertible<Fn, std::function<std::pair<K, V>(K, V)>>::value,
      "map2map requires a function of type std::pair<K, V>(K, V)");
    auto res = immer::map<K, V>{};
    for (auto kv : m) {
      res = res.insert(f(std::get<0>(kv), std::get<1>(kv)));
    }
    return res;
  }

  template<typename K, typename V, typename T, typename Fn>
  inline T foldLeft(immer::map<K, V> m, T init, Fn f) {
    static_assert(std::is_convertible<Fn, std::function<T(T, std::pair<K, V>)>>::value,
      "foldLeft requires a function of type U(U, std::pair<K, V>)");
    auto res = init;
    for (auto kv : m) {
      res = f(res, kv);
    }
    return res;
  }
}

#endif
