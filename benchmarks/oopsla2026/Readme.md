# Artifact Evaluation for OOPSLA 2026 Paper: "Compiling WebAssembly Concolic Execution with Staging, Continuations, and Snapshots"

This document provides evaluation instructions for the OOPSLA 2026 paper
"Compiling WebAssembly Concolic Execution with Staging, Continuations, and Snapshots." 
The paper resolves a dilemma in concolic execution:
instrumentation-based implementations are efficient but struggle to support
snapshot reuse, whereas interpretation-based techniques support snapshots but
are slow. The technique proposed in the paper uses staging to achieve
efficiency and CPS semantics to capture program control, thus enabling snapshot
reuse. As a result, the paper brings the best of both worlds to concolic
execution: efficient execution and snapshot reuse.

This artifact contains 

- GenWasym, a concolic-execution compiler implementing the proposed approach 
- WASP, the baseline concolic execution engine
- Four benchmark sets used in the paper: B-Tree, Collection-C, Arithmetic-Evaluator, and Quicksort. 

The artifact is self-contained and can be run in a Docker container. 
The artifact has been tested on Ubuntu 22.04. The remainder of this document
provides instructions for reviewers to evaluate the artifact and reproduce the
paper's results.

## 1. Overview of the Artifact

The artifact is packaged as a Docker image. The image already contains all
necessary dependencies and tools to run the experiments.

In the Docker image, besides the source code of GenWasym and WASP, we also installed
several third-party components required by GenWasym and WASP, including the LMS
framework (`lms-clean`), SMT solver Z3, and the `immer` persistent data
structure library.

The GenWasym source code is located in `/ae/GenWasym` inside the container. 
Its relevant directory structure is:

- `src/` contains the GenWasym implementation and test suite:
  - `main/scala/wasm/` contains the WebAssembly AST, parser, interpreter, and
    staged concolic-execution implementation. In particular,
    `StagedConcolicMiniWasm.scala` implements the main staged CPS interpreter.
  - `main/scala/lms/` contains LMS extensions and C++ code-generation support.
  - `main/java/wasm/` contains generated Java sources for the WebAssembly
    text-format parser.
  - `test/scala/genwasym/` contains the GenWasym test cases.
- `benchmarks/` contains the test inputs and evaluation benchmarks:
  - `wasm/` contains WebAssembly input programs for the GenWasym test suite.
  - `oopsla2026/` contains the inputs, scripts, and output directories for the
    paper's evaluation:
    - `btree/` for B-Tree;
    - `quicksort/` for Quicksort;
    - `crafted/` for Arithmetic-Evaluator; and
    - `Collection-C/` for Collection-C.
- `third-party/` contains external dependencies:
  - `wasp/` contains the WASP baseline source code.
  - `lms-clean/` contains the Lightweight Modular Staging (LMS) framework on
    which GenWasym is implemented.
  - `z3/` contains the Z3 SMT solver source and headers used by the concolic
    execution runtime.
  - `immer/` contains the persistent C++ data-structure library used by the
    generated C++ programs.

## 2. Prerequisites (Kick the tires)

**Expected Time: < 15 minutes**

This step tests the artifact's basic functionality and verifies that the
hardware can run the tests and experiments. The supplied Docker image requires
Linux and x86-64 (AMD64) CPUs. The artifact has been tested on Ubuntu 22.04,
and should work on other Linux distributions as well. 

The Docker image is distributed as a `.tar.gz` archive. Load it into Docker with:

```bash
docker load --input genwasym-oopsla2026.tar.gz
```

The command prints the image name and tag that were loaded. Verify that the
image is available:

```bash
docker image ls
```

Then start a container using the loaded image. If the loaded tag is `ae:latest`, run:

```bash
docker run --rm -it ae:latest bash
cd /ae/GenWasym
```

Run a simple test case to verify that GenWasym is working correctly:

```bash
sbt 'testOnly gensym.wasm.TestStagedConcolicEval -- -z fib'
```

Useful Docker commands for reviewers are listed below:

```sh
# From inside of the container: detach without exiting it
Ctrl-p Ctrl-q
```

```sh
# List all container names
docker ps -a
```

```sh
# Stop it from another terminal
docker stop <container-name>
```

```sh
# Resume it from another terminal
docker start -ai <container-name>
```

## 3. Benchmark Descriptions

The evaluation compares GenWasym's generated C++ concolic executables with the
WASP baseline on four WebAssembly benchmark suites. For each suite, the
`genwasym-test-input/` directory contains the `.wat` programs compiled by
GenWasym, and the corresponding `wasp-test-input/` directory contains the
`.wast` programs executed by WASP.

- **B-Tree** contains 26 cases that vary the numbers of ordered and unordered
  symbolic inputs, providing a controlled setting for concolic execution of
  B-Tree operations.
- **Collection-C** contains 8 modules and 143 cases in total. Its Wasm
  programs are compiled from C and exercise collection data structures,
  including arrays, lists, queues, deques, trees, and ring buffers. The normal
  programs are used for performance evaluation; the suite also includes buggy
  variants used in the bug-detection experiment.
- **Arithmetic-Evaluator** contains 9 WebAssembly programs that parse and
  evaluate string-represented arithmetic expressions. The cases vary the
  expression size and the number of explored paths.
- **Quicksort** is a C implementation compiled to Wasm. The cases vary the
  input-array size, the number of symbolic elements, and the complexity of
  their symbolic expressions.

## 4. Step-by-Step Evaluation Instructions 

### 4.1 Performance Evaluation (RQ1 and RQ2)

**Expected Time: ~48 hours.** This experiment aims to answer RQ1 and RQ2 in the
paper and produces the results in Table 1 and Figure 10. We will first examine
the benchmark files, then run the performance comparison between GenWasym and
WASP, and finally summarize the results to reproduce Table 1 and Figure 10.

#### Examine the Benchmark Files

The benchmark files are located in the `benchmarks/oopsla2026/` directory.
To show the number of lines in the WAT files for each benchmark suite:

```bash
find benchmarks/oopsla2026/btree/genwasym-test-input -name '*.wat' | xargs wc -l
find benchmarks/oopsla2026/quicksort/genwasym-test-input -name '*.wat' | xargs wc -l
find benchmarks/oopsla2026/crafted/genwasym-test-input -name '*.wat' | xargs wc -l
find benchmarks/oopsla2026/Collection-C/genwasym-test-input -name '*.wat' | xargs wc -l
```

#### Use GenWasym to Compile the Benchmark Files

From inside the container, use GenWasym to generate the C++ files from WAT:

```bash
sbt \
  'testOnly gensym.wasm.TestBenchmark -- -z compile-btree-benchmarks' \
  'testOnly gensym.wasm.TestBenchmark -- -z compile-quicksort-benchmark' \
  'testOnly gensym.wasm.TestBenchmark -- -z compile-crafted-benchmarks' \
  'testOnly gensym.wasm.TestBenchmark -- -z compile-collection-c-normal-benchmarks'
```

Compile the generated C++ files into executables:

```bash
python3 benchmarks/oopsla2026/compile.py --skip-newer \
  benchmarks/oopsla2026/btree/genwasym-test-artifacts \
  benchmarks/oopsla2026/quicksort/genwasym-test-artifacts \
  benchmarks/oopsla2026/crafted/genwasym-test-artifacts \
  benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts
```

#### Compare the Performance of Compiled Executables with WASP

In the paper, we run GenWasym and WASP on each benchmark both for 5 times. This
requires around 48 hours (tested on a machine with 32 GB RAM and 8 cores
running Ubuntu 22.04). 
We suggest that reviewers do not exit the container until all experiments finish. 

Then to compare the performance of GenWasym and WASP, run:

```bash
bash benchmarks/oopsla2026/run_btree.sh
bash benchmarks/oopsla2026/run_quicksort.sh
bash benchmarks/oopsla2026/run_crafted.sh
bash benchmarks/oopsla2026/run_collection_c.sh
```

Note: To quickly verify a single-run result, it is possible to run each test
case once instead of five times, use `--runs 1`:

```bash
bash benchmarks/oopsla2026/run_btree.sh --runs 1
bash benchmarks/oopsla2026/run_quicksort.sh --runs 1
bash benchmarks/oopsla2026/run_crafted.sh --runs 1
bash benchmarks/oopsla2026/run_collection_c.sh --runs 1
```

Each benchmark wrapper supports `--quick --case CASE` for a single-case
test; quick mode runs the selected case once. For B-Tree, Quicksort, and
Arithmetic-Evaluator, `CASE` is the input-file basename without the `.wat` or
`.wast` extension. For example, `2o1u` selects
`btree/genwasym-test-input/2o1u.wat` and `btree/wasp-test-input/2o1u.wast`:

```bash
bash benchmarks/oopsla2026/run_btree.sh --quick --case 2o1u
bash benchmarks/oopsla2026/run_quicksort.sh --quick --case quicksort1.sym2.size20
bash benchmarks/oopsla2026/run_crafted.sh --quick --case parse_expr2000-8
bash benchmarks/oopsla2026/run_collection_c.sh --quick --case array/array_test_add
```

For Collection-C, `CASE` is the path relative to its `normal/` input directory,
in the form `MODULE/CASE` (for example, `array/array_test_add`).

#### Summarize Results and Reproduce Table 1

After the runs finish, we first convert the raw WASP and GenWasym reports into
per-suite CSV files, then summarize each suite for the compilation experiment:

```bash
./summarize_results.sh
```

The above step produces the following CSV files:

```
benchmarks/oopsla2026/final_results_btree.compilation.csv
benchmarks/oopsla2026/final_results_quicksort.compilation.csv
benchmarks/oopsla2026/final_results_crafted.compilation.csv
benchmarks/oopsla2026/final_results_Collection-C.compilation.csv
```

The produced CSV files contain the following columns:

| Column | Description |
| --- | --- |
| `Suite` | Benchmark suite name (e.g., B-Tree, Quicksort, Arithmetic-Evaluator, Collection-C). |
| `Benchmark` | Name of single benchmark program program. |
| `npaths` | Number of execution paths explored. |
| `T_WASP_instr_exec(s)` | Mean WASP instruction-execution time, in seconds. |
| `SD_WASP_instr_exec(s)` | Standard deviation of WASP instruction-execution time, in seconds. |
| `T_WASP_total(s)` | Mean total WASP execution time, in seconds. |
| `T_GenWasym_NoConfig_instr_exec(s)` | Mean GenWasym instruction-execution time without snapshot reuse or the heuristic, in seconds. |
| `SD_GenWasym_NoConfig_instr_exec(s)` | Standard deviation of that time, in seconds. |
| `T_GenWasym_NoConfig_total(s)` | Mean total GenWasym execution time for the no snapshot reuse or the heuristic configuration, in seconds. |
| `Speedup_NoConfig` | WASP instruction-execution time divided by GenWasym noreuse instruction-execution time. |
| `T_GenWasym_Snapshot_instr_exec(s)` | Mean GenWasym instruction-execution time with snapshot reuse, in seconds. |
| `SD_GenWasym_Snapshot_instr_exec(s)` | Standard deviation of that time, in seconds. |
| `T_GenWasym_Snapshot_total(s)` | Mean total GenWasym execution time for the Snapshot configuration, in seconds. |
| `T_GenWasym_CostModel_instr_exec(s)` | Mean GenWasym instruction-execution time with snapshot reuse and the cost model, in seconds. |
| `SD_GenWasym_CostModel_instr_exec(s)` | Standard deviation of that time, in seconds. |
| `T_GenWasym_CostModel_total(s)` | Mean total GenWasym execution time for the CostModel configuration, in seconds. |
| `SpeedupSnapshot` | GenWasym NoConfig instruction-execution time divided by Snapshot instruction-execution time. |
| `SpeedupHeuristic` | GenWasym NoConfig instruction-execution time divided by CostModel instruction-execution time. |

Reviewers can compare the results in these CSV files with Table 1 in the paper. 

We consider the results reproduced if every `Speedup_NoConfig` value in the
CSV files is greater than `1x`; that is, GenWasym without snapshot reuse is
faster than WASP for every reported benchmark.
For the geometric mean of the speedups, it should be close to the `29.4x`
reported in the paper. Run the following command to compute the geometric mean
of the speedups for all benchmark rows in the CSV files. (The `--cutoff`
options exclude benchmarks for which WASP exceeded the two-hour timeout.)

```
python3 benchmarks/oopsla2026/average_speedup.py --cutoff 10o3u --cutoff quicksort2.sym3.size10
```

The command should produce output similar to the following. Exact values may
vary across machines and runs:

```
Average speedups across all selected benchmark rows:
  Default: arithmetic mean=100.6x, geomean=27.5x (51 data points; Collection-C: 8, btree: 26, crafted: 12, quicksort: 5)
  Snapshot: arithmetic mean=109.0x, geomean=37.6x (51 data points; Collection-C: 8, btree: 26, crafted: 12, quicksort: 5)
  CostModel: arithmetic mean=277.1x, geomean=44.4x (51 data points; Collection-C: 8, btree: 26, crafted: 12, quicksort: 5)
Relative GenWasym speedups:
  Snapshot/Default: arithmetic mean=1.6x, geomean=1.4x (51 data points; Collection-C: 8, btree: 26, crafted: 12, quicksort: 5)
  CostModel/Default: arithmetic mean=2.2x, geomean=1.6x (51 data points; Collection-C: 8, btree: 26, crafted: 12, quicksort: 5)
```

The `Snapshot` geometric mean corresponds to the **Staging + Snapshot** bar in
Figure 10b, and the `CostModel` geometric mean corresponds to the **Staging +
Snapshot + Heuristic** bar. We consider the results reproduced when the
`Default` geometric mean is close to the **29.4x** reported in the paper and
the `Snapshot/Default` and `CostModel/Default` geometric means are both close
to **1.0x**.

### 4.2 Bug Detection Experiments (RQ3)

**Expected Time: < 30min**

FIXME: explain how this correspond to paper table 2

This part of the artifact tests whether GenWasym can detect bugs in Wasm programs, as WASP does.

Use GenWasym to compile the buggy WebAssembly programs into C++ concolic execution programs:

```bash
sbt 'testOnly gensym.wasm.TestBenchmark -- -z compile-collection-c-buggy-benchmarks'
```

Compile the generated C++ files into executables:

```bash
bash benchmarks/oopsla2026/run_collection_c_buggy.sh --runs 1
```

Run the compiled executables:

```bash
./benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts/buggy/array_test_remove/array_test_remove.wat.exe
./benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts/buggy/list_test_zipIterAdd/list_test_zipIterAdd.wat.exe 
```

The run logs for the two buggy programs are stored in
`benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts/buggy/array_test_remove/run-logs`
and
`benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts/buggy/list_test_zipIterAdd/run-logs`,
respectively. 

To check the logs to see whether GenWasym detected the bugs:

```bash
cat benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts/buggy/array_test_remove/run-logs/array_test_remove.wat.exe.log
cat benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts/buggy/list_test_zipIterAdd/run-logs/list_test_zipIterAdd.wat.exe.log
```

The output should contain a message similar to the following. More than one
error may be reported if the execution encounters multiple invalid memory
accesses; the sequence of the reported address may vary due to the exploration
order of execution paths.

```
...
Address 66621 with width 1 is not in any allocated range.
...
```

This confirms that GenWasym can detect bugs in Wasm programs.

Then check if the bug is also detected by WASP:

```bash
cat benchmarks/oopsla2026/Collection-C/wasp-test-output/buggy/array_test_remove.out/report.json
```

The expected JSON output should contain the following fields:
```
...
"specification": false,
"reason": {}
...
```

This confirms that GenWasym effectively detects the bug as WASP does.
