package gensym.llvm;
// Generated from LLVMParser.g4 by ANTLR 4.12.0
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LLVMParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LLVMParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LLVMParser#module}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule(LLVMParser.ModuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#topLevelEntities}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTopLevelEntities(LLVMParser.TopLevelEntitiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#topLevelEntityList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTopLevelEntityList(LLVMParser.TopLevelEntityListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#topLevelEntity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTopLevelEntity(LLVMParser.TopLevelEntityContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#sourceFilename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceFilename(LLVMParser.SourceFilenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#targetDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetDefinition(LLVMParser.TargetDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#moduleAsm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleAsm(LLVMParser.ModuleAsmContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#typeDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDef(LLVMParser.TypeDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#comdatDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComdatDef(LLVMParser.ComdatDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#selectionKind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectionKind(LLVMParser.SelectionKindContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#globalDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalDecl(LLVMParser.GlobalDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#globalDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalDef(LLVMParser.GlobalDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optExternallyInitialized}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptExternallyInitialized(LLVMParser.OptExternallyInitializedContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#immutable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImmutable(LLVMParser.ImmutableContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#globalAttrs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalAttrs(LLVMParser.GlobalAttrsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#globalAttrList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalAttrList(LLVMParser.GlobalAttrListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#globalAttr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalAttr(LLVMParser.GlobalAttrContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#indirectSymbolDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndirectSymbolDef(LLVMParser.IndirectSymbolDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlias(LLVMParser.AliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#functionDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDecl(LLVMParser.FunctionDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#functionDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDef(LLVMParser.FunctionDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#functionHeader}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionHeader(LLVMParser.FunctionHeaderContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optGC}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptGC(LLVMParser.OptGCContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optPrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptPrefix(LLVMParser.OptPrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optPrologue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptPrologue(LLVMParser.OptPrologueContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optPersonality}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptPersonality(LLVMParser.OptPersonalityContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#functionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionBody(LLVMParser.FunctionBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#attrGroupDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttrGroupDef(LLVMParser.AttrGroupDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#namedMetadataDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedMetadataDef(LLVMParser.NamedMetadataDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#metadataNodes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataNodes(LLVMParser.MetadataNodesContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#metadataNodeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataNodeList(LLVMParser.MetadataNodeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#metadataNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataNode(LLVMParser.MetadataNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#metadataDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataDef(LLVMParser.MetadataDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optDistinct}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptDistinct(LLVMParser.OptDistinctContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#useListOrders}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUseListOrders(LLVMParser.UseListOrdersContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#useListOrderList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUseListOrderList(LLVMParser.UseListOrderListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#useListOrder}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUseListOrder(LLVMParser.UseListOrderContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#useListOrderBB}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUseListOrderBB(LLVMParser.UseListOrderBBContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#globalIdent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalIdent(LLVMParser.GlobalIdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#localIdent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalIdent(LLVMParser.LocalIdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#labelIdent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabelIdent(LLVMParser.LabelIdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#attrGroupID}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttrGroupID(LLVMParser.AttrGroupIDContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#comdatName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComdatName(LLVMParser.ComdatNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#metadataName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataName(LLVMParser.MetadataNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#metadataID}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataID(LLVMParser.MetadataIDContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#llvmType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLlvmType(LLVMParser.LlvmTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#concreteNonRecType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcreteNonRecType(LLVMParser.ConcreteNonRecTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#voidType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVoidType(LLVMParser.VoidTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#intType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntType(LLVMParser.IntTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#floatType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatType(LLVMParser.FloatTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#floatKind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatKind(LLVMParser.FloatKindContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#mmxType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMmxType(LLVMParser.MmxTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optAddrSpace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptAddrSpace(LLVMParser.OptAddrSpaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#addrSpace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddrSpace(LLVMParser.AddrSpaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#vectorType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVectorType(LLVMParser.VectorTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#labelType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabelType(LLVMParser.LabelTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#tokenType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTokenType(LLVMParser.TokenTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#metadataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataType(LLVMParser.MetadataTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#arrayType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayType(LLVMParser.ArrayTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#structType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructType(LLVMParser.StructTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#typeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeList(LLVMParser.TypeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#opaqueType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpaqueType(LLVMParser.OpaqueTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#namedType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedType(LLVMParser.NamedTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(LLVMParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#inlineAsm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInlineAsm(LLVMParser.InlineAsmContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optSideEffect}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptSideEffect(LLVMParser.OptSideEffectContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optAlignStack}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptAlignStack(LLVMParser.OptAlignStackContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optIntelDialect}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptIntelDialect(LLVMParser.OptIntelDialectContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(LLVMParser.ConstantContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#boolConst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolConst(LLVMParser.BoolConstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#boolLit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolLit(LLVMParser.BoolLitContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#intConst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntConst(LLVMParser.IntConstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#intLit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntLit(LLVMParser.IntLitContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#floatConst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatConst(LLVMParser.FloatConstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#nullConst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullConst(LLVMParser.NullConstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#noneConst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoneConst(LLVMParser.NoneConstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#structConst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructConst(LLVMParser.StructConstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#arrayConst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayConst(LLVMParser.ArrayConstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#charArrayConst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharArrayConst(LLVMParser.CharArrayConstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#stringLit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLit(LLVMParser.StringLitContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#vectorConst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVectorConst(LLVMParser.VectorConstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#zeroInitializerConst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZeroInitializerConst(LLVMParser.ZeroInitializerConstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#undefConst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUndefConst(LLVMParser.UndefConstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#blockAddressConst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockAddressConst(LLVMParser.BlockAddressConstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#constantExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantExpr(LLVMParser.ConstantExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#addExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddExpr(LLVMParser.AddExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fAddExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFAddExpr(LLVMParser.FAddExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#subExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubExpr(LLVMParser.SubExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fSubExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFSubExpr(LLVMParser.FSubExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#mulExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulExpr(LLVMParser.MulExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fMulExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFMulExpr(LLVMParser.FMulExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#uDivExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUDivExpr(LLVMParser.UDivExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#sDivExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSDivExpr(LLVMParser.SDivExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fDivExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFDivExpr(LLVMParser.FDivExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#uRemExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitURemExpr(LLVMParser.URemExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#sRemExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSRemExpr(LLVMParser.SRemExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fRemExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFRemExpr(LLVMParser.FRemExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#shlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShlExpr(LLVMParser.ShlExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#lShrExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLShrExpr(LLVMParser.LShrExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#ashrExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAshrExpr(LLVMParser.AshrExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#andExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(LLVMParser.AndExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#orExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpr(LLVMParser.OrExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#xorExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXorExpr(LLVMParser.XorExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#extractElementExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtractElementExpr(LLVMParser.ExtractElementExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#insertElementExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertElementExpr(LLVMParser.InsertElementExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#shuffleVectorExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShuffleVectorExpr(LLVMParser.ShuffleVectorExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#extractValueExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtractValueExpr(LLVMParser.ExtractValueExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#insertValueExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertValueExpr(LLVMParser.InsertValueExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#getElementPtrExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGetElementPtrExpr(LLVMParser.GetElementPtrExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#gepConstIndices}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGepConstIndices(LLVMParser.GepConstIndicesContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#gepConstIndexList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGepConstIndexList(LLVMParser.GepConstIndexListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#gepConstIndex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGepConstIndex(LLVMParser.GepConstIndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optInrange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptInrange(LLVMParser.OptInrangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#truncExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTruncExpr(LLVMParser.TruncExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#zExtExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZExtExpr(LLVMParser.ZExtExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#sExtExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSExtExpr(LLVMParser.SExtExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fPTruncExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFPTruncExpr(LLVMParser.FPTruncExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fpExtExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFpExtExpr(LLVMParser.FpExtExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fpToUIExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFpToUIExpr(LLVMParser.FpToUIExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fpToSIExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFpToSIExpr(LLVMParser.FpToSIExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#uiToFPExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUiToFPExpr(LLVMParser.UiToFPExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#siToFPExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSiToFPExpr(LLVMParser.SiToFPExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#ptrToIntExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPtrToIntExpr(LLVMParser.PtrToIntExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#intToPtrExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntToPtrExpr(LLVMParser.IntToPtrExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#bitCastExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitCastExpr(LLVMParser.BitCastExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#addrSpaceCastExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddrSpaceCastExpr(LLVMParser.AddrSpaceCastExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#iCmpExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitICmpExpr(LLVMParser.ICmpExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fCmpExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFCmpExpr(LLVMParser.FCmpExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#selectExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectExpr(LLVMParser.SelectExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#basicBlockList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasicBlockList(LLVMParser.BasicBlockListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#basicBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasicBlock(LLVMParser.BasicBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optLabelIdent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptLabelIdent(LLVMParser.OptLabelIdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#instructions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstructions(LLVMParser.InstructionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#instructionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstructionList(LLVMParser.InstructionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstruction(LLVMParser.InstructionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#valueInstruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueInstruction(LLVMParser.ValueInstructionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fNegInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFNegInst(LLVMParser.FNegInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#addInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddInst(LLVMParser.AddInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fAddInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFAddInst(LLVMParser.FAddInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#subInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubInst(LLVMParser.SubInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fSubInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFSubInst(LLVMParser.FSubInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#mulInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulInst(LLVMParser.MulInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fMulInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFMulInst(LLVMParser.FMulInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#uDivInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUDivInst(LLVMParser.UDivInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#sDivInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSDivInst(LLVMParser.SDivInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fDivInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFDivInst(LLVMParser.FDivInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#uRemInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitURemInst(LLVMParser.URemInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#sRemInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSRemInst(LLVMParser.SRemInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fRemInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFRemInst(LLVMParser.FRemInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#shlInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShlInst(LLVMParser.ShlInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#lshrInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLshrInst(LLVMParser.LshrInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#ashrInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAshrInst(LLVMParser.AshrInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#andInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndInst(LLVMParser.AndInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#orInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrInst(LLVMParser.OrInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#xorInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXorInst(LLVMParser.XorInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#extractElementInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtractElementInst(LLVMParser.ExtractElementInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#insertElementInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertElementInst(LLVMParser.InsertElementInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#shuffleVectorInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShuffleVectorInst(LLVMParser.ShuffleVectorInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#extractValueInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtractValueInst(LLVMParser.ExtractValueInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#insertValueInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertValueInst(LLVMParser.InsertValueInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#allocaInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllocaInst(LLVMParser.AllocaInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optInAlloca}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptInAlloca(LLVMParser.OptInAllocaContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optSwiftError}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptSwiftError(LLVMParser.OptSwiftErrorContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#loadInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoadInst(LLVMParser.LoadInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#storeInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStoreInst(LLVMParser.StoreInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fenceInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFenceInst(LLVMParser.FenceInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#cmpXchgInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmpXchgInst(LLVMParser.CmpXchgInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optWeak}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptWeak(LLVMParser.OptWeakContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#atomicRMWInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomicRMWInst(LLVMParser.AtomicRMWInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#binOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinOp(LLVMParser.BinOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#getElementPtrInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGetElementPtrInst(LLVMParser.GetElementPtrInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#truncInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTruncInst(LLVMParser.TruncInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#zExtInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZExtInst(LLVMParser.ZExtInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#sExtInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSExtInst(LLVMParser.SExtInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fpTruncInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFpTruncInst(LLVMParser.FpTruncInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fpExtInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFpExtInst(LLVMParser.FpExtInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fpToUIInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFpToUIInst(LLVMParser.FpToUIInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fpToSIInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFpToSIInst(LLVMParser.FpToSIInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#uiToFPInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUiToFPInst(LLVMParser.UiToFPInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#siToFPInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSiToFPInst(LLVMParser.SiToFPInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#ptrToIntInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPtrToIntInst(LLVMParser.PtrToIntInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#intToPtrInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntToPtrInst(LLVMParser.IntToPtrInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#bitCastInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitCastInst(LLVMParser.BitCastInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#addrSpaceCastInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddrSpaceCastInst(LLVMParser.AddrSpaceCastInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#iCmpInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitICmpInst(LLVMParser.ICmpInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fCmpInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFCmpInst(LLVMParser.FCmpInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#phiInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPhiInst(LLVMParser.PhiInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#incList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncList(LLVMParser.IncListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#inc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInc(LLVMParser.IncContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#selectInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectInst(LLVMParser.SelectInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#callInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallInst(LLVMParser.CallInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optTail}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptTail(LLVMParser.OptTailContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#vaArgInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVaArgInst(LLVMParser.VaArgInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#landingPadInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLandingPadInst(LLVMParser.LandingPadInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optCleanup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptCleanup(LLVMParser.OptCleanupContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClauses(LLVMParser.ClausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#clauseList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClauseList(LLVMParser.ClauseListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClause(LLVMParser.ClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#catchPadInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatchPadInst(LLVMParser.CatchPadInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#cleanupPadInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCleanupPadInst(LLVMParser.CleanupPadInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#terminator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerminator(LLVMParser.TerminatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#retTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRetTerm(LLVMParser.RetTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#brTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBrTerm(LLVMParser.BrTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#condBrTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondBrTerm(LLVMParser.CondBrTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#switchTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchTerm(LLVMParser.SwitchTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#cases}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCases(LLVMParser.CasesContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#caseList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseList(LLVMParser.CaseListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#llvmCase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLlvmCase(LLVMParser.LlvmCaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#indirectBrTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndirectBrTerm(LLVMParser.IndirectBrTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#labelList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabelList(LLVMParser.LabelListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#label}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel(LLVMParser.LabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#invokeTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvokeTerm(LLVMParser.InvokeTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#resumeTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResumeTerm(LLVMParser.ResumeTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#catchSwitchTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatchSwitchTerm(LLVMParser.CatchSwitchTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#catchRetTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatchRetTerm(LLVMParser.CatchRetTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#cleanupRetTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCleanupRetTerm(LLVMParser.CleanupRetTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#unreachableTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnreachableTerm(LLVMParser.UnreachableTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#unwindTarget}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnwindTarget(LLVMParser.UnwindTargetContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#mdTuple}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMdTuple(LLVMParser.MdTupleContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#mdFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMdFields(LLVMParser.MdFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#mdFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMdFieldList(LLVMParser.MdFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#mdField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMdField(LLVMParser.MdFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#metadata}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadata(LLVMParser.MetadataContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#mdString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMdString(LLVMParser.MdStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#metadataAttachment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataAttachment(LLVMParser.MetadataAttachmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#mdNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMdNode(LLVMParser.MdNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#metadataAttachments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataAttachments(LLVMParser.MetadataAttachmentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#metadataAttachmentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataAttachmentList(LLVMParser.MetadataAttachmentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optCommaSepMetadataAttachmentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptCommaSepMetadataAttachmentList(LLVMParser.OptCommaSepMetadataAttachmentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#commaSepMetadataAttachmentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommaSepMetadataAttachmentList(LLVMParser.CommaSepMetadataAttachmentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#specializedMDNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecializedMDNode(LLVMParser.SpecializedMDNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diCompileUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiCompileUnit(LLVMParser.DiCompileUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diCompileUnitFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiCompileUnitFields(LLVMParser.DiCompileUnitFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diCompileUnitFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiCompileUnitFieldList(LLVMParser.DiCompileUnitFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diCompileUnitField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiCompileUnitField(LLVMParser.DiCompileUnitFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diFile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiFile(LLVMParser.DiFileContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diFileFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiFileFields(LLVMParser.DiFileFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diFileFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiFileFieldList(LLVMParser.DiFileFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diFileField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiFileField(LLVMParser.DiFileFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diBasicType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiBasicType(LLVMParser.DiBasicTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diBasicTypeFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiBasicTypeFields(LLVMParser.DiBasicTypeFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diBasicTypeFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiBasicTypeFieldList(LLVMParser.DiBasicTypeFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diBasicTypeField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiBasicTypeField(LLVMParser.DiBasicTypeFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diSubroutineType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiSubroutineType(LLVMParser.DiSubroutineTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diSubroutineTypeFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiSubroutineTypeFields(LLVMParser.DiSubroutineTypeFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diSubroutineTypeFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiSubroutineTypeFieldList(LLVMParser.DiSubroutineTypeFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diSubroutineTypeField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiSubroutineTypeField(LLVMParser.DiSubroutineTypeFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diDerivedType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiDerivedType(LLVMParser.DiDerivedTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diDerivedTypeFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiDerivedTypeFields(LLVMParser.DiDerivedTypeFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diDerivedTypeFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiDerivedTypeFieldList(LLVMParser.DiDerivedTypeFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diDerivedTypeField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiDerivedTypeField(LLVMParser.DiDerivedTypeFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diCompositeType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiCompositeType(LLVMParser.DiCompositeTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diCompositeTypeFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiCompositeTypeFields(LLVMParser.DiCompositeTypeFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diCompositeTypeFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiCompositeTypeFieldList(LLVMParser.DiCompositeTypeFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diCompositeTypeField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiCompositeTypeField(LLVMParser.DiCompositeTypeFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diSubrange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiSubrange(LLVMParser.DiSubrangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diSubrangeFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiSubrangeFields(LLVMParser.DiSubrangeFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diSubrangeFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiSubrangeFieldList(LLVMParser.DiSubrangeFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diSubrangeField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiSubrangeField(LLVMParser.DiSubrangeFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diEnumerator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiEnumerator(LLVMParser.DiEnumeratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diEnumeratorFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiEnumeratorFields(LLVMParser.DiEnumeratorFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diEnumeratorFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiEnumeratorFieldList(LLVMParser.DiEnumeratorFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diEnumeratorField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiEnumeratorField(LLVMParser.DiEnumeratorFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diTemplateTypeParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiTemplateTypeParameter(LLVMParser.DiTemplateTypeParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diTemplateTypeParameterFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiTemplateTypeParameterFields(LLVMParser.DiTemplateTypeParameterFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diTemplateTypeParameterFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiTemplateTypeParameterFieldList(LLVMParser.DiTemplateTypeParameterFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diTemplateTypeParameterField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiTemplateTypeParameterField(LLVMParser.DiTemplateTypeParameterFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diTemplateValueParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiTemplateValueParameter(LLVMParser.DiTemplateValueParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diTemplateValueParameterFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiTemplateValueParameterFields(LLVMParser.DiTemplateValueParameterFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diTemplateValueParameterFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiTemplateValueParameterFieldList(LLVMParser.DiTemplateValueParameterFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diTemplateValueParameterField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiTemplateValueParameterField(LLVMParser.DiTemplateValueParameterFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diModule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiModule(LLVMParser.DiModuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diModuleFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiModuleFields(LLVMParser.DiModuleFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diModuleFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiModuleFieldList(LLVMParser.DiModuleFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diModuleField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiModuleField(LLVMParser.DiModuleFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diNamespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiNamespace(LLVMParser.DiNamespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diNamespaceFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiNamespaceFields(LLVMParser.DiNamespaceFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diNamespaceFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiNamespaceFieldList(LLVMParser.DiNamespaceFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diNamespaceField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiNamespaceField(LLVMParser.DiNamespaceFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diGlobalVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiGlobalVariable(LLVMParser.DiGlobalVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diGlobalVariableFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiGlobalVariableFields(LLVMParser.DiGlobalVariableFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diGlobalVariableFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiGlobalVariableFieldList(LLVMParser.DiGlobalVariableFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diGlobalVariableField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiGlobalVariableField(LLVMParser.DiGlobalVariableFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diSubprogram}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiSubprogram(LLVMParser.DiSubprogramContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diSubprogramFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiSubprogramFields(LLVMParser.DiSubprogramFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diSubprogramFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiSubprogramFieldList(LLVMParser.DiSubprogramFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diSubprogramField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiSubprogramField(LLVMParser.DiSubprogramFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diLexicalBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiLexicalBlock(LLVMParser.DiLexicalBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diLexicalBlockFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiLexicalBlockFields(LLVMParser.DiLexicalBlockFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diLexicalBlockFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiLexicalBlockFieldList(LLVMParser.DiLexicalBlockFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diLexicalBlockField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiLexicalBlockField(LLVMParser.DiLexicalBlockFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diLexicalBlockFile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiLexicalBlockFile(LLVMParser.DiLexicalBlockFileContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diLexicalBlockFileFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiLexicalBlockFileFields(LLVMParser.DiLexicalBlockFileFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diLexicalBlockFileFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiLexicalBlockFileFieldList(LLVMParser.DiLexicalBlockFileFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diLexicalBlockFileField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiLexicalBlockFileField(LLVMParser.DiLexicalBlockFileFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diLocation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiLocation(LLVMParser.DiLocationContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diLocationFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiLocationFields(LLVMParser.DiLocationFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diLocationFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiLocationFieldList(LLVMParser.DiLocationFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diLocationField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiLocationField(LLVMParser.DiLocationFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diLocalVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiLocalVariable(LLVMParser.DiLocalVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diLocalVariableFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiLocalVariableFields(LLVMParser.DiLocalVariableFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diLocalVariableFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiLocalVariableFieldList(LLVMParser.DiLocalVariableFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diLocalVariableField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiLocalVariableField(LLVMParser.DiLocalVariableFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiExpression(LLVMParser.DiExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diExpressionFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiExpressionFields(LLVMParser.DiExpressionFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diExpressionFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiExpressionFieldList(LLVMParser.DiExpressionFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diExpressionField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiExpressionField(LLVMParser.DiExpressionFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diGlobalVariableExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiGlobalVariableExpression(LLVMParser.DiGlobalVariableExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diGlobalVariableExpressionFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiGlobalVariableExpressionFields(LLVMParser.DiGlobalVariableExpressionFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diGlobalVariableExpressionFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiGlobalVariableExpressionFieldList(LLVMParser.DiGlobalVariableExpressionFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diGlobalVariableExpressionField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiGlobalVariableExpressionField(LLVMParser.DiGlobalVariableExpressionFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diObjCProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiObjCProperty(LLVMParser.DiObjCPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diObjCPropertyFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiObjCPropertyFields(LLVMParser.DiObjCPropertyFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diObjCPropertyFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiObjCPropertyFieldList(LLVMParser.DiObjCPropertyFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diObjCPropertyField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiObjCPropertyField(LLVMParser.DiObjCPropertyFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diImportedEntity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiImportedEntity(LLVMParser.DiImportedEntityContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diImportedEntityFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiImportedEntityFields(LLVMParser.DiImportedEntityFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diImportedEntityFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiImportedEntityFieldList(LLVMParser.DiImportedEntityFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diImportedEntityField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiImportedEntityField(LLVMParser.DiImportedEntityFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diMacro}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiMacro(LLVMParser.DiMacroContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diMacroFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiMacroFields(LLVMParser.DiMacroFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diMacroFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiMacroFieldList(LLVMParser.DiMacroFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diMacroField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiMacroField(LLVMParser.DiMacroFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diMacroFile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiMacroFile(LLVMParser.DiMacroFileContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diMacroFileFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiMacroFileFields(LLVMParser.DiMacroFileFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diMacroFileFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiMacroFileFieldList(LLVMParser.DiMacroFileFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diMacroFileField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiMacroFileField(LLVMParser.DiMacroFileFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#genericDINode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericDINode(LLVMParser.GenericDINodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#genericDINodeFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericDINodeFields(LLVMParser.GenericDINodeFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#genericDINodeFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericDINodeFieldList(LLVMParser.GenericDINodeFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#genericDINodeField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericDINodeField(LLVMParser.GenericDINodeFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fileField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFileField(LLVMParser.FileFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#isOptimizedField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsOptimizedField(LLVMParser.IsOptimizedFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#tagField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTagField(LLVMParser.TagFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#nameField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNameField(LLVMParser.NameFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#sizeField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSizeField(LLVMParser.SizeFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#alignField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlignField(LLVMParser.AlignFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#flagsField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlagsField(LLVMParser.FlagsFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#lineField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLineField(LLVMParser.LineFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#scopeField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScopeField(LLVMParser.ScopeFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#baseTypeField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseTypeField(LLVMParser.BaseTypeFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#offsetField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOffsetField(LLVMParser.OffsetFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#templateParamsField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplateParamsField(LLVMParser.TemplateParamsFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#intOrMDField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntOrMDField(LLVMParser.IntOrMDFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#typeField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeField(LLVMParser.TypeFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#linkageNameField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLinkageNameField(LLVMParser.LinkageNameFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#isLocalField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsLocalField(LLVMParser.IsLocalFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#isDefinitionField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsDefinitionField(LLVMParser.IsDefinitionFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#declarationField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarationField(LLVMParser.DeclarationFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#columnField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnField(LLVMParser.ColumnFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#typeMacinfoField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeMacinfoField(LLVMParser.TypeMacinfoFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#checksumkind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChecksumkind(LLVMParser.ChecksumkindContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diFlagList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiFlagList(LLVMParser.DiFlagListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#diFlag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiFlag(LLVMParser.DiFlagContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#dwarfAttEncoding}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDwarfAttEncoding(LLVMParser.DwarfAttEncodingContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#dwarfCC}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDwarfCC(LLVMParser.DwarfCCContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#dwarfLang}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDwarfLang(LLVMParser.DwarfLangContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#dwarfMacinfo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDwarfMacinfo(LLVMParser.DwarfMacinfoContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#dwarfOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDwarfOp(LLVMParser.DwarfOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#dwarfTag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDwarfTag(LLVMParser.DwarfTagContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#dwarfVirtuality}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDwarfVirtuality(LLVMParser.DwarfVirtualityContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#emissionKind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmissionKind(LLVMParser.EmissionKindContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#typeValues}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeValues(LLVMParser.TypeValuesContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#typeValueList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeValueList(LLVMParser.TypeValueListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#commaSepTypeValueList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommaSepTypeValueList(LLVMParser.CommaSepTypeValueListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#typeValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeValue(LLVMParser.TypeValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#typeConsts}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeConsts(LLVMParser.TypeConstsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#typeConstList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeConstList(LLVMParser.TypeConstListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#typeConst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeConst(LLVMParser.TypeConstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#alignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlignment(LLVMParser.AlignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#allocSize}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllocSize(LLVMParser.AllocSizeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#args}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgs(LLVMParser.ArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#argList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgList(LLVMParser.ArgListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg(LLVMParser.ArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#atomicOrdering}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomicOrdering(LLVMParser.AtomicOrderingContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optCallingConv}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptCallingConv(LLVMParser.OptCallingConvContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#callingConv}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallingConv(LLVMParser.CallingConvContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optComdat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptComdat(LLVMParser.OptComdatContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#comdat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComdat(LLVMParser.ComdatContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#dereferenceable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDereferenceable(LLVMParser.DereferenceableContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optDLLStorageClass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptDLLStorageClass(LLVMParser.OptDLLStorageClassContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#dllStorageClass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDllStorageClass(LLVMParser.DllStorageClassContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optExact}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptExact(LLVMParser.OptExactContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#exceptionArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExceptionArgs(LLVMParser.ExceptionArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#exceptionArgList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExceptionArgList(LLVMParser.ExceptionArgListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#exceptionArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExceptionArg(LLVMParser.ExceptionArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#exceptionScope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExceptionScope(LLVMParser.ExceptionScopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fastMathFlags}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFastMathFlags(LLVMParser.FastMathFlagsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fastMathFlagList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFastMathFlagList(LLVMParser.FastMathFlagListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fastMathFlag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFastMathFlag(LLVMParser.FastMathFlagContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#fpred}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFpred(LLVMParser.FpredContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#funcAttrs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncAttrs(LLVMParser.FuncAttrsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#funcAttrList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncAttrList(LLVMParser.FuncAttrListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#funcAttr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncAttr(LLVMParser.FuncAttrContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optInBounds}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptInBounds(LLVMParser.OptInBoundsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#indices}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndices(LLVMParser.IndicesContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#indexList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexList(LLVMParser.IndexListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex(LLVMParser.IndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#iPred}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIPred(LLVMParser.IPredContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optLinkage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptLinkage(LLVMParser.OptLinkageContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#linkage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLinkage(LLVMParser.LinkageContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optExternLinkage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptExternLinkage(LLVMParser.OptExternLinkageContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#externLinkage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExternLinkage(LLVMParser.ExternLinkageContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#operandBundles}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperandBundles(LLVMParser.OperandBundlesContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#operandBundleList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperandBundleList(LLVMParser.OperandBundleListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#operandBundle}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperandBundle(LLVMParser.OperandBundleContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#overflowFlags}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverflowFlags(LLVMParser.OverflowFlagsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#overflowFlagList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverflowFlagList(LLVMParser.OverflowFlagListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#overflowFlag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverflowFlag(LLVMParser.OverflowFlagContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#paramAttrs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamAttrs(LLVMParser.ParamAttrsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#paramAttrList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamAttrList(LLVMParser.ParamAttrListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#byval}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitByval(LLVMParser.ByvalContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#paramAttr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamAttr(LLVMParser.ParamAttrContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParams(LLVMParser.ParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#paramList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamList(LLVMParser.ParamListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(LLVMParser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optPreemptionSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptPreemptionSpecifier(LLVMParser.OptPreemptionSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#preemptionSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreemptionSpecifier(LLVMParser.PreemptionSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#returnAttrs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnAttrs(LLVMParser.ReturnAttrsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#returnAttrList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnAttrList(LLVMParser.ReturnAttrListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#returnAttr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnAttr(LLVMParser.ReturnAttrContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#section}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSection(LLVMParser.SectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#stackAlignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStackAlignment(LLVMParser.StackAlignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#optSyncScope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptSyncScope(LLVMParser.OptSyncScopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#threadLocal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThreadLocal(LLVMParser.ThreadLocalContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#tlsModel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTlsModel(LLVMParser.TlsModelContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#unnamedAddr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnnamedAddr(LLVMParser.UnnamedAddrContext ctx);
	/**
	 * Visit a parse tree produced by {@link LLVMParser#visibility}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVisibility(LLVMParser.VisibilityContext ctx);
}