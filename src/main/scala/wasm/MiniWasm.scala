package gensym.wasm.miniwasm

import gensym.wasm.ast._
import gensym.wasm.source._
import gensym.wasm.memory._

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap

case class Trap() extends Exception

case class ModuleInstance(
    types: List[FuncType],
    funcs: HashMap[Int, WIR],
    memory: List[RTMemory] = List(RTMemory()),
    globals: List[RTGlobal] = List(),
    exports: List[Export] = List()
)

object Primtives {
  def evalBinOp(op: BinOp, lhs: Value, rhs: Value): Value = op match {
    case Add(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) => I32V(v1 + v2)
        case (I64V(v1), I64V(v2)) => I64V(v1 + v2)
        case _                    => throw new Exception("Invalid types")
      }
    case Mul(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) => I32V(v1 * v2)
        case (I64V(v1), I64V(v2)) => I64V(v1 * v2)
        case _                    => throw new Exception("Invalid types")
      }
    case Sub(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) => I32V(v1 - v2)
        case (I64V(v1), I64V(v2)) => I64V(v1 - v2)
        case _                    => throw new Exception("Invalid types")
      }
    case Shl(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) => I32V(v1 << v2)
        case (I64V(v1), I64V(v2)) => I64V(v1 << v2)
        case _                    => throw new Exception("Invalid types")
      }
    case ShrU(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) => I32V(v1 >>> v2)
        case (I64V(v1), I64V(v2)) => I64V(v1 >>> v2)
        case _                    => throw new Exception("Invalid types")
      }
    case And(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) => I32V(v1 & v2)
        case (I64V(v1), I64V(v2)) => I64V(v1 & v2)
        case _                    => throw new Exception("Invalid types")
      }
    case _ => ???
  }
  def evalUnaryOp(op: UnaryOp, value: Value) = op match {
    case Clz(_) =>
      value match {
        case I32V(v) => I32V(Integer.numberOfLeadingZeros(v))
        case I64V(v) => I64V(java.lang.Long.numberOfLeadingZeros(v))
        case _       => throw new Exception("Invalid types")
      }
    case Ctz(_) =>
      value match {
        case I32V(v) => I32V(Integer.numberOfTrailingZeros(v))
        case I64V(v) => I64V(java.lang.Long.numberOfTrailingZeros(v))
        case _       => throw new Exception("Invalid types")
      }
    case Popcnt(_) =>
      value match {
        case I32V(v) => I32V(Integer.bitCount(v))
        case I64V(v) => I64V(java.lang.Long.bitCount(v))
        case _       => throw new Exception("Invalid types")
      }
    case _ => ???
  }

  // TODO: double check (copilot generated)
  def evalRelOp(op: RelOp, lhs: Value, rhs: Value) = op match {
    case Eq(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) => I32V(if (v1 == v2) 1 else 0)
        case (I64V(v1), I64V(v2)) => I32V(if (v1 == v2) 1 else 0)
        case _                    => throw new Exception("Invalid types")
      }
    case Ne(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) => I32V(if (v1 != v2) 1 else 0)
        case (I64V(v1), I64V(v2)) => I32V(if (v1 != v2) 1 else 0)
        case _                    => throw new Exception("Invalid types")
      }
    case LtS(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) => I32V(if (v1 < v2) 1 else 0)
        case (I64V(v1), I64V(v2)) => I32V(if (v1 < v2) 1 else 0)
        case _                    => throw new Exception("Invalid types")
      }
    case LtU(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) =>
          I32V(if (Integer.compareUnsigned(v1, v2) < 0) 1 else 0)
        case (I64V(v1), I64V(v2)) =>
          I32V(if (java.lang.Long.compareUnsigned(v1, v2) < 0) 1 else 0)
        case _ => throw new Exception("Invalid types")
      }
    case GtS(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) => I32V(if (v1 > v2) 1 else 0)
        case (I64V(v1), I64V(v2)) => I32V(if (v1 > v2) 1 else 0)
        case _                    => throw new Exception("Invalid types")
      }
    case GtU(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) =>
          I32V(if (Integer.compareUnsigned(v1, v2) > 0) 1 else 0)
        case (I64V(v1), I64V(v2)) =>
          I32V(if (java.lang.Long.compareUnsigned(v1, v2) > 0) 1 else 0)
        case _ => throw new Exception("Invalid types")
      }
    case LeS(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) => I32V(if (v1 <= v2) 1 else 0)
        case (I64V(v1), I64V(v2)) => I32V(if (v1 <= v2) 1 else 0)
        case _                    => throw new Exception("Invalid types")
      }
    case LeU(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) =>
          I32V(if (Integer.compareUnsigned(v1, v2) <= 0) 1 else 0)
        case (I64V(v1), I64V(v2)) =>
          I32V(if (java.lang.Long.compareUnsigned(v1, v2) <= 0) 1 else 0)
        case _ => throw new Exception("Invalid types")
      }
    case GeS(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) => I32V(if (v1 >= v2) 1 else 0)
        case (I64V(v1), I64V(v2)) => I32V(if (v1 >= v2) 1 else 0)
        case _                    => throw new Exception("Invalid types")
      }
    case GeU(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) =>
          I32V(if (Integer.compareUnsigned(v1, v2) >= 0) 1 else 0)
        case (I64V(v1), I64V(v2)) =>
          I32V(if (java.lang.Long.compareUnsigned(v1, v2) >= 0) 1 else 0)
        case _ => throw new Exception("Invalid types")
      }
  }

  def evalTestOp(op: TestOp, value: Value) = op match {
    case Eqz(_) =>
      value match {
        case I32V(v) => I32V(if (v == 0) 1 else 0)
        case I64V(v) => I32V(if (v == 0) 1 else 0)
        case _       => throw new Exception("Invalid types")
      }
  }

  def memOutOfBound(frame: Frame, memoryIndex: Int, offset: Int, size: Int) = {
    val memory = frame.module.memory(memoryIndex)
    offset + size > memory.size
  }

  def zero(t: ValueType): Value = t match {
    case NumType(kind) => kind match {
      case I32Type => I32V(0)
      case I64Type => I64V(0)
      case F32Type => F32V(0)
      case F64Type => F64V(0)
    }
    case VecType(kind) => ???
    case RefType(kind) => ???
  }
}

case class Frame(module: ModuleInstance, locals: ArrayBuffer[Value])

object Evaluator {
  import Primtives._

  type Cont[A] = List[Value] => A

  def eval[Ans](insts: List[Instr],
                stack: List[Value],
                frame: Frame,
                kont: Cont[Ans],
                trail: List[Cont[Ans]],
                ret: Int): Ans = {
    if (insts.isEmpty) return kont(stack)

    val inst = insts.head
    val rest = insts.tail

    // println(s"inst: ${inst} \t | ${frame.locals} | ${stack.reverse}" )

    inst match {
      case Drop => eval(rest, stack.tail, frame, kont, trail, ret)
      case Select(_) =>
        val I32V(cond) :: v2 :: v1 :: newStack = stack
        val value = if (cond == 0) v1 else v2
        eval(rest, value :: newStack, frame, kont, trail, ret)
      case LocalGet(i) =>
        eval(rest, frame.locals(i) :: stack, frame, kont, trail, ret)
      case LocalSet(i) =>
        val value :: newStack = stack
        frame.locals(i) = value
        eval(rest, newStack, frame, kont, trail, ret)
      case LocalTee(i) =>
        val value :: newStack = stack
        frame.locals(i) = value
        eval(rest, stack, frame, kont, trail, ret)
      case GlobalGet(i) =>
        eval(rest, frame.module.globals(i).value :: stack, frame, kont, trail, ret)
      case GlobalSet(i) =>
        val value :: newStack = stack
        frame.module.globals(i).ty match {
          case GlobalType(tipe, true) if value.tipe == tipe =>
            frame.module.globals(i).value = value
          case GlobalType(_, true) => throw new Exception("Invalid type")
          case _                   => throw new Exception("Cannot set immutable global")
        }
        eval(rest, newStack, frame, kont, trail, ret)
      case MemorySize =>
        eval(rest, I32V(frame.module.memory.head.size) :: stack, frame, kont, trail, ret)
      case MemoryGrow =>
        val I32V(delta) :: newStack = stack
        val mem = frame.module.memory.head
        val oldSize = mem.size
        mem.grow(delta) match {
          case Some(e) =>
            eval(rest, I32V(-1) :: newStack, frame, kont, trail, ret)
          case _ =>
            eval(rest, I32V(oldSize) :: newStack, frame, kont, trail, ret)
        }
      case MemoryFill =>
        val I32V(value) :: I32V(offset) :: I32V(size) :: newStack = stack
        if (memOutOfBound(frame, 0, offset, size))
          throw new Exception("Out of bounds memory access") // GW: turn this into a `trap`?
        else {
          frame.module.memory.head.fill(offset, size, value.toByte)
          eval(rest, newStack, frame, kont, trail, ret)
        }
      case MemoryCopy =>
        val I32V(n) :: I32V(src) :: I32V(dest) :: newStack = stack
        if (memOutOfBound(frame, 0, src, n) || memOutOfBound(frame, 0, dest, n))
          throw new Exception("Out of bounds memory access")
        else {
          frame.module.memory.head.copy(dest, src, n)
          eval(rest, newStack, frame, kont, trail, ret)
        }
      case Const(n) => eval(rest, n :: stack, frame, kont, trail, ret)
      case Binary(op) =>
        val v2 :: v1 :: newStack = stack
        eval(rest, evalBinOp(op, v1, v2) :: newStack, frame, kont, trail, ret)
      case Unary(op) =>
        val v :: newStack = stack
        eval(rest, evalUnaryOp(op, v) :: newStack, frame, kont, trail, ret)
      case Compare(op) =>
        val v2 :: v1 :: newStack = stack
        eval(rest, evalRelOp(op, v1, v2) :: newStack, frame, kont, trail, ret)
      case Test(op) =>
        val v :: newStack = stack
        eval(rest, evalTestOp(op, v) :: newStack, frame, kont, trail, ret)
      case Store(StoreOp(align, offset, ty, None)) =>
        val I32V(v) :: I32V(addr) :: newStack = stack
        frame.module.memory(0).storeInt(addr + offset, v)
        eval(rest, newStack, frame, kont, trail, ret)
      case Load(LoadOp(align, offset, ty, None, None)) =>
        val I32V(addr) :: newStack = stack
        val value = frame.module.memory(0).loadInt(addr + offset)
        eval(rest, I32V(value) :: newStack, frame, kont, trail, ret)
      case Nop =>
        eval(rest, stack, frame, kont, trail, ret)
      case Unreachable => throw Trap()
      case Block(ty, inner) =>
        val (inputs, restStack) = stack.splitAt(ty.inps.size)
        val restK: Cont[Ans] = (retStack) =>
          eval(rest, retStack ++ restStack, frame, kont, trail, ret)
        eval(inner, inputs, frame, restK, restK :: trail, ret + 1)
      case Loop(ty, inner) =>
        // We construct two continuations, one for the break (to the begining of the loop),
        // and one for fall-through to the next instruction following the syntactic structure
        // of the program.
        val (inputs, restStack) = stack.splitAt(ty.inps.size)
        val restK: Cont[Ans] = (retStack) => eval(rest, retStack ++ restStack, frame, kont, trail, ret)

        def loop(retStack: List[Value]): Ans = {
          val k: Cont[Ans] = (retStack) => loop(retStack) // k is just same as loop
          eval(inner, retStack, frame, restK, k :: trail, ret + 1)
        }

        loop(inputs)
      case If(ty, thn, els) =>
        val I32V(cond) :: newStack = stack
        val inner = if (cond != 0) thn else els
        val (inputs, restStack) = newStack.splitAt(ty.inps.size)
        val restK: Cont[Ans] = (retStack) =>
          eval(rest, retStack ++ restStack, frame, kont, trail, ret)
        eval(inner, inputs, frame, restK, restK :: trail, ret + 1)
      case Br(label) =>
        trail(label)(stack)
      case BrIf(label) =>
        val I32V(cond) :: newStack = stack
        if (cond != 0) trail(label)(newStack)
        else eval(rest, newStack, frame, kont, trail, ret)
      case BrTable(labels, default) =>
        val I32V(cond) :: newStack = stack
        val goto = if (cond < labels.length) labels(cond) else default
        trail(goto)(newStack)
      case Return => trail(ret)(stack)
      case Call(f) if frame.module.funcs(f).isInstanceOf[FuncDef] =>
        val FuncDef(_, FuncBodyDef(ty, _, locals, body)) = frame.module.funcs(f)
        val args = stack.take(ty.inps.size).reverse
        val newStack = stack.drop(ty.inps.size)
        val frameLocals = args ++ locals.map(zero(_))
        val newFrame = Frame(frame.module, ArrayBuffer(frameLocals: _*))
        val newK: Cont[Ans] = (retStack) => eval(rest, retStack.take(ty.out.size) ++ newStack, frame, kont, trail, ret)
        // We push newK on the trail since function creates a new block to escape
        // (more or less like `return`)
        eval(body, List(), newFrame, newK, List(newK), 0)
      case Call(f) if frame.module.funcs(f).isInstanceOf[Import] =>
        frame.module.funcs(f) match {
          case Import("console", "log", _) =>
            //println(s"[DEBUG] current stack: $stack")
            val I32V(v) :: newStack = stack
            println(v)
            eval(rest, newStack, frame, kont, trail, ret)
          case f => throw new Exception(s"Unknown import $f")
        }
      case _ =>
        println(inst)
        throw new Exception(s"instruction $inst not implemented")
    }
  }

  // If `main` is given, then we use that function as the entry point of the program;
  // otherwise, we look up the top-level `start` instruction to locate the entry point.
  def evalTop[Ans](module: Module, halt: Cont[Ans], main: Option[String] = None): Ans = {
    val instrs = main match {
      case Some(func_name) =>
        module.definitions.flatMap({
          case Export(`func_name`, ExportFunc(fid)) =>
            println(s"Entering function $main")
            module.funcEnv(fid) match {
              case FuncDef(_, FuncBodyDef(_, _, _, body)) => body
              case _ =>
                throw new Exception("Entry function has no concrete body")
            }
          case _ => List()
        })
      case None =>
        module.definitions.flatMap({
          case Start(id) =>
            println(s"Entering unnamed function $id")
            module.funcEnv(id) match {
              case FuncDef(_, FuncBodyDef(_, _, _, body)) => body
              case _ =>
                throw new Exception("Entry function has no concrete body")
            }
          case _ => List()
        })
    }

    if (instrs.isEmpty) {
      println("Warning: nothing is executed")
    }

    val types = List()
    val funcs = module.definitions
      .collect({
        case FuncDef(_, fndef @ FuncBodyDef(_, _, _, _)) => fndef
      })
      .toList

    val globals = module.definitions
      .collect({
        case Global(_, GlobalValue(ty, e)) =>
          (e.head) match {
            case Const(c) => RTGlobal(ty, c)
            // Q: What is the default behavior if case in non-exhaustive
            case _ => ???
          }
      })
      .toList

    // TODO: correct the behavior for memory
    val memory = module.definitions
      .collect({
        case Memory(id, MemoryType(min, max_opt)) =>
          RTMemory(min, max_opt)
      })
      .toList

    val moduleInst = ModuleInstance(types, module.funcEnv, memory, globals)

    Evaluator.eval(instrs, List(), Frame(moduleInst, ArrayBuffer(I32V(0))), halt, List(halt), 0)
  }

  def evalTop(m: Module): Unit = evalTop(m, stack => ())
}
