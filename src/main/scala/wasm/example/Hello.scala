package gensym.wasm.example

import gensym.wasm.ast._
import gensym.wasm.types._
import gensym.wasm.values._
import gensym.wasm.source._
import gensym.wasm.parser._
import gensym.wasm.memory._

object SimpleTest extends App {
  import gensym.wasm.eval._
  def basicTest() = {
    val instrs = List(
      Const(I32(1)),
      Const(I32(5)),
      Binary(BinOp.Int(Add)),
    )
    //.map(Plain(_))

    val moduleInst = ModuleInstance(List(), List())
    val config = Config(
      Frame(moduleInst, List()),
      1000
    )

    println(config.eval(List(), instrs))
  }

  def fileTest() = {
    val code = scala.io.Source.fromFile("./benchmarks/wasm/test.wat").mkString
    val module = Parser.parseString(code)
    println(module)

    val instrs = module.definitions.find({
      case FuncDef("$real_main", _, _, _) => true
      case _ => false
    }).get.asInstanceOf[FuncDef].body
    //.map(Plain(_))
    .toList

    // val types = module.value.definitions.collect({
    //   case Phrase(tipe@TypeDef(_, _)) => tipe
    // })
    val types = List()
    val funcs = module.definitions.collect({
      case fndef@FuncDef(_, _, _, _) => fndef
    }).toList

    val moduleInst = ModuleInstance(types, funcs)
    val config = Config(
      Frame(moduleInst, List()),
      1000
    )

    println(config.eval(List(), instrs))
  }

  basicTest()
  println("====")
  fileTest()
}

object SimpleStagedTest extends App {
  import gensym.wasm.stagedeval._
  import gensym.wasm.eval.ModuleInstance

  def basicTest() = {
    val instrs = List(
      Const(I32(1)),
      Const(I32(5)),
      Binary(BinOp.Int(Add)),
    )
    //.map(Plain(_))

    val moduleInst = ModuleInstance(List(), List())
    val config = Config(
      Frame(moduleInst, List()),
      1000
    )

    println(config.eval(List(), instrs))
  }

  def fileTest() = {
    val code = scala.io.Source.fromFile("./benchmarks/wasm/test.wat").mkString
    val module = Parser.parseString(code)
    println(module)

    val instrs = module.definitions.find({
      case FuncDef("$real_main", _, _, _) => true
      case _ => false
    }).get.asInstanceOf[FuncDef].body
    //.map(Plain(_))
    .toList

    // val types = module.value.definitions.collect({
    //   case Phrase(tipe@TypeDef(_, _)) => tipe
    // })
    val types = List()
    val funcs = module.definitions.collect({
      case fndef@FuncDef(_, _, _, _) => fndef
    }).toList

    val moduleInst = ModuleInstance(types, funcs)
    val config = Config(
      Frame(moduleInst, List()),
      1000
    )

    println(config.eval(List(), instrs))
  }

  basicTest()
  println("====")
  fileTest()
}
