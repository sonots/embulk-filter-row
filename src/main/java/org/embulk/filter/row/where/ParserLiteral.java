package org.embulk.filter.row.where;

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
    static ScriptingContainer jruby;

    public static void setJRuby(ScriptingContainer jruby)
    {
        ParserLiteral.jruby = jruby;
    }

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
    public boolean val;

    public BooleanLiteral(boolean val)
    {
        this.val = val;
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

    public NumberLiteral(double val)
    {
        this.val = val;
    }

    public NumberLiteral(String str)
    {
        this.val = Double.parseDouble(str);
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

    public StringLiteral(String val)
    {
        this.val = val;
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
    private static final DateTimeZone default_timezone  = DateTimeZone.forID("UTC");

    public TimestampLiteral(ParserVal val)
    {
        if (val.obj.getClass() == StringLiteral.class) {
            initTimestampLiteral(((StringLiteral)val.obj).val);
        }
        else if (val.obj.getClass() == NumberLiteral.class) {
            initTimestampLiteral(((NumberLiteral)(val.obj)).val);
        }
        else {
            throw new RuntimeException();
        }
    }

    public void initTimestampLiteral(String str)
    {
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
                TimestampParser timestampParser = new TimestampParser(jruby, format, default_timezone);
                this.val = timestampParser.parse(str);
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

    public void initTimestampLiteral(double epoch)
    {
        int epochSecond = (int) epoch;
        long nanoAdjustment = (long) ((epoch - epochSecond) * 1000000000);
        this.val = Timestamp.ofEpochSecond(epochSecond, nanoAdjustment);
    }

    public boolean isTimestamp()
    {
        return true;
    }

    public Timestamp getTimestamp(PageReader pageReader)
    {
        return val;
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
