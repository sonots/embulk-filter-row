#!/bin/bash -e
ROOT_PATH=$(cd $(dirname $0); cd ..; pwd)
SRC_DIR="."

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

# ./yacc.macosx -J -Jclass=Parser -Jval=Foo -Jnoconstruct where.y
$ROOT_PATH/script/yacc -J -Jclass=Parser "$SRC_DIR/where.y"
java -jar ~/bin/jflex-1.6.1.jar where.flex
javac Parser.java ParserVal.java ParserNode.java Yylex.java
