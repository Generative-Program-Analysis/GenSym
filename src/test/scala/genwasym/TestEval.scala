// TODO: maybe rename this to genwasym?
// need to rewrite everything in src/main/scala/wasm tho

package gensym.wasm

import gensym.wasm.ast._
import gensym.wasm.source._
import gensym.wasm.parser._
import gensym.wasm.memory._
import gensym.wasm.symbolic._

import org.scalatest.FunSuite

class TestEval extends FunSuite {

  // Mostly testing the files generated form `benchmarks/wasm/test.rs`
  def testfile(filename: String) = {
    import gensym.wasm.miniwasm._
    import collection.mutable.ArrayBuffer
    val module = Parser.parseFile(filename)

    val instrs = module.definitions
      .find({
        case FuncDef(Some("$real_main"), FuncBodyDef(_, _, _, _)) => true
        case _                                                    => false
      })
      .map({
        case FuncDef(_, FuncBodyDef(_, _, _, body)) => body
      })
      .get
      .toList

    val types = List()
    val funcs = module.definitions
      .collect({
        case FuncDef(_, fndef @ FuncBodyDef(_, _, _, _)) => fndef
      })
      .toList

    val globals = module.definitions
      .collect({
        case Global(_, GlobalValue(ty, e)) =>
          (e.head) match {
            case Const(c) => RTGlobal(ty, c)
            case _        => ???
          }
      })
      .toList

    val moduleInst = ModuleInstance(types, funcs, List(RTMemory()), globals)

    Evaluator.eval(instrs,
                   List(),
                   Frame(moduleInst, ArrayBuffer(I32V(0))),
                   newStack => println(s"retCont: $newStack"),
                   List(newStack => println(s"trail: $newStack")))

  }

  // Mostly testing the files generated form `benchmarks/wasm/test.rs`
  def test_btree(filename: String) = {
    import gensym.wasm.miniwasm._
    import collection.mutable.ArrayBuffer
    val module = Parser.parseFile(filename)

    val instrs = module.definitions
      .find({
        case FuncDef(Some("$real_main"), FuncBodyDef(_, _, _, _)) => true
        case _                                                    => false
      })
      .map({
        case FuncDef(_, FuncBodyDef(_, _, _, body)) => body
      })
      .get
      .toList

    val types = List()
    val funcs = module.definitions
      .collect({
        case FuncDef(_, fndef @ FuncBodyDef(_, _, _, _)) => fndef
      })
      .toList

    val globals = module.definitions
      .collect({
        case Global(_, GlobalValue(ty, e)) =>
          (e.head) match {
            case Const(c) => RTGlobal(ty, c)
            // Q: What is the default behavior if case in non exhaustive
            case _ => ???
          }
      })
      .toList

    // TODO: correct the behavior for memory
    val memory = module.definitions
      .collect({
        case Memory(id, MemoryType(a, b)) =>
          new RTMemory(RTMemoryType(a, b), ArrayBuffer[Byte]())
      })
      .toList

    val moduleInst = ModuleInstance(types, funcs, List(RTMemory()), globals)

    Evaluator.eval(instrs,
                   List(),
                   Frame(moduleInst, ArrayBuffer(I32V(0))),
                   newStack => println(s"retCont: $newStack"),
                   List(newStack => println(s"trail: $newStack")))

  }

  // test("ackermann") { testfile("./benchmarks/wasm/test_ack.wat") }
  // TODO: the power test can be used to test the stack
  // For now: 2^10 works, 2^100 results in 0 (why?),
  // and 2^1000 results in a stack overflow
  // test("power") { testfile("./benchmarks/wasm/test_pow.wat") }

  // TODO: fix this, this should fail at unreachable!
  test("btree") { test_btree("./benchmarks/wasm/btree/2o1u-no-label.wat") }

}
