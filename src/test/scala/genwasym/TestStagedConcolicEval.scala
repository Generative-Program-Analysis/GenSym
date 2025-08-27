package gensym.wasm

import org.scalatest.FunSuite

import lms.core.stub.Adapter

import gensym.wasm.miniwasm.{ModuleInstance}
import gensym.wasm.parser._
import gensym.wasm.stagedconcolicminiwasm._

class TestStagedConcolicEval extends FunSuite {
  def testFileConcolicCpp(filename: String, main: Option[String] = None) = {
    val moduleInst = ModuleInstance(Parser.parseFile(filename))
    val cppFile = s"$filename.cpp"
    val exe = s"$cppFile.exe"
    val exploreTreeFile = s"$filename.tree.dot"
    WasmToCppCompiler.compileToExe(moduleInst, main, cppFile, exe, true)

    import sys.process._
    val result = Process(s"./$exe", None, "TREE_FILE" -> exploreTreeFile).!!
    println(result)
  }

  // only test concrete execution and its result
  def testFileConcreteCpp(filename: String, main: Option[String] = None, expect: Option[List[Float]] = None) = {
    val moduleInst = ModuleInstance(Parser.parseFile(filename))
    val cppFile = s"$filename.cpp"
    val exe = s"$cppFile.exe"
    WasmToCppCompiler.compileToExe(moduleInst, main, cppFile, exe, true, "NO_INFO", "RUN_ONCE")

    import sys.process._
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

  test("ack-cpp") { testFileConcolicCpp("./benchmarks/wasm/ack.wat", Some("real_main")) }

  test("bug-finding") {
    testFileConcolicCpp("./benchmarks/wasm/branch-strip-buggy.wat", Some("real_main"))
  }

  test("brtable-bug-finding") {
    testFileConcolicCpp("./benchmarks/wasm/staged/brtable_concolic.wat")
  }

  test("return-poly - concrete") {
    testFileConcreteCpp("./benchmarks/wasm/staged/return_poly.wat", Some("$real_main"), expect=Some(List(42)))
  }
  test("ack-cpp - concrete") { testFileConcreteCpp("./benchmarks/wasm/ack.wat", Some("real_main"), expect=Some(List(7))) }
  test("power - concrete") { testFileConcreteCpp("./benchmarks/wasm/pow.wat", Some("real_main"), expect=Some(List(1024))) }
  test("start - concrete") { testFileConcreteCpp("./benchmarks/wasm/start.wat") }
  test("fact - concrete") { testFileConcreteCpp("./benchmarks/wasm/fact.wat", None, expect=Some(List(120))) }
  // TODO: Waiting more symbolic operators' implementations
  // test("loop - concrete") { testFileConcreteCpp("./benchmarks/wasm/loop.wat", None, expect=Some(List(10))) }
  test("even-odd - concrete") { testFileConcreteCpp("./benchmarks/wasm/even_odd.wat", None, expect=Some(List(1))) }
  // TODO: Waiting symbolic memory's implementations
  // test("load - concrete") { testFileConcreteCpp("./benchmarks/wasm/load.wat", None, expect=Some(List(1))) }
  // test("btree - concrete") { testFileConcreteCpp("./benchmarks/wasm/btree/2o1u-unlabeled.wat") }
  test("fib - concrete") { testFileConcreteCpp("./benchmarks/wasm/fib.wat", None, expect=Some(List(144))) }
  test("tribonacci - concrete") { testFileConcreteCpp("./benchmarks/wasm/tribonacci.wat", None, expect=Some(List(504))) }

  // test("return - concrete") {
  //   Since all of the thrown exceptions had been captured in concolic driver, this test is not valid anymore
  //   intercept[java.lang.RuntimeException] {
  //     testFileConcreteCpp("./benchmarks/wasm/return.wat", Some("$real_main"))
  //   }
  // }

  test("return_call - concrete") {
    testFileConcreteCpp("./benchmarks/wasm/sum.wat", Some("sum10"), expect=Some(List(55)))
  }

  test("block input - concrete") {
    testFileConcreteCpp("./benchmarks/wasm/block.wat", Some("real_main"), expect=Some(List(9)))
  }
  test("loop block input - concrete") {
    testFileConcreteCpp("./benchmarks/wasm/block.wat", Some("test_loop_input"), expect=Some(List(55)))
  }
  test("if block input - concrete") {
    testFileConcreteCpp("./benchmarks/wasm/block.wat", Some("test_if_input"), expect=Some(List(25)))
  }
  test("block input - poly br - concrete") {
    testFileConcreteCpp("./benchmarks/wasm/block.wat", Some("test_poly_br"), expect=Some(List(0)))
  }
  test("loop block - poly br - concrete") {
    testFileConcreteCpp("./benchmarks/wasm/loop_poly.wat", None, expect=Some(List(2, 1)))
  }

  test("brtable-cpp - concrete") {
    testFileConcreteCpp("./benchmarks/wasm/staged/brtable.wat")
  }

}
