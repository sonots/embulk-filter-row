//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";



package org.embulk.filter.row.parser;



//#line 2 "src/main/java/org/embulk/filter/row/parser/Parser.y"
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;
import java.util.HashMap;
//#line 22 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short EQ=257;
public final static short NEQ=258;
public final static short GT=259;
public final static short GE=260;
public final static short LT=261;
public final static short LE=262;
public final static short START_WITH=263;
public final static short END_WITH=264;
public final static short INCLUDE=265;
public final static short IS=266;
public final static short NOT=267;
public final static short AND=268;
public final static short OR=269;
public final static short NULL=270;
public final static short BOOLEAN=271;
public final static short STRING=272;
public final static short NUMBER=273;
public final static short IDENTIFIER=274;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    1,    1,    2,    2,    2,    2,    2,    2,
    2,    2,    2,    2,    2,    2,    2,    2,    2,    2,
    2,    2,    2,    2,    2,    2,    2,    2,    2,    2,
    2,    2,    2,    2,    2,
};
final static short yylen[] = {                            2,
    0,    2,    1,    2,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    4,    3,    3,    3,
};
final static short yydefred[] = {                         1,
    0,    0,    0,    0,    0,    3,    0,    2,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    4,    7,    8,   26,   27,
   28,   29,   30,   15,   16,   17,   18,   19,   20,    5,
   21,    9,    6,   22,   10,   11,   12,   13,   14,   23,
   24,   25,    0,   31,   35,   34,    0,   32,
};
final static short yydgoto[] = {                          1,
    8,    9,
};
final static short yysindex[] = {                         0,
  -10, -213, -240, -219, -250,    0,  -38,    0,   -9, -271,
 -254, -253, -243, -228, -227, -226, -225, -224, -223, -222,
 -221, -220, -245, -237, -244, -218, -217, -216, -214, -212,
 -211, -248,  -37,  -38,  -38,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -208,    0,    0,    0, -209,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   -4,    0,
};
final static short yygindex[] = {                         0,
    0,   -2,
};
final static int YYTABLESIZE=265;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                          6,
   36,    7,   37,   65,   33,   33,   23,   24,   25,   26,
   27,   28,   29,   30,   31,   32,   12,   13,   63,   38,
   39,   64,   14,   15,   16,   50,   51,   52,   56,    7,
   40,   66,   67,   53,   54,   55,   33,   17,   18,   19,
   20,   21,   22,   10,   11,   41,   42,   43,   44,   45,
   46,   47,   48,   49,   57,   58,   59,   60,   34,   61,
   62,   68,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   34,   35,    2,    3,    4,    5,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   34,   35,
    2,    3,    4,    5,   33,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         10,
   10,   40,  274,   41,    7,   10,  257,  258,  259,  260,
  261,  262,  263,  264,  265,  266,  257,  258,  267,  274,
  274,  270,  263,  264,  265,  271,  272,  273,  273,   40,
  274,   34,   35,  271,  272,  273,   41,  257,  258,  259,
  260,  261,  262,  257,  258,  274,  274,  274,  274,  274,
  274,  274,  274,  274,  273,  273,  273,  272,  268,  272,
  272,  270,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  268,  269,  271,  272,  273,  274,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  268,  269,
  271,  272,  273,  274,  269,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=274;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,"'\\n'",null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,"'('","')'",null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,"EQ","NEQ","GT","GE","LT","LE","START_WITH","END_WITH",
"INCLUDE","IS","NOT","AND","OR","NULL","BOOLEAN","STRING","NUMBER","IDENTIFIER",
};
final static String yyrule[] = {
"$accept : input",
"input :",
"input : input line",
"line : '\\n'",
"line : exp '\\n'",
"exp : IDENTIFIER EQ BOOLEAN",
"exp : IDENTIFIER NEQ BOOLEAN",
"exp : BOOLEAN EQ IDENTIFIER",
"exp : BOOLEAN NEQ IDENTIFIER",
"exp : IDENTIFIER EQ NUMBER",
"exp : IDENTIFIER NEQ NUMBER",
"exp : IDENTIFIER GT NUMBER",
"exp : IDENTIFIER GE NUMBER",
"exp : IDENTIFIER LT NUMBER",
"exp : IDENTIFIER LE NUMBER",
"exp : NUMBER EQ IDENTIFIER",
"exp : NUMBER NEQ IDENTIFIER",
"exp : NUMBER GT IDENTIFIER",
"exp : NUMBER GE IDENTIFIER",
"exp : NUMBER LT IDENTIFIER",
"exp : NUMBER LE IDENTIFIER",
"exp : IDENTIFIER EQ STRING",
"exp : IDENTIFIER NEQ STRING",
"exp : IDENTIFIER START_WITH STRING",
"exp : IDENTIFIER END_WITH STRING",
"exp : IDENTIFIER INCLUDE STRING",
"exp : STRING EQ IDENTIFIER",
"exp : STRING NEQ IDENTIFIER",
"exp : STRING START_WITH IDENTIFIER",
"exp : STRING END_WITH IDENTIFIER",
"exp : STRING INCLUDE IDENTIFIER",
"exp : IDENTIFIER IS NULL",
"exp : IDENTIFIER IS NOT NULL",
"exp : exp OR exp",
"exp : exp AND exp",
"exp : '(' exp ')'",
};

//#line 77 "src/main/java/org/embulk/filter/row/parser/Parser.y"

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
//#line 412 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 4:
//#line 41 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ root = val_peek(1); }
break;
case 5:
//#line 44 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new BooleanOpExp(val_peek(2), val_peek(0), EQ); }
break;
case 6:
//#line 45 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new BooleanOpExp(val_peek(2), val_peek(0), NEQ); }
break;
case 7:
//#line 46 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new BooleanOpExp(val_peek(2), val_peek(0), EQ); }
break;
case 8:
//#line 47 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new BooleanOpExp(val_peek(2), val_peek(0), NEQ); }
break;
case 9:
//#line 48 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new NumberOpExp(val_peek(2), val_peek(0), EQ); }
break;
case 10:
//#line 49 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new NumberOpExp(val_peek(2), val_peek(0), NEQ); }
break;
case 11:
//#line 50 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new NumberOpExp(val_peek(2), val_peek(0), GT); }
break;
case 12:
//#line 51 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new NumberOpExp(val_peek(2), val_peek(0), GE); }
break;
case 13:
//#line 52 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new NumberOpExp(val_peek(2), val_peek(0), LT); }
break;
case 14:
//#line 53 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new NumberOpExp(val_peek(2), val_peek(0), LE); }
break;
case 15:
//#line 54 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new NumberOpExp(val_peek(2), val_peek(0), EQ); }
break;
case 16:
//#line 55 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new NumberOpExp(val_peek(2), val_peek(0), NEQ); }
break;
case 17:
//#line 56 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new NumberOpExp(val_peek(2), val_peek(0), GT); }
break;
case 18:
//#line 57 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new NumberOpExp(val_peek(2), val_peek(0), GE); }
break;
case 19:
//#line 58 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new NumberOpExp(val_peek(2), val_peek(0), LT); }
break;
case 20:
//#line 59 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new NumberOpExp(val_peek(2), val_peek(0), LE); }
break;
case 21:
//#line 60 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new StringOpExp(val_peek(2), val_peek(0), EQ); }
break;
case 22:
//#line 61 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new StringOpExp(val_peek(2), val_peek(0), NEQ); }
break;
case 23:
//#line 62 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new StringOpExp(val_peek(2), val_peek(0), START_WITH); }
break;
case 24:
//#line 63 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new StringOpExp(val_peek(2), val_peek(0), END_WITH); }
break;
case 25:
//#line 64 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new StringOpExp(val_peek(2), val_peek(0), INCLUDE); }
break;
case 26:
//#line 65 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new StringOpExp(val_peek(2), val_peek(0), EQ); }
break;
case 27:
//#line 66 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new StringOpExp(val_peek(2), val_peek(0), NEQ); }
break;
case 28:
//#line 67 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new StringOpExp(val_peek(2), val_peek(0), START_WITH); }
break;
case 29:
//#line 68 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new StringOpExp(val_peek(2), val_peek(0), END_WITH); }
break;
case 30:
//#line 69 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new StringOpExp(val_peek(2), val_peek(0), INCLUDE); }
break;
case 31:
//#line 70 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new NullOpExp(val_peek(2), EQ); }
break;
case 32:
//#line 71 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new NullOpExp(val_peek(3), NEQ); }
break;
case 33:
//#line 72 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new LogicalOpExp(val_peek(2), val_peek(0), OR); }
break;
case 34:
//#line 73 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval.obj = new LogicalOpExp(val_peek(2), val_peek(0), AND); }
break;
case 35:
//#line 74 "src/main/java/org/embulk/filter/row/parser/Parser.y"
{ yyval = val_peek(1); }
break;
//#line 689 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
