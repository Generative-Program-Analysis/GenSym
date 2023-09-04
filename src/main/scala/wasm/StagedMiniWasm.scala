package gensym.wasm.miniwasm.staged

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
    case Block(ty, instrs) => IdBlock(BlockIds.next, ty, idBlocks(instrs.toList))
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
  object StaticState {
    var numLocals: Int = 0
    var frameStackPtr: Int = 0
  }

  trait State
  object State {
    def pushStack(value: Rep[Value]) =
      "static-state-push-stack".reflectWriteWith(value)(Adapter.CTRL)
    def stackAt(i: Int): Rep[Value] = 
      Wrap[Value](Adapter.g.reflectWrite("static-state-stack-at", Unwrap(i))(Adapter.CTRL))
    def popStack: Rep[Value] =
      Wrap[Value](Adapter.g.reflectWrite("static-state-pop-stack")(Adapter.CTRL))
    def peekStack: Rep[Value] =
      Wrap[Value](Adapter.g.reflectWrite("static-state-peek-stack")(Adapter.CTRL))
    def returnFromFun(numLocals: Int, numReturns: Int): Rep[Unit] =
      Wrap[Unit](Adapter.g.reflectWrite("static-state-return", Unwrap(numLocals), Unwrap(numReturns))(Adapter.CTRL))
    def bumpFramePtr(): Rep[Unit] =
      Wrap[Unit](Adapter.g.reflectWrite("static-state-bump-frame-ptr")(Adapter.CTRL))
    def getFramePtr: Rep[Int] =
      Wrap[Int](Adapter.g.reflectWrite("static-state-get-frame-ptr")(Adapter.CTRL))
    def setFramePtr(fp: Rep[Int]): Rep[Unit] =
      Wrap[Unit](Adapter.g.reflectWrite("static-state-set-frame-ptr", Unwrap(fp))(Adapter.CTRL))
    def reverseTopN(n: Int): Rep[Unit] =
      Wrap[Unit](Adapter.g.reflectWrite("static-state-reverse-top-n", Unwrap(n))(Adapter.CTRL))
    def removeStackRange(start: Rep[Int], end: Rep[Int]): Rep[Unit] =
      Wrap[Unit](Adapter.g.reflectWrite(
        "static-state-remove-stack-range", Unwrap(start), Unwrap(end))(Adapter.CTRL
      ))
    def getLocal(i: Rep[Int]): Rep[Value] =
      Wrap[Value](Adapter.g.reflectWrite("static-state-get-local", Unwrap(-StaticState.numLocals + i))(Adapter.CTRL))
    def setLocal(i: Rep[Int], v: Rep[Value]) =
      Adapter.g.reflectWrite("static-state-set-local", Unwrap(-StaticState.numLocals + i), Unwrap(v))(Adapter.CTRL)

    def printStack = 
      Adapter.g.reflectWrite("static-state-print-stack")(Adapter.CTRL)

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

  def initState(memory: Rep[List[Memory]], globals: Rep[List[RTGlobal]], numLocals: Int): Rep[State] =
    Wrap[State](Adapter.g.reflectWrite("state-init", Unwrap(memory), Unwrap(globals), Unwrap(numLocals))(Adapter.CTRL))

  def panic(msg: Rep[String]): Rep[Unit] =
    Wrap[Unit](Adapter.g.reflectWrite("panic", Unwrap(msg))(Adapter.CTRL))

  def reverse[M: Manifest](ls: Rep[List[M]]): Rep[List[M]] = Wrap[List[M]](Adapter.g.reflect("reverse-ls", Unwrap(ls)))

  trait Value
  def I32(i: Rep[Int]): Rep[Value] = Wrap[Value](Adapter.g.reflectWrite("I32V", Unwrap(i))(Adapter.CTRL))
  def I64(i: Rep[Long]): Rep[Value] = Wrap[Value](Adapter.g.reflectWrite("I64V", Unwrap(i))(Adapter.CTRL))

  implicit class IValueOps(i: Rep[Value]) {
    def int: Rep[Int] = Unwrap(i) match {
      case Adapter.g.Def("I32V", scala.collection.immutable.List(v: Backend.Exp)) => Wrap[Int](v)
      case _ => Wrap[Int](Adapter.g.reflect("I32V-proj", Unwrap(i)))
    }
  }

  def evalBinOp(op: BinOp, lhsV: Rep[Value], rhsV: Rep[Value]): Rep[Value] = op match {
    // assume I32
    case Add(_) => I32(lhsV.int + rhsV.int)
    case Sub(_) => I32(lhsV.int - rhsV.int)
    case Mul(_) => I32(lhsV.int * rhsV.int)
  }
  def evalUnaryOp(op: UnaryOp, value: Rep[Value]): Rep[Value] = ???
  def evalRelOp(op: RelOp, lhs: Rep[Value], rhs: Rep[Value]): Rep[Value] = ???
  def evalTestOp(op: TestOp, value: Rep[Value]): Rep[Value] = op match {
    case Eqz(_) => if (value.int == 0) I32(1) else I32(0)
  }

  type Cont = Unit => Unit

  def evalInsts(insts: List[Instr], ret: Rep[Cont], trail: List[Rep[Cont]]): Unit = {
    if (insts.isEmpty) return trail.head()
    
    val inst = insts.head
    val rest = insts.tail

    inst match {
      case Drop =>
        State.popStack
        evalInsts(rest, ret, trail)
      case Select(_) =>
        val (cond, v2, v1) = (State.popStack, State.popStack, State.popStack)
        State.pushStack(if (cond.int == 0) v2 else v1)
        evalInsts(rest, ret, trail)
      case LocalGet(i) =>
        val v = State.getLocal(i)
        State.pushStack(v)
        evalInsts(rest, ret, trail)
      case LocalSet(i) =>
        val v = State.popStack
        State.setLocal(i, v)
        evalInsts(rest, ret, trail)
      case LocalTee(i) =>
        val v = State.peekStack
        State.setLocal(i, v)
        evalInsts(rest, ret, trail)
      case GlobalGet(i) => 
        val v = State.getGlobal(i)
        State.pushStack(v)
        evalInsts(rest, ret, trail)
      case GlobalSet(i) =>
        val v = State.popStack
        State.setGlobal(i, v)
        evalInsts(rest, ret, trail)
      case MemorySize => ???
      case MemoryGrow => ???
      case MemoryFill => ???
      case MemoryCopy => ???
      case Konst(I32V(n)) => // FIXME: could be other const
        State.pushStack(I32(n))
        evalInsts(rest, ret, trail)
      case Binary(op) => 
        val (v2, v1) = (State.popStack, State.popStack)
        State.pushStack(evalBinOp(op, v1, v2))
        evalInsts(rest, ret, trail)
      case Unary(op) =>
        val v = State.popStack
        State.pushStack(evalUnaryOp(op, v))
        evalInsts(rest, ret, trail)
      case Compare(op) =>
        val (v2, v1) = (State.popStack, State.popStack)
        State.pushStack(evalRelOp(op, v1, v2))
        evalInsts(rest, ret, trail)
      case Test(op) =>
        val v = State.popStack
        State.pushStack(evalTestOp(op, v))
        evalInsts(rest, ret, trail)
      case Store(StoreOp(align, offset, ty, None)) => ???
      case Load(LoadOp(align, offset, ty, None, None)) => ???
      case Nop => evalInsts(rest, ret, trail)
      case Unreachable => panic("unreachable")
      case Block(ty, inner) => ???
      case Loop(ty, inner) => ???
      case If(ty, thn, els) => ???
      case IdBlock(id, ty, inner) =>
        val thunk: Rep[Unit] => Rep[Unit] = (u: Rep[Unit]) => {
          //retStack.take(ty.toList.size) ++ stack,
          evalInsts(rest, ret, trail)
        }
        val repThunk = topFun(thunk)
        // TODO: block can take inputs too
        val blockFun = (u: Rep[Unit]) => {
          evalInsts(inner, ret, repThunk::trail)
        }
        ???
      case IdLoop(id, ty, inner) => ???
      case IdIf(ty, thn, els) => ???
      case Br(label) =>
        trail(label)()
      case BrIf(label) =>
        if (State.popStack.int == 0) evalInsts(rest, ret, trail)
        else trail(label)()
      case Return => ret()
      case Call(f) => ???
      case _ => ???
    }
  }





  type SSCont = StaticState => Rep[Cont]

  trait Label {
    def apply(ss: StaticState): Rep[Unit]
  }
  case class ContLabel(k: SSCont) extends Label {
    def apply(ss: StaticState): Rep[Unit] = k(ss)(())
  }
  case class StringLabel(name: String) extends Label {
    def apply(ss: StaticState): Rep[Unit] = 
      Wrap[Unit](Adapter.g.reflectWrite("goto-label", Unwrap(name))(Adapter.CTRL))
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
    Wrap[Unit](Adapter.g.reflectWrite(s"call-fun-ret-cont", Unwrap(name))(Adapter.CTRL))
  def getFunRetCont(name: String): Rep[Cont] =
    Wrap[Cont](Adapter.g.reflect(s"get-fun-ret-cont", Unwrap(name)))
  def setFunRetCont(name: String, cont: Rep[Cont]) = {
    funRetConts += name
    Adapter.g.reflectWrite("set-fun-ret-cont", Unwrap(name), Unwrap(cont))(Adapter.CTRL)
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
    Wrap[Unit](Adapter.g.reflectWrite(s"call-fun-fun", Unwrap(funName), Unwrap(k))(Adapter.CTRL))

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
      emit(s"fun_ret_cont_${name.toString.drop(1)} = "); shallow(cont); emit(";")
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
    case Node(s, "reverse-ls", List(ls), _) => emit("flex_vector_reverse("); shallow(ls); emit(")")
    case Node(s, "I32V", List(i), _) => emit("I32V("); shallow(i); emit(")")
    case Node(s, "I32V-proj", List(i), _) => shallow(i); emit(".i32")
    case Node(s, "state-new", List(memory, globals, stack), _) => 
      emit("State("); 
      shallow(memory); emit(", "); shallow(globals); emit(", "); shallow(stack); 
      emit(")")
    case Node(s, "state-init", List(memory, globals, numLocals), _) => 
      emit("init_state("); 
      shallow(memory); emit(", "); shallow(globals); emit(", "); shallow(numLocals);
      emit(")")
    case Node(s, "state-memory", List(state), _) => shallow(state); emit(".memory")
    case Node(s, "state-globals", List(state), _) => shallow(state); emit(".globals")
    case Node(s, "state-stack", List(state), _) => shallow(state); emit(".stack")
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
      emit("global_state.set_local("); shallow(i); emit(", "); shallow(v); emit(")")
    case Node(s, "static-state-remove-stack-range", List(st, ed), _) => 
      emit("global_state.remove_stack_range("); shallow(st); emit(", "); shallow(ed); emit(")")
    case Node(s, "static-state-reverse-top-n", List(n), _) => 
      emit("global_state.reverse_top_n("); shallow(n); emit(")")
    case Node(s, "static-state-return", List(numLocals, retN), _) => 
      emit("global_state.return_from_fun("); shallow(numLocals); emit(", "); shallow(retN); emit(")")
    case Node(s, "static-state-bump-frame-ptr", List(), _) => 
      emit("global_state.bump_frame_ptr()")
    case Node(s, "static-state-get-frame-ptr", List(), _) => 
      emit("global_state.get_frame_ptr()")
    case Node(s, "static-state-set-frame-ptr", List(fp), _) => 
      emit("global_state.set_frame_ptr("); shallow(fp); emit(")")
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

    override def emitAll(g: Graph, name: String)(m1: Manifest[_], m2: Manifest[_]): Unit = {
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
        emitln(s"std::function<std::monostate (std::monostate)> fun_ret_cont_${f.drop(1)};");
      }
      emitFunctionDecls(stream)
      for ((n, f) <- q.funFunsGlobal) {
        val innerType = "std::function<std::monostate (std::monostate)>"
        emit(s"std::function<std::monostate ($innerType)> fun_fun_${n.drop(1)}");
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
        /*
        // val state = State(List[Memory](), List[Global](), List[Value](I32(0)))
        val config = Config(module, HashMap(), HashMap(), HashMap(), 1000)
        val fin = (ss: StaticState) => topFun { (_: Rep[Unit]) => println(ss.numLocals); State.printStack; () }
        initState(List[Memory](), List[RTGlobal](), 0)
        val ss = StaticState(Nil, None, 0, 0)
        for (FuncDef(name, FuncBodyDef(tipe, _, locals, body)) <- module.funcs) {
          config.compileFun(name.get, tipe, locals, body)
        }
        //config.compileStuffIn(instrs, fin)(ss) // not necessary
        System.out.println(s"blockConts: ${config.blockConts}")
        config.execInstrs(instrs, fin)(ss)
         */
        ???
      }
    }
  }

  val module = {
    val file = scala.io.Source.fromFile("./benchmarks/wasm/test.wat").mkString
    gensym.wasm.parser.Parser.parse(file)
  }

  val moduleInst = {
    val types = List()
    val funcs = module.definitions.collect({
      case FuncDef(name, FuncBodyDef(ty, nms, locals, body)) =>
        FuncDef(name, FuncBodyDef(ty, nms, locals, Preprocess.idBlocks(body)))
    }).toList
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
    module.definitions.find({
      case FuncDef(Some("$real_main"), _) => true
      case _ => false
    }).get.asInstanceOf[FuncDef].f.asInstanceOf[FuncBodyDef].body // Super unsafe...
  System.out.println(s"funcs: ${moduleInst.funcs}")
  val snip = mkVMSnippet(moduleInst, Preprocess.idBlocks(instrs))
  val code = snip.code
  println(code)
  snip.eval(0)
}
