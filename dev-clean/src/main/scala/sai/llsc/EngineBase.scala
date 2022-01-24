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

  /* Basic functionalities */

  def compile(funName: String, b: BB): Unit = {
    Predef.assert(!BBFuns.contains((funName, b)))
    val (f, n) = repBlockFun(funName, b)
    val realFunName = if (funName != "@main") funName.tail else "llsc_main"
    blockNameMap(n) = s"${realFunName}_Block$n"
    BBFuns((funName, b)) = f
  }
  def compile(f: FunctionDef): Unit = {
    Predef.assert(!FunFuns.contains(f.id))
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
  var funcEnv: StaticList[(Addr, String)] = StaticList()

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
      (rawSize + BYTE_SIZE - 1) / BYTE_SIZE
    }
    case PackedStruct(types) =>
      types.map(getTySize(_, align)).sum
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
  def evalHeapConst(v: Constant, ty: LLVMType): List[Rep[Value]] = {
    def evalAddr(v: Constant, ty: LLVMType): Rep[Addr] = v match {
      case GetElemPtrExpr(inBounds, baseType, ptrType, const, typedConsts) => {
        val indexLLVMValue = typedConsts.map(tv => tv.const.asInstanceOf[IntConst].n)
        val addr = evalAddr(const, ptrType)
        addr + calculateOffsetStatic(ptrType, indexLLVMValue)
      }
      case GlobalId(id) => heapEnv(id)
      case BitCastExpr(from, const, to) => evalAddr(const, to)
    }
    def evalValue(v: Constant, ty: LLVMType): Rep[Value] = v match {
      case BoolConst(b) => IntV(if (b) 1 else 0, 1)
      case IntConst(n) => IntV(n, ty.asInstanceOf[IntType].size)
      case FloatConst(f) => FloatV(f)
      case NullConst => NullV()
      case PtrToIntExpr(from, const, to) => 
        IntV(evalAddr(const, from), to.asInstanceOf[IntType].size)
      case _ => LocV(evalAddr(v, ty), LocV.kHeap)
    }
    v match {
      case StructConst(cs) =>
        cs.flatMap { case c => evalHeapConst(c.const, c.ty) }
      case ArrayConst(cs) =>
        cs.flatMap { case c => evalHeapConst(c.const, c.ty) }
      case CharArrayConst(s) =>
        s.map(c => IntV(c.toInt, 8)).toList ++ StaticList.fill(getTySize(ty) - s.length)(NullV())
      case ZeroInitializerConst => ty match {
        case ArrayType(size, ety) => StaticList.fill(size)(evalHeapConst(ZeroInitializerConst, ety)).flatten
        case Struct(types) => types.flatMap(evalHeapConst(ZeroInitializerConst, _))
        // TODO: fallback case is not typed
        case _ => IntV(0, getTySize(ty)) :: StaticList.fill(getTySize(ty) - 1)(NullV())
      }
      case _ => evalValue(v, ty) :: StaticList.fill(getTySize(ty) - 1)(NullV())
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
      module.funcDefMap.foreach { case (k, v) =>
        if (k != "@main")  {
          heapEnv += k -> unit(heapSize)
          funcEnv = (heapSize, k) :: funcEnv
          heapSize += 8;
        }
      }
      heapTmp ++= StaticList.fill(heapSize)(NullV())
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
