package gensym.wasm

import org.scalatest.FunSuite

import lms.core.stub.Adapter

import gensym.wasm.parser._
import gensym.wasm.miniwasm._

class TestStagedEval extends FunSuite {
  def testFile(filename: String, main: Option[String] = None) = {
    val moduleInst = ModuleInstance(Parser.parseFile(filename))
    val code = PartialEvaluator(moduleInst, None)
    println(code)
  }

  test("push-drop") {
    testFile("./benchmarks/wasm/staged/push-drop.wat")
  }
}
