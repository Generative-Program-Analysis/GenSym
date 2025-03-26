package gensym.wasm

import gensym.wasm.ast._
import gensym.wasm.source._
import gensym.wasm.parser._
import gensym.wasm.memory._
import gensym.wasm.symbolic._
import gensym.wasm.concolicminiwasm._

import org.scalatest.FunSuite
class TestConcolic extends FunSuite {

  def fileTestConcolicEval(filename: String, mainFnName: Option[String]) = {
    val module = Parser.parseFile(filename)
    val moduleInst = ModuleInstance(module)
    Evaluator(moduleInst).execWholeProgram(mainFnName)
  }

  // TODO: is there a way to test this in a more automatic way?
  //       we currently eyeball the path conditions
  test("pow") {
    fileTestConcolicEval("./benchmarks/wasm/pow.wat", Some("real_main"))
  }

  test("branch") {
    fileTestConcolicEval("./benchmarks/wasm/branch-strip.wat", Some("real_main"))
  }

}

class TestDriver extends FunSuite {
  import gensym.wasm.concolicdriver._
  import scala.collection.mutable.{HashMap, HashSet}
  import z3.scala._

  def fileTestDriver(file: String, mainFun: String, startEnv: HashMap[Int, Value]) = {
    import collection.mutable.ArrayBuffer
    val module = Parser.parseFile(file)
    ConcolicDriver.exec(module, mainFun, startEnv)(new Z3Context())
  }

  // def main(args: Array[String]) = {}

  // TODO: fix this
  test("driver") {
    fileTestDriver("./benchmarks/wasm/branch-strip.wat", "real_main", new HashMap[Int, Value]())
  }

}

// TODO: from: GenSym/src/main/scala/wasm/tests/TestConcolicWasm.scala

// package gensym.wasm.test

// import gensym.wasm.ast._
// import gensym.wasm.source._
// import gensym.wasm.parser._
// import gensym.wasm.memory._
// import gensym.wasm.symbolic._

// object ConcolicWasmTest {
//   def fileTestConcolicEval(file: String, mainFun: String) = {
//     import gensym.wasm.concolicminiwasm._
//     import collection.mutable.ArrayBuffer
//     val module = Parser.parseFile(file)
//     Evaluator.execWholeProgram(module, mainFun)
//   }

//   def main(args: Array[String]) = {
//     fileTestConcolicEval("./benchmarks/wasm/test_basic.wat", "$main")
//     // fileTestConcolicEval("./benchmarks/wasm/unit/loop.wat", "$main")
//     // fileTestConcolicEval("./benchmarks/wasm/unit/br.wat", "$main")
//     // fileTestConcolicEval(
//     //   "./benchmarks/wasm/Collections-C/_build/for-wasp/normal/array/array_test_add.wat",
//     //   "$__original_main"
//     // )
//   }
// }
