package gensym.wasm.smallstagedeval

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

case class ModuleInstance(
  types: List[FuncType],
  funcs: List[FuncDef],
)

@virtualize
trait StagedEval extends SAIOps {
  case class State(
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
    def memory: Rep[List[Memory]] = Wrap[List[Memory]](Adapter.g.reflect("state-memory", Unwrap(state)))
    def globals: Rep[List[Global]] = Wrap[List[Global]](Adapter.g.reflect("state-globals", Unwrap(state)))
    def frameLocals: Rep[List[Value]] = Wrap[List[Value]](Adapter.g.reflect("state-frameLocals", Unwrap(state)))
    def stack: Rep[List[Value]] = Wrap[List[Value]](Adapter.g.reflect("state-stack", Unwrap(state)))

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

  // case class Continue(state: Rep[State]) extends EvalResult
  // case class Breaking(n: Rep[Int], state: Rep[State]) extends EvalResult
  // case class Returning(state: Rep[State]) extends EvalResult
  def Continue(state: Rep[State]): Rep[EvalResult] =
    Wrap[EvalResult](Adapter.g.reflect("mk-continue", Unwrap(state)))
  def Breaking(n: Rep[Int], state: Rep[State]): Rep[EvalResult] =
    Wrap[EvalResult](Adapter.g.reflect("mk-breaking", Unwrap(n), Unwrap(state)))
  def Returning(state: Rep[State]): Rep[EvalResult] =
    Wrap[EvalResult](Adapter.g.reflect("mk-returning", Unwrap(state)))
  abstract class EvalResult

  implicit class EvalResultOps(result: Rep[EvalResult]) {
    def isContinue: Rep[Boolean] = Wrap[Boolean](Adapter.g.reflect("is-continue", Unwrap(result)))
    def isBreaking: Rep[Boolean] = Wrap[Boolean](Adapter.g.reflect("is-breaking", Unwrap(result)))
    def isReturning: Rep[Boolean] = Wrap[Boolean](Adapter.g.reflect("is-returning", Unwrap(result)))

    def getContinue: Rep[State] = Wrap[State](Adapter.g.reflect("get-continue", Unwrap(result)))
    def getBreakingN: Rep[Int] = Wrap[Int](Adapter.g.reflect("get-breaking-n", Unwrap(result)))
    def getBreakingState: Rep[State] = Wrap[State](Adapter.g.reflect("get-breaking-state", Unwrap(result)))
    def getReturningState: Rep[State] = Wrap[State](Adapter.g.reflect("get-returning", Unwrap(result)))

    def onContinue(f: Rep[State] => Rep[EvalResult]): Rep[EvalResult] = {
      if (result.isContinue) {
        f(result.getContinue)
      } else {
        result
      }
    }
  }

  def reverse[M: Manifest](ls: Rep[List[M]]): Rep[List[M]] =
    Wrap[List[M]](Adapter.g.reflect("reverse-ls", Unwrap(ls)))

  trait Value
  def I32(i: Rep[Int]): Rep[Value] = "I32V".reflectWith[Value](i)
  def I64(i: Rep[Long]): Rep[Value] = "I64V".reflectWith[Value](i)

  implicit def repI32Proj(i: Rep[Value]): Rep[Int] = Unwrap(i) match {
    case Adapter.g.Def("I32V", scala.collection.immutable.List(v: Backend.Exp)) =>
      Wrap[Int](v)
    case _ =>
      Wrap[Int](Adapter.g.reflect("I32V-proj", Unwrap(i)))
  }

  case class Config(module: ModuleInstance, stackBudget: Int) {
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

    def eval(state: Rep[State], instrs: List[Instr]): Rep[EvalResult] = {
      if (instrs.isEmpty) return Continue(state)

      val memory = state.memory
      val globals = state.globals
      val frameLocals = state.frameLocals
      val stack = state.stack
      instrs.head match {
        // Parametric Instructions
        case Drop => this.eval(state.withStack(stack.tail), instrs.tail)
        case Select(_) => {
          val (cond, v2, v1) = (stack(0), stack(1), stack(2))
          val newStack = stack.drop(3)
          val value = if (repI32Proj(cond) == 0) v1 else v2
          this.eval(state.withStack(value :: newStack), instrs.tail)
        }

        // Variable Instructions
        // https://www.w3.org/TR/wasm-core-2/exec/instructions.html#variable-instructions
        case LocalGet(local) =>
          this.eval(state.withStack(frameLocals(local) :: stack), instrs.tail)
        case LocalSet(local) => {
          val nState = state
              .withStack(stack.tail)
              .withFrameLocals(frameLocals.updated(local, stack.head))

          this.eval(nState, instrs.tail)
        }
        case LocalTee(local) => {
          val nState = state
              .withFrameLocals(frameLocals.updated(local, stack.head))

          this.eval(nState, instrs.tail)
        }
        case GlobalGet(global) => {
          val newState = state.withStack(globals(global).value :: stack)
          this.eval(newState, instrs.tail)
        }
        case GlobalSet(global) => {
          val v = stack.head
          val newGlobal = globals(global).withValue(v)
          val newState = state.withStack(stack.tail).withGlobals(globals.updated(global, newGlobal))
          this.eval(newState, instrs.tail)
        }

        // Memory Instructions
        // https://www.w3.org/TR/wasm-core-2/exec/instructions.html#memory-instructions
        case MemorySize =>
          this.eval(state.withStack(I32(state.memory.head.size) :: stack), instrs.tail)

        case MemoryGrow => {
          val delta = stack.head
          val memInstance = state.memory.head
          val oldSize = memInstance.size
          val newMemInstance = memInstance.grow(repI32Proj(delta))
          if (newMemInstance.size > oldSize) {
            val newState = state.withStack(I32(oldSize) :: stack.tail).withMemory(newMemInstance :: state.memory.tail)
            this.eval(newState, instrs.tail)
          } else {
            this.eval(state.withStack(I32(-1) :: stack.tail), instrs.tail)
          }
        }
        case MemoryFill => {
          val (value, offset, length) = (stack(0), stack(1), stack(2))
          val memInstance = state.memory.head
          // TODO when implementing Memory.fill, check out of bounds
          val newMemInstance = memInstance.fill(repI32Proj(offset), repI32Proj(length), repI32Proj(value))
          val newState = state.withStack(stack.drop(3)).withMemory(newMemInstance :: state.memory.tail)
          this.eval(newState, instrs.tail)
        }
        case MemoryCopy => {
          val (src, dst, length) = (stack(0), stack(1), stack(2))
          val memInstance = state.memory.head
          // TODO when implementing Memory.copy, check out of bounds
          val newMemInstance = memInstance.copy(repI32Proj(src), repI32Proj(dst), repI32Proj(length))
          val newState = state.withStack(stack.drop(3)).withMemory(newMemInstance :: state.memory.tail)
          this.eval(newState, instrs.tail)
        }

        // Numeric Instructions
        case Konst(I32C(n)) => this.eval(state.withStack(I32(n) :: stack), instrs.tail)
        case Binary(op) => {
          val (v2, v1) = (stack(0), stack(1))
          val newStack = stack.drop(2)
          this.eval(state.withStack(evalBinOp(op, v1, v2) :: newStack), instrs.tail)
        }
        case Unary(op) => {
          val v = stack.head
          val newStack = stack.tail
          this.eval(state.withStack(evalUnaryOp(op, v) :: newStack), instrs.tail)
        }
        case Compare(op) => {
          val (v2, v1) = (stack(0), stack(1))
          val newStack = stack.drop(2)
          this.eval(state.withStack(evalRelOp(op, v1, v2) :: newStack), instrs.tail)
        }
        case Test(testOp) => {
          val value = stack.head
          val newStack = stack.tail
          this.eval(state.withStack(evalTestOp(testOp, value) :: newStack), instrs.tail)
        }
        case Store(StoreOp(align, offset, tipe, None)) => {
          val (value, address) = (repI32Proj(stack(0)), repI32Proj(stack(1)))
          val newStack = stack.drop(2)
          val mem = memory.head
          val newMem = mem.storeInt(address + offset, value)
          this.eval(state.withStack(newStack).withMemory(newMem :: memory.tail), instrs.tail)
        }
        case Load(LoadOp(align, offset, tipe, None, None)) => {
          val address = repI32Proj(stack.head)
          val newStack = stack.tail
          val mem = memory.head
          val value = mem.loadInt(address + offset)
          this.eval(state.withStack(I32(value) :: newStack), instrs.tail)
        }

        // Control Instructions
        // https://www.w3.org/TR/wasm-core-2/exec/instructions.html#numeric-instructions
        case Nop => this.eval(state, instrs.tail)
        case Unreachable => ???
        case If(blockTy, thenInstrs, elseInstrs) => {
          val cond: Rep[Int] = stack.head
          val newStack = stack.tail
          val funcType = blockTy.toFuncType(Unit)

          val zero: Rep[Int] = 0
          val retState = if (cond == zero) {
            evalBlock(state, funcType.out.length, elseInstrs)
          } else {
            evalBlock(state, funcType.out.length, thenInstrs)
          }

          retState.onContinue { s =>
            evalBlock(s.withStack(newStack ++ s.stack), funcType.out.length, thenInstrs)
          }
        }
        case Block(blockTy, blockInstrs) => {
          val funcType = blockTy.toFuncType(Unit)
          evalBlock(state, funcType.out.length, blockInstrs.toList).onContinue { s =>
            this.eval(s.withStack(state.stack ++ s.stack), instrs.tail)
          }
        }
        case Loop(blockTy, loopInstrs) => {
          val funcType = blockTy.toFuncType(Unit)
          val loopInstrList = loopInstrs.toList
          def loop: Rep[State => EvalResult] = fun { inState =>
            evalBlock(inState, funcType.out.length, loopInstrList).onContinue { res =>
              loop(res)
            }
          }

          loop(state).onContinue { loopState =>
            this.eval(loopState, instrs.tail)
          }
        }
        case Br(label) => {
          Breaking(label, state)
        }
        case BrIf(label) => {
          val cond: Rep[Int] = stack.head
          val newState = state.withStack(stack.tail)
          val zero: Rep[Int] = 0
          if (cond == zero) {
            this.eval(newState, instrs.tail)
          } else {
            Breaking(label, newState)
          }
        }
        case Return => {
          Returning(state)
        }
        case Call(func) => {
           val FuncDef(_, funcType, locals, body) = module.funcs(func)
           val args = reverse(stack.take(funcType.inps.length))
           val newStack = stack.drop(funcType.inps.length)
     
           val initFrameLocals: Rep[List[Value]] = List.fill(locals.length)(I32(0))
           val frameLocals: Rep[List[Value]] = args ++ initFrameLocals
           val inState = state.withStack(List[Value]()).withFrameLocals(frameLocals)
           val retState = evalFunc(inState, funcType.out.length, body.toList)
           retState.onContinue { s =>
             this.eval(s.withStack(newStack ++ s.stack), instrs.tail)
           }
        }
      }
    }

    // basically equates to step with Label on top in the previous impl
    def evalBlock(state: Rep[State], arity: Int, instrs: List[Instr]): Rep[EvalResult] = {
      val res = this.eval(state.withStack(List[Value]()), instrs)

      if (res.isBreaking) {
        val n = res.getBreakingN
        val breakState = res.getBreakingState
        if (n == 0) {
          Continue(breakState.withStack(breakState.stack.take(arity)))
        } else {
          Breaking(n - 1, breakState)
        }
      } else {
        res
      }
    }

    def evalFunc(state: Rep[State], arity: Int, instrs: List[Instr]): Rep[EvalResult] = {
      val ret = evalBlock(state, arity, instrs)
      if (ret.isReturning) {
        val retState = ret.getReturningState
        Continue(retState.withStack(retState.stack.take(arity)))
      } else {
        ret
      }
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
    case Node(s, "state-update-memory", List(state, memory), _) => 
      shallow(state); emit(".withMemory("); shallow(memory); emit(")")
    case Node(s, "state-update-globals", List(state, globals), _) => 
      shallow(state); emit(".withGlobals("); shallow(globals); emit(")")
    case Node(s, "state-update-frameLocals", List(state, locals), _) =>
      shallow(state); emit(".withLocals("); shallow(locals); emit(")")
    case Node(s, "state-update-stack", List(state, stack), _) =>
      shallow(state); emit(".withStack("); shallow(stack); emit(")")
    case Node(s, "mk-continue", List(state), _) => 
      emit("EvalResult { tag: CONTINUE, n: -1, state: "); shallow(state); emit(" }")
    case Node(s, "mk-breaking", List(n, state), _) =>
      emit(s"EvalResult { tag: BREAKING, n: "); shallow(n); emit(", state: "); shallow(state); emit(" }")
    case Node(s, "mk-returning", List(state), _) => 
      emit("EvalResult { tag: RETURNING, n: -1, state: "); shallow(state); emit(" }")
    case Node(s, "is-continue", List(res), _) => shallow(res); emit(".tag == CONTINUE")
    case Node(s, "is-breaking", List(res), _) => shallow(res); emit(".tag == BREAKING")
    case Node(s, "is-returning", List(res), _) => shallow(res); emit(".tag == RETURNING")
    case Node(s, "get-continue", List(res), _) => shallow(res); emit(".state")
    case Node(s, "get-breaking-n", List(res), _) => shallow(res); emit(".n")
    case Node(s, "get-breaking-state", List(res), _) => shallow(res); emit(".state")
    case Node(s, "get-returning", List(res), _) => shallow(res); emit(".state")
    case Node(s, "reverse-ls", List(ls), _) => emit("flex_vector_reverse("); shallow(ls); emit(")")
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

trait CppStagedWasmDriver[A, B] extends CppSAIDriver[A, B] with StagedEval { q =>
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

object SmallStagedTest extends App {
  @virtualize
  def mkVMSnippet(
    module: ModuleInstance,
    instrs: List[Instr],
  ): CppSAIDriver[Int, List[Int]] with StagedEval = {
    new CppStagedWasmDriver[Int, List[Int]] with StagedEval {
      def snippet(arg: Rep[Int]): Rep[List[Int]] = {
        val config = Config(module, 1000)
        val state = State(List[Memory](), List[Global](), List[Value](I32(0)), List[Value]())
        val retState = config.eval(state, instrs)
        if (retState.isContinue) {
          val res = retState.getContinue.stack.map(x => repI32Proj(x))
          for (i <- 0 until res.size) {
            printf("%d ", res(i))
          }
          res
        } else {
          List()
        }
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
  // val instrs = List(
  //   // Konst(I32C(10)),
  //   // LocalSet(0),
  //   Block(ValBlockType(None), Seq(
  //     Loop(ValBlockType(None), Seq(
  //       // LocalGet(0),
  //       // If(ValBlockType(None), List(Konst(I32C(999))), List(Br(1))),
  //       // BrIf(1)
  //     )),
  //   ))
  //   // Binary(BinOp.Int(Add)),
  //   // If(ValBlockType(Some(NumType(I32Type))), List(Konst(I32C(111))), List(Konst(I32C(222))))
  // )
  val instrs = List(
    Konst(I32C(10)),
    LocalSet(0),
    // Call(0),
    Block(ValBlockType(Some(NumType(I32Type))), Seq(
      Loop(ValBlockType(Some(NumType(I32Type))), Seq(
        LocalGet(0),
        Test(TestOp.Int(Eqz)),
        BrIf(1),
        LocalGet(0),
        Konst(I32C(1)),
        Binary(BinOp.Int(Sub)),
        LocalTee(0),
      )),
    )),
    LocalGet(0),
    Call(0),
  )
  val snip = mkVMSnippet(module, instrs)
  val code = snip.code
  println(code)
  snip.eval(0)
}
