#ifndef PROFILE_HPP
#define PROFILE_HPP

#include "config.hpp"
#include "utils.hpp"
#include <array>
#include <chrono>
#include <iomanip>
#include <variant>

enum class ProfileKind {
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

class Profile_t {
public:
  Profile_t() : step_count(0) {}
  std::monostate step() {
    if (PROFILE_ENABLED)
      step_count++;
    return std::monostate();
  }
  std::monostate step(ProfileKind op) {
    if (PROFILE_ENABLED)
      op_count[static_cast<std::size_t>(op)]++;
    return std::monostate();
  }
  void print_summary() {
    if (PROFILE_ENABLED) {
      std::cout << "Profile Summary:" << std::endl;
      std::cout << "Total PUSH operations: "
                << op_count[static_cast<std::size_t>(ProfileKind::PUSH)]
                << std::endl;
      std::cout << "Total POP operations: "
                << op_count[static_cast<std::size_t>(ProfileKind::POP)]
                << std::endl;
      std::cout << "Total PEEK operations: "
                << op_count[static_cast<std::size_t>(ProfileKind::PEEK)]
                << std::endl;
      std::cout << "Total SHIFT operations: "
                << op_count[static_cast<std::size_t>(ProfileKind::SHIFT)]
                << std::endl;
      std::cout << "Total SET operations: "
                << op_count[static_cast<std::size_t>(ProfileKind::SET)]
                << std::endl;
      std::cout << "Total GET operations: "
                << op_count[static_cast<std::size_t>(ProfileKind::GET)]
                << std::endl;
      std::cout << "Total BINARY operations: "
                << op_count[static_cast<std::size_t>(ProfileKind::BINARY)]
                << std::endl;
      std::cout << "Total TREE_FILL operations: "
                << op_count[static_cast<std::size_t>(ProfileKind::TREE_FILL)]
                << std::endl;
      std::cout << "Total CURSOR_MOVE operations: "
                << op_count[static_cast<std::size_t>(ProfileKind::CURSOR_MOVE)]
                << std::endl;
      std::cout << "Total other instructions executed: " << step_count
                << std::endl;
      std::cout << "Total MEM_GROW operations: "
                << op_count[static_cast<std::size_t>(ProfileKind::MEM_GROW)]
                << std::endl;
      std::cout
          << "Total SNAPSHOT_CREATE operations: "
          << op_count[static_cast<std::size_t>(ProfileKind::SNAPSHOT_CREATE)]
          << std::endl;
      std::cout << "Total time for instruction execution (s): "
                << std::setprecision(15) << execution_time << std::endl;
    }
  }

  // record the time spent in main instruction execution, in seconds
  void add_instruction_time(double time) {
#ifdef ENABLE_PROFILE
    execution_time += time;
#endif
  }

private:
  int step_count;
  std::array<int, static_cast<std::size_t>(ProfileKind::OperationCount)>
      op_count;
  double execution_time = 0.0;
};

static Profile_t Profile;

class ManagedTimer {
public:
  ManagedTimer() { start = std::chrono::high_resolution_clock::now(); }
  ~ManagedTimer() {
    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double> elapsed = end - start;
    Profile.add_instruction_time(elapsed.count());
  }

private:
  std::chrono::high_resolution_clock::time_point start;
};

#endif // PROFILE_HPP