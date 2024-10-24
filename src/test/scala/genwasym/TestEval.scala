// TODO: maybe rename this to genwasym?
// need to rewrite everything in src/main/scala/wasm tho

package gensym.wasm

import gensym.wasm.ast._
import gensym.wasm.source._
import gensym.wasm.parser._
import gensym.wasm.memory._
import gensym.wasm.symbolic._
import gensym.wasm.miniwasm._
import collection.mutable.ArrayBuffer

import org.scalatest.FunSuite

class TestEval extends FunSuite {
  abstract class ExpResult
  case class ExpInt(i: Int) extends ExpResult
  case class ExpStack(stack: List[Value]) extends ExpResult
  case object Ignore extends ExpResult

  implicit def toI32V(i: Int): Value = I32V(i)

  def testFile(filename: String, main: Option[String] = None, expected: ExpResult = Ignore) = {
    val module = Parser.parseFile(filename)
    //println(module)
    val haltK: Evaluator.Cont[Unit] = stack => {
      println(s"halt cont: $stack")
      expected match {
        case ExpInt(e) => assert(stack(0) == I32V(e))
        case ExpStack(e) => assert(stack == e)
        case Ignore    => ()
      }
    }
    Evaluator.evalTop(module, haltK, main)
  }

  // TODO: the power test can be used to test the stack
  // For now: 2^10 works, 2^100 results in 0 (TODO: why?),
  // and 2^1000 results in a stack overflow
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

  // FIXME:
  //test("tribonacci-ret") { testFile("./benchmarks/wasm/tribonacci_ret.wat", None, Some(504)) }

  // TODO: add wasm spec tests? How to utilize wast files?
}
