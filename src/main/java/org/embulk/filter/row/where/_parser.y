%{
import org.embulk.spi.Column;
import org.embulk.spi.Schema;
%}

/* YACC Declarations */
%token EQ  /* = */
%token NEQ /* <> != */
%token GT  /* >  */ 
%token GE  /* >= */
%token LT  /* <  */
%token LE  /* <= */

%token START_WITH
%token END_WITH
%token INCLUDE
%token IS
%token NOT

%token AND
%token OR

%token NULL
%token BOOLEAN
%token STRING
%token NUMBER
%token IDENTIFIER

%token TIMESTAMP

%left OR
%left AND
%right TIMESTAMP
%right NOT

/* Grammar follows */
%%
input: /* empty string */
 | exp { root = $1; }
 ;

timestamp: TIMESTAMP STRING     { $$ = new ParserVal(new TimestampLiteral($2)); }
 | TIMESTAMP NUMBER             { $$ = new ParserVal(new TimestampLiteral($2)); }

exp: IDENTIFIER EQ BOOLEAN      { $$ = new ParserVal(new BooleanOpExp($1, $3, EQ)); }
 | IDENTIFIER NEQ BOOLEAN       { $$ = new ParserVal(new BooleanOpExp($1, $3, NEQ)); }
 | BOOLEAN EQ  IDENTIFIER       { $$ = new ParserVal(new BooleanOpExp($1, $3, EQ)); }
 | BOOLEAN NEQ IDENTIFIER       { $$ = new ParserVal(new BooleanOpExp($1, $3, NEQ)); }
 | IDENTIFIER EQ  NUMBER        { $$ = new ParserVal(new NumberOpExp($1, $3, EQ)); }
 | IDENTIFIER NEQ NUMBER        { $$ = new ParserVal(new NumberOpExp($1, $3, NEQ)); }
 | IDENTIFIER GT  NUMBER        { $$ = new ParserVal(new NumberOpExp($1, $3, GT)); }
 | IDENTIFIER GE  NUMBER        { $$ = new ParserVal(new NumberOpExp($1, $3, GE)); }
 | IDENTIFIER LT  NUMBER        { $$ = new ParserVal(new NumberOpExp($1, $3, LT)); }
 | IDENTIFIER LE  NUMBER        { $$ = new ParserVal(new NumberOpExp($1, $3, LE)); }
 | NUMBER EQ  IDENTIFIER        { $$ = new ParserVal(new NumberOpExp($1, $3, EQ)); }
 | NUMBER NEQ IDENTIFIER        { $$ = new ParserVal(new NumberOpExp($1, $3, NEQ)); }
 | NUMBER GT  IDENTIFIER        { $$ = new ParserVal(new NumberOpExp($1, $3, GT)); }
 | NUMBER GE  IDENTIFIER        { $$ = new ParserVal(new NumberOpExp($1, $3, GE)); }
 | NUMBER LT  IDENTIFIER        { $$ = new ParserVal(new NumberOpExp($1, $3, LT)); }
 | NUMBER LE  IDENTIFIER        { $$ = new ParserVal(new NumberOpExp($1, $3, LE)); }
 | IDENTIFIER EQ         STRING { $$ = new ParserVal(new StringOpExp($1, $3, EQ)); }
 | IDENTIFIER NEQ        STRING { $$ = new ParserVal(new StringOpExp($1, $3, NEQ)); }
 | IDENTIFIER START_WITH STRING { $$ = new ParserVal(new StringOpExp($1, $3, START_WITH)); }
 | IDENTIFIER END_WITH   STRING { $$ = new ParserVal(new StringOpExp($1, $3, END_WITH)); }
 | IDENTIFIER INCLUDE    STRING { $$ = new ParserVal(new StringOpExp($1, $3, INCLUDE)); }
 | STRING EQ         IDENTIFIER { $$ = new ParserVal(new StringOpExp($1, $3, EQ)); }
 | STRING NEQ        IDENTIFIER { $$ = new ParserVal(new StringOpExp($1, $3, NEQ)); }
 | STRING START_WITH IDENTIFIER { $$ = new ParserVal(new StringOpExp($1, $3, START_WITH)); }
 | STRING END_WITH   IDENTIFIER { $$ = new ParserVal(new StringOpExp($1, $3, END_WITH)); }
 | STRING INCLUDE    IDENTIFIER { $$ = new ParserVal(new StringOpExp($1, $3, INCLUDE)); }
 | IDENTIFIER EQ  timestamp     { $$ = new ParserVal(new TimestampOpExp($1, $3, EQ)); }
 | IDENTIFIER NEQ timestamp     { $$ = new ParserVal(new TimestampOpExp($1, $3, NEQ)); }
 | IDENTIFIER GT  timestamp     { $$ = new ParserVal(new TimestampOpExp($1, $3, GT)); }
 | IDENTIFIER GE  timestamp     { $$ = new ParserVal(new TimestampOpExp($1, $3, GE)); }
 | IDENTIFIER LT  timestamp     { $$ = new ParserVal(new TimestampOpExp($1, $3, LT)); }
 | IDENTIFIER LE  timestamp     { $$ = new ParserVal(new TimestampOpExp($1, $3, LE)); }
 | timestamp EQ  IDENTIFIER     { $$ = new ParserVal(new TimestampOpExp($1, $3, EQ)); }
 | timestamp NEQ IDENTIFIER     { $$ = new ParserVal(new TimestampOpExp($1, $3, NEQ)); }
 | timestamp GT  IDENTIFIER     { $$ = new ParserVal(new TimestampOpExp($1, $3, GT)); }
 | timestamp GE  IDENTIFIER     { $$ = new ParserVal(new TimestampOpExp($1, $3, GE)); }
 | timestamp LT  IDENTIFIER     { $$ = new ParserVal(new TimestampOpExp($1, $3, LT)); }
 | timestamp LE  IDENTIFIER     { $$ = new ParserVal(new TimestampOpExp($1, $3, LE)); }
 | IDENTIFIER IS NULL           { $$ = new ParserVal(new NullOpExp($1, EQ)); }
 | IDENTIFIER IS NOT NULL       { $$ = new ParserVal(new NullOpExp($1, NEQ)); }
 | exp OR exp                   { $$ = new ParserVal(new LogicalOpExp($1, $3, OR)); }
 | exp AND exp                  { $$ = new ParserVal(new LogicalOpExp($1, $3, AND)); }
 | NOT exp                      { $$ = new ParserVal(new NegateOpExp($2)); }
 | '(' exp ')'                  { $$ = $2; }
 ;
%%

protected Schema schema;
protected Yylex lexer;
protected ParserVal root;

public Parser(final Schema schema)
{
    this.schema = schema;
}

public Parser(final Schema schema, boolean yydebug)
{
    this.schema = schema;
    this.yydebug = yydebug;
}

public ParserExp parse(String str)
{
    lexer = new Yylex(str, this);
    yyparse();
    return ((ParserExp)(root.obj));
}

private int yylex () {
    int token = -1;
    try {
        token = lexer.yylex(); // next token
    }
    catch (java.io.IOException e) {
        e.printStackTrace(); // should not happen
    }
    return token;
}

void yyerror(String s)
{
    throw new RuntimeException("yyerror: " + s);
}
