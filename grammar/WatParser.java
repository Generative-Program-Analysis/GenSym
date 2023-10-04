package gensym.wasm;
// Generated from WatParser.g4 by ANTLR 4.13.0
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
	static { RuntimeMetaData.checkVersion("4.13.0", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		LPAR=1, RPAR=2, NAT=3, INT=4, FLOAT=5, STRING_=6, VALUE_TYPE=7, CONST=8, 
		SYMBOLIC=9, FUNCREF=10, EXTERNREF=11, MUT=12, NOP=13, SYM_ASSERT=14, ALLOC=15, 
		FREE=16, UNREACHABLE=17, DROP=18, BLOCK=19, LOOP=20, END=21, BR=22, BR_IF=23, 
		BR_TABLE=24, RETURN=25, IF=26, THEN=27, ELSE=28, SELECT=29, CALL=30, CALL_INDIRECT=31, 
		LOCAL_GET=32, LOCAL_SET=33, LOCAL_TEE=34, GLOBAL_GET=35, GLOBAL_SET=36, 
		LOAD=37, STORE=38, UNDERSCORE=39, OFFSET_EQ=40, ALIGN_EQ=41, SIGN_POSTFIX=42, 
		MEM_SIZE=43, I32=44, I64=45, F32=46, F64=47, IXX=48, FXX=49, OP_EQZ=50, 
		OP_EQ=51, OP_NE=52, OP_LT=53, OP_LTS=54, OP_LTU=55, OP_LE=56, OP_LES=57, 
		OP_LEU=58, OP_GT=59, OP_GTS=60, OP_GTU=61, OP_GE=62, OP_GES=63, OP_GEU=64, 
		OP_CLZ=65, OP_CTZ=66, OP_POPCNT=67, OP_NEG=68, OP_ABS=69, OP_SQRT=70, 
		OP_CEIL=71, OP_FLOOR=72, OP_TRUNC=73, OP_NEAREST=74, OP_ADD=75, OP_SUB=76, 
		OP_MUL=77, OP_DIV=78, OP_DIV_S=79, OP_DIV_U=80, OP_REM_S=81, OP_REM_U=82, 
		OP_AND=83, OP_OR=84, OP_XOR=85, OP_SHL=86, OP_SHR_S=87, OP_SHR_U=88, OP_ROTL=89, 
		OP_ROTR=90, OP_MIN=91, OP_MAX=92, OP_COPYSIGN=93, OP_WRAP=94, OP_TRUNC_=95, 
		OP_TRUNC_SAT=96, OP_CONVERT=97, OP_EXTEND=98, OP_DEMOTE=99, OP_PROMOTE=100, 
		OP_REINTER=101, MEMORY_SIZE=102, MEMORY_GROW=103, MEMORY_FILL=104, MEMORY_COPY=105, 
		MEMORY_INIT=106, TEST=107, COMPARE=108, UNARY=109, BINARY=110, CONVERT=111, 
		TYPE=112, FUNC=113, EXTERN=114, START_=115, PARAM=116, RESULT=117, LOCAL=118, 
		GLOBAL=119, TABLE=120, MEMORY=121, ELEM=122, DATA=123, OFFSET=124, IMPORT=125, 
		EXPORT=126, MODULE=127, BIN=128, QUOTE=129, SCRIPT=130, REGISTER=131, 
		INVOKE=132, GET=133, ASSERT_MALFORMED=134, ASSERT_INVALID=135, ASSERT_UNLINKABLE=136, 
		ASSERT_RETURN=137, ASSERT_RETURN_CANONICAL_NAN=138, ASSERT_RETURN_ARITHMETIC_NAN=139, 
		ASSERT_TRAP=140, ASSERT_EXHAUSTION=141, INPUT=142, OUTPUT=143, VAR=144, 
		V128=145, SPACE=146, COMMENT=147;
	public static final int
		RULE_value = 0, RULE_name = 1, RULE_numType = 2, RULE_refType = 3, RULE_vecType = 4, 
		RULE_valType = 5, RULE_heapType = 6, RULE_globalType = 7, RULE_defType = 8, 
		RULE_funcParamType = 9, RULE_funcResType = 10, RULE_funcType = 11, RULE_tableType = 12, 
		RULE_memoryType = 13, RULE_typeUse = 14, RULE_literal = 15, RULE_idx = 16, 
		RULE_bindVar = 17, RULE_instr = 18, RULE_plainInstr = 19, RULE_offsetEq = 20, 
		RULE_alignEq = 21, RULE_load = 22, RULE_store = 23, RULE_callIndirectInstr = 24, 
		RULE_callInstrParams = 25, RULE_callInstrInstr = 26, RULE_callInstrParamsInstr = 27, 
		RULE_callInstrResultsInstr = 28, RULE_blockInstr = 29, RULE_blockType = 30, 
		RULE_block = 31, RULE_foldedInstr = 32, RULE_expr = 33, RULE_callExprType = 34, 
		RULE_callExprParams = 35, RULE_callExprResults = 36, RULE_instrList = 37, 
		RULE_constExpr = 38, RULE_function = 39, RULE_funcFields = 40, RULE_funcFieldsBody = 41, 
		RULE_funcBody = 42, RULE_offset = 43, RULE_elem = 44, RULE_table = 45, 
		RULE_tableField = 46, RULE_data = 47, RULE_memory = 48, RULE_memoryField = 49, 
		RULE_global = 50, RULE_globalField = 51, RULE_importDesc = 52, RULE_simport = 53, 
		RULE_inlineImport = 54, RULE_exportDesc = 55, RULE_export_ = 56, RULE_inlineExport = 57, 
		RULE_typeDef = 58, RULE_start_ = 59, RULE_moduleField = 60, RULE_module_ = 61, 
		RULE_scriptModule = 62, RULE_action_ = 63, RULE_assertion = 64, RULE_cmd = 65, 
		RULE_meta = 66, RULE_wconst = 67, RULE_constList = 68, RULE_script = 69, 
		RULE_module = 70;
	private static String[] makeRuleNames() {
		return new String[] {
			"value", "name", "numType", "refType", "vecType", "valType", "heapType", 
			"globalType", "defType", "funcParamType", "funcResType", "funcType", 
			"tableType", "memoryType", "typeUse", "literal", "idx", "bindVar", "instr", 
			"plainInstr", "offsetEq", "alignEq", "load", "store", "callIndirectInstr", 
			"callInstrParams", "callInstrInstr", "callInstrParamsInstr", "callInstrResultsInstr", 
			"blockInstr", "blockType", "block", "foldedInstr", "expr", "callExprType", 
			"callExprParams", "callExprResults", "instrList", "constExpr", "function", 
			"funcFields", "funcFieldsBody", "funcBody", "offset", "elem", "table", 
			"tableField", "data", "memory", "memoryField", "global", "globalField", 
			"importDesc", "simport", "inlineImport", "exportDesc", "export_", "inlineExport", 
			"typeDef", "start_", "moduleField", "module_", "scriptModule", "action_", 
			"assertion", "cmd", "meta", "wconst", "constList", "script", "module"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", null, null, null, null, null, null, null, "'funcref'", 
			"'externref'", "'mut'", "'nop'", "'sym_assert'", "'alloc'", "'free'", 
			"'unreachable'", "'drop'", "'block'", "'loop'", "'end'", "'br'", "'br_if'", 
			"'br_table'", "'return'", "'if'", "'then'", "'else'", "'select'", "'call'", 
			"'call_indirect'", "'local.get'", "'local.set'", "'local.tee'", "'global.get'", 
			"'global.set'", null, null, "'_'", "'offset='", "'align='", null, null, 
			"'i32'", "'i64'", "'f32'", "'f64'", null, null, "'.eqz'", "'.eq'", "'.ne'", 
			"'.lt'", "'.lt_s'", "'.lt_u'", "'.le'", "'.le_s'", "'.le_u'", "'.gt'", 
			"'.gt_s'", "'.gt_u'", "'.ge'", "'.ge_s'", "'.ge_u'", "'.clz'", "'.ctz'", 
			"'.popcnt'", "'.neg'", "'.abs'", "'.sqrt'", "'.ceil'", "'.floor'", "'.trunc'", 
			"'.nearest'", "'.add'", "'.sub'", "'.mul'", "'.div'", "'.div_s'", "'.div_u'", 
			"'.rem_s'", "'.rem_u'", "'.and'", "'.or'", "'.xor'", "'.shl'", "'.shr_s'", 
			"'.shr_u'", "'.rotl'", "'.rotr'", "'.min'", "'.max'", "'.copysign'", 
			"'.wrap_'", "'.trunc_'", "'.trunc_sat_'", "'.convert_'", "'.extend_'", 
			"'.demote_'", "'.promote_'", "'.reinterpret_'", "'memory.size'", "'memory.grow'", 
			"'memory.fill'", "'memory.copy'", "'memory.init'", null, null, null, 
			null, null, "'type'", "'func'", "'extern'", "'start'", "'param'", "'result'", 
			"'local'", "'global'", "'table'", "'memory'", "'elem'", "'data'", "'offset'", 
			"'import'", "'export'", "'module'", "'binary'", "'quote'", "'script'", 
			"'register'", "'invoke'", "'get'", "'assert_malformed'", "'assert_invalid'", 
			"'assert_unlinkable'", "'assert_return'", "'assert_return_canonical_nan'", 
			"'assert_return_arithmetic_nan'", "'assert_trap'", "'assert_exhaustion'", 
			"'input'", "'output'", null, "'v128'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "LPAR", "RPAR", "NAT", "INT", "FLOAT", "STRING_", "VALUE_TYPE", 
			"CONST", "SYMBOLIC", "FUNCREF", "EXTERNREF", "MUT", "NOP", "SYM_ASSERT", 
			"ALLOC", "FREE", "UNREACHABLE", "DROP", "BLOCK", "LOOP", "END", "BR", 
			"BR_IF", "BR_TABLE", "RETURN", "IF", "THEN", "ELSE", "SELECT", "CALL", 
			"CALL_INDIRECT", "LOCAL_GET", "LOCAL_SET", "LOCAL_TEE", "GLOBAL_GET", 
			"GLOBAL_SET", "LOAD", "STORE", "UNDERSCORE", "OFFSET_EQ", "ALIGN_EQ", 
			"SIGN_POSTFIX", "MEM_SIZE", "I32", "I64", "F32", "F64", "IXX", "FXX", 
			"OP_EQZ", "OP_EQ", "OP_NE", "OP_LT", "OP_LTS", "OP_LTU", "OP_LE", "OP_LES", 
			"OP_LEU", "OP_GT", "OP_GTS", "OP_GTU", "OP_GE", "OP_GES", "OP_GEU", "OP_CLZ", 
			"OP_CTZ", "OP_POPCNT", "OP_NEG", "OP_ABS", "OP_SQRT", "OP_CEIL", "OP_FLOOR", 
			"OP_TRUNC", "OP_NEAREST", "OP_ADD", "OP_SUB", "OP_MUL", "OP_DIV", "OP_DIV_S", 
			"OP_DIV_U", "OP_REM_S", "OP_REM_U", "OP_AND", "OP_OR", "OP_XOR", "OP_SHL", 
			"OP_SHR_S", "OP_SHR_U", "OP_ROTL", "OP_ROTR", "OP_MIN", "OP_MAX", "OP_COPYSIGN", 
			"OP_WRAP", "OP_TRUNC_", "OP_TRUNC_SAT", "OP_CONVERT", "OP_EXTEND", "OP_DEMOTE", 
			"OP_PROMOTE", "OP_REINTER", "MEMORY_SIZE", "MEMORY_GROW", "MEMORY_FILL", 
			"MEMORY_COPY", "MEMORY_INIT", "TEST", "COMPARE", "UNARY", "BINARY", "CONVERT", 
			"TYPE", "FUNC", "EXTERN", "START_", "PARAM", "RESULT", "LOCAL", "GLOBAL", 
			"TABLE", "MEMORY", "ELEM", "DATA", "OFFSET", "IMPORT", "EXPORT", "MODULE", 
			"BIN", "QUOTE", "SCRIPT", "REGISTER", "INVOKE", "GET", "ASSERT_MALFORMED", 
			"ASSERT_INVALID", "ASSERT_UNLINKABLE", "ASSERT_RETURN", "ASSERT_RETURN_CANONICAL_NAN", 
			"ASSERT_RETURN_ARITHMETIC_NAN", "ASSERT_TRAP", "ASSERT_EXHAUSTION", "INPUT", 
			"OUTPUT", "VAR", "V128", "SPACE", "COMMENT"
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
			setState(142);
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
			setState(144);
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
			setState(146);
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
			setState(148);
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
			setState(150);
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
			setState(155);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VALUE_TYPE:
				enterOuterAlt(_localctx, 1);
				{
				setState(152);
				numType();
				}
				break;
			case V128:
				enterOuterAlt(_localctx, 2);
				{
				setState(153);
				vecType();
				}
				break;
			case FUNCREF:
			case EXTERNREF:
				enterOuterAlt(_localctx, 3);
				{
				setState(154);
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
			setState(157);
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
			setState(165);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VALUE_TYPE:
			case FUNCREF:
			case EXTERNREF:
			case V128:
				enterOuterAlt(_localctx, 1);
				{
				setState(159);
				valType();
				}
				break;
			case LPAR:
				enterOuterAlt(_localctx, 2);
				{
				setState(160);
				match(LPAR);
				setState(161);
				match(MUT);
				setState(162);
				valType();
				setState(163);
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
			setState(167);
			match(LPAR);
			setState(168);
			match(FUNC);
			setState(169);
			funcType();
			setState(170);
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
			setState(188);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(172);
					match(LPAR);
					setState(173);
					match(PARAM);
					setState(183);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case RPAR:
					case VALUE_TYPE:
					case FUNCREF:
					case EXTERNREF:
					case V128:
						{
						setState(177);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3200L) != 0) || _la==V128) {
							{
							{
							setState(174);
							valType();
							}
							}
							setState(179);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						}
						break;
					case VAR:
						{
						setState(180);
						bindVar();
						setState(181);
						valType();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(185);
					match(RPAR);
					}
					} 
				}
				setState(190);
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
			setState(202);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(191);
					match(LPAR);
					setState(192);
					match(RESULT);
					setState(196);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3200L) != 0) || _la==V128) {
						{
						{
						setState(193);
						valType();
						}
						}
						setState(198);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(199);
					match(RPAR);
					}
					} 
				}
				setState(204);
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
			setState(205);
			funcParamType();
			setState(206);
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
			setState(208);
			match(NAT);
			setState(210);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAT) {
				{
				setState(209);
				match(NAT);
				}
			}

			setState(212);
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
			setState(214);
			match(NAT);
			setState(216);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAT) {
				{
				setState(215);
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
			setState(218);
			match(LPAR);
			setState(219);
			match(TYPE);
			setState(220);
			idx();
			setState(221);
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
			setState(223);
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
			setState(225);
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
			setState(227);
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
		public FoldedInstrContext foldedInstr() {
			return getRuleContext(FoldedInstrContext.class,0);
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
			setState(233);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VALUE_TYPE:
			case CONST:
			case SYMBOLIC:
			case NOP:
			case SYM_ASSERT:
			case ALLOC:
			case FREE:
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
				setState(229);
				plainInstr();
				}
				break;
			case CALL_INDIRECT:
				enterOuterAlt(_localctx, 2);
				{
				setState(230);
				callInstrInstr();
				}
				break;
			case BLOCK:
			case LOOP:
			case IF:
				enterOuterAlt(_localctx, 3);
				{
				setState(231);
				blockInstr();
				}
				break;
			case LPAR:
				enterOuterAlt(_localctx, 4);
				{
				setState(232);
				foldedInstr();
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
		public TerminalNode SYMBOLIC() { return getToken(WatParser.SYMBOLIC, 0); }
		public TerminalNode SYM_ASSERT() { return getToken(WatParser.SYM_ASSERT, 0); }
		public TerminalNode ALLOC() { return getToken(WatParser.ALLOC, 0); }
		public TerminalNode FREE() { return getToken(WatParser.FREE, 0); }
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
			int _alt;
			setState(293);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(235);
				match(UNREACHABLE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(236);
				match(NOP);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(237);
				match(DROP);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(238);
				match(SELECT);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(239);
				match(BR);
				setState(240);
				idx();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(241);
				match(BR_IF);
				setState(242);
				idx();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(243);
				match(BR_TABLE);
				setState(245); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(244);
						idx();
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(247); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(249);
				match(RETURN);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(250);
				match(CALL);
				setState(251);
				idx();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(252);
				match(LOCAL_GET);
				setState(253);
				idx();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(254);
				match(LOCAL_SET);
				setState(255);
				idx();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(256);
				match(LOCAL_TEE);
				setState(257);
				idx();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(258);
				match(GLOBAL_GET);
				setState(259);
				idx();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(260);
				match(GLOBAL_SET);
				setState(261);
				idx();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(262);
				load();
				setState(264);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==OFFSET_EQ) {
					{
					setState(263);
					offsetEq();
					}
				}

				setState(267);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ALIGN_EQ) {
					{
					setState(266);
					alignEq();
					}
				}

				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(269);
				store();
				setState(271);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==OFFSET_EQ) {
					{
					setState(270);
					offsetEq();
					}
				}

				setState(274);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ALIGN_EQ) {
					{
					setState(273);
					alignEq();
					}
				}

				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(276);
				match(MEMORY_SIZE);
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(277);
				match(MEMORY_GROW);
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(278);
				match(MEMORY_FILL);
				}
				break;
			case 20:
				enterOuterAlt(_localctx, 20);
				{
				setState(279);
				match(MEMORY_COPY);
				}
				break;
			case 21:
				enterOuterAlt(_localctx, 21);
				{
				setState(280);
				match(MEMORY_INIT);
				setState(281);
				idx();
				}
				break;
			case 22:
				enterOuterAlt(_localctx, 22);
				{
				setState(282);
				match(CONST);
				setState(283);
				literal();
				}
				break;
			case 23:
				enterOuterAlt(_localctx, 23);
				{
				setState(284);
				match(SYMBOLIC);
				}
				break;
			case 24:
				enterOuterAlt(_localctx, 24);
				{
				setState(285);
				match(SYM_ASSERT);
				}
				break;
			case 25:
				enterOuterAlt(_localctx, 25);
				{
				setState(286);
				match(ALLOC);
				}
				break;
			case 26:
				enterOuterAlt(_localctx, 26);
				{
				setState(287);
				match(FREE);
				}
				break;
			case 27:
				enterOuterAlt(_localctx, 27);
				{
				setState(288);
				match(TEST);
				}
				break;
			case 28:
				enterOuterAlt(_localctx, 28);
				{
				setState(289);
				match(COMPARE);
				}
				break;
			case 29:
				enterOuterAlt(_localctx, 29);
				{
				setState(290);
				match(UNARY);
				}
				break;
			case 30:
				enterOuterAlt(_localctx, 30);
				{
				setState(291);
				match(BINARY);
				}
				break;
			case 31:
				enterOuterAlt(_localctx, 31);
				{
				setState(292);
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
			setState(295);
			match(OFFSET_EQ);
			setState(296);
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
			setState(298);
			match(ALIGN_EQ);
			setState(299);
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
			setState(301);
			numType();
			setState(302);
			match(LOAD);
			setState(306);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MEM_SIZE) {
				{
				setState(303);
				match(MEM_SIZE);
				setState(304);
				match(UNDERSCORE);
				setState(305);
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
			setState(308);
			numType();
			setState(309);
			match(STORE);
			setState(311);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MEM_SIZE) {
				{
				setState(310);
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
			setState(313);
			match(CALL_INDIRECT);
			setState(315);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				{
				setState(314);
				typeUse();
				}
				break;
			}
			setState(317);
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
			setState(330);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(319);
					match(LPAR);
					setState(320);
					match(PARAM);
					setState(324);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3200L) != 0) || _la==V128) {
						{
						{
						setState(321);
						valType();
						}
						}
						setState(326);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(327);
					match(RPAR);
					}
					} 
				}
				setState(332);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			}
			setState(344);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(333);
					match(LPAR);
					setState(334);
					match(RESULT);
					setState(338);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3200L) != 0) || _la==V128) {
						{
						{
						setState(335);
						valType();
						}
						}
						setState(340);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(341);
					match(RPAR);
					}
					} 
				}
				setState(346);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
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
			setState(347);
			match(CALL_INDIRECT);
			setState(349);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				{
				setState(348);
				typeUse();
				}
				break;
			}
			setState(351);
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
			setState(364);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(353);
					match(LPAR);
					setState(354);
					match(PARAM);
					setState(358);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3200L) != 0) || _la==V128) {
						{
						{
						setState(355);
						valType();
						}
						}
						setState(360);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(361);
					match(RPAR);
					}
					} 
				}
				setState(366);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			}
			setState(367);
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
			setState(380);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(369);
					match(LPAR);
					setState(370);
					match(RESULT);
					setState(374);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3200L) != 0) || _la==V128) {
						{
						{
						setState(371);
						valType();
						}
						}
						setState(376);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(377);
					match(RPAR);
					}
					} 
				}
				setState(382);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			}
			setState(383);
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
		public TerminalNode BLOCK() { return getToken(WatParser.BLOCK, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode END() { return getToken(WatParser.END, 0); }
		public List<BindVarContext> bindVar() {
			return getRuleContexts(BindVarContext.class);
		}
		public BindVarContext bindVar(int i) {
			return getRuleContext(BindVarContext.class,i);
		}
		public TerminalNode LOOP() { return getToken(WatParser.LOOP, 0); }
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
			setState(419);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BLOCK:
				enterOuterAlt(_localctx, 1);
				{
				setState(385);
				match(BLOCK);
				setState(387);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(386);
					bindVar();
					}
				}

				setState(389);
				block();
				setState(390);
				match(END);
				setState(392);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
				case 1:
					{
					setState(391);
					bindVar();
					}
					break;
				}
				}
				break;
			case LOOP:
				enterOuterAlt(_localctx, 2);
				{
				setState(394);
				match(LOOP);
				setState(396);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(395);
					bindVar();
					}
				}

				setState(398);
				block();
				setState(399);
				match(END);
				setState(401);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
				case 1:
					{
					setState(400);
					bindVar();
					}
					break;
				}
				}
				break;
			case IF:
				enterOuterAlt(_localctx, 3);
				{
				setState(403);
				match(IF);
				setState(405);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(404);
					bindVar();
					}
				}

				setState(407);
				block();
				setState(413);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(408);
					match(ELSE);
					setState(410);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==VAR) {
						{
						setState(409);
						bindVar();
						}
					}

					setState(412);
					instrList();
					}
				}

				setState(415);
				match(END);
				setState(417);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
				case 1:
					{
					setState(416);
					bindVar();
					}
					break;
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
			setState(421);
			match(LPAR);
			setState(422);
			match(RESULT);
			setState(423);
			valType();
			setState(424);
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
			setState(427);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
			case 1:
				{
				setState(426);
				blockType();
				}
				break;
			}
			setState(429);
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
	public static class FoldedInstrContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public FoldedInstrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_foldedInstr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterFoldedInstr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitFoldedInstr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitFoldedInstr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FoldedInstrContext foldedInstr() throws RecognitionException {
		FoldedInstrContext _localctx = new FoldedInstrContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_foldedInstr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(431);
			match(LPAR);
			setState(432);
			expr();
			setState(433);
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
	public static class ExprContext extends ParserRuleContext {
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
		public BlockTypeContext blockType() {
			return getRuleContext(BlockTypeContext.class,0);
		}
		public List<FoldedInstrContext> foldedInstr() {
			return getRuleContexts(FoldedInstrContext.class);
		}
		public FoldedInstrContext foldedInstr(int i) {
			return getRuleContext(FoldedInstrContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(WatParser.ELSE, 0); }
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
		enterRule(_localctx, 66, RULE_expr);
		int _la;
		try {
			int _alt;
			setState(477);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VALUE_TYPE:
			case CONST:
			case SYMBOLIC:
			case NOP:
			case SYM_ASSERT:
			case ALLOC:
			case FREE:
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
				setState(435);
				plainInstr();
				setState(439);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,38,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(436);
						expr();
						}
						} 
					}
					setState(441);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,38,_ctx);
				}
				}
				break;
			case CALL_INDIRECT:
				enterOuterAlt(_localctx, 2);
				{
				setState(442);
				match(CALL_INDIRECT);
				setState(443);
				callExprType();
				}
				break;
			case BLOCK:
				enterOuterAlt(_localctx, 3);
				{
				setState(444);
				match(BLOCK);
				setState(446);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
				case 1:
					{
					setState(445);
					bindVar();
					}
					break;
				}
				setState(448);
				block();
				}
				break;
			case LOOP:
				enterOuterAlt(_localctx, 4);
				{
				setState(449);
				match(LOOP);
				setState(451);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
				case 1:
					{
					setState(450);
					bindVar();
					}
					break;
				}
				setState(453);
				block();
				}
				break;
			case IF:
				enterOuterAlt(_localctx, 5);
				{
				setState(454);
				match(IF);
				setState(456);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(455);
					bindVar();
					}
				}

				setState(459);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
				case 1:
					{
					setState(458);
					blockType();
					}
					break;
				}
				setState(464);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,43,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(461);
						foldedInstr();
						}
						} 
					}
					setState(466);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,43,_ctx);
				}
				setState(467);
				match(LPAR);
				setState(468);
				match(THEN);
				setState(469);
				instrList();
				setState(475);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LPAR) {
					{
					setState(470);
					match(LPAR);
					setState(471);
					match(ELSE);
					setState(472);
					instrList();
					setState(473);
					match(RPAR);
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
			setState(480);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				{
				setState(479);
				typeUse();
				}
				break;
			}
			setState(482);
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
			setState(495);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(484);
					match(LPAR);
					setState(485);
					match(PARAM);
					setState(489);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3200L) != 0) || _la==V128) {
						{
						{
						setState(486);
						valType();
						}
						}
						setState(491);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(492);
					match(RPAR);
					}
					} 
				}
				setState(497);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
			}
			setState(498);
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
			setState(511);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAR) {
				{
				{
				setState(500);
				match(LPAR);
				setState(501);
				match(RESULT);
				setState(505);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3200L) != 0) || _la==V128) {
					{
					{
					setState(502);
					valType();
					}
					}
					setState(507);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(508);
				match(RPAR);
				}
				}
				setState(513);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(517);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,51,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(514);
					expr();
					}
					} 
				}
				setState(519);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,51,_ctx);
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
		enterRule(_localctx, 74, RULE_instrList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(523);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(520);
					instr();
					}
					} 
				}
				setState(525);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
			}
			setState(527);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
			case 1:
				{
				setState(526);
				callIndirectInstr();
				}
				break;
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
		enterRule(_localctx, 76, RULE_constExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(529);
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
		enterRule(_localctx, 78, RULE_function);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(531);
			match(LPAR);
			setState(532);
			match(FUNC);
			setState(534);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(533);
				bindVar();
				}
			}

			setState(536);
			funcFields();
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
		enterRule(_localctx, 80, RULE_funcFields);
		try {
			setState(552);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(540);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
				case 1:
					{
					setState(539);
					typeUse();
					}
					break;
				}
				setState(542);
				funcFieldsBody();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(543);
				inlineImport();
				setState(545);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
				case 1:
					{
					setState(544);
					typeUse();
					}
					break;
				}
				setState(547);
				funcType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(549);
				inlineExport();
				setState(550);
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
		enterRule(_localctx, 82, RULE_funcFieldsBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(554);
			funcType();
			setState(555);
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
		enterRule(_localctx, 84, RULE_funcBody);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(573);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,60,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(557);
					match(LPAR);
					setState(558);
					match(LOCAL);
					setState(568);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case RPAR:
					case VALUE_TYPE:
					case FUNCREF:
					case EXTERNREF:
					case V128:
						{
						setState(562);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3200L) != 0) || _la==V128) {
							{
							{
							setState(559);
							valType();
							}
							}
							setState(564);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						}
						break;
					case VAR:
						{
						setState(565);
						bindVar();
						setState(566);
						valType();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(570);
					match(RPAR);
					}
					} 
				}
				setState(575);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,60,_ctx);
			}
			setState(576);
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
		enterRule(_localctx, 86, RULE_offset);
		try {
			setState(584);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LPAR:
				enterOuterAlt(_localctx, 1);
				{
				setState(578);
				match(LPAR);
				setState(579);
				match(OFFSET);
				setState(580);
				constExpr();
				setState(581);
				match(RPAR);
				}
				break;
			case VALUE_TYPE:
			case CONST:
			case SYMBOLIC:
			case NOP:
			case SYM_ASSERT:
			case ALLOC:
			case FREE:
			case UNREACHABLE:
			case DROP:
			case BLOCK:
			case LOOP:
			case BR:
			case BR_IF:
			case BR_TABLE:
			case RETURN:
			case IF:
			case SELECT:
			case CALL:
			case CALL_INDIRECT:
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
				enterOuterAlt(_localctx, 2);
				{
				setState(583);
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
	public static class ElemContext extends ParserRuleContext {
		public List<TerminalNode> LPAR() { return getTokens(WatParser.LPAR); }
		public TerminalNode LPAR(int i) {
			return getToken(WatParser.LPAR, i);
		}
		public TerminalNode ELEM() { return getToken(WatParser.ELEM, 0); }
		public InstrContext instr() {
			return getRuleContext(InstrContext.class,0);
		}
		public List<TerminalNode> RPAR() { return getTokens(WatParser.RPAR); }
		public TerminalNode RPAR(int i) {
			return getToken(WatParser.RPAR, i);
		}
		public List<IdxContext> idx() {
			return getRuleContexts(IdxContext.class);
		}
		public IdxContext idx(int i) {
			return getRuleContext(IdxContext.class,i);
		}
		public OffsetContext offset() {
			return getRuleContext(OffsetContext.class,0);
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
		enterRule(_localctx, 88, RULE_elem);
		int _la;
		try {
			setState(616);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,66,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(586);
				match(LPAR);
				setState(587);
				match(ELEM);
				setState(589);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAT || _la==VAR) {
					{
					setState(588);
					idx();
					}
				}

				setState(591);
				match(LPAR);
				setState(592);
				instr();
				setState(593);
				match(RPAR);
				setState(597);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NAT || _la==VAR) {
					{
					{
					setState(594);
					idx();
					}
					}
					setState(599);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(600);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(602);
				match(LPAR);
				setState(603);
				match(ELEM);
				setState(605);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAT || _la==VAR) {
					{
					setState(604);
					idx();
					}
				}

				setState(607);
				offset();
				setState(611);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NAT || _la==VAR) {
					{
					{
					setState(608);
					idx();
					}
					}
					setState(613);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(614);
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
	public static class TableContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode TABLE() { return getToken(WatParser.TABLE, 0); }
		public TableFieldContext tableField() {
			return getRuleContext(TableFieldContext.class,0);
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
		enterRule(_localctx, 90, RULE_table);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(618);
			match(LPAR);
			setState(619);
			match(TABLE);
			setState(621);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(620);
				bindVar();
				}
			}

			setState(623);
			tableField();
			setState(624);
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
	public static class TableFieldContext extends ParserRuleContext {
		public TableTypeContext tableType() {
			return getRuleContext(TableTypeContext.class,0);
		}
		public InlineImportContext inlineImport() {
			return getRuleContext(InlineImportContext.class,0);
		}
		public InlineExportContext inlineExport() {
			return getRuleContext(InlineExportContext.class,0);
		}
		public TableFieldContext tableField() {
			return getRuleContext(TableFieldContext.class,0);
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
		public TableFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableField; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterTableField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitTableField(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitTableField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableFieldContext tableField() throws RecognitionException {
		TableFieldContext _localctx = new TableFieldContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_tableField);
		int _la;
		try {
			setState(644);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(626);
				tableType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(627);
				inlineImport();
				setState(628);
				tableType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(630);
				inlineExport();
				setState(631);
				tableField();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(633);
				refType();
				setState(634);
				match(LPAR);
				setState(635);
				match(ELEM);
				setState(639);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NAT || _la==VAR) {
					{
					{
					setState(636);
					idx();
					}
					}
					setState(641);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(642);
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
		public List<TerminalNode> LPAR() { return getTokens(WatParser.LPAR); }
		public TerminalNode LPAR(int i) {
			return getToken(WatParser.LPAR, i);
		}
		public TerminalNode DATA() { return getToken(WatParser.DATA, 0); }
		public InstrContext instr() {
			return getRuleContext(InstrContext.class,0);
		}
		public List<TerminalNode> RPAR() { return getTokens(WatParser.RPAR); }
		public TerminalNode RPAR(int i) {
			return getToken(WatParser.RPAR, i);
		}
		public IdxContext idx() {
			return getRuleContext(IdxContext.class,0);
		}
		public List<TerminalNode> STRING_() { return getTokens(WatParser.STRING_); }
		public TerminalNode STRING_(int i) {
			return getToken(WatParser.STRING_, i);
		}
		public OffsetContext offset() {
			return getRuleContext(OffsetContext.class,0);
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
		enterRule(_localctx, 94, RULE_data);
		int _la;
		try {
			setState(676);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,74,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(646);
				match(LPAR);
				setState(647);
				match(DATA);
				setState(649);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAT || _la==VAR) {
					{
					setState(648);
					idx();
					}
				}

				setState(651);
				match(LPAR);
				setState(652);
				instr();
				setState(653);
				match(RPAR);
				setState(657);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STRING_) {
					{
					{
					setState(654);
					match(STRING_);
					}
					}
					setState(659);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(660);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(662);
				match(LPAR);
				setState(663);
				match(DATA);
				setState(665);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAT || _la==VAR) {
					{
					setState(664);
					idx();
					}
				}

				setState(667);
				offset();
				setState(671);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STRING_) {
					{
					{
					setState(668);
					match(STRING_);
					}
					}
					setState(673);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(674);
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
	public static class MemoryContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode MEMORY() { return getToken(WatParser.MEMORY, 0); }
		public MemoryFieldContext memoryField() {
			return getRuleContext(MemoryFieldContext.class,0);
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
		enterRule(_localctx, 96, RULE_memory);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(678);
			match(LPAR);
			setState(679);
			match(MEMORY);
			setState(681);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(680);
				bindVar();
				}
			}

			setState(683);
			memoryField();
			setState(684);
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
	public static class MemoryFieldContext extends ParserRuleContext {
		public MemoryTypeContext memoryType() {
			return getRuleContext(MemoryTypeContext.class,0);
		}
		public InlineImportContext inlineImport() {
			return getRuleContext(InlineImportContext.class,0);
		}
		public InlineExportContext inlineExport() {
			return getRuleContext(InlineExportContext.class,0);
		}
		public MemoryFieldContext memoryField() {
			return getRuleContext(MemoryFieldContext.class,0);
		}
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode DATA() { return getToken(WatParser.DATA, 0); }
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public List<TerminalNode> STRING_() { return getTokens(WatParser.STRING_); }
		public TerminalNode STRING_(int i) {
			return getToken(WatParser.STRING_, i);
		}
		public MemoryFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memoryField; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterMemoryField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitMemoryField(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitMemoryField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemoryFieldContext memoryField() throws RecognitionException {
		MemoryFieldContext _localctx = new MemoryFieldContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_memoryField);
		int _la;
		try {
			setState(702);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,77,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(686);
				memoryType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(687);
				inlineImport();
				setState(688);
				memoryType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(690);
				inlineExport();
				setState(691);
				memoryField();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(693);
				match(LPAR);
				setState(694);
				match(DATA);
				setState(698);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STRING_) {
					{
					{
					setState(695);
					match(STRING_);
					}
					}
					setState(700);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(701);
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
	public static class GlobalContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode GLOBAL() { return getToken(WatParser.GLOBAL, 0); }
		public GlobalFieldContext globalField() {
			return getRuleContext(GlobalFieldContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public BindVarContext bindVar() {
			return getRuleContext(BindVarContext.class,0);
		}
		public GlobalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_global; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterGlobal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitGlobal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitGlobal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GlobalContext global() throws RecognitionException {
		GlobalContext _localctx = new GlobalContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_global);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(704);
			match(LPAR);
			setState(705);
			match(GLOBAL);
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
			globalField();
			setState(710);
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
	public static class GlobalFieldContext extends ParserRuleContext {
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
		public GlobalFieldContext globalField() {
			return getRuleContext(GlobalFieldContext.class,0);
		}
		public GlobalFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_globalField; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterGlobalField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitGlobalField(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitGlobalField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GlobalFieldContext globalField() throws RecognitionException {
		GlobalFieldContext _localctx = new GlobalFieldContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_globalField);
		try {
			setState(721);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,79,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(712);
				globalType();
				setState(713);
				constExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(715);
				inlineImport();
				setState(716);
				globalType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(718);
				inlineExport();
				setState(719);
				globalField();
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
		enterRule(_localctx, 104, RULE_importDesc);
		int _la;
		try {
			setState(763);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(723);
				match(LPAR);
				setState(724);
				match(FUNC);
				setState(726);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(725);
					bindVar();
					}
				}

				setState(728);
				typeUse();
				setState(729);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(731);
				match(LPAR);
				setState(732);
				match(FUNC);
				setState(734);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(733);
					bindVar();
					}
				}

				setState(736);
				funcType();
				setState(737);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(739);
				match(LPAR);
				setState(740);
				match(TABLE);
				setState(742);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(741);
					bindVar();
					}
				}

				setState(744);
				tableType();
				setState(745);
				match(RPAR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(747);
				match(LPAR);
				setState(748);
				match(MEMORY);
				setState(750);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(749);
					bindVar();
					}
				}

				setState(752);
				memoryType();
				setState(753);
				match(RPAR);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(755);
				match(LPAR);
				setState(756);
				match(GLOBAL);
				setState(758);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(757);
					bindVar();
					}
				}

				setState(760);
				globalType();
				setState(761);
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
		enterRule(_localctx, 106, RULE_simport);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(765);
			match(LPAR);
			setState(766);
			match(IMPORT);
			setState(767);
			name();
			setState(768);
			name();
			setState(769);
			importDesc();
			setState(770);
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
		enterRule(_localctx, 108, RULE_inlineImport);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(772);
			match(LPAR);
			setState(773);
			match(IMPORT);
			setState(774);
			name();
			setState(775);
			name();
			setState(776);
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
		enterRule(_localctx, 110, RULE_exportDesc);
		try {
			setState(798);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,86,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(778);
				match(LPAR);
				setState(779);
				match(FUNC);
				setState(780);
				idx();
				setState(781);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(783);
				match(LPAR);
				setState(784);
				match(TABLE);
				setState(785);
				idx();
				setState(786);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(788);
				match(LPAR);
				setState(789);
				match(MEMORY);
				setState(790);
				idx();
				setState(791);
				match(RPAR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(793);
				match(LPAR);
				setState(794);
				match(GLOBAL);
				setState(795);
				idx();
				setState(796);
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
		enterRule(_localctx, 112, RULE_export_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(800);
			match(LPAR);
			setState(801);
			match(EXPORT);
			setState(802);
			name();
			setState(803);
			exportDesc();
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
		enterRule(_localctx, 114, RULE_inlineExport);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(806);
			match(LPAR);
			setState(807);
			match(EXPORT);
			setState(808);
			name();
			setState(809);
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
		enterRule(_localctx, 116, RULE_typeDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(811);
			match(LPAR);
			setState(812);
			match(TYPE);
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
			defType();
			setState(817);
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
		enterRule(_localctx, 118, RULE_start_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(819);
			match(LPAR);
			setState(820);
			match(START_);
			setState(821);
			idx();
			setState(822);
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
		public GlobalContext global() {
			return getRuleContext(GlobalContext.class,0);
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
		enterRule(_localctx, 120, RULE_moduleField);
		try {
			setState(834);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(824);
				typeDef();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(825);
				global();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(826);
				table();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(827);
				memory();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(828);
				function();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(829);
				elem();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(830);
				data();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(831);
				start_();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(832);
				simport();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(833);
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
		enterRule(_localctx, 122, RULE_module_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(836);
			match(LPAR);
			setState(837);
			match(MODULE);
			setState(839);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(838);
				match(VAR);
				}
			}

			setState(844);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAR) {
				{
				{
				setState(841);
				moduleField();
				}
				}
				setState(846);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(847);
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
		enterRule(_localctx, 124, RULE_scriptModule);
		int _la;
		try {
			setState(863);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,93,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(849);
				module_();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(850);
				match(LPAR);
				setState(851);
				match(MODULE);
				setState(853);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(852);
					match(VAR);
					}
				}

				setState(855);
				_la = _input.LA(1);
				if ( !(_la==BIN || _la==QUOTE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(859);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STRING_) {
					{
					{
					setState(856);
					match(STRING_);
					}
					}
					setState(861);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(862);
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
		enterRule(_localctx, 126, RULE_action_);
		int _la;
		try {
			setState(882);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,96,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(865);
				match(LPAR);
				setState(866);
				match(INVOKE);
				setState(868);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(867);
					match(VAR);
					}
				}

				setState(870);
				name();
				setState(871);
				constList();
				setState(872);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(874);
				match(LPAR);
				setState(875);
				match(GET);
				setState(877);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(876);
					match(VAR);
					}
				}

				setState(879);
				name();
				setState(880);
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
		enterRule(_localctx, 128, RULE_assertion);
		try {
			setState(936);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,97,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(884);
				match(LPAR);
				setState(885);
				match(ASSERT_MALFORMED);
				setState(886);
				scriptModule();
				setState(887);
				match(STRING_);
				setState(888);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(890);
				match(LPAR);
				setState(891);
				match(ASSERT_INVALID);
				setState(892);
				scriptModule();
				setState(893);
				match(STRING_);
				setState(894);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(896);
				match(LPAR);
				setState(897);
				match(ASSERT_UNLINKABLE);
				setState(898);
				scriptModule();
				setState(899);
				match(STRING_);
				setState(900);
				match(RPAR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(902);
				match(LPAR);
				setState(903);
				match(ASSERT_TRAP);
				setState(904);
				scriptModule();
				setState(905);
				match(STRING_);
				setState(906);
				match(RPAR);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(908);
				match(LPAR);
				setState(909);
				match(ASSERT_RETURN);
				setState(910);
				action_();
				setState(911);
				constList();
				setState(912);
				match(RPAR);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(914);
				match(LPAR);
				setState(915);
				match(ASSERT_RETURN_CANONICAL_NAN);
				setState(916);
				action_();
				setState(917);
				match(RPAR);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(919);
				match(LPAR);
				setState(920);
				match(ASSERT_RETURN_ARITHMETIC_NAN);
				setState(921);
				action_();
				setState(922);
				match(RPAR);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(924);
				match(LPAR);
				setState(925);
				match(ASSERT_TRAP);
				setState(926);
				action_();
				setState(927);
				match(STRING_);
				setState(928);
				match(RPAR);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(930);
				match(LPAR);
				setState(931);
				match(ASSERT_EXHAUSTION);
				setState(932);
				action_();
				setState(933);
				match(STRING_);
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
		enterRule(_localctx, 130, RULE_cmd);
		int _la;
		try {
			setState(950);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,99,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(938);
				action_();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(939);
				assertion();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(940);
				scriptModule();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(941);
				match(LPAR);
				setState(942);
				match(REGISTER);
				setState(943);
				name();
				setState(945);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(944);
					match(VAR);
					}
				}

				setState(947);
				match(RPAR);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(949);
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
		enterRule(_localctx, 132, RULE_meta);
		int _la;
		try {
			setState(984);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,105,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(952);
				match(LPAR);
				setState(953);
				match(SCRIPT);
				setState(955);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(954);
					match(VAR);
					}
				}

				setState(960);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LPAR) {
					{
					{
					setState(957);
					cmd();
					}
					}
					setState(962);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(963);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(964);
				match(LPAR);
				setState(965);
				match(INPUT);
				setState(967);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(966);
					match(VAR);
					}
				}

				setState(969);
				match(STRING_);
				setState(970);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(971);
				match(LPAR);
				setState(972);
				match(OUTPUT);
				setState(974);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(973);
					match(VAR);
					}
				}

				setState(976);
				match(STRING_);
				setState(977);
				match(RPAR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(978);
				match(LPAR);
				setState(979);
				match(OUTPUT);
				setState(981);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(980);
					match(VAR);
					}
				}

				setState(983);
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
		enterRule(_localctx, 134, RULE_wconst);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(986);
			match(LPAR);
			setState(987);
			match(CONST);
			setState(988);
			literal();
			setState(989);
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
		enterRule(_localctx, 136, RULE_constList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(994);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAR) {
				{
				{
				setState(991);
				wconst();
				}
				}
				setState(996);
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
		enterRule(_localctx, 138, RULE_script);
		int _la;
		try {
			setState(1011);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,109,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1000);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LPAR) {
					{
					{
					setState(997);
					cmd();
					}
					}
					setState(1002);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1003);
				match(EOF);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1005); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(1004);
					moduleField();
					}
					}
					setState(1007); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==LPAR );
				setState(1009);
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
		enterRule(_localctx, 140, RULE_module);
		int _la;
		try {
			setState(1023);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,111,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1013);
				module_();
				setState(1014);
				match(EOF);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1019);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LPAR) {
					{
					{
					setState(1016);
					moduleField();
					}
					}
					setState(1021);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1022);
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
		"\u0004\u0001\u0093\u0402\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
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
		"E\u0002F\u0007F\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001"+
		"\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0003\u0005\u009c\b\u0005\u0001\u0006\u0001"+
		"\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0003\u0007\u00a6\b\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\t\u0001\t\u0001\t\u0005\t\u00b0\b\t\n\t\f\t\u00b3\t\t\u0001\t"+
		"\u0001\t\u0001\t\u0003\t\u00b8\b\t\u0001\t\u0005\t\u00bb\b\t\n\t\f\t\u00be"+
		"\t\t\u0001\n\u0001\n\u0001\n\u0005\n\u00c3\b\n\n\n\f\n\u00c6\t\n\u0001"+
		"\n\u0005\n\u00c9\b\n\n\n\f\n\u00cc\t\n\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\f\u0001\f\u0003\f\u00d3\b\f\u0001\f\u0001\f\u0001\r\u0001\r\u0003"+
		"\r\u00d9\b\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u00ea\b\u0012"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0004\u0013\u00f6\b\u0013"+
		"\u000b\u0013\f\u0013\u00f7\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013"+
		"\u0109\b\u0013\u0001\u0013\u0003\u0013\u010c\b\u0013\u0001\u0013\u0001"+
		"\u0013\u0003\u0013\u0110\b\u0013\u0001\u0013\u0003\u0013\u0113\b\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013"+
		"\u0126\b\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0003\u0016\u0133\b\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0003\u0017"+
		"\u0138\b\u0017\u0001\u0018\u0001\u0018\u0003\u0018\u013c\b\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0005\u0019\u0143"+
		"\b\u0019\n\u0019\f\u0019\u0146\t\u0019\u0001\u0019\u0005\u0019\u0149\b"+
		"\u0019\n\u0019\f\u0019\u014c\t\u0019\u0001\u0019\u0001\u0019\u0001\u0019"+
		"\u0005\u0019\u0151\b\u0019\n\u0019\f\u0019\u0154\t\u0019\u0001\u0019\u0005"+
		"\u0019\u0157\b\u0019\n\u0019\f\u0019\u015a\t\u0019\u0001\u001a\u0001\u001a"+
		"\u0003\u001a\u015e\b\u001a\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0005\u001b\u0165\b\u001b\n\u001b\f\u001b\u0168\t\u001b\u0001"+
		"\u001b\u0005\u001b\u016b\b\u001b\n\u001b\f\u001b\u016e\t\u001b\u0001\u001b"+
		"\u0001\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0005\u001c\u0175\b\u001c"+
		"\n\u001c\f\u001c\u0178\t\u001c\u0001\u001c\u0005\u001c\u017b\b\u001c\n"+
		"\u001c\f\u001c\u017e\t\u001c\u0001\u001c\u0001\u001c\u0001\u001d\u0001"+
		"\u001d\u0003\u001d\u0184\b\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0003"+
		"\u001d\u0189\b\u001d\u0001\u001d\u0001\u001d\u0003\u001d\u018d\b\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0003\u001d\u0192\b\u001d\u0001\u001d"+
		"\u0001\u001d\u0003\u001d\u0196\b\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0003\u001d\u019b\b\u001d\u0001\u001d\u0003\u001d\u019e\b\u001d\u0001"+
		"\u001d\u0001\u001d\u0003\u001d\u01a2\b\u001d\u0003\u001d\u01a4\b\u001d"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001f"+
		"\u0003\u001f\u01ac\b\u001f\u0001\u001f\u0001\u001f\u0001 \u0001 \u0001"+
		" \u0001 \u0001!\u0001!\u0005!\u01b6\b!\n!\f!\u01b9\t!\u0001!\u0001!\u0001"+
		"!\u0001!\u0003!\u01bf\b!\u0001!\u0001!\u0001!\u0003!\u01c4\b!\u0001!\u0001"+
		"!\u0001!\u0003!\u01c9\b!\u0001!\u0003!\u01cc\b!\u0001!\u0005!\u01cf\b"+
		"!\n!\f!\u01d2\t!\u0001!\u0001!\u0001!\u0001!\u0001!\u0001!\u0001!\u0001"+
		"!\u0003!\u01dc\b!\u0003!\u01de\b!\u0001\"\u0003\"\u01e1\b\"\u0001\"\u0001"+
		"\"\u0001#\u0001#\u0001#\u0005#\u01e8\b#\n#\f#\u01eb\t#\u0001#\u0005#\u01ee"+
		"\b#\n#\f#\u01f1\t#\u0001#\u0001#\u0001$\u0001$\u0001$\u0005$\u01f8\b$"+
		"\n$\f$\u01fb\t$\u0001$\u0005$\u01fe\b$\n$\f$\u0201\t$\u0001$\u0005$\u0204"+
		"\b$\n$\f$\u0207\t$\u0001%\u0005%\u020a\b%\n%\f%\u020d\t%\u0001%\u0003"+
		"%\u0210\b%\u0001&\u0001&\u0001\'\u0001\'\u0001\'\u0003\'\u0217\b\'\u0001"+
		"\'\u0001\'\u0001\'\u0001(\u0003(\u021d\b(\u0001(\u0001(\u0001(\u0003("+
		"\u0222\b(\u0001(\u0001(\u0001(\u0001(\u0001(\u0003(\u0229\b(\u0001)\u0001"+
		")\u0001)\u0001*\u0001*\u0001*\u0005*\u0231\b*\n*\f*\u0234\t*\u0001*\u0001"+
		"*\u0001*\u0003*\u0239\b*\u0001*\u0005*\u023c\b*\n*\f*\u023f\t*\u0001*"+
		"\u0001*\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0003+\u0249\b+\u0001"+
		",\u0001,\u0001,\u0003,\u024e\b,\u0001,\u0001,\u0001,\u0001,\u0005,\u0254"+
		"\b,\n,\f,\u0257\t,\u0001,\u0001,\u0001,\u0001,\u0001,\u0003,\u025e\b,"+
		"\u0001,\u0001,\u0005,\u0262\b,\n,\f,\u0265\t,\u0001,\u0001,\u0003,\u0269"+
		"\b,\u0001-\u0001-\u0001-\u0003-\u026e\b-\u0001-\u0001-\u0001-\u0001.\u0001"+
		".\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0005"+
		".\u027e\b.\n.\f.\u0281\t.\u0001.\u0001.\u0003.\u0285\b.\u0001/\u0001/"+
		"\u0001/\u0003/\u028a\b/\u0001/\u0001/\u0001/\u0001/\u0005/\u0290\b/\n"+
		"/\f/\u0293\t/\u0001/\u0001/\u0001/\u0001/\u0001/\u0003/\u029a\b/\u0001"+
		"/\u0001/\u0005/\u029e\b/\n/\f/\u02a1\t/\u0001/\u0001/\u0003/\u02a5\b/"+
		"\u00010\u00010\u00010\u00030\u02aa\b0\u00010\u00010\u00010\u00011\u0001"+
		"1\u00011\u00011\u00011\u00011\u00011\u00011\u00011\u00011\u00051\u02b9"+
		"\b1\n1\f1\u02bc\t1\u00011\u00031\u02bf\b1\u00012\u00012\u00012\u00032"+
		"\u02c4\b2\u00012\u00012\u00012\u00013\u00013\u00013\u00013\u00013\u0001"+
		"3\u00013\u00013\u00013\u00033\u02d2\b3\u00014\u00014\u00014\u00034\u02d7"+
		"\b4\u00014\u00014\u00014\u00014\u00014\u00014\u00034\u02df\b4\u00014\u0001"+
		"4\u00014\u00014\u00014\u00014\u00034\u02e7\b4\u00014\u00014\u00014\u0001"+
		"4\u00014\u00014\u00034\u02ef\b4\u00014\u00014\u00014\u00014\u00014\u0001"+
		"4\u00034\u02f7\b4\u00014\u00014\u00014\u00034\u02fc\b4\u00015\u00015\u0001"+
		"5\u00015\u00015\u00015\u00015\u00016\u00016\u00016\u00016\u00016\u0001"+
		"6\u00017\u00017\u00017\u00017\u00017\u00017\u00017\u00017\u00017\u0001"+
		"7\u00017\u00017\u00017\u00017\u00017\u00017\u00017\u00017\u00017\u0001"+
		"7\u00037\u031f\b7\u00018\u00018\u00018\u00018\u00018\u00018\u00019\u0001"+
		"9\u00019\u00019\u00019\u0001:\u0001:\u0001:\u0003:\u032f\b:\u0001:\u0001"+
		":\u0001:\u0001;\u0001;\u0001;\u0001;\u0001;\u0001<\u0001<\u0001<\u0001"+
		"<\u0001<\u0001<\u0001<\u0001<\u0001<\u0001<\u0003<\u0343\b<\u0001=\u0001"+
		"=\u0001=\u0003=\u0348\b=\u0001=\u0005=\u034b\b=\n=\f=\u034e\t=\u0001="+
		"\u0001=\u0001>\u0001>\u0001>\u0001>\u0003>\u0356\b>\u0001>\u0001>\u0005"+
		">\u035a\b>\n>\f>\u035d\t>\u0001>\u0003>\u0360\b>\u0001?\u0001?\u0001?"+
		"\u0003?\u0365\b?\u0001?\u0001?\u0001?\u0001?\u0001?\u0001?\u0001?\u0003"+
		"?\u036e\b?\u0001?\u0001?\u0001?\u0003?\u0373\b?\u0001@\u0001@\u0001@\u0001"+
		"@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001"+
		"@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001"+
		"@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001"+
		"@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001"+
		"@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001@\u0003@\u03a9"+
		"\b@\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0003A\u03b2\bA\u0001"+
		"A\u0001A\u0001A\u0003A\u03b7\bA\u0001B\u0001B\u0001B\u0003B\u03bc\bB\u0001"+
		"B\u0005B\u03bf\bB\nB\fB\u03c2\tB\u0001B\u0001B\u0001B\u0001B\u0003B\u03c8"+
		"\bB\u0001B\u0001B\u0001B\u0001B\u0001B\u0003B\u03cf\bB\u0001B\u0001B\u0001"+
		"B\u0001B\u0001B\u0003B\u03d6\bB\u0001B\u0003B\u03d9\bB\u0001C\u0001C\u0001"+
		"C\u0001C\u0001C\u0001D\u0005D\u03e1\bD\nD\fD\u03e4\tD\u0001E\u0005E\u03e7"+
		"\bE\nE\fE\u03ea\tE\u0001E\u0001E\u0004E\u03ee\bE\u000bE\fE\u03ef\u0001"+
		"E\u0001E\u0003E\u03f4\bE\u0001F\u0001F\u0001F\u0001F\u0005F\u03fa\bF\n"+
		"F\fF\u03fd\tF\u0001F\u0003F\u0400\bF\u0001F\u0000\u0000G\u0000\u0002\u0004"+
		"\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \""+
		"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086"+
		"\u0088\u008a\u008c\u0000\u0006\u0001\u0000\u0004\u0005\u0001\u0000\n\u000b"+
		"\u0001\u0000qr\u0001\u0000\u0003\u0005\u0002\u0000\u0003\u0003\u0090\u0090"+
		"\u0001\u0000\u0080\u0081\u046d\u0000\u008e\u0001\u0000\u0000\u0000\u0002"+
		"\u0090\u0001\u0000\u0000\u0000\u0004\u0092\u0001\u0000\u0000\u0000\u0006"+
		"\u0094\u0001\u0000\u0000\u0000\b\u0096\u0001\u0000\u0000\u0000\n\u009b"+
		"\u0001\u0000\u0000\u0000\f\u009d\u0001\u0000\u0000\u0000\u000e\u00a5\u0001"+
		"\u0000\u0000\u0000\u0010\u00a7\u0001\u0000\u0000\u0000\u0012\u00bc\u0001"+
		"\u0000\u0000\u0000\u0014\u00ca\u0001\u0000\u0000\u0000\u0016\u00cd\u0001"+
		"\u0000\u0000\u0000\u0018\u00d0\u0001\u0000\u0000\u0000\u001a\u00d6\u0001"+
		"\u0000\u0000\u0000\u001c\u00da\u0001\u0000\u0000\u0000\u001e\u00df\u0001"+
		"\u0000\u0000\u0000 \u00e1\u0001\u0000\u0000\u0000\"\u00e3\u0001\u0000"+
		"\u0000\u0000$\u00e9\u0001\u0000\u0000\u0000&\u0125\u0001\u0000\u0000\u0000"+
		"(\u0127\u0001\u0000\u0000\u0000*\u012a\u0001\u0000\u0000\u0000,\u012d"+
		"\u0001\u0000\u0000\u0000.\u0134\u0001\u0000\u0000\u00000\u0139\u0001\u0000"+
		"\u0000\u00002\u014a\u0001\u0000\u0000\u00004\u015b\u0001\u0000\u0000\u0000"+
		"6\u016c\u0001\u0000\u0000\u00008\u017c\u0001\u0000\u0000\u0000:\u01a3"+
		"\u0001\u0000\u0000\u0000<\u01a5\u0001\u0000\u0000\u0000>\u01ab\u0001\u0000"+
		"\u0000\u0000@\u01af\u0001\u0000\u0000\u0000B\u01dd\u0001\u0000\u0000\u0000"+
		"D\u01e0\u0001\u0000\u0000\u0000F\u01ef\u0001\u0000\u0000\u0000H\u01ff"+
		"\u0001\u0000\u0000\u0000J\u020b\u0001\u0000\u0000\u0000L\u0211\u0001\u0000"+
		"\u0000\u0000N\u0213\u0001\u0000\u0000\u0000P\u0228\u0001\u0000\u0000\u0000"+
		"R\u022a\u0001\u0000\u0000\u0000T\u023d\u0001\u0000\u0000\u0000V\u0248"+
		"\u0001\u0000\u0000\u0000X\u0268\u0001\u0000\u0000\u0000Z\u026a\u0001\u0000"+
		"\u0000\u0000\\\u0284\u0001\u0000\u0000\u0000^\u02a4\u0001\u0000\u0000"+
		"\u0000`\u02a6\u0001\u0000\u0000\u0000b\u02be\u0001\u0000\u0000\u0000d"+
		"\u02c0\u0001\u0000\u0000\u0000f\u02d1\u0001\u0000\u0000\u0000h\u02fb\u0001"+
		"\u0000\u0000\u0000j\u02fd\u0001\u0000\u0000\u0000l\u0304\u0001\u0000\u0000"+
		"\u0000n\u031e\u0001\u0000\u0000\u0000p\u0320\u0001\u0000\u0000\u0000r"+
		"\u0326\u0001\u0000\u0000\u0000t\u032b\u0001\u0000\u0000\u0000v\u0333\u0001"+
		"\u0000\u0000\u0000x\u0342\u0001\u0000\u0000\u0000z\u0344\u0001\u0000\u0000"+
		"\u0000|\u035f\u0001\u0000\u0000\u0000~\u0372\u0001\u0000\u0000\u0000\u0080"+
		"\u03a8\u0001\u0000\u0000\u0000\u0082\u03b6\u0001\u0000\u0000\u0000\u0084"+
		"\u03d8\u0001\u0000\u0000\u0000\u0086\u03da\u0001\u0000\u0000\u0000\u0088"+
		"\u03e2\u0001\u0000\u0000\u0000\u008a\u03f3\u0001\u0000\u0000\u0000\u008c"+
		"\u03ff\u0001\u0000\u0000\u0000\u008e\u008f\u0007\u0000\u0000\u0000\u008f"+
		"\u0001\u0001\u0000\u0000\u0000\u0090\u0091\u0005\u0006\u0000\u0000\u0091"+
		"\u0003\u0001\u0000\u0000\u0000\u0092\u0093\u0005\u0007\u0000\u0000\u0093"+
		"\u0005\u0001\u0000\u0000\u0000\u0094\u0095\u0007\u0001\u0000\u0000\u0095"+
		"\u0007\u0001\u0000\u0000\u0000\u0096\u0097\u0005\u0091\u0000\u0000\u0097"+
		"\t\u0001\u0000\u0000\u0000\u0098\u009c\u0003\u0004\u0002\u0000\u0099\u009c"+
		"\u0003\b\u0004\u0000\u009a\u009c\u0003\u0006\u0003\u0000\u009b\u0098\u0001"+
		"\u0000\u0000\u0000\u009b\u0099\u0001\u0000\u0000\u0000\u009b\u009a\u0001"+
		"\u0000\u0000\u0000\u009c\u000b\u0001\u0000\u0000\u0000\u009d\u009e\u0007"+
		"\u0002\u0000\u0000\u009e\r\u0001\u0000\u0000\u0000\u009f\u00a6\u0003\n"+
		"\u0005\u0000\u00a0\u00a1\u0005\u0001\u0000\u0000\u00a1\u00a2\u0005\f\u0000"+
		"\u0000\u00a2\u00a3\u0003\n\u0005\u0000\u00a3\u00a4\u0005\u0002\u0000\u0000"+
		"\u00a4\u00a6\u0001\u0000\u0000\u0000\u00a5\u009f\u0001\u0000\u0000\u0000"+
		"\u00a5\u00a0\u0001\u0000\u0000\u0000\u00a6\u000f\u0001\u0000\u0000\u0000"+
		"\u00a7\u00a8\u0005\u0001\u0000\u0000\u00a8\u00a9\u0005q\u0000\u0000\u00a9"+
		"\u00aa\u0003\u0016\u000b\u0000\u00aa\u00ab\u0005\u0002\u0000\u0000\u00ab"+
		"\u0011\u0001\u0000\u0000\u0000\u00ac\u00ad\u0005\u0001\u0000\u0000\u00ad"+
		"\u00b7\u0005t\u0000\u0000\u00ae\u00b0\u0003\n\u0005\u0000\u00af\u00ae"+
		"\u0001\u0000\u0000\u0000\u00b0\u00b3\u0001\u0000\u0000\u0000\u00b1\u00af"+
		"\u0001\u0000\u0000\u0000\u00b1\u00b2\u0001\u0000\u0000\u0000\u00b2\u00b8"+
		"\u0001\u0000\u0000\u0000\u00b3\u00b1\u0001\u0000\u0000\u0000\u00b4\u00b5"+
		"\u0003\"\u0011\u0000\u00b5\u00b6\u0003\n\u0005\u0000\u00b6\u00b8\u0001"+
		"\u0000\u0000\u0000\u00b7\u00b1\u0001\u0000\u0000\u0000\u00b7\u00b4\u0001"+
		"\u0000\u0000\u0000\u00b8\u00b9\u0001\u0000\u0000\u0000\u00b9\u00bb\u0005"+
		"\u0002\u0000\u0000\u00ba\u00ac\u0001\u0000\u0000\u0000\u00bb\u00be\u0001"+
		"\u0000\u0000\u0000\u00bc\u00ba\u0001\u0000\u0000\u0000\u00bc\u00bd\u0001"+
		"\u0000\u0000\u0000\u00bd\u0013\u0001\u0000\u0000\u0000\u00be\u00bc\u0001"+
		"\u0000\u0000\u0000\u00bf\u00c0\u0005\u0001\u0000\u0000\u00c0\u00c4\u0005"+
		"u\u0000\u0000\u00c1\u00c3\u0003\n\u0005\u0000\u00c2\u00c1\u0001\u0000"+
		"\u0000\u0000\u00c3\u00c6\u0001\u0000\u0000\u0000\u00c4\u00c2\u0001\u0000"+
		"\u0000\u0000\u00c4\u00c5\u0001\u0000\u0000\u0000\u00c5\u00c7\u0001\u0000"+
		"\u0000\u0000\u00c6\u00c4\u0001\u0000\u0000\u0000\u00c7\u00c9\u0005\u0002"+
		"\u0000\u0000\u00c8\u00bf\u0001\u0000\u0000\u0000\u00c9\u00cc\u0001\u0000"+
		"\u0000\u0000\u00ca\u00c8\u0001\u0000\u0000\u0000\u00ca\u00cb\u0001\u0000"+
		"\u0000\u0000\u00cb\u0015\u0001\u0000\u0000\u0000\u00cc\u00ca\u0001\u0000"+
		"\u0000\u0000\u00cd\u00ce\u0003\u0012\t\u0000\u00ce\u00cf\u0003\u0014\n"+
		"\u0000\u00cf\u0017\u0001\u0000\u0000\u0000\u00d0\u00d2\u0005\u0003\u0000"+
		"\u0000\u00d1\u00d3\u0005\u0003\u0000\u0000\u00d2\u00d1\u0001\u0000\u0000"+
		"\u0000\u00d2\u00d3\u0001\u0000\u0000\u0000\u00d3\u00d4\u0001\u0000\u0000"+
		"\u0000\u00d4\u00d5\u0003\u0006\u0003\u0000\u00d5\u0019\u0001\u0000\u0000"+
		"\u0000\u00d6\u00d8\u0005\u0003\u0000\u0000\u00d7\u00d9\u0005\u0003\u0000"+
		"\u0000\u00d8\u00d7\u0001\u0000\u0000\u0000\u00d8\u00d9\u0001\u0000\u0000"+
		"\u0000\u00d9\u001b\u0001\u0000\u0000\u0000\u00da\u00db\u0005\u0001\u0000"+
		"\u0000\u00db\u00dc\u0005p\u0000\u0000\u00dc\u00dd\u0003 \u0010\u0000\u00dd"+
		"\u00de\u0005\u0002\u0000\u0000\u00de\u001d\u0001\u0000\u0000\u0000\u00df"+
		"\u00e0\u0007\u0003\u0000\u0000\u00e0\u001f\u0001\u0000\u0000\u0000\u00e1"+
		"\u00e2\u0007\u0004\u0000\u0000\u00e2!\u0001\u0000\u0000\u0000\u00e3\u00e4"+
		"\u0005\u0090\u0000\u0000\u00e4#\u0001\u0000\u0000\u0000\u00e5\u00ea\u0003"+
		"&\u0013\u0000\u00e6\u00ea\u00034\u001a\u0000\u00e7\u00ea\u0003:\u001d"+
		"\u0000\u00e8\u00ea\u0003@ \u0000\u00e9\u00e5\u0001\u0000\u0000\u0000\u00e9"+
		"\u00e6\u0001\u0000\u0000\u0000\u00e9\u00e7\u0001\u0000\u0000\u0000\u00e9"+
		"\u00e8\u0001\u0000\u0000\u0000\u00ea%\u0001\u0000\u0000\u0000\u00eb\u0126"+
		"\u0005\u0011\u0000\u0000\u00ec\u0126\u0005\r\u0000\u0000\u00ed\u0126\u0005"+
		"\u0012\u0000\u0000\u00ee\u0126\u0005\u001d\u0000\u0000\u00ef\u00f0\u0005"+
		"\u0016\u0000\u0000\u00f0\u0126\u0003 \u0010\u0000\u00f1\u00f2\u0005\u0017"+
		"\u0000\u0000\u00f2\u0126\u0003 \u0010\u0000\u00f3\u00f5\u0005\u0018\u0000"+
		"\u0000\u00f4\u00f6\u0003 \u0010\u0000\u00f5\u00f4\u0001\u0000\u0000\u0000"+
		"\u00f6\u00f7\u0001\u0000\u0000\u0000\u00f7\u00f5\u0001\u0000\u0000\u0000"+
		"\u00f7\u00f8\u0001\u0000\u0000\u0000\u00f8\u0126\u0001\u0000\u0000\u0000"+
		"\u00f9\u0126\u0005\u0019\u0000\u0000\u00fa\u00fb\u0005\u001e\u0000\u0000"+
		"\u00fb\u0126\u0003 \u0010\u0000\u00fc\u00fd\u0005 \u0000\u0000\u00fd\u0126"+
		"\u0003 \u0010\u0000\u00fe\u00ff\u0005!\u0000\u0000\u00ff\u0126\u0003 "+
		"\u0010\u0000\u0100\u0101\u0005\"\u0000\u0000\u0101\u0126\u0003 \u0010"+
		"\u0000\u0102\u0103\u0005#\u0000\u0000\u0103\u0126\u0003 \u0010\u0000\u0104"+
		"\u0105\u0005$\u0000\u0000\u0105\u0126\u0003 \u0010\u0000\u0106\u0108\u0003"+
		",\u0016\u0000\u0107\u0109\u0003(\u0014\u0000\u0108\u0107\u0001\u0000\u0000"+
		"\u0000\u0108\u0109\u0001\u0000\u0000\u0000\u0109\u010b\u0001\u0000\u0000"+
		"\u0000\u010a\u010c\u0003*\u0015\u0000\u010b\u010a\u0001\u0000\u0000\u0000"+
		"\u010b\u010c\u0001\u0000\u0000\u0000\u010c\u0126\u0001\u0000\u0000\u0000"+
		"\u010d\u010f\u0003.\u0017\u0000\u010e\u0110\u0003(\u0014\u0000\u010f\u010e"+
		"\u0001\u0000\u0000\u0000\u010f\u0110\u0001\u0000\u0000\u0000\u0110\u0112"+
		"\u0001\u0000\u0000\u0000\u0111\u0113\u0003*\u0015\u0000\u0112\u0111\u0001"+
		"\u0000\u0000\u0000\u0112\u0113\u0001\u0000\u0000\u0000\u0113\u0126\u0001"+
		"\u0000\u0000\u0000\u0114\u0126\u0005f\u0000\u0000\u0115\u0126\u0005g\u0000"+
		"\u0000\u0116\u0126\u0005h\u0000\u0000\u0117\u0126\u0005i\u0000\u0000\u0118"+
		"\u0119\u0005j\u0000\u0000\u0119\u0126\u0003 \u0010\u0000\u011a\u011b\u0005"+
		"\b\u0000\u0000\u011b\u0126\u0003\u001e\u000f\u0000\u011c\u0126\u0005\t"+
		"\u0000\u0000\u011d\u0126\u0005\u000e\u0000\u0000\u011e\u0126\u0005\u000f"+
		"\u0000\u0000\u011f\u0126\u0005\u0010\u0000\u0000\u0120\u0126\u0005k\u0000"+
		"\u0000\u0121\u0126\u0005l\u0000\u0000\u0122\u0126\u0005m\u0000\u0000\u0123"+
		"\u0126\u0005n\u0000\u0000\u0124\u0126\u0005o\u0000\u0000\u0125\u00eb\u0001"+
		"\u0000\u0000\u0000\u0125\u00ec\u0001\u0000\u0000\u0000\u0125\u00ed\u0001"+
		"\u0000\u0000\u0000\u0125\u00ee\u0001\u0000\u0000\u0000\u0125\u00ef\u0001"+
		"\u0000\u0000\u0000\u0125\u00f1\u0001\u0000\u0000\u0000\u0125\u00f3\u0001"+
		"\u0000\u0000\u0000\u0125\u00f9\u0001\u0000\u0000\u0000\u0125\u00fa\u0001"+
		"\u0000\u0000\u0000\u0125\u00fc\u0001\u0000\u0000\u0000\u0125\u00fe\u0001"+
		"\u0000\u0000\u0000\u0125\u0100\u0001\u0000\u0000\u0000\u0125\u0102\u0001"+
		"\u0000\u0000\u0000\u0125\u0104\u0001\u0000\u0000\u0000\u0125\u0106\u0001"+
		"\u0000\u0000\u0000\u0125\u010d\u0001\u0000\u0000\u0000\u0125\u0114\u0001"+
		"\u0000\u0000\u0000\u0125\u0115\u0001\u0000\u0000\u0000\u0125\u0116\u0001"+
		"\u0000\u0000\u0000\u0125\u0117\u0001\u0000\u0000\u0000\u0125\u0118\u0001"+
		"\u0000\u0000\u0000\u0125\u011a\u0001\u0000\u0000\u0000\u0125\u011c\u0001"+
		"\u0000\u0000\u0000\u0125\u011d\u0001\u0000\u0000\u0000\u0125\u011e\u0001"+
		"\u0000\u0000\u0000\u0125\u011f\u0001\u0000\u0000\u0000\u0125\u0120\u0001"+
		"\u0000\u0000\u0000\u0125\u0121\u0001\u0000\u0000\u0000\u0125\u0122\u0001"+
		"\u0000\u0000\u0000\u0125\u0123\u0001\u0000\u0000\u0000\u0125\u0124\u0001"+
		"\u0000\u0000\u0000\u0126\'\u0001\u0000\u0000\u0000\u0127\u0128\u0005("+
		"\u0000\u0000\u0128\u0129\u0005\u0003\u0000\u0000\u0129)\u0001\u0000\u0000"+
		"\u0000\u012a\u012b\u0005)\u0000\u0000\u012b\u012c\u0005\u0003\u0000\u0000"+
		"\u012c+\u0001\u0000\u0000\u0000\u012d\u012e\u0003\u0004\u0002\u0000\u012e"+
		"\u0132\u0005%\u0000\u0000\u012f\u0130\u0005+\u0000\u0000\u0130\u0131\u0005"+
		"\'\u0000\u0000\u0131\u0133\u0005*\u0000\u0000\u0132\u012f\u0001\u0000"+
		"\u0000\u0000\u0132\u0133\u0001\u0000\u0000\u0000\u0133-\u0001\u0000\u0000"+
		"\u0000\u0134\u0135\u0003\u0004\u0002\u0000\u0135\u0137\u0005&\u0000\u0000"+
		"\u0136\u0138\u0005+\u0000\u0000\u0137\u0136\u0001\u0000\u0000\u0000\u0137"+
		"\u0138\u0001\u0000\u0000\u0000\u0138/\u0001\u0000\u0000\u0000\u0139\u013b"+
		"\u0005\u001f\u0000\u0000\u013a\u013c\u0003\u001c\u000e\u0000\u013b\u013a"+
		"\u0001\u0000\u0000\u0000\u013b\u013c\u0001\u0000\u0000\u0000\u013c\u013d"+
		"\u0001\u0000\u0000\u0000\u013d\u013e\u00032\u0019\u0000\u013e1\u0001\u0000"+
		"\u0000\u0000\u013f\u0140\u0005\u0001\u0000\u0000\u0140\u0144\u0005t\u0000"+
		"\u0000\u0141\u0143\u0003\n\u0005\u0000\u0142\u0141\u0001\u0000\u0000\u0000"+
		"\u0143\u0146\u0001\u0000\u0000\u0000\u0144\u0142\u0001\u0000\u0000\u0000"+
		"\u0144\u0145\u0001\u0000\u0000\u0000\u0145\u0147\u0001\u0000\u0000\u0000"+
		"\u0146\u0144\u0001\u0000\u0000\u0000\u0147\u0149\u0005\u0002\u0000\u0000"+
		"\u0148\u013f\u0001\u0000\u0000\u0000\u0149\u014c\u0001\u0000\u0000\u0000"+
		"\u014a\u0148\u0001\u0000\u0000\u0000\u014a\u014b\u0001\u0000\u0000\u0000"+
		"\u014b\u0158\u0001\u0000\u0000\u0000\u014c\u014a\u0001\u0000\u0000\u0000"+
		"\u014d\u014e\u0005\u0001\u0000\u0000\u014e\u0152\u0005u\u0000\u0000\u014f"+
		"\u0151\u0003\n\u0005\u0000\u0150\u014f\u0001\u0000\u0000\u0000\u0151\u0154"+
		"\u0001\u0000\u0000\u0000\u0152\u0150\u0001\u0000\u0000\u0000\u0152\u0153"+
		"\u0001\u0000\u0000\u0000\u0153\u0155\u0001\u0000\u0000\u0000\u0154\u0152"+
		"\u0001\u0000\u0000\u0000\u0155\u0157\u0005\u0002\u0000\u0000\u0156\u014d"+
		"\u0001\u0000\u0000\u0000\u0157\u015a\u0001\u0000\u0000\u0000\u0158\u0156"+
		"\u0001\u0000\u0000\u0000\u0158\u0159\u0001\u0000\u0000\u0000\u01593\u0001"+
		"\u0000\u0000\u0000\u015a\u0158\u0001\u0000\u0000\u0000\u015b\u015d\u0005"+
		"\u001f\u0000\u0000\u015c\u015e\u0003\u001c\u000e\u0000\u015d\u015c\u0001"+
		"\u0000\u0000\u0000\u015d\u015e\u0001\u0000\u0000\u0000\u015e\u015f\u0001"+
		"\u0000\u0000\u0000\u015f\u0160\u00036\u001b\u0000\u01605\u0001\u0000\u0000"+
		"\u0000\u0161\u0162\u0005\u0001\u0000\u0000\u0162\u0166\u0005t\u0000\u0000"+
		"\u0163\u0165\u0003\n\u0005\u0000\u0164\u0163\u0001\u0000\u0000\u0000\u0165"+
		"\u0168\u0001\u0000\u0000\u0000\u0166\u0164\u0001\u0000\u0000\u0000\u0166"+
		"\u0167\u0001\u0000\u0000\u0000\u0167\u0169\u0001\u0000\u0000\u0000\u0168"+
		"\u0166\u0001\u0000\u0000\u0000\u0169\u016b\u0005\u0002\u0000\u0000\u016a"+
		"\u0161\u0001\u0000\u0000\u0000\u016b\u016e\u0001\u0000\u0000\u0000\u016c"+
		"\u016a\u0001\u0000\u0000\u0000\u016c\u016d\u0001\u0000\u0000\u0000\u016d"+
		"\u016f\u0001\u0000\u0000\u0000\u016e\u016c\u0001\u0000\u0000\u0000\u016f"+
		"\u0170\u00038\u001c\u0000\u01707\u0001\u0000\u0000\u0000\u0171\u0172\u0005"+
		"\u0001\u0000\u0000\u0172\u0176\u0005u\u0000\u0000\u0173\u0175\u0003\n"+
		"\u0005\u0000\u0174\u0173\u0001\u0000\u0000\u0000\u0175\u0178\u0001\u0000"+
		"\u0000\u0000\u0176\u0174\u0001\u0000\u0000\u0000\u0176\u0177\u0001\u0000"+
		"\u0000\u0000\u0177\u0179\u0001\u0000\u0000\u0000\u0178\u0176\u0001\u0000"+
		"\u0000\u0000\u0179\u017b\u0005\u0002\u0000\u0000\u017a\u0171\u0001\u0000"+
		"\u0000\u0000\u017b\u017e\u0001\u0000\u0000\u0000\u017c\u017a\u0001\u0000"+
		"\u0000\u0000\u017c\u017d\u0001\u0000\u0000\u0000\u017d\u017f\u0001\u0000"+
		"\u0000\u0000\u017e\u017c\u0001\u0000\u0000\u0000\u017f\u0180\u0003$\u0012"+
		"\u0000\u01809\u0001\u0000\u0000\u0000\u0181\u0183\u0005\u0013\u0000\u0000"+
		"\u0182\u0184\u0003\"\u0011\u0000\u0183\u0182\u0001\u0000\u0000\u0000\u0183"+
		"\u0184\u0001\u0000\u0000\u0000\u0184\u0185\u0001\u0000\u0000\u0000\u0185"+
		"\u0186\u0003>\u001f\u0000\u0186\u0188\u0005\u0015\u0000\u0000\u0187\u0189"+
		"\u0003\"\u0011\u0000\u0188\u0187\u0001\u0000\u0000\u0000\u0188\u0189\u0001"+
		"\u0000\u0000\u0000\u0189\u01a4\u0001\u0000\u0000\u0000\u018a\u018c\u0005"+
		"\u0014\u0000\u0000\u018b\u018d\u0003\"\u0011\u0000\u018c\u018b\u0001\u0000"+
		"\u0000\u0000\u018c\u018d\u0001\u0000\u0000\u0000\u018d\u018e\u0001\u0000"+
		"\u0000\u0000\u018e\u018f\u0003>\u001f\u0000\u018f\u0191\u0005\u0015\u0000"+
		"\u0000\u0190\u0192\u0003\"\u0011\u0000\u0191\u0190\u0001\u0000\u0000\u0000"+
		"\u0191\u0192\u0001\u0000\u0000\u0000\u0192\u01a4\u0001\u0000\u0000\u0000"+
		"\u0193\u0195\u0005\u001a\u0000\u0000\u0194\u0196\u0003\"\u0011\u0000\u0195"+
		"\u0194\u0001\u0000\u0000\u0000\u0195\u0196\u0001\u0000\u0000\u0000\u0196"+
		"\u0197\u0001\u0000\u0000\u0000\u0197\u019d\u0003>\u001f\u0000\u0198\u019a"+
		"\u0005\u001c\u0000\u0000\u0199\u019b\u0003\"\u0011\u0000\u019a\u0199\u0001"+
		"\u0000\u0000\u0000\u019a\u019b\u0001\u0000\u0000\u0000\u019b\u019c\u0001"+
		"\u0000\u0000\u0000\u019c\u019e\u0003J%\u0000\u019d\u0198\u0001\u0000\u0000"+
		"\u0000\u019d\u019e\u0001\u0000\u0000\u0000\u019e\u019f\u0001\u0000\u0000"+
		"\u0000\u019f\u01a1\u0005\u0015\u0000\u0000\u01a0\u01a2\u0003\"\u0011\u0000"+
		"\u01a1\u01a0\u0001\u0000\u0000\u0000\u01a1\u01a2\u0001\u0000\u0000\u0000"+
		"\u01a2\u01a4\u0001\u0000\u0000\u0000\u01a3\u0181\u0001\u0000\u0000\u0000"+
		"\u01a3\u018a\u0001\u0000\u0000\u0000\u01a3\u0193\u0001\u0000\u0000\u0000"+
		"\u01a4;\u0001\u0000\u0000\u0000\u01a5\u01a6\u0005\u0001\u0000\u0000\u01a6"+
		"\u01a7\u0005u\u0000\u0000\u01a7\u01a8\u0003\n\u0005\u0000\u01a8\u01a9"+
		"\u0005\u0002\u0000\u0000\u01a9=\u0001\u0000\u0000\u0000\u01aa\u01ac\u0003"+
		"<\u001e\u0000\u01ab\u01aa\u0001\u0000\u0000\u0000\u01ab\u01ac\u0001\u0000"+
		"\u0000\u0000\u01ac\u01ad\u0001\u0000\u0000\u0000\u01ad\u01ae\u0003J%\u0000"+
		"\u01ae?\u0001\u0000\u0000\u0000\u01af\u01b0\u0005\u0001\u0000\u0000\u01b0"+
		"\u01b1\u0003B!\u0000\u01b1\u01b2\u0005\u0002\u0000\u0000\u01b2A\u0001"+
		"\u0000\u0000\u0000\u01b3\u01b7\u0003&\u0013\u0000\u01b4\u01b6\u0003B!"+
		"\u0000\u01b5\u01b4\u0001\u0000\u0000\u0000\u01b6\u01b9\u0001\u0000\u0000"+
		"\u0000\u01b7\u01b5\u0001\u0000\u0000\u0000\u01b7\u01b8\u0001\u0000\u0000"+
		"\u0000\u01b8\u01de\u0001\u0000\u0000\u0000\u01b9\u01b7\u0001\u0000\u0000"+
		"\u0000\u01ba\u01bb\u0005\u001f\u0000\u0000\u01bb\u01de\u0003D\"\u0000"+
		"\u01bc\u01be\u0005\u0013\u0000\u0000\u01bd\u01bf\u0003\"\u0011\u0000\u01be"+
		"\u01bd\u0001\u0000\u0000\u0000\u01be\u01bf\u0001\u0000\u0000\u0000\u01bf"+
		"\u01c0\u0001\u0000\u0000\u0000\u01c0\u01de\u0003>\u001f\u0000\u01c1\u01c3"+
		"\u0005\u0014\u0000\u0000\u01c2\u01c4\u0003\"\u0011\u0000\u01c3\u01c2\u0001"+
		"\u0000\u0000\u0000\u01c3\u01c4\u0001\u0000\u0000\u0000\u01c4\u01c5\u0001"+
		"\u0000\u0000\u0000\u01c5\u01de\u0003>\u001f\u0000\u01c6\u01c8\u0005\u001a"+
		"\u0000\u0000\u01c7\u01c9\u0003\"\u0011\u0000\u01c8\u01c7\u0001\u0000\u0000"+
		"\u0000\u01c8\u01c9\u0001\u0000\u0000\u0000\u01c9\u01cb\u0001\u0000\u0000"+
		"\u0000\u01ca\u01cc\u0003<\u001e\u0000\u01cb\u01ca\u0001\u0000\u0000\u0000"+
		"\u01cb\u01cc\u0001\u0000\u0000\u0000\u01cc\u01d0\u0001\u0000\u0000\u0000"+
		"\u01cd\u01cf\u0003@ \u0000\u01ce\u01cd\u0001\u0000\u0000\u0000\u01cf\u01d2"+
		"\u0001\u0000\u0000\u0000\u01d0\u01ce\u0001\u0000\u0000\u0000\u01d0\u01d1"+
		"\u0001\u0000\u0000\u0000\u01d1\u01d3\u0001\u0000\u0000\u0000\u01d2\u01d0"+
		"\u0001\u0000\u0000\u0000\u01d3\u01d4\u0005\u0001\u0000\u0000\u01d4\u01d5"+
		"\u0005\u001b\u0000\u0000\u01d5\u01db\u0003J%\u0000\u01d6\u01d7\u0005\u0001"+
		"\u0000\u0000\u01d7\u01d8\u0005\u001c\u0000\u0000\u01d8\u01d9\u0003J%\u0000"+
		"\u01d9\u01da\u0005\u0002\u0000\u0000\u01da\u01dc\u0001\u0000\u0000\u0000"+
		"\u01db\u01d6\u0001\u0000\u0000\u0000\u01db\u01dc\u0001\u0000\u0000\u0000"+
		"\u01dc\u01de\u0001\u0000\u0000\u0000\u01dd\u01b3\u0001\u0000\u0000\u0000"+
		"\u01dd\u01ba\u0001\u0000\u0000\u0000\u01dd\u01bc\u0001\u0000\u0000\u0000"+
		"\u01dd\u01c1\u0001\u0000\u0000\u0000\u01dd\u01c6\u0001\u0000\u0000\u0000"+
		"\u01deC\u0001\u0000\u0000\u0000\u01df\u01e1\u0003\u001c\u000e\u0000\u01e0"+
		"\u01df\u0001\u0000\u0000\u0000\u01e0\u01e1\u0001\u0000\u0000\u0000\u01e1"+
		"\u01e2\u0001\u0000\u0000\u0000\u01e2\u01e3\u0003F#\u0000\u01e3E\u0001"+
		"\u0000\u0000\u0000\u01e4\u01e5\u0005\u0001\u0000\u0000\u01e5\u01e9\u0005"+
		"t\u0000\u0000\u01e6\u01e8\u0003\n\u0005\u0000\u01e7\u01e6\u0001\u0000"+
		"\u0000\u0000\u01e8\u01eb\u0001\u0000\u0000\u0000\u01e9\u01e7\u0001\u0000"+
		"\u0000\u0000\u01e9\u01ea\u0001\u0000\u0000\u0000\u01ea\u01ec\u0001\u0000"+
		"\u0000\u0000\u01eb\u01e9\u0001\u0000\u0000\u0000\u01ec\u01ee\u0005\u0002"+
		"\u0000\u0000\u01ed\u01e4\u0001\u0000\u0000\u0000\u01ee\u01f1\u0001\u0000"+
		"\u0000\u0000\u01ef\u01ed\u0001\u0000\u0000\u0000\u01ef\u01f0\u0001\u0000"+
		"\u0000\u0000\u01f0\u01f2\u0001\u0000\u0000\u0000\u01f1\u01ef\u0001\u0000"+
		"\u0000\u0000\u01f2\u01f3\u0003H$\u0000\u01f3G\u0001\u0000\u0000\u0000"+
		"\u01f4\u01f5\u0005\u0001\u0000\u0000\u01f5\u01f9\u0005u\u0000\u0000\u01f6"+
		"\u01f8\u0003\n\u0005\u0000\u01f7\u01f6\u0001\u0000\u0000\u0000\u01f8\u01fb"+
		"\u0001\u0000\u0000\u0000\u01f9\u01f7\u0001\u0000\u0000\u0000\u01f9\u01fa"+
		"\u0001\u0000\u0000\u0000\u01fa\u01fc\u0001\u0000\u0000\u0000\u01fb\u01f9"+
		"\u0001\u0000\u0000\u0000\u01fc\u01fe\u0005\u0002\u0000\u0000\u01fd\u01f4"+
		"\u0001\u0000\u0000\u0000\u01fe\u0201\u0001\u0000\u0000\u0000\u01ff\u01fd"+
		"\u0001\u0000\u0000\u0000\u01ff\u0200\u0001\u0000\u0000\u0000\u0200\u0205"+
		"\u0001\u0000\u0000\u0000\u0201\u01ff\u0001\u0000\u0000\u0000\u0202\u0204"+
		"\u0003B!\u0000\u0203\u0202\u0001\u0000\u0000\u0000\u0204\u0207\u0001\u0000"+
		"\u0000\u0000\u0205\u0203\u0001\u0000\u0000\u0000\u0205\u0206\u0001\u0000"+
		"\u0000\u0000\u0206I\u0001\u0000\u0000\u0000\u0207\u0205\u0001\u0000\u0000"+
		"\u0000\u0208\u020a\u0003$\u0012\u0000\u0209\u0208\u0001\u0000\u0000\u0000"+
		"\u020a\u020d\u0001\u0000\u0000\u0000\u020b\u0209\u0001\u0000\u0000\u0000"+
		"\u020b\u020c\u0001\u0000\u0000\u0000\u020c\u020f\u0001\u0000\u0000\u0000"+
		"\u020d\u020b\u0001\u0000\u0000\u0000\u020e\u0210\u00030\u0018\u0000\u020f"+
		"\u020e\u0001\u0000\u0000\u0000\u020f\u0210\u0001\u0000\u0000\u0000\u0210"+
		"K\u0001\u0000\u0000\u0000\u0211\u0212\u0003J%\u0000\u0212M\u0001\u0000"+
		"\u0000\u0000\u0213\u0214\u0005\u0001\u0000\u0000\u0214\u0216\u0005q\u0000"+
		"\u0000\u0215\u0217\u0003\"\u0011\u0000\u0216\u0215\u0001\u0000\u0000\u0000"+
		"\u0216\u0217\u0001\u0000\u0000\u0000\u0217\u0218\u0001\u0000\u0000\u0000"+
		"\u0218\u0219\u0003P(\u0000\u0219\u021a\u0005\u0002\u0000\u0000\u021aO"+
		"\u0001\u0000\u0000\u0000\u021b\u021d\u0003\u001c\u000e\u0000\u021c\u021b"+
		"\u0001\u0000\u0000\u0000\u021c\u021d\u0001\u0000\u0000\u0000\u021d\u021e"+
		"\u0001\u0000\u0000\u0000\u021e\u0229\u0003R)\u0000\u021f\u0221\u0003l"+
		"6\u0000\u0220\u0222\u0003\u001c\u000e\u0000\u0221\u0220\u0001\u0000\u0000"+
		"\u0000\u0221\u0222\u0001\u0000\u0000\u0000\u0222\u0223\u0001\u0000\u0000"+
		"\u0000\u0223\u0224\u0003\u0016\u000b\u0000\u0224\u0229\u0001\u0000\u0000"+
		"\u0000\u0225\u0226\u0003r9\u0000\u0226\u0227\u0003P(\u0000\u0227\u0229"+
		"\u0001\u0000\u0000\u0000\u0228\u021c\u0001\u0000\u0000\u0000\u0228\u021f"+
		"\u0001\u0000\u0000\u0000\u0228\u0225\u0001\u0000\u0000\u0000\u0229Q\u0001"+
		"\u0000\u0000\u0000\u022a\u022b\u0003\u0016\u000b\u0000\u022b\u022c\u0003"+
		"T*\u0000\u022cS\u0001\u0000\u0000\u0000\u022d\u022e\u0005\u0001\u0000"+
		"\u0000\u022e\u0238\u0005v\u0000\u0000\u022f\u0231\u0003\n\u0005\u0000"+
		"\u0230\u022f\u0001\u0000\u0000\u0000\u0231\u0234\u0001\u0000\u0000\u0000"+
		"\u0232\u0230\u0001\u0000\u0000\u0000\u0232\u0233\u0001\u0000\u0000\u0000"+
		"\u0233\u0239\u0001\u0000\u0000\u0000\u0234\u0232\u0001\u0000\u0000\u0000"+
		"\u0235\u0236\u0003\"\u0011\u0000\u0236\u0237\u0003\n\u0005\u0000\u0237"+
		"\u0239\u0001\u0000\u0000\u0000\u0238\u0232\u0001\u0000\u0000\u0000\u0238"+
		"\u0235\u0001\u0000\u0000\u0000\u0239\u023a\u0001\u0000\u0000\u0000\u023a"+
		"\u023c\u0005\u0002\u0000\u0000\u023b\u022d\u0001\u0000\u0000\u0000\u023c"+
		"\u023f\u0001\u0000\u0000\u0000\u023d\u023b\u0001\u0000\u0000\u0000\u023d"+
		"\u023e\u0001\u0000\u0000\u0000\u023e\u0240\u0001\u0000\u0000\u0000\u023f"+
		"\u023d\u0001\u0000\u0000\u0000\u0240\u0241\u0003J%\u0000\u0241U\u0001"+
		"\u0000\u0000\u0000\u0242\u0243\u0005\u0001\u0000\u0000\u0243\u0244\u0005"+
		"|\u0000\u0000\u0244\u0245\u0003L&\u0000\u0245\u0246\u0005\u0002\u0000"+
		"\u0000\u0246\u0249\u0001\u0000\u0000\u0000\u0247\u0249\u0003B!\u0000\u0248"+
		"\u0242\u0001\u0000\u0000\u0000\u0248\u0247\u0001\u0000\u0000\u0000\u0249"+
		"W\u0001\u0000\u0000\u0000\u024a\u024b\u0005\u0001\u0000\u0000\u024b\u024d"+
		"\u0005z\u0000\u0000\u024c\u024e\u0003 \u0010\u0000\u024d\u024c\u0001\u0000"+
		"\u0000\u0000\u024d\u024e\u0001\u0000\u0000\u0000\u024e\u024f\u0001\u0000"+
		"\u0000\u0000\u024f\u0250\u0005\u0001\u0000\u0000\u0250\u0251\u0003$\u0012"+
		"\u0000\u0251\u0255\u0005\u0002\u0000\u0000\u0252\u0254\u0003 \u0010\u0000"+
		"\u0253\u0252\u0001\u0000\u0000\u0000\u0254\u0257\u0001\u0000\u0000\u0000"+
		"\u0255\u0253\u0001\u0000\u0000\u0000\u0255\u0256\u0001\u0000\u0000\u0000"+
		"\u0256\u0258\u0001\u0000\u0000\u0000\u0257\u0255\u0001\u0000\u0000\u0000"+
		"\u0258\u0259\u0005\u0002\u0000\u0000\u0259\u0269\u0001\u0000\u0000\u0000"+
		"\u025a\u025b\u0005\u0001\u0000\u0000\u025b\u025d\u0005z\u0000\u0000\u025c"+
		"\u025e\u0003 \u0010\u0000\u025d\u025c\u0001\u0000\u0000\u0000\u025d\u025e"+
		"\u0001\u0000\u0000\u0000\u025e\u025f\u0001\u0000\u0000\u0000\u025f\u0263"+
		"\u0003V+\u0000\u0260\u0262\u0003 \u0010\u0000\u0261\u0260\u0001\u0000"+
		"\u0000\u0000\u0262\u0265\u0001\u0000\u0000\u0000\u0263\u0261\u0001\u0000"+
		"\u0000\u0000\u0263\u0264\u0001\u0000\u0000\u0000\u0264\u0266\u0001\u0000"+
		"\u0000\u0000\u0265\u0263\u0001\u0000\u0000\u0000\u0266\u0267\u0005\u0002"+
		"\u0000\u0000\u0267\u0269\u0001\u0000\u0000\u0000\u0268\u024a\u0001\u0000"+
		"\u0000\u0000\u0268\u025a\u0001\u0000\u0000\u0000\u0269Y\u0001\u0000\u0000"+
		"\u0000\u026a\u026b\u0005\u0001\u0000\u0000\u026b\u026d\u0005x\u0000\u0000"+
		"\u026c\u026e\u0003\"\u0011\u0000\u026d\u026c\u0001\u0000\u0000\u0000\u026d"+
		"\u026e\u0001\u0000\u0000\u0000\u026e\u026f\u0001\u0000\u0000\u0000\u026f"+
		"\u0270\u0003\\.\u0000\u0270\u0271\u0005\u0002\u0000\u0000\u0271[\u0001"+
		"\u0000\u0000\u0000\u0272\u0285\u0003\u0018\f\u0000\u0273\u0274\u0003l"+
		"6\u0000\u0274\u0275\u0003\u0018\f\u0000\u0275\u0285\u0001\u0000\u0000"+
		"\u0000\u0276\u0277\u0003r9\u0000\u0277\u0278\u0003\\.\u0000\u0278\u0285"+
		"\u0001\u0000\u0000\u0000\u0279\u027a\u0003\u0006\u0003\u0000\u027a\u027b"+
		"\u0005\u0001\u0000\u0000\u027b\u027f\u0005z\u0000\u0000\u027c\u027e\u0003"+
		" \u0010\u0000\u027d\u027c\u0001\u0000\u0000\u0000\u027e\u0281\u0001\u0000"+
		"\u0000\u0000\u027f\u027d\u0001\u0000\u0000\u0000\u027f\u0280\u0001\u0000"+
		"\u0000\u0000\u0280\u0282\u0001\u0000\u0000\u0000\u0281\u027f\u0001\u0000"+
		"\u0000\u0000\u0282\u0283\u0005\u0002\u0000\u0000\u0283\u0285\u0001\u0000"+
		"\u0000\u0000\u0284\u0272\u0001\u0000\u0000\u0000\u0284\u0273\u0001\u0000"+
		"\u0000\u0000\u0284\u0276\u0001\u0000\u0000\u0000\u0284\u0279\u0001\u0000"+
		"\u0000\u0000\u0285]\u0001\u0000\u0000\u0000\u0286\u0287\u0005\u0001\u0000"+
		"\u0000\u0287\u0289\u0005{\u0000\u0000\u0288\u028a\u0003 \u0010\u0000\u0289"+
		"\u0288\u0001\u0000\u0000\u0000\u0289\u028a\u0001\u0000\u0000\u0000\u028a"+
		"\u028b\u0001\u0000\u0000\u0000\u028b\u028c\u0005\u0001\u0000\u0000\u028c"+
		"\u028d\u0003$\u0012\u0000\u028d\u0291\u0005\u0002\u0000\u0000\u028e\u0290"+
		"\u0005\u0006\u0000\u0000\u028f\u028e\u0001\u0000\u0000\u0000\u0290\u0293"+
		"\u0001\u0000\u0000\u0000\u0291\u028f\u0001\u0000\u0000\u0000\u0291\u0292"+
		"\u0001\u0000\u0000\u0000\u0292\u0294\u0001\u0000\u0000\u0000\u0293\u0291"+
		"\u0001\u0000\u0000\u0000\u0294\u0295\u0005\u0002\u0000\u0000\u0295\u02a5"+
		"\u0001\u0000\u0000\u0000\u0296\u0297\u0005\u0001\u0000\u0000\u0297\u0299"+
		"\u0005{\u0000\u0000\u0298\u029a\u0003 \u0010\u0000\u0299\u0298\u0001\u0000"+
		"\u0000\u0000\u0299\u029a\u0001\u0000\u0000\u0000\u029a\u029b\u0001\u0000"+
		"\u0000\u0000\u029b\u029f\u0003V+\u0000\u029c\u029e\u0005\u0006\u0000\u0000"+
		"\u029d\u029c\u0001\u0000\u0000\u0000\u029e\u02a1\u0001\u0000\u0000\u0000"+
		"\u029f\u029d\u0001\u0000\u0000\u0000\u029f\u02a0\u0001\u0000\u0000\u0000"+
		"\u02a0\u02a2\u0001\u0000\u0000\u0000\u02a1\u029f\u0001\u0000\u0000\u0000"+
		"\u02a2\u02a3\u0005\u0002\u0000\u0000\u02a3\u02a5\u0001\u0000\u0000\u0000"+
		"\u02a4\u0286\u0001\u0000\u0000\u0000\u02a4\u0296\u0001\u0000\u0000\u0000"+
		"\u02a5_\u0001\u0000\u0000\u0000\u02a6\u02a7\u0005\u0001\u0000\u0000\u02a7"+
		"\u02a9\u0005y\u0000\u0000\u02a8\u02aa\u0003\"\u0011\u0000\u02a9\u02a8"+
		"\u0001\u0000\u0000\u0000\u02a9\u02aa\u0001\u0000\u0000\u0000\u02aa\u02ab"+
		"\u0001\u0000\u0000\u0000\u02ab\u02ac\u0003b1\u0000\u02ac\u02ad\u0005\u0002"+
		"\u0000\u0000\u02ada\u0001\u0000\u0000\u0000\u02ae\u02bf\u0003\u001a\r"+
		"\u0000\u02af\u02b0\u0003l6\u0000\u02b0\u02b1\u0003\u001a\r\u0000\u02b1"+
		"\u02bf\u0001\u0000\u0000\u0000\u02b2\u02b3\u0003r9\u0000\u02b3\u02b4\u0003"+
		"b1\u0000\u02b4\u02bf\u0001\u0000\u0000\u0000\u02b5\u02b6\u0005\u0001\u0000"+
		"\u0000\u02b6\u02ba\u0005{\u0000\u0000\u02b7\u02b9\u0005\u0006\u0000\u0000"+
		"\u02b8\u02b7\u0001\u0000\u0000\u0000\u02b9\u02bc\u0001\u0000\u0000\u0000"+
		"\u02ba\u02b8\u0001\u0000\u0000\u0000\u02ba\u02bb\u0001\u0000\u0000\u0000"+
		"\u02bb\u02bd\u0001\u0000\u0000\u0000\u02bc\u02ba\u0001\u0000\u0000\u0000"+
		"\u02bd\u02bf\u0005\u0002\u0000\u0000\u02be\u02ae\u0001\u0000\u0000\u0000"+
		"\u02be\u02af\u0001\u0000\u0000\u0000\u02be\u02b2\u0001\u0000\u0000\u0000"+
		"\u02be\u02b5\u0001\u0000\u0000\u0000\u02bfc\u0001\u0000\u0000\u0000\u02c0"+
		"\u02c1\u0005\u0001\u0000\u0000\u02c1\u02c3\u0005w\u0000\u0000\u02c2\u02c4"+
		"\u0003\"\u0011\u0000\u02c3\u02c2\u0001\u0000\u0000\u0000\u02c3\u02c4\u0001"+
		"\u0000\u0000\u0000\u02c4\u02c5\u0001\u0000\u0000\u0000\u02c5\u02c6\u0003"+
		"f3\u0000\u02c6\u02c7\u0005\u0002\u0000\u0000\u02c7e\u0001\u0000\u0000"+
		"\u0000\u02c8\u02c9\u0003\u000e\u0007\u0000\u02c9\u02ca\u0003L&\u0000\u02ca"+
		"\u02d2\u0001\u0000\u0000\u0000\u02cb\u02cc\u0003l6\u0000\u02cc\u02cd\u0003"+
		"\u000e\u0007\u0000\u02cd\u02d2\u0001\u0000\u0000\u0000\u02ce\u02cf\u0003"+
		"r9\u0000\u02cf\u02d0\u0003f3\u0000\u02d0\u02d2\u0001\u0000\u0000\u0000"+
		"\u02d1\u02c8\u0001\u0000\u0000\u0000\u02d1\u02cb\u0001\u0000\u0000\u0000"+
		"\u02d1\u02ce\u0001\u0000\u0000\u0000\u02d2g\u0001\u0000\u0000\u0000\u02d3"+
		"\u02d4\u0005\u0001\u0000\u0000\u02d4\u02d6\u0005q\u0000\u0000\u02d5\u02d7"+
		"\u0003\"\u0011\u0000\u02d6\u02d5\u0001\u0000\u0000\u0000\u02d6\u02d7\u0001"+
		"\u0000\u0000\u0000\u02d7\u02d8\u0001\u0000\u0000\u0000\u02d8\u02d9\u0003"+
		"\u001c\u000e\u0000\u02d9\u02da\u0005\u0002\u0000\u0000\u02da\u02fc\u0001"+
		"\u0000\u0000\u0000\u02db\u02dc\u0005\u0001\u0000\u0000\u02dc\u02de\u0005"+
		"q\u0000\u0000\u02dd\u02df\u0003\"\u0011\u0000\u02de\u02dd\u0001\u0000"+
		"\u0000\u0000\u02de\u02df\u0001\u0000\u0000\u0000\u02df\u02e0\u0001\u0000"+
		"\u0000\u0000\u02e0\u02e1\u0003\u0016\u000b\u0000\u02e1\u02e2\u0005\u0002"+
		"\u0000\u0000\u02e2\u02fc\u0001\u0000\u0000\u0000\u02e3\u02e4\u0005\u0001"+
		"\u0000\u0000\u02e4\u02e6\u0005x\u0000\u0000\u02e5\u02e7\u0003\"\u0011"+
		"\u0000\u02e6\u02e5\u0001\u0000\u0000\u0000\u02e6\u02e7\u0001\u0000\u0000"+
		"\u0000\u02e7\u02e8\u0001\u0000\u0000\u0000\u02e8\u02e9\u0003\u0018\f\u0000"+
		"\u02e9\u02ea\u0005\u0002\u0000\u0000\u02ea\u02fc\u0001\u0000\u0000\u0000"+
		"\u02eb\u02ec\u0005\u0001\u0000\u0000\u02ec\u02ee\u0005y\u0000\u0000\u02ed"+
		"\u02ef\u0003\"\u0011\u0000\u02ee\u02ed\u0001\u0000\u0000\u0000\u02ee\u02ef"+
		"\u0001\u0000\u0000\u0000\u02ef\u02f0\u0001\u0000\u0000\u0000\u02f0\u02f1"+
		"\u0003\u001a\r\u0000\u02f1\u02f2\u0005\u0002\u0000\u0000\u02f2\u02fc\u0001"+
		"\u0000\u0000\u0000\u02f3\u02f4\u0005\u0001\u0000\u0000\u02f4\u02f6\u0005"+
		"w\u0000\u0000\u02f5\u02f7\u0003\"\u0011\u0000\u02f6\u02f5\u0001\u0000"+
		"\u0000\u0000\u02f6\u02f7\u0001\u0000\u0000\u0000\u02f7\u02f8\u0001\u0000"+
		"\u0000\u0000\u02f8\u02f9\u0003\u000e\u0007\u0000\u02f9\u02fa\u0005\u0002"+
		"\u0000\u0000\u02fa\u02fc\u0001\u0000\u0000\u0000\u02fb\u02d3\u0001\u0000"+
		"\u0000\u0000\u02fb\u02db\u0001\u0000\u0000\u0000\u02fb\u02e3\u0001\u0000"+
		"\u0000\u0000\u02fb\u02eb\u0001\u0000\u0000\u0000\u02fb\u02f3\u0001\u0000"+
		"\u0000\u0000\u02fci\u0001\u0000\u0000\u0000\u02fd\u02fe\u0005\u0001\u0000"+
		"\u0000\u02fe\u02ff\u0005}\u0000\u0000\u02ff\u0300\u0003\u0002\u0001\u0000"+
		"\u0300\u0301\u0003\u0002\u0001\u0000\u0301\u0302\u0003h4\u0000\u0302\u0303"+
		"\u0005\u0002\u0000\u0000\u0303k\u0001\u0000\u0000\u0000\u0304\u0305\u0005"+
		"\u0001\u0000\u0000\u0305\u0306\u0005}\u0000\u0000\u0306\u0307\u0003\u0002"+
		"\u0001\u0000\u0307\u0308\u0003\u0002\u0001\u0000\u0308\u0309\u0005\u0002"+
		"\u0000\u0000\u0309m\u0001\u0000\u0000\u0000\u030a\u030b\u0005\u0001\u0000"+
		"\u0000\u030b\u030c\u0005q\u0000\u0000\u030c\u030d\u0003 \u0010\u0000\u030d"+
		"\u030e\u0005\u0002\u0000\u0000\u030e\u031f\u0001\u0000\u0000\u0000\u030f"+
		"\u0310\u0005\u0001\u0000\u0000\u0310\u0311\u0005x\u0000\u0000\u0311\u0312"+
		"\u0003 \u0010\u0000\u0312\u0313\u0005\u0002\u0000\u0000\u0313\u031f\u0001"+
		"\u0000\u0000\u0000\u0314\u0315\u0005\u0001\u0000\u0000\u0315\u0316\u0005"+
		"y\u0000\u0000\u0316\u0317\u0003 \u0010\u0000\u0317\u0318\u0005\u0002\u0000"+
		"\u0000\u0318\u031f\u0001\u0000\u0000\u0000\u0319\u031a\u0005\u0001\u0000"+
		"\u0000\u031a\u031b\u0005w\u0000\u0000\u031b\u031c\u0003 \u0010\u0000\u031c"+
		"\u031d\u0005\u0002\u0000\u0000\u031d\u031f\u0001\u0000\u0000\u0000\u031e"+
		"\u030a\u0001\u0000\u0000\u0000\u031e\u030f\u0001\u0000\u0000\u0000\u031e"+
		"\u0314\u0001\u0000\u0000\u0000\u031e\u0319\u0001\u0000\u0000\u0000\u031f"+
		"o\u0001\u0000\u0000\u0000\u0320\u0321\u0005\u0001\u0000\u0000\u0321\u0322"+
		"\u0005~\u0000\u0000\u0322\u0323\u0003\u0002\u0001\u0000\u0323\u0324\u0003"+
		"n7\u0000\u0324\u0325\u0005\u0002\u0000\u0000\u0325q\u0001\u0000\u0000"+
		"\u0000\u0326\u0327\u0005\u0001\u0000\u0000\u0327\u0328\u0005~\u0000\u0000"+
		"\u0328\u0329\u0003\u0002\u0001\u0000\u0329\u032a\u0005\u0002\u0000\u0000"+
		"\u032as\u0001\u0000\u0000\u0000\u032b\u032c\u0005\u0001\u0000\u0000\u032c"+
		"\u032e\u0005p\u0000\u0000\u032d\u032f\u0003\"\u0011\u0000\u032e\u032d"+
		"\u0001\u0000\u0000\u0000\u032e\u032f\u0001\u0000\u0000\u0000\u032f\u0330"+
		"\u0001\u0000\u0000\u0000\u0330\u0331\u0003\u0010\b\u0000\u0331\u0332\u0005"+
		"\u0002\u0000\u0000\u0332u\u0001\u0000\u0000\u0000\u0333\u0334\u0005\u0001"+
		"\u0000\u0000\u0334\u0335\u0005s\u0000\u0000\u0335\u0336\u0003 \u0010\u0000"+
		"\u0336\u0337\u0005\u0002\u0000\u0000\u0337w\u0001\u0000\u0000\u0000\u0338"+
		"\u0343\u0003t:\u0000\u0339\u0343\u0003d2\u0000\u033a\u0343\u0003Z-\u0000"+
		"\u033b\u0343\u0003`0\u0000\u033c\u0343\u0003N\'\u0000\u033d\u0343\u0003"+
		"X,\u0000\u033e\u0343\u0003^/\u0000\u033f\u0343\u0003v;\u0000\u0340\u0343"+
		"\u0003j5\u0000\u0341\u0343\u0003p8\u0000\u0342\u0338\u0001\u0000\u0000"+
		"\u0000\u0342\u0339\u0001\u0000\u0000\u0000\u0342\u033a\u0001\u0000\u0000"+
		"\u0000\u0342\u033b\u0001\u0000\u0000\u0000\u0342\u033c\u0001\u0000\u0000"+
		"\u0000\u0342\u033d\u0001\u0000\u0000\u0000\u0342\u033e\u0001\u0000\u0000"+
		"\u0000\u0342\u033f\u0001\u0000\u0000\u0000\u0342\u0340\u0001\u0000\u0000"+
		"\u0000\u0342\u0341\u0001\u0000\u0000\u0000\u0343y\u0001\u0000\u0000\u0000"+
		"\u0344\u0345\u0005\u0001\u0000\u0000\u0345\u0347\u0005\u007f\u0000\u0000"+
		"\u0346\u0348\u0005\u0090\u0000\u0000\u0347\u0346\u0001\u0000\u0000\u0000"+
		"\u0347\u0348\u0001\u0000\u0000\u0000\u0348\u034c\u0001\u0000\u0000\u0000"+
		"\u0349\u034b\u0003x<\u0000\u034a\u0349\u0001\u0000\u0000\u0000\u034b\u034e"+
		"\u0001\u0000\u0000\u0000\u034c\u034a\u0001\u0000\u0000\u0000\u034c\u034d"+
		"\u0001\u0000\u0000\u0000\u034d\u034f\u0001\u0000\u0000\u0000\u034e\u034c"+
		"\u0001\u0000\u0000\u0000\u034f\u0350\u0005\u0002\u0000\u0000\u0350{\u0001"+
		"\u0000\u0000\u0000\u0351\u0360\u0003z=\u0000\u0352\u0353\u0005\u0001\u0000"+
		"\u0000\u0353\u0355\u0005\u007f\u0000\u0000\u0354\u0356\u0005\u0090\u0000"+
		"\u0000\u0355\u0354\u0001\u0000\u0000\u0000\u0355\u0356\u0001\u0000\u0000"+
		"\u0000\u0356\u0357\u0001\u0000\u0000\u0000\u0357\u035b\u0007\u0005\u0000"+
		"\u0000\u0358\u035a\u0005\u0006\u0000\u0000\u0359\u0358\u0001\u0000\u0000"+
		"\u0000\u035a\u035d\u0001\u0000\u0000\u0000\u035b\u0359\u0001\u0000\u0000"+
		"\u0000\u035b\u035c\u0001\u0000\u0000\u0000\u035c\u035e\u0001\u0000\u0000"+
		"\u0000\u035d\u035b\u0001\u0000\u0000\u0000\u035e\u0360\u0005\u0002\u0000"+
		"\u0000\u035f\u0351\u0001\u0000\u0000\u0000\u035f\u0352\u0001\u0000\u0000"+
		"\u0000\u0360}\u0001\u0000\u0000\u0000\u0361\u0362\u0005\u0001\u0000\u0000"+
		"\u0362\u0364\u0005\u0084\u0000\u0000\u0363\u0365\u0005\u0090\u0000\u0000"+
		"\u0364\u0363\u0001\u0000\u0000\u0000\u0364\u0365\u0001\u0000\u0000\u0000"+
		"\u0365\u0366\u0001\u0000\u0000\u0000\u0366\u0367\u0003\u0002\u0001\u0000"+
		"\u0367\u0368\u0003\u0088D\u0000\u0368\u0369\u0005\u0002\u0000\u0000\u0369"+
		"\u0373\u0001\u0000\u0000\u0000\u036a\u036b\u0005\u0001\u0000\u0000\u036b"+
		"\u036d\u0005\u0085\u0000\u0000\u036c\u036e\u0005\u0090\u0000\u0000\u036d"+
		"\u036c\u0001\u0000\u0000\u0000\u036d\u036e\u0001\u0000\u0000\u0000\u036e"+
		"\u036f\u0001\u0000\u0000\u0000\u036f\u0370\u0003\u0002\u0001\u0000\u0370"+
		"\u0371\u0005\u0002\u0000\u0000\u0371\u0373\u0001\u0000\u0000\u0000\u0372"+
		"\u0361\u0001\u0000\u0000\u0000\u0372\u036a\u0001\u0000\u0000\u0000\u0373"+
		"\u007f\u0001\u0000\u0000\u0000\u0374\u0375\u0005\u0001\u0000\u0000\u0375"+
		"\u0376\u0005\u0086\u0000\u0000\u0376\u0377\u0003|>\u0000\u0377\u0378\u0005"+
		"\u0006\u0000\u0000\u0378\u0379\u0005\u0002\u0000\u0000\u0379\u03a9\u0001"+
		"\u0000\u0000\u0000\u037a\u037b\u0005\u0001\u0000\u0000\u037b\u037c\u0005"+
		"\u0087\u0000\u0000\u037c\u037d\u0003|>\u0000\u037d\u037e\u0005\u0006\u0000"+
		"\u0000\u037e\u037f\u0005\u0002\u0000\u0000\u037f\u03a9\u0001\u0000\u0000"+
		"\u0000\u0380\u0381\u0005\u0001\u0000\u0000\u0381\u0382\u0005\u0088\u0000"+
		"\u0000\u0382\u0383\u0003|>\u0000\u0383\u0384\u0005\u0006\u0000\u0000\u0384"+
		"\u0385\u0005\u0002\u0000\u0000\u0385\u03a9\u0001\u0000\u0000\u0000\u0386"+
		"\u0387\u0005\u0001\u0000\u0000\u0387\u0388\u0005\u008c\u0000\u0000\u0388"+
		"\u0389\u0003|>\u0000\u0389\u038a\u0005\u0006\u0000\u0000\u038a\u038b\u0005"+
		"\u0002\u0000\u0000\u038b\u03a9\u0001\u0000\u0000\u0000\u038c\u038d\u0005"+
		"\u0001\u0000\u0000\u038d\u038e\u0005\u0089\u0000\u0000\u038e\u038f\u0003"+
		"~?\u0000\u038f\u0390\u0003\u0088D\u0000\u0390\u0391\u0005\u0002\u0000"+
		"\u0000\u0391\u03a9\u0001\u0000\u0000\u0000\u0392\u0393\u0005\u0001\u0000"+
		"\u0000\u0393\u0394\u0005\u008a\u0000\u0000\u0394\u0395\u0003~?\u0000\u0395"+
		"\u0396\u0005\u0002\u0000\u0000\u0396\u03a9\u0001\u0000\u0000\u0000\u0397"+
		"\u0398\u0005\u0001\u0000\u0000\u0398\u0399\u0005\u008b\u0000\u0000\u0399"+
		"\u039a\u0003~?\u0000\u039a\u039b\u0005\u0002\u0000\u0000\u039b\u03a9\u0001"+
		"\u0000\u0000\u0000\u039c\u039d\u0005\u0001\u0000\u0000\u039d\u039e\u0005"+
		"\u008c\u0000\u0000\u039e\u039f\u0003~?\u0000\u039f\u03a0\u0005\u0006\u0000"+
		"\u0000\u03a0\u03a1\u0005\u0002\u0000\u0000\u03a1\u03a9\u0001\u0000\u0000"+
		"\u0000\u03a2\u03a3\u0005\u0001\u0000\u0000\u03a3\u03a4\u0005\u008d\u0000"+
		"\u0000\u03a4\u03a5\u0003~?\u0000\u03a5\u03a6\u0005\u0006\u0000\u0000\u03a6"+
		"\u03a7\u0005\u0002\u0000\u0000\u03a7\u03a9\u0001\u0000\u0000\u0000\u03a8"+
		"\u0374\u0001\u0000\u0000\u0000\u03a8\u037a\u0001\u0000\u0000\u0000\u03a8"+
		"\u0380\u0001\u0000\u0000\u0000\u03a8\u0386\u0001\u0000\u0000\u0000\u03a8"+
		"\u038c\u0001\u0000\u0000\u0000\u03a8\u0392\u0001\u0000\u0000\u0000\u03a8"+
		"\u0397\u0001\u0000\u0000\u0000\u03a8\u039c\u0001\u0000\u0000\u0000\u03a8"+
		"\u03a2\u0001\u0000\u0000\u0000\u03a9\u0081\u0001\u0000\u0000\u0000\u03aa"+
		"\u03b7\u0003~?\u0000\u03ab\u03b7\u0003\u0080@\u0000\u03ac\u03b7\u0003"+
		"|>\u0000\u03ad\u03ae\u0005\u0001\u0000\u0000\u03ae\u03af\u0005\u0083\u0000"+
		"\u0000\u03af\u03b1\u0003\u0002\u0001\u0000\u03b0\u03b2\u0005\u0090\u0000"+
		"\u0000\u03b1\u03b0\u0001\u0000\u0000\u0000\u03b1\u03b2\u0001\u0000\u0000"+
		"\u0000\u03b2\u03b3\u0001\u0000\u0000\u0000\u03b3\u03b4\u0005\u0002\u0000"+
		"\u0000\u03b4\u03b7\u0001\u0000\u0000\u0000\u03b5\u03b7\u0003\u0084B\u0000"+
		"\u03b6\u03aa\u0001\u0000\u0000\u0000\u03b6\u03ab\u0001\u0000\u0000\u0000"+
		"\u03b6\u03ac\u0001\u0000\u0000\u0000\u03b6\u03ad\u0001\u0000\u0000\u0000"+
		"\u03b6\u03b5\u0001\u0000\u0000\u0000\u03b7\u0083\u0001\u0000\u0000\u0000"+
		"\u03b8\u03b9\u0005\u0001\u0000\u0000\u03b9\u03bb\u0005\u0082\u0000\u0000"+
		"\u03ba\u03bc\u0005\u0090\u0000\u0000\u03bb\u03ba\u0001\u0000\u0000\u0000"+
		"\u03bb\u03bc\u0001\u0000\u0000\u0000\u03bc\u03c0\u0001\u0000\u0000\u0000"+
		"\u03bd\u03bf\u0003\u0082A\u0000\u03be\u03bd\u0001\u0000\u0000\u0000\u03bf"+
		"\u03c2\u0001\u0000\u0000\u0000\u03c0\u03be\u0001\u0000\u0000\u0000\u03c0"+
		"\u03c1\u0001\u0000\u0000\u0000\u03c1\u03c3\u0001\u0000\u0000\u0000\u03c2"+
		"\u03c0\u0001\u0000\u0000\u0000\u03c3\u03d9\u0005\u0002\u0000\u0000\u03c4"+
		"\u03c5\u0005\u0001\u0000\u0000\u03c5\u03c7\u0005\u008e\u0000\u0000\u03c6"+
		"\u03c8\u0005\u0090\u0000\u0000\u03c7\u03c6\u0001\u0000\u0000\u0000\u03c7"+
		"\u03c8\u0001\u0000\u0000\u0000\u03c8\u03c9\u0001\u0000\u0000\u0000\u03c9"+
		"\u03ca\u0005\u0006\u0000\u0000\u03ca\u03d9\u0005\u0002\u0000\u0000\u03cb"+
		"\u03cc\u0005\u0001\u0000\u0000\u03cc\u03ce\u0005\u008f\u0000\u0000\u03cd"+
		"\u03cf\u0005\u0090\u0000\u0000\u03ce\u03cd\u0001\u0000\u0000\u0000\u03ce"+
		"\u03cf\u0001\u0000\u0000\u0000\u03cf\u03d0\u0001\u0000\u0000\u0000\u03d0"+
		"\u03d1\u0005\u0006\u0000\u0000\u03d1\u03d9\u0005\u0002\u0000\u0000\u03d2"+
		"\u03d3\u0005\u0001\u0000\u0000\u03d3\u03d5\u0005\u008f\u0000\u0000\u03d4"+
		"\u03d6\u0005\u0090\u0000\u0000\u03d5\u03d4\u0001\u0000\u0000\u0000\u03d5"+
		"\u03d6\u0001\u0000\u0000\u0000\u03d6\u03d7\u0001\u0000\u0000\u0000\u03d7"+
		"\u03d9\u0005\u0002\u0000\u0000\u03d8\u03b8\u0001\u0000\u0000\u0000\u03d8"+
		"\u03c4\u0001\u0000\u0000\u0000\u03d8\u03cb\u0001\u0000\u0000\u0000\u03d8"+
		"\u03d2\u0001\u0000\u0000\u0000\u03d9\u0085\u0001\u0000\u0000\u0000\u03da"+
		"\u03db\u0005\u0001\u0000\u0000\u03db\u03dc\u0005\b\u0000\u0000\u03dc\u03dd"+
		"\u0003\u001e\u000f\u0000\u03dd\u03de\u0005\u0002\u0000\u0000\u03de\u0087"+
		"\u0001\u0000\u0000\u0000\u03df\u03e1\u0003\u0086C\u0000\u03e0\u03df\u0001"+
		"\u0000\u0000\u0000\u03e1\u03e4\u0001\u0000\u0000\u0000\u03e2\u03e0\u0001"+
		"\u0000\u0000\u0000\u03e2\u03e3\u0001\u0000\u0000\u0000\u03e3\u0089\u0001"+
		"\u0000\u0000\u0000\u03e4\u03e2\u0001\u0000\u0000\u0000\u03e5\u03e7\u0003"+
		"\u0082A\u0000\u03e6\u03e5\u0001\u0000\u0000\u0000\u03e7\u03ea\u0001\u0000"+
		"\u0000\u0000\u03e8\u03e6\u0001\u0000\u0000\u0000\u03e8\u03e9\u0001\u0000"+
		"\u0000\u0000\u03e9\u03eb\u0001\u0000\u0000\u0000\u03ea\u03e8\u0001\u0000"+
		"\u0000\u0000\u03eb\u03f4\u0005\u0000\u0000\u0001\u03ec\u03ee\u0003x<\u0000"+
		"\u03ed\u03ec\u0001\u0000\u0000\u0000\u03ee\u03ef\u0001\u0000\u0000\u0000"+
		"\u03ef\u03ed\u0001\u0000\u0000\u0000\u03ef\u03f0\u0001\u0000\u0000\u0000"+
		"\u03f0\u03f1\u0001\u0000\u0000\u0000\u03f1\u03f2\u0005\u0000\u0000\u0001"+
		"\u03f2\u03f4\u0001\u0000\u0000\u0000\u03f3\u03e8\u0001\u0000\u0000\u0000"+
		"\u03f3\u03ed\u0001\u0000\u0000\u0000\u03f4\u008b\u0001\u0000\u0000\u0000"+
		"\u03f5\u03f6\u0003z=\u0000\u03f6\u03f7\u0005\u0000\u0000\u0001\u03f7\u0400"+
		"\u0001\u0000\u0000\u0000\u03f8\u03fa\u0003x<\u0000\u03f9\u03f8\u0001\u0000"+
		"\u0000\u0000\u03fa\u03fd\u0001\u0000\u0000\u0000\u03fb\u03f9\u0001\u0000"+
		"\u0000\u0000\u03fb\u03fc\u0001\u0000\u0000\u0000\u03fc\u03fe\u0001\u0000"+
		"\u0000\u0000\u03fd\u03fb\u0001\u0000\u0000\u0000\u03fe\u0400\u0005\u0000"+
		"\u0000\u0001\u03ff\u03f5\u0001\u0000\u0000\u0000\u03ff\u03fb\u0001\u0000"+
		"\u0000\u0000\u0400\u008d\u0001\u0000\u0000\u0000p\u009b\u00a5\u00b1\u00b7"+
		"\u00bc\u00c4\u00ca\u00d2\u00d8\u00e9\u00f7\u0108\u010b\u010f\u0112\u0125"+
		"\u0132\u0137\u013b\u0144\u014a\u0152\u0158\u015d\u0166\u016c\u0176\u017c"+
		"\u0183\u0188\u018c\u0191\u0195\u019a\u019d\u01a1\u01a3\u01ab\u01b7\u01be"+
		"\u01c3\u01c8\u01cb\u01d0\u01db\u01dd\u01e0\u01e9\u01ef\u01f9\u01ff\u0205"+
		"\u020b\u020f\u0216\u021c\u0221\u0228\u0232\u0238\u023d\u0248\u024d\u0255"+
		"\u025d\u0263\u0268\u026d\u027f\u0284\u0289\u0291\u0299\u029f\u02a4\u02a9"+
		"\u02ba\u02be\u02c3\u02d1\u02d6\u02de\u02e6\u02ee\u02f6\u02fb\u031e\u032e"+
		"\u0342\u0347\u034c\u0355\u035b\u035f\u0364\u036d\u0372\u03a8\u03b1\u03b6"+
		"\u03bb\u03c0\u03c7\u03ce\u03d5\u03d8\u03e2\u03e8\u03ef\u03f3\u03fb\u03ff";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}