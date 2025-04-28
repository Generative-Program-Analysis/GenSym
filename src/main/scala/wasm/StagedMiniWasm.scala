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
      case WasmConst(num) => eval(rest, Values.lift(num) :: stack, frame, kont, trail)
      case LocalGet(i) =>
        eval(rest, frame.get(i) :: stack, frame, kont, trail)
      case LocalSet(i) =>
        val (v, newStack) = (stack.head, stack.tail)
        frame(i) = v
        eval(rest, newStack, frame, kont, trail)
      case LocalTee(i) =>
        val (v, _) = (stack.head, stack.tail)
        frame(i) = v
        eval(rest, stack, frame, kont, trail)
      case GlobalGet(i) =>
        eval(rest, Global.globalGet(i) :: stack, frame, kont, trail)
      case GlobalSet(i) =>
        val (value, newStack) = (stack.head, stack.tail)
        module.globals(i).ty match {
          case GlobalType(tipe, true) => Global.globalSet(i, value)
          case _ => throw new Exception("Cannot set immutable global")
        }
        eval(rest, newStack, frame, kont, trail)
      case MemorySize => ???
      case MemoryGrow => ???
      case MemoryFill => ???
      case Nop =>
        eval(rest, stack, frame, kont, trail)
      case Unreachable => unreachable()
      case Test(op) =>
        val (v, newStack) = (stack.head, stack.tail)
        eval(rest, evalTestOp(op, v) :: newStack, frame, kont, trail)
      case Unary(op) =>
        val (v, newStack) = (stack.head, stack.tail)
        eval(rest, evalUnaryOp(op, v) :: newStack, frame, kont, trail)
      case Binary(op) =>
        val (v2, v1, newStack) = (stack.head, stack.tail.head, stack.tail.tail)
        eval(rest, evalBinOp(op, v1, v2) :: newStack, frame, kont, trail)
      case Compare(op) =>
        val (v2, v1, newStack) = (stack.head, stack.tail.head, stack.tail.tail)
        eval(rest, evalRelOp(op, v1, v2) :: newStack, frame, kont, trail)
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
        if (cond != Values.I32(0)) {
          eval(thn, inputs, frame, restK, restK :: trail)
        } else {
          eval(els, inputs, frame, restK, restK :: trail)
        }
      case Br(label) =>
        trail(label)(stack)
      case BrIf(label) =>
        val (cond, newStack) = (stack.head, stack.tail)
        if (cond != Values.I32(0)) trail(label)(newStack)
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
        val todo = "todo-op".reflectCtrlWith()
        eval(rest, todo :: stack, frame, kont, trail)
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
        val newFrame = frameOf(ty.inps.size + locals.size)
        newFrame.putAll(args)
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
          callee(Stack.emptyStack, newFrame, kont)
        else {
          val restK = fun(
            (retStack: Rep[Stack]) =>
              eval(rest, retStack.take(ty.out.size) ++ newStack, frame, kont, trail)
          )
          // We make a new trail by `restK`, since function creates a new block to escape
          // (more or less like `return`)
          callee(Stack.emptyStack, newFrame, restK)
        }
      case Import("console", "log", _)
         | Import("spectest", "print_i32", _) =>
        //println(s"[DEBUG] current stack: $stack")
        val (v, newStack) = (stack.head, stack.tail)
        println(v)
        eval(rest, newStack, frame, kont, trail)
      case Import(_, _, _) => throw new Exception(s"Unknown import at $funcIndex")
      case _               => throw new Exception(s"Definition at $funcIndex is not callable")
    }
  }

  def evalTestOp(op: TestOp, value: Rep[Num]): Rep[Num] = op match {
    case Eqz(_) => if (value.toInt == 0) Values.I32(1) else Values.I32(0)
  }

  def evalUnaryOp(op: UnaryOp, value: Rep[Num]): Rep[Num] = op match {
    case Clz(_) => value.clz()
    case Ctz(_) => value.ctz()
    case Popcnt(_) => value.popcnt()
    case _ => ???
  }

  def evalBinOp(op: BinOp, v1: Rep[Num], v2: Rep[Num]): Rep[Num] = op match {
    case Add(_) => v1 + v2
    case Mul(_) => v1 * v2
    case Sub(_) => v1 - v2
    case Shl(_) => v1 << v2
    // case ShrS(_) => v1 >> v2 // TODO: signed shift right
    case ShrU(_) => v1 >> v2
    case And(_) => v1 & v2
    case _ => ???
  }

  def evalRelOp(op: RelOp, v1: Rep[Num], v2: Rep[Num]): Rep[Num] = op match {
    case Eq(_) => v1 numEq v2
    case Ne(_) => v1 numNe v2
    case LtS(_) => v1 < v2
    case LtU(_) => v1 ltu v2
    case GtS(_) => v1 > v2
    case GtU(_) => v1 gtu v2
    case LeS(_) => v1 <= v2
    case LeU(_) => v1 leu v2
    case GeS(_) => v1 >= v2
    case GeU(_) => v1 geu v2
    case _ => ???
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
    eval(instrs, Stack.emptyStack, frame, kont, kont::Nil) // NOTE: simply use List(kont) here will cause compilation error
  }

  def evalTop(main: Option[String]): Rep[Unit] = {
    val haltK: Rep[Stack] => Rep[Unit] = stack => {
      "no-op".reflectCtrlWith[Unit]()
    }
    evalTop(fun(haltK), main)
  }

  // stack creation and operations
  object Stack {
    def emptyStack: Rep[Stack] = {
      "empty-stack".reflectWith()
    }
  }

  // call unreachable
  def unreachable(): Rep[Unit] = {
    "unreachable".reflectCtrlWith()
  }

  // runtime values
  object Values {
    def lift(num: Num): Rep[Num] = {
      num match {
        case I32V(i) => I32(i)
        case I64V(i) => I64(i)
      }
    }

    def I32(i: Rep[Int]): Rep[Num] = {
      "I32V".reflectWith(i)
    }

    def I64(i: Rep[Long]): Rep[Num] = {
      "I64V".reflectWith(i)
    }
  }

  // global read/write
  object Global{
    def globalGet(i: Int): Rep[Num] = {
      "global-get".reflectWith(i)
    }

    def globalSet(i: Int, value: Rep[Num]): Rep[Unit] = {
      "global-set".reflectCtrlWith(i, value)
    }
  }

  // TODO: The stack should be allocated on the stack to get optimal performance
  implicit class StackOps(stack: Rep[Stack]) {
    def head: Rep[Num] = {
      "stack-head".reflectCtrlWith(stack)
    }

    def tail: Rep[Stack] = {
      "stack-tail".reflectCtrlWith(stack)
    }

    def ::[A](v: Rep[Num]): Rep[Stack] = {
      "stack-cons".reflectCtrlWith(v, stack)
    }

    def ++(v: Rep[Stack]): Rep[Stack] = {
      "stack-append".reflectCtrlWith(stack, v)
    }

    def take(n: Int): Rep[Stack] = {
      if (n == 0) Stack.emptyStack
      else "stack-take".reflectWith(stack, n)
    }

    def drop(n: Int): Rep[Stack] = {
      if (n == 0) stack
      else "stack-drop".reflectWith(stack, n)
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

    def get(i: Int): Rep[Num] = {
      "frame-get".reflectCtrlWith(frame, i)
    }

    def putAll(args: Rep[Stack]) = {
      "frame-putAll".reflectCtrlWith(frame, args)
    }

    def update(i: Int, value: Rep[Num]) = {
      "frame-update".reflectCtrlWith(frame, i, value)
    }

  }

  // runtime Num type
  implicit class NumOps(num: Rep[Num]) {

    def toInt: Rep[Int] = "num-to-int".reflectWith(num)

    def clz(): Rep[Num] = "unary-clz".reflectWith(num)

    def ctz(): Rep[Num] = "unary-ctz".reflectWith(num)

    def popcnt(): Rep[Num] = "unary-popcnt".reflectWith(num)

    def +(rhs: Rep[Num]): Rep[Num] = "binary-add".reflectWith(num, rhs)

    def -(rhs: Rep[Num]): Rep[Num] = "binary-sub".reflectWith(num, rhs)

    def *(rhs: Rep[Num]): Rep[Num] = "binary-mul".reflectWith(num, rhs)

    def /(rhs: Rep[Num]): Rep[Num] = "binary-div".reflectWith(num, rhs)

    def <<(rhs: Rep[Num]): Rep[Num] = "binary-shl".reflectWith(num, rhs)

    def >>(rhs: Rep[Num]): Rep[Num] = "binary-shr".reflectWith(num, rhs)

    def &(rhs: Rep[Num]): Rep[Num] = "binary-and".reflectWith(num, rhs)

    def numEq(rhs: Rep[Num]): Rep[Num] = "relation-eq".reflectWith(num, rhs)

    def numNe(rhs: Rep[Num]): Rep[Num] = "relation-ne".reflectWith(num, rhs)

    def <(rhs: Rep[Num]): Rep[Num] = "relation-lt".reflectWith(num, rhs)

    def ltu(rhs: Rep[Num]): Rep[Num] = "relation-ltu".reflectWith(num, rhs)

    def >(rhs: Rep[Num]): Rep[Num] = "relation-gt".reflectWith(num, rhs)

    def gtu(rhs: Rep[Num]): Rep[Num] = "relation-gtu".reflectWith(num, rhs)

    def <=(rhs: Rep[Num]): Rep[Num] = "relation-le".reflectWith(num, rhs)

    def leu(rhs: Rep[Num]): Rep[Num] = "relation-leu".reflectWith(num, rhs)

    def >=(rhs: Rep[Num]): Rep[Num] = "relation-ge".reflectWith(num, rhs)

    def geu(rhs: Rep[Num]): Rep[Num] = "relation-geu".reflectWith(num, rhs)
  }
}

trait StagedWasmScalaGen extends ScalaGenBase with SAICodeGenBase {
  override def traverse(n: Node): Unit = n match {
    case Node(_, "frame-update", List(frame, i, value), _) =>
      // TODO: what is the protocol of automatic new line insertion?
      shallow(frame); emit(".update("); shallow(i); emit(", "); shallow(value); emit(")\n")
    case Node(_, "global-set", List(i, value), _) =>
      shallow(i); emit(".globalSet("); shallow(value); emit(")")
    case _ => super.traverse(n)
  }

  // code generation for pure nodes
  override def shallow(n: Node): Unit = n match {
    case Node(_, "stack-take", List(stack, n), _) =>
      shallow(stack); emit(".take("); shallow(n); emit(")")
    case Node(_, "stack-drop", List(stack, n), _) =>
      shallow(stack); emit(".drop("); shallow(n); emit(")")
    case Node(_, "stack-append", List(stack1, stack2), _) =>
      shallow(stack1); emit(".++("); shallow(stack2); emit(")")
    case Node(_, "stack-head", List(stack), _) =>
      shallow(stack); emit(".head")
    case Node(_, "stack-reverse", List(stack), _) =>
      shallow(stack); emit(".reverse")
    case Node(_, "stack-cons", List(v, stack), _) =>
      shallow(stack); emit(".::("); shallow(v); emit(")")
    case Node(_, "stack-tail", List(stack), _) =>
      shallow(stack); emit(".tail")
    case Node(_, "empty-stack", _, _) =>
      emit("Nil")
    case Node(_, "frame-of", List(size), _) =>
      emit("new Frame("); shallow(size); emit(")")
    case Node(_, "frame-get", List(frame, i), _) =>
      shallow(frame); emit("("); shallow(i); emit(")")
    case Node(_, "frame-putAll", List(frame, args), _) =>
      shallow(frame); emit(".putAll("); shallow(args); emit(")")
    case Node(_, "global-get", List(i), _) =>
      emit("Global.globalGet("); shallow(i); emit(")")
    case Node(_, "binary-add", List(lhs, rhs), _) =>
      shallow(lhs); emit(" + "); shallow(rhs)
    case Node(_, "binary-sub", List(lhs, rhs), _) =>
      shallow(lhs); emit(" - "); shallow(rhs)
    case Node(_, "binary-mul", List(lhs, rhs), _) =>
      shallow(lhs); emit(" * "); shallow(rhs)
    case Node(_, "binary-div", List(lhs, rhs), _) =>
      shallow(lhs); emit(" / "); shallow(rhs)
    case Node(_, "binary-shl", List(lhs, rhs), _) =>
      shallow(lhs); emit(" << "); shallow(rhs)
    case Node(_, "binary-shr", List(lhs, rhs), _) =>
      shallow(lhs); emit(" >> "); shallow(rhs)
    case Node(_, "binary-and", List(lhs, rhs), _) =>
      shallow(lhs); emit(" & "); shallow(rhs)
    case Node(_, "relation-eq", List(lhs, rhs), _) =>
      shallow(lhs); emit(" == "); shallow(rhs)
    case Node(_, "relation-ne", List(lhs, rhs), _) =>
      shallow(lhs); emit(" != "); shallow(rhs)
    case Node(_, "relation-lt", List(lhs, rhs), _) =>
      shallow(lhs); emit(" < "); shallow(rhs)
    case Node(_, "relation-ltu", List(lhs, rhs), _) =>
      shallow(lhs); emit(" < "); shallow(rhs)
    case Node(_, "relation-gt", List(lhs, rhs), _) =>
      shallow(lhs); emit(" > "); shallow(rhs)
    case Node(_, "relation-gtu", List(lhs, rhs), _) =>
      shallow(lhs); emit(" > "); shallow(rhs)
    case Node(_, "relation-le", List(lhs, rhs), _) =>
      shallow(lhs); emit(" <= "); shallow(rhs)
    case Node(_, "relation-leu", List(lhs, rhs), _) =>
      shallow(lhs); emit(" <= "); shallow(rhs)
    case Node(_, "relation-ge", List(lhs, rhs), _) =>
      shallow(lhs); emit(" >= "); shallow(rhs)
    case Node(_, "relation-geu", List(lhs, rhs), _) =>
      shallow(lhs); emit(" >= "); shallow(rhs)
    case Node(_, "num-to-int", List(num), _) =>
      shallow(num); emit(".toInt")
    case Node(_, "no-op", _, _) =>
      emit("()")
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
  sealed abstract class Num {
    def +(that: Num): Num = (this, that) match {
      case (I32V(x), I32V(y)) => I32V(x + y)
      case (I64V(x), I64V(y)) => I64V(x + y)
      case _ => throw new RuntimeException("Invalid addition")
    }

    def -(that: Num): Num = (this, that) match {
      case (I32V(x), I32V(y)) => I32V(x - y)
      case (I64V(x), I64V(y)) => I64V(x - y)
      case _ => throw new RuntimeException("Invalid subtraction")
    }

    def !=(that: Num): Num = (this, that) match {
      case (I32V(x), I32V(y)) => I32V(if (x != y) 1 else 0)
      case (I64V(x), I64V(y)) => I32V(if (x != y) 1 else 0)
      case _ => throw new RuntimeException("Invalid inequality")
    }

    def toInt: Int = this match {
      case I32V(i) => i
      case I64V(i) => i.toInt
    }
  }
  case class I32V(i: Int) extends Num
  case class I64V(i: Long) extends Num


  type Stack = List[Num]

  class Frame(val size: Int) {
    private val data = new Array[Num](size)
    def apply(i: Int): Num = data(i)
    def update(i: Int, v: Num): Unit = data(i) = v
    def putAll(xs: List[Num]): Unit = {
      for (i <- 0 until xs.size) {
        data(i) = xs(i)
      }
    }
  }

  object Global {
    // TODO: create global with specific size
    private val globals = new Array[Num](10)
    def globalGet(i: Int): Num = globals(i)
    def globalSet(i: Int, v: Num): Unit = globals(i) = v
  }
}
import Prelude._

object Main {
  def main(args: Array[String]): Unit = {
    val snippet = new Snippet()
    snippet(())
  }
}
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
