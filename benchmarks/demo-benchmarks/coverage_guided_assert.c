#include <stdbool.h>
#include <stddef.h>
#include <stdint.h>

void make_symbolic(void *addr, size_t byte_size, ...);
void gs_assert_eager(bool condition, ...);

#if defined(__clang__) || defined(__GNUC__)
#define NOINLINE __attribute__((noinline))
#else
#define NOINLINE
#endif

/*
 * This benchmark deliberately separates new coverage from path count.
 * warm_up_coverage() visits every block in decoy() and guarded_dispatch()
 * before symbolic execution begins.  Consequently, a state sent to decoy()
 * can create 2^24 paths but cannot discover a new block.  The all-ones route
 * through guarded_dispatch(), on the other hand, is the shortest route to the
 * one block which warm-up does not visit: the failing assertion.
 */
static NOINLINE uint32_t decoy(uint32_t bits) {
  volatile uint32_t value = 0x9e3779b9u;

#define DECOY_STEP(N)                                                        \
  do {                                                                       \
    if (bits & (1u << (N)))                                                  \
      value = (value << 5) + value + (uint32_t)(N) + 1u;                    \
    else                                                                     \
      value = (value ^ (0x45d9f3bu + (uint32_t)(N))) * 33u;                 \
  } while (0)

  DECOY_STEP(0);
  DECOY_STEP(1);
  DECOY_STEP(2);
  DECOY_STEP(3);
  DECOY_STEP(4);
  DECOY_STEP(5);
  DECOY_STEP(6);
  DECOY_STEP(7);
  DECOY_STEP(8);
  DECOY_STEP(9);
  DECOY_STEP(10);
  DECOY_STEP(11);
  DECOY_STEP(12);
  DECOY_STEP(13);
  DECOY_STEP(14);
  DECOY_STEP(15);
  DECOY_STEP(16);
  DECOY_STEP(17);
  DECOY_STEP(18);
  DECOY_STEP(19);
  DECOY_STEP(20);
  DECOY_STEP(21);
  DECOY_STEP(22);
  DECOY_STEP(23);

#undef DECOY_STEP
  return value;
}

static NOINLINE void hidden_bug(uint32_t key) {
  if (key == 0xc0def00du)
    gs_assert_eager(false, "coverage-guided benchmark reached the hidden bug");
}

static NOINLINE uint32_t guarded_dispatch(uint32_t selector,
                                          uint32_t payload,
                                          uint32_t key) {
  if (!(selector & 0x01u)) return decoy(payload);
  if (!(selector & 0x02u)) return decoy(payload);
  if (!(selector & 0x04u)) return decoy(payload);
  if (!(selector & 0x08u)) return decoy(payload);
  if (!(selector & 0x10u)) return decoy(payload);
  if (!(selector & 0x20u)) return decoy(payload);
  if (!(selector & 0x40u)) return decoy(payload);
  if (!(selector & 0x80u)) return decoy(payload);
  if (!(selector & 0x100u)) return decoy(payload);
  if (!(selector & 0x200u)) return decoy(payload);

  hidden_bug(key);
  return 0;
}

static NOINLINE void warm_up_coverage(void) {
  /* Visit both sides of every decoy branch without creating symbolic paths. */
  (void)decoy(0u);
  (void)decoy(UINT32_MAX);

  /* Visit every early-exit block in guarded_dispatch(). */
  (void)guarded_dispatch(0u, 0u, 0u);
  (void)guarded_dispatch(1u, 0u, 0u);
  (void)guarded_dispatch(3u, 0u, 0u);
  (void)guarded_dispatch(7u, 0u, 0u);
  (void)guarded_dispatch(15u, 0u, 0u);
  (void)guarded_dispatch(31u, 0u, 0u);
  (void)guarded_dispatch(63u, 0u, 0u);
  (void)guarded_dispatch(127u, 0u, 0u);
  (void)guarded_dispatch(255u, 0u, 0u);
  (void)guarded_dispatch(511u, 0u, 0u);

  /* Cover hidden_bug()'s safe path, leaving only its assertion block new. */
  (void)guarded_dispatch(1023u, 0u, 0u);
}

int main(void) {
  uint32_t selector;
  uint32_t payload;

  warm_up_coverage();
  make_symbolic(&selector, sizeof(selector), "selector");
  make_symbolic(&payload, sizeof(payload), "payload");

  (void)guarded_dispatch(selector, payload, 0xc0def00du);
  return 0;
}
