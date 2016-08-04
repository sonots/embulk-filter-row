%%

%byaccj

%{
  StringBuffer string = new StringBuffer();

  private Parser yyparser;

  public Yylex(String str, Parser yyparser) {
    this(new java.io.StringReader(str));
    this.yyparser = yyparser;
  }
%}

%state STRING
%state IDENTIFIER

NumberLiteral = [0-9]+(\.[0-9]+)?
QuotedIdentifierCharacter = [^\r\n\"\\]
UnquotedIdentifierCharacter = [^ \t\'\r\n\"\\]
StringCharacter = [^\r\n\'\\]

%%

<YYINITIAL> {
  /* operators */
  "("               { return (int) yycharat(0); }
  ")"               { return (int) yycharat(0); }
  "AND"             { return Parser.AND; }
  "OR"              { return Parser.OR; }
  "="               { return Parser.EQ; }
  "<>" | "!="       { return Parser.NEQ; }
  "!="              { return Parser.NEQ; }
  ">"               { return Parser.GT; }
  ">="              { return Parser.GE; }
  "<"               { return Parser.LT; }
  "<="              { return Parser.LE; }
  "START_WITH"      { return Parser.START_WITH; }
  "END_WITH"        { return Parser.END_WITH; }
  "INCLUDE"         { return Parser.INCLUDE; }
  "IS"              { return Parser.IS; }
  "NOT"             { return Parser.NOT; }
  "NULL"            { return Parser.NULL; }

  /* boolean literal */
  "true" | "TRUE"   { yyparser.yylval = new ParserVal(new BooleanLiteral(true)); return Parser.BOOLEAN; }
  "false" | "FALSE" { yyparser.yylval = new ParserVal(new BooleanLiteral(false)); return Parser.BOOLEAN; }

  /* number literal */
  {NumberLiteral}   { yyparser.yylval = new ParserVal(new NumberLiteral(Double.parseDouble(yytext()))); return Parser.NUMBER; }
  
  /* identifier literal */
  {UnquotedIdentifierCharacter}+ { yyparser.yylval = new ParserVal(new IdentifierLiteral(yytext())); return Parser.IDENTIFIER; }
  \"                { yybegin(IDENTIFIER); string.setLength(0); }

  /* string literal */
  \'                { yybegin(STRING); string.setLength(0); }

  /* whitespace */
  [ \t]+ { }
}

<IDENTIFIER> {
  \"                             { yybegin(YYINITIAL); yyparser.yylval = new ParserVal(new IdentifierLiteral(string.toString())); return Parser.IDENTIFIER; }
  
  {QuotedIdentifierCharacter}+   { string.append( yytext() ); }
  
  /* escape sequences */
  "\\\""                         { string.append( '\"' ); }
  "\\'"                          { string.append( '\'' ); }
  "\\\\"                         { string.append( '\\' ); }
  
  /* error cases */
  \\.                            { throw new RuntimeException("Illegal escape sequence \""+yytext()+"\""); }
}

<STRING> {
  \'                             { yybegin(YYINITIAL); yyparser.yylval = new ParserVal(new StringLiteral(string.toString())); return Parser.STRING; }
  
  {StringCharacter}+             { string.append( yytext() ); }
  
  /* escape sequences */
  "\\b"                          { string.append( '\b' ); }
  "\\t"                          { string.append( '\t' ); }
  "\\n"                          { string.append( '\n' ); }
  "\\f"                          { string.append( '\f' ); }
  "\\r"                          { string.append( '\r' ); }
  "\\\""                         { string.append( '\"' ); }
  "\\'"                          { string.append( '\'' ); }
  "\\\\"                         { string.append( '\\' ); }
  
  /* error cases */
  \\.                            { throw new RuntimeException("Illegal escape sequence \""+yytext()+"\""); }
}

/* error fallback */
[^]    { System.err.println("Error: unexpected character '"+yytext()+"'"); return -1; }
