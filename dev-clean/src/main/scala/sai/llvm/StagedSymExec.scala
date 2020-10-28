package sai.llvm

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.Parser._

import org.antlr.v4.runtime._
import scala.collection.JavaConverters._

import sai.structure.freer._
import Eff._
import Freer._
import Handlers._
import OpenUnion._
import State._

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._
import sai.structure.lattices._
import sai.structure.lattices.Lattices._

import scala.collection.immutable.{List => SList}
import scala.collection.immutable.{Map => SMap}
import sai.lmsx.smt.SMTBool

// TODO
// Add Primitive: make_symbolic and assert

// TODO to make maze run symbolically
// make switch symbolic
// make select, condbr mixing conc and sym
// Primitives.read should take in Symbolic value
// When call exit(1), invoke solver to gen input

@virtualize
trait StagedSymExecEff extends SAIOps with RepNondet {
  trait Mem
  trait Value
  trait SMTExpr
  type Addr = Int

  type Heap = Mem
  type Env = Map[Int, Int]
  type FEnv = List[Env]
  type Stack = (Mem, FEnv)

  type PC = Set[SMTBool]
  type SS = (Heap, Mem, FEnv, PC)
  type E = State[Rep[SS], *] ⊗ (Nondet ⊗ ∅)

  implicit class SSOps(ss: Rep[SS]) {
    def heap: Rep[Heap] = ss._1
    def stackMem: Rep[Mem] = ss._2
    def stackEnv: Rep[FEnv] = ss._3
    def pc: Rep[PC] = ss._4

    def withHeap(h: Rep[Heap]): Rep[SS] = (h, stackMem, stackEnv, pc)
    def withStack(m: Rep[Mem], e: Rep[FEnv]): Rep[SS] = (heap, m, e, pc)
    def withStackMem(s: Rep[Mem]): Rep[SS] = (heap, s, stackEnv, pc)
    def withStackEnv(e: Rep[FEnv]): Rep[SS] = (heap, stackMem, e, pc)
    def withPC(p: Rep[PC]): Rep[SS] = (heap, stackMem, stackEnv, p)
  }

  def emptyMem: Rep[Mem] = Wrap[Mem](Adapter.g.reflect("mt_mem"))

  // TODO: can be Comp[E, Unit]?
  def putState(s: Rep[SS]): Comp[E, Unit] = for { _ <- put[Rep[SS], E](s) } yield ()
  def getState: Comp[E, Rep[SS]] = get[Rep[SS], E]

  def lookupCurEnv(x: String): Comp[E, Rep[Addr]] = for {
    s <- getState
  } yield {
    val env = s.stackEnv.head
    env(x.hashCode)
  }
  def updateCurEnv(x: String, a: Rep[Addr]): Comp[E, Rep[Unit]] = for {
    s <- getState
    _ <- {
      val newEnv = s.stackEnv.head + (x.hashCode -> a)
      putState(s.withStackEnv(newEnv :: s.stackEnv.tail))
    }
  } yield ()

  def pushEmptyEnv: Comp[E, Rep[Unit]] = for {
    s <- getState
    _ <- putState(s.withStackEnv(Map[Int, Int]() :: s.stackEnv))
  } yield ()

  def getHeap: Comp[E, Rep[Heap]] = for { s <- get[Rep[SS], E] } yield s.heap
  def putHeap(h: Rep[Heap]) = for {
    s <- get[Rep[SS], E]
    _ <- put[Rep[SS], E](s.withHeap(h))
  } yield ()

  def getPC: Comp[E, Rep[PC]] = for { s <- get[Rep[SS], E] } yield s.pc
  def updatePCSet(x: Rep[Set[SMTBool]]): Comp[E, Rep[Unit]] = for { 
    s <- getState
    _ <- putState(s.withPC(s.pc ++ x))
  } yield ()
  def updatePC(x: Rep[SMTBool]): Comp[E, Rep[Unit]] = for { 
    s <- getState
    _ <- putState(s.withPC(s.pc ++ Set(x)))
  } yield ()

  def getStackMem: Comp[E, Rep[Mem]] = for { s <- getState } yield s.stackMem
  def putStackMem(st: Rep[Mem]): Comp[E, Rep[Unit]] = for {
    s <- getState
    _ <- putState(s.withStackMem(st))
  } yield ()

  def popFrame(keep: Rep[Int]): Comp[E, Rep[Unit]] =
    for {
      s <- getState
      _ <- putState(s.withStack(s.stackMem.take(keep), s.stackEnv.tail))
    } yield ()

  def stackAlloc(size: Int): Comp[E, Rep[Addr]] = {
    for {
      st <- getStackMem
      a <- {
        val (st_, a) = st.alloc(size)
        putStackMem(st_).map { _ => a }
      }
    } yield a
  }

  def stackUpdate(k: Rep[Addr], v: Rep[Value]): Comp[E, Rep[Unit]] =
    for {
      st <- getStackMem
      _ <- putStackMem(st.update(k, v))
    } yield ()

  def stackUpdate(x: String, v: Rep[Value]): Comp[E, Rep[Unit]] =
    for {
      st <- getStackMem
      _ <- {
        val (st_, a) = st.alloc(1)
        for {
          _ <- updateCurEnv(x, a)
          _ <- putStackMem(st_.update(a, v))
        } yield ()
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
      Wrap[Mem](Adapter.g.reflect("mem_update", Unwrap(σ), Unwrap(k), Unwrap(v)))
    }

    // Note: only used for the global memory
    def updateL(k: Rep[Addr], v: Rep[List[Value]]): Rep[Mem] =
      Wrap[Mem](Adapter.g.reflect("mem_updateL", Unwrap(σ), Unwrap(k), Unwrap(v)))

    def take(n: Rep[Int]): Rep[Mem] =
      Wrap[Mem](Adapter.g.reflect("mem_take", Unwrap(σ), Unwrap(n)))
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
    def select_loc(v: Rep[Value]): Rep[Int] =
      Wrap[Int](Adapter.g.reflectWrite("select_loc", Unwrap(v))(Adapter.CTRL))
    def apply(l: Rep[Addr], kind: Rep[Int]): Rep[Value] = 
      Wrap[Value](Adapter.g.reflectWrite("make_LocV", Unwrap(l), Unwrap(kind))(Adapter.CTRL))
  }
  object FunV {
    def apply(f: Rep[(SS, List[Value]) => List[(SS, Value)]]): Rep[Value] = Wrap[Value](Unwrap(f))
  }
  object SymV {
    def apply(s: Rep[String]): Rep[Value] = Wrap[Value](Adapter.g.reflectWrite("make_SymV", Unwrap(s))(Adapter.CTRL))
    def apply(s: Rep[String], bw: Int): Rep[Value] = Wrap[Value](Adapter.g.reflectWrite("make_SymV", Unwrap(s), Backend.Const(bw))(Adapter.CTRL))
  }

  object Op2 {
    def apply(op: String, o1: Rep[Value], o2: Rep[Value]) =
      Wrap[Value](Adapter.g.reflect("op_2", Backend.Const(op), Unwrap(o1), Unwrap(o2)))
  }

  implicit class ValueOps(v: Rep[Value]) {
    // if v is a HeapAddr, return heap, otherwise return its frame memory
    def selectMem: Comp[E, Rep[Mem]] = for {
      s <- getState
    } yield Wrap[Mem](Adapter.g.reflect("select_mem", Unwrap(v), Unwrap(s.heap), Unwrap(s.stackMem)))

    def loc: Rep[Addr] = Wrap[Addr](Adapter.g.reflect("proj_LocV", Unwrap(v)))
    def int: Rep[Int] = Wrap[Int](Adapter.g.reflect("proj_IntV", Unwrap(v)))
    def fun: Rep[(SS, List[Value]) => List[(SS, Value)]] =
      Wrap[(SS, List[Value]) => List[(SS, Value)]](Unwrap(v))

    def isConc: Rep[Boolean] =
      Wrap[Boolean](Adapter.g.reflect("isConc", Unwrap(v)))
    def toSMTExpr: Rep[SMTExpr] =
      Wrap[SMTExpr](Adapter.g.reflect("proj_SMTExpr", Unwrap(v)))
    def toSMTBool: Rep[SMTBool] =
      Wrap[SMTBool](Adapter.g.reflect("proj_SMTBool", Unwrap(v)))
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

  // how to make symbolic?
  object Primitives extends java.io.Serializable {

    def __make_symbolic(s: Rep[SS], args: Rep[List[Value]]) = {
      Wrap[List[(SS, Value)]](Adapter.g.reflect("make_symbolic", Unwrap(s), Unwrap(args)))
    }
    def make_symbolic: Rep[Value] = FunV(topFun(__make_symbolic))

    def __printf(s: Rep[SS], args: Rep[List[Value]]): Rep[List[(SS, Value)]] = {
      Wrap[List[(SS, Value)]](Adapter.g.reflect("sym_printf", Unwrap(s), Unwrap(args)))
    }
    def printf: Rep[Value] = FunV(topFun(__printf))

    def __concreteReadForMaze(s: Rep[SS], args: Rep[List[Value]]): Rep[List[(SS, Value)]] = {
      Wrap[List[(SS, Value)]](Adapter.g.reflect("read_maze", Unwrap(s), Unwrap(args)))
    }

    // should we directly generate code for this or?
    def __assert_false(s: Rep[SS], args: Rep[List[Value]]): Rep[List[(SS, Value)]] = {
      // push
      // s.pc.toList.foreach(assert(_))
      // handle(query(lit(false)))
      // pop
      return List[(SS, Value)]((s, IntV(0)));
    }

    def assert_false: Rep[Value] = FunV(topFun(__assert_false))

    def read: Rep[Value] = FunV(topFun(__concreteReadForMaze))

    def __exit(s: Rep[SS], args: Rep[List[Value]]): Rep[List[(SS, Value)]] = {
      push
      s.pc.toList.foreach(assert(_))
      handle(query(lit(false)))
      pop
      return List[(SS, Value)]((s, IntV(0)));
    }
    def exit: Rep[Value] = FunV(topFun(__exit))

    def __sleep(s: Rep[SS], args: Rep[List[Value]]): Rep[List[(SS, Value)]] = {
      return List[(SS, Value)]();
    }
    def sleep: Rep[Value] = FunV(topFun(__sleep))
  }

  object Magic {
    def reify[T: Manifest](s: Rep[SS])(comp: Comp[E, Rep[T]]): Rep[List[(SS, T)]] = {
      val p1: Comp[Nondet ⊗ ∅, (Rep[SS], Rep[T])] =
        State.run2[Nondet ⊗ ∅, Rep[SS], Rep[T]](s)(comp)
      val p2: Comp[Nondet ⊗ ∅, Rep[(SS, T)]] = p1.map(a => a)
      val p3: Comp[∅, Rep[List[(SS, T)]]] = runRepNondet[(SS, T)](p2)
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

  import CompileTimeRuntime._
  import Magic._

  def eval(v: LLVMValue)(implicit funName: String): Comp[E, Rep[Value]] = {
    v match {
      case LocalId(x) =>
        for { 
          st <- getStackMem
          a <- lookupCurEnv(funName + "_" + x)
      } yield { st.lookup(a) }
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
      case GlobalId(id) if funDeclMap.contains(id) => 
        val v = id match {
          case "@printf" => Primitives.printf
          case "@read" => Primitives.read
          case "@exit" => Primitives.exit
          case "@sleep" => Primitives.sleep
          case "@assert" => Primitives.assert_false
          case "@make_symbolic" => Primitives.make_symbolic
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
            case _ => LocV(lV.loc + offset, LocV.select_loc(lV))
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
        } yield Op2("+", v1, v2)
      case SubInst(ty, lhs, rhs, _) =>
        for {
          v1 <- eval(lhs)
          v2 <- eval(rhs)
        } yield Op2("-", v1, v2)
      case MulInst(ty, lhs, rhs, _) =>
        for {
          v1 <- eval(lhs)
          v2 <- eval(rhs)
        } yield Op2("*", v1, v2)
      case ICmpInst(pred, ty, lhs, rhs) =>
        for {
          v1 <- eval(lhs)
          v2 <- eval(rhs)
        } yield {
          pred match {
            // TODO: distinguish signed and unsigned comparsion
            case EQ => Op2("=", v1, v2)
            case NE => Op2("!=", v1, v2)
            case SLT => Op2("<", v1, v2)
            case SLE => Op2("<=", v1, v2)
            case SGT => Op2(">", v1, v2)
            case SGE => Op2(">=", v1, v2)
            case ULT => Op2("<", v1, v2)
            case ULE => Op2("<=", v1, v2)
            case UGT => Op2(">", v1, v2)
            case UGE => Op2("<=", v1, v2)
          }
        }
      case ZExtInst(from, value, to) => for {
        v <- eval(value)
      } yield v
      // TODO return original for symbolic
      case SExtInst(from, value, to) =>  for {
        v <- eval(value)
      } yield v
      case CallInst(ty, f, args) => 
        val argValues: List[LLVMValue] = args.map {
          case TypedArg(ty, attrs, value) => value
        }
        for {
          fv <- eval(f)
          vs <- mapM(argValues)(eval)
          _ <- pushEmptyEnv
          s <- getState
          v <- reflect(fv.fun(s, List(vs:_*)))
          _ <- popFrame(s.stackMem.size)
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
            case _ => LocV(lV.loc + offset, LocV.select_loc(lV))
          }
        }
      // TODO change
      case PhiInst(ty, incs) => for {
        v <- eval(incs.head.value)
      } yield v 
      case SelectInst(cndTy, cndVal, thnTy, thnVal, elsTy, elsVal) =>
        // symbolic
        // for {
        //   cnd <- eval(cndVal)
        // v <- choice(
        //   for {
        //     _ <- updatePC(cnd.toSMTBool)
        //     v <- eval(thnVal)
        //   } yield v,
        //   for {
        //     _ <- updatePC(not(cnd.toSMTBool))
        //     v <- eval(elsVal)
        //   } yield v
        // )
        // } yield v

        for {
          cnd <- eval(cndVal)
          s <- getState
          v <- reflect {
            if (cnd.isConc) {
              if (cnd.int == 1) {
                reify(s)(eval(thnVal))
              } else {
                reify(s)(eval(elsVal))
              }
            } else {
              reify(s) {choice(
                for {
                  _ <- updatePC(cnd.toSMTBool)
                  v <- eval(thnVal)
                } yield v,
                for {
                  _ <- updatePC(not(cnd.toSMTBool))
                  v <- eval(elsVal)
                } yield v
              )}
            }
          }
        } yield v
    }
  }

  // Note: Comp[E, Rep[Value]] vs Comp[E, Rep[Option[Value]]]?
  def execTerm(funName: String, inst: Terminator): Comp[E, Rep[Value]] = {
    inst match {
      // FIXME: unreachable
      case Unreachable => ret(IntV(-1))
      case RetTerm(ty, Some(value)) => eval(value)(funName)
      case RetTerm(ty, None) => ret(IntV(0))
      case BrTerm(lab) =>
        execBlock(funName, lab)
      case CondBrTerm(ty, cnd, thnLab, elsLab) =>
        for {
          ss <- getState
          cndVal <- eval(cnd)(funName)
          u <- reflect {
            if (cndVal.isConc) {
              if (cndVal.int == 1) reify(ss)(execBlock(funName, thnLab))
              else reify(ss)(execBlock(funName, elsLab))
            } else {
              reify(ss) {choice(
                for {
                  _ <- updatePC(cndVal.toSMTBool)
                  v <- execBlock(funName, thnLab)
                } yield v,
                for {
                  _ <- updatePC(not(cndVal.toSMTBool))
                  v <- execBlock(funName, elsLab)
                } yield v)
              }
            }
          }
        } yield u
        
        // Concrete
        // for {
        //   cndVal <- eval(cnd)(funName)
        //   s <- getState
        //   v <- {
        //     reflect(if (cndVal.int == 1) {
        //       reify(s)(execBlock(funName, thnLab))
        //     } else {
        //       reify(s)(execBlock(funName, elsLab))
        //     })
        //   }
        // } yield v

        // Symbolic
        // val brLabel: String = funName + thnLab + elsLab      
        // for {
        //   cndVal <- eval(cnd)(funName)
        //   v <- choice(
        //     for {
        //       _ <- updatePC(cndVal.toSMTBool)
        //       v <- execBlock(funName, thnLab)
        //     } yield v,
        //     for {
        //       _ <- updatePC(not(cndVal.toSMTBool))
        //       v <- execBlock(funName, elsLab)
        //     } yield v)
        // } yield v
        
      case SwitchTerm(cndTy, cndVal, default, table) =>
        def switchFun(v: Rep[Int], s: Rep[SS], table: List[LLVMCase]): Rep[List[(SS, Value)]] = {
          if (table.isEmpty) execBlock(funName, default, s)
          else {
            if (v == table.head.n) execBlock(funName, table.head.label, s)
            else switchFun(v, s, table.tail)
          }
        }

        def switchFunSym(v: Rep[Value], s: Rep[SS], table: List[LLVMCase], pc: Rep[Set[SMTBool]] = Set()): Rep[List[(SS, Value)]] = {
          if (table.isEmpty) reify(s)(for {
              _ <- updatePCSet(pc)
              u <- execBlock(funName, default)
            } yield u)
          else {
            val headPC = Op2("=", v, IntV(table.head.n)).toSMTBool
            reify(s)(choice(
              for {
                _ <- updatePC(headPC)
                u <- execBlock(funName, table.head.label)
              } yield u,
              reflect(switchFunSym(v, s, table.tail, pc ++ Set(not(headPC))))
            ))
          }
        }

        for {
          v <- eval(cndVal)(funName)
          s <- getState
          r <- reflect(if (v.isConc) {
            switchFun(v.int, s, table)
          } else {
            switchFunSym(v, s, table)
          })
        } yield r
    }
  }

  def execInst(inst: Instruction)(implicit fun: String): Comp[E, Rep[Unit]] = {
    inst match {
      case AssignInst(x, valInst) =>
        for {
          v <- execValueInst(valInst)(fun)
          _ <- stackUpdate(fun + "_" + x, v)
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
          _ <- pushEmptyEnv
          s <- getState
          v <- reflect(fv.fun(s, List(vs:_*)))
          _ <- popFrame(s.stackMem.size)
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

  def precompileHeap: SList[Rep[Value]] = {
    CompileTimeRuntime.globalDefMap.foldRight(SList[Rep[Value]]()) {case ((k, v), h) =>
      val addr = h.size
      CompileTimeRuntime.heapEnv = CompileTimeRuntime.heapEnv + (k -> unit(addr))
      h ++ evalConst(v.const)
    }
  }

  /*
  def precompileHeap(heap: Rep[Heap]): Rep[Heap] = {
    CompileTimeRuntime.globalDefMap.foldRight(heap) {case ((k, v), h) =>
      val (allocH, addr) = h.alloc(getTySize(v.typ))
      CompileTimeRuntime.heapEnv = CompileTimeRuntime.heapEnv + (k -> addr)
      allocH.updateL(addr, List(evalConst(v.const):_*))
    }
  }
   */

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
        //System.err.println("Already compiled " + b)
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
        //System.err.println("Already compiled " + f)
      } else {
        CompileTimeRuntime.FunFuns(f.id) = repRunFun(f)
      }
    }
  }

  def exec(m: Module, fname: String, args: Rep[List[Value]]): Rep[List[(SS, Value)]] = {
    CompileTimeRuntime.funMap = m.funcDefMap
    CompileTimeRuntime.funDeclMap = m.funcDeclMap
    CompileTimeRuntime.globalDefMap = m.globalDefMap

    val preHeap: Rep[List[Value]] = List(precompileHeap:_*) // (emptyMem)
    val heap0 = Wrap[Mem](Unwrap(preHeap))
    val comp = for {
      fv <- eval(GlobalId(fname))(fname)
      _ <- pushEmptyEnv
      s <- getState
      v <- reflect(fv.fun(s, args))
      _ <- popFrame(s.stackMem.size)
    } yield v
    val initState: Rep[SS] = (heap0, emptyMem, List[Env](), Set[SMTBool]())
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

  def quoteOp(op: String): String = {
    op match {
      case "+"  => "op_plus"
      case "-"  => "op_minus"
      case "*"  => "op_mult"
      case "/"  => "op_div"
      case "="  => "op_eq"
      case "!=" => "op_neq"
      case ">=" => "op_ge"
      case ">"  => "op_gt"
      case "<=" => "op_le"
      case "<"  => "op_lt"
    }
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "op_2", List(Backend.Const(op: String), x, y), _) =>
      es"op_2(${quoteOp(op)}, $x, $y)"
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
      else if (m.toString.endsWith("SMTExpr")) "Expr" //FIXME
      else super.remap(m)
    }
  }
}

object TestStagedSymExec {
  @virtualize
  def specialize(m: Module, fname: String): CppSAIDriver[Int, Unit] =
    new CppSymStagedLLVMDriver[Int, Unit] {
      def snippet(u: Rep[Int]) = {
        val args: Rep[List[Value]] = List[Value](
          SymV("x0"), SymV("x1"), SymV("x2"), 
          SymV("x3"), SymV("x4"), SymV("x5"),
          SymV("x6"), SymV("x7"), SymV("x8"),
          SymV("x9"),
          SymV("x10"), SymV("x11"),
          SymV("x12"), SymV("x13"), SymV("x14"),
          SymV("x15"),
          SymV("x16"), SymV("x17"),
          SymV("x18"), SymV("x19")
        )
        val res = exec(m, fname, args)
        // query a single test
        res.head._1.pc.toList.foreach(assert(_))
        handle(query(lit(false)))

        println(res.size)
      }
    }

  def testM(m: Module, output: String, fname: String) {
    val res = sai.utils.Utils.time {
      val code = specialize(m, fname)
      code.save(s"gen/$output")
    }
    println(res)
    //code.eval(0)
  }

  def testArrayAccess = testM(Benchmarks.arrayAccess, "array_access.cpp", "@main")
  def testPower = testM(Benchmarks.power, "power.cpp", "@main")

  def main(args: Array[String]): Unit = {
    testM(OOPSLA20Benchmarks.maze, "maze_sym.cpp", "@main")
    testM(OOPSLA20Benchmarks.mp1024, "mp1024_sym.cpp", "@f")
    testM(OOPSLA20Benchmarks.mp65536, "mp65536_sym.cpp", "@f")
    testM(OOPSLA20Benchmarks.mp1048576, "mp1048576_sym.cpp", "@f")
  }
}
