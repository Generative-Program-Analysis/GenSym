#ifndef CONFIG_HPP
#define CONFIG_HPP

// These settings have one definition in the runtime library. Configure them
// before execution; changing them during exploration is not supported.
enum class ExploreMode {
  EarlyExit,      // Stop at the first error encountered.
  ExitByCoverage // Exit when all syntactic branches are covered.
};

extern ExploreMode EXPLORE_MODE;
extern bool PROFILE_STEP;
extern bool PROFILE_TIME;
extern bool PROFILE_Z3_API_CALL;
extern bool PROFILE_CACHE;
extern bool PROFILE_PATH_CONDS;
extern bool PROFILE_SNAPSHOT;
extern bool REUSE_SNAPSHOT;
extern bool INTERACTIVE_MODE;
extern bool ENABLE_COST_MODEL;
extern bool SOFT_ASSERT;
extern bool RUN_ONCE_MODE;
extern bool INFO_ENABLED;
extern bool DBG_ENABLED;
extern bool DEBUG_ENABLED;
extern bool DEBUG_OP_ENABLED;
extern bool DEBUG_WHEN_ENABLED;

static inline void configure_runtime_options() {
#ifdef EARLY_EXIT
  EXPLORE_MODE = ExploreMode::EarlyExit;
#elif defined(BY_COVERAGE)
  EXPLORE_MODE = ExploreMode::ExitByCoverage;
#else
  EXPLORE_MODE = ExploreMode::EarlyExit;
#endif

#ifdef ENABLE_PROFILE_STEP
  PROFILE_STEP = true;
#else
  PROFILE_STEP = false;
#endif

#ifdef ENABLE_PROFILE_TIME
  PROFILE_TIME = true;
#else
  PROFILE_TIME = false;
#endif

#ifdef ENABLE_PROFILE_Z3_API_CALL
  PROFILE_Z3_API_CALL = true;
#else
  PROFILE_Z3_API_CALL = false;
#endif

#ifdef ENABLE_PROFILE_CACHE
  PROFILE_CACHE = true;
#else
  PROFILE_CACHE = false;
#endif

#ifdef ENABLE_PROFILE_PATH_CONDS
  PROFILE_PATH_CONDS = true;
#else
  PROFILE_PATH_CONDS = false;
#endif

#ifdef ENABLE_PROFILE_SNAPSHOT
  PROFILE_SNAPSHOT = true;
#else
  PROFILE_SNAPSHOT = false;
#endif

#ifdef NO_REUSE
  REUSE_SNAPSHOT = false;
#else
  REUSE_SNAPSHOT = true;
#endif

#ifdef INTERACTIVE
  INTERACTIVE_MODE = true;
#else
  INTERACTIVE_MODE = false;
#endif

#ifdef USE_COST_MODEL
  ENABLE_COST_MODEL = true;
#else
  ENABLE_COST_MODEL = false;
#endif

#ifdef USE_SOFT_ASSERT
  SOFT_ASSERT = true;
#else
  SOFT_ASSERT = false;
#endif

#ifdef RUN_ONCE
  RUN_ONCE_MODE = true;
#else
  RUN_ONCE_MODE = false;
#endif

#ifdef NO_INFO
  INFO_ENABLED = false;
#else
  INFO_ENABLED = true;
#endif

#ifdef NO_DBG
  DBG_ENABLED = false;
#else
  DBG_ENABLED = true;
#endif

#ifdef DEBUG
  DEBUG_ENABLED = true;
#else
  DEBUG_ENABLED = false;
#endif

#ifdef DEBUG_OP
  DEBUG_OP_ENABLED = true;
#else
  DEBUG_OP_ENABLED = false;
#endif

#ifdef DEBUGWHEN
  DEBUG_WHEN_ENABLED = true;
#else
  DEBUG_WHEN_ENABLED = false;
#endif
}

#endif // CONFIG_HPP
