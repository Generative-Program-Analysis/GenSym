#ifndef GS_PTREE_HEADERS
#define GS_PTREE_HEADERS
class PTreeNode;
using PTreeNodePtr = PTreeNode *;

class PTreeNode {
public:
  PTreeNodePtr parent = nullptr;

  PTreeNodePtr left;
  PTreeNodePtr right;
  bool has_task;
  uint64_t ssid;
  TaskFun task;

  //PTreeNode(const PTreeNode&) = delete;
  PTreeNode(PTreeNode *parent, uint64_t ssid) : parent{parent}, ssid{ssid} {
    left = nullptr;
    right = nullptr;
    has_task = false;
  }

  bool is_leaf() {
    return (!left) && (!right);
  }
};

class PTree {
private:
  std::mutex ptree_lock;
  PTreeNodePtr root;
  std::map<uint64_t, PTreeNodePtr> ptreemap;
  void set_task_flag(PTreeNodePtr curr_node) {
    assert(curr_node && !curr_node->has_task);
    while(curr_node && !curr_node->has_task) {
      curr_node->has_task = true;
      curr_node = curr_node->parent;
    }
  }
  void free_task_flag(PTreeNodePtr curr_node) {
    assert(curr_node);
    while(curr_node) {
      assert(curr_node->has_task);
      curr_node->has_task = false;
      if (!curr_node->parent) break;
      PTreeNodePtr other_child = curr_node->parent->left == curr_node ? curr_node->parent->right : curr_node->parent->left;
      if (other_child && other_child->has_task) break;
      curr_node = curr_node->parent;
    }
  }
public:
  PTree(uint64_t initial_ss): root(new PTreeNode(nullptr, initial_ss)) {
    ptreemap.insert({initial_ss, root});
  }
  ~PTree() = default;

  uint64_t fork(uint64_t ssid) {
    const std::scoped_lock lock(ptree_lock);
    auto it = ptreemap.find(ssid);
    assert(it != ptreemap.end());
    PTreeNodePtr old_ptr = it->second;
    //assert(!old_ptr->has_task);
    assert(!old_ptr->left);
    assert(!old_ptr->right);
    PTreeNodePtr new_ptr = new PTreeNode(old_ptr, ssid);
    it->second = new_ptr;
    PTreeNodePtr forked_ptr = new PTreeNode(old_ptr, cov().new_ssid());
    ptreemap.insert({forked_ptr->ssid, forked_ptr});
    old_ptr->left = new_ptr;
    old_ptr->right = forked_ptr;
    old_ptr->ssid = 0;
    if (old_ptr->has_task) {
      new_ptr->task = std::move(old_ptr->task);
      new_ptr->has_task = true;
    }
    return forked_ptr->ssid;
  }
  void remove(uint64_t ssid) {
    const std::scoped_lock lock(ptree_lock);
    free_task_flag(ptreemap[ssid]);
  }

  void add_task(uint64_t ssid, const TaskFun& f) {
    const std::scoped_lock lock(ptree_lock);
    auto it = ptreemap.find(ssid);
    assert(it != ptreemap.end());
    PTreeNodePtr curr_ptr = it->second;
    assert(curr_ptr->is_leaf());
    assert(!curr_ptr->has_task);
    curr_ptr->task = f;
    set_task_flag(curr_ptr);
  }

  bool pop_task(TaskFun& task) {
    const std::scoped_lock lock(ptree_lock);
    uint32_t flips=0, bits=0;
    if (!root->has_task) return false;
    PTreeNodePtr curr = root;
    unsigned seed1 = std::chrono::system_clock::now().time_since_epoch().count();
    while (!curr->is_leaf()) {
      assert(curr->has_task);
      if (!curr->left || !curr->left->has_task)  {
        curr = curr->right;
      } else if (!curr->right || !curr->right->has_task) {
        curr = curr->left;
      } else {
        if (bits==0) {
          flips = rand_uint32();
          bits = 32;
        }
        --bits;
        curr = ((flips & (1U << bits)) ? curr->left : curr->right);
      }
      assert(curr && curr->has_task);
    }
    task = std::move(curr->task);
    free_task_flag(curr);
    return true;
  }

  void print_ptree() {
    std::vector<PTreeNodePtr> vv1;
    std::vector<PTreeNodePtr> vv2;
    vv1.push_back(root);
    while (vv1.size() > 0) {
      for (auto& n: vv1) {
        std::cout << " " << n->ssid;
        if (n->left) vv2.push_back(n->left);
        if (n->right) vv2.push_back(n->right);
      }
      std::cout << "\n";
      vv1.swap(vv2);
      vv2.clear();
    }
  }
};

inline PTree ptree(1);

inline void ptree_add_task(uint64_t ssid, const TaskFun& f) {
  return ptree.add_task(ssid, f);
}

inline bool ptree_pop_task(TaskFun& task) {
  return ptree.pop_task(task);
}

inline uint64_t ss_fork(uint64_t ssid) {
  if (SearcherKind::randomPath == searcher_kind)
    return ptree.fork(ssid);
  else
    return cov().new_ssid();
}
#endif