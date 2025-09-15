#ifndef WASM_SYMBOLIC_RT_HPP
#define WASM_SYMBOLIC_RT_HPP

#include "concrete_rt.hpp"
#include "controls.hpp"
#include "utils.hpp"
#include <cassert>
#include <cstddef>
#include <cstdio>
#include <fstream>
#include <iterator>
#include <memory>
#include <optional>
#include <ostream>
#include <set>
#include <string>
#include <variant>
#include <vector>

class Symbolic {
public:
  Symbolic() {}                  // TODO: remove this default constructor later
  virtual ~Symbolic() = default; // Make Symbolic polymorphic
};

static int max_id = 0;

#ifdef NO_REUSE
static bool REUSE_MODE = false;
#else
static bool REUSE_MODE = true;
#endif

class Symbol : public Symbolic {
public:
  // TODO: add type information to determine the size of bitvector
  // for now we just assume that only i32 will be used
  Symbol(int id) : id(id) { max_id = std::max(max_id, id); }
  int get_id() const { return id; }

private:
  int id;
};

class SymConcrete : public Symbolic {
public:
  Num value;
  SymConcrete(Num num) : value(num) {}
};

struct SymBinary;

struct SymVal {
  std::shared_ptr<Symbolic> symptr;

  SymVal() : symptr(nullptr) {}
  SymVal(std::shared_ptr<Symbolic> symptr) : symptr(symptr) {}

  // data structure operations
  SymVal makeSymbolic() const;

  // arithmetic operations
  SymVal is_zero() const;
  SymVal add(const SymVal &other) const;
  SymVal minus(const SymVal &other) const;
  SymVal mul(const SymVal &other) const;
  SymVal div(const SymVal &other) const;
  SymVal eq(const SymVal &other) const;
  SymVal neq(const SymVal &other) const;
  SymVal lt(const SymVal &other) const;
  SymVal leq(const SymVal &other) const;
  SymVal gt(const SymVal &other) const;
  SymVal geq(const SymVal &other) const;
  SymVal negate() const;

  bool is_concrete() const;
};

static SymVal make_symbolic(int index) {
  return SymVal(std::make_shared<Symbol>(index));
}

inline SymVal Concrete(Num num) {
  return SymVal(std::make_shared<SymConcrete>(num));
}

enum Operation { ADD, SUB, MUL, DIV, EQ, NEQ, LT, LEQ, GT, GEQ };

struct SymBinary : Symbolic {
  Operation op;
  SymVal lhs;
  SymVal rhs;

  SymBinary(Operation op, SymVal lhs, SymVal rhs)
      : op(op), lhs(lhs), rhs(rhs) {}
};

inline SymVal SymVal::add(const SymVal &other) const {
  return SymVal(std::make_shared<SymBinary>(ADD, *this, other));
}

inline SymVal SymVal::minus(const SymVal &other) const {
  return SymVal(std::make_shared<SymBinary>(SUB, *this, other));
}

inline SymVal SymVal::mul(const SymVal &other) const {
  return SymVal(std::make_shared<SymBinary>(MUL, *this, other));
}

inline SymVal SymVal::div(const SymVal &other) const {
  return SymVal(std::make_shared<SymBinary>(DIV, *this, other));
}

inline SymVal SymVal::eq(const SymVal &other) const {
  return SymVal(std::make_shared<SymBinary>(EQ, *this, other));
}

inline SymVal SymVal::neq(const SymVal &other) const {
  return SymVal(std::make_shared<SymBinary>(NEQ, *this, other));
}
inline SymVal SymVal::lt(const SymVal &other) const {
  return SymVal(std::make_shared<SymBinary>(LT, *this, other));
}
inline SymVal SymVal::leq(const SymVal &other) const {
  return SymVal(std::make_shared<SymBinary>(LEQ, *this, other));
}
inline SymVal SymVal::gt(const SymVal &other) const {
  return SymVal(std::make_shared<SymBinary>(GT, *this, other));
}
inline SymVal SymVal::geq(const SymVal &other) const {
  return SymVal(std::make_shared<SymBinary>(GEQ, *this, other));
}
inline SymVal SymVal::is_zero() const {
  return SymVal(std::make_shared<SymBinary>(EQ, *this, Concrete(I32V(0))));
}
inline SymVal SymVal::negate() const {
  return SymVal(std::make_shared<SymBinary>(EQ, *this, Concrete(I32V(0))));
}

inline SymVal SymVal::makeSymbolic() const {
  auto concrete = dynamic_cast<SymConcrete *>(symptr.get());
  if (concrete) {
    // If the symbolic value is a concrete value, use it to create a symbol
    return SymVal(std::make_shared<Symbol>(concrete->value.toInt()));
  } else {
    throw std::runtime_error(
        "Cannot make symbolic a non-concrete symbolic value");
  }
}

inline bool SymVal::is_concrete() const {
  return dynamic_cast<SymConcrete *>(symptr.get()) != nullptr;
}

class Snapshot_t;

class SymStack_t {
public:
  void push(SymVal val) {
    // Push a symbolic value to the stack
    stack.push_back(val);
  }

  SymVal pop() {
    // Pop a symbolic value from the stack

#ifdef DEBUG
    printf("[Debug] poping from stack, size of symbolic stack is: %zu\n",
           stack.size());
#endif
    auto ret = stack.back();
    stack.pop_back();
    return ret;
  }

  SymVal peek() { return stack.back(); }

  std::monostate shift(int32_t offset, int32_t size) {
    auto n = stack.size();
    for (size_t i = n - size; i < n; ++i) {
      stack[i - offset] = stack[i];
    }
    stack.resize(n - offset);
    return std::monostate();
  }

  void reset() {
    // Reset the symbolic stack
    stack.clear();
  }

  void reuse(Snapshot_t snapshot);

  size_t size() const { return stack.size(); }

  SymVal operator[](size_t index) const { return stack[index]; }

private:
  std::vector<SymVal> stack;
};

static SymStack_t SymStack;

class SymFrames_t {
public:
  void pushFrame(int size) {
    // Push a new frame with the given size
    stack.resize(size + stack.size());
  }
  std::monostate popFrame(int size) {
    // Pop the frame of the given size
    stack.resize(stack.size() - size);
    return std::monostate();
  }

  SymVal get(int index) {
    // Get the symbolic value at the given frame index
    return stack[stack.size() - 1 - index];
  }

  void set(int index, SymVal val) {
    // Set the symbolic value at the given index
    // Not implemented yet
    stack[stack.size() - 1 - index] = val;
  }

  void reset() {
    // Reset the symbolic frames
    stack.clear();
  }

  void reuse(Snapshot_t snapshot);

  size_t size() const { return stack.size(); }

  SymVal operator[](size_t index) const { return stack[index]; }

private:
  std::vector<SymVal> stack;
};

struct NodeBox;
struct SymEnv_t;

// A snapshot of the symbolic state and execution context (control)
class Snapshot_t {
public:
  explicit Snapshot_t(Cont_t cont, MCont_t mcont);

  SymStack_t get_stack() const { return stack; }
  SymFrames_t get_frames() const { return frames; }

  std::monostate resume_execution(SymEnv_t &sym_env, NodeBox *node) const;

private:
  SymStack_t stack;
  SymFrames_t frames;
  // The continuation at the snapshot point
  Cont_t cont;
  MCont_t mcont;
};

inline void SymStack_t::reuse(Snapshot_t snapshot) {
// Reusing the symbolic stack from the snapshot
#ifdef DEBUG
  std::cout << "Reusing symbolic state from snapshot" << std::endl;
  std::cout << "Old stack size = " << stack.size() << std::endl;
  std::cout << "New stack size = " << snapshot.get_stack().stack.size()
            << std::endl;
#endif
  stack = snapshot.get_stack().stack;
}

inline void SymFrames_t::reuse(Snapshot_t snapshot) {
// Reusing the symbolic frames from the snapshot
#ifdef DEBUG
  std::cout << "Reusing symbolic state from snapshot" << std::endl;
  std::cout << "Old frame size = " << stack.size() << std::endl;
  std::cout << "New frame size = " << snapshot.get_frames().stack.size()
            << std::endl;
#endif
  stack = snapshot.get_frames().stack;
}

static SymFrames_t SymFrames;
static SymFrames_t SymGlobals;

struct Node;

struct NodeBox {
  explicit NodeBox(NodeBox *parent);
  std::unique_ptr<Node> node;
  NodeBox *parent;

  std::monostate fillIfElseNode(SymVal cond, int id);
  std::monostate fillFinishedNode();
  std::monostate fillFailedNode();
  std::monostate fillUnreachableNode();
  std::monostate fillSnapshotNode(Snapshot_t snapshot);
  bool isUnexplored() const;
  std::vector<SymVal> collect_path_conds();
};

struct Node {
  virtual ~Node(){};
  virtual std::string to_string() = 0;
  void to_graphviz(std::ostream &os) {
    os << "digraph G {\n";
    os << "  rankdir=TB;\n";
    os << "  node [shape=box, style=filled, fillcolor=lightblue];\n";
    current_id = 0;
    generate_dot(os, -1, "");

    os << "}\n";
  }
  virtual void generate_dot(std::ostream &os, int parent_dot_id,
                            const std::string &edge_label) = 0;

protected:
  // Counter for unique node IDs across the entire graph, only for generating
  // graphviz purpose
  static int current_id;
  void graphviz_node(std::ostream &os, const int node_id,
                     const std::string &label, const std::string &shape,
                     const std::string &fillcolor) {
    os << "  node" << node_id << " [label=\"" << label << "\", shape=" << shape
       << ", style=filled, fillcolor=" << fillcolor << "];\n";
  }

  void graphviz_edge(std::ostream &os, int from_id, int target_id,
                     const std::string &edge_label) {
    os << "  node" << from_id << " -> node" << target_id;
    if (!edge_label.empty()) {
      os << " [label=\"" << edge_label << "\"]";
    }
    os << ";\n";
  }
};

// TODO: use this header file in multiple compilation units will cause problems
// during linking
int Node::current_id = 0;

struct IfElseNode : Node {
  SymVal cond;
  std::unique_ptr<NodeBox> true_branch;
  std::unique_ptr<NodeBox> false_branch;
  int id;

  IfElseNode(SymVal cond, NodeBox *parent, int id)
      : cond(cond), true_branch(std::make_unique<NodeBox>(parent)),
        false_branch(std::make_unique<NodeBox>(parent)), id(id) {}

  std::string to_string() override {
    std::string result = "IfElseNode {\n";
    result += "  true_branch: ";
    if (true_branch) {
      result += true_branch->node->to_string();
    } else {
      result += "nullptr";
    }
    result += "\n";

    result += "  false_branch: ";
    if (false_branch) {
      result += false_branch->node->to_string();
    } else {
      result += "nullptr";
    }
    result += "\n";
    result += "}";
    return result;
  }

  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override {
    int current_node_dot_id = current_id;
    current_id += 1;

    graphviz_node(os, current_node_dot_id, "If", "diamond", "lightyellow");

    // Draw edge from parent if this is not the root node
    if (parent_dot_id != -1) {
      graphviz_edge(os, parent_dot_id, current_node_dot_id, edge_label);
    }
    assert(true_branch != nullptr);
    assert(true_branch->node != nullptr);
    true_branch->node->generate_dot(os, current_node_dot_id, "true");
    assert(false_branch != nullptr);
    assert(false_branch->node != nullptr);
    false_branch->node->generate_dot(os, current_node_dot_id, "false");
  }
};

struct UnExploredNode : Node {
  UnExploredNode() {}
  std::string to_string() override { return "UnexploredNode"; }

protected:
  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override {
    int current_node_dot_id = current_id++;
    graphviz_node(os, current_node_dot_id, "Unexplored", "octagon",
                  "lightgrey");

    if (parent_dot_id != -1) {
      graphviz_edge(os, parent_dot_id, current_node_dot_id, edge_label);
    }
  }
};

struct SnapshotNode : Node {
  SnapshotNode(Snapshot_t snapshot) : snapshot(snapshot) {}
  std::string to_string() override { return "SnapshotNode"; }
  const Snapshot_t &get_snapshot() const { return snapshot; }

protected:
  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override {
    int current_node_dot_id = current_id++;
    graphviz_node(os, current_node_dot_id, "Snapshot", "box", "lightblue");

    if (parent_dot_id != -1) {
      graphviz_edge(os, parent_dot_id, current_node_dot_id, edge_label);
    }
  }

private:
  Snapshot_t snapshot;
};

struct Finished : Node {
  Finished() {}
  std::string to_string() override { return "FinishedNode"; }

protected:
  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override {
    int current_node_dot_id = current_id++;
    graphviz_node(os, current_node_dot_id, "Finished", "box", "lightgreen");

    if (parent_dot_id != -1) {
      graphviz_edge(os, parent_dot_id, current_node_dot_id, edge_label);
    }
  }
};

struct Failed : Node {
  Failed() {}
  std::string to_string() override { return "FailedNode"; }

protected:
  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override {
    int current_node_dot_id = current_id++;
    graphviz_node(os, current_node_dot_id, "Failed", "box", "red");

    if (parent_dot_id != -1) {
      graphviz_edge(os, parent_dot_id, current_node_dot_id, edge_label);
    }
  }
};

struct Unreachable : Node {
  Unreachable() {}
  std::string to_string() override { return "UnreachableNode"; }

protected:
  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override {
    int current_node_dot_id = current_id++;
    graphviz_node(os, current_node_dot_id, "Unreachable", "box", "orange");

    if (parent_dot_id != -1) {
      graphviz_edge(os, parent_dot_id, current_node_dot_id, edge_label);
    }
  }
};

inline NodeBox::NodeBox(NodeBox *parent)
    : node(std::make_unique<UnExploredNode>()),
      /* TODO: avoid allocation of unexplored node */
      parent(parent) {}

inline std::monostate NodeBox::fillIfElseNode(SymVal cond, int id) {
  // fill the current NodeBox with an ifelse branch node when it's unexplored
  if (this->isUnexplored()) {
    node = std::make_unique<IfElseNode>(cond, this, id);
  }
  assert(
      dynamic_cast<IfElseNode *>(node.get()) != nullptr &&
      "Current node is not an Unexplored nor an IfElseNode, cannot fill it!");
  return std::monostate();
}

inline std::monostate NodeBox::fillSnapshotNode(Snapshot_t snapshot) {
  if (this->isUnexplored()) {
    node = std::make_unique<SnapshotNode>(snapshot);
  }
  return std::monostate();
}

inline std::monostate NodeBox::fillFinishedNode() {
  if (this->isUnexplored()) {
    node = std::make_unique<Finished>();
  } else {
    assert(dynamic_cast<Finished *>(node.get()) != nullptr);
  }
  return std::monostate();
}

inline std::monostate NodeBox::fillFailedNode() {
  if (this->isUnexplored()) {
    node = std::make_unique<Failed>();
  } else {
    assert(dynamic_cast<Failed *>(node.get()) != nullptr);
  }
  return std::monostate();
}

inline std::monostate NodeBox::fillUnreachableNode() {
  if (this->isUnexplored()) {
    node = std::make_unique<Unreachable>();
  } else {
    assert(dynamic_cast<Unreachable *>(node.get()) != nullptr);
  }
  return std::monostate();
}

inline bool NodeBox::isUnexplored() const {
  return dynamic_cast<UnExploredNode *>(node.get()) != nullptr ||
         dynamic_cast<SnapshotNode *>(node.get()) != nullptr;
}

inline std::vector<SymVal> NodeBox::collect_path_conds() {
  auto box = this;
  auto result = std::vector<SymVal>();
  while (box->parent) {
    auto parent = box->parent;
    auto if_else_node = dynamic_cast<IfElseNode *>(parent->node.get());
    if (if_else_node) {
      if (if_else_node->true_branch.get() == box) {
        // If the current box is the true branch, add the condition
        result.push_back(if_else_node->cond);
      } else if (if_else_node->false_branch.get() == box) {
        // If the current box is the false branch, add the negated condition
        result.push_back(if_else_node->cond.negate());
      } else {
        throw std::runtime_error("Unexpected node structure in explore tree");
      }
    }
    // Move to parent
    box = box->parent;
  }
  return result;
}

inline Snapshot_t::Snapshot_t(Cont_t cont, MCont_t mcont)
    : stack(SymStack), frames(SymFrames), cont(cont), mcont(mcont) {
#ifdef DEBUG
  std::cout << "Creating snapshot of size " << stack.size() << std::endl;
#endif
}

class ExploreTree_t {
public:
  explicit ExploreTree_t()
      : root(std::make_unique<NodeBox>(nullptr)), cursor(root.get()) {}

  void reset_cursor() {
    GENSYM_INFO("Resetting cursor to root");
    // Reset the cursor to the root of the tree
    cursor = root.get();
  }

  void set_cursor(NodeBox *new_cursor) {
    GENSYM_INFO("Setting cursor to a new node");
    cursor = new_cursor;
    assert(dynamic_cast<SnapshotNode *>(cursor->node.get()) != nullptr);
  }

  std::monostate fillFinishedNode() { return cursor->fillFinishedNode(); }

  std::monostate fillFailedNode() { return cursor->fillFailedNode(); }

  std::monostate fillIfElseNode(SymVal cond, int id) {
    return cursor->fillIfElseNode(cond, id);
  }

  std::monostate moveCursor(bool branch, Snapshot_t snapshot) {
    assert(cursor != nullptr);
    auto if_else_node = dynamic_cast<IfElseNode *>(cursor->node.get());
    assert(
        if_else_node != nullptr &&
        "Can't move cursor when the branch node is not initialized correctly!");
    if (branch) {
      true_branch_cov_map[if_else_node->id] = true;
      if_else_node->false_branch->fillSnapshotNode(snapshot);
      cursor = if_else_node->true_branch.get();
    } else {
      false_branch_cov_map[if_else_node->id] = true;
      if_else_node->true_branch->fillSnapshotNode(snapshot);
      cursor = if_else_node->false_branch.get();
    }

    return std::monostate();
  }

  std::monostate print() {
    std::cout << root->node->to_string() << std::endl;
    return std::monostate();
  }

  std::monostate to_graphviz(std::ostream &os) {
    root->node->to_graphviz(os);
    return std::monostate();
  }

  std::monostate dump_graphviz(std::string filepath) {
    std::ofstream ofs(filepath);
    if (!ofs.is_open()) {
      throw std::runtime_error("Failed to open " + filepath + "  for writing");
    }
    to_graphviz(ofs);
    return std::monostate();
  }

  std::optional<std::vector<SymVal>> get_unexplored_conditions() {
    // Get all unexplored conditions in the tree
    std::vector<SymVal> result;
    auto box = pick_unexplored();
    if (!box) {
      return std::nullopt;
    }
    return box->collect_path_conds();
  }

  NodeBox *pick_unexplored() {
    // Pick an unexplored node from the tree
    // For now, we just iterate through the tree and return the first unexplored
    return pick_unexplored_of(root.get());
  }
  std::vector<bool> true_branch_cov_map;
  std::vector<bool> false_branch_cov_map;
  bool all_branch_covered() const {
    for (bool covered : true_branch_cov_map) {
      if (!covered)
        return false;
    }
    for (bool covered : false_branch_cov_map) {
      if (!covered)
        return false;
    }
    return true;
  }

private:
  NodeBox *pick_unexplored_of(NodeBox *node) {
    if (node->isUnexplored()) {
      return node;
    }
    auto if_else_node = dynamic_cast<IfElseNode *>(node->node.get());
    if (if_else_node) {
      NodeBox *result = pick_unexplored_of(if_else_node->true_branch.get());
      if (result) {
        return result;
      }
      return pick_unexplored_of(if_else_node->false_branch.get());
    }
    return nullptr; // No unexplored node found
  }
  std::unique_ptr<NodeBox> root;
  NodeBox *cursor;
};

static ExploreTree_t ExploreTree;

class SymEnv_t {
public:
  Num read(const Symbol &symbol) {
    if (symbol.get_id() >= map.size()) {
      map.resize(symbol.get_id() + 1);
    }
#if DEBUG
    std::cout << "Read symbol: " << symbol.get_id()
              << " from symbolic environment" << std::endl;
    std::cout << "Current symbolic environment: " << to_string() << std::endl;
#endif

    return map[symbol.get_id()];
  }

  Num read(SymVal sym) {
    auto symbol = dynamic_cast<Symbol *>(sym.symptr.get());
    assert(symbol);
    return read(*symbol);
  }

  void update(std::vector<Num> new_env) { map = std::move(new_env); }

  std::string to_string() const {
    std::string result;
    result += "(\n";
    for (int i = 0; i < map.size(); ++i) {
      const Num &num = map[i];
      result +=
          "  (" + std::to_string(i) + "->" + std::to_string(num.value) + ")\n";
    }
    result += ")";
    return result;
  }

  size_t size() const { return map.size(); }

private:
  std::vector<Num> map; // The symbolic environment, a vector of Num
};

static SymEnv_t SymEnv;

// TODO: reduce the re-computation of the same symbolic expression, it's better
// if it can be done by the smt solver
static Num eval_sym_expr(const SymVal &sym, SymEnv_t &sym_env) {
  if (auto concrete = dynamic_cast<SymConcrete *>(sym.symptr.get())) {
    return concrete->value;
  } else if (auto operation = dynamic_cast<SymBinary *>(sym.symptr.get())) {
    // If it's a operation, we need to evaluate it
    auto lhs = eval_sym_expr(operation->lhs, sym_env);
    auto rhs = eval_sym_expr(operation->rhs, sym_env);
    switch (operation->op) {
    case ADD:
      return lhs + rhs;
    case SUB:
      return lhs - rhs;
    case MUL:
      return lhs * rhs;
    case DIV:
      return lhs / rhs;
    case LT:
      return lhs < rhs;
    case LEQ:
      return lhs <= rhs;
    case GT:
      return lhs > rhs;
    case GEQ:
      return lhs >= rhs;
    default:
      throw std::runtime_error("Operation not supported: " +
                               std::to_string(operation->op));
    }
  } else if (auto symbol = dynamic_cast<Symbol *>(sym.symptr.get())) {
    auto sym_id = symbol->get_id();
    GENSYM_INFO("Reading symbol: " + std::to_string(sym_id));
    return sym_env.read(sym);
  }
  throw std::runtime_error("Not supported symbolic expression");
}

static void resume_conc_stack(const SymStack_t &sym_stack, Stack_t &stack,
                              SymEnv_t &sym_env) {
  stack.resize(sym_stack.size());
  for (size_t i = 0; i < sym_stack.size(); ++i) {
    auto sym = sym_stack[i];
    auto conc = eval_sym_expr(sym, sym_env);
    stack.set_from_front(i, conc);
  }
}

static void resume_conc_frames(const SymFrames_t &sym_frame, Frames_t &frames,
                               SymEnv_t &sym_env) {
  frames.resize(sym_frame.size());
  for (size_t i = 0; i < sym_frame.size(); ++i) {
    auto sym = sym_frame[i];
    auto conc = eval_sym_expr(sym, sym_env);
    frames.set_from_front(i, conc);
  }
}

static void resume_conc_states(const SymStack_t &sym_stack,
                               const SymFrames_t &sym_frame, Stack_t &stack,
                               Frames_t &frames, SymEnv_t &sym_env) {
  resume_conc_stack(sym_stack, stack, sym_env);
  resume_conc_frames(sym_frame, frames, sym_env);
}

inline std::monostate Snapshot_t::resume_execution(SymEnv_t &sym_env,
                                                   NodeBox *node) const {
  // Reset explore tree's cursor
  ExploreTree.set_cursor(node);

  // Restore the symbolic state from the snapshot
  GENSYM_INFO("Reusing symbolic state from snapshot");
  SymStack = stack;
  SymFrames = frames;
  // Restore the concrete states from the symbolic states
  resume_conc_states(stack, frames, Stack, Frames, sym_env);
  // Resume execution from the continuation
  return cont(mcont);
}

struct Memory_t {
  std::vector<uint8_t> memory;
  // Try to have
  // std::vector<std::pair<uint8_t, SymVal>> memory;

  Memory_t(int32_t init_page_count) : memory(init_page_count * pagesize) {}

  int32_t loadInt(int32_t base, int32_t offset) {
    return *reinterpret_cast<int32_t *>(static_cast<uint8_t *>(memory.data()) +
                                        base + offset);
  }

  std::monostate storeInt(int32_t base, int32_t offset, int32_t value) {
    *reinterpret_cast<int32_t *>(static_cast<uint8_t *>(memory.data()) + base +
                                 offset) = value;
    return std::monostate{};
  }

  // grow memory by delta bytes when bytes > 0. return -1 if failed, return old
  // size when success
  int32_t grow(int32_t delta) {
    if (delta <= 0) {
      return memory.size();
    }

    try {
      memory.resize(memory.size() + delta * pagesize);
      auto old_page_count = page_count;
      page_count += delta;
      return memory.size();
    } catch (const std::bad_alloc &e) {
      return -1;
    }
  }
};

static Memory_t Memory(1); // 1 page memory

#endif // WASM_SYMBOLIC_RT_HPP