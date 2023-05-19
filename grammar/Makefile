# Download ANTLR from https://www.antlr.org/download.html
ANTLR4 = antlr-4.12.0-complete.jar

llvm:
	java -jar $(ANTLR4) LLVMLexer.g4
	java -jar $(ANTLR4) -visitor LLVMParser.g4

clean:
	rm -f *.java
	rm -f *.interp
	rm -f *.tokens
