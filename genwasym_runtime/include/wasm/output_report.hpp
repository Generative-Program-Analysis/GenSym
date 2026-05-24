#ifndef WASM_OUTPUT_REPORT_HPP
#define WASM_OUTPUT_REPORT_HPP

#include "config.hpp"
#include "profile.hpp"
#include "sym_rt.hpp"

void dump_all_summary_json(const Profile_t &profile,
                           const OverallResult &overall);

#endif // WASM_OUTPUT_REPORT_HPP