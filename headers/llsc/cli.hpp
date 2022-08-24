#ifndef LLSC_CLI_HEADER
#define LLSC_CLI_HEADER

/* TODO: generate a file containing generated function declarations <2022-05-24, David Deng> */
FS set_file(FS, String, Ptr<File>);

// TODO: "--stack-size" to set stack size using `inc_stack`.

static struct option long_options[] =
{
  /* These options set a flag. */
  {"exlib-failure-branch",            no_argument,       0, 'f'},
  {"no-hash-cons",                    no_argument,       0, 'H'},
  {"no-obj-cache",                    no_argument,       0, 'O'},
  {"no-cex-cache",                    no_argument,       0, 'C'},
  {"cons-indep",                      no_argument,       0, 'i'},
  {"output-tests-cov-new",            no_argument,       0, 'S'},
  {"output-ktest",                    no_argument,       0, 'K'},
  {"print-inst-count",                no_argument,       0, 'I'},
  {"print-cov",                       no_argument,       0, 'p'},
  {"readable-posix-inputs",           no_argument,       0, 'X'},
  {"search",                          required_argument, 0, 'R'},
  {"symloc-strategy",                 required_argument, 0, 'L'},
  {"add-sym-file",                    required_argument, 0, '+'},
  {"sym-file-size",                   required_argument, 0, 's'},
  {"sym-stdin",                       required_argument, 0, 'n'},
  {"sym-stdout",                       no_argument, 0, 'o'},
  {"thread",                          required_argument, 0, 't'},
  {"queue",                           required_argument, 0, 'q'},
  {"solver",                          required_argument, 0, 'v'},
  {"timeout",                         required_argument, 0, 'e'},
  {"argv",                            required_argument, 0, 'a'},
  {"help",                            no_argument,       0, 'h'},
  {0,                                 0,                 0, 0  }
};

inline void set_searcher(std::string& searcher) {
  if ("random-path" == searcher) {
    searcher_kind = SearcherKind::randomPath;
  } else if ("random-weight" == searcher) {
    searcher_kind = SearcherKind::randomWeight;
  } else {
    ABORT("unknown searcher");
  }
}

inline void set_solver(std::string& solver) {
  if ("z3" == solver) {
    solver_kind = SolverKind::z3;
  } else if ("stp" == solver) {
    solver_kind = SolverKind::stp;
  } else if ("disable" == solver) {
    use_solver = false;
  } else {
    ABORT("unknown solver");
  }
}

inline void set_symloc_strategy(std::string& strategy) {
  if ("one" == strategy) {
    symloc_strategy = SymLocStrategy::one;
  } else if ("feasible" == strategy) {
    symloc_strategy = SymLocStrategy::feasible;
  } else if ("all" == strategy) {
    symloc_strategy = SymLocStrategy::all;
  } else {
    ABORT("unknown symloc strategy");
  }
}

inline void print_help(char* main_name) {
  struct option* p = long_options;
  size_t len = sizeof(long_options) / sizeof(struct option);
  printf("usage: %s ", main_name);
  for (int i = 0; i < len; i++, p++) {
    if (p->name) {
      printf("[--%s", p->name);
      if (p->has_arg == required_argument) {
        std::string key = p->name;
        if (key == "solver") {
          printf("={stp,z3,disable}");
        } else if (key == "symloc-strategy") {
          printf("={one,feasible,all}");
        } else {
          // TODO: doc for other options
          printf("=<value>");
        }
      }
      printf("] ");
    }
  }
  printf("\n");
}

inline void handle_cli_args(int argc, char** argv) {
  extern char *optarg;
  int c;
  while (1) {
    int option_index = 0;
    c = getopt_long(argc, argv, "", long_options, &option_index);

    /* Detect the end of the options. */
    if (c == -1) break;

    switch (c) {
      case 0: break;
      case 'f':
        exlib_failure_branch = true;
        break;
      case 'H':
        use_hashcons = false;
        break;
      case 'O':
        use_objcache = false;
        break;
      case 'C':
        use_cexcache = false;
        break;
      case 'i':
        use_cons_indep = true;
        break;
      case 'S':
        only_output_covernew = true;
        break;
      case 'K':
        output_ktest = true;
        break;
      case 'I':
        print_inst_cnt = true;
        break;
      case 'p':
        print_cov_detail = true;
        break;
      case 'X':
        readable_posix = true;
        break;
      case 'R' : {
        auto searcher = std::string(optarg);
        set_searcher(searcher);
        break;
      }
      case 'L': {
        auto strategy = std::string(optarg);
        set_symloc_strategy(strategy);
        break;
      }
      case '+':
        initial_fs = set_file(initial_fs, std::string("/") + optarg, make_SymFile(optarg, default_sym_file_size));
        INFO("adding symfile: " << optarg << " with size " << default_sym_file_size);
        break;
      case 's':
        default_sym_file_size = atoi(optarg);
        INFO("set symfile size to " << default_sym_file_size << "\n");
        break;
      case 'n': {
        int size = atoi(optarg);
        initial_fs.set_stdin((size < 0) ? 0 : size);
        INFO("set stdin size to " << size << "\n");
        break;
      }
      case 'o': {
        initial_fs.set_stdout(0);
        INFO("set stdout size to " << 0 << "\n");
        break;
      }
      case 't': {
        int t = atoi(optarg);
        n_thread = (t <= 0) ? 1 : t;
        if (n_thread > 0) use_thread_pool = true;
        break;
      }
      case 'q': {
        int n = atoi(optarg);
        n_queue = n;
        break;
      }
      case 'v': {
        auto solver = std::string(optarg);
        set_solver(solver);
        break;
      }
      case 'e':
        timeout = atoi(optarg);
        break;
      case 'a':
        cli_argv = parse_args(std::string(optarg));
        g_argv = make_LocV(0, LocV::kStack, cli_argv.size() * 8, 0); // The global argv, pass to llsc_main
        g_argc = make_IntV(cli_argv.size());
        break;
      case '?':
        // parsing error, should be printed by getopt
      case 'h':
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
  if (!use_thread_pool) {
    // It is safe the reuse the global_vc object within one thread, but not otherwise.
    use_global_solver = true;
    std::cout << "Sequential execution mode; use global solver\n";
  } else {
    // thread pool will create (n_thread) threads, leaving the main thread idle.
    tp.init(n_thread, n_queue);
    std::cout << "Parallel execution mode: " << n_thread << " total threads; " << n_queue << " queues in the thread pool\n";
  }
  if (output_ktest && (cli_argv.size() > 0)) {
    g_conc_argc = cli_argv.size();
    g_conc_argv = new char* [g_conc_argc];
    for (int i = 0; i < g_conc_argc; i++) {
      int size = cli_argv[i].size();
      char* cur_argv = new char[size];
      for (int j = 0; j < size; j++) {
        ASSERT(cli_argv[i][j]->get_bw() == 8, "Bitwidth mismatch");
        cur_argv[j] = static_cast<unsigned char>(proj_IntV(cli_argv[i][j]));
      }
      cur_argv[size - 1] = 0;
      g_conc_argv[i] = cur_argv;
    }
  }
  INFO(initial_fs);
}

#endif
