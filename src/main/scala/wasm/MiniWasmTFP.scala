package gensym.wasm.miniwasm

import gensym.wasm.ast._
import gensym.wasm.source._
import gensym.wasm.memory._

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import Console.{GREEN, RED, RESET, YELLOW_B, UNDERLINED}

case class EvaluatorTFP(module: ModuleInstance) {
  import Primtives._
  implicit val m: ModuleInstance = module

  type Stack = List[Value]

  type Cont[A] = (Stack, MCont[A]) => A
  type MCont[A] = Stack => A

  // ZDH TODO: Maybe we can delete the `Cont[A]` type because only `initK` is passed in
  // (the kont for catch block is in the metacontinuation)
  type Handler[A] = (Stack, Cont[A], MCont[A]) => A

  case class ContV[A](k: (Stack, MCont[A], Handler[A]) => A) extends Value {
    def tipe(implicit m: ModuleInstance): ValueType = ???
  }

  // initK is a continuation that simply returns the inputed stack
  def initK[Ans] (s: Stack, m: MCont[Ans]): Ans = m(s)

  def eval1[Ans](inst: Instr, stack: Stack, frame: Frame,
                 kont: Cont[Ans], mkont: MCont[Ans],
                 brTable: List[Cont[Ans]], hs: Handler[Ans]): Ans =
    inst match {
      case Drop => kont(stack.tail, mkont)
      case Select(_) =>
        val I32V(cond) :: v2 :: v1 :: newStack = stack
        val value = if (cond == 0) v1 else v2
        kont(value :: newStack, mkont)
      case LocalGet(i) =>
        kont(frame.locals(i) :: stack, mkont)
      case LocalSet(i) =>
        val value :: newStack = stack
        frame.locals(i) = value
        kont(newStack, mkont)
      case LocalTee(i) =>
        val value :: newStack = stack
        frame.locals(i) = value
        kont(stack, mkont)
      case GlobalGet(i) =>
        kont(module.globals(i).value :: stack, mkont)
      case GlobalSet(i) =>
        val value :: newStack = stack
        module.globals(i).ty match {
          case GlobalType(tipe, true) if value.tipe == tipe =>
            module.globals(i).value = value
          case GlobalType(_, true) => throw new Exception("Invalid type")
          case _                   => throw new Exception("Cannot set immutable global")
        }
        kont(newStack, mkont)
      case MemorySize =>
        kont(I32V(module.memory.head.size) :: stack, mkont)
      case MemoryGrow =>
        val I32V(delta) :: newStack = stack
        val mem = module.memory.head
        val oldSize = mem.size
        mem.grow(delta) match {
          case Some(e) => kont(I32V(-1) :: newStack, mkont)
          case _ => kont(I32V(oldSize) :: newStack, mkont)
        }
      case MemoryFill =>
        val I32V(value) :: I32V(offset) :: I32V(size) :: newStack = stack
        if (memOutOfBound(module, 0, offset, size))
          throw new Exception("Out of bounds memory access") // GW: turn this into a `trap`?
        else {
          module.memory.head.fill(offset, size, value.toByte)
          kont(newStack, mkont)
        }
      case MemoryCopy =>
        val I32V(n) :: I32V(src) :: I32V(dest) :: newStack = stack
        if (memOutOfBound(module, 0, src, n) || memOutOfBound(module, 0, dest, n))
          throw new Exception("Out of bounds memory access")
        else {
          module.memory.head.copy(dest, src, n)
          kont(newStack, mkont)
        }
      case Const(n) => kont(n :: stack, mkont)
      case Binary(op) =>
        val v2 :: v1 :: newStack = stack
        kont(evalBinOp(op, v1, v2) :: newStack, mkont)
      case Unary(op) =>
        val v :: newStack = stack
        kont(evalUnaryOp(op, v) :: newStack, mkont)
      case Compare(op) =>
        val v2 :: v1 :: newStack = stack
        kont(evalRelOp(op, v1, v2) :: newStack, mkont)
      case Test(op) =>
        val v :: newStack = stack
        kont(evalTestOp(op, v) :: newStack, mkont)
      case Store(StoreOp(align, offset, ty, None)) =>
        val I32V(v) :: I32V(addr) :: newStack = stack
        module.memory(0).storeInt(addr + offset, v)
        kont(newStack, mkont)
      case Load(LoadOp(align, offset, ty, None, None)) =>
        val I32V(addr) :: newStack = stack
        val value = module.memory(0).loadInt(addr + offset)
        kont(I32V(value) :: newStack, mkont)
      case Nop => kont(stack, mkont)
      case Unreachable => throw Trap()
      case Block(ty, inner) =>
        val funcTy = getFuncType(ty)
        val (inputs, restStack) = stack.splitAt(funcTy.inps.size)
        val escape: Cont[Ans] = (s1, m1) => kont(s1.take(funcTy.out.size) ++ restStack, m1)
        evalList(inner, inputs, frame, escape, mkont, escape::brTable, hs)
      case Loop(ty, inner) =>
        val funcTy = getFuncType(ty)
        val (inputs, restStack) = stack.splitAt(funcTy.inps.size)
        val escape: Cont[Ans] = (s1, m1) => kont(s1.take(funcTy.out.size) ++ restStack, m1)
        def loop(retStack: List[Value], mkont: MCont[Ans]): Ans =
          evalList(inner, retStack.take(funcTy.inps.size), frame, escape, mkont, (loop _ : Cont[Ans])::brTable, hs)
        loop(inputs, mkont)
      case If(ty, thn, els) =>
        val funcTy = getFuncType(ty)
        val I32V(cond) :: newStack = stack
        val inner = if (cond != 0) thn else els
        val (inputs, restStack) = newStack.splitAt(funcTy.inps.size)
        val escape: Cont[Ans] = (s1, m1) => kont(s1.take(funcTy.out.size) ++ restStack, m1)
        evalList(inner, inputs, frame, escape, mkont, escape::brTable, hs)
      case Br(label) =>
        brTable(label)(stack, mkont)
      case BrIf(label) =>
        val I32V(cond) :: newStack = stack
        if (cond != 0) brTable(label)(newStack, mkont)
        else kont(newStack, mkont)
      case BrTable(labels, default) =>
        val I32V(cond) :: newStack = stack
        val goto = if (cond < labels.length) labels(cond) else default
        brTable(goto)(newStack, mkont)
      case Return        => brTable.last(stack, mkont)
      case Call(f)       => evalCall1(f, stack, frame, kont, mkont, brTable, hs, false)
      case ReturnCall(f) => evalCall1(f, stack, frame, kont, mkont, brTable, hs, true)
      case RefFunc(f)    =>
        // TODO: RefFuncV stores an applicable function, instead of a syntactic structure
        kont(RefFuncV(f) :: stack, mkont)

      // resumable try-catch exception handling:
      case TryCatch(es1, es2) =>
        val newHandler: Handler[Ans] = (s1, k1, m1) => evalList(es2, s1, frame, k1, m1, List(), hs)
        val m1: MCont[Ans] = (s1) => kont(s1, mkont)
        evalList(es1, List(), frame, initK[Ans], m1, List(), newHandler)
      case Resume0() =>
        val (resume: ContV[Ans]) :: newStack = stack
        // no test fail if we pass in 
        resume.k(List(), s => kont(s, mkont), hs)
      case Throw() =>
        val err :: newStack = stack
        // the handlers for kr is at the capture site
        def kr(s1: Stack, m1: MCont[Ans], hs: Handler[Ans]): Ans = kont(s1, m1)
        hs(List(err, ContV(kr)), initK[Ans], mkont)

      case CallRef(ty) =>
        val RefFuncV(f) :: newStack = stack
        evalCall1(f, newStack, frame, kont, mkont, brTable, hs, false)

      case _ =>
        println(inst)
        throw new Exception(s"instruction $inst not implemented")
    }

  def evalList[Ans](insts: List[Instr], stack: Stack, frame: Frame,
                 kont: Cont[Ans], mkont: MCont[Ans],
                 brTable: List[Cont[Ans]], hs: Handler[Ans]): Ans = {
    insts match {
      case Nil => kont(stack, mkont)
      case inst :: rest =>
        val newKont: Cont[Ans] = (s1, m1) => evalList(rest, s1, frame, kont, m1, brTable, hs)
        eval1(inst, stack, frame, newKont, mkont, brTable, hs)
    }
  }

  def evalCall1[Ans](funcIndex: Int,
                     stack: List[Value],
                     frame: Frame,
                     kont: Cont[Ans],
                     mkont: MCont[Ans],
                     brTable: List[Cont[Ans]],
                     h: Handler[Ans],
                     isTail: Boolean): Ans =
    module.funcs(funcIndex) match {
      case FuncDef(_, FuncBodyDef(ty, _, locals, body)) =>
        val args = stack.take(ty.inps.size).reverse
        val newStack = stack.drop(ty.inps.size)
        val frameLocals = args ++ locals.map(zero(_))
        val newFrame = Frame(ArrayBuffer(frameLocals: _*))
        if (isTail)
          // when tail call, share the continuation for returning with the callee
          evalList(body, List(), newFrame, kont, mkont, List(kont), h)
        else {
          val restK: Cont[Ans] = (s1, m1) => kont(s1.take(ty.out.size) ++ newStack, m1)
          // We make a new brTable by `restK`, since function creates a new block to escape
          // (more or less like `return`)
          evalList(body, List(), newFrame, restK, mkont, List(restK), h)
        }
      case Import("console", "log", _) =>
        // println(s"[DEBUG] current stack: $stack")
        val I32V(v) :: newStack = stack
        println(v)
        kont(newStack, mkont)
      case Import("spectest", "print_i32", _) =>
        // println(s"[DEBUG] current stack: $stack")
        val I32V(v) :: newStack = stack
        println(v)
        kont(newStack, mkont)
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
    val handler0: Handler[Ans] = (stack: Stack, _, _) => throw new Exception (s"Uncaught exception: $stack")

    evalList(instrs, List(), frame, halt, mhalt, List(halt), handler0)
  }

  def evalTop(m: ModuleInstance): Unit = evalTop(initK[Unit], stack => ())
}
