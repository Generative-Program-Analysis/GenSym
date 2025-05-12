package gensym.wasm.miniwasm

import scala.collection.mutable.{ArrayBuffer, HashMap}

import lms.core.stub.Adapter
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{Base, ScalaGenBase, CGenBase}
import lms.core.Backend._
import lms.core.Backend.{Block => LMSBlock}

import gensym.wasm.ast._
import gensym.wasm.ast.{Const => WasmConst, Block => WasmBlock}
import gensym.lmsx.{SAIDriver, StringOps, SAIOps, SAICodeGenBase, CppSAIDriver, CppSAICodeGenBase}

@virtualize
trait StagedWasmEvaluator extends SAIOps {
  def module: ModuleInstance
  // NOTE: we don't need the following statements anymore, but where are they initialized?
  // reset and initialize the internal state of Adapter
  // Adapter.resetState
  // Adapter.g = Adapter.mkGraphBuilder

  trait Slice

  trait Frame

  type Cont[A] = Unit => A
  type Trail[A] = List[Rep[Cont[A]]]

  // a cache storing the compiled code for each function, to reduce re-compilation
  val compileCache = new HashMap[Int, Rep[(Cont[Unit]) => Unit]]

  // NOTE: We don't support Ans type polymorphism yet
  def eval(insts: List[Instr],
           kont: Rep[Cont[Unit]],
           trail: Trail[Unit]): Rep[Unit] = {
    if (insts.isEmpty) return kont()
    val (inst, rest) = (insts.head, insts.tail)
    inst match {
      case Drop =>
        Stack.pop()
        eval(rest, kont, trail)
      case WasmConst(num) =>
        Stack.push(num)
        eval(rest, kont, trail)
      case LocalGet(i) =>
        Stack.push(Frames.get(i))
        eval(rest, kont, trail)
      case LocalSet(i) =>
        Frames.set(i, Stack.pop())
        eval(rest, kont, trail)
      case LocalTee(i) =>
        Frames.set(i, Stack.peek)
        eval(rest, kont, trail)
      case GlobalGet(i) =>
        Stack.push(Global.globalGet(i))
        eval(rest, kont, trail)
      case GlobalSet(i) =>
        val value = Stack.pop()
        module.globals(i).ty match {
          case GlobalType(tipe, true) => Global.globalSet(i, value)
          case _ => throw new Exception("Cannot set immutable global")
        }
        eval(rest, kont, trail)
      case MemorySize => ???
      case MemoryGrow => ???
      case MemoryFill => ???
      case Nop =>
        eval(rest, kont, trail)
      case Unreachable => unreachable()
      case Test(op) =>
        val v = Stack.pop()
        Stack.push(evalTestOp(op, v))
        eval(rest, kont, trail)
      case Unary(op) =>
        val v = Stack.pop()
        Stack.push(evalUnaryOp(op, v))
        eval(rest, kont, trail)
      case Binary(op) =>
        val v2 = Stack.pop()
        val v1 = Stack.pop()
        Stack.push(evalBinOp(op, v1, v2))
        eval(rest, kont, trail)
      case Compare(op) =>
        val v2 = Stack.pop()
        val v1 = Stack.pop()
        Stack.push(evalRelOp(op, v1, v2))
        eval(rest, kont, trail)
      case WasmBlock(ty, inner) =>
        // no need to modify the stack when entering a block
        // the type system guarantees that we will never take more than the input size from the stack
        val funcTy = ty.funcType
        // TODO: somehow the type of exitSize in residual program is nothing
        val exitSize = Stack.size - funcTy.inps.size + funcTy.out.size
        val restK: Rep[Cont[Unit]] = fun((_: Rep[Unit]) => {
          Stack.reset(exitSize)
          eval(rest, kont, trail)
        })
        eval(inner, restK, restK :: trail)
      case Loop(ty, inner) =>
        val funcTy = ty.funcType
        val exitSize = Stack.size - funcTy.inps.size + funcTy.out.size
        val restK = fun((_: Rep[Unit]) => {
          Stack.reset(exitSize)
          eval(rest, kont, trail)
        })
        def loop(_u: Rep[Unit]): Rep[Unit] =
          eval(inner, restK, fun(loop _) :: trail)
        loop(())
      case If(ty, thn, els) =>
        val funcTy = ty.funcType
        val exitSize = Stack.size - funcTy.inps.size + funcTy.out.size
        val cond = Stack.pop()
        // TODO: can we avoid code duplication here?
        val restK = fun((_: Rep[Unit]) => {
          Stack.reset(exitSize)
          eval(rest, kont, trail)
        })
        if (cond != Values.I32(0)) {
          eval(thn, restK, restK :: trail)
        } else {
          eval(els, restK, restK :: trail)
        }
      case Br(label) =>
        info(s"Jump to $label")
        trail(label)(())
      case BrIf(label) =>
        val cond = Stack.pop()
        info(s"The br_if(${label})'s condition is ", cond)
        if (cond != Values.I32(0)) {
          info(s"Jump to $label")
          trail(label)(())
        } else {
          info(s"Continue")
          eval(rest, kont, trail)
        }
      case BrTable(labels, default) =>
        val cond = Stack.pop()
        def aux(choices: List[Int], idx: Int): Rep[Unit] = {
          if (choices.isEmpty) trail(default)(())
          else {
            if (cond.toInt == idx) trail(choices.head)(())
            else aux(choices.tail, idx + 1)
          }
        }
        aux(labels, 0)
      case Return        => trail.last(())
      case Call(f)       => evalCall(rest, kont, trail, f, false)
      case ReturnCall(f) => evalCall(rest, kont, trail, f, true)
      case _ =>
        val todo = "todo-op".reflectCtrlWith()
        eval(rest, kont, trail)
    }
  }

  def evalCall(rest: List[Instr],
               kont: Rep[Cont[Unit]],
               trail: Trail[Unit],
               funcIndex: Int,
               isTail: Boolean): Rep[Unit] = {
    module.funcs(funcIndex) match {
      case FuncDef(_, FuncBodyDef(ty, _, locals, body)) =>
        val returnSize = Stack.size - ty.inps.size + ty.out.size
        val args = Stack.take(ty.inps.size)
        info("New frame:", Frames.top)
        val callee =
          if (compileCache.contains(funcIndex)) {
            compileCache(funcIndex)
          } else {
            val callee = topFun(
              (kont: Rep[Cont[Unit]]) => {
                info(s"Entered the function at $funcIndex, stackSize =", Stack.size, ", frame =", Frames.top)
                eval(body, kont, kont::Nil): Rep[Unit]
              }
            )
            compileCache(funcIndex) = callee
            callee
          }
        val frameSize = ty.inps.size + locals.size
        if (isTail) {
          // when tail call, return to the caller's return continuation
          Frames.popFrame()
          Frames.pushFrame(frameSize)
          Frames.putAll(args)
          callee(trail.last)
        } else {
          val restK: Rep[Cont[Unit]] = fun((_: Rep[Unit]) => {
            Stack.reset(returnSize)
            Frames.popFrame()
            eval(rest, kont, trail)
          })
          // We make a new trail by `restK`, since function creates a new block to escape
          // (more or less like `return`)
          Frames.pushFrame(frameSize)
          Frames.putAll(args)
          callee(restK)
        }
      case Import("console", "log", _)
         | Import("spectest", "print_i32", _) =>
        //println(s"[DEBUG] current stack: $stack")
        val v = Stack.pop()
        println(v)
        eval(rest, kont, trail)
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
            Predef.println(s"Now compiling start with function $main")
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
    Stack.initialize()
    Frames.pushFrame(localSize)
    eval(instrs, kont, kont::Nil)
    Frames.popFrame()
  }

  def evalTop(main: Option[String], printRes: Boolean = false): Rep[Unit] = {
    val haltK: Rep[Unit] => Rep[Unit] = (_) => {
      if (printRes) {
        Stack.print()
      }
      "no-op".reflectCtrlWith[Unit]()
    }
    val temp: Rep[Cont[Unit]] = fun(haltK)
    evalTop(temp, main)
  }

  // stack creation and operations
  object Stack {
    def initialize(): Rep[Unit] = {
      "stack-init".reflectCtrlWith()
    }

    def pop(): Rep[Num] = {
      "stack-pop".reflectCtrlWith[Num]()
    }

    def peek: Rep[Num] = {
      "stack-peek".reflectCtrlWith()
    }

    def push(v: Rep[Num]): Rep[Unit] = {
      "stack-push".reflectCtrlWith(v)
    }

    def drop(n: Int): Rep[Unit] = {
      "stack-drop".reflectCtrlWith(n)
    }

    def print(): Rep[Unit] = {
      "stack-print".reflectCtrlWith()
    }

    def size: Rep[Int] = {
      "stack-size".reflectCtrlWith[Int]()
    }

    def reset(x: Rep[Int]): Rep[Unit] = {
      "stack-reset".reflectCtrlWith(x)
    }

    def take(n: Int): Rep[Slice] = {
      "stack-take".reflectCtrlWith(n)
    }
  }

  object Frames {
    def get(i: Int): Rep[Num] = {
      "frame-get".reflectCtrlWith(i)
    }

    def set(i: Int, v: Rep[Num]): Rep[Unit] = {
      "frame-set".reflectCtrlWith(i, v)
    }

    def pushFrame(i: Int): Rep[Unit] = {
      "frame-push".reflectCtrlWith(i)
    }

    def popFrame(): Rep[Unit] = {
      "frame-pop".reflectCtrlWith()
    }

    def putAll(args: Rep[Slice]): Rep[Unit] = {
      "frame-putAll".reflectCtrlWith(args)
    }

    def top: Rep[Frame] = {
      "frame-top".reflectCtrlWith()
    }
  }


  // call unreachable
  def unreachable(): Rep[Unit] = {
    "unreachable".reflectCtrlWith()
  }

  def info(xs: Rep[_]*): Rep[Unit] = {
    "info".reflectCtrlWith(xs: _*)
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

  // runtime Num type
  implicit class NumOps(num: Rep[Num]) {

    def toInt: Rep[Int] = "num-to-int".reflectWith[Int](num)

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
  implicit class SliceOps(slice: Rep[Slice]) {
    def reverse: Rep[Slice] = "slice-reverse".reflectWith(slice)
  }
}

trait StagedWasmScalaGen extends ScalaGenBase with SAICodeGenBase {
  override def traverse(n: Node): Unit = n match {
    case Node(_, "stack-push", List(value), _) =>
      emit("Stack.push("); shallow(value); emit(")\n")
    case Node(_, "stack-drop", List(n), _) =>
      emit("Stack.drop("); shallow(n); emit(")\n")
    case Node(_, "stack-reset", List(n), _) =>
      emit("Stack.reset("); shallow(n); emit(")\n")
    case Node(_, "stack-init", _, _) =>
      emit("Stack.initialize()\n")
    case Node(_, "stack-print", _, _) =>
      emit("Stack.print()\n")
    case Node(_, "frame-push", List(i), _) =>
      emit("Frames.pushFrame("); shallow(i); emit(")\n")
    case Node(_, "frame-pop", _, _) =>
      emit("Frames.popFrame()\n")
    case Node(_, "frame-putAll", List(args), _) =>
      emit("Frames.putAll("); shallow(args); emit(")\n")
    case Node(_, "frame-set", List(i, value), _) =>
      emit("Frames.set("); shallow(i); emit(", "); shallow(value); emit(")\n")
    case Node(_, "global-set", List(i, value), _) =>
      emit("Global.globalSet("); shallow(i); emit(", "); shallow(value); emit(")\n")
    case _ => super.traverse(n)
  }

  // code generation for pure nodes
  override def shallow(n: Node): Unit = n match {
    case Node(_, "frame-get", List(i), _) =>
      emit("Frames.get("); shallow(i); emit(")")
    case Node(_, "stack-pop", _, _) =>
      emit("Stack.pop()")
    case Node(_, "frame-pop", _, _) =>
      emit("Frames.popFrame()")
    case Node(_, "stack-peek", _, _) =>
      emit("Stack.peek")
    case Node(_, "stack-take", List(n), _) =>
      emit("Stack.take("); shallow(n); emit(")")
    case Node(_, "slice-reverse", List(slice), _) =>
      shallow(slice); emit(".reverse")
    case Node(_, "stack-size", _, _) =>
      emit("Stack.size")
    case Node(_, "global-get", List(i), _) =>
      emit("Global.globalGet("); shallow(i); emit(")")
    case Node(_, "frame-top", _, _) =>
      emit("Frames.top")
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

trait WasmToScalaCompilerDriver[A, B]
  extends SAIDriver[A, B] with StagedWasmEvaluator { q =>
  override val codegen = new StagedWasmScalaGen {
    val IR: q.type = q
    import IR._
    override def remap(m: Manifest[_]): String = {
      if (m.toString.endsWith("Stack")) "Stack"
      else if(m.toString.endsWith("Frame")) "Frame"
      else if(m.toString.endsWith("Slice")) "Slice"
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

object Stack {
  private val buffer = new scala.collection.mutable.ArrayBuffer[Num]()
  def push(v: Num): Unit = buffer.append(v)
  def pop(): Num = {
    buffer.remove(buffer.size - 1)
  }
  def peek: Num = {
    buffer.last
  }
  def size: Int = buffer.size
  def drop(n: Int): Unit = {
    buffer.remove(buffer.size - n, n)
  }
  def take(n: Int): List[Num] = {
    val xs = buffer.takeRight(n).toList
    drop(n)
    xs
  }
  def reset(size: Int): Unit = {
    info(s"Reset stack to size $size")
    while (buffer.size > size) {
      buffer.remove(buffer.size - 1)
    }
  }
  def initialize(): Unit = buffer.clear()
  def print(): Unit = {
    println("Stack: " + buffer.mkString(", "))
  }
}

  type Slice = List[Num]

  class Frame(val size: Int) {
    private val data = new Array[Num](size)
    def apply(i: Int): Num = {
      info(s"frame(${i}) is ${data(i)}")
      data(i)
    }
    def update(i: Int, v: Num): Unit = {
      info(s"set frame(${i}) to ${v}")
      data(i) = v
    }
    def putAll(xs: List[Num]): Unit = {
      for (i <- 0 until xs.size) {
        data(i) = xs(i)
      }
    }
    override def toString: String = {
      "Frame(" + data.mkString(", ") + ")"
    }
  }

  object Frames {
    private var frames = List[Frame]()
    def pushFrame(size: Int): Unit = {
      frames = new Frame(size) :: frames
    }
    def popFrame(): Unit = {
      frames = frames.tail
    }
    def top: Frame = frames.head
    def set(i: Int, v: Num): Unit = {
      top(i) = v
    }
    def get(i: Int): Num = {
      top(i)
    }
    def putAll(xs: Slice) = {
      for (i <- 0 until xs.size) {
        top(i) = xs(i)
      }
    }
  }

  object Global {
    // TODO: create global with specific size
    private val globals = new Array[Num](10)
    def globalGet(i: Int): Num = globals(i)
    def globalSet(i: Int, v: Num): Unit = globals(i) = v
  }

  def info(xs: Any*): Unit = {
    if (System.getenv("DEBUG") != null) {
      println("[INFO] " + xs.mkString(" "))
    }
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



object WasmToScalaCompiler {
  def apply(moduleInst: ModuleInstance, main: Option[String], printRes: Boolean = false): String = {
    println(s"Now compiling wasm module with entry function $main")
    val code = new WasmToScalaCompilerDriver[Unit, Unit] {
      def module: ModuleInstance = moduleInst
      def snippet(x: Rep[Unit]): Rep[Unit] = {
        evalTop(main, printRes)
      }
    }
    code.code
  }
}

trait StagedWasmCppGen extends CGenBase with CppSAICodeGenBase {
  override def remap(m: Manifest[_]): String = {
    if (m.toString.endsWith("Num")) "Num"
    else if (m.toString.endsWith("Slice")) "Slice"
    else if (m.toString.endsWith("Frame")) "Frame"
    else if (m.toString.endsWith("Stack")) "Stack"
    else if (m.toString.endsWith("Global")) "Global"
    else if (m.toString.endsWith("I32V")) "I32V"
    else if (m.toString.endsWith("I64V")) "I64V"
    else super.remap(m)
  }

  // for now, the traverse/shallow is same as the scala backend's
  override def traverse(n: Node): Unit = n match {
    case Node(_, "stack-push", List(value), _) =>
      emit("Stack.push("); shallow(value); emit(");\n")
    case Node(_, "stack-drop", List(n), _) =>
      emit("Stack.drop("); shallow(n); emit(");\n")
    case Node(_, "stack-reset", List(n), _) =>
      emit("Stack.reset("); shallow(n); emit(");\n")
    case Node(_, "stack-init", _, _) =>
      emit("Stack.initialize();\n")
    case Node(_, "stack-print", _, _) =>
      emit("Stack.print();\n")
    case Node(_, "frame-push", List(i), _) =>
      emit("Frames.pushFrame("); shallow(i); emit(");\n")
    case Node(_, "frame-pop", _, _) =>
      emit("Frames.popFrame();\n")
    case Node(_, "frame-putAll", List(args), _) =>
      emit("Frames.putAll("); shallow(args); emit(");\n")
    case Node(_, "frame-set", List(i, value), _) =>
      emit("Frames.set("); shallow(i); emit(", "); shallow(value); emit(");\n")
    case Node(_, "global-set", List(i, value), _) =>
      emit("Global.globalSet("); shallow(i); emit(", "); shallow(value); emit(");\n")
    case _ => super.traverse(n)
  }

  // code generation for pure nodes
  override def shallow(n: Node): Unit = n match {
    case Node(_, "frame-get", List(i), _) =>
      emit("Frames.get("); shallow(i); emit(")")
    case Node(_, "stack-pop", _, _) =>
      emit("Stack.pop()")
    case Node(_, "frame-pop", _, _) =>
      emit("Frames.popFrame()")
    case Node(_, "stack-peek", _, _) =>
      emit("Stack.peek")
    case Node(_, "stack-take", List(n), _) =>
      emit("Stack.take("); shallow(n); emit(")")
    case Node(_, "slice-reverse", List(slice), _) =>
      shallow(slice); emit(".reverse")
    case Node(_, "stack-size", _, _) =>
      emit("Stack.size()")
    case Node(_, "global-get", List(i), _) =>
      emit("Global.globalGet("); shallow(i); emit(")")
    case Node(_, "frame-top", _, _) =>
      emit("Frames.top()")
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


trait WasmToCppCompilerDriver[A, B] extends CppSAIDriver[A, B] with StagedWasmEvaluator { q =>
  override val codegen = new StagedWasmCppGen {
    val IR: q.type = q
    import IR._
  }
}

object WasmToCppCompiler {
  def apply(moduleInst: ModuleInstance, main: Option[String], printRes: Boolean = false): String = {
    println(s"Now compiling wasm module with entry function $main")
    val code = new WasmToCppCompilerDriver[Unit, Unit] {
      def module: ModuleInstance = moduleInst
      def snippet(x: Rep[Unit]): Rep[Unit] = {
        evalTop(main, printRes)
      }
    }
    code.code
  }
}

