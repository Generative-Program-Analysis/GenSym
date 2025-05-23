package gensym.wasm

import org.scalatest.FunSuite

import lms.core.stub.Adapter

import gensym.wasm.parser._
import gensym.wasm.miniwasm._

class TestStagedEval extends FunSuite {
  def testFileToScala(filename: String, main: Option[String] = None, printRes: Boolean = false) = {
    val moduleInst = ModuleInstance(Parser.parseFile(filename))
    val code = WasmToScalaCompiler.compile(moduleInst, main, true)
    println(code)
  }

  test("ack-scala") { testFileToScala("./benchmarks/wasm/ack.wat", Some("real_main"), printRes = true) }

  test("brtable-scala") {
    testFileToScala("./benchmarks/wasm/staged/brtable.wat")
  }

  test("drop-scala") {
    testFileToScala("./benchmarks/wasm/staged/pop.wat")
  }

  def testFileToCpp(filename: String, main: Option[String] = None, expect: Option[List[Float]]=None) = {
    val moduleInst = ModuleInstance(Parser.parseFile(filename))
    val code = WasmToCppCompiler.compile(moduleInst, main, true)

    val cppFile = s"$filename.cpp"

    val writer = new java.io.PrintWriter(new java.io.File(cppFile))
    try {
      writer.write(code)
    } finally {
      writer.close()
    }
    import sys.process._

    val exe = s"$cppFile.exe"
    val command = s"g++ -o $exe $cppFile"

    if (command.! != 0) {
      throw new RuntimeException(s"Compilation failed for $cppFile")
    }

    val result = s"./$exe".!!
    println(result)

    expect.map(vs => {
      val stackValues = result
        .split("Stack contents: \n")(1)
        .split("\n")
        .map(_.toFloat)
        .toList
      assert(vs == stackValues)
    })
  }

  test("ack-cpp") { testFileToCpp("./benchmarks/wasm/ack.wat", Some("real_main"), expect=Some(List(7))) }
  test("power") { testFileToCpp("./benchmarks/wasm/pow.wat", Some("real_main"), expect=Some(List(1024))) }
  test("start") { testFileToCpp("./benchmarks/wasm/start.wat") }
  test("fact") { testFileToCpp("./benchmarks/wasm/fact.wat", None, expect=Some(List(120))) }
  test("loop") { testFileToCpp("./benchmarks/wasm/loop.wat", None, expect=Some(List(10))) }
  test("even-odd") { testFileToCpp("./benchmarks/wasm/even_odd.wat", None, expect=Some(List(1))) }
  test("load") { testFileToCpp("./benchmarks/wasm/load.wat", None, expect=Some(List(1))) }
  // TODO: this case will fail because of some undefined variables
  test("btree") { testFileToCpp("./benchmarks/wasm/btree/2o1u-unlabeled.wat") }
  test("fib") { testFileToCpp("./benchmarks/wasm/fib.wat", None, expect=Some(List(144))) }
  test("tribonacci") { testFileToCpp("./benchmarks/wasm/tribonacci.wat", None, expect=Some(List(504))) }

  test("return") {
    intercept[java.lang.RuntimeException] {
      testFileToCpp("./benchmarks/wasm/return.wat", Some("$real_main"))
    }
  }
  test("return_call") {
    testFileToCpp("./benchmarks/wasm/sum.wat", Some("sum10"), expect=Some(List(55)))
  }

  test("block input") {
    testFileToCpp("./benchmarks/wasm/block.wat", Some("real_main"), expect=Some(List(9)))
  }
  test("loop block input") {
    testFileToCpp("./benchmarks/wasm/block.wat", Some("test_loop_input"), expect=Some(List(55)))
  }
  test("if block input") {
    testFileToCpp("./benchmarks/wasm/block.wat", Some("test_if_input"), expect=Some(List(25)))
  }
  test("block input - poly br") {
    testFileToCpp("./benchmarks/wasm/block.wat", Some("test_poly_br"), expect=Some(List(0)))
  }
  test("loop block - poly br") {
    testFileToCpp("./benchmarks/wasm/loop_poly.wat", None, expect=Some(List(2, 1)))
  }

  test("brtable-cpp") {
    testFileToCpp("./benchmarks/wasm/staged/brtable.wat")
  }

}

object Benchmark extends App {

  def bench(f: => Unit): Double = {
    import gensym.utils.Utils._
    // run a function f 20 times and return the average time taken
    val times = for (i <- 1 to 20) yield {
      time(f)._2
    }
    times.sum / times.size.toDouble
  }

  def benchmarkWasmInterpreter(filePath: String, main: Option[String] = None): Double = {
    val moduleInst = ModuleInstance(Parser.parseFile(filePath))
    val evaluator = Evaluator(moduleInst)
    val haltK: evaluator.Cont[Unit] = stack => ()
    bench { evaluator.evalTop(haltK, main) }
  }

  def benchmarkWasmToCpp(filePath: String, main: Option[String] = None): Double = {
    val moduleInst = ModuleInstance(Parser.parseFile(filePath))
    val code = WasmToCppCompiler.compile(moduleInst, main, false)

    val cppFile = s"$filePath.cpp"

    val writer = new java.io.PrintWriter(new java.io.File(cppFile))
    try {
      writer.write(code)
    } finally {
      writer.close()
    }
    import sys.process._

    val exe = s"$cppFile.exe"
    // use -O0 optimization to more accurately inspect the interpretation overhead that we reduced by compilation
    val command = s"g++ -o $exe $cppFile -O0"

    if (command.! != 0) {
      throw new RuntimeException(s"Compilation failed for $cppFile")
    }

    println(s"Running $exe")
    bench { s"./$exe".! }
  }

  case class BenchmarkResult(filePath: String, interpretExecutionTime: Double, compiledExecutionTime: Double)

  def benchmarkFile(filePath: String, main: Option[String] = None): Unit = {
    val interpretExecutionTime = benchmarkWasmInterpreter(filePath, main)
    val compiledExecutionTime = benchmarkWasmToCpp(filePath, main)
    val result = BenchmarkResult(filePath, interpretExecutionTime, compiledExecutionTime)
    println(s"Benchmark result for $filePath:")
    println(s"  Average interpreter execution time: $interpretExecutionTime ms")
    println(s"  Average compiled execution time: $compiledExecutionTime ms")
    println(s"  Speedup: ${interpretExecutionTime / compiledExecutionTime}x")
    println()
  }

  override def main(args: Array[String]): Unit = {
    benchmarkFile("./benchmarks/wasm/performance/ack.wat", Some("real_main"))
    benchmarkFile("./benchmarks/wasm/performance/pow.wat", Some("real_main"))
  }
}
