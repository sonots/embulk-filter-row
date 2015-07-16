package org.embulk.filter.row;
import org.embulk.spi.time.Timestamp;

public class TimestampCondition implements Condition
{
    private TimestampComparator comparator;

    @FunctionalInterface
    interface TimestampComparator {
        boolean compare(Timestamp subject);
    }

    public TimestampCondition(String operator, Timestamp argument) {
        switch (operator) {
            case "==":
                this.comparator = (Timestamp subject) -> { return subject.equals(argument); };
                break;
            case "!=":
                this.comparator = (Timestamp subject) -> { return !subject.equals(argument); };
                break;
            case ">":
                this.comparator = (Timestamp subject) -> { return subject.compareTo(argument) > 0; };
                break;
            case ">=":
                this.comparator = (Timestamp subject) -> { return subject.compareTo(argument) >= 0; };
            case "<":
                this.comparator = (Timestamp subject) -> { return subject.compareTo(argument) < 0; };
                break;
            case "<=":
                this.comparator = (Timestamp subject) -> { return subject.compareTo(argument) <= 0; };
                break;
            default:
                assert(false);
                break;
        }
    }

    public boolean compare(Timestamp subject) {
        return this.comparator.compare(subject);
    }
}
