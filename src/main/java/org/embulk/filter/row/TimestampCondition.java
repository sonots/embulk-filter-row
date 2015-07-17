package org.embulk.filter.row;
import org.embulk.spi.time.Timestamp;

public class TimestampCondition implements Condition
{
    private TimestampComparator comparator;

    @FunctionalInterface
    interface TimestampComparator {
        boolean compare(Timestamp subject);
    }

    public TimestampCondition(String operator, Timestamp argument, boolean not) {
        TimestampComparator comparator;
        switch (operator.toUpperCase()) {
            case ">":
                comparator = (Timestamp subject) -> {
                    return subject == null ? false : subject.compareTo(argument) > 0;
                };
                break;
            case ">=":
                comparator = (Timestamp subject) -> {
                    return subject == null ? false : subject.compareTo(argument) >= 0;
                };
                break;
            case "<":
                comparator = (Timestamp subject) -> {
                    return subject == null ? false : subject.compareTo(argument) < 0;
                };
                break;
            case "<=":
                comparator = (Timestamp subject) -> {
                    return subject == null ? false : subject.compareTo(argument) <= 0;
                };
                break;
            case "IS NULL":
                comparator = (Timestamp subject) -> {
                    return subject == null;
                };
                break;
            case "IS NOT NULL":
                comparator = (Timestamp subject) -> {
                    return subject != null;
                };
                break;
            case "!=":
                comparator = (Timestamp subject) -> {
                    return subject == null ? true : !subject.equals(argument);
                };
                break;
            default: // case "==":
                comparator = (Timestamp subject) -> {
                    return subject == null ? false : subject.equals(argument);
                };
                break;
        }
        this.comparator = comparator;
        if (not) this.comparator = (Timestamp subject) -> { return !comparator.compare(subject); };
    }

    public boolean compare(Timestamp subject) {
        return this.comparator.compare(subject);
    }
}
