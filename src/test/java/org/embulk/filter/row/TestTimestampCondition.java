package org.embulk.filter.row;

import org.embulk.filter.row.TimestampCondition;
import org.junit.Test;
import static org.junit.Assert.*;

import org.embulk.spi.time.Timestamp;

public class TestTimestampCondition
{
    @Test
    public void testIsNull() {
        TimestampCondition condition = new TimestampCondition("IS NULL", null, false);
        assertTrue(condition.compare(null));
        assertFalse(condition.compare(Timestamp.ofEpochSecond(10)));
    }

    @Test
    public void testIsNotNull() {
        TimestampCondition condition = new TimestampCondition("IS NOT NULL", null, false);
        assertFalse(condition.compare(null));
        assertTrue(condition.compare(Timestamp.ofEpochSecond(10)));
    }

    @Test
    public void testEquals() {
        TimestampCondition condition = new TimestampCondition("==", Timestamp.ofEpochSecond(10), false);
        assertFalse(condition.compare(null));
        assertTrue( condition.compare(Timestamp.ofEpochSecond(10)));
        assertFalse(condition.compare(Timestamp.ofEpochSecond(11)));

        // Prohibited by Factory
        // TimestampCondition condition = new TimestampCondition("==", null, false);
    }

    @Test
    public void testNotEquals() {
        TimestampCondition condition = new TimestampCondition("!=", Timestamp.ofEpochSecond(10), false);
        assertTrue( condition.compare(null));
        assertFalse(condition.compare(Timestamp.ofEpochSecond(10)));
        assertTrue( condition.compare(Timestamp.ofEpochSecond(11)));
    }

    @Test
    public void testGreaterThan() {
        TimestampCondition condition = new TimestampCondition(">", Timestamp.ofEpochSecond(10), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(Timestamp.ofEpochSecond(10)));
        assertTrue( condition.compare(Timestamp.ofEpochSecond(11)));
    }

    @Test
    public void testGreaterEqual() {
        TimestampCondition condition = new TimestampCondition(">=", Timestamp.ofEpochSecond(11), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(Timestamp.ofEpochSecond(10)));
        assertTrue( condition.compare(Timestamp.ofEpochSecond(11)));
    }

    @Test
    public void testLessThan() {
        TimestampCondition condition = new TimestampCondition("<", Timestamp.ofEpochSecond(11), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(Timestamp.ofEpochSecond(11)));
        assertTrue( condition.compare(Timestamp.ofEpochSecond(10)));
    }

    @Test
    public void testLessEqual() {
        TimestampCondition condition = new TimestampCondition("<=", Timestamp.ofEpochSecond(11), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(Timestamp.ofEpochSecond(12)));
        assertTrue( condition.compare(Timestamp.ofEpochSecond(11)));
    }

    @Test
    public void testNot() {
        TimestampCondition condition = new TimestampCondition("<=", Timestamp.ofEpochSecond(11), true);
        assertTrue( condition.compare(null));
        assertTrue( condition.compare(Timestamp.ofEpochSecond(12)));
        assertFalse(condition.compare(Timestamp.ofEpochSecond(11)));
    }
}
