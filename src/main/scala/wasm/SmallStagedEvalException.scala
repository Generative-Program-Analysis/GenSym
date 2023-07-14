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
  case class Returning(stack: Rep[List[Value]]) extends Exception

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

        case If(blockTy, thenInstrs, elseInstrs) => {
          val cond: Rep[Int] = stack.head
          val newStack = stack.tail
          val funcType = blockTy.toFuncType(frame.module)

          val zero: Rep[Int] = 0
          val retStack = if (cond == zero) {
            evalBlock(funcType.out.length, elseInstrs)
          } else {
            evalBlock(funcType.out.length, thenInstrs)
          }

          this.eval(retStack ++ newStack, instrs.tail)
        }
        case Block(blockTy, blockInstrs) => {
          val funcType = blockTy.toFuncType(frame.module)
          val retStack = evalBlock(funcType.out.length, blockInstrs.toList)
          this.eval(retStack ++ stack, instrs.tail)
        }
        case Loop(blockTy, loopInstrs) => {
          val funcType = blockTy.toFuncType(frame.module)
          val loopInstrList = loopInstrs.toList
          def loop: Rep[List[Value] => List[Value]] = fun { loopStack =>
            val blockStack = evalBlock(funcType.out.length, loopInstrList)
            loop(loopStack ++ blockStack)
          }
          this.eval(loop(stack), instrs.tail)
        }
        case Br(label) => {
          throw Breaking(label, stack)
        }
        case BrIf(label) => {
          val cond: Rep[Int] = stack.head
          val newStack = stack.tail
          val zero: Rep[Int] = 0
          if (cond == zero) {
            this.eval(newStack, instrs.tail)
          } else {
            throw Breaking(label, newStack)
          }
        }
        case Return => {
          throw Returning(stack)
        }
        case Call(func) => {
           val FuncDef(_, funcType, locals, body) = frame.module.funcs(func)
           // TODO: reverse args
           val args = stack.take(funcType.inps.length)
           val newStack = stack.drop(funcType.inps.length)
     
           val initFrameLocals: Rep[List[Value]] = List.fill(locals.length)(I32(0))
           val frameLocals: Rep[List[Value]] = args ++ initFrameLocals
           val newFrame = Frame(frame.module, frameLocals)
           val retStack = evalFunc(args, newFrame, funcType, body.toList)
           this.eval(retStack ++ newStack, instrs.tail)
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

    def evalFunc(args: Rep[List[Value]], nframe: Frame, funcType: FuncType, instrs: List[Instr]): Rep[List[Value]] = {
      try {
        this.copy(frame = nframe).evalBlock(funcType.out.length, instrs)
      } catch {
        case Returning(retStack) => retStack.take(funcType.out.length)
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
    val funcs = List(
      FuncDef("add5", FuncType(Seq(NumType(I32Type)), Seq(NumType(I32Type))), Seq(NumType(I32Type)), Seq(
        Konst(I32C(5)), Binary(BinOp.Int(Add))
      ))
    )
    ModuleInstance(types, funcs)
  }
  val instrs = List(
    Konst(I32C(10)),
    Loop(ValBlockType(None), Seq(
      Konst(I32C(1)), Binary(BinOp.Int(Add))
    ))
    // Binary(BinOp.Int(Add)),
    // If(ValBlockType(Some(NumType(I32Type))), List(Konst(I32C(2999))), List(Br(0)))
  )
  val snip = mkVMSnippet(instrs, module)
  val code = snip.code
  println(code)
  // snip.eval(List(5))
}

