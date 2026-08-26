#ifndef GS_THREAD_POOL_HEADER
#define GS_THREAD_POOL_HEADER

#include <atomic>
#include <chrono>
#include <condition_variable>
#include <cstdint>
#include <functional>
#include <future>
#include <iostream>
#include <memory>
#include <mutex>
#include <queue>
#include <thread>
#include <type_traits>
#include <utility>

/* #include "concurrentqueue/blockingconcurrentqueue.h"
 * Pros: Performance is good for long-running + large number of threads.
 * Cons:
 *   - number-in-queue is an apprimation, leading to a few seconds latency after execution.
 *   - just FIFO queue, no priority.
 */

using TaskFun = std::function<std::monostate()>;

// Forward declarations

inline void ptree_add_task(uint64_t ssid, const TaskFun& f);
inline bool ptree_pop_task(TaskFun& task);
inline void check_pc_to_file(const SS& state);
inline uint64_t coverage_guided_priority(BlockLabel block);

// The Task structure stored in task pool

struct Task {
  TaskFun f;
  uint64_t weight;
};

template<>
struct std::less<Task> {
  constexpr bool operator()(const Task& lhs, const Task& rhs) const {
    return lhs.weight < rhs.weight;
  }
};

// The thread pool class

class thread_pool {
private:
  bool running = true;
  bool paused = false;

  // Protects task publication and the scheduler state below.  A worker first
  // reserves a published task under this lock, then removes a task from one of
  // the independently locked queues.
  mutable std::mutex scheduler_lock;
  std::condition_variable task_available;
  std::condition_variable tasks_finished;
  size_t queued_tasks = 0;
  size_t tasks_num_total = 0;

  std::vector<std::mutex> qlocks;
  std::vector<std::priority_queue<Task>> ptasks;

  std::unique_ptr<std::thread[]> threads;
  std::unique_ptr<std::thread::id[]> thread_ids;

  bool inited = false;

public:
  size_t thread_num;
  size_t queue_num;

  thread_pool() : thread_num(0) {}
  ~thread_pool() {
    {
      const std::scoped_lock lock(scheduler_lock);
      running = false;
    }
    task_available.notify_all();
    for (size_t i = 0; i < thread_num; i++) {
      threads[i].join();
    }
  }

  void init(const size_t n_thread, const size_t n_queue) {
    if (inited) ABORT("Thread pool is already initialized.");
    thread_num = n_thread;
    queue_num = n_queue;

    qlocks = std::vector<std::mutex>(n_queue);
    ptasks = std::vector<std::priority_queue<Task>>(n_queue);

    threads.reset(new std::thread[thread_num]);
    thread_ids.reset(new std::thread::id[thread_num]);
    for (size_t i = 0; i < thread_num; i++) {
      INFO("Create thread " << i);
      threads[i] = std::thread(&thread_pool::worker, this, i);
      thread_ids[i] = threads[i].get_id();
    }

    inited = true;
  }

  void with_thread_ids(const std::function<void(std::thread::id)>& f) {
    for (size_t i = 0; i < thread_num; i++) { f(thread_ids[i]); }
  }

  void queue_add_task(const TaskFun& f, uint64_t w) {
    INFO("Adding task into queue with weight " << w);
    unsigned id = rand_int(queue_num)-1;
    {
      const std::scoped_lock lock(qlocks.at(id));
      ptasks[id].push({f, w});
    }
  }

  void add_task(uint64_t ssid, BlockLabel block, const TaskFun& f) {
    {
      const std::scoped_lock lock(scheduler_lock);
      if (SearcherKind::randomPath == searcher_kind) {
        ptree_add_task(ssid, f);
      } else {
        ASSERT(SearcherKind::randomWeight == searcher_kind ||
               SearcherKind::coverageGuided == searcher_kind, "unknown searcher");
        auto weight = SearcherKind::coverageGuided == searcher_kind
          ? coverage_guided_priority(block)
          : static_cast<uint64_t>(rand_int(1024));
        queue_add_task(f, weight);
      }
      ++queued_tasks;
      ++tasks_num_total;
    }
    task_available.notify_one();
  }

  void worker(unsigned id) {
    while (true) {
      struct Task task;
      {
        std::unique_lock lock(scheduler_lock);
        task_available.wait(lock, [this] {
          return !running || (!paused && queued_tasks != 0);
        });
        if (!running) return;

        --queued_tasks;
      }

      // Concurrent reservations and insertions can move the task that backs a
      // reservation to a queue already inspected in this pass.  Retry until a
      // reserved task is found; unlike the old idle loop, this path is entered
      // only when published work is known to exist.
      bool get = false;
      while (!get) {
        if (SearcherKind::randomPath == searcher_kind) {
          get = ptree_pop_task(task.f);
        } else {
          ASSERT(SearcherKind::randomWeight == searcher_kind ||
                 SearcherKind::coverageGuided == searcher_kind, "unknown searcher");
          for (size_t i = id; i < id+queue_num; i++) {
            if (queue_pop_task(i % queue_num, task)) { get = true; break; }
          }
        }
        if (!get) std::this_thread::yield();
      }

      //std::cout << "thread " << std::this_thread::get_id() << " is running; " << running_tasks_num() << "\n";
      try {
        task.f();
      } catch (NullDerefException e) {
        std::cout << "Warning: read/write at a null location; generating a test case\n";
        check_pc_to_file(e.ss.get());
      }
      //std::cout << "thread " << std::this_thread::get_id() << " finished\n";
      {
        const std::scoped_lock lock(scheduler_lock);
        --tasks_num_total;
        if (tasks_num_total == 0 || (paused && tasks_num_total == queued_tasks)) {
          tasks_finished.notify_all();
        }
      }
    }
  }

  bool queue_pop_task(unsigned id, struct Task& task) {
    const std::scoped_lock lock(qlocks.at(id));
    if (ptasks[id].empty()) return false;
    task = std::move(ptasks[id].top());
    ptasks[id].pop();
    return true;
  }

  void stop_all_tasks() {
    {
      const std::scoped_lock lock(scheduler_lock);
      running = false;
      paused = true;
    }
    task_available.notify_all();
    tasks_finished.notify_all();
  }

  void wait_for_tasks() {
    std::unique_lock lock(scheduler_lock);
    tasks_finished.wait(lock, [this] {
      return paused ? tasks_num_total == queued_tasks : tasks_num_total == 0;
    });
  }

  size_t running_tasks_num() {
    const std::scoped_lock lock(scheduler_lock);
    return tasks_num_total - queued_tasks;
  }

  size_t tasks_num_queued() {
    const std::scoped_lock lock(scheduler_lock);
    return queued_tasks;
  }
};

#endif
