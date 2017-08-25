package org.embulk.filter.row.condition;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;

import org.apache.commons.lang3.math.NumberUtils;
import org.embulk.config.ConfigException;
import org.embulk.spi.Column;
import org.embulk.spi.time.Timestamp;
import org.embulk.spi.time.TimestampParseException;
import org.embulk.spi.time.TimestampParser;
import org.embulk.spi.type.BooleanType;
import org.embulk.spi.type.DoubleType;
import org.embulk.spi.type.LongType;
import org.embulk.spi.type.StringType;
import org.embulk.spi.type.TimestampType;
import org.embulk.spi.type.Type;
import org.joda.time.DateTimeZone;
import org.jruby.embed.ScriptingContainer;

public class ConditionFactory
{
    private Column column;
    private String columnName;
    private Type columnType;
    private ConditionConfig conditionConfig;
    private String operator;
    private boolean not;

    public ConditionFactory(Column column, ConditionConfig conditionConfig)
    {
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
        assert false;
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
            Boolean argument = (Boolean) conditionConfig.getArgument().get();
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
        else if (NumberUtils.isNumber(conditionConfig.getArgument().get().toString())) {
            Double argument = new Double(conditionConfig.getArgument().get().toString());
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
        else if (NumberUtils.isNumber(conditionConfig.getArgument().get().toString())) {
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
            String argument = (String) conditionConfig.getArgument().get();
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
            String argument        = (String) conditionConfig.getArgument().get();
            String format          = (String) conditionConfig.getFormat().get();
            DateTimeZone timezone  = DateTimeZone.forID((String) conditionConfig.getTimezone().get());

            TimestampParser parser = getTimestampParser(format, timezone);
            try {
                Timestamp timestamp = parser.parse(argument);
                return new TimestampCondition(operator, timestamp, not);
            }
            catch (TimestampParseException ex) {
                throw Throwables.propagate(ex);
            }
        }
        else {
            throw new ConfigException(String.format("RowFilterPlugin: Type mismatch on column: %s", columnName));
        }
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

    private TimestampParser getTimestampParser(String format, DateTimeZone timezone)
    {
        // ToDo: Use following codes after deciding to drop supporting embulk < 0.8.29.
        //
        //     return new TimestampParser(format, timezone);
        String date = "1970-01-01";
        TimestampParserTaskImpl task = new TimestampParserTaskImpl(timezone, format, date);
        TimestampParserColumnOptionImpl columnOption = new TimestampParserColumnOptionImpl(
                Optional.of(timezone), Optional.of(format), Optional.of(date));
        return new TimestampParser(task, columnOption);
    }
}
