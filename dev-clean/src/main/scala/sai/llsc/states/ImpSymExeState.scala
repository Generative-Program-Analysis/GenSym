package sai.llsc.imp

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.llsc.{Constants, BasicDefs, Coverage, Opaques, ValueDefs}

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
trait ImpSymExeDefs extends SAIOps with BasicDefs with ValueDefs with Opaques with Coverage {
  import Constants._

  type Cont = PCont[Ref]

  implicit def toRef(s: Rep[SS]): Rep[Ref[SS]] = s.asRepOf[Ref[SS]]
  implicit def fromRef(s: Rep[Ref[SS]]): Rep[SS] = s.asRepOf[SS]
  implicit def lift(v: Rep[Value])(implicit s: Rep[SS]): Rep[List[(SS, Value)]] = List[(SS, Value)]((s, v))

  /* 0 -- all control dependency
   * 1 -- read/write hard dependency
   * 2 -- hard + soft dependency
   */
  var depLevel: Int = 2

  // Auxiliary effectful reflect functions for the experiment
  def reflectCtrl[T: Manifest](op: String, rs: Rep[_]*): Rep[T] =
    Wrap[T](Adapter.g.reflectEffect(op, rs.map(Unwrap):_*)(Adapter.CTRL)(Adapter.CTRL))
  def reflectRW[T: Manifest](op: String, rs: Rep[_]*)(rk: Rep[_]*)(wk: Rep[_]*): Rep[T] =
    Wrap[T](Adapter.g.reflectEffect(op, rs.map(Unwrap):_*)(rk.map(Unwrap):_*)(wk.map(Unwrap):_*))

  def reflectRead[T: Manifest](op: String, rs: Rep[_]*)(es: Rep[_]*): Rep[T] =
    if (depLevel == 0) reflectCtrl[T](op, rs:_*)
    else if (depLevel == 1) reflectRW[T](op, rs:_*)(es:_*)(es:_*)
    else if (depLevel == 2) Wrap[T](Adapter.g.reflectRead(op, rs.map(Unwrap):_*)(es.map(Unwrap):_*))
    else ???
  def reflectWrite[T: Manifest](op: String, rs: Rep[_]*)(es: Rep[_]*): Rep[T] =
    if (depLevel == 0) reflectCtrl[T](op, rs:_*)
    else if (depLevel == 1) reflectRW[T](op, rs:_*)(es:_*)(es:_*)
    else if (depLevel == 2) Wrap[T](Adapter.g.reflectWrite(op, rs.map(Unwrap):_*)(es.map(Unwrap):_*))
    else ???

  def currentMethodName: String = Thread.currentThread.getStackTrace()(2).getMethodName

  class SSOps(ss: Rep[SS]) {
    // Caveat: in the presence of higher-order function (e.g. flatMap, fold),
    // since we currently lack of aliases information, simply using reflectWrite
    // might not be enough to preserve some operation, rendering undesired DCE.
    // `assign` and `update` are currently changed to reflectCtrl to generate correct code
    // for ImpEngine.

    private def assignSeq(xs: List[Int], vs: Rep[List[Value]]): Rep[Unit] =
      reflectWrite[Unit]("ss-assign-seq", ss, xs, vs)(ss)

    def lookup(x: String): Rep[Value] = {
      //System.out.println("Debug info: " + x + "->" + x.hashCode)
      reflectRead[Value]("ss-lookup-env", ss, x.hashCode)(ss)
    }
    def assign(x: String, v: Rep[Value]): Rep[Unit] =
      reflectCtrl[Unit]("ss-assign", ss, x.hashCode, v)
      //reflectWrite[Unit]("ss-assign", ss, x.hashCode, v)(ss)
    def assign(xs: List[String], vs: Rep[List[Value]]): Rep[Unit] = assignSeq(xs.map(_.hashCode), vs)
    def lookup(addr: Rep[Value], size: Int = 1, isStruct: Int = 0): Rep[Value] = {
      require(size > 0)
      if (isStruct == 0) reflectRead[Value]("ss-lookup-addr", ss, addr)(ss)
      else reflectRead[Value]("ss-lookup-addr-struct", ss, addr, size)(ss)
    }
    def update(a: Rep[Value], v: Rep[Value]): Rep[Unit] =
      reflectCtrl[Unit]("ss-update", ss, a, v)
      //reflectWrite[Unit]("ss-update", ss, a, v)(ss)
    def allocStack(n: Rep[Int]): Rep[Unit] = reflectWrite[Unit]("ss-alloc-stack", ss, n)(ss)

    def heapLookup(addr: Rep[Addr]): Rep[Value] = reflectRead[Value]("ss-lookup-heap", ss, addr)(ss)
    def heapSize: Rep[Int] = reflectRead[Int]("ss-heap-size", ss)(ss)
    def heapAppend(vs: Rep[List[Value]]): Rep[Unit] = reflectWrite[Unit]("ss-heap-append", ss, vs)(ss)

    def stackSize: Rep[Int] = reflectRead[Int]("ss-stack-size", ss)(ss)
    def freshStackAddr: Rep[Addr] = stackSize

    def push: Rep[Unit] = reflectWrite[Unit]("ss-push", ss)(ss)
    // XXX: since pop is used in a map, will be DCE-ed if no CTRL
    def pop(keep: Rep[Int]): Rep[Unit] = reflectWrite[Unit]("ss-pop", ss, keep)(ss, Adapter.CTRL)
    def addPC(e: Rep[SMTBool]): Rep[Unit] = reflectWrite[Unit]("ss-addpc", ss, e)(ss)
    def addPCSet(es: Rep[Set[SMTBool]]): Rep[Unit] = reflectWrite[Unit]("ss-addpcset", ss, es)(ss)
    def pc: Rep[PC] = "get-pc".reflectWith[PC](ss)
    def updateArg(l: Rep[Int]): Rep[Unit] = reflectWrite[Unit]("ss-arg", ss, l)(ss)

    def addIncomingBlock(x: String): Rep[Unit] = reflectWrite[Unit]("ss-add-incoming-block", ss, x.hashCode)(ss)
    def incomingBlock: Rep[BlockLabel] = reflectRead[BlockLabel]("ss-incoming-block", ss)(ss)

    def copy: Rep[SS] = reflectRead[SS]("ss-copy", ss)(ss)
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
  }
  implicit class RefSSOps(ss: Rep[Ref[SS]]) extends SSOpsOpt(ss.asRepOf[SS])
}
