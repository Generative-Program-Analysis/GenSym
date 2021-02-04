package sai.llsc

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

@virtualize
trait SymExeState extends SAIOps with StagedNondet {
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

  object Magic {
    def reify[T: Manifest](s: Rep[SS])(comp: Comp[E, Rep[T]]): Rep[List[(SS, T)]] = {
      val p1: Comp[Nondet ⊗ ∅, (Rep[SS], Rep[T])] =
        State.runState[Nondet ⊗ ∅, Rep[SS], Rep[T]](s)(comp)
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
  }

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

  def stackAlloc(size: Int): Comp[E, Rep[Addr]] =
    for {
      st <- getStackMem
      a <- {
        val (st_, a) = st.alloc(size)
        putStackMem(st_).map { _ => a }
      }
    } yield a

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

  def stackUpdate(xs: List[String], vs: Rep[List[Value]]): Comp[E, Rep[Unit]] =
    if (xs.isEmpty) ret(())
    else
      for {
        _ <- stackUpdate(xs.head, vs.head)
        _ <- stackUpdate(xs.tail, vs.tail)
      } yield ()

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

    def bv_sext(bw: Rep[Int]): Rep[Value] = 
      Wrap[Value](Adapter.g.reflect("bv_sext", Unwrap(v), Unwrap(bw)))
  }
}
