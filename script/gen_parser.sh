#!/bin/bash -e
BYACCJ_VERSION="1.15"
JFLEX_VERSION="1.6.1"

ROOT_PATH=$(cd $(dirname $0); cd ..; pwd)
SRC_DIR="src/main/java/org/embulk/filter/row/where"

if [ $(uname) = "Darwin" ]; then
  platform="macosx"
else
  platform="linux"
fi

if [ ! -f "$ROOT_PATH/script/yacc" ]; then
  curl -s -L "http://downloads.sourceforge.net/project/byaccj/byaccj/${BYACCJ_VERSION}/byaccj${BYACCJ_VERSION}_${platform}.tar.gz" | tar xz -C $ROOT_PATH/script/
  mv $ROOT_PATH/script/yacc.${platform} $ROOT_PATH/script/yacc
  chmod a+x $ROOT_PATH/script/yacc
fi

if [ ! -L "$ROOT_PATH/script/jflex" ]; then
  curl -s -L "http://jflex.de/release/jflex-${JFLEX_VERSION}.tar.gz" | tar xz -C $ROOT_PATH/script/
fi

# yacc
$ROOT_PATH/script/yacc -J -Jclass=Parser -Jpackage=org.embulk.filter.row.where "$SRC_DIR/Parser.y"
mv Parser.java ParserVal.java $SRC_DIR/

# flex
$ROOT_PATH/script/jflex/bin/jflex -d $SRC_DIR "$SRC_DIR/Yylex.flex"
