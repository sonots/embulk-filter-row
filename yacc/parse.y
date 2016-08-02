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
 | exp '\n' { System.out.println(" " + $1.bval + " "); }
 ;

term: IDENTIFIER EQ BOOLEAN     { $$.bval = ((Boolean)(values.get($1.sval))).booleanValue() == $3.bval; }
 | IDENTIFIER NEQ BOOLEAN       { $$.bval = ((Boolean)(values.get($1.sval))).booleanValue() != $3.bval; }
 | IDENTIFIER EQ NUMBER         { $$.bval = ((Double)(values.get($1.sval))).doubleValue() == $3.dval; }
 | IDENTIFIER NEQ NUMBER        { $$.bval = ((Double)(values.get($1.sval))).doubleValue() != $3.dval; }
 | IDENTIFIER GT NUMBER         { $$.bval = ((Double)(values.get($1.sval))).doubleValue() > $3.dval; }
 | IDENTIFIER GE NUMBER         { $$.bval = ((Double)(values.get($1.sval))).doubleValue() >= $3.dval; }
 | IDENTIFIER LT NUMBER         { $$.bval = ((Double)(values.get($1.sval))).doubleValue() < $3.dval; }
 | IDENTIFIER LE NUMBER         { $$.bval = ((Double)(values.get($1.sval))).doubleValue() <= $3.dval; }
 | IDENTIFIER EQ STRING         { $$.bval = ((String)(values.get($1.sval))).equals($3.sval); }
 | IDENTIFIER NEQ STRING        { $$.bval = ! (((String)(values.get($1.sval))).equals($3.sval)); }
 | IDENTIFIER START_WITH STRING { $$.bval = ((String)(values.get($1.sval))).startsWith($3.sval); }
 | IDENTIFIER END_WITH STRING   { $$.bval = ((String)(values.get($1.sval))).endsWith($3.sval); }
 | IDENTIFIER INCLUDE STRING    { $$.bval = ((String)(values.get($1.sval))).contains($3.sval); }
 | IDENTIFIER IS NULL      { $$.bval = values.get($1.sval) == null; }
 | IDENTIFIER IS NOT NULL  { $$.bval = values.get($1.sval) != null; }

exp: term { $$ = $1; }
 | exp OR exp { $$ = new ParserVal($1.bval || $3.bval); }
 | exp AND exp { $$ = new ParserVal($1.bval && $3.bval); }
 | '(' exp ')' { $$ = $2; }
 ;
%%

String ins;
StringTokenizer st;
static HashMap<String, Object> values;

void yyerror(String s)
{
    System.out.println("par:"+s);
}

boolean newline;
int yylex()
{
    String s;
    int token;
    Double d;
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
    if (s.equals("=")) {
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
    //else if (s.equals("REGEXP")) {
    //    token = REGEXP;
    //}
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
        yylval = new ParserVal(true);
    }
    else if (s.equals("false") || s.equals("FALSE")) {
        token = BOOLEAN;
        yylval = new ParserVal(false);
    }
    else if (s.startsWith("'") && s.endsWith("'")) { /* ' for quoting string values */
        token = STRING;
        yylval = new ParserVal(s.substring(1, s.length() - 2));
    }
    else {
        try {
            token = NUMBER;
            yylval = new ParserVal(Double.valueOf(s).doubleValue());
        } catch (Exception e) {
            token = IDENTIFIER;
            yylval = new ParserVal(s);
        }
    }
    return token;
}

void dotest()
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
    }
}

public static void main(String args[])
{
    Parser par = new Parser(false);
    values = new HashMap<String, Object>();
    values.put("boolean", Boolean.TRUE);
    values.put("integer", new Long(1));
    values.put("float", new Double(1.5));
    values.put("string", "string");
    par.dotest();
}
