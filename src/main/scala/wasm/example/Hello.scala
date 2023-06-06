package gensym.wasm.example

import gensym.wasm.ast._
import gensym.wasm.values._
import gensym.wasm.source._
import gensym.wasm.eval._
import gensym.wasm.parser._
import gensym.wasm.memory._

object SimpleTest extends App {
  def basicTest() = {
    val instrs = List(
      Const(I32(1)),
      Const(I32(5)),
      Binary(BinOp.Int(Add)),
    )
    .map(Plain(_))

    val moduleInst = ModuleInstance(List(), List())
    val config = Config(
      Frame(moduleInst, List()),
      Code(List(), instrs),
      1000
    )

    println(config.eval.code.stack)
  }

  def fileTest() = {
    val module = Parser.parseFile("./benchmarks/wasm/test.wat")
    println(module)

    val instrs = module.definitions.find({
      case FuncDef(Some("$real_main"), FuncBodyDef(_, _, _, _)) => true
      case _ => false
    }).get.asInstanceOf[FuncBodyDef].body
    .map(Plain(_))
    .toList

    // val types = module.value.definitions.collect({
    //   case Phrase(tipe@TypeDef(_, _)) => tipe
    // })
    val types = List()
    val funcs = module.definitions.collect({
      case FuncDef(_, fndef@FuncBodyDef(_, _, _, _)) => fndef
    }).toList

    val moduleInst = ModuleInstance(types, funcs)
    val config = Config(
      Frame(moduleInst, List()),
      Code(List(), instrs),
      1000
    )

    println(config.eval.code.stack)
  }

  //basicTest()
  //println("====")
  fileTest()
}
