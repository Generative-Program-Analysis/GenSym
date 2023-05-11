package wasm.example

import wasm.ast._
import wasm.types._
import wasm.values._
import wasm.source._
import wasm.eval._
import wasm.parser._
import wasm.memory._

import scala.collection.mutable.ArrayBuffer

object SimpleTest extends App {
  def basicTest() = {
    val instrs = List(
      Const(I32(1)),
      Const(I32(5)),
      Binary(BinOp.Int(Add)),
    )
    .map(Plain(_))

    val moduleInst = ModuleInstance(List(), List(), List(Memory(ArrayBuffer())))
    val config = Config(
      Frame(moduleInst, List()),
      Code(List(), instrs),
      1000
    )

    println(config.eval.code.stack)
  }

  def fileTest() = {
    val code = scala.io.Source.fromFile("./benchmarks/wasm/test.wat").mkString
    val module = Parser.parseString(code)
    println(module)

    val instrs = module.definitions.find({
      case FuncDef("$real_main", _, _, _) => true
      case _ => false
    }).get.asInstanceOf[FuncDef].body
    .map(Plain(_))
    .toList

    // val types = module.value.definitions.collect({
    //   case Phrase(tipe@TypeDef(_, _)) => tipe
    // })
    val types = List()
    val funcs = module.definitions.collect({
      case fndef@FuncDef(_, _, _, _) => fndef
    }).toList

    val moduleInst = ModuleInstance(types, funcs, List(Memory(ArrayBuffer())))
    val config = Config(
      Frame(moduleInst, List()),
      Code(List(), instrs),
      1000
    )

    println(config.eval.code.stack)
  }

  basicTest()
  println("====")
  fileTest()
}
