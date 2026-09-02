#include "wasm/smt_solver.hpp"

#include <cassert>
#include <chrono>
#include <cstring>
#include <stdexcept>
#include <string>
#include <utility>

QueryResultWithWitness::QueryResultWithWitness(ImmNumMapBox map_box,
                                               z3::model model,
                                               NodeBox *witness)
    : QueryResult{map_box, model}, witness(witness) {}

QueryResult compose_query_results(const std::vector<QueryResult> &results) {
  ManagedTimer timer(TimeProfileKind::SPLIT_CONDITIONS);
  NumMap combined_map;
  z3::model combined_model(global_z3_ctx());

  for (const auto &res : results) {
    auto num_map = res.map_box;
    for (const auto &[id, num] : *num_map) {
      assert(
          combined_map.find(id) == combined_map.end() &&
          "Conflicting symbolic environment ids when composing query results");
      combined_map[id] = num;
    }

    const z3::model &model = res.model;
    for (unsigned i = 0; i < model.num_consts(); ++i) {
      z3::func_decl decl = model.get_const_decl(i);
      std::string name = decl.name().str();

      assert((starts_with(name, "s_int") || starts_with(name, "s_f32") ||
              starts_with(name, "s_f64")) &&
             "Unexpected declaration in query result model");

      assert(!combined_model.has_interp(decl) &&
             "Internal Error: Conflicting constant declarations when composing query results");

      z3::expr value = model.get_const_interp(decl);
      combined_model.add_const_interp(decl, value);
    }
  }

  ImmNumMapBox combined_map_box(combined_map);
  return QueryResult{combined_map_box, combined_model};
}

std::optional<int> group_of_symval(const SymVal &sym, UnionFind &uf) {
  if (auto symbol = dynamic_cast<Symbol *>(sym.symptr.get())) {
    return symbol->get_id();
  } else if (auto concrete = dynamic_cast<SymConcrete *>(sym.symptr.get())) {
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
  } else if (auto unary = dynamic_cast<SymUnary *>(sym.symptr.get())) {
    return group_of_symval(unary->value, uf);
  } else if (auto extract = dynamic_cast<SymExtract *>(sym.symptr.get())) {
    return group_of_symval(extract->value, uf);
  }

  return std::nullopt;
}

VectorGroupMap build_group_map(const std::vector<SymVal> &conditions) {
  ManagedTimer timer(TimeProfileKind::SPLIT_CONDITIONS);

  if (conditions.empty()) {
    return VectorGroupMap{};
  }

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

GroupResult split_conditions(
    const std::vector<SymVal> &conditions,
    const VectorGroupMap &group_map,
    const std::unordered_set<int> unused_indexes) {
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

Solver solver;

Solver::Solver() {}

std::optional<QueryResult>
Solver::solve_path_conds(std::vector<SymVal> &conditions,
                         bool only_latest_unseen) {
  if (conditions.empty()) {
    return QueryResult{ImmNumMapBox(NumMap{}), z3::model(global_z3_ctx())};
  }

  auto group_map = build_group_map(conditions);

  if (only_latest_unseen) {
    auto latest_pc_index = 0;
    if (group_map.find(latest_pc_index) == group_map.end()) {
      return std::nullopt;
    }
  }

  GroupResult groups = split_conditions(conditions, group_map);

  if (only_latest_unseen) {
    groups.ungrouped_conds.clear();
  }

  return solve_by_groups(groups, group_map, conditions.size());
}

std::optional<QueryResult>
Solver::solve(const std::vector<SymVal> &conditions) {
  if (conditions.empty()) {
    return QueryResult{ImmNumMapBox(NumMap{}), z3::model(global_z3_ctx())};
  }

  VectorGroupMap group_map = build_group_map(conditions);
  GroupResult groups = split_conditions(conditions, group_map);

  return solve_by_groups(groups, group_map, conditions.size());
}

std::optional<QueryResult>
Solver::solve_under_reachable_path(std::vector<SymVal> &&conditions,
                                   SymVal extra_cond) {
  conditions.push_back(extra_cond);

  VectorGroupMap group_map = build_group_map(conditions);
  std::unordered_set<int> unused_indexes;

  for (size_t i = 0; i < conditions.size() - 1; ++i) {
    if (group_map.find(i) == group_map.end()) {
      unused_indexes.insert(i);
    }
  }

  GroupResult groups = split_conditions(conditions, group_map, unused_indexes);
  return solve_by_groups(groups, group_map, conditions.size());
}

std::optional<QueryResultWithWitness> Solver::find_reachable_path_with_witness(
    const std::vector<std::vector<SymVal>> &all_conditions,
    const std::vector<NodeBox *> &candidate_nodes) {
  assert(all_conditions.size() == candidate_nodes.size() &&
         "Conditions size and candidate nodes size must be equal");

  std::vector<SymVal> disjuncts;
  auto witness = SymVal::get_witness_symbol();
  SymVal disjunction = SVFactory::FALSE;

  {
    ManagedTimer timer(TimeProfileKind::COLLECT_PATH_CONDITIONS);

    for (size_t i = 0; i < all_conditions.size(); ++i) {
      const auto &conds = all_conditions[i];
      auto clause = make_conjunction(conds, true);
      clause =
          clause.land(witness.eq_bool(SVFactory::make_concrete_bv(Num(i), 32)));

      disjuncts.push_back(clause);
    }

    disjunction = make_disjunction(disjuncts);
  }

  auto result = solve_group({disjunction}, false);
  if (!result.has_value()) {
    return std::nullopt;
  }

  z3::model &model = result->model;
  z3::expr witness_expr = model.eval(witness->z3_expr(), true);
  int witness_index = witness_expr.get_numeral_int64();

  return QueryResultWithWitness{
      result->map_box,
      result->model,
      candidate_nodes[witness_index],
  };
}

std::optional<QueryResult>
Solver::solve_group(const std::vector<SymVal> &conditions, bool is_bv) {
  z3::solver z3_solver(global_z3_ctx());
  SymVal conjunction = SVFactory::TRUE;
  z3::check_result solver_result;
  double z3_solver_time = 0.0;

  {
    auto timer =
        ManagedTimer(TimeProfileKind::CALL_Z3_SOLVER, z3_solver_time);
    Profile.incr_call_solver_count();

    conjunction = make_conjunction(conditions, is_bv);

    if (auto it = solver_cache.find(conjunction); it != solver_cache.end()) {
      Profile.cache_hit();
      return it->second;
    }

    Profile.cache_miss();

    SymValSet added_conds;
    for (size_t i = 0; i < conditions.size(); ++i) {
      SymVal temp = is_bv ? conditions[i].bv2bool() : conditions[i];

      if (added_conds.find(temp) != added_conds.end()) {
        continue;
      }

      z3_solver.add(temp->z3_expr());
      added_conds.insert(temp);
    }

    GENSYM_INFO("Solving conditions with Z3 solver...");
    solver_result = z3_solver.check();
  }

  Profile.record_z3_solver_time(z3_solver, z3_solver_time,
                                solver_result == z3::sat);

  switch (solver_result) {
  case z3::unsat:
    solver_cache[conjunction] = std::nullopt;
    return std::nullopt;

  case z3::sat: {
    z3::model model = z3_solver.get_model();
    NumMap result;

    GENSYM_INFO("Solved Z3 model");
    GENSYM_INFO(model);

    for (unsigned i = 0; i < model.size(); ++i) {
      z3::func_decl var = model[i];
      z3::expr value = model.get_const_interp(var);
      std::string name = var.name().str();

      if (starts_with(name, "s_int")) {
        int id = std::stoi(name.substr(std::string("s_int").length()));
        z3::expr evaluated = model.eval(value, true);
        uint64_t bits = evaluated.get_numeral_uint64();
        int64_t raw = 0;
        std::memcpy(&raw, &bits, sizeof(raw));
        result[id] = Num(raw);
      } else if (starts_with(name, "s_f32")) {
        int id = std::stoi(name.substr(std::string("s_f32").length()));
        z3::expr evaluated = model.eval(value.mk_to_ieee_bv(), true);
        uint64_t bits = evaluated.get_numeral_uint64();
        result[id] = Num(static_cast<int64_t>(static_cast<uint32_t>(bits)));
      } else if (starts_with(name, "s_f64")) {
        int id = std::stoi(name.substr(std::string("s_f64").length()));
        z3::expr evaluated = model.eval(value.mk_to_ieee_bv(), true);
        uint64_t bits = evaluated.get_numeral_uint64();
        int64_t raw = 0;
        std::memcpy(&raw, &bits, sizeof(raw));
        result[id] = Num(raw);
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

  return std::nullopt;
}

std::optional<QueryResult>
Solver::solve_by_groups(const GroupResult &groups,
                        const VectorGroupMap &group_map,
                        int condition_size) {
  if (!solve_group(groups.ungrouped_conds, true).has_value()) {
    return std::nullopt;
  }

  std::vector<QueryResult> group_results;
  std::unordered_set<int> processed_groups;

  for (size_t i = 0; i < condition_size; ++i) {
    if (group_map.find(i) == group_map.end()) {
      continue;
    }

    int group_id = group_map.at(i);

    if (processed_groups.find(group_id) != processed_groups.end()) {
      continue;
    }

    processed_groups.insert(group_id);
    auto &group_conds = groups.conds_in_groups.at(group_id);
    auto group_result = solve_group(group_conds, true);

    if (!group_result.has_value()) {
      return std::nullopt;
    }

    group_results.push_back(group_result.value());
  }

  return compose_query_results(group_results);
}

SymVal Solver::make_conjunction(const std::vector<SymVal> &conditions,
                                bool is_bv) {
  ManagedTimer timer(TimeProfileKind::MAKE_CONJUNCTION);
  SymVal result = SVFactory::make_concrete_bool(true);
  SymValSet added_conds;

  for (size_t i = 0; i < conditions.size(); ++i) {
    SymVal temp = is_bv ? conditions[i].bv2bool() : conditions[i];

    if (added_conds.find(temp) != added_conds.end()) {
      continue;
    }

    added_conds.insert(temp);
    result = result.land(temp);
  }

  return result;
}

SymVal Solver::make_disjunction(const std::vector<SymVal> &conditions) {
  SymVal fls = SVFactory::make_concrete_bool(false);
  SymVal result = fls;
  SymValSet added_conds;

  for (size_t i = 0; i < conditions.size(); ++i) {
    if (added_conds.find(conditions[i]) != added_conds.end()) {
      continue;
    }

    added_conds.insert(conditions[i]);
    result = result.lor(conditions[i]);
  }

  return result;
}

z3::expr Solver::to_z3_conjunction(std::vector<SymVal> &conditions) {
  z3::expr conjunction = global_z3_ctx().bool_val(true);

  for (auto &cond : conditions) {
    auto z3_cond = cond->z3_expr();
    conjunction = conjunction && z3_cond != global_z3_ctx().bv_val(0, 32);
  }

  return conjunction;
}

EvalRes eval_sym_expr_by_model(const SymVal &sym, z3::model &model) {
  auto expr = sym->z3_expr();

  switch (sym->value_kind()) {
  case KindBV: {
    z3::expr value = model.eval(expr, true);
    int width = expr.get_sort().bv_size();
    return EvalRes(Num(value.get_numeral_int64()), width, KindBV);
  }

  case KindBool: {
    assert(false && "unreachable");
  }

  case KindFP: {
    z3::expr value = model.eval(expr.mk_to_ieee_bv(), true);
    int width = get_z3_fp_sort_size(expr.get_sort());
    return EvalRes(Num(value.get_numeral_int64()), width, KindFP);
  }
  }
}

std::monostate GENSYM_SYM_ASSERT(SymVal &sym_cond) {
  ManagedTimer timer(TimeProfileKind::SOLVER_TOTAL);

  auto start = std::chrono::steady_clock::now();
  std::vector<SymVal> conds = ExploreTree.collect_current_path_conds();

  auto result = solver.solve_under_reachable_path(
      std::move(conds), sym_cond.bv_negate().bool2bv());

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