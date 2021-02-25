// https://gist.githubusercontent.com/rindPHI/fa50284b1176a37ddb1fdef3e0025768/raw/323ed65893ad2d9ba6ea1511b293c078a86c4929/LLVMLexer.g4

// This ANTLR4 lexer grammar is based on the lexer part of an LLVM BNF grammar from
// https://gist.github.com/mewmew/a2487392d5519ef49658fd8f84d9eed5,
// which in turn has been based on the source code of the official LLVM project,
// as of 2018-02-19 (rev db070bbdacd303ae7da129f59beaf35024d94c53).

lexer grammar LLVMLexer;

LT : '<' ;
EQSIGN : '=' ;
GT : '>' ;
VDASH : '|' ;
COMMA : ',' ;
BANG : '!' ;
DOTS : '...' ;
LPAREN : '(' ;
RPAREN : ')' ;
LBRACK : '[' ;
RBRACK : ']' ;
LBRACE : '{' ;
RBRACE : '}' ;
STAR : '*' ;
ACQ_REL : 'acq_rel' ;
ACQUIRE : 'acquire' ;
ADD : 'add' ;
ADDRSPACE : 'addrspace' ;
ADDRSPACECAST : 'addrspacecast' ;
AFN : 'afn' ;
ALIAS : 'alias' ;
ALIGNCOLON : 'align:' ;
ALIGN : 'align' ;
ALIGNSTACK : 'alignstack' ;
ALLOCA : 'alloca' ;
ALLOCSIZE : 'allocsize' ;
ALWAYSINLINE : 'alwaysinline' ;
AMDGPU_CS : 'amdgpu_cs' ;
AMDGPU_ES : 'amdgpu_es' ;
AMDGPU_GS : 'amdgpu_gs' ;
AMDGPU_HS : 'amdgpu_hs' ;
AMDGPU_KERNEL : 'amdgpu_kernel' ;
AMDGPU_LS : 'amdgpu_ls' ;
AMDGPU_PS : 'amdgpu_ps' ;
AMDGPU_VS : 'amdgpu_vs' ;
AND : 'and' ;
ANY : 'any' ;
ANYREGCC : 'anyregcc' ;
APPENDING : 'appending' ;
ARCP : 'arcp' ;
ARGCOLON : 'arg:' ;
ARGMEMONLY : 'argmemonly' ;
ARM_AAPCSCC : 'arm_aapcscc' ;
ARM_AAPCS_VFPCC : 'arm_aapcs_vfpcc' ;
ARM_APCSCC : 'arm_apcscc' ;
ASHR : 'ashr' ;
ASM : 'asm' ;
ATOMIC : 'atomic' ;
ATOMICRMW : 'atomicrmw' ;
ATTRIBUTESCOLON : 'attributes:' ;
ATTRIBUTES : 'attributes' ;
AVAILABLE_EXTERNALLY : 'available_externally' ;
AVR_INTRCC : 'avr_intrcc' ;
AVR_SIGNALCC : 'avr_signalcc' ;
BASETYPECOLON : 'baseType:' ;
BITCAST : 'bitcast' ;
BLOCKADDRESS : 'blockaddress' ;
BR : 'br' ;
BUILTIN : 'builtin' ;
BYVAL : 'byval' ;
C : 'c' ;
CALL : 'call' ;
CALLER : 'caller' ;
CATCH : 'catch' ;
CATCHPAD : 'catchpad' ;
CATCHRET : 'catchret' ;
CATCHSWITCH : 'catchswitch' ;
CCCOLON : 'cc:' ;
CC : 'cc' ;
CCC : 'ccc' ;
CHECKSUMCOLON : 'checksum:' ;
CHECKSUMKINDCOLON : 'checksumkind:' ;
CLEANUP : 'cleanup' ;
CLEANUPPAD : 'cleanuppad' ;
CLEANUPRET : 'cleanupret' ;
CMPXCHG : 'cmpxchg' ;
COLD : 'cold' ;
COLDCC : 'coldcc' ;
COLUMNCOLON : 'column:' ;
COMDAT : 'comdat' ;
COMMON : 'common' ;
CONFIGMACROSCOLON : 'configMacros:' ;
CONSTANT : 'constant' ;
CONTAININGTYPECOLON : 'containingType:' ;
CONTRACT : 'contract' ;
CONVERGENT : 'convergent' ;
COUNTCOLON : 'count:' ;
CXX_FAST_TLSCC : 'cxx_fast_tlscc' ;
DATALAYOUT : 'datalayout' ;
DEBUGINFOFORPROFILINGCOLON : 'debugInfoForProfiling:' ;
DECLARATIONCOLON : 'declaration:' ;
DECLARE : 'declare' ;
DEFAULT : 'default' ;
DEFINE : 'define' ;
DEREFERENCEABLE : 'dereferenceable' ;
DEREFERENCEABLE_OR_NULL : 'dereferenceable_or_null' ;
NOTDIBASICTYPE : '!DIBasicType' ;
NOTDICOMPILEUNIT : '!DICompileUnit' ;
NOTDICOMPOSITETYPE : '!DICompositeType' ;
NOTDIDERIVEDTYPE : '!DIDerivedType' ;
NOTDIENUMERATOR : '!DIEnumerator' ;
NOTDIEXPRESSION : '!DIExpression' ;
NOTDIFILE : '!DIFile' ;
NOTDIGLOBALVARIABLE : '!DIGlobalVariable' ;
NOTDIGLOBALVARIABLEEXPRESSION : '!DIGlobalVariableExpression' ;
NOTDIIMPORTEDENTITY : '!DIImportedEntity' ;
NOTDILEXICALBLOCK : '!DILexicalBlock' ;
NOTDILEXICALBLOCKFILE : '!DILexicalBlockFile' ;
NOTDILOCALVARIABLE : '!DILocalVariable' ;
NOTDILOCATION : '!DILocation' ;
NOTDIMACRO : '!DIMacro' ;
NOTDIMACROFILE : '!DIMacroFile' ;
NOTDIMODULE : '!DIModule' ;
NOTDINAMESPACE : '!DINamespace' ;
NOTDIOBJCPROPERTY : '!DIObjCProperty' ;
DIRECTORYCOLON : 'directory:' ;
DISCRIMINATORCOLON : 'discriminator:' ;
DISTINCT : 'distinct' ;
NOTDISUBPROGRAM : '!DISubprogram' ;
NOTDISUBRANGE : '!DISubrange' ;
NOTDISUBROUTINETYPE : '!DISubroutineType' ;
NOTDITEMPLATETYPEPARAMETER : '!DITemplateTypeParameter' ;
NOTDITEMPLATEVALUEPARAMETER : '!DITemplateValueParameter' ;
DLLEXPORT : 'dllexport' ;
DLLIMPORT : 'dllimport' ;
DOUBLE : 'double' ;
DSO_LOCAL : 'dso_local' ;
DSO_PREEMPTABLE : 'dso_preemptable' ;
DWARFADDRESSSPACECOLON : 'dwarfAddressSpace:' ;
DWOIDCOLON : 'dwoId:' ;
ELEMENTSCOLON : 'elements:' ;
EMISSIONKINDCOLON : 'emissionKind:' ;
ENCODINGCOLON : 'encoding:' ;
ENTITYCOLON : 'entity:' ;
ENUMSCOLON : 'enums:' ;
EQ : 'eq' ;
EXACT : 'exact' ;
EXACTMATCH : 'exactmatch' ;
EXPORTSYMBOLSCOLON : 'exportSymbols:' ;
EXPRCOLON : 'expr:' ;
EXTERNAL : 'external' ;
EXTERNALLY_INITIALIZED : 'externally_initialized' ;
EXTERN_WEAK : 'extern_weak' ;
EXTRACTELEMENT : 'extractelement' ;
EXTRACTVALUE : 'extractvalue' ;
EXTRADATACOLON : 'extraData:' ;
FADD : 'fadd' ;
FALSE : 'false' ;
FAST : 'fast' ;
FASTCC : 'fastcc' ;
FCMP : 'fcmp' ;
FDIV : 'fdiv' ;
FENCE : 'fence' ;
FILECOLON : 'file:' ;
FILENAMECOLON : 'filename:' ;
FILTER : 'filter' ;
FLAGSCOLON : 'flags:' ;
FLOAT : 'float' ;
FMUL : 'fmul' ;
FP128 : 'fp128' ;
FPEXT : 'fpext' ;
FPTOSI : 'fptosi' ;
FPTOUI : 'fptoui' ;
FPTRUNC : 'fptrunc' ;
FREM : 'frem' ;
FROM : 'from' ;
FSUB : 'fsub' ;
FULLDEBUG : 'FullDebug' ;
GC : 'gc' ;
NOTGENERICDINODE : '!GenericDINode' ;
GETELEMENTPTR : 'getelementptr' ;
GETTERCOLON : 'getter:' ;
GHCCC : 'ghccc' ;
GLOBAL : 'global' ;
GLOBALSCOLON : 'globals:' ;
GNUPUBNAMESCOLON : 'gnuPubnames:' ;
HALF : 'half' ;
HEADERCOLON : 'header:' ;
HHVMCC : 'hhvmcc' ;
HHVM_CCC : 'hhvm_ccc' ;
HIDDEN_VISIB : 'hidden' ;
ICMP : 'icmp' ;
IDENTIFIERCOLON : 'identifier:' ;
IFUNC : 'ifunc' ;
IMMARG : 'immarg' ;
IMPORTSCOLON : 'imports:' ;
INACCESSIBLEMEMONLY : 'inaccessiblememonly' ;
INACCESSIBLEMEM_OR_ARGMEMONLY : 'inaccessiblemem_or_argmemonly' ;
INALLOCA : 'inalloca' ;
INBOUNDS : 'inbounds' ;
INCLUDEPATHCOLON : 'includePath:' ;
INDIRECTBR : 'indirectbr' ;
INITIALEXEC : 'initialexec' ;
INLINEDATCOLON : 'inlinedAt:' ;
INLINEHINT : 'inlinehint' ;
INRANGE : 'inrange' ;
INREG : 'inreg' ;
INSERTELEMENT : 'insertelement' ;
INSERTVALUE : 'insertvalue' ;
INTELDIALECT : 'inteldialect' ;
INTEL_OCL_BICC : 'intel_ocl_bicc' ;
INTERNAL : 'internal' ;
INTTOPTR : 'inttoptr' ;
INVOKE : 'invoke' ;
ISDEFINITIONCOLON : 'isDefinition:' ;
ISLOCALCOLON : 'isLocal:' ;
ISOPTIMIZEDCOLON : 'isOptimized:' ;
ISUNSIGNEDCOLON : 'isUnsigned:' ;
ISYSROOTCOLON : 'isysroot:' ;
JUMPTABLE : 'jumptable' ;
LABEL : 'label' ;
LANDINGPAD : 'landingpad' ;
LANGUAGECOLON : 'language:' ;
LARGEST : 'largest' ;
LINECOLON : 'line:' ;
LINETABLESONLY : 'LineTablesOnly' ;
LINKAGENAMECOLON : 'linkageName:' ;
LINKONCE : 'linkonce' ;
LINKONCE_ODR : 'linkonce_odr' ;
LOAD : 'load' ;
LOCALDYNAMIC : 'localdynamic' ;
LOCALEXEC : 'localexec' ;
LOCAL_UNNAMED_ADDR : 'local_unnamed_addr' ;
LOWERBOUNDCOLON : 'lowerBound:' ;
LSHR : 'lshr' ;
MACROSCOLON : 'macros:' ;
MAX : 'max' ;
METADATA : 'metadata' ;
MIN : 'min' ;
MINSIZE : 'minsize' ;
MODULE : 'module' ;
MONOTONIC : 'monotonic' ;
MSP430_INTRCC : 'msp430_intrcc' ;
MUL : 'mul' ;
MUSTTAIL : 'musttail' ;
NAKED : 'naked' ;
NAMECOLON : 'name:' ;
NAND : 'nand' ;
NE : 'ne' ;
NEST : 'nest' ;
NINF : 'ninf' ;
NNAN : 'nnan' ;
NOALIAS : 'noalias' ;
NOBUILTIN : 'nobuiltin' ;
NOCAPTURE : 'nocapture' ;
NODEBUG : 'NoDebug' ;
NODESCOLON : 'nodes:' ;
NODUPLICATE : 'noduplicate' ;
NODUPLICATES : 'noduplicates' ;
NOIMPLICITFLOAT : 'noimplicitfloat' ;
NOINLINE : 'noinline' ;
NONE : 'none' ;
NONLAZYBIND : 'nonlazybind' ;
NONNULL : 'nonnull' ;
NORECURSE : 'norecurse' ;
NOREDZONE : 'noredzone' ;
NORETURN : 'noreturn' ;
NOTAIL : 'notail' ;
NOUNWIND : 'nounwind' ;
NSW : 'nsw' ;
NSZ : 'nsz' ;
NULL : 'null' ;
NUW : 'nuw' ;
OEQ : 'oeq' ;
OFFSETCOLON : 'offset:' ;
OGE : 'oge' ;
OGT : 'ogt' ;
OLE : 'ole' ;
OLT : 'olt' ;
ONE : 'one' ;
OPAQUE : 'opaque' ;
OPERANDSCOLON : 'operands:' ;
OPTNONE : 'optnone' ;
OPTSIZE : 'optsize' ;
OR : 'or' ;
ORD : 'ord' ;
PERSONALITY : 'personality' ;
PHI : 'phi' ;
PPC_FP128 : 'ppc_fp128' ;
PREFIX : 'prefix' ;
PRESERVE_ALLCC : 'preserve_allcc' ;
PRESERVE_MOSTCC : 'preserve_mostcc' ;
PRIVATE : 'private' ;
PRODUCERCOLON : 'producer:' ;
PROLOGUE : 'prologue' ;
PROTECTED : 'protected' ;
PTRTOINT : 'ptrtoint' ;
PTX_DEVICE : 'ptx_device' ;
PTX_KERNEL : 'ptx_kernel' ;
READNONE : 'readnone' ;
READONLY : 'readonly' ;
REASSOC : 'reassoc' ;
RELEASE : 'release' ;
RESUME : 'resume' ;
RET : 'ret' ;
RETAINEDTYPESCOLON : 'retainedTypes:' ;
RETURNED : 'returned' ;
RETURNS_TWICE : 'returns_twice' ;
RUNTIMELANGCOLON : 'runtimeLang:' ;
RUNTIMEVERSIONCOLON : 'runtimeVersion:' ;
SAFESTACK : 'safestack' ;
SAMESIZE : 'samesize' ;
SANITIZE_ADDRESS : 'sanitize_address' ;
SANITIZE_HWADDRESS : 'sanitize_hwaddress' ;
SANITIZE_MEMORY : 'sanitize_memory' ;
SANITIZE_THREAD : 'sanitize_thread' ;
SCOPECOLON : 'scope:' ;
SCOPELINECOLON : 'scopeLine:' ;
SDIV : 'sdiv' ;
SECTION : 'section' ;
SELECT : 'select' ;
SEQ_CST : 'seq_cst' ;
SETTERCOLON : 'setter:' ;
SEXT : 'sext' ;
SGE : 'sge' ;
SGT : 'sgt' ;
SHL : 'shl' ;
SHUFFLEVECTOR : 'shufflevector' ;
SIDEEFFECT : 'sideeffect' ;
SIGNEXT : 'signext' ;
SITOFP : 'sitofp' ;
SIZECOLON : 'size:' ;
SLE : 'sle' ;
SLT : 'slt' ;
SOURCE_FILENAME : 'source_filename' ;
SPECULATABLE : 'speculatable' ;
SPIR_FUNC : 'spir_func' ;
SPIR_KERNEL : 'spir_kernel' ;
SPLITDEBUGFILENAMECOLON : 'splitDebugFilename:' ;
SPLITDEBUGINLININGCOLON : 'splitDebugInlining:' ;
SREM : 'srem' ;
SRET : 'sret' ;
SSP : 'ssp' ;
SSPREQ : 'sspreq' ;
SSPSTRONG : 'sspstrong' ;
STORE : 'store' ;
STRICTFP : 'strictfp' ;
SUB : 'sub' ;
SWIFTCC : 'swiftcc' ;
SWIFTERROR : 'swifterror' ;
SWIFTSELF : 'swiftself' ;
SWITCH : 'switch' ;
SYNCSCOPE : 'syncscope' ;
TAGCOLON : 'tag:' ;
TAIL : 'tail' ;
TARGET : 'target' ;
TEMPLATEPARAMSCOLON : 'templateParams:' ;
THISADJUSTMENTCOLON : 'thisAdjustment:' ;
THREAD_LOCAL : 'thread_local' ;
THROWNTYPESCOLON : 'thrownTypes:' ;
TO : 'to' ;
TOKEN : 'token' ;
TRIPLE : 'triple' ;
TRUE : 'true' ;
TRUNC : 'trunc' ;
TYPECOLON : 'type:' ;
TYPE : 'type' ;
TYPESCOLON : 'types:' ;
UDIV : 'udiv' ;
UEQ : 'ueq' ;
UGE : 'uge' ;
UGT : 'ugt' ;
UITOFP : 'uitofp' ;
ULE : 'ule' ;
ULT : 'ult' ;
UMAX : 'umax' ;
UMIN : 'umin' ;
UNDEF : 'undef' ;
UNE : 'une' ;
UNITCOLON : 'unit:' ;
UNNAMED_ADDR : 'unnamed_addr' ;
UNO : 'uno' ;
UNORDERED : 'unordered' ;
UNREACHABLE : 'unreachable' ;
UNWIND : 'unwind' ;
UREM : 'urem' ;
USELISTORDER : 'uselistorder' ;
USELISTORDER_BB : 'uselistorder_bb' ;
UWTABLE : 'uwtable' ;
VA_ARG : 'va_arg' ;
VALUECOLON : 'value:' ;
VARCOLON : 'var:' ;
VARIABLESCOLON : 'variables:' ;
VIRTUALINDEXCOLON : 'virtualIndex:' ;
VIRTUALITYCOLON : 'virtuality:' ;
VOID : 'void' ;
VTABLEHOLDERCOLON : 'vtableHolder:' ;
WEAK : 'weak' ;
WEAK_ODR : 'weak_odr' ;
WEBKIT_JSCC : 'webkit_jscc' ;
WIN64CC : 'win64cc' ;
WITHIN : 'within' ;
WRITEONLY : 'writeonly' ;
X : 'x' ;
X86_64_SYSVCC : 'x86_64_sysvcc' ;
X86_FASTCALLCC : 'x86_fastcallcc' ;
X86_FP80 : 'x86_fp80' ;
X86_INTRCC : 'x86_intrcc' ;
X86_MMX : 'x86_mmx' ;
X86_REGCALLCC : 'x86_regcallcc' ;
X86_STDCALLCC : 'x86_stdcallcc' ;
X86_THISCALLCC : 'x86_thiscallcc' ;
X86_VECTORCALLCC : 'x86_vectorcallcc' ;
XCHG : 'xchg' ;
XOR : 'xor' ;
ZEROEXT : 'zeroext' ;
ZEROINITIALIZER : 'zeroinitializer' ;
ZEXT : 'zext' ;
VOLATILE : 'volatile' ;

// ==============================================================================

fragment ASCII_LETTER_UPPER
    : 'A'..'Z'
;

fragment ASCII_LETTER_LOWER
    : 'a'..'z'
;

fragment ASCII_LETTER
    : ASCII_LETTER_UPPER
    | ASCII_LETTER_LOWER
;

fragment LETTER
    : ASCII_LETTER
    | '$'
    | '-'
    | '.'
    | '_'
;

fragment ESCAPE_LETTER
    : LETTER
    | '\\'
;

fragment DECIMAL_DIGIT
    : '0'..'9'
;

fragment HEX_DIGIT
    : DECIMAL_DIGIT
    | 'A'..'F'
    | 'a'..'f'
;

COMMENT : ';' .*? '\r' ? '\n' -> skip ;
WHITESPACE : [ \t\r\n]+ -> skip ;

// --- [ Attribute group identifiers ] -----------------------------------------

ATTR_GROUP_ID
    : '#' ID
;

// --- [ Comdat identifiers ] --------------------------------------------------

COMDAT_NAME
    : '$' ( NAME | QUOTED_NAME )
;

// --- [ Metadata identifiers ] ------------------------------------------------

METADATA_NAME
    : '!' ESCAPE_NAME
;

METADATA_ID
    : '!' ID
;

// DW_TAG_foo
DWARF_TAG
    : 'D' 'W' '_' 'T' 'A' 'G' '_' ( ASCII_LETTER | DECIMAL_DIGIT | '_' ) *
;

// DW_ATE_foo
DWARF_ATT_ENCODING
    : 'D' 'W' '_' 'A' 'T' 'E' '_' ( ASCII_LETTER | DECIMAL_DIGIT | '_' ) *
;

// DIFlagFoo
DI_FLAG
    : 'D' 'I' 'F' 'l' 'a' 'g' ( ASCII_LETTER | DECIMAL_DIGIT | '_' ) *
;

// DW_LANG_foo
DWARF_LANG
    : 'D' 'W' '_' 'L' 'A' 'N' 'G' '_' ( ASCII_LETTER | DECIMAL_DIGIT | '_' ) *
;

// DW_CC_foo
DWARF_CC
    : 'D' 'W' '_' 'C' 'C' '_' ( ASCII_LETTER | DECIMAL_DIGIT | '_' ) *
;

// CSK_foo
CHECKSUM_KIND
    : 'C' 'S' 'K' '_' ( ASCII_LETTER | DECIMAL_DIGIT | '_' ) *
;

// DW_VIRTUALITY_foo
DWARF_VIRTUALITY
    : 'D' 'W' '_' 'V' 'I' 'R' 'T' 'U' 'A' 'L' 'I' 'T' 'Y' '_' ( ASCII_LETTER | DECIMAL_DIGIT | '_' ) *
;

// DW_MACINFO_foo
DWARF_MACINFO
    : 'D' 'W' '_' 'M' 'A' 'C' 'I' 'N' 'F' 'O' '_' ( ASCII_LETTER | DECIMAL_DIGIT | '_' ) *
;

// DW_OP_foo
DWARF_OP
    : 'D' 'W' '_' 'O' 'P' '_' ( ASCII_LETTER | DECIMAL_DIGIT | '_' ) *
;

// === [ Integer literals ] ====================================================

//   Integer           [-]?[0-9]+

INT_LIT
    : DECIMAL_LIT
;

DECIMAL_LIT
    : '-' ? DECIMALS
;

DECIMALS
    : DECIMAL_DIGIT +
;

// === [ Floating-point literals ] =============================================

//   FPConstant        [-+]?[0-9]+[.][0-9]*([eE][-+]?[0-9]+)?

FLOAT_LIT
    : FRAC_LIT
    | SCI_LIT
    | FLOAT_HEX_LIT
;

FRAC_LIT
    : SIGN ? DECIMALS '.' DECIMAL_DIGIT *
;

SIGN
    : '+'
    | '-'
;

SCI_LIT
    : FRAC_LIT ( 'e' | 'E' ) SIGN ? DECIMALS
;

//   HexFPConstant     0x[0-9A-Fa-f]+     // 16 hex digits
//   HexFP80Constant   0xK[0-9A-Fa-f]+    // 20 hex digits
//   HexFP128Constant  0xL[0-9A-Fa-f]+    // 32 hex digits
//   HexPPC128Constant 0xM[0-9A-Fa-f]+    // 32 hex digits
//   HexHalfConstant   0xH[0-9A-Fa-f]+    // 4 hex digits

FLOAT_HEX_LIT
    :  '0' 'x'      HEX_DIGIT ( HEX_DIGIT ) *
    |  '0' 'x' 'K'  HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    |  '0' 'x' 'L'  HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    |  '0' 'x' 'M'  HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    |  '0' 'x' 'H'  HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
;

// === [ String literals ] =====================================================

STRING_LIT
    : QUOTED_STRING
;

QUOTED_STRING
    : '"' .*? '"'
;

// === [ Types ] ===============================================================

INT_TYPE
    : 'i' DECIMALS
;

// === [ Identifiers ] =========================================================

NAME
    : LETTER ( LETTER | DECIMAL_DIGIT ) *
;

ESCAPE_NAME
    : ESCAPE_LETTER ( ESCAPE_LETTER | DECIMAL_DIGIT ) *
;

QUOTED_NAME
    : QUOTED_STRING
;

ID
    : DECIMALS
;

// --- [ Global identifiers ] --------------------------------------------------

GLOBAL_IDENT
    : GLOBAL_NAME
    | GLOBAL_ID
;

GLOBAL_NAME
    : '@' ( NAME | QUOTED_NAME )
;

GLOBAL_ID
    : '@' ID
;

// --- [ Local identifiers ] ---------------------------------------------------

LOCAL_IDENT
    : LOCAL_NAME
    | LOCAL_ID
;

LOCAL_NAME
    : '%' ( NAME | QUOTED_NAME )
;

LOCAL_ID
    : '%' ID
;

// --- [ Labels ] --------------------------------------------------------------

//   Label             [-a-zA-Z$._0-9]+:

LABEL_IDENT
    : ( LETTER | DECIMAL_DIGIT ) ( LETTER | DECIMAL_DIGIT ) * ':'
    | QUOTED_STRING ':'
;
