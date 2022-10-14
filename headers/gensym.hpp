#ifndef GS_HEADERS
#define GS_HEADERS

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
#include <signal.h>

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

#include <parallel_hashmap/phmap.h>

#include <gensym/immeralgo.hpp>
#include <gensym/auxiliary.hpp>
#include <gensym/defs.hpp>
#include <gensym/parallel.hpp>
#include <gensym/monitor.hpp>
#include <gensym/ptree.hpp>
#include <gensym/value_ops.hpp>
#include <gensym/filesys.hpp>
#include <gensym/args.hpp>
#include <gensym/cli.hpp>

#ifdef PURE_STATE
#include <gensym/state_pure.hpp>
#endif
#ifdef IMPURE_STATE
#include <gensym/state_tsnt.hpp>
#endif

#include <gensym/smt_checker.hpp>
#include <gensym/branch.hpp>
#include <gensym/misc.hpp>

#ifdef PURE_STATE
#include <gensym/external_pure.hpp>
#endif

#ifdef IMPURE_STATE
#include <gensym/external_imp.hpp>
#endif

#include <gensym/external.hpp>

#endif
