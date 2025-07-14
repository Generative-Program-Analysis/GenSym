#ifndef WASM_SYMBOLIC_RT_HPP
#define WASM_SYMBOLIC_RT_HPP

#include "concrete_rt.hpp"
#include <cassert>
#include <cstdio>
#include <iterator>
#include <memory>
#include <ostream>
#include <variant>
#include <vector>

class Symbolic {};

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
  return SymVal(std::make_shared<SymBinary>(ADD, this, other));
}

inline SymVal SymVal::minus(const SymVal &other) const {
  return SymVal(std::make_shared<SymBinary>(SUB, this, other));
}

inline SymVal SymVal::mul(const SymVal &other) const {
  return SymVal(std::make_shared<SymBinary>(MUL, this, other));
}

inline SymVal SymVal::div(const SymVal &other) const {
  return SymVal(std::make_shared<SymBinary>(DIV, this, other));
}

inline SymVal SymVal::eq(const SymVal &other) const {
  return SymVal(std::make_shared<SymBinary>(EQ, this, other));
}

inline SymVal SymVal::neq(const SymVal &other) const {
  return SymVal(std::make_shared<SymBinary>(NEQ, this, other));
}
inline SymVal SymVal::lt(const SymVal &other) const {
  return SymVal(std::make_shared<SymBinary>(LT, this, other));
}
inline SymVal SymVal::leq(const SymVal &other) const {
  return SymVal(std::make_shared<SymBinary>(LEQ, this, other));
}
inline SymVal SymVal::gt(const SymVal &other) const {
  return SymVal(std::make_shared<SymBinary>(GT, this, other));
}
inline SymVal SymVal::geq(const SymVal &other) const {
  return SymVal(std::make_shared<SymBinary>(GEQ, this, other));
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

  std::vector<SymVal> stack;
};

static SymFrames_t SymFrames;

struct Node;

struct NodeBox {
  explicit NodeBox();
  std::unique_ptr<Node> node;
  NodeBox *parent;
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
  int get_next_id(int &id_counter) { return id_counter++; }
  virtual int generate_dot(std::ostream &os, int parent_dot_id,
                           const std::string &edge_label) = 0;

protected:
  // Counter for unique node IDs across the entire graph, only for generating
  // graphviz purpose
  static int current_id;
};

// TODO: use this header file in multiple compilation units will cause problems
// during linking
int Node::current_id = 0;

struct IfElseNode : Node {
  Symbolic cond;
  std::unique_ptr<NodeBox> true_branch;
  std::unique_ptr<NodeBox> false_branch;

  IfElseNode(Symbolic cond)
      : cond(cond), true_branch(std::make_unique<NodeBox>()),
        false_branch(std::make_unique<NodeBox>()) {}

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

  int generate_dot(std::ostream &os, int parent_dot_id,
                   const std::string &edge_label) override {
    int current_node_dot_id = current_id;
    current_id += 1;

    os << "  node" << current_node_dot_id << " [label=\"If\","
       << "shape=diamond, fillcolor=lightyellow];\n";

    // Draw edge from parent if this is not the root node
    if (parent_dot_id != -1) {
      os << "  node" << parent_dot_id << " -> node" << current_node_dot_id;
      if (!edge_label.empty()) {
        os << " [label=\"" << edge_label << "\"]";
      }
      os << ";\n";
    }
    assert(true_branch != nullptr);
    assert(true_branch->node != nullptr);
    true_branch->node->generate_dot(os, current_node_dot_id, "true");
    assert(false_branch != nullptr);
    assert(false_branch->node != nullptr);
    false_branch->node->generate_dot(os, current_node_dot_id, "false");
    return current_node_dot_id;
  }
};

struct UnExploredNode : Node {
  UnExploredNode() {}
  std::string to_string() override { return "UnexploredNode"; }

protected:
  int generate_dot(std::ostream &os, int parent_dot_id,
                   const std::string &edge_label) override {
    int current_node_dot_id = current_id++;

    os << "  node" << current_node_dot_id
       << " [label=\"Unexplored\", shape=octagon, style=filled, "
          "fillcolor=lightgrey];\n";

    if (parent_dot_id != -1) {
      os << "  node" << parent_dot_id << " -> node" << current_node_dot_id;
      if (!edge_label.empty()) {
        os << " [label=\"" << edge_label << "\"]";
      }
      os << ";\n";
    }

    return current_node_dot_id;
  }
};

static UnExploredNode unexplored;

inline NodeBox::NodeBox()
    : node(std::make_unique<
           UnExploredNode>() /* TODO: avoid allocation of unexplored node */) {}

class ExploreTree_t {
public:
  explicit ExploreTree_t()
      : root(std::make_unique<NodeBox>()), cursor(root.get()) {}
  std::monostate fillIfElseNode(Symbolic cond) {
    // fill the current node with an ifelse branch node
    cursor->node = std::make_unique<IfElseNode>(cond);
    return std::monostate();
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

private:
  std::unique_ptr<NodeBox> root;
  NodeBox *cursor;
};

static ExploreTree_t ExploreTree;

#endif // WASM_SYMBOLIC_RT_HPP