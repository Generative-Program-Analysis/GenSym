#ifndef GS_MISC_HEADER
#define GS_MISC_HEADER

inline void init_output_folder() {
  // prepare output folder
  if (mkdir(output_dir_str.c_str(), 0777) == -1) {
    if (errno == EEXIST) { }
    else ABORT("Cannot create the output folder, abort.\n");
  }
  // prepare tests folder
  test_dir_str = output_dir_str + "/tests";
  if (mkdir(test_dir_str.c_str(), 0777) == -1) {
    if (errno == EEXIST) { }
    else ABORT("Cannot create the tests folder, abort.\n");
  }
  // prepare log output stream
  output_log_str = output_dir_str + "/log.txt";
  gs_log.open(output_log_str);
}

inline void prelude(int argc, char** argv) {
  ASSERT(sizeof(long double) >= 10, "Require fp80 support");
  inc_stack(STACKSIZE_128MB);
  init_rand();
  handle_cli_args(argc, argv);
  init_output_folder();
  init_solvers();
  cov().start_monitor();
}

inline void epilogue() {
  if (can_par_tp()) {
    tp.wait_for_tasks();
  }
  cov().stop_monitor();
  cov().print_all(true);
  gs_log.close();
}

#endif
