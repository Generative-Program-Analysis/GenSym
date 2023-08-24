package gensym.wasm;
// Generated from WatParser.g4 by ANTLR 4.12.0
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class WatParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.12.0", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		LPAR=1, RPAR=2, NAT=3, INT=4, FLOAT=5, STRING_=6, VALUE_TYPE=7, CONST=8, 
		FUNCREF=9, EXTERNREF=10, MUT=11, NOP=12, UNREACHABLE=13, DROP=14, BLOCK=15, 
		LOOP=16, END=17, BR=18, BR_IF=19, BR_TABLE=20, RETURN=21, IF=22, THEN=23, 
		ELSE=24, SELECT=25, CALL=26, CALL_INDIRECT=27, LOCAL_GET=28, LOCAL_SET=29, 
		LOCAL_TEE=30, GLOBAL_GET=31, GLOBAL_SET=32, LOAD=33, STORE=34, UNDERSCORE=35, 
		OFFSET_EQ=36, ALIGN_EQ=37, SIGN_POSTFIX=38, MEM_SIZE=39, I32=40, I64=41, 
		F32=42, F64=43, IXX=44, FXX=45, OP_EQZ=46, OP_EQ=47, OP_NE=48, OP_LT=49, 
		OP_LTS=50, OP_LTU=51, OP_LE=52, OP_LES=53, OP_LEU=54, OP_GT=55, OP_GTS=56, 
		OP_GTU=57, OP_GE=58, OP_GES=59, OP_GEU=60, OP_CLZ=61, OP_CTZ=62, OP_POPCNT=63, 
		OP_NEG=64, OP_ABS=65, OP_SQRT=66, OP_CEIL=67, OP_FLOOR=68, OP_TRUNC=69, 
		OP_NEAREST=70, OP_ADD=71, OP_SUB=72, OP_MUL=73, OP_DIV=74, OP_DIV_S=75, 
		OP_DIV_U=76, OP_REM_S=77, OP_REM_U=78, OP_AND=79, OP_OR=80, OP_XOR=81, 
		OP_SHL=82, OP_SHR_S=83, OP_SHR_U=84, OP_ROTL=85, OP_ROTR=86, OP_MIN=87, 
		OP_MAX=88, OP_COPYSIGN=89, OP_WRAP=90, OP_TRUNC_=91, OP_TRUNC_SAT=92, 
		OP_CONVERT=93, OP_EXTEND=94, OP_DEMOTE=95, OP_PROMOTE=96, OP_REINTER=97, 
		MEMORY_SIZE=98, MEMORY_GROW=99, MEMORY_FILL=100, MEMORY_COPY=101, MEMORY_INIT=102, 
		TEST=103, COMPARE=104, UNARY=105, BINARY=106, CONVERT=107, TYPE=108, FUNC=109, 
		EXTERN=110, START_=111, PARAM=112, RESULT=113, LOCAL=114, GLOBAL=115, 
		TABLE=116, MEMORY=117, ELEM=118, DATA=119, OFFSET=120, IMPORT=121, EXPORT=122, 
		MODULE=123, BIN=124, QUOTE=125, SCRIPT=126, REGISTER=127, INVOKE=128, 
		GET=129, ASSERT_MALFORMED=130, ASSERT_INVALID=131, ASSERT_UNLINKABLE=132, 
		ASSERT_RETURN=133, ASSERT_RETURN_CANONICAL_NAN=134, ASSERT_RETURN_ARITHMETIC_NAN=135, 
		ASSERT_TRAP=136, ASSERT_EXHAUSTION=137, INPUT=138, OUTPUT=139, VAR=140, 
		V128=141, SPACE=142, COMMENT=143;
	public static final int
		RULE_value = 0, RULE_name = 1, RULE_numType = 2, RULE_refType = 3, RULE_vecType = 4, 
		RULE_valType = 5, RULE_heapType = 6, RULE_globalType = 7, RULE_defType = 8, 
		RULE_funcParamType = 9, RULE_funcResType = 10, RULE_funcType = 11, RULE_tableType = 12, 
		RULE_memoryType = 13, RULE_typeUse = 14, RULE_literal = 15, RULE_idx = 16, 
		RULE_bindVar = 17, RULE_instr = 18, RULE_plainInstr = 19, RULE_offsetEq = 20, 
		RULE_alignEq = 21, RULE_load = 22, RULE_store = 23, RULE_callIndirectInstr = 24, 
		RULE_callInstrParams = 25, RULE_callInstrInstr = 26, RULE_callInstrParamsInstr = 27, 
		RULE_callInstrResultsInstr = 28, RULE_blockInstr = 29, RULE_blockType = 30, 
		RULE_block = 31, RULE_expr = 32, RULE_expr1 = 33, RULE_callExprType = 34, 
		RULE_callExprParams = 35, RULE_callExprResults = 36, RULE_ifBlock = 37, 
		RULE_instrList = 38, RULE_constExpr = 39, RULE_function = 40, RULE_funcFields = 41, 
		RULE_funcFieldsBody = 42, RULE_funcBody = 43, RULE_offset = 44, RULE_elem = 45, 
		RULE_table = 46, RULE_tableFields = 47, RULE_data = 48, RULE_memory = 49, 
		RULE_memoryFields = 50, RULE_sglobal = 51, RULE_globalFields = 52, RULE_importDesc = 53, 
		RULE_simport = 54, RULE_inlineImport = 55, RULE_exportDesc = 56, RULE_export_ = 57, 
		RULE_inlineExport = 58, RULE_typeDef = 59, RULE_start_ = 60, RULE_moduleField = 61, 
		RULE_module_ = 62, RULE_scriptModule = 63, RULE_action_ = 64, RULE_assertion = 65, 
		RULE_cmd = 66, RULE_meta = 67, RULE_wconst = 68, RULE_constList = 69, 
		RULE_script = 70, RULE_module = 71;
	private static String[] makeRuleNames() {
		return new String[] {
			"value", "name", "numType", "refType", "vecType", "valType", "heapType", 
			"globalType", "defType", "funcParamType", "funcResType", "funcType", 
			"tableType", "memoryType", "typeUse", "literal", "idx", "bindVar", "instr", 
			"plainInstr", "offsetEq", "alignEq", "load", "store", "callIndirectInstr", 
			"callInstrParams", "callInstrInstr", "callInstrParamsInstr", "callInstrResultsInstr", 
			"blockInstr", "blockType", "block", "expr", "expr1", "callExprType", 
			"callExprParams", "callExprResults", "ifBlock", "instrList", "constExpr", 
			"function", "funcFields", "funcFieldsBody", "funcBody", "offset", "elem", 
			"table", "tableFields", "data", "memory", "memoryFields", "sglobal", 
			"globalFields", "importDesc", "simport", "inlineImport", "exportDesc", 
			"export_", "inlineExport", "typeDef", "start_", "moduleField", "module_", 
			"scriptModule", "action_", "assertion", "cmd", "meta", "wconst", "constList", 
			"script", "module"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", null, null, null, null, null, null, "'funcref'", 
			"'externref'", "'mut'", "'nop'", "'unreachable'", "'drop'", "'block'", 
			"'loop'", "'end'", "'br'", "'br_if'", "'br_table'", "'return'", "'if'", 
			"'then'", "'else'", "'select'", "'call'", "'call_indirect'", "'local.get'", 
			"'local.set'", "'local.tee'", "'global.get'", "'global.set'", "'.load'", 
			"'.store'", "'_'", "'offset='", "'align='", null, null, "'i32'", "'i64'", 
			"'f32'", "'f64'", null, null, "'.eqz'", "'.eq'", "'.ne'", "'.lt'", "'.lt_s'", 
			"'.lt_u'", "'.le'", "'.le_s'", "'.le_u'", "'.gt'", "'.gt_s'", "'.gt_u'", 
			"'.ge'", "'.ge_s'", "'.ge_u'", "'.clz'", "'.ctz'", "'.popcnt'", "'.neg'", 
			"'.abs'", "'.sqrt'", "'.ceil'", "'.floor'", "'.trunc'", "'.nearest'", 
			"'.add'", "'.sub'", "'.mul'", "'.div'", "'.div_s'", "'.div_u'", "'.rem_s'", 
			"'.rem_u'", "'.and'", "'.or'", "'.xor'", "'.shl'", "'.shr_s'", "'.shr_u'", 
			"'.rotl'", "'.rotr'", "'.min'", "'.max'", "'.copysign'", "'.wrap_'", 
			"'.trunc_'", "'.trunc_sat_'", "'.convert_'", "'.extend_'", "'.demote_'", 
			"'.promote_'", "'.reinterpret_'", "'memory.size'", "'memory.grow'", "'memory.fill'", 
			"'memory.copy'", "'memory.init'", null, null, null, null, null, "'type'", 
			"'func'", "'extern'", "'start'", "'param'", "'result'", "'local'", "'global'", 
			"'table'", "'memory'", "'elem'", "'data'", "'offset'", "'import'", "'export'", 
			"'module'", "'binary'", "'quote'", "'script'", "'register'", "'invoke'", 
			"'get'", "'assert_malformed'", "'assert_invalid'", "'assert_unlinkable'", 
			"'assert_return'", "'assert_return_canonical_nan'", "'assert_return_arithmetic_nan'", 
			"'assert_trap'", "'assert_exhaustion'", "'input'", "'output'", null, 
			"'v128'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "LPAR", "RPAR", "NAT", "INT", "FLOAT", "STRING_", "VALUE_TYPE", 
			"CONST", "FUNCREF", "EXTERNREF", "MUT", "NOP", "UNREACHABLE", "DROP", 
			"BLOCK", "LOOP", "END", "BR", "BR_IF", "BR_TABLE", "RETURN", "IF", "THEN", 
			"ELSE", "SELECT", "CALL", "CALL_INDIRECT", "LOCAL_GET", "LOCAL_SET", 
			"LOCAL_TEE", "GLOBAL_GET", "GLOBAL_SET", "LOAD", "STORE", "UNDERSCORE", 
			"OFFSET_EQ", "ALIGN_EQ", "SIGN_POSTFIX", "MEM_SIZE", "I32", "I64", "F32", 
			"F64", "IXX", "FXX", "OP_EQZ", "OP_EQ", "OP_NE", "OP_LT", "OP_LTS", "OP_LTU", 
			"OP_LE", "OP_LES", "OP_LEU", "OP_GT", "OP_GTS", "OP_GTU", "OP_GE", "OP_GES", 
			"OP_GEU", "OP_CLZ", "OP_CTZ", "OP_POPCNT", "OP_NEG", "OP_ABS", "OP_SQRT", 
			"OP_CEIL", "OP_FLOOR", "OP_TRUNC", "OP_NEAREST", "OP_ADD", "OP_SUB", 
			"OP_MUL", "OP_DIV", "OP_DIV_S", "OP_DIV_U", "OP_REM_S", "OP_REM_U", "OP_AND", 
			"OP_OR", "OP_XOR", "OP_SHL", "OP_SHR_S", "OP_SHR_U", "OP_ROTL", "OP_ROTR", 
			"OP_MIN", "OP_MAX", "OP_COPYSIGN", "OP_WRAP", "OP_TRUNC_", "OP_TRUNC_SAT", 
			"OP_CONVERT", "OP_EXTEND", "OP_DEMOTE", "OP_PROMOTE", "OP_REINTER", "MEMORY_SIZE", 
			"MEMORY_GROW", "MEMORY_FILL", "MEMORY_COPY", "MEMORY_INIT", "TEST", "COMPARE", 
			"UNARY", "BINARY", "CONVERT", "TYPE", "FUNC", "EXTERN", "START_", "PARAM", 
			"RESULT", "LOCAL", "GLOBAL", "TABLE", "MEMORY", "ELEM", "DATA", "OFFSET", 
			"IMPORT", "EXPORT", "MODULE", "BIN", "QUOTE", "SCRIPT", "REGISTER", "INVOKE", 
			"GET", "ASSERT_MALFORMED", "ASSERT_INVALID", "ASSERT_UNLINKABLE", "ASSERT_RETURN", 
			"ASSERT_RETURN_CANONICAL_NAN", "ASSERT_RETURN_ARITHMETIC_NAN", "ASSERT_TRAP", 
			"ASSERT_EXHAUSTION", "INPUT", "OUTPUT", "VAR", "V128", "SPACE", "COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "WatParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public WatParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValueContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(WatParser.INT, 0); }
		public TerminalNode FLOAT() { return getToken(WatParser.FLOAT, 0); }
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_value);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			_la = _input.LA(1);
			if ( !(_la==INT || _la==FLOAT) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NameContext extends ParserRuleContext {
		public TerminalNode STRING_() { return getToken(WatParser.STRING_, 0); }
		public NameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NameContext name() throws RecognitionException {
		NameContext _localctx = new NameContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(146);
			match(STRING_);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NumTypeContext extends ParserRuleContext {
		public TerminalNode VALUE_TYPE() { return getToken(WatParser.VALUE_TYPE, 0); }
		public NumTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterNumType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitNumType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitNumType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumTypeContext numType() throws RecognitionException {
		NumTypeContext _localctx = new NumTypeContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_numType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			match(VALUE_TYPE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RefTypeContext extends ParserRuleContext {
		public TerminalNode FUNCREF() { return getToken(WatParser.FUNCREF, 0); }
		public TerminalNode EXTERNREF() { return getToken(WatParser.EXTERNREF, 0); }
		public RefTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_refType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterRefType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitRefType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitRefType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RefTypeContext refType() throws RecognitionException {
		RefTypeContext _localctx = new RefTypeContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_refType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(150);
			_la = _input.LA(1);
			if ( !(_la==FUNCREF || _la==EXTERNREF) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VecTypeContext extends ParserRuleContext {
		public TerminalNode V128() { return getToken(WatParser.V128, 0); }
		public VecTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vecType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterVecType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitVecType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitVecType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VecTypeContext vecType() throws RecognitionException {
		VecTypeContext _localctx = new VecTypeContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_vecType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(152);
			match(V128);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValTypeContext extends ParserRuleContext {
		public NumTypeContext numType() {
			return getRuleContext(NumTypeContext.class,0);
		}
		public VecTypeContext vecType() {
			return getRuleContext(VecTypeContext.class,0);
		}
		public RefTypeContext refType() {
			return getRuleContext(RefTypeContext.class,0);
		}
		public ValTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterValType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitValType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitValType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValTypeContext valType() throws RecognitionException {
		ValTypeContext _localctx = new ValTypeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_valType);
		try {
			setState(157);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VALUE_TYPE:
				enterOuterAlt(_localctx, 1);
				{
				setState(154);
				numType();
				}
				break;
			case V128:
				enterOuterAlt(_localctx, 2);
				{
				setState(155);
				vecType();
				}
				break;
			case FUNCREF:
			case EXTERNREF:
				enterOuterAlt(_localctx, 3);
				{
				setState(156);
				refType();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class HeapTypeContext extends ParserRuleContext {
		public TerminalNode FUNC() { return getToken(WatParser.FUNC, 0); }
		public TerminalNode EXTERN() { return getToken(WatParser.EXTERN, 0); }
		public HeapTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_heapType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterHeapType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitHeapType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitHeapType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HeapTypeContext heapType() throws RecognitionException {
		HeapTypeContext _localctx = new HeapTypeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_heapType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(159);
			_la = _input.LA(1);
			if ( !(_la==FUNC || _la==EXTERN) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GlobalTypeContext extends ParserRuleContext {
		public ValTypeContext valType() {
			return getRuleContext(ValTypeContext.class,0);
		}
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode MUT() { return getToken(WatParser.MUT, 0); }
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public GlobalTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_globalType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterGlobalType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitGlobalType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitGlobalType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GlobalTypeContext globalType() throws RecognitionException {
		GlobalTypeContext _localctx = new GlobalTypeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_globalType);
		try {
			setState(167);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VALUE_TYPE:
			case FUNCREF:
			case EXTERNREF:
			case V128:
				enterOuterAlt(_localctx, 1);
				{
				setState(161);
				valType();
				}
				break;
			case LPAR:
				enterOuterAlt(_localctx, 2);
				{
				setState(162);
				match(LPAR);
				setState(163);
				match(MUT);
				setState(164);
				valType();
				setState(165);
				match(RPAR);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DefTypeContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode FUNC() { return getToken(WatParser.FUNC, 0); }
		public FuncTypeContext funcType() {
			return getRuleContext(FuncTypeContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public DefTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterDefType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitDefType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitDefType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefTypeContext defType() throws RecognitionException {
		DefTypeContext _localctx = new DefTypeContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_defType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(169);
			match(LPAR);
			setState(170);
			match(FUNC);
			setState(171);
			funcType();
			setState(172);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FuncParamTypeContext extends ParserRuleContext {
		public List<TerminalNode> LPAR() { return getTokens(WatParser.LPAR); }
		public TerminalNode LPAR(int i) {
			return getToken(WatParser.LPAR, i);
		}
		public List<TerminalNode> PARAM() { return getTokens(WatParser.PARAM); }
		public TerminalNode PARAM(int i) {
			return getToken(WatParser.PARAM, i);
		}
		public List<TerminalNode> RPAR() { return getTokens(WatParser.RPAR); }
		public TerminalNode RPAR(int i) {
			return getToken(WatParser.RPAR, i);
		}
		public List<BindVarContext> bindVar() {
			return getRuleContexts(BindVarContext.class);
		}
		public BindVarContext bindVar(int i) {
			return getRuleContext(BindVarContext.class,i);
		}
		public List<ValTypeContext> valType() {
			return getRuleContexts(ValTypeContext.class);
		}
		public ValTypeContext valType(int i) {
			return getRuleContext(ValTypeContext.class,i);
		}
		public FuncParamTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcParamType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterFuncParamType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitFuncParamType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitFuncParamType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncParamTypeContext funcParamType() throws RecognitionException {
		FuncParamTypeContext _localctx = new FuncParamTypeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_funcParamType);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(190);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(174);
					match(LPAR);
					setState(175);
					match(PARAM);
					setState(185);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case RPAR:
					case VALUE_TYPE:
					case FUNCREF:
					case EXTERNREF:
					case V128:
						{
						setState(179);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1664L) != 0) || _la==V128) {
							{
							{
							setState(176);
							valType();
							}
							}
							setState(181);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						}
						break;
					case VAR:
						{
						setState(182);
						bindVar();
						setState(183);
						valType();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(187);
					match(RPAR);
					}
					} 
				}
				setState(192);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FuncResTypeContext extends ParserRuleContext {
		public List<TerminalNode> LPAR() { return getTokens(WatParser.LPAR); }
		public TerminalNode LPAR(int i) {
			return getToken(WatParser.LPAR, i);
		}
		public List<TerminalNode> RESULT() { return getTokens(WatParser.RESULT); }
		public TerminalNode RESULT(int i) {
			return getToken(WatParser.RESULT, i);
		}
		public List<TerminalNode> RPAR() { return getTokens(WatParser.RPAR); }
		public TerminalNode RPAR(int i) {
			return getToken(WatParser.RPAR, i);
		}
		public List<ValTypeContext> valType() {
			return getRuleContexts(ValTypeContext.class);
		}
		public ValTypeContext valType(int i) {
			return getRuleContext(ValTypeContext.class,i);
		}
		public FuncResTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcResType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterFuncResType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitFuncResType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitFuncResType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncResTypeContext funcResType() throws RecognitionException {
		FuncResTypeContext _localctx = new FuncResTypeContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_funcResType);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(204);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(193);
					match(LPAR);
					setState(194);
					match(RESULT);
					setState(198);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1664L) != 0) || _la==V128) {
						{
						{
						setState(195);
						valType();
						}
						}
						setState(200);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(201);
					match(RPAR);
					}
					} 
				}
				setState(206);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FuncTypeContext extends ParserRuleContext {
		public FuncParamTypeContext funcParamType() {
			return getRuleContext(FuncParamTypeContext.class,0);
		}
		public FuncResTypeContext funcResType() {
			return getRuleContext(FuncResTypeContext.class,0);
		}
		public FuncTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterFuncType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitFuncType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitFuncType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncTypeContext funcType() throws RecognitionException {
		FuncTypeContext _localctx = new FuncTypeContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_funcType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			funcParamType();
			setState(208);
			funcResType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TableTypeContext extends ParserRuleContext {
		public List<TerminalNode> NAT() { return getTokens(WatParser.NAT); }
		public TerminalNode NAT(int i) {
			return getToken(WatParser.NAT, i);
		}
		public RefTypeContext refType() {
			return getRuleContext(RefTypeContext.class,0);
		}
		public TableTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterTableType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitTableType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitTableType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableTypeContext tableType() throws RecognitionException {
		TableTypeContext _localctx = new TableTypeContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_tableType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(210);
			match(NAT);
			setState(212);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAT) {
				{
				setState(211);
				match(NAT);
				}
			}

			setState(214);
			refType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MemoryTypeContext extends ParserRuleContext {
		public List<TerminalNode> NAT() { return getTokens(WatParser.NAT); }
		public TerminalNode NAT(int i) {
			return getToken(WatParser.NAT, i);
		}
		public MemoryTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memoryType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterMemoryType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitMemoryType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitMemoryType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemoryTypeContext memoryType() throws RecognitionException {
		MemoryTypeContext _localctx = new MemoryTypeContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_memoryType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(216);
			match(NAT);
			setState(218);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAT) {
				{
				setState(217);
				match(NAT);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeUseContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode TYPE() { return getToken(WatParser.TYPE, 0); }
		public IdxContext idx() {
			return getRuleContext(IdxContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public TypeUseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeUse; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterTypeUse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitTypeUse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitTypeUse(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeUseContext typeUse() throws RecognitionException {
		TypeUseContext _localctx = new TypeUseContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_typeUse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(220);
			match(LPAR);
			setState(221);
			match(TYPE);
			setState(222);
			idx();
			setState(223);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode NAT() { return getToken(WatParser.NAT, 0); }
		public TerminalNode INT() { return getToken(WatParser.INT, 0); }
		public TerminalNode FLOAT() { return getToken(WatParser.FLOAT, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(225);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 56L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IdxContext extends ParserRuleContext {
		public TerminalNode NAT() { return getToken(WatParser.NAT, 0); }
		public TerminalNode VAR() { return getToken(WatParser.VAR, 0); }
		public IdxContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idx; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterIdx(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitIdx(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitIdx(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdxContext idx() throws RecognitionException {
		IdxContext _localctx = new IdxContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_idx);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(227);
			_la = _input.LA(1);
			if ( !(_la==NAT || _la==VAR) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BindVarContext extends ParserRuleContext {
		public TerminalNode VAR() { return getToken(WatParser.VAR, 0); }
		public BindVarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bindVar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterBindVar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitBindVar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitBindVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BindVarContext bindVar() throws RecognitionException {
		BindVarContext _localctx = new BindVarContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_bindVar);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(229);
			match(VAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InstrContext extends ParserRuleContext {
		public PlainInstrContext plainInstr() {
			return getRuleContext(PlainInstrContext.class,0);
		}
		public CallInstrInstrContext callInstrInstr() {
			return getRuleContext(CallInstrInstrContext.class,0);
		}
		public BlockInstrContext blockInstr() {
			return getRuleContext(BlockInstrContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public InstrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterInstr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitInstr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitInstr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstrContext instr() throws RecognitionException {
		InstrContext _localctx = new InstrContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_instr);
		try {
			setState(235);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VALUE_TYPE:
			case CONST:
			case NOP:
			case UNREACHABLE:
			case DROP:
			case BR:
			case BR_IF:
			case BR_TABLE:
			case RETURN:
			case SELECT:
			case CALL:
			case LOCAL_GET:
			case LOCAL_SET:
			case LOCAL_TEE:
			case GLOBAL_GET:
			case GLOBAL_SET:
			case MEMORY_SIZE:
			case MEMORY_GROW:
			case MEMORY_FILL:
			case MEMORY_COPY:
			case MEMORY_INIT:
			case TEST:
			case COMPARE:
			case UNARY:
			case BINARY:
			case CONVERT:
				enterOuterAlt(_localctx, 1);
				{
				setState(231);
				plainInstr();
				}
				break;
			case CALL_INDIRECT:
				enterOuterAlt(_localctx, 2);
				{
				setState(232);
				callInstrInstr();
				}
				break;
			case BLOCK:
			case LOOP:
			case IF:
				enterOuterAlt(_localctx, 3);
				{
				setState(233);
				blockInstr();
				}
				break;
			case LPAR:
				enterOuterAlt(_localctx, 4);
				{
				setState(234);
				expr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PlainInstrContext extends ParserRuleContext {
		public TerminalNode UNREACHABLE() { return getToken(WatParser.UNREACHABLE, 0); }
		public TerminalNode NOP() { return getToken(WatParser.NOP, 0); }
		public TerminalNode DROP() { return getToken(WatParser.DROP, 0); }
		public TerminalNode SELECT() { return getToken(WatParser.SELECT, 0); }
		public TerminalNode BR() { return getToken(WatParser.BR, 0); }
		public List<IdxContext> idx() {
			return getRuleContexts(IdxContext.class);
		}
		public IdxContext idx(int i) {
			return getRuleContext(IdxContext.class,i);
		}
		public TerminalNode BR_IF() { return getToken(WatParser.BR_IF, 0); }
		public TerminalNode BR_TABLE() { return getToken(WatParser.BR_TABLE, 0); }
		public TerminalNode RETURN() { return getToken(WatParser.RETURN, 0); }
		public TerminalNode CALL() { return getToken(WatParser.CALL, 0); }
		public TerminalNode LOCAL_GET() { return getToken(WatParser.LOCAL_GET, 0); }
		public TerminalNode LOCAL_SET() { return getToken(WatParser.LOCAL_SET, 0); }
		public TerminalNode LOCAL_TEE() { return getToken(WatParser.LOCAL_TEE, 0); }
		public TerminalNode GLOBAL_GET() { return getToken(WatParser.GLOBAL_GET, 0); }
		public TerminalNode GLOBAL_SET() { return getToken(WatParser.GLOBAL_SET, 0); }
		public LoadContext load() {
			return getRuleContext(LoadContext.class,0);
		}
		public OffsetEqContext offsetEq() {
			return getRuleContext(OffsetEqContext.class,0);
		}
		public AlignEqContext alignEq() {
			return getRuleContext(AlignEqContext.class,0);
		}
		public StoreContext store() {
			return getRuleContext(StoreContext.class,0);
		}
		public TerminalNode MEMORY_SIZE() { return getToken(WatParser.MEMORY_SIZE, 0); }
		public TerminalNode MEMORY_GROW() { return getToken(WatParser.MEMORY_GROW, 0); }
		public TerminalNode MEMORY_FILL() { return getToken(WatParser.MEMORY_FILL, 0); }
		public TerminalNode MEMORY_COPY() { return getToken(WatParser.MEMORY_COPY, 0); }
		public TerminalNode MEMORY_INIT() { return getToken(WatParser.MEMORY_INIT, 0); }
		public TerminalNode CONST() { return getToken(WatParser.CONST, 0); }
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public TerminalNode TEST() { return getToken(WatParser.TEST, 0); }
		public TerminalNode COMPARE() { return getToken(WatParser.COMPARE, 0); }
		public TerminalNode UNARY() { return getToken(WatParser.UNARY, 0); }
		public TerminalNode BINARY() { return getToken(WatParser.BINARY, 0); }
		public TerminalNode CONVERT() { return getToken(WatParser.CONVERT, 0); }
		public PlainInstrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_plainInstr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterPlainInstr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitPlainInstr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitPlainInstr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PlainInstrContext plainInstr() throws RecognitionException {
		PlainInstrContext _localctx = new PlainInstrContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_plainInstr);
		int _la;
		try {
			setState(291);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(237);
				match(UNREACHABLE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(238);
				match(NOP);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(239);
				match(DROP);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(240);
				match(SELECT);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(241);
				match(BR);
				setState(242);
				idx();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(243);
				match(BR_IF);
				setState(244);
				idx();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(245);
				match(BR_TABLE);
				setState(247); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(246);
					idx();
					}
					}
					setState(249); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==NAT || _la==VAR );
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(251);
				match(RETURN);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(252);
				match(CALL);
				setState(253);
				idx();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(254);
				match(LOCAL_GET);
				setState(255);
				idx();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(256);
				match(LOCAL_SET);
				setState(257);
				idx();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(258);
				match(LOCAL_TEE);
				setState(259);
				idx();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(260);
				match(GLOBAL_GET);
				setState(261);
				idx();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(262);
				match(GLOBAL_SET);
				setState(263);
				idx();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(264);
				load();
				setState(266);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==OFFSET_EQ) {
					{
					setState(265);
					offsetEq();
					}
				}

				setState(269);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ALIGN_EQ) {
					{
					setState(268);
					alignEq();
					}
				}

				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(271);
				store();
				setState(273);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==OFFSET_EQ) {
					{
					setState(272);
					offsetEq();
					}
				}

				setState(276);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ALIGN_EQ) {
					{
					setState(275);
					alignEq();
					}
				}

				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(278);
				match(MEMORY_SIZE);
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(279);
				match(MEMORY_GROW);
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(280);
				match(MEMORY_FILL);
				}
				break;
			case 20:
				enterOuterAlt(_localctx, 20);
				{
				setState(281);
				match(MEMORY_COPY);
				}
				break;
			case 21:
				enterOuterAlt(_localctx, 21);
				{
				setState(282);
				match(MEMORY_INIT);
				setState(283);
				idx();
				}
				break;
			case 22:
				enterOuterAlt(_localctx, 22);
				{
				setState(284);
				match(CONST);
				setState(285);
				literal();
				}
				break;
			case 23:
				enterOuterAlt(_localctx, 23);
				{
				setState(286);
				match(TEST);
				}
				break;
			case 24:
				enterOuterAlt(_localctx, 24);
				{
				setState(287);
				match(COMPARE);
				}
				break;
			case 25:
				enterOuterAlt(_localctx, 25);
				{
				setState(288);
				match(UNARY);
				}
				break;
			case 26:
				enterOuterAlt(_localctx, 26);
				{
				setState(289);
				match(BINARY);
				}
				break;
			case 27:
				enterOuterAlt(_localctx, 27);
				{
				setState(290);
				match(CONVERT);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OffsetEqContext extends ParserRuleContext {
		public TerminalNode OFFSET_EQ() { return getToken(WatParser.OFFSET_EQ, 0); }
		public TerminalNode NAT() { return getToken(WatParser.NAT, 0); }
		public OffsetEqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_offsetEq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterOffsetEq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitOffsetEq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitOffsetEq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OffsetEqContext offsetEq() throws RecognitionException {
		OffsetEqContext _localctx = new OffsetEqContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_offsetEq);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(293);
			match(OFFSET_EQ);
			setState(294);
			match(NAT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AlignEqContext extends ParserRuleContext {
		public TerminalNode ALIGN_EQ() { return getToken(WatParser.ALIGN_EQ, 0); }
		public TerminalNode NAT() { return getToken(WatParser.NAT, 0); }
		public AlignEqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alignEq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterAlignEq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitAlignEq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitAlignEq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AlignEqContext alignEq() throws RecognitionException {
		AlignEqContext _localctx = new AlignEqContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_alignEq);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(296);
			match(ALIGN_EQ);
			setState(297);
			match(NAT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LoadContext extends ParserRuleContext {
		public NumTypeContext numType() {
			return getRuleContext(NumTypeContext.class,0);
		}
		public TerminalNode LOAD() { return getToken(WatParser.LOAD, 0); }
		public TerminalNode MEM_SIZE() { return getToken(WatParser.MEM_SIZE, 0); }
		public TerminalNode UNDERSCORE() { return getToken(WatParser.UNDERSCORE, 0); }
		public TerminalNode SIGN_POSTFIX() { return getToken(WatParser.SIGN_POSTFIX, 0); }
		public LoadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_load; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterLoad(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitLoad(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitLoad(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LoadContext load() throws RecognitionException {
		LoadContext _localctx = new LoadContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_load);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(299);
			numType();
			setState(300);
			match(LOAD);
			setState(304);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MEM_SIZE) {
				{
				setState(301);
				match(MEM_SIZE);
				setState(302);
				match(UNDERSCORE);
				setState(303);
				match(SIGN_POSTFIX);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StoreContext extends ParserRuleContext {
		public NumTypeContext numType() {
			return getRuleContext(NumTypeContext.class,0);
		}
		public TerminalNode STORE() { return getToken(WatParser.STORE, 0); }
		public TerminalNode MEM_SIZE() { return getToken(WatParser.MEM_SIZE, 0); }
		public StoreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_store; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterStore(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitStore(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitStore(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StoreContext store() throws RecognitionException {
		StoreContext _localctx = new StoreContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_store);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(306);
			numType();
			setState(307);
			match(STORE);
			setState(309);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MEM_SIZE) {
				{
				setState(308);
				match(MEM_SIZE);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CallIndirectInstrContext extends ParserRuleContext {
		public TerminalNode CALL_INDIRECT() { return getToken(WatParser.CALL_INDIRECT, 0); }
		public CallInstrParamsContext callInstrParams() {
			return getRuleContext(CallInstrParamsContext.class,0);
		}
		public TypeUseContext typeUse() {
			return getRuleContext(TypeUseContext.class,0);
		}
		public CallIndirectInstrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_callIndirectInstr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterCallIndirectInstr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitCallIndirectInstr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitCallIndirectInstr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CallIndirectInstrContext callIndirectInstr() throws RecognitionException {
		CallIndirectInstrContext _localctx = new CallIndirectInstrContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_callIndirectInstr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(311);
			match(CALL_INDIRECT);
			setState(313);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				{
				setState(312);
				typeUse();
				}
				break;
			}
			setState(315);
			callInstrParams();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CallInstrParamsContext extends ParserRuleContext {
		public List<TerminalNode> LPAR() { return getTokens(WatParser.LPAR); }
		public TerminalNode LPAR(int i) {
			return getToken(WatParser.LPAR, i);
		}
		public List<TerminalNode> PARAM() { return getTokens(WatParser.PARAM); }
		public TerminalNode PARAM(int i) {
			return getToken(WatParser.PARAM, i);
		}
		public List<TerminalNode> RPAR() { return getTokens(WatParser.RPAR); }
		public TerminalNode RPAR(int i) {
			return getToken(WatParser.RPAR, i);
		}
		public List<TerminalNode> RESULT() { return getTokens(WatParser.RESULT); }
		public TerminalNode RESULT(int i) {
			return getToken(WatParser.RESULT, i);
		}
		public List<ValTypeContext> valType() {
			return getRuleContexts(ValTypeContext.class);
		}
		public ValTypeContext valType(int i) {
			return getRuleContext(ValTypeContext.class,i);
		}
		public CallInstrParamsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_callInstrParams; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterCallInstrParams(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitCallInstrParams(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitCallInstrParams(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CallInstrParamsContext callInstrParams() throws RecognitionException {
		CallInstrParamsContext _localctx = new CallInstrParamsContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_callInstrParams);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(328);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(317);
					match(LPAR);
					setState(318);
					match(PARAM);
					setState(322);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1664L) != 0) || _la==V128) {
						{
						{
						setState(319);
						valType();
						}
						}
						setState(324);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(325);
					match(RPAR);
					}
					} 
				}
				setState(330);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			}
			setState(342);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAR) {
				{
				{
				setState(331);
				match(LPAR);
				setState(332);
				match(RESULT);
				setState(336);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1664L) != 0) || _la==V128) {
					{
					{
					setState(333);
					valType();
					}
					}
					setState(338);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(339);
				match(RPAR);
				}
				}
				setState(344);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CallInstrInstrContext extends ParserRuleContext {
		public TerminalNode CALL_INDIRECT() { return getToken(WatParser.CALL_INDIRECT, 0); }
		public CallInstrParamsInstrContext callInstrParamsInstr() {
			return getRuleContext(CallInstrParamsInstrContext.class,0);
		}
		public TypeUseContext typeUse() {
			return getRuleContext(TypeUseContext.class,0);
		}
		public CallInstrInstrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_callInstrInstr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterCallInstrInstr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitCallInstrInstr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitCallInstrInstr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CallInstrInstrContext callInstrInstr() throws RecognitionException {
		CallInstrInstrContext _localctx = new CallInstrInstrContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_callInstrInstr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(345);
			match(CALL_INDIRECT);
			setState(347);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				{
				setState(346);
				typeUse();
				}
				break;
			}
			setState(349);
			callInstrParamsInstr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CallInstrParamsInstrContext extends ParserRuleContext {
		public CallInstrResultsInstrContext callInstrResultsInstr() {
			return getRuleContext(CallInstrResultsInstrContext.class,0);
		}
		public List<TerminalNode> LPAR() { return getTokens(WatParser.LPAR); }
		public TerminalNode LPAR(int i) {
			return getToken(WatParser.LPAR, i);
		}
		public List<TerminalNode> PARAM() { return getTokens(WatParser.PARAM); }
		public TerminalNode PARAM(int i) {
			return getToken(WatParser.PARAM, i);
		}
		public List<TerminalNode> RPAR() { return getTokens(WatParser.RPAR); }
		public TerminalNode RPAR(int i) {
			return getToken(WatParser.RPAR, i);
		}
		public List<ValTypeContext> valType() {
			return getRuleContexts(ValTypeContext.class);
		}
		public ValTypeContext valType(int i) {
			return getRuleContext(ValTypeContext.class,i);
		}
		public CallInstrParamsInstrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_callInstrParamsInstr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterCallInstrParamsInstr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitCallInstrParamsInstr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitCallInstrParamsInstr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CallInstrParamsInstrContext callInstrParamsInstr() throws RecognitionException {
		CallInstrParamsInstrContext _localctx = new CallInstrParamsInstrContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_callInstrParamsInstr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(362);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(351);
					match(LPAR);
					setState(352);
					match(PARAM);
					setState(356);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1664L) != 0) || _la==V128) {
						{
						{
						setState(353);
						valType();
						}
						}
						setState(358);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(359);
					match(RPAR);
					}
					} 
				}
				setState(364);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			}
			setState(365);
			callInstrResultsInstr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CallInstrResultsInstrContext extends ParserRuleContext {
		public InstrContext instr() {
			return getRuleContext(InstrContext.class,0);
		}
		public List<TerminalNode> LPAR() { return getTokens(WatParser.LPAR); }
		public TerminalNode LPAR(int i) {
			return getToken(WatParser.LPAR, i);
		}
		public List<TerminalNode> RESULT() { return getTokens(WatParser.RESULT); }
		public TerminalNode RESULT(int i) {
			return getToken(WatParser.RESULT, i);
		}
		public List<TerminalNode> RPAR() { return getTokens(WatParser.RPAR); }
		public TerminalNode RPAR(int i) {
			return getToken(WatParser.RPAR, i);
		}
		public List<ValTypeContext> valType() {
			return getRuleContexts(ValTypeContext.class);
		}
		public ValTypeContext valType(int i) {
			return getRuleContext(ValTypeContext.class,i);
		}
		public CallInstrResultsInstrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_callInstrResultsInstr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterCallInstrResultsInstr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitCallInstrResultsInstr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitCallInstrResultsInstr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CallInstrResultsInstrContext callInstrResultsInstr() throws RecognitionException {
		CallInstrResultsInstrContext _localctx = new CallInstrResultsInstrContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_callInstrResultsInstr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(378);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(367);
					match(LPAR);
					setState(368);
					match(RESULT);
					setState(372);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1664L) != 0) || _la==V128) {
						{
						{
						setState(369);
						valType();
						}
						}
						setState(374);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(375);
					match(RPAR);
					}
					} 
				}
				setState(380);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			}
			setState(381);
			instr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockInstrContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode END() { return getToken(WatParser.END, 0); }
		public TerminalNode BLOCK() { return getToken(WatParser.BLOCK, 0); }
		public TerminalNode LOOP() { return getToken(WatParser.LOOP, 0); }
		public List<BindVarContext> bindVar() {
			return getRuleContexts(BindVarContext.class);
		}
		public BindVarContext bindVar(int i) {
			return getRuleContext(BindVarContext.class,i);
		}
		public TerminalNode IF() { return getToken(WatParser.IF, 0); }
		public TerminalNode ELSE() { return getToken(WatParser.ELSE, 0); }
		public InstrListContext instrList() {
			return getRuleContext(InstrListContext.class,0);
		}
		public BlockInstrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockInstr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterBlockInstr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitBlockInstr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitBlockInstr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockInstrContext blockInstr() throws RecognitionException {
		BlockInstrContext _localctx = new BlockInstrContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_blockInstr);
		int _la;
		try {
			setState(408);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BLOCK:
			case LOOP:
				enterOuterAlt(_localctx, 1);
				{
				setState(383);
				_la = _input.LA(1);
				if ( !(_la==BLOCK || _la==LOOP) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(385);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(384);
					bindVar();
					}
				}

				setState(387);
				block();
				setState(388);
				match(END);
				setState(390);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(389);
					bindVar();
					}
				}

				}
				break;
			case IF:
				enterOuterAlt(_localctx, 2);
				{
				setState(392);
				match(IF);
				setState(394);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(393);
					bindVar();
					}
				}

				setState(396);
				block();
				setState(402);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(397);
					match(ELSE);
					setState(399);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==VAR) {
						{
						setState(398);
						bindVar();
						}
					}

					setState(401);
					instrList();
					}
				}

				setState(404);
				match(END);
				setState(406);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(405);
					bindVar();
					}
				}

				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockTypeContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode RESULT() { return getToken(WatParser.RESULT, 0); }
		public ValTypeContext valType() {
			return getRuleContext(ValTypeContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public BlockTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterBlockType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitBlockType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitBlockType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockTypeContext blockType() throws RecognitionException {
		BlockTypeContext _localctx = new BlockTypeContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_blockType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(410);
			match(LPAR);
			setState(411);
			match(RESULT);
			setState(412);
			valType();
			setState(413);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockContext extends ParserRuleContext {
		public InstrListContext instrList() {
			return getRuleContext(InstrListContext.class,0);
		}
		public BlockTypeContext blockType() {
			return getRuleContext(BlockTypeContext.class,0);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_block);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(416);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				{
				setState(415);
				blockType();
				}
				break;
			}
			setState(418);
			instrList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public Expr1Context expr1() {
			return getRuleContext(Expr1Context.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_expr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(420);
			match(LPAR);
			setState(421);
			expr1();
			setState(422);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Expr1Context extends ParserRuleContext {
		public PlainInstrContext plainInstr() {
			return getRuleContext(PlainInstrContext.class,0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode CALL_INDIRECT() { return getToken(WatParser.CALL_INDIRECT, 0); }
		public CallExprTypeContext callExprType() {
			return getRuleContext(CallExprTypeContext.class,0);
		}
		public TerminalNode BLOCK() { return getToken(WatParser.BLOCK, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public BindVarContext bindVar() {
			return getRuleContext(BindVarContext.class,0);
		}
		public TerminalNode LOOP() { return getToken(WatParser.LOOP, 0); }
		public TerminalNode IF() { return getToken(WatParser.IF, 0); }
		public IfBlockContext ifBlock() {
			return getRuleContext(IfBlockContext.class,0);
		}
		public Expr1Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr1; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterExpr1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitExpr1(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitExpr1(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Expr1Context expr1() throws RecognitionException {
		Expr1Context _localctx = new Expr1Context(_ctx, getState());
		enterRule(_localctx, 66, RULE_expr1);
		int _la;
		try {
			setState(448);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VALUE_TYPE:
			case CONST:
			case NOP:
			case UNREACHABLE:
			case DROP:
			case BR:
			case BR_IF:
			case BR_TABLE:
			case RETURN:
			case SELECT:
			case CALL:
			case LOCAL_GET:
			case LOCAL_SET:
			case LOCAL_TEE:
			case GLOBAL_GET:
			case GLOBAL_SET:
			case MEMORY_SIZE:
			case MEMORY_GROW:
			case MEMORY_FILL:
			case MEMORY_COPY:
			case MEMORY_INIT:
			case TEST:
			case COMPARE:
			case UNARY:
			case BINARY:
			case CONVERT:
				enterOuterAlt(_localctx, 1);
				{
				setState(424);
				plainInstr();
				setState(428);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LPAR) {
					{
					{
					setState(425);
					expr();
					}
					}
					setState(430);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case CALL_INDIRECT:
				enterOuterAlt(_localctx, 2);
				{
				setState(431);
				match(CALL_INDIRECT);
				setState(432);
				callExprType();
				}
				break;
			case BLOCK:
				enterOuterAlt(_localctx, 3);
				{
				setState(433);
				match(BLOCK);
				setState(435);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(434);
					bindVar();
					}
				}

				setState(437);
				block();
				}
				break;
			case LOOP:
				enterOuterAlt(_localctx, 4);
				{
				setState(438);
				match(LOOP);
				setState(440);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(439);
					bindVar();
					}
				}

				setState(442);
				block();
				}
				break;
			case IF:
				enterOuterAlt(_localctx, 5);
				{
				setState(443);
				match(IF);
				setState(445);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(444);
					bindVar();
					}
				}

				setState(447);
				ifBlock();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CallExprTypeContext extends ParserRuleContext {
		public CallExprParamsContext callExprParams() {
			return getRuleContext(CallExprParamsContext.class,0);
		}
		public TypeUseContext typeUse() {
			return getRuleContext(TypeUseContext.class,0);
		}
		public CallExprTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_callExprType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterCallExprType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitCallExprType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitCallExprType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CallExprTypeContext callExprType() throws RecognitionException {
		CallExprTypeContext _localctx = new CallExprTypeContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_callExprType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(451);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
			case 1:
				{
				setState(450);
				typeUse();
				}
				break;
			}
			setState(453);
			callExprParams();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CallExprParamsContext extends ParserRuleContext {
		public CallExprResultsContext callExprResults() {
			return getRuleContext(CallExprResultsContext.class,0);
		}
		public List<TerminalNode> LPAR() { return getTokens(WatParser.LPAR); }
		public TerminalNode LPAR(int i) {
			return getToken(WatParser.LPAR, i);
		}
		public List<TerminalNode> PARAM() { return getTokens(WatParser.PARAM); }
		public TerminalNode PARAM(int i) {
			return getToken(WatParser.PARAM, i);
		}
		public List<TerminalNode> RPAR() { return getTokens(WatParser.RPAR); }
		public TerminalNode RPAR(int i) {
			return getToken(WatParser.RPAR, i);
		}
		public List<ValTypeContext> valType() {
			return getRuleContexts(ValTypeContext.class);
		}
		public ValTypeContext valType(int i) {
			return getRuleContext(ValTypeContext.class,i);
		}
		public CallExprParamsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_callExprParams; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterCallExprParams(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitCallExprParams(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitCallExprParams(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CallExprParamsContext callExprParams() throws RecognitionException {
		CallExprParamsContext _localctx = new CallExprParamsContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_callExprParams);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(466);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,43,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(455);
					match(LPAR);
					setState(456);
					match(PARAM);
					setState(460);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1664L) != 0) || _la==V128) {
						{
						{
						setState(457);
						valType();
						}
						}
						setState(462);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(463);
					match(RPAR);
					}
					} 
				}
				setState(468);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,43,_ctx);
			}
			setState(469);
			callExprResults();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CallExprResultsContext extends ParserRuleContext {
		public List<TerminalNode> LPAR() { return getTokens(WatParser.LPAR); }
		public TerminalNode LPAR(int i) {
			return getToken(WatParser.LPAR, i);
		}
		public List<TerminalNode> RESULT() { return getTokens(WatParser.RESULT); }
		public TerminalNode RESULT(int i) {
			return getToken(WatParser.RESULT, i);
		}
		public List<TerminalNode> RPAR() { return getTokens(WatParser.RPAR); }
		public TerminalNode RPAR(int i) {
			return getToken(WatParser.RPAR, i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<ValTypeContext> valType() {
			return getRuleContexts(ValTypeContext.class);
		}
		public ValTypeContext valType(int i) {
			return getRuleContext(ValTypeContext.class,i);
		}
		public CallExprResultsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_callExprResults; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterCallExprResults(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitCallExprResults(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitCallExprResults(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CallExprResultsContext callExprResults() throws RecognitionException {
		CallExprResultsContext _localctx = new CallExprResultsContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_callExprResults);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(482);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,45,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(471);
					match(LPAR);
					setState(472);
					match(RESULT);
					setState(476);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1664L) != 0) || _la==V128) {
						{
						{
						setState(473);
						valType();
						}
						}
						setState(478);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(479);
					match(RPAR);
					}
					} 
				}
				setState(484);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,45,_ctx);
			}
			setState(488);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAR) {
				{
				{
				setState(485);
				expr();
				}
				}
				setState(490);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IfBlockContext extends ParserRuleContext {
		public BlockTypeContext blockType() {
			return getRuleContext(BlockTypeContext.class,0);
		}
		public IfBlockContext ifBlock() {
			return getRuleContext(IfBlockContext.class,0);
		}
		public List<TerminalNode> LPAR() { return getTokens(WatParser.LPAR); }
		public TerminalNode LPAR(int i) {
			return getToken(WatParser.LPAR, i);
		}
		public TerminalNode THEN() { return getToken(WatParser.THEN, 0); }
		public List<InstrListContext> instrList() {
			return getRuleContexts(InstrListContext.class);
		}
		public InstrListContext instrList(int i) {
			return getRuleContext(InstrListContext.class,i);
		}
		public List<TerminalNode> RPAR() { return getTokens(WatParser.RPAR); }
		public TerminalNode RPAR(int i) {
			return getToken(WatParser.RPAR, i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(WatParser.ELSE, 0); }
		public IfBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterIfBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitIfBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitIfBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfBlockContext ifBlock() throws RecognitionException {
		IfBlockContext _localctx = new IfBlockContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_ifBlock);
		int _la;
		try {
			int _alt;
			setState(511);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(491);
				blockType();
				setState(492);
				ifBlock();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(497);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(494);
						expr();
						}
						} 
					}
					setState(499);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
				}
				setState(500);
				match(LPAR);
				setState(501);
				match(THEN);
				setState(502);
				instrList();
				setState(503);
				match(RPAR);
				setState(509);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LPAR) {
					{
					setState(504);
					match(LPAR);
					setState(505);
					match(ELSE);
					setState(506);
					instrList();
					setState(507);
					match(RPAR);
					}
				}

				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InstrListContext extends ParserRuleContext {
		public List<InstrContext> instr() {
			return getRuleContexts(InstrContext.class);
		}
		public InstrContext instr(int i) {
			return getRuleContext(InstrContext.class,i);
		}
		public CallIndirectInstrContext callIndirectInstr() {
			return getRuleContext(CallIndirectInstrContext.class,0);
		}
		public InstrListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instrList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterInstrList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitInstrList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitInstrList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstrListContext instrList() throws RecognitionException {
		InstrListContext _localctx = new InstrListContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_instrList);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(516);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(513);
					instr();
					}
					} 
				}
				setState(518);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
			}
			setState(520);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CALL_INDIRECT) {
				{
				setState(519);
				callIndirectInstr();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstExprContext extends ParserRuleContext {
		public InstrListContext instrList() {
			return getRuleContext(InstrListContext.class,0);
		}
		public ConstExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterConstExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitConstExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitConstExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstExprContext constExpr() throws RecognitionException {
		ConstExprContext _localctx = new ConstExprContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_constExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(522);
			instrList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode FUNC() { return getToken(WatParser.FUNC, 0); }
		public FuncFieldsContext funcFields() {
			return getRuleContext(FuncFieldsContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public BindVarContext bindVar() {
			return getRuleContext(BindVarContext.class,0);
		}
		public FunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitFunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionContext function() throws RecognitionException {
		FunctionContext _localctx = new FunctionContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_function);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(524);
			match(LPAR);
			setState(525);
			match(FUNC);
			setState(527);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(526);
				bindVar();
				}
			}

			setState(529);
			funcFields();
			setState(530);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FuncFieldsContext extends ParserRuleContext {
		public FuncFieldsBodyContext funcFieldsBody() {
			return getRuleContext(FuncFieldsBodyContext.class,0);
		}
		public TypeUseContext typeUse() {
			return getRuleContext(TypeUseContext.class,0);
		}
		public InlineImportContext inlineImport() {
			return getRuleContext(InlineImportContext.class,0);
		}
		public FuncTypeContext funcType() {
			return getRuleContext(FuncTypeContext.class,0);
		}
		public InlineExportContext inlineExport() {
			return getRuleContext(InlineExportContext.class,0);
		}
		public FuncFieldsContext funcFields() {
			return getRuleContext(FuncFieldsContext.class,0);
		}
		public FuncFieldsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcFields; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterFuncFields(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitFuncFields(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitFuncFields(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncFieldsContext funcFields() throws RecognitionException {
		FuncFieldsContext _localctx = new FuncFieldsContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_funcFields);
		try {
			setState(545);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(533);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
				case 1:
					{
					setState(532);
					typeUse();
					}
					break;
				}
				setState(535);
				funcFieldsBody();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(536);
				inlineImport();
				setState(538);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
				case 1:
					{
					setState(537);
					typeUse();
					}
					break;
				}
				setState(540);
				funcType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(542);
				inlineExport();
				setState(543);
				funcFields();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FuncFieldsBodyContext extends ParserRuleContext {
		public FuncTypeContext funcType() {
			return getRuleContext(FuncTypeContext.class,0);
		}
		public FuncBodyContext funcBody() {
			return getRuleContext(FuncBodyContext.class,0);
		}
		public FuncFieldsBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcFieldsBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterFuncFieldsBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitFuncFieldsBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitFuncFieldsBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncFieldsBodyContext funcFieldsBody() throws RecognitionException {
		FuncFieldsBodyContext _localctx = new FuncFieldsBodyContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_funcFieldsBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(547);
			funcType();
			setState(548);
			funcBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FuncBodyContext extends ParserRuleContext {
		public InstrListContext instrList() {
			return getRuleContext(InstrListContext.class,0);
		}
		public List<TerminalNode> LPAR() { return getTokens(WatParser.LPAR); }
		public TerminalNode LPAR(int i) {
			return getToken(WatParser.LPAR, i);
		}
		public List<TerminalNode> LOCAL() { return getTokens(WatParser.LOCAL); }
		public TerminalNode LOCAL(int i) {
			return getToken(WatParser.LOCAL, i);
		}
		public List<TerminalNode> RPAR() { return getTokens(WatParser.RPAR); }
		public TerminalNode RPAR(int i) {
			return getToken(WatParser.RPAR, i);
		}
		public List<BindVarContext> bindVar() {
			return getRuleContexts(BindVarContext.class);
		}
		public BindVarContext bindVar(int i) {
			return getRuleContext(BindVarContext.class,i);
		}
		public List<ValTypeContext> valType() {
			return getRuleContexts(ValTypeContext.class);
		}
		public ValTypeContext valType(int i) {
			return getRuleContext(ValTypeContext.class,i);
		}
		public FuncBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterFuncBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitFuncBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitFuncBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncBodyContext funcBody() throws RecognitionException {
		FuncBodyContext _localctx = new FuncBodyContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_funcBody);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(566);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,58,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(550);
					match(LPAR);
					setState(551);
					match(LOCAL);
					setState(561);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case RPAR:
					case VALUE_TYPE:
					case FUNCREF:
					case EXTERNREF:
					case V128:
						{
						setState(555);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1664L) != 0) || _la==V128) {
							{
							{
							setState(552);
							valType();
							}
							}
							setState(557);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						}
						break;
					case VAR:
						{
						setState(558);
						bindVar();
						setState(559);
						valType();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(563);
					match(RPAR);
					}
					} 
				}
				setState(568);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,58,_ctx);
			}
			setState(569);
			instrList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OffsetContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode OFFSET() { return getToken(WatParser.OFFSET, 0); }
		public ConstExprContext constExpr() {
			return getRuleContext(ConstExprContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public OffsetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_offset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterOffset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitOffset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitOffset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OffsetContext offset() throws RecognitionException {
		OffsetContext _localctx = new OffsetContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_offset);
		try {
			setState(577);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(571);
				match(LPAR);
				setState(572);
				match(OFFSET);
				setState(573);
				constExpr();
				setState(574);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(576);
				expr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElemContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode ELEM() { return getToken(WatParser.ELEM, 0); }
		public OffsetContext offset() {
			return getRuleContext(OffsetContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public List<IdxContext> idx() {
			return getRuleContexts(IdxContext.class);
		}
		public IdxContext idx(int i) {
			return getRuleContext(IdxContext.class,i);
		}
		public ElemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterElem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitElem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitElem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElemContext elem() throws RecognitionException {
		ElemContext _localctx = new ElemContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_elem);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(579);
			match(LPAR);
			setState(580);
			match(ELEM);
			setState(582);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAT || _la==VAR) {
				{
				setState(581);
				idx();
				}
			}

			setState(584);
			offset();
			setState(588);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NAT || _la==VAR) {
				{
				{
				setState(585);
				idx();
				}
				}
				setState(590);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(591);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TableContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode TABLE() { return getToken(WatParser.TABLE, 0); }
		public TableFieldsContext tableFields() {
			return getRuleContext(TableFieldsContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public BindVarContext bindVar() {
			return getRuleContext(BindVarContext.class,0);
		}
		public TableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_table; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterTable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitTable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitTable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableContext table() throws RecognitionException {
		TableContext _localctx = new TableContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_table);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(593);
			match(LPAR);
			setState(594);
			match(TABLE);
			setState(596);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(595);
				bindVar();
				}
			}

			setState(598);
			tableFields();
			setState(599);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TableFieldsContext extends ParserRuleContext {
		public TableTypeContext tableType() {
			return getRuleContext(TableTypeContext.class,0);
		}
		public InlineImportContext inlineImport() {
			return getRuleContext(InlineImportContext.class,0);
		}
		public InlineExportContext inlineExport() {
			return getRuleContext(InlineExportContext.class,0);
		}
		public TableFieldsContext tableFields() {
			return getRuleContext(TableFieldsContext.class,0);
		}
		public RefTypeContext refType() {
			return getRuleContext(RefTypeContext.class,0);
		}
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode ELEM() { return getToken(WatParser.ELEM, 0); }
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public List<IdxContext> idx() {
			return getRuleContexts(IdxContext.class);
		}
		public IdxContext idx(int i) {
			return getRuleContext(IdxContext.class,i);
		}
		public TableFieldsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableFields; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterTableFields(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitTableFields(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitTableFields(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableFieldsContext tableFields() throws RecognitionException {
		TableFieldsContext _localctx = new TableFieldsContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_tableFields);
		int _la;
		try {
			setState(619);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(601);
				tableType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(602);
				inlineImport();
				setState(603);
				tableType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(605);
				inlineExport();
				setState(606);
				tableFields();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(608);
				refType();
				setState(609);
				match(LPAR);
				setState(610);
				match(ELEM);
				setState(614);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NAT || _la==VAR) {
					{
					{
					setState(611);
					idx();
					}
					}
					setState(616);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(617);
				match(RPAR);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DataContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode DATA() { return getToken(WatParser.DATA, 0); }
		public OffsetContext offset() {
			return getRuleContext(OffsetContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public IdxContext idx() {
			return getRuleContext(IdxContext.class,0);
		}
		public List<TerminalNode> STRING_() { return getTokens(WatParser.STRING_); }
		public TerminalNode STRING_(int i) {
			return getToken(WatParser.STRING_, i);
		}
		public DataContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_data; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterData(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitData(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitData(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataContext data() throws RecognitionException {
		DataContext _localctx = new DataContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_data);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(621);
			match(LPAR);
			setState(622);
			match(DATA);
			setState(624);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAT || _la==VAR) {
				{
				setState(623);
				idx();
				}
			}

			setState(626);
			offset();
			setState(630);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==STRING_) {
				{
				{
				setState(627);
				match(STRING_);
				}
				}
				setState(632);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(633);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MemoryContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode MEMORY() { return getToken(WatParser.MEMORY, 0); }
		public MemoryFieldsContext memoryFields() {
			return getRuleContext(MemoryFieldsContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public BindVarContext bindVar() {
			return getRuleContext(BindVarContext.class,0);
		}
		public MemoryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memory; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterMemory(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitMemory(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitMemory(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemoryContext memory() throws RecognitionException {
		MemoryContext _localctx = new MemoryContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_memory);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(635);
			match(LPAR);
			setState(636);
			match(MEMORY);
			setState(638);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(637);
				bindVar();
				}
			}

			setState(640);
			memoryFields();
			setState(641);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MemoryFieldsContext extends ParserRuleContext {
		public MemoryTypeContext memoryType() {
			return getRuleContext(MemoryTypeContext.class,0);
		}
		public InlineImportContext inlineImport() {
			return getRuleContext(InlineImportContext.class,0);
		}
		public InlineExportContext inlineExport() {
			return getRuleContext(InlineExportContext.class,0);
		}
		public MemoryFieldsContext memoryFields() {
			return getRuleContext(MemoryFieldsContext.class,0);
		}
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode DATA() { return getToken(WatParser.DATA, 0); }
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public List<TerminalNode> STRING_() { return getTokens(WatParser.STRING_); }
		public TerminalNode STRING_(int i) {
			return getToken(WatParser.STRING_, i);
		}
		public MemoryFieldsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memoryFields; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterMemoryFields(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitMemoryFields(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitMemoryFields(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemoryFieldsContext memoryFields() throws RecognitionException {
		MemoryFieldsContext _localctx = new MemoryFieldsContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_memoryFields);
		int _la;
		try {
			setState(659);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(643);
				memoryType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(644);
				inlineImport();
				setState(645);
				memoryType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(647);
				inlineExport();
				setState(648);
				memoryFields();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(650);
				match(LPAR);
				setState(651);
				match(DATA);
				setState(655);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STRING_) {
					{
					{
					setState(652);
					match(STRING_);
					}
					}
					setState(657);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(658);
				match(RPAR);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SglobalContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode GLOBAL() { return getToken(WatParser.GLOBAL, 0); }
		public GlobalFieldsContext globalFields() {
			return getRuleContext(GlobalFieldsContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public BindVarContext bindVar() {
			return getRuleContext(BindVarContext.class,0);
		}
		public SglobalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sglobal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterSglobal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitSglobal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitSglobal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SglobalContext sglobal() throws RecognitionException {
		SglobalContext _localctx = new SglobalContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_sglobal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(661);
			match(LPAR);
			setState(662);
			match(GLOBAL);
			setState(664);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(663);
				bindVar();
				}
			}

			setState(666);
			globalFields();
			setState(667);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GlobalFieldsContext extends ParserRuleContext {
		public GlobalTypeContext globalType() {
			return getRuleContext(GlobalTypeContext.class,0);
		}
		public ConstExprContext constExpr() {
			return getRuleContext(ConstExprContext.class,0);
		}
		public InlineImportContext inlineImport() {
			return getRuleContext(InlineImportContext.class,0);
		}
		public InlineExportContext inlineExport() {
			return getRuleContext(InlineExportContext.class,0);
		}
		public GlobalFieldsContext globalFields() {
			return getRuleContext(GlobalFieldsContext.class,0);
		}
		public GlobalFieldsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_globalFields; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterGlobalFields(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitGlobalFields(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitGlobalFields(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GlobalFieldsContext globalFields() throws RecognitionException {
		GlobalFieldsContext _localctx = new GlobalFieldsContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_globalFields);
		try {
			setState(678);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,71,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(669);
				globalType();
				setState(670);
				constExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(672);
				inlineImport();
				setState(673);
				globalType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(675);
				inlineExport();
				setState(676);
				globalFields();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ImportDescContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode FUNC() { return getToken(WatParser.FUNC, 0); }
		public TypeUseContext typeUse() {
			return getRuleContext(TypeUseContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public BindVarContext bindVar() {
			return getRuleContext(BindVarContext.class,0);
		}
		public FuncTypeContext funcType() {
			return getRuleContext(FuncTypeContext.class,0);
		}
		public TerminalNode TABLE() { return getToken(WatParser.TABLE, 0); }
		public TableTypeContext tableType() {
			return getRuleContext(TableTypeContext.class,0);
		}
		public TerminalNode MEMORY() { return getToken(WatParser.MEMORY, 0); }
		public MemoryTypeContext memoryType() {
			return getRuleContext(MemoryTypeContext.class,0);
		}
		public TerminalNode GLOBAL() { return getToken(WatParser.GLOBAL, 0); }
		public GlobalTypeContext globalType() {
			return getRuleContext(GlobalTypeContext.class,0);
		}
		public ImportDescContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importDesc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterImportDesc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitImportDesc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitImportDesc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportDescContext importDesc() throws RecognitionException {
		ImportDescContext _localctx = new ImportDescContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_importDesc);
		int _la;
		try {
			setState(720);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,77,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(680);
				match(LPAR);
				setState(681);
				match(FUNC);
				setState(683);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(682);
					bindVar();
					}
				}

				setState(685);
				typeUse();
				setState(686);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(688);
				match(LPAR);
				setState(689);
				match(FUNC);
				setState(691);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(690);
					bindVar();
					}
				}

				setState(693);
				funcType();
				setState(694);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(696);
				match(LPAR);
				setState(697);
				match(TABLE);
				setState(699);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(698);
					bindVar();
					}
				}

				setState(701);
				tableType();
				setState(702);
				match(RPAR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(704);
				match(LPAR);
				setState(705);
				match(MEMORY);
				setState(707);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(706);
					bindVar();
					}
				}

				setState(709);
				memoryType();
				setState(710);
				match(RPAR);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(712);
				match(LPAR);
				setState(713);
				match(GLOBAL);
				setState(715);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(714);
					bindVar();
					}
				}

				setState(717);
				globalType();
				setState(718);
				match(RPAR);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SimportContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode IMPORT() { return getToken(WatParser.IMPORT, 0); }
		public List<NameContext> name() {
			return getRuleContexts(NameContext.class);
		}
		public NameContext name(int i) {
			return getRuleContext(NameContext.class,i);
		}
		public ImportDescContext importDesc() {
			return getRuleContext(ImportDescContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public SimportContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simport; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterSimport(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitSimport(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitSimport(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimportContext simport() throws RecognitionException {
		SimportContext _localctx = new SimportContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_simport);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(722);
			match(LPAR);
			setState(723);
			match(IMPORT);
			setState(724);
			name();
			setState(725);
			name();
			setState(726);
			importDesc();
			setState(727);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InlineImportContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode IMPORT() { return getToken(WatParser.IMPORT, 0); }
		public List<NameContext> name() {
			return getRuleContexts(NameContext.class);
		}
		public NameContext name(int i) {
			return getRuleContext(NameContext.class,i);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public InlineImportContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inlineImport; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterInlineImport(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitInlineImport(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitInlineImport(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InlineImportContext inlineImport() throws RecognitionException {
		InlineImportContext _localctx = new InlineImportContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_inlineImport);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(729);
			match(LPAR);
			setState(730);
			match(IMPORT);
			setState(731);
			name();
			setState(732);
			name();
			setState(733);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExportDescContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode FUNC() { return getToken(WatParser.FUNC, 0); }
		public IdxContext idx() {
			return getRuleContext(IdxContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public TerminalNode TABLE() { return getToken(WatParser.TABLE, 0); }
		public TerminalNode MEMORY() { return getToken(WatParser.MEMORY, 0); }
		public TerminalNode GLOBAL() { return getToken(WatParser.GLOBAL, 0); }
		public ExportDescContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exportDesc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterExportDesc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitExportDesc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitExportDesc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExportDescContext exportDesc() throws RecognitionException {
		ExportDescContext _localctx = new ExportDescContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_exportDesc);
		try {
			setState(755);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(735);
				match(LPAR);
				setState(736);
				match(FUNC);
				setState(737);
				idx();
				setState(738);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(740);
				match(LPAR);
				setState(741);
				match(TABLE);
				setState(742);
				idx();
				setState(743);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(745);
				match(LPAR);
				setState(746);
				match(MEMORY);
				setState(747);
				idx();
				setState(748);
				match(RPAR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(750);
				match(LPAR);
				setState(751);
				match(GLOBAL);
				setState(752);
				idx();
				setState(753);
				match(RPAR);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Export_Context extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode EXPORT() { return getToken(WatParser.EXPORT, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public ExportDescContext exportDesc() {
			return getRuleContext(ExportDescContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public Export_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_export_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterExport_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitExport_(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitExport_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Export_Context export_() throws RecognitionException {
		Export_Context _localctx = new Export_Context(_ctx, getState());
		enterRule(_localctx, 114, RULE_export_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(757);
			match(LPAR);
			setState(758);
			match(EXPORT);
			setState(759);
			name();
			setState(760);
			exportDesc();
			setState(761);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InlineExportContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode EXPORT() { return getToken(WatParser.EXPORT, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public InlineExportContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inlineExport; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterInlineExport(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitInlineExport(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitInlineExport(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InlineExportContext inlineExport() throws RecognitionException {
		InlineExportContext _localctx = new InlineExportContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_inlineExport);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(763);
			match(LPAR);
			setState(764);
			match(EXPORT);
			setState(765);
			name();
			setState(766);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeDefContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode TYPE() { return getToken(WatParser.TYPE, 0); }
		public DefTypeContext defType() {
			return getRuleContext(DefTypeContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public BindVarContext bindVar() {
			return getRuleContext(BindVarContext.class,0);
		}
		public TypeDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterTypeDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitTypeDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitTypeDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeDefContext typeDef() throws RecognitionException {
		TypeDefContext _localctx = new TypeDefContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_typeDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(768);
			match(LPAR);
			setState(769);
			match(TYPE);
			setState(771);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(770);
				bindVar();
				}
			}

			setState(773);
			defType();
			setState(774);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Start_Context extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode START_() { return getToken(WatParser.START_, 0); }
		public IdxContext idx() {
			return getRuleContext(IdxContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public Start_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_start_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterStart_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitStart_(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitStart_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Start_Context start_() throws RecognitionException {
		Start_Context _localctx = new Start_Context(_ctx, getState());
		enterRule(_localctx, 120, RULE_start_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(776);
			match(LPAR);
			setState(777);
			match(START_);
			setState(778);
			idx();
			setState(779);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ModuleFieldContext extends ParserRuleContext {
		public TypeDefContext typeDef() {
			return getRuleContext(TypeDefContext.class,0);
		}
		public SglobalContext sglobal() {
			return getRuleContext(SglobalContext.class,0);
		}
		public TableContext table() {
			return getRuleContext(TableContext.class,0);
		}
		public MemoryContext memory() {
			return getRuleContext(MemoryContext.class,0);
		}
		public FunctionContext function() {
			return getRuleContext(FunctionContext.class,0);
		}
		public ElemContext elem() {
			return getRuleContext(ElemContext.class,0);
		}
		public DataContext data() {
			return getRuleContext(DataContext.class,0);
		}
		public Start_Context start_() {
			return getRuleContext(Start_Context.class,0);
		}
		public SimportContext simport() {
			return getRuleContext(SimportContext.class,0);
		}
		public Export_Context export_() {
			return getRuleContext(Export_Context.class,0);
		}
		public ModuleFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_moduleField; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterModuleField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitModuleField(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitModuleField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleFieldContext moduleField() throws RecognitionException {
		ModuleFieldContext _localctx = new ModuleFieldContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_moduleField);
		try {
			setState(791);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,80,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(781);
				typeDef();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(782);
				sglobal();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(783);
				table();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(784);
				memory();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(785);
				function();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(786);
				elem();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(787);
				data();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(788);
				start_();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(789);
				simport();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(790);
				export_();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Module_Context extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode MODULE() { return getToken(WatParser.MODULE, 0); }
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public TerminalNode VAR() { return getToken(WatParser.VAR, 0); }
		public List<ModuleFieldContext> moduleField() {
			return getRuleContexts(ModuleFieldContext.class);
		}
		public ModuleFieldContext moduleField(int i) {
			return getRuleContext(ModuleFieldContext.class,i);
		}
		public Module_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterModule_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitModule_(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitModule_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Module_Context module_() throws RecognitionException {
		Module_Context _localctx = new Module_Context(_ctx, getState());
		enterRule(_localctx, 124, RULE_module_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(793);
			match(LPAR);
			setState(794);
			match(MODULE);
			setState(796);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(795);
				match(VAR);
				}
			}

			setState(801);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAR) {
				{
				{
				setState(798);
				moduleField();
				}
				}
				setState(803);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(804);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ScriptModuleContext extends ParserRuleContext {
		public Module_Context module_() {
			return getRuleContext(Module_Context.class,0);
		}
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode MODULE() { return getToken(WatParser.MODULE, 0); }
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public TerminalNode BIN() { return getToken(WatParser.BIN, 0); }
		public TerminalNode QUOTE() { return getToken(WatParser.QUOTE, 0); }
		public TerminalNode VAR() { return getToken(WatParser.VAR, 0); }
		public List<TerminalNode> STRING_() { return getTokens(WatParser.STRING_); }
		public TerminalNode STRING_(int i) {
			return getToken(WatParser.STRING_, i);
		}
		public ScriptModuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scriptModule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterScriptModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitScriptModule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitScriptModule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ScriptModuleContext scriptModule() throws RecognitionException {
		ScriptModuleContext _localctx = new ScriptModuleContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_scriptModule);
		int _la;
		try {
			setState(820);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(806);
				module_();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(807);
				match(LPAR);
				setState(808);
				match(MODULE);
				setState(810);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(809);
					match(VAR);
					}
				}

				setState(812);
				_la = _input.LA(1);
				if ( !(_la==BIN || _la==QUOTE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(816);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STRING_) {
					{
					{
					setState(813);
					match(STRING_);
					}
					}
					setState(818);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(819);
				match(RPAR);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Action_Context extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode INVOKE() { return getToken(WatParser.INVOKE, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public ConstListContext constList() {
			return getRuleContext(ConstListContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public TerminalNode VAR() { return getToken(WatParser.VAR, 0); }
		public TerminalNode GET() { return getToken(WatParser.GET, 0); }
		public Action_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_action_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterAction_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitAction_(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitAction_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Action_Context action_() throws RecognitionException {
		Action_Context _localctx = new Action_Context(_ctx, getState());
		enterRule(_localctx, 128, RULE_action_);
		int _la;
		try {
			setState(839);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(822);
				match(LPAR);
				setState(823);
				match(INVOKE);
				setState(825);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(824);
					match(VAR);
					}
				}

				setState(827);
				name();
				setState(828);
				constList();
				setState(829);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(831);
				match(LPAR);
				setState(832);
				match(GET);
				setState(834);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(833);
					match(VAR);
					}
				}

				setState(836);
				name();
				setState(837);
				match(RPAR);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssertionContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode ASSERT_MALFORMED() { return getToken(WatParser.ASSERT_MALFORMED, 0); }
		public ScriptModuleContext scriptModule() {
			return getRuleContext(ScriptModuleContext.class,0);
		}
		public TerminalNode STRING_() { return getToken(WatParser.STRING_, 0); }
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public TerminalNode ASSERT_INVALID() { return getToken(WatParser.ASSERT_INVALID, 0); }
		public TerminalNode ASSERT_UNLINKABLE() { return getToken(WatParser.ASSERT_UNLINKABLE, 0); }
		public TerminalNode ASSERT_TRAP() { return getToken(WatParser.ASSERT_TRAP, 0); }
		public TerminalNode ASSERT_RETURN() { return getToken(WatParser.ASSERT_RETURN, 0); }
		public Action_Context action_() {
			return getRuleContext(Action_Context.class,0);
		}
		public ConstListContext constList() {
			return getRuleContext(ConstListContext.class,0);
		}
		public TerminalNode ASSERT_RETURN_CANONICAL_NAN() { return getToken(WatParser.ASSERT_RETURN_CANONICAL_NAN, 0); }
		public TerminalNode ASSERT_RETURN_ARITHMETIC_NAN() { return getToken(WatParser.ASSERT_RETURN_ARITHMETIC_NAN, 0); }
		public TerminalNode ASSERT_EXHAUSTION() { return getToken(WatParser.ASSERT_EXHAUSTION, 0); }
		public AssertionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assertion; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterAssertion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitAssertion(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitAssertion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssertionContext assertion() throws RecognitionException {
		AssertionContext _localctx = new AssertionContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_assertion);
		try {
			setState(893);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(841);
				match(LPAR);
				setState(842);
				match(ASSERT_MALFORMED);
				setState(843);
				scriptModule();
				setState(844);
				match(STRING_);
				setState(845);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(847);
				match(LPAR);
				setState(848);
				match(ASSERT_INVALID);
				setState(849);
				scriptModule();
				setState(850);
				match(STRING_);
				setState(851);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(853);
				match(LPAR);
				setState(854);
				match(ASSERT_UNLINKABLE);
				setState(855);
				scriptModule();
				setState(856);
				match(STRING_);
				setState(857);
				match(RPAR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(859);
				match(LPAR);
				setState(860);
				match(ASSERT_TRAP);
				setState(861);
				scriptModule();
				setState(862);
				match(STRING_);
				setState(863);
				match(RPAR);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(865);
				match(LPAR);
				setState(866);
				match(ASSERT_RETURN);
				setState(867);
				action_();
				setState(868);
				constList();
				setState(869);
				match(RPAR);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(871);
				match(LPAR);
				setState(872);
				match(ASSERT_RETURN_CANONICAL_NAN);
				setState(873);
				action_();
				setState(874);
				match(RPAR);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(876);
				match(LPAR);
				setState(877);
				match(ASSERT_RETURN_ARITHMETIC_NAN);
				setState(878);
				action_();
				setState(879);
				match(RPAR);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(881);
				match(LPAR);
				setState(882);
				match(ASSERT_TRAP);
				setState(883);
				action_();
				setState(884);
				match(STRING_);
				setState(885);
				match(RPAR);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(887);
				match(LPAR);
				setState(888);
				match(ASSERT_EXHAUSTION);
				setState(889);
				action_();
				setState(890);
				match(STRING_);
				setState(891);
				match(RPAR);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CmdContext extends ParserRuleContext {
		public Action_Context action_() {
			return getRuleContext(Action_Context.class,0);
		}
		public AssertionContext assertion() {
			return getRuleContext(AssertionContext.class,0);
		}
		public ScriptModuleContext scriptModule() {
			return getRuleContext(ScriptModuleContext.class,0);
		}
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode REGISTER() { return getToken(WatParser.REGISTER, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public TerminalNode VAR() { return getToken(WatParser.VAR, 0); }
		public MetaContext meta() {
			return getRuleContext(MetaContext.class,0);
		}
		public CmdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmd; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterCmd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitCmd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitCmd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CmdContext cmd() throws RecognitionException {
		CmdContext _localctx = new CmdContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_cmd);
		int _la;
		try {
			setState(907);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,91,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(895);
				action_();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(896);
				assertion();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(897);
				scriptModule();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(898);
				match(LPAR);
				setState(899);
				match(REGISTER);
				setState(900);
				name();
				setState(902);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(901);
					match(VAR);
					}
				}

				setState(904);
				match(RPAR);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(906);
				meta();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MetaContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode SCRIPT() { return getToken(WatParser.SCRIPT, 0); }
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public TerminalNode VAR() { return getToken(WatParser.VAR, 0); }
		public List<CmdContext> cmd() {
			return getRuleContexts(CmdContext.class);
		}
		public CmdContext cmd(int i) {
			return getRuleContext(CmdContext.class,i);
		}
		public TerminalNode INPUT() { return getToken(WatParser.INPUT, 0); }
		public TerminalNode STRING_() { return getToken(WatParser.STRING_, 0); }
		public TerminalNode OUTPUT() { return getToken(WatParser.OUTPUT, 0); }
		public MetaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_meta; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterMeta(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitMeta(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitMeta(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetaContext meta() throws RecognitionException {
		MetaContext _localctx = new MetaContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_meta);
		int _la;
		try {
			setState(941);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,97,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(909);
				match(LPAR);
				setState(910);
				match(SCRIPT);
				setState(912);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(911);
					match(VAR);
					}
				}

				setState(917);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LPAR) {
					{
					{
					setState(914);
					cmd();
					}
					}
					setState(919);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(920);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(921);
				match(LPAR);
				setState(922);
				match(INPUT);
				setState(924);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(923);
					match(VAR);
					}
				}

				setState(926);
				match(STRING_);
				setState(927);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(928);
				match(LPAR);
				setState(929);
				match(OUTPUT);
				setState(931);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(930);
					match(VAR);
					}
				}

				setState(933);
				match(STRING_);
				setState(934);
				match(RPAR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(935);
				match(LPAR);
				setState(936);
				match(OUTPUT);
				setState(938);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(937);
					match(VAR);
					}
				}

				setState(940);
				match(RPAR);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WconstContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode CONST() { return getToken(WatParser.CONST, 0); }
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public WconstContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wconst; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterWconst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitWconst(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitWconst(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WconstContext wconst() throws RecognitionException {
		WconstContext _localctx = new WconstContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_wconst);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(943);
			match(LPAR);
			setState(944);
			match(CONST);
			setState(945);
			literal();
			setState(946);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstListContext extends ParserRuleContext {
		public List<WconstContext> wconst() {
			return getRuleContexts(WconstContext.class);
		}
		public WconstContext wconst(int i) {
			return getRuleContext(WconstContext.class,i);
		}
		public ConstListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterConstList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitConstList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitConstList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstListContext constList() throws RecognitionException {
		ConstListContext _localctx = new ConstListContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_constList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(951);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAR) {
				{
				{
				setState(948);
				wconst();
				}
				}
				setState(953);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ScriptContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(WatParser.EOF, 0); }
		public List<CmdContext> cmd() {
			return getRuleContexts(CmdContext.class);
		}
		public CmdContext cmd(int i) {
			return getRuleContext(CmdContext.class,i);
		}
		public List<ModuleFieldContext> moduleField() {
			return getRuleContexts(ModuleFieldContext.class);
		}
		public ModuleFieldContext moduleField(int i) {
			return getRuleContext(ModuleFieldContext.class,i);
		}
		public ScriptContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_script; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterScript(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitScript(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitScript(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ScriptContext script() throws RecognitionException {
		ScriptContext _localctx = new ScriptContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_script);
		int _la;
		try {
			setState(968);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,101,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(957);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LPAR) {
					{
					{
					setState(954);
					cmd();
					}
					}
					setState(959);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(960);
				match(EOF);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(962); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(961);
					moduleField();
					}
					}
					setState(964); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==LPAR );
				setState(966);
				match(EOF);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ModuleContext extends ParserRuleContext {
		public Module_Context module_() {
			return getRuleContext(Module_Context.class,0);
		}
		public TerminalNode EOF() { return getToken(WatParser.EOF, 0); }
		public List<ModuleFieldContext> moduleField() {
			return getRuleContexts(ModuleFieldContext.class);
		}
		public ModuleFieldContext moduleField(int i) {
			return getRuleContext(ModuleFieldContext.class,i);
		}
		public ModuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitModule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitModule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleContext module() throws RecognitionException {
		ModuleContext _localctx = new ModuleContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_module);
		int _la;
		try {
			setState(980);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(970);
				module_();
				setState(971);
				match(EOF);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(976);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LPAR) {
					{
					{
					setState(973);
					moduleField();
					}
					}
					setState(978);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(979);
				match(EOF);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u008f\u03d7\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007"+
		"\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007"+
		"\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007"+
		"\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007"+
		"\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007"+
		"\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007"+
		"\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007"+
		"\"\u0002#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007"+
		"\'\u0002(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007"+
		",\u0002-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u00070\u00021\u0007"+
		"1\u00022\u00072\u00023\u00073\u00024\u00074\u00025\u00075\u00026\u0007"+
		"6\u00027\u00077\u00028\u00078\u00029\u00079\u0002:\u0007:\u0002;\u0007"+
		";\u0002<\u0007<\u0002=\u0007=\u0002>\u0007>\u0002?\u0007?\u0002@\u0007"+
		"@\u0002A\u0007A\u0002B\u0007B\u0002C\u0007C\u0002D\u0007D\u0002E\u0007"+
		"E\u0002F\u0007F\u0002G\u0007G\u0001\u0000\u0001\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001"+
		"\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005\u009e\b\u0005\u0001"+
		"\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0003\u0007\u00a8\b\u0007\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0001\t\u0001\t\u0001\t\u0005\t\u00b2\b\t\n\t\f\t\u00b5\t\t"+
		"\u0001\t\u0001\t\u0001\t\u0003\t\u00ba\b\t\u0001\t\u0005\t\u00bd\b\t\n"+
		"\t\f\t\u00c0\t\t\u0001\n\u0001\n\u0001\n\u0005\n\u00c5\b\n\n\n\f\n\u00c8"+
		"\t\n\u0001\n\u0005\n\u00cb\b\n\n\n\f\n\u00ce\t\n\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\f\u0001\f\u0003\f\u00d5\b\f\u0001\f\u0001\f\u0001\r"+
		"\u0001\r\u0003\r\u00db\b\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0011"+
		"\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012"+
		"\u00ec\b\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0004\u0013"+
		"\u00f8\b\u0013\u000b\u0013\f\u0013\u00f9\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0003\u0013\u010b\b\u0013\u0001\u0013\u0003\u0013\u010e\b\u0013"+
		"\u0001\u0013\u0001\u0013\u0003\u0013\u0112\b\u0013\u0001\u0013\u0003\u0013"+
		"\u0115\b\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0003\u0013\u0124\b\u0013\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u0131\b\u0016\u0001\u0017"+
		"\u0001\u0017\u0001\u0017\u0003\u0017\u0136\b\u0017\u0001\u0018\u0001\u0018"+
		"\u0003\u0018\u013a\b\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0005\u0019\u0141\b\u0019\n\u0019\f\u0019\u0144\t\u0019\u0001"+
		"\u0019\u0005\u0019\u0147\b\u0019\n\u0019\f\u0019\u014a\t\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0005\u0019\u014f\b\u0019\n\u0019\f\u0019\u0152"+
		"\t\u0019\u0001\u0019\u0005\u0019\u0155\b\u0019\n\u0019\f\u0019\u0158\t"+
		"\u0019\u0001\u001a\u0001\u001a\u0003\u001a\u015c\b\u001a\u0001\u001a\u0001"+
		"\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0005\u001b\u0163\b\u001b\n"+
		"\u001b\f\u001b\u0166\t\u001b\u0001\u001b\u0005\u001b\u0169\b\u001b\n\u001b"+
		"\f\u001b\u016c\t\u001b\u0001\u001b\u0001\u001b\u0001\u001c\u0001\u001c"+
		"\u0001\u001c\u0005\u001c\u0173\b\u001c\n\u001c\f\u001c\u0176\t\u001c\u0001"+
		"\u001c\u0005\u001c\u0179\b\u001c\n\u001c\f\u001c\u017c\t\u001c\u0001\u001c"+
		"\u0001\u001c\u0001\u001d\u0001\u001d\u0003\u001d\u0182\b\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0003\u001d\u0187\b\u001d\u0001\u001d\u0001\u001d"+
		"\u0003\u001d\u018b\b\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0003\u001d"+
		"\u0190\b\u001d\u0001\u001d\u0003\u001d\u0193\b\u001d\u0001\u001d\u0001"+
		"\u001d\u0003\u001d\u0197\b\u001d\u0003\u001d\u0199\b\u001d\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001f\u0003\u001f"+
		"\u01a1\b\u001f\u0001\u001f\u0001\u001f\u0001 \u0001 \u0001 \u0001 \u0001"+
		"!\u0001!\u0005!\u01ab\b!\n!\f!\u01ae\t!\u0001!\u0001!\u0001!\u0001!\u0003"+
		"!\u01b4\b!\u0001!\u0001!\u0001!\u0003!\u01b9\b!\u0001!\u0001!\u0001!\u0003"+
		"!\u01be\b!\u0001!\u0003!\u01c1\b!\u0001\"\u0003\"\u01c4\b\"\u0001\"\u0001"+
		"\"\u0001#\u0001#\u0001#\u0005#\u01cb\b#\n#\f#\u01ce\t#\u0001#\u0005#\u01d1"+
		"\b#\n#\f#\u01d4\t#\u0001#\u0001#\u0001$\u0001$\u0001$\u0005$\u01db\b$"+
		"\n$\f$\u01de\t$\u0001$\u0005$\u01e1\b$\n$\f$\u01e4\t$\u0001$\u0005$\u01e7"+
		"\b$\n$\f$\u01ea\t$\u0001%\u0001%\u0001%\u0001%\u0005%\u01f0\b%\n%\f%\u01f3"+
		"\t%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0003"+
		"%\u01fe\b%\u0003%\u0200\b%\u0001&\u0005&\u0203\b&\n&\f&\u0206\t&\u0001"+
		"&\u0003&\u0209\b&\u0001\'\u0001\'\u0001(\u0001(\u0001(\u0003(\u0210\b"+
		"(\u0001(\u0001(\u0001(\u0001)\u0003)\u0216\b)\u0001)\u0001)\u0001)\u0003"+
		")\u021b\b)\u0001)\u0001)\u0001)\u0001)\u0001)\u0003)\u0222\b)\u0001*\u0001"+
		"*\u0001*\u0001+\u0001+\u0001+\u0005+\u022a\b+\n+\f+\u022d\t+\u0001+\u0001"+
		"+\u0001+\u0003+\u0232\b+\u0001+\u0005+\u0235\b+\n+\f+\u0238\t+\u0001+"+
		"\u0001+\u0001,\u0001,\u0001,\u0001,\u0001,\u0001,\u0003,\u0242\b,\u0001"+
		"-\u0001-\u0001-\u0003-\u0247\b-\u0001-\u0001-\u0005-\u024b\b-\n-\f-\u024e"+
		"\t-\u0001-\u0001-\u0001.\u0001.\u0001.\u0003.\u0255\b.\u0001.\u0001.\u0001"+
		".\u0001/\u0001/\u0001/\u0001/\u0001/\u0001/\u0001/\u0001/\u0001/\u0001"+
		"/\u0001/\u0005/\u0265\b/\n/\f/\u0268\t/\u0001/\u0001/\u0003/\u026c\b/"+
		"\u00010\u00010\u00010\u00030\u0271\b0\u00010\u00010\u00050\u0275\b0\n"+
		"0\f0\u0278\t0\u00010\u00010\u00011\u00011\u00011\u00031\u027f\b1\u0001"+
		"1\u00011\u00011\u00012\u00012\u00012\u00012\u00012\u00012\u00012\u0001"+
		"2\u00012\u00012\u00052\u028e\b2\n2\f2\u0291\t2\u00012\u00032\u0294\b2"+
		"\u00013\u00013\u00013\u00033\u0299\b3\u00013\u00013\u00013\u00014\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00034\u02a7\b4\u0001"+
		"5\u00015\u00015\u00035\u02ac\b5\u00015\u00015\u00015\u00015\u00015\u0001"+
		"5\u00035\u02b4\b5\u00015\u00015\u00015\u00015\u00015\u00015\u00035\u02bc"+
		"\b5\u00015\u00015\u00015\u00015\u00015\u00015\u00035\u02c4\b5\u00015\u0001"+
		"5\u00015\u00015\u00015\u00015\u00035\u02cc\b5\u00015\u00015\u00015\u0003"+
		"5\u02d1\b5\u00016\u00016\u00016\u00016\u00016\u00016\u00016\u00017\u0001"+
		"7\u00017\u00017\u00017\u00017\u00018\u00018\u00018\u00018\u00018\u0001"+
		"8\u00018\u00018\u00018\u00018\u00018\u00018\u00018\u00018\u00018\u0001"+
		"8\u00018\u00018\u00018\u00018\u00038\u02f4\b8\u00019\u00019\u00019\u0001"+
		"9\u00019\u00019\u0001:\u0001:\u0001:\u0001:\u0001:\u0001;\u0001;\u0001"+
		";\u0003;\u0304\b;\u0001;\u0001;\u0001;\u0001<\u0001<\u0001<\u0001<\u0001"+
		"<\u0001=\u0001=\u0001=\u0001=\u0001=\u0001=\u0001=\u0001=\u0001=\u0001"+
		"=\u0003=\u0318\b=\u0001>\u0001>\u0001>\u0003>\u031d\b>\u0001>\u0005>\u0320"+
		"\b>\n>\f>\u0323\t>\u0001>\u0001>\u0001?\u0001?\u0001?\u0001?\u0003?\u032b"+
		"\b?\u0001?\u0001?\u0005?\u032f\b?\n?\f?\u0332\t?\u0001?\u0003?\u0335\b"+
		"?\u0001@\u0001@\u0001@\u0003@\u033a\b@\u0001@\u0001@\u0001@\u0001@\u0001"+
		"@\u0001@\u0001@\u0003@\u0343\b@\u0001@\u0001@\u0001@\u0003@\u0348\b@\u0001"+
		"A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001"+
		"A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001"+
		"A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001"+
		"A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001"+
		"A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001"+
		"A\u0001A\u0003A\u037e\bA\u0001B\u0001B\u0001B\u0001B\u0001B\u0001B\u0001"+
		"B\u0003B\u0387\bB\u0001B\u0001B\u0001B\u0003B\u038c\bB\u0001C\u0001C\u0001"+
		"C\u0003C\u0391\bC\u0001C\u0005C\u0394\bC\nC\fC\u0397\tC\u0001C\u0001C"+
		"\u0001C\u0001C\u0003C\u039d\bC\u0001C\u0001C\u0001C\u0001C\u0001C\u0003"+
		"C\u03a4\bC\u0001C\u0001C\u0001C\u0001C\u0001C\u0003C\u03ab\bC\u0001C\u0003"+
		"C\u03ae\bC\u0001D\u0001D\u0001D\u0001D\u0001D\u0001E\u0005E\u03b6\bE\n"+
		"E\fE\u03b9\tE\u0001F\u0005F\u03bc\bF\nF\fF\u03bf\tF\u0001F\u0001F\u0004"+
		"F\u03c3\bF\u000bF\fF\u03c4\u0001F\u0001F\u0003F\u03c9\bF\u0001G\u0001"+
		"G\u0001G\u0001G\u0005G\u03cf\bG\nG\fG\u03d2\tG\u0001G\u0003G\u03d5\bG"+
		"\u0001G\u0000\u0000H\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014"+
		"\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfh"+
		"jlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0000\u0007"+
		"\u0001\u0000\u0004\u0005\u0001\u0000\t\n\u0001\u0000mn\u0001\u0000\u0003"+
		"\u0005\u0002\u0000\u0003\u0003\u008c\u008c\u0001\u0000\u000f\u0010\u0001"+
		"\u0000|}\u0434\u0000\u0090\u0001\u0000\u0000\u0000\u0002\u0092\u0001\u0000"+
		"\u0000\u0000\u0004\u0094\u0001\u0000\u0000\u0000\u0006\u0096\u0001\u0000"+
		"\u0000\u0000\b\u0098\u0001\u0000\u0000\u0000\n\u009d\u0001\u0000\u0000"+
		"\u0000\f\u009f\u0001\u0000\u0000\u0000\u000e\u00a7\u0001\u0000\u0000\u0000"+
		"\u0010\u00a9\u0001\u0000\u0000\u0000\u0012\u00be\u0001\u0000\u0000\u0000"+
		"\u0014\u00cc\u0001\u0000\u0000\u0000\u0016\u00cf\u0001\u0000\u0000\u0000"+
		"\u0018\u00d2\u0001\u0000\u0000\u0000\u001a\u00d8\u0001\u0000\u0000\u0000"+
		"\u001c\u00dc\u0001\u0000\u0000\u0000\u001e\u00e1\u0001\u0000\u0000\u0000"+
		" \u00e3\u0001\u0000\u0000\u0000\"\u00e5\u0001\u0000\u0000\u0000$\u00eb"+
		"\u0001\u0000\u0000\u0000&\u0123\u0001\u0000\u0000\u0000(\u0125\u0001\u0000"+
		"\u0000\u0000*\u0128\u0001\u0000\u0000\u0000,\u012b\u0001\u0000\u0000\u0000"+
		".\u0132\u0001\u0000\u0000\u00000\u0137\u0001\u0000\u0000\u00002\u0148"+
		"\u0001\u0000\u0000\u00004\u0159\u0001\u0000\u0000\u00006\u016a\u0001\u0000"+
		"\u0000\u00008\u017a\u0001\u0000\u0000\u0000:\u0198\u0001\u0000\u0000\u0000"+
		"<\u019a\u0001\u0000\u0000\u0000>\u01a0\u0001\u0000\u0000\u0000@\u01a4"+
		"\u0001\u0000\u0000\u0000B\u01c0\u0001\u0000\u0000\u0000D\u01c3\u0001\u0000"+
		"\u0000\u0000F\u01d2\u0001\u0000\u0000\u0000H\u01e2\u0001\u0000\u0000\u0000"+
		"J\u01ff\u0001\u0000\u0000\u0000L\u0204\u0001\u0000\u0000\u0000N\u020a"+
		"\u0001\u0000\u0000\u0000P\u020c\u0001\u0000\u0000\u0000R\u0221\u0001\u0000"+
		"\u0000\u0000T\u0223\u0001\u0000\u0000\u0000V\u0236\u0001\u0000\u0000\u0000"+
		"X\u0241\u0001\u0000\u0000\u0000Z\u0243\u0001\u0000\u0000\u0000\\\u0251"+
		"\u0001\u0000\u0000\u0000^\u026b\u0001\u0000\u0000\u0000`\u026d\u0001\u0000"+
		"\u0000\u0000b\u027b\u0001\u0000\u0000\u0000d\u0293\u0001\u0000\u0000\u0000"+
		"f\u0295\u0001\u0000\u0000\u0000h\u02a6\u0001\u0000\u0000\u0000j\u02d0"+
		"\u0001\u0000\u0000\u0000l\u02d2\u0001\u0000\u0000\u0000n\u02d9\u0001\u0000"+
		"\u0000\u0000p\u02f3\u0001\u0000\u0000\u0000r\u02f5\u0001\u0000\u0000\u0000"+
		"t\u02fb\u0001\u0000\u0000\u0000v\u0300\u0001\u0000\u0000\u0000x\u0308"+
		"\u0001\u0000\u0000\u0000z\u0317\u0001\u0000\u0000\u0000|\u0319\u0001\u0000"+
		"\u0000\u0000~\u0334\u0001\u0000\u0000\u0000\u0080\u0347\u0001\u0000\u0000"+
		"\u0000\u0082\u037d\u0001\u0000\u0000\u0000\u0084\u038b\u0001\u0000\u0000"+
		"\u0000\u0086\u03ad\u0001\u0000\u0000\u0000\u0088\u03af\u0001\u0000\u0000"+
		"\u0000\u008a\u03b7\u0001\u0000\u0000\u0000\u008c\u03c8\u0001\u0000\u0000"+
		"\u0000\u008e\u03d4\u0001\u0000\u0000\u0000\u0090\u0091\u0007\u0000\u0000"+
		"\u0000\u0091\u0001\u0001\u0000\u0000\u0000\u0092\u0093\u0005\u0006\u0000"+
		"\u0000\u0093\u0003\u0001\u0000\u0000\u0000\u0094\u0095\u0005\u0007\u0000"+
		"\u0000\u0095\u0005\u0001\u0000\u0000\u0000\u0096\u0097\u0007\u0001\u0000"+
		"\u0000\u0097\u0007\u0001\u0000\u0000\u0000\u0098\u0099\u0005\u008d\u0000"+
		"\u0000\u0099\t\u0001\u0000\u0000\u0000\u009a\u009e\u0003\u0004\u0002\u0000"+
		"\u009b\u009e\u0003\b\u0004\u0000\u009c\u009e\u0003\u0006\u0003\u0000\u009d"+
		"\u009a\u0001\u0000\u0000\u0000\u009d\u009b\u0001\u0000\u0000\u0000\u009d"+
		"\u009c\u0001\u0000\u0000\u0000\u009e\u000b\u0001\u0000\u0000\u0000\u009f"+
		"\u00a0\u0007\u0002\u0000\u0000\u00a0\r\u0001\u0000\u0000\u0000\u00a1\u00a8"+
		"\u0003\n\u0005\u0000\u00a2\u00a3\u0005\u0001\u0000\u0000\u00a3\u00a4\u0005"+
		"\u000b\u0000\u0000\u00a4\u00a5\u0003\n\u0005\u0000\u00a5\u00a6\u0005\u0002"+
		"\u0000\u0000\u00a6\u00a8\u0001\u0000\u0000\u0000\u00a7\u00a1\u0001\u0000"+
		"\u0000\u0000\u00a7\u00a2\u0001\u0000\u0000\u0000\u00a8\u000f\u0001\u0000"+
		"\u0000\u0000\u00a9\u00aa\u0005\u0001\u0000\u0000\u00aa\u00ab\u0005m\u0000"+
		"\u0000\u00ab\u00ac\u0003\u0016\u000b\u0000\u00ac\u00ad\u0005\u0002\u0000"+
		"\u0000\u00ad\u0011\u0001\u0000\u0000\u0000\u00ae\u00af\u0005\u0001\u0000"+
		"\u0000\u00af\u00b9\u0005p\u0000\u0000\u00b0\u00b2\u0003\n\u0005\u0000"+
		"\u00b1\u00b0\u0001\u0000\u0000\u0000\u00b2\u00b5\u0001\u0000\u0000\u0000"+
		"\u00b3\u00b1\u0001\u0000\u0000\u0000\u00b3\u00b4\u0001\u0000\u0000\u0000"+
		"\u00b4\u00ba\u0001\u0000\u0000\u0000\u00b5\u00b3\u0001\u0000\u0000\u0000"+
		"\u00b6\u00b7\u0003\"\u0011\u0000\u00b7\u00b8\u0003\n\u0005\u0000\u00b8"+
		"\u00ba\u0001\u0000\u0000\u0000\u00b9\u00b3\u0001\u0000\u0000\u0000\u00b9"+
		"\u00b6\u0001\u0000\u0000\u0000\u00ba\u00bb\u0001\u0000\u0000\u0000\u00bb"+
		"\u00bd\u0005\u0002\u0000\u0000\u00bc\u00ae\u0001\u0000\u0000\u0000\u00bd"+
		"\u00c0\u0001\u0000\u0000\u0000\u00be\u00bc\u0001\u0000\u0000\u0000\u00be"+
		"\u00bf\u0001\u0000\u0000\u0000\u00bf\u0013\u0001\u0000\u0000\u0000\u00c0"+
		"\u00be\u0001\u0000\u0000\u0000\u00c1\u00c2\u0005\u0001\u0000\u0000\u00c2"+
		"\u00c6\u0005q\u0000\u0000\u00c3\u00c5\u0003\n\u0005\u0000\u00c4\u00c3"+
		"\u0001\u0000\u0000\u0000\u00c5\u00c8\u0001\u0000\u0000\u0000\u00c6\u00c4"+
		"\u0001\u0000\u0000\u0000\u00c6\u00c7\u0001\u0000\u0000\u0000\u00c7\u00c9"+
		"\u0001\u0000\u0000\u0000\u00c8\u00c6\u0001\u0000\u0000\u0000\u00c9\u00cb"+
		"\u0005\u0002\u0000\u0000\u00ca\u00c1\u0001\u0000\u0000\u0000\u00cb\u00ce"+
		"\u0001\u0000\u0000\u0000\u00cc\u00ca\u0001\u0000\u0000\u0000\u00cc\u00cd"+
		"\u0001\u0000\u0000\u0000\u00cd\u0015\u0001\u0000\u0000\u0000\u00ce\u00cc"+
		"\u0001\u0000\u0000\u0000\u00cf\u00d0\u0003\u0012\t\u0000\u00d0\u00d1\u0003"+
		"\u0014\n\u0000\u00d1\u0017\u0001\u0000\u0000\u0000\u00d2\u00d4\u0005\u0003"+
		"\u0000\u0000\u00d3\u00d5\u0005\u0003\u0000\u0000\u00d4\u00d3\u0001\u0000"+
		"\u0000\u0000\u00d4\u00d5\u0001\u0000\u0000\u0000\u00d5\u00d6\u0001\u0000"+
		"\u0000\u0000\u00d6\u00d7\u0003\u0006\u0003\u0000\u00d7\u0019\u0001\u0000"+
		"\u0000\u0000\u00d8\u00da\u0005\u0003\u0000\u0000\u00d9\u00db\u0005\u0003"+
		"\u0000\u0000\u00da\u00d9\u0001\u0000\u0000\u0000\u00da\u00db\u0001\u0000"+
		"\u0000\u0000\u00db\u001b\u0001\u0000\u0000\u0000\u00dc\u00dd\u0005\u0001"+
		"\u0000\u0000\u00dd\u00de\u0005l\u0000\u0000\u00de\u00df\u0003 \u0010\u0000"+
		"\u00df\u00e0\u0005\u0002\u0000\u0000\u00e0\u001d\u0001\u0000\u0000\u0000"+
		"\u00e1\u00e2\u0007\u0003\u0000\u0000\u00e2\u001f\u0001\u0000\u0000\u0000"+
		"\u00e3\u00e4\u0007\u0004\u0000\u0000\u00e4!\u0001\u0000\u0000\u0000\u00e5"+
		"\u00e6\u0005\u008c\u0000\u0000\u00e6#\u0001\u0000\u0000\u0000\u00e7\u00ec"+
		"\u0003&\u0013\u0000\u00e8\u00ec\u00034\u001a\u0000\u00e9\u00ec\u0003:"+
		"\u001d\u0000\u00ea\u00ec\u0003@ \u0000\u00eb\u00e7\u0001\u0000\u0000\u0000"+
		"\u00eb\u00e8\u0001\u0000\u0000\u0000\u00eb\u00e9\u0001\u0000\u0000\u0000"+
		"\u00eb\u00ea\u0001\u0000\u0000\u0000\u00ec%\u0001\u0000\u0000\u0000\u00ed"+
		"\u0124\u0005\r\u0000\u0000\u00ee\u0124\u0005\f\u0000\u0000\u00ef\u0124"+
		"\u0005\u000e\u0000\u0000\u00f0\u0124\u0005\u0019\u0000\u0000\u00f1\u00f2"+
		"\u0005\u0012\u0000\u0000\u00f2\u0124\u0003 \u0010\u0000\u00f3\u00f4\u0005"+
		"\u0013\u0000\u0000\u00f4\u0124\u0003 \u0010\u0000\u00f5\u00f7\u0005\u0014"+
		"\u0000\u0000\u00f6\u00f8\u0003 \u0010\u0000\u00f7\u00f6\u0001\u0000\u0000"+
		"\u0000\u00f8\u00f9\u0001\u0000\u0000\u0000\u00f9\u00f7\u0001\u0000\u0000"+
		"\u0000\u00f9\u00fa\u0001\u0000\u0000\u0000\u00fa\u0124\u0001\u0000\u0000"+
		"\u0000\u00fb\u0124\u0005\u0015\u0000\u0000\u00fc\u00fd\u0005\u001a\u0000"+
		"\u0000\u00fd\u0124\u0003 \u0010\u0000\u00fe\u00ff\u0005\u001c\u0000\u0000"+
		"\u00ff\u0124\u0003 \u0010\u0000\u0100\u0101\u0005\u001d\u0000\u0000\u0101"+
		"\u0124\u0003 \u0010\u0000\u0102\u0103\u0005\u001e\u0000\u0000\u0103\u0124"+
		"\u0003 \u0010\u0000\u0104\u0105\u0005\u001f\u0000\u0000\u0105\u0124\u0003"+
		" \u0010\u0000\u0106\u0107\u0005 \u0000\u0000\u0107\u0124\u0003 \u0010"+
		"\u0000\u0108\u010a\u0003,\u0016\u0000\u0109\u010b\u0003(\u0014\u0000\u010a"+
		"\u0109\u0001\u0000\u0000\u0000\u010a\u010b\u0001\u0000\u0000\u0000\u010b"+
		"\u010d\u0001\u0000\u0000\u0000\u010c\u010e\u0003*\u0015\u0000\u010d\u010c"+
		"\u0001\u0000\u0000\u0000\u010d\u010e\u0001\u0000\u0000\u0000\u010e\u0124"+
		"\u0001\u0000\u0000\u0000\u010f\u0111\u0003.\u0017\u0000\u0110\u0112\u0003"+
		"(\u0014\u0000\u0111\u0110\u0001\u0000\u0000\u0000\u0111\u0112\u0001\u0000"+
		"\u0000\u0000\u0112\u0114\u0001\u0000\u0000\u0000\u0113\u0115\u0003*\u0015"+
		"\u0000\u0114\u0113\u0001\u0000\u0000\u0000\u0114\u0115\u0001\u0000\u0000"+
		"\u0000\u0115\u0124\u0001\u0000\u0000\u0000\u0116\u0124\u0005b\u0000\u0000"+
		"\u0117\u0124\u0005c\u0000\u0000\u0118\u0124\u0005d\u0000\u0000\u0119\u0124"+
		"\u0005e\u0000\u0000\u011a\u011b\u0005f\u0000\u0000\u011b\u0124\u0003 "+
		"\u0010\u0000\u011c\u011d\u0005\b\u0000\u0000\u011d\u0124\u0003\u001e\u000f"+
		"\u0000\u011e\u0124\u0005g\u0000\u0000\u011f\u0124\u0005h\u0000\u0000\u0120"+
		"\u0124\u0005i\u0000\u0000\u0121\u0124\u0005j\u0000\u0000\u0122\u0124\u0005"+
		"k\u0000\u0000\u0123\u00ed\u0001\u0000\u0000\u0000\u0123\u00ee\u0001\u0000"+
		"\u0000\u0000\u0123\u00ef\u0001\u0000\u0000\u0000\u0123\u00f0\u0001\u0000"+
		"\u0000\u0000\u0123\u00f1\u0001\u0000\u0000\u0000\u0123\u00f3\u0001\u0000"+
		"\u0000\u0000\u0123\u00f5\u0001\u0000\u0000\u0000\u0123\u00fb\u0001\u0000"+
		"\u0000\u0000\u0123\u00fc\u0001\u0000\u0000\u0000\u0123\u00fe\u0001\u0000"+
		"\u0000\u0000\u0123\u0100\u0001\u0000\u0000\u0000\u0123\u0102\u0001\u0000"+
		"\u0000\u0000\u0123\u0104\u0001\u0000\u0000\u0000\u0123\u0106\u0001\u0000"+
		"\u0000\u0000\u0123\u0108\u0001\u0000\u0000\u0000\u0123\u010f\u0001\u0000"+
		"\u0000\u0000\u0123\u0116\u0001\u0000\u0000\u0000\u0123\u0117\u0001\u0000"+
		"\u0000\u0000\u0123\u0118\u0001\u0000\u0000\u0000\u0123\u0119\u0001\u0000"+
		"\u0000\u0000\u0123\u011a\u0001\u0000\u0000\u0000\u0123\u011c\u0001\u0000"+
		"\u0000\u0000\u0123\u011e\u0001\u0000\u0000\u0000\u0123\u011f\u0001\u0000"+
		"\u0000\u0000\u0123\u0120\u0001\u0000\u0000\u0000\u0123\u0121\u0001\u0000"+
		"\u0000\u0000\u0123\u0122\u0001\u0000\u0000\u0000\u0124\'\u0001\u0000\u0000"+
		"\u0000\u0125\u0126\u0005$\u0000\u0000\u0126\u0127\u0005\u0003\u0000\u0000"+
		"\u0127)\u0001\u0000\u0000\u0000\u0128\u0129\u0005%\u0000\u0000\u0129\u012a"+
		"\u0005\u0003\u0000\u0000\u012a+\u0001\u0000\u0000\u0000\u012b\u012c\u0003"+
		"\u0004\u0002\u0000\u012c\u0130\u0005!\u0000\u0000\u012d\u012e\u0005\'"+
		"\u0000\u0000\u012e\u012f\u0005#\u0000\u0000\u012f\u0131\u0005&\u0000\u0000"+
		"\u0130\u012d\u0001\u0000\u0000\u0000\u0130\u0131\u0001\u0000\u0000\u0000"+
		"\u0131-\u0001\u0000\u0000\u0000\u0132\u0133\u0003\u0004\u0002\u0000\u0133"+
		"\u0135\u0005\"\u0000\u0000\u0134\u0136\u0005\'\u0000\u0000\u0135\u0134"+
		"\u0001\u0000\u0000\u0000\u0135\u0136\u0001\u0000\u0000\u0000\u0136/\u0001"+
		"\u0000\u0000\u0000\u0137\u0139\u0005\u001b\u0000\u0000\u0138\u013a\u0003"+
		"\u001c\u000e\u0000\u0139\u0138\u0001\u0000\u0000\u0000\u0139\u013a\u0001"+
		"\u0000\u0000\u0000\u013a\u013b\u0001\u0000\u0000\u0000\u013b\u013c\u0003"+
		"2\u0019\u0000\u013c1\u0001\u0000\u0000\u0000\u013d\u013e\u0005\u0001\u0000"+
		"\u0000\u013e\u0142\u0005p\u0000\u0000\u013f\u0141\u0003\n\u0005\u0000"+
		"\u0140\u013f\u0001\u0000\u0000\u0000\u0141\u0144\u0001\u0000\u0000\u0000"+
		"\u0142\u0140\u0001\u0000\u0000\u0000\u0142\u0143\u0001\u0000\u0000\u0000"+
		"\u0143\u0145\u0001\u0000\u0000\u0000\u0144\u0142\u0001\u0000\u0000\u0000"+
		"\u0145\u0147\u0005\u0002\u0000\u0000\u0146\u013d\u0001\u0000\u0000\u0000"+
		"\u0147\u014a\u0001\u0000\u0000\u0000\u0148\u0146\u0001\u0000\u0000\u0000"+
		"\u0148\u0149\u0001\u0000\u0000\u0000\u0149\u0156\u0001\u0000\u0000\u0000"+
		"\u014a\u0148\u0001\u0000\u0000\u0000\u014b\u014c\u0005\u0001\u0000\u0000"+
		"\u014c\u0150\u0005q\u0000\u0000\u014d\u014f\u0003\n\u0005\u0000\u014e"+
		"\u014d\u0001\u0000\u0000\u0000\u014f\u0152\u0001\u0000\u0000\u0000\u0150"+
		"\u014e\u0001\u0000\u0000\u0000\u0150\u0151\u0001\u0000\u0000\u0000\u0151"+
		"\u0153\u0001\u0000\u0000\u0000\u0152\u0150\u0001\u0000\u0000\u0000\u0153"+
		"\u0155\u0005\u0002\u0000\u0000\u0154\u014b\u0001\u0000\u0000\u0000\u0155"+
		"\u0158\u0001\u0000\u0000\u0000\u0156\u0154\u0001\u0000\u0000\u0000\u0156"+
		"\u0157\u0001\u0000\u0000\u0000\u01573\u0001\u0000\u0000\u0000\u0158\u0156"+
		"\u0001\u0000\u0000\u0000\u0159\u015b\u0005\u001b\u0000\u0000\u015a\u015c"+
		"\u0003\u001c\u000e\u0000\u015b\u015a\u0001\u0000\u0000\u0000\u015b\u015c"+
		"\u0001\u0000\u0000\u0000\u015c\u015d\u0001\u0000\u0000\u0000\u015d\u015e"+
		"\u00036\u001b\u0000\u015e5\u0001\u0000\u0000\u0000\u015f\u0160\u0005\u0001"+
		"\u0000\u0000\u0160\u0164\u0005p\u0000\u0000\u0161\u0163\u0003\n\u0005"+
		"\u0000\u0162\u0161\u0001\u0000\u0000\u0000\u0163\u0166\u0001\u0000\u0000"+
		"\u0000\u0164\u0162\u0001\u0000\u0000\u0000\u0164\u0165\u0001\u0000\u0000"+
		"\u0000\u0165\u0167\u0001\u0000\u0000\u0000\u0166\u0164\u0001\u0000\u0000"+
		"\u0000\u0167\u0169\u0005\u0002\u0000\u0000\u0168\u015f\u0001\u0000\u0000"+
		"\u0000\u0169\u016c\u0001\u0000\u0000\u0000\u016a\u0168\u0001\u0000\u0000"+
		"\u0000\u016a\u016b\u0001\u0000\u0000\u0000\u016b\u016d\u0001\u0000\u0000"+
		"\u0000\u016c\u016a\u0001\u0000\u0000\u0000\u016d\u016e\u00038\u001c\u0000"+
		"\u016e7\u0001\u0000\u0000\u0000\u016f\u0170\u0005\u0001\u0000\u0000\u0170"+
		"\u0174\u0005q\u0000\u0000\u0171\u0173\u0003\n\u0005\u0000\u0172\u0171"+
		"\u0001\u0000\u0000\u0000\u0173\u0176\u0001\u0000\u0000\u0000\u0174\u0172"+
		"\u0001\u0000\u0000\u0000\u0174\u0175\u0001\u0000\u0000\u0000\u0175\u0177"+
		"\u0001\u0000\u0000\u0000\u0176\u0174\u0001\u0000\u0000\u0000\u0177\u0179"+
		"\u0005\u0002\u0000\u0000\u0178\u016f\u0001\u0000\u0000\u0000\u0179\u017c"+
		"\u0001\u0000\u0000\u0000\u017a\u0178\u0001\u0000\u0000\u0000\u017a\u017b"+
		"\u0001\u0000\u0000\u0000\u017b\u017d\u0001\u0000\u0000\u0000\u017c\u017a"+
		"\u0001\u0000\u0000\u0000\u017d\u017e\u0003$\u0012\u0000\u017e9\u0001\u0000"+
		"\u0000\u0000\u017f\u0181\u0007\u0005\u0000\u0000\u0180\u0182\u0003\"\u0011"+
		"\u0000\u0181\u0180\u0001\u0000\u0000\u0000\u0181\u0182\u0001\u0000\u0000"+
		"\u0000\u0182\u0183\u0001\u0000\u0000\u0000\u0183\u0184\u0003>\u001f\u0000"+
		"\u0184\u0186\u0005\u0011\u0000\u0000\u0185\u0187\u0003\"\u0011\u0000\u0186"+
		"\u0185\u0001\u0000\u0000\u0000\u0186\u0187\u0001\u0000\u0000\u0000\u0187"+
		"\u0199\u0001\u0000\u0000\u0000\u0188\u018a\u0005\u0016\u0000\u0000\u0189"+
		"\u018b\u0003\"\u0011\u0000\u018a\u0189\u0001\u0000\u0000\u0000\u018a\u018b"+
		"\u0001\u0000\u0000\u0000\u018b\u018c\u0001\u0000\u0000\u0000\u018c\u0192"+
		"\u0003>\u001f\u0000\u018d\u018f\u0005\u0018\u0000\u0000\u018e\u0190\u0003"+
		"\"\u0011\u0000\u018f\u018e\u0001\u0000\u0000\u0000\u018f\u0190\u0001\u0000"+
		"\u0000\u0000\u0190\u0191\u0001\u0000\u0000\u0000\u0191\u0193\u0003L&\u0000"+
		"\u0192\u018d\u0001\u0000\u0000\u0000\u0192\u0193\u0001\u0000\u0000\u0000"+
		"\u0193\u0194\u0001\u0000\u0000\u0000\u0194\u0196\u0005\u0011\u0000\u0000"+
		"\u0195\u0197\u0003\"\u0011\u0000\u0196\u0195\u0001\u0000\u0000\u0000\u0196"+
		"\u0197\u0001\u0000\u0000\u0000\u0197\u0199\u0001\u0000\u0000\u0000\u0198"+
		"\u017f\u0001\u0000\u0000\u0000\u0198\u0188\u0001\u0000\u0000\u0000\u0199"+
		";\u0001\u0000\u0000\u0000\u019a\u019b\u0005\u0001\u0000\u0000\u019b\u019c"+
		"\u0005q\u0000\u0000\u019c\u019d\u0003\n\u0005\u0000\u019d\u019e\u0005"+
		"\u0002\u0000\u0000\u019e=\u0001\u0000\u0000\u0000\u019f\u01a1\u0003<\u001e"+
		"\u0000\u01a0\u019f\u0001\u0000\u0000\u0000\u01a0\u01a1\u0001\u0000\u0000"+
		"\u0000\u01a1\u01a2\u0001\u0000\u0000\u0000\u01a2\u01a3\u0003L&\u0000\u01a3"+
		"?\u0001\u0000\u0000\u0000\u01a4\u01a5\u0005\u0001\u0000\u0000\u01a5\u01a6"+
		"\u0003B!\u0000\u01a6\u01a7\u0005\u0002\u0000\u0000\u01a7A\u0001\u0000"+
		"\u0000\u0000\u01a8\u01ac\u0003&\u0013\u0000\u01a9\u01ab\u0003@ \u0000"+
		"\u01aa\u01a9\u0001\u0000\u0000\u0000\u01ab\u01ae\u0001\u0000\u0000\u0000"+
		"\u01ac\u01aa\u0001\u0000\u0000\u0000\u01ac\u01ad\u0001\u0000\u0000\u0000"+
		"\u01ad\u01c1\u0001\u0000\u0000\u0000\u01ae\u01ac\u0001\u0000\u0000\u0000"+
		"\u01af\u01b0\u0005\u001b\u0000\u0000\u01b0\u01c1\u0003D\"\u0000\u01b1"+
		"\u01b3\u0005\u000f\u0000\u0000\u01b2\u01b4\u0003\"\u0011\u0000\u01b3\u01b2"+
		"\u0001\u0000\u0000\u0000\u01b3\u01b4\u0001\u0000\u0000\u0000\u01b4\u01b5"+
		"\u0001\u0000\u0000\u0000\u01b5\u01c1\u0003>\u001f\u0000\u01b6\u01b8\u0005"+
		"\u0010\u0000\u0000\u01b7\u01b9\u0003\"\u0011\u0000\u01b8\u01b7\u0001\u0000"+
		"\u0000\u0000\u01b8\u01b9\u0001\u0000\u0000\u0000\u01b9\u01ba\u0001\u0000"+
		"\u0000\u0000\u01ba\u01c1\u0003>\u001f\u0000\u01bb\u01bd\u0005\u0016\u0000"+
		"\u0000\u01bc\u01be\u0003\"\u0011\u0000\u01bd\u01bc\u0001\u0000\u0000\u0000"+
		"\u01bd\u01be\u0001\u0000\u0000\u0000\u01be\u01bf\u0001\u0000\u0000\u0000"+
		"\u01bf\u01c1\u0003J%\u0000\u01c0\u01a8\u0001\u0000\u0000\u0000\u01c0\u01af"+
		"\u0001\u0000\u0000\u0000\u01c0\u01b1\u0001\u0000\u0000\u0000\u01c0\u01b6"+
		"\u0001\u0000\u0000\u0000\u01c0\u01bb\u0001\u0000\u0000\u0000\u01c1C\u0001"+
		"\u0000\u0000\u0000\u01c2\u01c4\u0003\u001c\u000e\u0000\u01c3\u01c2\u0001"+
		"\u0000\u0000\u0000\u01c3\u01c4\u0001\u0000\u0000\u0000\u01c4\u01c5\u0001"+
		"\u0000\u0000\u0000\u01c5\u01c6\u0003F#\u0000\u01c6E\u0001\u0000\u0000"+
		"\u0000\u01c7\u01c8\u0005\u0001\u0000\u0000\u01c8\u01cc\u0005p\u0000\u0000"+
		"\u01c9\u01cb\u0003\n\u0005\u0000\u01ca\u01c9\u0001\u0000\u0000\u0000\u01cb"+
		"\u01ce\u0001\u0000\u0000\u0000\u01cc\u01ca\u0001\u0000\u0000\u0000\u01cc"+
		"\u01cd\u0001\u0000\u0000\u0000\u01cd\u01cf\u0001\u0000\u0000\u0000\u01ce"+
		"\u01cc\u0001\u0000\u0000\u0000\u01cf\u01d1\u0005\u0002\u0000\u0000\u01d0"+
		"\u01c7\u0001\u0000\u0000\u0000\u01d1\u01d4\u0001\u0000\u0000\u0000\u01d2"+
		"\u01d0\u0001\u0000\u0000\u0000\u01d2\u01d3\u0001\u0000\u0000\u0000\u01d3"+
		"\u01d5\u0001\u0000\u0000\u0000\u01d4\u01d2\u0001\u0000\u0000\u0000\u01d5"+
		"\u01d6\u0003H$\u0000\u01d6G\u0001\u0000\u0000\u0000\u01d7\u01d8\u0005"+
		"\u0001\u0000\u0000\u01d8\u01dc\u0005q\u0000\u0000\u01d9\u01db\u0003\n"+
		"\u0005\u0000\u01da\u01d9\u0001\u0000\u0000\u0000\u01db\u01de\u0001\u0000"+
		"\u0000\u0000\u01dc\u01da\u0001\u0000\u0000\u0000\u01dc\u01dd\u0001\u0000"+
		"\u0000\u0000\u01dd\u01df\u0001\u0000\u0000\u0000\u01de\u01dc\u0001\u0000"+
		"\u0000\u0000\u01df\u01e1\u0005\u0002\u0000\u0000\u01e0\u01d7\u0001\u0000"+
		"\u0000\u0000\u01e1\u01e4\u0001\u0000\u0000\u0000\u01e2\u01e0\u0001\u0000"+
		"\u0000\u0000\u01e2\u01e3\u0001\u0000\u0000\u0000\u01e3\u01e8\u0001\u0000"+
		"\u0000\u0000\u01e4\u01e2\u0001\u0000\u0000\u0000\u01e5\u01e7\u0003@ \u0000"+
		"\u01e6\u01e5\u0001\u0000\u0000\u0000\u01e7\u01ea\u0001\u0000\u0000\u0000"+
		"\u01e8\u01e6\u0001\u0000\u0000\u0000\u01e8\u01e9\u0001\u0000\u0000\u0000"+
		"\u01e9I\u0001\u0000\u0000\u0000\u01ea\u01e8\u0001\u0000\u0000\u0000\u01eb"+
		"\u01ec\u0003<\u001e\u0000\u01ec\u01ed\u0003J%\u0000\u01ed\u0200\u0001"+
		"\u0000\u0000\u0000\u01ee\u01f0\u0003@ \u0000\u01ef\u01ee\u0001\u0000\u0000"+
		"\u0000\u01f0\u01f3\u0001\u0000\u0000\u0000\u01f1\u01ef\u0001\u0000\u0000"+
		"\u0000\u01f1\u01f2\u0001\u0000\u0000\u0000\u01f2\u01f4\u0001\u0000\u0000"+
		"\u0000\u01f3\u01f1\u0001\u0000\u0000\u0000\u01f4\u01f5\u0005\u0001\u0000"+
		"\u0000\u01f5\u01f6\u0005\u0017\u0000\u0000\u01f6\u01f7\u0003L&\u0000\u01f7"+
		"\u01fd\u0005\u0002\u0000\u0000\u01f8\u01f9\u0005\u0001\u0000\u0000\u01f9"+
		"\u01fa\u0005\u0018\u0000\u0000\u01fa\u01fb\u0003L&\u0000\u01fb\u01fc\u0005"+
		"\u0002\u0000\u0000\u01fc\u01fe\u0001\u0000\u0000\u0000\u01fd\u01f8\u0001"+
		"\u0000\u0000\u0000\u01fd\u01fe\u0001\u0000\u0000\u0000\u01fe\u0200\u0001"+
		"\u0000\u0000\u0000\u01ff\u01eb\u0001\u0000\u0000\u0000\u01ff\u01f1\u0001"+
		"\u0000\u0000\u0000\u0200K\u0001\u0000\u0000\u0000\u0201\u0203\u0003$\u0012"+
		"\u0000\u0202\u0201\u0001\u0000\u0000\u0000\u0203\u0206\u0001\u0000\u0000"+
		"\u0000\u0204\u0202\u0001\u0000\u0000\u0000\u0204\u0205\u0001\u0000\u0000"+
		"\u0000\u0205\u0208\u0001\u0000\u0000\u0000\u0206\u0204\u0001\u0000\u0000"+
		"\u0000\u0207\u0209\u00030\u0018\u0000\u0208\u0207\u0001\u0000\u0000\u0000"+
		"\u0208\u0209\u0001\u0000\u0000\u0000\u0209M\u0001\u0000\u0000\u0000\u020a"+
		"\u020b\u0003L&\u0000\u020bO\u0001\u0000\u0000\u0000\u020c\u020d\u0005"+
		"\u0001\u0000\u0000\u020d\u020f\u0005m\u0000\u0000\u020e\u0210\u0003\""+
		"\u0011\u0000\u020f\u020e\u0001\u0000\u0000\u0000\u020f\u0210\u0001\u0000"+
		"\u0000\u0000\u0210\u0211\u0001\u0000\u0000\u0000\u0211\u0212\u0003R)\u0000"+
		"\u0212\u0213\u0005\u0002\u0000\u0000\u0213Q\u0001\u0000\u0000\u0000\u0214"+
		"\u0216\u0003\u001c\u000e\u0000\u0215\u0214\u0001\u0000\u0000\u0000\u0215"+
		"\u0216\u0001\u0000\u0000\u0000\u0216\u0217\u0001\u0000\u0000\u0000\u0217"+
		"\u0222\u0003T*\u0000\u0218\u021a\u0003n7\u0000\u0219\u021b\u0003\u001c"+
		"\u000e\u0000\u021a\u0219\u0001\u0000\u0000\u0000\u021a\u021b\u0001\u0000"+
		"\u0000\u0000\u021b\u021c\u0001\u0000\u0000\u0000\u021c\u021d\u0003\u0016"+
		"\u000b\u0000\u021d\u0222\u0001\u0000\u0000\u0000\u021e\u021f\u0003t:\u0000"+
		"\u021f\u0220\u0003R)\u0000\u0220\u0222\u0001\u0000\u0000\u0000\u0221\u0215"+
		"\u0001\u0000\u0000\u0000\u0221\u0218\u0001\u0000\u0000\u0000\u0221\u021e"+
		"\u0001\u0000\u0000\u0000\u0222S\u0001\u0000\u0000\u0000\u0223\u0224\u0003"+
		"\u0016\u000b\u0000\u0224\u0225\u0003V+\u0000\u0225U\u0001\u0000\u0000"+
		"\u0000\u0226\u0227\u0005\u0001\u0000\u0000\u0227\u0231\u0005r\u0000\u0000"+
		"\u0228\u022a\u0003\n\u0005\u0000\u0229\u0228\u0001\u0000\u0000\u0000\u022a"+
		"\u022d\u0001\u0000\u0000\u0000\u022b\u0229\u0001\u0000\u0000\u0000\u022b"+
		"\u022c\u0001\u0000\u0000\u0000\u022c\u0232\u0001\u0000\u0000\u0000\u022d"+
		"\u022b\u0001\u0000\u0000\u0000\u022e\u022f\u0003\"\u0011\u0000\u022f\u0230"+
		"\u0003\n\u0005\u0000\u0230\u0232\u0001\u0000\u0000\u0000\u0231\u022b\u0001"+
		"\u0000\u0000\u0000\u0231\u022e\u0001\u0000\u0000\u0000\u0232\u0233\u0001"+
		"\u0000\u0000\u0000\u0233\u0235\u0005\u0002\u0000\u0000\u0234\u0226\u0001"+
		"\u0000\u0000\u0000\u0235\u0238\u0001\u0000\u0000\u0000\u0236\u0234\u0001"+
		"\u0000\u0000\u0000\u0236\u0237\u0001\u0000\u0000\u0000\u0237\u0239\u0001"+
		"\u0000\u0000\u0000\u0238\u0236\u0001\u0000\u0000\u0000\u0239\u023a\u0003"+
		"L&\u0000\u023aW\u0001\u0000\u0000\u0000\u023b\u023c\u0005\u0001\u0000"+
		"\u0000\u023c\u023d\u0005x\u0000\u0000\u023d\u023e\u0003N\'\u0000\u023e"+
		"\u023f\u0005\u0002\u0000\u0000\u023f\u0242\u0001\u0000\u0000\u0000\u0240"+
		"\u0242\u0003@ \u0000\u0241\u023b\u0001\u0000\u0000\u0000\u0241\u0240\u0001"+
		"\u0000\u0000\u0000\u0242Y\u0001\u0000\u0000\u0000\u0243\u0244\u0005\u0001"+
		"\u0000\u0000\u0244\u0246\u0005v\u0000\u0000\u0245\u0247\u0003 \u0010\u0000"+
		"\u0246\u0245\u0001\u0000\u0000\u0000\u0246\u0247\u0001\u0000\u0000\u0000"+
		"\u0247\u0248\u0001\u0000\u0000\u0000\u0248\u024c\u0003X,\u0000\u0249\u024b"+
		"\u0003 \u0010\u0000\u024a\u0249\u0001\u0000\u0000\u0000\u024b\u024e\u0001"+
		"\u0000\u0000\u0000\u024c\u024a\u0001\u0000\u0000\u0000\u024c\u024d\u0001"+
		"\u0000\u0000\u0000\u024d\u024f\u0001\u0000\u0000\u0000\u024e\u024c\u0001"+
		"\u0000\u0000\u0000\u024f\u0250\u0005\u0002\u0000\u0000\u0250[\u0001\u0000"+
		"\u0000\u0000\u0251\u0252\u0005\u0001\u0000\u0000\u0252\u0254\u0005t\u0000"+
		"\u0000\u0253\u0255\u0003\"\u0011\u0000\u0254\u0253\u0001\u0000\u0000\u0000"+
		"\u0254\u0255\u0001\u0000\u0000\u0000\u0255\u0256\u0001\u0000\u0000\u0000"+
		"\u0256\u0257\u0003^/\u0000\u0257\u0258\u0005\u0002\u0000\u0000\u0258]"+
		"\u0001\u0000\u0000\u0000\u0259\u026c\u0003\u0018\f\u0000\u025a\u025b\u0003"+
		"n7\u0000\u025b\u025c\u0003\u0018\f\u0000\u025c\u026c\u0001\u0000\u0000"+
		"\u0000\u025d\u025e\u0003t:\u0000\u025e\u025f\u0003^/\u0000\u025f\u026c"+
		"\u0001\u0000\u0000\u0000\u0260\u0261\u0003\u0006\u0003\u0000\u0261\u0262"+
		"\u0005\u0001\u0000\u0000\u0262\u0266\u0005v\u0000\u0000\u0263\u0265\u0003"+
		" \u0010\u0000\u0264\u0263\u0001\u0000\u0000\u0000\u0265\u0268\u0001\u0000"+
		"\u0000\u0000\u0266\u0264\u0001\u0000\u0000\u0000\u0266\u0267\u0001\u0000"+
		"\u0000\u0000\u0267\u0269\u0001\u0000\u0000\u0000\u0268\u0266\u0001\u0000"+
		"\u0000\u0000\u0269\u026a\u0005\u0002\u0000\u0000\u026a\u026c\u0001\u0000"+
		"\u0000\u0000\u026b\u0259\u0001\u0000\u0000\u0000\u026b\u025a\u0001\u0000"+
		"\u0000\u0000\u026b\u025d\u0001\u0000\u0000\u0000\u026b\u0260\u0001\u0000"+
		"\u0000\u0000\u026c_\u0001\u0000\u0000\u0000\u026d\u026e\u0005\u0001\u0000"+
		"\u0000\u026e\u0270\u0005w\u0000\u0000\u026f\u0271\u0003 \u0010\u0000\u0270"+
		"\u026f\u0001\u0000\u0000\u0000\u0270\u0271\u0001\u0000\u0000\u0000\u0271"+
		"\u0272\u0001\u0000\u0000\u0000\u0272\u0276\u0003X,\u0000\u0273\u0275\u0005"+
		"\u0006\u0000\u0000\u0274\u0273\u0001\u0000\u0000\u0000\u0275\u0278\u0001"+
		"\u0000\u0000\u0000\u0276\u0274\u0001\u0000\u0000\u0000\u0276\u0277\u0001"+
		"\u0000\u0000\u0000\u0277\u0279\u0001\u0000\u0000\u0000\u0278\u0276\u0001"+
		"\u0000\u0000\u0000\u0279\u027a\u0005\u0002\u0000\u0000\u027aa\u0001\u0000"+
		"\u0000\u0000\u027b\u027c\u0005\u0001\u0000\u0000\u027c\u027e\u0005u\u0000"+
		"\u0000\u027d\u027f\u0003\"\u0011\u0000\u027e\u027d\u0001\u0000\u0000\u0000"+
		"\u027e\u027f\u0001\u0000\u0000\u0000\u027f\u0280\u0001\u0000\u0000\u0000"+
		"\u0280\u0281\u0003d2\u0000\u0281\u0282\u0005\u0002\u0000\u0000\u0282c"+
		"\u0001\u0000\u0000\u0000\u0283\u0294\u0003\u001a\r\u0000\u0284\u0285\u0003"+
		"n7\u0000\u0285\u0286\u0003\u001a\r\u0000\u0286\u0294\u0001\u0000\u0000"+
		"\u0000\u0287\u0288\u0003t:\u0000\u0288\u0289\u0003d2\u0000\u0289\u0294"+
		"\u0001\u0000\u0000\u0000\u028a\u028b\u0005\u0001\u0000\u0000\u028b\u028f"+
		"\u0005w\u0000\u0000\u028c\u028e\u0005\u0006\u0000\u0000\u028d\u028c\u0001"+
		"\u0000\u0000\u0000\u028e\u0291\u0001\u0000\u0000\u0000\u028f\u028d\u0001"+
		"\u0000\u0000\u0000\u028f\u0290\u0001\u0000\u0000\u0000\u0290\u0292\u0001"+
		"\u0000\u0000\u0000\u0291\u028f\u0001\u0000\u0000\u0000\u0292\u0294\u0005"+
		"\u0002\u0000\u0000\u0293\u0283\u0001\u0000\u0000\u0000\u0293\u0284\u0001"+
		"\u0000\u0000\u0000\u0293\u0287\u0001\u0000\u0000\u0000\u0293\u028a\u0001"+
		"\u0000\u0000\u0000\u0294e\u0001\u0000\u0000\u0000\u0295\u0296\u0005\u0001"+
		"\u0000\u0000\u0296\u0298\u0005s\u0000\u0000\u0297\u0299\u0003\"\u0011"+
		"\u0000\u0298\u0297\u0001\u0000\u0000\u0000\u0298\u0299\u0001\u0000\u0000"+
		"\u0000\u0299\u029a\u0001\u0000\u0000\u0000\u029a\u029b\u0003h4\u0000\u029b"+
		"\u029c\u0005\u0002\u0000\u0000\u029cg\u0001\u0000\u0000\u0000\u029d\u029e"+
		"\u0003\u000e\u0007\u0000\u029e\u029f\u0003N\'\u0000\u029f\u02a7\u0001"+
		"\u0000\u0000\u0000\u02a0\u02a1\u0003n7\u0000\u02a1\u02a2\u0003\u000e\u0007"+
		"\u0000\u02a2\u02a7\u0001\u0000\u0000\u0000\u02a3\u02a4\u0003t:\u0000\u02a4"+
		"\u02a5\u0003h4\u0000\u02a5\u02a7\u0001\u0000\u0000\u0000\u02a6\u029d\u0001"+
		"\u0000\u0000\u0000\u02a6\u02a0\u0001\u0000\u0000\u0000\u02a6\u02a3\u0001"+
		"\u0000\u0000\u0000\u02a7i\u0001\u0000\u0000\u0000\u02a8\u02a9\u0005\u0001"+
		"\u0000\u0000\u02a9\u02ab\u0005m\u0000\u0000\u02aa\u02ac\u0003\"\u0011"+
		"\u0000\u02ab\u02aa\u0001\u0000\u0000\u0000\u02ab\u02ac\u0001\u0000\u0000"+
		"\u0000\u02ac\u02ad\u0001\u0000\u0000\u0000\u02ad\u02ae\u0003\u001c\u000e"+
		"\u0000\u02ae\u02af\u0005\u0002\u0000\u0000\u02af\u02d1\u0001\u0000\u0000"+
		"\u0000\u02b0\u02b1\u0005\u0001\u0000\u0000\u02b1\u02b3\u0005m\u0000\u0000"+
		"\u02b2\u02b4\u0003\"\u0011\u0000\u02b3\u02b2\u0001\u0000\u0000\u0000\u02b3"+
		"\u02b4\u0001\u0000\u0000\u0000\u02b4\u02b5\u0001\u0000\u0000\u0000\u02b5"+
		"\u02b6\u0003\u0016\u000b\u0000\u02b6\u02b7\u0005\u0002\u0000\u0000\u02b7"+
		"\u02d1\u0001\u0000\u0000\u0000\u02b8\u02b9\u0005\u0001\u0000\u0000\u02b9"+
		"\u02bb\u0005t\u0000\u0000\u02ba\u02bc\u0003\"\u0011\u0000\u02bb\u02ba"+
		"\u0001\u0000\u0000\u0000\u02bb\u02bc\u0001\u0000\u0000\u0000\u02bc\u02bd"+
		"\u0001\u0000\u0000\u0000\u02bd\u02be\u0003\u0018\f\u0000\u02be\u02bf\u0005"+
		"\u0002\u0000\u0000\u02bf\u02d1\u0001\u0000\u0000\u0000\u02c0\u02c1\u0005"+
		"\u0001\u0000\u0000\u02c1\u02c3\u0005u\u0000\u0000\u02c2\u02c4\u0003\""+
		"\u0011\u0000\u02c3\u02c2\u0001\u0000\u0000\u0000\u02c3\u02c4\u0001\u0000"+
		"\u0000\u0000\u02c4\u02c5\u0001\u0000\u0000\u0000\u02c5\u02c6\u0003\u001a"+
		"\r\u0000\u02c6\u02c7\u0005\u0002\u0000\u0000\u02c7\u02d1\u0001\u0000\u0000"+
		"\u0000\u02c8\u02c9\u0005\u0001\u0000\u0000\u02c9\u02cb\u0005s\u0000\u0000"+
		"\u02ca\u02cc\u0003\"\u0011\u0000\u02cb\u02ca\u0001\u0000\u0000\u0000\u02cb"+
		"\u02cc\u0001\u0000\u0000\u0000\u02cc\u02cd\u0001\u0000\u0000\u0000\u02cd"+
		"\u02ce\u0003\u000e\u0007\u0000\u02ce\u02cf\u0005\u0002\u0000\u0000\u02cf"+
		"\u02d1\u0001\u0000\u0000\u0000\u02d0\u02a8\u0001\u0000\u0000\u0000\u02d0"+
		"\u02b0\u0001\u0000\u0000\u0000\u02d0\u02b8\u0001\u0000\u0000\u0000\u02d0"+
		"\u02c0\u0001\u0000\u0000\u0000\u02d0\u02c8\u0001\u0000\u0000\u0000\u02d1"+
		"k\u0001\u0000\u0000\u0000\u02d2\u02d3\u0005\u0001\u0000\u0000\u02d3\u02d4"+
		"\u0005y\u0000\u0000\u02d4\u02d5\u0003\u0002\u0001\u0000\u02d5\u02d6\u0003"+
		"\u0002\u0001\u0000\u02d6\u02d7\u0003j5\u0000\u02d7\u02d8\u0005\u0002\u0000"+
		"\u0000\u02d8m\u0001\u0000\u0000\u0000\u02d9\u02da\u0005\u0001\u0000\u0000"+
		"\u02da\u02db\u0005y\u0000\u0000\u02db\u02dc\u0003\u0002\u0001\u0000\u02dc"+
		"\u02dd\u0003\u0002\u0001\u0000\u02dd\u02de\u0005\u0002\u0000\u0000\u02de"+
		"o\u0001\u0000\u0000\u0000\u02df\u02e0\u0005\u0001\u0000\u0000\u02e0\u02e1"+
		"\u0005m\u0000\u0000\u02e1\u02e2\u0003 \u0010\u0000\u02e2\u02e3\u0005\u0002"+
		"\u0000\u0000\u02e3\u02f4\u0001\u0000\u0000\u0000\u02e4\u02e5\u0005\u0001"+
		"\u0000\u0000\u02e5\u02e6\u0005t\u0000\u0000\u02e6\u02e7\u0003 \u0010\u0000"+
		"\u02e7\u02e8\u0005\u0002\u0000\u0000\u02e8\u02f4\u0001\u0000\u0000\u0000"+
		"\u02e9\u02ea\u0005\u0001\u0000\u0000\u02ea\u02eb\u0005u\u0000\u0000\u02eb"+
		"\u02ec\u0003 \u0010\u0000\u02ec\u02ed\u0005\u0002\u0000\u0000\u02ed\u02f4"+
		"\u0001\u0000\u0000\u0000\u02ee\u02ef\u0005\u0001\u0000\u0000\u02ef\u02f0"+
		"\u0005s\u0000\u0000\u02f0\u02f1\u0003 \u0010\u0000\u02f1\u02f2\u0005\u0002"+
		"\u0000\u0000\u02f2\u02f4\u0001\u0000\u0000\u0000\u02f3\u02df\u0001\u0000"+
		"\u0000\u0000\u02f3\u02e4\u0001\u0000\u0000\u0000\u02f3\u02e9\u0001\u0000"+
		"\u0000\u0000\u02f3\u02ee\u0001\u0000\u0000\u0000\u02f4q\u0001\u0000\u0000"+
		"\u0000\u02f5\u02f6\u0005\u0001\u0000\u0000\u02f6\u02f7\u0005z\u0000\u0000"+
		"\u02f7\u02f8\u0003\u0002\u0001\u0000\u02f8\u02f9\u0003p8\u0000\u02f9\u02fa"+
		"\u0005\u0002\u0000\u0000\u02fas\u0001\u0000\u0000\u0000\u02fb\u02fc\u0005"+
		"\u0001\u0000\u0000\u02fc\u02fd\u0005z\u0000\u0000\u02fd\u02fe\u0003\u0002"+
		"\u0001\u0000\u02fe\u02ff\u0005\u0002\u0000\u0000\u02ffu\u0001\u0000\u0000"+
		"\u0000\u0300\u0301\u0005\u0001\u0000\u0000\u0301\u0303\u0005l\u0000\u0000"+
		"\u0302\u0304\u0003\"\u0011\u0000\u0303\u0302\u0001\u0000\u0000\u0000\u0303"+
		"\u0304\u0001\u0000\u0000\u0000\u0304\u0305\u0001\u0000\u0000\u0000\u0305"+
		"\u0306\u0003\u0010\b\u0000\u0306\u0307\u0005\u0002\u0000\u0000\u0307w"+
		"\u0001\u0000\u0000\u0000\u0308\u0309\u0005\u0001\u0000\u0000\u0309\u030a"+
		"\u0005o\u0000\u0000\u030a\u030b\u0003 \u0010\u0000\u030b\u030c\u0005\u0002"+
		"\u0000\u0000\u030cy\u0001\u0000\u0000\u0000\u030d\u0318\u0003v;\u0000"+
		"\u030e\u0318\u0003f3\u0000\u030f\u0318\u0003\\.\u0000\u0310\u0318\u0003"+
		"b1\u0000\u0311\u0318\u0003P(\u0000\u0312\u0318\u0003Z-\u0000\u0313\u0318"+
		"\u0003`0\u0000\u0314\u0318\u0003x<\u0000\u0315\u0318\u0003l6\u0000\u0316"+
		"\u0318\u0003r9\u0000\u0317\u030d\u0001\u0000\u0000\u0000\u0317\u030e\u0001"+
		"\u0000\u0000\u0000\u0317\u030f\u0001\u0000\u0000\u0000\u0317\u0310\u0001"+
		"\u0000\u0000\u0000\u0317\u0311\u0001\u0000\u0000\u0000\u0317\u0312\u0001"+
		"\u0000\u0000\u0000\u0317\u0313\u0001\u0000\u0000\u0000\u0317\u0314\u0001"+
		"\u0000\u0000\u0000\u0317\u0315\u0001\u0000\u0000\u0000\u0317\u0316\u0001"+
		"\u0000\u0000\u0000\u0318{\u0001\u0000\u0000\u0000\u0319\u031a\u0005\u0001"+
		"\u0000\u0000\u031a\u031c\u0005{\u0000\u0000\u031b\u031d\u0005\u008c\u0000"+
		"\u0000\u031c\u031b\u0001\u0000\u0000\u0000\u031c\u031d\u0001\u0000\u0000"+
		"\u0000\u031d\u0321\u0001\u0000\u0000\u0000\u031e\u0320\u0003z=\u0000\u031f"+
		"\u031e\u0001\u0000\u0000\u0000\u0320\u0323\u0001\u0000\u0000\u0000\u0321"+
		"\u031f\u0001\u0000\u0000\u0000\u0321\u0322\u0001\u0000\u0000\u0000\u0322"+
		"\u0324\u0001\u0000\u0000\u0000\u0323\u0321\u0001\u0000\u0000\u0000\u0324"+
		"\u0325\u0005\u0002\u0000\u0000\u0325}\u0001\u0000\u0000\u0000\u0326\u0335"+
		"\u0003|>\u0000\u0327\u0328\u0005\u0001\u0000\u0000\u0328\u032a\u0005{"+
		"\u0000\u0000\u0329\u032b\u0005\u008c\u0000\u0000\u032a\u0329\u0001\u0000"+
		"\u0000\u0000\u032a\u032b\u0001\u0000\u0000\u0000\u032b\u032c\u0001\u0000"+
		"\u0000\u0000\u032c\u0330\u0007\u0006\u0000\u0000\u032d\u032f\u0005\u0006"+
		"\u0000\u0000\u032e\u032d\u0001\u0000\u0000\u0000\u032f\u0332\u0001\u0000"+
		"\u0000\u0000\u0330\u032e\u0001\u0000\u0000\u0000\u0330\u0331\u0001\u0000"+
		"\u0000\u0000\u0331\u0333\u0001\u0000\u0000\u0000\u0332\u0330\u0001\u0000"+
		"\u0000\u0000\u0333\u0335\u0005\u0002\u0000\u0000\u0334\u0326\u0001\u0000"+
		"\u0000\u0000\u0334\u0327\u0001\u0000\u0000\u0000\u0335\u007f\u0001\u0000"+
		"\u0000\u0000\u0336\u0337\u0005\u0001\u0000\u0000\u0337\u0339\u0005\u0080"+
		"\u0000\u0000\u0338\u033a\u0005\u008c\u0000\u0000\u0339\u0338\u0001\u0000"+
		"\u0000\u0000\u0339\u033a\u0001\u0000\u0000\u0000\u033a\u033b\u0001\u0000"+
		"\u0000\u0000\u033b\u033c\u0003\u0002\u0001\u0000\u033c\u033d\u0003\u008a"+
		"E\u0000\u033d\u033e\u0005\u0002\u0000\u0000\u033e\u0348\u0001\u0000\u0000"+
		"\u0000\u033f\u0340\u0005\u0001\u0000\u0000\u0340\u0342\u0005\u0081\u0000"+
		"\u0000\u0341\u0343\u0005\u008c\u0000\u0000\u0342\u0341\u0001\u0000\u0000"+
		"\u0000\u0342\u0343\u0001\u0000\u0000\u0000\u0343\u0344\u0001\u0000\u0000"+
		"\u0000\u0344\u0345\u0003\u0002\u0001\u0000\u0345\u0346\u0005\u0002\u0000"+
		"\u0000\u0346\u0348\u0001\u0000\u0000\u0000\u0347\u0336\u0001\u0000\u0000"+
		"\u0000\u0347\u033f\u0001\u0000\u0000\u0000\u0348\u0081\u0001\u0000\u0000"+
		"\u0000\u0349\u034a\u0005\u0001\u0000\u0000\u034a\u034b\u0005\u0082\u0000"+
		"\u0000\u034b\u034c\u0003~?\u0000\u034c\u034d\u0005\u0006\u0000\u0000\u034d"+
		"\u034e\u0005\u0002\u0000\u0000\u034e\u037e\u0001\u0000\u0000\u0000\u034f"+
		"\u0350\u0005\u0001\u0000\u0000\u0350\u0351\u0005\u0083\u0000\u0000\u0351"+
		"\u0352\u0003~?\u0000\u0352\u0353\u0005\u0006\u0000\u0000\u0353\u0354\u0005"+
		"\u0002\u0000\u0000\u0354\u037e\u0001\u0000\u0000\u0000\u0355\u0356\u0005"+
		"\u0001\u0000\u0000\u0356\u0357\u0005\u0084\u0000\u0000\u0357\u0358\u0003"+
		"~?\u0000\u0358\u0359\u0005\u0006\u0000\u0000\u0359\u035a\u0005\u0002\u0000"+
		"\u0000\u035a\u037e\u0001\u0000\u0000\u0000\u035b\u035c\u0005\u0001\u0000"+
		"\u0000\u035c\u035d\u0005\u0088\u0000\u0000\u035d\u035e\u0003~?\u0000\u035e"+
		"\u035f\u0005\u0006\u0000\u0000\u035f\u0360\u0005\u0002\u0000\u0000\u0360"+
		"\u037e\u0001\u0000\u0000\u0000\u0361\u0362\u0005\u0001\u0000\u0000\u0362"+
		"\u0363\u0005\u0085\u0000\u0000\u0363\u0364\u0003\u0080@\u0000\u0364\u0365"+
		"\u0003\u008aE\u0000\u0365\u0366\u0005\u0002\u0000\u0000\u0366\u037e\u0001"+
		"\u0000\u0000\u0000\u0367\u0368\u0005\u0001\u0000\u0000\u0368\u0369\u0005"+
		"\u0086\u0000\u0000\u0369\u036a\u0003\u0080@\u0000\u036a\u036b\u0005\u0002"+
		"\u0000\u0000\u036b\u037e\u0001\u0000\u0000\u0000\u036c\u036d\u0005\u0001"+
		"\u0000\u0000\u036d\u036e\u0005\u0087\u0000\u0000\u036e\u036f\u0003\u0080"+
		"@\u0000\u036f\u0370\u0005\u0002\u0000\u0000\u0370\u037e\u0001\u0000\u0000"+
		"\u0000\u0371\u0372\u0005\u0001\u0000\u0000\u0372\u0373\u0005\u0088\u0000"+
		"\u0000\u0373\u0374\u0003\u0080@\u0000\u0374\u0375\u0005\u0006\u0000\u0000"+
		"\u0375\u0376\u0005\u0002\u0000\u0000\u0376\u037e\u0001\u0000\u0000\u0000"+
		"\u0377\u0378\u0005\u0001\u0000\u0000\u0378\u0379\u0005\u0089\u0000\u0000"+
		"\u0379\u037a\u0003\u0080@\u0000\u037a\u037b\u0005\u0006\u0000\u0000\u037b"+
		"\u037c\u0005\u0002\u0000\u0000\u037c\u037e\u0001\u0000\u0000\u0000\u037d"+
		"\u0349\u0001\u0000\u0000\u0000\u037d\u034f\u0001\u0000\u0000\u0000\u037d"+
		"\u0355\u0001\u0000\u0000\u0000\u037d\u035b\u0001\u0000\u0000\u0000\u037d"+
		"\u0361\u0001\u0000\u0000\u0000\u037d\u0367\u0001\u0000\u0000\u0000\u037d"+
		"\u036c\u0001\u0000\u0000\u0000\u037d\u0371\u0001\u0000\u0000\u0000\u037d"+
		"\u0377\u0001\u0000\u0000\u0000\u037e\u0083\u0001\u0000\u0000\u0000\u037f"+
		"\u038c\u0003\u0080@\u0000\u0380\u038c\u0003\u0082A\u0000\u0381\u038c\u0003"+
		"~?\u0000\u0382\u0383\u0005\u0001\u0000\u0000\u0383\u0384\u0005\u007f\u0000"+
		"\u0000\u0384\u0386\u0003\u0002\u0001\u0000\u0385\u0387\u0005\u008c\u0000"+
		"\u0000\u0386\u0385\u0001\u0000\u0000\u0000\u0386\u0387\u0001\u0000\u0000"+
		"\u0000\u0387\u0388\u0001\u0000\u0000\u0000\u0388\u0389\u0005\u0002\u0000"+
		"\u0000\u0389\u038c\u0001\u0000\u0000\u0000\u038a\u038c\u0003\u0086C\u0000"+
		"\u038b\u037f\u0001\u0000\u0000\u0000\u038b\u0380\u0001\u0000\u0000\u0000"+
		"\u038b\u0381\u0001\u0000\u0000\u0000\u038b\u0382\u0001\u0000\u0000\u0000"+
		"\u038b\u038a\u0001\u0000\u0000\u0000\u038c\u0085\u0001\u0000\u0000\u0000"+
		"\u038d\u038e\u0005\u0001\u0000\u0000\u038e\u0390\u0005~\u0000\u0000\u038f"+
		"\u0391\u0005\u008c\u0000\u0000\u0390\u038f\u0001\u0000\u0000\u0000\u0390"+
		"\u0391\u0001\u0000\u0000\u0000\u0391\u0395\u0001\u0000\u0000\u0000\u0392"+
		"\u0394\u0003\u0084B\u0000\u0393\u0392\u0001\u0000\u0000\u0000\u0394\u0397"+
		"\u0001\u0000\u0000\u0000\u0395\u0393\u0001\u0000\u0000\u0000\u0395\u0396"+
		"\u0001\u0000\u0000\u0000\u0396\u0398\u0001\u0000\u0000\u0000\u0397\u0395"+
		"\u0001\u0000\u0000\u0000\u0398\u03ae\u0005\u0002\u0000\u0000\u0399\u039a"+
		"\u0005\u0001\u0000\u0000\u039a\u039c\u0005\u008a\u0000\u0000\u039b\u039d"+
		"\u0005\u008c\u0000\u0000\u039c\u039b\u0001\u0000\u0000\u0000\u039c\u039d"+
		"\u0001\u0000\u0000\u0000\u039d\u039e\u0001\u0000\u0000\u0000\u039e\u039f"+
		"\u0005\u0006\u0000\u0000\u039f\u03ae\u0005\u0002\u0000\u0000\u03a0\u03a1"+
		"\u0005\u0001\u0000\u0000\u03a1\u03a3\u0005\u008b\u0000\u0000\u03a2\u03a4"+
		"\u0005\u008c\u0000\u0000\u03a3\u03a2\u0001\u0000\u0000\u0000\u03a3\u03a4"+
		"\u0001\u0000\u0000\u0000\u03a4\u03a5\u0001\u0000\u0000\u0000\u03a5\u03a6"+
		"\u0005\u0006\u0000\u0000\u03a6\u03ae\u0005\u0002\u0000\u0000\u03a7\u03a8"+
		"\u0005\u0001\u0000\u0000\u03a8\u03aa\u0005\u008b\u0000\u0000\u03a9\u03ab"+
		"\u0005\u008c\u0000\u0000\u03aa\u03a9\u0001\u0000\u0000\u0000\u03aa\u03ab"+
		"\u0001\u0000\u0000\u0000\u03ab\u03ac\u0001\u0000\u0000\u0000\u03ac\u03ae"+
		"\u0005\u0002\u0000\u0000\u03ad\u038d\u0001\u0000\u0000\u0000\u03ad\u0399"+
		"\u0001\u0000\u0000\u0000\u03ad\u03a0\u0001\u0000\u0000\u0000\u03ad\u03a7"+
		"\u0001\u0000\u0000\u0000\u03ae\u0087\u0001\u0000\u0000\u0000\u03af\u03b0"+
		"\u0005\u0001\u0000\u0000\u03b0\u03b1\u0005\b\u0000\u0000\u03b1\u03b2\u0003"+
		"\u001e\u000f\u0000\u03b2\u03b3\u0005\u0002\u0000\u0000\u03b3\u0089\u0001"+
		"\u0000\u0000\u0000\u03b4\u03b6\u0003\u0088D\u0000\u03b5\u03b4\u0001\u0000"+
		"\u0000\u0000\u03b6\u03b9\u0001\u0000\u0000\u0000\u03b7\u03b5\u0001\u0000"+
		"\u0000\u0000\u03b7\u03b8\u0001\u0000\u0000\u0000\u03b8\u008b\u0001\u0000"+
		"\u0000\u0000\u03b9\u03b7\u0001\u0000\u0000\u0000\u03ba\u03bc\u0003\u0084"+
		"B\u0000\u03bb\u03ba\u0001\u0000\u0000\u0000\u03bc\u03bf\u0001\u0000\u0000"+
		"\u0000\u03bd\u03bb\u0001\u0000\u0000\u0000\u03bd\u03be\u0001\u0000\u0000"+
		"\u0000\u03be\u03c0\u0001\u0000\u0000\u0000\u03bf\u03bd\u0001\u0000\u0000"+
		"\u0000\u03c0\u03c9\u0005\u0000\u0000\u0001\u03c1\u03c3\u0003z=\u0000\u03c2"+
		"\u03c1\u0001\u0000\u0000\u0000\u03c3\u03c4\u0001\u0000\u0000\u0000\u03c4"+
		"\u03c2\u0001\u0000\u0000\u0000\u03c4\u03c5\u0001\u0000\u0000\u0000\u03c5"+
		"\u03c6\u0001\u0000\u0000\u0000\u03c6\u03c7\u0005\u0000\u0000\u0001\u03c7"+
		"\u03c9\u0001\u0000\u0000\u0000\u03c8\u03bd\u0001\u0000\u0000\u0000\u03c8"+
		"\u03c2\u0001\u0000\u0000\u0000\u03c9\u008d\u0001\u0000\u0000\u0000\u03ca"+
		"\u03cb\u0003|>\u0000\u03cb\u03cc\u0005\u0000\u0000\u0001\u03cc\u03d5\u0001"+
		"\u0000\u0000\u0000\u03cd\u03cf\u0003z=\u0000\u03ce\u03cd\u0001\u0000\u0000"+
		"\u0000\u03cf\u03d2\u0001\u0000\u0000\u0000\u03d0\u03ce\u0001\u0000\u0000"+
		"\u0000\u03d0\u03d1\u0001\u0000\u0000\u0000\u03d1\u03d3\u0001\u0000\u0000"+
		"\u0000\u03d2\u03d0\u0001\u0000\u0000\u0000\u03d3\u03d5\u0005\u0000\u0000"+
		"\u0001\u03d4\u03ca\u0001\u0000\u0000\u0000\u03d4\u03d0\u0001\u0000\u0000"+
		"\u0000\u03d5\u008f\u0001\u0000\u0000\u0000h\u009d\u00a7\u00b3\u00b9\u00be"+
		"\u00c6\u00cc\u00d4\u00da\u00eb\u00f9\u010a\u010d\u0111\u0114\u0123\u0130"+
		"\u0135\u0139\u0142\u0148\u0150\u0156\u015b\u0164\u016a\u0174\u017a\u0181"+
		"\u0186\u018a\u018f\u0192\u0196\u0198\u01a0\u01ac\u01b3\u01b8\u01bd\u01c0"+
		"\u01c3\u01cc\u01d2\u01dc\u01e2\u01e8\u01f1\u01fd\u01ff\u0204\u0208\u020f"+
		"\u0215\u021a\u0221\u022b\u0231\u0236\u0241\u0246\u024c\u0254\u0266\u026b"+
		"\u0270\u0276\u027e\u028f\u0293\u0298\u02a6\u02ab\u02b3\u02bb\u02c3\u02cb"+
		"\u02d0\u02f3\u0303\u0317\u031c\u0321\u032a\u0330\u0334\u0339\u0342\u0347"+
		"\u037d\u0386\u038b\u0390\u0395\u039c\u03a3\u03aa\u03ad\u03b7\u03bd\u03c4"+
		"\u03c8\u03d0\u03d4";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}