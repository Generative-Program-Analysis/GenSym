package genwasym

import genwasym.ast._
import genwasym.source._
import genwasym.parser._
import genwasym.memory._
import genwasym.symbolic._
import genwasym.concolicminiwasm._

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
  import genwasym.concolicdriver._
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
    fileTestDriver("./benchmarks/wasm/branch-strip.wat", "real_main", HashMap())
    fileTestDriver("./benchmarks/wasm/branch-strip1.wat", "real_main", HashMap())
  }

  test("bug-finding") {
    fileTestDriver("./benchmarks/wasm/branch-strip-buggy.wat", "real_main", HashMap())
  }

}

// Legacy tests from TestConcolicWasm.scala.

// package genwasym.test

// import genwasym.ast._
// import genwasym.source._
// import genwasym.parser._
// import genwasym.memory._
// import genwasym.symbolic._

// object ConcolicWasmTest {
//   def fileTestConcolicEval(file: String, mainFun: String) = {
//     import genwasym.concolicminiwasm._
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
