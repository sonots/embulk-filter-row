package org.embulk.filter.row;
import org.embulk.spi.time.Timestamp;

public class TimestampCondition implements Condition
{
    private TimestampComparator comparator;

    // @FunctionalInterface
    interface TimestampComparator {
        boolean compare(Timestamp subject);
    }

    public TimestampCondition(final String operator, final Timestamp argument, final boolean not) {
        final TimestampComparator comparator;
        switch (operator.toUpperCase()) {
            case ">":
                comparator = new TimestampComparator() {
                    public boolean compare(Timestamp subject) {
                        return subject == null ? false : subject.compareTo(argument) > 0;
                    }
                };
                break;
            case ">=":
                comparator = new TimestampComparator() {
                    public boolean compare(Timestamp subject) {
                        return subject == null ? false : subject.compareTo(argument) >= 0;
                    }
                };
                break;
            case "<":
                comparator = new TimestampComparator() {
                    public boolean compare(Timestamp subject) {
                        return subject == null ? false : subject.compareTo(argument) < 0;
                    }
                };
                break;
            case "<=":
                comparator = new TimestampComparator() {
                    public boolean compare(Timestamp subject) {
                        return subject == null ? false : subject.compareTo(argument) <= 0;
                    }
                };
                break;
            case "IS NULL":
                comparator = new TimestampComparator() {
                    public boolean compare(Timestamp subject) {
                        return subject == null;
                    }
                };
                break;
            case "IS NOT NULL":
                comparator = new TimestampComparator() {
                    public boolean compare(Timestamp subject) {
                        return subject != null;
                    }
                };
                break;
            case "!=":
                comparator = new TimestampComparator() {
                    public boolean compare(Timestamp subject) {
                        return subject == null ? true : !subject.equals(argument);
                    }
                };
                break;
            default: // case "==":
                comparator = new TimestampComparator() {
                    public boolean compare(Timestamp subject) {
                        return subject == null ? false : subject.equals(argument);
                    }
                };
                break;
        }
        this.comparator = comparator;
        if (not) {
            this.comparator = new TimestampComparator() {
                public boolean compare(Timestamp subject) {
                    return !comparator.compare(subject);
                }
            };
        }
    }

    public boolean compare(Timestamp subject) {
        return this.comparator.compare(subject);
    }
}
