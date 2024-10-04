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

  // Mostly testing the files generated form `benchmarks/wasm/test.rs`
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
  test("ack") { testFile("./benchmarks/wasm/ack.wat", Some("$real_main"), Some(7)) }
  test("power") { testFile("./benchmarks/wasm/pow.wat", Some("$real_main"), Some(1024)) }
  test("start") { testFile("./benchmarks/wasm/start.wat") }
  test("fact") { testFile("./benchmarks/wasm/fact.wat", None, Some(120)) }
  test("loop") { testFile("./benchmarks/wasm/loop.wat", None, Some(10)) }
  test("even-odd") { testFile("./benchmarks/wasm/even_odd.wat", None, Some(1)) }
  test("return") { testFile("./benchmarks/wasm/return.wat", None, None) }

  // Parser works, but the memory issue remains
  // test("btree") { testFile("./benchmarks/wasm/btree/2o1u-no-label-for-real.wat") }

  // TODO: add more wasm spec tests?
  // test("memory") { test_btree("./benchmarks/wasm/spectest/test.wat", "$real_main") }
}
