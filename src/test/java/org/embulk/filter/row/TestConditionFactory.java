package org.embulk.filter.row;

import org.embulk.spi.Column;
import org.embulk.spi.type.Type;
import static org.embulk.spi.type.Types.*;
import org.embulk.spi.type.BooleanType;
import org.embulk.spi.type.LongType;
import org.embulk.spi.type.DoubleType;
import org.embulk.spi.type.StringType;
import org.embulk.spi.type.TimestampType;
import org.embulk.spi.time.Timestamp;
import org.embulk.config.ConfigException;
import org.embulk.config.TaskSource;

import org.embulk.filter.row.ConditionConfig;
import org.embulk.filter.row.ConditionFactory;
import org.embulk.filter.row.BooleanCondition;
import org.embulk.filter.row.DoubleCondition;
import org.embulk.filter.row.Condition;
import org.embulk.filter.row.StringCondition;
import org.embulk.filter.row.TimestampCondition;

import com.google.common.base.Optional;
import org.jruby.embed.ScriptingContainer;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.NullPointerException;

public class TestConditionFactory
{
    public class DefaultConditionConfig implements ConditionConfig
    {
        public String getColumn()             { return "column"; }
        public Optional<String> getOperator() { return Optional.of("IS NULL"); }
        public Optional<Object> getArgument() { return Optional.absent(); }
        public Optional<Boolean> getNot()     { return Optional.of(false); }
        public Optional<String> getFormat()   { return Optional.of("%Y-%m-%d"); }
        public Optional<String> getTimezone() { return Optional.of("UTC"); }
        public TaskSource dump()              { return null; }
        public void validate()                {}
    }

    private final ScriptingContainer jruby;

    public TestConditionFactory()
    {
        jruby = new ScriptingContainer();
    }

    @Test
    public void testCreateBooleanCondition() {
        Column column = new Column(0, "column", BOOLEAN);
        ConditionConfig  config;
        BooleanCondition condition;

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator() { return Optional.of("IS NULL"); }
        };
        condition = (BooleanCondition)new ConditionFactory(jruby, column, config).createCondition();
        assertTrue(condition.compare(null));

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator() { return Optional.of("=="); }
            public Optional<Object> getArgument() { return Optional.absent(); }
        };
        try {
            condition = (BooleanCondition)new ConditionFactory(jruby, column, config).createCondition();
            fail("Argument is required");
        } catch (ConfigException e) {
        }

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator() { return Optional.of("=="); }
            public Optional<Object> getArgument() { return Optional.of(new Boolean(true)); }
        };
        condition = (BooleanCondition)new ConditionFactory(jruby, column, config).createCondition();
        assertTrue(condition.compare(new Boolean(true)));

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator() { return Optional.of("=="); }
            public Optional<Object> getArgument() { return Optional.of(new Long(10)); }
        };
        try {
            condition = (BooleanCondition)new ConditionFactory(jruby, column, config).createCondition();
            fail("Argument type mismatch");
        } catch (ConfigException e) {
        }
    }

    @Test
    public void testCreateDoubleCondition() {
        Column column = new Column(0, "column", DOUBLE);
        ConditionConfig  config;
        DoubleCondition condition;

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator() { return Optional.of("IS NULL"); }
        };
        condition = (DoubleCondition)new ConditionFactory(jruby, column, config).createCondition();
        assertTrue(condition.compare(null));

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator() { return Optional.of("=="); }
            public Optional<Object> getArgument() { return Optional.absent(); }
        };
        try {
            condition = (DoubleCondition)new ConditionFactory(jruby, column, config).createCondition();
            fail("Argument is required");
        } catch (ConfigException e) {
        }

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator() { return Optional.of("=="); }
            public Optional<Object> getArgument() { return Optional.of(new Double(10)); }
        };
        condition = (DoubleCondition)new ConditionFactory(jruby, column, config).createCondition();
        assertTrue(condition.compare(new Double(10)));

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator() { return Optional.of("=="); }
            public Optional<Object> getArgument() { return Optional.of(new Boolean(true)); }
        };
        try {
            condition = (DoubleCondition)new ConditionFactory(jruby, column, config).createCondition();
            fail("Argument type mismatch");
        } catch (ConfigException e) {
        }
    }

    @Test
    public void testCreateLongCondition() {
        Column column = new Column(0, "column", LONG);
        ConditionConfig  config;
        LongCondition condition;

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator() { return Optional.of("IS NULL"); }
        };
        condition = (LongCondition)new ConditionFactory(jruby, column, config).createCondition();
        assertTrue(condition.compare(null));

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator() { return Optional.of("=="); }
            public Optional<Object> getArgument() { return Optional.absent(); }
        };
        try {
            condition = (LongCondition)new ConditionFactory(jruby, column, config).createCondition();
            fail("Argument is required");
        } catch (ConfigException e) {
        }

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator() { return Optional.of("=="); }
            public Optional<Object> getArgument() { return Optional.of(new Long(10)); }
        };
        condition = (LongCondition)new ConditionFactory(jruby, column, config).createCondition();
        assertTrue(condition.compare(new Long(10)));

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator() { return Optional.of("=="); }
            public Optional<Object> getArgument() { return Optional.of(new Boolean(true)); }
        };
        try {
            condition = (LongCondition)new ConditionFactory(jruby, column, config).createCondition();
            fail("Argument type mismatch");
        } catch (ConfigException e) {
        }
    }

    @Test
    public void testCreateStringCondition() {
        Column column = new Column(0, "column", STRING);
        ConditionConfig  config;
        StringCondition condition;

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator() { return Optional.of("IS NULL"); }
        };
        condition = (StringCondition)new ConditionFactory(jruby, column, config).createCondition();
        assertTrue(condition.compare(null));

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator() { return Optional.of("=="); }
            public Optional<Object> getArgument() { return Optional.absent(); }
        };
        try {
            condition = (StringCondition)new ConditionFactory(jruby, column, config).createCondition();
            fail("Argument is required");
        } catch (ConfigException e) {
        }

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator() { return Optional.of("=="); }
            public Optional<Object> getArgument() { return Optional.of("foo"); }
        };
        condition = (StringCondition)new ConditionFactory(jruby, column, config).createCondition();
        assertTrue(condition.compare("foo"));

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator() { return Optional.of("=="); }
            public Optional<Object> getArgument() { return Optional.of(new Boolean(true)); }
        };
        try {
            condition = (StringCondition)new ConditionFactory(jruby, column, config).createCondition();
            fail("Argument type mismatch");
        } catch (ConfigException e) {
        }
    }

    @Test
    public void testCreateTimestampCondition() {
        Column column = new Column(0, "column", TIMESTAMP);
        ConditionConfig  config;
        TimestampCondition condition;

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator() { return Optional.of("IS NULL"); }
        };
        condition = (TimestampCondition)new ConditionFactory(jruby, column, config).createCondition();
        assertTrue(condition.compare(null));

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator() { return Optional.of("=="); }
            public Optional<Object> getArgument() { return Optional.absent(); }
        };
        try {
            condition = (TimestampCondition)new ConditionFactory(jruby, column, config).createCondition();
            fail("Argument is required");
        } catch (ConfigException e) {
        }

        //ToDo: How to create jruby object correctly?
        //config = new DefaultConditionConfig() {
        //    public Optional<String> getOperator() { return Optional.of("=="); }
        //    public Optional<Object> getArgument() { return Optional.of("2015-07-15"); }
        //    public Optional<String> getFormat()   { return Optional.of("%Y-%m-%d"); }
        //};
        //condition = (TimestampCondition)new ConditionFactory(jruby, column, config).createCondition();

        config = new DefaultConditionConfig() {
            public Optional<String> getOperator() { return Optional.of("=="); }
            public Optional<Object> getArgument() { return Optional.of(new Boolean(true)); }
        };
        try {
            condition = (TimestampCondition)new ConditionFactory(jruby, column, config).createCondition();
            fail("Argument type mismatch");
        } catch (ConfigException e) {
        }
    }
}
