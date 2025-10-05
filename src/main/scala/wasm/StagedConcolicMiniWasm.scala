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

object Counter {
  var currentId: Int = 0

  // WIR is the branch's corresponding ast, while the Int stands for the nth
  // branch of the AST(a WIR may contain multiple branches, e.g., br_table)
  private val dict = new HashMap[(WIR, Int), Int]()

  def reset(): Unit = {
    currentId = 0
    dict.clear()
  }

  def getId(wir: WIR, nth: Int = 0): Int = {
    if (dict.contains((wir, nth))) {
      dict((wir, nth))
    } else {
      val id = currentId
      currentId += 1
      dict((wir, nth)) = id
      id
    }

  }

  def getId(wir: WIR): Int = {
    getId(wir, 0)
  }
}
@virtualize
trait StagedWasmEvaluator extends SAIOps {
  def module: ModuleInstance

  case class StagedConcreteNum(tipe: ValueType, i: Rep[Num])


  case class StagedSymbolicNum(tipe: ValueType, s: Rep[SymVal])

  def toStagedNum(num: Num): StagedConcreteNum = {
    num match {
      case I32V(_) => StagedConcreteNum(NumType(I32Type), num)
      case I64V(_) => StagedConcreteNum(NumType(I64Type), num)
      case F32V(_) => StagedConcreteNum(NumType(F32Type), num)
      case F64V(_) => StagedConcreteNum(NumType(F64Type), num)
    }
  }

  def toStagedSymbolicNum(num: Num): StagedSymbolicNum = {
    num match {
      case I32V(_) => StagedSymbolicNum(NumType(I32Type), Concrete(num))
      case I64V(_) => StagedSymbolicNum(NumType(I64Type), Concrete(num))
      case F32V(_) => StagedSymbolicNum(NumType(F32Type), Concrete(num))
      case F64V(_) => StagedSymbolicNum(NumType(F64Type), Concrete(num))
    }
  }

  implicit class ValueTypeOps(ty: ValueType) {
    def size: Int = ty match {
      case NumType(I32Type) => 4
      case NumType(I64Type) => 8
      case NumType(F32Type) => 4
      case NumType(F64Type) => 8
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

  type MCont[A] = Unit => A
  type Cont[A] = (MCont[A]) => A
  type Trail[A] = List[Context => Rep[Cont[A]]]

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


  // TODO: maybe we don't need concern snapshot at compile time at all
  trait Snapshot

  // Create a snapshot of the symbolic execution, we should ensure that current symstack is in use
  // We need to store the control information, so we can resume the execution later
  def makeSnapshot(kont: Rep[Cont[Unit]], mkont: Rep[MCont[Unit]]): Rep[Snapshot] = {
    "snapshot-make".reflectCtrlWith[Snapshot](kont, mkont)
  }

  def eval(insts: List[Instr],
           kont: Context => Rep[Cont[Unit]],
           mkont: Rep[MCont[Unit]],
           trail: Trail[Unit])
          (implicit ctx: Context): Rep[Unit] = {
    if (insts.isEmpty) return kont(ctx)(mkont)

    // Predef.println(s"[DEBUG] Evaluating instructions: ${insts.mkString(", ")}")
    // Predef.println(s"[DEBUG] Current context: $ctx")

    val (inst, rest) = (insts.head, insts.tail)
    inst match {
      case Drop =>
        val (ty, newCtx) = ctx.pop()
        Stack.popC(ty)
        Stack.popS(ty)
        eval(rest, kont, mkont, trail)(newCtx)
      case WasmConst(num) =>
        Stack.pushC(toStagedNum(num))
        Stack.pushS(toStagedSymbolicNum(num))
        val newCtx = ctx.push(num.tipe(module))
        eval(rest, kont, mkont, trail)(newCtx)
      case Symbolic(ty) =>
        Stack.popC(ty)
        val id = Stack.popS(ty)
        val symVal = id.makeSymbolic(ty)
        val num = SymEnv.read(symVal.s)
        Stack.pushC(StagedConcreteNum(ty, num))
        Stack.pushS(symVal)
        val newCtx = ctx.pop()._2.push(ty)
        eval(rest, kont, mkont, trail)(newCtx)
      case LocalGet(i) =>
        Stack.pushC(Frames.getC(i))
        Stack.pushS(Frames.getS(i))
        val newCtx = ctx.push(ctx.frameTypes(i))
        eval(rest, kont, mkont, trail)(newCtx)
      case LocalSet(i) =>
        val (ty, newCtx) = ctx.pop()
        val num = Stack.popC(ty)
        val sym = Stack.popS(ty)
        Frames.setC(i, num)
        Frames.setS(i, sym)
        eval(rest, kont, mkont, trail)(newCtx)
      case LocalTee(i) =>
        val ty = ctx.pop()._1
        val num = Stack.peekC(ty)
        val sym = Stack.peekS(ty)
        Frames.setC(i, num)
        Frames.setS(i, sym)
        eval(rest, kont, mkont, trail)(ctx)
      case GlobalGet(i) =>
        Stack.pushC(Globals.getC(i))
        Stack.pushS(Globals.getS(i))
        val newCtx = ctx.push(module.globals(i).ty.ty)
        eval(rest, kont, mkont, trail)(newCtx)
      case GlobalSet(i) =>
        val (ty, newCtx) = ctx.pop()
        val num = Stack.popC(ty)
        val sym = Stack.popS(ty)
        module.globals(i).ty match {
          case GlobalType(tipe, true) => {
            Globals.setC(i, num)
            Globals.setS(i, sym)
          }
          case _ => throw new Exception("Cannot set immutable global")
        }
        eval(rest, kont, mkont, trail)(newCtx)
      case Store(StoreOp(align, offset, ty, None)) =>
        val (ty1, newCtx1) = ctx.pop()
        val value = Stack.popC(ty1)
        val symValue = Stack.popS(ty1)
        val (ty2, newCtx2) = newCtx1.pop()
        val addr = Stack.popC(ty2)
        val symAddr = Stack.popS(ty2)
        Memory.storeInt(addr.toInt, offset, (value.toInt, symValue))
        eval(rest, kont, mkont, trail)(newCtx2)
      case Nop => eval(rest, kont, mkont, trail)
      case Load(LoadOp(align, offset, ty, None, None)) =>
        val (ty1, newCtx1) = ctx.pop()
        val addr = Stack.popC(ty1)
        Stack.popS(ty1)
        val num = Memory.loadIntC(addr.toInt, offset)
        val sym = Memory.loadIntS(addr.toInt, offset)
        Stack.pushC(num)
        Stack.pushS(sym)
        val newCtx2 = newCtx1.push(ty)
        eval(rest, kont, mkont, trail)(newCtx2)
      case MemorySize => ???
      case MemoryGrow =>
        val (ty, newCtx) = ctx.pop()
        val delta = Stack.popC(ty)
        Stack.popS(ty)
        val ret = Memory.grow(delta.toInt)
        val retNum = Values.I32V(ret)
        // For now, we assume that the result of memory.grow only depends on the execution path, 
        // we can relax this by turning it return to a symbol value and mimic the memory.grow's result as input. 
        val retSym = "Concrete".reflectCtrlWith[SymVal](retNum)
        Stack.pushC(StagedConcreteNum(NumType(I32Type), retNum))
        Stack.pushS(StagedSymbolicNum(NumType(I32Type), retSym))
        val newCtx2 = newCtx.push(NumType(I32Type))
        eval(rest, kont, mkont, trail)(newCtx2)
      case MemoryFill => ???
      case Unreachable => unreachable()
      case Test(op) =>
        val (ty, newCtx1) = ctx.pop()
        val v = Stack.popC(ty)
        val s = Stack.popS(ty)
        Stack.pushC(evalTestOpC(op, v))
        Stack.pushS(evalTestOpS(op, s))
        val newCtx2 = newCtx1.push(v.tipe)
        eval(rest, kont, mkont, trail)(newCtx2)
      case Unary(op) =>
        val (ty, newCtx1) = ctx.pop()
        val v = Stack.popC(ty)
        val s = Stack.popS(ty)
        val res = evalUnaryOpC(op, v)
        Stack.pushC(res)
        Stack.pushS(evalUnaryOpS(op, s))
        val newCtx2 = newCtx1.push(res.tipe)
        eval(rest, kont, mkont, trail)(newCtx2)
      case Binary(op) =>
        val (ty2, newCtx1) = ctx.pop()
        val v2 = Stack.popC(ty2)
        val s2 = Stack.popS(ty2)
        val (ty1, newCtx2) = newCtx1.pop()
        val v1 = Stack.popC(ty1)
        val s1 = Stack.popS(ty1)
        val res = evalBinOpC(op, v1, v2)
        Stack.pushC(res)
        Stack.pushS(evalBinOpS(op, s1, s2))
        val newCtx3 = newCtx2.push(res.tipe)
        eval(rest, kont, mkont, trail)(newCtx3)
      case Compare(op) =>
        val (ty2, newCtx1) = ctx.pop()
        val v2 = Stack.popC(ty2)
        val s2 = Stack.popS(ty2)
        val (ty1, newCtx2) = newCtx1.pop()
        val v1 = Stack.popC(ty1)
        val s1 = Stack.popS(ty1)
        val res = evalRelOpC(op, v1, v2)
        Stack.pushC(res)
        Stack.pushS(evalRelOpS(op, s1, s2))
        val newCtx3 = newCtx2.push(res.tipe)
        eval(rest, kont, mkont, trail)(newCtx3)
      case WasmBlock(ty, inner) =>
        // no need to modify the stack when entering a block
        // the type system guarantees that we will never take more than the input size from the stack
        val funcTy = ty.funcType
        val exitSize = ctx.stackTypes.size - funcTy.inps.size + funcTy.out.size
        val dummy = makeDummy
        def restK(restCtx: Context): Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => {
          info(s"Exiting the block, stackSize =", Stack.size)
          val offset = restCtx.stackTypes.size - exitSize
          Stack.shiftC(offset, funcTy.out.size)
          Stack.shiftS(offset, funcTy.out.size)
          val newRestCtx = restCtx.shift(offset, funcTy.out.size)
          eval(rest, kont, mk, trail)(newRestCtx)
        })
        eval(inner, restK _, mkont, restK _ :: trail)
      case Loop(ty, inner) =>
        val funcTy = ty.funcType
        val exitSize = ctx.stackTypes.size - funcTy.inps.size + funcTy.out.size
        val dummy = makeDummy
        def restK(restCtx: Context): Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => {
          info(s"Exiting the loop, stackSize =", Stack.size)
          val offset = restCtx.stackTypes.size - exitSize
          Stack.shiftC(offset, funcTy.out.size)
          Stack.shiftS(offset, funcTy.out.size)
          val newRestCtx = restCtx.shift(offset, funcTy.out.size)
          eval(rest, kont, mk, trail)(newRestCtx)
        })
        val enterSize = ctx.stackTypes.size
        def loop(restCtx: Context): Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => {
          info(s"Entered the loop, stackSize =", Stack.size)
          val offset = restCtx.stackTypes.size - enterSize
          Stack.shiftC(offset, funcTy.inps.size)
          Stack.shiftS(offset, funcTy.inps.size)
          val newRestCtx = restCtx.shift(offset, funcTy.inps.size)
          eval(inner, restK _, mk, loop _ :: trail)(newRestCtx)
        })
        loop(ctx)(mkont)
      case If(ty, thn, els) =>
        val funcTy = ty.funcType
        val (condTy, newCtx) = ctx.pop()
        val cond = Stack.popC(condTy)
        val symCond = Stack.popS(condTy)
        val exitSize = newCtx.stackTypes.size - funcTy.inps.size + funcTy.out.size
        def restK(restCtx: Context): Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => {
          info(s"Exiting the if, stackSize =", Stack.size)
          val offset = restCtx.stackTypes.size - exitSize
          Stack.shiftC(offset, funcTy.out.size)
          Stack.shiftS(offset, funcTy.out.size)
          val newRestCtx = restCtx.shift(offset, funcTy.out.size)
          eval(rest, kont, mk, trail)(newRestCtx)
        })
        val id = Counter.getId(inst)
        ExploreTree.fillWithIfElse(symCond.s, id)
        def thnK: Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => {
          info(s"Entering the true branch $id of the if")
          eval(thn, restK _, mk, restK _ :: trail)(newCtx)
        })
        def elsK: Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => {
          info(s"Entering the false branch $id of the if")
          eval(els, restK _, mk, restK _ :: trail)(newCtx)
        })
        if (cond.toInt != 0) {
          val snapshot = makeSnapshot(elsK, mkont)
          ExploreTree.moveCursor(true, snapshot)
          thnK(mkont)
        } else {
          val snapshot = makeSnapshot(thnK, mkont)
          ExploreTree.moveCursor(false, snapshot)
          elsK(mkont)
        }
        ()
      case Br(label) =>
        info(s"Jump to $label")
        trail(label)(ctx)(mkont)
      case BrIf(label) =>
        val (ty, newCtx) = ctx.pop()
        val cond = Stack.popC(ty)
        val symCond = Stack.popS(ty)
        info(s"The br_if(${label})'s condition is ", cond.toInt)
        val id = Counter.getId(inst)
        ExploreTree.fillWithIfElse(symCond.s, id)
        def thnK: Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => {
          trail(label)(newCtx)(mk)
        })
        def elsK: Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => {
          eval(rest, kont, mk, trail)(newCtx)
        })
        if (cond.toInt != 0) {
          info(s"Jump to $label")
          val snapshot = makeSnapshot(elsK, mkont)
          ExploreTree.moveCursor(true, snapshot)
          thnK(mkont)
        } else {
          info(s"Continue")
          val snapshot = makeSnapshot(thnK, mkont)
          ExploreTree.moveCursor(false, snapshot)
          elsK(mkont)
        }
        ()
      case BrTable(labels, default) =>
        val (ty, newCtx) = ctx.pop()
        def aux(choices: List[Int], idx: Int, mkont: Rep[MCont[Unit]]): Rep[Unit] = {
          if (choices.isEmpty) {
            Stack.popC(ty)
            Stack.popS(ty)
            trail(default)(newCtx)(mkont)
          } else {
            val label = Stack.peekC(ty)
            val labelSym = Stack.peekS(ty)
            val cond = (label - toStagedNum(I32V(idx))).isZero()
            val condSym = (labelSym - toStagedSymbolicNum(I32V(idx))).isZero()
            val id = Counter.getId(inst, idx)
            ExploreTree.fillWithIfElse(condSym.s, id)
            // When moving the cursor to a branch, we mark another branch as
            // snapshotNode (this is done by moveCursor's runtime implementation)
            // TODO: store snapshot into this snapshot node
            def thnK: Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => {
              info(s"Entering the true branch $id of the br_table")
              Stack.popC(ty)
              Stack.popS(ty)
              trail(choices.head)(newCtx)(mk)
            })
            def elsK: Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => {
              info(s"Entering the false branch $id of the br_table")
              aux(choices.tail, idx + 1, mk)
            })
            if (cond.toInt != 0) {
              val snapshot = makeSnapshot(elsK, mkont)
              ExploreTree.moveCursor(true, snapshot)
              thnK(mkont)
            }
            else {
              val snapshot = makeSnapshot(thnK, mkont)
              ExploreTree.moveCursor(false, snapshot)
              elsK(mkont)
            }
          }
        }
        aux(labels, 0, mkont)
      case Return        => trail.last(ctx)(mkont)
      case Call(f)       => evalCall(rest, kont, mkont, trail, f, false)
      case ReturnCall(f) => evalCall(rest, kont, mkont, trail, f, true)
      case _ =>
        val todo = "todo-op".reflectCtrlWith[Unit]()
        eval(rest, kont, mkont, trail)
    }
  }

  def forwardKont: Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => mk(()))


  def evalCall(rest: List[Instr],
               kont: Context => Rep[Cont[Unit]],
               mkont: Rep[MCont[Unit]],
               trail: Trail[Unit],
               funcIndex: Int,
               isTail: Boolean)
              (implicit ctx: Context): Rep[Unit] = {
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
              def retK(ctx: Context): Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => {
                info(s"Exiting the function at $funcIndex, stackSize =", Stack.size)
                val offset = ctx.stackTypes.size - ty.out.size
                Stack.shiftC(offset, ty.out.size)
                Stack.shiftS(offset, ty.out.size)
                mk(())
              })
              eval(body, retK _, mk, retK _::Nil)(Context(Nil, locals))
            })
            compileCache(funcIndex) = callee
            callee
          }
        // Predef.println(s"[DEBUG] locals size: ${locals.size}")
        val newCtx = ctx.take(ty.inps.size)
        val argsC = Stack.takeC(ty.inps)
        val argsS = Stack.takeS(ty.inps)
        if (isTail) {
          // when tail call, return to the caller's return continuation
          Frames.popFrameC(ctx.frameTypes.size)
          Frames.popFrameS(ctx.frameTypes.size)
          Frames.pushFrameC(locals)
          Frames.pushFrameS(locals)
          Frames.putAllC(argsC)
          Frames.putAllS(argsS)
          callee(mkont)
        } else {
          // We make a new trail by `restK`, since function creates a new block to escape
          // (more or less like `return`)
          val restK: Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => {
            info(s"Exiting the function at $funcIndex, stackSize =", Stack.size)
            Frames.popFrameC(locals.size)
            Frames.popFrameS(locals.size)
            eval(rest, kont, mk, trail)(newCtx.copy(stackTypes = ty.out.reverse ++ ctx.stackTypes.drop(ty.inps.size)))
          })
          val dummy = makeDummy
          val newMKont: Rep[MCont[Unit]] = funHere((_u: Rep[Unit]) => {
            restK(mkont)
          }, dummy)
          Frames.pushFrameC(locals)
          Frames.pushFrameS(locals)
          Frames.putAllC(argsC)
          Frames.putAllS(argsS)
          callee(newMKont)
        }
      case Import("console", "log", _)
         | Import("spectest", "print_i32", _) =>
        //println(s"[DEBUG] current stack: $stack")
        val (ty, newCtx) = ctx.pop()
        val v = Stack.popC(ty)
        Stack.popS(ty)
        println(v.toInt)
        eval(rest, kont, mkont, trail)(newCtx)
      case Import("console", "assert", _) =>
        val (ty, newCtx) = ctx.pop()
        val v = Stack.popC(ty)
        // TODO: We should also add s into exploration tree
        val s = Stack.popS(ty)
        runtimeAssert(v.toInt != 0)
        eval(rest, kont, mkont, trail)(newCtx)
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
    Counter.reset()
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
    // resetStacks() // Don't manually reset the global states (like stack), manage them in the driver
    initGlobals(module.globals)
    Frames.pushFrameC(locals)
    Frames.pushFrameS(locals)
    eval(instrs, (_: Context) => forwardKont, mkont, ((_: Context) => forwardKont)::Nil)(Context(Nil, locals))
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

    def popC(ty: ValueType): StagedConcreteNum = {
      StagedConcreteNum(ty, "stack-pop".reflectCtrlWith[Num]())
    }

    def popS(ty: ValueType): StagedSymbolicNum = {
      StagedSymbolicNum(ty, "sym-stack-pop".reflectCtrlWith[SymVal]())
    }

    def peekC(ty: ValueType): StagedConcreteNum = {
      StagedConcreteNum(ty, "stack-peek".reflectCtrlWith[Num]())
    }

    def peekS(ty: ValueType): StagedSymbolicNum = {
      StagedSymbolicNum(ty, "sym-stack-peek".reflectCtrlWith[SymVal]())
    }

    def pushC(num: StagedConcreteNum) = "stack-push".reflectCtrlWith[Unit](num.i)

    def pushS(num: StagedSymbolicNum) = "sym-stack-push".reflectCtrlWith[Unit](num.s)

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
      StagedConcreteNum(ctx.frameTypes(i), "frame-get".reflectCtrlWith[Num](i))
    }

    def getS(i: Int)(implicit ctx: Context): StagedSymbolicNum = {
      StagedSymbolicNum(ctx.frameTypes(i), "sym-frame-get".reflectCtrlWith[SymVal](i))
    }

    def setC(i: Int, v: StagedConcreteNum): Rep[Unit] = {
      "frame-set".reflectCtrlWith[Unit](i, v.i)
    }

    def setS(i: Int, s: StagedSymbolicNum): Rep[Unit] = {
      "sym-frame-set".reflectCtrlWith[Unit](i, s.s)
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
    // TODO: why this is only one function, rather than `storeInC` and `storeInS`?
    // TODO: what should the type of SymVal be?
    def storeInt(base: Rep[Int], offset: Int, value: (Rep[Int], StagedSymbolicNum)): Rep[Unit] = {
      "memory-store-int".reflectCtrlWith[Unit](base, offset, value._1)
      "sym-store-int".reflectCtrlWith[Unit](base, offset, value._2.s)
    }

    def loadIntC(base: Rep[Int], offset: Int): StagedConcreteNum = {
      StagedConcreteNum(NumType(I32Type), "I32V".reflectCtrlWith[Num]("memory-load-int".reflectCtrlWith[Int](base, offset)))
    }

    def loadIntS(base: Rep[Int], offset: Int): StagedSymbolicNum = {
      StagedSymbolicNum(NumType(I32Type), "sym-load-int".reflectCtrlWith[SymVal](base, offset))
    }

    // Returns the previous memory size on success, or -1 if the memory cannot be grown.
    def grow(delta: Rep[Int]): Rep[Int] = {
      "memory-grow".reflectCtrlWith[Int](delta)
    }
  }

  def resetStacks(): Rep[Unit] = {
    "reset-stacks".reflectCtrlWith[Unit]()
  }

  def initGlobals(globals: List[RTGlobal]): Rep[Unit] = {
    Globals.reserveSpace(globals.size)
    for ((g, i) <- globals.view.zipWithIndex) {
      val initValue = g.value match {
        case n: Num => n
        case _ => throw new RuntimeException("Non-numeric global value is not supported yet")
      }
      Globals.setC(i, toStagedNum(initValue))
      Globals.setS(i, toStagedSymbolicNum(initValue))
    }
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
    def reserveSpace(size: Int): Rep[Unit] = {
      "global-reserve".reflectCtrlWith[Unit](size)
      "sym-global-reserve".reflectCtrlWith[Unit](size)
    }

    def getC(i: Int): StagedConcreteNum = {
      StagedConcreteNum(module.globals(i).ty.ty, "global-get".reflectCtrlWith[Num](i))
    }

    def getS(i: Int): StagedSymbolicNum = {
      StagedSymbolicNum(module.globals(i).ty.ty, "sym-global-get".reflectCtrlWith[SymVal](i))
    }

    def setC(i: Int, v: StagedConcreteNum): Rep[Unit] = {
      "global-set".reflectCtrlWith[Unit](i, v.i)
    }

    def setS(i: Int, s: StagedSymbolicNum): Rep[Unit] = {
      "sym-global-set".reflectCtrlWith[Unit](i, s.s)
    }
  }

  // Exploration tree,
  object ExploreTree {
    def fillWithIfElse(s: Rep[SymVal], id: Int): Rep[Unit] = {
      "tree-fill-if-else".reflectCtrlWith[Unit](s, id)
    }

    def fillWithFinished(): Rep[Unit] = {
      "tree-fill-finished".reflectCtrlWith[Unit]()
    }

    def moveCursor(branch: Boolean, snapshot: Rep[Snapshot]): Rep[Unit] = {
      // when moving cursor from to an unexplored node, we need to change the reuse state
      "tree-move-cursor".reflectCtrlWith[Unit](branch, snapshot)
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

  // runtime Num type
  implicit class StagedConcreteNumOps(num: StagedConcreteNum) {

    def makeSymbolic(ty: ValueType): StagedSymbolicNum = num.tipe match {
      case NumType(I32Type) =>
        StagedSymbolicNum(NumType(I32Type), "make-symbolic-concrete".reflectCtrlWith[SymVal](num.toInt))
    }

    def toInt: Rep[Int] = "num-to-int".reflectCtrlWith[Int](num.i)

    def isZero(): StagedConcreteNum = num.tipe match {
      case NumType(I32Type) =>
        StagedConcreteNum(NumType(I32Type), Values.I32V("is-zero".reflectCtrlWith[Int](num.toInt)))
    }

    def clz(): StagedConcreteNum = num.tipe match {
      case NumType(I32Type) => StagedConcreteNum(NumType(I32Type), "clz".reflectCtrlWith[Num](num.i))
      case NumType(I64Type) => StagedConcreteNum(NumType(I64Type), "clz".reflectCtrlWith[Num](num.i))
    }

    def ctz(): StagedConcreteNum = num.tipe match {
      case NumType(I32Type) => StagedConcreteNum(NumType(I32Type), "ctz".reflectCtrlWith[Num](num.i))
      case NumType(I64Type) => StagedConcreteNum(NumType(I64Type), "ctz".reflectCtrlWith[Num](num.i))
    }

    def popcnt(): StagedConcreteNum = num.tipe match {
      case NumType(I32Type) => StagedConcreteNum(NumType(I32Type), "popcnt".reflectCtrlWith[Num](num.i))
      case NumType(I64Type) => StagedConcreteNum(NumType(I64Type), "popcnt".reflectCtrlWith[Num](num.i))
    }

    def +(rhs: StagedConcreteNum): StagedConcreteNum = (num.tipe, rhs.tipe) match {
      case (NumType(I32Type), NumType(I32Type)) =>
        StagedConcreteNum(NumType(I32Type), "binary-add".reflectCtrlWith[Num](num.i, rhs.i))
      case (NumType(I64Type), NumType(I64Type)) =>
        StagedConcreteNum(NumType(I64Type), "binary-add".reflectCtrlWith[Num](num.i, rhs.i))
      case (NumType(F32Type), NumType(F32Type)) =>
        StagedConcreteNum(NumType(F32Type), "binary-add".reflectCtrlWith[Num](num.i, rhs.i))
      case (NumType(F64Type), NumType(F64Type)) =>
        StagedConcreteNum(NumType(F64Type), "binary-add".reflectCtrlWith[Num](num.i, rhs.i))
    }

    def -(rhs: StagedConcreteNum): StagedConcreteNum = (num.tipe, rhs.tipe) match {
      case (NumType(I32Type), NumType(I32Type)) =>
        StagedConcreteNum(NumType(I32Type), "binary-sub".reflectCtrlWith[Num](num.i, rhs.i))
      case (NumType(I64Type), NumType(I64Type)) =>
        StagedConcreteNum(NumType(I64Type), "binary-sub".reflectCtrlWith[Num](num.i, rhs.i))
      case (NumType(F32Type), NumType(F32Type)) =>
        StagedConcreteNum(NumType(F32Type), "binary-sub".reflectCtrlWith[Num](num.i, rhs.i))
      case (NumType(F64Type), NumType(F64Type)) =>
        StagedConcreteNum(NumType(F64Type), "binary-sub".reflectCtrlWith[Num](num.i, rhs.i))
    }

    def *(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "binary-mul".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I64Type), "binary-mul".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedConcreteNum(NumType(F32Type), "binary-mul".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedConcreteNum(NumType(F64Type), "binary-mul".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def /(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "binary-div".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I64Type), "binary-div".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedConcreteNum(NumType(F32Type), "binary-div".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedConcreteNum(NumType(F64Type), "binary-div".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def <<(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "binary-shl".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I64Type), "binary-shl".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedConcreteNum(NumType(F32Type), "binary-shl".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedConcreteNum(NumType(F64Type), "binary-shl".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def >>(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "binary-shr".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I64Type), "binary-shr".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedConcreteNum(NumType(F32Type), "binary-shr".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedConcreteNum(NumType(F64Type), "binary-shr".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def &(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "binary-and".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I64Type), "binary-and".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedConcreteNum(NumType(F32Type), "binary-and".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedConcreteNum(NumType(F64Type), "binary-and".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def numEq(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "relation-eq".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I32Type), "relation-eq".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def numNe(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "relation-ne".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I32Type), "relation-ne".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def <(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "relation-lt".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I32Type), "relation-lt".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def ltu(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "relation-ltu".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I32Type), "relation-ltu".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def >(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "relation-gt".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I32Type), "relation-gt".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def gtu(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "relation-gtu".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I32Type), "relation-gtu".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def <=(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "relation-le".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I32Type), "relation-le".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def leu(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "relation-leu".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I32Type), "relation-leu".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def >=(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "relation-ge".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I32Type), "relation-ge".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def geu(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "relation-geu".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I32Type), "relation-geu".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }
  }

  implicit class StagedSymbolicNumOps(num: StagedSymbolicNum) {
    def makeSymbolic(ty: ValueType): StagedSymbolicNum = num.tipe match {
      case NumType(I32Type) => StagedSymbolicNum(NumType(I32Type), "make-symbolic".reflectCtrlWith[SymVal](num.s))
      case _ => throw new RuntimeException("Symbol index must be an i32")
    }

    def isZero(): StagedSymbolicNum = num.tipe match {
      case NumType(I32Type) => StagedSymbolicNum(NumType(I32Type), "sym-is-zero".reflectCtrlWith[SymVal](num.s))
    }

    def clz(): StagedSymbolicNum = num.tipe match {
      case NumType(I32Type) => StagedSymbolicNum(NumType(I32Type), "sym-clz".reflectCtrlWith[SymVal](num.s))
      case NumType(I64Type) => StagedSymbolicNum(NumType(I64Type), "sym-clz".reflectCtrlWith[SymVal](num.s))
    }

    def ctz(): StagedSymbolicNum = num.tipe match {
      case NumType(I32Type) => StagedSymbolicNum(NumType(I32Type), "sym-ctz".reflectCtrlWith[SymVal](num.s))
      case NumType(I64Type) => StagedSymbolicNum(NumType(I64Type), "sym-ctz".reflectCtrlWith[SymVal](num.s))
    }

    def popcnt(): StagedSymbolicNum = num.tipe match {
      case NumType(I32Type) => StagedSymbolicNum(NumType(I32Type), "sym-popcnt".reflectCtrlWith[SymVal](num.s))
      case NumType(I64Type) => StagedSymbolicNum(NumType(I64Type), "sym-popcnt".reflectCtrlWith[SymVal](num.s))
    }

    def +(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-add".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-add".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(F32Type), "sym-binary-add".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(F64Type), "sym-binary-add".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def -(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-sub".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-sub".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(F32Type), "sym-binary-sub".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(F64Type), "sym-binary-sub".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def *(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-mul".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-mul".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(F32Type), "sym-binary-mul".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(F64Type), "sym-binary-mul".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def /(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-div".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-div".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(F32Type), "sym-binary-div".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(F64Type), "sym-binary-div".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def <<(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-shl".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-shl".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(F32Type), "sym-binary-shl".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(F64Type), "sym-binary-shl".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def >>(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-shr".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-shr".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(F32Type), "sym-binary-shr".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(F64Type), "sym-binary-shr".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def &(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-and".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-and".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(F32Type), "sym-binary-and".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(F64Type), "sym-binary-and".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def numEq(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-eq".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-eq".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def numNe(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-ne".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-ne".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def <(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-lt".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-lt".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def ltu(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "relation-ltu".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "relation-ltu".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def >(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-gt".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-gt".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def gtu(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-gtu".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-gtu".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def <=(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-le".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-le".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def leu(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-leu".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-leu".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def >=(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-ge".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-ge".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def geu(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-geu".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-geu".reflectCtrlWith[SymVal](num.s, rhs.s))
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
    else if (m.toString.endsWith("Snapshot")) "Snapshot_t"
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
    // Note: The following code is copied from the traverse of CppBackend.scala, try to avoid duplicated code
    case n @ Node(f, "λ", (b: LMSBlock)::LMSConst(0)::rest, _) =>
      // TODO: Is a leading block followed by 0 a hint for top function?
      super.traverse(n)
    case n @ Node(f, "λ", (b: LMSBlock)::rest, _) =>
      val retType = remap(typeBlockRes(b.res))
      val argTypes = b.in.map(a => remap(typeMap(a))).mkString(", ")
      emitln(s"std::function<$retType(${argTypes})> ${quote(f)};")
      emit(quote(f)); emit(" = ")
      // We need to capture by value here, because we want to save a function in
      // snapshot, and use the function later, while the local variables have
      // been released.
      quoteTypedBlock(b, false, true, capture = "=")
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
    case Node(_, "snapshot-make", List(k, mk), _) =>
      emit("makeSnapshot("); shallow(k); emit(", "); shallow(mk); emit(")")
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
    // Symbolic Memory
    case Node(_, "sym-store-int", List(base, offset, s_value), _) =>
      emit("SymMemory.storeSym("); shallow(base); emit(", "); shallow(offset); emit(", "); shallow(s_value); emit(")")
    case Node(_, "sym-load-int", List(base, offset), _) =>
      emit("SymMemory.loadSym("); shallow(base); emit(", "); shallow(offset); emit(")")
    case Node(_, "sym-memory-grow", List(delta), _) =>
      emit("SymMemory.grow("); shallow(delta); emit(")")
    // Globals
    case Node(_, "global-get", List(i), _) =>
      emit("Globals.get("); shallow(i); emit(")")
    case Node(_, "sym-global-get", List(i), _) =>
      emit("SymGlobals.get("); shallow(i); emit(")")
    case Node(_, "global-set", List(i, value), _) =>
      emit("Globals.set("); shallow(i); emit(", "); shallow(value); emit(")")
    case Node(_, "sym-global-set", List(i, s_value), _) =>
      emit("SymGlobals.set("); shallow(i); emit(", "); shallow(s_value); emit(")")
    case Node(_, "global-reserve", List(i), _) =>
      emit("Globals.pushFrame("); shallow(i); emit(")")
    case Node(_, "sym-global-reserve", List(i), _) =>
      emit("SymGlobals.pushFrame("); shallow(i); emit(")")
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
    case Node(_, "sym-binary-and", List(lhs, rhs), _) =>
      shallow(lhs); emit(".bitwise_and("); shallow(rhs); emit(")")
    case Node(_, "sym-relation-le", List(lhs, rhs), _) =>
      shallow(lhs); emit(".le("); shallow(rhs); emit(")")
    case Node(_, "sym-relation-leu", List(lhs, rhs), _) =>
      shallow(lhs); emit(".leu("); shallow(rhs); emit(")")
    case Node(_, "sym-relation-lt", List(lhs, rhs), _) =>
      shallow(lhs); emit(".lt("); shallow(rhs); emit(")")
    case Node(_, "sym-relation-ge", List(lhs, rhs), _) =>
      shallow(lhs); emit(".ge("); shallow(rhs); emit(")")
    case Node(_, "sym-relation-geu", List(lhs, rhs), _) =>
      shallow(lhs); emit(".geu("); shallow(rhs); emit(")")
    case Node(_, "sym-relation-eq", List(lhs, rhs), _) =>
      shallow(lhs); emit(".eq("); shallow(rhs); emit(")")
    case Node(_, "sym-relation-ne", List(lhs, rhs), _) =>
      shallow(lhs); emit(".neq("); shallow(rhs); emit(")")
    case Node(_, "sym-relation-gt", List(lhs, rhs), _) =>
      shallow(lhs); emit(".gt("); shallow(rhs); emit(")")
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
    case Node(_, "tree-fill-if-else", List(sym, id), _) =>
      emit("ExploreTree.fillIfElseNode("); shallow(sym); emit(", "); emit(id.toString); emit(")")
    case Node(_, "tree-fill-finished", List(), _) =>
      emit("ExploreTree.fillFinishedNode()")
    case Node(_, "tree-move-cursor", List(b, snapshot), _) =>
      emit("ExploreTree.moveCursor("); shallow(b); emit(", "); shallow(snapshot); emit(")")
    case Node(_, "tree-print", List(), _) =>
      emit("ExploreTree.print()")
    case Node(_, "tree-dump-graphviz", List(f), _) =>
      emit("ExploreTree.dump_graphviz("); shallow(f); emit(")")
    case Node(_, "sym-not", List(s), _) =>
      shallow(s); emit(".negate()")
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
    emitln(s"""
    |/*****************************************
    |End of Generated Code
    |*******************************************/
    |int main(int argc, char *argv[]) {
    |  start_concolic_execution_with(Snippet, ${Counter.currentId});
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
    val command = s"g++ -std=c++17 $outputCpp -o $outputExe -O3 -g -l z3 " + includeFlags + " " + macroFlags
    if (command.! != 0) {
      throw new RuntimeException(s"Compilation failed for $outputCpp")
    }
  }

}


