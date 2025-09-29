#ifndef CONCOLIC_DRIVER_HPP
#define CONCOLIC_DRIVER_HPP

#include "concrete_rt.hpp"
#include "smt_solver.hpp"
#include "symbolic_rt.hpp"
#include "utils.hpp"
#include <functional>
#include <optional>
#include <ostream>
#include <stdexcept>
#include <string>
#include <vector>

enum class ExploreMode { EarlyExit, ExitByCoverage };

#ifdef EARLY_EXIT
static const ExploreMode EXPLORE_MODE = ExploreMode::EarlyExit;
#elif defined(BY_COVERAGE)
static const ExploreMode EXPLORE_MODE = ExploreMode::ExitByCoverage;
#else
static const ExploreMode EXPLORE_MODE = ExploreMode::EarlyExit;
#endif

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
  Solver solver;
  std::function<void()> entrypoint;
  std::optional<std::string> tree_file;
};

class ManagedConcolicCleanup {
  const ConcolicDriver &driver;

public:
  ManagedConcolicCleanup(const ConcolicDriver &driver) : driver(driver) {}
  ~ManagedConcolicCleanup() {
    if (driver.tree_file.has_value())
      ExploreTree.dump_graphviz(driver.tree_file.value());
  }
};

inline void ConcolicDriver::run() {
  ExploreTree.reset_cursor();
  while (true) {
    ManagedConcolicCleanup cleanup{*this};

    auto unexplored = ExploreTree.pick_unexplored();
    if (!unexplored) {
      GENSYM_INFO("No unexplored nodes found, exiting...");
      return;
    }
    auto cond = unexplored->collect_path_conds();
    auto result = solver.solve(cond);
    if (!result.has_value()) {
      GENSYM_INFO("Found an unreachable path, marking it as unreachable...");
      unexplored->fillUnreachableNode();
      continue;
    }
    auto new_env = result.value();

    // update global symbolic environment from SMT solved model
    SymEnv.update(std::move(new_env));
    try {
      GENSYM_INFO("Now execute the program with symbolic environment: ");
      GENSYM_INFO(SymEnv.to_string());
      if (auto snapshot_node =
              dynamic_cast<SnapshotNode *>(unexplored->node.get())) {
        snapshot_node->get_snapshot().resume_execution(SymEnv, unexplored);
      } else {
        entrypoint();
      }

      GENSYM_INFO("Execution finished successfully with symbolic environment:");
      GENSYM_INFO(SymEnv.to_string());
    } catch (std::runtime_error &e) {
      std::cout << "Caught runtime error: " << e.what() << std::endl;
      ExploreTree.fillFailedNode();
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
          GENSYM_INFO("Found a bug, but not all branches covered, continuing...");
        }
      }
    }
#if defined(RUN_ONCE)
    return;
#endif
  }
}

static std::monostate reset_stacks() {
  Stack.reset();
  Frames.reset();
  SymStack.reset();
  SymFrames.reset();
  initRand();
  Memory = Memory_t(1);
  return std::monostate{};
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