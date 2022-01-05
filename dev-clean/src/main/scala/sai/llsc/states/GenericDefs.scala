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

trait BasicDefs { self: SAIOps =>
  trait Mem
  trait Value
  trait Stack
  trait SS

  type IntData = Long
  type BlockLabel = Int
  type Addr = Int
  type PC = Set[SMTBool]
  type Id[T] = T

  object SS {
    def init: Rep[SS] = "init-ss".reflectWriteWith[SS]()(Adapter.CTRL)
    def init(m: Rep[Mem]): Rep[SS] = "init-ss".reflectWriteWith[SS](m)(Adapter.CTRL)
    def checkPCToFile(s: Rep[SS]): Unit = "check_pc_to_file".reflectWriteWith[Unit](s)(Adapter.CTRL)
    def checkPC(pc: Rep[PC]): Rep[Boolean] = "check_pc".reflectWriteWith[Boolean](pc)(Adapter.CTRL)
  }
}

trait Coverage { self: SAIOps =>
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
}

trait Opaques { self: SAIOps with BasicDefs =>
  object External extends Serializable {
    val warned = MutableSet[String]()
    val modeled = MutableSet[String](
      "sym_print", "malloc", "realloc", "llsc_assert", "make_symbolic",
      "open", "close", "sym_exit",
      "__assert_fail"
    )
    def print: Rep[Value] = "llsc-external-wrapper".reflectWith[Value]("sym_print")
    def noop: Rep[Value] = "llsc-external-wrapper".reflectWith[Value]("noop")
  }

  object Intrinsics {
    val warned = MutableSet[String]()
    def get(id: String): Rep[Value] =
      if (id.startsWith("@llvm.va_start")) llvm_va_start
      else if (id.startsWith("@llvm.memcpy")) llvm_memcopy
      else if (id.startsWith("@llvm.memset")) llvm_memset
      else if (id.startsWith("@llvm.memmove")) llvm_memset
      else {
        if (!warned.contains(id)) {
          System.out.println(s"Warning: intrinsic $id is ignored")
          warned.add(id)
        }
        External.noop
      }
    def llvm_memcopy: Rep[Value] = "llsc-external-wrapper".reflectWith[Value]("llvm_memcpy")
    def llvm_va_start: Rep[Value] = "llsc-external-wrapper".reflectWith[Value]("llvm_va_start")
    def llvm_memset: Rep[Value] = "llsc-external-wrapper".reflectWith[Value]("llvm_memset")
    def llvm_memmove: Rep[Value] = "llsc-external-wrapper".reflectWith[Value]("llvm_memmove")
  }
}

trait ValueDefs { self: SAIOps with BasicDefs =>
  import Constants._
  val bConst = Backend.Const
  lazy val gNode = Adapter.g.Def

  type bExp = Backend.Exp
  type PCont[W[_]] = ((W[SS], Value) => Unit)

  object IntV {
    def apply(i: Rep[Int]): Rep[Value] = IntV(i, DEFAULT_INT_BW)
    def apply(i: Rep[Int], bw: Int): Rep[Value] =
      "make_IntV".reflectMutableWith[Value](i, bw)
    def apply(i: Rep[Long])(implicit d: DummyImplicit): Rep[Value] = IntV(i, DEFAULT_INT_BW)
    def apply(i: Rep[Long], bw: Int)(implicit d: DummyImplicit): Rep[Value] =
      "make_IntV".reflectMutableWith[Value](i, bw)
    def unapply(v: Rep[Value]): Option[(Int, Int)] = Unwrap(v) match {
      case gNode("make_IntV", bConst(v: Int)::bConst(bw: Int)::_) =>
        Some((v, bw))
      case gNode("make_IntV", bConst(v: Long)::bConst(bw: Int)::_) =>
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
    def apply(l: Rep[Addr], kind: Rep[Int], size: Rep[Int] = unit(-1)): Rep[Value] =
      "make_LocV".reflectMutableWith[Value](l, kind, size)
    def unapply(v: Rep[Value]): Option[(Rep[Addr], Int, Int)] = Unwrap(v) match {
      case gNode("make_LocV", (a: bExp)::bConst(k: Int)::bConst(size: Int)::_) =>
        Some((Wrap[Addr](a), k, size))
      case _ => None
    }
  }
  object FunV {
    def apply[W[_]](f: Rep[(W[SS], List[Value]) => List[(SS, Value)]])(implicit m: Manifest[W[SS]]): Rep[Value] = f.asRepOf[Value]
  }
  object CPSFunV {
    def apply[W[_]](f: Rep[(W[SS], List[Value], PCont[W]) => Unit])(implicit m: Manifest[W[SS]]): Rep[Value] = f.asRepOf[Value]
  }
  object SymV {
    def apply(s: Rep[String]): Rep[Value] = apply(s, DEFAULT_INT_BW)
    def apply(s: Rep[String], bw: Int): Rep[Value] =
      "make_SymV".reflectWriteWith[Value](s, bw)(Adapter.CTRL)  //XXX: reflectMutable?
    def makeSymVList(i: Int): Rep[List[Value]] =
      List[Value](Range(0, i).map(x => apply("x" + x.toString)):_*)
  }
  object NullV {
    def apply(): Rep[Value] = "null-v".reflectMutableWith[Value]()
  }

  object IntOp2 {
    def apply(op: String, o1: Rep[Value], o2: Rep[Value]) = "int_op_2".reflectWith[Value](op, o1, o2)
  }

  object FloatOp2 {
    def apply(op: String, o1: Rep[Value], o2: Rep[Value]) = "float_op_2".reflectWith[Value](op, o1, o2)
  }

  implicit class ValueOps(v: Rep[Value]) {
    def loc: Rep[Addr] = v match {
      case LocV(a, k, size) => a
      case _ => "proj_LocV".reflectWith[Addr](v)
    }
    def kind: Rep[Int] = v match {
      case LocV(a, k, size) => k
      case _ => "proj_LocV_kind".reflectWith[Int](v)
    }
    def int: Rep[Int] = v match {
      case IntV(n, bw) => unit(n)
      case _ => "proj_IntV".reflectWith[Int](v)
    }
    def float: Rep[Float] = "proj_FloatV".reflectWith[Float](v)
    def structAt(i: Rep[Int]) = "structV_at".reflectWith[Value](v, i)
    def apply(s: Rep[SS], args: Rep[List[Value]]): Rep[List[(SS, Value)]] = {
      Unwrap(v) match {
        case gNode("llsc-external-wrapper", bConst("noop")::Nil) =>
          List((s, IntV(0)))
        case gNode("llsc-external-wrapper", bConst(f: String)::Nil) =>
          System.out.println("use external function: " + f)
          f.reflectWith[List[(SS, Value)]](s, args)
        case _ =>
          val f = v.asRepOf[(SS, List[Value]) => List[(SS, Value)]]
          f(s, args)
      }
    }
    // The CPS version
    // W[_] is parameterized over pass-by-value (Id) or pass-by-ref (Ref) of SS
    def apply[W[_]](s: Rep[W[SS]], args: Rep[List[Value]], k: Rep[PCont[W]])(implicit m: Manifest[W[SS]]): Rep[Unit] = {
      Unwrap(v) match {
        case gNode("llsc-external-wrapper", bConst("noop")::Nil) =>
          k(s, IntV(0))
        case gNode("llsc-external-wrapper", bConst(f: String)::Nil) =>
          // XXX: if the external function does not diverge, we don't need to
          // pass the continuation into it, we can just return a pair of state/value.
          System.out.println("use external function: " + f)
          f.reflectWith[Unit](s, args, k)
        case _ =>
          val f = v.asRepOf[(W[SS], List[Value], PCont[W]) => Unit]
          f(s, args, k)
      }
    }
    def deref: Rep[Any] = "ValPtr-deref".reflectWith[Any](v)

    def bv_sext(bw: Rep[Int]): Rep[Value] =  "bv_sext".reflectWith[Value](v, bw)
    // TODO: impl bv_zext in backend
    // XXX: bv_sext -> bv_zext?
    def bv_zext(bw: Rep[Int]): Rep[Value] =  "bv_sext".reflectWith[Value](v, bw)

    def isConc: Rep[Boolean] = "is-conc".reflectWith[Boolean](v)
    def toSMTBool: Rep[SMTBool] = "to-SMTBool".reflectWith[SMTBool](v)
    def toSMTBoolNeg: Rep[SMTBool] = "to-SMTBoolNeg".reflectWith[SMTBool](v)

    def fp_toui(to: Int): Rep[Value] = "fp_toui".reflectWith[Value](v, to)
    def fp_tosi(to: Int): Rep[Value] = "fp_tosi".reflectWith[Value](v, to)
    def ui_tofp: Rep[Value] = "ui_tofp".reflectWith[Value](v)
    def si_tofp: Rep[Value] = "si_tofp".reflectWith[Value](v)
    def trunc(from: Rep[Int], to: Rep[Int]): Rep[Value] =
      "trunc".reflectWith[Value](v, from, to)
    def to_IntV: Rep[Value] = "to-IntV".reflectWith[Value](v)
  }
}
