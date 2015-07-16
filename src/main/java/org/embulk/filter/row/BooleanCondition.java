package org.embulk.filter.row;

public class BooleanCondition implements Condition
{
    private BooleanComparator comparator;

    @FunctionalInterface
    interface BooleanComparator {
        boolean compare(Boolean subject);
    }

    public BooleanCondition(String operator, Boolean argument) {
        switch (operator.toUpperCase()) {
            case "==":
                this.comparator = (Boolean subject) -> { return subject.equals(argument); };
                break;
            case "!=":
                this.comparator = (Boolean subject) -> { return !subject.equals(argument); };
                break;
            case "IS NULL":
                this.comparator = (Boolean subject) -> { return subject == null; };
                break;
            case "IS NOT NULL":
                this.comparator = (Boolean subject) -> { return subject != null; };
                break;
            default:
                assert(false);
                break;
        }
    }

    public boolean compare(Boolean subject) {
        return this.comparator.compare(subject);
    }
}
