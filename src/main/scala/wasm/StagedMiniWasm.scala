package gensym.wasm.miniwasm

import scala.collection.mutable.ArrayBuffer

import lms.core.stub.Adapter
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{Base, ScalaGenBase}
import lms.core.Backend._
import lms.core.Backend.{Block => LMSBlock}

import gensym.wasm.ast._
import gensym.wasm.ast.{Const => ConstInstr}
import gensym.lmsx.{SAIDriver, StringOps, SAIOps, SAICodeGenBase}

@virtualize
trait StagedWasmEvaluator extends SAIOps {
  def module: ModuleInstance
  // NOTE: we don't need the following statements anymore, but where are they initialized?
  // reset and initialize the internal state of Adapter
  // Adapter.resetState
  // Adapter.g = Adapter.mkGraphBuilder

  trait Stack
  type Cont[A] = Rep[Stack => A]
  type Trail[A] = List[Cont[A]]

  trait Frame

  // Ans should be instantiated to something like Int, Unit, etc, which is the result type of staged program
  def eval(insts: List[Instr],
           stack: Rep[Stack],
           frame: Rep[Frame],
           kont: Cont[Unit],
           trail: Trail[Unit]): Rep[Unit] = {
    if (insts.isEmpty) return kont(stack)
    val (inst, rest) = (insts.head, insts.tail)
    inst match {
      case Drop => eval(rest, stack.tail, frame, kont, trail)
      case ConstInstr(num) => eval(rest, num :: stack, frame, kont, trail)
      case LocalGet(i) =>
        eval(rest, frame.locals(i) :: stack, frame, kont, trail)
      case _ => 
        val noOp = "todo-op".reflectCtrlWith()
        eval(rest, noOp :: stack, frame, kont, trail)
    }
  }

  def evalTop(kont: Cont[Unit], main: Option[String]): Rep[Unit] = {
    val funBody: FuncBodyDef = main match {
      case Some(func_name) =>
        module.defs.flatMap({
          case Export(`func_name`, ExportFunc(fid)) =>
            println(s"Entering function $main")
            module.funcs(fid) match {
              case FuncDef(_, body@FuncBodyDef(_,_,_,_)) => Some(body)
              case _ => throw new Exception("Entry function has no concrete body")
            }
          case _ => None
        }).head
      case None =>
        val startIds = module.defs.flatMap {
            case Start(id) => Some(id)
            case _ => None
        }
        val startId = startIds.headOption.getOrElse { throw new Exception("No start function") }
        module.funcs(startId) match {
          case FuncDef(_, body@FuncBodyDef(_,_,_,_)) => body
          case _ =>
            throw new Exception("Entry function has no concrete body")
        }
    }
    val (instrs, localSize) = (funBody.body, funBody.locals.size)
    val frame = frameOf(localSize)
    eval(instrs, emptyStack, frame, kont, kont::Nil) // NOTE: simply use List(kont) here will cause compilation error
  }

  def evalTop(main: Option[String]): Rep[Unit] = {
    val haltK: Rep[Stack] => Rep[Unit] = stack => {
      "no-op".reflectCtrlWith()
    }
    evalTop(fun(haltK), main)
  }


  // stack creation and operations
  def emptyStack: Rep[Stack] = {
    "empty-stack".reflectWith()
  }

  // TODO: The stack should be allocated on the stack to get optimal performance
  implicit class StackOps(stack: Rep[Stack]) {
    def tail(): Rep[Stack] = {
      "stack-tail".reflectCtrlWith(stack)
    }

    def ::[A](v: Rep[A]): Rep[Stack] = {
      "stack-cons".reflectCtrlWith(v, stack)
    }
  }

  // frame creation and operations
  def frameOf(size: Int): Rep[Frame] = {
    "frame-of".reflectWith(size)
  }

  implicit class FrameOps(frame: Rep[Frame]) {

    def locals(i: Int): Rep[Num] = {
      "frame-locals".reflectCtrlWith(frame, i)
    }
  }
}
trait StagedWasmScalaGen extends ScalaGenBase with SAICodeGenBase {
  override def traverse(n: Node): Unit = n match {
    case _ => super.traverse(n)
  }

  // code generation for pure nodes
  override def shallow(n: Node): Unit = n match {
    case Node(_, "stack-cons", List(v, stack), _) =>
      shallow(stack); emit(".push("); shallow(v); emit(")")
    case Node(_, "stack-tail", List(stack), _) =>
      shallow(stack); emit(".pop()")
    case Node(_, "empty-stack", _, _) =>
      emit("new Stack()")
    case _ => super.shallow(n)
  }
}
trait WasmCompilerDriver[A, B]
  extends SAIDriver[A, B] with StagedWasmEvaluator { q =>
  override val codegen = new StagedWasmScalaGen {
    val IR: q.type = q
    import IR._
    override def remap(m: Manifest[_]): String = {
      if (m.toString.endsWith("Stack")) "Stack"
      else super.remap(m)
    }
  }

   override val prelude =
 """
 object Prelude {
 }
 import Prelude._
 """
}

object PartialEvaluator {
  def apply(moduleInst: ModuleInstance, main: Option[String]): String = {
    println(s"Now compiling wasm module with entry function $main")
    val code = new WasmCompilerDriver[Unit, Unit] {
      def module: ModuleInstance = moduleInst
      def snippet(x: Rep[Unit]): Rep[Unit] = {
        evalTop(main)
      }
    }
    code.code
  }
}
