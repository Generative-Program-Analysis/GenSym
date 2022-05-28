#ifndef LLSC_HEADERS
#define LLSC_HEADERS

#include <ostream>
#include <fstream>
#include <variant>
#include <string>
#include <regex>
#include <typeinfo>
#include <vector>
#include <iostream>
#include <map>
#include <cstdint>
#include <chrono>

#include <thread>
#include <memory>
#include <mutex>
#include <condition_variable>
#include <future>

#include <unistd.h>
#include <fcntl.h>
#include <bits/stdc++.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <sys/resource.h>
#include <sys/syscall.h>
#include <cstdio>
#include <cstdlib>
#include <getopt.h>

#include <sai.hpp>
#include <immer/flex_vector_transient.hpp>

using namespace std::chrono;

#include <llsc/auxiliary.hpp>
#include <llsc/parallel.hpp>
#include <llsc/monitor.hpp>
#include <llsc/value_ops.hpp>
#include <llsc/filesys.hpp>
#include <llsc/args.hpp>
#include <llsc/cli.hpp>

#ifdef PURE_STATE
#include <llsc/state_pure.hpp>
#endif
#ifdef IMPURE_STATE
#include <llsc/state_imp.hpp>
#endif

#include <llsc/smt_checker.hpp>
#include <llsc/branch.hpp>
#include <llsc/misc.hpp>

#ifdef PURE_STATE
#include <llsc/external_pure.hpp>
#endif

#ifdef IMPURE_STATE
#include <llsc/external_imp.hpp>
#endif

#include <llsc/external.hpp>

#endif
