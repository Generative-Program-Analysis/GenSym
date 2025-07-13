#ifndef WASM_SYMBOLIC_RT_HPP
#define WASM_SYMBOLIC_RT_HPP

#include "concrete_rt.hpp"
#include <cassert>
#include <iterator>
#include <memory>
#include <variant>

class SymVal {
public:
  SymVal operator+(const SymVal &other) const {
    // Define how to add two symbolic values
    // Not implemented yet
    return SymVal();
  }

  SymVal is_zero() const {
    // Check if the symbolic value is zero
    // Not implemented yet
    return SymVal();
  }

  SymVal negate() const {
    // negate the symbolic condition by creating a new symbolic value
    // not implemented yet
    return SymVal();
  }
};

class SymStack_t {
public:
  void push(SymVal val) {
    // Push a symbolic value to the stack
    // Not implemented yet
  }

  SymVal pop() {
    // Pop a symbolic value from the stack
    // Not implemented yet
    return SymVal();
  }

  SymVal peek() { return SymVal(); }
};

static SymStack_t SymStack;

class SymFrames_t {
public:
  void pushFrame(int size) {
    // Push a new frame with the given size
    // Not implemented yet
  }
  std::monostate popFrame(int size) {
    // Pop the frame of the given size
    // Not implemented yet
    return std::monostate();
  }

  SymVal get(int index) {
    // Get the symbolic value at the given index
    // Not implemented yet
    return SymVal();
  }

  void set(int index, SymVal val) {
    // Set the symbolic value at the given index
    // Not implemented yet
  }
};

static SymFrames_t SymFrames;

static SymVal Concrete(Num num) {
  // Convert a concrete number to a symbolic value
  // Not implemented yet
  return SymVal();
}

struct Node;

struct NodeBox {
  explicit NodeBox();
  std::unique_ptr<Node> node;
  NodeBox *parent;
};

struct Node {
  virtual ~Node(){};
  virtual std::string to_string() = 0;
};

struct IfElseNode : Node {
  SymVal cond;
  std::unique_ptr<NodeBox> true_branch;
  std::unique_ptr<NodeBox> false_branch;

  IfElseNode(SymVal cond)
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
};

struct UnExploredNode : Node {
  UnExploredNode() {}
  std::string to_string() override { return "UnexploredNode"; }
};

static UnExploredNode unexplored;

inline NodeBox::NodeBox()
    : node(std::make_unique<
           UnExploredNode>() /* TODO: avoid allocation of unexplored node */) {}

class ExploreTree_t {
public:
  explicit ExploreTree_t()
      : root(std::make_unique<NodeBox>()), cursor(root.get()) {}
  std::monostate fillIfElseNode(SymVal cond) {
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

private:
  std::unique_ptr<NodeBox> root;
  NodeBox *cursor;
};

static ExploreTree_t ExploreTree;

#endif // WASM_SYMBOLIC_RT_HPP