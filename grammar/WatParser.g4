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

parser grammar WatParser;

options { tokenVocab=WatLexer; }

value
  : INT | FLOAT
  ;

/* Auxiliaries */

name
  : STRING_
  ;

/* Types */

numType
  : VALUE_TYPE
  ;

// exception handling and function references seems to have different definitions
// for refType, which is probably going to be unified in wasm 3.0
// https://webassembly.github.io/function-references/core/text/types.html
// https://webassembly.github.io/exception-handling/core/_download/WebAssembly.pdf
refType
  : FUNCREF                // equivalent to (ref null func)
  | EXTERNREF              // equivalent to (ref null extern)
  | LPAR REF idx RPAR      // here idx must be a heap type
  | LPAR REF NULL idx RPAR // here idx must be a heap type
  ;

vecType
  : V128
  ;

valType : numType | vecType | refType ;

heapType
  : FUNC
  | EXTERN
  | funcType
  ;

globalType
  : valType | LPAR MUT valType RPAR
  ;

defType
  : LPAR FUNC funcType RPAR
  | LPAR CONT idx RPAR
  ;

funcParamType
  : (LPAR PARAM (valType* | bindVar valType) RPAR)*
  ;

funcResType
  : (LPAR RESULT valType* RPAR)*
  ;

funcType
  : funcParamType funcResType
  ;

tableType
  : NAT NAT? refType
  ;

memoryType
  : NAT NAT?
  ;

typeUse
  : LPAR TYPE idx RPAR
  ;

/* Immediates */

literal
  : NAT | INT | FLOAT
  ;

idx
  : NAT | VAR
  ;

bindVar
  : VAR
  ;

/* Instructions & Expressions */

instr
  : plainInstr
  /* | callInstrInstr */
  | blockInstr
  | foldedInstr
  | resumeInstr
  | forLoop
  ;

forLoop
  : 'for' '(' instrList '|' instrList '|' instrList ')' instrList
  ;

plainInstr
  : UNREACHABLE
  | NOP
  | DROP
  | selectInstr
  | BR idx
  | BR_IF idx
  | BR_TABLE idx+
  | RETURN
  | CALL idx
  | RETURN_CALL idx
  | LOCAL_GET idx
  | LOCAL_SET idx
  | LOCAL_TEE idx
  | GLOBAL_GET idx
  | GLOBAL_SET idx
  | load offsetEq? alignEq?
  | store offsetEq? alignEq?
  | MEMORY_SIZE
  | MEMORY_GROW
  | MEMORY_FILL
  | MEMORY_COPY
  | MEMORY_INIT idx
  | CONST literal
  | SYMBOLIC
  | SYM_ASSERT
  | ALLOC
  | FREE
  | TEST
  | COMPARE
  | UNARY
  | BINARY
  | CONVERT
  | callIndirectInstr
  | CONTNEW idx
  | REFFUNC idx
  | SUSPEND idx
  | CONTBIND idx idx
  | CALLREF idx
  | REFNULL idx
  | REFISNULL
  // resumable try-catch extension:
  | RESUME0
  | THROW
  ;

resumeInstr
  : RESUME idx handlerInstr*
  ;

handlerInstr
  : LPAR ON idx idx RPAR
  ;

offsetEq : OFFSET_EQ NAT ;

alignEq: ALIGN_EQ NAT ;

load
    : numType LOAD (MEM_SIZE UNDERSCORE SIGN_POSTFIX)?
;

store
  : numType STORE (MEM_SIZE)?
;

selectInstr
  : numType SELECT
;

callIndirectInstr
  /* : CALL_INDIRECT typeUse? callInstrParams */
  : CALL_INDIRECT idx? typeUse
  | RETURN_CALL_INDIRECT idx? typeUse
  ;

callInstrParams
  : (LPAR PARAM valType* RPAR)* (LPAR RESULT valType* RPAR)*
  ;

/*
callInstrInstr
  : CALL_INDIRECT typeUse? callInstrParamsInstr
  ;
*/

callInstrParamsInstr
  : (LPAR PARAM valType* RPAR)* callInstrResultsInstr
  ;

callInstrResultsInstr
  : (LPAR RESULT valType * RPAR)* instr
  ;

blockInstr
  : BLOCK bindVar? block END bindVar?
  | LOOP bindVar? block END bindVar?
  | IF bindVar? block (ELSE bindVar? instrList)? END bindVar?
  // resumable try-catch extension:
  | TRY block CATCH block END
  ;

blockType
  : (LPAR RESULT valType RPAR)?
  | typeUse funcType
  | funcType // abbreviation
  ;

block
  : blockType instrList
  ;

foldedInstr
  : LPAR expr RPAR
  ;

expr
  : plainInstr expr*
  | CALL_INDIRECT callExprType
  | RETURN_CALL_INDIRECT callExprType
  | BLOCK bindVar? block
  | LOOP bindVar? block
  // | IF bindVar? ifBlock
  | IF bindVar? blockType foldedInstr* LPAR THEN instrList (LPAR ELSE instrList RPAR)?
  ;

callExprType
  : typeUse? callExprParams
  ;

callExprParams
  : (LPAR PARAM valType* RPAR)* callExprResults
  ;

callExprResults
  : (LPAR RESULT valType* RPAR)* expr*
  ;

/*
ifBlock
  : blockType ifBlock
  | expr* LPAR THEN instrList RPAR (LPAR ELSE instrList RPAR)?
  ;
*/

instrList
  : instr* callIndirectInstr?
  ;

constExpr
  : instrList
  ;

/* Functions */

function
  : LPAR FUNC bindVar? funcFields RPAR
  ;

funcFields
  : typeUse? funcFieldsBody
  | inlineImport typeUse? funcType
  | inlineExport funcFields
  ;

funcFieldsBody
  : funcType funcBody
  ;

funcBody
  : (LPAR LOCAL (valType* | bindVar valType) RPAR)* instrList
  ;

/* Tables, Memories & Globals */

offset
  : LPAR OFFSET constExpr RPAR
  | expr
  ;

// TODO: not sure about the the parsing rules here
// fow now, I only extend it to support declarative mode for ref.func
// like (elem declarative func 1)
// TBH I'm not even sure what the `func 1` should count as
// TODO: align with the rules here:
// https://webassembly.github.io/function-references/core/_download/WebAssembly.pdf
elem
  : LPAR ELEM idx? LPAR instr RPAR idx* RPAR
  | LPAR ELEM idx? offset idx* RPAR
  | LPAR ELEM idx? DECLARE FUNC idx* RPAR
  ;

table
  : LPAR TABLE bindVar? tableField RPAR
  ;

tableField
  : tableType
  | inlineImport tableType
  | inlineExport tableField
  | refType LPAR ELEM idx* RPAR
  ;

data
  : LPAR DATA idx? LPAR instr RPAR STRING_* RPAR
  | LPAR DATA idx? offset STRING_* RPAR
  ;

memory
  : LPAR MEMORY bindVar? memoryField RPAR
  ;

memoryField
  : memoryType
  | inlineImport memoryType
  | inlineExport memoryField
  | LPAR DATA STRING_* RPAR
  ;

global
  : LPAR GLOBAL bindVar? globalField RPAR
  ;

globalField
  : globalType constExpr
  | inlineImport globalType
  | inlineExport globalField
  ;

/* Imports & Exports */

importDesc
  : LPAR FUNC bindVar? typeUse RPAR
  | LPAR FUNC bindVar? funcType RPAR
  | LPAR TABLE bindVar? tableType RPAR
  | LPAR MEMORY bindVar? memoryType RPAR
  | LPAR GLOBAL bindVar? globalType RPAR
  ;

simport
  :  LPAR IMPORT name name importDesc RPAR
  ;

inlineImport
  : LPAR IMPORT name name RPAR
  ;

exportDesc
  : LPAR FUNC idx RPAR
  | LPAR TABLE idx RPAR
  | LPAR MEMORY idx RPAR
  | LPAR GLOBAL idx RPAR
  ;

export_
  : LPAR EXPORT name exportDesc RPAR
  ;

inlineExport
  : LPAR EXPORT name RPAR
  ;

/* Tags */

// Note: this seems slightly off from
// https://webassembly.github.io/exception-handling/core/_download/WebAssembly.pdf
// based on the exception handling proposal, the funcType here is not required
// but output from `wasmfx-tools print` seems to leave the funcType in
tag
  : LPAR TAG bindVar? typeUse funcType RPAR
  ;


/* Modules */

typeDef
  : LPAR TYPE bindVar? defType RPAR
  ;

start_
  : LPAR START_ idx RPAR
  ;

moduleField
  : typeDef
  | global
  | table
  | memory
  | function
  | elem
  | data
  | start_
  | simport
  | export_
  | tag
  ;

module_
  : LPAR MODULE VAR? moduleField* RPAR
  ;

/* Scripts */

scriptModule
  : module_
  | LPAR MODULE VAR? (BIN | QUOTE) STRING_* RPAR
  | LPAR MODULE DEFINITION VAR? BIN STRING_* RPAR
  ;

action_
  : LPAR INVOKE VAR? name constList RPAR
  | LPAR GET VAR? name RPAR
  ;

assertion
  : LPAR ASSERT_MALFORMED scriptModule STRING_ RPAR
  | LPAR ASSERT_INVALID scriptModule STRING_ RPAR
  | LPAR ASSERT_UNLINKABLE scriptModule STRING_ RPAR
  | LPAR ASSERT_TRAP scriptModule STRING_ RPAR
  | LPAR ASSERT_RETURN action_ constList RPAR
  | LPAR ASSERT_RETURN_CANONICAL_NAN action_ RPAR
  | LPAR ASSERT_RETURN_ARITHMETIC_NAN action_ RPAR
  | LPAR ASSERT_TRAP action_ STRING_ RPAR
  | LPAR ASSERT_EXHAUSTION action_ STRING_ RPAR
  ;

cmd
  : action_
  | assertion
  | scriptModule
  | LPAR REGISTER name VAR? RPAR
  | meta
  | instance
  ;

instance
  : LPAR MODULE INSTANCE VAR? VAR? RPAR
  ;

meta
  : LPAR SCRIPT VAR? cmd* RPAR
  | LPAR INPUT VAR? STRING_ RPAR
  | LPAR OUTPUT VAR? STRING_ RPAR
  | LPAR OUTPUT VAR? RPAR
  ;

wconst
  : LPAR CONST literal RPAR
  ;

constList
  : wconst*
  ;

script
  : cmd* EOF
  | moduleField+ EOF
  ;

module
  : module_ EOF
  | moduleField* EOF
  ;
