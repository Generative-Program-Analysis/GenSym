#ifndef LLSC_HEADERS
#define LLSC_HEADERS

#include <ostream>
#include <fstream>
#include <variant>
#include <string>
#include <vector>
#include <iostream>
#include <map>
#include <cstdint>
#include <thread>
#include <mutex>
#include <chrono>

#include <memory>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <future>

#include <unistd.h>
#include <fcntl.h>
#include <bits/stdc++.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <sys/resource.h>
#include <cstdio>
#include <cstdlib>
#include <getopt.h>

#include <sai.hpp>
#include <immer/flex_vector_transient.hpp>

using namespace std::chrono;

#define IMPURE_STATE

#include <llsc/auxiliary.hpp>
#include <llsc/cli.hpp>
#include <llsc/monitor.hpp>
#include <llsc/value_ops.hpp>
#include <llsc/state_imp.hpp>
#include <llsc/parallel.hpp>
#include <llsc/smt_stp.hpp>
#include <llsc/branch.hpp>

#include <llsc/external_imp.hpp>
#include <llsc/intrinsics_imp.hpp>

#endif
