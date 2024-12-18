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
		REFFUNC=36, CALLREF=37, RESUME=38, ON=39, CONTNEW=40, CONTBIND=41, SUSPEND=42, 
		TRY=43, CATCH=44, THROW=45, RESUME0=46, LOCAL_GET=47, LOCAL_SET=48, LOCAL_TEE=49, 
		GLOBAL_GET=50, GLOBAL_SET=51, LOAD=52, STORE=53, UNDERSCORE=54, OFFSET_EQ=55, 
		ALIGN_EQ=56, SIGN_POSTFIX=57, MEM_SIZE=58, I32=59, I64=60, F32=61, F64=62, 
		IXX=63, FXX=64, OP_EQZ=65, OP_EQ=66, OP_NE=67, OP_LT=68, OP_LTS=69, OP_LTU=70, 
		OP_LE=71, OP_LES=72, OP_LEU=73, OP_GT=74, OP_GTS=75, OP_GTU=76, OP_GE=77, 
		OP_GES=78, OP_GEU=79, OP_CLZ=80, OP_CTZ=81, OP_POPCNT=82, OP_NEG=83, OP_ABS=84, 
		OP_SQRT=85, OP_CEIL=86, OP_FLOOR=87, OP_TRUNC=88, OP_NEAREST=89, OP_ADD=90, 
		OP_SUB=91, OP_MUL=92, OP_DIV=93, OP_DIV_S=94, OP_DIV_U=95, OP_REM_S=96, 
		OP_REM_U=97, OP_AND=98, OP_OR=99, OP_XOR=100, OP_SHL=101, OP_SHR_S=102, 
		OP_SHR_U=103, OP_ROTL=104, OP_ROTR=105, OP_MIN=106, OP_MAX=107, OP_COPYSIGN=108, 
		OP_WRAP=109, OP_TRUNC_=110, OP_TRUNC_SAT=111, OP_CONVERT=112, OP_EXTEND=113, 
		OP_DEMOTE=114, OP_PROMOTE=115, OP_REINTER=116, MEMORY_SIZE=117, MEMORY_GROW=118, 
		MEMORY_FILL=119, MEMORY_COPY=120, MEMORY_INIT=121, TEST=122, COMPARE=123, 
		UNARY=124, BINARY=125, CONVERT=126, TYPE=127, FUNC=128, EXTERN=129, START_=130, 
		PARAM=131, RESULT=132, LOCAL=133, GLOBAL=134, TABLE=135, MEMORY=136, ELEM=137, 
		DATA=138, OFFSET=139, IMPORT=140, EXPORT=141, TAG=142, DECLARE=143, MODULE=144, 
		BIN=145, QUOTE=146, DEFINITION=147, INSTANCE=148, SCRIPT=149, REGISTER=150, 
		INVOKE=151, GET=152, ASSERT_MALFORMED=153, ASSERT_INVALID=154, ASSERT_UNLINKABLE=155, 
		ASSERT_RETURN=156, ASSERT_RETURN_CANONICAL_NAN=157, ASSERT_RETURN_ARITHMETIC_NAN=158, 
		ASSERT_TRAP=159, ASSERT_EXHAUSTION=160, INPUT=161, OUTPUT=162, VAR=163, 
		V128=164, SPACE=165, COMMENT=166;
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
		RULE_assertion = 67, RULE_cmd = 68, RULE_instance = 69, RULE_meta = 70, 
		RULE_wconst = 71, RULE_constList = 72, RULE_script = 73, RULE_module = 74;
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
			"module_", "scriptModule", "action_", "assertion", "cmd", "instance", 
			"meta", "wconst", "constList", "script", "module"
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
			"'ref.func'", "'call_ref'", "'resume'", "'on'", "'cont.new'", "'cont.bind'", 
			"'suspend'", "'try'", "'catch'", "'throw'", "'resume0'", "'local.get'", 
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
			"'tag'", "'declare'", "'module'", "'binary'", "'quote'", "'definition'", 
			"'instance'", "'script'", "'register'", "'invoke'", "'get'", "'assert_malformed'", 
			"'assert_invalid'", "'assert_unlinkable'", "'assert_return'", "'assert_return_canonical_nan'", 
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
			"CALLREF", "RESUME", "ON", "CONTNEW", "CONTBIND", "SUSPEND", "TRY", "CATCH", 
			"THROW", "RESUME0", "LOCAL_GET", "LOCAL_SET", "LOCAL_TEE", "GLOBAL_GET", 
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
			"TABLE", "MEMORY", "ELEM", "DATA", "OFFSET", "IMPORT", "EXPORT", "TAG", 
			"DECLARE", "MODULE", "BIN", "QUOTE", "DEFINITION", "INSTANCE", "SCRIPT", 
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
			setState(150);
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
			setState(152);
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
			setState(154);
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
			setState(163);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FUNCREF:
				enterOuterAlt(_localctx, 1);
				{
				setState(156);
				match(FUNCREF);
				}
				break;
			case EXTERNREF:
				enterOuterAlt(_localctx, 2);
				{
				setState(157);
				match(EXTERNREF);
				}
				break;
			case LPAR:
				enterOuterAlt(_localctx, 3);
				{
				setState(158);
				match(LPAR);
				setState(159);
				match(REF);
				setState(160);
				idx();
				setState(161);
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
			setState(165);
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
			setState(170);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VALUE_TYPE:
				enterOuterAlt(_localctx, 1);
				{
				setState(167);
				numType();
				}
				break;
			case V128:
				enterOuterAlt(_localctx, 2);
				{
				setState(168);
				vecType();
				}
				break;
			case LPAR:
			case FUNCREF:
			case EXTERNREF:
				enterOuterAlt(_localctx, 3);
				{
				setState(169);
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
			setState(175);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FUNC:
				enterOuterAlt(_localctx, 1);
				{
				setState(172);
				match(FUNC);
				}
				break;
			case EXTERN:
				enterOuterAlt(_localctx, 2);
				{
				setState(173);
				match(EXTERN);
				}
				break;
			case EOF:
			case LPAR:
				enterOuterAlt(_localctx, 3);
				{
				setState(174);
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
			setState(183);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(177);
				valType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(178);
				match(LPAR);
				setState(179);
				match(MUT);
				setState(180);
				valType();
				setState(181);
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
			setState(195);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(185);
				match(LPAR);
				setState(186);
				match(FUNC);
				setState(187);
				funcType();
				setState(188);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(190);
				match(LPAR);
				setState(191);
				match(CONT);
				setState(192);
				idx();
				setState(193);
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
			setState(213);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(197);
					match(LPAR);
					setState(198);
					match(PARAM);
					setState(208);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case LPAR:
					case RPAR:
					case VALUE_TYPE:
					case FUNCREF:
					case EXTERNREF:
					case V128:
						{
						setState(202);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3202L) != 0) || _la==V128) {
							{
							{
							setState(199);
							valType();
							}
							}
							setState(204);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						}
						break;
					case VAR:
						{
						setState(205);
						bindVar();
						setState(206);
						valType();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(210);
					match(RPAR);
					}
					} 
				}
				setState(215);
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
			setState(227);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(216);
					match(LPAR);
					setState(217);
					match(RESULT);
					setState(221);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3202L) != 0) || _la==V128) {
						{
						{
						setState(218);
						valType();
						}
						}
						setState(223);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(224);
					match(RPAR);
					}
					} 
				}
				setState(229);
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
			setState(230);
			funcParamType();
			setState(231);
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
			setState(233);
			match(NAT);
			setState(235);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAT) {
				{
				setState(234);
				match(NAT);
				}
			}

			setState(237);
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
			setState(239);
			match(NAT);
			setState(241);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAT) {
				{
				setState(240);
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
			setState(243);
			match(LPAR);
			setState(244);
			match(TYPE);
			setState(245);
			idx();
			setState(246);
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
			setState(248);
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
			setState(250);
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
			setState(252);
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
			setState(258);
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
			case CALLREF:
			case CONTNEW:
			case CONTBIND:
			case SUSPEND:
			case THROW:
			case RESUME0:
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
				setState(254);
				plainInstr();
				}
				break;
			case BLOCK:
			case LOOP:
			case IF:
			case TRY:
				enterOuterAlt(_localctx, 2);
				{
				setState(255);
				blockInstr();
				}
				break;
			case LPAR:
				enterOuterAlt(_localctx, 3);
				{
				setState(256);
				foldedInstr();
				}
				break;
			case RESUME:
				enterOuterAlt(_localctx, 4);
				{
				setState(257);
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
		public TerminalNode CONTBIND() { return getToken(WatParser.CONTBIND, 0); }
		public TerminalNode CALLREF() { return getToken(WatParser.CALLREF, 0); }
		public TerminalNode RESUME0() { return getToken(WatParser.RESUME0, 0); }
		public TerminalNode THROW() { return getToken(WatParser.THROW, 0); }
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
			setState(335);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(260);
				match(UNREACHABLE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(261);
				match(NOP);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(262);
				match(DROP);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(263);
				selectInstr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(264);
				match(BR);
				setState(265);
				idx();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(266);
				match(BR_IF);
				setState(267);
				idx();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(268);
				match(BR_TABLE);
				setState(270); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(269);
						idx();
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(272); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(274);
				match(RETURN);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(275);
				match(CALL);
				setState(276);
				idx();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(277);
				match(RETURN_CALL);
				setState(278);
				idx();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(279);
				match(LOCAL_GET);
				setState(280);
				idx();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(281);
				match(LOCAL_SET);
				setState(282);
				idx();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(283);
				match(LOCAL_TEE);
				setState(284);
				idx();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(285);
				match(GLOBAL_GET);
				setState(286);
				idx();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(287);
				match(GLOBAL_SET);
				setState(288);
				idx();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(289);
				load();
				setState(291);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==OFFSET_EQ) {
					{
					setState(290);
					offsetEq();
					}
				}

				setState(294);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ALIGN_EQ) {
					{
					setState(293);
					alignEq();
					}
				}

				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(296);
				store();
				setState(298);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==OFFSET_EQ) {
					{
					setState(297);
					offsetEq();
					}
				}

				setState(301);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ALIGN_EQ) {
					{
					setState(300);
					alignEq();
					}
				}

				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(303);
				match(MEMORY_SIZE);
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(304);
				match(MEMORY_GROW);
				}
				break;
			case 20:
				enterOuterAlt(_localctx, 20);
				{
				setState(305);
				match(MEMORY_FILL);
				}
				break;
			case 21:
				enterOuterAlt(_localctx, 21);
				{
				setState(306);
				match(MEMORY_COPY);
				}
				break;
			case 22:
				enterOuterAlt(_localctx, 22);
				{
				setState(307);
				match(MEMORY_INIT);
				setState(308);
				idx();
				}
				break;
			case 23:
				enterOuterAlt(_localctx, 23);
				{
				setState(309);
				match(CONST);
				setState(310);
				literal();
				}
				break;
			case 24:
				enterOuterAlt(_localctx, 24);
				{
				setState(311);
				match(SYMBOLIC);
				}
				break;
			case 25:
				enterOuterAlt(_localctx, 25);
				{
				setState(312);
				match(SYM_ASSERT);
				}
				break;
			case 26:
				enterOuterAlt(_localctx, 26);
				{
				setState(313);
				match(ALLOC);
				}
				break;
			case 27:
				enterOuterAlt(_localctx, 27);
				{
				setState(314);
				match(FREE);
				}
				break;
			case 28:
				enterOuterAlt(_localctx, 28);
				{
				setState(315);
				match(TEST);
				}
				break;
			case 29:
				enterOuterAlt(_localctx, 29);
				{
				setState(316);
				match(COMPARE);
				}
				break;
			case 30:
				enterOuterAlt(_localctx, 30);
				{
				setState(317);
				match(UNARY);
				}
				break;
			case 31:
				enterOuterAlt(_localctx, 31);
				{
				setState(318);
				match(BINARY);
				}
				break;
			case 32:
				enterOuterAlt(_localctx, 32);
				{
				setState(319);
				match(CONVERT);
				}
				break;
			case 33:
				enterOuterAlt(_localctx, 33);
				{
				setState(320);
				callIndirectInstr();
				}
				break;
			case 34:
				enterOuterAlt(_localctx, 34);
				{
				setState(321);
				match(CONTNEW);
				setState(322);
				idx();
				}
				break;
			case 35:
				enterOuterAlt(_localctx, 35);
				{
				setState(323);
				match(REFFUNC);
				setState(324);
				idx();
				}
				break;
			case 36:
				enterOuterAlt(_localctx, 36);
				{
				setState(325);
				match(SUSPEND);
				setState(326);
				idx();
				}
				break;
			case 37:
				enterOuterAlt(_localctx, 37);
				{
				setState(327);
				match(CONTBIND);
				setState(328);
				idx();
				setState(329);
				idx();
				}
				break;
			case 38:
				enterOuterAlt(_localctx, 38);
				{
				setState(331);
				match(CALLREF);
				setState(332);
				idx();
				}
				break;
			case 39:
				enterOuterAlt(_localctx, 39);
				{
				setState(333);
				match(RESUME0);
				}
				break;
			case 40:
				enterOuterAlt(_localctx, 40);
				{
				setState(334);
				match(THROW);
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
			setState(337);
			match(RESUME);
			setState(338);
			idx();
			setState(342);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(339);
					handlerInstr();
					}
					} 
				}
				setState(344);
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
			setState(345);
			match(LPAR);
			setState(346);
			match(ON);
			setState(347);
			idx();
			setState(348);
			idx();
			setState(349);
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
			setState(351);
			match(OFFSET_EQ);
			setState(352);
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
			setState(354);
			match(ALIGN_EQ);
			setState(355);
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
			setState(357);
			numType();
			setState(358);
			match(LOAD);
			setState(362);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MEM_SIZE) {
				{
				setState(359);
				match(MEM_SIZE);
				setState(360);
				match(UNDERSCORE);
				setState(361);
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
			setState(364);
			numType();
			setState(365);
			match(STORE);
			setState(367);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MEM_SIZE) {
				{
				setState(366);
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
			setState(369);
			numType();
			setState(370);
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
			setState(382);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CALL_INDIRECT:
				enterOuterAlt(_localctx, 1);
				{
				setState(372);
				match(CALL_INDIRECT);
				setState(374);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAT || _la==VAR) {
					{
					setState(373);
					idx();
					}
				}

				setState(376);
				typeUse();
				}
				break;
			case RETURN_CALL_INDIRECT:
				enterOuterAlt(_localctx, 2);
				{
				setState(377);
				match(RETURN_CALL_INDIRECT);
				setState(379);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAT || _la==VAR) {
					{
					setState(378);
					idx();
					}
				}

				setState(381);
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
			setState(395);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(384);
					match(LPAR);
					setState(385);
					match(PARAM);
					setState(389);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3202L) != 0) || _la==V128) {
						{
						{
						setState(386);
						valType();
						}
						}
						setState(391);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(392);
					match(RPAR);
					}
					} 
				}
				setState(397);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			}
			setState(409);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAR) {
				{
				{
				setState(398);
				match(LPAR);
				setState(399);
				match(RESULT);
				setState(403);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3202L) != 0) || _la==V128) {
					{
					{
					setState(400);
					valType();
					}
					}
					setState(405);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(406);
				match(RPAR);
				}
				}
				setState(411);
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
			setState(423);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(412);
					match(LPAR);
					setState(413);
					match(PARAM);
					setState(417);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3202L) != 0) || _la==V128) {
						{
						{
						setState(414);
						valType();
						}
						}
						setState(419);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(420);
					match(RPAR);
					}
					} 
				}
				setState(425);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
			}
			setState(426);
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
			setState(439);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(428);
					match(LPAR);
					setState(429);
					match(RESULT);
					setState(433);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3202L) != 0) || _la==V128) {
						{
						{
						setState(430);
						valType();
						}
						}
						setState(435);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(436);
					match(RPAR);
					}
					} 
				}
				setState(441);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
			}
			setState(442);
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
		public List<BlockContext> block() {
			return getRuleContexts(BlockContext.class);
		}
		public BlockContext block(int i) {
			return getRuleContext(BlockContext.class,i);
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
		public TerminalNode TRY() { return getToken(WatParser.TRY, 0); }
		public TerminalNode CATCH() { return getToken(WatParser.CATCH, 0); }
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
			setState(484);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BLOCK:
				enterOuterAlt(_localctx, 1);
				{
				setState(444);
				match(BLOCK);
				setState(446);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(445);
					bindVar();
					}
				}

				setState(448);
				block();
				setState(449);
				match(END);
				setState(451);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
				case 1:
					{
					setState(450);
					bindVar();
					}
					break;
				}
				}
				break;
			case LOOP:
				enterOuterAlt(_localctx, 2);
				{
				setState(453);
				match(LOOP);
				setState(455);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(454);
					bindVar();
					}
				}

				setState(457);
				block();
				setState(458);
				match(END);
				setState(460);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
				case 1:
					{
					setState(459);
					bindVar();
					}
					break;
				}
				}
				break;
			case IF:
				enterOuterAlt(_localctx, 3);
				{
				setState(462);
				match(IF);
				setState(464);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(463);
					bindVar();
					}
				}

				setState(466);
				block();
				setState(472);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(467);
					match(ELSE);
					setState(469);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==VAR) {
						{
						setState(468);
						bindVar();
						}
					}

					setState(471);
					instrList();
					}
				}

				setState(474);
				match(END);
				setState(476);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
				case 1:
					{
					setState(475);
					bindVar();
					}
					break;
				}
				}
				break;
			case TRY:
				enterOuterAlt(_localctx, 4);
				{
				setState(478);
				match(TRY);
				setState(479);
				block();
				setState(480);
				match(CATCH);
				setState(481);
				block();
				setState(482);
				match(END);
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
			setState(497);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(491);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
				case 1:
					{
					setState(486);
					match(LPAR);
					setState(487);
					match(RESULT);
					setState(488);
					valType();
					setState(489);
					match(RPAR);
					}
					break;
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(493);
				typeUse();
				setState(494);
				funcType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(496);
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
			setState(499);
			blockType();
			setState(500);
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
			setState(502);
			match(LPAR);
			setState(503);
			expr();
			setState(504);
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
			setState(548);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(506);
				plainInstr();
				setState(510);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,44,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(507);
						expr();
						}
						} 
					}
					setState(512);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,44,_ctx);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(513);
				match(CALL_INDIRECT);
				setState(514);
				callExprType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(515);
				match(RETURN_CALL_INDIRECT);
				setState(516);
				callExprType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(517);
				match(BLOCK);
				setState(519);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
				case 1:
					{
					setState(518);
					bindVar();
					}
					break;
				}
				setState(521);
				block();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(522);
				match(LOOP);
				setState(524);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
				case 1:
					{
					setState(523);
					bindVar();
					}
					break;
				}
				setState(526);
				block();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(527);
				match(IF);
				setState(529);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(528);
					bindVar();
					}
				}

				setState(531);
				blockType();
				setState(535);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(532);
						foldedInstr();
						}
						} 
					}
					setState(537);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
				}
				setState(538);
				match(LPAR);
				setState(539);
				match(THEN);
				setState(540);
				instrList();
				setState(546);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LPAR) {
					{
					setState(541);
					match(LPAR);
					setState(542);
					match(ELSE);
					setState(543);
					instrList();
					setState(544);
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
			setState(551);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				{
				setState(550);
				typeUse();
				}
				break;
			}
			setState(553);
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
			setState(566);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(555);
					match(LPAR);
					setState(556);
					match(PARAM);
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
				}
				setState(568);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
			}
			setState(569);
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
			setState(582);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAR) {
				{
				{
				setState(571);
				match(LPAR);
				setState(572);
				match(RESULT);
				setState(576);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3202L) != 0) || _la==V128) {
					{
					{
					setState(573);
					valType();
					}
					}
					setState(578);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(579);
				match(RPAR);
				}
				}
				setState(584);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(588);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,56,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(585);
					expr();
					}
					} 
				}
				setState(590);
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
			setState(594);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(591);
					instr();
					}
					} 
				}
				setState(596);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
			}
			setState(598);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
			case 1:
				{
				setState(597);
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
			setState(600);
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
			setState(602);
			match(LPAR);
			setState(603);
			match(FUNC);
			setState(605);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(604);
				bindVar();
				}
			}

			setState(607);
			funcFields();
			setState(608);
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
			setState(623);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(611);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
				case 1:
					{
					setState(610);
					typeUse();
					}
					break;
				}
				setState(613);
				funcFieldsBody();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(614);
				inlineImport();
				setState(616);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
				case 1:
					{
					setState(615);
					typeUse();
					}
					break;
				}
				setState(618);
				funcType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(620);
				inlineExport();
				setState(621);
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
			setState(625);
			funcType();
			setState(626);
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
			setState(644);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(628);
					match(LPAR);
					setState(629);
					match(LOCAL);
					setState(639);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case LPAR:
					case RPAR:
					case VALUE_TYPE:
					case FUNCREF:
					case EXTERNREF:
					case V128:
						{
						setState(633);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3202L) != 0) || _la==V128) {
							{
							{
							setState(630);
							valType();
							}
							}
							setState(635);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						}
						break;
					case VAR:
						{
						setState(636);
						bindVar();
						setState(637);
						valType();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(641);
					match(RPAR);
					}
					} 
				}
				setState(646);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
			}
			setState(647);
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
			setState(655);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LPAR:
				enterOuterAlt(_localctx, 1);
				{
				setState(649);
				match(LPAR);
				setState(650);
				match(OFFSET);
				setState(651);
				constExpr();
				setState(652);
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
			case CALLREF:
			case CONTNEW:
			case CONTBIND:
			case SUSPEND:
			case THROW:
			case RESUME0:
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
				setState(654);
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
			setState(697);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
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
				match(LPAR);
				setState(663);
				instr();
				setState(664);
				match(RPAR);
				setState(668);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NAT || _la==VAR) {
					{
					{
					setState(665);
					idx();
					}
					}
					setState(670);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(671);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(673);
				match(LPAR);
				setState(674);
				match(ELEM);
				setState(676);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAT || _la==VAR) {
					{
					setState(675);
					idx();
					}
				}

				setState(678);
				offset();
				setState(682);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NAT || _la==VAR) {
					{
					{
					setState(679);
					idx();
					}
					}
					setState(684);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(685);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(687);
				match(LPAR);
				setState(688);
				match(ELEM);
				setState(690);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAT || _la==VAR) {
					{
					setState(689);
					idx();
					}
				}

				setState(692);
				match(DECLARE);
				setState(693);
				match(FUNC);
				setState(694);
				idx();
				setState(695);
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
			setState(699);
			match(LPAR);
			setState(700);
			match(TABLE);
			setState(702);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(701);
				bindVar();
				}
			}

			setState(704);
			tableField();
			setState(705);
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
			setState(725);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,75,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(707);
				tableType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(708);
				inlineImport();
				setState(709);
				tableType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(711);
				inlineExport();
				setState(712);
				tableField();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(714);
				refType();
				setState(715);
				match(LPAR);
				setState(716);
				match(ELEM);
				setState(720);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NAT || _la==VAR) {
					{
					{
					setState(717);
					idx();
					}
					}
					setState(722);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(723);
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
			setState(757);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,80,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
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
				match(LPAR);
				setState(733);
				instr();
				setState(734);
				match(RPAR);
				setState(738);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STRING_) {
					{
					{
					setState(735);
					match(STRING_);
					}
					}
					setState(740);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(741);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(743);
				match(LPAR);
				setState(744);
				match(DATA);
				setState(746);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAT || _la==VAR) {
					{
					setState(745);
					idx();
					}
				}

				setState(748);
				offset();
				setState(752);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STRING_) {
					{
					{
					setState(749);
					match(STRING_);
					}
					}
					setState(754);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(755);
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
			setState(759);
			match(LPAR);
			setState(760);
			match(MEMORY);
			setState(762);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(761);
				bindVar();
				}
			}

			setState(764);
			memoryField();
			setState(765);
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
			setState(783);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,83,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(767);
				memoryType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(768);
				inlineImport();
				setState(769);
				memoryType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(771);
				inlineExport();
				setState(772);
				memoryField();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(774);
				match(LPAR);
				setState(775);
				match(DATA);
				setState(779);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STRING_) {
					{
					{
					setState(776);
					match(STRING_);
					}
					}
					setState(781);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(782);
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
			setState(785);
			match(LPAR);
			setState(786);
			match(GLOBAL);
			setState(788);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(787);
				bindVar();
				}
			}

			setState(790);
			globalField();
			setState(791);
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
			setState(802);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(793);
				globalType();
				setState(794);
				constExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(796);
				inlineImport();
				setState(797);
				globalType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(799);
				inlineExport();
				setState(800);
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
			setState(844);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,91,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(804);
				match(LPAR);
				setState(805);
				match(FUNC);
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
				typeUse();
				setState(810);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(812);
				match(LPAR);
				setState(813);
				match(FUNC);
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
				funcType();
				setState(818);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(820);
				match(LPAR);
				setState(821);
				match(TABLE);
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
				tableType();
				setState(826);
				match(RPAR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(828);
				match(LPAR);
				setState(829);
				match(MEMORY);
				setState(831);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(830);
					bindVar();
					}
				}

				setState(833);
				memoryType();
				setState(834);
				match(RPAR);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(836);
				match(LPAR);
				setState(837);
				match(GLOBAL);
				setState(839);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(838);
					bindVar();
					}
				}

				setState(841);
				globalType();
				setState(842);
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
			setState(846);
			match(LPAR);
			setState(847);
			match(IMPORT);
			setState(848);
			name();
			setState(849);
			name();
			setState(850);
			importDesc();
			setState(851);
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
			setState(853);
			match(LPAR);
			setState(854);
			match(IMPORT);
			setState(855);
			name();
			setState(856);
			name();
			setState(857);
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
			setState(879);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,92,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(859);
				match(LPAR);
				setState(860);
				match(FUNC);
				setState(861);
				idx();
				setState(862);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(864);
				match(LPAR);
				setState(865);
				match(TABLE);
				setState(866);
				idx();
				setState(867);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(869);
				match(LPAR);
				setState(870);
				match(MEMORY);
				setState(871);
				idx();
				setState(872);
				match(RPAR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(874);
				match(LPAR);
				setState(875);
				match(GLOBAL);
				setState(876);
				idx();
				setState(877);
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
			setState(881);
			match(LPAR);
			setState(882);
			match(EXPORT);
			setState(883);
			name();
			setState(884);
			exportDesc();
			setState(885);
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
			setState(887);
			match(LPAR);
			setState(888);
			match(EXPORT);
			setState(889);
			name();
			setState(890);
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
			setState(892);
			match(LPAR);
			setState(893);
			match(TAG);
			setState(895);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(894);
				bindVar();
				}
			}

			setState(897);
			typeUse();
			setState(898);
			funcType();
			setState(899);
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
			setState(901);
			match(LPAR);
			setState(902);
			match(TYPE);
			setState(904);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(903);
				bindVar();
				}
			}

			setState(906);
			defType();
			setState(907);
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
			setState(909);
			match(LPAR);
			setState(910);
			match(START_);
			setState(911);
			idx();
			setState(912);
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
			setState(925);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,95,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(914);
				typeDef();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(915);
				global();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(916);
				table();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(917);
				memory();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(918);
				function();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(919);
				elem();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(920);
				data();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(921);
				start_();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(922);
				simport();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(923);
				export_();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(924);
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
			setState(927);
			match(LPAR);
			setState(928);
			match(MODULE);
			setState(930);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(929);
				match(VAR);
				}
			}

			setState(935);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAR) {
				{
				{
				setState(932);
				moduleField();
				}
				}
				setState(937);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(938);
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
		public TerminalNode DEFINITION() { return getToken(WatParser.DEFINITION, 0); }
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
			setState(968);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,102,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(940);
				module_();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(941);
				match(LPAR);
				setState(942);
				match(MODULE);
				setState(944);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(943);
					match(VAR);
					}
				}

				setState(946);
				_la = _input.LA(1);
				if ( !(_la==BIN || _la==QUOTE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(950);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STRING_) {
					{
					{
					setState(947);
					match(STRING_);
					}
					}
					setState(952);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(953);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(954);
				match(LPAR);
				setState(955);
				match(MODULE);
				setState(956);
				match(DEFINITION);
				setState(958);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(957);
					match(VAR);
					}
				}

				setState(960);
				match(BIN);
				setState(964);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STRING_) {
					{
					{
					setState(961);
					match(STRING_);
					}
					}
					setState(966);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(967);
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
			setState(987);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,105,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(970);
				match(LPAR);
				setState(971);
				match(INVOKE);
				setState(973);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(972);
					match(VAR);
					}
				}

				setState(975);
				name();
				setState(976);
				constList();
				setState(977);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(979);
				match(LPAR);
				setState(980);
				match(GET);
				setState(982);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(981);
					match(VAR);
					}
				}

				setState(984);
				name();
				setState(985);
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
			setState(1041);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,106,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(989);
				match(LPAR);
				setState(990);
				match(ASSERT_MALFORMED);
				setState(991);
				scriptModule();
				setState(992);
				match(STRING_);
				setState(993);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(995);
				match(LPAR);
				setState(996);
				match(ASSERT_INVALID);
				setState(997);
				scriptModule();
				setState(998);
				match(STRING_);
				setState(999);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1001);
				match(LPAR);
				setState(1002);
				match(ASSERT_UNLINKABLE);
				setState(1003);
				scriptModule();
				setState(1004);
				match(STRING_);
				setState(1005);
				match(RPAR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1007);
				match(LPAR);
				setState(1008);
				match(ASSERT_TRAP);
				setState(1009);
				scriptModule();
				setState(1010);
				match(STRING_);
				setState(1011);
				match(RPAR);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1013);
				match(LPAR);
				setState(1014);
				match(ASSERT_RETURN);
				setState(1015);
				action_();
				setState(1016);
				constList();
				setState(1017);
				match(RPAR);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1019);
				match(LPAR);
				setState(1020);
				match(ASSERT_RETURN_CANONICAL_NAN);
				setState(1021);
				action_();
				setState(1022);
				match(RPAR);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1024);
				match(LPAR);
				setState(1025);
				match(ASSERT_RETURN_ARITHMETIC_NAN);
				setState(1026);
				action_();
				setState(1027);
				match(RPAR);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1029);
				match(LPAR);
				setState(1030);
				match(ASSERT_TRAP);
				setState(1031);
				action_();
				setState(1032);
				match(STRING_);
				setState(1033);
				match(RPAR);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1035);
				match(LPAR);
				setState(1036);
				match(ASSERT_EXHAUSTION);
				setState(1037);
				action_();
				setState(1038);
				match(STRING_);
				setState(1039);
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
		public InstanceContext instance() {
			return getRuleContext(InstanceContext.class,0);
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
			setState(1056);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,108,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1043);
				action_();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1044);
				assertion();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1045);
				scriptModule();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1046);
				match(LPAR);
				setState(1047);
				match(REGISTER);
				setState(1048);
				name();
				setState(1050);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(1049);
					match(VAR);
					}
				}

				setState(1052);
				match(RPAR);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1054);
				meta();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1055);
				instance();
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
	public static class InstanceContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(WatParser.LPAR, 0); }
		public TerminalNode MODULE() { return getToken(WatParser.MODULE, 0); }
		public TerminalNode INSTANCE() { return getToken(WatParser.INSTANCE, 0); }
		public TerminalNode RPAR() { return getToken(WatParser.RPAR, 0); }
		public List<TerminalNode> VAR() { return getTokens(WatParser.VAR); }
		public TerminalNode VAR(int i) {
			return getToken(WatParser.VAR, i);
		}
		public InstanceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instance; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).enterInstance(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WatParserListener ) ((WatParserListener)listener).exitInstance(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WatParserVisitor ) return ((WatParserVisitor<? extends T>)visitor).visitInstance(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstanceContext instance() throws RecognitionException {
		InstanceContext _localctx = new InstanceContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_instance);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1058);
			match(LPAR);
			setState(1059);
			match(MODULE);
			setState(1060);
			match(INSTANCE);
			setState(1062);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,109,_ctx) ) {
			case 1:
				{
				setState(1061);
				match(VAR);
				}
				break;
			}
			setState(1065);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(1064);
				match(VAR);
				}
			}

			setState(1067);
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
		enterRule(_localctx, 140, RULE_meta);
		int _la;
		try {
			setState(1101);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,116,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1069);
				match(LPAR);
				setState(1070);
				match(SCRIPT);
				setState(1072);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(1071);
					match(VAR);
					}
				}

				setState(1077);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LPAR) {
					{
					{
					setState(1074);
					cmd();
					}
					}
					setState(1079);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1080);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1081);
				match(LPAR);
				setState(1082);
				match(INPUT);
				setState(1084);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(1083);
					match(VAR);
					}
				}

				setState(1086);
				match(STRING_);
				setState(1087);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1088);
				match(LPAR);
				setState(1089);
				match(OUTPUT);
				setState(1091);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(1090);
					match(VAR);
					}
				}

				setState(1093);
				match(STRING_);
				setState(1094);
				match(RPAR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1095);
				match(LPAR);
				setState(1096);
				match(OUTPUT);
				setState(1098);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VAR) {
					{
					setState(1097);
					match(VAR);
					}
				}

				setState(1100);
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
		enterRule(_localctx, 142, RULE_wconst);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1103);
			match(LPAR);
			setState(1104);
			match(CONST);
			setState(1105);
			literal();
			setState(1106);
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
		enterRule(_localctx, 144, RULE_constList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1111);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAR) {
				{
				{
				setState(1108);
				wconst();
				}
				}
				setState(1113);
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
		enterRule(_localctx, 146, RULE_script);
		int _la;
		try {
			setState(1128);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,120,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1117);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LPAR) {
					{
					{
					setState(1114);
					cmd();
					}
					}
					setState(1119);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1120);
				match(EOF);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1122); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(1121);
					moduleField();
					}
					}
					setState(1124); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==LPAR );
				setState(1126);
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
		enterRule(_localctx, 148, RULE_module);
		int _la;
		try {
			setState(1140);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,122,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1130);
				module_();
				setState(1131);
				match(EOF);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1136);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LPAR) {
					{
					{
					setState(1133);
					moduleField();
					}
					}
					setState(1138);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1139);
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
		"\u0004\u0001\u00a6\u0477\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
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
		"J\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0003\u0003\u00a4\b\u0003\u0001\u0004\u0001\u0004\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0003\u0005\u00ab\b\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0003\u0006\u00b0\b\u0006\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u00b8\b\u0007\u0001\b"+
		"\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0003\b\u00c4\b\b\u0001\t\u0001\t\u0001\t\u0005\t\u00c9\b\t\n\t\f\t"+
		"\u00cc\t\t\u0001\t\u0001\t\u0001\t\u0003\t\u00d1\b\t\u0001\t\u0005\t\u00d4"+
		"\b\t\n\t\f\t\u00d7\t\t\u0001\n\u0001\n\u0001\n\u0005\n\u00dc\b\n\n\n\f"+
		"\n\u00df\t\n\u0001\n\u0005\n\u00e2\b\n\n\n\f\n\u00e5\t\n\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\f\u0001\f\u0003\f\u00ec\b\f\u0001\f\u0001\f\u0001"+
		"\r\u0001\r\u0003\r\u00f2\b\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001"+
		"\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003"+
		"\u0012\u0103\b\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0004"+
		"\u0013\u010f\b\u0013\u000b\u0013\f\u0013\u0110\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u0124\b\u0013\u0001\u0013"+
		"\u0003\u0013\u0127\b\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u012b\b"+
		"\u0013\u0001\u0013\u0003\u0013\u012e\b\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0003"+
		"\u0013\u0150\b\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0005\u0014\u0155"+
		"\b\u0014\n\u0014\f\u0014\u0158\t\u0014\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0003\u0018\u016b\b\u0018\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0003\u0019\u0170\b\u0019\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001b\u0001\u001b\u0003\u001b\u0177\b\u001b\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0003\u001b\u017c\b\u001b\u0001\u001b\u0003\u001b\u017f\b"+
		"\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0005\u001c\u0184\b\u001c\n"+
		"\u001c\f\u001c\u0187\t\u001c\u0001\u001c\u0005\u001c\u018a\b\u001c\n\u001c"+
		"\f\u001c\u018d\t\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0005\u001c"+
		"\u0192\b\u001c\n\u001c\f\u001c\u0195\t\u001c\u0001\u001c\u0005\u001c\u0198"+
		"\b\u001c\n\u001c\f\u001c\u019b\t\u001c\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0005\u001d\u01a0\b\u001d\n\u001d\f\u001d\u01a3\t\u001d\u0001\u001d\u0005"+
		"\u001d\u01a6\b\u001d\n\u001d\f\u001d\u01a9\t\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0005\u001e\u01b0\b\u001e\n\u001e"+
		"\f\u001e\u01b3\t\u001e\u0001\u001e\u0005\u001e\u01b6\b\u001e\n\u001e\f"+
		"\u001e\u01b9\t\u001e\u0001\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0003"+
		"\u001f\u01bf\b\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0003\u001f\u01c4"+
		"\b\u001f\u0001\u001f\u0001\u001f\u0003\u001f\u01c8\b\u001f\u0001\u001f"+
		"\u0001\u001f\u0001\u001f\u0003\u001f\u01cd\b\u001f\u0001\u001f\u0001\u001f"+
		"\u0003\u001f\u01d1\b\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0003\u001f"+
		"\u01d6\b\u001f\u0001\u001f\u0003\u001f\u01d9\b\u001f\u0001\u001f\u0001"+
		"\u001f\u0003\u001f\u01dd\b\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001"+
		"\u001f\u0001\u001f\u0001\u001f\u0003\u001f\u01e5\b\u001f\u0001 \u0001"+
		" \u0001 \u0001 \u0001 \u0003 \u01ec\b \u0001 \u0001 \u0001 \u0001 \u0003"+
		" \u01f2\b \u0001!\u0001!\u0001!\u0001\"\u0001\"\u0001\"\u0001\"\u0001"+
		"#\u0001#\u0005#\u01fd\b#\n#\f#\u0200\t#\u0001#\u0001#\u0001#\u0001#\u0001"+
		"#\u0001#\u0003#\u0208\b#\u0001#\u0001#\u0001#\u0003#\u020d\b#\u0001#\u0001"+
		"#\u0001#\u0003#\u0212\b#\u0001#\u0001#\u0005#\u0216\b#\n#\f#\u0219\t#"+
		"\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0003#\u0223"+
		"\b#\u0003#\u0225\b#\u0001$\u0003$\u0228\b$\u0001$\u0001$\u0001%\u0001"+
		"%\u0001%\u0005%\u022f\b%\n%\f%\u0232\t%\u0001%\u0005%\u0235\b%\n%\f%\u0238"+
		"\t%\u0001%\u0001%\u0001&\u0001&\u0001&\u0005&\u023f\b&\n&\f&\u0242\t&"+
		"\u0001&\u0005&\u0245\b&\n&\f&\u0248\t&\u0001&\u0005&\u024b\b&\n&\f&\u024e"+
		"\t&\u0001\'\u0005\'\u0251\b\'\n\'\f\'\u0254\t\'\u0001\'\u0003\'\u0257"+
		"\b\'\u0001(\u0001(\u0001)\u0001)\u0001)\u0003)\u025e\b)\u0001)\u0001)"+
		"\u0001)\u0001*\u0003*\u0264\b*\u0001*\u0001*\u0001*\u0003*\u0269\b*\u0001"+
		"*\u0001*\u0001*\u0001*\u0001*\u0003*\u0270\b*\u0001+\u0001+\u0001+\u0001"+
		",\u0001,\u0001,\u0005,\u0278\b,\n,\f,\u027b\t,\u0001,\u0001,\u0001,\u0003"+
		",\u0280\b,\u0001,\u0005,\u0283\b,\n,\f,\u0286\t,\u0001,\u0001,\u0001-"+
		"\u0001-\u0001-\u0001-\u0001-\u0001-\u0003-\u0290\b-\u0001.\u0001.\u0001"+
		".\u0003.\u0295\b.\u0001.\u0001.\u0001.\u0001.\u0005.\u029b\b.\n.\f.\u029e"+
		"\t.\u0001.\u0001.\u0001.\u0001.\u0001.\u0003.\u02a5\b.\u0001.\u0001.\u0005"+
		".\u02a9\b.\n.\f.\u02ac\t.\u0001.\u0001.\u0001.\u0001.\u0001.\u0003.\u02b3"+
		"\b.\u0001.\u0001.\u0001.\u0001.\u0001.\u0003.\u02ba\b.\u0001/\u0001/\u0001"+
		"/\u0003/\u02bf\b/\u0001/\u0001/\u0001/\u00010\u00010\u00010\u00010\u0001"+
		"0\u00010\u00010\u00010\u00010\u00010\u00010\u00050\u02cf\b0\n0\f0\u02d2"+
		"\t0\u00010\u00010\u00030\u02d6\b0\u00011\u00011\u00011\u00031\u02db\b"+
		"1\u00011\u00011\u00011\u00011\u00051\u02e1\b1\n1\f1\u02e4\t1\u00011\u0001"+
		"1\u00011\u00011\u00011\u00031\u02eb\b1\u00011\u00011\u00051\u02ef\b1\n"+
		"1\f1\u02f2\t1\u00011\u00011\u00031\u02f6\b1\u00012\u00012\u00012\u0003"+
		"2\u02fb\b2\u00012\u00012\u00012\u00013\u00013\u00013\u00013\u00013\u0001"+
		"3\u00013\u00013\u00013\u00013\u00053\u030a\b3\n3\f3\u030d\t3\u00013\u0003"+
		"3\u0310\b3\u00014\u00014\u00014\u00034\u0315\b4\u00014\u00014\u00014\u0001"+
		"5\u00015\u00015\u00015\u00015\u00015\u00015\u00015\u00015\u00035\u0323"+
		"\b5\u00016\u00016\u00016\u00036\u0328\b6\u00016\u00016\u00016\u00016\u0001"+
		"6\u00016\u00036\u0330\b6\u00016\u00016\u00016\u00016\u00016\u00016\u0003"+
		"6\u0338\b6\u00016\u00016\u00016\u00016\u00016\u00016\u00036\u0340\b6\u0001"+
		"6\u00016\u00016\u00016\u00016\u00016\u00036\u0348\b6\u00016\u00016\u0001"+
		"6\u00036\u034d\b6\u00017\u00017\u00017\u00017\u00017\u00017\u00017\u0001"+
		"8\u00018\u00018\u00018\u00018\u00018\u00019\u00019\u00019\u00019\u0001"+
		"9\u00019\u00019\u00019\u00019\u00019\u00019\u00019\u00019\u00019\u0001"+
		"9\u00019\u00019\u00019\u00019\u00019\u00039\u0370\b9\u0001:\u0001:\u0001"+
		":\u0001:\u0001:\u0001:\u0001;\u0001;\u0001;\u0001;\u0001;\u0001<\u0001"+
		"<\u0001<\u0003<\u0380\b<\u0001<\u0001<\u0001<\u0001<\u0001=\u0001=\u0001"+
		"=\u0003=\u0389\b=\u0001=\u0001=\u0001=\u0001>\u0001>\u0001>\u0001>\u0001"+
		">\u0001?\u0001?\u0001?\u0001?\u0001?\u0001?\u0001?\u0001?\u0001?\u0001"+
		"?\u0001?\u0003?\u039e\b?\u0001@\u0001@\u0001@\u0003@\u03a3\b@\u0001@\u0005"+
		"@\u03a6\b@\n@\f@\u03a9\t@\u0001@\u0001@\u0001A\u0001A\u0001A\u0001A\u0003"+
		"A\u03b1\bA\u0001A\u0001A\u0005A\u03b5\bA\nA\fA\u03b8\tA\u0001A\u0001A"+
		"\u0001A\u0001A\u0001A\u0003A\u03bf\bA\u0001A\u0001A\u0005A\u03c3\bA\n"+
		"A\fA\u03c6\tA\u0001A\u0003A\u03c9\bA\u0001B\u0001B\u0001B\u0003B\u03ce"+
		"\bB\u0001B\u0001B\u0001B\u0001B\u0001B\u0001B\u0001B\u0003B\u03d7\bB\u0001"+
		"B\u0001B\u0001B\u0003B\u03dc\bB\u0001C\u0001C\u0001C\u0001C\u0001C\u0001"+
		"C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001"+
		"C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001"+
		"C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001"+
		"C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001"+
		"C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0003C\u0412\bC\u0001D\u0001"+
		"D\u0001D\u0001D\u0001D\u0001D\u0001D\u0003D\u041b\bD\u0001D\u0001D\u0001"+
		"D\u0001D\u0003D\u0421\bD\u0001E\u0001E\u0001E\u0001E\u0003E\u0427\bE\u0001"+
		"E\u0003E\u042a\bE\u0001E\u0001E\u0001F\u0001F\u0001F\u0003F\u0431\bF\u0001"+
		"F\u0005F\u0434\bF\nF\fF\u0437\tF\u0001F\u0001F\u0001F\u0001F\u0003F\u043d"+
		"\bF\u0001F\u0001F\u0001F\u0001F\u0001F\u0003F\u0444\bF\u0001F\u0001F\u0001"+
		"F\u0001F\u0001F\u0003F\u044b\bF\u0001F\u0003F\u044e\bF\u0001G\u0001G\u0001"+
		"G\u0001G\u0001G\u0001H\u0005H\u0456\bH\nH\fH\u0459\tH\u0001I\u0005I\u045c"+
		"\bI\nI\fI\u045f\tI\u0001I\u0001I\u0004I\u0463\bI\u000bI\fI\u0464\u0001"+
		"I\u0001I\u0003I\u0469\bI\u0001J\u0001J\u0001J\u0001J\u0005J\u046f\bJ\n"+
		"J\fJ\u0472\tJ\u0001J\u0003J\u0475\bJ\u0001J\u0000\u0000K\u0000\u0002\u0004"+
		"\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \""+
		"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086"+
		"\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0000\u0004\u0001\u0000\u0004"+
		"\u0005\u0001\u0000\u0003\u0005\u0002\u0000\u0003\u0003\u00a3\u00a3\u0001"+
		"\u0000\u0091\u0092\u04fb\u0000\u0096\u0001\u0000\u0000\u0000\u0002\u0098"+
		"\u0001\u0000\u0000\u0000\u0004\u009a\u0001\u0000\u0000\u0000\u0006\u00a3"+
		"\u0001\u0000\u0000\u0000\b\u00a5\u0001\u0000\u0000\u0000\n\u00aa\u0001"+
		"\u0000\u0000\u0000\f\u00af\u0001\u0000\u0000\u0000\u000e\u00b7\u0001\u0000"+
		"\u0000\u0000\u0010\u00c3\u0001\u0000\u0000\u0000\u0012\u00d5\u0001\u0000"+
		"\u0000\u0000\u0014\u00e3\u0001\u0000\u0000\u0000\u0016\u00e6\u0001\u0000"+
		"\u0000\u0000\u0018\u00e9\u0001\u0000\u0000\u0000\u001a\u00ef\u0001\u0000"+
		"\u0000\u0000\u001c\u00f3\u0001\u0000\u0000\u0000\u001e\u00f8\u0001\u0000"+
		"\u0000\u0000 \u00fa\u0001\u0000\u0000\u0000\"\u00fc\u0001\u0000\u0000"+
		"\u0000$\u0102\u0001\u0000\u0000\u0000&\u014f\u0001\u0000\u0000\u0000("+
		"\u0151\u0001\u0000\u0000\u0000*\u0159\u0001\u0000\u0000\u0000,\u015f\u0001"+
		"\u0000\u0000\u0000.\u0162\u0001\u0000\u0000\u00000\u0165\u0001\u0000\u0000"+
		"\u00002\u016c\u0001\u0000\u0000\u00004\u0171\u0001\u0000\u0000\u00006"+
		"\u017e\u0001\u0000\u0000\u00008\u018b\u0001\u0000\u0000\u0000:\u01a7\u0001"+
		"\u0000\u0000\u0000<\u01b7\u0001\u0000\u0000\u0000>\u01e4\u0001\u0000\u0000"+
		"\u0000@\u01f1\u0001\u0000\u0000\u0000B\u01f3\u0001\u0000\u0000\u0000D"+
		"\u01f6\u0001\u0000\u0000\u0000F\u0224\u0001\u0000\u0000\u0000H\u0227\u0001"+
		"\u0000\u0000\u0000J\u0236\u0001\u0000\u0000\u0000L\u0246\u0001\u0000\u0000"+
		"\u0000N\u0252\u0001\u0000\u0000\u0000P\u0258\u0001\u0000\u0000\u0000R"+
		"\u025a\u0001\u0000\u0000\u0000T\u026f\u0001\u0000\u0000\u0000V\u0271\u0001"+
		"\u0000\u0000\u0000X\u0284\u0001\u0000\u0000\u0000Z\u028f\u0001\u0000\u0000"+
		"\u0000\\\u02b9\u0001\u0000\u0000\u0000^\u02bb\u0001\u0000\u0000\u0000"+
		"`\u02d5\u0001\u0000\u0000\u0000b\u02f5\u0001\u0000\u0000\u0000d\u02f7"+
		"\u0001\u0000\u0000\u0000f\u030f\u0001\u0000\u0000\u0000h\u0311\u0001\u0000"+
		"\u0000\u0000j\u0322\u0001\u0000\u0000\u0000l\u034c\u0001\u0000\u0000\u0000"+
		"n\u034e\u0001\u0000\u0000\u0000p\u0355\u0001\u0000\u0000\u0000r\u036f"+
		"\u0001\u0000\u0000\u0000t\u0371\u0001\u0000\u0000\u0000v\u0377\u0001\u0000"+
		"\u0000\u0000x\u037c\u0001\u0000\u0000\u0000z\u0385\u0001\u0000\u0000\u0000"+
		"|\u038d\u0001\u0000\u0000\u0000~\u039d\u0001\u0000\u0000\u0000\u0080\u039f"+
		"\u0001\u0000\u0000\u0000\u0082\u03c8\u0001\u0000\u0000\u0000\u0084\u03db"+
		"\u0001\u0000\u0000\u0000\u0086\u0411\u0001\u0000\u0000\u0000\u0088\u0420"+
		"\u0001\u0000\u0000\u0000\u008a\u0422\u0001\u0000\u0000\u0000\u008c\u044d"+
		"\u0001\u0000\u0000\u0000\u008e\u044f\u0001\u0000\u0000\u0000\u0090\u0457"+
		"\u0001\u0000\u0000\u0000\u0092\u0468\u0001\u0000\u0000\u0000\u0094\u0474"+
		"\u0001\u0000\u0000\u0000\u0096\u0097\u0007\u0000\u0000\u0000\u0097\u0001"+
		"\u0001\u0000\u0000\u0000\u0098\u0099\u0005\u0006\u0000\u0000\u0099\u0003"+
		"\u0001\u0000\u0000\u0000\u009a\u009b\u0005\u0007\u0000\u0000\u009b\u0005"+
		"\u0001\u0000\u0000\u0000\u009c\u00a4\u0005\n\u0000\u0000\u009d\u00a4\u0005"+
		"\u000b\u0000\u0000\u009e\u009f\u0005\u0001\u0000\u0000\u009f\u00a0\u0005"+
		"\r\u0000\u0000\u00a0\u00a1\u0003 \u0010\u0000\u00a1\u00a2\u0005\u0002"+
		"\u0000\u0000\u00a2\u00a4\u0001\u0000\u0000\u0000\u00a3\u009c\u0001\u0000"+
		"\u0000\u0000\u00a3\u009d\u0001\u0000\u0000\u0000\u00a3\u009e\u0001\u0000"+
		"\u0000\u0000\u00a4\u0007\u0001\u0000\u0000\u0000\u00a5\u00a6\u0005\u00a4"+
		"\u0000\u0000\u00a6\t\u0001\u0000\u0000\u0000\u00a7\u00ab\u0003\u0004\u0002"+
		"\u0000\u00a8\u00ab\u0003\b\u0004\u0000\u00a9\u00ab\u0003\u0006\u0003\u0000"+
		"\u00aa\u00a7\u0001\u0000\u0000\u0000\u00aa\u00a8\u0001\u0000\u0000\u0000"+
		"\u00aa\u00a9\u0001\u0000\u0000\u0000\u00ab\u000b\u0001\u0000\u0000\u0000"+
		"\u00ac\u00b0\u0005\u0080\u0000\u0000\u00ad\u00b0\u0005\u0081\u0000\u0000"+
		"\u00ae\u00b0\u0003\u0016\u000b\u0000\u00af\u00ac\u0001\u0000\u0000\u0000"+
		"\u00af\u00ad\u0001\u0000\u0000\u0000\u00af\u00ae\u0001\u0000\u0000\u0000"+
		"\u00b0\r\u0001\u0000\u0000\u0000\u00b1\u00b8\u0003\n\u0005\u0000\u00b2"+
		"\u00b3\u0005\u0001\u0000\u0000\u00b3\u00b4\u0005\f\u0000\u0000\u00b4\u00b5"+
		"\u0003\n\u0005\u0000\u00b5\u00b6\u0005\u0002\u0000\u0000\u00b6\u00b8\u0001"+
		"\u0000\u0000\u0000\u00b7\u00b1\u0001\u0000\u0000\u0000\u00b7\u00b2\u0001"+
		"\u0000\u0000\u0000\u00b8\u000f\u0001\u0000\u0000\u0000\u00b9\u00ba\u0005"+
		"\u0001\u0000\u0000\u00ba\u00bb\u0005\u0080\u0000\u0000\u00bb\u00bc\u0003"+
		"\u0016\u000b\u0000\u00bc\u00bd\u0005\u0002\u0000\u0000\u00bd\u00c4\u0001"+
		"\u0000\u0000\u0000\u00be\u00bf\u0005\u0001\u0000\u0000\u00bf\u00c0\u0005"+
		"\u000e\u0000\u0000\u00c0\u00c1\u0003 \u0010\u0000\u00c1\u00c2\u0005\u0002"+
		"\u0000\u0000\u00c2\u00c4\u0001\u0000\u0000\u0000\u00c3\u00b9\u0001\u0000"+
		"\u0000\u0000\u00c3\u00be\u0001\u0000\u0000\u0000\u00c4\u0011\u0001\u0000"+
		"\u0000\u0000\u00c5\u00c6\u0005\u0001\u0000\u0000\u00c6\u00d0\u0005\u0083"+
		"\u0000\u0000\u00c7\u00c9\u0003\n\u0005\u0000\u00c8\u00c7\u0001\u0000\u0000"+
		"\u0000\u00c9\u00cc\u0001\u0000\u0000\u0000\u00ca\u00c8\u0001\u0000\u0000"+
		"\u0000\u00ca\u00cb\u0001\u0000\u0000\u0000\u00cb\u00d1\u0001\u0000\u0000"+
		"\u0000\u00cc\u00ca\u0001\u0000\u0000\u0000\u00cd\u00ce\u0003\"\u0011\u0000"+
		"\u00ce\u00cf\u0003\n\u0005\u0000\u00cf\u00d1\u0001\u0000\u0000\u0000\u00d0"+
		"\u00ca\u0001\u0000\u0000\u0000\u00d0\u00cd\u0001\u0000\u0000\u0000\u00d1"+
		"\u00d2\u0001\u0000\u0000\u0000\u00d2\u00d4\u0005\u0002\u0000\u0000\u00d3"+
		"\u00c5\u0001\u0000\u0000\u0000\u00d4\u00d7\u0001\u0000\u0000\u0000\u00d5"+
		"\u00d3\u0001\u0000\u0000\u0000\u00d5\u00d6\u0001\u0000\u0000\u0000\u00d6"+
		"\u0013\u0001\u0000\u0000\u0000\u00d7\u00d5\u0001\u0000\u0000\u0000\u00d8"+
		"\u00d9\u0005\u0001\u0000\u0000\u00d9\u00dd\u0005\u0084\u0000\u0000\u00da"+
		"\u00dc\u0003\n\u0005\u0000\u00db\u00da\u0001\u0000\u0000\u0000\u00dc\u00df"+
		"\u0001\u0000\u0000\u0000\u00dd\u00db\u0001\u0000\u0000\u0000\u00dd\u00de"+
		"\u0001\u0000\u0000\u0000\u00de\u00e0\u0001\u0000\u0000\u0000\u00df\u00dd"+
		"\u0001\u0000\u0000\u0000\u00e0\u00e2\u0005\u0002\u0000\u0000\u00e1\u00d8"+
		"\u0001\u0000\u0000\u0000\u00e2\u00e5\u0001\u0000\u0000\u0000\u00e3\u00e1"+
		"\u0001\u0000\u0000\u0000\u00e3\u00e4\u0001\u0000\u0000\u0000\u00e4\u0015"+
		"\u0001\u0000\u0000\u0000\u00e5\u00e3\u0001\u0000\u0000\u0000\u00e6\u00e7"+
		"\u0003\u0012\t\u0000\u00e7\u00e8\u0003\u0014\n\u0000\u00e8\u0017\u0001"+
		"\u0000\u0000\u0000\u00e9\u00eb\u0005\u0003\u0000\u0000\u00ea\u00ec\u0005"+
		"\u0003\u0000\u0000\u00eb\u00ea\u0001\u0000\u0000\u0000\u00eb\u00ec\u0001"+
		"\u0000\u0000\u0000\u00ec\u00ed\u0001\u0000\u0000\u0000\u00ed\u00ee\u0003"+
		"\u0006\u0003\u0000\u00ee\u0019\u0001\u0000\u0000\u0000\u00ef\u00f1\u0005"+
		"\u0003\u0000\u0000\u00f0\u00f2\u0005\u0003\u0000\u0000\u00f1\u00f0\u0001"+
		"\u0000\u0000\u0000\u00f1\u00f2\u0001\u0000\u0000\u0000\u00f2\u001b\u0001"+
		"\u0000\u0000\u0000\u00f3\u00f4\u0005\u0001\u0000\u0000\u00f4\u00f5\u0005"+
		"\u007f\u0000\u0000\u00f5\u00f6\u0003 \u0010\u0000\u00f6\u00f7\u0005\u0002"+
		"\u0000\u0000\u00f7\u001d\u0001\u0000\u0000\u0000\u00f8\u00f9\u0007\u0001"+
		"\u0000\u0000\u00f9\u001f\u0001\u0000\u0000\u0000\u00fa\u00fb\u0007\u0002"+
		"\u0000\u0000\u00fb!\u0001\u0000\u0000\u0000\u00fc\u00fd\u0005\u00a3\u0000"+
		"\u0000\u00fd#\u0001\u0000\u0000\u0000\u00fe\u0103\u0003&\u0013\u0000\u00ff"+
		"\u0103\u0003>\u001f\u0000\u0100\u0103\u0003D\"\u0000\u0101\u0103\u0003"+
		"(\u0014\u0000\u0102\u00fe\u0001\u0000\u0000\u0000\u0102\u00ff\u0001\u0000"+
		"\u0000\u0000\u0102\u0100\u0001\u0000\u0000\u0000\u0102\u0101\u0001\u0000"+
		"\u0000\u0000\u0103%\u0001\u0000\u0000\u0000\u0104\u0150\u0005\u0013\u0000"+
		"\u0000\u0105\u0150\u0005\u000f\u0000\u0000\u0106\u0150\u0005\u0014\u0000"+
		"\u0000\u0107\u0150\u00034\u001a\u0000\u0108\u0109\u0005\u0018\u0000\u0000"+
		"\u0109\u0150\u0003 \u0010\u0000\u010a\u010b\u0005\u0019\u0000\u0000\u010b"+
		"\u0150\u0003 \u0010\u0000\u010c\u010e\u0005\u001a\u0000\u0000\u010d\u010f"+
		"\u0003 \u0010\u0000\u010e\u010d\u0001\u0000\u0000\u0000\u010f\u0110\u0001"+
		"\u0000\u0000\u0000\u0110\u010e\u0001\u0000\u0000\u0000\u0110\u0111\u0001"+
		"\u0000\u0000\u0000\u0111\u0150\u0001\u0000\u0000\u0000\u0112\u0150\u0005"+
		"\u001b\u0000\u0000\u0113\u0114\u0005 \u0000\u0000\u0114\u0150\u0003 \u0010"+
		"\u0000\u0115\u0116\u0005\"\u0000\u0000\u0116\u0150\u0003 \u0010\u0000"+
		"\u0117\u0118\u0005/\u0000\u0000\u0118\u0150\u0003 \u0010\u0000\u0119\u011a"+
		"\u00050\u0000\u0000\u011a\u0150\u0003 \u0010\u0000\u011b\u011c\u00051"+
		"\u0000\u0000\u011c\u0150\u0003 \u0010\u0000\u011d\u011e\u00052\u0000\u0000"+
		"\u011e\u0150\u0003 \u0010\u0000\u011f\u0120\u00053\u0000\u0000\u0120\u0150"+
		"\u0003 \u0010\u0000\u0121\u0123\u00030\u0018\u0000\u0122\u0124\u0003,"+
		"\u0016\u0000\u0123\u0122\u0001\u0000\u0000\u0000\u0123\u0124\u0001\u0000"+
		"\u0000\u0000\u0124\u0126\u0001\u0000\u0000\u0000\u0125\u0127\u0003.\u0017"+
		"\u0000\u0126\u0125\u0001\u0000\u0000\u0000\u0126\u0127\u0001\u0000\u0000"+
		"\u0000\u0127\u0150\u0001\u0000\u0000\u0000\u0128\u012a\u00032\u0019\u0000"+
		"\u0129\u012b\u0003,\u0016\u0000\u012a\u0129\u0001\u0000\u0000\u0000\u012a"+
		"\u012b\u0001\u0000\u0000\u0000\u012b\u012d\u0001\u0000\u0000\u0000\u012c"+
		"\u012e\u0003.\u0017\u0000\u012d\u012c\u0001\u0000\u0000\u0000\u012d\u012e"+
		"\u0001\u0000\u0000\u0000\u012e\u0150\u0001\u0000\u0000\u0000\u012f\u0150"+
		"\u0005u\u0000\u0000\u0130\u0150\u0005v\u0000\u0000\u0131\u0150\u0005w"+
		"\u0000\u0000\u0132\u0150\u0005x\u0000\u0000\u0133\u0134\u0005y\u0000\u0000"+
		"\u0134\u0150\u0003 \u0010\u0000\u0135\u0136\u0005\b\u0000\u0000\u0136"+
		"\u0150\u0003\u001e\u000f\u0000\u0137\u0150\u0005\t\u0000\u0000\u0138\u0150"+
		"\u0005\u0010\u0000\u0000\u0139\u0150\u0005\u0011\u0000\u0000\u013a\u0150"+
		"\u0005\u0012\u0000\u0000\u013b\u0150\u0005z\u0000\u0000\u013c\u0150\u0005"+
		"{\u0000\u0000\u013d\u0150\u0005|\u0000\u0000\u013e\u0150\u0005}\u0000"+
		"\u0000\u013f\u0150\u0005~\u0000\u0000\u0140\u0150\u00036\u001b\u0000\u0141"+
		"\u0142\u0005(\u0000\u0000\u0142\u0150\u0003 \u0010\u0000\u0143\u0144\u0005"+
		"$\u0000\u0000\u0144\u0150\u0003 \u0010\u0000\u0145\u0146\u0005*\u0000"+
		"\u0000\u0146\u0150\u0003 \u0010\u0000\u0147\u0148\u0005)\u0000\u0000\u0148"+
		"\u0149\u0003 \u0010\u0000\u0149\u014a\u0003 \u0010\u0000\u014a\u0150\u0001"+
		"\u0000\u0000\u0000\u014b\u014c\u0005%\u0000\u0000\u014c\u0150\u0003 \u0010"+
		"\u0000\u014d\u0150\u0005.\u0000\u0000\u014e\u0150\u0005-\u0000\u0000\u014f"+
		"\u0104\u0001\u0000\u0000\u0000\u014f\u0105\u0001\u0000\u0000\u0000\u014f"+
		"\u0106\u0001\u0000\u0000\u0000\u014f\u0107\u0001\u0000\u0000\u0000\u014f"+
		"\u0108\u0001\u0000\u0000\u0000\u014f\u010a\u0001\u0000\u0000\u0000\u014f"+
		"\u010c\u0001\u0000\u0000\u0000\u014f\u0112\u0001\u0000\u0000\u0000\u014f"+
		"\u0113\u0001\u0000\u0000\u0000\u014f\u0115\u0001\u0000\u0000\u0000\u014f"+
		"\u0117\u0001\u0000\u0000\u0000\u014f\u0119\u0001\u0000\u0000\u0000\u014f"+
		"\u011b\u0001\u0000\u0000\u0000\u014f\u011d\u0001\u0000\u0000\u0000\u014f"+
		"\u011f\u0001\u0000\u0000\u0000\u014f\u0121\u0001\u0000\u0000\u0000\u014f"+
		"\u0128\u0001\u0000\u0000\u0000\u014f\u012f\u0001\u0000\u0000\u0000\u014f"+
		"\u0130\u0001\u0000\u0000\u0000\u014f\u0131\u0001\u0000\u0000\u0000\u014f"+
		"\u0132\u0001\u0000\u0000\u0000\u014f\u0133\u0001\u0000\u0000\u0000\u014f"+
		"\u0135\u0001\u0000\u0000\u0000\u014f\u0137\u0001\u0000\u0000\u0000\u014f"+
		"\u0138\u0001\u0000\u0000\u0000\u014f\u0139\u0001\u0000\u0000\u0000\u014f"+
		"\u013a\u0001\u0000\u0000\u0000\u014f\u013b\u0001\u0000\u0000\u0000\u014f"+
		"\u013c\u0001\u0000\u0000\u0000\u014f\u013d\u0001\u0000\u0000\u0000\u014f"+
		"\u013e\u0001\u0000\u0000\u0000\u014f\u013f\u0001\u0000\u0000\u0000\u014f"+
		"\u0140\u0001\u0000\u0000\u0000\u014f\u0141\u0001\u0000\u0000\u0000\u014f"+
		"\u0143\u0001\u0000\u0000\u0000\u014f\u0145\u0001\u0000\u0000\u0000\u014f"+
		"\u0147\u0001\u0000\u0000\u0000\u014f\u014b\u0001\u0000\u0000\u0000\u014f"+
		"\u014d\u0001\u0000\u0000\u0000\u014f\u014e\u0001\u0000\u0000\u0000\u0150"+
		"\'\u0001\u0000\u0000\u0000\u0151\u0152\u0005&\u0000\u0000\u0152\u0156"+
		"\u0003 \u0010\u0000\u0153\u0155\u0003*\u0015\u0000\u0154\u0153\u0001\u0000"+
		"\u0000\u0000\u0155\u0158\u0001\u0000\u0000\u0000\u0156\u0154\u0001\u0000"+
		"\u0000\u0000\u0156\u0157\u0001\u0000\u0000\u0000\u0157)\u0001\u0000\u0000"+
		"\u0000\u0158\u0156\u0001\u0000\u0000\u0000\u0159\u015a\u0005\u0001\u0000"+
		"\u0000\u015a\u015b\u0005\'\u0000\u0000\u015b\u015c\u0003 \u0010\u0000"+
		"\u015c\u015d\u0003 \u0010\u0000\u015d\u015e\u0005\u0002\u0000\u0000\u015e"+
		"+\u0001\u0000\u0000\u0000\u015f\u0160\u00057\u0000\u0000\u0160\u0161\u0005"+
		"\u0003\u0000\u0000\u0161-\u0001\u0000\u0000\u0000\u0162\u0163\u00058\u0000"+
		"\u0000\u0163\u0164\u0005\u0003\u0000\u0000\u0164/\u0001\u0000\u0000\u0000"+
		"\u0165\u0166\u0003\u0004\u0002\u0000\u0166\u016a\u00054\u0000\u0000\u0167"+
		"\u0168\u0005:\u0000\u0000\u0168\u0169\u00056\u0000\u0000\u0169\u016b\u0005"+
		"9\u0000\u0000\u016a\u0167\u0001\u0000\u0000\u0000\u016a\u016b\u0001\u0000"+
		"\u0000\u0000\u016b1\u0001\u0000\u0000\u0000\u016c\u016d\u0003\u0004\u0002"+
		"\u0000\u016d\u016f\u00055\u0000\u0000\u016e\u0170\u0005:\u0000\u0000\u016f"+
		"\u016e\u0001\u0000\u0000\u0000\u016f\u0170\u0001\u0000\u0000\u0000\u0170"+
		"3\u0001\u0000\u0000\u0000\u0171\u0172\u0003\u0004\u0002\u0000\u0172\u0173"+
		"\u0005\u001f\u0000\u0000\u01735\u0001\u0000\u0000\u0000\u0174\u0176\u0005"+
		"!\u0000\u0000\u0175\u0177\u0003 \u0010\u0000\u0176\u0175\u0001\u0000\u0000"+
		"\u0000\u0176\u0177\u0001\u0000\u0000\u0000\u0177\u0178\u0001\u0000\u0000"+
		"\u0000\u0178\u017f\u0003\u001c\u000e\u0000\u0179\u017b\u0005#\u0000\u0000"+
		"\u017a\u017c\u0003 \u0010\u0000\u017b\u017a\u0001\u0000\u0000\u0000\u017b"+
		"\u017c\u0001\u0000\u0000\u0000\u017c\u017d\u0001\u0000\u0000\u0000\u017d"+
		"\u017f\u0003\u001c\u000e\u0000\u017e\u0174\u0001\u0000\u0000\u0000\u017e"+
		"\u0179\u0001\u0000\u0000\u0000\u017f7\u0001\u0000\u0000\u0000\u0180\u0181"+
		"\u0005\u0001\u0000\u0000\u0181\u0185\u0005\u0083\u0000\u0000\u0182\u0184"+
		"\u0003\n\u0005\u0000\u0183\u0182\u0001\u0000\u0000\u0000\u0184\u0187\u0001"+
		"\u0000\u0000\u0000\u0185\u0183\u0001\u0000\u0000\u0000\u0185\u0186\u0001"+
		"\u0000\u0000\u0000\u0186\u0188\u0001\u0000\u0000\u0000\u0187\u0185\u0001"+
		"\u0000\u0000\u0000\u0188\u018a\u0005\u0002\u0000\u0000\u0189\u0180\u0001"+
		"\u0000\u0000\u0000\u018a\u018d\u0001\u0000\u0000\u0000\u018b\u0189\u0001"+
		"\u0000\u0000\u0000\u018b\u018c\u0001\u0000\u0000\u0000\u018c\u0199\u0001"+
		"\u0000\u0000\u0000\u018d\u018b\u0001\u0000\u0000\u0000\u018e\u018f\u0005"+
		"\u0001\u0000\u0000\u018f\u0193\u0005\u0084\u0000\u0000\u0190\u0192\u0003"+
		"\n\u0005\u0000\u0191\u0190\u0001\u0000\u0000\u0000\u0192\u0195\u0001\u0000"+
		"\u0000\u0000\u0193\u0191\u0001\u0000\u0000\u0000\u0193\u0194\u0001\u0000"+
		"\u0000\u0000\u0194\u0196\u0001\u0000\u0000\u0000\u0195\u0193\u0001\u0000"+
		"\u0000\u0000\u0196\u0198\u0005\u0002\u0000\u0000\u0197\u018e\u0001\u0000"+
		"\u0000\u0000\u0198\u019b\u0001\u0000\u0000\u0000\u0199\u0197\u0001\u0000"+
		"\u0000\u0000\u0199\u019a\u0001\u0000\u0000\u0000\u019a9\u0001\u0000\u0000"+
		"\u0000\u019b\u0199\u0001\u0000\u0000\u0000\u019c\u019d\u0005\u0001\u0000"+
		"\u0000\u019d\u01a1\u0005\u0083\u0000\u0000\u019e\u01a0\u0003\n\u0005\u0000"+
		"\u019f\u019e\u0001\u0000\u0000\u0000\u01a0\u01a3\u0001\u0000\u0000\u0000"+
		"\u01a1\u019f\u0001\u0000\u0000\u0000\u01a1\u01a2\u0001\u0000\u0000\u0000"+
		"\u01a2\u01a4\u0001\u0000\u0000\u0000\u01a3\u01a1\u0001\u0000\u0000\u0000"+
		"\u01a4\u01a6\u0005\u0002\u0000\u0000\u01a5\u019c\u0001\u0000\u0000\u0000"+
		"\u01a6\u01a9\u0001\u0000\u0000\u0000\u01a7\u01a5\u0001\u0000\u0000\u0000"+
		"\u01a7\u01a8\u0001\u0000\u0000\u0000\u01a8\u01aa\u0001\u0000\u0000\u0000"+
		"\u01a9\u01a7\u0001\u0000\u0000\u0000\u01aa\u01ab\u0003<\u001e\u0000\u01ab"+
		";\u0001\u0000\u0000\u0000\u01ac\u01ad\u0005\u0001\u0000\u0000\u01ad\u01b1"+
		"\u0005\u0084\u0000\u0000\u01ae\u01b0\u0003\n\u0005\u0000\u01af\u01ae\u0001"+
		"\u0000\u0000\u0000\u01b0\u01b3\u0001\u0000\u0000\u0000\u01b1\u01af\u0001"+
		"\u0000\u0000\u0000\u01b1\u01b2\u0001\u0000\u0000\u0000\u01b2\u01b4\u0001"+
		"\u0000\u0000\u0000\u01b3\u01b1\u0001\u0000\u0000\u0000\u01b4\u01b6\u0005"+
		"\u0002\u0000\u0000\u01b5\u01ac\u0001\u0000\u0000\u0000\u01b6\u01b9\u0001"+
		"\u0000\u0000\u0000\u01b7\u01b5\u0001\u0000\u0000\u0000\u01b7\u01b8\u0001"+
		"\u0000\u0000\u0000\u01b8\u01ba\u0001\u0000\u0000\u0000\u01b9\u01b7\u0001"+
		"\u0000\u0000\u0000\u01ba\u01bb\u0003$\u0012\u0000\u01bb=\u0001\u0000\u0000"+
		"\u0000\u01bc\u01be\u0005\u0015\u0000\u0000\u01bd\u01bf\u0003\"\u0011\u0000"+
		"\u01be\u01bd\u0001\u0000\u0000\u0000\u01be\u01bf\u0001\u0000\u0000\u0000"+
		"\u01bf\u01c0\u0001\u0000\u0000\u0000\u01c0\u01c1\u0003B!\u0000\u01c1\u01c3"+
		"\u0005\u0017\u0000\u0000\u01c2\u01c4\u0003\"\u0011\u0000\u01c3\u01c2\u0001"+
		"\u0000\u0000\u0000\u01c3\u01c4\u0001\u0000\u0000\u0000\u01c4\u01e5\u0001"+
		"\u0000\u0000\u0000\u01c5\u01c7\u0005\u0016\u0000\u0000\u01c6\u01c8\u0003"+
		"\"\u0011\u0000\u01c7\u01c6\u0001\u0000\u0000\u0000\u01c7\u01c8\u0001\u0000"+
		"\u0000\u0000\u01c8\u01c9\u0001\u0000\u0000\u0000\u01c9\u01ca\u0003B!\u0000"+
		"\u01ca\u01cc\u0005\u0017\u0000\u0000\u01cb\u01cd\u0003\"\u0011\u0000\u01cc"+
		"\u01cb\u0001\u0000\u0000\u0000\u01cc\u01cd\u0001\u0000\u0000\u0000\u01cd"+
		"\u01e5\u0001\u0000\u0000\u0000\u01ce\u01d0\u0005\u001c\u0000\u0000\u01cf"+
		"\u01d1\u0003\"\u0011\u0000\u01d0\u01cf\u0001\u0000\u0000\u0000\u01d0\u01d1"+
		"\u0001\u0000\u0000\u0000\u01d1\u01d2\u0001\u0000\u0000\u0000\u01d2\u01d8"+
		"\u0003B!\u0000\u01d3\u01d5\u0005\u001e\u0000\u0000\u01d4\u01d6\u0003\""+
		"\u0011\u0000\u01d5\u01d4\u0001\u0000\u0000\u0000\u01d5\u01d6\u0001\u0000"+
		"\u0000\u0000\u01d6\u01d7\u0001\u0000\u0000\u0000\u01d7\u01d9\u0003N\'"+
		"\u0000\u01d8\u01d3\u0001\u0000\u0000\u0000\u01d8\u01d9\u0001\u0000\u0000"+
		"\u0000\u01d9\u01da\u0001\u0000\u0000\u0000\u01da\u01dc\u0005\u0017\u0000"+
		"\u0000\u01db\u01dd\u0003\"\u0011\u0000\u01dc\u01db\u0001\u0000\u0000\u0000"+
		"\u01dc\u01dd\u0001\u0000\u0000\u0000\u01dd\u01e5\u0001\u0000\u0000\u0000"+
		"\u01de\u01df\u0005+\u0000\u0000\u01df\u01e0\u0003B!\u0000\u01e0\u01e1"+
		"\u0005,\u0000\u0000\u01e1\u01e2\u0003B!\u0000\u01e2\u01e3\u0005\u0017"+
		"\u0000\u0000\u01e3\u01e5\u0001\u0000\u0000\u0000\u01e4\u01bc\u0001\u0000"+
		"\u0000\u0000\u01e4\u01c5\u0001\u0000\u0000\u0000\u01e4\u01ce\u0001\u0000"+
		"\u0000\u0000\u01e4\u01de\u0001\u0000\u0000\u0000\u01e5?\u0001\u0000\u0000"+
		"\u0000\u01e6\u01e7\u0005\u0001\u0000\u0000\u01e7\u01e8\u0005\u0084\u0000"+
		"\u0000\u01e8\u01e9\u0003\n\u0005\u0000\u01e9\u01ea\u0005\u0002\u0000\u0000"+
		"\u01ea\u01ec\u0001\u0000\u0000\u0000\u01eb\u01e6\u0001\u0000\u0000\u0000"+
		"\u01eb\u01ec\u0001\u0000\u0000\u0000\u01ec\u01f2\u0001\u0000\u0000\u0000"+
		"\u01ed\u01ee\u0003\u001c\u000e\u0000\u01ee\u01ef\u0003\u0016\u000b\u0000"+
		"\u01ef\u01f2\u0001\u0000\u0000\u0000\u01f0\u01f2\u0003\u0016\u000b\u0000"+
		"\u01f1\u01eb\u0001\u0000\u0000\u0000\u01f1\u01ed\u0001\u0000\u0000\u0000"+
		"\u01f1\u01f0\u0001\u0000\u0000\u0000\u01f2A\u0001\u0000\u0000\u0000\u01f3"+
		"\u01f4\u0003@ \u0000\u01f4\u01f5\u0003N\'\u0000\u01f5C\u0001\u0000\u0000"+
		"\u0000\u01f6\u01f7\u0005\u0001\u0000\u0000\u01f7\u01f8\u0003F#\u0000\u01f8"+
		"\u01f9\u0005\u0002\u0000\u0000\u01f9E\u0001\u0000\u0000\u0000\u01fa\u01fe"+
		"\u0003&\u0013\u0000\u01fb\u01fd\u0003F#\u0000\u01fc\u01fb\u0001\u0000"+
		"\u0000\u0000\u01fd\u0200\u0001\u0000\u0000\u0000\u01fe\u01fc\u0001\u0000"+
		"\u0000\u0000\u01fe\u01ff\u0001\u0000\u0000\u0000\u01ff\u0225\u0001\u0000"+
		"\u0000\u0000\u0200\u01fe\u0001\u0000\u0000\u0000\u0201\u0202\u0005!\u0000"+
		"\u0000\u0202\u0225\u0003H$\u0000\u0203\u0204\u0005#\u0000\u0000\u0204"+
		"\u0225\u0003H$\u0000\u0205\u0207\u0005\u0015\u0000\u0000\u0206\u0208\u0003"+
		"\"\u0011\u0000\u0207\u0206\u0001\u0000\u0000\u0000\u0207\u0208\u0001\u0000"+
		"\u0000\u0000\u0208\u0209\u0001\u0000\u0000\u0000\u0209\u0225\u0003B!\u0000"+
		"\u020a\u020c\u0005\u0016\u0000\u0000\u020b\u020d\u0003\"\u0011\u0000\u020c"+
		"\u020b\u0001\u0000\u0000\u0000\u020c\u020d\u0001\u0000\u0000\u0000\u020d"+
		"\u020e\u0001\u0000\u0000\u0000\u020e\u0225\u0003B!\u0000\u020f\u0211\u0005"+
		"\u001c\u0000\u0000\u0210\u0212\u0003\"\u0011\u0000\u0211\u0210\u0001\u0000"+
		"\u0000\u0000\u0211\u0212\u0001\u0000\u0000\u0000\u0212\u0213\u0001\u0000"+
		"\u0000\u0000\u0213\u0217\u0003@ \u0000\u0214\u0216\u0003D\"\u0000\u0215"+
		"\u0214\u0001\u0000\u0000\u0000\u0216\u0219\u0001\u0000\u0000\u0000\u0217"+
		"\u0215\u0001\u0000\u0000\u0000\u0217\u0218\u0001\u0000\u0000\u0000\u0218"+
		"\u021a\u0001\u0000\u0000\u0000\u0219\u0217\u0001\u0000\u0000\u0000\u021a"+
		"\u021b\u0005\u0001\u0000\u0000\u021b\u021c\u0005\u001d\u0000\u0000\u021c"+
		"\u0222\u0003N\'\u0000\u021d\u021e\u0005\u0001\u0000\u0000\u021e\u021f"+
		"\u0005\u001e\u0000\u0000\u021f\u0220\u0003N\'\u0000\u0220\u0221\u0005"+
		"\u0002\u0000\u0000\u0221\u0223\u0001\u0000\u0000\u0000\u0222\u021d\u0001"+
		"\u0000\u0000\u0000\u0222\u0223\u0001\u0000\u0000\u0000\u0223\u0225\u0001"+
		"\u0000\u0000\u0000\u0224\u01fa\u0001\u0000\u0000\u0000\u0224\u0201\u0001"+
		"\u0000\u0000\u0000\u0224\u0203\u0001\u0000\u0000\u0000\u0224\u0205\u0001"+
		"\u0000\u0000\u0000\u0224\u020a\u0001\u0000\u0000\u0000\u0224\u020f\u0001"+
		"\u0000\u0000\u0000\u0225G\u0001\u0000\u0000\u0000\u0226\u0228\u0003\u001c"+
		"\u000e\u0000\u0227\u0226\u0001\u0000\u0000\u0000\u0227\u0228\u0001\u0000"+
		"\u0000\u0000\u0228\u0229\u0001\u0000\u0000\u0000\u0229\u022a\u0003J%\u0000"+
		"\u022aI\u0001\u0000\u0000\u0000\u022b\u022c\u0005\u0001\u0000\u0000\u022c"+
		"\u0230\u0005\u0083\u0000\u0000\u022d\u022f\u0003\n\u0005\u0000\u022e\u022d"+
		"\u0001\u0000\u0000\u0000\u022f\u0232\u0001\u0000\u0000\u0000\u0230\u022e"+
		"\u0001\u0000\u0000\u0000\u0230\u0231\u0001\u0000\u0000\u0000\u0231\u0233"+
		"\u0001\u0000\u0000\u0000\u0232\u0230\u0001\u0000\u0000\u0000\u0233\u0235"+
		"\u0005\u0002\u0000\u0000\u0234\u022b\u0001\u0000\u0000\u0000\u0235\u0238"+
		"\u0001\u0000\u0000\u0000\u0236\u0234\u0001\u0000\u0000\u0000\u0236\u0237"+
		"\u0001\u0000\u0000\u0000\u0237\u0239\u0001\u0000\u0000\u0000\u0238\u0236"+
		"\u0001\u0000\u0000\u0000\u0239\u023a\u0003L&\u0000\u023aK\u0001\u0000"+
		"\u0000\u0000\u023b\u023c\u0005\u0001\u0000\u0000\u023c\u0240\u0005\u0084"+
		"\u0000\u0000\u023d\u023f\u0003\n\u0005\u0000\u023e\u023d\u0001\u0000\u0000"+
		"\u0000\u023f\u0242\u0001\u0000\u0000\u0000\u0240\u023e\u0001\u0000\u0000"+
		"\u0000\u0240\u0241\u0001\u0000\u0000\u0000\u0241\u0243\u0001\u0000\u0000"+
		"\u0000\u0242\u0240\u0001\u0000\u0000\u0000\u0243\u0245\u0005\u0002\u0000"+
		"\u0000\u0244\u023b\u0001\u0000\u0000\u0000\u0245\u0248\u0001\u0000\u0000"+
		"\u0000\u0246\u0244\u0001\u0000\u0000\u0000\u0246\u0247\u0001\u0000\u0000"+
		"\u0000\u0247\u024c\u0001\u0000\u0000\u0000\u0248\u0246\u0001\u0000\u0000"+
		"\u0000\u0249\u024b\u0003F#\u0000\u024a\u0249\u0001\u0000\u0000\u0000\u024b"+
		"\u024e\u0001\u0000\u0000\u0000\u024c\u024a\u0001\u0000\u0000\u0000\u024c"+
		"\u024d\u0001\u0000\u0000\u0000\u024dM\u0001\u0000\u0000\u0000\u024e\u024c"+
		"\u0001\u0000\u0000\u0000\u024f\u0251\u0003$\u0012\u0000\u0250\u024f\u0001"+
		"\u0000\u0000\u0000\u0251\u0254\u0001\u0000\u0000\u0000\u0252\u0250\u0001"+
		"\u0000\u0000\u0000\u0252\u0253\u0001\u0000\u0000\u0000\u0253\u0256\u0001"+
		"\u0000\u0000\u0000\u0254\u0252\u0001\u0000\u0000\u0000\u0255\u0257\u0003"+
		"6\u001b\u0000\u0256\u0255\u0001\u0000\u0000\u0000\u0256\u0257\u0001\u0000"+
		"\u0000\u0000\u0257O\u0001\u0000\u0000\u0000\u0258\u0259\u0003N\'\u0000"+
		"\u0259Q\u0001\u0000\u0000\u0000\u025a\u025b\u0005\u0001\u0000\u0000\u025b"+
		"\u025d\u0005\u0080\u0000\u0000\u025c\u025e\u0003\"\u0011\u0000\u025d\u025c"+
		"\u0001\u0000\u0000\u0000\u025d\u025e\u0001\u0000\u0000\u0000\u025e\u025f"+
		"\u0001\u0000\u0000\u0000\u025f\u0260\u0003T*\u0000\u0260\u0261\u0005\u0002"+
		"\u0000\u0000\u0261S\u0001\u0000\u0000\u0000\u0262\u0264\u0003\u001c\u000e"+
		"\u0000\u0263\u0262\u0001\u0000\u0000\u0000\u0263\u0264\u0001\u0000\u0000"+
		"\u0000\u0264\u0265\u0001\u0000\u0000\u0000\u0265\u0270\u0003V+\u0000\u0266"+
		"\u0268\u0003p8\u0000\u0267\u0269\u0003\u001c\u000e\u0000\u0268\u0267\u0001"+
		"\u0000\u0000\u0000\u0268\u0269\u0001\u0000\u0000\u0000\u0269\u026a\u0001"+
		"\u0000\u0000\u0000\u026a\u026b\u0003\u0016\u000b\u0000\u026b\u0270\u0001"+
		"\u0000\u0000\u0000\u026c\u026d\u0003v;\u0000\u026d\u026e\u0003T*\u0000"+
		"\u026e\u0270\u0001\u0000\u0000\u0000\u026f\u0263\u0001\u0000\u0000\u0000"+
		"\u026f\u0266\u0001\u0000\u0000\u0000\u026f\u026c\u0001\u0000\u0000\u0000"+
		"\u0270U\u0001\u0000\u0000\u0000\u0271\u0272\u0003\u0016\u000b\u0000\u0272"+
		"\u0273\u0003X,\u0000\u0273W\u0001\u0000\u0000\u0000\u0274\u0275\u0005"+
		"\u0001\u0000\u0000\u0275\u027f\u0005\u0085\u0000\u0000\u0276\u0278\u0003"+
		"\n\u0005\u0000\u0277\u0276\u0001\u0000\u0000\u0000\u0278\u027b\u0001\u0000"+
		"\u0000\u0000\u0279\u0277\u0001\u0000\u0000\u0000\u0279\u027a\u0001\u0000"+
		"\u0000\u0000\u027a\u0280\u0001\u0000\u0000\u0000\u027b\u0279\u0001\u0000"+
		"\u0000\u0000\u027c\u027d\u0003\"\u0011\u0000\u027d\u027e\u0003\n\u0005"+
		"\u0000\u027e\u0280\u0001\u0000\u0000\u0000\u027f\u0279\u0001\u0000\u0000"+
		"\u0000\u027f\u027c\u0001\u0000\u0000\u0000\u0280\u0281\u0001\u0000\u0000"+
		"\u0000\u0281\u0283\u0005\u0002\u0000\u0000\u0282\u0274\u0001\u0000\u0000"+
		"\u0000\u0283\u0286\u0001\u0000\u0000\u0000\u0284\u0282\u0001\u0000\u0000"+
		"\u0000\u0284\u0285\u0001\u0000\u0000\u0000\u0285\u0287\u0001\u0000\u0000"+
		"\u0000\u0286\u0284\u0001\u0000\u0000\u0000\u0287\u0288\u0003N\'\u0000"+
		"\u0288Y\u0001\u0000\u0000\u0000\u0289\u028a\u0005\u0001\u0000\u0000\u028a"+
		"\u028b\u0005\u008b\u0000\u0000\u028b\u028c\u0003P(\u0000\u028c\u028d\u0005"+
		"\u0002\u0000\u0000\u028d\u0290\u0001\u0000\u0000\u0000\u028e\u0290\u0003"+
		"F#\u0000\u028f\u0289\u0001\u0000\u0000\u0000\u028f\u028e\u0001\u0000\u0000"+
		"\u0000\u0290[\u0001\u0000\u0000\u0000\u0291\u0292\u0005\u0001\u0000\u0000"+
		"\u0292\u0294\u0005\u0089\u0000\u0000\u0293\u0295\u0003 \u0010\u0000\u0294"+
		"\u0293\u0001\u0000\u0000\u0000\u0294\u0295\u0001\u0000\u0000\u0000\u0295"+
		"\u0296\u0001\u0000\u0000\u0000\u0296\u0297\u0005\u0001\u0000\u0000\u0297"+
		"\u0298\u0003$\u0012\u0000\u0298\u029c\u0005\u0002\u0000\u0000\u0299\u029b"+
		"\u0003 \u0010\u0000\u029a\u0299\u0001\u0000\u0000\u0000\u029b\u029e\u0001"+
		"\u0000\u0000\u0000\u029c\u029a\u0001\u0000\u0000\u0000\u029c\u029d\u0001"+
		"\u0000\u0000\u0000\u029d\u029f\u0001\u0000\u0000\u0000\u029e\u029c\u0001"+
		"\u0000\u0000\u0000\u029f\u02a0\u0005\u0002\u0000\u0000\u02a0\u02ba\u0001"+
		"\u0000\u0000\u0000\u02a1\u02a2\u0005\u0001\u0000\u0000\u02a2\u02a4\u0005"+
		"\u0089\u0000\u0000\u02a3\u02a5\u0003 \u0010\u0000\u02a4\u02a3\u0001\u0000"+
		"\u0000\u0000\u02a4\u02a5\u0001\u0000\u0000\u0000\u02a5\u02a6\u0001\u0000"+
		"\u0000\u0000\u02a6\u02aa\u0003Z-\u0000\u02a7\u02a9\u0003 \u0010\u0000"+
		"\u02a8\u02a7\u0001\u0000\u0000\u0000\u02a9\u02ac\u0001\u0000\u0000\u0000"+
		"\u02aa\u02a8\u0001\u0000\u0000\u0000\u02aa\u02ab\u0001\u0000\u0000\u0000"+
		"\u02ab\u02ad\u0001\u0000\u0000\u0000\u02ac\u02aa\u0001\u0000\u0000\u0000"+
		"\u02ad\u02ae\u0005\u0002\u0000\u0000\u02ae\u02ba\u0001\u0000\u0000\u0000"+
		"\u02af\u02b0\u0005\u0001\u0000\u0000\u02b0\u02b2\u0005\u0089\u0000\u0000"+
		"\u02b1\u02b3\u0003 \u0010\u0000\u02b2\u02b1\u0001\u0000\u0000\u0000\u02b2"+
		"\u02b3\u0001\u0000\u0000\u0000\u02b3\u02b4\u0001\u0000\u0000\u0000\u02b4"+
		"\u02b5\u0005\u008f\u0000\u0000\u02b5\u02b6\u0005\u0080\u0000\u0000\u02b6"+
		"\u02b7\u0003 \u0010\u0000\u02b7\u02b8\u0005\u0002\u0000\u0000\u02b8\u02ba"+
		"\u0001\u0000\u0000\u0000\u02b9\u0291\u0001\u0000\u0000\u0000\u02b9\u02a1"+
		"\u0001\u0000\u0000\u0000\u02b9\u02af\u0001\u0000\u0000\u0000\u02ba]\u0001"+
		"\u0000\u0000\u0000\u02bb\u02bc\u0005\u0001\u0000\u0000\u02bc\u02be\u0005"+
		"\u0087\u0000\u0000\u02bd\u02bf\u0003\"\u0011\u0000\u02be\u02bd\u0001\u0000"+
		"\u0000\u0000\u02be\u02bf\u0001\u0000\u0000\u0000\u02bf\u02c0\u0001\u0000"+
		"\u0000\u0000\u02c0\u02c1\u0003`0\u0000\u02c1\u02c2\u0005\u0002\u0000\u0000"+
		"\u02c2_\u0001\u0000\u0000\u0000\u02c3\u02d6\u0003\u0018\f\u0000\u02c4"+
		"\u02c5\u0003p8\u0000\u02c5\u02c6\u0003\u0018\f\u0000\u02c6\u02d6\u0001"+
		"\u0000\u0000\u0000\u02c7\u02c8\u0003v;\u0000\u02c8\u02c9\u0003`0\u0000"+
		"\u02c9\u02d6\u0001\u0000\u0000\u0000\u02ca\u02cb\u0003\u0006\u0003\u0000"+
		"\u02cb\u02cc\u0005\u0001\u0000\u0000\u02cc\u02d0\u0005\u0089\u0000\u0000"+
		"\u02cd\u02cf\u0003 \u0010\u0000\u02ce\u02cd\u0001\u0000\u0000\u0000\u02cf"+
		"\u02d2\u0001\u0000\u0000\u0000\u02d0\u02ce\u0001\u0000\u0000\u0000\u02d0"+
		"\u02d1\u0001\u0000\u0000\u0000\u02d1\u02d3\u0001\u0000\u0000\u0000\u02d2"+
		"\u02d0\u0001\u0000\u0000\u0000\u02d3\u02d4\u0005\u0002\u0000\u0000\u02d4"+
		"\u02d6\u0001\u0000\u0000\u0000\u02d5\u02c3\u0001\u0000\u0000\u0000\u02d5"+
		"\u02c4\u0001\u0000\u0000\u0000\u02d5\u02c7\u0001\u0000\u0000\u0000\u02d5"+
		"\u02ca\u0001\u0000\u0000\u0000\u02d6a\u0001\u0000\u0000\u0000\u02d7\u02d8"+
		"\u0005\u0001\u0000\u0000\u02d8\u02da\u0005\u008a\u0000\u0000\u02d9\u02db"+
		"\u0003 \u0010\u0000\u02da\u02d9\u0001\u0000\u0000\u0000\u02da\u02db\u0001"+
		"\u0000\u0000\u0000\u02db\u02dc\u0001\u0000\u0000\u0000\u02dc\u02dd\u0005"+
		"\u0001\u0000\u0000\u02dd\u02de\u0003$\u0012\u0000\u02de\u02e2\u0005\u0002"+
		"\u0000\u0000\u02df\u02e1\u0005\u0006\u0000\u0000\u02e0\u02df\u0001\u0000"+
		"\u0000\u0000\u02e1\u02e4\u0001\u0000\u0000\u0000\u02e2\u02e0\u0001\u0000"+
		"\u0000\u0000\u02e2\u02e3\u0001\u0000\u0000\u0000\u02e3\u02e5\u0001\u0000"+
		"\u0000\u0000\u02e4\u02e2\u0001\u0000\u0000\u0000\u02e5\u02e6\u0005\u0002"+
		"\u0000\u0000\u02e6\u02f6\u0001\u0000\u0000\u0000\u02e7\u02e8\u0005\u0001"+
		"\u0000\u0000\u02e8\u02ea\u0005\u008a\u0000\u0000\u02e9\u02eb\u0003 \u0010"+
		"\u0000\u02ea\u02e9\u0001\u0000\u0000\u0000\u02ea\u02eb\u0001\u0000\u0000"+
		"\u0000\u02eb\u02ec\u0001\u0000\u0000\u0000\u02ec\u02f0\u0003Z-\u0000\u02ed"+
		"\u02ef\u0005\u0006\u0000\u0000\u02ee\u02ed\u0001\u0000\u0000\u0000\u02ef"+
		"\u02f2\u0001\u0000\u0000\u0000\u02f0\u02ee\u0001\u0000\u0000\u0000\u02f0"+
		"\u02f1\u0001\u0000\u0000\u0000\u02f1\u02f3\u0001\u0000\u0000\u0000\u02f2"+
		"\u02f0\u0001\u0000\u0000\u0000\u02f3\u02f4\u0005\u0002\u0000\u0000\u02f4"+
		"\u02f6\u0001\u0000\u0000\u0000\u02f5\u02d7\u0001\u0000\u0000\u0000\u02f5"+
		"\u02e7\u0001\u0000\u0000\u0000\u02f6c\u0001\u0000\u0000\u0000\u02f7\u02f8"+
		"\u0005\u0001\u0000\u0000\u02f8\u02fa\u0005\u0088\u0000\u0000\u02f9\u02fb"+
		"\u0003\"\u0011\u0000\u02fa\u02f9\u0001\u0000\u0000\u0000\u02fa\u02fb\u0001"+
		"\u0000\u0000\u0000\u02fb\u02fc\u0001\u0000\u0000\u0000\u02fc\u02fd\u0003"+
		"f3\u0000\u02fd\u02fe\u0005\u0002\u0000\u0000\u02fee\u0001\u0000\u0000"+
		"\u0000\u02ff\u0310\u0003\u001a\r\u0000\u0300\u0301\u0003p8\u0000\u0301"+
		"\u0302\u0003\u001a\r\u0000\u0302\u0310\u0001\u0000\u0000\u0000\u0303\u0304"+
		"\u0003v;\u0000\u0304\u0305\u0003f3\u0000\u0305\u0310\u0001\u0000\u0000"+
		"\u0000\u0306\u0307\u0005\u0001\u0000\u0000\u0307\u030b\u0005\u008a\u0000"+
		"\u0000\u0308\u030a\u0005\u0006\u0000\u0000\u0309\u0308\u0001\u0000\u0000"+
		"\u0000\u030a\u030d\u0001\u0000\u0000\u0000\u030b\u0309\u0001\u0000\u0000"+
		"\u0000\u030b\u030c\u0001\u0000\u0000\u0000\u030c\u030e\u0001\u0000\u0000"+
		"\u0000\u030d\u030b\u0001\u0000\u0000\u0000\u030e\u0310\u0005\u0002\u0000"+
		"\u0000\u030f\u02ff\u0001\u0000\u0000\u0000\u030f\u0300\u0001\u0000\u0000"+
		"\u0000\u030f\u0303\u0001\u0000\u0000\u0000\u030f\u0306\u0001\u0000\u0000"+
		"\u0000\u0310g\u0001\u0000\u0000\u0000\u0311\u0312\u0005\u0001\u0000\u0000"+
		"\u0312\u0314\u0005\u0086\u0000\u0000\u0313\u0315\u0003\"\u0011\u0000\u0314"+
		"\u0313\u0001\u0000\u0000\u0000\u0314\u0315\u0001\u0000\u0000\u0000\u0315"+
		"\u0316\u0001\u0000\u0000\u0000\u0316\u0317\u0003j5\u0000\u0317\u0318\u0005"+
		"\u0002\u0000\u0000\u0318i\u0001\u0000\u0000\u0000\u0319\u031a\u0003\u000e"+
		"\u0007\u0000\u031a\u031b\u0003P(\u0000\u031b\u0323\u0001\u0000\u0000\u0000"+
		"\u031c\u031d\u0003p8\u0000\u031d\u031e\u0003\u000e\u0007\u0000\u031e\u0323"+
		"\u0001\u0000\u0000\u0000\u031f\u0320\u0003v;\u0000\u0320\u0321\u0003j"+
		"5\u0000\u0321\u0323\u0001\u0000\u0000\u0000\u0322\u0319\u0001\u0000\u0000"+
		"\u0000\u0322\u031c\u0001\u0000\u0000\u0000\u0322\u031f\u0001\u0000\u0000"+
		"\u0000\u0323k\u0001\u0000\u0000\u0000\u0324\u0325\u0005\u0001\u0000\u0000"+
		"\u0325\u0327\u0005\u0080\u0000\u0000\u0326\u0328\u0003\"\u0011\u0000\u0327"+
		"\u0326\u0001\u0000\u0000\u0000\u0327\u0328\u0001\u0000\u0000\u0000\u0328"+
		"\u0329\u0001\u0000\u0000\u0000\u0329\u032a\u0003\u001c\u000e\u0000\u032a"+
		"\u032b\u0005\u0002\u0000\u0000\u032b\u034d\u0001\u0000\u0000\u0000\u032c"+
		"\u032d\u0005\u0001\u0000\u0000\u032d\u032f\u0005\u0080\u0000\u0000\u032e"+
		"\u0330\u0003\"\u0011\u0000\u032f\u032e\u0001\u0000\u0000\u0000\u032f\u0330"+
		"\u0001\u0000\u0000\u0000\u0330\u0331\u0001\u0000\u0000\u0000\u0331\u0332"+
		"\u0003\u0016\u000b\u0000\u0332\u0333\u0005\u0002\u0000\u0000\u0333\u034d"+
		"\u0001\u0000\u0000\u0000\u0334\u0335\u0005\u0001\u0000\u0000\u0335\u0337"+
		"\u0005\u0087\u0000\u0000\u0336\u0338\u0003\"\u0011\u0000\u0337\u0336\u0001"+
		"\u0000\u0000\u0000\u0337\u0338\u0001\u0000\u0000\u0000\u0338\u0339\u0001"+
		"\u0000\u0000\u0000\u0339\u033a\u0003\u0018\f\u0000\u033a\u033b\u0005\u0002"+
		"\u0000\u0000\u033b\u034d\u0001\u0000\u0000\u0000\u033c\u033d\u0005\u0001"+
		"\u0000\u0000\u033d\u033f\u0005\u0088\u0000\u0000\u033e\u0340\u0003\"\u0011"+
		"\u0000\u033f\u033e\u0001\u0000\u0000\u0000\u033f\u0340\u0001\u0000\u0000"+
		"\u0000\u0340\u0341\u0001\u0000\u0000\u0000\u0341\u0342\u0003\u001a\r\u0000"+
		"\u0342\u0343\u0005\u0002\u0000\u0000\u0343\u034d\u0001\u0000\u0000\u0000"+
		"\u0344\u0345\u0005\u0001\u0000\u0000\u0345\u0347\u0005\u0086\u0000\u0000"+
		"\u0346\u0348\u0003\"\u0011\u0000\u0347\u0346\u0001\u0000\u0000\u0000\u0347"+
		"\u0348\u0001\u0000\u0000\u0000\u0348\u0349\u0001\u0000\u0000\u0000\u0349"+
		"\u034a\u0003\u000e\u0007\u0000\u034a\u034b\u0005\u0002\u0000\u0000\u034b"+
		"\u034d\u0001\u0000\u0000\u0000\u034c\u0324\u0001\u0000\u0000\u0000\u034c"+
		"\u032c\u0001\u0000\u0000\u0000\u034c\u0334\u0001\u0000\u0000\u0000\u034c"+
		"\u033c\u0001\u0000\u0000\u0000\u034c\u0344\u0001\u0000\u0000\u0000\u034d"+
		"m\u0001\u0000\u0000\u0000\u034e\u034f\u0005\u0001\u0000\u0000\u034f\u0350"+
		"\u0005\u008c\u0000\u0000\u0350\u0351\u0003\u0002\u0001\u0000\u0351\u0352"+
		"\u0003\u0002\u0001\u0000\u0352\u0353\u0003l6\u0000\u0353\u0354\u0005\u0002"+
		"\u0000\u0000\u0354o\u0001\u0000\u0000\u0000\u0355\u0356\u0005\u0001\u0000"+
		"\u0000\u0356\u0357\u0005\u008c\u0000\u0000\u0357\u0358\u0003\u0002\u0001"+
		"\u0000\u0358\u0359\u0003\u0002\u0001\u0000\u0359\u035a\u0005\u0002\u0000"+
		"\u0000\u035aq\u0001\u0000\u0000\u0000\u035b\u035c\u0005\u0001\u0000\u0000"+
		"\u035c\u035d\u0005\u0080\u0000\u0000\u035d\u035e\u0003 \u0010\u0000\u035e"+
		"\u035f\u0005\u0002\u0000\u0000\u035f\u0370\u0001\u0000\u0000\u0000\u0360"+
		"\u0361\u0005\u0001\u0000\u0000\u0361\u0362\u0005\u0087\u0000\u0000\u0362"+
		"\u0363\u0003 \u0010\u0000\u0363\u0364\u0005\u0002\u0000\u0000\u0364\u0370"+
		"\u0001\u0000\u0000\u0000\u0365\u0366\u0005\u0001\u0000\u0000\u0366\u0367"+
		"\u0005\u0088\u0000\u0000\u0367\u0368\u0003 \u0010\u0000\u0368\u0369\u0005"+
		"\u0002\u0000\u0000\u0369\u0370\u0001\u0000\u0000\u0000\u036a\u036b\u0005"+
		"\u0001\u0000\u0000\u036b\u036c\u0005\u0086\u0000\u0000\u036c\u036d\u0003"+
		" \u0010\u0000\u036d\u036e\u0005\u0002\u0000\u0000\u036e\u0370\u0001\u0000"+
		"\u0000\u0000\u036f\u035b\u0001\u0000\u0000\u0000\u036f\u0360\u0001\u0000"+
		"\u0000\u0000\u036f\u0365\u0001\u0000\u0000\u0000\u036f\u036a\u0001\u0000"+
		"\u0000\u0000\u0370s\u0001\u0000\u0000\u0000\u0371\u0372\u0005\u0001\u0000"+
		"\u0000\u0372\u0373\u0005\u008d\u0000\u0000\u0373\u0374\u0003\u0002\u0001"+
		"\u0000\u0374\u0375\u0003r9\u0000\u0375\u0376\u0005\u0002\u0000\u0000\u0376"+
		"u\u0001\u0000\u0000\u0000\u0377\u0378\u0005\u0001\u0000\u0000\u0378\u0379"+
		"\u0005\u008d\u0000\u0000\u0379\u037a\u0003\u0002\u0001\u0000\u037a\u037b"+
		"\u0005\u0002\u0000\u0000\u037bw\u0001\u0000\u0000\u0000\u037c\u037d\u0005"+
		"\u0001\u0000\u0000\u037d\u037f\u0005\u008e\u0000\u0000\u037e\u0380\u0003"+
		"\"\u0011\u0000\u037f\u037e\u0001\u0000\u0000\u0000\u037f\u0380\u0001\u0000"+
		"\u0000\u0000\u0380\u0381\u0001\u0000\u0000\u0000\u0381\u0382\u0003\u001c"+
		"\u000e\u0000\u0382\u0383\u0003\u0016\u000b\u0000\u0383\u0384\u0005\u0002"+
		"\u0000\u0000\u0384y\u0001\u0000\u0000\u0000\u0385\u0386\u0005\u0001\u0000"+
		"\u0000\u0386\u0388\u0005\u007f\u0000\u0000\u0387\u0389\u0003\"\u0011\u0000"+
		"\u0388\u0387\u0001\u0000\u0000\u0000\u0388\u0389\u0001\u0000\u0000\u0000"+
		"\u0389\u038a\u0001\u0000\u0000\u0000\u038a\u038b\u0003\u0010\b\u0000\u038b"+
		"\u038c\u0005\u0002\u0000\u0000\u038c{\u0001\u0000\u0000\u0000\u038d\u038e"+
		"\u0005\u0001\u0000\u0000\u038e\u038f\u0005\u0082\u0000\u0000\u038f\u0390"+
		"\u0003 \u0010\u0000\u0390\u0391\u0005\u0002\u0000\u0000\u0391}\u0001\u0000"+
		"\u0000\u0000\u0392\u039e\u0003z=\u0000\u0393\u039e\u0003h4\u0000\u0394"+
		"\u039e\u0003^/\u0000\u0395\u039e\u0003d2\u0000\u0396\u039e\u0003R)\u0000"+
		"\u0397\u039e\u0003\\.\u0000\u0398\u039e\u0003b1\u0000\u0399\u039e\u0003"+
		"|>\u0000\u039a\u039e\u0003n7\u0000\u039b\u039e\u0003t:\u0000\u039c\u039e"+
		"\u0003x<\u0000\u039d\u0392\u0001\u0000\u0000\u0000\u039d\u0393\u0001\u0000"+
		"\u0000\u0000\u039d\u0394\u0001\u0000\u0000\u0000\u039d\u0395\u0001\u0000"+
		"\u0000\u0000\u039d\u0396\u0001\u0000\u0000\u0000\u039d\u0397\u0001\u0000"+
		"\u0000\u0000\u039d\u0398\u0001\u0000\u0000\u0000\u039d\u0399\u0001\u0000"+
		"\u0000\u0000\u039d\u039a\u0001\u0000\u0000\u0000\u039d\u039b\u0001\u0000"+
		"\u0000\u0000\u039d\u039c\u0001\u0000\u0000\u0000\u039e\u007f\u0001\u0000"+
		"\u0000\u0000\u039f\u03a0\u0005\u0001\u0000\u0000\u03a0\u03a2\u0005\u0090"+
		"\u0000\u0000\u03a1\u03a3\u0005\u00a3\u0000\u0000\u03a2\u03a1\u0001\u0000"+
		"\u0000\u0000\u03a2\u03a3\u0001\u0000\u0000\u0000\u03a3\u03a7\u0001\u0000"+
		"\u0000\u0000\u03a4\u03a6\u0003~?\u0000\u03a5\u03a4\u0001\u0000\u0000\u0000"+
		"\u03a6\u03a9\u0001\u0000\u0000\u0000\u03a7\u03a5\u0001\u0000\u0000\u0000"+
		"\u03a7\u03a8\u0001\u0000\u0000\u0000\u03a8\u03aa\u0001\u0000\u0000\u0000"+
		"\u03a9\u03a7\u0001\u0000\u0000\u0000\u03aa\u03ab\u0005\u0002\u0000\u0000"+
		"\u03ab\u0081\u0001\u0000\u0000\u0000\u03ac\u03c9\u0003\u0080@\u0000\u03ad"+
		"\u03ae\u0005\u0001\u0000\u0000\u03ae\u03b0\u0005\u0090\u0000\u0000\u03af"+
		"\u03b1\u0005\u00a3\u0000\u0000\u03b0\u03af\u0001\u0000\u0000\u0000\u03b0"+
		"\u03b1\u0001\u0000\u0000\u0000\u03b1\u03b2\u0001\u0000\u0000\u0000\u03b2"+
		"\u03b6\u0007\u0003\u0000\u0000\u03b3\u03b5\u0005\u0006\u0000\u0000\u03b4"+
		"\u03b3\u0001\u0000\u0000\u0000\u03b5\u03b8\u0001\u0000\u0000\u0000\u03b6"+
		"\u03b4\u0001\u0000\u0000\u0000\u03b6\u03b7\u0001\u0000\u0000\u0000\u03b7"+
		"\u03b9\u0001\u0000\u0000\u0000\u03b8\u03b6\u0001\u0000\u0000\u0000\u03b9"+
		"\u03c9\u0005\u0002\u0000\u0000\u03ba\u03bb\u0005\u0001\u0000\u0000\u03bb"+
		"\u03bc\u0005\u0090\u0000\u0000\u03bc\u03be\u0005\u0093\u0000\u0000\u03bd"+
		"\u03bf\u0005\u00a3\u0000\u0000\u03be\u03bd\u0001\u0000\u0000\u0000\u03be"+
		"\u03bf\u0001\u0000\u0000\u0000\u03bf\u03c0\u0001\u0000\u0000\u0000\u03c0"+
		"\u03c4\u0005\u0091\u0000\u0000\u03c1\u03c3\u0005\u0006\u0000\u0000\u03c2"+
		"\u03c1\u0001\u0000\u0000\u0000\u03c3\u03c6\u0001\u0000\u0000\u0000\u03c4"+
		"\u03c2\u0001\u0000\u0000\u0000\u03c4\u03c5\u0001\u0000\u0000\u0000\u03c5"+
		"\u03c7\u0001\u0000\u0000\u0000\u03c6\u03c4\u0001\u0000\u0000\u0000\u03c7"+
		"\u03c9\u0005\u0002\u0000\u0000\u03c8\u03ac\u0001\u0000\u0000\u0000\u03c8"+
		"\u03ad\u0001\u0000\u0000\u0000\u03c8\u03ba\u0001\u0000\u0000\u0000\u03c9"+
		"\u0083\u0001\u0000\u0000\u0000\u03ca\u03cb\u0005\u0001\u0000\u0000\u03cb"+
		"\u03cd\u0005\u0097\u0000\u0000\u03cc\u03ce\u0005\u00a3\u0000\u0000\u03cd"+
		"\u03cc\u0001\u0000\u0000\u0000\u03cd\u03ce\u0001\u0000\u0000\u0000\u03ce"+
		"\u03cf\u0001\u0000\u0000\u0000\u03cf\u03d0\u0003\u0002\u0001\u0000\u03d0"+
		"\u03d1\u0003\u0090H\u0000\u03d1\u03d2\u0005\u0002\u0000\u0000\u03d2\u03dc"+
		"\u0001\u0000\u0000\u0000\u03d3\u03d4\u0005\u0001\u0000\u0000\u03d4\u03d6"+
		"\u0005\u0098\u0000\u0000\u03d5\u03d7\u0005\u00a3\u0000\u0000\u03d6\u03d5"+
		"\u0001\u0000\u0000\u0000\u03d6\u03d7\u0001\u0000\u0000\u0000\u03d7\u03d8"+
		"\u0001\u0000\u0000\u0000\u03d8\u03d9\u0003\u0002\u0001\u0000\u03d9\u03da"+
		"\u0005\u0002\u0000\u0000\u03da\u03dc\u0001\u0000\u0000\u0000\u03db\u03ca"+
		"\u0001\u0000\u0000\u0000\u03db\u03d3\u0001\u0000\u0000\u0000\u03dc\u0085"+
		"\u0001\u0000\u0000\u0000\u03dd\u03de\u0005\u0001\u0000\u0000\u03de\u03df"+
		"\u0005\u0099\u0000\u0000\u03df\u03e0\u0003\u0082A\u0000\u03e0\u03e1\u0005"+
		"\u0006\u0000\u0000\u03e1\u03e2\u0005\u0002\u0000\u0000\u03e2\u0412\u0001"+
		"\u0000\u0000\u0000\u03e3\u03e4\u0005\u0001\u0000\u0000\u03e4\u03e5\u0005"+
		"\u009a\u0000\u0000\u03e5\u03e6\u0003\u0082A\u0000\u03e6\u03e7\u0005\u0006"+
		"\u0000\u0000\u03e7\u03e8\u0005\u0002\u0000\u0000\u03e8\u0412\u0001\u0000"+
		"\u0000\u0000\u03e9\u03ea\u0005\u0001\u0000\u0000\u03ea\u03eb\u0005\u009b"+
		"\u0000\u0000\u03eb\u03ec\u0003\u0082A\u0000\u03ec\u03ed\u0005\u0006\u0000"+
		"\u0000\u03ed\u03ee\u0005\u0002\u0000\u0000\u03ee\u0412\u0001\u0000\u0000"+
		"\u0000\u03ef\u03f0\u0005\u0001\u0000\u0000\u03f0\u03f1\u0005\u009f\u0000"+
		"\u0000\u03f1\u03f2\u0003\u0082A\u0000\u03f2\u03f3\u0005\u0006\u0000\u0000"+
		"\u03f3\u03f4\u0005\u0002\u0000\u0000\u03f4\u0412\u0001\u0000\u0000\u0000"+
		"\u03f5\u03f6\u0005\u0001\u0000\u0000\u03f6\u03f7\u0005\u009c\u0000\u0000"+
		"\u03f7\u03f8\u0003\u0084B\u0000\u03f8\u03f9\u0003\u0090H\u0000\u03f9\u03fa"+
		"\u0005\u0002\u0000\u0000\u03fa\u0412\u0001\u0000\u0000\u0000\u03fb\u03fc"+
		"\u0005\u0001\u0000\u0000\u03fc\u03fd\u0005\u009d\u0000\u0000\u03fd\u03fe"+
		"\u0003\u0084B\u0000\u03fe\u03ff\u0005\u0002\u0000\u0000\u03ff\u0412\u0001"+
		"\u0000\u0000\u0000\u0400\u0401\u0005\u0001\u0000\u0000\u0401\u0402\u0005"+
		"\u009e\u0000\u0000\u0402\u0403\u0003\u0084B\u0000\u0403\u0404\u0005\u0002"+
		"\u0000\u0000\u0404\u0412\u0001\u0000\u0000\u0000\u0405\u0406\u0005\u0001"+
		"\u0000\u0000\u0406\u0407\u0005\u009f\u0000\u0000\u0407\u0408\u0003\u0084"+
		"B\u0000\u0408\u0409\u0005\u0006\u0000\u0000\u0409\u040a\u0005\u0002\u0000"+
		"\u0000\u040a\u0412\u0001\u0000\u0000\u0000\u040b\u040c\u0005\u0001\u0000"+
		"\u0000\u040c\u040d\u0005\u00a0\u0000\u0000\u040d\u040e\u0003\u0084B\u0000"+
		"\u040e\u040f\u0005\u0006\u0000\u0000\u040f\u0410\u0005\u0002\u0000\u0000"+
		"\u0410\u0412\u0001\u0000\u0000\u0000\u0411\u03dd\u0001\u0000\u0000\u0000"+
		"\u0411\u03e3\u0001\u0000\u0000\u0000\u0411\u03e9\u0001\u0000\u0000\u0000"+
		"\u0411\u03ef\u0001\u0000\u0000\u0000\u0411\u03f5\u0001\u0000\u0000\u0000"+
		"\u0411\u03fb\u0001\u0000\u0000\u0000\u0411\u0400\u0001\u0000\u0000\u0000"+
		"\u0411\u0405\u0001\u0000\u0000\u0000\u0411\u040b\u0001\u0000\u0000\u0000"+
		"\u0412\u0087\u0001\u0000\u0000\u0000\u0413\u0421\u0003\u0084B\u0000\u0414"+
		"\u0421\u0003\u0086C\u0000\u0415\u0421\u0003\u0082A\u0000\u0416\u0417\u0005"+
		"\u0001\u0000\u0000\u0417\u0418\u0005\u0096\u0000\u0000\u0418\u041a\u0003"+
		"\u0002\u0001\u0000\u0419\u041b\u0005\u00a3\u0000\u0000\u041a\u0419\u0001"+
		"\u0000\u0000\u0000\u041a\u041b\u0001\u0000\u0000\u0000\u041b\u041c\u0001"+
		"\u0000\u0000\u0000\u041c\u041d\u0005\u0002\u0000\u0000\u041d\u0421\u0001"+
		"\u0000\u0000\u0000\u041e\u0421\u0003\u008cF\u0000\u041f\u0421\u0003\u008a"+
		"E\u0000\u0420\u0413\u0001\u0000\u0000\u0000\u0420\u0414\u0001\u0000\u0000"+
		"\u0000\u0420\u0415\u0001\u0000\u0000\u0000\u0420\u0416\u0001\u0000\u0000"+
		"\u0000\u0420\u041e\u0001\u0000\u0000\u0000\u0420\u041f\u0001\u0000\u0000"+
		"\u0000\u0421\u0089\u0001\u0000\u0000\u0000\u0422\u0423\u0005\u0001\u0000"+
		"\u0000\u0423\u0424\u0005\u0090\u0000\u0000\u0424\u0426\u0005\u0094\u0000"+
		"\u0000\u0425\u0427\u0005\u00a3\u0000\u0000\u0426\u0425\u0001\u0000\u0000"+
		"\u0000\u0426\u0427\u0001\u0000\u0000\u0000\u0427\u0429\u0001\u0000\u0000"+
		"\u0000\u0428\u042a\u0005\u00a3\u0000\u0000\u0429\u0428\u0001\u0000\u0000"+
		"\u0000\u0429\u042a\u0001\u0000\u0000\u0000\u042a\u042b\u0001\u0000\u0000"+
		"\u0000\u042b\u042c\u0005\u0002\u0000\u0000\u042c\u008b\u0001\u0000\u0000"+
		"\u0000\u042d\u042e\u0005\u0001\u0000\u0000\u042e\u0430\u0005\u0095\u0000"+
		"\u0000\u042f\u0431\u0005\u00a3\u0000\u0000\u0430\u042f\u0001\u0000\u0000"+
		"\u0000\u0430\u0431\u0001\u0000\u0000\u0000\u0431\u0435\u0001\u0000\u0000"+
		"\u0000\u0432\u0434\u0003\u0088D\u0000\u0433\u0432\u0001\u0000\u0000\u0000"+
		"\u0434\u0437\u0001\u0000\u0000\u0000\u0435\u0433\u0001\u0000\u0000\u0000"+
		"\u0435\u0436\u0001\u0000\u0000\u0000\u0436\u0438\u0001\u0000\u0000\u0000"+
		"\u0437\u0435\u0001\u0000\u0000\u0000\u0438\u044e\u0005\u0002\u0000\u0000"+
		"\u0439\u043a\u0005\u0001\u0000\u0000\u043a\u043c\u0005\u00a1\u0000\u0000"+
		"\u043b\u043d\u0005\u00a3\u0000\u0000\u043c\u043b\u0001\u0000\u0000\u0000"+
		"\u043c\u043d\u0001\u0000\u0000\u0000\u043d\u043e\u0001\u0000\u0000\u0000"+
		"\u043e\u043f\u0005\u0006\u0000\u0000\u043f\u044e\u0005\u0002\u0000\u0000"+
		"\u0440\u0441\u0005\u0001\u0000\u0000\u0441\u0443\u0005\u00a2\u0000\u0000"+
		"\u0442\u0444\u0005\u00a3\u0000\u0000\u0443\u0442\u0001\u0000\u0000\u0000"+
		"\u0443\u0444\u0001\u0000\u0000\u0000\u0444\u0445\u0001\u0000\u0000\u0000"+
		"\u0445\u0446\u0005\u0006\u0000\u0000\u0446\u044e\u0005\u0002\u0000\u0000"+
		"\u0447\u0448\u0005\u0001\u0000\u0000\u0448\u044a\u0005\u00a2\u0000\u0000"+
		"\u0449\u044b\u0005\u00a3\u0000\u0000\u044a\u0449\u0001\u0000\u0000\u0000"+
		"\u044a\u044b\u0001\u0000\u0000\u0000\u044b\u044c\u0001\u0000\u0000\u0000"+
		"\u044c\u044e\u0005\u0002\u0000\u0000\u044d\u042d\u0001\u0000\u0000\u0000"+
		"\u044d\u0439\u0001\u0000\u0000\u0000\u044d\u0440\u0001\u0000\u0000\u0000"+
		"\u044d\u0447\u0001\u0000\u0000\u0000\u044e\u008d\u0001\u0000\u0000\u0000"+
		"\u044f\u0450\u0005\u0001\u0000\u0000\u0450\u0451\u0005\b\u0000\u0000\u0451"+
		"\u0452\u0003\u001e\u000f\u0000\u0452\u0453\u0005\u0002\u0000\u0000\u0453"+
		"\u008f\u0001\u0000\u0000\u0000\u0454\u0456\u0003\u008eG\u0000\u0455\u0454"+
		"\u0001\u0000\u0000\u0000\u0456\u0459\u0001\u0000\u0000\u0000\u0457\u0455"+
		"\u0001\u0000\u0000\u0000\u0457\u0458\u0001\u0000\u0000\u0000\u0458\u0091"+
		"\u0001\u0000\u0000\u0000\u0459\u0457\u0001\u0000\u0000\u0000\u045a\u045c"+
		"\u0003\u0088D\u0000\u045b\u045a\u0001\u0000\u0000\u0000\u045c\u045f\u0001"+
		"\u0000\u0000\u0000\u045d\u045b\u0001\u0000\u0000\u0000\u045d\u045e\u0001"+
		"\u0000\u0000\u0000\u045e\u0460\u0001\u0000\u0000\u0000\u045f\u045d\u0001"+
		"\u0000\u0000\u0000\u0460\u0469\u0005\u0000\u0000\u0001\u0461\u0463\u0003"+
		"~?\u0000\u0462\u0461\u0001\u0000\u0000\u0000\u0463\u0464\u0001\u0000\u0000"+
		"\u0000\u0464\u0462\u0001\u0000\u0000\u0000\u0464\u0465\u0001\u0000\u0000"+
		"\u0000\u0465\u0466\u0001\u0000\u0000\u0000\u0466\u0467\u0005\u0000\u0000"+
		"\u0001\u0467\u0469\u0001\u0000\u0000\u0000\u0468\u045d\u0001\u0000\u0000"+
		"\u0000\u0468\u0462\u0001\u0000\u0000\u0000\u0469\u0093\u0001\u0000\u0000"+
		"\u0000\u046a\u046b\u0003\u0080@\u0000\u046b\u046c\u0005\u0000\u0000\u0001"+
		"\u046c\u0475\u0001\u0000\u0000\u0000\u046d\u046f\u0003~?\u0000\u046e\u046d"+
		"\u0001\u0000\u0000\u0000\u046f\u0472\u0001\u0000\u0000\u0000\u0470\u046e"+
		"\u0001\u0000\u0000\u0000\u0470\u0471\u0001\u0000\u0000\u0000\u0471\u0473"+
		"\u0001\u0000\u0000\u0000\u0472\u0470\u0001\u0000\u0000\u0000\u0473\u0475"+
		"\u0005\u0000\u0000\u0001\u0474\u046a\u0001\u0000\u0000\u0000\u0474\u0470"+
		"\u0001\u0000\u0000\u0000\u0475\u0095\u0001\u0000\u0000\u0000{\u00a3\u00aa"+
		"\u00af\u00b7\u00c3\u00ca\u00d0\u00d5\u00dd\u00e3\u00eb\u00f1\u0102\u0110"+
		"\u0123\u0126\u012a\u012d\u014f\u0156\u016a\u016f\u0176\u017b\u017e\u0185"+
		"\u018b\u0193\u0199\u01a1\u01a7\u01b1\u01b7\u01be\u01c3\u01c7\u01cc\u01d0"+
		"\u01d5\u01d8\u01dc\u01e4\u01eb\u01f1\u01fe\u0207\u020c\u0211\u0217\u0222"+
		"\u0224\u0227\u0230\u0236\u0240\u0246\u024c\u0252\u0256\u025d\u0263\u0268"+
		"\u026f\u0279\u027f\u0284\u028f\u0294\u029c\u02a4\u02aa\u02b2\u02b9\u02be"+
		"\u02d0\u02d5\u02da\u02e2\u02ea\u02f0\u02f5\u02fa\u030b\u030f\u0314\u0322"+
		"\u0327\u032f\u0337\u033f\u0347\u034c\u036f\u037f\u0388\u039d\u03a2\u03a7"+
		"\u03b0\u03b6\u03be\u03c4\u03c8\u03cd\u03d6\u03db\u0411\u041a\u0420\u0426"+
		"\u0429\u0430\u0435\u043c\u0443\u044a\u044d\u0457\u045d\u0464\u0468\u0470"+
		"\u0474";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}