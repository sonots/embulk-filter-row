package org.embulk.filter.row;

public class DoubleCondition implements Condition
{
    private DoubleComparator comparator;

    // @FunctionalInterface
    interface DoubleComparator {
        boolean compare(Double subject);
    }

    public DoubleCondition(final String operator, final Double argument, final boolean not) {
        final DoubleComparator comparator;
        switch (operator.toUpperCase()) {
            case "IS NULL":
                comparator = new DoubleComparator() {
                    public boolean compare(Double subject) {
                        return subject == null;
                    }
                };
                break;
            case "IS NOT NULL":
                comparator = new DoubleComparator() {
                    public boolean compare(Double subject) {
                        return subject != null;
                    }
                };
                break;
            case ">":
                comparator = new DoubleComparator() {
                    public boolean compare(Double subject) {
                        return subject == null ? false : subject.compareTo(argument) > 0;
                    }
                };
                break;
            case ">=":
                comparator = new DoubleComparator() {
                    public boolean compare(Double subject) {
                        return subject == null ? false : subject.compareTo(argument) >= 0;
                    }
                };
                break;
            case "<":
                comparator = new DoubleComparator() {
                    public boolean compare(Double subject) {
                        return subject == null ? false : subject.compareTo(argument) < 0;
                    }
                };
                break;
            case "<=":
                comparator = new DoubleComparator() {
                    public boolean compare(Double subject) {
                        return subject == null ? false : subject.compareTo(argument) <= 0;
                    }
                };
                break;
            case "!=":
                comparator = new DoubleComparator() {
                    public boolean compare(Double subject) {
                        return subject == null ? true : !subject.equals(argument);
                    }
                };
                break;
            default: // case "==":
                comparator = new DoubleComparator() {
                    public boolean compare(Double subject) {
                        return subject == null ? false : subject.equals(argument);
                    }
                };
                break;
        }
        this.comparator = comparator;
        if (not) {
            this.comparator = new DoubleComparator() {
                public boolean compare(Double subject) {
                    return !comparator.compare(subject);
                }
            };
        }
    }

    public boolean compare(Double subject) {
        return this.comparator.compare(subject);
    }
}
