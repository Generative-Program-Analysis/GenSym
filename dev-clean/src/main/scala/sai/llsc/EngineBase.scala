package sai.llsc

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._
import sai.llsc.ASTUtils._

import scala.collection.JavaConverters._

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._
import sai.lmsx.smt.SMTBool
import scala.collection.immutable.{List => StaticList, Map => StaticMap}

trait EngineBase extends SAIOps { self: BasicDefs with ValueDefs =>
  import collection.mutable.HashMap
  import Constants._

  val m: Module
  type BFTy // Block-function type
  type FFTy // Function-function type
  def compile(funName: String, block: BB): Unit
  def compile(f: FunctionDef): Unit
  def compile(funs: List[FunctionDef]): Unit = funs.foreach(compile)

  def funMap: StaticMap[String, FunctionDef] = m.funcDefMap
  def funDeclMap: StaticMap[String, FunctionDecl] = m.funcDeclMap
  def globalDefMap: StaticMap[String, GlobalDef] = m.globalDefMap
  def globalDeclMap: StaticMap[String, GlobalDecl] = m.globalDeclMap
  def typeDefMap: StaticMap[String, LLVMType] = m.typeDefMap

  var heapEnv: StaticMap[String, Rep[Addr]] = StaticMap()

  val funNameMap: HashMap[Int, String] = new HashMap()
  val blockNameMap: HashMap[Int, String] = new HashMap()

  val BBFuns: HashMap[(String, BB), BFTy] = new HashMap[(String, BB), BFTy]
  val FunFuns: HashMap[String, FFTy] = new HashMap[String, FFTy]

  def getBBFun(funName: String, blockLab: String): BFTy =
    getBBFun(funName, findBlock(funName, blockLab).get)

  def getBBFun(funName: String, b: BB): BFTy = {
    if (!BBFuns.contains((funName, b))) compile(funName, b)
    BBFuns((funName, b))
  }

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

  def calculateOffset(ty: LLVMType, index: List[Rep[Int]]): Rep[Int] = {
    if (index.isEmpty) 0 else ty match {
      case PtrType(ety, addrSpace) =>
        index.head * getTySize(ety) + calculateOffset(ety, index.tail)
      case ArrayType(size, ety) =>
        index.head * getTySize(ety) + calculateOffset(ety, index.tail)
      case NamedType(id) =>
        calculateOffset(typeDefMap(id), index)
      case Struct(types) =>
        // https://llvm.org/docs/LangRef.html#getelementptr-instruction
        // "When indexing into a (optionally packed) structure, only i32 integer
        //  constants are allowed"
        // TODO: the align argument for getTySize
        // TODO: test this
        val indexCst: List[Long] = index.map { case Wrap(Backend.Const(n: Int)) => n.toLong }
        unit(calculateOffsetStatic(ty, indexCst))
      case _ => ???
    }
  }

  // FIXME: Alignment: CharArrayConst, ArrayConst
  // Float Type
  def evalHeapConst(v: Constant, ty: LLVMType): List[Rep[Value]] = v match {
    case BoolConst(b) =>
      StaticList(IntV(if (b) 1 else 0, 1))
    case IntConst(n) =>
      StaticList(IntV(n, ty.asInstanceOf[IntType].size)) ++ StaticList.fill(getTySize(ty) - 1)(NullV())
    case FloatConst(f) =>
      StaticList(FloatV(f))
    case ZeroInitializerConst => ty match {
      case ArrayType(size, ety) => StaticList.fill(flattenTy(ty).map(lty => getTySize(lty)).sum)(IntV(0))
      case Struct(types) => StaticList.fill(flattenTy(ty).map(lty => getTySize(lty)).sum)(IntV(0))
      case _ => StaticList.fill(getTySize(ty))(IntV(0))
    }
    case ArrayConst(cs) =>
      flattenAS(v).flatMap(c => evalHeapConst(c, flattenTy(ty).head))
    case CharArrayConst(s) =>
      s.map(c => IntV(c.toInt, 8)).toList ++ StaticList.fill(getTySize(ty) - s.length)(NullV())
    case StructConst(cs) =>
      cs.flatMap { case c => evalHeapConst(c.const, c.ty)}
    case NullConst => StaticList.fill(getTySize(ty))(NullV())
    case GetElemPtrExpr(inBounds, baseType, ptrType, const, typedConsts) => {
      val indexLLVMValue = typedConsts.map(tv => tv.const.asInstanceOf[IntConst].n)
      val id = const match { // is this one exclusive?
        case GlobalId(id) => id
        case BitCastExpr(from, const, to) => const.asInstanceOf[GlobalId].id
      }
      LocV(heapEnv(id) + calculateOffsetStatic(ptrType, indexLLVMValue), LocV.kHeap) :: StaticList.fill(getTySize(ty) - 1)(NullV())
    }
    case GlobalId(id) =>
      LocV(heapEnv(id), LocV.kHeap) :: StaticList.fill(getTySize(ty) - 1)(NullV())
    case BitCastExpr(from, const, to) =>
      evalHeapConst(const, to)
  }


  def precomputeHeapAddr(globalDefMap: StaticMap[String, GlobalDef], prevSize: Int): Unit = {
    var addr: Int = prevSize
    globalDefMap.foreach { case (k, v) =>
      heapEnv = heapEnv + (k -> unit(addr))
      addr = addr + getTySize(v.typ)
    }
  }

  def precompileHeap(globalDefMap: StaticMap[String, GlobalDef], prevSize: Int): StaticList[Rep[Value]] = {
    precomputeHeapAddr(globalDefMap, prevSize)
    globalDefMap.foldLeft(StaticList[Rep[Value]]()) { case (h, (k, v)) =>
      h ++ evalHeapConst(v.const, getRealType(v.typ))
    }
  }

  def precompileHeapDecl(globalDeclMap: StaticMap[String, GlobalDecl],
    prevSize: Int, mname: String): StaticList[Rep[Value]] =
    globalDeclMap.foldRight(StaticList[Rep[Value]]()) { case ((k, v), h) =>
      // TODO external_weak linkage
      val realID = mname + "_" + v.id
      val addr = h.size + prevSize
      heapEnv = heapEnv + (realID -> unit(addr))
      h ++ StaticList.fill(getTySize(v.typ))(IntV(0))
    }

  def precompileHeapLists(modules: StaticList[Module]): StaticList[Rep[Value]] = {
    var heapSize = 0;
    var heapTmp: StaticList[Rep[Value]] = StaticList()
    for (module <- modules) {
      heapTmp = heapTmp ++ precompileHeap(module.globalDefMap, heapTmp.size)
      if (!module.globalDeclMap.isEmpty) {
        heapTmp = heapTmp ++ precompileHeapDecl(module.globalDeclMap, heapTmp.size, module.mname)
      }
    }
    heapTmp
  }
}
