#include <tuple>
#include <iostream>
#include <cstdarg>
#include <functional>
#include <immer/map.hpp>
#include <immer/algorithm.hpp>
#include <sai.hpp>

int main(int argc, char** argv) {
  const auto v0 = immer::map<std::string, int>{};
  assert(v0["hello"] == 0);
  assert(Map::notContains<std::string>(v0, "hello"));

  const auto v1 = v0.set("hello", 42);
  assert(v1["hello"] == 42);
  assert(Map::contains<std::string>(v1, "hello"));
  assert(*v1.find("hello") == 42);

  const auto v2 = v1.erase("hello");
  assert(!v2.find("hello"));
  assert(Map::notContains<std::string>(v2, "hello"));
  //safe_at<std::string, int>(v2, "hello");

  auto v3 = v2.insert({"hello", 42});
  assert(v3["hello"] == 42);

  std::pair<std::string, int> t = {"world", 2};
  auto v4 = v3.insert(t);
  assert(v4["world"] == 2);

  assert((Map::getOrElse<std::string, int>(v4, "what", 10) == 10));

  auto v5 = Map::make_map<int, int>({ std::make_pair<int, int>(1, 2), {3, 4} });
  assert(v5[1] == 2);
  assert(v5[3] == 4);
  
  auto v6 = Map::make_map_from_tuples<int, int>({ {5, 6}, {7, 8} });
  assert(v6[5] == 6);
  assert(v6[7] == 8);

  auto v7 = Map::concat(v5, v6);
  assert(v7.size() == 4);
  assert(v7[1] == 2);
  assert(v7[3] == 4);
  assert(v7[5] == 6);
  assert(v7[7] == 8);
  
  v7 = Map::filter(v7, [](auto kv) { return std::get<0>(kv) >= 5; });
  assert(v7.size() == 2);
  
  // note: if there is no explicit return type annotation, we have to write make_pair.
  //v7 = Map::map2map(v7, [](auto kv) { return std::make_pair(std::get<0>(kv)+1, std::get<1>(kv)+1); });
  v7 = Map::map2map(v7, [](auto kv)->std::pair<int, int> { 
    return { std::get<0>(kv)+1, std::get<1>(kv)+1 }; 
  });
  assert(v7[6] == 7);
  assert(v7[8] == 9);
}
