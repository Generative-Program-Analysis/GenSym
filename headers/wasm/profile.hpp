#ifndef PROFILE_HPP
#define PROFILE_HPP

#include "config.hpp"
#include "utils.hpp"
#include <array>
#include <chrono>
#include <iomanip>
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
  OperationCount // keep this as the last element, this is used to get the
                 // number of kinds of operations
};

enum class TimeProfileKind {
  INSTR,
  SOLVER,
  RESUME_SNAPSHOT,
  COUNT_SYM_SIZE,
  TimeOperationCount // keep this as the last element, this is used to get the
                     // number of kinds of operations
};

class Profile_t {
public:
  Profile_t() : step_count(0) {}
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
      std::cout << "Total time for instruction execution (s): "
                << std::setprecision(15) << execution_time << std::endl;
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
    }
  }

  // record the time spent in main instruction execution, in seconds
  void add_instruction_time(TimeProfileKind kind, double time) {
    time_count[static_cast<std::size_t>(kind)] += time;
  }

private:
  int step_count;
  std::array<int, static_cast<std::size_t>(StepProfileKind::OperationCount)>
      op_count;
  std::array<double,
             static_cast<std::size_t>(TimeProfileKind::TimeOperationCount)>
      time_count;
  double execution_time = 0.0;
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

struct CostManager_t {
  int instr_cost;

  CostManager_t() : instr_cost(0) {}

  std::monostate add_instr_cost(int n) {
    instr_cost += n;
    return {};
  }

  int dump_instr_cost() {
    auto cost = instr_cost;
    instr_cost = 0;
    return normalize_cost(cost);
  }

  int normalize_cost(int cost) { return 1 * cost; }
};

static CostManager_t CostManager;

#endif // PROFILE_HPP