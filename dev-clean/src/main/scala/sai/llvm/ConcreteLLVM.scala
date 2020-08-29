package sai.llvm

import sai.lang.llvm._
import sai.lang.llvm.IR._

import org.antlr.v4.runtime._
import scala.collection.JavaConverters._

import scala.collection.mutable
import scala.collection.immutable.Nil

// An imperative implementation of concrete execution
object Debug {
  val debug = false
}

object ConcExec {

  var funDeclMap: Map[String, FunctionDecl] = Map()
  var funMap: Map[String, FunctionDef] = Map()
  var globalDefMap: Map[String, GlobalDef] = Map()

  val heap: mutable.Map[Loc, Value] = mutable.Map.empty

  var curStack: Stack = List()
  def push(frame: Frame): Unit = { curStack = frame :: curStack }
  def pop: Unit = { curStack = curStack.tail }
  def curFrame: Frame = curStack.head

  class Frame(val fname: String, val initStore: Store = Map.empty) {
    import scala.collection.mutable.Map
    val store: Map[Loc, Value] = Map(initStore.toSeq:_*)
    def apply(x: Loc): Value = store(x)
    def apply(x: String): Value = store(FrameLoc(x, fname))
    def update(x: Loc, v: Value) = store.update(x, v)
    def update(x: String, v: Value) = store.update(FrameLoc(x, fname), v)
  }

  abstract class Loc
  case class GlobalLoc(x: String) extends Loc
  case class FrameLoc(x: String, frame: String) extends Loc
  case class SpecialLoc(x: String) extends Loc
  // Really ugly, need refined with new memory layout
  case class ArrayLoc(loc: Loc, index: List[IntValue]) extends Loc
  class AllocaLoc(inst: ValueInstruction, frame: Frame) extends Loc

  abstract class Value
  case object BotValue extends Value
  case class IntValue(x: Int) extends Value
  case class LocValue(loc: Loc) extends Value
  case class ArrayValue(vs: mutable.ListBuffer[Value]) extends Value
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
  
  type Store = Map[Loc, Value]
  type Stack = List[Frame]

  def arrayGet(arr: Value, index: List[IntValue]): Value = {
    arr match {
      case ArrayValue(vs) => index match {
        case Nil => arr
        case head :: tl => arrayGet(vs(head.x), tl)
      }
      case _ => arr
    }
  }

  def arrayUpdate(arr: Value, index: List[IntValue], v: Value): Unit = {
    index match {
      case head :: Nil =>
        arr match {
          case ArrayValue(vs) => vs(head.x) = v
          case _ => throw new RuntimeException("Update non-array")
        }
      case head :: tl => 
        arr match {
          case ArrayValue(vs) => arrayUpdate(vs(head.x), tl, v)
          case _ => throw new RuntimeException("Update non-array")
        }
    }
  }

  def eval(v: LLVMValue): Value = {
    v match {
      case LocalId(x) => curFrame(x)
      case IntConst(n) => IntValue(n)
      case GlobalId(id) if (funMap.contains(id)) =>
        val funDef = funMap(id)
        val paramNames: List[String] = funDef.header.params.map {
          case TypedParam(ty, attrs, localId) => localId.get
          case Vararg => ???
        }
        FunValue(id, paramNames)
      case GlobalId(id) if (funDeclMap.contains(id)) =>
        new FunValue(id, List()) {
          override def apply(args: List[Value]): Value = id match {
            // TODO: do the job of the function
            case "print" => VoidValue
          }
        }
      case GlobalId(id) if (globalDefMap.contains(id)) =>
        val globalDef = globalDefMap(id)
        eval(globalDef.const)
      case GlobalId(id) =>
        throw new RuntimeException("Cannot evaluate global id " + id)
      case ArrayConst(cs) => 
        ArrayValue(mutable.ListBuffer.empty ++= cs.map(v => eval(v.const)))
      // FIXME: may not work
      case BitCastExpr(from, const, to) =>
        eval(const)
    }
  }

  def execInst(inst: Instruction): Unit = {
    if (Debug.debug) println(inst)
    inst match {
      case AssignInst(x, valInst) =>
        curFrame(x) = execValueInst(valInst)
      case StoreInst(ty1, val1, ty2, val2, align) =>
        val v1 = eval(val1)
        eval(val2) match {
          case LocValue(l) =>
            //FIXME: store to where depends on l, now assume every allocation happens on the local frame
            // added array cases
            l match {
              case ArrayLoc(loc, index) => {
                val arr = loc match {
                  case GlobalLoc(x) => eval(GlobalId(x))
                  case _ => curFrame(loc)
                }
                arrayUpdate(arr, index, v1)
              }
              case _ => curFrame(l) = v1
            }
        }
      case CallInst(ty, f, args) =>
        //TODO: also need to handle call here
        ???
    }
  }

  def findBlock(fname: String, lab: String): Option[BB] = {
    funMap.get(fname).get.lookupBlock(lab)
  }

  // FIXME: Unsafe methods
  def findFirstBlock(fname: String): BB = {
    findFundef(fname).body.blocks(0)
  }

  def findFundef(fname: String) = funMap.get(fname).get

  def execTerm(inst: Terminator): Option[Value] = {
    inst match {
      case RetTerm(ty, Some(value)) =>
        Some(eval(value))
      case RetTerm(ty, None) => None
      case BrTerm(lab) =>
        val Some(b) = findBlock(curFrame.fname, lab)
        execBlock(b)
      case CondBrTerm(ty, cnd, thnLab, elsLab) =>
        val IntValue(v) = eval(cnd)
        if (v == 1) {
          val Some(b) = findBlock(curFrame.fname, thnLab)
          execBlock(b)
        } else {
          val Some(b) = findBlock(curFrame.fname, elsLab)
          execBlock(b)
        }
      case SwitchTerm(cndTy, cndVal, default, table) => ???
      case Unreachable => ???
    }
  }

  def evalArrayInit(vt: LLVMType): Value = vt match {
    case ArrayType(size, ety) => 
      ArrayValue(mutable.ListBuffer.fill(size)(evalArrayInit(ety)))
    case _ => BotValue
  }

  def execValueInst(inst: ValueInstruction): Value = {
    inst match {
      case AllocaInst(IntType(_), _) =>
        val ptr = new AllocaLoc(inst, curFrame)
        curFrame(ptr) = BotValue
        LocValue(ptr)
      // FIXME: Array Experiment
      case AllocaInst(vt : ArrayType, _) =>
        val ptr = new AllocaLoc(inst, curFrame)
        curFrame(ptr) = evalArrayInit(vt)
        LocValue(ptr)
      case AllocaInst(ArrayType(n, IntType(m)), _) =>
        val size = (n * m) / 8 //bytes
        val ptr = new AllocaLoc(inst, curFrame)
        curFrame(ptr) = ArrayValue(mutable.ListBuffer.fill(n)(BotValue))
        LocValue(ptr)
      case AllocaInst(PtrType(ty, _), _) =>
        val ptr = new AllocaLoc(inst, curFrame)
        curFrame(ptr) = BotValue
        LocValue(ptr)
      case AllocaInst(ty, align) => ???
      case LoadInst(valTy, ptrTy, value, align) =>
        eval(value) match {
          case LocValue(ptr) => ptr match {
            case ArrayLoc(loc, index) => 
              // if we unify the store, then this would be Store(loc)
              val arr = loc match {
                case GlobalLoc(x) => eval(GlobalId(x))
                case _ => curFrame(loc)
              }
              arrayGet(arr, index)
            case _ => curFrame(ptr)
          }
        }
      // getElmPtr Note:
      // typedValues will contain an "extra" parameter compares to C
      // why? see https://llvm.org/docs/GetElementPtr.html#why-is-the-extra-0-index-required
      case GetElemPtrInst(_, baseTy, ptrTy, ptrVal, typedValues) =>
        val indices = typedValues.tail.map(v => eval(v.value).asInstanceOf[IntValue])
        ptrVal match {
          case GlobalId(id) => LocValue(ArrayLoc(GlobalLoc(id), indices))
          case _ => 
            val LocValue(loc) = eval(ptrVal) 
            loc match {
              case FrameLoc(x, frame) => ???
              case ArrayLoc(aloc, index) => 
                LocValue(ArrayLoc(aloc, index ++ indices))
              case alloc: AllocaLoc => 
                LocValue(ArrayLoc(alloc, indices))
            }
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
          case EQ => IntValue(if (v1 == v2) 1 else 0)
          case NE => IntValue(if (v1 != v2) 1 else 0)
          case SLT => IntValue(if (v1 < v2) 1 else 0)
          case SLE => IntValue(if (v1 <= v2) 1 else 0)
          case SGT => IntValue(if (v1 > v2) 1 else 0)
          case SGE => IntValue(if (v1 >= v2) 1 else 0)
          case ULT => IntValue(if (v1 < v2) 1 else 0)
          case ULE => IntValue(if (v1 <= v2) 1 else 0)
          case UGT => IntValue(if (v1 > v2) 1 else 0)
          case UGE => IntValue(if (v1 >= v2) 1 else 0)
        }
      case ZExtInst(from, value, to) => eval(value) match {
        case IntValue(x) => IntValue(x)
        // could also be vector
        case _ => ???
      }
      case SExtInst(from, value, to) => eval(value) match {
        // TODO What does sext mean?
        case IntValue(x) => IntValue(x)
      }
      case CallInst(ty, f, args) =>
        val fun@FunValue(fid, _) = eval(f)
        val argValues: List[Value] = args.map {
          case TypedArg(ty, attrs, value) => eval(value)
        }
        push(new Frame(fid))
        val ret = fun(argValues)
        pop
        ret
      case PhiInst(ty, incomings) => ???
      case SelectInst(cndTy, cndVal, thnTy, thnVal, elsTy, elsVal) => ???
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

  /*
  val mainStore0 = Map(
    FrameLoc("%0", curFrame.fname) -> IntValue(0),
    FrameLoc("%1", curFrame.fname) -> LocValue(SpecialLoc("argv")),
    SpecialLoc("argc") -> ArrayValue(List(IntValue(0))))
   */

  def exec(m: Module, fname: String, initStore: => Store): Option[Value] = {
    val Some(f) = m.lookupFuncDef(fname)
    funMap = m.funcDefMap
    funDeclMap= m.funcDeclMap
    globalDefMap = m.globalDefMap

    push(new Frame(fname, initStore))
    execBlock(f.body.blocks(0))
  }
}

object TestMaze {
  import ConcExec._
  def testNoArg(file: String, main: String)(f: Option[Value] => Unit): Unit = {
    val testInput = scala.io.Source.fromFile(file).mkString
    if (Debug.debug) printAst(testInput)
    val m = parse(testInput)
    val result = ConcExec.exec(m, main, Map())
    println(result)
    f(result)
  }

  def testAdd = testNoArg("llvm/benchmarks/add.ll", "@main") {
    case Some(IntValue(3)) =>
  }

  def testArrayAccess = testNoArg("llvm/benchmarks/arrayAccess.ll", "@main") {
    case Some(IntValue(4)) =>
  }

  def testArrayAccessLocal = testNoArg("llvm/benchmarks/arrayAccessLocal.ll", "@main") {
    case Some(IntValue(4)) =>
  }

  def testArrayGetSet = testNoArg("llvm/benchmarks/arrayGetSet.ll", "@main") {
    case Some(IntValue(636)) =>
  }


  def testPower = testNoArg("llvm/benchmarks/power.ll", "@main") {
    case Some(IntValue(27)) =>
  }

  def testSinglePath = {
    val testInput = scala.io.Source.fromFile("llvm/benchmarks/single_path.ll").mkString
    val m = parse(testInput)
    printAst(testInput)
    val result = ConcExec.exec(m, "@main", Map())
    println(result)
  }

  def testSimpleBranch = {
    val testInput = scala.io.Source.fromFile("llvm/benchmarks/branch.ll").mkString
    val m = parse(testInput)

    val result = ConcExec.exec(m, "@f", Map(
      FrameLoc("%x", curFrame.fname) -> IntValue(5)))
    println(result)
  }

  def printBB(bb: BB): Unit = {
    println("  Block: ")
    println(s"    Label: ${bb.label}")
    println()
    println("    Inst:")
    bb.ins.foreach(u => println(s"      ${u}"))
    println()
    println("    Term:")
    println(s"      ${bb.term}")
    println()
    println()
  }

  def printAst(input: String): Unit = {
    parse(input).es foreach {u => u match {
      case FunctionDef(id, linkage, metadata, header, body) => 
        println(s"Fundef: id: ${id}; linkage: ${linkage}; metadata: ${metadata};\n FunctionHeader: ${header}")
        body.blocks foreach(printBB(_))
      case _ => println(u)
    }}
    println("------------------endofAST--------------------")
  }

  def parse(input: String): Module = {
    val charStream = new ANTLRInputStream(input)
    val lexer = new LLVMLexer(charStream)
    val tokens = new CommonTokenStream(lexer)
    val parser = new LLVMParser(tokens)

    val visitor = new MyVisitor()
    val res: Module  = visitor.visit(parser.module).asInstanceOf[Module]
    //println(res.es(3))
    //println(res)
    res
  }

  def testMaze = {
    val testInput = scala.io.Source.fromFile("llvm/benchmarks/maze.ll").mkString
    printAst(testInput)
    val m = parse(testInput)

    val result = ConcExec.exec(m, "@main", Map(
      (FrameLoc("%argc", "@main") -> IntValue(5)),
      (FrameLoc("%argv", "@main") -> IntValue(5))
      ))
    println(result)
  }

  def main(args: Array[String]): Unit = {
    testArrayGetSet
    testArrayAccessLocal
    testArrayAccess
    testAdd
    testPower
    //testMaze
  }
}
