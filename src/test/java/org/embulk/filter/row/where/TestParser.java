package org.embulk.filter.row.where;

import org.embulk.EmbulkTestRuntime;
import org.embulk.config.Config;
import org.embulk.config.ConfigException;
import org.embulk.spi.Page;
import org.embulk.spi.PageReader;
import org.embulk.spi.PageTestUtils;
import org.embulk.spi.Schema;
import org.embulk.spi.SchemaConfigException;
import org.embulk.spi.time.Timestamp;

import org.embulk.spi.time.TimestampParseException;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

import java.util.List;

import static org.embulk.spi.type.Types.BOOLEAN;
import static org.embulk.spi.type.Types.DOUBLE;
import static org.embulk.spi.type.Types.JSON;
import static org.embulk.spi.type.Types.LONG;
import static org.embulk.spi.type.Types.STRING;
import static org.embulk.spi.type.Types.TIMESTAMP;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestParser
{
    private static EmbulkTestRuntime runtime = new EmbulkTestRuntime(); // very slow

    private static PageReader buildPageReader(Schema schema, final Object... objects)
    {
        PageReader pageReader = new PageReader(schema);
        List<Page> pages = PageTestUtils.buildPage(runtime.getBufferAllocator(), schema, objects);
        for (Page page : pages) {
            pageReader.setPage(page);
        }
        pageReader.nextRecord();
        return pageReader;
    }

    private static PageReader reader;
    private static Schema schema;

    @BeforeClass
    public static void setupBeforeClass()
    {
        // {"k1":{"k1":"v"},"k2":{"k2":"v"}}
        Value k1 = ValueFactory.newString("k1");
        Value k2 = ValueFactory.newString("k2");
        Value v = ValueFactory.newString("v");
        Value map = ValueFactory.newMap(
                k1, ValueFactory.newMap(k1, v),
                k2, ValueFactory.newMap(k2, v));

        schema = Schema.builder()
                .add("timestamp", TIMESTAMP)
                .add("string", STRING)
                .add("boolean", BOOLEAN)
                .add("long", LONG)
                .add("double", DOUBLE)
                .add("true", BOOLEAN)
                .add("null", BOOLEAN)
                .add("json", JSON)
                .build();

        reader = buildPageReader(schema,
                Timestamp.ofEpochSecond(1, 500000000),
                "string",
                true,
                1L,
                1.5,
                true,
                null,
                map
                );
    }

    @Test
    public void testIdentifierLiteral()
    {
        Parser parser = new Parser(schema);
        ParserExp exp;

        exp = parser.parse("boolean = true");
        assertTrue(exp.eval(reader));

        exp = parser.parse("\"true\" = true");
        assertTrue(exp.eval(reader));

        try {
            parser.parse("\"unknown\" IS NULL");
            assertTrue(false);
        }
        catch (SchemaConfigException e) {
        }
    }

    @Test
    public void testBooleanOpExp()
    {
        Parser parser = new Parser(schema);
        ParserExp exp;

        exp = parser.parse("boolean = true");
        assertTrue(exp.eval(reader));
        exp = parser.parse("boolean = false");
        assertFalse(exp.eval(reader));

        exp = parser.parse("boolean != false");
        assertTrue(exp.eval(reader));
        exp = parser.parse("boolean != true");
        assertFalse(exp.eval(reader));

        exp = parser.parse("true = boolean");
        assertTrue(exp.eval(reader));

        try {
            parser.parse("timestamp = true");
            assertTrue(false);
        }
        catch (ConfigException e) {
        }

        try {
            parser.parse("boolean > true");
            assertTrue(false);
        }
        catch (ConfigException e) {
        }
    }

    @Test
    public void testNumberOpExp()
    {
        Parser parser = new Parser(schema);
        ParserExp exp;

        exp = parser.parse("double = 1.5");
        assertTrue(exp.eval(reader));
        exp = parser.parse("double = 1.0");
        assertFalse(exp.eval(reader));

        exp = parser.parse("double != 1.0");
        assertTrue(exp.eval(reader));
        exp = parser.parse("double != 1.5");
        assertFalse(exp.eval(reader));

        exp = parser.parse("double > 1.0");
        assertTrue(exp.eval(reader));
        exp = parser.parse("double > 1.5");
        assertFalse(exp.eval(reader));

        exp = parser.parse("double >= 1.5");
        assertTrue(exp.eval(reader));
        exp = parser.parse("double >= 2.0");
        assertFalse(exp.eval(reader));

        exp = parser.parse("double < 2.0");
        assertTrue(exp.eval(reader));
        exp = parser.parse("double < 1.5");
        assertFalse(exp.eval(reader));

        exp = parser.parse("double <= 1.5");
        assertTrue(exp.eval(reader));
        exp = parser.parse("double <= 1.0");
        assertFalse(exp.eval(reader));

        exp = parser.parse("1.5 = double");
        assertTrue(exp.eval(reader));

        try {
            parser.parse("boolean = 1.5");
            assertTrue(false);
        }
        catch (ConfigException e) {
        }

        try {
            parser.parse("double START_WITH 1.5");
            assertTrue(false);
        }
        catch (ConfigException e) {
        }
    }

    @Test
    public void testStringOpExp()
    {
        Parser parser = new Parser(schema);
        ParserExp exp;

        exp = parser.parse("string = 'string'");
        assertTrue(exp.eval(reader));
        exp = parser.parse("string = 'foobar'");
        assertFalse(exp.eval(reader));

        exp = parser.parse("string != 'foobar'");
        assertTrue(exp.eval(reader));
        exp = parser.parse("string != 'string'");
        assertFalse(exp.eval(reader));

        exp = parser.parse("string <> 'foobar'");
        assertTrue(exp.eval(reader));
        exp = parser.parse("string <> 'string'");
        assertFalse(exp.eval(reader));

        exp = parser.parse("string > 's'");
        assertTrue(exp.eval(reader));
        exp = parser.parse("string > 't'");
        assertFalse(exp.eval(reader));

        exp = parser.parse("string >= 's'");
        assertTrue(exp.eval(reader));
        exp = parser.parse("string >= 't'");
        assertFalse(exp.eval(reader));

        exp = parser.parse("string < 't'");
        assertTrue(exp.eval(reader));
        exp = parser.parse("string < 's'");
        assertFalse(exp.eval(reader));

        exp = parser.parse("string <= 't'");
        assertTrue(exp.eval(reader));
        exp = parser.parse("string <= 's'");
        assertFalse(exp.eval(reader));

        exp = parser.parse("string START_WITH 's'");
        assertTrue(exp.eval(reader));
        exp = parser.parse("string START_WITH 'f'");
        assertFalse(exp.eval(reader));

        exp = parser.parse("string END_WITH 'g'");
        assertTrue(exp.eval(reader));
        exp = parser.parse("string END_WITH 'r'");
        assertFalse(exp.eval(reader));

        exp = parser.parse("string INCLUDE 'tr'");
        assertTrue(exp.eval(reader));
        exp = parser.parse("string INCLUDE 'oo'");
        assertFalse(exp.eval(reader));

        exp = parser.parse("'string' = string");
        assertTrue(exp.eval(reader));

        try {
            parser.parse("boolean = 'string'");
            assertTrue(false);
        }
        catch (ConfigException e) {
        }

        try {
            parser.parse("string AND 'string'");
            assertTrue(false);
        }
        catch (ConfigException e) {
        }
    }

    @Test
    public void testTimestampOpExpWithNumber()
    {
        Parser parser = new Parser(schema);
        ParserExp exp;

        exp = parser.parse("timestamp = TIMESTAMP 1.5");
        assertTrue(exp.eval(reader));
        exp = parser.parse("timestamp = TIMESTAMP 1.0");
        assertFalse(exp.eval(reader));

        exp = parser.parse("timestamp != TIMESTAMP 1.0");
        assertTrue(exp.eval(reader));
        exp = parser.parse("timestamp != TIMESTAMP 1.5");
        assertFalse(exp.eval(reader));

        exp = parser.parse("timestamp > TIMESTAMP 1.0");
        assertTrue(exp.eval(reader));
        exp = parser.parse("timestamp > TIMESTAMP 1.5");
        assertFalse(exp.eval(reader));

        exp = parser.parse("timestamp >= TIMESTAMP 1.5");
        assertTrue(exp.eval(reader));
        exp = parser.parse("timestamp >= TIMESTAMP 2.0");
        assertFalse(exp.eval(reader));

        exp = parser.parse("timestamp < TIMESTAMP 2.0");
        assertTrue(exp.eval(reader));
        exp = parser.parse("timestamp < TIMESTAMP 1.5");
        assertFalse(exp.eval(reader));

        exp = parser.parse("timestamp <= TIMESTAMP 1.5");
        assertTrue(exp.eval(reader));
        exp = parser.parse("timestamp <= TIMESTAMP 1.0");
        assertFalse(exp.eval(reader));

        exp = parser.parse("TIMESTAMP 1.5 = timestamp");
        assertTrue(exp.eval(reader));

        // auto guess of TIMESTAMP
        exp = parser.parse("timestamp = 1.5");
        assertTrue(exp.eval(reader));
        exp = parser.parse("1.5 = timestamp");
        assertTrue(exp.eval(reader));

        try {
            parser.parse("timestamp START_WITH 1.5");
            assertTrue(false);
        }
        catch (ConfigException e) {
        }
    }

    @Test
    public void testTimestampOpExpWithString()
    {
        Parser parser = new Parser(schema);
        ParserExp exp;

        exp = parser.parse("timestamp = TIMESTAMP '1970-01-01 09:00:01.5 +0900'");
        assertTrue(exp.eval(reader));
        exp = parser.parse("timestamp = TIMESTAMP '1970-01-01 09:00:01.0 +09:00'");
        assertFalse(exp.eval(reader));

        exp = parser.parse("timestamp != TIMESTAMP '1970-01-01 09:00:01.0 +0900'");
        assertTrue(exp.eval(reader));
        exp = parser.parse("timestamp != TIMESTAMP '1970-01-01 09:00:01.5 +0900'");
        assertFalse(exp.eval(reader));

        exp = parser.parse("timestamp >  TIMESTAMP '1970-01-01 09:00:01.0 +0900'");
        assertTrue(exp.eval(reader));
        exp = parser.parse("timestamp >  TIMESTAMP '1970-01-01 09:00:01.5 +0900'");
        assertFalse(exp.eval(reader));

        exp = parser.parse("timestamp >= TIMESTAMP '1970-01-01 09:00:01.5 +09:00'");
        assertTrue(exp.eval(reader));
        exp = parser.parse("timestamp >= TIMESTAMP '1970-01-01 09:00:02.0 +09:00'");
        assertFalse(exp.eval(reader));

        exp = parser.parse("timestamp <  TIMESTAMP '1970-01-01 09:00:02.0 +09:00'");
        assertTrue(exp.eval(reader));
        exp = parser.parse("timestamp <  TIMESTAMP '1970-01-01 09:00:01.5 +09:00'");
        assertFalse(exp.eval(reader));

        exp = parser.parse("timestamp <= TIMESTAMP '1970-01-01 09:00:01.5 +09:00'");
        assertTrue(exp.eval(reader));
        exp = parser.parse("timestamp <= TIMESTAMP '1970-01-01 09:00:01.0 +09:00'");
        assertFalse(exp.eval(reader));

        exp = parser.parse("TIMESTAMP '1970-01-01 09:00:01.5 +09:00' = timestamp");
        assertTrue(exp.eval(reader));

        exp = parser.parse("timestamp = TIMESTAMP '1970-01-01 09:00:01.5 +0900'");
        assertTrue(exp.eval(reader));
        exp = parser.parse("timestamp = TIMESTAMP '1970-01-01 00:00:01.5'");
        assertTrue(exp.eval(reader));
        exp = parser.parse("timestamp = TIMESTAMP '1970-01-01 09:00:01 +09:00'");
        assertFalse(exp.eval(reader));
        exp = parser.parse("timestamp = TIMESTAMP '1970-01-01 00:00:01'");
        assertFalse(exp.eval(reader));
        exp = parser.parse("timestamp = TIMESTAMP '1970-01-01 +09:00'");
        assertFalse(exp.eval(reader));
        exp = parser.parse("timestamp = TIMESTAMP '1970-01-01'");
        assertFalse(exp.eval(reader));

        // auto guess of TIMESTAMP
        exp = parser.parse("timestamp = '1970-01-01 09:00:01.5 +0900'");
        assertTrue(exp.eval(reader));
        exp = parser.parse("'1970-01-01 09:00:01.5 +0900' = timestamp");
        assertTrue(exp.eval(reader));

        try {
            parser.parse("timestamp = '1970:01:01'");
            assertTrue(false);
        }
        catch (TimestampParseException ex) {
        }

        try {
            parser.parse("timestamp START_WITH '1970-01-01 09:00:01.5 +0900'");
            assertTrue(false);
        }
        catch (ConfigException e) {
        }
    }

    @Test
    public void testRegexpOpExp()
    {
        Parser parser = new Parser(schema);
        ParserExp exp;

        exp = parser.parse("string REGEXP '^st'");
        assertTrue(exp.eval(reader));
        exp = parser.parse("string REGEXP 'st$'");
        assertFalse(exp.eval(reader));

        try {
            // right-side identifier is not allowed
            parser.parse("'string' REGEXP string");
            assertTrue(false);
        }
        catch (ConfigException e) {
        }

        try {
            parser.parse("string REGEXP 1.5");
            assertTrue(false);
        }
        catch (ConfigException e) {
        }

        try {
            parser.parse("boolean REGEXP '^st'");
            assertTrue(false);
        }
        catch (ConfigException e) {
        }
    }

    @Test
    public void testNullOpExp()
    {
        Parser parser = new Parser(schema);
        ParserExp exp;

        exp = parser.parse("null IS NULL");
        assertTrue(exp.eval(reader));
        exp = parser.parse("null IS NOT NULL");
        assertFalse(exp.eval(reader));

        exp = parser.parse("string IS NOT NULL");
        assertTrue(exp.eval(reader));
        exp = parser.parse("string IS NULL");
        assertFalse(exp.eval(reader));
    }

    @Test
    public void testLogicalOpExp()
    {
        Parser parser = new Parser(schema);
        ParserExp exp;

        exp = parser.parse("\"true\" = true AND \"true\" = true");
        assertTrue(exp.eval(reader));
        exp = parser.parse("\"true\" = true AND \"true\" = false");
        assertFalse(exp.eval(reader));

        exp = parser.parse("\"true\" = false OR \"true\" = true");
        assertTrue(exp.eval(reader));
        exp = parser.parse("\"true\" = false OR \"true\" = false");
        assertFalse(exp.eval(reader));

        // a AND b OR c #=> (a AND b) OR c
        // a OR b AND c #=> a OR (b AND c)
        exp = parser.parse("\"true\" = true OR \"true\" = false AND \"true\" = false");
        assertTrue(exp.eval(reader));
        exp = parser.parse("( \"true\" = true OR \"true\" = false ) AND \"true\" = false");
        assertFalse(exp.eval(reader));
    }

    @Test
    public void testNegateOpExp()
    {
        Parser parser = new Parser(schema);
        ParserExp exp;

        exp = parser.parse("NOT \"true\" = false");
        assertTrue(exp.eval(reader));
        exp = parser.parse("NOT ( \"true\" = false )");
        assertTrue(exp.eval(reader));

        // NOT a AND B #=> (NOT a) AND B
        exp = parser.parse("NOT \"true\" = false AND \"true\" = true");
        assertTrue(exp.eval(reader));
    }
}
