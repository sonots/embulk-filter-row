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



package org.embulk.filter.row.where;



//#line 2 "src/main/java/org/embulk/filter/row/where/_parser.y"
import org.embulk.spi.Column;
import org.embulk.spi.Schema;
//#line 20 "Parser.java"




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
public final static short TIMESTAMP=275;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    2,    2,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,
};
final static short yylen[] = {                            2,
    0,    1,    2,    2,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    4,    3,    3,    2,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   47,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    3,    4,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    7,    8,   26,   27,   28,
   29,   30,   15,   16,   17,   18,   19,   20,    5,   21,
    9,   31,    6,   22,   10,   32,   11,   33,   12,   34,
   13,   35,   14,   36,   23,   24,   25,    0,   43,   48,
   46,    0,   37,   38,   39,   40,   41,   42,   44,
};
final static short yydgoto[] = {                          8,
    9,   10,
};
final static short yysindex[] = {                       -40,
  -40, -234, -237, -228, -247, -208,  -40,    0, -202, -214,
    0, -215, -206, -205, -204, -203, -201, -200, -199, -198,
 -197, -196, -195, -194, -267, -236, -218, -217, -213, -212,
 -265, -191, -190, -245,    0,    0,  -39,  -40,  -40, -189,
 -188, -187, -186, -185, -184,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -232,    0,    0,
    0, -177,    0,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                        72,
    0,    0,    0,    0,    0,    0,    0,    0,   83,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    1,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    2,   24,
};
final static int YYTABLESIZE=270;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                          7,
   45,   80,   11,   59,   60,   61,   75,    6,   37,   25,
   26,   27,   28,   29,   30,   31,   32,   33,   34,   14,
   15,   78,   12,   13,   79,   16,   17,   18,   19,   20,
   21,   22,   23,   24,   63,   64,   65,   89,    6,   81,
   82,   45,   40,   41,   42,   43,   44,   45,   62,   66,
   68,   70,   72,   74,   67,   69,    6,    6,   46,   71,
   73,    6,    6,   35,   36,   38,   39,   47,   48,   49,
   50,    1,   51,   52,   53,   54,   55,   56,   57,   58,
   76,   77,    2,    0,   83,   84,   85,   86,   87,   88,
   38,    0,    0,    0,    0,    0,    0,    0,    0,    0,
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
    0,    0,    0,    0,    0,    0,    1,    0,   38,   39,
    2,    3,    4,    5,    6,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   45,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   41,    1,  271,  272,  273,  272,  275,    7,  257,
  258,  259,  260,  261,  262,  263,  264,  265,  266,  257,
  258,  267,  257,  258,  270,  263,  264,  265,  257,  258,
  259,  260,  261,  262,  271,  272,  273,  270,  275,   38,
   39,   41,  257,  258,  259,  260,  261,  262,   25,   26,
   27,   28,   29,   30,  273,  273,  275,  275,  274,  273,
  273,  275,  275,  272,  273,  268,  269,  274,  274,  274,
  274,    0,  274,  274,  274,  274,  274,  274,  274,  274,
  272,  272,    0,   -1,  274,  274,  274,  274,  274,  274,
  268,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
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
   -1,   -1,   -1,   -1,   -1,   -1,  267,   -1,  268,  269,
  271,  272,  273,  274,  275,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  269,
};
}
final static short YYFINAL=8;
final static short YYMAXTOKEN=275;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'",null,null,null,
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
null,null,null,null,"EQ","NEQ","GT","GE","LT","LE","START_WITH","END_WITH",
"INCLUDE","IS","NOT","AND","OR","NULL","BOOLEAN","STRING","NUMBER","IDENTIFIER",
"TIMESTAMP",
};
final static String yyrule[] = {
"$accept : input",
"input :",
"input : exp",
"timestamp : TIMESTAMP STRING",
"timestamp : TIMESTAMP NUMBER",
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
"exp : IDENTIFIER EQ timestamp",
"exp : IDENTIFIER NEQ timestamp",
"exp : IDENTIFIER GT timestamp",
"exp : IDENTIFIER GE timestamp",
"exp : IDENTIFIER LT timestamp",
"exp : IDENTIFIER LE timestamp",
"exp : timestamp EQ IDENTIFIER",
"exp : timestamp NEQ IDENTIFIER",
"exp : timestamp GT IDENTIFIER",
"exp : timestamp GE IDENTIFIER",
"exp : timestamp LT IDENTIFIER",
"exp : timestamp LE IDENTIFIER",
"exp : IDENTIFIER IS NULL",
"exp : IDENTIFIER IS NOT NULL",
"exp : exp OR exp",
"exp : exp AND exp",
"exp : NOT exp",
"exp : '(' exp ')'",
};

//#line 91 "src/main/java/org/embulk/filter/row/where/_parser.y"

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
//#line 343 "Parser.java"
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
case 2:
//#line 39 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ root = val_peek(0); }
break;
case 3:
//#line 42 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new TimestampLiteral(val_peek(0))); }
break;
case 4:
//#line 43 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new TimestampLiteral(val_peek(0))); }
break;
case 5:
//#line 45 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new BooleanOpExp(val_peek(2), val_peek(0), EQ)); }
break;
case 6:
//#line 46 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new BooleanOpExp(val_peek(2), val_peek(0), NEQ)); }
break;
case 7:
//#line 47 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new BooleanOpExp(val_peek(2), val_peek(0), EQ)); }
break;
case 8:
//#line 48 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new BooleanOpExp(val_peek(2), val_peek(0), NEQ)); }
break;
case 9:
//#line 49 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new NumberOpExp(val_peek(2), val_peek(0), EQ)); }
break;
case 10:
//#line 50 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new NumberOpExp(val_peek(2), val_peek(0), NEQ)); }
break;
case 11:
//#line 51 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new NumberOpExp(val_peek(2), val_peek(0), GT)); }
break;
case 12:
//#line 52 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new NumberOpExp(val_peek(2), val_peek(0), GE)); }
break;
case 13:
//#line 53 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new NumberOpExp(val_peek(2), val_peek(0), LT)); }
break;
case 14:
//#line 54 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new NumberOpExp(val_peek(2), val_peek(0), LE)); }
break;
case 15:
//#line 55 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new NumberOpExp(val_peek(2), val_peek(0), EQ)); }
break;
case 16:
//#line 56 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new NumberOpExp(val_peek(2), val_peek(0), NEQ)); }
break;
case 17:
//#line 57 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new NumberOpExp(val_peek(2), val_peek(0), GT)); }
break;
case 18:
//#line 58 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new NumberOpExp(val_peek(2), val_peek(0), GE)); }
break;
case 19:
//#line 59 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new NumberOpExp(val_peek(2), val_peek(0), LT)); }
break;
case 20:
//#line 60 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new NumberOpExp(val_peek(2), val_peek(0), LE)); }
break;
case 21:
//#line 61 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new StringOpExp(val_peek(2), val_peek(0), EQ)); }
break;
case 22:
//#line 62 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new StringOpExp(val_peek(2), val_peek(0), NEQ)); }
break;
case 23:
//#line 63 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new StringOpExp(val_peek(2), val_peek(0), START_WITH)); }
break;
case 24:
//#line 64 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new StringOpExp(val_peek(2), val_peek(0), END_WITH)); }
break;
case 25:
//#line 65 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new StringOpExp(val_peek(2), val_peek(0), INCLUDE)); }
break;
case 26:
//#line 66 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new StringOpExp(val_peek(2), val_peek(0), EQ)); }
break;
case 27:
//#line 67 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new StringOpExp(val_peek(2), val_peek(0), NEQ)); }
break;
case 28:
//#line 68 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new StringOpExp(val_peek(2), val_peek(0), START_WITH)); }
break;
case 29:
//#line 69 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new StringOpExp(val_peek(2), val_peek(0), END_WITH)); }
break;
case 30:
//#line 70 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new StringOpExp(val_peek(2), val_peek(0), INCLUDE)); }
break;
case 31:
//#line 71 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new TimestampOpExp(val_peek(2), val_peek(0), EQ)); }
break;
case 32:
//#line 72 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new TimestampOpExp(val_peek(2), val_peek(0), NEQ)); }
break;
case 33:
//#line 73 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new TimestampOpExp(val_peek(2), val_peek(0), GT)); }
break;
case 34:
//#line 74 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new TimestampOpExp(val_peek(2), val_peek(0), GE)); }
break;
case 35:
//#line 75 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new TimestampOpExp(val_peek(2), val_peek(0), LT)); }
break;
case 36:
//#line 76 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new TimestampOpExp(val_peek(2), val_peek(0), LE)); }
break;
case 37:
//#line 77 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new TimestampOpExp(val_peek(2), val_peek(0), EQ)); }
break;
case 38:
//#line 78 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new TimestampOpExp(val_peek(2), val_peek(0), NEQ)); }
break;
case 39:
//#line 79 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new TimestampOpExp(val_peek(2), val_peek(0), GT)); }
break;
case 40:
//#line 80 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new TimestampOpExp(val_peek(2), val_peek(0), GE)); }
break;
case 41:
//#line 81 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new TimestampOpExp(val_peek(2), val_peek(0), LT)); }
break;
case 42:
//#line 82 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new TimestampOpExp(val_peek(2), val_peek(0), LE)); }
break;
case 43:
//#line 83 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new NullOpExp(val_peek(2), EQ)); }
break;
case 44:
//#line 84 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new NullOpExp(val_peek(3), NEQ)); }
break;
case 45:
//#line 85 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new LogicalOpExp(val_peek(2), val_peek(0), OR)); }
break;
case 46:
//#line 86 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new LogicalOpExp(val_peek(2), val_peek(0), AND)); }
break;
case 47:
//#line 87 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = new ParserVal(new NegateOpExp(val_peek(0))); }
break;
case 48:
//#line 88 "src/main/java/org/embulk/filter/row/where/_parser.y"
{ yyval = val_peek(1); }
break;
//#line 680 "Parser.java"
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
//## The -Jnoconstruct option was used ##
//###############################################################



}
//################### END OF CLASS ##############################
