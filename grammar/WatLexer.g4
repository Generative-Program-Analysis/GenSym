/*
Copyright (c) 2019 Renata Hodovan.
All rights reserved.

Modified by Guannan Wei (2023).

Modified by Mikail Khan (2023).

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

3. Neither the name of the copyright holder nor the names of its contributors
   may be used to endorse or promote products derived from this software
   without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

lexer grammar WatLexer;

LPAR : '(' ;
RPAR : ')' ;

NAT : Nat ;
INT : Int ;
FLOAT : Float ;
STRING_ : String_ ;
VALUE_TYPE : NXX ;
CONST : NXX '.const' ;
SYMBOLIC : NXX '.symbolic' ;

FUNCREF: 'funcref' ;
EXTERNREF: 'externref' ;
MUT: 'mut' ;
REF: 'ref' ;
CONT: 'cont' ;
NULL: 'null' ;

NOP: 'nop' ;
SYM_ASSERT: 'sym_assert' ;
ALLOC: 'alloc' ;
FREE: 'free' ;
UNREACHABLE: 'unreachable' ;
DROP: 'drop' ;
BLOCK: 'block' ;
LOOP: 'loop' ;
FOR : 'for';
VBAR: '|';
END: 'end' ;
BR: 'br' ;
BR_IF: 'br_if' ;
BR_TABLE: 'br_table' ;
RETURN: 'return' ;
IF: 'if' ;
THEN: 'then' ;
ELSE: 'else' ;
SELECT: '.select' ;
CALL: 'call' ;
CALL_INDIRECT: 'call_indirect' ;
RETURN_CALL: 'return_call' ;
RETURN_CALL_INDIRECT: 'return_call_indirect' ;
REFFUNC: 'ref.func' ;
CALLREF: 'call_ref' ;
RESUME: 'resume' ;
ON: 'on' ;
CONTNEW: 'cont.new' ;
CONTBIND: 'cont.bind' ;
SUSPEND: 'suspend' ;
REFNULL: 'ref.null' ;
REFISNULL: 'ref.is_null' ;

// resumable try-catch extension:
TRY: 'try' ;
CATCH: 'catch' ;
THROW: 'throw' ;
RESUME0: 'resume0' ;

LOCAL_GET: 'local.get' ;
LOCAL_SET: 'local.set' ;
LOCAL_TEE: 'local.tee' ;
GLOBAL_GET: 'global.get' ;
GLOBAL_SET: 'global.set' ;

LOAD : '.load' (MEM_SIZE UNDERSCORE SIGN_POSTFIX)?;

STORE : '.store' MEM_SIZE? ;
UNDERSCORE : '_';

OFFSET_EQ : 'offset=' ;
ALIGN_EQ : 'align=' ;

SIGN_POSTFIX : 's' | 'u';

MEM_SIZE : '8' | '16' | '32' | '64';

I32 : 'i32';
I64 : 'i64';

F32 : 'f32';
F64 : 'f64';

IXX : I32 | I64;
FXX : F32 | F64;

OP_EQZ : '.eqz';

OP_EQ  : '.eq';
OP_NE  : '.ne';
OP_LT  : '.lt';
OP_LTS : '.lt_s';
OP_LTU : '.lt_u';
OP_LE  : '.le';
OP_LES : '.le_s';
OP_LEU : '.le_u';
OP_GT  : '.gt';
OP_GTS : '.gt_s';
OP_GTU : '.gt_u';
OP_GE  : '.ge';
OP_GES : '.ge_s';
OP_GEU : '.ge_u';

OP_CLZ : '.clz';
OP_CTZ : '.ctz';
OP_POPCNT : '.popcnt';
OP_NEG : '.neg';
OP_ABS : '.abs';
OP_SQRT : '.sqrt';
OP_CEIL : '.ceil';
OP_FLOOR : '.floor';
OP_TRUNC : '.trunc';
OP_NEAREST : '.nearest';

OP_ADD : '.add';
OP_SUB : '.sub';
OP_MUL : '.mul';
OP_DIV : '.div';
OP_DIV_S : '.div_s';
OP_DIV_U : '.div_u';
OP_REM_S : '.rem_s';
OP_REM_U : '.rem_u';
OP_AND : '.and';
OP_OR : '.or';
OP_XOR : '.xor';
OP_SHL : '.shl';
OP_SHR_S : '.shr_s';
OP_SHR_U : '.shr_u';
OP_ROTL : '.rotl';
OP_ROTR :  '.rotr';
OP_MIN : '.min';
OP_MAX : '.max';
OP_COPYSIGN : '.copysign';

OP_WRAP : '.wrap_';
OP_TRUNC_ : '.trunc_';
OP_TRUNC_SAT : '.trunc_sat_';
OP_CONVERT : '.convert_';
OP_EXTEND : '.extend_';
OP_DEMOTE : '.demote_';
OP_PROMOTE : '.promote_';
OP_REINTER : '.reinterpret_';

MEMORY_SIZE : 'memory.size' ;
MEMORY_GROW : 'memory.grow' ;
MEMORY_FILL : 'memory.fill' ;
MEMORY_COPY : 'memory.copy' ;
MEMORY_INIT : 'memory.init' ;

TEST : IXX OP_EQZ;

COMPARE :
    IXX '.eq'
  | IXX '.ne'
  | IXX '.lt_s'
  | IXX '.lt_u'
  | IXX '.le_s'
  | IXX '.le_u'
  | IXX '.gt_s'
  | IXX '.gt_u'
  | IXX '.ge_s'
  | IXX '.ge_u'
  | FXX '.eq'
  | FXX '.ne'
  | FXX '.lt'
  | FXX '.le'
  | FXX '.gt'
  | FXX '.ge'
  ;

UNARY
  : IXX '.clz'
  | IXX '.ctz'
  | IXX '.popcnt'
  | FXX '.neg'
  | FXX '.abs'
  | FXX '.sqrt'
  | FXX '.ceil'
  | FXX '.floor'
  | FXX '.trunc'
  | FXX '.nearest'
  ;

BINARY
  : IXX '.add'
  | IXX '.sub'
  | IXX '.mul'
  | IXX '.div_s'
  | IXX '.div_u'
  | IXX '.rem_s'
  | IXX '.rem_u'
  | IXX '.and'
  | IXX '.or'
  | IXX '.xor'
  | IXX '.shl'
  | IXX '.shr_s'
  | IXX '.shr_u'
  | IXX '.rotl'
  | IXX '.rotr'
  | FXX '.add'
  | FXX '.sub'
  | FXX '.mul'
  | FXX '.div'
  | FXX '.min'
  | FXX '.max'
  | FXX '.copysign'
  ;

CONVERT
  : I32 '.wrap_' I64
  | IXX '.trunc_' FXX UNDERSCORE SIGN_POSTFIX
  | IXX '.trunc_sat_' FXX UNDERSCORE SIGN_POSTFIX
  | I64 '.extend_' I32 UNDERSCORE SIGN_POSTFIX
  | FXX '.convert_' IXX UNDERSCORE SIGN_POSTFIX
  | F32 '.demote_' F64
  | F64 '.promote_' F32
  | F32 '.reinterpret_' I32
  | F64 '.reinterpret_' I64
  | I32 '.reinterpret_' F32
  | I64 '.reinterpret_' F64
  ;

TYPE: 'type' ;
FUNC: 'func' ;
EXTERN: 'extern' ;
START_: 'start' ;
PARAM: 'param' ;
RESULT: 'result' ;
LOCAL: 'local' ;
GLOBAL: 'global' ;
TABLE: 'table' ;
MEMORY: 'memory' ;
ELEM: 'elem' ;
DATA: 'data' ;
OFFSET: 'offset' ;
IMPORT: 'import' ;
EXPORT: 'export' ;
TAG: 'tag' ;

DECLARE: 'declare' ;

MODULE : 'module' ;
BIN : 'binary' ;
QUOTE : 'quote' ;
DEFINITION : 'definition' ;
INSTANCE : 'instance' ;

SCRIPT: 'script' ;
REGISTER: 'register' ;
INVOKE: 'invoke' ;
GET: 'get' ;
ASSERT_MALFORMED: 'assert_malformed' ;
ASSERT_INVALID: 'assert_invalid' ;
ASSERT_UNLINKABLE: 'assert_unlinkable' ;
ASSERT_RETURN: 'assert_return' ;
ASSERT_RETURN_CANONICAL_NAN: 'assert_return_canonical_nan' ;
ASSERT_RETURN_ARITHMETIC_NAN: 'assert_return_arithmetic_nan' ;
ASSERT_TRAP: 'assert_trap' ;
ASSERT_EXHAUSTION: 'assert_exhaustion' ;
INPUT: 'input' ;
OUTPUT: 'output' ;

VAR : Name ;

V128 : 'v128' ;

SPACE
  : [ \t\r\n]+ -> skip
  ;

COMMENT
  : ( '(;' .*? ';)'
  | ';;' .*? '\n')-> skip
  ;

fragment Symbol
  : '.' | '+' | '-' | '*' | '/' | '\\' | '^' | '~' | '=' | '<' | '>' | '!' | '?' | '@' | '#' | '$' | '%' | '&' | '|' | ':' | '\'' | '`'
  ;

fragment Num
  : Digit ('_'? Digit)*
  ;

fragment HexNum
  : HexDigit ('_'? HexDigit)*
  ;

fragment Sign
  : '+' | '-'
  ;

fragment Digit
  : [0-9]
  ;

fragment HexDigit
  : [0-9a-fA-F]
  ;

fragment Letter
  : [a-zA-Z]
  ;

fragment Nat : Num | ('0x' HexNum) ;
fragment Int : Sign Nat ;
fragment Frac : Num ;
fragment HexFrac : HexNum ;

fragment Float
  : Sign? Num '.' Frac?
  | Sign? Num ('.' Frac?)? ('e' | 'E') Sign? Num
  | Sign? '0x' HexNum '.' HexFrac?
  | Sign? '0x' HexNum ('.' HexFrac?)? ('p' | 'P') Sign? Num
  | Sign? 'inf'
  | Sign? 'nan'
  | Sign? 'nan:' '0x' HexNum
  ;

fragment String_
  : '"' ( Char | '\n' | '\t' | '\\' | '\'' | '\\' HexDigit HexDigit | '\\u{' HexDigit+ '}' )* '"'
  ;

fragment Name
  : '$' (Letter | Digit | '_' | Symbol)+
  ;

fragment Escape : [nrt'"\\] ;

fragment NXX : IXX | FXX ;
/* fragment MIXX : 'i' ('8' | '16' | '32' | '64') ; */
/* fragment MFXX : 'f' ('32' | '64') ; */
/* fragment SIGN : 's' | 'u' ; */

fragment Char : ~["'\\\u0000-\u001f\u007f-\u00ff] ;
fragment Ascii : [\u0000-\u007f] ;
fragment Ascii_no_nl : [\u0000-\u0009\u000b-\u007f] ;
fragment Utf8Cont : [\u0080-\u00bf] ;
fragment Utf8 : Ascii | Utf8Enc ;
fragment Utf8_no_nl : Ascii_no_nl | Utf8Enc ;

fragment Utf8Enc
  : [\u00c2-\u00df] Utf8Cont
  | [\u00e0] [\u00a0-\u00bf] Utf8Cont
  | [\u00ed] [\u0080-\u009f] Utf8Cont
  | [\u00e1-\u00ec\u00ee-\u00ef] Utf8Cont Utf8Cont
  | [\u00f0] [\u0090-\u00bf] Utf8Cont Utf8Cont
  | [\u00f4] [\u0080-\u008f] Utf8Cont Utf8Cont
  | [\u00f1-\u00f3] Utf8Cont Utf8Cont Utf8Cont
  ;
