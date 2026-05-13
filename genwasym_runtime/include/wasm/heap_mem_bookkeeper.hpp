#ifndef HEAP_MEM_BOOKKEEPER_HPP
#define HEAP_MEM_BOOKKEEPER_HPP

#include <memory>
#include <set>

// Todo: remove this later, this is just a workaround to make sure that the
// SymVals' memory will not be freed during the main execution.
// We can leave the SymVal's memory unmanaged if reference counting is not
// performant
template <typename T> struct MemBookKeeper {
  std::set<std::shared_ptr<T>> allocated;

  template <typename R, typename... Args>
  std::shared_ptr<R> allocate(Args &&...args) {
    auto ptr = std::make_shared<R>(std::forward<Args>(args)...);
    // allocated.insert(ptr);
    return ptr;
  }

  void clear() { allocated.clear(); }
};

#endif // HEAP_MEM_BOOKKEEPER_HPP