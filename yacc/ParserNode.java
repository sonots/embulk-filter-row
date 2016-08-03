//package org.embulk.filter.row;
import java.util.HashMap;

abstract class ParserNode
{
}

abstract class ParserLiteral extends ParserNode
{
    public Object value(HashMap<String, Object> variables)
    {
        throw new RuntimeException();
    }
    public boolean booleanValue(HashMap<String, Object> variables)
    {
        throw new RuntimeException();
    }
    public double doubleValue(HashMap<String, Object> variables)
    {
        throw new RuntimeException();
    }
    public String stringValue(HashMap<String, Object> variables)
    {
        throw new RuntimeException();
    }
}

abstract class ParserExp extends ParserNode
{
    abstract boolean eval(HashMap<String, Object> variables);
}

abstract class BinaryOpExp extends ParserExp
{
    ParserLiteral left;
    ParserLiteral right;
    int operator;

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

    public boolean eval(HashMap<String, Object> variables)
    {
        boolean l = left.booleanValue(variables);
        boolean r = right.booleanValue(variables);
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

    public boolean eval(HashMap<String, Object> variables)
    {
        double l = left.doubleValue(variables);
        double r = right.doubleValue(variables);
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

    public boolean eval(HashMap<String, Object> variables)
    {
        String l = left.stringValue(variables);
        String r = right.stringValue(variables);
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
    ParserLiteral val;
    int operator;

    public NullOpExp(ParserLiteral val, int operator)
    {
        this.val = val;
        this.operator = operator;
    }

    public NullOpExp(ParserVal val, int operator)
    {
        this((ParserLiteral)(val.obj), operator);
    }

    public boolean eval(HashMap<String, Object> variables)
    {
        Object v = val.value(variables);
        if (operator == Parser.EQ) {
            return v == null;
        }
        else if (operator == Parser.NEQ) {
            return v != null;
        }
        else {
            assert(false);
            return false;
        }
    }
}

class LogicalOpExp extends ParserExp
{
    ParserExp left;
    ParserExp right;
    int operator;

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

    public boolean eval(HashMap<String, Object> variables)
    {
        boolean l = left.eval(variables);
        boolean r = right.eval(variables);
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

class BooleanLiteral extends ParserLiteral
{
    public boolean val;

    public BooleanLiteral(boolean val)
    {
        this.val = val;
    }

    public boolean booleanValue(HashMap<String, Object> variables)
    {
        return val;
    }
}

class NumberLiteral extends ParserLiteral
{
    public double val;

    public NumberLiteral(double val)
    {
        this.val = val;
    }

    public double doubleValue(HashMap<String, Object> variables)
    {
        return val;
    }
}

class StringLiteral extends ParserLiteral
{
    public String val;

    public StringLiteral(String val)
    {
        this.val = val;
    }

    public String stringValue(HashMap<String, Object> variables)
    {
        return val;
    }
}

class IdentifierLiteral extends ParserLiteral
{
    public String name;

    public IdentifierLiteral(String name)
    {
        this.name = name;
    }

    public Object value(HashMap<String, Object> variables)
    {
        return variables.get(name);
    }

    public boolean booleanValue(HashMap<String, Object> variables)
    {
        return ((Boolean)variables.get(name)).booleanValue();
    }

    public double doubleValue(HashMap<String, Object> variables)
    {
        return ((Double)variables.get(name)).doubleValue();
    }

    public String stringValue(HashMap<String, Object> variables)
    {
        return (String)variables.get(name);
    }
}
