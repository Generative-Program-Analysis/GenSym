#include "wasm/union_find.hpp"
#include "runtime_repr.hpp"

UnionFind::UnionFind() : repr_(std::make_unique<UnionFindRepr>()) {}
UnionFind::~UnionFind() = default;
UnionFind::UnionFind(UnionFind &&other) noexcept = default;
UnionFind &UnionFind::operator=(UnionFind &&other) noexcept = default;

UnionFind::UnionFind(const UnionFind &other)
    : repr_(other.repr_ ? std::make_unique<UnionFindRepr>(*other.repr_)
                       : std::make_unique<UnionFindRepr>()) {}

UnionFind &UnionFind::operator=(const UnionFind &other) {
  if (this != &other) *this = UnionFind(other);
  return *this;
}

int UnionFind::find(int x) const {
  if (!repr_) return x;
  auto parent_opt = repr_->parent.find(x);
  if (!parent_opt) {
    return x;
  }
  if (*parent_opt == x) {
    return x;
  }
  return find(*parent_opt);
}

void UnionFind::unite(int x, int y) {
  if (!repr_) repr_ = std::make_unique<UnionFindRepr>();
  int root_x = find(x);
  int root_y = find(y);

  if (root_x == root_y) {
    return;
  }

  auto rank_x_ptr = repr_->rank.find(root_x);
  auto rank_y_ptr = repr_->rank.find(root_y);
  int rank_x = rank_x_ptr ? *rank_x_ptr : 0;
  int rank_y = rank_y_ptr ? *rank_y_ptr : 0;

  if (rank_x < rank_y) {
    repr_->parent.set(root_x, root_y);
  } else if (rank_x > rank_y) {
    repr_->parent.set(root_y, root_x);
  } else {
    repr_->parent.set(root_y, root_x);
    repr_->rank.set(root_x, rank_x + 1);
  }
}

bool UnionFind::connected(int x, int y) const {
  return find(x) == find(y);
}

void UnionFind::clear() {
  if (repr_) {
    repr_->parent = {};
    repr_->rank = {};
  }
}
