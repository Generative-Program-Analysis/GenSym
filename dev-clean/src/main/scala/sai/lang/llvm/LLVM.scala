package sai.lang.llvm

import org.antlr.v4.runtime._
import scala.collection.JavaConverters._

package IR {
  abstract class LAST

  // Module

  case class Module(es: List[TopLevelEntity]) extends LAST

  // TopLevelEntityList

  case class TopLevelEntityList(es: List[TopLevelEntity]) extends LAST

  // TopLevelEntity

  abstract class TopLevelEntity extends LAST

  case object DummyEntity extends TopLevelEntity
  case class SourceFilename(name: String) extends TopLevelEntity
  case class TargetDefinition(ty: String, value: String) extends TopLevelEntity
  case class ModuleAsm(ctx: LLVMParser.ModuleAsmContext) extends TopLevelEntity
  case class TypeDef(ctx: LLVMParser.TypeDefContext) extends TopLevelEntity
  case class ComdatDef(ctx: LLVMParser.ComdatDefContext) extends TopLevelEntity
  case class GlobalDecl(ctx: LLVMParser.GlobalDeclContext) extends TopLevelEntity
  case class GlobalDef(
    id: String,
    linkage: Option[Linkage],
    preemptionSpec: Option[PreemptionSpec],
    visibility: Option[Visibility],
    dllStorageClass: Option[DllStorageClass],
    threadLocal: Option[ThreadLocal],
    unnamedAddr: Option[UnnamedAddr],
    addrSpace: Option[AddrSpace],
    externInit: Option[ExternInitialized],
    immutable: Immutable,
    typ: LLVMType,
    const: Constant,
    globalAttrs: List[GlobalAttr],
    funcAttrs: List[FuncAttr],
  ) extends TopLevelEntity
  case class IndirectSymbolDef(ctx: LLVMParser.IndirectSymbolDefContext) extends TopLevelEntity
  case class FunctionDecl(ctx: LLVMParser.FunctionDeclContext) extends TopLevelEntity
  case class FunctionDef(
    name: String,
    linkage: Option[Linkage],
    header: FunctionHeader,
    body: FunctionBody,
    //ctx: LLVMParser.FunctionDefContext,
  ) extends TopLevelEntity
  case class AttrGroupDef(ctx: LLVMParser.AttrGroupDefContext) extends TopLevelEntity
  case class NamedMetadataDef(ctx: LLVMParser.NamedMetadataDefContext) extends TopLevelEntity
  case class MetadataDef(ctx: LLVMParser.MetadataDefContext) extends TopLevelEntity
  case class UseListOrder(ctx: LLVMParser.UseListOrderContext) extends TopLevelEntity
  case class UseListOrderBB(ctx: LLVMParser.UseListOrderBBContext) extends TopLevelEntity

  case class FunctionHeader(
    preemptionSpec: Option[PreemptionSpec],
    visibility: Option[Visibility],
    dllStorageClass: Option[DllStorageClass],
    callingConv: Option[CallingConv],
    returnAttrs: List[ReturnAttr],
    returnType: LLVMType,
    globalId: String,
    params: List[Param],
    unnamedaddr: Option[UnnamedAddr],
    funcAttrs: List[FuncAttr],
    section: Option[Section],
    // Skipped: optComdat optGC optPrefix optPrologue optPersonality
  ) extends LAST

  case class FunctionBody(
    blocks: List[BB],
    useListOrders: List[UseListOrder]
  ) extends LAST

  case class BB(label: Option[String], ins: List[Instruction], term: Terminator) extends LAST

  case class BasicBlockList(bbs: List[BB]) extends LAST
  case class UseListOrderList(os: List[UseListOrder]) extends LAST

  abstract class CallingConv extends LAST
  // TODO

  case class Section(sec: String) extends LAST

  // Linkage

  abstract class Linkage extends LAST
  case object Appending extends Linkage
  case object AvailableExtern extends Linkage
  case object Common extends Linkage
  case object Internal extends Linkage
  case object LinkOnce extends Linkage
  case object LinkOnceOdr extends Linkage
  case object Private extends Linkage
  case object Weak extends Linkage
  case object WeakOdr extends Linkage

  // Visibility

  abstract class Visibility extends LAST
  case object Default extends Visibility
  case object Hidden extends Visibility
  case object Protected extends Visibility

  abstract class PreemptionSpec extends LAST

  abstract class DllStorageClass extends LAST

  abstract class ThreadLocal extends LAST

  abstract class UnnamedAddr extends LAST
  case object LocalUnnamedAddr extends UnnamedAddr
  case object UnnamedAddr extends UnnamedAddr

  case class AddrSpace(s: Long) extends LAST

  case class ExternInitialized() extends LAST

  abstract class Immutable extends LAST
  case object ConstantImm extends Immutable
  case object GlobalImm extends Immutable

  abstract class FloatKind extends LAST
  case object FK_Half extends FloatKind
  case object FK_Float extends FloatKind
  case object FK_Double extends FloatKind
  case object FK_X86_FP80 extends FloatKind
  case object FK_FP128 extends FloatKind
  case object FK_PPC_FP1289 extends FloatKind

  abstract class LLVMType extends LAST
  case object VoidType extends LLVMType
  case class IntType(size: Int) extends LLVMType {
    override def toString = "i" + size.toString
  }
  case class FloatType(k: FloatKind) extends LLVMType
  case class VectorType(size: Int, ety: LLVMType) extends LLVMType
  case object LabelType extends LLVMType
  case class ArrayType(size: Int, ety: LLVMType) extends LLVMType
  abstract class StructType extends LLVMType
    case class Struct(types: List[LLVMType]) extends StructType
    case class PackedStruct(types: List[LLVMType]) extends StructType
  case class NamedType(id: String) extends LLVMType
  case object MMXType extends LLVMType
  case object TokenType extends LLVMType
  case class FuncType(params: List[Param], ret: LLVMType) extends LLVMType
  case object MetadataType extends LLVMType
  case class PtrType(ty: LLVMType, addrSpace: Option[AddrSpace]) extends LLVMType

  abstract class MetadataAttachment extends LAST

  case class MetadataAttachmentList(ms: List[MetadataAttachment]) extends LAST

  abstract class Param extends LAST
  case object Vararg extends Param
  case class TypedParam(ty: LLVMType, attrs: List[ParamAttr], localId: Option[String]) extends Param

  case class TypedConst(ty: LLVMType, const: Constant) extends LAST

  case class ParamList(ps: List[Param]) extends LAST

  case class TypeList(ts: List[LLVMType]) extends LAST

  case class ParamAttrList(as: List[ParamAttr]) extends LAST

  case class GlobalAttrList(as: List[GlobalAttr]) extends LAST

  case class ReturnAttrList(as: List[ReturnAttr]) extends LAST

  case class FuncAttrList(as: List[FuncAttr]) extends LAST

  case class TypedConstList(cs: List[TypedConst]) extends LAST

  abstract class Attr extends LAST

  case class Alignment(n: Int) extends Attr
      with ParamAttr with GlobalAttr with FuncAttr with ReturnAttr

  case class Dereferenceable(n: Int, orNull: Boolean) extends Attr
      with ParamAttr with ReturnAttr

  case object InReg extends Attr with ReturnAttr with ParamAttr
  case object NoAlias extends Attr with ReturnAttr with ParamAttr
  case object NonNull extends Attr with ReturnAttr with ParamAttr
  case object SignExt extends Attr with ReturnAttr with ParamAttr
  case object ZeroExt extends Attr with ReturnAttr with ParamAttr
  case class Other(s: String) extends Attr with ParamAttr

  trait ParamAttr extends Attr
  case object ByVal extends ParamAttr
  case object InAlloca extends ParamAttr
  case object Nest extends ParamAttr
  case object NoCapture extends ParamAttr
  case object ReadNone extends ParamAttr
  case object Returned extends ParamAttr
  case object SRet extends ParamAttr
  case object SwiftErro extends ParamAttr
  case object SwiftSelf extends ParamAttr
  case object WriteOnly extends ParamAttr

  trait GlobalAttr extends Attr

  trait FuncAttr extends Attr
  case class AttrGroupId(id: String) extends FuncAttr {
    override def toString = id
  }

  trait ReturnAttr extends Attr

  abstract class Constant extends LAST
  case class BoolConst(b: Boolean) extends Constant
  case class IntConst(n: Int) extends Constant
  case class FloatConst(f: Float) extends Constant
  case object NullConst extends Constant
  case object NoneConst extends Constant
  case class StructConst() extends Constant // Skipped
  case class ArrayConst(cs: List[TypedConst]) extends Constant
  case class CharArrayConst(s: String) extends Constant {
    override def toString = s
  }
  case class VectorConst(cs: List[TypedConst]) extends Constant
  case object ZeroInitializerConst extends Constant
  case class GlobalId(id: String) extends Constant
  case object UndefConst extends Constant
  case class BlockAddrConst(globalId: String, localId: String) extends Constant
  abstract class ConstantExpr extends Constant

  abstract class Instruction extends LAST

  abstract class Terminator extends Instruction

}

import IR._

class MyVisitor extends LLVMParserBaseVisitor[LAST] {
  def error = ???

  override def visitModule(ctx: LLVMParser.ModuleContext): LAST = {
    val es = visit(ctx.topLevelEntities).asInstanceOf[TopLevelEntityList].es
    Module(es)
  }
  
  override def visitTopLevelEntityList(ctx: LLVMParser.TopLevelEntityListContext): LAST = {
    val entity = visit(ctx.topLevelEntity).asInstanceOf[TopLevelEntity]
    if (ctx.topLevelEntityList == null)
      TopLevelEntityList(List(entity))
    else {
      val entities = visit(ctx.topLevelEntityList).asInstanceOf[TopLevelEntityList]
      TopLevelEntityList(entities.es ++ List(entity))
    }
  }
  
  override def visitSourceFilename(ctx: LLVMParser.SourceFilenameContext): LAST = {
    SourceFilename(ctx.stringLit.STRING_LIT.getText)
  }

  override def visitTargetDefinition(ctx: LLVMParser.TargetDefinitionContext): LAST = { 
    if (ctx.DATALAYOUT != null)
      TargetDefinition(ctx.DATALAYOUT.getText, ctx.stringLit.STRING_LIT.getText) 
    else if (ctx.TRIPLE != null)
      TargetDefinition(ctx.TRIPLE.getText, ctx.stringLit.STRING_LIT.getText) 
    else error
  }

  override def visitModuleAsm(ctx: LLVMParser.ModuleAsmContext): LAST = { ModuleAsm(ctx) }

  override def visitTypeDef(ctx: LLVMParser.TypeDefContext): LAST = { TypeDef(ctx) }

  override def visitComdatDef(ctx: LLVMParser.ComdatDefContext): LAST = { ComdatDef(ctx) }

  override def visitGlobalDecl(ctx: LLVMParser.GlobalDeclContext): LAST = { GlobalDecl(ctx) }

  override def visitGlobalDef(ctx: LLVMParser.GlobalDefContext): LAST =  {
    val id = ctx.globalIdent.GLOBAL_IDENT.getText
    val linkage =
      if (ctx.optLinkage.linkage != null)
        Some(visit(ctx.optLinkage.linkage).asInstanceOf[Linkage])
      else None
    val preem = None // TODO: Skipped optPreemptionSpecifier
    val vis =
      if (ctx.visibility != null)
        Some(visit(ctx.visibility).asInstanceOf[Visibility])
      else None
    val dll = None // TODO: Skipped optDLLStorageClass
    val thread = None // TODO: Skipped threadLocal
    val unnamedAddr =
      if (ctx.unnamedAddr != null)
        Some(visit(ctx.unnamedAddr).asInstanceOf[UnnamedAddr])
      else None
    val addrSpace =
      if (ctx.optAddrSpace.addrSpace != null)
        Some(visit(ctx.optAddrSpace.addrSpace).asInstanceOf[AddrSpace])
      else None
    val externInit =
      if (ctx.optExternallyInitialized.EXTERNALLY_INITIALIZED != null)
        Some(ExternInitialized()) else None
    val immutable = visit(ctx.immutable).asInstanceOf[Immutable]
    val typ = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val globalAttrs = visit(ctx.globalAttrs).asInstanceOf[GlobalAttrList].as
    val funcAttrs = visit(ctx.funcAttrs).asInstanceOf[FuncAttrList].as
    val const = visit(ctx.constant).asInstanceOf[Constant]
    GlobalDef(id, linkage, preem, vis, dll,
      thread, unnamedAddr, addrSpace,
      externInit, immutable, typ, const, globalAttrs, funcAttrs)
  }

  override def visitIndirectSymbolDef(ctx: LLVMParser.IndirectSymbolDefContext): LAST = {
    IndirectSymbolDef(ctx)
  }

  override def visitFunctionDecl(ctx: LLVMParser.FunctionDeclContext): LAST = {
    FunctionDecl(ctx)
  }

  override def visitAttrGroupDef(ctx: LLVMParser.AttrGroupDefContext): LAST = {
    AttrGroupDef(ctx)
  }

  override def visitNamedMetadataDef(ctx: LLVMParser.NamedMetadataDefContext): LAST = {
    NamedMetadataDef(ctx)
  }

  override def visitMetadataDef(ctx: LLVMParser.MetadataDefContext): LAST = {
    MetadataDef(ctx)
  }

  override def visitUseListOrder(ctx: LLVMParser.UseListOrderContext): LAST = {
    UseListOrder(ctx)
  }

  override def visitUseListOrderBB(ctx: LLVMParser.UseListOrderBBContext): LAST = {
    UseListOrderBB(ctx)
  }

  /***************************************************************************************/

  override def visitLinkage(ctx: LLVMParser.LinkageContext): LAST = {
    if (ctx.APPENDING != null) Appending
    else if (ctx.AVAILABLE_EXTERNALLY != null) AvailableExtern
    else if (ctx.COMMON != null) Common
    else if (ctx.INTERNAL != null) Internal
    else if (ctx.LINKONCE != null) LinkOnce
    else if (ctx.LINKONCE_ODR != null) LinkOnceOdr
    else if (ctx.PRIVATE != null) Private
    else if (ctx.WEAK != null) Weak
    else if (ctx.WEAK_ODR != null) WeakOdr
    else error
  }

  override def visitVisibility(ctx: LLVMParser.VisibilityContext): LAST = {
    if (ctx.DEFAULT != null) Default
    else if (ctx.HIDDEN_VISIB != null) Hidden
    else if (ctx.PROTECTED != null) Protected
    else error
  }

  override def visitUnnamedAddr(ctx: LLVMParser.UnnamedAddrContext): LAST = {
    if (ctx.LOCAL_UNNAMED_ADDR != null) LocalUnnamedAddr
    else if (ctx.UNNAMED_ADDR != null) UnnamedAddr
    else error
  }

  override def visitAddrSpace(ctx: LLVMParser.AddrSpaceContext): LAST = {
    AddrSpace(ctx.INT_LIT.getText.toLong)
  }

  override def visitImmutable(ctx: LLVMParser.ImmutableContext): LAST = {
    if (ctx.CONSTANT != null) ConstantImm
    else if (ctx.GLOBAL != null) GlobalImm
    else error
  }

  override def visitIntType(ctx: LLVMParser.IntTypeContext): LAST = {
    val size_str = ctx.INT_TYPE.getText
    val size = size_str.slice(1, size_str.size).toInt
    IntType(size)
  }

  override def visitFloatKind(ctx: LLVMParser.FloatKindContext): LAST = {
    if (ctx.HALF != null) FK_Half
    else if (ctx.FLOAT != null) FK_Float
    else if (ctx.DOUBLE != null) FK_Double
    else if (ctx.X86_FP80 != null) FK_X86_FP80
    else if (ctx.FP128 != null) FK_FP128
    else if (ctx.PPC_FP128 != null) FK_PPC_FP1289
    else error
  }

  override def visitFloatType(ctx: LLVMParser.FloatTypeContext): LAST = {
    val k = visit(ctx.floatKind).asInstanceOf[FloatKind]
    FloatType(k)
  }

  override def visitVectorType(ctx: LLVMParser.VectorTypeContext): LAST = {
    val size = ctx.INT_LIT.getText.toInt
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    VectorType(size, ty)
  }

  override def visitLabelType(ctx: LLVMParser.LabelTypeContext): LAST = {
    LabelType
  }

  override def visitArrayType(ctx: LLVMParser.ArrayTypeContext): LAST = {
    val size = ctx.INT_LIT.getText.toInt
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    ArrayType(size, ty)
  }

  override def visitStructType(ctx: LLVMParser.StructTypeContext): LAST = {
    if (ctx.LT != null && ctx.GT != null) {
      assert (ctx.LBRACE != null && ctx.RBRACE != null)
      if (ctx.typeList == null) PackedStruct(List()) // '< { } >'
      else {
        // '< {' typeList '} >'
        val types = visit(ctx.typeList).asInstanceOf[TypeList].ts
        PackedStruct(types)
      }
    } else if (ctx.LBRACE != null && ctx.RBRACE != null) {
      if (ctx.typeList == null) Struct(List()) // '{ }'
      else {
        // '{' typeList '}'
        val types = visit(ctx.typeList).asInstanceOf[TypeList].ts
        Struct(types)
      }
    } else error
  }

  override def visitTypeList(ctx: LLVMParser.TypeListContext): LAST = {
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    if (ctx.typeList == null) { TypeList(List(ty)) }
    else {
      val tys = visit(ctx.typeList).asInstanceOf[TypeList]
      TypeList(tys.ts ++ List(ty))
    }
  }

  override def visitNamedType(ctx: LLVMParser.NamedTypeContext): LAST = {
    ???
  }

  override def visitMmxType(ctx: LLVMParser.MmxTypeContext): LAST = {
    ???
  }

  override def visitTokenType(ctx: LLVMParser.TokenTypeContext): LAST = {
    ???
  }

  override def visitParams(ctx: LLVMParser.ParamsContext): LAST = {
    if (ctx.paramList != null) {
      val ps = visit(ctx.paramList).asInstanceOf[ParamList]
      if (ctx.DOTS != null && ctx.COMMA != null) {
        ParamList(ps.ps ++ List(Vararg))
      } else { ps }
    } else if (ctx.DOTS != null) {
      ParamList(List(Vararg))
    } else {
      ParamList(List())
    }
  }

  override def visitParam(ctx: LLVMParser.ParamContext): LAST = {
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val attrs = visit(ctx.paramAttrs).asInstanceOf[ParamAttrList].as
    if (ctx.localIdent == null) {
      TypedParam(ty, attrs, None)
    } else {
      val id = ctx.localIdent.LOCAL_IDENT.getText
      TypedParam(ty, attrs, Some(id))
    }
  }

  override def visitParamList(ctx: LLVMParser.ParamListContext): LAST = {
    val p = visit(ctx.param).asInstanceOf[Param]
    if (ctx.paramList == null) {
      ParamList(List(p))
    } else {
      val ps = visit(ctx.paramList).asInstanceOf[ParamList].ps
      ParamList(ps ++ List(p))
    }
  }

  override def visitParamAttrs(ctx: LLVMParser.ParamAttrsContext): LAST = {
    if (ctx.paramAttrList != null)
      visit(ctx.paramAttrList)
    else
      ParamAttrList(List())
  }

  override def visitParamAttrList(ctx: LLVMParser.ParamAttrListContext): LAST = {
    val a = visit(ctx.paramAttr).asInstanceOf[ParamAttr]
    if (ctx.paramAttrList == null) {
      ParamAttrList(List(a))
    } else {
      val as = visit(ctx.paramAttrList).asInstanceOf[ParamAttrList].as
      ParamAttrList(as ++ List(a))
    }
  }

  override def visitParamAttr(ctx: LLVMParser.ParamAttrContext): LAST = {
    // Return ParamAttr
    ???
  }

  override def visitLlvmType(ctx: LLVMParser.LlvmTypeContext): LAST = {
    if (ctx.voidType != null)
      VoidType
    else if (ctx.concreteNonRecType != null)
      visit(ctx.concreteNonRecType)
    else if (ctx.llvmType != null && ctx.params != null) {
      val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
      val ps = visit(ctx.params).asInstanceOf[ParamList].ps
      FuncType(ps, ty)
    }
    else if (ctx.metadataType != null) MetadataType
    else if (ctx.llvmType != null && ctx.optAddrSpace != null && ctx.STAR != null) {
      val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
      val addrSpace =
        if (ctx.optAddrSpace.addrSpace != null)
          Some(visit(ctx.optAddrSpace.addrSpace).asInstanceOf[AddrSpace])
        else None
      PtrType(ty, addrSpace)
    }
    else error
  }

  override def visitConstant(ctx: LLVMParser.ConstantContext): LAST = {
    super.visitConstant(ctx)
  }

  // This can be removed
  override def visitBoolConst(ctx: LLVMParser.BoolConstContext): LAST = {
    visit(ctx.boolLit)
  }

  override def visitBoolLit(ctx: LLVMParser.BoolLitContext): LAST = {
    if (ctx.TRUE != null) BoolConst(true)
    else if (ctx.FALSE != null) BoolConst(false)
    else error
  }

  // Note: there is a duplication of intConst and intLit in the grammar
  override def visitIntConst(ctx: LLVMParser.IntConstContext): LAST = {
    IntConst(ctx.INT_LIT.getText.toInt)
  }

  override def visitIntLit(ctx: LLVMParser.IntLitContext): LAST = {
    IntConst(ctx.INT_LIT.getText.toInt)
  }

  override def visitFloatConst(ctx: LLVMParser.FloatConstContext): LAST = {
    FloatConst(ctx.FLOAT_LIT.getText.toFloat)
  }

  override def visitArrayConst(ctx: LLVMParser.ArrayConstContext): LAST = {
    val cs = visit(ctx.typeConsts).asInstanceOf[TypedConstList].cs
    ArrayConst(cs)
  }

  override def visitVectorConst(ctx: LLVMParser.VectorConstContext): LAST = {
    val cs = visit(ctx.typeConsts).asInstanceOf[TypedConstList].cs
    VectorConst(cs)
  }

  override def visitNullConst(ctx: LLVMParser.NullConstContext): LAST = {
    NullConst
  }

  override def visitNoneConst(ctx: LLVMParser.NoneConstContext): LAST = {
    NoneConst
  }

  override def visitCharArrayConst(ctx: LLVMParser.CharArrayConstContext): LAST = {
    CharArrayConst(ctx.stringLit.STRING_LIT.getText)
  }

  override def visitStructConst(ctx: LLVMParser.StructConstContext): LAST = {
    ??? //Skipped
  }

  override def visitZeroInitializerConst(ctx: LLVMParser.ZeroInitializerConstContext): LAST = {
    ZeroInitializerConst
  }

  override def visitGlobalIdent(ctx: LLVMParser.GlobalIdentContext): LAST = {
    val id = ctx.GLOBAL_IDENT.getText
    GlobalId(id)
  }

  override def visitUndefConst(ctx: LLVMParser.UndefConstContext): LAST = {
    UndefConst
  }

  override def visitBlockAddressConst(ctx: LLVMParser.BlockAddressConstContext): LAST = {
    val g = ctx.globalIdent.GLOBAL_IDENT.getText
    val l = ctx.localIdent.LOCAL_IDENT.getText
    BlockAddrConst(g, l)
  }

  override def visitConstantExpr(ctx: LLVMParser.ConstantExprContext): LAST = {
    ???
  }

  override def visitTypeConsts(ctx: LLVMParser.TypeConstsContext): LAST = {
    if (ctx.typeConstList != null)
      visit(ctx.typeConstList)
    else TypedConstList(List())
  }

  override def visitTypeConstList(ctx: LLVMParser.TypeConstListContext): LAST = {
    val tc = visit(ctx.typeConst).asInstanceOf[TypedConst]
    if (ctx.typeConstList == null) {
      TypedConstList(List(tc))
    } else {
      val tcs = visit(ctx.typeConstList).asInstanceOf[TypedConstList].cs
      TypedConstList(tcs ++ List(tc))
    }
  }

  override def visitTypeConst(ctx: LLVMParser.TypeConstContext): LAST = {
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val const = visit(ctx.constant).asInstanceOf[Constant]
    TypedConst(ty, const)
  }

  override def visitGlobalAttrs(ctx: LLVMParser.GlobalAttrsContext): LAST = {
    if (ctx.globalAttrList != null) visit(ctx.globalAttrList)
    else GlobalAttrList(List())
  }

  override def visitGlobalAttrList(ctx: LLVMParser.GlobalAttrListContext): LAST = {
    val a = visit(ctx.globalAttr).asInstanceOf[GlobalAttr]
    if (ctx.globalAttrList == null) {
      GlobalAttrList(List(a))
    } else {
      val as = visit(ctx.globalAttrList).asInstanceOf[GlobalAttrList].as
      GlobalAttrList(as ++ List(a))
    }
  }

  override def visitGlobalAttr(ctx: LLVMParser.GlobalAttrContext): LAST = {
    super.visitGlobalAttr(ctx)
    // Note: only alignment is handled
  }

  override def visitAlignment(ctx: LLVMParser.AlignmentContext): LAST = {
    Alignment(ctx.INT_LIT.getText.toInt)
  }

  override def visitDereferenceable(ctx: LLVMParser.DereferenceableContext): LAST = {
    ???
  }

  override def visitFuncAttrs(ctx: LLVMParser.FuncAttrsContext): LAST = {
    if (ctx.funcAttrList != null) visit(ctx.funcAttrList)
    else FuncAttrList(List())
  }

  override def visitFuncAttrList(ctx: LLVMParser.FuncAttrListContext): LAST = {
    val a = visit(ctx.funcAttr).asInstanceOf[FuncAttr]
    if (ctx.funcAttrList == null) {
      FuncAttrList(List(a))
    } else {
      val as = visit(ctx.funcAttrList).asInstanceOf[FuncAttrList].as
      FuncAttrList(as ++ List(a))
    }
  }

  override def visitFuncAttr(ctx: LLVMParser.FuncAttrContext): LAST = {
    //println(ctx.getText)
    super.visitFuncAttr(ctx)
  }

  override def visitAttrGroupID(ctx: LLVMParser.AttrGroupIDContext): LAST = {
    AttrGroupId(ctx.ATTR_GROUP_ID.getText)
  }

  override def visitFunctionDef(ctx: LLVMParser.FunctionDefContext): LAST = {
    val linkage =
      if (ctx.optLinkage.linkage != null)
        Some(visit(ctx.optLinkage.linkage).asInstanceOf[Linkage])
      else None
    val header = visit(ctx.functionHeader).asInstanceOf[FunctionHeader]
    println(header)
    val id = header.globalId
    val ms = visit(ctx.metadataAttachments).asInstanceOf[MetadataAttachmentList].ms
    ???
  }

  override def visitFunctionBody(ctx: LLVMParser.FunctionBodyContext): LAST = {
    val BBs = visit(ctx.basicBlockList).asInstanceOf[BasicBlockList].bbs
    val orders = visit(ctx.useListOrders).asInstanceOf[UseListOrderList].os
    FunctionBody(BBs, orders)
  }

  override def visitBasicBlockList(ctx: LLVMParser.BasicBlockListContext): LAST = {
    val bb = visit(ctx.basicBlock).asInstanceOf[BB]
    if (ctx.basicBlockList == null)
      BasicBlockList(List(bb))
    else {
      val bbs = visit(ctx.basicBlockList).asInstanceOf[BasicBlockList].bbs
      BasicBlockList(bbs ++ List(bb))
    }
  }

  override def visitBasicBlock(ctx: LLVMParser.BasicBlockContext): LAST = {
    ???
  }

  override def visitUseListOrders(ctx: LLVMParser.UseListOrdersContext): LAST = {
    // Skipped
    UseListOrderList(List())
  }

  override def visitFunctionHeader(ctx: LLVMParser.FunctionHeaderContext): LAST = {
    val optPreepSpec = None
    val vis =
      if (ctx.visibility != null)
        Some(visit(ctx.visibility).asInstanceOf[Visibility])
      else None
    val optDll = None
    val optCallConv = None
    val returnAttrs = visit(ctx.returnAttrs).asInstanceOf[ReturnAttrList].as
    val retType = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val id = ctx.globalIdent.GLOBAL_IDENT.getText
    val params = visit(ctx.params).asInstanceOf[ParamList].ps
    val unnamedAddr =
      if (ctx.unnamedAddr != null)
        Some(visit(ctx.unnamedAddr).asInstanceOf[UnnamedAddr])
      else None
    val funcAttrs = visit(ctx.funcAttrs).asInstanceOf[FuncAttrList].as //TODO
    val section =
      if (ctx.section != null)
        Some(visit(ctx.section).asInstanceOf[Section]) //TODO
      else None
    FunctionHeader(optPreepSpec, vis, optDll, optCallConv, returnAttrs,
      retType, id, params, unnamedAddr, funcAttrs, section)
  }

  override def visitMetadataAttachments(ctx: LLVMParser.MetadataAttachmentsContext): LAST = {
    //super.visitMetadataAttachments(ctx)
    // Skipped
    MetadataAttachmentList(List())
  }

  override def visitReturnAttrs(ctx: LLVMParser.ReturnAttrsContext): LAST = {
    if (ctx.returnAttrList == null) ReturnAttrList(List())
    else visit(ctx.returnAttrList)
  }

  override def visitReturnAttrList(ctx: LLVMParser.ReturnAttrListContext): LAST = {
    val a = visit(ctx.returnAttr).asInstanceOf[ReturnAttr]
    if (ctx.returnAttrList == null)
      ReturnAttrList(List(a))
    else {
      val as = visit(ctx.returnAttrList).asInstanceOf[ReturnAttrList].as
      ReturnAttrList(as ++ List(a))
    }
  }

  override def visitReturnAttr(ctx: LLVMParser.ReturnAttrContext): LAST = {
    if (ctx.stringLit != null) {
      Other(ctx.stringLit.STRING_LIT.getText)
    } else if (ctx.INREG != null) {
      InReg
    } else if (ctx.NOALIAS != null) {
      NoAlias
    } else if (ctx.NONNULL != null) {
      NonNull
    } else if (ctx.SIGNEXT != null) {
      SignExt
    } else if (ctx.ZEROEXT != null) {
      ZeroExt
    } else {
      super.visitReturnAttr(ctx)
    }
  }

}

object LLVMTest {
  def parse(input: String) = {
    val charStream = new ANTLRInputStream(input)
    val lexer = new LLVMLexer(charStream)
    val tokens = new CommonTokenStream(lexer)
    val parser = new LLVMParser(tokens)

    val visitor = new MyVisitor()
    val res: Module  = visitor.visit(parser.module).asInstanceOf[Module]
    //println(res.es(3))
    println(res.es(19))
    //println(res)
  }

  def main(args: Array[String]): Unit = {
    //val testInput = scala.io.Source.fromFile("test.ll").mkString
    val testInput = scala.io.Source.fromFile("maze.ll").mkString
    println(testInput)
    parse(testInput)
  }
}
