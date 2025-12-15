#ifndef PROFILE_HPP
#define PROFILE_HPP

#include "config.hpp"
#include "utils.hpp"
#include <array>
#include <chrono>
#include <iomanip>
#include <ratio>
#include <string>
#include <variant>
#include <vector>

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
  OperationCount // keep this as the last element, this is used to get the
                 // number of kinds of operations
};

enum class ExecutionKind {
  RESTART,
  FROMSNAPSHOT,
  ExecutionKindCount // keep this as the last element, this is used to get the
                     // number of kinds of operations
};

enum class TimeProfileKind {
  INSTR,
  SOLVER,
  RESUME_SNAPSHOT,
  COUNT_SYM_SIZE,
  SPLIT_CONDITIONS,
  TimeOperationCount // keep this as the last element, this is used to get the
                     // number of kinds of operations
};

class Profile_t {
public:
  Profile_t() : step_count(0), cache_hit_count(0), cache_miss_count(0) {}

  void cache_hit() {
    if (PROFILE_CACHE)
      cache_hit_count++;
  }

  void cache_miss() {
    if (PROFILE_CACHE)
      cache_miss_count++;
  }

  std::monostate step() {
    if (PROFILE_STEP)
      step_count++;
    return std::monostate();
  }
  std::monostate step(StepProfileKind op) {
    if (PROFILE_STEP)
      op_count[static_cast<std::size_t>(op)]++;
    return std::monostate();
  }
  std::monostate incr_restart_count() {
    exec_kind_count[static_cast<std::size_t>(ExecutionKind::RESTART)]++;
    return std::monostate();
  }
  std::monostate incr_fromsnapshot_count() {
    exec_kind_count[static_cast<std::size_t>(ExecutionKind::FROMSNAPSHOT)]++;
    return std::monostate();
  }
  void print_summary() {
    if (PROFILE_STEP) {
      std::cout << "Profile Summary:" << std::endl;
      std::cout << "Total PUSH operations: "
                << op_count[static_cast<std::size_t>(StepProfileKind::PUSH)]
                << std::endl;
      std::cout << "Total POP operations: "
                << op_count[static_cast<std::size_t>(StepProfileKind::POP)]
                << std::endl;
      std::cout << "Total PEEK operations: "
                << op_count[static_cast<std::size_t>(StepProfileKind::PEEK)]
                << std::endl;
      std::cout << "Total SHIFT operations: "
                << op_count[static_cast<std::size_t>(StepProfileKind::SHIFT)]
                << std::endl;
      std::cout << "Total SET operations: "
                << op_count[static_cast<std::size_t>(StepProfileKind::SET)]
                << std::endl;
      std::cout << "Total GET operations: "
                << op_count[static_cast<std::size_t>(StepProfileKind::GET)]
                << std::endl;
      std::cout << "Total BINARY operations: "
                << op_count[static_cast<std::size_t>(StepProfileKind::BINARY)]
                << std::endl;
      std::cout
          << "Total TREE_FILL operations: "
          << op_count[static_cast<std::size_t>(StepProfileKind::TREE_FILL)]
          << std::endl;
      std::cout
          << "Total CURSOR_MOVE operations: "
          << op_count[static_cast<std::size_t>(StepProfileKind::CURSOR_MOVE)]
          << std::endl;
      std::cout << "Total other instructions executed: " << step_count
                << std::endl;
      std::cout << "Total MEM_GROW operations: "
                << op_count[static_cast<std::size_t>(StepProfileKind::MEM_GROW)]
                << std::endl;
      std::cout << "Total SNAPSHOT_CREATE operations: "
                << op_count[static_cast<std::size_t>(
                       StepProfileKind::SNAPSHOT_CREATE)]
                << std::endl;
      std::cout << "Total SYM_EVAL operations: "
                << op_count[static_cast<std::size_t>(StepProfileKind::SYM_EVAL)]
                << std::endl;
    }
    if (PROFILE_TIME) {
      std::cout << "Time Profile Summary:" << std::endl;
      std::cout << "Total time in instruction execution (s): "
                << std::setprecision(15)
                << time_count[static_cast<std::size_t>(TimeProfileKind::INSTR)]
                << std::endl;
      std::cout << "Total time in solver (s): " << std::setprecision(15)
                << time_count[static_cast<std::size_t>(TimeProfileKind::SOLVER)]
                << std::endl;
      std::cout << "Total time in resuming from snapshot (s): "
                << std::setprecision(15)
                << time_count[static_cast<std::size_t>(
                       TimeProfileKind::RESUME_SNAPSHOT)]
                << std::endl;
      std::cout << "Total time in counting symbolic size (s): "
                << std::setprecision(15)
                << time_count[static_cast<std::size_t>(
                       TimeProfileKind::COUNT_SYM_SIZE)]
                << std::endl;
      std::cout << "Total time in splitting path conditions (s): "
                << std::setprecision(15)
                << time_count[static_cast<std::size_t>(
                       TimeProfileKind::SPLIT_CONDITIONS)]
                << std::endl;
    }
    if (PROFILE_CACHE) {
      std::cout << "Solver Cache Summary:" << std::endl;
      std::cout << "Total cache hits: " << cache_hit_count << std::endl;
      std::cout << "Total cache misses: " << cache_miss_count << std::endl;
      std::cout << "Cache hit rate: "
                << static_cast<double>(cache_hit_count) /
                       static_cast<double>(cache_hit_count + cache_miss_count)
                << std::endl;
    }
    std::cout << "Execution Kind Summary:" << std::endl;
    std::cout
        << "Total RESTART executions: "
        << exec_kind_count[static_cast<std::size_t>(ExecutionKind::RESTART)]
        << std::endl;
    std::cout << "Total FROMSNAPSHOT executions: "
              << exec_kind_count[static_cast<std::size_t>(
                     ExecutionKind::FROMSNAPSHOT)]
              << std::endl;
  }

  void write_as_json(std::ostream &os) const {
    os << "  \"profile_summary\": {\n";
    if (PROFILE_STEP) {
      os << "    \"total_push_operations\": "
         << op_count[static_cast<std::size_t>(StepProfileKind::PUSH)] << ",\n";
      os << "    \"total_pop_operations\": "
         << op_count[static_cast<std::size_t>(StepProfileKind::POP)] << ",\n";
      os << "    \"total_peek_operations\": "
         << op_count[static_cast<std::size_t>(StepProfileKind::PEEK)] << ",\n";
      os << "    \"total_shift_operations\": "
         << op_count[static_cast<std::size_t>(StepProfileKind::SHIFT)] << ",\n";
      os << "    \"total_set_operations\": "
         << op_count[static_cast<std::size_t>(StepProfileKind::SET)] << ",\n";
      os << "    \"total_get_operations\": "
         << op_count[static_cast<std::size_t>(StepProfileKind::GET)] << ",\n";
      os << "    \"total_binary_operations\": "
         << op_count[static_cast<std::size_t>(StepProfileKind::BINARY)]
         << ",\n";
      os << "    \"total_tree_fill_operations\": "
         << op_count[static_cast<std::size_t>(StepProfileKind::TREE_FILL)]
         << ",\n";
      os << "    \"total_cursor_move_operations\": "
         << op_count[static_cast<std::size_t>(StepProfileKind::CURSOR_MOVE)]
         << ",\n";
      os << "    \"total_other_instructions_executed\": " << step_count
         << ",\n";
      os << "    \"total_mem_grow_operations\": "
         << op_count[static_cast<std::size_t>(StepProfileKind::MEM_GROW)]
         << ",\n";
      os << "    \"total_snapshot_create_operations\": "
         << op_count[static_cast<std::size_t>(StepProfileKind::SNAPSHOT_CREATE)]
         << ",\n";
      os << "    \"total_sym_eval_operations\": "
         << op_count[static_cast<std::size_t>(StepProfileKind::SYM_EVAL)]
         << "\n";
    }
    if (PROFILE_TIME) {
      os << "    \"total_time_instruction_execution_s\": "
         << std::setprecision(15)
         << time_count[static_cast<std::size_t>(TimeProfileKind::INSTR)]
         << ",\n";
      os << "    \"total_time_solver_s\": " << std::setprecision(15)
         << time_count[static_cast<std::size_t>(TimeProfileKind::SOLVER)]
         << ",\n";
      os << "    \"total_time_resuming_from_snapshot_s\": "
         << std::setprecision(15)
         << time_count[static_cast<std::size_t>(
                TimeProfileKind::RESUME_SNAPSHOT)]
         << ",\n";
      os << "    \"total_time_counting_symbolic_size_s\": "
         << std::setprecision(15)
         << time_count[static_cast<std::size_t>(
                TimeProfileKind::COUNT_SYM_SIZE)]
         << "\n";
    }
    if (PROFILE_CACHE) {
      os << "    \"total_cache_hits\": " << cache_hit_count << ",\n";
      os << "    \"total_cache_misses\": " << cache_miss_count << ",\n";
      os << "    \"cache_hit_rate\": "
         << static_cast<double>(cache_hit_count) /
                static_cast<double>(cache_hit_count + cache_miss_count)
         << "\n";
    }
    os << "  }\n";
  }

  // record the time spent in main instruction execution, in seconds
  void add_instruction_time(TimeProfileKind kind, double time) {
    time_count[static_cast<std::size_t>(kind)] += time;
  }

  void remove_instruction_time(TimeProfileKind kind, double time) {
    time_count[static_cast<std::size_t>(kind)] -= time;
  }

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
};

static Profile_t Profile;

class ManagedTimer {
public:
  ManagedTimer() = delete;
  ManagedTimer(TimeProfileKind kind) : kind(kind) {
    start = std::chrono::high_resolution_clock::now();
  }
  ~ManagedTimer() {
    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double> elapsed = end - start;
    Profile.add_instruction_time(kind, elapsed.count());
  }

private:
  TimeProfileKind kind;
  std::chrono::high_resolution_clock::time_point start;
};

using Time = std::chrono::time_point<std::chrono::steady_clock>;

inline Time getCurrentTime() { return std::chrono::steady_clock::now(); }

inline double duration_time(Time start, Time end) {
  std::chrono::duration<double, std::milli> duration = end - start;
  return duration.count();
}

struct CostManager_t {
  Time start_time;

  CostManager_t() : start_time() {}

  std::monostate reset_timer() {
    start_time = getCurrentTime();
    return std::monostate();
  }

  double dump_instr_cost() {
    auto current = getCurrentTime();
    double duration = duration_time(start_time, current);
    reset_timer();
    return normalize_cost(duration);
  }

  double normalize_cost(double cost) {
    // Just return duration time as it is
    return 1 * cost;
  }
};

static CostManager_t CostManager;

#endif // PROFILE_HPP