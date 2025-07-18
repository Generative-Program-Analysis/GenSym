#ifndef CONCOLIC_DRIVER_HPP
#define CONCOLIC_DRIVER_HPP

#include "smt_solver.hpp"
#include "symbolic_rt.hpp"
#include <functional>
#include <ostream>
#include <string>

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
      std::cout << "No unexplored nodes found, exiting..." << std::endl;
      return;
    }
    auto cond = unexplored->collect_path_conds();
    auto new_env = solver.solve(cond);
    if (!new_env.has_value()) {
      // TODO: current implementation is buggy, there could be other reachable
      // unexplored paths
      std::cout << "Found an unreachable path, marking it as unreachable..."
                << std::endl;
      unexplored->fillUnreachableNode();
      continue;  
    }
    SymEnv.update(std::move(new_env.value()));
    try {
      entrypoint();
      std::cout << "Execution finished successfully with symbolic environment:"
                << std::endl;
      std::cout << SymEnv.to_string() << std::endl;
    } catch (...) {
      ExploreTree.fillFailedNode();
      std::cout << "Caught runtime error with symbolic environment:"
                << std::endl;
      std::cout << SymEnv.to_string() << std::endl;
      return;
    }
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