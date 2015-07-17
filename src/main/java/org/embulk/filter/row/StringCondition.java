package org.embulk.filter.row;

public class StringCondition implements Condition
{
    private StringComparator comparator;

    @FunctionalInterface
    interface StringComparator {
        boolean compare(String subject);
    }

    public StringCondition(String operator, String argument, boolean not) {
        StringComparator comparator;
        switch (operator.toUpperCase()) {
            case "START_WITH":
                comparator = (String subject) -> {
                    return subject == null ? false : subject.startsWith(argument);
                };
                break;
            case "END_WITH":
                comparator = (String subject) -> {
                    return subject == null ? false : subject.endsWith(argument);
                };
                break;
            case "INCLUDE":
                comparator = (String subject) -> {
                    return subject == null ? false : subject.contains(argument);
                };
                break;
            case "IS NULL":
                comparator = (String subject) -> {
                    return subject == null;
                };
                break;
            case "IS NOT NULL":
                comparator = (String subject) -> {
                    return subject != null;
                };
                break;
            case "!=":
                comparator = (String subject) -> {
                    return subject == null ? false : !subject.equals(argument);
                };
                break;
            default: // case "==":
                comparator = (String subject) -> {
                    return subject == null ? false : subject.equals(argument);
                };
                break;
        }
        this.comparator = comparator;
        if (not) this.comparator = (String subject) -> { return !comparator.compare(subject); };
    }

    public boolean compare(String subject) {
        return this.comparator.compare(subject);
    }
}
