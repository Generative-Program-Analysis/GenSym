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

  type Handler[A] = (Stack, Cont[A], Trail[A], MCont[A]) => A
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

  def eval1[Ans](inst: Instr, stack: Stack, frame: Frame,
                 kont: Cont[Ans], trail: List[Cont[Ans]], mkont: MCont[Ans],
                 brTable: List[Cont[Ans]], hs: Handlers[Ans]): Ans =
    inst match {
      case Drop => kont(stack.tail, trail, mkont)
      case Select(_) =>
        val I32V(cond) :: v2 :: v1 :: newStack = stack
        val value = if (cond == 0) v1 else v2
        kont(value :: newStack, trail, mkont)
      case LocalGet(i) =>
        kont(frame.locals(i) :: stack, trail, mkont)
      case LocalSet(i) =>
        val value :: newStack = stack
        frame.locals(i) = value
        kont(newStack, trail, mkont)
      case LocalTee(i) =>
        val value :: newStack = stack
        frame.locals(i) = value
        kont(stack, trail, mkont)
      case GlobalGet(i) =>
        kont(module.globals(i).value :: stack, trail, mkont)
      case GlobalSet(i) =>
        val value :: newStack = stack
        module.globals(i).ty match {
          case GlobalType(tipe, true) if value.tipe == tipe =>
            module.globals(i).value = value
          case GlobalType(_, true) => throw new Exception("Invalid type")
          case _                   => throw new Exception("Cannot set immutable global")
        }
        kont(newStack, trail, mkont)
      case MemorySize =>
        kont(I32V(module.memory.head.size) :: stack, trail, mkont)
      case MemoryGrow =>
        val I32V(delta) :: newStack = stack
        val mem = module.memory.head
        val oldSize = mem.size
        mem.grow(delta) match {
          case Some(e) => kont(I32V(-1) :: newStack, trail, mkont)
          case _ => kont(I32V(oldSize) :: newStack, trail, mkont)
        }
      case MemoryFill =>
        val I32V(value) :: I32V(offset) :: I32V(size) :: newStack = stack
        if (memOutOfBound(module, 0, offset, size))
          throw new Exception("Out of bounds memory access") // GW: turn this into a `trap`?
        else {
          module.memory.head.fill(offset, size, value.toByte)
          kont(newStack, trail, mkont)
        }
      case MemoryCopy =>
        val I32V(n) :: I32V(src) :: I32V(dest) :: newStack = stack
        if (memOutOfBound(module, 0, src, n) || memOutOfBound(module, 0, dest, n))
          throw new Exception("Out of bounds memory access")
        else {
          module.memory.head.copy(dest, src, n)
          kont(newStack, trail, mkont)
        }
      case Const(n) => kont(n :: stack, trail, mkont)
      case Binary(op) =>
        val v2 :: v1 :: newStack = stack
        kont(evalBinOp(op, v1, v2) :: newStack, trail, mkont)
      case Unary(op) =>
        val v :: newStack = stack
        kont(evalUnaryOp(op, v) :: newStack, trail, mkont)
      case Compare(op) =>
        val v2 :: v1 :: newStack = stack
        kont(evalRelOp(op, v1, v2) :: newStack, trail, mkont)
      case Test(op) =>
        val v :: newStack = stack
        kont(evalTestOp(op, v) :: newStack, trail, mkont)
      case Store(StoreOp(align, offset, ty, None)) =>
        val I32V(v) :: I32V(addr) :: newStack = stack
        module.memory(0).storeInt(addr + offset, v)
        kont(newStack, trail, mkont)
      case Load(LoadOp(align, offset, ty, None, None)) =>
        val I32V(addr) :: newStack = stack
        val value = module.memory(0).loadInt(addr + offset)
        kont(I32V(value) :: newStack, trail, mkont)
      case Nop => kont(stack, trail, mkont)
      case Unreachable => throw Trap()
      case Block(ty, inner) =>
        val funcTy = getFuncType(ty)
        val (inputs, restStack) = stack.splitAt(funcTy.inps.size)
        val escape: Cont[Ans] = (s1, t1, m1) => kont(s1.take(funcTy.out.size) ++ restStack, t1, m1)
        evalList(inner, inputs, frame, escape, trail, mkont, escape::brTable, hs)
      case Loop(ty, inner) =>
        val funcTy = getFuncType(ty)
        val (inputs, restStack) = stack.splitAt(funcTy.inps.size)
        val escape: Cont[Ans] = (s1, t1, m1) => kont(s1.take(funcTy.out.size) ++ restStack, t1, m1)
        def loop(retStack: List[Value], trail1: List[Cont[Ans]], mkont: MCont[Ans]): Ans =
          evalList(inner, retStack.take(funcTy.inps.size), frame, escape, trail, mkont, (loop _ : Cont[Ans])::brTable, hs)
        loop(inputs, trail, mkont)
      case If(ty, thn, els) =>
        val funcTy = getFuncType(ty)
        val I32V(cond) :: newStack = stack
        val inner = if (cond != 0) thn else els
        val (inputs, restStack) = newStack.splitAt(funcTy.inps.size)
        val escape: Cont[Ans] = (s1, t1, m1) => kont(s1.take(funcTy.out.size) ++ restStack, t1, m1)
        evalList(inner, inputs, frame, escape, trail, mkont, escape::brTable, hs)
      case Br(label) =>
        brTable(label)(stack, trail, mkont)
      case BrIf(label) =>
        val I32V(cond) :: newStack = stack
        if (cond != 0) brTable(label)(newStack, trail, mkont)
        else kont(newStack, trail, mkont)
      case BrTable(labels, default) =>
        val I32V(cond) :: newStack = stack
        val goto = if (cond < labels.length) labels(cond) else default
        brTable(goto)(newStack, trail, mkont)
      case Return        => brTable.last(stack, trail, mkont)
      case Call(f)       => evalCall1(f, stack, frame, kont, trail, mkont, brTable, hs, false)
      case ReturnCall(f) => evalCall1(f, stack, frame, kont, trail, mkont, brTable, hs, true)
      case RefFunc(f)    =>
        // TODO: RefFuncV stores an applicable function, instead of a syntactic structure
        kont(RefFuncV(f) :: stack, trail, mkont)
      case CallRef(ty) =>
        val RefFuncV(f) :: newStack = stack
        kont(newStack, trail, mkont)

      // resumable try-catch exception handling:
      // NOTE(GW): so far we haven't use trail at all, could consider removing it
      case TryCatch(es1, es2) =>
        val newHandler: Handler[Ans] = (s1, k1, _, m1) => evalList(es2, s1, frame, initK[Ans], List(), m1, List(), hs)
        val m1: MCont[Ans] = (s1) => kont(s1, List(), mkont)
        evalList(es1, List(), frame, initK[Ans], List(), m1, List(), List((-1, newHandler)) ++ hs)
      case Resume0() =>
        val (resume: TCContV[Ans]) :: newStack = stack
        resume.k(List(), kont, List(), mkont)
      case Throw() =>
        val err :: newStack = stack
        def kr(s: Stack, k1: Cont[Ans], t1: List[Cont[Ans]], m1: MCont[Ans]): Ans = kont(s, List(), s1 => k1(s1, List(), m1))
        hs.head._2(List(err, TCContV(kr)), kont, List(), mkont)

      /*
      // WasmFX effect handlers:
      case ContNew(ty) =>
        val RefFuncV(f) :: newStack = stack
        def kr(s: Stack, k1: Cont[Ans], trail1: List[Cont[Ans]], mk: MCont[Ans], hs: Handlers[Ans]): Ans = {
          evalCall(f, List(), s, frame/*?*/, k1, trail1, mk, List(), hs, false)
        }
        eval(rest, ContV(kr) :: newStack, frame, kont, trail1, mkont, trail2, h)
      case Suspend(tagId) =>
        val FuncType(_, inps, out) = module.tags(tagId)
        val (inputs, restStack) = stack.splitAt(inps.size)
        val k = (s: Stack, k1: Cont[Ans], newTrail1: List[Cont[Ans]], mk: MCont[Ans], hs: Handlers[Ans]) => {
          eval(rest, s ++ restStack, frame, kont, trail1 ++ (k1 :: newTrail1), mk, trail2, hs)
        }
        val newStack = ContV(k) :: inputs
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
          case Handler(tagId, labelId) => (tagId, (newStack) => trail2(labelId)(newStack, trail1, mkont))
        }
        //val newCont: Cont[Ans] = (s, trail1, mk) => eval(rest, s, frame, kont, trail1, mk, trail2, h)
        val newMk: MCont[Ans] = (s) => eval(rest, s, frame, kont, trail1, mkont, trail2, h)
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
        def kr(s: Stack, k1: Cont[Ans], trail1: List[Cont[Ans]], mk: MCont[Ans], handlers: Handlers[Ans]): Ans = {
          f.k(s ++ inputs, k1, trail1, mk, handlers)
        }

        eval(rest, ContV(kr) :: restStack, frame, kont, trail1, mkont, trail2, h)

      case CallRef(ty) =>
        val RefFuncV(f) :: newStack = stack
        evalCall(f, rest, newStack, frame, kont, trail1, mkont, trail2, h, false)

      case CallRef(ty) =>
        val RefFuncV(f) :: newStack = stack
        evalCall(f, rest, newStack, frame, kont, trail1, mkont, trail2, h, false)
      */
      case _ =>
        println(inst)
        throw new Exception(s"instruction $inst not implemented")
    }

  def evalList[Ans](insts: List[Instr], stack: Stack, frame: Frame,
                 kont: Cont[Ans], trail1: List[Cont[Ans]], mkont: MCont[Ans],
                 brTable: List[Cont[Ans]], hs: Handlers[Ans]): Ans = {
    insts match {
      case Nil => kont(stack, trail1, mkont)
      case inst :: rest =>
        val newKont: Cont[Ans] = (s1, t1, m1) => evalList(rest, s1, frame, kont, t1, m1, brTable, hs)
        eval1(inst, stack, frame, newKont, trail1, mkont, brTable, hs)
    }
  }

  def evalCall1[Ans](funcIndex: Int,
                     stack: List[Value],
                     frame: Frame,
                     kont: Cont[Ans],
                     trail: List[Cont[Ans]],
                     mkont: MCont[Ans],
                     brTable: List[Cont[Ans]],
                     h: Handlers[Ans],
                     isTail: Boolean): Ans =
    module.funcs(funcIndex) match {
      case FuncDef(_, FuncBodyDef(ty, _, locals, body)) =>
        val args = stack.take(ty.inps.size).reverse
        val newStack = stack.drop(ty.inps.size)
        val frameLocals = args ++ locals.map(zero(_))
        val newFrame = Frame(ArrayBuffer(frameLocals: _*))
        if (isTail)
          // when tail call, share the continuation for returning with the callee
          evalList(body, List(), newFrame, kont, trail, mkont, List(kont), h)
        else {
          val restK: Cont[Ans] = (s1, t1, m1) => kont(s1.take(ty.out.size) ++ newStack, t1, m1)
          // We make a new brTable by `restK`, since function creates a new block to escape
          // (more or less like `return`)
          evalList(body, List(), newFrame, restK, trail, mkont, List(restK), h)
        }
      case Import("console", "log", _) =>
        // println(s"[DEBUG] current stack: $stack")
        val I32V(v) :: newStack = stack
        println(v)
        kont(newStack, trail, mkont)
      case Import("spectest", "print_i32", _) =>
        // println(s"[DEBUG] current stack: $stack")
        val I32V(v) :: newStack = stack
        println(v)
        kont(newStack, trail, mkont)
      case Import(_, _, _) => throw new Exception(s"Unknown import at $funcIndex")
      case _               => throw new Exception(s"Definition at $funcIndex is not callable")
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
    evalList(instrs, List(), frame, halt, List(), mhalt, List(halt), List())
  }

  def evalTop(m: ModuleInstance): Unit = evalTop(initK[Unit], stack => ())
}
