package gensym.wasm.example

import gensym.wasm.ast._
import gensym.wasm.source._
import gensym.wasm.parser._
import gensym.wasm.memory._
import gensym.wasm.symbolic._

object SimpleTest extends App {
  import gensym.wasm.eval._
  def basicTest() = {
    val instrs = List(
      Const(I32V(1)),
      Const(I32V(5)),
      Binary(Add(NumType(I32Type))),
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
    val module = Parser.parseFile("./benchmarks/wasm/test.wat")
    println(module)

    val instrs = module.definitions.find({
      case FuncDef(Some("$real_main"), FuncBodyDef(_, _, _, _)) => true
      case _ => false
    }).map({
      case FuncDef(_, FuncBodyDef(_, _, _, body)) => body
    }).get
    //.map(Plain(_))
    .toList

    // val types = module.value.definitions.collect({
    //   case Phrase(tipe@TypeDef(_, _)) => tipe
    // })
    val types = List()
    val funcs = module.definitions.collect({
      case FuncDef(_, fndef@FuncBodyDef(_, _, _, _)) => fndef
    }).toList

    println(module.definitions)

    val moduleInst = ModuleInstance(types, funcs)
    val config = Config(
      Frame(moduleInst, List(I32V(0))),
      1000
    )

    // val testInstrs = List(
    //   Const(I32(5)),
    //   Block(ValBlockType(Some(NumType(I32Type))), Seq(
    //     Block(ValBlockType(Some(NumType(I32Type))), Seq(
    //       Block(ValBlockType(Some(NumType(I32Type))), Seq(
    //         LocalGet(0),
    //         BrIf(2),
    //         Const(I32(1)),
    //       ))
    //     ))
    //   ))
    // )
    println(config.eval(List(), instrs))
  }

  def fileTestNewEval() = {
    import gensym.wasm.miniwasm._
    import collection.mutable.ArrayBuffer
    val module = Parser.parseFile("./benchmarks/wasm/test.wat")
    println(module)

    val instrs = module.definitions.find({
      case FuncDef(Some("$real_main"), FuncBodyDef(_, _, _, _)) => true
      case _ => false
    }).map({
      case FuncDef(_, FuncBodyDef(_, _, _, body)) => body
    }).get
    .toList

    val types = List()
    val funcs = module.definitions.collect({
      case FuncDef(_, fndef@FuncBodyDef(_, _, _, _)) => fndef
    }).toList

    val moduleInst = ModuleInstance(types, funcs)

    Evaluator.eval(instrs, List(), Frame(moduleInst, ArrayBuffer(I32V(0))),
      newStack => println(s"retCont: $newStack"),
      List(newStack => println(s"trail: $newStack")))
  }

  def fileTestConcolicEval() = {
    import gensym.wasm.concolicminiwasm._
    val module = Parser.parseFile("./benchmarks/wasm/test.wat")
    Evaluator.execWholeProgram(module, "$real_main")
  }

  //basicTest()
  //println("====")
  //fileTest()
  // fileTestNewEval()
  fileTestConcolicEval()
}

//object SimpleStagedTest extends App {
//  import gensym.wasm.stagedeval._
//  import gensym.wasm.eval.ModuleInstance

//  def basicTest() = {
//    val instrs = List(
//      Const(I32(1)),
//      Const(I32(5)),
//      Binary(BinOp.Int(Add)),
//    )
//    //.map(Plain(_))

//    val moduleInst = ModuleInstance(List(), List())
//    val config = Config(
//      Frame(moduleInst, List()),
//      1000
//    )

//    println(config.eval(List(), instrs))
//  }

//  def fileTest() = {
//    val code = scala.io.Source.fromFile("./benchmarks/wasm/test.wat").mkString
//    val module = Parser.parseString(code)
//    println(module)

//    val instrs = module.definitions.find({
//      case FuncDef("$real_main", _, _, _) => true
//      case _ => false
//    }).get.asInstanceOf[FuncDef].body
//    //.map(Plain(_))
//    .toList

//    // val types = module.value.definitions.collect({
//    //   case Phrase(tipe@TypeDef(_, _)) => tipe
//    // })
//    val types = List()
//    val funcs = module.definitions.collect({
//      case fndef@FuncDef(_, _, _, _) => fndef
//    }).toList

//    val moduleInst = ModuleInstance(types, funcs)
//    val config = Config(
//      Frame(moduleInst, List()),
//      1000
//    )

//    println(config.eval(List(), instrs))
//  }

//  basicTest()
//  println("====")
//  fileTest()
//}
