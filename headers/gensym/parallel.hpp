#ifndef GS_PAR_HEADER
#define GS_PAR_HEADER

/* Thread pool */

#include "thread_pool.hpp"
inline thread_pool tp;

/* Auxiliary paralle functions */

inline bool can_par_tp() {
  return use_thread_pool;
}

/* Async (Deprecated) */

inline std::mutex m;

inline bool can_par_async() {
  return num_async < n_thread-1;
}

template <typename F, typename... Ts>
inline auto really_async(F&& f, Ts&&... params) {
  return std::async(std::launch::async, std::forward<F>(f), std::forward<Ts>(params)...);
}

template<class T>
auto create_async(std::function<T()> f) -> std::future<T> {
  std::unique_lock<std::mutex> lk(m);
  num_async++;
  tt_num_async++;
  lk.unlock();

  std::future<T> fu = std::async(std::launch::async, [&]{
    T t = f();
    std::unique_lock<std::mutex> lk(m);
    num_async--;
    lk.unlock();
    return t;
  });
  return fu;
}

#endif
