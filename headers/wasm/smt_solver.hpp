#ifndef SMT_SOLVER_HPP
#define SMT_SOLVER_HPP

#include "concrete_rt.hpp"
#include "symbolic_rt.hpp"
#include "union_find.hpp"
#include "utils.hpp"
#include "wasm/profile.hpp"
#include "z3++.h"
#include "z3_env.hpp"
#include <array>
#include <memory>
#include <optional>
#include <set>
#include <string>
#include <tuple>
#include <unordered_set>
#include <variant>
#include <vector>

struct QueryResult {
  ImmNumMapBox map_box;
  z3::model model;
};

static QueryResult
compose_query_results(const std::vector<QueryResult> &results) {
  ManagedTimer timer(TimeProfileKind::SPLIT_CONDITIONS);
  NumMap combined_map;
  for (const auto &res : results) {
    auto num_map = res.map_box;
    for (const auto &[id, num] : *num_map) {
      assert(
          combined_map.find(id) == combined_map.end() &&
          "Conflicting symbolic environment ids when composing query results");
      combined_map[id] = num;
    }
  }
  ImmNumMapBox combined_map_box(combined_map);

  z3::solver solver(global_z3_ctx());

  // build a combined z3 model
  for (const auto &[id, num] : combined_map) {
    solver.add(
        global_z3_ctx().bv_const(("s_" + std::to_string(id)).c_str(), 32) ==
        global_z3_ctx().bv_val(num.value, 32));
  }
  solver.check();
  return QueryResult{combined_map_box, solver.get_model()};
}

// VectorGroupResult groups a vector. key is the vector index, and value is the
// group id, ungrouped items do not have group id
using VectorGroupMap = std::unordered_map<int, int>;

struct GroupResult {
  std::unordered_map<int, std::vector<SymVal>> conds_in_groups;
  std::vector<SymVal> ungrouped_conds;
};

static std::optional<int> group_of_symval(const SymVal &sym, UnionFind &uf) {
  // TODO: This process is un optimized and slow, just want to see if the idea
  // of independent resolving works
  if (auto symbol = dynamic_cast<Symbol *>(sym.symptr.get())) {
    return symbol->get_id();
  } else if (auto concrete = dynamic_cast<SymConcrete *>(sym.symptr.get())) {
    return std::nullopt;
  } else if (auto smallbv = dynamic_cast<SmallBV *>(sym.symptr.get())) {
    return std::nullopt;
  } else if (auto binary = dynamic_cast<SymBinary *>(sym.symptr.get())) {
    auto left_group = group_of_symval(binary->lhs, uf);
    auto right_group = group_of_symval(binary->rhs, uf);
    if (left_group.has_value() && right_group.has_value()) {
      uf.unite(*left_group, *right_group);
      return uf.find(*left_group);
    } else if (left_group.has_value()) {
      return uf.find(*left_group);
    } else if (right_group.has_value()) {
      return uf.find(*right_group);
    } else {
      return std::nullopt;
    }
  } else if (auto extract = dynamic_cast<SymExtract *>(sym.symptr.get())) {
    return group_of_symval(extract->value, uf);
  }
  return std::nullopt;
}

static VectorGroupMap build_group_map(const std::vector<SymVal> &conditions) {
  // TODO: This is a slow temporary solution which only used for validating the
  // idea of independent constraint resolving, the intermediate result of
  // independent solving is reusable
  ManagedTimer timer(TimeProfileKind::SPLIT_CONDITIONS);
  if (conditions.empty()) {
    return VectorGroupMap{};
  }
  // use union find to group the conditions
  UnionFind uf;
  for (const auto &cond : conditions) {
    group_of_symval(cond, uf);
  }

  VectorGroupMap result;

  for (size_t i = 0; i < conditions.size(); ++i) {
    auto group = group_of_symval(conditions[i], uf);
    if (group.has_value()) {
      int group_id = uf.find(*group);
      result[i] = group_id;
    }
  }
  return result;
}

static struct GroupResult
split_conditions(const std::vector<SymVal> &conditions,
                 const VectorGroupMap &group_map,
                 const std::unordered_set<int> unused_indexes = {}) {
  ManagedTimer timer(TimeProfileKind::SPLIT_CONDITIONS);
  std::vector<SymVal> ungrouped_conds;
  std::unordered_map<int, std::vector<SymVal>> conds_in_groups;
  for (size_t i = 0; i < conditions.size(); ++i) {
    auto cond = conditions[i];
    if (group_map.find(i) != group_map.end()) {
      int group_id = group_map.at(i);
      conds_in_groups[group_id].push_back(cond);
    } else {
      if (unused_indexes.find(i) == unused_indexes.end()) {
        ungrouped_conds.push_back(cond);
      }
    }
  }
  return GroupResult{conds_in_groups, ungrouped_conds};
}

class Solver {
public:
  Solver() {}

  // Solve the path conditions. `only_latest_unseen` indicates whether the
  // previous conditions and the negation of the latest condition have been
  // reached before
  std::optional<QueryResult> solve_path_conds(std::vector<SymVal> &conditions,
                                              bool only_latest_unseen) {
    if (conditions.empty()) {
      return QueryResult{ImmNumMapBox(NumMap{}), z3::model(global_z3_ctx())};
    }

    // split the conditions into independent groups
    auto group_map = build_group_map(conditions);

    if (only_latest_unseen) {
      auto latest_pc_index = 0;
      if (group_map.find(latest_pc_index) == group_map.end()) {
        // the latest path condition is pure concrete, it must be a
        // unsatisfiable concrete condition, because its negation has been
        // executed before
        return std::nullopt;
      }
    }

    GroupResult groups = split_conditions(conditions, group_map);

    if (only_latest_unseen) {
      // We can safely remove all previously seen pure concrete conditions.
      // They are satisfiable and contribute nothing to model.
      groups.ungrouped_conds.clear();
    }

    return solve_by_groups(groups, group_map, conditions.size());
  }

  std::optional<QueryResult> solve(const std::vector<SymVal> &conditions) {
    if (conditions.empty()) {
      return QueryResult{ImmNumMapBox(NumMap{}), z3::model(global_z3_ctx())};
    }

    // split the conditions into independent groups
    VectorGroupMap group_map = build_group_map(conditions);
    GroupResult groups = split_conditions(conditions, group_map);

    return solve_by_groups(groups, group_map, conditions.size());
  }

  std::optional<QueryResult>
  solve_under_reachable_path(std::vector<SymVal> &&conditions,
                             SymVal extra_cond) {
    conditions.push_back(extra_cond);
    VectorGroupMap group_map = build_group_map(conditions);
    std::unordered_set<int> unused_indexes;
    for (size_t i = 0; i < conditions.size() - 1; ++i) {
      if (group_map.find(i) == group_map.end()) {
        // this condition is pure concrete, and has been executed before, it
        // must be satisfiable and can be ignored
        unused_indexes.insert(i);
      }
    }
    GroupResult groups =
        split_conditions(conditions, group_map, unused_indexes);
    return solve_by_groups(groups, group_map, conditions.size());
  }

private:
  std::optional<QueryResult>
  solve_group(const std::vector<SymVal> &conditions) {
    z3::solver z3_solver(global_z3_ctx());
    SymVal conjunction;
    z3::check_result solver_result;
    double z3_solver_time = 0.0;
    {
      auto timer =
          ManagedTimer(TimeProfileKind::CALL_Z3_SOLVER, z3_solver_time);
      // make an conjunction of all conditions
      conjunction = make_conjunction(conditions);
      // call z3 to solve the condition
      // NOTE: half of the solver time is spent in solver.add
      z3_solver.add(conjunction->z3_expr());
      if (solver_cache.find(conjunction) != solver_cache.end()) {
        Profile.cache_hit();
        auto cached_result = solver_cache[conjunction];
        return cached_result;
      }
      Profile.cache_miss();
      GENSYM_INFO("Solving conditions with Z3 solver...");
      solver_result = z3_solver.check();
    }
    Profile.record_z3_solver_time(conjunction->z3_expr(), z3_solver_time,
                                  solver_result == z3::sat);
    switch (solver_result) {
    case z3::unsat:
      solver_cache[conjunction] = std::nullopt;
      return std::nullopt; // No solution found
    case z3::sat: {
      z3::model model = z3_solver.get_model();
      NumMap result;
      // Reference:
      // https://github.com/Z3Prover/z3/blob/master/examples/c%2B%2B/example.cpp#L59
      GENSYM_INFO("Solved Z3 model");
      GENSYM_INFO(model);
      for (unsigned i = 0; i < model.size(); ++i) {
        z3::func_decl var = model[i];
        z3::expr value = model.get_const_interp(var);
        std::string name = var.name().str();
        if (starts_with(name, "s_")) {
          int id = std::stoi(name.substr(2));
          result[id] = Num(value.get_numeral_int64());
        } else {
          GENSYM_INFO("Find a variable that is not created by GenSym: " + name);
        }
      }
      ImmNumMapBox map_box(result);
      QueryResult query_result{map_box, model};
      solver_cache[conjunction] = query_result;
      return query_result;
    }
    case z3::unknown:
      throw std::runtime_error("Z3 solver returned unknown status");
    }
    return std::nullopt; // Should not reach here
  }

  std::optional<QueryResult> solve_by_groups(const GroupResult &groups,
                                             const VectorGroupMap &group_map,
                                             int condition_size) {

    if (!solve_group(groups.ungrouped_conds).has_value()) {
      return std::nullopt;
    }

    std::vector<QueryResult> group_results;
    std::unordered_set<int> processed_groups;
    for (size_t i = 0; i < condition_size; ++i) {
      if (group_map.find(i) == group_map.end()) {
        // ungrouped condition, skip it
        continue;
      }
      int group_id = group_map.at(i);
      if (processed_groups.find(group_id) != processed_groups.end()) {
        // already processed
        continue;
      }
      processed_groups.insert(group_id);
      auto &group_conds = groups.conds_in_groups.at(group_id);
      auto group_result = solve_group(group_conds);
      if (!group_result.has_value()) {
        // this group is unsatisfiable, so the whole condition is
        // unsatisfiable
        return std::nullopt;
      }
      group_results.push_back(group_result.value());
    }

    // combine the results from all groups
    return compose_query_results(group_results);
  }

  SymVal make_conjunction(const std::vector<SymVal> &conditions) {
    SymVal result = SymVal().eq_bool(SymVal()); // true
    for (size_t i = 0; i < conditions.size(); ++i) {
      result = result.land(conditions[i].neq_bool(SymVal()));
    }
    return result;
  }

  z3::expr to_z3_conjunction(std::vector<SymVal> &conditions) {
    z3::expr conjunction = global_z3_ctx().bool_val(true);
    for (auto &cond : conditions) {
      auto z3_cond = cond.symptr->build_z3_expr();
      conjunction = conjunction && z3_cond != global_z3_ctx().bv_val(0, 32);
    }
#ifdef DEBUG
    // std::cout << "Symbolic conditions size: " << conditions.size() <<
    // std::endl; std::cout << "Solving conditions: " << conjunction <<
    // std::endl;
#endif
    return conjunction;
  }

  std::unordered_map<SymVal, std::optional<QueryResult>> solver_cache;
};

static Solver solver;

inline EvalRes eval_sym_expr_by_model(const SymVal &sym, z3::model &model) {
  auto expr = sym.symptr->build_z3_expr();
  // let z3 decide the value of symbols that are not in the model
  z3::expr value = model.eval(expr, true);
  // every value is bitvector
  int width = expr.get_sort().bv_size();
  return EvalRes(Num(value.get_numeral_int64()), width);
}

inline std::monostate GENSYM_SYM_ASSERT(SymVal &sym_cond) {
  ManagedTimer timer(TimeProfileKind::SOLVER_TOTAL);
  auto start = std::chrono::steady_clock::now();
  std::vector<SymVal> conds = ExploreTree.collect_current_path_conds();
  auto result =
      solver.solve_under_reachable_path(std::move(conds), sym_cond.negate());
  auto end = std::chrono::steady_clock::now();
  auto time_need_to_be_removed = std::chrono::duration<double>(end - start);
  Profile.remove_instruction_time(TimeProfileKind::INSTR,
                                  time_need_to_be_removed.count());
  if (result.has_value()) {
    std::cout << "Symbolic assertion failed" << std::endl;
    throw std::runtime_error("Symbolic assertion failed");
  }
  return std::monostate{};
}

#endif // SMT_SOLVER_HPP