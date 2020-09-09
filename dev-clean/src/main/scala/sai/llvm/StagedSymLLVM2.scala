package sai.llvm.se

import sai.lang.llvm._
import sai.lang.llvm.IR._

import org.antlr.v4.runtime._
import scala.collection.JavaConverters._

import sai.structure.freer3._
import Eff._
import Freer._
import Handlers._
import OpenUnion._
import Nondet._
import State._

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._
import sai.structure.lattices._
import sai.structure.lattices.Lattices._

import sai.imp.{RepNondet}

import scala.collection.immutable.{List => SList}
import scala.collection.immutable.{Map => SMap}

@virtualize
trait StagedSymExecEff extends SAIOps with RepNondet {
  // FIXME: concrete representation of Mem and Expr
  trait Mem
  trait SMTExpr
  trait Addr
  trait Value

  type Heap = Mem
  type Frame = (String, Mem)
  type PC = Set[SMTExpr]
  type Stack = List[Frame]
  type SS = (Heap, Stack, PC)
  type E = State[Rep[SS], *] ⊗ (Nondet ⊗ ∅)

  def getState: Comp[E, Rep[SS]] = get[Rep[SS], E]
  def getHeap: Comp[E, Rep[Heap]] = for { s <- get[Rep[SS], E] } yield s._1
  def getStack: Comp[E, Rep[Stack]] = for { s <- get[Rep[SS], E] } yield s._2
  def getPC: Comp[E, Rep[PC]] = for { s <- get[Rep[SS], E] } yield s._3
  def curFrame: Comp[E, Rep[Frame]] = for { fs <- getStack } yield fs.head

  object Mem {
    def lookup(σ: Rep[Mem], a: Rep[Addr]): Rep[Value] = ???
    def frameLookup(f: Rep[Frame], a: Rep[Addr]): Rep[Value] = lookup(f._2, a)

    def replaceCurrentFrame(f: Rep[Frame]): Comp[E, Rep[Unit]] = ???
    def alloc(σ: Rep[Mem], size: Int): (Rep[Mem], Rep[Addr]) = ???
    def frameAlloc(f: Rep[Frame], size: Int): (Rep[Frame], Rep[Addr]) = {
      val (σ, a) = alloc(f._2, size)
      ((f._1, σ), a)
    }
    def frameAlloc(size: Int): Comp[E, Rep[Addr]] = {
      /* 
       for {
         f <- curFrame
         val (f_, a) = frameAlloc(f, size)
         _ <- replaceCurrentFrame(f_)
       } yield a
       */
      // Note: using val keyword in monadic style seems having some trouble
      curFrame.flatMap { f =>
        val (f_, a) = frameAlloc(f, size)
        replaceCurrentFrame(f_).map { _ => a }
      }
    }

    def update(σ: Rep[Mem], k: Rep[Addr], v: Rep[Value]): Rep[Mem] = ???
    def frameUpdate(f: Rep[Frame], k: Rep[Addr], v: Rep[Value]): Rep[Frame] = {
      val σ = update(f._2, k, v)
      (f._1, σ)
    }
    def frameUpdate(x: String, v: Rep[Value]): Comp[E, Rep[Unit]] = {
      ???
    }

    def selectMem(v: Rep[Value]): Comp[E, Rep[Mem]] = {
      // if v is a HeapAddr, return heap, otherwise return its frame memory
      ???
    }
    def updateMem(k: Rep[Value], v: Rep[Value]): Comp[E, Rep[Unit]] = {
      // if v is a HeapAddr, update heap, otherwise update its frame memory
      // v should be a LocV and wrap an actual location
      ???
    }
  }

  object Addr {
    def localAddr(f: Rep[Frame], x: String): Rep[Addr] = ???
  }

  object Value {
    def IntV(i: Rep[Int]): Rep[Value] = IntV(i, 32)
    def IntV(i: Rep[Int], bw: Int): Rep[Value] = ???
    def LocV(l: Rep[Addr]): Rep[Value] = ???

    def projLocV(v: Rep[Value]): Rep[Addr] = ???
    def projIntV(v: Rep[Value]): Rep[Int] = ???
  }

  object CompileTimeRuntime {
    var funMap: collection.immutable.Map[String, FunctionDef] = SMap()
    var funDeclMap: collection.immutable.Map[String, FunctionDecl] = SMap()

    val BBFuns: collection.mutable.HashMap[BB, Rep[SS => List[(SS, Value)]]] =
      new collection.mutable.HashMap[BB, Rep[SS => List[(SS, Value)]]]
    val FunFuns: collection.mutable.HashMap[String, Rep[SS => List[(SS, Value)]]] =
      new collection.mutable.HashMap[String, Rep[SS => List[(SS, Value)]]]

    def getTySize(vt: LLVMType, align: Int = 1): Int = vt match {
      case ArrayType(size, ety) =>
        val rawSize = size * getTySize(ety, align)
        if (rawSize % align == 0) rawSize
        else (rawSize / align + 1) * align
      case _ => 1
    }

    def findBlock(fname: String, lab: String): Option[BB] = {
      funMap.get(fname).get.lookupBlock(lab)
    }
    def findFirstBlock(fname: String): BB = {
      findFundef(fname).body.blocks(0)
    }
    def findFundef(fname: String) = funMap.get(fname).get
  }

  object Magic {
    def reify[T: Manifest](s: Rep[SS])(comp: Comp[E, Rep[T]]): Rep[List[(SS, T)]] = {
      val p1: Comp[Nondet ⊗ ∅, (Rep[SS], Rep[T])] =
        State.run2[Nondet ⊗ ∅, Rep[SS], Rep[T]](s)(comp)
      val p2: Comp[Nondet ⊗ ∅, Rep[(SS, T)]] = p1.map(a => a)
      val p3: Comp[∅, Rep[List[(SS, T)]]] = runRepNondet(p2)
      p3
    }

    def reflect[T: Manifest](res: Rep[List[(SS, T)]]): Comp[E, Rep[T]] = {
      for {
        ssu <- select[E, (SS, T)](res)
        _ <- put[Rep[SS], E](ssu._1)
      } yield ssu._2
    }
  }

  import Mem._
  import Addr._
  import Value._
  import CompileTimeRuntime._
  import Magic._

  def eval(v: LLVMValue): Comp[E, Rep[Value]] = {
    v match {
      case LocalId(x) => 
        for { f <- curFrame } yield { frameLookup(f, localAddr(f, x)) }
      case IntConst(n) => ret(IntV(n))
      case ArrayConst(cs) => ???
      case BitCastExpr(from, const, to) => ???
      case BoolConst(b) => ???
      case GlobalId(id) => ???
    }
  }

  def execValueInst(inst: ValueInstruction): Comp[E, Rep[Value]] = {
    inst match {
      case AllocaInst(ty, align) =>
        for {
          f <- curFrame
          a <- frameAlloc(getTySize(ty, align.n))
        } yield LocV(a)
      case LoadInst(valTy, ptrTy, value, align) =>
        for {
          v <- eval(value)
          σ <- selectMem(v)
        } yield lookup(σ, projLocV(v))
      case AddInst(ty, lhs, rhs, _) =>
        for {
          v1 <- eval(lhs)
          v2 <- eval(rhs)
        } yield IntV(projIntV(v1) + projIntV(v2))
      case SubInst(ty, lhs, rhs, _) =>
        for {
          v1 <- eval(lhs)
          v2 <- eval(rhs)
        } yield IntV(projIntV(v1) - projIntV(v2))
      /*
      case ICmpInst(pred, ty, lhs, rhs) =>
        for {
          val1 <- eval(lhs)
          val2 <- eval(rhs)
        } yield {
          val v1 = ProjInt(val1)
          val v2 = ProjInt(val2)
          pred match {
            case EQ => IntV(if (v1 == v2) 1 else 0)
            case NE => IntV(if (v1 != v2) 1 else 0)
            case SLT => IntV(if (v1 < v2) 1 else 0)
            case SLE => IntV(if (v1 <= v2) 1 else 0)
            case SGT => IntV(if (v1 > v2) 1 else 0)
            case SGE => IntV(if (v1 >= v2) 1 else 0)
            case ULT => IntV(if (v1 < v2) 1 else 0)
            case ULE => IntV(if (v1 <= v2) 1 else 0)
            case UGT => IntV(if (v1 > v2) 1 else 0)
            case UGE => IntV(if (v1 >= v2) 1 else 0)
          }
        }
      case ZExtInst(from, value, to) => ???
      case SExtInst(from, value, to) => ???
      case CallInst(ty, f, args) => ???
      case GetElemPtrInst(inBounds, baseType, ptrType, ptrValue, typedValues) => ???
      */
    }
  }

  // Note: Comp[E, Rep[Value]] vs Comp[E, Rep[Option[Value]]]?
  def execTerm(fun: String, inst: Terminator): Comp[E, Rep[Value]] = {
    inst match {
      case RetTerm(ty, Some(value)) => eval(value)
      case RetTerm(ty, None) => ret(IntV(0))
      case BrTerm(lab) =>
        val Some(b) = findBlock(fun, lab)
        execBlock(fun, b)
        // branches = lab :: branches
      /*
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
      */
    }
  }

  def execInst(fun: String, inst: Instruction): Comp[E, Rep[Unit]] = {
    inst match {
      case AssignInst(x, valInst) =>
        for {
          v <- execValueInst(valInst)
          _ <- frameUpdate(x, v)
        } yield ()
      case StoreInst(ty1, val1, ty2, val2, align) =>
        for {
          v1 <- eval(val1)
          v2 <- eval(val2)
          _ <- updateMem(v2, v1)
        } yield ()
      case CallInst(ty, f, args) =>
        ???
        /*
        val fun@FunValue(fid, _) = eval(f)
        val argValues: List[Value] = args.map {
          case TypedArg(ty, attrs, value) => eval(value)
        }
        push(Frame(fid))
        fun(argValues)
        pop
         */
    }
  }

  def execBlock(funName: String, bb: BB): Comp[E, Rep[Value]] = {
    val f = CompileTimeRuntime.BBFuns(bb)
    for {
      s <- getState
      v <- reflect(f(s))
    } yield v
  }

  def precompileBlocks(funName: String, blocks: List[BB]): Unit = {
    def runInstList(is: List[Instruction], term: Terminator): Comp[E, Rep[Value]] = {
      is match {
        case SList(i) => for {
          _ <- execInst(funName, i)
          v <- execTerm(funName, term)
        } yield v
        case i :: is =>
          for {
            _ <- execInst(funName, i)
            v <- runInstList(is, term)
          } yield v
      }
    }
    def runBlock(b: BB)(ss: Rep[SS]): Rep[List[(SS, Value)]] = {
      reify[Value](ss)(runInstList(b.ins, b.term))
    }

    for (b <- blocks) {
      // FIXME: topFun or fun?
      if (CompileTimeRuntime.BBFuns.contains(b)) {
        System.err.println("Already compiled " + b)
      } else {
        val repRunBlock: Rep[SS => List[(SS, Value)]] = fun(runBlock(b))
        CompileTimeRuntime.BBFuns(b) = repRunBlock
      }
    }
  }

  // Note: this should run at the very beginning
  def precompileFunctions(funs: List[FunctionDef]): Unit = {
    def runFunction(f: FunctionDef): Comp[E, Rep[Value]] = {
      precompileBlocks(f.id, f.blocks)
      execBlock(f.id, f.blocks(0))
    }
    def repRunFun(f: FunctionDef)(ss: Rep[SS]): Rep[List[(SS, Value)]] = {
      reify[Value](ss)(runFunction(f))
    }

    for (f <- funs) {
      if (CompileTimeRuntime.FunFuns.contains(f.id)) {
        System.err.println("Already compiled " + f)
      } else {
        // FIXME: topFun or fun?
        val repf: Rep[SS => List[(SS, Value)]] = fun(repRunFun(f))
        CompileTimeRuntime.FunFuns(f.id) = repf
      }
    }
  }


}
