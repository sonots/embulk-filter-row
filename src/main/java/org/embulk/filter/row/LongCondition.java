package org.embulk.filter.row;

public class LongCondition implements Condition
{
    private LongComparator comparator;

    @FunctionalInterface
    interface LongComparator {
        boolean compare(Long subject);
    }

    public LongCondition(String operator, Long argument, boolean not) {
        LongComparator comparator;
        switch (operator.toUpperCase()) {
            case "IS NULL":
                comparator = (Long subject) -> {
                    return subject == null;
                };
                break;
            case "IS NOT NULL":
                comparator = (Long subject) -> {
                    return subject != null;
                };
                break;
            case ">":
                comparator = (Long subject) -> {
                    return subject == null ? false : subject.compareTo(argument) > 0;
                };
                break;
            case ">=":
                comparator = (Long subject) -> {
                    return subject == null ? false : subject.compareTo(argument) >= 0;
                };
                break;
            case "<":
                comparator = (Long subject) -> {
                    return subject == null ? false : subject.compareTo(argument) < 0;
                };
                break;
            case "<=":
                comparator = (Long subject) -> {
                    return subject == null ? false : subject.compareTo(argument) <= 0; 
                };
                break;
            case "!=":
                comparator = (Long subject) -> {
                    return subject == null ? true : !subject.equals(argument);
                };
                break;
            default: // case "==":
                comparator = (Long subject) -> {
                    return subject == null ? false : subject.equals(argument);
                };
                break;
        }
        this.comparator = comparator;
        if (not) this.comparator = (Long subject) -> { return !comparator.compare(subject); };
    }

    public boolean compare(Long subject) {
        return this.comparator.compare(subject);
    }
}
