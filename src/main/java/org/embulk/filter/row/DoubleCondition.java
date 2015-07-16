package org.embulk.filter.row;

public class DoubleCondition implements Condition
{
    private DoubleComparator comparator;

    @FunctionalInterface
    interface DoubleComparator {
        boolean compare(double subject);
    }

    public DoubleCondition(String operator, double argument) {
        switch (operator) {
            case "==":
                this.comparator = (double subject) -> { return subject == argument; };
                break;
            case "!=":
                this.comparator = (double subject) -> { return subject != argument; };
            case ">":
                this.comparator = (double subject) -> { return subject > argument; };
                break;
            case ">=":
                this.comparator = (double subject) -> { return subject >= argument; };
                break;
            case "<":
                this.comparator = (double subject) -> { return subject < argument; };
                break;
            case "<=":
                this.comparator = (double subject) -> { return subject <= argument; };
                break;
            default:
                assert(false);
                break;
        }
    }

    public boolean compare(double subject) {
        return this.comparator.compare(subject);
    }
}
