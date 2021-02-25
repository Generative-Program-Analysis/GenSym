// Generated from LLVMParser.g4 by ANTLR 4.9.1
package sai.lang.llvm;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LLVMParser}.
 */
public interface LLVMParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LLVMParser#module}.
	 * @param ctx the parse tree
	 */
	void enterModule(LLVMParser.ModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#module}.
	 * @param ctx the parse tree
	 */
	void exitModule(LLVMParser.ModuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#topLevelEntities}.
	 * @param ctx the parse tree
	 */
	void enterTopLevelEntities(LLVMParser.TopLevelEntitiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#topLevelEntities}.
	 * @param ctx the parse tree
	 */
	void exitTopLevelEntities(LLVMParser.TopLevelEntitiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#topLevelEntityList}.
	 * @param ctx the parse tree
	 */
	void enterTopLevelEntityList(LLVMParser.TopLevelEntityListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#topLevelEntityList}.
	 * @param ctx the parse tree
	 */
	void exitTopLevelEntityList(LLVMParser.TopLevelEntityListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#topLevelEntity}.
	 * @param ctx the parse tree
	 */
	void enterTopLevelEntity(LLVMParser.TopLevelEntityContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#topLevelEntity}.
	 * @param ctx the parse tree
	 */
	void exitTopLevelEntity(LLVMParser.TopLevelEntityContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#sourceFilename}.
	 * @param ctx the parse tree
	 */
	void enterSourceFilename(LLVMParser.SourceFilenameContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#sourceFilename}.
	 * @param ctx the parse tree
	 */
	void exitSourceFilename(LLVMParser.SourceFilenameContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#targetDefinition}.
	 * @param ctx the parse tree
	 */
	void enterTargetDefinition(LLVMParser.TargetDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#targetDefinition}.
	 * @param ctx the parse tree
	 */
	void exitTargetDefinition(LLVMParser.TargetDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#moduleAsm}.
	 * @param ctx the parse tree
	 */
	void enterModuleAsm(LLVMParser.ModuleAsmContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#moduleAsm}.
	 * @param ctx the parse tree
	 */
	void exitModuleAsm(LLVMParser.ModuleAsmContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#typeDef}.
	 * @param ctx the parse tree
	 */
	void enterTypeDef(LLVMParser.TypeDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#typeDef}.
	 * @param ctx the parse tree
	 */
	void exitTypeDef(LLVMParser.TypeDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#comdatDef}.
	 * @param ctx the parse tree
	 */
	void enterComdatDef(LLVMParser.ComdatDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#comdatDef}.
	 * @param ctx the parse tree
	 */
	void exitComdatDef(LLVMParser.ComdatDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#selectionKind}.
	 * @param ctx the parse tree
	 */
	void enterSelectionKind(LLVMParser.SelectionKindContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#selectionKind}.
	 * @param ctx the parse tree
	 */
	void exitSelectionKind(LLVMParser.SelectionKindContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#globalDecl}.
	 * @param ctx the parse tree
	 */
	void enterGlobalDecl(LLVMParser.GlobalDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#globalDecl}.
	 * @param ctx the parse tree
	 */
	void exitGlobalDecl(LLVMParser.GlobalDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#globalDef}.
	 * @param ctx the parse tree
	 */
	void enterGlobalDef(LLVMParser.GlobalDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#globalDef}.
	 * @param ctx the parse tree
	 */
	void exitGlobalDef(LLVMParser.GlobalDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optExternallyInitialized}.
	 * @param ctx the parse tree
	 */
	void enterOptExternallyInitialized(LLVMParser.OptExternallyInitializedContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optExternallyInitialized}.
	 * @param ctx the parse tree
	 */
	void exitOptExternallyInitialized(LLVMParser.OptExternallyInitializedContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#immutable}.
	 * @param ctx the parse tree
	 */
	void enterImmutable(LLVMParser.ImmutableContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#immutable}.
	 * @param ctx the parse tree
	 */
	void exitImmutable(LLVMParser.ImmutableContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#globalAttrs}.
	 * @param ctx the parse tree
	 */
	void enterGlobalAttrs(LLVMParser.GlobalAttrsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#globalAttrs}.
	 * @param ctx the parse tree
	 */
	void exitGlobalAttrs(LLVMParser.GlobalAttrsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#globalAttrList}.
	 * @param ctx the parse tree
	 */
	void enterGlobalAttrList(LLVMParser.GlobalAttrListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#globalAttrList}.
	 * @param ctx the parse tree
	 */
	void exitGlobalAttrList(LLVMParser.GlobalAttrListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#globalAttr}.
	 * @param ctx the parse tree
	 */
	void enterGlobalAttr(LLVMParser.GlobalAttrContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#globalAttr}.
	 * @param ctx the parse tree
	 */
	void exitGlobalAttr(LLVMParser.GlobalAttrContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#indirectSymbolDef}.
	 * @param ctx the parse tree
	 */
	void enterIndirectSymbolDef(LLVMParser.IndirectSymbolDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#indirectSymbolDef}.
	 * @param ctx the parse tree
	 */
	void exitIndirectSymbolDef(LLVMParser.IndirectSymbolDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#alias}.
	 * @param ctx the parse tree
	 */
	void enterAlias(LLVMParser.AliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#alias}.
	 * @param ctx the parse tree
	 */
	void exitAlias(LLVMParser.AliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#functionDecl}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDecl(LLVMParser.FunctionDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#functionDecl}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDecl(LLVMParser.FunctionDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#functionDef}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDef(LLVMParser.FunctionDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#functionDef}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDef(LLVMParser.FunctionDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#functionHeader}.
	 * @param ctx the parse tree
	 */
	void enterFunctionHeader(LLVMParser.FunctionHeaderContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#functionHeader}.
	 * @param ctx the parse tree
	 */
	void exitFunctionHeader(LLVMParser.FunctionHeaderContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optGC}.
	 * @param ctx the parse tree
	 */
	void enterOptGC(LLVMParser.OptGCContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optGC}.
	 * @param ctx the parse tree
	 */
	void exitOptGC(LLVMParser.OptGCContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optPrefix}.
	 * @param ctx the parse tree
	 */
	void enterOptPrefix(LLVMParser.OptPrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optPrefix}.
	 * @param ctx the parse tree
	 */
	void exitOptPrefix(LLVMParser.OptPrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optPrologue}.
	 * @param ctx the parse tree
	 */
	void enterOptPrologue(LLVMParser.OptPrologueContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optPrologue}.
	 * @param ctx the parse tree
	 */
	void exitOptPrologue(LLVMParser.OptPrologueContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optPersonality}.
	 * @param ctx the parse tree
	 */
	void enterOptPersonality(LLVMParser.OptPersonalityContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optPersonality}.
	 * @param ctx the parse tree
	 */
	void exitOptPersonality(LLVMParser.OptPersonalityContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#functionBody}.
	 * @param ctx the parse tree
	 */
	void enterFunctionBody(LLVMParser.FunctionBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#functionBody}.
	 * @param ctx the parse tree
	 */
	void exitFunctionBody(LLVMParser.FunctionBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#attrGroupDef}.
	 * @param ctx the parse tree
	 */
	void enterAttrGroupDef(LLVMParser.AttrGroupDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#attrGroupDef}.
	 * @param ctx the parse tree
	 */
	void exitAttrGroupDef(LLVMParser.AttrGroupDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#namedMetadataDef}.
	 * @param ctx the parse tree
	 */
	void enterNamedMetadataDef(LLVMParser.NamedMetadataDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#namedMetadataDef}.
	 * @param ctx the parse tree
	 */
	void exitNamedMetadataDef(LLVMParser.NamedMetadataDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#metadataNodes}.
	 * @param ctx the parse tree
	 */
	void enterMetadataNodes(LLVMParser.MetadataNodesContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#metadataNodes}.
	 * @param ctx the parse tree
	 */
	void exitMetadataNodes(LLVMParser.MetadataNodesContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#metadataNodeList}.
	 * @param ctx the parse tree
	 */
	void enterMetadataNodeList(LLVMParser.MetadataNodeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#metadataNodeList}.
	 * @param ctx the parse tree
	 */
	void exitMetadataNodeList(LLVMParser.MetadataNodeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#metadataNode}.
	 * @param ctx the parse tree
	 */
	void enterMetadataNode(LLVMParser.MetadataNodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#metadataNode}.
	 * @param ctx the parse tree
	 */
	void exitMetadataNode(LLVMParser.MetadataNodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#metadataDef}.
	 * @param ctx the parse tree
	 */
	void enterMetadataDef(LLVMParser.MetadataDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#metadataDef}.
	 * @param ctx the parse tree
	 */
	void exitMetadataDef(LLVMParser.MetadataDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optDistinct}.
	 * @param ctx the parse tree
	 */
	void enterOptDistinct(LLVMParser.OptDistinctContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optDistinct}.
	 * @param ctx the parse tree
	 */
	void exitOptDistinct(LLVMParser.OptDistinctContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#useListOrders}.
	 * @param ctx the parse tree
	 */
	void enterUseListOrders(LLVMParser.UseListOrdersContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#useListOrders}.
	 * @param ctx the parse tree
	 */
	void exitUseListOrders(LLVMParser.UseListOrdersContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#useListOrderList}.
	 * @param ctx the parse tree
	 */
	void enterUseListOrderList(LLVMParser.UseListOrderListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#useListOrderList}.
	 * @param ctx the parse tree
	 */
	void exitUseListOrderList(LLVMParser.UseListOrderListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#useListOrder}.
	 * @param ctx the parse tree
	 */
	void enterUseListOrder(LLVMParser.UseListOrderContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#useListOrder}.
	 * @param ctx the parse tree
	 */
	void exitUseListOrder(LLVMParser.UseListOrderContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#useListOrderBB}.
	 * @param ctx the parse tree
	 */
	void enterUseListOrderBB(LLVMParser.UseListOrderBBContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#useListOrderBB}.
	 * @param ctx the parse tree
	 */
	void exitUseListOrderBB(LLVMParser.UseListOrderBBContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#globalIdent}.
	 * @param ctx the parse tree
	 */
	void enterGlobalIdent(LLVMParser.GlobalIdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#globalIdent}.
	 * @param ctx the parse tree
	 */
	void exitGlobalIdent(LLVMParser.GlobalIdentContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#localIdent}.
	 * @param ctx the parse tree
	 */
	void enterLocalIdent(LLVMParser.LocalIdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#localIdent}.
	 * @param ctx the parse tree
	 */
	void exitLocalIdent(LLVMParser.LocalIdentContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#labelIdent}.
	 * @param ctx the parse tree
	 */
	void enterLabelIdent(LLVMParser.LabelIdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#labelIdent}.
	 * @param ctx the parse tree
	 */
	void exitLabelIdent(LLVMParser.LabelIdentContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#attrGroupID}.
	 * @param ctx the parse tree
	 */
	void enterAttrGroupID(LLVMParser.AttrGroupIDContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#attrGroupID}.
	 * @param ctx the parse tree
	 */
	void exitAttrGroupID(LLVMParser.AttrGroupIDContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#comdatName}.
	 * @param ctx the parse tree
	 */
	void enterComdatName(LLVMParser.ComdatNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#comdatName}.
	 * @param ctx the parse tree
	 */
	void exitComdatName(LLVMParser.ComdatNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#metadataName}.
	 * @param ctx the parse tree
	 */
	void enterMetadataName(LLVMParser.MetadataNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#metadataName}.
	 * @param ctx the parse tree
	 */
	void exitMetadataName(LLVMParser.MetadataNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#metadataID}.
	 * @param ctx the parse tree
	 */
	void enterMetadataID(LLVMParser.MetadataIDContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#metadataID}.
	 * @param ctx the parse tree
	 */
	void exitMetadataID(LLVMParser.MetadataIDContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#llvmType}.
	 * @param ctx the parse tree
	 */
	void enterLlvmType(LLVMParser.LlvmTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#llvmType}.
	 * @param ctx the parse tree
	 */
	void exitLlvmType(LLVMParser.LlvmTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#concreteNonRecType}.
	 * @param ctx the parse tree
	 */
	void enterConcreteNonRecType(LLVMParser.ConcreteNonRecTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#concreteNonRecType}.
	 * @param ctx the parse tree
	 */
	void exitConcreteNonRecType(LLVMParser.ConcreteNonRecTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#voidType}.
	 * @param ctx the parse tree
	 */
	void enterVoidType(LLVMParser.VoidTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#voidType}.
	 * @param ctx the parse tree
	 */
	void exitVoidType(LLVMParser.VoidTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#intType}.
	 * @param ctx the parse tree
	 */
	void enterIntType(LLVMParser.IntTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#intType}.
	 * @param ctx the parse tree
	 */
	void exitIntType(LLVMParser.IntTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#floatType}.
	 * @param ctx the parse tree
	 */
	void enterFloatType(LLVMParser.FloatTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#floatType}.
	 * @param ctx the parse tree
	 */
	void exitFloatType(LLVMParser.FloatTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#floatKind}.
	 * @param ctx the parse tree
	 */
	void enterFloatKind(LLVMParser.FloatKindContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#floatKind}.
	 * @param ctx the parse tree
	 */
	void exitFloatKind(LLVMParser.FloatKindContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#mmxType}.
	 * @param ctx the parse tree
	 */
	void enterMmxType(LLVMParser.MmxTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#mmxType}.
	 * @param ctx the parse tree
	 */
	void exitMmxType(LLVMParser.MmxTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optAddrSpace}.
	 * @param ctx the parse tree
	 */
	void enterOptAddrSpace(LLVMParser.OptAddrSpaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optAddrSpace}.
	 * @param ctx the parse tree
	 */
	void exitOptAddrSpace(LLVMParser.OptAddrSpaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#addrSpace}.
	 * @param ctx the parse tree
	 */
	void enterAddrSpace(LLVMParser.AddrSpaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#addrSpace}.
	 * @param ctx the parse tree
	 */
	void exitAddrSpace(LLVMParser.AddrSpaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#vectorType}.
	 * @param ctx the parse tree
	 */
	void enterVectorType(LLVMParser.VectorTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#vectorType}.
	 * @param ctx the parse tree
	 */
	void exitVectorType(LLVMParser.VectorTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#labelType}.
	 * @param ctx the parse tree
	 */
	void enterLabelType(LLVMParser.LabelTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#labelType}.
	 * @param ctx the parse tree
	 */
	void exitLabelType(LLVMParser.LabelTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#tokenType}.
	 * @param ctx the parse tree
	 */
	void enterTokenType(LLVMParser.TokenTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#tokenType}.
	 * @param ctx the parse tree
	 */
	void exitTokenType(LLVMParser.TokenTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#metadataType}.
	 * @param ctx the parse tree
	 */
	void enterMetadataType(LLVMParser.MetadataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#metadataType}.
	 * @param ctx the parse tree
	 */
	void exitMetadataType(LLVMParser.MetadataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#arrayType}.
	 * @param ctx the parse tree
	 */
	void enterArrayType(LLVMParser.ArrayTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#arrayType}.
	 * @param ctx the parse tree
	 */
	void exitArrayType(LLVMParser.ArrayTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#structType}.
	 * @param ctx the parse tree
	 */
	void enterStructType(LLVMParser.StructTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#structType}.
	 * @param ctx the parse tree
	 */
	void exitStructType(LLVMParser.StructTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#typeList}.
	 * @param ctx the parse tree
	 */
	void enterTypeList(LLVMParser.TypeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#typeList}.
	 * @param ctx the parse tree
	 */
	void exitTypeList(LLVMParser.TypeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#opaqueType}.
	 * @param ctx the parse tree
	 */
	void enterOpaqueType(LLVMParser.OpaqueTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#opaqueType}.
	 * @param ctx the parse tree
	 */
	void exitOpaqueType(LLVMParser.OpaqueTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#namedType}.
	 * @param ctx the parse tree
	 */
	void enterNamedType(LLVMParser.NamedTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#namedType}.
	 * @param ctx the parse tree
	 */
	void exitNamedType(LLVMParser.NamedTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(LLVMParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(LLVMParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#inlineAsm}.
	 * @param ctx the parse tree
	 */
	void enterInlineAsm(LLVMParser.InlineAsmContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#inlineAsm}.
	 * @param ctx the parse tree
	 */
	void exitInlineAsm(LLVMParser.InlineAsmContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optSideEffect}.
	 * @param ctx the parse tree
	 */
	void enterOptSideEffect(LLVMParser.OptSideEffectContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optSideEffect}.
	 * @param ctx the parse tree
	 */
	void exitOptSideEffect(LLVMParser.OptSideEffectContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optAlignStack}.
	 * @param ctx the parse tree
	 */
	void enterOptAlignStack(LLVMParser.OptAlignStackContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optAlignStack}.
	 * @param ctx the parse tree
	 */
	void exitOptAlignStack(LLVMParser.OptAlignStackContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optIntelDialect}.
	 * @param ctx the parse tree
	 */
	void enterOptIntelDialect(LLVMParser.OptIntelDialectContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optIntelDialect}.
	 * @param ctx the parse tree
	 */
	void exitOptIntelDialect(LLVMParser.OptIntelDialectContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(LLVMParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(LLVMParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#boolConst}.
	 * @param ctx the parse tree
	 */
	void enterBoolConst(LLVMParser.BoolConstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#boolConst}.
	 * @param ctx the parse tree
	 */
	void exitBoolConst(LLVMParser.BoolConstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#boolLit}.
	 * @param ctx the parse tree
	 */
	void enterBoolLit(LLVMParser.BoolLitContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#boolLit}.
	 * @param ctx the parse tree
	 */
	void exitBoolLit(LLVMParser.BoolLitContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#intConst}.
	 * @param ctx the parse tree
	 */
	void enterIntConst(LLVMParser.IntConstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#intConst}.
	 * @param ctx the parse tree
	 */
	void exitIntConst(LLVMParser.IntConstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#intLit}.
	 * @param ctx the parse tree
	 */
	void enterIntLit(LLVMParser.IntLitContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#intLit}.
	 * @param ctx the parse tree
	 */
	void exitIntLit(LLVMParser.IntLitContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#floatConst}.
	 * @param ctx the parse tree
	 */
	void enterFloatConst(LLVMParser.FloatConstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#floatConst}.
	 * @param ctx the parse tree
	 */
	void exitFloatConst(LLVMParser.FloatConstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#nullConst}.
	 * @param ctx the parse tree
	 */
	void enterNullConst(LLVMParser.NullConstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#nullConst}.
	 * @param ctx the parse tree
	 */
	void exitNullConst(LLVMParser.NullConstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#noneConst}.
	 * @param ctx the parse tree
	 */
	void enterNoneConst(LLVMParser.NoneConstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#noneConst}.
	 * @param ctx the parse tree
	 */
	void exitNoneConst(LLVMParser.NoneConstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#structConst}.
	 * @param ctx the parse tree
	 */
	void enterStructConst(LLVMParser.StructConstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#structConst}.
	 * @param ctx the parse tree
	 */
	void exitStructConst(LLVMParser.StructConstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#arrayConst}.
	 * @param ctx the parse tree
	 */
	void enterArrayConst(LLVMParser.ArrayConstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#arrayConst}.
	 * @param ctx the parse tree
	 */
	void exitArrayConst(LLVMParser.ArrayConstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#charArrayConst}.
	 * @param ctx the parse tree
	 */
	void enterCharArrayConst(LLVMParser.CharArrayConstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#charArrayConst}.
	 * @param ctx the parse tree
	 */
	void exitCharArrayConst(LLVMParser.CharArrayConstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#stringLit}.
	 * @param ctx the parse tree
	 */
	void enterStringLit(LLVMParser.StringLitContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#stringLit}.
	 * @param ctx the parse tree
	 */
	void exitStringLit(LLVMParser.StringLitContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#vectorConst}.
	 * @param ctx the parse tree
	 */
	void enterVectorConst(LLVMParser.VectorConstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#vectorConst}.
	 * @param ctx the parse tree
	 */
	void exitVectorConst(LLVMParser.VectorConstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#zeroInitializerConst}.
	 * @param ctx the parse tree
	 */
	void enterZeroInitializerConst(LLVMParser.ZeroInitializerConstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#zeroInitializerConst}.
	 * @param ctx the parse tree
	 */
	void exitZeroInitializerConst(LLVMParser.ZeroInitializerConstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#undefConst}.
	 * @param ctx the parse tree
	 */
	void enterUndefConst(LLVMParser.UndefConstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#undefConst}.
	 * @param ctx the parse tree
	 */
	void exitUndefConst(LLVMParser.UndefConstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#blockAddressConst}.
	 * @param ctx the parse tree
	 */
	void enterBlockAddressConst(LLVMParser.BlockAddressConstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#blockAddressConst}.
	 * @param ctx the parse tree
	 */
	void exitBlockAddressConst(LLVMParser.BlockAddressConstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#constantExpr}.
	 * @param ctx the parse tree
	 */
	void enterConstantExpr(LLVMParser.ConstantExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#constantExpr}.
	 * @param ctx the parse tree
	 */
	void exitConstantExpr(LLVMParser.ConstantExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#addExpr}.
	 * @param ctx the parse tree
	 */
	void enterAddExpr(LLVMParser.AddExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#addExpr}.
	 * @param ctx the parse tree
	 */
	void exitAddExpr(LLVMParser.AddExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fAddExpr}.
	 * @param ctx the parse tree
	 */
	void enterFAddExpr(LLVMParser.FAddExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fAddExpr}.
	 * @param ctx the parse tree
	 */
	void exitFAddExpr(LLVMParser.FAddExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#subExpr}.
	 * @param ctx the parse tree
	 */
	void enterSubExpr(LLVMParser.SubExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#subExpr}.
	 * @param ctx the parse tree
	 */
	void exitSubExpr(LLVMParser.SubExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fSubExpr}.
	 * @param ctx the parse tree
	 */
	void enterFSubExpr(LLVMParser.FSubExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fSubExpr}.
	 * @param ctx the parse tree
	 */
	void exitFSubExpr(LLVMParser.FSubExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#mulExpr}.
	 * @param ctx the parse tree
	 */
	void enterMulExpr(LLVMParser.MulExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#mulExpr}.
	 * @param ctx the parse tree
	 */
	void exitMulExpr(LLVMParser.MulExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fMulExpr}.
	 * @param ctx the parse tree
	 */
	void enterFMulExpr(LLVMParser.FMulExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fMulExpr}.
	 * @param ctx the parse tree
	 */
	void exitFMulExpr(LLVMParser.FMulExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#uDivExpr}.
	 * @param ctx the parse tree
	 */
	void enterUDivExpr(LLVMParser.UDivExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#uDivExpr}.
	 * @param ctx the parse tree
	 */
	void exitUDivExpr(LLVMParser.UDivExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#sDivExpr}.
	 * @param ctx the parse tree
	 */
	void enterSDivExpr(LLVMParser.SDivExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#sDivExpr}.
	 * @param ctx the parse tree
	 */
	void exitSDivExpr(LLVMParser.SDivExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fDivExpr}.
	 * @param ctx the parse tree
	 */
	void enterFDivExpr(LLVMParser.FDivExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fDivExpr}.
	 * @param ctx the parse tree
	 */
	void exitFDivExpr(LLVMParser.FDivExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#uRemExpr}.
	 * @param ctx the parse tree
	 */
	void enterURemExpr(LLVMParser.URemExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#uRemExpr}.
	 * @param ctx the parse tree
	 */
	void exitURemExpr(LLVMParser.URemExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#sRemExpr}.
	 * @param ctx the parse tree
	 */
	void enterSRemExpr(LLVMParser.SRemExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#sRemExpr}.
	 * @param ctx the parse tree
	 */
	void exitSRemExpr(LLVMParser.SRemExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fRemExpr}.
	 * @param ctx the parse tree
	 */
	void enterFRemExpr(LLVMParser.FRemExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fRemExpr}.
	 * @param ctx the parse tree
	 */
	void exitFRemExpr(LLVMParser.FRemExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#shlExpr}.
	 * @param ctx the parse tree
	 */
	void enterShlExpr(LLVMParser.ShlExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#shlExpr}.
	 * @param ctx the parse tree
	 */
	void exitShlExpr(LLVMParser.ShlExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#lShrExpr}.
	 * @param ctx the parse tree
	 */
	void enterLShrExpr(LLVMParser.LShrExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#lShrExpr}.
	 * @param ctx the parse tree
	 */
	void exitLShrExpr(LLVMParser.LShrExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#ashrExpr}.
	 * @param ctx the parse tree
	 */
	void enterAshrExpr(LLVMParser.AshrExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#ashrExpr}.
	 * @param ctx the parse tree
	 */
	void exitAshrExpr(LLVMParser.AshrExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(LLVMParser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(LLVMParser.AndExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#orExpr}.
	 * @param ctx the parse tree
	 */
	void enterOrExpr(LLVMParser.OrExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#orExpr}.
	 * @param ctx the parse tree
	 */
	void exitOrExpr(LLVMParser.OrExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#xorExpr}.
	 * @param ctx the parse tree
	 */
	void enterXorExpr(LLVMParser.XorExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#xorExpr}.
	 * @param ctx the parse tree
	 */
	void exitXorExpr(LLVMParser.XorExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#extractElementExpr}.
	 * @param ctx the parse tree
	 */
	void enterExtractElementExpr(LLVMParser.ExtractElementExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#extractElementExpr}.
	 * @param ctx the parse tree
	 */
	void exitExtractElementExpr(LLVMParser.ExtractElementExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#insertElementExpr}.
	 * @param ctx the parse tree
	 */
	void enterInsertElementExpr(LLVMParser.InsertElementExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#insertElementExpr}.
	 * @param ctx the parse tree
	 */
	void exitInsertElementExpr(LLVMParser.InsertElementExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#shuffleVectorExpr}.
	 * @param ctx the parse tree
	 */
	void enterShuffleVectorExpr(LLVMParser.ShuffleVectorExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#shuffleVectorExpr}.
	 * @param ctx the parse tree
	 */
	void exitShuffleVectorExpr(LLVMParser.ShuffleVectorExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#extractValueExpr}.
	 * @param ctx the parse tree
	 */
	void enterExtractValueExpr(LLVMParser.ExtractValueExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#extractValueExpr}.
	 * @param ctx the parse tree
	 */
	void exitExtractValueExpr(LLVMParser.ExtractValueExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#insertValueExpr}.
	 * @param ctx the parse tree
	 */
	void enterInsertValueExpr(LLVMParser.InsertValueExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#insertValueExpr}.
	 * @param ctx the parse tree
	 */
	void exitInsertValueExpr(LLVMParser.InsertValueExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#getElementPtrExpr}.
	 * @param ctx the parse tree
	 */
	void enterGetElementPtrExpr(LLVMParser.GetElementPtrExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#getElementPtrExpr}.
	 * @param ctx the parse tree
	 */
	void exitGetElementPtrExpr(LLVMParser.GetElementPtrExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#gepConstIndices}.
	 * @param ctx the parse tree
	 */
	void enterGepConstIndices(LLVMParser.GepConstIndicesContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#gepConstIndices}.
	 * @param ctx the parse tree
	 */
	void exitGepConstIndices(LLVMParser.GepConstIndicesContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#gepConstIndexList}.
	 * @param ctx the parse tree
	 */
	void enterGepConstIndexList(LLVMParser.GepConstIndexListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#gepConstIndexList}.
	 * @param ctx the parse tree
	 */
	void exitGepConstIndexList(LLVMParser.GepConstIndexListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#gepConstIndex}.
	 * @param ctx the parse tree
	 */
	void enterGepConstIndex(LLVMParser.GepConstIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#gepConstIndex}.
	 * @param ctx the parse tree
	 */
	void exitGepConstIndex(LLVMParser.GepConstIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optInrange}.
	 * @param ctx the parse tree
	 */
	void enterOptInrange(LLVMParser.OptInrangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optInrange}.
	 * @param ctx the parse tree
	 */
	void exitOptInrange(LLVMParser.OptInrangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#truncExpr}.
	 * @param ctx the parse tree
	 */
	void enterTruncExpr(LLVMParser.TruncExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#truncExpr}.
	 * @param ctx the parse tree
	 */
	void exitTruncExpr(LLVMParser.TruncExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#zExtExpr}.
	 * @param ctx the parse tree
	 */
	void enterZExtExpr(LLVMParser.ZExtExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#zExtExpr}.
	 * @param ctx the parse tree
	 */
	void exitZExtExpr(LLVMParser.ZExtExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#sExtExpr}.
	 * @param ctx the parse tree
	 */
	void enterSExtExpr(LLVMParser.SExtExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#sExtExpr}.
	 * @param ctx the parse tree
	 */
	void exitSExtExpr(LLVMParser.SExtExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fPTruncExpr}.
	 * @param ctx the parse tree
	 */
	void enterFPTruncExpr(LLVMParser.FPTruncExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fPTruncExpr}.
	 * @param ctx the parse tree
	 */
	void exitFPTruncExpr(LLVMParser.FPTruncExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fpExtExpr}.
	 * @param ctx the parse tree
	 */
	void enterFpExtExpr(LLVMParser.FpExtExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fpExtExpr}.
	 * @param ctx the parse tree
	 */
	void exitFpExtExpr(LLVMParser.FpExtExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fpToUIExpr}.
	 * @param ctx the parse tree
	 */
	void enterFpToUIExpr(LLVMParser.FpToUIExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fpToUIExpr}.
	 * @param ctx the parse tree
	 */
	void exitFpToUIExpr(LLVMParser.FpToUIExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fpToSIExpr}.
	 * @param ctx the parse tree
	 */
	void enterFpToSIExpr(LLVMParser.FpToSIExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fpToSIExpr}.
	 * @param ctx the parse tree
	 */
	void exitFpToSIExpr(LLVMParser.FpToSIExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#uiToFPExpr}.
	 * @param ctx the parse tree
	 */
	void enterUiToFPExpr(LLVMParser.UiToFPExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#uiToFPExpr}.
	 * @param ctx the parse tree
	 */
	void exitUiToFPExpr(LLVMParser.UiToFPExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#siToFPExpr}.
	 * @param ctx the parse tree
	 */
	void enterSiToFPExpr(LLVMParser.SiToFPExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#siToFPExpr}.
	 * @param ctx the parse tree
	 */
	void exitSiToFPExpr(LLVMParser.SiToFPExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#ptrToIntExpr}.
	 * @param ctx the parse tree
	 */
	void enterPtrToIntExpr(LLVMParser.PtrToIntExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#ptrToIntExpr}.
	 * @param ctx the parse tree
	 */
	void exitPtrToIntExpr(LLVMParser.PtrToIntExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#intToPtrExpr}.
	 * @param ctx the parse tree
	 */
	void enterIntToPtrExpr(LLVMParser.IntToPtrExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#intToPtrExpr}.
	 * @param ctx the parse tree
	 */
	void exitIntToPtrExpr(LLVMParser.IntToPtrExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#bitCastExpr}.
	 * @param ctx the parse tree
	 */
	void enterBitCastExpr(LLVMParser.BitCastExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#bitCastExpr}.
	 * @param ctx the parse tree
	 */
	void exitBitCastExpr(LLVMParser.BitCastExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#addrSpaceCastExpr}.
	 * @param ctx the parse tree
	 */
	void enterAddrSpaceCastExpr(LLVMParser.AddrSpaceCastExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#addrSpaceCastExpr}.
	 * @param ctx the parse tree
	 */
	void exitAddrSpaceCastExpr(LLVMParser.AddrSpaceCastExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#iCmpExpr}.
	 * @param ctx the parse tree
	 */
	void enterICmpExpr(LLVMParser.ICmpExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#iCmpExpr}.
	 * @param ctx the parse tree
	 */
	void exitICmpExpr(LLVMParser.ICmpExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fCmpExpr}.
	 * @param ctx the parse tree
	 */
	void enterFCmpExpr(LLVMParser.FCmpExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fCmpExpr}.
	 * @param ctx the parse tree
	 */
	void exitFCmpExpr(LLVMParser.FCmpExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#selectExpr}.
	 * @param ctx the parse tree
	 */
	void enterSelectExpr(LLVMParser.SelectExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#selectExpr}.
	 * @param ctx the parse tree
	 */
	void exitSelectExpr(LLVMParser.SelectExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#basicBlockList}.
	 * @param ctx the parse tree
	 */
	void enterBasicBlockList(LLVMParser.BasicBlockListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#basicBlockList}.
	 * @param ctx the parse tree
	 */
	void exitBasicBlockList(LLVMParser.BasicBlockListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#basicBlock}.
	 * @param ctx the parse tree
	 */
	void enterBasicBlock(LLVMParser.BasicBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#basicBlock}.
	 * @param ctx the parse tree
	 */
	void exitBasicBlock(LLVMParser.BasicBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optLabelIdent}.
	 * @param ctx the parse tree
	 */
	void enterOptLabelIdent(LLVMParser.OptLabelIdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optLabelIdent}.
	 * @param ctx the parse tree
	 */
	void exitOptLabelIdent(LLVMParser.OptLabelIdentContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#instructions}.
	 * @param ctx the parse tree
	 */
	void enterInstructions(LLVMParser.InstructionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#instructions}.
	 * @param ctx the parse tree
	 */
	void exitInstructions(LLVMParser.InstructionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#instructionList}.
	 * @param ctx the parse tree
	 */
	void enterInstructionList(LLVMParser.InstructionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#instructionList}.
	 * @param ctx the parse tree
	 */
	void exitInstructionList(LLVMParser.InstructionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterInstruction(LLVMParser.InstructionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitInstruction(LLVMParser.InstructionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#valueInstruction}.
	 * @param ctx the parse tree
	 */
	void enterValueInstruction(LLVMParser.ValueInstructionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#valueInstruction}.
	 * @param ctx the parse tree
	 */
	void exitValueInstruction(LLVMParser.ValueInstructionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#addInst}.
	 * @param ctx the parse tree
	 */
	void enterAddInst(LLVMParser.AddInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#addInst}.
	 * @param ctx the parse tree
	 */
	void exitAddInst(LLVMParser.AddInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fAddInst}.
	 * @param ctx the parse tree
	 */
	void enterFAddInst(LLVMParser.FAddInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fAddInst}.
	 * @param ctx the parse tree
	 */
	void exitFAddInst(LLVMParser.FAddInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#subInst}.
	 * @param ctx the parse tree
	 */
	void enterSubInst(LLVMParser.SubInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#subInst}.
	 * @param ctx the parse tree
	 */
	void exitSubInst(LLVMParser.SubInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fSubInst}.
	 * @param ctx the parse tree
	 */
	void enterFSubInst(LLVMParser.FSubInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fSubInst}.
	 * @param ctx the parse tree
	 */
	void exitFSubInst(LLVMParser.FSubInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#mulInst}.
	 * @param ctx the parse tree
	 */
	void enterMulInst(LLVMParser.MulInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#mulInst}.
	 * @param ctx the parse tree
	 */
	void exitMulInst(LLVMParser.MulInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fMulInst}.
	 * @param ctx the parse tree
	 */
	void enterFMulInst(LLVMParser.FMulInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fMulInst}.
	 * @param ctx the parse tree
	 */
	void exitFMulInst(LLVMParser.FMulInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#uDivInst}.
	 * @param ctx the parse tree
	 */
	void enterUDivInst(LLVMParser.UDivInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#uDivInst}.
	 * @param ctx the parse tree
	 */
	void exitUDivInst(LLVMParser.UDivInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#sDivInst}.
	 * @param ctx the parse tree
	 */
	void enterSDivInst(LLVMParser.SDivInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#sDivInst}.
	 * @param ctx the parse tree
	 */
	void exitSDivInst(LLVMParser.SDivInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fDivInst}.
	 * @param ctx the parse tree
	 */
	void enterFDivInst(LLVMParser.FDivInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fDivInst}.
	 * @param ctx the parse tree
	 */
	void exitFDivInst(LLVMParser.FDivInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#uRemInst}.
	 * @param ctx the parse tree
	 */
	void enterURemInst(LLVMParser.URemInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#uRemInst}.
	 * @param ctx the parse tree
	 */
	void exitURemInst(LLVMParser.URemInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#sRemInst}.
	 * @param ctx the parse tree
	 */
	void enterSRemInst(LLVMParser.SRemInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#sRemInst}.
	 * @param ctx the parse tree
	 */
	void exitSRemInst(LLVMParser.SRemInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fRemInst}.
	 * @param ctx the parse tree
	 */
	void enterFRemInst(LLVMParser.FRemInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fRemInst}.
	 * @param ctx the parse tree
	 */
	void exitFRemInst(LLVMParser.FRemInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#shlInst}.
	 * @param ctx the parse tree
	 */
	void enterShlInst(LLVMParser.ShlInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#shlInst}.
	 * @param ctx the parse tree
	 */
	void exitShlInst(LLVMParser.ShlInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#lshrInst}.
	 * @param ctx the parse tree
	 */
	void enterLshrInst(LLVMParser.LshrInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#lshrInst}.
	 * @param ctx the parse tree
	 */
	void exitLshrInst(LLVMParser.LshrInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#ashrInst}.
	 * @param ctx the parse tree
	 */
	void enterAshrInst(LLVMParser.AshrInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#ashrInst}.
	 * @param ctx the parse tree
	 */
	void exitAshrInst(LLVMParser.AshrInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#andInst}.
	 * @param ctx the parse tree
	 */
	void enterAndInst(LLVMParser.AndInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#andInst}.
	 * @param ctx the parse tree
	 */
	void exitAndInst(LLVMParser.AndInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#orInst}.
	 * @param ctx the parse tree
	 */
	void enterOrInst(LLVMParser.OrInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#orInst}.
	 * @param ctx the parse tree
	 */
	void exitOrInst(LLVMParser.OrInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#xorInst}.
	 * @param ctx the parse tree
	 */
	void enterXorInst(LLVMParser.XorInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#xorInst}.
	 * @param ctx the parse tree
	 */
	void exitXorInst(LLVMParser.XorInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#extractElementInst}.
	 * @param ctx the parse tree
	 */
	void enterExtractElementInst(LLVMParser.ExtractElementInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#extractElementInst}.
	 * @param ctx the parse tree
	 */
	void exitExtractElementInst(LLVMParser.ExtractElementInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#insertElementInst}.
	 * @param ctx the parse tree
	 */
	void enterInsertElementInst(LLVMParser.InsertElementInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#insertElementInst}.
	 * @param ctx the parse tree
	 */
	void exitInsertElementInst(LLVMParser.InsertElementInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#shuffleVectorInst}.
	 * @param ctx the parse tree
	 */
	void enterShuffleVectorInst(LLVMParser.ShuffleVectorInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#shuffleVectorInst}.
	 * @param ctx the parse tree
	 */
	void exitShuffleVectorInst(LLVMParser.ShuffleVectorInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#extractValueInst}.
	 * @param ctx the parse tree
	 */
	void enterExtractValueInst(LLVMParser.ExtractValueInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#extractValueInst}.
	 * @param ctx the parse tree
	 */
	void exitExtractValueInst(LLVMParser.ExtractValueInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#insertValueInst}.
	 * @param ctx the parse tree
	 */
	void enterInsertValueInst(LLVMParser.InsertValueInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#insertValueInst}.
	 * @param ctx the parse tree
	 */
	void exitInsertValueInst(LLVMParser.InsertValueInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#allocaInst}.
	 * @param ctx the parse tree
	 */
	void enterAllocaInst(LLVMParser.AllocaInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#allocaInst}.
	 * @param ctx the parse tree
	 */
	void exitAllocaInst(LLVMParser.AllocaInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optInAlloca}.
	 * @param ctx the parse tree
	 */
	void enterOptInAlloca(LLVMParser.OptInAllocaContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optInAlloca}.
	 * @param ctx the parse tree
	 */
	void exitOptInAlloca(LLVMParser.OptInAllocaContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optSwiftError}.
	 * @param ctx the parse tree
	 */
	void enterOptSwiftError(LLVMParser.OptSwiftErrorContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optSwiftError}.
	 * @param ctx the parse tree
	 */
	void exitOptSwiftError(LLVMParser.OptSwiftErrorContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#loadInst}.
	 * @param ctx the parse tree
	 */
	void enterLoadInst(LLVMParser.LoadInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#loadInst}.
	 * @param ctx the parse tree
	 */
	void exitLoadInst(LLVMParser.LoadInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#storeInst}.
	 * @param ctx the parse tree
	 */
	void enterStoreInst(LLVMParser.StoreInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#storeInst}.
	 * @param ctx the parse tree
	 */
	void exitStoreInst(LLVMParser.StoreInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fenceInst}.
	 * @param ctx the parse tree
	 */
	void enterFenceInst(LLVMParser.FenceInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fenceInst}.
	 * @param ctx the parse tree
	 */
	void exitFenceInst(LLVMParser.FenceInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#cmpXchgInst}.
	 * @param ctx the parse tree
	 */
	void enterCmpXchgInst(LLVMParser.CmpXchgInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#cmpXchgInst}.
	 * @param ctx the parse tree
	 */
	void exitCmpXchgInst(LLVMParser.CmpXchgInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optWeak}.
	 * @param ctx the parse tree
	 */
	void enterOptWeak(LLVMParser.OptWeakContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optWeak}.
	 * @param ctx the parse tree
	 */
	void exitOptWeak(LLVMParser.OptWeakContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#atomicRMWInst}.
	 * @param ctx the parse tree
	 */
	void enterAtomicRMWInst(LLVMParser.AtomicRMWInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#atomicRMWInst}.
	 * @param ctx the parse tree
	 */
	void exitAtomicRMWInst(LLVMParser.AtomicRMWInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#binOp}.
	 * @param ctx the parse tree
	 */
	void enterBinOp(LLVMParser.BinOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#binOp}.
	 * @param ctx the parse tree
	 */
	void exitBinOp(LLVMParser.BinOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#getElementPtrInst}.
	 * @param ctx the parse tree
	 */
	void enterGetElementPtrInst(LLVMParser.GetElementPtrInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#getElementPtrInst}.
	 * @param ctx the parse tree
	 */
	void exitGetElementPtrInst(LLVMParser.GetElementPtrInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#truncInst}.
	 * @param ctx the parse tree
	 */
	void enterTruncInst(LLVMParser.TruncInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#truncInst}.
	 * @param ctx the parse tree
	 */
	void exitTruncInst(LLVMParser.TruncInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#zExtInst}.
	 * @param ctx the parse tree
	 */
	void enterZExtInst(LLVMParser.ZExtInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#zExtInst}.
	 * @param ctx the parse tree
	 */
	void exitZExtInst(LLVMParser.ZExtInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#sExtInst}.
	 * @param ctx the parse tree
	 */
	void enterSExtInst(LLVMParser.SExtInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#sExtInst}.
	 * @param ctx the parse tree
	 */
	void exitSExtInst(LLVMParser.SExtInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fpTruncInst}.
	 * @param ctx the parse tree
	 */
	void enterFpTruncInst(LLVMParser.FpTruncInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fpTruncInst}.
	 * @param ctx the parse tree
	 */
	void exitFpTruncInst(LLVMParser.FpTruncInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fpExtInst}.
	 * @param ctx the parse tree
	 */
	void enterFpExtInst(LLVMParser.FpExtInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fpExtInst}.
	 * @param ctx the parse tree
	 */
	void exitFpExtInst(LLVMParser.FpExtInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fpToUIInst}.
	 * @param ctx the parse tree
	 */
	void enterFpToUIInst(LLVMParser.FpToUIInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fpToUIInst}.
	 * @param ctx the parse tree
	 */
	void exitFpToUIInst(LLVMParser.FpToUIInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fpToSIInst}.
	 * @param ctx the parse tree
	 */
	void enterFpToSIInst(LLVMParser.FpToSIInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fpToSIInst}.
	 * @param ctx the parse tree
	 */
	void exitFpToSIInst(LLVMParser.FpToSIInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#uiToFPInst}.
	 * @param ctx the parse tree
	 */
	void enterUiToFPInst(LLVMParser.UiToFPInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#uiToFPInst}.
	 * @param ctx the parse tree
	 */
	void exitUiToFPInst(LLVMParser.UiToFPInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#siToFPInst}.
	 * @param ctx the parse tree
	 */
	void enterSiToFPInst(LLVMParser.SiToFPInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#siToFPInst}.
	 * @param ctx the parse tree
	 */
	void exitSiToFPInst(LLVMParser.SiToFPInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#ptrToIntInst}.
	 * @param ctx the parse tree
	 */
	void enterPtrToIntInst(LLVMParser.PtrToIntInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#ptrToIntInst}.
	 * @param ctx the parse tree
	 */
	void exitPtrToIntInst(LLVMParser.PtrToIntInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#intToPtrInst}.
	 * @param ctx the parse tree
	 */
	void enterIntToPtrInst(LLVMParser.IntToPtrInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#intToPtrInst}.
	 * @param ctx the parse tree
	 */
	void exitIntToPtrInst(LLVMParser.IntToPtrInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#bitCastInst}.
	 * @param ctx the parse tree
	 */
	void enterBitCastInst(LLVMParser.BitCastInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#bitCastInst}.
	 * @param ctx the parse tree
	 */
	void exitBitCastInst(LLVMParser.BitCastInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#addrSpaceCastInst}.
	 * @param ctx the parse tree
	 */
	void enterAddrSpaceCastInst(LLVMParser.AddrSpaceCastInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#addrSpaceCastInst}.
	 * @param ctx the parse tree
	 */
	void exitAddrSpaceCastInst(LLVMParser.AddrSpaceCastInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#iCmpInst}.
	 * @param ctx the parse tree
	 */
	void enterICmpInst(LLVMParser.ICmpInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#iCmpInst}.
	 * @param ctx the parse tree
	 */
	void exitICmpInst(LLVMParser.ICmpInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fCmpInst}.
	 * @param ctx the parse tree
	 */
	void enterFCmpInst(LLVMParser.FCmpInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fCmpInst}.
	 * @param ctx the parse tree
	 */
	void exitFCmpInst(LLVMParser.FCmpInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#phiInst}.
	 * @param ctx the parse tree
	 */
	void enterPhiInst(LLVMParser.PhiInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#phiInst}.
	 * @param ctx the parse tree
	 */
	void exitPhiInst(LLVMParser.PhiInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#incList}.
	 * @param ctx the parse tree
	 */
	void enterIncList(LLVMParser.IncListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#incList}.
	 * @param ctx the parse tree
	 */
	void exitIncList(LLVMParser.IncListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#inc}.
	 * @param ctx the parse tree
	 */
	void enterInc(LLVMParser.IncContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#inc}.
	 * @param ctx the parse tree
	 */
	void exitInc(LLVMParser.IncContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#selectInst}.
	 * @param ctx the parse tree
	 */
	void enterSelectInst(LLVMParser.SelectInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#selectInst}.
	 * @param ctx the parse tree
	 */
	void exitSelectInst(LLVMParser.SelectInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#callInst}.
	 * @param ctx the parse tree
	 */
	void enterCallInst(LLVMParser.CallInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#callInst}.
	 * @param ctx the parse tree
	 */
	void exitCallInst(LLVMParser.CallInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optTail}.
	 * @param ctx the parse tree
	 */
	void enterOptTail(LLVMParser.OptTailContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optTail}.
	 * @param ctx the parse tree
	 */
	void exitOptTail(LLVMParser.OptTailContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#vaArgInst}.
	 * @param ctx the parse tree
	 */
	void enterVaArgInst(LLVMParser.VaArgInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#vaArgInst}.
	 * @param ctx the parse tree
	 */
	void exitVaArgInst(LLVMParser.VaArgInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#landingPadInst}.
	 * @param ctx the parse tree
	 */
	void enterLandingPadInst(LLVMParser.LandingPadInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#landingPadInst}.
	 * @param ctx the parse tree
	 */
	void exitLandingPadInst(LLVMParser.LandingPadInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optCleanup}.
	 * @param ctx the parse tree
	 */
	void enterOptCleanup(LLVMParser.OptCleanupContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optCleanup}.
	 * @param ctx the parse tree
	 */
	void exitOptCleanup(LLVMParser.OptCleanupContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#clauses}.
	 * @param ctx the parse tree
	 */
	void enterClauses(LLVMParser.ClausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#clauses}.
	 * @param ctx the parse tree
	 */
	void exitClauses(LLVMParser.ClausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#clauseList}.
	 * @param ctx the parse tree
	 */
	void enterClauseList(LLVMParser.ClauseListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#clauseList}.
	 * @param ctx the parse tree
	 */
	void exitClauseList(LLVMParser.ClauseListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#clause}.
	 * @param ctx the parse tree
	 */
	void enterClause(LLVMParser.ClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#clause}.
	 * @param ctx the parse tree
	 */
	void exitClause(LLVMParser.ClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#catchPadInst}.
	 * @param ctx the parse tree
	 */
	void enterCatchPadInst(LLVMParser.CatchPadInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#catchPadInst}.
	 * @param ctx the parse tree
	 */
	void exitCatchPadInst(LLVMParser.CatchPadInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#cleanupPadInst}.
	 * @param ctx the parse tree
	 */
	void enterCleanupPadInst(LLVMParser.CleanupPadInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#cleanupPadInst}.
	 * @param ctx the parse tree
	 */
	void exitCleanupPadInst(LLVMParser.CleanupPadInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#terminator}.
	 * @param ctx the parse tree
	 */
	void enterTerminator(LLVMParser.TerminatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#terminator}.
	 * @param ctx the parse tree
	 */
	void exitTerminator(LLVMParser.TerminatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#retTerm}.
	 * @param ctx the parse tree
	 */
	void enterRetTerm(LLVMParser.RetTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#retTerm}.
	 * @param ctx the parse tree
	 */
	void exitRetTerm(LLVMParser.RetTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#brTerm}.
	 * @param ctx the parse tree
	 */
	void enterBrTerm(LLVMParser.BrTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#brTerm}.
	 * @param ctx the parse tree
	 */
	void exitBrTerm(LLVMParser.BrTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#condBrTerm}.
	 * @param ctx the parse tree
	 */
	void enterCondBrTerm(LLVMParser.CondBrTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#condBrTerm}.
	 * @param ctx the parse tree
	 */
	void exitCondBrTerm(LLVMParser.CondBrTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#switchTerm}.
	 * @param ctx the parse tree
	 */
	void enterSwitchTerm(LLVMParser.SwitchTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#switchTerm}.
	 * @param ctx the parse tree
	 */
	void exitSwitchTerm(LLVMParser.SwitchTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#cases}.
	 * @param ctx the parse tree
	 */
	void enterCases(LLVMParser.CasesContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#cases}.
	 * @param ctx the parse tree
	 */
	void exitCases(LLVMParser.CasesContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#caseList}.
	 * @param ctx the parse tree
	 */
	void enterCaseList(LLVMParser.CaseListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#caseList}.
	 * @param ctx the parse tree
	 */
	void exitCaseList(LLVMParser.CaseListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#llvmCase}.
	 * @param ctx the parse tree
	 */
	void enterLlvmCase(LLVMParser.LlvmCaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#llvmCase}.
	 * @param ctx the parse tree
	 */
	void exitLlvmCase(LLVMParser.LlvmCaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#indirectBrTerm}.
	 * @param ctx the parse tree
	 */
	void enterIndirectBrTerm(LLVMParser.IndirectBrTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#indirectBrTerm}.
	 * @param ctx the parse tree
	 */
	void exitIndirectBrTerm(LLVMParser.IndirectBrTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#labelList}.
	 * @param ctx the parse tree
	 */
	void enterLabelList(LLVMParser.LabelListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#labelList}.
	 * @param ctx the parse tree
	 */
	void exitLabelList(LLVMParser.LabelListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#label}.
	 * @param ctx the parse tree
	 */
	void enterLabel(LLVMParser.LabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#label}.
	 * @param ctx the parse tree
	 */
	void exitLabel(LLVMParser.LabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#invokeTerm}.
	 * @param ctx the parse tree
	 */
	void enterInvokeTerm(LLVMParser.InvokeTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#invokeTerm}.
	 * @param ctx the parse tree
	 */
	void exitInvokeTerm(LLVMParser.InvokeTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#resumeTerm}.
	 * @param ctx the parse tree
	 */
	void enterResumeTerm(LLVMParser.ResumeTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#resumeTerm}.
	 * @param ctx the parse tree
	 */
	void exitResumeTerm(LLVMParser.ResumeTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#catchSwitchTerm}.
	 * @param ctx the parse tree
	 */
	void enterCatchSwitchTerm(LLVMParser.CatchSwitchTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#catchSwitchTerm}.
	 * @param ctx the parse tree
	 */
	void exitCatchSwitchTerm(LLVMParser.CatchSwitchTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#catchRetTerm}.
	 * @param ctx the parse tree
	 */
	void enterCatchRetTerm(LLVMParser.CatchRetTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#catchRetTerm}.
	 * @param ctx the parse tree
	 */
	void exitCatchRetTerm(LLVMParser.CatchRetTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#cleanupRetTerm}.
	 * @param ctx the parse tree
	 */
	void enterCleanupRetTerm(LLVMParser.CleanupRetTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#cleanupRetTerm}.
	 * @param ctx the parse tree
	 */
	void exitCleanupRetTerm(LLVMParser.CleanupRetTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#unreachableTerm}.
	 * @param ctx the parse tree
	 */
	void enterUnreachableTerm(LLVMParser.UnreachableTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#unreachableTerm}.
	 * @param ctx the parse tree
	 */
	void exitUnreachableTerm(LLVMParser.UnreachableTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#unwindTarget}.
	 * @param ctx the parse tree
	 */
	void enterUnwindTarget(LLVMParser.UnwindTargetContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#unwindTarget}.
	 * @param ctx the parse tree
	 */
	void exitUnwindTarget(LLVMParser.UnwindTargetContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#mdTuple}.
	 * @param ctx the parse tree
	 */
	void enterMdTuple(LLVMParser.MdTupleContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#mdTuple}.
	 * @param ctx the parse tree
	 */
	void exitMdTuple(LLVMParser.MdTupleContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#mdFields}.
	 * @param ctx the parse tree
	 */
	void enterMdFields(LLVMParser.MdFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#mdFields}.
	 * @param ctx the parse tree
	 */
	void exitMdFields(LLVMParser.MdFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#mdFieldList}.
	 * @param ctx the parse tree
	 */
	void enterMdFieldList(LLVMParser.MdFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#mdFieldList}.
	 * @param ctx the parse tree
	 */
	void exitMdFieldList(LLVMParser.MdFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#mdField}.
	 * @param ctx the parse tree
	 */
	void enterMdField(LLVMParser.MdFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#mdField}.
	 * @param ctx the parse tree
	 */
	void exitMdField(LLVMParser.MdFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#metadata}.
	 * @param ctx the parse tree
	 */
	void enterMetadata(LLVMParser.MetadataContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#metadata}.
	 * @param ctx the parse tree
	 */
	void exitMetadata(LLVMParser.MetadataContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#mdString}.
	 * @param ctx the parse tree
	 */
	void enterMdString(LLVMParser.MdStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#mdString}.
	 * @param ctx the parse tree
	 */
	void exitMdString(LLVMParser.MdStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#metadataAttachment}.
	 * @param ctx the parse tree
	 */
	void enterMetadataAttachment(LLVMParser.MetadataAttachmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#metadataAttachment}.
	 * @param ctx the parse tree
	 */
	void exitMetadataAttachment(LLVMParser.MetadataAttachmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#mdNode}.
	 * @param ctx the parse tree
	 */
	void enterMdNode(LLVMParser.MdNodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#mdNode}.
	 * @param ctx the parse tree
	 */
	void exitMdNode(LLVMParser.MdNodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#metadataAttachments}.
	 * @param ctx the parse tree
	 */
	void enterMetadataAttachments(LLVMParser.MetadataAttachmentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#metadataAttachments}.
	 * @param ctx the parse tree
	 */
	void exitMetadataAttachments(LLVMParser.MetadataAttachmentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#metadataAttachmentList}.
	 * @param ctx the parse tree
	 */
	void enterMetadataAttachmentList(LLVMParser.MetadataAttachmentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#metadataAttachmentList}.
	 * @param ctx the parse tree
	 */
	void exitMetadataAttachmentList(LLVMParser.MetadataAttachmentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optCommaSepMetadataAttachmentList}.
	 * @param ctx the parse tree
	 */
	void enterOptCommaSepMetadataAttachmentList(LLVMParser.OptCommaSepMetadataAttachmentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optCommaSepMetadataAttachmentList}.
	 * @param ctx the parse tree
	 */
	void exitOptCommaSepMetadataAttachmentList(LLVMParser.OptCommaSepMetadataAttachmentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#commaSepMetadataAttachmentList}.
	 * @param ctx the parse tree
	 */
	void enterCommaSepMetadataAttachmentList(LLVMParser.CommaSepMetadataAttachmentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#commaSepMetadataAttachmentList}.
	 * @param ctx the parse tree
	 */
	void exitCommaSepMetadataAttachmentList(LLVMParser.CommaSepMetadataAttachmentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#specializedMDNode}.
	 * @param ctx the parse tree
	 */
	void enterSpecializedMDNode(LLVMParser.SpecializedMDNodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#specializedMDNode}.
	 * @param ctx the parse tree
	 */
	void exitSpecializedMDNode(LLVMParser.SpecializedMDNodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diCompileUnit}.
	 * @param ctx the parse tree
	 */
	void enterDiCompileUnit(LLVMParser.DiCompileUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diCompileUnit}.
	 * @param ctx the parse tree
	 */
	void exitDiCompileUnit(LLVMParser.DiCompileUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diCompileUnitFields}.
	 * @param ctx the parse tree
	 */
	void enterDiCompileUnitFields(LLVMParser.DiCompileUnitFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diCompileUnitFields}.
	 * @param ctx the parse tree
	 */
	void exitDiCompileUnitFields(LLVMParser.DiCompileUnitFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diCompileUnitFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiCompileUnitFieldList(LLVMParser.DiCompileUnitFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diCompileUnitFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiCompileUnitFieldList(LLVMParser.DiCompileUnitFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diCompileUnitField}.
	 * @param ctx the parse tree
	 */
	void enterDiCompileUnitField(LLVMParser.DiCompileUnitFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diCompileUnitField}.
	 * @param ctx the parse tree
	 */
	void exitDiCompileUnitField(LLVMParser.DiCompileUnitFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diFile}.
	 * @param ctx the parse tree
	 */
	void enterDiFile(LLVMParser.DiFileContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diFile}.
	 * @param ctx the parse tree
	 */
	void exitDiFile(LLVMParser.DiFileContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diFileFields}.
	 * @param ctx the parse tree
	 */
	void enterDiFileFields(LLVMParser.DiFileFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diFileFields}.
	 * @param ctx the parse tree
	 */
	void exitDiFileFields(LLVMParser.DiFileFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diFileFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiFileFieldList(LLVMParser.DiFileFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diFileFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiFileFieldList(LLVMParser.DiFileFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diFileField}.
	 * @param ctx the parse tree
	 */
	void enterDiFileField(LLVMParser.DiFileFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diFileField}.
	 * @param ctx the parse tree
	 */
	void exitDiFileField(LLVMParser.DiFileFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diBasicType}.
	 * @param ctx the parse tree
	 */
	void enterDiBasicType(LLVMParser.DiBasicTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diBasicType}.
	 * @param ctx the parse tree
	 */
	void exitDiBasicType(LLVMParser.DiBasicTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diBasicTypeFields}.
	 * @param ctx the parse tree
	 */
	void enterDiBasicTypeFields(LLVMParser.DiBasicTypeFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diBasicTypeFields}.
	 * @param ctx the parse tree
	 */
	void exitDiBasicTypeFields(LLVMParser.DiBasicTypeFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diBasicTypeFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiBasicTypeFieldList(LLVMParser.DiBasicTypeFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diBasicTypeFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiBasicTypeFieldList(LLVMParser.DiBasicTypeFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diBasicTypeField}.
	 * @param ctx the parse tree
	 */
	void enterDiBasicTypeField(LLVMParser.DiBasicTypeFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diBasicTypeField}.
	 * @param ctx the parse tree
	 */
	void exitDiBasicTypeField(LLVMParser.DiBasicTypeFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diSubroutineType}.
	 * @param ctx the parse tree
	 */
	void enterDiSubroutineType(LLVMParser.DiSubroutineTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diSubroutineType}.
	 * @param ctx the parse tree
	 */
	void exitDiSubroutineType(LLVMParser.DiSubroutineTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diSubroutineTypeFields}.
	 * @param ctx the parse tree
	 */
	void enterDiSubroutineTypeFields(LLVMParser.DiSubroutineTypeFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diSubroutineTypeFields}.
	 * @param ctx the parse tree
	 */
	void exitDiSubroutineTypeFields(LLVMParser.DiSubroutineTypeFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diSubroutineTypeFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiSubroutineTypeFieldList(LLVMParser.DiSubroutineTypeFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diSubroutineTypeFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiSubroutineTypeFieldList(LLVMParser.DiSubroutineTypeFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diSubroutineTypeField}.
	 * @param ctx the parse tree
	 */
	void enterDiSubroutineTypeField(LLVMParser.DiSubroutineTypeFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diSubroutineTypeField}.
	 * @param ctx the parse tree
	 */
	void exitDiSubroutineTypeField(LLVMParser.DiSubroutineTypeFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diDerivedType}.
	 * @param ctx the parse tree
	 */
	void enterDiDerivedType(LLVMParser.DiDerivedTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diDerivedType}.
	 * @param ctx the parse tree
	 */
	void exitDiDerivedType(LLVMParser.DiDerivedTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diDerivedTypeFields}.
	 * @param ctx the parse tree
	 */
	void enterDiDerivedTypeFields(LLVMParser.DiDerivedTypeFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diDerivedTypeFields}.
	 * @param ctx the parse tree
	 */
	void exitDiDerivedTypeFields(LLVMParser.DiDerivedTypeFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diDerivedTypeFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiDerivedTypeFieldList(LLVMParser.DiDerivedTypeFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diDerivedTypeFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiDerivedTypeFieldList(LLVMParser.DiDerivedTypeFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diDerivedTypeField}.
	 * @param ctx the parse tree
	 */
	void enterDiDerivedTypeField(LLVMParser.DiDerivedTypeFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diDerivedTypeField}.
	 * @param ctx the parse tree
	 */
	void exitDiDerivedTypeField(LLVMParser.DiDerivedTypeFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diCompositeType}.
	 * @param ctx the parse tree
	 */
	void enterDiCompositeType(LLVMParser.DiCompositeTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diCompositeType}.
	 * @param ctx the parse tree
	 */
	void exitDiCompositeType(LLVMParser.DiCompositeTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diCompositeTypeFields}.
	 * @param ctx the parse tree
	 */
	void enterDiCompositeTypeFields(LLVMParser.DiCompositeTypeFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diCompositeTypeFields}.
	 * @param ctx the parse tree
	 */
	void exitDiCompositeTypeFields(LLVMParser.DiCompositeTypeFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diCompositeTypeFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiCompositeTypeFieldList(LLVMParser.DiCompositeTypeFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diCompositeTypeFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiCompositeTypeFieldList(LLVMParser.DiCompositeTypeFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diCompositeTypeField}.
	 * @param ctx the parse tree
	 */
	void enterDiCompositeTypeField(LLVMParser.DiCompositeTypeFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diCompositeTypeField}.
	 * @param ctx the parse tree
	 */
	void exitDiCompositeTypeField(LLVMParser.DiCompositeTypeFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diSubrange}.
	 * @param ctx the parse tree
	 */
	void enterDiSubrange(LLVMParser.DiSubrangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diSubrange}.
	 * @param ctx the parse tree
	 */
	void exitDiSubrange(LLVMParser.DiSubrangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diSubrangeFields}.
	 * @param ctx the parse tree
	 */
	void enterDiSubrangeFields(LLVMParser.DiSubrangeFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diSubrangeFields}.
	 * @param ctx the parse tree
	 */
	void exitDiSubrangeFields(LLVMParser.DiSubrangeFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diSubrangeFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiSubrangeFieldList(LLVMParser.DiSubrangeFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diSubrangeFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiSubrangeFieldList(LLVMParser.DiSubrangeFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diSubrangeField}.
	 * @param ctx the parse tree
	 */
	void enterDiSubrangeField(LLVMParser.DiSubrangeFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diSubrangeField}.
	 * @param ctx the parse tree
	 */
	void exitDiSubrangeField(LLVMParser.DiSubrangeFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diEnumerator}.
	 * @param ctx the parse tree
	 */
	void enterDiEnumerator(LLVMParser.DiEnumeratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diEnumerator}.
	 * @param ctx the parse tree
	 */
	void exitDiEnumerator(LLVMParser.DiEnumeratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diEnumeratorFields}.
	 * @param ctx the parse tree
	 */
	void enterDiEnumeratorFields(LLVMParser.DiEnumeratorFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diEnumeratorFields}.
	 * @param ctx the parse tree
	 */
	void exitDiEnumeratorFields(LLVMParser.DiEnumeratorFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diEnumeratorFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiEnumeratorFieldList(LLVMParser.DiEnumeratorFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diEnumeratorFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiEnumeratorFieldList(LLVMParser.DiEnumeratorFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diEnumeratorField}.
	 * @param ctx the parse tree
	 */
	void enterDiEnumeratorField(LLVMParser.DiEnumeratorFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diEnumeratorField}.
	 * @param ctx the parse tree
	 */
	void exitDiEnumeratorField(LLVMParser.DiEnumeratorFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diTemplateTypeParameter}.
	 * @param ctx the parse tree
	 */
	void enterDiTemplateTypeParameter(LLVMParser.DiTemplateTypeParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diTemplateTypeParameter}.
	 * @param ctx the parse tree
	 */
	void exitDiTemplateTypeParameter(LLVMParser.DiTemplateTypeParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diTemplateTypeParameterFields}.
	 * @param ctx the parse tree
	 */
	void enterDiTemplateTypeParameterFields(LLVMParser.DiTemplateTypeParameterFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diTemplateTypeParameterFields}.
	 * @param ctx the parse tree
	 */
	void exitDiTemplateTypeParameterFields(LLVMParser.DiTemplateTypeParameterFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diTemplateTypeParameterFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiTemplateTypeParameterFieldList(LLVMParser.DiTemplateTypeParameterFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diTemplateTypeParameterFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiTemplateTypeParameterFieldList(LLVMParser.DiTemplateTypeParameterFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diTemplateTypeParameterField}.
	 * @param ctx the parse tree
	 */
	void enterDiTemplateTypeParameterField(LLVMParser.DiTemplateTypeParameterFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diTemplateTypeParameterField}.
	 * @param ctx the parse tree
	 */
	void exitDiTemplateTypeParameterField(LLVMParser.DiTemplateTypeParameterFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diTemplateValueParameter}.
	 * @param ctx the parse tree
	 */
	void enterDiTemplateValueParameter(LLVMParser.DiTemplateValueParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diTemplateValueParameter}.
	 * @param ctx the parse tree
	 */
	void exitDiTemplateValueParameter(LLVMParser.DiTemplateValueParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diTemplateValueParameterFields}.
	 * @param ctx the parse tree
	 */
	void enterDiTemplateValueParameterFields(LLVMParser.DiTemplateValueParameterFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diTemplateValueParameterFields}.
	 * @param ctx the parse tree
	 */
	void exitDiTemplateValueParameterFields(LLVMParser.DiTemplateValueParameterFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diTemplateValueParameterFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiTemplateValueParameterFieldList(LLVMParser.DiTemplateValueParameterFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diTemplateValueParameterFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiTemplateValueParameterFieldList(LLVMParser.DiTemplateValueParameterFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diTemplateValueParameterField}.
	 * @param ctx the parse tree
	 */
	void enterDiTemplateValueParameterField(LLVMParser.DiTemplateValueParameterFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diTemplateValueParameterField}.
	 * @param ctx the parse tree
	 */
	void exitDiTemplateValueParameterField(LLVMParser.DiTemplateValueParameterFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diModule}.
	 * @param ctx the parse tree
	 */
	void enterDiModule(LLVMParser.DiModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diModule}.
	 * @param ctx the parse tree
	 */
	void exitDiModule(LLVMParser.DiModuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diModuleFields}.
	 * @param ctx the parse tree
	 */
	void enterDiModuleFields(LLVMParser.DiModuleFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diModuleFields}.
	 * @param ctx the parse tree
	 */
	void exitDiModuleFields(LLVMParser.DiModuleFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diModuleFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiModuleFieldList(LLVMParser.DiModuleFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diModuleFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiModuleFieldList(LLVMParser.DiModuleFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diModuleField}.
	 * @param ctx the parse tree
	 */
	void enterDiModuleField(LLVMParser.DiModuleFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diModuleField}.
	 * @param ctx the parse tree
	 */
	void exitDiModuleField(LLVMParser.DiModuleFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diNamespace}.
	 * @param ctx the parse tree
	 */
	void enterDiNamespace(LLVMParser.DiNamespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diNamespace}.
	 * @param ctx the parse tree
	 */
	void exitDiNamespace(LLVMParser.DiNamespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diNamespaceFields}.
	 * @param ctx the parse tree
	 */
	void enterDiNamespaceFields(LLVMParser.DiNamespaceFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diNamespaceFields}.
	 * @param ctx the parse tree
	 */
	void exitDiNamespaceFields(LLVMParser.DiNamespaceFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diNamespaceFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiNamespaceFieldList(LLVMParser.DiNamespaceFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diNamespaceFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiNamespaceFieldList(LLVMParser.DiNamespaceFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diNamespaceField}.
	 * @param ctx the parse tree
	 */
	void enterDiNamespaceField(LLVMParser.DiNamespaceFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diNamespaceField}.
	 * @param ctx the parse tree
	 */
	void exitDiNamespaceField(LLVMParser.DiNamespaceFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diGlobalVariable}.
	 * @param ctx the parse tree
	 */
	void enterDiGlobalVariable(LLVMParser.DiGlobalVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diGlobalVariable}.
	 * @param ctx the parse tree
	 */
	void exitDiGlobalVariable(LLVMParser.DiGlobalVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diGlobalVariableFields}.
	 * @param ctx the parse tree
	 */
	void enterDiGlobalVariableFields(LLVMParser.DiGlobalVariableFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diGlobalVariableFields}.
	 * @param ctx the parse tree
	 */
	void exitDiGlobalVariableFields(LLVMParser.DiGlobalVariableFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diGlobalVariableFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiGlobalVariableFieldList(LLVMParser.DiGlobalVariableFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diGlobalVariableFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiGlobalVariableFieldList(LLVMParser.DiGlobalVariableFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diGlobalVariableField}.
	 * @param ctx the parse tree
	 */
	void enterDiGlobalVariableField(LLVMParser.DiGlobalVariableFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diGlobalVariableField}.
	 * @param ctx the parse tree
	 */
	void exitDiGlobalVariableField(LLVMParser.DiGlobalVariableFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diSubprogram}.
	 * @param ctx the parse tree
	 */
	void enterDiSubprogram(LLVMParser.DiSubprogramContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diSubprogram}.
	 * @param ctx the parse tree
	 */
	void exitDiSubprogram(LLVMParser.DiSubprogramContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diSubprogramFields}.
	 * @param ctx the parse tree
	 */
	void enterDiSubprogramFields(LLVMParser.DiSubprogramFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diSubprogramFields}.
	 * @param ctx the parse tree
	 */
	void exitDiSubprogramFields(LLVMParser.DiSubprogramFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diSubprogramFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiSubprogramFieldList(LLVMParser.DiSubprogramFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diSubprogramFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiSubprogramFieldList(LLVMParser.DiSubprogramFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diSubprogramField}.
	 * @param ctx the parse tree
	 */
	void enterDiSubprogramField(LLVMParser.DiSubprogramFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diSubprogramField}.
	 * @param ctx the parse tree
	 */
	void exitDiSubprogramField(LLVMParser.DiSubprogramFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diLexicalBlock}.
	 * @param ctx the parse tree
	 */
	void enterDiLexicalBlock(LLVMParser.DiLexicalBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diLexicalBlock}.
	 * @param ctx the parse tree
	 */
	void exitDiLexicalBlock(LLVMParser.DiLexicalBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diLexicalBlockFields}.
	 * @param ctx the parse tree
	 */
	void enterDiLexicalBlockFields(LLVMParser.DiLexicalBlockFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diLexicalBlockFields}.
	 * @param ctx the parse tree
	 */
	void exitDiLexicalBlockFields(LLVMParser.DiLexicalBlockFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diLexicalBlockFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiLexicalBlockFieldList(LLVMParser.DiLexicalBlockFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diLexicalBlockFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiLexicalBlockFieldList(LLVMParser.DiLexicalBlockFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diLexicalBlockField}.
	 * @param ctx the parse tree
	 */
	void enterDiLexicalBlockField(LLVMParser.DiLexicalBlockFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diLexicalBlockField}.
	 * @param ctx the parse tree
	 */
	void exitDiLexicalBlockField(LLVMParser.DiLexicalBlockFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diLexicalBlockFile}.
	 * @param ctx the parse tree
	 */
	void enterDiLexicalBlockFile(LLVMParser.DiLexicalBlockFileContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diLexicalBlockFile}.
	 * @param ctx the parse tree
	 */
	void exitDiLexicalBlockFile(LLVMParser.DiLexicalBlockFileContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diLexicalBlockFileFields}.
	 * @param ctx the parse tree
	 */
	void enterDiLexicalBlockFileFields(LLVMParser.DiLexicalBlockFileFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diLexicalBlockFileFields}.
	 * @param ctx the parse tree
	 */
	void exitDiLexicalBlockFileFields(LLVMParser.DiLexicalBlockFileFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diLexicalBlockFileFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiLexicalBlockFileFieldList(LLVMParser.DiLexicalBlockFileFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diLexicalBlockFileFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiLexicalBlockFileFieldList(LLVMParser.DiLexicalBlockFileFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diLexicalBlockFileField}.
	 * @param ctx the parse tree
	 */
	void enterDiLexicalBlockFileField(LLVMParser.DiLexicalBlockFileFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diLexicalBlockFileField}.
	 * @param ctx the parse tree
	 */
	void exitDiLexicalBlockFileField(LLVMParser.DiLexicalBlockFileFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diLocation}.
	 * @param ctx the parse tree
	 */
	void enterDiLocation(LLVMParser.DiLocationContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diLocation}.
	 * @param ctx the parse tree
	 */
	void exitDiLocation(LLVMParser.DiLocationContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diLocationFields}.
	 * @param ctx the parse tree
	 */
	void enterDiLocationFields(LLVMParser.DiLocationFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diLocationFields}.
	 * @param ctx the parse tree
	 */
	void exitDiLocationFields(LLVMParser.DiLocationFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diLocationFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiLocationFieldList(LLVMParser.DiLocationFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diLocationFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiLocationFieldList(LLVMParser.DiLocationFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diLocationField}.
	 * @param ctx the parse tree
	 */
	void enterDiLocationField(LLVMParser.DiLocationFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diLocationField}.
	 * @param ctx the parse tree
	 */
	void exitDiLocationField(LLVMParser.DiLocationFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diLocalVariable}.
	 * @param ctx the parse tree
	 */
	void enterDiLocalVariable(LLVMParser.DiLocalVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diLocalVariable}.
	 * @param ctx the parse tree
	 */
	void exitDiLocalVariable(LLVMParser.DiLocalVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diLocalVariableFields}.
	 * @param ctx the parse tree
	 */
	void enterDiLocalVariableFields(LLVMParser.DiLocalVariableFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diLocalVariableFields}.
	 * @param ctx the parse tree
	 */
	void exitDiLocalVariableFields(LLVMParser.DiLocalVariableFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diLocalVariableFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiLocalVariableFieldList(LLVMParser.DiLocalVariableFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diLocalVariableFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiLocalVariableFieldList(LLVMParser.DiLocalVariableFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diLocalVariableField}.
	 * @param ctx the parse tree
	 */
	void enterDiLocalVariableField(LLVMParser.DiLocalVariableFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diLocalVariableField}.
	 * @param ctx the parse tree
	 */
	void exitDiLocalVariableField(LLVMParser.DiLocalVariableFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diExpression}.
	 * @param ctx the parse tree
	 */
	void enterDiExpression(LLVMParser.DiExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diExpression}.
	 * @param ctx the parse tree
	 */
	void exitDiExpression(LLVMParser.DiExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diExpressionFields}.
	 * @param ctx the parse tree
	 */
	void enterDiExpressionFields(LLVMParser.DiExpressionFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diExpressionFields}.
	 * @param ctx the parse tree
	 */
	void exitDiExpressionFields(LLVMParser.DiExpressionFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diExpressionFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiExpressionFieldList(LLVMParser.DiExpressionFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diExpressionFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiExpressionFieldList(LLVMParser.DiExpressionFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diExpressionField}.
	 * @param ctx the parse tree
	 */
	void enterDiExpressionField(LLVMParser.DiExpressionFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diExpressionField}.
	 * @param ctx the parse tree
	 */
	void exitDiExpressionField(LLVMParser.DiExpressionFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diGlobalVariableExpression}.
	 * @param ctx the parse tree
	 */
	void enterDiGlobalVariableExpression(LLVMParser.DiGlobalVariableExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diGlobalVariableExpression}.
	 * @param ctx the parse tree
	 */
	void exitDiGlobalVariableExpression(LLVMParser.DiGlobalVariableExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diGlobalVariableExpressionFields}.
	 * @param ctx the parse tree
	 */
	void enterDiGlobalVariableExpressionFields(LLVMParser.DiGlobalVariableExpressionFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diGlobalVariableExpressionFields}.
	 * @param ctx the parse tree
	 */
	void exitDiGlobalVariableExpressionFields(LLVMParser.DiGlobalVariableExpressionFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diGlobalVariableExpressionFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiGlobalVariableExpressionFieldList(LLVMParser.DiGlobalVariableExpressionFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diGlobalVariableExpressionFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiGlobalVariableExpressionFieldList(LLVMParser.DiGlobalVariableExpressionFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diGlobalVariableExpressionField}.
	 * @param ctx the parse tree
	 */
	void enterDiGlobalVariableExpressionField(LLVMParser.DiGlobalVariableExpressionFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diGlobalVariableExpressionField}.
	 * @param ctx the parse tree
	 */
	void exitDiGlobalVariableExpressionField(LLVMParser.DiGlobalVariableExpressionFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diObjCProperty}.
	 * @param ctx the parse tree
	 */
	void enterDiObjCProperty(LLVMParser.DiObjCPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diObjCProperty}.
	 * @param ctx the parse tree
	 */
	void exitDiObjCProperty(LLVMParser.DiObjCPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diObjCPropertyFields}.
	 * @param ctx the parse tree
	 */
	void enterDiObjCPropertyFields(LLVMParser.DiObjCPropertyFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diObjCPropertyFields}.
	 * @param ctx the parse tree
	 */
	void exitDiObjCPropertyFields(LLVMParser.DiObjCPropertyFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diObjCPropertyFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiObjCPropertyFieldList(LLVMParser.DiObjCPropertyFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diObjCPropertyFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiObjCPropertyFieldList(LLVMParser.DiObjCPropertyFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diObjCPropertyField}.
	 * @param ctx the parse tree
	 */
	void enterDiObjCPropertyField(LLVMParser.DiObjCPropertyFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diObjCPropertyField}.
	 * @param ctx the parse tree
	 */
	void exitDiObjCPropertyField(LLVMParser.DiObjCPropertyFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diImportedEntity}.
	 * @param ctx the parse tree
	 */
	void enterDiImportedEntity(LLVMParser.DiImportedEntityContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diImportedEntity}.
	 * @param ctx the parse tree
	 */
	void exitDiImportedEntity(LLVMParser.DiImportedEntityContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diImportedEntityFields}.
	 * @param ctx the parse tree
	 */
	void enterDiImportedEntityFields(LLVMParser.DiImportedEntityFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diImportedEntityFields}.
	 * @param ctx the parse tree
	 */
	void exitDiImportedEntityFields(LLVMParser.DiImportedEntityFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diImportedEntityFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiImportedEntityFieldList(LLVMParser.DiImportedEntityFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diImportedEntityFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiImportedEntityFieldList(LLVMParser.DiImportedEntityFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diImportedEntityField}.
	 * @param ctx the parse tree
	 */
	void enterDiImportedEntityField(LLVMParser.DiImportedEntityFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diImportedEntityField}.
	 * @param ctx the parse tree
	 */
	void exitDiImportedEntityField(LLVMParser.DiImportedEntityFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diMacro}.
	 * @param ctx the parse tree
	 */
	void enterDiMacro(LLVMParser.DiMacroContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diMacro}.
	 * @param ctx the parse tree
	 */
	void exitDiMacro(LLVMParser.DiMacroContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diMacroFields}.
	 * @param ctx the parse tree
	 */
	void enterDiMacroFields(LLVMParser.DiMacroFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diMacroFields}.
	 * @param ctx the parse tree
	 */
	void exitDiMacroFields(LLVMParser.DiMacroFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diMacroFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiMacroFieldList(LLVMParser.DiMacroFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diMacroFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiMacroFieldList(LLVMParser.DiMacroFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diMacroField}.
	 * @param ctx the parse tree
	 */
	void enterDiMacroField(LLVMParser.DiMacroFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diMacroField}.
	 * @param ctx the parse tree
	 */
	void exitDiMacroField(LLVMParser.DiMacroFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diMacroFile}.
	 * @param ctx the parse tree
	 */
	void enterDiMacroFile(LLVMParser.DiMacroFileContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diMacroFile}.
	 * @param ctx the parse tree
	 */
	void exitDiMacroFile(LLVMParser.DiMacroFileContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diMacroFileFields}.
	 * @param ctx the parse tree
	 */
	void enterDiMacroFileFields(LLVMParser.DiMacroFileFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diMacroFileFields}.
	 * @param ctx the parse tree
	 */
	void exitDiMacroFileFields(LLVMParser.DiMacroFileFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diMacroFileFieldList}.
	 * @param ctx the parse tree
	 */
	void enterDiMacroFileFieldList(LLVMParser.DiMacroFileFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diMacroFileFieldList}.
	 * @param ctx the parse tree
	 */
	void exitDiMacroFileFieldList(LLVMParser.DiMacroFileFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diMacroFileField}.
	 * @param ctx the parse tree
	 */
	void enterDiMacroFileField(LLVMParser.DiMacroFileFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diMacroFileField}.
	 * @param ctx the parse tree
	 */
	void exitDiMacroFileField(LLVMParser.DiMacroFileFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#genericDINode}.
	 * @param ctx the parse tree
	 */
	void enterGenericDINode(LLVMParser.GenericDINodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#genericDINode}.
	 * @param ctx the parse tree
	 */
	void exitGenericDINode(LLVMParser.GenericDINodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#genericDINodeFields}.
	 * @param ctx the parse tree
	 */
	void enterGenericDINodeFields(LLVMParser.GenericDINodeFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#genericDINodeFields}.
	 * @param ctx the parse tree
	 */
	void exitGenericDINodeFields(LLVMParser.GenericDINodeFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#genericDINodeFieldList}.
	 * @param ctx the parse tree
	 */
	void enterGenericDINodeFieldList(LLVMParser.GenericDINodeFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#genericDINodeFieldList}.
	 * @param ctx the parse tree
	 */
	void exitGenericDINodeFieldList(LLVMParser.GenericDINodeFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#genericDINodeField}.
	 * @param ctx the parse tree
	 */
	void enterGenericDINodeField(LLVMParser.GenericDINodeFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#genericDINodeField}.
	 * @param ctx the parse tree
	 */
	void exitGenericDINodeField(LLVMParser.GenericDINodeFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fileField}.
	 * @param ctx the parse tree
	 */
	void enterFileField(LLVMParser.FileFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fileField}.
	 * @param ctx the parse tree
	 */
	void exitFileField(LLVMParser.FileFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#isOptimizedField}.
	 * @param ctx the parse tree
	 */
	void enterIsOptimizedField(LLVMParser.IsOptimizedFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#isOptimizedField}.
	 * @param ctx the parse tree
	 */
	void exitIsOptimizedField(LLVMParser.IsOptimizedFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#tagField}.
	 * @param ctx the parse tree
	 */
	void enterTagField(LLVMParser.TagFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#tagField}.
	 * @param ctx the parse tree
	 */
	void exitTagField(LLVMParser.TagFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#nameField}.
	 * @param ctx the parse tree
	 */
	void enterNameField(LLVMParser.NameFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#nameField}.
	 * @param ctx the parse tree
	 */
	void exitNameField(LLVMParser.NameFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#sizeField}.
	 * @param ctx the parse tree
	 */
	void enterSizeField(LLVMParser.SizeFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#sizeField}.
	 * @param ctx the parse tree
	 */
	void exitSizeField(LLVMParser.SizeFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#alignField}.
	 * @param ctx the parse tree
	 */
	void enterAlignField(LLVMParser.AlignFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#alignField}.
	 * @param ctx the parse tree
	 */
	void exitAlignField(LLVMParser.AlignFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#flagsField}.
	 * @param ctx the parse tree
	 */
	void enterFlagsField(LLVMParser.FlagsFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#flagsField}.
	 * @param ctx the parse tree
	 */
	void exitFlagsField(LLVMParser.FlagsFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#lineField}.
	 * @param ctx the parse tree
	 */
	void enterLineField(LLVMParser.LineFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#lineField}.
	 * @param ctx the parse tree
	 */
	void exitLineField(LLVMParser.LineFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#scopeField}.
	 * @param ctx the parse tree
	 */
	void enterScopeField(LLVMParser.ScopeFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#scopeField}.
	 * @param ctx the parse tree
	 */
	void exitScopeField(LLVMParser.ScopeFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#baseTypeField}.
	 * @param ctx the parse tree
	 */
	void enterBaseTypeField(LLVMParser.BaseTypeFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#baseTypeField}.
	 * @param ctx the parse tree
	 */
	void exitBaseTypeField(LLVMParser.BaseTypeFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#offsetField}.
	 * @param ctx the parse tree
	 */
	void enterOffsetField(LLVMParser.OffsetFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#offsetField}.
	 * @param ctx the parse tree
	 */
	void exitOffsetField(LLVMParser.OffsetFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#templateParamsField}.
	 * @param ctx the parse tree
	 */
	void enterTemplateParamsField(LLVMParser.TemplateParamsFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#templateParamsField}.
	 * @param ctx the parse tree
	 */
	void exitTemplateParamsField(LLVMParser.TemplateParamsFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#intOrMDField}.
	 * @param ctx the parse tree
	 */
	void enterIntOrMDField(LLVMParser.IntOrMDFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#intOrMDField}.
	 * @param ctx the parse tree
	 */
	void exitIntOrMDField(LLVMParser.IntOrMDFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#typeField}.
	 * @param ctx the parse tree
	 */
	void enterTypeField(LLVMParser.TypeFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#typeField}.
	 * @param ctx the parse tree
	 */
	void exitTypeField(LLVMParser.TypeFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#linkageNameField}.
	 * @param ctx the parse tree
	 */
	void enterLinkageNameField(LLVMParser.LinkageNameFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#linkageNameField}.
	 * @param ctx the parse tree
	 */
	void exitLinkageNameField(LLVMParser.LinkageNameFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#isLocalField}.
	 * @param ctx the parse tree
	 */
	void enterIsLocalField(LLVMParser.IsLocalFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#isLocalField}.
	 * @param ctx the parse tree
	 */
	void exitIsLocalField(LLVMParser.IsLocalFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#isDefinitionField}.
	 * @param ctx the parse tree
	 */
	void enterIsDefinitionField(LLVMParser.IsDefinitionFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#isDefinitionField}.
	 * @param ctx the parse tree
	 */
	void exitIsDefinitionField(LLVMParser.IsDefinitionFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#declarationField}.
	 * @param ctx the parse tree
	 */
	void enterDeclarationField(LLVMParser.DeclarationFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#declarationField}.
	 * @param ctx the parse tree
	 */
	void exitDeclarationField(LLVMParser.DeclarationFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#columnField}.
	 * @param ctx the parse tree
	 */
	void enterColumnField(LLVMParser.ColumnFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#columnField}.
	 * @param ctx the parse tree
	 */
	void exitColumnField(LLVMParser.ColumnFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#typeMacinfoField}.
	 * @param ctx the parse tree
	 */
	void enterTypeMacinfoField(LLVMParser.TypeMacinfoFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#typeMacinfoField}.
	 * @param ctx the parse tree
	 */
	void exitTypeMacinfoField(LLVMParser.TypeMacinfoFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#checksumkind}.
	 * @param ctx the parse tree
	 */
	void enterChecksumkind(LLVMParser.ChecksumkindContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#checksumkind}.
	 * @param ctx the parse tree
	 */
	void exitChecksumkind(LLVMParser.ChecksumkindContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diFlagList}.
	 * @param ctx the parse tree
	 */
	void enterDiFlagList(LLVMParser.DiFlagListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diFlagList}.
	 * @param ctx the parse tree
	 */
	void exitDiFlagList(LLVMParser.DiFlagListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#diFlag}.
	 * @param ctx the parse tree
	 */
	void enterDiFlag(LLVMParser.DiFlagContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#diFlag}.
	 * @param ctx the parse tree
	 */
	void exitDiFlag(LLVMParser.DiFlagContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#dwarfAttEncoding}.
	 * @param ctx the parse tree
	 */
	void enterDwarfAttEncoding(LLVMParser.DwarfAttEncodingContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#dwarfAttEncoding}.
	 * @param ctx the parse tree
	 */
	void exitDwarfAttEncoding(LLVMParser.DwarfAttEncodingContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#dwarfCC}.
	 * @param ctx the parse tree
	 */
	void enterDwarfCC(LLVMParser.DwarfCCContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#dwarfCC}.
	 * @param ctx the parse tree
	 */
	void exitDwarfCC(LLVMParser.DwarfCCContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#dwarfLang}.
	 * @param ctx the parse tree
	 */
	void enterDwarfLang(LLVMParser.DwarfLangContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#dwarfLang}.
	 * @param ctx the parse tree
	 */
	void exitDwarfLang(LLVMParser.DwarfLangContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#dwarfMacinfo}.
	 * @param ctx the parse tree
	 */
	void enterDwarfMacinfo(LLVMParser.DwarfMacinfoContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#dwarfMacinfo}.
	 * @param ctx the parse tree
	 */
	void exitDwarfMacinfo(LLVMParser.DwarfMacinfoContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#dwarfOp}.
	 * @param ctx the parse tree
	 */
	void enterDwarfOp(LLVMParser.DwarfOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#dwarfOp}.
	 * @param ctx the parse tree
	 */
	void exitDwarfOp(LLVMParser.DwarfOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#dwarfTag}.
	 * @param ctx the parse tree
	 */
	void enterDwarfTag(LLVMParser.DwarfTagContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#dwarfTag}.
	 * @param ctx the parse tree
	 */
	void exitDwarfTag(LLVMParser.DwarfTagContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#dwarfVirtuality}.
	 * @param ctx the parse tree
	 */
	void enterDwarfVirtuality(LLVMParser.DwarfVirtualityContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#dwarfVirtuality}.
	 * @param ctx the parse tree
	 */
	void exitDwarfVirtuality(LLVMParser.DwarfVirtualityContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#emissionKind}.
	 * @param ctx the parse tree
	 */
	void enterEmissionKind(LLVMParser.EmissionKindContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#emissionKind}.
	 * @param ctx the parse tree
	 */
	void exitEmissionKind(LLVMParser.EmissionKindContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#typeValues}.
	 * @param ctx the parse tree
	 */
	void enterTypeValues(LLVMParser.TypeValuesContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#typeValues}.
	 * @param ctx the parse tree
	 */
	void exitTypeValues(LLVMParser.TypeValuesContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#typeValueList}.
	 * @param ctx the parse tree
	 */
	void enterTypeValueList(LLVMParser.TypeValueListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#typeValueList}.
	 * @param ctx the parse tree
	 */
	void exitTypeValueList(LLVMParser.TypeValueListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#commaSepTypeValueList}.
	 * @param ctx the parse tree
	 */
	void enterCommaSepTypeValueList(LLVMParser.CommaSepTypeValueListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#commaSepTypeValueList}.
	 * @param ctx the parse tree
	 */
	void exitCommaSepTypeValueList(LLVMParser.CommaSepTypeValueListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#typeValue}.
	 * @param ctx the parse tree
	 */
	void enterTypeValue(LLVMParser.TypeValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#typeValue}.
	 * @param ctx the parse tree
	 */
	void exitTypeValue(LLVMParser.TypeValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#typeConsts}.
	 * @param ctx the parse tree
	 */
	void enterTypeConsts(LLVMParser.TypeConstsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#typeConsts}.
	 * @param ctx the parse tree
	 */
	void exitTypeConsts(LLVMParser.TypeConstsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#typeConstList}.
	 * @param ctx the parse tree
	 */
	void enterTypeConstList(LLVMParser.TypeConstListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#typeConstList}.
	 * @param ctx the parse tree
	 */
	void exitTypeConstList(LLVMParser.TypeConstListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#typeConst}.
	 * @param ctx the parse tree
	 */
	void enterTypeConst(LLVMParser.TypeConstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#typeConst}.
	 * @param ctx the parse tree
	 */
	void exitTypeConst(LLVMParser.TypeConstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#alignment}.
	 * @param ctx the parse tree
	 */
	void enterAlignment(LLVMParser.AlignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#alignment}.
	 * @param ctx the parse tree
	 */
	void exitAlignment(LLVMParser.AlignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#allocSize}.
	 * @param ctx the parse tree
	 */
	void enterAllocSize(LLVMParser.AllocSizeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#allocSize}.
	 * @param ctx the parse tree
	 */
	void exitAllocSize(LLVMParser.AllocSizeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#args}.
	 * @param ctx the parse tree
	 */
	void enterArgs(LLVMParser.ArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#args}.
	 * @param ctx the parse tree
	 */
	void exitArgs(LLVMParser.ArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#argList}.
	 * @param ctx the parse tree
	 */
	void enterArgList(LLVMParser.ArgListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#argList}.
	 * @param ctx the parse tree
	 */
	void exitArgList(LLVMParser.ArgListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterArg(LLVMParser.ArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitArg(LLVMParser.ArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#atomicOrdering}.
	 * @param ctx the parse tree
	 */
	void enterAtomicOrdering(LLVMParser.AtomicOrderingContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#atomicOrdering}.
	 * @param ctx the parse tree
	 */
	void exitAtomicOrdering(LLVMParser.AtomicOrderingContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optCallingConv}.
	 * @param ctx the parse tree
	 */
	void enterOptCallingConv(LLVMParser.OptCallingConvContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optCallingConv}.
	 * @param ctx the parse tree
	 */
	void exitOptCallingConv(LLVMParser.OptCallingConvContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#callingConv}.
	 * @param ctx the parse tree
	 */
	void enterCallingConv(LLVMParser.CallingConvContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#callingConv}.
	 * @param ctx the parse tree
	 */
	void exitCallingConv(LLVMParser.CallingConvContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optComdat}.
	 * @param ctx the parse tree
	 */
	void enterOptComdat(LLVMParser.OptComdatContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optComdat}.
	 * @param ctx the parse tree
	 */
	void exitOptComdat(LLVMParser.OptComdatContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#comdat}.
	 * @param ctx the parse tree
	 */
	void enterComdat(LLVMParser.ComdatContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#comdat}.
	 * @param ctx the parse tree
	 */
	void exitComdat(LLVMParser.ComdatContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#dereferenceable}.
	 * @param ctx the parse tree
	 */
	void enterDereferenceable(LLVMParser.DereferenceableContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#dereferenceable}.
	 * @param ctx the parse tree
	 */
	void exitDereferenceable(LLVMParser.DereferenceableContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optDLLStorageClass}.
	 * @param ctx the parse tree
	 */
	void enterOptDLLStorageClass(LLVMParser.OptDLLStorageClassContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optDLLStorageClass}.
	 * @param ctx the parse tree
	 */
	void exitOptDLLStorageClass(LLVMParser.OptDLLStorageClassContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#dllStorageClass}.
	 * @param ctx the parse tree
	 */
	void enterDllStorageClass(LLVMParser.DllStorageClassContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#dllStorageClass}.
	 * @param ctx the parse tree
	 */
	void exitDllStorageClass(LLVMParser.DllStorageClassContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optExact}.
	 * @param ctx the parse tree
	 */
	void enterOptExact(LLVMParser.OptExactContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optExact}.
	 * @param ctx the parse tree
	 */
	void exitOptExact(LLVMParser.OptExactContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#exceptionArgs}.
	 * @param ctx the parse tree
	 */
	void enterExceptionArgs(LLVMParser.ExceptionArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#exceptionArgs}.
	 * @param ctx the parse tree
	 */
	void exitExceptionArgs(LLVMParser.ExceptionArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#exceptionArgList}.
	 * @param ctx the parse tree
	 */
	void enterExceptionArgList(LLVMParser.ExceptionArgListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#exceptionArgList}.
	 * @param ctx the parse tree
	 */
	void exitExceptionArgList(LLVMParser.ExceptionArgListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#exceptionArg}.
	 * @param ctx the parse tree
	 */
	void enterExceptionArg(LLVMParser.ExceptionArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#exceptionArg}.
	 * @param ctx the parse tree
	 */
	void exitExceptionArg(LLVMParser.ExceptionArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#exceptionScope}.
	 * @param ctx the parse tree
	 */
	void enterExceptionScope(LLVMParser.ExceptionScopeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#exceptionScope}.
	 * @param ctx the parse tree
	 */
	void exitExceptionScope(LLVMParser.ExceptionScopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fastMathFlags}.
	 * @param ctx the parse tree
	 */
	void enterFastMathFlags(LLVMParser.FastMathFlagsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fastMathFlags}.
	 * @param ctx the parse tree
	 */
	void exitFastMathFlags(LLVMParser.FastMathFlagsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fastMathFlagList}.
	 * @param ctx the parse tree
	 */
	void enterFastMathFlagList(LLVMParser.FastMathFlagListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fastMathFlagList}.
	 * @param ctx the parse tree
	 */
	void exitFastMathFlagList(LLVMParser.FastMathFlagListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fastMathFlag}.
	 * @param ctx the parse tree
	 */
	void enterFastMathFlag(LLVMParser.FastMathFlagContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fastMathFlag}.
	 * @param ctx the parse tree
	 */
	void exitFastMathFlag(LLVMParser.FastMathFlagContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#fpred}.
	 * @param ctx the parse tree
	 */
	void enterFpred(LLVMParser.FpredContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#fpred}.
	 * @param ctx the parse tree
	 */
	void exitFpred(LLVMParser.FpredContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#funcAttrs}.
	 * @param ctx the parse tree
	 */
	void enterFuncAttrs(LLVMParser.FuncAttrsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#funcAttrs}.
	 * @param ctx the parse tree
	 */
	void exitFuncAttrs(LLVMParser.FuncAttrsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#funcAttrList}.
	 * @param ctx the parse tree
	 */
	void enterFuncAttrList(LLVMParser.FuncAttrListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#funcAttrList}.
	 * @param ctx the parse tree
	 */
	void exitFuncAttrList(LLVMParser.FuncAttrListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#funcAttr}.
	 * @param ctx the parse tree
	 */
	void enterFuncAttr(LLVMParser.FuncAttrContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#funcAttr}.
	 * @param ctx the parse tree
	 */
	void exitFuncAttr(LLVMParser.FuncAttrContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optInBounds}.
	 * @param ctx the parse tree
	 */
	void enterOptInBounds(LLVMParser.OptInBoundsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optInBounds}.
	 * @param ctx the parse tree
	 */
	void exitOptInBounds(LLVMParser.OptInBoundsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#indices}.
	 * @param ctx the parse tree
	 */
	void enterIndices(LLVMParser.IndicesContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#indices}.
	 * @param ctx the parse tree
	 */
	void exitIndices(LLVMParser.IndicesContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#indexList}.
	 * @param ctx the parse tree
	 */
	void enterIndexList(LLVMParser.IndexListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#indexList}.
	 * @param ctx the parse tree
	 */
	void exitIndexList(LLVMParser.IndexListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#index}.
	 * @param ctx the parse tree
	 */
	void enterIndex(LLVMParser.IndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#index}.
	 * @param ctx the parse tree
	 */
	void exitIndex(LLVMParser.IndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#iPred}.
	 * @param ctx the parse tree
	 */
	void enterIPred(LLVMParser.IPredContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#iPred}.
	 * @param ctx the parse tree
	 */
	void exitIPred(LLVMParser.IPredContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optLinkage}.
	 * @param ctx the parse tree
	 */
	void enterOptLinkage(LLVMParser.OptLinkageContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optLinkage}.
	 * @param ctx the parse tree
	 */
	void exitOptLinkage(LLVMParser.OptLinkageContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#linkage}.
	 * @param ctx the parse tree
	 */
	void enterLinkage(LLVMParser.LinkageContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#linkage}.
	 * @param ctx the parse tree
	 */
	void exitLinkage(LLVMParser.LinkageContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optExternLinkage}.
	 * @param ctx the parse tree
	 */
	void enterOptExternLinkage(LLVMParser.OptExternLinkageContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optExternLinkage}.
	 * @param ctx the parse tree
	 */
	void exitOptExternLinkage(LLVMParser.OptExternLinkageContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#externLinkage}.
	 * @param ctx the parse tree
	 */
	void enterExternLinkage(LLVMParser.ExternLinkageContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#externLinkage}.
	 * @param ctx the parse tree
	 */
	void exitExternLinkage(LLVMParser.ExternLinkageContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#operandBundles}.
	 * @param ctx the parse tree
	 */
	void enterOperandBundles(LLVMParser.OperandBundlesContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#operandBundles}.
	 * @param ctx the parse tree
	 */
	void exitOperandBundles(LLVMParser.OperandBundlesContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#operandBundleList}.
	 * @param ctx the parse tree
	 */
	void enterOperandBundleList(LLVMParser.OperandBundleListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#operandBundleList}.
	 * @param ctx the parse tree
	 */
	void exitOperandBundleList(LLVMParser.OperandBundleListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#operandBundle}.
	 * @param ctx the parse tree
	 */
	void enterOperandBundle(LLVMParser.OperandBundleContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#operandBundle}.
	 * @param ctx the parse tree
	 */
	void exitOperandBundle(LLVMParser.OperandBundleContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#overflowFlags}.
	 * @param ctx the parse tree
	 */
	void enterOverflowFlags(LLVMParser.OverflowFlagsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#overflowFlags}.
	 * @param ctx the parse tree
	 */
	void exitOverflowFlags(LLVMParser.OverflowFlagsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#overflowFlagList}.
	 * @param ctx the parse tree
	 */
	void enterOverflowFlagList(LLVMParser.OverflowFlagListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#overflowFlagList}.
	 * @param ctx the parse tree
	 */
	void exitOverflowFlagList(LLVMParser.OverflowFlagListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#overflowFlag}.
	 * @param ctx the parse tree
	 */
	void enterOverflowFlag(LLVMParser.OverflowFlagContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#overflowFlag}.
	 * @param ctx the parse tree
	 */
	void exitOverflowFlag(LLVMParser.OverflowFlagContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#paramAttrs}.
	 * @param ctx the parse tree
	 */
	void enterParamAttrs(LLVMParser.ParamAttrsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#paramAttrs}.
	 * @param ctx the parse tree
	 */
	void exitParamAttrs(LLVMParser.ParamAttrsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#paramAttrList}.
	 * @param ctx the parse tree
	 */
	void enterParamAttrList(LLVMParser.ParamAttrListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#paramAttrList}.
	 * @param ctx the parse tree
	 */
	void exitParamAttrList(LLVMParser.ParamAttrListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#paramAttr}.
	 * @param ctx the parse tree
	 */
	void enterParamAttr(LLVMParser.ParamAttrContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#paramAttr}.
	 * @param ctx the parse tree
	 */
	void exitParamAttr(LLVMParser.ParamAttrContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#params}.
	 * @param ctx the parse tree
	 */
	void enterParams(LLVMParser.ParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#params}.
	 * @param ctx the parse tree
	 */
	void exitParams(LLVMParser.ParamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#paramList}.
	 * @param ctx the parse tree
	 */
	void enterParamList(LLVMParser.ParamListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#paramList}.
	 * @param ctx the parse tree
	 */
	void exitParamList(LLVMParser.ParamListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(LLVMParser.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(LLVMParser.ParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optPreemptionSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterOptPreemptionSpecifier(LLVMParser.OptPreemptionSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optPreemptionSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitOptPreemptionSpecifier(LLVMParser.OptPreemptionSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#preemptionSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterPreemptionSpecifier(LLVMParser.PreemptionSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#preemptionSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitPreemptionSpecifier(LLVMParser.PreemptionSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#returnAttrs}.
	 * @param ctx the parse tree
	 */
	void enterReturnAttrs(LLVMParser.ReturnAttrsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#returnAttrs}.
	 * @param ctx the parse tree
	 */
	void exitReturnAttrs(LLVMParser.ReturnAttrsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#returnAttrList}.
	 * @param ctx the parse tree
	 */
	void enterReturnAttrList(LLVMParser.ReturnAttrListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#returnAttrList}.
	 * @param ctx the parse tree
	 */
	void exitReturnAttrList(LLVMParser.ReturnAttrListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#returnAttr}.
	 * @param ctx the parse tree
	 */
	void enterReturnAttr(LLVMParser.ReturnAttrContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#returnAttr}.
	 * @param ctx the parse tree
	 */
	void exitReturnAttr(LLVMParser.ReturnAttrContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#section}.
	 * @param ctx the parse tree
	 */
	void enterSection(LLVMParser.SectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#section}.
	 * @param ctx the parse tree
	 */
	void exitSection(LLVMParser.SectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#stackAlignment}.
	 * @param ctx the parse tree
	 */
	void enterStackAlignment(LLVMParser.StackAlignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#stackAlignment}.
	 * @param ctx the parse tree
	 */
	void exitStackAlignment(LLVMParser.StackAlignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#optSyncScope}.
	 * @param ctx the parse tree
	 */
	void enterOptSyncScope(LLVMParser.OptSyncScopeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#optSyncScope}.
	 * @param ctx the parse tree
	 */
	void exitOptSyncScope(LLVMParser.OptSyncScopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#threadLocal}.
	 * @param ctx the parse tree
	 */
	void enterThreadLocal(LLVMParser.ThreadLocalContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#threadLocal}.
	 * @param ctx the parse tree
	 */
	void exitThreadLocal(LLVMParser.ThreadLocalContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#tlsModel}.
	 * @param ctx the parse tree
	 */
	void enterTlsModel(LLVMParser.TlsModelContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#tlsModel}.
	 * @param ctx the parse tree
	 */
	void exitTlsModel(LLVMParser.TlsModelContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#unnamedAddr}.
	 * @param ctx the parse tree
	 */
	void enterUnnamedAddr(LLVMParser.UnnamedAddrContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#unnamedAddr}.
	 * @param ctx the parse tree
	 */
	void exitUnnamedAddr(LLVMParser.UnnamedAddrContext ctx);
	/**
	 * Enter a parse tree produced by {@link LLVMParser#visibility}.
	 * @param ctx the parse tree
	 */
	void enterVisibility(LLVMParser.VisibilityContext ctx);
	/**
	 * Exit a parse tree produced by {@link LLVMParser#visibility}.
	 * @param ctx the parse tree
	 */
	void exitVisibility(LLVMParser.VisibilityContext ctx);
}