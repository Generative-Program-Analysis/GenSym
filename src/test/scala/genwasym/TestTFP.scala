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

import java.io.{ByteArrayOutputStream, StringReader}
import org.scalatest.FunSuite

class TestTFP extends FunSuite {
  abstract class ExpResult
  case class ExpInt(i: Int) extends ExpResult
  case class ExpStack(stack: List[Value]) extends ExpResult
  case object Ignore extends ExpResult

  implicit def toI32V(i: Int): Value = I32V(i)

  def testFile(filename: String, main: Option[String] = None, expected: ExpResult = Ignore) = {
    val module = Parser.parseFile(filename)
    // println(module)
    val evaluator = EvaluatorTFP(ModuleInstance(module))
    type Cont = evaluator.Cont[Unit]
    type MCont = evaluator.MCont[Unit]
    val haltK: Cont = evaluator.initK
    val haltMK: MCont = (stack) => {
      // println(s"halt cont: $stack")
      expected match {
        case ExpInt(e)   => assert(stack(0) == I32V(e))
        case ExpStack(e) => assert(stack == e)
        case Ignore      => ()
      }
    }
    evaluator.evalTop(haltK, haltMK, main)
  }

  // So far it assumes that the output is multi-line integers
  def testFileOutput(filename: String, exp: List[Int], main: Option[String] = None) = {
    val out = new ByteArrayOutputStream()
    Console.withOut(out) {
      testFile(filename, main)
    }
    assert(out.toString.split("\n").toList.map(_.toInt) == exp)
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

  // `for`` is a syntactic construct only introduced for TFP
  test("for loop") {
    testFile("./benchmarks/wasm/for_loop.wat", Some("for_loop"), ExpInt(55))
  }

  // New effect handler tests:
  test("call_ref") {
    testFile("./benchmarks/wasm/wasmfx/callref-strip.wast")
  }

  test("try-catch") {
    testFileOutput("./benchmarks/wasm/trycatch/try_catch.wat", List(1, 2, 3, 4, 5))
  }

  test("try-catch-succ") {
    // no exception was thrown
    testFileOutput("./benchmarks/wasm/trycatch/try_catch_succ.wat", List(1, 3, 5))
  }

  test("try-catch-discard") {
    // discard the resumption in the catch block
    testFileOutput("./benchmarks/wasm/trycatch/try_catch_discard.wat", List(1, 42, 4, 5))
  }

  test("nested-try-catch") {
    testFileOutput("./benchmarks/wasm/trycatch/nested_try_catch.wat", List(1, 2, 3, 4, 5, 6, 7, 8, 9))
  }

  test("try-catch-multishot") {
    testFileOutput("./benchmarks/wasm/trycatch/multishot.wat", List(1, 2, 3, 4, 3, 5))
  }

  test("try-catch-deep-handler") {
    testFileOutput("./benchmarks/wasm/trycatch/deep.wat", List(1, 2, 3, 2, 4, 4, 5))
  }

  test("try-catch-block") {
    testFileOutput("./benchmarks/wasm/trycatch/try_catch_block.wat", List(1, 2, 3, 4, 5))
  }

  test("try-catch-br2") {
    testFileOutput("./benchmarks/wasm/trycatch/try_catch_br2.wat", List(1, 2, 6, 4, 5))
  }

  test("try-catch-br") {
    // break out of try block is not allowed
    assertThrows[IndexOutOfBoundsException] {
      testFileOutput("./benchmarks/wasm/trycatch/try_catch_br.wat", List(1, 2, 6))
    }
  }

  test("try-catch-throw-twice") {
    testFileOutput("./benchmarks/wasm/trycatch/throw_twice.wat", List(1, 2, 6, 2, 3, 4, 4, 5))
  }

  test("try-catch-throw-twice2") {
    testFileOutput("./benchmarks/wasm/trycatch/throw_twice2.wat", List(1, 2, 6, 2, 3, 4, 4, 5))
  }

  test("try-catch-br3") {
    testFileOutput("./benchmarks/wasm/trycatch/try_catch_br3.wat", List(1, 2, 3, 4, 5))
  }

  test("try-catch-br4") {
    testFileOutput("./benchmarks/wasm/trycatch/try_catch_br4.wat", List(1, 2, 6, 2, 7, 4, 4, 5))
  }

  test("try-catch-catch-br") {
    testFileOutput("./benchmarks/wasm/trycatch/try_catch_catch_br.wat", List(1, 2, 6, 4, 6, 5))
  }

  // SpecTest
  test("spectest_return_call") {
    testWastFile("./benchmarks/wasm/spectest/return_call.bin.wast")
  }

  // one of the failing test cases
  test("count") {
    testFile("./benchmarks/wasm/count.wat", None, ExpInt(0))
  }

}
