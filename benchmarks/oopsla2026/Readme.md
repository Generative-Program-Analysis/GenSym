# Artifact Evaluation for the OOPSLA 2026 Paper: "Compiling WebAssembly Concolic Execution with Staging, Continuations, and Snapshots"

This document provides evaluation instructions for the OOPSLA 2026 paper
"Compiling WebAssembly Concolic Execution with Staging, Continuations, and Snapshots."
The paper resolves a dilemma in concolic execution:
instrumentation-based implementations are efficient but struggle to support
snapshot reuse, whereas interpretation-based techniques support snapshots but
are slow. The technique proposed in the paper uses staging to achieve
efficiency and CPS semantics to capture program control, thus enabling snapshot
reuse. As a result, the paper brings the best of both worlds to concolic
execution: efficient execution and snapshot reuse.

This artifact contains:

- GenWasym, a concolic-execution compiler implementing the proposed approach;
- WASP, the baseline concolic execution engine; and
- four benchmark sets used in the paper: B-Tree, Collection-C, Arithmetic-Evaluator, and Quicksort.

The artifact is self-contained and can be run in a Docker container.
The artifact has been tested on Ubuntu 22.04. The remainder of this document
provides instructions for reviewers to evaluate the artifact and reproduce the
paper's results.

You can download the artifact from https://zenodo.org/uploads/21527163.

## 1. Overview of the Artifact

The artifact is packaged as a Docker image. The image already contains all
necessary dependencies and tools to run the experiments.

In addition to the GenWasym and WASP source code, the Docker image includes
several third-party components required by GenWasym and WASP, including the LMS
framework (`lms-clean`), the Z3 SMT solver, and the `immer` persistent data
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
    - `evaluator/` for Arithmetic-Evaluator; and
    - `Collection-C/` for Collection-C.
- `third-party/` contains external dependencies:
  - `wasp/` contains the WASP baseline source code.
  - `lms-clean/` contains the Lightweight Modular Staging (LMS) framework on
    which GenWasym is implemented.
  - `z3/` contains the Z3 SMT solver source and headers used by the concolic
    execution runtime.
  - `immer/` contains the persistent C++ data-structure library used by the
    generated C++ programs.

## 2. Prerequisites (Kick the Tires)

**Expected Time: < 15 minutes**

This step tests the artifact's basic functionality and verifies that the
hardware can run the tests and experiments. The supplied Docker image requires
Linux and an x86-64 (AMD64) CPU. The artifact has been tested on Ubuntu 22.04
and should work on other Linux distributions as well.

For the kick-the-tires test, first load the Docker image and start a container.
Then, run a few simple commands to verify that the container provides the
environment and tools required to run the experiments.

### 2.1 Load the Docker Image

The Docker image is distributed as a `.tar.gz` archive. Load it into Docker with:

```bash
docker load --input genwasym-oopsla2026.tar.gz
```

A successful load ends with:

```text
Loaded image: genwasym:latest
```

Verify that the image is available:

```bash
docker image ls
```

The listed images should include the image that was just loaded.

Then, start a container using the loaded image:

```bash
sudo docker run --name genwasym-artifact-evaluation -it genwasym:latest bash
```

### 2.2 Run the Smoke Test

Run a small GenWasym smoke test. It first builds GenWasym, then uses GenWasym to
compile and run `benchmarks/wasm/fib.wat`, and verifies that the result:

```bash
sbt 'testOnly gensym.wasm.TestStagedConcolicEval -- -z fib'
```

A successful run includes the following output:

```text
Stack contents:
144
...
[info] Tests: succeeded 1, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
```

Run the complete GenWasym test suite:

```bash
sbt 'testOnly gensym.wasm.TestStagedConcolicEval'
```

A successful run ends with `[info] All tests passed.`


### 2.3 Other Useful Docker Commands


Since running all the experiments takes more than 48 hours, we list some useful
commands for reviewers' reference.

```sh
# From inside the container: detach without exiting it
Ctrl-p Ctrl-q
```

```sh
# List all container names
docker ps -a
```

```sh
# Stop the container from another terminal
docker stop <container-name>
```

```sh
# Resume the container from another terminal
docker start -ai <container-name>
```

## 3. Benchmark Descriptions

The evaluation compares GenWasym's generated C++ concolic executables with the
WASP baseline on four WebAssembly benchmark suites. For each suite, the
`genwasym-test-input/` directory contains the `.wat` programs compiled by
GenWasym, and the corresponding `wasp-test-input/` directory contains the
`.wat` or `.wast` programs executed by WASP.

- **B-Tree** contains 28 cases that vary the numbers of ordered and unordered
  symbolic inputs, providing a controlled setting for concolic execution of
  B-Tree operations.
- **Collection-C** contains 8 modules and 144 cases in total. Its Wasm
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

**Expected Results:** This evaluation runs GenWasym and WASP on the benchmark
suites presented in the paper and summarizes their performance.
Before running the experiments, verify that no results are present:

```bash
bash benchmarks/oopsla2026/summarize_results.sh
```

The script creates one CSV file per benchmark suite to summarize the
performance-evaluation results:

```text
benchmarks/oopsla2026/final_results_btree.compilation.csv
benchmarks/oopsla2026/final_results_quicksort.compilation.csv
benchmarks/oopsla2026/final_results_evaluator.compilation.csv
benchmarks/oopsla2026/final_results_Collection-C.compilation.csv
```

At this point, each CSV file should contain only its header and no data rows.
The same command is used after the experiments to summarize the collected
results.


#### Examine the Benchmark Files

The benchmark files are located in the `benchmarks/oopsla2026/` directory. Run
the following commands to show the number of lines in the WAT files for each
benchmark suite:

```bash
find benchmarks/oopsla2026/btree/genwasym-test-input -name '*.wat' | xargs wc -l
find benchmarks/oopsla2026/quicksort/genwasym-test-input -name '*.wat' | xargs wc -l
find benchmarks/oopsla2026/evaluator/genwasym-test-input -name '*.wat' | xargs wc -l
find benchmarks/oopsla2026/Collection-C/genwasym-test-input -name '*.wat' | xargs wc -l
```

#### Use GenWasym to Compile the Benchmark Files

In this step, we will compile the benchmark WAT files into C++ concolic-execution
programs using GenWasym and then use `clang++` to compile the generated C++ files
into executables.

From inside the container, use GenWasym to generate the C++ files from WAT:

```bash
bash benchmarks/oopsla2026/compile_wats.sh
```

This step uses `sbt` to run the GenWasym compiler on the benchmark WAT files.
The command succeeds when it prints `All tests passed.`

Verify that GenWasym generated one C++ file for every performance benchmark:

```bash
find benchmarks/oopsla2026/{btree,quicksort,evaluator}/genwasym-test-artifacts benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts/normal -type f -name '*.wat.cpp' | wc -l
```

The expected count is `187`.

Then, compile the generated C++ files into executables:

```bash
python3 benchmarks/oopsla2026/compile.py --skip-newer \
  benchmarks/oopsla2026/btree/genwasym-test-artifacts
python3 benchmarks/oopsla2026/compile.py --skip-newer \
  benchmarks/oopsla2026/quicksort/genwasym-test-artifacts
python3 benchmarks/oopsla2026/compile.py --skip-newer \
  benchmarks/oopsla2026/evaluator/genwasym-test-artifacts
python3 benchmarks/oopsla2026/compile.py --skip-newer \
  benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts
```

During this step, the compilation commands will be printed in the terminal.
The resulting executables are stored in the same directories as the C++ files.
Verify them with this command:

```bash
find benchmarks/oopsla2026/{btree,quicksort,evaluator}/genwasym-test-artifacts benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts/normal -type f -name '*.exe' | wc -l
```

The expected count is `561` (`3 * 187`), because each benchmark is compiled for
three GenWasym configurations.

#### Compare the Performance of Compiled Executables with WASP

In the paper, we run each benchmark five times with both GenWasym and WASP. This
requires around 48 hours (tested on a machine with 32 GB RAM and 8 cores
running Ubuntu 22.04).
We suggest that reviewers do not exit the container until all experiments finish.

To compare the performance of GenWasym and WASP, run:

```bash
bash benchmarks/oopsla2026/run_btree.sh
bash benchmarks/oopsla2026/run_quicksort.sh
bash benchmarks/oopsla2026/run_evaluator.sh
bash benchmarks/oopsla2026/run_collection_c.sh
```

While running, these scripts print the execution command for each benchmark case.

For each benchmark, both GenWasym and WASP record the execution-time metrics in
a file. These reports are stored in the
`benchmarks/oopsla2026/<suite>/genwasym-test-output/` and
`benchmarks/oopsla2026/<suite>/wasp-test-output/` directories. In the next
section, we will use these reports to compare the performance of GenWasym and
WASP.

Note: To quickly verify a single-run result, run each test case once instead of
five times by using `--runs 1`:

```bash
bash benchmarks/oopsla2026/run_btree.sh --runs 1
bash benchmarks/oopsla2026/run_quicksort.sh --runs 1
bash benchmarks/oopsla2026/run_evaluator.sh --runs 1
bash benchmarks/oopsla2026/run_collection_c.sh --runs 1
```

#### Summarize Results and Reproduce Table 1 and Figure 10

After the runs finish, we first convert the raw WASP and GenWasym reports into
per-suite CSV files and then summarize each suite for the compilation
experiment:

```bash
bash benchmarks/oopsla2026/summarize_results.sh
```

This step produces the following CSV files:

```
benchmarks/oopsla2026/final_results_btree.compilation.csv
benchmarks/oopsla2026/final_results_quicksort.compilation.csv
benchmarks/oopsla2026/final_results_evaluator.compilation.csv
benchmarks/oopsla2026/final_results_Collection-C.compilation.csv
```

The CSV files contain the following columns:

| Column | Description |
| --- | --- |
| `Suite` | Benchmark suite name (e.g., B-Tree, Quicksort, Arithmetic-Evaluator, Collection-C). |
| `Benchmark` | Name of the benchmark program. |
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

```bash
python3 benchmarks/oopsla2026/average_speedup.py --cutoff 10o3u --cutoff quicksort2.sym3.size10
```

The command should produce output similar to the following. Exact values may
vary across machines and runs:

```
Average speedups across all selected benchmark rows:
  Default: arithmetic mean=100.6x, geomean=27.5x (49 data points; Collection-C: 8, btree: 27, evaluator: 9, quicksort: 5)
  Snapshot: arithmetic mean=109.0x, geomean=37.6x (49 data points; Collection-C: 8, btree: 27, evaluator: 9, quicksort: 5)
  CostModel: arithmetic mean=277.1x, geomean=44.4x (49 data points; Collection-C: 8, btree: 27, evaluator: 9, quicksort: 5)
Relative GenWasym speedups:
  Snapshot/Default: arithmetic mean=1.6x, geomean=1.4x (49 data points; Collection-C: 8, btree: 27, evaluator: 9, quicksort: 5)
  CostModel/Default: arithmetic mean=2.2x, geomean=1.6x (49 data points; Collection-C: 8, btree: 27, evaluator: 9, quicksort: 5)
```

The `Snapshot` geometric mean corresponds to the **Staging + Snapshot** bar in
Figure 10b, and the `CostModel` geometric mean corresponds to the **Staging +
Snapshot + Heuristic** bar. We consider the results reproduced when the
`Default` geometric mean is close to the **29.4x** reported in the paper and
the `Snapshot/Default` and `CostModel/Default` geometric means are both close
to **1.0x**.

To reproduce the two panels of Figure 10, run the following command to generate
the SVG files:

```bash
python3 benchmarks/oopsla2026/plot_speedup.py
```

The script reads the four compilation CSV files, separates Quicksort into its
two settings, and produces `benchmarks/oopsla2026/fig_speedup_left.svg` (Figure 10a:
per-benchmark speedups) and `benchmarks/oopsla2026/fig_speedup_right.svg` (Figure 10b:
overall speedups and the legend). The left panel uses the selected per-group
summary statistic (mean by default), while the right panel uses geometric means
across all available benchmark rows.

### 4.2 Bug Detection Experiments (RQ3)

**Expected Time: < 30 minutes.** This experiment aims to answer RQ3 and
reproduces the results in Table 2. We will compile two buggy Collection-C
programs, run them with GenWasym and WASP, and summarize whether each tool
detects the bugs.

**Expected Results:** Both GenWasym and WASP should detect the invalid memory
accesses in the two buggy Collection-C programs. The summary table should show
that both tools find each bug and report their path counts and execution times.

#### Use GenWasym and WASP to Detect Bugs in Two Buggy Collection-C Programs

Use GenWasym to compile the buggy WebAssembly programs into C++
concolic-execution programs:

```bash
sbt 'testOnly gensym.wasm.TestBenchmark -- -z compile-collection-c-buggy-benchmarks'
```

Compile the generated C++ files, run the two GenWasym executables once, and run
the corresponding WASP inputs once:

```bash
bash benchmarks/oopsla2026/run_collection_c_buggy.sh --runs 1
```

The run logs for the two buggy programs are stored in
`benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts/buggy/array_test_remove/run-logs`
and
`benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts/buggy/list_test_zipIterAdd/run-logs`,
respectively. 

To check the logs to see whether GenWasym detected the bugs:

```bash
cat benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts/buggy/array_test_remove/run-logs/array_test_remove.wat.exe.run_0.log
cat benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts/buggy/list_test_zipIterAdd/run-logs/list_test_zipIterAdd.wat.exe.run_0.log
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

Then check the WASP's output to see if the bug is also detected by WASP:

```bash
cat benchmarks/oopsla2026/Collection-C/wasp-test-output/buggy/array_test_remove.out/report_0.json
```

The JSON output should contain the following fields:

```
...
"specification": false,
"reason": {}
...
```

This confirms that GenWasym effectively detects the bug as WASP does.

Run the following command to summarize the GenWasym NoConfig execution times in
a table:

```bash
bash benchmarks/oopsla2026/summarize_buggy.sh
```

The table reports separate GenWasym and WASP path counts, whether each tool
finds the bug, and instruction-execution and total times for both tools. Write
the same table as a CSV file with:

```bash
bash benchmarks/oopsla2026/summarize_buggy.sh \
  -o benchmarks/oopsla2026/Collection-C/buggy_runtime.csv
```

The output table columns are:

| Column | Description |
| --- | --- |
| `Benchmark` | Name of the buggy benchmark. |
| `n_GenWasymPaths` | Number of paths finished by GenWasym in the noreuse config. |
| `n_WASP_paths` | Number of paths explored by WASP. This can differ from the GenWasym count. |
| `Finds Bug` | Whether GenWasym and WASP each reported the invalid memory access. |
| `T_GenWasym_instr_exec_s` | GenWasym instruction-execution time, in seconds. |
| `T_GenWasym_total_s` | GenWasym total wall-clock runtime, in seconds. |
| `T_WASP_instr_exec_s` | WASP instruction-execution time, in seconds. |
| `T_WASP_total_s` | WASP total loop runtime, in seconds. |

Now reviewers can compare the results in this CSV file with Table 2 in the paper.
