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
  trait State
  object State {
    def pushStack(value: Rep[Value])(implicit ss: StaticState) =
      Adapter.g.reflectWrite("static-state-push-stack", Unwrap(value), Unwrap(ss.stackPtr))(Adapter.CTRL)
    def stackAt(i: Int)(implicit ss: StaticState): Rep[Value] = 
      Wrap[Value](Adapter.g.reflectWrite("static-state-stack-at", Unwrap(ss.stackPtr - 1 - i))(Adapter.CTRL))
    def popStack(implicit ss: StaticState): Rep[Value] = peekStack
    def peekStack(implicit ss: StaticState): Rep[Value] =
      Wrap[Value](Adapter.g.reflectWrite("static-state-peek-stack", Unwrap(ss.stackPtr))(Adapter.CTRL))
    def removeStackRange(start: Rep[Int], end: Rep[Int])(implicit ss: StaticState) =
      Adapter.g.reflectWrite(
        "static-state-remove-stack-range", Unwrap(start), Unwrap(end), Unwrap(ss.stackPtr))(Adapter.CTRL
      )
    def getLocal(i: Rep[Int])(implicit ss: StaticState): Rep[Value] =
      Wrap[Value](
        Adapter.g.reflectWrite("static-state-get-local", Unwrap(ss.localPtr + i))(Adapter.CTRL)
      )
    def setLocal(i: Rep[Int], v: Rep[Value])(implicit ss: StaticState) =
      Adapter.g.reflectWrite("static-state-set-local", Unwrap(ss.localPtr + i), Unwrap(v))(Adapter.CTRL)

    def printStack(implicit ss: StaticState) = 
      Adapter.g.reflectWrite("static-state-print-stack", Unwrap(ss.stackPtr))(Adapter.CTRL)

    def getGlobal(i: Int): Rep[Value] =
      Wrap[Value](Adapter.g.reflectWrite("static-state-get-global", Unwrap(i))(Adapter.CTRL))
    def setGlobal(i: Int, v: Rep[Value]) =
      Adapter.g.reflectWrite("static-state-set-global", Unwrap(i), Unwrap(v))(Adapter.CTRL)

    def memorySize: Rep[Int] =
      Wrap[Int](Adapter.g.reflect("static-state-memory-size"))
    def memoryGrow(n: Rep[Int]): Rep[Int] =
      Wrap[Int](Adapter.g.reflect("static-state-memory-grow", Unwrap(n)))
    def memoryFill(offset: Rep[Int], size: Rep[Int], value: Rep[Int]) =
      Adapter.g.reflectWrite("static-state-memory-fill", Unwrap(offset), Unwrap(size), Unwrap(value))(Adapter.CTRL)
    def memoryCopy(srcOffset: Rep[Int], dstOffset: Rep[Int], size: Rep[Int]) =
      Adapter.g.reflectWrite("static-state-memory-copy", Unwrap(srcOffset), Unwrap(dstOffset), Unwrap(size))(Adapter.CTRL)
  }

  def initState(memory: Rep[List[Memory]], globals: Rep[List[Global]]): Rep[State] =
    Wrap[State](Adapter.g.reflectWrite("state-init", Unwrap(memory), Unwrap(globals))(Adapter.CTRL))

  def panic(msg: Rep[String]): Rep[Unit] =
    Wrap[Unit](Adapter.g.reflectWrite("panic", Unwrap(msg))(Adapter.CTRL))

  def reverse[M: Manifest](ls: Rep[List[M]]): Rep[List[M]] =
    Wrap[List[M]](Adapter.g.reflect("reverse-ls", Unwrap(ls)))

  trait Value
  // def I32(i: Rep[Int]): Rep[Value] = "I32V".reflectWith[Value](i)
  // def I64(i: Rep[Long]): Rep[Value] = "I64V".reflectWith[Value](i)
  def I32(i: Rep[Int]): Rep[Value] = Wrap[Value](Adapter.g.reflectWrite("I32V", Unwrap(i))(Adapter.CTRL))
  def I64(i: Rep[Long]): Rep[Value] = Wrap[Value](Adapter.g.reflectWrite("I64V", Unwrap(i))(Adapter.CTRL))

  type Cont = Unit => Unit
  type SSCont = StaticState => Rep[Cont]

  implicit def repI32Proj(i: Rep[Value]): Rep[Int] = Unwrap(i) match {
    case Adapter.g.Def("I32V", scala.collection.immutable.List(v: Backend.Exp)) =>
      Wrap[Int](v)
    case _ =>
      Wrap[Int](Adapter.g.reflect("I32V-proj", Unwrap(i)))
  }

  case class StaticState(
    labels: List[SSCont],
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
    // TODO: pass precise type to operations
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
      case BinOp.Int(Mul) => {
        val (lhs, rhs) = (repI32Proj(lhsV), repI32Proj(rhsV))
        I32(lhs * rhs)
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

    // TODO: probably use a monad for ss
    def execInst
    (instr: Instr, k: (StaticState, SSCont) => Rep[Unit])(kk: SSCont)(implicit ss: StaticState): Rep[Unit]
    = {
      print(s"Instr: $instr, sp: ${ss.stackPtr}, lp: ${ss.localPtr} ")
      State.printStack(ss)
      instr match {
        // Parametric Instructions
        case Drop => k(ss.copy(stackPtr = ss.stackPtr - 1), kk)
        case Select(_) => {
          val (cond, v2, v1) = (State.stackAt(0), State.stackAt(1), State.stackAt(2))
          val res = if (cond == I32(0)) v2 else v1
          State.pushStack(res)(ss.copy(stackPtr = ss.stackPtr - 3))
          k(ss.copy(stackPtr = ss.stackPtr - 2), kk)
        }

        // Variable Instructions
        // https://www.w3.org/TR/wasm-core-2/exec/instructions.html#variable-instructions
        case LocalGet(local) => {
          val v = State.getLocal(local)
          State.pushStack(v)
          k(ss.copy(stackPtr = ss.stackPtr + 1), kk)
        }
        case LocalSet(local) => {
          val v = State.popStack
          State.setLocal(local, v)(ss.copy(stackPtr = ss.stackPtr - 1))
          k(ss.copy(stackPtr = ss.stackPtr - 1), kk)
        }
        case LocalTee(local) => {
          val v = State.peekStack
          State.setLocal(local, v)
          k(ss, kk)
        }
        case GlobalGet(global) => {
          val v = State.getGlobal(global)
          State.pushStack(v)
          k(ss.copy(stackPtr = ss.stackPtr + 1), kk)
        }
        case GlobalSet(global) => {
          val v = State.popStack
          State.setGlobal(global, v)
          k(ss.copy(stackPtr = ss.stackPtr - 1), kk)
        }

        // Memory Instructions
        // https://www.w3.org/TR/wasm-core-2/exec/instructions.html#memory-instructions
        case MemorySize => {
          val size = State.memorySize
          State.pushStack(I32(size))
          k(ss.copy(stackPtr = ss.stackPtr + 1), kk)
        }
        case MemoryGrow => {
          val delta = State.popStack
          val oldSize = State.memorySize
          State.memoryGrow(delta)
          val newSize = State.memorySize
          if (newSize == oldSize + delta) {
            State.pushStack(I32(oldSize))
          } else {
            State.pushStack(I32(-1))
          }
          k(ss, kk)
        }
        case MemoryFill => {
          val (value, offset, length) = (State.stackAt(0), State.stackAt(1), State.stackAt(2))
          State.memoryFill(value, offset, length)
          k(ss.copy(stackPtr = ss.stackPtr - 3), kk)
        }
        case MemoryCopy => {
          val (src, dst, length) = (State.stackAt(0), State.stackAt(1), State.stackAt(2))
          State.memoryCopy(src, dst, length)
          k(ss.copy(stackPtr = ss.stackPtr - 3), kk)
        }

        // Numeric Instructions
        case Konst(I32C(n)) => 
          State.pushStack(I32(n))
          k(ss.copy(stackPtr = ss.stackPtr + 1), kk)
        case Binary(op) => {
          val (v2, v1) = (State.stackAt(0), State.stackAt(1))
          val res = evalBinOp(op, v1, v2)
          State.pushStack(res)(ss.copy(stackPtr = ss.stackPtr - 2))
          k(ss.copy(stackPtr = ss.stackPtr - 1), kk)
        }
        case Unary(op) => {
          val v = State.popStack
          val res = evalUnaryOp(op, v)
          State.pushStack(res)(ss.copy(stackPtr = ss.stackPtr - 1))
          k(ss, kk)
        }
        case Test(testOp) => {
          val v = State.popStack
          State.pushStack(evalTestOp(testOp, v))(ss.copy(stackPtr = ss.stackPtr - 1))
          k(ss, kk)
        }
        case Compare(op) => {
          val (v2, v1) = (State.stackAt(0), State.stackAt(1))
          val res = evalRelOp(op, v1, v2)
          State.pushStack(res)(ss.copy(stackPtr = ss.stackPtr - 2))
          k(ss.copy(stackPtr = ss.stackPtr - 1), kk)
        }
        case Store(StoreOp(align, offset, tipe, packSize)) => {
          val (value, addr) = (State.stackAt(0), State.stackAt(1))
          ???
          k(ss.copy(stackPtr = ss.stackPtr - 2), kk)
        }
        case Load(LoadOp(align, offset, tipe, packSize, extension)) => {
          val addr: Rep[Int] = State.stackAt(0)
          ???
          k(ss.copy(stackPtr = ss.stackPtr + 1), kk)
        }

        // Control Instructions
        // https://www.w3.org/TR/wasm-core-2/exec/instructions.html#numeric-instructions
        case Nop => k(ss, kk)
        case Unreachable => panic("unreachable")

        case Return => {
          // TODO: update sp depending on retN
          State.removeStackRange(ss.stackPtr - ss.retN.get, ss.stackPtr)
          ss.returnLabel match {
            case Some(cont) => cont(())
            case None => throw new Exception("return outside function")
          }
        }
        case Br(label) => {
          val cont = ss.labels(label)
          cont(ss)(())
        }
        case BrIf(label) => {
          val cond: Rep[Int] = State.popStack
          val zero: Rep[Int] = 0
          if (cond == zero) {
            k(ss.copy(stackPtr = ss.stackPtr - 1), kk)
          } else {
            val cont = ss.labels(label)
            cont(ss.copy(stackPtr = ss.stackPtr - 1))(())
          }
        }
      }
    }

    def execInstrs(instrs: List[Instr], k: SSCont)(implicit ss: StaticState): Rep[Unit] = instrs match {
      case Nil => k(ss)(())
      case If(blockTy, thenInstrs, elseInstrs) :: rest => {
        val cond: Rep[Int] = State.popStack
        val zero: Rep[Int] = 0

        // TODO: sp should be updated somehow based on blockTy
        val endK = (endSS: StaticState) => topFun { (_: Rep[Unit]) => 
          execInstrs(rest, k)(ss.copy(stackPtr = endSS.stackPtr))
        }

        if (cond == zero) {
          execInstrs(elseInstrs, k)(ss.copy(labels = endK :: ss.labels))
        } else {
          execInstrs(thenInstrs, k)(ss.copy(labels = endK :: ss.labels))
        }
      }
      case Block(blockTy, blockInstrs) :: rest => {
        // continuation for after the block ends
        // TODO: sp should be updated somehow based on blockTy
        val blockK = (blockSS: StaticState) => topFun { (_: Rep[Unit]) => 
          execInstrs(rest, k)(ss.copy(stackPtr = blockSS.stackPtr))
        }

        execInstrs(blockInstrs.toList, blockK)(ss.copy(blockK :: ss.labels))
      }
      case Loop(blockTy, loopInstrs) :: rest => {
        // continuation for after the loop breaks
        // TODO: sp should be updated somehow based on blockTy
        val loopK = (loopDoneSS: StaticState) => topFun { (_: Rep[Unit]) => 
          execInstrs(rest, k)(ss)
        }

        // the actual loop function
        def loopFn: SSCont = (loopSS: StaticState) => topFun { (_: Rep[Unit]) =>
          execInstrs(loopInstrs.toList, loopFn)(ss.copy(loopFn :: loopK :: ss.labels))
        }
        loopFn(ss)(())
      }
      case Call(func) :: rest => {
        val FuncDef(name, funcType, locals, body) = module.funcs(func)

        // can't use fun because of state scoping, could probably be fixed just by making
        // all the state methods global in cpp even if they're methods in scala
        def compileFun(argNum: Int, retNum: Int, body: List[Instr]): Rep[Cont => Unit] = topFun { (k: Rep[Cont]) =>
          for (local <- locals) {
            State.pushStack(I32(0)) // TODO: default values for other types
          }
          val newSP = ss.stackPtr + locals.length
          val newSS = ss.copy(
            returnLabel = Some(k), stackPtr = newSP, localPtr = ss.stackPtr - funcType.inps.length, retN = Some(retNum)
          )
          val finK = (funSS: StaticState) => topFun { (_: Rep[Unit]) =>
            if (ss.stackPtr + funcType.out.length != funSS.stackPtr) {
              State.removeStackRange(ss.stackPtr + funcType.out.length, funSS.stackPtr)(funSS)
            }
            State.removeStackRange(ss.stackPtr - funcType.inps.length, ss.stackPtr)(funSS)
            k(())
          }
          execInstrs(body, finK)(newSS)
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
          execInstrs(rest, k)(ss.copy(stackPtr = ss.stackPtr - funcType.inps.length + funcType.out.length))
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
    case Node(s, "state-init", List(memory, globals), _) => 
      emit("init_state("); 
      shallow(memory); emit(", "); shallow(globals)
      emit(")")
    case Node(s, "state-memory", List(state), _) => shallow(state); emit(".memory")
    case Node(s, "state-globals", List(state), _) => shallow(state); emit(".globals")
    case Node(s, "state-stack", List(state), _) => shallow(state); emit(".stack")
    case Node(s, "static-state-stack-at", List(i), _) =>
      emit("global_state.stack_at("); shallow(i); emit(")")
    case Node(s, "static-state-push-stack", List(v, sp), _) => 
      emit("global_state.push_stack("); shallow(v); emit(", "); shallow(sp); emit(")")
    case Node(s, "static-state-peek-stack", List(sp), _) => 
      emit("global_state.peek_stack("); shallow(sp); emit(")")
    case Node(s, "static-state-print-stack", List(sp), _) =>
      emit("global_state.print_stack("); shallow(sp); emit(")")
    case Node(s, "static-state-get-local", List(i), _) =>
      emit("global_state.get_local("); shallow(i); emit(")")
    case Node(s, "static-state-set-local", List(i, v), _) => 
      emit("global_state.set_local("); shallow(i); emit(", "); shallow(v); emit(")")
    case Node(s, "static-state-remove-stack-range", List(st, ed, sp), _) => 
      emit("global_state.remove_stack_range("); shallow(st); emit(", "); shallow(ed); emit(", "); shallow(sp); emit(")")
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
        initState(List[Memory](), List[Global]())
        val config = Config(module, HashMap(), 1000)
        val fin = (ss: StaticState) => topFun { (_: Rep[Unit]) => println(ss.localPtr); State.printStack(ss); () }
        config.execInstrs(instrs, fin)(StaticState(Nil, None, 1, 0, None))
      }
    }
  }

  val module = {
    val file = scala.io.Source.fromFile("./benchmarks/wasm/test.wat").mkString
    gensym.wasm.parser.Parser.parseString(file)
  }

  val moduleInst = {
    val types = List()
    val funcs = module.definitions.collect({
      case fndef@FuncDef(_, _, _, _) => fndef
    }).toList
    ModuleInstance(List(), funcs)
  }
  val instrs =
    module.definitions.find({
      case FuncDef("$real_main", _, _, _) => true
      case _ => false
    }).get.asInstanceOf[FuncDef].body.toList
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
  //   // Call(0),
  //   // Call(0),
  // )
  val snip = mkVMSnippet(moduleInst, instrs)
  val code = snip.code
  println(code)
  snip.eval(0)
}
