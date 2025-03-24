package gensym.wasm

import gensym.wasm.ast._
import gensym.wasm.source._
import gensym.wasm.parser._
import gensym.wasm.memory._
import gensym.wasm.symbolic._

import org.scalatest.FunSuite
class TestConcolic extends FunSuite {

  // TODO: actually test this
  def fileTestConcolicEval() = {
    import gensym.wasm.concolicminiwasm._
    val module = Parser.parseFile("./benchmarks/wasm/pow.wat")
    val moduleInst = ModuleInstance(module)
    Evaluator(moduleInst).execWholeProgram(Some("real_main"))
  }

  test("fileTestConcolicEval") {
    fileTestConcolicEval()
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
