#include "wasm/concolic_driver.hpp"

ConcolicDriver::ConcolicDriver(std::function<void()> entrypoint,
                               std::optional<std::string> tree_file,
                               int branchCount)
    : entrypoint(entrypoint), tree_file(tree_file) {
  ExploreTree.true_branch_cov_map.assign(branchCount, false);
  ExploreTree.false_branch_cov_map.assign(branchCount, false);
}

ManagedConcolicCleanup::ManagedConcolicCleanup(const ConcolicDriver &driver)
    : driver(driver) {}

ManagedConcolicCleanup::~ManagedConcolicCleanup() {
  if (driver.tree_file.has_value())
    ExploreTree.dump_graphviz(driver.tree_file.value());

  // Profile.print_summary();
}

PathPicker::PathPicker(std::vector<NodeBox *> &unexplored_paths,
                       std::set<NodeBox *> &visited)
    : unexplored_paths(unexplored_paths), visited(visited) {}

DefaultPathPicker::DefaultPathPicker(std::vector<NodeBox *> &unexplored_paths,
                                     std::set<NodeBox *> &visited)
    : PathPicker(unexplored_paths, visited) {}

std::optional<PathFrontier> DefaultPathPicker::pick_path() {
  NodeBox *node = unexplored_paths.back();
  unexplored_paths.pop_back();

  if (visited.find(node) != visited.end()) {
    return std::nullopt;
  } else {
    visited.insert(node);
  }

  if (!node->isUnexplored()) {
    return std::nullopt;
  }

  std::optional<QueryResult> result;
  {
    ManagedTimer timer(TimeProfileKind::SOLVER_TOTAL);
    auto cond = node->collect_path_conds();
    result = solver.solve_path_conds(cond, true);
  }

  if (!result.has_value()) {
    GENSYM_INFO("Found an unreachable path, marking it as unreachable...");
    node->fillUnreachableNode();
    return std::nullopt;
  }

  return PathFrontier{result.value(), node};
}

RandomPathPicker::RandomPathPicker(std::vector<NodeBox *> &unexplored_paths,
                                   std::set<NodeBox *> &visited)
    : PathPicker(unexplored_paths, visited) {}

std::optional<PathFrontier> RandomPathPicker::pick_path() {
  ManagedTimer timer(TimeProfileKind::SOLVER_TOTAL);

  if (unexplored_paths.empty()) {
    return std::nullopt;
  }

  std::vector<std::vector<SymVal>> all_path_conds;
  std::vector<NodeBox *> candidate_nodes;

  for (auto node : unexplored_paths) {
    ManagedTimer timer(TimeProfileKind::COLLECT_PATH_CONDITIONS);
    if (visited.find(node) != visited.end()) {
      continue;
    }
    if (!node->isUnexplored()) {
      continue;
    }
    all_path_conds.push_back(node->collect_path_conds());
    candidate_nodes.push_back(node);
  }

  auto result =
      solver.find_reachable_path_with_witness(all_path_conds, candidate_nodes);

  if (!result.has_value()) {
    for (auto node : candidate_nodes) {
      GENSYM_INFO("Found an unreachable path, marking it as unreachable...");
      node->fillUnreachableNode();
    }
    unexplored_paths.clear();
    return std::nullopt;
  }

  return PathFrontier{.query_result = *result, .node = result->witness};
}

std::vector<std::vector<SymVal>>
ConcolicDriver::collect_all_unexplored_path_conds() {
  std::vector<std::vector<SymVal>> result;
  for (auto node : work_list) {
    if (node->isUnexplored()) {
      result.push_back(node->collect_path_conds());
    }
  }
  return result;
}

void ConcolicDriver::run() {
  main_exploration_loop();
  auto overall = ExploreTree.read_current_overall_result();
  overall.print();
  Profile.print_summary();
  dump_all_summary_json(Profile, overall);
}