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
  type Handlers[A] = List[(Int, Handler[A])]
  // delimited continuation: need a stack, a meta continuation, and a list of exception handlers to resume
  type Cont[A] = (Stack, Handlers[A], MCont[A]) => A
  // meta continuation
  type MCont[A] = Stack => A
  type Handler[A] = Stack => A

  // Only used for resumable try-catch (need refactoring):
  case class TCContV[A](k: Cont[A]) extends Value {
    def tipe(implicit m: ModuleInstance): ValueType = ???
  }

  def evalCall[Ans](funcIndex: Int,
                    rest: List[Instr],
                    stack: List[Value],
                    frame: Frame,
                    kont: Cont[Ans],
                    mkont: MCont[Ans],
                    trail: List[Cont[Ans]],
                    h: Handler[Ans],
                    ehs: List[(Int, Handler[Ans])],
                    isTail: Boolean): Ans = {
    module.funcs(funcIndex) match {
      case FuncDef(_, FuncBodyDef(ty, _, locals, body)) =>
        val args = stack.take(ty.inps.size).reverse
        val newStack = stack.drop(ty.inps.size)
        val frameLocals = args ++ locals.map(zero(_))
        val newFrame = Frame(ArrayBuffer(frameLocals: _*))
        if (isTail)
          // when tail call, share the continuation for returning with the callee
          eval(body, List(), newFrame, kont, mkont, List(kont), h, ehs)
        else {
          val restK: Cont[Ans] = (retStack, ehs, mkont) =>
            eval(rest, retStack.take(ty.out.size) ++ newStack, frame, kont, mkont, trail, h, ehs)
          // We make a new trail by `restK`, since function creates a new block to escape
          // (more or less like `return`)
          eval(body, List(), newFrame, restK, mkont, List(restK), h, ehs)
        }
      case Import("console", "log", _) =>
        //println(s"[DEBUG] current stack: $stack")
        val I32V(v) :: newStack = stack
        println(v)
        eval(rest, newStack, frame, kont, mkont, trail, h, ehs)
      case Import("spectest", "print_i32", _) =>
        //println(s"[DEBUG] current stack: $stack")
        val I32V(v) :: newStack = stack
        println(v)
        eval(rest, newStack, frame, kont, mkont, trail, h, ehs)
      case Import(_, _, _) => throw new Exception(s"Unknown import at $funcIndex")
      case _               => throw new Exception(s"Definition at $funcIndex is not callable")
    }
  }

  def eval[Ans](insts: List[Instr],
                stack: List[Value],
                frame: Frame,
                kont: Cont[Ans],
                mkont: MCont[Ans],
                trail: List[Cont[Ans]],
                h: Handler[Ans],
                ehs: List[(Int, Handler[Ans])]): Ans = {
    if (insts.isEmpty) return kont(stack, ehs, mkont)

    val inst = insts.head
    val rest = insts.tail

    // println(s"inst: ${inst} \t | ${frame.locals} | ${stack.reverse}" )

    inst match {
      case Drop => eval(rest, stack.tail, frame, kont, mkont, trail, h, ehs)
      case Select(_) =>
        val I32V(cond) :: v2 :: v1 :: newStack = stack
        val value = if (cond == 0) v1 else v2
        eval(rest, value :: newStack, frame, kont, mkont, trail, h, ehs)
      case LocalGet(i) =>
        eval(rest, frame.locals(i) :: stack, frame, kont, mkont, trail, h, ehs)
      case LocalSet(i) =>
        val value :: newStack = stack
        frame.locals(i) = value
        eval(rest, newStack, frame, kont, mkont, trail, h, ehs)
      case LocalTee(i) =>
        val value :: newStack = stack
        frame.locals(i) = value
        eval(rest, stack, frame, kont, mkont, trail, h, ehs)
      case GlobalGet(i) =>
        eval(rest, module.globals(i).value :: stack, frame, kont, mkont, trail, h, ehs)
      case GlobalSet(i) =>
        val value :: newStack = stack
        module.globals(i).ty match {
          case GlobalType(tipe, true) if value.tipe == tipe =>
            module.globals(i).value = value
          case GlobalType(_, true) => throw new Exception("Invalid type")
          case _                   => throw new Exception("Cannot set immutable global")
        }
        eval(rest, newStack, frame, kont, mkont, trail, h, ehs)
      case MemorySize =>
        eval(rest, I32V(module.memory.head.size) :: stack, frame, kont, mkont, trail, h, ehs)
      case MemoryGrow =>
        val I32V(delta) :: newStack = stack
        val mem = module.memory.head
        val oldSize = mem.size
        mem.grow(delta) match {
          case Some(e) =>
            eval(rest, I32V(-1) :: newStack, frame, kont, mkont, trail, h, ehs)
          case _ =>
            eval(rest, I32V(oldSize) :: newStack, frame, kont, mkont, trail, h, ehs)
        }
      case MemoryFill =>
        val I32V(value) :: I32V(offset) :: I32V(size) :: newStack = stack
        if (memOutOfBound(module, 0, offset, size))
          throw new Exception("Out of bounds memory access") // GW: turn this into a `trap`?
        else {
          module.memory.head.fill(offset, size, value.toByte)
          eval(rest, newStack, frame, kont, mkont, trail, h, ehs)
        }
      case MemoryCopy =>
        val I32V(n) :: I32V(src) :: I32V(dest) :: newStack = stack
        if (memOutOfBound(module, 0, src, n) || memOutOfBound(module, 0, dest, n))
          throw new Exception("Out of bounds memory access")
        else {
          module.memory.head.copy(dest, src, n)
          eval(rest, newStack, frame, kont, mkont, trail, h, ehs)
        }
      case Const(n) => eval(rest, n :: stack, frame, kont, mkont, trail, h, ehs)
      case Binary(op) =>
        val v2 :: v1 :: newStack = stack
        eval(rest, evalBinOp(op, v1, v2) :: newStack, frame, kont, mkont, trail, h, ehs)
      case Unary(op) =>
        val v :: newStack = stack
        eval(rest, evalUnaryOp(op, v) :: newStack, frame, kont, mkont, trail, h, ehs)
      case Compare(op) =>
        val v2 :: v1 :: newStack = stack
        eval(rest, evalRelOp(op, v1, v2) :: newStack, frame, kont, mkont, trail, h, ehs)
      case Test(op) =>
        val v :: newStack = stack
        eval(rest, evalTestOp(op, v) :: newStack, frame, kont, mkont, trail, h, ehs)
      case Store(StoreOp(align, offset, ty, None)) =>
        val I32V(v) :: I32V(addr) :: newStack = stack
        module.memory(0).storeInt(addr + offset, v)
        eval(rest, newStack, frame, kont, mkont, trail, h, ehs)
      case Load(LoadOp(align, offset, ty, None, None)) =>
        val I32V(addr) :: newStack = stack
        val value = module.memory(0).loadInt(addr + offset)
        eval(rest, I32V(value) :: newStack, frame, kont, mkont, trail, h, ehs)
      case Nop =>
        eval(rest, stack, frame, kont, mkont, trail, h, ehs)
      case Unreachable => throw Trap()
      case Block(ty, inner) =>
        val funcTy = getFuncType(ty)
        val (inputs, restStack) = stack.splitAt(funcTy.inps.size)
        val restK: Cont[Ans] = (retStack, ehs, mkont) =>
          eval(rest, retStack.take(funcTy.out.size) ++ restStack, frame, kont, mkont, trail, h, ehs)
        eval(inner, inputs, frame, restK, mkont, restK :: trail, h, ehs)
      case Loop(ty, inner) =>
        // We construct two continuations, one for the break (to the begining of the loop),
        // and one for fall-through to the next instruction following the syntactic structure
        // of the program.
        val funcTy = getFuncType(ty)
        val (inputs, restStack) = stack.splitAt(funcTy.inps.size)
        val restK: Cont[Ans] = (retStack, ehs, mkont) =>
          eval(rest, retStack.take(funcTy.out.size) ++ restStack, frame, kont, mkont, trail, h, ehs)
        def loop(retStack: List[Value], ehs: Handlers[Ans], mkont: MCont[Ans]): Ans =
          eval(inner, retStack.take(funcTy.inps.size), frame, restK, mkont, loop _ :: trail, h, ehs)
        loop(inputs, ehs, mkont)
      case If(ty, thn, els) =>
        val funcTy = getFuncType(ty)
        val I32V(cond) :: newStack = stack
        val inner = if (cond != 0) thn else els
        val (inputs, restStack) = newStack.splitAt(funcTy.inps.size)
        val restK: Cont[Ans] = (retStack, ehs, mkont) =>
          eval(rest, retStack.take(funcTy.out.size) ++ restStack, frame, kont, mkont, trail, h, ehs)
        eval(inner, inputs, frame, restK, mkont, restK :: trail, h, ehs)
      case Br(label) =>
        trail(label)(stack, ehs, mkont)
      case BrIf(label) =>
        val I32V(cond) :: newStack = stack
        if (cond != 0) trail(label)(newStack, ehs, mkont)
        else eval(rest, newStack, frame, kont, mkont, trail, h, ehs)
      case BrTable(labels, default) =>
        val I32V(cond) :: newStack = stack
        val goto = if (cond < labels.length) labels(cond) else default
        trail(goto)(newStack, ehs, mkont)
      case Return        => trail.last(stack, ehs, mkont)
      case Call(f)       => evalCall(f, rest, stack, frame, kont, mkont, trail, h, ehs, false)
      case ReturnCall(f) => evalCall(f, rest, stack, frame, kont, mkont, trail, h, ehs, true)
      // XXX (GW): consider implementing call_ref too
      case RefFunc(f) =>
        // TODO: RefFuncV stores an applicable function, instead of a syntactic structure
        eval(rest, RefFuncV(f) :: stack, frame, kont, mkont, trail, h, ehs)
      case ContNew(ty) =>
        val RefFuncV(f) :: newStack = stack
        // create a continuation which only performs a function call
        val idK: Cont[Ans] = (s, ehs, m) => m(s)
        val k: Cont[Ans] = (s, ehs, mk) => evalCall(f, List(), s, frame, idK, mk, trail, h, ehs, false)
        eval(rest, TCContV(k) :: newStack, frame, kont, mkont, trail, h, ehs)
      case Suspend(tag_id) => {
        val FuncType(_, inps, out) = module.tags(tag_id)
        val (input, restStack) = stack.splitAt(inps.size)
        // todo: ensure the specified effect handlers here is correct
        val kr: Cont[Ans] = (s, ehs, mk) => eval(rest, s ++ restStack, frame, kont, mk, trail, h, ehs)
        ehs.find(_._1 == tag_id) match {
          case Some((_, handler)) => handler(TCContV(kr) :: input)
          case None               => 
            throw Trap()
        }
      }
      case Resume(kty_id, handlers) => {
        val (resume: TCContV[Ans]) :: newStack = stack
        val contTy = module.types(kty_id)
        val ContType(funcTypeId) = contTy
        val FuncType(_, inps, out) = module.types(funcTypeId)
        val (inputs, restStack) = newStack.splitAt(inps.size)
        val m: MCont[Ans] = (s) => eval(rest, s ++ restStack, frame, kont, mkont, trail, h, ehs)
        val newEhs: List[(Int, Handler[Ans])] = handlers.map {
          case Handler(tag_id, handler) => (tag_id, (s) => trail(handler)(s, ehs, mkont))
        }
        // resume with new effect handlers installed each time
        resume.k(inputs, newEhs, m)
      }
      case CallRef(ty) =>
        val RefFuncV(f) :: newStack = stack
        evalCall(f, rest, newStack, frame, kont, mkont, trail, h, ehs, false)
      // resumable try-catch exception handling:
      case TryCatch(es1, es2) =>
        val join: MCont[Ans] = (newStack) => eval(rest, stack, frame, kont, mkont, trail, h, ehs)
        val idK: Cont[Ans] = (s, ehs, m) => m(s)
        val newHandler: Handler[Ans] = (newStack) => eval(es2, newStack, frame, idK, join, trail, h, ehs)
        eval(es1, List(), frame, idK, join, trail, newHandler, ehs)
      case Resume0() =>
        val (resume: TCContV[Ans]) :: newStack = stack
        val m: MCont[Ans] = (s) => eval(rest, newStack/*!*/, frame, kont, mkont, trail, h, ehs)
        resume.k(List(), List(), m)
      case Throw() =>
        val err :: newStack = stack
        val kr: Cont[Ans] = (s, ehs, m) => eval(rest, newStack/*!*/, frame, kont, m/*!*/, trail, h, ehs)
        h(List(err, TCContV(kr)))
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
            println(s"Entering function $main")
            module.funcs(fid) match {
              case FuncDef(_, FuncBodyDef(_, _, _, body)) => body
              case _ => throw new Exception("Entry function has no concrete body")
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
    if (instrs.isEmpty) println("Warning: nothing is executed")
    val handler0: Handler[Ans] = stack => throw new Exception(s"Uncaught exception: $stack")
    eval(instrs, List(), Frame(ArrayBuffer(I32V(0))), halt, mhalt, List(halt), handler0, List())
  }

  def evalTop(m: ModuleInstance): Unit = {
    val halt: Cont[Unit] = (stack, ehs, m) => m(stack)
    evalTop(halt, stack => ())
  }
}
