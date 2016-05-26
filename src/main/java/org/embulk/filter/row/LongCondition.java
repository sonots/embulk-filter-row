package org.embulk.filter.row;


public class LongCondition implements Condition
{
    private LongComparator comparator;

    // @FunctionalInterface
    interface LongComparator
    {
        boolean compare(Long subject);
    }

    public LongCondition(final String operator, final Double argument, final boolean not)
    {
        final LongComparator comparator;
        switch (operator.toUpperCase()) {
            case "IS NULL":
                comparator = new LongComparator() {
                    public boolean compare(Long subject)
                    {
                        return subject == null;
                    }
                };
                break;
            case "IS NOT NULL":
                comparator = new LongComparator() {
                    public boolean compare(Long subject)
                    {
                        return subject != null;
                    }
                };
                break;
            case ">":
                comparator = new LongComparator() {
                    public boolean compare(Long subject)
                    {
                        return subject == null ? false : new Double(subject).compareTo(argument) > 0;
                    }
                };
                break;
            case ">=":
                comparator = new LongComparator() {
                    public boolean compare(Long subject)
                    {
                        return subject == null ? false : new Double(subject).compareTo(argument) >= 0;
                    }
                };
                break;
            case "<":
                comparator = new LongComparator() {
                    public boolean compare(Long subject)
                    {
                        return subject == null ? false : new Double(subject).compareTo(argument) < 0;
                    }
                };
                break;
            case "<=":
                comparator = new LongComparator() {
                    public boolean compare(Long subject)
                    {
                        return subject == null ? false : new Double(subject).compareTo(argument) <= 0;
                    }
                };
                break;
            case "!=":
                comparator = new LongComparator() {
                    public boolean compare(Long subject)
                    {
                        return subject == null ? true : !new Double(subject).equals(argument);
                    }
                };
                break;
            default: // case "==":
                comparator = new LongComparator() {
                    public boolean compare(Long subject)
                    {
                        return subject == null ? false : new Double(subject).equals(argument);
                    }
                };
                break;
        }
        this.comparator = comparator;
        if (not) {
            this.comparator = new LongComparator() {
                public boolean compare(Long subject)
                {
                    return !comparator.compare(subject);
                }
            };
        }
    }

    public boolean compare(Long subject)
    {
        return this.comparator.compare(subject);
    }
}
