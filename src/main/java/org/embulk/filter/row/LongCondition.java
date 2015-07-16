package org.embulk.filter.row;

public class LongCondition implements Condition
{
    private LongComparator comparator;

    @FunctionalInterface
    interface LongComparator {
        boolean compare(long subject);
    }

    public LongCondition(String operator, long argument) {
        switch (operator) {
            case "==":
                this.comparator = (long subject) -> { return subject == argument; };
                break;
            case "!=":
                this.comparator = (long subject) -> { return subject != argument; };
            case ">":
                this.comparator = (long subject) -> { return subject > argument; };
                break;
            case ">=":
                this.comparator = (long subject) -> { return subject >= argument; };
                break;
            case "<":
                this.comparator = (long subject) -> { return subject < argument; };
                break;
            case "<=":
                this.comparator = (long subject) -> { return subject <= argument; };
                break;
            default:
                assert(false);
                break;
        }
    }

    public boolean compare(long subject) {
        return this.comparator.compare(subject);
    }
}
