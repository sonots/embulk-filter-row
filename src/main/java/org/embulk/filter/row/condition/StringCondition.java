package org.embulk.filter.row.condition;

public class StringCondition implements Condition
{
    private StringComparator comparator;

    // @FunctionalInterface
    interface StringComparator
    {
        boolean compare(String subject);
    }

    public StringCondition(final String operator, final String argument, final boolean not)
    {
        final StringComparator comparator;
        switch (operator.toUpperCase()) {
            case "START_WITH":
            case "STARTSWITH":
                comparator = new StringComparator() {
                    public boolean compare(String subject)
                    {
                        return subject == null ? false : subject.startsWith(argument);
                    }
                };
                break;
            case "END_WITH":
            case "ENDSWITH":
                comparator = new StringComparator() {
                    public boolean compare(String subject)
                    {
                        return subject == null ? false : subject.endsWith(argument);
                    }
                };
                break;
            case "INCLUDE":
            case "CONTAINS":
                comparator = new StringComparator() {
                    public boolean compare(String subject)
                    {
                        return subject == null ? false : subject.contains(argument);
                    }
                };
                break;
            case "IS NULL":
                comparator = new StringComparator() {
                    public boolean compare(String subject)
                    {
                        return subject == null;
                    }
                };
                break;
            case "IS NOT NULL":
                comparator = new StringComparator() {
                    public boolean compare(String subject)
                    {
                        return subject != null;
                    }
                };
                break;
            case "!=":
                comparator = new StringComparator() {
                    public boolean compare(String subject)
                    {
                        return subject == null ? true : !subject.equals(argument);
                    }
                };
                break;
            default: // case "==":
                comparator = new StringComparator() {
                    public boolean compare(String subject)
                    {
                        return subject == null ? false : subject.equals(argument);
                    }
                };
                break;
        }
        this.comparator = comparator;
        if (not) {
            this.comparator = new StringComparator() {
                public boolean compare(String subject)
                {
                    return !comparator.compare(subject);
                }
            };
        }
    }

    public boolean compare(String subject)
    {
        return this.comparator.compare(subject);
    }
}
