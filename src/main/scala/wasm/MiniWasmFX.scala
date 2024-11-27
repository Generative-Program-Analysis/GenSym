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

  trait ContTrait[A] {
    def apply(stack: Stack, trail1: List[ContTrait[A]], mcont: MCont[A]): A
  }

  type Stack = List[Value]
  type Cont[A] = ContTrait[A]
  type MCont[A] = Stack => A
  type Handler[A] = Stack => A

  case class ContV[A](k: (Stack, Cont[A], List[Cont[A]], MCont[A], Handler[A]) => A) extends Value {
    def tipe(implicit m: ModuleInstance): ValueType = ???
  }

  def init[Ans](s: Stack, trail1: List[Cont[Ans]], mkont: MCont[Ans]): Ans = {
    trail1 match {
      case k1 :: trail1 => k1(s, trail1, mkont)
      case Nil => mkont(s)
    }
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
                    trail1: List[Cont[Ans]],
                    mkont: MCont[Ans],
                    trail2: List[Cont[Ans]],
                    h: Handler[Ans],
                    isTail: Boolean): Ans = {
    module.funcs(funcIndex) match {
      case FuncDef(_, FuncBodyDef(ty, _, locals, body)) =>
        val args = stack.take(ty.inps.size).reverse
        val newStack = stack.drop(ty.inps.size)
        val frameLocals = args ++ locals.map(zero(_))
        val newFrame = Frame(ArrayBuffer(frameLocals: _*))
        if (isTail)
          // when tail call, share the continuation for returning with the callee
          eval(body, List(), newFrame, kont, trail1, mkont, List(kont), h)
        else {
          val restK: Cont[Ans] = (retStack, trail1, mkont) =>
            eval(rest, retStack.take(ty.out.size) ++ newStack, frame, kont, trail1, mkont, trail2, h)
          // We make a new trail2 by `restK`, since function creates a new block to escape
          // (more or less like `return`)
          eval(body, List(), newFrame, restK, trail1, mkont, List(restK), h)
        }
      case Import("console", "log", _) =>
        // println(s"[DEBUG] current stack: $stack")
        val I32V(v) :: newStack = stack
        println(v)
        eval(rest, newStack, frame, kont, trail1, mkont, trail2, h)
      case Import("spectest", "print_i32", _) =>
        // println(s"[DEBUG] current stack: $stack")
        val I32V(v) :: newStack = stack
        println(v)
        eval(rest, newStack, frame, kont, trail1, mkont, trail2, h)
      case Import(_, _, _) => throw new Exception(s"Unknown import at $funcIndex")
      case _               => throw new Exception(s"Definition at $funcIndex is not callable")
    }
  }

  def eval[Ans](insts: List[Instr],
                stack: List[Value],
                frame: Frame,
                kont: Cont[Ans],
                trail1: List[Cont[Ans]],
                mkont: MCont[Ans],
                trail2: List[Cont[Ans]],
                h: Handler[Ans]): Ans = {
    // Note kont is only used in the base case, or when we are appending k and k1
    if (insts.isEmpty) return kont(stack, trail1, mkont)

    val inst = insts.head
    val rest = insts.tail

    // TODO: uncommenting this will fail tests that uses `testFileOutput`
    // println(s"inst: ${inst} \t | ${frame.locals} | ${stack.reverse}" )

    inst match {
      case Drop => eval(rest, stack.tail, frame, kont, trail1, mkont, trail2, h)
      case Select(_) =>
        val I32V(cond) :: v2 :: v1 :: newStack = stack
        val value = if (cond == 0) v1 else v2
        eval(rest, value :: newStack, frame, kont, trail1, mkont, trail2, h)
      case LocalGet(i) =>
        eval(rest, frame.locals(i) :: stack, frame, kont, trail1, mkont, trail2, h)
      case LocalSet(i) =>
        val value :: newStack = stack
        frame.locals(i) = value
        eval(rest, newStack, frame, kont, trail1, mkont, trail2, h)
      case LocalTee(i) =>
        val value :: newStack = stack
        frame.locals(i) = value
        eval(rest, stack, frame, kont, trail1, mkont, trail2, h)
      case GlobalGet(i) =>
        eval(rest, module.globals(i).value :: stack, frame, kont, trail1, mkont, trail2, h)
      case GlobalSet(i) =>
        val value :: newStack = stack
        module.globals(i).ty match {
          case GlobalType(tipe, true) if value.tipe == tipe =>
            module.globals(i).value = value
          case GlobalType(_, true) => throw new Exception("Invalid type")
          case _                   => throw new Exception("Cannot set immutable global")
        }
        eval(rest, newStack, frame, kont, trail1, mkont, trail2, h)
      case MemorySize =>
        eval(rest, I32V(module.memory.head.size) :: stack, frame, kont, trail1, mkont, trail2, h)
      case MemoryGrow =>
        val I32V(delta) :: newStack = stack
        val mem = module.memory.head
        val oldSize = mem.size
        mem.grow(delta) match {
          case Some(e) =>
            eval(rest, I32V(-1) :: newStack, frame, kont, trail1, mkont, trail2, h)
          case _ =>
            eval(rest, I32V(oldSize) :: newStack, frame, kont, trail1, mkont, trail2, h)
        }
      case MemoryFill =>
        val I32V(value) :: I32V(offset) :: I32V(size) :: newStack = stack
        if (memOutOfBound(module, 0, offset, size))
          throw new Exception("Out of bounds memory access") // GW: turn this into a `trap`?
        else {
          module.memory.head.fill(offset, size, value.toByte)
          eval(rest, newStack, frame, kont, trail1, mkont, trail2, h)
        }
      case MemoryCopy =>
        val I32V(n) :: I32V(src) :: I32V(dest) :: newStack = stack
        if (memOutOfBound(module, 0, src, n) || memOutOfBound(module, 0, dest, n))
          throw new Exception("Out of bounds memory access")
        else {
          module.memory.head.copy(dest, src, n)
          eval(rest, newStack, frame, kont, trail1, mkont, trail2, h)
        }
      case Const(n) => eval(rest, n :: stack, frame, kont, trail1, mkont, trail2, h)
      case Binary(op) =>
        val v2 :: v1 :: newStack = stack
        eval(rest, evalBinOp(op, v1, v2) :: newStack, frame, kont, trail1, mkont, trail2, h)
      case Unary(op) =>
        val v :: newStack = stack
        eval(rest, evalUnaryOp(op, v) :: newStack, frame, kont, trail1, mkont, trail2, h)
      case Compare(op) =>
        val v2 :: v1 :: newStack = stack
        eval(rest, evalRelOp(op, v1, v2) :: newStack, frame, kont, trail1, mkont, trail2, h)
      case Test(op) =>
        val v :: newStack = stack
        eval(rest, evalTestOp(op, v) :: newStack, frame, kont, trail1, mkont, trail2, h)
      case Store(StoreOp(align, offset, ty, None)) =>
        val I32V(v) :: I32V(addr) :: newStack = stack
        module.memory(0).storeInt(addr + offset, v)
        eval(rest, newStack, frame, kont, trail1, mkont, trail2, h)
      case Load(LoadOp(align, offset, ty, None, None)) =>
        val I32V(addr) :: newStack = stack
        val value = module.memory(0).loadInt(addr + offset)
        eval(rest, I32V(value) :: newStack, frame, kont, trail1, mkont, trail2, h)
      case Nop =>
        eval(rest, stack, frame, kont, trail1, mkont, trail2, h)
      case Unreachable => throw Trap()
      case Block(ty, inner) =>
        val funcTy = getFuncType(ty)
        val (inputs, restStack) = stack.splitAt(funcTy.inps.size)
        // why a block is introducing a new mknot1
        // I feel like we will almost never need to change the mkont for a block
        val restK: Cont[Ans] = (retStack, trail1, mkont1) => {
          // kont -> mkont -> mkont1
          eval(rest, retStack.take(funcTy.out.size) ++ restStack, frame, kont, trail1, mkont1, trail2, h)
        }
        eval(inner, inputs, frame, restK, trail1, mkont, restK :: trail2, h)
      case Loop(ty, inner) =>
        // We construct two continuations, one for the break (to the begining of the loop),
        // and one for fall-through to the next instruction following the syntactic structure
        // of the program.
        val funcTy = getFuncType(ty)
        val (inputs, restStack) = stack.splitAt(funcTy.inps.size)
        val restK: Cont[Ans] = (retStack, trail1, mkont) =>
          eval(rest, retStack.take(funcTy.out.size) ++ restStack, frame, kont, trail1, mkont, trail2, h)
        def loop(retStack: List[Value], trail1: List[Cont[Ans]], mkont: MCont[Ans]): Ans =
          eval(inner, retStack.take(funcTy.inps.size), frame, restK, trail1, mkont, (loop _ : Cont[Ans]):: trail2, h)
        // for example, loop here doesn't change the meta-continuation at all
        // compare with block
        loop(inputs, trail1, mkont)
      case If(ty, thn, els) =>
        val funcTy = getFuncType(ty)
        val I32V(cond) :: newStack = stack
        val inner = if (cond != 0) thn else els
        val (inputs, restStack) = newStack.splitAt(funcTy.inps.size)
        val restK: Cont[Ans] = (retStack, trail1, mkont) =>
          eval(rest, retStack.take(funcTy.out.size) ++ restStack, frame, kont, trail1, mkont, trail2, h)
        eval(inner, inputs, frame, restK, trail1, mkont, restK :: trail2, h)
      case Br(label) =>
        trail2(label)(stack, trail1, mkont) // s => ().asInstanceOf[Ans]) //mkont)
      case BrIf(label) =>
        val I32V(cond) :: newStack = stack
        if (cond != 0) trail2(label)(newStack, trail1, mkont)
        else eval(rest, newStack, frame, kont, trail1, mkont, trail2, h)
      case BrTable(labels, default) =>
        val I32V(cond) :: newStack = stack
        val goto = if (cond < labels.length) labels(cond) else default
        trail2(goto)(newStack, trail1, mkont)
      case Return        => trail2.last(stack, trail1, mkont)
      case Call(f)       => evalCall(f, rest, stack, frame, kont, trail1, mkont, trail2, h, false)
      case ReturnCall(f) => evalCall(f, rest, stack, frame, kont, trail1, mkont, trail2, h, true)
      case RefFunc(f)    =>
        // TODO: RefFuncV stores an applicable function, instead of a syntactic structure
        eval(rest, RefFuncV(f) :: stack, frame, kont, trail1, mkont, trail2, h)
      case CallRef(ty) =>
        val RefFuncV(f) :: newStack = stack
        evalCall(f, rest, newStack, frame, kont, trail1, mkont, trail2, h, false)

      // resumable try-catch exception handling:
      case TryCatch(es1, es2) =>
        // push trail1 to join point
        val join: MCont[Ans] = (newStack) => eval(rest, stack, frame, kont, trail1, mkont, trail2, h)
        // the `restK` for catch block (es2) is the join point
        // the restK simply applies the meta-continuation, this is the same the [nil] case
        // where we fall back to join point
        // val idK: Cont[Ans] = (s, m) => m(s)
        val newHandler: Handler[Ans] = (newStack) => eval(es2, newStack, frame, init: Cont[Ans], List(), join, trail2, h)
        eval(es1, List(), frame, init: Cont[Ans], List(), join, trail2, newHandler)
      case Resume0() =>
        val (resume: TCContV[Ans]) :: newStack = stack
        val k: Cont[Ans] = (s, trail1, m) => eval(rest, newStack /*!*/, frame, kont, trail1, m, trail2, h)
        resume.k(List(), k, trail1, mkont)
      case Throw() =>
        val err :: newStack = stack
        // kont composed with k1 and trail1
        // note that kr doesn't use the stack at all
        // it only takes the err value
        def kr(s: Stack, k1: Cont[Ans], newTrail1: List[Cont[Ans]], m: MCont[Ans]): Ans = {
          eval(rest, newStack /*!*/, frame, kont, trail1 ++ List(k1) ++ newTrail1, m /*vs mkont?*/, trail2, h)
        }
        h(List(err, TCContV(kr)))

      // WasmFX effect handlers:
      case ContNew(ty) =>
        val RefFuncV(f) :: newStack = stack

        def kr(s: Stack, k1: Cont[Ans], trail1: List[Cont[Ans]], mk: MCont[Ans], handler: Handler[Ans]): Ans = {
          // k1 is rest for `resume`
          // mk holds the handler for `suspend`

          // Q: Do we need to care about kont here??
          // Answer: we don't, because continuation is only activated by `resume`
          // and it must be handled by the default handler (k1) or the handler associated
          // to `suspend`

          // we can discard trail2 since it is a new function call (similar to `kont`)
          evalCall(f, List(), s, frame, init: Cont[Ans], List(), mk, List(), handler, false)
        }

        eval(rest, ContV(kr) :: newStack, frame, kont, trail1, mkont, trail2, h)

      case Suspend(tag_id) => {
        // println(s"${RED}Unimplimented Suspending tag $tag_id")
        // add the continuation on the stack

        val k = (s: Stack, k1: Cont[Ans], trail1: List[Cont[Ans]], m: MCont[Ans], handler: Handler[Ans]) => {
          // TODO: does the following work?
          // val kontK: Cont[Ans] = (s1, m1) => kont(s1, s2 => k1(s2, m1))

          // Q: is it okay to forget `k1` and `mkont` here?
          // Ans: No! Because the resumable continuation might be install by
          // a different `resume` with a different set of handlers
          val newMk: MCont[Ans] = (s) => k1(s, trail1, m)
          eval(rest, s, frame, kont, trail1, newMk, trail2, handler)
        }
        val newStack = ContV(k) :: stack
        h(newStack)
      }

      // TODO: resume should create a list of handlers to capture suspend
      case Resume(kty_id, handler) => {
        val (f: ContV[Ans]) :: newStack = stack
        val contTy = module.types(kty_id)
        val ContType(funcTypeId) = contTy
        val FuncType(_, inps, out) = module.types(funcTypeId)
        val (inputs, restStack) = newStack.splitAt(inps.size)

        if (handler.length == 0) {
          // the metacontinuation contains the default handler
          val mk: MCont[Ans] = (s) => eval(rest, s, frame, kont, trail1, mkont, trail2, h)
          f.k(inputs, init, List(), mk, h)
        } else {
          if (handler.length > 1) {
            throw new Exception("only single tag is supported")
          }
          // if suspend happens, the label id holds the handler
          val Handler(tagId, labelId) = handler.head

          val newHandler: Handler[Ans] = (newStack) => trail2(labelId)(newStack, List(), mkont)

          // f might be handled by the default handler (namely kont), or by the
          // handler specified by tags (newhandler, which has the same type as meta-continuation)
          val mk: MCont[Ans] = (s) => eval(rest, s, frame, kont, trail1, mkont, trail2, h)
          f.k(inputs, init, List(), mk, newHandler)

        }

      }

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
        def kr(s: Stack, k1: Cont[Ans], trail1: List[Cont[Ans]], mk: MCont[Ans], handler: Handler[Ans]): Ans = {
          f.k(s ++ inputs, k1, trail1, mk, handler)
        }

        eval(rest, ContV(kr) :: restStack, frame, kont, trail1, mkont, trail2, h)

      case CallRef(ty) =>
        val RefFuncV(f) :: newStack = stack
        evalCall(f, rest, newStack, frame, kont, trail1, mkont, trail2, h, false)

      case CallRef(ty) =>
        val RefFuncV(f) :: newStack = stack
        evalCall(f, rest, newStack, frame, kont, trail1, mkont, trail2, h, false)

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
    val handler0: Handler[Ans] = stack => throw new Exception(s"Uncaught exception: $stack")
    // initialized locals
    val frame = Frame(ArrayBuffer(locals.map(zero(_)): _*))
    eval(instrs, List(), frame, halt, List(), mhalt, List(halt), handler0)
  }

  def evalTop(m: ModuleInstance): Unit = {
    val halt: Cont[Unit] = init
    evalTop(halt, stack => ())
  }
}
