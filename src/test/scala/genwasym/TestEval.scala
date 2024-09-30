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
  def testFile(filename: String, main: String, expected: Option[Int] = None) = {
    val module = Parser.parseFile(filename)
    println(module)

    val instrs = module.definitions.flatMap({
        case FuncDef(Some(`main`), FuncBodyDef(_, _, _, body)) => body
        case _ => List()
    })

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
    //val retK: Evaluator.RetCont = newStack => println(s"retCont: $newStack")
    val trailK: Evaluator.Cont = newStack => {
      println(s"trail: $newStack")
      expected match {
        case Some(e) => assert(newStack(0) == I32V(e))
        case None    => ()
      }
    }

    Evaluator.eval(instrs, List(), Frame(moduleInst, ArrayBuffer(I32V(0))), 0/*retK*/, List(trailK))
  }

  // XXX (GW): remove this and just using testFile?
  def test_btree(filename: String, main: String) = {
    val module = Parser.parseFile(filename)

    val instrs = module.definitions
      .find({
        case FuncDef(Some(`main`), FuncBodyDef(_, _, _, _)) => true
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
        case Memory(id, MemoryType(min, max_opt)) =>
          RTMemory(min, max_opt)
      })
      .toList

    val moduleInst = ModuleInstance(types, funcs, memory, globals)

    Evaluator.eval(instrs,
                   List(),
                   Frame(moduleInst, ArrayBuffer(I32V(0))),
                   0, //newStack => println(s"retCont: $newStack"),
                   List(newStack => println(s"trail: $newStack")))

  }

  // test("ackermann") { testfile("./benchmarks/wasm/test_ack.wat") }
  // TODO: the power test can be used to test the stack
  // For now: 2^10 works, 2^100 results in 0 (TODO: why?),
  // and 2^1000 results in a stack overflow
  //test("ack") { testFile("./benchmarks/wasm/ack.wat", "$real_main", Some(7)) }
  //test("power") { testFile("./benchmarks/wasm/pow.wat", "$real_main", Some(1024)) }
  test("loop") { testFile("./benchmarks/wasm/loop.wat", "$real_main") }

  //test("btree") { test_btree("./benchmarks/wasm/btree/2o1u-no-label.wat", "$real_main") }

  // TODO: add more wasm spec tests?
  // test("memory") { test_btree("./benchmarks/wasm/spectest/test.wat", "$real_main") }


}
