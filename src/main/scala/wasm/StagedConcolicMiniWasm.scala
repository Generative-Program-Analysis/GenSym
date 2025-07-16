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

@virtualize
trait StagedWasmEvaluator extends SAIOps {
  def module: ModuleInstance

  trait ReturnSite

  trait StagedNum {
    def tipe: ValueType = this match {
      case I32(_, _) => NumType(I32Type)
      case I64(_, _) => NumType(I64Type)
      case F32(_, _) => NumType(F32Type)
      case F64(_, _) => NumType(F64Type)
    }

    def i: Rep[Num]

    def s: Rep[SymVal]
  }
  case class I32(i: Rep[Num], s: Rep[SymVal]) extends StagedNum
  case class I64(i: Rep[Num], s: Rep[SymVal]) extends StagedNum
  case class F32(i: Rep[Num], s: Rep[SymVal]) extends StagedNum
  case class F64(i: Rep[Num], s: Rep[SymVal]) extends StagedNum

  def toStagedNum(num: Num): StagedNum = {
    num match {
      case I32V(_) => I32(num, Concrete(num))
      case I64V(_) => I64(num, Concrete(num))
      case F32V(_) => F32(num, Concrete(num))
      case F64V(_) => F64(num, Concrete(num))
    }
  }

  implicit class ValueTypeOps(ty: ValueType) {
    def size: Int = ty match {
      case NumType(I32Type) => 4
      case NumType(I64Type) => 8
      case NumType(F32Type) => 4
      case NumType(F64Type) => 8
    }

    def toTagger: (Rep[Num], Rep[SymVal]) => StagedNum = {
      ty match {
        case NumType(I32Type) => I32
        case NumType(I64Type) => I64
        case NumType(F32Type) => F32
        case NumType(F64Type) => F64
      }
    }
  }

  case class Context(
    stackTypes: List[ValueType],
    frameTypes: List[ValueType]
  ) {
    def push(ty: ValueType): Context = {
      Context(ty :: stackTypes, frameTypes)
    }

    def pop(): (ValueType, Context) = {
      val (ty :: rest) = stackTypes
      (ty, Context(rest, frameTypes))
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
        val (_, newCtx) = Stack.pop()
        eval(rest, kont, mkont, trail)(newCtx)
      case WasmConst(num) =>
        val newCtx = Stack.push(toStagedNum(num))
        eval(rest, kont, mkont, trail)(newCtx)
      case Symbolic(ty) =>
        val (id, newCtx1) = Stack.pop()
        val symVal = id.makeSymbolic()
        val concVal = SymEnv.read(symVal)
        val tagger = ty.toTagger
        val value = tagger(concVal, symVal)
        val newCtx2 = Stack.push(value)(newCtx1)
        eval(rest, kont, mkont, trail)(newCtx2)
      case LocalGet(i) =>
        val newCtx = Stack.push(Frames.get(i))
        eval(rest, kont, mkont, trail)(newCtx)
      case LocalSet(i) =>
        val (num, newCtx) = Stack.pop()
        Frames.set(i, num)(newCtx)
        eval(rest, kont, mkont, trail)(newCtx)
      case LocalTee(i) =>
        val (num, newCtx) = Stack.peek
        Frames.set(i, num)
        eval(rest, kont, mkont, trail)(newCtx)
      case GlobalGet(i) =>
        val newCtx = Stack.push(Globals(i))
        eval(rest, kont, mkont, trail)(newCtx)
      case GlobalSet(i) =>
        val (value, newCtx) = Stack.pop()
        module.globals(i).ty match {
          case GlobalType(tipe, true) => Globals(i) = value
          case _ => throw new Exception("Cannot set immutable global")
        }
        eval(rest, kont, mkont, trail)(newCtx)
      case Store(StoreOp(align, offset, ty, None)) =>
        val (value, newCtx1) = Stack.pop()
        val (addr, newCtx2) = Stack.pop()(newCtx1)
        Memory.storeInt(addr.toInt, offset, value.toInt)
        eval(rest, kont, mkont, trail)(newCtx2)
      case Nop => eval(rest, kont, mkont, trail)
      case Load(LoadOp(align, offset, ty, None, None)) =>
        val (addr, newCtx1) = Stack.pop()
        val value = Memory.loadInt(addr.toInt, offset)
        val newCtx2 = Stack.push(value)(newCtx1)
        eval(rest, kont, mkont, trail)(newCtx2)
      case MemorySize => ???
      case MemoryGrow =>
        val (delta, newCtx1) = Stack.pop()
        val ret = Memory.grow(delta.toInt)
        val retNum = Values.I32V(ret)
        val retSym = "Concrete".reflectCtrlWith[SymVal](retNum)
        val newCtx2 = Stack.push(I32(retNum, retSym))(newCtx1)
        eval(rest, kont, mkont, trail)(newCtx2)
      case MemoryFill => ???
      case Unreachable => unreachable()
      case Test(op) =>
        val (v, newCtx1) = Stack.pop()
        val newCtx2 = Stack.push(evalTestOp(op, v))(newCtx1)
        eval(rest, kont, mkont, trail)(newCtx2)
      case Unary(op) =>
        val (v, newCtx1) = Stack.pop()
        val newCtx2 = Stack.push(evalUnaryOp(op, v))(newCtx1)
        eval(rest, kont, mkont, trail)(newCtx2)
      case Binary(op) =>
        val (v2, newCtx1) = Stack.pop()
        val (v1, newCtx2) = Stack.pop()(newCtx1)
        val newCtx3 = Stack.push(evalBinOp(op, v1, v2))(newCtx2)
        eval(rest, kont, mkont, trail)(newCtx3)
      case Compare(op) =>
        val (v2, newCtx1) = Stack.pop()
        val (v1, newCtx2) = Stack.pop()(newCtx1)
        val newCtx3 = Stack.push(evalRelOp(op, v1, v2))(newCtx2)
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
          val newRestCtx = Stack.shift(offset, funcTy.out.size)(restCtx)
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
          val newRestCtx = Stack.shift(offset, funcTy.out.size)(restCtx)
          eval(rest, kont, mk, trail)(newRestCtx)
        })
        val enterSize = ctx.stackTypes.size
        def loop(restCtx: Context): Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => {
          info(s"Entered the loop, stackSize =", Stack.size)
          val offset = restCtx.stackTypes.size - enterSize
          val newRestCtx = Stack.shift(offset, funcTy.inps.size)(restCtx)
          eval(inner, restK _, mk, loop _ :: trail)(newRestCtx)
        })
        loop(ctx)(mkont)
      case If(ty, thn, els) =>
        val funcTy = ty.funcType
        val (cond, newCtx) = Stack.pop()
        val exitSize = newCtx.stackTypes.size - funcTy.inps.size + funcTy.out.size
        // TODO: can we avoid code duplication here?
        val dummy = makeDummy
        def restK(restCtx: Context): Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => {
          info(s"Exiting the if, stackSize =", Stack.size)
          val offset = restCtx.stackTypes.size - exitSize
          val newRestCtx = Stack.shift(offset, funcTy.out.size)(restCtx)
          eval(rest, kont, mk, trail)(newRestCtx)
        })
        // TODO: put the cond.s to path condition
        ExploreTree.fillWithIfElse(cond.s)
        if (cond.toInt != 0) {
          ExploreTree.moveCursor(true)
          eval(thn, restK _, mkont, restK _ :: trail)(newCtx)
        } else {
          ExploreTree.moveCursor(false)
          eval(els, restK _, mkont, restK _ :: trail)(newCtx)
        }
        ()
      case Br(label) =>
        info(s"Jump to $label")
        trail(label)(ctx)(mkont)
      case BrIf(label) =>
        val (cond, newCtx) = Stack.pop()
        info(s"The br_if(${label})'s condition is ", cond.toInt)
        // TODO: put the cond.s to path condition
        ExploreTree.fillWithIfElse(cond.s)
        if (cond.toInt != 0) {
          info(s"Jump to $label")
          ExploreTree.moveCursor(true)
          trail(label)(newCtx)(mkont)
        } else {
          info(s"Continue")
          ExploreTree.moveCursor(false)
          eval(rest, kont, mkont, trail)(newCtx)
        }
        ()
      case BrTable(labels, default) =>
        val (cond, newCtx) = Stack.pop()
        def aux(choices: List[Int], idx: Int): Rep[Unit] = {
          if (choices.isEmpty) trail(default)(newCtx)(mkont)
          else {
            if (cond.toInt == idx) trail(choices.head)(newCtx)(mkont)
            else aux(choices.tail, idx + 1)
          }
        }
        aux(labels, 0)
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
              // we can do some check here to ensure the function returns correct size of stack
              eval(body, (_: Context) => forwardKont, mk, ((_: Context) => forwardKont)::Nil)(Context(Nil, locals))
            })
            compileCache(funcIndex) = callee
            callee
          }
        // Predef.println(s"[DEBUG] locals size: ${locals.size}")
        val (args, newCtx) = Stack.take(ty.inps.size)
        if (isTail) {
          // when tail call, return to the caller's return continuation
          Frames.popFrame(ctx.frameTypes.size)
          Frames.pushFrame(locals)
          Frames.putAll(args)
          callee(mkont)
        } else {
          // We make a new trail by `restK`, since function creates a new block to escape
          // (more or less like `return`)
          val restK: Rep[Cont[Unit]] = topFun((mk: Rep[MCont[Unit]]) => {
            info(s"Exiting the function at $funcIndex, stackSize =", Stack.size)
            Frames.popFrame(locals.size)
            eval(rest, kont, mk, trail)(newCtx.copy(stackTypes = ty.out.reverse ++ ctx.stackTypes.drop(ty.inps.size)))
          })
          val dummy = makeDummy
          val newMKont: Rep[MCont[Unit]] = funHere((_u: Rep[Unit]) => {
            restK(mkont)
          }, dummy)
          Frames.pushFrame(locals)
          Frames.putAll(args)
          callee(newMKont)
        }
      case Import("console", "log", _)
         | Import("spectest", "print_i32", _) =>
        //println(s"[DEBUG] current stack: $stack")
        val (v, newCtx) = Stack.pop()
        println(v.toInt)
        eval(rest, kont, mkont, trail)(newCtx)
      case Import("console", "assert", _) =>
        val (v, newCtx) = Stack.pop()
        runtimeAssert(v.toInt != 0)
        eval(rest, kont, mkont, trail)(newCtx)
      case Import(_, _, _) => throw new Exception(s"Unknown import at $funcIndex")
      case _               => throw new Exception(s"Definition at $funcIndex is not callable")
    }
  }

  def evalTestOp(op: TestOp, value: StagedNum): StagedNum = op match {
    case Eqz(_) => value.isZero
  }

  def evalUnaryOp(op: UnaryOp, value: StagedNum): StagedNum = op match {
    case Clz(_) => value.clz()
    case Ctz(_) => value.ctz()
    case Popcnt(_) => value.popcnt()
    case _ => ???
  }

  def evalBinOp(op: BinOp, v1: StagedNum, v2: StagedNum): StagedNum = op match {
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

  def evalRelOp(op: RelOp, v1: StagedNum, v2: StagedNum): StagedNum = op match {
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
    Stack.initialize()
    Frames.pushFrame(locals)
    eval(instrs, (_: Context) => forwardKont, mkont, ((_: Context) => forwardKont)::Nil)(Context(Nil, locals))
    Frames.popFrame(locals.size)
  }

  def evalTop(main: Option[String], printRes: Boolean, dumpTree: Option[String]): Rep[Unit] = {
    val haltK: Rep[Unit] => Rep[Unit] = (_) => {
      info("Exiting the program...")
      if (printRes) {
        Stack.print()
      }
      dumpTree match {
        case Some(filePath) => ExploreTree.dumpGraphiviz(filePath)
        case None => ()
      }
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
    def shift(offset: Int, size: Int)(ctx: Context): Context = {
      if (offset > 0) {
        "stack-shift".reflectCtrlWith[Unit](offset, size)
      }
      ctx.shift(offset, size)
    }

    def initialize(): Rep[Unit] = {
      "stack-init".reflectCtrlWith[Unit]()
    }

    def pop()(implicit ctx: Context): (StagedNum, Context) = {
      val (ty, newContext) = ctx.pop()
      val num = ty match {
        case NumType(I32Type) => I32("stack-pop".reflectCtrlWith[Num](), "sym-stack-pop".reflectCtrlWith[SymVal]())
        case NumType(I64Type) => I64("stack-pop".reflectCtrlWith[Num](), "sym-stack-pop".reflectCtrlWith[SymVal]())
        case NumType(F32Type) => F32("stack-pop".reflectCtrlWith[Num](), "sym-stack-pop".reflectCtrlWith[SymVal]())
        case NumType(F32Type) => F64("stack-pop".reflectCtrlWith[Num](), "sym-stack-pop".reflectCtrlWith[SymVal]())
      }
      (num, newContext)
    }

    def peek(implicit ctx: Context): (StagedNum, Context) = {
      val ty = ctx.stackTypes.head
      val num = ty match {
        case NumType(I32Type) => I32("stack-peek".reflectCtrlWith[Num](), "sym-stack-peek".reflectCtrlWith[SymVal]())
        case NumType(I64Type) => I64("stack-peek".reflectCtrlWith[Num](), "sym-stack-peek".reflectCtrlWith[SymVal]())
        case NumType(F32Type) => F32("stack-peek".reflectCtrlWith[Num](), "sym-stack-peek".reflectCtrlWith[SymVal]())
        case NumType(F32Type) => F64("stack-peek".reflectCtrlWith[Num](), "sym-stack-peek".reflectCtrlWith[SymVal]())
      }
      (num, ctx)
    }

    def push(num: StagedNum)(implicit ctx: Context): Context = {
      num match {
        case I32(v, s) => "stack-push".reflectCtrlWith[Unit](v); "sym-stack-push".reflectCtrlWith[Unit](s)
        case I64(v, s) => "stack-push".reflectCtrlWith[Unit](v); "sym-stack-push".reflectCtrlWith[Unit](s)
        case F32(v, s) => "stack-push".reflectCtrlWith[Unit](v); "sym-stack-push".reflectCtrlWith[Unit](s)
        case F64(v, s) => "stack-push".reflectCtrlWith[Unit](v); "sym-stack-push".reflectCtrlWith[Unit](s)
      }
      ctx.push(num.tipe)
    }

    def take(n: Int)(implicit ctx: Context): (List[StagedNum], Context) = n match {
      case 0 => (Nil, ctx)
      case n =>
        val (v, newCtx1) = pop()
        val (rest, newCtx2) = take(n - 1)
        (v::rest, newCtx2)
    }

    def drop(n: Int)(implicit ctx: Context): Context = {
      take(n)._2
    }

    def shift(offset: Rep[Int], size: Rep[Int]): Rep[Unit] = {
      if (offset > 0) {
        "stack-shift".reflectCtrlWith[Unit](offset, size)
        "sym-stack-shift".reflectCtrlWith[Unit](offset, size)
      }
    }

    def print(): Rep[Unit] = {
      "stack-print".reflectCtrlWith[Unit]()
    }

    def size: Rep[Int] = {
      "stack-size".reflectCtrlWith[Int]()
    }
  }

  object Frames {
    def get(i: Int)(implicit ctx: Context): StagedNum = {
      // val offset = ctx.frameTypes.take(i).map(_.size).sum
      ctx.frameTypes(i) match {
        case NumType(I32Type) => I32("frame-get".reflectCtrlWith[Num](i), "sym-frame-get".reflectCtrlWith[SymVal](i))
        case NumType(I64Type) => I64("frame-get".reflectCtrlWith[Num](i), "sym-frame-get".reflectCtrlWith[SymVal](i))
        case NumType(F32Type) => F32("frame-get".reflectCtrlWith[Num](i), "sym-frame-get".reflectCtrlWith[SymVal](i))
        case NumType(F64Type) => F64("frame-get".reflectCtrlWith[Num](i), "sym-frame-get".reflectCtrlWith[SymVal](i))
      }
    }

    def set(i: Int, v: StagedNum)(implicit ctx: Context): Rep[Unit] = {
      // val offset = ctx.frameTypes.take(i).map(_.size).sum
      v match {
        case I32(v, s) => "frame-set".reflectCtrlWith[Unit](i, v); "sym-frame-set".reflectCtrlWith[Unit](i, s)
        case I64(v, s) => "frame-set".reflectCtrlWith[Unit](i, v); "sym-frame-set".reflectCtrlWith[Unit](i, s)
        case F32(v, s) => "frame-set".reflectCtrlWith[Unit](i, v); "sym-frame-set".reflectCtrlWith[Unit](i, s)
        case F64(v, s) => "frame-set".reflectCtrlWith[Unit](i, v); "sym-frame-set".reflectCtrlWith[Unit](i, s)
      }
    }

    def pushFrame(locals: List[ValueType]): Rep[Unit] = {
      // Predef.println(s"[DEBUG] push frame: $locals")
      val size = locals.size
      "frame-push".reflectCtrlWith[Unit](size)
      "sym-frame-push".reflectCtrlWith[Unit](size)
    }

    def popFrame(size: Int): Rep[Unit] = {
      "frame-pop".reflectCtrlWith[Unit](size)
      "sym-frame-pop".reflectCtrlWith[Unit](size)
    }

    def putAll(args: List[StagedNum])(implicit ctx: Context): Rep[Unit] = {
      for ((arg, i) <- args.view.reverse.zipWithIndex) {
        Frames.set(i, arg)
      }
    }
  }

  object Memory {
    def storeInt(base: Rep[Int], offset: Int, value: Rep[Int]): Rep[Unit] = {
      "memory-store-int".reflectCtrlWith[Unit](base, offset, value)
    }

    def loadInt(base: Rep[Int], offset: Int): StagedNum = {
      I32("I32V".reflectCtrlWith[Num]("memory-load-int".reflectCtrlWith[Int](base, offset)), "sym-load-int-todo".reflectCtrlWith[SymVal](base, offset))
    }

    // Returns the previous memory size on success, or -1 if the memory cannot be grown.
    def grow(delta: Rep[Int]): Rep[Int] = {
      "memory-grow".reflectCtrlWith[Int](delta)
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
    def apply(i: Int): StagedNum = {
      module.globals(i).ty match {
        case GlobalType(NumType(I32Type), _) => I32("global-get".reflectCtrlWith[Num](i), "sym-global-get".reflectCtrlWith[SymVal](i))
        case GlobalType(NumType(I64Type), _) => I64("global-get".reflectCtrlWith[Num](i), "sym-global-get".reflectCtrlWith[SymVal](i))
        case GlobalType(NumType(F32Type), _) => F32("global-get".reflectCtrlWith[Num](i), "sym-global-get".reflectCtrlWith[SymVal](i))
        case GlobalType(NumType(F64Type), _) => F64("global-get".reflectCtrlWith[Num](i), "sym-global-get".reflectCtrlWith[SymVal](i))
      }
    }

    def update(i: Int, v: StagedNum): Rep[Unit] = {
      module.globals(i).ty match {
        case GlobalType(NumType(I32Type), _) => "global-set".reflectCtrlWith[Unit](i, v.i);"sym-global-set".reflectCtrlWith[Unit](i, v.s)
        case GlobalType(NumType(I64Type), _) => "global-set".reflectCtrlWith[Unit](i, v.i);"sym-global-set".reflectCtrlWith[Unit](i, v.s)
        case GlobalType(NumType(F32Type), _) => "global-set".reflectCtrlWith[Unit](i, v.i);"sym-global-set".reflectCtrlWith[Unit](i, v.s)
        case GlobalType(NumType(F64Type), _) => "global-set".reflectCtrlWith[Unit](i, v.i);"sym-global-set".reflectCtrlWith[Unit](i, v.s)
      }
    }
  }

  // Exploration tree, 
  object ExploreTree {
    def fillWithIfElse(s: Rep[SymVal]): Rep[Unit] = {
      "tree-fill-if-else".reflectCtrlWith[Unit](s)
    }

    def moveCursor(branch: Boolean): Rep[Unit] = {
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

  // runtime Num type
  implicit class StagedNumOps(num: StagedNum) {

    def toInt: Rep[Int] = "num-to-int".reflectCtrlWith[Int](num.i)

    def isZero(): StagedNum = num match {
      case I32(x_c, x_s) => I32(Values.I32V("is-zero".reflectCtrlWith[Int](num.toInt)), "sym-is-zero".reflectCtrlWith[SymVal](x_s))
    }

    def clz(): StagedNum = num match {
      case I32(x_c, x_s) => I32("clz".reflectCtrlWith[Num](x_c), "sym-clz".reflectCtrlWith[SymVal](x_s))
      case I64(x_c, x_s) => I64("clz".reflectCtrlWith[Num](x_c), "sym-clz".reflectCtrlWith[SymVal](x_s))
    }

    def ctz(): StagedNum = num match {
      case I32(x_c, x_s) => I32("ctz".reflectCtrlWith[Num](x_c), "sym-ctz".reflectCtrlWith[SymVal](x_s))
      case I64(x_c, x_s) => I64("ctz".reflectCtrlWith[Num](x_c), "sym-ctz".reflectCtrlWith[SymVal](x_s))
    }

    def popcnt(): StagedNum = num match {
      case I32(x_c, x_s) => I32("popcnt".reflectCtrlWith[Num](x_c), "sym-popcnt".reflectCtrlWith[SymVal](x_s))
      case I64(x_c, x_s) => I64("popcnt".reflectCtrlWith[Num](x_c), "sym-popcnt".reflectCtrlWith[SymVal](x_s))
    }

    def makeSymbolic(): Rep[SymVal] = {
      "make-symbolic".reflectCtrlWith[SymVal](num.s)
    }

    def +(rhs: StagedNum): StagedNum = {
      (num, rhs) match {
        case (I32(x_c, x_s), I32(y_c, y_s)) => I32("binary-add".reflectCtrlWith[Num](x_c, y_c), "sym-binary-add".reflectCtrlWith[SymVal](x_s, y_s))
        case (I64(x_c, x_s), I64(y_c, y_s)) => I64("binary-add".reflectCtrlWith[Num](x_c, y_c), "sym-binary-add".reflectCtrlWith[SymVal](x_s, y_s))
        case (F32(x_c, x_s), F32(y_c, y_s)) => F32("binary-add".reflectCtrlWith[Num](x_c, y_c), "sym-binary-add".reflectCtrlWith[SymVal](x_s, y_s))
        case (F64(x_c, x_s), F64(y_c, y_s)) => F64("binary-add".reflectCtrlWith[Num](x_c, y_c), "sym-binary-add".reflectCtrlWith[SymVal](x_s, y_s))
      }
    }


    def -(rhs: StagedNum): StagedNum = {
      (num, rhs) match {
        case (I32(x_c, x_s), I32(y_c, y_s)) => I32("binary-sub".reflectCtrlWith[Num](x_c, y_c), "sym-binary-sub".reflectCtrlWith[SymVal](x_s, y_s))
        case (I64(x_c, x_s), I64(y_c, y_s)) => I64("binary-sub".reflectCtrlWith[Num](x_c, y_c), "sym-binary-sub".reflectCtrlWith[SymVal](x_s, y_s))
        case (F32(x_c, x_s), F32(y_c, y_s)) => F32("binary-sub".reflectCtrlWith[Num](x_c, y_c), "sym-binary-sub".reflectCtrlWith[SymVal](x_s, y_s))
        case (F64(x_c, x_s), F64(y_c, y_s)) => F64("binary-sub".reflectCtrlWith[Num](x_c, y_c), "sym-binary-sub".reflectCtrlWith[SymVal](x_s, y_s))
      }
    }

    def *(rhs: StagedNum): StagedNum = {
      (num, rhs) match {
        case (I32(x_c, x_s), I32(y_c, y_s)) => I32("binary-mul".reflectCtrlWith[Num](x_c, y_c), "sym-binary-mul".reflectCtrlWith[SymVal](x_s, y_s))
        case (I64(x_c, x_s), I64(y_c, y_s)) => I64("binary-mul".reflectCtrlWith[Num](x_c, y_c), "sym-binary-mul".reflectCtrlWith[SymVal](x_s, y_s))
        case (F32(x_c, x_s), F32(y_c, y_s)) => F32("binary-mul".reflectCtrlWith[Num](x_c, y_c), "sym-binary-mul".reflectCtrlWith[SymVal](x_s, y_s))
        case (F64(x_c, x_s), F64(y_c, y_s)) => F64("binary-mul".reflectCtrlWith[Num](x_c, y_c), "sym-binary-mul".reflectCtrlWith[SymVal](x_s, y_s))
      }
    }

    def /(rhs: StagedNum): StagedNum = {
      (num, rhs) match {
        case (I32(x_c, x_s), I32(y_c, y_s)) => I32("binary-div".reflectCtrlWith[Num](x_c, y_c), "sym-binary-div".reflectCtrlWith[SymVal](x_s, y_s))
        case (I64(x_c, x_s), I64(y_c, y_s)) => I64("binary-div".reflectCtrlWith[Num](x_c, y_c), "sym-binary-div".reflectCtrlWith[SymVal](x_s, y_s))
        case (F32(x_c, x_s), F32(y_c, y_s)) => F32("binary-div".reflectCtrlWith[Num](x_c, y_c), "sym-binary-div".reflectCtrlWith[SymVal](x_s, y_s))
        case (F64(x_c, x_s), F64(y_c, y_s)) => F64("binary-div".reflectCtrlWith[Num](x_c, y_c), "sym-binary-div".reflectCtrlWith[SymVal](x_s, y_s))
      }
    }

    def <<(rhs: StagedNum): StagedNum = {
      (num, rhs) match {
        case (I32(x_c, x_s), I32(y_c, y_s)) => I32("binary-shl".reflectCtrlWith[Num](x_c, y_c), "sym-binary-shl".reflectCtrlWith[SymVal](x_s, y_s))
        case (I64(x_c, x_s), I64(y_c, y_s)) => I64("binary-shl".reflectCtrlWith[Num](x_c, y_c), "sym-binary-shl".reflectCtrlWith[SymVal](x_s, y_s))
        case (F32(x_c, x_s), F32(y_c, y_s)) => F32("binary-shl".reflectCtrlWith[Num](x_c, y_c), "sym-binary-shl".reflectCtrlWith[SymVal](x_s, y_s))
        case (F64(x_c, x_s), F64(y_c, y_s)) => F64("binary-shl".reflectCtrlWith[Num](x_c, y_c), "sym-binary-shl".reflectCtrlWith[SymVal](x_s, y_s))
      }
    }

    def >>(rhs: StagedNum): StagedNum = {
      (num, rhs) match {
        case (I32(x_c, x_s), I32(y_c, y_s)) => I32("binary-shr".reflectCtrlWith[Num](x_c, y_c), "sym-binary-shr".reflectCtrlWith[SymVal](x_s, y_s))
        case (I64(x_c, x_s), I64(y_c, y_s)) => I64("binary-shr".reflectCtrlWith[Num](x_c, y_c), "sym-binary-shr".reflectCtrlWith[SymVal](x_s, y_s))
        case (F32(x_c, x_s), F32(y_c, y_s)) => F32("binary-shr".reflectCtrlWith[Num](x_c, y_c), "sym-binary-shr".reflectCtrlWith[SymVal](x_s, y_s))
        case (F64(x_c, x_s), F64(y_c, y_s)) => F64("binary-shr".reflectCtrlWith[Num](x_c, y_c), "sym-binary-shr".reflectCtrlWith[SymVal](x_s, y_s))
      }
    }

    def &(rhs: StagedNum): StagedNum = {
      (num, rhs) match {
        case (I32(x_c, x_s), I32(y_c, y_s)) => I32("binary-and".reflectCtrlWith[Num](x_c, y_c), "sym-binary-and".reflectCtrlWith[SymVal](x_s, y_s))
        case (I64(x_c, x_s), I64(y_c, y_s)) => I64("binary-and".reflectCtrlWith[Num](x_c, y_c), "sym-binary-and".reflectCtrlWith[SymVal](x_s, y_s))
        case (F32(x_c, x_s), F32(y_c, y_s)) => F32("binary-and".reflectCtrlWith[Num](x_c, y_c), "sym-binary-and".reflectCtrlWith[SymVal](x_s, y_s))
        case (F64(x_c, x_s), F64(y_c, y_s)) => F64("binary-and".reflectCtrlWith[Num](x_c, y_c), "sym-binary-and".reflectCtrlWith[SymVal](x_s, y_s))
      }
    }

    def numEq(rhs: StagedNum): StagedNum = {
      (num, rhs) match {
        case (I32(x_c, x_s), I32(y_c, y_s)) => I32("relation-eq".reflectCtrlWith[Num](x_c, y_c), "sym-relation-eq".reflectCtrlWith[SymVal](x_s, y_s))
        case (I64(x_c, x_s), I64(y_c, y_s)) => I32("relation-eq".reflectCtrlWith[Num](x_c, y_c), "sym-relation-eq".reflectCtrlWith[SymVal](x_s, y_s))
      }
    }

    def numNe(rhs: StagedNum): StagedNum = {
      (num, rhs) match {
        case (I32(x_c, x_s), I32(y_c, y_s)) => I32("relation-ne".reflectCtrlWith[Num](x_c, y_c), "sym-relation-ne".reflectCtrlWith[SymVal](x_s, y_s))
        case (I64(x_c, x_s), I64(y_c, y_s)) => I32("relation-ne".reflectCtrlWith[Num](x_c, y_c), "sym-relation-ne".reflectCtrlWith[SymVal](x_s, y_s))
      }
    }

    def <(rhs: StagedNum): StagedNum = {
      (num, rhs) match {
        case (I32(x_c, x_s), I32(y_c, y_s)) => I32("relation-lt".reflectCtrlWith[Num](x_c, y_c), "sym-relation-lt".reflectCtrlWith[SymVal](x_s, y_s))
        case (I64(x_c, x_s), I64(y_c, y_s)) => I32("relation-lt".reflectCtrlWith[Num](x_c, y_c), "sym-relation-lt".reflectCtrlWith[SymVal](x_s, y_s))
      }
    }

    def ltu(rhs: StagedNum): StagedNum = {
      (num, rhs) match {
        case (I32(x_c, x_s), I32(y_c, y_s)) => I32("relation-ltu".reflectCtrlWith[Num](x_c, y_c), "sym-relation-ltu".reflectCtrlWith[SymVal](x_s, y_s))
        case (I64(x_c, x_s), I64(y_c, y_s)) => I32("relation-ltu".reflectCtrlWith[Num](x_c, y_c), "sym-relation-ltu".reflectCtrlWith[SymVal](x_s, y_s))
      }
    }

    def >(rhs: StagedNum): StagedNum = {
      (num, rhs) match {
        case (I32(x_c, x_s), I32(y_c, y_s)) => I32("relation-gt".reflectCtrlWith[Num](x_c, y_c), "sym-relation-gt".reflectCtrlWith[SymVal](x_s, y_s))
        case (I64(x_c, x_s), I64(y_c, y_s)) => I32("relation-gt".reflectCtrlWith[Num](x_c, y_c), "sym-relation-gt".reflectCtrlWith[SymVal](x_s, y_s))
      }
    }

    def gtu(rhs: StagedNum): StagedNum = {
      (num, rhs) match {
        case (I32(x_c, x_s), I32(y_c, y_s)) => I32("relation-gtu".reflectCtrlWith[Num](x_c, y_c), "sym-relation-gtu".reflectCtrlWith[SymVal](x_s, y_s))
        case (I64(x_c, x_s), I64(y_c, y_s)) => I32("relation-gtu".reflectCtrlWith[Num](x_c, y_c), "sym-relation-gtu".reflectCtrlWith[SymVal](x_s, y_s))
      }
    }

    def <=(rhs: StagedNum): StagedNum = {
      (num, rhs) match {
        case (I32(x_c, x_s), I32(y_c, y_s)) => I32("relation-le".reflectCtrlWith[Num](x_c, y_c), "sym-relation-le".reflectCtrlWith[SymVal](x_s, y_s))
        case (I64(x_c, x_s), I64(y_c, y_s)) => I32("relation-le".reflectCtrlWith[Num](x_c, y_c), "sym-relation-le".reflectCtrlWith[SymVal](x_s, y_s))
      }
    }

    def leu(rhs: StagedNum): StagedNum = {
      (num, rhs) match {
        case (I32(x_c, x_s), I32(y_c, y_s)) => I32("relation-leu".reflectCtrlWith[Num](x_c, y_c), "sym-relation-leu".reflectCtrlWith[SymVal](x_s, y_s))
        case (I64(x_c, x_s), I64(y_c, y_s)) => I32("relation-leu".reflectCtrlWith[Num](x_c, y_c), "sym-relation-leu".reflectCtrlWith[SymVal](x_s, y_s))
      }
    }

    def >=(rhs: StagedNum): StagedNum = {
      (num, rhs) match {
        case (I32(x_c, x_s), I32(y_c, y_s)) => I32("relation-ge".reflectCtrlWith[Num](x_c, y_c), "sym-relation-ge".reflectCtrlWith[SymVal](x_s, y_s))
        case (I64(x_c, x_s), I64(y_c, y_s)) => I32("relation-ge".reflectCtrlWith[Num](x_c, y_c), "sym-relation-ge".reflectCtrlWith[SymVal](x_s, y_s))
      }
    }

    def geu(rhs: StagedNum): StagedNum = {
      (num, rhs) match {
        case (I32(x_c, x_s), I32(y_c, y_s)) => I32("relation-geu".reflectCtrlWith[Num](x_c, y_c), "sym-relation-geu".reflectCtrlWith[SymVal](x_s, y_s))
        case (I64(x_c, x_s), I64(y_c, y_s)) => I32("relation-geu".reflectCtrlWith[Num](x_c, y_c), "sym-relation-geu".reflectCtrlWith[SymVal](x_s, y_s))
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
    case Node(_, "sym-binary-add", List(lhs, rhs), _) =>
      shallow(lhs); emit(".add("); shallow(rhs); emit(")")
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
    case Node(_, "sym-env-read", List(sym), _) =>
      emit("SymEnv.read("); shallow(sym); emit(")")
    case Node(_, "assert-true", List(cond), _) =>
      emit("assert("); shallow(cond); emit(")")
    case Node(_, "tree-fill-if-else", List(s), _) => 
      emit("ExploreTree.fillIfElseNode("); shallow(s); emit(")")
    case Node(_, "tree-move-cursor", List(b), _) =>
      emit("ExploreTree.moveCursor("); shallow(b); emit(")")
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
    emitln("""
    |/*****************************************
    |End of Generated Code
    |*******************************************/
    |int main(int argc, char *argv[]) {
    |  Snippet(std::monostate{});
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

  def compile(moduleInst: ModuleInstance, main: Option[String], printRes: Boolean, dumpTree: Option[String]): GeneratedCpp = {
    println(s"Now compiling wasm module with entry function $main")
    val driver = new WasmToCppCompilerDriver[Unit, Unit] {
      def module: ModuleInstance = moduleInst
      def snippet(x: Rep[Unit]): Rep[Unit] = {
        evalTop(main, printRes, dumpTree)
      }
    }
    GeneratedCpp(driver.code, driver.codegen.includePaths.toList)
  }

  def compileToExe(moduleInst: ModuleInstance,
                   main: Option[String],
                   outputCpp: String,
                   outputExe: String,
                   printRes: Boolean,
                   dumpTree: Option[String]): Unit = {
    val generated = compile(moduleInst, main, printRes, dumpTree)
    val code = generated.source

    val writer = new java.io.PrintWriter(new java.io.File(outputCpp))
    try {
      writer.write(code)
    } finally {
      writer.close()
    }

    import sys.process._
    val command = s"g++ -std=c++17 $outputCpp -o $outputExe -O3 -g " + generated.headerFolders.map(f => s"-I$f").mkString(" ")
    if (command.! != 0) {
      throw new RuntimeException(s"Compilation failed for $outputCpp")
    }
  }

}


