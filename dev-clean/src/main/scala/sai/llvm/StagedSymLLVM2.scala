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
  trait Mem
  trait Value
  trait SMTExpr
  type Addr = Int

  // TODO: if there is no dyanmic heap allocation, the object
  //       Heap can be a Map[Rep[Addr], Rep[Value]] (or maybe Array[Rep[Value]])
  type Heap = Mem
  type Stack = Mem

  type PC = Set[SMTExpr]
  type SS = (Heap, Stack, PC)
  type E = State[Rep[SS], *] ⊗ (Nondet ⊗ ∅)

  def emptyMem: Rep[Mem] = Wrap[Mem](Adapter.g.reflect("mt_mem"))

  // TODO: can be Comp[E, Unit]?
  def putState(s: Rep[SS]): Comp[E, Unit] = for { _ <- put[Rep[SS], E](s) } yield ()
  def getState: Comp[E, Rep[SS]] = get[Rep[SS], E]

  def getHeap: Comp[E, Rep[Heap]] = for { s <- get[Rep[SS], E] } yield s._1
  def putHeap(h: Rep[Heap]) = for {
    s <- get[Rep[SS], E]
    _ <- put[Rep[SS], E]((h, s._2, s._3))
  } yield ()

  def getPC: Comp[E, Rep[PC]] = for { s <- get[Rep[SS], E] } yield s._3
  def updatePC(x: Rep[SMTExpr]): Comp[E, Rep[Unit]] = for { 
    s <- getState
    _ <- putState((s._1, s._2, s._3 ++ Set(x)))
  } yield ()

  def getStack: Comp[E, Rep[Stack]] = for { s <- get[Rep[SS], E] } yield s._2
  def putStack(st: Rep[Stack]): Comp[E, Rep[Unit]] = for {
    s <- getState
    _ <- putState((s._1, st, s._3))
  } yield ()

  def popFrame(keep: Rep[Int]): Comp[E, Rep[Unit]] =
    for {
      s <- getState
      _ <- putState((s._1, s._2.take(keep), s._3))
    } yield ()

  def stackAlloc(size: Int): Comp[E, Rep[Addr]] = {
    for {
      st <- getStack
      a <- {
        val (st_, a) = st.alloc(size)
        putStack(st_).map { _ => a }
      }
    } yield a
  }

  def stackUpdate(k: Rep[Addr], v: Rep[Value]): Comp[E, Rep[Unit]] =
    for {
      st <- getStack
      _ <- putStack(st.update(k, v))
    } yield ()

  def stackUpdate(x: String, v: Rep[Value]): Comp[E, Rep[Unit]] =
    for {
      st <- getStack
      _ <- {
        val (st_, a) = st.alloc(1)
        StackAddr.save(x, a)
        putStack(st_.update(a, v))
      }
    } yield ()

  def stackUpdate(xs: List[String], vs: Rep[List[Value]]): Comp[E, Rep[Unit]] = {
    // TODO: improve this
    if (xs.isEmpty) ret(())
    else {
      val x = xs.head
      val v = vs.head
      for {
        _ <- stackUpdate(x, v)
        _ <- stackUpdate(xs.tail, vs.tail)
      } yield ()
    }
  }

  def updateMem(k: Rep[Value], v: Rep[Value]): Comp[E, Rep[Unit]] = {
    // if v is a HeapAddr, update heap, otherwise update its frame memory
    // v should be a LocV and wrap an actual location
    for {
      s <- getState
      _ <- {
        val newState = Wrap[SS](Adapter.g.reflect("update_mem", Unwrap(s), Unwrap(k), Unwrap(v)))
        putState(newState)
      }
    } yield ()
  }

  implicit class MemOps(σ: Rep[Mem]) {
    def alloc(size: Int): (Rep[Mem], Rep[Addr]) = {
      val a = Wrap[Addr](Adapter.g.reflectWrite("fresh_addr", Unwrap(σ))(Adapter.CTRL))
      val m = Wrap[Mem](Adapter.g.reflectWrite("mem_alloc", Unwrap(σ), Backend.Const(size))(Adapter.CTRL))
      (m, a)
    }
    def size: Rep[Int] = Wrap[Int](Adapter.g.reflect("mem_size", Unwrap(σ)))

    def lookup(a: Rep[Addr]): Rep[Value] =
      Wrap[Value](Adapter.g.reflect("mem_lookup", Unwrap(σ), Unwrap(a)))

    def update(k: Rep[Addr], v: Rep[Value]): Rep[Mem] = {
      // Note: since allocation doesn't put anything at `k`,
      //       we need to check if `k` is a valid address.
      //       Most of the cases, σ.size == k, and we can just append `v`.
      Wrap[Mem](Adapter.g.reflect("mem_update", Unwrap(σ), Unwrap(k), Unwrap(v)))
    }

    // Note: only used for the global memory
    def updateL(k: Rep[Addr], v: Rep[List[Value]]): Rep[Mem] =
      Wrap[Mem](Adapter.g.reflect("mem_updateL", Unwrap(σ), Unwrap(k), Unwrap(v)))

    def take(n: Rep[Int]): Rep[Mem] =
      Wrap[Mem](Adapter.g.reflect("mem_take", Unwrap(σ), Unwrap(n)))
  }

  object StackAddr {
    def save(x: String, a: Rep[Addr]): Rep[Unit] = {
      Wrap[Unit](Adapter.g.reflectWrite("stack_addr_save", Backend.Const(x), Unwrap(a))(Adapter.CTRL))
    }
    def apply(st: Rep[Mem], x: String): Rep[Addr] = {
      /*
      if (!CompileTimeRuntime.env.contains(x)) {
        System.err.println(s"Not allocated $x!")
      } else {
        System.err.println(s"Hit $x!")
        System.err.println(CompileTimeRuntime.env(x))
        CompileTimeRuntime.env(x)
      }
      */
      Wrap[Addr](Adapter.g.reflectWrite("stack_addr", Unwrap(st), Backend.Const(x))(Adapter.CTRL))
    }
  }

  object HeapAddr {
    def apply(x: String): Rep[Addr] = {
      Wrap[Addr](Adapter.g.reflectWrite("heap_addr", Backend.Const(x))(Adapter.CTRL))
    }
  }
  
  implicit class AddrOps(a: Rep[Addr]) {
    // def +(x: Rep[Int]): Rep[Addr] = x + a
      // Wrap[Addr](Adapter.g.reflect("addr_plus", Unwrap(a), Unwrap(x)))
  }

  object IntV {
    def apply(i: Rep[Int]): Rep[Value] = IntV(i, 32)
    def apply(i: Rep[Int], bw: Int): Rep[Value] =
      Wrap[Value](Adapter.g.reflectWrite("make_IntV", Unwrap(i), Backend.Const(bw))(Adapter.CTRL))
  }
  object LocV {
    def kStack: Rep[Int] =
      Wrap[Int](Adapter.g.reflectWrite("kStack")(Adapter.CTRL))
    def kHeap: Rep[Int] =
      Wrap[Int](Adapter.g.reflectWrite("kHeap")(Adapter.CTRL))
    def apply(l: Rep[Addr], kind: Rep[Int]): Rep[Value] = 
      Wrap[Value](Adapter.g.reflectWrite("make_LocV", Unwrap(l), Unwrap(kind))(Adapter.CTRL))
  }
  object FunV {
    def apply(f: Rep[(SS, List[Value]) => List[(SS, Value)]]): Rep[Value] = Wrap[Value](Unwrap(f))
  }

  implicit class ValueOps(v: Rep[Value]) {
    // if v is a HeapAddr, return heap, otherwise return its frame memory
    def selectMem: Comp[E, Rep[Mem]] = for {
      s <- getState
    } yield Wrap[Mem](Adapter.g.reflect("select_mem", Unwrap(v), Unwrap(s._1), Unwrap(s._2)))

    def loc: Rep[Addr] = Wrap[Addr](Adapter.g.reflect("proj_LocV", Unwrap(v)))
    def int: Rep[Int] = Wrap[Int](Adapter.g.reflect("proj_IntV", Unwrap(v)))
    def fun: Rep[(SS, List[Value]) => List[(SS, Value)]] =
      Wrap[(SS, List[Value]) => List[(SS, Value)]](Unwrap(v))

    def toSMT: Rep[SMTExpr] =
      Wrap[SMTExpr](Adapter.g.reflect("proj_SMTExpr", Unwrap(v)))
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

    val env: collection.mutable.HashMap[String, Rep[Addr]] =
      new collection.mutable.HashMap[String, Rep[Addr]]

    def getTySize(vt: LLVMType, align: Int = 1): Int = vt match {
      case ArrayType(size, ety) =>
        val rawSize = size * getTySize(ety, align)
        if (rawSize % align == 0) rawSize
        else (rawSize / align + 1) * align
      case _ => 1
    }

    def calculateOffset(ty: LLVMType, index: List[Rep[Int]]): Rep[Int] = {
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

  // TODO: PhiInst, record branches in SS
  import CompileTimeRuntime._
  import Magic._

  def eval(v: LLVMValue)(implicit funName: String): Comp[E, Rep[Value]] = {
    v match {
      case LocalId(x) =>
        for { st <- getStack } yield { st.lookup(StackAddr(st, funName + "_" + x)) }
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
          case TypedParam(ty, attrs, localId) => funDef.id + "_" + localId.get
        }
        if (!CompileTimeRuntime.FunFuns.contains(id)) {
          precompileFunctions(SList(funMap(id)))
        }
        val f: Rep[SS] => Rep[List[(SS, Value)]] = CompileTimeRuntime.FunFuns(id)
        def repf(s: Rep[SS], args: Rep[List[Value]]): Rep[List[(SS, Value)]] = {
          val m: Comp[E, Rep[Value]] = for {
            _ <- stackUpdate(params, args)
            s <- getState
            v <- reflect(f(s))
          } yield v
          reify(s)(m)
        }
        ret(FunV(topFun(repf)))
        // ret(FunV(fun(repf)))
      case GlobalId(id) if funDeclMap.contains(id) => 
        val v = id match {
          case "@printf" => Primitives.printf
          case "@read" => Primitives.read
          case "@exit" => ??? // returns nondet fail? this should be something like a break
          case "@sleep" => ??? //noop
        }
        ret(v)
      case GlobalId(id) if globalDefMap.contains(id) =>
        for { h <- getHeap } yield h.lookup(CompileTimeRuntime.heapEnv(id))
      case GetElemPtrExpr(_, baseType, ptrType, const, typedConsts) => 
        // typedConst are not all int, could be local id
        val indexLLVMValue = typedConsts.map(tv => tv.const)
        for {
          vs <- mapM(indexLLVMValue)(eval)
          lV <- eval(const)
        } yield {
          val indexValue = vs.map(v => v.int)
          val offset = calculateOffset(ptrType, indexValue)
          const match {
            case GlobalId(id) => LocV(heapEnv(id) + offset, LocV.kHeap)
            case _ => LocV(lV.loc + offset, LocV.kStack)
          }
        }
      case ZeroInitializerConst => ret(IntV(0))
    }
  }

  def execValueInst(inst: ValueInstruction)(implicit funName: String): Comp[E, Rep[Value]] = {
    inst match {
      case AllocaInst(ty, align) =>
        for {
          a <- stackAlloc(getTySize(ty, align.n))
        } yield LocV(a, LocV.kStack)
      case LoadInst(valTy, ptrTy, value, align) =>
        for {
          v <- eval(value)
          σ <- v.selectMem
        } yield σ.lookup(v.loc)
      case AddInst(ty, lhs, rhs, _) =>
        for {
          v1 <- eval(lhs)
          v2 <- eval(rhs)
        } yield IntV(v1.int + v2.int)
      case SubInst(ty, lhs, rhs, _) =>
        for {
          v1 <- eval(lhs)
          v2 <- eval(rhs)
        } yield IntV(v1.int - v2.int)
      case MulInst(ty, lhs, rhs, _) =>
        for {
          v1 <- eval(lhs)
          v2 <- eval(rhs)
        } yield IntV(v1.int * v2.int)
      case ICmpInst(pred, ty, lhs, rhs) =>
        for {
          val1 <- eval(lhs)
          val2 <- eval(rhs)
        } yield {
          val v1 = val1.int
          val v2 = val2.int
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
      case ZExtInst(from, value, to) => for {
        v <- eval(value)
      } yield IntV(v.int, to.asInstanceOf[IntType].size)
      case SExtInst(from, value, to) =>  for {
        v <- eval(value)
      } yield IntV(v.int, to.asInstanceOf[IntType].size)
      case CallInst(ty, f, args) => 
        val argValues: List[LLVMValue] = args.map {
          case TypedArg(ty, attrs, value) => value
        }
        for {
          fv <- eval(f)
          vs <- mapM(argValues)(eval)
          s <- getState
          v <- reflect(fv.fun(s, List(vs:_*)))
          _ <- popFrame(s._2.size)
        } yield v
      case GetElemPtrInst(_, baseType, ptrType, ptrValue, typedValues) =>
        // it seems that typedValues must be IntConst
        val indexLLVMValue = typedValues.map(tv => tv.value)
        for {
          vs <- mapM(indexLLVMValue)(eval)
          lV <- eval(ptrValue)
        } yield {
          val indexValue = vs.map(v => v.int)
          val offset = calculateOffset(ptrType, indexValue)
          ptrValue match {
            case GlobalId(id) => LocV(heapEnv(id) + offset, LocV.kHeap)
            case _ => LocV(lV.loc + offset, LocV.kStack)
          }
        }
      case PhiInst(ty, incs) => ???
      case SelectInst(cndTy, cndVal, thnTy, thnVal, elsTy, elsVal) =>
        for {
          cnd <- eval(cndVal)
          v <- choice(
            for {
              _ <- updatePC(cnd.toSMT)
              v <- eval(thnVal)
            } yield v,
            for {
              _ <- updatePC(/* not */cnd.toSMT)
              v <- eval(elsVal)
            } yield v
          )
        } yield v
    }
  }

  // Note: Comp[E, Rep[Value]] vs Comp[E, Rep[Option[Value]]]?
  def execTerm(funName: String, inst: Terminator): Comp[E, Rep[Value]] = {
    inst match {
      case RetTerm(ty, Some(value)) => eval(value)(funName)
      case RetTerm(ty, None) => ret(IntV(0))
      case BrTerm(lab) =>
        execBlock(funName, lab)
        // branches = lab :: branches
      case CondBrTerm(ty, cnd, thnLab, elsLab) =>
        // TODO: needs to consider the case wehre cnd is a concrete value
        // val cndM: Rep[SMTExpr] = ???
        /*
        for {
          cndVal <- eval(cnd)(funName)
          v <- choice(
            for {
              _ <- updatePC(cndVal)
              v <- execBlock(funName, thnLab)
            } yield v,
            for {
              _ <- updatePC(/* not */cndVal)
              // update branches
              v <- execBlock(funName, elsLab)
            } yield v)
        } yield v
         */
        // Temp: concrete execution below:
        for {
          cndVal <- eval(cnd)(funName)
          s <- getState
          v <- {
            reflect(if (cndVal.int == 1) {
              reify(s)(execBlock(funName, thnLab))
            } else {
              reify(s)(execBlock(funName, elsLab))
            })
          }
        } yield v
      case SwitchTerm(cndTy, cndVal, default, table) =>
        // TODO: cndVal can be either concrete or symbolic
        // TODO: if symbolic, update PC here, for default, take the negation of all other conditions
        // Note: this is now a concrete switch
        def switchFun(v: Rep[Int], s: Rep[SS], table: List[LLVMCase]): Rep[List[(SS, Value)]] = {
          if (table.isEmpty) execBlock(funName, default, s)
          else {
            if (v == table.head.n) execBlock(funName, table.head.label, s)
            else switchFun(v, s, table.tail)
          }
        }
        for {
          v <- eval(cndVal)(funName)
          s <- getState
          r <- reflect(switchFun(v.int, s, table))
        } yield r
    }
  }

  def execInst(inst: Instruction)(implicit fun: String): Comp[E, Rep[Unit]] = {
    inst match {
      case AssignInst(x, valInst) =>
        for {
          v <- execValueInst(valInst)(fun)
          _ <- stackUpdate(fun + "_" + x, v) //FIXME: x is not unique
        } yield ()
      case StoreInst(ty1, val1, ty2, val2, align) =>
        for {
          v1 <- eval(val1)(fun)
          v2 <- eval(val2)(fun)
          _ <- updateMem(v2, v1)
        } yield ()
      case CallInst(ty, f, args) =>
        val argValues: List[LLVMValue] = args.map {
          case TypedArg(ty, attrs, value) => value
        }
        for {
          fv <- eval(f)
          vs <- mapM(argValues)(eval)
          s <- getState
          v <- reflect(fv.fun(s, List(vs:_*)))
          _ <- popFrame(s._2.size)
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
    def evalConst(v: Constant): List[Rep[Value]] = v match {
      case BoolConst(b) =>
        SList(IntV(if (b) 1 else 0, 1))
      case IntConst(n) =>
        SList(IntV(n))
      case ZeroInitializerConst =>
        SList(IntV(0))
      case ArrayConst(cs) =>
        flattenArray(v).flatMap(c => evalConst(c))
      case CharArrayConst(s) =>
        s.map(c => IntV(c.toInt, 8)).toList
    }

    CompileTimeRuntime.globalDefMap.foldRight(heap) {case ((k, v), h) =>
      val (allocH, addr) = h.alloc(getTySize(v.typ))
      CompileTimeRuntime.heapEnv = CompileTimeRuntime.heapEnv + (k -> addr)
      allocH.updateL(addr, List(evalConst(v.const):_*))
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
        _ <- mapM(is)(execInst(_)(funName))
        v <- execTerm(funName, term)
      } yield v
    }
    def runBlock(b: BB)(ss: Rep[SS]): Rep[List[(SS, Value)]] = {
      reify[Value](ss)(runInstList(b.ins, b.term))
    }

    for (b <- blocks) {
      if (CompileTimeRuntime.BBFuns.contains(b)) {
        System.err.println("Already compiled " + b)
      } else {
        // val repRunBlock: Rep[SS => List[(SS, Value)]] = fun(runBlock(b))
        val repRunBlock: Rep[SS => List[(SS, Value)]] = topFun(runBlock(b))
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
        CompileTimeRuntime.FunFuns(f.id) = repRunFun(f)
      }
    }
  }

  def exec(m: Module, fname: String, args: Rep[List[Value]]): Rep[List[(SS, Value)]] = {
    CompileTimeRuntime.funMap = m.funcDefMap
    CompileTimeRuntime.funDeclMap = m.funcDeclMap
    CompileTimeRuntime.globalDefMap = m.globalDefMap

    val heap0 = precompileHeap(emptyMem)
    val comp = for {
      fv <- eval(GlobalId(fname))(fname)
      s <- getState
      v <- reflect(fv.fun(s, args))
      _ <- popFrame(s._2.size)
    } yield v
    val initState: Rep[SS] = (heap0, emptyMem, Set[SMTExpr]())
    reify[Value](initState)(comp)
  }
}

trait SymStagedLLVMGen extends CppSAICodeGenBase {
  registerHeader("./headers", "<sai_llvm_sym2.hpp>")

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, name, _, _) if name.startsWith("IntV") => false
    case Node(_, name, _, _) if name.startsWith("LocV") => false
    case Node(_, "stack_addr", _, _) => true
    case Node(_, "heap_addr", _, _) => true
    case _ => super.mayInline(n)
  }

  override def quote(s: Def): String = s match {
    case Const(()) => "std::monostate{}";
    case _ => super.quote(s)
  }

  // Note: depends on the concrete representation of Mem, we can emit different code
  override def shallow(n: Node): Unit = n match {
    // case Node(s, "mem-lookup", List(σ, a), _) =>
    case _ => super.shallow(n)
  }
}

trait CppSymStagedLLVMDriver[A, B] extends CppSAIDriver[A, B] with StagedSymExecEff { q =>
  override val codegen = new CGenBase with SymStagedLLVMGen {
    val IR: q.type = q
    import IR._

    override def primitive(t: String): String = t match {
      case "Unit" => "std::monostate"
      case _ => super.primitive(t)
    }

    override def remap(m: Manifest[_]): String = {
      if (m.toString == "java.lang.String") "String"
      else if (m.toString.endsWith("$Value")) "PtrVal"
      else if (m.toString.endsWith("$Addr")) "Addr"
      else if (m.toString.endsWith("$Mem")) "Mem"
      else if (m.toString.endsWith("SMTExpr")) "PtrVal" //FIXME
      else super.remap(m)
    }
  }
}

object TestStagedLLVM {
  def parse(file: String): Module = {
    val input = scala.io.Source.fromFile(file).mkString
    sai.llvm.LLVMTest.parse(input)
  }

  val add = parse("llvm/benchmarks/add.ll")
  val power = parse("llvm/benchmarks/power.ll")
  // val singlepath = parse("llvm/benchmarks/single_path5.ll")
  val branch = parse("llvm/benchmarks/branch2.ll")
  val multipath= parse("llvm/benchmarks/multipath.ll")
  val arrayAccess = parse("llvm/benchmarks/arrayAccess.ll")

  @virtualize
  def specialize(m: Module, fname: String): CppSAIDriver[Int, Unit] =
    new CppSymStagedLLVMDriver[Int, Unit] {
      def snippet(u: Rep[Int]) = {
        //def exec(m: Module, fname: String, s0: Rep[Map[Loc, Value]]): Rep[List[(SS, Value)]]
        //val s = Map(FrameLoc("f_%x") -> IntV(5), FrameLoc("f_%y") -> IntV(2))
        //val s = Map(FrameLoc("%x") -> IntV(5))
        // val s = Map(FrameLoc("f_%a") -> IntV(5),
        // FrameLoc("f_%b") -> IntV(6),
        //FrameLoc("f_%c") -> IntV(7))
        val args: Rep[List[Value]] = List[Value]()
        val s = Map()
        val res = exec(m, fname, args)
        println(res.size)
      }
    }

  def testArrayAccess = {
    // problem: heap loc undefined in other functions
    val code = specialize(arrayAccess, "@main")
    code.save("gen/arrayAccess.cpp")
    println(code.code)
    code.compile("gen/arrayAccess.cpp")
    code.eval(0)
  }

  def testPower = {
    // problem: FrameEnv override in recursive call
    val code = specialize(power, "@main")
    code.save("gen/power.cpp")
    println(code.code)
    code.compile("gen/power.cpp")
    code.eval(0)
  }
  def main(args: Array[String]): Unit = {
    
    // val code = specialize(power, "@main")
    //val code = specialize(singlepath, "@singlepath")
    // val code = specialize(branch, "@f")
    //val code = specialize(multipath, "@f")
    //println(code.code)
    //code.eval(5)

    testArrayAccess
    testPower

    
    println("Done")
  }
}
