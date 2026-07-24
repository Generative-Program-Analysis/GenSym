# Artifact Evaluation for OOPSLA 2026 Paper: "Compiling WebAssembly Concolic Execution with Staging, Continuations, and Snapshots"

## Artifact Evaluation Guide
This artifact contains the implementation of GenWasym, a concolic execution engine for WebAssembly. To achieve efficient concolic execution, GenWasym compiles WebAssembly programs into C++ concolic execution programs through multi-stage programming in LMS.

We suggest that artifact reviewers evaluate this artifact in two steps:
1. Evaluate the functionality of the artifact to verify that it satisfies the Functional and Reusable badges.****
2. Reproduce the results in the paper to verify that it satisfies the Results Validated: Results Reproduced badge.


## Step 0: Prerequisites (Kick the tires)
Our artifact requires a Linux environment with Docker installed. The artifact has been tested on 22.04, and should work on other Linux distributions as well.

The Docker image is distributed as a `.tar.gz` archive. Load it into Docker with:

```bash
docker load --input genwasym-oopsla2026.tar.gz
```

The command prints the image name and tag that were loaded. Verify that the
image is available:

```bash
docker image ls
```

Then start a container using the loaded image. If the loaded tag is
`ae:latest`, run:

```bash
docker run --rm -it ae:latest bash
cd /ae/GenSym
```


## Part 1: Steps to Evaluate This Artifact

This section explains the organization of the artifact and how to inspect and
run its tests.

### Architecture and Source Code

GenWasym is implemented in Lightweight Modular Staging (LMS) in Scala. Its main
implementation is a staged wasm CPS interpreter in `src/main/scala/wasm/StagedConcolicMiniWasm.scala`, whose entry point is the `eval` function that takes an AST of a WebAssembly program and produces a staged C++ concolic execution program. For implementation details, please refer to the source code and the paper.


### Run the Tests
The GenWasym-related test cases are located in `src/test/scala/genwasym/`,
and the GenSym tests are located in `src/test/scala/gensym/`. Benchmark inputs
are located under `benchmarks/wasm/`.


Run the staged concolic evaluation test suite with:

```bash
sbt 'testOnly gensym.wasm.TestStagedConcolicEval'
```


## Part 2: Reproduce the Results in the Paper

Running whole of experiment requires at around 36 hours (tested on a machine with 32 GB RAM and 8 cores, Ubuntu 22.04). So we suggest reviewers don't exit the container until all the experiments finish. Some useful commands:


```sh
# From inside of the container: detach without exiting it
Ctrl-p Ctrl-q
```

```sh
# List all container's names
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


### Run Performance Experiments

#### Use GenWasym to Compile the Benchmark Files
From inside the container, use GenWasym to generate the C++ files from wat:

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

Then to compare the performance of GenWasym and WASP, run:

```bash
bash benchmarks/oopsla2026/run_btree.sh
bash benchmarks/oopsla2026/run_quicksort.sh
bash benchmarks/oopsla2026/run_crafted.sh
bash benchmarks/oopsla2026/run_collection_c.sh
```

****

To run each testcase once instead of five times, use `--runs 1`:

```bash
bash benchmarks/oopsla2026/run_btree.sh --runs 1
bash benchmarks/oopsla2026/run_quicksort.sh --runs 1
bash benchmarks/oopsla2026/run_crafted.sh --runs 1
bash benchmarks/oopsla2026/run_collection_c.sh --runs 1
```

Use `--quick --case CASE` for a single-case smoke test; quick mode runs the
selected testcase once regardless of `--runs`.

#### Collect Results

After the runs finish, convert the raw WASP and GenWasym reports into the
final CSV files:

```bash
python3 benchmarks/oopsla2026/collect_result.py --suite btree --write-raw
python3 benchmarks/oopsla2026/collect_result.py --suite quicksort --write-raw
python3 benchmarks/oopsla2026/collect_result.py --suite crafted --write-raw
python3 benchmarks/oopsla2026/collect_result.py --suite Collection-C --write-raw
```

This creates files named:

```text
benchmarks/oopsla2026/final_results_btree.csv
benchmarks/oopsla2026/final_results_quicksort.csv
benchmarks/oopsla2026/final_results_crafted.csv
benchmarks/oopsla2026/final_results_Collection-C.csv
```


#### Summarize Results and Reproduce Table 1

Summarize the results of each suite into a single CSV file:

```bash
python3 benchmarks/oopsla2026/summary.py \
  --rq compilation --suite btree \
  -o benchmarks/oopsla2026/final_results_btree.compilation.csv

python3 benchmarks/oopsla2026/summary.py \
  --rq compilation --suite quicksort \
  -o benchmarks/oopsla2026/final_results_quicksort.compilation.csv

python3 benchmarks/oopsla2026/summary.py \
  --rq compilation --suite crafted \
  -o benchmarks/oopsla2026/final_results_crafted.compilation.csv

python3 benchmarks/oopsla2026/summary.py \
  --rq compilation --suite Collection-C \
  -o benchmarks/oopsla2026/final_results_Collection-C.compilation.csv
```


Reviewers can compare the results in these CSV files with Table 1 in the
paper. We consider the results reproduced if the `Speedup_NoConfig` values in
the CSV files are greater than `3x` for the reported benchmarks and their
geometric mean is greater than `20x`. To compute the geometric mean of the
speedups, run:

```
python3 benchmarks/oopsla2026/average_speedup.py --cutoff 3o1u --cutoff 10o3u --cutoff quicksort2.sym3.size10
```

The reviewer should see output similar to this (the exact values might be different):
```
Average speedups across all selected benchmark rows:
  Default: arithmetic mean=100.6x, geomean=27.5x (51 data points; Collection-C: 8, btree: 26, crafted: 12, quicksort: 5)
  Snapshot: arithmetic mean=109.0x, geomean=37.6x (51 data points; Collection-C: 8, btree: 26, crafted: 12, quicksort: 5)
  CostModel: arithmetic mean=277.1x, geomean=44.4x (51 data points; Collection-C: 8, btree: 26, crafted: 12, quicksort: 5)
Relative GenWasym speedups:
  Snapshot/Default: arithmetic mean=1.6x, geomean=1.4x (51 data points; Collection-C: 8, btree: 26, crafted: 12, quicksort: 5)
  CostModel/Default: arithmetic mean=2.2x, geomean=1.6x (51 data points; Collection-C: 8, btree: 26, crafted: 12, quicksort: 5)
```

The geometric mean of speedups of `Snapshot` corresponds to the bar "Staging + Snapshot" in figure 10b. 
The geometric mean of speedups of `CostModel` corresponds the bar "Staging + Snapshot + Heuristic" in figure 10b. 



### Run Bug Detection Experiments

Use GenWasym to compile the buggy WebAssembly programs into C++ concolic execution programs:

```bash
sbt 'testOnly gensym.wasm.TestBenchmark -- -z compile-collection-c-buggy-benchmarks'
```

Compile the generated C++ files into executables, run the compiled executables:

```bash
bash benchmarks/oopsla2026/run_collection_c_buggy.sh --runs 1
```

Run the compiled executables:

```bash
./benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts/buggy/array_test_remove/array_test_remove.wat.exe
./benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts/buggy/list_test_zipIterAdd/list_test_zipIterAdd.wat.exe 
```


Then the terminal should print the detected memory access errors:

```
...
Address 66604 with width 1 is not in any allocated range.
...
```

Now we have successfully reproduced the results in table 2. 
