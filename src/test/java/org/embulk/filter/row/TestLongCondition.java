package org.embulk.filter.row;

import org.embulk.filter.row.LongCondition;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestLongCondition
{
    @Test
    public void testIsNull() {
        LongCondition condition = new LongCondition("IS NULL", null, false);
        assertTrue(condition.compare(null));
        assertFalse(condition.compare(new Long(10)));
    }

    @Test
    public void testIsNotNull() {
        LongCondition condition = new LongCondition("IS NOT NULL", null, false);
        assertFalse(condition.compare(null));
        assertTrue(condition.compare(new Long(10)));
    }

    @Test
    public void testEquals() {
        LongCondition condition = new LongCondition("==", new Long(10), false);
        assertFalse(condition.compare(null));
        assertTrue( condition.compare(new Long(10)));
        assertFalse(condition.compare(new Long(11)));

        // Prohibited by Factory
        // LongCondition condition = new LongCondition("==", null, false);
    }

    @Test
    public void testNotEquals() {
        LongCondition condition = new LongCondition("!=", new Long(10), false);
        assertTrue( condition.compare(null));
        assertFalse(condition.compare(new Long(10)));
        assertTrue( condition.compare(new Long(11)));
    }

    @Test
    public void testGreaterThan() {
        LongCondition condition = new LongCondition(">", new Long(10), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(new Long(10)));
        assertTrue( condition.compare(new Long(11)));
    }

    @Test
    public void testGreaterEqual() {
        LongCondition condition = new LongCondition(">=", new Long(11), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(new Long(10)));
        assertTrue( condition.compare(new Long(11)));
    }

    @Test
    public void testLessThan() {
        LongCondition condition = new LongCondition("<", new Long(11), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(new Long(11)));
        assertTrue( condition.compare(new Long(10)));
    }

    @Test
    public void testLessEqual() {
        LongCondition condition = new LongCondition("<=", new Long(11), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(new Long(12)));
        assertTrue( condition.compare(new Long(11)));
    }

    @Test
    public void testNot() {
        LongCondition condition = new LongCondition("<=", new Long(11), true);
        assertTrue( condition.compare(null));
        assertTrue( condition.compare(new Long(12)));
        assertFalse(condition.compare(new Long(11)));
    }
}
