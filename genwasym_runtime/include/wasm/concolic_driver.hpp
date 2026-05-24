#ifndef CONCOLIC_DRIVER_HPP
#define CONCOLIC_DRIVER_HPP

#include "concrete_rt.hpp"
#include "config.hpp"
#include "output_report.hpp"
#include "profile.hpp"
#include "smt_solver.hpp"
#include "sym_rt.hpp"
#include "utils.hpp"
#include "z3++.h"
#include <cassert>
#include <chrono>
#include <functional>
#include <optional>
#include <ostream>
#include <set>
#include <stdexcept>
#include <string>
#include <vector>

class ConcolicDriver {
  friend class ManagedConcolicCleanup;

public:
  ConcolicDriver(std::function<void()> entrypoint,
                 std::optional<std::string> tree_file, int branchCount);
  void run();

private:
  void main_exploration_loop();
  std::optional<QueryResult> get_new_input();
  std::vector<std::vector<SymVal>> collect_all_unexplored_path_conds();
  std::function<void()> entrypoint;
  std::optional<std::string> tree_file;
  std::vector<NodeBox *> work_list;
  std::set<NodeBox *> visited;
};

class ManagedConcolicCleanup {
  const ConcolicDriver &driver;

public:
  ManagedConcolicCleanup(const ConcolicDriver &driver);
  ~ManagedConcolicCleanup();
};

static std::monostate reset_stacks();

// A PathFrontier represents the frontier of an unexplored path. From this
// frontier, we can explore the path by executing the program from the beginning
// with the model stored in QueryResult.
struct PathFrontier {
  QueryResult query_result;
  NodeBox *node;
};

class PathPicker {
public:
  PathPicker(std::vector<NodeBox *> &unexplored_paths,
             std::set<NodeBox *> &visited);

  virtual std::optional<PathFrontier> pick_path() = 0;

protected:
  std::vector<NodeBox *> &unexplored_paths;
  std::set<NodeBox *> &visited;
};

class DefaultPathPicker : public PathPicker {
public:
  DefaultPathPicker(std::vector<NodeBox *> &unexplored_paths,
                    std::set<NodeBox *> &visited);

  std::optional<PathFrontier> pick_path() override;
};

class RandomPathPicker : public PathPicker {
public:
  RandomPathPicker(std::vector<NodeBox *> &unexplored_paths,
                   std::set<NodeBox *> &visited);

  std::optional<PathFrontier> pick_path() override;
};

inline void ConcolicDriver::main_exploration_loop() {

  // Register a collector to ExploreTree to add new nodes to work_list
  ExploreTree.register_new_node_collector([&](NodeBox *new_node) {
    if (std::find(work_list.begin(), work_list.end(), new_node) ==
        work_list.end())
      work_list.push_back(new_node);
  });

  assert(ExploreTree.get_root()->isUnexplored() &&
         "Before main loop, root should be unexplored!");
  work_list.push_back(ExploreTree.get_root());

  PathPicker &&picker = DefaultPathPicker(work_list, visited);

  while (!work_list.empty()) {
    if (INTERACTIVE_MODE) {
      std::cout << "Press Enter to continue to the next path..." << std::endl;
      std::cin.get();
    }
    ManagedConcolicCleanup cleanup{*this};
    ManagedTimer timer(TimeProfileKind::MAIN_LOOP);
    // Pick a frontier of an unexplored path from the work list
    auto frontier = picker.pick_path();
    if (!frontier.has_value()) {
      continue;
    }

    auto &node = frontier.value().node;

    const NumMap &new_env = *frontier.value().query_result.map_box;
    z3::model &model = frontier.value().query_result.model;

    // update global symbolic environment from SMT solved model
    SymEnv.update(new_env);
    try {
      GENSYM_INFO("Now execute the program with symbolic environment: ");
      GENSYM_INFO(SymEnv.to_string());
      auto snapshot = dynamic_cast<SnapshotNode *>(node->node.get());
      if (REUSE_SNAPSHOT && snapshot && snapshot->worth_to_reuse()) {
        assert(REUSE_SNAPSHOT);
        Profile.incr_fromsnapshot_count();
        auto snap = snapshot->get_snapshot();
        snap.resume_execution_by_model(node, model);
      } else {
        Profile.incr_restart_count();
        auto timer = ManagedTimer(TimeProfileKind::INSTR);
        ExploreTree.reset_cursor();
        reset_stacks();
        CostManager.reset_timer();
        entrypoint();
      }

      GENSYM_INFO("Execution finished successfully");
    } catch (std::runtime_error &e) {
      std::cout << "Caught runtime error: " << e.what() << std::endl;
      ExploreTree.fillFailedNode();

      if (std::string(e.what()) == "Symbolic assertion failed") {
        GENSYM_INFO("Symbolic assertion failed, continuing to next path...");
        continue;
      }

      GENSYM_INFO("Caught runtime error during execution");
      switch (EXPLORE_MODE) {
      case ExploreMode::EarlyExit:
        return;
      case ExploreMode::ExitByCoverage:
        if (ExploreTree.all_branch_covered()) {
          GENSYM_INFO("All branches covered, exiting...");
          return;
        } else {
          GENSYM_INFO(
              "Found a bug, but not all branches covered, continuing...");
        }
        std::cout << e.what() << std::endl;
      }
    }
#if defined(RUN_ONCE)
    return;
#endif
  }
}

static void start_concolic_execution_with(
    std::function<std::monostate(std::monostate)> entrypoint, int branchCount) {

  const char *env_tree_file = std::getenv("TREE_FILE");

  auto tree_file =
      env_tree_file ? std::make_optional(env_tree_file) : std::nullopt;

  ConcolicDriver driver = ConcolicDriver(
      [=]() { entrypoint(std::monostate{}); }, tree_file, branchCount);
  driver.run();
  std::quick_exit(0);
}

#endif // CONCOLIC_DRIVER_HPP