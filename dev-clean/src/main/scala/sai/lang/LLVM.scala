package sai.lang.llvm

import org.antlr.v4.runtime._
import scala.collection.JavaConverters._

package IR {
  abstract class LAST

  // Module

  case class Module(es: List[TopLevelEntity]) extends LAST {
    var mname: String = ""
    val funcDefMap: Map[String, FunctionDef] =
      es.filter(_.isInstanceOf[FunctionDef]).asInstanceOf[List[FunctionDef]].map(f => (f.id, f)).toMap
    val funcDeclMap: Map[String, FunctionDecl] =
      es.filter(_.isInstanceOf[FunctionDecl]).asInstanceOf[List[FunctionDecl]].map(f => (f.id, f)).toMap
    val globalDefMap: Map[String, GlobalDef] =
      es.filter(_.isInstanceOf[GlobalDef]).asInstanceOf[List[GlobalDef]].map(d => (d.id, d)).toMap
    val globalDeclMap: Map[String, GlobalDecl] =
      es.filter(_.isInstanceOf[GlobalDecl]).asInstanceOf[List[GlobalDecl]].map(d => (d.id, d)).toMap
    val typeDefMap: Map[String, LLVMType] =
      es.filter(_.isInstanceOf[TypeDef]).asInstanceOf[List[TypeDef]].map(d => (d.id, d.ty)).toMap

    def lookupFuncDef(id: String): Option[FunctionDef] = funcDefMap.get(id)
    def lookupFuncDecl(id: String): Option[FunctionDecl] = funcDeclMap.get(id)
    def lookupGlobalDef(id: String): Option[GlobalDef] = globalDefMap.get(id)

    def lookup(id: String): Option[TopLevelEntity] = {
      lookupFuncDef(id) orElse
      lookupFuncDecl(id) orElse
      lookupGlobalDef(id)
    }
  }

  // TopLevelEntityList

  case class TopLevelEntityList(es: List[TopLevelEntity]) extends LAST

  // TopLevelEntity

  abstract class TopLevelEntity extends LAST

  case object DummyEntity extends TopLevelEntity
  case class SourceFilename(name: String) extends TopLevelEntity
  case class TargetDefinition(ty: String, value: String) extends TopLevelEntity
  case class ModuleAsm(ctx: LLVMParser.ModuleAsmContext) extends TopLevelEntity
  case class TypeDef(
    id: String,
    ty: LLVMType
  ) extends TopLevelEntity
  case class ComdatDef(ctx: LLVMParser.ComdatDefContext) extends TopLevelEntity
  case class GlobalDecl(
    id: String,
    linkage: ExternalLinkage,
    preemptionSpec: Option[PreemptionSpec],
    visibility: Option[Visibility],
    dllStorageClass: Option[DllStorageClass],
    threadLocal: Option[ThreadLocal],
    unnamedAddr: Option[UnnamedAddr],
    addrSpace: Option[AddrSpace],
    externInit: Option[ExternInitialized],
    immutable: Immutable,
    typ: LLVMType,
    globalAttrs: List[GlobalAttr],
    funcAttrs: List[FuncAttr],
  ) extends TopLevelEntity
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
  case class FunctionDecl(id: String, header: FunctionHeader) extends TopLevelEntity
  case class FunctionDef(
    id: String,
    linkage: Option[Linkage],
    metadata: List[MetadataAttachment],
    header: FunctionHeader,
    body: FunctionBody,
    //ctx: LLVMParser.FunctionDefContext,
  ) extends TopLevelEntity {
    val retType: LLVMType = header.returnType
    val params: List[Param] = header.params
    val blocks: List[BB] = body.blocks
    def lookupBlock(label: String): Option[BB] = {
      // Assume all blocks exists
      blocks.find(_.label == Some(label))
    }
  }
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
  case class InstructionList(is: List[Instruction]) extends LAST
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

  abstract class ExternalLinkage extends LAST
  case object ExternalWeak extends ExternalLinkage
  case object External extends ExternalLinkage

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
  case object FK_BFloat extends FloatKind
  case object FK_Float extends FloatKind
  case object FK_Double extends FloatKind
  case object FK_X86_FP80 extends FloatKind
  case object FK_FP128 extends FloatKind
  case object FK_PPC_FP128 extends FloatKind

  abstract class LLVMType extends LAST
  case object VoidType extends LLVMType
  case object OpaqueType extends LLVMType
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

  trait Param extends LAST
  case class TypedParam(ty: LLVMType, attrs: List[ParamAttr], localId: Option[String]) extends Param

  trait Arg extends LAST
  case class TypedArg(ty: LLVMType, attrs: List[ParamAttr], value: LLVMValue) extends Arg
  case class MetadataArg(/* TODO */) extends Arg

  case object Vararg extends Param with Arg

  case class ParamList(ps: List[Param]) extends LAST

  case class ArgList(as: List[Arg]) extends LAST

  case class TypeList(ts: List[LLVMType]) extends LAST

  case class ParamAttrList(as: List[ParamAttr]) extends LAST

  case class GlobalAttrList(as: List[GlobalAttr]) extends LAST

  case class ReturnAttrList(as: List[ReturnAttr]) extends LAST

  case class FuncAttrList(as: List[FuncAttr]) extends LAST

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
  case object ReadOnly extends ParamAttr
  case object Immarg extends ParamAttr
  case object UnknownAttr extends ParamAttr

  trait GlobalAttr extends Attr

  trait FuncAttr extends Attr
  case class AttrGroupId(id: String) extends FuncAttr {
    override def toString = id
  }

  trait ReturnAttr extends Attr

  abstract class LLVMValue extends LAST

  case class LocalId(x: String) extends LLVMValue
  case class InlineASM(/*TODO*/) extends LLVMValue

  abstract class Constant extends LLVMValue
  case class BoolConst(b: Boolean) extends Constant
  case class IntConst(n: Long) extends Constant
  case class FloatConst(f: Float) extends Constant
  case object NullConst extends Constant
  case object NoneConst extends Constant
  case class StructConst(cs: List[TypedConst]) extends Constant
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

  case class GetElemPtrExpr(
    inBounds: Boolean, // false means not specified
    baseType: LLVMType,
    ptrType: LLVMType,
    const: Constant,
    typedConsts: List[TypedConst]
  ) extends ConstantExpr

  case class PtrToIntExpr(from: LLVMType, const: Constant, to: LLVMType) extends ConstantExpr
  case class BitCastExpr(from: LLVMType, const: Constant, to: LLVMType) extends ConstantExpr

  case class TypedConst(inRange: Option[Boolean], ty: LLVMType, const: Constant) extends LAST
  case class TypedConstList(cs: List[TypedConst]) extends LAST
  case class ConstList(cs: List[Constant]) extends LAST

  abstract class OverflowFlag extends LAST
  case object NSW extends OverflowFlag
  case object NUW extends OverflowFlag

  case class OverflowFlagList(fs: List[OverflowFlag]) extends LAST

  case class TypedValue(ty: LLVMType, value: LLVMValue) extends LAST
  case class TypedValueList(tvs: List[TypedValue]) extends LAST

  abstract class Instruction extends LAST
  abstract class ValueInstruction extends Instruction
  case class AddInst(ty: LLVMType, lhs: LLVMValue, rhs: LLVMValue, of: List[OverflowFlag]) extends ValueInstruction
  case class MulInst(ty: LLVMType, lhs: LLVMValue, rhs: LLVMValue, of: List[OverflowFlag]) extends ValueInstruction
  case class SubInst(ty: LLVMType, lhs: LLVMValue, rhs: LLVMValue, of: List[OverflowFlag]) extends ValueInstruction
  case class FAddInst(ty: LLVMType, lhs: LLVMValue, rhs: LLVMValue) extends ValueInstruction
  case class FSubInst(ty: LLVMType, lhs: LLVMValue, rhs: LLVMValue) extends ValueInstruction
  case class FMulInst(ty: LLVMType, lhs: LLVMValue, rhs: LLVMValue) extends ValueInstruction
  case class UDivInst(ty: LLVMType, lhs: LLVMValue, rhs: LLVMValue) extends ValueInstruction
  case class SDivInst(ty: LLVMType, lhs: LLVMValue, rhs: LLVMValue) extends ValueInstruction
  case class FDivInst(ty: LLVMType, lhs: LLVMValue, rhs: LLVMValue) extends ValueInstruction
  case class URemInst(ty: LLVMType, lhs: LLVMValue, rhs: LLVMValue) extends ValueInstruction
  case class SRemInst(ty: LLVMType, lhs: LLVMValue, rhs: LLVMValue) extends ValueInstruction
  case class ShlInst(ty: LLVMType, lhs: LLVMValue, rhs: LLVMValue) extends ValueInstruction
  case class LshrInst(ty: LLVMType, lhs: LLVMValue, rhs: LLVMValue) extends ValueInstruction
  case class AshrInst(ty: LLVMType, lhs: LLVMValue, rhs: LLVMValue) extends ValueInstruction
  case class AndInst(ty: LLVMType, lhs: LLVMValue, rhs: LLVMValue) extends ValueInstruction
  case class OrInst(ty: LLVMType, lhs: LLVMValue, rhs: LLVMValue) extends ValueInstruction
  case class XorInst(ty: LLVMType, lhs: LLVMValue, rhs: LLVMValue) extends ValueInstruction

  case class AllocaInst(ty: LLVMType, align: Alignment) extends ValueInstruction
  case class LoadInst(
    valTy: LLVMType,
    ptrTy: LLVMType,
    value: LLVMValue,
    align: Alignment
  ) extends ValueInstruction
  case class GetElemPtrInst(
    inBounds: Boolean, // false means not specified
    baseType: LLVMType,
    ptrType: LLVMType,
    ptrValue: LLVMValue,
    typedValues: List[TypedValue]
  ) extends ValueInstruction
  case class BitCastInst(from: LLVMType, value: LLVMValue, to: LLVMType) extends ValueInstruction
  case class ICmpInst(pred: IPredicate, ty: LLVMType, lhs: LLVMValue, rhs: LLVMValue) extends ValueInstruction
  case class FCmpInst(pred: FPredicate, ty: LLVMType, lhs: LLVMValue, rhs: LLVMValue) extends ValueInstruction
  case class TruncInst(from: LLVMType, value: LLVMValue, to: LLVMType) extends ValueInstruction
  case class ZExtInst(from: LLVMType, value: LLVMValue, to: LLVMType) extends ValueInstruction
  case class SExtInst(from: LLVMType, value: LLVMValue, to: LLVMType) extends ValueInstruction
  case class FpExtInst(from: LLVMType, value: LLVMValue, to: LLVMType) extends ValueInstruction
  case class FpToUIInst(from: LLVMType, value: LLVMValue, to: LLVMType) extends ValueInstruction
  case class FpToSIInst(from: LLVMType, value: LLVMValue, to: LLVMType) extends ValueInstruction
  case class UiToFPInst(from: LLVMType, value: LLVMValue, to: LLVMType) extends ValueInstruction
  case class SiToFPInst(from: LLVMType, value: LLVMValue, to: LLVMType) extends ValueInstruction
  case class PtrToIntInst(from: LLVMType, value: LLVMValue, to: LLVMType) extends ValueInstruction
  case class IntToPtrInst(from: LLVMType, value: LLVMValue, to: LLVMType) extends ValueInstruction

  case class ExtractValueInst(ty: LLVMType, struct: LLVMValue, indices: List[Constant]) extends ValueInstruction
  case class CallInst(ty: LLVMType, f: LLVMValue, args: List[Arg]) extends ValueInstruction
  case class PhiInst(ty: LLVMType, incs: List[Inc]) extends ValueInstruction
  case class SelectInst(
    cndTy: LLVMType,
    cndVal: LLVMValue,
    thnTy: LLVMType,
    thnVal: LLVMValue,
    elsTy: LLVMType,
    elsVal: LLVMValue
  ) extends ValueInstruction

  // Incoming value with block label
  case class Inc(value: LLVMValue, label: String) extends LAST
  case class IncList(incs: List[Inc]) extends LAST

  case class AssignInst(x: String, valInst: ValueInstruction) extends Instruction
  case class StoreInst(vt: LLVMType, vv: LLVMValue,
    at: LLVMType, av: LLVMValue, align: Alignment) extends Instruction

  abstract class Terminator extends Instruction
  case class RetTerm(ty: LLVMType, value: Option[LLVMValue]) extends Terminator
  case class BrTerm(label: String) extends Terminator
  case class CondBrTerm(
    ty: IntType,
    cnd: LLVMValue,
    thnLab: String,
    elsLab: String
  ) extends Terminator
  case object Unreachable extends Terminator
  case class SwitchTerm(
    cndTy: LLVMType,
    cndVal: LLVMValue,
    default: String,
    table: List[LLVMCase]
  ) extends Terminator

  case class LLVMCase(ty: LLVMType, n: Int, label: String) extends LAST
  case class CaseList(cs: List[LLVMCase]) extends LAST

  // check visitIPred and visitFpred for available ops
  case class IPredicate(op: String) extends LAST
  case class FPredicate(op: String) extends LAST

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

  override def visitTypeDef(ctx: LLVMParser.TypeDefContext): LAST = {
    val id = ctx.localIdent.LOCAL_IDENT.getText
    if (ctx.opaqueType != null) {
      TypeDef(id, OpaqueType)
    }
    else {
      val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
      TypeDef(id, ty)
    }
  }

  override def visitComdatDef(ctx: LLVMParser.ComdatDefContext): LAST = { ComdatDef(ctx) }

  override def visitGlobalDecl(ctx: LLVMParser.GlobalDeclContext): LAST = {
    val id = ctx.globalIdent.GLOBAL_IDENT.getText
    val linkage = visit(ctx.externLinkage).asInstanceOf[ExternalLinkage]
    val preem = None // TODO: Skipped optPreemptionSpecifier
    val dll = None // TODO: Skipped optDLLStorageClass
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
    val vis =
      if (ctx.visibility != null)
        Some(visit(ctx.visibility).asInstanceOf[Visibility])
      else None
    val thread = None // TODO: Skipped threadLocal
    val unnamedAddr =
      if (ctx.unnamedAddr != null)
        Some(visit(ctx.unnamedAddr).asInstanceOf[UnnamedAddr])
      else None
    GlobalDecl(id, linkage, preem, vis, dll,
      thread, unnamedAddr, addrSpace,
      externInit, immutable, typ, globalAttrs, funcAttrs)
  }

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

  override def visitExternLinkage(ctx: LLVMParser.ExternLinkageContext): LAST = {
    if (ctx.EXTERNAL != null) External
    else if (ctx.EXTERN_WEAK != null) ExternalWeak
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
    else if (ctx.PPC_FP128 != null) FK_PPC_FP128
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
    val id = ctx.localIdent.LOCAL_IDENT.getText
    NamedType(id)
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

  override def visitParamList(ctx: LLVMParser.ParamListContext): LAST = {
    val p = visit(ctx.param).asInstanceOf[Param]
    if (ctx.paramList == null) {
      ParamList(List(p))
    } else {
      val ps = visit(ctx.paramList).asInstanceOf[ParamList].ps
      ParamList(ps ++ List(p))
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

    if (ctx.ZEROEXT() != null) ZeroExt
    else if (ctx.SIGNEXT() != null) SignExt
    else if (ctx.INREG() != null) InReg
    else if (ctx.BYVAL() != null) ByVal
    else if (ctx.INALLOCA() != null) InAlloca
    else if (ctx.SRET() != null) SRet
    else if (ctx.NOALIAS() != null) NoAlias
    else if (ctx.NOCAPTURE() != null) NoCapture
    else if (ctx.NONNULL() != null) NonNull
    else if (ctx.WRITEONLY() != null) WriteOnly
    else if (ctx.READONLY() != null) ReadOnly
    else if (ctx.IMMARG() != null) Immarg
    else if (ctx.alignment() != null) {
      visit(ctx.alignment)
    } else {
      println("Warning: parsing unknown param attr")
      UnknownAttr
    }
    // ?immarg??
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
    IntConst(ctx.INT_LIT.getText.toLong)
  }

  override def visitIntLit(ctx: LLVMParser.IntLitContext): LAST = {
    IntConst(ctx.INT_LIT.getText.toLong)
  }

  override def visitFloatConst(ctx: LLVMParser.FloatConstContext): LAST = {
    val floatStr = ctx.FLOAT_LIT.getText
    if (floatStr.contains('.')) FloatConst(floatStr.toFloat)
    else if (floatStr.startsWith("0x")) {
      val hexString = floatStr.substring(2)
      val longBits = java.lang.Long.parseUnsignedLong(hexString, 16)
      FloatConst(java.lang.Double.longBitsToDouble(longBits).asInstanceOf[Float])
    }
    else ???
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
    val raw = ctx.stringLit.STRING_LIT.getText
    var s = raw.slice(1, raw.length - 1)
    s = s.replaceAllLiterally("\\0A", "\n")
    s = s.replaceAllLiterally("\\00", "\u0000")
    CharArrayConst(s)
  }

  override def visitStructConst(ctx: LLVMParser.StructConstContext): LAST = {
    val cs = visit(ctx.typeConstList).asInstanceOf[TypedConstList].cs
    StructConst(cs)
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
    super.visitConstantExpr(ctx)
  }

  override def visitGetElementPtrExpr(ctx: LLVMParser.GetElementPtrExprContext): LAST = {
    val inBounds =
      if (ctx.optInBounds.INBOUNDS != null) true
      else false
    val baseTy = visit(ctx.llvmType(0)).asInstanceOf[LLVMType]
    val ptrTy = visit(ctx.llvmType(1)).asInstanceOf[LLVMType]
    val const = visit(ctx.constant).asInstanceOf[Constant]
    val restConsts = visit(ctx.gepConstIndices).asInstanceOf[TypedConstList].cs
    GetElemPtrExpr(inBounds, baseTy, ptrTy, const, restConsts)
  }

  override def visitGepConstIndices(ctx: LLVMParser.GepConstIndicesContext): LAST = {
    if (ctx.gepConstIndexList == null)
      TypedConstList(List())
    else
      visit(ctx.gepConstIndexList)
  }

  override def visitGepConstIndexList(ctx: LLVMParser.GepConstIndexListContext): LAST = {
    val c = visit(ctx.gepConstIndex).asInstanceOf[TypedConst]
    if (ctx.gepConstIndexList == null)
      TypedConstList(List(c))
    else {
      val cs = visit(ctx.gepConstIndexList).asInstanceOf[TypedConstList].cs
      TypedConstList(cs ++ List(c))
    }
  }

  override def visitGepConstIndex(ctx: LLVMParser.GepConstIndexContext): LAST = {
    val inRange =
      if (ctx.optInrange.INRANGE != null) Some(true)
      else Some(false)
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val const = visit(ctx.constant).asInstanceOf[Constant]
    TypedConst(inRange, ty, const)
  }

  override def visitPtrToIntExpr(ctx: LLVMParser.PtrToIntExprContext): LAST = {
    val from = visit(ctx.llvmType(0)).asInstanceOf[LLVMType]
    val const = visit(ctx.constant).asInstanceOf[Constant]
    val to = visit(ctx.llvmType(1)).asInstanceOf[LLVMType]
    PtrToIntExpr(from, const, to)
  }

  override def visitBitCastExpr(ctx: LLVMParser.BitCastExprContext): LAST = {
    val from = visit(ctx.llvmType(0)).asInstanceOf[LLVMType]
    val const = visit(ctx.constant).asInstanceOf[Constant]
    val to = visit(ctx.llvmType(1)).asInstanceOf[LLVMType]
    BitCastExpr(from, const, to)
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
    TypedConst(None, ty, const)
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
    val id = header.globalId
    val ms = visit(ctx.metadataAttachments).asInstanceOf[MetadataAttachmentList].ms
    val body = visit(ctx.functionBody).asInstanceOf[FunctionBody]
    FunctionDef(id, linkage, ms, header, body)
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
    val label =
      if (ctx.optLabelIdent.labelIdent != null) {
        val l = ctx.optLabelIdent.labelIdent.LABEL_IDENT.getText
        Some("%"+l.substring(0, l.size-1))
      }
      else
        None
    val instructions = visit(ctx.instructions).asInstanceOf[InstructionList].is
    val term = visit(ctx.terminator).asInstanceOf[Terminator]
    BB(label, instructions, term)
  }

  override def visitInstructions(ctx: LLVMParser.InstructionsContext): LAST = {
    if (ctx.instructionList == null) InstructionList(List())
    else visit(ctx.instructionList)
  }

  override def visitInstructionList(ctx: LLVMParser.InstructionListContext): LAST = {
    val i = visit(ctx.instruction).asInstanceOf[Instruction]
    if (ctx.instructionList == null)
      InstructionList(List(i))
    else {
      val is = visit(ctx.instructionList).asInstanceOf[InstructionList].is
      InstructionList(is ++ List(i))
    }
  }

  override def visitInstruction(ctx: LLVMParser.InstructionContext): LAST = {
    if (ctx.localIdent != null && ctx.valueInstruction != null) {
      val id = ctx.localIdent.LOCAL_IDENT.getText
      val valInst = visit(ctx.valueInstruction).asInstanceOf[ValueInstruction]
      AssignInst(id, valInst)
    } else {
      super.visitInstruction(ctx)
    }
  }

  override def visitValueInstruction(ctx: LLVMParser.ValueInstructionContext): LAST = {
    super.visitValueInstruction(ctx)
  }

  override def visitAddInst(ctx: LLVMParser.AddInstContext): LAST = {
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val lhs = visit(ctx.value(0)).asInstanceOf[LLVMValue]
    val rhs = visit(ctx.value(1)).asInstanceOf[LLVMValue]
    val ofFlags = visit(ctx.overflowFlags).asInstanceOf[OverflowFlagList].fs
    // Skipped optCommaSepMetadataAttachmentList
    AddInst(ty, lhs, rhs, ofFlags)
  }

  override def visitMulInst(ctx: LLVMParser.MulInstContext): LAST = {
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val lhs = visit(ctx.value(0)).asInstanceOf[LLVMValue]
    val rhs = visit(ctx.value(1)).asInstanceOf[LLVMValue]
    val ofFlags = visit(ctx.overflowFlags).asInstanceOf[OverflowFlagList].fs
    // Skipped optCommaSepMetadataAttachmentList
    MulInst(ty, lhs, rhs, ofFlags)
  }

  override def visitSubInst(ctx: LLVMParser.SubInstContext): LAST = {
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val lhs = visit(ctx.value(0)).asInstanceOf[LLVMValue]
    val rhs = visit(ctx.value(1)).asInstanceOf[LLVMValue]
    val ofFlags = visit(ctx.overflowFlags).asInstanceOf[OverflowFlagList].fs
    // Skipped optCommaSepMetadataAttachmentList
    SubInst(ty, lhs, rhs, ofFlags)
  }

  override def visitFAddInst(ctx: LLVMParser.FAddInstContext): LAST = {
    // Skipped FastMathFlagsContext
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val lhs = visit(ctx.value(0)).asInstanceOf[LLVMValue]
    val rhs = visit(ctx.value(1)).asInstanceOf[LLVMValue]
    // Skipped optCommaSepMetadataAttachmentList
    FAddInst(ty, lhs, rhs)
  }

  override def visitFSubInst(ctx: LLVMParser.FSubInstContext): LAST = {
    // Skipped FastMathFlagsContext
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val lhs = visit(ctx.value(0)).asInstanceOf[LLVMValue]
    val rhs = visit(ctx.value(1)).asInstanceOf[LLVMValue]
    // Skipped optCommaSepMetadataAttachmentList
    FSubInst(ty, lhs, rhs)
  }

  override def visitFMulInst(ctx: LLVMParser.FMulInstContext): LAST = {
    // Skipped FastMathFlagsContext
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val lhs = visit(ctx.value(0)).asInstanceOf[LLVMValue]
    val rhs = visit(ctx.value(1)).asInstanceOf[LLVMValue]
    // Skipped optCommaSepMetadataAttachmentList
    FMulInst(ty, lhs, rhs)
  }

  override def visitUDivInst(ctx: LLVMParser.UDivInstContext): LAST = {
    // Skipped OptExactContext
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val lhs = visit(ctx.value(0)).asInstanceOf[LLVMValue]
    val rhs = visit(ctx.value(1)).asInstanceOf[LLVMValue]
    // Skipped OptCommaSepMetadataAttachmentListContext
    UDivInst(ty, lhs, rhs)
  }

  override def visitSDivInst(ctx: LLVMParser.SDivInstContext): LAST = {
    // Skipped OptExactContext
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val lhs = visit(ctx.value(0)).asInstanceOf[LLVMValue]
    val rhs = visit(ctx.value(1)).asInstanceOf[LLVMValue]
    // OptCommaSepMetadataAttachmentListContext
    SDivInst(ty, lhs, rhs)
  }

  override def visitFDivInst(ctx: LLVMParser.FDivInstContext): LAST = {
    // Skipped FastMathFlagsContext
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val lhs = visit(ctx.value(0)).asInstanceOf[LLVMValue]
    val rhs = visit(ctx.value(1)).asInstanceOf[LLVMValue]
    // Skipped OptCommaSepMetadataAttachmentListContext
    SDivInst(ty, lhs, rhs)
  }

  override def visitURemInst(ctx: LLVMParser.URemInstContext): LAST = {
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val lhs = visit(ctx.value(0)).asInstanceOf[LLVMValue]
    val rhs = visit(ctx.value(1)).asInstanceOf[LLVMValue]
    // Skipped OptCommaSepMetadataAttachmentListContext
    URemInst(ty, lhs, rhs)
  }

  override def visitSRemInst(ctx: LLVMParser.SRemInstContext): LAST = {
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val lhs = visit(ctx.value(0)).asInstanceOf[LLVMValue]
    val rhs = visit(ctx.value(1)).asInstanceOf[LLVMValue]
    // Skipped OptCommaSepMetadataAttachmentListContext
    SRemInst(ty, lhs, rhs)
  }

  override def visitShlInst(ctx: LLVMParser.ShlInstContext): LAST = {
    // Skipped OverflowFlagsContext
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val lhs = visit(ctx.value(0)).asInstanceOf[LLVMValue]
    val rhs = visit(ctx.value(1)).asInstanceOf[LLVMValue]
    // Skipped OptCommaSepMetadataAttachmentListContext
    ShlInst(ty, lhs, rhs)
  }

  override def visitLshrInst(ctx: LLVMParser.LshrInstContext): LAST = {
    // Skipped OptExactContext
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val lhs = visit(ctx.value(0)).asInstanceOf[LLVMValue]
    val rhs = visit(ctx.value(1)).asInstanceOf[LLVMValue]
    // Skipped OptCommaSepMetadataAttachmentListContext
    LshrInst(ty, lhs, rhs)
  }

  override def visitAshrInst(ctx: LLVMParser.AshrInstContext): LAST = {
    // Skipped OptExactContext
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val lhs = visit(ctx.value(0)).asInstanceOf[LLVMValue]
    val rhs = visit(ctx.value(1)).asInstanceOf[LLVMValue]
    // Skipped OptCommaSepMetadataAttachmentListContext
    AshrInst(ty, lhs, rhs)
  }

  override def visitAndInst(ctx: LLVMParser.AndInstContext): LAST = {
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val lhs = visit(ctx.value(0)).asInstanceOf[LLVMValue]
    val rhs = visit(ctx.value(1)).asInstanceOf[LLVMValue]
    // Skipped OptCommaSepMetadataAttachmentListContext
    AndInst(ty, lhs, rhs)
  }

  override def visitOrInst(ctx: LLVMParser.OrInstContext): LAST = {
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val lhs = visit(ctx.value(0)).asInstanceOf[LLVMValue]
    val rhs = visit(ctx.value(1)).asInstanceOf[LLVMValue]
    // Skipped OptCommaSepMetadataAttachmentListContext
    OrInst(ty, lhs, rhs)
  }

  override def visitXorInst(ctx: LLVMParser.XorInstContext): LAST = {
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val lhs = visit(ctx.value(0)).asInstanceOf[LLVMValue]
    val rhs = visit(ctx.value(1)).asInstanceOf[LLVMValue]
    // Skipped OptCommaSepMetadataAttachmentListContext
    XorInst(ty, lhs, rhs)
  }

  override def visitExtractValueInst(ctx: LLVMParser.ExtractValueInstContext): LAST = {
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val extractVal = visit(ctx.value).asInstanceOf[LLVMValue]
    val constList = visit(ctx.indexList).asInstanceOf[ConstList].cs
    ExtractValueInst(ty, extractVal, constList)
  }

  override def visitIndexList(ctx: LLVMParser.IndexListContext): LAST = {
    val indice = visit(ctx.index).asInstanceOf[Constant]
    if (ctx.indexList == null) {
      ConstList(List(indice))
    } else {
      val restOfList = visit(ctx.indexList).asInstanceOf[ConstList]
      ConstList(indice +: restOfList.cs)
    }
  }

  override def visitIndex(ctx: LLVMParser.IndexContext): LAST = {
    IntConst(ctx.INT_LIT.getText.toInt)
  }



  override def visitAllocaInst(ctx: LLVMParser.AllocaInstContext): LAST = {
    // Only support the simplest form:
    //'alloca' llvmType ',' alignment
    val ty = visit(ctx.llvmType(0)).asInstanceOf[LLVMType]
    val align = visit(ctx.alignment).asInstanceOf[Alignment]
    AllocaInst(ty, align)
  }

  override def visitLoadInst(ctx: LLVMParser.LoadInstContext): LAST = {
    // Only support the simplest form:
    // 'load' llvmType ',' llvmType value ',' alignment
    val valTy = visit(ctx.llvmType(0)).asInstanceOf[LLVMType]
    val ptrTy = visit(ctx.llvmType(1)).asInstanceOf[LLVMType]
    val ptrVal = visit(ctx.value).asInstanceOf[LLVMValue]
    val align = visit(ctx.alignment).asInstanceOf[Alignment]
    LoadInst(valTy, ptrTy, ptrVal, align)
  }

  override def visitGetElementPtrInst(ctx: LLVMParser.GetElementPtrInstContext): LAST = {
    // Skipped optCommaSepMetadataAttachmentList
    val inBounds = ctx.optInBounds.INBOUNDS != null
    val baseTy = visit(ctx.llvmType(0)).asInstanceOf[LLVMType]
    val ptrTy = visit(ctx.llvmType(1)).asInstanceOf[LLVMType]
    val ptrVal = visit(ctx.value).asInstanceOf[LLVMValue]
    val typedValues = visit(ctx.commaSepTypeValueList).asInstanceOf[TypedValueList].tvs
    GetElemPtrInst(inBounds, baseTy, ptrTy, ptrVal, typedValues)
  }

  override def visitBitCastInst(ctx: LLVMParser.BitCastInstContext): LAST = {
    val from = visit(ctx.llvmType(0)).asInstanceOf[LLVMType]
    val value = visit(ctx.value).asInstanceOf[LLVMValue]
    val to = visit(ctx.llvmType(1)).asInstanceOf[LLVMType]
    BitCastInst(from, value, to)
  }

  override def visitICmpInst(ctx: LLVMParser.ICmpInstContext): LAST = {
    // Skipped optCommaSepMetadataAttachmentList
    // 'icmp' iPred llvmType value ',' value
    val pred = visit(ctx.iPred).asInstanceOf[IPredicate]
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val lhs = visit(ctx.value(0)).asInstanceOf[LLVMValue]
    val rhs = visit(ctx.value(1)).asInstanceOf[LLVMValue]
    ICmpInst(pred, ty, lhs, rhs)
  }

  override def visitFCmpInst(ctx: LLVMParser.FCmpInstContext): LAST = {
    // Skipped optCommaSepMetadataAttachmentList
    // 'fcmp' iPred llvmType value ',' value
    val pred = visit(ctx.fpred).asInstanceOf[FPredicate]
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val lhs = visit(ctx.value(0)).asInstanceOf[LLVMValue]
    val rhs = visit(ctx.value(1)).asInstanceOf[LLVMValue]
    FCmpInst(pred, ty, lhs, rhs)
  }

  override def visitTruncInst(ctx: LLVMParser.TruncInstContext): LAST = {
    val from = visit(ctx.llvmType(0)).asInstanceOf[LLVMType]
    val to = visit(ctx.llvmType(1)).asInstanceOf[LLVMType]
    val value = visit(ctx.value).asInstanceOf[LLVMValue]
    // Skipped optCommaSepMetadataAttachmentList
    TruncInst(from, value, to)
  }

  override def visitZExtInst(ctx: LLVMParser.ZExtInstContext): LAST = {
    // Skipped optCommaSepMetadataAttachmentList
    val from = visit(ctx.llvmType(0)).asInstanceOf[LLVMType]
    val to = visit(ctx.llvmType(1)).asInstanceOf[LLVMType]
    val value = visit(ctx.value).asInstanceOf[LLVMValue]
    ZExtInst(from, value, to)
  }

  override def visitSExtInst(ctx: LLVMParser.SExtInstContext): LAST = {
    // Skipped optCommaSepMetadataAttachmentList
    val from = visit(ctx.llvmType(0)).asInstanceOf[LLVMType]
    val to = visit(ctx.llvmType(1)).asInstanceOf[LLVMType]
    val value = visit(ctx.value).asInstanceOf[LLVMValue]
    SExtInst(from, value, to)
  }

  override def visitFpExtInst(ctx: LLVMParser.FpExtInstContext): LAST = {
    // Skipped optCommaSepMetadataAttachmentList
    val from = visit(ctx.llvmType(0)).asInstanceOf[LLVMType]
    val to = visit(ctx.llvmType(1)).asInstanceOf[LLVMType]
    val value = visit(ctx.value).asInstanceOf[LLVMValue]
    FpExtInst(from, value, to)
  }

  override def visitFpToUIInst(ctx: LLVMParser.FpToUIInstContext): LAST = {
    // Skipped optCommaSepMetadataAttachmentList
    val from = visit(ctx.llvmType(0)).asInstanceOf[LLVMType]
    val to = visit(ctx.llvmType(1)).asInstanceOf[LLVMType]
    val value = visit(ctx.value).asInstanceOf[LLVMValue]
    FpToUIInst(from, value, to)
  }

  override def visitFpToSIInst(ctx: LLVMParser.FpToSIInstContext): LAST = {
    // Skipped optCommaSepMetadataAttachmentList
    val from = visit(ctx.llvmType(0)).asInstanceOf[LLVMType]
    val to = visit(ctx.llvmType(1)).asInstanceOf[LLVMType]
    val value = visit(ctx.value).asInstanceOf[LLVMValue]
    FpToSIInst(from, value, to)
  }

  override def visitUiToFPInst(ctx: LLVMParser.UiToFPInstContext): LAST = {
    // Skipped optCommaSepMetadataAttachmentList
    val from = visit(ctx.llvmType(0)).asInstanceOf[LLVMType]
    val to = visit(ctx.llvmType(1)).asInstanceOf[LLVMType]
    val value = visit(ctx.value).asInstanceOf[LLVMValue]
    UiToFPInst(from, value, to)
  }

  override def visitSiToFPInst(ctx: LLVMParser.SiToFPInstContext): LAST = {
    // Skipped optCommaSepMetadataAttachmentList
    val from = visit(ctx.llvmType(0)).asInstanceOf[LLVMType]
    val to = visit(ctx.llvmType(1)).asInstanceOf[LLVMType]
    val value = visit(ctx.value).asInstanceOf[LLVMValue]
    SiToFPInst(from, value, to)
  }

  override def visitPtrToIntInst(ctx: LLVMParser.PtrToIntInstContext): LAST = {
    // Skipped optCommaSepMetadataAttachmentList
    val from = visit(ctx.llvmType(0)).asInstanceOf[LLVMType]
    val to = visit(ctx.llvmType(1)).asInstanceOf[LLVMType]
    val value = visit(ctx.value).asInstanceOf[LLVMValue]
    PtrToIntInst(from, value, to)
  }

  override def visitIntToPtrInst(ctx: LLVMParser.IntToPtrInstContext): LAST = {
    // Skipped optCommaSepMetadataAttachmentList
    val from = visit(ctx.llvmType(0)).asInstanceOf[LLVMType]
    val to = visit(ctx.llvmType(1)).asInstanceOf[LLVMType]
    val value = visit(ctx.value).asInstanceOf[LLVMValue]
    IntToPtrInst(from, value, to)
  }

  override def visitCallInst(ctx: LLVMParser.CallInstContext): LAST = {
    // Handles 'call' llvmType value '(' args ')'
    // Note: llvmType can be just the return type, or the whole function type of the callee
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val f = visit(ctx.value).asInstanceOf[LLVMValue]
    val args = visit(ctx.args).asInstanceOf[ArgList].as
    CallInst(ty, f, args)
  }

  override def visitPhiInst(ctx: LLVMParser.PhiInstContext): LAST = {
    // Skipped optCommaSepMetadataAttachmentList
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val incs = visit(ctx.incList).asInstanceOf[IncList].incs
    PhiInst(ty, incs)
  }

  override def visitSelectInst(ctx: LLVMParser.SelectInstContext): LAST = {
    // Skipped optCommaSepMetadataAttachmentList
    val cndTy = visit(ctx.llvmType(0)).asInstanceOf[LLVMType]
    val cndVal = visit(ctx.value(0)).asInstanceOf[LLVMValue]
    val thnTy = visit(ctx.llvmType(1)).asInstanceOf[LLVMType]
    val thnVal = visit(ctx.value(1)).asInstanceOf[LLVMValue]
    val elsTy = visit(ctx.llvmType(2)).asInstanceOf[LLVMType]
    val elsVal = visit(ctx.value(2)).asInstanceOf[LLVMValue]
    SelectInst(cndTy, cndVal, thnTy, thnVal, elsTy, elsVal)
  }

  override def visitStoreInst(ctx: LLVMParser.StoreInstContext): LAST = {
    // Only support the simplest form, for now:
    //   'store' llvmType value ',' llvmType value ',' alignment
    val valType = visit(ctx.llvmType(0)).asInstanceOf[LLVMType]
    val valValue = visit(ctx.value(0)).asInstanceOf[LLVMValue]
    val addrType = visit(ctx.llvmType(1)).asInstanceOf[LLVMType]
    val addrValue = visit(ctx.value(1)).asInstanceOf[LLVMValue]
    val align = visit(ctx.alignment).asInstanceOf[Alignment]
    StoreInst(valType, valValue, addrType, addrValue, align)
  }

  override def visitFenceInst(ctx: LLVMParser.FenceInstContext): LAST = {
    ???
  }

  override def visitCmpXchgInst(ctx: LLVMParser.CmpXchgInstContext): LAST = {
    ???
  }

  override def visitAtomicRMWInst(ctx: LLVMParser.AtomicRMWInstContext): LAST = {
    ???
  }

  override def visitTerminator(ctx: LLVMParser.TerminatorContext): LAST = {
    super.visitTerminator(ctx)
  }

  override def visitRetTerm(ctx: LLVMParser.RetTermContext): LAST = {
    // Skipped optCommaSepMetadataAttachmentList
    if (ctx.voidType != null) {
      RetTerm(VoidType, None)
    } else {
      val ty =
        if (ctx.llvmType != null && ctx.optAddrSpace != null && ctx.STAR != null) {
          val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
          val addrSpace =
            if (ctx.optAddrSpace.addrSpace != null)
              Some(visit(ctx.optAddrSpace.addrSpace).asInstanceOf[AddrSpace])
            else None
          PtrType(ty, addrSpace)
        } else {
          visit(ctx.concreteNonRecType).asInstanceOf[LLVMType]
        }
      val value = visit(ctx.value).asInstanceOf[LLVMValue]
      RetTerm(ty, Some(value))
    }
  }

  override def visitBrTerm(ctx: LLVMParser.BrTermContext): LAST = {
    // Skipped optCommaSepMetadataAttachmentList
    val lab = ctx.localIdent.LOCAL_IDENT.getText
    BrTerm(lab)
  }

  override def visitCondBrTerm(ctx: LLVMParser.CondBrTermContext): LAST = {
    val intTy = visit(ctx.intType).asInstanceOf[IntType]
    val cnd = visit(ctx.value).asInstanceOf[LLVMValue]
    val thn = ctx.localIdent(0).LOCAL_IDENT.getText
    val els = ctx.localIdent(1).LOCAL_IDENT.getText
    CondBrTerm(intTy, cnd, thn, els)
  }

  override def visitSwitchTerm(ctx: LLVMParser.SwitchTermContext): LAST = {
    // Skipped optCommaSepMetadataAttachmentList
    val cndTy = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val cndVal = visit(ctx.value).asInstanceOf[LLVMValue]
    val default = ctx.localIdent.LOCAL_IDENT.getText
    val cases = visit(ctx.cases).asInstanceOf[CaseList].cs
    SwitchTerm(cndTy, cndVal, default, cases)
  }

  override def visitCases(ctx: LLVMParser.CasesContext): LAST = {
    if (ctx.caseList == null) CaseList(List())
    else visit(ctx.caseList)
  }

  override def visitCaseList(ctx: LLVMParser.CaseListContext): LAST = {
    val c = visit(ctx.llvmCase).asInstanceOf[LLVMCase]
    if (ctx.caseList == null)
      CaseList(List(c))
    else {
      val cs = visit(ctx.caseList).asInstanceOf[CaseList].cs
      CaseList(cs ++ List(c))
    }
  }

  override def visitLlvmCase(ctx: LLVMParser.LlvmCaseContext): LAST = {
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val n = visit(ctx.intConst).asInstanceOf[IntConst].n.toInt
    val lab = ctx.localIdent.LOCAL_IDENT.getText
    LLVMCase(ty, n, lab)
  }

  override def visitUnreachableTerm(ctx: LLVMParser.UnreachableTermContext): LAST = {
    // Skipped optCommaSepMetadataAttachmentList
    Unreachable
  }

  override def visitValue(ctx: LLVMParser.ValueContext): LAST = {
    if (ctx.localIdent != null) {
      LocalId(ctx.localIdent.LOCAL_IDENT.getText)
    } else if (ctx.constant != null) {
      visitConstant(ctx.constant)
    } else {
      // inlineAsm
      InlineASM()
    }
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

  override def visitOverflowFlags(ctx: LLVMParser.OverflowFlagsContext): LAST = {
    if (ctx.overflowFlagList == null) OverflowFlagList(List())
    else visit(ctx.overflowFlagList)
  }

  override def visitOverflowFlagList(ctx: LLVMParser.OverflowFlagListContext): LAST = {
    val a = visit(ctx.overflowFlag).asInstanceOf[OverflowFlag]
    if (ctx.overflowFlagList == null)
      OverflowFlagList(List(a))
    else {
      val as = visit(ctx.overflowFlagList).asInstanceOf[OverflowFlagList].fs
      OverflowFlagList(as ++ List(a))
    }
  }

  override def visitOverflowFlag(ctx: LLVMParser.OverflowFlagContext): LAST = {
    if (ctx.NSW != null) NSW
    else if (ctx.NUW != null) NUW
    else error
  }

  override def visitTypeValue(ctx: LLVMParser.TypeValueContext): LAST = {
    val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
    val value = visit(ctx.value).asInstanceOf[LLVMValue]
    TypedValue(ty, value)
  }

  override def visitCommaSepTypeValueList(ctx: LLVMParser.CommaSepTypeValueListContext): LAST = {
    val tv = visit(ctx.typeValue).asInstanceOf[TypedValue]
    if (ctx.commaSepTypeValueList == null) {
      TypedValueList(List(tv))
    } else {
      val tvs = visit(ctx.commaSepTypeValueList).asInstanceOf[TypedValueList].tvs
      TypedValueList(tvs ++ List(tv))
    }
  }

  override def visitIPred(ctx: LLVMParser.IPredContext): LAST = {
    if (ctx.EQ != null) IPredicate("eq")
    else if (ctx.NE != null) IPredicate("neq")
    else if (ctx.SLT != null) IPredicate("slt")
    else if (ctx.SLE != null) IPredicate("sle")
    else if (ctx.SGT != null) IPredicate("sgt")
    else if (ctx.SGE != null) IPredicate("sge")
    else if (ctx.ULT != null) IPredicate("ult")
    else if (ctx.ULE != null) IPredicate("ule")
    else if (ctx.UGT != null) IPredicate("ugt")
    else if (ctx.UGE != null) IPredicate("uge")
    else error
  }

  override def visitFpred(ctx: LLVMParser.FpredContext): LAST = {
    if (ctx.FALSE != null) FPredicate("false")
    else if (ctx.OEQ != null) FPredicate("oeq")
    else if (ctx.OGE != null) FPredicate("oge")
    else if (ctx.OGT != null) FPredicate("ogt")
    else if (ctx.OLE != null) FPredicate("ole")
    else if (ctx.OLT != null) FPredicate("olt")
    else if (ctx.ONE != null) FPredicate("one")
    else if (ctx.ORD != null) FPredicate("ord")
    else if (ctx.TRUE != null) FPredicate("true")
    else if (ctx.UEQ != null) FPredicate("ueq")
    else if (ctx.UGE != null) FPredicate("uge")
    else if (ctx.UGT != null) FPredicate("ugt")
    else if (ctx.ULE != null) FPredicate("ule")
    else if (ctx.ULT != null) FPredicate("ult")
    else if (ctx.UNE != null) FPredicate("une")
    else if (ctx.UNO != null) FPredicate("uno")
    else error
  }

  override def visitArgs(ctx: LLVMParser.ArgsContext): LAST = {
    if (ctx.argList != null) {
      val ps = visit(ctx.argList).asInstanceOf[ArgList]
      if (ctx.DOTS != null && ctx.COMMA != null) {
        ArgList(ps.as ++ List(Vararg))
      } else { ps }
    } else if (ctx.DOTS != null) {
      ArgList(List(Vararg))
    } else {
      ArgList(List())
    }
  }

  override def visitArgList(ctx: LLVMParser.ArgListContext): LAST = {
    val p = visit(ctx.arg).asInstanceOf[Arg]
    if (ctx.argList == null) {
      ArgList(List(p))
    } else {
      val ps = visit(ctx.argList).asInstanceOf[ArgList].as
      ArgList(ps ++ List(p))
    }
  }

  override def visitArg(ctx: LLVMParser.ArgContext): LAST = {
    if (ctx.metadataType != null) {
      ???
    } else {
      val ty =
        if (ctx.llvmType != null && ctx.optAddrSpace != null && ctx.STAR != null) {
          val ty = visit(ctx.llvmType).asInstanceOf[LLVMType]
          val addrSpace =
            if (ctx.optAddrSpace.addrSpace != null)
              Some(visit(ctx.optAddrSpace.addrSpace).asInstanceOf[AddrSpace])
            else None
          PtrType(ty, addrSpace)
        } else {
          visit(ctx.concreteNonRecType).asInstanceOf[LLVMType]
        }
      val attrs = visit(ctx.paramAttrs).asInstanceOf[ParamAttrList].as
      val value = visit(ctx.value).asInstanceOf[LLVMValue]
      TypedArg(ty, attrs, value)
    }
  }

  override def visitIncList(ctx: LLVMParser.IncListContext): LAST = {
    val inc = visit(ctx.inc).asInstanceOf[Inc]
    if (ctx.incList == null)
      IncList(List(inc))
    else {
      val incs = visit(ctx.incList).asInstanceOf[IncList].incs
      IncList(incs ++ List(inc))
    }
  }

  override def visitInc(ctx: LLVMParser.IncContext): LAST = {
    val value = visit(ctx.value).asInstanceOf[LLVMValue]
    val id = ctx.localIdent.LOCAL_IDENT.getText
    Inc(value, id)
  }

  override def visitFunctionDecl(ctx: LLVMParser.FunctionDeclContext): LAST = {
    // Skipped metadataAttachments and optExternLinkage
    val header = visit(ctx.functionHeader).asInstanceOf[FunctionHeader]
    val id = header.globalId
    FunctionDecl(id, header)
  }
}

package parser {
  object Parser {
    def parse(input: String): Module = {
      val charStream = new ANTLRInputStream(input)
      val lexer = new LLVMLexer(charStream)
      val tokens = new CommonTokenStream(lexer)
      val parser = new LLVMParser(tokens)

      val visitor = new MyVisitor()
      val res: Module  = visitor.visit(parser.module).asInstanceOf[Module]
      res
    }

    def parseFile(filepath: String): Module = {
      val input = scala.io.Source.fromFile(filepath).mkString
      val m = parse(input)
      m.mname = filepath.substring(filepath.lastIndexOf("/") + 1)
      m
    }
  }

  object TestParser extends App {
    import PPrinter._
    import Parser._
    //printAst(parseFile("/home/shangyint/research/sai/dev-clean/external/strcmp.ll"))

    printAst(parseFile("benchmarks/llscExpr/trunc.ll"))
    //printAst(parseFile("benchmarks/coreutils/libc7.ll"))
  }

  object PPrinter {
    def printBB(bb: BB): Unit = {
      println("  Block: ")
      println(s"    Label: ${bb.label}")
      println()
      println("    Inst:")
      bb.ins.foreach(u => println(s"      ${u}"))
      println()
      println("    Term:")
      println(s"      ${bb.term}")
      println()
      println()
    }

    def printAst(m: Module): Unit = {
      m.es foreach {u => u match {
        case FunctionDef(id, linkage, metadata, header, body) =>
          println(s"Fundef: id: ${id}; linkage: ${linkage}; metadata: ${metadata};\n FunctionHeader: ${header}")
          body.blocks foreach(printBB(_))
        case _ => println(u)
      }}
      println("------------------endofAST--------------------")
    }
  }
}
