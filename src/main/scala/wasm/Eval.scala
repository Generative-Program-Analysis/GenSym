package gensym.wasm.eval

import gensym.wasm.ast._
import gensym.wasm.types._
import gensym.wasm.values._
import gensym.wasm.source._
import gensym.wasm.memory._
import gensym.wasm.globals._

import scala.collection.mutable.ArrayBuffer

case class ModuleInstance(
  types: List[FuncType],
  funcs: List[FuncDef],
  memory: List[Memory],
  globals: List[Global],
  // data: List[DataInstance]

  // tables are used for JS interop, and elem is used for table initialization
  // so we don't need them for now
  // tables: List[TableInstance],
  // elem: List[ElemInstance],
)
object ModuleInstance {
  def apply(types: List[FuncType], funcs: List[FuncDef]): ModuleInstance = {
    ModuleInstance(types, funcs, List(Memory()), List())
  }
}

// TODO: use mutable data structures?
case class Frame(module: ModuleInstance, var locals: List[Value])

abstract class AdminInstr
case class Plain(instr: Instr) extends AdminInstr
case class Invoke(func: Int) extends AdminInstr
// case class Returning(stack: List[Value]) extends AdminInstr
case class Trapping(msg: String) extends AdminInstr
// case class Breaking(n: Int, stack: List[Value]) extends AdminInstr
case class FrameInstr(n: Int, frame: Frame, code: Code) extends AdminInstr
case class Label(n: Int, instrs: List[Instr], code: Code) extends AdminInstr

case class Code(stack: List[Value], adminInstrs: List[AdminInstr])

case class Continue(stack: List[Value]) extends EvalResult
case class Breaking(n: Int, stack: List[Value]) extends EvalResult
case class Returning(stack: List[Value]) extends EvalResult
abstract class EvalResult {
  def onContinue(f: List[Value] => EvalResult): EvalResult = {
    this match {
      case Continue(stack) => f(stack)
      case _ => this
    }
  }
}

case class Config(var frame: Frame, stackBudget: Int) {
  def evalBinOp(op: BinOp, lhs: Value, rhs: Value) = op match {
    case BinOp.Int(Add) => (lhs, rhs) match {
      case (I32(v1), I32(v2)) => I32(v1 + v2)
      case (I64(v1), I64(v2)) => I64(v1 + v2)
      case _ => throw new Exception("Invalid types")
    }
    case BinOp.Int(Mul) => (lhs, rhs) match {
      case (I32(v1), I32(v2)) => I32(v1 * v2)
      case (I64(v1), I64(v2)) => I64(v1 * v2)
      case _ => throw new Exception("Invalid types")
    }
    case BinOp.Int(Sub) => (lhs, rhs) match {
      case (I32(v1), I32(v2)) => I32(v1 - v2)
      case (I64(v1), I64(v2)) => I64(v1 - v2)
      case _ => throw new Exception("Invalid types")
    }
    case BinOp.Int(Shl) => (lhs, rhs) match {
      case (I32(v1), I32(v2)) => I32(v1 << v2)
      case (I64(v1), I64(v2)) => I64(v1 << v2)
      case _ => throw new Exception("Invalid types")
    }
    case BinOp.Int(ShrU) => (lhs, rhs) match {
      case (I32(v1), I32(v2)) => I32(v1 >>> v2)
      case (I64(v1), I64(v2)) => I64(v1 >>> v2)
      case _ => throw new Exception("Invalid types")
    }
  }

  def evalUnaryOp(op: UnaryOp, value: Value) = op match {
    case UnaryOp.Int(Clz) => value match {
      case I32(v) => I32(Integer.numberOfLeadingZeros(v))
      case I64(v) => I64(java.lang.Long.numberOfLeadingZeros(v))
      case _ => throw new Exception("Invalid types")
    }
    case UnaryOp.Int(Ctz) => value match {
      case I32(v) => I32(Integer.numberOfTrailingZeros(v))
      case I64(v) => I64(java.lang.Long.numberOfTrailingZeros(v))
      case _ => throw new Exception("Invalid types")
    }
    case UnaryOp.Int(Popcnt) => value match {
      case I32(v) => I32(Integer.bitCount(v))
      case I64(v) => I64(java.lang.Long.bitCount(v))
      case _ => throw new Exception("Invalid types")
    }
    case _ => ???
  }

  // TODO: double check (copilot generated)
  def evalRelOp(op: RelOp, lhs: Value, rhs: Value) = op match {
    case RelOp.Int(Eq) => (lhs, rhs) match {
      case (I32(v1), I32(v2)) => I32(if (v1 == v2) 1 else 0)
      case (I64(v1), I64(v2)) => I32(if (v1 == v2) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
    case RelOp.Int(Ne) => (lhs, rhs) match {
      case (I32(v1), I32(v2)) => I32(if (v1 != v2) 1 else 0)
      case (I64(v1), I64(v2)) => I32(if (v1 != v2) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
    case RelOp.Int(LtS) => (lhs, rhs) match {
      case (I32(v1), I32(v2)) => I32(if (v1 < v2) 1 else 0)
      case (I64(v1), I64(v2)) => I32(if (v1 < v2) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
    case RelOp.Int(LtU) => (lhs, rhs) match {
      case (I32(v1), I32(v2)) => I32(if (Integer.compareUnsigned(v1, v2) < 0) 1 else 0)
      case (I64(v1), I64(v2)) => I32(if (java.lang.Long.compareUnsigned(v1, v2) < 0) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
    case RelOp.Int(GtS) => (lhs, rhs) match {
      case (I32(v1), I32(v2)) => I32(if (v1 > v2) 1 else 0)
      case (I64(v1), I64(v2)) => I32(if (v1 > v2) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
    case RelOp.Int(GtU) => (lhs, rhs) match {
      case (I32(v1), I32(v2)) => I32(if (Integer.compareUnsigned(v1, v2) > 0) 1 else 0)
      case (I64(v1), I64(v2)) => I32(if (java.lang.Long.compareUnsigned(v1, v2) > 0) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
    case RelOp.Int(LeS) => (lhs, rhs) match {
      case (I32(v1), I32(v2)) => I32(if (v1 <= v2) 1 else 0)
      case (I64(v1), I64(v2)) => I32(if (v1 <= v2) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
    case RelOp.Int(LeU) => (lhs, rhs) match {
      case (I32(v1), I32(v2)) => I32(if (Integer.compareUnsigned(v1, v2) <= 0) 1 else 0)
      case (I64(v1), I64(v2)) => I32(if (java.lang.Long.compareUnsigned(v1, v2) <= 0) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
    case RelOp.Int(GeS) => (lhs, rhs) match {
      case (I32(v1), I32(v2)) => I32(if (v1 >= v2) 1 else 0)
      case (I64(v1), I64(v2)) => I32(if (v1 >= v2) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
    case RelOp.Int(GeU) => (lhs, rhs) match {
      case (I32(v1), I32(v2)) => I32(if (Integer.compareUnsigned(v1, v2) >= 0) 1 else 0)
      case (I64(v1), I64(v2)) => I32(if (java.lang.Long.compareUnsigned(v1, v2) >= 0) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
  }

  def evalTestOp(op: TestOp, value: Value) = op match {
    case TestOp.Int(Eqz) => value match {
      case I32(v) => I32(if (v == 0) 1 else 0)
      case I64(v) => I32(if (v == 0) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
  }

  // def evalCvtOp(op: CvtOp, value: Value) = ???

  def memOob(frame: Frame, memoryIndex: Int, offset: Int, size: Int) = {
    val memory = frame.module.memory(memoryIndex)
    val end = offset + size
    end > memory.size
  }

  def eval(stack: List[Value], instrs: List[Instr]): EvalResult = {
    if (instrs.isEmpty) return Continue(stack)

    println(s"Stack: $stack, instr: ${instrs.head}, locals: ${frame.locals}")
    instrs.head match {
      // Parametric Instructions
      case Drop => stack match {
        case _ :: newStack => this.eval(newStack, instrs.tail)
        case _ => throw new Exception("Invalid stack")
      }

      case Select(_) => stack match {
        case I32(cond) :: v2 :: v1 :: newStack => {
          val value = if (cond == 0) v1 else v2
          this.eval(value :: newStack, instrs.tail)
        }
        case _ => throw new Exception("Invalid stack")
      }

      // Variable Instructions
      // https://www.w3.org/TR/wasm-core-2/exec/instructions.html#variable-instructions
      case LocalGet(local) => this.eval(frame.locals(local) :: stack, instrs.tail)
      case LocalSet(local) => stack match {
        case value :: newStack => {
          frame.locals = frame.locals.updated(local, value)
          this.eval(newStack, instrs.tail)
        }
        case _ => throw new Exception("Invalid stack")
      }
      case LocalTee(local) => stack match {
        case value :: newStack => {
          frame.locals = frame.locals.updated(local, value)
          this.eval(value :: newStack, instrs.tail)
        }
        case _ => throw new Exception("Invalid stack")
      }
      case GlobalGet(global) => this.eval(frame.module.globals(global).value :: stack, instrs.tail)
      case GlobalSet(global) => stack match {
        case value :: newStack => {
          frame.module.globals(global).tipe match {
            case GlobalType(tipe, true) if value.tipe == tipe => frame.module.globals(global).value = value
            case GlobalType(_, true) => throw new Exception("Invalid type")
            case _ => throw new Exception("Cannot set immutable global")
          }
          this.eval(newStack, instrs.tail)
        }
        case _ => throw new Exception("Invalid stack")
      }

      // Memory Instructions
      // https://www.w3.org/TR/wasm-core-2/exec/instructions.html#memory-instructions
      case MemorySize => this.eval(I32(frame.module.memory.head.size) :: stack, instrs.tail)

      // https://github.com/WebAssembly/spec/blob/main/interpreter/exec/eval.ml#L406
      // https://github.com/WebAssembly/spec/blob/main/interpreter/runtime/memory.ml#L50
      // https://www.w3.org/TR/wasm-core-2/exec/instructions.html#xref-syntax-instructions-syntax-instr-memory-mathsf-memory-grow
      case MemoryGrow => stack match {
        case I32(delta) :: newStack => {
          val mem = frame.module.memory.head
          val oldSize = mem.size
          try {
            mem.grow(delta)
            this.eval(I32(oldSize) :: newStack, instrs.tail)
          } catch {
            case e: Exception => this.eval(I32(-1) :: newStack, instrs.tail)
          }
        }
        case _ => throw new Exception("Invalid stack")
      }

      case MemoryFill => stack match {
        case I32(value) :: I32(offset) :: I32(size) :: newStack => {
          if (memOob(frame, 0, offset, size)) {
            throw new Exception("Out of bounds memory access")
          } else {
            val mem = frame.module.memory.head
            mem.fill(offset, size, value.toByte)
            this.eval(newStack, instrs.tail)
          }
        }
        case _ => throw new Exception("Invalid stack")
      }

      case MemoryCopy => stack match {
        case I32(n) :: I32(src) :: I32(dest) :: newStack => {
          if (memOob(frame, 0, src, n) || memOob(frame, 0, dest, n)) {
            throw new Exception("Out of bounds memory access")
          } else {
            val mem = frame.module.memory.head
            mem.copy(dest, src, n)
            this.eval(newStack, instrs.tail)
          }
        }
        case _ => throw new Exception("Invalid stack")
      }

      // Numeric Instructions
      // https://www.w3.org/TR/wasm-core-2/exec/instructions.html#numeric-instructions
      case Const(num) => this.eval(num :: stack, instrs.tail)
      case Binary(op) => stack match {
        case v2 :: v1 :: rest => this.eval(evalBinOp(op, v1, v2) :: rest, instrs.tail)
        case _ => throw new Exception("Invalid stack")
      }
      case Unary(op) => stack match {
        case value :: rest => this.eval(evalUnaryOp(op, value) :: rest, instrs.tail)
        case _ => throw new Exception("Invalid stack")
      }
      case Compare(op) => stack match {
        case v2 :: v1 :: rest => this.eval(evalRelOp(op, v1, v2) :: rest, instrs.tail)
        case _ => throw new Exception("Invalid stack")
      }
      case Test(testOp) => stack match {
        case value :: rest => this.eval(evalTestOp(testOp, value) :: rest, instrs.tail)
        case _ => throw new Exception("Invalid stack")
      }
      case Store(StoreOp(align, offset, tipe, None)) => stack match {
        case I32(value) :: I32(addr) :: newStack => {
          val mem = frame.module.memory(0)
          mem.storeInt(addr + offset, value)
          this.eval(newStack, instrs.tail)
        }
        case _ => throw new Exception("Invalid stack")
      }
      case Load(LoadOp(align, offset, tipe, None, None)) => stack match {
        case I32(addr) :: newStack => {
          val mem = frame.module.memory(0)
          val value = mem.loadInt(addr + offset)
          this.eval(I32(value) :: newStack, instrs.tail)
        }
        case _ => throw new Exception("Invalid stack")
      }

      // Control Instructions
      // https://www.w3.org/TR/wasm-core-2/exec/instructions.html#numeric-instructions
      case Nop => this.eval(stack, instrs.tail)
      case Unreachable => throw new Exception("Unreachable")
      case Block(blockTy, blockInstrs) => {
        val funcType = blockTy.toFuncType(frame.module)
        evalBlock(funcType.out.length, blockInstrs.toList).onContinue { retStack =>
          this.eval(retStack ++ stack, instrs.tail)
        }
      }
      case Loop(blockTy, loopInstrs) => {
        val funcType = blockTy.toFuncType(frame.module)
        evalBlock(funcType.out.length, loopInstrs.toList).onContinue { retStack =>
          this.eval(retStack ++ stack, instrs) // instead of instrs.tail
        }
      }
      case If(blockTy, thenInstrs, elseInstrs) => stack match {
        case I32(cond) :: newStack => {
          val condInstrs = if (cond == 0) elseInstrs else thenInstrs
          val funcType = blockTy.toFuncType(frame.module)
          evalBlock(funcType.out.length, condInstrs.toList).onContinue { retStack =>
            this.eval(retStack ++ newStack, instrs.tail)
          }
        }
        case _ => throw new Exception("Invalid stack")
      }
      case Br(label) => {
        Breaking(label, stack)
      }
      case BrIf(label) => stack match {
        case I32(0) :: newStack => {
          this.eval(newStack, instrs.tail)
        }
        case I32(_) :: newStack => {
          Breaking(label, newStack)
        }
        case _ => throw new Exception("Invalid stack")
      }
      case Return => {
        Returning(stack)
      }
      case Call(func) => {
        val FuncDef(_, funcType, locals, body) = frame.module.funcs(func)
        val args = stack.take(funcType.inps.length).reverse
        val newStack = stack.drop(funcType.inps.length)
        
        val frameLocals = args ++ locals.map(_ => I32(0))
        val newFrame = Frame(frame.module, frameLocals)
        evalFunc(args, newFrame, funcType, body.toList).onContinue { retStack =>
          this.eval(retStack ++ newStack, instrs.tail)
        }
      }
    }
  }

  // basically equates to step with FrameInstr on top in the previous impl
  def evalFunc(args: List[Value], nframe: Frame, funcType: FuncType, instrs: List[Instr]): EvalResult = {
    val ret = this.copy(frame = nframe).evalBlock(funcType.out.length, instrs)

    ret match {
      case Returning(retStack) => Continue(retStack.take(funcType.out.length))
      case _ => ret
    }
  }

  // basically equates to step with Label on top in the previous impl
  def evalBlock(outs: Int, instrs: List[Instr]): EvalResult = {
    val ret = this.copy().eval(List(), instrs)
    ret match {
      case Breaking(0, breakStack) => Continue(breakStack.take(outs))
      case Breaking(n, breakStack) => Breaking(n - 1, breakStack)
      case _ => ret
    }
  }
}
