package org.embulk.filter.row;

import org.slf4j.Logger;

import org.embulk.spi.Exec;
import org.embulk.spi.Column;
import org.embulk.spi.type.Type;
import org.embulk.spi.type.BooleanType;
import org.embulk.spi.type.LongType;
import org.embulk.spi.type.DoubleType;
import org.embulk.spi.type.StringType;
import org.embulk.spi.type.TimestampType;
import org.embulk.spi.time.Timestamp;

import org.embulk.filter.row.ConditionConfig;
import org.embulk.filter.row.Condition;
import org.embulk.filter.row.BooleanCondition;
import org.embulk.filter.row.LongCondition;
import org.embulk.filter.row.DoubleCondition;
import org.embulk.filter.row.StringCondition;
import org.embulk.filter.row.TimestampCondition;
import org.slf4j.Logger;

public class ConditionFactory
{
    private Column column;
    private String columnName;
    private Type columnType;
    private ConditionConfig conditionConfig;
    private String operator;
    private boolean not;
    private final Logger log;

    public ConditionFactory(Column column, ConditionConfig conditionConfig)
    {
        this.log             = Exec.getLogger(ConditionFactory.class);
        this.column          = column;
        this.columnName      = column.getName();
        this.columnType      = column.getType();
        this.conditionConfig = conditionConfig;
        this.operator        = conditionConfig.getOperator().get().toUpperCase(); // default: ==
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
            log.warn(String.format("RowFilterPlugin: Argument is missing on column: %s", columnName));
            // how to exit embulk without dirty stack trace??
            System.exit(1);
        }
        else if (conditionConfig.getArgument().get() instanceof Boolean) {
            Boolean argument = (Boolean)conditionConfig.getArgument().get();
            return new BooleanCondition(operator, argument, not);
        }
        else {
            log.warn(String.format("RowFilterPlugin: Type mismatch on column: %s", columnName));
            // how to exit embulk without dirty stack trace??
            System.exit(1);
        }
        return null;
    }

    public LongCondition createLongCondition()
    {
        if (operator.equals("IS NULL") || operator.equals("IS NOT NULL")) {
            return new LongCondition(operator, null, not);
        }
        else if (!conditionConfig.getArgument().isPresent()) {
            log.warn(String.format("RowFilterPlugin: Argument is missing on column: %s", columnName));
            System.exit(1);
        }
        else if (conditionConfig.getArgument().get() instanceof Integer) {
            Long argument = new Long(((Integer)conditionConfig.getArgument().get()).longValue());
            return new LongCondition(operator, argument, not);
        }
        else {
            log.warn(String.format("RowFilterPlugin: Type mismatch on column: %s", columnName));
            System.exit(1);
        }
        return null;
    }

    public DoubleCondition createDoubleCondition()
    {
        if (operator.equals("IS NULL") || operator.equals("IS NOT NULL")) {
            return new DoubleCondition(operator, null, not);
        }
        else if (!conditionConfig.getArgument().isPresent()) {
            log.warn(String.format("RowFilterPlugin: Argument is missing on column: %s", columnName));
            System.exit(1);
        }
        else if (conditionConfig.getArgument().get() instanceof Number) {
            Double argument = new Double(conditionConfig.getArgument().get().toString());
            return new DoubleCondition(operator, argument, not);
        }
        else {
            log.warn(String.format("RowFilterPlugin: Type mismatch on column: %s", columnName));
            System.exit(1);
        }
        return null;
    }

    public StringCondition createStringCondition()
    {
        if (operator.equals("IS NULL") || operator.equals("IS NOT NULL")) {
            return new StringCondition(operator, null, not);
        }
        else if (!conditionConfig.getArgument().isPresent()) {
            log.warn(String.format("RowFilterPlugin: Argument is missing on column: %s", columnName));
            System.exit(1);
        }
        else if (conditionConfig.getArgument().get() instanceof String) {
            String argument = (String)conditionConfig.getArgument().get();
            return new StringCondition(operator, argument, not);
        }
        else {
            log.warn(String.format("RowFilterPlugin: Type mismatch on column: %s", columnName));
            System.exit(1);
        }
        return null;
    }

    public TimestampCondition createTimestampCondition()
    {
        if (operator.equals("IS NULL") || operator.equals("IS NOT NULL")) {
            return new TimestampCondition(operator, null, not);
        }
        else if (!conditionConfig.getArgument().isPresent()) {
            log.warn(String.format("RowFilterPlugin: Argument is missing on column: %s", columnName));
            System.exit(1);
        }
        else if (conditionConfig.getArgument().get() instanceof Timestamp) {
            Timestamp argument = (Timestamp)conditionConfig.getArgument().get();
            return new TimestampCondition(operator, argument, not);
        }
        else {
            log.warn(String.format("RowFilterPlugin: Type mismatch on column: %s", columnName));
            System.exit(1);
        }
        return null;
    }
}
