#ifndef LLSC_CLI_HEADERS
#define LLSC_CLI_HEADERS

inline bool use_solver = true;
inline bool use_global_solver = false;
inline bool use_objcache = true;
inline bool use_cexcache = true;
inline bool use_cons_indep = false;
inline bool exlib_failure_branch = false;

// TODO: "--stack-size" to set stack size using `inc_stack`.

static struct option long_options[] =
{
  /* These options set a flag. */
  {"disable-solver",       no_argument,       0, 'd'},
  {"exlib-failure-branch", no_argument,       0, 'f'},
  {"no-obj-cache",         no_argument,       0, 'O'},
  {"no-cex-cache",         no_argument,       0, 'C'},
  {"cons-indep",           no_argument,       0, 'i'},
  {"add-sym-file",         required_argument, 0, '+'},
  {"sym-file-size",        required_argument, 0, 's'},
  {"thread",               required_argument, 0, 't'},
  {"queue",                required_argument, 0, 'q'},
  {"timeout",              required_argument, 0, 'e'},
  {"argv",                 required_argument, 0, 'a'},
  {0,                      0,                 0, 0  }
};

inline void print_help(char* main_name) {
  struct option* p = long_options;
  size_t len = sizeof(long_options) / sizeof(struct option);
  printf("usage: %s ", main_name);
  for (int i = 0; i < len; i++, p++) {
    if (p->name) {
      printf("[--%s", p->name);
      if (p->has_arg == required_argument)
        printf("=<value>");
      printf("] ");
    }
  }
  printf("\n");
}

// XXX: can also specify symbolic argument here?
inline void handle_cli_args(int argc, char** argv) {
  extern char *optarg;
  int c;
  while (1) {
    int option_index = 0;
    c = getopt_long(argc, argv, "", long_options, &option_index);

    /* Detect the end of the options. */
    if (c == -1) break;

    switch (c) {
      case 0:
        break;
      case 'd':
        use_solver = false;
        break;
      case 'f':
        exlib_failure_branch = true;
        break;
      case 'O':
        use_objcache = false;
      case 'C':
        use_cexcache = false;
        break;
      case 'i':
        use_cons_indep = true;
        break;
      case 'a':
        cli_argv = parse_args(std::string(optarg));
        g_argv = make_LocV(0, LocV::kStack); // The global argv, pass to llsc_main
        g_argc = make_IntV(cli_argv.size());
        break;
      case '+':
        initial_fs.set_file(std::string(optarg), make_SymFile(std::string(optarg), default_sym_file_size));
        INFO("adding symfile: " << optarg << " with size " << default_sym_file_size << "%d\n");
        break;
      case 's':
        default_sym_file_size = atoi(optarg);
        INFO("set symfile size to " << default_sym_file_size << "\n");
        break;
      case 't': {
        int t = atoi(optarg);
        n_thread = (t <= 0) ? 1 : t;
        break;
      }
      case 'q': {
        int n = atoi(optarg);
        n_queue = n;
        break;
      }
      case 'e':
        timeout = atoi(optarg);
        break;
      case '?':
        // parsing error, should be printed by getopt
      default:
        print_help(argv[0]);
        exit(-1);
    }
  }
  // parsing non-options
  if (optind < argc) {
    if (optind != argc - 1) {
      // more than one non-options
      print_help(argv[0]);
      exit(-1);
    }
  }
  if (n_thread == 1) {
    // It is safe the reuse the global_vc object within one thread, but not otherwise.
    std::cout << "Use global solver\n";
    use_global_solver = true;
  }
  std::cout << "Use " << n_thread << " total threads; " << n_queue << " queues in the thread pool\n";
#ifdef USE_TP
  // thread pool will create (n_thread) threads, leaving the main thread idle.
  tp.init(n_thread, n_queue);
#endif
  use_objcache = use_objcache && use_global_solver;
  use_cexcache = use_cexcache && use_global_solver;
  INFO(initial_fs);
}

#endif
