// https://gist.githubusercontent.com/rindPHI/954e5f7dd0b357d1ca2125877fcfc045/raw/06d573b117818575935464007da6ef70389ff051/LLVMParser.g4

// This ANTLR4 parser grammar is based on the parser part of an LLVM BNF grammar from
// https://gist.github.com/mewmew/a2487392d5519ef49658fd8f84d9eed5,
// which in turn has been based on the source code of the official LLVM project,
// as of 2018-02-19 (rev db070bbdacd303ae7da129f59beaf35024d94c53).

//    * lib/AsmParser/LLParser.cpp

// === [ Module ] ==============================================================

// https://llvm.org/docs/LangRef.html#module-structure

// ref: Run
//
//   module ::= toplevelentity*

parser grammar LLVMParser;

options {
    tokenVocab = LLVMLexer ;
}

module
    : topLevelEntities
;

topLevelEntities
    : topLevelEntityList ?
;

topLevelEntityList
    : topLevelEntity
    | topLevelEntityList topLevelEntity
;

// --- [ Top-level Entities ] --------------------------------------------------

// ref: ParseTopLevelEntities

topLevelEntity
    : sourceFilename
    | targetDefinition
    | moduleAsm
    | typeDef
    | comdatDef
    | globalDecl
    | globalDef
    | indirectSymbolDef
    | functionDecl
    | functionDef
    | attrGroupDef
    | namedMetadataDef
    | metadataDef
    | useListOrder
    | useListOrderBB
;

// ~~~ [ Source Filename ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#source-filename

// ref: ParseSourceFileName
//
//   ::= 'source_filename' '=' STRINGCONSTANT

sourceFilename
    : 'source_filename' '=' stringLit
;

// ~~~ [ Target Definition ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#target-triple
// https://llvm.org/docs/LangRef.html#data-layout

// ref: ParseTargetDefinition
//
//   ::= 'target' 'triple' '=' STRINGCONSTANT
//   ::= 'target' 'datalayout' '=' STRINGCONSTANT

targetDefinition
    : 'target' 'datalayout' '=' stringLit
    | 'target' 'triple' '=' stringLit
;

// ~~~ [ Module-level Inline Assembly ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#module-level-inline-assembly

// ref: ParseModuleAsm
//
//   ::= 'module' 'asm' STRINGCONSTANT

moduleAsm
    : 'module' 'asm' stringLit
;

// ~~~ [ type Defintion ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#structure-type

// ref: ParseUnnamedType
//
//   ::= LocalVarID '=' 'type' type

// ref: ParseNamedType
//
//   ::= LocalVar '=' 'type' type

typeDef
    : localIdent '=' 'type' opaqueType
    | localIdent '=' 'type' llvmType
;

// ~~~ [ comdat Definition ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#langref-comdats

// ref: parseComdat

comdatDef
    : comdatName '=' 'comdat' selectionKind
;

selectionKind
    : 'any'
    | 'exactmatch'
    | 'largest'
    | 'noduplicates'
    | 'samesize'
;

// ~~~ [ Global Variable Declaration ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#global-variables

// ref: ParseUnnamedGlobal
//
//   OptionalVisibility (ALIAS | IFUNC) ...
//   OptionalLinkage OptionalPreemptionSpecifier OptionalVisibility
//   OptionalDLLStorageClass
//                                                     ...   -> global variable
//   GlobalID '=' OptionalVisibility (ALIAS | IFUNC) ...
//   GlobalID '=' OptionalLinkage OptionalPreemptionSpecifier OptionalVisibility
//                OptionalDLLStorageClass
//                                                     ...   -> global variable

// ref: ParseNamedGlobal
//
//   GlobalVar '=' OptionalVisibility (ALIAS | IFUNC) ...
//   GlobalVar '=' OptionalLinkage OptionalPreemptionSpecifier
//                 OptionalVisibility OptionalDLLStorageClass
//                                                     ...   -> global variable

// ref: ParseGlobal
//
//   ::= GlobalVar '=' OptionalLinkage OptionalPreemptionSpecifier
//       OptionalVisibility OptionalDLLStorageClass
//       OptionalThreadLocal OptionalUnnamedAddr OptionalAddrSpace
//       OptionalExternallyInitialized GlobalType type Const OptionalAttrs
//   ::= OptionalLinkage OptionalPreemptionSpecifier OptionalVisibility
//       OptionalDLLStorageClass OptionalThreadLocal OptionalUnnamedAddr
//       OptionalAddrSpace OptionalExternallyInitialized GlobalType type
//       Const OptionalAttrs

globalDecl
    : globalIdent '=' externLinkage optPreemptionSpecifier visibility ? optDLLStorageClass threadLocal ? unnamedAddr ? optAddrSpace optExternallyInitialized immutable llvmType globalAttrs funcAttrs
;

// ~~~ [ Global Variable Definition ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

globalDef
    : globalIdent '=' optLinkage optPreemptionSpecifier visibility ? optDLLStorageClass threadLocal ? unnamedAddr ? optAddrSpace optExternallyInitialized immutable llvmType constant globalAttrs funcAttrs
;

optExternallyInitialized
    :  'externally_initialized' ?
;

// ref: ParseGlobalType
//
//   ::= 'constant'
//   ::= 'global'

immutable
    : 'constant'
    | 'global'
;

globalAttrs
    : (',' globalAttrList) ?
;

globalAttrList
    : globalAttr
    | globalAttrList ',' globalAttr
;

globalAttr
    : section
    | comdat
    | alignment
    //   ::= !dbg !57
    | metadataAttachment
;

// ~~~ [ Indirect Symbol Definition ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#aliases
// https://llvm.org/docs/LangRef.html#ifuncs

// ref: parseIndirectSymbol
//
//   ::= GlobalVar '=' OptionalLinkage OptionalPreemptionSpecifier
//                     OptionalVisibility OptionalDLLStorageClass
//                     OptionalThreadLocal OptionalUnnamedAddr
//                     'alias|ifunc' IndirectSymbol
//
//  IndirectSymbol
//   ::= TypeAndValue

indirectSymbolDef
    : globalIdent '=' externLinkage optPreemptionSpecifier visibility ? optDLLStorageClass threadLocal ? unnamedAddr ? alias llvmType ',' llvmType constant
    | globalIdent '=' optLinkage optPreemptionSpecifier visibility ? optDLLStorageClass threadLocal ? unnamedAddr ? alias llvmType ',' llvmType constant
;

alias
    : 'alias'
    | 'ifunc'
;

// ~~~ [ Function Declaration ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#functions

// ref: ParseDeclare
//
//   ::= 'declare' functionHeader

functionDecl
    : 'declare' metadataAttachments optExternLinkage functionHeader
;

// ~~~ [ Function Definition ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#functions

// ref: ParseDefine
//
//   ::= 'define' functionHeader (!dbg !56)* '{' ...

functionDef
    : 'define' optLinkage functionHeader metadataAttachments functionBody
;

// ref: ParseFunctionHeader
//
//   ::= OptionalLinkage OptionalPreemptionSpecifier OptionalVisibility
//       OptionalCallingConv OptRetAttrs UnnamedAddr ? type GlobalName
//       '(' argList ')' OptFuncAttrs section ? OptionalAlign OptGC
//       OptionalPrefix OptionalPrologue OptPersonalityFn

// TODO: Add OptAlignment before OptGC once the LR-1 conflict has been resolved,
// as funcAttrs also contains 'align'.

functionHeader
    : optPreemptionSpecifier visibility ? optDLLStorageClass optCallingConv returnAttrs llvmType globalIdent '(' params ')' unnamedAddr ? funcAttrs section ? optComdat optGC optPrefix optPrologue optPersonality
;

optGC
    : ('gc' stringLit) ?
;

optPrefix
    : ('prefix' llvmType constant) ?
;

optPrologue
    : ('prologue' llvmType constant) ?
;

optPersonality
    : ('personality' llvmType constant) ?
;

// ref: ParseFunctionBody
//
//   ::= '{' basicBlock+ UseListOrderDirective* '}'

functionBody
    : '{' basicBlockList useListOrders '}'
;

// ~~~ [ Attribute Group Definition ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#attribute-groups

// ref: ParseUnnamedAttrGrp
//
//   ::= 'attributes' AttrGrpID '=' '{' AttrValPair+ '}'

attrGroupDef
    : 'attributes' attrGroupID '=' '{' funcAttrs '}'
;

// ~~~ [ Named metadata Definition ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#named-metadata

// ref: ParseNamedMetadata
//
//   !foo = !{ !1, !2 }

namedMetadataDef
    : metadataName '=' '!' '{' metadataNodes '}'
;

metadataNodes
    : metadataNodeList ?
;

metadataNodeList
    : metadataNode
    | metadataNodeList ',' metadataNode
;

metadataNode
    : metadataID
    // Parse diExpressions inline as a special case. They are still mdNodes, so
    // they can still appear in named metadata. Remove this logic if they become
    // plain metadata.
    | diExpression
;

// ~~~ [ metadata Definition ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#metadata-nodes-and-metadata-strings

// ref: ParseStandaloneMetadata
//
//   !42 = !{...}

metadataDef
    : metadataID '=' optDistinct mdTuple
    | metadataID '=' optDistinct specializedMDNode
;

optDistinct
    : 'distinct' ?
;

// ~~~ [ Use-list Order Directives ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#use-list-order-directives

// ref: ParseUseListOrder
//
//   ::= 'uselistorder' type value ',' UseListOrderIndexes
//  UseListOrderIndexes
//   ::= '{' uint32 (',' uint32)+ '}'

useListOrders
    : useListOrderList ?
;

useListOrderList
    : useListOrder
    | useListOrderList useListOrder
;

useListOrder
    : 'uselistorder' llvmType value ',' '{' indexList '}'
;

// ref: ParseUseListOrderBB
//
//   ::= 'uselistorder_bb' @foo ',' %bar ',' UseListOrderIndexes

useListOrderBB
    : 'uselistorder_bb' globalIdent ',' localIdent ',' '{' indexList '}'
;

// === [ Identifiers ] =========================================================

// --- [ Global Identifiers ] --------------------------------------------------

globalIdent
    : GLOBAL_IDENT
;

// --- [ Local Identifiers ] ---------------------------------------------------

localIdent
    : LOCAL_IDENT
;

// --- [ label Identifiers ] ---------------------------------------------------

labelIdent
    : LABEL_IDENT
;

// --- [ Attribute Group Identifiers ] -----------------------------------------

attrGroupID
    : ATTR_GROUP_ID
;

// --- [ comdat Identifiers ] --------------------------------------------------

comdatName
    : COMDAT_NAME
;

// --- [ metadata Identifiers ] ------------------------------------------------

metadataName
    : METADATA_NAME
;

metadataID
    : METADATA_ID
;

// === [ types ] ===============================================================

// ref: ParseType
//
//  TYPEKEYWORD('void',      type::getVoidTy(Context));
//  TYPEKEYWORD('half',      type::getHalfTy(Context));
//  TYPEKEYWORD('float',     type::getFloatTy(Context));
//  TYPEKEYWORD('double',    type::getDoubleTy(Context));
//  TYPEKEYWORD('x86_fp80',  type::getX86_FP80Ty(Context));
//  TYPEKEYWORD('fp128',     type::getFP128Ty(Context));
//  TYPEKEYWORD('ppc_fp128', type::getPPC_FP128Ty(Context));
//  TYPEKEYWORD('label',     type::getLabelTy(Context));
//  TYPEKEYWORD('metadata',  type::getMetadataTy(Context));
//  TYPEKEYWORD('x86_mmx',   type::getX86_MMXTy(Context));
//  TYPEKEYWORD('token',     type::getTokenTy(Context));

llvmType
    : voidType
    | concreteNonRecType
    // types '(' ArgTypeListI ')' OptFuncAttrs
    | llvmType '(' params ')' // funcType
    | metadataType
    // concreteType -- inlined to eliminate mutual left-recursion
    | llvmType optAddrSpace '*' // pointerType
;

// (DS, 2018-08-24): Factured out the non-recursive parts of concreteType to
// eliminate mutual (indirect) left-recursion
concreteNonRecType
    : intType
    // llvmType ::= 'float' | 'void' (etc)
    | floatType
    // llvmType ::= llvmType '*'
    // llvmType ::= llvmType 'addrspace' '(' uint32 ')' '*'
    // llvmType ::= '<' ... '>'
    | vectorType
    | labelType
    // llvmType ::= '[' ... ']'
    | arrayType
    // llvmType ::= structType
    | structType
    // llvmType ::= %foo
    // llvmType ::= %4
    | namedType
    | mmxType
    | tokenType
;

// --- [ void types ] ----------------------------------------------------------

voidType
    : 'void'
;

// --- [ Function types ] ------------------------------------------------------

// ref: ParseFunctionType
//
//  ::= type ArgumentList OptionalAttrs

// Removed following rule because of mutual left-recursion
//funcType
//   : llvmType '(' params ')'
//;

// --- [ Integer types ] -------------------------------------------------------

intType
    : INT_TYPE
;

// --- [ Floating-point types ] ------------------------------------------------

floatType
    : floatKind
;

floatKind
    : 'half'
    | 'float'
    | 'double'
    | 'x86_fp80'
    | 'fp128'
    | 'ppc_fp128'
;

// --- [ MMX types ] -----------------------------------------------------------

mmxType
    : 'x86_mmx'
;

// --- [ Pointer types ] -------------------------------------------------------

// Removed following rule because of mutual left-recursion
//pointerType
//    : llvmType optAddrSpace '*'
//;

// ref: ParseOptionalAddrSpace
//
//   := empty
//   := 'addrspace' '(' uint32 ')'

optAddrSpace
    : addrSpace ?
;

addrSpace
    : 'addrspace' '(' INT_LIT ')'
;

// --- [ Vector types ] --------------------------------------------------------

// ref: ParseArrayVectorType
//
//     ::= '<' APSINTVAL 'x' types '>'

vectorType
    : '<' INT_LIT 'x' llvmType '>'
;

// --- [ label types ] ---------------------------------------------------------

labelType
    : 'label'
;

// --- [ Token types ] ---------------------------------------------------------

tokenType
    : 'token'
;

// --- [ metadata types ] ------------------------------------------------------

metadataType
    : 'metadata'
;

// --- [ Array types ] ---------------------------------------------------------

// ref: ParseArrayVectorType
//
//     ::= '[' APSINTVAL 'x' types ']'

arrayType
    : '[' INT_LIT 'x' llvmType ']'
;

// --- [ Structure types ] -----------------------------------------------------

// ref: ParseStructBody
//
//   structType
//     ::= '{' '}'
//     ::= '{' type (',' type)* '}'
//     ::= '<' '{' '}' '>'
//     ::= '<' '{' type (',' type)* '}' '>'

// TODO: Simplify when parser generator is not limited by 1 token lookahead.
//
//    structType
//       : '{' types '}'
//       | '<' '{' types '}' '>'
//    ;

structType
    : '{' '}'
    | '{' typeList '}'
    | '<' '{' '}' '>'
    | '<' '{' typeList '}' '>'
;

typeList
    : llvmType
    | typeList ',' llvmType
;

opaqueType
    : 'opaque'
;

// --- [ Named types ] ---------------------------------------------------------

namedType
    : localIdent
;

// === [ Values ] ==============================================================

// ref: ParseValue

value
    : constant
    // %42
    // %foo
    | localIdent
    | inlineAsm
;

// --- [ Inline Assembler Expressions ] ----------------------------------------

// https://llvm.org/docs/LangRef.html#inline-assembler-expressions

// ref: ParseValID
//
//  ::= 'asm' SideEffect? AlignStack? IntelDialect? STRINGCONSTANT ','
//             STRINGCONSTANT

inlineAsm
    : 'asm' optSideEffect optAlignStack optIntelDialect stringLit ',' stringLit
;

optSideEffect
    : 'sideeffect' ?
;

optAlignStack
    : 'alignstack' ?
;

optIntelDialect
    : 'inteldialect' ?
;

// === [ Constants ] ===========================================================

// https://llvm.org/docs/LangRef.html#constants

// ref: ParseValID

constant
    : boolConst
    | intConst
    | floatConst
    | nullConst
    | noneConst
    | structConst
    | arrayConst
    | charArrayConst
    | vectorConst
    | zeroInitializerConst
    // @42
    // @foo
    | globalIdent
    | undefConst
    | blockAddressConst
    | constantExpr
;

// --- [ Boolean Constants ] ---------------------------------------------------

// https://llvm.org/docs/LangRef.html#simple-constants

// ref: ParseValID

boolConst
    : boolLit
;

boolLit
    : 'true'
    | 'false'
;

// --- [ Integer Constants ] ---------------------------------------------------

// https://llvm.org/docs/LangRef.html#simple-constants

// ref: ParseValID

intConst
    : INT_LIT
;

intLit
    : INT_LIT
;

// --- [ Floating-point Constants ] --------------------------------------------

// https://llvm.org/docs/LangRef.html#simple-constants

// ref: ParseValID

floatConst
    : FLOAT_LIT
;

// --- [ null Pointer Constants ] ----------------------------------------------

// https://llvm.org/docs/LangRef.html#simple-constants

// ref: ParseValID

nullConst
    : 'null'
;

// --- [ Token Constants ] -----------------------------------------------------

// https://llvm.org/docs/LangRef.html#simple-constants

// ref: ParseValID

noneConst
    : 'none'
;

// --- [ Structure Constants ] -------------------------------------------------

// https://llvm.org/docs/LangRef.html#complex-constants

// ref: ParseValID
//
//  ::= '{' ConstVector '}'
//  ::= '<' '{' ConstVector '}' '>' --> Packed Struct.

// TODO: Simplify when parser generator is not limited by 1 token lookahead.
//
//    structConst
//       : '{' Elems '}'
//       | '<' '{' Elems '}' '>'
//    ;

structConst
    : '{' '}'
    | '{' typeConstList '}'
    | '<' '{' '}' '>'
    | '<' '{' typeConstList '}' '>'
;

// --- [ Array Constants ] -----------------------------------------------------

// https://llvm.org/docs/LangRef.html#complex-constants

// ref: ParseValID
//
//  c 'foo'

arrayConst
    : '[' typeConsts ']'
;

charArrayConst
    : 'c' stringLit
;

stringLit
    : STRING_LIT
;

// --- [ Vector Constants ] ----------------------------------------------------

// https://llvm.org/docs/LangRef.html#complex-constants

// ref: ParseValID
//
//  ::= '<' ConstVector '>'         --> Vector.

vectorConst
    : '<' typeConsts '>'
;

// --- [ Zero Initialization Constants ] ---------------------------------------

// https://llvm.org/docs/LangRef.html#complex-constants

// ref: ParseValID

zeroInitializerConst
    : 'zeroinitializer'
;

// --- [ Undefined Values ] ----------------------------------------------------

// https://llvm.org/docs/LangRef.html#undefined-values

// ref: ParseValID

undefConst
    : 'undef'
;

// --- [ Addresses of Basic Blocks ] -------------------------------------------

// https://llvm.org/docs/LangRef.html#addresses-of-basic-blocks

// ref: ParseValID
//
//  ::= 'blockaddress' '(' @foo ',' %bar ')'

blockAddressConst
    : 'blockaddress' '(' globalIdent ',' localIdent ')'
;

// === [ constant expressions ] ================================================

// https://llvm.org/docs/LangRef.html#constant-expressions

// ref: ParseValID

constantExpr
    // Binary expressions
    : addExpr
    | fAddExpr
    | subExpr
    | fSubExpr
    | mulExpr
    | fMulExpr
    | uDivExpr
    | sDivExpr
    | fDivExpr
    | uRemExpr
    | sRemExpr
    | fRemExpr
    // Bitwise expressions
    | shlExpr
    | lShrExpr
    | ashrExpr
    | andExpr
    | orExpr
    | xorExpr
    // Vector expressions
    | extractElementExpr
    | insertElementExpr
    | shuffleVectorExpr
    // Aggregate expressions
    | extractValueExpr
    | insertValueExpr
    // Memory expressions
    | getElementPtrExpr
    // Conversion expressions
    | truncExpr
    | zExtExpr
    | sExtExpr
    | fPTruncExpr
    | fpExtExpr
    | fpToUIExpr
    | fpToSIExpr
    | uiToFPExpr
    | siToFPExpr
    | ptrToIntExpr
    | intToPtrExpr
    | bitCastExpr
    | addrSpaceCastExpr
    // Other expressions
    | iCmpExpr
    | fCmpExpr
    | selectExpr
;

// --- [ Binary expressions ] --------------------------------------------------

// https://llvm.org/docs/LangRef.html#constant-expressions

// ~~~ [ add ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

addExpr
    : 'add' overflowFlags '(' llvmType constant ',' llvmType constant ')'
;

// ~~~ [ fadd ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

fAddExpr
    : 'fadd' '(' llvmType constant ',' llvmType constant ')'
;

// ~~~ [ sub ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

subExpr
    : 'sub' overflowFlags '(' llvmType constant ',' llvmType constant ')'
;

// ~~~ [ fsub ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

fSubExpr
    : 'fsub' '(' llvmType constant ',' llvmType constant ')'
;

// ~~~ [ mul ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

mulExpr
    : 'mul' overflowFlags '(' llvmType constant ',' llvmType constant ')'
;

// ~~~ [ fmul ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

fMulExpr
    : 'fmul' '(' llvmType constant ',' llvmType constant ')'
;

// ~~~ [ udiv ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

uDivExpr
    : 'udiv' optExact '(' llvmType constant ',' llvmType constant ')'
;

// ~~~ [ sdiv ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

sDivExpr
    : 'sdiv' optExact '(' llvmType constant ',' llvmType constant ')'
;

// ~~~ [ fdiv ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

fDivExpr
    : 'fdiv' '(' llvmType constant ',' llvmType constant ')'
;

// ~~~ [ urem ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

uRemExpr
    : 'urem' '(' llvmType constant ',' llvmType constant ')'
;

// ~~~ [ srem ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

sRemExpr
    : 'srem' '(' llvmType constant ',' llvmType constant ')'
;

// ~~~ [ frem ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

fRemExpr
    : 'frem' '(' llvmType constant ',' llvmType constant ')'
;

// --- [ Bitwise expressions ] -------------------------------------------------

// https://llvm.org/docs/LangRef.html#constant-expressions

// ~~~ [ shl ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

shlExpr
    : 'shl' overflowFlags '(' llvmType constant ',' llvmType constant ')'
;

// ~~~ [ lshr ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

lShrExpr
    : 'lshr' optExact '(' llvmType constant ',' llvmType constant ')'
;

// ~~~ [ ashr ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

ashrExpr
    : 'ashr' optExact '(' llvmType constant ',' llvmType constant ')'
;

// ~~~ [ and ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

andExpr
    : 'and' '(' llvmType constant ',' llvmType constant ')'
;

// ~~~ [ or ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

orExpr
    : 'or' '(' llvmType constant ',' llvmType constant ')'
;

// ~~~ [ xor ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

xorExpr
    : 'xor' '(' llvmType constant ',' llvmType constant ')'
;

// --- [ Vector expressions ] --------------------------------------------------

// https://llvm.org/docs/LangRef.html#constant-expressions

// ~~~ [ extractelement ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

extractElementExpr
    : 'extractelement' '(' llvmType constant ',' llvmType constant ')'
;

// ~~~ [ insertelement ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

insertElementExpr
    : 'insertelement' '(' llvmType constant ',' llvmType constant ',' llvmType constant ')'
;

// ~~~ [ shufflevector ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

shuffleVectorExpr
    : 'shufflevector' '(' llvmType constant ',' llvmType constant ',' llvmType constant ')'
;

// --- [ Aggregate expressions ] -----------------------------------------------

// https://llvm.org/docs/LangRef.html#constant-expressions

// ~~~ [ extractvalue ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

extractValueExpr
    : 'extractvalue' '(' llvmType constant indices ')'
;

// ~~~ [ insertvalue ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

insertValueExpr
    : 'insertvalue' '(' llvmType constant ',' llvmType constant indices ')'
;

// --- [ Memory expressions ] --------------------------------------------------

// https://llvm.org/docs/LangRef.html#constant-expressions

// ~~~ [ getelementptr ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

getElementPtrExpr
    : 'getelementptr' optInBounds '(' llvmType ',' llvmType constant ',' gepConstIndices ')'
;

// ref: ParseGlobalValueVector
//
//   ::= empty
//   ::= [inrange] TypeAndValue (',' [inrange] TypeAndValue)*

gepConstIndices
    : gepConstIndexList ?
;

gepConstIndexList
    : gepConstIndex
    | gepConstIndexList ',' gepConstIndex
;

gepConstIndex
    : optInrange llvmType constant
;

optInrange
    : 'inrange' ?
;

// --- [ Conversion expressions ] ----------------------------------------------

// https://llvm.org/docs/LangRef.html#constant-expressions

// ~~~ [ trunc ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

truncExpr
    : 'trunc' '(' llvmType constant 'to' llvmType ')'
;

// ~~~ [ zext ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

zExtExpr
    : 'zext' '(' llvmType constant 'to' llvmType ')'
;

// ~~~ [ sext ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

sExtExpr
    : 'sext' '(' llvmType constant 'to' llvmType ')'
;

// ~~~ [ fptrunc ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

fPTruncExpr
    : 'fptrunc' '(' llvmType constant 'to' llvmType ')'
;

// ~~~ [ fpext ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

fpExtExpr
    : 'fpext' '(' llvmType constant 'to' llvmType ')'
;

// ~~~ [ fptoui ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

fpToUIExpr
    : 'fptoui' '(' llvmType constant 'to' llvmType ')'
;

// ~~~ [ fptosi ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

fpToSIExpr
    : 'fptosi' '(' llvmType constant 'to' llvmType ')'
;

// ~~~ [ uitofp ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

uiToFPExpr
    : 'uitofp' '(' llvmType constant 'to' llvmType ')'
;

// ~~~ [ sitofp ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

siToFPExpr
    : 'sitofp' '(' llvmType constant 'to' llvmType ')'
;

// ~~~ [ ptrtoint ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

ptrToIntExpr
    : 'ptrtoint' '(' llvmType constant 'to' llvmType ')'
;

// ~~~ [ inttoptr ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

intToPtrExpr
    : 'inttoptr' '(' llvmType constant 'to' llvmType ')'
;

// ~~~ [ bitcast ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

bitCastExpr
    : 'bitcast' '(' llvmType constant 'to' llvmType ')'
;

// ~~~ [ addrspacecast ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

addrSpaceCastExpr
    : 'addrspacecast' '(' llvmType constant 'to' llvmType ')'
;

// --- [ Other expressions ] ---------------------------------------------------

// https://llvm.org/docs/LangRef.html#constant-expressions

// ~~~ [ icmp ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

iCmpExpr
    : 'icmp' iPred '(' llvmType constant ',' llvmType constant ')'
;

// ~~~ [ fcmp ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

fCmpExpr
    : 'fcmp' fpred '(' llvmType constant ',' llvmType constant ')'
;

// ~~~ [ select ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseValID

selectExpr
    : 'select' '(' llvmType constant ',' llvmType constant ',' llvmType constant ')'
;

// === [ Basic Blocks ] ========================================================

// ref: ParseBasicBlock
//
//   ::= LabelStr? instruction*

basicBlockList
    : basicBlock
    | basicBlockList basicBlock
;

basicBlock
    : optLabelIdent instructions terminator
;

optLabelIdent
    : labelIdent ?
;

// === [ instructions ] ========================================================

// https://llvm.org/docs/LangRef.html#instruction-reference

// ref: ParseInstruction

instructions
    : instructionList ?
;

instructionList
    : instruction
    | instructionList instruction
;

instruction
    // instructions not producing values.
    : storeInst
    | fenceInst
    | cmpXchgInst
    | atomicRMWInst
    // instructions producing values.
    | localIdent '=' valueInstruction
    | valueInstruction
;

valueInstruction
    // Unary instructions
    : fNegInst
    // Binary instructions
    | addInst
    | fAddInst
    | subInst
    | fSubInst
    | mulInst
    | fMulInst
    | uDivInst
    | sDivInst
    | fDivInst
    | uRemInst
    | sRemInst
    | fRemInst
    // Bitwise instructions
    | shlInst
    | lshrInst
    | ashrInst
    | andInst
    | orInst
    | xorInst
    // Vector instructions
    | extractElementInst
    | insertElementInst
    | shuffleVectorInst
    // Aggregate instructions
    | extractValueInst
    | insertValueInst
    // Memory instructions
    | allocaInst
    | loadInst
    | getElementPtrInst
    // Conversion instructions
    | truncInst
    | zExtInst
    | sExtInst
    | fpTruncInst
    | fpExtInst
    | fpToUIInst
    | fpToSIInst
    | uiToFPInst
    | siToFPInst
    | ptrToIntInst
    | intToPtrInst
    | bitCastInst
    | addrSpaceCastInst
    // Other instructions
    | iCmpInst
    | fCmpInst
    | phiInst
    | selectInst
    | callInst
    | vaArgInst
    | landingPadInst
    | catchPadInst
    | cleanupPadInst
;

// --- [ Unary instructions ] --------------------------------------------------

// ~~~ [ fneg ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#fneg-instruction

// ref: ParseArithmetic
//
//  ::= ArithmeticOps TypeAndValue

fNegInst
    : 'fneg' fastMathFlags llvmType value optCommaSepMetadataAttachmentList
;

// --- [ Binary instructions ] -------------------------------------------------

// ~~~ [ add ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#add-instruction

// ref: ParseArithmetic
//
//  ::= ArithmeticOps TypeAndValue ',' value

addInst
    : 'add' overflowFlags llvmType value ',' value optCommaSepMetadataAttachmentList
;

// ~~~ [ fadd ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#fadd-instruction

// ref: ParseArithmetic
//
//  ::= ArithmeticOps TypeAndValue ',' value

fAddInst
    : 'fadd' fastMathFlags llvmType value ',' value optCommaSepMetadataAttachmentList
;

// ~~~ [ sub ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#sub-instruction

// ref: ParseArithmetic
//
//  ::= ArithmeticOps TypeAndValue ',' value

subInst
    : 'sub' overflowFlags llvmType value ',' value optCommaSepMetadataAttachmentList
;

// ~~~ [ fsub ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#fsub-instruction

// ref: ParseArithmetic
//
//  ::= ArithmeticOps TypeAndValue ',' value

fSubInst
    : 'fsub' fastMathFlags llvmType value ',' value optCommaSepMetadataAttachmentList
;

// ~~~ [ mul ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#mul-instruction

// ref: ParseArithmetic
//
//  ::= ArithmeticOps TypeAndValue ',' value

mulInst
    : 'mul' overflowFlags llvmType value ',' value optCommaSepMetadataAttachmentList
;

// ~~~ [ fmul ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#fmul-instruction

// ref: ParseArithmetic
//
//  ::= ArithmeticOps TypeAndValue ',' value

fMulInst
    : 'fmul' fastMathFlags llvmType value ',' value optCommaSepMetadataAttachmentList
;

// ~~~ [ udiv ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#udiv-instruction

// ref: ParseArithmetic
//
//  ::= ArithmeticOps TypeAndValue ',' value

uDivInst
    : 'udiv' optExact llvmType value ',' value optCommaSepMetadataAttachmentList
;

// ~~~ [ sdiv ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#sdiv-instruction

// ref: ParseArithmetic
//
//  ::= ArithmeticOps TypeAndValue ',' value

sDivInst
    : 'sdiv' optExact llvmType value ',' value optCommaSepMetadataAttachmentList
;

// ~~~ [ fdiv ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#fdiv-instruction

// ref: ParseArithmetic
//
//  ::= ArithmeticOps TypeAndValue ',' value

fDivInst
    : 'fdiv' fastMathFlags llvmType value ',' value optCommaSepMetadataAttachmentList
;

// ~~~ [ urem ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#urem-instruction

// ref: ParseArithmetic
//
//  ::= ArithmeticOps TypeAndValue ',' value

uRemInst
    : 'urem' llvmType value ',' value optCommaSepMetadataAttachmentList
;

// ~~~ [ srem ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#srem-instruction

// ref: ParseArithmetic
//
//  ::= ArithmeticOps TypeAndValue ',' value

sRemInst
    : 'srem' llvmType value ',' value optCommaSepMetadataAttachmentList
;

// ~~~ [ frem ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#frem-instruction

// ref: ParseArithmetic
//
//  ::= ArithmeticOps TypeAndValue ',' value

fRemInst
    : 'frem' fastMathFlags llvmType value ',' value optCommaSepMetadataAttachmentList
;

// --- [ Bitwise instructions ] ------------------------------------------------

// ~~~ [ shl ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#shl-instruction

// ref: ParseArithmetic
//
//  ::= ArithmeticOps TypeAndValue ',' value

shlInst
    : 'shl' overflowFlags llvmType value ',' value optCommaSepMetadataAttachmentList
;

// ~~~ [ lshr ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#lshr-instruction

// ref: ParseArithmetic
//
//  ::= ArithmeticOps TypeAndValue ',' value

lshrInst
    : 'lshr' optExact llvmType value ',' value optCommaSepMetadataAttachmentList
;

// ~~~ [ ashr ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#ashr-instruction

// ref: ParseArithmetic
//
//  ::= ArithmeticOps TypeAndValue ',' value

ashrInst
    : 'ashr' optExact llvmType value ',' value optCommaSepMetadataAttachmentList
;

// ~~~ [ and ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#and-instruction

// ref: ParseLogical
//
//  ::= ArithmeticOps TypeAndValue ',' value {

andInst
    : 'and' llvmType value ',' value optCommaSepMetadataAttachmentList
;

// ~~~ [ or ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#or-instruction

// ref: ParseLogical
//
//  ::= ArithmeticOps TypeAndValue ',' value {

orInst
    : 'or' llvmType value ',' value optCommaSepMetadataAttachmentList
;

// ~~~ [ xor ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#xor-instruction

// ref: ParseLogical
//
//  ::= ArithmeticOps TypeAndValue ',' value {

xorInst
    : 'xor' llvmType value ',' value optCommaSepMetadataAttachmentList
;

// --- [ Vector instructions ] -------------------------------------------------

// ~~~ [ extractelement ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#extractelement-instruction

// ref: ParseExtractElement
//
//   ::= 'extractelement' TypeAndValue ',' TypeAndValue

extractElementInst
    : 'extractelement' llvmType value ',' llvmType value optCommaSepMetadataAttachmentList
;

// ~~~ [ insertelement ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#insertelement-instruction

// ref: ParseInsertElement
//
//   ::= 'insertelement' TypeAndValue ',' TypeAndValue ',' TypeAndValue

insertElementInst
    : 'insertelement' llvmType value ',' llvmType value ',' llvmType value optCommaSepMetadataAttachmentList
;

// ~~~ [ shufflevector ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#shufflevector-instruction

// ref: ParseShuffleVector
//
//   ::= 'shufflevector' TypeAndValue ',' TypeAndValue ',' TypeAndValue

shuffleVectorInst
    : 'shufflevector' llvmType value ',' llvmType value ',' llvmType value optCommaSepMetadataAttachmentList
;

// --- [ Aggregate instructions ] ----------------------------------------------

// ~~~ [ extractvalue ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#extractvalue-instruction

// ref: ParseExtractValue
//
//   ::= 'extractvalue' TypeAndValue (',' uint32)+

extractValueInst
    : 'extractvalue' llvmType value ',' indexList optCommaSepMetadataAttachmentList
;

// ~~~ [ insertvalue ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#insertvalue-instruction

// ref: ParseInsertValue
//
//   ::= 'insertvalue' TypeAndValue ',' TypeAndValue (',' uint32)+

insertValueInst
    : 'insertvalue' llvmType value ',' llvmType value ',' indexList optCommaSepMetadataAttachmentList
;

// --- [ Memory instructions ] -------------------------------------------------

// ~~~ [ alloca ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#alloca-instruction

// ref: ParseAlloc
//
//   ::= 'alloca' 'inalloca'? 'swifterror'? type (',' TypeAndValue)?
//       (',' 'align' i32)? (',', 'addrspace(n))?

// TODO: Simplify when parser generator is not limited by 1 token lookahead.
//
//    allocaInst
//       : 'alloca' optInAlloca OptSwiftError type OptCommaTypeValue OptCommaAlignment OptCommaAddrSpace optCommaSepMetadataAttachmentList
//    ;

allocaInst
    : 'alloca' optInAlloca optSwiftError llvmType optCommaSepMetadataAttachmentList
    | 'alloca' optInAlloca optSwiftError llvmType ',' alignment optCommaSepMetadataAttachmentList
    | 'alloca' optInAlloca optSwiftError llvmType ',' llvmType value optCommaSepMetadataAttachmentList
    | 'alloca' optInAlloca optSwiftError llvmType ',' llvmType value ',' alignment optCommaSepMetadataAttachmentList
    | 'alloca' optInAlloca optSwiftError llvmType ',' addrSpace optCommaSepMetadataAttachmentList
    | 'alloca' optInAlloca optSwiftError llvmType ',' alignment ',' addrSpace optCommaSepMetadataAttachmentList
    | 'alloca' optInAlloca optSwiftError llvmType ',' llvmType value ',' addrSpace optCommaSepMetadataAttachmentList
    | 'alloca' optInAlloca optSwiftError llvmType ',' llvmType value ',' alignment ',' addrSpace optCommaSepMetadataAttachmentList
;

optInAlloca
    : 'inalloca' ?
;

optSwiftError
    : 'swifterror' ?
;

// ~~~ [ load ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#load-instruction

// ref: ParseLoad
//
//   ::= 'load' 'volatile'? TypeAndValue (',' 'align' i32)?
//   ::= 'load' 'atomic' 'volatile'? TypeAndValue
//       'singlethread'? AtomicOrdering (',' 'align' i32)?

// TODO: Simplify when parser generator is not limited by 1 token lookahead.
//
//    loadInst
//       : 'load' VOLATILE ? type ',' type value OptCommaAlignment optCommaSepMetadataAttachmentList
//       | 'load' 'atomic' VOLATILE ? type ',' type value optSyncScope AtomicOrdering OptCommaAlignment optCommaSepMetadataAttachmentList
//    ;

loadInst
    // load.
    : 'load' VOLATILE ? llvmType ',' llvmType value optCommaSepMetadataAttachmentList
    | 'load' VOLATILE ? llvmType ',' llvmType value ',' alignment optCommaSepMetadataAttachmentList
    // atomic load.
    | 'load' 'atomic' VOLATILE ? llvmType ',' llvmType value optSyncScope atomicOrdering optCommaSepMetadataAttachmentList
    | 'load' 'atomic' VOLATILE ? llvmType ',' llvmType value optSyncScope atomicOrdering ',' alignment optCommaSepMetadataAttachmentList
;

// ~~~ [ store ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#store-instruction

// ref: ParseStore
//
//   ::= 'store' 'volatile'? TypeAndValue ',' TypeAndValue (',' 'align' i32)?
//   ::= 'store' 'atomic' 'volatile'? TypeAndValue ',' TypeAndValue
//       'singlethread'? AtomicOrdering (',' 'align' i32)?

// TODO: Simplify when parser generator is not limited by 1 token lookahead.
//
//    storeInst
//       : 'store' VOLATILE ? type value ',' type value OptCommaAlignment optCommaSepMetadataAttachmentList
//       | 'store' 'atomic' VOLATILE ? type value ',' type value optSyncScope AtomicOrdering OptCommaAlignment optCommaSepMetadataAttachmentList
//    ;

storeInst
    : 'store' VOLATILE ? llvmType value ',' llvmType value optCommaSepMetadataAttachmentList
    | 'store' VOLATILE ? llvmType value ',' llvmType value ',' alignment optCommaSepMetadataAttachmentList
    | 'store' 'atomic' VOLATILE ? llvmType value ',' llvmType value optSyncScope atomicOrdering optCommaSepMetadataAttachmentList
    | 'store' 'atomic' VOLATILE ? llvmType value ',' llvmType value optSyncScope atomicOrdering ',' alignment optCommaSepMetadataAttachmentList
;

// ~~~ [ fence ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#fence-instruction

// ref: ParseFence
//
//   ::= 'fence' 'singlethread'? AtomicOrdering

fenceInst
    : 'fence' optSyncScope atomicOrdering optCommaSepMetadataAttachmentList
;

// ~~~ [ cmpxchg ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#cmpxchg-instruction

// ref: ParseCmpXchg
//
//   ::= 'cmpxchg' 'weak'? 'volatile'? TypeAndValue ',' TypeAndValue ','
//       TypeAndValue 'singlethread'? AtomicOrdering AtomicOrdering

cmpXchgInst
    : 'cmpxchg' optWeak VOLATILE ? llvmType value ',' llvmType value ',' llvmType value optSyncScope atomicOrdering atomicOrdering optCommaSepMetadataAttachmentList
;

optWeak
    : 'weak' ?
;

// ~~~ [ atomicrmw ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#atomicrmw-instruction

// ref: ParseAtomicRMW
//
//   ::= 'atomicrmw' 'volatile'? binOp TypeAndValue ',' TypeAndValue
//       'singlethread'? AtomicOrdering

atomicRMWInst
    : 'atomicrmw' VOLATILE ? binOp llvmType value ',' llvmType value optSyncScope atomicOrdering optCommaSepMetadataAttachmentList
;

binOp
    : 'add'
    | 'and'
    | 'max'
    | 'min'
    | 'nand'
    | 'or'
    | 'sub'
    | 'umax'
    | 'umin'
    | 'xchg'
    | 'xor'
;

// ~~~ [ getelementptr ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#getelementptr-instruction

// ref: ParseGetElementPtr
//
//   ::= 'getelementptr' 'inbounds'? TypeAndValue (',' TypeAndValue)*

// TODO: Simplify when parser generator is not limited by 1 token lookahead.
//
//    getElementPtrInst
//       : 'getelementptr' optInBounds type ',' type value GEPIndices optCommaSepMetadataAttachmentList
//    ;

getElementPtrInst
    : 'getelementptr' optInBounds llvmType ',' llvmType value optCommaSepMetadataAttachmentList
    | 'getelementptr' optInBounds llvmType ',' llvmType value ',' commaSepTypeValueList optCommaSepMetadataAttachmentList
;

// --- [ Conversion instructions ] ---------------------------------------------

// ~~~ [ trunc ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#trunc-instruction

// ref: ParseCast
//
//   ::= CastOpc TypeAndValue 'to' type

truncInst
    : 'trunc' llvmType value 'to' llvmType optCommaSepMetadataAttachmentList
;

// ~~~ [ zext ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#zext-instruction

// ref: ParseCast
//
//   ::= CastOpc TypeAndValue 'to' type

zExtInst
    : 'zext' llvmType value 'to' llvmType optCommaSepMetadataAttachmentList
;

// ~~~ [ sext ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#sext-instruction

// ref: ParseCast
//
//   ::= CastOpc TypeAndValue 'to' type

sExtInst
    : 'sext' llvmType value 'to' llvmType optCommaSepMetadataAttachmentList
;

// ~~~ [ fptrunc ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#fptrunc-instruction

// ref: ParseCast
//
//   ::= CastOpc TypeAndValue 'to' type

fpTruncInst
    : 'fptrunc' llvmType value 'to' llvmType optCommaSepMetadataAttachmentList
;

// ~~~ [ fpext ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#fpext-instruction

// ref: ParseCast
//
//   ::= CastOpc TypeAndValue 'to' type

fpExtInst
    : 'fpext' llvmType value 'to' llvmType optCommaSepMetadataAttachmentList
;

// ~~~ [ fptoui ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#fptoui-instruction

// ref: ParseCast
//
//   ::= CastOpc TypeAndValue 'to' type

fpToUIInst
    : 'fptoui' llvmType value 'to' llvmType optCommaSepMetadataAttachmentList
;

// ~~~ [ fptosi ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#fptosi-instruction

// ref: ParseCast
//
//   ::= CastOpc TypeAndValue 'to' type

fpToSIInst
    : 'fptosi' llvmType value 'to' llvmType optCommaSepMetadataAttachmentList
;

// ~~~ [ uitofp ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#uitofp-instruction

// ref: ParseCast
//
//   ::= CastOpc TypeAndValue 'to' type

uiToFPInst
    : 'uitofp' llvmType value 'to' llvmType optCommaSepMetadataAttachmentList
;

// ~~~ [ sitofp ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#sitofp-instruction

// ref: ParseCast
//
//   ::= CastOpc TypeAndValue 'to' type

siToFPInst
    : 'sitofp' llvmType value 'to' llvmType optCommaSepMetadataAttachmentList
;

// ~~~ [ ptrtoint ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#ptrtoint-instruction

// ref: ParseCast
//
//   ::= CastOpc TypeAndValue 'to' type

ptrToIntInst
    : 'ptrtoint' llvmType value 'to' llvmType optCommaSepMetadataAttachmentList
;

// ~~~ [ inttoptr ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#inttoptr-instruction

// ref: ParseCast
//
//   ::= CastOpc TypeAndValue 'to' type

intToPtrInst
    : 'inttoptr' llvmType value 'to' llvmType optCommaSepMetadataAttachmentList
;

// ~~~ [ bitcast ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#bitcast-instruction

// ref: ParseCast
//
//   ::= CastOpc TypeAndValue 'to' type

bitCastInst
    : 'bitcast' llvmType value 'to' llvmType optCommaSepMetadataAttachmentList
;

// ~~~ [ addrspacecast ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#addrspacecast-instruction

// ref: ParseCast
//
//   ::= CastOpc TypeAndValue 'to' type

addrSpaceCastInst
    : 'addrspacecast' llvmType value 'to' llvmType optCommaSepMetadataAttachmentList
;

// --- [ Other instructions ] --------------------------------------------------

// ~~~ [ icmp ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#icmp-instruction

// ref: ParseCompare
//
//  ::= 'icmp' IPredicates TypeAndValue ',' value

iCmpInst
    : 'icmp' iPred llvmType value ',' value optCommaSepMetadataAttachmentList
;

// ~~~ [ fcmp ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#fcmp-instruction

// ref: ParseCompare
//
//  ::= 'fcmp' FPredicates TypeAndValue ',' value

fCmpInst
    : 'fcmp' fastMathFlags fpred llvmType value ',' value optCommaSepMetadataAttachmentList
;

// ~~~ [ phi ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#phi-instruction

// ref: ParsePHI
//
//   ::= 'phi' type '[' value ',' value ']' (',' '[' value ',' value ']')*

phiInst
    : 'phi' llvmType incList optCommaSepMetadataAttachmentList
;

incList
    : inc
    | incList ',' inc
;

inc
    : '[' value ',' localIdent ']'
;

// ~~~ [ select ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#select-instruction

// ref: ParseSelect
//
//   ::= 'select' TypeAndValue ',' TypeAndValue ',' TypeAndValue

selectInst
    : 'select' llvmType value ',' llvmType value ',' llvmType value optCommaSepMetadataAttachmentList
;

// ~~~ [ call ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#call-instruction

// ref: ParseCall
//
//   ::= 'call' OptionalFastMathFlags OptionalCallingConv
//           OptionalAttrs type value ParameterList OptionalAttrs
//   ::= 'tail' 'call' OptionalFastMathFlags OptionalCallingConv
//           OptionalAttrs type value ParameterList OptionalAttrs
//   ::= 'musttail' 'call' OptionalFastMathFlags OptionalCallingConv
//           OptionalAttrs type value ParameterList OptionalAttrs
//   ::= 'notail' 'call'  OptionalFastMathFlags OptionalCallingConv
//           OptionalAttrs type value ParameterList OptionalAttrs

callInst
    : optTail 'call' fastMathFlags optCallingConv returnAttrs llvmType value '(' args ')' funcAttrs operandBundles optCommaSepMetadataAttachmentList
;

optTail
    : (
        'musttail'
      | 'notail'
      | 'tail'
      ) ?
;

// ~~~ [ va_arg ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#va_arg-instruction

// ref: ParseVA_Arg
//
//   ::= 'va_arg' TypeAndValue ',' type

vaArgInst
    : 'va_arg' llvmType value ',' llvmType optCommaSepMetadataAttachmentList
;

// ~~~ [ landingpad ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#landingpad-instruction

// ref: ParseLandingPad
//
//   ::= 'landingpad' type 'personality' TypeAndValue 'cleanup'? clause+
//  clause
//   ::= 'catch' TypeAndValue
//   ::= 'filter'
//   ::= 'filter' TypeAndValue ( ',' TypeAndValue )*

landingPadInst
    : 'landingpad' llvmType optCleanup clauses optCommaSepMetadataAttachmentList
;

optCleanup
    : 'cleanup' ?
;

clauses
    : clauseList ?
;

clauseList
    : clause
    | clauseList clause
;

clause
    : 'catch' llvmType value
    | 'filter' llvmType arrayConst
;

// --- [ catchpad ] ------------------------------------------------------------

// ref: ParseCatchPad
//
//   ::= 'catchpad' paramList 'to' TypeAndValue 'unwind' TypeAndValue

catchPadInst
    : 'catchpad' 'within' localIdent '[' exceptionArgs ']' optCommaSepMetadataAttachmentList
;

// --- [ cleanuppad ] ----------------------------------------------------------

// ref: ParseCleanupPad
//
//   ::= 'cleanuppad' within Parent paramList

cleanupPadInst
    : 'cleanuppad' 'within' exceptionScope '[' exceptionArgs ']' optCommaSepMetadataAttachmentList
;

// === [ Terminators ] =========================================================

// https://llvm.org/docs/LangRef.html#terminator-instructions

// ref: ParseInstruction

terminator
    : retTerm
    | brTerm
    | condBrTerm
    | switchTerm
    | indirectBrTerm
    | invokeTerm
    | resumeTerm
    | catchSwitchTerm
    | catchRetTerm
    | cleanupRetTerm
    | unreachableTerm
;

// --- [ ret ] -----------------------------------------------------------------

// https://llvm.org/docs/LangRef.html#ret-instruction

// ref: ParseRet
//
//   ::= 'ret' void (',' !dbg, !1)*
//   ::= 'ret' TypeAndValue (',' !dbg, !1)*

retTerm
    // void return.
    : 'ret' voidType optCommaSepMetadataAttachmentList
    // value return.
    | 'ret' (llvmType optAddrSpace '*' | concreteNonRecType) value optCommaSepMetadataAttachmentList
;

// --- [ br ] ------------------------------------------------------------------

// https://llvm.org/docs/LangRef.html#br-instruction

// ref: ParseBr
//
//   ::= 'br' TypeAndValue
//   ::= 'br' TypeAndValue ',' TypeAndValue ',' TypeAndValue

// Unconditional branch.
brTerm
    : 'br' labelType localIdent optCommaSepMetadataAttachmentList
;

// Conditional branch.
condBrTerm
    : 'br' intType value ',' labelType localIdent ',' labelType localIdent optCommaSepMetadataAttachmentList
;

// --- [ switch ] --------------------------------------------------------------

// https://llvm.org/docs/LangRef.html#switch-instruction

// ref: ParseSwitch
//
//    ::= 'switch' TypeAndValue ',' TypeAndValue '[' JumpTable ']'
//  JumpTable
//    ::= (TypeAndValue ',' TypeAndValue)*

switchTerm
    : 'switch' llvmType value ',' labelType localIdent '[' cases ']' optCommaSepMetadataAttachmentList
;

cases
    : caseList ?
;

caseList
    : llvmCase
    | caseList llvmCase
;

llvmCase
    : llvmType intConst ',' labelType localIdent
;

// --- [ indirectbr ] ----------------------------------------------------------

// https://llvm.org/docs/LangRef.html#indirectbr-instruction

// ref: ParseIndirectBr
//
//    ::= 'indirectbr' TypeAndValue ',' '[' labelList ']'

indirectBrTerm
    : 'indirectbr' llvmType value ',' '[' labelList ']' optCommaSepMetadataAttachmentList
;

labelList
    : label
    | labelList ',' label
;

label
    : labelType localIdent
;

// --- [ invoke ] --------------------------------------------------------------

// https://llvm.org/docs/LangRef.html#invoke-instruction

// ref: ParseInvoke
//
//   ::= 'invoke' OptionalCallingConv OptionalAttrs type value paramList
//       OptionalAttrs 'to' TypeAndValue 'unwind' TypeAndValue

invokeTerm
    : 'invoke' optCallingConv returnAttrs llvmType value '(' args ')' funcAttrs operandBundles 'to' labelType localIdent 'unwind' labelType localIdent optCommaSepMetadataAttachmentList
;

// --- [ resume ] --------------------------------------------------------------

// https://llvm.org/docs/LangRef.html#resume-instruction

// ref: ParseResume
//
//   ::= 'resume' TypeAndValue

resumeTerm
    : 'resume' llvmType value optCommaSepMetadataAttachmentList
;

// --- [ catchswitch ] ---------------------------------------------------------

// https://llvm.org/docs/LangRef.html#catchswitch-instruction

// ref: ParseCatchSwitch
//
//   ::= 'catchswitch' within Parent

catchSwitchTerm
    : 'catchswitch' 'within' exceptionScope '[' labelList ']' 'unwind' unwindTarget optCommaSepMetadataAttachmentList
;

// --- [ catchret ] ------------------------------------------------------------

// https://llvm.org/docs/LangRef.html#catchret-instruction

// ref: ParseCatchRet
//
//   ::= 'catchret' from Parent value 'to' TypeAndValue

catchRetTerm
    : 'catchret' 'from' value 'to' labelType localIdent optCommaSepMetadataAttachmentList
;

// --- [ cleanupret ] ----------------------------------------------------------

// https://llvm.org/docs/LangRef.html#cleanupret-instruction

// ref: ParseCleanupRet
//
//   ::= 'cleanupret' from value unwind ('to' 'caller' | TypeAndValue)

cleanupRetTerm
    : 'cleanupret' 'from' value 'unwind' unwindTarget optCommaSepMetadataAttachmentList
;

// --- [ unreachable ] ---------------------------------------------------------

// https://llvm.org/docs/LangRef.html#unreachable-instruction

// ref: ParseInstruction

unreachableTerm
    : 'unreachable' optCommaSepMetadataAttachmentList
;

// ___ [ Helpers ] _____________________________________________________________

unwindTarget
    : 'to' 'caller'
    | labelType localIdent
;
// === [ metadata Nodes and metadata Strings ] =================================

// https://llvm.org/docs/LangRef.html#metadata-nodes-and-metadata-strings

// --- [ metadata Tuple ] ------------------------------------------------------

// ref: ParseMDTuple

mdTuple
    : '!' mdFields
;

// ref: ParseMDNodeVector
//
//   ::= { Element (',' Element)* }
//  Element
//   ::= 'null' | TypeAndValue

// ref: ParseMDField(mdFieldList &)

mdFields
    : '{' '}'
    | '{' mdFieldList '}'
;

mdFieldList
    : mdField
    | mdFieldList ',' mdField
;

// ref: ParseMDField(mdField &)

mdField
    // null is a special case since it is typeless.
    : 'null'
    | metadata
;

// --- [ metadata ] ------------------------------------------------------------

// ref: ParseMetadata
//
//  ::= i32 %local
//  ::= i32 @global
//  ::= i32 7
//  ::= !42
//  ::= !{...}
//  ::= !'string'
//  ::= !diLocation(...)

metadata
    : llvmType value
    | mdString
    // !{ ... }
    | mdTuple
    // !7
    | metadataID
    | specializedMDNode
;

// --- [ metadata String ] -----------------------------------------------------

// ref: ParseMDString
//
//   ::= '!' STRINGCONSTANT

mdString
    : '!' stringLit
;

// --- [ metadata Attachment ] -------------------------------------------------

// ref: ParseMetadataAttachment
//
//   ::= !dbg !42

metadataAttachment
    : metadataName mdNode
;

// --- [ metadata Node ] -------------------------------------------------------

// ref: ParseMDNode
//
//  ::= !{ ... }
//  ::= !7
//  ::= !diLocation(...)

mdNode
    // !{ ... }
    : mdTuple
    // !42
    | metadataID
    | specializedMDNode
;

// ### [ Helper productions ] ##################################################

// ref: ParseOptionalFunctionMetadata
//
//   ::= (!dbg !57)*

metadataAttachments
    : metadataAttachmentList ?
;

metadataAttachmentList
    : metadataAttachment
    | metadataAttachmentList metadataAttachment
;

// ref: ParseInstructionMetadata
//
//   ::= !dbg !42 (',' !dbg !57)*

optCommaSepMetadataAttachmentList
    : (',' commaSepMetadataAttachmentList) ?
;

commaSepMetadataAttachmentList
    : metadataAttachment
    | commaSepMetadataAttachmentList ',' metadataAttachment
;

// --- [ Specialized metadata Nodes ] ------------------------------------------

// https://llvm.org/docs/LangRef.html#specialized-metadata-nodes

// ref: ParseSpecializedMDNode

specializedMDNode
    : diCompileUnit
    | diFile
    | diBasicType
    | diSubroutineType
    | diDerivedType
    | diCompositeType
    | diSubrange
    | diEnumerator
    | diTemplateTypeParameter
    | diTemplateValueParameter
    | diModule // not in spec as of 2018-02-21
    | diNamespace
    | diGlobalVariable
    | diSubprogram
    | diLexicalBlock
    | diLexicalBlockFile
    | diLocation
    | diLocalVariable
    | diExpression
    | diGlobalVariableExpression // not in spec as of 2018-02-21
    | diObjCProperty
    | diImportedEntity
    | diMacro
    | diMacroFile
    | genericDINode // not in spec as of 2018-02-21
;

// ~~~ [ diCompileUnit ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#dicompileunit

// ref: ParseDICompileUnit
//
//   ::= !diCompileUnit(language: DW_LANG_C99, file: !0, producer: 'clang',
//                      isOptimized: true, flags: '-O2', runtimeVersion: 1,
//                      splitDebugFilename: 'abc.debug',
//                      emissionKind: FullDebug, enums: !1, retainedTypes: !2,
//                      globals: !4, imports: !5, macros: !6, dwoId: 0x0abcd)
//
//  REQUIRED(language, DwarfLangField, );
//  REQUIRED(file, mdField, (AllowNull false));
//  OPTIONAL(producer, mdStringField, );
//  OPTIONAL(isOptimized, mdBoolField, );
//  OPTIONAL(flags, mdStringField, );
//  OPTIONAL(runtimeVersion, mdUnsignedField, (0, UINT32_MAX));
//  OPTIONAL(splitDebugFilename, mdStringField, );
//  OPTIONAL(emissionKind, EmissionKindField, );
//  OPTIONAL(enums, mdField, );
//  OPTIONAL(retainedTypes, mdField, );
//  OPTIONAL(globals, mdField, );
//  OPTIONAL(imports, mdField, );
//  OPTIONAL(macros, mdField, );
//  OPTIONAL(dwoId, mdUnsignedField, );
//  OPTIONAL(splitDebugInlining, mdBoolField, = true);
//  OPTIONAL(debugInfoForProfiling, mdBoolField, = false);
//  OPTIONAL(gnuPubnames, mdBoolField, = false);

diCompileUnit
    : '!DICompileUnit' '(' diCompileUnitFields ')'
;

diCompileUnitFields
    : diCompileUnitFieldList ?
;

diCompileUnitFieldList
    : diCompileUnitField
    | diCompileUnitFieldList ',' diCompileUnitField
;

diCompileUnitField
    : 'language:' dwarfLang
    | fileField
    | 'producer:' stringLit
    | isOptimizedField
    | 'flags:' stringLit
    | 'runtimeVersion:' intLit
    | 'splitDebugFilename:' stringLit
    | 'emissionKind:' emissionKind
    | 'enums:' mdField
    | 'retainedTypes:' mdField
    | 'globals:' mdField
    | 'imports:' mdField
    | 'macros:' mdField
    | 'dwoId:' intLit
    | 'splitDebugInlining:' boolLit
    | 'debugInfoForProfiling:' boolLit
    | 'gnuPubnames:' boolLit
;

// ~~~ [ diFile ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#difile

// ref: ParseDIFileType
//
//   ::= !diFileType(filename: 'path/to/file', directory: '/path/to/dir'
//                   checksumkind: CSK_MD5,
//                   checksum: '000102030405060708090a0b0c0d0e0f')
//
//  REQUIRED(filename, mdStringField, );
//  REQUIRED(directory, mdStringField, );
//  OPTIONAL(checksumkind, ChecksumKindField, (diFile::CSK_MD5));
//  OPTIONAL(checksum, mdStringField, );

diFile
    : '!DIFile' '(' diFileFields ')'
;

diFileFields
    : diFileFieldList ?
;

diFileFieldList
    : diFileField
    | diFileFieldList ',' diFileField
;

diFileField
    : 'filename:' stringLit
    | 'directory:' stringLit
    | 'checksumkind:' checksumkind
    | 'checksum:' stringLit
;

// ~~~ [ diBasicType ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#dibasictype

// ref: ParseDIBasicType
//
//   ::= !diBasicType(tag: DW_TAG_base_type, name: 'int', size: 32, align: 32)
//
//  OPTIONAL(tag, DwarfTagField, (dwarf::DW_TAG_base_type));
//  OPTIONAL(name, mdStringField, );
//  OPTIONAL(size, mdUnsignedField, (0, UINT64_MAX));
//  OPTIONAL(align, mdUnsignedField, (0, UINT32_MAX));
//  OPTIONAL(encoding, DwarfAttEncodingField, );

diBasicType
    : '!DIBasicType' '(' diBasicTypeFields ')'
;

diBasicTypeFields
    : diBasicTypeFieldList ?
;

diBasicTypeFieldList
    : diBasicTypeField
    | diBasicTypeFieldList ',' diBasicTypeField
;

diBasicTypeField
    : tagField
    | nameField
    | sizeField
    | alignField
    | 'encoding:' dwarfAttEncoding
;

// ~~~ [ diSubroutineType ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#disubroutinetype

// ref: ParseDISubroutineType
//
//  OPTIONAL(flags, diFlagField, );
//  OPTIONAL(cc, DwarfCCField, );
//  REQUIRED(types, mdField, );

diSubroutineType
    : '!DISubroutineType' '(' diSubroutineTypeFields ')'
;

diSubroutineTypeFields
    : diSubroutineTypeFieldList ?
;

diSubroutineTypeFieldList
    : diSubroutineTypeField
    | diSubroutineTypeFieldList ',' diSubroutineTypeField
;

diSubroutineTypeField
    : flagsField
    | 'cc:' dwarfCC
    | 'types:' mdField
;

// ~~~ [ diDerivedType ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#diderivedtype

// ref: ParseDIDerivedType
//
//   ::= !diDerivedType(tag: DW_TAG_pointer_type, name: 'int', file: !0,
//                      line: 7, scope: !1, baseType: !2, size: 32,
//                      align: 32, offset: 0, flags: 0, extraData: !3,
//                      dwarfAddressSpace: 3)
//
//  REQUIRED(tag, DwarfTagField, );
//  OPTIONAL(name, mdStringField, );
//  OPTIONAL(scope, mdField, );
//  OPTIONAL(file, mdField, );
//  OPTIONAL(line, lineField, );
//  REQUIRED(baseType, mdField, );
//  OPTIONAL(size, mdUnsignedField, (0, UINT64_MAX));
//  OPTIONAL(align, mdUnsignedField, (0, UINT32_MAX));
//  OPTIONAL(offset, mdUnsignedField, (0, UINT64_MAX));
//  OPTIONAL(flags, diFlagField, );
//  OPTIONAL(extraData, mdField, );
//  OPTIONAL(dwarfAddressSpace, mdUnsignedField, (UINT32_MAX, UINT32_MAX));

diDerivedType
    : '!DIDerivedType' '(' diDerivedTypeFields ')'
;

diDerivedTypeFields
    : diDerivedTypeFieldList ?
;

diDerivedTypeFieldList
    : diDerivedTypeField
    | diDerivedTypeFieldList ',' diDerivedTypeField
;

diDerivedTypeField
    : tagField
    | nameField
    | scopeField
    | fileField
    | lineField
    | baseTypeField
    | sizeField
    | alignField
    | offsetField
    | flagsField
    | 'extraData:' mdField
    | 'dwarfAddressSpace:' intLit
;

// ~~~ [ diCompositeType ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#dicompositetype

// ref: ParseDICompositeType
//
//  REQUIRED(tag, DwarfTagField, );
//  OPTIONAL(name, mdStringField, );
//  OPTIONAL(scope, mdField, );
//  OPTIONAL(file, mdField, );
//  OPTIONAL(line, lineField, );
//  OPTIONAL(baseType, mdField, );
//  OPTIONAL(size, mdUnsignedField, (0, UINT64_MAX));
//  OPTIONAL(align, mdUnsignedField, (0, UINT32_MAX));
//  OPTIONAL(offset, mdUnsignedField, (0, UINT64_MAX));
//  OPTIONAL(flags, diFlagField, );
//  OPTIONAL(elements, mdField, );
//  OPTIONAL(runtimeLang, DwarfLangField, );
//  OPTIONAL(vtableHolder, mdField, );
//  OPTIONAL(templateParams, mdField, );
//  OPTIONAL(identifier, mdStringField, );
//  OPTIONAL(discriminator, mdField, );

diCompositeType
    : '!DICompositeType' '(' diCompositeTypeFields ')'
;

diCompositeTypeFields
    : diCompositeTypeFieldList ?
;

diCompositeTypeFieldList
    : diCompositeTypeField
    | diCompositeTypeFieldList ',' diCompositeTypeField
;

diCompositeTypeField
    : tagField
    | nameField
    | scopeField
    | fileField
    | lineField
    | baseTypeField
    | sizeField
    | alignField
    | offsetField
    | flagsField
    | 'elements:' mdField
    | 'runtimeLang:' dwarfLang
    | 'vtableHolder:' mdField
    | templateParamsField
    | 'identifier:' stringLit
    | 'discriminator:' mdField
;

// ~~~ [ diSubrange ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#disubrange

// ref: ParseDISubrange
//
//   ::= !diSubrange(count: 30, lowerBound: 2)
//   ::= !diSubrange(count: !node, lowerBound: 2)
//
//  REQUIRED(count, mdSignedOrMDField, (-1, -1, INT64_MAX, false));
//  OPTIONAL(lowerBound, mdSignedField, );

diSubrange
    : '!DISubrange' '(' diSubrangeFields ')'
;

diSubrangeFields
    : diSubrangeFieldList ?
;

diSubrangeFieldList
    : diSubrangeField
    | diSubrangeFieldList ',' diSubrangeField
;

diSubrangeField
    : 'count:' intOrMDField
    | 'lowerBound:' intLit
;

// ~~~ [ diEnumerator ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#dienumerator

// ref: ParseDIEnumerator
//
//   ::= !diEnumerator(value: 30, isUnsigned: true, name: 'SomeKind')
//
//  REQUIRED(name, mdStringField, );
//  REQUIRED(value, mdSignedOrUnsignedField, );
//  OPTIONAL(isUnsigned, mdBoolField, (false));

diEnumerator
    : '!DIEnumerator' '(' diEnumeratorFields ')'
;

diEnumeratorFields
    : diEnumeratorFieldList ?
;

diEnumeratorFieldList
    : diEnumeratorField
    | diEnumeratorFieldList ',' diEnumeratorField
;

diEnumeratorField
    : nameField
    | 'value:' intLit
    | 'isUnsigned:' boolLit
;

// ~~~ [ diTemplateTypeParameter ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#ditemplatetypeparameter

// ref: ParseDITemplateTypeParameter
//
//   ::= !diTemplateTypeParameter(name: 'Ty', type: !1)
//
//  OPTIONAL(name, mdStringField, );
//  REQUIRED(type, mdField, );

diTemplateTypeParameter
    : '!DITemplateTypeParameter' '(' diTemplateTypeParameterFields ')'
;

diTemplateTypeParameterFields
    : diTemplateTypeParameterFieldList ?
;

diTemplateTypeParameterFieldList
    : diTemplateTypeParameterField
    | diTemplateTypeParameterFieldList ',' diTemplateTypeParameterField
;

diTemplateTypeParameterField
    : nameField
    | typeField
;

// ~~~ [ diTemplateValueParameter ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#ditemplatevalueparameter

// ref: ParseDITemplateValueParameter
//
//   ::= !diTemplateValueParameter(tag: DW_TAG_template_value_parameter,
//                                 name: 'V', type: !1, value: i32 7)
//
//  OPTIONAL(tag, DwarfTagField, (dwarf::DW_TAG_template_value_parameter));
//  OPTIONAL(name, mdStringField, );
//  OPTIONAL(type, mdField, );
//  REQUIRED(value, mdField, );

diTemplateValueParameter
    : '!DITemplateValueParameter' '(' diTemplateValueParameterFields ')'
;

diTemplateValueParameterFields
    : diTemplateValueParameterFieldList ?
;

diTemplateValueParameterFieldList
    : diTemplateValueParameterField
    | diTemplateValueParameterFieldList ',' diTemplateValueParameterField
;

diTemplateValueParameterField
    : tagField
    | nameField
    | typeField
    | 'value:' mdField
;

// ~~~ [ diModule ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseDIModule
//
//   ::= !diModule(scope: !0, name: 'SomeModule', configMacros: '-DNDEBUG',
//                 includePath: '/usr/include', isysroot: '/')
//
//  REQUIRED(scope, mdField, );
//  REQUIRED(name, mdStringField, );
//  OPTIONAL(configMacros, mdStringField, );
//  OPTIONAL(includePath, mdStringField, );
//  OPTIONAL(isysroot, mdStringField, );

diModule
    : '!DIModule' '(' diModuleFields ')'
;

diModuleFields
    : diModuleFieldList ?
;

diModuleFieldList
    : diModuleField
    | diModuleFieldList ',' diModuleField
;

diModuleField
    : scopeField
    | nameField
    | 'configMacros:' stringLit
    | 'includePath:' stringLit
    | 'isysroot:' stringLit
;

// ~~~ [ diNamespace ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#dinamespace

// ref: ParseDINamespace
//
//   ::= !diNamespace(scope: !0, file: !2, name: 'SomeNamespace', line: 9)
//
//  REQUIRED(scope, mdField, );
//  OPTIONAL(name, mdStringField, );
//  OPTIONAL(exportSymbols, mdBoolField, );

diNamespace
    : '!DINamespace' '(' diNamespaceFields ')'
;

diNamespaceFields
    : diNamespaceFieldList ?
;

diNamespaceFieldList
    : diNamespaceField
    | diNamespaceFieldList ',' diNamespaceField
;

diNamespaceField
    : scopeField
    | nameField
    | 'exportSymbols:' boolLit
;

// ~~~ [ diGlobalVariable ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#diglobalvariable

// ref: ParseDIGlobalVariable
//
//   ::= !diGlobalVariable(scope: !0, name: 'foo', linkageName: 'foo',
//                         file: !1, line: 7, type: !2, isLocal: false,
//                         isDefinition: true, declaration: !3, align: 8)
//
//  REQUIRED(name, mdStringField, (AllowEmpty false));
//  OPTIONAL(scope, mdField, );
//  OPTIONAL(linkageName, mdStringField, );
//  OPTIONAL(file, mdField, );
//  OPTIONAL(line, lineField, );
//  OPTIONAL(type, mdField, );
//  OPTIONAL(isLocal, mdBoolField, );
//  OPTIONAL(isDefinition, mdBoolField, (true));
//  OPTIONAL(declaration, mdField, );
//  OPTIONAL(align, mdUnsignedField, (0, UINT32_MAX));

diGlobalVariable
    : '!DIGlobalVariable' '(' diGlobalVariableFields ')'
;

diGlobalVariableFields
    : diGlobalVariableFieldList ?
;

diGlobalVariableFieldList
    : diGlobalVariableField
    | diGlobalVariableFieldList ',' diGlobalVariableField
;

diGlobalVariableField
    : nameField
    | scopeField
    | linkageNameField
    | fileField
    | lineField
    | typeField
    | isLocalField
    | isDefinitionField
    | declarationField
    | alignField
;

// ~~~ [ diSubprogram ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#disubprogram

// ref: ParseDISubprogram
//
//   ::= !diSubprogram(scope: !0, name: 'foo', linkageName: '_Zfoo',
//                     file: !1, line: 7, type: !2, isLocal: false,
//                     isDefinition: true, scopeLine: 8, containingType: !3,
//                     virtuality: DW_VIRTUALTIY_pure_virtual,
//                     virtualIndex: 10, thisAdjustment: 4, flags: 11,
//                     isOptimized: false, templateParams: !4, declaration: !5,
//                     variables: !6, thrownTypes: !7)
//
//  OPTIONAL(name, mdStringField, );
//  OPTIONAL(scope, mdField, );
//  OPTIONAL(linkageName, mdStringField, );
//  OPTIONAL(file, mdField, );
//  OPTIONAL(line, lineField, );
//  OPTIONAL(type, mdField, );
//  OPTIONAL(isLocal, mdBoolField, );
//  OPTIONAL(isDefinition, mdBoolField, (true));
//  OPTIONAL(scopeLine, lineField, );
//  OPTIONAL(containingType, mdField, );
//  OPTIONAL(virtuality, DwarfVirtualityField, );
//  OPTIONAL(virtualIndex, mdUnsignedField, (0, UINT32_MAX));
//  OPTIONAL(thisAdjustment, mdSignedField, (0, INT32_MIN, INT32_MAX));
//  OPTIONAL(flags, diFlagField, );
//  OPTIONAL(isOptimized, mdBoolField, );
//  OPTIONAL(unit, mdField, );
//  OPTIONAL(templateParams, mdField, );
//  OPTIONAL(declaration, mdField, );
//  OPTIONAL(variables, mdField, );
//  OPTIONAL(thrownTypes, mdField, );

diSubprogram
    : '!DISubprogram' '(' diSubprogramFields ')'
;

diSubprogramFields
    : diSubprogramFieldList ?
;

diSubprogramFieldList
    : diSubprogramField
    | diSubprogramFieldList ',' diSubprogramField
;

diSubprogramField
    : nameField
    | scopeField
    | linkageNameField
    | fileField
    | lineField
    | typeField
    | isLocalField
    | isDefinitionField
    | 'scopeLine:' intLit
    | 'containingType:' mdField
    | 'virtuality:' dwarfVirtuality
    | 'virtualIndex:' intLit
    | 'thisAdjustment:' intLit
    | flagsField
    | isOptimizedField
    | 'unit:' mdField
    | templateParamsField
    | declarationField
    | 'variables:' mdField
    | 'thrownTypes:' mdField
;

// ~~~ [ diLexicalBlock ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#dilexicalblock

// ref: ParseDILexicalBlock
//
//   ::= !diLexicalBlock(scope: !0, file: !2, line: 7, column: 9)
//
//  REQUIRED(scope, mdField, (AllowNull false));
//  OPTIONAL(file, mdField, );
//  OPTIONAL(line, lineField, );
//  OPTIONAL(column, columnField, );

diLexicalBlock
    : '!DILexicalBlock' '(' diLexicalBlockFields ')'
;

diLexicalBlockFields
    : diLexicalBlockFieldList ?
;

diLexicalBlockFieldList
    : diLexicalBlockField
    | diLexicalBlockFieldList ',' diLexicalBlockField
;

diLexicalBlockField
    : scopeField
    | fileField
    | lineField
    | columnField
;

// ~~~ [ diLexicalBlockFile ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#dilexicalblockfile

// ref: ParseDILexicalBlockFile
//
//   ::= !diLexicalBlockFile(scope: !0, file: !2, discriminator: 9)
//
//  REQUIRED(scope, mdField, (AllowNull false));
//  OPTIONAL(file, mdField, );
//  REQUIRED(discriminator, mdUnsignedField, (0, UINT32_MAX));

diLexicalBlockFile
    : '!DILexicalBlockFile' '(' diLexicalBlockFileFields ')'
;

diLexicalBlockFileFields
    : diLexicalBlockFileFieldList ?
;

diLexicalBlockFileFieldList
    : diLexicalBlockFileField
    | diLexicalBlockFileFieldList ',' diLexicalBlockFileField
;

diLexicalBlockFileField
    : scopeField
    | fileField
    | 'discriminator:' intLit
;

// ~~~ [ diLocation ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#dilocation

// ref: ParseDILocation
//
//   ::= !diLocation(line: 43, column: 8, scope: !5, inlinedAt: !6)
//
//  OPTIONAL(line, lineField, );
//  OPTIONAL(column, columnField, );
//  REQUIRED(scope, mdField, (AllowNull false));
//  OPTIONAL(inlinedAt, mdField, );

diLocation
    : '!DILocation' '(' diLocationFields ')'
;

diLocationFields
    : diLocationFieldList ?
;

diLocationFieldList
    : diLocationField
    | diLocationFieldList ',' diLocationField
;

diLocationField
    : lineField
    | columnField
    | scopeField
    | 'inlinedAt:' mdField
;

// ~~~ [ diLocalVariable ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#dilocalvariable

// ref: ParseDILocalVariable
//
//   ::= !diLocalVariable(arg: 7, scope: !0, name: 'foo',
//                        file: !1, line: 7, type: !2, arg: 2, flags: 7,
//                        align: 8)
//   ::= !diLocalVariable(scope: !0, name: 'foo',
//                        file: !1, line: 7, type: !2, arg: 2, flags: 7,
//                        align: 8)
//
//  OPTIONAL(name, mdStringField, );
//  OPTIONAL(arg, mdUnsignedField, (0, UINT16_MAX));
//  REQUIRED(scope, mdField, (AllowNull false));
//  OPTIONAL(file, mdField, );
//  OPTIONAL(line, lineField, );
//  OPTIONAL(type, mdField, );
//  OPTIONAL(flags, diFlagField, );
//  OPTIONAL(align, mdUnsignedField, (0, UINT32_MAX));

diLocalVariable
    : '!DILocalVariable' '(' diLocalVariableFields ')'
;

diLocalVariableFields
    : diLocalVariableFieldList ?
;

diLocalVariableFieldList
    : diLocalVariableField
    | diLocalVariableFieldList ',' diLocalVariableField
;

diLocalVariableField
    : nameField
    | 'arg:' intLit
    | scopeField
    | fileField
    | lineField
    | typeField
    | flagsField
    | alignField
;

// ~~~ [ diExpression ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#diexpression

// ref: ParseDIExpression
//
//   ::= !diExpression(0, 7, -1)

diExpression
    : '!DIExpression' '(' diExpressionFields ')'
;

diExpressionFields
    : diExpressionFieldList ?
;

diExpressionFieldList
    : diExpressionField
    | diExpressionFieldList ',' diExpressionField
;

diExpressionField
    : INT_LIT
    | dwarfOp
;

// ~~~ [ diGlobalVariableExpression ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseDIGlobalVariableExpression
//
//   ::= !diGlobalVariableExpression(var: !0, expr: !1)
//
//  REQUIRED(var, mdField, );
//  REQUIRED(expr, mdField, );

diGlobalVariableExpression
    : '!DIGlobalVariableExpression' '(' diGlobalVariableExpressionFields ')'
;

diGlobalVariableExpressionFields
    : diGlobalVariableExpressionFieldList ?
;

diGlobalVariableExpressionFieldList
    : diGlobalVariableExpressionField
    | diGlobalVariableExpressionFieldList ',' diGlobalVariableExpressionField
;

diGlobalVariableExpressionField
    : 'var:' mdField
    | 'expr:' mdField
;

// ~~~ [ diObjCProperty ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#diobjcproperty

// ref: ParseDIObjCProperty
//
//   ::= !diObjCProperty(name: 'foo', file: !1, line: 7, setter: 'setFoo',
//                       getter: 'getFoo', attributes: 7, type: !2)
//
//  OPTIONAL(name, mdStringField, );
//  OPTIONAL(file, mdField, );
//  OPTIONAL(line, lineField, );
//  OPTIONAL(setter, mdStringField, );
//  OPTIONAL(getter, mdStringField, );
//  OPTIONAL(attributes, mdUnsignedField, (0, UINT32_MAX));
//  OPTIONAL(type, mdField, );

diObjCProperty
    : '!DIObjCProperty' '(' diObjCPropertyFields ')'
;

diObjCPropertyFields
    : diObjCPropertyFieldList ?
;

diObjCPropertyFieldList
    : diObjCPropertyField
    | diObjCPropertyFieldList ',' diObjCPropertyField
;

diObjCPropertyField
    : nameField
    | fileField
    | lineField
    | 'setter:' stringLit
    | 'getter:' stringLit
    | 'attributes:' intLit
    | typeField
;

// ~~~ [ diImportedEntity ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#diimportedentity

// ref: ParseDIImportedEntity
//
//   ::= !diImportedEntity(tag: DW_TAG_imported_module, scope: !0, entity: !1,
//                         line: 7, name: 'foo')
//
//  REQUIRED(tag, DwarfTagField, );
//  REQUIRED(scope, mdField, );
//  OPTIONAL(entity, mdField, );
//  OPTIONAL(file, mdField, );
//  OPTIONAL(line, lineField, );
//  OPTIONAL(name, mdStringField, );

diImportedEntity
    : '!DIImportedEntity' '(' diImportedEntityFields ')'
;

diImportedEntityFields
    : diImportedEntityFieldList ?
;

diImportedEntityFieldList
    : diImportedEntityField
    | diImportedEntityFieldList ',' diImportedEntityField
;

diImportedEntityField
    : tagField
    | scopeField
    | 'entity:' mdField
    | fileField
    | lineField
    | nameField
;

// ~~~ [ diMacro ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#dimacro

// ref: ParseDIMacro
//
//   ::= !diMacro(macinfo: type, line: 9, name: 'SomeMacro', value: 'SomeValue')
//
//  REQUIRED(type, DwarfMacinfoTypeField, );
//  OPTIONAL(line, lineField, );
//  REQUIRED(name, mdStringField, );
//  OPTIONAL(value, mdStringField, );

diMacro
    : '!DIMacro' '(' diMacroFields ')'
;

diMacroFields
    : diMacroFieldList ?
;

diMacroFieldList
    : diMacroField
    | diMacroFieldList ',' diMacroField
;

diMacroField
    : typeMacinfoField
    | lineField
    | nameField
    | 'value:' stringLit
;

// ~~~ [ diMacroFile ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// https://llvm.org/docs/LangRef.html#dimacrofile

// ref: ParseDIMacroFile
//
//   ::= !diMacroFile(line: 9, file: !2, nodes: !3)
//
//  OPTIONAL(type, DwarfMacinfoTypeField, (dwarf::DW_MACINFO_start_file));
//  OPTIONAL(line, lineField, );
//  REQUIRED(file, mdField, );
//  OPTIONAL(nodes, mdField, );

diMacroFile
    : '!DIMacroFile' '(' diMacroFileFields ')'
;

diMacroFileFields
    : diMacroFileFieldList ?
;

diMacroFileFieldList
    : diMacroFileField
    | diMacroFileFieldList ',' diMacroFileField
;

diMacroFileField
    : typeMacinfoField
    | lineField
    | fileField
    | 'nodes:' mdField
;

// ~~~ [ genericDINode ] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ref: ParseGenericDINode
//
//   ::= !genericDINode(tag: 15, header: '...', operands: {...})
//
//  REQUIRED(tag, DwarfTagField, );
//  OPTIONAL(header, mdStringField, );
//  OPTIONAL(operands, mdFieldList, );

genericDINode
    : '!GenericDINode' '(' genericDINodeFields ')'
;

genericDINodeFields
    : genericDINodeFieldList ?
;

genericDINodeFieldList
    : genericDINodeField
    | genericDINodeFieldList ',' genericDINodeField
;

genericDINodeField
    : tagField
    | 'header:' stringLit
    | 'operands:' mdFields
;

// ### [ Helper productions ] ##################################################

fileField
    : 'file:' mdField
;

isOptimizedField
    : 'isOptimized:' boolLit
;

tagField
    : 'tag:' dwarfTag
;

nameField
    : 'name:' stringLit
;

sizeField
    : 'size:' intLit
;

alignField
    : 'align:' intLit
;

flagsField
    : 'flags:' diFlagList
;

lineField
    : 'line:' intLit
;

scopeField
    : 'scope:' mdField
;

baseTypeField
    : 'baseType:' mdField
;

offsetField
    : 'offset:' intLit
;

templateParamsField
    : 'templateParams:' mdField
;

// ref: ParseMDField(mdSignedOrMDField &)

intOrMDField
    : INT_LIT
    | mdField
;

typeField
    : 'type:' mdField
;

linkageNameField
    : 'linkageName:' stringLit
;

isLocalField
    : 'isLocal:' boolLit
;

isDefinitionField
    : 'isDefinition:' boolLit
;

declarationField
    : 'declaration:' mdField
;

columnField
    : 'column:' intLit
;

typeMacinfoField
    : 'type:' dwarfMacinfo
;

checksumkind
    // CSK_foo
    : CHECKSUM_KIND
;

// ref: ParseMDField(diFlagField &)
//
//  ::= uint32
//  ::= diFlagVector
//  ::= diFlagVector '|' diFlagFwdDecl '|' uint32 '|' diFlagPublic

diFlagList
    : diFlag
    | diFlagList '|' diFlag
;

diFlag
    : intLit
    // diFlagFoo
    | DI_FLAG
;

// ref: ParseMDField(DwarfAttEncodingField &)

dwarfAttEncoding
    : intLit
    // DW_ATE_foo
    | DWARF_ATT_ENCODING
;

// ref: ParseMDField(DwarfCCField &Result)

dwarfCC
    : intLit
    // DW_CC_foo
    | DWARF_CC
;

// ref: ParseMDField(DwarfLangField &)

dwarfLang
    : intLit
    // DW_LANG_foo
    | DWARF_LANG
;

// ref: ParseMDField(DwarfMacinfoTypeField &)

dwarfMacinfo
    : intLit
    // DW_MACINFO_foo
    | DWARF_MACINFO
;

dwarfOp
    // DW_OP_foo
    : DWARF_OP
;

// ref: ParseMDField(DwarfTagField &)

dwarfTag
    : intLit
    // DW_TAG_foo
    | DWARF_TAG
;

// ref: ParseMDField(DwarfVirtualityField &)

dwarfVirtuality
    : intLit
    // DW_VIRTUALITY_foo
    | DWARF_VIRTUALITY
;

emissionKind
    : intLit
    | 'FullDebug'
    | 'LineTablesOnly'
    | 'NoDebug'
;

// ### [ Helper productions ] ##################################################

typeValues
    : typeValueList ?
;

typeValueList
    : typeValue
    | typeValueList typeValue
;

commaSepTypeValueList
    : typeValue
    | commaSepTypeValueList ',' typeValue
;

typeValue
    : llvmType value
;

typeConsts
    : typeConstList ?
;

typeConstList
    : typeConst
    | typeConstList ',' typeConst
;

typeConst
    : llvmType constant
;

// ref: ParseOptionalAlignment
//
//   ::= empty
//   ::= 'align' 4

alignment
    : 'align' INT_LIT
;

// ref: parseAllocSizeArguments

allocSize
    : 'allocsize' '(' INT_LIT ')'
    | 'allocsize' '(' INT_LIT ',' INT_LIT ')'
;

// ref: ParseParameterList
//
//    ::= '(' ')'
//    ::= '(' arg (',' arg)* ')'
//  arg
//    ::= type OptionalAttributes value OptionalAttributes

args
    : (
        '...'
      | argList
      | argList ',' '...'
      ) ?
;

argList
    : arg
    | argList ',' arg
;

// ref: ParseMetadataAsValue
//
//  ::= metadata i32 %local
//  ::= metadata i32 @global
//  ::= metadata i32 7
//  ::= metadata !0
//  ::= metadata !{...}
//  ::= metadata !'string'

arg
    : (llvmType optAddrSpace '*' | concreteNonRecType) paramAttrs value
    | metadataType metadata
;

// ref: ParseOrdering
//
//   ::= AtomicOrdering

atomicOrdering
    : 'acq_rel'
    | 'acquire'
    | 'monotonic'
    | 'release'
    | 'seq_cst'
    | 'unordered'
;

// ref: ParseOptionalCallingConv
//
//   ::= empty
//   ::= 'ccc'
//   ::= 'fastcc'
//   ::= 'intel_ocl_bicc'
//   ::= 'coldcc'
//   ::= 'x86_stdcallcc'
//   ::= 'x86_fastcallcc'
//   ::= 'x86_thiscallcc'
//   ::= 'x86_vectorcallcc'
//   ::= 'arm_apcscc'
//   ::= 'arm_aapcscc'
//   ::= 'arm_aapcs_vfpcc'
//   ::= 'msp430_intrcc'
//   ::= 'avr_intrcc'
//   ::= 'avr_signalcc'
//   ::= 'ptx_kernel'
//   ::= 'ptx_device'
//   ::= 'spir_func'
//   ::= 'spir_kernel'
//   ::= 'x86_64_sysvcc'
//   ::= 'win64cc'
//   ::= 'webkit_jscc'
//   ::= 'anyregcc'
//   ::= 'preserve_mostcc'
//   ::= 'preserve_allcc'
//   ::= 'ghccc'
//   ::= 'swiftcc'
//   ::= 'x86_intrcc'
//   ::= 'hhvmcc'
//   ::= 'hhvm_ccc'
//   ::= 'cxx_fast_tlscc'
//   ::= 'amdgpu_vs'
//   ::= 'amdgpu_ls'
//   ::= 'amdgpu_hs'
//   ::= 'amdgpu_es'
//   ::= 'amdgpu_gs'
//   ::= 'amdgpu_ps'
//   ::= 'amdgpu_cs'
//   ::= 'amdgpu_kernel'
//   ::= 'cc' UINT

optCallingConv
    : callingConv ?
;

callingConv
    : 'amdgpu_cs'
    | 'amdgpu_es'
    | 'amdgpu_gs'
    | 'amdgpu_hs'
    | 'amdgpu_kernel'
    | 'amdgpu_ls'
    | 'amdgpu_ps'
    | 'amdgpu_vs'
    | 'anyregcc'
    | 'arm_aapcs_vfpcc'
    | 'arm_aapcscc'
    | 'arm_apcscc'
    | 'avr_intrcc'
    | 'avr_signalcc'
    | 'ccc'
    | 'coldcc'
    | 'cxx_fast_tlscc'
    | 'fastcc'
    | 'ghccc'
    | 'hhvm_ccc'
    | 'hhvmcc'
    | 'intel_ocl_bicc'
    | 'msp430_intrcc'
    | 'preserve_allcc'
    | 'preserve_mostcc'
    | 'ptx_device'
    | 'ptx_kernel'
    | 'spir_func'
    | 'spir_kernel'
    | 'swiftcc'
    | 'webkit_jscc'
    | 'win64cc'
    | 'x86_64_sysvcc'
    | 'x86_fastcallcc'
    | 'x86_intrcc'
    | 'x86_regcallcc'
    | 'x86_stdcallcc'
    | 'x86_thiscallcc'
    | 'x86_vectorcallcc'
    | 'cc' INT_LIT
;

// ref: parseOptionalComdat

optComdat
    : comdat ?
;

comdat
    : 'comdat'
    | 'comdat' '(' comdatName ')'
;

dereferenceable
    : 'dereferenceable' '(' INT_LIT ')'
    | 'dereferenceable_or_null' '(' INT_LIT ')'
;

// https://llvm.org/docs/LangRef.html#dll-storage-classes

// ref: ParseOptionalDLLStorageClass
//
//   ::= empty
//   ::= 'dllimport'
//   ::= 'dllexport'

optDLLStorageClass
    : dllStorageClass ?
;

dllStorageClass
    : 'dllexport'
    | 'dllimport'
;

optExact
    : 'exact' ?
;

// ref: ParseExceptionArgs

exceptionArgs
    : exceptionArgList ?
;

exceptionArgList
    : exceptionArg
    | exceptionArgList ',' exceptionArg
;

exceptionArg
    : (llvmType optAddrSpace '*' | concreteNonRecType) value
    | metadataType metadata
;

exceptionScope
    : noneConst
    | localIdent
;

// ref: EatFastMathFlagsIfPresent

fastMathFlags
    : fastMathFlagList ?
;

fastMathFlagList
    : fastMathFlag
    | fastMathFlagList fastMathFlag
;

fastMathFlag
    : 'afn'
    | 'arcp'
    | 'contract'
    | 'fast'
    | 'ninf'
    | 'nnan'
    | 'nsz'
    | 'reassoc'
;

// ref: ParseCmpPredicate

fpred
    : 'false'
    | 'oeq'
    | 'oge'
    | 'ogt'
    | 'ole'
    | 'olt'
    | 'one'
    | 'ord'
    | 'true'
    | 'ueq'
    | 'uge'
    | 'ugt'
    | 'ule'
    | 'ult'
    | 'une'
    | 'uno'
;

// ___ [ Function Attribute ] __________________________________________________

// ref: ParseFnAttributeValuePairs
//
//   ::= <attr> | <attr> '=' <value>

funcAttrs
    : funcAttrList ?
;

funcAttrList
    : funcAttr
    | funcAttrList funcAttr
;

funcAttr
    // not used in attribute groups.
    : attrGroupID
    // used in attribute groups.
    | 'align' '=' INT_LIT
    | 'alignstack' '=' INT_LIT
    // used in functions.
    | alignment
    | allocSize
    | stackAlignment
    | stringLit
    | stringLit '=' stringLit
    | 'alwaysinline'
    | 'argmemonly'
    | 'builtin'
    | 'cold'
    | 'convergent'
    | 'inaccessiblemem_or_argmemonly'
    | 'inaccessiblememonly'
    | 'inlinehint'
    | 'jumptable'
    | 'minsize'
    | 'naked'
    | 'nobuiltin'
    | 'noduplicate'
    | 'nofree'
    | 'noimplicitfloat'
    | 'noinline'
    | 'nonlazybind'
    | 'norecurse'
    | 'noredzone'
    | 'noreturn'
    | 'nounwind'
    | 'optnone'
    | 'optsize'
    | 'readnone'
    | 'readonly'
    | 'returns_twice'
    | 'safestack'
    | 'sanitize_address'
    | 'sanitize_hwaddress'
    | 'sanitize_memory'
    | 'sanitize_thread'
    | 'speculatable'
    | 'ssp'
    | 'sspreq'
    | 'sspstrong'
    | 'strictfp'
    | 'uwtable'
    | 'willreturn'
    | 'writeonly'
;

optInBounds
    : 'inbounds' ?
;

// ref: ParseIndexList
//
//    ::=  (',' uint32)+

indices
    : (',' indexList) ?
;

indexList
    : index
    | indexList ',' index
;

index
    : INT_LIT
;

// ref: ParseCmpPredicate

iPred
    : 'eq'
    | 'ne'
    | 'sge'
    | 'sgt'
    | 'sle'
    | 'slt'
    | 'uge'
    | 'ugt'
    | 'ule'
    | 'ult'
;

// https://llvm.org/docs/LangRef.html#linkage-types

// ref: ParseOptionalLinkage
//
//   ::= empty
//   ::= 'private'
//   ::= 'internal'
//   ::= 'weak'
//   ::= 'weak_odr'
//   ::= 'linkonce'
//   ::= 'linkonce_odr'
//   ::= 'available_externally'
//   ::= 'appending'
//   ::= 'common'
//   ::= 'extern_weak'
//   ::= 'external'

optLinkage
    : linkage ?
;

linkage
    : 'appending'
    | 'available_externally'
    | 'common'
    | 'internal'
    | 'linkonce'
    | 'linkonce_odr'
    | 'private'
    | 'weak'
    | 'weak_odr'
;

optExternLinkage
    : externLinkage ?
;

externLinkage
    : 'extern_weak'
    | 'external'
;

// ref: ParseOptionalOperandBundles
//
//    ::= empty
//    ::= '[' operandBundle [, operandBundle ]* ']'
//
//  operandBundle
//    ::= bundle-tag '(' ')'
//    ::= bundle-tag '(' type value [, type value ]* ')'
//
//  bundle-tag ::= String constant

operandBundles
    : ('[' operandBundleList ']') ?
;

operandBundleList
    : operandBundle
    | operandBundleList operandBundle
;

operandBundle
    : stringLit '(' typeValues ')'
;

overflowFlags
    : overflowFlagList ?
;

overflowFlagList
    : overflowFlag
    | overflowFlagList overflowFlag
;

overflowFlag
    : 'nsw'
    | 'nuw'
;

// ___ [ Parameter Attribute ] _________________________________________________

// ref: ParseOptionalParamAttrs

paramAttrs
    : paramAttrList ?
;

paramAttrList
    : paramAttr
    | paramAttrList paramAttr
;

// ref: ParseOptionalDerefAttrBytes
//
//   ::= empty
//   ::= AttrKind '(' 4 ')'

byval :
  'byval' '(' llvmType ')'
;

paramAttr
    : alignment
    | dereferenceable
    | stringLit
    | byval
    | 'immarg'
    | 'noundef'
    | 'inalloca'
    | 'inreg'
    | 'nest'
    | 'noalias'
    | 'nocapture'
    | 'nonnull'
    | 'readnone'
    | 'readonly'
    | 'returned'
    | 'signext'
    | 'sret'
    | 'swifterror'
    | 'swiftself'
    | 'writeonly'
    | 'zeroext'
;

// ref: ParseArgumentList
//
//   ::= '(' ArgTypeListI ')'
//  ArgTypeListI
//   ::= empty
//   ::= '...'
//   ::= ArgTypeList ',' '...'
//   ::= ArgType (',' ArgType)*

params
    : ( '...'
      | paramList
      | paramList ',' '...'
      ) ?
;

paramList
    : param
    | paramList ',' param
;

param
    : llvmType paramAttrs
    | llvmType paramAttrs localIdent
;

// https://llvm.org/docs/LangRef.html#runtime-preemption-model

// ref: ParseOptionalDSOLocal

optPreemptionSpecifier
    : preemptionSpecifier ?
;

preemptionSpecifier
    : 'dso_local'
    | 'dso_preemptable'
;

// ___ [ Return Attribute ] __________________________________________________

// ref: ParseOptionalReturnAttrs

returnAttrs
    : returnAttrList ?
;

returnAttrList
    : returnAttr
    | returnAttrList returnAttr
;

returnAttr
    : alignment
    | dereferenceable
    | stringLit
    | 'inreg'
    | 'noalias'
    | 'nonnull'
    | 'signext'
    | 'zeroext'
;

section
    : SECTION STRING_LIT
;

// ref: ParseOptionalStackAlignment
//
//   ::= empty
//   ::= 'alignstack' '(' 4 ')'
stackAlignment
    : 'alignstack' '(' INT_LIT ')'
;

// ref: ParseScope
//
//   ::= syncscope('singlethread' | '<target scope>')?

optSyncScope
    : ('syncscope' '(' STRING_LIT ')') ?
;

// ref: ParseOptionalThreadLocal
//
//   := empty
//   := 'thread_local'
//   := 'thread_local' '(' tlsModel ')'

threadLocal
    : 'thread_local'
    | 'thread_local' '(' tlsModel ')'
;

// ref: ParseTLSModel
//
//   := 'localdynamic'
//   := 'initialexec'
//   := 'localexec'

tlsModel
    : 'initialexec'
    | 'localdynamic'
    | 'localexec'
;

// ref: ParseOptionalUnnamedAddr

unnamedAddr
    : 'local_unnamed_addr'
    | 'unnamed_addr'
;

// https://llvm.org/docs/LangRef.html#visibility-styles

// ref: ParseOptionalVisibility
//
//   ::= empty
//   ::= 'default'
//   ::= 'hidden'
//   ::= 'protected'

visibility
    : DEFAULT
    | HIDDEN_VISIB
    | PROTECTED
;
