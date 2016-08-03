package org.embulk.filter.row.where;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestParser
{
    HashMap<String, Object> variables;

    public TestParser()
    {
        variables = new HashMap<>();
        variables.put("true", Boolean.TRUE);
        variables.put("boolean", Boolean.TRUE);
        variables.put("long", new Long(1));
        variables.put("double", new Double(1.5));
        variables.put("string", "string");
    }

    @Test
    public void testIdentifierLiteral()
    {
        Parser parser = new Parser(false);
        ParserExp exp;

        exp = parser.parse("boolean = true");
        assertTrue(exp.eval(variables));

        exp = parser.parse("\"true\" = true");
        assertTrue(exp.eval(variables));

        exp = parser.parse("\"false\" IS NULL");
        assertTrue(exp.eval(variables));
    }

    @Test
    public void testBooleanOpExp()
    {
        Parser parser = new Parser(false);
        ParserExp exp;

        exp = parser.parse("boolean = true");
        assertTrue(exp.eval(variables));
        exp = parser.parse("boolean = false");
        assertFalse(exp.eval(variables));

        exp = parser.parse("boolean != false");
        assertTrue(exp.eval(variables));
        exp = parser.parse("boolean != true");
        assertFalse(exp.eval(variables));

        exp = parser.parse("true = boolean");
        assertTrue(exp.eval(variables));
    }

    @Test
    public void testNumberOpExp()
    {
        Parser parser = new Parser(false);
        ParserExp exp;

        exp = parser.parse("double = 1.5");
        assertTrue(exp.eval(variables));
        exp = parser.parse("double = 1.0");
        assertFalse(exp.eval(variables));

        exp = parser.parse("double != 1.0");
        assertTrue(exp.eval(variables));
        exp = parser.parse("double != 1.5");
        assertFalse(exp.eval(variables));

        exp = parser.parse("double > 1.0");
        assertTrue(exp.eval(variables));
        exp = parser.parse("double > 1.5");
        assertFalse(exp.eval(variables));

        exp = parser.parse("double >= 1.5");
        assertTrue(exp.eval(variables));
        exp = parser.parse("double >= 2.0");
        assertFalse(exp.eval(variables));

        exp = parser.parse("double < 2.0");
        assertTrue(exp.eval(variables));
        exp = parser.parse("double < 1.5");
        assertFalse(exp.eval(variables));

        exp = parser.parse("double <= 1.5");
        assertTrue(exp.eval(variables));
        exp = parser.parse("double <= 1.0");
        assertFalse(exp.eval(variables));

        exp = parser.parse("1.5 = double");
        assertTrue(exp.eval(variables));
    }

    @Test
    public void testStringOpExp()
    {
        Parser parser = new Parser(false);
        ParserExp exp;

        exp = parser.parse("string = 'string'");
        assertTrue(exp.eval(variables));
        exp = parser.parse("string = 'foobar'");
        assertFalse(exp.eval(variables));

        exp = parser.parse("string != 'foobar'");
        assertTrue(exp.eval(variables));
        exp = parser.parse("string != 'string'");
        assertFalse(exp.eval(variables));

        exp = parser.parse("string <> 'foobar'");
        assertTrue(exp.eval(variables));
        exp = parser.parse("string <> 'string'");
        assertFalse(exp.eval(variables));

        exp = parser.parse("string START_WITH 's'");
        assertTrue(exp.eval(variables));
        exp = parser.parse("string START_WITH 'f'");
        assertFalse(exp.eval(variables));

        exp = parser.parse("string END_WITH 'g'");
        assertTrue(exp.eval(variables));
        exp = parser.parse("string END_WITH 'r'");
        assertFalse(exp.eval(variables));

        exp = parser.parse("string INCLUDE 'tr'");
        assertTrue(exp.eval(variables));
        exp = parser.parse("string INCLUDE 'oo'");
        assertFalse(exp.eval(variables));

        exp = parser.parse("'string' = string");
        assertTrue(exp.eval(variables));
    }

    @Test
    public void testNullOpExp()
    {
        Parser parser = new Parser(false);
        ParserExp exp;

        exp = parser.parse("foo IS NULL");
        assertTrue(exp.eval(variables));
        exp = parser.parse("foo IS NOT NULL");
        assertFalse(exp.eval(variables));

        exp = parser.parse("string IS NOT NULL");
        assertTrue(exp.eval(variables));
        exp = parser.parse("string IS NULL");
        assertFalse(exp.eval(variables));
    }

    @Test
    public void testLogicalOpExp()
    {
        Parser parser = new Parser(false);
        ParserExp exp;

        exp = parser.parse("\"true\" = true AND \"true\" = true");
        assertTrue(exp.eval(variables));
        exp = parser.parse("\"true\" = true AND \"true\" = false");
        assertFalse(exp.eval(variables));

        exp = parser.parse("\"true\" = false OR \"true\" = true");
        assertTrue(exp.eval(variables));
        exp = parser.parse("\"true\" = false OR \"true\" = false");
        assertFalse(exp.eval(variables));

        // a AND b OR c #=> (a AND b) OR c
        // a OR b AND c #=> a OR (b AND c)
        exp = parser.parse("\"true\" = true OR \"true\" = false AND \"true\" = false");
        assertTrue(exp.eval(variables));
        exp = parser.parse("( \"true\" = true OR \"true\" = false ) AND \"true\" = false");
        assertFalse(exp.eval(variables));
    }
    @Test
    public void testNegateOpExp()
    {
        Parser parser = new Parser(false);
        ParserExp exp;

        exp = parser.parse("NOT \"true\" = false");
        assertTrue(exp.eval(variables));
        exp = parser.parse("NOT ( \"true\" = false )");
        assertTrue(exp.eval(variables));

        // NOT a AND B #=> (NOT a) AND B
        exp = parser.parse("NOT \"true\" = false AND \"true\" = true");
        assertTrue(exp.eval(variables));
    }
}
