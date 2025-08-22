#ifndef CONCOLIC_DRIVER_HPP
#define CONCOLIC_DRIVER_HPP

#include "concrete_rt.hpp"
#include "smt_solver.hpp"
#include "symbolic_rt.hpp"
#include "utils.hpp"
#include <functional>
#include <ostream>
#include <string>
#include <vector>

class ConcolicDriver {
  friend class ManagedConcolicCleanup;

public:
  ConcolicDriver(std::function<void()> entrypoint, std::string tree_file)
      : entrypoint(entrypoint), tree_file(tree_file) {}
  ConcolicDriver(std::function<void()> entrypoint)
      : entrypoint(entrypoint), tree_file(std::nullopt) {}
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
  ManagedConcolicCleanup cleanup{*this};
  while (true) {
    ExploreTree.reset_cursor();

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
    SymEnv.update(std::move(new_env));
    try {
      GENSYM_INFO("Now execute the program with symbolic environment: ");
      GENSYM_INFO(SymEnv.to_string());
      entrypoint();
      GENSYM_INFO("Execution finished successfully with symbolic environment:");
      GENSYM_INFO(SymEnv.to_string());
    } catch (...) {
      ExploreTree.fillFailedNode();
      GENSYM_INFO("Caught runtime error with symbolic environment:");
      GENSYM_INFO(SymEnv.to_string());
      return;
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
    std::function<std::monostate(std::monostate)> entrypoint,
    std::string tree_file) {
  ConcolicDriver driver([=]() { entrypoint(std::monostate{}); }, tree_file);
  driver.run();
}

static void start_concolic_execution_with(
    std::function<std::monostate(std::monostate)> entrypoint) {

  const char *env_tree_file = std::getenv("TREE_FILE");

  ConcolicDriver driver =
      env_tree_file ? ConcolicDriver([=]() { entrypoint(std::monostate{}); },
                                     env_tree_file)
                    : ConcolicDriver([=]() { entrypoint(std::monostate{}); });
  driver.run();
}

#endif // CONCOLIC_DRIVER_HPP