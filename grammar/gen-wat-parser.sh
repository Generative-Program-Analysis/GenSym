#! /usr/bin/env bash
#
ANTLR4=antlr-4.13.2-complete.jar

java -jar $ANTLR4 WatLexer.g4
java -jar $ANTLR4 -visitor WatParser.g4

DST=../src/main/java/wasm/
echo "Copy WAT parsers into $DST"

for file in "WatLexer.java" "WatParserBaseVisitor.java" "WatParserListener.java" "WatParserBaseListener.java" "WatParser.java" "WatParserVisitor.java"
do
  if [[ "$OSTYPE" == "darwin"* ]]; then
    sed -i '' $'1i\\\npackage gensym.wasm;\n' $file
  else
    sed -i "1ipackage gensym.wasm;$line" $file
  fi
  cp $file "$DST/$file"
  rm $file
done

rm *.tokens *.interp
