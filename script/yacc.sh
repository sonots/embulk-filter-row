#!/bin/bash -e
ROOT_PATH=$(cd $(dirname $0); cd ..; pwd)
SRC_DIR="src/main/java/org/embulk/filter/row/parser"

if [ $(uname) = "Darwin" ]; then
  platform="macosx"
else
  platform="linux"
fi

if [ ! -f "$ROOT_PATH/script/yacc" ]; then
  curl -s -L "http://downloads.sourceforge.net/project/byaccj/byaccj/1.15/byaccj1.15_${platform}.tar.gz" | tar xz -C $ROOT_PATH/script/
  mv $ROOT_PATH/script/yacc.${platform} $ROOT_PATH/script/yacc
  chmod a+x $ROOT_PATH/script/yacc
fi

# ./yacc.macosx -J -Jclass=Parser -Jval=Foo -Jnoconstruct parse.y
$ROOT_PATH/script/yacc -J -Jclass=Parser -Jpackage=org.embulk.filter.row.parser "$SRC_DIR/Parser.y"
mv Parser.java ParserVal.java $SRC_DIR/
