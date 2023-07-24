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
    frameLocals: List[Value],
    stack: List[Value],
  )

  object State {
    def apply(
      memory: Rep[List[Memory]], globals: Rep[List[Global]], frameLocals: Rep[List[Value]], stack: Rep[List[Value]]
    ): Rep[State] =
      Wrap[State](Adapter.g.reflect("state-new", Unwrap(memory), Unwrap(globals), Unwrap(frameLocals), Unwrap(stack)))
  }

  implicit class StateOps(state: Rep[State]) {
    def pushStack(value: Rep[Value]) =
      Adapter.g.reflectWrite("state-push-stack", Unwrap(state), Unwrap(value))(Adapter.CTRL)
    def popStack: Rep[Value] =
      Wrap[Value](Adapter.g.reflectWrite("state-pop-stack", Unwrap(state))(Adapter.CTRL))

    def memory: Rep[List[Memory]] = Wrap[List[Memory]](Adapter.g.reflect("state-memory", Unwrap(state)))
    def globals: Rep[List[Global]] = Wrap[List[Global]](Adapter.g.reflect("state-globals", Unwrap(state)))
    def frameLocals: Rep[List[Value]] = Wrap[List[Value]](Adapter.g.reflect("state-frameLocals", Unwrap(state)))
    def stack: Rep[List[Value]] = Wrap[List[Value]](Adapter.g.reflect("state-stack", Unwrap(state)))

    def printStack() = Adapter.g.reflectWrite("state-print-stack", Unwrap(state))(Adapter.CTRL)

    def withMemory(memory: Rep[List[Memory]]): Rep[State] =
      Wrap[State](Adapter.g.reflect("state-update-memory", Unwrap(state), Unwrap(memory)))

    def withGlobals(globals: Rep[List[Global]]): Rep[State] =
      Wrap[State](Adapter.g.reflect("state-update-globals", Unwrap(state), Unwrap(globals)))

    def withFrameLocals(frameLocals: Rep[List[Value]]): Rep[State] =
      Wrap[State](Adapter.g.reflect("state-update-frameLocals", Unwrap(state), Unwrap(frameLocals)))

    def withStack(stack: Rep[List[Value]]): Rep[State] =
      Wrap[State](Adapter.g.reflect("state-update-stack", Unwrap(state), Unwrap(stack)))
  }

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
  def I32(i: Rep[Int]): Rep[Value] = "I32V".reflectWith[Value](i)
  def I64(i: Rep[Long]): Rep[Value] = "I64V".reflectWith[Value](i)

  type Cont = Unit => Unit

  implicit def repI32Proj(i: Rep[Value]): Rep[Int] = Unwrap(i) match {
    case Adapter.g.Def("I32V", scala.collection.immutable.List(v: Backend.Exp)) =>
      Wrap[Int](v)
    case _ =>
      Wrap[Int](Adapter.g.reflect("I32V-proj", Unwrap(i)))
  }

  case class StaticState(labels: List[Rep[Cont]], returnLabel: Option[Rep[Cont]])

  case class Config(
    state: Rep[State],
    module: ModuleInstance,
    funcConts: HashMap[String, (Rep[List[Value]], Rep[Cont]) => Rep[Cont]],
    stackBudget: Int
  ) {
    def stack(i: Int): Rep[Value] = state.stack(i)

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
      case TestOp.Int(Eqz) => if (repI32Proj(value) == 0) I32(1) else I32(0)
    }

    def execInst(instr: Instr, k: Rep[Cont] => Rep[Unit])(kk: Rep[Cont])(implicit ss: StaticState): Rep[Unit]
    = instr match {
      case Konst(I32C(n)) => 
        state.pushStack(I32(n))
        k(kk)
      case Binary(op) => {
        val (v2, v1) = (state.popStack, state.popStack)
        val res = evalBinOp(op, v1, v2)
        state.pushStack(res)
        k(kk)
      }
      case Br(label) => {
        val cont = ss.labels(label)
        cont(())
      }
      case BrIf(label) => {
        val cond: Rep[Int] = state.popStack
        val zero: Rep[Int] = 0
        if (cond == zero) {
          k(kk)
        } else {
          val cont = ss.labels(label)
          cont(())
        }
      }
    }

    def execInstrs(instrs: List[Instr], k: Rep[Cont])(implicit ss: StaticState): Rep[Unit] = instrs match {
      case Nil => k(())
      case Block(blockTy, blockInstrs) :: rest => {
        // continuation for after the block ends
        val blockK = fun { (_: Rep[Unit]) => execInstrs(rest, k) }
        execInstrs(blockInstrs.toList, blockK)(ss.copy(blockK :: ss.labels))
      }
      case Loop(blockTy, loopInstrs) :: rest => {
        // continuation for after the loop breaks
        val loopK = fun { (_: Rep[Unit]) => execInstrs(rest, k) }

        // the actual loop function
        def loopFn: Rep[Cont] = fun { (u: Rep[Unit]) =>
          execInstrs(loopInstrs.toList, loopFn)(ss.copy(loopK :: ss.labels))
        }
        loopFn(())
      }
      case Call(func) :: rest => {
        val FuncDef(name, funcType, locals, body) = module.funcs(func)
        val args = reverse(state.stack.take(funcType.inps.length))

        def compileFun(body: List[Instr])(args: Rep[List[Value]], k: Rep[Cont]): Rep[Cont] = fun { (_: Rep[Unit]) =>
          // 1. init new locals
          // 2. push new locals
          execInstrs(body, k)(ss.copy(returnLabel = Some(k)))
          // 4. pop locals
        }

        val funcCont = funcConts.get(name) match {
          case Some(f) => f
          case None => {
            val f: (Rep[List[Value]], Rep[Cont]) => Rep[Cont] = compileFun(body.toList)(_, _)
            funcConts += (name -> f)
            f
          }
        }
        funcCont(args, k)(())
      }
      case instr :: rest =>
        execInst(instr, k1 => execInstrs(rest, k1))(k)
    }
  }
}

trait CppStagedWasmGen extends CppSAICodeGenBase {
  import lms.core.Backend._
  registerHeader("./headers", "<wasm_state_continue.hpp>")

  override def shallow(n: Node): Unit = n match {
    case Node(s, "I32V", List(i), _) => emit("I32V("); shallow(i); emit(")")
    case Node(s, "I32V-proj", List(i), _) => shallow(i); emit(".i32")
    case Node(s, "state-new", List(memory, globals, locals, stack), _) => 
      emit("State("); 
      shallow(memory); emit(", "); shallow(globals); emit(", "); shallow(locals); emit(", "); shallow(stack); 
      emit(")")
    case Node(s, "state-memory", List(state), _) => shallow(state); emit(".memory")
    case Node(s, "state-globals", List(state), _) => shallow(state); emit(".globals")
    case Node(s, "state-frameLocals", List(state), _) => shallow(state); emit(".locals")
    case Node(s, "state-stack", List(state), _) => shallow(state); emit(".stack")
    case Node(s, "state-push-stack", List(state, v), _) => shallow(state); emit(".push_stack("); shallow(v); emit(")")
    case Node(s, "state-pop-stack", List(state), _) => shallow(state); emit(".pop_stack("); emit(")")
    case Node(s, "state-print-stack", List(state), _) => shallow(state); emit(".print_stack()")
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
        val state = State(List[Memory](), List[Global](), List[Value](I32(0)), List[Value]())
        val config = Config(state, module, HashMap(), 1000)
        config.execInstrs(instrs, fun { _ => config.state.printStack(); () })(StaticState(Nil, None))
      }
    }
  }

  val module = {
    val types = List()
    val funcs = List(
      FuncDef("add5", FuncType(Seq(NumType(I32Type)), Seq(NumType(I32Type))), Seq(NumType(I32Type)), Seq(
        LocalGet(0), Konst(I32C(5)), Binary(BinOp.Int(Add))
      ))
    )
    ModuleInstance(types, funcs)
  }
  val instrs = List(
    Loop(ValBlockType(None), Seq(
      Konst(I32C(1)),
      BrIf(0),
    )),
    // Block(ValBlockType(None), Seq(
    //   Konst(I32C(10)),
    //   Konst(I32C(-10)),
    //   Binary(BinOp.Int(Add)),
    //   BrIf(0),
    //   Konst(I32C(1)),
    // )),
    Konst(I32C(12)),
  )
  val snip = mkVMSnippet(module, instrs)
  val code = snip.code
  println(code)
  snip.eval(0)
}
