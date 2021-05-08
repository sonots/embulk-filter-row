package org.embulk.filter.row.condition;

import org.embulk.spi.time.Timestamp;

import java.time.Instant;

public class TimestampCondition implements Condition
{
    private TimestampComparator comparator;

    // @FunctionalInterface
    interface TimestampComparator
    {
        boolean compare(Instant subject);
    }

    public TimestampCondition(final String operator, final Instant argument, final boolean not)
    {
        final TimestampComparator comparator;
        switch (operator.toUpperCase()) {
            case ">":
                comparator = new TimestampComparator() {
                    public boolean compare(Instant subject)
                    {
                        return subject == null ? false : subject.compareTo(argument) > 0;
                    }
                };
                break;
            case ">=":
                comparator = new TimestampComparator() {
                    public boolean compare(Instant subject)
                    {
                        return subject == null ? false : subject.compareTo(argument) >= 0;
                    }
                };
                break;
            case "<":
                comparator = new TimestampComparator() {
                    public boolean compare(Instant subject)
                    {
                        return subject == null ? false : subject.compareTo(argument) < 0;
                    }
                };
                break;
            case "<=":
                comparator = new TimestampComparator() {
                    public boolean compare(Instant subject)
                    {
                        return subject == null ? false : subject.compareTo(argument) <= 0;
                    }
                };
                break;
            case "IS NULL":
                comparator = new TimestampComparator() {
                    public boolean compare(Instant subject)
                    {
                        return subject == null;
                    }
                };
                break;
            case "IS NOT NULL":
                comparator = new TimestampComparator() {
                    public boolean compare(Instant subject)
                    {
                        return subject != null;
                    }
                };
                break;
            case "!=":
                comparator = new TimestampComparator() {
                    public boolean compare(Instant subject)
                    {
                        return subject == null ? true : !subject.equals(argument);
                    }
                };
                break;
            default: // case "==":
                comparator = new TimestampComparator() {
                    public boolean compare(Instant subject)
                    {
                        return subject == null ? false : subject.equals(argument);
                    }
                };
                break;
        }
        this.comparator = comparator;
        if (not) {
            this.comparator = new TimestampComparator() {
                public boolean compare(Instant subject)
                {
                    return !comparator.compare(subject);
                }
            };
        }
    }

    public boolean compare(Instant subject)
    {
        return this.comparator.compare(subject);
    }
}
