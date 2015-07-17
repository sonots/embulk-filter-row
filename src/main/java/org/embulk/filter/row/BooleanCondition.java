package org.embulk.filter.row;

public class BooleanCondition implements Condition
{
    private BooleanComparator comparator;

    @FunctionalInterface
    interface BooleanComparator {
        boolean compare(Boolean subject);
    }

    public BooleanCondition(String operator, Boolean argument, boolean not) {
        BooleanComparator comparator;
        switch (operator.toUpperCase()) {
            case "IS NULL":
                comparator = (Boolean subject) -> {
                    return subject == null;
                };
                break;
            case "IS NOT NULL":
                comparator = (Boolean subject) -> {
                    return subject != null;
                };
                break;
            case "!=":
                comparator = (Boolean subject) -> {
                    return subject == null ? true : !subject.equals(argument);
                };
                break;
            default: // case "==":
                comparator = (Boolean subject) -> {
                    return subject == null ? false : subject.equals(argument);
                };
                break;
        }
        this.comparator = comparator;
        if (not) this.comparator = (Boolean subject) -> { return !comparator.compare(subject); };
    }

    public boolean compare(Boolean subject) {
        return this.comparator.compare(subject);
    }
}
