#ifndef WASM_UNION_FIND_HPP
#define WASM_UNION_FIND_HPP

#include "config.hpp"
#include <memory>
#include <optional>

// TODO: merge this file with headers/gensym/unionfind.hpp with a general implementation in a new PR
struct UnionFindRepr;

class UnionFind {
private:
  std::unique_ptr<UnionFindRepr> repr_;

public:
  UnionFind();
  ~UnionFind();
  UnionFind(const UnionFind &other);
  UnionFind &operator=(const UnionFind &other);
  UnionFind(UnionFind &&other) noexcept;
  UnionFind &operator=(UnionFind &&other) noexcept;

  int find(int x) const;
  void unite(int x, int y);
  bool connected(int x, int y) const;
  void clear();
};

#endif // WASM_UNION_FIND_HPP
