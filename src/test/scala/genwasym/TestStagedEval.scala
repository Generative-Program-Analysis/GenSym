package gensym.wasm

import org.scalatest.FunSuite

import lms.core.stub.Adapter

import gensym.wasm.parser._
import gensym.wasm.miniwasm._

class TestStagedEval extends FunSuite {
  def testFile(filename: String, main: Option[String] = None, printRes: Boolean = false) = {
    val moduleInst = ModuleInstance(Parser.parseFile(filename))
    val code = PartialEvaluator(moduleInst, main, true)
    println(code)
  }

  /*
  test("scratch") {
    testFile("./benchmarks/wasm/staged/scratch.wat")
  }

  test("ack") { testFile("./benchmarks/wasm/ack.wat", Some("real_main"), printRes = true) }
  */

  test("brtable") {
    testFile("./benchmarks/wasm/staged/brtable.wat")
  }
}
