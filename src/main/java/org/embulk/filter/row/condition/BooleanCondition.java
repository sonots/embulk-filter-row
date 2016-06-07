package org.embulk.filter.row.condition;

public class BooleanCondition implements Condition
{
    private BooleanComparator comparator;

    // @FunctionalInterface
    interface BooleanComparator
    {
        boolean compare(Boolean subject);
    }

    public BooleanCondition(final String operator, final Boolean argument, final boolean not)
    {
        final BooleanComparator comparator;
        switch (operator.toUpperCase()) {
            case "IS NULL":
                comparator = new BooleanComparator() {
                    public boolean compare(Boolean subject)
                    {
                        return subject == null;
                    }
                };
                break;
            case "IS NOT NULL":
                comparator = new BooleanComparator() {
                    public boolean compare(Boolean subject)
                    {
                        return subject != null;
                    }
                };
                break;
            case "!=":
                comparator = new BooleanComparator() {
                    public boolean compare(Boolean subject)
                    {
                        return subject == null ? true : !subject.equals(argument);
                    }
                };
                break;
            default: // case "==":
                comparator = new BooleanComparator() {
                    public boolean compare(Boolean subject)
                    {
                        return subject == null ? false : subject.equals(argument);
                    }
                };
                break;
        }
        this.comparator = comparator;
        if (not) {
            this.comparator = new BooleanComparator() {
                public boolean compare(Boolean subject)
                {
                    return !comparator.compare(subject);
                }
            };
        }
    }

    public boolean compare(Boolean subject)
    {
        return this.comparator.compare(subject);
    }
}
