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
#include "symbolic.hpp"
#include "symval.hpp"
#include "symval_factory.hpp"
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
  void push(SymVal val);

  SymVal pop();

  SymVal peek();

  std::monostate shift(int32_t offset, int32_t size);

  void reset();

  size_t size() const;

  SymVal operator[](size_t index) const;

  int total_sym_size() const;

private:
  int symbolic_size = 0;
#ifdef USE_IMM
  immer::vector_transient<SymVal> stack;
#else
  std::vector<SymVal> stack;
#endif
};

extern SymStack_t SymStack;

class SymFrames_t {

public:
  void restore_frame_ptr(Frames_t &frame) const;

  void pushFramePtr();

  void pushFrameSlot(int width);

  std::monostate popFrameCaller(int size);

  std::monostate popFrameCallee(int size);

  SymVal get(int index);

  void set(int index, SymVal val);

  void reset();

  size_t size() const;

  SymVal operator[](size_t index) const;

  int total_sym_size() const;

private:
  size_t current_frame_base() const;

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

  SymVal loadSymByte(int32_t addr);

  SymVal loadSym(int32_t base, int32_t offset);

  SymVal loadSymLong(int32_t base, int32_t offset);

  SymVal loadSymFloat(int32_t base, int32_t offset);

  SymVal loadSymDouble(int32_t base, int32_t offset);

  SymVal loadSymInt8U(int32_t base, int32_t offset);

  SymVal loadSymInt8S(int32_t base, int32_t offset);

  SymVal loadSymInt16U(int32_t base, int32_t offset);

  SymVal loadSymInt16S(int32_t base, int32_t offset);

  SymVal loadSymLong8U(int32_t base, int32_t offset);

  SymVal loadSymLong8S(int32_t base, int32_t offset);

  SymVal loadSymLong16U(int32_t base, int32_t offset);

  SymVal loadSymLong16S(int32_t base, int32_t offset);

  SymVal loadSymLong32U(int32_t base, int32_t offset);

  SymVal loadSymLong32S(int32_t base, int32_t offset);

  // when loading a symval, we need to concat 4 symbolic values
  // This sounds terribly bad for SMT...
  // Load a 4-byte symbolic value from memory
  // Store a 4-byte symbolic value to memory
  std::monostate storeSym(int32_t base, int32_t offset, SymVal value);

  std::monostate storeSymLong(int32_t base, int32_t offset, SymVal value);

  std::monostate storeSymInt8(int32_t base, int32_t offset, SymVal value);

  std::monostate storeSymInt16(int32_t base, int32_t offset, SymVal value);

  std::monostate storeSymLong8(int32_t base, int32_t offset, SymVal value);

  std::monostate storeSymLong16(int32_t base, int32_t offset, SymVal value);

  std::monostate storeSymLong32(int32_t base, int32_t offset, SymVal value);

  std::monostate storeSymFloat(int32_t base, int32_t offset, SymVal value);

  std::monostate storeSymDouble(int32_t base, int32_t offset, SymVal value);

  std::monostate storeSymByte(int32_t addr, SymVal value);

  std::monostate reset();

  int total_sym_size() const;
};

extern SymMemory_t SymMemory;

std::monostate memoryInitialize(int32_t offset,
                                       const std::string &data);

using NumMap = std::unordered_map<int, Num>;

// TODO: remove this class later
class ImmNumMapBox {
public:
  ImmNumMapBox(const NumMap &sym_env);

  const NumMap *operator->() const;
  const NumMap &operator*() const;

private:
  std::shared_ptr<NumMap> map_ptr;
};

class SymEnv_t {
public:
  SymEnv_t();

  Num read(const Symbol &symbol) const;

  Num read(SymVal sym);

  void update(NumMap new_env);

  // Absorb another symbolic environment into this one, if some keys not exist
  // in another environment and exist in this one, they will be kept unchanged.
  void absorb(const NumMap &other);

  std::string to_string() const;

  size_t size() const;

  ImmNumMapBox get_num_map() const;

private:
  NumMap map; // The symbolic environment, a vector of Num
  ImmNumMapBox imm_map_box;
};

extern SymEnv_t SymEnv;

// A snapshot of the symbolic state and execution context (control)
class Snapshot_t {
public:
  explicit Snapshot_t(Cont_t cont, MCont_t mcont, SymStack_t stack,
                      SymFrames_t frames,
                      SymFrames_t globals, SymMemory_t memory, ImmNumMapBox num_map /* Current num map that corresponds to the symbolic environment */);

  SymStack_t get_stack() const;
  SymFrames_t get_frames() const;
  SymFrames_t get_globals() const;
  SymMemory_t get_memory() const;

  [[deprecated]] std::monostate resume_execution(NodeBox *node) const;
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

extern SymFrames_t SymFrames;
extern SymFrames_t SymGlobals;

Control makeControl(Cont_t cont, MCont_t mcont);

Snapshot_t makeSnapshot(Control control);

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
  // Collect and cache path conditions in an immutable vector.
  immer::vector<SymVal> collect_path_conds_imm();

  [[deprecated]] void reach_here(std::function<void()>);

  Node *operator->();
};

struct Node {
  friend struct NodeBox;
  virtual ~Node();
  void set_cost(double c);
  double get_cost() const;
  virtual std::string to_string() = 0;
  void to_graphviz(std::ostream &os);
  virtual void generate_dot(std::ostream &os, int parent_dot_id,
                            const std::string &edge_label) = 0;

protected:
  // Counter for unique node IDs across the entire graph, only for generating
  // graphviz purpose
  static int current_id;
  void graphviz_node(std::ostream &os, const int node_id,
                     const std::string &label, const std::string &shape,
                     const std::string &fillcolor);

  void graphviz_edge(std::ostream &os, int from_id, int target_id,
                     const std::string &edge_label);

private:
  double instr_cost = 0.0;
  std::optional<immer::vector<SymVal>> path_conds_cache;
};

struct IfElseNode : Node {
  SymVal cond;
  std::unique_ptr<NodeBox> true_branch;
  std::unique_ptr<NodeBox> false_branch;
  int id;

  IfElseNode(SymVal cond, NodeBox *parent, int id);

  std::string to_string() override;

  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override;
};

struct CallIndirectNode : Node {
  SymVal cond;
  std::unordered_map<int, std::unique_ptr<NodeBox>> branches;
  std::unique_ptr<NodeBox> otherwise_branch;
  int id;
  CallIndirectNode(SymVal cond, NodeBox *parent, int id);
  std::string to_string() override;

  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override;
};

struct UnExploredNode : Node {
  UnExploredNode();
  std::string to_string() override;

protected:
  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override;
};

struct NotToExploreNode : Node {
  NotToExploreNode();
  std::string to_string() override;

protected:
  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override;
};

struct SnapshotNode : Node {
  SnapshotNode(Snapshot_t snapshot);
  std::string to_string() override;
  const Snapshot_t &get_snapshot() const;
  Snapshot_t move_out_snapshot();

  bool worth_to_reuse() const;

protected:
  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override;

private:
  Snapshot_t snapshot;
};

struct Finished : Node {
  Finished();
  std::string to_string() override;

protected:
  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override;
};

struct Failed : Node {
  Failed();
  std::string to_string() override;

protected:
  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override;
};

struct Unreachable : Node {
  Unreachable();
  std::string to_string() override;

protected:
  void generate_dot(std::ostream &os, int parent_dot_id,
                    const std::string &edge_label) override;
};

const double INSTR_COST_SCALING_FACTOR = 1E-03;

struct OverallResult {
  int unexplored_count = 0;
  int finished_count = 0;
  int failed_count = 0;
  int not_to_explore_count = 0;
  int unreachable_count = 0;

  void print();
};

class ExploreTree_t {
public:
  explicit ExploreTree_t();

  void reset_cursor();

  void clear();

  void set_cursor(NodeBox *new_cursor);

  std::monostate fillFinishedNode();

  std::monostate fillFailedNode();

  std::monostate fillIfElseNode(SymVal cond, int id);

  std::monostate fillCallIndirectNode(SymVal cond, int id);

  std::monostate fillNotToExploredNode();

  std::vector<SymVal> collect_current_path_conds();

  std::monostate moveCursor(bool branch, Control control);

  std::monostate moveCursorNoControl(bool branch);

  std::monostate moveCursorIndirect(int branch_index);

  std::monostate print();

  std::monostate to_graphviz(std::ostream &os);

  std::monostate dump_graphviz(std::string filepath);

  OverallResult read_current_overall_result();

  NodeBox *pick_unexplored();
  std::vector<bool> true_branch_cov_map;
  std::vector<bool> false_branch_cov_map;
  bool all_branch_covered() const;

  NodeBox *get_root() const;

  void register_new_node_collector(std::function<void(NodeBox *)> func);

private:
  NodeBox *pick_unexplored_of(NodeBox *node);
  void register_new_node(NodeBox *node);
  std::unique_ptr<NodeBox> root;
  NodeBox *cursor;
  std::vector<std::function<void(NodeBox *)>> new_node_collectors;
};

extern ExploreTree_t ExploreTree;

std::monostate reset_stacks();

struct EvalRes {
  Num value;
  ValueKind kind;
  int width; // in bits
  EvalRes(Num value, int width, ValueKind kind);
};

EvalRes eval_binary_op(EvalRes lhs_res, EvalRes rhs_res,
                              BinOperation operation);

// TODO: reduce the re-computation of the same symbolic expression, it's better
// if it can be done by the smt solver
EvalRes eval_sym_expr(const SymVal &sym, const SymEnv_t &sym_env);

EvalRes eval_sym_expr_by_model(const SymVal &sym, z3::model &model);

void resume_conc_stack(const SymStack_t &sym_stack, Stack_t &stack,
                              SymEnv_t &sym_env);

void resume_conc_stack_by_model(const SymStack_t &sym_stack,
                                       Stack_t &stack, z3::model &model);

void resume_conc_frames(const SymFrames_t &sym_frame, Frames_t &frames,
                               SymEnv_t &sym_env);

void resume_conc_frames_by_model(const SymFrames_t &sym_frame,
                                        Frames_t &frames, z3::model &model);

void resume_conc_memory(const SymMemory_t &sym_memory, Memory_t &memory,
                               const SymEnv_t &sym_env);

void resume_conc_memory_by_model(const SymMemory_t &sym_memory,
                                        Memory_t &memory, z3::model &model);

void resume_conc_states(const SymStack_t &sym_stack,
                               const SymFrames_t &sym_frame,
                               const SymFrames_t &sym_globals,
                               const SymMemory_t &sym_memory, Stack_t &stack,
                               Frames_t &frames, Frames_t &globals,
                               Memory_t &memory, SymEnv_t &sym_env);

void resume_conc_states_by_model(const SymStack_t &sym_stack,
                                        const SymFrames_t &sym_frame,
                                        const SymFrames_t &sym_globals,
                                        const SymMemory_t &sym_memory,
                                        Stack_t &stack, Frames_t &frames,
                                        Frames_t &globals, Memory_t &memory,
                                        z3::model &model);

#endif // WASM_SYMBOLIC_RT_HPP
