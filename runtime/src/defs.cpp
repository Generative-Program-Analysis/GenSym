#include <gensym.hpp>

std::atomic<uint32_t> g_sym_id = 0;
int vararg_id = -1;

unsigned int default_bw = 32;
unsigned int addr_bw = 64;
unsigned int addr_index_bw = addr_bw;

bool use_thread_pool = false;
unsigned int n_thread = 1;
unsigned int n_queue = 1;

bool use_solver = true;
bool use_global_solver = false;
bool use_hashcons = true;
bool use_objcache = true;
bool use_cexcache = true;
bool use_brcache = true;
bool use_cons_indep = true;
bool only_output_covernew = false;
bool output_ktest = false;
bool readable_file_tests = false;

unsigned int timeout = 3600;

bool exlib_failure_branch = false;

bool print_inst_cnt = false;
bool print_cov_detail = false;
uint32_t print_detailed_log = 0;

unsigned int max_sym_array_size = 0;
unsigned int max_size_bound = 400;
bool use_symv_simplify = false;

bool stdout_log = true;
