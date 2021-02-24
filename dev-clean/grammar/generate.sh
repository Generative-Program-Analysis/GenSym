#! /usr/bin/env bash

ANTLR4=antlr-4.9.1-complete.jar

java -jar $ANTLR4 LLVMLexer.g4
java -jar $ANTLR4 -visitor LLVMParser.g4
