# GenWasym CLI

GenWasym compiles WebAssembly text (`.wat`) programs into specialized C++
concolic executors.

Java 8 or newer is required. From the GenWasym source-tree root, compile the
existing Fibonacci benchmark with:

```bash
dist/genwasym/bin/genwasym --help
dist/genwasym/bin/genwasym --input benchmarks/wasm/fib.wat \
  --output fib.wat.cpp --print-result
```

Compile the generated C++ with Clang and Z3:

```bash
clang++ fib.wat.cpp -o fib \
  -std=c++17 -O3 -DNDEBUG \
  -DUSE_IMM -DENABLE_PROFILE_TIME -DNO_INFO -DBY_COVERAGE \
  -DUSE_SOFT_ASSERT -DNO_REUSE \
  -I/ae/GenWasym/dist/genwasym/include \
  -I/ae/GenWasym/third-party/z3/build/z3_install/include \
  -L/ae/GenWasym/third-party/z3/build/z3_install/lib \
  -Wl,-rpath,/ae/GenWasym/third-party/z3/build/z3_install/lib \
  -lz3
./fib
```

The final stack contains `144`.

The `lib/` directory is private to the launcher. The `include/` directory
contains the C++ runtime headers.

Set `GENWASYM_JAVA_OPTS` to override the launcher's default JVM options.
