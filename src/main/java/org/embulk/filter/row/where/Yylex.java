/* The following code was generated by JFlex 1.6.1 */

package org.embulk.filter.row.where;

import org.embulk.config.ConfigException;
import org.embulk.spi.Schema;

/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.6.1
 * from the specification file <tt>src/main/java/org/embulk/filter/row/where/_lexer.l</tt>
 */
class Yylex {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int STRING = 2;
  public static final int IDENTIFIER = 4;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1,  1,  2, 2
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\54\1\12\1\55\1\55\1\11\22\0\1\54\1\24\1\4"+
    "\4\0\1\10\1\13\1\13\3\0\1\1\1\3\1\0\12\2\2\0"+
    "\1\22\1\21\1\23\2\0\1\14\1\5\1\34\1\16\1\33\1\53"+
    "\1\37\1\32\1\31\2\5\1\35\1\42\1\15\1\17\1\41\1\5"+
    "\1\20\1\25\1\26\1\36\1\5\1\30\1\40\2\5\1\6\1\7"+
    "\2\6\1\27\1\6\1\50\1\56\2\5\1\46\1\47\5\5\1\51"+
    "\1\5\1\57\3\5\1\44\1\52\1\43\1\45\5\5\12\0\1\55"+
    "\u1fa2\0\1\55\1\55\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\udfe6\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\3\0\2\1\1\2\1\3\1\4\1\5\2\6\1\7"+
    "\4\4\1\10\1\11\1\12\1\1\7\4\1\6\1\13"+
    "\1\1\1\14\1\13\1\15\1\1\1\0\3\4\1\16"+
    "\1\4\1\17\1\20\1\21\4\4\1\22\4\4\1\23"+
    "\1\24\1\25\1\26\1\27\1\30\1\31\1\32\1\33"+
    "\1\2\1\34\1\35\12\4\1\36\2\4\1\37\10\4"+
    "\1\40\6\4\1\41\3\4\1\42\1\4\1\43\1\44";

  private static int [] zzUnpackAction() {
    int [] result = new int[101];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\60\0\140\0\220\0\300\0\360\0\220\0\u0120"+
    "\0\220\0\u0150\0\220\0\220\0\u0180\0\u01b0\0\u01e0\0\u0210"+
    "\0\220\0\u0240\0\u0270\0\u02a0\0\u02d0\0\u0300\0\u0330\0\u0360"+
    "\0\u0390\0\u03c0\0\u03f0\0\u0420\0\u0450\0\u0480\0\220\0\u04b0"+
    "\0\220\0\u04e0\0\u0510\0\u0540\0\u0570\0\u05a0\0\u0120\0\u05d0"+
    "\0\220\0\220\0\220\0\u0600\0\u0630\0\u0660\0\u0690\0\u0120"+
    "\0\u06c0\0\u06f0\0\u0720\0\u0750\0\220\0\220\0\220\0\220"+
    "\0\220\0\220\0\220\0\220\0\220\0\u0510\0\u0120\0\u0120"+
    "\0\u0780\0\u07b0\0\u07e0\0\u0810\0\u0840\0\u0870\0\u08a0\0\u08d0"+
    "\0\u0900\0\u0930\0\u0120\0\u0960\0\u0990\0\u0120\0\u09c0\0\u09f0"+
    "\0\u0a20\0\u0a50\0\u0a80\0\u0ab0\0\u0ae0\0\u0b10\0\u0120\0\u0b40"+
    "\0\u0b70\0\u0ba0\0\u0bd0\0\u0c00\0\u0c30\0\u0120\0\u0c60\0\u0c90"+
    "\0\u0cc0\0\u0120\0\u0cf0\0\u0120\0\u0120";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[101];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\4\1\5\1\6\1\4\1\7\1\10\2\4\1\11"+
    "\1\12\1\13\1\14\1\15\1\16\1\10\1\17\1\20"+
    "\1\21\1\22\1\23\1\24\1\25\1\26\2\10\1\27"+
    "\1\10\1\30\7\10\1\31\3\10\1\32\3\10\1\33"+
    "\1\34\1\4\2\10\7\35\1\36\1\37\2\4\45\35"+
    "\4\40\1\41\2\40\1\42\1\40\2\4\45\40\62\0"+
    "\1\6\57\0\1\6\1\43\56\0\1\10\2\0\3\10"+
    "\4\0\5\10\4\0\27\10\2\0\2\10\12\0\1\13"+
    "\47\0\1\10\2\0\3\10\4\0\1\10\1\44\3\10"+
    "\4\0\27\10\2\0\2\10\2\0\1\10\2\0\3\10"+
    "\4\0\3\10\1\45\1\10\4\0\11\10\1\46\15\10"+
    "\2\0\2\10\2\0\1\10\2\0\3\10\4\0\4\10"+
    "\1\47\4\0\27\10\2\0\2\10\2\0\1\10\2\0"+
    "\3\10\4\0\5\10\4\0\6\10\1\50\20\10\2\0"+
    "\2\10\21\0\1\51\1\0\1\52\55\0\1\53\57\0"+
    "\1\52\40\0\1\10\2\0\3\10\4\0\5\10\4\0"+
    "\1\10\1\54\25\10\2\0\2\10\2\0\1\10\2\0"+
    "\3\10\4\0\4\10\1\55\4\0\4\10\1\56\22\10"+
    "\2\0\2\10\2\0\1\10\2\0\3\10\4\0\1\10"+
    "\1\57\3\10\4\0\1\60\26\10\2\0\2\10\2\0"+
    "\1\10\2\0\3\10\4\0\1\10\1\61\3\10\4\0"+
    "\27\10\2\0\2\10\2\0\1\10\2\0\3\10\4\0"+
    "\5\10\4\0\17\10\1\62\7\10\2\0\2\10\2\0"+
    "\1\10\2\0\3\10\4\0\5\10\4\0\23\10\1\63"+
    "\3\10\2\0\2\10\2\0\1\10\2\0\3\10\4\0"+
    "\1\64\4\10\4\0\27\10\2\0\2\10\54\0\1\34"+
    "\3\0\7\35\4\0\45\35\4\65\1\66\2\65\1\67"+
    "\1\70\2\0\30\65\1\71\1\72\2\65\1\73\5\65"+
    "\1\0\1\74\1\75\4\40\1\0\2\40\1\0\1\40"+
    "\2\0\45\40\4\65\1\66\2\65\1\67\1\70\2\0"+
    "\42\65\1\0\2\65\2\0\1\76\57\0\1\10\2\0"+
    "\3\10\4\0\2\10\1\77\2\10\4\0\27\10\2\0"+
    "\2\10\2\0\1\10\2\0\3\10\4\0\5\10\4\0"+
    "\1\10\1\100\25\10\2\0\2\10\2\0\1\10\2\0"+
    "\3\10\4\0\5\10\4\0\10\10\1\101\16\10\2\0"+
    "\2\10\2\0\1\10\2\0\3\10\4\0\5\10\4\0"+
    "\12\10\1\102\14\10\2\0\2\10\2\0\1\10\2\0"+
    "\3\10\4\0\1\103\4\10\4\0\27\10\2\0\2\10"+
    "\2\0\1\10\2\0\3\10\4\0\5\10\4\0\11\10"+
    "\1\104\15\10\2\0\2\10\2\0\1\10\2\0\3\10"+
    "\4\0\5\10\4\0\15\10\1\105\11\10\2\0\2\10"+
    "\2\0\1\10\2\0\3\10\4\0\5\10\4\0\7\10"+
    "\1\106\17\10\2\0\2\10\2\0\1\10\2\0\3\10"+
    "\4\0\2\10\1\107\2\10\4\0\27\10\2\0\2\10"+
    "\2\0\1\10\2\0\3\10\4\0\5\10\4\0\20\10"+
    "\1\110\6\10\2\0\2\10\2\0\1\10\2\0\3\10"+
    "\4\0\5\10\4\0\24\10\1\111\2\10\2\0\2\10"+
    "\2\0\1\10\2\0\3\10\4\0\5\10\4\0\10\10"+
    "\1\112\16\10\2\0\2\10\2\0\1\10\2\0\3\10"+
    "\4\0\5\10\4\0\10\10\1\113\16\10\2\0\2\10"+
    "\2\0\1\10\2\0\3\10\4\0\5\10\4\0\6\10"+
    "\1\114\20\10\2\0\2\10\2\0\1\10\2\0\3\10"+
    "\4\0\4\10\1\115\4\0\27\10\2\0\2\10\2\0"+
    "\1\10\2\0\3\10\4\0\5\10\4\0\6\10\1\116"+
    "\20\10\2\0\2\10\2\0\1\10\2\0\3\10\4\0"+
    "\5\10\4\0\6\10\1\117\20\10\2\0\2\10\2\0"+
    "\1\10\2\0\3\10\4\0\5\10\4\0\10\10\1\120"+
    "\16\10\2\0\2\10\2\0\1\10\2\0\3\10\4\0"+
    "\5\10\4\0\2\10\1\121\24\10\2\0\2\10\2\0"+
    "\1\10\2\0\3\10\4\0\5\10\4\0\21\10\1\116"+
    "\5\10\2\0\2\10\2\0\1\10\2\0\3\10\4\0"+
    "\5\10\4\0\25\10\1\110\1\10\2\0\2\10\2\0"+
    "\1\10\2\0\3\10\4\0\5\10\4\0\1\104\26\10"+
    "\2\0\2\10\2\0\1\10\2\0\3\10\4\0\5\10"+
    "\4\0\13\10\1\122\13\10\2\0\2\10\2\0\1\10"+
    "\2\0\3\10\4\0\5\10\4\0\1\10\1\123\25\10"+
    "\2\0\2\10\2\0\1\10\2\0\3\10\4\0\5\10"+
    "\4\0\1\124\26\10\2\0\2\10\2\0\1\10\2\0"+
    "\3\10\4\0\5\10\4\0\11\10\1\125\15\10\2\0"+
    "\2\10\2\0\1\10\2\0\3\10\4\0\5\10\4\0"+
    "\3\10\1\126\23\10\2\0\2\10\2\0\1\10\2\0"+
    "\3\10\4\0\5\10\4\0\14\10\1\127\12\10\2\0"+
    "\2\10\2\0\1\10\2\0\3\10\4\0\5\10\4\0"+
    "\2\10\1\130\24\10\2\0\2\10\2\0\1\10\2\0"+
    "\3\10\4\0\5\10\4\0\1\10\1\131\25\10\2\0"+
    "\2\10\2\0\1\10\2\0\3\10\4\0\2\10\1\132"+
    "\2\10\4\0\27\10\2\0\2\10\2\0\1\10\2\0"+
    "\3\10\4\0\5\10\4\0\4\10\1\133\22\10\2\0"+
    "\2\10\2\0\1\10\2\0\3\10\4\0\5\10\4\0"+
    "\3\10\1\134\23\10\2\0\2\10\2\0\1\10\2\0"+
    "\3\10\4\0\1\135\4\10\4\0\27\10\2\0\2\10"+
    "\2\0\1\10\2\0\3\10\4\0\5\10\4\0\6\10"+
    "\1\136\20\10\2\0\2\10\2\0\1\10\2\0\3\10"+
    "\4\0\5\10\4\0\1\10\1\137\25\10\2\0\2\10"+
    "\2\0\1\10\2\0\3\10\4\0\5\10\4\0\4\10"+
    "\1\140\22\10\2\0\2\10\2\0\1\10\2\0\3\10"+
    "\4\0\5\10\4\0\15\10\1\141\11\10\2\0\2\10"+
    "\2\0\1\10\2\0\3\10\4\0\5\10\4\0\5\10"+
    "\1\142\21\10\2\0\2\10\2\0\1\10\2\0\3\10"+
    "\4\0\5\10\4\0\1\10\1\143\25\10\2\0\2\10"+
    "\2\0\1\10\2\0\3\10\4\0\5\10\4\0\14\10"+
    "\1\144\12\10\2\0\2\10\2\0\1\10\2\0\3\10"+
    "\4\0\5\10\4\0\5\10\1\145\21\10\2\0\2\10";

  private static int [] zzUnpackTrans() {
    int [] result = new int[3360];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\3\0\1\11\2\1\1\11\1\1\1\11\1\1\2\11"+
    "\4\1\1\11\15\1\1\11\1\1\1\11\1\1\1\0"+
    "\5\1\3\11\11\1\11\11\50\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[101];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;
  
  /** 
   * The number of occupied positions in zzBuffer beyond zzEndRead.
   * When a lead/high surrogate has been read from the input stream
   * into the final zzBuffer position, this will have a value of 1;
   * otherwise, it will have a value of 0.
   */
  private int zzFinalHighSurrogate = 0;

  /* user code: */
  private StringBuffer string = new StringBuffer();

  protected Parser yyparser;
  protected Schema schema;

  public Yylex(String str, Parser yyparser) {
    this(new java.io.StringReader(str));
    this.yyparser = yyparser;
    this.schema = yyparser.schema;
  }


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  Yylex(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x110000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 180) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length - zzFinalHighSurrogate) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzBuffer.length*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
    }

    /* fill the buffer with new input */
    int requested = zzBuffer.length - zzEndRead;
    int numRead = zzReader.read(zzBuffer, zzEndRead, requested);

    /* not supposed to occur according to specification of java.io.Reader */
    if (numRead == 0) {
      throw new java.io.IOException("Reader returned 0 characters. See JFlex examples for workaround.");
    }
    if (numRead > 0) {
      zzEndRead += numRead;
      /* If numRead == requested, we might have requested to few chars to
         encode a full Unicode character. We assume that a Reader would
         otherwise never return half characters. */
      if (numRead == requested) {
        if (Character.isHighSurrogate(zzBuffer[zzEndRead - 1])) {
          --zzEndRead;
          zzFinalHighSurrogate = 1;
        }
      }
      /* potentially more input available */
      return false;
    }

    /* numRead < 0 ==> end of stream */
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * Internal scan buffer is resized down to its initial length, if it has grown.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    zzFinalHighSurrogate = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
    if (zzBuffer.length > ZZ_BUFFERSIZE)
      zzBuffer = new char[ZZ_BUFFERSIZE];
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public int yylex() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
            zzDoEOF();
          { return 0; }
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { throw new ConfigException("yylex: Unexpected character '"+yytext()+"'");
            }
          case 37: break;
          case 2: 
            { yyparser.yylval = new ParserVal(new NumberLiteral(yytext())); return Parser.NUMBER;
            }
          case 38: break;
          case 3: 
            { yybegin(IDENTIFIER); string.setLength(0);
            }
          case 39: break;
          case 4: 
            { yyparser.yylval = new ParserVal(new IdentifierLiteral(yytext(), schema)); return Parser.IDENTIFIER;
            }
          case 40: break;
          case 5: 
            { yybegin(STRING); string.setLength(0);
            }
          case 41: break;
          case 6: 
            { 
            }
          case 42: break;
          case 7: 
            { return (int) yycharat(0);
            }
          case 43: break;
          case 8: 
            { return Parser.EQ;
            }
          case 44: break;
          case 9: 
            { return Parser.LT;
            }
          case 45: break;
          case 10: 
            { return Parser.GT;
            }
          case 46: break;
          case 11: 
            { string.append( yytext() );
            }
          case 47: break;
          case 12: 
            { yybegin(YYINITIAL); yyparser.yylval = new ParserVal(new StringLiteral(string.toString())); return Parser.STRING;
            }
          case 48: break;
          case 13: 
            { yybegin(YYINITIAL); yyparser.yylval = new ParserVal(new IdentifierLiteral(string.toString(), schema)); return Parser.IDENTIFIER;
            }
          case 49: break;
          case 14: 
            { return Parser.OR;
            }
          case 50: break;
          case 15: 
            { return Parser.LE;
            }
          case 51: break;
          case 16: 
            { return Parser.NEQ;
            }
          case 52: break;
          case 17: 
            { return Parser.GE;
            }
          case 53: break;
          case 18: 
            { return Parser.IS;
            }
          case 54: break;
          case 19: 
            { throw new ConfigException("yylex: Illegal escape sequence \""+yytext()+"\"");
            }
          case 55: break;
          case 20: 
            { string.append( '\"' );
            }
          case 56: break;
          case 21: 
            { string.append( '\\' );
            }
          case 57: break;
          case 22: 
            { string.append( '\'' );
            }
          case 58: break;
          case 23: 
            { string.append( '\t' );
            }
          case 59: break;
          case 24: 
            { string.append( '\r' );
            }
          case 60: break;
          case 25: 
            { string.append( '\f' );
            }
          case 61: break;
          case 26: 
            { string.append( '\b' );
            }
          case 62: break;
          case 27: 
            { string.append( '\n' );
            }
          case 63: break;
          case 28: 
            { return Parser.AND;
            }
          case 64: break;
          case 29: 
            { return Parser.NOT;
            }
          case 65: break;
          case 30: 
            { return Parser.NULL;
            }
          case 66: break;
          case 31: 
            { yyparser.yylval = new ParserVal(new BooleanLiteral(yytext())); return Parser.BOOLEAN;
            }
          case 67: break;
          case 32: 
            { return Parser.REGEXP;
            }
          case 68: break;
          case 33: 
            { return Parser.INCLUDE;
            }
          case 69: break;
          case 34: 
            { return Parser.END_WITH;
            }
          case 70: break;
          case 35: 
            { return Parser.TIMESTAMP;
            }
          case 71: break;
          case 36: 
            { return Parser.START_WITH;
            }
          case 72: break;
          default:
            zzScanError(ZZ_NO_MATCH);
        }
      }
    }
  }


}
