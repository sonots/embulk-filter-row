package org.embulk.filter.row.where;

import org.embulk.spi.PageReader;

// Operation Node of AST (Abstract Syntax Tree)
public abstract class ParserExp extends ParserNode
{
    public abstract boolean eval(PageReader pageReader);
}

abstract class BinaryOpExp extends ParserExp
{
    protected ParserLiteral left;
    protected ParserLiteral right;
    protected int operator;

    public BinaryOpExp(ParserLiteral left, ParserLiteral right, int operator)
    {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public BinaryOpExp(ParserVal left, ParserVal right, int operator)
    {
        this((ParserLiteral)(left.obj), (ParserLiteral)(right.obj), operator);
    }
}

class BooleanOpExp extends BinaryOpExp
{
    public BooleanOpExp(ParserLiteral left, ParserLiteral right, int operator)
    {
        super(left, right, operator);
    }

    public BooleanOpExp(ParserVal left, ParserVal right, int operator)
    {
        super(left, right, operator);
    }

    public boolean eval(PageReader pageReader)
    {
        boolean l = left.getBoolean(pageReader);
        boolean r = right.getBoolean(pageReader);
        if (operator == Parser.EQ) {
            return l == r;
        }
        else if (operator == Parser.NEQ) {
            return l != r;
        }
        else {
            assert(false);
            return false;
        }
    }
}

class NumberOpExp extends BinaryOpExp
{
    public NumberOpExp(ParserLiteral left, ParserLiteral right, int operator)
    {
        super(left, right, operator);
    }

    public NumberOpExp(ParserVal left, ParserVal right, int operator)
    {
        super(left, right, operator);
    }

    public boolean eval(PageReader pageReader)
    {
        double l = left.getNumber(pageReader);
        double r = right.getNumber(pageReader);
        if (operator == Parser.EQ) {
            return l == r;
        }
        else if (operator == Parser.NEQ) {
            return l != r;
        }
        else if (operator == Parser.GT) {
            return l > r;
        }
        else if (operator == Parser.GE) {
            return l >= r;
        }
        else if (operator == Parser.LT) {
            return l < r;
        }
        else if (operator == Parser.LE) {
            return l <= r;
        }
        else {
            assert(false);
            return false;
        }
    }
}

class StringOpExp extends BinaryOpExp
{
    public StringOpExp(ParserLiteral left, ParserLiteral right, int operator)
    {
        super(left, right, operator);
    }

    public StringOpExp(ParserVal left, ParserVal right, int operator)
    {
        super(left, right, operator);
    }

    public boolean eval(PageReader pageReader)
    {
        String l = left.getString(pageReader);
        String r = right.getString(pageReader);
        if (operator == Parser.EQ) {
            return l.equals(r);
        }
        else if (operator == Parser.NEQ) {
            return ! l.equals(r);
        }
        else if (operator == Parser.START_WITH) {
            return l.startsWith(r);
        }
        else if (operator == Parser.END_WITH) {
            return l.endsWith(r);
        }
        else if (operator == Parser.INCLUDE) {
            return l.contains(r);
        }
        else {
            assert(false);
            return false;
        }
    }
}

class NullOpExp extends ParserExp
{
    protected ParserLiteral val;
    protected int operator;

    public NullOpExp(ParserLiteral val, int operator)
    {
        this.val = val;
        this.operator = operator;
    }

    public NullOpExp(ParserVal val, int operator)
    {
        this((ParserLiteral)(val.obj), operator);
    }

    public boolean eval(PageReader pageReader)
    {
        boolean isNull = val.isNull(pageReader);
        if (operator == Parser.EQ) {
            return isNull;
        }
        else if (operator == Parser.NEQ) {
            return ! isNull;
        }
        else {
            assert(false);
            return false;
        }
    }
}

class LogicalOpExp extends ParserExp
{
    protected ParserExp left;
    protected ParserExp right;
    protected int operator;

    public LogicalOpExp(ParserExp left, ParserExp right, int operator)
    {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public LogicalOpExp(ParserVal left, ParserVal right, int operator)
    {
        this((ParserExp)(left.obj), (ParserExp)(right.obj), operator);
    }

    public boolean eval(PageReader pageReader)
    {
        boolean l = left.eval(pageReader);
        boolean r = right.eval(pageReader);
        if (operator == Parser.OR) {
            return l || r;
        }
        else if (operator == Parser.AND) {
            return l && r;
        }
        else {
            assert(false);
            return false;
        }
    }
}

class NegateOpExp extends ParserExp
{
    protected ParserExp exp;

    public NegateOpExp(ParserExp exp)
    {
        this.exp = exp;
    }

    public NegateOpExp(ParserVal exp)
    {
        this((ParserExp)(exp.obj));
    }

    public boolean eval(PageReader pageReader)
    {
        return ! exp.eval(pageReader);
    }
}
