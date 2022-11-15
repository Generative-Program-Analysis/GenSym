#ifndef GS_MON_HEADER
#define GS_MON_HEADER

/* Coverage information */

struct Monitor {
  private:
    using BlockId = uint64_t;
    using BranchId = uint64_t;
    // Total number of blocks
    uint64_t num_blocks;
    // The number of execution for each block
    std::vector<std::atomic_uint64_t> block_cov;
    // The number of execution for each branch
    std::map<BlockId, std::map<BranchId, std::atomic_uint64_t>> branch_cov;
    // Number of discovered paths
    std::atomic_uint64_t num_paths;
    // Number of executed instructions
    std::atomic_uint64_t num_insts;
    // Number of states
    std::atomic_uint64_t num_states;
    // Starting time
    steady_clock::time_point start, stop;
    std::thread watcher;
    std::promise<void> signal_exit;

  public:
    Monitor() : num_blocks(0), num_paths(0), num_states(1), start(steady_clock::now()) {}
    Monitor(uint64_t num_blocks, const std::vector<std::pair<unsigned, unsigned>> &branch_num) :
      num_blocks(num_blocks), num_paths(0), num_states(1),
      block_cov(num_blocks),
      start(steady_clock::now()) {
      extend_blocks(num_blocks, branch_num);
    }

    void extend_blocks(uint64_t nblks, const std::vector<std::pair<unsigned, unsigned>> &branch_num) {
      if (num_blocks != nblks) block_cov = std::move(decltype(block_cov)(num_blocks = nblks));
      // `branch_num` contains the ids of blocks whose terminator is br/switch,
      // for each of such block, `br_arity` is the number of branches.
      for (const auto& [blk_id, br_arity] : branch_num) {
        branch_cov[blk_id] = std::map<BranchId, std::atomic_uint64_t>();
        ASSERT(br_arity > 0, "Wrong number of branches");
        for (auto i = 0; i < br_arity; i++) {
          branch_cov[blk_id][i] = 0;
        }
      }
    }

    void inc_block(BlockId b) {
      block_cov[b]++;
    }
    bool is_uncovered(BlockId b) {
      return 0 == block_cov[b];
    }
    void inc_branch(BlockId b, BranchId x) {
      branch_cov[b][x]++;
    }
    void inc_path(size_t n) {
      num_paths += n;
    }
    void inc_inst(size_t n) {
      num_insts += n;
    }
    uint64_t new_ssid() {
      return ++num_states;
    }
    void print_inst_stat() {
      std::cout << "#insts: " << num_insts << "; " << std::flush;
    }
    void print_path_cov() {
      std::cout << "#paths: " << num_paths << "; " << std::flush;
    }
    void print_block_cov() {
      size_t covered = 0;
      for (auto& v : block_cov) { if (v != 0) covered++; }
      std::cout << "#blocks: "
                << covered << "/"
                << num_blocks << "; "
                << std::flush;
    }
    void print_block_cov_detail() {
      size_t covered = 0;
      for (auto& v : block_cov) { if (v != 0) covered++; }
      std::cout << "Block coverage: " << covered << "/" << num_blocks << "\n";
      for (int i = 0; i < block_cov.size(); i++) {
        if (block_cov[i] == 0) continue;
        std::cout << "  Block " << i << ", "
                  << "visited " << block_cov[i] << "\n"
                  << std::flush;
      }
    }
    void print_branch_cov() {
      // number of branches that at least one outcome is covered
      size_t partial_branch = 0;
      // number of branches that all possible outcomes are covered
      size_t full_branch = 0;
      for (const auto& [blk_id, br_map] : branch_cov) {
        bool partial_cov = false;
        bool full_cov = true;
        for (const auto& [br_id, br_exe_num] : br_map) {
          partial_cov |= (br_exe_num > 0);
          full_cov &= (br_exe_num > 0);
        }
        if (partial_cov) partial_branch++;
        if (full_cov) full_branch++;
      }
      // We output the number of partial branches excluding fully covered branches
      std::cout << "#br: "
	        << (partial_branch - full_branch) << "/"
	        << full_branch << "/"
	        << branch_cov.size() << "; "
	        << std::flush;
    }
    void print_branch_cov_detail() {
      std::cout << "Branch coverage: \n";
      for (const auto& [blk_id, br_map] : branch_cov) {
        std::cout << "Block " << blk_id << "\n";
        for (const auto& [br_id, br_exe_num] : br_map) {
          std::cout << "  branch [" << br_id << "] visited " << br_exe_num << "\n";
        }
      }
      std:: cout << std::flush;
    }
    void print_thread_pool() {
      std::cout << "#threads: " << n_thread << "; #task-in-q: " << tp.tasks_num_queued() << "; " << std::flush;
    }
    void print_query_stat() {
      std::cout << "#queries: " << br_query_num << "/" << generated_test_num << " (" << cached_query_num << ")\n" << std::flush;
    }
    void print_time(bool done) {
      steady_clock::time_point now = done ? stop : steady_clock::now();
      if (print_detailed_time == 1 || (done && print_detailed_time == 2)) {
        std::cout << "Expr construction: " << (cons_expr_time / 1.0e6) << "s; "
                  << "Gen test: " << (gen_test_time / 1.0e6) << "s; "
                  << "Cons indep: " << (cons_indep_time / 1.0e6) << "s; "
                  << "Branch solver: " << (br_solver_time / 1.0e6) << "s; "
                  << "Conc. solver: " << (conc_solver_time / 1.0e6) << "s; "
                  << "add cons. time: " << (add_cons_time / 1.0e6) << "s; "
                  << "#Conc. query: " << conc_query_num << "\n";

        std::cout << "else-br: " << (else_miss_time / 1.0e6) << "s; "
                  << "then-br: " << (then_miss_time / 1.0e6) << "s; "
                  << "both-br: " << (both_miss_time / 1.0e6) << "s\n";

        std::cout << "complete path: " << completed_path_num << "\n";
      }
      std::cout << "[" << (ext_solver_time / 1.0e6) << "s/"
                << (int_solver_time / 1.0e6) << "s/"
                << (fs_time / 1.0e6) << "s/"
                << (duration_cast<microseconds>(now - start).count() / 1.0e6) << "s] ";
    }
    void print_all(bool done = false) {
      print_time(done);
      if (print_inst_cnt) print_inst_stat();
      print_block_cov();
      print_branch_cov();
      print_path_cov();
      print_thread_pool();
      print_query_stat();
      if (done && print_cov_detail) {
        print_block_cov_detail();
        print_branch_cov_detail();
      }
    }
    void start_monitor() {
      std::future<void> future = signal_exit.get_future();
      watcher = std::thread([this](std::future<void> fut) {
        while (fut.wait_for(milliseconds(1)) == std::future_status::timeout) {
          steady_clock::time_point now = steady_clock::now();
          if (duration_cast<seconds>(now - start) > seconds(timeout)) {
            std::cout << "Timeout, aborting.\n";
            stop = now;
            print_all(true);
            _exit(0);
            // Note: Directly exit may cause other threads in a random state.
            // When using the thread pool, we could use the following to wait
            // all other worker threads to finish:
            // tp.stop_all_tasks(); break;
          }
          print_all();
          std::this_thread::sleep_for(seconds(1));
        }
      }, std::move(future));
    }
    void stop_monitor() {
      stop = steady_clock::now();
      signal_exit.set_value();
      if (watcher.joinable()) {
        // XXX: this is still not idea, since for execution < 1s, we need to wait for watcher to join...
        watcher.join();
      }
      //ASSERT(num_paths == num_states, "In-consistent path state");
    }
};

/* Declare the function to get monitor; will be emited by the front-end in common.h */

inline Monitor& cov();

#endif
