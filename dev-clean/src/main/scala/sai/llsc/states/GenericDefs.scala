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

import scala.collection.immutable.{List => StaticList, Map => StaticMap, Set => StaticSet, Range => StaticRange}
import scala.collection.mutable.{Map => MutableMap, Set => MutableSet}

trait BasicDefs { self: SAIOps =>
  trait Mem
  trait Value
  trait Stack
  trait SS
  trait FS

  type IntData = Long
  type BlockLabel = Int
  type Addr = Long
  type PC = Set[SMTBool]
  type Id[T] = T
  type Fd = Int
  val bConst = Backend.Const
  lazy val gNode = Adapter.g.Def
  type bExp = Backend.Exp

  def initState: Rep[SS] = "init-ss".reflectWriteWith[SS]()(Adapter.CTRL)
  def initState(m: Rep[Mem]): Rep[SS] = "init-ss".reflectWriteWith[SS](m)(Adapter.CTRL)
  def checkPCToFile(s: Rep[SS]): Unit = "check_pc_to_file".reflectWriteWith[Unit](s)(Adapter.CTRL)
  def checkPC(pc: Rep[PC]): Rep[Boolean] = "check_pc".reflectWriteWith[Boolean](pc)(Adapter.CTRL)
}

object BlockCounter {
  private var counter: Int = 0
  def count: Int = counter
  def reset: Unit = counter = 0
  def fresh: Int = try { counter } finally { counter += 1 }
}

trait Coverage { self: SAIOps =>
  object Coverage {
    import scala.collection.mutable.HashMap
    private val blockMap: HashMap[String, Int] = HashMap[String, Int]()
    def getBlockId(s: String): Int =
      if (blockMap.contains(s)) blockMap(s)
      else {
        val id = BlockCounter.fresh
        blockMap(s) = id
        id
      }

    def setBlockNum: Rep[Unit] = "cov-set-blocknum".reflectWriteWith[Unit](BlockCounter.count)(Adapter.CTRL)
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
  object ExternalFun {
    private val warned = MutableSet[String]()
    private val used = MutableSet[String]()
    private val modeled = MutableSet[String](
      "sym_print", "print_string", "malloc", "realloc",
      "llsc_assert", "llsc_assert_eager", "__assert_fail", "sym_exit",
      "make_symbolic", "make_symbolic_whole",
      "open", "close", "read", "write", "stat", "stop", "syscall", "llsc_assume"
    )
    def apply(f: String): Rep[Value] = {
      if (!used.contains(f)) {
        System.out.println(s"Use external function $f.")
        used.add(f)
      }
      "llsc-external-wrapper".reflectWith[Value](f)
    }
    def unapply(v: Rep[Value]): Option[String] = Unwrap(v) match {
      case gNode("llsc-external-wrapper", bConst(f: String)::Nil) => Some(f)
      case _ => None
    }
    def get(id: String): Rep[Value] =
      if (modeled.contains(id.tail)) ExternalFun(id.tail)
      else if (id.startsWith("@llvm.va_start")) ExternalFun("llvm_va_start")
      else if (id.startsWith("@llvm.va_end")) ExternalFun("llvm_va_end")
      else if (id.startsWith("@llvm.va_copy")) ExternalFun("llvm_va_copy")
      else if (id.startsWith("@llvm.memcpy")) ExternalFun("llvm_memcpy")
      else if (id.startsWith("@llvm.memset")) ExternalFun("llvm_memset")
      else if (id.startsWith("@llvm.memmove")) ExternalFun("llvm_memset")
      else {
        if (!warned.contains(id)) {
          System.out.println(s"External function ${id.tail} is treated as a noop.")
          warned.add(id)
        }
        ExternalFun("noop")
      }
  }
}

@virtualize
trait ValueDefs { self: SAIOps with BasicDefs with Opaques =>
  import Constants._
  type PCont[W[_]] = ((W[SS], Value) => Unit)

  def mainArgs: Rep[List[Value]] = List[Value](unchecked[Value]("g_argc"), unchecked[Value]("g_argv"))
  implicit def intToLong(i: Int): Long = i.toLong

  object IntV {
    def apply(i: Long): Rep[Value] = IntV(unit(i), DEFAULT_INT_BW)
    def apply(i: Long, bw: Int): Rep[Value] = IntV(unit(i), bw)
    def apply(i: Rep[Long]): Rep[Value] = IntV(i, DEFAULT_INT_BW)
    def apply(i: Rep[Long], bw: Int): Rep[Value] = "make_IntV".reflectMutableWith[Value](i, bw)
    def unapply(v: Rep[Value]): Option[(Int, Int)] = Unwrap(v) match {
      case gNode("make_IntV", bConst(v: Long)::bConst(bw: Int)::_) =>
        Some((v, bw))
      case _ => None
    }
  }
  object FloatV {
    def apply(f: Rep[Float]): Rep[Value] = apply(f, 32)
    def apply(f: Rep[Float], bw: Int): Rep[Value] = "make_FloatV".reflectWriteWith[Value](f, bw)(Adapter.CTRL)
    def apply(v: String, bw: Int): Rep[Value] = {
      require(bw == 80)
      "make_FloatV".reflectWriteWith[Value](v, bw)(Adapter.CTRL)
    }
    def unapply(v: Rep[Value]): Option[(Either[Float, String], Int)] = Unwrap(v) match {
      case gNode("make_FloatV", bConst(f: Float)::bConst(bw: Int)::_) => Some((Left(f), bw))
      case gNode("make_FloatV", bConst(v: String)::bConst(bw: Int)::_) => Some((Right(v), bw))
      case _ => None
    }
  }
  object LocV {
    trait Kind
    def nullloc: Rep[Value] = "make_LocV_null".reflectMutableWith[Value]()
    def kStack: Rep[Kind] = "kStack".reflectMutableWith[Kind]()
    def kHeap: Rep[Kind] = "kHeap".reflectMutableWith[Kind]()
    def apply(l: Rep[Addr], kind: Rep[Kind], size: Rep[Int] = unit(-1)): Rep[Value] =
      "make_LocV".reflectMutableWith[Value](l, kind, size)
    def unapply(v: Rep[Value]): Option[(Rep[Addr], Rep[Kind], Rep[Int])] = Unwrap(v) match {
      case gNode("make_LocV", (a: bExp)::(k: bExp)::(size: bExp)::_) =>
        Some((Wrap[Addr](a), Wrap[Kind](k), Wrap[Int](size)))
      case _ => None
    }
  }

  object FunV {
    def apply[W[_]](f: Rep[(W[SS], List[Value]) => List[(SS, Value)]])(implicit m: Manifest[W[SS]]): Rep[Value] = {
      "make_FunV".reflectMutableWith[Value](f)
    }
    def unapply[W[_]](v: Rep[Value])(implicit m: Manifest[W[SS]]): Option[Rep[(W[SS], List[Value]) => List[(SS, Value)]]] =
      Unwrap(v) match {
        case gNode("make_FunV", (f: bExp)::Nil) =>
          Some(Wrap[(W[SS], List[Value]) => List[(SS, Value)]](f))
        case _ => None
      }
  }

  object CPSFunV {
    def apply[W[_]](f: Rep[(W[SS], List[Value], PCont[W]) => Unit])(implicit m: Manifest[W[SS]]): Rep[Value] = {
      "make_CPSFunV".reflectMutableWith[Value](f)
    }
    def unapply[W[_]](v: Rep[Value])(implicit m: Manifest[W[SS]]): Option[Rep[(W[SS], List[Value], PCont[W]) => Unit]] =
      Unwrap(v) match {
        case gNode("make_CPSFunV", (f: bExp)::Nil) =>
          Some(Wrap[(W[SS], List[Value], PCont[W]) => Unit](f))
        case _ => None
      }
  }

  object SymV {
    def apply(s: String): Rep[Value] = apply(s, DEFAULT_INT_BW)
    def apply(s: String, bw: Int): Rep[Value] =
      "make_SymV".reflectWriteWith[Value](unit(s), bw)(Adapter.CTRL)  //XXX: reflectMutable?
    def makeSymVList(i: Int): Rep[List[Value]] =
      List[Value](Range(0, i).map(x => apply("x" + x.toString)):_*)
    def unapply(v: Rep[Value]): Option[(String, Int)] = Unwrap(v) match {
      case gNode("make_SymV", bConst(x: String)::bConst(bw: Int)::_) => Some((x, bw))
      case _ => None
    }
  }

  object ShadowV {
    def apply(): Rep[Value] = "make_ShadowV".reflectMutableWith[Value]()
    def apply(i: Int): Rep[Value] = "make_ShadowV".reflectMutableWith[Value](unit(i))
    def unapply(v: Rep[Value]): Boolean = Unwrap(v) match {
      case gNode("make_ShadowV", _) => true
      case _ => false
    }
    def seq(size: Int): StaticList[Rep[Value]] = {
      val s = ShadowV()
      StaticList.fill(size)(s)
    }
    def indexSeq(size: Int): StaticList[Rep[Value]] = {
      require(size >= 0)
      StaticRange(-size, 0).reverse.map(ShadowV(_)).toList
    }
  }

  object NullPtr {
    def apply(): Rep[Value] = "nullptr".reflectMutableWith[Value]()
    def unapply(v: Rep[Value]): Boolean = Unwrap(v) match {
      case gNode("nullptr", _) => true
      case _ => false
    }
    def seq(size: Int): StaticList[Rep[Value]] = {
      val s = NullPtr()
      StaticList.fill(size)(s)
    }
  }

  object IntOp2 {
    def apply_noopt(op: String, o1: Rep[Value], o2: Rep[Value]): Rep[Value] =
      "int_op_2".reflectWith[Value](op, o1, o2)
    def apply(op: String, o1: Rep[Value], o2: Rep[Value]): Rep[Value] =
      op match {
        case "neq" => neq(o1, o2)
        case "eq" => eq(o1, o2)
        case _ => apply_noopt(op, o1, o2)
      }
    def neq(o1: Rep[Value], o2: Rep[Value]): Rep[Value] = (Unwrap(o1), Unwrap(o2)) match {
      case (gNode("bv_sext", (e1: bExp)::bConst(bw1: Int)::_),
            gNode("bv_sext", (e2: bExp)::bConst(bw2: Int)::_)) if bw1 == bw2 =>
        val v1 = Wrap[Value](e1)
        val v2 = Wrap[Value](e2)
        if (v1.bw == v2.bw) apply_noopt("neq", v1, v2)
        else apply_noopt("neq", o1, o2)
      case _ => apply_noopt("neq", o1, o2)
    }
    def eq(o1: Rep[Value], o2: Rep[Value]): Rep[Value] = (Unwrap(o1), Unwrap(o2)) match {
      case (gNode("bv_sext", (e1: bExp)::bConst(bw1: Int)::_),
            gNode("bv_sext", (e2: bExp)::bConst(bw2: Int)::_)) if bw1 == bw2 =>
        val v1 = Wrap[Value](e1)
        val v2 = Wrap[Value](e2)
        if (v1.bw == v2.bw) apply_noopt("eq", v1, v2)
        else apply_noopt("eq", o1, o2)
      case _ => apply_noopt("eq", o1, o2)
    }
  }

  object FloatOp2 {
    def apply(op: String, o1: Rep[Value], o2: Rep[Value]) = "float_op_2".reflectWith[Value](op, o1, o2)
  }

  implicit class ValueOps(v: Rep[Value]) {
    def bw: Rep[Int] = v match {
      case IntV(n, bw) => bw
      case LocV(a, k, size) => unit(64)
      case _ => "get-bw".reflectWith[Int](v)
    }
    def loc: Rep[Addr] = v match {
      case LocV(a, k, size) => a
      case _ => "proj_LocV".reflectWith[Addr](v)
    }
    def kind: Rep[LocV.Kind] = v match {
      case LocV(a, k, size) => k
      case _ => "proj_LocV_kind".reflectWith[LocV.Kind](v)
    }
    def int: Rep[Long] = v match {
      case IntV(n, bw) => unit(n)
      case _ => "proj_IntV".reflectWith[Int](v)
    }
    def float: Rep[Float] = "proj_FloatV".reflectWith[Float](v)
    def structAt(i: Rep[Long]) = "structV_at".reflectWith[Value](v, i)
    def apply[W[_]](s: Rep[W[SS]], args: Rep[List[Value]])(implicit m: Manifest[W[SS]]): Rep[List[(SS, Value)]] = {
      v match {
        case ExternalFun(f) =>
          if (f == "noop") List((s.asRepOf[SS], IntV(0)))
          else f.reflectWith[List[(SS, Value)]](s, args)
        case FunV(f) => f(s, args)
        case _ => "direct_apply".reflectWith[List[(SS, Value)]](v, s, args)
      }
    }
    // The CPS version
    // W[_] is parameterized over pass-by-value (Id) or pass-by-ref (Ref) of SS
    def apply[W[_]](s: Rep[W[SS]], args: Rep[List[Value]], k: Rep[PCont[W]])(implicit m: Manifest[W[SS]]): Rep[Unit] =
      v match {
        case ExternalFun(f) =>
          if (f == "noop") k(s, IntV(0))
          else f.reflectWith[Unit](s, args, k)
        case CPSFunV(f) => f(s, args, k)                       // direct call
        case _ => "cps_apply".reflectWith[Unit](v, s, args, k) // indirect call
      }

    def deref: Rep[Any] = "ValPtr-deref".reflectWith[Any](v)

    def sExt(bw: Int): Rep[Value] = "bv_sext".reflectWith[Value](v, bw)
    def zExt(bw: Int): Rep[Value] = "bv_zext".reflectWith[Value](v, bw)

    def isConc: Rep[Boolean] = v match {
      case IntV(_, _) => unit(true)
      case _ => "is-conc".reflectWith[Boolean](v)
    }
    def toSMTBool: Rep[SMTBool] = v.asRepOf[SMTBool]
    def toSMTBoolNeg: Rep[SMTBool] = "to-SMTNeg".reflectWith[SMTBool](v)

    def notNull: Rep[Boolean] = "not-null".reflectWith[Boolean](v)
    def fromFloatToUInt(toSize: Int): Rep[Value] = "fp_toui".reflectWith[Value](v, toSize)
    def fromFloatToSInt(toSize: Int): Rep[Value] = "fp_tosi".reflectWith[Value](v, toSize)
    def fromUIntToFloat: Rep[Value] = "ui_tofp".reflectWith[Value](v)
    def fromSIntToFloat: Rep[Value] = "si_tofp".reflectWith[Value](v)
    def trunc(from: Int, to: Int): Rep[Value] = "trunc".reflectWith[Value](v, from, to)
    def toIntV: Rep[Value] = "to-IntV".reflectWith[Value](v)
    def toLocV: Rep[Value] = "to-LocV".reflectWith[Value](v)

    def toBytes: Rep[List[Value]] = v match {
      case ShadowV() => List[Value](v)
      case IntV(n, bw) => ???
      case FloatV(f, bw) => ???
      case LocV(_, _, _) | FunV(_) | CPSFunV(_) =>
        List[Value](v::ShadowV.indexSeq(7):_*)
      case _ => "to-bytes".reflectWith[List[Value]](v)
    }

    def toShadowBytes: Rep[List[Value]] = v match {
      case ShadowV() => List[Value](v)
      case IntV(n, bw) => List[Value](v::ShadowV.indexSeq((bw+BYTE_SIZE-1)/BYTE_SIZE - 1):_*)
      case FloatV(f, bw) => List[Value](v::ShadowV.indexSeq((bw+BYTE_SIZE-1)/BYTE_SIZE - 1):_*)
      case LocV(_, _, _) | FunV(_) | CPSFunV(_) =>
        List[Value](v::ShadowV.indexSeq(7):_*)
      case _ => "to-bytes-shadow".reflectWith[List[Value]](v)
    }

  }
}
