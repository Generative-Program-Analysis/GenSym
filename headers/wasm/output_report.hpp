#ifndef WASM_OUTPUT_REPORT_HPP
#define WASM_OUTPUT_REPORT_HPP

#include "profile.hpp"
#include "symbolic_rt.hpp"
#include "config.hpp"
#include <filesystem>

inline void dump_all_summary_json(const Profile_t &profile,
                                  const OverallResult &overall) {
  // use environment variable OUTPUT_FILE to config particular output profiling file
  const char *output_file = std::getenv("OUTPUT_FILE");
  if (output_file == nullptr) {
    return;
  }

  std::filesystem::path report_path(output_file);

  auto parent = report_path.parent_path();
  if (!parent.empty()) {
    std::error_code ec;
    std::filesystem::create_directories(parent, ec);
    if (ec) {
      throw std::runtime_error("Failed to create output directory: " +
                               ec.message());
    }
  }

  std::ofstream ofs(report_path);
  if (!ofs.is_open()) {
    throw std::runtime_error("Failed to open " + report_path.string() +
                             " for writing");
  }

  // Simple JSON dump (pretty-printed)
  ofs << "{\n";
  ofs << "  \"unexplored_count\": " << overall.unexplored_count << ",\n";
  ofs << "  \"finished_count\": " << overall.finished_count << ",\n";
  ofs << "  \"failed_count\": " << overall.failed_count << ",\n";
  ofs << "  \"not_to_explore_count\": " << overall.not_to_explore_count
      << ",\n";
  ofs << "  \"unreachable_count\": " << overall.unreachable_count;
  if (PROFILE_STEP || PROFILE_TIME) {
    ofs << ",\n";
    profile.write_as_json(ofs);
  }
  ofs << "}\n";
  ofs.close();
}
#endif // WASM_OUTPUT_REPORT_HPP