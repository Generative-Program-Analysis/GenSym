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

case class Counter() {
  import scala.collection.mutable.HashMap
  private var counter: Int = 0
  private val map: HashMap[String, Int] = HashMap[String, Int]()
  override def toString: String =
    map.toList.sortBy(_._2).map(p => s"  ${p._1} -> ${p._2}").mkString("\n")
  def count: Int = counter
  def reset: Unit = { counter = 0; map.clear }
  def fresh: Int = try { counter } finally { counter += 1 }
  def get(s: String): Int = {
    require(s.contains("_"))
    if (map.contains(s)) map(s) else try { fresh } finally { map(s) = count-1 }
  }
}

object Counter {
  import scala.collection.mutable.HashMap
  val block = Counter()
  val variable = Counter()
  val function = Counter()
  val branchStat: HashMap[Int, Int] = HashMap[Int, Int]()
  def setBranchNum(ctx: Ctx, n: Int): Unit = {
    val blockId = Counter.block.get(ctx.toString)
    if (!branchStat.contains(blockId)) branchStat(blockId) = n
  }
}

trait BasicDefs { self: SAIOps =>
  trait Mem; trait Stack
  trait SS;  trait PC; trait FS
  trait Value
  trait IntV extends Value
  trait FloatV extends Value
  trait LocV extends IntV
  trait SymV extends Value
  trait SymLocV extends SymV
  trait FunV extends Value
  trait CPSFunV extends Value

  type IntData = Long
  type BlockLabel = Int
  type Addr = Long
  type Id[T] = T
  type Fd = Int
  val bConst = Backend.Const
  type bSym = Backend.Sym
  lazy val gNode = Adapter.g.Def
  type bExp = Backend.Exp

  def initState: Rep[SS] = "init-ss".reflectWriteWith[SS]()(Adapter.CTRL)
  def initState(m: Rep[Mem]): Rep[SS] = "init-ss".reflectWriteWith[SS](m)(Adapter.CTRL)
  def checkPCToFile(s: Rep[SS]): Unit = "check_pc_to_file".reflectWriteWith[Unit](s)(Adapter.CTRL)
  def checkPC(pc: Rep[PC]): Rep[Boolean] = "check_pc".reflectWriteWith[Boolean](pc)(Adapter.CTRL)

  def varId(x: String)(implicit ctx: Ctx): Int =
    if (x == "Vararg") -1 else Counter.variable.get(ctx.withVar(x))
}

trait Coverage { self: SAIOps =>
  object Coverage {
    def setBlockNum: Rep[Unit] = "cov-set-blocknum".reflectWriteWith[Unit](Counter.block.count)(Adapter.CTRL)
    //def incBlock(funName: String, label: String): Rep[Unit] = incBlock(Ctx(funName, label))
    //def incBlock(ctx: Ctx): Rep[Unit] =
    //  "cov-inc-block".reflectWriteWith[Unit](Counter.block.get(ctx.toString))(Adapter.CTRL)
    def incBranch(ctx: Ctx, n: Int): Unit =
      "cov-inc-br".reflectWriteWith[Unit](Counter.block.get(ctx.toString), n)(Adapter.CTRL)

    def incPath(n: Rep[Int]): Rep[Unit] = "cov-inc-path".reflectWriteWith[Unit](n)(Adapter.CTRL)
    def incInst(n: Int): Rep[Unit] = "cov-inc-inst".reflectWriteWith[Unit](n)(Adapter.CTRL)
    def startMonitor: Rep[Unit] = "cov-start-mon".reflectWriteWith[Unit]()(Adapter.CTRL)
    def printBlockCov: Rep[Unit] = "print-block-cov".reflectWriteWith[Unit]()(Adapter.CTRL)
    def printPathCov: Rep[Unit] = "print-path-cov".reflectWriteWith[Unit]()(Adapter.CTRL)
    def printTime: Rep[Unit] = "print-time".reflectWriteWith[Unit]()(Adapter.CTRL)
  }
}

trait Opaques { self: SAIOps with BasicDefs =>
  trait NativeFun

  object NativeExternalFun {
    private val used = MutableSet[String]()
    def apply(f: String, ret: Option[LLVMType] = None): Rep[NativeFun] = {
      if (!used.contains(f)) {
        System.out.println(s"Use native function $f.")
        used.add(f)
      }
      "llsc-native-external-wrapper".reflectWith[NativeFun](f, ret)
    }
    def unapply(v: Rep[Value]): Option[(String, Option[LLVMType])] = Unwrap(v) match {
      case gNode("llsc-native-external-wrapper", bConst(f: String)::bConst(ret: Option[LLVMType])::Nil) => Some((f, ret))
      case _ => None
    }
  }

  implicit class NativeFunOps(v: Rep[NativeFun]) {
    def apply[A: Manifest](args: List[Rep[Any]]): Rep[A] = v match {
      case NativeExternalFun(f, ty) => f.reflectWriteWith[A](args:_*)(Adapter.CTRL)
    }
  }

  object ExternalFun {
    import scala.collection.immutable.{Set => ImmSet}
    private val warned = MutableSet[String]()
    private val used = MutableSet[String]()
    private val modeled = ImmSet[String](
      "sym_print", "print_string", "malloc", "realloc",
      "llsc_assert", "llsc_assert_eager", "__assert_fail", "sym_exit",
      "make_symbolic", "make_symbolic_whole",
      "stop", "syscall", "llsc_assume",
      "__errno_location", "_exit", "exit", "abort", "calloc",
      "llsc_is_symbolic", "llsc_get_valuel", "getpagesize", "memalign", "reallocarray",
      "llsc_prefer_cex", "llsc_posix_prefer_cex", "llsc_warning_once"
    )
    private val syscalls = ImmSet[String](
      "open", "close", "read", "write", "lseek", "stat", "mkdir", "rmdir", "creat", "unlink", "chmod", "chown",
      "lseek64", "lstat", "fstat", "statfs", "ioctl", "fcntl"
    )
    val shouldRedirect = ImmSet[String]("@memcpy", "@memset", "@memmove")
    private val unsafeExternals = ImmSet[String]("fork", "exec", "error", "raise", "kill", "free", "vprintf")

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

  // TODO: name crash, refactor/remove dependencies of SMTBaseOps in SAIOps
  def ITE(cnd: Rep[Value], thn: Rep[Value], els: Rep[Value]): Rep[Value] =
    "ite".reflectWith[Value](cnd, thn, els)

  object MustConc {
    def unapply(v: Rep[Value]): Boolean = Unwrap(v) match {
      case IntV(_, _) => true
      case _ => false
    }
  }

  object MustSym {
    def unapply(v: Rep[Value]): Boolean = Unwrap(v) match {
      case SymV(_, _) => true
      case _ => false
    }
  }

  object IntV {
    def apply(i: Long): Rep[IntV] = IntV(unit(i), DEFAULT_INT_BW)
    def apply(i: Long, bw: Int): Rep[IntV] = IntV(unit(i), bw)
    def apply(i: Rep[Long]): Rep[IntV] = IntV(i, DEFAULT_INT_BW)
    def apply(i: Rep[Long], bw: Int): Rep[IntV] = "make_IntV".reflectMutableWith[IntV](i, bw)
    def unapply(v: Rep[Value]): Option[(Long, Int)] = Unwrap(v) match {
      case gNode("make_IntV", bConst(v: Long)::bConst(bw: Int)::_) => Some((v, bw))
      case _ => None
    }
  }

  object FloatV {
    def apply(f: Rep[Double]): Rep[FloatV] = apply(f, 32)
    def apply(f: Rep[Double], bw: Int): Rep[FloatV] = "make_FloatV".reflectWriteWith[FloatV](f, bw)(Adapter.CTRL)
    def apply(v: String, bw: Int): Rep[FloatV] = {
      require(bw == 80)
      "make_FloatV".reflectWriteWith[FloatV](v, bw)(Adapter.CTRL)
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
    def apply(l: Rep[Addr], kind: Rep[Kind], size: Rep[Long], off: Rep[Long] = 0L): Rep[LocV] =
      "make_LocV".reflectMutableWith[LocV](l, kind, size, off)
    def unapply(v: Rep[Value]): Option[(Rep[Addr], Rep[Kind], Rep[Long], Rep[Long])] = Unwrap(v) match {
      case gNode("make_LocV", (a: bExp)::(k: bExp)::(size: bExp)::(off: bExp)::_) =>
        Some((Wrap[Addr](a), Wrap[Kind](k), Wrap[Long](size), Wrap[Long](off)))
      case _ => None
    }
  }

  object SymLocV {
    def apply(l: Rep[Addr], kind: Rep[LocV.Kind], size: Rep[Long], off: Rep[Value]): Rep[SymLocV] =
      "make_SymLocV".reflectMutableWith[SymLocV](l, kind, size, off)
    def unapply(v: Rep[Value]): Option[(Rep[Addr], Rep[LocV.Kind], Rep[Long], Rep[Value])] = Unwrap(v) match {
      case gNode("make_SymLocV", (a: bExp)::(k: bExp)::(size: bExp)::(off: bSym)::_) =>
        Some((Wrap[Addr](a), Wrap[LocV.Kind](k), Wrap[Long](size), Wrap[Value](off)))
      case _ => None
    }
  }

  object FunV {
    def apply[W[_]](f: Rep[(W[SS], List[Value]) => List[(SS, Value)]])(implicit m: Manifest[W[SS]]): Rep[FunV] =
      "make_FunV".reflectMutableWith[FunV](f)
    def unapply[W[_]](v: Rep[Value])(implicit m: Manifest[W[SS]]): Option[Rep[(W[SS], List[Value]) => List[(SS, Value)]]] =
      Unwrap(v) match {
        case gNode("make_FunV", (f: bExp)::Nil) =>
          Some(Wrap[(W[SS], List[Value]) => List[(SS, Value)]](f))
        case _ => None
      }
  }

  object CPSFunV {
    def apply[W[_]](f: Rep[(W[SS], List[Value], PCont[W]) => Unit])(implicit m: Manifest[W[SS]]): Rep[CPSFunV] =
      "make_CPSFunV".reflectMutableWith[CPSFunV](f)
    def unapply[W[_]](v: Rep[Value])(implicit m: Manifest[W[SS]]): Option[Rep[(W[SS], List[Value], PCont[W]) => Unit]] =
      Unwrap(v) match {
        case gNode("make_CPSFunV", (f: bExp)::Nil) =>
          Some(Wrap[(W[SS], List[Value], PCont[W]) => Unit](f))
        case _ => None
      }
  }

  object SymV {
    def apply(s: String): Rep[SymV] = apply(s, DEFAULT_INT_BW)
    def apply(s: String, bw: Int): Rep[SymV] =
      "make_SymV".reflectWriteWith[SymV](unit(s), bw)(Adapter.CTRL)  //XXX: reflectMutable?
    def makeSymVList(i: Int, pfx: String = "x", b: Int = 0): Rep[List[Value]] =
      List[SymV](Range(b, b+i).map(x => apply(pfx + x.toString)):_*)
    def unapply(v: Rep[Value]): Option[(String, Int)] = Unwrap(v) match {
      case gNode("make_SymV", bConst(x: String)::bConst(bw: Int)::_) => Some((x, bw))
      case _ => None
    }
    def fromBool(b: Rep[Boolean]): Rep[SymV] =
      "sym_bool_const".reflectWith[SymV](b)
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

  object IntOp1 {
    def neg(v: Rep[Value]): Rep[Value] = "int_op_1".reflectWith[Value]("neg", v)
    def bvnot(v: Rep[Value]): Rep[Value] = "int_op_1".reflectWith[Value]("bvnot", v)
  }

  object IntOp2 {
    def applyNoOpt(op: String, o1: Rep[Value], o2: Rep[Value]): Rep[Value] =
      "int_op_2".reflectWith[Value](op, o1, o2)
    def apply(op: String, o1: Rep[Value], o2: Rep[Value]): Rep[Value] =
      if (!Config.opt) applyNoOpt(op, o1, o2)
      else op match {
        case "neq" => neq(o1, o2)
        case "eq" => eq(o1, o2)
        case "add" => add(o1, o2)
        case _ => applyNoOpt(op, o1, o2)
      }

    def unapply(v: Rep[Value]): Option[(String, Rep[Value], Rep[Value])] = Unwrap(v) match {
      case gNode("int_op_2", bConst(x: String)::(o1: bSym)::(o2: bSym)::_) =>
        Some((x, Wrap[Value](o1), Wrap[Value](o2)))
      case _ => None
    }

    def add(v1: Rep[Value], v2: Rep[Value]): Rep[Value] = (v1, v2) match {
      case (IntV(n1, bw1), IntV(n2, bw2)) if (bw1 == bw2) => IntV(n1 + n2, bw1)
      case _ => applyNoOpt("add", v1, v2)
    }

    def mul(v1: Rep[Value], v2: Rep[Value]): Rep[Value] = (v1, v2) match {
      case (IntV(n1, bw1), IntV(n2, bw2)) if (bw1 == bw2) => IntV(n1 * n2, bw1)
      case _ => applyNoOpt("mul", v1, v2)
    }

    def neq(o1: Rep[Value], o2: Rep[Value]): Rep[Value] = (Unwrap(o1), Unwrap(o2)) match {
      case (gNode("bv_sext", (e1: bExp)::bConst(bw1: Int)::_),
            gNode("bv_sext", (e2: bExp)::bConst(bw2: Int)::_)) if bw1 == bw2 =>
        val v1 = Wrap[Value](e1)
        val v2 = Wrap[Value](e2)
        if (v1.bw == v2.bw) applyNoOpt("neq", v1, v2)
        else applyNoOpt("neq", o1, o2)
      case _ => applyNoOpt("neq", o1, o2)
    }
    def eq(o1: Rep[Value], o2: Rep[Value]): Rep[Value] = (Unwrap(o1), Unwrap(o2)) match {
      case (gNode("bv_sext", (e1: bExp)::bConst(bw1: Int)::_),
            gNode("bv_sext", (e2: bExp)::bConst(bw2: Int)::_)) if bw1 == bw2 =>
        val v1 = Wrap[Value](e1)
        val v2 = Wrap[Value](e2)
        if (v1.bw == v2.bw) applyNoOpt("eq", v1, v2)
        else applyNoOpt("eq", o1, o2)
      case _ => applyNoOpt("eq", o1, o2)
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

    def defaultRetVal(ty: Option[LLVMType]): Rep[Value] = ty match {
      case Some(IntType(size)) => IntV(0, size)
      case Some(PtrType(_, _)) => IntV(0, ARCH_WORD_SIZE)
      case _ => IntV(0, 32)
    }

    def apply[W[_]](s: Rep[W[SS]], args: Rep[List[Value]])(implicit m: Manifest[W[SS]]): Rep[List[(SS, Value)]] =
      v match {
        case ExternalFun(f, ty) =>
          if (f == "noop" && Config.opt) List((s.asRepOf[SS], defaultRetVal(ty)))
          else f.reflectWith[List[(SS, Value)]](s, args)
        case FunV(f) => f(s, args)
        case _ => "direct_apply".reflectWith[List[(SS, Value)]](v, s, args)
      }

    // The CPS version
    // W[_] is parameterized over pass-by-value (Id) or pass-by-ref (Ref) of SS
    def apply[W[_]](s: Rep[W[SS]], args: Rep[List[Value]], k: Rep[PCont[W]])(implicit m: Manifest[W[SS]]): Rep[Unit] =
      v match {
        case ExternalFun(f, ty) =>
          if (f == "noop" && Config.opt) k(s, defaultRetVal(ty))
          else f.reflectWith[Unit](s, args, k)
        case CPSFunV(f) => f(s, args, k)                       // direct call
        case _ => "cps_apply".reflectWith[Unit](v, s, args, k) // indirect call
      }

    def deref: Rep[Any] = "ValPtr-deref".reflectUnsafeWith[Any](v)

    val foldableOp = StaticSet[String]("make_SymV", "make_IntV", "bv_sext", "bv_zext")

    def sExt(bw: Int): Rep[Value] = Unwrap(v) match {
      case gNode(s, (v1: bExp)::bConst(bw1: Int)::_) if (foldableOp(s) && (bw1 == bw)) => v
      case gNode("make_IntV", (v1: bExp)::bConst(bw1: Int)::_) if bw > bw1 =>
        // sExt(IntV(n, bw1), bw) ⇒ IntV(n, bw)
        IntV(Wrap[Long](v1), bw)
      case gNode("bv_sext", (v1: bExp)::bConst(bw1: Int)::_) if bw > bw1=>
        // sExt(sExt(n, bw1), bw) ⇒ sExt(n, bw)
        Wrap[Value](v1).sExt(bw)
      case _ => "bv_sext".reflectWith[Value](v, bw)
    }

    def zExt(bw: Int): Rep[Value] = Unwrap(v) match {
      case gNode(s, (v1: bExp)::bConst(bw1: Int)::_) if (foldableOp(s) && (bw1 == bw)) => v
      case gNode("make_IntV", (v1: bExp)::bConst(bw1: Int)::_) if bw > bw1 =>
        // zExt(IntV(n, bw1), bw) ⇒ IntV(n, bw)
        IntV(Wrap[Long](v1), bw)
      case gNode("bv_zext", (v1: bExp)::bConst(bw1: Int)::_) if bw > bw1=>
        // zExt(sExt(n, bw1), bw) ⇒ zExt(n, bw)
        Wrap[Value](v1).zExt(bw)
      case _ => "bv_zext".reflectWith[Value](v, bw)
    }

    def isConc: Rep[Boolean] = v match {
      case MustConc() if Config.opt => unit(true)
      case MustSym() if Config.opt => unit(false)
      case _ => "is-conc".reflectWith[Boolean](v)
    }

    def notNull: Rep[Boolean] = "not-null".reflectWith[Boolean](v)
    def fromFloatToUInt(toSize: Int): Rep[Value] = "fp_toui".reflectWith[Value](v, toSize)
    def fromFloatToSInt(toSize: Int): Rep[Value] = "fp_tosi".reflectWith[Value](v, toSize)
    def fromUIntToFloat: Rep[Value] = "ui_tofp".reflectWith[Value](v)
    def fromSIntToFloat: Rep[Value] = "si_tofp".reflectWith[Value](v)
    def trunc(from: Int, to: Int): Rep[Value] = "trunc".reflectWith[Value](v, from, to)

    def +(rhs: Rep[Value]): Rep[Value] = IntOp2.add(v, rhs)
    def *(rhs: Rep[Value]): Rep[Value] = IntOp2.mul(v, rhs)
    def &(rhs: Rep[Value]): Rep[Value] = IntOp2("and", v, rhs)
    def |(rhs: Rep[Value]): Rep[Value] = IntOp2("or", v, rhs)
    def unary_! : Rep[Value] = IntOp1.neg(v)
    def unary_~ : Rep[Value] = IntOp1.bvnot(v)

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

  implicit class LocVOps(v: Rep[LocV]) {
    def +(off: Rep[Value]): Rep[Value] = (v, off) match {
      // Todo: Add case for IntV non-const
      case (LocV(a, k, s, o), IntV(n, _)) => LocV(a, k, s, o + n)
      case _ => "ptroff".reflectWith[Value](v, off)
    }
  }

}
