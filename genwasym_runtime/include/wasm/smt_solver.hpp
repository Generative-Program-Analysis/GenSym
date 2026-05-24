#ifndef SMT_SOLVER_HPP
#define SMT_SOLVER_HPP

#include "concrete_rt.hpp"
#include "sym_rt.hpp"
#include "union_find.hpp"
#include "utils.hpp"
#include "wasm/profile.hpp"
#include "wasm/symbolic_decl.hpp"
#include "z3++.h"
#include "z3_env.hpp"
#include <memory>
#include <optional>
#include <set>
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <variant>
#include <vector>

struct QueryResult {
  ImmNumMapBox map_box;
  z3::model model;
};

struct QueryResultWithWitness : public QueryResult {
  QueryResultWithWitness(ImmNumMapBox map_box, z3::model model,
                         NodeBox *witness);

  NodeBox *witness;
};

QueryResult compose_query_results(const std::vector<QueryResult> &results);

using VectorGroupMap = std::unordered_map<int, int>;

struct GroupResult {
  std::unordered_map<int, std::vector<SymVal>> conds_in_groups;
  std::vector<SymVal> ungrouped_conds;
};

std::optional<int> group_of_symval(const SymVal &sym, UnionFind &uf);

VectorGroupMap build_group_map(const std::vector<SymVal> &conditions);

GroupResult split_conditions(
    const std::vector<SymVal> &conditions,
    const VectorGroupMap &group_map,
    const std::unordered_set<int> unused_indexes = {});

class Solver {
public:
  Solver();

  std::optional<QueryResult> solve_path_conds(std::vector<SymVal> &conditions,
                                              bool only_latest_unseen);

  std::optional<QueryResult> solve(const std::vector<SymVal> &conditions);

  std::optional<QueryResult>
  solve_under_reachable_path(std::vector<SymVal> &&conditions,
                             SymVal extra_cond);

  std::optional<QueryResultWithWitness> find_reachable_path_with_witness(
      const std::vector<std::vector<SymVal>> &all_conditions,
      const std::vector<NodeBox *> &candidate_nodes);

private:
  std::optional<QueryResult> solve_group(const std::vector<SymVal> &conditions,
                                         bool is_bv);

  std::optional<QueryResult> solve_by_groups(const GroupResult &groups,
                                             const VectorGroupMap &group_map,
                                             int condition_size);

  SymVal make_conjunction(const std::vector<SymVal> &conditions, bool is_bv);

  SymVal make_disjunction(const std::vector<SymVal> &conditions);

  z3::expr to_z3_conjunction(std::vector<SymVal> &conditions);

  SymValMap<std::optional<QueryResult>> solver_cache;
};

extern Solver solver;

EvalRes eval_sym_expr_by_model(const SymVal &sym, z3::model &model);

std::monostate GENSYM_SYM_ASSERT(SymVal &sym_cond);

#endif // SMT_SOLVER_HPP