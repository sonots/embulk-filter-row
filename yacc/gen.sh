#!/bin/bash

# ./yacc.macosx -J -Jclass=Parser -Jval=Foo -Jpackage=org.embulk.filter.row -Jnoconstruct parse.y
./yacc.macosx -J -Jclass=Parser parse.y
git checkout ParserVal.java
