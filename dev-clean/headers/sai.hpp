#include <tuple>
#include <cassert>
#include <cstdarg>
#include <cstdlib>
#include <ctime>
#include <iostream>
#include <sstream>
#include <functional>
#include <variant>
#include <immer/flex_vector.hpp>
#include <immer/map.hpp>
#include <immer/set.hpp>
#include <immer/algorithm.hpp>

/* TODO: 
 *   pass by reference: contains, notContains
 *   pass const argument?
 */

#ifndef SAI_HEADERS
#define SAI_HEADERS

// #define DEBUG

// Auxiliary definitions

using String = std::string;

inline void init_rand() {
  srand((unsigned) time(0));
}

inline int rand_int(int ub) {
  int r =  (rand() % ub) + 1;
#ifdef DEBUG
  std::cout << "rand number: " << r << std::endl;
#endif
  return r;
}

template <typename T>
using Ptr = std::shared_ptr<T>;

#   define ABORT(message) \
    do { \
      std::cerr << "Abort at " << __FILE__ << " line " << __LINE__ \
                << ": " << message << std::endl; \
      exit(-1); \
    } while (false)


#ifndef DEBUG
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

template<class... Ts> struct overloaded : Ts... { using Ts::operator()...; };
template<class... Ts> overloaded(Ts...) -> overloaded<Ts...>;

template<typename T>
void print_vec(immer::flex_vector<T>& v) {
  std::cout << "[";
  for (int i = 0; i < v.size(); i++) {
    std::cout << v.at(i);
    if (i != v.size()-1) std::cout << ", ";
  }
  std::cout << "]";
}

template<typename T>
void print_set(immer::set<T>& s) {
  std::cout << "{";
  int i = 0;
  for (auto x : s) {
    std::cout << x;
    if (i != s.size()-1) std::cout << ", ";
    i = i + 1;
  }
  std::cout << "}";
}

template<typename T, class... Types>
inline bool isInstanceOf(const std::variant<Types...>& v) {
  return std::holds_alternative<T>(v);
}

/* Vectors */

namespace Vec {

  // Iterative implementation of map
  template<typename U, typename T, typename Fn>
  inline immer::flex_vector<U> vmap(immer::flex_vector<T> vec, Fn f) {
    static_assert(std::is_convertible<Fn, std::function<U(T)>>::value,
      "Vec::map requires a function of type U(T)");
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
      "Vec::map_rec requires a function of type U(T)");
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
      "Vec::filter requires a function of type bool(T)");
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
      "Vec::filter_rec requires a function of type bool(T)");
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
      "Vec::foldLeft requires a function of type U(U, T)");
    for (int i = 0; i < vec.size(); i++) {
      acc = f(acc, vec.at(i));
    }
    return acc;
  }

  // Recursive implementation of foldLeft
  template<typename T, typename U, typename Fn>
  U foldLeft_rec(immer::flex_vector<T> vec, U acc, Fn f) {
    static_assert(std::is_convertible<Fn, std::function<U(U, T)>>::value,
      "Vec::foldLeft_rec requires a function of type U(U, T)");
    if (vec.size() == 0)
      return acc;
    else
      return foldLeft_rec(vec.drop(1), f(acc, vec.front()), f);
  }

  // Iterative implementation of foldRight
  template<typename T, typename U, typename Fn>
  inline U foldRight(immer::flex_vector<T> vec, U acc, Fn f) {
    static_assert(std::is_convertible<Fn, std::function<U(T, U)>>::value,
      "Vec::foldLeft requires a function of type U(T, U)");
    for (int i = vec.size()-1; i >= 0; i--) {
      acc = f(vec.at(i), acc);
    }
    return acc;
  }

  // Recursive implementation of foldRight
  template<typename T, typename U, typename Fn>
  U foldRight_rec(immer::flex_vector<T> vec, U acc, Fn f) {
    static_assert(std::is_convertible<Fn, std::function<U(T, U)>>::value,
      "Vec::foldRight_rec requires a function of type U(T, U)");
    if (vec.size() == 0) return acc;
    else return f(vec.front(), foldRight_rec(vec.drop(1), acc, f));
  }

  template<typename U, typename T, typename Fn>
  inline auto flatMap(immer::flex_vector<T> vec, Fn f) {
    static_assert(std::is_convertible<Fn, std::function<immer::flex_vector<U>(T)>>::value,
      "Vec::flatMap requires a function of type flex_vector<U>(T)");
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

  template<typename T, typename Fn>
  inline void foreach(immer::flex_vector<T> vec, Fn f) {
    // static_assert(std::is_convertible<Fn, std::function<U(T)>>::value,
    //   "Vec::map requires a function of type U(T)");
    // auto res = immer::flex_vector<U>();
    for (int i = 0; i < vec.size(); i++) {
      f(vec.at(i));
    }
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
    for (auto kv : m2) { m1 = m1.insert(kv); }
    return m1;
  }

  template<typename K, typename V, typename P>
  inline immer::map<K, V> filter(immer::map<K, V> m, P f) {
    static_assert(std::is_convertible<P, std::function<bool(std::pair<K, V>)>>::value,
      "Map::filter requires a function of type bool(std::pari<K, V>)");
    auto res = immer::map<K, V>{};
    for (auto kv : m) {
      if (f(kv)) res = res.insert(kv);
    }
    return res;
  }

  template<typename T, typename K, typename V, typename Fn>
  inline immer::flex_vector<T> map(immer::map<K, V> m, Fn f) {
    static_assert(std::is_convertible<Fn, std::function<T(K, V)>>::value,
      "Map::map requires a function of type T(K, V)");
    auto res = immer::flex_vector<T>();
    for (auto kv : m) {
      res = res.push_back(f(std::get<0>(kv), std::get<1>(kv)));
    }
    return res;
  }

  template<typename K, typename V, typename Fn>
  inline immer::map<K, V> map2map(immer::map<K, V> m, Fn f) {
    static_assert(std::is_convertible<Fn, std::function<std::pair<K, V>(K, V)>>::value,
      "Map::map2map requires a function of type std::pair<K, V>(K, V)");
    auto res = immer::map<K, V>{};
    for (auto kv : m) {
      res = res.insert(f(std::get<0>(kv), std::get<1>(kv)));
    }
    return res;
  }

  template<typename K, typename V, typename T, typename Fn>
  inline T foldLeft(immer::map<K, V> m, T init, Fn f) {
    static_assert(std::is_convertible<Fn, std::function<T(T, std::pair<K, V>)>>::value,
      "Map::foldLeft requires a function of type U(U, std::pair<K, V>)");
    auto res = init;
    for (auto kv : m) {
      res = f(res, kv);
    }
    return res;
  }
}

namespace Set {

  template<typename T>
  inline immer::set<T> make_set(std::initializer_list<T> xs) {
    auto set0 = immer::set<T>();
    for (auto&& x : xs) { set0 = set0.insert(x); }
    return set0;
  }
  
  template<typename T>
  inline bool contains(immer::set<T> s, T t) {
    return s.count(t) == 1;
  }

  template<typename T>
  inline bool notContains(immer::set<T> s, T t) {
    return s.count(t) == 0;
  }

  template<typename T>
  inline immer::flex_vector<T> toList(immer::set<T> s) {
    auto res = immer::flex_vector<T>();
    for (auto x : s) res = res.push_back(x);
    return res;
  }
  
  template<typename T>
  inline immer::set<T> join(immer::set<T> s1, immer::set<T> s2) {
    for (auto x : s2) {
      s1 = s1.insert(x);
    }
    return s1;
  }

  template<typename T>
  inline immer::set<T> intersect(immer::set<T> s1, immer::set<T> s2) {
    auto res = immer::set<T>();
    for (auto x : s2) {
      if (contains(s1, x)) res = res.insert(x);
    }
    return res;
  }

  template<typename T>
  inline immer::set<T> subsetOf(immer::set<T> s1, immer::set<T> s2) {
    for (auto x : s1) {
      if (!contains(s2, x)) return false;
    }
    return true;
  }

  template<typename S, typename T, typename Fn>
  inline immer::set<S> map(immer::set<T> s, Fn f) {
    static_assert(std::is_convertible<Fn, std::function<S(T)>>::value,
      "Set::map requires a function of type S(T)");
    auto res = immer::set<S>();
    for (auto x : s) { res = res.insert(f(x)); }
    return res;
  }

  template<typename T, typename Fn>
  inline void foreach(immer::set<T> s, Fn f) {
    for (auto x : s) { f(x); }
  }

  template<typename S, typename T, typename Fn>
  inline S foldLeft(immer::set<T> s, S acc, Fn f) {
    static_assert(std::is_convertible<Fn, std::function<S(S, T)>>::value,
      "Set::foldLeft requires a function of type S(S, T)");
    for (auto x : s) { acc = f(acc, x); }
    return acc;
  }

  template<typename T, typename P>
  inline immer::set<T> filter(immer::set<T> s, P p) {
    static_assert(std::is_convertible<P, std::function<bool(T)>>::value,
      "Set::filter requires a function of type bool(T)");
    auto res = immer::set<T>();
    for (auto x : s) {
      if (p(x)) res = res.insert(x);
    }
    return res;
  }
}

#endif
