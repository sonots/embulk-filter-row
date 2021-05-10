package org.embulk.filter.row.where;

import org.embulk.config.ConfigException;
import org.embulk.spi.Schema;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.embulk.spi.type.Types.BOOLEAN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestYylex
{
    Parser yyparser;
    Yylex lexer;

    @Before
    public void setUp()
    {
        yyparser = new Parser(Schema.builder().build());
    }

    void assertNextToken(int token)
    {
        try {
            assertEquals(token, lexer.yylex());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    ParserNode currentNode()
    {
       return (ParserNode)(yyparser.yylval.obj);
    }

    @Test
    public void testOperator()
    {
        lexer = new Yylex("START_WITH", yyparser);
        assertNextToken(Parser.START_WITH);

        lexer = new Yylex("(", yyparser);
        assertNextToken('(');

        lexer = new Yylex(" = ", yyparser);
        assertNextToken(Parser.EQ);
    }

    void assertIdentifier(String name)
    {
        assertEquals("IdentifierLiteral", currentNode().getClass().getSimpleName());
        assertEquals(name, ((IdentifierLiteral)currentNode()).name);
    }

    @Test
    public void testIdentifier()
    {
        Schema schema = Schema.builder()
                .add("foobar", BOOLEAN)
                .add("_foobar", BOOLEAN)
                .add("foo bar", BOOLEAN)
                .add("foo\"bar", BOOLEAN)
                .add("$.foo.bar", BOOLEAN)
                .add("$['foo'][0:4][*]", BOOLEAN)
                .build();
        yyparser = new Parser(schema);

        lexer = new Yylex("foobar", yyparser);
        assertNextToken(Parser.IDENTIFIER);
        assertIdentifier("foobar");

        lexer = new Yylex(" foobar ", yyparser);
        assertNextToken(Parser.IDENTIFIER);
        assertIdentifier("foobar");

        lexer = new Yylex("\"foobar\"", yyparser);
        assertNextToken(Parser.IDENTIFIER);
        assertIdentifier("foobar");

        lexer = new Yylex(" _foobar ", yyparser);
        assertNextToken(Parser.IDENTIFIER);
        assertIdentifier("_foobar");

        lexer = new Yylex(" \"foo bar\" ", yyparser);
        assertNextToken(Parser.IDENTIFIER);
        assertIdentifier("foo bar");

        lexer = new Yylex(" \"foo\\\"bar\" ", yyparser);
        assertNextToken(Parser.IDENTIFIER);
        assertIdentifier("foo\"bar");

        lexer = new Yylex("\"$.foo.bar\"", yyparser);
        assertNextToken(Parser.IDENTIFIER);
        assertIdentifier("$.foo.bar");

        lexer = new Yylex("\"$['foo'][0:4][*]\"", yyparser);
        assertNextToken(Parser.IDENTIFIER);
        assertIdentifier("$['foo'][0:4][*]");

        try {
            lexer = new Yylex("foo-bar", yyparser);
            assertNextToken(Parser.IDENTIFIER);
            assertTrue(false);
        }
        catch (ConfigException e) {
        }

        try {
            lexer = new Yylex("$['foo'][0:4][*]", yyparser);
            assertNextToken(Parser.IDENTIFIER);
            assertTrue(false);
        }
        catch (ConfigException e) {
        }

        try {
            lexer = new Yylex("unknown", yyparser);
            assertNextToken(Parser.IDENTIFIER);
            assertTrue(false);
        }
        catch (ConfigException e) {
        }
    }

    void assertBoolean(boolean val)
    {
        assertEquals("BooleanLiteral", currentNode().getClass().getSimpleName());
        assertEquals(val, ((BooleanLiteral)currentNode()).val);
    }

    @Test
    public void testBoolean()
    {
        lexer = new Yylex("true", yyparser);
        assertNextToken(Parser.BOOLEAN);
        assertBoolean(true);

        lexer = new Yylex("false", yyparser);
        assertNextToken(Parser.BOOLEAN);
        assertBoolean(false);

        lexer = new Yylex("TRUE", yyparser);
        assertNextToken(Parser.BOOLEAN);
        assertBoolean(true);

        lexer = new Yylex("FALSE", yyparser);
        assertNextToken(Parser.BOOLEAN);
        assertBoolean(false);

        lexer = new Yylex(" true ", yyparser);
        assertNextToken(Parser.BOOLEAN);
        assertBoolean(true);
    }

    void assertNumber(double val)
    {
        assertEquals("NumberLiteral", currentNode().getClass().getSimpleName());
        assertEquals(val, ((NumberLiteral)currentNode()).val, 0.0);
    }

    @Test
    public void testNumber()
    {
        lexer = new Yylex("1.5", yyparser);
        assertNextToken(Parser.NUMBER);
        assertNumber(1.5);

        lexer = new Yylex("1", yyparser);
        assertNextToken(Parser.NUMBER);
        assertNumber(1);

        lexer = new Yylex("-1.5", yyparser);
        assertNextToken(Parser.NUMBER);
        assertNumber(-1.5);

        lexer = new Yylex(" -1.5 ", yyparser);
        assertNextToken(Parser.NUMBER);
        assertNumber(-1.5);
    }

    void assertString(String val)
    {
        assertEquals("StringLiteral", currentNode().getClass().getSimpleName());
        assertEquals(val, ((StringLiteral)currentNode()).val);
    }

    @Test
    public void testString()
    {
        lexer = new Yylex("'foobar'", yyparser);
        assertNextToken(Parser.STRING);
        assertString("foobar");

        lexer = new Yylex("'foo bar'", yyparser);
        assertNextToken(Parser.STRING);
        assertString("foo bar");

        lexer = new Yylex("'foo\\'bar'", yyparser);
        assertNextToken(Parser.STRING);
        assertString("foo\'bar");
    }

    @Test
    public void testTimestamp()
    {
        lexer = new Yylex("TIMESTAMP 1.5", yyparser);
        assertNextToken(Parser.TIMESTAMP);
        assertNextToken(Parser.NUMBER);
        assertNumber(1.5);

        lexer = new Yylex("TIMESTAMP '2015-01-01'", yyparser);
        assertNextToken(Parser.TIMESTAMP);
        assertNextToken(Parser.STRING);
        assertString("2015-01-01");
    }

    @Test
    public void testComplex()
    {
        Schema schema = Schema.builder()
                .add("true", BOOLEAN)
                .add("foobar", BOOLEAN)
                .build();
        yyparser = new Parser(schema);

        lexer = new Yylex("( \"true\" = true OR \"true\" = false ) AND foobar = false", yyparser);
        assertNextToken('(');
        assertNextToken(Parser.IDENTIFIER);
        assertNextToken(Parser.EQ);
        assertNextToken(Parser.BOOLEAN);
        assertNextToken(Parser.OR);
        assertNextToken(Parser.IDENTIFIER);
        assertNextToken(Parser.EQ);
        assertNextToken(Parser.BOOLEAN);
        assertNextToken(')');
        assertNextToken(Parser.AND);
        assertNextToken(Parser.IDENTIFIER);
        assertNextToken(Parser.EQ);
        assertNextToken(Parser.BOOLEAN);

        lexer = new Yylex("(\"true\"=true OR\"true\"=false)AND foobar=false", yyparser);
        assertNextToken('(');
        assertNextToken(Parser.IDENTIFIER);
        assertNextToken(Parser.EQ);
        assertNextToken(Parser.BOOLEAN);
        assertNextToken(Parser.OR);
        assertNextToken(Parser.IDENTIFIER);
        assertNextToken(Parser.EQ);
        assertNextToken(Parser.BOOLEAN);
        assertNextToken(')');
        assertNextToken(Parser.AND);
        assertNextToken(Parser.IDENTIFIER);
        assertNextToken(Parser.EQ);
        assertNextToken(Parser.BOOLEAN);

        lexer = new Yylex("NOT(true=\"true\")", yyparser);
        assertNextToken(Parser.NOT);
        assertNextToken('(');
        assertNextToken(Parser.BOOLEAN);
        assertNextToken(Parser.EQ);
        assertNextToken(Parser.IDENTIFIER);
        assertNextToken(')');
    }
}
