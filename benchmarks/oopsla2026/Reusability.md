# GenWasym Usage and Reusability Guide

## What GenWasym Is

GenWasym is a **compiler for WebAssembly concolic execution**. It takes a
WebAssembly text-format module (`.wat`) as input, and generates a C++ program
that implements a concolic execution as output.

The complete compilation pipeline is:

```text
WebAssembly text (.wat)
        |
        | GenWasym parser
        v
WebAssembly AST
        |
        | Staging: specialize the CPS concolic evaluator to the AST
        v
Generated C++ (.wat.cpp)
        |
        | clang++: compile and link the GenWasym runtime, Z3, and Immer
        v
Standalone concolic executable (.exe)
```

GenWasym first parses the `.wat` file into its WebAssembly AST and constructs a
module instance. It then stages the CPS concolic evaluator with respect to that
module and emits a specialized C++ concolic executor. Finally, `clang++`
compiles and links the generated C++ with the GenWasym runtime, Z3, and Immer.

## How to Build GenWasym CLI from Source

If you are using version v8 or later of the Zenodo artifact, GenWasym is already
built and available on `PATH`, so you can skip this section.

The source package, `genwasym-source.tar.gz`, is available from the
[artifact page](https://doi.org/10.5281/zenodo.21517229). Build it in a Linux
environment with Java 8 and `sbt`. The Docker container included with
every version of the artifact provides this environment.

Assume that the artifact container has been started and is named
`genwasym-artifact-evaluation`:

```bash
sudo docker cp genwasym-source.tar.gz \
  genwasym-artifact-evaluation:/ae/genwasym-source.tar.gz
sudo docker exec -it genwasym-artifact-evaluation bash
```

Extract the archive under `/ae`. This replaces the relevant source files in
`/ae/GenWasym` and creates the separate `/ae/genwasym-library-demo` project.
Then rebuild GenWasym from the updated source tree:

```bash
cd /ae
tar -xzf /ae/genwasym-source.tar.gz
/ae/GenWasym/build-genwasym-release.sh
```

The expected output is the following CLI distribution under
`/ae/GenWasym`:

```text
dist/genwasym/
├── bin/genwasym
├── lib/
├── include/
├── README.md
└── VERSION
```

Add GenWasym to `PATH`:

```bash
export PATH="/ae/GenWasym/dist/genwasym/bin:$PATH"
```

## How to Run GenWasym on New WebAssembly Programs

The following two examples show how to use GenWasym to compile Wasm programs:
the first runs a concrete Fibonacci program, and the second performs a simple
concolic execution.

Run both examples in a separate workspace so their generated C++ files and
executables do not modify the GenWasym source tree:

```bash
mkdir -p /ae/genwasym-workspace
cd /ae/genwasym-workspace
```

### Concrete Execution Example

The test file `benchmarks/wasm/fib.wat` computes Fibonacci(12) from
its Wasm start function. Generate its specialized C++ with:

```bash
genwasym \
  --input /ae/GenWasym/benchmarks/wasm/fib.wat \
  --output /ae/genwasym-workspace/fib.wat.cpp \
  --print-result
```

This creates `/ae/genwasym-workspace/fib.wat.cpp`.

Omit `--main` only when the module declares a Wasm entrypoint function by `start`.
Run `genwasym --help` for all options. Then compile and run the generated C++:

```bash
clang++ /ae/genwasym-workspace/fib.wat.cpp \
  -o /ae/genwasym-workspace/fib \
  -std=c++17 -O3 \
  -DUSE_IMM -DNO_INFO -DBY_COVERAGE \
  -DNO_REUSE \
  -I/ae/GenWasym/headers \
  -I/ae/GenWasym/third-party/immer \
  -I/ae/GenWasym/third-party/z3/build/z3_install/include \
  -L/ae/GenWasym/third-party/z3/build/z3_install/lib \
  -Wl,-rpath,/ae/GenWasym/third-party/z3/build/z3_install/lib \
  -lz3

/ae/genwasym-workspace/fib
```

This command uses the repository-maintained Z3 build under
`/ae/GenWasym/third-party/z3/build/z3_install`, not the OPAM Z3 package. The
expected output is:

```text
Stack contents:
144
End of Stack contents
Explore Tree Overall Result:
  Unexplored paths: 0
  Finished paths: 1
  Failed paths: 0
  Unreachable paths: 841
  NotToExplore paths: 0
Number of calls to solver: 0
Execution Kind Summary:
Total RESTART executions: 1
Total FROMSNAPSHOT executions: 0
```

`144` is Fibonacci(12). `Finished paths: 1` and `Failed paths: 0` show that the
single concrete execution completed successfully. The 841 unreachable paths
are untaken sides of concrete branches. Because this program has no symbolic
input, GenWasym makes no solver calls. It starts once from the initial state and
does not resume from a snapshot; snapshot reuse is disabled here by `-DNO_REUSE`
option.

### Concolic Execution Example

`benchmarks/wasm/staged/brtable_concolic.wat` creates a symbolic `i32` and uses
it as the selector of a `br_table`. Generate and compile its concolic executor:

```bash
genwasym \
  --input /ae/GenWasym/benchmarks/wasm/staged/brtable_concolic.wat \
  --output /ae/genwasym-workspace/brtable_concolic.wat.cpp

clang++ /ae/genwasym-workspace/brtable_concolic.wat.cpp \
  -o /ae/genwasym-workspace/brtable_concolic \
  -std=c++17 -O3 \
  -DUSE_IMM -DNO_INFO -DBY_COVERAGE -DNO_REUSE \
  -I/ae/GenWasym/headers \
  -I/ae/GenWasym/third-party/immer \
  -I/ae/GenWasym/third-party/z3/build/z3_install/include \
  -L/ae/GenWasym/third-party/z3/build/z3_install/lib \
  -Wl,-rpath,/ae/GenWasym/third-party/z3/build/z3_install/lib \
  -lz3

/ae/genwasym-workspace/brtable_concolic
```

The result includes:

```text
Caught runtime error: Assertion failed: ...
Explore Tree Overall Result:
  Unexplored paths: 0
  Finished paths: 3
  Failed paths: 1
  Unreachable paths: 0
  NotToExplore paths: 0
Number of calls to solver: 6
Execution Kind Summary:
Total RESTART executions: 4
Total FROMSNAPSHOT executions: 0
```

The result shows that different values of the symbolic selector lead to four
feasible execution paths. Three paths finish normally, and one reaches the
deliberately failing assertion. GenWasym calls the solver to discover input
values for these paths. Because this build uses `-DNO_REUSE`, all four
executions restart from the beginning. If `-DNO_REUSE` is removed, GenWasym
saves execution state at symbolic branches and resumes from those snapshots for
the remaining paths. For this example, the counts become one `RESTART` and
three `FROMSNAPSHOT` executions; the same three paths finish and one fails.

### Supported Wasm Subset and Limitations

As a prototype tool, GenWasym supports the core WebAssembly text subset used by
the artifact:

- **Syntax:** GenWasym supports `.wat` S-expression modules that use numeric
  indices and separate import and export fields. Named indices and inline
  import/export abbreviations are not generally supported.
- **Supported features:** scalar `i32`, `i64`, `f32`, and `f64` values;
  constants; locals and globals; `block`, `loop`, `if`, and branches; direct
  calls by `call` and indirect calls by `call_indirect`; selected numeric
  operations and conversions such as `i32.add`, `i64.mul`, `f32.div`,
  `i64.extend_i32_s`, and `i32.trunc_f32_s`; memory operations such as
  `i32.load`, `i64.load32_u`, `f32.load`, `i32.store8`, `i64.store32`, and
  `f64.store`; data segments; and `memory.grow`.
- **Symbolic operations:** `i32.symbolic`, `f32.symbolic`, and `f64.symbolic`
  create symbolic inputs identified by caller-supplied values;
  `i32.sym_assume` restricts exploration to paths satisfying a condition;
  `i32.sym_assert` checks whether an assertion violation is feasible on the
  current path; and `i32.is_symbolic` reports whether an identifier represents
  a symbolic input.
- **Supported imported functions:** `console.log` and `spectest.print_i32`
  print values; `console.assert` checks a concrete condition; `mem.alloc` and
  `mem.free` register and unregister heap ranges for memory checks; and
  `env.proc_exit` accepts a program exit code. Other host imports are not
  supported.
- **Features not generally supported:** features outside the subset above are
  not generally supported, including `.wasm` binaries, `.wast` scripts, SIMD,
  threads and atomics, multiple memories, memory64, GC, reference types,
  exception handling, and instructions such as `memory.size`, `memory.fill`,
  `memory.copy`, and `memory.init`.

The `.wat` programs under `benchmarks/wasm/` can be used as reference inputs.

## How to Reuse GenWasym from Other Software

So far, we have used GenWasym as a standalone CLI to compile a Wasm program to
C++ and then compile the generated C++ into an executable. Next, we show how to
integrate GenWasym as a library in a Scala 2 project.

### Use GenWasym as a Library

#### Step 1: Prepare the Demo Project

In version v8 or later of the Zenodo artifact, the demo is already available at
`/ae/genwasym-library-demo`; continue with Step 2.

For an earlier version, download `genwasym-source.tar.gz` from the
[Zenodo artifact](https://doi.org/10.5281/zenodo.21517229). On the host, copy it
into the running artifact container and enter the container:

```bash
sudo docker cp genwasym-source.tar.gz \
  genwasym-artifact-evaluation:/ae/genwasym-source.tar.gz
sudo docker exec -it genwasym-artifact-evaluation bash
```

Inside the container, extract the archive under `/ae` and build GenWasym:

```bash
cd /ae
tar -xzf /ae/genwasym-source.tar.gz
/ae/GenWasym/build-genwasym-release.sh
cd /ae/genwasym-library-demo
```

The archive contains two top-level directories: `GenWasym/`, with the newer
compiler and CLI sources, and `genwasym-library-demo/`. Extracting it under
`/ae` replaces the relevant source files in `/ae/GenWasym`; the following
build command rebuilds GenWasym. It also creates the separate demo at
`/ae/genwasym-library-demo`.

#### Step 2: Copy the GenWasym JAR

Copy the GenWasym JAR into the demo project's standard `lib/` directory:

```bash
mkdir -p /ae/genwasym-library-demo/lib
cp /ae/GenWasym/dist/genwasym/lib/genwasym.jar \
  /ae/genwasym-library-demo/lib/genwasym.jar
```

#### Step 3: Run the Demo

```bash
sbt "run /ae/GenWasym/benchmarks/wasm/fib.wat /ae/genwasym-library-demo/target/fib.wat.cpp"
```

It runs a simple demo that calls the public API `compileFile` of GenWasym:

```scala
import java.nio.file.Paths
import gensym.wasm.GenWasym

val generatedCpp = GenWasym.compileFile(
  input = Paths.get(args(0)),
  output = Some(Paths.get(args(1))),
  mainExport = args.lift(2)
)
```

`args(0)` is the input `.wat` file and `args(1)` is the path of generated C++ file.
The optional third argument selects an exported entry function; when it is
omitted, GenWasym uses the module's Wasm start function. `compileFile` parses
the module, stages its concolic executor, writes the C++ file, and returns that
file's path.

The generated C++ is written to
`/ae/genwasym-library-demo/target/fib.wat.cpp`. sbt automatically includes the
copied `/ae/genwasym-library-demo/lib/genwasym.jar` because it is in the
project's standard `lib/` directory.
