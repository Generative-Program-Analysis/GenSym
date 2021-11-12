package sai.llsc.imp

import sai.lang.llvm._
import sai.lang.llvm.IR._

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
trait ImpSymExeDefs extends SAIOps {
  type Cont = ((Ref[SS], Value) => Unit)

  object Coverage {
    import scala.collection.mutable.HashMap
    private var counter: Int = 0
    private val blockMap: HashMap[String, Int] = HashMap[String, Int]()
    def getBlockId(s: String): Int =
      if (blockMap.contains(s)) blockMap(s)
      else {
        val id = counter
        blockMap(s) = id
        counter += 1
        return id
      }

    def setBlockNum: Rep[Unit] = "cov-set-blocknum".reflectWriteWith[Unit](counter)(Adapter.CTRL)
    def incBlock(funName: String, label: String): Rep[Unit] = {
      val blockId = getBlockId(funName + label)
      "cov-inc-block".reflectWriteWith[Unit](blockId)(Adapter.CTRL)
    }
    def incPath(n: Int): Rep[Unit] = "cov-inc-path".reflectWriteWith[Unit](n)(Adapter.CTRL)
    def startMonitor: Rep[Unit] = "cov-start-mon".reflectWriteWith[Unit]()(Adapter.CTRL)
    def printBlockCov: Rep[Unit] = "print-block-cov".reflectWriteWith[Unit]()(Adapter.CTRL)
    def printPathCov: Rep[Unit] = "print-path-cov".reflectWriteWith[Unit]()(Adapter.CTRL)
    def printTime: Rep[Unit] = "print-time".reflectWriteWith[Unit]()(Adapter.CTRL)
  }

  trait Mem
  trait Value
  trait Stack
  trait SS

  type IntData = Long
  type BlockLabel = Int
  type Addr = Int
  type PC = Set[SMTBool]

  final val BYTE_SIZE: Int = 8
  final val DEFAULT_INT_BW: Int = BYTE_SIZE * 4
  final val ARCH_WORD_SIZE: Int = 64

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

  object SS {
    def init: Rep[SS] = "init-ss".reflectWriteWith[SS]()(Adapter.CTRL)
    def init(m: Rep[Mem]): Rep[SS] = "init-ss".reflectWriteWith[SS](m)(Adapter.CTRL)
    def checkPCToFile(s: Rep[SS]): Unit = "check_pc_to_file".reflectWriteWith[Unit](s)(Adapter.CTRL)
    def checkPC(pc: Rep[PC]): Rep[Boolean] = "check_pc".reflectWriteWith[Boolean](pc)(Adapter.CTRL)
  }

  class SSOps(ss: Rep[SS]) {
    private def assignSeq(xs: List[Int], vs: Rep[List[Value]]): Rep[Unit] =
      reflectWrite[Unit]("ss-assign-seq", ss, xs, vs)(ss)

    def lookup(x: String): Rep[Value] = reflectRead[Value]("ss-lookup-env", ss, x.hashCode)(ss)
    def assign(x: String, v: Rep[Value]): Rep[Unit] = reflectWrite[Unit]("ss-assign", ss, x.hashCode, v)(ss)
    def assign(xs: List[String], vs: Rep[List[Value]]): Rep[Unit] = assignSeq(xs.map(_.hashCode), vs)
    def lookup(addr: Rep[Value], size: Int = 1, isStruct: Int = 0): Rep[Value] = {
      require(size > 0)
      if (isStruct == 0) reflectRead[Value]("ss-lookup-addr", ss, addr)(ss)
      else reflectRead[Value]("ss-lookup-addr-struct", ss, addr, size)(ss)
    }
    def update(a: Rep[Value], v: Rep[Value]): Rep[Unit] = reflectWrite[Unit]("ss-update", ss, a, v)(ss)
    def allocStack(n: Rep[Int]): Rep[Unit] = reflectWrite[Unit]("ss-alloc-stack", ss, n)(ss)

    def heapLookup(addr: Rep[Addr]): Rep[Value] = reflectRead[Value]("ss-lookup-heap", ss, addr)(ss)
    def heapSize: Rep[Int] = reflectRead[Int]("ss-heap-size", ss)(ss)
    def heapAppend(vs: Rep[List[Value]]): Rep[Unit] = reflectWrite[Unit]("ss-heap-append", ss, vs)(ss)

    def stackSize: Rep[Int] = reflectRead[Int]("ss-stack-size", ss)(ss)
    def freshStackAddr: Rep[Addr] = stackSize

    def push: Rep[Unit] = reflectWrite[Unit]("ss-push", ss)(ss)
    def pop(keep: Rep[Int]): Rep[Unit] = reflectWrite[Unit]("ss-pop", ss, keep)(ss, Adapter.CTRL) // XXX: since pop is used in a map, will be DCE-ed if no CTRL
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
  implicit def lift(v: Rep[Value])(implicit s: Rep[SS]): Rep[List[(SS, Value)]] = List[(SS, Value)]((s, v))
  implicit def SS2RefSS(s: Rep[SS]): Rep[Ref[SS]] = s.asRepOf[Ref[SS]]
  implicit def RefSS2SS(s: Rep[Ref[SS]]): Rep[SS] = s.asRepOf[SS]

  object IntV {
    def apply(i: Rep[Int]): Rep[Value] = IntV(i, DEFAULT_INT_BW)
    def apply(i: Rep[Int], bw: Int): Rep[Value] =
      "make_IntV".reflectMutableWith[Value](i, bw)
    def apply(i: Rep[Long])(implicit d: DummyImplicit): Rep[Value] = IntV(i, DEFAULT_INT_BW)
    def apply(i: Rep[Long], bw: Int)(implicit d: DummyImplicit): Rep[Value] =
      "make_IntV".reflectMutableWith[Value](i, bw)
    def unapply(v: Rep[Value]): Option[(Int, Int)] = Unwrap(v) match {
      case Adapter.g.Def("make_IntV", Backend.Const(v: Int)::Backend.Const(bw: Int)::_) =>
        Some((v, bw))
      case Adapter.g.Def("make_IntV", Backend.Const(v: Long)::Backend.Const(bw: Int)::_) =>
        Some((v, bw))
      case _ => None
    }
  }
  object FloatV {
    // TODO: shall we keep float kinds?
    def apply(f: Rep[Float]): Rep[Value] = "make_FloatV".reflectWriteWith[Value](f)(Adapter.CTRL)
  }
  object LocV {
    def kStack: Rep[Int] = "kStack".reflectMutableWith[Int]()
    def kHeap: Rep[Int] = "kHeap".reflectMutableWith[Int]()
    def apply(l: Rep[Addr], kind: Rep[Int], size: Rep[Int] = unit(-1)):
      Rep[Value] = "make_LocV".reflectMutableWith[Value](l, kind, size)
  }
  object FunV {
    def apply(f: Rep[(Ref[SS], List[Value]) => List[(SS, Value)]]): Rep[Value] = f.asRepOf[Value]
  }
  object CPSFunV {
    def apply(f: Rep[(Ref[SS], List[Value], Cont) => Unit]): Rep[Value] = f.asRepOf[Value]
  }
  object SymV {
    def apply(s: Rep[String]): Rep[Value] = apply(s, DEFAULT_INT_BW)
    def apply(s: Rep[String], bw: Int): Rep[Value] =
      "make_SymV".reflectWriteWith[Value](s, bw)(Adapter.CTRL) //XXX: reflectMutable?
    def makeSymVList(i: Int): Rep[List[Value]] = {
      List[Value](Range(0, i).map(x => apply("x" + x.toString)):_*)
    }
  }
  object NullV {
    // for now
    def apply(): Rep[Value] = "null-v".reflectMutableWith[Value]()
  }

  object IntOp2 {
    def apply(op: String, o1: Rep[Value], o2: Rep[Value]) = "int_op_2".reflectWith[Value](op, o1, o2)
  }

  object FloatOp2 {
    def apply(op: String, o1: Rep[Value], o2: Rep[Value]) = "float_op_2".reflectWith[Value](op, o1, o2)
  }

  implicit class ValueOps(v: Rep[Value]) {
    // TODO: optimization like int
    def loc: Rep[Addr] = "proj_LocV".reflectWith[Addr](v)
    def int: Rep[Int] = v match {
      case IntV(n, bw) => unit(n)
      case _ => "proj_IntV".reflectWith[Int](v)
    }
    def float: Rep[Float] = "proj_FloatV".reflectWith[Float](v)
    def kind: Rep[Int] = "proj_LocV_kind".reflectWith[Int](v)
    def structAt(i: Rep[Int]) = "structV_at".reflectWith[Value](v, i)
    def apply(s: Rep[SS], args: Rep[List[Value]]): Rep[List[(SS, Value)]] = {
      Unwrap(v) match {
        case Adapter.g.Def("llsc-external-wrapper", Backend.Const("noop")::Nil) =>
          List((s, IntV(0)))
        case Adapter.g.Def("llsc-external-wrapper", Backend.Const(f: String)::Nil) =>
          f.reflectWith[List[(SS, Value)]](s, args)
        case _ =>
          val f = v.asRepOf[(SS, List[Value]) => List[(SS, Value)]]
          f(s, args)
      }
    }
    // The CPS version
    def apply(s: Rep[SS], args: Rep[List[Value]], k: Rep[Cont]): Rep[Unit] = {
      Unwrap(v) match {
        case Adapter.g.Def("llsc-external-wrapper", Backend.Const("noop")::Nil) =>
          k(s, IntV(0))
        case Adapter.g.Def("llsc-external-wrapper", Backend.Const(f: String)::Nil) =>
          // XXX: if the external function does not diverge, we don' need to
          // pass the continuation into it, we can just return a pair of state/value.
          System.out.println("use external function: " + f)
          f.reflectWith[Unit](s, args, k)
        case _ =>
          val f = v.asRepOf[(SS, List[Value], Cont) => Unit]
          f(s, args, k)
      }
    }
    def deref: Rep[Any] = "ValPtr-deref".reflectWith[Any](v)

    def bv_sext(bw: Rep[Int]): Rep[Value] =  "bv_sext".reflectWith[Value](v, bw)
    def isConc: Rep[Boolean] = "is-conc".reflectWith[Boolean](v)
    def toSMTBool: Rep[SMTBool] = "to-SMTBool".reflectWith[SMTBool](v)
    def toSMTBoolNeg: Rep[SMTBool] = "to-SMTBoolNeg".reflectWith[SMTBool](v)

    // TODO: toSMTBool vs toSMTExpr?
    //def toSMTExpr: Rep[SMTExpr] =
    //  Wrap[SMTExpr](Adapter.g.reflect("proj_SMTExpr", Unwrap(v)))

    def fp_toui(to: Int): Rep[Value] = "fp_toui".reflectWith[Value](v, to)
    def fp_tosi(to: Int): Rep[Value] = "fp_tosi".reflectWith[Value](v, to)
    def ui_tofp: Rep[Value] = "ui_tofp".reflectWith[Value](v)
    def si_tofp: Rep[Value] = "si_tofp".reflectWith[Value](v)
    def trunc(from: Rep[Int], to: Rep[Int]): Rep[Value] =
      "trunc".reflectWith[Value](v, from, to)
    def to_IntV: Rep[Value] = "to-IntV".reflectWith[Value](v)
  }

  object External extends Serializable {
    val warned = MultableSet[String]()
    val modeled = MultableSet[String](
      "sym_print", "malloc", "realloc", "llsc_assert", "make_symbolic",
      "__assert_fail"
    )
    def print: Rep[Value] = "llsc-external-wrapper".reflectWith[Value]("sym_print")
    def noop: Rep[Value] = "llsc-external-wrapper".reflectWith[Value]("noop")
  }

  object Intrinsics {
    val warnedSet = MultableSet[String]()
    def get(id: String): Rep[Value] =
      if (id.startsWith("@llvm.va_start")) llvm_va_start
      else if (id.startsWith("@llvm.memcpy")) llvm_memcopy
      else if (id.startsWith("@llvm.memset")) llvm_memset
      else if (id.startsWith("@llvm.memmove")) llvm_memset
      else {
        if (!warnedSet.contains(id)) {
          System.out.println(s"Warning: intrinsic $id is ignored")
          warnedSet.add(id)
        }
        External.noop
      }
    def llvm_memcopy: Rep[Value] = "llsc-external-wrapper".reflectWith[Value]("llvm_memcpy")
    def llvm_va_start: Rep[Value] = "llsc-external-wrapper".reflectWith[Value]("llvm_va_start")
    def llvm_memset: Rep[Value] = "llsc-external-wrapper".reflectWith[Value]("llvm_memset")
    def llvm_memmove: Rep[Value] = "llsc-external-wrapper".reflectWith[Value]("llvm_memmove")
  }
}
