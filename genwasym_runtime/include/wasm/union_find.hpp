#ifndef WASM_UNION_FIND_HPP
#define WASM_UNION_FIND_HPP

#include "config.hpp"
#include "immer/map.hpp"
#include <immer/map_transient.hpp>
#include <immer/vector.hpp>
#include <optional>

// TODO: merge this file with headers/gensym/unionfind.hpp with a general implementation in a new PR
class UnionFind {
private:
  immer::map_transient<int, int> parent;
  immer::map_transient<int, int> rank;

public:
  UnionFind();

  int find(int x) const;
  void unite(int x, int y);
  bool connected(int x, int y) const;
  void clear();
};

#endif // WASM_UNION_FIND_HPP