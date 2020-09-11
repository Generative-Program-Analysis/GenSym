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

  // TODO: can be Comp[E, Unit]?
  def putState(s: Rep[SS]): Comp[E, Rep[Unit]] = for { _ <- put[Rep[SS], E](s) } yield ()
  def getState: Comp[E, Rep[SS]] = get[Rep[SS], E]
  def getHeap: Comp[E, Rep[Heap]] = for { s <- get[Rep[SS], E] } yield s._1
  def putHeap(h: Rep[Heap]) = for {
    s <- get[Rep[SS], E]
    _ <- put[Rep[SS], E]((h, s._2, s._3))
  } yield ()
  def getStack: Comp[E, Rep[Stack]] = for { s <- get[Rep[SS], E] } yield s._2
  def getPC: Comp[E, Rep[PC]] = for { s <- get[Rep[SS], E] } yield s._3
  def updatePC(x: Rep[SMTExpr]): Comp[E, Rep[Unit]] = for { 
    s <- get[Rep[SS], E]
    _ <- put[Rep[SS], E]((s._1, s._2, s._3 ++ Set(x)))
  } yield ()
  def curFrame: Comp[E, Rep[Frame]] = for { fs <- getStack } yield fs.head

  object Mem {
    import Addr._
    // it seems that heapEnv can be static
    // def heapEnv(s: Rep[String]): Comp[E, Rep[Addr]] = ???
    def frameEnv(s: Rep[String]): Comp[E, Rep[Addr]] = ???

    def lookup(σ: Rep[Mem], a: Rep[Addr]): Rep[Value] =
      Wrap[Value](Adapter.g.reflect("mem-lookup", Unwrap(σ), Unwrap(a)))
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
    def updateL(σ: Rep[Mem], k: Rep[Addr], v: List[Rep[Value]]): Rep[Mem] = ???
    def frameUpdate(k: Rep[Addr], v: Rep[Value]): Comp[E, Rep[Unit]] = {
      ???
    }
    def frameUpdate(x: String, v: Rep[Value]): Comp[E, Rep[Unit]] = for {
      s <- getState
      addr <- frameEnv(x)
      _ <- frameUpdate(addr, v)
    } yield ()
    def frameUpdate(xs: List[String], vs: Rep[List[Value]]): Comp[E, Rep[Unit]] = {
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

    def pushFrame(f: String): Comp[E, Rep[Unit]] = ???
    def popFrame: Comp[E, Rep[Unit]] = ???
  }

  object Addr {
    def localAddr(f: Rep[Frame], x: String): Rep[Addr] = ???
    def heapAddr(x: String): Rep[Addr] = ???
    
    implicit class AddrOP(a: Rep[Addr]) {
      def +(x: Int): Rep[Addr] = ???
    }
  }

  object Value {
    def IntV(i: Rep[Int]): Rep[Value] = IntV(i, 32)
    def IntV(i: Rep[Int], bw: Int): Rep[Value] = ???
    def LocV(l: Rep[Addr]): Rep[Value] = ???
    def FunV(f: Rep[(SS, List[Value]) => List[(SS, Value)]]): Rep[Value] = ???

    def projLocV(v: Rep[Value]): Rep[Addr] = ???
    def projIntV(v: Rep[Value]): Rep[Int] = ???
    def projFunV(v: Rep[Value]): Rep[(SS, List[Value]) => List[(SS, Value)]] = ???
  }

  object CompileTimeRuntime {
    var funMap: collection.immutable.Map[String, FunctionDef] = SMap()
    var funDeclMap: collection.immutable.Map[String, FunctionDecl] = SMap()
    var globalDefMap: collection.immutable.Map[String, GlobalDef] = SMap()
    var heapEnv: collection.immutable.Map[String, Rep[Addr]] = SMap()

    val BBFuns: collection.mutable.HashMap[BB, Rep[SS => List[(SS, Value)]]] =
      new collection.mutable.HashMap[BB, Rep[SS => List[(SS, Value)]]]
    val FunFuns: collection.mutable.HashMap[String, Rep[SS] => Rep[List[(SS, Value)]]] =
      new collection.mutable.HashMap[String, Rep[SS] => Rep[List[(SS, Value)]]]

    def getTySize(vt: LLVMType, align: Int = 1): Int = vt match {
      case ArrayType(size, ety) =>
        val rawSize = size * getTySize(ety, align)
        if (rawSize % align == 0) rawSize
        else (rawSize / align + 1) * align
      case _ => 1
    }

    def calculateOffset(ty: LLVMType, index: List[Int]): Int = {
      if (index.isEmpty) 0 else ty match {
        case PtrType(ety, addrSpace) =>
          index.head * getTySize(ety) + calculateOffset(ety, index.tail)
        case ArrayType(size, ety) =>
          index.head * getTySize(ety) + calculateOffset(ety, index.tail)
        case _ => ???
      }
    }

    def flattenArray(cst: Constant): List[Constant] = cst match {
      case ArrayConst(xs) =>
        xs.map(typC => typC.const).foldRight(SList[Constant]())((con, ls) => flattenArray(con) ++ ls)
      case _ => SList(cst)
    }

    def findBlock(fname: String, lab: String): Option[BB] = {
      funMap.get(fname).get.lookupBlock(lab)
    }
    def findFirstBlock(fname: String): BB = {
      findFundef(fname).body.blocks(0)
    }
    def findFundef(fname: String) = funMap.get(fname).get
  }

  object Primitives {
    import Value._
    def __printf(s: Rep[SS], args: Rep[List[Value]]): Rep[List[(SS, Value)]] = {
      // generate printf
      ???
    }
    def printf: Rep[Value] = FunV(fun(__printf))

    def __read(s: Rep[SS], args: Rep[List[Value]]): Rep[List[(SS, Value)]] = {
      ???
    }
    def read: Rep[Value] = FunV(fun(__read))
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

    def mapM[A, B](xs: List[A])(f: A => Comp[E, B]): Comp[E, List[B]] = xs match {
      case Nil => ret(SList())
      case x::xs =>
        for {
          b <- f(x)
          bs <- mapM(xs)(f)
        } yield b::bs
    }
  }

  // TODO:
  // eval ArrayConst
  // ICmpInst
  // PhiInst, record branches in SS
  // SwitchTerm
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
      // case ArrayConst(cs) => 
      case BitCastExpr(from, const, to) =>
        eval(const)
      case BoolConst(b) => b match {
        case true => ret(IntV(1))
        case false => ret(IntV(0))
      }
      // case CharArrayConst(s) => 
      case GlobalId(id) if funMap.contains(id) =>
        val funDef = funMap(id)
        val params: List[String] = funDef.header.params.map {
          case TypedParam(ty, attrs, localId) => localId.get
        }
        if (!CompileTimeRuntime.FunFuns.contains(id)) {
          precompileFunctions(SList(funMap(id)))
        }
        val f: Rep[SS] => Rep[List[(SS, Value)]] = CompileTimeRuntime.FunFuns(id)
        def repf(s: Rep[SS], args: Rep[List[Value]]): Rep[List[(SS, Value)]] = {
          val m: Comp[E, Rep[Value]] = for {
            _ <- frameUpdate(params, args)
            s <- getState
            v <- reflect(f(s))
          } yield v
          reify(s)(m)
        }
        ret(FunV(fun(repf)))
      case GlobalId(id) if funDeclMap.contains(id) => 
        val v = id match {
          case "@printf" => Primitives.printf
          case "@read" => Primitives.read
          case "@exit" => ??? // generate an exit
          case "@sleep" => ??? //noop
        }
        ret(v)
      case GlobalId(id) if globalDefMap.contains(id) =>
        for { h <- getHeap } yield lookup(h, heapAddr(id))
      case GetElemPtrExpr(_, baseType, ptrType, const, typedConsts) => 
        val indexValue: List[Int] = typedConsts.map(tv => tv.const.asInstanceOf[IntConst].n)
        val offset = calculateOffset(ptrType, indexValue)
        const match {
          case GlobalId(id) => ret(LocV(heapEnv(id) + offset))
          case _ => for {
            lV <- eval(const)
          } yield LocV(projLocV(lV) + offset)
        }
      case ZeroInitializerConst => ret(IntV(0))
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
      // case ICmpInst(pred, ty, lhs, rhs) =>
      //   for {
      //     val1 <- eval(lhs)
      //     val2 <- eval(rhs)
      //   } yield {
      //     val v1 = ProjInt(val1)
      //     val v2 = ProjInt(val2)
      //     pred match {
      //       case EQ => IntV(if (v1 == v2) 1 else 0)
      //       case NE => IntV(if (v1 != v2) 1 else 0)
      //       case SLT => IntV(if (v1 < v2) 1 else 0)
      //       case SLE => IntV(if (v1 <= v2) 1 else 0)
      //       case SGT => IntV(if (v1 > v2) 1 else 0)
      //       case SGE => IntV(if (v1 >= v2) 1 else 0)
      //       case ULT => IntV(if (v1 < v2) 1 else 0)
      //       case ULE => IntV(if (v1 <= v2) 1 else 0)
      //       case UGT => IntV(if (v1 > v2) 1 else 0)
      //       case UGE => IntV(if (v1 >= v2) 1 else 0)
      //     }
      //   }
      case ZExtInst(from, value, to) => for {
        v <- eval(value)
      } yield IntV(projIntV(v), to.asInstanceOf[IntType].size)
      case SExtInst(from, value, to) =>  for {
        v <- eval(value)
      } yield IntV(projIntV(v), to.asInstanceOf[IntType].size)
      case CallInst(ty, f, args) => 
        val argValues: List[LLVMValue] = args.map {
          case TypedArg(ty, attrs, value) => value
        }
        for {
          fv <- eval(f)
          vs <- mapM(argValues)(eval)
          // FIXME: potentially problematic: 
          // f could be bitCast as well
          // GW: yes, f could be bitCast, but after the evaluation, fv should be a function, right?
          _ <- pushFrame(f.asInstanceOf[GlobalId].id)
          s <- getState
          v <- reflect(projFunV(fv)(s, List(vs:_*)))
          _ <- popFrame
        } yield v
      case GetElemPtrInst(_, baseType, ptrType, ptrValue, typedValues) =>
        // it seems that typedValues must be IntConst
        val indexValue: List[Int] = typedValues.map(tv => tv.value.asInstanceOf[IntConst].n)
        val offset = calculateOffset(ptrType, indexValue)
        ptrValue match {
          case GlobalId(id) => ret(LocV(heapEnv(id) + offset))
          case _ => for {
            lV <- eval(ptrValue)
          } yield LocV(projLocV(lV) + offset)
        }
      case PhiInst(ty, incs) => ???
      case SelectInst(cndTy, cndVal, thnTy, thnVal, elsTy, elsVal) =>
        val cndM: Rep[SMTExpr] = ???
        val thnM = for {
          _ <- updatePC(cndM)
          v <- eval(thnVal)
        } yield v
        val elsM = for {
          _ <- updatePC(/* not */cndM)
          v <- eval(elsVal)
        } yield v
        choice(thnM, elsM)
    }
  }

  // Note: Comp[E, Rep[Value]] vs Comp[E, Rep[Option[Value]]]?
  def execTerm(funName: String, inst: Terminator): Comp[E, Rep[Value]] = {
    inst match {
      case RetTerm(ty, Some(value)) => eval(value)
      case RetTerm(ty, None) => ret(IntV(0))
      case BrTerm(lab) =>
        execBlock(funName, lab)
        // branches = lab :: branches
      case CondBrTerm(ty, cnd, thnLab, elsLab) =>
        // TODO: needs to consider the case wehre cnd is a concrete value
        val cndM: Rep[SMTExpr] = ???
        val thnM = for {
          _ <- updatePC(cndM)
          // update branches
          v <- execBlock(funName, thnLab)
        } yield v
        val elsM = for {
          _ <- updatePC(/* not */cndM)
          // update branches
          v <- execBlock(funName, elsLab)
        } yield v
        choice(thnM, elsM)
      case SwitchTerm(cndTy, cndVal, default, table) =>
        // TODO: cndVal can be either concrete or symbolic
        // TODO: if symbolic, update PC here, for default, take the negation of all other conditions
        def switchFun(v: Rep[Int], s: Rep[SS], table: List[LLVMCase]): Rep[List[(SS, Value)]] = {
          if (table.isEmpty) execBlock(funName, default, s)
          else {
            if (v == table.head.n) execBlock(funName, table.head.label, s)
            else switchFun(v, s, table.tail)
          }
        }
        for {
          v <- eval(cndVal)
          s <- getState
          r <- reflect(switchFun(projIntV(v), s, table))
        } yield r
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
        val argValues: List[LLVMValue] = args.map {
          case TypedArg(ty, attrs, value) => value
        }
        for {
          fv <- eval(f)
          vs <- mapM(argValues)(eval)
          // FIXME: potentially problematic: 
          // f could be bitCast as well
          _ <- pushFrame(f.asInstanceOf[GlobalId].id)
          s <- getState
          v <- reflect(projFunV(fv)(s, List(vs:_*)))
          _ <- popFrame
        } yield ()
    }
  }

  def execBlock(funName: String, label: String, s: Rep[SS]): Rep[List[(SS, Value)]] = {
    val Some(block) = findBlock(funName, label)
    execBlock(funName, block, s)
  }

  def execBlock(funName: String, bb: BB, s: Rep[SS]): Rep[List[(SS, Value)]] = {
    if (!CompileTimeRuntime.BBFuns.contains(bb)) {
      precompileBlocks(funName, SList(bb))
    }
    val f = CompileTimeRuntime.BBFuns(bb)
    f(s)
  }

  def execBlock(funName: String, label: String): Comp[E, Rep[Value]] = {
    val Some(block) = findBlock(funName, label)
    execBlock(funName, block)
  }

  def execBlock(funName: String, bb: BB): Comp[E, Rep[Value]] = {
    for {
      s <- getState
      v <- reflect(execBlock(funName, bb, s))
    } yield v
  }

  def precompileHeap(heap: Rep[Heap]): Rep[Heap] = {
    def evalConst(v: Constant): Rep[Value] = v match {
      case BoolConst(b) =>
        IntV(if (b) 1 else 0, 1) 
      case IntConst(n) =>
        IntV(n)
      case ZeroInitializerConst =>
        IntV(0)
    }
    def evalConstL(v: Constant): List[Rep[Value]] = v match {
      case ArrayConst(cs) =>
        flattenArray(v).map(c => evalConst(c))
      case CharArrayConst(s) =>
        s.map(c => IntV(c.toInt, 8)).toList
    }

    CompileTimeRuntime.globalDefMap.foldRight(heap) {case ((k, v), h) =>
      val (allocH, addr) = alloc(h, getTySize(v.typ))
      CompileTimeRuntime.heapEnv = CompileTimeRuntime.heapEnv + (k -> addr)
      if (v.const.isInstanceOf[ArrayConst] || v.const.isInstanceOf[CharArrayConst]) {
        updateL(allocH, addr, evalConstL(v.const))
      } else {
        update(allocH, addr, evalConst(v.const))
      }
    }
  }

  def precompileHeapM: Comp[E, Rep[Unit]] = {
    for {
      h <- getHeap
      _ <- putHeap(precompileHeap(h))
    } yield ()
  }

  def precompileBlocks(funName: String, blocks: List[BB]): Unit = {
    def runInstList(is: List[Instruction], term: Terminator): Comp[E, Rep[Value]] = {
      for {
        _ <- mapM(is)(execInst(funName, _))
        v <- execTerm(funName, term)
      } yield v
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
        CompileTimeRuntime.FunFuns(f.id) = repRunFun(f)
      }
    }
  }
}

trait SymStagedLLVMGen extends CppSAICodeGenBase {
  registerHeader("./headers", "<sai_llvm_sym2.hpp>")

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, name, _, _) if name.startsWith("IntV") => false
    case Node(_, name, _, _) if name.startsWith("LocV") => false
    case _ => super.mayInline(n)
  }

  override def quote(s: Def): String = s match {
    case Const(()) => "std::monostate{}";
    case _ => super.quote(s)
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "mem-lookup", List(σ, a), _) =>
      // Note: depends on the concrete representation of Mem, we can emit different code
      ???
    case _ => super.shallow(n)
  }
}
