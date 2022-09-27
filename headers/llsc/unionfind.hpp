#ifndef LLSC_UNIONFIND_HEADER
#define LLSC_UNIONFIND_HEADER

struct UnionFind {
  immer::map_transient<PtrVal, PtrVal> parent;
  immer::map_transient<PtrVal, PtrVal> next;
  immer::map_transient<PtrVal, std::uint32_t> size;

  bool init(PtrVal v) {
    if (parent.find(v) == nullptr) {
      parent.set(v, v);
      next.set(v, v);
      return false;
    }
    return true;
  }

  PtrVal find(PtrVal v) {
    if (!init(v)) return v;
    PtrVal root = v;
    while (root != parent[root])
      root = parent[root];
    while (v != root) {
      auto newv = parent[v];
      parent.set(v, root);
      v = newv;
    }
    return root;
  }

  void print_set(PtrVal v) {
    auto root = v;
    std::cout << *v << ", ";
    while (root != next[v]) {
      v = next[v];
      std::cout << *v << ", ";
    }
    std::cout << "\n";
  }

  void join(PtrVal p, PtrVal q) {
    auto root_p = find(p);
    auto root_q = find(q);
    if (root_p == root_q) return;
    auto tmp = next[root_p];
    next.set(root_p, next[root_q]);
    next.set(root_q, tmp);
    if (size[root_p] < size[root_q]) {
      parent.set(root_p, root_q);
      size.update(root_q, [&](auto s) { return s + size[root_p]; });
    } else {
      parent.set(root_q, root_p);
      size.update(root_p, [&](auto s) { return s + size[root_q]; });
    }
  }
};

#endif