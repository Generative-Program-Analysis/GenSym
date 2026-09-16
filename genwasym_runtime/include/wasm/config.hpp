#ifndef CONFIG_HPP
#define CONFIG_HPP

// This file contains configuration settings for the concolic execution

// If ENABLE_PROFILE_STEP defined, the compiled program will collect and print
// profiling how much steps of each data structure's operations are executed
#ifdef ENABLE_PROFILE_STEP
const bool PROFILE_STEP = true;
#else
const bool PROFILE_STEP = false;
#endif

// If ENABLE_PROFILE_TIME defined, the compiled program will collect and print
// the profile of time spent in main loop and constraint solving
#ifdef ENABLE_PROFILE_TIME
const bool PROFILE_TIME = true;
#else
const bool PROFILE_TIME = false;
#endif

#ifdef ENABLE_PROFILE_Z3_API_CALL
const bool PROFILE_Z3_API_CALL = true;
#else
const bool PROFILE_Z3_API_CALL = false;
#endif

#ifdef ENABLE_PROFILE_CACHE
const bool PROFILE_CACHE = true;
#else
const bool PROFILE_CACHE = false;
#endif

#ifdef ENABLE_PROFILE_PATH_CONDS
const bool PROFILE_PATH_CONDS = true;
#else
const bool PROFILE_PATH_CONDS = false;
#endif

#ifdef ENABLE_PROFILE_SNAPSHOT
const bool PROFILE_SNAPSHOT = true;
#else
const bool PROFILE_SNAPSHOT = false;
#endif

// This variable define when concolic execution will stop
enum class ExploreMode {
  EarlyExit, // Stop at the first error encountered

  ExitByCoverage // Exit when all syntactic branches are covered
};

#ifdef EARLY_EXIT
static const ExploreMode EXPLORE_MODE = ExploreMode::EarlyExit;
#elif defined(BY_COVERAGE)
static const ExploreMode EXPLORE_MODE = ExploreMode::ExitByCoverage;
#else
static const ExploreMode EXPLORE_MODE = ExploreMode::EarlyExit;
#endif

// This variable decides whether we enable the snapshot reuse optimization
#ifdef NO_REUSE
static const bool REUSE_SNAPSHOT = false;
#else
static const bool REUSE_SNAPSHOT = true;
#endif

// If we use immutable data structures for symbolic states to reduce the cost of
// copying.
#ifdef USE_IMM
static const bool IMMUTABLE_SYMS = true;
#else
static const bool IMMUTABLE_SYMS = false;
#endif

#ifdef INTERACTIVE
static const bool INTERACTIVE_MODE = true;
#else
static const bool INTERACTIVE_MODE = false;
#endif

#ifdef USE_COST_MODEL
static const bool ENABLE_COST_MODEL = true;
#else
static const bool ENABLE_COST_MODEL = false;
#endif

#ifdef USE_SOFT_ASSERT
static const bool SOFT_ASSERT = true;
#else
static const bool SOFT_ASSERT = false;
#endif

#endif // CONFIG_HPP
