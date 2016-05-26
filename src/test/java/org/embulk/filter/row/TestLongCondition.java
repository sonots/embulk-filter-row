package org.embulk.filter.row;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestLongCondition
{
    @Test
    public void testIsNull()
    {
        LongCondition condition = new LongCondition("IS NULL", null, false);
        assertTrue(condition.compare(null));
        assertFalse(condition.compare(new Long(10)));
    }

    @Test
    public void testIsNotNull()
    {
        LongCondition condition = new LongCondition("IS NOT NULL", null, false);
        assertFalse(condition.compare(null));
        assertTrue(condition.compare(new Long(10)));
    }

    @Test
    public void testEquals()
    {
        LongCondition condition = new LongCondition("==", new Double(10), false);
        assertFalse(condition.compare(null));
        assertTrue(condition.compare(new Long(10)));
        assertFalse(condition.compare(new Long(11)));

        // Prohibited by Factory
        // LongCondition condition = new LongCondition("==", null, false);
    }

    @Test
    public void testNotEquals()
    {
        LongCondition condition = new LongCondition("!=", new Double(10), false);
        assertTrue(condition.compare(null));
        assertFalse(condition.compare(new Long(10)));
        assertTrue(condition.compare(new Long(11)));
    }

    @Test
    public void testGreaterThan()
    {
        LongCondition condition = new LongCondition(">", new Double(10), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(new Long(10)));
        assertTrue(condition.compare(new Long(11)));
    }

    @Test
    public void testGreaterEqual()
    {
        LongCondition condition = new LongCondition(">=", new Double(11), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(new Long(10)));
        assertTrue(condition.compare(new Long(11)));
    }

    @Test
    public void testLessThan()
    {
        LongCondition condition = new LongCondition("<", new Double(11), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(new Long(11)));
        assertTrue(condition.compare(new Long(10)));
    }

    @Test
    public void testLessEqual()
    {
        LongCondition condition = new LongCondition("<=", new Double(11), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(new Long(12)));
        assertTrue(condition.compare(new Long(11)));
    }

    @Test
    public void testNot()
    {
        LongCondition condition = new LongCondition("<=", new Double(11), true);
        assertTrue(condition.compare(null));
        assertTrue(condition.compare(new Long(12)));
        assertFalse(condition.compare(new Long(11)));
    }

    @Test
    public void testDoubleOperatorEquals()
    {
        LongCondition condition = new LongCondition("==", new Double(10.0), false);
        assertFalse(condition.compare(null));
        assertTrue(condition.compare(new Long(10)));
        assertFalse(condition.compare(new Long(11)));
    }

    @Test
    public void testDoubleOperatorNotEquals()
    {
        LongCondition condition = new LongCondition("!=", new Double(10.0), false);
        assertTrue(condition.compare(null));
        assertFalse(condition.compare(new Long(10)));
        assertTrue(condition.compare(new Long(11)));
    }

    @Test
    public void testDoubleOperatorGreaterThan()
    {
        LongCondition condition = new LongCondition(">", new Double(10.0), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(new Long(10)));
        assertTrue(condition.compare(new Long(11)));
    }

    @Test
    public void testDoubleOperatorGreaterEqual()
    {
        LongCondition condition = new LongCondition(">=", new Double(11.0), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(new Long(10)));
        assertTrue(condition.compare(new Long(11)));
    }

    @Test
    public void testDoubleOperatorLessThan()
    {
        LongCondition condition = new LongCondition("<", new Double(11.0), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(new Long(11)));
        assertTrue(condition.compare(new Long(10)));
    }

    @Test
    public void testDoubleOperatorLessEqual()
    {
        LongCondition condition = new LongCondition("<=", new Double(11.0), false);
        assertFalse(condition.compare(null));
        assertFalse(condition.compare(new Long(12)));
        assertTrue(condition.compare(new Long(11)));
    }

    @Test
    public void testDoubleOperatorNot()
    {
        LongCondition condition = new LongCondition("<=", new Double(11.0), true);
        assertTrue(condition.compare(null));
        assertTrue(condition.compare(new Long(12)));
        assertFalse(condition.compare(new Long(11)));
    }
}
