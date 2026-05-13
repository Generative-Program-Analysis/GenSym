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
  UnionFind() = default;

  int find(int x) const {
    auto parent_opt = parent.find(x);
    if (!parent_opt) {
      return x;
    }
    if (*parent_opt == x) {
      return x;
    }
    return find(*parent_opt);
  }

  void unite(int x, int y) {
    int root_x = find(x);
    int root_y = find(y);

    if (root_x == root_y) {
      return;
    }

    auto rank_x_ptr = rank.find(root_x);
    auto rank_y_ptr = rank.find(root_y);
    int rank_x = rank_x_ptr ? *rank_x_ptr : 0;
    int rank_y = rank_y_ptr ? *rank_y_ptr : 0;

    if (rank_x < rank_y) {
      parent.set(root_x, root_y);
    } else if (rank_x > rank_y) {
      parent.set(root_y, root_x);
    } else {
      parent.set(root_y, root_x);
      rank.set(root_x, rank_x + 1);
    }
  }

  bool connected(int x, int y) const { return find(x) == find(y); }

  void clear() {
    parent = immer::map_transient<int, int>();
    rank = immer::map_transient<int, int>();
  }
};

#endif // WASM_UNION_FIND_HPP