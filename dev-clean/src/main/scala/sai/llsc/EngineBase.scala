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

  /* Abstract definitions */

  val m: Module
  type BFTy // Block-function type
  type FFTy // Function-function type

  def repBlockFun(funName: String, b: BB): (BFTy, Int)
  def repFunFun(f: FunctionDef): (FFTy, Int)
  def wrapFunV(f: FFTy): Rep[Value]

  /* Basic functionalities */

  def compile(funName: String, b: BB): Unit = {
    if (BBFuns.contains((funName, b))) {
      System.out.println(s"Warning: ignoring the compilation of $funName - ${b.label}")
      return
    }
    val (fn, n) = repBlockFun(funName, b)
    val realFunName = if (funName != "@main") funName.tail else "llsc_main"
    blockNameMap(n) = s"${realFunName}_Block$n"
    BBFuns((funName, b)) = fn
  }
  def compile(f: FunctionDef): Unit = {
    if (FunFuns.contains(f.id)) {
      System.out.println(s"Warning: ignoring the recompilation of ${f.id}")
      return
    }
    val (fn, n) = repFunFun(f)
    funNameMap(n) = if (f.id != "@main") f.id.tail else "llsc_main"
    FunFuns(f.id) = fn
  }
  def compile(funs: List[FunctionDef]): Unit = funs.foreach(compile)

  def funMap: StaticMap[String, FunctionDef] = m.funcDefMap
  def funDeclMap: StaticMap[String, FunctionDecl] = m.funcDeclMap
  def globalDefMap: StaticMap[String, GlobalDef] = m.globalDefMap
  def globalDeclMap: StaticMap[String, GlobalDecl] = m.globalDeclMap
  def typeDefMap: StaticMap[String, LLVMType] = m.typeDefMap

  var heapEnv: StaticMap[String, Rep[Addr]] = StaticMap()

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

  object StructCalc {
    private def padding(size: Int, align: Int): Int =
      size + (align - size % align) % align
    
    private def offset(types: List[LLVMType]): (Int, Int) =
      types.foldLeft((0, 0)) { case ((size, align), ty) =>
        val (sz, al) = getTySizeAlign(ty)
        (padding(size, al) + sz, al max align)
      }
    
    def getSizeAlign(types: List[LLVMType]): (Int, Int) = {
      val (size, align) = offset(types)
      (padding(size, align), align)
    }

    def getFieldOffset(types: List[LLVMType], idx: Int): Int =
      offset(types.take(idx))._1
  }

  def getTySizeAlign(vt: LLVMType): (Int, Int) = vt match {
    case ArrayType(num, ety) =>
      val (size, align) = getTySizeAlign(ety)
      (num * size, align)
    case Struct(types) =>
      StructCalc.getSizeAlign(types)
    case NamedType(id) =>
      getTySizeAlign(typeDefMap(id))
    case IntType(size) =>
      val elemSize = (size + BYTE_SIZE - 1) / BYTE_SIZE
      (elemSize, elemSize)
    case PtrType(ty, addrSpace) =>
      val elemSize = ARCH_WORD_SIZE / BYTE_SIZE
      (elemSize, elemSize)
    case FloatType(fk) => {
      val rawSize = fk match {
        case FK_Half => 16
        case FK_BFloat => 16
        case FK_Float => 32
        case FK_Double => 64
        case FK_X86_FP80 => 80
        case FK_FP128 => 128
        case FK_PPC_FP1289 => 128
      }
      val elemSize = (rawSize + BYTE_SIZE - 1) / BYTE_SIZE
      (elemSize, elemSize)
    }
    case PackedStruct(types) =>
      (types.map(getTySizeAlign(_)._1).sum, 1)
    case _ => ???
  }

  def getTySize(vt: LLVMType): Int = getTySizeAlign(vt)._1

  def calculateOffsetStatic(ty: LLVMType, index: List[Long]): Int = {
    implicit def longToInt(x: Long) = x.toInt
    if (index.isEmpty) 0 else ty match {
      case Struct(types) =>
        val prev: Int = StructCalc.getFieldOffset(types, index.head)
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
  // TODO: refactor
  //     1. using flat recursion
  //     2. code reuse with Engine-level eval?
  def evalHeapConst(v: Constant, ty: LLVMType): List[Rep[Value]] = {
    def evalAddr(v: Constant, ty: LLVMType): Rep[Addr] = v match {
      case GetElemPtrExpr(inBounds, baseType, ptrType, const, typedConsts) => {
        val indexLLVMValue = typedConsts.map(tv => tv.const.asInstanceOf[IntConst].n)
        val addr = evalAddr(const, getRealType(ptrType))
        addr + calculateOffsetStatic(ptrType, indexLLVMValue)
      }
      case GlobalId(id) => heapEnv(id)
    }
    def evalValue(v: Constant, ty: LLVMType): Rep[Value] = v match {
      case BoolConst(b) => IntV(if (b) 1 else 0, 1)
      case IntConst(n) => IntV(n, ty.asInstanceOf[IntType].size)
      case FloatConst(f) => FloatV(f)
      case NullConst => NullV()
      case PtrToIntExpr(from, const, to) =>
        IntV(evalAddr(const, from), to.asInstanceOf[IntType].size)
      case GlobalId(id) if funMap.contains(id) =>
        if (!FunFuns.contains(id)) compile(funMap(id))
        wrapFunV(FunFuns(id)) //XXX: padding?
      case BitCastExpr(from, const, to) => evalValue(const, to)
      case _ => LocV(evalAddr(v, ty), LocV.kHeap)
    }
    val real_ty = getRealType(ty)
    v match {
      case StructConst(cs) =>
        cs.flatMap { case c => evalHeapConst(c.const, c.ty) }
      case ArrayConst(cs) =>
        cs.flatMap { case c => evalHeapConst(c.const, c.ty) }
      case CharArrayConst(s) =>
        s.map(c => IntV(c.toInt, 8)).toList ++ StaticList.fill(getTySize(real_ty) - s.length)(NullV())
      case ZeroInitializerConst => real_ty match {
        case ArrayType(size, ety) => StaticList.fill(size)(evalHeapConst(ZeroInitializerConst, ety)).flatten
        case Struct(types) => types.flatMap(evalHeapConst(ZeroInitializerConst, _))
        // TODO: fallback case is not typed
        case _ => IntV(0, 8 * getTySize(real_ty)) :: StaticList.fill(getTySize(real_ty) - 1)(NullV())
      }
      case _ => evalValue(v, real_ty) :: StaticList.fill(getTySize(real_ty) - 1)(NullV())
    }
  }

  def precompileHeapLists(modules: StaticList[Module]): StaticList[Rep[Value]] = {
    var heapSize = 0;
    var heapTmp: StaticList[Rep[Value]] = StaticList()
    for (module <- modules) {
      // module.funcDeclMap.foreach { case (k, v) =>
      //   heapEnv += k -> unit(heapSize)
      //   heapSize += 8;
      // }
      // module.funcDefMap.foreach { case (k, v) =>
      //   if (k != "@main")  {
      //     heapEnv += k -> unit(heapSize)
      //     funcEnv = (heapSize, k) :: funcEnv
      //     heapSize += 8;
      //   }
      // }
      // heapTmp ++= StaticList.fill(heapSize)(NullV())
      module.globalDeclMap.foreach { case (k, v) =>
        val realname = module.mname + "_" + v.id
        heapEnv += realname -> unit(heapSize);
        heapSize += getTySize(v.typ)
        heapTmp ++= evalHeapConst(ZeroInitializerConst, v.typ)
      }
      module.globalDefMap.foreach { case (k, v) =>
        heapEnv += k -> unit(heapSize);
        heapSize += getTySize(v.typ)
      }
      module.globalDefMap.foreach { case (k, v) =>
        heapTmp ++= evalHeapConst(v.const, getRealType(v.typ))
      }
    }
    heapTmp
  }
}
