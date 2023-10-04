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

refType
  : FUNCREF
  | EXTERNREF
  ;

vecType
  : V128
  ;

valType : numType | vecType | refType ;

heapType
  : FUNC
  | EXTERN
  ;

globalType
  : valType | LPAR MUT valType RPAR
  ;

defType
  : LPAR FUNC funcType RPAR
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
  | callInstrInstr
  | blockInstr
  | foldedInstr
  ;

plainInstr
  : UNREACHABLE
  | NOP
  | DROP
  | SELECT
  | BR idx
  | BR_IF idx
  | BR_TABLE idx+
  | RETURN
  | CALL idx
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
  ;

offsetEq : OFFSET_EQ NAT ;

alignEq: ALIGN_EQ NAT ;

load 
    : numType LOAD (MEM_SIZE UNDERSCORE SIGN_POSTFIX)?
;

store
  : numType STORE (MEM_SIZE)?
;

callIndirectInstr
  : CALL_INDIRECT typeUse? callInstrParams
  ;

callInstrParams
  : (LPAR PARAM valType* RPAR)* (LPAR RESULT valType* RPAR)*
  ;

callInstrInstr
  : CALL_INDIRECT typeUse? callInstrParamsInstr
  ;

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
  ;

blockType
  : LPAR RESULT valType RPAR
  ;

block
  : blockType? instrList
  ;

foldedInstr
  : LPAR expr RPAR
  ;

expr
  : plainInstr expr*
  | CALL_INDIRECT callExprType
  | BLOCK bindVar? block
  | LOOP bindVar? block
  // | IF bindVar? ifBlock
  | IF bindVar? blockType? foldedInstr* LPAR THEN instrList (LPAR ELSE instrList RPAR)?
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

elem
  : LPAR ELEM idx? LPAR instr RPAR idx* RPAR
  | LPAR ELEM idx? offset idx* RPAR
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
  ;

module_
  : LPAR MODULE VAR? moduleField* RPAR
  ;

/* Scripts */

scriptModule
  : module_
  | LPAR MODULE VAR? (BIN | QUOTE) STRING_* RPAR
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
