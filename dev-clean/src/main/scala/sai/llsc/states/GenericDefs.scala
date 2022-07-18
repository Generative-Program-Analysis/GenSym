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
  trait PC
  trait FS

  type IntData = Long
  type BlockLabel = Int
  type Addr = Long
  type Id[T] = T
  type Fd = Int
  val bConst = Backend.Const
  type bSym = Backend.Sym
  lazy val gNode = Adapter.g.Def
  type bExp = Backend.Exp

  type CppAddr = Array[Char]

  def initState: Rep[SS] = "init-ss".reflectWriteWith[SS]()(Adapter.CTRL)
  def initState(m: Rep[Mem]): Rep[SS] = "init-ss".reflectWriteWith[SS](m)(Adapter.CTRL)
  def checkPCToFile(s: Rep[SS]): Unit = "check_pc_to_file".reflectWriteWith[Unit](s)(Adapter.CTRL)
  def checkPC(pc: Rep[PC]): Rep[Boolean] = "check_pc".reflectWriteWith[Boolean](pc)(Adapter.CTRL)

  def getPrimitiveTypeManifest(vt: LLVMType): Manifest[_] = vt match {
    case IntType(1) => manifest[Boolean]
    case IntType(8) => manifest[Char]
    case IntType(16) => manifest[Short]
    case IntType(32) => manifest[Int]
    case IntType(64) => manifest[Long]
    case FloatType(FK_Float) => manifest[Float]
    case FloatType(FK_Double) => manifest[Double]
    case PtrType(IntType(1), addrSpace) => manifest[Array[Boolean]]
    case PtrType(IntType(8), addrSpace) => manifest[Array[Char]]
    case PtrType(IntType(16), addrSpace) => manifest[Array[Short]]
    case PtrType(IntType(32), addrSpace) => manifest[Array[Int]]
    case PtrType(IntType(64), addrSpace) => manifest[Array[Long]]
    case _ => ???
  }

  def getPrimitiveTypeName(vt: LLVMType): String = vt match {
    case IntType(1) => "bool"
    case IntType(8) => "char"
    case IntType(16) => "short"
    case IntType(32) => "int"
    case IntType(64) => "long"
    case FloatType(FK_Float) => "float"
    case FloatType(FK_Double) => "double"
    case PtrType(ty, addrSpace) => "pointer"
    case _ => ???
  }
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
    def incInst(n: Int): Rep[Unit] = "cov-inc-inst".reflectWriteWith[Unit](n)(Adapter.CTRL)
    def startMonitor: Rep[Unit] = "cov-start-mon".reflectWriteWith[Unit]()(Adapter.CTRL)
    def printBlockCov: Rep[Unit] = "print-block-cov".reflectWriteWith[Unit]()(Adapter.CTRL)
    def printPathCov: Rep[Unit] = "print-path-cov".reflectWriteWith[Unit]()(Adapter.CTRL)
    def printTime: Rep[Unit] = "print-time".reflectWriteWith[Unit]()(Adapter.CTRL)
  }
}

trait Opaques { self: SAIOps with BasicDefs =>
  object NativeExternalFun {
    private val used = MutableSet[String]()
    def apply(f: String, ret: Option[LLVMType] = None): Rep[Value] = {
      if (!used.contains(f)) {
        System.out.println(s"Use native function $f.")
        used.add(f)
      }
      "llsc-native-external-wrapper".reflectWith[Value](f, ret)
    }
    def unapply(v: Rep[Value]): Option[(String, Option[LLVMType])] = Unwrap(v) match {
      case gNode("llsc-native-external-wrapper", bConst(f: String)::bConst(ret: Option[LLVMType])::Nil) => Some((f, ret))
      case _ => None
    }
  }

  object ExternalFun {
    private val warned = MutableSet[String]()
    private val used = MutableSet[String]()
    private val modeled = MutableSet[String](
      "sym_print", "print_string", "malloc", "realloc",
      "llsc_assert", "llsc_assert_eager", "__assert_fail", "sym_exit",
      "make_symbolic", "make_symbolic_whole",
      "stop", "syscall", "llsc_assume",
      "__errno_location", "_exit", "exit", "abort", "calloc", "llsc_is_symbolic", "llsc_get_valuel", "getpagesize", "memalign"
    )
    private val syscalls = MutableSet[String](
      "open", "close", "read", "write", "lseek", "stat", "mkdir", "rmdir", "creat", "unlink", "chmod", "chown"
    )
    val rederict = scala.collection.immutable.Set[String]("@memcpy", "@memset", "@memmove")
    val unsafeExternals = MutableSet[String]("fork", "exec", "error", "raise", "kill", "free", "vprintf")
    def apply(f: String, ret: Option[LLVMType] = None): Rep[Value] = {
      if (!used.contains(f)) {
        System.out.println(s"Use external function $f.")
        used.add(f)
      }
      "llsc-external-wrapper".reflectWith[Value](f, ret)
    }
    def unapply(v: Rep[Value]): Option[(String, Option[LLVMType])] = Unwrap(v) match {
      case gNode("llsc-external-wrapper", bConst(f: String)::bConst(ret: Option[LLVMType])::Nil) => Some((f, ret))
      case _ => None
    }
    def get(id: String, ret: Option[LLVMType] = None, argTypes: Option[List[LLVMType]]): Option[Rep[Value]] =
      if (modeled.contains(id.tail)) Some(ExternalFun(id.tail))
      else if (syscalls.contains(id.tail)) Some(ExternalFun(s"syscall_${id.tail}"))
      else if (id.startsWith("@llvm.va_start")) Some(ExternalFun("llvm_va_start"))
      else if (id.startsWith("@llvm.va_end")) Some(ExternalFun("llvm_va_end"))
      else if (id.startsWith("@llvm.va_copy")) Some(ExternalFun("llvm_va_copy"))
      else if (id.startsWith("@llvm.memcpy")) Some(ExternalFun("llvm_memcpy"))
      else if (id.startsWith("@llvm.memset")) Some(ExternalFun("llvm_memset"))
      else if (id.startsWith("@llvm.memmove")) Some(ExternalFun("llvm_memmove"))
      else if (id == "@memcpy") Some(ExternalFun("llvm_memcpy"))
      else if (id == "@memset") Some(ExternalFun("llvm_memset"))
      else if (id == "@memmove") Some(ExternalFun("llvm_memmove"))
      else if (unsafeExternals.contains(id.tail) || id.startsWith("@llvm.")) {
        if (!warned.contains(id)) {
          System.out.println(s"Unsafe External function ${id.tail} is treated as a noop.")
          warned.add(id)
        }
        Some(ExternalFun("noop", ret))
      } else None // Will be executed natively
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
      // Todo: add bSym rhs case
      case gNode("make_IntV", bConst(v: Long)::bConst(bw: Int)::_) =>
        Some((v, bw))
      case _ => None
    }
  }
  object FloatV {
    def apply(f: Rep[Double]): Rep[Value] = apply(f, 32)
    def apply(f: Rep[Double], bw: Int): Rep[Value] = "make_FloatV".reflectWriteWith[Value](f, bw)(Adapter.CTRL)
    def apply(v: String, bw: Int): Rep[Value] = {
      require(bw == 80)
      "make_FloatV".reflectWriteWith[Value](v, bw)(Adapter.CTRL)
    }
    def unapply(v: Rep[Value]): Option[(Either[Double, String], Int)] = Unwrap(v) match {
      case gNode("make_FloatV", bConst(f: Double)::bConst(bw: Int)::_) => Some((Left(f), bw))
      case gNode("make_FloatV", bConst(v: String)::bConst(bw: Int)::_) => Some((Right(v), bw))
      case _ => None
    }
  }
  object LocV {
    trait Kind
    def kStack: Rep[Kind] = "kStack".reflectMutableWith[Kind]()
    def kHeap: Rep[Kind] = "kHeap".reflectMutableWith[Kind]()
    def apply(l: Rep[Addr], kind: Rep[Kind], size: Rep[Long], off: Rep[Long] = 0L): Rep[Value] =
      "make_LocV".reflectMutableWith[Value](l, kind, size, off)
    def unapply(v: Rep[Value]): Option[(Rep[Addr], Rep[Kind], Rep[Long], Rep[Long])] = Unwrap(v) match {
      case gNode("make_LocV", (a: bExp)::(k: bExp)::(size: bExp)::(off: bExp)::_) =>
        Some((Wrap[Addr](a), Wrap[Kind](k), Wrap[Long](size), Wrap[Long](off)))
      case _ => None
    }
  }

  object SymLocV {
    def apply(l: Rep[Addr], kind: Rep[LocV.Kind], size: Rep[Long], off: Rep[Value]): Rep[Value] =
      "make_SymLocV".reflectMutableWith[Value](l, kind, size, off)
    def unapply(v: Rep[Value]): Option[(Rep[Addr], Rep[LocV.Kind], Rep[Long], Rep[Value])] = Unwrap(v) match {
      case gNode("make_SymLocV", (a: bExp)::(k: bExp)::(size: bExp)::(off: bSym)::_) =>
        Some((Wrap[Addr](a), Wrap[LocV.Kind](k), Wrap[Long](size), Wrap[Value](off)))
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

  // This refers to null in LLVM
  object NullLoc {
    def apply(): Rep[Value] = "make_LocV_null".reflectMutableWith[Value]()
    def unapply(v: Rep[Value]): Boolean = Unwrap(v) match {
      case gNode("make_LocV_null", _) => true
      case _ => false
    }
  }

  // This refers to the nullptr in C++
  object NullPtr {
    def apply[T: Manifest]: Rep[T] = "nullptr".reflectUnsafeWith[T]()
    def unapply[T: Manifest](v: Rep[T]): Boolean = Unwrap(v) match {
      case gNode("nullptr", _) => true
      case _ => false
    }
    def seq[T: Manifest](size: Int): StaticList[Rep[T]] =
      StaticList.fill(size)(NullPtr[T])
  }

  object IntOp2 {
    def apply_noopt(op: String, o1: Rep[Value], o2: Rep[Value]): Rep[Value] =
      "int_op_2".reflectWith[Value](op, o1, o2)
    def apply(op: String, o1: Rep[Value], o2: Rep[Value]): Rep[Value] =
      if (!Config.opt) {
        apply_noopt(op, o1, o2)
      } else {
        op match {
          case "neq" => neq(o1, o2)
          case "eq" => eq(o1, o2)
          case _ => apply_noopt(op, o1, o2)
        }
      }

    def unapply(v: Rep[Value]): Option[(String, Rep[Value], Rep[Value])] = Unwrap(v) match {
      case gNode("int_op_2", bConst(x: String)::(o1: bSym)::(o2: bSym)::_) =>
        Some((x, Wrap[Value](o1), Wrap[Value](o2)))
      case _ => None
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
      case IntV(n, bw) if Config.opt => bw
      case LocV(a, k, size, off) if Config.opt => unit(DEFAULT_ADDR_BW)
      case SymLocV(a, k, size, off) if Config.opt => unit(DEFAULT_ADDR_BW)
      case _ => "get-bw".reflectWith[Int](v)
    }
    def loc: Rep[Addr] = v match {
      case LocV(a, k, size, off) if Config.opt => a
      // Todo: should we add here for symlocv
      case _ => "proj_LocV".reflectWith[Addr](v)
    }
    def kind: Rep[LocV.Kind] = v match {
      case LocV(a, k, size, off) if Config.opt => k
      case SymLocV(a, k, size, off) if Config.opt => k
      case _ => "proj_LocV_kind".reflectWith[LocV.Kind](v)
    }
    def int: Rep[Long] = v match {
      case IntV(n, bw) if Config.opt => unit(n)
      case _ => "proj_IntV".reflectWith[Long](v)
    }
    def float: Rep[Float] = "proj_FloatV".reflectWith[Float](v)
    def structAt(i: Rep[Long]) = "structV_at".reflectWith[Value](v, i)
    def apply[W[_]](s: Rep[W[SS]], args: Rep[List[Value]])(implicit m: Manifest[W[SS]]): Rep[List[(SS, Value)]] = {
      v match {
        case ExternalFun(f, ty) =>
          if (f == "noop" && Config.opt) {
            val retval = ty match {
              case Some(IntType(size)) => IntV(0, size)
              case Some(PtrType(_, _)) => IntV(0, 64)
              case _ => IntV(0)
            }
            List((s.asRepOf[SS], retval))
          } else f.reflectWith[List[(SS, Value)]](s, args)
        case FunV(f) => f(s, args)
        case _ => "direct_apply".reflectWith[List[(SS, Value)]](v, s, args)
      }
    }
    // The CPS version
    // W[_] is parameterized over pass-by-value (Id) or pass-by-ref (Ref) of SS
    def apply[W[_]](s: Rep[W[SS]], args: Rep[List[Value]], k: Rep[PCont[W]])(implicit m: Manifest[W[SS]]): Rep[Unit] =
      v match {
        case ExternalFun(f, ty) =>
          if (f == "noop" && Config.opt) {
            val retval = ty match {
              case Some(IntType(size)) => IntV(0, size)
              case Some(PtrType(_, _)) => IntV(0, 64)
              case _ => IntV(0)
            }
            k(s, retval)
          } else f.reflectWith[Unit](s, args, k)
        case CPSFunV(f) => f(s, args, k)                       // direct call
        case _ => "cps_apply".reflectWith[Unit](v, s, args, k) // indirect call
      }

    def applyNative[A:Manifest](args: List[Rep[Any]]): Rep[A] = {
      v match {
        case NativeExternalFun(f, ty) =>
          f.reflectWriteWith[A](args:_*)(Adapter.CTRL)
        case _ => ???
      }
    }

    def deref: Rep[Any] = "ValPtr-deref".reflectUnsafeWith[Any](v)

    val ext_simpl_op = StaticList[String]("make_SymV", "make_IntV", "bv_sext", "bv_zext")

    def sExt(bw: Int): Rep[Value] = Unwrap(v) match {
      case gNode(s, (v1: bExp)::bConst(bw1: Int)::_) if (ext_simpl_op.contains(s) && (bw1 == bw)) => v
      case _ => "bv_sext".reflectWith[Value](v, bw)
    }

    def zExt(bw: Int): Rep[Value] = Unwrap(v) match {
      case gNode(s, (v1: bExp)::bConst(bw1: Int)::_) if (ext_simpl_op.contains(s) && (bw1 == bw)) => v
      case _ => "bv_zext".reflectWith[Value](v, bw)
    }

    def isConc: Rep[Boolean] = v match {
      case IntV(_, _) if Config.opt => unit(true)
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

    def ptrOff(off: Rep[Value]): Rep[Value] =
      (v, off) match {
        // Todo: Add case for IntV non-const
        case (LocV(a, k, s, o), IntV(n, _)) => LocV(a, k, s, o + n)
        case _ => "ptroff".reflectWith[Value](v, off)
      }

    def addOff(rhs: Rep[Value]): Rep[Value] =
      (v, rhs) match {
        case (IntV(n1, bw1), IntV(n2, bw2)) => if (bw1 == bw2) IntV(n1 + n2, bw1) else ???
        case _ => IntOp2("add", v, rhs)
      }

    def mulOff(rhs: Rep[Value]): Rep[Value] =
      (v, rhs) match {
        case (IntV(n1, bw1), IntV(n2, bw2)) => if (bw1 == bw2) IntV(n1 * n2, bw1) else ???
        case _ => IntOp2("mul", v, rhs)
      }

    def toBytes: Rep[List[Value]] = v match {
      case ShadowV() => List[Value](v)
      case IntV(n, bw) => ???
      case FloatV(f, bw) => ???
      case LocV(_, _, _, _) | SymLocV(_, _, _, _) | FunV(_) | CPSFunV(_) =>
        List[Value](v::ShadowV.indexSeq(7):_*)
      case _ => "to-bytes".reflectWith[List[Value]](v)
    }

    def toShadowBytes: Rep[List[Value]] = v match {
      case ShadowV() => List[Value](v)
      case IntV(n, bw) => List[Value](v::ShadowV.indexSeq((bw+BYTE_SIZE-1)/BYTE_SIZE - 1):_*)
      case FloatV(f, bw) => List[Value](v::ShadowV.indexSeq((bw+BYTE_SIZE-1)/BYTE_SIZE - 1):_*)
      case LocV(_, _, _, _) | SymLocV(_, _, _, _) | FunV(_) | CPSFunV(_) =>
        List[Value](v::ShadowV.indexSeq(7):_*)
      case NullLoc() => List[Value](v::ShadowV.indexSeq((ARCH_WORD_SIZE+BYTE_SIZE-1)/BYTE_SIZE - 1):_*)
      case _ => "to-bytes-shadow".reflectWith[List[Value]](v)
    }

  }
}
