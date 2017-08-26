package org.embulk.filter.row.where;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import org.embulk.config.ConfigException;
import org.embulk.spi.Column;
import org.embulk.spi.PageReader;
import org.embulk.spi.Schema;
import org.embulk.spi.time.Timestamp;
import org.embulk.spi.time.TimestampParseException;
import org.embulk.spi.time.TimestampParser;
import org.embulk.spi.type.BooleanType;
import org.embulk.spi.type.DoubleType;
import org.embulk.spi.type.JsonType;
import org.embulk.spi.type.LongType;
import org.embulk.spi.type.StringType;
import org.embulk.spi.type.TimestampType;
import org.embulk.spi.type.Type;
import org.joda.time.DateTimeZone;
import org.jruby.embed.ScriptingContainer;
import org.msgpack.value.Value;

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
    public Timestamp getTimestamp(PageReader pageReader)
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
    protected Timestamp val;
    private static final DateTimeZone defaultTimeZone  = DateTimeZone.forID("UTC");

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
        Timestamp val = null;
        TimestampParseException ex = null;
        for (String format : formats) {
            try {
                TimestampParser timestampParser = createTimestampParser(format, defaultTimeZone);
                this.val = timestampParser.parse(literal.val);
                break;
            }
            catch (TimestampParseException e) {
                ex = e;
            }
        }
        if (this.val == null) {
            throw Throwables.propagate(ex);
        }
    }

    void initTimestampLiteral(NumberLiteral literal)
    {
        this.yytext = literal.yytext;
        double epoch = literal.val;
        int epochSecond = (int) epoch;
        long nanoAdjustment = (long) ((epoch - epochSecond) * 1000000000);
        this.val = Timestamp.ofEpochSecond(epochSecond, nanoAdjustment);
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

    public Timestamp getTimestamp(PageReader pageReader)
    {
        return val;
    }

    private class TimestampParserTaskImpl implements TimestampParser.Task
    {
        private final DateTimeZone defaultTimeZone;
        private final String defaultTimestampFormat;
        private final String defaultDate;
        public TimestampParserTaskImpl(
                DateTimeZone defaultTimeZone,
                String defaultTimestampFormat,
                String defaultDate)
        {
            this.defaultTimeZone = defaultTimeZone;
            this.defaultTimestampFormat = defaultTimestampFormat;
            this.defaultDate = defaultDate;
        }
        @Override
        public DateTimeZone getDefaultTimeZone()
        {
            return this.defaultTimeZone;
        }
        @Override
        public String getDefaultTimestampFormat()
        {
            return this.defaultTimestampFormat;
        }
        @Override
        public String getDefaultDate()
        {
            return this.defaultDate;
        }
        @Override
        public ScriptingContainer getJRuby()
        {
            return null;
        }
    }

    private class TimestampParserColumnOptionImpl implements TimestampParser.TimestampColumnOption
    {
        private final Optional<DateTimeZone> timeZone;
        private final Optional<String> format;
        private final Optional<String> date;
        public TimestampParserColumnOptionImpl(
                Optional<DateTimeZone> timeZone,
                Optional<String> format,
                Optional<String> date)
        {
            this.timeZone = timeZone;
            this.format = format;
            this.date = date;
        }
        @Override
        public Optional<DateTimeZone> getTimeZone()
        {
            return this.timeZone;
        }
        @Override
        public Optional<String> getFormat()
        {
            return this.format;
        }
        @Override
        public Optional<String> getDate()
        {
            return this.date;
        }
    }

    // ToDo: Replace with `new TimestampParser(format, timezone)`
    // after deciding to drop supporting embulk < 0.8.29.
    private TimestampParser createTimestampParser(String format, DateTimeZone timezone)
    {
        return createTimestampParser(format, timezone, "1970-01-01");
    }

    // ToDo: Replace with `new TimestampParser(format, timezone, date)`
    // after deciding to drop supporting embulk < 0.8.29.
    private TimestampParser createTimestampParser(String format, DateTimeZone timezone, String date)
    {
        TimestampParserTaskImpl task = new TimestampParserTaskImpl(timezone, format, date);
        TimestampParserColumnOptionImpl columnOption = new TimestampParserColumnOptionImpl(
                Optional.of(timezone), Optional.of(format), Optional.of(date));
        return new TimestampParser(task, columnOption);
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

    public Timestamp getTimestamp(PageReader pageReader)
    {
        return pageReader.getTimestamp(column);
    }

    public Value getJson(PageReader pageReader)
    {
        return pageReader.getJson(column);
    }
}
