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

/* Naming convention for IR nodes:
   - If the node can be and should be handled by the default case of the codegen,
     i.e. directly emitting the IR name and arguments sequentially,
     we shall use underscore `_` as the delimiter in the IR name, e.g. `proj_LocV`.
   - If the node requires additional care in the codegen,
     we should use dash `-` as the delimiter in the IR name, e.g. `ss-lookup-stack`.
     The reason is that in common codegen targets (such as C/C++/Scala), `-` is
     not a valid use of identifiers.
 */

@virtualize
trait SymExeDefs extends SAIOps with StagedNondet {
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

  trait Mem
  trait Value
  trait Stack
  trait SS

  type Addr = Int
  type Heap = Mem
  type PC = Set[SMTBool]
  type E = State[Rep[SS], *] ⊗ (Nondet ⊗ ∅)

  object SS {
    def init: Rep[SS] = "init-ss".reflectWriteWith()(Adapter.CTRL)
  }

  implicit class SSOps(ss: Rep[SS]) {
    def lookup(x: String): Rep[Value] = "ss-lookup-stack".reflectWith[Value](ss, x.hashCode)
    def lookup(addr: Rep[Value]): Rep[Value] = "ss-lookup-addr".reflectWith[Value](ss, addr)
    def heapLookup(addr: Rep[Addr]): Rep[Value] = "ss-lookup-heap".reflectWith[Value](ss, addr)
    def assign(x: String, v: Rep[Value]): Rep[SS] = "ss-assign".reflectWith[SS](ss, x.hashCode, v)
    def assign(xs: List[String], vs: Rep[List[Value]]): Rep[SS] =
      "ss-assign-seq".reflectWith[SS](ss, xs.map(_.hashCode), vs)
    def stackSize: Rep[Int] = "ss-stack-size".reflectWith[Int](ss)
    def freshStackAddr: Rep[Addr] = stackSize
    def allocStack(n: Rep[Int]): Rep[SS] = "ss-alloc-stack".reflectWith[SS](ss, n)
    def update(a: Rep[Value], v: Rep[Value]): Rep[SS] = "ss-update".reflectWith[SS](ss, a, v)
    def push: Rep[SS] = "ss-push".reflectWith[SS](ss)
    def pop(keep: Rep[Int]): Rep[SS] = "ss-pop".reflectWith[SS](ss, keep)
    def addPC(e: Rep[SMTBool]): Rep[SS] = "ss-addpc".reflectWith[SS](ss, e)
    def addPCSet(es: Rep[Set[SMTBool]]): Rep[SS] = "ss-addpcset".reflectWith[SS](ss, es)
  }

  def putState(s: Rep[SS]): Comp[E, Rep[Unit]] = for { _ <- put[Rep[SS], E](s) } yield ()
  def getState: Comp[E, Rep[SS]] = get[Rep[SS], E]

  def stackUpdate(xs: List[String], vs: Rep[List[Value]]): Comp[E, Rep[Unit]] =
    for {
      ss <- getState
      _ <- putState(ss.assign(xs, vs))
    } yield ()
  def stackUpdate(x: String, v: Rep[Value]): Comp[E, Rep[Unit]] =
    for {
      ss <- getState
      _ <- putState(ss.assign(x, v))
    } yield ()

  def stackPush: Comp[E, Rep[Unit]] =
    for {
      ss <- getState
      _ <- putState(ss.push)
    } yield ()

  def stackPop(keep: Rep[Int]): Comp[E, Rep[Unit]] =
    for {
      ss <- getState
      _ <- putState(ss.pop(keep))
    } yield ()

  def updateMem(k: Rep[Value], v: Rep[Value]): Comp[E, Rep[Unit]] =
    for {
      ss <- getState
      _ <- putState(ss.update(k, v))
    } yield ()

  def updatePCSet(x: Rep[Set[SMTBool]]): Comp[E, Rep[Unit]] =
    for {
      s <- getState
      _ <- putState(s.addPCSet(x))
    } yield ()

  def updatePC(x: Rep[SMTBool]): Comp[E, Rep[Unit]] =
    for {
      s <- getState
      _ <- putState(s.addPC(x))
    } yield ()

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
    def apply(f: Rep[(SS, List[Value]) => List[(SS, Value)]]): Rep[Value] = f.asRepOf[Value]
  }
  object SymV {
    def apply(s: Rep[String]): Rep[Value] =
      Wrap[Value](Adapter.g.reflectWrite("make_SymV", Unwrap(s))(Adapter.CTRL))
    def apply(s: Rep[String], bw: Int): Rep[Value] =
      Wrap[Value](Adapter.g.reflectWrite("make_SymV", Unwrap(s), Backend.Const(bw))(Adapter.CTRL))
  }

  object Op2 {
    def apply(op: String, o1: Rep[Value], o2: Rep[Value]) = "op_2".reflectWith[Value](op, o1, o2)
  }

  implicit class ValueOps(v: Rep[Value]) {
    def loc: Rep[Addr] = "proj_LocV".reflectWith[Addr](v)
    def int: Rep[Int] = "proj_IntV".reflectWith[Int](v)
    def fun: Rep[(SS, List[Value]) => List[(SS, Value)]] =
      v.asRepOf[(SS, List[Value]) => List[(SS, Value)]]
    def kind: Rep[Int] = "proj_LocV_kind".reflectWith[Int](v)

    def isConc: Rep[Boolean] = "is-conc".reflectWith[Boolean](v)
    def toSMTBool: Rep[SMTBool] = "to-SMTBool".reflectWith[SMTBool](v)

    // TODO: toSMTBool vs toSMTExpr?
    //def toSMTExpr: Rep[SMTExpr] =
    //  Wrap[SMTExpr](Adapter.g.reflect("proj_SMTExpr", Unwrap(v)))

    def bv_sext(bw: Rep[Int]): Rep[Value] = 
      Wrap[Value](Adapter.g.reflect("bv_sext", Unwrap(v), Unwrap(bw)))
  }
}
