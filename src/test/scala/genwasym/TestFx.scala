package gensym.wasm

import gensym.wasm.parser.Parser
import gensym.wasm.miniwasmscript.ScriptRunner

import gensym.wasm.ast._
import gensym.wasm.source._
import gensym.wasm.parser._
import gensym.wasm.memory._
import gensym.wasm.symbolic._
import gensym.wasm.miniwasm._
import collection.mutable.ArrayBuffer


import org.scalatest.FunSuite

class TestFx extends FunSuite {
  abstract class ExpResult
  case class ExpInt(i: Int) extends ExpResult
  case class ExpStack(stack: List[Value]) extends ExpResult
  case object Ignore extends ExpResult

  implicit def toI32V(i: Int): Value = I32V(i)

  def testFile(filename: String, main: Option[String] = None, expected: ExpResult = Ignore) = {
    val module = Parser.parseFile(filename)
    println(module)
    val evaluator = EvaluatorFX(ModuleInstance(module))
    type Cont = evaluator.Cont[Unit]
    type MCont = evaluator.MCont[Unit]
    val haltK: Cont = (stack, m) => m(stack)
    val haltMK: MCont = (stack) => {
      println(s"halt cont: $stack")
      expected match {
        case ExpInt(e) => assert(stack(0) == I32V(e))
        case ExpStack(e) => assert(stack == e)
        case Ignore    => ()
      }
    }
    evaluator.evalTop(haltK, haltMK, main)
  }

  def testWastFile(filename: String): Unit = {
    val script = Parser.parseScriptFile(filename).get
    val runner = new ScriptRunner()
    runner.run(script)
  }

  // non-effect tests should still pass:
  test("ack") { testFile("./benchmarks/wasm/ack.wat", Some("real_main"), ExpInt(7)) }
  test("power") { testFile("./benchmarks/wasm/pow.wat", Some("real_main"), ExpInt(1024)) }
  test("start") { testFile("./benchmarks/wasm/start.wat") }
  test("fact") { testFile("./benchmarks/wasm/fact.wat", None, ExpInt(120)) }
  test("loop") { testFile("./benchmarks/wasm/loop.wat", None, ExpInt(10)) }
  test("even-odd") { testFile("./benchmarks/wasm/even_odd.wat", None, ExpInt(1)) }
  test("load") { testFile("./benchmarks/wasm/load.wat", None, ExpInt(1)) }
  test("btree") { testFile("./benchmarks/wasm/btree/2o1u-unlabeled.wat") }
  test("fib") { testFile("./benchmarks/wasm/fib.wat", None, ExpInt(144)) }
  test("tribonacci") { testFile("./benchmarks/wasm/tribonacci.wat", None, ExpInt(504)) }

  test("return") {
    intercept[gensym.wasm.miniwasm.Trap] {
      testFile("./benchmarks/wasm/return.wat", Some("$real_main"))
    }
  }
  test("return_call") {
    testFile("./benchmarks/wasm/sum.wat", Some("sum10"), ExpInt(55))
  }

  test("block input") {
    testFile("./benchmarks/wasm/block.wat", Some("real_main"), ExpInt(9))
  }
  test("loop block input") {
    testFile("./benchmarks/wasm/block.wat", Some("test_loop_input"), ExpInt(55))
  }
  test("if block input") {
    testFile("./benchmarks/wasm/block.wat", Some("test_if_input"), ExpInt(25))
  }
  test("block input - poly br") {
    testFile("./benchmarks/wasm/block.wat", Some("test_poly_br"), ExpInt(0))
  }
  test("loop block - poly br") {
    testFile("./benchmarks/wasm/loop_poly.wat", None, ExpStack(List(2, 1)))
  }

  // New effect handler tests:

  // test("simple script") {
  //   TestWastFile("./benchmarks/wasm/wasmfx/cont_bind3.bin.wast")
  // }

  test("call_ref") {
    testFile("./benchmarks/wasm/wasmfx/callref-strip.wast")
  }

  test("try-catch") {
    // expect output: 1 2 3 4 5
    testFile("./benchmarks/wasm/trycatch/try_catch.wat")
  }

  test("try-catch-block") {
    // expect output: 1 2 3 4 5
    testFile("./benchmarks/wasm/trycatch/try_catch_block.wat")
  }

  test("try-catch-succ") {
    // no exception was thrown
    // expect output: 1 3 5
    testFile("./benchmarks/wasm/trycatch/try_catch_succ.wat")
  }

  test("try-catch-discard") {
    // discard the resumption in the catch block
    // expect output: 1 42 4 5
    testFile("./benchmarks/wasm/trycatch/try_catch_discard.wat")
  }

  test("nested-try-catch") {
    // expect output: 1 2 3 4 5 6 7 8 9
    testFile("./benchmarks/wasm/trycatch/nested_try_catch.wat")
  }

}
