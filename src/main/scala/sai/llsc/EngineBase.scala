package sai.llsc

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._
import sai.llsc.IRUtils._

import scala.collection.JavaConverters._

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._

case class Ctx(funName: String, blockLab: String) {
  override def toString: String = funName + "_" + blockLab
  def withVar(x: String): String = funName + "_" + x
  def withBlock(lab: String): String = funName + "_" + lab
}

trait EngineBase extends SAIOps { self: BasicDefs with ValueDefs =>
  import scala.collection.immutable.{List => StaticList, Map => StaticMap}
  import collection.mutable.{HashMap, HashSet}
  import Constants._

  /* Abstract definitions */

  implicit val m: Module
  type BFTy // Block-function type
  type FFTy // Function-function type

  // TODO: use Counter to identify block/function
  def repBlockFun(funName: String, b: BB): (BFTy, Int)
  def repFunFun(f: FunctionDef): (FFTy, Int)
  // XXX: should we increase block coverage here?
  def repExternFun(f: FunctionDecl, ret: LLVMType, argTypes: List[LLVMType]): (FFTy, Int)

  def wrapFunV(f: FFTy): Rep[Value]
  def getRealFunctionName(funName: String): String = {
    val new_fname = if (funName != "@main") "__LLSC_USER_"+funName.tail else "llsc_main"
    new_fname.replaceAllLiterally(".","_")
  }

  /* Basic functionalities */

  def info(msg: String) = unchecked("INFO(\"" + msg + "\")")

  def compile(funName: String, b: BB): Unit = {
    if (BBFuns.contains((funName, b))) {
      System.out.println(s"Warning: ignoring the compilation of $funName - ${b.label}")
      return
    }
    val (fn, n) = repBlockFun(funName, b)
    val realFunName = getRealFunctionName(funName)
    blockNameMap(n) = s"${realFunName}_block$n"
    BBFuns((funName, b)) = fn
  }
  def compile(f: FunctionDef): Unit = {
    if (FunFuns.contains(f.id)) {
      System.out.println(s"Warning: ignoring the recompilation of ${f.id}")
      return
    }
    val (fn, n) = repFunFun(f)
    funNameMap(n) = getRealFunctionName(f.id)
    FunFuns(f.id) = fn
  }
  def compile(funs: List[FunctionDef]): Unit = funs.foreach(compile)

  // `ret` and `argTypes` are the type information from the call-site
  def compile(f: FunctionDecl, ret: LLVMType, argTypes: List[LLVMType]): Unit = {
    val mangledName = getMangledFunctionName(f, argTypes);
    if (FunFuns.contains(mangledName)) {
      System.out.println(s"Warning: ignoring the re-generation of missing native external ${mangledName}")
      return
    }
    val (fn, n) = repExternFun(f, ret, argTypes)
    funNameMap(n) = "__LLSC_NATIVE_EXTERNAL_"+mangledName.tail
    FunFuns(mangledName) = fn
  }

  val funMap: StaticMap[String, FunctionDef] = m.funcDefMap
  val funDeclMap: StaticMap[String, FunctionDecl] = m.funcDeclMap
  val globalDefMap: StaticMap[String, GlobalDef] = m.globalDefMap
  val globalDeclMap: StaticMap[String, GlobalDecl] = m.globalDeclMap
  val typeDefMap: StaticMap[String, LLVMType] = m.typeDefMap
  val symDefMap: StaticMap[String, IndirectSymbolDef] = m.symDefMap

  // Note: the following two functions checks/retrieves functions considering aliases.
  // Note: we assume aliases in symDefMap all indeed have a definition (instead of a declaration)
  def isFunDefined(f: String): Boolean =
    funMap.contains(f) || (symDefMap.contains(f) && {
      val src = symDefMap(f).const
      src.isInstanceOf[GlobalId] && funMap.contains(src.asInstanceOf[GlobalId].id)
    })
  def getFunDef(f: String): FunctionDef =
    funMap.getOrElse(f, (for {
      d <- symDefMap.get(f)
      if d.const.isInstanceOf[GlobalId]
      v <- funMap.get(d.const.asInstanceOf[GlobalId].id)
    } yield v).get)

  lazy val cfg: CFG = CFG(funMap)
  //cfg.prettyPrint

  var heapEnv: StaticMap[String, () => Rep[Value]] = StaticMap()

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

  def calculateOffset(ty: LLVMType, index: List[Rep[Value]]): Rep[Value] = {
    if (index.isEmpty) IntV(0.toLong, DEFAULT_INDEX_BW) else ty match {
      case PtrType(ety, addrSpace) =>
        index.head.sExt(DEFAULT_INDEX_BW) * IntV(ety.size, DEFAULT_INDEX_BW) + calculateOffset(ety, index.tail)
      case ArrayType(size, ety) =>
        index.head.sExt(DEFAULT_INDEX_BW) * IntV(ety.size, DEFAULT_INDEX_BW) + calculateOffset(ety, index.tail)
      case NamedType(id) =>
        calculateOffset(typeDefMap(id), index)
      case Struct(types) =>
        // https://llvm.org/docs/LangRef.html#getelementptr-instruction
        // "When indexing into a (optionally packed) structure, only i32 integer
        //  constants are allowed"
        // TODO: the align argument for getTySize
        // TODO: test this
        val indexCst: List[Long] = index.map { case IntV(n, _) => n.toLong }
        IntV(calculateOffsetStatic(ty, indexCst), DEFAULT_INDEX_BW)
      case PackedStruct(types) =>
        val indexCst: List[Long] = index.map { case IntV(n, _) => n.toLong }
        IntV(calculateOffsetStatic(ty, indexCst), DEFAULT_INDEX_BW)
      case _ => ???
    }
  }

  // Note: we can also assign symbolic values here
  def uninitValue: Rep[Value] = IntV(0, 8) //NullPtr()

  def evalHeapAtomicConst(v: Constant, ty: LLVMType): Rep[Value] = v match {
    case BoolConst(b) => IntV(if (b) 1 else 0, 1)
    case IntConst(n) => IntV(n, ty.asInstanceOf[IntType].size)
    case FloatConst(f) => FloatV(f, ty.asInstanceOf[FloatType].size)
    case FloatLitConst(l) => FloatV(l, 80)
    case NullConst => NullLoc()
    case PtrToIntExpr(from, const, to) =>
      val v = evalHeapAtomicConst(const, from)
      if (ARCH_WORD_SIZE == to.asInstanceOf[IntType].size) v
      else v.trunc(ARCH_WORD_SIZE, to.asInstanceOf[IntType].size)
    case GlobalId(id) if funMap.contains(id) =>
      if (!FunFuns.contains(id)) compile(funMap(id))
      wrapFunV(FunFuns(id))
    case GlobalId(id) => heapEnv(id)()
    case BitCastExpr(from, const, to) => evalHeapAtomicConst(const, to)
    case GetElemPtrExpr(inBounds, baseType, ptrType, const, typedConsts) =>
      val indexLLVMValue = typedConsts.map(tv => tv.const.asInstanceOf[IntConst].n)
      val offset = calculateOffsetStatic(ptrType, indexLLVMValue)
      val base = evalHeapAtomicConst(const, getRealType(ptrType)).asRepOf[LocV]
      base + IntV(offset, DEFAULT_INDEX_BW)
    case _ => throw new Exception("Not atomic heap constant " + v)
  }

  def evalHeapComplexConst(v: Constant, realTy: LLVMType): (List[Rep[Value]], Int) = {
    v match {
      case StructConst(cs) => realTy match {
        case Struct(types) =>
          StructCalc().concat(cs, n => StaticList.fill(n)(uninitValue)) { c =>
            evalHeapConstWithAlign(c.const, c.ty)
          }
        case PackedStruct(types) =>
          PackedStructCalc().concat(cs) { c => evalHeapConstWithAlign(c.const, c.ty) }
        case _ => ???
      }
      case ArrayConst(cs) =>
        (cs.map(c => evalHeapConstWithAlign(c.const, c.ty)._1).flatten, realTy.sizeAlign._2)
      case CharArrayConst(s) =>
        val (size, align) = realTy.sizeAlign
        (s.map(c => IntV(c.toInt, 8)).toList ++ StaticList.fill(size-s.length)(uninitValue), align)
      case ZeroInitializerConst => realTy match {
        case ArrayType(size, ety) =>
          val (value, align) = evalHeapConstWithAlign(ZeroInitializerConst, ety)
          (StaticList.fill(size)(value).flatten, align)
        case Struct(types) =>
          StructCalc().concat(types, n => StaticList.fill(n)(uninitValue)) {
            evalHeapConstWithAlign(ZeroInitializerConst, _)
          }
        case PackedStruct(types) =>
          PackedStructCalc().concat(types) { evalHeapConstWithAlign(ZeroInitializerConst, _) }
        // TODO: fallback case is not typed
        case _ =>
          val (size, align) = realTy.sizeAlign
          (IntV(0, 8 * size).toShadowBytes.toStatic, align)
      }
      case UndefConst => realTy match {
        case ArrayType(size, ety) =>
          val (value, align) = evalHeapConstWithAlign(UndefConst, ety)
          (StaticList.fill(size)(value).flatten, align)
        case Struct(types) =>
          StructCalc().concat(types, n => StaticList.fill(n)(uninitValue)) {
            evalHeapConstWithAlign(UndefConst, _)
          }
        case PackedStruct(types) =>
          PackedStructCalc().concat(types) { evalHeapConstWithAlign(UndefConst, _) }
        // TODO: fallback case is not typed
        case _ =>
          val (size, align) = realTy.sizeAlign
          (StaticList.fill(size)(uninitValue), align)
      }
      case _ => throw new Exception("Not complex heap constant: " + v)
    }
  }

  // FIXME: Alignment: CharArrayConst, ArrayConst
  def evalHeapConstWithAlign(v: Constant, ty: LLVMType): (List[Rep[Value]], Int) =
    v match {
      case v if isAtomicConst(v) =>
        val realTy = getRealType(ty)
        val (size, align) = realTy.sizeAlign
        (evalHeapAtomicConst(v, realTy).toShadowBytes.toStatic, align)
      case _ => evalHeapComplexConst(v, getRealType(ty))
    }

  def evalHeapConst(v: Constant, ty: LLVMType): List[Rep[Value]] = evalHeapConstWithAlign(v, ty)._1

  def precompileHeapLists(modules: StaticList[Module]): StaticList[Rep[Value]] = {
    var heapSize = 8
    var heapTmp: StaticList[Rep[Value]] = StaticList.fill(heapSize)(NullPtr[Value])
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
      // heapTmp ++= StaticList.fill(heapSize)(NullPtr())
      module.globalDeclMap.foreach { case (k, v) =>
        val realname = module.mname + "_" + v.id
        val curSize = v.typ.size.toLong
        val heapSize2 = heapSize.toLong
        heapEnv += realname -> (() => LocV(heapSize2, LocV.kHeap, curSize))
        heapSize += curSize
        heapTmp ++= evalHeapConst(ZeroInitializerConst, v.typ)
      }
      module.globalDefMap.foreach { case (k, v) =>
        val curSize = v.typ.size.toLong
        val heapSize2 = heapSize.toLong
        heapEnv += k -> (() => LocV(heapSize2, LocV.kHeap, curSize))
        heapSize += curSize
      }
      module.globalDefMap.foreach { case (k, v) =>
        heapTmp ++= evalHeapConst(v.const, getRealType(v.typ))
      }
    }
    // Additional assert here in case we parse llvm string literals in-correctly
    if (heapTmp.size != heapSize) ???
    heapTmp
  }
}
