package org.embulk.filter.row.where;

import org.embulk.config.ConfigException;
import org.embulk.spi.Column;
import org.embulk.spi.PageReader;
import org.embulk.spi.Schema;
import org.embulk.spi.type.BooleanType;
import org.embulk.spi.type.DoubleType;
import org.embulk.spi.type.JsonType;
import org.embulk.spi.type.LongType;
import org.embulk.spi.type.StringType;
import org.embulk.spi.type.TimestampType;
import org.embulk.util.timestamp.TimestampFormatter;
import org.msgpack.value.Value;

import java.time.Instant;
import java.time.format.DateTimeParseException;

// Literal Node of AST (Abstract Syntax Tree)
public abstract class ParserLiteral extends ParserNode
{
    protected String yytext;

    public boolean isBoolean()
    {
        return false;
    }
    public boolean isNumber()
    {
        return false;
    }
    public boolean isString()
    {
        return false;
    }
    public boolean isTimestamp()
    {
        return false;
    }
    public boolean isJson()
    {
        return false;
    }
    public boolean isIdentifier()
    {
        return false;
    }

    public boolean isNull(PageReader pageReader)
    {
        throw new RuntimeException();
    }
    public boolean getBoolean(PageReader pageReader)
    {
        throw new RuntimeException();
    }
    public double getNumber(PageReader pageReader)
    {
        throw new RuntimeException();
    }
    public String getString(PageReader pageReader)
    {
        throw new RuntimeException();
    }
    public Instant getTimestamp(PageReader pageReader)
    {
        throw new RuntimeException();
    }
    public Value getJson(PageReader pageReader)
    {
        throw new RuntimeException();
    }

}

class BooleanLiteral extends ParserLiteral
{
    protected boolean val;

    public BooleanLiteral(String yytext)
    {
        this.yytext = yytext;
        this.val = Boolean.parseBoolean(yytext); // but, only true|TRUE|false|FALSE are allowed in lexer
    }

    public boolean isBoolean()
    {
        return true;
    }

    public boolean getBoolean(PageReader pageReader)
    {
        return val;
    }
}

class NumberLiteral extends ParserLiteral
{
    protected double val;

    public NumberLiteral(String yytext)
    {
        this.yytext = yytext;
        this.val = Double.parseDouble(yytext);
    }

    public boolean isNumber()
    {
        return true;
    }

    public double getNumber(PageReader pageReader)
    {
        return val;
    }
}

class StringLiteral extends ParserLiteral
{
    protected String val;

    public StringLiteral(String yytext)
    {
        this.yytext = yytext;
        this.val = yytext;
    }

    public boolean isString()
    {
        return true;
    }

    public String getString(PageReader pageReader)
    {
        return val;
    }
}

class TimestampLiteral extends ParserLiteral
{
    protected Instant val;
    private static final String defaultTimeZone = "UTC";

    public TimestampLiteral(ParserLiteral literal)
    {
        if (literal.getClass() == StringLiteral.class) {
            initTimestampLiteral((StringLiteral)literal);
        }
        else if (literal.getClass() == NumberLiteral.class) {
            initTimestampLiteral((NumberLiteral)(literal));
        }
        else if (literal.getClass() == TimestampLiteral.class) {
            initTimestampLiteral((TimestampLiteral)literal);
        }
        else {
            throw new ConfigException(String.format("\"%s\" is not a Timestamp literal", literal.yytext));
        }
    }

    public TimestampLiteral(ParserVal val)
    {
        this((ParserLiteral)(val.obj));
    }

    void initTimestampLiteral(StringLiteral literal)
    {
        this.yytext = literal.yytext;
        String[] formats = {
                "%Y-%m-%d %H:%M:%S.%N %z",
                "%Y-%m-%d %H:%M:%S.%N",
                "%Y-%m-%d %H:%M:%S %z",
                "%Y-%m-%d %H:%M:%S",
                "%Y-%m-%d %z",
                "%Y-%m-%d",
                };
        Instant val = null;
        DateTimeParseException ex = null;
        for (String format : formats) {
            try {
                TimestampFormatter timestampParser = createTimestampParser(format, defaultTimeZone);
                this.val = timestampParser.parse(literal.val);
                break;
            }
            catch (DateTimeParseException e) {
                ex = e;
            }
        }
        if (this.val == null) {
            if ( ex instanceof RuntimeException ) {
                throw (RuntimeException)ex;
            }
            throw new RuntimeException(ex);
        }
    }

    void initTimestampLiteral(NumberLiteral literal)
    {
        this.yytext = literal.yytext;
        double epoch = literal.val;
        int epochSecond = (int) epoch;
        long nanoAdjustment = (long) ((epoch - epochSecond) * 1000000000);
        this.val = Instant.ofEpochSecond(epochSecond, nanoAdjustment);
    }

    void initTimestampLiteral(TimestampLiteral literal)
    {
        this.yytext = literal.yytext;
        this.val = literal.val;
    }

    public boolean isTimestamp()
    {
        return true;
    }

    public Instant getTimestamp(PageReader pageReader)
    {
        return val;
    }

    private TimestampFormatter createTimestampParser(String format, String timezone)
    {
        TimestampFormatter parser;

        return TimestampFormatter.builder(format,true)
                .setDefaultZoneFromString("UTC")
                .setDefaultDateFromString("1970-01-01").build();
    }
}

class IdentifierLiteral extends ParserLiteral
{
    protected String name;
    protected Column column;

    public IdentifierLiteral(String name, Schema schema)
    {
        this.name = name;
        this.column = schema.lookupColumn(name); // throw SchemaConfigException
        // ToDo: Support filtering value with type: json
        if (column.getType() instanceof JsonType) {
            throw new ConfigException(String.format("Identifier for a json column '%s' is not supported", name));
        }
    }

    public boolean isBoolean()
    {
        return (column.getType() instanceof BooleanType);
    }
    public boolean isNumber()
    {
        return (column.getType() instanceof LongType) || (column.getType() instanceof DoubleType);
    }
    public boolean isString()
    {
        return (column.getType() instanceof StringType);
    }
    public boolean isTimestamp()
    {
        return (column.getType() instanceof TimestampType);
    }
    public boolean isJson()
    {
        return (column.getType() instanceof JsonType);
    }
    public boolean isIdentifier()
    {
        return true;
    }

    public boolean isNull(PageReader pageReader)
    {
        return pageReader.isNull(column);
    }

    public boolean getBoolean(PageReader pageReader)
    {
        return pageReader.getBoolean(column);
    }

    public double getNumber(PageReader pageReader)
    {
        if (column.getType() instanceof LongType) {
            return (double) pageReader.getLong(column);
        }
        else {
            return pageReader.getDouble(column);
        }
    }

    public String getString(PageReader pageReader)
    {
        return pageReader.getString(column);
    }

    public Instant getTimestamp(PageReader pageReader)
    {
        // TODO: use getTimstampInstant after dropping v0.9
        return pageReader.getTimestamp(column).getInstant();
    }

    public Value getJson(PageReader pageReader)
    {
        return pageReader.getJson(column);
    }
}
