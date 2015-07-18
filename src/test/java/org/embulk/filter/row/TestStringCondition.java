package org.embulk.filter.row;

import org.embulk.filter.row.StringCondition;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestStringCondition
{
    @Test
    public void testIsNull() {
        StringCondition condition = new StringCondition("IS NULL", null, false);
        assertTrue(condition.compare(null));
        assertFalse(condition.compare("foo"));
    }

    @Test
    public void testIsNotNull() {
        StringCondition condition = new StringCondition("IS NOT NULL", null, false);
        assertFalse(condition.compare(null));
        assertTrue(condition.compare("foo"));
    }

    @Test
    public void testEquals() {
        StringCondition condition = new StringCondition("==", "foo", false);
        assertFalse(condition.compare(null));
        assertTrue( condition.compare("foo"));
        assertFalse(condition.compare("bar"));

        // Prohibited by Factory
        // StringCondition condition = new StringCondition("==", null, false);
    }

    @Test
    public void testNotEquals() {
        StringCondition condition = new StringCondition("!=", "foo", false);
        assertTrue( condition.compare(null));
        assertFalse(condition.compare("foo"));
        assertTrue( condition.compare("bar"));
    }

    @Test
    public void testStartWith() {
        StringCondition condition = new StringCondition("start_with", "f", false);
        assertFalse(condition.compare(null));
        assertTrue( condition.compare("foo"));
        assertFalse(condition.compare("bar"));
    }

    @Test
    public void testStartsWith() {
        StringCondition condition = new StringCondition("startsWith", "f", false);
        assertFalse(condition.compare(null));
        assertTrue( condition.compare("foo"));
        assertFalse(condition.compare("bar"));
    }

    @Test
    public void testEndWith() {
        StringCondition condition = new StringCondition("end_with", "o", false);
        assertFalse(condition.compare(null));
        assertTrue( condition.compare("foo"));
        assertFalse(condition.compare("bar"));
    }

    @Test
    public void testEndsWith() {
        StringCondition condition = new StringCondition("endsWith", "o", false);
        assertFalse(condition.compare(null));
        assertTrue( condition.compare("foo"));
        assertFalse(condition.compare("bar"));
    }

    @Test
    public void testInclude() {
        StringCondition condition = new StringCondition("include", "o", false);
        assertFalse(condition.compare(null));
        assertTrue( condition.compare("foo"));
        assertFalse(condition.compare("bar"));
    }

    @Test
    public void testContains() {
        StringCondition condition = new StringCondition("contains", "o", false);
        assertFalse(condition.compare(null));
        assertTrue( condition.compare("foo"));
        assertFalse(condition.compare("bar"));
    }

    @Test
    public void testNot() {
        StringCondition condition = new StringCondition("include", "o", true);
        assertTrue( condition.compare(null));
        assertFalse(condition.compare("foo"));
        assertTrue( condition.compare("bar"));
    }
}
