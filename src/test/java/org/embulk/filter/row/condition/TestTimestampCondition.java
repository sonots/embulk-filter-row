package org.embulk.filter.row.condition;

import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestTimestampCondition
{
    @Test
    public void testIsNull()
    {
        TimestampCondition condition = new TimestampCondition("IS NULL", null, false);
        assertTrue(condition.compare(null));
        assertFalse(condition.compare(Instant.ofEpochSecond(10)));
    }

    @Test
    public void testIsNotNull()
    {
        TimestampCondition condition = new TimestampCondition("IS NOT NULL", null, false);
        assertFalse(condition.compare(null));
        assertTrue(condition.compare(Instant.ofEpochSecond(10)));
    }

    @Test
    public void testEquals()
    {
        TimestampCondition condition = new TimestampCondition("==", Instant.ofEpochSecond(10), false);
        assertFalse(condition.compare(null));
        assertTrue(condition.compare(Instant.ofEpochSecond(10)));
        assertFalse(condition.compare(Instant.ofEpochSecond(11)));

        // Prohibited by Factory
        // TimestampCondition condition = new TimestampCondition("==", null, false);
    }

    @Test
    public void testNotEquals()
    {
        TimestampCondition condition = new TimestampCondition("!=", Instant.ofEpochSecond(10), false);
        assertTrue(condition.compare(null));
        assertFalse(condition.compare(Instant.ofEpochSecond(10)));
        assertTrue(condition.compare(Instant.ofEpochSecond(11)));
    }

    @Test
    public void testGreaterThan()
    {
        TimestampCondition condition = new TimestampCondition(">", Instant.ofEpochSecond(10), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(Instant.ofEpochSecond(10)));
        assertTrue(condition.compare(Instant.ofEpochSecond(11)));
    }

    @Test
    public void testGreaterEqual()
    {
        TimestampCondition condition = new TimestampCondition(">=", Instant.ofEpochSecond(11), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(Instant.ofEpochSecond(10)));
        assertTrue(condition.compare(Instant.ofEpochSecond(11)));
    }

    @Test
    public void testLessThan()
    {
        TimestampCondition condition = new TimestampCondition("<", Instant.ofEpochSecond(11), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(Instant.ofEpochSecond(11)));
        assertTrue(condition.compare(Instant.ofEpochSecond(10)));
    }

    @Test
    public void testLessEqual()
    {
        TimestampCondition condition = new TimestampCondition("<=", Instant.ofEpochSecond(11), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(Instant.ofEpochSecond(12)));
        assertTrue(condition.compare(Instant.ofEpochSecond(11)));
    }

    @Test
    public void testNot()
    {
        TimestampCondition condition = new TimestampCondition("<=", Instant.ofEpochSecond(11), true);
        assertTrue(condition.compare(null));
        assertTrue(condition.compare(Instant.ofEpochSecond(12)));
        assertFalse(condition.compare(Instant.ofEpochSecond(11)));
    }
}
