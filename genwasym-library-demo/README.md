# GenWasym Library Demo

This independent Scala 2 project uses GenWasym as a library and calls
`GenWasym.compileFile` directly. In the [artifact](https://doi.org/10.5281/zenodo.21517229), it is installed alongside the
GenWasym source tree:

```text
/ae/GenWasym
/ae/genwasym-library-demo
```

The project `genwasym-library-demo` includes a simple demo program
that calls GenWasym API to compile input Wasm program.


Run the demo with the existing Fibonacci benchmark:

```bash
mkdir -p /ae/genwasym-library-demo/lib
cp /ae/GenWasym/dist/genwasym/lib/genwasym.jar \
  /ae/genwasym-library-demo/lib/genwasym.jar
cd /ae/genwasym-library-demo
sbt "run /ae/GenWasym/benchmarks/wasm/fib.wat /ae/genwasym-library-demo/target/fib.wat.cpp"
```

The generated C++ is written to
`/ae/genwasym-library-demo/target/fib.wat.cpp`. sbt automatically includes the
copied JAR because it is in the project's standard `lib/` directory.
