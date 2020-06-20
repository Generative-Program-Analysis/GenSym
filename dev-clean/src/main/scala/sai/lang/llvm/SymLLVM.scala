package sai.lang.llvm
import IR._

import org.antlr.v4.runtime._
import scala.collection.JavaConverters._

// An imperative implementation of concrete execution

object ConcExec {
  class Frame(val fname: String)

  abstract class Loc
  case class FrameLoc(x: String, frame: Frame) extends Loc
  case class SpecialLoc(x: String) extends Loc
  class AllocaLoc(inst: ValueInstruction, frame: Frame) extends Loc

  abstract class Value
  case object BotValue extends Value
  case class IntValue(x: Int) extends Value
  case class LocValue(loc: Loc) extends Value
  case class ArrayValue(vs: List[Value]) extends Value
  
  type Store = Map[Loc, Value]
  type Stack = List[Frame]

  var funTable: List[FunctionDef] = List()
  var curFrame: Frame = new Frame("@main")
  var curStore: Store = Map[Loc, Value]()
  var curStack: Stack = List()

  def eval(v: LLVMValue): Value = {
    v match {
      case LocalId(x) =>
        val loc = FrameLoc(x, curFrame)
        curStore(loc)
      case IntConst(n) =>
        IntValue(n)
    }
  }

  def execInst(inst: Instruction): Unit = {
    inst match {
      case AssignInst(x, valInst) =>
        val addr = FrameLoc(x, curFrame)
        val value = execValueInst(valInst)
        curStore = curStore + (addr -> value)
      case StoreInst(ty1, val1, ty2, val2, align) =>
        val v1 = eval(val1)
        eval(val2) match {
          case LocValue(l) =>
            curStore = curStore + (l -> v1)
        }
    }
  }

  def findBlock(fname: String, lab: String): Option[BB] = {
    funTable.find(_.id == fname).get.lookupBlock(lab)
  }

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

  def execValueInst(inst: ValueInstruction): Value = {
    inst match {
      case AllocaInst(IntType(_), _) =>
        val ptr = new AllocaLoc(inst, curFrame)
        curStore = curStore + (ptr -> BotValue)
        LocValue(ptr)
      case AllocaInst(ArrayType(n, IntType(m)), _) =>
        val size = (n * m) / 8 //bytes
        val ptr = new AllocaLoc(inst, curFrame)
        curStore = curStore + (ptr -> ArrayValue(List.fill(n)(BotValue)))
        LocValue(ptr)
      case AllocaInst(PtrType(ty, _), _) =>
        val ptr = new AllocaLoc(inst, curFrame)
        curStore = curStore + (ptr -> BotValue)
        LocValue(ptr)
      case AllocaInst(ty, align) => ???
      case LoadInst(valTy, ptrTy, value, align) =>
        eval(value) match {
          case LocValue(ptr) => curStore(ptr)
        }
      case GetElemPtrInst(_, baseTy, ptrTy, ptrVal, typedValues) => ???
      case AddInst(ty, lhs, rhs, _) =>
        val IntValue(v1) = eval(lhs)
        val IntValue(v2) = eval(rhs)
        IntValue(v1 + v2)
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
      case ZExtInst(from, value, to) => ???
      case SExtInst(from, value, to) => ???
      case CallInst(ty, f, args) => ???
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
    println(term)
    execTerm(term)
  }

  val mainStore0 = Map(
    FrameLoc("%0", curFrame) -> IntValue(0),
    FrameLoc("%1", curFrame) -> LocValue(SpecialLoc("argv")),
    SpecialLoc("argc") -> ArrayValue(List(IntValue(0))))

  def exec(m: Module, fname: String, initStore: => Store): Option[Value] = {
    val Some(f) = m.lookupFuncDef(fname)
    funTable = m.es.filter(_.isInstanceOf[FunctionDef]).asInstanceOf[List[FunctionDef]]
    curFrame = new Frame(fname)
    curStore = initStore
    execBlock(f.body.blocks(0))
  }
}

/**************************************************************/

object SymExecEff {
  import sai.structure.freer3._
  import Eff._
  import Freer._
  import Handlers._
  import OpenUnion._
  import Nondet._
  import State._

  class Frame(val fname: String) {
    override def toString = fname
  }

  abstract class Loc
  case class FrameLoc(x: String, frame: Frame) extends Loc
  case class SpecialLoc(x: String) extends Loc
  case class AllocaLoc(lhs: String, frame: Frame) extends Loc

  abstract class Value
  case object BotValue extends Value
  case class IntValue(x: Int) extends Value
  case class LocValue(loc: Loc) extends Value
  case class ArrayValue(vs: List[Value]) extends Value
  
  type Store = Map[Loc, Value]
  type Stack = List[Frame]
  type PC = Set[String]
  type SS = (Frame, Store, Stack, PC)

  type E = (State[SS, *] ⊗ (Nondet ⊗ ∅))

  def getFrame: Comp[E, Frame] = for {
    s <- get[SS, E]
  } yield s._1

  def getStore: Comp[E, Store] = for {
    s <- get[SS, E]
  } yield s._2

  def getStack: Comp[E, Stack] = for {
    s <- get[SS, E]
  } yield s._3

  def getPC: Comp[E, PC] = for {
    s <- get[SS, E]
  } yield s._4

  def updateStore(a: Loc, v: Value): Comp[E, Unit] = for {
    s <- get[SS, E]
    _ <- put[SS, E]((s._1, s._2 + (a -> v), s._3, s._4))
  } yield ()

  def updatePC(c: String): Comp[E, Unit] = for {
    s <- get[SS, E]
    _ <- put[SS, E]((s._1, s._2, s._3, s._4 ++ Set(c)))
  } yield ()

  def eval(v: LLVMValue): Comp[E, Value] = {
    v match {
      case LocalId(x) => for {
        f <- getFrame
        σ <- getStore
      } yield { σ(FrameLoc(x, f)) }
      case IntConst(n) =>
        ret(IntValue(n))
    }
  }

  def execValueInst(lhs: String, inst: ValueInstruction): Comp[E, Value] = {
    inst match {
      case AllocaInst(IntType(_), _) =>
        for {
          f <- getFrame
          _ <- updateStore(AllocaLoc(lhs, f), BotValue)
        } yield LocValue(AllocaLoc(lhs, f))
      case AllocaInst(ArrayType(n, IntType(m)), _) =>
        for {
          f <- getFrame
          _ <- updateStore(AllocaLoc(lhs, f), ArrayValue(List.fill(n)(BotValue)))
        } yield LocValue(AllocaLoc(lhs, f))
      case AllocaInst(PtrType(ty, _), _) =>
        for {
          f <- getFrame
          _ <- updateStore(AllocaLoc(lhs, f), BotValue)
        } yield LocValue(AllocaLoc(lhs, f))
      case AllocaInst(ty, align) => ???
      case LoadInst(valTy, ptrTy, value, align) =>
        for {
          v <- eval(value)
          σ <- getStore
        } yield σ(v.asInstanceOf[LocValue].loc)
      case GetElemPtrInst(_, baseTy, ptrTy, ptrVal, typedValues) => ???
      case AddInst(ty, lhs, rhs, _) =>
        for {
          v1 <- eval(lhs)
          v2 <- eval(rhs)
        } yield IntValue(v1.asInstanceOf[IntValue].x + v2.asInstanceOf[IntValue].x)
      case SubInst(ty, lhs, rhs, _) =>
        for {
          v1 <- eval(lhs)
          v2 <- eval(rhs)
        } yield IntValue(v1.asInstanceOf[IntValue].x - v2.asInstanceOf[IntValue].x)
      case ICmpInst(pred, ty, lhs, rhs) =>
        for {
          val1 <- eval(lhs)
          val2 <- eval(rhs)
        } yield {
          val v1 = val1.asInstanceOf[IntValue].x
          val v2 = val2.asInstanceOf[IntValue].x
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
        }
      case ZExtInst(from, value, to) => ???
      case SExtInst(from, value, to) => ???
      case CallInst(ty, f, args) => ???
      case PhiInst(ty, incomings) => ???
      case SelectInst(cndTy, cndVal, thnTy, thnVal, elsTy, elsVal) => ???
    }
  }

  var funTable: List[FunctionDef] = List()

  def findBlock(fname: String, lab: String): Option[BB] = {
    funTable.find(_.id == fname).get.lookupBlock(lab)
  }

  def execTerm(inst: Terminator): Comp[E, Value] = {
    inst match {
      case RetTerm(ty, Some(value)) =>
        eval(value)
      case RetTerm(ty, None) => ret(IntValue(0)) //return 0 by default
      case BrTerm(lab) =>
        for {
          f <- getFrame
          v <- { val Some(b) = findBlock(f.fname, lab); execBlock(b) }
        } yield v
      case CondBrTerm(ty, cnd, thnLab, elsLab) =>
        for {
          f <- getFrame
          c <- eval(cnd)
          v <- {
            val Some(b1) = findBlock(f.fname, thnLab)
            val m1 = for {
              _ <- updatePC(cnd.toString)
              v <- execBlock(b1)
            } yield v
            val Some(b2) = findBlock(f.fname, elsLab)
            val m2 = for {
              _ <- updatePC("not " + cnd.toString)
              v <- execBlock(b2)
            } yield v
            choice(m1, m2)
          }
        } yield v
      case CondBrTerm(ty, cnd, thnLab, elsLab) => //determinisitc execution
        for {
          f <- getFrame
          c <- eval(cnd)
          v <- {
            val IntValue(i) = c
            if (c == 1) {
              val Some(b) = findBlock(f.fname, thnLab)
              for {
                _ <- updatePC(cnd.toString)
                v <- execBlock(b)
              } yield v
            } else {
              val Some(b) = findBlock(f.fname, elsLab)
              for {
                _ <- updatePC("not " + cnd.toString)
                v <- execBlock(b)
              } yield v
            }
          }
        } yield v
      case SwitchTerm(cndTy, cndVal, default, table) => ???
      case Unreachable => ???
    }
  }

  def execInst(inst: Instruction): Comp[E, Unit] = {
    inst match {
      case AssignInst(x, valInst) =>
        for {
          f <- getFrame
          v <- execValueInst(x, valInst)
          _ <- updateStore(FrameLoc(x, f), v)
        } yield ()
      case StoreInst(ty1, val1, ty2, val2, align) =>
        for {
          v1 <- eval(val1)
          v2 <- eval(val2)
          _ <- updateStore(v2.asInstanceOf[LocValue].loc, v1)
        } yield ()
    }
  }

  def execBlock(bb: BB): Comp[E, Value] = {
    val insts = bb.ins
    val term = bb.term
    def runInstList(is: List[Instruction]): Comp[E, Value] = {
      is match {
        case List(i) => for {
          _ <- execInst(i)
          v <- execTerm(term)
        } yield v
        case i :: is =>
          for {
            _ <- execInst(i)
            v <- runInstList(is)
          } yield v
      }
    }
    runInstList(insts)
  }

  def runLocalState[R <: Eff, S, A](s: S, comp: Comp[State[S, *] ⊗ (Nondet ⊗ R), A]):
      Comp[R, List[Comp[R, List[(S, A)]]]] = {
    val p: Comp[Nondet ⊗ R, S => Comp[Nondet ⊗ R, (S, A)]] = State.run[Nondet ⊗ R, S, A](comp)
    val p1: Comp[R, List[S => Comp[Nondet ⊗ R, (S, A)]]] = Nondet.run[R, S => Comp[Nondet ⊗ R, (S, A)]](p)
    for {fs <- p1} yield fs.map(f => Nondet.run(f(s)))
  }

  def exec(m: Module, fname: String, initStore: Map[String, Value]): List[(SS, Value)] = {
    val Some(f) = m.lookupFuncDef(fname)
    funTable = m.es.filter(_.isInstanceOf[FunctionDef]).asInstanceOf[List[FunctionDef]]

    val f0 = new Frame(fname)
    val s0: Map[Loc, Value] = initStore.map({ case (k,v) => (FrameLoc(k, f0), v) })
    val st0 = List[Frame]()
    val init: SS = (f0, s0, st0, Set())
    val r: List[List[(SS, Value)]] =
      extract(runLocalState[∅, SS, Value](init, execBlock(f.body.blocks(0)))).map(extract)
    r(0)
  }

}

object LLVMTest {
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

  def testAdd = {
    import ConcExec._
    val testInput = scala.io.Source.fromFile("llvm/test/add.ll").mkString
    val m = parse(testInput)

    val result = ConcExec.exec(m, "@add", Map(
      FrameLoc("%0", curFrame) -> IntValue(5),
      FrameLoc("%1", curFrame) -> IntValue(2)))
    println(result)
  }

  def testAddEff = {
    import SymExecEff._
    val testInput = scala.io.Source.fromFile("llvm/test/add.ll").mkString
    val m = parse(testInput)

    val result = SymExecEff.exec(m, "@add", Map(
      "%0" -> IntValue(5),
      "%1" -> IntValue(2)))
    println(result)
  }

  def testSinglePath = {
    import ConcExec._
    val testInput = scala.io.Source.fromFile("llvm/test/single_path.ll").mkString
    val m = parse(testInput)

    val result = ConcExec.exec(m, "@singlepath", Map(
      FrameLoc("%x", curFrame) -> IntValue(5)))
    println(result)
  }

  def testSinglePathEff = {
    import SymExecEff._
    val testInput = scala.io.Source.fromFile("llvm/test/single_path2.ll").mkString // 8k
    //val testInput = scala.io.Source.fromFile("llvm/test/single_path.ll").mkString //2k
    //val testInput = scala.io.Source.fromFile("llvm/test/single_path5.ll").mkString //4k
    val m = parse(testInput)

    val t0 = System.nanoTime()
    val result = SymExecEff.exec(m, "@singlepath", Map("%x" -> IntValue(5)))
    println(result.size)
    val t1 = System.nanoTime()
    val t = (t1 - t0) / 1000000000.0
    println("time: " + t)
  }

  def testSimpleBranch = {
    import ConcExec._
    val testInput = scala.io.Source.fromFile("llvm/test/branch.ll").mkString
    val m = parse(testInput)

    val result = ConcExec.exec(m, "@f", Map(
      FrameLoc("%x", curFrame) -> IntValue(5)))
    println(result)
  }

  def testSimpleBranchEff = {
    import SymExecEff._
    //val testInput = scala.io.Source.fromFile("llvm/test/branch2.ll").mkString
    val testInput = scala.io.Source.fromFile("llvm/test/multipath4.ll").mkString
    val m = parse(testInput)

    val t0 = System.nanoTime()
    val result = SymExecEff.exec(m, "@f", Map("%a" -> IntValue(5),
      "%b" -> IntValue(6),
      "%c" -> IntValue(7)))
    println(result.size)
    val t1 = System.nanoTime()
    val t = (t1 - t0) / 1000000000.0
    println("time: " + t)

  }

  def main(args: Array[String]): Unit = {
    //val testInput = scala.io.Source.fromFile("llvm/test/maze.ll").mkString
    //val testInput = scala.io.Source.fromFile("llvm/test/add.ll").mkString
    //println(testInput)

    //testAdd
    //testAddEff

    /*
    testSinglePath
    testSinglePathEff
    testSinglePathEff
    testSinglePathEff
     */

    //testSimpleBranch
    testSimpleBranchEff
  }
}
