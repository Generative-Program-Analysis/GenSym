#include "../gensym.hpp"

#include <cassert>
#include <thread>
#include <vector>

inline Monitor& cov() {
  static Monitor monitor;
  return monitor;
}

int main() {
  Monitor monitor(4, {}, {{1}, {2}, {}, {}});

  assert(monitor.inc_block(0));
  assert(!monitor.inc_block(0));
  assert(monitor.inc_block(1));

  auto distance_two = monitor.coverage_priority(0, 0);
  auto distance_one = monitor.coverage_priority(1, 0);
  auto uncovered = monitor.coverage_priority(2, 0);
  assert(uncovered > distance_one);
  assert(distance_one > distance_two);
  assert(monitor.coverage_priority(unknown_block_id, 17) == 17);
  assert(monitor.coverage_priority(3, 23) > 23); // block 3 itself is uncovered

  Task frozen_task{[] { return std::monostate{}; }, distance_one};
  assert(monitor.inc_block(2));
  assert(monitor.coverage_priority(1, 31) == 31); // no reachable uncovered block
  assert(frozen_task.weight == distance_one);     // an enqueued Task keeps this value

  std::priority_queue<Task> queue;
  queue.push(Task{[] { return std::monostate{}; }, distance_two});
  queue.push(Task{[] { return std::monostate{}; }, uncovered});
  queue.push(Task{[] { return std::monostate{}; }, distance_one});
  assert(queue.top().weight == uncovered);

  Monitor concurrent(1, {}, {{}});
  std::atomic<int> first_visits{0};
  std::vector<std::thread> workers;
  for (int i = 0; i < 8; ++i) {
    workers.emplace_back([&] { if (concurrent.inc_block(0)) ++first_visits; });
  }
  for (auto& worker : workers) worker.join();
  assert(first_visits == 1);
}
