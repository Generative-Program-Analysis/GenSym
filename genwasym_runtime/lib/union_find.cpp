#include "wasm/union_find.hpp"

UnionFind::UnionFind() = default;

int UnionFind::find(int x) const {
  auto parent_opt = parent.find(x);
  if (!parent_opt) {
    return x;
  }
  if (*parent_opt == x) {
    return x;
  }
  return find(*parent_opt);
}

void UnionFind::unite(int x, int y) {
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

bool UnionFind::connected(int x, int y) const {
  return find(x) == find(y);
}

void UnionFind::clear() {
  parent = immer::map_transient<int, int>();
  rank = immer::map_transient<int, int>();
}