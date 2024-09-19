package gensym.wasm.miniwasm

import gensym.wasm.ast._
import gensym.wasm.source._
import gensym.wasm.memory._

import scala.collection.mutable.ArrayBuffer

case class ModuleInstance(
    types: List[FuncType],
    funcs: List[FuncBodyDef],
    memory: List[RTMemory] = List(RTMemory()),
    globals: List[RTGlobal] = List(),
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
}

case class Frame(module: ModuleInstance, locals: ArrayBuffer[Value])

object Evaluator {
  import Primtives._

  type RetCont = List[Value] => Unit
  type Cont = List[Value] => Unit

  def eval(insts: List[Instr],
           stack: List[Value],
           frame: Frame,
           ret: Int, // Indicate the number of contexts/continuations to pop
           trail: List[Cont]): Unit = {
    if (insts.isEmpty) return trail.head(stack)

    val inst = insts.head
    val rest = insts.tail

    inst match {
      case Drop => eval(rest, stack.tail, frame, ret, trail)
      case Select(_) =>
        val I32V(cond) :: v2 :: v1 :: newStack = stack
        val value = if (cond == 0) v1 else v2
        eval(rest, value :: newStack, frame, ret, trail)
      case LocalGet(i) =>
        eval(rest, frame.locals(i) :: stack, frame, ret, trail)
      case LocalSet(i) =>
        val value :: newStack = stack
        frame.locals(i) = value
        eval(rest, newStack, frame, ret, trail)
      case LocalTee(i) =>
        val value :: newStack = stack
        frame.locals(i) = value
        eval(rest, stack, frame, ret, trail)
      case GlobalGet(i) =>
        eval(rest, frame.module.globals(i).value :: stack, frame, ret, trail)
      case GlobalSet(i) =>
        val value :: newStack = stack
        frame.module.globals(i).ty match {
          case GlobalType(tipe, true) if value.tipe == tipe =>
            frame.module.globals(i).value = value
          case GlobalType(_, true) => throw new Exception("Invalid type")
          case _                   => throw new Exception("Cannot set immutable global")
        }
        eval(rest, newStack, frame, ret, trail)
      case MemorySize =>
        eval(rest,
             I32V(frame.module.memory.head.size) :: stack,
             frame,
             ret,
             trail)
      case MemoryGrow =>
        val I32V(delta) :: newStack = stack
        val mem = frame.module.memory.head
        val oldSize = mem.size
        mem.grow(delta) match {
          case Some(e) => eval(rest, I32V(-1) :: newStack, frame, ret, trail)
          case _       => eval(rest, I32V(oldSize) :: newStack, frame, ret, trail)
        }
      case MemoryFill =>
        val I32V(value) :: I32V(offset) :: I32V(size) :: newStack = stack
        if (memOutOfBound(frame, 0, offset, size))
          throw new Exception("Out of bounds memory access") // GW: turn this into a `trap`?
        else {
          frame.module.memory.head.fill(offset, size, value.toByte)
          eval(rest, newStack, frame, ret, trail)
        }
      case MemoryCopy =>
        val I32V(n) :: I32V(src) :: I32V(dest) :: newStack = stack
        if (memOutOfBound(frame, 0, src, n) || memOutOfBound(frame, 0, dest, n))
          throw new Exception("Out of bounds memory access")
        else {
          frame.module.memory.head.copy(dest, src, n)
          eval(rest, newStack, frame, ret, trail)
        }
      case Const(n) => eval(rest, n :: stack, frame, ret, trail)
      case Binary(op) =>
        val v2 :: v1 :: newStack = stack
        eval(rest, evalBinOp(op, v1, v2) :: newStack, frame, ret, trail)
      case Unary(op) =>
        val v :: newStack = stack
        eval(rest, evalUnaryOp(op, v) :: newStack, frame, ret, trail)
      case Compare(op) =>
        val v2 :: v1 :: newStack = stack
        eval(rest, evalRelOp(op, v1, v2) :: newStack, frame, ret, trail)
      case Test(op) =>
        val v :: newStack = stack
        eval(rest, evalTestOp(op, v) :: newStack, frame, ret, trail)
      case Store(StoreOp(align, offset, ty, None)) =>
        val I32V(v) :: I32V(addr) :: newStack = stack
        frame.module.memory(0).storeInt(addr + offset, v)
        eval(rest, newStack, frame, ret, trail)
      case Load(LoadOp(align, offset, ty, None, None)) =>
        val I32V(addr) :: newStack = stack
        val value = frame.module.memory(0).loadInt(addr + offset)
        eval(rest, I32V(value) :: newStack, frame, ret, trail)
      case Nop =>
        eval(rest, stack, frame, ret, trail)
      case Unreachable => throw new RuntimeException("Unreachable")
      case Block(ty, inner) =>
        val k: Cont = (retStack) =>
          eval(rest, retStack.take(ty.toList.size) ++ stack, frame, ret, trail)
        // TODO: block can take inputs too
        eval(inner, List(), frame, ret+1, k :: trail)
      case Loop(ty, inner) =>
        val k: Cont = (retStack) =>
          eval(insts, retStack.take(ty.toList.size) ++ stack, frame, ret, trail)
        eval(inner, List(), frame, ret+1, k :: trail)
      case If(ty, thn, els) =>
        val I32V(cond) :: newStack = stack
        val inner = if (cond == 0) thn else els
        val k: Cont = (retStack) =>
          eval(rest,
               retStack.take(ty.toList.size) ++ newStack,
               frame,
               ret,
               trail)
        eval(inner, List(), frame, ret+1, k :: trail)
      case Br(label) =>
        trail(label)(stack)
      case BrIf(label) =>
        val I32V(cond) :: newStack = stack
        if (cond == 0) eval(rest, newStack, frame, ret, trail)
        else trail(label)(newStack)
      case Return => trail(ret)(stack)
      case Call(f) =>
        val FuncBodyDef(ty, _, locals, body) = frame.module.funcs(f)
        val args = stack.take(ty.inps.size).reverse
        val newStack = stack.drop(ty.inps.size)
        val frameLocals = args ++ locals.map(_ => I32V(0)) // GW: always I32? or depending on their types?
        val newFrame = Frame(frame.module, ArrayBuffer(frameLocals: _*))
        val newK: RetCont = (retStack) =>
          eval(rest, retStack.take(ty.out.size) ++ newStack, frame, ret, trail)
        eval(body, List(), newFrame, 0, newK :: trail)
      case _ =>
        println(inst)
        throw new Exception(s"instruction $inst not implemented")
    }
  }
}
