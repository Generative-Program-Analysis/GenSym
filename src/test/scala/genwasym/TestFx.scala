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

class TestFx extends FunSuite {
  abstract class ExpResult
  case class ExpInt(i: Int) extends ExpResult
  case class ExpStack(stack: List[Value]) extends ExpResult
  case object Ignore extends ExpResult

  implicit def toI32V(i: Int): Value = I32V(i)

  def testFile(filename: String, main: Option[String] = None, expected: ExpResult = Ignore) = {
    val module = Parser.parseFile(filename)
    // println(module)
    val evaluator = EvaluatorFX(ModuleInstance(module))
    type Cont = evaluator.Cont[Unit]
    type MCont = evaluator.MCont[Unit]
    val haltK: Cont = (stack, trail, _hs) => {
      if (!trail.isEmpty) {
        // TODO: this throw will fail the test, we should redesign our trail composition rather than list append
        System.err.println(s"[Debug]: $trail")
        throw new Exception("Trail is not empty")
      }
      // println(s"halt cont: $stack")
      expected match {
        case ExpInt(e)   => assert(stack(0) == I32V(e))
        case ExpStack(e) => assert(stack == e)
        case Ignore      => ()
      }
    }
    evaluator.evalTop(haltK, main)
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

  // New effect handler tests:
  test("call_ref") {
    testFile("./benchmarks/wasm/wasmfx/callref-strip.wast")
  }

  /* REAL WASMFX STUFF */
  test("cont") {
    // testFile("./benchmarks/wasm/wasmfx/callcont.wast", None, ExpInt(11))
    testWastFile("./benchmarks/wasm/wasmfx/callcont.bin.wast")
  }

  test("resume w/o suspend") {
    testWastFile("./benchmarks/wasm/wasmfx/resume1.bin.wast")
  }

  // wasmfx sec 2.3 like example
  test("test_cont") {
    testFileOutput("./benchmarks/wasm/wasmfx/test_cont-strip.wast", List(10, -1, 11, 11, -1, 12, 12, -1, 13, 13, -1, 14, -2))
  }

  test("resume_chain1") {
    testWastFile("./benchmarks/wasm/wasmfx/resume_chain1-strip.wast")
  }

  // printing 0 not 1
  test("nested suspend") {
    testFileOutput("./benchmarks/wasm/wasmfx/nested_suspend-strip.wat", List(0))
  }

  // going to print 100 to 1 and then print 42
  test("gen") {
    testFileOutput("./benchmarks/wasm/wasmfx/gen-stripped.wast", (100 to 1 by -1).toList ++ List(42))
  }

  test("diff resume") {
    testFileOutput("./benchmarks/wasm/wasmfx/diff_resume-strip.wat", List(10, 11, 42))
  }

  test("cont_bind_4") {
    testWastFile("./benchmarks/wasm/wasmfx/cont_bind4.bin.wast")
  }

  test("cont_bind_5") {
    testWastFile("./benchmarks/wasm/wasmfx/cont_bind5.bin.wast")
  }

  test("diff_handler") {
    testFileOutput("./benchmarks/wasm/wasmfx/diff_handler.wast", List(0, 1))
  }

  test("nested_resume") {
    testFileOutput("./benchmarks/wasm/wasmfx/nested_resume-strip.wat", List(0, 111, 222, 333, 444, 555))
  }


  test("nested") {
    testFileOutput("./benchmarks/wasm/wasmfx/nested-strip.wat", List(0, 0, 111, 222, 333, 444, 555, 666))
  }

  test("suspend16") {
    testWastFile("./benchmarks/wasm/wasmfx/suspend16.bin.wast")
    // testFile("./benchmarks/wasm/wasmfx/suspend16-strip.wast")
  }
  
  test("fun-state") {
    testWastFile("./benchmarks/wasm/wasmfx/fun-state.bin.wast")
  }

  // having -1 printing from (nats generator) 0 to 9
  // and -2 summing up the the nats generated
  test("pipes") {
    testWastFile("./benchmarks/wasm/wasmfx/fun-pipes.bin.wast")
  }
  
}
