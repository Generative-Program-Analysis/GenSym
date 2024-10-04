package gensym.wasm.newstagedevalcps

import scala.collection.mutable.HashMap

import gensym.wasm.ast.{Const => Konst, _}
//import gensym.wasm.values.{I32 => I32C}
//import gensym.wasm.types._
import gensym.wasm.memory._
//import gensym.wasm.globals._

import lms.core._
import lms.core.stub._
import lms.macros.SourceContext
import lms.core.virtualize
import gensym.lmsx._

case class ModuleInstance(types: List[FuncType], funcs: List[FuncDef])

object Preprocess {
  object BlockIds {
    var id: Int = 0
    def next: Int = {
      id += 1
      id
    }
  }

  def idBlocks(instrs: List[Instr]): List[Instr] = instrs.map {
    case Block(ty, instrs) =>
      IdBlock(BlockIds.next, ty, idBlocks(instrs.toList))
    case Loop(ty, instrs) => IdLoop(BlockIds.next, ty, idBlocks(instrs.toList))
    case If(ty, thenInstrs, elseInstrs) =>
      val thenBlock = IdBlock(BlockIds.next, ty, idBlocks(thenInstrs))
      val elseBlock = IdBlock(BlockIds.next, ty, idBlocks(elseInstrs))
      IdIf(ty, thenBlock, elseBlock)
    case instr => instr
  }
}

@virtualize
trait StagedEvalCPS extends SAIOps {
  trait State
  object State {
    def pushStack(value: Rep[Value]) =
      Adapter.g.reflectWrite("static-state-push-stack", Unwrap(value))(
        Adapter.CTRL)
    def stackAt(i: Int): Rep[Value] =
      Wrap[Value](
        Adapter.g.reflectWrite("static-state-stack-at", Unwrap(i))(
          Adapter.CTRL))
    def popStack: Rep[Value] =
      Wrap[Value](
        Adapter.g.reflectWrite("static-state-pop-stack")(Adapter.CTRL))
    def peekStack: Rep[Value] =
      Wrap[Value](
        Adapter.g.reflectWrite("static-state-peek-stack")(Adapter.CTRL))
    def returnFromFun(numLocals: Int, numReturns: Int): Rep[Unit] =
      Wrap[Unit](
        Adapter.g.reflectWrite("static-state-return",
                               Unwrap(numLocals),
                               Unwrap(numReturns))(Adapter.CTRL))
    def bumpFramePtr(): Rep[Unit] =
      Wrap[Unit](
        Adapter.g.reflectWrite("static-state-bump-frame-ptr")(Adapter.CTRL))
    def getFramePtr: Rep[Int] =
      Wrap[Int](
        Adapter.g.reflectWrite("static-state-get-frame-ptr")(Adapter.CTRL))
    def setFramePtr(fp: Rep[Int]): Rep[Unit] =
      Wrap[Unit](
        Adapter.g.reflectWrite("static-state-set-frame-ptr", Unwrap(fp))(
          Adapter.CTRL))
    def reverseTopN(n: Int): Rep[Unit] =
      Wrap[Unit](
        Adapter.g.reflectWrite("static-state-reverse-top-n", Unwrap(n))(
          Adapter.CTRL))
    def removeStackRange(start: Rep[Int], end: Rep[Int]): Rep[Unit] =
      Wrap[Unit](
        Adapter.g.reflectWrite("static-state-remove-stack-range",
                               Unwrap(start),
                               Unwrap(end))(Adapter.CTRL))
    def getLocal(i: Rep[Int])(implicit ss: StaticState): Rep[Value] =
      Wrap[Value](
        Adapter.g.reflectWrite("static-state-get-local",
                               Unwrap(-ss.numLocals + i))(Adapter.CTRL)
      )
    def setLocal(i: Rep[Int], v: Rep[Value])(implicit ss: StaticState) =
      Adapter.g.reflectWrite("static-state-set-local",
                             Unwrap(-ss.numLocals + i),
                             Unwrap(v))(Adapter.CTRL)

    def printStack =
      Adapter.g.reflectWrite("static-state-print-stack")(Adapter.CTRL)

    def getGlobal(i: Int): Rep[Value] =
      Wrap[Value](
        Adapter.g.reflectWrite("static-state-get-global", Unwrap(i))(
          Adapter.CTRL))
    def setGlobal(i: Int, v: Rep[Value]) =
      Adapter.g.reflectWrite("static-state-set-global", Unwrap(i), Unwrap(v))(
        Adapter.CTRL)

    def memorySize: Rep[Int] =
      Wrap[Int](Adapter.g.reflect("static-state-memory-size"))
    def memoryGrow(n: Rep[Int]): Rep[Int] =
      Wrap[Int](Adapter.g.reflect("static-state-memory-grow", Unwrap(n)))
    def memoryFill(offset: Rep[Int], size: Rep[Int], value: Rep[Int]) =
      Adapter.g.reflectWrite("static-state-memory-fill",
                             Unwrap(offset),
                             Unwrap(size),
                             Unwrap(value))(Adapter.CTRL)
    def memoryCopy(srcOffset: Rep[Int], dstOffset: Rep[Int], size: Rep[Int]) =
      Adapter.g.reflectWrite("static-state-memory-copy",
                             Unwrap(srcOffset),
                             Unwrap(dstOffset),
                             Unwrap(size))(Adapter.CTRL)
  }

  def initState(memory: Rep[List[Memory]],
                globals: Rep[List[RTGlobal]],
                numLocals: Int): Rep[State] =
    Wrap[State](
      Adapter.g.reflectWrite("state-init",
                             Unwrap(memory),
                             Unwrap(globals),
                             Unwrap(numLocals))(Adapter.CTRL))

  def panic(msg: Rep[String]): Rep[Unit] =
    Wrap[Unit](Adapter.g.reflectWrite("panic", Unwrap(msg))(Adapter.CTRL))

  def reverse[M: Manifest](ls: Rep[List[M]]): Rep[List[M]] =
    Wrap[List[M]](Adapter.g.reflect("reverse-ls", Unwrap(ls)))

  trait Value
  // def I32(i: Rep[Int]): Rep[Value] = "I32V".reflectWith[Value](i)
  // def I64(i: Rep[Long]): Rep[Value] = "I64V".reflectWith[Value](i)
  def I32(i: Rep[Int]): Rep[Value] =
    Wrap[Value](Adapter.g.reflectWrite("I32V", Unwrap(i))(Adapter.CTRL))
  def I64(i: Rep[Long]): Rep[Value] =
    Wrap[Value](Adapter.g.reflectWrite("I64V", Unwrap(i))(Adapter.CTRL))

  type Cont = Unit => Unit
  type SSCont = StaticState => Rep[Cont]

  implicit def repI32Proj(i: Rep[Value]): Rep[Int] = Unwrap(i) match {
    case Adapter.g
          .Def("I32V", scala.collection.immutable.List(v: Backend.Exp)) =>
      Wrap[Int](v)
    case _ =>
      Wrap[Int](Adapter.g.reflect("I32V-proj", Unwrap(i)))
  }

  trait Label {
    def apply(ss: StaticState): Rep[Unit]
  }
  case class ContLabel(k: SSCont) extends Label {
    def apply(ss: StaticState): Rep[Unit] = k(ss)(())
  }
  case class StringLabel(name: String) extends Label {
    def apply(ss: StaticState): Rep[Unit] =
      Wrap[Unit](
        Adapter.g.reflectWrite("goto-label", Unwrap(name))(Adapter.CTRL))
  }

  case class StaticState(
      labels: List[Label],
      returnLabel: Option[Rep[Cont]],
      frameStackPtr: Int,
      numLocals: Int,
  )

  var funFunsGlobal: HashMap[String, Backend.Sym] = HashMap.empty
  var funRetConts: Set[String] = scala.collection.immutable.Set.empty

  def callFunRetCont(name: String): Rep[Unit] =
    Wrap[Unit](
      Adapter.g.reflectWrite(s"call-fun-ret-cont", Unwrap(name))(Adapter.CTRL))
  def getFunRetCont(name: String): Rep[Cont] =
    Wrap[Cont](Adapter.g.reflect(s"get-fun-ret-cont", Unwrap(name)))
  def setFunRetCont(name: String, cont: Rep[Cont]) = {
    funRetConts += name
    Adapter.g.reflectWrite("set-fun-ret-cont", Unwrap(name), Unwrap(cont))(
      Adapter.CTRL)
  }
  def pushFunRetCont(cont: Rep[Cont]) = {
    Adapter.g.reflectWrite("push-fun-ret-cont", Unwrap(cont))(Adapter.CTRL)
  }
  def popFunRetCont(): Rep[Unit] = {
    Wrap[Unit](Adapter.g.reflectWrite("pop-fun-ret-cont")(Adapter.CTRL))
  }
  def addLabel(name: String) = {
    Adapter.g.reflectWrite("add-label", Unwrap(name))(Adapter.CTRL)
  }

  def callFunFun(funName: String, k: Rep[Cont]): Rep[Unit] =
    Wrap[Unit](
      Adapter.g.reflectWrite(s"call-fun-fun", Unwrap(funName), Unwrap(k))(
        Adapter.CTRL))

  case class Config(
      module: ModuleInstance,
      funFuns: HashMap[String, Rep[Cont => Unit]],
      blockConts: HashMap[Int, SSCont],
      loopFuns: HashMap[Int, Rep[Cont]],
      stackBudget: Int
  ) {
    // TODO: pass precise type to operations
    def evalBinOp(op: BinOp, lhsV: Rep[Value], rhsV: Rep[Value]): Rep[Value] =
      op match {
        case Add(_) => {
          // assume I32
          val (lhs, rhs) = (repI32Proj(lhsV), repI32Proj(rhsV))
          I32(lhs + rhs)
        }
        case Sub(_) => {
          val (lhs, rhs) = (repI32Proj(lhsV), repI32Proj(rhsV))
          I32(lhs - rhs)
        }
        case Mul(_) => {
          val (lhs, rhs) = (repI32Proj(lhsV), repI32Proj(rhsV))
          I32(lhs * rhs)
        }
      }
    def evalUnaryOp(op: UnaryOp, value: Rep[Value]): Rep[Value] = ???
    def evalRelOp(op: RelOp, lhs: Rep[Value], rhs: Rep[Value]): Rep[Value] = ???
    def evalTestOp(op: TestOp, value: Rep[Value]): Rep[Value] = op match {
      case Eqz(_) => {
        val v: Rep[Int] = repI32Proj(value)
        val zero: Rep[Int] = 0
        if (v == zero) I32(1) else I32(0)
      }
    }

    def compileStuffIn(instrs: List[Instr], k: SSCont)(
        implicit ss: StaticState): Unit = {
      if (instrs != Nil)
        System.out.println(
          s"Compiling ${instrs.head}, ${instrs.tail.length} more")
      instrs match {
        case Nil => ()
        case IdBlock(id, blockTy, blockInstrs) :: rest => {
          compileBlock(id, blockInstrs.toList, rest, k)
          // GW: should recur on compileStuffIn?
        }
        case IdLoop(id, blockTy, loopInstrs) :: rest => {
          compileLoop(id, loopInstrs.toList, rest, k)
          // GW: should recur on compileStuffIn?
        }
        case _ :: rest => compileStuffIn(rest, k)
      }
    }

    def compileBlock(id: Int,
                     body: List[Instr],
                     nextInstrs: List[Instr],
                     k: SSCont)(implicit ss: StaticState): Unit = {
      if (blockConts.contains(id)) return
      System.out.println(s"Compiling block $id, ${nextInstrs.length} after")
      val blockK = (nextSS: StaticState) => {
        compileStuffIn(nextInstrs, k)(nextSS.copy(labels = ss.labels))
        // GW: topFun generated but not used?
        topFun { (_: Rep[Unit]) =>
          System.out.println(s"started topFun for compileBlock $id")
          execInstrs(nextInstrs, k)(nextSS.copy(labels = ss.labels))
          System.out.println(s"finished topFun for compileBlock $id")
        }
      }
      compileStuffIn(body, blockK)(
        ss.copy(labels = ContLabel(blockK) :: ss.labels))
      blockConts += (id -> blockK)
      System.out.println(s"Finished compiling block $id")
    }

    def compileLoop(id: Int,
                    body: List[Instr],
                    nextInstrs: List[Instr],
                    k: SSCont)(implicit ss: StaticState): Unit = {
      if (loopFuns.contains(id)) return
      System.out.println(s"Compiling loop $id")
      // compileBlock(id, body, nextInstrs, _ => loopFn)
      val blockK = (nextSS: StaticState) => {
        compileStuffIn(nextInstrs, k)(nextSS.copy(labels = ss.labels))
        topFun { (_: Rep[Unit]) =>
          execInstrs(nextInstrs, k)(nextSS.copy(labels = ss.labels))
        }
      }
      blockConts(id) = blockK // StaticState => Rep[Unit => Unit]
      compileStuffIn(body, _ => loopFn)(
        ss.copy(labels = ContLabel((_: StaticState) => loopFn) :: ss.labels))
      def loopFn: Rep[Cont] = topFun { (_: Rep[Unit]) =>
        addLabel(s"loop$id")
        System.out.println(s"started topFun for loop $id")
        // discard ss on each iteration
        val label = StringLabel(s"loop$id") // -> break 0
        execInstrs(body, blockConts(id))(
          ss.copy(StringLabel(s"loop$id") :: ss.labels))
      // System.out.println(s"finished topFun for loop $id")
      }
      loopFuns += (id -> loopFn)
    }

    def compileFun(name: String,
                   funcType: FuncType,
                   locals: List[ValueType],
                   body: List[Instr]): Unit = {
      if (funFuns.contains(name)) return

      // basically calling k1
      val retCont = topFun { (_: Rep[Unit]) =>
        // print(s"Returning: inps: ${funcType.inps.length}, outs: ${funcType.out.length} \t\t"); State.printStack
        State.returnFromFun(funcType.inps.length + locals.length,
                            funcType.out.length)
        popFunRetCont()
        // callFunRetCont(name)
        // k1(())
      }
      val innerSS =
        StaticState(Nil, Some(retCont), 0, funcType.inps.length + locals.length)
      compileStuffIn(body, (_: StaticState) => retCont)(innerSS)
      val funFun = topFun { (k1: Rep[Cont]) =>
        System.out.println(s"started topFun for fun $name")
        // TODO: make some sort of compile time map of retCont and then
        // use LMS reflection to substitute in a direct identifier and static assignments
        // funRetConts(name) = retCont
        // setFunRetCont(name, k1)
        pushFunRetCont(k1)

        // State.reverseTopN(funcType.inps.length)
        val range: Range = 0 until locals.length
        for (i <- range) State.pushStack(I32(0))
        State.bumpFramePtr

        // discard the resulting static state
        execInstrs(body.toList, (_: StaticState) => retCont)(
          innerSS.copy(returnLabel = Some(retCont)))
        System.out.println(s"finished topFun for fun $name")
      }

      val node = Unwrap(funFun).asInstanceOf[Backend.Sym]
      Adapter.g.reflectWrite("register-fun", Unwrap(funFun))(Adapter.CTRL)
      funNameMap += (node -> name)
      funFunsGlobal += (name -> node)
      funFuns += (name -> funFun)
    }

    def execInstr(instr: Instr, k: (StaticState, SSCont) => Rep[Unit])(
        kk: SSCont)(implicit ss: StaticState): Rep[Unit] = {
      // print(s"instr: $instr, numLocals: ${ss.numLocals} \t\t"); State.printStack
      instr match {
        // Parametric Instructions
        case Drop => k(ss, kk)
        case Select(_) => {
          val (cond, v2, v1) = (State.popStack, State.popStack, State.popStack)
          val res = if (cond == I32(0)) v2 else v1
          State.pushStack(res)
          k(ss, kk)
        }

        // Variable Instructions
        // https://www.w3.org/TR/wasm-core-2/exec/instructions.html#variable-instructions
        case LocalGet(local) => {
          val v = State.getLocal(local)
          State.pushStack(v)
          k(ss, kk)
        }
        case LocalSet(local) => {
          val v = State.popStack
          State.setLocal(local, v)
          k(ss, kk)
        }
        case LocalTee(local) => {
          val v = State.peekStack
          State.setLocal(local, v)
          k(ss, kk)
        }
        case GlobalGet(global) => {
          val v = State.getGlobal(global)
          State.pushStack(v)
          k(ss, kk)
        }
        case GlobalSet(global) => {
          val v = State.popStack
          State.setGlobal(global, v)
          k(ss, kk)
        }

        // Numeric Instructions
        case gensym.wasm.ast.Const(I32V(n)) => {
          State.pushStack(I32(n))
          k(ss, kk)
        }
        case Binary(op) => {
          val (v2, v1) = (State.popStack, State.popStack)
          val res = evalBinOp(op, v1, v2)
          State.pushStack(res)
          k(ss, kk)
        }
        case Unary(op) => {
          val v = State.popStack
          val res = evalUnaryOp(op, v)
          State.pushStack(res)
          k(ss, kk)
        }
        case Test(testOp) => {
          val v = State.popStack
          State.pushStack(evalTestOp(testOp, v))
          k(ss, kk)
        }
        case Compare(op) => {
          val (v2, v1) = (State.popStack, State.popStack)
          val res = evalRelOp(op, v1, v2)
          State.pushStack(res)
          k(ss, kk)
        }

        // Control Instructions
        // https://www.w3.org/TR/wasm-core-2/exec/instructions.html#numeric-instructions
        case Nop         => k(ss, kk)
        case Unreachable => panic("unreachable")

        case Return => {
          // State.returnFromFun
          // State.printStack
          ss.returnLabel match {
            case Some(cont) => cont(())
            case None       => throw new Exception("return outside function")
          }
        }
        case Br(label) => {
          val cont = ss.labels(label)
          cont(ss)
        }
        case BrIf(label) => {
          val cond: Rep[Int] = State.popStack
          val zero: Rep[Int] = 0
          if (cond == zero) {
            k(ss, kk)
          } else {
            val cont = ss.labels(label)
            cont(ss)
          }
        }
      }
    }

    // Note: k could be eliminated? using returnLabel
    def execInstrs(instrs: List[Instr], k: SSCont)(
        implicit ss: StaticState): Rep[Unit] = instrs match {
      case Nil              => k(ss)(())
      case Block(_, _) :: _ => ??? // should be IdBlock
      case IdBlock(id, blockTy, blockInstrs) :: rest => {
        // compileBlock(id, blockInstrs.toList, rest, k)
        val blockK = blockConts(id)
        execInstrs(blockInstrs.toList, blockK)(
          ss.copy(labels = ContLabel(blockK) :: ss.labels))
      }
      case IdLoop(id, blockTy, loopInstrs) :: rest => {
        // compileLoop(id, loopInstrs.toList, rest, k)
        val loopFn = loopFuns(id)
        loopFn(())
      }
      case Call(func) :: rest => {
        // print(s"instr: Call($func), numLocals: ${ss.numLocals} \t\t"); State.printStack
        val FuncDef(name, FuncBodyDef(funcType, _, locals, body)) =
          module.funcs(func)

        // compileFun(name, funcType, locals.toList, body.toList)
        // val funFun = funFuns(name)
        val oldFramePtr = State.getFramePtr
        val execRest = fun { (_: Rep[Unit]) =>
          State.setFramePtr(oldFramePtr)
          execInstrs(rest, k)(ss)
        }
        // XXX: name.get may fail
        callFunFun(name.get, execRest)
        // funFun(execRest)
      }
      case instr :: rest =>
        execInstr(instr, (nextSS, k1) => execInstrs(rest, k1)(nextSS))(k)
    }
  }
}

trait CppStagedWasmGen extends CppSAICodeGenBase {
  import lms.core.Backend._
  registerHeader("./headers", "<wasm_state_continue.hpp>")

  override def shallow(n: Node): Unit = n match {
    case Node(s, "register-fun", _, _) => ()
    case Node(s, "call-fun-fun", List(name, k), _) => {
      emit(s"fun_fun_${name.toString.drop(1)}("); shallow(k); emit(")")
    }
    case Node(s, "call-fun-ret-cont", List(name), _) => {
      emit(s"fun_ret_cont_${name.toString.drop(1)}(std::monostate{})")
    }
    case Node(s, "get-fun-ret-cont", List(name), _) => {
      emit(s"fun_ret_cont_${name.toString.drop(1)}")
    }
    case Node(s, "set-fun-ret-cont", List(name, cont), _) => {
      emit(s"fun_ret_cont_${name.toString.drop(1)} = "); shallow(cont);
      emit(";")
    }
    case Node(s, "push-fun-ret-cont", List(cont), _) => {
      emit("push_fun_ret_cont_stack("); shallow(cont); emit(");")
    }
    case Node(s, "pop-fun-ret-cont", List(), _) => {
      emit("pop_fun_ret_cont_stack()")
    }
    case Node(s, "add-label", List(name), _) => {
      emit(s"$name:\n")
    }
    case Node(s, "goto-label", List(name), _) => {
      emit(s"goto $name; std::monostate{}")
    }
    case Node(s, "reverse-ls", List(ls), _) =>
      emit("flex_vector_reverse("); shallow(ls); emit(")")
    case Node(s, "I32V", List(i), _)      => emit("I32V("); shallow(i); emit(")")
    case Node(s, "I32V-proj", List(i), _) => shallow(i); emit(".i32")
    case Node(s, "state-new", List(memory, globals, stack), _) =>
      emit("State(");
      shallow(memory); emit(", "); shallow(globals); emit(", "); shallow(stack);
      emit(")")
    case Node(s, "state-init", List(memory, globals, numLocals), _) =>
      emit("init_state(");
      shallow(memory); emit(", "); shallow(globals); emit(", ");
      shallow(numLocals);
      emit(")")
    case Node(s, "state-memory", List(state), _) =>
      shallow(state); emit(".memory")
    case Node(s, "state-globals", List(state), _) =>
      shallow(state); emit(".globals")
    case Node(s, "state-stack", List(state), _) =>
      shallow(state); emit(".stack")
    case Node(s, "static-state-stack-at", List(i), _) =>
      emit("global_state.stack_at("); shallow(i); emit(")")
    case Node(s, "static-state-push-stack", List(v), _) =>
      emit("global_state.push_stack("); shallow(v); emit(")")
    case Node(s, "static-state-pop-stack", List(), _) =>
      emit("global_state.pop_stack()")
    case Node(s, "static-state-peek-stack", List(), _) =>
      emit("global_state.peek_stack()")
    case Node(s, "static-state-print-stack", List(), _) =>
      emit("global_state.print_stack()")
    case Node(s, "static-state-get-local", List(i), _) =>
      emit("global_state.get_local("); shallow(i); emit(")")
    case Node(s, "static-state-set-local", List(i, v), _) =>
      emit("global_state.set_local("); shallow(i); emit(", "); shallow(v);
      emit(")")
    case Node(s, "static-state-remove-stack-range", List(st, ed), _) =>
      emit("global_state.remove_stack_range("); shallow(st); emit(", ");
      shallow(ed); emit(")")
    case Node(s, "static-state-reverse-top-n", List(n), _) =>
      emit("global_state.reverse_top_n("); shallow(n); emit(")")
    case Node(s, "static-state-return", List(numLocals, retN), _) =>
      emit("global_state.return_from_fun("); shallow(numLocals); emit(", ");
      shallow(retN); emit(")")
    case Node(s, "static-state-bump-frame-ptr", List(), _) =>
      emit("global_state.bump_frame_ptr()")
    case Node(s, "static-state-get-frame-ptr", List(), _) =>
      emit("global_state.get_frame_ptr()")
    case Node(s, "static-state-set-frame-ptr", List(fp), _) =>
      emit("global_state.set_frame_ptr("); shallow(fp); emit(")")
    case Node(s, "memory-size", List(memory), _) =>
      shallow(memory); emit(".size()")
    case Node(s, "memory-grow", List(memory, delta), _) =>
      shallow(memory); emit(".grow("); shallow(delta); emit(")")
    case Node(s, "memory-fill", List(memory, offset, size, value), _) =>
      shallow(memory); emit(".fill("); shallow(offset); emit(", ");
      shallow(size); emit(", "); shallow(value); emit(")")
    case Node(s, "memory-copy", List(memory, srcOffset, dstOffset, size), _) =>
      shallow(memory); emit(".copy(");
      shallow(srcOffset); emit(", "); shallow(dstOffset); emit(", ");
      shallow(size); emit(")")
    case Node(s, "memory-store-int", List(memory, offset, value), _) =>
      shallow(memory); emit(".storeInt("); shallow(offset); emit(", ");
      shallow(value); emit(")")
    case Node(s, "memory-load-int", List(memory, offset), _) =>
      shallow(memory); emit(".loadInt("); shallow(offset); emit(")")
    case _ => super.shallow(n)
  }
}

trait CppStagedWasmDriver[A, B] extends CppSAIDriver[A, B] with StagedEvalCPS {
  q =>
  override val codegen = new CGenBase with CppStagedWasmGen {
    val IR: q.type = q
    import IR._
    override def remap(m: Manifest[_]): String = {
      if (m.toString.endsWith("$Value")) "Value"
      else if (m.toString.endsWith("$State")) "State"
      else if (m.toString.endsWith("Memory")) "Mem"
      else if (m.toString.endsWith("Global")) "Global"
      else if (m.toString.endsWith("$EvalResult")) "EvalResult"
      else super.remap(m)
    }

    override def emitAll(g: Graph, name: String)(m1: Manifest[_],
                                                 m2: Manifest[_]): Unit = {
      val ng = init(g)
      val efs = ""
      val stt = dce.statics.toList.map(quoteStatic).mkString(", ")

      emitln("""
      #include <sys/resource.h>

      |/*****************************************
      |Emitting Generated Code
      |*******************************************/
      """.stripMargin)

      val src = run(name, ng)
      emitHeaders(stream)
      //emitln("using namespace immer;")
      for (f <- q.funRetConts) {
        emitln(
          s"std::function<std::monostate (std::monostate)> fun_ret_cont_${f.drop(1)};");
      }
      emitFunctionDecls(stream)
      for ((n, f) <- q.funFunsGlobal) {
        val innerType = "std::function<std::monostate (std::monostate)>"
        emit(
          s"std::function<std::monostate ($innerType)> fun_fun_${n.drop(1)}");
        emit("="); shallow(f); emitln(";")
      }
      emitDatastructures(stream)
      emitFunctions(stream)
      emitInit(stream)

      emitln(s"\n/**************** $name ****************/")
      emit(src)
      emitln("""
      |/*****************************************
      |End of Generated Code
      |*******************************************/
      |int main(int argc, char *argv[]) {
      |  struct rlimit rlim;
      |  getrlimit(RLIMIT_STACK, &rlim);
      |  rlim.rlim_cur = 1024L * 1024L * 1024L * 1024L;
      |  setrlimit(RLIMIT_STACK, &rlim);
      |  //initRand();
      |  if (argc != 2) {
      |    printf("usage: %s <arg>\n", argv[0]);
      |    return 0;
      |  }""".stripMargin)
      if (initStream.size > 0)
        emitln("if (init()) return 0;")
      emitln(s"""
      |  // TODO: what is the right way to pass arguments?
      |  $name(${convert("argv[1]", m1)});
      |  return 0;
      |}""".stripMargin)
    }
  }
}

object StagedEvalCPSTest extends App {
  @virtualize
  def mkVMSnippet(
      module: ModuleInstance,
      instrs: List[Instr],
  ): CppSAIDriver[Int, Unit] with StagedEvalCPS = {
    new CppStagedWasmDriver[Int, Unit] with StagedEvalCPS {
      def snippet(arg: Rep[Int]): Rep[Unit] = {
        // val state = State(List[Memory](), List[Global](), List[Value](I32(0)))
        val config = Config(module, HashMap(), HashMap(), HashMap(), 1000)
        val fin = (ss: StaticState) =>
          topFun { (_: Rep[Unit]) =>
            println(ss.numLocals); State.printStack; ()
        }
        initState(List[Memory](), List[RTGlobal](), 0)
        val ss = StaticState(Nil, None, 0, 0)
        for (FuncDef(name, FuncBodyDef(tipe, _, locals, body)) <- module.funcs) {
          config.compileFun(name.get, tipe, locals, body)
        }
        //config.compileStuffIn(instrs, fin)(ss) // not necessary
        System.out.println(s"blockConts: ${config.blockConts}")
        config.execInstrs(instrs, fin)(ss)
      }
    }
  }

  val module = {
    val file = scala.io.Source.fromFile("./benchmarks/wasm/test_rs.wat").mkString
    gensym.wasm.parser.Parser.parse(file)
  }

  val moduleInst = {
    val types = List()
    val funcs = module.definitions
      .collect({
        case FuncDef(name, FuncBodyDef(ty, nms, locals, body)) =>
          FuncDef(name, FuncBodyDef(ty, nms, locals, Preprocess.idBlocks(body)))
      })
      .toList
    ModuleInstance(List(), funcs)
  }
  // val moduleInst = {
  //   val types = List()
  //   val funcs = List(
  //     FuncDef("add5", FuncType(Seq(NumType(I32Type)), Seq(NumType(I32Type))), Seq(), Seq(
  //       LocalGet(0), Konst(I32C(5)), Binary(BinOp.Int(Add))
  //     ))
  //   )
  //   ModuleInstance(types, funcs)
  // }
  val instrs =
    module.definitions
      .find({
        case FuncDef(Some("$real_main"), _) => true
        case _                              => false
      })
      .get
      .asInstanceOf[FuncDef]
      .f
      .asInstanceOf[FuncBodyDef]
      .body // Super unsafe...
  // val instrs = List(
  //   Konst(I32C(10)),
  //   // LocalSet(0),
  //   // LocalGet(0),
  //   // Konst(I32C(5)),
  //   // Binary(BinOp.Int(Add)),
  //   // LocalSet(1),
  //   // Loop(ValBlockType(None), Seq(
  //   //   LocalGet(0),
  //   //   Konst(I32C(1)),
  //   //   Binary(BinOp.Int(Sub)),
  //   //   LocalTee(0),
  //   //   Konst(I32C(5)),
  //   //   Binary(BinOp.Int(Sub)),
  //   //   Test(TestOp.Int(Eqz)),
  //   //   BrIf(1),
  //   // )),
  //   // Block(ValBlockType(None), Seq(
  //   //   Konst(I32C(0)),
  //   //   // Konst(I32C(-15)),
  //   //   // Binary(BinOp.Int(Add)),
  //   //   // Block(ValBlockType(None), Seq(
  //   //   //   Konst(I32C(10))
  //   //   // )),
  //   //   BrIf(0),
  //   //   Konst(I32C(20)),
  //   // )),
  //   // Konst(I32C(12)),
  //   // Call(0),
  //   // Call(0),
  // )
  System.out.println(s"funcs: ${moduleInst.funcs}")
  val snip = mkVMSnippet(moduleInst, Preprocess.idBlocks(instrs))
  val code = snip.code
  println(code)
  snip.eval(0)
}
