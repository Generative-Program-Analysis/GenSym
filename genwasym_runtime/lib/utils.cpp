#include "wasm/utils.hpp"

std::unordered_map<int32_t, GensymHeapRecord> GENSYM_HEAP_RECORDS;

bool GENSYM_IS_IN_ALLOCATED_RANGE(int32_t addr, size_t width) {
  const int64_t start = addr;
  const int64_t end = start + static_cast<int64_t>(width);
  for (const auto &[base, record] : GENSYM_HEAP_RECORDS) {
    if (record.status != GensymHeapStatus::Allocated) {
      continue;
    }
    const int64_t alloc_start = base;
    const int64_t alloc_end = alloc_start + static_cast<int64_t>(record.size);
    if (alloc_start <= start && end <= alloc_end) {
      return true;
    }
  }
  return false;
}

bool GENSYM_SHOULD_CHECK_ALLOCATION(int32_t addr) {
  if (GENSYM_HEAP_RECORDS.empty()) {
    return false;
  }
  int32_t heap_base = std::numeric_limits<int32_t>::max();
  for (const auto &[base, _] : GENSYM_HEAP_RECORDS) {
    heap_base = std::min(heap_base, base);
  }
  return addr >= heap_base;
}

void GENSYM_ASSERT_ADDR_ALLOCATED(int32_t addr, size_t width) {
  if (!GENSYM_SHOULD_CHECK_ALLOCATION(addr)) {
    return;
  }
  GENSYM_ASSERT(GENSYM_IS_IN_ALLOCATED_RANGE(addr, width));
}

int32_t GENSYM_ALLOC(int32_t base, int32_t size) {
  // std::cout << "Allocating memory at address " << base << " with size " << size
  //           << std::endl;
  GENSYM_ASSERT(base >= 0);
  GENSYM_ASSERT(size >= 0);
  GENSYM_HEAP_RECORDS[base] =
      GensymHeapRecord{size, GensymHeapStatus::Allocated};
  return base;
}

std::monostate GENSYM_FREE(int32_t ptr) {
  // std::cout << "Freeing memory at address " << ptr << std::endl;
  GENSYM_ASSERT(ptr >= 0);
  auto it = GENSYM_HEAP_RECORDS.find(ptr);
  GENSYM_ASSERT(it != GENSYM_HEAP_RECORDS.end());
  it->second.status = GensymHeapStatus::Freed;
  return std::monostate{};
}
