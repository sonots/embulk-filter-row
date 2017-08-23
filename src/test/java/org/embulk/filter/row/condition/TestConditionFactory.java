package org.embulk.filter.row.condition;

import com.google.common.base.Optional;

import org.embulk.config.ConfigException;
import org.embulk.config.TaskSource;
import org.embulk.spi.Column;

import org.junit.Test;

import static org.embulk.spi.type.Types.BOOLEAN;
import static org.embulk.spi.type.Types.DOUBLE;
import static org.embulk.spi.type.Types.LONG;
import static org.embulk.spi.type.Types.STRING;
import static org.embulk.spi.type.Types.TIMESTAMP;

//import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestConditionFactory
{
    public class DefaultConditionConfig implements ConditionConfig
    {
        public String getColumn()
        {
            return "column";
        }

        public Optional<String> getOperator()
        {
            return Optional.of("IS NULL");
        }

        public Optional<Object> getArgument()
        {
            return Optional.absent();
        }

        public Optional<Boolean> getNot()
        {
            return Optional.of(false);
        }

        public Optional<String> getFormat()
        {
            return Optional.of("%Y-%m-%d");
        }

        public Optional<String> getTimezone()
        {
            return Optional.of("UTC");
        }

        public TaskSource dump()
        {
            return null;
        }

        public void validate()
        {
        }
    }

    public TestConditionFactory()
    {
    }

    @Test
    public void testCreateBooleanCondition()
    {
        Column column = new Column(0, "column", BOOLEAN);
        ConditionConfig  config;
        BooleanCondition condition;

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator()
            {
                return Optional.of("IS NULL");
            }
        };
        condition = (BooleanCondition) new ConditionFactory(column, config).createCondition();
        assertTrue(condition.compare(null));

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator()
            {
                return Optional.of("==");
            }
            public Optional<Object> getArgument()
            {
                return Optional.absent();
            }
        };
        try {
            condition = (BooleanCondition) new ConditionFactory(column, config).createCondition();
            fail("Argument is required");
        }
        catch (ConfigException e) {
        }

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator()
            {
                return Optional.of("==");
            }
            public Optional<Object> getArgument()
            {
                return Optional.of((Object) new Boolean(true));
            }
        };
        condition = (BooleanCondition) new ConditionFactory(column, config).createCondition();
        assertTrue(condition.compare(new Boolean(true)));

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator()
            {
                return Optional.of("==");
            }
            public Optional<Object> getArgument()
            {
                return Optional.of((Object) new Long(10));
            }
        };
        try {
            condition = (BooleanCondition) new ConditionFactory(column, config).createCondition();
            fail("Argument type mismatch");
        }
        catch (ConfigException e) {
        }
    }

    @Test
    public void testCreateDoubleCondition()
    {
        Column column = new Column(0, "column", DOUBLE);
        ConditionConfig  config;
        DoubleCondition condition;

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator()
            {
                return Optional.of("IS NULL");
            }
        };
        condition = (DoubleCondition) new ConditionFactory(column, config).createCondition();
        assertTrue(condition.compare(null));

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator()
            {
                return Optional.of("==");
            }
            public Optional<Object> getArgument()
            {
                return Optional.absent();
            }
        };
        try {
            condition = (DoubleCondition) new ConditionFactory(column, config).createCondition();
            fail("Argument is required");
        }
        catch (ConfigException e) {
        }

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator()
            {
                return Optional.of("==");
            }
            public Optional<Object> getArgument()
            {
                return Optional.of((Object) new Double(10));
            }
        };
        condition = (DoubleCondition) new ConditionFactory(column, config).createCondition();
        assertTrue(condition.compare(new Double(10)));

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator()
            {
                return Optional.of("==");
            }
            public Optional<Object> getArgument()
            {
                return Optional.of((Object) new Boolean(true));
            }
        };
        try {
            condition = (DoubleCondition) new ConditionFactory(column, config).createCondition();
            fail("Argument type mismatch");
        }
        catch (ConfigException e) {
        }
    }

    @Test
    public void testCreateLongCondition()
    {
        Column column = new Column(0, "column", LONG);
        ConditionConfig  config;
        LongCondition condition;

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator()
            {
                return Optional.of("IS NULL");
            }
        };
        condition = (LongCondition) new ConditionFactory(column, config).createCondition();
        assertTrue(condition.compare(null));

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator()
            {
                return Optional.of("==");
            }
            public Optional<Object> getArgument()
            {
                return Optional.absent();
            }
        };
        try {
            condition = (LongCondition) new ConditionFactory(column, config).createCondition();
            fail("Argument is required");
        }
        catch (ConfigException e) {
        }

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator()
            {
                return Optional.of("==");
            }
            public Optional<Object> getArgument()
            {
                return Optional.of((Object) new Long(10));
            }
        };
        condition = (LongCondition) new ConditionFactory(column, config).createCondition();
        assertTrue(condition.compare(new Long(10)));

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator()
            {
                return Optional.of("==");
            }
            public Optional<Object> getArgument()
            {
                return Optional.of((Object) new Boolean(true));
            }
        };
        try {
            condition = (LongCondition) new ConditionFactory(column, config).createCondition();
            fail("Argument type mismatch");
        }
        catch (ConfigException e) {
        }
    }

    @Test
    public void testCreateStringCondition()
    {
        Column column = new Column(0, "column", STRING);
        ConditionConfig  config;
        StringCondition condition;

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator()
            {
                return Optional.of("IS NULL");
            }
        };
        condition = (StringCondition) new ConditionFactory(column, config).createCondition();
        assertTrue(condition.compare(null));

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator()
            {
                return Optional.of("==");
            }
            public Optional<Object> getArgument()
            {
                return Optional.absent();
            }
        };
        try {
            condition = (StringCondition) new ConditionFactory(column, config).createCondition();
            fail("Argument is required");
        }
        catch (ConfigException e) {
        }

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator()
            {
                return Optional.of("==");
            }
            public Optional<Object> getArgument()
            {
                return Optional.of((Object) "foo");
            }
        };
        condition = (StringCondition) new ConditionFactory(column, config).createCondition();
        assertTrue(condition.compare("foo"));

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator()
            {
                return Optional.of("==");
            }
            public Optional<Object> getArgument()
            {
                return Optional.of((Object) new Boolean(true));
            }
        };
        try {
            condition = (StringCondition) new ConditionFactory(column, config).createCondition();
            fail("Argument type mismatch");
        }
        catch (ConfigException e) {
        }
    }

    @Test
    public void testCreateTimestampCondition()
    {
        Column column = new Column(0, "column", TIMESTAMP);
        ConditionConfig  config;
        TimestampCondition condition;

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator()
            {
                return Optional.of("IS NULL");
            }
        };
        condition = (TimestampCondition) new ConditionFactory(column, config).createCondition();
        assertTrue(condition.compare(null));

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator()
            {
                return Optional.of("==");
            }
            public Optional<Object> getArgument()
            {
                return Optional.absent();
            }
        };
        try {
            condition = (TimestampCondition) new ConditionFactory(column, config).createCondition();
            fail("Argument is required");
        }
        catch (ConfigException e) {
        }

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator()
            {
                return Optional.of("==");
            }
            public Optional<Object> getArgument()
            {
                return Optional.of((Object) new Boolean(true));
            }
        };
        try {
            condition = (TimestampCondition) new ConditionFactory(column, config).createCondition();
            fail("Argument type mismatch");
        }
        catch (ConfigException e) {
        }
    }
}
