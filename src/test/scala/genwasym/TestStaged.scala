package gensym.wasm

import gensym.wasm.newstagedevalcps.{
  CppStagedWasmDriver,
  ModuleInstance,
  Preprocess,
  StagedEvalCPS
}
import scala.collection.mutable.HashMap

import gensym.wasm.ast.{Const => Konst, _}
//import gensym.wasm.values.{I32 => I32C}
//import gensym.wasm.types._
import gensym.wasm.memory._
//import gensym.wasm.globals._

import lms.core._
import lms.core.stub._
import lms.macros.SourceContext
import lms.core.virtualize
import gensym.lmsx._

import scala.collection.mutable.HashMap

import org.scalatest.FunSuite

// TODO: fix me, there's a duplicate version under NewStagedEvalCPS.scala
// that extends App, which runs
class StagedEvalCPSTest extends FunSuite {
  @virtualize
  def mkVMSnippet(
      module: ModuleInstance,
      instrs: List[Instr],
  ): CppSAIDriver[Int, Unit] with StagedEvalCPS = {
    new CppStagedWasmDriver[Int, Unit] with StagedEvalCPS {
      def snippet(arg: Rep[Int]): Rep[Unit] = {
        // val state = State(List[Memory](), List[Global](), List[Value](I32(0)))
        val config = Config(module, HashMap(), HashMap(), HashMap(), 1000)
        val fin = (ss: StaticState) =>
          topFun { (_: Rep[Unit]) =>
            println(ss.numLocals); State.printStack; ()
        }
        initState(List[Memory](), List[RTGlobal](), 0)
        val ss = StaticState(Nil, None, 0, 0)
        for (FuncDef(name, FuncBodyDef(tipe, _, locals, body)) <- module.funcs) {
          config.compileFun(name.get, tipe, locals, body)
        }
        //config.compileStuffIn(instrs, fin)(ss) // not necessary
        System.out.println(s"blockConts: ${config.blockConts}")
        config.execInstrs(instrs, fin)(ss)
      }
    }
  }

  val module = {
    val file =
      scala.io.Source.fromFile("./benchmarks/wasm/test_rs.wat").mkString
    gensym.wasm.parser.Parser.parse(file)
  }

  val moduleInst = {
    val types = List()
    val funcs = module.definitions
      .collect({
        case FuncDef(name, FuncBodyDef(ty, nms, locals, body)) =>
          FuncDef(name, FuncBodyDef(ty, nms, locals, Preprocess.idBlocks(body)))
      })
      .toList
    ModuleInstance(List(), funcs)
  }
  // val moduleInst = {
  //   val types = List()
  //   val funcs = List(
  //     FuncDef("add5", FuncType(Seq(NumType(I32Type)), Seq(NumType(I32Type))), Seq(), Seq(
  //       LocalGet(0), Konst(I32C(5)), Binary(BinOp.Int(Add))
  //     ))
  //   )
  //   ModuleInstance(types, funcs)
  // }
  val instrs =
    module.definitions
      .find({
        case FuncDef(Some("$real_main"), _) => true
        case _                              => false
      })
      .get
      .asInstanceOf[FuncDef]
      .f
      .asInstanceOf[FuncBodyDef]
      .body // Super unsafe...
  // val instrs = List(
  //   Konst(I32C(10)),
  //   // LocalSet(0),
  //   // LocalGet(0),
  //   // Konst(I32C(5)),
  //   // Binary(BinOp.Int(Add)),
  //   // LocalSet(1),
  //   // Loop(ValBlockType(None), Seq(
  //   //   LocalGet(0),
  //   //   Konst(I32C(1)),
  //   //   Binary(BinOp.Int(Sub)),
  //   //   LocalTee(0),
  //   //   Konst(I32C(5)),
  //   //   Binary(BinOp.Int(Sub)),
  //   //   Test(TestOp.Int(Eqz)),
  //   //   BrIf(1),
  //   // )),
  //   // Block(ValBlockType(None), Seq(
  //   //   Konst(I32C(0)),
  //   //   // Konst(I32C(-15)),
  //   //   // Binary(BinOp.Int(Add)),
  //   //   // Block(ValBlockType(None), Seq(
  //   //   //   Konst(I32C(10))
  //   //   // )),
  //   //   BrIf(0),
  //   //   Konst(I32C(20)),
  //   // )),
  //   // Konst(I32C(12)),
  //   // Call(0),
  //   // Call(0),
  // )
  System.out.println(s"funcs: ${moduleInst.funcs}")
  val snip = mkVMSnippet(moduleInst, Preprocess.idBlocks(instrs))
  val code = snip.code
  println(code)
  snip.eval(0)
}
// TODO: old test from Hello.scala

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
