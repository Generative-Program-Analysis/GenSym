[![Scala CI](https://github.com/Generative-Program-Analysis/GenSym/actions/workflows/scala.yml/badge.svg)](https://github.com/Generative-Program-Analysis/GenSym/actions/workflows/scala.yml)

# GenSym

GenSym is a high-performance, parallel symbolic execution engine for LLVM IR.

- GenSym's high performance is achieved by making use of advanced
compiler technologies that given an input LLVM IR program, it
generates optimized code to perform symbolic execution and test case generation.
In this sense, GenSym is a symbolic-execution compiler that generates code
following the symbolic semantics (instead of concrete semantics) of LLVM IR
and it does not induce interpretation overhead.

- GenSym views fork-based symbolic execution as a concurrency semantics, therefore
it generates code in continuation-passing style. 
In this way, the generated code that interleaves execution of different paths.

- GenSym's efficient parallelism is achieved on top of its continuation-based
code generation. Since continuations are first-class citizens, a global scheduler
can dispatch the different jobs (represented by continuations) to multiple OS-threads.

GenSym is still a research prototype, but it can already handle a large class
of real-world programs (e.g. programs from GNU Coreutils). 
See our [ICSE 2023 paper](https://continuation.passing.style/static/papers/icse23.pdf) 
for detailed evaluation.

### Usage

The easiest way to try GenSym is to use the Docker image we build for the [accompanying ICSE 23 artifact](https://github.com/Generative-Program-Analysis/icse23-artifact-evaluation), which has all dependencies installed.

To obtain the Docker image (you may need root privilege to run `docker`):

```
$ docker pull guannanwei/gensym:icse23
```

To instantiate a Docker image, run the following command (we need to use
`ulimit` to increase stack size to avoid stack overflow):

```
$ sudo docker run --name try-gensym --ulimit='stack=268435456:268435456' -it guannanwei/gensym:icse23 bash -c 'source /icse23/sync.sh; bash'
```

Then you should see the prompt of `bash`.

GenSym is a Scala project, therefore the simplest way to use GenSym is through
an interactive `sbt` session:

```
$ cd /icse23/GenSym
$ ./start_sbt
```

Then you should see the prompt of `sbt`:
```
sbt:GenSym> 
```

To see all command-line arguments supported by GenSym:

```
sbt:GenSym> runMain gensym.RunGenSym --help

Usage: gensym <ll-filepath> [--entrance=<string>] [--output=<string>] [--nSym=<int>]
              [--use-argv] [--noOpt] [--engine=<string>] [--main-O0]

<ll-filepath>           - the input LLVM IR program (.ll)
--entrance=<string>     - the entrance function name (default=main)
--output=<string>       - the folder name containing generated Makefile/C++ code (default=basename of the input .ll file)
--nSym=<int>            - the number of symbolic 32-bit input arguments to `entrance`, cannot be used with --use-argv (default=0)
--use-argv              - take argv argument at runtime, cannot be used with --nSym
--noOpt                 - disable first-stage optimizations
--engine=<string>       - compiler/backend variant (default=ImpCPS)
  =ImpCPS               -   generate code in CPS with impure data structures, can run in parallel
  =ImpDirect            -   generate code in direct-style with impure data structures, cannot run in parallel
  =PureCPS              -   generate code in CPS with pure data structures, can run in parallel
  =PureDirect           -   generate code in direct-style with pure data structures, cannot run in parallel
--main-opt=<string>     - g++ optimization level when compiling the main file containing the initial heap object
--emit-block-id-map     - emit a map from block names to id in common.h
--emit-var-id-map       - emit a map from variable names to id in common.h
--switch-type=<string>  - compilation variants of `switch` statement (default=nonMerge)
  =merge                -   only fork `m` paths of distinct targets
  =nonMerge             -   fork `n` paths where `n` is the total number of feasible cases (including default)
--help                  - print this help message
```

### Demonstration

We consider a simple branch program for demonstrating how GenSym works.
This simple program has 4 paths with different conditions imposed by the `if` 
statements.

```
int f(int x, int y) {
  if (x <= 0 || y <= 0) return -1;
  if (x * x + y * y == 25) return 1;
  return 0;
}
```

We already have the compiled LLVM IR program in the Docker image, located
at `/icse23/GenSym/benchmarks/llvm/branch.ll`

To compile this LLVM IR program with GenSym, we use following command.
We additionally need to specify the entrance function (`f`) and the 
number of symbolic inputs to function `f` (which is 2).

```
sbt:GenSym> runMain gensym.RunGenSym /icse23/GenSym/benchmarks/llvm/branch.ll --entrance=f --nSym=2
```

The generated C++ code is located at `/icse23/GenSym/gs_gen/branch`.
In file `__GS_USER_f.cpp`, you may also inspect the generated function for source function `f`.
Then we can further compile the C++ code to an executable using the accompanying Makefile:

```
# cd /icse23/GenSym/gs_gen/branch
# make -j
```

This steps generates the executable `branch`, then running the executable file prints the statistics, e.g. the number of discovered blocks/paths:

```
# ./branch
...
[0.04903s/0.049225s/0s/0.049329s] #blocks: 7/7; #br: 0/3/3; #paths: 4; #threads: 1; #task-in-q: 0; #queries: 6/4 (3)
```

The generated executable file also has several runtime options.
For most of users, it suffices to use the default options. However if you would like to
play with it, you can check those options by `./branch --help`.

The generated tests and an archived log file can be found under `gensym-DDMMYYYY-HHMMSS/tests` for further inspection.

### Publications

If you would like to mention GenSym in research papers, please cite this paper:

* Compiling Parallel Symbolic Execution with Continuation  
  Guannan Wei, Songlin Jia, Ruiqi Gao, Haotian Deng, Shangyin Tan, Oliver Bračevac, Tiark Rompf  
  The 45th International Conference on Software Engineering (ICSE 2023)  
  [PDF](https://continuation.passing.style/static/papers/icse23.pdf)

Other related publications:
  
* Towards Partially Evaluating Symbolic Interpreters for All  
  Shangyin Tan, Guannan Wei, Tiark Rompf  
  ACM SIGPLAN Workshop on Partial Evaluation and Program Manipulation (PEPM), co-located with POPL 2022. Philadelphia, PA, USA  
  [PDF](http://continuation.passing.style/static/papers/pepm22.pdf)

* LLSC: A Parallel Symbolic Execution Compiler for LLVM IR (Demo Paper)  
  Guannan Wei, Shangyin Tan, Oliver Bračevac, Tiark Rompf  
  Proceedings of The 29th ACM Joint European Software Engineering Conference and Symposium on the Foundations of Software Engineering (ESEC/FSE 2021)  
  [PDF (ACM DL)](https://dl.acm.org/doi/10.1145/3468264.3473108)

* Compiling Symbolic Execution with Staging and Algebraic Effect  
  Guannan Wei, Oliver Bračevac, Shangyin Tan, Tiark Rompf  
  Proceedings of the ACM on Programming Languages, Volume 4 (OOPSLA 2020). Online  
  [PDF (ACM DL)](https://dl.acm.org/doi/10.1145/3428232)

* Staged Abstract Interpreters: Fast and Modular Whole-Program Analysis via Meta-Programming  
  Guannan Wei, Yuxuan Chen, Tiark Rompf  
  Proceedings of the ACM on Programming Languages, Volume 3 (OOPSLA 2019). Athens, Greece  
  [PDF (ACM DL)](https://dl.acm.org/doi/10.1145/3360552)
