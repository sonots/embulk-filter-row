#!/bin/bash
cp Parser.y ../src/main/java/org/embulk/filter/row/parser
sed 's/\/\/package/package/' ParserNode.java  > ../src/main/java/org/embulk/filter/row/parser/ParserNode.java

