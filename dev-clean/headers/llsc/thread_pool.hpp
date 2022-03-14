#ifndef LLSC_THREAD_POOL_HEADER
#define LLSC_THREAD_POOL_HEADER

// Code adapted and changed from https://github.com/bshoshany/thread-pool/blob/master/thread_pool.hpp

#include <atomic>
#include <chrono>
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

#ifdef USE_LKFREE_Q
/* Pros: Performance is good for long-running + large number of threads.
 * Cons:
 *   - number-in-queue is an apprimation, leading to a few seconds latency after execution.
 *   - just FIFO queue, no priority.
 */
#include "concurrentqueue/blockingconcurrentqueue.h"
#endif

using TaskFun = std::function<std::monostate()>;

struct Task {
  TaskFun f;
  int weight;
};

template<>
struct std::less<Task> {
  constexpr bool operator()(const Task& lhs, const Task& rhs) const {
    return lhs.weight < rhs.weight;
  }
};

class thread_pool {
private:
  std::atomic<bool> running = true;
  std::atomic<bool> paused = false;

#ifdef USE_LKFREE_Q
  moodycamel::ConcurrentQueue<Task> Q;
#else
  std::vector<std::mutex> qlocks;
  std::vector<std::priority_queue<Task>> ptasks;
#endif

  std::unique_ptr<std::thread[]> threads;
  std::unique_ptr<std::thread::id[]> thread_ids;

  size_t sleep_duration = 500;
  std::atomic<size_t> tasks_num_total = 0;
  bool inited = false;

public:
  size_t thread_num;
  size_t queue_num;

  thread_pool() : thread_num(0) {}
  ~thread_pool() {
    running = false;
    for (size_t i = 0; i < thread_num; i++) {
      threads[i].join();
    }
  }

  void init(const size_t n_thread, const size_t n_queue) {
    if (inited) ABORT("Thread pool is already initialized.");
    thread_num = n_thread;
    queue_num = n_queue;

#ifdef USE_LKFREE_Q
#else
    qlocks = std::vector<std::mutex>(n_queue);
    ptasks = std::vector<std::priority_queue<Task>>(n_queue);
#endif

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
  void add_task(const TaskFun& f) { add_task(f, rand_int(1024)); }
  void add_task(const TaskFun& f, int w) {
    tasks_num_total++;
    INFO("Adding task into queue with weight " << w);
#ifdef USE_LKFREE_Q
    Q.enqueue({f, w});
#else
    unsigned id = rand_int(queue_num)-1;
    {
      const std::scoped_lock lock(qlocks.at(id));
      ptasks[id].push({f, w});
    }
#endif
  }

  void worker(unsigned id) {
    while (running) {
      //std::cout << "Running tasks " << running_tasks_num()
      //          << "; queued tasks " << tasks_num_queued() << "\n";
      struct Task task;
      bool get = false;
      for (size_t i = id; i < id+queue_num; i++) {
        if (pop_task(i % queue_num, task)) { get = true; break; }
      }
      if (!paused && get) {
        //std::cout << "thread " << std::this_thread::get_id() << " is running; " << running_tasks_num() << "\n";
        task.f();
        //std::cout << "thread " << std::this_thread::get_id() << " finished\n";
        tasks_num_total--;
      } else {
        sleep_or_yield();
      }
    }
  }

  bool pop_task(unsigned id, struct Task& task) {
#ifdef USE_LKFREE_Q
    bool found = Q.try_dequeue(task);
    return found;
#else
    const std::scoped_lock lock(qlocks.at(id));
    if (ptasks[id].empty()) return false;
    task = std::move(ptasks[id].top());
    ptasks[id].pop();
    return true;
#endif
  }

  void stop_all_tasks() {
    running = false;
    paused = true;
  }

  void wait_for_tasks() {
    while (true) {
      if (!paused) {
        if (tasks_num_total == 0) break;
      } else {
        if (running_tasks_num() == 0) break;
      }
      sleep_or_yield();
    }
  }

  size_t running_tasks_num() {
    return tasks_num_total - tasks_num_queued();
  }

  size_t tasks_num_queued() {
#ifdef USE_LKFREE_Q
    return Q.size_approx();
#else
    // FIXME: check balance?
    size_t sum = 0;
    for (int i = 0; i < ptasks.size(); i++) {
      sum += ptasks[i].size();
    }
    return sum;
#endif
  }

  void sleep_or_yield() {
    if (sleep_duration) std::this_thread::sleep_for(std::chrono::milliseconds(sleep_duration));
    else std::this_thread::yield();
  }
};

#endif
