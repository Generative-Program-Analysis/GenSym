#! /usr/bin/env bash

ANTLR4=antlr-4.12.0-complete.jar

java -jar $ANTLR4 LLVMLexer.g4
java -jar $ANTLR4 -visitor LLVMParser.g4

DST=../src/main/java/
echo "Copy LLVM parsers into $DST"

for file in "LLVMLexer.java" "LLVMParserBaseVisitor.java" "LLVMParserListener.java" "LLVMParserBaseListener.java" "LLVMParser.java" "LLVMParserVisitor.java"
do
  sed -i "1ipackage gensym.llvm;$line" $file
  cp $file $DST/$file
done

