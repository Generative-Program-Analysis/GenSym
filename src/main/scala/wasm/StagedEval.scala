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

  // TODO: hack because otherwise variable names get messed up
  type Value = Int
  def I32(i: Rep[Int]): Rep[Value] = i
  def repI32Proj(i: Rep[Value]): Rep[Int] = i

  // trait Value
  // def I32(i: Rep[Int]): Rep[Value] = "I32V".reflectWith[Value](i)
  // def I64(i: Rep[Long]): Rep[Value] = "I64V".reflectWith[Value](i)

  // implicit def repI32Proj(i: Rep[Value]): Rep[Int] = Unwrap(i) match {
  //   case Adapter.g.Def("I32V", scala.collection.immutable.List(v: Backend.Exp)) =>
  //     Wrap[Int](v)
  //   case _ =>
  //     Wrap[Int](Adapter.g.reflect("I32V-proj", Unwrap(i)))
  // }

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

    def eval(state: Rep[State], instrs: List[Instr]): Rep[EvalResult] = {
      if (instrs.isEmpty) return Continue(state)

      val memory = state.memory
      val globals = state.globals
      val frameLocals = state.frameLocals
      val stack = state.stack
      instrs.head match {
        case Drop => this.eval(state.withStack(stack.tail), instrs.tail)

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

        case Konst(I32C(n)) => this.eval(state.withStack(I32(n) :: stack), instrs.tail)
        case Binary(op) => {
          val (v2, v1) = (stack(0), stack(1))
          val newStack = stack.drop(2)
          this.eval(state.withStack(evalBinOp(op, v1, v2) :: newStack), instrs.tail)
        }

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
          // val retStack = evalBlock(funcType.out.length, blockInstrs.toList)
          // def block: Rep[State => EvalResult] = fun { inState =>
          //   val outState = evalBlock(inState, funcType.out.length, blockInstrs.toList)
          //   State(memory, globals, outState.frameLocals, inState.stack ++ outState.stack)
          // }

          // this.eval(block(state), instrs.tail)
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

          loop(state).onContinue { state =>
            this.eval(state, instrs.tail)
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
        // case Call(func) => {
        //    val FuncDef(_, funcType, locals, body) = frame.module.funcs(func)
        //    // TODO: reverse args
        //    val args = stack.take(funcType.inps.length)
        //    val newStack = stack.drop(funcType.inps.length)
     
        //    val initFrameLocals: Rep[List[Value]] = List.fill(locals.length)(I32(0))
        //    val frameLocals: Rep[List[Value]] = args ++ initFrameLocals
        //    val newFrame = Frame(frame.module, frameLocals)
        //    val retState = evalFunc(args, newFrame, funcType, body.toList)
        //    this.eval(retState.copy(stack = newStack ++ retState.stack, instrs.tail)
        // }
      }
    }

    // basically equates to step with Label on top in the previous impl
    def evalBlock(state: Rep[State], arity: Int, instrs: List[Instr]): Rep[EvalResult] = {
      val res = this.eval(state.withStack(List()), instrs)

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

    // def evalFunc(args: Rep[List[Value]], nframe: Frame, funcType: FuncType, instrs: List[Instr]): Config = {
    //   try {
    //     this.copy(frame = nframe).evalBlock(funcType.out.length, instrs)
    //   } catch {
    //     case Returning(retStack) => retStack.take(funcType.out.length)
    //     case e: Exception => throw e
    //   }
    // }
  }
}

trait CppStagedWasmGen extends CppSAICodeGenBase {
  import lms.core.Backend._
  registerHeader("./headers", "<wasm_state_continue.hpp>")

  override def shallow(n: Node): Unit = n match {
    case Node(s, "I32V", List(i), _) => emit(s"int("); shallow(i); emit(")")
    case Node(s, "I32V-proj", List(i), _) => shallow(i)
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
    case _ => super.shallow(n)
  }
}

trait CppStagedWasmDriver[A, B] extends CppSAIDriver[A, B] with StagedEval { q =>
  override val codegen = new CGenBase with CppStagedWasmGen {
    val IR: q.type = q
    import IR._
    override def remap(m: Manifest[_]): String = {
      if (m.toString.endsWith("$Value")) "int"
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
  ): CppSAIDriver[List[Int], List[Int]] with StagedEval = {
    new CppStagedWasmDriver[List[Int], List[Int]] with StagedEval {
      def snippet(locals: Rep[List[Int]]): Rep[List[Int]] = {
        // stack.map(x => I32(x)).map(x => repI32Proj(x))
        val config = Config(module, 1000)
        val realLocals = locals.map(x => I32(x))
        val state = State(List(), List(), realLocals, List())
        val retState = config.eval(state, instrs)
        if (retState.isContinue) {
          retState.getContinue.stack.map(x => repI32Proj(x))
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
        Konst(I32C(5)), Binary(BinOp.Int(Add))
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
    Block(ValBlockType(Some(NumType(I32Type))), Seq(
      Loop(ValBlockType(Some(NumType(I32Type))), Seq(
        LocalGet(0),
        // BrIf(1),
        // Konst(I32C(1)),
        // Binary(BinOp.Int(Sub)),
        // LocalTee(0),
        // If(ValBlockType(None), List(), List(Br(2))),
      )),
    ))
  )
  val snip = mkVMSnippet(module, instrs)
  val code = snip.code
  println(code)
  // snip.eval(List(5))
}
