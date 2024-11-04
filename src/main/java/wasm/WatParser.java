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
		SYMBOLIC=9, FUNCREF=10, EXTERNREF=11, MUT=12, REF=13, CONT=14, NOP=15, 
		SYM_ASSERT=16, ALLOC=17, FREE=18, UNREACHABLE=19, DROP=20, BLOCK=21, LOOP=22, 
		END=23, BR=24, BR_IF=25, BR_TABLE=26, RETURN=27, IF=28, THEN=29, ELSE=30, 
		SELECT=31, CALL=32, CALL_INDIRECT=33, RETURN_CALL=34, RETURN_CALL_INDIRECT=35, 
		REFFUNC=36, RESUME=37, ON=38, CONTNEW=39, SUSPEND=40, LOCAL_GET=41, LOCAL_SET=42, 
		LOCAL_TEE=43, GLOBAL_GET=44, GLOBAL_SET=45, LOAD=46, STORE=47, UNDERSCORE=48, 
		OFFSET_EQ=49, ALIGN_EQ=50, SIGN_POSTFIX=51, MEM_SIZE=52, I32=53, I64=54, 
		F32=55, F64=56, IXX=57, FXX=58, OP_EQZ=59, OP_EQ=60, OP_NE=61, OP_LT=62, 
		OP_LTS=63, OP_LTU=64, OP_LE=65, OP_LES=66, OP_LEU=67, OP_GT=68, OP_GTS=69, 
		OP_GTU=70, OP_GE=71, OP_GES=72, OP_GEU=73, OP_CLZ=74, OP_CTZ=75, OP_POPCNT=76, 
		OP_NEG=77, OP_ABS=78, OP_SQRT=79, OP_CEIL=80, OP_FLOOR=81, OP_TRUNC=82, 
		OP_NEAREST=83, OP_ADD=84, OP_SUB=85, OP_MUL=86, OP_DIV=87, OP_DIV_S=88, 
		OP_DIV_U=89, OP_REM_S=90, OP_REM_U=91, OP_AND=92, OP_OR=93, OP_XOR=94, 
		OP_SHL=95, OP_SHR_S=96, OP_SHR_U=97, OP_ROTL=98, OP_ROTR=99, OP_MIN=100, 
		OP_MAX=101, OP_COPYSIGN=102, OP_WRAP=103, OP_TRUNC_=104, OP_TRUNC_SAT=105, 
		OP_CONVERT=106, OP_EXTEND=107, OP_DEMOTE=108, OP_PROMOTE=109, OP_REINTER=110, 
		MEMORY_SIZE=111, MEMORY_GROW=112, MEMORY_FILL=113, MEMORY_COPY=114, MEMORY_INIT=115, 
		TEST=116, COMPARE=117, UNARY=118, BINARY=119, CONVERT=120, TYPE=121, FUNC=122, 
		EXTERN=123, START_=124, PARAM=125, RESULT=126, LOCAL=127, GLOBAL=128, 
		TABLE=129, MEMORY=130, ELEM=131, DATA=132, OFFSET=133, IMPORT=134, EXPORT=135, 
		TAG=136, DECLARE=137, MODULE=138, BIN=139, QUOTE=140, SCRIPT=141, REGISTER=142, 
		INVOKE=143, GET=144, ASSERT_MALFORMED=145, ASSERT_INVALID=146, ASSERT_UNLINKABLE=147, 
		ASSERT_RETURN=148, ASSERT_RETURN_CANONICAL_NAN=149, ASSERT_RETURN_ARITHMETIC_NAN=150, 
		ASSERT_TRAP=151, ASSERT_EXHAUSTION=152, INPUT=153, OUTPUT=154, VAR=155, 
		V128=156, SPACE=157, COMMENT=158;
	public static final int
		RULE_value = 0, RULE_name = 1, RULE_numType = 2, RULE_refType = 3, RULE_vecType = 4, 
		RULE_valType = 5, RULE_heapType = 6, RULE_globalType = 7, RULE_defType = 8, 
		RULE_funcParamType = 9, RULE_funcResType = 10, RULE_funcType = 11, RULE_tableType = 12, 
		RULE_memoryType = 13, RULE_typeUse = 14, RULE_literal = 15, RULE_idx = 16, 
		RULE_bindVar = 17, RULE_instr = 18, RULE_plainInstr = 19, RULE_resumeInstr = 20, 
		RULE_handlerInstr = 21, RULE_offsetEq = 22, RULE_alignEq = 23, RULE_load = 24, 
		RULE_store = 25, RULE_selectInstr = 26, RULE_callIndirectInstr = 27, RULE_callInstrParams = 28, 
		RULE_callInstrParamsInstr = 29, RULE_callInstrResultsInstr = 30, RULE_blockInstr = 31, 
		RULE_blockType = 32, RULE_block = 33, RULE_foldedInstr = 34, RULE_expr = 35, 
		RULE_callExprType = 36, RULE_callExprParams = 37, RULE_callExprResults = 38, 
		RULE_instrList = 39, RULE_constExpr = 40, RULE_function = 41, RULE_funcFields = 42, 
		RULE_funcFieldsBody = 43, RULE_funcBody = 44, RULE_offset = 45, RULE_elem = 46, 
		RULE_table = 47, RULE_tableField = 48, RULE_data = 49, RULE_memory = 50, 
		RULE_memoryField = 51, RULE_global = 52, RULE_globalField = 53, RULE_importDesc = 54, 
		RULE_simport = 55, RULE_inlineImport = 56, RULE_exportDesc = 57, RULE_export_ = 58, 
		RULE_inlineExport = 59, RULE_tag = 60, RULE_typeDef = 61, RULE_start_ = 62, 
		RULE_moduleField = 63, RULE_module_ = 64, RULE_scriptModule = 65, RULE_action_ = 66, 
		RULE_assertion = 67, RULE_cmd = 68, RULE_meta = 69, RULE_wconst = 70, 
		RULE_constList = 71, RULE_script = 72, RULE_module = 73;
	private static String[] makeRuleNames() {
		return new String[] {
			"value", "name", "numType", "refType", "vecType", "valType", "heapType", 
			"globalType", "defType", "funcParamType", "funcResType", "funcType", 
			"tableType", "memoryType", "typeUse", "literal", "idx", "bindVar", "instr", 
			"plainInstr", "resumeInstr", "handlerInstr", "offsetEq", "alignEq", "load", 
			"store", "selectInstr", "callIndirectInstr", "callInstrParams", "callInstrParamsInstr", 
			"callInstrResultsInstr", "blockInstr", "blockType", "block", "foldedInstr", 
			"expr", "callExprType", "callExprParams", "callExprResults", "instrList", 
			"constExpr", "function", "funcFields", "funcFieldsBody", "funcBody", 
			"offset", "elem", "table", "tableField", "data", "memory", "memoryField", 
			"global", "globalField", "importDesc", "simport", "inlineImport", "exportDesc", 
			"export_", "inlineExport", "tag", "typeDef", "start_", "moduleField", 
			"module_", "scriptModule", "action_", "assertion", "cmd", "meta", "wconst", 
			"constList", "script", "module"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", null, null, null, null, null, null, null, "'funcref'", 
			"'externref'", "'mut'", "'ref'", "'cont'", "'nop'", "'sym_assert'", "'alloc'", 
			"'free'", "'unreachable'", "'drop'", "'block'", "'loop'", "'end'", "'br'", 
			"'br_if'", "'br_table'", "'return'", "'if'", "'then'", "'else'", "'.select'", 
			"'call'", "'call_indirect'", "'return_call'", "'return_call_indirect'", 
			"'ref.func'", "'resume'", "'on'", "'cont.new'", "'suspend'", "'local.get'", 
			"'local.set'", "'local.tee'", "'global.get'", "'global.set'", null, null, 
			"'_'", "'offset='", "'align='", null, null, "'i32'", "'i64'", "'f32'", 
			"'f64'", null, null, "'.eqz'", "'.eq'", "'.ne'", "'.lt'", "'.lt_s'", 
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
			"'tag'", "'declare'", "'module'", "'binary'", "'quote'", "'script'", 
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
			"CONST", "SYMBOLIC", "FUNCREF", "EXTERNREF", "MUT", "REF", "CONT", "NOP", 
			"SYM_ASSERT", "ALLOC", "FREE", "UNREACHABLE", "DROP", "BLOCK", "LOOP", 
			"END", "BR", "BR_IF", "BR_TABLE", "RETURN", "IF", "THEN", "ELSE", "SELECT", 
			"CALL", "CALL_INDIRECT", "RETURN_CALL", "RETURN_CALL_INDIRECT", "REFFUNC", 
			"RESUME", "ON", "CONTNEW", "SUSPEND", "LOCAL_GET", "LOCAL_SET", "LOCAL_TEE", 
			"GLOBAL_GET", "GLOBAL_SET", "LOAD", "STORE", "UNDERSCORE", "OFFSET_EQ", 
			"ALIGN_EQ", "SIGN_POSTFIX", "MEM_SIZE", "I32", "I64", "F32", "F64", "IXX", 
			"FXX", "OP_EQZ", "OP_EQ", "OP_NE", "OP_LT", "OP_LTS", "OP_LTU", "OP_LE", 
			"OP_LES", "OP_LEU", "OP_GT", "OP_GTS", "OP_GTU", "OP_GE", "OP_GES", "OP_GEU", 
			"OP_CLZ", "OP_CTZ", "OP_POPCNT", "OP_NEG", "OP_ABS", "OP_SQRT", "OP_CEIL", 
			"OP_FLOOR", "OP_TRUNC", "OP_NEAREST", "OP_ADD", "OP_SUB", "OP_MUL", "OP_DIV", 
			"OP_DIV_S", "OP_DIV_U", "OP_REM_S", "OP_REM_U", "OP_AND", "OP_OR", "OP_XOR", 
			"OP_SHL", "OP_SHR_S", "OP_SHR_U", "OP_ROTL", "OP_ROTR", "OP_MIN", "OP_MAX", 
			"OP_COPYSIGN", "OP_WRAP", "OP_TRUNC_", "OP_TRUNC_SAT", "OP_CONVERT", 
			"OP_EXTEND", "OP_DEMOTE", "OP_PROMOTE", "OP_REINTER", "MEMORY_SIZE", 
			"MEMORY_GROW", "MEMORY_FILL", "MEMORY_COPY", "MEMORY_INIT", "TEST", "COMPARE", 
			"UNARY", "BINARY", "CONVERT", "TYPE", "FUNC", "EXTERN", "START_", "PARAM", 
			"RESULT", "LOCAL", "GLOBAL", "TABLE", "MEMORY", "ELEM", "DATA", "OFFSET", 
			"IMPORT", "EXPORT", "TAG", "DECLARE", "MODULE", "BIN", "QUOTE", "SCRIPT", 
			"REGISTER", "INVOKE", "GET", "ASSERT_MALFORMED", "ASSERT_INVALID", "ASSERT_UNLINKABLE", 
			"ASSERT_RETURN", "ASSERT_RETURN_CANONICAL_NAN", "ASSERT_RETURN_ARITHMETIC_NAN", 
			"ASSERT_TRAP", "ASSERT_EXHAUSTION", "INPUT", "OUTPUT", "VAR", "V128", 
			"SPACE", "COMMENT"
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
			setState(148);
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
			setState(150);
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
			setState(152);
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
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode REF() { return getToken(WatParser.REF, 0); }
		public IdxContext idx() {
			return getRuleContext(IdxContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
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
		try {
			setState(161);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FUNCREF:
				enterOuterAlt(_localctx, 1);
				{
				setState(154);
				match(FUNCREF);
				}
				break;
			case EXTERNREF:
				enterOuterAlt(_localctx, 2);
				{
				setState(155);
				match(EXTERNREF);
				}
				break;
			case LPAR:
				enterOuterAlt(_localctx, 3);
				{
				setState(156);
				match(LPAR);
				setState(157);
				match(REF);
				setState(158);
				idx();
				setState(159);
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
			setState(163);
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
			setState(168);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VALUE_TYPE:
				enterOuterAlt(_localctx, 1);
				{
				setState(165);
				numType();
				}
				break;
			case V128:
				enterOuterAlt(_localctx, 2);
				{
				setState(166);
				vecType();
				}
				break;
			case LPAR:
			case FUNCREF:
			case EXTERNREF:
				enterOuterAlt(_localctx, 3);
				{
				setState(167);
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
		public FuncTypeContext funcType() {
			return getRuleContext(FuncTypeContext.class,0);
		}
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
		try {
			setState(173);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FUNC:
				enterOuterAlt(_localctx, 1);
				{
				setState(170);
				match(FUNC);
				}
				break;
			case EXTERN:
				enterOuterAlt(_localctx, 2);
				{
				setState(171);
				match(EXTERN);
				}
				break;
			case EOF:
			case LPAR:
				enterOuterAlt(_localctx, 3);
				{
				setState(172);
				funcType();
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
			setState(181);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(175);
				valType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(176);
				match(LPAR);
				setState(177);
				match(MUT);
				setState(178);
				valType();
				setState(179);
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
	public static class DefTypeContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode FUNC() { return getToken(WatParser.FUNC, 0); }
		public FuncTypeContext funcType() {
			return getRuleContext(FuncTypeContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public TerminalNode CONT() { return getToken(WatParser.CONT, 0); }
		public IdxContext idx() {
			return getRuleContext(IdxContext.class,0);
		}
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
			setState(193);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(183);
				match(LPAR);
				setState(184);
				match(FUNC);
				setState(185);
				funcType();
				setState(186);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(188);
				match(LPAR);
				setState(189);
				match(CONT);
				setState(190);
				idx();
				setState(191);
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
			setState(211);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(195);
					match(LPAR);
					setState(196);
					match(PARAM);
					setState(206);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case LPAR:
					case RPAR:
					case VALUE_TYPE:
					case FUNCREF:
					case EXTERNREF:
					case V128:
						{
						setState(200);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3202L) != 0) || _la==V128) {
							{
							{
							setState(197);
							valType();
							}
							}
							setState(202);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						}
						break;
					case VAR:
						{
						setState(203);
						bindVar();
						setState(204);
						valType();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(208);
					match(RPAR);
					}
					} 
				}
				setState(213);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
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
			setState(225);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(214);
					match(LPAR);
					setState(215);
					match(RESULT);
					setState(219);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3202L) != 0) || _la==V128) {
						{
						{
						setState(216);
						valType();
						}
						}
						setState(221);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(222);
					match(RPAR);
					}
					} 
				}
				setState(227);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
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
			setState(228);
			funcParamType();
			setState(229);
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
			setState(231);
			match(NAT);
			setState(233);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAT) {
				{
				setState(232);
				match(NAT);
				}
			}

			setState(235);
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
			setState(237);
			match(NAT);
			setState(239);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAT) {
				{
				setState(238);
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
			setState(241);
			match(LPAR);
			setState(242);
			match(TYPE);
			setState(243);
			idx();
			setState(244);
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
			setState(246);
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
			setState(248);
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
			setState(250);
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
		public BlockInstrContext blockInstr() {
			return getRuleContext(BlockInstrContext.class,0);
		}
		public FoldedInstrContext foldedInstr() {
			return getRuleContext(FoldedInstrContext.class,0);
		}
		public ResumeInstrContext resumeInstr() {
			return getRuleContext(ResumeInstrContext.class,0);
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
			setState(256);
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
			case CALL:
			case CALL_INDIRECT:
			case RETURN_CALL:
			case RETURN_CALL_INDIRECT:
			case REFFUNC:
			case CONTNEW:
			case SUSPEND:
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
				setState(252);
				plainInstr();
				}
				break;
			case BLOCK:
			case LOOP:
			case IF:
				enterOuterAlt(_localctx, 2);
				{
				setState(253);
				blockInstr();
				}
				break;
			case LPAR:
				enterOuterAlt(_localctx, 3);
				{
				setState(254);
				foldedInstr();
				}
				break;
			case RESUME:
				enterOuterAlt(_localctx, 4);
				{
				setState(255);
				resumeInstr();
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
		public SelectInstrContext selectInstr() {
			return getRuleContext(SelectInstrContext.class,0);
		}
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
		public TerminalNode RETURN_CALL() { return getToken(WatParser.RETURN_CALL, 0); }
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
		public CallIndirectInstrContext callIndirectInstr() {
			return getRuleContext(CallIndirectInstrContext.class,0);
		}
		public TerminalNode CONTNEW() { return getToken(WatParser.CONTNEW, 0); }
		public TerminalNode REFFUNC() { return getToken(WatParser.REFFUNC, 0); }
		public TerminalNode SUSPEND() { return getToken(WatParser.SUSPEND, 0); }
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
			setState(325);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(258);
				match(UNREACHABLE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(259);
				match(NOP);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(260);
				match(DROP);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(261);
				selectInstr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(262);
				match(BR);
				setState(263);
				idx();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(264);
				match(BR_IF);
				setState(265);
				idx();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(266);
				match(BR_TABLE);
				setState(268); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(267);
						idx();
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(270); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(272);
				match(RETURN);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(273);
				match(CALL);
				setState(274);
				idx();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(275);
				match(RETURN_CALL);
				setState(276);
				idx();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(277);
				match(LOCAL_GET);
				setState(278);
				idx();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(279);
				match(LOCAL_SET);
				setState(280);
				idx();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(281);
				match(LOCAL_TEE);
				setState(282);
				idx();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(283);
				match(GLOBAL_GET);
				setState(284);
				idx();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(285);
				match(GLOBAL_SET);
				setState(286);
				idx();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(287);
				load();
				setState(289);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==OFFSET_EQ) {
					{
					setState(288);
					offsetEq();
					}
				}

				setState(292);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ALIGN_EQ) {
					{
					setState(291);
					alignEq();
					}
				}

				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(294);
				store();
				setState(296);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==OFFSET_EQ) {
					{
					setState(295);
					offsetEq();
					}
				}

				setState(299);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ALIGN_EQ) {
					{
					setState(298);
					alignEq();
					}
				}

				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(301);
				match(MEMORY_SIZE);
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(302);
				match(MEMORY_GROW);
				}
				break;
			case 20:
				enterOuterAlt(_localctx, 20);
				{
				setState(303);
				match(MEMORY_FILL);
				}
				break;
			case 21:
				enterOuterAlt(_localctx, 21);
				{
				setState(304);
				match(MEMORY_COPY);
				}
				break;
			case 22:
				enterOuterAlt(_localctx, 22);
				{
				setState(305);
				match(MEMORY_INIT);
				setState(306);
				idx();
				}
				break;
			case 23:
				enterOuterAlt(_localctx, 23);
				{
				setState(307);
				match(CONST);
				setState(308);
				literal();
				}
				break;
			case 24:
				enterOuterAlt(_localctx, 24);
				{
				setState(309);
				match(SYMBOLIC);
				}
				break;
			case 25:
				enterOuterAlt(_localctx, 25);
				{
				setState(310);
				match(SYM_ASSERT);
				}
				break;
			case 26:
				enterOuterAlt(_localctx, 26);
				{
				setState(311);
				match(ALLOC);
				}
				break;
			case 27:
				enterOuterAlt(_localctx, 27);
				{
				setState(312);
				match(FREE);
				}
				break;
			case 28:
				enterOuterAlt(_localctx, 28);
				{
				setState(313);
				match(TEST);
				}
				break;
			case 29:
				enterOuterAlt(_localctx, 29);
				{
				setState(314);
				match(COMPARE);
				}
				break;
			case 30:
				enterOuterAlt(_localctx, 30);
				{
				setState(315);
				match(UNARY);
				}
				break;
			case 31:
				enterOuterAlt(_localctx, 31);
				{
				setState(316);
				match(BINARY);
				}
				break;
			case 32:
				enterOuterAlt(_localctx, 32);
				{
				setState(317);
				match(CONVERT);
				}
				break;
			case 33:
				enterOuterAlt(_localctx, 33);
				{
				setState(318);
				callIndirectInstr();
				}
				break;
			case 34:
				enterOuterAlt(_localctx, 34);
				{
				setState(319);
				match(CONTNEW);
				setState(320);
				idx();
				}
				break;
			case 35:
				enterOuterAlt(_localctx, 35);
				{
				setState(321);
				match(REFFUNC);
				setState(322);
				idx();
				}
				break;
			case 36:
				enterOuterAlt(_localctx, 36);
				{
				setState(323);
				match(SUSPEND);
				setState(324);
				idx();
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
	public static class ResumeInstrContext extends ParserRuleContext {
		public TerminalNode RESUME() { return getToken(WatParser.RESUME, 0); }
		public IdxContext idx() {
			return getRuleContext(IdxContext.class,0);
		}
		public List<HandlerInstrContext> handlerInstr() {
			return getRuleContexts(HandlerInstrContext.class);
		}
		public HandlerInstrContext handlerInstr(int i) {
			return getRuleContext(HandlerInstrContext.class,i);
		}
		public ResumeInstrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resumeInstr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterResumeInstr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitResumeInstr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitResumeInstr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResumeInstrContext resumeInstr() throws RecognitionException {
		ResumeInstrContext _localctx = new ResumeInstrContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_resumeInstr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(327);
			match(RESUME);
			setState(328);
			idx();
			setState(332);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(329);
					handlerInstr();
					}
					} 
				}
				setState(334);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
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
	public static class HandlerInstrContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode ON() { return getToken(WatParser.ON, 0); }
		public List<IdxContext> idx() {
			return getRuleContexts(IdxContext.class);
		}
		public IdxContext idx(int i) {
			return getRuleContext(IdxContext.class,i);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public HandlerInstrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_handlerInstr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterHandlerInstr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitHandlerInstr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitHandlerInstr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HandlerInstrContext handlerInstr() throws RecognitionException {
		HandlerInstrContext _localctx = new HandlerInstrContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_handlerInstr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(335);
			match(LPAR);
			setState(336);
			match(ON);
			setState(337);
			idx();
			setState(338);
			idx();
			setState(339);
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
		enterRule(_localctx, 44, RULE_offsetEq);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(341);
			match(OFFSET_EQ);
			setState(342);
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
		enterRule(_localctx, 46, RULE_alignEq);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(344);
			match(ALIGN_EQ);
			setState(345);
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
		enterRule(_localctx, 48, RULE_load);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(347);
			numType();
			setState(348);
			match(LOAD);
			setState(352);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MEM_SIZE) {
				{
				setState(349);
				match(MEM_SIZE);
				setState(350);
				match(UNDERSCORE);
				setState(351);
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
		enterRule(_localctx, 50, RULE_store);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(354);
			numType();
			setState(355);
			match(STORE);
			setState(357);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MEM_SIZE) {
				{
				setState(356);
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
	public static class SelectInstrContext extends ParserRuleContext {
		public NumTypeContext numType() {
			return getRuleContext(NumTypeContext.class,0);
		}
		public TerminalNode SELECT() { return getToken(WatParser.SELECT, 0); }
		public SelectInstrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectInstr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterSelectInstr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitSelectInstr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitSelectInstr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectInstrContext selectInstr() throws RecognitionException {
		SelectInstrContext _localctx = new SelectInstrContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_selectInstr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(359);
			numType();
			setState(360);
			match(SELECT);
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
		public TypeUseContext typeUse() {
			return getRuleContext(TypeUseContext.class,0);
		}
		public IdxContext idx() {
			return getRuleContext(IdxContext.class,0);
		}
		public TerminalNode RETURN_CALL_INDIRECT() { return getToken(WatParser.RETURN_CALL_INDIRECT, 0); }
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
		enterRule(_localctx, 54, RULE_callIndirectInstr);
		int _la;
		try {
			setState(372);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CALL_INDIRECT:
				enterOuterAlt(_localctx, 1);
				{
				setState(362);
				match(CALL_INDIRECT);
				setState(364);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAT || _la==VAR) {
					{
					setState(363);
					idx();
					}
				}

				setState(366);
				typeUse();
				}
				break;
			case RETURN_CALL_INDIRECT:
				enterOuterAlt(_localctx, 2);
				{
				setState(367);
				match(RETURN_CALL_INDIRECT);
				setState(369);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAT || _la==VAR) {
					{
					setState(368);
					idx();
					}
				}

				setState(371);
				typeUse();
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
		enterRule(_localctx, 56, RULE_callInstrParams);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(385);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(374);
					match(LPAR);
					setState(375);
					match(PARAM);
					setState(379);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3202L) != 0) || _la==V128) {
						{
						{
						setState(376);
						valType();
						}
						}
						setState(381);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(382);
					match(RPAR);
					}
					} 
				}
				setState(387);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			}
			setState(399);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAR) {
				{
				{
				setState(388);
				match(LPAR);
				setState(389);
				match(RESULT);
				setState(393);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3202L) != 0) || _la==V128) {
					{
					{
					setState(390);
					valType();
					}
					}
					setState(395);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(396);
				match(RPAR);
				}
				}
				setState(401);
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
		enterRule(_localctx, 58, RULE_callInstrParamsInstr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(413);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(402);
					match(LPAR);
					setState(403);
					match(PARAM);
					setState(407);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3202L) != 0) || _la==V128) {
						{
						{
						setState(404);
						valType();
						}
						}
						setState(409);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(410);
					match(RPAR);
					}
					} 
				}
				setState(415);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
			}
			setState(416);
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
		enterRule(_localctx, 60, RULE_callInstrResultsInstr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(429);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(418);
					match(LPAR);
					setState(419);
					match(RESULT);
					setState(423);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3202L) != 0) || _la==V128) {
						{
						{
						setState(420);
						valType();
						}
						}
						setState(425);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(426);
					match(RPAR);
					}
					} 
				}
				setState(431);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
			}
			setState(432);
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
		enterRule(_localctx, 62, RULE_blockInstr);
		int _la;
		try {
			setState(468);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BLOCK:
				enterOuterAlt(_localctx, 1);
				{
				setState(434);
				match(BLOCK);
				setState(436);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(435);
					bindVar();
					}
				}

				setState(438);
				block();
				setState(439);
				match(END);
				setState(441);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
				case 1:
					{
					setState(440);
					bindVar();
					}
					break;
				}
				}
				break;
			case LOOP:
				enterOuterAlt(_localctx, 2);
				{
				setState(443);
				match(LOOP);
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
				block();
				setState(448);
				match(END);
				setState(450);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
				case 1:
					{
					setState(449);
					bindVar();
					}
					break;
				}
				}
				break;
			case IF:
				enterOuterAlt(_localctx, 3);
				{
				setState(452);
				match(IF);
				setState(454);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(453);
					bindVar();
					}
				}

				setState(456);
				block();
				setState(462);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(457);
					match(ELSE);
					setState(459);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==VAR) {
						{
						setState(458);
						bindVar();
						}
					}

					setState(461);
					instrList();
					}
				}

				setState(464);
				match(END);
				setState(466);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
				case 1:
					{
					setState(465);
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
		public TypeUseContext typeUse() {
			return getRuleContext(TypeUseContext.class,0);
		}
		public FuncTypeContext funcType() {
			return getRuleContext(FuncTypeContext.class,0);
		}
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
		enterRule(_localctx, 64, RULE_blockType);
		try {
			setState(481);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(475);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
				case 1:
					{
					setState(470);
					match(LPAR);
					setState(471);
					match(RESULT);
					setState(472);
					valType();
					setState(473);
					match(RPAR);
					}
					break;
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(477);
				typeUse();
				setState(478);
				funcType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(480);
				funcType();
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
	public static class BlockContext extends ParserRuleContext {
		public BlockTypeContext blockType() {
			return getRuleContext(BlockTypeContext.class,0);
		}
		public InstrListContext instrList() {
			return getRuleContext(InstrListContext.class,0);
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
		enterRule(_localctx, 66, RULE_block);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(483);
			blockType();
			setState(484);
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
		enterRule(_localctx, 68, RULE_foldedInstr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(486);
			match(LPAR);
			setState(487);
			expr();
			setState(488);
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
		public TerminalNode RETURN_CALL_INDIRECT() { return getToken(WatParser.RETURN_CALL_INDIRECT, 0); }
		public TerminalNode BLOCK() { return getToken(WatParser.BLOCK, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public BindVarContext bindVar() {
			return getRuleContext(BindVarContext.class,0);
		}
		public TerminalNode LOOP() { return getToken(WatParser.LOOP, 0); }
		public TerminalNode IF() { return getToken(WatParser.IF, 0); }
		public BlockTypeContext blockType() {
			return getRuleContext(BlockTypeContext.class,0);
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
		enterRule(_localctx, 70, RULE_expr);
		int _la;
		try {
			int _alt;
			setState(532);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(490);
				plainInstr();
				setState(494);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,44,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(491);
						expr();
						}
						} 
					}
					setState(496);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,44,_ctx);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(497);
				match(CALL_INDIRECT);
				setState(498);
				callExprType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(499);
				match(RETURN_CALL_INDIRECT);
				setState(500);
				callExprType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(501);
				match(BLOCK);
				setState(503);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
				case 1:
					{
					setState(502);
					bindVar();
					}
					break;
				}
				setState(505);
				block();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(506);
				match(LOOP);
				setState(508);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
				case 1:
					{
					setState(507);
					bindVar();
					}
					break;
				}
				setState(510);
				block();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(511);
				match(IF);
				setState(513);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(512);
					bindVar();
					}
				}

				setState(515);
				blockType();
				setState(519);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(516);
						foldedInstr();
						}
						} 
					}
					setState(521);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
				}
				setState(522);
				match(LPAR);
				setState(523);
				match(THEN);
				setState(524);
				instrList();
				setState(530);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LPAR) {
					{
					setState(525);
					match(LPAR);
					setState(526);
					match(ELSE);
					setState(527);
					instrList();
					setState(528);
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
		enterRule(_localctx, 72, RULE_callExprType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(535);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				{
				setState(534);
				typeUse();
				}
				break;
			}
			setState(537);
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
		enterRule(_localctx, 74, RULE_callExprParams);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(550);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(539);
					match(LPAR);
					setState(540);
					match(PARAM);
					setState(544);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3202L) != 0) || _la==V128) {
						{
						{
						setState(541);
						valType();
						}
						}
						setState(546);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(547);
					match(RPAR);
					}
					} 
				}
				setState(552);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
			}
			setState(553);
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
		enterRule(_localctx, 76, RULE_callExprResults);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(566);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAR) {
				{
				{
				setState(555);
				match(LPAR);
				setState(556);
				match(RESULT);
				setState(560);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3202L) != 0) || _la==V128) {
					{
					{
					setState(557);
					valType();
					}
					}
					setState(562);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(563);
				match(RPAR);
				}
				}
				setState(568);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(572);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,56,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(569);
					expr();
					}
					} 
				}
				setState(574);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,56,_ctx);
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
		enterRule(_localctx, 78, RULE_instrList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(578);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(575);
					instr();
					}
					} 
				}
				setState(580);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
			}
			setState(582);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
			case 1:
				{
				setState(581);
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
		enterRule(_localctx, 80, RULE_constExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(584);
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
		enterRule(_localctx, 82, RULE_function);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(586);
			match(LPAR);
			setState(587);
			match(FUNC);
			setState(589);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(588);
				bindVar();
				}
			}

			setState(591);
			funcFields();
			setState(592);
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
		enterRule(_localctx, 84, RULE_funcFields);
		try {
			setState(607);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(595);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
				case 1:
					{
					setState(594);
					typeUse();
					}
					break;
				}
				setState(597);
				funcFieldsBody();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(598);
				inlineImport();
				setState(600);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
				case 1:
					{
					setState(599);
					typeUse();
					}
					break;
				}
				setState(602);
				funcType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(604);
				inlineExport();
				setState(605);
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
		enterRule(_localctx, 86, RULE_funcFieldsBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(609);
			funcType();
			setState(610);
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
		enterRule(_localctx, 88, RULE_funcBody);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(628);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(612);
					match(LPAR);
					setState(613);
					match(LOCAL);
					setState(623);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case LPAR:
					case RPAR:
					case VALUE_TYPE:
					case FUNCREF:
					case EXTERNREF:
					case V128:
						{
						setState(617);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3202L) != 0) || _la==V128) {
							{
							{
							setState(614);
							valType();
							}
							}
							setState(619);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						}
						break;
					case VAR:
						{
						setState(620);
						bindVar();
						setState(621);
						valType();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(625);
					match(RPAR);
					}
					} 
				}
				setState(630);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
			}
			setState(631);
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
		enterRule(_localctx, 90, RULE_offset);
		try {
			setState(639);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LPAR:
				enterOuterAlt(_localctx, 1);
				{
				setState(633);
				match(LPAR);
				setState(634);
				match(OFFSET);
				setState(635);
				constExpr();
				setState(636);
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
			case CALL:
			case CALL_INDIRECT:
			case RETURN_CALL:
			case RETURN_CALL_INDIRECT:
			case REFFUNC:
			case CONTNEW:
			case SUSPEND:
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
				setState(638);
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
		public TerminalNode DECLARE() { return getToken(WatParser.DECLARE, 0); }
		public TerminalNode FUNC() { return getToken(WatParser.FUNC, 0); }
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
		enterRule(_localctx, 92, RULE_elem);
		int _la;
		try {
			setState(681);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(641);
				match(LPAR);
				setState(642);
				match(ELEM);
				setState(644);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAT || _la==VAR) {
					{
					setState(643);
					idx();
					}
				}

				setState(646);
				match(LPAR);
				setState(647);
				instr();
				setState(648);
				match(RPAR);
				setState(652);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NAT || _la==VAR) {
					{
					{
					setState(649);
					idx();
					}
					}
					setState(654);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(655);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(657);
				match(LPAR);
				setState(658);
				match(ELEM);
				setState(660);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAT || _la==VAR) {
					{
					setState(659);
					idx();
					}
				}

				setState(662);
				offset();
				setState(666);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NAT || _la==VAR) {
					{
					{
					setState(663);
					idx();
					}
					}
					setState(668);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(669);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(671);
				match(LPAR);
				setState(672);
				match(ELEM);
				setState(674);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAT || _la==VAR) {
					{
					setState(673);
					idx();
					}
				}

				setState(676);
				match(DECLARE);
				setState(677);
				match(FUNC);
				setState(678);
				idx();
				setState(679);
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
		enterRule(_localctx, 94, RULE_table);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(683);
			match(LPAR);
			setState(684);
			match(TABLE);
			setState(686);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(685);
				bindVar();
				}
			}

			setState(688);
			tableField();
			setState(689);
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
		enterRule(_localctx, 96, RULE_tableField);
		int _la;
		try {
			setState(709);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,75,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(691);
				tableType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(692);
				inlineImport();
				setState(693);
				tableType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(695);
				inlineExport();
				setState(696);
				tableField();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(698);
				refType();
				setState(699);
				match(LPAR);
				setState(700);
				match(ELEM);
				setState(704);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NAT || _la==VAR) {
					{
					{
					setState(701);
					idx();
					}
					}
					setState(706);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(707);
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
		enterRule(_localctx, 98, RULE_data);
		int _la;
		try {
			setState(741);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,80,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(711);
				match(LPAR);
				setState(712);
				match(DATA);
				setState(714);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAT || _la==VAR) {
					{
					setState(713);
					idx();
					}
				}

				setState(716);
				match(LPAR);
				setState(717);
				instr();
				setState(718);
				match(RPAR);
				setState(722);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STRING_) {
					{
					{
					setState(719);
					match(STRING_);
					}
					}
					setState(724);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(725);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(727);
				match(LPAR);
				setState(728);
				match(DATA);
				setState(730);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAT || _la==VAR) {
					{
					setState(729);
					idx();
					}
				}

				setState(732);
				offset();
				setState(736);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STRING_) {
					{
					{
					setState(733);
					match(STRING_);
					}
					}
					setState(738);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(739);
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
		enterRule(_localctx, 100, RULE_memory);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(743);
			match(LPAR);
			setState(744);
			match(MEMORY);
			setState(746);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(745);
				bindVar();
				}
			}

			setState(748);
			memoryField();
			setState(749);
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
		enterRule(_localctx, 102, RULE_memoryField);
		int _la;
		try {
			setState(767);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,83,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(751);
				memoryType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(752);
				inlineImport();
				setState(753);
				memoryType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(755);
				inlineExport();
				setState(756);
				memoryField();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(758);
				match(LPAR);
				setState(759);
				match(DATA);
				setState(763);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STRING_) {
					{
					{
					setState(760);
					match(STRING_);
					}
					}
					setState(765);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(766);
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
		enterRule(_localctx, 104, RULE_global);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(769);
			match(LPAR);
			setState(770);
			match(GLOBAL);
			setState(772);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(771);
				bindVar();
				}
			}

			setState(774);
			globalField();
			setState(775);
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
		enterRule(_localctx, 106, RULE_globalField);
		try {
			setState(786);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(777);
				globalType();
				setState(778);
				constExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(780);
				inlineImport();
				setState(781);
				globalType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(783);
				inlineExport();
				setState(784);
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
		enterRule(_localctx, 108, RULE_importDesc);
		int _la;
		try {
			setState(828);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,91,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(788);
				match(LPAR);
				setState(789);
				match(FUNC);
				setState(791);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(790);
					bindVar();
					}
				}

				setState(793);
				typeUse();
				setState(794);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(796);
				match(LPAR);
				setState(797);
				match(FUNC);
				setState(799);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(798);
					bindVar();
					}
				}

				setState(801);
				funcType();
				setState(802);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(804);
				match(LPAR);
				setState(805);
				match(TABLE);
				setState(807);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(806);
					bindVar();
					}
				}

				setState(809);
				tableType();
				setState(810);
				match(RPAR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(812);
				match(LPAR);
				setState(813);
				match(MEMORY);
				setState(815);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(814);
					bindVar();
					}
				}

				setState(817);
				memoryType();
				setState(818);
				match(RPAR);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(820);
				match(LPAR);
				setState(821);
				match(GLOBAL);
				setState(823);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(822);
					bindVar();
					}
				}

				setState(825);
				globalType();
				setState(826);
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
		enterRule(_localctx, 110, RULE_simport);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(830);
			match(LPAR);
			setState(831);
			match(IMPORT);
			setState(832);
			name();
			setState(833);
			name();
			setState(834);
			importDesc();
			setState(835);
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
		enterRule(_localctx, 112, RULE_inlineImport);
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
		enterRule(_localctx, 114, RULE_exportDesc);
		try {
			setState(863);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,92,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(843);
				match(LPAR);
				setState(844);
				match(FUNC);
				setState(845);
				idx();
				setState(846);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(848);
				match(LPAR);
				setState(849);
				match(TABLE);
				setState(850);
				idx();
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
				match(MEMORY);
				setState(855);
				idx();
				setState(856);
				match(RPAR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(858);
				match(LPAR);
				setState(859);
				match(GLOBAL);
				setState(860);
				idx();
				setState(861);
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
		enterRule(_localctx, 116, RULE_export_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(865);
			match(LPAR);
			setState(866);
			match(EXPORT);
			setState(867);
			name();
			setState(868);
			exportDesc();
			setState(869);
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
		enterRule(_localctx, 118, RULE_inlineExport);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(871);
			match(LPAR);
			setState(872);
			match(EXPORT);
			setState(873);
			name();
			setState(874);
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
	public static class TagContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode TAG() { return getToken(WatParser.TAG, 0); }
		public TypeUseContext typeUse() {
			return getRuleContext(TypeUseContext.class,0);
		}
		public FuncTypeContext funcType() {
			return getRuleContext(FuncTypeContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public BindVarContext bindVar() {
			return getRuleContext(BindVarContext.class,0);
		}
		public TagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tag; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterTag(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitTag(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitTag(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TagContext tag() throws RecognitionException {
		TagContext _localctx = new TagContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_tag);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(876);
			match(LPAR);
			setState(877);
			match(TAG);
			setState(879);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(878);
				bindVar();
				}
			}

			setState(881);
			typeUse();
			setState(882);
			funcType();
			setState(883);
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
		enterRule(_localctx, 122, RULE_typeDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(885);
			match(LPAR);
			setState(886);
			match(TYPE);
			setState(888);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(887);
				bindVar();
				}
			}

			setState(890);
			defType();
			setState(891);
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
		enterRule(_localctx, 124, RULE_start_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(893);
			match(LPAR);
			setState(894);
			match(START_);
			setState(895);
			idx();
			setState(896);
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
		public TagContext tag() {
			return getRuleContext(TagContext.class,0);
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
		enterRule(_localctx, 126, RULE_moduleField);
		try {
			setState(909);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,95,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(898);
				typeDef();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(899);
				global();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(900);
				table();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(901);
				memory();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(902);
				function();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(903);
				elem();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(904);
				data();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(905);
				start_();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(906);
				simport();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(907);
				export_();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(908);
				tag();
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
		enterRule(_localctx, 128, RULE_module_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(911);
			match(LPAR);
			setState(912);
			match(MODULE);
			setState(914);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(913);
				match(VAR);
				}
			}

			setState(919);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAR) {
				{
				{
				setState(916);
				moduleField();
				}
				}
				setState(921);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(922);
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
		enterRule(_localctx, 130, RULE_scriptModule);
		int _la;
		try {
			setState(938);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,100,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(924);
				module_();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(925);
				match(LPAR);
				setState(926);
				match(MODULE);
				setState(928);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(927);
					match(VAR);
					}
				}

				setState(930);
				_la = _input.LA(1);
				if ( !(_la==BIN || _la==QUOTE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(934);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STRING_) {
					{
					{
					setState(931);
					match(STRING_);
					}
					}
					setState(936);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(937);
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
		enterRule(_localctx, 132, RULE_action_);
		int _la;
		try {
			setState(957);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(940);
				match(LPAR);
				setState(941);
				match(INVOKE);
				setState(943);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(942);
					match(VAR);
					}
				}

				setState(945);
				name();
				setState(946);
				constList();
				setState(947);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(949);
				match(LPAR);
				setState(950);
				match(GET);
				setState(952);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(951);
					match(VAR);
					}
				}

				setState(954);
				name();
				setState(955);
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
		enterRule(_localctx, 134, RULE_assertion);
		try {
			setState(1011);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,104,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(959);
				match(LPAR);
				setState(960);
				match(ASSERT_MALFORMED);
				setState(961);
				scriptModule();
				setState(962);
				match(STRING_);
				setState(963);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(965);
				match(LPAR);
				setState(966);
				match(ASSERT_INVALID);
				setState(967);
				scriptModule();
				setState(968);
				match(STRING_);
				setState(969);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(971);
				match(LPAR);
				setState(972);
				match(ASSERT_UNLINKABLE);
				setState(973);
				scriptModule();
				setState(974);
				match(STRING_);
				setState(975);
				match(RPAR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(977);
				match(LPAR);
				setState(978);
				match(ASSERT_TRAP);
				setState(979);
				scriptModule();
				setState(980);
				match(STRING_);
				setState(981);
				match(RPAR);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(983);
				match(LPAR);
				setState(984);
				match(ASSERT_RETURN);
				setState(985);
				action_();
				setState(986);
				constList();
				setState(987);
				match(RPAR);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(989);
				match(LPAR);
				setState(990);
				match(ASSERT_RETURN_CANONICAL_NAN);
				setState(991);
				action_();
				setState(992);
				match(RPAR);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(994);
				match(LPAR);
				setState(995);
				match(ASSERT_RETURN_ARITHMETIC_NAN);
				setState(996);
				action_();
				setState(997);
				match(RPAR);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(999);
				match(LPAR);
				setState(1000);
				match(ASSERT_TRAP);
				setState(1001);
				action_();
				setState(1002);
				match(STRING_);
				setState(1003);
				match(RPAR);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1005);
				match(LPAR);
				setState(1006);
				match(ASSERT_EXHAUSTION);
				setState(1007);
				action_();
				setState(1008);
				match(STRING_);
				setState(1009);
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
		enterRule(_localctx, 136, RULE_cmd);
		int _la;
		try {
			setState(1025);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,106,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1013);
				action_();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1014);
				assertion();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1015);
				scriptModule();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1016);
				match(LPAR);
				setState(1017);
				match(REGISTER);
				setState(1018);
				name();
				setState(1020);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(1019);
					match(VAR);
					}
				}

				setState(1022);
				match(RPAR);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1024);
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
		enterRule(_localctx, 138, RULE_meta);
		int _la;
		try {
			setState(1059);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,112,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1027);
				match(LPAR);
				setState(1028);
				match(SCRIPT);
				setState(1030);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(1029);
					match(VAR);
					}
				}

				setState(1035);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LPAR) {
					{
					{
					setState(1032);
					cmd();
					}
					}
					setState(1037);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1038);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1039);
				match(LPAR);
				setState(1040);
				match(INPUT);
				setState(1042);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(1041);
					match(VAR);
					}
				}

				setState(1044);
				match(STRING_);
				setState(1045);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1046);
				match(LPAR);
				setState(1047);
				match(OUTPUT);
				setState(1049);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(1048);
					match(VAR);
					}
				}

				setState(1051);
				match(STRING_);
				setState(1052);
				match(RPAR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1053);
				match(LPAR);
				setState(1054);
				match(OUTPUT);
				setState(1056);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(1055);
					match(VAR);
					}
				}

				setState(1058);
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
		enterRule(_localctx, 140, RULE_wconst);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1061);
			match(LPAR);
			setState(1062);
			match(CONST);
			setState(1063);
			literal();
			setState(1064);
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
		enterRule(_localctx, 142, RULE_constList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1069);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAR) {
				{
				{
				setState(1066);
				wconst();
				}
				}
				setState(1071);
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
		enterRule(_localctx, 144, RULE_script);
		int _la;
		try {
			setState(1086);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,116,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1075);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LPAR) {
					{
					{
					setState(1072);
					cmd();
					}
					}
					setState(1077);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1078);
				match(EOF);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1080); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(1079);
					moduleField();
					}
					}
					setState(1082); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==LPAR );
				setState(1084);
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
		enterRule(_localctx, 146, RULE_module);
		int _la;
		try {
			setState(1098);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,118,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1088);
				module_();
				setState(1089);
				match(EOF);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1094);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LPAR) {
					{
					{
					setState(1091);
					moduleField();
					}
					}
					setState(1096);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1097);
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
		"\u0004\u0001\u009e\u044d\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
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
		"E\u0002F\u0007F\u0002G\u0007G\u0002H\u0007H\u0002I\u0007I\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0003\u0003\u00a2\b\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0003\u0005\u00a9\b\u0005\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0003\u0006\u00ae\b\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0003\u0007\u00b6\b\u0007\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0003\b\u00c2"+
		"\b\b\u0001\t\u0001\t\u0001\t\u0005\t\u00c7\b\t\n\t\f\t\u00ca\t\t\u0001"+
		"\t\u0001\t\u0001\t\u0003\t\u00cf\b\t\u0001\t\u0005\t\u00d2\b\t\n\t\f\t"+
		"\u00d5\t\t\u0001\n\u0001\n\u0001\n\u0005\n\u00da\b\n\n\n\f\n\u00dd\t\n"+
		"\u0001\n\u0005\n\u00e0\b\n\n\n\f\n\u00e3\t\n\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\f\u0001\f\u0003\f\u00ea\b\f\u0001\f\u0001\f\u0001\r\u0001"+
		"\r\u0003\r\u00f0\b\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0011\u0001"+
		"\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u0101"+
		"\b\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0004\u0013\u010d"+
		"\b\u0013\u000b\u0013\f\u0013\u010e\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0003\u0013\u0122\b\u0013\u0001\u0013\u0003\u0013"+
		"\u0125\b\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u0129\b\u0013\u0001"+
		"\u0013\u0003\u0013\u012c\b\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u0146\b\u0013\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0005\u0014\u014b\b\u0014\n\u0014\f\u0014\u014e\t\u0014"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0017"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0003\u0018"+
		"\u0161\b\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u0166\b"+
		"\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0003"+
		"\u001b\u016d\b\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0003\u001b\u0172"+
		"\b\u001b\u0001\u001b\u0003\u001b\u0175\b\u001b\u0001\u001c\u0001\u001c"+
		"\u0001\u001c\u0005\u001c\u017a\b\u001c\n\u001c\f\u001c\u017d\t\u001c\u0001"+
		"\u001c\u0005\u001c\u0180\b\u001c\n\u001c\f\u001c\u0183\t\u001c\u0001\u001c"+
		"\u0001\u001c\u0001\u001c\u0005\u001c\u0188\b\u001c\n\u001c\f\u001c\u018b"+
		"\t\u001c\u0001\u001c\u0005\u001c\u018e\b\u001c\n\u001c\f\u001c\u0191\t"+
		"\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0005\u001d\u0196\b\u001d\n"+
		"\u001d\f\u001d\u0199\t\u001d\u0001\u001d\u0005\u001d\u019c\b\u001d\n\u001d"+
		"\f\u001d\u019f\t\u001d\u0001\u001d\u0001\u001d\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0005\u001e\u01a6\b\u001e\n\u001e\f\u001e\u01a9\t\u001e\u0001"+
		"\u001e\u0005\u001e\u01ac\b\u001e\n\u001e\f\u001e\u01af\t\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001f\u0001\u001f\u0003\u001f\u01b5\b\u001f\u0001\u001f"+
		"\u0001\u001f\u0001\u001f\u0003\u001f\u01ba\b\u001f\u0001\u001f\u0001\u001f"+
		"\u0003\u001f\u01be\b\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0003\u001f"+
		"\u01c3\b\u001f\u0001\u001f\u0001\u001f\u0003\u001f\u01c7\b\u001f\u0001"+
		"\u001f\u0001\u001f\u0001\u001f\u0003\u001f\u01cc\b\u001f\u0001\u001f\u0003"+
		"\u001f\u01cf\b\u001f\u0001\u001f\u0001\u001f\u0003\u001f\u01d3\b\u001f"+
		"\u0003\u001f\u01d5\b\u001f\u0001 \u0001 \u0001 \u0001 \u0001 \u0003 \u01dc"+
		"\b \u0001 \u0001 \u0001 \u0001 \u0003 \u01e2\b \u0001!\u0001!\u0001!\u0001"+
		"\"\u0001\"\u0001\"\u0001\"\u0001#\u0001#\u0005#\u01ed\b#\n#\f#\u01f0\t"+
		"#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0003#\u01f8\b#\u0001#\u0001"+
		"#\u0001#\u0003#\u01fd\b#\u0001#\u0001#\u0001#\u0003#\u0202\b#\u0001#\u0001"+
		"#\u0005#\u0206\b#\n#\f#\u0209\t#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001"+
		"#\u0001#\u0001#\u0003#\u0213\b#\u0003#\u0215\b#\u0001$\u0003$\u0218\b"+
		"$\u0001$\u0001$\u0001%\u0001%\u0001%\u0005%\u021f\b%\n%\f%\u0222\t%\u0001"+
		"%\u0005%\u0225\b%\n%\f%\u0228\t%\u0001%\u0001%\u0001&\u0001&\u0001&\u0005"+
		"&\u022f\b&\n&\f&\u0232\t&\u0001&\u0005&\u0235\b&\n&\f&\u0238\t&\u0001"+
		"&\u0005&\u023b\b&\n&\f&\u023e\t&\u0001\'\u0005\'\u0241\b\'\n\'\f\'\u0244"+
		"\t\'\u0001\'\u0003\'\u0247\b\'\u0001(\u0001(\u0001)\u0001)\u0001)\u0003"+
		")\u024e\b)\u0001)\u0001)\u0001)\u0001*\u0003*\u0254\b*\u0001*\u0001*\u0001"+
		"*\u0003*\u0259\b*\u0001*\u0001*\u0001*\u0001*\u0001*\u0003*\u0260\b*\u0001"+
		"+\u0001+\u0001+\u0001,\u0001,\u0001,\u0005,\u0268\b,\n,\f,\u026b\t,\u0001"+
		",\u0001,\u0001,\u0003,\u0270\b,\u0001,\u0005,\u0273\b,\n,\f,\u0276\t,"+
		"\u0001,\u0001,\u0001-\u0001-\u0001-\u0001-\u0001-\u0001-\u0003-\u0280"+
		"\b-\u0001.\u0001.\u0001.\u0003.\u0285\b.\u0001.\u0001.\u0001.\u0001.\u0005"+
		".\u028b\b.\n.\f.\u028e\t.\u0001.\u0001.\u0001.\u0001.\u0001.\u0003.\u0295"+
		"\b.\u0001.\u0001.\u0005.\u0299\b.\n.\f.\u029c\t.\u0001.\u0001.\u0001."+
		"\u0001.\u0001.\u0003.\u02a3\b.\u0001.\u0001.\u0001.\u0001.\u0001.\u0003"+
		".\u02aa\b.\u0001/\u0001/\u0001/\u0003/\u02af\b/\u0001/\u0001/\u0001/\u0001"+
		"0\u00010\u00010\u00010\u00010\u00010\u00010\u00010\u00010\u00010\u0001"+
		"0\u00050\u02bf\b0\n0\f0\u02c2\t0\u00010\u00010\u00030\u02c6\b0\u00011"+
		"\u00011\u00011\u00031\u02cb\b1\u00011\u00011\u00011\u00011\u00051\u02d1"+
		"\b1\n1\f1\u02d4\t1\u00011\u00011\u00011\u00011\u00011\u00031\u02db\b1"+
		"\u00011\u00011\u00051\u02df\b1\n1\f1\u02e2\t1\u00011\u00011\u00031\u02e6"+
		"\b1\u00012\u00012\u00012\u00032\u02eb\b2\u00012\u00012\u00012\u00013\u0001"+
		"3\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u00053\u02fa"+
		"\b3\n3\f3\u02fd\t3\u00013\u00033\u0300\b3\u00014\u00014\u00014\u00034"+
		"\u0305\b4\u00014\u00014\u00014\u00015\u00015\u00015\u00015\u00015\u0001"+
		"5\u00015\u00015\u00015\u00035\u0313\b5\u00016\u00016\u00016\u00036\u0318"+
		"\b6\u00016\u00016\u00016\u00016\u00016\u00016\u00036\u0320\b6\u00016\u0001"+
		"6\u00016\u00016\u00016\u00016\u00036\u0328\b6\u00016\u00016\u00016\u0001"+
		"6\u00016\u00016\u00036\u0330\b6\u00016\u00016\u00016\u00016\u00016\u0001"+
		"6\u00036\u0338\b6\u00016\u00016\u00016\u00036\u033d\b6\u00017\u00017\u0001"+
		"7\u00017\u00017\u00017\u00017\u00018\u00018\u00018\u00018\u00018\u0001"+
		"8\u00019\u00019\u00019\u00019\u00019\u00019\u00019\u00019\u00019\u0001"+
		"9\u00019\u00019\u00019\u00019\u00019\u00019\u00019\u00019\u00019\u0001"+
		"9\u00039\u0360\b9\u0001:\u0001:\u0001:\u0001:\u0001:\u0001:\u0001;\u0001"+
		";\u0001;\u0001;\u0001;\u0001<\u0001<\u0001<\u0003<\u0370\b<\u0001<\u0001"+
		"<\u0001<\u0001<\u0001=\u0001=\u0001=\u0003=\u0379\b=\u0001=\u0001=\u0001"+
		"=\u0001>\u0001>\u0001>\u0001>\u0001>\u0001?\u0001?\u0001?\u0001?\u0001"+
		"?\u0001?\u0001?\u0001?\u0001?\u0001?\u0001?\u0003?\u038e\b?\u0001@\u0001"+
		"@\u0001@\u0003@\u0393\b@\u0001@\u0005@\u0396\b@\n@\f@\u0399\t@\u0001@"+
		"\u0001@\u0001A\u0001A\u0001A\u0001A\u0003A\u03a1\bA\u0001A\u0001A\u0005"+
		"A\u03a5\bA\nA\fA\u03a8\tA\u0001A\u0003A\u03ab\bA\u0001B\u0001B\u0001B"+
		"\u0003B\u03b0\bB\u0001B\u0001B\u0001B\u0001B\u0001B\u0001B\u0001B\u0003"+
		"B\u03b9\bB\u0001B\u0001B\u0001B\u0003B\u03be\bB\u0001C\u0001C\u0001C\u0001"+
		"C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001"+
		"C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001"+
		"C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001"+
		"C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001"+
		"C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0003C\u03f4"+
		"\bC\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0003D\u03fd\bD\u0001"+
		"D\u0001D\u0001D\u0003D\u0402\bD\u0001E\u0001E\u0001E\u0003E\u0407\bE\u0001"+
		"E\u0005E\u040a\bE\nE\fE\u040d\tE\u0001E\u0001E\u0001E\u0001E\u0003E\u0413"+
		"\bE\u0001E\u0001E\u0001E\u0001E\u0001E\u0003E\u041a\bE\u0001E\u0001E\u0001"+
		"E\u0001E\u0001E\u0003E\u0421\bE\u0001E\u0003E\u0424\bE\u0001F\u0001F\u0001"+
		"F\u0001F\u0001F\u0001G\u0005G\u042c\bG\nG\fG\u042f\tG\u0001H\u0005H\u0432"+
		"\bH\nH\fH\u0435\tH\u0001H\u0001H\u0004H\u0439\bH\u000bH\fH\u043a\u0001"+
		"H\u0001H\u0003H\u043f\bH\u0001I\u0001I\u0001I\u0001I\u0005I\u0445\bI\n"+
		"I\fI\u0448\tI\u0001I\u0003I\u044b\bI\u0001I\u0000\u0000J\u0000\u0002\u0004"+
		"\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \""+
		"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086"+
		"\u0088\u008a\u008c\u008e\u0090\u0092\u0000\u0004\u0001\u0000\u0004\u0005"+
		"\u0001\u0000\u0003\u0005\u0002\u0000\u0003\u0003\u009b\u009b\u0001\u0000"+
		"\u008b\u008c\u04c7\u0000\u0094\u0001\u0000\u0000\u0000\u0002\u0096\u0001"+
		"\u0000\u0000\u0000\u0004\u0098\u0001\u0000\u0000\u0000\u0006\u00a1\u0001"+
		"\u0000\u0000\u0000\b\u00a3\u0001\u0000\u0000\u0000\n\u00a8\u0001\u0000"+
		"\u0000\u0000\f\u00ad\u0001\u0000\u0000\u0000\u000e\u00b5\u0001\u0000\u0000"+
		"\u0000\u0010\u00c1\u0001\u0000\u0000\u0000\u0012\u00d3\u0001\u0000\u0000"+
		"\u0000\u0014\u00e1\u0001\u0000\u0000\u0000\u0016\u00e4\u0001\u0000\u0000"+
		"\u0000\u0018\u00e7\u0001\u0000\u0000\u0000\u001a\u00ed\u0001\u0000\u0000"+
		"\u0000\u001c\u00f1\u0001\u0000\u0000\u0000\u001e\u00f6\u0001\u0000\u0000"+
		"\u0000 \u00f8\u0001\u0000\u0000\u0000\"\u00fa\u0001\u0000\u0000\u0000"+
		"$\u0100\u0001\u0000\u0000\u0000&\u0145\u0001\u0000\u0000\u0000(\u0147"+
		"\u0001\u0000\u0000\u0000*\u014f\u0001\u0000\u0000\u0000,\u0155\u0001\u0000"+
		"\u0000\u0000.\u0158\u0001\u0000\u0000\u00000\u015b\u0001\u0000\u0000\u0000"+
		"2\u0162\u0001\u0000\u0000\u00004\u0167\u0001\u0000\u0000\u00006\u0174"+
		"\u0001\u0000\u0000\u00008\u0181\u0001\u0000\u0000\u0000:\u019d\u0001\u0000"+
		"\u0000\u0000<\u01ad\u0001\u0000\u0000\u0000>\u01d4\u0001\u0000\u0000\u0000"+
		"@\u01e1\u0001\u0000\u0000\u0000B\u01e3\u0001\u0000\u0000\u0000D\u01e6"+
		"\u0001\u0000\u0000\u0000F\u0214\u0001\u0000\u0000\u0000H\u0217\u0001\u0000"+
		"\u0000\u0000J\u0226\u0001\u0000\u0000\u0000L\u0236\u0001\u0000\u0000\u0000"+
		"N\u0242\u0001\u0000\u0000\u0000P\u0248\u0001\u0000\u0000\u0000R\u024a"+
		"\u0001\u0000\u0000\u0000T\u025f\u0001\u0000\u0000\u0000V\u0261\u0001\u0000"+
		"\u0000\u0000X\u0274\u0001\u0000\u0000\u0000Z\u027f\u0001\u0000\u0000\u0000"+
		"\\\u02a9\u0001\u0000\u0000\u0000^\u02ab\u0001\u0000\u0000\u0000`\u02c5"+
		"\u0001\u0000\u0000\u0000b\u02e5\u0001\u0000\u0000\u0000d\u02e7\u0001\u0000"+
		"\u0000\u0000f\u02ff\u0001\u0000\u0000\u0000h\u0301\u0001\u0000\u0000\u0000"+
		"j\u0312\u0001\u0000\u0000\u0000l\u033c\u0001\u0000\u0000\u0000n\u033e"+
		"\u0001\u0000\u0000\u0000p\u0345\u0001\u0000\u0000\u0000r\u035f\u0001\u0000"+
		"\u0000\u0000t\u0361\u0001\u0000\u0000\u0000v\u0367\u0001\u0000\u0000\u0000"+
		"x\u036c\u0001\u0000\u0000\u0000z\u0375\u0001\u0000\u0000\u0000|\u037d"+
		"\u0001\u0000\u0000\u0000~\u038d\u0001\u0000\u0000\u0000\u0080\u038f\u0001"+
		"\u0000\u0000\u0000\u0082\u03aa\u0001\u0000\u0000\u0000\u0084\u03bd\u0001"+
		"\u0000\u0000\u0000\u0086\u03f3\u0001\u0000\u0000\u0000\u0088\u0401\u0001"+
		"\u0000\u0000\u0000\u008a\u0423\u0001\u0000\u0000\u0000\u008c\u0425\u0001"+
		"\u0000\u0000\u0000\u008e\u042d\u0001\u0000\u0000\u0000\u0090\u043e\u0001"+
		"\u0000\u0000\u0000\u0092\u044a\u0001\u0000\u0000\u0000\u0094\u0095\u0007"+
		"\u0000\u0000\u0000\u0095\u0001\u0001\u0000\u0000\u0000\u0096\u0097\u0005"+
		"\u0006\u0000\u0000\u0097\u0003\u0001\u0000\u0000\u0000\u0098\u0099\u0005"+
		"\u0007\u0000\u0000\u0099\u0005\u0001\u0000\u0000\u0000\u009a\u00a2\u0005"+
		"\n\u0000\u0000\u009b\u00a2\u0005\u000b\u0000\u0000\u009c\u009d\u0005\u0001"+
		"\u0000\u0000\u009d\u009e\u0005\r\u0000\u0000\u009e\u009f\u0003 \u0010"+
		"\u0000\u009f\u00a0\u0005\u0002\u0000\u0000\u00a0\u00a2\u0001\u0000\u0000"+
		"\u0000\u00a1\u009a\u0001\u0000\u0000\u0000\u00a1\u009b\u0001\u0000\u0000"+
		"\u0000\u00a1\u009c\u0001\u0000\u0000\u0000\u00a2\u0007\u0001\u0000\u0000"+
		"\u0000\u00a3\u00a4\u0005\u009c\u0000\u0000\u00a4\t\u0001\u0000\u0000\u0000"+
		"\u00a5\u00a9\u0003\u0004\u0002\u0000\u00a6\u00a9\u0003\b\u0004\u0000\u00a7"+
		"\u00a9\u0003\u0006\u0003\u0000\u00a8\u00a5\u0001\u0000\u0000\u0000\u00a8"+
		"\u00a6\u0001\u0000\u0000\u0000\u00a8\u00a7\u0001\u0000\u0000\u0000\u00a9"+
		"\u000b\u0001\u0000\u0000\u0000\u00aa\u00ae\u0005z\u0000\u0000\u00ab\u00ae"+
		"\u0005{\u0000\u0000\u00ac\u00ae\u0003\u0016\u000b\u0000\u00ad\u00aa\u0001"+
		"\u0000\u0000\u0000\u00ad\u00ab\u0001\u0000\u0000\u0000\u00ad\u00ac\u0001"+
		"\u0000\u0000\u0000\u00ae\r\u0001\u0000\u0000\u0000\u00af\u00b6\u0003\n"+
		"\u0005\u0000\u00b0\u00b1\u0005\u0001\u0000\u0000\u00b1\u00b2\u0005\f\u0000"+
		"\u0000\u00b2\u00b3\u0003\n\u0005\u0000\u00b3\u00b4\u0005\u0002\u0000\u0000"+
		"\u00b4\u00b6\u0001\u0000\u0000\u0000\u00b5\u00af\u0001\u0000\u0000\u0000"+
		"\u00b5\u00b0\u0001\u0000\u0000\u0000\u00b6\u000f\u0001\u0000\u0000\u0000"+
		"\u00b7\u00b8\u0005\u0001\u0000\u0000\u00b8\u00b9\u0005z\u0000\u0000\u00b9"+
		"\u00ba\u0003\u0016\u000b\u0000\u00ba\u00bb\u0005\u0002\u0000\u0000\u00bb"+
		"\u00c2\u0001\u0000\u0000\u0000\u00bc\u00bd\u0005\u0001\u0000\u0000\u00bd"+
		"\u00be\u0005\u000e\u0000\u0000\u00be\u00bf\u0003 \u0010\u0000\u00bf\u00c0"+
		"\u0005\u0002\u0000\u0000\u00c0\u00c2\u0001\u0000\u0000\u0000\u00c1\u00b7"+
		"\u0001\u0000\u0000\u0000\u00c1\u00bc\u0001\u0000\u0000\u0000\u00c2\u0011"+
		"\u0001\u0000\u0000\u0000\u00c3\u00c4\u0005\u0001\u0000\u0000\u00c4\u00ce"+
		"\u0005}\u0000\u0000\u00c5\u00c7\u0003\n\u0005\u0000\u00c6\u00c5\u0001"+
		"\u0000\u0000\u0000\u00c7\u00ca\u0001\u0000\u0000\u0000\u00c8\u00c6\u0001"+
		"\u0000\u0000\u0000\u00c8\u00c9\u0001\u0000\u0000\u0000\u00c9\u00cf\u0001"+
		"\u0000\u0000\u0000\u00ca\u00c8\u0001\u0000\u0000\u0000\u00cb\u00cc\u0003"+
		"\"\u0011\u0000\u00cc\u00cd\u0003\n\u0005\u0000\u00cd\u00cf\u0001\u0000"+
		"\u0000\u0000\u00ce\u00c8\u0001\u0000\u0000\u0000\u00ce\u00cb\u0001\u0000"+
		"\u0000\u0000\u00cf\u00d0\u0001\u0000\u0000\u0000\u00d0\u00d2\u0005\u0002"+
		"\u0000\u0000\u00d1\u00c3\u0001\u0000\u0000\u0000\u00d2\u00d5\u0001\u0000"+
		"\u0000\u0000\u00d3\u00d1\u0001\u0000\u0000\u0000\u00d3\u00d4\u0001\u0000"+
		"\u0000\u0000\u00d4\u0013\u0001\u0000\u0000\u0000\u00d5\u00d3\u0001\u0000"+
		"\u0000\u0000\u00d6\u00d7\u0005\u0001\u0000\u0000\u00d7\u00db\u0005~\u0000"+
		"\u0000\u00d8\u00da\u0003\n\u0005\u0000\u00d9\u00d8\u0001\u0000\u0000\u0000"+
		"\u00da\u00dd\u0001\u0000\u0000\u0000\u00db\u00d9\u0001\u0000\u0000\u0000"+
		"\u00db\u00dc\u0001\u0000\u0000\u0000\u00dc\u00de\u0001\u0000\u0000\u0000"+
		"\u00dd\u00db\u0001\u0000\u0000\u0000\u00de\u00e0\u0005\u0002\u0000\u0000"+
		"\u00df\u00d6\u0001\u0000\u0000\u0000\u00e0\u00e3\u0001\u0000\u0000\u0000"+
		"\u00e1\u00df\u0001\u0000\u0000\u0000\u00e1\u00e2\u0001\u0000\u0000\u0000"+
		"\u00e2\u0015\u0001\u0000\u0000\u0000\u00e3\u00e1\u0001\u0000\u0000\u0000"+
		"\u00e4\u00e5\u0003\u0012\t\u0000\u00e5\u00e6\u0003\u0014\n\u0000\u00e6"+
		"\u0017\u0001\u0000\u0000\u0000\u00e7\u00e9\u0005\u0003\u0000\u0000\u00e8"+
		"\u00ea\u0005\u0003\u0000\u0000\u00e9\u00e8\u0001\u0000\u0000\u0000\u00e9"+
		"\u00ea\u0001\u0000\u0000\u0000\u00ea\u00eb\u0001\u0000\u0000\u0000\u00eb"+
		"\u00ec\u0003\u0006\u0003\u0000\u00ec\u0019\u0001\u0000\u0000\u0000\u00ed"+
		"\u00ef\u0005\u0003\u0000\u0000\u00ee\u00f0\u0005\u0003\u0000\u0000\u00ef"+
		"\u00ee\u0001\u0000\u0000\u0000\u00ef\u00f0\u0001\u0000\u0000\u0000\u00f0"+
		"\u001b\u0001\u0000\u0000\u0000\u00f1\u00f2\u0005\u0001\u0000\u0000\u00f2"+
		"\u00f3\u0005y\u0000\u0000\u00f3\u00f4\u0003 \u0010\u0000\u00f4\u00f5\u0005"+
		"\u0002\u0000\u0000\u00f5\u001d\u0001\u0000\u0000\u0000\u00f6\u00f7\u0007"+
		"\u0001\u0000\u0000\u00f7\u001f\u0001\u0000\u0000\u0000\u00f8\u00f9\u0007"+
		"\u0002\u0000\u0000\u00f9!\u0001\u0000\u0000\u0000\u00fa\u00fb\u0005\u009b"+
		"\u0000\u0000\u00fb#\u0001\u0000\u0000\u0000\u00fc\u0101\u0003&\u0013\u0000"+
		"\u00fd\u0101\u0003>\u001f\u0000\u00fe\u0101\u0003D\"\u0000\u00ff\u0101"+
		"\u0003(\u0014\u0000\u0100\u00fc\u0001\u0000\u0000\u0000\u0100\u00fd\u0001"+
		"\u0000\u0000\u0000\u0100\u00fe\u0001\u0000\u0000\u0000\u0100\u00ff\u0001"+
		"\u0000\u0000\u0000\u0101%\u0001\u0000\u0000\u0000\u0102\u0146\u0005\u0013"+
		"\u0000\u0000\u0103\u0146\u0005\u000f\u0000\u0000\u0104\u0146\u0005\u0014"+
		"\u0000\u0000\u0105\u0146\u00034\u001a\u0000\u0106\u0107\u0005\u0018\u0000"+
		"\u0000\u0107\u0146\u0003 \u0010\u0000\u0108\u0109\u0005\u0019\u0000\u0000"+
		"\u0109\u0146\u0003 \u0010\u0000\u010a\u010c\u0005\u001a\u0000\u0000\u010b"+
		"\u010d\u0003 \u0010\u0000\u010c\u010b\u0001\u0000\u0000\u0000\u010d\u010e"+
		"\u0001\u0000\u0000\u0000\u010e\u010c\u0001\u0000\u0000\u0000\u010e\u010f"+
		"\u0001\u0000\u0000\u0000\u010f\u0146\u0001\u0000\u0000\u0000\u0110\u0146"+
		"\u0005\u001b\u0000\u0000\u0111\u0112\u0005 \u0000\u0000\u0112\u0146\u0003"+
		" \u0010\u0000\u0113\u0114\u0005\"\u0000\u0000\u0114\u0146\u0003 \u0010"+
		"\u0000\u0115\u0116\u0005)\u0000\u0000\u0116\u0146\u0003 \u0010\u0000\u0117"+
		"\u0118\u0005*\u0000\u0000\u0118\u0146\u0003 \u0010\u0000\u0119\u011a\u0005"+
		"+\u0000\u0000\u011a\u0146\u0003 \u0010\u0000\u011b\u011c\u0005,\u0000"+
		"\u0000\u011c\u0146\u0003 \u0010\u0000\u011d\u011e\u0005-\u0000\u0000\u011e"+
		"\u0146\u0003 \u0010\u0000\u011f\u0121\u00030\u0018\u0000\u0120\u0122\u0003"+
		",\u0016\u0000\u0121\u0120\u0001\u0000\u0000\u0000\u0121\u0122\u0001\u0000"+
		"\u0000\u0000\u0122\u0124\u0001\u0000\u0000\u0000\u0123\u0125\u0003.\u0017"+
		"\u0000\u0124\u0123\u0001\u0000\u0000\u0000\u0124\u0125\u0001\u0000\u0000"+
		"\u0000\u0125\u0146\u0001\u0000\u0000\u0000\u0126\u0128\u00032\u0019\u0000"+
		"\u0127\u0129\u0003,\u0016\u0000\u0128\u0127\u0001\u0000\u0000\u0000\u0128"+
		"\u0129\u0001\u0000\u0000\u0000\u0129\u012b\u0001\u0000\u0000\u0000\u012a"+
		"\u012c\u0003.\u0017\u0000\u012b\u012a\u0001\u0000\u0000\u0000\u012b\u012c"+
		"\u0001\u0000\u0000\u0000\u012c\u0146\u0001\u0000\u0000\u0000\u012d\u0146"+
		"\u0005o\u0000\u0000\u012e\u0146\u0005p\u0000\u0000\u012f\u0146\u0005q"+
		"\u0000\u0000\u0130\u0146\u0005r\u0000\u0000\u0131\u0132\u0005s\u0000\u0000"+
		"\u0132\u0146\u0003 \u0010\u0000\u0133\u0134\u0005\b\u0000\u0000\u0134"+
		"\u0146\u0003\u001e\u000f\u0000\u0135\u0146\u0005\t\u0000\u0000\u0136\u0146"+
		"\u0005\u0010\u0000\u0000\u0137\u0146\u0005\u0011\u0000\u0000\u0138\u0146"+
		"\u0005\u0012\u0000\u0000\u0139\u0146\u0005t\u0000\u0000\u013a\u0146\u0005"+
		"u\u0000\u0000\u013b\u0146\u0005v\u0000\u0000\u013c\u0146\u0005w\u0000"+
		"\u0000\u013d\u0146\u0005x\u0000\u0000\u013e\u0146\u00036\u001b\u0000\u013f"+
		"\u0140\u0005\'\u0000\u0000\u0140\u0146\u0003 \u0010\u0000\u0141\u0142"+
		"\u0005$\u0000\u0000\u0142\u0146\u0003 \u0010\u0000\u0143\u0144\u0005("+
		"\u0000\u0000\u0144\u0146\u0003 \u0010\u0000\u0145\u0102\u0001\u0000\u0000"+
		"\u0000\u0145\u0103\u0001\u0000\u0000\u0000\u0145\u0104\u0001\u0000\u0000"+
		"\u0000\u0145\u0105\u0001\u0000\u0000\u0000\u0145\u0106\u0001\u0000\u0000"+
		"\u0000\u0145\u0108\u0001\u0000\u0000\u0000\u0145\u010a\u0001\u0000\u0000"+
		"\u0000\u0145\u0110\u0001\u0000\u0000\u0000\u0145\u0111\u0001\u0000\u0000"+
		"\u0000\u0145\u0113\u0001\u0000\u0000\u0000\u0145\u0115\u0001\u0000\u0000"+
		"\u0000\u0145\u0117\u0001\u0000\u0000\u0000\u0145\u0119\u0001\u0000\u0000"+
		"\u0000\u0145\u011b\u0001\u0000\u0000\u0000\u0145\u011d\u0001\u0000\u0000"+
		"\u0000\u0145\u011f\u0001\u0000\u0000\u0000\u0145\u0126\u0001\u0000\u0000"+
		"\u0000\u0145\u012d\u0001\u0000\u0000\u0000\u0145\u012e\u0001\u0000\u0000"+
		"\u0000\u0145\u012f\u0001\u0000\u0000\u0000\u0145\u0130\u0001\u0000\u0000"+
		"\u0000\u0145\u0131\u0001\u0000\u0000\u0000\u0145\u0133\u0001\u0000\u0000"+
		"\u0000\u0145\u0135\u0001\u0000\u0000\u0000\u0145\u0136\u0001\u0000\u0000"+
		"\u0000\u0145\u0137\u0001\u0000\u0000\u0000\u0145\u0138\u0001\u0000\u0000"+
		"\u0000\u0145\u0139\u0001\u0000\u0000\u0000\u0145\u013a\u0001\u0000\u0000"+
		"\u0000\u0145\u013b\u0001\u0000\u0000\u0000\u0145\u013c\u0001\u0000\u0000"+
		"\u0000\u0145\u013d\u0001\u0000\u0000\u0000\u0145\u013e\u0001\u0000\u0000"+
		"\u0000\u0145\u013f\u0001\u0000\u0000\u0000\u0145\u0141\u0001\u0000\u0000"+
		"\u0000\u0145\u0143\u0001\u0000\u0000\u0000\u0146\'\u0001\u0000\u0000\u0000"+
		"\u0147\u0148\u0005%\u0000\u0000\u0148\u014c\u0003 \u0010\u0000\u0149\u014b"+
		"\u0003*\u0015\u0000\u014a\u0149\u0001\u0000\u0000\u0000\u014b\u014e\u0001"+
		"\u0000\u0000\u0000\u014c\u014a\u0001\u0000\u0000\u0000\u014c\u014d\u0001"+
		"\u0000\u0000\u0000\u014d)\u0001\u0000\u0000\u0000\u014e\u014c\u0001\u0000"+
		"\u0000\u0000\u014f\u0150\u0005\u0001\u0000\u0000\u0150\u0151\u0005&\u0000"+
		"\u0000\u0151\u0152\u0003 \u0010\u0000\u0152\u0153\u0003 \u0010\u0000\u0153"+
		"\u0154\u0005\u0002\u0000\u0000\u0154+\u0001\u0000\u0000\u0000\u0155\u0156"+
		"\u00051\u0000\u0000\u0156\u0157\u0005\u0003\u0000\u0000\u0157-\u0001\u0000"+
		"\u0000\u0000\u0158\u0159\u00052\u0000\u0000\u0159\u015a\u0005\u0003\u0000"+
		"\u0000\u015a/\u0001\u0000\u0000\u0000\u015b\u015c\u0003\u0004\u0002\u0000"+
		"\u015c\u0160\u0005.\u0000\u0000\u015d\u015e\u00054\u0000\u0000\u015e\u015f"+
		"\u00050\u0000\u0000\u015f\u0161\u00053\u0000\u0000\u0160\u015d\u0001\u0000"+
		"\u0000\u0000\u0160\u0161\u0001\u0000\u0000\u0000\u01611\u0001\u0000\u0000"+
		"\u0000\u0162\u0163\u0003\u0004\u0002\u0000\u0163\u0165\u0005/\u0000\u0000"+
		"\u0164\u0166\u00054\u0000\u0000\u0165\u0164\u0001\u0000\u0000\u0000\u0165"+
		"\u0166\u0001\u0000\u0000\u0000\u01663\u0001\u0000\u0000\u0000\u0167\u0168"+
		"\u0003\u0004\u0002\u0000\u0168\u0169\u0005\u001f\u0000\u0000\u01695\u0001"+
		"\u0000\u0000\u0000\u016a\u016c\u0005!\u0000\u0000\u016b\u016d\u0003 \u0010"+
		"\u0000\u016c\u016b\u0001\u0000\u0000\u0000\u016c\u016d\u0001\u0000\u0000"+
		"\u0000\u016d\u016e\u0001\u0000\u0000\u0000\u016e\u0175\u0003\u001c\u000e"+
		"\u0000\u016f\u0171\u0005#\u0000\u0000\u0170\u0172\u0003 \u0010\u0000\u0171"+
		"\u0170\u0001\u0000\u0000\u0000\u0171\u0172\u0001\u0000\u0000\u0000\u0172"+
		"\u0173\u0001\u0000\u0000\u0000\u0173\u0175\u0003\u001c\u000e\u0000\u0174"+
		"\u016a\u0001\u0000\u0000\u0000\u0174\u016f\u0001\u0000\u0000\u0000\u0175"+
		"7\u0001\u0000\u0000\u0000\u0176\u0177\u0005\u0001\u0000\u0000\u0177\u017b"+
		"\u0005}\u0000\u0000\u0178\u017a\u0003\n\u0005\u0000\u0179\u0178\u0001"+
		"\u0000\u0000\u0000\u017a\u017d\u0001\u0000\u0000\u0000\u017b\u0179\u0001"+
		"\u0000\u0000\u0000\u017b\u017c\u0001\u0000\u0000\u0000\u017c\u017e\u0001"+
		"\u0000\u0000\u0000\u017d\u017b\u0001\u0000\u0000\u0000\u017e\u0180\u0005"+
		"\u0002\u0000\u0000\u017f\u0176\u0001\u0000\u0000\u0000\u0180\u0183\u0001"+
		"\u0000\u0000\u0000\u0181\u017f\u0001\u0000\u0000\u0000\u0181\u0182\u0001"+
		"\u0000\u0000\u0000\u0182\u018f\u0001\u0000\u0000\u0000\u0183\u0181\u0001"+
		"\u0000\u0000\u0000\u0184\u0185\u0005\u0001\u0000\u0000\u0185\u0189\u0005"+
		"~\u0000\u0000\u0186\u0188\u0003\n\u0005\u0000\u0187\u0186\u0001\u0000"+
		"\u0000\u0000\u0188\u018b\u0001\u0000\u0000\u0000\u0189\u0187\u0001\u0000"+
		"\u0000\u0000\u0189\u018a\u0001\u0000\u0000\u0000\u018a\u018c\u0001\u0000"+
		"\u0000\u0000\u018b\u0189\u0001\u0000\u0000\u0000\u018c\u018e\u0005\u0002"+
		"\u0000\u0000\u018d\u0184\u0001\u0000\u0000\u0000\u018e\u0191\u0001\u0000"+
		"\u0000\u0000\u018f\u018d\u0001\u0000\u0000\u0000\u018f\u0190\u0001\u0000"+
		"\u0000\u0000\u01909\u0001\u0000\u0000\u0000\u0191\u018f\u0001\u0000\u0000"+
		"\u0000\u0192\u0193\u0005\u0001\u0000\u0000\u0193\u0197\u0005}\u0000\u0000"+
		"\u0194\u0196\u0003\n\u0005\u0000\u0195\u0194\u0001\u0000\u0000\u0000\u0196"+
		"\u0199\u0001\u0000\u0000\u0000\u0197\u0195\u0001\u0000\u0000\u0000\u0197"+
		"\u0198\u0001\u0000\u0000\u0000\u0198\u019a\u0001\u0000\u0000\u0000\u0199"+
		"\u0197\u0001\u0000\u0000\u0000\u019a\u019c\u0005\u0002\u0000\u0000\u019b"+
		"\u0192\u0001\u0000\u0000\u0000\u019c\u019f\u0001\u0000\u0000\u0000\u019d"+
		"\u019b\u0001\u0000\u0000\u0000\u019d\u019e\u0001\u0000\u0000\u0000\u019e"+
		"\u01a0\u0001\u0000\u0000\u0000\u019f\u019d\u0001\u0000\u0000\u0000\u01a0"+
		"\u01a1\u0003<\u001e\u0000\u01a1;\u0001\u0000\u0000\u0000\u01a2\u01a3\u0005"+
		"\u0001\u0000\u0000\u01a3\u01a7\u0005~\u0000\u0000\u01a4\u01a6\u0003\n"+
		"\u0005\u0000\u01a5\u01a4\u0001\u0000\u0000\u0000\u01a6\u01a9\u0001\u0000"+
		"\u0000\u0000\u01a7\u01a5\u0001\u0000\u0000\u0000\u01a7\u01a8\u0001\u0000"+
		"\u0000\u0000\u01a8\u01aa\u0001\u0000\u0000\u0000\u01a9\u01a7\u0001\u0000"+
		"\u0000\u0000\u01aa\u01ac\u0005\u0002\u0000\u0000\u01ab\u01a2\u0001\u0000"+
		"\u0000\u0000\u01ac\u01af\u0001\u0000\u0000\u0000\u01ad\u01ab\u0001\u0000"+
		"\u0000\u0000\u01ad\u01ae\u0001\u0000\u0000\u0000\u01ae\u01b0\u0001\u0000"+
		"\u0000\u0000\u01af\u01ad\u0001\u0000\u0000\u0000\u01b0\u01b1\u0003$\u0012"+
		"\u0000\u01b1=\u0001\u0000\u0000\u0000\u01b2\u01b4\u0005\u0015\u0000\u0000"+
		"\u01b3\u01b5\u0003\"\u0011\u0000\u01b4\u01b3\u0001\u0000\u0000\u0000\u01b4"+
		"\u01b5\u0001\u0000\u0000\u0000\u01b5\u01b6\u0001\u0000\u0000\u0000\u01b6"+
		"\u01b7\u0003B!\u0000\u01b7\u01b9\u0005\u0017\u0000\u0000\u01b8\u01ba\u0003"+
		"\"\u0011\u0000\u01b9\u01b8\u0001\u0000\u0000\u0000\u01b9\u01ba\u0001\u0000"+
		"\u0000\u0000\u01ba\u01d5\u0001\u0000\u0000\u0000\u01bb\u01bd\u0005\u0016"+
		"\u0000\u0000\u01bc\u01be\u0003\"\u0011\u0000\u01bd\u01bc\u0001\u0000\u0000"+
		"\u0000\u01bd\u01be\u0001\u0000\u0000\u0000\u01be\u01bf\u0001\u0000\u0000"+
		"\u0000\u01bf\u01c0\u0003B!\u0000\u01c0\u01c2\u0005\u0017\u0000\u0000\u01c1"+
		"\u01c3\u0003\"\u0011\u0000\u01c2\u01c1\u0001\u0000\u0000\u0000\u01c2\u01c3"+
		"\u0001\u0000\u0000\u0000\u01c3\u01d5\u0001\u0000\u0000\u0000\u01c4\u01c6"+
		"\u0005\u001c\u0000\u0000\u01c5\u01c7\u0003\"\u0011\u0000\u01c6\u01c5\u0001"+
		"\u0000\u0000\u0000\u01c6\u01c7\u0001\u0000\u0000\u0000\u01c7\u01c8\u0001"+
		"\u0000\u0000\u0000\u01c8\u01ce\u0003B!\u0000\u01c9\u01cb\u0005\u001e\u0000"+
		"\u0000\u01ca\u01cc\u0003\"\u0011\u0000\u01cb\u01ca\u0001\u0000\u0000\u0000"+
		"\u01cb\u01cc\u0001\u0000\u0000\u0000\u01cc\u01cd\u0001\u0000\u0000\u0000"+
		"\u01cd\u01cf\u0003N\'\u0000\u01ce\u01c9\u0001\u0000\u0000\u0000\u01ce"+
		"\u01cf\u0001\u0000\u0000\u0000\u01cf\u01d0\u0001\u0000\u0000\u0000\u01d0"+
		"\u01d2\u0005\u0017\u0000\u0000\u01d1\u01d3\u0003\"\u0011\u0000\u01d2\u01d1"+
		"\u0001\u0000\u0000\u0000\u01d2\u01d3\u0001\u0000\u0000\u0000\u01d3\u01d5"+
		"\u0001\u0000\u0000\u0000\u01d4\u01b2\u0001\u0000\u0000\u0000\u01d4\u01bb"+
		"\u0001\u0000\u0000\u0000\u01d4\u01c4\u0001\u0000\u0000\u0000\u01d5?\u0001"+
		"\u0000\u0000\u0000\u01d6\u01d7\u0005\u0001\u0000\u0000\u01d7\u01d8\u0005"+
		"~\u0000\u0000\u01d8\u01d9\u0003\n\u0005\u0000\u01d9\u01da\u0005\u0002"+
		"\u0000\u0000\u01da\u01dc\u0001\u0000\u0000\u0000\u01db\u01d6\u0001\u0000"+
		"\u0000\u0000\u01db\u01dc\u0001\u0000\u0000\u0000\u01dc\u01e2\u0001\u0000"+
		"\u0000\u0000\u01dd\u01de\u0003\u001c\u000e\u0000\u01de\u01df\u0003\u0016"+
		"\u000b\u0000\u01df\u01e2\u0001\u0000\u0000\u0000\u01e0\u01e2\u0003\u0016"+
		"\u000b\u0000\u01e1\u01db\u0001\u0000\u0000\u0000\u01e1\u01dd\u0001\u0000"+
		"\u0000\u0000\u01e1\u01e0\u0001\u0000\u0000\u0000\u01e2A\u0001\u0000\u0000"+
		"\u0000\u01e3\u01e4\u0003@ \u0000\u01e4\u01e5\u0003N\'\u0000\u01e5C\u0001"+
		"\u0000\u0000\u0000\u01e6\u01e7\u0005\u0001\u0000\u0000\u01e7\u01e8\u0003"+
		"F#\u0000\u01e8\u01e9\u0005\u0002\u0000\u0000\u01e9E\u0001\u0000\u0000"+
		"\u0000\u01ea\u01ee\u0003&\u0013\u0000\u01eb\u01ed\u0003F#\u0000\u01ec"+
		"\u01eb\u0001\u0000\u0000\u0000\u01ed\u01f0\u0001\u0000\u0000\u0000\u01ee"+
		"\u01ec\u0001\u0000\u0000\u0000\u01ee\u01ef\u0001\u0000\u0000\u0000\u01ef"+
		"\u0215\u0001\u0000\u0000\u0000\u01f0\u01ee\u0001\u0000\u0000\u0000\u01f1"+
		"\u01f2\u0005!\u0000\u0000\u01f2\u0215\u0003H$\u0000\u01f3\u01f4\u0005"+
		"#\u0000\u0000\u01f4\u0215\u0003H$\u0000\u01f5\u01f7\u0005\u0015\u0000"+
		"\u0000\u01f6\u01f8\u0003\"\u0011\u0000\u01f7\u01f6\u0001\u0000\u0000\u0000"+
		"\u01f7\u01f8\u0001\u0000\u0000\u0000\u01f8\u01f9\u0001\u0000\u0000\u0000"+
		"\u01f9\u0215\u0003B!\u0000\u01fa\u01fc\u0005\u0016\u0000\u0000\u01fb\u01fd"+
		"\u0003\"\u0011\u0000\u01fc\u01fb\u0001\u0000\u0000\u0000\u01fc\u01fd\u0001"+
		"\u0000\u0000\u0000\u01fd\u01fe\u0001\u0000\u0000\u0000\u01fe\u0215\u0003"+
		"B!\u0000\u01ff\u0201\u0005\u001c\u0000\u0000\u0200\u0202\u0003\"\u0011"+
		"\u0000\u0201\u0200\u0001\u0000\u0000\u0000\u0201\u0202\u0001\u0000\u0000"+
		"\u0000\u0202\u0203\u0001\u0000\u0000\u0000\u0203\u0207\u0003@ \u0000\u0204"+
		"\u0206\u0003D\"\u0000\u0205\u0204\u0001\u0000\u0000\u0000\u0206\u0209"+
		"\u0001\u0000\u0000\u0000\u0207\u0205\u0001\u0000\u0000\u0000\u0207\u0208"+
		"\u0001\u0000\u0000\u0000\u0208\u020a\u0001\u0000\u0000\u0000\u0209\u0207"+
		"\u0001\u0000\u0000\u0000\u020a\u020b\u0005\u0001\u0000\u0000\u020b\u020c"+
		"\u0005\u001d\u0000\u0000\u020c\u0212\u0003N\'\u0000\u020d\u020e\u0005"+
		"\u0001\u0000\u0000\u020e\u020f\u0005\u001e\u0000\u0000\u020f\u0210\u0003"+
		"N\'\u0000\u0210\u0211\u0005\u0002\u0000\u0000\u0211\u0213\u0001\u0000"+
		"\u0000\u0000\u0212\u020d\u0001\u0000\u0000\u0000\u0212\u0213\u0001\u0000"+
		"\u0000\u0000\u0213\u0215\u0001\u0000\u0000\u0000\u0214\u01ea\u0001\u0000"+
		"\u0000\u0000\u0214\u01f1\u0001\u0000\u0000\u0000\u0214\u01f3\u0001\u0000"+
		"\u0000\u0000\u0214\u01f5\u0001\u0000\u0000\u0000\u0214\u01fa\u0001\u0000"+
		"\u0000\u0000\u0214\u01ff\u0001\u0000\u0000\u0000\u0215G\u0001\u0000\u0000"+
		"\u0000\u0216\u0218\u0003\u001c\u000e\u0000\u0217\u0216\u0001\u0000\u0000"+
		"\u0000\u0217\u0218\u0001\u0000\u0000\u0000\u0218\u0219\u0001\u0000\u0000"+
		"\u0000\u0219\u021a\u0003J%\u0000\u021aI\u0001\u0000\u0000\u0000\u021b"+
		"\u021c\u0005\u0001\u0000\u0000\u021c\u0220\u0005}\u0000\u0000\u021d\u021f"+
		"\u0003\n\u0005\u0000\u021e\u021d\u0001\u0000\u0000\u0000\u021f\u0222\u0001"+
		"\u0000\u0000\u0000\u0220\u021e\u0001\u0000\u0000\u0000\u0220\u0221\u0001"+
		"\u0000\u0000\u0000\u0221\u0223\u0001\u0000\u0000\u0000\u0222\u0220\u0001"+
		"\u0000\u0000\u0000\u0223\u0225\u0005\u0002\u0000\u0000\u0224\u021b\u0001"+
		"\u0000\u0000\u0000\u0225\u0228\u0001\u0000\u0000\u0000\u0226\u0224\u0001"+
		"\u0000\u0000\u0000\u0226\u0227\u0001\u0000\u0000\u0000\u0227\u0229\u0001"+
		"\u0000\u0000\u0000\u0228\u0226\u0001\u0000\u0000\u0000\u0229\u022a\u0003"+
		"L&\u0000\u022aK\u0001\u0000\u0000\u0000\u022b\u022c\u0005\u0001\u0000"+
		"\u0000\u022c\u0230\u0005~\u0000\u0000\u022d\u022f\u0003\n\u0005\u0000"+
		"\u022e\u022d\u0001\u0000\u0000\u0000\u022f\u0232\u0001\u0000\u0000\u0000"+
		"\u0230\u022e\u0001\u0000\u0000\u0000\u0230\u0231\u0001\u0000\u0000\u0000"+
		"\u0231\u0233\u0001\u0000\u0000\u0000\u0232\u0230\u0001\u0000\u0000\u0000"+
		"\u0233\u0235\u0005\u0002\u0000\u0000\u0234\u022b\u0001\u0000\u0000\u0000"+
		"\u0235\u0238\u0001\u0000\u0000\u0000\u0236\u0234\u0001\u0000\u0000\u0000"+
		"\u0236\u0237\u0001\u0000\u0000\u0000\u0237\u023c\u0001\u0000\u0000\u0000"+
		"\u0238\u0236\u0001\u0000\u0000\u0000\u0239\u023b\u0003F#\u0000\u023a\u0239"+
		"\u0001\u0000\u0000\u0000\u023b\u023e\u0001\u0000\u0000\u0000\u023c\u023a"+
		"\u0001\u0000\u0000\u0000\u023c\u023d\u0001\u0000\u0000\u0000\u023dM\u0001"+
		"\u0000\u0000\u0000\u023e\u023c\u0001\u0000\u0000\u0000\u023f\u0241\u0003"+
		"$\u0012\u0000\u0240\u023f\u0001\u0000\u0000\u0000\u0241\u0244\u0001\u0000"+
		"\u0000\u0000\u0242\u0240\u0001\u0000\u0000\u0000\u0242\u0243\u0001\u0000"+
		"\u0000\u0000\u0243\u0246\u0001\u0000\u0000\u0000\u0244\u0242\u0001\u0000"+
		"\u0000\u0000\u0245\u0247\u00036\u001b\u0000\u0246\u0245\u0001\u0000\u0000"+
		"\u0000\u0246\u0247\u0001\u0000\u0000\u0000\u0247O\u0001\u0000\u0000\u0000"+
		"\u0248\u0249\u0003N\'\u0000\u0249Q\u0001\u0000\u0000\u0000\u024a\u024b"+
		"\u0005\u0001\u0000\u0000\u024b\u024d\u0005z\u0000\u0000\u024c\u024e\u0003"+
		"\"\u0011\u0000\u024d\u024c\u0001\u0000\u0000\u0000\u024d\u024e\u0001\u0000"+
		"\u0000\u0000\u024e\u024f\u0001\u0000\u0000\u0000\u024f\u0250\u0003T*\u0000"+
		"\u0250\u0251\u0005\u0002\u0000\u0000\u0251S\u0001\u0000\u0000\u0000\u0252"+
		"\u0254\u0003\u001c\u000e\u0000\u0253\u0252\u0001\u0000\u0000\u0000\u0253"+
		"\u0254\u0001\u0000\u0000\u0000\u0254\u0255\u0001\u0000\u0000\u0000\u0255"+
		"\u0260\u0003V+\u0000\u0256\u0258\u0003p8\u0000\u0257\u0259\u0003\u001c"+
		"\u000e\u0000\u0258\u0257\u0001\u0000\u0000\u0000\u0258\u0259\u0001\u0000"+
		"\u0000\u0000\u0259\u025a\u0001\u0000\u0000\u0000\u025a\u025b\u0003\u0016"+
		"\u000b\u0000\u025b\u0260\u0001\u0000\u0000\u0000\u025c\u025d\u0003v;\u0000"+
		"\u025d\u025e\u0003T*\u0000\u025e\u0260\u0001\u0000\u0000\u0000\u025f\u0253"+
		"\u0001\u0000\u0000\u0000\u025f\u0256\u0001\u0000\u0000\u0000\u025f\u025c"+
		"\u0001\u0000\u0000\u0000\u0260U\u0001\u0000\u0000\u0000\u0261\u0262\u0003"+
		"\u0016\u000b\u0000\u0262\u0263\u0003X,\u0000\u0263W\u0001\u0000\u0000"+
		"\u0000\u0264\u0265\u0005\u0001\u0000\u0000\u0265\u026f\u0005\u007f\u0000"+
		"\u0000\u0266\u0268\u0003\n\u0005\u0000\u0267\u0266\u0001\u0000\u0000\u0000"+
		"\u0268\u026b\u0001\u0000\u0000\u0000\u0269\u0267\u0001\u0000\u0000\u0000"+
		"\u0269\u026a\u0001\u0000\u0000\u0000\u026a\u0270\u0001\u0000\u0000\u0000"+
		"\u026b\u0269\u0001\u0000\u0000\u0000\u026c\u026d\u0003\"\u0011\u0000\u026d"+
		"\u026e\u0003\n\u0005\u0000\u026e\u0270\u0001\u0000\u0000\u0000\u026f\u0269"+
		"\u0001\u0000\u0000\u0000\u026f\u026c\u0001\u0000\u0000\u0000\u0270\u0271"+
		"\u0001\u0000\u0000\u0000\u0271\u0273\u0005\u0002\u0000\u0000\u0272\u0264"+
		"\u0001\u0000\u0000\u0000\u0273\u0276\u0001\u0000\u0000\u0000\u0274\u0272"+
		"\u0001\u0000\u0000\u0000\u0274\u0275\u0001\u0000\u0000\u0000\u0275\u0277"+
		"\u0001\u0000\u0000\u0000\u0276\u0274\u0001\u0000\u0000\u0000\u0277\u0278"+
		"\u0003N\'\u0000\u0278Y\u0001\u0000\u0000\u0000\u0279\u027a\u0005\u0001"+
		"\u0000\u0000\u027a\u027b\u0005\u0085\u0000\u0000\u027b\u027c\u0003P(\u0000"+
		"\u027c\u027d\u0005\u0002\u0000\u0000\u027d\u0280\u0001\u0000\u0000\u0000"+
		"\u027e\u0280\u0003F#\u0000\u027f\u0279\u0001\u0000\u0000\u0000\u027f\u027e"+
		"\u0001\u0000\u0000\u0000\u0280[\u0001\u0000\u0000\u0000\u0281\u0282\u0005"+
		"\u0001\u0000\u0000\u0282\u0284\u0005\u0083\u0000\u0000\u0283\u0285\u0003"+
		" \u0010\u0000\u0284\u0283\u0001\u0000\u0000\u0000\u0284\u0285\u0001\u0000"+
		"\u0000\u0000\u0285\u0286\u0001\u0000\u0000\u0000\u0286\u0287\u0005\u0001"+
		"\u0000\u0000\u0287\u0288\u0003$\u0012\u0000\u0288\u028c\u0005\u0002\u0000"+
		"\u0000\u0289\u028b\u0003 \u0010\u0000\u028a\u0289\u0001\u0000\u0000\u0000"+
		"\u028b\u028e\u0001\u0000\u0000\u0000\u028c\u028a\u0001\u0000\u0000\u0000"+
		"\u028c\u028d\u0001\u0000\u0000\u0000\u028d\u028f\u0001\u0000\u0000\u0000"+
		"\u028e\u028c\u0001\u0000\u0000\u0000\u028f\u0290\u0005\u0002\u0000\u0000"+
		"\u0290\u02aa\u0001\u0000\u0000\u0000\u0291\u0292\u0005\u0001\u0000\u0000"+
		"\u0292\u0294\u0005\u0083\u0000\u0000\u0293\u0295\u0003 \u0010\u0000\u0294"+
		"\u0293\u0001\u0000\u0000\u0000\u0294\u0295\u0001\u0000\u0000\u0000\u0295"+
		"\u0296\u0001\u0000\u0000\u0000\u0296\u029a\u0003Z-\u0000\u0297\u0299\u0003"+
		" \u0010\u0000\u0298\u0297\u0001\u0000\u0000\u0000\u0299\u029c\u0001\u0000"+
		"\u0000\u0000\u029a\u0298\u0001\u0000\u0000\u0000\u029a\u029b\u0001\u0000"+
		"\u0000\u0000\u029b\u029d\u0001\u0000\u0000\u0000\u029c\u029a\u0001\u0000"+
		"\u0000\u0000\u029d\u029e\u0005\u0002\u0000\u0000\u029e\u02aa\u0001\u0000"+
		"\u0000\u0000\u029f\u02a0\u0005\u0001\u0000\u0000\u02a0\u02a2\u0005\u0083"+
		"\u0000\u0000\u02a1\u02a3\u0003 \u0010\u0000\u02a2\u02a1\u0001\u0000\u0000"+
		"\u0000\u02a2\u02a3\u0001\u0000\u0000\u0000\u02a3\u02a4\u0001\u0000\u0000"+
		"\u0000\u02a4\u02a5\u0005\u0089\u0000\u0000\u02a5\u02a6\u0005z\u0000\u0000"+
		"\u02a6\u02a7\u0003 \u0010\u0000\u02a7\u02a8\u0005\u0002\u0000\u0000\u02a8"+
		"\u02aa\u0001\u0000\u0000\u0000\u02a9\u0281\u0001\u0000\u0000\u0000\u02a9"+
		"\u0291\u0001\u0000\u0000\u0000\u02a9\u029f\u0001\u0000\u0000\u0000\u02aa"+
		"]\u0001\u0000\u0000\u0000\u02ab\u02ac\u0005\u0001\u0000\u0000\u02ac\u02ae"+
		"\u0005\u0081\u0000\u0000\u02ad\u02af\u0003\"\u0011\u0000\u02ae\u02ad\u0001"+
		"\u0000\u0000\u0000\u02ae\u02af\u0001\u0000\u0000\u0000\u02af\u02b0\u0001"+
		"\u0000\u0000\u0000\u02b0\u02b1\u0003`0\u0000\u02b1\u02b2\u0005\u0002\u0000"+
		"\u0000\u02b2_\u0001\u0000\u0000\u0000\u02b3\u02c6\u0003\u0018\f\u0000"+
		"\u02b4\u02b5\u0003p8\u0000\u02b5\u02b6\u0003\u0018\f\u0000\u02b6\u02c6"+
		"\u0001\u0000\u0000\u0000\u02b7\u02b8\u0003v;\u0000\u02b8\u02b9\u0003`"+
		"0\u0000\u02b9\u02c6\u0001\u0000\u0000\u0000\u02ba\u02bb\u0003\u0006\u0003"+
		"\u0000\u02bb\u02bc\u0005\u0001\u0000\u0000\u02bc\u02c0\u0005\u0083\u0000"+
		"\u0000\u02bd\u02bf\u0003 \u0010\u0000\u02be\u02bd\u0001\u0000\u0000\u0000"+
		"\u02bf\u02c2\u0001\u0000\u0000\u0000\u02c0\u02be\u0001\u0000\u0000\u0000"+
		"\u02c0\u02c1\u0001\u0000\u0000\u0000\u02c1\u02c3\u0001\u0000\u0000\u0000"+
		"\u02c2\u02c0\u0001\u0000\u0000\u0000\u02c3\u02c4\u0005\u0002\u0000\u0000"+
		"\u02c4\u02c6\u0001\u0000\u0000\u0000\u02c5\u02b3\u0001\u0000\u0000\u0000"+
		"\u02c5\u02b4\u0001\u0000\u0000\u0000\u02c5\u02b7\u0001\u0000\u0000\u0000"+
		"\u02c5\u02ba\u0001\u0000\u0000\u0000\u02c6a\u0001\u0000\u0000\u0000\u02c7"+
		"\u02c8\u0005\u0001\u0000\u0000\u02c8\u02ca\u0005\u0084\u0000\u0000\u02c9"+
		"\u02cb\u0003 \u0010\u0000\u02ca\u02c9\u0001\u0000\u0000\u0000\u02ca\u02cb"+
		"\u0001\u0000\u0000\u0000\u02cb\u02cc\u0001\u0000\u0000\u0000\u02cc\u02cd"+
		"\u0005\u0001\u0000\u0000\u02cd\u02ce\u0003$\u0012\u0000\u02ce\u02d2\u0005"+
		"\u0002\u0000\u0000\u02cf\u02d1\u0005\u0006\u0000\u0000\u02d0\u02cf\u0001"+
		"\u0000\u0000\u0000\u02d1\u02d4\u0001\u0000\u0000\u0000\u02d2\u02d0\u0001"+
		"\u0000\u0000\u0000\u02d2\u02d3\u0001\u0000\u0000\u0000\u02d3\u02d5\u0001"+
		"\u0000\u0000\u0000\u02d4\u02d2\u0001\u0000\u0000\u0000\u02d5\u02d6\u0005"+
		"\u0002\u0000\u0000\u02d6\u02e6\u0001\u0000\u0000\u0000\u02d7\u02d8\u0005"+
		"\u0001\u0000\u0000\u02d8\u02da\u0005\u0084\u0000\u0000\u02d9\u02db\u0003"+
		" \u0010\u0000\u02da\u02d9\u0001\u0000\u0000\u0000\u02da\u02db\u0001\u0000"+
		"\u0000\u0000\u02db\u02dc\u0001\u0000\u0000\u0000\u02dc\u02e0\u0003Z-\u0000"+
		"\u02dd\u02df\u0005\u0006\u0000\u0000\u02de\u02dd\u0001\u0000\u0000\u0000"+
		"\u02df\u02e2\u0001\u0000\u0000\u0000\u02e0\u02de\u0001\u0000\u0000\u0000"+
		"\u02e0\u02e1\u0001\u0000\u0000\u0000\u02e1\u02e3\u0001\u0000\u0000\u0000"+
		"\u02e2\u02e0\u0001\u0000\u0000\u0000\u02e3\u02e4\u0005\u0002\u0000\u0000"+
		"\u02e4\u02e6\u0001\u0000\u0000\u0000\u02e5\u02c7\u0001\u0000\u0000\u0000"+
		"\u02e5\u02d7\u0001\u0000\u0000\u0000\u02e6c\u0001\u0000\u0000\u0000\u02e7"+
		"\u02e8\u0005\u0001\u0000\u0000\u02e8\u02ea\u0005\u0082\u0000\u0000\u02e9"+
		"\u02eb\u0003\"\u0011\u0000\u02ea\u02e9\u0001\u0000\u0000\u0000\u02ea\u02eb"+
		"\u0001\u0000\u0000\u0000\u02eb\u02ec\u0001\u0000\u0000\u0000\u02ec\u02ed"+
		"\u0003f3\u0000\u02ed\u02ee\u0005\u0002\u0000\u0000\u02eee\u0001\u0000"+
		"\u0000\u0000\u02ef\u0300\u0003\u001a\r\u0000\u02f0\u02f1\u0003p8\u0000"+
		"\u02f1\u02f2\u0003\u001a\r\u0000\u02f2\u0300\u0001\u0000\u0000\u0000\u02f3"+
		"\u02f4\u0003v;\u0000\u02f4\u02f5\u0003f3\u0000\u02f5\u0300\u0001\u0000"+
		"\u0000\u0000\u02f6\u02f7\u0005\u0001\u0000\u0000\u02f7\u02fb\u0005\u0084"+
		"\u0000\u0000\u02f8\u02fa\u0005\u0006\u0000\u0000\u02f9\u02f8\u0001\u0000"+
		"\u0000\u0000\u02fa\u02fd\u0001\u0000\u0000\u0000\u02fb\u02f9\u0001\u0000"+
		"\u0000\u0000\u02fb\u02fc\u0001\u0000\u0000\u0000\u02fc\u02fe\u0001\u0000"+
		"\u0000\u0000\u02fd\u02fb\u0001\u0000\u0000\u0000\u02fe\u0300\u0005\u0002"+
		"\u0000\u0000\u02ff\u02ef\u0001\u0000\u0000\u0000\u02ff\u02f0\u0001\u0000"+
		"\u0000\u0000\u02ff\u02f3\u0001\u0000\u0000\u0000\u02ff\u02f6\u0001\u0000"+
		"\u0000\u0000\u0300g\u0001\u0000\u0000\u0000\u0301\u0302\u0005\u0001\u0000"+
		"\u0000\u0302\u0304\u0005\u0080\u0000\u0000\u0303\u0305\u0003\"\u0011\u0000"+
		"\u0304\u0303\u0001\u0000\u0000\u0000\u0304\u0305\u0001\u0000\u0000\u0000"+
		"\u0305\u0306\u0001\u0000\u0000\u0000\u0306\u0307\u0003j5\u0000\u0307\u0308"+
		"\u0005\u0002\u0000\u0000\u0308i\u0001\u0000\u0000\u0000\u0309\u030a\u0003"+
		"\u000e\u0007\u0000\u030a\u030b\u0003P(\u0000\u030b\u0313\u0001\u0000\u0000"+
		"\u0000\u030c\u030d\u0003p8\u0000\u030d\u030e\u0003\u000e\u0007\u0000\u030e"+
		"\u0313\u0001\u0000\u0000\u0000\u030f\u0310\u0003v;\u0000\u0310\u0311\u0003"+
		"j5\u0000\u0311\u0313\u0001\u0000\u0000\u0000\u0312\u0309\u0001\u0000\u0000"+
		"\u0000\u0312\u030c\u0001\u0000\u0000\u0000\u0312\u030f\u0001\u0000\u0000"+
		"\u0000\u0313k\u0001\u0000\u0000\u0000\u0314\u0315\u0005\u0001\u0000\u0000"+
		"\u0315\u0317\u0005z\u0000\u0000\u0316\u0318\u0003\"\u0011\u0000\u0317"+
		"\u0316\u0001\u0000\u0000\u0000\u0317\u0318\u0001\u0000\u0000\u0000\u0318"+
		"\u0319\u0001\u0000\u0000\u0000\u0319\u031a\u0003\u001c\u000e\u0000\u031a"+
		"\u031b\u0005\u0002\u0000\u0000\u031b\u033d\u0001\u0000\u0000\u0000\u031c"+
		"\u031d\u0005\u0001\u0000\u0000\u031d\u031f\u0005z\u0000\u0000\u031e\u0320"+
		"\u0003\"\u0011\u0000\u031f\u031e\u0001\u0000\u0000\u0000\u031f\u0320\u0001"+
		"\u0000\u0000\u0000\u0320\u0321\u0001\u0000\u0000\u0000\u0321\u0322\u0003"+
		"\u0016\u000b\u0000\u0322\u0323\u0005\u0002\u0000\u0000\u0323\u033d\u0001"+
		"\u0000\u0000\u0000\u0324\u0325\u0005\u0001\u0000\u0000\u0325\u0327\u0005"+
		"\u0081\u0000\u0000\u0326\u0328\u0003\"\u0011\u0000\u0327\u0326\u0001\u0000"+
		"\u0000\u0000\u0327\u0328\u0001\u0000\u0000\u0000\u0328\u0329\u0001\u0000"+
		"\u0000\u0000\u0329\u032a\u0003\u0018\f\u0000\u032a\u032b\u0005\u0002\u0000"+
		"\u0000\u032b\u033d\u0001\u0000\u0000\u0000\u032c\u032d\u0005\u0001\u0000"+
		"\u0000\u032d\u032f\u0005\u0082\u0000\u0000\u032e\u0330\u0003\"\u0011\u0000"+
		"\u032f\u032e\u0001\u0000\u0000\u0000\u032f\u0330\u0001\u0000\u0000\u0000"+
		"\u0330\u0331\u0001\u0000\u0000\u0000\u0331\u0332\u0003\u001a\r\u0000\u0332"+
		"\u0333\u0005\u0002\u0000\u0000\u0333\u033d\u0001\u0000\u0000\u0000\u0334"+
		"\u0335\u0005\u0001\u0000\u0000\u0335\u0337\u0005\u0080\u0000\u0000\u0336"+
		"\u0338\u0003\"\u0011\u0000\u0337\u0336\u0001\u0000\u0000\u0000\u0337\u0338"+
		"\u0001\u0000\u0000\u0000\u0338\u0339\u0001\u0000\u0000\u0000\u0339\u033a"+
		"\u0003\u000e\u0007\u0000\u033a\u033b\u0005\u0002\u0000\u0000\u033b\u033d"+
		"\u0001\u0000\u0000\u0000\u033c\u0314\u0001\u0000\u0000\u0000\u033c\u031c"+
		"\u0001\u0000\u0000\u0000\u033c\u0324\u0001\u0000\u0000\u0000\u033c\u032c"+
		"\u0001\u0000\u0000\u0000\u033c\u0334\u0001\u0000\u0000\u0000\u033dm\u0001"+
		"\u0000\u0000\u0000\u033e\u033f\u0005\u0001\u0000\u0000\u033f\u0340\u0005"+
		"\u0086\u0000\u0000\u0340\u0341\u0003\u0002\u0001\u0000\u0341\u0342\u0003"+
		"\u0002\u0001\u0000\u0342\u0343\u0003l6\u0000\u0343\u0344\u0005\u0002\u0000"+
		"\u0000\u0344o\u0001\u0000\u0000\u0000\u0345\u0346\u0005\u0001\u0000\u0000"+
		"\u0346\u0347\u0005\u0086\u0000\u0000\u0347\u0348\u0003\u0002\u0001\u0000"+
		"\u0348\u0349\u0003\u0002\u0001\u0000\u0349\u034a\u0005\u0002\u0000\u0000"+
		"\u034aq\u0001\u0000\u0000\u0000\u034b\u034c\u0005\u0001\u0000\u0000\u034c"+
		"\u034d\u0005z\u0000\u0000\u034d\u034e\u0003 \u0010\u0000\u034e\u034f\u0005"+
		"\u0002\u0000\u0000\u034f\u0360\u0001\u0000\u0000\u0000\u0350\u0351\u0005"+
		"\u0001\u0000\u0000\u0351\u0352\u0005\u0081\u0000\u0000\u0352\u0353\u0003"+
		" \u0010\u0000\u0353\u0354\u0005\u0002\u0000\u0000\u0354\u0360\u0001\u0000"+
		"\u0000\u0000\u0355\u0356\u0005\u0001\u0000\u0000\u0356\u0357\u0005\u0082"+
		"\u0000\u0000\u0357\u0358\u0003 \u0010\u0000\u0358\u0359\u0005\u0002\u0000"+
		"\u0000\u0359\u0360\u0001\u0000\u0000\u0000\u035a\u035b\u0005\u0001\u0000"+
		"\u0000\u035b\u035c\u0005\u0080\u0000\u0000\u035c\u035d\u0003 \u0010\u0000"+
		"\u035d\u035e\u0005\u0002\u0000\u0000\u035e\u0360\u0001\u0000\u0000\u0000"+
		"\u035f\u034b\u0001\u0000\u0000\u0000\u035f\u0350\u0001\u0000\u0000\u0000"+
		"\u035f\u0355\u0001\u0000\u0000\u0000\u035f\u035a\u0001\u0000\u0000\u0000"+
		"\u0360s\u0001\u0000\u0000\u0000\u0361\u0362\u0005\u0001\u0000\u0000\u0362"+
		"\u0363\u0005\u0087\u0000\u0000\u0363\u0364\u0003\u0002\u0001\u0000\u0364"+
		"\u0365\u0003r9\u0000\u0365\u0366\u0005\u0002\u0000\u0000\u0366u\u0001"+
		"\u0000\u0000\u0000\u0367\u0368\u0005\u0001\u0000\u0000\u0368\u0369\u0005"+
		"\u0087\u0000\u0000\u0369\u036a\u0003\u0002\u0001\u0000\u036a\u036b\u0005"+
		"\u0002\u0000\u0000\u036bw\u0001\u0000\u0000\u0000\u036c\u036d\u0005\u0001"+
		"\u0000\u0000\u036d\u036f\u0005\u0088\u0000\u0000\u036e\u0370\u0003\"\u0011"+
		"\u0000\u036f\u036e\u0001\u0000\u0000\u0000\u036f\u0370\u0001\u0000\u0000"+
		"\u0000\u0370\u0371\u0001\u0000\u0000\u0000\u0371\u0372\u0003\u001c\u000e"+
		"\u0000\u0372\u0373\u0003\u0016\u000b\u0000\u0373\u0374\u0005\u0002\u0000"+
		"\u0000\u0374y\u0001\u0000\u0000\u0000\u0375\u0376\u0005\u0001\u0000\u0000"+
		"\u0376\u0378\u0005y\u0000\u0000\u0377\u0379\u0003\"\u0011\u0000\u0378"+
		"\u0377\u0001\u0000\u0000\u0000\u0378\u0379\u0001\u0000\u0000\u0000\u0379"+
		"\u037a\u0001\u0000\u0000\u0000\u037a\u037b\u0003\u0010\b\u0000\u037b\u037c"+
		"\u0005\u0002\u0000\u0000\u037c{\u0001\u0000\u0000\u0000\u037d\u037e\u0005"+
		"\u0001\u0000\u0000\u037e\u037f\u0005|\u0000\u0000\u037f\u0380\u0003 \u0010"+
		"\u0000\u0380\u0381\u0005\u0002\u0000\u0000\u0381}\u0001\u0000\u0000\u0000"+
		"\u0382\u038e\u0003z=\u0000\u0383\u038e\u0003h4\u0000\u0384\u038e\u0003"+
		"^/\u0000\u0385\u038e\u0003d2\u0000\u0386\u038e\u0003R)\u0000\u0387\u038e"+
		"\u0003\\.\u0000\u0388\u038e\u0003b1\u0000\u0389\u038e\u0003|>\u0000\u038a"+
		"\u038e\u0003n7\u0000\u038b\u038e\u0003t:\u0000\u038c\u038e\u0003x<\u0000"+
		"\u038d\u0382\u0001\u0000\u0000\u0000\u038d\u0383\u0001\u0000\u0000\u0000"+
		"\u038d\u0384\u0001\u0000\u0000\u0000\u038d\u0385\u0001\u0000\u0000\u0000"+
		"\u038d\u0386\u0001\u0000\u0000\u0000\u038d\u0387\u0001\u0000\u0000\u0000"+
		"\u038d\u0388\u0001\u0000\u0000\u0000\u038d\u0389\u0001\u0000\u0000\u0000"+
		"\u038d\u038a\u0001\u0000\u0000\u0000\u038d\u038b\u0001\u0000\u0000\u0000"+
		"\u038d\u038c\u0001\u0000\u0000\u0000\u038e\u007f\u0001\u0000\u0000\u0000"+
		"\u038f\u0390\u0005\u0001\u0000\u0000\u0390\u0392\u0005\u008a\u0000\u0000"+
		"\u0391\u0393\u0005\u009b\u0000\u0000\u0392\u0391\u0001\u0000\u0000\u0000"+
		"\u0392\u0393\u0001\u0000\u0000\u0000\u0393\u0397\u0001\u0000\u0000\u0000"+
		"\u0394\u0396\u0003~?\u0000\u0395\u0394\u0001\u0000\u0000\u0000\u0396\u0399"+
		"\u0001\u0000\u0000\u0000\u0397\u0395\u0001\u0000\u0000\u0000\u0397\u0398"+
		"\u0001\u0000\u0000\u0000\u0398\u039a\u0001\u0000\u0000\u0000\u0399\u0397"+
		"\u0001\u0000\u0000\u0000\u039a\u039b\u0005\u0002\u0000\u0000\u039b\u0081"+
		"\u0001\u0000\u0000\u0000\u039c\u03ab\u0003\u0080@\u0000\u039d\u039e\u0005"+
		"\u0001\u0000\u0000\u039e\u03a0\u0005\u008a\u0000\u0000\u039f\u03a1\u0005"+
		"\u009b\u0000\u0000\u03a0\u039f\u0001\u0000\u0000\u0000\u03a0\u03a1\u0001"+
		"\u0000\u0000\u0000\u03a1\u03a2\u0001\u0000\u0000\u0000\u03a2\u03a6\u0007"+
		"\u0003\u0000\u0000\u03a3\u03a5\u0005\u0006\u0000\u0000\u03a4\u03a3\u0001"+
		"\u0000\u0000\u0000\u03a5\u03a8\u0001\u0000\u0000\u0000\u03a6\u03a4\u0001"+
		"\u0000\u0000\u0000\u03a6\u03a7\u0001\u0000\u0000\u0000\u03a7\u03a9\u0001"+
		"\u0000\u0000\u0000\u03a8\u03a6\u0001\u0000\u0000\u0000\u03a9\u03ab\u0005"+
		"\u0002\u0000\u0000\u03aa\u039c\u0001\u0000\u0000\u0000\u03aa\u039d\u0001"+
		"\u0000\u0000\u0000\u03ab\u0083\u0001\u0000\u0000\u0000\u03ac\u03ad\u0005"+
		"\u0001\u0000\u0000\u03ad\u03af\u0005\u008f\u0000\u0000\u03ae\u03b0\u0005"+
		"\u009b\u0000\u0000\u03af\u03ae\u0001\u0000\u0000\u0000\u03af\u03b0\u0001"+
		"\u0000\u0000\u0000\u03b0\u03b1\u0001\u0000\u0000\u0000\u03b1\u03b2\u0003"+
		"\u0002\u0001\u0000\u03b2\u03b3\u0003\u008eG\u0000\u03b3\u03b4\u0005\u0002"+
		"\u0000\u0000\u03b4\u03be\u0001\u0000\u0000\u0000\u03b5\u03b6\u0005\u0001"+
		"\u0000\u0000\u03b6\u03b8\u0005\u0090\u0000\u0000\u03b7\u03b9\u0005\u009b"+
		"\u0000\u0000\u03b8\u03b7\u0001\u0000\u0000\u0000\u03b8\u03b9\u0001\u0000"+
		"\u0000\u0000\u03b9\u03ba\u0001\u0000\u0000\u0000\u03ba\u03bb\u0003\u0002"+
		"\u0001\u0000\u03bb\u03bc\u0005\u0002\u0000\u0000\u03bc\u03be\u0001\u0000"+
		"\u0000\u0000\u03bd\u03ac\u0001\u0000\u0000\u0000\u03bd\u03b5\u0001\u0000"+
		"\u0000\u0000\u03be\u0085\u0001\u0000\u0000\u0000\u03bf\u03c0\u0005\u0001"+
		"\u0000\u0000\u03c0\u03c1\u0005\u0091\u0000\u0000\u03c1\u03c2\u0003\u0082"+
		"A\u0000\u03c2\u03c3\u0005\u0006\u0000\u0000\u03c3\u03c4\u0005\u0002\u0000"+
		"\u0000\u03c4\u03f4\u0001\u0000\u0000\u0000\u03c5\u03c6\u0005\u0001\u0000"+
		"\u0000\u03c6\u03c7\u0005\u0092\u0000\u0000\u03c7\u03c8\u0003\u0082A\u0000"+
		"\u03c8\u03c9\u0005\u0006\u0000\u0000\u03c9\u03ca\u0005\u0002\u0000\u0000"+
		"\u03ca\u03f4\u0001\u0000\u0000\u0000\u03cb\u03cc\u0005\u0001\u0000\u0000"+
		"\u03cc\u03cd\u0005\u0093\u0000\u0000\u03cd\u03ce\u0003\u0082A\u0000\u03ce"+
		"\u03cf\u0005\u0006\u0000\u0000\u03cf\u03d0\u0005\u0002\u0000\u0000\u03d0"+
		"\u03f4\u0001\u0000\u0000\u0000\u03d1\u03d2\u0005\u0001\u0000\u0000\u03d2"+
		"\u03d3\u0005\u0097\u0000\u0000\u03d3\u03d4\u0003\u0082A\u0000\u03d4\u03d5"+
		"\u0005\u0006\u0000\u0000\u03d5\u03d6\u0005\u0002\u0000\u0000\u03d6\u03f4"+
		"\u0001\u0000\u0000\u0000\u03d7\u03d8\u0005\u0001\u0000\u0000\u03d8\u03d9"+
		"\u0005\u0094\u0000\u0000\u03d9\u03da\u0003\u0084B\u0000\u03da\u03db\u0003"+
		"\u008eG\u0000\u03db\u03dc\u0005\u0002\u0000\u0000\u03dc\u03f4\u0001\u0000"+
		"\u0000\u0000\u03dd\u03de\u0005\u0001\u0000\u0000\u03de\u03df\u0005\u0095"+
		"\u0000\u0000\u03df\u03e0\u0003\u0084B\u0000\u03e0\u03e1\u0005\u0002\u0000"+
		"\u0000\u03e1\u03f4\u0001\u0000\u0000\u0000\u03e2\u03e3\u0005\u0001\u0000"+
		"\u0000\u03e3\u03e4\u0005\u0096\u0000\u0000\u03e4\u03e5\u0003\u0084B\u0000"+
		"\u03e5\u03e6\u0005\u0002\u0000\u0000\u03e6\u03f4\u0001\u0000\u0000\u0000"+
		"\u03e7\u03e8\u0005\u0001\u0000\u0000\u03e8\u03e9\u0005\u0097\u0000\u0000"+
		"\u03e9\u03ea\u0003\u0084B\u0000\u03ea\u03eb\u0005\u0006\u0000\u0000\u03eb"+
		"\u03ec\u0005\u0002\u0000\u0000\u03ec\u03f4\u0001\u0000\u0000\u0000\u03ed"+
		"\u03ee\u0005\u0001\u0000\u0000\u03ee\u03ef\u0005\u0098\u0000\u0000\u03ef"+
		"\u03f0\u0003\u0084B\u0000\u03f0\u03f1\u0005\u0006\u0000\u0000\u03f1\u03f2"+
		"\u0005\u0002\u0000\u0000\u03f2\u03f4\u0001\u0000\u0000\u0000\u03f3\u03bf"+
		"\u0001\u0000\u0000\u0000\u03f3\u03c5\u0001\u0000\u0000\u0000\u03f3\u03cb"+
		"\u0001\u0000\u0000\u0000\u03f3\u03d1\u0001\u0000\u0000\u0000\u03f3\u03d7"+
		"\u0001\u0000\u0000\u0000\u03f3\u03dd\u0001\u0000\u0000\u0000\u03f3\u03e2"+
		"\u0001\u0000\u0000\u0000\u03f3\u03e7\u0001\u0000\u0000\u0000\u03f3\u03ed"+
		"\u0001\u0000\u0000\u0000\u03f4\u0087\u0001\u0000\u0000\u0000\u03f5\u0402"+
		"\u0003\u0084B\u0000\u03f6\u0402\u0003\u0086C\u0000\u03f7\u0402\u0003\u0082"+
		"A\u0000\u03f8\u03f9\u0005\u0001\u0000\u0000\u03f9\u03fa\u0005\u008e\u0000"+
		"\u0000\u03fa\u03fc\u0003\u0002\u0001\u0000\u03fb\u03fd\u0005\u009b\u0000"+
		"\u0000\u03fc\u03fb\u0001\u0000\u0000\u0000\u03fc\u03fd\u0001\u0000\u0000"+
		"\u0000\u03fd\u03fe\u0001\u0000\u0000\u0000\u03fe\u03ff\u0005\u0002\u0000"+
		"\u0000\u03ff\u0402\u0001\u0000\u0000\u0000\u0400\u0402\u0003\u008aE\u0000"+
		"\u0401\u03f5\u0001\u0000\u0000\u0000\u0401\u03f6\u0001\u0000\u0000\u0000"+
		"\u0401\u03f7\u0001\u0000\u0000\u0000\u0401\u03f8\u0001\u0000\u0000\u0000"+
		"\u0401\u0400\u0001\u0000\u0000\u0000\u0402\u0089\u0001\u0000\u0000\u0000"+
		"\u0403\u0404\u0005\u0001\u0000\u0000\u0404\u0406\u0005\u008d\u0000\u0000"+
		"\u0405\u0407\u0005\u009b\u0000\u0000\u0406\u0405\u0001\u0000\u0000\u0000"+
		"\u0406\u0407\u0001\u0000\u0000\u0000\u0407\u040b\u0001\u0000\u0000\u0000"+
		"\u0408\u040a\u0003\u0088D\u0000\u0409\u0408\u0001\u0000\u0000\u0000\u040a"+
		"\u040d\u0001\u0000\u0000\u0000\u040b\u0409\u0001\u0000\u0000\u0000\u040b"+
		"\u040c\u0001\u0000\u0000\u0000\u040c\u040e\u0001\u0000\u0000\u0000\u040d"+
		"\u040b\u0001\u0000\u0000\u0000\u040e\u0424\u0005\u0002\u0000\u0000\u040f"+
		"\u0410\u0005\u0001\u0000\u0000\u0410\u0412\u0005\u0099\u0000\u0000\u0411"+
		"\u0413\u0005\u009b\u0000\u0000\u0412\u0411\u0001\u0000\u0000\u0000\u0412"+
		"\u0413\u0001\u0000\u0000\u0000\u0413\u0414\u0001\u0000\u0000\u0000\u0414"+
		"\u0415\u0005\u0006\u0000\u0000\u0415\u0424\u0005\u0002\u0000\u0000\u0416"+
		"\u0417\u0005\u0001\u0000\u0000\u0417\u0419\u0005\u009a\u0000\u0000\u0418"+
		"\u041a\u0005\u009b\u0000\u0000\u0419\u0418\u0001\u0000\u0000\u0000\u0419"+
		"\u041a\u0001\u0000\u0000\u0000\u041a\u041b\u0001\u0000\u0000\u0000\u041b"+
		"\u041c\u0005\u0006\u0000\u0000\u041c\u0424\u0005\u0002\u0000\u0000\u041d"+
		"\u041e\u0005\u0001\u0000\u0000\u041e\u0420\u0005\u009a\u0000\u0000\u041f"+
		"\u0421\u0005\u009b\u0000\u0000\u0420\u041f\u0001\u0000\u0000\u0000\u0420"+
		"\u0421\u0001\u0000\u0000\u0000\u0421\u0422\u0001\u0000\u0000\u0000\u0422"+
		"\u0424\u0005\u0002\u0000\u0000\u0423\u0403\u0001\u0000\u0000\u0000\u0423"+
		"\u040f\u0001\u0000\u0000\u0000\u0423\u0416\u0001\u0000\u0000\u0000\u0423"+
		"\u041d\u0001\u0000\u0000\u0000\u0424\u008b\u0001\u0000\u0000\u0000\u0425"+
		"\u0426\u0005\u0001\u0000\u0000\u0426\u0427\u0005\b\u0000\u0000\u0427\u0428"+
		"\u0003\u001e\u000f\u0000\u0428\u0429\u0005\u0002\u0000\u0000\u0429\u008d"+
		"\u0001\u0000\u0000\u0000\u042a\u042c\u0003\u008cF\u0000\u042b\u042a\u0001"+
		"\u0000\u0000\u0000\u042c\u042f\u0001\u0000\u0000\u0000\u042d\u042b\u0001"+
		"\u0000\u0000\u0000\u042d\u042e\u0001\u0000\u0000\u0000\u042e\u008f\u0001"+
		"\u0000\u0000\u0000\u042f\u042d\u0001\u0000\u0000\u0000\u0430\u0432\u0003"+
		"\u0088D\u0000\u0431\u0430\u0001\u0000\u0000\u0000\u0432\u0435\u0001\u0000"+
		"\u0000\u0000\u0433\u0431\u0001\u0000\u0000\u0000\u0433\u0434\u0001\u0000"+
		"\u0000\u0000\u0434\u0436\u0001\u0000\u0000\u0000\u0435\u0433\u0001\u0000"+
		"\u0000\u0000\u0436\u043f\u0005\u0000\u0000\u0001\u0437\u0439\u0003~?\u0000"+
		"\u0438\u0437\u0001\u0000\u0000\u0000\u0439\u043a\u0001\u0000\u0000\u0000"+
		"\u043a\u0438\u0001\u0000\u0000\u0000\u043a\u043b\u0001\u0000\u0000\u0000"+
		"\u043b\u043c\u0001\u0000\u0000\u0000\u043c\u043d\u0005\u0000\u0000\u0001"+
		"\u043d\u043f\u0001\u0000\u0000\u0000\u043e\u0433\u0001\u0000\u0000\u0000"+
		"\u043e\u0438\u0001\u0000\u0000\u0000\u043f\u0091\u0001\u0000\u0000\u0000"+
		"\u0440\u0441\u0003\u0080@\u0000\u0441\u0442\u0005\u0000\u0000\u0001\u0442"+
		"\u044b\u0001\u0000\u0000\u0000\u0443\u0445\u0003~?\u0000\u0444\u0443\u0001"+
		"\u0000\u0000\u0000\u0445\u0448\u0001\u0000\u0000\u0000\u0446\u0444\u0001"+
		"\u0000\u0000\u0000\u0446\u0447\u0001\u0000\u0000\u0000\u0447\u0449\u0001"+
		"\u0000\u0000\u0000\u0448\u0446\u0001\u0000\u0000\u0000\u0449\u044b\u0005"+
		"\u0000\u0000\u0001\u044a\u0440\u0001\u0000\u0000\u0000\u044a\u0446\u0001"+
		"\u0000\u0000\u0000\u044b\u0093\u0001\u0000\u0000\u0000w\u00a1\u00a8\u00ad"+
		"\u00b5\u00c1\u00c8\u00ce\u00d3\u00db\u00e1\u00e9\u00ef\u0100\u010e\u0121"+
		"\u0124\u0128\u012b\u0145\u014c\u0160\u0165\u016c\u0171\u0174\u017b\u0181"+
		"\u0189\u018f\u0197\u019d\u01a7\u01ad\u01b4\u01b9\u01bd\u01c2\u01c6\u01cb"+
		"\u01ce\u01d2\u01d4\u01db\u01e1\u01ee\u01f7\u01fc\u0201\u0207\u0212\u0214"+
		"\u0217\u0220\u0226\u0230\u0236\u023c\u0242\u0246\u024d\u0253\u0258\u025f"+
		"\u0269\u026f\u0274\u027f\u0284\u028c\u0294\u029a\u02a2\u02a9\u02ae\u02c0"+
		"\u02c5\u02ca\u02d2\u02da\u02e0\u02e5\u02ea\u02fb\u02ff\u0304\u0312\u0317"+
		"\u031f\u0327\u032f\u0337\u033c\u035f\u036f\u0378\u038d\u0392\u0397\u03a0"+
		"\u03a6\u03aa\u03af\u03b8\u03bd\u03f3\u03fc\u0401\u0406\u040b\u0412\u0419"+
		"\u0420\u0423\u042d\u0433\u043a\u043e\u0446\u044a";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}