package org.embulk.filter.row;

public class BooleanCondition implements Condition
{
    private BooleanComparator comparator;

    @FunctionalInterface
    interface BooleanComparator {
        boolean compare(boolean subject);
    }

    public BooleanCondition(String operator, boolean argument) {
        switch (operator) {
            case "==":
                this.comparator = (boolean subject) -> { return subject == argument; };
                break;
            case "!=":
                this.comparator = (boolean subject) -> { return subject != argument; };
                break;
            default:
                assert(false);
                break;
        }
    }

    public boolean compare(boolean subject) {
        return this.comparator.compare(subject);
    }
}
