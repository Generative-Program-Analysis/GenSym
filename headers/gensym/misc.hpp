#ifndef GS_MISC_HEADER
#define GS_MISC_HEADER

inline void prelude(int argc, char** argv) {
  ASSERT(sizeof(long double) >= 10, "Require fp80 support");
  inc_stack(STACKSIZE_128MB);
  init_rand();
  handle_cli_args(argc, argv);
  init_solvers();
  cov().start_monitor();
}

inline void epilogue() {
  if (can_par_tp()) {
    tp.wait_for_tasks();
  }
  cov().stop_monitor();
  cov().print_all(true);
}

#endif
