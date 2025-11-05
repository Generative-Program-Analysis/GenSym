#ifndef WASM_SYMBOLIC_RT_HPP
#define WASM_SYMBOLIC_RT_HPP

#include "concrete_rt.hpp"
#include "config.hpp"
#include "controls.hpp"
#include "heap_mem_bookkeeper.hpp"
#include "immer/map.hpp"
#include "immer/map_transient.hpp"
#include "immer/vector_transient.hpp"
#include "profile.hpp"
#include "utils.hpp"
#include "z3++.h"
#include <cassert>
#include <cstddef>
#include <cstdint>
#include <cstdio>
#include <filesystem>
#include <fstream>
#include <iterator>
#include <memory>
#include <optional>
#include <ostream>
#include <set>
#include <string>
#include <unordered_map>
#include <variant>
#include <vector>

class Symbolic {
public:
  Symbolic() {}
  virtual ~Symbolic() = default; // Make Symbolic polymorphic
  virtual int size() = 0;
};

static int max_id = 0;

class Symbol : public Symbolic {
public:
  // TODO: add type information to determine the size of bitvector
  // for now we just assume that only i32 will be used
  Symbol(int id) : id(id) { max_id = std::max(max_id, id); }
  int get_id() const { return id; }
  int size() override { return 1; }

private:
  int id;
};

class SymConcrete : public Symbolic {
public:
  Num value;
  SymConcrete(Num num) : value(num) {}
  int size() override { return 1; }
};

class SmallBV : public Symbolic {
public:
  SmallBV(int width, int64_t value) : width(width), value(value) {}
  int get_size() const { return width; }
  int64_t get_value() const { return value; }
  int size() override { return 1; }

private:
  int width; // in bits
  int64_t value;
};
struct SymBinary;

enum Operation {
  ADD,    // Addition
  SUB,    // Subtraction
  MUL,    // Multiplication
  DIV,    // Division
  EQ,     // Equal
  NEQ,    // Not equal
  LT,     // Less than
  LEQ,    // Less than or equal
  GT,     // Greater than
  GEQ,    // Greater than or equal
  B_AND,  // Bitwise AND
  CONCAT, // Byte-level concatenation
};
static MemBookKeeper<Symbolic> SymBookKeeper;

static std::shared_ptr<SymConcrete> ZERO =
    SymBookKeeper.allocate<SymConcrete>(I32V(0));
static std::shared_ptr<SmallBV> ZeroByte =
    SymBookKeeper.allocate<SmallBV>(8, 0);

struct SymVal {
  std::shared_ptr<Symbolic> symptr;
  std::shared_ptr<z3::expr> z3_expr;

  SymVal() : symptr(ZERO) {}
  SymVal(std::shared_ptr<Symbolic> symptr) : symptr(symptr) {}

  // data structure operations
  SymVal makeSymbolic() const;

  // bitvector arithmetic operations
  SymVal is_zero() const;
  SymVal add(const SymVal &other) const;
  SymVal minus(const SymVal &other) const;
  SymVal mul(const SymVal &other) const;
  SymVal div(const SymVal &other) const;
  SymVal eq(const SymVal &other) const;
  SymVal neq(const SymVal &other) const;
  SymVal lt(const SymVal &other) const;
  SymVal le(const SymVal &other) const;
  SymVal gt(const SymVal &other) const;
  SymVal ge(const SymVal &other) const;
  SymVal negate() const;
  SymVal bitwise_and(const SymVal &other) const;
  SymVal concat(const SymVal &other) const;
  SymVal extract(int high, int low) const;
  // TODO: add bitwise operations, and use the underlying bitvector theory

  bool is_concrete() const;
  int size() const { return symptr->size(); }

private:
  static SymVal make_binary(Operation op, const SymVal &lhs, const SymVal &rhs);
};

static SymVal make_symbolic(int index) {
  return SymVal(SymBookKeeper.allocate<Symbol>(index));
}

static std::unordered_map<int64_t, SymVal> concrete_pool;

inline SymVal Concrete(Num num) {
  if (concrete_pool.find(num.toInt()) != concrete_pool.end()) {
    return concrete_pool[num.toInt()];
  }

  auto new_val = SymVal(SymBookKeeper.allocate<SymConcrete>(num));
  concrete_pool[num.toInt()] = new_val;
  return new_val;
}

// Extract is different from other operations, it only has one symbolic operand,
// the other two operands are constants
// Extract from value, both high and low are inclusive byte indexes
struct SymExtract : Symbolic {
  SymVal value;
  int high;
  int low;

  SymExtract(SymVal value, int high, int low)
      : value(value), high(high), low(low) {}
  int size() override { return 1 + value.size(); }
};

struct SymBinary : Symbolic {
  Operation op;
  SymVal lhs;
  SymVal rhs;

  SymBinary(Operation op, SymVal lhs, SymVal rhs)
      : op(op), lhs(lhs), rhs(rhs) {}
  int size() override { return 1 + lhs.size() + rhs.size(); }
};

inline SymVal SymVal::add(const SymVal &other) const {
  return make_binary(ADD, *this, other);
}

inline SymVal SymVal::minus(const SymVal &other) const {
  return make_binary(SUB, *this, other);
}

inline SymVal SymVal::mul(const SymVal &other) const {
  return make_binary(MUL, *this, other);
}

inline SymVal SymVal::div(const SymVal &other) const {
  return make_binary(DIV, *this, other);
}

inline SymVal SymVal::eq(const SymVal &other) const {
  return make_binary(EQ, *this, other);
}

inline SymVal SymVal::neq(const SymVal &other) const {
  return make_binary(NEQ, *this, other);
}

inline SymVal SymVal::lt(const SymVal &other) const {
  return make_binary(LT, *this, other);
}

inline SymVal SymVal::le(const SymVal &other) const {
  return make_binary(LEQ, *this, other);
}

inline SymVal SymVal::gt(const SymVal &other) const {
  return make_binary(GT, *this, other);
}

inline SymVal SymVal::ge(const SymVal &other) const {
  return make_binary(GEQ, *this, other);
}

inline SymVal SymVal::is_zero() const {
  return make_binary(EQ, *this, Concrete(I32V(0)));
}

inline SymVal SymVal::negate() const {
  return make_binary(EQ, *this, Concrete(I32V(0)));
}

inline SymVal SymVal::concat(const SymVal &other) const {
  return make_binary(CONCAT, *this, other);
}

inline SymVal SymVal::extract(int high, int low) const {
  assert(high >= low && "Invalid extract range");
  return SymVal(SymBookKeeper.allocate<SymExtract>(*this, high, low));
}

inline SymVal SymVal::bitwise_and(const SymVal &other) const {
  return make_binary(B_AND, *this, other);
}
inline SymVal SymVal::make_binary(Operation op, const SymVal &lhs,
                                  const SymVal &rhs) {
  assert(lhs.symptr != nullptr && rhs.symptr != nullptr);
  return SymVal(SymBookKeeper.allocate<SymBinary>(op, lhs, rhs));
}
static std::unordered_map<int, SymVal> SymbolCache;

inline SymVal SymVal::makeSymbolic() const {
  auto concrete = dynamic_cast<SymConcrete *>(symptr.get());
  if (concrete) {
    // If the symbolic value is a concrete value, use it to create a symbol
    auto id = concrete->value.toInt();
    auto it = SymbolCache.find(id);
    if (it != SymbolCache.end()) {
      return it->second;
    }
    auto sym = Symbol(id);
    auto ptr = SymBookKeeper.allocate<Symbol>(sym);
    return SymVal(ptr);

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
#ifdef USE_IMM
    auto ret = *(stack.end() - 1);
    stack.take(stack.size() - 1);
    return ret;
#else
    auto ret = stack.back();
    stack.pop_back();
    return ret;
#endif
  }

  SymVal peek() { return *(stack.end() - 1); }

  std::monostate shift(int32_t offset, int32_t size) {
    auto n = stack.size();
    for (size_t i = n - size; i < n; ++i) {
      assert(i - offset >= 0);
#ifdef USE_IMM
      stack.set(i - offset, stack[i]);
#else
      stack[i - offset] = stack[i];
#endif
    }
#ifdef USE_IMM
    stack.take(n - offset);
#else
    stack.resize(n - offset);
#endif
    return std::monostate();
  }

  void reset() {
// Reset the symbolic stack
#ifdef USE_IMM
    stack = immer::vector_transient<SymVal>();
#else
    stack.clear();
#endif
  }

  size_t size() const { return stack.size(); }

  SymVal operator[](size_t index) const { return stack[index]; }

  int cost_of_copy() const {
    int cost = 0;
    for (size_t i = 0; i < stack.size(); ++i) {
      cost += stack[i].size();
    }
    return cost;
  }

private:
#ifdef USE_IMM
  immer::vector_transient<SymVal> stack;
#else
  std::vector<SymVal> stack;
#endif
};

static SymStack_t SymStack;

class SymFrames_t {

public:
  void pushFrame(int size) {
    // Push a new frame with the given size
#ifdef USE_IMM
    for (int i = 0; i < size; ++i) {
      stack.push_back(SymVal());
    }
#else
    stack.resize(size + stack.size());
#endif
  }
  std::monostate popFrame(int size) {
    // Pop the frame of the given size

#ifdef USE_IMM
    stack.take(stack.size() - size);
#else
    stack.resize(stack.size() - size);
#endif
    return std::monostate();
  }

  SymVal get(int index) {
    // Get the symbolic value at the given frame index
    auto res = stack[stack.size() - 1 - index];
    return res;
  }

  void set(int index, SymVal val) {
    // Set the symbolic value at the given index
    assert(val.symptr != nullptr);
#ifdef USE_IMM
    stack.set(stack.size() - 1 - index, val);
#else
    stack[stack.size() - 1 - index] = val;
#endif
  }

  void reset() {
    // Reset the symbolic frames

#ifdef USE_IMM
    stack = immer::vector_transient<SymVal>();
#else
    stack.clear();
#endif
  }

  size_t size() const { return stack.size(); }

  SymVal operator[](size_t index) const { return stack[index]; }

  int cost_of_copy() const {
    int cost = 0;
    for (size_t i = 0; i < stack.size(); ++i) {
      cost += stack[i].size();
    }
    return cost;
  }

private:
#ifdef USE_IMM
  immer::vector_transient<SymVal> stack;
#else
  std::vector<SymVal> stack;
#endif
};

struct NodeBox;
struct SymEnv_t;

class SymMemory_t {
public:
#ifdef USE_IMM
  immer::map_transient<int, SymVal> memory;
#else
  std::unordered_map<int, SymVal> memory;
#endif

  SymVal loadSymByte(int32_t addr) {
// if the address is not in the memory, it must be a zero-initialized memory
#ifdef USE_IMM
    auto it = memory.find(addr);
    if (it != nullptr) {
      return *it;
    } else {
      auto s = SymVal(ZeroByte);
      return s;
    }
#else
    auto it = memory.find(addr);
    SymVal s = (it != memory.end()) ? it->second : SymVal(ZeroByte);
    return s;
#endif
  }

  SymVal loadSym(int32_t base, int32_t offset) {
    // calculate the real address

#ifdef USE_IMM
    int32_t addr = base + offset;
    auto it = memory.find(addr);
    SymVal s0 = it ? *it : SymVal(ZeroByte);
    it = memory.find(addr + 1);
    SymVal s1 = it ? *it : SymVal(ZeroByte);
    it = memory.find(addr + 2);
    SymVal s2 = it ? *it : SymVal(ZeroByte);
    it = memory.find(addr + 3);
    SymVal s3 = it ? *it : SymVal(ZeroByte);

    return s3.concat(s2).concat(s1).concat(s0);
#else
    int32_t addr = base + offset;
    auto it = memory.find(addr);
    SymVal s0 = (it != memory.end()) ? it->second : SymVal(ZeroByte);
    it = memory.find(addr + 1);
    SymVal s1 = (it != memory.end()) ? it->second : SymVal(ZeroByte);
    it = memory.find(addr + 2);
    SymVal s2 = (it != memory.end()) ? it->second : SymVal(ZeroByte);
    it = memory.find(addr + 3);
    SymVal s3 = (it != memory.end()) ? it->second : SymVal(ZeroByte);

    return s3.concat(s2).concat(s1).concat(s0);
#endif
  }

  // when loading a symval, we need to concat 4 symbolic values
  // This sounds terribly bad for SMT...
  // Load a 4-byte symbolic value from memory
  // Store a 4-byte symbolic value to memory
  std::monostate storeSym(int32_t base, int32_t offset, SymVal value) {
    int32_t addr = base + offset;
    // Extract 4 bytes from that symbol
    SymVal s0 = value.extract(1, 1);
    SymVal s1 = value.extract(2, 2);
    SymVal s2 = value.extract(3, 3);
    SymVal s3 = value.extract(4, 4);
#ifdef USE_IMM
    memory.set(addr, s0);
    memory.set(addr + 1, s1);
    memory.set(addr + 2, s2);
    memory.set(addr + 3, s3);
#else
    memory[addr] = s0;
    memory[addr + 1] = s1;
    memory[addr + 2] = s2;
    memory[addr + 3] = s3;
#endif
    return std::monostate{};
  }

  std::monostate reset() {
#ifdef USE_IMM
    memory = immer::map_transient<int, SymVal>();
#else
    memory.clear();
#endif
    return std::monostate{};
  }

  int cost_of_copy() const {
#ifdef USE_IMM
    // If we use immer, the copy cost should be negligible
    return 0;
#else
    return memory.size();
#endif
  }
};

static SymMemory_t SymMemory;

// A snapshot of the symbolic state and execution context (control)
class Snapshot_t {
public:
  explicit Snapshot_t(Cont_t cont, MCont_t mcont, SymStack_t stack,
                      SymFrames_t frames, SymMemory_t memory);
  explicit Snapshot_t() {}

  SymStack_t get_stack() const { return stack; }
  SymFrames_t get_frames() const { return frames; }
  SymMemory_t get_memory() const { return memory; }

  std::monostate resume_execution(NodeBox *node) const;

  static int cost_of_snapshot();

private:
  SymStack_t stack;
  SymFrames_t frames;
  SymMemory_t memory;
  // The continuation at the snapshot point
  Cont_t cont;
  MCont_t mcont;
};

static SymFrames_t SymFrames;
static SymFrames_t SymGlobals;

static Control makeControl(Cont_t cont, MCont_t mcont) {
  return Control(cont, mcont);
}

static Snapshot_t makeSnapshot(Control control) {
  // create a snapshot from the current symbolic states and the control
  return Snapshot_t(control.cont, control.mcont, SymStack, SymFrames,
                    SymMemory);
}

struct Node;

struct NodeBox {
  explicit NodeBox(NodeBox *parent);
  std::unique_ptr<Node> node;
  NodeBox *parent;
  int cost;
  int instr_cost;

  bool fillIfElseNode(SymVal cond, int id);
  std::monostate fillFinishedNode();
  std::monostate fillFailedNode();
  std::monostate fillUnreachableNode();
  std::monostate fillSnapshotNode(Snapshot_t snapshot);
  std::monostate fillNotToExploreNode();
  bool isUnexplored() const;
  std::vector<SymVal> collect_path_conds();
  int min_cost_of_reaching_here();
  void reach_here(std::function<void()>);
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
  std::optional<Snapshot_t> snapshot;

  IfElseNode(SymVal cond, NodeBox *parent, int id)
      : cond(cond), true_branch(std::make_unique<NodeBox>(parent)),
        false_branch(std::make_unique<NodeBox>(parent)), id(id),
        snapshot(std::nullopt) {}

  IfElseNode(SymVal cond, NodeBox *parent, int id, Snapshot_t snapshot)
      : cond(cond), true_branch(std::make_unique<NodeBox>(parent)),
        false_branch(std::make_unique<NodeBox>(parent)), id(id),
        snapshot(snapshot) {}

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

struct NotToExploreNode : Node {
  NotToExploreNode() {}
  std::string to_string() override { return "NotToExploreNode"; }

protected:
  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override {
    int current_node_dot_id = current_id++;
    graphviz_node(os, current_node_dot_id, "NotToExplore", "box", "grey");

    if (parent_dot_id != -1) {
      graphviz_edge(os, parent_dot_id, current_node_dot_id, edge_label);
    }
  }
};

struct SnapshotNode : Node {
  SnapshotNode(Snapshot_t snapshot) : snapshot(snapshot) {}
  std::string to_string() override { return "SnapshotNode"; }
  const Snapshot_t &get_snapshot() const { return snapshot; }
  Snapshot_t move_out_snapshot() { return std::move(snapshot); }

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
      parent(parent), cost(-1), instr_cost(0) {}

inline bool NodeBox::fillIfElseNode(SymVal cond, int id) {
  // fill the current NodeBox with an ifelse branch node when it's unexplored
  if (auto ptr = dynamic_cast<SnapshotNode *>(node.get())) {
    node =
        std::make_unique<IfElseNode>(cond, this, id, ptr->move_out_snapshot());
    return true;
  } else if (dynamic_cast<UnExploredNode *>(node.get())) {
    node = std::make_unique<IfElseNode>(cond, this, id);
    return true;
  }
  assert(
      dynamic_cast<IfElseNode *>(node.get()) != nullptr &&
      "Current node is not an Unexplored nor an IfElseNode, cannot fill it!");
  return false;
}

inline std::monostate NodeBox::fillSnapshotNode(Snapshot_t snapshot) {
  if (this->isUnexplored()) {
    node = std::make_unique<SnapshotNode>(snapshot);
  }
  return std::monostate();
}

inline std::monostate NodeBox::fillNotToExploreNode() {
  if (this->isUnexplored()) {
    node = std::make_unique<NotToExploreNode>();
  } else {
    assert(dynamic_cast<NotToExploreNode *>(node.get()) != nullptr);
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
  assert(node != nullptr);
  if (dynamic_cast<UnExploredNode *>(node.get()) != nullptr) {
    return true;
  }
  if (dynamic_cast<SnapshotNode *>(node.get()) != nullptr) {
    return true;
  }
  return false;
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

inline int NodeBox::min_cost_of_reaching_here() {
  if (cost != -1) {
    return cost;
  }

  if (auto snapshot = dynamic_cast<SnapshotNode *>(node.get())) {
    cost = snapshot->get_snapshot().cost_of_snapshot();
    return cost;
  }

  if (parent != nullptr) {
    auto parent_cost = parent->min_cost_of_reaching_here();
    cost = parent_cost + instr_cost;
    return cost;
  }
  cost = instr_cost;
  return cost;
}

inline Snapshot_t::Snapshot_t(Cont_t cont, MCont_t mcont, SymStack_t stack,
                              SymFrames_t frames, SymMemory_t memory)
    : stack(std::move(stack)), frames(std::move(frames)),
      memory(std::move(memory)), cont(cont), mcont(mcont) {
  Profile.step(StepProfileKind::SNAPSHOT_CREATE);
#ifdef DEBUG
  std::cout << "Creating snapshot of size " << stack.size() << std::endl;
#endif
}

inline int Snapshot_t::cost_of_snapshot() {
  auto cost_of_stack_copy = SymStack.cost_of_copy();
  auto cost_of_frame_copy = SymFrames.cost_of_copy();
  auto cost_of_memory_copy = SymMemory.cost_of_copy();
  return 5.336 *
         (cost_of_stack_copy + cost_of_frame_copy + cost_of_memory_copy);
}

struct OverallResult {
  int unexplored_count = 0;
  int finished_count = 0;
  int failed_count = 0;
  int not_to_explore_count = 0;
  int unreachable_count = 0;

  void print() {
    std::cout << "Explore Tree Overall Result:" << std::endl;
    std::cout << "  Unexplored paths: " << unexplored_count << std::endl;
    std::cout << "  Finished paths: " << finished_count << std::endl;
    std::cout << "  Failed paths: " << failed_count << std::endl;
    std::cout << "  Unreachable paths: " << unreachable_count << std::endl;
    std::cout << "  NotToExplore paths: " << not_to_explore_count << std::endl;
  }
};

class ExploreTree_t {
public:
  explicit ExploreTree_t()
      : root(std::make_unique<NodeBox>(nullptr)), cursor(root.get()) {}

  void reset_cursor() {
    GENSYM_INFO("Resetting cursor to root");
    // Reset the cursor to the root of the tree
    cursor = root.get();
  }

  void clear() {
    GENSYM_INFO("Clearing the explore tree");
    root = std::make_unique<NodeBox>(nullptr);
    cursor = root.get();
    true_branch_cov_map.clear();
    false_branch_cov_map.clear();
  }

  void set_cursor(NodeBox *new_cursor) {
    GENSYM_INFO("Setting cursor to a new node");
    cursor = new_cursor;
    assert(dynamic_cast<SnapshotNode *>(cursor->node.get()) != nullptr);
  }

  std::monostate fillFinishedNode() { return cursor->fillFinishedNode(); }

  std::monostate fillFailedNode() { return cursor->fillFailedNode(); }

  std::monostate fillIfElseNode(SymVal cond, int id) {
    if (cursor->fillIfElseNode(cond, id)) {
      auto if_else_node = dynamic_cast<IfElseNode *>(cursor->node.get());
      register_new_node(if_else_node->true_branch.get());
      register_new_node(if_else_node->false_branch.get());
    }
    return std::monostate();
  }

  std::monostate fillNotToExploredNode() {
    return cursor->fillNotToExploreNode();
  }

  std::vector<SymVal> collect_current_path_conds() {
    return cursor->collect_path_conds();
  }

  bool worth_to_create_snapshot() {
    if (!ENABLE_COST_MODEL) {
      return REUSE_SNAPSHOT;
    }
    // find out the best way to reach the current position via our cost model
    auto snapshot_cost = Snapshot_t::cost_of_snapshot();
    int reach_parent_cost = 0;
    if (cursor->parent) {
      reach_parent_cost = cursor->parent->min_cost_of_reaching_here();
    } else {
      reach_parent_cost = 0;
    }
    auto parent_cost =
        cursor->parent ? cursor->parent->min_cost_of_reaching_here() : 0;
    auto exec_from_parent_cost = reach_parent_cost + cursor->instr_cost;
    GENSYM_INFO("The score of snapshot tendency: " +
                std::to_string(exec_from_parent_cost - snapshot_cost));
    return snapshot_cost <= exec_from_parent_cost;
  }

  std::monostate moveCursor(bool branch, Control control) {
    Profile.step(StepProfileKind::CURSOR_MOVE);
    assert(cursor != nullptr);
    auto if_else_node = dynamic_cast<IfElseNode *>(cursor->node.get());
    assert(
        if_else_node != nullptr &&
        "Can't move cursor when the branch node is not initialized correctly!");
    int cost_from_parent = CostManager.dump_instr_cost();
    cursor->instr_cost = cost_from_parent;
    if (branch) {
      true_branch_cov_map[if_else_node->id] = true;
      if (worth_to_create_snapshot()) {
        auto snapshot = makeSnapshot(control);
        if_else_node->false_branch->fillSnapshotNode(snapshot);
      } else {
        // Do nothing, the initial value of the branch is an unexplored node
      }
      cursor = if_else_node->true_branch.get();
    } else {
      false_branch_cov_map[if_else_node->id] = true;
      if (worth_to_create_snapshot()) {
        auto snapshot = makeSnapshot(control);
        if_else_node->true_branch->fillSnapshotNode(snapshot);
      } else {
        // Do nothing, the initial value of the branch is an unexplored node
      }
      cursor = if_else_node->false_branch.get();
    }

    return std::monostate();
  }

  std::monostate moveCursorNoControl(bool branch) {
    Profile.step(StepProfileKind::CURSOR_MOVE);
    assert(cursor != nullptr);
    auto if_else_node = dynamic_cast<IfElseNode *>(cursor->node.get());
    assert(
        if_else_node != nullptr &&
        "Can't move cursor when the branch node is not initialized correctly!");
    int cost_from_parent = CostManager.dump_instr_cost();
    cursor->instr_cost = cost_from_parent;
    if (branch) {
      true_branch_cov_map[if_else_node->id] = true;
      if_else_node->false_branch->fillNotToExploreNode();
      cursor = if_else_node->true_branch.get();
    } else {
      assert(false &&
             "moveCursorNoControl should not be used for false branch");
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
    std::filesystem::path out_path(filepath);
    auto parent = out_path.parent_path();
    if (!parent.empty()) {
      std::error_code ec;
      std::filesystem::create_directories(parent, ec);
      if (ec) {
        throw std::runtime_error("Failed to create output directory: " +
                                 ec.message());
      }
    }
    std::ofstream ofs(filepath);
    if (!ofs.is_open()) {
      throw std::runtime_error("Failed to open " + filepath + "  for writing");
    }
    to_graphviz(ofs);
    return std::monostate();
  }

  OverallResult read_current_overall_result() {
    OverallResult result;
    std::function<void(NodeBox *)> dfs = [&](NodeBox *node) {
      if (auto if_else_node = dynamic_cast<IfElseNode *>(node->node.get())) {
        dfs(if_else_node->true_branch.get());
        dfs(if_else_node->false_branch.get());
      } else if (dynamic_cast<UnExploredNode *>(node->node.get())) {
        result.unexplored_count += 1;
      } else if (dynamic_cast<Finished *>(node->node.get())) {
        result.finished_count += 1;
      } else if (dynamic_cast<Failed *>(node->node.get())) {
        result.failed_count += 1;
      } else if (dynamic_cast<Unreachable *>(node->node.get())) {
        result.unreachable_count += 1;
      } else if (dynamic_cast<SnapshotNode *>(node->node.get())) {
        // Snapshot node is considered unexplored
        result.unexplored_count += 1;
      } else if (dynamic_cast<NotToExploreNode *>(node->node.get())) {
        result.not_to_explore_count += 1;
      } else {
        throw std::runtime_error("Unknown node type in explore tree");
      }
    };
    dfs(root.get());
    return result;
  }

  std::monostate print_overall_result() {}

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

  NodeBox *get_root() const { return root.get(); }

  void register_new_node_collector(std::function<void(NodeBox *)> func) {
    new_node_collectors.push_back(func);
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
  void register_new_node(NodeBox *node) {
    for (auto &func : new_node_collectors) {
      func(node);
    }
  }
  std::unique_ptr<NodeBox> root;
  NodeBox *cursor;
  std::vector<std::function<void(NodeBox *)>> new_node_collectors;
};

static ExploreTree_t ExploreTree;

using NumMap = std::unordered_map<int, Num>;

class SymEnv_t {
public:
  Num read(const Symbol &symbol) {
#if DEBUG
    std::cout << "Read symbol: " << symbol.get_id()
              << " from symbolic environment" << std::endl;
    std::cout << "Current symbolic environment: " << to_string() << std::endl;
#endif
    map.try_emplace(symbol.get_id(), Num(I32V(0)));
    return map.at(symbol.get_id());
  }

  Num read(SymVal sym) {
    // Read the value of a symbolic value from the environment, it will update
    // the environment if the key does not exist.
    auto symbol = dynamic_cast<Symbol *>(sym.symptr.get());
    assert(symbol);
    return read(*symbol);
  }

  void update(NumMap new_env) { map = std::move(new_env); }

  std::string to_string() const {
    std::string result;
    result += "(\n";
    for (const auto &[id, num] : map) {
      result +=
          "  (" + std::to_string(id) + "->" + std::to_string(num.value) + ")\n";
    }
    result += ")";
    return result;
  }

  size_t size() const { return map.size(); }

private:
  NumMap map; // The symbolic environment, a vector of Num
};

static SymEnv_t SymEnv;

static std::monostate reset_stacks() {
  Stack.reset();
  SymStack.reset();
  Frames.reset();
  SymFrames.reset();
  Memory.reset();
  SymMemory.reset();
  initRand();
  return std::monostate{};
}

inline void NodeBox::reach_here(std::function<void()> entrypoint) {
  // reach the node of exploration tree with given input (symbolic environment)
  if (auto snapshot = dynamic_cast<SnapshotNode *>(node.get())) {
    assert(REUSE_SNAPSHOT);
    auto snap = snapshot->get_snapshot();
    snap.resume_execution(this);
    return;
  } else if (parent == nullptr) {
    // if it's the root node, the only way to reach here is to reset everything
    // and start a new execution
    assert(this == ExploreTree.get_root() &&
           "Only the root node can have no parent");
    auto timer = ManagedTimer(TimeProfileKind::INSTR);
    ExploreTree.reset_cursor();
    reset_stacks();
    entrypoint();
    return;
  }
  // Reach the parent node, then from the parent node, we can reach here
  // TODO: short circuit the lookup
  parent->reach_here(entrypoint);
  return;
}

struct EvalRes {
  Num value;
  int width; // in bits
  EvalRes(Num value, int width) : value(value), width(width) {}
};

// TODO: reduce the re-computation of the same symbolic expression, it's better
// if it can be done by the smt solver
static EvalRes eval_sym_expr(const SymVal &sym, SymEnv_t &sym_env) {
  Profile.step(StepProfileKind::SYM_EVAL);
  assert(sym.symptr != nullptr && "Symbolic expression is null");
  if (auto concrete = dynamic_cast<SymConcrete *>(sym.symptr.get())) {
    return EvalRes(concrete->value, 32);
  } else if (auto extract = dynamic_cast<SymExtract *>(sym.symptr.get())) {
    auto res = eval_sym_expr(extract->value, sym_env);
    int high = extract->high;
    int low = extract->low;
    assert(high >= low && "Invalid extract range");
    int size = high - low + 1; // size in bytes
    int64_t mask = (1LL << (size * 8)) - 1;
    int64_t extracted_value = (res.value.toInt() >> ((low - 1) * 8)) & mask;
    return EvalRes(Num(I64V(extracted_value)), size * 8);
  } else if (auto smallbv = dynamic_cast<SmallBV *>(sym.symptr.get())) {
    return EvalRes(Num(I64V(smallbv->get_value())), smallbv->get_size());
  } else if (auto operation = dynamic_cast<SymBinary *>(sym.symptr.get())) {
    // If it's a operation, we need to evaluate it
    auto lhs_res = eval_sym_expr(operation->lhs, sym_env);
    auto rhs_res = eval_sym_expr(operation->rhs, sym_env);
    auto lhs = lhs_res.value;
    auto rhs = rhs_res.value;
    switch (operation->op) {
    case ADD:
      return EvalRes(lhs + rhs, 32);
    case SUB:
      return EvalRes(lhs - rhs, 32);
    case MUL:
      return EvalRes(lhs * rhs, 32);
    case DIV:
      return EvalRes(lhs / rhs, 32);
    case LT:
      return EvalRes(lhs < rhs, 32);
    case LEQ:
      return EvalRes(lhs <= rhs, 32);
    case GT:
      return EvalRes(lhs > rhs, 32);
    case GEQ:
      return EvalRes(lhs >= rhs, 32);
    case NEQ:
      return EvalRes(lhs != rhs, 32);
    case EQ:
      return EvalRes(lhs == rhs, 32);
    case B_AND:
      return EvalRes(Num(I64V(lhs.value & rhs.value)), 32);
    case CONCAT: {
      auto lhs_width = lhs_res.width;
      auto rhs_width = rhs_res.width;
      auto conc_value = (lhs.value << rhs_width) | (rhs.value);
      auto new_width = lhs_width + rhs_width;
      return EvalRes(Num(I64V(conc_value)), new_width);
    }
    default:
      assert(false && "Operation not supported in evaluation");
    }
  } else if (auto symbol = dynamic_cast<Symbol *>(sym.symptr.get())) {
    auto sym_id = symbol->get_id();
    GENSYM_INFO("Reading symbol: " + std::to_string(sym_id));
    return EvalRes(sym_env.read(sym), 32);
  }
  throw std::runtime_error("Not supported symbolic expression");
}

static void resume_conc_stack(const SymStack_t &sym_stack, Stack_t &stack,
                              SymEnv_t &sym_env) {
  stack.resize(sym_stack.size());
  for (size_t i = 0; i < sym_stack.size(); ++i) {
    auto sym = sym_stack[i];
    auto res = eval_sym_expr(sym, sym_env);
    auto conc = res.value;
    stack.set_from_front(i, conc);
  }
}

static void resume_conc_frames(const SymFrames_t &sym_frame, Frames_t &frames,
                               SymEnv_t &sym_env) {
  frames.resize(sym_frame.size());
  for (size_t i = 0; i < sym_frame.size(); ++i) {
    auto sym = sym_frame[i];
    assert(sym.symptr != nullptr);
    auto res = eval_sym_expr(sym, sym_env);
    auto conc = res.value;
    frames.set_from_front(i, conc);
  }
}

static void resume_conc_memory(const SymMemory_t &sym_memory, Memory_t &memory,
                               SymEnv_t &sym_env) {
  memory.reset();
  for (const auto &pair : sym_memory.memory) {
    int32_t addr = pair.first;
    SymVal sym = pair.second;
    assert(sym.symptr != nullptr);
    auto res = eval_sym_expr(sym, sym_env);
    auto conc = res.value;
    assert(res.width == 8 && "Memory should only store bytes");
    memory.store_byte(addr, conc.value & 0xFF);
  }
}

static void resume_conc_states(const SymStack_t &sym_stack,
                               const SymFrames_t &sym_frame,
                               const SymMemory_t &sym_memory, Stack_t &stack,
                               Frames_t &frames, Memory_t &memory,
                               SymEnv_t &sym_env) {
  resume_conc_stack(sym_stack, stack, sym_env);
  resume_conc_frames(sym_frame, frames, sym_env);
  resume_conc_memory(sym_memory, memory, sym_env);
}

inline std::monostate Snapshot_t::resume_execution(NodeBox *node) const {
  // Reset explore tree's cursor
  ExploreTree.set_cursor(node);

  // Restore the symbolic state from the snapshot
  GENSYM_INFO("Reusing symbolic state from snapshot");
  SymStack = stack;
  SymFrames = frames;
  SymMemory = memory;
  {
    auto timer = ManagedTimer(TimeProfileKind::RESUME_SNAPSHOT);
    // Restore the concrete states from the symbolic states
    resume_conc_states(stack, frames, memory, Stack, Frames, Memory, SymEnv);
  }
  int sym_size = 0;
  {
    auto timer = ManagedTimer(TimeProfileKind::COUNT_SYM_SIZE);
    for (size_t i = 0; i < stack.size(); ++i) {
      sym_size += stack[i].size();
    }
    for (size_t i = 0; i < frames.size(); ++i) {
      sym_size += frames[i].size();
    }
  }
  std::cout << "[Info] Resumed symbolic execution from snapshot, total "
               "symbolic expression and frame size: "
            << sym_size << std::endl;

  // Resume execution from the continuation
  auto timer = ManagedTimer(TimeProfileKind::INSTR);
  return cont(mcont);
}

#endif // WASM_SYMBOLIC_RT_HPP
