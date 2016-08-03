%{
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;
import java.util.HashMap;
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

%left OR
%left AND
%right NOT

/* Grammar follows */
%%
input: /* empty string */
 | input line
 ;

line: '\n'
 | exp '\n' { root = $1; }
 ;

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
 | IDENTIFIER IS NULL           { $$ = new ParserVal(new NullOpExp($1, EQ)); }
 | IDENTIFIER IS NOT NULL       { $$ = new ParserVal(new NullOpExp($1, NEQ)); }
 | exp OR exp                   { $$ = new ParserVal(new LogicalOpExp($1, $3, OR)); }
 | exp AND exp                  { $$ = new ParserVal(new LogicalOpExp($1, $3, AND)); }
 | NOT exp                      { $$ = new ParserVal(new NegateOpExp($2)); }
 | '(' exp ')'                  { $$ = $2; }
 ;
%%

StringTokenizer st;
ParserVal root;

void yyerror(String s)
{
    System.out.println("par:"+s);
}

boolean newline;
int yylex()
{
    String s;
    int token;
    if (!st.hasMoreTokens()) {
        if (!newline) {
            newline=true;
            return '\n'; //So we look like classic YACC example
        }
        else {
            return 0;
        }
    }
    s = st.nextToken();
    if (s.equals("(")) {
        token = '(';
    }
    else if (s.equals(")")) {
        token = ')';
    }
    else if (s.equals("AND")) {
        token = AND;
    }
    else if (s.equals("OR")) {
        token = OR;
    }
    else if (s.equals("=")) {
        token = EQ;
    }
    else if (s.equals("<>") || s.equals("!=")) {
        token = NEQ;
    }
    else if (s.equals(">")) {
        token = GT;
    }
    else if (s.equals(">=")) {
        token = GE;
    }
    else if (s.equals("<")) {
        token = LT;
    }
    else if (s.equals("<=")) {
        token = LE;
    }
    else if (s.equals("START_WITH")) {
        token = START_WITH;
    }
    else if (s.equals("END_WITH")) {
        token = END_WITH;
    }
    else if (s.equals("INCLUDE")) {
        token = INCLUDE;
    }
    else if (s.equals("IS")) {
        token = IS;
    }
    else if (s.equals("NOT")) {
        token = NOT;
    }
    else if (s.equals("NULL")) {
        token = NULL;
    }
    else if (s.equals("true") || s.equals("TRUE")) {
        token = BOOLEAN;
        yylval = new ParserVal(new BooleanLiteral(true));
    }
    else if (s.equals("false") || s.equals("FALSE")) {
        token = BOOLEAN;
        yylval = new ParserVal(new BooleanLiteral(false));
    }
    else if (s.startsWith("'") && s.endsWith("'")) { // ' for quoting string values
        token = STRING;
        yylval = new ParserVal(new StringLiteral(s.substring(1, s.length() - 1)));
    }
    else {
        try {
            Double d = Double.valueOf(s); // this may fail
            token = NUMBER;
            yylval = new ParserVal(new NumberLiteral(d.doubleValue()));
        } catch (Exception e) {
            token = IDENTIFIER;
            if (s.startsWith("\"") && s.endsWith("\"")) {
                yylval = new ParserVal(new IdentifierLiteral(s.substring(1, s.length() - 1)));
            }
            else {
                yylval = new ParserVal(new IdentifierLiteral(s));
            }
        }
    }
    //System.out.println(String.format("token: %s %d", s, token));
    //if (yylval != null) {
    //    System.out.println(String.format("yylval: %s", yylval.obj.toString()));
    //}
    return token;
}

public ParserExp parse(String ins)
{
    st = new StringTokenizer(ins);
    newline = false;
    yyparse();
    return ((ParserExp)(root.obj));
}
