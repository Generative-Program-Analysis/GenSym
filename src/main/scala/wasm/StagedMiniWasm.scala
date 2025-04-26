package gensym.wasm.miniwasm

import scala.collection.mutable.{ArrayBuffer, HashMap}

import lms.core.stub.Adapter
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{Base, ScalaGenBase}
import lms.core.Backend._
import lms.core.Backend.{Block => LMSBlock}

import gensym.wasm.ast._
import gensym.wasm.ast.{Const => WasmConst, Block => WasmBlock}
import gensym.lmsx.{SAIDriver, StringOps, SAIOps, SAICodeGenBase}

@virtualize
trait StagedWasmEvaluator extends SAIOps {
  def module: ModuleInstance
  // NOTE: we don't need the following statements anymore, but where are they initialized?
  // reset and initialize the internal state of Adapter
  // Adapter.resetState
  // Adapter.g = Adapter.mkGraphBuilder

  trait Stack
  type Cont[A] = Stack => A
  type Trail[A] = List[Rep[Cont[A]]]

  trait Frame

  // a cache storing the compiled code for each function, to reduce re-compilation
  val compileCache = new HashMap[Int, Rep[(Stack, Frame, Cont[Unit]) => Unit]]

  // NOTE: We don't support Ans type polymorphism yet
  def eval(insts: List[Instr],
           stack: Rep[Stack],
           frame: Rep[Frame],
           kont: Rep[Cont[Unit]],
           trail: Trail[Unit]): Rep[Unit] = {
    if (insts.isEmpty) return kont(stack)
    val (inst, rest) = (insts.head, insts.tail)
    inst match {
      case Drop => eval(rest, stack.tail, frame, kont, trail)
      case WasmConst(num) => eval(rest, num :: stack, frame, kont, trail)
      case LocalGet(i) =>
        eval(rest, frame.locals(i) :: stack, frame, kont, trail)
      case WasmBlock(ty, inner) =>
        val funcTy = ty.funcType
        val (inputs, restStack) = stack.splitAt(funcTy.inps.size)
        val restK = fun(
          (retStack: Rep[Stack]) =>
            eval(rest, retStack.take(funcTy.out.size) ++ restStack, frame, kont, trail)
        )
        eval(inner, inputs, frame, restK, restK :: trail)
      case Loop(ty, inner) =>
        val funcTy = ty.funcType
        val (inputs, restStack) = stack.splitAt(funcTy.inps.size)
        val restK = fun(
          (retStack: Rep[Stack]) =>
            eval(rest, retStack.take(funcTy.out.size) ++ restStack, frame, kont, trail)
        )
        def loop(retStack: Rep[Stack]): Rep[Unit] =
          eval(inner, retStack.take(funcTy.inps.size), frame, restK, fun(loop _) :: trail)
        loop(inputs)
      case If(ty, thn, els) =>
        val funcTy = ty.funcType
        val (cond, newStack) = (stack.head, stack.tail)
        val (inputs, restStack) = newStack.splitAt(funcTy.inps.size)
        // TODO: can we avoid code duplication here?
        val restK = fun(
          (retStack: Rep[Stack]) =>
            eval(rest, retStack.take(funcTy.out.size) ++ restStack, frame, kont, trail)
        )
        if (cond != 0) {
          eval(thn, inputs, frame, restK, restK :: trail)
        } else {
          eval(els, inputs, frame, restK, restK :: trail)
        }
      case Br(label) =>
        trail(label)(stack)
      case BrIf(label) =>
        val (cond, newStack) = (stack.head, stack.tail)
        if (cond != 0) trail(label)(newStack)
        else eval(rest, newStack, frame, kont, trail)
      case BrTable(labels, default) =>
        val (cond, newStack) = (stack.head, stack.tail)
        if (cond.toInt < labels.length) {
          var targets: Rep[List[Cont[Unit]]] = List(labels.map(i => trail(i)): _*)
          val goto: Rep[Cont[Unit]] = targets(cond.toInt)
          goto(newStack) // TODO: this line will trigger an exception
        } else {
          trail(default)(newStack)
        }
      case Return        => trail.last(stack)
      case Call(f)       => evalCall(rest, stack, frame, kont, trail, f, false)
      case ReturnCall(f) => evalCall(rest, stack, frame, kont, trail, f, true)
      case _ =>
        val noOp = "todo-op".reflectCtrlWith()
        eval(rest, noOp :: stack, frame, kont, trail)
    }
  }

  def evalCall(rest: List[Instr],
               stack: Rep[Stack],
               frame: Rep[Frame],
               kont: Rep[Cont[Unit]],
               trail: Trail[Unit],
               funcIndex: Int,
               isTail: Boolean): Rep[Unit] = {
    module.funcs(funcIndex) match {
      case FuncDef(_, FuncBodyDef(ty, _, locals, body)) =>
        val args = stack.take(ty.inps.size).reverse
        val newStack = stack.drop(ty.inps.size)
        val newFrame = frameOf(ty.inps.size + locals.size).put(args)
        val callee =
          if (compileCache.contains(funcIndex)) {
            compileCache(funcIndex)
          } else {
            val callee = fun(
              (stack: Rep[Stack], frame: Rep[Frame], kont: Rep[Cont[Unit]]) => {
                eval(body, stack, frame, kont, kont::Nil):Rep[Unit]
              }
            )
            compileCache(funcIndex) = callee
            callee
          }
        if (isTail)
          // when tail call, share the continuation for returning with the callee
          callee(emptyStack, newFrame, kont)
        else {
          val restK = fun(
            (retStack: Rep[Stack]) =>
              eval(rest, retStack.take(ty.out.size) ++ newStack, frame, kont, trail)
          )
          // We make a new trail by `restK`, since function creates a new block to escape
          // (more or less like `return`)
          callee(emptyStack, newFrame, restK)
        }
      // TODO: Support imported functions
      // case Import("console", "log", _) =>
      //   //println(s"[DEBUG] current stack: $stack")
      //   val I32V(v) :: newStack = stack
      //   println(v)
      //   eval(rest, newStack, frame, kont, trail)
      // case Import("spectest", "print_i32", _) =>
      //   //println(s"[DEBUG] current stack: $stack")
      //   val I32V(v) :: newStack = stack
      //   println(v)
      //   eval(rest, newStack, frame, kont, trail)
      case Import(_, _, _) => throw new Exception(s"Unknown import at $funcIndex")
      case _               => throw new Exception(s"Definition at $funcIndex is not callable")
    }
  }


  def evalTop(kont: Rep[Cont[Unit]], main: Option[String]): Rep[Unit] = {
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
    def head: Rep[Num] = {
      "stack-head".reflectCtrlWith(stack)
    }

    def tail: Rep[Stack] = {
      "stack-tail".reflectCtrlWith(stack)
    }

    def ::[A](v: Rep[A]): Rep[Stack] = {
      "stack-cons".reflectCtrlWith(v, stack)
    }

    def ++(v: Rep[Stack]): Rep[Stack] = {
      "stack-append".reflectCtrlWith(stack, v)
    }

    def take(n: Int): Rep[Stack] = {
      "stack-take".reflectWith(stack, n)
    }

    def drop(n: Int): Rep[Stack] = {
      "stack-drop".reflectWith(stack, n)
    }

    def reverse: Rep[Stack] = {
      "stack-reverse".reflectWith(stack)
    }

    def splitAt(n: Int): (Rep[Stack], Rep[Stack]) = {
      (take(n), drop(n))
    }
  }

  // frame creation and operations
  def frameOf(size: Int): Rep[Frame] = {
    "frame-of".reflectWith(size)
  }

  implicit class FrameOps(frame: Rep[Frame]) {

    def locals(i: Int): Rep[Num] = {
      "frame-get".reflectCtrlWith(frame, i)
    }

    def put(args: Rep[Stack]): Rep[Frame] = {
      "frame-put".reflectCtrlWith(frame, args)
    }

  }

  // runtime Num type
  implicit class NumOps(num: Rep[Num]) {
    def toInt: Rep[Int] = {
      "num-to-int".reflectWith(num)
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
      else if(m.toString.endsWith("Frame")) "Frame"
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
