package org.embulk.filter.row;

public class StringCondition implements Condition
{
    private StringComparator comparator;

    @FunctionalInterface
    interface StringComparator {
        boolean compare(String subject);
    }

    public StringCondition(String operator, String argument) {
        switch (operator) {
            case "==":
                this.comparator = (String subject) -> { return subject.equals(argument); };
                break;
            case "!=":
                this.comparator = (String subject) -> { return !subject.equals(argument); };
                break;
            case "start_with":
                this.comparator = (String subject) -> { return subject.startsWith(argument); };
                break;
            case "end_with":
                this.comparator = (String subject) -> { return subject.endsWith(argument); };
                break;
            case "include":
                this.comparator = (String subject) -> { return subject.contains(argument); };
                break;
            default:
                assert(false);
                break;
        }
    }

    public boolean compare(String subject) {
        return this.comparator.compare(subject);
    }
}
