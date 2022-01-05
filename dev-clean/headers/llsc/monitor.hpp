#ifndef LLSC_MON_HEADERS
#define LLSC_MON_HEADERS

/* Coverage information */

// TODO: branch coverage
// Some note on overhead: recording coverage 1m path/block exec poses ~2.5sec overhead.
struct Monitor {
  private:
    using BlockId = std::int64_t;
    // Total number of blocks
    std::uint64_t num_blocks;
    // The number of execution for each block
    std::vector<std::uint64_t> block_cov;
    // Number of discovered paths
    std::uint64_t num_paths;
    // Starting time
    steady_clock::time_point start;
    std::mutex bm;
    std::mutex pm;
    std::thread watcher;
    std::promise<void> signal_exit;
  public:
    Monitor() : num_blocks(0), num_paths(0), start(steady_clock::now()) {}
    Monitor(std::uint64_t num_blocks) : num_blocks(num_blocks), num_paths(0), start(steady_clock::now()) {}
    void set_num_blocks(std::uint64_t n) {
      num_blocks = n;
      block_cov.resize(n, 0);
    }
    void inc_block(BlockId b) {
      std::unique_lock<std::mutex> lk(bm);
      block_cov[b]++;
    }
    void inc_path(size_t n) {
      std::unique_lock<std::mutex> lk(pm);
      num_paths += n;
    }
    void print_path_cov() {
      std::cout << "#paths: " << num_paths << "; " << std::flush;
    }
    void print_block_cov() {
      size_t covered = 0;
      for (auto v : block_cov) { if (v != 0) covered++; }
      std::cout << "#blocks: "
                << covered << "/"
                << num_blocks << "; "
                << std::flush;
    }
    void print_block_cov_detail() {
      print_block_cov();
      for (int i = 0; i < block_cov.size(); i++) {
        std::cout << "Block: " << i << "; "
                  << "visited: " << block_cov[i] << "\n"
                  << std::flush;
      }
    }
    void print_async() {
      std::cout << "#threads: " << num_async + 1 << "; #async created: " << tt_num_async << "; " << std::flush;
      //std::cout << "current #async: " << pool.tasks_size() << " total #async: " << tt_num_async << "\n";
    }
    void print_query_stat() {
      std::cout << "#queries: " << br_query_num << "/" << test_query_num << " (" << cached_query_num << ")\n" << std::flush;
    }
    void print_time() {
      steady_clock::time_point now = steady_clock::now();
      std::cout << "[" << (solver_time.count() / 1.0e6) << "s/"
                << (duration_cast<microseconds>(now - start).count() / 1.0e6) << "s] ";
    }
    void start_monitor() {
      std::future<void> future = signal_exit.get_future();
      watcher = std::thread([this](std::future<void> fut){
        while (this->block_cov.size() <= this->num_blocks &&
               fut.wait_for(milliseconds(1)) == std::future_status::timeout) {
          print_time();
          print_block_cov();
          print_path_cov();
          print_async();
          print_query_stat();
          std::this_thread::sleep_for(seconds(1));
        }
      }, std::move(future));
    }
    void stop_monitor() {
      signal_exit.set_value();
      if (watcher.joinable()) {
        watcher.join();
      }
    }
};

inline Monitor cov;

#endif
