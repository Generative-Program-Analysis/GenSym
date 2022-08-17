#ifndef LLSC_END_HEADER
#define LLSC_END_HEADER

inline void prelude(int argc, char** argv) {
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
