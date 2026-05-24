#ifndef PROFILE_HPP
#define PROFILE_HPP

#include "config.hpp"
#include "z3++.h"
#include <array>
#include <chrono>
#include <ostream>
#include <string>
#include <variant>

enum class StepProfileKind {
  PUSH,
  POP,
  PEEK,
  SHIFT,
  SET,
  GET,
  BINARY,
  TREE_FILL,
  CURSOR_MOVE,
  MEM_GROW,
  SNAPSHOT_CREATE,
  SYM_EVAL,
  OperationCount
};

enum class ExecutionKind {
  RESTART,
  FROMSNAPSHOT,
  ExecutionKindCount
};

enum class TimeProfileKind {
  INSTR,
  CALL_Z3_SOLVER,
  MAKE_CONJUNCTION,
  SOLVER_TOTAL,
  RESUME_SNAPSHOT,
  COUNT_SYM_SIZE,
  SPLIT_CONDITIONS,
  COLLECT_PATH_CONDITIONS,
  MAIN_LOOP,
  TimeOperationCount
};

class Profile_t {
public:
  Profile_t();

  void cache_hit();
  void cache_miss();

  std::monostate step();
  std::monostate step(StepProfileKind op);
  std::monostate incr_restart_count();
  std::monostate incr_fromsnapshot_count();
  std::monostate incr_call_solver_count();

  void print_summary();
  void write_as_json(std::ostream &os) const;
  void record_z3_solver_time(z3::solver expr, double time, bool is_sat);

  void add_instruction_time(TimeProfileKind kind, double time);
  void remove_instruction_time(TimeProfileKind kind, double time);

  std::string base_profile_output_path = "genwasym_profile_output";
  std::string z3_expr_output_path = "genwasym_profile_output/z3_expressions";

  int step_count;
  std::array<int, static_cast<std::size_t>(StepProfileKind::OperationCount)>
      op_count;
  std::array<double,
             static_cast<std::size_t>(TimeProfileKind::TimeOperationCount)>
      time_count;
  std::array<int, static_cast<std::size_t>(ExecutionKind::ExecutionKindCount)>
      exec_kind_count;

  int cache_hit_count;
  int cache_miss_count;
  int call_solver_count;
};

extern Profile_t Profile;

class ManagedTimer {
public:
  ManagedTimer() = delete;
  ManagedTimer(TimeProfileKind kind);
  ManagedTimer(TimeProfileKind kind, double &time_ref);
  ~ManagedTimer();

private:
  TimeProfileKind kind;
  std::chrono::high_resolution_clock::time_point start;
  double *time_ref;
};

using Time = std::chrono::time_point<std::chrono::steady_clock>;

Time getCurrentTime();
double duration_time(Time start, Time end);

struct CostManager_t {
  Time start_time;

  CostManager_t();

  std::monostate reset_timer();
  double dump_instr_cost();
  double normalize_cost(double cost);
};

extern CostManager_t CostManager;

#endif // PROFILE_HPP