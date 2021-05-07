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
trait SymExeDefs extends SAIOps with StagedNondet {
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

  def runParRepNondet[A: Manifest](comp: Comp[Nondet ⊗ ∅, Rep[A]]): Comp[∅, Rep[List[A]]] = {
    (handler[Nondet, ∅, Rep[A], Rep[List[A]]] {
      case Return(x) => ret(List(x))
    } (new DeepH[Nondet, ∅, Rep[List[A]]] {
      def apply[X] = {
        case NondetList$(xs, k) => ret(xs.foldLeft(List[A]()) { case (acc, x) => acc ++ k(x) })
        case BinChoice$((), k) =>
          for {
            xs <- k(true)
            ys <- k(false)
          } yield {
            //System.out.println(xs, ys)
            xs ++ ys
          }
      }
    }))(comp)
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

  trait Mem
  trait Value
  trait Stack
  trait SS

  type IntData = Long
  type BlockLabel = Int
  type Addr = Int
  type PC = Set[SMTBool]
  type E = State[Rep[SS], *] ⊗ (Nondet ⊗ ∅)

  final val BYTE_SIZE: Int = 8
  final val DEFAULT_INT_BW: Int = BYTE_SIZE * 4
  final val ARCH_WORD_SIZE: Int = 64

  object SS {
    def init: Rep[SS] = "init-ss".reflectWriteWith[SS]()(Adapter.CTRL)
    def init(m: Rep[Mem]): Rep[SS] = "init-ss".reflectWriteWith[SS](m)(Adapter.CTRL)
    def checkPCToFile(s: Rep[SS]): Unit = "check_pc_to_file".reflectWriteWith[Unit](s)(Adapter.CTRL)
    def checkPC(pc: Rep[PC]): Rep[Boolean] = "check_pc".reflectWriteWith[Boolean](pc)(Adapter.CTRL)
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


  object IntV {
    def apply(i: Rep[Int]): Rep[Value] = IntV(i, DEFAULT_INT_BW)
    def apply(i: Rep[Int], bw: Int): Rep[Value] =
      "make_IntV".reflectWriteWith[Value](i, bw)(Adapter.CTRL)
      //"make_IntV".reflectWriteWith[Value](i, bw)()
    def unapply(v: Rep[Value]): Option[(Int, Int)] = Unwrap(v) match {
      case Adapter.g.Def("make_IntV", Backend.Const(v: Int)::Backend.Const(bw: Int)::_) =>
        Some((v, bw))
      case _ => None
    }
  }
  object FloatV {
    // TODO: shall we keep float kinds?
    def apply(f: Rep[Float]): Rep[Value] = "make_FloatV".reflectWriteWith[Value](f)(Adapter.CTRL)
  }
  object LocV {
    def kStack: Rep[Int] = "kStack".reflectWriteWith[Int]()(Adapter.CTRL)
    def kHeap: Rep[Int] = "kHeap".reflectWriteWith[Int]()(Adapter.CTRL)
    def apply(l: Rep[Addr], kind: Rep[Int], size: Rep[Int]=unit(-1)): 
      Rep[Value] = "make_LocV".reflectWriteWith[Value](l, kind, size)(Adapter.CTRL)
  }
  object FunV {
    def apply(f: Rep[(SS, List[Value]) => List[(SS, Value)]]): Rep[Value] = f.asRepOf[Value]
  }
  object SymV {
    def apply(s: Rep[String]): Rep[Value] = apply(s, DEFAULT_INT_BW)
    def apply(s: Rep[String], bw: Int): Rep[Value] = "make_SymV".reflectWriteWith[Value](s, bw)(Adapter.CTRL)
    def makeSymVList(i: Int): Rep[List[Value]] = {
      List[Value](Range(0, i).map(x => apply("x" + x.toString)):_*)
    }
  }
  object NullV {
    // for now
    def apply(): Rep[Value] = IntV(0)
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
        case Adapter.g.Def("llsc-external-wrapper", Backend.Const(f: String)::Nil) =>
          f.reflectWith[List[(SS, Value)]](s, args)
        case _ =>
          val f = v.asRepOf[(SS, List[Value]) => List[(SS, Value)]]
          f(s, args)
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
    val warned_external = MultableSet[String]()
    val modeled_external: MultableSet[String] = MultableSet(
      "sym_print", "malloc", "realloc", "llsc_assert", "make_symbolic",
      "__assert_fail"
    )
    def print: Rep[Value] = "llsc-external-wrapper".reflectWith[Value]("sym_print")
    def noop: Rep[Value] = "llsc-external-wrapper".reflectWith[Value]("noop")
  }

  object Intrinsics {
    val warned_set = MultableSet[String]()
    def match_intrinsics(id: String): Rep[Value] = id match {
      case id if id.startsWith("@llvm.va_start") => llvm_va_start
      case id if id.startsWith("@llvm.memcpy") => llvm_memcopy
      case id if id.startsWith("@llvm.memset") => llvm_memset
      case id if id.startsWith("@llvm.memmove") => llvm_memset
      case _ => {
        if (!warned_set.contains(id)) {
          System.out.println(s"Warning: intrinsic $id is ignored")
          warned_set.add(id)
        }
        External.noop
      }
    }
    def llvm_memcopy: Rep[Value] = "llsc-external-wrapper".reflectWith[Value]("llvm_memcpy")
    def llvm_va_start: Rep[Value] = "llsc-external-wrapper".reflectWith[Value]("llvm_va_start")
    def llvm_memset: Rep[Value] = "llsc-external-wrapper".reflectWith[Value]("llvm_memset")
    def llvm_memmove: Rep[Value] = "llsc-external-wrapper".reflectWith[Value]("llvm_memmove")
  }
}

object FunName {
  val bindings: MultableMap[Int, String] = MultableMap()
  val blockBindings: MultableMap[Int, String] = MultableMap()
}
