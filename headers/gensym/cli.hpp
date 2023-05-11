#ifndef GS_CLI_HEADER
#define GS_CLI_HEADER

/* TODO: generate a file containing generated function declarations <2022-05-24, David Deng> */
FS set_file(FS, String, Ptr<File>);
Ptr<File> set_file_type(Ptr<File>, int);
inline int n_sym_files = 0;
inline int n_sym_stdout = 0;
inline int n_sym_stdin = 0;

// TODO: "--stack-size" to set stack size using `inc_stack`.

static struct option long_options[] =
{
  {"help",                       no_argument,       0, 22},
  // Optimizations
  {"no-hash-cons",               no_argument,       0, 2},
  {"no-obj-cache",               no_argument,       0, 3},
  {"no-cex-cache",               no_argument,       0, 4},
  {"no-br-cache",                no_argument,       0, 27},
  {"no-cons-indep",              no_argument,       0, 5},
  {"simplify",                   no_argument,       0, 26},
  // Test case generation
  {"output-tests-cov-new",       no_argument,       0, 6},
  {"output-ktest",               no_argument,       0, 7},
  {"output-readable-file-tests", no_argument,       0, 10},
  // Scheduling
  {"search-strategy",            required_argument, 0, 11},
  {"thread",                     required_argument, 0, 17},
  {"queue",                      required_argument, 0, 18},
  {"timeout",                    required_argument, 0, 20},
  // Symbolic behavior
  {"exlib-failure-branch",       no_argument,       0, 1},
  {"symloc-strategy",            required_argument, 0, 12},
  {"solver",                     required_argument, 0, 19},
  {"max-sym-array-size",         required_argument, 0, 24},
  {"symbolic-uninit",            no_argument,       0, 29},
  // Symbolic inputs
  {"add-sym-file",               required_argument, 0, 13},
  {"sym-file-size",              required_argument, 0, 14},
  {"sym-stdin",                  required_argument, 0, 15},
  {"sym-stdout",                 no_argument,       0, 16},
  {"argv",                       required_argument, 0, 21},
  // Logging
  {"print-inst-count",           no_argument,       0, 8},
  {"print-cov",                  no_argument,       0, 9},
  {"print-detailed-log",         required_argument, 0, 25},
  {"output-dir",                 required_argument, 0, 23},
  {"no-stdout-log",              no_argument,       0, 28},
  // Next 30
  {0,                            0,                 0, 0 }
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
      case 1:
        exlib_failure_branch = true;
        break;
      case 2:
        use_hashcons = false;
        break;
      case 3:
        use_objcache = false;
        break;
      case 4:
        use_cexcache = false;
        break;
      case 5:
        use_cons_indep = false;
        break;
      case 6:
        only_output_covernew = true;
        break;
      case 7:
        output_ktest = true;
        break;
      case 8:
        print_inst_cnt = true;
        break;
      case 9:
        print_cov_detail = true;
        break;
      case 10:
        readable_file_tests = true;
        break;
      case 11: {
        auto searcher = std::string(optarg);
        set_searcher(searcher);
        break;
      }
      case 12: {
        auto strategy = std::string(optarg);
        set_symloc_strategy(strategy);
        break;
      }
      case 13:
        initial_fs = set_file(initial_fs, std::string("/") + optarg, set_file_type(make_SymFile(optarg, default_sym_file_size), S_IFREG));
        initial_fs.sym_objs = initial_fs.sym_objs.push_back(SymObj(std::string(optarg) + "-data", default_sym_file_size, false));
        initial_fs.sym_objs = initial_fs.sym_objs.push_back(SymObj(std::string(optarg) + "-data-stat", stat_size, false));
        n_sym_files++;
        INFO("adding symfile: " << optarg << " with size " << default_sym_file_size);
        INFO("initial_fs.preferred_cex: " << (vec_to_string<List, PtrVal>(initial_fs.preferred_cex)));
        break;
      case 14:
        default_sym_file_size = atoi(optarg);
        INFO("set symfile size to " << default_sym_file_size << "\n");
        break;
      case 15: {
        int size = atoi(optarg);
        n_sym_stdin = (size < 0) ? 0 : size;
        initial_fs.set_stdin(n_sym_stdin);
        INFO("set stdin size to " << n_sym_stdin << "\n");
        break;
      }
      case 16: {
        n_sym_stdout = 1024;
        initial_fs.set_stdout(n_sym_stdout);
        INFO("set stdout size to " << n_sym_stdout << "\n");
        break;
      }
      case 17: {
        int t = atoi(optarg);
        n_thread = (t <= 0) ? 1 : t;
        if (n_thread > 0) use_thread_pool = true;
        break;
      }
      case 18: {
        int n = atoi(optarg);
        n_queue = n;
        break;
      }
      case 19: {
        auto solver = std::string(optarg);
        set_solver(solver);
        break;
      }
      case 20:
        timeout = atoi(optarg);
        break;
      case 21:
        cli_argv = parse_args(std::string(optarg));
        g_argv = make_LocV(0, LocV::kStack, cli_argv.size() * 8, 0); // The global argv, pass to gs_main
        g_argc = make_IntV(cli_argv.size());
        break;
      case 22:
        print_help(argv[0]);
        exit(-1);
      case 23:
        output_dir_str = std::string(optarg);
        break;
      case 24: {
        int n = atoi(optarg);
        max_sym_array_size = (n > 0) ? n : 0;
        break;
      }
      case 25:
        print_detailed_log = atoi(optarg);
        break;
      case 26:
        use_symv_simplify = true;
        break;
      case 27:
        use_brcache = false;
        break;
      case 28:
        stdout_log = false;
        break;
      case 29:
        symbolic_uninit = true;
        break;
      case '?':
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
  // symargs -> symfiles -> sym-stdin -> sym-stdout (must be in this order for klee-replay to work)

  // TODO: the following code should be refactored in favor of readability and mantainance.

  // count number of symbolic arguments, create one SymObj for each.
  int n_sym_arg = 0;
  TrList<SymObj> symargs;
  for (int i = 0; i < cli_argv.size(); ++i) {
    // only check the first byte for symbolic value, as KLEE doesn't support mixed values
    // we should implement our own replay tool.
    if (std::dynamic_pointer_cast<SymV>(cli_argv[i][0])) {
      std::ostringstream ss;
      ss << "arg" << std::setw(2) << std::setfill('0') << n_sym_arg;
      //std::cout << "ss.str(): " << ss.str() << std::endl;
      symargs.push_back(SymObj(ss.str() , cli_argv[i].size(), false));
      n_sym_arg++;
    }
  }
  initial_fs.sym_objs = symargs.persistent() + initial_fs.sym_objs;

  if (n_sym_stdin > 0) {
    initial_fs.sym_objs = initial_fs.sym_objs.push_back(SymObj("stdin", n_sym_stdin, false));
    initial_fs.sym_objs = initial_fs.sym_objs.push_back(SymObj("stdin-stat", stat_size, false));
  }
  if (n_sym_stdout > 0) {
    initial_fs.sym_objs = initial_fs.sym_objs.push_back(SymObj("stdout", n_sym_stdout, false));
    initial_fs.sym_objs = initial_fs.sym_objs.push_back(SymObj("stdout-stat", stat_size, false));
  }
  if (output_ktest && (cli_argv.size() > 0)) {
    const int extra_args = 3 + 3;
    // + 3 for -sym-files n m, + 3 for -sym-stdout, -sym-stdin n
    // each symarg also requires an additional slot

    g_conc_argc = cli_argv.size() + extra_args + n_sym_arg;
    g_conc_argv = new char* [g_conc_argc + extra_args + n_sym_arg];
    INFO("g_conc_argc: " << g_conc_argc);
    auto cli_iter = cli_argv.begin();
    int i = 0;
    for (; i < g_conc_argc - extra_args; i++, cli_iter++) {
      INFO("i: " << i);
      if (std::dynamic_pointer_cast<SymV>((*cli_iter)[0])) {
        INFO("symbolic argument");
        // symbolic argument
        g_conc_argv[i]   = new char [9] {'-', 's', 'y', 'm', '-', 'a', 'r', 'g'};
        INFO("*cli_iter: " << vec_to_string(*cli_iter));
        int nargs = cli_iter->size() - 1; // excluding the null byte at the end
        std::string nargs_str = std::to_string(nargs);
        INFO("nargs_str: " << nargs_str);

        g_conc_argv[i+1] = new char [nargs_str.size() + 1];
        memcpy(g_conc_argv[i+1], nargs_str.c_str(), nargs_str.size());
        g_conc_argv[i+1][nargs_str.size()] = '\0';

        i++;
        continue;
      } else {
        INFO("concrete argument");
        // concrete argument
        int size = (*cli_iter).size();
        char* cur_argv = new char[size];
        for (int j = 0; j < size; j++) {
          ASSERT((*cli_iter)[j]->get_bw() == 8, "Bitwidth mismatch");
          cur_argv[j] = static_cast<unsigned char>(proj_IntV((*cli_iter)[j]));
        }
        cur_argv[size - 1] = 0;
        g_conc_argv[i] = cur_argv;
      }
    }
    INFO("Done converting arguments");
    INFO("i: " << i);
    INFO("n_sym_files: " << n_sym_files);
    // -sym-files
    if (n_sym_files > 0) {
      g_conc_argv[i] = new char [11] {'-', 's', 'y', 'm', '-', 'f', 'i', 'l', 'e', 's'};

      auto n_sym_files_str = std::to_string(n_sym_files);
      g_conc_argv[i+1] = new char [n_sym_files_str.size() + 1];
      memcpy(g_conc_argv[i+1], n_sym_files_str.c_str(), n_sym_files_str.size());
      g_conc_argv[i+1][n_sym_files_str.size()] = '\0';

      auto default_sym_file_size_str = std::to_string(default_sym_file_size);
      g_conc_argv[i+2] = new char [default_sym_file_size_str.size() + 1];
      memcpy(g_conc_argv[i+2], default_sym_file_size_str.c_str(), default_sym_file_size_str.size());
      g_conc_argv[i+2][default_sym_file_size_str.size()] = '\0';
      i += 3;
    } else {
      g_conc_argc -= 3;
    }
    //std::cout << "i: " << i << std::endl;
    //std::cout << "n_sym_stdin: " << n_sym_stdin << std::endl;
    // -sym-stdin n
    if (n_sym_stdin > 0) {
      g_conc_argv[i] = new char [11] {'-', 's', 'y', 'm', '-', 's', 't', 'd', 'i', 'n'};

      auto n_sym_stdin_str = std::to_string(n_sym_stdin);
      g_conc_argv[i+1] = new char [n_sym_stdin_str.size() + 1];
      memcpy(g_conc_argv[i+1], n_sym_stdin_str.c_str(), n_sym_stdin_str.size());
      g_conc_argv[i+1][n_sym_stdin_str.size()] = '\0';
      i += 2;
    } else {
      g_conc_argc -= 2;
    }
    //std::cout << "n_sym_stdout: " << n_sym_stdout << std::endl;
    // -sym-stdout
    if (n_sym_stdout > 0) {
      g_conc_argv[i] = new char [12] {'-', 's', 'y', 'm', '-', 's', 't', 'd', 'o', 'u', 't'};
      i += 1;
    } else {
      g_conc_argc -= 1;
    }
  }
  INFO(initial_fs);
}

#endif
