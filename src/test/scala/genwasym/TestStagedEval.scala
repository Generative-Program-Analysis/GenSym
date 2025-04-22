package gensym.wasm

import org.scalatest.FunSuite

import lms.core.stub.Adapter

import gensym.wasm.parser._
import gensym.wasm.miniwasm._

class TestStagedEval extends FunSuite {
  def testFile(filename: String, main: Option[String] = None) = {
    val module = Parser.parseFile(filename)
    val partialEvaluator = StagedEvaluator(ModuleInstance(module))
    val block = partialEvaluator.codegen(main)
    println(Adapter.g)
    println(block)
  }

  test("push-drop") {
    testFile("./benchmarks/wasm/staged/push-drop.wat")
  }
}
