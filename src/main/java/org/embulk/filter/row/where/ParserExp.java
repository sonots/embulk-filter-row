package org.embulk.filter.row.where;

import org.embulk.config.ConfigException;
import org.embulk.spi.PageReader;
import org.embulk.spi.time.Timestamp;

import org.jcodings.specific.UTF8Encoding;
import org.joni.Matcher;
import org.joni.Option;
import org.joni.Regex;

// Operation Node of AST (Abstract Syntax Tree)
public abstract class ParserExp extends ParserNode
{
    public abstract boolean eval(PageReader pageReader);
}

abstract class BinaryOpExp extends ParserExp
{
    ParserLiteral left;
    ParserLiteral right;
    int operator;

    public BinaryOpExp() {}

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

    static BinaryOpExp create(ParserLiteral left, ParserLiteral right, int operator)
    {
        if (left.isTimestamp() || right.isTimestamp()) {
            // Either of left or right would be a string or a number
            return new TimestampOpExp(left, right, operator);
        }
        else if (left.isString() || right.isString()) {
            return new StringOpExp(left, right, operator);
        }
        else if (left.isNumber() || right.isNumber()) {
            return new NumberOpExp(left, right, operator);
        }
        else if (left.isBoolean() || right.isBoolean()) {
            return new BooleanOpExp(left, right, operator);
        }
        else {
            throw new RuntimeException();
        }
    }

    static BinaryOpExp create(ParserVal left, ParserVal right, int operator)
    {
        return BinaryOpExp.create(((ParserLiteral)left.obj), ((ParserLiteral)right.obj), operator);
    }

    static boolean isOperatorAllowed(int[] operators, int operator)
    {
        for (int o : operators) {
            if (operator == o) {
                return true;
            }
        }
        return false;
    }
}

class BooleanOpExp extends BinaryOpExp
{
    public static int[] operators = {Parser.EQ, Parser.NEQ, Parser.GT, Parser.GE, Parser.LT, Parser.LE};

    public BooleanOpExp(ParserLiteral left, ParserLiteral right, int operator)
    {
        super(left, right, operator);
        if (! left.isBoolean()) {
            throw new ConfigException(String.format("\"%s\" is not a Boolean column", ((IdentifierLiteral)left).name));
        }
        if (! right.isBoolean()) {
            throw new ConfigException(String.format("\"%s\" is not a Boolean column", ((IdentifierLiteral)right).name));
        }
        if (! isOperatorAllowed(operators, operator)) {
            throw new ConfigException(String.format("\"%s\" is not an allowed operator for BooleanOpExp", Parser.yyname[operator]));
        }
    }

    public BooleanOpExp(ParserVal left, ParserVal right, int operator)
    {
        this((ParserLiteral)(left.obj), (ParserLiteral)(right.obj), operator);
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
    public static int[] operators = {Parser.EQ, Parser.NEQ, Parser.GT, Parser.GE, Parser.LT, Parser.LE};

    public NumberOpExp(ParserLiteral left, ParserLiteral right, int operator)
    {
        super(left, right, operator);
        if (! left.isNumber()) {
            throw new ConfigException(String.format("\"%s\" is not a Number column", ((IdentifierLiteral)left).name));
        }
        if (! right.isNumber()) {
            throw new ConfigException(String.format("\"%s\" is not a Number column", ((IdentifierLiteral)right).name));
        }
        if (! isOperatorAllowed(operators, operator)) {
            throw new ConfigException(String.format("\"%s\" is not an allowed operator for NumberOpExp", Parser.yyname[operator]));
        }
    }

    public NumberOpExp(ParserVal left, ParserVal right, int operator)
    {
        this((ParserLiteral)(left.obj), (ParserLiteral)(right.obj), operator);
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

class TimestampOpExp extends BinaryOpExp
{
    public static int[] operators = {Parser.EQ, Parser.NEQ, Parser.GT, Parser.GE, Parser.LT, Parser.LE};

    public TimestampOpExp(ParserLiteral left, ParserLiteral right, int operator)
    {
        this.left = left.isIdentifier() ? left : new TimestampLiteral(left);
        this.right = right.isIdentifier() ? right : new TimestampLiteral(right);
        this.operator = operator;

        if (! this.left.isTimestamp()) {
            throw new ConfigException(String.format("\"%s\" is not a Timestamp column", ((IdentifierLiteral)this.left).name));
        }
        if (! this.right.isTimestamp()) {
            throw new ConfigException(String.format("\"%s\" is not a Timestamp column", ((IdentifierLiteral)this.right).name));
        }
        if (! isOperatorAllowed(operators, operator)) {
            throw new ConfigException(String.format("\"%s\" is not an allowed operator for TimestampOpExp", Parser.yyname[operator]));
        }
    }

    public TimestampOpExp(ParserVal left, ParserVal right, int operator)
    {
        this((ParserLiteral)(left.obj), (ParserLiteral)(right.obj), operator);
    }

    public boolean eval(PageReader pageReader)
    {
        Timestamp l = left.getTimestamp(pageReader);
        Timestamp r = right.getTimestamp(pageReader);
        if (operator == Parser.EQ) {
            return l.equals(r);
        }
        else if (operator == Parser.NEQ) {
            return ! l.equals(r);
        }
        else if (operator == Parser.GT) {
            return l.compareTo(r) > 0;
        }
        else if (operator == Parser.GE) {
            return l.compareTo(r) >= 0;
        }
        else if (operator == Parser.LT) {
            return l.compareTo(r) < 0;
        }
        else if (operator == Parser.LE) {
            return l.compareTo(r) <= 0;
        }
        else {
            assert(false);
            return false;
        }
    }
}

class StringOpExp extends BinaryOpExp
{
    public static int[] operators = {
            Parser.EQ, Parser.NEQ, Parser.GT, Parser.GE, Parser.LT, Parser.LE,
            Parser.START_WITH, Parser.END_WITH, Parser.INCLUDE, Parser.REGEXP
    };

    public StringOpExp(ParserLiteral left, ParserLiteral right, int operator)
    {
        super(left, right, operator);
        if (! left.isString()) {
            throw new ConfigException(String.format("\"%s\" is not a String column", ((IdentifierLiteral)left).name));
        }
        if (! right.isString()) {
            throw new ConfigException(String.format("\"%s\" is not a String column", ((IdentifierLiteral)right).name));
        }
        if (! isOperatorAllowed(operators, operator)) {
            throw new ConfigException(String.format("\"%s\" is not an allowed operator for StringOpExp", Parser.yyname[operator]));
        }
    }

    public StringOpExp(ParserVal left, ParserVal right, int operator)
    {
        this((ParserLiteral)(left.obj), (ParserLiteral)(right.obj), operator);
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
        else if (operator == Parser.GT) {
            return l.compareTo(r) > 0;
        }
        else if (operator == Parser.GE) {
            return l.compareTo(r) >= 0;
        }
        else if (operator == Parser.LT) {
            return l.compareTo(r) < 0;
        }
        else if (operator == Parser.LE) {
            return l.compareTo(r) <= 0;
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

class RegexpOpExp extends BinaryOpExp
{
    Regex regex;

    public RegexpOpExp(ParserLiteral left, ParserLiteral right, int operator)
    {
        super(left, right, operator);

        byte[] pattern = (((StringLiteral)right).val).getBytes();
        this.regex = new Regex(pattern, 0, pattern.length, Option.NONE, UTF8Encoding.INSTANCE);

        if (! left.isString()) {
            throw new ConfigException(String.format("\"%s\" is not a String column", ((IdentifierLiteral)left).name));
        }
    }

    public RegexpOpExp(ParserVal left, ParserVal right, int operator)
    {
        this((ParserLiteral)(left.obj), (ParserLiteral)(right.obj), operator);
    }

    public boolean eval(PageReader pageReader)
    {
        byte[] l = left.getString(pageReader).getBytes();
        Matcher matcher = regex.matcher(l);
        int result = matcher.search(0, l.length, Option.DEFAULT);
        return result != -1;
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
