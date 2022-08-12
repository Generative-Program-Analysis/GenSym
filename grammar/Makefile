# Download ANTLR from https://www.antlr.org/download.html
ANTLR4 = antlr-4.9.3-complete.jar

all:
	java -jar $(ANTLR4) LLVMLexer.g4
	java -jar $(ANTLR4) -visitor LLVMParser.g4

clean:
	rm -f *.java
	rm -f *.interp
	rm -f *.tokens
