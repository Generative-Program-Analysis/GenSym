#include "wasm/profile.hpp"

#include <filesystem>
#include <fstream>
#include <iomanip>
#include <iostream>
#include <stdexcept>
#include <string>

Profile_t Profile;
CostManager_t CostManager;

Profile_t::Profile_t() : step_count(0), cache_hit_count(0), cache_miss_count(0),
                         call_solver_count(0) {
  op_count.fill(0);
  time_count.fill(0.0);
  exec_kind_count.fill(0);

  // refresh the output profile directory
  if (PROFILE_Z3_API_CALL) {
    std::filesystem::path out_path(base_profile_output_path);
    std::error_code ec;
    std::filesystem::remove_all(out_path, ec);
    if (ec) {
      throw std::runtime_error("Failed to clear output directory: " +
                               ec.message());
    }

    std::filesystem::create_directories(out_path, ec);
    if (ec) {
      throw std::runtime_error("Failed to create output directory: " +
                               ec.message());
    }

    std::string record_file =
        base_profile_output_path + "/z3_solver_time_record.csv";
    std::ofstream ofs(record_file);
    ofs << "Expression file,time spent (s),is_sat\n";
    ofs.close();

    std::filesystem::create_directories(z3_expr_output_path, ec);
    if (ec) {
      throw std::runtime_error("Failed to create z3 expr output directory: " +
                               ec.message());
    }
  }
}

void Profile_t::cache_hit() {
  if (PROFILE_CACHE)
    cache_hit_count++;
}

void Profile_t::cache_miss() {
  if (PROFILE_CACHE)
    cache_miss_count++;
}

std::monostate Profile_t::step() {
  if (PROFILE_STEP)
    step_count++;
  return std::monostate();
}

std::monostate Profile_t::step(StepProfileKind op) {
  if (PROFILE_STEP)
    op_count[static_cast<std::size_t>(op)]++;
  return std::monostate();
}

std::monostate Profile_t::incr_restart_count() {
  exec_kind_count[static_cast<std::size_t>(ExecutionKind::RESTART)]++;
  return std::monostate();
}

std::monostate Profile_t::incr_fromsnapshot_count() {
  exec_kind_count[static_cast<std::size_t>(ExecutionKind::FROMSNAPSHOT)]++;
  return std::monostate();
}

std::monostate Profile_t::incr_call_solver_count() {
  call_solver_count++;
  return std::monostate();
}

void Profile_t::print_summary() {
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
    std::cout << "Total TREE_FILL operations: "
              << op_count[static_cast<std::size_t>(StepProfileKind::TREE_FILL)]
              << std::endl;
    std::cout << "Total CURSOR_MOVE operations: "
              << op_count[static_cast<std::size_t>(
                     StepProfileKind::CURSOR_MOVE)]
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
    std::cout
        << "Total time in solver (s): " << std::setprecision(15)
        << time_count[static_cast<std::size_t>(TimeProfileKind::SOLVER_TOTAL)]
        << std::endl;
    std::cout << "Total time in z3 api call (s): " << std::setprecision(15)
              << time_count[static_cast<std::size_t>(
                     TimeProfileKind::CALL_Z3_SOLVER)]
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
    std::cout << "Total time in collecting path conditions (s): "
              << std::setprecision(15)
              << time_count[static_cast<std::size_t>(
                     TimeProfileKind::COLLECT_PATH_CONDITIONS)]
              << std::endl;
    std::cout
        << "Total time in main loop (s): " << std::setprecision(15)
        << time_count[static_cast<std::size_t>(TimeProfileKind::MAIN_LOOP)]
        << std::endl;
  }

  if (PROFILE_CACHE) {
    std::cout << "Solver Cache Summary:" << std::endl;
    std::cout << "Total cache hits: " << cache_hit_count << std::endl;
    std::cout << "Total cache misses: " << cache_miss_count << std::endl;
    std::cout << "Time of making conjunctions (s): " << std::setprecision(15)
              << time_count[static_cast<std::size_t>(
                     TimeProfileKind::MAKE_CONJUNCTION)]
              << std::endl;
    std::cout << "Cache hit rate: "
              << static_cast<double>(cache_hit_count) /
                     static_cast<double>(cache_hit_count + cache_miss_count)
              << std::endl;
  }

  if (PROFILE_PATH_CONDS) {
    std::cout << "Path Conditions Profile Summary:" << std::endl;
    std::cout << "Total time in collecting path conditions (s): "
              << std::setprecision(15)
              << time_count[static_cast<std::size_t>(
                     TimeProfileKind::COLLECT_PATH_CONDITIONS)]
              << std::endl;
  }

  std::cout << "Number of calls to solver: " << call_solver_count << std::endl;
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

void Profile_t::write_as_json(std::ostream &os) const {
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
       << time_count[static_cast<std::size_t>(
              TimeProfileKind::CALL_Z3_SOLVER)]
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
    os << "    \"total_time_splitting_path_conditions_s\": "
       << std::setprecision(15)
       << time_count[static_cast<std::size_t>(
              TimeProfileKind::SPLIT_CONDITIONS)]
       << ",\n";
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

void Profile_t::record_z3_solver_time(z3::solver expr, double time,
                                      bool is_sat) {
  if (PROFILE_Z3_API_CALL) {
    static int count = 0;
    std::string expr_file =
        z3_expr_output_path + "/z3_expr_" + std::to_string(count) + ".smt2";
    std::error_code ec;
    std::ofstream ofs(expr_file);
    ofs << expr;
    ofs.close();

    std::string record_file =
        base_profile_output_path + "/z3_solver_time_record.csv";
    std::ofstream rofs(record_file, std::ios::app);
    rofs << expr_file << "," << std::setprecision(15) << time << ","
         << (is_sat ? "sat" : "unsat") << "\n";
    rofs.close();

    count++;
  }
}

void Profile_t::add_instruction_time(TimeProfileKind kind, double time) {
  time_count[static_cast<std::size_t>(kind)] += time;
}

void Profile_t::remove_instruction_time(TimeProfileKind kind, double time) {
  time_count[static_cast<std::size_t>(kind)] -= time;
}

ManagedTimer::ManagedTimer(TimeProfileKind kind)
    : kind(kind), time_ref(nullptr) {
  start = std::chrono::high_resolution_clock::now();
}

ManagedTimer::ManagedTimer(TimeProfileKind kind, double &time_ref)
    : kind(kind), time_ref(&time_ref) {
  start = std::chrono::high_resolution_clock::now();
}

ManagedTimer::~ManagedTimer() {
  auto end = std::chrono::high_resolution_clock::now();
  std::chrono::duration<double> elapsed = end - start;
  Profile.add_instruction_time(kind, elapsed.count());
  if (time_ref != nullptr) {
    *time_ref += elapsed.count();
  }
}

Time getCurrentTime() {
  return std::chrono::steady_clock::now();
}

double duration_time(Time start, Time end) {
  std::chrono::duration<double, std::milli> duration = end - start;
  return duration.count();
}

CostManager_t::CostManager_t() : start_time() {}

std::monostate CostManager_t::reset_timer() {
  start_time = getCurrentTime();
  return std::monostate();
}

double CostManager_t::dump_instr_cost() {
  auto current = getCurrentTime();
  double duration = duration_time(start_time, current);
  reset_timer();
  return normalize_cost(duration);
}

double CostManager_t::normalize_cost(double cost) {
  return 1 * cost;
}