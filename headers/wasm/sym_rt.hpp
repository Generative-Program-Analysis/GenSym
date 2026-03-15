#ifndef WASM_SYMBOLIC_RT_HPP
#define WASM_SYMBOLIC_RT_HPP

#include "concrete_rt.hpp"
#include "config.hpp"
#include "controls.hpp"
#include "heap_mem_bookkeeper.hpp"
#include "immer/map.hpp"
#include "immer/map_transient.hpp"
#include "immer/vector.hpp"
#include "immer/vector_transient.hpp"
#include "profile.hpp"
#include "symbolic_decl.hpp"
#include "symbolic_impl.hpp"
#include "symval_decl.hpp"
#include "symval_factory.hpp"
#include "symval_impl.hpp"
#include "utils.hpp"
#include "wasm/concrete_num.hpp"
#include "wasm/z3_env.hpp"
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
    stack.erase(stack.begin() + (n - offset), stack.end());
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
    symbolic_size = 0;
  }

  size_t size() const { return stack.size(); }

  SymVal operator[](size_t index) const { return stack[index]; }

  int total_sym_size() const {
    ManagedTimer timer(TimeProfileKind::COUNT_SYM_SIZE);
    int total_size = 0;
    for (const auto &val : stack) {
      // std::cout << "Symbolic Expression: " << val->z3_expr() << "\n";
      // std::cout << "Val size: " << val.size() << "\n";
      total_size += val->size();
    }
    return total_size;
  }

private:
  int symbolic_size = 0;
#ifdef USE_IMM
  immer::vector_transient<SymVal> stack;
#else
  std::vector<SymVal> stack;
#endif
};

static SymStack_t SymStack;

class SymFrames_t {

public:
  void restore_frame_ptr(Frames_t &frame) const;

  void pushFramePtr() {
#ifdef USE_IMM
    frame_ptrs.push_back(stack.size());
#else
    frame_ptrs.push_back(stack.size());
#endif
  }

  void pushFrameSlot(int width) {
#ifdef USE_IMM
    stack.push_back(SVFactory::make_concrete_bv(I64V(0), width));
#else
    stack.emplace_back(SVFactory::make_concrete_bv(I64V(0), width));
#endif
  }

  std::monostate popFrameCaller(int size) {
    assert(size >= 0);
    assert(static_cast<size_t>(size) <= stack.size());
    assert(!frame_ptrs.empty());
    auto frame_base = current_frame_base();
    assert(frame_base + size == stack.size());

#ifdef USE_IMM
    stack.take(stack.size() - size);
#else
    stack.erase(stack.end() - size, stack.end());
#endif

#ifdef USE_IMM
    frame_ptrs.take(frame_ptrs.size() - 1);
#else
    frame_ptrs.pop_back();
#endif

    return std::monostate{};
  }

  std::monostate popFrameCallee(int size) {
    // Pop the frame of the given size
    assert(size >= 0);
    assert(static_cast<size_t>(size) <= stack.size());

#ifdef USE_IMM
    stack.take(stack.size() - size);
#else
    stack.erase(stack.end() - size, stack.end());
#endif

    return std::monostate{};
  }

  SymVal get(int index) {
    // Get the symbolic value at the given frame index
    assert(!frame_ptrs.empty());
    auto frame_base = current_frame_base();
    assert(index >= 0 &&
           static_cast<size_t>(frame_base + index) < stack.size());
    auto res = stack[frame_base + index];
    return res;
  }

  void set(int index, SymVal val) {
    // Set the symbolic value at the given index
    assert(val.symptr != nullptr);
    assert(!frame_ptrs.empty());
    auto frame_base = current_frame_base();
    assert(index >= 0 &&
           static_cast<size_t>(frame_base + index) < stack.size());
#ifdef USE_IMM
    stack.set(frame_base + index, val);
#else
    stack[frame_base + index] = val;
#endif
  }

  void reset() {
    // Reset the symbolic frames

#ifdef USE_IMM
    stack = immer::vector_transient<SymVal>();
    frame_ptrs = immer::vector_transient<size_t>();
#else
    stack.clear();
    frame_ptrs.clear();
#endif
    symbolic_size = 0;
  }

  size_t size() const { return stack.size(); }

  SymVal operator[](size_t index) const { return stack[index]; }

  int total_sym_size() const {
    ManagedTimer timer(TimeProfileKind::COUNT_SYM_SIZE);
    int total_size = 0;
    for (const auto &val : stack) {
      total_size += val->size();
    }
    return total_size;
  }

private:
  size_t current_frame_base() const {
#ifdef USE_IMM
    return *(frame_ptrs.end() - 1);
#else
    return frame_ptrs.back();
#endif
  }

  int symbolic_size = 0;
#ifdef USE_IMM
  immer::vector_transient<size_t> frame_ptrs;
  immer::vector_transient<SymVal> stack;
#else
  std::vector<size_t> frame_ptrs;
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
  int symbolic_size = 0;

  SymVal loadSymByte(int32_t addr) {
// if the address is not in the memory, it must be a zero-initialized memory
#ifdef USE_IMM
    auto it = memory.find(addr);
    if (it != nullptr) {
      return *it;
    } else {
      auto s = SVFactory::ZeroByte;
      return s;
    }
#else
    auto it = memory.find(addr);
    SymVal s = (it != memory.end()) ? it->second : SVFactory::ZeroByte;
    return s;
#endif
  }

  SymVal loadSym(int32_t base, int32_t offset) {
    // calculate the real address

#ifdef USE_IMM
    int32_t addr = base + offset;
    auto it = memory.find(addr);
    SymVal s0 = it ? *it : SVFactory::ZeroByte;
    it = memory.find(addr + 1);
    SymVal s1 = it ? *it : SVFactory::ZeroByte;
    it = memory.find(addr + 2);
    SymVal s2 = it ? *it : SVFactory::ZeroByte;
    it = memory.find(addr + 3);
    SymVal s3 = it ? *it : SVFactory::ZeroByte;

    return s3.concat(s2).concat(s1).concat(s0);
#else
    int32_t addr = base + offset;
    auto it = memory.find(addr);
    SymVal s0 = (it != memory.end()) ? it->second : SVFactory::ZeroByte;
    it = memory.find(addr + 1);
    SymVal s1 = (it != memory.end()) ? it->second : SVFactory::ZeroByte;
    it = memory.find(addr + 2);
    SymVal s2 = (it != memory.end()) ? it->second : SVFactory::ZeroByte;
    it = memory.find(addr + 3);
    SymVal s3 = (it != memory.end()) ? it->second : SVFactory::ZeroByte;

    return s3.concat(s2).concat(s1).concat(s0);
#endif
  }

  SymVal loadSymLong(int32_t base, int32_t offset) {
#ifdef USE_IMM
    int32_t addr = base + offset;
    auto it = memory.find(addr);
    SymVal s0 = it ? *it : SVFactory::ZeroByte;
    it = memory.find(addr + 1);
    SymVal s1 = it ? *it : SVFactory::ZeroByte;
    it = memory.find(addr + 2);
    SymVal s2 = it ? *it : SVFactory::ZeroByte;
    it = memory.find(addr + 3);
    SymVal s3 = it ? *it : SVFactory::ZeroByte;
    it = memory.find(addr + 4);
    SymVal s4 = it ? *it : SVFactory::ZeroByte;
    it = memory.find(addr + 5);
    SymVal s5 = it ? *it : SVFactory::ZeroByte;
    it = memory.find(addr + 6);
    SymVal s6 = it ? *it : SVFactory::ZeroByte;
    it = memory.find(addr + 7);
    SymVal s7 = it ? *it : SVFactory::ZeroByte;
#else
    int32_t addr = base + offset;
    auto it = memory.find(addr);
    SymVal s0 = (it != memory.end()) ? it->second : SVFactory::ZeroByte;
    it = memory.find(addr + 1);
    SymVal s1 = (it != memory.end()) ? it->second : SVFactory::ZeroByte;
    it = memory.find(addr + 2);
    SymVal s2 = (it != memory.end()) ? it->second : SVFactory::ZeroByte;
    it = memory.find(addr + 3);
    SymVal s3 = (it != memory.end()) ? it->second : SVFactory::ZeroByte;
    it = memory.find(addr + 4);
    SymVal s4 = (it != memory.end()) ? it->second : SVFactory::ZeroByte;
    it = memory.find(addr + 5);
    SymVal s5 = (it != memory.end()) ? it->second : SVFactory::ZeroByte;
    it = memory.find(addr + 6);
    SymVal s6 = (it != memory.end()) ? it->second : SVFactory::ZeroByte;
    it = memory.find(addr + 7);
    SymVal s7 = (it != memory.end()) ? it->second : SVFactory::ZeroByte;
#endif

    return s7.concat(s6)
        .concat(s5)
        .concat(s4)
        .concat(s3)
        .concat(s2)
        .concat(s1)
        .concat(s0);
  }

  SymVal loadSymFloat(int32_t base, int32_t offset) {
    // For simplicity, we treat float as concrete value for now
    auto symbv = loadSym(base, offset);
    assert(symbv.is_concrete() && "Currently only support concrete symbolic "
                                  "value for float-point values");
    if (auto concrete = dynamic_cast<SymConcrete *>(symbv.symptr.get())) {
      auto value = concrete->value;
      return SVFactory::make_concrete_fp(value, 32);
    } else {
      assert(false && "unreachable");
    }
  }

  SymVal loadSymDouble(int32_t base, int32_t offset) {
    // For simplicity, we treat double as concrete value for now
    auto symbv = loadSymLong(base, offset);
    assert(symbv.is_concrete() && "Currently only support concrete symbolic "
                                  "value for float-point values");
    if (auto concrete = dynamic_cast<SymConcrete *>(symbv.symptr.get())) {
      auto value = concrete->value;
      return SVFactory::make_concrete_fp(value, 64);
    } else {
      assert(false && "unreachable");
    }
  }

  SymVal loadSymInt8U(int32_t base, int32_t offset) {
    return SVFactory::make_smallbv(24, 0).concat(loadSymByte(base + offset));
  }

  SymVal loadSymInt8S(int32_t base, int32_t offset) {
    auto value = loadSymInt8U(base, offset);
    auto shift = SVFactory::make_concrete_bv(I32V(24), 32);
    return value.shl(shift).shr_s(shift);
  }

  SymVal loadSymInt16U(int32_t base, int32_t offset) {
    auto low = loadSymByte(base + offset);
    auto high = loadSymByte(base + offset + 1);
    return SVFactory::make_smallbv(16, 0).concat(high).concat(low);
  }

  SymVal loadSymInt16S(int32_t base, int32_t offset) {
    auto value = loadSymInt16U(base, offset);
    auto shift = SVFactory::make_concrete_bv(I32V(16), 32);
    return value.shl(shift).shr_s(shift);
  }

  SymVal loadSymLong8U(int32_t base, int32_t offset) {
    return SVFactory::make_smallbv(56, 0).concat(loadSymByte(base + offset));
  }

  SymVal loadSymLong8S(int32_t base, int32_t offset) {
    auto value = loadSymLong8U(base, offset);
    auto shift = SVFactory::make_concrete_bv(I64V(56), 64);
    return value.shl(shift).shr_s(shift);
  }

  SymVal loadSymLong16U(int32_t base, int32_t offset) {
    auto low = loadSymByte(base + offset);
    auto high = loadSymByte(base + offset + 1);
    return SVFactory::make_smallbv(48, 0).concat(high).concat(low);
  }

  SymVal loadSymLong16S(int32_t base, int32_t offset) {
    auto value = loadSymLong16U(base, offset);
    auto shift = SVFactory::make_concrete_bv(I64V(48), 64);
    return value.shl(shift).shr_s(shift);
  }

  SymVal loadSymLong32U(int32_t base, int32_t offset) {
    auto b0 = loadSymByte(base + offset);
    auto b1 = loadSymByte(base + offset + 1);
    auto b2 = loadSymByte(base + offset + 2);
    auto b3 = loadSymByte(base + offset + 3);
    return SVFactory::make_smallbv(32, 0)
        .concat(b3)
        .concat(b2)
        .concat(b1)
        .concat(b0);
  }

  SymVal loadSymLong32S(int32_t base, int32_t offset) {
    auto value = loadSymLong32U(base, offset);
    auto shift = SVFactory::make_concrete_bv(I64V(32), 64);
    return value.shl(shift).shr_s(shift);
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
    storeSymByte(addr, s0);
    storeSymByte(addr + 1, s1);
    storeSymByte(addr + 2, s2);
    storeSymByte(addr + 3, s3);
    return std::monostate{};
  }

  std::monostate storeSymLong(int32_t base, int32_t offset, SymVal value) {
    int32_t addr = base + offset;
    // TODO: Can we receive a float point symbolic value here? which may produce
    // a bug
    SymVal s0 = value.extract(1, 1);
    SymVal s1 = value.extract(2, 2);
    SymVal s2 = value.extract(3, 3);
    SymVal s3 = value.extract(4, 4);
    SymVal s4 = value.extract(5, 5);
    SymVal s5 = value.extract(6, 6);
    SymVal s6 = value.extract(7, 7);
    SymVal s7 = value.extract(8, 8);
    storeSymByte(addr, s0);
    storeSymByte(addr + 1, s1);
    storeSymByte(addr + 2, s2);
    storeSymByte(addr + 3, s3);
    storeSymByte(addr + 4, s4);
    storeSymByte(addr + 5, s5);
    storeSymByte(addr + 6, s6);
    storeSymByte(addr + 7, s7);
    return std::monostate{};
  }

  std::monostate storeSymInt8(int32_t base, int32_t offset, SymVal value) {
    int32_t addr = base + offset;
    storeSymByte(addr, value.extract(1, 1));
    return std::monostate{};
  }

  std::monostate storeSymInt16(int32_t base, int32_t offset, SymVal value) {
    int32_t addr = base + offset;
    storeSymByte(addr, value.extract(1, 1));
    storeSymByte(addr + 1, value.extract(2, 2));
    return std::monostate{};
  }

  std::monostate storeSymLong8(int32_t base, int32_t offset, SymVal value) {
    int32_t addr = base + offset;
    storeSymByte(addr, value.extract(1, 1));
    return std::monostate{};
  }

  std::monostate storeSymLong16(int32_t base, int32_t offset, SymVal value) {
    int32_t addr = base + offset;
    storeSymByte(addr, value.extract(1, 1));
    storeSymByte(addr + 1, value.extract(2, 2));
    return std::monostate{};
  }

  std::monostate storeSymLong32(int32_t base, int32_t offset, SymVal value) {
    int32_t addr = base + offset;
    storeSymByte(addr, value.extract(1, 1));
    storeSymByte(addr + 1, value.extract(2, 2));
    storeSymByte(addr + 2, value.extract(3, 3));
    storeSymByte(addr + 3, value.extract(4, 4));
    return std::monostate{};
  }

  std::monostate storeSymFloat(int32_t base, int32_t offset, SymVal value) {
    assert(value.is_concrete() && "Currently only support concrete symbolic "
                                  "value for float-point values");
    return storeSym(base, offset, value);
  }

  std::monostate storeSymDouble(int32_t base, int32_t offset, SymVal value) {
    assert(value.is_concrete() && "Currently only support concrete symbolic "
                                  "value for float-point values");
    return storeSymLong(base, offset, value);
  }

  std::monostate storeSymByte(int32_t addr, SymVal value) {
    // assume the input value is 8-bit symbolic value
    bool exists;
#ifdef USE_IMM
    auto it = memory.find(addr);
    exists = (it != nullptr);
#else
    auto it = memory.find(addr);
    exists = (it != memory.end());
#endif
    auto old_value = loadSymByte(addr);
#ifdef USE_IMM
    memory.set(addr, value);
#else
    auto inserted = memory.insert({addr, value});
    if (!inserted.second) {
      inserted.first->second = value;
    }
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

  int total_sym_size() const {
    ManagedTimer timer(TimeProfileKind::COUNT_SYM_SIZE);
    int total_size = 0;
    for (const auto &[_, val] : memory) {
      total_size += val->size();
    }
    return total_size;
  }
};

inline void SymFrames_t::restore_frame_ptr(Frames_t &frame) const {
  frame.frame_ptrs = frame_ptrs;
}

static SymMemory_t SymMemory;

static std::monostate memoryInitialize(int32_t offset,
                                       const std::string &data) {
  // initialize concrete memory
  for (size_t i = 0; i < data.size(); ++i) {
    Memory.storeInt(offset, i, static_cast<uint8_t>(data[i]));
  }
  // initialize symbolic memory
  for (size_t i = 0; i < data.size(); ++i) {
    SymMemory.storeSymByte(
        offset + i, SVFactory::make_smallbv(8, static_cast<uint8_t>(data[i])));
  }
  return {};
}

using NumMap = std::unordered_map<int, Num>;

// TODO: remove this class later
class ImmNumMapBox {
public:
  ImmNumMapBox(const NumMap &sym_env)
      : map_ptr(std::make_shared<NumMap>(
            sym_env) /* create a immutable copy of SymEnv */
        ) {}

  const NumMap *operator->() const { return map_ptr.get(); }
  const NumMap &operator*() const { return *map_ptr; }

private:
  std::shared_ptr<NumMap> map_ptr;
};

class SymEnv_t {
public:
  SymEnv_t() : map(), imm_map_box(map) {}

  Num read(const Symbol &symbol) const {
#if DEBUG
    std::cout << "Read symbol: " << symbol.get_id()
              << " from symbolic environment" << std::endl;
    std::cout << "Current symbolic environment: " << to_string() << std::endl;
#endif
    if (map.find(symbol.get_id()) == map.end()) {
      return Num(I32V(0));
    }
    return map.at(symbol.get_id());
  }

  Num read(SymVal sym) {
    // Read the value of a symbolic value from the environment, it will update
    // the environment if the key does not exist.
    auto symbol = dynamic_cast<Symbol *>(sym.symptr.get());
    assert(symbol);
    return read(*symbol);
  }

  void update(NumMap new_env) {
    map = std::move(new_env);
    imm_map_box = ImmNumMapBox(map);
  }

  // Absorb another symbolic environment into this one, if some keys not exist
  // in another environment and exist in this one, they will be kept unchanged.
  void absorb(const NumMap &other) {
    for (const auto &[id, num] : other) {
      map[id] = num;
    }
    imm_map_box = ImmNumMapBox(map);
  }

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

  ImmNumMapBox get_num_map() const { return imm_map_box; }

private:
  NumMap map; // The symbolic environment, a vector of Num
  ImmNumMapBox imm_map_box;
};

static SymEnv_t SymEnv;

inline Num isSymbolic(int index) {
  auto it = SVFactory::SymbolStore.find(index);
  if (it != SVFactory::SymbolStore.end()) {
    return Num(I32V(1));
  } else {
    return Num(I32V(0));
  }
}

// A snapshot of the symbolic state and execution context (control)
class Snapshot_t {
public:
  explicit Snapshot_t(Cont_t cont, MCont_t mcont, SymStack_t stack,
                      SymFrames_t frames,
                      SymFrames_t globals, SymMemory_t memory, ImmNumMapBox num_map /* Current num map that corresponds to the symbolic environment */);

  SymStack_t get_stack() const { return stack; }
  SymFrames_t get_frames() const { return frames; }
  SymFrames_t get_globals() const { return globals; }
  SymMemory_t get_memory() const { return memory; }

  std::monostate resume_execution(NodeBox *node) const;
  std::monostate resume_execution_by_model(NodeBox *node,
                                           z3::model &model) const;

  double cost_of_snapshot() const;

private:
  SymStack_t stack;
  SymFrames_t frames;
  SymFrames_t globals;
  SymMemory_t memory;
  // The continuation at the snapshot point
  Cont_t cont;
  MCont_t mcont;
  ImmNumMapBox num_map;
  void restore_states_to_global() const;
};

static SymFrames_t SymFrames;
static SymFrames_t SymGlobals;

static Control makeControl(Cont_t cont, MCont_t mcont) {
  return Control(cont, mcont);
}

static Snapshot_t makeSnapshot(Control control) {
  // create a snapshot from the current symbolic states and the control
  return Snapshot_t(control.cont, control.mcont, SymStack, SymFrames,
                    SymGlobals, SymMemory, SymEnv.get_num_map());
}

struct Node;

struct NodeBox {
  explicit NodeBox(NodeBox *parent);
  std::unique_ptr<Node> node;
  NodeBox *parent;
  double instr_cost() const;

  bool fillIfElseNode(SymVal cond, int id);
  bool fillCallIndirectNode(SymVal cond, int id);
  std::monostate fillFinishedNode();
  std::monostate fillFailedNode();
  std::monostate fillUnreachableNode();
  std::monostate fillSnapshotNode(Snapshot_t snapshot);
  std::monostate fillNotToExploreNode();
  bool isUnexplored() const;
  bool isSnapshotNode() const;
  std::vector<SymVal> collect_path_conds();
  immer::vector<SymVal> collect_path_conds_imm();

  void reach_here(std::function<void()>);

  Node *operator->() {
    assert(node != nullptr && "Accessing an empty NodeBox");
    return node.get();
  }
};

struct Node {
  friend struct NodeBox;
  virtual ~Node() {};
  void set_cost(double c) { instr_cost = c; }
  double get_cost() const { return instr_cost; }
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

private:
  double instr_cost = 0.0;
  std::optional<immer::vector<SymVal>> path_conds_cache;
};

inline double NodeBox::instr_cost() const {
  if (node) {
    return node->get_cost();
  } else {
    return 0.0;
  }
}

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

struct CallIndirectNode : Node {
  SymVal cond;
  std::unordered_map<int, std::unique_ptr<NodeBox>> branches;
  std::unique_ptr<NodeBox> otherwise_branch;
  int id;
  CallIndirectNode(SymVal cond, NodeBox *parent, int id)
      : cond(cond), id(id),
        otherwise_branch(std::make_unique<NodeBox>(parent)) {}
  std::string to_string() override {
    std::string result = "CallIndirectNode {\n";
    for (const auto &pair : branches) {
      result += "  branch " + std::to_string(pair.first) + ": ";
      if (pair.second && pair.second->node) {
        result += pair.second->node->to_string();
      } else {
        result += "nullptr";
      }
      result += "\n";
    }
    result += "}";
    return result;
  }

  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override {
    int current_node_dot_id = current_id;
    current_id += 1;

    graphviz_node(os, current_node_dot_id, "Branch", "diamond", "lightyellow");

    // Draw edge from parent if this is not the root node
    if (parent_dot_id != -1) {
      graphviz_edge(os, parent_dot_id, current_node_dot_id, edge_label);
    }
    for (const auto &pair : branches) {
      assert(pair.second != nullptr);
      assert(pair.second->node != nullptr);
      pair.second->node->generate_dot(os, current_node_dot_id,
                                      "branch " + std::to_string(pair.first));
    }
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

  bool worth_to_reuse() const {
    if (!ENABLE_COST_MODEL) {
      // If we are not using cost model, always create snapshot
      return REUSE_SNAPSHOT;
    }
    // find out the best way to reach the current position via our cost model
    auto snapshot_cost = snapshot.cost_of_snapshot();
    double re_execution_cost = get_cost();
    // std::cout << "Snapshot cost: " << snapshot_cost
    //           << ", re-execution cost: " << re_execution_cost << std::endl;
    if (snapshot_cost <= re_execution_cost) {
      GENSYM_INFO("Snapshot is worth to create");
    } else {
      GENSYM_INFO("Snapshot is NOT worth to create");
    }
    return snapshot_cost <= re_execution_cost;
  }

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

inline bool NodeBox::fillIfElseNode(SymVal cond, int id) {
  // fill the current NodeBox with an ifelse branch node when it's unexplored
  double cost_from_parent = CostManager.dump_instr_cost();
  double cost_from_root =
      cost_from_parent + (this->parent ? this->parent->instr_cost() : 0);
  // std::cout << "Cost from parent: " << cost_from_parent
  //           << ", cost from root: " << cost_from_root << std::endl;

  if (auto ptr = dynamic_cast<SnapshotNode *>(node.get())) {
    node = std::make_unique<IfElseNode>(cond, this, id);
    node->set_cost(cost_from_root);
    return true;
  } else if (dynamic_cast<UnExploredNode *>(node.get())) {
    node = std::make_unique<IfElseNode>(cond, this, id);
    node->set_cost(cost_from_root);
    return true;
  } else if (dynamic_cast<NotToExploreNode *>(node.get()) != nullptr) {
    assert(false &&
           "Unexpected traversal: arrived at a node marked 'NotToExplore'.");
    return false;
  }

  node->set_cost(cost_from_root);
  assert(
      dynamic_cast<IfElseNode *>(node.get()) != nullptr &&
      "Current node is not an Unexplored nor an IfElseNode, cannot fill it!");
  return false;
}

inline bool NodeBox::fillCallIndirectNode(SymVal cond, int id) {
  // fill the current NodeBox with a call_indirect branch node when it's
  // unexplored
  if (auto ptr = dynamic_cast<SnapshotNode *>(node.get())) {
    node = std::make_unique<CallIndirectNode>(cond, this, id);
    return true;
  } else if (dynamic_cast<UnExploredNode *>(node.get())) {
    node = std::make_unique<CallIndirectNode>(cond, this, id);
    return true;
  } else if (dynamic_cast<NotToExploreNode *>(node.get()) != nullptr) {
    assert(false &&
           "Unexpected traversal: arrived at a node marked 'NotToExplore'.");
    return false;
  }

  assert(
      dynamic_cast<CallIndirectNode *>(node.get()) != nullptr &&
      "Current node is not an Unexplored nor a CallIndirectNode, cannot fill "
      "it!");
  return false;
}

inline std::monostate NodeBox::fillSnapshotNode(Snapshot_t snapshot) {
  if (this->isUnexplored()) {
    node = std::make_unique<SnapshotNode>(snapshot);
  }
  node->set_cost(parent->instr_cost());
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

inline bool NodeBox::isSnapshotNode() const {
  assert(node != nullptr);
  return dynamic_cast<SnapshotNode *>(node.get()) != nullptr;
}

inline bool NodeBox::isUnexplored() const {
  assert(node != nullptr);
  if (dynamic_cast<UnExploredNode *>(node.get()) != nullptr) {
    return true;
  }
  if (this->isSnapshotNode()) {
    return true;
  }
  return false;
}

inline std::vector<SymVal> NodeBox::collect_path_conds() {
  ManagedTimer timer(TimeProfileKind::COLLECT_PATH_CONDITIONS);
  auto box = this;
  auto result = std::vector<SymVal>();
  while (box->parent) {
    auto parent = box->parent;
    if (auto if_else_node = dynamic_cast<IfElseNode *>(parent->node.get())) {
      if (if_else_node->true_branch.get() == box) {
        // If the current box is the true branch, add the condition
        result.push_back(if_else_node->cond);
      } else if (if_else_node->false_branch.get() == box) {
        // If the current box is the false branch, add the negated condition
        result.push_back(if_else_node->cond.bv_negate().bool2bv());
      } else {
        throw std::runtime_error("Unexpected node structure in explore tree");
      }
    } else if (auto call_indirect_node =
                   dynamic_cast<CallIndirectNode *>(parent->node.get())) {
      // Find which branch we are in
      bool found = false;
      for (const auto &pair : call_indirect_node->branches) {
        if (pair.second.get() == box) {
          // We are in this branch
          // Add the condition that leads to this branch
          result.push_back(
              call_indirect_node->cond.eq(Concrete(I32V(pair.first), 32)));
          found = true;
          break;
        }
      }
      if (!found) {
        // We must be in the otherwise branch
        if (call_indirect_node->otherwise_branch.get() != box) {
          throw std::runtime_error("Unexpected node structure in explore tree");
        }
        // Add the negated conditions for all other branches
        SymVal negated_conditions = Concrete(I32V(1), 32); // true
        for (const auto &pair : call_indirect_node->branches) {
          negated_conditions = negated_conditions.bitwise_and(
              call_indirect_node->cond.neq(Concrete(I32V(pair.first), 32)));
        }
        result.push_back(negated_conditions);
      }
    } else {
      // should never reach here
    }
    // Move to parent
    box = box->parent;
  }
  return result;
}

// same as collect_path_conds but return immer::vector, and cache the result
inline immer::vector<SymVal> NodeBox::collect_path_conds_imm() {
  ManagedTimer timer(TimeProfileKind::COLLECT_PATH_CONDITIONS);

  auto box = this;
  if (box->node->path_conds_cache.has_value()) {
    return box->node->path_conds_cache.value();
  }

  if (!box->parent) {
    // root node, and no path conditions
    immer::vector<SymVal> empty;
    box->node->path_conds_cache = empty;
    return empty;
  }

  auto parent_conds = box->parent->collect_path_conds_imm();
  immer::vector<SymVal> result = parent_conds;
  if (auto if_else_node = dynamic_cast<IfElseNode *>(box->parent->node.get())) {
    if (if_else_node->true_branch.get() == box) {
      // If the current box is the true branch, add the condition
      result = result.push_back(if_else_node->cond);
    } else if (if_else_node->false_branch.get() == box) {
      // If the current box is the false branch, add the negated condition
      result = result.push_back(if_else_node->cond.bv_negate().bool2bv());
    } else {
      throw std::runtime_error("Unexpected node structure in explore tree");
    }
  } else if (auto call_indirect_node =
                 dynamic_cast<CallIndirectNode *>(box->parent->node.get())) {
    // Find which branch we are in
    bool found = false;
    for (const auto &pair : call_indirect_node->branches) {
      if (pair.second.get() == box) {
        // We are in this branch
        // Add the condition that leads to this branch
        result = result.push_back(
            call_indirect_node->cond.eq(Concrete(I32V(pair.first), 32)));
        found = true;
        break;
      }
    }
    if (!found) {
      // We must be in the otherwise branch
      if (call_indirect_node->otherwise_branch.get() != box) {
        throw std::runtime_error("Unexpected node structure in explore tree");
      }
      // Add the negated conditions for all other branches
      SymVal negated_conditions = Concrete(I32V(1), 32); // true
      for (const auto &pair : call_indirect_node->branches) {
        negated_conditions = negated_conditions.bitwise_and(
            call_indirect_node->cond.neq(Concrete(I32V(pair.first), 32)));
      }
      result = result.push_back(negated_conditions);
    }
  } else {
    // should never reach here
  }
  box->node->path_conds_cache = result;
  return result;
}

inline Snapshot_t::Snapshot_t(Cont_t cont, MCont_t mcont, SymStack_t stack,
                              SymFrames_t frames, SymFrames_t globals,
                              SymMemory_t memory, ImmNumMapBox num_map)
    : stack(std::move(stack)), frames(std::move(frames)),
      globals(std::move(globals)), memory(std::move(memory)), cont(cont),
      mcont(mcont), num_map(num_map) {
  Profile.step(StepProfileKind::SNAPSHOT_CREATE);
#ifdef DEBUG
  std::cout << "Creating snapshot of size " << stack.size() << std::endl;
#endif
}

const double INSTR_COST_SCALING_FACTOR = 1E-03;

inline double Snapshot_t::cost_of_snapshot() const {
  auto stack_sym_size = stack.total_sym_size();
  assert(stack_sym_size >= 0);
  auto frame_sym_size = frames.total_sym_size();
  assert(frame_sym_size >= 0);
  auto memory_sym_size = memory.total_sym_size();
  assert(memory_sym_size >= 0);
  auto global_sym_size = globals.total_sym_size();
  assert(global_sym_size >= 0);
  // The speed ratio between symbolic expression instantiation and WebAssembly
  // instruction execution, given by benchmark results
  auto total_size =
      stack_sym_size + frame_sym_size + memory_sym_size + global_sym_size;
  return INSTR_COST_SCALING_FACTOR * total_size;
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

  std::monostate fillCallIndirectNode(SymVal cond, int id) {
    if (cursor->fillCallIndirectNode(cond, id)) {
      auto indirect_node = dynamic_cast<CallIndirectNode *>(cursor->node.get());
      register_new_node(indirect_node->otherwise_branch.get());
    }
    return std::monostate();
  }

  std::monostate fillNotToExploredNode() {
    return cursor->fillNotToExploreNode();
  }

  std::vector<SymVal> collect_current_path_conds() {
    return cursor->collect_path_conds();
  }

  std::monostate moveCursor(bool branch, Control control) {
    Profile.step(StepProfileKind::CURSOR_MOVE);
    assert(cursor != nullptr);
    auto if_else_node = dynamic_cast<IfElseNode *>(cursor->node.get());
    assert(
        if_else_node != nullptr &&
        "Can't move cursor when the branch node is not initialized correctly!");

    if (branch) {
      true_branch_cov_map[if_else_node->id] = true;
      if (if_else_node->cond.is_concrete()) {
        if_else_node->false_branch->fillUnreachableNode();
      } else {
        if (REUSE_SNAPSHOT && !if_else_node->false_branch->isSnapshotNode()) {
          auto snapshot = makeSnapshot(control);
          if_else_node->false_branch->fillSnapshotNode(snapshot);
        } else {
          // Do nothing, the initial value of the branch is an unexplored node
        }
      }
      cursor = if_else_node->true_branch.get();
    } else {
      false_branch_cov_map[if_else_node->id] = true;
      if (if_else_node->cond.is_concrete()) {
        if_else_node->true_branch->fillUnreachableNode();
      } else {
        if (REUSE_SNAPSHOT && !if_else_node->true_branch->isSnapshotNode()) {
          auto snapshot = makeSnapshot(control);
          if_else_node->true_branch->fillSnapshotNode(snapshot);
        } else {
          // Do nothing, the initial value of the branch is an unexplored node
        }
      }
      cursor = if_else_node->false_branch.get();
    }
    CostManager.reset_timer();
    return std::monostate();
  }

  std::monostate moveCursorNoControl(bool branch) {
    Profile.step(StepProfileKind::CURSOR_MOVE);
    assert(cursor != nullptr);
    auto if_else_node = dynamic_cast<IfElseNode *>(cursor->node.get());
    assert(
        if_else_node != nullptr &&
        "Can't move cursor when the branch node is not initialized correctly!");
    if (branch) {
      true_branch_cov_map[if_else_node->id] = true;
      if_else_node->false_branch->fillNotToExploreNode();
      cursor = if_else_node->true_branch.get();
    } else {
      assert(false &&
             "moveCursorNoControl should not be used for false branch");
    }
    CostManager.reset_timer();
    return std::monostate();
  }

  std::monostate moveCursorIndirect(int branch_index) {
    // Dont use snapshot reuse for untaken branches of indirect call
    Profile.step(StepProfileKind::CURSOR_MOVE);
    assert(cursor != nullptr);
    auto branch_node = dynamic_cast<CallIndirectNode *>(cursor->node.get());
    assert(branch_node != nullptr &&
           "Can't move cursor when the branch node is not initialized ");
    if (branch_node->branches.find(branch_index) ==
        branch_node->branches.end()) {
      // Create a new branch
      branch_node->branches[branch_index] = std::make_unique<NodeBox>(cursor);
      register_new_node(branch_node->branches[branch_index].get());
    }
    cursor = branch_node->branches[branch_index].get();

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
    std::vector<NodeBox *> stack;
    stack.push_back(root.get());

    while (!stack.empty()) {
      NodeBox *node = stack.back();
      stack.pop_back();

      if (auto if_else_node = dynamic_cast<IfElseNode *>(node->node.get())) {
        stack.push_back(if_else_node->true_branch.get());
        stack.push_back(if_else_node->false_branch.get());
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
      } else if (auto call_indirect_node =
                     dynamic_cast<CallIndirectNode *>(node->node.get())) {
        for (const auto &pair : call_indirect_node->branches) {
          stack.push_back(pair.second.get());
        }
        stack.push_back(call_indirect_node->otherwise_branch.get());
      } else {
        throw std::runtime_error("Unknown node type in explore tree");
      }
    }
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

[[deprecated]] inline void
NodeBox::reach_here(std::function<void()> entrypoint) {
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
  ValueKind kind;
  int width; // in bits
  EvalRes(Num value, int width, ValueKind kind)
      : value(value), width(width), kind(kind) {}
};

static EvalRes eval_binary_op(EvalRes lhs_res, EvalRes rhs_res,
                              BinOperation operation) {
  auto lhs = lhs_res.value;
  auto rhs = rhs_res.value;
  auto lhs_width = lhs_res.width;
  auto rhs_width = rhs_res.width;
  switch (operation) {
  case ADD:
    if (lhs_width == 32 && rhs_width == 32) {
      return EvalRes(lhs.i32_add(rhs), 32, KindBV);
    } else if (lhs_width == 64 && rhs_width == 64) {
      return EvalRes(lhs.i64_add(rhs), 64, KindBV);
    } else {
      assert(false && "TODO");
    }
  case SUB:
    if (lhs_width == 32 && rhs_width == 32) {
      return EvalRes(lhs.i32_sub(rhs), 32, KindBV);
    } else if (lhs_width == 64 && rhs_width == 64) {
      return EvalRes(lhs.i64_sub(rhs), 64, KindBV);
    } else {
      assert(false && "TODO");
    }
  case MUL:
    if (lhs_width == 32 && rhs_width == 32) {
      return EvalRes(lhs.i32_mul(rhs), 32, KindBV);
    } else if (lhs_width == 64 && rhs_width == 64) {
      return EvalRes(lhs.i64_mul(rhs), 64, KindBV);
    } else {
      assert(false && "TODO");
    }
  case DIV:
    if (lhs_width == 32 && rhs_width == 32) {
      return EvalRes(lhs.i32_div_s(rhs), 32, KindBV);
    } else if (lhs_width == 64 && rhs_width == 64) {
      return EvalRes(lhs.i64_div_s(rhs), 64, KindBV);
    } else {
      assert(false && "TODO");
    }
  case LT_BOOL:
    if (lhs_width == 32 && rhs_width == 32) {
      return EvalRes(lhs.i32_lt_s(rhs), 32, KindBool);
    } else if (lhs_width == 64 && rhs_width == 64) {
      return EvalRes(lhs.i64_lt_s(rhs), 32, KindBool);
    } else {
      assert(false && "TODO");
    }
  case LEQ_BOOL:
    if (lhs_width == 32 && rhs_width == 32) {
      return EvalRes(lhs.i32_le_s(rhs), 32, KindBool);
    } else if (lhs_width == 64 && rhs_width == 64) {
      return EvalRes(lhs.i64_le_s(rhs), 32, KindBool);
    } else {
      assert(false && "TODO");
    }
  case GT_BOOL:
    if (lhs_width == 32 && rhs_width == 32) {
      return EvalRes(lhs.i32_gt_s(rhs), 32, KindBool);
    } else if (lhs_width == 64 && rhs_width == 64) {
      return EvalRes(lhs.i64_gt_s(rhs), 32, KindBool);
    } else {
      assert(false && "TODO");
    }
  case GEQ_BOOL:
    if (lhs_width == 32 && rhs_width == 32) {
      return EvalRes(lhs.i32_ge_s(rhs), 32, KindBool);
    } else if (lhs_width == 64 && rhs_width == 64) {
      return EvalRes(lhs.i64_ge_s(rhs), 32, KindBool);
    } else {
      assert(false && "TODO");
    }
  case NEQ_BOOL:
    if (lhs_width == 32 && rhs_width == 32) {
      return EvalRes(lhs.i32_ne(rhs), 32, KindBool);
    } else if (lhs_width == 64 && rhs_width == 64) {
      return EvalRes(lhs.i64_ne(rhs), 32, KindBool);
    } else {
      assert(false && "TODO");
    }
  case EQ_BOOL:
    if (lhs_width == 32 && rhs_width == 32) {
      return EvalRes(lhs.i32_eq(rhs), 32, KindBool);
    } else if (lhs_width == 64 && rhs_width == 64) {
      return EvalRes(lhs.i64_eq(rhs), 32, KindBool);
    } else {
      assert(false && "TODO");
    }
  case B_AND:
    if (lhs_width == 32 && rhs_width == 32) {
      return EvalRes(lhs.i32_and(rhs), 32, KindBV);
    } else if (lhs_width == 64 && rhs_width == 64) {
      return EvalRes(lhs.i64_and(rhs), 64, KindBV);
    } else {
      assert(false && "TODO");
    }
  case CONCAT: {
    auto conc_value = (lhs.value << rhs_width) | (rhs.value);
    auto new_width = lhs_width + rhs_width;
    return EvalRes(Num(I64V(conc_value)), new_width, KindBV);
  }
  case B_XOR:
    if (lhs_width == 32 && rhs_width == 32) {
      return EvalRes(lhs.i32_xor(rhs), 32, KindBV);
    } else if (lhs_width == 64 && rhs_width == 64) {
      return EvalRes(lhs.i64_xor(rhs), 64, KindBV);
    } else {
      assert(false && "TODO");
    }
  case B_OR:
    if (lhs_width == 32 && rhs_width == 32) {
      return EvalRes(lhs.i32_or(rhs), 32, KindBV);
    } else if (lhs_width == 64 && rhs_width == 64) {
      return EvalRes(lhs.i64_or(rhs), 64, KindBV);
    } else {
      assert(false && "TODO");
    }
  case SHR_U:
    if (lhs_width == 32 && rhs_width == 32) {
      return EvalRes(lhs.i32_shr_u(rhs), 32, KindBV);
    } else if (lhs_width == 64 && rhs_width == 64) {
      return EvalRes(lhs.i64_shr_u(rhs), 64, KindBV);
    } else {
      assert(false && "TODO");
    }
  case SHR_S:
    if (lhs_width == 32 && rhs_width == 32) {
      return EvalRes(lhs.i32_shr_s(rhs), 32, KindBV);
    } else if (lhs_width == 64 && rhs_width == 64) {
      return EvalRes(lhs.i64_shr_s(rhs), 64, KindBV);
    } else {
      assert(false && "TODO");
    }
  case LTU_BOOL:
    if (lhs_width == 32 && rhs_width == 32) {
      return EvalRes(lhs.i32_lt_u(rhs), 32, KindBool);
    } else if (lhs_width == 64 && rhs_width == 64) {
      return EvalRes(lhs.i64_lt_u(rhs), 32, KindBool);
    } else {
      assert(false && "TODO");
    }
  case GTU_BOOL:
    if (lhs_width == 32 && rhs_width == 32) {
      return EvalRes(lhs.i32_gt_u(rhs), 32, KindBool);
    } else if (lhs_width == 64 && rhs_width == 64) {
      return EvalRes(lhs.i64_gt_u(rhs), 32, KindBool);
    } else {
      assert(false && "TODO");
    }
  case GEU_BOOL:
    if (lhs_width == 32 && rhs_width == 32) {
      return EvalRes(lhs.i32_ge_u(rhs), 32, KindBool);
    } else if (lhs_width == 64 && rhs_width == 64) {
      return EvalRes(lhs.i64_ge_u(rhs), 32, KindBool);
    } else {
      assert(false && "TODO");
    }
  case AND:
    return EvalRes(lhs.logical_and(rhs), 32, KindBool);
  case OR:
    return EvalRes(lhs.logical_or(rhs), 32, KindBool);
  default:
    assert(false && "Operation not supported in evaluation");
  }
}

// TODO: reduce the re-computation of the same symbolic expression, it's better
// if it can be done by the smt solver
static EvalRes eval_sym_expr(const SymVal &sym, const SymEnv_t &sym_env) {
  Profile.step(StepProfileKind::SYM_EVAL);
  assert(sym.symptr != nullptr && "Symbolic expression is null");
  if (auto concrete = dynamic_cast<SymConcrete *>(sym.symptr.get())) {
    return EvalRes(concrete->value, concrete->width(), concrete->kind);
  } else if (auto extract = dynamic_cast<SymExtract *>(sym.symptr.get())) {
    auto res = eval_sym_expr(extract->value, sym_env);
    int high = extract->high;
    int low = extract->low;
    assert(high >= low && "Invalid extract range");
    int size = high - low + 1; // size in bytes
    int64_t mask = (1LL << (size * 8)) - 1;
    int64_t extracted_value = (res.value.toInt() >> ((low - 1) * 8)) & mask;
    return EvalRes(Num(I64V(extracted_value)), size * 8, KindBV);
  } else if (auto operation = dynamic_cast<SymBinary *>(sym.symptr.get())) {
    // If it's a operation, we need to evaluate it
    auto lhs_res = eval_sym_expr(operation->lhs, sym_env);
    auto rhs_res = eval_sym_expr(operation->rhs, sym_env);
    auto lhs = lhs_res.value;
    auto rhs = rhs_res.value;
    auto lhs_width = lhs_res.width;
    auto rhs_width = rhs_res.width;
    return eval_binary_op(lhs_res, rhs_res, operation->op);
  } else if (auto symbol = dynamic_cast<Symbol *>(sym.symptr.get())) {
    auto sym_id = symbol->get_id();
    GENSYM_INFO("Reading symbol: " + std::to_string(sym_id));
    return EvalRes(sym_env.read(*symbol), 32, KindBV);
  }
  throw std::runtime_error("Not supported symbolic expression");
}

inline EvalRes eval_sym_expr_by_model(const SymVal &sym, z3::model &model);

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

static void resume_conc_stack_by_model(const SymStack_t &sym_stack,
                                       Stack_t &stack, z3::model &model) {
  GENSYM_INFO("Restoring concrete stack from symbolic stack");
  stack.resize(sym_stack.size());
  for (size_t i = 0; i < sym_stack.size(); ++i) {
    auto sym = sym_stack[i];
    auto res = eval_sym_expr_by_model(sym, model);
    auto conc = res.value;
    stack.set_from_front(i, conc);
  }
}

static void resume_conc_frames(const SymFrames_t &sym_frame, Frames_t &frames,
                               SymEnv_t &sym_env) {
  GENSYM_INFO("Restoring concrete frames from symbolic frames");
  frames.resize(sym_frame.size());
  for (size_t i = 0; i < sym_frame.size(); ++i) {
    auto sym = sym_frame[i];
    assert(sym.symptr != nullptr);
    auto res = eval_sym_expr(sym, sym_env);
    auto conc = res.value;
    frames.set_from_front(i, conc);
  }
  sym_frame.restore_frame_ptr(frames);
}

static void resume_conc_frames_by_model(const SymFrames_t &sym_frame,
                                        Frames_t &frames, z3::model &model) {
  GENSYM_INFO("Restoring concrete frames from symbolic frames");
  frames.resize(sym_frame.size());
  for (size_t i = 0; i < sym_frame.size(); ++i) {
    auto sym = sym_frame[i];
    assert(sym.symptr != nullptr);
    auto res = eval_sym_expr_by_model(sym, model);
    auto conc = res.value;
    frames.set_from_front(i, conc);
  }
  sym_frame.restore_frame_ptr(frames);
}

static void resume_conc_memory(const SymMemory_t &sym_memory, Memory_t &memory,
                               const SymEnv_t &sym_env) {
  GENSYM_INFO("Restoring concrete memory from symbolic memory");
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

static void resume_conc_memory_by_model(const SymMemory_t &sym_memory,
                                        Memory_t &memory, z3::model &model) {
  GENSYM_INFO("Restoring concrete memory from symbolic memory");
  memory.reset();
  for (const auto &pair : sym_memory.memory) {
    int32_t addr = pair.first;
    SymVal sym = pair.second;
    assert(sym.symptr != nullptr);
    auto res = eval_sym_expr_by_model(sym, model);
    auto conc = res.value;
    assert(res.width == 8 && "Memory should only store bytes");
    memory.store_byte(addr, conc.value & 0xFF);
  }
}

static void resume_conc_states(const SymStack_t &sym_stack,
                               const SymFrames_t &sym_frame,
                               const SymFrames_t &sym_globals,
                               const SymMemory_t &sym_memory, Stack_t &stack,
                               Frames_t &frames, Frames_t &globals,
                               Memory_t &memory, SymEnv_t &sym_env) {
  resume_conc_stack(sym_stack, stack, sym_env);
  resume_conc_frames(sym_frame, frames, sym_env);
  resume_conc_frames(sym_globals, globals, sym_env);
  resume_conc_memory(sym_memory, memory, sym_env);
}

static void resume_conc_states_by_model(const SymStack_t &sym_stack,
                                        const SymFrames_t &sym_frame,
                                        const SymFrames_t &sym_globals,
                                        const SymMemory_t &sym_memory,
                                        Stack_t &stack, Frames_t &frames,
                                        Frames_t &globals, Memory_t &memory,
                                        z3::model &model) {
  resume_conc_stack_by_model(sym_stack, stack, model);
  resume_conc_frames_by_model(sym_frame, frames, model);
  resume_conc_frames_by_model(sym_globals, globals, model);
  resume_conc_memory_by_model(sym_memory, memory, model);
}

inline void Snapshot_t::restore_states_to_global() const {
  // Restore the symbolic state from the snapshot
  GENSYM_INFO("Reusing symbolic state from snapshot");
  SymStack = stack;
  SymFrames = frames;
  SymMemory = memory;
  SymGlobals = globals;
}

inline std::monostate
Snapshot_t::resume_execution_by_model(NodeBox *node, z3::model &model) const {
  // Reset explore tree's cursor and restore symbolic states
  ExploreTree.set_cursor(node);
  restore_states_to_global();

  {
    auto timer = ManagedTimer(TimeProfileKind::RESUME_SNAPSHOT);
    // Restore the concrete states from the symbolic states
    resume_conc_states_by_model(stack, frames, globals, memory, Stack, Frames,
                                Globals, Memory, model);
  }
  // Resume execution from the continuation
  auto timer = ManagedTimer(TimeProfileKind::INSTR);
  CostManager.reset_timer();
  CURRENT_MCONT = mcont;
  return cont(std::monostate{});
}

[[deprecated]] inline std::monostate
Snapshot_t::resume_execution(NodeBox *node) const {
  // Reset explore tree's cursor and restore symbolic states
  ExploreTree.set_cursor(node);
  restore_states_to_global();
  {
    auto timer = ManagedTimer(TimeProfileKind::RESUME_SNAPSHOT);
    // Restore the concrete states from the symbolic states
    resume_conc_states(stack, frames, globals, memory, Stack, Frames, Globals,
                       Memory, SymEnv);
  }

  // Resume execution from the continuation
  auto timer = ManagedTimer(TimeProfileKind::INSTR);
  CURRENT_MCONT = mcont;
  return cont(std::monostate{});
}

#endif // WASM_SYMBOLIC_RT_HPP
