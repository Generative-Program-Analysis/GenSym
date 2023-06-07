package gensym.wasm.eval

import gensym.wasm.ast._
import gensym.wasm.source._
import gensym.wasm.memory._

import scala.collection.mutable.ArrayBuffer

case class ModuleInstance(
  types: List[FuncType],
  funcs: List[FuncBodyDef],
  memory: List[Memory],
  globals: List[Global],
  // data: List[DataInstance]

  // tables are used for JS interop, and elem is used for table initialization
  // so we don't need them for now
  // tables: List[TableInstance],
  // elem: List[ElemInstance],
)
object ModuleInstance {
  def apply(types: List[FuncType], funcs: List[FuncBodyDef]): ModuleInstance = {
    ModuleInstance(types, funcs, List(Memory()), List())
  }
}

// TODO: use mutable data structures?
case class Frame(module: ModuleInstance, var locals: List[Value])

abstract class AdminInstr
case class Plain(instr: Instr) extends AdminInstr
case class Invoke(func: Int) extends AdminInstr
case class Returning(stack: List[Value]) extends AdminInstr
case class Trapping(msg: String) extends AdminInstr
case class Breaking(n: Int, stack: List[Value]) extends AdminInstr
case class FrameInstr(n: Int, frame: Frame, code: Code) extends AdminInstr
case class Label(n: Int, instrs: List[Instr], code: Code) extends AdminInstr

case class Code(stack: List[Value], adminInstrs: List[AdminInstr])
case class Config(var frame: Frame, code: Code, stackBudget: Int) {
  def evalBinOp(op: BinOp, lhs: Value, rhs: Value): Value = op match {
    case Add(_) => (lhs, rhs) match {
      case (I32V(v1), I32V(v2)) => I32V(v1 + v2)
      case (I64V(v1), I64V(v2)) => I64V(v1 + v2)
      case _ => throw new Exception("Invalid types")
    }
    case Mul(_) => (lhs, rhs) match {
      case (I32V(v1), I32V(v2)) => I32V(v1 * v2)
      case (I64V(v1), I64V(v2)) => I64V(v1 * v2)
      case _ => throw new Exception("Invalid types")
    }
    case Sub(_) => (lhs, rhs) match {
      case (I32V(v1), I32V(v2)) => I32V(v1 - v2)
      case (I64V(v1), I64V(v2)) => I64V(v1 - v2)
      case _ => throw new Exception("Invalid types")
    }
    case Shl(_) => (lhs, rhs) match {
      case (I32V(v1), I32V(v2)) => I32V(v1 << v2)
      case (I64V(v1), I64V(v2)) => I64V(v1 << v2)
      case _ => throw new Exception("Invalid types")
    }
    case ShrU(_) => (lhs, rhs) match {
      case (I32V(v1), I32V(v2)) => I32V(v1 >>> v2)
      case (I64V(v1), I64V(v2)) => I64V(v1 >>> v2)
      case _ => throw new Exception("Invalid types")
    }
    case _ => ???
  }

  def evalUnaryOp(op: UnaryOp, value: Value) = op match {
    case Clz(_) => value match {
      case I32V(v) => I32V(Integer.numberOfLeadingZeros(v))
      case I64V(v) => I64V(java.lang.Long.numberOfLeadingZeros(v))
      case _ => throw new Exception("Invalid types")
    }
    case Ctz(_) => value match {
      case I32V(v) => I32V(Integer.numberOfTrailingZeros(v))
      case I64V(v) => I64V(java.lang.Long.numberOfTrailingZeros(v))
      case _ => throw new Exception("Invalid types")
    }
    case Popcnt(_) => value match {
      case I32V(v) => I32V(Integer.bitCount(v))
      case I64V(v) => I64V(java.lang.Long.bitCount(v))
      case _ => throw new Exception("Invalid types")
    }
    case _ => ???
  }

  // TODO: double check (copilot generated)
  def evalRelOp(op: RelOp, lhs: Value, rhs: Value) = op match {
    case Eq(_) => (lhs, rhs) match {
      case (I32V(v1), I32V(v2)) => I32V(if (v1 == v2) 1 else 0)
      case (I64V(v1), I64V(v2)) => I32V(if (v1 == v2) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
    case Ne(_) => (lhs, rhs) match {
      case (I32V(v1), I32V(v2)) => I32V(if (v1 != v2) 1 else 0)
      case (I64V(v1), I64V(v2)) => I32V(if (v1 != v2) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
    case LtS(_) => (lhs, rhs) match {
      case (I32V(v1), I32V(v2)) => I32V(if (v1 < v2) 1 else 0)
      case (I64V(v1), I64V(v2)) => I32V(if (v1 < v2) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
    case LtU(_) => (lhs, rhs) match {
      case (I32V(v1), I32V(v2)) => I32V(if (Integer.compareUnsigned(v1, v2) < 0) 1 else 0)
      case (I64V(v1), I64V(v2)) => I32V(if (java.lang.Long.compareUnsigned(v1, v2) < 0) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
    case GtS(_) => (lhs, rhs) match {
      case (I32V(v1), I32V(v2)) => I32V(if (v1 > v2) 1 else 0)
      case (I64V(v1), I64V(v2)) => I32V(if (v1 > v2) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
    case GtU(_) => (lhs, rhs) match {
      case (I32V(v1), I32V(v2)) => I32V(if (Integer.compareUnsigned(v1, v2) > 0) 1 else 0)
      case (I64V(v1), I64V(v2)) => I32V(if (java.lang.Long.compareUnsigned(v1, v2) > 0) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
    case LeS(_) => (lhs, rhs) match {
      case (I32V(v1), I32V(v2)) => I32V(if (v1 <= v2) 1 else 0)
      case (I64V(v1), I64V(v2)) => I32V(if (v1 <= v2) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
    case LeU(_) => (lhs, rhs) match {
      case (I32V(v1), I32V(v2)) => I32V(if (Integer.compareUnsigned(v1, v2) <= 0) 1 else 0)
      case (I64V(v1), I64V(v2)) => I32V(if (java.lang.Long.compareUnsigned(v1, v2) <= 0) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
    case GeS(_) => (lhs, rhs) match {
      case (I32V(v1), I32V(v2)) => I32V(if (v1 >= v2) 1 else 0)
      case (I64V(v1), I64V(v2)) => I32V(if (v1 >= v2) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
    case GeU(_) => (lhs, rhs) match {
      case (I32V(v1), I32V(v2)) => I32V(if (Integer.compareUnsigned(v1, v2) >= 0) 1 else 0)
      case (I64V(v1), I64V(v2)) => I32V(if (java.lang.Long.compareUnsigned(v1, v2) >= 0) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
  }

  def evalTestOp(op: TestOp, value: Value) = op match {
    case Eqz(_) => value match {
      case I32V(v) => I32V(if (v == 0) 1 else 0)
      case I64V(v) => I32V(if (v == 0) 1 else 0)
      case _ => throw new Exception("Invalid types")
    }
  }

  // def evalCvtOp(op: CvtOp, value: Value) = ???

  def memOob(frame: Frame, memoryIndex: Int, offset: Int, size: Int) = {
    val memory = frame.module.memory(memoryIndex)
    val end = offset + size
    end > memory.size
  }

  def step: Config = {
    val Code(stack, adminInstrs) = code
    // adminInstrs.head.value match {
    //   case Plain(instr) => println(stack, instr)
    //   case _ => println(stack)
    // }
    val (newStack, newInstrs): (List[Value], List[AdminInstr])  = adminInstrs.head match {
      case Plain(instr) => instr match {
        // Parametric Instructions
        case Drop => stack match {
          case _ :: newStack => (newStack, adminInstrs.tail)
          case _ => throw new Exception("Invalid stack")
        }

        case Select(_) => stack match {
          case I32V(cond) :: v2 :: v1 :: newStack => {
            val value = if (cond == 0) v1 else v2
            (value :: newStack, adminInstrs.tail)
          }
          case _ => throw new Exception("Invalid stack")
        }

        // Variable Instructions
        // https://www.w3.org/TR/wasm-core-2/exec/instructions.html#variable-instructions
        case LocalGet(local) => (frame.locals(local) :: stack, adminInstrs.tail)
        case LocalSet(local) => stack match {
          case value :: newStack => {
            frame.locals = frame.locals.updated(local, value)
            (newStack, adminInstrs.tail)
          }
          case _ => throw new Exception("Invalid stack")
        }
        case LocalTee(local) => stack match {
          case value :: newStack => {
            frame.locals = frame.locals.updated(local, value)
            (value :: newStack, adminInstrs.tail)
          }
          case _ => throw new Exception("Invalid stack")
        }
        case GlobalGet(global) => (frame.module.globals(global).value :: stack, adminInstrs.tail)
        case GlobalSet(global) => stack match {
          case value :: newStack => {
            frame.module.globals(global).tipe match {
              case GlobalType(tipe, true) if value.tipe == tipe => frame.module.globals(global).value = value
              case GlobalType(_, true) => throw new Exception("Invalid type")
              case _ => throw new Exception("Cannot set immutable global")
            }
            (newStack, adminInstrs.tail)
          }
          case _ => throw new Exception("Invalid stack")
        }

        // Memory Instructions
        // https://www.w3.org/TR/wasm-core-2/exec/instructions.html#memory-instructions
        case MemorySize => (I32V(frame.module.memory.head.size) :: stack, adminInstrs.tail)

        // https://github.com/WebAssembly/spec/blob/main/interpreter/exec/eval.ml#L406
        // https://github.com/WebAssembly/spec/blob/main/interpreter/runtime/memory.ml#L50
        // https://www.w3.org/TR/wasm-core-2/exec/instructions.html#xref-syntax-instructions-syntax-instr-memory-mathsf-memory-grow
        case MemoryGrow => stack match {
          case I32V(delta) :: newStack => {
            val mem = frame.module.memory.head
            val oldSize = mem.size
            try {
              mem.grow(delta)
              (I32V(oldSize) :: newStack, adminInstrs.tail)
            } catch {
              case _: Throwable => (I32V(-1) :: newStack, adminInstrs.tail)
            }
          }
          case _ => throw new Exception("Invalid stack")
        }

        case MemoryFill => stack match {
          case I32V(value) :: I32V(offset) :: I32V(size) :: newStack => {
            if (memOob(frame, 0, offset, size)) {
              val trap: AdminInstr = Trapping("Out of bounds memory access")
              (newStack, trap :: adminInstrs.tail)
            } else {
              val mem = frame.module.memory.head
              mem.fill(offset, size, value.toByte)
              (newStack, adminInstrs.tail)
            }
          }
          case _ => throw new Exception("Invalid stack")
        }

        case MemoryCopy => stack match {
          case I32V(n) :: I32V(src) :: I32V(dest) :: newStack => {
            if (memOob(frame, 0, src, n) || memOob(frame, 0, dest, n)) {
              val trap: AdminInstr = Trapping("Out of bounds memory access")
              (newStack, trap :: adminInstrs.tail)
            } else {
              val mem = frame.module.memory.head
              mem.copy(dest, src, n)
              ??? // FIXME
            }
          }
          case _ => throw new Exception("Invalid stack")
        }

        // Numeric Instructions
        // https://www.w3.org/TR/wasm-core-2/exec/instructions.html#numeric-instructions
        case Const(num) => (num :: stack, adminInstrs.tail)
        case Binary(op) => stack match {
          case v2 :: v1 :: rest => (evalBinOp(op, v1, v2) :: rest, adminInstrs.tail)
          case _ => throw new Exception("Invalid stack")
        }
        case Unary(op) => stack match {
          case value :: rest => (evalUnaryOp(op, value) :: rest, adminInstrs.tail)
          case _ => throw new Exception("Invalid stack")
        }
        case Compare(op) => stack match {
          case v2 :: v1 :: rest => (evalRelOp(op, v1, v2) :: rest, adminInstrs.tail)
          case _ => throw new Exception("Invalid stack")
        }
        case Test(testOp) => stack match {
          case value :: rest => (evalTestOp(testOp, value) :: rest, adminInstrs.tail)
          case _ => throw new Exception("Invalid stack")
        }
        case Store(StoreOp(align, offset, tipe, None)) => stack match {
          case I32V(value) :: I32V(addr) :: newStack => {
            val mem = frame.module.memory(0)
            mem.storeInt(addr + offset, value)
            (newStack, adminInstrs.tail)
          }
          case _ => throw new Exception("Invalid stack")
        }
        case Load(LoadOp(align, offset, tipe, None, None)) => stack match {
          case I32V(addr) :: newStack => {
            val mem = frame.module.memory(0)
            val value = mem.loadInt(addr + offset)
            (I32V(value) :: newStack, adminInstrs.tail)
          }
          case _ => throw new Exception("Invalid stack")
        }

        // Control Instructions
        // https://www.w3.org/TR/wasm-core-2/exec/instructions.html#numeric-instructions
        case Nop => (stack, adminInstrs.tail)
        case Unreachable => throw new Exception("Unreachable")
        case Block(blockTy, instrs) => {
          val funcType = blockTy.toFuncType(frame.module)
          val args = stack.take(funcType.inps.length)
          val newStack = stack.drop(funcType.inps.length)
          val labelInstrs = instrs.map(instr => Plain(instr).asInstanceOf[AdminInstr]).toList
          val label: AdminInstr = Label(funcType.out.length, List(), Code(args, labelInstrs))
          (newStack, label :: adminInstrs.tail)
        }
        case Loop(blockTy, instrs) => {
          val funcType = blockTy.toFuncType(frame.module)
          val args = stack.take(funcType.inps.length)
          val newStack = stack.drop(funcType.inps.length)
          val labelInstrs = instrs.map(instr => Plain(instr).asInstanceOf[AdminInstr]).toList
          val label: AdminInstr = Label(funcType.inps.length, List(instr), Code(args, labelInstrs))
          (newStack, label :: adminInstrs.tail)
        }
        case If(blockTy, thenInstrs, elseInstrs) => stack match {
          case I32V(cond) :: newStack => {
            val instrs = if (cond == 0) elseInstrs else thenInstrs
            val block: AdminInstr = Plain(Block(blockTy, instrs))
            (newStack, block :: adminInstrs.tail)
          }
          case _ => throw new Exception("Invalid stack")
        }
        case Br(label) => {
          val breaking: AdminInstr = Breaking(label, stack)
          (List(), breaking :: adminInstrs.tail)
        }
        case BrIf(label) => stack match {
          case I32V(0) :: newStack => {
            (newStack, adminInstrs.tail)
          }
          case I32V(_) :: newStack => {
            val branch: AdminInstr = Plain(Br(label))
            (newStack, branch :: adminInstrs.tail)
          }
          case _ => throw new Exception("Invalid stack")
        }
        case Return => {
          val returning: AdminInstr = Returning(stack)
          (List(), returning :: adminInstrs.tail)
        }
        case Call(func) => {
          val invoke: AdminInstr = Invoke(func)
          (stack, invoke :: adminInstrs.tail)
        }
      }

      // Administrative Instructions
      // https://www.w3.org/TR/wasm-core-2/exec/runtime.html#administrative-instructions

      case Trapping(msg) => throw new Exception(s"Trap: $msg")

      case Invoke(_) if stackBudget == 0 => {
        throw new Exception("Stack overflow")
      }

      case Invoke(func) => {
        val FuncBodyDef(tipe, names, fnLocals, body) = frame.module.funcs(func)
        val args = stack.take(tipe.inps.length).reverse
        val newStack = stack.drop(tipe.inps.length)

        val locals = args ++ fnLocals.map(_ => I32V(0)) // TODO: map locals to default value for type
        val fnFrame = Frame(frame.module, locals)
        val labelCode = Code(List(), body.map(Plain(_).asInstanceOf[AdminInstr]).toList)
        val label = Label(tipe.out.length, List(), labelCode)
        val code = Code(List(), List(label))
        val frameInstr: AdminInstr = FrameInstr(tipe.out.length, fnFrame, code)
        (newStack, frameInstr :: adminInstrs.tail)
      }

      case FrameInstr(n, innerFrame, Code(frameStack, List())) =>
        (frameStack ++ stack, adminInstrs.tail)

      case FrameInstr(n, innerFrame, Code(frameStack, Returning(retStack) :: rest)) =>
        (retStack.take(n) ++ stack, adminInstrs.tail)

      case Label(_, labelInstrs, Code(labelStack, trap@Trapping(_) :: rest)) =>
        (stack, trap.asInstanceOf[AdminInstr] :: adminInstrs.tail)

      case FrameInstr(n, innerFrame, code) => {
        val frameConfig = Config(innerFrame, code, stackBudget - 1).step
        val frameInstr: AdminInstr = FrameInstr(n, frameConfig.frame, frameConfig.code)
        (stack, frameInstr :: adminInstrs.tail)
      }

      case Label(_, _, Code(labelStack, List())) =>
        (labelStack ++ stack, adminInstrs.tail)

      case Label(_, _, Code(_labelStack, Returning(retStack) :: rest)) => {
        val returning: AdminInstr = Returning(retStack)
        (stack, returning :: adminInstrs.tail)
      }

      case Label(n, labelInstrs, Code(labelStack, Breaking(0, breakStack) :: rest)) => {
        val newInstrs = labelInstrs.map(instr => Plain(instr).asInstanceOf[AdminInstr])
        (breakStack.take(n) ++ stack, newInstrs ++ adminInstrs.tail)
      }

      case Label(_, labelInstrs, Code(labelStack, Breaking(n, breakStack) :: rest)) => {
        val breaking: AdminInstr = Breaking(n - 1, breakStack)
        (stack, breaking :: adminInstrs.tail)
      }

      case Label(_, labelInstrs, Code(labelStack, trap@Trapping(_) :: rest)) => {
        (stack, trap.asInstanceOf[AdminInstr] :: adminInstrs.tail)
      }

      case Label(n, instrs, labelCode) => {
        val labelConfig = this.copy(code = labelCode).step
        val label: AdminInstr = Label(n, instrs, labelConfig.code)
        (stack, label :: adminInstrs.tail)
      }

      case instr => throw new Exception(s"Invalid admin instruction $instr")
    }

    this.copy(code = Code(newStack, newInstrs))
  }

  def eval: Config = this.code.adminInstrs match {
    case Nil => this
    case Trapping(msg) :: _ => throw new Exception(s"Trap: $msg")
    case _ => this.step.eval
  }
}
