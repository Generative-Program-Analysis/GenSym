package gensym.wasm

import org.scalatest.FunSuite

import lms.core.stub.Adapter

import gensym.wasm.parser._
import gensym.wasm.miniwasm._

class TestStagedEval extends FunSuite {
  def testFileToScala(filename: String, main: Option[String] = None, printRes: Boolean = false) = {
    val moduleInst = ModuleInstance(Parser.parseFile(filename))
    val code = WasmToScalaCompiler(moduleInst, main, true)
    println(code)
  }

  test("ack-scala") { testFileToScala("./benchmarks/wasm/ack.wat", Some("real_main"), printRes = true) }

  test("brtable-scala") {
    testFileToScala("./benchmarks/wasm/staged/brtable.wat")
  }

  test("drop-scala") {
    testFileToScala("./benchmarks/wasm/staged/pop.wat")
  }

  def testFileToCpp(filename: String, main: Option[String] = None, printRes: Boolean = false) = {
    val moduleInst = ModuleInstance(Parser.parseFile(filename))
    val code = WasmToCppCompiler(moduleInst, main, true)
    println(code)
  }

  test("ack-cpp") { testFileToCpp("./benchmarks/wasm/ack.wat", Some("real_main"), printRes = true) }

  test("brtable-cpp") {
    testFileToCpp("./benchmarks/wasm/staged/brtable.wat")
  }

}
