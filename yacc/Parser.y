%{
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;
import java.util.HashMap;
%}

/* YACC Declarations */
%token EQ  /* = */
%token NEQ /* <> */
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

/* Grammar follows */
%%
input: /* empty string */
 | input line
 ;

line: '\n'
 | exp '\n' { root = $1; }
 ;

exp: IDENTIFIER EQ BOOLEAN      { $$.obj = new BooleanOpExp($1, $3, EQ); }
 | IDENTIFIER NEQ BOOLEAN       { $$.obj = new BooleanOpExp($1, $3, NEQ); }
 | BOOLEAN EQ  IDENTIFIER       { $$.obj = new BooleanOpExp($1, $3, EQ); }
 | BOOLEAN NEQ IDENTIFIER       { $$.obj = new BooleanOpExp($1, $3, NEQ); }
 | IDENTIFIER EQ  NUMBER        { $$.obj = new NumberOpExp($1, $3, EQ); }
 | IDENTIFIER NEQ NUMBER        { $$.obj = new NumberOpExp($1, $3, NEQ); }
 | IDENTIFIER GT  NUMBER        { $$.obj = new NumberOpExp($1, $3, GT); }
 | IDENTIFIER GE  NUMBER        { $$.obj = new NumberOpExp($1, $3, GE); }
 | IDENTIFIER LT  NUMBER        { $$.obj = new NumberOpExp($1, $3, LT); }
 | IDENTIFIER LE  NUMBER        { $$.obj = new NumberOpExp($1, $3, LE); }
 | NUMBER EQ  IDENTIFIER        { $$.obj = new NumberOpExp($1, $3, EQ); }
 | NUMBER NEQ IDENTIFIER        { $$.obj = new NumberOpExp($1, $3, NEQ); }
 | NUMBER GT  IDENTIFIER        { $$.obj = new NumberOpExp($1, $3, GT); }
 | NUMBER GE  IDENTIFIER        { $$.obj = new NumberOpExp($1, $3, GE); }
 | NUMBER LT  IDENTIFIER        { $$.obj = new NumberOpExp($1, $3, LT); }
 | NUMBER LE  IDENTIFIER        { $$.obj = new NumberOpExp($1, $3, LE); }
 | IDENTIFIER EQ         STRING { $$.obj = new StringOpExp($1, $3, EQ); }
 | IDENTIFIER NEQ        STRING { $$.obj = new StringOpExp($1, $3, NEQ); }
 | IDENTIFIER START_WITH STRING { $$.obj = new StringOpExp($1, $3, START_WITH); }
 | IDENTIFIER END_WITH   STRING { $$.obj = new StringOpExp($1, $3, END_WITH); }
 | IDENTIFIER INCLUDE    STRING { $$.obj = new StringOpExp($1, $3, INCLUDE); }
 | STRING EQ         IDENTIFIER { $$.obj = new StringOpExp($1, $3, EQ); }
 | STRING NEQ        IDENTIFIER { $$.obj = new StringOpExp($1, $3, NEQ); }
 | STRING START_WITH IDENTIFIER { $$.obj = new StringOpExp($1, $3, START_WITH); }
 | STRING END_WITH   IDENTIFIER { $$.obj = new StringOpExp($1, $3, END_WITH); }
 | STRING INCLUDE    IDENTIFIER { $$.obj = new StringOpExp($1, $3, INCLUDE); }
 | IDENTIFIER IS NULL           { $$.obj = new NullOpExp($1, EQ); }
 | IDENTIFIER IS NOT NULL       { $$.obj = new NullOpExp($1, NEQ); }
 | exp OR exp                   { $$.obj = new LogicalOpExp($1, $3, OR); }
 | exp AND exp                  { $$.obj = new LogicalOpExp($1, $3, AND); }
 | '(' exp ')'                  { $$ = $2; }
 ;
%%

String ins;
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
    System.out.println("token:"+s);
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
    else if (s.equals("<>")) {
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
        yylval.obj = new BooleanLiteral(true);
    }
    else if (s.equals("false") || s.equals("FALSE")) {
        token = BOOLEAN;
        yylval.obj = new BooleanLiteral(false);
    }
    else if (s.startsWith("'") && s.endsWith("'")) { // ' for quoting string values
        token = STRING;
        yylval.obj = new StringLiteral(s.substring(1, s.length() - 2));
    }
    else {
        try {
            Double d = Double.valueOf(s); // this may fail
            token = NUMBER;
            yylval.obj = new NumberLiteral(d.doubleValue());
        } catch (Exception e) {
            token = IDENTIFIER;
            yylval.obj = new IdentifierLiteral(s);
        }
    }
    return token;
}

void dotest(HashMap<String, Object> variables)
{
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("BYACC/J Calculator Demo");
    while (true) {
        System.out.print("expression:");
        try {
            ins = in.readLine();
        }
        catch (Exception e) {
        }
        st = new StringTokenizer(ins);
        newline = false;
        yyparse();
        ParserExp exp = ((ParserExp)root.obj);
        System.out.println("result: " + exp.eval(variables));
    }
}

public static void main(String args[])
{
    Parser par = new Parser(false);
    HashMap<String, Object> variables = new HashMap<String, Object>();
    variables.put("boolean", Boolean.TRUE);
    variables.put("integer", new Long(1));
    variables.put("float", new Double(1.5));
    variables.put("string", "string");
    par.dotest(variables);
}
