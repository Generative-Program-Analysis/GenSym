#ifndef CONCOLIC_DRIVER_HPP
#define CONCOLIC_DRIVER_HPP

#include "concrete_rt.hpp"
#include "config.hpp"
#include "output_report.hpp"
#include "profile.hpp"
#include "smt_solver.hpp"
#include "symbolic_rt.hpp"
#include "utils.hpp"
#include <cassert>
#include <chrono>
#include <functional>
#include <optional>
#include <ostream>
#include <stdexcept>
#include <string>
#include <vector>

class ConcolicDriver {
  friend class ManagedConcolicCleanup;

public:
  ConcolicDriver(std::function<void()> entrypoint,
                 std::optional<std::string> tree_file, int branchCount)
      : entrypoint(entrypoint), tree_file(tree_file) {
    ExploreTree.true_branch_cov_map.assign(branchCount, false);
    ExploreTree.false_branch_cov_map.assign(branchCount, false);
  }
  void run();

private:
  void main_exploration_loop();
  std::function<void()> entrypoint;
  std::optional<std::string> tree_file;
  std::vector<NodeBox *> work_list;
};

class ManagedConcolicCleanup {
  const ConcolicDriver &driver;

public:
  ManagedConcolicCleanup(const ConcolicDriver &driver) : driver(driver) {}
  ~ManagedConcolicCleanup() {
    // put any cleanup code that needs to be done after each execution here

    // Dump the explore tree if needed
    if (driver.tree_file.has_value())
      ExploreTree.dump_graphviz(driver.tree_file.value());

    // Clear the symbol bookkeeper
    SymBookKeeper.clear();
  }
};

static std::monostate reset_stacks();

inline void ConcolicDriver::main_exploration_loop() {

  // Register a collector to ExploreTree to add new nodes to work_list
  ExploreTree.register_new_node_collector(
      [&](NodeBox *new_node) { work_list.push_back(new_node); });

  std::set<NodeBox *> visited;

  assert(ExploreTree.get_root()->isUnexplored() &&
         "Before main loop, root should be unexplored!");
  work_list.push_back(ExploreTree.get_root());

  while (!work_list.empty()) {
    ManagedConcolicCleanup cleanup{*this};
    // Pick an unexplored node from the work list
    auto node = work_list.back();
    work_list.pop_back();

    if (visited.find(node) != visited.end()) {
      continue;
    } else {
      visited.insert(node);
    }

    if (!node->isUnexplored()) {
      // if it's not unexplored anymore, skip it
      continue;
    }

    if (INTERACTIVE_MODE) {
      std::cout << "Press Enter to continue to the next path..." << std::endl;
      std::cin.get();
    }

    auto cond = node->collect_path_conds();
    auto result = solver.solve(cond);
    if (!result.has_value()) {
      GENSYM_INFO("Found an unreachable path, marking it as unreachable...");
      node->fillUnreachableNode();
      continue;
    }
    auto &new_env = result.value().first;
    auto &model = result.value().second;

    // update global symbolic environment from SMT solved model
    SymEnv.update(std::move(new_env));
    try {
      GENSYM_INFO("Now execute the program with symbolic environment: ");
      GENSYM_INFO(SymEnv.to_string());
      if (REUSE_SNAPSHOT) {
        if (auto snapshot = dynamic_cast<SnapshotNode *>(node->node.get())) {
          assert(REUSE_SNAPSHOT);
          auto snap = snapshot->get_snapshot();
          snap.resume_execution_by_model(node, model);
        } else {
          auto timer = ManagedTimer(TimeProfileKind::INSTR);
          ExploreTree.reset_cursor();
          reset_stacks();
          entrypoint();
        }
      } else {
        auto timer = ManagedTimer(TimeProfileKind::INSTR);
        ExploreTree.reset_cursor();
        reset_stacks();
        entrypoint();
      }

      GENSYM_INFO("Execution finished successfully with symbolic environment:");
      GENSYM_INFO(SymEnv.to_string());
    } catch (std::runtime_error &e) {
      std::cout << "Caught runtime error: " << e.what() << std::endl;
      ExploreTree.fillFailedNode();

      if (std::string(e.what()) == "Symbolic assertion failed") {
        GENSYM_INFO("Symbolic assertion failed, continuing to next path...");
        continue;
      }

      GENSYM_INFO("Caught runtime error with symbolic environment:");
      GENSYM_INFO(SymEnv.to_string());
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

inline void ConcolicDriver::run() {
  main_exploration_loop();
  auto overall = ExploreTree.read_current_overall_result();
  overall.print();
  Profile.print_summary();
  dump_all_summary_json(Profile, overall);
}

static void start_concolic_execution_with(
    std::function<std::monostate(std::monostate)> entrypoint, int branchCount) {

  const char *env_tree_file = std::getenv("TREE_FILE");

  auto tree_file =
      env_tree_file ? std::make_optional(env_tree_file) : std::nullopt;

  ConcolicDriver driver = ConcolicDriver(
      [=]() { entrypoint(std::monostate{}); }, tree_file, branchCount);
  driver.run();
}

#endif // CONCOLIC_DRIVER_HPP