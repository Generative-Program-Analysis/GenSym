#ifndef LLSC_END_HEADERS
#define LLSC_END_HEADERS

inline void prelude(int argc, char** argv) {
  init_rand();
  handle_cli_args(argc, argv);
#ifdef Z3
  cz3.init_solvers();
#endif
  cov.start_monitor();
}

inline void epilogue() {
#ifdef USE_TP
  tp.wait_for_tasks();
#endif
  cov.stop_monitor();
  cov.print_all();
}

#endif
