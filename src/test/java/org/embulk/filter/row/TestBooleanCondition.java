package org.embulk.filter.row;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestBooleanCondition
{
    @Test
    public void testIsNull()
    {
        BooleanCondition condition = new BooleanCondition("IS NULL", null, false);
        assertTrue(condition.compare(null));
        assertFalse(condition.compare(new Boolean(true)));
        assertFalse(condition.compare(new Boolean(false)));
    }

    @Test
    public void testIsNotNull()
    {
        BooleanCondition condition = new BooleanCondition("IS NOT NULL", null, false);
        assertFalse(condition.compare(null));
        assertTrue(condition.compare(new Boolean(true)));
        assertTrue(condition.compare(new Boolean(false)));
    }

    @Test
    public void testNot()
    {
        BooleanCondition condition = new BooleanCondition("IS NOT NULL", null, true);
        assertTrue(condition.compare(null));
        assertFalse(condition.compare(new Boolean(true)));
        assertFalse(condition.compare(new Boolean(false)));
    }

    @Test
    public void testEquals()
    {
        BooleanCondition condition = new BooleanCondition("==", new Boolean(true), false);
        assertTrue(condition.compare(new Boolean(true)));
        assertFalse(condition.compare(new Boolean(false)));
        assertFalse(condition.compare(null));

        condition = new BooleanCondition("==", new Boolean(false), false);
        assertTrue(condition.compare(new Boolean(false)));
        assertFalse(condition.compare(new Boolean(true)));
        assertFalse(condition.compare(null));

        // Prohibited by Factory
        // BooleanCondition condition = new BooleanCondition("==", null, false);
    }

    @Test
    public void testNotEquals()
    {
        BooleanCondition condition = new BooleanCondition("!=", new Boolean(true), false);
        assertFalse(condition.compare(new Boolean(true)));
        assertTrue(condition.compare(new Boolean(false)));
        assertTrue(condition.compare(null));

        condition = new BooleanCondition("!=", new Boolean(false), false);
        assertFalse(condition.compare(new Boolean(false)));
        assertTrue(condition.compare(new Boolean(true)));
        assertTrue(condition.compare(null));

        // Prohibited by Factory
        // BooleanCondition condition = new BooleanCondition("!=", null, false);
    }
}
