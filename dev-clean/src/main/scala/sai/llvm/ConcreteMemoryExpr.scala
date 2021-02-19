package sai.llvm

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._

import org.antlr.v4.runtime._
import scala.collection.JavaConverters._

import scala.collection.mutable
import scala.collection.immutable.Nil

/* Another imperative implementation of LLVM IR concrete execution.
 * Different than the `ConcreteLLVM.scala` implementation,
 * this file uses mutatable arrays to represent memory objects.
 */

object ConcExecMemory {
  type Store = Map[String, Value]
  type Stack = List[Frame]

  case class ExitException(value: Value) extends RuntimeException(value.toString)

  var curStack: Stack = List()
  def push(frame: Frame): Unit = {
    curStack = frame :: curStack
  }
  def pop: Unit = {
    curStack = curStack.tail
  }
  def curFrame: Frame = curStack.head
  var branches: List[String] = List()

  var funDeclMap: Map[String, FunctionDecl] = Map()
  var funMap: Map[String, FunctionDef] = Map()
  var globalDefMap: Map[String, GlobalDef] = Map()

  trait Memory {
    def apply(x: Loc): Value
    def update(x: Loc, v: Value): Unit
  }

  object Heap extends Memory {
    val heap: mutable.ArrayBuffer[Value] = mutable.ArrayBuffer.empty
    val env: mutable.Map[String, Int] = mutable.Map.empty

    def update(x: String, v: Value): Unit = {
      env(x) = heap.length
      v match {
        case ArrayValue(ty, vs) => heap ++= flattenArray(v)
        case _ => heap.append(v)
      }
    }
    def update(x: Loc, v: Value): Unit = x match { case HeapLoc(i) => heap(i) = v }
    def apply(x: String): Value = heap(env(x))
    def apply(x: Loc): Value = x match { case HeapLoc(i) => heap(i) }
    def getLoc(x: String): HeapLoc = HeapLoc(env(x))
  }

  case class Frame(fname: String, initStore: Store = Map.empty) extends Memory {
    val addressMap: mutable.Map[String, Int] = mutable.Map.empty
    val store: mutable.ArrayBuffer[Value] = mutable.ArrayBuffer.empty

    initStore foreach {case (k, v) => update(k, v)}

    def alloca(n: Int): FrameLoc = {
      val addr = store.size
      store ++= mutable.ArrayBuffer.fill(n)(BotValue)
      FrameLoc(addr, this)
    }
    
    def apply(x: Loc): Value = x match { case FrameLoc(i, _) => store(i) }
    def apply(x: String): Value = apply(FrameLoc(addressMap(x), this))
    def update(x: Loc, v: Value): Unit = x match { case FrameLoc(i, _) => store(i) = v }
    def update(x: String, v: Value): Unit = {
      addressMap.update(x, store.size)
      store.append(v)
      /* It seems that there won't be updating LocalId with array value
      v match {
        case ArrayValue(ty, vs) =>
          store ++= flattenArray(v)
        case _ =>
          store.append(v)
      } */
    }
  }

  abstract class Loc {
    def mem: Memory
    def inc: Loc
    def +(i: Int): Loc
  }
  case class HeapLoc(i: Int) extends Loc {
    def mem: Memory = Heap
    def inc: Loc = HeapLoc(i+1)
    def +(j: Int): Loc = HeapLoc(i + j)
  }
  case class FrameLoc(i: Int, frame: Frame) extends Loc {
    def mem: Memory = frame
    def inc: Loc = FrameLoc(i+1, frame)
    def +(j: Int): Loc = FrameLoc(i + j, frame)
  }
  
  abstract class Value
  case object BotValue extends Value
  case class IntValue(x: Int) extends Value
  case class LocValue(loc: Loc) extends Value
  // ArrayValue is the outside view of Array, while inside memory, we have
  // Value | ... | Value 
  case class ArrayValue(ty: ArrayType, vs: List[Value]) extends Value
  case class FunValue(id: String, params: List[String]) extends Value {
    def apply(args: List[Value]): Value = {
      params.zip(args).foreach { case (k, v) => curFrame(k) = v }
      execBlock(findFirstBlock(id)) match {
        case None => VoidValue
        case Some(value) => value
      }
    }
  }
  case object VoidValue extends Value

  def flattenArray(v: Value): mutable.ArrayBuffer[Value] = v match {
    case ArrayValue(ty, vs) =>
      val res: mutable.ArrayBuffer[Value] = mutable.ArrayBuffer.empty
      vs.foreach(elm => res ++= flattenArray(elm))
      res
    case _ => mutable.ArrayBuffer(v)
  }

  def getTySize(vt: LLVMType, align : Int = 1): Int = vt match {
    case ArrayType(size, ety) =>
      val rawSize = size * getTySize(ety, align)
      if (rawSize % align == 0) rawSize
      else (rawSize / align + 1) * align
    case _ => 1
  }

  def getStrFromMemory(loc: Loc): String = {
    var tmp: Loc = loc
    var res: String = ""
    while (loc.mem(tmp) != IntValue(0)) {
      res = res + loc.mem(tmp).asInstanceOf[IntValue].x.toChar
      tmp = tmp.inc
    }
    res
  }

  object Primitives {
    def printf(args: List[Value]): Value = {
      var varArgs = args.tail
      val LocValue(loc) = args.head
      var patternS = getStrFromMemory(loc)
      for (i <- 0 until (patternS.length - 1)) {
        patternS.slice(i, i + 2) match {
          case "%c" => {
            val replacement = varArgs.head.asInstanceOf[IntValue].x.toChar.toString
            varArgs = varArgs.tail
            patternS = patternS.replaceFirst("%c", replacement)
          }
          case "%d" => {
            val replacement = varArgs.head.asInstanceOf[IntValue].x.toString
            varArgs = varArgs.tail
            patternS = patternS.replaceFirst("%d", replacement)
          }
          case "%s" => {
            val LocValue(loc) = varArgs.head
            varArgs = varArgs.tail
            patternS = patternS.replaceFirst("%s", getStrFromMemory(loc))
          }
          case "%4" => {
            val LocValue(loc) = varArgs.head
            varArgs = varArgs.tail
            patternS = patternS.replaceFirst("%42s", getStrFromMemory(loc))
          }
          case _ => ()
        }
      }
      print(patternS)
      VoidValue
    }

    def read(args: List[Value]): Value = {
      //FIXME: args(0) is not handled
      val LocValue(start) = args(1)
      val IntValue(len) = args(2)
      // FIXME: This would corrupt the memory
      val rawInput = "ssssddddwwaawwddddssssddwwww"
      val inputStr = rawInput.take(Math.min(len, rawInput.length))
      for (i <- 0 until inputStr.length) {
        start.mem(start + i) = IntValue(inputStr(i).toInt)
      }
      start.mem(start + inputStr.length) = IntValue(0)
      VoidValue
      //eval(CharArrayConst("ssssddddwwaawwddddssssddwwww"))
    }
  }

  def eval(v: LLVMValue): Value = {
    v match {
      case LocalId(x) => curFrame(x)
      case IntConst(n) => IntValue(n)
      case BoolConst(b) => if (b) IntValue(1) else IntValue(0)
      case ArrayConst(cs) =>
        ArrayValue(ArrayType(cs.length, cs.head.ty), cs.map(v => eval(v.const)))
      case CharArrayConst(s) =>
        // TODO need to be modified in parser
        ArrayValue(ArrayType(s.length, IntType(8)), s.map(c => IntValue(c.toInt)).toList)
      case ZeroInitializerConst => IntValue(0)
      case GlobalId(id) if funMap.contains(id) =>
        val funDef = funMap(id)
        val paramNames: List[String] = funDef.header.params.map {
          case TypedParam(ty, attrs, localId) => localId.get
          case Vararg => ???
        }
        FunValue(id, paramNames)
      case GlobalId(id) if funDeclMap.contains(id) =>
        new FunValue(id, List()) {
          override def apply(args: List[Value]): Value = id match {
            case "@printf" => Primitives.printf(args)
            case "@read" => Primitives.read(args)
            case "@exit" => throw ExitException(args(0))
            case "@sleep" => VoidValue
          }
        }
      case GlobalId(id) if globalDefMap.contains(id) => Heap(id)
      case GlobalId(id) =>
        throw new RuntimeException("Cannot evaluate global id " + id)
      case BitCastExpr(from, const, to) =>
        eval(const)
      case GetElemPtrExpr(_, baseTy, ptrTy, ptrVal, typedValues) =>
        val index = typedValues.map(v => eval(v.const).asInstanceOf[IntValue].x)
        val offset = calculateOffset(ptrTy, index)
        ptrVal match {
          case GlobalId(id) => LocValue(Heap.getLoc(id) + offset)
          case _ =>
            val LocValue(l) = eval(ptrVal)
            LocValue(l + offset)
        }
    }
  }

  def execInst(inst: Instruction): Unit = {
    inst match {
      case AssignInst(x, valInst) =>
        curFrame(x) = execValueInst(valInst)
      case StoreInst(ty1, val1, ty2, val2, align) =>
        val v1 = eval(val1)
        eval(val2) match {
          case LocValue(l@FrameLoc(_, m)) => m(l) = v1
          case LocValue(l@HeapLoc(_)) => Heap(l) = v1
        }
      case CallInst(ty, f, args) =>
        val fun@FunValue(fid, _) = eval(f)
        val argValues: List[Value] = args.map {
          case TypedArg(ty, attrs, value) => eval(value)
        }
        push(Frame(fid))
        fun(argValues)
        pop
    }
  }

  def findBlock(fname: String, lab: String): Option[BB] = {
    funMap.get(fname).get.lookupBlock(lab)
  }

  def findFirstBlock(fname: String): BB = {
    findFundef(fname).body.blocks(0)
  }

  def findFundef(fname: String) = funMap.get(fname).get

  def calculateOffset(ty: LLVMType, index: List[Int]): Int = {
    if (index.isEmpty) 0 else ty match {
      case PtrType(ety, addrSpace) =>
        index.head * getTySize(ety) + calculateOffset(ety, index.tail)
      case ArrayType(size, ety) =>
        index.head * getTySize(ety) + calculateOffset(ety, index.tail)
      case _ => ???
    }
  }

  def execTerm(inst: Terminator): Option[Value] = {
    inst match {
      case RetTerm(ty, Some(value)) => Some(eval(value))
      case RetTerm(ty, None) => None
      case BrTerm(lab) =>
        val Some(b) = findBlock(curFrame.fname, lab)
        branches = lab :: branches
        execBlock(b)
      case CondBrTerm(ty, cnd, thnLab, elsLab) =>
        val IntValue(v) = eval(cnd)
        if (v == 1) {
          val Some(b) = findBlock(curFrame.fname, thnLab)
          branches = thnLab :: branches
          execBlock(b)
        } else {
          val Some(b) = findBlock(curFrame.fname, elsLab)
          branches = elsLab :: branches
          execBlock(b)
        }
      case SwitchTerm(cndTy, cndVal, default, table) =>
        val IntValue(i) = eval(cndVal)
        val matchCase = table.filter(_.n == i)
        if (matchCase.isEmpty) {
          val Some(b) = findBlock(curFrame.fname, default)
          branches = default :: branches
          execBlock(b)
        } else {
          val Some(b) = findBlock(curFrame.fname, matchCase.head.label)
          branches = matchCase.head.label :: branches
          execBlock(b)
        }
      case Unreachable => throw new RuntimeException("Unreachable")
    }
  }

  def execValueInst(inst: ValueInstruction): Value = {
    inst match {
      case AllocaInst(ty, align) =>
        LocValue(curFrame.alloca(getTySize(ty, align.n)))
      case LoadInst(valTy, ptrTy, value, align) =>
        eval(value) match {
          case LocValue(l@FrameLoc(_, m)) => m(l)
          case LocValue(l@HeapLoc(_)) => Heap(l)
        }
      case GetElemPtrInst(_, baseTy, ptrTy, ptrVal, typedValues) =>
        // NOTE: typedValues will contain an "extra" parameter compares to C
        // why? see https://llvm.org/docs/GetElementPtr.html#why-is-the-extra-0-index-required
        val index = typedValues.map(v => eval(v.value).asInstanceOf[IntValue].x)
        val offset = calculateOffset(ptrTy, index)
        ptrVal match {
          case GlobalId(id) => LocValue(Heap.getLoc(id) + offset)
          case _ =>
            val LocValue(l) = eval(ptrVal)
            LocValue(l + offset)
        }
      case AddInst(ty, lhs, rhs, _) =>
        val IntValue(v1) = eval(lhs)
        val IntValue(v2) = eval(rhs)
        IntValue(v1 + v2)
      case MulInst(ty, lhs, rhs, _) =>
        val IntValue(v1) = eval(lhs)
        val IntValue(v2) = eval(rhs)
        IntValue(v1 * v2)
      case SubInst(ty, lhs, rhs, _) =>
        val IntValue(v1) = eval(lhs)
        val IntValue(v2) = eval(rhs)
        IntValue(v1 - v2)
      case ICmpInst(pred, ty, lhs, rhs) =>
        val IntValue(v1) = eval(lhs)
        val IntValue(v2) = eval(rhs)
        pred match {
          case IPredicate("eq")  => IntValue(if (v1 == v2) 1 else 0)
          case IPredicate("neq")  => IntValue(if (v1 != v2) 1 else 0)
          case IPredicate("slt")  => IntValue(if (v1 < v2) 1 else 0)
          case IPredicate("sle")  => IntValue(if (v1 <= v2) 1 else 0)
          case IPredicate("sgt")  => IntValue(if (v1 > v2) 1 else 0)
          case IPredicate("sge") => IntValue(if (v1 >= v2) 1 else 0)
          case IPredicate("ult")  => IntValue(if (v1 < v2) 1 else 0)
          case IPredicate("ule")  => IntValue(if (v1 <= v2) 1 else 0)
          case IPredicate("ugt")  => IntValue(if (v1 > v2) 1 else 0)
          case IPredicate("uge")  => IntValue(if (v1 >= v2) 1 else 0)
        }
      case ZExtInst(from, value, to) => eval(value) match {
        case IntValue(x) => IntValue(x)
      }
      case SExtInst(from, value, to) => eval(value) match {
        case BotValue => IntValue(0)
        case IntValue(x) => IntValue(x)
      }
      case CallInst(ty, f, args) =>
        val fun@FunValue(fid, _) = eval(f)
        val argValues: List[Value] = args.map {
          case TypedArg(ty, attrs, value) => eval(value)
        }
        push(Frame(fid))
        val ret = fun(argValues)
        pop
        ret
      case PhiInst(ty, incomings) => 
        val lastBr = branches.tail.head
        eval((incomings filter(inc => inc.label == lastBr)).head.value)
      case SelectInst(cndTy, cndVal, thnTy, thnVal, elsTy, elsVal) =>
        if (eval(cndVal) == IntValue(1)) eval(thnVal) else eval(elsVal)
    }
  }

  def execBlock(bb: BB): Option[Value] = {
    val insts = bb.ins
    val term = bb.term
    for (i <- insts) {
      execInst(i)
    }
    execTerm(term)
  }

  def exec(m: Module, fname: String, initStore: => Store): Option[Value] = {
    val Some(f) = m.lookupFuncDef(fname)
    funMap = m.funcDefMap
    funDeclMap= m.funcDeclMap
    m.globalDefMap foreach {case (s, gDef) =>
      Heap(gDef.id) = eval(gDef.const)
    }

    push(Frame(fname, initStore))
    try {
      execBlock(f.body.blocks(0))
    } catch {
      case ExitException(v) =>
        pop
        Some(v)
    }
  }
}

object TestConcMem {
  import ConcExecMemory._

  def test(m: Module, main: String, mem: Store = Map())(f: Option[Value] => Unit): Unit = {
    val result = ConcExecMemory.exec(m, main, mem)
    println(result)
    f(result)
  }

  def testAdd = test(Benchmarks.add, "@main") {
    case Some(IntValue(3)) =>
  }

  def testArrayAccess = test(Benchmarks.arrayAccess, "@main") {
    case Some(IntValue(4)) =>
  }

  def testArrayAccessLocal = test(Benchmarks.arrayAccessLocal, "@main") {
    case Some(IntValue(4)) =>
  }

  def testArrayGetSet = test(Benchmarks.arrayGetSet, "@main") {
    case Some(IntValue(636)) =>
  }

  def testPower = test(Benchmarks.power, "@main") {
    case Some(IntValue(27)) =>
  }

  def testSinglePath = test(Benchmarks.sp1, "@main"){_ => }

  def testSimpleBranch =
    test(Benchmarks.branch, "@f", Map("%x" -> IntValue(5))){_ => }

  def testMaze =
    test(Benchmarks.maze, "@main", Map(
      "%argc" -> IntValue(5),
      "%argv" -> IntValue(5)
    )){_ => }

  def testMazeNoPhi = {
    val testInput = scala.io.Source.fromFile("llvm/benchmarks/maze_nophi.ll").mkString
    val m = parse(testInput)
    val result = ConcExecMemory.exec(m, "@main", Map(
      "%argc" -> IntValue(5),
      "%argv" -> IntValue(5)
      ))
    println(result)
  }

  def main(args: Array[String]): Unit = {
    // testArrayAccess
    // testArrayGetSet
    // testArrayAccessLocal
    // testAdd
    // testPower
    testMaze
    // testMazeNoPhi
  }
}
