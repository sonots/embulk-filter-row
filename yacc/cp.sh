#!/bin/bash
cp where.y where.flex ../src/main/java/org/embulk/filter/row/where
sed 's/\/\/package/package/' ParserNode.java  > ../src/main/java/org/embulk/filter/row/where/ParserNode.java

