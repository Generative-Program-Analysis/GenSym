package gensym.wasm;
// Generated from WatParser.g4 by ANTLR 4.12.0
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link WatParser}.
 */
public interface WatParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link WatParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(WatParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(WatParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(WatParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(WatParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#numType}.
	 * @param ctx the parse tree
	 */
	void enterNumType(WatParser.NumTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#numType}.
	 * @param ctx the parse tree
	 */
	void exitNumType(WatParser.NumTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#refType}.
	 * @param ctx the parse tree
	 */
	void enterRefType(WatParser.RefTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#refType}.
	 * @param ctx the parse tree
	 */
	void exitRefType(WatParser.RefTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#vecType}.
	 * @param ctx the parse tree
	 */
	void enterVecType(WatParser.VecTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#vecType}.
	 * @param ctx the parse tree
	 */
	void exitVecType(WatParser.VecTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#valType}.
	 * @param ctx the parse tree
	 */
	void enterValType(WatParser.ValTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#valType}.
	 * @param ctx the parse tree
	 */
	void exitValType(WatParser.ValTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#heapType}.
	 * @param ctx the parse tree
	 */
	void enterHeapType(WatParser.HeapTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#heapType}.
	 * @param ctx the parse tree
	 */
	void exitHeapType(WatParser.HeapTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#globalType}.
	 * @param ctx the parse tree
	 */
	void enterGlobalType(WatParser.GlobalTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#globalType}.
	 * @param ctx the parse tree
	 */
	void exitGlobalType(WatParser.GlobalTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#defType}.
	 * @param ctx the parse tree
	 */
	void enterDefType(WatParser.DefTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#defType}.
	 * @param ctx the parse tree
	 */
	void exitDefType(WatParser.DefTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#funcParamType}.
	 * @param ctx the parse tree
	 */
	void enterFuncParamType(WatParser.FuncParamTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#funcParamType}.
	 * @param ctx the parse tree
	 */
	void exitFuncParamType(WatParser.FuncParamTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#funcResType}.
	 * @param ctx the parse tree
	 */
	void enterFuncResType(WatParser.FuncResTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#funcResType}.
	 * @param ctx the parse tree
	 */
	void exitFuncResType(WatParser.FuncResTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#funcType}.
	 * @param ctx the parse tree
	 */
	void enterFuncType(WatParser.FuncTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#funcType}.
	 * @param ctx the parse tree
	 */
	void exitFuncType(WatParser.FuncTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#tableType}.
	 * @param ctx the parse tree
	 */
	void enterTableType(WatParser.TableTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#tableType}.
	 * @param ctx the parse tree
	 */
	void exitTableType(WatParser.TableTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#memoryType}.
	 * @param ctx the parse tree
	 */
	void enterMemoryType(WatParser.MemoryTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#memoryType}.
	 * @param ctx the parse tree
	 */
	void exitMemoryType(WatParser.MemoryTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#typeUse}.
	 * @param ctx the parse tree
	 */
	void enterTypeUse(WatParser.TypeUseContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#typeUse}.
	 * @param ctx the parse tree
	 */
	void exitTypeUse(WatParser.TypeUseContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(WatParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(WatParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#idx}.
	 * @param ctx the parse tree
	 */
	void enterIdx(WatParser.IdxContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#idx}.
	 * @param ctx the parse tree
	 */
	void exitIdx(WatParser.IdxContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#bindVar}.
	 * @param ctx the parse tree
	 */
	void enterBindVar(WatParser.BindVarContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#bindVar}.
	 * @param ctx the parse tree
	 */
	void exitBindVar(WatParser.BindVarContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#instr}.
	 * @param ctx the parse tree
	 */
	void enterInstr(WatParser.InstrContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#instr}.
	 * @param ctx the parse tree
	 */
	void exitInstr(WatParser.InstrContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#plainInstr}.
	 * @param ctx the parse tree
	 */
	void enterPlainInstr(WatParser.PlainInstrContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#plainInstr}.
	 * @param ctx the parse tree
	 */
	void exitPlainInstr(WatParser.PlainInstrContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#offsetEq}.
	 * @param ctx the parse tree
	 */
	void enterOffsetEq(WatParser.OffsetEqContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#offsetEq}.
	 * @param ctx the parse tree
	 */
	void exitOffsetEq(WatParser.OffsetEqContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#alignEq}.
	 * @param ctx the parse tree
	 */
	void enterAlignEq(WatParser.AlignEqContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#alignEq}.
	 * @param ctx the parse tree
	 */
	void exitAlignEq(WatParser.AlignEqContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#load}.
	 * @param ctx the parse tree
	 */
	void enterLoad(WatParser.LoadContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#load}.
	 * @param ctx the parse tree
	 */
	void exitLoad(WatParser.LoadContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#store}.
	 * @param ctx the parse tree
	 */
	void enterStore(WatParser.StoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#store}.
	 * @param ctx the parse tree
	 */
	void exitStore(WatParser.StoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#test}.
	 * @param ctx the parse tree
	 */
	void enterTest(WatParser.TestContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#test}.
	 * @param ctx the parse tree
	 */
	void exitTest(WatParser.TestContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#compare}.
	 * @param ctx the parse tree
	 */
	void enterCompare(WatParser.CompareContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#compare}.
	 * @param ctx the parse tree
	 */
	void exitCompare(WatParser.CompareContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#unary}.
	 * @param ctx the parse tree
	 */
	void enterUnary(WatParser.UnaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#unary}.
	 * @param ctx the parse tree
	 */
	void exitUnary(WatParser.UnaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#binary}.
	 * @param ctx the parse tree
	 */
	void enterBinary(WatParser.BinaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#binary}.
	 * @param ctx the parse tree
	 */
	void exitBinary(WatParser.BinaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#callIndirectInstr}.
	 * @param ctx the parse tree
	 */
	void enterCallIndirectInstr(WatParser.CallIndirectInstrContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#callIndirectInstr}.
	 * @param ctx the parse tree
	 */
	void exitCallIndirectInstr(WatParser.CallIndirectInstrContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#callInstrParams}.
	 * @param ctx the parse tree
	 */
	void enterCallInstrParams(WatParser.CallInstrParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#callInstrParams}.
	 * @param ctx the parse tree
	 */
	void exitCallInstrParams(WatParser.CallInstrParamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#callInstrInstr}.
	 * @param ctx the parse tree
	 */
	void enterCallInstrInstr(WatParser.CallInstrInstrContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#callInstrInstr}.
	 * @param ctx the parse tree
	 */
	void exitCallInstrInstr(WatParser.CallInstrInstrContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#callInstrParamsInstr}.
	 * @param ctx the parse tree
	 */
	void enterCallInstrParamsInstr(WatParser.CallInstrParamsInstrContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#callInstrParamsInstr}.
	 * @param ctx the parse tree
	 */
	void exitCallInstrParamsInstr(WatParser.CallInstrParamsInstrContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#callInstrResultsInstr}.
	 * @param ctx the parse tree
	 */
	void enterCallInstrResultsInstr(WatParser.CallInstrResultsInstrContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#callInstrResultsInstr}.
	 * @param ctx the parse tree
	 */
	void exitCallInstrResultsInstr(WatParser.CallInstrResultsInstrContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#blockInstr}.
	 * @param ctx the parse tree
	 */
	void enterBlockInstr(WatParser.BlockInstrContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#blockInstr}.
	 * @param ctx the parse tree
	 */
	void exitBlockInstr(WatParser.BlockInstrContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#blockType}.
	 * @param ctx the parse tree
	 */
	void enterBlockType(WatParser.BlockTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#blockType}.
	 * @param ctx the parse tree
	 */
	void exitBlockType(WatParser.BlockTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(WatParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(WatParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(WatParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(WatParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#expr1}.
	 * @param ctx the parse tree
	 */
	void enterExpr1(WatParser.Expr1Context ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#expr1}.
	 * @param ctx the parse tree
	 */
	void exitExpr1(WatParser.Expr1Context ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#callExprType}.
	 * @param ctx the parse tree
	 */
	void enterCallExprType(WatParser.CallExprTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#callExprType}.
	 * @param ctx the parse tree
	 */
	void exitCallExprType(WatParser.CallExprTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#callExprParams}.
	 * @param ctx the parse tree
	 */
	void enterCallExprParams(WatParser.CallExprParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#callExprParams}.
	 * @param ctx the parse tree
	 */
	void exitCallExprParams(WatParser.CallExprParamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#callExprResults}.
	 * @param ctx the parse tree
	 */
	void enterCallExprResults(WatParser.CallExprResultsContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#callExprResults}.
	 * @param ctx the parse tree
	 */
	void exitCallExprResults(WatParser.CallExprResultsContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#ifBlock}.
	 * @param ctx the parse tree
	 */
	void enterIfBlock(WatParser.IfBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#ifBlock}.
	 * @param ctx the parse tree
	 */
	void exitIfBlock(WatParser.IfBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#instrList}.
	 * @param ctx the parse tree
	 */
	void enterInstrList(WatParser.InstrListContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#instrList}.
	 * @param ctx the parse tree
	 */
	void exitInstrList(WatParser.InstrListContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#constExpr}.
	 * @param ctx the parse tree
	 */
	void enterConstExpr(WatParser.ConstExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#constExpr}.
	 * @param ctx the parse tree
	 */
	void exitConstExpr(WatParser.ConstExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(WatParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(WatParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#funcFields}.
	 * @param ctx the parse tree
	 */
	void enterFuncFields(WatParser.FuncFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#funcFields}.
	 * @param ctx the parse tree
	 */
	void exitFuncFields(WatParser.FuncFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#funcFieldsBody}.
	 * @param ctx the parse tree
	 */
	void enterFuncFieldsBody(WatParser.FuncFieldsBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#funcFieldsBody}.
	 * @param ctx the parse tree
	 */
	void exitFuncFieldsBody(WatParser.FuncFieldsBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#funcBody}.
	 * @param ctx the parse tree
	 */
	void enterFuncBody(WatParser.FuncBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#funcBody}.
	 * @param ctx the parse tree
	 */
	void exitFuncBody(WatParser.FuncBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#offset}.
	 * @param ctx the parse tree
	 */
	void enterOffset(WatParser.OffsetContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#offset}.
	 * @param ctx the parse tree
	 */
	void exitOffset(WatParser.OffsetContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#elem}.
	 * @param ctx the parse tree
	 */
	void enterElem(WatParser.ElemContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#elem}.
	 * @param ctx the parse tree
	 */
	void exitElem(WatParser.ElemContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#table}.
	 * @param ctx the parse tree
	 */
	void enterTable(WatParser.TableContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#table}.
	 * @param ctx the parse tree
	 */
	void exitTable(WatParser.TableContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#tableFields}.
	 * @param ctx the parse tree
	 */
	void enterTableFields(WatParser.TableFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#tableFields}.
	 * @param ctx the parse tree
	 */
	void exitTableFields(WatParser.TableFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#data}.
	 * @param ctx the parse tree
	 */
	void enterData(WatParser.DataContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#data}.
	 * @param ctx the parse tree
	 */
	void exitData(WatParser.DataContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#memory}.
	 * @param ctx the parse tree
	 */
	void enterMemory(WatParser.MemoryContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#memory}.
	 * @param ctx the parse tree
	 */
	void exitMemory(WatParser.MemoryContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#memoryFields}.
	 * @param ctx the parse tree
	 */
	void enterMemoryFields(WatParser.MemoryFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#memoryFields}.
	 * @param ctx the parse tree
	 */
	void exitMemoryFields(WatParser.MemoryFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#sglobal}.
	 * @param ctx the parse tree
	 */
	void enterSglobal(WatParser.SglobalContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#sglobal}.
	 * @param ctx the parse tree
	 */
	void exitSglobal(WatParser.SglobalContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#globalFields}.
	 * @param ctx the parse tree
	 */
	void enterGlobalFields(WatParser.GlobalFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#globalFields}.
	 * @param ctx the parse tree
	 */
	void exitGlobalFields(WatParser.GlobalFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#importDesc}.
	 * @param ctx the parse tree
	 */
	void enterImportDesc(WatParser.ImportDescContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#importDesc}.
	 * @param ctx the parse tree
	 */
	void exitImportDesc(WatParser.ImportDescContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#simport}.
	 * @param ctx the parse tree
	 */
	void enterSimport(WatParser.SimportContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#simport}.
	 * @param ctx the parse tree
	 */
	void exitSimport(WatParser.SimportContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#inlineImport}.
	 * @param ctx the parse tree
	 */
	void enterInlineImport(WatParser.InlineImportContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#inlineImport}.
	 * @param ctx the parse tree
	 */
	void exitInlineImport(WatParser.InlineImportContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#exportDesc}.
	 * @param ctx the parse tree
	 */
	void enterExportDesc(WatParser.ExportDescContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#exportDesc}.
	 * @param ctx the parse tree
	 */
	void exitExportDesc(WatParser.ExportDescContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#export_}.
	 * @param ctx the parse tree
	 */
	void enterExport_(WatParser.Export_Context ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#export_}.
	 * @param ctx the parse tree
	 */
	void exitExport_(WatParser.Export_Context ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#inlineExport}.
	 * @param ctx the parse tree
	 */
	void enterInlineExport(WatParser.InlineExportContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#inlineExport}.
	 * @param ctx the parse tree
	 */
	void exitInlineExport(WatParser.InlineExportContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#typeDef}.
	 * @param ctx the parse tree
	 */
	void enterTypeDef(WatParser.TypeDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#typeDef}.
	 * @param ctx the parse tree
	 */
	void exitTypeDef(WatParser.TypeDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#start_}.
	 * @param ctx the parse tree
	 */
	void enterStart_(WatParser.Start_Context ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#start_}.
	 * @param ctx the parse tree
	 */
	void exitStart_(WatParser.Start_Context ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#moduleField}.
	 * @param ctx the parse tree
	 */
	void enterModuleField(WatParser.ModuleFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#moduleField}.
	 * @param ctx the parse tree
	 */
	void exitModuleField(WatParser.ModuleFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#module_}.
	 * @param ctx the parse tree
	 */
	void enterModule_(WatParser.Module_Context ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#module_}.
	 * @param ctx the parse tree
	 */
	void exitModule_(WatParser.Module_Context ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#scriptModule}.
	 * @param ctx the parse tree
	 */
	void enterScriptModule(WatParser.ScriptModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#scriptModule}.
	 * @param ctx the parse tree
	 */
	void exitScriptModule(WatParser.ScriptModuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#action_}.
	 * @param ctx the parse tree
	 */
	void enterAction_(WatParser.Action_Context ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#action_}.
	 * @param ctx the parse tree
	 */
	void exitAction_(WatParser.Action_Context ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#assertion}.
	 * @param ctx the parse tree
	 */
	void enterAssertion(WatParser.AssertionContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#assertion}.
	 * @param ctx the parse tree
	 */
	void exitAssertion(WatParser.AssertionContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#cmd}.
	 * @param ctx the parse tree
	 */
	void enterCmd(WatParser.CmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#cmd}.
	 * @param ctx the parse tree
	 */
	void exitCmd(WatParser.CmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#meta}.
	 * @param ctx the parse tree
	 */
	void enterMeta(WatParser.MetaContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#meta}.
	 * @param ctx the parse tree
	 */
	void exitMeta(WatParser.MetaContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#wconst}.
	 * @param ctx the parse tree
	 */
	void enterWconst(WatParser.WconstContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#wconst}.
	 * @param ctx the parse tree
	 */
	void exitWconst(WatParser.WconstContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#constList}.
	 * @param ctx the parse tree
	 */
	void enterConstList(WatParser.ConstListContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#constList}.
	 * @param ctx the parse tree
	 */
	void exitConstList(WatParser.ConstListContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#script}.
	 * @param ctx the parse tree
	 */
	void enterScript(WatParser.ScriptContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#script}.
	 * @param ctx the parse tree
	 */
	void exitScript(WatParser.ScriptContext ctx);
	/**
	 * Enter a parse tree produced by {@link WatParser#module}.
	 * @param ctx the parse tree
	 */
	void enterModule(WatParser.ModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link WatParser#module}.
	 * @param ctx the parse tree
	 */
	void exitModule(WatParser.ModuleContext ctx);
}