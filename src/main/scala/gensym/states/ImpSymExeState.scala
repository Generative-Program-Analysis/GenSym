package gensym.imp

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import gensym.lmsx._
import gensym.llvm._
import gensym.llvm.IR._
import gensym.{Constants, BasicDefs, Coverage, Opaques, ValueDefs, Counter, Ctx, Config, Global}

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

class Mut[T](var x: T) {
  override def toString: String = x.toString
}

@virtualize
trait ImpSymExeDefs extends SAIOps with BasicDefs with ValueDefs with Opaques with Coverage {
  import Constants._

  type Cont = PCont[Ref]

  override def usingPureEngine: Boolean = false

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

  implicit class PCOps(pc: Rep[PC]) {
    def addPC(e: Rep[Value]): Rep[Unit] = reflectWrite[Unit]("add-pc", pc, e)(pc)
  }

  implicit class SSOps(ss: Rep[SS]) {
    // Caveat: in the presence of higher-order function (e.g. flatMap, fold),
    // since we currently lack of aliases information, simply using reflectWrite
    // might not be enough to preserve some operation, rendering undesired DCE.
    // `assign` and `update` are currently changed to reflectCtrl to generate correct code
    // for ImpEngine.

    private def assignSeq(xs: List[Int], vs: Rep[List[Value]]): Rep[Unit] =
      reflectWrite[Unit]("ss-assign-seq", ss, xs, vs)(ss)

    def lookup(x: String)(implicit ctx: Ctx): Rep[Value] =
      reflectRead[Value]("ss-lookup-env", ss, varId(x))(ss)
    def assign(x: String, v: Rep[Value])(implicit ctx: Ctx): Rep[Unit] =
      reflectWrite[Unit]("ss-assign", ss, varId(x), v)(ss)
    def assign(xs: List[String], vs: Rep[List[Value]])(implicit ctx: Ctx): Rep[Unit] =
      assignSeq(xs.map(varId(_)), vs)
    def lookup(addr: Rep[Value], size: Int = 1, isStruct: Int = 0): Rep[Value] = {
      require(size > 0)
      if (isStruct == 0) reflectRead[Value]("ss-lookup-addr", ss, addr, size)(ss)
      else reflectRead[Value]("ss-lookup-addr-struct", ss, addr, size)(ss)
    }

    //def arrayLookup(base: Rep[Value], offset: Rep[Value], eSize: Int, k: Rep[Cont]): Rep[Unit] =
    //  "ss-array-lookup".reflectWith[Unit](ss, base, offset, eSize, k)
    //def arrayLookup(base: Rep[Value], offset: Rep[Value], eSize: Int): Rep[List[(SS, Value)]] =
    //  "ss-array-lookup".reflectWith[List[(SS, Value)]](ss, base, offset, eSize)

    def update(a: Rep[Value], v: Rep[Value], sz: Int): Rep[Unit] = reflectCtrl[Unit]("ss-update", ss, a, v, sz)
    @deprecated("Use update with size", "now and forever")
    def update(a: Rep[Value], v: Rep[Value]): Rep[Unit] = reflectCtrl[Unit]("ss-update", ss, a, v)
    def allocStack(n: Int, align: Int): Rep[Unit] =
      reflectWrite[Unit]("ss-alloc-stack", ss, new Mut[Int](n))(ss)

    def heapLookup(addr: Rep[Addr]): Rep[Value] = reflectRead[Value]("ss-lookup-heap", ss, addr)(ss)
    def heapSize: Rep[Int] = reflectRead[Int]("ss-heap-size", ss)(ss)
    def heapAppend(vs: Rep[List[Value]]): Rep[Unit] = reflectWrite[Unit]("ss-heap-append", ss, vs)(ss)

    def stackSize: Rep[Int] = reflectRead[Int]("ss-stack-size", ss)(ss)
    def freshStackAddr: Rep[Addr] = stackSize

    // push before function call could be DCE-ed, due to high-level function dependency issue.
    def push: Rep[Unit] = reflectWrite[Unit]("ss-push", ss)(ss, Adapter.CTRL)
    def push(cont: Rep[Cont]): Rep[Unit] = reflectWrite[Unit]("ss-push", ss, cont)(ss, Adapter.CTRL)
    // XXX: since pop is used in a map, will be DCE-ed if no CTRL
    def pop(keep: Rep[Int]): Rep[Cont] = reflectWrite[Cont]("ss-pop", ss, keep)(ss, Adapter.CTRL)
    def addPC(e: Rep[Value]): Rep[Unit] = reflectWrite[Unit]("ss-addpc", ss, e)(ss)
    def addPCSet(es: Rep[List[Value]]): Rep[Unit] = reflectWrite[Unit]("ss-addpcset", ss, es)(ss)
    def pc: Rep[PC] = reflectRead[PC]("get-pc", ss)(ss)
    def copyPC: Rep[PC] = reflectRead[PC]("ss-copy-pc", ss)(ss)
    def updateArg: Rep[Unit] = reflectWrite[Unit]("ss-arg", ss)(ss)
    def initErrorLoc: Rep[Unit] = reflectWrite[Unit]("ss-init-error-loc", ss)(ss)
    def getErrorLoc: Rep[Value] = reflectRead[Value]("ss-get-error-loc", ss)(ss)
    def setErrorLoc(v: Rep[IntV]): Rep[Unit] = ss.update(ss.getErrorLoc, v, 4)

    def addIncomingBlock(ctx: Ctx): Rep[Unit] =
      reflectWrite[Unit]("ss-add-incoming-block", ss, Counter.block.get(ctx.toString))(ss)
    def incomingBlock: Rep[BlockLabel] = reflectRead[BlockLabel]("ss-incoming-block", ss)(ss)
    def coverBlock(ctx: Ctx): Rep[Unit] =
      reflectWrite[Unit]("ss-cover-block", ss, Counter.block.get(ctx.toString))(ss, Adapter.CTRL)

    def copy: Rep[SS] = reflectRead[SS]("ss-copy", ss)(ss)
    def fork: Rep[SS] = reflectRW[SS]("ss-fork", ss)(ss, Adapter.CTRL)(Adapter.CTRL)

    // Note: getIntArg/getFloatArg/getPointerArg may potentially call solver
    def getIntArg(x : Rep[Value]): Rep[Long] = reflectRead[Long]("ss-get-int-arg", ss, x)(ss)
    def getFloatArg(x : Rep[Value]): Rep[Double] = reflectRead[Double]("ss-get-float-arg", ss, x)(ss)
    def getPointerArg(x : Rep[Value]): Rep[Ptr[Char]] = reflectRead[Ptr[Char]]("ss-get-pointer-arg", ss, x)(ss)
    def writebackPointerArg(res: Rep[Any], addr:Rep[Value], x: Rep[Ptr[Char]]): Rep[Unit] =
      reflectWrite[Unit]("ss-writeback-pointer-arg", ss, res, addr, x)(ss)
  }

  implicit class RefSSOps(ss: Rep[Ref[SS]]) extends SSOps(ss.asRepOf[SS])

}
