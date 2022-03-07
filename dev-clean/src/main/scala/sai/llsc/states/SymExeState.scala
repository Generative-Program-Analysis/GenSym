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

import scala.collection.immutable.{List => StaticList, Map => StaticMap, Set => StaticSet}
import scala.collection.mutable.{Map => MutableMap, Set => MutableSet}

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
      if (isStruct == 0) "ss-lookup-addr".reflectWith[Value](ss, addr, size)
      else "ss-lookup-addr-struct".reflectWith[Value](ss, addr, size)
    }
    def lookupSeq(addr: Rep[Value], count: Rep[Int]): Rep[List[Value]] = "ss-lookup-addr-seq".reflectWith[List[Value]](ss, addr, count)

    def arrayLookup(base: Rep[Value], offset: Rep[Value], eSize: Int, nSize: Int, k: Rep[Cont]): Rep[Unit] =
      "ss-array-lookup".reflectWith[Unit](ss, base, offset, eSize, nSize, k)
    def arrayLookup(base: Rep[Value], offset: Rep[Value], eSize: Int, nSize: Int): Rep[List[(SS, Value)]] =
      "ss-array-lookup".reflectWith[List[(SS, Value)]](ss, base, offset, eSize, nSize)

    def update(a: Rep[Value], v: Rep[Value], sz: Int): Rep[SS] = "ss-update".reflectWith[SS](ss, a, v, sz)
    def updateSeq(a: Rep[Value], v: Rep[List[Value]]): Rep[SS] = "ss-update-seq".reflectWith[SS](ss, a, v)
    def allocStack(n: Int, align: Int): Rep[SS] = "ss-alloc-stack".reflectWith[SS](ss, n)

    def heapLookup(addr: Rep[Addr]): Rep[Value] = "ss-lookup-heap".reflectWith[Value](ss, addr)
    def heapSize: Rep[Int] = "ss-heap-size".reflectWith[Int](ss)
    def heapAppend(vs: Rep[List[Value]]) = "ss-heap-append".reflectWith[SS](ss, vs)

    def stackSize: Rep[Int] = "ss-stack-size".reflectWith[Int](ss)
    def freshStackAddr: Rep[Addr] = stackSize

    def push: Rep[SS] = "ss-push".reflectWith[SS](ss)
    def pop(keep: Rep[Int]): Rep[SS] = "ss-pop".reflectWith[SS](ss, keep)
    def addPC(e: Rep[SMTBool]): Rep[SS] = "ss-addpc".reflectWith[SS](ss, e)
    def addPCSet(es: Rep[List[SMTBool]]): Rep[SS] = "ss-addpcset".reflectWith[SS](ss, es)
    def pc: Rep[PC] = "get-pc".reflectWith[PC](ss)
    def updateArg: Rep[SS] = "ss-arg".reflectWith[SS](ss)

    def addIncomingBlock(x: String): Rep[SS] = "ss-add-incoming-block".reflectWith[SS](ss, x.hashCode)
    def incomingBlock: Rep[BlockLabel] = "ss-incoming-block".reflectWith[BlockLabel](ss)

    def getFs: Rep[FS] = "ss-get-fs".reflectCtrlWith[FS](ss)
    def setFs(fs: Rep[FS]): Unit = "ss-set-fs".reflectCtrlWith[FS](ss, fs)
  }

  implicit class SSOpsOpt(ss: Rep[SS]) extends SSOps(ss) {
    private def lookupOpt(x: Int, s: Backend.Def, default: => Rep[Value], bound: Int): Rep[Value] =
      if (bound == 0) default
      else s match {
        case gNode("ss-assign", StaticList(ss0, bConst(y), v: bExp)) if y == x => Wrap[Value](v)
        case gNode("ss-assign-seq", StaticList(ss0, bConst(vars: StaticList[Int]), vals: bExp)) =>
          val idx = vars.indexOf(x)
          if (idx != -1) (Wrap[List[Value]](vals): Rep[List[Value]])(idx)
          else lookupOpt(x, ss0, default, bound-1)
        case gNode("ss-assign", StaticList(ss0, _, _)) => lookupOpt(x, ss0, default, bound-1)
        case gNode("ss-alloc-stack", StaticList(ss0, _)) => lookupOpt(x, ss0, default, bound-1)
        case gNode("ss-update", StaticList(ss0, _, _, _)) => lookupOpt(x, ss0, default, bound-1)
        case gNode("ss-add-incoming-block", StaticList(ss0, _)) => lookupOpt(x, ss0, default, bound-1)
        // TODO: update-seq?
        case _ => default
      }

    override def lookup(x: String): Rep[Value] = lookupOpt(x.hashCode, Unwrap(ss), super.lookup(x), 30)

    override def assign(x: String, v: Rep[Value]): Rep[SS] = Unwrap(ss) match {
      // Idea: coalesce multiple subsequent assign into assign-seq
      // Idea: dead assignment can be eliminated -- dead: no lookup of it of the whole program
      case _ => super.assign(x, v)
    }

    override def stackSize: Rep[Int] = Unwrap(ss) match {
      case gNode("ss-alloc-stack", StaticList(ss0: bExp, bConst(inc: Int))) => Wrap[SS](ss0).stackSize + inc
      case gNode("ss-assign", StaticList(ss0: bExp, _, _)) => Wrap[SS](ss0).stackSize
      case _ => super.stackSize
    }
  }

  implicit class FSOps(fs: Rep[FS]) {
    def openFile(path: Rep[String], flag: Rep[Int]): Rep[Fd] =
      "fs-open-file".reflectCtrlWith[Fd](fs, path, flag)
    def closeFile(fd: Rep[Fd]): Rep[Int] =
      "fs-close-file".reflectCtrlWith[Int](fs, fd)
    def readFile(fd: Rep[Fd], nbytes: Rep[Int]): Rep[(List[Value], Int)] =
      "fs-read-file".reflectCtrlWith[(List[Value], Int)](fs, fd, nbytes)
    def writeFile(fd: Rep[Fd], content: Rep[List[Value]], nbytes: Rep[Int]): Rep[Int] =
      "fs-write-file".reflectCtrlWith[Int](fs, fd, content, nbytes)
    def statFile(ptr: Rep[String]): Rep[(List[Value], Int)] =
      "fs-stat-file".reflectCtrlWith[(List[Value], Int)](fs, ptr)
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
  def updateMem(k: Rep[Value], v: Rep[Value], sz: Int): Comp[E, Rep[Unit]] = updateState(_.update(k, v, sz))
  def updatePCSet(x: Rep[List[SMTBool]]): Comp[E, Rep[Unit]] = updateState(_.addPCSet(x))
  def updatePC(x: Rep[SMTBool]): Comp[E, Rep[Unit]] = updateState(_.addPC(x))
  def updateIncomingBlock(x: String): Comp[E, Rep[Unit]] = updateState(_.addIncomingBlock(x))
  def initializeArg: Comp[E, Rep[Unit]] = updateState(_.updateArg)
}
