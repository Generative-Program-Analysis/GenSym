package sai.llvm

import sai.lang.llvm._
import sai.lang.llvm.IR._

import org.antlr.v4.runtime._
import scala.collection.JavaConverters._

import scala.collection.mutable
import scala.collection.immutable.Nil

// An imperative implementation of concrete execution
object ConcExecMemory {

  var funDeclMap: Map[String, FunctionDecl] = Map()
  var funMap: Map[String, FunctionDef] = Map()
  var globalDefMap: Map[String, GlobalDef] = Map()

  val heap: mutable.Map[Loc, Value] = mutable.Map.empty
  // array implementation of store
  var store: mutable.ArrayBuffer[Value] = mutable.ArrayBuffer.empty
  val globalAddressMap: mutable.Map[String, Int] = mutable.Map.empty
  def addGlobalVal(x: String, v : Value): Unit = {
    globalAddressMap.update(x, store.length)
    v match {
      case ArrayValue(ty, vs) =>
        store.append(ArrayHead(ty))
        store ++= flattenArray(v)
      case _ => store.append(v)
    } 
  }
  
  var curStack: Stack = List()
  def push(implicit frame: Frame): Unit = {
    originHead = store.length
    curStack = frame :: curStack
  }
  def pop(implicit frame: Frame): Unit = {
    store = store.take(originHead)
    curStack = curStack.tail
  }
  def curFrame: Frame = curStack.head
  var originHead = 0 
  var branches: List[String] = List()


  class Frame(val fname: String, val initStore: Store = Map.empty) {
    val addressMap: mutable.Map[String, Int] = mutable.Map.empty
    initStore foreach {case (k, v) => addLocalVar(k, v)}

    // def addressInit(x: String) = addressMap.update(x, lastIndex)
    def allocaIndex = store.length
    def alloca: Unit = store.append(BotValue)
    def allocaArray(ty: ArrayType): Unit = {
      store.append(ArrayHead(ty))
      store ++= mutable.ArrayBuffer.fill(getTySize(ty))(BotValue)
    }

    def addLocalVar(x: String, v : Value): Unit = {
      addressMap.update(x, allocaIndex)
      v match {
        case ArrayValue(ty, vs) =>
          store.append(ArrayHead(ty))
          store ++= flattenArray(v)
        case _ => store.append(v)
      } 
    }
    
    def apply(x: Loc): Value = x match {
      case GeneralLoc(i) => store(i)
      case FrameLoc(x, frame) => store(addressMap(x))
    }
    def apply(x: String): Value = apply(FrameLoc(x, fname))
    def update(x: GeneralLoc, v: Value): Unit = { store(x.i) = v }
    def update(x: String, v: Value): Unit = update(GeneralLoc(addressMap(x)), v)
  }

  abstract class Loc
  case class GeneralLoc(i: Int) extends Loc
  case class GlobalLoc(x: String) extends Loc
  case class FrameLoc(x: String, frame: String) extends Loc
  case class SpecialLoc(x: String) extends Loc
  // Really ugly, need refined with new memory layout
  case class ArrayLoc(loc: Loc, index: List[IntValue]) extends Loc
  class AllocaLoc(inst: ValueInstruction, frame: Frame) extends Loc

  abstract class Value
  case object BotValue extends Value
  case class IntValue(x: Int) extends Value {
    override def toString = x.toString()
  }
  case class LocValue(loc: Loc) extends Value
  case class ArrayHead(ty: ArrayType) extends Value
  // ArrayValue is the outside view of Array, while inside memory, we have 
  // ArrayHead | Value | ... | Value 
  case class ArrayValue(ty: ArrayType, vs: mutable.ListBuffer[Value]) extends Value
  case class FunValue(id: String, params: List[String]) extends Value {
    def apply(args: List[Value]): Value = {
      params.zip(args).foreach { case (k, v) => curFrame.addLocalVar(k, v) }
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
  
  type Store = Map[String, Value]
  type Stack = List[Frame]

  // def arrayGet(arr: Value, index: List[IntValue]): Value = {
  //   arr match {
  //     case ArrayValue(tp, vs) => index match {
  //       case Nil => arr
  //       case head :: tl => arrayGet(vs(head.x), tl)
  //     }
  //     case _ => arr
  //   }
  // }

  // def arrayUpdate(arr: Value, index: List[IntValue], v: Value): Unit = {
  //   index match {
  //     case head :: Nil =>
  //       arr match {
  //         case ArrayValue(tp, vs) => vs(head.x) = v
  //         case _ => throw new RuntimeException("Update non-array")
  //       }
  //     case head :: tl => 
  //       arr match {
  //         case ArrayValue(tp, vs) => arrayUpdate(vs(head.x), tl, v)
  //         case _ => throw new RuntimeException("Update non-array")
  //       }
  //   }
  // }

  def getTySize(vt: LLVMType): Int = vt match {
    case ArrayType(size, ety) => size * getTySize(ety)
    case _ => 1
  }

  def eval(v: LLVMValue): Value = {
    v match {
      case LocalId(x) => curFrame(x)
      case IntConst(n) => IntValue(n)
      case BoolConst(b) => if (b) IntValue(1) else IntValue(0)
      case ZeroInitializerConst => VoidValue
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
            case "@printf" => {
              
              print("Printf: ")
              val LocValue(GeneralLoc(i)) = args.head
              val ArrayHead(ty) = store(i-1)
              var tmp = 
              store.slice(i, i + ty.size) map {
                case IntValue(x) =>
                  print(x.toChar)
              }
              print(" ")
              args.tail foreach (i => print(i + " "))
              println()
              VoidValue
            }
            case "@read" => 
              val LocValue(GeneralLoc(start)) = args(1)
              val IntValue(len) = args(2)
              val rawInput = "ssssddddwwadwwddddssssddwwww"
              val inputStr = rawInput.take(Math.min(len, rawInput.length))
              Range(0, len) foreach (i => store(start + i) = IntValue(inputStr(i).toInt))
              VoidValue
            //eval(CharArrayConst("ssssddddwwaawwddddssssddwwww"))
            case "@exit" => VoidValue
            case "@sleep" => VoidValue
          }
        }
      case GlobalId(id) if (globalDefMap.contains(id)) =>
        store(globalAddressMap(id))
      case GlobalId(id) =>
        throw new RuntimeException("Cannot evaluate global id " + id)
      case ArrayConst(cs) => 
        ArrayValue(ArrayType(cs.length, cs.head.ty), mutable.ListBuffer.empty ++= cs.map(v => eval(v.const)))
      case BitCastExpr(from, const, to) =>
        eval(const)
      case CharArrayConst(s) =>
        // TODO need to be modified in parser
        val realS = s.slice(1, s.length-1)
        ArrayValue(ArrayType(realS.length, IntType(8)), mutable.ListBuffer.empty ++= realS.map(c => IntValue(c.toInt)))
      case GetElemPtrExpr(_, baseType, ptrType, const, typedConsts) =>
        def calculateOffset(ty: LLVMType, index: List[Int]): Int = {
          if (index.isEmpty) 0 else ty match {
            case PtrType(ety, addrSpace) =>
              index.head * getTySize(ety) + calculateOffset(ety, index.tail)
            case ArrayType(size, ety) =>
              index.head * getTySize(ety) + calculateOffset(ety, index.tail)
            // Struct
            case _ => ???
          }
        }
        val index = typedConsts.map(v => eval(v.const).asInstanceOf[IntValue].x)
        // calculate offset
        val baseAddress:Int = const match {
          case GlobalId(id) => globalAddressMap(id)
          case _ => 
            val LocValue(loc) = eval(const)
            loc match {
              case GeneralLoc(i) => i
            }
        }
        val offset = calculateOffset(ptrType, index) + { 
          if (store(baseAddress).isInstanceOf[ArrayHead]) 1 else 0
        }
        LocValue(GeneralLoc(baseAddress + offset))
    }
  }

  def inspectMemory(x: Int, y: Int): Unit =
    {
      Range(x, y) foreach(i => print(store(i).asInstanceOf[IntValue].x.toChar))
      println()
    }

  def execInst(inst: Instruction): Unit = {
    if (Debug.debug) {println(inst);}
    inst match {
      case AssignInst(x, valInst) =>
        val curVal = execValueInst(valInst)
        curFrame.addLocalVar(x, curVal)
      case StoreInst(ty1, val1, ty2, val2, align) =>
        val v1 = eval(val1)
        eval(val2) match {
          case LocValue(l) =>
            l match {
              case gl@GeneralLoc(i) => curFrame(gl) = v1
            }
        }
      case CallInst(ty, f, args) =>
        val fun@FunValue(fid, _) = eval(f)
        val argValues: List[Value] = args.map {
          case TypedArg(ty, attrs, value) => eval(value)
        }
        implicit val frame = new Frame(fid)
        push
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

  def execTerm(inst: Terminator): Option[Value] = {
    if (Debug.debug) println(inst)
    inst match {
      case RetTerm(ty, Some(value)) =>
        println(value)
        println(eval(value))
        Some(eval(value))
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
        val matchCase = table.filter(lcase => { lcase.n == i })
        if (matchCase.isEmpty) {
          val Some(b) = findBlock(curFrame.fname, default)
          branches = default :: branches
          execBlock(b)
        } else {
          val Some(b) = findBlock(curFrame.fname, matchCase.head.label)
          branches = matchCase.head.label :: branches
          execBlock(b)
        }
      // simply return
      case Unreachable => None
    }
  }

  def evalArrayInit(vt: LLVMType): Value = vt match {
    case at@ArrayType(size, ety) => 
      ArrayValue(at, mutable.ListBuffer.fill(size)(evalArrayInit(ety)))
    case _ => BotValue
  }

  def execValueInst(inst: ValueInstruction): Value = {
    inst match {
      case AllocaInst(IntType(_), _) =>
        val ptr = GeneralLoc(curFrame.allocaIndex)
        curFrame.alloca
        LocValue(ptr)
      case AllocaInst(vt : ArrayType, _) =>
        val ptr = GeneralLoc(curFrame.allocaIndex)
        curFrame.allocaArray(vt)
        LocValue(ptr)
      case AllocaInst(PtrType(ty, _), _) =>
        val ptr = GeneralLoc(curFrame.allocaIndex)
        curFrame.alloca
        LocValue(ptr)
      case AllocaInst(ty, align) => ???
      case LoadInst(valTy, ptrTy, value, align) =>
        val LocValue(ptr) = eval(value)
        curFrame(ptr)

      // getElmPtr Note:
      // typedValues will contain an "extra" parameter compares to C
      // why? see https://llvm.org/docs/GetElementPtr.html#why-is-the-extra-0-index-required
      
      // So it is necessary to convert to a unified memory as GetElemPtr
      // also: is &arr[0] ArrayLoc or Ptr(int)? It should be the same thing in the whole memory representation
      case GetElemPtrInst(_, baseTy, ptrTy, ptrVal, typedValues) =>
        def calculateOffset(ty: LLVMType, index: List[Int]): Int = {
          if (index.isEmpty) 0 else ty match {
            case PtrType(ety, addrSpace) =>
              index.head * getTySize(ety) + calculateOffset(ety, index.tail)
            case ArrayType(size, ety) =>
              index.head * getTySize(ety) + calculateOffset(ety, index.tail)
            case _ => ???
          }
        }
        val index = typedValues.map(v => eval(v.value).asInstanceOf[IntValue].x)
        // calculate offset
        val baseAddress:Int = ptrVal match {
          case GlobalId(id) => globalAddressMap(id)
          case _ => 
            val LocValue(loc) = eval(ptrVal)
            loc match {
              case GeneralLoc(i) => i
            }
        }
        val offset = calculateOffset(ptrTy, index) + { 
          if (store(baseAddress).isInstanceOf[ArrayHead]) 1 else 0
        } 
        // if (ptrVal == GlobalId("@maze")) {
        //   println("++++++++++++++++++++++++++++++++++ base: " + baseAddress + "  offset: " + offset)
        //   inspectMemory(baseAddress+1, baseAddress+12)
        //   inspectMemory(baseAddress+12, baseAddress+23)
        //   inspectMemory(baseAddress+23, baseAddress+34)
        // }
        // if (ptrVal == LocalId("%arrayidx18")) {
        //   println("++++++++++++++++++++++++++++++++++ mem: " + (baseAddress + offset))
        //   inspectMemory(baseAddress+1, baseAddress+12)
        //   inspectMemory(baseAddress+12, baseAddress+23)
        //   inspectMemory(baseAddress+23, baseAddress+34)
        // }
        LocValue(GeneralLoc(baseAddress + offset))
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
        implicit val frame = new Frame(fid)
        push
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
    m.globalDefMap foreach {case (s, gDef) =>
      addGlobalVal(gDef.id, eval(gDef.const))
    }

    push(new Frame(fname, initStore))
    execBlock(f.body.blocks(0))
  }
}

object TestMemory {
  import ConcExecMemory._
  def testNoArg(file: String, main: String)(f: Option[Value] => Unit): Unit = {
    val testInput = scala.io.Source.fromFile(file).mkString
    if (Debug.debug) printAst(testInput)
    val m = parse(testInput)
    val result = ConcExecMemory.exec(m, main, Map())
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

  // should this work?
  def testArraySetLocal = testNoArg("llvm/benchmarks/arraySetLocal.ll", "@main") {
    case Some(IntValue(42)) =>
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
    val result = ConcExecMemory.exec(m, "@main", Map())
    println(result)
  }

  def testSimpleBranch = {
    val testInput = scala.io.Source.fromFile("llvm/benchmarks/branch.ll").mkString
    val m = parse(testInput)

    val result = ConcExecMemory.exec(m, "@f", Map(
     "%x" -> IntValue(5)))
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
    val m = parse(testInput)
    if (Debug.debug) printAst(testInput)
    val result = ConcExecMemory.exec(m, "@main", Map(
      "%argc" -> IntValue(5),
      "%argv" -> IntValue(5)
      ))
    println(result)
  }

  def testMazeNoPhi = {
    val testInput = scala.io.Source.fromFile("llvm/benchmarks/maze_nophi.ll").mkString
    val m = parse(testInput)
    if (Debug.debug) printAst(testInput)
    val result = ConcExecMemory.exec(m, "@main", Map(
      "%argc" -> IntValue(5),
      "%argv" -> IntValue(5)
      ))
    println(result)
  }

  def main(args: Array[String]): Unit = {
    // testArraySetLocal
    // testArrayAccess
    // testArrayGetSet
    // testArrayAccessLocal
    
    // testAdd
    // testPower
    testMaze
    // testMazeNoPhi
  }
}
