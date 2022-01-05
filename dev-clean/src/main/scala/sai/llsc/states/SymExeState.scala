package sai.llsc

import sai.lang.llvm._
import sai.lang.llvm.IR._

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
import sai.lmsx.smt.SMTBool

import scala.collection.immutable.{List => StaticList, Map => StaticMap}
import scala.collection.mutable.{Map => MultableMap}
import scala.collection.immutable.{Set => StaticSet}
import scala.collection.mutable.{Set => MultableSet}

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
trait SymExeDefs extends SAIOps with StagedNondet with BasicDefs with ValueDefs with Opaques with Coverage {
  type E = State[Rep[SS], *] ⊗ (Nondet ⊗ ∅)
  type Cont = PCont[Id]

  trait Future[T]

  object ThreadPool {
    def enqueue[T: Manifest](f: Rep[Unit] => Rep[T]): Rep[Future[T]] = {
      val block = Adapter.g.reify(x => Unwrap(f(Wrap[Unit](x))))
      Wrap[Future[T]](Adapter.g.reflectWrite("tp-enqueue", block)(Adapter.CTRL))
    }
    def async[T: Manifest](f: Rep[Unit] => Rep[T]): Rep[Future[T]] = {
      val block = Adapter.g.reify(x => Unwrap(f(Wrap[Unit](x))))
      Wrap[Future[T]](Adapter.g.reflectWrite("tp-async", block)(Adapter.CTRL))
    }
    def get[T: Manifest](f: Rep[Future[T]]): Rep[T] =
      "tp-future-get".reflectWriteWith[T](f)(Adapter.CTRL)

    def canPar: Rep[Boolean] = "can-par".reflectWriteWith[Boolean]()(Adapter.CTRL)
  }

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

  class SSOps(ss: Rep[SS]) {
    private def assignSeq(xs: List[Int], vs: Rep[List[Value]]): Rep[SS] =
      "ss-assign-seq".reflectWith[SS](ss, xs, vs)

    def lookup(x: String): Rep[Value] = "ss-lookup-env".reflectWith[Value](ss, x.hashCode)
    def assign(x: String, v: Rep[Value]): Rep[SS] = "ss-assign".reflectWith[SS](ss, x.hashCode, v)
    def assign(xs: List[String], vs: Rep[List[Value]]): Rep[SS] = assignSeq(xs.map(_.hashCode), vs)
    def lookup(addr: Rep[Value], size: Int = 1, isStruct: Int = 0): Rep[Value] = {
      require(size > 0)
      if (isStruct == 0) "ss-lookup-addr".reflectWith[Value](ss, addr)
      else "ss-lookup-addr-struct".reflectWith[Value](ss, addr, size)
    }
    def update(a: Rep[Value], v: Rep[Value]): Rep[SS] = "ss-update".reflectWith[SS](ss, a, v)
    def allocStack(n: Rep[Int]): Rep[SS] = "ss-alloc-stack".reflectWith[SS](ss, n)

    def heapLookup(addr: Rep[Addr]): Rep[Value] = "ss-lookup-heap".reflectWith[Value](ss, addr)
    def heapSize: Rep[Int] = "ss-heap-size".reflectWith[Int](ss)
    def heapAppend(vs: Rep[List[Value]]) = "ss-heap-append".reflectWith[SS](ss, vs)

    def stackSize: Rep[Int] = "ss-stack-size".reflectWith[Int](ss)
    def freshStackAddr: Rep[Addr] = stackSize

    def push: Rep[SS] = "ss-push".reflectWith[SS](ss)
    def pop(keep: Rep[Int]): Rep[SS] = "ss-pop".reflectWith[SS](ss, keep)
    def addPC(e: Rep[SMTBool]): Rep[SS] = "ss-addpc".reflectWith[SS](ss, e)
    def addPCSet(es: Rep[Set[SMTBool]]): Rep[SS] = "ss-addpcset".reflectWith[SS](ss, es)
    def pc: Rep[PC] = "get-pc".reflectWith[PC](ss)
    def updateArg(l: Rep[Int]): Rep[SS] = "ss-arg".reflectWith[SS](ss, l)

    def addIncomingBlock(x: String): Rep[SS] = "ss-add-incoming-block".reflectWith[SS](ss, x.hashCode)
    def incomingBlock: Rep[BlockLabel] = "ss-incoming-block".reflectWith[BlockLabel](ss)
  }

  implicit class SSOpsOpt(ss: Rep[SS]) extends SSOps(ss) {
    private def lookupOpt(x: Int, s: Backend.Def, default: => Rep[Value], bound: Int): Rep[Value] =
      if (bound == 0) default
      else s match {
        case Adapter.g.Def("ss-assign", ss0::Backend.Const(y)::(v: Backend.Sym)::Nil) if y == x => Wrap[Value](v)
        case Adapter.g.Def("ss-assign", ss0::Backend.Const(y)::(v: Backend.Sym)::Nil) => lookupOpt(x, ss0, default, bound-1)
        // TODO: ss-assign-seq?
        case _ => default
      }

    override def lookup(x: String): Rep[Value] = lookupOpt(x.hashCode, Unwrap(ss), super.lookup(x), 5)
    override def assign(x: String, v: Rep[Value]): Rep[SS] = Unwrap(ss) match {
      /*
      case Adapter.g.Def("ss-assign", ss0::Backend.Const(y: Int)::(w: Backend.Exp)::Nil) =>
        val hs: Rep[List[Int]] = List(y, x.hashCode)
        val vs: Rep[List[Value]] = List(Wrap[Value](w), v)
        // s.lookup(x) if x != s0.assign(x, v)
        Wrap[SS](Adapter.g.reflect("ss-assign-seq", ss0, Unwrap(hs), Unwrap(vs)))
       */
      case _ => super.assign(x, v)
    }
  }

  def putState(s: Rep[SS]): Comp[E, Rep[Unit]] = for { _ <- put[Rep[SS], E](s) } yield ()
  def getState: Comp[E, Rep[SS]] = get[Rep[SS], E]

  def updateState(f: Rep[SS] => Rep[SS]): Comp[E, Rep[Unit]] =
    for {
      ss <- getState
      _ <- putState(f(ss))
    } yield ()

  def heapAppend(vs: Rep[List[Value]]): Comp[E, Rep[Unit]]  = updateState(_.heapAppend(vs))
  def stackUpdate(xs: List[String], vs: Rep[List[Value]]): Comp[E, Rep[Unit]] = updateState(_.assign(xs, vs))
  def stackUpdate(x: String, v: Rep[Value]): Comp[E, Rep[Unit]] = updateState(_.assign(x, v))
  def pushFrame: Comp[E, Rep[Unit]] = updateState(_.push)
  def popFrame(keep: Rep[Int]): Comp[E, Rep[Unit]] = updateState(_.pop(keep))
  def updateMem(k: Rep[Value], v: Rep[Value]): Comp[E, Rep[Unit]] = updateState(_.update(k, v))
  def updatePCSet(x: Rep[Set[SMTBool]]): Comp[E, Rep[Unit]] = updateState(_.addPCSet(x))
  def updatePC(x: Rep[SMTBool]): Comp[E, Rep[Unit]] = updateState(_.addPC(x))
  def updateIncomingBlock(x: String): Comp[E, Rep[Unit]] = updateState(_.addIncomingBlock(x))
  def initializeArg(x: Rep[Int]): Comp[E, Rep[Unit]] = updateState(_.updateArg(x))
}
