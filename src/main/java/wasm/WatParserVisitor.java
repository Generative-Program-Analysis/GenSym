package gensym.wasm;
// Generated from WatParser.g4 by ANTLR 4.12.0
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link WatParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface WatParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link WatParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(WatParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(WatParser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#valueType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueType(WatParser.ValueTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#elemType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElemType(WatParser.ElemTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#globalType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalType(WatParser.GlobalTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#defType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefType(WatParser.DefTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#funcParamType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncParamType(WatParser.FuncParamTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#funcType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncType(WatParser.FuncTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#tableType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableType(WatParser.TableTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#memoryType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemoryType(WatParser.MemoryTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#typeUse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeUse(WatParser.TypeUseContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(WatParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#var_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar_(WatParser.Var_Context ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#bindVar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBindVar(WatParser.BindVarContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#instr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstr(WatParser.InstrContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#plainInstr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlainInstr(WatParser.PlainInstrContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#callInstr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallInstr(WatParser.CallInstrContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#callInstrParams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallInstrParams(WatParser.CallInstrParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#callInstrInstr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallInstrInstr(WatParser.CallInstrInstrContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#callInstrParamsInstr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallInstrParamsInstr(WatParser.CallInstrParamsInstrContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#callInstrResultsInstr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallInstrResultsInstr(WatParser.CallInstrResultsInstrContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#blockInstr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockInstr(WatParser.BlockInstrContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#blockType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockType(WatParser.BlockTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(WatParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(WatParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#expr1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr1(WatParser.Expr1Context ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#callExprType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallExprType(WatParser.CallExprTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#callExprParams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallExprParams(WatParser.CallExprParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#callExprResults}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallExprResults(WatParser.CallExprResultsContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#ifBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfBlock(WatParser.IfBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#instrList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstrList(WatParser.InstrListContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#constExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstExpr(WatParser.ConstExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#func_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_(WatParser.Func_Context ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#funcFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncFields(WatParser.FuncFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#funcFieldsBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncFieldsBody(WatParser.FuncFieldsBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#funcLocalParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncLocalParam(WatParser.FuncLocalParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#funcBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncBody(WatParser.FuncBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#offset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOffset(WatParser.OffsetContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElem(WatParser.ElemContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable(WatParser.TableContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#tableFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableFields(WatParser.TableFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#data}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitData(WatParser.DataContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#memory}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemory(WatParser.MemoryContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#memoryFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemoryFields(WatParser.MemoryFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#sglobal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSglobal(WatParser.SglobalContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#globalFields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalFields(WatParser.GlobalFieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#importDesc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportDesc(WatParser.ImportDescContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#simport}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimport(WatParser.SimportContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#inlineImport}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInlineImport(WatParser.InlineImportContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#exportDesc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExportDesc(WatParser.ExportDescContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#export_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExport_(WatParser.Export_Context ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#inlineExport}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInlineExport(WatParser.InlineExportContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#type_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_(WatParser.Type_Context ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#typeDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDef(WatParser.TypeDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#start_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart_(WatParser.Start_Context ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#moduleField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleField(WatParser.ModuleFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#module_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule_(WatParser.Module_Context ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#scriptModule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScriptModule(WatParser.ScriptModuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#action_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAction_(WatParser.Action_Context ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#assertion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssertion(WatParser.AssertionContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmd(WatParser.CmdContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#meta}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMeta(WatParser.MetaContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#wconst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWconst(WatParser.WconstContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#constList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstList(WatParser.ConstListContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#script}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScript(WatParser.ScriptContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#module}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule(WatParser.ModuleContext ctx);
}