package gensym.wasm.stagedconcolicminiwasm

import scala.collection.mutable.{ArrayBuffer, HashMap}

import lms.core.stub.Adapter
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{Base, ScalaGenBase, CGenBase}
import lms.core.Backend._
import lms.core.Backend.{Block => LMSBlock, Const => LMSConst}
import lms.core.Graph

import gensym.wasm.ast._
import gensym.wasm.ast.{Const => WasmConst, Block => WasmBlock}
import gensym.wasm.miniwasm.{ModuleInstance}
import gensym.wasm.symbolic.{SymVal}
import gensym.lmsx.{SAIDriver, StringOps, SAIOps, SAICodeGenBase, CppSAIDriver, CppSAICodeGenBase}
import gensym.wasm.symbolic.Concrete
import gensym.wasm.symbolic.ExploreTree
import gensym.structure.freer.Explore

@virtualize
trait StagedWasmEvaluator extends SAIOps {
  def module: ModuleInstance

  trait ReturnSite

  trait StagedNum {
    def tipe: ValueType
  }

  trait StagedConcreteNum {
    def tipe: ValueType = this match {
      case I32C(_) => NumType(I32Type)
      case I64C(_) => NumType(I64Type)
      case F32C(_) => NumType(F32Type)
      case F64C(_) => NumType(F64Type)
    }

    def i: Rep[Num]
  }

  case class I32C(i: Rep[Num]) extends StagedConcreteNum
  case class I64C(i: Rep[Num]) extends StagedConcreteNum
  case class F32C(i: Rep[Num]) extends StagedConcreteNum
  case class F64C(i: Rep[Num]) extends StagedConcreteNum


  trait StagedSymbolicNum {
    def tipe: ValueType = this match {
      case I32S(_) => NumType(I32Type)
      case I64S(_) => NumType(I64Type)
      case F32S(_) => NumType(F32Type)
      case F64S(_) => NumType(F64Type)
    }

    def s: Rep[SymVal]
  }

  case class I32S(s: Rep[SymVal]) extends StagedSymbolicNum
  case class I64S(s: Rep[SymVal]) extends StagedSymbolicNum
  case class F32S(s: Rep[SymVal]) extends StagedSymbolicNum
  case class F64S(s: Rep[SymVal]) extends StagedSymbolicNum

  def toStagedNum(num: Num): StagedConcreteNum = {
    num match {
      case I32V(_) => I32C(num)
      case I64V(_) => I64C(num)
      case F32V(_) => F32C(num)
      case F64V(_) => F64C(num)
    }
  }

  def toStagedSymbolicNum(num: Num): StagedSymbolicNum = {
    num match {
      case I32V(_) => I32S(Concrete(num))
      case I64V(_) => I64S(Concrete(num))
      case F32V(_) => F32S(Concrete(num))
      case F64V(_) => F64S(Concrete(num))
    }
  }

  implicit class ValueTypeOps(ty: ValueType) {
    def size: Int = ty match {
      case NumType(I32Type) => 4
      case NumType(I64Type) => 8
      case NumType(F32Type) => 4
      case NumType(F64Type) => 8
    }

    def concreteTag: (Rep[Num]) => StagedConcreteNum = {
      ty match {
        case NumType(I32Type) => I32C
        case NumType(I64Type) => I64C
        case NumType(F32Type) => F32C
        case NumType(F64Type) => F64C
      }
    }

    def symbolicTag: (Rep[SymVal]) => StagedSymbolicNum = {
      ty match {
        case NumType(I32Type) => I32S
        case NumType(I64Type) => I64S
        case NumType(F32Type) => F32S
        case NumType(F64Type) => F64S
      }
    }
  }

  case class Context(
    stackTypes: List[ValueType],
    frameTypes: List[ValueType]
  ) {
    def push(ty: ValueType): Context = {
      this.copy(stackTypes = ty :: stackTypes)
    }

    def peek: ValueType = {
      stackTypes.head
    }

    def pop(): (ValueType, Context) = {
      val (ty :: rest) = stackTypes
      (ty, this.copy(stackTypes = rest))
    }

    def take(n: Int): Context = {
      Predef.assert(n <= stackTypes.size, s"Context.take size $n is larger than stack size ${stackTypes.size}")
      val (taken, rest) = stackTypes.splitAt(n)
      this.copy(stackTypes = rest)
    }

    def shift(offset: Int, size: Int): Context = {
      // Predef.println(s"[DEBUG] Shifting stack by $offset, size $size, $this")
      Predef.assert(offset >= 0, s"Context shift offset must be non-negative, get $offset")
      if (offset == 0) {
        this
      } else {
        this.copy(
          stackTypes = stackTypes.take(size) ++ stackTypes.drop(offset + size)
        )
      }
    }

  }

  case class ContextTransition(startCtx: Context, history: List[Instr], endCtx: Context) {
    def log(instr: Instr): ContextTransition = {
      this.copy(history = instr :: history)
    }

    def clearHistory: (Context, List[Instr], CleanCT) = {
      (startCtx, history, CleanCT(endCtx))
    }

    def push(ty: ValueType): ContextTransition = {
      this.copy(endCtx = endCtx.push(ty))
    }

    def peek: ValueType = {
      endCtx.peek
    }

    def pop(): (ValueType, ContextTransition) = {
      val (ty, newCtx) = endCtx.pop()
      (ty, this.copy(endCtx = newCtx))
    }

    def take(n: Int): ContextTransition = {
      this.copy(endCtx = endCtx.take(n))
    }

    def shift(offset: Int, size: Int): ContextTransition = {
      this.copy(endCtx = endCtx.shift(offset, size))
    }
  }

  case class CleanCT(ctx: Context)

  // we can treat every CleanCT as a ContextTransition
  implicit def toContextCT(ct: CleanCT): ContextTransition = {
    ContextTransition(ct.ctx, Nil, ct.ctx)
  }

  type MCont[A] = Unit => A
  type Cont[A] = (MCont[A]) => A
  type Trail[A] = List[CleanCT => Rep[Cont[A]]]

  // a cache storing the compiled code for each function, to reduce re-compilation
  val compileCache = new HashMap[Int, Rep[(MCont[Unit]) => Unit]]

  def makeDummy: Rep[Unit] = "dummy".reflectCtrlWith[Unit]()

  def funHere[A:Manifest,B:Manifest](f: Rep[A] => Rep[B], dummy: Rep[Unit]): Rep[A => B] = {
    // to avoid LMS lifting a function, we create a dummy node and read it inside function
    fun((x: Rep[A]) => {
      "dummy-op".reflectCtrlWith[Unit](dummy)
      f(x)
    })
  }

  def isSymStackInUse: Rep[Boolean] = !ReuseManager.isReusing

  def eval(insts: List[Instr],
           kont: CleanCT => Rep[Cont[Unit]],
           mkont: Rep[MCont[Unit]],
           trail: Trail[Unit])
          (oldCT: ContextTransition): Rep[Unit] = {
    if (insts.isEmpty) {
      val (oldCtx, history, ct) = oldCT.clearHistory
      if (isSymStackInUse) {
        evalSym(history)(oldCtx)
      }
      return kont(ct)(mkont)
    }

    // Predef.println(s"[DEBUG] Evaluating instructions: ${insts.mkString(", ")}")
    // Predef.println(s"[DEBUG] Current context: $ctx")
    val (inst, rest) = (insts.head, insts.tail)
    val ct = oldCT.log(inst)
    inst match {
      case Drop =>
        val (ty, ct1) = ct.pop()
        Stack.popC(ty)
        eval(rest, kont, mkont, trail)(ct1)
      case WasmConst(num) =>
        Stack.pushC(toStagedNum(num))
        val ct1 = ct.push(num.tipe(module))
        eval(rest, kont, mkont, trail)(ct1)
      case Symbolic(ty) =>
        val id = Stack.popC(ty)
        val symVal = id.makeSymbolic(ty)
        val num = SymEnv.read(symVal.s)
        Stack.pushC(ty.concreteTag(num))
        val ct1 = ct.pop()._2.push(ty)
        eval(rest, kont, mkont, trail)(ct1)
      case LocalGet(i) =>
        Stack.pushC(Frames.getC(i)(ct.endCtx))
        val ct1 = ct.push(ct.endCtx.frameTypes(i))
        eval(rest, kont, mkont, trail)(ct1)
      case LocalSet(i) =>
        val (ty, ct1) = ct.pop()
        val num = Stack.popC(ty)
        Frames.setC(i, num)
        eval(rest, kont, mkont, trail)(ct1)
      case LocalTee(i) =>
        val ty = ct.peek
        val num = Stack.peekC(ty)
        Frames.setC(i, num)
        eval(rest, kont, mkont, trail)(ct)
      case GlobalGet(i) =>
        Stack.pushC(Globals.getC(i))
        val ct1 = ct.push(module.globals(i).ty.ty)
        eval(rest, kont, mkont, trail)(ct1)
      case GlobalSet(i) =>
        val (ty, ct1) = ct.pop()
        val num = Stack.popC(ty)
        module.globals(i).ty match {
          case GlobalType(tipe, true) => {
            Globals.setC(i, num)
          }
          case _ => throw new Exception("Cannot set immutable global")
        }
        eval(rest, kont, mkont, trail)(ct1)
      case Store(StoreOp(align, offset, ty, None)) =>
        val (ty1, ct1) = ct.pop()
        val value = Stack.popC(ty1)
        val (ty2, ct2) = ct1.pop()
        val addr = Stack.popC(ty2)
        Memory.storeInt(addr.toInt, offset, value.toInt)
        eval(rest, kont, mkont, trail)(ct2)
      case Nop => eval(rest, kont, mkont, trail)(ct)
      case Load(LoadOp(align, offset, ty, None, None)) =>
        val (ty1, ct1) = ct.pop()
        val addr = Stack.popC(ty1)
        val num = Memory.loadIntC(addr.toInt, offset)
        Stack.pushC(num)
        val ct2 = ct1.push(ty)
        eval(rest, kont, mkont, trail)(ct2)
      case MemorySize => ???
      case MemoryGrow =>
        val (ty, ct1) = ct.pop()
        val delta = Stack.popC(ty)
        val ret = Memory.grow(delta.toInt)
        val retNum = Values.I32V(ret)
        // For now, we assume that the result of memory.grow only depends on the execution path, 
        // we can relax this by turning it return to a symbol value and mimic the memory.grow's result as input. 
        Stack.pushC(I32C(retNum))
        val ct2 = ct1.push(NumType(I32Type))
        eval(rest, kont, mkont, trail)(ct2)
      case MemoryFill => ???
      case Unreachable => unreachable()
      case Test(op) =>
        val (ty, ct1) = ct.pop()
        val v = Stack.popC(ty)
        Stack.pushC(evalTestOpC(op, v))
        val ct2 = ct1.push(v.tipe)
        eval(rest, kont, mkont, trail)(ct2)
      case Unary(op) =>
        val (ty, ct1) = ct.pop()
        val v = Stack.popC(ty)
        val res = evalUnaryOpC(op, v)
        Stack.pushC(res)
        val ct2 = ct1.push(res.tipe)
        eval(rest, kont, mkont, trail)(ct2)
      case Binary(op) =>
        val (ty2, ct1) = ct.pop()
        val v2 = Stack.popC(ty2)
        val (ty1, ct2) = ct1.pop()
        val v1 = Stack.popC(ty1)
        val res = evalBinOpC(op, v1, v2)
        Stack.pushC(res)
        val ct3 = ct2.push(res.tipe)
        eval(rest, kont, mkont, trail)(ct3)
      case Compare(op) =>
        val (ty2, ct1) = ct.pop()
        val v2 = Stack.popC(ty2)
        val (ty1, ct2) = ct1.pop()
        val v1 = Stack.popC(ty1)
        val res = evalRelOpC(op, v1, v2)
        Stack.pushC(res)
        val ct3 = ct2.push(res.tipe)
        eval(rest, kont, mkont, trail)(ct3)
      case WasmBlock(ty, inner) =>
        // no need to modify the stack when entering a block
        // the type system guarantees that we will never take more than the input size from the stack
        val funcTy = ty.funcType
        val exitSize = ct.endCtx.stackTypes.size - funcTy.inps.size + funcTy.out.size
        val dummy = makeDummy
        def restK(ct: CleanCT): Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => {
          info(s"Exiting the block, stackSize =", Stack.size)
          val offset = ct.endCtx.stackTypes.size - exitSize
          Stack.shiftC(offset, funcTy.out.size)
          if (isSymStackInUse) {
            Stack.shiftS(offset, funcTy.out.size)
          }
          val ct1 = ct.shift(offset, funcTy.out.size)
          eval(rest, kont, mk, trail)(ct1)
        })
        // TODO: extract this into a function
        val (oldCtx, history, ct1) = ct.clearHistory
        if (isSymStackInUse) {
          evalSym(history)(oldCtx)
        }
        eval(inner, restK _, mkont, restK _ :: trail)(ct1)
      case Loop(ty, inner) =>
        val funcTy = ty.funcType
        val exitSize = ct.endCtx.stackTypes.size - funcTy.inps.size + funcTy.out.size
        val dummy = makeDummy
        def restK(ct: CleanCT): Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => {
          info(s"Exiting the loop, stackSize =", Stack.size)
          val offset = ct.endCtx.stackTypes.size - exitSize
          Stack.shiftC(offset, funcTy.out.size)
          if (isSymStackInUse) {
            Stack.shiftS(offset, funcTy.out.size)
          }
          val ct1 = ct.shift(offset, funcTy.out.size)
          eval(rest, kont, mk, trail)(ct1)
        })
        val enterSize = ct.endCtx.stackTypes.size
        def loop(ct: CleanCT): Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => {
          info(s"Entered the loop, stackSize =", Stack.size)
          val offset = ct.endCtx.stackTypes.size - enterSize
          Stack.shiftC(offset, funcTy.inps.size)
          if (isSymStackInUse) {
            Stack.shiftS(offset, funcTy.inps.size)
          }
          val ct1 = ct.shift(offset, funcTy.inps.size)
          eval(inner, restK _, mk, loop _ :: trail)(ct1)
        })
        val (oldCtx, history, ct1) = ct.clearHistory
        if (isSymStackInUse) {
          evalSym(history)(oldCtx)
        }
        loop(ct1)(mkont)
      case If(ty, thn, els) =>
        val funcTy = ty.funcType
        val (condTy, ct1) = ct.pop()
        val cond = Stack.popC(condTy)
        val exitSize = ct1.endCtx.stackTypes.size - funcTy.inps.size + funcTy.out.size
        def restK(ct: CleanCT): Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => {
          info(s"Exiting the if, stackSize =", Stack.size)
          val offset = ct.endCtx.stackTypes.size - exitSize
          Stack.shiftC(offset, funcTy.out.size)
          if (isSymStackInUse) {
            Stack.shiftS(offset, funcTy.out.size)
          }
          val ct1 = ct.shift(offset, funcTy.out.size)
          eval(rest, kont, mk, trail)(ct1)
        })
        val (oldCtx, history, ct2) = ct1.clearHistory
        if (isSymStackInUse) {
          // when we are not reusing
          evalSym(history)(oldCtx)
          val symCond = Stack.popS(condTy)
          ExploreTree.fillWithIfElse(symCond.s)
        }
        if (cond.toInt != 0) {
          ExploreTree.moveCursor(true)
          eval(thn, restK _, mkont, restK _ :: trail)(ct2)
        } else {
          ExploreTree.moveCursor(false)
          eval(els, restK _, mkont, restK _ :: trail)(ct2)
        }
        ()
      case Br(label) =>
        info(s"Jump to $label")
        val (oldCtx, history, ct1) = ct.clearHistory
        if (isSymStackInUse) {
          evalSym(history)(oldCtx)
        }
        trail(label)(ct1)(mkont)
      case BrIf(label) =>
        val (ty, ct1) = ct.pop()
        val cond = Stack.popC(ty)
        val (oldCtx, history, ct2) = ct1.clearHistory
        info(s"The br_if(${label})'s condition is ", cond.toInt)
        if (isSymStackInUse) {
          evalSym(history)(oldCtx)
          val symCond = Stack.popS(ty)
          ExploreTree.fillWithIfElse(symCond.s)
        }
        if (cond.toInt != 0) {
          info(s"Jump to $label")
          ExploreTree.moveCursor(true)
          trail(label)(ct2)(mkont)
        } else {
          info(s"Continue")
          ExploreTree.moveCursor(false)
          eval(rest, kont, mkont, trail)(ct2)
        }
        ()
      case BrTable(labels, default) =>
        val (ty, ct1) = ct.pop()
        val label = Stack.popC(ty)
        val (oldCtx, history, ct2) = ct1.clearHistory
        if (isSymStackInUse) {
          evalSym(history)(oldCtx)
        }
        def aux(choices: List[Int], idx: Int): Rep[Unit] = {
          if (choices.isEmpty) trail(default)(ct2)(mkont)
          else {
            val cond = (label - toStagedNum(I32V(idx))).isZero()
            if (isSymStackInUse) {
              val labelSym = Stack.peekS(ty)
              val condSym = (labelSym - toStagedSymbolicNum(I32V(idx))).isZero()
              ExploreTree.fillWithIfElse(condSym.s)
            }
            if (cond.toInt != 0) {
              ExploreTree.moveCursor(true)
              trail(choices.head)(ct2)(mkont)
            }
            else {
              ExploreTree.moveCursor(false)
              aux(choices.tail, idx + 1)
            }
          }
        }
        aux(labels, 0)
        if (isSymStackInUse) {
          Stack.popS(ty)
        }
        ()
      case Return        =>
        // return instruction is also stack-polymorphic
        val (oldCtx, history, ct2) = ct.clearHistory
        if (isSymStackInUse) {
          evalSym(history)(oldCtx)
        }
        trail.last(ct2)(mkont)
      case Call(f)       => evalCall(rest, kont, mkont, trail, f, false)(ct)
      case ReturnCall(f) => evalCall(rest, kont, mkont, trail, f, true)(ct)
      case _ =>
        val todo = "todo-op".reflectCtrlWith[Unit]()
        eval(rest, kont, mkont, trail)(ct)
    }
  }

  def replayAndClearHistory(ct: ContextTransition): ContextTransition = {
    val (oldCtx, history, ct1) = ct.clearHistory
    if (isSymStackInUse) {
      evalSym(history)(oldCtx)
    }
    ct1
  }

  // call the symbolic interpreter to evaluate the history that just executed by
  // concrete interpreter
  def evalSym(history: List[Instr])
             (ctx: Context): Rep[Unit] = {
    // val func = topFun((_: Rep[Unit]) => evalS(history.reverse))
    // func(())
    evalS(history.reverse)(ctx)
  }

  def evalS(insts: List[Instr])
           (ctx: Context): Rep[Unit] = {
    if (insts.isEmpty) return ()

    // Predef.println(s"[DEBUG] Evaluating instructions: ${insts.mkString(", ")}")
    // Predef.println(s"[DEBUG] Current context: $ctx")
    val (inst, rest) = (insts.head, insts.tail)
    inst match {
      case Drop =>
        val (ty, newCtx) = ctx.pop()
        Stack.popS(ty)
        evalS(rest)(newCtx)
      case WasmConst(num) =>
        Stack.pushS(toStagedSymbolicNum(num))
        val newCtx = ctx.push(num.tipe(module))
        evalS(rest)(newCtx)
      case Symbolic(ty) =>
        val id = Stack.popS(ty)
        val symVal = id.makeSymbolic(ty)
        Stack.pushS(symVal)
        val newCtx = ctx.pop()._2.push(ty)
        evalS(rest)(newCtx)
      case LocalGet(i) =>
        Stack.pushS(Frames.getS(i)(ctx))
        val newCtx = ctx.push(ctx.frameTypes(i))
        evalS(rest)(newCtx)
      case LocalSet(i) =>
        val (ty, newCtx) = ctx.pop()
        val sym = Stack.popS(ty)
        Frames.setS(i, sym)
        evalS(rest)(newCtx)
      case LocalTee(i) =>
        val ty = ctx.pop()._1
        val sym = Stack.peekS(ty)
        Frames.setS(i, sym)
        evalS(rest)(ctx)
      case GlobalGet(i) =>
        Stack.pushS(Globals.getS(i))
        val newCtx = ctx.push(module.globals(i).ty.ty)
        evalS(rest)(newCtx)
      case GlobalSet(i) =>
        val (ty, newCtx) = ctx.pop()
        val sym = Stack.popS(ty)
        module.globals(i).ty match {
          case GlobalType(tipe, true) => {
            Globals.setS(i, sym)
          }
          case _ => throw new Exception("Cannot set immutable global")
        }
        evalS(rest)(newCtx)
      case Nop => evalS(rest)(ctx)
      case Store(StoreOp(align, offset, ty, None)) => ???
      case Load(LoadOp(align, offset, ty, None, None)) => ???
      case MemorySize => ???
      case MemoryGrow => ???
      case MemoryFill => ???
      case Unreachable => unreachable()
      case Test(op) =>
        val (ty, newCtx1) = ctx.pop()
        val s = Stack.popS(ty)
        Stack.pushS(evalTestOpS(op, s))
        val newCtx2 = newCtx1.push(s.tipe)
        evalS(rest)(newCtx2)
      case Unary(op) =>
        val (ty, newCtx1) = ctx.pop()
        val s = Stack.popS(ty)
        val res = evalUnaryOpS(op, s)
        Stack.pushS(res)
        val newCtx2 = newCtx1.push(res.tipe)
        evalS(rest)(newCtx2)
      case Binary(op) =>
        val (ty2, newCtx1) = ctx.pop()
        val s2 = Stack.popS(ty2)
        val (ty1, newCtx2) = newCtx1.pop()
        val s1 = Stack.popS(ty1)
        val res = evalBinOpS(op, s1, s2)
        Stack.pushS(res)
        val newCtx3 = newCtx2.push(res.tipe)
        evalS(rest)(newCtx3)
      case Compare(op) =>
        val (ty2, newCtx1) = ctx.pop()
        val s2 = Stack.popS(ty2)
        val (ty1, newCtx2) = newCtx1.pop()
        val s1 = Stack.popS(ty1)
        val res = evalRelOpS(op, s1, s2)
        Stack.pushS(res)
        val newCtx3 = newCtx2.push(res.tipe)
        evalS(rest)(newCtx3)
      case WasmBlock(ty, inner) => ()
      case Loop(ty, inner) => ()
      case If(ty, thn, els) => ()
      case Br(label) => ()
      case BrIf(label) => ()
      case BrTable(labels, default) => ()
      case Return        => ()
      case Call(f)       => ()
      case ReturnCall(f) => ()
      case _ =>
        val todo = "todo-op".reflectCtrlWith[Unit]()
        evalS(rest)(ctx)
    }
  }

  def forwardKont: Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => mk(()))


  def evalCall(rest: List[Instr],
               kont: CleanCT => Rep[Cont[Unit]],
               mkont: Rep[MCont[Unit]],
               trail: Trail[Unit],
               funcIndex: Int,
               isTail: Boolean)
              (implicit ct: ContextTransition): Rep[Unit] = {
    val (oldCtx, history, ct1) = ct.clearHistory
    if (isSymStackInUse) {
      evalSym(history)(oldCtx)
    }
    module.funcs(funcIndex) match {
      case FuncDef(_, FuncBodyDef(ty, _, bodyLocals, body)) =>
        val locals = bodyLocals ++ ty.inps
        val callee =
          if (compileCache.contains(funcIndex)) {
            compileCache(funcIndex)
          } else {
            val callee = topFun((mk: Rep[MCont[Unit]]) => {
              info(s"Entered the function at $funcIndex, stackSize =", Stack.size)
              // the return instruction is also stack polymorphic
              def retK(ct: CleanCT): Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => {
                info(s"Exiting the function at $funcIndex, stackSize =", Stack.size)
                val offset = ct.ctx.stackTypes.size - ty.out.size
                Stack.shiftC(offset, ty.out.size)
                Stack.shiftS(offset, ty.out.size)
                mk(())
              })
              eval(body, retK _, mk, retK _::Nil)(CleanCT(Context(Nil, locals)))
            })
            compileCache(funcIndex) = callee
            callee
          }
        // Predef.println(s"[DEBUG] locals size: ${locals.size}")
        val ct2 = ct1.take(ty.inps.size)
        val argsC = Stack.takeC(ty.inps)
        val argsS = Stack.takeS(ty.inps)
        val exitSize = ty.out.size + ct2.endCtx.stackTypes.size
        if (isTail) {
          // when tail call, return to the caller's return continuation
          Frames.popFrameC(ct2.endCtx.frameTypes.size)
          Frames.pushFrameC(locals)
          Frames.putAllC(argsC)
          if (isSymStackInUse) {
            Frames.popFrameS(ct2.endCtx.frameTypes.size)
            Frames.pushFrameS(locals)
            Frames.putAllS(argsS)
          }
          callee(mkont)
        } else {
          // We make a new trail by `restK`, since function creates a new block to escape
          // (more or less like `return`)
          val restK: Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => {
            info(s"Exiting the function at $funcIndex, stackSize =", Stack.size)
            Frames.popFrameC(locals.size)
            Frames.popFrameS(locals.size)
            val newCtx = ct2.endCtx.copy(stackTypes = ty.out.reverse ++ ct2.endCtx.stackTypes)
            eval(rest, kont, mk, trail)(CleanCT(newCtx))
          })
          val dummy = makeDummy
          val newMKont: Rep[MCont[Unit]] = funHere((_u: Rep[Unit]) => {
            restK(mkont)
          }, dummy)
          Frames.pushFrameC(locals)
          Frames.putAllC(argsC)
          if (isSymStackInUse) {
            Frames.pushFrameS(locals)
            Frames.putAllS(argsS)
          }
          callee(newMKont)
        }
      case Import("console", "log", _)
         | Import("spectest", "print_i32", _) =>
        //println(s"[DEBUG] current stack: $stack")
        val (ty, ct2) = ct1.pop()
        val v = Stack.popC(ty)
        Stack.popS(ty)
        println(v.toInt)
        eval(rest, kont, mkont, trail)(ct2)
      case Import("console", "assert", _) =>
        val (ty, ct2) = ct1.pop()
        val v = Stack.popC(ty)
        // TODO: We should also add s into exploration tree
        val s = Stack.popS(ty)
        runtimeAssert(v.toInt != 0)
        eval(rest, kont, mkont, trail)(ct2)
      case Import(_, _, _) => throw new Exception(s"Unknown import at $funcIndex")
      case _               => throw new Exception(s"Definition at $funcIndex is not callable")
    }
  }

  def evalTestOpC(op: TestOp, value: StagedConcreteNum): StagedConcreteNum = op match {
    case Eqz(_) => value.isZero
  }

  def evalTestOpS(op: TestOp, value: StagedSymbolicNum): StagedSymbolicNum = op match {
    case Eqz(_) => value.isZero
  }

  def evalUnaryOpC(op: UnaryOp, value: StagedConcreteNum): StagedConcreteNum = op match {
    case Clz(_) => value.clz()
    case Ctz(_) => value.ctz()
    case Popcnt(_) => value.popcnt()
    case _ => ???
  }

  def evalUnaryOpS(op: UnaryOp, value: StagedSymbolicNum): StagedSymbolicNum = op match {
    case Clz(_) => value.clz()
    case Ctz(_) => value.ctz()
    case Popcnt(_) => value.popcnt()
    case _ => ???
  }

  def evalBinOpC(op: BinOp, v1: StagedConcreteNum, v2: StagedConcreteNum): StagedConcreteNum = op match {
    case Add(_) => v1 + v2
    case Mul(_) => v1 * v2
    case Sub(_) => v1 - v2
    case Shl(_) => v1 << v2
    // case ShrS(_) => v1 >> v2 // TODO: signed shift right
    case ShrU(_) => v1 >> v2
    case And(_) => v1 & v2
    case DivS(_) => v1 / v2
    case DivU(_) => v1 / v2
    case _ =>
      throw new Exception(s"Unknown binary operation $op")
  }

  def evalBinOpS(op: BinOp, v1: StagedSymbolicNum, v2: StagedSymbolicNum): StagedSymbolicNum = op match {
    case Add(_) => v1 + v2
    case Mul(_) => v1 * v2
    case Sub(_) => v1 - v2
    case Shl(_) => v1 << v2
    // case ShrS(_) => v1 >> v2 // TODO: signed shift right
    case ShrU(_) => v1 >> v2
    case And(_) => v1 & v2
    case DivS(_) => v1 / v2
    case DivU(_) => v1 / v2
    case _ =>
      throw new Exception(s"Unknown binary operation $op")
  }

  def evalRelOpC(op: RelOp, v1: StagedConcreteNum, v2: StagedConcreteNum): StagedConcreteNum = op match {
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

  def evalRelOpS(op: RelOp, v1: StagedSymbolicNum, v2: StagedSymbolicNum): StagedSymbolicNum = op match {
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

  def evalTop(mkont: Rep[MCont[Unit]], main: Option[String]): Rep[Unit] = {
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
    val (instrs, locals) = (funBody.body, funBody.locals)
    resetStacks()
    Frames.pushFrameC(locals)
    Frames.pushFrameS(locals)
    eval(instrs, _ => forwardKont, mkont, ((_: CleanCT) => forwardKont)::Nil)(CleanCT(Context(Nil, locals)))
    Frames.popFrameC(locals.size)
    Frames.popFrameS(locals.size)
  }

  def evalTop(main: Option[String], printRes: Boolean): Rep[Unit] = {
    val haltK: Rep[Unit] => Rep[Unit] = (_) => {
      info("Exiting the program...")
      if (printRes) {
        Stack.print()
      }
      ExploreTree.fillWithFinished()
      "no-op".reflectCtrlWith[Unit]()
    }
    val temp: Rep[MCont[Unit]] = topFun(haltK)
    evalTop(temp, main)
  }

  def runtimeAssert(b: Rep[Boolean]): Rep[Unit] = {
    "assert-true".reflectCtrlWith[Unit](b)
  }

  // stack operations
  object Stack {
    def shiftC(offset: Int, size: Int) = {
      if (offset > 0) {
        "stack-shift".reflectCtrlWith[Unit](offset, size)
      }
    }

    def shiftS(offset: Int, size: Int) = {
      if (offset > 0) {
        "sym-stack-shift".reflectCtrlWith[Unit](offset, size)
      }
    }

    def initialize(): Rep[Unit] = {
      "stack-init".reflectCtrlWith[Unit]()
    }

    def popC(ty: ValueType): StagedConcreteNum = ty match {
      case NumType(I32Type) => I32C("stack-pop".reflectCtrlWith[Num]())
      case NumType(I64Type) => I64C("stack-pop".reflectCtrlWith[Num]())
      case NumType(F32Type) => F32C("stack-pop".reflectCtrlWith[Num]())
      case NumType(F32Type) => F64C("stack-pop".reflectCtrlWith[Num]())
    }

    def popS(ty: ValueType): StagedSymbolicNum = ty match {
      case NumType(I32Type) => I32S("sym-stack-pop".reflectCtrlWith[SymVal]())
      case NumType(I64Type) => I64S("sym-stack-pop".reflectCtrlWith[SymVal]())
      case NumType(F32Type) => F32S("sym-stack-pop".reflectCtrlWith[SymVal]())
      case NumType(F64Type) => F64S("sym-stack-pop".reflectCtrlWith[SymVal]())
    }

    def peekC(ty: ValueType): StagedConcreteNum = ty match {
      case NumType(I32Type) => I32C("stack-peek".reflectCtrlWith[Num]())
      case NumType(I64Type) => I64C("stack-peek".reflectCtrlWith[Num]())
      case NumType(F32Type) => F32C("stack-peek".reflectCtrlWith[Num]())
      case NumType(F32Type) => F64C("stack-peek".reflectCtrlWith[Num]())
    }

    def peekS(ty: ValueType): StagedSymbolicNum = ty match {
      case NumType(I32Type) => I32S("sym-stack-peek".reflectCtrlWith[SymVal]())
      case NumType(I64Type) => I64S("sym-stack-peek".reflectCtrlWith[SymVal]())
      case NumType(F32Type) => F32S("sym-stack-peek".reflectCtrlWith[SymVal]())
      case NumType(F64Type) => F64S("sym-stack-peek".reflectCtrlWith[SymVal]())
    }

    def pushC(num: StagedConcreteNum) = num match {
      case I32C(v) => "stack-push".reflectCtrlWith[Unit](v)
      case I64C(v) => "stack-push".reflectCtrlWith[Unit](v)
      case F32C(v) => "stack-push".reflectCtrlWith[Unit](v)
      case F64C(v) => "stack-push".reflectCtrlWith[Unit](v)
    }

    def pushS(num: StagedSymbolicNum) = num match {
      case I32S(s) => "sym-stack-push".reflectCtrlWith[Unit](s)
      case I64S(s) => "sym-stack-push".reflectCtrlWith[Unit](s)
      case F32S(s) => "sym-stack-push".reflectCtrlWith[Unit](s)
      case F64S(s) => "sym-stack-push".reflectCtrlWith[Unit](s)
    }

    def takeC(types: List[ValueType]): List[StagedConcreteNum] = types match {
      case Nil => Nil
      case t :: ts =>
        val v = popC(t)
        val rest = takeC(ts)
        v :: rest
    }

    def takeS(types: List[ValueType]): List[StagedSymbolicNum] = types match {
      case Nil => Nil
      case t :: ts =>
        val v = popS(t)
        val rest = takeS(ts)
        v :: rest
    }

    def print(): Rep[Unit] = {
      "stack-print".reflectCtrlWith[Unit]()
    }

    def size: Rep[Int] = {
      "stack-size".reflectCtrlWith[Int]()
    }
  }

  object Frames {
    def getC(i: Int)(implicit ctx: Context): StagedConcreteNum = {
      // val offset = ctx.frameTypes.take(i).map(_.size).sum
      ctx.frameTypes(i) match {
        case NumType(I32Type) => I32C("frame-get".reflectCtrlWith[Num](i))
        case NumType(I64Type) => I64C("frame-get".reflectCtrlWith[Num](i))
        case NumType(F32Type) => F32C("frame-get".reflectCtrlWith[Num](i))
        case NumType(F64Type) => F64C("frame-get".reflectCtrlWith[Num](i))
      }
    }

    def getS(i: Int)(implicit ctx: Context): StagedSymbolicNum = {
      ctx.frameTypes(i) match {
        case NumType(I32Type) => I32S("sym-frame-get".reflectCtrlWith[SymVal](i))
        case NumType(I64Type) => I64S("sym-frame-get".reflectCtrlWith[SymVal](i))
        case NumType(F32Type) => F32S("sym-frame-get".reflectCtrlWith[SymVal](i))
        case NumType(F64Type) => F64S("sym-frame-get".reflectCtrlWith[SymVal](i))
      }
    }

    def setC(i: Int, v: StagedConcreteNum): Rep[Unit] = {
      v match {
        case I32C(v) => "frame-set".reflectCtrlWith[Unit](i, v)
        case I64C(v) => "frame-set".reflectCtrlWith[Unit](i, v)
        case F32C(v) => "frame-set".reflectCtrlWith[Unit](i, v)
        case F64C(v) => "frame-set".reflectCtrlWith[Unit](i, v)
      }
    }

    def setS(i: Int, s: StagedSymbolicNum): Rep[Unit] = {
      s match {
        case I32S(s) => "sym-frame-set".reflectCtrlWith[Unit](i, s)
        case I64S(s) => "sym-frame-set".reflectCtrlWith[Unit](i, s)
        case F32S(s) => "sym-frame-set".reflectCtrlWith[Unit](i, s)
        case F64S(s) => "sym-frame-set".reflectCtrlWith[Unit](i, s)
      }
    }

    def pushFrameC(locals: List[ValueType]): Rep[Unit] = {
      // Predef.println(s"[DEBUG] push frame: $locals")
      val size = locals.size
      "frame-push".reflectCtrlWith[Unit](size)
    }

    def pushFrameS(locals: List[ValueType]): Rep[Unit] = {
      // Predef.println(s"[DEBUG] push frame: $locals")
      val size = locals.size
      "sym-frame-push".reflectCtrlWith[Unit](size)
    }

    def popFrameC(size: Int): Rep[Unit] = {
      "frame-pop".reflectCtrlWith[Unit](size)
    }

    def popFrameS(size: Int): Rep[Unit] = {
      "sym-frame-pop".reflectCtrlWith[Unit](size)
    }

    def putAllC(args: List[StagedConcreteNum]): Rep[Unit] = {
      for ((arg, i) <- args.view.reverse.zipWithIndex) {
        Frames.setC(i, arg)
      }
    }

    def putAllS(args: List[StagedSymbolicNum]): Rep[Unit] = {
      for ((arg, i) <- args.view.reverse.zipWithIndex) {
        Frames.setS(i, arg)
      }
    }
  }

  object Memory {
    def storeInt(base: Rep[Int], offset: Int, value: Rep[Int]): Rep[Unit] = {
      "memory-store-int".reflectCtrlWith[Unit](base, offset, value)
      // todo: store symbolic value to memory via extract/concat operation
    }

    def loadIntC(base: Rep[Int], offset: Int): StagedConcreteNum = {
      I32C("I32V".reflectCtrlWith[Num]("memory-load-int".reflectCtrlWith[Int](base, offset)))
    }

    def loadIntS(base: Rep[Int], offset: Int): StagedSymbolicNum = {
      I32S("sym-load-int-todo".reflectCtrlWith[SymVal](base, offset))
    }

    // Returns the previous memory size on success, or -1 if the memory cannot be grown.
    def grow(delta: Rep[Int]): Rep[Int] = {
      "memory-grow".reflectCtrlWith[Int](delta)
    }
  }

  def resetStacks(): Rep[Unit] = {
    "reset-stacks".reflectCtrlWith[Unit]()
  }

  // call unreachable
  def unreachable(): Rep[Unit] = {
    "unreachable".reflectCtrlWith[Unit]()
  }

  def info(xs: Rep[_]*): Rep[Unit] = {
    "info".reflectCtrlWith[Unit](xs: _*)
  }

  // runtime values
  object Values {
    def I32V(i: Rep[Int]): Rep[Num] = {
      "I32V".reflectCtrlWith[Num](i)
    }

    def I64V(i: Rep[Long]): Rep[Num] = {
      "I64V".reflectCtrlWith[Num](i)
    }
  }

  // global read/write
  object Globals {
    def getC(i: Int): StagedConcreteNum = {
      module.globals(i).ty match {
        case GlobalType(NumType(I32Type), _) => I32C("global-get".reflectCtrlWith[Num](i))
        case GlobalType(NumType(I64Type), _) => I64C("global-get".reflectCtrlWith[Num](i))
        case GlobalType(NumType(F32Type), _) => F32C("global-get".reflectCtrlWith[Num](i))
        case GlobalType(NumType(F64Type), _) => F64C("global-get".reflectCtrlWith[Num](i))
      }
    }

    def getS(i: Int): StagedSymbolicNum = {
      module.globals(i).ty match {
        case GlobalType(NumType(I32Type), _) => I32S("sym-global-get".reflectCtrlWith[SymVal](i))
        case GlobalType(NumType(I64Type), _) => I64S("sym-global-get".reflectCtrlWith[SymVal](i))
        case GlobalType(NumType(F32Type), _) => F32S("sym-global-get".reflectCtrlWith[SymVal](i))
        case GlobalType(NumType(F64Type), _) => F64S("sym-global-get".reflectCtrlWith[SymVal](i))
      }
    }

    def setC(i: Int, v: StagedConcreteNum): Rep[Unit] = {
      module.globals(i).ty match {
        case GlobalType(NumType(I32Type), _) => "global-set".reflectCtrlWith[Unit](i, v.i)
        case GlobalType(NumType(I64Type), _) => "global-set".reflectCtrlWith[Unit](i, v.i)
        case GlobalType(NumType(F32Type), _) => "global-set".reflectCtrlWith[Unit](i, v.i)
        case GlobalType(NumType(F64Type), _) => "global-set".reflectCtrlWith[Unit](i, v.i)
      }
    }

    def setS(i: Int, s: StagedSymbolicNum): Rep[Unit] = {
      module.globals(i).ty match {
        case GlobalType(NumType(I32Type), _) => "sym-global-set".reflectCtrlWith[Unit](i, s.s)
        case GlobalType(NumType(I64Type), _) => "sym-global-set".reflectCtrlWith[Unit](i, s.s)
        case GlobalType(NumType(F32Type), _) => "sym-global-set".reflectCtrlWith[Unit](i, s.s)
        case GlobalType(NumType(F64Type), _) => "sym-global-set".reflectCtrlWith[Unit](i, s.s)
      }
    }
  }

  // Exploration tree, 
  object ExploreTree {
    def fillWithIfElse(s: Rep[SymVal]): Rep[Unit] = {
      "tree-fill-if-else".reflectCtrlWith[Unit](s)
    }

    def fillWithFinished(): Rep[Unit] = {
      "tree-fill-finished".reflectCtrlWith[Unit]()
    }

    def moveCursor(branch: Boolean): Rep[Unit] = {
      // when moving cursor from to an unexplored node, we need to change the reuse state
      "tree-move-cursor".reflectCtrlWith[Unit](branch)
    }

    def print(): Rep[Unit] = {
      "tree-print".reflectCtrlWith[Unit]()
    }

    def dumpGraphiviz(filePath: String): Rep[Unit] = {
      "tree-dump-graphviz".reflectCtrlWith[Unit](filePath)
    }
  }

  object SymEnv {
    def read(sym: Rep[SymVal]): Rep[Num] = {
      "sym-env-read".reflectCtrlWith[Num](sym)
    }
  }

  object ReuseManager {
    def isReusing: Rep[Boolean] = {
      "reuse-is-reusing".reflectCtrlWith[Boolean]()
    }

    def turnOnReuse(): Rep[Unit] = {
      "reuse-turn-on".reflectCtrlWith[Unit]()
    }

    def turnOffReuse(): Rep[Unit] = {
      "reuse-turn-off".reflectCtrlWith[Unit]()
    }
  }

  // runtime Num type
  implicit class StagedConcreteNumOps(num: StagedConcreteNum) {

    def makeSymbolic(ty: ValueType): StagedSymbolicNum = num match {
      case I32C(x) => I32S("make-symbolic-concrete".reflectCtrlWith[SymVal](num.toInt))
    }

    def toInt: Rep[Int] = "num-to-int".reflectCtrlWith[Int](num.i)

    def isZero(): StagedConcreteNum = num match {
      case I32C(x_c) => I32C(Values.I32V("is-zero".reflectCtrlWith[Int](num.toInt)))
    }

    def clz(): StagedConcreteNum = num match {
      case I32C(x) => I32C("clz".reflectCtrlWith[Num](x))
      case I64C(x) => I64C("clz".reflectCtrlWith[Num](x))
    }

    def ctz(): StagedConcreteNum = num match {
      case I32C(x) => I32C("ctz".reflectCtrlWith[Num](x))
      case I64C(x) => I64C("ctz".reflectCtrlWith[Num](x))
    }

    def popcnt(): StagedConcreteNum = num match {
      case I32C(x) => I32C("popcnt".reflectCtrlWith[Num](x))
      case I64C(x) => I64C("popcnt".reflectCtrlWith[Num](x))
    }

    def +(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num, rhs) match {
        case (I32C(x), I32C(y)) => I32C("binary-add".reflectCtrlWith[Num](x, y))
        case (I64C(x), I64C(y)) => I64C("binary-add".reflectCtrlWith[Num](x, y))
        case (F32C(x), F32C(y)) => F32C("binary-add".reflectCtrlWith[Num](x, y))
        case (F64C(x), F64C(y)) => F64C("binary-add".reflectCtrlWith[Num](x, y))
      }
    }

    def -(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num, rhs) match {
        case (I32C(x), I32C(y)) => I32C("binary-sub".reflectCtrlWith[Num](x, y))
        case (I64C(x), I64C(y)) => I64C("binary-sub".reflectCtrlWith[Num](x, y))
        case (F32C(x), F32C(y)) => F32C("binary-sub".reflectCtrlWith[Num](x, y))
        case (F64C(x), F64C(y)) => F64C("binary-sub".reflectCtrlWith[Num](x, y))
      }
    }

    def *(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num, rhs) match {
        case (I32C(x), I32C(y)) => I32C("binary-mul".reflectCtrlWith[Num](x, y))
        case (I64C(x), I64C(y)) => I64C("binary-mul".reflectCtrlWith[Num](x, y))
        case (F32C(x), F32C(y)) => F32C("binary-mul".reflectCtrlWith[Num](x, y))
        case (F64C(x), F64C(y)) => F64C("binary-mul".reflectCtrlWith[Num](x, y))
      }
    }

    def /(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num, rhs) match {
        case (I32C(x), I32C(y)) => I32C("binary-div".reflectCtrlWith[Num](x, y))
        case (I64C(x), I64C(y)) => I64C("binary-div".reflectCtrlWith[Num](x, y))
        case (F32C(x), F32C(y)) => F32C("binary-div".reflectCtrlWith[Num](x, y))
        case (F64C(x), F64C(y)) => F64C("binary-div".reflectCtrlWith[Num](x, y))
      }
    }

    def <<(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num, rhs) match {
        case (I32C(x), I32C(y)) => I32C("binary-shl".reflectCtrlWith[Num](x, y))
        case (I64C(x), I64C(y)) => I64C("binary-shl".reflectCtrlWith[Num](x, y))
        case (F32C(x), F32C(y)) => F32C("binary-shl".reflectCtrlWith[Num](x, y))
        case (F64C(x), F64C(y)) => F64C("binary-shl".reflectCtrlWith[Num](x, y))
      }
    }

    def >>(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num, rhs) match {
        case (I32C(x), I32C(y)) => I32C("binary-shr".reflectCtrlWith[Num](x, y))
        case (I64C(x), I64C(y)) => I64C("binary-shr".reflectCtrlWith[Num](x, y))
        case (F32C(x), F32C(y)) => F32C("binary-shr".reflectCtrlWith[Num](x, y))
        case (F64C(x), F64C(y)) => F64C("binary-shr".reflectCtrlWith[Num](x, y))
      }
    }

    def &(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num, rhs) match {
        case (I32C(x), I32C(y)) => I32C("binary-and".reflectCtrlWith[Num](x, y))
        case (I64C(x), I64C(y)) => I64C("binary-and".reflectCtrlWith[Num](x, y))
        case (F32C(x), F32C(y)) => F32C("binary-and".reflectCtrlWith[Num](x, y))
        case (F64C(x), F64C(y)) => F64C("binary-and".reflectCtrlWith[Num](x, y))
      }
    }

    def numEq(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num, rhs) match {
        case (I32C(x), I32C(y)) => I32C("relation-eq".reflectCtrlWith[Num](x, y))
        case (I64C(x), I64C(y)) => I32C("relation-eq".reflectCtrlWith[Num](x, y))
      }
    }

    def numNe(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num, rhs) match {
        case (I32C(x), I32C(y)) => I32C("relation-ne".reflectCtrlWith[Num](x, y))
        case (I64C(x), I64C(y)) => I32C("relation-ne".reflectCtrlWith[Num](x, y))
      }
    }

    def <(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num, rhs) match {
        case (I32C(x), I32C(y)) => I32C("relation-lt".reflectCtrlWith[Num](x, y))
        case (I64C(x), I64C(y)) => I32C("relation-lt".reflectCtrlWith[Num](x, y))
      }
    }

    def ltu(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num, rhs) match {
        case (I32C(x), I32C(y)) => I32C("relation-ltu".reflectCtrlWith[Num](x, y))
        case (I64C(x), I64C(y)) => I32C("relation-ltu".reflectCtrlWith[Num](x, y))
      }
    }

    def >(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num, rhs) match {
        case (I32C(x), I32C(y)) => I32C("relation-gt".reflectCtrlWith[Num](x, y))
        case (I64C(x), I64C(y)) => I32C("relation-gt".reflectCtrlWith[Num](x, y))
      }
    }

    def gtu(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num, rhs) match {
        case (I32C(x), I32C(y)) => I32C("relation-gtu".reflectCtrlWith[Num](x, y))
        case (I64C(x), I64C(y)) => I32C("relation-gtu".reflectCtrlWith[Num](x, y))
      }
    }

    def <=(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num, rhs) match {
        case (I32C(x), I32C(y)) => I32C("relation-le".reflectCtrlWith[Num](x, y))
        case (I64C(x), I64C(y)) => I32C("relation-le".reflectCtrlWith[Num](x, y))
      }
    }

    def leu(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num, rhs) match {
        case (I32C(x), I32C(y)) => I32C("relation-leu".reflectCtrlWith[Num](x, y))
        case (I64C(x), I64C(y)) => I32C("relation-leu".reflectCtrlWith[Num](x, y))
      }
    }

    def >=(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num, rhs) match {
        case (I32C(x), I32C(y)) => I32C("relation-ge".reflectCtrlWith[Num](x, y))
        case (I64C(x), I64C(y)) => I32C("relation-ge".reflectCtrlWith[Num](x, y))
      }
    }

    def geu(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num, rhs) match {
        case (I32C(x), I32C(y)) => I32C("relation-geu".reflectCtrlWith[Num](x, y))
        case (I64C(x), I64C(y)) => I32C("relation-geu".reflectCtrlWith[Num](x, y))
      }
    }
  }

  implicit class StagedSymbolicNumOps(num: StagedSymbolicNum) {
    def makeSymbolic(ty: ValueType): StagedSymbolicNum = num match {
      case I32S(x) => I32S("make-symbolic".reflectCtrlWith[SymVal](x))
      case _ => throw new RuntimeException("Symbol index must be an i32")
    }

    def isZero(): StagedSymbolicNum = num match {
      case I32S(x) => I32S("sym-is-zero".reflectCtrlWith[SymVal](x))
    }

    def clz(): StagedSymbolicNum = num match {
      case I32S(x) => I32S("sym-clz".reflectCtrlWith[SymVal](x))
      case I64S(x) => I64S("sym-clz".reflectCtrlWith[SymVal](x))
    }

    def ctz(): StagedSymbolicNum = num match {
      case I32S(x) => I32S("sym-ctz".reflectCtrlWith[SymVal](x))
      case I64S(x) => I64S("sym-ctz".reflectCtrlWith[SymVal](x))
    }

    def popcnt(): StagedSymbolicNum = num match {
      case I32S(x) => I32S("sym-popcnt".reflectCtrlWith[SymVal](x))
      case I64S(x) => I64S("sym-popcnt".reflectCtrlWith[SymVal](x))
    }

    def +(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num, rhs) match {
        case (I32S(x), I32S(y)) => I32S("sym-binary-add".reflectCtrlWith[SymVal](x, y))
        case (I64S(x), I64S(y)) => I64S("sym-binary-add".reflectCtrlWith[SymVal](x, y))
        case (F32S(x), F32S(y)) => F32S("sym-binary-add".reflectCtrlWith[SymVal](x, y))
        case (F64S(x), F64S(y)) => F64S("sym-binary-add".reflectCtrlWith[SymVal](x, y))
      }
    }

    def -(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num, rhs) match {
        case (I32S(x), I32S(y)) => I32S("sym-binary-sub".reflectCtrlWith[SymVal](x, y))
        case (I64S(x), I64S(y)) => I64S("sym-binary-sub".reflectCtrlWith[SymVal](x, y))
        case (F32S(x), F32S(y)) => F32S("sym-binary-sub".reflectCtrlWith[SymVal](x, y))
        case (F64S(x), F64S(y)) => F64S("sym-binary-sub".reflectCtrlWith[SymVal](x, y))
      }
    }

    def *(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num, rhs) match {
        case (I32S(x), I32S(y)) => I32S("sym-binary-mul".reflectCtrlWith[SymVal](x, y))
        case (I64S(x), I64S(y)) => I64S("sym-binary-mul".reflectCtrlWith[SymVal](x, y))
        case (F32S(x), F32S(y)) => F32S("sym-binary-mul".reflectCtrlWith[SymVal](x, y))
        case (F64S(x), F64S(y)) => F64S("sym-binary-mul".reflectCtrlWith[SymVal](x, y))
      }
    }

    def /(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num, rhs) match {
        case (I32S(x), I32S(y)) => I32S("sym-binary-div".reflectCtrlWith[SymVal](x, y))
        case (I64S(x), I64S(y)) => I64S("sym-binary-div".reflectCtrlWith[SymVal](x, y))
        case (F32S(x), F32S(y)) => F32S("sym-binary-div".reflectCtrlWith[SymVal](x, y))
        case (F64S(x), F64S(y)) => F64S("sym-binary-div".reflectCtrlWith[SymVal](x, y))
      }
    }

    def <<(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num, rhs) match {
        case (I32S(x), I32S(y)) => I32S("sym-binary-shl".reflectCtrlWith[SymVal](x, y))
        case (I64S(x), I64S(y)) => I64S("sym-binary-shl".reflectCtrlWith[SymVal](x, y))
        case (F32S(x), F32S(y)) => F32S("sym-binary-shl".reflectCtrlWith[SymVal](x, y))
        case (F64S(x), F64S(y)) => F64S("sym-binary-shl".reflectCtrlWith[SymVal](x, y))
      }
    }

    def >>(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num, rhs) match {
        case (I32S(x), I32S(y)) => I32S("sym-binary-shr".reflectCtrlWith[SymVal](x, y))
        case (I64S(x), I64S(y)) => I64S("sym-binary-shr".reflectCtrlWith[SymVal](x, y))
        case (F32S(x), F32S(y)) => F32S("sym-binary-shr".reflectCtrlWith[SymVal](x, y))
        case (F64S(x), F64S(y)) => F64S("sym-binary-shr".reflectCtrlWith[SymVal](x, y))
      }
    }

    def &(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num, rhs) match {
        case (I32S(x), I32S(y)) => I32S("sym-binary-and".reflectCtrlWith[SymVal](x, y))
        case (I64S(x), I64S(y)) => I64S("sym-binary-and".reflectCtrlWith[SymVal](x, y))
        case (F32S(x), F32S(y)) => F32S("sym-binary-and".reflectCtrlWith[SymVal](x, y))
        case (F64S(x), F64S(y)) => F64S("sym-binary-and".reflectCtrlWith[SymVal](x, y))
      }
    }

    def numEq(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num, rhs) match {
        case (I32S(x), I32S(y)) => I32S("sym-relation-eq".reflectCtrlWith[SymVal](x, y))
        case (I64S(x), I64S(y)) => I32S("sym-relation-eq".reflectCtrlWith[SymVal](x, y))
      }
    }

    def numNe(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num, rhs) match {
        case (I32S(x), I32S(y)) => I32S("sym-relation-ne".reflectCtrlWith[SymVal](x, y))
        case (I64S(x), I64S(y)) => I32S("sym-relation-ne".reflectCtrlWith[SymVal](x, y))
      }
    }

    def <(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num, rhs) match {
        case (I32S(x), I32S(y)) => I32S("sym-relation-lt".reflectCtrlWith[SymVal](x, y))
        case (I64S(x), I64S(y)) => I32S("sym-relation-lt".reflectCtrlWith[SymVal](x, y))
      }
    }

    def ltu(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num, rhs) match {
        case (I32S(x), I32S(y)) => I32S("relation-ltu".reflectCtrlWith[SymVal](x, y))
        case (I64S(x), I64S(y)) => I32S("relation-ltu".reflectCtrlWith[SymVal](x, y))
      }
    }

    def >(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num, rhs) match {
        case (I32S(x), I32S(y)) => I32S("sym-relation-gt".reflectCtrlWith[SymVal](x, y))
        case (I64S(x), I64S(y)) => I32S("sym-relation-gt".reflectCtrlWith[SymVal](x, y))
      }
    }

    def gtu(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num, rhs) match {
        case (I32S(x), I32S(y)) => I32S("sym-relation-gtu".reflectCtrlWith[SymVal](x, y))
        case (I64S(x), I64S(y)) => I32S("sym-relation-gtu".reflectCtrlWith[SymVal](x, y))
      }
    }

    def <=(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num, rhs) match {
        case (I32S(x), I32S(y)) => I32S("sym-relation-le".reflectCtrlWith[SymVal](x, y))
        case (I64S(x), I64S(y)) => I32S("sym-relation-le".reflectCtrlWith[SymVal](x, y))
      }
    }

    def leu(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num, rhs) match {
        case (I32S(x), I32S(y)) => I32S("sym-relation-leu".reflectCtrlWith[SymVal](x, y))
        case (I64S(x), I64S(y)) => I32S("sym-relation-leu".reflectCtrlWith[SymVal](x, y))
      }
    }

    def >=(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num, rhs) match {
        case (I32S(x), I32S(y)) => I32S("sym-relation-ge".reflectCtrlWith[SymVal](x, y))
        case (I64S(x), I64S(y)) => I32S("sym-relation-ge".reflectCtrlWith[SymVal](x, y))
      }
    }

    def geu(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num, rhs) match {
        case (I32S(x), I32S(y)) => I32S("sym-relation-geu".reflectCtrlWith[SymVal](x, y))
        case (I64S(x), I64S(y)) => I32S("sym-relation-geu".reflectCtrlWith[SymVal](x, y))
      }
    }
  }

  implicit class SymbolicOps(s: Rep[SymVal]) {
    def not(): Rep[SymVal] = {
      "sym-not".reflectCtrlWith(s)
    }
  }
}

trait StagedWasmCppGen extends CGenBase with CppSAICodeGenBase {
  // clear include path and headers by first
  includePaths.clear()
  headers.clear()

  registerHeader("headers", "\"wasm.hpp\"")
  registerHeader("<functional>")
  registerHeader("<stdbool.h>")
  registerHeader("<stdint.h>")
  registerHeader("<variant>")

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, "stack-pop", _, _)
       | Node(_, "stack-peek", _, _)
       | Node(_, "sym-stack-pop", _, _)
      => false
    case _ => super.mayInline(n)
  }

  override def remap(m: Manifest[_]): String = {
    if (m.toString.endsWith("Num")) "Num"
    else if (m.toString.endsWith("Frame")) "Frame"
    else if (m.toString.endsWith("Stack")) "Stack"
    else if (m.toString.endsWith("Global")) "Global"
    else if (m.toString.endsWith("I32V")) "I32V"
    else if (m.toString.endsWith("I64V")) "I64V"
    else if (m.toString.endsWith("SymVal")) "SymVal"
    
    else super.remap(m)
  }

  override def traverse(n: Node): Unit = n match {
    case Node(_, "stack-push", List(value), _) =>
      emit("Stack.push("); shallow(value); emit(");\n")
    case Node(_, "sym-stack-push", List(s_value), _) =>
      emit("SymStack.push("); shallow(s_value); emit(");\n")
    case Node(_, "stack-drop", List(n), _) =>
      emit("Stack.drop("); shallow(n); emit(");\n")
    case Node(_, "stack-init", _, _) =>
      emit("Stack.initialize();\n")
    case Node(_, "stack-print", _, _) =>
      emit("Stack.print();\n")
    case Node(_, "frame-push", List(i), _) =>
      emit("Frames.pushFrame("); shallow(i); emit(");\n")
    case Node(_, "sym-frame-push", List(i), _) =>
      emit("SymFrames.pushFrame("); shallow(i); emit(");\n")
    case Node(_, "frame-pop", List(i), _) =>
      emit("Frames.popFrame("); shallow(i); emit(");\n")
    case Node(_, "frame-set", List(i, value), _) =>
      emit("Frames.set("); shallow(i); emit(", "); shallow(value); emit(");\n")
    case Node(_, "sym-frame-set", List(i, s_value), _) =>
      emit("SymFrames.set("); shallow(i); emit(", "); shallow(s_value); emit(");\n")
    case Node(_, "global-set", List(i, value), _) =>
      emit("Global.globalSet("); shallow(i); emit(", "); shallow(value); emit(");\n")
    // Note: The following code is copied from the traverse of CppBackend.scala, try to avoid duplicated code
    case n @ Node(f, "λ", (b: LMSBlock)::LMSConst(0)::rest, _) =>
      // TODO: Is a leading block followed by 0 a hint for top function?
      super.traverse(n)
    case n @ Node(f, "λ", (b: LMSBlock)::rest, _) =>
      val retType = remap(typeBlockRes(b.res))
      val argTypes = b.in.map(a => remap(typeMap(a))).mkString(", ")
      emitln(s"std::function<$retType(${argTypes})> ${quote(f)};")
      emit(quote(f)); emit(" = ")
      quoteTypedBlock(b, false, true, capture = "&")
      emitln(";")
    case _ => super.traverse(n)
  }

  override def shallow(n: Node): Unit = n match {
    case Node(_, "reset-stacks", _, _) =>
      emit("reset_stacks()")
    case Node(_, "frame-get", List(i), _) =>
      emit("Frames.get("); shallow(i); emit(")")
    case Node(_, "sym-frame-get", List(i), _) =>
      emit("SymFrames.get("); shallow(i); emit(")")
    case Node(_, "stack-drop", List(n), _) =>
      emit("Stack.drop("); shallow(n); emit(")")
    case Node(_, "stack-push", List(value), _) =>
      emit("Stack.push("); shallow(value); emit(")")
    case Node(_, "stack-shift", List(offset, size), _) =>
      emit("Stack.shift("); shallow(offset); emit(", "); shallow(size); emit(")")
    case Node(_, "sym-stack-shift", List(offset, size), _) =>
      emit("SymStack.shift("); shallow(offset); emit(", "); shallow(size); emit(")")
    case Node(_, "stack-pop", _, _) =>
      emit("Stack.pop()")
    case Node(_, "sym-stack-pop", _, _) =>
      emit("SymStack.pop()")
    case Node(_, "frame-pop", List(i), _) =>
      emit("Frames.popFrame("); shallow(i); emit(")")
    case Node(_, "sym-frame-pop", List(i), _) =>
      emit("SymFrames.popFrame("); shallow(i); emit(")")
    case Node(_, "stack-peek", _, _) =>
      emit("Stack.peek()")
    case Node(_, "sym-stack-peek", _, _) =>
      emit("SymStack.peek()")
    case Node(_, "stack-take", List(n), _) =>
      emit("Stack.take("); shallow(n); emit(")")
    case Node(_, "slice-reverse", List(slice), _) =>
      shallow(slice); emit(".reverse")
    case Node(_, "memory-store-int", List(base, offset, value), _) =>
      emit("Memory.storeInt("); shallow(base); emit(", "); shallow(offset); emit(", "); shallow(value); emit(")")
    case Node(_, "memory-load-int", List(base, offset), _) =>
      emit("Memory.loadInt("); shallow(base); emit(", "); shallow(offset); emit(")")
    case Node(_, "memory-grow", List(delta), _) =>
      emit("Memory.grow("); shallow(delta); emit(")")
    case Node(_, "stack-size", _, _) =>
      emit("Stack.size()")
    case Node(_, "global-get", List(i), _) =>
      emit("Global.globalGet("); shallow(i); emit(")")
    case Node(_, "is-zero", List(num), _) =>
      emit("(0 == "); shallow(num); emit(")")
    case Node(_, "sym-is-zero", List(s_num), _) =>
      shallow(s_num); emit(".is_zero()")
    case Node(_, "binary-add", List(lhs, rhs), _) =>
      shallow(lhs); emit(" + "); shallow(rhs)
    case Node(_, "binary-sub", List(lhs, rhs), _) =>
      // todo: avoid using c++ operator, use explicit method call so operator's precedence issues won't exist
      emit("("); shallow(lhs); emit(" - "); shallow(rhs); emit(")")
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
    case Node(_, "sym-binary-add", List(lhs, rhs), _) =>
      shallow(lhs); emit(".add("); shallow(rhs); emit(")")
    case Node(_, "sym-binary-sub", List(lhs, rhs), _) =>
      shallow(lhs); emit(".minus("); shallow(rhs); emit(")")
    case Node(_, "sym-binary-mul", List(lhs, rhs), _) =>
      shallow(lhs); emit(".mul("); shallow(rhs); emit(")")
    case Node(_, "sym-binary-div", List(lhs, rhs), _) =>
      shallow(lhs); emit(".div("); shallow(rhs); emit(")")
    case Node(_, "sym-relation-le", List(lhs, rhs), _) =>
      shallow(lhs); emit(".leq("); shallow(rhs); emit(")")
    case Node(_, "sym-relation-leu", List(lhs, rhs), _) =>
      shallow(lhs); emit(".leu("); shallow(rhs); emit(")")
    case Node(_, "sym-relation-ge", List(lhs, rhs), _) => 
      shallow(lhs); emit(".ge("); shallow(rhs); emit(")")
    case Node(_, "sym-relation-geu", List(lhs, rhs), _) =>
      shallow(lhs); emit(".geu("); shallow(rhs); emit(")")
    case Node(_, "sym-relation-eq", List(lhs, rhs), _) =>
      shallow(lhs); emit(".eq("); shallow(rhs); emit(")")
    case Node(_, "sym-relation-ne", List(lhs, rhs), _) =>
      shallow(lhs); emit(".neq("); shallow(rhs); emit(")")
    case Node(_, "num-to-int", List(num), _) =>
      shallow(num); emit(".toInt()")
    case Node(_, "make-symbolic", List(num), _) =>
      shallow(num); emit(".makeSymbolic()")
    case Node(_, "make-symbolic-concrete", List(num), _) => 
      emit("make_symbolic("); shallow(num); emit(")")
    case Node(_, "sym-env-read", List(sym), _) =>
      emit("SymEnv.read("); shallow(sym); emit(")")
    case Node(_, "assert-true", List(cond), _) =>
      emit("GENSYM_ASSERT("); shallow(cond); emit(")")
    case Node(_, "tree-fill-if-else", List(s), _) => 
      emit("ExploreTree.fillIfElseNode("); shallow(s); emit(")")
    case Node(_, "tree-fill-finished", List(), _) =>
      emit("ExploreTree.fillFinishedNode()")
    case Node(_, "tree-move-cursor", List(b), _) =>
      emit("ExploreTree.moveCursor("); shallow(b); emit(")")
    case Node(_, "tree-print", List(), _) =>
      emit("ExploreTree.print()")
    case Node(_, "tree-dump-graphviz", List(f), _) =>
      emit("ExploreTree.dump_graphviz("); shallow(f); emit(")")
    case Node(_, "sym-not", List(s), _) =>
      shallow(s); emit(".negate()")
    case Node(_, "reuse-is-reusing", List(), _) =>
      emit("Reuse.is_reusing()")
    case Node(_, "dummy", _, _) => emit("std::monostate()")
    case Node(_, "dummy-op", _, _) => emit("std::monostate()")
    case Node(_, "no-op", _, _) =>
      emit("std::monostate()")
    case _ => super.shallow(n)
  }

  override def registerTopLevelFunction(id: String, streamId: String = "general")(f: => Unit) =
  if (!registeredFunctions(id)) {
    //if (ongoingFun(streamId)) ???
    //ongoingFun += streamId
    registeredFunctions += id
    withStream(functionsStreams.getOrElseUpdate(id, {
      val functionsStream = new java.io.ByteArrayOutputStream()
      val functionsWriter = new java.io.PrintStream(functionsStream)
      (functionsWriter, functionsStream)
    })._1)(f)
    //ongoingFun -= streamId
  } else {
    // If a function is registered, don't re-register it.
    // withStream(functionsStreams(id)._1)(f)
  }

  override def emitAll(g: Graph, name: String)(m1: Manifest[_], m2: Manifest[_]): Unit = {
    val ng = init(g)
    emitHeaders(stream)
    emitln("""
    |/*****************************************
    |Emitting Generated Code
    |*******************************************/
    """.stripMargin)
    val src = run(name, ng)
    emitFunctionDecls(stream)
    emitDatastructures(stream)
    emitFunctions(stream)
    emit(src)
    emitln("""
    |/*****************************************
    |End of Generated Code
    |*******************************************/
    |int main(int argc, char *argv[]) {
    |  start_concolic_execution_with(Snippet);
    |  return 0;
    |}""".stripMargin)
  }
}

trait WasmToCppCompilerDriver[A, B] extends CppSAIDriver[A, B] with StagedWasmEvaluator { q =>
  override val codegen = new StagedWasmCppGen {
    val IR: q.type = q
    import IR._
  }
}

object WasmToCppCompiler {
  case class GeneratedCpp(source: String, headerFolders: List[String])

  def compile(moduleInst: ModuleInstance, main: Option[String], printRes: Boolean): GeneratedCpp = {
    println(s"Now compiling wasm module with entry function $main")
    val driver = new WasmToCppCompilerDriver[Unit, Unit] {
      def module: ModuleInstance = moduleInst
      def snippet(x: Rep[Unit]): Rep[Unit] = {
        evalTop(main, printRes)
      }
    }
    GeneratedCpp(driver.code, driver.codegen.includePaths.toList)
  }

  def compileToExe(moduleInst: ModuleInstance,
                   main: Option[String],
                   outputCpp: String,
                   outputExe: String,
                   printRes: Boolean,
                   macros: String*): Unit = {
    val generated = compile(moduleInst, main, printRes)
    val code = generated.source

    val writer = new java.io.PrintWriter(new java.io.File(outputCpp))
    try {
      writer.write(code)
    } finally {
      writer.close()
    }

    import sys.process._
    val includeFlags = generated.headerFolders.map(f => s"-I$f").mkString(" ")
    val macroFlags = macros.map(m => s"-D$m").mkString(" ")
    val command = s"g++ -std=c++20 $outputCpp -o $outputExe -O3 -g -l z3 " + includeFlags + " " + macroFlags
    if (command.! != 0) {
      throw new RuntimeException(s"Compilation failed for $outputCpp")
    }
  }

}


