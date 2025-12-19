# Evaluation Guide
This artifact contains WebAssembly concolic compiler GenWasym implemented in
Scala, which transforms WebAssembly modules to C++ code that simultaneously
executes both concrete and symbolic executions.
In this evaluation, we make three main evaluation claims:
1. The instruction execution of compiled C++ code is faster than that of WASP.
2. The snapshot reuse mechanism improves the performance of concolic execution
   when the program shared long common execution path prefixes.
3. The heuristic strategy developed in the paper can effectively decide whether
   to take snapshots and reuse them or simply re-execute the program from the
   beginning.

These claims correspond to the three RQs in the paper:
1. RQ1: What speedup does compilation achieve over interpretation-based approaches?
2. RQ2: How much does snapshot-reuse improve execution on top of compilation?
3. RQ3: How effective is the heuristic in deciding when to create snapshots?


The evaluation comprises three main parts:
1. On the B-tree benchmark, compare the performance of compiled C++ code against
   WASP.
2. On the crafted benchmark, compare the performance of our concolic compiler
   with and without snapshot reuse.
3. On the crafted benchmark, compare the performance of our concolic compiler
   with and without the heuristic strategy for snapshot creation.

The rest of this document describes how to reproduce the evaluation results
presented in the paper.

## Prerequisites
To run the evaluation, you need both setup both the GenWasym concolic compiler
and the WASP.

### Setting up GenWasym
Checkout to the `zdh/benchmark` branch of the GenSym repository.
```bash
git clone git@github.com:Generative-Program-Analysis/GenSym.git
```

Then, use the sbt tool to build the project. Please make sure you have
installed sbt in your system. You can follow the instructions in the
[sbt website](https://www.scala-sbt.org/download.html) to install sbt.

```bash
sbt compile
```


### Setting up WASP
We are using the latest release version release/0.2.3 of WASP for the
evaluation. Please follow the instructions in the [WASP
repository](https://github.com/formalsec/wasp/tree/release/0.2.3) to set up
WASP.


## Running the Evaluation
### On the B-tree benchmark, compare the performance of compiled C++ code against WASP
The B-tree benchmark program is located in the `benchmarks/pldi2026/btree/`
directory. To compile the B-tree benchmark to C++ using GenWasym, run the
following command in the root directory of the project:
```bash
sbt 'testOnly gensym.wasm.TestBenchmark -- -z compile-btree-benchmarks'
```
Now we can find the compiled C++ code in the `benchmarks/pldi2026/btree/`
directory.

To compile and the compiled g++ code, run the following python script in the `benchmarks/pldi2026/btree/` directory:
```bash
python compile.py
```

To run these compiled binaries, run the following command:
```bash
python run -r
```


### On the crafted benchmark, compare the performance of our concolic compiler in different settings
The crafted benchmark program is located in the `benchmarks/pldi2026/crafted/`
directory. To compile the crafted benchmark to C++ using GenWasym, run the
following command in the root directory of the project:
```bash
sbt 'testOnly gensym.wasm.TestBenchmark -- -z compile-crafted-benchmarks'
```
Now we can find the compiled C++ code in the `benchmarks/pldi2026/crafted/`
directory.

To compile and the compiled g++ code, run the following python script in the `benchmarks/pldi2026/crafted/` directory:
```bash
python compile.py
```

To run these compiled binaries, run the following command:
```bash
python run -r
```
