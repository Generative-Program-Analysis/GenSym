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

// the maximal number of additional parallel thread (excluding main thread)
inline size_t max_par_num = 0;

class thread_pool {
private:
  std::atomic<bool> running = true;
  std::atomic<bool> paused = false;

  std::mutex q_lock = {};
  std::queue<std::function<void()>> tasks = {};

  size_t thread_num;
  std::unique_ptr<std::thread[]> threads;
  std::unique_ptr<std::thread::id[]> thread_ids;

  size_t sleep_duration = 1000;
  std::atomic<size_t> tasks_num_total = 0;
  bool inited = false;
public:
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
    tasks_num_total++;
    {
    const std::scoped_lock lock(q_lock);
    tasks.push(f);
    }
  }
  void worker() {
    while (running) {
      //std::cout << "worker running; running tasks " << running_tasks_num()
      //          << "; queued tasks " << tasks_num_queued() << "\n";
      std::function<void()> task;
      if (!paused && pop_task(task)) {
        //std::cout << "thread " << std::this_thread::get_id() << " is running; " << running_tasks_num() << "\n";
        task();
        tasks_num_total--;
      } else {
        sleep_or_yield();
      }
    }
  }
  bool pop_task(std::function<void()>& task) {
    const std::scoped_lock lock(q_lock);
    if (tasks.empty()) return false;
    task = std::move(tasks.front());
    tasks.pop();
    return true;
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
    return tasks.size();
  }
  void sleep_or_yield() {
    if (sleep_duration) std::this_thread::sleep_for(std::chrono::microseconds(sleep_duration));
    else std::this_thread::yield();
  }
};

#endif
