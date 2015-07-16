package org.embulk.filter.row;

public class DoubleCondition implements Condition
{
    private DoubleComparator comparator;

    @FunctionalInterface
    interface DoubleComparator {
        boolean compare(Double subject);
    }

    public DoubleCondition(String operator, Double argument) {
        switch (operator.toUpperCase()) {
            case "==":
                this.comparator = (Double subject) -> { return subject.equals(argument); };
                break;
            case "!=":
                this.comparator = (Double subject) -> { return !subject.equals(argument); };
                break;
            case ">":
                this.comparator = (Double subject) -> { return subject.compareTo(argument) > 0; };
                break;
            case ">=":
                this.comparator = (Double subject) -> { return subject.compareTo(argument) >= 0; };
                break;
            case "<":
                this.comparator = (Double subject) -> { return subject.compareTo(argument) < 0; };
                break;
            case "<=":
                this.comparator = (Double subject) -> { return subject.compareTo(argument) <= 0; };
                break;
            case "IS NULL":
                this.comparator = (Double subject) -> { return subject == null; };
                break;
            case "IS NOT NULL":
                this.comparator = (Double subject) -> { return subject != null; };
                break;
            default:
                assert(false);
                break;
        }
    }

    public boolean compare(Double subject) {
        return this.comparator.compare(subject);
    }
}
