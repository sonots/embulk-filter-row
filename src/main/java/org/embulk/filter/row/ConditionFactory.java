package org.embulk.filter.row;

import org.embulk.config.Task;
import org.embulk.spi.Exec;
import org.embulk.spi.Column;
import org.embulk.spi.type.Type;
import org.embulk.spi.type.BooleanType;
import org.embulk.spi.type.LongType;
import org.embulk.spi.type.DoubleType;
import org.embulk.spi.type.StringType;
import org.embulk.spi.type.TimestampType;
import org.embulk.config.ConfigException;

import org.joda.time.DateTimeZone;
import org.embulk.spi.time.Timestamp;
import org.embulk.spi.time.TimestampParser;
import org.embulk.spi.time.TimestampParseException;
import com.google.common.base.Throwables;

import org.embulk.filter.row.ConditionConfig;
import org.embulk.filter.row.Condition;
import org.embulk.filter.row.BooleanCondition;
import org.embulk.filter.row.LongCondition;
import org.embulk.filter.row.DoubleCondition;
import org.embulk.filter.row.StringCondition;
import org.embulk.filter.row.TimestampCondition;
import org.jruby.embed.ScriptingContainer;

public class ConditionFactory
{
    private final ScriptingContainer jruby;
    private Column column;
    private String columnName;
    private Type columnType;
    private ConditionConfig conditionConfig;
    private String operator;
    private boolean not;

    public ConditionFactory(ScriptingContainer jruby, Column column, ConditionConfig conditionConfig)
    {
        this.jruby           = jruby;
        this.column          = column;
        this.columnName      = column.getName();
        this.columnType      = column.getType();
        this.conditionConfig = conditionConfig;
        this.operator        = conditionConfig.getOperator().get().toUpperCase(); // default: ==
        System.out.println(conditionConfig.getNot().get().getClass());
        this.not             = conditionConfig.getNot().get().booleanValue(); // default: false
    }

    public Condition createCondition()
    {
        if (columnType instanceof BooleanType) {
            return createBooleanCondition();
        }
        else if (columnType instanceof LongType) {
            return createLongCondition();
        }
        else if (columnType instanceof DoubleType) {
            return createDoubleCondition();
        }
        else if (columnType instanceof StringType) {
            return createStringCondition();
        }
        else if (columnType instanceof TimestampType) {
            return createTimestampCondition();
        }
        assert(false);
        return null;
    }

    public BooleanCondition createBooleanCondition()
    {
        if (operator.equals("IS NULL") || operator.equals("IS NOT NULL")) {
            return new BooleanCondition(operator, null, not);
        }
        else if (!conditionConfig.getArgument().isPresent()) {
            throw new ConfigException(String.format("RowFilterPlugin: Argument is missing on column: %s", columnName));
        }
        else if (conditionConfig.getArgument().get() instanceof Boolean) {
            Boolean argument = (Boolean)conditionConfig.getArgument().get();
            return new BooleanCondition(operator, argument, not);
        }
        else {
            throw new ConfigException(String.format("RowFilterPlugin: Type mismatch on column: %s", columnName));
        }
    }

    public LongCondition createLongCondition()
    {
        if (operator.equals("IS NULL") || operator.equals("IS NOT NULL")) {
            return new LongCondition(operator, null, not);
        }
        else if (!conditionConfig.getArgument().isPresent()) {
            throw new ConfigException(String.format("RowFilterPlugin: Argument is missing on column: %s", columnName));
        }
        else if (conditionConfig.getArgument().get() instanceof Number) {
            Long argument = new Long(conditionConfig.getArgument().get().toString()).longValue();
            return new LongCondition(operator, argument, not);
        }
        else {
            throw new ConfigException(String.format("RowFilterPlugin: Type mismatch on column: %s", columnName));
        }
    }

    public DoubleCondition createDoubleCondition()
    {
        if (operator.equals("IS NULL") || operator.equals("IS NOT NULL")) {
            return new DoubleCondition(operator, null, not);
        }
        else if (!conditionConfig.getArgument().isPresent()) {
            throw new ConfigException(String.format("RowFilterPlugin: Argument is missing on column: %s", columnName));
        }
        else if (conditionConfig.getArgument().get() instanceof Number) {
            Double argument = new Double(conditionConfig.getArgument().get().toString());
            return new DoubleCondition(operator, argument, not);
        }
        else {
            throw new ConfigException(String.format("RowFilterPlugin: Type mismatch on column: %s", columnName));
        }
    }

    public StringCondition createStringCondition()
    {
        if (operator.equals("IS NULL") || operator.equals("IS NOT NULL")) {
            return new StringCondition(operator, null, not);
        }
        else if (!conditionConfig.getArgument().isPresent()) {
            throw new ConfigException(String.format("RowFilterPlugin: Argument is missing on column: %s", columnName));
        }
        else if (conditionConfig.getArgument().get() instanceof String) {
            String argument = (String)conditionConfig.getArgument().get();
            return new StringCondition(operator, argument, not);
        }
        else {
            throw new ConfigException(String.format("RowFilterPlugin: Type mismatch on column: %s", columnName));
        }
    }

    public TimestampCondition createTimestampCondition()
    {
        if (operator.equals("IS NULL") || operator.equals("IS NOT NULL")) {
            return new TimestampCondition(operator, null, not);
        }
        else if (!conditionConfig.getArgument().isPresent()) {
            throw new ConfigException(String.format("RowFilterPlugin: Argument is missing on column: %s", columnName));
        }
        else if (conditionConfig.getArgument().get() instanceof String) {
            String argument        = (String)conditionConfig.getArgument().get();
            String format          = (String)conditionConfig.getFormat().get();
            DateTimeZone timezone  = DateTimeZone.forID((String)conditionConfig.getTimezone().get());

            TimestampParser parser = new TimestampParser(jruby, format, timezone);
            try {
                Timestamp timestamp = parser.parse(argument);
                return new TimestampCondition(operator, timestamp, not);
            } catch(TimestampParseException ex) {
                throw Throwables.propagate(ex);
            }
        }
        else {
            throw new ConfigException(String.format("RowFilterPlugin: Type mismatch on column: %s", columnName));
        }
    }
}
