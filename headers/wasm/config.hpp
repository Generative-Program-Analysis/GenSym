#ifndef CONFIG_HPP
#define CONFIG_HPP

// This file contains configuration settings for the concolic execution

// If ENABLE_PROFILE defined, the compiled program will collect and print
// profiling information
#ifdef ENABLE_PROFILE
const bool PROFILE_ENABLED = true;
#else
const bool PROFILE_ENABLED = false;
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

#endif // CONFIG_HPP