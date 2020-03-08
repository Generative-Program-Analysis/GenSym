#include <tuple>
#include <iostream>
#include <functional>
#include <immer/map.hpp>
#include <immer/algorithm.hpp>
#include <sai.hpp>

int main(int argc, char** argv) {
  const auto v0 = immer::map<std::string, int>{};
  assert(v0["hello"] == 0);
  assert(notContains<std::string>(v0, "hello"));

  const auto v1 = v0.set("hello", 42);
  assert(v1["hello"] == 42);
  assert(contains<std::string>(v1, "hello"));
  assert(*v1.find("hello") == 42);

  const auto v2 = v1.erase("hello");
  assert(!v2.find("hello"));
  assert(notContains<std::string>(v2, "hello"));
  //safe_at<std::string, int>(v2, "hello");

  auto v3 = v2.insert({"hello", 42});
  assert(v3["hello"] == 42);

  //const auto v3 = immer::map<int, int>{ {1, 2}, {3, 4} };
  std::pair<std::string, int> t = {"world", 2};
  auto v4 = v3.insert(t);
  assert(v4["world"] == 2);
}
