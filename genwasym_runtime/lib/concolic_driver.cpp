#include "wasm/concolic_driver.hpp"

#include <algorithm>
#include <cassert>
#include <cstdlib>
#include <iostream>
#include <stdexcept>

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

void ConcolicDriver::main_exploration_loop() {

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

void start_concolic_execution_with(
    std::function<std::monostate(std::monostate)> entrypoint, int branchCount) {

  const char *env_tree_file = std::getenv("TREE_FILE");

  auto tree_file =
      env_tree_file ? std::make_optional(env_tree_file) : std::nullopt;

  ConcolicDriver driver = ConcolicDriver(
      [=]() { entrypoint(std::monostate{}); }, tree_file, branchCount);
  driver.run();
  std::quick_exit(0);
}
