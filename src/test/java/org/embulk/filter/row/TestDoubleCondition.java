package org.embulk.filter.row;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestDoubleCondition
{
    @Test
    public void testIsNull()
    {
        DoubleCondition condition = new DoubleCondition("IS NULL", null, false);
        assertTrue(condition.compare(null));
        assertFalse(condition.compare(new Double(10)));
    }

    @Test
    public void testIsNotNull()
    {
        DoubleCondition condition = new DoubleCondition("IS NOT NULL", null, false);
        assertFalse(condition.compare(null));
        assertTrue(condition.compare(new Double(10)));
    }

    @Test
    public void testEquals()
    {
        DoubleCondition condition = new DoubleCondition("==", new Double(10), false);
        assertFalse(condition.compare(null));
        assertTrue(condition.compare(new Double(10)));
        assertFalse(condition.compare(new Double(11)));

        // Prohibited by Factory
        // DoubleCondition condition = new DoubleCondition("==", null, false);
    }

    @Test
    public void testNotEquals()
    {
        DoubleCondition condition = new DoubleCondition("!=", new Double(10), false);
        assertTrue(condition.compare(null));
        assertFalse(condition.compare(new Double(10)));
        assertTrue(condition.compare(new Double(11)));
    }

    @Test
    public void testGreaterThan()
    {
        DoubleCondition condition = new DoubleCondition(">", new Double(10), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(new Double(10)));
        assertTrue(condition.compare(new Double(11)));
    }

    @Test
    public void testGreaterEqual()
    {
        DoubleCondition condition = new DoubleCondition(">=", new Double(11), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(new Double(10)));
        assertTrue(condition.compare(new Double(11)));
    }

    @Test
    public void testLessThan()
    {
        DoubleCondition condition = new DoubleCondition("<", new Double(11), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(new Double(11)));
        assertTrue(condition.compare(new Double(10)));
    }

    @Test
    public void testLessEqual()
    {
        DoubleCondition condition = new DoubleCondition("<=", new Double(11), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(new Double(12)));
        assertTrue(condition.compare(new Double(11)));
    }

    @Test
    public void testNot()
    {
        DoubleCondition condition = new DoubleCondition("<=", new Double(11), true);
        assertTrue(condition.compare(null));
        assertTrue(condition.compare(new Double(12)));
        assertFalse(condition.compare(new Double(11)));
    }
}
