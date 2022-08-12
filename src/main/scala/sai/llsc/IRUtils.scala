package sai.llsc

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._

import sai.lmsx._

object Constants {
  final val BYTE_SIZE: Int = 8
  final val DEFAULT_INT_BW: Int = BYTE_SIZE * 4
  final val DEFAULT_ADDR_BW: Int = BYTE_SIZE * 8
  final val DEFAULT_INDEX_BW: Int = BYTE_SIZE * 8
  final val ARCH_WORD_SIZE: Int = 64
}

object IRUtils {
  import Constants._

  // For functions with Variable Arguments, we need to generate different code for different call-sites and argument types.
  def getMangledFunctionName(f: FunctionDecl, argTypes: List[LLVMType]): String = {
    val hasVararg = f.header.params.contains(Vararg)
    val mangledName = if (!hasVararg) f.id else f.id + argTypes.map("_"+_.prettyName).mkString("")
    mangledName.replaceAllLiterally(".", "_")
  }

  def extractValues(args: List[Arg]): List[LLVMValue] = args.map {
    case TypedArg(ty, attrs, value) => value
  }

  def extractTypes(args: List[Arg]): List[LLVMType] = args.map {
    case TypedArg(ty, attrs, value) => ty
  }

  implicit class ConstantOps(cst: Constant) {
    def flatten: List[Constant] = cst match {
      case ArrayConst(xs) => xs.map(_.const.flatten).flatten
      case StructConst(xs) => xs.map(_.const.flatten).flatten
      case _ => List(cst)
    }
  }

  implicit class LLVMTypeOps(t: LLVMType) {
    // Note: `m` is needed only when t is a NamedType
    def size(implicit m: Module): Int = t.sizeAlign._1

    def sizeAlign(implicit m: Module): (Int, Int) = t match {
      case ArrayType(num, ety) =>
        val (size, align) = ety.sizeAlign
        (num * size, align)
      case Struct(types) =>
        StructCalc().getSizeAlign(types)
      case NamedType(id) =>
        m.typeDefMap(id).sizeAlign
      case IntType(size) =>
        val elemSize = (size + BYTE_SIZE - 1) / BYTE_SIZE
        (elemSize, elemSize)
      case PtrType(ty, addrSpace) =>
        val elemSize = ARCH_WORD_SIZE / BYTE_SIZE
        (elemSize, elemSize)
      case ft@FloatType(fk) =>
        import scala.math.{log, ceil, pow}
        val elemSize = (ft.size + BYTE_SIZE - 1) / BYTE_SIZE
        val align = pow(2, ceil(log(elemSize)/log(2)))
        (elemSize, align.toInt)
      case PackedStruct(types) =>
        PackedStructCalc().getSizeAlign(types)
      case _ =>
        throw new Exception(s"type $t is not handled by t.sizeAlign")
    }

    def flatten: List[LLVMType] = t match {
      case Struct(types) =>
        types.flatMap(_.flatten)
      case ArrayType(size, ety) =>
        List.fill(size)(ety.flatten).flatten
      case _ => List(t)
    }

    def toManifest: Manifest[_] = t match {
      case IntType(1) => manifest[Boolean]
      case IntType(8) => manifest[Char]
      case IntType(16) => manifest[Short]
      case IntType(32) => manifest[Int]
      case IntType(64) => manifest[Long]
      case FloatType(FK_Float) => manifest[Float]
      case FloatType(FK_Double) => manifest[Double]
      case PtrType(IntType(1), addrSpace) => manifest[Ptr[Boolean]]
      case PtrType(IntType(8), addrSpace) => manifest[Ptr[Char]]
      case PtrType(IntType(16), addrSpace) => manifest[Ptr[Short]]
      case PtrType(IntType(32), addrSpace) => manifest[Ptr[Int]]
      case PtrType(IntType(64), addrSpace) => manifest[Ptr[Long]]
      case _ => ???
    }

    def prettyName: String = t match {
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

  case class StructCalc(implicit val m: Module) {
    private def padding(size: Int, align: Int): Int =
      (align - size % align) % align

    private def fields(types: List[LLVMType]): (Int, Int, Int) =
      types.foldLeft((0, 0, 0)) { case ((begin, end, maxalign), ty) =>
        val (size, align) = ty.sizeAlign
        val new_begin = end + padding(end, align)
        (new_begin, new_begin + size, align max maxalign)
      }

    def getSizeAlign(types: List[LLVMType]): (Int, Int) = {
      val (_, size, align) = fields(types)
      (size + padding(size, align), align)
    }

    def getFieldOffset(types: List[LLVMType], idx: Int): Int =
      fields(types.take(idx+1))._1

    def getFieldOffsetSize(types: List[LLVMType], idx: Int): (Int, Int) = {
      val (begin, end, align) = fields(types.take(idx+1))
      (begin, end - begin)
    }

    def concat[E, T](cs: List[E], fill: Int => List[T])(f: E => (List[T], Int)): (List[T], Int) = {
      val (list, align) = cs.foldLeft((List[T](), 0)) { case ((list, maxalign), c) =>
        val (value, align) = f(c)
        (list ++ fill(padding(list.size, align)) ++ value, align max maxalign)
      }
      (list ++ fill(padding(list.size, align)), align)
    }
  }

  case class PackedStructCalc(implicit val m: Module) {
    private def fields(types: List[LLVMType]): (Int, Int) =
      types.foldLeft((0, 0)) { case ((begin, end), ty) => (end, end + ty.size) }

    def getSizeAlign(types: List[LLVMType]): (Int, Int) = {
      val size = fields(types)._2
      (size, 1)
    }

    def getFieldOffset(types: List[LLVMType], idx: Int): Int =
      fields(types.take(idx+1))._1

    def concat[E, T](cs: List[E])(f: E => (List[T], Int)): (List[T], Int) = {
      val (list, align) = cs.foldLeft((List[T](), 0)) { case ((list, maxalign), c) =>
        val (value, align) = f(c)
        (list ++ value, 1)
      }
      (list, 1)
    }
  }

  def calculateOffsetStatic(ty: LLVMType, index: List[Long])(implicit m: Module): Long = {
    implicit def longToInt(x: Long) = x.toInt
    if (index.isEmpty) 0 else ty match {
      case Struct(types) =>
        val prev: Int = StructCalc().getFieldOffset(types, index.head)
        prev + calculateOffsetStatic(types(index.head), index.tail)
      case ArrayType(size, ety) =>
        index.head * ety.size + calculateOffsetStatic(ety, index.tail)
      case NamedType(id) =>
        calculateOffsetStatic(m.typeDefMap(id), index)
      case PtrType(ety, addrSpace) =>
        index.head * ety.size + calculateOffsetStatic(ety, index.tail)
      case PackedStruct(types) =>
        val prev: Int = PackedStructCalc().getFieldOffset(types, index.head)
        prev + calculateOffsetStatic(types(index.head), index.tail)
      case _ => ???
    }
  }

  def isAtomicConst(c: Constant): Boolean = c match {
    case BoolConst(_) | IntConst(_) | FloatConst(_) | FloatLitConst(_)
       | NullConst | PtrToIntExpr(_, _, _) | GlobalId(_)
       | BitCastExpr(_, _, _) | GetElemPtrExpr(_, _, _, _, _) =>
      true
    case _ => false
  }
}

case class CFG(funMap: Map[String, FunctionDef]) {
  import collection.mutable.HashMap
  import sai.structure.lattices.Lattices._

  type Fun = String
  type Label = String
  type Succs = Map[Label, Set[Label]]
  type Preds = Map[Label, Set[Label]]
  type Graph = (Succs, Preds)

  val mtGraph: Graph = Lattice[Graph].bot

  val funCFG: Map[Fun, Graph] = funMap.map({ case (f, d) => (f, construct(d.body.blocks)) }).toMap

  def succ(fname: String, label: Label): Set[Label] = funCFG(fname)._1(label)
  def pred(fname: String, label: Label): Set[Label] = funCFG(fname)._2(label)

  def construct(blocks: List[BB]): Graph = blocks.foldLeft(mtGraph) { case (g, b) =>
    val from: Label = b.label.get
    val to: Set[Label] = b.term match {
      case BrTerm(lab) => Set(lab)
      case CondBrTerm(ty, cnd, thnLab, elsLab) => Set(thnLab, elsLab)
      case SwitchTerm(cndTy, cndVal, default, table) => Set(default) ++ table.map(_.label).toSet
      case _ => Set()
    }
    g ⊔ (Map(from → to), to.map(_ → Set(from)).toMap)
  }

  def prettyPrint: Unit =
    funCFG.foreach { case (f, g) =>
      println(s"$f\n  successors:")
      g._1.foreach { case (from, to) =>
        val toStr = to.mkString(",")
        println(s"    $from → {$toStr}")
      }
      println("  predecessors:")
      g._2.foreach { case (to, from) =>
        val fromStr = from.mkString(",")
        println(s"    $to → {$fromStr}")
      }
    }
}
