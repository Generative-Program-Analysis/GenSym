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
  def testFile(filename: String, main: Option[String] = None, expected: Option[Int] = None) = {
    val module = Parser.parseFile(filename)
    //println(module)
    val haltK: Evaluator.Cont[Unit] = stack => {
      println(s"halt cont: $stack")
      expected match {
        case Some(e) => assert(stack(0) == I32V(e))
        case None    => ()
      }
    }
    Evaluator.evalTop(module, haltK, main)
  }

  // TODO: the power test can be used to test the stack
  // For now: 2^10 works, 2^100 results in 0 (TODO: why?),
  // and 2^1000 results in a stack overflow
  test("ack") { testFile("./benchmarks/wasm/ack.wat", Some("real_main"), Some(7)) }
  test("power") { testFile("./benchmarks/wasm/pow.wat", Some("real_main"), Some(1024)) }
  test("start") { testFile("./benchmarks/wasm/start.wat") }
  test("fact") { testFile("./benchmarks/wasm/fact.wat", None, Some(120)) }
  test("loop") { testFile("./benchmarks/wasm/loop.wat", None, Some(10)) }
  test("even-odd") { testFile("./benchmarks/wasm/even_odd.wat", None, Some(1)) }
  test("load") { testFile("./benchmarks/wasm/load.wat", None, Some(1)) }
  test("btree") { testFile("./benchmarks/wasm/btree/2o1u-unlabeled.wat") }
  test("fib") { testFile("./benchmarks/wasm/fib.wat", None, Some(144)) }
  test("tribonacci") { testFile("./benchmarks/wasm/tribonacci.wat", None, Some(504)) }

  test("return") {
    intercept[gensym.wasm.miniwasm.Trap] {
      testFile("./benchmarks/wasm/return.wat", Some("$real_main"), None)
    }
  }

  // FIXME:
  //test("tribonacci-ret") { testFile("./benchmarks/wasm/tribonacci_ret.wat", None, Some(504)) }

  // TODO: add wasm spec tests? How to utilize wast files?
}
