package gensym.wasm.stagedevalcps

import scala.collection.mutable.HashMap

import gensym.wasm.ast.{Const => Konst, _}
import gensym.wasm.values.{I32 => I32C}
import gensym.wasm.types._
import gensym.wasm.memory._
import gensym.wasm.globals._

import lms.core._
import lms.core.stub._
import lms.macros.SourceContext
import lms.core.virtualize
import gensym.lmsx._

case class ModuleInstance(types: List[FuncType], funcs: List[FuncDef])

@virtualize
trait StagedEvalCPS extends SAIOps {
  class State(
    memory: List[Memory],
    globals: List[Global],
    stack: List[Value],
  )

  object State {
    // def apply(
    //   memory: Rep[List[Memory]], globals: Rep[List[Global]], stack: Rep[List[Value]]
    // ): Rep[State] =
    //   Wrap[State](Adapter.g.reflect("state-new", Unwrap(memory), Unwrap(globals), Unwrap(stack)))
    def stackLength: Rep[Int] =
      Wrap[Int](Adapter.g.reflect("static-state-stack-length"))
    def pushStack(value: Rep[Value]) =
      Adapter.g.reflectWrite("static-state-push-stack", Unwrap(value))(Adapter.CTRL)
    def popStack: Rep[Value] =
      Wrap[Value](Adapter.g.reflectWrite("static-state-pop-stack")(Adapter.CTRL))
    def peekStack: Rep[Value] =
      Wrap[Value](Adapter.g.reflectWrite("static-state-peek-stack")(Adapter.CTRL))
    def removeStackRange(start: Rep[Int], end: Rep[Int]) =
      Adapter.g.reflectWrite("static-state-remove-stack-range", Unwrap(start), Unwrap(end))(Adapter.CTRL)
    def getLocal(i: Rep[Int])(implicit ss: StaticState): Rep[Value] =
      Wrap[Value](
        Adapter.g.reflectWrite("static-state-get-local", Unwrap(ss.localPtr + i))(Adapter.CTRL)
      )

    def setLocal(i: Rep[Int], v: Rep[Value])(implicit ss: StaticState) =
      Adapter.g.reflectWrite("static-state-set-local", Unwrap(ss.localPtr + i), Unwrap(v))(Adapter.CTRL)

    def printStack() = Adapter.g.reflectWrite("static-state-print-stack")(Adapter.CTRL)
  }

  def initState(memory: Rep[List[Memory]], globals: Rep[List[Global]], stack: Rep[List[Value]]): Rep[State] =
    Wrap[State](Adapter.g.reflectWrite("state-init", Unwrap(memory), Unwrap(globals), Unwrap(stack))(Adapter.CTRL))

  // TODO: can probably replace Global with Value since we assume validation
  implicit class GlobalOps(global: Rep[Global]) {
    def value: Rep[Value] = Wrap[Value](Adapter.g.reflect("global-value", Unwrap(global)))
    def withValue(value: Rep[Value]): Rep[Global] =
      Wrap[Global](Adapter.g.reflect("global-update-value", Unwrap(global), Unwrap(value)))
  }

  implicit class MemoryOps(memory: Rep[Memory]) {
    def size: Rep[Int] = Wrap[Int](Adapter.g.reflect("memory-size", Unwrap(memory)))
    def grow(n: Rep[Int]): Rep[Memory] = Wrap[Memory](Adapter.g.reflect("memory-grow", Unwrap(memory), Unwrap(n)))
    def fill(offset: Rep[Int], size: Rep[Int], value: Rep[Int]): Rep[Memory] =
      Wrap[Memory](Adapter.g.reflect("memory-fill", Unwrap(memory), Unwrap(offset), Unwrap(size), Unwrap(value)))
    def copy(srcOffset: Rep[Int], dstOffset: Rep[Int], size: Rep[Int]): Rep[Memory] =
      Wrap[Memory](Adapter.g.reflect("memory-copy", Unwrap(memory), Unwrap(srcOffset), Unwrap(dstOffset), Unwrap(size)))
    def storeInt(addr: Rep[Int], value: Rep[Int]) =
      Wrap[Memory](Adapter.g.reflect("memory-store-int", Unwrap(memory), Unwrap(addr), Unwrap(value)))
    def loadInt(addr: Rep[Int]) =
      Wrap[Int](Adapter.g.reflect("memory-load-int", Unwrap(memory), Unwrap(addr)))
  }

  def reverse[M: Manifest](ls: Rep[List[M]]): Rep[List[M]] =
    Wrap[List[M]](Adapter.g.reflect("reverse-ls", Unwrap(ls)))

  trait Value
  // def I32(i: Rep[Int]): Rep[Value] = "I32V".reflectWith[Value](i)
  // def I64(i: Rep[Long]): Rep[Value] = "I64V".reflectWith[Value](i)
  def I32(i: Rep[Int]): Rep[Value] = Wrap[Value](Adapter.g.reflectWrite("I32V", Unwrap(i))(Adapter.CTRL))
  def I64(i: Rep[Long]): Rep[Value] = Wrap[Value](Adapter.g.reflectWrite("I64V", Unwrap(i))(Adapter.CTRL))

  type Cont = Unit => Unit

  implicit def repI32Proj(i: Rep[Value]): Rep[Int] = Unwrap(i) match {
    case Adapter.g.Def("I32V", scala.collection.immutable.List(v: Backend.Exp)) =>
      Wrap[Int](v)
    case _ =>
      Wrap[Int](Adapter.g.reflect("I32V-proj", Unwrap(i)))
  }

  case class StaticState(
    labels: List[Rep[Cont]],
    returnLabel: Option[Rep[Cont]],
    stackPtr: Int,
    localPtr: Int,
    retN: Option[Int]
  )

  case class Config(
    module: ModuleInstance,
    funFuns: HashMap[String, Rep[Cont] => Rep[Unit]],
    stackBudget: Int
  ) {
    def evalBinOp(op: BinOp, lhsV: Rep[Value], rhsV: Rep[Value]): Rep[Value] = op match {
      case BinOp.Int(Add) => {
        // assume I32
        val (lhs, rhs) = (repI32Proj(lhsV), repI32Proj(rhsV))
        I32(lhs + rhs)
      }
      case BinOp.Int(Sub) => {
        val (lhs, rhs) = (repI32Proj(lhsV), repI32Proj(rhsV))
        I32(lhs - rhs)
      }
    }
    def evalUnaryOp(op: UnaryOp, value: Rep[Value]): Rep[Value] = ???
    def evalRelOp(op: RelOp, lhs: Rep[Value], rhs: Rep[Value]): Rep[Value] = ???
    def evalTestOp(op: TestOp, value: Rep[Value]): Rep[Value] = op match {
      case TestOp.Int(Eqz) => {
        val v: Rep[Int] = repI32Proj(value)
        val zero: Rep[Int] = 0
        if (v == zero) I32(1) else I32(0)
      }
    }

    def execInst
    (instr: Instr, k: (StaticState, Rep[Cont]) => Rep[Unit])(kk: Rep[Cont])(implicit ss: StaticState): Rep[Unit]
    = {
      // print(s"Instr: $instr, sp: ${ss.stackPtr}, ")
      // State.printStack()
      instr match {
        case Konst(I32C(n)) => 
          State.pushStack(I32(n))
          k(ss.copy(stackPtr = ss.stackPtr + 1), kk)
        case Binary(op) => {
          val (v2, v1) = (State.popStack, State.popStack)
          val res = evalBinOp(op, v1, v2)
          State.pushStack(res)
          k(ss.copy(stackPtr = ss.stackPtr - 1), kk)
        }
        case Test(testOp) => {
          val v = State.popStack
          State.pushStack(evalTestOp(testOp, v))
          k(ss, kk)
        }
        case LocalGet(local) => {
          val v = State.getLocal(local)
          State.pushStack(v)
          k(ss.copy(stackPtr = ss.stackPtr + 1), kk)
        }
        case LocalSet(local) => {
          val v = State.popStack
          State.setLocal(local, v)
          k(ss.copy(stackPtr = ss.stackPtr - 1), kk)
        }
        case LocalTee(local) => {
          val v = State.peekStack
          State.setLocal(local, v)
          k(ss, kk)
        }
        case Return => {
          State.removeStackRange(State.stackLength - ss.retN.get, State.stackLength)
          ss.returnLabel match {
            case Some(cont) => cont(())
            case None => throw new Exception("return outside function")
          }
        }
        case Br(label) => {
          val cont = ss.labels(label)
          cont(())
        }
        case BrIf(label) => {
          val cond: Rep[Int] = State.popStack
          val zero: Rep[Int] = 0
          if (cond == zero) {
            k(ss.copy(stackPtr = ss.stackPtr - 1), kk)
          } else {
            val cont = ss.labels(label)
            cont(())
          }
        }
      }
    }

    def execInstrs(instrs: List[Instr], k: Rep[Cont])(implicit ss: StaticState): Rep[Unit] = instrs match {
      case Nil => k(())
      case Block(blockTy, blockInstrs) :: rest => {
        // continuation for after the block ends
        val blockK = topFun { (_: Rep[Unit]) => execInstrs(rest, k) }
        execInstrs(blockInstrs.toList, blockK)(ss.copy(blockK :: ss.labels))
      }
      case Loop(blockTy, loopInstrs) :: rest => {
        // continuation for after the loop breaks
        val loopK = topFun { (_: Rep[Unit]) => execInstrs(rest, k) }

        // the actual loop function
        def loopFn: Rep[Cont] = topFun { (_: Rep[Unit]) =>
          execInstrs(loopInstrs.toList, loopFn)(ss.copy(loopK :: ss.labels))
        }
        loopFn(())
      }
      case Call(func) :: rest => {
        val FuncDef(name, funcType, locals, body) = module.funcs(func)

        // can't use topFun because of state scoping, could probably be fixed just by making
        // all the state methods global in cpp even if they're methods in scala
        def compileFun(argNum: Int, retNum: Int, body: List[Instr]): Rep[Cont => Unit] = topFun { (k: Rep[Cont]) =>
          for (local <- locals) {
            State.pushStack(I32(0)) // TODO: default values for other types
          }
          val newSP = ss.stackPtr + locals.length
          val newSS = ss.copy(
            returnLabel = Some(k), stackPtr = newSP, localPtr = newSP - funcType.inps.length, retN = Some(retNum)
          )
          execInstrs(body, k)(newSS)
        }

        val funFun = funFuns.get(name) match {
          case Some(f) => f
          case None => {
            val f: Rep[Cont] => Rep[Unit] = compileFun(funcType.inps.length, funcType.out.length, body.toList)(_)
            funFuns += (name -> f)
            f
          }
        }

        val evalRest = topFun { (_: Rep[Unit]) => 
          State.removeStackRange(ss.stackPtr - funcType.inps.length, ss.stackPtr)
          execInstrs(rest, k) 
        }
        funFun(evalRest)
      }
      case instr :: rest =>
        execInst(instr, (ss, k1) => execInstrs(rest, k1)(ss))(k)
    }
  }
}

trait CppStagedWasmGen extends CppSAICodeGenBase {
  import lms.core.Backend._
  registerHeader("./headers", "<wasm_state_continue.hpp>")

  override def shallow(n: Node): Unit = n match {
    case Node(s, "reverse-ls", List(ls), _) => emit("flex_vector_reverse("); shallow(ls); emit(")")
    case Node(s, "I32V", List(i), _) => emit("I32V("); shallow(i); emit(")")
    case Node(s, "I32V-proj", List(i), _) => shallow(i); emit(".i32")
    case Node(s, "state-new", List(memory, globals, stack), _) => 
      emit("State("); 
      shallow(memory); emit(", "); shallow(globals); emit(", "); shallow(stack); 
      emit(")")
    case Node(s, "state-init", List(memory, globals, stack), _) => 
      emit("init_state("); 
      shallow(memory); emit(", "); shallow(globals); emit(", "); shallow(stack); 
      emit(")")
    case Node(s, "state-memory", List(state), _) => shallow(state); emit(".memory")
    case Node(s, "state-globals", List(state), _) => shallow(state); emit(".globals")
    case Node(s, "state-push-locals", List(state, locals), _) => 
      shallow(state); emit(".push_locals("); shallow(locals); emit(")")
    case Node(s, "state-pop-locals", List(state), _) => shallow(state); emit(".pop_locals()")
    case Node(s, "state-stack", List(state), _) => shallow(state); emit(".stack")
    case Node(s, "state-stack-length", List(state), _) => shallow(state); emit(".stack.size()")
    case Node(s, "static-state-stack-length", List(), _) => emit("global_state.stack.size()")
    case Node(s, "state-push-stack", List(state, v), _) => shallow(state); emit(".push_stack("); shallow(v); emit(")")
    case Node(s, "static-state-push-stack", List(v), _) => emit("global_state.push_stack("); shallow(v); emit(")")
    case Node(s, "state-pop-stack", List(state), _) => shallow(state); emit(".pop_stack("); emit(")")
    case Node(s, "static-state-pop-stack", List(), _) => emit("global_state.pop_stack()")
    case Node(s, "state-peek-stack", List(state), _) => shallow(state); emit(".peek_stack("); emit(")")
    case Node(s, "static-state-peek-stack", List(), _) => emit("global_state.peek_stack()")
    case Node(s, "state-print-stack", List(state), _) => shallow(state); emit(".print_stack()")
    case Node(s, "static-state-print-stack", List(), _) => emit("global_state.print_stack()")
    case Node(s, "state-get-local", List(state, i), _) => shallow(state); emit(".get_local("); shallow(i); emit(")")
    case Node(s, "static-state-get-local", List(i), _) => emit("global_state.get_local("); shallow(i); emit(")")
    case Node(s, "state-set-local", List(state, i, v), _) => 
      shallow(state); emit(".set_local("); shallow(i); emit(", "); shallow(v); emit(")")
    case Node(s, "static-state-set-local", List(i, v), _) => 
      emit("global_state.set_local("); shallow(i); emit(", "); shallow(v); emit(")")
    case Node(s, "state-remove-stack-range", List(state, st, ed), _) => 
      shallow(state); emit(".remove_stack_range("); shallow(st); emit(", "); shallow(ed); emit(")")
    case Node(s, "static-state-remove-stack-range", List(st, ed), _) => 
      emit("global_state.remove_stack_range("); shallow(st); emit(", "); shallow(ed); emit(")")
    case Node(s, "memory-size", List(memory), _) => shallow(memory); emit(".size()")
    case Node(s, "memory-grow", List(memory, delta), _) => 
      shallow(memory); emit(".grow("); shallow(delta); emit(")")
    case Node(s, "memory-fill", List(memory, offset, size, value), _) =>
      shallow(memory); emit(".fill("); shallow(offset); emit(", "); shallow(size); emit(", "); shallow(value); emit(")")
    case Node(s, "memory-copy", List(memory, srcOffset, dstOffset, size), _) =>
      shallow(memory); emit(".copy("); 
      shallow(srcOffset); emit(", "); shallow(dstOffset); emit(", "); shallow(size); emit(")")
    case Node(s, "memory-store-int", List(memory, offset, value), _) =>
      shallow(memory); emit(".storeInt("); shallow(offset); emit(", "); shallow(value); emit(")")
    case Node(s, "memory-load-int", List(memory, offset), _) =>
      shallow(memory); emit(".loadInt("); shallow(offset); emit(")")
    case _ => super.shallow(n)
  }
}

trait CppStagedWasmDriver[A, B] extends CppSAIDriver[A, B] with StagedEvalCPS { q =>
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
        initState(List[Memory](), List[Global](), List[Value](I32(0)))
        val config = Config(module, HashMap(), 1000)
        config.execInstrs(instrs, topFun { _ => State.printStack(); () })(StaticState(Nil, None, 1, 0, None))
      }
    }
  }

  val module = {
    val types = List()
    val funcs = List(
      FuncDef("add5", FuncType(Seq(NumType(I32Type)), Seq(NumType(I32Type))), Seq(), Seq(
        LocalGet(0), Konst(I32C(5)), Binary(BinOp.Int(Add))
      ))
    )
    ModuleInstance(types, funcs)
  }
  val instrs = {
    val file = scala.io.Source.fromFile("./benchmarks/wasm/iter.wat").mkString
    val module = gensym.wasm.parser.Parser.parseString(file)
    module.definitions.find({
      case FuncDef("$real_main", _, _, _) => true
      case _ => false
    }).get.asInstanceOf[FuncDef].body.toList
  }
  // val instrs = List(
  //   Konst(I32C(10)),
  //   LocalSet(0),
  //   Loop(ValBlockType(None), Seq(
  //     LocalGet(0),
  //     Konst(I32C(1)),
  //     Binary(BinOp.Int(Sub)),
  //     LocalTee(0),
  //     Konst(I32C(5)),
  //     Binary(BinOp.Int(Sub)),
  //     Test(TestOp.Int(Eqz)),
  //     BrIf(0),
  //   )),
  //   // Block(ValBlockType(None), Seq(
  //   //   Konst(I32C(10)),
  //   //   Konst(I32C(-10)),
  //   //   Binary(BinOp.Int(Add)),
  //   //   BrIf(0),
  //   //   Konst(I32C(1)),
  //   // )),
  //   // Konst(I32C(12)),
  //   Call(0),
  //   Call(0),
  // )
  val snip = mkVMSnippet(module, instrs)
  val code = snip.code
  println(code)
  snip.eval(0)
}
