#ifndef WASM_SYMBOLIC_RT_HPP
#define WASM_SYMBOLIC_RT_HPP

#include "concrete_rt.hpp"
#include <cassert>
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
};

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

class SymStack_t {
public:
  void push(SymVal val) {
    // Push a symbolic value to the stack
    stack.push_back(val);
  }

  SymVal pop() {
    // Pop a symbolic value from the stack
    auto ret = stack.back();
    stack.pop_back();
    return ret;
  }

  SymVal peek() { return stack.back(); }

  void reset() {
    // Reset the symbolic stack
    stack.clear();
  }

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

  std::vector<SymVal> stack;
};

static SymFrames_t SymFrames;

struct Node;

struct NodeBox {
  explicit NodeBox(NodeBox *parent);
  std::unique_ptr<Node> node;
  NodeBox *parent;

  std::monostate fillIfElseNode(SymVal cond);
  std::monostate fillFinishedNode();
  std::monostate fillFailedNode();
  std::monostate fillUnreachableNode();

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

  IfElseNode(SymVal cond, NodeBox *parent)
      : cond(cond), true_branch(std::make_unique<NodeBox>(parent)),
        false_branch(std::make_unique<NodeBox>(parent)) {}

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

inline std::monostate NodeBox::fillIfElseNode(SymVal cond) {
  // fill the current NodeBox with an ifelse branch node it's unexplored
  if (dynamic_cast<UnExploredNode *>(node.get())) {
    node = std::make_unique<IfElseNode>(cond, this);
  }
  assert(dynamic_cast<IfElseNode *>(node.get()) != nullptr &&
         "Current node is not an IfElseNode, cannot fill it!");
  return std::monostate();
}

inline std::monostate NodeBox::fillFinishedNode() {
  if (dynamic_cast<UnExploredNode *>(node.get())) {
    node = std::make_unique<Finished>();
  } else {
    assert(dynamic_cast<Finished *>(node.get()) != nullptr);
  }
  return std::monostate();
}

inline std::monostate NodeBox::fillFailedNode() {
  if (dynamic_cast<UnExploredNode *>(node.get())) {
    node = std::make_unique<Failed>();
  } else {
    assert(dynamic_cast<Failed *>(node.get()) != nullptr);
  }
  return std::monostate();
}

inline std::monostate NodeBox::fillUnreachableNode() {
  if (dynamic_cast<UnExploredNode *>(node.get())) {
    node = std::make_unique<Unreachable>();
  } else {
    assert(dynamic_cast<Unreachable *>(node.get()) != nullptr);
  }
  return std::monostate();
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

class ExploreTree_t {
public:
  explicit ExploreTree_t()
      : root(std::make_unique<NodeBox>(nullptr)), cursor(root.get()) {}

  void reset_cursor() {
    // Reset the cursor to the root of the tree
    cursor = root.get();
  }

  std::monostate fillFinishedNode() { return cursor->fillFinishedNode(); }

  std::monostate fillFailedNode() { return cursor->fillFailedNode(); }

  std::monostate fillIfElseNode(SymVal cond) {
    return cursor->fillIfElseNode(cond);
  }

  std::monostate moveCursor(bool branch) {
    assert(cursor != nullptr);
    auto if_else_node = dynamic_cast<IfElseNode *>(cursor->node.get());
    assert(
        if_else_node != nullptr &&
        "Can't move cursor when the branch node is not initialized correctly!");
    if (branch) {
      cursor = if_else_node->true_branch.get();
    } else {
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

private:
  NodeBox *pick_unexplored_of(NodeBox *node) {
    if (dynamic_cast<UnExploredNode *>(node->node.get()) != nullptr) {
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
  Num read(SymVal sym) {
    auto symbol = dynamic_cast<Symbol *>(sym.symptr.get());
    assert(symbol);
    if (symbol->get_id() >= map.size()) {
      map.resize(symbol->get_id() + 1);
    }
#if DEBUG
    std::cout << "Read symbol: " << symbol->get_id()
              << " from symbolic environment" << std::endl;
    std::cout << "Current symbolic environment: " << to_string() << std::endl;
#endif
    return map[symbol->get_id()];
  }

  void update(std::vector<Num> new_env) {
    map = std::move(new_env);
  }

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

private:
  std::vector<Num> map;    // The symbolic environment, a vector of Num
};

static SymEnv_t SymEnv;

#endif // WASM_SYMBOLIC_RT_HPP