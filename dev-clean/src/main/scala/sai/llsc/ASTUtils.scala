package sai.llsc

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._

object ASTUtils {
  def flattenTypedList(xs: List[TypedConst]) = xs.map(c => flattenAS(c.const)).flatten

  def flattenAS(cst: Constant): List[Constant] = cst match {
    case ArrayConst(xs) =>
      flattenTypedList(xs)
    case StructConst(xs) =>
      flattenTypedList(xs)
    case _ => List(cst)
  }

  def flattenTy(ty: LLVMType): List[LLVMType] = ty match {
    case Struct(types) => 
      types.flatMap(flattenTy(_))
    case ArrayType(size, ety) => 
      List.fill(size)(flattenTy(ety)).flatten
    case _ => List(ty)
  }
}

object Constants {
  final val BYTE_SIZE: Int = 8
  final val DEFAULT_INT_BW: Int = BYTE_SIZE * 4
  final val ARCH_WORD_SIZE: Int = 64
}

class CompileTimeRuntime[Addr, BFTy, FFTy]() {
  import collection.mutable.HashMap
  import Constants._

  var funMap: Map[String, FunctionDef] = Map()
  var funDeclMap: Map[String, FunctionDecl] = Map()
  var globalDefMap: Map[String, GlobalDef] = Map()
  var globalDeclMap: Map[String, GlobalDecl] = Map()
  var typeDefMap: Map[String, LLVMType] = Map()
  var heapEnv: Map[String, Addr] = Map()

  def reset(main: Module): Unit = {
    funMap = main.funcDefMap
    funDeclMap = main.funcDeclMap
    globalDefMap = main.globalDefMap
    globalDeclMap = main.globalDeclMap
    typeDefMap = main.typeDefMap
    heapEnv = Map[String, Addr]()
  }

  val funNameMap: HashMap[Int, String] = new HashMap()
  val blockNameMap: HashMap[Int, String] = new HashMap()

  val BBFuns: HashMap[(String, BB), BFTy] = new HashMap[(String, BB), BFTy]
  val FunFuns: HashMap[String, FFTy] = new HashMap[String, FFTy]

  def findBlock(funName: String, lab: String): Option[BB] = funMap.get(funName).get.lookupBlock(lab)
  def findFirstBlock(funName: String): BB = findFundef(funName).body.blocks(0)
  def findFundef(funName: String) = funMap.get(funName).get

  def getRealType(vt: LLVMType): LLVMType = vt match {
    case NamedType(id) => typeDefMap(id)
    case _ => vt
  }

  def getTySize(vt: LLVMType, align: Int = 1): Int = vt match {
    case ArrayType(size, ety) =>
      val rawSize = size * getTySize(ety, align)
      if (rawSize % align == 0) rawSize
      else (rawSize / align + 1) * align
    case Struct(types) =>
      types.map(getTySize(_, align)).sum
    case NamedType(id) =>
      getTySize(typeDefMap(id), align)
    case IntType(size) =>
      (size + BYTE_SIZE - 1) / BYTE_SIZE
    case PtrType(ty, addrSpace) =>
      ARCH_WORD_SIZE / BYTE_SIZE
    case _ => ???
  }

  def calculateOffsetStatic(ty: LLVMType, index: List[Long]): Int = {
    implicit def longToInt(x: Long) = x.toInt
    if (index.isEmpty) 0 else ty match {
      case Struct(types) =>
        val prev: Int = Range(0, index.head).foldLeft(0)((sum, i) => getTySize(types(i)) + sum)
        prev + calculateOffsetStatic(types(index.head), index.tail)
      case ArrayType(size, ety) =>
        index.head * getTySize(ety) + calculateOffsetStatic(ety, index.tail)
      case NamedType(id) =>
        calculateOffsetStatic(typeDefMap(id), index)
      case PtrType(ety, addrSpace) =>
        index.head * getTySize(ety) + calculateOffsetStatic(ety, index.tail)
      case _ => ???
    }
  }
}
