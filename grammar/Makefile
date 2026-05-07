# Download ANTLR from https://www.antlr.org/download.html
ANTLR4 = antlr-4.13.0-complete.jar

CURRENT_DIR := $(shell pwd)
LLVM_OUTPUT = $(CURRENT_DIR)/../src/main/java/llvm
WASM_OUTPUT = $(CURRENT_DIR)/../src/main/java/wasm
LLVM_PACKAGE = gensym.llvm
WASM_PACKAGE = gensym.wasm

llvm:
	java -jar $(ANTLR4) -o $(LLVM_OUTPUT) -package $(LLVM_PACKAGE) LLVMLexer.g4
	java -jar $(ANTLR4) -o $(LLVM_OUTPUT) -package $(LLVM_PACKAGE) -visitor LLVMParser.g4

wasm:
	java -jar $(ANTLR4) -o $(WASM_OUTPUT) -package $(WASM_PACKAGE) WatLexer.g4
	java -jar $(ANTLR4) -o $(WASM_OUTPUT) -package $(WASM_PACKAGE) -visitor WatParser.g4

clean:
	rm -f *.java
	rm -f *.interp
	rm -f *.tokens
