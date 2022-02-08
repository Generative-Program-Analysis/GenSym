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

struct Task {
  std::function<void()> f;
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

  std::mutex q_lock = {};
  std::queue<std::function<void()>> tasks = {};
  std::priority_queue<Task> ptasks = {};

  std::unique_ptr<std::thread[]> threads;
  std::unique_ptr<std::thread::id[]> thread_ids;

  size_t sleep_duration = 500;
  std::atomic<size_t> tasks_num_total = 0;
  bool inited = false;
public:
  size_t thread_num;

  thread_pool(const size_t thread_num) :
    thread_num(thread_num), threads(new std::thread[thread_num]), thread_ids(new std::thread::id[thread_num]) {
    init(thread_num);
  }
  thread_pool() : thread_num(0) {}
  ~thread_pool() {
    running = false;
    for (size_t i = 0; i < thread_num; i++) {
      threads[i].join();
    }
  }
  void init(const size_t n) {
    if (inited) ABORT("Thread pool is already initialized.");
    thread_num = n;
    threads.reset(new std::thread[thread_num]);
    thread_ids.reset(new std::thread::id[thread_num]);
    for (size_t i = 0; i < thread_num; i++) {
      std::cout << "create thread " << i << "\n";
      threads[i] = std::thread(&thread_pool::worker, this);
      thread_ids[i] = threads[i].get_id();
    }
    inited = true;
  }
  void with_thread_ids(const std::function<void(std::thread::id)>& f) {
    for (size_t i = 0; i < thread_num; i++) { f(thread_ids[i]); }
  }
  void add_task(const std::function<void()>& f) {
    add_task(f, rand_int(1024));
  }
  void add_task(const std::function<void()>& f, int w) {
    tasks_num_total++;
    {
    const std::scoped_lock lock(q_lock);
    //std::cout << "Adding task with weight " << w << "\n";
    ptasks.push({f, w});
    }
  }
  void worker() {
    pthread_setcancelstate(PTHREAD_CANCEL_ENABLE, NULL);
    pthread_setcanceltype(PTHREAD_CANCEL_ASYNCHRONOUS, NULL);
    while (running) {
      //std::cout << "Running tasks " << running_tasks_num()
      //          << "; queued tasks " << tasks_num_queued() << "\n";
      struct Task task;
      if (!paused && pop_task(task)) {
        //std::cout << "thread " << std::this_thread::get_id() << " is running; " << running_tasks_num() << "\n";
        task.f();
        //std::cout << "thread " << std::this_thread::get_id() << " finished\n";
        tasks_num_total--;
      } else {
        sleep_or_yield();
      }
    }
  }
  bool pop_task(struct Task& task) {
    const std::scoped_lock lock(q_lock);
    if (ptasks.empty()) return false;
    task = std::move(ptasks.top());
    ptasks.pop();
    return true;
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
    const std::scoped_lock lock(q_lock);
    return ptasks.size();
  }
  void sleep_or_yield() {
    if (sleep_duration) std::this_thread::sleep_for(std::chrono::milliseconds(sleep_duration));
    else std::this_thread::yield();
  }
};

#endif
