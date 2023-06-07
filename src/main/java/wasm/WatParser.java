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
		OFFSET_EQ=36, ALIGN_EQ=37, SIGN_POSTFIX=38, MEM_SIZE=39, OP_EQZ=40, OP_EQ=41, 
		OP_NE=42, OP_LT=43, OP_LTS=44, OP_LTU=45, OP_LE=46, OP_LES=47, OP_LEU=48, 
		OP_GT=49, OP_GTS=50, OP_GTU=51, OP_GE=52, OP_GES=53, OP_GEU=54, OP_CLZ=55, 
		OP_CTZ=56, OP_POPCNT=57, OP_NEG=58, OP_ABS=59, OP_SQRT=60, OP_CEIL=61, 
		OP_FLOOR=62, OP_TRUNC=63, OP_NEAREST=64, OP_ADD=65, OP_SUB=66, OP_MUL=67, 
		OP_DIV=68, OP_DIV_S=69, OP_DIV_U=70, OP_REM_S=71, OP_REM_U=72, OP_AND=73, 
		OP_OR=74, OP_XOR=75, OP_SHL=76, OP_SHR_S=77, OP_SHR_U=78, OP_ROTL=79, 
		OP_ROTR=80, OP_MIN=81, OP_MAX=82, OP_COPYSIGN=83, CONVERT=84, MEMORY_SIZE=85, 
		MEMORY_GROW=86, MEMORY_FILL=87, MEMORY_COPY=88, MEMORY_INIT=89, TYPE=90, 
		FUNC=91, EXTERN=92, START_=93, PARAM=94, RESULT=95, LOCAL=96, GLOBAL=97, 
		TABLE=98, MEMORY=99, ELEM=100, DATA=101, OFFSET=102, IMPORT=103, EXPORT=104, 
		MODULE=105, BIN=106, QUOTE=107, SCRIPT=108, REGISTER=109, INVOKE=110, 
		GET=111, ASSERT_MALFORMED=112, ASSERT_INVALID=113, ASSERT_UNLINKABLE=114, 
		ASSERT_RETURN=115, ASSERT_RETURN_CANONICAL_NAN=116, ASSERT_RETURN_ARITHMETIC_NAN=117, 
		ASSERT_TRAP=118, ASSERT_EXHAUSTION=119, INPUT=120, OUTPUT=121, VAR=122, 
		V128=123, SPACE=124, COMMENT=125, IXX=126, FXX=127;
	public static final int
		RULE_value = 0, RULE_name = 1, RULE_numType = 2, RULE_refType = 3, RULE_vecType = 4, 
		RULE_valType = 5, RULE_heapType = 6, RULE_globalType = 7, RULE_defType = 8, 
		RULE_funcParamType = 9, RULE_funcResType = 10, RULE_funcType = 11, RULE_tableType = 12, 
		RULE_memoryType = 13, RULE_typeUse = 14, RULE_literal = 15, RULE_idx = 16, 
		RULE_bindVar = 17, RULE_instr = 18, RULE_plainInstr = 19, RULE_offsetEq = 20, 
		RULE_alignEq = 21, RULE_load = 22, RULE_store = 23, RULE_test = 24, RULE_compare = 25, 
		RULE_unary = 26, RULE_binary = 27, RULE_callIndirectInstr = 28, RULE_callInstrParams = 29, 
		RULE_callInstrInstr = 30, RULE_callInstrParamsInstr = 31, RULE_callInstrResultsInstr = 32, 
		RULE_blockInstr = 33, RULE_blockType = 34, RULE_block = 35, RULE_expr = 36, 
		RULE_expr1 = 37, RULE_callExprType = 38, RULE_callExprParams = 39, RULE_callExprResults = 40, 
		RULE_ifBlock = 41, RULE_instrList = 42, RULE_constExpr = 43, RULE_function = 44, 
		RULE_funcFields = 45, RULE_funcFieldsBody = 46, RULE_funcBody = 47, RULE_offset = 48, 
		RULE_elem = 49, RULE_table = 50, RULE_tableFields = 51, RULE_data = 52, 
		RULE_memory = 53, RULE_memoryFields = 54, RULE_sglobal = 55, RULE_globalFields = 56, 
		RULE_importDesc = 57, RULE_simport = 58, RULE_inlineImport = 59, RULE_exportDesc = 60, 
		RULE_export_ = 61, RULE_inlineExport = 62, RULE_typeDef = 63, RULE_start_ = 64, 
		RULE_moduleField = 65, RULE_module_ = 66, RULE_scriptModule = 67, RULE_action_ = 68, 
		RULE_assertion = 69, RULE_cmd = 70, RULE_meta = 71, RULE_wconst = 72, 
		RULE_constList = 73, RULE_script = 74, RULE_module = 75;
	private static String[] makeRuleNames() {
		return new String[] {
			"value", "name", "numType", "refType", "vecType", "valType", "heapType", 
			"globalType", "defType", "funcParamType", "funcResType", "funcType", 
			"tableType", "memoryType", "typeUse", "literal", "idx", "bindVar", "instr", 
			"plainInstr", "offsetEq", "alignEq", "load", "store", "test", "compare", 
			"unary", "binary", "callIndirectInstr", "callInstrParams", "callInstrInstr", 
			"callInstrParamsInstr", "callInstrResultsInstr", "blockInstr", "blockType", 
			"block", "expr", "expr1", "callExprType", "callExprParams", "callExprResults", 
			"ifBlock", "instrList", "constExpr", "function", "funcFields", "funcFieldsBody", 
			"funcBody", "offset", "elem", "table", "tableFields", "data", "memory", 
			"memoryFields", "sglobal", "globalFields", "importDesc", "simport", "inlineImport", 
			"exportDesc", "export_", "inlineExport", "typeDef", "start_", "moduleField", 
			"module_", "scriptModule", "action_", "assertion", "cmd", "meta", "wconst", 
			"constList", "script", "module"
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
			"'.store'", "'_'", "'offset='", "'align='", null, null, "'.eqz'", "'.eq'", 
			"'.ne'", "'.lt'", "'.lt_s'", "'.lt_u'", "'.le'", "'.le_s'", "'.le_u'", 
			"'.gt'", "'.gt_s'", "'.gt_u'", "'.ge'", "'.ge_s'", "'.ge_u'", "'.clz'", 
			"'.ctz'", "'.popcnt'", "'.neg'", "'.abs'", "'.sqrt'", "'.ceil'", "'.floor'", 
			"'.trunc'", "'.nearest'", "'.add'", "'.sub'", "'.mul'", "'.div'", "'.div_s'", 
			"'.div_u'", "'.rem_s'", "'.rem_u'", "'.and'", "'.or'", "'.xor'", "'.shl'", 
			"'.shr_s'", "'.shr_u'", "'.rotl'", "'.rotr'", "'.min'", "'.max'", "'.copysign'", 
			null, "'memory.size'", "'memory.grow'", "'memory.fill'", "'memory.copy'", 
			"'memory.init'", "'type'", "'func'", "'extern'", "'start'", "'param'", 
			"'result'", "'local'", "'global'", "'table'", "'memory'", "'elem'", "'data'", 
			"'offset'", "'import'", "'export'", "'module'", "'binary'", "'quote'", 
			"'script'", "'register'", "'invoke'", "'get'", "'assert_malformed'", 
			"'assert_invalid'", "'assert_unlinkable'", "'assert_return'", "'assert_return_canonical_nan'", 
			"'assert_return_arithmetic_nan'", "'assert_trap'", "'assert_exhaustion'", 
			"'input'", "'output'", null, "'v128'"
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
			"OFFSET_EQ", "ALIGN_EQ", "SIGN_POSTFIX", "MEM_SIZE", "OP_EQZ", "OP_EQ", 
			"OP_NE", "OP_LT", "OP_LTS", "OP_LTU", "OP_LE", "OP_LES", "OP_LEU", "OP_GT", 
			"OP_GTS", "OP_GTU", "OP_GE", "OP_GES", "OP_GEU", "OP_CLZ", "OP_CTZ", 
			"OP_POPCNT", "OP_NEG", "OP_ABS", "OP_SQRT", "OP_CEIL", "OP_FLOOR", "OP_TRUNC", 
			"OP_NEAREST", "OP_ADD", "OP_SUB", "OP_MUL", "OP_DIV", "OP_DIV_S", "OP_DIV_U", 
			"OP_REM_S", "OP_REM_U", "OP_AND", "OP_OR", "OP_XOR", "OP_SHL", "OP_SHR_S", 
			"OP_SHR_U", "OP_ROTL", "OP_ROTR", "OP_MIN", "OP_MAX", "OP_COPYSIGN", 
			"CONVERT", "MEMORY_SIZE", "MEMORY_GROW", "MEMORY_FILL", "MEMORY_COPY", 
			"MEMORY_INIT", "TYPE", "FUNC", "EXTERN", "START_", "PARAM", "RESULT", 
			"LOCAL", "GLOBAL", "TABLE", "MEMORY", "ELEM", "DATA", "OFFSET", "IMPORT", 
			"EXPORT", "MODULE", "BIN", "QUOTE", "SCRIPT", "REGISTER", "INVOKE", "GET", 
			"ASSERT_MALFORMED", "ASSERT_INVALID", "ASSERT_UNLINKABLE", "ASSERT_RETURN", 
			"ASSERT_RETURN_CANONICAL_NAN", "ASSERT_RETURN_ARITHMETIC_NAN", "ASSERT_TRAP", 
			"ASSERT_EXHAUSTION", "INPUT", "OUTPUT", "VAR", "V128", "SPACE", "COMMENT", 
			"IXX", "FXX"
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
			setState(152);
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
			setState(154);
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
			setState(156);
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
			setState(158);
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
			setState(160);
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
			setState(165);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VALUE_TYPE:
				enterOuterAlt(_localctx, 1);
				{
				setState(162);
				numType();
				}
				break;
			case V128:
				enterOuterAlt(_localctx, 2);
				{
				setState(163);
				vecType();
				}
				break;
			case FUNCREF:
			case EXTERNREF:
				enterOuterAlt(_localctx, 3);
				{
				setState(164);
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
			setState(167);
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
			setState(175);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VALUE_TYPE:
			case FUNCREF:
			case EXTERNREF:
			case V128:
				enterOuterAlt(_localctx, 1);
				{
				setState(169);
				valType();
				}
				break;
			case LPAR:
				enterOuterAlt(_localctx, 2);
				{
				setState(170);
				match(LPAR);
				setState(171);
				match(MUT);
				setState(172);
				valType();
				setState(173);
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
			setState(177);
			match(LPAR);
			setState(178);
			match(FUNC);
			setState(179);
			funcType();
			setState(180);
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
			setState(198);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(182);
					match(LPAR);
					setState(183);
					match(PARAM);
					setState(193);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case RPAR:
					case VALUE_TYPE:
					case FUNCREF:
					case EXTERNREF:
					case V128:
						{
						setState(187);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1664L) != 0) || _la==V128) {
							{
							{
							setState(184);
							valType();
							}
							}
							setState(189);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						}
						break;
					case VAR:
						{
						setState(190);
						bindVar();
						setState(191);
						valType();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(195);
					match(RPAR);
					}
					} 
				}
				setState(200);
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
			setState(212);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(201);
					match(LPAR);
					setState(202);
					match(RESULT);
					setState(206);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1664L) != 0) || _la==V128) {
						{
						{
						setState(203);
						valType();
						}
						}
						setState(208);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(209);
					match(RPAR);
					}
					} 
				}
				setState(214);
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
			setState(215);
			funcParamType();
			setState(216);
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
			setState(218);
			match(NAT);
			setState(220);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAT) {
				{
				setState(219);
				match(NAT);
				}
			}

			setState(222);
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
			setState(224);
			match(NAT);
			setState(226);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAT) {
				{
				setState(225);
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
			setState(228);
			match(LPAR);
			setState(229);
			match(TYPE);
			setState(230);
			idx();
			setState(231);
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
			setState(233);
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
			setState(235);
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
			setState(237);
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
			setState(243);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VALUE_TYPE:
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
			case CONVERT:
			case MEMORY_SIZE:
			case MEMORY_GROW:
			case MEMORY_FILL:
			case MEMORY_COPY:
			case MEMORY_INIT:
			case IXX:
			case FXX:
				enterOuterAlt(_localctx, 1);
				{
				setState(239);
				plainInstr();
				}
				break;
			case CALL_INDIRECT:
				enterOuterAlt(_localctx, 2);
				{
				setState(240);
				callInstrInstr();
				}
				break;
			case BLOCK:
			case LOOP:
			case IF:
				enterOuterAlt(_localctx, 3);
				{
				setState(241);
				blockInstr();
				}
				break;
			case LPAR:
				enterOuterAlt(_localctx, 4);
				{
				setState(242);
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
		public NumTypeContext numType() {
			return getRuleContext(NumTypeContext.class,0);
		}
		public TerminalNode CONST() { return getToken(WatParser.CONST, 0); }
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public TestContext test() {
			return getRuleContext(TestContext.class,0);
		}
		public CompareContext compare() {
			return getRuleContext(CompareContext.class,0);
		}
		public UnaryContext unary() {
			return getRuleContext(UnaryContext.class,0);
		}
		public BinaryContext binary() {
			return getRuleContext(BinaryContext.class,0);
		}
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
			setState(301);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(245);
				match(UNREACHABLE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(246);
				match(NOP);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(247);
				match(DROP);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(248);
				match(SELECT);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(249);
				match(BR);
				setState(250);
				idx();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(251);
				match(BR_IF);
				setState(252);
				idx();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(253);
				match(BR_TABLE);
				setState(255); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(254);
					idx();
					}
					}
					setState(257); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==NAT || _la==VAR );
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(259);
				match(RETURN);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(260);
				match(CALL);
				setState(261);
				idx();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(262);
				match(LOCAL_GET);
				setState(263);
				idx();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(264);
				match(LOCAL_SET);
				setState(265);
				idx();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(266);
				match(LOCAL_TEE);
				setState(267);
				idx();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(268);
				match(GLOBAL_GET);
				setState(269);
				idx();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(270);
				match(GLOBAL_SET);
				setState(271);
				idx();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(272);
				load();
				setState(274);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==OFFSET_EQ) {
					{
					setState(273);
					offsetEq();
					}
				}

				setState(277);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ALIGN_EQ) {
					{
					setState(276);
					alignEq();
					}
				}

				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(279);
				store();
				setState(281);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==OFFSET_EQ) {
					{
					setState(280);
					offsetEq();
					}
				}

				setState(284);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ALIGN_EQ) {
					{
					setState(283);
					alignEq();
					}
				}

				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(286);
				match(MEMORY_SIZE);
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(287);
				match(MEMORY_GROW);
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(288);
				match(MEMORY_FILL);
				}
				break;
			case 20:
				enterOuterAlt(_localctx, 20);
				{
				setState(289);
				match(MEMORY_COPY);
				}
				break;
			case 21:
				enterOuterAlt(_localctx, 21);
				{
				setState(290);
				match(MEMORY_INIT);
				setState(291);
				idx();
				}
				break;
			case 22:
				enterOuterAlt(_localctx, 22);
				{
				setState(292);
				numType();
				setState(293);
				match(CONST);
				setState(294);
				literal();
				}
				break;
			case 23:
				enterOuterAlt(_localctx, 23);
				{
				setState(296);
				test();
				}
				break;
			case 24:
				enterOuterAlt(_localctx, 24);
				{
				setState(297);
				compare();
				}
				break;
			case 25:
				enterOuterAlt(_localctx, 25);
				{
				setState(298);
				unary();
				}
				break;
			case 26:
				enterOuterAlt(_localctx, 26);
				{
				setState(299);
				binary();
				}
				break;
			case 27:
				enterOuterAlt(_localctx, 27);
				{
				setState(300);
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
			setState(303);
			match(OFFSET_EQ);
			setState(304);
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
			setState(306);
			match(ALIGN_EQ);
			setState(307);
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
			setState(309);
			numType();
			setState(310);
			match(LOAD);
			setState(314);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MEM_SIZE) {
				{
				setState(311);
				match(MEM_SIZE);
				setState(312);
				match(UNDERSCORE);
				setState(313);
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
			setState(316);
			numType();
			setState(317);
			match(STORE);
			setState(319);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MEM_SIZE) {
				{
				setState(318);
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
	public static class TestContext extends ParserRuleContext {
		public TerminalNode IXX() { return getToken(WatParser.IXX, 0); }
		public TerminalNode OP_EQZ() { return getToken(WatParser.OP_EQZ, 0); }
		public TestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_test; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TestContext test() throws RecognitionException {
		TestContext _localctx = new TestContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_test);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(321);
			match(IXX);
			setState(322);
			match(OP_EQZ);
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
	public static class CompareContext extends ParserRuleContext {
		public TerminalNode IXX() { return getToken(WatParser.IXX, 0); }
		public TerminalNode OP_EQ() { return getToken(WatParser.OP_EQ, 0); }
		public TerminalNode OP_NE() { return getToken(WatParser.OP_NE, 0); }
		public TerminalNode OP_LTS() { return getToken(WatParser.OP_LTS, 0); }
		public TerminalNode OP_LTU() { return getToken(WatParser.OP_LTU, 0); }
		public TerminalNode OP_LES() { return getToken(WatParser.OP_LES, 0); }
		public TerminalNode OP_LEU() { return getToken(WatParser.OP_LEU, 0); }
		public TerminalNode OP_GTS() { return getToken(WatParser.OP_GTS, 0); }
		public TerminalNode OP_GTU() { return getToken(WatParser.OP_GTU, 0); }
		public TerminalNode OP_GES() { return getToken(WatParser.OP_GES, 0); }
		public TerminalNode OP_GEU() { return getToken(WatParser.OP_GEU, 0); }
		public TerminalNode FXX() { return getToken(WatParser.FXX, 0); }
		public TerminalNode OP_LT() { return getToken(WatParser.OP_LT, 0); }
		public TerminalNode OP_LE() { return getToken(WatParser.OP_LE, 0); }
		public TerminalNode OP_GT() { return getToken(WatParser.OP_GT, 0); }
		public TerminalNode OP_GE() { return getToken(WatParser.OP_GE, 0); }
		public CompareContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compare; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterCompare(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitCompare(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitCompare(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompareContext compare() throws RecognitionException {
		CompareContext _localctx = new CompareContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_compare);
		try {
			setState(356);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(324);
				match(IXX);
				setState(325);
				match(OP_EQ);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(326);
				match(IXX);
				setState(327);
				match(OP_NE);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(328);
				match(IXX);
				setState(329);
				match(OP_LTS);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(330);
				match(IXX);
				setState(331);
				match(OP_LTU);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(332);
				match(IXX);
				setState(333);
				match(OP_LES);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(334);
				match(IXX);
				setState(335);
				match(OP_LEU);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(336);
				match(IXX);
				setState(337);
				match(OP_GTS);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(338);
				match(IXX);
				setState(339);
				match(OP_GTU);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(340);
				match(IXX);
				setState(341);
				match(OP_GES);
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(342);
				match(IXX);
				setState(343);
				match(OP_GEU);
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(344);
				match(FXX);
				setState(345);
				match(OP_EQ);
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(346);
				match(FXX);
				setState(347);
				match(OP_NE);
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(348);
				match(FXX);
				setState(349);
				match(OP_LT);
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(350);
				match(FXX);
				setState(351);
				match(OP_LE);
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(352);
				match(FXX);
				setState(353);
				match(OP_GT);
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(354);
				match(FXX);
				setState(355);
				match(OP_GE);
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
	public static class UnaryContext extends ParserRuleContext {
		public TerminalNode IXX() { return getToken(WatParser.IXX, 0); }
		public TerminalNode OP_CLZ() { return getToken(WatParser.OP_CLZ, 0); }
		public TerminalNode OP_CTZ() { return getToken(WatParser.OP_CTZ, 0); }
		public TerminalNode OP_POPCNT() { return getToken(WatParser.OP_POPCNT, 0); }
		public TerminalNode FXX() { return getToken(WatParser.FXX, 0); }
		public TerminalNode OP_NEG() { return getToken(WatParser.OP_NEG, 0); }
		public TerminalNode OP_ABS() { return getToken(WatParser.OP_ABS, 0); }
		public TerminalNode OP_SQRT() { return getToken(WatParser.OP_SQRT, 0); }
		public TerminalNode OP_CEIL() { return getToken(WatParser.OP_CEIL, 0); }
		public TerminalNode OP_FLOOR() { return getToken(WatParser.OP_FLOOR, 0); }
		public TerminalNode OP_TRUNC() { return getToken(WatParser.OP_TRUNC, 0); }
		public TerminalNode OP_NEAREST() { return getToken(WatParser.OP_NEAREST, 0); }
		public UnaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unary; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterUnary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitUnary(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitUnary(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryContext unary() throws RecognitionException {
		UnaryContext _localctx = new UnaryContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_unary);
		try {
			setState(378);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(358);
				match(IXX);
				setState(359);
				match(OP_CLZ);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(360);
				match(IXX);
				setState(361);
				match(OP_CTZ);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(362);
				match(IXX);
				setState(363);
				match(OP_POPCNT);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(364);
				match(FXX);
				setState(365);
				match(OP_NEG);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(366);
				match(FXX);
				setState(367);
				match(OP_ABS);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(368);
				match(FXX);
				setState(369);
				match(OP_SQRT);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(370);
				match(FXX);
				setState(371);
				match(OP_CEIL);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(372);
				match(FXX);
				setState(373);
				match(OP_FLOOR);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(374);
				match(FXX);
				setState(375);
				match(OP_TRUNC);
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(376);
				match(FXX);
				setState(377);
				match(OP_NEAREST);
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
	public static class BinaryContext extends ParserRuleContext {
		public TerminalNode IXX() { return getToken(WatParser.IXX, 0); }
		public TerminalNode OP_ADD() { return getToken(WatParser.OP_ADD, 0); }
		public TerminalNode OP_SUB() { return getToken(WatParser.OP_SUB, 0); }
		public TerminalNode OP_MUL() { return getToken(WatParser.OP_MUL, 0); }
		public TerminalNode OP_DIV_S() { return getToken(WatParser.OP_DIV_S, 0); }
		public TerminalNode OP_DIV_U() { return getToken(WatParser.OP_DIV_U, 0); }
		public TerminalNode OP_REM_S() { return getToken(WatParser.OP_REM_S, 0); }
		public TerminalNode OP_REM_U() { return getToken(WatParser.OP_REM_U, 0); }
		public TerminalNode OP_AND() { return getToken(WatParser.OP_AND, 0); }
		public TerminalNode OP_OR() { return getToken(WatParser.OP_OR, 0); }
		public TerminalNode OP_XOR() { return getToken(WatParser.OP_XOR, 0); }
		public TerminalNode OP_SHL() { return getToken(WatParser.OP_SHL, 0); }
		public TerminalNode OP_SHR_S() { return getToken(WatParser.OP_SHR_S, 0); }
		public TerminalNode OP_SHR_U() { return getToken(WatParser.OP_SHR_U, 0); }
		public TerminalNode OP_ROTL() { return getToken(WatParser.OP_ROTL, 0); }
		public TerminalNode OP_ROTR() { return getToken(WatParser.OP_ROTR, 0); }
		public TerminalNode FXX() { return getToken(WatParser.FXX, 0); }
		public TerminalNode OP_DIV() { return getToken(WatParser.OP_DIV, 0); }
		public TerminalNode OP_MIN() { return getToken(WatParser.OP_MIN, 0); }
		public TerminalNode OP_MAX() { return getToken(WatParser.OP_MAX, 0); }
		public TerminalNode OP_COPYSIGN() { return getToken(WatParser.OP_COPYSIGN, 0); }
		public BinaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_binary; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterBinary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitBinary(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitBinary(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BinaryContext binary() throws RecognitionException {
		BinaryContext _localctx = new BinaryContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_binary);
		try {
			setState(424);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(380);
				match(IXX);
				setState(381);
				match(OP_ADD);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(382);
				match(IXX);
				setState(383);
				match(OP_SUB);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(384);
				match(IXX);
				setState(385);
				match(OP_MUL);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(386);
				match(IXX);
				setState(387);
				match(OP_DIV_S);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(388);
				match(IXX);
				setState(389);
				match(OP_DIV_U);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(390);
				match(IXX);
				setState(391);
				match(OP_REM_S);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(392);
				match(IXX);
				setState(393);
				match(OP_REM_U);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(394);
				match(IXX);
				setState(395);
				match(OP_AND);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(396);
				match(IXX);
				setState(397);
				match(OP_OR);
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(398);
				match(IXX);
				setState(399);
				match(OP_XOR);
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(400);
				match(IXX);
				setState(401);
				match(OP_SHL);
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(402);
				match(IXX);
				setState(403);
				match(OP_SHR_S);
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(404);
				match(IXX);
				setState(405);
				match(OP_SHR_U);
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(406);
				match(IXX);
				setState(407);
				match(OP_ROTL);
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(408);
				match(IXX);
				setState(409);
				match(OP_ROTR);
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(410);
				match(FXX);
				setState(411);
				match(OP_ADD);
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(412);
				match(FXX);
				setState(413);
				match(OP_SUB);
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(414);
				match(FXX);
				setState(415);
				match(OP_MUL);
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(416);
				match(FXX);
				setState(417);
				match(OP_DIV);
				}
				break;
			case 20:
				enterOuterAlt(_localctx, 20);
				{
				setState(418);
				match(FXX);
				setState(419);
				match(OP_MIN);
				}
				break;
			case 21:
				enterOuterAlt(_localctx, 21);
				{
				setState(420);
				match(FXX);
				setState(421);
				match(OP_MAX);
				}
				break;
			case 22:
				enterOuterAlt(_localctx, 22);
				{
				setState(422);
				match(FXX);
				setState(423);
				match(OP_COPYSIGN);
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
		enterRule(_localctx, 56, RULE_callIndirectInstr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(426);
			match(CALL_INDIRECT);
			setState(428);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(427);
				typeUse();
				}
				break;
			}
			setState(430);
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
		enterRule(_localctx, 58, RULE_callInstrParams);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(443);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(432);
					match(LPAR);
					setState(433);
					match(PARAM);
					setState(437);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1664L) != 0) || _la==V128) {
						{
						{
						setState(434);
						valType();
						}
						}
						setState(439);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(440);
					match(RPAR);
					}
					} 
				}
				setState(445);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
			}
			setState(457);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAR) {
				{
				{
				setState(446);
				match(LPAR);
				setState(447);
				match(RESULT);
				setState(451);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1664L) != 0) || _la==V128) {
					{
					{
					setState(448);
					valType();
					}
					}
					setState(453);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(454);
				match(RPAR);
				}
				}
				setState(459);
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
		enterRule(_localctx, 60, RULE_callInstrInstr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(460);
			match(CALL_INDIRECT);
			setState(462);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				{
				setState(461);
				typeUse();
				}
				break;
			}
			setState(464);
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
		enterRule(_localctx, 62, RULE_callInstrParamsInstr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(477);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(466);
					match(LPAR);
					setState(467);
					match(PARAM);
					setState(471);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1664L) != 0) || _la==V128) {
						{
						{
						setState(468);
						valType();
						}
						}
						setState(473);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(474);
					match(RPAR);
					}
					} 
				}
				setState(479);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			}
			setState(480);
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
		enterRule(_localctx, 64, RULE_callInstrResultsInstr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(493);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(482);
					match(LPAR);
					setState(483);
					match(RESULT);
					setState(487);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1664L) != 0) || _la==V128) {
						{
						{
						setState(484);
						valType();
						}
						}
						setState(489);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(490);
					match(RPAR);
					}
					} 
				}
				setState(495);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
			}
			setState(496);
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
		enterRule(_localctx, 66, RULE_blockInstr);
		int _la;
		try {
			setState(523);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BLOCK:
			case LOOP:
				enterOuterAlt(_localctx, 1);
				{
				setState(498);
				_la = _input.LA(1);
				if ( !(_la==BLOCK || _la==LOOP) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(500);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(499);
					bindVar();
					}
				}

				setState(502);
				block();
				setState(503);
				match(END);
				setState(505);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(504);
					bindVar();
					}
				}

				}
				break;
			case IF:
				enterOuterAlt(_localctx, 2);
				{
				setState(507);
				match(IF);
				setState(509);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(508);
					bindVar();
					}
				}

				setState(511);
				block();
				setState(517);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(512);
					match(ELSE);
					setState(514);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==VAR) {
						{
						setState(513);
						bindVar();
						}
					}

					setState(516);
					instrList();
					}
				}

				setState(519);
				match(END);
				setState(521);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(520);
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
		enterRule(_localctx, 68, RULE_blockType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(525);
			match(LPAR);
			setState(526);
			match(RESULT);
			setState(527);
			valType();
			setState(528);
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
		enterRule(_localctx, 70, RULE_block);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(531);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				{
				setState(530);
				blockType();
				}
				break;
			}
			setState(533);
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
		enterRule(_localctx, 72, RULE_expr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(535);
			match(LPAR);
			setState(536);
			expr1();
			setState(537);
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
		enterRule(_localctx, 74, RULE_expr1);
		int _la;
		try {
			setState(563);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VALUE_TYPE:
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
			case CONVERT:
			case MEMORY_SIZE:
			case MEMORY_GROW:
			case MEMORY_FILL:
			case MEMORY_COPY:
			case MEMORY_INIT:
			case IXX:
			case FXX:
				enterOuterAlt(_localctx, 1);
				{
				setState(539);
				plainInstr();
				setState(543);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LPAR) {
					{
					{
					setState(540);
					expr();
					}
					}
					setState(545);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case CALL_INDIRECT:
				enterOuterAlt(_localctx, 2);
				{
				setState(546);
				match(CALL_INDIRECT);
				setState(547);
				callExprType();
				}
				break;
			case BLOCK:
				enterOuterAlt(_localctx, 3);
				{
				setState(548);
				match(BLOCK);
				setState(550);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(549);
					bindVar();
					}
				}

				setState(552);
				block();
				}
				break;
			case LOOP:
				enterOuterAlt(_localctx, 4);
				{
				setState(553);
				match(LOOP);
				setState(555);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(554);
					bindVar();
					}
				}

				setState(557);
				block();
				}
				break;
			case IF:
				enterOuterAlt(_localctx, 5);
				{
				setState(558);
				match(IF);
				setState(560);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(559);
					bindVar();
					}
				}

				setState(562);
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
		enterRule(_localctx, 76, RULE_callExprType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(566);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				{
				setState(565);
				typeUse();
				}
				break;
			}
			setState(568);
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
		enterRule(_localctx, 78, RULE_callExprParams);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(581);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,46,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(570);
					match(LPAR);
					setState(571);
					match(PARAM);
					setState(575);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1664L) != 0) || _la==V128) {
						{
						{
						setState(572);
						valType();
						}
						}
						setState(577);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(578);
					match(RPAR);
					}
					} 
				}
				setState(583);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,46,_ctx);
			}
			setState(584);
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
		enterRule(_localctx, 80, RULE_callExprResults);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(597);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(586);
					match(LPAR);
					setState(587);
					match(RESULT);
					setState(591);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1664L) != 0) || _la==V128) {
						{
						{
						setState(588);
						valType();
						}
						}
						setState(593);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(594);
					match(RPAR);
					}
					} 
				}
				setState(599);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
			}
			setState(603);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAR) {
				{
				{
				setState(600);
				expr();
				}
				}
				setState(605);
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
		enterRule(_localctx, 82, RULE_ifBlock);
		int _la;
		try {
			int _alt;
			setState(626);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(606);
				blockType();
				setState(607);
				ifBlock();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(612);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(609);
						expr();
						}
						} 
					}
					setState(614);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
				}
				setState(615);
				match(LPAR);
				setState(616);
				match(THEN);
				setState(617);
				instrList();
				setState(618);
				match(RPAR);
				setState(624);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LPAR) {
					{
					setState(619);
					match(LPAR);
					setState(620);
					match(ELSE);
					setState(621);
					instrList();
					setState(622);
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
		enterRule(_localctx, 84, RULE_instrList);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(631);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(628);
					instr();
					}
					} 
				}
				setState(633);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
			}
			setState(635);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CALL_INDIRECT) {
				{
				setState(634);
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
		enterRule(_localctx, 86, RULE_constExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(637);
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
		enterRule(_localctx, 88, RULE_function);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(639);
			match(LPAR);
			setState(640);
			match(FUNC);
			setState(642);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(641);
				bindVar();
				}
			}

			setState(644);
			funcFields();
			setState(645);
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
		enterRule(_localctx, 90, RULE_funcFields);
		try {
			setState(660);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(648);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
				case 1:
					{
					setState(647);
					typeUse();
					}
					break;
				}
				setState(650);
				funcFieldsBody();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(651);
				inlineImport();
				setState(653);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
				case 1:
					{
					setState(652);
					typeUse();
					}
					break;
				}
				setState(655);
				funcType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(657);
				inlineExport();
				setState(658);
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
		enterRule(_localctx, 92, RULE_funcFieldsBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(662);
			funcType();
			setState(663);
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
		enterRule(_localctx, 94, RULE_funcBody);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(681);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,61,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(665);
					match(LPAR);
					setState(666);
					match(LOCAL);
					setState(676);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case RPAR:
					case VALUE_TYPE:
					case FUNCREF:
					case EXTERNREF:
					case V128:
						{
						setState(670);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1664L) != 0) || _la==V128) {
							{
							{
							setState(667);
							valType();
							}
							}
							setState(672);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						}
						break;
					case VAR:
						{
						setState(673);
						bindVar();
						setState(674);
						valType();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(678);
					match(RPAR);
					}
					} 
				}
				setState(683);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,61,_ctx);
			}
			setState(684);
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
		enterRule(_localctx, 96, RULE_offset);
		try {
			setState(692);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(686);
				match(LPAR);
				setState(687);
				match(OFFSET);
				setState(688);
				constExpr();
				setState(689);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(691);
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
		enterRule(_localctx, 98, RULE_elem);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(694);
			match(LPAR);
			setState(695);
			match(ELEM);
			setState(697);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAT || _la==VAR) {
				{
				setState(696);
				idx();
				}
			}

			setState(699);
			offset();
			setState(703);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NAT || _la==VAR) {
				{
				{
				setState(700);
				idx();
				}
				}
				setState(705);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(706);
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
		enterRule(_localctx, 100, RULE_table);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(708);
			match(LPAR);
			setState(709);
			match(TABLE);
			setState(711);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(710);
				bindVar();
				}
			}

			setState(713);
			tableFields();
			setState(714);
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
		enterRule(_localctx, 102, RULE_tableFields);
		int _la;
		try {
			setState(734);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,67,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(716);
				tableType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(717);
				inlineImport();
				setState(718);
				tableType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(720);
				inlineExport();
				setState(721);
				tableFields();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(723);
				refType();
				setState(724);
				match(LPAR);
				setState(725);
				match(ELEM);
				setState(729);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NAT || _la==VAR) {
					{
					{
					setState(726);
					idx();
					}
					}
					setState(731);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(732);
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
		enterRule(_localctx, 104, RULE_data);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(736);
			match(LPAR);
			setState(737);
			match(DATA);
			setState(739);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAT || _la==VAR) {
				{
				setState(738);
				idx();
				}
			}

			setState(741);
			offset();
			setState(745);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==STRING_) {
				{
				{
				setState(742);
				match(STRING_);
				}
				}
				setState(747);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(748);
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
		enterRule(_localctx, 106, RULE_memory);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(750);
			match(LPAR);
			setState(751);
			match(MEMORY);
			setState(753);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(752);
				bindVar();
				}
			}

			setState(755);
			memoryFields();
			setState(756);
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
		enterRule(_localctx, 108, RULE_memoryFields);
		int _la;
		try {
			setState(774);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(758);
				memoryType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(759);
				inlineImport();
				setState(760);
				memoryType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(762);
				inlineExport();
				setState(763);
				memoryFields();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(765);
				match(LPAR);
				setState(766);
				match(DATA);
				setState(770);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STRING_) {
					{
					{
					setState(767);
					match(STRING_);
					}
					}
					setState(772);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(773);
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
		enterRule(_localctx, 110, RULE_sglobal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(776);
			match(LPAR);
			setState(777);
			match(GLOBAL);
			setState(779);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(778);
				bindVar();
				}
			}

			setState(781);
			globalFields();
			setState(782);
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
		enterRule(_localctx, 112, RULE_globalFields);
		try {
			setState(793);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,74,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(784);
				globalType();
				setState(785);
				constExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(787);
				inlineImport();
				setState(788);
				globalType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(790);
				inlineExport();
				setState(791);
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
		enterRule(_localctx, 114, RULE_importDesc);
		int _la;
		try {
			setState(835);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,80,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(795);
				match(LPAR);
				setState(796);
				match(FUNC);
				setState(798);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(797);
					bindVar();
					}
				}

				setState(800);
				typeUse();
				setState(801);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(803);
				match(LPAR);
				setState(804);
				match(FUNC);
				setState(806);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(805);
					bindVar();
					}
				}

				setState(808);
				funcType();
				setState(809);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(811);
				match(LPAR);
				setState(812);
				match(TABLE);
				setState(814);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(813);
					bindVar();
					}
				}

				setState(816);
				tableType();
				setState(817);
				match(RPAR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(819);
				match(LPAR);
				setState(820);
				match(MEMORY);
				setState(822);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(821);
					bindVar();
					}
				}

				setState(824);
				memoryType();
				setState(825);
				match(RPAR);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(827);
				match(LPAR);
				setState(828);
				match(GLOBAL);
				setState(830);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(829);
					bindVar();
					}
				}

				setState(832);
				globalType();
				setState(833);
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
		enterRule(_localctx, 116, RULE_simport);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(837);
			match(LPAR);
			setState(838);
			match(IMPORT);
			setState(839);
			name();
			setState(840);
			name();
			setState(841);
			importDesc();
			setState(842);
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
		enterRule(_localctx, 118, RULE_inlineImport);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(844);
			match(LPAR);
			setState(845);
			match(IMPORT);
			setState(846);
			name();
			setState(847);
			name();
			setState(848);
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
		enterRule(_localctx, 120, RULE_exportDesc);
		try {
			setState(870);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(850);
				match(LPAR);
				setState(851);
				match(FUNC);
				setState(852);
				idx();
				setState(853);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(855);
				match(LPAR);
				setState(856);
				match(TABLE);
				setState(857);
				idx();
				setState(858);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(860);
				match(LPAR);
				setState(861);
				match(MEMORY);
				setState(862);
				idx();
				setState(863);
				match(RPAR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(865);
				match(LPAR);
				setState(866);
				match(GLOBAL);
				setState(867);
				idx();
				setState(868);
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
		enterRule(_localctx, 122, RULE_export_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(872);
			match(LPAR);
			setState(873);
			match(EXPORT);
			setState(874);
			name();
			setState(875);
			exportDesc();
			setState(876);
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
		enterRule(_localctx, 124, RULE_inlineExport);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(878);
			match(LPAR);
			setState(879);
			match(EXPORT);
			setState(880);
			name();
			setState(881);
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
		enterRule(_localctx, 126, RULE_typeDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(883);
			match(LPAR);
			setState(884);
			match(TYPE);
			setState(886);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(885);
				bindVar();
				}
			}

			setState(888);
			defType();
			setState(889);
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
		enterRule(_localctx, 128, RULE_start_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(891);
			match(LPAR);
			setState(892);
			match(START_);
			setState(893);
			idx();
			setState(894);
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
		enterRule(_localctx, 130, RULE_moduleField);
		try {
			setState(906);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,83,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(896);
				typeDef();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(897);
				sglobal();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(898);
				table();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(899);
				memory();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(900);
				function();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(901);
				elem();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(902);
				data();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(903);
				start_();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(904);
				simport();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(905);
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
		enterRule(_localctx, 132, RULE_module_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(908);
			match(LPAR);
			setState(909);
			match(MODULE);
			setState(911);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(910);
				match(VAR);
				}
			}

			setState(916);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAR) {
				{
				{
				setState(913);
				moduleField();
				}
				}
				setState(918);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(919);
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
		enterRule(_localctx, 134, RULE_scriptModule);
		int _la;
		try {
			setState(935);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(921);
				module_();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(922);
				match(LPAR);
				setState(923);
				match(MODULE);
				setState(925);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(924);
					match(VAR);
					}
				}

				setState(927);
				_la = _input.LA(1);
				if ( !(_la==BIN || _la==QUOTE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(931);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STRING_) {
					{
					{
					setState(928);
					match(STRING_);
					}
					}
					setState(933);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(934);
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
		enterRule(_localctx, 136, RULE_action_);
		int _la;
		try {
			setState(954);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,91,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(937);
				match(LPAR);
				setState(938);
				match(INVOKE);
				setState(940);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(939);
					match(VAR);
					}
				}

				setState(942);
				name();
				setState(943);
				constList();
				setState(944);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(946);
				match(LPAR);
				setState(947);
				match(GET);
				setState(949);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(948);
					match(VAR);
					}
				}

				setState(951);
				name();
				setState(952);
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
		enterRule(_localctx, 138, RULE_assertion);
		try {
			setState(1008);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,92,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(956);
				match(LPAR);
				setState(957);
				match(ASSERT_MALFORMED);
				setState(958);
				scriptModule();
				setState(959);
				match(STRING_);
				setState(960);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(962);
				match(LPAR);
				setState(963);
				match(ASSERT_INVALID);
				setState(964);
				scriptModule();
				setState(965);
				match(STRING_);
				setState(966);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(968);
				match(LPAR);
				setState(969);
				match(ASSERT_UNLINKABLE);
				setState(970);
				scriptModule();
				setState(971);
				match(STRING_);
				setState(972);
				match(RPAR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(974);
				match(LPAR);
				setState(975);
				match(ASSERT_TRAP);
				setState(976);
				scriptModule();
				setState(977);
				match(STRING_);
				setState(978);
				match(RPAR);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(980);
				match(LPAR);
				setState(981);
				match(ASSERT_RETURN);
				setState(982);
				action_();
				setState(983);
				constList();
				setState(984);
				match(RPAR);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(986);
				match(LPAR);
				setState(987);
				match(ASSERT_RETURN_CANONICAL_NAN);
				setState(988);
				action_();
				setState(989);
				match(RPAR);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(991);
				match(LPAR);
				setState(992);
				match(ASSERT_RETURN_ARITHMETIC_NAN);
				setState(993);
				action_();
				setState(994);
				match(RPAR);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(996);
				match(LPAR);
				setState(997);
				match(ASSERT_TRAP);
				setState(998);
				action_();
				setState(999);
				match(STRING_);
				setState(1000);
				match(RPAR);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1002);
				match(LPAR);
				setState(1003);
				match(ASSERT_EXHAUSTION);
				setState(1004);
				action_();
				setState(1005);
				match(STRING_);
				setState(1006);
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
		enterRule(_localctx, 140, RULE_cmd);
		int _la;
		try {
			setState(1022);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,94,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1010);
				action_();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1011);
				assertion();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1012);
				scriptModule();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1013);
				match(LPAR);
				setState(1014);
				match(REGISTER);
				setState(1015);
				name();
				setState(1017);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(1016);
					match(VAR);
					}
				}

				setState(1019);
				match(RPAR);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1021);
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
		enterRule(_localctx, 142, RULE_meta);
		int _la;
		try {
			setState(1056);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,100,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1024);
				match(LPAR);
				setState(1025);
				match(SCRIPT);
				setState(1027);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(1026);
					match(VAR);
					}
				}

				setState(1032);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LPAR) {
					{
					{
					setState(1029);
					cmd();
					}
					}
					setState(1034);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1035);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1036);
				match(LPAR);
				setState(1037);
				match(INPUT);
				setState(1039);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(1038);
					match(VAR);
					}
				}

				setState(1041);
				match(STRING_);
				setState(1042);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1043);
				match(LPAR);
				setState(1044);
				match(OUTPUT);
				setState(1046);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(1045);
					match(VAR);
					}
				}

				setState(1048);
				match(STRING_);
				setState(1049);
				match(RPAR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1050);
				match(LPAR);
				setState(1051);
				match(OUTPUT);
				setState(1053);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(1052);
					match(VAR);
					}
				}

				setState(1055);
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
		enterRule(_localctx, 144, RULE_wconst);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1058);
			match(LPAR);
			setState(1059);
			match(CONST);
			setState(1060);
			literal();
			setState(1061);
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
		enterRule(_localctx, 146, RULE_constList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1066);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAR) {
				{
				{
				setState(1063);
				wconst();
				}
				}
				setState(1068);
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
		enterRule(_localctx, 148, RULE_script);
		int _la;
		try {
			setState(1083);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,104,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1072);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LPAR) {
					{
					{
					setState(1069);
					cmd();
					}
					}
					setState(1074);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1075);
				match(EOF);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1077); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(1076);
					moduleField();
					}
					}
					setState(1079); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==LPAR );
				setState(1081);
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
		enterRule(_localctx, 150, RULE_module);
		int _la;
		try {
			setState(1095);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,106,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1085);
				module_();
				setState(1086);
				match(EOF);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1091);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LPAR) {
					{
					{
					setState(1088);
					moduleField();
					}
					}
					setState(1093);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1094);
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
		"\u0004\u0001\u007f\u044a\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
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
		"E\u0002F\u0007F\u0002G\u0007G\u0002H\u0007H\u0002I\u0007I\u0002J\u0007"+
		"J\u0002K\u0007K\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001"+
		"\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0003\u0005\u00a6\b\u0005\u0001\u0006\u0001"+
		"\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0003\u0007\u00b0\b\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\t\u0001\t\u0001\t\u0005\t\u00ba\b\t\n\t\f\t\u00bd\t\t\u0001\t"+
		"\u0001\t\u0001\t\u0003\t\u00c2\b\t\u0001\t\u0005\t\u00c5\b\t\n\t\f\t\u00c8"+
		"\t\t\u0001\n\u0001\n\u0001\n\u0005\n\u00cd\b\n\n\n\f\n\u00d0\t\n\u0001"+
		"\n\u0005\n\u00d3\b\n\n\n\f\n\u00d6\t\n\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\f\u0001\f\u0003\f\u00dd\b\f\u0001\f\u0001\f\u0001\r\u0001\r\u0003"+
		"\r\u00e3\b\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u00f4\b\u0012"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0004\u0013\u0100\b\u0013"+
		"\u000b\u0013\f\u0013\u0101\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013"+
		"\u0113\b\u0013\u0001\u0013\u0003\u0013\u0116\b\u0013\u0001\u0013\u0001"+
		"\u0013\u0003\u0013\u011a\b\u0013\u0001\u0013\u0003\u0013\u011d\b\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u012e\b\u0013\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u013b\b\u0016"+
		"\u0001\u0017\u0001\u0017\u0001\u0017\u0003\u0017\u0140\b\u0017\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u0165\b\u0019"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0003\u001a\u017b\b\u001a\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0003\u001b\u01a9\b\u001b\u0001\u001c\u0001\u001c\u0003\u001c\u01ad\b"+
		"\u001c\u0001\u001c\u0001\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0005"+
		"\u001d\u01b4\b\u001d\n\u001d\f\u001d\u01b7\t\u001d\u0001\u001d\u0005\u001d"+
		"\u01ba\b\u001d\n\u001d\f\u001d\u01bd\t\u001d\u0001\u001d\u0001\u001d\u0001"+
		"\u001d\u0005\u001d\u01c2\b\u001d\n\u001d\f\u001d\u01c5\t\u001d\u0001\u001d"+
		"\u0005\u001d\u01c8\b\u001d\n\u001d\f\u001d\u01cb\t\u001d\u0001\u001e\u0001"+
		"\u001e\u0003\u001e\u01cf\b\u001e\u0001\u001e\u0001\u001e\u0001\u001f\u0001"+
		"\u001f\u0001\u001f\u0005\u001f\u01d6\b\u001f\n\u001f\f\u001f\u01d9\t\u001f"+
		"\u0001\u001f\u0005\u001f\u01dc\b\u001f\n\u001f\f\u001f\u01df\t\u001f\u0001"+
		"\u001f\u0001\u001f\u0001 \u0001 \u0001 \u0005 \u01e6\b \n \f \u01e9\t"+
		" \u0001 \u0005 \u01ec\b \n \f \u01ef\t \u0001 \u0001 \u0001!\u0001!\u0003"+
		"!\u01f5\b!\u0001!\u0001!\u0001!\u0003!\u01fa\b!\u0001!\u0001!\u0003!\u01fe"+
		"\b!\u0001!\u0001!\u0001!\u0003!\u0203\b!\u0001!\u0003!\u0206\b!\u0001"+
		"!\u0001!\u0003!\u020a\b!\u0003!\u020c\b!\u0001\"\u0001\"\u0001\"\u0001"+
		"\"\u0001\"\u0001#\u0003#\u0214\b#\u0001#\u0001#\u0001$\u0001$\u0001$\u0001"+
		"$\u0001%\u0001%\u0005%\u021e\b%\n%\f%\u0221\t%\u0001%\u0001%\u0001%\u0001"+
		"%\u0003%\u0227\b%\u0001%\u0001%\u0001%\u0003%\u022c\b%\u0001%\u0001%\u0001"+
		"%\u0003%\u0231\b%\u0001%\u0003%\u0234\b%\u0001&\u0003&\u0237\b&\u0001"+
		"&\u0001&\u0001\'\u0001\'\u0001\'\u0005\'\u023e\b\'\n\'\f\'\u0241\t\'\u0001"+
		"\'\u0005\'\u0244\b\'\n\'\f\'\u0247\t\'\u0001\'\u0001\'\u0001(\u0001(\u0001"+
		"(\u0005(\u024e\b(\n(\f(\u0251\t(\u0001(\u0005(\u0254\b(\n(\f(\u0257\t"+
		"(\u0001(\u0005(\u025a\b(\n(\f(\u025d\t(\u0001)\u0001)\u0001)\u0001)\u0005"+
		")\u0263\b)\n)\f)\u0266\t)\u0001)\u0001)\u0001)\u0001)\u0001)\u0001)\u0001"+
		")\u0001)\u0001)\u0003)\u0271\b)\u0003)\u0273\b)\u0001*\u0005*\u0276\b"+
		"*\n*\f*\u0279\t*\u0001*\u0003*\u027c\b*\u0001+\u0001+\u0001,\u0001,\u0001"+
		",\u0003,\u0283\b,\u0001,\u0001,\u0001,\u0001-\u0003-\u0289\b-\u0001-\u0001"+
		"-\u0001-\u0003-\u028e\b-\u0001-\u0001-\u0001-\u0001-\u0001-\u0003-\u0295"+
		"\b-\u0001.\u0001.\u0001.\u0001/\u0001/\u0001/\u0005/\u029d\b/\n/\f/\u02a0"+
		"\t/\u0001/\u0001/\u0001/\u0003/\u02a5\b/\u0001/\u0005/\u02a8\b/\n/\f/"+
		"\u02ab\t/\u0001/\u0001/\u00010\u00010\u00010\u00010\u00010\u00010\u0003"+
		"0\u02b5\b0\u00011\u00011\u00011\u00031\u02ba\b1\u00011\u00011\u00051\u02be"+
		"\b1\n1\f1\u02c1\t1\u00011\u00011\u00012\u00012\u00012\u00032\u02c8\b2"+
		"\u00012\u00012\u00012\u00013\u00013\u00013\u00013\u00013\u00013\u0001"+
		"3\u00013\u00013\u00013\u00013\u00053\u02d8\b3\n3\f3\u02db\t3\u00013\u0001"+
		"3\u00033\u02df\b3\u00014\u00014\u00014\u00034\u02e4\b4\u00014\u00014\u0005"+
		"4\u02e8\b4\n4\f4\u02eb\t4\u00014\u00014\u00015\u00015\u00015\u00035\u02f2"+
		"\b5\u00015\u00015\u00015\u00016\u00016\u00016\u00016\u00016\u00016\u0001"+
		"6\u00016\u00016\u00016\u00056\u0301\b6\n6\f6\u0304\t6\u00016\u00036\u0307"+
		"\b6\u00017\u00017\u00017\u00037\u030c\b7\u00017\u00017\u00017\u00018\u0001"+
		"8\u00018\u00018\u00018\u00018\u00018\u00018\u00018\u00038\u031a\b8\u0001"+
		"9\u00019\u00019\u00039\u031f\b9\u00019\u00019\u00019\u00019\u00019\u0001"+
		"9\u00039\u0327\b9\u00019\u00019\u00019\u00019\u00019\u00019\u00039\u032f"+
		"\b9\u00019\u00019\u00019\u00019\u00019\u00019\u00039\u0337\b9\u00019\u0001"+
		"9\u00019\u00019\u00019\u00019\u00039\u033f\b9\u00019\u00019\u00019\u0003"+
		"9\u0344\b9\u0001:\u0001:\u0001:\u0001:\u0001:\u0001:\u0001:\u0001;\u0001"+
		";\u0001;\u0001;\u0001;\u0001;\u0001<\u0001<\u0001<\u0001<\u0001<\u0001"+
		"<\u0001<\u0001<\u0001<\u0001<\u0001<\u0001<\u0001<\u0001<\u0001<\u0001"+
		"<\u0001<\u0001<\u0001<\u0001<\u0003<\u0367\b<\u0001=\u0001=\u0001=\u0001"+
		"=\u0001=\u0001=\u0001>\u0001>\u0001>\u0001>\u0001>\u0001?\u0001?\u0001"+
		"?\u0003?\u0377\b?\u0001?\u0001?\u0001?\u0001@\u0001@\u0001@\u0001@\u0001"+
		"@\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001"+
		"A\u0003A\u038b\bA\u0001B\u0001B\u0001B\u0003B\u0390\bB\u0001B\u0005B\u0393"+
		"\bB\nB\fB\u0396\tB\u0001B\u0001B\u0001C\u0001C\u0001C\u0001C\u0003C\u039e"+
		"\bC\u0001C\u0001C\u0005C\u03a2\bC\nC\fC\u03a5\tC\u0001C\u0003C\u03a8\b"+
		"C\u0001D\u0001D\u0001D\u0003D\u03ad\bD\u0001D\u0001D\u0001D\u0001D\u0001"+
		"D\u0001D\u0001D\u0003D\u03b6\bD\u0001D\u0001D\u0001D\u0003D\u03bb\bD\u0001"+
		"E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001"+
		"E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001"+
		"E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001"+
		"E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001"+
		"E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001"+
		"E\u0001E\u0003E\u03f1\bE\u0001F\u0001F\u0001F\u0001F\u0001F\u0001F\u0001"+
		"F\u0003F\u03fa\bF\u0001F\u0001F\u0001F\u0003F\u03ff\bF\u0001G\u0001G\u0001"+
		"G\u0003G\u0404\bG\u0001G\u0005G\u0407\bG\nG\fG\u040a\tG\u0001G\u0001G"+
		"\u0001G\u0001G\u0003G\u0410\bG\u0001G\u0001G\u0001G\u0001G\u0001G\u0003"+
		"G\u0417\bG\u0001G\u0001G\u0001G\u0001G\u0001G\u0003G\u041e\bG\u0001G\u0003"+
		"G\u0421\bG\u0001H\u0001H\u0001H\u0001H\u0001H\u0001I\u0005I\u0429\bI\n"+
		"I\fI\u042c\tI\u0001J\u0005J\u042f\bJ\nJ\fJ\u0432\tJ\u0001J\u0001J\u0004"+
		"J\u0436\bJ\u000bJ\fJ\u0437\u0001J\u0001J\u0003J\u043c\bJ\u0001K\u0001"+
		"K\u0001K\u0001K\u0005K\u0442\bK\nK\fK\u0445\tK\u0001K\u0003K\u0448\bK"+
		"\u0001K\u0000\u0000L\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014"+
		"\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfh"+
		"jlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090\u0092"+
		"\u0094\u0096\u0000\u0007\u0001\u0000\u0004\u0005\u0001\u0000\t\n\u0001"+
		"\u0000[\\\u0001\u0000\u0003\u0005\u0002\u0000\u0003\u0003zz\u0001\u0000"+
		"\u000f\u0010\u0001\u0000jk\u04d0\u0000\u0098\u0001\u0000\u0000\u0000\u0002"+
		"\u009a\u0001\u0000\u0000\u0000\u0004\u009c\u0001\u0000\u0000\u0000\u0006"+
		"\u009e\u0001\u0000\u0000\u0000\b\u00a0\u0001\u0000\u0000\u0000\n\u00a5"+
		"\u0001\u0000\u0000\u0000\f\u00a7\u0001\u0000\u0000\u0000\u000e\u00af\u0001"+
		"\u0000\u0000\u0000\u0010\u00b1\u0001\u0000\u0000\u0000\u0012\u00c6\u0001"+
		"\u0000\u0000\u0000\u0014\u00d4\u0001\u0000\u0000\u0000\u0016\u00d7\u0001"+
		"\u0000\u0000\u0000\u0018\u00da\u0001\u0000\u0000\u0000\u001a\u00e0\u0001"+
		"\u0000\u0000\u0000\u001c\u00e4\u0001\u0000\u0000\u0000\u001e\u00e9\u0001"+
		"\u0000\u0000\u0000 \u00eb\u0001\u0000\u0000\u0000\"\u00ed\u0001\u0000"+
		"\u0000\u0000$\u00f3\u0001\u0000\u0000\u0000&\u012d\u0001\u0000\u0000\u0000"+
		"(\u012f\u0001\u0000\u0000\u0000*\u0132\u0001\u0000\u0000\u0000,\u0135"+
		"\u0001\u0000\u0000\u0000.\u013c\u0001\u0000\u0000\u00000\u0141\u0001\u0000"+
		"\u0000\u00002\u0164\u0001\u0000\u0000\u00004\u017a\u0001\u0000\u0000\u0000"+
		"6\u01a8\u0001\u0000\u0000\u00008\u01aa\u0001\u0000\u0000\u0000:\u01bb"+
		"\u0001\u0000\u0000\u0000<\u01cc\u0001\u0000\u0000\u0000>\u01dd\u0001\u0000"+
		"\u0000\u0000@\u01ed\u0001\u0000\u0000\u0000B\u020b\u0001\u0000\u0000\u0000"+
		"D\u020d\u0001\u0000\u0000\u0000F\u0213\u0001\u0000\u0000\u0000H\u0217"+
		"\u0001\u0000\u0000\u0000J\u0233\u0001\u0000\u0000\u0000L\u0236\u0001\u0000"+
		"\u0000\u0000N\u0245\u0001\u0000\u0000\u0000P\u0255\u0001\u0000\u0000\u0000"+
		"R\u0272\u0001\u0000\u0000\u0000T\u0277\u0001\u0000\u0000\u0000V\u027d"+
		"\u0001\u0000\u0000\u0000X\u027f\u0001\u0000\u0000\u0000Z\u0294\u0001\u0000"+
		"\u0000\u0000\\\u0296\u0001\u0000\u0000\u0000^\u02a9\u0001\u0000\u0000"+
		"\u0000`\u02b4\u0001\u0000\u0000\u0000b\u02b6\u0001\u0000\u0000\u0000d"+
		"\u02c4\u0001\u0000\u0000\u0000f\u02de\u0001\u0000\u0000\u0000h\u02e0\u0001"+
		"\u0000\u0000\u0000j\u02ee\u0001\u0000\u0000\u0000l\u0306\u0001\u0000\u0000"+
		"\u0000n\u0308\u0001\u0000\u0000\u0000p\u0319\u0001\u0000\u0000\u0000r"+
		"\u0343\u0001\u0000\u0000\u0000t\u0345\u0001\u0000\u0000\u0000v\u034c\u0001"+
		"\u0000\u0000\u0000x\u0366\u0001\u0000\u0000\u0000z\u0368\u0001\u0000\u0000"+
		"\u0000|\u036e\u0001\u0000\u0000\u0000~\u0373\u0001\u0000\u0000\u0000\u0080"+
		"\u037b\u0001\u0000\u0000\u0000\u0082\u038a\u0001\u0000\u0000\u0000\u0084"+
		"\u038c\u0001\u0000\u0000\u0000\u0086\u03a7\u0001\u0000\u0000\u0000\u0088"+
		"\u03ba\u0001\u0000\u0000\u0000\u008a\u03f0\u0001\u0000\u0000\u0000\u008c"+
		"\u03fe\u0001\u0000\u0000\u0000\u008e\u0420\u0001\u0000\u0000\u0000\u0090"+
		"\u0422\u0001\u0000\u0000\u0000\u0092\u042a\u0001\u0000\u0000\u0000\u0094"+
		"\u043b\u0001\u0000\u0000\u0000\u0096\u0447\u0001\u0000\u0000\u0000\u0098"+
		"\u0099\u0007\u0000\u0000\u0000\u0099\u0001\u0001\u0000\u0000\u0000\u009a"+
		"\u009b\u0005\u0006\u0000\u0000\u009b\u0003\u0001\u0000\u0000\u0000\u009c"+
		"\u009d\u0005\u0007\u0000\u0000\u009d\u0005\u0001\u0000\u0000\u0000\u009e"+
		"\u009f\u0007\u0001\u0000\u0000\u009f\u0007\u0001\u0000\u0000\u0000\u00a0"+
		"\u00a1\u0005{\u0000\u0000\u00a1\t\u0001\u0000\u0000\u0000\u00a2\u00a6"+
		"\u0003\u0004\u0002\u0000\u00a3\u00a6\u0003\b\u0004\u0000\u00a4\u00a6\u0003"+
		"\u0006\u0003\u0000\u00a5\u00a2\u0001\u0000\u0000\u0000\u00a5\u00a3\u0001"+
		"\u0000\u0000\u0000\u00a5\u00a4\u0001\u0000\u0000\u0000\u00a6\u000b\u0001"+
		"\u0000\u0000\u0000\u00a7\u00a8\u0007\u0002\u0000\u0000\u00a8\r\u0001\u0000"+
		"\u0000\u0000\u00a9\u00b0\u0003\n\u0005\u0000\u00aa\u00ab\u0005\u0001\u0000"+
		"\u0000\u00ab\u00ac\u0005\u000b\u0000\u0000\u00ac\u00ad\u0003\n\u0005\u0000"+
		"\u00ad\u00ae\u0005\u0002\u0000\u0000\u00ae\u00b0\u0001\u0000\u0000\u0000"+
		"\u00af\u00a9\u0001\u0000\u0000\u0000\u00af\u00aa\u0001\u0000\u0000\u0000"+
		"\u00b0\u000f\u0001\u0000\u0000\u0000\u00b1\u00b2\u0005\u0001\u0000\u0000"+
		"\u00b2\u00b3\u0005[\u0000\u0000\u00b3\u00b4\u0003\u0016\u000b\u0000\u00b4"+
		"\u00b5\u0005\u0002\u0000\u0000\u00b5\u0011\u0001\u0000\u0000\u0000\u00b6"+
		"\u00b7\u0005\u0001\u0000\u0000\u00b7\u00c1\u0005^\u0000\u0000\u00b8\u00ba"+
		"\u0003\n\u0005\u0000\u00b9\u00b8\u0001\u0000\u0000\u0000\u00ba\u00bd\u0001"+
		"\u0000\u0000\u0000\u00bb\u00b9\u0001\u0000\u0000\u0000\u00bb\u00bc\u0001"+
		"\u0000\u0000\u0000\u00bc\u00c2\u0001\u0000\u0000\u0000\u00bd\u00bb\u0001"+
		"\u0000\u0000\u0000\u00be\u00bf\u0003\"\u0011\u0000\u00bf\u00c0\u0003\n"+
		"\u0005\u0000\u00c0\u00c2\u0001\u0000\u0000\u0000\u00c1\u00bb\u0001\u0000"+
		"\u0000\u0000\u00c1\u00be\u0001\u0000\u0000\u0000\u00c2\u00c3\u0001\u0000"+
		"\u0000\u0000\u00c3\u00c5\u0005\u0002\u0000\u0000\u00c4\u00b6\u0001\u0000"+
		"\u0000\u0000\u00c5\u00c8\u0001\u0000\u0000\u0000\u00c6\u00c4\u0001\u0000"+
		"\u0000\u0000\u00c6\u00c7\u0001\u0000\u0000\u0000\u00c7\u0013\u0001\u0000"+
		"\u0000\u0000\u00c8\u00c6\u0001\u0000\u0000\u0000\u00c9\u00ca\u0005\u0001"+
		"\u0000\u0000\u00ca\u00ce\u0005_\u0000\u0000\u00cb\u00cd\u0003\n\u0005"+
		"\u0000\u00cc\u00cb\u0001\u0000\u0000\u0000\u00cd\u00d0\u0001\u0000\u0000"+
		"\u0000\u00ce\u00cc\u0001\u0000\u0000\u0000\u00ce\u00cf\u0001\u0000\u0000"+
		"\u0000\u00cf\u00d1\u0001\u0000\u0000\u0000\u00d0\u00ce\u0001\u0000\u0000"+
		"\u0000\u00d1\u00d3\u0005\u0002\u0000\u0000\u00d2\u00c9\u0001\u0000\u0000"+
		"\u0000\u00d3\u00d6\u0001\u0000\u0000\u0000\u00d4\u00d2\u0001\u0000\u0000"+
		"\u0000\u00d4\u00d5\u0001\u0000\u0000\u0000\u00d5\u0015\u0001\u0000\u0000"+
		"\u0000\u00d6\u00d4\u0001\u0000\u0000\u0000\u00d7\u00d8\u0003\u0012\t\u0000"+
		"\u00d8\u00d9\u0003\u0014\n\u0000\u00d9\u0017\u0001\u0000\u0000\u0000\u00da"+
		"\u00dc\u0005\u0003\u0000\u0000\u00db\u00dd\u0005\u0003\u0000\u0000\u00dc"+
		"\u00db\u0001\u0000\u0000\u0000\u00dc\u00dd\u0001\u0000\u0000\u0000\u00dd"+
		"\u00de\u0001\u0000\u0000\u0000\u00de\u00df\u0003\u0006\u0003\u0000\u00df"+
		"\u0019\u0001\u0000\u0000\u0000\u00e0\u00e2\u0005\u0003\u0000\u0000\u00e1"+
		"\u00e3\u0005\u0003\u0000\u0000\u00e2\u00e1\u0001\u0000\u0000\u0000\u00e2"+
		"\u00e3\u0001\u0000\u0000\u0000\u00e3\u001b\u0001\u0000\u0000\u0000\u00e4"+
		"\u00e5\u0005\u0001\u0000\u0000\u00e5\u00e6\u0005Z\u0000\u0000\u00e6\u00e7"+
		"\u0003 \u0010\u0000\u00e7\u00e8\u0005\u0002\u0000\u0000\u00e8\u001d\u0001"+
		"\u0000\u0000\u0000\u00e9\u00ea\u0007\u0003\u0000\u0000\u00ea\u001f\u0001"+
		"\u0000\u0000\u0000\u00eb\u00ec\u0007\u0004\u0000\u0000\u00ec!\u0001\u0000"+
		"\u0000\u0000\u00ed\u00ee\u0005z\u0000\u0000\u00ee#\u0001\u0000\u0000\u0000"+
		"\u00ef\u00f4\u0003&\u0013\u0000\u00f0\u00f4\u0003<\u001e\u0000\u00f1\u00f4"+
		"\u0003B!\u0000\u00f2\u00f4\u0003H$\u0000\u00f3\u00ef\u0001\u0000\u0000"+
		"\u0000\u00f3\u00f0\u0001\u0000\u0000\u0000\u00f3\u00f1\u0001\u0000\u0000"+
		"\u0000\u00f3\u00f2\u0001\u0000\u0000\u0000\u00f4%\u0001\u0000\u0000\u0000"+
		"\u00f5\u012e\u0005\r\u0000\u0000\u00f6\u012e\u0005\f\u0000\u0000\u00f7"+
		"\u012e\u0005\u000e\u0000\u0000\u00f8\u012e\u0005\u0019\u0000\u0000\u00f9"+
		"\u00fa\u0005\u0012\u0000\u0000\u00fa\u012e\u0003 \u0010\u0000\u00fb\u00fc"+
		"\u0005\u0013\u0000\u0000\u00fc\u012e\u0003 \u0010\u0000\u00fd\u00ff\u0005"+
		"\u0014\u0000\u0000\u00fe\u0100\u0003 \u0010\u0000\u00ff\u00fe\u0001\u0000"+
		"\u0000\u0000\u0100\u0101\u0001\u0000\u0000\u0000\u0101\u00ff\u0001\u0000"+
		"\u0000\u0000\u0101\u0102\u0001\u0000\u0000\u0000\u0102\u012e\u0001\u0000"+
		"\u0000\u0000\u0103\u012e\u0005\u0015\u0000\u0000\u0104\u0105\u0005\u001a"+
		"\u0000\u0000\u0105\u012e\u0003 \u0010\u0000\u0106\u0107\u0005\u001c\u0000"+
		"\u0000\u0107\u012e\u0003 \u0010\u0000\u0108\u0109\u0005\u001d\u0000\u0000"+
		"\u0109\u012e\u0003 \u0010\u0000\u010a\u010b\u0005\u001e\u0000\u0000\u010b"+
		"\u012e\u0003 \u0010\u0000\u010c\u010d\u0005\u001f\u0000\u0000\u010d\u012e"+
		"\u0003 \u0010\u0000\u010e\u010f\u0005 \u0000\u0000\u010f\u012e\u0003 "+
		"\u0010\u0000\u0110\u0112\u0003,\u0016\u0000\u0111\u0113\u0003(\u0014\u0000"+
		"\u0112\u0111\u0001\u0000\u0000\u0000\u0112\u0113\u0001\u0000\u0000\u0000"+
		"\u0113\u0115\u0001\u0000\u0000\u0000\u0114\u0116\u0003*\u0015\u0000\u0115"+
		"\u0114\u0001\u0000\u0000\u0000\u0115\u0116\u0001\u0000\u0000\u0000\u0116"+
		"\u012e\u0001\u0000\u0000\u0000\u0117\u0119\u0003.\u0017\u0000\u0118\u011a"+
		"\u0003(\u0014\u0000\u0119\u0118\u0001\u0000\u0000\u0000\u0119\u011a\u0001"+
		"\u0000\u0000\u0000\u011a\u011c\u0001\u0000\u0000\u0000\u011b\u011d\u0003"+
		"*\u0015\u0000\u011c\u011b\u0001\u0000\u0000\u0000\u011c\u011d\u0001\u0000"+
		"\u0000\u0000\u011d\u012e\u0001\u0000\u0000\u0000\u011e\u012e\u0005U\u0000"+
		"\u0000\u011f\u012e\u0005V\u0000\u0000\u0120\u012e\u0005W\u0000\u0000\u0121"+
		"\u012e\u0005X\u0000\u0000\u0122\u0123\u0005Y\u0000\u0000\u0123\u012e\u0003"+
		" \u0010\u0000\u0124\u0125\u0003\u0004\u0002\u0000\u0125\u0126\u0005\b"+
		"\u0000\u0000\u0126\u0127\u0003\u001e\u000f\u0000\u0127\u012e\u0001\u0000"+
		"\u0000\u0000\u0128\u012e\u00030\u0018\u0000\u0129\u012e\u00032\u0019\u0000"+
		"\u012a\u012e\u00034\u001a\u0000\u012b\u012e\u00036\u001b\u0000\u012c\u012e"+
		"\u0005T\u0000\u0000\u012d\u00f5\u0001\u0000\u0000\u0000\u012d\u00f6\u0001"+
		"\u0000\u0000\u0000\u012d\u00f7\u0001\u0000\u0000\u0000\u012d\u00f8\u0001"+
		"\u0000\u0000\u0000\u012d\u00f9\u0001\u0000\u0000\u0000\u012d\u00fb\u0001"+
		"\u0000\u0000\u0000\u012d\u00fd\u0001\u0000\u0000\u0000\u012d\u0103\u0001"+
		"\u0000\u0000\u0000\u012d\u0104\u0001\u0000\u0000\u0000\u012d\u0106\u0001"+
		"\u0000\u0000\u0000\u012d\u0108\u0001\u0000\u0000\u0000\u012d\u010a\u0001"+
		"\u0000\u0000\u0000\u012d\u010c\u0001\u0000\u0000\u0000\u012d\u010e\u0001"+
		"\u0000\u0000\u0000\u012d\u0110\u0001\u0000\u0000\u0000\u012d\u0117\u0001"+
		"\u0000\u0000\u0000\u012d\u011e\u0001\u0000\u0000\u0000\u012d\u011f\u0001"+
		"\u0000\u0000\u0000\u012d\u0120\u0001\u0000\u0000\u0000\u012d\u0121\u0001"+
		"\u0000\u0000\u0000\u012d\u0122\u0001\u0000\u0000\u0000\u012d\u0124\u0001"+
		"\u0000\u0000\u0000\u012d\u0128\u0001\u0000\u0000\u0000\u012d\u0129\u0001"+
		"\u0000\u0000\u0000\u012d\u012a\u0001\u0000\u0000\u0000\u012d\u012b\u0001"+
		"\u0000\u0000\u0000\u012d\u012c\u0001\u0000\u0000\u0000\u012e\'\u0001\u0000"+
		"\u0000\u0000\u012f\u0130\u0005$\u0000\u0000\u0130\u0131\u0005\u0003\u0000"+
		"\u0000\u0131)\u0001\u0000\u0000\u0000\u0132\u0133\u0005%\u0000\u0000\u0133"+
		"\u0134\u0005\u0003\u0000\u0000\u0134+\u0001\u0000\u0000\u0000\u0135\u0136"+
		"\u0003\u0004\u0002\u0000\u0136\u013a\u0005!\u0000\u0000\u0137\u0138\u0005"+
		"\'\u0000\u0000\u0138\u0139\u0005#\u0000\u0000\u0139\u013b\u0005&\u0000"+
		"\u0000\u013a\u0137\u0001\u0000\u0000\u0000\u013a\u013b\u0001\u0000\u0000"+
		"\u0000\u013b-\u0001\u0000\u0000\u0000\u013c\u013d\u0003\u0004\u0002\u0000"+
		"\u013d\u013f\u0005\"\u0000\u0000\u013e\u0140\u0005\'\u0000\u0000\u013f"+
		"\u013e\u0001\u0000\u0000\u0000\u013f\u0140\u0001\u0000\u0000\u0000\u0140"+
		"/\u0001\u0000\u0000\u0000\u0141\u0142\u0005~\u0000\u0000\u0142\u0143\u0005"+
		"(\u0000\u0000\u01431\u0001\u0000\u0000\u0000\u0144\u0145\u0005~\u0000"+
		"\u0000\u0145\u0165\u0005)\u0000\u0000\u0146\u0147\u0005~\u0000\u0000\u0147"+
		"\u0165\u0005*\u0000\u0000\u0148\u0149\u0005~\u0000\u0000\u0149\u0165\u0005"+
		",\u0000\u0000\u014a\u014b\u0005~\u0000\u0000\u014b\u0165\u0005-\u0000"+
		"\u0000\u014c\u014d\u0005~\u0000\u0000\u014d\u0165\u0005/\u0000\u0000\u014e"+
		"\u014f\u0005~\u0000\u0000\u014f\u0165\u00050\u0000\u0000\u0150\u0151\u0005"+
		"~\u0000\u0000\u0151\u0165\u00052\u0000\u0000\u0152\u0153\u0005~\u0000"+
		"\u0000\u0153\u0165\u00053\u0000\u0000\u0154\u0155\u0005~\u0000\u0000\u0155"+
		"\u0165\u00055\u0000\u0000\u0156\u0157\u0005~\u0000\u0000\u0157\u0165\u0005"+
		"6\u0000\u0000\u0158\u0159\u0005\u007f\u0000\u0000\u0159\u0165\u0005)\u0000"+
		"\u0000\u015a\u015b\u0005\u007f\u0000\u0000\u015b\u0165\u0005*\u0000\u0000"+
		"\u015c\u015d\u0005\u007f\u0000\u0000\u015d\u0165\u0005+\u0000\u0000\u015e"+
		"\u015f\u0005\u007f\u0000\u0000\u015f\u0165\u0005.\u0000\u0000\u0160\u0161"+
		"\u0005\u007f\u0000\u0000\u0161\u0165\u00051\u0000\u0000\u0162\u0163\u0005"+
		"\u007f\u0000\u0000\u0163\u0165\u00054\u0000\u0000\u0164\u0144\u0001\u0000"+
		"\u0000\u0000\u0164\u0146\u0001\u0000\u0000\u0000\u0164\u0148\u0001\u0000"+
		"\u0000\u0000\u0164\u014a\u0001\u0000\u0000\u0000\u0164\u014c\u0001\u0000"+
		"\u0000\u0000\u0164\u014e\u0001\u0000\u0000\u0000\u0164\u0150\u0001\u0000"+
		"\u0000\u0000\u0164\u0152\u0001\u0000\u0000\u0000\u0164\u0154\u0001\u0000"+
		"\u0000\u0000\u0164\u0156\u0001\u0000\u0000\u0000\u0164\u0158\u0001\u0000"+
		"\u0000\u0000\u0164\u015a\u0001\u0000\u0000\u0000\u0164\u015c\u0001\u0000"+
		"\u0000\u0000\u0164\u015e\u0001\u0000\u0000\u0000\u0164\u0160\u0001\u0000"+
		"\u0000\u0000\u0164\u0162\u0001\u0000\u0000\u0000\u01653\u0001\u0000\u0000"+
		"\u0000\u0166\u0167\u0005~\u0000\u0000\u0167\u017b\u00057\u0000\u0000\u0168"+
		"\u0169\u0005~\u0000\u0000\u0169\u017b\u00058\u0000\u0000\u016a\u016b\u0005"+
		"~\u0000\u0000\u016b\u017b\u00059\u0000\u0000\u016c\u016d\u0005\u007f\u0000"+
		"\u0000\u016d\u017b\u0005:\u0000\u0000\u016e\u016f\u0005\u007f\u0000\u0000"+
		"\u016f\u017b\u0005;\u0000\u0000\u0170\u0171\u0005\u007f\u0000\u0000\u0171"+
		"\u017b\u0005<\u0000\u0000\u0172\u0173\u0005\u007f\u0000\u0000\u0173\u017b"+
		"\u0005=\u0000\u0000\u0174\u0175\u0005\u007f\u0000\u0000\u0175\u017b\u0005"+
		">\u0000\u0000\u0176\u0177\u0005\u007f\u0000\u0000\u0177\u017b\u0005?\u0000"+
		"\u0000\u0178\u0179\u0005\u007f\u0000\u0000\u0179\u017b\u0005@\u0000\u0000"+
		"\u017a\u0166\u0001\u0000\u0000\u0000\u017a\u0168\u0001\u0000\u0000\u0000"+
		"\u017a\u016a\u0001\u0000\u0000\u0000\u017a\u016c\u0001\u0000\u0000\u0000"+
		"\u017a\u016e\u0001\u0000\u0000\u0000\u017a\u0170\u0001\u0000\u0000\u0000"+
		"\u017a\u0172\u0001\u0000\u0000\u0000\u017a\u0174\u0001\u0000\u0000\u0000"+
		"\u017a\u0176\u0001\u0000\u0000\u0000\u017a\u0178\u0001\u0000\u0000\u0000"+
		"\u017b5\u0001\u0000\u0000\u0000\u017c\u017d\u0005~\u0000\u0000\u017d\u01a9"+
		"\u0005A\u0000\u0000\u017e\u017f\u0005~\u0000\u0000\u017f\u01a9\u0005B"+
		"\u0000\u0000\u0180\u0181\u0005~\u0000\u0000\u0181\u01a9\u0005C\u0000\u0000"+
		"\u0182\u0183\u0005~\u0000\u0000\u0183\u01a9\u0005E\u0000\u0000\u0184\u0185"+
		"\u0005~\u0000\u0000\u0185\u01a9\u0005F\u0000\u0000\u0186\u0187\u0005~"+
		"\u0000\u0000\u0187\u01a9\u0005G\u0000\u0000\u0188\u0189\u0005~\u0000\u0000"+
		"\u0189\u01a9\u0005H\u0000\u0000\u018a\u018b\u0005~\u0000\u0000\u018b\u01a9"+
		"\u0005I\u0000\u0000\u018c\u018d\u0005~\u0000\u0000\u018d\u01a9\u0005J"+
		"\u0000\u0000\u018e\u018f\u0005~\u0000\u0000\u018f\u01a9\u0005K\u0000\u0000"+
		"\u0190\u0191\u0005~\u0000\u0000\u0191\u01a9\u0005L\u0000\u0000\u0192\u0193"+
		"\u0005~\u0000\u0000\u0193\u01a9\u0005M\u0000\u0000\u0194\u0195\u0005~"+
		"\u0000\u0000\u0195\u01a9\u0005N\u0000\u0000\u0196\u0197\u0005~\u0000\u0000"+
		"\u0197\u01a9\u0005O\u0000\u0000\u0198\u0199\u0005~\u0000\u0000\u0199\u01a9"+
		"\u0005P\u0000\u0000\u019a\u019b\u0005\u007f\u0000\u0000\u019b\u01a9\u0005"+
		"A\u0000\u0000\u019c\u019d\u0005\u007f\u0000\u0000\u019d\u01a9\u0005B\u0000"+
		"\u0000\u019e\u019f\u0005\u007f\u0000\u0000\u019f\u01a9\u0005C\u0000\u0000"+
		"\u01a0\u01a1\u0005\u007f\u0000\u0000\u01a1\u01a9\u0005D\u0000\u0000\u01a2"+
		"\u01a3\u0005\u007f\u0000\u0000\u01a3\u01a9\u0005Q\u0000\u0000\u01a4\u01a5"+
		"\u0005\u007f\u0000\u0000\u01a5\u01a9\u0005R\u0000\u0000\u01a6\u01a7\u0005"+
		"\u007f\u0000\u0000\u01a7\u01a9\u0005S\u0000\u0000\u01a8\u017c\u0001\u0000"+
		"\u0000\u0000\u01a8\u017e\u0001\u0000\u0000\u0000\u01a8\u0180\u0001\u0000"+
		"\u0000\u0000\u01a8\u0182\u0001\u0000\u0000\u0000\u01a8\u0184\u0001\u0000"+
		"\u0000\u0000\u01a8\u0186\u0001\u0000\u0000\u0000\u01a8\u0188\u0001\u0000"+
		"\u0000\u0000\u01a8\u018a\u0001\u0000\u0000\u0000\u01a8\u018c\u0001\u0000"+
		"\u0000\u0000\u01a8\u018e\u0001\u0000\u0000\u0000\u01a8\u0190\u0001\u0000"+
		"\u0000\u0000\u01a8\u0192\u0001\u0000\u0000\u0000\u01a8\u0194\u0001\u0000"+
		"\u0000\u0000\u01a8\u0196\u0001\u0000\u0000\u0000\u01a8\u0198\u0001\u0000"+
		"\u0000\u0000\u01a8\u019a\u0001\u0000\u0000\u0000\u01a8\u019c\u0001\u0000"+
		"\u0000\u0000\u01a8\u019e\u0001\u0000\u0000\u0000\u01a8\u01a0\u0001\u0000"+
		"\u0000\u0000\u01a8\u01a2\u0001\u0000\u0000\u0000\u01a8\u01a4\u0001\u0000"+
		"\u0000\u0000\u01a8\u01a6\u0001\u0000\u0000\u0000\u01a97\u0001\u0000\u0000"+
		"\u0000\u01aa\u01ac\u0005\u001b\u0000\u0000\u01ab\u01ad\u0003\u001c\u000e"+
		"\u0000\u01ac\u01ab\u0001\u0000\u0000\u0000\u01ac\u01ad\u0001\u0000\u0000"+
		"\u0000\u01ad\u01ae\u0001\u0000\u0000\u0000\u01ae\u01af\u0003:\u001d\u0000"+
		"\u01af9\u0001\u0000\u0000\u0000\u01b0\u01b1\u0005\u0001\u0000\u0000\u01b1"+
		"\u01b5\u0005^\u0000\u0000\u01b2\u01b4\u0003\n\u0005\u0000\u01b3\u01b2"+
		"\u0001\u0000\u0000\u0000\u01b4\u01b7\u0001\u0000\u0000\u0000\u01b5\u01b3"+
		"\u0001\u0000\u0000\u0000\u01b5\u01b6\u0001\u0000\u0000\u0000\u01b6\u01b8"+
		"\u0001\u0000\u0000\u0000\u01b7\u01b5\u0001\u0000\u0000\u0000\u01b8\u01ba"+
		"\u0005\u0002\u0000\u0000\u01b9\u01b0\u0001\u0000\u0000\u0000\u01ba\u01bd"+
		"\u0001\u0000\u0000\u0000\u01bb\u01b9\u0001\u0000\u0000\u0000\u01bb\u01bc"+
		"\u0001\u0000\u0000\u0000\u01bc\u01c9\u0001\u0000\u0000\u0000\u01bd\u01bb"+
		"\u0001\u0000\u0000\u0000\u01be\u01bf\u0005\u0001\u0000\u0000\u01bf\u01c3"+
		"\u0005_\u0000\u0000\u01c0\u01c2\u0003\n\u0005\u0000\u01c1\u01c0\u0001"+
		"\u0000\u0000\u0000\u01c2\u01c5\u0001\u0000\u0000\u0000\u01c3\u01c1\u0001"+
		"\u0000\u0000\u0000\u01c3\u01c4\u0001\u0000\u0000\u0000\u01c4\u01c6\u0001"+
		"\u0000\u0000\u0000\u01c5\u01c3\u0001\u0000\u0000\u0000\u01c6\u01c8\u0005"+
		"\u0002\u0000\u0000\u01c7\u01be\u0001\u0000\u0000\u0000\u01c8\u01cb\u0001"+
		"\u0000\u0000\u0000\u01c9\u01c7\u0001\u0000\u0000\u0000\u01c9\u01ca\u0001"+
		"\u0000\u0000\u0000\u01ca;\u0001\u0000\u0000\u0000\u01cb\u01c9\u0001\u0000"+
		"\u0000\u0000\u01cc\u01ce\u0005\u001b\u0000\u0000\u01cd\u01cf\u0003\u001c"+
		"\u000e\u0000\u01ce\u01cd\u0001\u0000\u0000\u0000\u01ce\u01cf\u0001\u0000"+
		"\u0000\u0000\u01cf\u01d0\u0001\u0000\u0000\u0000\u01d0\u01d1\u0003>\u001f"+
		"\u0000\u01d1=\u0001\u0000\u0000\u0000\u01d2\u01d3\u0005\u0001\u0000\u0000"+
		"\u01d3\u01d7\u0005^\u0000\u0000\u01d4\u01d6\u0003\n\u0005\u0000\u01d5"+
		"\u01d4\u0001\u0000\u0000\u0000\u01d6\u01d9\u0001\u0000\u0000\u0000\u01d7"+
		"\u01d5\u0001\u0000\u0000\u0000\u01d7\u01d8\u0001\u0000\u0000\u0000\u01d8"+
		"\u01da\u0001\u0000\u0000\u0000\u01d9\u01d7\u0001\u0000\u0000\u0000\u01da"+
		"\u01dc\u0005\u0002\u0000\u0000\u01db\u01d2\u0001\u0000\u0000\u0000\u01dc"+
		"\u01df\u0001\u0000\u0000\u0000\u01dd\u01db\u0001\u0000\u0000\u0000\u01dd"+
		"\u01de\u0001\u0000\u0000\u0000\u01de\u01e0\u0001\u0000\u0000\u0000\u01df"+
		"\u01dd\u0001\u0000\u0000\u0000\u01e0\u01e1\u0003@ \u0000\u01e1?\u0001"+
		"\u0000\u0000\u0000\u01e2\u01e3\u0005\u0001\u0000\u0000\u01e3\u01e7\u0005"+
		"_\u0000\u0000\u01e4\u01e6\u0003\n\u0005\u0000\u01e5\u01e4\u0001\u0000"+
		"\u0000\u0000\u01e6\u01e9\u0001\u0000\u0000\u0000\u01e7\u01e5\u0001\u0000"+
		"\u0000\u0000\u01e7\u01e8\u0001\u0000\u0000\u0000\u01e8\u01ea\u0001\u0000"+
		"\u0000\u0000\u01e9\u01e7\u0001\u0000\u0000\u0000\u01ea\u01ec\u0005\u0002"+
		"\u0000\u0000\u01eb\u01e2\u0001\u0000\u0000\u0000\u01ec\u01ef\u0001\u0000"+
		"\u0000\u0000\u01ed\u01eb\u0001\u0000\u0000\u0000\u01ed\u01ee\u0001\u0000"+
		"\u0000\u0000\u01ee\u01f0\u0001\u0000\u0000\u0000\u01ef\u01ed\u0001\u0000"+
		"\u0000\u0000\u01f0\u01f1\u0003$\u0012\u0000\u01f1A\u0001\u0000\u0000\u0000"+
		"\u01f2\u01f4\u0007\u0005\u0000\u0000\u01f3\u01f5\u0003\"\u0011\u0000\u01f4"+
		"\u01f3\u0001\u0000\u0000\u0000\u01f4\u01f5\u0001\u0000\u0000\u0000\u01f5"+
		"\u01f6\u0001\u0000\u0000\u0000\u01f6\u01f7\u0003F#\u0000\u01f7\u01f9\u0005"+
		"\u0011\u0000\u0000\u01f8\u01fa\u0003\"\u0011\u0000\u01f9\u01f8\u0001\u0000"+
		"\u0000\u0000\u01f9\u01fa\u0001\u0000\u0000\u0000\u01fa\u020c\u0001\u0000"+
		"\u0000\u0000\u01fb\u01fd\u0005\u0016\u0000\u0000\u01fc\u01fe\u0003\"\u0011"+
		"\u0000\u01fd\u01fc\u0001\u0000\u0000\u0000\u01fd\u01fe\u0001\u0000\u0000"+
		"\u0000\u01fe\u01ff\u0001\u0000\u0000\u0000\u01ff\u0205\u0003F#\u0000\u0200"+
		"\u0202\u0005\u0018\u0000\u0000\u0201\u0203\u0003\"\u0011\u0000\u0202\u0201"+
		"\u0001\u0000\u0000\u0000\u0202\u0203\u0001\u0000\u0000\u0000\u0203\u0204"+
		"\u0001\u0000\u0000\u0000\u0204\u0206\u0003T*\u0000\u0205\u0200\u0001\u0000"+
		"\u0000\u0000\u0205\u0206\u0001\u0000\u0000\u0000\u0206\u0207\u0001\u0000"+
		"\u0000\u0000\u0207\u0209\u0005\u0011\u0000\u0000\u0208\u020a\u0003\"\u0011"+
		"\u0000\u0209\u0208\u0001\u0000\u0000\u0000\u0209\u020a\u0001\u0000\u0000"+
		"\u0000\u020a\u020c\u0001\u0000\u0000\u0000\u020b\u01f2\u0001\u0000\u0000"+
		"\u0000\u020b\u01fb\u0001\u0000\u0000\u0000\u020cC\u0001\u0000\u0000\u0000"+
		"\u020d\u020e\u0005\u0001\u0000\u0000\u020e\u020f\u0005_\u0000\u0000\u020f"+
		"\u0210\u0003\n\u0005\u0000\u0210\u0211\u0005\u0002\u0000\u0000\u0211E"+
		"\u0001\u0000\u0000\u0000\u0212\u0214\u0003D\"\u0000\u0213\u0212\u0001"+
		"\u0000\u0000\u0000\u0213\u0214\u0001\u0000\u0000\u0000\u0214\u0215\u0001"+
		"\u0000\u0000\u0000\u0215\u0216\u0003T*\u0000\u0216G\u0001\u0000\u0000"+
		"\u0000\u0217\u0218\u0005\u0001\u0000\u0000\u0218\u0219\u0003J%\u0000\u0219"+
		"\u021a\u0005\u0002\u0000\u0000\u021aI\u0001\u0000\u0000\u0000\u021b\u021f"+
		"\u0003&\u0013\u0000\u021c\u021e\u0003H$\u0000\u021d\u021c\u0001\u0000"+
		"\u0000\u0000\u021e\u0221\u0001\u0000\u0000\u0000\u021f\u021d\u0001\u0000"+
		"\u0000\u0000\u021f\u0220\u0001\u0000\u0000\u0000\u0220\u0234\u0001\u0000"+
		"\u0000\u0000\u0221\u021f\u0001\u0000\u0000\u0000\u0222\u0223\u0005\u001b"+
		"\u0000\u0000\u0223\u0234\u0003L&\u0000\u0224\u0226\u0005\u000f\u0000\u0000"+
		"\u0225\u0227\u0003\"\u0011\u0000\u0226\u0225\u0001\u0000\u0000\u0000\u0226"+
		"\u0227\u0001\u0000\u0000\u0000\u0227\u0228\u0001\u0000\u0000\u0000\u0228"+
		"\u0234\u0003F#\u0000\u0229\u022b\u0005\u0010\u0000\u0000\u022a\u022c\u0003"+
		"\"\u0011\u0000\u022b\u022a\u0001\u0000\u0000\u0000\u022b\u022c\u0001\u0000"+
		"\u0000\u0000\u022c\u022d\u0001\u0000\u0000\u0000\u022d\u0234\u0003F#\u0000"+
		"\u022e\u0230\u0005\u0016\u0000\u0000\u022f\u0231\u0003\"\u0011\u0000\u0230"+
		"\u022f\u0001\u0000\u0000\u0000\u0230\u0231\u0001\u0000\u0000\u0000\u0231"+
		"\u0232\u0001\u0000\u0000\u0000\u0232\u0234\u0003R)\u0000\u0233\u021b\u0001"+
		"\u0000\u0000\u0000\u0233\u0222\u0001\u0000\u0000\u0000\u0233\u0224\u0001"+
		"\u0000\u0000\u0000\u0233\u0229\u0001\u0000\u0000\u0000\u0233\u022e\u0001"+
		"\u0000\u0000\u0000\u0234K\u0001\u0000\u0000\u0000\u0235\u0237\u0003\u001c"+
		"\u000e\u0000\u0236\u0235\u0001\u0000\u0000\u0000\u0236\u0237\u0001\u0000"+
		"\u0000\u0000\u0237\u0238\u0001\u0000\u0000\u0000\u0238\u0239\u0003N\'"+
		"\u0000\u0239M\u0001\u0000\u0000\u0000\u023a\u023b\u0005\u0001\u0000\u0000"+
		"\u023b\u023f\u0005^\u0000\u0000\u023c\u023e\u0003\n\u0005\u0000\u023d"+
		"\u023c\u0001\u0000\u0000\u0000\u023e\u0241\u0001\u0000\u0000\u0000\u023f"+
		"\u023d\u0001\u0000\u0000\u0000\u023f\u0240\u0001\u0000\u0000\u0000\u0240"+
		"\u0242\u0001\u0000\u0000\u0000\u0241\u023f\u0001\u0000\u0000\u0000\u0242"+
		"\u0244\u0005\u0002\u0000\u0000\u0243\u023a\u0001\u0000\u0000\u0000\u0244"+
		"\u0247\u0001\u0000\u0000\u0000\u0245\u0243\u0001\u0000\u0000\u0000\u0245"+
		"\u0246\u0001\u0000\u0000\u0000\u0246\u0248\u0001\u0000\u0000\u0000\u0247"+
		"\u0245\u0001\u0000\u0000\u0000\u0248\u0249\u0003P(\u0000\u0249O\u0001"+
		"\u0000\u0000\u0000\u024a\u024b\u0005\u0001\u0000\u0000\u024b\u024f\u0005"+
		"_\u0000\u0000\u024c\u024e\u0003\n\u0005\u0000\u024d\u024c\u0001\u0000"+
		"\u0000\u0000\u024e\u0251\u0001\u0000\u0000\u0000\u024f\u024d\u0001\u0000"+
		"\u0000\u0000\u024f\u0250\u0001\u0000\u0000\u0000\u0250\u0252\u0001\u0000"+
		"\u0000\u0000\u0251\u024f\u0001\u0000\u0000\u0000\u0252\u0254\u0005\u0002"+
		"\u0000\u0000\u0253\u024a\u0001\u0000\u0000\u0000\u0254\u0257\u0001\u0000"+
		"\u0000\u0000\u0255\u0253\u0001\u0000\u0000\u0000\u0255\u0256\u0001\u0000"+
		"\u0000\u0000\u0256\u025b\u0001\u0000\u0000\u0000\u0257\u0255\u0001\u0000"+
		"\u0000\u0000\u0258\u025a\u0003H$\u0000\u0259\u0258\u0001\u0000\u0000\u0000"+
		"\u025a\u025d\u0001\u0000\u0000\u0000\u025b\u0259\u0001\u0000\u0000\u0000"+
		"\u025b\u025c\u0001\u0000\u0000\u0000\u025cQ\u0001\u0000\u0000\u0000\u025d"+
		"\u025b\u0001\u0000\u0000\u0000\u025e\u025f\u0003D\"\u0000\u025f\u0260"+
		"\u0003R)\u0000\u0260\u0273\u0001\u0000\u0000\u0000\u0261\u0263\u0003H"+
		"$\u0000\u0262\u0261\u0001\u0000\u0000\u0000\u0263\u0266\u0001\u0000\u0000"+
		"\u0000\u0264\u0262\u0001\u0000\u0000\u0000\u0264\u0265\u0001\u0000\u0000"+
		"\u0000\u0265\u0267\u0001\u0000\u0000\u0000\u0266\u0264\u0001\u0000\u0000"+
		"\u0000\u0267\u0268\u0005\u0001\u0000\u0000\u0268\u0269\u0005\u0017\u0000"+
		"\u0000\u0269\u026a\u0003T*\u0000\u026a\u0270\u0005\u0002\u0000\u0000\u026b"+
		"\u026c\u0005\u0001\u0000\u0000\u026c\u026d\u0005\u0018\u0000\u0000\u026d"+
		"\u026e\u0003T*\u0000\u026e\u026f\u0005\u0002\u0000\u0000\u026f\u0271\u0001"+
		"\u0000\u0000\u0000\u0270\u026b\u0001\u0000\u0000\u0000\u0270\u0271\u0001"+
		"\u0000\u0000\u0000\u0271\u0273\u0001\u0000\u0000\u0000\u0272\u025e\u0001"+
		"\u0000\u0000\u0000\u0272\u0264\u0001\u0000\u0000\u0000\u0273S\u0001\u0000"+
		"\u0000\u0000\u0274\u0276\u0003$\u0012\u0000\u0275\u0274\u0001\u0000\u0000"+
		"\u0000\u0276\u0279\u0001\u0000\u0000\u0000\u0277\u0275\u0001\u0000\u0000"+
		"\u0000\u0277\u0278\u0001\u0000\u0000\u0000\u0278\u027b\u0001\u0000\u0000"+
		"\u0000\u0279\u0277\u0001\u0000\u0000\u0000\u027a\u027c\u00038\u001c\u0000"+
		"\u027b\u027a\u0001\u0000\u0000\u0000\u027b\u027c\u0001\u0000\u0000\u0000"+
		"\u027cU\u0001\u0000\u0000\u0000\u027d\u027e\u0003T*\u0000\u027eW\u0001"+
		"\u0000\u0000\u0000\u027f\u0280\u0005\u0001\u0000\u0000\u0280\u0282\u0005"+
		"[\u0000\u0000\u0281\u0283\u0003\"\u0011\u0000\u0282\u0281\u0001\u0000"+
		"\u0000\u0000\u0282\u0283\u0001\u0000\u0000\u0000\u0283\u0284\u0001\u0000"+
		"\u0000\u0000\u0284\u0285\u0003Z-\u0000\u0285\u0286\u0005\u0002\u0000\u0000"+
		"\u0286Y\u0001\u0000\u0000\u0000\u0287\u0289\u0003\u001c\u000e\u0000\u0288"+
		"\u0287\u0001\u0000\u0000\u0000\u0288\u0289\u0001\u0000\u0000\u0000\u0289"+
		"\u028a\u0001\u0000\u0000\u0000\u028a\u0295\u0003\\.\u0000\u028b\u028d"+
		"\u0003v;\u0000\u028c\u028e\u0003\u001c\u000e\u0000\u028d\u028c\u0001\u0000"+
		"\u0000\u0000\u028d\u028e\u0001\u0000\u0000\u0000\u028e\u028f\u0001\u0000"+
		"\u0000\u0000\u028f\u0290\u0003\u0016\u000b\u0000\u0290\u0295\u0001\u0000"+
		"\u0000\u0000\u0291\u0292\u0003|>\u0000\u0292\u0293\u0003Z-\u0000\u0293"+
		"\u0295\u0001\u0000\u0000\u0000\u0294\u0288\u0001\u0000\u0000\u0000\u0294"+
		"\u028b\u0001\u0000\u0000\u0000\u0294\u0291\u0001\u0000\u0000\u0000\u0295"+
		"[\u0001\u0000\u0000\u0000\u0296\u0297\u0003\u0016\u000b\u0000\u0297\u0298"+
		"\u0003^/\u0000\u0298]\u0001\u0000\u0000\u0000\u0299\u029a\u0005\u0001"+
		"\u0000\u0000\u029a\u02a4\u0005`\u0000\u0000\u029b\u029d\u0003\n\u0005"+
		"\u0000\u029c\u029b\u0001\u0000\u0000\u0000\u029d\u02a0\u0001\u0000\u0000"+
		"\u0000\u029e\u029c\u0001\u0000\u0000\u0000\u029e\u029f\u0001\u0000\u0000"+
		"\u0000\u029f\u02a5\u0001\u0000\u0000\u0000\u02a0\u029e\u0001\u0000\u0000"+
		"\u0000\u02a1\u02a2\u0003\"\u0011\u0000\u02a2\u02a3\u0003\n\u0005\u0000"+
		"\u02a3\u02a5\u0001\u0000\u0000\u0000\u02a4\u029e\u0001\u0000\u0000\u0000"+
		"\u02a4\u02a1\u0001\u0000\u0000\u0000\u02a5\u02a6\u0001\u0000\u0000\u0000"+
		"\u02a6\u02a8\u0005\u0002\u0000\u0000\u02a7\u0299\u0001\u0000\u0000\u0000"+
		"\u02a8\u02ab\u0001\u0000\u0000\u0000\u02a9\u02a7\u0001\u0000\u0000\u0000"+
		"\u02a9\u02aa\u0001\u0000\u0000\u0000\u02aa\u02ac\u0001\u0000\u0000\u0000"+
		"\u02ab\u02a9\u0001\u0000\u0000\u0000\u02ac\u02ad\u0003T*\u0000\u02ad_"+
		"\u0001\u0000\u0000\u0000\u02ae\u02af\u0005\u0001\u0000\u0000\u02af\u02b0"+
		"\u0005f\u0000\u0000\u02b0\u02b1\u0003V+\u0000\u02b1\u02b2\u0005\u0002"+
		"\u0000\u0000\u02b2\u02b5\u0001\u0000\u0000\u0000\u02b3\u02b5\u0003H$\u0000"+
		"\u02b4\u02ae\u0001\u0000\u0000\u0000\u02b4\u02b3\u0001\u0000\u0000\u0000"+
		"\u02b5a\u0001\u0000\u0000\u0000\u02b6\u02b7\u0005\u0001\u0000\u0000\u02b7"+
		"\u02b9\u0005d\u0000\u0000\u02b8\u02ba\u0003 \u0010\u0000\u02b9\u02b8\u0001"+
		"\u0000\u0000\u0000\u02b9\u02ba\u0001\u0000\u0000\u0000\u02ba\u02bb\u0001"+
		"\u0000\u0000\u0000\u02bb\u02bf\u0003`0\u0000\u02bc\u02be\u0003 \u0010"+
		"\u0000\u02bd\u02bc\u0001\u0000\u0000\u0000\u02be\u02c1\u0001\u0000\u0000"+
		"\u0000\u02bf\u02bd\u0001\u0000\u0000\u0000\u02bf\u02c0\u0001\u0000\u0000"+
		"\u0000\u02c0\u02c2\u0001\u0000\u0000\u0000\u02c1\u02bf\u0001\u0000\u0000"+
		"\u0000\u02c2\u02c3\u0005\u0002\u0000\u0000\u02c3c\u0001\u0000\u0000\u0000"+
		"\u02c4\u02c5\u0005\u0001\u0000\u0000\u02c5\u02c7\u0005b\u0000\u0000\u02c6"+
		"\u02c8\u0003\"\u0011\u0000\u02c7\u02c6\u0001\u0000\u0000\u0000\u02c7\u02c8"+
		"\u0001\u0000\u0000\u0000\u02c8\u02c9\u0001\u0000\u0000\u0000\u02c9\u02ca"+
		"\u0003f3\u0000\u02ca\u02cb\u0005\u0002\u0000\u0000\u02cbe\u0001\u0000"+
		"\u0000\u0000\u02cc\u02df\u0003\u0018\f\u0000\u02cd\u02ce\u0003v;\u0000"+
		"\u02ce\u02cf\u0003\u0018\f\u0000\u02cf\u02df\u0001\u0000\u0000\u0000\u02d0"+
		"\u02d1\u0003|>\u0000\u02d1\u02d2\u0003f3\u0000\u02d2\u02df\u0001\u0000"+
		"\u0000\u0000\u02d3\u02d4\u0003\u0006\u0003\u0000\u02d4\u02d5\u0005\u0001"+
		"\u0000\u0000\u02d5\u02d9\u0005d\u0000\u0000\u02d6\u02d8\u0003 \u0010\u0000"+
		"\u02d7\u02d6\u0001\u0000\u0000\u0000\u02d8\u02db\u0001\u0000\u0000\u0000"+
		"\u02d9\u02d7\u0001\u0000\u0000\u0000\u02d9\u02da\u0001\u0000\u0000\u0000"+
		"\u02da\u02dc\u0001\u0000\u0000\u0000\u02db\u02d9\u0001\u0000\u0000\u0000"+
		"\u02dc\u02dd\u0005\u0002\u0000\u0000\u02dd\u02df\u0001\u0000\u0000\u0000"+
		"\u02de\u02cc\u0001\u0000\u0000\u0000\u02de\u02cd\u0001\u0000\u0000\u0000"+
		"\u02de\u02d0\u0001\u0000\u0000\u0000\u02de\u02d3\u0001\u0000\u0000\u0000"+
		"\u02dfg\u0001\u0000\u0000\u0000\u02e0\u02e1\u0005\u0001\u0000\u0000\u02e1"+
		"\u02e3\u0005e\u0000\u0000\u02e2\u02e4\u0003 \u0010\u0000\u02e3\u02e2\u0001"+
		"\u0000\u0000\u0000\u02e3\u02e4\u0001\u0000\u0000\u0000\u02e4\u02e5\u0001"+
		"\u0000\u0000\u0000\u02e5\u02e9\u0003`0\u0000\u02e6\u02e8\u0005\u0006\u0000"+
		"\u0000\u02e7\u02e6\u0001\u0000\u0000\u0000\u02e8\u02eb\u0001\u0000\u0000"+
		"\u0000\u02e9\u02e7\u0001\u0000\u0000\u0000\u02e9\u02ea\u0001\u0000\u0000"+
		"\u0000\u02ea\u02ec\u0001\u0000\u0000\u0000\u02eb\u02e9\u0001\u0000\u0000"+
		"\u0000\u02ec\u02ed\u0005\u0002\u0000\u0000\u02edi\u0001\u0000\u0000\u0000"+
		"\u02ee\u02ef\u0005\u0001\u0000\u0000\u02ef\u02f1\u0005c\u0000\u0000\u02f0"+
		"\u02f2\u0003\"\u0011\u0000\u02f1\u02f0\u0001\u0000\u0000\u0000\u02f1\u02f2"+
		"\u0001\u0000\u0000\u0000\u02f2\u02f3\u0001\u0000\u0000\u0000\u02f3\u02f4"+
		"\u0003l6\u0000\u02f4\u02f5\u0005\u0002\u0000\u0000\u02f5k\u0001\u0000"+
		"\u0000\u0000\u02f6\u0307\u0003\u001a\r\u0000\u02f7\u02f8\u0003v;\u0000"+
		"\u02f8\u02f9\u0003\u001a\r\u0000\u02f9\u0307\u0001\u0000\u0000\u0000\u02fa"+
		"\u02fb\u0003|>\u0000\u02fb\u02fc\u0003l6\u0000\u02fc\u0307\u0001\u0000"+
		"\u0000\u0000\u02fd\u02fe\u0005\u0001\u0000\u0000\u02fe\u0302\u0005e\u0000"+
		"\u0000\u02ff\u0301\u0005\u0006\u0000\u0000\u0300\u02ff\u0001\u0000\u0000"+
		"\u0000\u0301\u0304\u0001\u0000\u0000\u0000\u0302\u0300\u0001\u0000\u0000"+
		"\u0000\u0302\u0303\u0001\u0000\u0000\u0000\u0303\u0305\u0001\u0000\u0000"+
		"\u0000\u0304\u0302\u0001\u0000\u0000\u0000\u0305\u0307\u0005\u0002\u0000"+
		"\u0000\u0306\u02f6\u0001\u0000\u0000\u0000\u0306\u02f7\u0001\u0000\u0000"+
		"\u0000\u0306\u02fa\u0001\u0000\u0000\u0000\u0306\u02fd\u0001\u0000\u0000"+
		"\u0000\u0307m\u0001\u0000\u0000\u0000\u0308\u0309\u0005\u0001\u0000\u0000"+
		"\u0309\u030b\u0005a\u0000\u0000\u030a\u030c\u0003\"\u0011\u0000\u030b"+
		"\u030a\u0001\u0000\u0000\u0000\u030b\u030c\u0001\u0000\u0000\u0000\u030c"+
		"\u030d\u0001\u0000\u0000\u0000\u030d\u030e\u0003p8\u0000\u030e\u030f\u0005"+
		"\u0002\u0000\u0000\u030fo\u0001\u0000\u0000\u0000\u0310\u0311\u0003\u000e"+
		"\u0007\u0000\u0311\u0312\u0003V+\u0000\u0312\u031a\u0001\u0000\u0000\u0000"+
		"\u0313\u0314\u0003v;\u0000\u0314\u0315\u0003\u000e\u0007\u0000\u0315\u031a"+
		"\u0001\u0000\u0000\u0000\u0316\u0317\u0003|>\u0000\u0317\u0318\u0003p"+
		"8\u0000\u0318\u031a\u0001\u0000\u0000\u0000\u0319\u0310\u0001\u0000\u0000"+
		"\u0000\u0319\u0313\u0001\u0000\u0000\u0000\u0319\u0316\u0001\u0000\u0000"+
		"\u0000\u031aq\u0001\u0000\u0000\u0000\u031b\u031c\u0005\u0001\u0000\u0000"+
		"\u031c\u031e\u0005[\u0000\u0000\u031d\u031f\u0003\"\u0011\u0000\u031e"+
		"\u031d\u0001\u0000\u0000\u0000\u031e\u031f\u0001\u0000\u0000\u0000\u031f"+
		"\u0320\u0001\u0000\u0000\u0000\u0320\u0321\u0003\u001c\u000e\u0000\u0321"+
		"\u0322\u0005\u0002\u0000\u0000\u0322\u0344\u0001\u0000\u0000\u0000\u0323"+
		"\u0324\u0005\u0001\u0000\u0000\u0324\u0326\u0005[\u0000\u0000\u0325\u0327"+
		"\u0003\"\u0011\u0000\u0326\u0325\u0001\u0000\u0000\u0000\u0326\u0327\u0001"+
		"\u0000\u0000\u0000\u0327\u0328\u0001\u0000\u0000\u0000\u0328\u0329\u0003"+
		"\u0016\u000b\u0000\u0329\u032a\u0005\u0002\u0000\u0000\u032a\u0344\u0001"+
		"\u0000\u0000\u0000\u032b\u032c\u0005\u0001\u0000\u0000\u032c\u032e\u0005"+
		"b\u0000\u0000\u032d\u032f\u0003\"\u0011\u0000\u032e\u032d\u0001\u0000"+
		"\u0000\u0000\u032e\u032f\u0001\u0000\u0000\u0000\u032f\u0330\u0001\u0000"+
		"\u0000\u0000\u0330\u0331\u0003\u0018\f\u0000\u0331\u0332\u0005\u0002\u0000"+
		"\u0000\u0332\u0344\u0001\u0000\u0000\u0000\u0333\u0334\u0005\u0001\u0000"+
		"\u0000\u0334\u0336\u0005c\u0000\u0000\u0335\u0337\u0003\"\u0011\u0000"+
		"\u0336\u0335\u0001\u0000\u0000\u0000\u0336\u0337\u0001\u0000\u0000\u0000"+
		"\u0337\u0338\u0001\u0000\u0000\u0000\u0338\u0339\u0003\u001a\r\u0000\u0339"+
		"\u033a\u0005\u0002\u0000\u0000\u033a\u0344\u0001\u0000\u0000\u0000\u033b"+
		"\u033c\u0005\u0001\u0000\u0000\u033c\u033e\u0005a\u0000\u0000\u033d\u033f"+
		"\u0003\"\u0011\u0000\u033e\u033d\u0001\u0000\u0000\u0000\u033e\u033f\u0001"+
		"\u0000\u0000\u0000\u033f\u0340\u0001\u0000\u0000\u0000\u0340\u0341\u0003"+
		"\u000e\u0007\u0000\u0341\u0342\u0005\u0002\u0000\u0000\u0342\u0344\u0001"+
		"\u0000\u0000\u0000\u0343\u031b\u0001\u0000\u0000\u0000\u0343\u0323\u0001"+
		"\u0000\u0000\u0000\u0343\u032b\u0001\u0000\u0000\u0000\u0343\u0333\u0001"+
		"\u0000\u0000\u0000\u0343\u033b\u0001\u0000\u0000\u0000\u0344s\u0001\u0000"+
		"\u0000\u0000\u0345\u0346\u0005\u0001\u0000\u0000\u0346\u0347\u0005g\u0000"+
		"\u0000\u0347\u0348\u0003\u0002\u0001\u0000\u0348\u0349\u0003\u0002\u0001"+
		"\u0000\u0349\u034a\u0003r9\u0000\u034a\u034b\u0005\u0002\u0000\u0000\u034b"+
		"u\u0001\u0000\u0000\u0000\u034c\u034d\u0005\u0001\u0000\u0000\u034d\u034e"+
		"\u0005g\u0000\u0000\u034e\u034f\u0003\u0002\u0001\u0000\u034f\u0350\u0003"+
		"\u0002\u0001\u0000\u0350\u0351\u0005\u0002\u0000\u0000\u0351w\u0001\u0000"+
		"\u0000\u0000\u0352\u0353\u0005\u0001\u0000\u0000\u0353\u0354\u0005[\u0000"+
		"\u0000\u0354\u0355\u0003 \u0010\u0000\u0355\u0356\u0005\u0002\u0000\u0000"+
		"\u0356\u0367\u0001\u0000\u0000\u0000\u0357\u0358\u0005\u0001\u0000\u0000"+
		"\u0358\u0359\u0005b\u0000\u0000\u0359\u035a\u0003 \u0010\u0000\u035a\u035b"+
		"\u0005\u0002\u0000\u0000\u035b\u0367\u0001\u0000\u0000\u0000\u035c\u035d"+
		"\u0005\u0001\u0000\u0000\u035d\u035e\u0005c\u0000\u0000\u035e\u035f\u0003"+
		" \u0010\u0000\u035f\u0360\u0005\u0002\u0000\u0000\u0360\u0367\u0001\u0000"+
		"\u0000\u0000\u0361\u0362\u0005\u0001\u0000\u0000\u0362\u0363\u0005a\u0000"+
		"\u0000\u0363\u0364\u0003 \u0010\u0000\u0364\u0365\u0005\u0002\u0000\u0000"+
		"\u0365\u0367\u0001\u0000\u0000\u0000\u0366\u0352\u0001\u0000\u0000\u0000"+
		"\u0366\u0357\u0001\u0000\u0000\u0000\u0366\u035c\u0001\u0000\u0000\u0000"+
		"\u0366\u0361\u0001\u0000\u0000\u0000\u0367y\u0001\u0000\u0000\u0000\u0368"+
		"\u0369\u0005\u0001\u0000\u0000\u0369\u036a\u0005h\u0000\u0000\u036a\u036b"+
		"\u0003\u0002\u0001\u0000\u036b\u036c\u0003x<\u0000\u036c\u036d\u0005\u0002"+
		"\u0000\u0000\u036d{\u0001\u0000\u0000\u0000\u036e\u036f\u0005\u0001\u0000"+
		"\u0000\u036f\u0370\u0005h\u0000\u0000\u0370\u0371\u0003\u0002\u0001\u0000"+
		"\u0371\u0372\u0005\u0002\u0000\u0000\u0372}\u0001\u0000\u0000\u0000\u0373"+
		"\u0374\u0005\u0001\u0000\u0000\u0374\u0376\u0005Z\u0000\u0000\u0375\u0377"+
		"\u0003\"\u0011\u0000\u0376\u0375\u0001\u0000\u0000\u0000\u0376\u0377\u0001"+
		"\u0000\u0000\u0000\u0377\u0378\u0001\u0000\u0000\u0000\u0378\u0379\u0003"+
		"\u0010\b\u0000\u0379\u037a\u0005\u0002\u0000\u0000\u037a\u007f\u0001\u0000"+
		"\u0000\u0000\u037b\u037c\u0005\u0001\u0000\u0000\u037c\u037d\u0005]\u0000"+
		"\u0000\u037d\u037e\u0003 \u0010\u0000\u037e\u037f\u0005\u0002\u0000\u0000"+
		"\u037f\u0081\u0001\u0000\u0000\u0000\u0380\u038b\u0003~?\u0000\u0381\u038b"+
		"\u0003n7\u0000\u0382\u038b\u0003d2\u0000\u0383\u038b\u0003j5\u0000\u0384"+
		"\u038b\u0003X,\u0000\u0385\u038b\u0003b1\u0000\u0386\u038b\u0003h4\u0000"+
		"\u0387\u038b\u0003\u0080@\u0000\u0388\u038b\u0003t:\u0000\u0389\u038b"+
		"\u0003z=\u0000\u038a\u0380\u0001\u0000\u0000\u0000\u038a\u0381\u0001\u0000"+
		"\u0000\u0000\u038a\u0382\u0001\u0000\u0000\u0000\u038a\u0383\u0001\u0000"+
		"\u0000\u0000\u038a\u0384\u0001\u0000\u0000\u0000\u038a\u0385\u0001\u0000"+
		"\u0000\u0000\u038a\u0386\u0001\u0000\u0000\u0000\u038a\u0387\u0001\u0000"+
		"\u0000\u0000\u038a\u0388\u0001\u0000\u0000\u0000\u038a\u0389\u0001\u0000"+
		"\u0000\u0000\u038b\u0083\u0001\u0000\u0000\u0000\u038c\u038d\u0005\u0001"+
		"\u0000\u0000\u038d\u038f\u0005i\u0000\u0000\u038e\u0390\u0005z\u0000\u0000"+
		"\u038f\u038e\u0001\u0000\u0000\u0000\u038f\u0390\u0001\u0000\u0000\u0000"+
		"\u0390\u0394\u0001\u0000\u0000\u0000\u0391\u0393\u0003\u0082A\u0000\u0392"+
		"\u0391\u0001\u0000\u0000\u0000\u0393\u0396\u0001\u0000\u0000\u0000\u0394"+
		"\u0392\u0001\u0000\u0000\u0000\u0394\u0395\u0001\u0000\u0000\u0000\u0395"+
		"\u0397\u0001\u0000\u0000\u0000\u0396\u0394\u0001\u0000\u0000\u0000\u0397"+
		"\u0398\u0005\u0002\u0000\u0000\u0398\u0085\u0001\u0000\u0000\u0000\u0399"+
		"\u03a8\u0003\u0084B\u0000\u039a\u039b\u0005\u0001\u0000\u0000\u039b\u039d"+
		"\u0005i\u0000\u0000\u039c\u039e\u0005z\u0000\u0000\u039d\u039c\u0001\u0000"+
		"\u0000\u0000\u039d\u039e\u0001\u0000\u0000\u0000\u039e\u039f\u0001\u0000"+
		"\u0000\u0000\u039f\u03a3\u0007\u0006\u0000\u0000\u03a0\u03a2\u0005\u0006"+
		"\u0000\u0000\u03a1\u03a0\u0001\u0000\u0000\u0000\u03a2\u03a5\u0001\u0000"+
		"\u0000\u0000\u03a3\u03a1\u0001\u0000\u0000\u0000\u03a3\u03a4\u0001\u0000"+
		"\u0000\u0000\u03a4\u03a6\u0001\u0000\u0000\u0000\u03a5\u03a3\u0001\u0000"+
		"\u0000\u0000\u03a6\u03a8\u0005\u0002\u0000\u0000\u03a7\u0399\u0001\u0000"+
		"\u0000\u0000\u03a7\u039a\u0001\u0000\u0000\u0000\u03a8\u0087\u0001\u0000"+
		"\u0000\u0000\u03a9\u03aa\u0005\u0001\u0000\u0000\u03aa\u03ac\u0005n\u0000"+
		"\u0000\u03ab\u03ad\u0005z\u0000\u0000\u03ac\u03ab\u0001\u0000\u0000\u0000"+
		"\u03ac\u03ad\u0001\u0000\u0000\u0000\u03ad\u03ae\u0001\u0000\u0000\u0000"+
		"\u03ae\u03af\u0003\u0002\u0001\u0000\u03af\u03b0\u0003\u0092I\u0000\u03b0"+
		"\u03b1\u0005\u0002\u0000\u0000\u03b1\u03bb\u0001\u0000\u0000\u0000\u03b2"+
		"\u03b3\u0005\u0001\u0000\u0000\u03b3\u03b5\u0005o\u0000\u0000\u03b4\u03b6"+
		"\u0005z\u0000\u0000\u03b5\u03b4\u0001\u0000\u0000\u0000\u03b5\u03b6\u0001"+
		"\u0000\u0000\u0000\u03b6\u03b7\u0001\u0000\u0000\u0000\u03b7\u03b8\u0003"+
		"\u0002\u0001\u0000\u03b8\u03b9\u0005\u0002\u0000\u0000\u03b9\u03bb\u0001"+
		"\u0000\u0000\u0000\u03ba\u03a9\u0001\u0000\u0000\u0000\u03ba\u03b2\u0001"+
		"\u0000\u0000\u0000\u03bb\u0089\u0001\u0000\u0000\u0000\u03bc\u03bd\u0005"+
		"\u0001\u0000\u0000\u03bd\u03be\u0005p\u0000\u0000\u03be\u03bf\u0003\u0086"+
		"C\u0000\u03bf\u03c0\u0005\u0006\u0000\u0000\u03c0\u03c1\u0005\u0002\u0000"+
		"\u0000\u03c1\u03f1\u0001\u0000\u0000\u0000\u03c2\u03c3\u0005\u0001\u0000"+
		"\u0000\u03c3\u03c4\u0005q\u0000\u0000\u03c4\u03c5\u0003\u0086C\u0000\u03c5"+
		"\u03c6\u0005\u0006\u0000\u0000\u03c6\u03c7\u0005\u0002\u0000\u0000\u03c7"+
		"\u03f1\u0001\u0000\u0000\u0000\u03c8\u03c9\u0005\u0001\u0000\u0000\u03c9"+
		"\u03ca\u0005r\u0000\u0000\u03ca\u03cb\u0003\u0086C\u0000\u03cb\u03cc\u0005"+
		"\u0006\u0000\u0000\u03cc\u03cd\u0005\u0002\u0000\u0000\u03cd\u03f1\u0001"+
		"\u0000\u0000\u0000\u03ce\u03cf\u0005\u0001\u0000\u0000\u03cf\u03d0\u0005"+
		"v\u0000\u0000\u03d0\u03d1\u0003\u0086C\u0000\u03d1\u03d2\u0005\u0006\u0000"+
		"\u0000\u03d2\u03d3\u0005\u0002\u0000\u0000\u03d3\u03f1\u0001\u0000\u0000"+
		"\u0000\u03d4\u03d5\u0005\u0001\u0000\u0000\u03d5\u03d6\u0005s\u0000\u0000"+
		"\u03d6\u03d7\u0003\u0088D\u0000\u03d7\u03d8\u0003\u0092I\u0000\u03d8\u03d9"+
		"\u0005\u0002\u0000\u0000\u03d9\u03f1\u0001\u0000\u0000\u0000\u03da\u03db"+
		"\u0005\u0001\u0000\u0000\u03db\u03dc\u0005t\u0000\u0000\u03dc\u03dd\u0003"+
		"\u0088D\u0000\u03dd\u03de\u0005\u0002\u0000\u0000\u03de\u03f1\u0001\u0000"+
		"\u0000\u0000\u03df\u03e0\u0005\u0001\u0000\u0000\u03e0\u03e1\u0005u\u0000"+
		"\u0000\u03e1\u03e2\u0003\u0088D\u0000\u03e2\u03e3\u0005\u0002\u0000\u0000"+
		"\u03e3\u03f1\u0001\u0000\u0000\u0000\u03e4\u03e5\u0005\u0001\u0000\u0000"+
		"\u03e5\u03e6\u0005v\u0000\u0000\u03e6\u03e7\u0003\u0088D\u0000\u03e7\u03e8"+
		"\u0005\u0006\u0000\u0000\u03e8\u03e9\u0005\u0002\u0000\u0000\u03e9\u03f1"+
		"\u0001\u0000\u0000\u0000\u03ea\u03eb\u0005\u0001\u0000\u0000\u03eb\u03ec"+
		"\u0005w\u0000\u0000\u03ec\u03ed\u0003\u0088D\u0000\u03ed\u03ee\u0005\u0006"+
		"\u0000\u0000\u03ee\u03ef\u0005\u0002\u0000\u0000\u03ef\u03f1\u0001\u0000"+
		"\u0000\u0000\u03f0\u03bc\u0001\u0000\u0000\u0000\u03f0\u03c2\u0001\u0000"+
		"\u0000\u0000\u03f0\u03c8\u0001\u0000\u0000\u0000\u03f0\u03ce\u0001\u0000"+
		"\u0000\u0000\u03f0\u03d4\u0001\u0000\u0000\u0000\u03f0\u03da\u0001\u0000"+
		"\u0000\u0000\u03f0\u03df\u0001\u0000\u0000\u0000\u03f0\u03e4\u0001\u0000"+
		"\u0000\u0000\u03f0\u03ea\u0001\u0000\u0000\u0000\u03f1\u008b\u0001\u0000"+
		"\u0000\u0000\u03f2\u03ff\u0003\u0088D\u0000\u03f3\u03ff\u0003\u008aE\u0000"+
		"\u03f4\u03ff\u0003\u0086C\u0000\u03f5\u03f6\u0005\u0001\u0000\u0000\u03f6"+
		"\u03f7\u0005m\u0000\u0000\u03f7\u03f9\u0003\u0002\u0001\u0000\u03f8\u03fa"+
		"\u0005z\u0000\u0000\u03f9\u03f8\u0001\u0000\u0000\u0000\u03f9\u03fa\u0001"+
		"\u0000\u0000\u0000\u03fa\u03fb\u0001\u0000\u0000\u0000\u03fb\u03fc\u0005"+
		"\u0002\u0000\u0000\u03fc\u03ff\u0001\u0000\u0000\u0000\u03fd\u03ff\u0003"+
		"\u008eG\u0000\u03fe\u03f2\u0001\u0000\u0000\u0000\u03fe\u03f3\u0001\u0000"+
		"\u0000\u0000\u03fe\u03f4\u0001\u0000\u0000\u0000\u03fe\u03f5\u0001\u0000"+
		"\u0000\u0000\u03fe\u03fd\u0001\u0000\u0000\u0000\u03ff\u008d\u0001\u0000"+
		"\u0000\u0000\u0400\u0401\u0005\u0001\u0000\u0000\u0401\u0403\u0005l\u0000"+
		"\u0000\u0402\u0404\u0005z\u0000\u0000\u0403\u0402\u0001\u0000\u0000\u0000"+
		"\u0403\u0404\u0001\u0000\u0000\u0000\u0404\u0408\u0001\u0000\u0000\u0000"+
		"\u0405\u0407\u0003\u008cF\u0000\u0406\u0405\u0001\u0000\u0000\u0000\u0407"+
		"\u040a\u0001\u0000\u0000\u0000\u0408\u0406\u0001\u0000\u0000\u0000\u0408"+
		"\u0409\u0001\u0000\u0000\u0000\u0409\u040b\u0001\u0000\u0000\u0000\u040a"+
		"\u0408\u0001\u0000\u0000\u0000\u040b\u0421\u0005\u0002\u0000\u0000\u040c"+
		"\u040d\u0005\u0001\u0000\u0000\u040d\u040f\u0005x\u0000\u0000\u040e\u0410"+
		"\u0005z\u0000\u0000\u040f\u040e\u0001\u0000\u0000\u0000\u040f\u0410\u0001"+
		"\u0000\u0000\u0000\u0410\u0411\u0001\u0000\u0000\u0000\u0411\u0412\u0005"+
		"\u0006\u0000\u0000\u0412\u0421\u0005\u0002\u0000\u0000\u0413\u0414\u0005"+
		"\u0001\u0000\u0000\u0414\u0416\u0005y\u0000\u0000\u0415\u0417\u0005z\u0000"+
		"\u0000\u0416\u0415\u0001\u0000\u0000\u0000\u0416\u0417\u0001\u0000\u0000"+
		"\u0000\u0417\u0418\u0001\u0000\u0000\u0000\u0418\u0419\u0005\u0006\u0000"+
		"\u0000\u0419\u0421\u0005\u0002\u0000\u0000\u041a\u041b\u0005\u0001\u0000"+
		"\u0000\u041b\u041d\u0005y\u0000\u0000\u041c\u041e\u0005z\u0000\u0000\u041d"+
		"\u041c\u0001\u0000\u0000\u0000\u041d\u041e\u0001\u0000\u0000\u0000\u041e"+
		"\u041f\u0001\u0000\u0000\u0000\u041f\u0421\u0005\u0002\u0000\u0000\u0420"+
		"\u0400\u0001\u0000\u0000\u0000\u0420\u040c\u0001\u0000\u0000\u0000\u0420"+
		"\u0413\u0001\u0000\u0000\u0000\u0420\u041a\u0001\u0000\u0000\u0000\u0421"+
		"\u008f\u0001\u0000\u0000\u0000\u0422\u0423\u0005\u0001\u0000\u0000\u0423"+
		"\u0424\u0005\b\u0000\u0000\u0424\u0425\u0003\u001e\u000f\u0000\u0425\u0426"+
		"\u0005\u0002\u0000\u0000\u0426\u0091\u0001\u0000\u0000\u0000\u0427\u0429"+
		"\u0003\u0090H\u0000\u0428\u0427\u0001\u0000\u0000\u0000\u0429\u042c\u0001"+
		"\u0000\u0000\u0000\u042a\u0428\u0001\u0000\u0000\u0000\u042a\u042b\u0001"+
		"\u0000\u0000\u0000\u042b\u0093\u0001\u0000\u0000\u0000\u042c\u042a\u0001"+
		"\u0000\u0000\u0000\u042d\u042f\u0003\u008cF\u0000\u042e\u042d\u0001\u0000"+
		"\u0000\u0000\u042f\u0432\u0001\u0000\u0000\u0000\u0430\u042e\u0001\u0000"+
		"\u0000\u0000\u0430\u0431\u0001\u0000\u0000\u0000\u0431\u0433\u0001\u0000"+
		"\u0000\u0000\u0432\u0430\u0001\u0000\u0000\u0000\u0433\u043c\u0005\u0000"+
		"\u0000\u0001\u0434\u0436\u0003\u0082A\u0000\u0435\u0434\u0001\u0000\u0000"+
		"\u0000\u0436\u0437\u0001\u0000\u0000\u0000\u0437\u0435\u0001\u0000\u0000"+
		"\u0000\u0437\u0438\u0001\u0000\u0000\u0000\u0438\u0439\u0001\u0000\u0000"+
		"\u0000\u0439\u043a\u0005\u0000\u0000\u0001\u043a\u043c\u0001\u0000\u0000"+
		"\u0000\u043b\u0430\u0001\u0000\u0000\u0000\u043b\u0435\u0001\u0000\u0000"+
		"\u0000\u043c\u0095\u0001\u0000\u0000\u0000\u043d\u043e\u0003\u0084B\u0000"+
		"\u043e\u043f\u0005\u0000\u0000\u0001\u043f\u0448\u0001\u0000\u0000\u0000"+
		"\u0440\u0442\u0003\u0082A\u0000\u0441\u0440\u0001\u0000\u0000\u0000\u0442"+
		"\u0445\u0001\u0000\u0000\u0000\u0443\u0441\u0001\u0000\u0000\u0000\u0443"+
		"\u0444\u0001\u0000\u0000\u0000\u0444\u0446\u0001\u0000\u0000\u0000\u0445"+
		"\u0443\u0001\u0000\u0000\u0000\u0446\u0448\u0005\u0000\u0000\u0001\u0447"+
		"\u043d\u0001\u0000\u0000\u0000\u0447\u0443\u0001\u0000\u0000\u0000\u0448"+
		"\u0097\u0001\u0000\u0000\u0000k\u00a5\u00af\u00bb\u00c1\u00c6\u00ce\u00d4"+
		"\u00dc\u00e2\u00f3\u0101\u0112\u0115\u0119\u011c\u012d\u013a\u013f\u0164"+
		"\u017a\u01a8\u01ac\u01b5\u01bb\u01c3\u01c9\u01ce\u01d7\u01dd\u01e7\u01ed"+
		"\u01f4\u01f9\u01fd\u0202\u0205\u0209\u020b\u0213\u021f\u0226\u022b\u0230"+
		"\u0233\u0236\u023f\u0245\u024f\u0255\u025b\u0264\u0270\u0272\u0277\u027b"+
		"\u0282\u0288\u028d\u0294\u029e\u02a4\u02a9\u02b4\u02b9\u02bf\u02c7\u02d9"+
		"\u02de\u02e3\u02e9\u02f1\u0302\u0306\u030b\u0319\u031e\u0326\u032e\u0336"+
		"\u033e\u0343\u0366\u0376\u038a\u038f\u0394\u039d\u03a3\u03a7\u03ac\u03b5"+
		"\u03ba\u03f0\u03f9\u03fe\u0403\u0408\u040f\u0416\u041d\u0420\u042a\u0430"+
		"\u0437\u043b\u0443\u0447";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}