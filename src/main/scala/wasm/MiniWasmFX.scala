package gensym.wasm.miniwasm

import gensym.wasm.ast._
import gensym.wasm.source._
import gensym.wasm.memory._

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import Console.{GREEN, RED, RESET, YELLOW_B, UNDERLINED}

case class EvaluatorFX(module: ModuleInstance) {
  import Primtives._
  implicit val m: ModuleInstance = module

  type Stack = List[Value]

  trait Cont[A] {
    def apply(stack: Stack, trail: Trail[A], mcont: MCont[A]): A
  }
  type Trail[A] = List[Cont[A]]
  type MCont[A] = Stack => A

  type Handler[A] = Stack => A
  type Handlers[A] = List[(Int, Handler[A])]

  case class ContV[A](k: (Stack, Cont[A], List[Cont[A]], MCont[A], Handlers[A]) => A) extends Value {
    def tipe(implicit m: ModuleInstance): ValueType = ???
  }

  // initK is a continuation that simply returns the inputed stack
  def initK[Ans](s: Stack, trail: List[Cont[Ans]], mkont: MCont[Ans]): Ans =
    trail match {
      case k1 :: trail => k1(s, trail, mkont)
      case Nil => mkont(s)
    }

  // Only used for resumable try-catch (need refactoring):
  case class TCContV[A](k: (Stack, Cont[A], List[Cont[A]], MCont[A]) => A) extends Value {
    def tipe(implicit m: ModuleInstance): ValueType = ???
  }

  def evalCall[Ans](funcIndex: Int,
                    rest: List[Instr],
                    stack: List[Value],
                    frame: Frame,
                    kont: Cont[Ans],
                    trail: List[Cont[Ans]],
                    mkont: MCont[Ans],
                    brTable: List[Cont[Ans]],
                    h: Handlers[Ans],
                    isTail: Boolean): Ans = {
    module.funcs(funcIndex) match {
      case FuncDef(_, FuncBodyDef(ty, _, locals, body)) =>
        val args = stack.take(ty.inps.size).reverse
        val newStack = stack.drop(ty.inps.size)
        val frameLocals = args ++ locals.map(zero(_))
        val newFrame = Frame(ArrayBuffer(frameLocals: _*))
        if (isTail)
          // when tail call, share the continuation for returning with the callee
          eval(body, List(), newFrame, kont, trail, mkont, List(kont), h)
        else {
          val restK: Cont[Ans] = (retStack, trail, mkont) =>
            eval(rest, retStack.take(ty.out.size) ++ newStack, frame, kont, trail, mkont, brTable, h)
          // We make a new brTable by `restK`, since function creates a new block to escape
          // (more or less like `return`)
          eval(body, List(), newFrame, restK, trail, mkont, List(restK), h)
        }
      case Import("console", "log", _) =>
        // println(s"[DEBUG] current stack: $stack")
        val I32V(v) :: newStack = stack
        println(v)
        eval(rest, newStack, frame, kont, trail, mkont, brTable, h)
      case Import("spectest", "print_i32", _) =>
        // println(s"[DEBUG] current stack: $stack")
        val I32V(v) :: newStack = stack
        println(v)
        eval(rest, newStack, frame, kont, trail, mkont, brTable, h)
      case Import(_, _, _) => throw new Exception(s"Unknown import at $funcIndex")
      case _               => throw new Exception(s"Definition at $funcIndex is not callable")
    }
  }

  def eval[Ans](insts: List[Instr],
                stack: List[Value],
                frame: Frame,
                kont: Cont[Ans],
                trail: List[Cont[Ans]],
                mkont: MCont[Ans],
                brTable: List[Cont[Ans]],
                h: Handlers[Ans]): Ans = {
    // Note kont is only used in the base case, or when we are appending k and k1
    if (insts.isEmpty) return kont(stack, trail, mkont)

    val inst = insts.head
    val rest = insts.tail

    // TODO: uncommenting this will fail tests that uses `testFileOutput`
    // println(s"inst: ${inst} \t | ${frame.locals} | ${stack.reverse}" )

    inst match {
      case Drop => eval(rest, stack.tail, frame, kont, trail, mkont, brTable, h)
      case Select(_) =>
        val I32V(cond) :: v2 :: v1 :: newStack = stack
        val value = if (cond == 0) v1 else v2
        eval(rest, value :: newStack, frame, kont, trail, mkont, brTable, h)
      case LocalGet(i) =>
        eval(rest, frame.locals(i) :: stack, frame, kont, trail, mkont, brTable, h)
      case LocalSet(i) =>
        val value :: newStack = stack
        frame.locals(i) = value
        eval(rest, newStack, frame, kont, trail, mkont, brTable, h)
      case LocalTee(i) =>
        val value :: newStack = stack
        frame.locals(i) = value
        eval(rest, stack, frame, kont, trail, mkont, brTable, h)
      case GlobalGet(i) =>
        eval(rest, module.globals(i).value :: stack, frame, kont, trail, mkont, brTable, h)
      case GlobalSet(i) =>
        val value :: newStack = stack
        module.globals(i).ty match {
          case GlobalType(tipe, true) if value.tipe == tipe =>
            module.globals(i).value = value
          case GlobalType(_, true) => throw new Exception("Invalid type")
          case _                   => throw new Exception("Cannot set immutable global")
        }
        eval(rest, newStack, frame, kont, trail, mkont, brTable, h)
      case MemorySize =>
        eval(rest, I32V(module.memory.head.size) :: stack, frame, kont, trail, mkont, brTable, h)
      case MemoryGrow =>
        val I32V(delta) :: newStack = stack
        val mem = module.memory.head
        val oldSize = mem.size
        mem.grow(delta) match {
          case Some(e) =>
            eval(rest, I32V(-1) :: newStack, frame, kont, trail, mkont, brTable, h)
          case _ =>
            eval(rest, I32V(oldSize) :: newStack, frame, kont, trail, mkont, brTable, h)
        }
      case MemoryFill =>
        val I32V(value) :: I32V(offset) :: I32V(size) :: newStack = stack
        if (memOutOfBound(module, 0, offset, size))
          throw new Exception("Out of bounds memory access") // GW: turn this into a `trap`?
        else {
          module.memory.head.fill(offset, size, value.toByte)
          eval(rest, newStack, frame, kont, trail, mkont, brTable, h)
        }
      case MemoryCopy =>
        val I32V(n) :: I32V(src) :: I32V(dest) :: newStack = stack
        if (memOutOfBound(module, 0, src, n) || memOutOfBound(module, 0, dest, n))
          throw new Exception("Out of bounds memory access")
        else {
          module.memory.head.copy(dest, src, n)
          eval(rest, newStack, frame, kont, trail, mkont, brTable, h)
        }
      case Const(n) => eval(rest, n :: stack, frame, kont, trail, mkont, brTable, h)
      case Binary(op) =>
        val v2 :: v1 :: newStack = stack
        eval(rest, evalBinOp(op, v1, v2) :: newStack, frame, kont, trail, mkont, brTable, h)
      case Unary(op) =>
        val v :: newStack = stack
        eval(rest, evalUnaryOp(op, v) :: newStack, frame, kont, trail, mkont, brTable, h)
      case Compare(op) =>
        val v2 :: v1 :: newStack = stack
        eval(rest, evalRelOp(op, v1, v2) :: newStack, frame, kont, trail, mkont, brTable, h)
      case Test(op) =>
        val v :: newStack = stack
        eval(rest, evalTestOp(op, v) :: newStack, frame, kont, trail, mkont, brTable, h)
      case Store(StoreOp(align, offset, ty, None)) =>
        val I32V(v) :: I32V(addr) :: newStack = stack
        module.memory(0).storeInt(addr + offset, v)
        eval(rest, newStack, frame, kont, trail, mkont, brTable, h)
      case Load(LoadOp(align, offset, ty, None, None)) =>
        val I32V(addr) :: newStack = stack
        val value = module.memory(0).loadInt(addr + offset)
        eval(rest, I32V(value) :: newStack, frame, kont, trail, mkont, brTable, h)
      case Nop =>
        eval(rest, stack, frame, kont, trail, mkont, brTable, h)
      case Unreachable => throw Trap()
      case Block(ty, inner) =>
        val funcTy = getFuncType(ty)
        val (inputs, restStack) = stack.splitAt(funcTy.inps.size)
        // why a block is introducing a new mknot1
        // I feel like we will almost never need to change the mkont for a block
        val restK: Cont[Ans] = (retStack, trail, mkont1) => {
          // kont -> mkont -> mkont1
          eval(rest, retStack.take(funcTy.out.size) ++ restStack, frame, kont, trail, mkont1, brTable, h)
        }
        eval(inner, inputs, frame, restK, trail, mkont, restK :: brTable, h)
      case Loop(ty, inner) =>
        // We construct two continuations, one for the break (to the begining of the loop),
        // and one for fall-through to the next instruction following the syntactic structure
        // of the program.
        val funcTy = getFuncType(ty)
        val (inputs, restStack) = stack.splitAt(funcTy.inps.size)
        val restK: Cont[Ans] = (retStack, trail, mkont) =>
          eval(rest, retStack.take(funcTy.out.size) ++ restStack, frame, kont, trail, mkont, brTable, h)
        def loop(retStack: List[Value], trail: List[Cont[Ans]], mkont: MCont[Ans]): Ans =
          eval(inner, retStack.take(funcTy.inps.size), frame, restK, trail, mkont, (loop _ : Cont[Ans]):: brTable, h)
        // for example, loop here doesn't change the meta-continuation at all
        // compare with block
        loop(inputs, trail, mkont)
      case If(ty, thn, els) =>
        val funcTy = getFuncType(ty)
        val I32V(cond) :: newStack = stack
        val inner = if (cond != 0) thn else els
        val (inputs, restStack) = newStack.splitAt(funcTy.inps.size)
        val restK: Cont[Ans] = (retStack, trail, mkont) =>
          eval(rest, retStack.take(funcTy.out.size) ++ restStack, frame, kont, trail, mkont, brTable, h)
        eval(inner, inputs, frame, restK, trail, mkont, restK :: brTable, h)
      case Br(label) =>
        brTable(label)(stack, trail, mkont) // s => ().asInstanceOf[Ans]) //mkont)
      case BrIf(label) =>
        val I32V(cond) :: newStack = stack
        if (cond != 0) brTable(label)(newStack, trail, mkont)
        else eval(rest, newStack, frame, kont, trail, mkont, brTable, h)
      case BrTable(labels, default) =>
        val I32V(cond) :: newStack = stack
        val goto = if (cond < labels.length) labels(cond) else default
        brTable(goto)(newStack, trail, mkont)
      case Return        => brTable.last(stack, trail, mkont)
      case Call(f)       => evalCall(f, rest, stack, frame, kont, trail, mkont, brTable, h, false)
      case ReturnCall(f) => evalCall(f, rest, stack, frame, kont, trail, mkont, brTable, h, true)
      case RefFunc(f)    =>
        // TODO: RefFuncV stores an applicable function, instead of a syntactic structure
        eval(rest, RefFuncV(f) :: stack, frame, kont, trail, mkont, brTable, h)
      case CallRef(ty) =>
        val RefFuncV(f) :: newStack = stack
        evalCall(f, rest, newStack, frame, kont, trail, mkont, brTable, h, false)

      // resumable try-catch exception handling:
      case TryCatch(es1, es2) =>
        // put trail into join point
        val join: MCont[Ans] = (newStack) => eval(rest, stack, frame, kont, trail, mkont, brTable, h)
        // here we clear the brTable, to forbid breaking out of the try-catch block
        val newHandler: Handler[Ans] = (newStack) => eval(es2, newStack, frame, initK: Cont[Ans], List(), join, List(), h)
        eval(es1, List(), frame, initK: Cont[Ans], List(), join, List(), List((-1, newHandler)) ++ h)
      case Resume0() =>
        val (resume: TCContV[Ans]) :: newStack = stack
        val k: Cont[Ans] = (s, trail, m) => eval(rest, newStack /*!*/, frame, kont, trail, m, brTable, h)
        resume.k(List(), k, trail, mkont)
      case Throw() =>
        val err :: newStack = stack
        // kont composed with k1 and trail
        // note that kr doesn't use the stack at all
        // it only takes the err value
        def kr(s: Stack, k1: Cont[Ans], newtrail: List[Cont[Ans]], m: MCont[Ans]): Ans = {
          eval(rest, newStack /*!*/, frame, kont, trail ++ List(k1) ++ newtrail, m /*vs mkont?*/, brTable, h)
        }
        h.head._2(List(err, TCContV(kr)))

      // WasmFX effect handlers:
      case ContNew(ty) =>
        val RefFuncV(f) :: newStack = stack
        def kr(s: Stack, k1: Cont[Ans], trail: List[Cont[Ans]], mk: MCont[Ans], hs: Handlers[Ans]): Ans = {
          evalCall(f, List(), s, frame/*?*/, k1, trail, mk, List(), hs, false)
        }
        eval(rest, ContV(kr) :: newStack, frame, kont, trail, mkont, brTable, h)
      case Suspend(tagId) =>
        val FuncType(_, inps, out) = module.tags(tagId)
        val (inputs, restStack) = stack.splitAt(inps.size)
        val kr = (s: Stack, k1: Cont[Ans], newTrail: List[Cont[Ans]], mk: MCont[Ans], hs: Handlers[Ans]) => {
          eval(rest, s ++ restStack, frame, kont, trail ++ (k1 :: newTrail), mk, brTable, hs)
        }
        val newStack = ContV(kr) :: inputs
        h.find(_._1 == tagId) match {
          case Some((_, handler)) => handler(newStack)
          case None               => throw new Exception(s"no handler for tag $tagId")
        }
      case Resume(tyId, handler) =>
        val (f: ContV[Ans]) :: newStack = stack
        val ContType(funcTypeId) = module.types(tyId)
        val FuncType(_, inps, out) = module.types(funcTypeId)
        val (inputs, restStack) = newStack.splitAt(inps.size)
        val newHs: List[(Int, Handler[Ans])] = handler.map {
          case Handler(tagId, labelId) => (tagId, (newStack) => brTable(labelId)(newStack, trail, mkont))
        }
        val newCont: Cont[Ans] = (s, trail, mk) => eval(rest, s, frame, kont, trail, mk, brTable, h)
        val newMk: MCont[Ans] = (s) => eval(rest, s, frame, kont, trail, mkont, brTable, h)
        f.k(inputs, initK, List(), newMk, newHs ++ h)

      case ContBind(oldContTyId, newConTyId) =>
        val (f: ContV[Ans]) :: newStack = stack
        // use oldParamTy - newParamTy to get how many values to pop from the stack
        val ContType(oldId) = module.types(oldContTyId)
        val FuncType(_, oldParamTy, _) = module.types(oldId)
        val ContType(newId) = module.types(newConTyId)
        val FuncType(_, newParamTy, _) = module.types(newId)

        // get oldParamTy - newParamTy (there's no type checking at all)
        val inputSize = oldParamTy.size - newParamTy.size

        val (inputs, restStack) = newStack.splitAt(inputSize)

        // partially apply the old continuation
        def kr(s: Stack, k1: Cont[Ans], trail: List[Cont[Ans]], mk: MCont[Ans], handlers: Handlers[Ans]): Ans = {
          f.k(s ++ inputs, k1, trail, mk, handlers)
        }

        eval(rest, ContV(kr) :: restStack, frame, kont, trail, mkont, brTable, h)

      case CallRef(ty) =>
        val RefFuncV(f) :: newStack = stack
        evalCall(f, rest, newStack, frame, kont, trail, mkont, brTable, h, false)

      case CallRef(ty) =>
        val RefFuncV(f) :: newStack = stack
        evalCall(f, rest, newStack, frame, kont, trail, mkont, brTable, h, false)

      case _ =>
        println(inst)
        throw new Exception(s"instruction $inst not implemented")
    }
  }

  // If `main` is given, then we use that function as the entry point of the program;
  // otherwise, we look up the top-level `start` instruction to locate the entry point.
  def evalTop[Ans](halt: Cont[Ans], mhalt: MCont[Ans], main: Option[String] = None): Ans = {
    val instrs = main match {
      case Some(func_name) =>
        module.defs.flatMap({
          case Export(`func_name`, ExportFunc(fid)) =>
            System.err.println(s"Entering function $main")
            module.funcs(fid) match {
              case FuncDef(_, FuncBodyDef(_, _, locals, body)) => body
              case _ => throw new Exception("Entry function has no concrete body")
            }
          case _ => List()
        })
      case None =>
        module.defs.flatMap({
          case Start(id) =>
            System.err.println(s"Entering unnamed function $id")
            module.funcs(id) match {
              case FuncDef(_, FuncBodyDef(_, _, locals, body)) => body
              case _ =>
                throw new Exception("Entry function has no concrete body")
            }
          case _ => List()
        })
    }
    val locals = main match {
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
              case _ =>
                throw new Exception("Entry function has no concrete body")
            }
          case _ => List()
        })
    }
    if (instrs.isEmpty) println("Warning: nothing is executed")
    // initialized locals
    val frame = Frame(ArrayBuffer(locals.map(zero(_)): _*))
    eval(instrs, List(), frame, halt, List(), mhalt, List(halt), List())
  }

  def evalTop(m: ModuleInstance): Unit = evalTop(initK[Unit], stack => ())
}
