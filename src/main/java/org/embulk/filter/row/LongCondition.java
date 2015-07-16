package org.embulk.filter.row;

public class LongCondition implements Condition
{
    private LongComparator comparator;

    @FunctionalInterface
    interface LongComparator {
        boolean compare(Long subject);
    }

    public LongCondition(String operator, Long argument) {
        switch (operator.toUpperCase()) {
            case "==":
                this.comparator = (Long subject) -> { return subject.equals(argument); };
                break;
            case "!=":
                this.comparator = (Long subject) -> { return !subject.equals(argument); };
                break;
            case ">":
                this.comparator = (Long subject) -> { return subject.compareTo(argument) > 0; };
                break;
            case ">=":
                this.comparator = (Long subject) -> { return subject.compareTo(argument) >= 0; };
                break;
            case "<":
                this.comparator = (Long subject) -> { return subject.compareTo(argument) < 0; };
                break;
            case "<=":
                this.comparator = (Long subject) -> { return subject.compareTo(argument) <= 0; };
                break;
            case "IS NULL":
                this.comparator = (Long subject) -> { return subject == null; };
                break;
            case "IS NOT NULL":
                this.comparator = (Long subject) -> { return subject != null; };
                break;
            default:
                assert(false);
                break;
        }
    }

    public boolean compare(Long subject) {
        return this.comparator.compare(subject);
    }
}
