package gensym.wasm.concolicminiwasm

import gensym.wasm.ast._
import gensym.wasm.source._
// import gensym.wasm.memory._
import gensym.wasm.concolicmemory._
import gensym.wasm.symbolic._
import gensym.wasm.parser._

import scala.util.Random

import scala.collection.mutable.{ArrayBuffer, HashMap}

case class ModuleInstance(
    // TODO: expand definition of this?
    defs: List[Definition],
    types: List[FuncLikeType],
    funcs: HashMap[Int, Callable],
    memory: List[ConcolicMemory] = List(ConcolicMemory()),
    globals: ArrayBuffer[(RTGlobal, SymVal)] = ArrayBuffer[(RTGlobal, SymVal)]()
)

object ModuleInstance {
  def apply(module: Module): ModuleInstance = {
    val types = module.definitions
      .collect({ case TypeDef(_, ft) =>
        ft
      })
      .toList

    ModuleInstance(module.definitions, types, module.funcEnv)
  }
}

object Primitives {
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
    case _ => {
      println(s"unimplemented binop: $op")
      ???
    }
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
        case (I32V(v1), I32V(v2)) => I32V(if (Integer.compareUnsigned(v1, v2) < 0) 1 else 0)
        case (I64V(v1), I64V(v2)) => I32V(if (java.lang.Long.compareUnsigned(v1, v2) < 0) 1 else 0)
        case _                    => throw new Exception("Invalid types")
      }
    case GtS(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) => I32V(if (v1 > v2) 1 else 0)
        case (I64V(v1), I64V(v2)) => I32V(if (v1 > v2) 1 else 0)
        case _                    => throw new Exception("Invalid types")
      }
    case GtU(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) => I32V(if (Integer.compareUnsigned(v1, v2) > 0) 1 else 0)
        case (I64V(v1), I64V(v2)) => I32V(if (java.lang.Long.compareUnsigned(v1, v2) > 0) 1 else 0)
        case _                    => throw new Exception("Invalid types")
      }
    case LeS(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) => I32V(if (v1 <= v2) 1 else 0)
        case (I64V(v1), I64V(v2)) => I32V(if (v1 <= v2) 1 else 0)
        case _                    => throw new Exception("Invalid types")
      }
    case LeU(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) => I32V(if (Integer.compareUnsigned(v1, v2) <= 0) 1 else 0)
        case (I64V(v1), I64V(v2)) => I32V(if (java.lang.Long.compareUnsigned(v1, v2) <= 0) 1 else 0)
        case _                    => throw new Exception("Invalid types")
      }
    case GeS(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) => I32V(if (v1 >= v2) 1 else 0)
        case (I64V(v1), I64V(v2)) => I32V(if (v1 >= v2) 1 else 0)
        case _                    => throw new Exception("Invalid types")
      }
    case GeU(_) =>
      (lhs, rhs) match {
        case (I32V(v1), I32V(v2)) => I32V(if (Integer.compareUnsigned(v1, v2) >= 0) 1 else 0)
        case (I64V(v1), I64V(v2)) => I32V(if (java.lang.Long.compareUnsigned(v1, v2) >= 0) 1 else 0)
        case _                    => throw new Exception("Invalid types")
      }
  }

  def evalTestOp(op: TestOp, value: Value) = op match {
    // TODO: add path cond here
    case Eqz(_) =>
      value match {
        case I32V(v) => I32V(if (v == 0) 1 else 0)
        case I64V(v) => I32V(if (v == 0) 1 else 0)
        case _       => throw new Exception("Invalid types")
      }
  }

  def evalSymBinOp(op: BinOp, lhs: SymVal, rhs: SymVal): SymVal = (lhs, rhs) match {
    case (Concrete(lhs), Concrete(rhs)) => Concrete(evalBinOp(op, lhs, rhs))
    case _                              => SymBinary(op, lhs, rhs)
  }

  def evalSymTestOp(op: TestOp, sv: SymVal): SymVal = sv match {
    case Concrete(v) => Concrete(evalTestOp(op, v))
    case _ =>
      op match {
        case Eqz(_) => SymIte(CondEqz(sv), Concrete(I32V(1)), Concrete(I32V(0)))
      }
  }

  def evalSymRelOp(op: RelOp, lhs: SymVal, rhs: SymVal): SymVal = (lhs, rhs) match {
    case (Concrete(lhs), Concrete(rhs)) => Concrete(evalRelOp(op, lhs, rhs))
    case _                              => SymIte(RelCond(op, lhs, rhs), Concrete(I32V(1)), Concrete(I32V(0)))
  }

  def memOutOfBound(frame: Frame, memoryIndex: Int, offset: Int, size: Int) = {
    // TODO
    false
    // val memory = frame.module.memory(memoryIndex)
    // offset + size > memory.size
    // offset + size > memory.size
  }

  def zero(t: ValueType): Value = t match {
    case NumType(kind) =>
      kind match {
        case I32Type => I32V(0)
        case I64Type => I64V(0)
        case F32Type => F32V(0)
        case F64Type => F64V(0)
      }
    case VecType(kind) => ???
    case RefType(kind) => RefNullV(kind)
  }

  def randomOfTy(ty: ValueType): Value = ty match {
    case NumType(I32Type) => I32V(Random.nextInt())
    case NumType(I64Type) => I64V(Random.nextLong())
    case NumType(F32Type) => F32V(Random.nextFloat())
    case NumType(F64Type) => F64V(Random.nextDouble())
  }
}

case class Frame(module: ModuleInstance, locals: ArrayBuffer[Value], symLocals: ArrayBuffer[SymVal])

case class Evaluator(module: ModuleInstance) {
  import Primitives._

  type RetCont = (List[Value], List[SymVal], List[Cond]) => Unit
  type Cont = (List[Value], List[SymVal], List[Cond]) => Unit

  val symEnv = HashMap[Int, Value]()

  // TODO: should this have a uniform return type like Wasm?
  def eval(
      insts: List[Instr],
      concStack: List[Value],
      symStack: List[SymVal],
      frame: Frame,
      ret: RetCont,
      trail: List[Cont]
  )(implicit pathConds: List[Cond]): Unit = {
    if (insts.isEmpty) return ret(concStack, symStack, pathConds)

    println(s"pathConds: $pathConds")

    val inst = insts.head
    val rest = insts.tail

    println(s"inst: $inst, concStack: $concStack, symStack: $symStack")

    inst match {
      case PushSym(name, v) =>
        eval(rest, v :: concStack, SymV(name) :: symStack, frame, ret, trail)
      case Symbolic(ty) =>
        val I32V(symIndex) :: newStack = concStack
        val symVal = SymV(s"sym_$symIndex")
        if (!symEnv.contains(symIndex)) {
          symEnv(symIndex) = Primitives.randomOfTy(ty)
        }
        val v = symEnv(symIndex)
        eval(rest, v :: newStack, symVal :: symStack, frame, ret, trail)
      case Drop => eval(rest, concStack.tail, symStack.tail, frame, ret, trail)
      case Select(_) =>
        val I32V(cond) :: v2 :: v1 :: newStack = concStack
        val symCond :: symV2 :: symV1 :: newSymStack = symStack
        val value = if (cond == 0) v1 else v2
        val symVal = SymIte(CondEqz(symCond), symV1, symV2)
        eval(rest, value :: newStack, symVal :: newSymStack, frame, ret, trail)
      case LocalGet(i) =>
        eval(rest, frame.locals(i) :: concStack, frame.symLocals(i) :: symStack, frame, ret, trail)
      case LocalSet(i) =>
        val value :: newStack = concStack
        val symVal :: newSymStack = symStack
        frame.locals(i) = value
        frame.symLocals(i) = symVal
        eval(rest, newStack, newSymStack, frame, ret, trail)
      case LocalTee(i) =>
        val value :: _ = concStack
        val symVal :: _ = symStack
        frame.locals(i) = value
        frame.symLocals(i) = symVal
        eval(rest, concStack, symStack, frame, ret, trail)
      case GlobalGet(i) =>
        val (conc, sym) = frame.module.globals(i)
        eval(rest, conc.value :: concStack, sym :: symStack, frame, ret, trail)
      case GlobalSet(i) =>
        val value :: newStack = concStack
        val symVal :: newSymStack = symStack
        val oldConc = frame.module.globals(i)._1
        frame.module.globals(i) = (oldConc.copy(value = value), symVal)
        eval(rest, newStack, newSymStack, frame, ret, trail)
      // I think these are essentially dummies in WASP
      // to more accurately replace them, we should probably
      // add a dummy memory size field to ConcolicMemory
      case MemorySize =>
        eval(rest, I32V(100) :: concStack, Concrete(I32V(100)) :: symStack, frame, ret, trail)
      // val cv = I32V(frame.module.memory.head.size)
      // val sv = Concrete(cv)
      // eval(rest, cv::concStack, sv::symStack, frame, ret, trail)
      case MemoryGrow =>
        eval(rest, I32V(100) :: concStack, Concrete(I32V(100)) :: symStack, frame, ret, trail)
      // val I32V(delta)::newStack = concStack
      // val mem = frame.module.memory.head
      // val oldSize = mem.size
      // val cv = mem.grow(delta) match {
      //   case Some(e) => I32V(-1)
      //   case _ => I32V(-1)
      // }
      // eval(rest, cv :: newStack, Concrete(cv) :: symStack.tail, frame, ret, trail)
      // WASP doesn't implement these
      case MemoryFill => ???
      // val I32V(value) :: I32V(offset) :: I32V(size) :: newStack = concStack
      // if (memOutOfBound(frame, 0, offset, size)) throw new Exception("Out of bounds memory access") // GW: turn this into a `trap`?
      // else {
      //   frame.module.memory.head.fill(offset, size, value.toByte)
      //   eval(rest, newStack, frame, ret, trail)
      // }
      case MemoryCopy => ???
      // val I32V(n) :: I32V(src) :: I32V(dest) :: newStack = concStack
      // if (memOutOfBound(frame, 0, src, n) || memOutOfBound(frame, 0, dest, n)) throw new Exception("Out of bounds memory access")
      // else {
      //   frame.module.memory.head.copy(dest, src, n)
      //   eval(rest, newStack, frame, ret, trail)
      // }
      case Const(n) => eval(rest, n :: concStack, Concrete(n) :: symStack, frame, ret, trail)
      case Binary(op) =>
        val v2 :: v1 :: newStack = concStack
        val sv2 :: sv1 :: newSymStack = symStack
        eval(rest, evalBinOp(op, v1, v2) :: newStack, evalSymBinOp(op, sv1, sv2) :: newSymStack, frame, ret, trail)
      case Unary(op) =>
        val v :: newStack = concStack
        val sv :: newSymStack = symStack
        eval(rest, evalUnaryOp(op, v) :: newStack, SymUnary(op, sv) :: newSymStack, frame, ret, trail)
      case Compare(op) =>
        val v2 :: v1 :: newStack = concStack
        val sv2 :: sv1 :: newSymStack = symStack
        eval(rest, evalRelOp(op, v1, v2) :: newStack, evalSymRelOp(op, sv1, sv2) :: newSymStack, frame, ret, trail)
      case Test(op) =>
        val v :: newStack = concStack
        val sv :: newSymStack = symStack
        val test = evalTestOp(op, v)
        val symTest = evalSymTestOp(op, sv)
        eval(rest, test :: newStack, symTest :: newSymStack, frame, ret, trail)
      case Store(StoreOp(align, offset, ty, None)) =>
        val I32V(v) :: I32V(addr) :: newStack = concStack
        val sv :: sa :: newSymStack = symStack
        // need to concretize sa and then checkAccess
        frame.module.memory(0).storeInt(addr + offset, (v, sv))
        eval(rest, newStack, symStack.drop(2), frame, ret, trail)
      case Load(LoadOp(align, offset, ty, None, None)) =>
        val I32V(addr) :: newStack = concStack
        val sa :: newSymStack = symStack
        // need to concretize sv and then checkAccess
        val (value, sv) = frame.module.memory(0).loadInt(addr + offset)
        eval(rest, I32V(value) :: newStack, sv :: newSymStack, frame, ret, trail)
      case Nop =>
        eval(rest, concStack, symStack, frame, ret, trail)
      case Unreachable => throw new RuntimeException("Unreachable")
      case Block(ty, inner) =>
        val k: Cont = (retStack, retSymStack, newPathConds) =>
          eval(rest, concStack ++ retStack, symStack ++ retSymStack, frame, ret, trail)(newPathConds)

        eval(inner, List(), List(), frame, k, k :: trail)
      case Loop(ty, inner) =>
        val k: Cont = (retStack, retSymStack, newPathConds) =>
          eval(insts, concStack ++ retStack, symStack ++ retSymStack, frame, ret, trail)(newPathConds)
        eval(inner, List(), List(), frame, k, k :: trail)
      case If(ty, thn, els) =>
        val scnd :: newSymStack = symStack
        val I32V(cond) :: newStack = concStack
        val inner = if (cond == 0) thn else els
        val newPathConds = scnd match {
          case Concrete(_) => pathConds
          case _           => if (cond == 0) CondEqz(scnd) :: pathConds else Not(CondEqz(scnd)) :: pathConds
        }
        val k: Cont = (retStack, retSymStack, newPathConds) =>
          eval(rest, retStack ++ newStack, retSymStack ++ newSymStack, frame, ret, trail)(newPathConds)
        eval(inner, List(), List(), frame, ret, k :: trail)(newPathConds)
      case Br(label) =>
        trail(label)(concStack, symStack, pathConds)
      case BrIf(label) =>
        val scnd :: newSymStack = symStack
        val I32V(cond) :: newStack = concStack
        val newPathConds = scnd match {
          case Concrete(_) => pathConds
          case _           => if (cond == 0) CondEqz(scnd) :: pathConds else Not(CondEqz(scnd)) :: pathConds
        }
        if (cond == 0) eval(rest, newStack, newSymStack, frame, ret, trail)(newPathConds)
        else trail(label)(newStack, newSymStack, newPathConds)
      case Return => ret(concStack, symStack, pathConds)
      case Call(f) =>
        evalCall(rest, concStack, symStack, frame, ret, trail, f, false)
      case _ => ???
    }
  }

  // def eval(insts: List[Instr], concStack: List[Value], symStack: List[SymVal],
  //          frame: Frame, ret: RetCont, trail: List[Cont])(implicit pathConds: List[Cond])

  def evalCall(
      rest: List[Instr],
      concStack: List[Value],
      symStack: List[SymVal],
      frame: Frame,
      ret: RetCont,
      trail: List[Cont],
      funcIndex: Int,
      isTail: Boolean
  )(implicit pathConds: List[Cond]): Unit =
    module.funcs(funcIndex) match {
      case FuncDef(_, FuncBodyDef(ty, _, locals, body)) =>
        println(s"call $funcIndex: locals: ${locals}")
        val args = concStack.take(ty.inps.size).reverse
        val symArgs = symStack.take(ty.inps.size).reverse
        val newStack = concStack.drop(ty.inps.size)
        val newSymStack = symStack.drop(ty.inps.size)
        val frameLocals = args ++ locals.map(_ => I32V(0)) // GW: always I32? or depending on their types?
        val symFrameLocals = symArgs ++ locals.map(_ => Concrete(I32V(0)))
        val newFrame = Frame(frame.module, ArrayBuffer(frameLocals: _*), ArrayBuffer(symFrameLocals: _*))
        val newRet: RetCont = (retStack, retSymStack, newPathConds) =>
          eval(rest, retStack ++ newStack, retSymStack ++ newSymStack, frame, ret, trail)(newPathConds)
        // val k: Cont = (retStack, symStack) =>
        //   eval(rest, retStack, frame, ret, trail)
        eval(body, List(), List(), newFrame, newRet, newRet :: trail) // GW: should we install new trail cont?

      // TODO: clean up the other cases
      // case Import("console", "log", _) =>
      //   val I32V(v) :: newStack = stack
      //   println(v)
      //   eval(rest, newStack, frame, kont, trail)
      // case Import("spectest", "print_i32", _) =>
      //   val I32V(v) :: newStack = stack
      //   println(v)
      //   eval(rest, newStack, frame, kont, trail)
      // case Import(_, _, _) => throw new Exception(s"Unknown import at $funcIndex")
      case _ => throw new Exception(s"Definition at $funcIndex is not callable")
    }

  // TODO: seems bad, global might have an expression to evaluate (maybe only constants?)
  def evalExpr(expr: List[Instr]): (Value, SymVal) = {
    var cv = null.asInstanceOf[Value]
    var sv = null.asInstanceOf[SymVal]
    eval(
      expr,
      List(),
      List(),
      null,
      (concStack, symStack, pathConds) => {
        cv = concStack.head
        sv = symStack.head
      },
      List()
    )(List())
    (cv, sv)
  }

  private def printRetCont(concStack: List[Value], symStack: List[SymVal], pathConds: List[Cond]) = {
    println(s"retCont: $concStack")
    println(s"symStack: $symStack")
    println(s"pathCnds: $pathConds")
  }

  def execWholeProgram(
      main: Option[String] = None,
      symEnv: HashMap[Int, Value] = HashMap(),
      k: RetCont = printRetCont
  ) = {
    import collection.mutable.ArrayBuffer

    this.symEnv.clear()
    this.symEnv ++= symEnv

    val instrs = main match {
      case Some(func_name) =>
        module.defs.flatMap({
          case Export(`func_name`, ExportFunc(fid)) =>
            println(s"Entering function $main")
            module.funcs(fid) match {
              case FuncDef(_, FuncBodyDef(_, _, _, body)) => body
              case _                                      => throw new Exception("Entry function has no concrete body")
            }
          case _ => List()
        })
      case None =>
        module.defs.flatMap({
          case Start(id) =>
            println(s"Entering unnamed function $id")
            module.funcs(id) match {
              case FuncDef(_, FuncBodyDef(_, _, _, body)) => body
              case _ =>
                throw new Exception("Entry function has no concrete body")
            }
          case _ => List()
        })
    }
    val funcDefs = module.defs.collect({ case f @ FuncDef(_, _) => f })

    val funcId = funcDefs.indexWhere({ case FuncDef(name, _) =>
      name == Some(main)
    })

    print(s"instrs: $instrs")

    // val instrs = List(Call(funcId))

    // TODO: what are we tryign to do with globals here
    // does global values allow general expressions?
    // val globals = module.definitions.collect({ case g@Global(_, _) => g })

    // for (global <- globals) {
    //   global.f match {
    //     case GlobalValue(ty, e) => {
    //       val (cv, sv) = evalExpr(e)
    //       moduleInst.globals.append((RTGlobal(ty, cv), sv))
    //     }
    //     case _ => ???
    //   }
    // }

    val locals = extractLocals(module, main)

    val concreteLocals = locals.map(zero(_))
    val symLocals = locals.map(zero(_)).map(Concrete(_))
    val frame = Frame(module, ArrayBuffer(concreteLocals: _*), ArrayBuffer(symLocals: _*))

    // val frame = Frame(module, ArrayBuffer(locals.map(zero(_)): _*), ArrayBuffer(locals.map(zero(_)): _*))

    eval(
      instrs,
      List(),
      List(),
      // frame,
      Frame(module, ArrayBuffer(I32V(0)), ArrayBuffer(Concrete(I32V(0)))),
      k,
      List((newStack, _, _) => println(s"trail: $newStack"))
    )(List())

  }

  def extractLocals(module: ModuleInstance, main: Option[String]): List[ValueType] =
    main match {
      case Some(func_name) =>
        module.defs.flatMap({
          case Export(`func_name`, ExportFunc(fid)) =>
            System.err.println(s"Entering function $main")
            module.funcs(fid) match {
              case FuncDef(_, FuncBodyDef(_, _, locals, _)) => locals
              case _ => throw new Exception("Entry function has no concrete body")
            }
          case _ => List()
        })
      case None =>
        module.defs.flatMap({
          case Start(id) =>
            System.err.println(s"Entering unnamed function $id")
            module.funcs(id) match {
              case FuncDef(_, FuncBodyDef(_, _, locals, body)) => locals
              case _ => throw new Exception("Entry function has no concrete body")
            }
          case _ => List()
        })
    }

}
