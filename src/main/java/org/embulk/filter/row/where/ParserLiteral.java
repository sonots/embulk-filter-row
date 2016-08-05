package org.embulk.filter.row.where;

import org.embulk.config.ConfigException;
import org.embulk.spi.Column;
import org.embulk.spi.PageReader;
import org.embulk.spi.Schema;
import org.embulk.spi.time.Timestamp;
import org.embulk.spi.type.JsonType;
import org.embulk.spi.type.LongType;
import org.msgpack.value.Value;

// Literal Node of AST (Abstract Syntax Tree)
public abstract class ParserLiteral extends ParserNode
{
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

    public String getString(PageReader pageReader)
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
