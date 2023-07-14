package gensym.wasm.smallstagedevalexception

import gensym.wasm.eval.ModuleInstance
import gensym.wasm.ast.{Const => Konst, _}
import gensym.wasm.values.{I32 => I32C}
import gensym.wasm.types._

import lms.core._
import lms.core.stub._
import lms.macros.SourceContext
import lms.core.virtualize
import gensym.lmsx._

@virtualize
trait StagedEval extends SAIOps {
  trait Value
  def I32(i: Rep[Int]): Rep[Value] = "I32V".reflectWith[Value](i)
  def I64(i: Rep[Long]): Rep[Value] = "I64V".reflectWith[Value](i)

  implicit def repI32Proj(i: Rep[Value]): Rep[Int] = Unwrap(i) match {
    case Adapter.g.Def("I32V", scala.collection.immutable.List(v: Backend.Exp)) =>
      Wrap[Int](v)
    case _ =>
      Wrap[Int](Adapter.g.reflect("I32V-proj", Unwrap(i)))
  }

  case class Frame(module: ModuleInstance, locals: Rep[List[Value]])
  case class Breaking(n: Int, stack: Rep[List[Value]]) extends Exception
  // case class Continue(stack: Rep[List[Value]]) extends EvalResult
  // case class Breaking(n: Int, stack: Rep[List[Value]]) extends EvalResult
  // case class Returning(stack: Rep[List[Value]]) extends EvalResult
  // abstract class EvalResult {
  //   def onContinue(f: Rep[List[Value]] => EvalResult): EvalResult = {
  //     this match {
  //       case Continue(stack) => f(stack)
  //       case _ => this
  //     }
  //   }
  // }

  case class Config(frame: Frame, stackBudget: Int) {
    def evalBinOp(op: BinOp, lhsV: Rep[Value], rhsV: Rep[Value]): Rep[Value] = op match {
      case BinOp.Int(Add) => {
        // assume I32
        val (lhs, rhs) = (repI32Proj(lhsV), repI32Proj(rhsV))
        I32(lhs + rhs)
      }
    }

    def eval(stack: Rep[List[Value]], instrs: List[Instr]): Rep[List[Value]] = {
      if (instrs.isEmpty) return stack

      instrs.head match {
        case Drop => this.eval(stack.tail, instrs.tail)

        case Konst(I32C(n)) => this.eval(I32(n) :: stack, instrs.tail)
        case Binary(op) => {
          val (v2, v1) = (stack(0), stack(1))
          val newStack = stack.drop(2)
          this.eval(evalBinOp(op, v1, v2) :: newStack, instrs.tail)
        }
        case Br(label) => {
          throw Breaking(label, stack)
        }

        case If(blockTy, thenInstrs, elseInstrs) => {
          val cond: Rep[Int] = stack.head
          val newStack = stack.tail
          val funcType = blockTy.toFuncType(frame.module)

          val zero: Rep[Int] = 0
          val condBool = cond == zero
          val res = if (condBool) {
            val retStack = evalBlock(funcType.out.length, elseInstrs)
            this.eval(retStack ++ newStack, instrs.tail)
          } else {
            val retStack = evalBlock(funcType.out.length, thenInstrs)
            this.eval(retStack ++ newStack, instrs.tail)
          }
          res
        }
      }
    }

    // basically equates to step with Label on top in the previous impl
    def evalBlock(outs: Int, instrs: List[Instr]): Rep[List[Value]] = {
      try {
        this.copy().eval(List[Value](), instrs)
      } catch {
        case Breaking(0, stack) => stack.take(outs)
        case Breaking(n, stack) => throw Breaking(n - 1, stack)
        case e: Exception => throw e
      }
    }
  }
}

object SmallStagedTest extends App {
  @virtualize
  def mkVMSnippet(instrs: List[Instr], module: ModuleInstance): CppSAIDriver[List[Int], List[Int]] with StagedEval = {
    new CppSAIDriver[List[Int], List[Int]] with StagedEval {
      def snippet(stack: Rep[List[Int]]): Rep[List[Int]] = {
        val config = Config(Frame(module, List[Value]()), 1000)
        config.eval(stack.map(I32), instrs).map(repI32Proj)
        // config.eval(stack.map(I32), instrs) match {
        //   case Continue(s) => s.map(repI32Proj)
        //   case _ => ???
        // }
      }
    }
  }

  val module = {
    val types = List()
    val funcs = List()
    ModuleInstance(types, funcs)
  }
  val instrs = List(
    // Konst(I32C(-5)),
    // Binary(BinOp.Int(Add)),
    If(ValBlockType(Some(NumType(I32Type))), List(Konst(I32C(2999))), List(Br(0)))
  )
  val snip = mkVMSnippet(instrs, module)
  val code = snip.code
  println(code)
  // snip.eval(List(5))
}

