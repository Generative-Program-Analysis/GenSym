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
	 * Visit a parse tree produced by {@link WatParser#value_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue_type(WatParser.Value_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#elem_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElem_type(WatParser.Elem_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#global_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobal_type(WatParser.Global_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#def_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDef_type(WatParser.Def_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#func_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_type(WatParser.Func_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#table_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_type(WatParser.Table_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#memory_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemory_type(WatParser.Memory_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#type_use}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_use(WatParser.Type_useContext ctx);
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
	 * Visit a parse tree produced by {@link WatParser#bind_var}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBind_var(WatParser.Bind_varContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#instr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstr(WatParser.InstrContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#plain_instr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlain_instr(WatParser.Plain_instrContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#call_instr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall_instr(WatParser.Call_instrContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#call_instr_params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall_instr_params(WatParser.Call_instr_paramsContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#call_instr_instr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall_instr_instr(WatParser.Call_instr_instrContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#call_instr_params_instr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall_instr_params_instr(WatParser.Call_instr_params_instrContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#call_instr_results_instr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall_instr_results_instr(WatParser.Call_instr_results_instrContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#block_instr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock_instr(WatParser.Block_instrContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#block_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock_type(WatParser.Block_typeContext ctx);
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
	 * Visit a parse tree produced by {@link WatParser#call_expr_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall_expr_type(WatParser.Call_expr_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#call_expr_params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall_expr_params(WatParser.Call_expr_paramsContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#call_expr_results}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall_expr_results(WatParser.Call_expr_resultsContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#if_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_block(WatParser.If_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#instr_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstr_list(WatParser.Instr_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#const_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConst_expr(WatParser.Const_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#func_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_(WatParser.Func_Context ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#func_fields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_fields(WatParser.Func_fieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#func_fields_import}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_fields_import(WatParser.Func_fields_importContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#func_fields_import_result}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_fields_import_result(WatParser.Func_fields_import_resultContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#func_fields_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_fields_body(WatParser.Func_fields_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#func_result_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_result_body(WatParser.Func_result_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#func_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_body(WatParser.Func_bodyContext ctx);
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
	 * Visit a parse tree produced by {@link WatParser#table_fields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_fields(WatParser.Table_fieldsContext ctx);
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
	 * Visit a parse tree produced by {@link WatParser#memory_fields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemory_fields(WatParser.Memory_fieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#sglobal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSglobal(WatParser.SglobalContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#global_fields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobal_fields(WatParser.Global_fieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#import_desc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImport_desc(WatParser.Import_descContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#simport}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimport(WatParser.SimportContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#inline_import}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInline_import(WatParser.Inline_importContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#export_desc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExport_desc(WatParser.Export_descContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#export_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExport_(WatParser.Export_Context ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#inline_export}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInline_export(WatParser.Inline_exportContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#type_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_(WatParser.Type_Context ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#type_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_def(WatParser.Type_defContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#start_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart_(WatParser.Start_Context ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#module_field}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule_field(WatParser.Module_fieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#module_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule_(WatParser.Module_Context ctx);
	/**
	 * Visit a parse tree produced by {@link WatParser#script_module}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScript_module(WatParser.Script_moduleContext ctx);
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
	 * Visit a parse tree produced by {@link WatParser#const_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConst_list(WatParser.Const_listContext ctx);
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