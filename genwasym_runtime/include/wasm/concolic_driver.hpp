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
#include <functional>
#include <optional>
#include <set>
#include <string>
#include <variant>
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

void start_concolic_execution_with(
    std::function<std::monostate(std::monostate)> entrypoint, int branchCount);

#endif // CONCOLIC_DRIVER_HPP
