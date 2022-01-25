#ifndef LLSC_CLI_HEADERS
#define LLSC_CLI_HEADERS

inline bool use_solver = true;
inline bool use_global_solver = false;
inline bool use_objcache = true;
inline bool use_cexcache = true;
inline bool use_cons_indep = false;
inline bool exlib_failure_branch = false;

// XXX: can also specify symbolic argument here?
inline void handle_cli_args(int argc, char** argv) {
  extern char *optarg;
  int c;
  while (1) {
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
      {0,                      0,                 0, 0  }
    };
    int option_index = 0;

    c = getopt_long(argc, argv, "", long_options, &option_index);

    /* Detect the end of the options. */
    if (c == -1)
      break;

    switch (c)
    {
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
      case '+':
        initial_fs.add_file(make_SymFile(std::string(optarg), default_sym_file_size));
#ifdef DEBUG
        printf("adding symfile: %s with size %d\n", optarg, default_sym_file_size);
#endif
        break;
      case 's':
        default_sym_file_size = atoi(optarg);
#ifdef DEBUG
        printf("set symfile size to %d\n", default_sym_file_size);
#endif
        break;
      case '?':
        // parsing error, should be printed by getopt
      default:
        printf("usage: %s <#threads> [--disable-solver] [--exlib-failure-branch]\n", argv[0]);
        exit(-1);
    }

  }
  // FIXME: if #thread is not given, the program silently continues, which should not
  // parsing non-options
  if (optind < argc) {
    if (optind != argc - 1) {
      // more than one non-options
      printf("usage: %s <#threads> [--disable-solver] [--exlib-failure-branch]\n", argv[0]);
      exit(-1);
    }
    int t = std::stoi(argv[optind]);
    if (t <= 0) {
      std::cout << "Invalid #threads, use 1 instead.\n";
      max_par_num = 0;
    } else {
      max_par_num = t - 1;
    }
  }
  if (max_par_num == 0) {
    // It is safe the reuse the global_vc object within one thread, but not otherwise.
    std::cout << "Use global solver\n";
    use_global_solver = true;
  } else {
    std::cout << "Use " << max_par_num << " (additional) threads\n";
    tp.init(max_par_num);
  }
  use_objcache = use_objcache && use_global_solver;
  use_cexcache = use_cexcache && use_global_solver;
#ifdef DEBUG
  std::cout << initial_fs << std::endl;
#endif
}

#endif
