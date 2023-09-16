package gensym.wasm.test

import gensym.wasm.ast._
import gensym.wasm.source._
import gensym.wasm.parser._
import gensym.wasm.memory._

object ConcolicWasmTest {
  def fileTestConcolicEval(file: String, mainFun: String) = {
    import gensym.wasm.concolicminiwasm._
    import collection.mutable.ArrayBuffer
    val module = Parser.parseFile(file)
    println(module)

    val types = List()
    val funcs = module.definitions.collect({
      case FuncDef(_, fndef@FuncBodyDef(_, _, _, _)) => fndef
    }).toList
    val funcNames: List[String] = module.definitions.collect({
      case FuncDef(name, _) => name.get // when can function names be None?
    })
    val real_main = funcNames.indexOf(mainFun)

    val moduleInst = ModuleInstance(types, funcs)

    Evaluator.eval(
      List(Call(real_main)),
      List(), 
      List(), 
      Frame(moduleInst, ArrayBuffer(I32V(0)), ArrayBuffer(Concrete(I32V(0)))),
      (newStack, newSymStack, pathCnds) => {
        println(s"retCont: $newStack")
        println(s"symStack: $newSymStack")
        println(s"pathCnds: $pathCnds")
      },
      List((newStack, _, _) => println(s"trail: $newStack"))
    )(List())
  }

  def main(args: Array[String]) = {
    fileTestConcolicEval("./benchmarks/wasm/test.wat", "$real_main")
    fileTestConcolicEval(
      "./benchmarks/wasm/Collections-C/_build/for-wasp/normal/array/array_test_add.wat",
      "$__original_main"
    )
  }
}
